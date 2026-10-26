package src.controlador;

 
import src.mensajes.MensajeJugador;
import src.mensajes.TipoMensaje;

public interface ManejadorMensaje {

    public TipoMensaje getTipoMensaje();
    public void ejecutar(MensajeJugador msg, Controlador controlador);
}