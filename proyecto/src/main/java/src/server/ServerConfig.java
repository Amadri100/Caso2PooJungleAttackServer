package src.server;
import src.utilidades.*;

import java.util.concurrent.ConcurrentSkipListSet;

public class ServerConfig {

    private static final ServerConfig instancia = new ServerConfig();

    private final String ip;
    private final int puerto;
    private final ConcurrentSkipListSet<String> nombresOcupados;

    private ServerConfig() {
        this.ip = Constantes.IP_SERVIDOR;
        this.puerto = Constantes.PUERTO_DEFECTO;
        this.nombresOcupados = new ConcurrentSkipListSet<>();
        this.nombresOcupados.add(Constantes.NOMBRE_UNIVERSAL);
    }

    public static ServerConfig getInstance() {
        return instancia;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    // Devuelve true si el nombre estaba libre y quedó reservado; false si ya estaba ocupado
    public boolean reservarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        return nombresOcupados.add(nombre); //Si no esta -> true, si esta -> false
    }

    public void liberarNombre(String nombre) {
        if (nombre == null) {
            return;
        }
        if (nombre.equals(Constantes.NOMBRE_UNIVERSAL)) {
            return;
        }
        nombresOcupados.remove(nombre);
    }
}