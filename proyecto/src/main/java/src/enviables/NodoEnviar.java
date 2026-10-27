package src.enviables;

import java.util.Objects;
import src.mapa.Nodo;
import src.utilidades.Punto;

public class NodoEnviar {

    private final int id;
    private final Punto posicion;
    private final int idZona;

    public NodoEnviar(int id, Punto posicion, int idZona) {
        this.id = id;
        Punto punto = Objects.requireNonNull(posicion, "La posición del nodo es obligatoria.");
        this.posicion = new Punto(punto.getX(), punto.getY());
        this.idZona = idZona;
    }

    public static NodoEnviar desde(Nodo nodo) {
        Objects.requireNonNull(nodo, "El nodo es obligatorio.");
        if (nodo.getZona() == null) {
            throw new IllegalStateException("El nodo debe pertenecer a una zona para enviarse.");
        }
        return new NodoEnviar(nodo.getId(), nodo.getPosicion(), nodo.getZona().getId());
    }

    public int getId() {
        return id;
    }

    public Punto getPosicion() {
        return new Punto(posicion.getX(), posicion.getY());
    }

    public int getIdZona() {
        return idZona;
    }
}
