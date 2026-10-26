package src.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import src.enviables.DatosEnviar;
import src.logica.Partida;
import src.logica.ResumenPartida;
import src.mapa.Mapa;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeServidor;
import src.server.Servidor;

public class Controlador {

    private Mapa mapa;
    private Servidor servidor;
    private ConcurrentHashMap<String, Partida> partidas;
    private ConcurrentHashMap<String, String> partidaDeJugador;
    private ExecutorService ejecutor;
    private EjecutorMensajes ejecutorMensajes;

    public Controlador() {
        this.partidas = new ConcurrentHashMap<>();
        this.partidaDeJugador = new ConcurrentHashMap<>();
    }

    public void ejecutarMensaje(MensajeJugador msg) {}

    public void actualizarEstado(ArrayList<DatosEnviar> datos) {}

    public void enviar(String nombre, MensajeServidor msg) {}

    public void enviarATodos(MensajeServidor msg) {}

    public String crearJuego(String nickname) {
        return null;
    }

    public boolean unirsePartida(String codigo, String nickname) {
        return false;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Partida getPartidaDe(String nombre) {
        return null;
    }

    public void finalizarPartida(String codigo) {}

    public void jugadorDesconectado(String nombre) {}

    public List<ResumenPartida> getResumenesPartidas() {
        return null;  
    }

    public int getCantidadPartidas() {
        return partidas.size();
    }

}