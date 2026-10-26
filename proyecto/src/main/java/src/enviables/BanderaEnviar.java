package src.enviables;

import java.awt.Color;
import src.logica.Bandera;
import src.logica.TipoEntidad;

public class BanderaEnviar extends EntidadEnviar {

    private int idJugador;
    private Color color;

    public BanderaEnviar(int id, PosicionEnviar posicion, int idJugador, Color color) {
        super(id, TipoEntidad.BANDERA, posicion);
    }

    public static BanderaEnviar desde(Bandera bandera) {
        return null;
    }

    public int getIdJugador() {
        return 0;
    }

    public Color getColor() {
        return null;
    }
}
