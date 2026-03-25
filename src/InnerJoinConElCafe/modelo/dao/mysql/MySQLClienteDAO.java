package InnerJoinConElCafe.modelo.dao.mysql;

import InnerJoinConElCafe.modelo.Cliente;
import InnerJoinConElCafe.modelo.ClienteEstandar;
import InnerJoinConElCafe.modelo.ClientePremium;
import InnerJoinConElCafe.modelo.dao.ClienteDAO;
import InnerJoinConElCafe.modelo.dao.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAO implements ClienteDAO {

    private final String INSERT = "INSERT INTO clientes (nif, nombre, domicilio, email, tipo, cuotaAnual, descuentoEnvio) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String GET_ALL = "SELECT * FROM clientes";

    @Override
    public void insertar(Cliente c) throws Exception {
        try (Connection conn = ConexionBD.conectar();
        PreparedStatement stat = conn.prepareStatement(INSERT)) {
        
            stat.setString(1, c.getNif());
            stat.setString(2, c.getNombre());
            stat.setString(3, c.getDomicilio());
            stat.setString(4, c.getEmail());
        
            // Determinamos el tipo y sacamos los valores específicos
            if (c instanceof ClientePremium) {
                stat.setString(5, "Premium");
                // Usa el getter de la clase Cliente, estos getters se encargan de revisar las subclases para determinar el valor correcto
                stat.setDouble(6, c.getCuotaAnual()); 
                stat.setDouble(7, c.getDescuentoEnvio());
            } else {
                stat.setString(5, "Estandar");
                stat.setDouble(6, c.getCuotaAnual());
                stat.setDouble(7, c.getDescuentoEnvio());
            }
        
            stat.executeUpdate();
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stat = conn.prepareStatement(GET_ALL);
             ResultSet rs = stat.executeQuery()) {
            while (rs.next()) {
                Cliente c;
                String tipo = rs.getString("tipo");
                if ("Premium".equals(tipo)) {
                    c = new ClientePremium(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
                } else {
                    c = new ClienteEstandar(rs.getString("nombre"), rs.getString("domicilio"), rs.getString("nif"), rs.getString("email"));
                }
                lista.add(c);
            }
        }
        return lista;
    }

    @Override public void modificar(Cliente t) throws Exception {}
    @Override public void eliminar(Cliente t) throws Exception {}
    @Override public Cliente obtener(String id) throws Exception { return null; }
}