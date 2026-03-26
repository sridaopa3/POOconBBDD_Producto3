package InnerJoinConElCafe.DAO.Implementacion_DAO;

import InnerJoinConElCafe.DAO.Articulo_DAO;
import InnerJoinConElCafe.DAO.Cliente_DAO;
import InnerJoinConElCafe.DAO.Pedido_DAO;

public class Factory_DAO {
       public static Articulo_DAO getArticulo_DAO() {
        return new MySQLArticulo_DAO();
    }

        public static Cliente_DAO getCliente_DAO() {
        return new MySQLCliente_DAO();
    }

        public static Pedido_DAO getPedido_DAO(){
        return new MySQLPedido_DAO();
    }
}


