package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void insertar(Cliente cliente ) throws SQLException;
    Cliente buscarPorNif(String nif) throws SQLException;
    List<Cliente> obtenerTodos() throws SQLException;
    List<Cliente> obtenerEstandar() throws SQLException;
    List<Cliente> obtenerPremium() throws  SQLException;

}
