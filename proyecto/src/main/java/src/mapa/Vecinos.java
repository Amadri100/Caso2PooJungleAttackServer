package src.mapa;

public class Vecinos {
    private Nodo nodoA;
    private Nodo nodoB;
    private double peso;

    public Vecinos(Nodo a, Nodo b, double peso) {
        this.nodoA = a;
        this.nodoB = b;
        this.peso = peso;
    }

    public Nodo getNodoA() {
        return this.nodoA;
    }

    public Nodo getNodoB() {
        return this.nodoB;
    }

    public double getPeso() {
        return this.peso;
    }

    public boolean formaParte(Nodo nodo) {
        return this.nodoA.igual(nodo) || this.nodoB.igual(nodo);
    }

    public Nodo getOtro(Nodo nodo) {
        if (this.nodoA.igual(nodo)) {
            return this.nodoB;
        }
        if (this.nodoB.igual(nodo)) {
            return this.nodoA;
        }
        return null;
    }
}