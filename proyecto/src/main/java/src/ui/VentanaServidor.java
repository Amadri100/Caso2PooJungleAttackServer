package src.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;

import src.controlador.Controlador;
import src.logica.ResumenPartida;
import src.utilidades.Constantes;
import src.utilidades.GeneradorAleatorio;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public class VentanaServidor extends JFrame {

    private final Controlador controlador;
    private PanelPartidas panelPartidas;
    private PanelConsola panelConsola;
    private PanelControl panelControl;
    private JSplitPane divisor;
    private Timer timerRefresco;

    private final JLabel etiquetaClientes = new JLabel();
    private final JLabel etiquetaPartidas = new JLabel();
    private final JLabel etiquetaSeed = new JLabel();

    public VentanaServidor(Controlador controlador) {
        super(Constantes.UI_TITULO_VENTANA);
        this.controlador = controlador;
        construirUI();
    }

    public void iniciarRefresco() {
        if (timerRefresco != null && !timerRefresco.isRunning()) {
            timerRefresco.start();
        }
    }

    public void detenerRefresco() {
        if (timerRefresco != null) {
            timerRefresco.stop();
        }
    }

    private void construirUI() {
        panelConsola = new PanelConsola();
        panelControl = new PanelControl(panelConsola);
        panelPartidas = new PanelPartidas();

        // Derecha: barra de control sobre la consola
        JPanel lado = new JPanel(new BorderLayout());
        lado.add(panelControl, BorderLayout.NORTH);
        lado.add(panelConsola, BorderLayout.CENTER);

        divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelPartidas, lado);
        divisor.setResizeWeight(Constantes.UI_PESO_DIVISOR);
        divisor.setContinuousLayout(true);

        JPanel barraEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, Constantes.UI_ESPACIO_BARRA_ESTADO, 2));
        barraEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Constantes.UI_COLOR_BORDE_TARJETA));
        barraEstado.add(etiquetaClientes);
        barraEstado.add(etiquetaPartidas);
        barraEstado.add(etiquetaSeed);

        setLayout(new BorderLayout());
        add(divisor, BorderLayout.CENTER);
        add(barraEstado, BorderLayout.SOUTH);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                alCerrar();
            }
        });

        timerRefresco = new Timer(Constantes.INTERVALO_REFRESCO_UI_MS, e -> refrescar());
        setSize(Constantes.UI_ANCHO_VENTANA, Constantes.UI_ALTO_VENTANA);
        setLocationRelativeTo(null);
        divisor.setDividerLocation(Constantes.UI_POSICION_DIVISOR);
        refrescar();
    }

    // El controlador no conoce la UI: aqui solo se piden resumenes y datos
    private void refrescar() {
        try {
            List<ResumenPartida> resumenes = controlador.getResumenesPartidas();
            panelPartidas.actualizar(resumenes);
            etiquetaClientes.setText(Constantes.UI_ESTADO_CLIENTES + controlador.cantidadJugadoresConectador());
            etiquetaPartidas.setText(Constantes.UI_ESTADO_PARTIDAS
                    + (resumenes == null ? 0 : resumenes.size()));
            etiquetaSeed.setText(Constantes.UI_ESTADO_SEED + GeneradorAleatorio.getInstance().getSeed());
        } catch (RuntimeException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.UI_ORIGEN_VENTANA,
                    Constantes.UI_ERROR_REFRESCAR, e);
        }
    }

    private void alCerrar() {
        detenerRefresco();
        Logger.getInstance().removerListener(panelConsola);
        try {
            controlador.detenerServidor();
        } catch (RuntimeException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.UI_ORIGEN_VENTANA,
                    Constantes.UI_ERROR_DETENER_SERVIDOR, e);
        }
        Logger.getInstance().cerrar();
        dispose();
    }
}
