package src.logica;

import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Ruta;
import src.utilidades.Constantes;

public class ThreadBot extends ThreadAnimal {

    private Bot bot;

    public ThreadBot(Partida partida, Bot bot) {
        super(partida);
        this.bot = bot;
    }

    @Override
    public void detener() {
        super.detener();
    }

    @Override
    protected Pack obtenerPack() {
        return bot;
    }

    @Override
    public void decidir(long ahora) {
        if (bot.getVivos() == 0) {
            return;
        }
        if (bot.getEstrategia() == null) {
            bot.cambiarDeEstrategia();
        }
        if (intentarAtacar(ahora)) {
            return;
        }

        boolean todosSinRuta = bot.getLista().stream()
                .filter(Animal::estaVivo)
                .allMatch(animal -> animal.getRuta() == null);
        if (bot.getRutaActual() == null
                || todosSinRuta
                || bot.getMovimientosEnRuta() >= Constantes.MOVIMIENTOS_MINIMOS_CAMBIAR) {
            buscarSiguienteRuta();
        }
    }

    private boolean intentarAtacar(long ahora) {
        for (Animal enemigo : getPartida().obtenerEnemigos(bot)) {
            for (Animal atacante : bot.getLista()) {
                if (!atacante.estaVivo() || !estanEnRango(atacante, enemigo)) {
                    continue;
                }
                bot.setObjetivo(enemigo);
                if (getPartida().ejecutarAtaque(bot, atacante, enemigo, ahora) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean estanEnRango(Animal a, Animal b) {
        if (a.getPosicion() == null || b.getPosicion() == null
                || a.getPosicion().getPunto() == null || b.getPosicion().getPunto() == null) {
            return false;
        }
        double diferenciaX = a.getPosicion().getPunto().getX() - b.getPosicion().getPunto().getX();
        double diferenciaY = a.getPosicion().getPunto().getY() - b.getPosicion().getPunto().getY();
        return Math.hypot(diferenciaX, diferenciaY) <= Constantes.ENCOUNTER_RADIUS;
    }

    private void buscarSiguienteRuta() {
        Ruta ruta = bot.getEstrategia().elegirDestino(bot, getPartida());
        if (ruta == null || ruta.getNodos().isEmpty()) {
            bot.setRutaActual(null);
            return;
        }

        Nodo destino = ruta.getNodos().get(ruta.getNodos().size() - 1);
        bot.setRutaActual(ruta);
        Mapa mapa = Mapa.getInstance();
        for (Animal animal : bot.getLista()) {
            if (!animal.estaVivo() || animal.getPosicion() == null
                    || animal.getPosicion().getNodo() == null) {
                continue;
            }
            Ruta rutaAnimal = mapa.getRuta(animal.getPosicion().getNodo(), destino);
            animal.setRuta(rutaAnimal);
        }
    }

    @Override
    protected void moverAnimales(Pack pack, double tiempoTranscurridoSegundos) {
        for (Animal animal : pack.getLista()) {
            if (!animal.estaVivo()) {
                continue;
            }
            Nodo nodoAnterior = animal.getPosicion() == null
                    ? null
                    : animal.getPosicion().getNodo();
            animal.moverse(tiempoTranscurridoSegundos);
            Nodo nodoActual = animal.getPosicion() == null
                    ? null
                    : animal.getPosicion().getNodo();
            if (nodoActual != null && nodoActual != nodoAnterior) {
                bot.registrarMovimiento();
            }
        }
    }

    public Bot getBot() {
        return this.bot;
    }

}