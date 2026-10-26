package src.controlador;

import java.util.HashMap;

import src.mensajes.MensajeJugador;
import src.mensajes.TipoMensaje;

public class EjecutorMensajes {
    private HashMap<TipoMensaje, ManejadorMensaje> manejadores;
    private Controlador controlador;
    public EjecutorMensajes(Controlador controlador) {
        this.controlador = controlador;
        manejadores = new HashMap<>();
    }
    public void registrar(ManejadorMensaje manejador) {
        this.manejadores.put(manejador.getTipoMensaje(), manejador);
    }
    public void ejecutar(MensajeJugador msg) {
        ManejadorMensaje manejo = manejadores.get(msg.getTipo());
        if (manejo != null) {
            manejo.ejecutar(msg, controlador);
        }
    }
}
