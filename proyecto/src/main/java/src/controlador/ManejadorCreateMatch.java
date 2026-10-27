package src.controlador;

import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeMatchCreated;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;

public class ManejadorCreateMatch implements ManejadorMensaje {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.CREATE_MATCH;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        String nombre = msg.getNombre();
        String codigo = controlador.crearJuego(nombre);
        if (codigo == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_CREAR));
            return;
        }
        controlador.enviar(nombre, new MensajeMatchCreated(codigo));
    }
}
