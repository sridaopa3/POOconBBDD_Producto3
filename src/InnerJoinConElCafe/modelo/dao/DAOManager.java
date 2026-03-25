package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.dao.mysql.MySQLArticuloDAO;
import InnerJoinConElCafe.modelo.dao.mysql.MySQLClienteDAO;
import InnerJoinConElCafe.modelo.dao.mysql.MySQLPedidoDAO;

public class DAOManager {

    private ArticuloDAO articulos = null;
    private ClienteDAO clientes = null;
    private PedidoDAO pedidos = null;

    /**
     * Devuelve la implementación de ArticuloDAO.
     * Si no existe, la crea (Lazy Initialization).
     */
    public ArticuloDAO getArticuloDAO() {
        if (articulos == null) {
            articulos = new MySQLArticuloDAO();
        }
        return articulos;
    }

    /**
     * Devuelve la implementación de ClienteDAO.
     */
    public ClienteDAO getClienteDAO() {
        if (clientes == null) {
            clientes = new MySQLClienteDAO();
        }
        return clientes;
    }

    /**
     * Devuelve la implementación de PedidoDAO.
     */
    public PedidoDAO getPedidoDAO() {
        if (pedidos == null) {
            pedidos = new MySQLPedidoDAO();
        }
        return pedidos;
    }
}