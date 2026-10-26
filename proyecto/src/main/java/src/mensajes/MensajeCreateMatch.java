package src.mensajes;

// Cliente -> Servidor. Sin payload.
public class MensajeCreateMatch extends MensajeJugador {

    // Constructor para Jackson
    protected MensajeCreateMatch() {
        super(TipoMensaje.CREATE_MATCH, null);
    }

    public MensajeCreateMatch(String nombre) {
        super(TipoMensaje.CREATE_MATCH, nombre);
    }
}
