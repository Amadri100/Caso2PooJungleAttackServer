package src.logica;

import java.util.ArrayList;
import java.util.List;

import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Ruta;

public class ThreadJugador extends ThreadAnimal {

    private Jugador animal;
    private List<int[][]> ataquesPendientes;

    public ThreadJugador(Partida partida, Jugador animal) {
        super(partida);
        this.animal = animal;
        this.ataquesPendientes = new ArrayList<>();
    }

    @Override
    public void run() {}

    public Jugador getAnimal() {
        return this.animal;
    }

    public void setObjetivo(Animal objetivo) {}

    // Ordena a los animales indicados (int[]{idPack, idAnimal}) ir hacia un nodo.
    // Cada uno recibe su propia ruta desde el nodo en el que está ahora.
    public void moverAnimales(List<int[]> animales, int[] idNodo) {
        Mapa mapa = Mapa.getInstance();
        Nodo destino = mapa.getNodo(idNodo);
        if (destino == null) {
            return;
        }
        for (int[] id : animales) {
            if (this.animal.getLista().size() > id[1]) {
                Animal animal = this.animal.getLista().get(id[1]);;
                if (animal == null || !animal.estaVivo()) {
                    continue;
                }
                // AJUSTE: se asume Animal.getNodoActual()
                Nodo origen = animal.getPosicion().getNodo();
                if (origen == null) {
                    continue;
                }
                Ruta ruta = mapa.getRuta(origen, destino);
                if (ruta != null) {
                    // AJUSTE: se asume Animal.setRuta(Ruta)
                    animal.setRuta(ruta);
                }
            }

        }
    }

    @Override
    public void detener() {}

    private void moverse() {}

    private void atacar() {}

    @Override
    public void decidir(long ahora) {}

    public void agregarAtaque(int[] idJugador, int[] idEnemigo) {}

    private boolean verificarBandera() {
        return false;
    }

}