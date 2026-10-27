package src.enviables;

import java.util.Objects;
import src.logica.Evento;
import src.logica.TipoEvento;

public class EventoEnviar {

    private final TipoEvento tipo;
    private final PosicionEnviar origen;
    private final PosicionEnviar destino;

    public EventoEnviar(TipoEvento tipo, PosicionEnviar origen, PosicionEnviar destino) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo del evento es obligatorio.");
        this.origen = origen;
        this.destino = destino;
    }

    public static EventoEnviar desde(Evento evento) {
        Objects.requireNonNull(evento, "El evento es obligatorio.");
        return new EventoEnviar(
                evento.getTipo(),
                PosicionEnviar.desde(evento.getOrigen()),
                PosicionEnviar.desde(evento.getDestino()));
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public PosicionEnviar getOrigen() {
        return origen;
    }

    public PosicionEnviar getDestino() {
        return destino;
    }
}
