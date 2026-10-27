package src.logica;

import java.util.List;

public class ResumenPartida {

    private final String codigo;
    private final EstadoPartida estado;
    private final List<ResumenJugador> jugadores;
    private final int botsVivos;
    private final int botsTotales;
    private final int cafesRestantes;
    private final long duracionMs;
    private final String ganador;

    public static ResumenPartida crearResumenSafe(Partida partida) {
        ResumenPartida resumen = null;
        if (partida != null) {
            resumen = new ResumenPartida(partida);
        }
        return  resumen;
    } 

    public ResumenPartida(Partida partida) {
        this.codigo = partida.getCodigo();
        this.estado = partida.getEstado();
        int[] vivoYTotales = partida.getBotsVivosTotales();
        this.jugadores = partida.resumirJugadores();
        this.botsVivos = vivoYTotales[0];
        this.botsTotales = vivoYTotales[1];
        this.cafesRestantes = partida.getCafesRestantes();
        this.duracionMs = partida.tiempoTranscurrido();
        this.ganador = partida.getGanador();
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public List<ResumenJugador> getJugadores() {
        return jugadores;
    }

    public int getBotsVivos() {
        return botsVivos;
    }

    public int getBotsTotales() {
        return botsTotales;
    }

    public int getCafesRestantes() {
        return cafesRestantes;
    }

    public long getDuracionMs() {
        return duracionMs;
    }

    // null mientras la partida no haya terminado
    public String getGanador() {
        return ganador;
    }
}