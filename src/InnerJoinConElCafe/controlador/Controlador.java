package InnerJoinConElCafe.controlador;

import java.time.LocalDateTime;
import java.util.List;
import InnerJoinConElCafe.excepciones.*;
import InnerJoinConElCafe.modelo.*;

public class Controlador {
    private Datos datos;

    public Controlador() {
        this.datos = new Datos();
    }

    // --- ARTÍCULOS ---

    public Resultado<String> añadirArticulo(String desc, double precio, double envio, int tiempo) {
        try {
            if (precio <= 0) throw new ValidacionDatosException("El precio debe ser mayor que 0.");
            if (tiempo <= 0) throw new ValidacionDatosException("El tiempo debe ser al menos de 1 min.");

            Articulo nuevo = new Articulo(desc, precio, envio, tiempo);
            datos.addArticulo(nuevo);
            return new Resultado<>(desc, "Artículo añadido correctamente a la BBDD.");
        } catch (ValidacionDatosException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error en BBDD: " + e.getMessage());
        }
    }

    public Resultado<Lista<Articulo>> obtenerArticulos() {
        try {
            List<Articulo> listaBBDD = datos.getListaArticulos();
            if (listaBBDD.isEmpty()) throw new ListaVaciaException("No hay artículos en el catálogo.");
            
            Lista<Articulo> articulos = new Lista<>();
            for (Articulo a : listaBBDD) articulos.añadir(a);
            
            return new Resultado<>(articulos, "Lista de artículos obtenida.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error al recuperar artículos: " + e.getMessage());
        }
    }

    // --- CLIENTES ---

    public Resultado<String> añadirCliente(String nombre, String dom, String nif, String email, int tipo) {
        try {
            // Verificamos si ya existe consultando a la BBDD
            for (Cliente c : datos.getListaClientes()) {
                if (c.getNif().equalsIgnoreCase(nif)) {
                    throw new ClienteException("Ya existe un cliente con el NIF: " + nif);
                }
            }

            Cliente nuevoCliente = (tipo == 2) ? 
                new ClientePremium(nombre, dom, nif, email) : 
                new ClienteEstandar(nombre, dom, nif, email);
        
            datos.addCliente(nuevoCliente);
            return new Resultado<>(nif, "Cliente registrado en BBDD correctamente.");
        } catch (ClienteException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error inesperado: " + e.getMessage());
        }
    }

    public Resultado<Lista<Cliente>> obtenerClientes(int opcion) {
        try {
            List<Cliente> todosBBDD = datos.getListaClientes();
            Lista<Cliente> filtrados = new Lista<>();

            for (Cliente c : todosBBDD) {
                if (opcion == 1) filtrados.añadir(c);
                else if (opcion == 2 && c instanceof ClienteEstandar) filtrados.añadir(c);
                else if (opcion == 3 && c instanceof ClientePremium) filtrados.añadir(c);
            }

            if (filtrados.getSize() == 0) throw new ListaVaciaException("No hay clientes del tipo seleccionado.");
            return new Resultado<>(filtrados, "Lista de clientes recuperada.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error: " + e.getMessage());
        }
    }

    // --- PEDIDOS ---

    public Articulo buscarArticulo(int codigo) throws DatoNoEncontradoException, Exception {
        for (Articulo a : datos.getListaArticulos()) {
            if (a.getCodigo() == codigo) return a;
        }
        throw new DatoNoEncontradoException("El artículo con código '" + codigo + "' no existe.");
    }

    public Cliente buscarCliente(String nif) throws DatoNoEncontradoException, Exception {
        for (Cliente c : datos.getListaClientes()) {
            if (c.getNif().equals(nif)) return c;
        }
        throw new DatoNoEncontradoException("El cliente con NIF '" + nif + "' no existe.");
    }

    public int generarNuevoNumeroPedido() {
        try {
            return datos.getListaPedidos().size() + 1;
        } catch (Exception e) {
            return 1;
        }
    }

    public Resultado<String> añadirPedido(int num, String nif, int codigo, int cant) {
        try {
            if (cant <= 0) throw new ValidacionDatosException("La cantidad debe ser al menos 1.");
            
            // Verificamos duplicados en BBDD
            for (Pedido p : datos.getListaPedidos()) {
                if (p.getNumeroPedido() == num) throw new PedidoException("Ya existe el pedido nº " + num);
            }

            Cliente cliente = buscarCliente(nif);
            Articulo articulo = buscarArticulo(codigo);

            Pedido nuevo = new Pedido(num, cant, LocalDateTime.now(), articulo, cliente);
            datos.addPedido(nuevo);
        
            return new Resultado<>(String.valueOf(num), "Pedido creado con éxito en BBDD.");
        } catch (DatoNoEncontradoException | ValidacionDatosException | PedidoException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error: " + e.getMessage());
        }
    }

    public Resultado<Lista<Pedido>> obtenerPedidosFiltrados(char opcionEstado, String nif) {
        try {
            List<Pedido> todosBBDD = datos.getListaPedidos();
            Lista<Pedido> filtrados = new Lista<>();

            for (Pedido p : todosBBDD) {
                boolean cumpleEstado = (opcionEstado == '1') || 
                                     (opcionEstado == '2' && p.puedeCancelarse()) || 
                                     (opcionEstado == '3' && !p.puedeCancelarse());

                boolean cumpleCliente = (nif == null || p.getCliente().getNif().equalsIgnoreCase(nif));

                if (cumpleEstado && cumpleCliente) filtrados.añadir(p);
            }

            if (filtrados.getSize() == 0) throw new ListaVaciaException("No se encontraron pedidos.");
            return new Resultado<>(filtrados, "Búsqueda finalizada con éxito.");
        } catch (ListaVaciaException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error: " + e.getMessage());
        }
    }

    public Resultado<String> cancelarPedido(int numeroPedido) {
        try {
            Pedido pedido = null;
            // Buscamos el pedido en la BBDD
            for (Pedido p : datos.getListaPedidos()) {
                if (p.getNumeroPedido() == numeroPedido) {
                    pedido = p;
                    break;
                }
            }

            if (pedido == null) {
                throw new PedidoException("No se ha encontrado el pedido nº: " + numeroPedido);
            }

            // Comprobamos que se puede cancelar
            pedido.cancelar(); 

            // 2. Persistencia: Si no ha saltado error arriba, borramos de la BBDD
            datos.eliminarPedido(pedido); 
        
            return new Resultado<>(null, "Pedido nº " + numeroPedido + " cancelado y eliminado de la BBDD.");

        } catch (PedidoException e) {
            return new Resultado<>(e.getMessage());
        } catch (Exception e) {
            return new Resultado<>("Error técnico al cancelar: " + e.getMessage());
        }
    }
}