package src.logica;

import java.util.concurrent.atomic.AtomicInteger;
import src.mapa.Posicion;
import src.mapa.Ruta;
import src.mapa.Zona;

public abstract class Animal extends Entidad {

    private final AtomicInteger energia;
    private volatile Ruta ruta;
    private volatile Zona zonaActual;

    protected Animal(int id, Posicion posicion, int energia) {
        super(id, TipoEntidad.ANIMAL, posicion);
        this.energia = new AtomicInteger(energia);
    }


    public int getEnergia() {
        return energia.get();
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public Zona getZonaActual() {
        return zonaActual;
    }


    public boolean estaVivo() {
        return energia.get() > 0;
    }

}