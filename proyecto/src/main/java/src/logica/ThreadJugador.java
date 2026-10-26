package src.logica;

import java.util.ArrayList;
import java.util.List;

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

    public void setObjetivo(Animal objetivo) {}

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