package src.logica;

import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Ruta;

public class EstrategiaDirecta implements EstrategiaBot {

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

        Nodo origen = origenAnimal.getPosicion().getNodo();
        Mapa mapa = Mapa.getInstance();
        Ruta mejorRuta = null;
        double menorDistancia = Double.POSITIVE_INFINITY;
        for (Animal enemigo : partida.obtenerEnemigos(bot)) {
            if (enemigo.getPosicion() == null || enemigo.getPosicion().getNodo() == null) {
                continue;
            }
            Ruta ruta = mapa.getRuta(origen, enemigo.getPosicion().getNodo());
            if (ruta == null) {
                continue;
            }
            double distancia = distancia(ruta);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                mejorRuta = ruta;
            }
        }
        return mejorRuta;
    }

    static double distancia(Ruta ruta) {
        double distancia = 0.0;
        for (int indice = 1; indice < ruta.getNodos().size(); indice++) {
            Nodo anterior = ruta.getNodos().get(indice - 1);
            Nodo siguiente = ruta.getNodos().get(indice);
            double dx = siguiente.getPosicion().getX() - anterior.getPosicion().getX();
            double dy = siguiente.getPosicion().getY() - anterior.getPosicion().getY();
            distancia += Math.hypot(dx, dy);
        }
        return distancia;
    }
}
