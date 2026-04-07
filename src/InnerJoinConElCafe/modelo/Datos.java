package InnerJoinConElCafe.modelo;

import InnerJoinConElCafe.modelo.dao.*;
import java.util.List;

public class Datos {

    // NUEVO: Manager para acceder a la Base de Datos
    private DAOManager daoManager;


    public Datos() {
        // Inicializamos el manager de DAOs
        this.daoManager = new DAOManager();
    }

    // --- MÉTODOS PARA ARTÍCULOS ---

    public void addArticulo(Articulo a) throws Exception {
        daoManager.getArticuloDAO().insertar(a);
    }

    public List<Articulo> getListaArticulos() throws Exception {
        return daoManager.getArticuloDAO().obtenerTodos();
    }


    // --- MÉTODOS PARA CLIENTES ---

    public void addCliente(Cliente c) throws Exception {
        daoManager.getClienteDAO().insertar(c);
    }

    public List<Cliente> getListaClientes() throws Exception {
        return daoManager.getClienteDAO().obtenerTodos();
    }


    // --- MÉTODOS PARA PEDIDOS ---

    public void addPedido(Pedido p) throws Exception {
        daoManager.getPedidoDAO().insertar(p);
    }

    public List<Pedido> getListaPedidos() throws Exception {
        return daoManager.getPedidoDAO().obtenerTodos();
    }

    public void eliminarPedido(Pedido p) throws Exception {
        daoManager.getPedidoDAO().eliminar(p);
    }

}

