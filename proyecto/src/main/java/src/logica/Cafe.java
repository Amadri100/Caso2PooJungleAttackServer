package src.logica;
 
import src.mapa.Posicion;
 
public class Cafe extends Entidad {
 
    private boolean consumido;
 
    public Cafe(int id, Posicion posicion) {
        super(id, TipoEntidad.CAFE, posicion);
        this.consumido = false;
    }
 
    public synchronized boolean consumir() {
        if (this.consumido) {
            return false;
        }
        this.consumido = true;
        return true;
    }
 
    public synchronized boolean estaDisponible() {
        return !this.consumido;
    }
 
}