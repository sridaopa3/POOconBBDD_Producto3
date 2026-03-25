package InnerJoinConElCafe.modelo.dao.mysql;

import InnerJoinConElCafe.modelo.*;
import InnerJoinConElCafe.modelo.dao.PedidoDAO;
import InnerJoinConElCafe.modelo.dao.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLPedidoDAO implements PedidoDAO {
    
    // SQL con JOIN para traer los datos del Cliente y del Artículo asociados al pedido
    private final String GET_ALL = "SELECT p.*, a.descripcion, a.precioVenta, a.gastosEnvio, a.tiempoPreparacion, " +
                                   "c.nombre, c.domicilio, c.nif, c.tipo, c.email " +
                                   "FROM pedidos p " +
                                   "INNER JOIN articulos a ON p.articulo_codigo = a.codigo " +
                                   "INNER JOIN clientes c ON p.cliente_nif = c.nif";

    public void insertar(Pedido p) throws Exception {
        Connection conn = null;
        try {
            conn = ConexionBD.conectar();
            conn.setAutoCommit(false);

            String sql = "{call insertarPedido(?, ?, ?, ?, ?)}";
            try (CallableStatement cst = conn.prepareCall(sql)) {
                cst.setInt(1, p.getNumeroPedido());
                cst.setInt(2, p.getCantidad());
                cst.setTimestamp(3, Timestamp.valueOf(p.getFechaHora()));
                cst.setString(4, p.getCliente().getNif());
                cst.setInt(5, p.getArticulo().getCodigo());
            
                cst.executeUpdate();
            }

            conn.commit(); 
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public List<Pedido> obtenerTodos() throws Exception {
        List<Pedido> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stat = conn.prepareStatement(GET_ALL);
             ResultSet rs = stat.executeQuery()) {
            
            while (rs.next()) {
                // 1. Reconstruir Artículo
                Articulo art = new Articulo(
                    rs.getString("descripcion"),
                    rs.getDouble("precioVenta"),
                    rs.getDouble("gastosEnvio"),
                    rs.getInt("tiempoPreparacion")
                );
                art.setCodigo(rs.getInt("articulo_codigo"));

                // 2. Reconstruir Cliente (según tipo)
                Cliente cli;
                if ("Premium".equals(rs.getString("tipo"))) {
                    cli = new ClientePremium(
                        rs.getString("nombre"), 
                        rs.getString("domicilio"), 
                        rs.getString("nif"), 
                        rs.getString("email")
                    );
                } else {
                    cli = new ClienteEstandar(
                        rs.getString("nombre"), 
                        rs.getString("domicilio"), 
                        rs.getString("nif"), 
                        rs.getString("email")
                    );
                }

                // 3. Crear Pedido con sus objetos internos
                Pedido ped = new Pedido(
                    rs.getInt("numeroPedido"),
                    rs.getInt("cantidad"),
                    rs.getTimestamp("fechaHora").toLocalDateTime(),
                    art,
                    cli
                );
                lista.add(ped);
            }
        }
        return lista;
    }

    @Override public void eliminar(Pedido t) throws Exception {
        String SQL = "DELETE FROM pedidos WHERE numeroPedido = ?";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stat = conn.prepareStatement(SQL)) {
            
            stat.setInt(1, t.getNumeroPedido());
            
            int filasAfectadas = stat.executeUpdate();
            if (filasAfectadas == 0) {
                throw new Exception("No se pudo eliminar: El pedido no existe en la base de datos.");
            }
        }
    }

    @Override public void modificar(Pedido t) throws Exception {}
    @Override public Pedido obtener(Integer id) throws Exception { return null; }
}