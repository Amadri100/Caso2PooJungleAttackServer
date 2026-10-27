package src.enviables;

import java.util.Objects;
import src.mapa.Posicion;
import src.utilidades.Punto;

public class PosicionEnviar {

    private final NodoEnviar nodo;
    private final Punto punto;

    public PosicionEnviar(NodoEnviar nodo, Punto punto) {
        this.nodo = nodo;
        this.punto = Objects.requireNonNull(punto, "El punto de la posición es obligatorio.");
    }

    public static PosicionEnviar desde(Posicion posicion) {
        if (posicion == null) {
            return null;
        }
        Punto punto = Objects.requireNonNull(posicion.getPunto(), "La posición debe tener punto.");
        NodoEnviar nodo = posicion.getNodo() == null ? null : NodoEnviar.desde(posicion.getNodo());
        return new PosicionEnviar(nodo, new Punto(punto.getX(), punto.getY()));
    }

    public NodoEnviar getNodo() {
        return nodo;
    }

    public Punto getPunto() {
        return punto;
    }
}
