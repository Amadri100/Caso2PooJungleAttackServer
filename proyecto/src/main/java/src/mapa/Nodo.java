package src.mapa;
import java.util.Objects;

import src.utilidades.*;
public class Nodo {
    private int id; //Id en zona
    private Punto posicion;
    private Zona zona;
    
    public Nodo (int id, Punto posicion) {
        this.id = id;
        this.posicion = posicion;
        this.zona = null;
    }
    public Nodo (int id, Punto posicion, Zona zona) {
        this.id = id;
        this.posicion = posicion;
        this.zona = zona;
    }
    public int getId() {
        return this.id;
    }
    public Punto getPosicion() {
        return this.posicion;
    }
    public void setZona(Zona zona) {
        this.zona = zona;
    }
    public Zona getZona() {
        return this.zona;
    }
    public boolean igual(Nodo nodo) {
        boolean reque1 = this.posicion.igual(nodo.getPosicion());
        boolean reque2 = this.id == nodo.getId();
        boolean reque3 = this.zona.getId() == nodo.getZona().getId();
        return reque1 && reque2 && reque3;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof Nodo n && this.igual(n);
    }

    @Override
    public int hashCode() {
        //El objeto se distingue en hashmaps por su id y zona no por la direccion de memoria real de este
        return Objects.hash(id, zona.getId());
    }
}
