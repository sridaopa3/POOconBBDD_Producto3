package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/OnlineStore";
    private static final String USER = "root";
    private static final String PASS = "ESCRIBIR_CONTRASEÑA";

    public static Connection Conectar(){
        Connection conexion = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
        }
           catch (ClassNotFoundException | SQLException e) {
                System.err.println("Error conexión" + e.getMessage());
            }
            return conexion;
    } 
}


class TestConexion {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión");

        Connection c = ConexionBBDD.Conectar();
        
        if (c != null) {
            System.out.println("CONEXION ESTABLECIDA. Java puede conectar con MySQL.");
            try {
                c.close(); 
            } catch (Exception e) {}
        } else {
            System.out.println("ERROR: La conexión devolvió null.");
        }
    }
}