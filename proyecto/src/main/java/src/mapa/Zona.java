package src.mapa;
import java.util.ArrayList;
public class Zona { 
    private int id;
    private ArrayList<Nodo> nodos;
    private Mapa mapa;
    public Zona(Mapa map) {
        this.mapa = map;
    }
    public ArrayList<Nodo> getNodos() {
        return this.nodos;
    } 
    public Mapa getMapa() {
        return this.mapa;
    } 
    public int getId() {
        return this.id;
    }
    public void agregarNodo(Nodo nodo) {
        this.nodos.add(nodo);
    }
    public boolean contiene(Nodo nodo) {
        if (nodos.size() < nodo.getId()) {
            if (nodos.get(nodo.getId()).igual(nodo)) {
                return true;
            }
        }
        return false;
    }
}
