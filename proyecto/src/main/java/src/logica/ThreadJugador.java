package src.logica;

import java.util.ArrayList;
import java.util.List;

import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Posicion;
import src.mapa.Ruta;
import src.utilidades.Constantes;

public class ThreadJugador extends ThreadAnimal {

    private Jugador animal;
    private List<int[][]> ataquesPendientes;

    public ThreadJugador(Partida partida, Jugador animal) {
        super(partida);
        this.animal = animal;
        this.ataquesPendientes = new ArrayList<>();
    }

    public Jugador getAnimal() {
        return this.animal;
    }

    public void setObjetivo(Animal objetivo) {
        animal.setObjetivo(objetivo);
    }

    // Ordena a los animales indicados (int[]{idPack, idAnimal}) ir hacia un nodo.
    // Cada uno recibe su propia ruta desde el nodo en el que está ahora.
    public void moverAnimales(List<int[]> animales, int[] idNodo) {
        if (animales == null || animales.isEmpty() || idNodo == null || idNodo.length < 2) {
            getPartida().reportarError(animal.getNickname(), Constantes.ERROR_ANIMALES);
            return;
        }
        Mapa mapa = Mapa.getInstance();
        Nodo destino = mapa.getNodo(idNodo);
        if (destino == null) {
            getPartida().reportarError(
                    animal.getNickname(),
                    Constantes.ERROR_PARTIDA_MOVIMIENTO_INVALIDO);
            return;
        }
        for (int[] id : animales) {
            if (id == null || id.length != 2 || id[0] != animal.getId()
                    || id[1] < 0 || id[1] >= animal.getLista().size()) {
                getPartida().reportarError(animal.getNickname(), Constantes.ERROR_ANIMALES);
                return;
            }
            Animal seleccionado = animal.getLista().get(id[1]);
            if (!seleccionado.estaVivo()) {
                continue;
            }
            Posicion posicion = seleccionado.getPosicion();
            Nodo origen = posicion == null ? null : posicion.getNodo();
            if (origen == null) {
                getPartida().reportarError(
                        animal.getNickname(),
                        Constantes.ERROR_PARTIDA_MOVIMIENTO_INVALIDO);
                continue;
            }
            Ruta ruta = mapa.getRuta(origen, destino);
            if (ruta == null) {
                getPartida().reportarError(
                        animal.getNickname(),
                        Constantes.ERROR_PARTIDA_MOVIMIENTO_INVALIDO);
                continue;
            }
            seleccionado.setRuta(ruta);
        }
    }

    @Override
    public void detener() {
        super.detener();
    }

    @Override
    public synchronized void decidir(long ahora) {
        while (!ataquesPendientes.isEmpty()) {
            int[][] ataque = ataquesPendientes.remove(0);
            if (ataque[0][0] != animal.getId()) {
                getPartida().reportarError(
                        animal.getNickname(),
                        Constantes.ERROR_PARTIDA_ANIMAL_AJENO);
                continue;
            }
            List<Animal> animalesPropios = animal.getLista();
            if (ataque[0][1] < 0 || ataque[0][1] >= animalesPropios.size()) {
                getPartida().reportarError(animal.getNickname(), Constantes.ERROR_ANIMALES);
                continue;
            }
            Animal atacante = animalesPropios.get(ataque[0][1]);
            Animal objetivo = getPartida().obtenerAnimal(ataque[1]);
            if (!atacante.estaVivo() || objetivo == null || !objetivo.estaVivo()) {
                getPartida().reportarError(
                        animal.getNickname(),
                        Constantes.ERROR_PARTIDA_ATAQUE_INVALIDO);
                continue;
            }
            setObjetivo(objetivo);
            getPartida().ejecutarAtaque(animal, atacante, objetivo, ahora);
        }
        verificarBandera();
    }

    public synchronized void agregarAtaque(int[] idJugador, int[] idEnemigo) {
        if (idJugador == null || idJugador.length != 2
                || idEnemigo == null || idEnemigo.length != 2) {
            getPartida().reportarError(
                    animal.getNickname(),
                    Constantes.ERROR_PARTIDA_ATAQUE_INVALIDO);
            return;
        }
        ataquesPendientes.add(new int[][]{idJugador.clone(), idEnemigo.clone()});
    }

    private boolean verificarBandera() {
        EstadoPartida estadoAnterior = getPartida().getEstado();
        getPartida().procesarInteracciones(animal);
        return estadoAnterior == EstadoPartida.JUGANDO
                && getPartida().getEstado() == EstadoPartida.TERMINADA;
    }

    @Override
    protected Pack obtenerPack() {
        return animal;
    }

}