package InnerJoinConElCafe.modelo.dao;

public class DAOFactory {
    public static ArticuloDAO getArticuloDAO(){
        return new ArticuloDAOImpl();
    }

    public static ClienteDAO getClienteDAO(){
        return new ClienteDAOImpl();
    }

    public static PedidoDAO getPedidoDAO(){
        return new PedidoDAOImpl();
    }
}
