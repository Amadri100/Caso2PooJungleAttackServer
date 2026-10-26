package src.enviables;

import java.util.List;
import src.logica.*;
import src.mapa.*;

public class JugadorEnviar {

    private int id;
    private String nickname;
    private TipoEspecie especie;
    private List<AnimalEnviar> animales;
    private int cafesConsumidos;
    private int botsEliminados;
    private boolean conectado;

    public JugadorEnviar(int id, String nickname, TipoEspecie especie,
                         List<AnimalEnviar> animales, int cafesConsumidos,
                         int botsEliminados, boolean conectado) {
    }

    public int getId() {
        return 0;
    }

    public String getNickname() {
        return null;
    }

    public TipoEspecie getEspecie() {
        return null;
    }

    public List<AnimalEnviar> getAnimales() {
        return null;
    }

    public int getCafesConsumidos() {
        return 0;
    }

    public int getBotsEliminados() {
        return 0;
    }

    public boolean isConectado() {
        return false;
    }

    public static JugadorEnviar desde(Jugador jugador, Zona zonaVisible) {
        return null;
    }
}
