package src.enviables;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import src.mapa.Nodo;
import src.mapa.Zona;


public class ZonaEnviar {

    private final int id;
    private final List<NodoEnviar> nodos;

    public ZonaEnviar(int id, List<NodoEnviar> nodos) {
        this.id = id;
        this.nodos = List.copyOf(Objects.requireNonNull(nodos, "La lista de nodos es obligatoria."));
    }

    public static ZonaEnviar desde(Zona zona) {
        Objects.requireNonNull(zona, "La zona es obligatoria.");
        List<NodoEnviar> nodos = zona.getNodos().stream()
                .map(NodoEnviar::desde)
                .collect(Collectors.toList());
        return new ZonaEnviar(zona.getId(), nodos);
    }

    public int getId() {
        return id;
    }

    public List<NodoEnviar> getNodos() {
        return nodos;
    }
}
