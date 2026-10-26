package src.mensajes;

// Cliente -> Servidor
public class MensajeViewZone extends MensajeJugador {
    private int idZona;

    // Constructor para Jackson
    protected MensajeViewZone() {
        super(TipoMensaje.VIEW_ZONE, null);
    }

    public MensajeViewZone(String nombre, int idZona) {
        super(TipoMensaje.VIEW_ZONE, nombre);
        this.idZona = idZona;
    }

    public int getIdZona() {
        return idZona;
    }
}
