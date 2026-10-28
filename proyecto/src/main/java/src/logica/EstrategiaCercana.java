package src.logica;

import java.util.ArrayList;
import java.util.List;
import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Ruta;
import src.mapa.Vecinos;

public class EstrategiaCercana implements EstrategiaBot {

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
        List<Nodo> candidatos = new ArrayList<>();
        for (Animal enemigo : partida.obtenerEnemigos(bot)) {
            if (enemigo.getPosicion() == null || enemigo.getPosicion().getNodo() == null) {
                continue;
            }
            for (Vecinos vecinos : mapa.getVecinosDe(enemigo.getPosicion().getNodo())) {
                Nodo candidato = vecinos.getOtro(enemigo.getPosicion().getNodo());
                if (candidato != null && !candidatos.contains(candidato)) {
                    candidatos.add(candidato);
                }
            }
        }

        Ruta mejorRuta = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for (Nodo candidato : candidatos) {
            Ruta ruta = mapa.getRuta(origen, candidato);
            if (ruta == null) {
                continue;
            }
            double distancia = EstrategiaDirecta.distancia(ruta);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                mejorRuta = ruta;
            }
        }
        return mejorRuta;
    }
}
