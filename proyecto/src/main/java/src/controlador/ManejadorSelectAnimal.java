package src.controlador;

import src.logica.Partida;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeSelectAnimal;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;

public class ManejadorSelectAnimal implements ManejadorMensaje {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.SELECT_ANIMAL;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        MensajeSelectAnimal mensaje = (MensajeSelectAnimal) msg;
        String nombre = mensaje.getNombre();

        Partida partida = controlador.getPartidaDe(nombre);
        if (partida == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_SIN_PARTIDA));
            return;
        }
        if (mensaje.getEspecie() == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_ESPECIE));
            return;
        }
        partida.seleccionarEspecie(nombre, mensaje.getEspecie());
    }
}
