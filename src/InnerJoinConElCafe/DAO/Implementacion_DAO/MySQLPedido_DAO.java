package InnerJoinConElCafe.DAO.Implementacion_DAO;

import InnerJoinConElCafe.DAO.Pedido_DAO;
import InnerJoinConElCafe.modelo.*;
import JDBC.ConexionBBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLPedido_DAO implements Pedido_DAO {

    @Override
    public void insertar (Pedido pedido) throws Exception {
        String sql = "INSERT INTO Pedido (NumeroPedido, Cantidad, FechaHora, idArticulo, idCliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBBDD.Conectar();
        PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, pedido.getNumeroPedido());
            ps.setInt(2, pedido.getCantidad());
            ps.setTimestamp(3, Timestamp.valueOf(pedido.getFechaHora()));
            ps.setString(4, pedido.getArticulo().getCodigo());
            ps.setString(5, pedido.getCliente().getNif());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Pedido> listarPedidos() throws Exception {
        return new ArrayList<>();
    }
    
    @Override
    public Pedido obtenerPorNumeroPedido(int numeroPedido) throws Exception {
        return null;
    }
}
