package src.logica;

public class ThreadBot implements Runnable {

    private Bot bot;

    public ThreadBot(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {}

    public void detener() {}

    private void moverse() {}

    private void atacar() {}

    public void decidir(long ahora) {}

    private void buscarSiguienteRuta() {}

    private boolean intentarAtacar() {
        return false;
    }
    public Bot getBot() {
        return this.bot;
    }

}