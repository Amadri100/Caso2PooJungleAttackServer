package src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import src.mensajes.Mensaje;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeIndicarNombre;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeServidor;
import src.utilidades.Constantes;
import src.utilidades.Json;

public class ClientHandler implements Runnable {


    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Servidor servidor;
    private volatile String nombre;
    private volatile boolean activo;

    public ClientHandler(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
        this.activo = true;
        try {
            this.entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void run() {
        try {
            String linea;
            while (activo && (linea = entrada.readLine()) != null) {
                Mensaje recibido;
                try {
                    recibido = Json.jsonA(linea);
                } catch (IllegalArgumentException e) {
                    // JSON vacío o inválido: se ignora la línea
                    continue;
                }
                if (recibido instanceof MensajeIndicarNombre) {
                    manejarNombre((MensajeIndicarNombre) recibido);
                } else if (recibido instanceof MensajeJugador && nombre != null) {
                    // Solo se aceptan mensajes de clientes que ya tienen nombre
                    ascenderMensaje((MensajeJugador) recibido);
                }
            }
        } catch (IOException e) {
            // Conexión perdida o cerrada: se limpia en el finally
        } finally {
            cerrar();
        }
    }

    private void manejarNombre(MensajeIndicarNombre msg) {
        String nombreSolicitado = msg.getNombre();
        if (nombre == null && servidor.asignarNombre(this, nombreSolicitado)) {
            this.nombre = nombreSolicitado;
            mandar(new MensajeCaso(true, Constantes.NO_HAY_ERROR));
        } else {
            mandar(new MensajeCaso(false, Constantes.ERROR_NOMBRE_NO_DISPONIBLE));
        }
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
        servidor.procesarMensaje(this, mensaje);
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