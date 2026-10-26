package src.enviables;
import src.logica.*;

public class AnimalEnviar extends EntidadEnviar {

    private int idPack;
    private TipoEspecie especie;
    private int energia;

    public AnimalEnviar(int id, PosicionEnviar posicion, int idPack,
                        TipoEspecie especie, int energia) {
        super(id, TipoEntidad.ANIMAL, posicion);
    }

    public static AnimalEnviar desde(Animal animal) {
        return null;
    }

    public int getIdPack() {
        return 0;
    }

    public TipoEspecie getEspecie() {
        return null;
    }

    public int getEnergia() {
        return 0;
    }
}
