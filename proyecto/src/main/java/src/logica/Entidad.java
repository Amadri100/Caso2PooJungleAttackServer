package src.logica;

import src.mapa.Posicion;
import src.mapa.Zona;

public abstract class Entidad {

    private final int id;
    private final TipoEntidad tipo;
    private Posicion posicion;
    private Zona zonaRegistrada;

    protected Entidad(int id, TipoEntidad tipo, Posicion posicion) {
        this.id = id;
        this.tipo = tipo;
        this.posicion = posicion;
        actualizarRegistroZona(posicion);
    }

    public int getId() { return id; }

    public TipoEntidad getTipo() { return tipo; }

    public Posicion getPosicion() { return posicion; }

    public synchronized void setPosicion(Posicion posicion) {
        actualizarRegistroZona(posicion);
        this.posicion = posicion;
    }

    private void actualizarRegistroZona(Posicion posicion) {
        Zona nuevaZona = posicion == null || posicion.getNodo() == null
                ? null
                : posicion.getNodo().getZona();
        if (zonaRegistrada != nuevaZona) {
            if (zonaRegistrada != null) {
                zonaRegistrada.quitarEntidad(this);
            }
            if (nuevaZona != null) {
                nuevaZona.agregarEntidad(this);
            }
            zonaRegistrada = nuevaZona;
        }
    }

}