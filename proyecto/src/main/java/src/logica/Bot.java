package src.logica;

import src.mapa.Ruta;

public class Bot extends Pack {

    private EstrategiaBot estrategia;
    private Ruta rutaActual;
    private int movimientosEnRuta;

    public Bot(int id, TipoEspecie tipoEspecie, int damage) {
        super(id, tipoEspecie, damage);
    }

    public EstrategiaBot getEstrategia() {
        return null;
    }

    public void cambiarDeEstrategia() {
    }

    @Override
    public int atacar(int[] idEnemigo) {
        return -1; //TODO
    }

}
