package src.ui;

import java.awt.FlowLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import src.utilidades.Constantes;
import src.utilidades.GeneradorAleatorio;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public class PanelControl extends JPanel {

    private final PanelConsola panelConsola;
    private final JTextField campoSeed = new JTextField(Constantes.UI_COLUMNAS_CAMPO_SEED);
    private final JCheckBox checkSeedFija = new JCheckBox(Constantes.UI_TEXTO_SEED_FIJA);
    private final JButton botonAplicarSeed = new JButton(Constantes.UI_TEXTO_APLICAR_SEED);
    private final JButton botonSeedAleatoria = new JButton(Constantes.UI_TEXTO_SEED_ALEATORIA);
    private final JComboBox<NivelLog> comboNivel = new JComboBox<>(NivelLog.values());
    private final JCheckBox checkAutoScroll = new JCheckBox(Constantes.UI_TEXTO_AUTOSCROLL, true);
    private final JButton botonLimpiar = new JButton(Constantes.UI_TEXTO_LIMPIAR);
    private final JTextField campoFiltro = new JTextField(Constantes.UI_COLUMNAS_CAMPO_FILTRO);
    private final JButton botonGuardar = new JButton(Constantes.UI_TEXTO_GUARDAR);

    public PanelControl(PanelConsola panelConsola) {
        super(new FlowLayout(FlowLayout.LEFT, Constantes.UI_ESPACIO, Constantes.UI_ESPACIO));
        this.panelConsola = panelConsola;
        construir();
        conectarEventos();
    }

    private void construir() {
        campoSeed.setText(String.valueOf(GeneradorAleatorio.getInstance().getSeed()));
        comboNivel.setSelectedItem(NivelLog.INFO);

        add(new JLabel(Constantes.UI_TEXTO_ETIQUETA_SEED));
        add(campoSeed);
        add(checkSeedFija);
        add(botonAplicarSeed);
        add(botonSeedAleatoria);
        add(new JLabel(Constantes.UI_TEXTO_ETIQUETA_NIVEL));
        add(comboNivel);
        add(checkAutoScroll);
        add(new JLabel(Constantes.UI_TEXTO_ETIQUETA_FILTRO));
        add(campoFiltro);
        add(botonLimpiar);
        add(botonGuardar);
    }

    private void conectarEventos() {
        botonAplicarSeed.addActionListener(e -> aplicarSeed());
        botonSeedAleatoria.addActionListener(e -> generarSeedAleatoria());
        comboNivel.addActionListener(e -> {
            NivelLog nivel = (NivelLog) comboNivel.getSelectedItem();
            if (nivel != null) {
                Logger.getInstance().setNivelMinimo(nivel);
                panelConsola.setFiltroNivel(nivel);
            }
        });
        checkAutoScroll.addActionListener(e -> panelConsola.setAutoScroll(checkAutoScroll.isSelected()));
        botonLimpiar.addActionListener(e -> panelConsola.limpiar());
        botonGuardar.addActionListener(e -> guardar());
        campoFiltro.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                panelConsola.setFiltroTexto(campoFiltro.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                panelConsola.setFiltroTexto(campoFiltro.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                panelConsola.setFiltroTexto(campoFiltro.getText());
            }
        });
    }

    // Solo afecta a partidas nuevas: las que ya corren conservan su stream
    private void aplicarSeed() {
        long seed;
        try {
            seed = Long.parseLong(campoSeed.getText().trim());
        } catch (NumberFormatException e) {
            Logger.getInstance().crearLog(NivelLog.WARN, Constantes.UI_ORIGEN_CONTROL,
                    Constantes.UI_ERROR_SEED_INVALIDA + campoSeed.getText(), e);
            JOptionPane.showMessageDialog(this, Constantes.UI_ERROR_SEED_INVALIDA_DIALOGO,
                    Constantes.UI_TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }
        GeneradorAleatorio.getInstance().setSeed(seed);
        GeneradorAleatorio.getInstance().setSeedFija(checkSeedFija.isSelected());
        Logger.getInstance().crearLog(NivelLog.INFO, Constantes.UI_ORIGEN_CONTROL,
                Constantes.UI_MENSAJE_SEED_APLICADA + seed + Constantes.UI_MENSAJE_SEED_FIJA
                        + checkSeedFija.isSelected() + Constantes.UI_MENSAJE_SOLO_NUEVAS,
                null);
    }

    private void generarSeedAleatoria() {
        long seed = new Random().nextLong();
        campoSeed.setText(String.valueOf(seed));
        GeneradorAleatorio.getInstance().setSeed(seed);
        Logger.getInstance().crearLog(NivelLog.INFO, Constantes.UI_ORIGEN_CONTROL,
                Constantes.UI_MENSAJE_SEED_GENERADA + seed + Constantes.UI_MENSAJE_SOLO_NUEVAS, null);
    }

    // Exporta lo visible en la consola
    private void guardar() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new java.io.File(Constantes.UI_NOMBRE_ARCHIVO_EXPORT));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path destino = selector.getSelectedFile().toPath();
        try {
            Files.writeString(destino, panelConsola.getTextoVisible(), StandardCharsets.UTF_8);
            Logger.getInstance().crearLog(NivelLog.INFO, Constantes.UI_ORIGEN_CONTROL,
                    Constantes.UI_MENSAJE_CONSOLA_GUARDADA + destino, null);
        } catch (IOException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.UI_ORIGEN_CONTROL,
                    Constantes.UI_ERROR_GUARDAR_CONSOLA + destino, e);
            JOptionPane.showMessageDialog(this, Constantes.UI_ERROR_GUARDAR_CONSOLA_DIALOGO,
                    Constantes.UI_TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
        }
    }
}
