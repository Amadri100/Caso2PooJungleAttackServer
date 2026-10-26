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


    @Override
    public void run() {
        while (activo) {
            Socket socket = null;
            try {
                socket = serverSocket.accept(); //El codigo se queda aqui hasta recibir una conexion
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
            serverSocket.close();
        } catch (IOException e) {
            // Ya estaba cerrado
        }
    }

}