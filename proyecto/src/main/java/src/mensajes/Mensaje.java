package src.mensajes;

public  class Mensaje {
    private TipoMensaje tipo;
    public Mensaje(TipoMensaje tipo) {
        this.tipo = tipo;
    }

    public TipoMensaje getTipo() {
        return this.tipo;
    }
}
