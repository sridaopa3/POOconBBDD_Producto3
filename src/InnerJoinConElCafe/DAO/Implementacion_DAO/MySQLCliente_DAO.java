package InnerJoinConElCafe.DAO.Implementacion_DAO;

import InnerJoinConElCafe.DAO.Cliente_DAO;
import InnerJoinConElCafe.modelo.Cliente;
import JDBC.ConexionBBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCliente_DAO implements Cliente_DAO {

    @Override
    public void insertar (Cliente cli) throws Exception {
        String sql = "INSERT INTO Cliente (Nombre, Domicilio, NIF, Email) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBBDD.Conectar();
        PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cli.getNombre());
            ps.setString(2, cli.getDomicilio());
            ps.setString(3, cli.getNif());
            ps.setString(4, cli.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Cliente> listarClientes() throws Exception {
        return new ArrayList<>();
    }
    
    @Override
    public Cliente obtenerPorNIF(String nif) throws Exception {
        return null;
    }
}
