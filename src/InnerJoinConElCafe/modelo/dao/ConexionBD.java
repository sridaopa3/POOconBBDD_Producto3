package InnerJoinConElCafe.modelo.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConexionBD {
    
    private static Connection conexion = null;
    private static final String CONFIG_FILE = "conections/db.properties";


    //Función para iniciar la conexión
    public static Connection conectar() {
        try {
            // Si la conexión no existe o se ha cerrado por timeout, la iniciamos
            if (conexion == null || conexion.isClosed()) {
                Properties props = new Properties();
                
                // Cargamos las credenciales desde db.properties
                try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                    props.load(fis);
                    
                    String url = props.getProperty("db.url");
                    String user = props.getProperty("db.user");
                    String pass = props.getProperty("db.pass");

                    // Establecemos la conexión con el Driver JDBC
                    conexion = DriverManager.getConnection(url, user, pass);
                    System.out.println(">>> Conexión establecida con éxito a MySQL.");
                    
                } catch (IOException e) {
                    System.err.println("ERROR: No se pudo leer el archivo de configuración en: " + CONFIG_FILE);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Fallo al conectar con la base de datos.");
            System.err.println("Mensaje: " + e.getMessage());
        }
        return conexion;
    }

    // Función para cerrar la conexión
    public static void cerrar() {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                    System.out.println(">>> Conexión a la base de datos cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("ERROR al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}