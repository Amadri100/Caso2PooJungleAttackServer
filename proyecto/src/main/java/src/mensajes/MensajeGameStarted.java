package src.mensajes;

import src.enviables.DatosEnviar;

// Servidor -> Cliente
// datos = estado inicial visible para ese jugador.
public class MensajeGameStarted extends MensajeServidor {
    private DatosEnviar datos;

    // Constructor para Jackson
    protected MensajeGameStarted() {
        super(TipoMensaje.GAME_STARTED);
    }

    public MensajeGameStarted(DatosEnviar datos) {
        super(TipoMensaje.GAME_STARTED);
        this.datos = datos;
    }

    public DatosEnviar getDatos() {
        return datos;
    }
}
