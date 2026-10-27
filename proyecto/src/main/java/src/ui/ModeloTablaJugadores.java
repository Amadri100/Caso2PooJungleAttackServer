package src.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import src.logica.ResumenJugador;
import src.utilidades.Constantes;

public class ModeloTablaJugadores extends AbstractTableModel {

    private List<ResumenJugador> filas = new ArrayList<>();

    public void setJugadores(List<ResumenJugador> jugadores) {
        filas = (jugadores == null) ? new ArrayList<>() : new ArrayList<>(jugadores);
        fireTableDataChanged();
    }

   /* public boolean estaDesconectado(int fila) {
        if (fila < 0 || fila >= filas.size()) {
            return false;
        }
        return !filas.get(fila).isConectado();
    }*/

    @Override
    public int getRowCount() {
        return filas.size();
    }

    @Override
    public int getColumnCount() {
        return Constantes.UI_COLUMNAS_JUGADORES.length;
    }

    @Override
    public String getColumnName(int col) {
        return Constantes.UI_COLUMNAS_JUGADORES[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return (col >= Constantes.UI_PRIMERA_COLUMNA_NUMERICA) ? Integer.class : String.class;
    }

    // Columnas: nickname, especie, animales vivos, energia, cafes, bots eliminados
    @Override
    public Object getValueAt(int fila, int col) {
        ResumenJugador jugador = filas.get(fila);
        switch (col) {
            case 0:
                return jugador.getNickname();
            case 1:
                return String.valueOf(jugador.getEspecie());
            case 2:
                return jugador.getTotalVivos();
            case 3:
                return jugador.getEnergia();
            case 4:
                return jugador.getCafesConsumidos();
            case 5:
                return jugador.getBotsEliminados();
            default:
                return null;
        }
    }
}
