package src.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import src.mapa.Nodo;
import src.mapa.Posicion;
import src.mapa.Zona;
import src.utilidades.Punto;

public abstract class Pack {

    private final int id;
    private final List<Animal> animales = new ArrayList<>();
    private TipoEspecie especie;
    private final int damage;
    private int conteoDeBajas = 0;
    private int conteoMuertes = 0;

    protected Pack(int id, TipoEspecie tipoEspecie, int damage) {
        this.id = id;
        this.especie = tipoEspecie;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }

    protected void setEspecie(TipoEspecie tipoEspecie) {
        this.especie = tipoEspecie;
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

    protected synchronized void crearAnimales(Nodo nodoInicial, int cantidad, int energiaInicial) {
        Objects.requireNonNull(nodoInicial, "El nodo inicial no puede ser null.");
        if (cantidad < 0 || energiaInicial < 0) {
            throw new IllegalArgumentException("La cantidad y energía inicial no pueden ser negativas.");
        }
        if (!animales.isEmpty()) {
            throw new IllegalStateException("Los animales de este pack ya fueron creados.");
        }

        Punto punto = Objects.requireNonNull(nodoInicial.getPosicion(), "El nodo debe tener posición.");
        ArrayList<Posicion> posiciones = new ArrayList<>(cantidad);
        for (int idAnimal = 0; idAnimal < cantidad; idAnimal++) {
            posiciones.add(new Posicion(
                    nodoInicial,
                    new Punto(punto.getX(), punto.getY())));
        }
        crearAnimales(posiciones, energiaInicial);
    }

    protected synchronized void crearAnimales(List<Posicion> posiciones, int energiaInicial) {
        Objects.requireNonNull(posiciones, "La lista de posiciones es obligatoria.");
        if (especie == TipoEspecie.NINGUNA) {
            throw new IllegalStateException("Debe seleccionarse una especie antes de crear animales.");
        }
        if (energiaInicial < 0) {
            throw new IllegalArgumentException("La energía inicial no puede ser negativa.");
        }
        if (!animales.isEmpty()) {
            throw new IllegalStateException("Los animales de este pack ya fueron creados.");
        }
        for (Posicion posicion : posiciones) {
            Objects.requireNonNull(posicion, "Las posiciones no pueden contener null.");
            Objects.requireNonNull(posicion.getPunto(), "Cada posición debe contener un punto.");
        }

        for (int idAnimal = 0; idAnimal < posiciones.size(); idAnimal++) {
            agregarAnimal(new AnimalPack(idAnimal, posiciones.get(idAnimal), energiaInicial));
        }
    }

    protected synchronized void agregarAnimal(int idAnimal, Posicion posicion, int energiaInicial) {
        Objects.requireNonNull(posicion, "La posición del animal es obligatoria.");
        Objects.requireNonNull(posicion.getPunto(), "La posición del animal debe tener un punto.");
        if (idAnimal < 0 || energiaInicial < 0) {
            throw new IllegalArgumentException("El id y la energía del animal no pueden ser negativos.");
        }
        if (especie == TipoEspecie.NINGUNA) {
            throw new IllegalStateException("Debe seleccionarse una especie antes de agregar animales.");
        }
        agregarAnimal(new AnimalPack(idAnimal, posicion, energiaInicial));
    }

    private static final class AnimalPack extends Animal {

        private AnimalPack(int id, Posicion posicion, int energia) {
            super(id, posicion, energia);
        }
    }
}