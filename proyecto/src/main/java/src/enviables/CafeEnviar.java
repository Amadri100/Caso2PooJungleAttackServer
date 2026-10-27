package src.enviables;

import java.util.Objects;
import src.logica.Cafe;
import src.logica.TipoEntidad;

public class CafeEnviar extends EntidadEnviar {

    private final boolean consumido;

    public CafeEnviar(int id, PosicionEnviar posicion, boolean consumido) {
        super(id, TipoEntidad.CAFE, posicion);
        this.consumido = consumido;
    }

    public static CafeEnviar desde(Cafe cafe) {
        Objects.requireNonNull(cafe, "El café es obligatorio.");
        return new CafeEnviar(
                cafe.getId(),
                PosicionEnviar.desde(cafe.getPosicion()),
                !cafe.estaDisponible());
    }

    public boolean isConsumido() {
        return consumido;
    }
}
