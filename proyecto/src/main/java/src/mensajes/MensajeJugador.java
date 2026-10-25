package src.mensajes;

public class MensajeJugador extends Mensaje{
    private String nombre;
    public MensajeJugador(TipoMensaje tipo, String nombre) {
        super(tipo);
        this.nombre = nombre;
    }
    public String getNombre() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
