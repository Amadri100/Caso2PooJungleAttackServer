package src.logica;

import src.utilidades.Constantes;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public abstract class ThreadAnimal implements Runnable {

    private final Partida partida;
    private volatile boolean activo;

    public ThreadAnimal(Partida partida) {
        this.partida = partida;
        this.activo = true;
    }

    @Override
    public void run() {
        long ultimoCiclo = System.nanoTime();
        while (activo && partida.getEstado() == EstadoPartida.JUGANDO) {
            long ahoraNanos = System.nanoTime();
            double tiempoTranscurridoSegundos = (ahoraNanos - ultimoCiclo) / 1_000_000_000.0;
            ultimoCiclo = ahoraNanos;
            try {
                decidir(System.currentTimeMillis());
                moverAnimales(obtenerPack(), tiempoTranscurridoSegundos);
                partida.procesarInteracciones(obtenerPack());
            } catch (RuntimeException e) {
                Logger.getInstance().crearLog(
                        NivelLog.ERROR,
                        Constantes.ORIGEN_PARTIDA,
                        "Error al actualizar los animales del pack " + obtenerPack().getId(),
                        e);
                detener();
                return;
            }

            try {
                Thread.sleep(Constantes.TIEMPO_SLEEP_MINIMO);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                detener();
                return;
            }
        }
    }

    public void detener() {
        this.activo = false;
    }

    public Partida getPartida() {
        return partida;
    }

    public abstract void decidir(long ahora);

    protected abstract Pack obtenerPack();

    protected void moverAnimales(Pack pack, double tiempoTranscurridoSegundos) {
        if (pack == null || tiempoTranscurridoSegundos <= 0) {
            return;
        }
        for (Animal animal : pack.getLista()) {
            if (animal.estaVivo()) {
                animal.moverse(tiempoTranscurridoSegundos);
            }
        }
    }

    protected boolean intentarAtaque(Pack atacante, Animal objetivo, long ahora) {
        return partida.ejecutarAtaque(atacante, objetivo, ahora) >= 0;
    }

}