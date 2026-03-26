package InnerJoinConElCafe.DAO;

import InnerJoinConElCafe.modelo.Articulo;
import java.util.List;

public interface Articulo_DAO {
    void insertar(Articulo articulo) throws Exception;
    List<Articulo> listarArticulos() throws Exception;
    Articulo obtenerPorCodigo(String codigo) throws Exception;
}
