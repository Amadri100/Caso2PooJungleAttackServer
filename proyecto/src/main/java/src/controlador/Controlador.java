package src.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import src.enviables.DatosEnviar;
import src.logica.Partida;
import src.logica.ResumenPartida;
import src.mapa.Mapa;
import src.mensajes.MensajeJugador;
import src.mensajes.MensajeServidor;
import src.mensajes.MensajeStateUpdate;
import src.server.Servidor;
import src.utilidades.GeneradorAleatorio;

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
        this.ejecutor = Executors.newCachedThreadPool();
        this.ejecutorMensajes = new EjecutorMensajes(this);
        ejecutorMensajes.registrar(new ManejadorCreateMatch());
        ejecutorMensajes.registrar(new ManejadorJoinMatch());
        ejecutorMensajes.registrar(new ManejadorSelectAnimal());
        ejecutorMensajes.registrar(new ManejadorMoveAnimals());
        ejecutorMensajes.registrar(new ManejadorAttack());
        ejecutorMensajes.registrar(new ManejadorViewZone());
    }

    // Temporal,
    public void registrarManejador(ManejadorMensaje manejador) {
        ejecutorMensajes.registrar(manejador);
    }

    // Ejecuta el mensaje en otro hilo para no bloquear el hilo que lee del socket
    public void ejecutarMensaje(MensajeJugador msg) {
        ejecutor.execute(() -> ejecutorMensajes.ejecutar(msg)); //No bloquea el thread
    }

    // Manda a cada jugador su estado; el nombre asociado a cada dato decide el destinatario
    // (si es NOMBRE_UNIVERSAL, el Servidor lo envía a todos)
    public void actualizarEstado(ArrayList<DatosEnviar> datos) {
        if (datos == null) {
            return;
        }
        for (DatosEnviar dato : datos) {
            enviar(dato.getJugadorA().getNickname(), new MensajeStateUpdate(dato));
        }
    }

    public void enviar(String nombre, MensajeServidor msg) {
        if (servidor != null) {
            servidor.mandarACliente(nombre, msg);
        }
    }

    public void enviarATodos(MensajeServidor msg) {
        if (servidor != null) {
            servidor.mandarATodos(msg);
        }
    }

    // Crea una partida con un código único y agrega al creador. Devuelve el código, o null si falla.
    public String crearJuego(String nickname) {
        if (nickname == null || nickname.isBlank()) { //Nombre no valido
            return null;
        }
        String codigo;
        Partida partida;
        do { //Intenta encontrar un codigo para la partida.
            codigo = GeneradorAleatorio.getInstance().generarCodigo();
            partida = new Partida(this, codigo);
        } while (partidas.putIfAbsent(codigo, partida) != null);

        if (partidaDeJugador.putIfAbsent(nickname, codigo) != null) {
            partidas.remove(codigo);
            return null;
        }

        if (!partida.crearJuego() || !partida.agregarJugador(nickname)) {
            partidas.remove(codigo);
            partidaDeJugador.remove(nickname, codigo);
            return null;
        }
        return codigo;
    }

    public boolean unirsePartida(String codigo, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return false;
        }

        Partida partida = partidas.get(codigo);
        if (partida == null) {
            return false;
        }

        if (partidaDeJugador.putIfAbsent(nickname, codigo) != null) { //Si ya tiene partida
            return false;
        }

        if (!partida.agregarJugador(nickname)) {
            partidaDeJugador.remove(nickname, codigo);
            return false;
        }
        
        return true;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public Partida getPartidaDe(String nombre) {
        String codigo = partidaDeJugador.get(nombre);
        if (codigo == null) {
            return null;
        }
        return partidas.get(codigo);
    }

    // Quita la partida y libera a todos sus jugadores
    public void finalizarPartida(String codigo) {
        if (partidas.remove(codigo) == null) {
            return;
        }
        partidaDeJugador.values().removeIf(c -> c.equals(codigo));
    }

    // Libera al jugador; si era el último de su partida, la partida se elimina
    public void jugadorDesconectado(String nombre) {
        String codigo = partidaDeJugador.remove(nombre);
        if (codigo == null) {
            return;
        }
        boolean quedanJugadores = partidaDeJugador.containsValue(codigo);
        if (!quedanJugadores) {
            finalizarPartida(codigo);
        }
    }

    public List<ResumenPartida> getResumenesPartidas() {
        return partidas.values().stream() //Obtiene todas las partidas
                .map(ResumenPartida::crearResumenSafe) //Crea cada resumen
                .filter(Objects::nonNull) //Eimina los que son null
                .collect(Collectors.toList()); //Covinerte a lista
    }

    public int getCantidadPartidas() {
        return partidas.size();
    }

}