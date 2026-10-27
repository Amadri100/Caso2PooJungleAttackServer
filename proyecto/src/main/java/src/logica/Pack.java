package src.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import src.mapa.Posicion;
import src.mapa.Zona;

public abstract class Pack {

    private final int id;
    private final List<Animal> animales = new ArrayList<>();
    private Especie especie;
    private final int damage;
    private int conteoDeBajas = 0;
    private int conteoMuertes = 0;

    protected Pack(int id, TipoEspecie tipoEspecie, int damage) {
        this.id = id;
        this.especie = new Especie(Objects.requireNonNull(tipoEspecie, "La especie es obligatoria."));
        if (damage < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public Especie getEspecie() {
        return especie;
    }

    protected void setEspecie(TipoEspecie tipoEspecie) {
        this.especie = new Especie(Objects.requireNonNull(tipoEspecie, "La especie es obligatoria."));
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

    public int getEnergiaTotal() {
        return animales.stream()
                .filter(Animal::estaVivo)
                .mapToInt(Animal::getEnergia)
                .sum();
    }

    public ArrayList<Zona> getZonasAnimales() {
        ArrayList<Zona> zonas = new ArrayList<>();
        for (Animal animal : animales) {
            Zona zona = animal.getZonaActual();
            if (animal.estaVivo() && zona != null && !zonas.contains(zona)) {
                zonas.add(zona);
            }
        }
        return zonas;
    }

    public synchronized void agregarBotEliminado() {
        conteoDeBajas++;
    }

    public synchronized int getConteoDeBajas() {
        return conteoDeBajas;
    }

    protected synchronized void agregarAnimal(Animal animal) {
        Objects.requireNonNull(animal, "El animal no puede ser null.");
        if (animales.contains(animal)) {
            return;
        }
        animal.asignarPack(this);
        animales.add(animal);
    }

    public abstract int atacar(int[] idEnemigo);

    public List<Posicion> getPosiciones() {
        ArrayList<Posicion> posiciones = new ArrayList<>();
        for (Animal animal : animales) {
            posiciones.add(animal.getPosicion());
        }
        return posiciones;
    }
}