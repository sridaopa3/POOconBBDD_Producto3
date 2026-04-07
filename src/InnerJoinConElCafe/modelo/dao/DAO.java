package InnerJoinConElCafe.modelo.dao;

import java.util.List;

public interface DAO<T, K> {
    void insertar(T t) throws Exception;
    void modificar(T t) throws Exception;
    void eliminar(T t) throws Exception;
    List<T> obtenerTodos() throws Exception;
    T obtener(K id) throws Exception;
}
