package InnerJoinConElCafe.vista;

import java.util.Scanner;
import InnerJoinConElCafe.controlador.Controlador;
import InnerJoinConElCafe.excepciones.DatoNoEncontradoException;
import InnerJoinConElCafe.modelo.Articulo;
import InnerJoinConElCafe.modelo.Cliente;
import InnerJoinConElCafe.modelo.Lista;
import InnerJoinConElCafe.modelo.Pedido;
import InnerJoinConElCafe.modelo.Resultado;

public class GestionOS {
    private Controlador controlador;
    private Scanner teclado;

    public GestionOS() {
        this.controlador = new Controlador(); // La vista inicializa al controlador
        this.teclado = new Scanner(System.in);
    }

    public void inicio() {
        boolean salir = false;
        char opcion;
        do {
            System.out.println("1. Añadir Artículo");
            System.out.println("2. Mostrar Artículos");
            System.out.println("3. Añadir Cliente");
            System.out.println("4. Mostrar Clientes");
            System.out.println("5. Añadir Pedido");
            System.out.println("6. Mostrar Pedidos");
            System.out.println("7. Cancelar Pedido");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = pedirOpcion();
            switch (opcion) {
                case '1':
                    añadirArticulo();
                    break;
                case '2':
                    mostrarArticulo();
                    break;
                case '3':
                    añadirCliente();
                    break;
                case '4':
                    mostrarClientes();
                    break;
                case '5':
                    añadirPedido();
                    break;
                case '6':
                    mostrarPedidos();
                    break;
                case '7':
                    eliminarPedido();
                    break;
                case '0':
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida. Selecciona de nuevo.");
                    break;
            }
        } while (!salir);
    }

    private char pedirOpcion() {
        String resp = teclado.nextLine();
        if (resp.isEmpty()) return ' ';
        return resp.charAt(0);
    }



    //Metodos del menu

    //Articulos
    private void añadirArticulo() {
        System.out.println("--- Nuevo Artículo ---");
        System.out.print("Descripción: ");
        String desc = teclado.nextLine();

        // --- Validación para Precio de Venta ---
        double precioV = 0;
        boolean precioValido = false;
        while (!precioValido) {
            try {
                System.out.print("Precio de Venta: ");
                precioV = Double.parseDouble(teclado.nextLine());
                precioValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un número decimal válido (ej: 10.5).");
            }
        }

        // --- Validación para Gastos de Envío ---
        double gastosE = 0;
        boolean gastosValidos = false;
        while (!gastosValidos) {
            try {
                System.out.print("Gastos de envio: ");
                gastosE = Double.parseDouble(teclado.nextLine());
                gastosValidos = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un número decimal válido (ej: 10.5).");
            }
        }

        // --- Validación para Tiempo de Preparación ---
        int tiempoPrep = 0;
        boolean tiempoValido = false;
        while (!tiempoValido) {
            try {
                System.out.print("Tiempo de preparacion (min): ");
                tiempoPrep = Integer.parseInt(teclado.nextLine());
                tiempoValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un número entero para los minutos.");
            }
        }

        // Enviamos los datos ya limpios y validados al controlador
        Resultado<String> res = controlador.añadirArticulo(desc, precioV, gastosE, tiempoPrep);
        System.out.println(res.getMensaje());
    }

    private void mostrarArticulo() {
        System.out.println("--- Lista de Artículos ---");
        Resultado<Lista<Articulo>> res = controlador.obtenerArticulos();

        if (!res.esExitoso()) {
            System.out.println(res.getMensaje());
        } else {
            for (Articulo a : res.getDato().getArrayList()) {
                System.out.println(a.toString());
            }
        }
    }



    //Clientes
    private void añadirCliente() {
        System.out.println("--- Nuevo Cliente ---");
        System.out.println("Nombre:");
        String nombre = teclado.nextLine();
        System.out.println("Domicilio:");
        String domicilio = teclado.nextLine();
        System.out.println("NIF:");
        String nif = teclado.nextLine();
        System.out.println("Email:");
        String email = teclado.nextLine();

        //Hasta que no se selecciona 1 o 2, no sale del bucle
        int tipo = 0;
        do {
            System.out.println("Tipo de cliente (1. Estándar / 2. Premium):");
            tipo = teclado.nextInt();
            teclado.nextLine(); //Limpieza de buffer
            if(tipo < 1 || tipo > 2){
                System.out.println("Opcion no valida.");
            }
            
        } while (tipo < 1 || tipo > 2);

        // Enviamos al controlador el 'tipo' para que él decida qué objeto crear
        Resultado<String> res = controlador.añadirCliente(nombre, domicilio, nif, email, tipo);
        System.out.println(res.getMensaje());
    }

