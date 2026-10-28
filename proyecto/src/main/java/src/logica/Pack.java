package src.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import src.mapa.Nodo;
import src.mapa.Posicion;
import src.mapa.Zona;
import src.utilidades.Constantes;
import src.utilidades.Punto;

public abstract class Pack {

    private final int id;
    private final List<Animal> animales = new ArrayList<>();
    private TipoEspecie especie;
    private int damage;
    private int conteoDeBajas = 0;
    private int conteoMuertes = 0;
    private long ultimoAtaque = Long.MIN_VALUE;
    private Animal objetivo;

    protected Pack(int id, TipoEspecie tipoEspecie, int damage) {
        if (id < 0 || tipoEspecie == null || damage < 0) {
            throw new IllegalArgumentException("El id, especie y daño del pack deben ser válidos.");
        }
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

    public synchronized void aumentarDamage(int incremento) {
        if (incremento < 0) {
            throw new IllegalArgumentException("El incremento de daño no puede ser negativo.");
        }
        damage += incremento;
    }

    public synchronized void setObjetivo(Animal objetivo) {
        this.objetivo = objetivo;
    }

    public synchronized Animal getObjetivo() {
        return objetivo;
    }

    public synchronized int atacar(Animal objetivo, long ahora) {
        if (objetivo == null || !objetivo.estaVivo() || objetivo.getPack() == this
                || getVivos() == 0) {
            return -1;
        }
        if (ultimoAtaque != Long.MIN_VALUE
                && (ahora < ultimoAtaque || ahora - ultimoAtaque < Constantes.ATTACK_COOLDOWN_MS)) {
            return -1;
        }

        ultimoAtaque = ahora;
        this.objetivo = objetivo;
        boolean estabaVivo = objetivo.estaVivo();
        objetivo.recibirDamage(damage);
        if (estabaVivo && !objetivo.estaVivo()) {
            conteoDeBajas++;
            Pack packObjetivo = objetivo.getPack();
            if (packObjetivo != null) {
                packObjetivo.registrarMuerte();
            }
            if (this instanceof Jugador jugador
                    && packObjetivo instanceof Bot bot
                    && bot.getVivos() == 0) {
                jugador.sumarBotEliminado();
                if (jugador.getBotsEliminados() % 2 == 0) {
                    for (Animal animal : jugador.getLista()) {
                        if (animal.estaVivo()) {
                            animal.recuperarEnergia(
                                    Constantes.ENERGY_BONUS_PER_TWO_BOTS,
                                    Constantes.MAX_PLAYER_ENERGY);
                        }
                    }
                }
            }
        }
        return damage;
    }

    synchronized void registrarMuerte() {
        conteoMuertes++;
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

    public synchronized int getConteoMuertes() {
        return conteoMuertes;
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

    protected synchronized int atacarObjetivo(int[] idEnemigo) {
        if (idEnemigo == null || idEnemigo.length != 2 || objetivo == null
                || objetivo.getPack() == null
                || objetivo.getPack().getId() != idEnemigo[0]
                || objetivo.getId() != idEnemigo[1]) {
            return -1;
        }
        return atacar(objetivo, System.currentTimeMillis());
    }

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
        if (especie == null || especie == TipoEspecie.NINGUNA) {
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
        if (especie == null || especie == TipoEspecie.NINGUNA) {
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