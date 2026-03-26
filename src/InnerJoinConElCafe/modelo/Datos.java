package InnerJoinConElCafe.modelo;
import java.util.List;
import InnerJoinConElCafe.DAO.Implementacion_DAO.Factory_DAO;

public class Datos {

    public Datos() {}

    // Métodos para llamar a los datos provinientes de la BBDD
    //Objeto: Articulos
    public void addArticulo(Articulo a) throws Exception {
        Factory_DAO.getArticulo_DAO().insertar(a);
    }

    public List<Articulo> getListaArticulos() throws Exception {
        return Factory_DAO.getArticulo_DAO().listarArticulos();
    }

 //Objeto: Clientes
    public void addCliente(Cliente c) throws Exception {
        Factory_DAO.getCliente_DAO().insertar(c);
    }

    public List<Cliente> getListaClientes() throws Exception {
        return Factory_DAO.getCliente_DAO().listarClientes();
    }

 //Objeto: Pedidos
    public void addPedido(Pedido p) throws Exception {
        Factory_DAO.getPedido_DAO().insertar(p);
    }

    public List<Pedido> getListaPedidos() throws Exception {
        return Factory_DAO.getPedido_DAO().listarPedidos();
    }

    @Override
    public String toString() {
        return "Conexion a la BBDD activa";
    }
}

