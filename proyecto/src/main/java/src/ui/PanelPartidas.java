package src.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import src.logica.ResumenPartida;
import src.utilidades.Constantes;

public class PanelPartidas extends JPanel {

    private final JPanel contenedor = new ContenedorVertical();
    private final Map<String, TarjetaPartida> tarjetas = new LinkedHashMap<>();
    private final JLabel etiquetaVacio = new JLabel(Constantes.UI_TEXTO_SIN_PARTIDAS, SwingConstants.CENTER);

    public PanelPartidas() {
        super(new BorderLayout());
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBorder(BorderFactory.createEmptyBorder(Constantes.UI_ESPACIO, Constantes.UI_ESPACIO,
                Constantes.UI_ESPACIO, Constantes.UI_ESPACIO));

        JPanel envoltorio = new ContenedorVertical();
        envoltorio.setLayout(new BorderLayout());
        envoltorio.add(contenedor, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(envoltorio,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(Constantes.UI_INCREMENTO_SCROLL);
        scroll.setBorder(BorderFactory.createTitledBorder(Constantes.UI_TITULO_PARTIDAS));
        add(scroll, BorderLayout.CENTER);

        contenedor.add(etiquetaVacio);
    }

    // Se llama desde el hilo de Swing con los resumenes actuales
    public void actualizar(List<ResumenPartida> resumenes) {
        List<ResumenPartida> actuales = (resumenes == null) ? new ArrayList<>() : resumenes;

        Set<String> codigosActuales = new HashSet<>();
        boolean cambioEstructura = false;
        for (ResumenPartida resumen : actuales) {
            if (resumen == null) {
                continue;
            }
            codigosActuales.add(resumen.getCodigo());
            if (!tarjetas.containsKey(resumen.getCodigo())) {
                tarjetas.put(resumen.getCodigo(), new TarjetaPartida(resumen.getCodigo()));
                cambioEstructura = true;
            }
        }
        if (tarjetas.keySet().removeIf(codigo -> !codigosActuales.contains(codigo))) {
            cambioEstructura = true;
        }

        for (ResumenPartida resumen : actuales) {
            if (resumen != null) {
                tarjetas.get(resumen.getCodigo()).actualizar(resumen);
            }
        }

        if (cambioEstructura) {
            reconstruirContenedor();
        }
    }

    private void reconstruirContenedor() {
        contenedor.removeAll();
        if (tarjetas.isEmpty()) {
            contenedor.add(etiquetaVacio);
        } else {
            for (TarjetaPartida tarjeta : tarjetas.values()) {
                tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
                contenedor.add(tarjeta);
                contenedor.add(javax.swing.Box.createVerticalStrut(Constantes.UI_ESPACIO));
            }
        }
        contenedor.revalidate();
        contenedor.repaint();
    }

    // Panel que sigue el ancho del viewport para que las tarjetas no desborden horizontalmente
    private static class ContenedorVertical extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return Constantes.UI_INCREMENTO_SCROLL;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return visible.height;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
