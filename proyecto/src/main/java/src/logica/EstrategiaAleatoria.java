package src.logica;

import java.util.ArrayList;
import java.util.List;
import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Ruta;
import src.mapa.Zona;
import src.utilidades.GeneradorAleatorio;

public class EstrategiaAleatoria implements EstrategiaBot {

    @Override
    public Ruta elegirDestino(Bot bot, Partida partida) {
        Animal origenAnimal = bot.getLista().stream()
                .filter(Animal::estaVivo)
                .filter(animal -> animal.getPosicion() != null)
                .filter(animal -> animal.getPosicion().getNodo() != null)
                .findFirst()
                .orElse(null);
        if (origenAnimal == null) {
            return null;
        }

        Mapa mapa = Mapa.getInstance();
        Nodo origen = origenAnimal.getPosicion().getNodo();
        ArrayList<Nodo> destinos = new ArrayList<>();
        for (Zona zona : mapa.getZonas()) {
            destinos.addAll(zona.getNodos());
        }
        List<Nodo> alcanzables = new ArrayList<>();
        for (Nodo destino : destinos) {
            if (!destino.equals(origen) && mapa.getRuta(origen, destino) != null) {
                alcanzables.add(destino);
            }
        }
        Nodo destino = GeneradorAleatorio.getInstance().elegir(alcanzables);
        return destino == null ? null : mapa.getRuta(origen, destino);
    }
}
