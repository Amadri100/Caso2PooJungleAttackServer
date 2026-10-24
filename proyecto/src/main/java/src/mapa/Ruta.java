package src.mapa;
import java.util.ArrayList;
public class Ruta {
    private ArrayList<Nodo> nodos;
    private int nodoActual;
    public Ruta(ArrayList<Nodo> nodos) {
        this.nodos = nodos;
    }
    public ArrayList<Nodo> getNodos() {
        return this.nodos;
    }
    public int getNodoActual() {
        return this.nodoActual;
    }
    public boolean avanzar() {
        if (nodos.size() - 1 > this.nodoActual) {
            this.nodoActual++;
            return true;
        }
        return false;
    }
    public boolean haTerminado() {
        return this.nodoActual >= nodos.size()-1;

    }


}
