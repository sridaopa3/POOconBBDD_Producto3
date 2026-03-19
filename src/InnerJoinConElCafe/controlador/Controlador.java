package InnerJoinConElCafe.controlador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import InnerJoinConElCafe.excepciones.*;
import InnerJoinConElCafe.modelo.*;
import InnerJoinConElCafe.modelo.dao.*;

public class Controlador {
    private final ArticuloDAO articuloDAO;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;


    public Controlador() {
        this.articuloDAO = DAOFactory.getArticuloDAO();
        this.clienteDAO = DAOFactory.getClienteDAO();
        this.pedidoDAO = DAOFactory.getPedidoDAO();
    }


    //Articulos

    //Añadir articulo
    public Resultado<String> añadirArticulo(String codigo, String desc, double precio, double envio, int tiempo) {
        try {
            // 1. VALIDACIÓN DE DATOS
            if (precio <= 0) {
                throw new ValidacionDatosException("El precio del artículo debe ser mayor que 0.");
            }
            if (tiempo <= 0) {
                throw new ValidacionDatosException("El tiempo de preparación debe ser al menos de 1 minuto.");
            }

            // 2. Validación de duplicados
            if (articuloDAO.buscarPorCodigo(codigo) != null) {
                throw new ArticuloException("El código '" + codigo + "' ya existe.");
            }

            articuloDAO.insertar(new Articulo(codigo, desc, precio, envio, tiempo));
            return new Resultado<>(codigo, "Artículo añadido correctamente.");

        } catch (ValidacionDatosException | ArticuloException e) {
            // Capturamos cualquiera de nuestros errores de lógica
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error inesperado: " + e.getMessage());
        }
    }

