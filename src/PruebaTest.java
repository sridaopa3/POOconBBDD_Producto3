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
        cafe = new Articulo("Cafe de prueba", 10.0, 5.0, 1);
        
        // Cliente Premium con 20% de descuento (0.20)
        clientePremium = new ClientePremium("Marta Premium", "Calle A", "12345678A", "marta@test.com");
        
        // Cliente Estándar sin descuento
        clienteNormal = new ClienteEstandar("Pepe Normal", "Calle B","87654321B", "pepe@test.com");
    }

    @Test
    public void calcularPrecioClienteEstandar() {

        /* Lógica: (10 * 2) + (5 * 2) = 30 */ 
        //arrange
        Articulo articulo = new Articulo ("Camiseta", 10.0, 5.0, 60);
        Cliente cliente = new ClienteEstandar("Juan", "C. Calabria", "47121189L", "Juan_ito@gmail.com");
        Pedido pedido = new Pedido (1, 2, LocalDateTime.now(),articulo, cliente);
        //act
        double precio = pedido.calcularPrecio();
        //assert
        assertEquals(30.0, precio, 0.01);
    }

    @Test
    void testCalculoPrecioPremiumConDescuento() {
        // Lógica: 10.0 + (5.0 * 0.8) = 14.0
        Pedido pedido = new Pedido(1, 1, LocalDateTime.now(), cafe, clientePremium);
        double esperado = 14.0;
        
        assertEquals(esperado, pedido.calcularPrecio(), 0.01, 
            "El descuento del 20% sobre el precio del envio no se ha calculado bien.");
    }

    @Test
    public void siPuedeCancelarse() {
        // Arrange - tiempo de preparación 60 minutos, pedido hecho ahora
        Articulo articulo = new Articulo("Camiseta", 10.0, 5.0, 60);
        Cliente cliente = new ClienteEstandar("Juan", "C. Calabria", "47121189L", "Juan_ito@gmail.com");
        Pedido pedido = new Pedido(1, 2, LocalDateTime.now(), articulo, cliente);

        // Act & Assert
        assertTrue(pedido.puedeCancelarse());
    }

    @Test
    void testControlExpiracionPedido() {
        // Creamos un artículo que se prepara en 0 minutos (expira al instante)
        Articulo cafeFlash = new Articulo("Cafe Flash", 10.0, 5.0, 0);
        Pedido pedidoExpira = new Pedido(2, 1, LocalDateTime.now(), cafeFlash, clienteNormal);
        
        // Al tener 0 min de preparación, no debería dejar cancelarlo nada más crearse
        assertFalse(pedidoExpira.puedeCancelarse(), 
            "El pedido no deberia poder cancelarse, ya está procesado.");
    }

    @Test
    void testVerificacionFormatoEmail() {
        // Comprobamos que el email guardado en el cliente contiene una '@' y un '.'
        String email = clientePremium.getEmail();
        
        assertTrue(email.contains("@") && email.contains("."), 
            "El formato del email del cliente no es válido.");
    }

}