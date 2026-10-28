package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import src.mensajes.Mensaje;
import src.mensajes.MensajeIndicarNombre;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajePrimerMensaje;
import src.mensajes.MensajeServidor;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;
import src.utilidades.Json;

public class ClientHandler implements Runnable {


    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Servidor servidor;
    private volatile String nombre;
    private volatile boolean activo;

    public ClientHandler(Socket socket, Servidor servidor) throws IOException {
        this.socket = socket;
        this.servidor = servidor;
        this.activo = true;
        InputStreamReader input = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
        this.entrada = new BufferedReader(input);
        this.salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

    }

    @Override
    public void run() {
        String linea;
        Mensaje recibido;
        try {
            while (activo && (linea = entrada.readLine()) != null) {
                try {
                    recibido = Json.jsonA(linea);
                } catch (IllegalArgumentException e) {
                    // JSON vacío o inválido: se ignora la línea
                    continue;
                }
                if (recibido.getTipo() == TipoMensaje.INDICAR_NOMBRE) {
                    manejarNombre((MensajeIndicarNombre) recibido);
                } else if (recibido instanceof MensajeJugador && nombre != null) {
                    // Solo se aceptan mensajes de clientes que ya tienen nombre
                    ascenderMensaje((MensajeJugador) recibido);
                }
                try {
                    Thread.sleep(Constantes.TIEMPO_SLEEP_MINIMO);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); 
                }
            }
        } catch (IOException e) {
            // Conexión perdida o cerrada: se limpia en el finally
        } finally {
            cerrar();
        }
    }

    private void manejarNombre(MensajeIndicarNombre msg) {
        if (nombre != null) {
            mandar(new MensajePrimerMensaje(false, Constantes.ERROR_NOMBRE_YA_ASIGNADO));
            return;
        }
        String nombreSolicitado = msg.getNombre();
        if (!servidor.asignarNombre(this, nombreSolicitado)) {
            mandar(new MensajePrimerMensaje(false, Constantes.ERROR_NOMBRE_NO_DISPONIBLE));
            return;
        }
        this.nombre = nombreSolicitado;
        mandar(new MensajePrimerMensaje(true, null));
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public synchronized void mandar(MensajeServidor mensaje) {
        if (!activo) {
            return;
        }
        salida.println(Json.mensajeAJson(mensaje));
    }

    // Entrega el mensaje recibido al servidor, que lo pasa al sistema
    public void ascenderMensaje(MensajeJugador mensaje) {
        servidor.mandarASistema(mensaje);
    }

    public synchronized void cerrar() {
        if (!activo) {
            return;
        }
        activo = false;
        servidor.removerCliente(this);
        try {
            socket.close();
        } catch (IOException e) {
            // Ya estaba cerrado
        }
    }
}