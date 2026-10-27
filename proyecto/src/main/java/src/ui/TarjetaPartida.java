package src.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import src.logica.EstadoPartida;
import src.logica.ResumenPartida;
import src.utilidades.Constantes;

public class TarjetaPartida extends JPanel {

    private final String codigo;
    private final JLabel etiquetaCodigo = new JLabel();
    private final JLabel etiquetaEstado = new JLabel();
    private final JLabel etiquetaDuracion = new JLabel();
    private final JLabel etiquetaBots = new JLabel();
    private final JLabel etiquetaCafes = new JLabel();
    private final JLabel etiquetaGanador = new JLabel();
    private final ModeloTablaJugadores modeloJugadores = new ModeloTablaJugadores();
    private final JTable tablaJugadores = new JTable(modeloJugadores);

    public TarjetaPartida(String codigo) {
        this.codigo = codigo;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(Constantes.UI_ESPACIO, Constantes.UI_ESPACIO));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constantes.UI_COLOR_BORDE_TARJETA),
                BorderFactory.createEmptyBorder(Constantes.UI_ESPACIO, Constantes.UI_ESPACIO,
                        Constantes.UI_ESPACIO, Constantes.UI_ESPACIO)));

        etiquetaCodigo.setText(Constantes.UI_PREFIJO_CODIGO + codigo);
        etiquetaCodigo.setFont(etiquetaCodigo.getFont().deriveFont(java.awt.Font.BOLD,
                Constantes.UI_TAMANO_TITULO_TARJETA));

        etiquetaEstado.setOpaque(true);
        etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.LEFT, Constantes.UI_ESPACIO, 0));
        cabecera.add(etiquetaCodigo);
        cabecera.add(etiquetaEstado);
        cabecera.add(etiquetaDuracion);

        JPanel datos = new JPanel(new FlowLayout(FlowLayout.LEFT, Constantes.UI_ESPACIO, 0));
        datos.add(etiquetaBots);
        datos.add(etiquetaCafes);
        datos.add(etiquetaGanador);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(cabecera, BorderLayout.NORTH);
        norte.add(datos, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);

        tablaJugadores.setRowSelectionAllowed(false);
        tablaJugadores.setFocusable(false);
        tablaJugadores.setDefaultRenderer(Object.class, new RenderizadorJugador());
        tablaJugadores.setDefaultRenderer(Integer.class, new RenderizadorJugador());

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(tablaJugadores.getTableHeader(), BorderLayout.NORTH);
        centro.add(tablaJugadores, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    public void actualizar(ResumenPartida resumen) {
        if (resumen == null) {
            return;
        }
        EstadoPartida estado = resumen.getEstado();
        etiquetaEstado.setText(String.valueOf(estado));
        etiquetaEstado.setBackground(colorDeEstado(estado));
        etiquetaEstado.setForeground(Constantes.UI_COLOR_TEXTO_ESTADO);
        etiquetaDuracion.setText(Constantes.UI_PREFIJO_DURACION + formatearDuracion(resumen.getDuracionMs()));
        etiquetaBots.setText(Constantes.UI_PREFIJO_BOTS + resumen.getBotsVivos()
                + Constantes.UI_SEPARADOR_BOTS + resumen.getBotsTotales());
        etiquetaCafes.setText(Constantes.UI_PREFIJO_CAFES + resumen.getCafesRestantes());

        String ganador = resumen.getGanador();
        etiquetaGanador.setText(ganador == null ? "" : Constantes.UI_PREFIJO_GANADOR + ganador);

        modeloJugadores.setJugadores(resumen.getJugadores());
        ajustarAlturaTabla();
    }

    private void ajustarAlturaTabla() {
        int filas = Math.max(1, modeloJugadores.getRowCount());
        Dimension anterior = tablaJugadores.getPreferredSize();
        int alto = filas * tablaJugadores.getRowHeight();
        if (anterior.height != alto) {
            tablaJugadores.setPreferredSize(new Dimension(anterior.width, alto));
            revalidate();
        }
    }

    private static String formatearDuracion(long ms) {
        long totalSegundos = Math.max(0, ms) / Constantes.UI_MS_POR_SEGUNDO;
        long horas = totalSegundos / Constantes.UI_SEGUNDOS_POR_HORA;
        long minutos = (totalSegundos % Constantes.UI_SEGUNDOS_POR_HORA) / Constantes.UI_SEGUNDOS_POR_MINUTO;
        long segundos = totalSegundos % Constantes.UI_SEGUNDOS_POR_MINUTO;
        if (horas > 0) {
            return String.format(Constantes.UI_FORMATO_DURACION_LARGA, horas, minutos, segundos);
        }
        return String.format(Constantes.UI_FORMATO_DURACION_CORTA, minutos, segundos);
    }

    // ESPERANDO = amarillo, SELECCION = azul, JUGANDO = verde, TERMINADA = gris
    private Color colorDeEstado(EstadoPartida estado) {
        if (estado == null) {
            return Constantes.UI_COLOR_ESTADO_TERMINADA;
        }
        switch (estado) {
            case ESPERANDO:
                return Constantes.UI_COLOR_ESTADO_ESPERANDO;
            case SELECCION:
                return Constantes.UI_COLOR_ESTADO_SELECCION;
            case JUGANDO:
                return Constantes.UI_COLOR_ESTADO_JUGANDO;
            case TERMINADA:
            default:
                return Constantes.UI_COLOR_ESTADO_TERMINADA;
        }
    }

    // Jugador desconectado se muestra en rojo
    private class RenderizadorJugador extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            Component c = super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
            c.setForeground(tabla.getForeground());
            return c;
        }
    }
}
