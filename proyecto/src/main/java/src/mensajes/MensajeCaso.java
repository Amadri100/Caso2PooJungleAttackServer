package src.mensajes;

// Servidor -> Cliente
// caso indica el éxito (true) o el fallo (false) de la operación; descripcion explica el motivo
// (Constantes.NO_HAY_ERROR cuando todo salió bien).
public class MensajeCaso extends MensajeServidor {
    private boolean caso;
    private String descripcion;

    // Constructor para Jackson
    protected MensajeCaso() {
        super(TipoMensaje.CASO);
    }

    public MensajeCaso(boolean caso, String descripcion) {
        super(TipoMensaje.CASO);
        this.caso = caso;
        this.descripcion = descripcion;
    }

    // Atajo para un caso fallido: caso = false
    public MensajeCaso(String descripcion) {
        this(false, descripcion);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCaso() {
        return caso;
    }
}
