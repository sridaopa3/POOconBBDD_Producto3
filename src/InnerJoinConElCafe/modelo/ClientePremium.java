package InnerJoinConElCafe.modelo;

public class ClientePremium extends Cliente {

private static final double CUOTA_ANUAL = 30.0;
private static final double DESCUENTO = 0.20;

    public ClientePremium(String nombre, String domicilio, String nif, String email){
        super(nombre,domicilio,nif,email);

    }


     //ToString
    @Override
    public String toString() {
        return super.toString() + 
        "Tipo Cliente: Premium " + 
        "[Cuota: " + String.format("%.2f", CUOTA_ANUAL) + 
        "euros, Descuento: " + DESCUENTO * 100 + 
        "%]";
    }
    
    @Override
    public double aplicarDescuento(double precioBase) {
        return precioBase - (precioBase * DESCUENTO);
    }

    public double getCuotaAnual() {
        return CUOTA_ANUAL;
    }

}
