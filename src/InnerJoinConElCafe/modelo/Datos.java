package InnerJoinConElCafe.modelo;

import java.util.ArrayList;
import java.util.List;

public class Datos {

    private List<Cliente> listaClientes;
    private List<Articulo> listaArticulos;
    private List<Pedido> listaPedidos;

    @Override
    public String toString() {
        return "Tienda{" +
                "listaClientes=" + listaClientes +
                ", listaArticulos=" + listaArticulos +
                ", listaPedidos=" + listaPedidos +
                '}';
    }

    public Datos() {
        this.listaClientes = new ArrayList<>();
        this.listaArticulos = new ArrayList<>();
        this.listaPedidos = new ArrayList<>();
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<Articulo> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }
}

