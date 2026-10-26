package src.mensajes;

import java.util.List;

// Cliente -> Servidor
// Cada animal se identifica como int[]{idPack, idAnimal}.
// El servidor calcula la ruta con Mapa.getRuta().
public class MensajeMoveAnimals extends MensajeJugador {
    private List<int[]> animales;
    private int idNodoDestino;

    // Constructor para Jackson
    protected MensajeMoveAnimals() {
        super(TipoMensaje.MOVE_ANIMALS, null);
    }

    public MensajeMoveAnimals(String nombre, List<int[]> animales, int idNodoDestino) {
        super(TipoMensaje.MOVE_ANIMALS, nombre);
        this.animales = animales;
        this.idNodoDestino = idNodoDestino;
    }

    public List<int[]> getAnimales() {
        return animales;
    }

    public int getIdNodoDestino() {
        return idNodoDestino;
    }
}
