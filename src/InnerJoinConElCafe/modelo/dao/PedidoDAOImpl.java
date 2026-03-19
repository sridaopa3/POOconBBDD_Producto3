package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.*;
import InnerJoinConElCafe.util.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO{

    private ClienteDAO clienteDAO = new ClienteDAOImpl();
    private ArticuloDAO articuloDAO = new ArticuloDAOImpl();

    @Override
    public void insertar(Pedido pedido) throws SQLException{
        String sql = "INSERT INTO pedidos VALUES (?,?,?,?,?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedido.getNumeroPedido());
            ps.setString(2, pedido.getCliente().getNif());
            ps.setString(3, pedido.getArticulo().getCodigo());
            ps.setInt(4, pedido.getCantidad());
            ps.setTimestamp(5, Timestamp.valueOf(pedido.getFechaHora()));
            ps.executeUpdate();
        }
    }

    @Override
    public Pedido buscarPorNumero(int numeroPedido) throws SQLException{
        String sql = "SELECT * FROM pedidos WHERE numero_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, numeroPedido);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return construirPedido(rs);
            }
            return null;
        }
    }

    @Override
    public List<Pedido> obtenerTodos() throws SQLException {
        return obtenerPorFiltro(null, null);
    }

    @Override
    public List<Pedido> obtenerPendientes()throws SQLException{
        List<Pedido> todos = obtenerTodos();
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido p : todos){
            if (p.puedeCancelarse()) pendientes.add(p);
        }
        return pendientes;
    }

    @Override
    public List<Pedido> obtenerEnviados() throws SQLException{
        List<Pedido> todos = obtenerTodos();
        List<Pedido> enviados = new ArrayList<>();
        for (Pedido p: todos){
            if (!p.puedeCancelarse()) enviados.add(p);
        }
        return enviados;
    }

    @Override
    public List<Pedido> obtenerPorClientes(String nif) throws SQLException{
        return obtenerPorFiltro(nif, null);
    }

    @Override
    public void eliminar(int numeroPedido) throws SQLException{
        String sql = "DELETE FROM pedidos WHERE numero_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,numeroPedido);
            ps.executeUpdate();
        }
    }

    private List<Pedido> obtenerPorFiltro(String nif, String estado) throws SQLException {
        String sql = nif == null ?
                "SELECT * FROM pedidos" :
                "SELECT * FROM pedidos WHERE nif_cliente = ?";
        List<Pedido> lista = new ArrayList<>();
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            if (nif != null) ps.setString(1,nif);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lista.add(construirPedido(rs));
            }
        }
        return lista;
    }

    private Pedido construirPedido(ResultSet rs) throws SQLException{
        int numeroPedido = rs.getInt("numero_pedido");
        int cantidad = rs.getInt("cantidad");
        LocalDateTime fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
        String nifCliente = rs.getString("nif_cliente");
        String codigoArticulo = rs.getString("codigo_articulo");

        Cliente cliente = clienteDAO.buscarPorNif(nifCliente);
        Articulo articulo = articuloDAO.buscarPorCodigo(codigoArticulo);

        return new Pedido(numeroPedido, cantidad, fechaHora, articulo, cliente);

    }

}
