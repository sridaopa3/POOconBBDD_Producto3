package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.Articulo;
import InnerJoinConElCafe.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO{

    @Override
    public void insertar(Articulo articulo) throws SQLException{
        String sql = "INSERT INTO articulos VALUES (?,?,?,?,?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3,articulo.getPrecioVenta());
            ps.setDouble(4,articulo.getGastosEnvio());
            ps.setInt(5,articulo.getTiempoPreparacion());
            ps.executeUpdate();
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) throws SQLException{
        String sql = "SELECT * FROM articulos WHERE codigo = ?";
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                );
            }
            return null;
        }
    }

    @Override
    public List<Articulo> obtenerTodos() throws SQLException{
        String sql = "SELECT * FROM articulos";
        List<Articulo> lista = new ArrayList<>();
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                lista.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                ));
            }
        }
        return lista;
    }

    @Override
    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM articulos WHERE codigo = ?";
        try(Connection con = ConexionBD.getConexion();
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,codigo);
            ps.executeUpdate();
        }
    }
}
