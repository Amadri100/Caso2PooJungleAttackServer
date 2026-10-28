package src.logica;

import java.util.List;
import src.mapa.Nodo;
import src.mapa.Posicion;
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

    public void crearAnimales(List<Posicion> posiciones) {
        crearAnimales(posiciones, Constantes.BOT_INITIAL_ENERGY);
    }

    public void crearAnimales(List<Posicion> posiciones, int energiaInicial) {
        super.crearAnimales(posiciones, energiaInicial);
    }

    public EstrategiaBot getEstrategia() {
        return estrategia;
    }

    public void cambiarDeEstrategia() {
        estrategia = new SelectorEstrategia().elegir();
        rutaActual = null;
        movimientosEnRuta = 0;
    }

    public void setRutaActual(Ruta rutaActual) {
        this.rutaActual = rutaActual;
        this.movimientosEnRuta = 0;
    }

    public Ruta getRutaActual() {
        return rutaActual;
    }

    public int getMovimientosEnRuta() {
        return movimientosEnRuta;
    }

    public void registrarMovimiento() {
        movimientosEnRuta++;
    }

    @Override
    public int atacar(int[] idEnemigo) {
        return atacarObjetivo(idEnemigo);
    }

}
