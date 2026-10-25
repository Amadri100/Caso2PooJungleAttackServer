package src.server;

 
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.mensajes.MensajeJugador;
import src.mensajes.MensajeServidor;
import src.utilidades.Constantes;
import src.controlador.Controlador;
public class Servidor {

    private final Controlador controlador;
    // Clientes que ya tienen nombre
    private final ConcurrentHashMap<String, ClientHandler> clientes;
    // Clientes conectados que todavía no han recibido nombre
    private final CopyOnWriteArrayList<ClientHandler> conectados;
    private ThreadConexiones threadConexiones;
    private final ExecutorService executor;

    public Servidor() {
        this.clientes = new ConcurrentHashMap<>();
        this.conectados = new CopyOnWriteArrayList<>();
        this.executor = Executors.newCachedThreadPool();
        this.controlador = new Controlador();
    }

    public void iniciar() {
 
    }

    public void detener() {
 
    }

    // Lo llama ThreadConexiones por cada socket aceptado
    public void registrarCliente(ClientHandler handler) {

    }

    // Reserva el nombre y mueve al cliente de "conectados" a "clientes".
    // Devuelve false si el cliente ya tenía nombre o si el nombre está ocupado.
    public boolean asignarNombre(ClientHandler handler, String nombre) {
        return false; //TODO
    }

    public void removerCliente(ClientHandler handler) {

    }

    // Si el nombre es NOMBRE_UNIVERSAL, el mensaje se envía a todos los clientes
    public void mandarACliente(String nombre, MensajeServidor msg) {

    }

    public void mandarATodos(MensajeServidor msg) {

    }

    // Entrega al sistema (Controlador) un mensaje que viene de un jugador
    public void mandarASistema(MensajeJugador msg) {

    }

    // Puente para el método que ClientHandler ya invoca (no se modificó esa clase)
    public void procesarMensaje(ClientHandler handler, MensajeJugador msg) {

    }

    // Cantidad de clientes con nombre asignado
    public int getCantidadClientes() {
        return -1; //Temp
    }
}