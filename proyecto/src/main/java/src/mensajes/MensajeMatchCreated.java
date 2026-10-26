package src.mensajes;

// Servidor -> Cliente
public class MensajeMatchCreated extends MensajeServidor {
    private String codigo;

    // Constructor para Jackson
    protected MensajeMatchCreated() {
        super(TipoMensaje.MATCH_CREATED);
    }

    public MensajeMatchCreated(String codigo) {
        super(TipoMensaje.MATCH_CREATED);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
