package InnerJoinConElCafe.modelo;
import java.time.LocalDateTime;

public class Datos {

    private Lista<Cliente> listaClientes;
    private Lista<Articulo> listaArticulos;
    private Lista<Pedido> listaPedidos;

    @Override
    public String toString() {
        return "Tienda{" +
                "listaClientes=" + listaClientes +
                ", listaArticulos=" + listaArticulos +
                ", listaPedidos=" + listaPedidos +
                '}';
    }

    public Datos() {
        this.listaClientes = new Lista<>();
        this.listaArticulos = new Lista<>();
        this.listaPedidos = new Lista<>();

        //Cargar datos de prueba
        cargarDatosPrueba();
    }

    // Getters
    public Lista<Articulo> getListaArticulos() { return listaArticulos; }
    public Lista<Cliente> getListaClientes() { return listaClientes; }
    public Lista<Pedido> getListaPedidos() { return listaPedidos; }

    // Métodos para interactuar con las listas genéricas
    public void addArticulo(Articulo a) {
        listaArticulos.añadir(a);
    }

    public void addCliente(Cliente c) {
        listaClientes.añadir(c);
    }

    public void addPedido(Pedido p) {
        listaPedidos.añadir(p);
    }


    //Datos de prueba
    private void cargarDatosPrueba() {
        // 1. Artículos
        Articulo a1 = new Articulo("A1", "Cafe en grano 1kg", 15.50, 3.50, 5);
        Articulo a2 = new Articulo("A2", "Cafetera Italiana", 45.00, 5.00, 10);
        listaArticulos.añadir(a1);
        listaArticulos.añadir(a2);

        // 2. Clientes
        Cliente c1 = new ClienteEstandar("Marta Garcia", "Calle Mayor 1", "12345678A", "marta@gmail.com");
        Cliente c2 = new ClientePremium("Juan Perez", "Av. Diagonal 20", "87654321B", "juan@premium.com");
        listaClientes.añadir(c1);
        listaClientes.añadir(c2);

        // 3. Pedidos
        Pedido p1 = new Pedido(1, 2, LocalDateTime.now(), a1, c1);
        Pedido p2 = new Pedido(2, 2, LocalDateTime.now(), a1, c2);
        listaPedidos.añadir(p1);
        listaPedidos.añadir(p2);
    }
}

