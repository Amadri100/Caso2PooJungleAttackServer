package src.mensajes;

// Servidor -> Cliente
public class MensajeGameOver extends MensajeServidor {
    private String ganador;

    // Constructor para Jackson
    protected MensajeGameOver() {
        super(TipoMensaje.GAME_OVER);
    }

    public MensajeGameOver(String ganador) {
        super(TipoMensaje.GAME_OVER);
        this.ganador = ganador;
    }

    public String getGanador() {
        return ganador;
    }
}
