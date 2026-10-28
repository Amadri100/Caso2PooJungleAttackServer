package src.enviables;

import java.util.Objects;
import src.logica.Bandera;
import src.logica.TipoEntidad;

public class BanderaEnviar extends EntidadEnviar {

    private final int idJugador;
    private final String color; // Formato hexadecimal "#RRGGBB"

    public BanderaEnviar(int id, PosicionEnviar posicion, int idJugador, String color) {
        super(id, TipoEntidad.BANDERA, posicion);
        this.idJugador = idJugador;
        this.color = Objects.requireNonNull(color, "El color es obligatorio.");
    }

    public static BanderaEnviar desde(Bandera bandera) {
        Objects.requireNonNull(bandera, "La bandera es obligatoria.");
        if (bandera.getJugador() == null) {
            throw new IllegalStateException("La bandera debe tener un jugador propietario.");
        }
        java.awt.Color color = Objects.requireNonNull(
                bandera.getColor(), "La bandera debe tener un color.");
        return new BanderaEnviar(
                bandera.getId(),
                PosicionEnviar.desde(bandera.getPosicion()),
                bandera.getJugador().getId(),
                aHexadecimal(color));
    }

    // Color.toString() produce "java.awt.Color[r=..]", que el cliente no puede interpretar
    private static String aHexadecimal(java.awt.Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public int getIdJugador() {
        return idJugador;
    }

    public String getColor() {
        return color;
    }
}