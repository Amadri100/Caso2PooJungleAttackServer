package src.utilidades;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Logger {

    private static final Logger instancia = new Logger();

    private final LinkedBlockingQueue<EntradaLog> cola = new LinkedBlockingQueue<>(Constantes.MAX_COLA);
    private final CopyOnWriteArrayList<LogListener> listeners = new CopyOnWriteArrayList<>();
    private final ArrayDeque<EntradaLog> historial = new ArrayDeque<>();

    private volatile NivelLog nivelMinimo = NivelLog.INFO;
    private volatile boolean activo;
    private Thread hilo;

    private Logger() {
    }

    public static Logger getInstance() {
        return instancia;
    }

    public synchronized void iniciar() {
        if (activo) {
            return;
        }
        activo = true;
        hilo = new Thread(this::procesar, Constantes.NOMBRE_THREAD_LOGGER); //Thread para procesar
        hilo.setDaemon(true); //No bloquea el ciere del app
        hilo.start();
    }

 
    public synchronized void cerrar() {
        if (!activo) {
            return;
        }
        activo = false;
        try {
            hilo.join(Constantes.MS_ANTES_DE_MORRIR);
        } catch (InterruptedException e) {
            reportarError(e);
            Thread.currentThread().interrupt();
        }
    }

    public void setNivelMinimo(NivelLog nivel) {
        this.nivelMinimo = nivel;
    }

    public void agregarListener(LogListener listener) {
        listeners.add(listener);
    }

    public void removerListener(LogListener listener) {
        listeners.remove(listener);
    }

    // Copia: el historial interno lo modifica el hilo del logger
    public List<EntradaLog> getHistorial() {
        synchronized (historial) {
            return new ArrayList<>(historial);
        }
    }

    // Filtra por nivel y encola. Si la cola está llena, descarta la entrada.
    public void crearLog(NivelLog nivel, String origen, String mensaje, Throwable causa) {
        if (nivel.compareTo(nivelMinimo) < 0) {
            return;
        }
        cola.offer(new EntradaLog(nivel, origen, mensaje, causa));
    }

    //Erroes del propio logger no se pueden manda al logger (Cicla el programa)
    //Evita ocultar los erroes.
    static void reportarError(Throwable error) {
        System.err.println(Constantes.ERROR_EN_LOGGER + Constantes.SEPARADOR_ERROR + error);
    }

    private void procesar() {
        try {
            while (activo || !cola.isEmpty()) {
                EntradaLog entrada = cola.poll(Constantes.MS_ESPERA_MAXIMA_COLA, TimeUnit.MILLISECONDS);
                if (entrada == null) {
                    continue;
                }
                guardarEnHistorial(entrada);
                escribirEnListeners(entrada);
                if (cola.isEmpty()) {
                    avisarLoteTerminado();
                }
            }
        } catch (InterruptedException e) {
            reportarError(e);
            Thread.currentThread().interrupt();
        } finally {
            avisarFinalEjecucion();
        }
    }

    private void guardarEnHistorial(EntradaLog entrada) {
        synchronized (historial) {
            if (historial.size() >= Constantes.MAX_HISTORIAL_LOG) {
                historial.pollFirst();
            }
            historial.addLast(entrada);
        }
    }

    // Se ejecuta en el hilo del logger. Si un listener necesita otro hilo (la GUI), lo cambia él.
    private void escribirEnListeners(EntradaLog entrada) {
        for (LogListener listener : listeners) {
            try {
                listener.onLog(entrada);
            } catch (RuntimeException e) {
                // Un listener defectuoso no debe afectar a los demás
                reportarError(e);
            }
        }
    }

    private void avisarLoteTerminado() {
        for (LogListener listener : listeners) {
            try {
                listener.finalLote();
            } catch (RuntimeException e) {
                reportarError(e);
            }
        }
    }

    // Último paso del hilo: cada listener cierra lo que tenga abierto
    private void avisarFinalEjecucion() {
        for (LogListener listener : listeners) {
            try {
                listener.finalEjecucion();
            } catch (RuntimeException e) {
                reportarError(e);
            }
        }
    }

}