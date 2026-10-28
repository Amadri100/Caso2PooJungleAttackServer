 package src.logica;

import src.mapa.Ruta;

public interface EstrategiaBot {
    Ruta elegirDestino(Bot bot, Partida partida);
}