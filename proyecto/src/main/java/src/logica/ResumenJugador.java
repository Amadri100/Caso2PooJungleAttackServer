package src.logica;

import src.mapa.Zona;

public class ResumenJugador {
    private String nickname;
    private TipoEspecie especie;
    private int energia;
    private int damage;
    private int conteoDeBajas;
    private Zona zonaElegida;
    private int cafesConsumidos;
    private int botsEliminados;
    private int totalVivos;

    public ResumenJugador(Jugador jugador) {
        this.nickname = jugador.getNickname();
        this.energia = jugador.getEnergiaTotal();
        this.especie = jugador.getEspecie();
        this.damage = jugador.getDamage();
        this.conteoDeBajas = jugador.getConteoDeBajas();
        this.zonaElegida = jugador.getZonaElegida();
        this.cafesConsumidos = jugador.getCafesConsumidos();
        this.botsEliminados = jugador.getBotsEliminados();
        this.totalVivos = jugador.getVivos();

    }

    public int getEnergia() {
        return this.energia;
    }

    public String getNickname() {
        return this.nickname;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }

    public void setEspecie(TipoEspecie especie) {
        this.especie = especie;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getConteoDeBajas() {
        return conteoDeBajas;
    }

    public void setConteoDeBajas(int conteoDeBajas) {
        this.conteoDeBajas = conteoDeBajas;
    }

    public Zona getZonaElegida() {
        return zonaElegida;
    }

    public void setZonaElegida(Zona zonaElegida) {
        this.zonaElegida = zonaElegida;
    }

    public int getCafesConsumidos() {
        return cafesConsumidos;
    }

    public void setCafesConsumidos(int cafesConsumidos) {
        this.cafesConsumidos = cafesConsumidos;
    }

    public int getBotsEliminados() {
        return botsEliminados;
    }

    public void setBotsEliminados(int botsEliminados) {
        this.botsEliminados = botsEliminados;
    }

    public int getTotalVivos() {
        return totalVivos;
    }

    public void setTotalVivos(int totalVivos) {
        this.totalVivos = totalVivos;
    }
}