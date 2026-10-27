package src.controlador;

import java.util.List;

import src.logica.Partida;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJoinMatch;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeMatchJoined;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes; 

public class ManejadorJoinMatch implements ManejadorMensaje {
    
    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.JOIN_MATCH;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        MensajeJoinMatch mensaje = (MensajeJoinMatch) msg;
        String nombre = mensaje.getNombre();
        String codigo = mensaje.getCodigo();

        if (!controlador.unirsePartida(codigo, nombre)) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_UNIRSE));
            return;
        }

        // Todos los jugadores de la partida reciben la lista actualizada
        Partida partida = controlador.getPartidaDe(nombre);
        if (partida == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_SIN_PARTIDA));
            return;
        }
        List<String> jugadores = partida.getNombresJugadores();
        MensajeMatchJoined aviso = new MensajeMatchJoined(codigo, jugadores);
        for (String jugador : jugadores) {
            controlador.enviar(jugador, aviso);
        }
    }
}
