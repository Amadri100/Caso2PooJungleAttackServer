package src.logica;

import src.mapa.Nodo;
import src.mapa.Ruta;
import src.utilidades.Constantes;

public class Bot extends Pack {

    private EstrategiaBot estrategia;
    private Ruta rutaActual;
    private int movimientosEnRuta;

    public Bot(int id, TipoEspecie tipoEspecie) {
        super(id, tipoEspecie, Constantes.BOT_DAMAGE);
    }

    public void crearAnimales(Nodo nodoInicial) {
        crearAnimales(nodoInicial, Constantes.BOT_PACK_SIZE, Constantes.BOT_INITIAL_ENERGY);
    }

    public EstrategiaBot getEstrategia() {
        return estrategia;
    }

    public void cambiarDeEstrategia() {
    }

    @Override
    public int atacar(int[] idEnemigo) {
        return -1; //TODO
    }

}
