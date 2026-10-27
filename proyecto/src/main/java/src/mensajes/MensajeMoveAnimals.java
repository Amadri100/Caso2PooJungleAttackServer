package src.mensajes;

import java.util.List;

// Cliente -> Servidor
// Cada animal se identifica como int[]{idPack, idAnimal}.
// El servidor calcula la ruta con Mapa.getRuta().
public class MensajeMoveAnimals extends MensajeJugador {
    private List<int[]> animales;
    private int[] idNodo; // {idZona, idNodo}

    // Constructor para Jackson
    protected MensajeMoveAnimals() {
        super(TipoMensaje.MOVE_ANIMALS, null);
    }

    public MensajeMoveAnimals(String nombre, List<int[]> animales, int[] idNodo) {
        super(TipoMensaje.MOVE_ANIMALS, nombre);
        this.animales = animales;
        this.idNodo = idNodo;
    }

    public List<int[]> getAnimales() {
        return animales;
    }

    public int[] getidNodo() {
        return idNodo;
    }
}
