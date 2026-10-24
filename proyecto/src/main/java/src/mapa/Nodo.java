package src.mapa;
import src.utilidades.*;
public class Nodo {
    private int id; //Id en zona
    private Punto posicion;
    private Zona zona;
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
    public Zona getZona() {
        return this.zona;
    }
    public boolean igual(Nodo nodo) {
        boolean reque1 = this.posicion.igual(nodo.getPosicion());
        boolean reque2 = this.id == nodo.getId();
        boolean reque3 = this.zona.getId() == nodo.getZona().getId();
        return reque1 && reque2 && reque3;
    }
}
