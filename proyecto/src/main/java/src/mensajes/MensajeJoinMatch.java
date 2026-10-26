package src.mensajes;

// Cliente -> Servidor
public class MensajeJoinMatch extends MensajeJugador {
    private String codigo;

    // Constructor para Jackson
    protected MensajeJoinMatch() {
        super(TipoMensaje.JOIN_MATCH, null);
    }

    public MensajeJoinMatch(String nombre, String codigo) {
        super(TipoMensaje.JOIN_MATCH, nombre);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
