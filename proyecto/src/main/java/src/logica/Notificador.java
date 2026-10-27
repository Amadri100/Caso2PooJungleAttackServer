package src.logica;

import java.util.HashMap;
import java.util.Map;

import src.enviables.DatosEnviar;
import src.mensajes.MensajeGameOver;
import src.mensajes.MensajeGameStarted;
import src.mensajes.MensajeStateUpdate;

public class Notificador {

    private final Partida partida;

    public Notificador(Partida partida) {
        this.partida = partida;
    }

    // Primer envío del estado: avisa a cada jugador de que empezó la partida (GAME_STARTED)
    public void enviarInicio(HashMap<String, DatosEnviar> datos) {
        for (Map.Entry<String, DatosEnviar> entrada : datos.entrySet()) {
            partida.getControlador().enviar(entrada.getKey(), new MensajeGameStarted(entrada.getValue()));
        }
    }

    // Envía a cada jugador su estado actual (STATE_UPDATE)
    public void enviarDatos(HashMap<String, DatosEnviar> datos) {
        for (Map.Entry<String, DatosEnviar> entrada : datos.entrySet()) {
            partida.getControlador().enviar(entrada.getKey(), new MensajeStateUpdate(entrada.getValue()));
        }
    }

    // Avisa a los jugadores de esta partida de quién ganó (GAME_OVER)
    public void enviarFin(String ganador) {
        MensajeGameOver mensaje = new MensajeGameOver(ganador);
        for (String nombre : partida.getNombresJugadores()) {
            partida.getControlador().enviar(nombre, mensaje);
        }
    }
}