package src.utilidades;

import java.io.BufferedWriter;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Logger {
    private static Logger instancia;
    private Path archivo;
    private BufferedWriter escritor;
    private CopyOnWriteArrayList listeners;
    private ArrayDeque<EntradaLog> historial;
    private ReentrantLock lock;
    private Logger() {

    }
    //Temp
    public static Logger getInstance() {
        return instancia;
    }
    public void iniciar(String ruta) {}
    public void cerrar() {}
    public void setNivelMinimo(NivelLog nivel) {}

    public void agregarListener(LogListener listener) {}
    public void removerListener(LogListener listener) {}
    public ArrayDeque<EntradaLog> getHistorial() {return this.historial;}
    public void crearLog(NivelLog nivel, String origen, String mensaje, Throwable causa) {}
    private void escribirEnListeners(EntradaLog entrada) {}


}
