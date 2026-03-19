package InnerJoinConElCafe.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static String URL;
    private static String USUARIO;
    private static String PASSWORD;

    static{
        try{
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            URL= props.getProperty("db.url");
            USUARIO=props.getProperty("db.usuario");
            PASSWORD=props.getProperty("db.password");
        } catch (IOException e){
            throw new RuntimeException("No se peude cargar db.properties: "+ e.getMessage());
        }
    }
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL,USUARIO,PASSWORD);
    }
}
