package src.logica;

import src.mapa.Zona;

public class ResumenJugador {

    private Especie especie;
    private int damage;
    private int conteoDeBajas;
    private Zona zonaElegida;
    private int cafesConsumidos;
    private int botsEliminados;
    private int totalVivos;

    public ResumenJugador() {}

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
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