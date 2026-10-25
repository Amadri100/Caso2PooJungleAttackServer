package src.logica;

import java.util.ArrayList;
import java.util.List;
import src.mapa.Zona;

public abstract class Pack {

    private final int id;
    private final List<Animal> animales = new ArrayList<>();
    private final Especie especie;
    private final int damage;

    protected Pack(int id, TipoEspecie tipoEspecie, int damage) {
        this.id = id;
        this.especie = new Especie(tipoEspecie);
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public Especie getEspecie() {
        return especie;
    }

    public int getDamage() {
        return damage;
    }

    public List<Animal> getLista() {
        return List.copyOf(animales);
    }

    public int getVivos() {
        return (int) animales.stream().filter(Animal::estaVivo).count();
    }


    protected void agregarAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("El animal no puede ser null.");
        }
        if (animales.contains(animal)) {
            return;
        }
        animal.asignarPack(this);
        animales.add(animal);
    }

    public abstract int atacar(int[] idEnemigo);
}