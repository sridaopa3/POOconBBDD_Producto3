package InnerJoinConElCafe.DAO;

import InnerJoinConElCafe.modelo.Pedido;
import java.util.List;

public interface Pedido_DAO {
    void insertar(Pedido pedido) throws Exception;
    List<Pedido> listarPedidos() throws Exception;
    Pedido obtenerPorNumeroPedido(int NumeroPedido) throws Exception;
}

