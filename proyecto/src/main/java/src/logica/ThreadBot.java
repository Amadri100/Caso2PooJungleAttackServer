package src.logica;

public class ThreadBot extends ThreadAnimal {

    private Bot bot;

    public ThreadBot(Partida partida, Bot bot) {
        super(partida);
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