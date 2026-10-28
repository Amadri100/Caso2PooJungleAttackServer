package src.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import src.utilidades.Constantes;
import src.utilidades.EntradaLog;
import src.utilidades.LogListener;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public class PanelConsola extends JPanel implements LogListener {

    private final JTextPane areaTexto = new JTextPane();
    private final JScrollPane scroll = new JScrollPane(areaTexto);
    private volatile boolean autoScroll = true;
    private NivelLog filtroNivel = NivelLog.DEBUG;
    private String filtroTexto = "";
    private int lineas = 0;

    // Entradas recibidas (sin filtrar), para poder re-filtrar sin perder lo ocurrido
    private final ArrayDeque<EntradaLog> recibidas = new ArrayDeque<>();
    // Entradas llegadas desde el hilo del Logger, pendientes de pasar al hilo de Swing
    private final Queue<EntradaLog> pendientes = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean vaciadoProgramado = new AtomicBoolean(false);
    // Entradas del historial ya mostradas, para no duplicarlas si tambien llegan por onLog
    private final Set<EntradaLog> yaCargadas = Collections.newSetFromMap(new IdentityHashMap<>());

    public PanelConsola() {
        super(new BorderLayout());
        areaTexto.setEditable(false);
        areaTexto.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN,
                Constantes.UI_TAMANO_FUENTE_CONSOLA));
        ((DefaultCaret) areaTexto.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        add(scroll, BorderLayout.CENTER);

        // Primero se registra y luego se carga el historial: no se pierde nada, y los duplicados se descartan
        Logger.getInstance().agregarListener(this);
        cargarHistorial(Logger.getInstance().getHistorial());
    }

    // Se ejecuta en el hilo del Logger: solo encola y pide un vaciado en el hilo de Swing
    @Override
    public void onLog(EntradaLog entrada) {
        pendientes.offer(entrada);
        if (vaciadoProgramado.compareAndSet(false, true)) {
            SwingUtilities.invokeLater(this::vaciarPendientes);
        }
    }

    @Override
    public void finalLote() {
        // Sin trabajo extra: el vaciado ya agrupa las entradas en lotes
    }

    @Override
    public void finalEjecucion() {
        // La consola no tiene recursos propios que cerrar
    }

    private void vaciarPendientes() {
        vaciadoProgramado.set(false);
        boolean huboCambios = false;
        EntradaLog entrada;
        while ((entrada = pendientes.poll()) != null) {
            if (!yaCargadas.isEmpty()) {
                if (yaCargadas.remove(entrada)) {
                    continue;
                }
                yaCargadas.clear();
            }
            registrar(entrada);
            huboCambios = true;
        }
        if (huboCambios) {
            recortarSiSobrepasa();
            irAlFinalSiCorresponde();
        }
    }

    public void cargarHistorial(List<EntradaLog> entradas) {
        if (entradas == null) {
            return;
        }
        for (EntradaLog entrada : entradas) {
            yaCargadas.add(entrada);
            registrar(entrada);
        }
        recortarSiSobrepasa();
        irAlFinalSiCorresponde();
    }

    private void registrar(EntradaLog entrada) {
        if (recibidas.size() >= Constantes.UI_MAX_LINEAS_CONSOLA) {
            recibidas.pollFirst();
        }
        recibidas.addLast(entrada);
        agregarLinea(entrada);
    }

    public void limpiar() {
        recibidas.clear();
        reconstruir();
    }

    public void setAutoScroll(boolean activo) {
        this.autoScroll = activo;
        irAlFinalSiCorresponde();
    }

    public void setFiltroNivel(NivelLog nivel) {
        this.filtroNivel = (nivel == null) ? NivelLog.DEBUG : nivel;
        reconstruir();
    }

    public void setFiltroTexto(String texto) {
        this.filtroTexto = (texto == null) ? "" : texto.trim().toLowerCase();
        reconstruir();
    }

    // Lo visible en la consola (ya filtrado)
    public String getTextoVisible() {
        return areaTexto.getText();
    }

    private void reconstruir() {
        areaTexto.setText("");
        lineas = 0;
        for (EntradaLog entrada : recibidas) {
            agregarLinea(entrada);
        }
        recortarSiSobrepasa();
        irAlFinalSiCorresponde();
    }

    private boolean pasaFiltros(EntradaLog entrada) {
        if (entrada.getNivel().compareTo(filtroNivel) < 0) {
            return false;
        }
        return filtroTexto.isEmpty() || entrada.toString().toLowerCase().contains(filtroTexto);
    }

    private void agregarLinea(EntradaLog entrada) {
        if (!pasaFiltros(entrada)) {
            return;
        }
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, colorDeNivel(entrada.getNivel()));
        StyledDocument doc = areaTexto.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), entrada.toString() + System.lineSeparator(), estilo);
            lineas++;
        } catch (BadLocationException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.UI_ORIGEN_CONSOLA,
                    Constantes.UI_ERROR_AGREGAR_LINEA, e);
        }
    }

    private void recortarSiSobrepasa() {
        if (lineas <= Constantes.UI_MAX_LINEAS_CONSOLA) {
            return;
        }
        int sobrantes = lineas - Constantes.UI_MAX_LINEAS_CONSOLA;
        StyledDocument doc = areaTexto.getStyledDocument();
        Element raiz = doc.getDefaultRootElement();
        int ultimaLineaABorrar = Math.min(sobrantes, raiz.getElementCount()) - 1;
        if (ultimaLineaABorrar < 0) {
            return;
        }
        int fin = raiz.getElement(ultimaLineaABorrar).getEndOffset();
        try {
            doc.remove(0, Math.min(fin, doc.getLength()));
            lineas -= sobrantes;
        } catch (BadLocationException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.UI_ORIGEN_CONSOLA,
                    Constantes.UI_ERROR_RECORTAR_CONSOLA, e);
        }
    }

    private void irAlFinalSiCorresponde() {
        if (autoScroll) {
            areaTexto.setCaretPosition(areaTexto.getDocument().getLength());
        }
    }

    // DEBUG gris, INFO color normal, WARN naranja, ERROR rojo
    private Color colorDeNivel(NivelLog nivel) {
        switch (nivel) {
            case DEBUG:
                return Constantes.UI_COLOR_LOG_DEBUG;
            case WARN:
                return Constantes.UI_COLOR_LOG_WARN;
            case ERROR:
                return Constantes.UI_COLOR_LOG_ERROR;
            case INFO:
            default:
                return areaTexto.getForeground();
        }
    }
}
