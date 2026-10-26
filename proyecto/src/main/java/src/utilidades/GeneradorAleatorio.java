package src.utilidades;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
 

public class GeneradorAleatorio {

    private static final GeneradorAleatorio instancia = new GeneradorAleatorio();

    private long seed;
    private boolean seedFija;
    private volatile Random random;
    private ConcurrentHashMap<String, Random> streams;

    private GeneradorAleatorio() {
        this.seed = Constantes.SEED_POR_DEFECTO;
        this.seedFija = true;
        this.random = new Random(seed);
        this.streams = new ConcurrentHashMap<>();
    }

    public static GeneradorAleatorio getInstance() {
        return instancia;
    }

    // Genera una seed nueva al azar y reinicia el generador con ella
    public synchronized void seedAleatoria() {
        this.seed = System.nanoTime() ^ new Random().nextLong();
        this.random = new Random(seed);
        this.streams.clear();
    }

    // Fija la seed indicada y reinicia el generador con ella
    public synchronized void setSeed(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.streams.clear();
    }

    public long getSeed() {
        return seed;
    }

    // Si es true, reiniciar() repite siempre la misma secuencia; si es false, reiniciar() usa una seed nueva
    public void setSeedFija(boolean fija) {
        this.seedFija = fija;
    }

    public boolean isSeedFija() {
        return seedFija;
    }

    // Con seed fija vuelve a empezar la misma secuencia; sin seed fija elige una seed nueva
    public synchronized void reiniciar() {
        if (seedFija) {
            setSeed(seed);
        } else {
            seedAleatoria();
        }
    }

    // Entero en [0, limite)
    public int nextInt(int limite) {
        return random.nextInt(limite);
    }

    // Entero en [min, max): incluye min y excluye max
    public int nextInt(int min, int max) {
        if (min >= max) {
            int temp = max;
            max = min;
            min = temp;
        }
        return min + random.nextInt(max - min);
    }

    // Double en [0.0, 1.0)
    public double nextDouble() {
        return random.nextDouble();
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    // Elemento al azar de la lista
    public <T> T elegir(List<T> lista) {
        if (lista == null || lista.isEmpty()) {
            return null;
        }
        return lista.get(random.nextInt(lista.size()));
    }

    // Mezcla la lista en su lugar
    public <T> void mezclar(List<T> lista) {
        Collections.shuffle(lista, random);
    }

    // Código alfanumérico en mayúsculas para identificar una partida
    public String generarCodigo() {
        String caracteres = Constantes.CARACTERES_CODIGO;
        StringBuilder codigo = new StringBuilder(Constantes.LONGITUD_CODIGO);
        for (int i = 0; i < Constantes.LONGITUD_CODIGO; i++) {
            codigo.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return codigo.toString();
    }

}