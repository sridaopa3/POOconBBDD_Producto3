package InnerJoinConElCafe.modelo.dao.mysql;

import InnerJoinConElCafe.modelo.Articulo;
import InnerJoinConElCafe.modelo.dao.ArticuloDAO;
import InnerJoinConElCafe.modelo.dao.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLArticuloDAO implements ArticuloDAO {

    private final String INSERT = "INSERT INTO articulos (descripcion, precioVenta, gastosEnvio, tiempoPreparacion) VALUES (?, ?, ?, ?)";
    private final String GET_ALL = "SELECT * FROM articulos";
    private final String DELETE = "DELETE FROM articulos WHERE codigo = ?";

    @Override
    public void insertar(Articulo a) throws Exception {
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stat = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stat.setString(1, a.getDescripcion());
            stat.setDouble(2, a.getPrecioVenta());
            stat.setDouble(3, a.getGastosEnvio());
            stat.setInt(4, a.getTiempoPreparacion());

            stat.executeUpdate();

            // Obtenemos el ID generado por el AUTO_INCREMENT de MySQL y lo asignamos al objeto directamente tras crearlo
            try (ResultSet rs = stat.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setCodigo(rs.getInt(1)); 
                }
            }
        }
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        List<Articulo> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stat = conn.prepareStatement(GET_ALL);
            ResultSet rs = stat.executeQuery()) {
            
            while (rs.next()) {
                // Usamos el constructor sin ID
                Articulo a = new Articulo(
                    rs.getString("descripcion"),
                    rs.getDouble("precioVenta"),
                    rs.getDouble("gastosEnvio"),
                    rs.getInt("tiempoPreparacion")
                );
                // Seteamos el ID manualmente tras crearlo
                a.setCodigo(rs.getInt("codigo"));
                lista.add(a);
            }
        }
        return lista;
    }

    @Override
    public void eliminar(Articulo a) throws Exception {
        try (Connection conn = ConexionBD.conectar();
            PreparedStatement stat = conn.prepareStatement(DELETE)) {
            stat.setInt(1, a.getCodigo());
            stat.executeUpdate();
        }
    }

    @Override public void modificar(Articulo t) throws Exception { /* Próxima fase */ }
    @Override public Articulo obtener(Integer id) throws Exception { return null; }
}