package InnerJoinConElCafe.modelo.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBDTest {

    @Test
    @DisplayName("Prueba de conexión a la base de datos")
    void testConexion() {
        Connection conn = ConexionBD.conectar();
        
        assertNotNull(conn, "La conexión no debería ser nula");
        
        try {
            assertFalse(conn.isClosed(), "La conexión debería estar abierta");
            System.out.println("DEBUG: Test de conexión superado.");
        } catch (SQLException e) {
            fail("Error al verificar el estado de la conexión: " + e.getMessage());
        } finally {
            ConexionBD.cerrar();
        }
    }
}