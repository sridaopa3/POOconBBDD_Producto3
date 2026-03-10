import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import InnerJoinConElCafe.modelo.*;

public class PruebaTest {

    private Articulo cafe;
    private ClientePremium clientePremium;
    private ClienteEstandar clienteNormal;

    @BeforeEach
    void setUp() {
        // Articulo: 10€ venta, 5€ envío, 1 minuto de preparación
        cafe = new Articulo("A1", "Cafe de prueba", 10.0, 5.0, 1);
        
        // Cliente Premium con 20% de descuento (0.20)
        clientePremium = new ClientePremium("Marta Premium", "Calle A", "12345678A", "marta@test.com");
        
        // Cliente Estándar sin descuento
        clienteNormal = new ClienteEstandar("Pepe Normal", "Calle B","87654321B", "pepe@test.com");
    }

    @Test
    void testCalculoPrecioPremiumConDescuento() {
        // Lógica: (10.0 * 0.8) + 5.0 = 13.0
        Pedido pedido = new Pedido(1, 1, LocalDateTime.now(), cafe, clientePremium);
        double esperado = 13.0;
        
        assertEquals(esperado, pedido.calcularPrecio(), 0.01, 
            "El descuento del 20% sobre el precio del artículo no se ha calculado bien.");
    }

    @Test
    void testControlExpiracionPedido() {
        // Creamos un artículo que se prepara en 0 minutos (expira al instante)
        Articulo cafeFlash = new Articulo("A2", "Cafe Flash", 10.0, 5.0, 0);
        Pedido pedidoExpira = new Pedido(2, 1, LocalDateTime.now(), cafeFlash, clienteNormal);
        
        // Al tener 0 min de preparación, no debería dejar cancelarlo nada más crearse
        assertFalse(pedidoExpira.puedeCancelarse(), 
            "Un pedido con tiempo de preparación 0 debería considerarse 'ya en proceso' y no cancelable.");
    }

    @Test
    void testVerificacionFormatoEmail() {
        // Comprobamos que el email guardado en el cliente contiene una '@' y un '.'
        String email = clientePremium.getEmail();
        
        assertTrue(email.contains("@") && email.contains("."), 
            "El formato del email del cliente no es válido.");
    }
}