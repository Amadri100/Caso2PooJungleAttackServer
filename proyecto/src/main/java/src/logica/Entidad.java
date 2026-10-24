package src.logica;

import src.mapa.Posicion;

public abstract class Entidad {

    private final int id;
    private final TipoEntidad tipo;
    private Posicion posicion;

    protected Entidad(int id, TipoEntidad tipo, Posicion posicion) {
        this.id = id;
        this.tipo = tipo;
        this.posicion = posicion;
    }

    public int getId() { return id; }

    public TipoEntidad getTipo() { return tipo; }

    public Posicion getPosicion() { return posicion; }

    public void setPosicion(Posicion posicion) { this.posicion = posicion; }

}