package src.enviables;

import java.util.Objects;
import src.logica.Animal;
import src.logica.Pack;
import src.logica.TipoEntidad;
import src.logica.TipoEspecie;

public class AnimalEnviar extends EntidadEnviar {

    private final int idPack;
    private final TipoEspecie especie;
    private final int energia;

    public AnimalEnviar(int id, PosicionEnviar posicion, int idPack,
                        TipoEspecie especie, int energia) {
        super(id, TipoEntidad.ANIMAL, posicion);
        this.idPack = idPack;
        this.especie = Objects.requireNonNull(especie, "La especie es obligatoria.");
        this.energia = energia;
    }

    public static AnimalEnviar desde(Animal animal) {
        Objects.requireNonNull(animal, "El animal es obligatorio.");
        Pack pack = animal.getPack();
        if (pack == null) {
            throw new IllegalStateException("El animal debe pertenecer a un pack para enviarse.");
        }
        return new AnimalEnviar(
                animal.getId(),
                PosicionEnviar.desde(animal.getPosicion()),
                pack.getId(),
                pack.getEspecie().getTipo(),
                animal.getEnergia());
    }

    public int getIdPack() {
        return idPack;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }

    public int getEnergia() {
        return energia;
    }
}
