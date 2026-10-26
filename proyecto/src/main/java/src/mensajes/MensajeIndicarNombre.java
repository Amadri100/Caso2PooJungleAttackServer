package src.mensajes;

// Cliente -> Servidor. Solo usa el nombre heredado.
public class MensajeIndicarNombre extends MensajeJugador {

    // Constructor para Jackson
    protected MensajeIndicarNombre() {
        super(TipoMensaje.INDICAR_NOMBRE, null);
    }

    public MensajeIndicarNombre(String nombre) {
        super(TipoMensaje.INDICAR_NOMBRE, nombre);
    }
}
