package InnerJoinConElCafe.DAO.Implementacion_DAO;

import InnerJoinConElCafe.DAO.Articulo_DAO;
import InnerJoinConElCafe.modelo.Articulo;
import JDBC.ConexionBBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLArticulo_DAO implements Articulo_DAO {

    @Override
    public void insertar (Articulo art) throws Exception {
        String sql = "INSERT INTO Articulo (CodigoArticulo, Descripcion, PrecioVenta, GastosEnvio, TempoPreparacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBBDD.Conectar();
        PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, art.getCodigo());
            ps.setString(2, art.getDescripcion());
            ps.setDouble(3, art.getPrecioVenta());
            ps.setDouble(4, art.getGastosEnvio());
            ps.setInt(5, art.getTiempoPreparacion());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Articulo> listarArticulos() throws Exception {
        return new ArrayList<>();
    }
    
    @Override
    public Articulo obtenerPorCodigo(String codigo) throws Exception {
        return null;
    }
}
