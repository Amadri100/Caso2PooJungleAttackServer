package src.logica;

import java.util.concurrent.atomic.AtomicInteger;
import src.mapa.Posicion;
import src.mapa.Ruta;
import src.mapa.Zona;

public abstract class Animal extends Entidad {

    private final AtomicInteger energia;
    private volatile Pack pack;
    private volatile Ruta ruta;
    private volatile Zona zonaActual;

    protected Animal(int id, Posicion posicion, int energia) {
        super(id, TipoEntidad.ANIMAL, posicion);
        if (energia < 0) {
            throw new IllegalArgumentException("La energía no puede ser negativa.");
        }
        this.energia = new AtomicInteger(energia);
        actualizarZonaActual(posicion);
    }

    public int getEnergia() {
        return energia.get();
    }

    public void recibirDamage(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }
        energia.updateAndGet(actual -> Math.max(0, actual - cantidad));
    }

    public Pack getPack() {
        return pack;
    }

    void asignarPack(Pack pack) {
        if (this.pack != null && this.pack != pack) {
            throw new IllegalStateException("El animal ya pertenece a otro pack.");
        }
        this.pack = pack;
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

    @Override
    public void setPosicion(Posicion posicion) {
        super.setPosicion(posicion);
        actualizarZonaActual(posicion);
    }

    public boolean estaVivo() {
        return energia.get() > 0;
    }

    private void actualizarZonaActual(Posicion posicion) {
        zonaActual = posicion == null || posicion.getNodo() == null
                ? null
                : posicion.getNodo().getZona();
    }

}