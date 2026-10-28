package src.mapa;
import src.utilidades.*;
public class Posicion {
    private Nodo nodo;
    private Punto punto;

    public Posicion (Nodo nodo, Punto punto) {
        this.nodo = nodo;
        this.punto = punto;
    }

    public Nodo getNodo() {
        return this.nodo;
    }
    public void setNodo(Nodo nodo) {
        this.nodo = nodo;
    }
    public Punto getPunto() {
        return this.punto;
    }
    public void setPunto(Punto punto) {
        this.punto = punto;
    }

    

}
