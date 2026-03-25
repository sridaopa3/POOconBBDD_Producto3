package InnerJoinConElCafe.modelo.dao.mysql;

import InnerJoinConElCafe.modelo.*;
import InnerJoinConElCafe.modelo.dao.PedidoDAO;
import InnerJoinConElCafe.modelo.dao.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLPedidoDAO implements PedidoDAO {

    private final String INSERT = "INSERT INTO pedidos (numeroPedido, cantidad, fechaHora, nif_cliente, articulo_codigo) VALUES (?, ?, ?, ?, ?)";
    
    // SQL con JOIN para traer los datos del Cliente y del Artículo asociados al pedido
    private final String GET_ALL = "SELECT p.*, a.descripcion, a.precioVenta, a.gastosEnvio, a.tiempoPreparacion, " +
                                   "c.nombre, c.domicilio, c.email, c.tipo " +
                                   "FROM pedidos p " +
                                   "INNER JOIN articulos a ON p.articulo_codigo = a.codigo " +
                                   "INNER JOIN clientes c ON p.nif_cliente = c.nif";

    @Override
    public void insertar(Pedido p) throws Exception {
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stat = conn.prepareStatement(INSERT)) {
            stat.setInt(1, p.getNumeroPedido());
            stat.setInt(2, p.getCantidad());
            stat.setTimestamp(3, Timestamp.valueOf(p.getFechaHora()));
            stat.setString(4, p.getCliente().getNif());
            stat.setInt(5, p.getArticulo().getCodigo());
            stat.executeUpdate();
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
                    cli = new ClientePremium(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif_cliente"), rs.getString("email"));
                } else {
                    cli = new ClienteEstandar(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif_cliente"), rs.getString("email"));
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