package src.mensajes;

import src.enviables.DatosEnviar;

// Servidor -> Cliente
// datos = estado actual visible para ese jugador.
public class MensajeStateUpdate extends MensajeServidor {
    private DatosEnviar datos;

    // Constructor para Jackson
    protected MensajeStateUpdate() {
        super(TipoMensaje.STATE_UPDATE);
    }

    public MensajeStateUpdate(DatosEnviar datos) {
        super(TipoMensaje.STATE_UPDATE);
        this.datos = datos;
    }

    public DatosEnviar getDatos() {
        return datos;
    }
}
