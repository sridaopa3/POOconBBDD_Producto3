package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.Pedido;

import java.sql.SQLException;
import java.util.List;

public interface PedidoDAO {
    void insertar(Pedido pedido)throws SQLException;
    Pedido buscarPorNumero(int numeroPedido) throws SQLException;
    List<Pedido> obtenerTodos() throws SQLException;
    List<Pedido> obtenerPendientes() throws SQLException;
    List<Pedido> obtenerEnviados() throws SQLException;
    List<Pedido> obtenerPorClientes(String nif) throws SQLException;

    void eliminar(int numeroPedido) throws SQLException;
}
