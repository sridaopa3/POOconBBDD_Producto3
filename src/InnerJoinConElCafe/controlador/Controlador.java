package InnerJoinConElCafe.controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import InnerJoinConElCafe.excepciones.*;
import InnerJoinConElCafe.modelo.*;

public class Controlador {
    private Datos datos;

    public Controlador() {
        this.datos = new Datos();
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
            for (Articulo a : datos.getListaArticulos()) {
                if (a.getCodigo().equalsIgnoreCase(codigo)) {
                    throw new ArticuloException("El código '" + codigo + "' ya existe.");
                }
            }

            Articulo nuevo = new Articulo(codigo, desc, precio, envio, tiempo);
            datos.addArticulo(nuevo);
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
            List<Articulo> articulos = datos.getListaArticulos();
            if (articulos.isEmpty()) {
                throw new ListaVaciaException("No hay artículos registrados en el catálogo.");
            }
            return new Resultado<>(articulos, "Lista de artículos obtenida con éxito.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
        return new Resultado<>("Error de base de datos:" + e.getMessage());
    }
    }



    //Clientes

    //Añadir cliente
    public Resultado<String> añadirCliente(String nombre, String dom, String nif, String email, int tipo) {
        
        try {
            for (Cliente c : datos.getListaClientes()) {
                if (c.getNif().equalsIgnoreCase(nif)) {
                    // LANZAMOS nuestra excepción personalizada
                    throw new ClienteException("Ya existe un cliente con el NIF: " + nif);
                }
            }

            // 2. Si no existe, procedemos con la creación
            Cliente nuevoCliente;
            if (tipo == 2) {
                nuevoCliente = new ClientePremium(nombre, dom, nif, email);
            } else {
                nuevoCliente = new ClienteEstandar(nombre, dom, nif, email);
            }
        
            datos.addCliente(nuevoCliente);
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
            List<Cliente> todos = datos.getListaClientes();
            List<Cliente> filtrados = new ArrayList<>();

            for (Cliente c : todos) {
                if (opcion == 1) { // TODOS
                    filtrados.add(c);
                } else if (opcion == 2 && c instanceof ClienteEstandar) { // SOLO ESTÁNDAR
                    filtrados.add(c);
                } else if (opcion == 3 && c instanceof ClientePremium) { // SOLO PREMIUM
                    filtrados.add(c);
                }
            }

            // Reutilizamos nuestra excepción si el filtro no devuelve nada
            if (filtrados.isEmpty()) {
                throw new ListaVaciaException("No hay clientes del tipo seleccionado.");
            }

            return new Resultado<>(filtrados, "Lista de clientes recuperada.");

        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        }
        catch (Exception e) {
            return new Resultado<>("Error de base de datos" + e.getMessage());
        }
    }



    //Pedidos

    //Metodos para buscar cliente y articulo
    public Articulo buscarArticulo(String codigo) throws Exception {
        for (Articulo a : datos.getListaArticulos()) {
            if (a.getCodigo().equals(codigo)) return a;
        }
        // Si sale del bucle sin retornar, es que no existe
        throw new DatoNoEncontradoException("El artículo con código '" + codigo + "' no existe.");
    }

    public Cliente buscarCliente(String nif) throws Exception {
        for (Cliente c : datos.getListaClientes()) {
            if (c.getNif().equals(nif)) return c;
        }
        // Si sale del bucle sin retornar, es que no existe
        throw new DatoNoEncontradoException("El cliente con NIF '" + nif + "' no existe.");
    }

    //Nuevo metodo para generar el numero de pedido automaticamente
    public int generarNuevoNumeroPedido() {
        try {
            return datos.getListaPedidos().size() +1;
        } catch (Exception e) {
            return 1;
        }
    }


    //Añadir pedido
    public Resultado<String> añadirPedido(int num, String nif, String codigo, int cant) {
        try {
            // VALIDACIÓN DE DATOS
            for (Pedido p : datos.getListaPedidos()) {
                if (p.getNumeroPedido() == num) {
                    throw new PedidoException("Error: Ya existe un pedido con el número " + num + ".");
                }
            }
            if (cant <= 0) {
                throw new ValidacionDatosException("La cantidad del pedido debe ser al menos de 1 unidad.");
            }

            Cliente cliente = buscarCliente(nif);
            Articulo articulo = buscarArticulo(codigo);

            Pedido nuevo = new Pedido(num, cant, LocalDateTime.now(), articulo, cliente);
            datos.addPedido(nuevo);
        
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
            List<Pedido> todos = datos.getListaPedidos();
            List<Pedido> filtrados = new ArrayList<>();

            for (Pedido p : todos) {
                boolean cumpleEstado = false;
                if (opcionEstado == '1') cumpleEstado = true;
                else if (opcionEstado == '2' && p.puedeCancelarse()) cumpleEstado = true;
                else if (opcionEstado == '3' && !p.puedeCancelarse()) cumpleEstado = true;

                boolean cumpleCliente = (nif == null || p.getCliente().getNif().equalsIgnoreCase(nif));

                if (cumpleEstado && cumpleCliente) {
                    filtrados.add(p);
                }
            }

            if (filtrados.isEmpty()) {
                throw new ListaVaciaException("No se encontraron pedidos con los criterios seleccionados.");
            }

            return new Resultado<>(filtrados, "Búsqueda finalizada con éxito.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error onexion a la BBDD: " + e.getMessage());
        }
    }


    //Cancelar Pedido
    public Resultado<String> cancelarPedido(int numeroPedido) {
        try {
            Pedido pedido = null;
            for (Pedido p : datos.getListaPedidos()) {
                if (p.getNumeroPedido() == numeroPedido) {
                    pedido = p;
                    break;
                }
            }

            // 2. Si no existe, lanzamos nuestra PedidoException
            if (pedido == null) {
                throw new PedidoException("No se ha encontrado ningún pedido con el número: " + numeroPedido);
            }

            // 3. Intentamos cancelar. 
            // Si no puede cancelarse, Pedido.java lanzará la excepción aquí mismo.
            pedido.cancelar(); 

            // 4. Si la línea anterior no lanzó excepción, procedemos a borrarlo de la lista
            datos.getListaPedidos().remove(pedido);
        
            return new Resultado<>(null, "El pedido ha sido cancelado y eliminado del sistema.");

        } catch (PedidoException e) {
            // Capturamos el error específico (tiempo agotado o no encontrado)
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            // Capturamos cualquier otro error inesperado
            return new Resultado<>("Error inesperado al cancelar: " + e.getMessage());
        }
    }
}