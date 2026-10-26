package src.server;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadConexiones implements Runnable {

    private ServerSocket serverSocket;
    private Servidor servidor;
    private volatile boolean activo;

    public ThreadConexiones(Servidor servidor, int puerto)  throws IOException{
        this.servidor = servidor;
        this.activo = true;
        this.serverSocket = new ServerSocket(puerto);
    }

    // Hilo que acepta conexiones. Por cada Socket crea un ClientHandler
    // y se lo entrega a Servidor.registrarCliente().
    @Override
    public void run() {
        while (activo) {
            Socket socket = null;
            try {
                socket = serverSocket.accept(); //El codigo se queda aqui hasta recibir una señal
                ClientHandler handler = new ClientHandler(socket, servidor);
                servidor.registrarCliente(handler);
            } catch (IOException | UncheckedIOException e) {
                if (!activo) { //Termina
                    break;
                }
                // Falló una conexión puntual: se descarta ese socket y se sigue aceptando
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ignorada) {
                        // Ya estaba cerrado
                    }
                }
            }
        }
    }

    public void detener() {
        activo = false;
        try {
            // Cerrar el ServerSocket desbloquea el accept() que está esperando
            serverSocket.close();
        } catch (IOException e) {
            // Ya estaba cerrado
        }
    }

}