    //Mostrar articulo
    public Resultado<List<Articulo>> obtenerArticulos() {
        try {
            List<Articulo> lista = articuloDAO.obtenerTodos();
            if (lista.isEmpty()) throw new ListaVaciaException("No hay artículos registrados.");
            return new Resultado<>(lista, "Lista de artículos obtenida con éxito.");

        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (SQLException e) {
            return new Resultado<>("Error de base de datos: " + e.getMessage());
        }
    }


    //Clientes

    //Añadir cliente
    public Resultado<String> añadirCliente(String nombre, String dom, String nif, String email, int tipo) {
        
        try {
            if (clienteDAO.buscarPorNif(nif) != null) {
                throw new ClienteException("Ya existe un cliente con NIF: " + nif);
            }

            // 2. Si no existe, procedemos con la creación
            Cliente nuevoCliente;
            if (tipo == 2) {
                nuevoCliente = new ClientePremium(nombre, dom, nif, email);
            } else {
                nuevoCliente = new ClienteEstandar(nombre, dom, nif, email);
            }

            clienteDAO.insertar(nuevoCliente);
            return new Resultado<>(nif, "Cliente registrado correctamente.");

        } catch (ClienteException e) {
            // 3. Capturamos ESPECÍFICAMENTE nuestra excepción y devolvemos su mensaje
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            // 4. Capturamos cualquier otro error inesperado (opcional, por seguridad)
            return new Resultado<>("Error inesperado: " + e.getMessage());
        }
    }

    //Mostrar clientes
    public Resultado<List<Cliente>> obtenerClientes(int opcion) {
        try {
            List<Cliente> lista = switch (opcion) {
                case 2 -> clienteDAO.obtenerEstandar();
                case 3 -> clienteDAO.obtenerPremium();
                default -> clienteDAO.obtenerTodos();
            };

            if (lista.isEmpty()) throw new ListaVaciaException("No hay clientes del tipo seleccionado.");
            return new Resultado<>(lista, "Lista obtenida con éxito.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (SQLException e) {
            return new Resultado<>("Error de base de datos: " + e.getMessage());
        }
    }

    //Pedidos

    //Metodos para buscar cliente y articulo
    public Articulo buscarArticulo(String codigo) throws DatoNoEncontradoException {
        try {
            Articulo a = articuloDAO.buscarPorCodigo(codigo);
            if (a == null) throw new DatoNoEncontradoException("Artículo con código '" + codigo + "' no existe.");
            return a;
        } catch (SQLException e) {
            throw new DatoNoEncontradoException("Error de base de datos: " + e.getMessage());
        }
    }

    public Cliente buscarCliente(String nif) throws DatoNoEncontradoException {
        try {
            Cliente c = clienteDAO.buscarPorNif(nif);
            if (c == null) throw new DatoNoEncontradoException("Cliente con NIF '" + nif + "' no existe.");
            return c;
        } catch (SQLException e) {
            throw new DatoNoEncontradoException("Error de base de datos: " + e.getMessage());
        }
    }
    //Nuevo metodo para generar el numero de pedido automaticamente
    public int generarNuevoNumeroPedido() {
            try {
                return pedidoDAO.obtenerTodos().size() + 1;
            } catch (SQLException e) {
                return 1;
            }
    }


    //Añadir pedido
    public Resultado<String> añadirPedido(int num, String nif, String codigo, int cant) {
        try {
            // VALIDACIÓN DE DATOS
            if (pedidoDAO.buscarPorNumero(num) != null) {
                throw new PedidoException("Ya existe un pedido con el número " + num);
            }
            if (cant <= 0) {
                throw new ValidacionDatosException("La cantidad del pedido debe ser al menos de 1 unidad.");
            }

            Cliente cliente = buscarCliente(nif);
            Articulo articulo = buscarArticulo(codigo);

            pedidoDAO.insertar(new Pedido(num, cant, LocalDateTime.now(), articulo, cliente));
            return new Resultado<>(String.valueOf(num), "Pedido creado con éxito.");

        } catch (DatoNoEncontradoException | ValidacionDatosException | PedidoException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error: " + e.getMessage());
        }
    }

    //Mostrar pedido
    public Resultado<List<Pedido>> obtenerPedidosFiltrados(char opcionEstado, String nif) {
        try {
            List<Pedido> filtrados = switch (opcionEstado) {
                case '2' -> pedidoDAO.obtenerPendientes();
                case '3' -> pedidoDAO.obtenerEnviados();
                default -> nif != null ? pedidoDAO.obtenerPorClientes(nif) : pedidoDAO.obtenerTodos();
            };

            if (filtrados.isEmpty()) {
                throw new ListaVaciaException("No se encontraron pedidos con los criterios seleccionados.");
            }

            return new Resultado<>(filtrados, "Búsqueda finalizada con éxito.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (SQLException e) {
            return new Resultado<>("Error de base de datos: " + e.getMessage());
        }
    }


    //Cancelar Pedido
    public Resultado<String> cancelarPedido(int numeroPedido) {
        try {
            Pedido pedido = pedidoDAO.buscarPorNumero(numeroPedido);

            // 2. Si no existe, lanzamos nuestra PedidoException
            if (pedido == null) {
                throw new PedidoException("No se ha encontrado ningún pedido con el número: " + numeroPedido);
            }

            // 3. Intentamos cancelar. 
            // Si no puede cancelarse, Pedido.java lanzará la excepción aquí mismo.
            pedido.cancelar(); 

            // 4. Si la línea anterior no lanzó excepción, procedemos a borrarlo de la lista
            pedidoDAO.eliminar(numeroPedido);
        
            return new Resultado<>(null, "El pedido ha sido cancelado y eliminado del sistema.");

        } catch (PedidoException e) {
            // Capturamos el error específico (tiempo agotado o no encontrado)
            return new Resultado<>(e.getMessage());
        } catch (SQLException e) {
            return new Resultado<>("Error de base de datos: " + e.getMessage());
        }
        catch (Exception e) {
            // Capturamos cualquier otro error inesperado
            return new Resultado<>("Error inesperado al cancelar: " + e.getMessage());
        }
    }
}