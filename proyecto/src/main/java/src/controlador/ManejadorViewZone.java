package src.controlador;

import src.logica.Partida;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeViewZone;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;

public class ManejadorViewZone implements ManejadorMensaje {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.VIEW_ZONE;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        MensajeViewZone mensaje = (MensajeViewZone) msg;
        String nombre = mensaje.getNombre();

        Partida partida = controlador.getPartidaDe(nombre);
        if (partida == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_SIN_PARTIDA));
            return;
        }
        partida.verZona(nombre, mensaje.getIdZona());
    }
}