    private void mostrarClientes() {
        System.out.println("\n--- FILTRAR CLIENTES ---");
        System.out.println("1. Mostrar todos");
        System.out.println("2. Mostrar clientes Estándar");
        System.out.println("3. Mostrar clientes Premium");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción: ");
    
        try {
            int opcion = Integer.parseInt(teclado.nextLine());
            if (opcion < 0 || opcion > 3) {
                System.out.println("Opción no válida.");
                return;
            }

            if (opcion != 0){
                Resultado<Lista<Cliente>> res = controlador.obtenerClientes(opcion);

                if (res.esExitoso()) {
                    System.out.println("\n--- LISTA DE CLIENTES ---");
                    for (Cliente c : res.getDato().getArrayList()) {
                        System.out.println(c.toString());
                    }
                } else {
                    System.out.println("\nAVISO: " + res.getMensaje());
                }
            } else { return; }

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número válido (1, 2, 3 o 0).");
        }
    }



    //Pedidos
    private void añadirPedido() {
        System.out.println("--- Nuevo Pedido ---");
        //Se genera automaticamente el numero de pedido
        int num = controlador.generarNuevoNumeroPedido(); 
        System.out.println("Número de pedido asignado: " + num);
    
        System.out.print("NIF del Cliente: ");
        String nif = teclado.nextLine();

        try {
            controlador.buscarCliente(nif); 
        } catch (DatoNoEncontradoException e) {
            System.out.println("El cliente no existe. ¿Desea registrarlo ahora? (S/N):");

            if (teclado.nextLine().equalsIgnoreCase("s")) {
                añadirCliente();
            } else {
                System.out.println("Pedido cancelado: cliente no encontrado.");
                return; 
            }
        } catch (Exception e) {
            System.out.println("Error crítico de base de datos: " + e.getMessage());
            return;
        }

        System.out.println("Código del Artículo:");
        int codigo = teclado.nextInt();
        teclado.nextLine();
    
        System.out.println("Cantidad:");
        int cant = teclado.nextInt();
        teclado.nextLine(); //Limpieza de buffer

        // El controlador hará el trabajo sucio de buscar y unir las piezas
        Resultado<String> res = controlador.añadirPedido(num, nif, codigo, cant);
        System.out.println(res.getMensaje());
    }

    private void mostrarPedidos() {
        System.out.println("\n--- CONSULTA DE PEDIDOS ---");
        System.out.println("1. Mostrar TODOS los pedidos");
        System.out.println("2. Mostrar pedidos EN CURSO (Pendientes de envío)");
        System.out.println("3. Mostrar pedidos PROCESADOS (Ya enviados)");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");

        String entrada = teclado.nextLine();
    
        if (entrada.equals("0")) {
            return; 
        }
        
        char opcionEstado = entrada.charAt(0);

        // Validamos rango (si no es 1, 2 o 3)
        if (opcionEstado < '1' || opcionEstado > '3') {
            System.out.println("Opción no válida.");
            return;
        }

        // --- Segundo nivel de filtro ---
        System.out.println("\n¿Desea filtrar por un cliente específico? (S/N):");
        char filtrarPorCliente = pedirOpcion();

        String nif = null;
        if (filtrarPorCliente == 's' || filtrarPorCliente == 'S') {
            System.out.print("Introduce el NIF del cliente: ");
            nif = teclado.nextLine();
        }

        // Llamada al controlador
        Resultado<Lista<Pedido>> res = controlador.obtenerPedidosFiltrados(opcionEstado, nif);

        // Visualización de resultados
        System.out.println("\n==========================================");
        if (!res.esExitoso()) {
            System.out.println("MENSAJE: " + res.getMensaje());
        } else {
            System.out.println("RESULTADOS ENCONTRADOS:");
            for (Pedido p : res.getDato().getArrayList()) {
                System.out.println("------------------------------------------");
                System.out.println(p.toString());
                // Mostramos si el pedido ya ha pasado el tiempo de preparación o no
                System.out.println("ESTADO: " + (p.puedeCancelarse() ? "EN CURSO" : "PROCESADO"));
                System.out.println("TOTAL: " + String.format("%.2f", p.calcularPrecio()) + " euros");
            }
        }
        System.out.println("==========================================\n");
    }


    private void eliminarPedido() {
        System.out.println("\n--- CANCELACIÓN DE PEDIDO ---");
        System.out.print("Introduce el número del pedido a eliminar: ");
    
        // Validamos que sea un número
        try {
            int numPedido = Integer.parseInt(teclado.nextLine());
        
            // Llamamos al controlador
            Resultado<String> res = controlador.cancelarPedido(numPedido);
        
            // Mostramos el mensaje (sea de éxito o el de PedidoException)
            System.out.println("\n>> RESULTADO: " + res.getMensaje());
        
        } catch (NumberFormatException e) {
            System.out.println("\n>> ERROR: Debes introducir un número de pedido válido.");
        }
    }
}
