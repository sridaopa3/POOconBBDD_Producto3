package InnerJoinConElCafe.modelo.dao;

import InnerJoinConElCafe.modelo.Cliente;
import InnerJoinConElCafe.modelo.ClienteEstandar;
import InnerJoinConElCafe.modelo.ClientePremium;
import InnerJoinConElCafe.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public void insertar(Cliente cliente) throws SQLException{
        String sql = "INSERT INTO clientes VALUES (?,?,?,?,?)";
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,cliente.getNif());
            ps.setString(2,cliente.getNombre());
            ps.setString(3,cliente.getDomicilio());
            ps.setString(4, cliente.getEmail());
            ps.setString(5,cliente instanceof ClientePremium ? "PREMIUM" : "ESTANDAR");
            ps.executeUpdate();
        }
    }

    @Override
    public Cliente buscarPorNif(String nif)throws SQLException{
        String sql = "SELECT * FROM clientes WHERE nif = ?";
        try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, nif);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return construirCliente(rs);
            }
            return null;
        }
    }

    @Override
    public List<Cliente> obtenerTodos()throws SQLException{
        return obtenerPorTipo(null);

    }
    @Override
    public List<Cliente> obtenerEstandar() throws  SQLException{
        return obtenerPorTipo("ESTANDAR");
    }
    @Override
    public List<Cliente> obtenerPremium() throws  SQLException{
        return obtenerPorTipo("PREMIUM");
    }

    private List<Cliente> obtenerPorTipo(String tipo) throws SQLException{
        String sql = tipo == null?
                "SELECT * FROM clientes":
                "SELECT * FROM clientes WHERE tipo = ?";
        List<Cliente> lista = new ArrayList<>();
        try(Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){
            if (tipo != null) ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                lista.add(construirCliente(rs));
            }
        }
        return lista;
    }

    private Cliente construirCliente(ResultSet rs) throws SQLException{
        String nif = rs.getString("nif");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String email = rs.getString("email");
        String tipo = rs.getString("tipo");

        if("PREMIUM".equals(tipo)){
            return new ClientePremium(nombre, domicilio, nif,email);
        } else {
            return new ClienteEstandar(nombre, domicilio, nif,email);
        }
    }

}
