package src.logica;

import java.util.concurrent.atomic.AtomicInteger;
import src.mapa.Posicion;
import src.mapa.Ruta;
import src.mapa.Zona;
import src.utilidades.Constantes;
import src.utilidades.Punto;

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

    public void recuperarEnergia(int cantidad, int maximo) {
        if (cantidad < 0 || maximo < 0) {
            throw new IllegalArgumentException("La energía y su máximo no pueden ser negativos.");
        }
        energia.updateAndGet(actual -> actual == 0 ? 0 : Math.min(maximo, actual + cantidad));
    }

    public synchronized boolean moverse(double tiempoTranscurridoSegundos) {
        if (!estaVivo() || !Double.isFinite(tiempoTranscurridoSegundos)
                || tiempoTranscurridoSegundos <= 0) {
            return false;
        }
        Ruta rutaActual = ruta;
        if (rutaActual == null) {
            return false;
        }

        double distanciaDisponible = Constantes.ANIMAL_SPEED * tiempoTranscurridoSegundos;
        boolean seMovio = false;
        while (distanciaDisponible > 0) {
            var nodos = rutaActual.getNodos();
            int indiceDestino = rutaActual.getNodoActual() + 1;
            if (indiceDestino >= nodos.size()) {
                ruta = null;
                break;
            }

            var nodoDestino = nodos.get(indiceDestino);
            Punto puntoDestino = nodoDestino.getPosicion();
            Posicion posicionActual = getPosicion();
            if (puntoDestino == null || posicionActual == null || posicionActual.getPunto() == null) {
                ruta = null;
                break;
            }

            Punto puntoActual = posicionActual.getPunto();
            double desplazamientoX = puntoDestino.getX() - puntoActual.getX();
            double desplazamientoY = puntoDestino.getY() - puntoActual.getY();
            double distancia = Math.hypot(desplazamientoX, desplazamientoY);
            if (distancia <= distanciaDisponible || distancia == 0) {
                setPosicion(new Posicion(
                        nodoDestino,
                        new Punto(puntoDestino.getX(), puntoDestino.getY())));
                seMovio |= distancia > 0;
                distanciaDisponible -= distancia;
                rutaActual.avanzar();
                if (rutaActual.haTerminado()) {
                    ruta = null;
                    break;
                }
            } else {
                double proporcion = distanciaDisponible / distancia;
                setPosicion(new Posicion(
                        null,
                        new Punto(
                                puntoActual.getX() + desplazamientoX * proporcion,
                                puntoActual.getY() + desplazamientoY * proporcion)));
                seMovio = true;
                distanciaDisponible = 0;
            }
        }
        return seMovio;
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

    public synchronized void setRuta(Ruta ruta) {
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