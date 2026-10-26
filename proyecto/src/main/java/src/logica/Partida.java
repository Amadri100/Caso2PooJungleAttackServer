package src.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import src.controlador.Controlador;
import src.enviables.*;
import src.mapa.Zona;
public class Partida implements Runnable {

    private Controlador controlador;
    private String codigo;
    private EstadoPartida estado;
    private ArrayList<ThreadJugador> jugadores;
    private ArrayList<ThreadBot> bots;
    private ArrayList<Pack> manadas;
    private ArrayList<Cafe> cafes;
    private ArrayList<Bandera> banderas;
    private ExecutorService ejecutor;
    private ConcurrentLinkedQueue<Evento> eventosPendientes;
    private Notificador notificador;
    private long inicio;
    private String ganador;

    public Partida(Controlador controlador, String codigo) {
        this.controlador = controlador;
        this.codigo = codigo;
        this.jugadores = new ArrayList<>();
        this.bots = new ArrayList<>();
        this.manadas = new ArrayList<>();
        this.cafes = new ArrayList<>();
        this.banderas = new ArrayList<>();
        this.eventosPendientes = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public boolean agregarJugador(String nickname) { //Termina de iniciar la partida
        return false;
    }

    public boolean crearJuego() {
        return false;
    }

    public boolean iniciarPartida() {
        return false;
    }

    public void seleccionarEspecie(String nombre, TipoEspecie especie) {
    }

    public void moverAnimales(String nombre, List<int[]> animales, int idNodoDestino) {
    }

    public void mandarAtaque(String nombre, int[] A, int[] B) {
    }

    public void verZona(String nombre, int idZona) {
    }

    public void registrarEvento(Evento evento) {
    }

    public void notificar(ArrayList<DatosEnviar> datos) {
    }

    public boolean finalizar(Jugador ganador) {
        return false;
    }

    public ResumenPartida getResumen() {
        return null;
    }

    private void asignarZonas() {
    }

    private void crearBanderas() {
    }

    private void crearAnimales() {
    }

    private void crearCafes() {
    }

    private Zona filtrarZona(Jugador jugador) {
        return null;
    }

    private DatosEnviar obtenerDatos(Jugador jugador) {
        return null;
    }

    private ArrayList<DatosEnviar> construirDatos() {
        return null;
    }

}