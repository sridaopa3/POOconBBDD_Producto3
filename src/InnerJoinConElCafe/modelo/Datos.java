package InnerJoinConElCafe.modelo;
import java.util.List;
import InnerJoinConElCafe.DAO.Implementacion_DAO.Factory_DAO;

public class Datos {

    public Datos() {}

    // Métodos para llamar a los datos provinientes de la BBDD
    //Objeto: Articulos
    public void addArticulo(Articulo art) throws Exception {
        Factory_DAO.getArticulo_DAO().insertar(art);
    }

    public List<Articulo> getListaArticulos() throws Exception {
        return Factory_DAO.getArticulo_DAO().listarArticulos();
    }

 //Objeto: Clientes
    public void addCliente(Cliente cli) throws Exception {
        Factory_DAO.getCliente_DAO().insertar(cli);
    }

    public List<Cliente> getListaClientes() throws Exception {
        return Factory_DAO.getCliente_DAO().listarClientes();
    }

 //Objeto: Pedidos
    public void addPedido(Pedido ped) throws Exception {
        Factory_DAO.getPedido_DAO().insertar(ped);
    }

    public List<Pedido> getListaPedidos() throws Exception {
        return Factory_DAO.getPedido_DAO().listarPedidos();
    }

    @Override
    public String toString() {
        return "Conexion a la BBDD activa";
    }
}

