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

    public ResumenPartida(String codigo, EstadoPartida estado, List<ResumenJugador> jugadores,
                          int botsVivos, int botsTotales, int cafesRestantes,
                          long duracionMs, String ganador) {
        this.codigo = codigo;
        this.estado = estado;
        this.jugadores = jugadores;
        this.botsVivos = botsVivos;
        this.botsTotales = botsTotales;
        this.cafesRestantes = cafesRestantes;
        this.duracionMs = duracionMs;
        this.ganador = ganador;
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