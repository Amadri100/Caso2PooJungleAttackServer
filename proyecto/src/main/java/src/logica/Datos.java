package src.logica;

import java.util.List;
import src.enviables.DatosEnviar;
import src.mapa.Zona;

public class Datos {

    private final Jugador jugadorA;
    private final Jugador jugadorB;         
    private final List<Entidad> entidadesDisponibles;
    private final Zona zonaActual;
    private final List<Evento> eventos;

    public Datos(Jugador jugadorA, Jugador jugadorB,
                 List<Entidad> entidadesDisponibles,
                 Zona zonaActual, List<Evento> eventos) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.entidadesDisponibles = List.copyOf(entidadesDisponibles);
        this.zonaActual = zonaActual;
        this.eventos = List.copyOf(eventos);
    }

    public Jugador getJugadorA() {
        return jugadorA;
    }

    public Jugador getJugadorB() {
        return jugadorB;
    }

    public List<Entidad> getEntidadesDisponibles() {
        return entidadesDisponibles;
    }

    public Zona getZonaActual() {
        return zonaActual;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public DatosEnviar getEnviable() {
        return DatosEnviar.desde(this);
    }

}