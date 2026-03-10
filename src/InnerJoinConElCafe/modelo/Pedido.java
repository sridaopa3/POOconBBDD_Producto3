package InnerJoinConElCafe.modelo;

import java.time.LocalDateTime;

import InnerJoinConElCafe.excepciones.PedidoException;

public class Pedido {

    private int numeroPedido;
    private int cantidad;
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private Articulo articulo;

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaHora +
                ", cliente=" + cliente +
                ", articulo=" + articulo +
                '}';
    }

    public Pedido(int numeroPedido, int cantidad, LocalDateTime fechaHora, Articulo articulo, Cliente cliente) {
        this.numeroPedido = numeroPedido;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.articulo = articulo;
    }

    public double calcularPrecio() {
        double precioBase = articulo.getPrecioVenta() * cantidad;
        double precioFinalProductos = cliente.aplicarDescuento(precioBase);
        double envio = articulo.getGastosEnvio() * cantidad; 
    
        return precioFinalProductos + envio;
    }

    public boolean puedeCancelarse(){

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime momentoEnvio = fechaHora.plusMinutes(articulo.getTiempoPreparacion());
        return ahora.isBefore(momentoEnvio);
    }

    public void cancelar() throws PedidoException { 
        if(!puedeCancelarse()){
            // Lanzamos la excepcion específica
            throw new PedidoException("El pedido no puede cancelarse. Tiempo insuficiente");
        }
    }



    //getters y setters
    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }
}


