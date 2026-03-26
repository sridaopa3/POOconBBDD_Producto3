package InnerJoinConElCafe.DAO;

import InnerJoinConElCafe.modelo.Cliente;
import java.util.List;

public interface Cliente_DAO {
    void insertar(Cliente cliente) throws Exception;
    List<Cliente> listarClientes() throws Exception;
    Cliente obtenerPorNIF(String nif) throws Exception;
}

