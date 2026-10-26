package src.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import src.mensajes.MensajeJugador;
import src.mensajes.MensajeServidor;
import src.utilidades.Constantes;
import src.utilidades.Logger;
import src.utilidades.NivelLog;
import src.controlador.Controlador;

public class Servidor {

    private Controlador controlador;
    // Clientes que ya tienen nombre
    private ConcurrentHashMap<String, ClientHandler> clientes;
    // Clientes conectados que todavía no han recibido nombre
    private CopyOnWriteArrayList<ClientHandler> conectados;
    private ThreadConexiones threadConexiones;
    private ExecutorService executor;

    public Servidor() {
        this.clientes = new ConcurrentHashMap<>();
        this.conectados = new CopyOnWriteArrayList<>();
        this.executor = Executors.newCachedThreadPool();
    }

    public Servidor(Controlador controlador) {
        this();
        this.controlador = controlador;
    }

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public boolean iniciar() { //Intenta iniciar el servidor y la busqueda de conexiones
        int port = ServerConfig.getInstance().getPuerto();
        try {
            threadConexiones = new ThreadConexiones(this, port);
            executor.execute(threadConexiones);
            return true;
        }
        catch (IOException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.ORIGEN_SERVER, Constantes.ERROR_INICIO_SERVER, e);
        }
        return false;
    }

    public void detener() {  
        if (threadConexiones != null) {
            threadConexiones.detener();
        }
        for (ClientHandler handler : conectados) {
            handler.cerrar();
        }
        for (ClientHandler handler : clientes.values()) {
            handler.cerrar();
        }
        conectados.clear();
        clientes.clear();
        executor.shutdownNow();
    }

    // Registra un cliente aceptado
    public void registrarCliente(ClientHandler handler) {
        conectados.add(handler);
        executor.execute(handler);
    }

    //Le asigna el nombre al cliente, true = exito, false = fallo
    public synchronized boolean asignarNombre(ClientHandler handler, String nombre) {
        if (nombre == null || nombre.isBlank() || nombre.equals(Constantes.NOMBRE_UNIVERSAL)) { 
            //Evita nombres invalidados
            return false;
        }
        if (!conectados.contains(handler)) { //Evita agregarlo si ya tiene nombre
            return false;
        }
        if (clientes.putIfAbsent(nombre, handler) != null) { //Lo agrega pero si 
            return false;
        }
        conectados.remove(handler);
        return true;
    }

    public void removerCliente(ClientHandler handler) {
        conectados.remove(handler);

        String nombre = handler.getNombre();
        if (nombre != null && clientes.remove(nombre, handler) && controlador != null) {
            controlador.jugadorDesconectado(nombre);
        }
    }

    public void mandarACliente(String nombre, MensajeServidor msg) {
        if (Constantes.NOMBRE_UNIVERSAL.equals(nombre)) {
            mandarATodos(msg);
            return;
        }
        ClientHandler handler = clientes.get(nombre);
        if (handler != null) {
            handler.mandar(msg);
        }
    }

    public void mandarATodos(MensajeServidor msg) {
        for (ClientHandler handler : clientes.values()) {
            handler.mandar(msg);
        }
    }

    // Entrega al sistema (Controlador) un mensaje que viene de un jugador
    public void mandarASistema(MensajeJugador msg) {
        if (controlador != null) {
            controlador.ejecutarMensaje(msg);
        }
    }

    // Puente para el método que ClientHandler ya invoca (no se modificó esa clase)
    public void procesarMensaje(ClientHandler handler, MensajeJugador msg) {
        mandarASistema(msg);
    }

    public int getCantidadClientes() { //Unicamente clientes con nombres ya asignados
        return clientes.size();
    }
}