package src.enviables;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import src.logica.Datos;
import src.logica.Entidad;
import src.logica.Evento;
import src.logica.Jugador;
import src.mapa.Zona;


public class DatosEnviar {

    private final JugadorEnviar jugadorA;
    private final JugadorEnviar jugadorB;
    private final List<EntidadEnviar> entidadesDisponibles;
    private final ZonaEnviar zonaActual;
    private final List<EventoEnviar> eventos;

    public DatosEnviar(JugadorEnviar jugadorA, JugadorEnviar jugadorB,
                       List<EntidadEnviar> entidadesDisponibles, ZonaEnviar zonaActual,
                       List<EventoEnviar> eventos) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.entidadesDisponibles = List.copyOf(entidadesDisponibles);
        this.zonaActual = zonaActual;
        this.eventos = List.copyOf(eventos);
    }

    public JugadorEnviar getJugadorA() {
        return jugadorA;
    }

    public JugadorEnviar getJugadorB() {
        return jugadorB;
    }

    public List<EntidadEnviar> getEntidadesDisponibles() {
        return entidadesDisponibles;
    }

    public ZonaEnviar getZonaActual() {
        return zonaActual;
    }

    public List<EventoEnviar> getEventos() {
        return eventos;
    }

    public static DatosEnviar desde(Datos datos) {
        Objects.requireNonNull(datos, "Los datos son obligatorios.");
        Zona zona = datos.getZonaActual();
        Jugador jugadorA = datos.getJugadorA();
        Jugador jugadorB = datos.getJugadorB();

        return new DatosEnviar(
                jugadorA == null ? null : conversionValida(
                        JugadorEnviar.desde(jugadorA, zona), "JugadorEnviar.desde"),
                jugadorB == null ? null : conversionValida(
                        JugadorEnviar.desde(jugadorB, zona), "JugadorEnviar.desde"),
                datos.getEntidadesDisponibles().stream()
                        .map(EntidadEnviar::desde)
                        .map(entidad -> conversionValida(entidad, "EntidadEnviar.desde"))
                        .collect(Collectors.toList()),
                zona == null ? null : conversionValida(
                        ZonaEnviar.desde(zona), "ZonaEnviar.desde"),
                datos.getEventos().stream()
                        .map(EventoEnviar::desde)
                        .map(evento -> conversionValida(evento, "EventoEnviar.desde"))
                        .collect(Collectors.toList()));
    }

    private static <T> T conversionValida(T resultado, String conversion) {
        if (resultado == null) {
            throw new IllegalStateException(conversion + " todavía no está implementado.");
        }
        return resultado;
    }
}
