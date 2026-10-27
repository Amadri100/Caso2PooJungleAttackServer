package src.enviables;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import src.logica.Animal;
import src.logica.Jugador;
import src.logica.TipoEspecie;
import src.mapa.Zona;

public class JugadorEnviar {

    private final int id;
    private final String nickname;
    private final TipoEspecie especie;
    private final List<AnimalEnviar> animales;
    private final int cafesConsumidos;
    private final int botsEliminados;
    private final boolean conectado;

    public JugadorEnviar(int id, String nickname, TipoEspecie especie,
                         List<AnimalEnviar> animales, int cafesConsumidos,
                         int botsEliminados, boolean conectado) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname, "El nickname es obligatorio.");
        this.especie = Objects.requireNonNull(especie, "La especie es obligatoria.");
        this.animales = List.copyOf(animales);
        this.cafesConsumidos = cafesConsumidos;
        this.botsEliminados = botsEliminados;
        this.conectado = conectado;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }

    public List<AnimalEnviar> getAnimales() {
        return animales;
    }

    public int getCafesConsumidos() {
        return cafesConsumidos;
    }

    public int getBotsEliminados() {
        return botsEliminados;
    }

    public boolean isConectado() {
        return conectado;
    }

    public static JugadorEnviar desde(Jugador jugador, Zona zonaVisible) {
        Objects.requireNonNull(jugador, "El jugador es obligatorio.");
        List<AnimalEnviar> animalesVisibles = jugador.getLista().stream()
                .filter(animal -> zonaVisible != null && animal.getZonaActual() == zonaVisible)
                .map(AnimalEnviar::desde)
                .collect(Collectors.toList());

        return new JugadorEnviar(
                jugador.getId(),
                jugador.getNickname(),
                jugador.getEspecie(),
                animalesVisibles,
                jugador.getCafesConsumidos(),
                jugador.getBotsEliminados(),
                jugador.isConectado());
    }
}
