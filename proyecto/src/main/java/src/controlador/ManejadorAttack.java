package src.controlador;

import src.logica.Partida;
import src.mensajes.MensajeAttack;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeJugador;
import src.mensajes.TipoMensaje;
import src.utilidades.Constantes;

public class ManejadorAttack implements ManejadorMensaje {


    @Override
    public TipoMensaje getTipoMensaje() {
        return TipoMensaje.ATTACK;
    }

    @Override
    public void ejecutar(MensajeJugador msg, Controlador controlador) {
        MensajeAttack mensaje = (MensajeAttack) msg;
        String nombre = mensaje.getNombre();

        Partida partida = controlador.getPartidaDe(nombre);
        if (partida == null) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_SIN_PARTIDA));
            return;
        }
        if (!animalValido(mensaje.getAtacante()) || !animalValido(mensaje.getEnemigo())) {
            controlador.enviar(nombre, new MensajeCaso(Constantes.ERROR_ANIMALES));
            return;
        }
        partida.mandarAtaque(nombre, mensaje.getAtacante(), mensaje.getEnemigo());
    }

    // Cada animal debe venir como int[]{idPack, idAnimal}
    private boolean animalValido(int[] animal) {
        return animal != null && animal.length == 2;
    }
}
