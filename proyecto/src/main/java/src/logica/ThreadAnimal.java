package src.logica;

public abstract class ThreadAnimal implements Runnable {

    private Partida partida;
    private long ultimoAtaque;
    private boolean activo;

    public ThreadAnimal(Partida partida) {
        this.partida = partida;
        this.ultimoAtaque = 0;
        this.activo = true;
    }

    @Override
    public void run() {
    }

    public void detener() {
        this.activo = false;
    }

    public Partida getPartida() {
        return partida;
    }

    public abstract void decidir(long ahora);

    private void moverse(long ahora) {}

    private void atacar(long ahora) {}

}