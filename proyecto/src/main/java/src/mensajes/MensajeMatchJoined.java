package src.mensajes;

import java.util.List;

// Servidor -> Cliente
public class MensajeMatchJoined extends MensajeServidor {
    private String codigo;
    private List<String> jugadores;

    // Constructor para Jackson
    protected MensajeMatchJoined() {
        super(TipoMensaje.MATCH_JOINED);
    }

    public MensajeMatchJoined(String codigo, List<String> jugadores) {
        super(TipoMensaje.MATCH_JOINED);
        this.codigo = codigo;
        this.jugadores = jugadores;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<String> getJugadores() {
        return jugadores;
    }
}
