package src.logica;

import java.util.ArrayList;
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
import src.utilidades.Punto;

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
    private Long tiempoInicio = null;
    private Long tiempoFinal = null;
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

    public synchronized List<Jugador> getJugadores() {
        ArrayList<Jugador> resultado = new ArrayList<>(jugadores.size());
        for (ThreadJugador hilo : jugadores) {
            resultado.add(hilo.getAnimal());
        }
        return List.copyOf(resultado);
    }

    public synchronized List<Pack> getPacks() {
        return List.copyOf(manadas);
    }

    public synchronized List<Animal> obtenerEnemigos(Pack pack) {
        ArrayList<Animal> enemigos = new ArrayList<>();
        for (Pack manada : manadas) {
            if (manada != pack) {
                for (Animal animal : manada.getLista()) {
                    if (animal.estaVivo()) {
                        enemigos.add(animal);
                    }
                }
            }
        }
        return enemigos;
    }

    public synchronized Animal obtenerAnimal(int[] idAnimal) {
        if (idAnimal == null || idAnimal.length != 2 || idAnimal[0] < 0 || idAnimal[1] < 0) {
            return null;
        }
        for (Pack pack : manadas) {
            if (pack.getId() == idAnimal[0]) {
                List<Animal> animales = pack.getLista();
                return idAnimal[1] < animales.size() ? animales.get(idAnimal[1]) : null;
            }
        }
        return null;
    }

    public synchronized int ejecutarAtaque(Pack atacante, Animal objetivo, long ahora) {
        if (atacante == null || objetivo == null) {
            return -1;
        }
        for (Animal animalAtacante : atacante.getLista()) {
            if (animalAtacante.estaVivo() && enRango(animalAtacante, objetivo)) {
                return ejecutarAtaque(atacante, animalAtacante, objetivo, ahora);
            }
        }
        return -1;
    }

    public synchronized int ejecutarAtaque(
            Pack atacante,
            Animal animalAtacante,
            Animal objetivo,
            long ahora) {
        if (estado != EstadoPartida.JUGANDO || atacante == null || animalAtacante == null
                || objetivo == null || !manadas.contains(atacante)
                || animalAtacante.getPack() != atacante || !animalAtacante.estaVivo()
                || objetivo.getPack() == atacante || !manadas.contains(objetivo.getPack())
                || !objetivo.estaVivo()
                || !enRango(animalAtacante, objetivo)) {
            return -1;
        }

        Posicion origen = animalAtacante.getPosicion();
        Posicion destino = objetivo.getPosicion();
        int damage = atacante.atacar(objetivo, ahora);
        if (damage >= 0) {
            registrarEvento(new Evento(TipoEvento.ATAQUE, origen, destino));
        }
        return damage;
    }

    public synchronized void procesarInteracciones(Pack pack) {
        if (estado != EstadoPartida.JUGANDO || pack == null || !manadas.contains(pack)) {
            return;
        }
        for (Animal animal : pack.getLista()) {
            if (!animal.estaVivo()) {
                continue;
            }
            for (Cafe cafe : cafes) {
                if (cafe.estaDisponible() && enRango(animal, cafe)
                        && cafe.consumir()) {
                    pack.aumentarDamage(Constantes.COFFEE_DAMAGE_BONUS);
                    if (pack instanceof Jugador jugador) {
                        jugador.sumarCafe();
                    }
                }
            }
            if (pack instanceof Jugador jugador) {
                for (Bandera bandera : banderas) {
                    if (bandera.getJugador() != jugador && bandera.fueTocadaPor(animal)) {
                        finalizar(jugador);
                        return;
                    }
                }
            }
        }
    }

    void reportarError(String nombre, String descripcion) {
        enviarError(nombre, descripcion);
    }

    private boolean enRango(Entidad a, Entidad b) {
        if (a == null || b == null || a.getPosicion() == null || b.getPosicion() == null
                || a.getPosicion().getPunto() == null || b.getPosicion().getPunto() == null) {
            return false;
        }
        double diferenciaX = a.getPosicion().getPunto().getX() - b.getPosicion().getPunto().getX();
        double diferenciaY = a.getPosicion().getPunto().getY() - b.getPosicion().getPunto().getY();
        return Math.hypot(diferenciaX, diferenciaY) <= Constantes.ENCOUNTER_RADIUS;
    }

    private ArrayList<int[]> generarPosicionesAleatorias(int cantidadNum) {
        if (cantidadNum < 0) {
            throw new IllegalArgumentException("La cantidad de ubicaciones no puede ser negativa.");
        }
        ArrayList<int[]> posiciones = new ArrayList<>();
        Mapa mapa = Mapa.getInstance();
        List<Zona> zonas = mapa.getZonas();
        ArrayList<Integer> zonasConNodos = obtenerZonasConNodos(zonas);
        if (cantidadNum == 0 || zonasConNodos.isEmpty()) {
            return posiciones;
        }

        GeneradorAleatorio generador = GeneradorAleatorio.getInstance();
        generador.mezclar(zonasConNodos);
        for (int i = 0; i < cantidadNum; i++) {
            int indiceZona = i < zonasConNodos.size()
                    ? zonasConNodos.get(i)
                    : zonasConNodos.get(generador.nextInt(zonasConNodos.size()));
            List<Nodo> nodos = zonas.get(indiceZona).getNodos();
            int indiceNodo = generador.nextInt(nodos.size());
            posiciones.add(new int[]{indiceZona, indiceNodo});
        }
        return posiciones;
    }

    private ArrayList<int[]> asignarZonaObjetos() { 
        return generarPosicionesAleatorias(Constantes.TOTAL_COFFEE_CUPS);
    }

    private ArrayList<int[]> asignarZonaAnimales(int cantidadPacks) {
        return generarPosicionesAleatorias(cantidadPacks);
    }

    private boolean inicializarInstancias() {
        if (jugadores.size() != Constantes.MAX_JUGADORES_PARTIDA
                || especiesElegidas.size() != jugadores.size()) {
            registrarErrorInicio(Constantes.ERROR_PARTIDA_ESTADO_INVALIDO);
            return false;
        }

        int cantidadBots = Constantes.TOTAL_BOTS;
        int cantidadPacks = jugadores.size() + cantidadBots;
        Mapa mapa = Mapa.getInstance();
        List<Zona> zonas = mapa.getZonas();
        if (obtenerZonasConNodos(zonas).size() < cantidadPacks) {
            registrarErrorInicio(Constantes.ERROR_PARTIDA_ZONAS_INSUFICIENTES);
            return false;
        }

        if (Constantes.COLORES_BANDERAS == null
                || Constantes.COLORES_BANDERAS.length < jugadores.size()) {
            registrarErrorInicio("No hay suficientes colores configurados para las banderas.");
            return false;
        }
        if (!bots.isEmpty() || !cafes.isEmpty() || !banderas.isEmpty()) {
            registrarErrorInicio("Las instancias de la partida ya fueron inicializadas.");
            return false;
        }

        ArrayList<TipoEspecie> especiesBot = new ArrayList<>(List.of(TipoEspecie.values()));
        especiesBot.remove(TipoEspecie.NINGUNA);
        if (especiesBot.isEmpty()) {
            registrarErrorInicio("No hay especies válidas configuradas para los bots.");
            return false;
        }

        for (ThreadJugador hilo : jugadores) {
            Jugador jugador = hilo.getAnimal();
            TipoEspecie especie = especiesElegidas.get(jugador.getNickname());
            if (especie == null || especie == TipoEspecie.NINGUNA) {
                registrarErrorInicio(Constantes.ERROR_PARTIDA_ESPECIE_NO_DISPONIBLE);
                return false;
            }
            if (!jugador.getLista().isEmpty()) {
                registrarErrorInicio("Los animales de un jugador ya estaban inicializados.");
                return false;
            }
        }

        ArrayList<int[]> ubicacionesAnimales = asignarZonaAnimales(cantidadPacks);
        ArrayList<int[]> ubicacionesCafes = asignarZonaObjetos();
        if (ubicacionesAnimales.size() != cantidadPacks
                || ubicacionesCafes.size() != Constantes.TOTAL_COFFEE_CUPS) {
            registrarErrorInicio(Constantes.ERROR_PARTIDA_ZONAS_INSUFICIENTES);
            return false;
        }

        ArrayList<Nodo> nodosIniciales = new ArrayList<>(cantidadPacks);
        for (int[] ubicacion : ubicacionesAnimales) {
            Nodo nodo = obtenerNodo(mapa, ubicacion);
            if (nodo.getZona() == null) {
                registrarErrorInicio("Cada nodo de inicio debe pertenecer a una zona.");
                return false;
            }
            nodosIniciales.add(nodo);
        }

        ArrayList<Nodo> nodosCafes = new ArrayList<>(ubicacionesCafes.size());
        for (int[] ubicacion : ubicacionesCafes) {
            nodosCafes.add(obtenerNodo(mapa, ubicacion));
        }

        GeneradorAleatorio generador = GeneradorAleatorio.getInstance();
        ArrayList<ThreadBot> nuevosBots = new ArrayList<>(cantidadBots);
        for (int indiceBot = 0; indiceBot < cantidadBots; indiceBot++) {
            TipoEspecie especie = especiesBot.get(generador.nextInt(especiesBot.size()));
            Bot bot = new Bot(jugadores.size() + indiceBot, especie);
            bot.crearAnimales(nodosIniciales.get(jugadores.size() + indiceBot));
            nuevosBots.add(new ThreadBot(this, bot));
        }

        ArrayList<Bandera> nuevasBanderas = new ArrayList<>(jugadores.size());
        for (int indiceJugador = 0; indiceJugador < jugadores.size(); indiceJugador++) {
            Jugador jugador = jugadores.get(indiceJugador).getAnimal();
            nuevasBanderas.add(new Bandera(
                    jugador.getId(),
                    crearPosicion(nodosIniciales.get(indiceJugador)),
                    jugador,
                    Constantes.COLORES_BANDERAS[indiceJugador]));
        }

        ArrayList<Cafe> nuevosCafes = new ArrayList<>(Constantes.TOTAL_COFFEE_CUPS);
        for (int indiceCafe = 0; indiceCafe < nodosCafes.size(); indiceCafe++) {
            nuevosCafes.add(new Cafe(indiceCafe, crearPosicion(nodosCafes.get(indiceCafe))));
        }

        for (int indiceJugador = 0; indiceJugador < jugadores.size(); indiceJugador++) {
            Jugador jugador = jugadores.get(indiceJugador).getAnimal();
            Nodo nodoInicial = nodosIniciales.get(indiceJugador);
            jugador.elegirEspecie(especiesElegidas.get(jugador.getNickname()));
            jugador.elegirZona(nodoInicial.getZona());
            jugador.crearAnimales(nodoInicial);
        }

        bots.addAll(nuevosBots);
        for (ThreadBot hiloBot : nuevosBots) {
            manadas.add(hiloBot.getBot());
        }
        banderas.addAll(nuevasBanderas);
        cafes.addAll(nuevosCafes);
        return true;
    }

    private ArrayList<Integer> obtenerZonasConNodos(List<Zona> zonas) {
        ArrayList<Integer> zonasConNodos = new ArrayList<>();
        if (zonas == null) {
            return zonasConNodos;
        }
        for (int indiceZona = 0; indiceZona < zonas.size(); indiceZona++) {
            Zona zona = zonas.get(indiceZona);
            if (zona == null) {
                continue;
            }
            List<Nodo> nodos = zona.getNodos();
            if (nodos.isEmpty()) {
                continue;
            }
            boolean nodosValidos = true;
            for (Nodo nodo : nodos) {
                if (nodo == null || nodo.getPosicion() == null || nodo.getZona() == null) {
                    nodosValidos = false;
                    break;
                }
            }
            if (nodosValidos) {
                zonasConNodos.add(indiceZona);
            }
        }
        return zonasConNodos;
    }

    private Nodo obtenerNodo(Mapa mapa, int[] ubicacion) {
        Nodo nodo = mapa.getNodo(ubicacion);
        if (nodo == null || nodo.getPosicion() == null) {
            throw new IllegalStateException("La ubicación seleccionada no contiene un nodo válido.");
        }
        return nodo;
    }

    private Posicion crearPosicion(Nodo nodo) {
        Punto punto = nodo.getPosicion();
        return new Posicion(nodo, new Punto(punto.getX(), punto.getY()));
    }

    private void registrarErrorInicio(String detalle) {
        Logger.getInstance().crearLog(NivelLog.ERROR, Constantes.ORIGEN_PARTIDA, detalle, null);
        for (ThreadJugador hilo : jugadores) {
            enviarError(hilo.getAnimal().getNickname(), detalle);
        }
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
        if (!inicializarInstancias()) {
            return false;
        }
        estado = EstadoPartida.JUGANDO;
        notificador = new Notificador(this);
        ejecutor = Executors.newCachedThreadPool();
        tiempoInicio = System.nanoTime();
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
        if (especie == null || especie == TipoEspecie.NINGUNA
                || especiesElegidas.containsValue(especie)) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ESPECIE_NO_DISPONIBLE);
            return;
        }

        especiesElegidas.put(nombre, especie);
        buscarJugador(nombre).getAnimal().elegirEspecie(especie);
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
        if (animales == null || animales.isEmpty() || idNodo == null || idNodo.length < 2) {
            enviarError(nombre, Constantes.ERROR_ANIMALES);
            return;
        }
        int idPack = hilo.getAnimal().getId();
        for (int[] animal : animales) {
            if (animal == null || animal.length != 2 || animal[1] < 0) {
                enviarError(nombre, Constantes.ERROR_ANIMALES);
                return;
            }
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
        if (A == null || A.length != 2 || B == null || B.length != 2
                || A[1] < 0 || B[0] < 0 || B[1] < 0) {
            enviarError(nombre, Constantes.ERROR_PARTIDA_ATAQUE_INVALIDO);
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
        this.tiempoFinal = System.nanoTime();
        return true;
    }

    public synchronized int[] getBotsVivosTotales() {
        int[] datos = new int[2];
        int botsVivos = 0; //Packs de bots vivos
        for (ThreadBot hilo : bots) {
            if (hilo.getBot().getVivos() > 0) { 
                botsVivos++;
            }
        }
        datos[0] = botsVivos;
        datos[1] = this.bots.size();
        return datos;
    }
 
    public synchronized int getCafesRestantes() {
        int cafesRestantes = 0;
        for (Cafe cafe : cafes) {
            if (cafe.estaDisponible()) {
                cafesRestantes++;
            }
        }
        return cafesRestantes;
    }

    public synchronized long tiempoTranscurrido() {
        long resultado = 0;
        if (this.tiempoInicio != null) {
            //Termino
            if (this.tiempoFinal != null) { 
                resultado = this.tiempoFinal - this.tiempoInicio;
            }
            //No ha terminado
            else {
                resultado = System.nanoTime() - this.tiempoInicio;
            }
        }
        return  resultado / 1000000; //Nanosegundo -> milisegundos
    }

    public String getGanador() {
        return this.ganador;
    }

    public ArrayList<ResumenJugador> resumirJugadores() {
        ArrayList<ResumenJugador> resumenes = new ArrayList<>();
        for (ThreadJugador jugadores : jugadores) {
            resumenes.add(new ResumenJugador(jugadores.getAnimal()));
        }
        return resumenes;
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
                if (animal.getZonaActual() == zona) {
                    entidades.add(animal);
                }
            }
        }
        for (ThreadBot bot : bots) {
            for (Animal animal : bot.getBot().getLista()) {
                if (animal.getZonaActual() == zona) {
                    entidades.add(animal);
                }
            }
        }
        for (Cafe cafe : cafes) {
            if (estaEnZona(cafe, zona)) {
                entidades.add(cafe);
            }
        }
        for (Bandera bandera : banderas) {
            if (estaEnZona(bandera, zona)) {
                entidades.add(bandera);
            }
        }
        return entidades;
    }

    private boolean estaEnZona(Entidad entidad, Zona zona) {
        Posicion posicion = entidad.getPosicion();
        return posicion != null
                && posicion.getNodo() != null
                && posicion.getNodo().getZona() == zona;
    }

    private DatosEnviar obtenerDatos(Jugador jugador) {
        Zona zona = filtrarZona(jugador);
        List<Entidad> entidades = zona == null ? List.of() : obtenerEntidadesConZona(zona);
        Jugador oponente = oponenteDe(jugador);
        Datos datos = new Datos(
                jugador,
                oponente == null ? null : oponente.copiaBasica(),
                entidades,
                zona,
                eventosDelTick);
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
        disponibles.remove(TipoEspecie.NINGUNA);
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