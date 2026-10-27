package src.logica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import src.controlador.Controlador;
import src.enviables.*;
import src.mapa.Mapa;
import src.mapa.Nodo;
import src.mapa.Posicion;
import src.mapa.Zona;
import src.mensajes.MensajeAnimalSelection;
import src.mensajes.MensajeCaso;
import src.mensajes.MensajeServidor;
import src.utilidades.Constantes;
import src.utilidades.GeneradorAleatorio;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public class Partida implements Runnable {

    private Controlador controlador;
    private String codigo;
    private volatile EstadoPartida estado;
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

    // Especie elegida por cada jugador durante la fase de selección (nickname -> especie)
    private LinkedHashMap<String, TipoEspecie> especiesElegidas = new LinkedHashMap<>();
    // Zona que cada jugador está viendo (nickname -> zona). Si no hay, se usa la primera con animales suyos.
    private ConcurrentHashMap<String, Zona> zonaVista = new ConcurrentHashMap<>();
    // Eventos recogidos en la actualización actual; se incluyen en los datos de todos los jugadores
    private volatile List<Evento> eventosDelTick = List.of();

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

    // Ciclo de actualización: cada intervalo recoge los eventos y manda a cada jugador su estado
    @Override
    public void run() {
        while (estado == EstadoPartida.JUGANDO) {
            try {
                Thread.sleep(Constantes.INTERVALO_ACTUALIZACION_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (estado != EstadoPartida.JUGANDO) {
                break;
            }
            enviarEstado(false);
        }
    }

    public Controlador getControlador() {
        return controlador;
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    private ArrayList<int[]> generarPosicionesAleatorias(int cantidadNum) {
        ArrayList<int[]> posiciones = new ArrayList<>();
        Mapa mapa = Mapa.getInstance();
        int totalZonas = mapa.getZonas().size();
        GeneradorAleatorio generador = GeneradorAleatorio.getInstance();
        ArrayList<Integer> indicesZonas = new ArrayList<>(); 
        for (int i = 0; i < totalZonas; i++) {
            indicesZonas.add(i);
        }
        generador.mezclar(indicesZonas);
        // Se asignan las zonas
        for (int i = 0; i < cantidadNum; i++) {
            int indiceZona = indicesZonas.get(i);
            Zona nodos = mapa.getZonas().get(indiceZona);
            int indiceNodo = generador.nextInt(nodos.getNodos().size());
            posiciones.add(new int[]{indiceZona, indiceNodo});
        }
        return posiciones;
    }

    private ArrayList<int[]> asignarZonaObjetos() { 
        return generarPosicionesAleatorias(Constantes.TOTAL_COFFEE_CUPS);
    }

    private ArrayList<int[]> asignarZonaAnimales() {
        return generarPosicionesAleatorias(Constantes.TOTAL_ANIMALES);
    }

    private void incializarInstancias() {

    }


    // Prepara una partida nueva. Solo se puede llamar una vez.
    public synchronized boolean crearJuego() {
        if (estado != null) {
            return false;
        }
        estado = EstadoPartida.ESPERANDO;
        return true;
    }

    // Agrega un jugador. Al entrar el último, termina de iniciar la partida:
    // pasa a la fase de selección y manda a cada jugador las especies disponibles.
    public synchronized boolean agregarJugador(String nickname) {
        if (estado != EstadoPartida.ESPERANDO
                || nickname == null || nickname.isBlank()
                || buscarJugador(nickname) != null
                || jugadores.size() >= Constantes.MAX_JUGADORES_PARTIDA) {
            return false;
        }

        // AJUSTE: se asume el constructor Jugador(int id, String nickname)
        Jugador jugador = new Jugador(jugadores.size(), nickname);
        jugadores.add(new ThreadJugador(this, jugador));
        manadas.add(jugador);

        if (jugadores.size() == Constantes.MAX_JUGADORES_PARTIDA) {
            estado = EstadoPartida.SELECCION;
            for (ThreadJugador hilo : jugadores) {
                enviar(hilo.getAnimal().getNickname(),
                        new MensajeAnimalSelection(especiesDisponibles(), null));
            }
        }
        return true;
    }

    // Empieza el juego: crea el mundo, avisa a los jugadores y arranca los hilos
    public synchronized boolean iniciarPartida() {
        if (estado != EstadoPartida.SELECCION
                || especiesElegidas.size() < Constantes.MAX_JUGADORES_PARTIDA) {
            return false;
        }
        estado = EstadoPartida.JUGANDO;
        inicio = System.currentTimeMillis();

        incializarInstancias();

        notificador = new Notificador(this);
        ejecutor = Executors.newCachedThreadPool();
        enviarEstado(true);

        for (ThreadJugador hilo : jugadores) {
            ejecutor.execute(hilo);
        }
        for (ThreadBot hilo : bots) {
            ejecutor.execute(hilo);
        }
        ejecutor.execute(this);
        return true;
    }

    // Cada jugador elige una especie distinta. Cuando ambos eligieron, empieza la partida.
    public synchronized void seleccionarEspecie(String nombre, TipoEspecie especie) {
        if (estado != EstadoPartida.SELECCION) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ESTADO_INVALIDO);
            return;
        }
        if (buscarJugador(nombre) == null) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_JUGADOR_NO_ENCONTRADO);
            return;
        }
        if (especiesElegidas.containsKey(nombre)) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ESPECIE_YA_ELEGIDA);
            return;
        }
        if (especie == null || especiesElegidas.containsValue(especie)) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ESPECIE_NO_DISPONIBLE);
            return;
        }

        especiesElegidas.put(nombre, especie);
        enviar(nombre, new MensajeCaso(true, Constantes.NO_HAY_ERROR));

        // El oponente ve qué especie se eligió y cuáles quedan
        String oponente = nombreOponente(nombre);
        if (oponente != null) {
            enviar(oponente, new MensajeAnimalSelection(especiesDisponibles(), especie));
        }

        if (especiesElegidas.size() == Constantes.MAX_JUGADORES_PARTIDA) {
            iniciarPartida();
        }
    }

    // Valida la orden y se la pasa al hilo del jugador, que calcula la ruta con el mapa
    public void moverAnimales(String nombre, List<int[]> animales, int[] idNodo) {
        ThreadJugador hilo = validarAccion(nombre);
        if (hilo == null) {
            return;
        }
        int idPack = hilo.getAnimal().getId();
        for (int[] animal : animales) {
            if (animal[0] != idPack) {
                enviarError(nombre, Constantes.ERROR_PARTIDA_ANIMAL_AJENO);
                return;
            }
        }
        // AJUSTE: método nuevo en ThreadJugador
        hilo.moverAnimales(animales, idNodo);
    }

    public void mandarAtaque(String nombre, int[] A, int[] B) {
        ThreadJugador hilo = validarAccion(nombre);
        if (hilo == null) {
            return;
        }
        int idPack = hilo.getAnimal().getId();
        if (A[0] != idPack) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ANIMAL_AJENO);
            return;
        }
        if (B[0] == idPack) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ATAQUE_INVALIDO);
            return;
        }
        hilo.agregarAtaque(A, B);
    }

    // Solo se puede ver una zona donde el jugador tenga animales. El cambio llega en la próxima actualización.
    public void verZona(String nombre, int idZona) {
        ThreadJugador hilo = validarAccion(nombre);
        if (hilo == null) {
            return;
        }
        for (Zona zona : hilo.getAnimal().getZonasAnimales()) {
            // AJUSTE: se asume Zona.getId()
            if (zona.getId() == idZona) {
                zonaVista.put(nombre, zona);
                return;
            }
        }
        enviarError(nombre, Constantes.ERROR_PARTIDA_ZONA_NO_VISIBLE);
    }

    public void registrarEvento(Evento evento) {
        if (evento != null) {
            eventosPendientes.add(evento);
        }
    }

    // Reparte los datos (nickname -> datos de ese jugador) mediante el Notificador
    public void notificar(HashMap<String, DatosEnviar> datos) {
        if (notificador != null) {
            notificador.enviarDatos(datos);
        }
    }

    public synchronized List<String> getNombresJugadores() {
        ArrayList<String> nombres = new ArrayList<>();
        for (ThreadJugador jugador : jugadores) {
            nombres.add(jugador.getAnimal().getNickname());
        }
        return nombres;
    }

    // Termina la partida: detiene los hilos, avisa del ganador y se retira del Controlador
    public synchronized boolean finalizar(Jugador ganador) {
        if (estado != EstadoPartida.JUGANDO || ganador == null) {
            return false;
        }
        estado = EstadoPartida.TERMINADA;
        this.ganador = ganador.getNickname();

        for (ThreadJugador hilo : jugadores) {
            hilo.detener();
        }
        for (ThreadBot hilo : bots) {
            hilo.detener();
        }
        notificador.enviarFin(this.ganador);
        ejecutor.shutdown();
        controlador.finalizarPartida(codigo);
        return true;
    }

 public synchronized ResumenPartida getResumen() {
        ArrayList<ResumenJugador> resumenJugadores = new ArrayList<>();
        for (ThreadJugador hilo : jugadores) {
            resumenJugadores.add(resumirJugador(hilo.getAnimal()));
        }

        int botsVivos = 0; //Packs de bots vivos
        for (ThreadBot hilo : bots) {
            if (hilo.getBot().getVivos() > 0) { 
                botsVivos++;
            }
        }

        int cafesRestantes = 0;
        for (Cafe cafe : cafes) {
            if (cafe.estaDisponible()) {
                cafesRestantes++;
            }
        }

        return new ResumenPartida(codigo, estado, resumenJugadores, botsVivos, bots.size(),
                cafesRestantes, System.currentTimeMillis(), ganador);
    }

    private ResumenJugador resumirJugador(Jugador jugador) {
        ResumenJugador resumen = new ResumenJugador();
        resumen.setEspecie(jugador.getEspecie());
        resumen.setDamage(jugador.getDamage());
        resumen.setConteoDeBajas(jugador.getConteoDeBajas());
        resumen.setTotalVivos(jugador.getVivos());
        resumen.setZonaElegida(filtrarZona(jugador));
        resumen.setCafesConsumidos(jugador.getCafesConsumidos());
        resumen.setBotsEliminados(jugador.getBotsEliminados());
        return resumen;
    }


    // Zona que ve el jugador: la elegida con verZona (si aún tiene animales ahí) o la primera con animales suyos
    private Zona filtrarZona(Jugador jugador) {
        ArrayList<Zona> zonas = jugador.getZonasAnimales();
        Zona elegida = zonaVista.get(jugador.getNickname());
        if (elegida != null && zonas.contains(elegida)) {
            return elegida;
        }
        return zonas.isEmpty() ? null : zonas.get(0);
    }

    private List<Entidad> obtenerEntidadesConZona(Zona zona) {
        ArrayList<Entidad> entidades = new ArrayList<>();
        //Itera entre todos y los filtra segun si son iguales a la zona
        for (ThreadJugador jugador : jugadores) {
            for (Animal animal : jugador.getAnimal().getLista()) {
                if (animal.getPosicion().getNodo().getZona().equals(zona)) {
                    entidades.add(animal);
                }
            }
        }
        for (ThreadBot bot : bots) {
            for (Animal animal : bot.getBot().getLista()) {
                if (animal.getPosicion().getNodo().getZona().equals(zona)) {
                    entidades.add(animal);
                }
            }
        }
        for (Cafe cafe : cafes) {
            if (cafe.getPosicion().getNodo().getZona().equals(zona)) {
                entidades.add(cafe);
            }
        }
        for (Bandera bandera : banderas) {
            if (bandera.getPosicion().getNodo().getZona().equals(zona)) {
                entidades.add(bandera);
            }
        }
        return entidades;
    }
    private DatosEnviar obtenerDatos(Jugador jugador) {
        Zona zona = filtrarZona(jugador);
        // AJUSTE: se asume Zona.getEntidades() : List<Entidad>
        List<Entidad> entidades = zona == null ? List.of() : obtenerEntidadesConZona(zona);
        // AJUSTE: se asume el constructor Datos(Jugador a, Jugador b, Zona zona, List<Entidad> entidades, List<Evento> eventos)
        Datos datos = new Datos(jugador, oponenteDe(jugador), entidades, zona, eventosDelTick);
        return DatosEnviar.desde(datos);
    }

    // Un DatosEnviar por jugador (cada uno ve lo suyo). DatosEnviar no lleva nombre, por eso es un mapa.
    private HashMap<String, DatosEnviar> construirDatos() {
        ArrayList<Evento> eventos = new ArrayList<>();
        Evento evento;
        while ((evento = eventosPendientes.poll()) != null) {
            eventos.add(evento);
        }
        eventosDelTick = eventos;

        HashMap<String, DatosEnviar> resultado = new HashMap<>();
        for (ThreadJugador hilo : jugadores) {
            Jugador jugador = hilo.getAnimal();
            resultado.put(jugador.getNickname(), obtenerDatos(jugador));
        }
        return resultado;
    }

    // Si la conversión falla no se detiene la partida: se registra y se reintenta en la siguiente actualización
    private void enviarEstado(boolean esInicio) {
        try {
            HashMap<String, DatosEnviar> datos = construirDatos();
            if (esInicio) {
                notificador.enviarInicio(datos);
            } else {
                notificar(datos);
            }
        } catch (RuntimeException e) {
            Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.ORIGEN_PARTIDA,
                    Constantes.ERROR_PARTIDA_ENVIAR_ESTADO, e);
        }
    }

    // ---- Auxiliares ----

    // Comprueba que la partida esté en juego y que el jugador pertenezca; si no, le avisa y devuelve null
    private ThreadJugador validarAccion(String nombre) {
        if (estado != EstadoPartida.JUGANDO) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ESTADO_INVALIDO);
            return null;
        }
        ThreadJugador hilo = buscarJugador(nombre);
        if (hilo == null) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_JUGADOR_NO_ENCONTRADO);
        }
        return hilo;
    }

    private synchronized ThreadJugador buscarJugador(String nombre) {
        for (ThreadJugador hilo : jugadores) {
            if (hilo.getAnimal().getNickname().equals(nombre)) {
                return hilo;
            }
        }
        return null;
    }

    private synchronized String nombreOponente(String nombre) {
        for (ThreadJugador hilo : jugadores) {
            if (!hilo.getAnimal().getNickname().equals(nombre)) {
                return hilo.getAnimal().getNickname();
            }
        }
        return null;
    }

    private synchronized Jugador oponenteDe(Jugador jugador) {
        for (ThreadJugador hilo : jugadores) {
            if (hilo.getAnimal() != jugador) {
                return hilo.getAnimal();
            }
        }
        return null;
    }

    // Todas las especies que aún no ha elegido nadie
    private List<TipoEspecie> especiesDisponibles() {
        ArrayList<TipoEspecie> disponibles = new ArrayList<>(List.of(TipoEspecie.values()));
        disponibles.removeAll(especiesElegidas.values());
        return disponibles;
    }

    private void enviar(String nombre, MensajeServidor mensaje) {
        controlador.enviar(nombre, mensaje);
    }

    private void enviarError(String nombre, String descripcion) {
        controlador.enviar(nombre, new MensajeCaso(descripcion));
    }

}