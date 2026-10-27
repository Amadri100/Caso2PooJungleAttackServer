package src.logica;

import java.util.Objects;
import src.mapa.Nodo;
import src.mapa.Zona;
import src.utilidades.Constantes;

public class Jugador extends Pack {

    private final String nickname;
    private Zona zonaElegida;
    private int cafesConsumidos;
    private int botsEliminados;
    private boolean conectado;

    public Jugador(int id, String nickname) {
        this(id, nickname, Constantes.PLAYER_DAMAGE);
    }

    public Jugador(int id, String nickname, int damage) {
        super(id, TipoEspecie.NINGUNA, damage);
        this.nickname = Objects.requireNonNull(nickname, "El nickname es obligatorio.");
        this.conectado = true;
    }

    public String getNickname() {
        return nickname;
    }

    public void elegirZona(Zona zona) {
        this.zonaElegida = Objects.requireNonNull(zona, "La zona elegida es obligatoria.");
    }

    public Zona getZonaElegida() {
        return zonaElegida;
    }

    public void elegirEspecie(TipoEspecie especie) {
        if (especie == TipoEspecie.NINGUNA) {
            throw new IllegalArgumentException("Debe elegirse una especie concreta.");
        }
        setEspecie(especie);
    }

    public void seleccionarEspecie(TipoEspecie especie) {
        elegirEspecie(especie);
    }

    public void crearAnimales(Nodo nodoInicial) {
        if (getEspecie() == TipoEspecie.NINGUNA) {
            throw new IllegalStateException("Debe seleccionar una especie antes de crear animales.");
        }
        crearAnimales(nodoInicial, Constantes.PLAYER_PACK_SIZE, Constantes.PLAYER_INITIAL_ENERGY);
    }

    public Jugador copiaBasica() { //Copia con los datos basicos
        return new Jugador(this.getId(), this.getNickname()); 
    }

    public synchronized void sumarCafe() {
        cafesConsumidos++;
    }

    public synchronized int getCafesConsumidos() {
        return cafesConsumidos;
    }

    public synchronized void sumarBotEliminado() {
        botsEliminados++;
    }

    public synchronized int getBotsEliminados() {
        return botsEliminados;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    @Override
    public int atacar(int[] idEnemigo) {
        return -1;
    }
}
