package src.enviables;
import src.logica.*;
public class CafeEnviar extends EntidadEnviar {

    private boolean consumido;

    public CafeEnviar(int id, PosicionEnviar posicion, boolean consumido) {
        super(id, TipoEntidad.CAFE, posicion);
    }

    public static CafeEnviar desde(Cafe cafe) {
        return null;
    }

    public boolean isConsumido() {
        return false;
    }
}
