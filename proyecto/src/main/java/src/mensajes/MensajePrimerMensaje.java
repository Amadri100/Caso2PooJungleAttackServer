package src.mensajes;

// Servidor -> Cliente
// razon indica por qué se rechazó la conexión (null si exito es true).
public class MensajePrimerMensaje extends MensajeServidor {
    private boolean exito;
    private String razon;

    // Constructor para Jackson
    protected MensajePrimerMensaje() {
        super(TipoMensaje.PRIMER_MENSAJE);
    }

    public MensajePrimerMensaje(boolean exito, String razon) {
        super(TipoMensaje.PRIMER_MENSAJE);
        this.exito = exito;
        this.razon = razon;
    }

    public boolean isExito() {
        return exito;
    }

    public String getRazon() {
        return razon;
    }
}
