package InnerJoinConElCafe.modelo;

public class ClienteEstandar extends Cliente {
    
    public ClienteEstandar(String nombre, String domicilio, String nif, String email){
        super(nombre, domicilio, nif, email);
    }


    @Override
    public String toString() {
        return super.toString() + 
        "Tipo Cliente: Estandar ";
    }
}
