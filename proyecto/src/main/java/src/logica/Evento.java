package src.logica;

import src.mapa.Posicion;

public class Evento {

    private final TipoEvento tipo;
    private final Posicion origen;
    private final Posicion destino;

    public Evento(TipoEvento tipo, Posicion origen, Posicion destino) {
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public Posicion getOrigen() {
        return origen;
    }

    public Posicion getDestino() {
        return destino;
    }
}
