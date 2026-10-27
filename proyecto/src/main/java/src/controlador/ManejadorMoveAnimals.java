package src.controlador;

import src.logica.Partida;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeMoveAnimals;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;

public class ManejadorMoveAnimals implements ManejadorMensaje {

    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.MOVE_ANIMALS;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        MensajeMoveAnimals mensaje = (MensajeMoveAnimals) msg;
        String nombre = mensaje.getNombre();

        Partida partida = controlador.getPartidaDe(nombre);
        if (partida == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_SIN_PARTIDA));
            return;
        }
        if (!animalesValidos(mensaje)) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_ANIMALES));
            return;
        }
        partida.moverAnimales(nombre, mensaje.getAnimales(), mensaje.getidNodo());
    }

    // Cada animal debe venir como int[]{idPack, idAnimal}
    private boolean animalesValidos(MensajeMoveAnimals mensaje) {
        if (mensaje.getAnimales() == null || mensaje.getAnimales().isEmpty()) {
            return false;
        }
        for (int[] animal : mensaje.getAnimales()) {
            if (animal == null || animal.length != 2) {
                return false;
            }
        }
        return true;
    }
}
