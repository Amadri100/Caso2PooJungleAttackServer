package src.mapa;

import java.util.ArrayList;
import java.util.List;
import src.logica.Entidad;

public class Zona {
    private int id;
    private final ArrayList<Nodo> nodos;
    private final ArrayList<Entidad> entidades;
    private final Mapa mapa;

    public Zona(Mapa map) {
        this.mapa = map;
        this.nodos = new ArrayList<>();
        this.entidades = new ArrayList<>();
    }

    public Zona(int id, Mapa mapa) {
        this(mapa);
        this.id = id;
    }

    public ArrayList<Nodo> getNodos() {
        return new ArrayList<>(this.nodos);
    }

    public synchronized List<Entidad> getEntidades() {
        return List.copyOf(entidades);
    }

    public Mapa getMapa() {
        return mapa;
    }

    public int getId() {
        return id;
    }

    public void agregarNodo(Nodo nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("El nodo no puede ser null.");
        }
        nodos.add(nodo);
    }

    public synchronized void agregarEntidad(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser null.");
        }
        if (!entidades.contains(entidad)) {
            entidades.add(entidad);
        }
    }

    public synchronized void quitarEntidad(Entidad entidad) {
        entidades.remove(entidad);
    }

    public boolean contiene(Nodo nodo) {
        if (nodo == null || nodo.getId() < 0 || nodo.getId() >= nodos.size()) {
            return false;
        }
        return nodos.get(nodo.getId()).igual(nodo);
    }
}
