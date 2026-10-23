## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Se utilizo java 21 con Maven, Jackson, jsvg y JUnit5

Estudiantes: Andres Madrigal y David Zuniga

## Spec Server

```
src/
|-- main/
    |-- Main.java
|-- server/
    |-- utilidades/
        |-- Json.java
        |-- Punto.java
        |-- Constantes.java
        |-- GeneradorAleatorio.java
        |-- NivelLog.java
        |-- EntradaLog.java
        |-- LogListener.java
        |-- Logger.java
    |-- ui/
        |-- VentanaServidor.java
        |-- PanelPartidas.java
        |-- TarjetaPartida.java
        |-- ModeloTablaJugadores.java
        |-- PanelConsola.java
        |-- PanelControl.java
    |-- server/
        |-- ClientHandler.java
        |-- ThreadConexiones.java
        |-- Servidor.java
        |-- ServerConfig.java
    |-- controlador/
        |-- Controlador.java
        |-- EjecutorMensajes.java
        |-- ManejadorMensaje.java
        |-- ManejadorCreateMatch.java
        |-- ManejadorJoinMatch.java
        |-- ManejadorSelectAnimal.java
        |-- ManejadorMoveAnimals.java
        |-- ManejadorAttack.java
        |-- ManejadorViewZone.java
    |-- enviables/
        |-- DatosEnviar.java
        |-- JugadorEnviar.java
        |-- EntidadEnviar.java
        |-- AnimalEnviar.java
        |-- CafeEnviar.java
        |-- BanderaEnviar.java
        |-- EventoEnviar.java
        |-- ZonaEnviar.java
        |-- NodoEnviar.java
        |-- PosicionEnviar.java
    |-- logica/
        |-- EstadoPartida.java
        |-- Partida.java
        |-- ResumenPartida.java
        |-- ResumenJugador.java
        |-- Notificador.java
        |-- ThreadAnimal.java
        |-- ThreadJugador.java
        |-- ThreadBot.java
        |-- EstrategiaBot.java
        |-- EstrategiaDirecta.java
        |-- EstrategiaCercana.java
        |-- EstrategiaAleatoria.java
        |-- SelectorEstrategia.java
        |-- TipoEntidad.java
        |-- Entidad.java
        |-- Animal.java
        |-- Pack.java
        |-- Jugador.java
        |-- Bot.java
        |-- Cafe.java
        |-- Bandera.java
        |-- TipoEvento.java
        |-- Evento.java
        |-- Datos.java
        |-- TipoEspecie.java
    |-- mapa/
        |-- Posicion.java
        |-- Ruta.java
        |-- Mapa.java
        |-- Zona.java
        |-- Nodo.java
        |-- Vecinos.java
    |-- mensajes/
        |-- TipoMensaje.java
        |-- Mensaje.java
        |-- MensajeJugador.java
        |-- MensajeServidor.java
        |-- MensajeIndicarNombre.java
        |-- MensajeCreateMatch.java
        |-- MensajeJoinMatch.java
        |-- MensajeSelectAnimal.java
        |-- MensajeMoveAnimals.java
        |-- MensajeAttack.java
        |-- MensajeViewZone.java
        |-- MensajePrimerMensaje.java
        |-- MensajeMatchCreated.java
        |-- MensajeMatchJoined.java
        |-- MensajeAnimalSelection.java
        |-- MensajeGameStarted.java
        |-- MensajeStateUpdate.java
        |-- MensajeGameOver.java
        |-- MensajeError.java
|-- test/
    |-- JsonTest.java
    |-- MapaTest.java
    |-- PartidaTest.java
    |-- ControladorTest.java
```

### main
#### Main
+ main(String args[]) : static void 

// Orden de arranque:
// Logger -> GeneradorAleatorio ->Servidor -> Ventana -> Servidor.iniciar()

### utilidades

#### Json
+ mensajeAJson(msg : Mensaje) : Static String
+ jsonAMensaje(json : String) : Static Mensaje

#### Punto 
- x : double
- y : double
+ Punto(x : double, y : double) : Punto
+ getX() : double
+ getY() : double

#### Constantes (Clase final)
+ LONGITUD_CODIGO : static final int
+ IP_SERVIDOR : static final String 
+ ENCOUNTER_RADIUS : static final double 
+ ANIMAL_SPEED : static final double 
+ ATTACK_COOLDOWN_MS : static final long 
+ MAX_PLAYER_ENERGY : static final int 
+ PLAYER_INITIAL_ENERGY : static final int 
+ PLAYER_DAMAGE : static final int 
+ BOT_INITIAL_ENERGY : static final int
+ BOT_DAMAGE : static final int }
+ PLAYER_PACK_SIZE : static final int
+ BOT_PACK_SIZE : static final int
+ TOTAL_ZONES : static final int 
+ TOTAL_COFFEE_CUPS : static final int 
+ NOMBRE_UNIVERSAL : static final String
+ MOVIMIENTOS_MINIMOS_CAMBIAR : static final int
+ INTERVALO_STATE_UPDATE_MS : static final long
+ LOG_FILE : static final String                  
+ SEED_POR_DEFECTO : static final long
+ INTERVALO_REFRESCO_UI_MS : static final int      
+ MAX_LINEAS_CONSOLA : static final int            
+ MAX_HISTORIAL_LOG : static final int             
- Constantes()

// Las constantes de este archivo no seran modificadas, por lo tanto no es necesario usar un singleton.
// Ip = "localhost", que es la ip de el dispositvo
// NOMBRE_UNIVERSAL string como @Todos, reservado especificamente para referirse a todos los clientes.

#### GeneradorAleatorio (singleton)
- static instancia : GeneradorAleatorio
- seed : long
- seedFija : boolean
- random : Random                     
- streams : ConcurrentHashMap\<String, Random>   
- GeneradorAleatorio()
+ static getInstance() : GeneradorAleatorio
+ seedAleatoria() : void
+ setSeed(seed : long) : void           
+ getSeed() : long
+ setSeedFija(fija : boolean) : void  
+ isSeedFija() : boolean
+ reiniciar() : void                  
+ nextInt(limite : int) : int
+ nextInt(min : int, max : int) : int    
+ nextDouble() : double
+ nextBoolean() : boolean
+ elegir(lista : List\<T>) : T
+ mezclar(lista : List\<T>) : void
+ generarCodigo() : String 

#### NivelLog (enum)
+ DEBUG
+ INFO
+ WARN
+ ERROR

#### EntradaLog
- momento : LocalDateTime
- nivel : NivelLog
- origen : String          
- mensaje : String
+ EntradaLog(momento : LocalDateTime, nivel : NivelLog, origen : String, mensaje : String)
+ getMomento() : LocalDateTime
+ getNivel() : NivelLog
+ getOrigen() : String
+ getMensaje() : String
+ formatear() : String     


#### LogListener (interface)
+ onLog(entrada : EntradaLog) : void

#### Logger (singleton)
- static instancia : Logger
- archivo : Path
- escritor : BufferedWriter
- listeners : CopyOnWriteArrayList\<LogListener>
- historial : ArrayDeque\<EntradaLog>        
- nivelMinimo : NivelLog
- lock : ReentrantLock
- Logger()
+ static getInstance() : Logger
+ iniciar(ruta : String) : void              
+ cerrar() : void                            
+ setNivelMinimo(nivel : NivelLog) : void
+ getNivelMinimo() : NivelLog
+ agregarListener(listener : LogListener) : void
+ removerListener(listener : LogListener) : void
+ getHistorial() : List\<EntradaLog>         
+ log(nivel : NivelLog, origen : String, mensaje : String) : void
+ debug(origen : String, mensaje : String) : void
+ info(origen : String, mensaje : String) : void
+ warn(origen : String, mensaje : String) : void
+ error(origen : String, mensaje : String, causa : Throwable) : void
- escribirEnArchivo(entrada : EntradaLog) : void
- notificarListeners(entrada : EntradaLog) : void

// listener es "notificado" y recibe el texto del logger


### ui

#### VentanaServidor (extends JFrame)
- controlador : Controlador
- panelPartidas : PanelPartidas
- panelConsola : PanelConsola
- panelControl : PanelControl
- divisor : JSplitPane
- timerRefresco : javax.swing.Timer
+ VentanaServidor(controlador : Controlador)
+ iniciarRefresco() : void
+ detenerRefresco() : void
- construirUI() : void
- refrescar() : void
- alCerrar() : void

// refrescar() (cada INTERVALO_REFRESCO_UI_MS): pide los resumenes y los pasa a panelPartidas.actualizar().
// alCerrar(): detiene el timer, quita el listener del Logger, servidor.detener() y Logger.cerrar().
// Barra de estado inferior: clientes conectados, partidas activas, seed actual.

#### PanelPartidas (extends JPanel)
- contenedor : JPanel
- tarjetas : Map\<String, TarjetaPartida>
- etiquetaVacio : JLabel
+ PanelPartidas()
+ actualizar(resumenes : List\<ResumenPartida>) : void

#### TarjetaPartida (extends JPanel)
- codigo : String
- etiquetaCodigo : JLabel
- etiquetaEstado : JLabel
- etiquetaDuracion : JLabel
- etiquetaBots : JLabel
- etiquetaCafes : JLabel
- tablaJugadores : JTable
- modeloJugadores : ModeloTablaJugadores
+ TarjetaPartida(codigo : String)
+ actualizar(resumen : ResumenPartida) : void
- colorDeEstado(estado : EstadoPartida) : Color

// ESPERANDO = amarillo, SELECCION = azul, JUGANDO = verde, TERMINADA = gris.
// etiquetaBots muestra "Bots: vivos/total". etiquetaCafes muestra los cafes restantes.
// Columnas de la tabla: nickname, especie, animales vivos, energia, cafes, bots eliminados.
// Si hay ganador, muestra "Ganador: <nickname>". Jugador desconectado se muestra en rojo.

#### ModeloTablaJugadores (extends AbstractTableModel)
- filas : List\<ResumenJugador>
+ setJugadores(jugadores : List\<ResumenJugador>) : void
+ getRowCount() : int
+ getColumnCount() : int
+ getColumnName(col : int) : String
+ getValueAt(fila : int, col : int) : Object

#### PanelConsola (extends JPanel, implements LogListener)
- areaTexto : JTextPane
- scroll : JScrollPane
- autoScroll : boolean
- filtroNivel : NivelLog
- filtroTexto : String
- lineas : int
+ PanelConsola()
+ onLog(entrada : EntradaLog) : void
+ cargarHistorial(entradas : List\<EntradaLog>) : void
+ limpiar() : void
+ setAutoScroll(activo : boolean) : void
+ setFiltroNivel(nivel : NivelLog) : void
+ setFiltroTexto(texto : String) : void
- agregarLinea(entrada : EntradaLog) : void
- recortarSiSobrepasa() : void
- colorDeNivel(nivel : NivelLog) : Color

// Al crearse se registra con Logger.agregarListener(this) y llama cargarHistorial(Logger.getHistorial()) para mostrar lo ocurrido antes de abrir la ventana.
// Colores: DEBUG gris, INFO color normal, WARN naranja, ERROR rojo.

#### PanelControl (extends JPanel)
- campoSeed : JTextField
- checkSeedFija : JCheckBox
- botonAplicarSeed : JButton
- botonSeedAleatoria : JButton
- comboNivel : JComboBox\<NivelLog>
- checkAutoScroll : JCheckBox
- botonLimpiar : JButton
- campoFiltro : JTextField
- botonGuardar : JButton
+ PanelControl(panelConsola : PanelConsola)
- aplicarSeed() : void
- generarSeedAleatoria() : void

// Barra de herramientas sobre la consola.
// aplicarSeed(): valida el long, llama GeneradorAleatorio.setSeed(...) y setSeedFija(checkSeedFija).
// Solo afecta a partidas NUEVAS (las que ya corren conservan su stream); se avisa en el Logger.
// generarSeedAleatoria(): pone una seed nueva en el campo y en el generador.
// comboNivel -> Logger.setNivelMinimo y panelConsola.setFiltroNivel.
// botonGuardar: exporta lo visible en la consola a un archivo elegido con JFileChooser.

### server
#### ClientHandler (implements Runnable)
- socket : Socket
- entrada : BufferedReader
- salida : PrintWriter
- servidor : Servidor
- nombre : String
- activo : boolean
+ ClientHandler(socket : Socket, servidor : Servidor)
+ run() : void
+ getNombre() : String
+ setNombre(nombre : String) : void
+ mandar(mensaje : MensajeServidor) : void
+ ascenderMensaje(mensaje : MensajeJugador) : void
+ cerrar() : void

// Recibe y procesa los mensajes que vienen desde un cliente.

#### ThreadConexiones (implements Runnable) 
- serverSocket : ServerSocket
- servidor : Servidor
- activo : boolean
+ ThreadConexiones(servidor : Servidor, puerto : int)
+ run() : void
+ detener() : void

// Hilo infinito que acepta conexiones. Por cada Socket crea un ClientHandler
// y se lo entrega a Servidor.registrarCliente(). El puerto viene de Config.

#### Servidor
- controlador : Controlador
- clientes : ConcurrentHashMap\<String, ClientHandler>  
- conectados : List\<ClientHandler>                      
- threadConexiones : ThreadConexiones
- executor : ExecutorService
+ Servidor()
+ iniciar() : void
+ detener() : void
+ registrarCliente(handler : ClientHandler) : void
+ asignarNombre(handler : ClientHandler, nombre : String) : boolean
+ removerCliente(handler : ClientHandler) : void
+ mandarACliente(nombre : String, msg : MensajeServidor) : void
+ mandarATodos(msg : MensajeServidor) : void
+ mandarASistema(msg : MensajeJugador) : void
 + getCantidadClientes() : int
 
// Se manda el mensaje segun el nombre que esta asociado al dato, si tiene el NOMBRE_UNIVERSAL, se manda ese mismo mensaje a todos
los clientes.
// conectados son los clientes que no han recibido nombre

#### ServerConfig
- static instancia : ServerConfig
- ip : String
- puerto : int
- nombresOcupados : ConcurrentSkipListSet\<String>    
- ServerConfig()
+ static getInstance() : ServerConfig
+ getIp() : String
+ getPuerto() : int
+ reservarNombre(nombre : String) : boolean
+ liberarNombre(nombre : String) : void

// Sigue un patron singleton

// Un set porque no se repite

### controlador
#### Controlador
- mapa : Mapa
- servidor : Servidor
- partidas : ConcurrentHashMap\<String, Partida>
- partidaDeJugador : ConcurrentHashMap\<String, String>   
- ejecutor : ExecutorService
- ejecutorMensajes : EjecutorMensajes
+ Controlador(servidor : Servidor)
+ ejecutarMensaje(msg : MensajeJugador) : void
+ actualizarEstado(datos : HashMap\<String, DatosEnviar>) : void
+ enviar(nombre : String, msg : MensajeServidor) : void
+ enviarATodos(msg : MensajeServidor) : void
+ crearJuego(nickname : String) : String
+ unirsePartida(codigo : String, nickname : String) : boolean
+ getPartidaDe(nombre : String) : Partida
+ finalizarPartida(codigo : String) : void
+ jugadorDesconectado(nombre : String) : void
+ getResumenesPartidas() : List\<ResumenPartida>
+ getCantidadPartidas() : int


#### EjecutorMensajes 
- manejadores : Map\<TipoMensaje, ManejadorMensaje>
- controlador : Controlador
+ EjecutorMensajes(controlador : Controlador)
+ registrar(manejador : ManejadorMensaje) : void
+ ejecutar(msg : MensajeJugador) : void
- registrarPorDefecto() : void

//Contexto del patron strategy

//Siguen el patron de strategy, unicamente se aplica ejecutar con la clase de contexto, no importa cuantos tipos de mensajes existan.

#### ManejadorMensaje (interface)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void

#### ManejadorCreateMatch (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void
#### ManejadorJoinMatch (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void
#### ManejadorSelectAnimal (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void
#### ManejadorMoveAnimals (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void
#### ManejadorAttack (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void
#### ManejadorViewZone (implements ManejadorMensaje)
+ getTipo() : TipoMensaje
+ ejecutar(msg : MensajeJugador, controlador : Controlador) : void

 ### enviables

//Datos enviados por mensaje con modificaciones para que no de errores

 #### DatosEnviar 
- jugadorA : JugadorEnviar
- jugadorB : JugadorEnviar
- entidadesDisponibles : List\<EntidadEnviar>
- zonaActual : ZonaEnviar
- eventos : List\<EventoEnviar>
+ DatosEnviar(jugadorA : JugadorEnviar, jugadorB : JugadorEnviar, entidadesDisponibles : List\<EntidadEnviar>, zonaActual : ZonaEnviar, eventos : List\<EventoEnviar>)
+ getJugadorA() : JugadorEnviar
+ getJugadorB() : JugadorEnviar
+ getEntidadesDisponibles() : List\<EntidadEnviar>
+ getZonaActual() : ZonaEnviar
+ getEventos() : List\<EventoEnviar>
+ static desde(datos : Datos) : DatosEnviar

// Reemplaza a Datos en MensajeGameStarted y MensajeStateUpdate.
// desde(): jugadorB puede ser null (el oponente aún no está). Las listas se copian a listas nuevas inmutables (List.copyOf).
// El campo se llama "eventos" (no "evento"), igual que el cliente.
// Se debe llamar dentro del lock de Partida (snapshot consistente), ver comentarios finales.

#### JugadorEnviar 
- id : int
- nickname : String
- especie : TipoEspecie
- animales : List\<AnimalEnviar>
- cafesConsumidos : int
- botsEliminados : int
- conectado : boolean
+ JugadorEnviar(id : int, nickname : String, especie : TipoEspecie, animales : List\<AnimalEnviar>, cafesConsumidos : int, botsEliminados : int, conectado : boolean)
+ getId() : int
+ getNickname() : String
+ getEspecie() : TipoEspecie
+ getAnimales() : List\<AnimalEnviar>
+ getCafesConsumidos() : int
+ getBotsEliminados() : int
+ isConectado() : boolean
+ static desde(jugador : Jugador, zonaVisible : Zona) : JugadorEnviar

// id = jugador.getId() (idPack del Pack).
// especie = jugador.getEspecie().getTipo(), o TipoEspecie.NINGUNA si todavía no eligió.
// desde(): solo incluye los animales (vivos o muertos) cuyo getZonaActual() == zonaVisible. Así el rival no filtra animales de zonas que el jugador no ve.
// No se envían: zonaElegida, damage, conteoDeBajas ni el objeto Especie.

#### EntidadEnviar (abstract)
- id : int
- tipo : TipoEntidad
- posicion : PosicionEnviar
+ EntidadEnviar(id : int, tipo : TipoEntidad, posicion : PosicionEnviar)
+ static desde(entidad : Entidad) : EntidadEnviar
+ getId() : int
+ getTipo() : TipoEntidad
+ getPosicion() : PosicionEnviar

// Polimorfismo con las mismas anotaciones que Entidad del cliente:
//   @JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "tipo", visible = true)
//   @JsonSubTypes({ @Type(value = AnimalEnviar.class, name = "ANIMAL"), @Type(value = CafeEnviar.class, name = "CAFE"), @Type(value = BanderaEnviar.class, name = "BANDERA") })
// desde() despacha según el tipo: Animal -> AnimalEnviar.desde(), Cafe -> CafeEnviar.desde(), Bandera -> BanderaEnviar.desde().
// Si la entidad es de un tipo desconocido lanza IllegalArgumentException.
// El campo "tipo" es el discriminador: no se debe renombrar.

#### AnimalEnviar (extends EntidadEnviar)
- idPack : int
- especie : TipoEspecie
- energia : int
+ AnimalEnviar(id : int, posicion : PosicionEnviar, idPack : int, especie : TipoEspecie, energia : int)
+ static desde(animal : Animal) : AnimalEnviar
+ getIdPack() : int
+ getEspecie() : TipoEspecie
+ getEnergia() : int

// tipo = TipoEntidad.ANIMAL (fijo en el constructor).
// idPack = animal.getPack().getId(). especie = animal.getPack().getEspecie().getTipo().
// No se envían: pack, ruta ni zonaActual (ciclos y datos pesados).

#### CafeEnviar (extends EntidadEnviar)
- consumido : boolean
+ CafeEnviar(id : int, posicion : PosicionEnviar, consumido : boolean)
+ static desde(cafe : Cafe) : CafeEnviar
+ isConsumido() : boolean

// tipo = TipoEntidad.CAFE. consumido = !cafe.estaDisponible().

#### BanderaEnviar 
 
- idJugador : int
- color : String
+ BanderaEnviar(id : int, posicion : PosicionEnviar, idJugador : int, color : String)
+ static desde(bandera : Bandera) : BanderaEnviar
+ getIdJugador() : int
+ getColor() : String

// tipo = TipoEntidad.BANDERA. idJugador = bandera.getJugador().getId() (idPack del dueño).
// No se envía el objeto Jugador (ciclo Jugador -> animales -> ...).

#### EventoEnviar 
- tipo : TipoEvento
- origen : PosicionEnviar
- destino : PosicionEnviar
+ EventoEnviar(tipo : TipoEvento, origen : PosicionEnviar, destino : PosicionEnviar)
+ static desde(evento : Evento) : EventoEnviar
+ getTipo() : TipoEvento
+ getOrigen() : PosicionEnviar
+ getDestino() : PosicionEnviar

// Los eventos de Partida.eventosPendientes se copian aquí al construir los datos; después se vacía la cola.

#### ZonaEnviar
- id : int
- nodos : List\<NodoEnviar>
+ ZonaEnviar(id : int, nodos : List\<NodoEnviar>)
+ static desde(zona : Zona) : ZonaEnviar
+ getId() : int
+ getNodos() : List\<NodoEnviar>

// Requiere Zona.getId() en el dominio (campo nuevo, asignado en Mapa.crearZonas()).
// No se envía Zona.mapa.
// La zona es estática: se puede crear una sola vez por zona y reutilizarla (ver comentarios finales).

#### NodoEnviar
- id : int
- posicion : Punto
- idZona : int
+ NodoEnviar(id : int, posicion : Punto, idZona : int)
+ static desde(nodo : Nodo) : NodoEnviar
+ getId() : int
+ getPosicion() : Punto
+ getIdZona() : int

// idZona = nodo.getZona().getId(). Rompe el ciclo Nodo <-> Zona.

#### PosicionEnviar 
- nodo : NodoEnviar
- punto : Punto
+ PosicionEnviar(nodo : NodoEnviar, punto : Punto)
+ static desde(posicion : Posicion) : PosicionEnviar
+ getNodo() : NodoEnviar
+ getPunto() : Punto

// nodo puede ser null si la entidad está en pleno recorrido entre nodos.
// El nodo va como NodoEnviar liviano (id, posicion, idZona), sin la zona ni sus demás nodos.
// punto se copia (Punto es inmutable), así no cambia mientras Jackson lo escribe.
// Se cambia el tipo de Datos a DatosEnviar. En el cliente la clase sigue llamándose Datos: Jackson solo mira los nombres de los campos.


### logica

#### EstadoPartida (enum)
+ ESPERANDO
+ SELECCION
+ JUGANDO
+ TERMINADA

#### Partida (implements Runnable)
- controlador : Controlador
- codigo : String
- estado : EstadoPartida
- jugadores : ArrayList\<Jugador>
- bots : ArrayList\<Bot>
- manadas : ArrayList\<Pack>
- cafes : ArrayList\<Cafe>
- banderas : ArrayList\<Bandera>
- ejecutor : ExecutorService
- eventosPendientes : ConcurrentLinkedQueue\<Evento>
- notificador : Notificador                      
- inicio : long
- ganador : String
+ Partida(controlador : Controlador, codigo : String)
+ run() : void
+ getCodigo() : String
+ getEstado() : EstadoPartida
+ agregarJugador(nickname : String) : boolean
+ crearJuego() : boolean
+ iniciarPartida() : boolean
+ seleccionarEspecie(nombre : String, especie : TipoEspecie) : void
+ moverAnimales(nombre : String, animales : List\<int[]>, idNodoDestino : int) : void
+ mandarAtaque(nombre : String, A : int[], B : int[]) : void
+ verZona(nombre : String, idZona : int) : void
+ registrarEvento(evento : Evento) : void
+ notificar(datos : HashMap\<String, DatosEnviar>) : void
+ finalizar(ganador : Jugador) : boolean
+ getResumen() : ResumenPartida
- asignarZonas() : void
- crearBanderas() : void
- crearAnimales() : void
- crearCafes() : void
- filtrarZona(jugador : Jugador) : Zona
- obtenerDatos(jugador : Jugador) : DatosEnviar
- construirDatos() : HashMap\<String, DatosEnviar>

//eventosPendientes: eventos que sucedieron antes de notificar.
// El cliente al elegir un animal se elige el animal como {idPack, idDelAnimal} y lo mismo para el enemigo por lo 
tanto eso es el int[]
// getResumen() construye un ResumenPartida bajo sincronizacion corta (copia de datos basicos).

#### ResumenPartida
- codigo : String
- estado : EstadoPartida
- jugadores : List\<ResumenJugador>
- botsVivos : int
- botsTotales : int
- cafesRestantes : int
- duracionMs : long
- ganador : String
+ ResumenPartida(codigo : String, estado : EstadoPartida, jugadores : List\<ResumenJugador>, botsVivos : int, botsTotales : int, cafesRestantes : int, duracionMs : long, ganador : String)
+ getCodigo() : String
+ getEstado() : EstadoPartida
+ getJugadores() : List\<ResumenJugador>
+ getBotsVivos() : int
+ getBotsTotales() : int
+ getCafesRestantes() : int
+ getDuracionMs() : long
+ getGanador() : String

#### Notificador
- partida : Partida
+ Notificador(partida : Partida)
+ enviarDatos(datos : HashMap\<String, DatosEnviar>) : void
+ enviarFin(ganador : String) : void

#### ThreadAnimal (Abstract implements Runnable)
- partida : Partida
- ultimoAtaque : long
- activo : boolean
+ run () : void
+ detener() : void
+ getPartida() : Partida
+ decidir(ahora : long) : void 
- moverse(ahora : long) : void
- atacar(ahora : long) : void

#### ThreadJugador (extender ThreadAnimal)
- animal : Jugador
- ataquesPendientes : List\<int[][]>
+ run() : void
+ setObjetivo(Animal objetivo) : void
+ detener() : void
- moverse() : void
- atacar() : void
+ decidir(ahora : long) : void 
+ agregarAtaque(idJugador : int[], idEnemigo : int[]) : void 
- verificarBandera() : boolean



#### ThreadBot (implements Runnable) 
- bot : Bot
+ run() : void
+ detener() : void
- moverse() : void
- atacar() : void
+ decidir(ahora : long) : void 
- buscarSiguienteRuta() : void
- intentarAtacar() : boolean

//Los ataques del Jugador y Bot deben notificar el evento a la
partida.
//El bot cambia de ruta despues de una cantidad determinada
de movimientos

//  Patron strategy:

#### EstrategiaBot (interface)
+ elegirDestino(Bot bot, Partida partida) : Ruta

#### EstrategiaDirecta (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Ruta

// Va directamente hacia un enemigo

#### EstrategiaCercana (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Ruta

// Va a una zona cercana a un enemigo

#### EstrategiaAleatoria (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Ruta

// Va a cualquier nodo

#### SelectorEstrategia
+ elegir() : EstrategiaBot

#### TipoEntidad (enum)
+ ANIMAL
+ CAFE
+ BANDERA

#### Entidad (abstract)
- id : int
- tipo : TipoEntidad
- posicion : Posicion
+ Entidad(id : int, tipo : TipoEntidad, posicion : Posicion) : void
+ getPosicion() : Posicion
+ setPosicion(posicion : Posicion) : void
+ distanciaCon(otra : Entidad) : double
+ getEnviable() : EntidadEnviar

#### Animal (abstract, extends Entidad)
- pack : Pack
- energia : int
- ruta : Ruta
- zonaActual : Zona
+ getPack() : Pack
+ getEnergia() : int
+ setRuta(ruta : Ruta) : void
+ getRuta() : Ruta
+ getZonaActual() : Zona
+ recibirDamage(cantidad : int) : void
+ estaVivo() : boolean
+ moverse() : boolean
+ getEnviable() : AnimalEnviar

#### Pack (abstract)
- id : int
- animales : List\<Animal>
- especie : Especie
- damage : int
- conteoDeBajas : int
+ getId() : int
+ atacar(idEnemigo : int[]) : int
+ getLista() : List\<Animal>
+ getZonasAnimales() : ArrayList\<Zona>
+ getVivos() : int
+ getEnergiaTotal() : int
+ agregarBotEliminado() : void


// si atacar() devuelve -1 significa que no se ejecuto

#### Jugador (extends Pack)
- nickname : String
- zonaElegida : Zona
- cafesConsumidos : int
- botsEliminados : int
- conectado : boolean
+ getNickname() : String
+ elegirZona(zona : Zona) : void
+ sumarCafe() : void
+ sumarBotEliminado() : void
+ isConectado() : boolean
+ setConectado(conectado : boolean) : void

#### Bot (extends Pack)
- estrategia : EstrategiaBot
- rutaActual : Ruta
- movimientosEnRuta : int
+ getEstrategia() : EstrategiaBot
+ cambiarDeEstrategia() : void

#### Cafe (extends Entidad)
- consumido : boolean
+ consumir() : boolean
+ estaDisponible() : boolean
+ getEnviable() : CafeEnviar

#### Bandera (extends Entidad)
- jugador : Jugador
- color : String
+ fueTocadaPor(Animal animal) : boolean
+ getEnviable() : BanderaEnviar

#### TipoEvento (enum)
+ ATAQUE

#### Evento
- tipo : TipoEvento
- origen : Posicion
- destino : Posicion
+ Evento(tipo : TipoEvento, origen : Posicion, destino : Posicion)
+ getTipo() : TipoEvento
+ getOrigen() : Posicion
+ getDestino() : Posicion
+ getEnviable() : EventoEnviar

#### Datos:
- jugadorA : Jugador
- jugadorB : Jugador
- entidadesDisponibles : List\<Entidad>
- zonaActual : Zona
- evento : List\<Eventos> 
+ getJugadorA() : Jugador
+ getJugadorB() : Jugador
+ getEntidadesDisponibles() : List\<Entidad>
+ getZonaActual() : Zona
+ getEventos() : List\<Evento>
+ getEnviable() : DatosEnviar

#### TipoEspecie (enum)
+ JAGUAR
+ YIGUIRRO
+ PEREZOSO
+ DANTA
+ TUCAN
+ VENADO_CO_BLA
+ MONO_CO_BLA
+ NINGUNA

### mapa

#### Posicion
- nodo : Nodo
- punto : Punto
+ getNodo() : Nodo
+ seguirRuta(ruta : Ruta) : boolean
+ setPunto(punto : Punto) : void
+ getPunto() : Punto
+ getEnviable() : PosicionEnviar

// El punto es la posición real en el plano
// Permite seguir la ruta y moverse segun la configuracion

#### Ruta 
- nodos : List\<Nodo>
- nodoActual : int
+ getNodos() : List\<Nodo>
+ getNodoActual() : Nodo
+ avanzar() : boolean
+ haTerminado() : boolean

#### Mapa (singleton)
- zonas : List\<Zona>
- vecinos : Map\<Nodo, List\<Vecinos>>
- Mapa()
- crearZonas() : void
- crearNodos() : void
- crearVecinos() : void
- crearMapaVecinos() : void
- crearMapa() : Mapa
+ getInstance() : Mapa
+ getRuta(nodoA : Nodo, nodoB : Nodo) : Ruta
+ getZonas() : List\<Zona>
+ getVecinos() : Map\<Nodo, List\<Vecinos>>
+ getVecinosDe(Nodo) : List\<Vecinos>
+ getPeso(Nodo, Nodo) : Double

// Primero se crean las zonas, despues se crean los nodos y se insertan en las zonas y finalmente se crean los vecinos, al crear 
los vecinos de un nodo, se agrega ese mismo vecino al nodo A y al B para evitar crear el mismo objeto 2 veces.

// Como el mapa no cambia es mejor que exista un unico mapa para todo el programa.

#### Zona
- nodos : List\<Nodo>
- mapa : Map
+ Zona(Map)
+ getNodos() : List\<Nodo>
+ getMapa() : Map
+ agregarNodo(Nodo) : void
+ contiene(Nodo) : boolean
+ getEnviable() : ZonaEnviar

#### Nodo
- id : int
- posicion : Punto
- zona : Zona
+ Nodo(int, Punto, Zona)
+ getId() : int
+ getPosicion() : Punto
+ getZona() : Zona
+ equals(Object) : boolean
+ hashCode() : int
+ getEnviable() : NodoEnviar

#### Vecinos
- nodoA : Nodo
- nodoB : Nodo
- peso : Double
+ Vecinos(Nodo, Nodo, Double)
+ getNodoA() : Nodo
+ getNodoB() : Nodo
+ getPeso() : Double
+ formaParte(Nodo) : boolean
+ getOtro(Nodo) : Nodo

### mensajes

#### TipoMensaje (enum)
+ PRIMER_MENSAJE
+ INDICAR_NOMBRE
+ CREATE_MATCH
+ JOIN_MATCH
+ SELECT_ANIMAL
+ MOVE_ANIMALS
+ ATTACK
+ VIEW_ZONE
+ MATCH_CREATED
+ MATCH_JOINED
+ ANIMAL_SELECTION
+ GAME_STARTED
+ STATE_UPDATE
+ GAME_OVER
+ ERROR

#### Mensaje (abstract)
- tipo : TipoMensaje
+ Mensaje(tipo : TipoMensaje, nombre : String)
+ getTipo() : TipoMensaje

// Clase base de todos los mensajes.
// Si nombre es Constantes.NOMBRE_UNIVERSAL (@Todos), el servidor lo manda a todos los clientes

#### MensajeJugador (abstract, extends Mensaje)
- nombre : String
+ MensajeJugador(tipo : TipoMensaje, nombre : String)
+ getNombre() : String
+ setNombre(nombre : String) : void
// Agrupa todos los mensajes cliente -> servidor..

#### MensajeServidor (abstract, extends Mensaje)
+ MensajeServidor(tipo : TipoMensaje)

// Agrupa todos los mensajes servidor -> cliente.


//Cliente -> Servidor 

#### MensajeIndicarNombre (extends MensajeJugador)
+ MensajeIndicarNombre(nombre : String)

// tipo = INDICAR_NOMBRE
// Solo usa el nombre heredado.

#### MensajeCreateMatch (extends MensajeJugador)
+ MensajeCreateMatch(nombre : String)

// tipo = CREATE_MATCH
// Sin payload.

#### MensajeJoinMatch (extends MensajeJugador)
- codigo : String
+ MensajeJoinMatch(nombre : String, codigo : String)
+ getCodigo() : String

// tipo = JOIN_MATCH

#### MensajeSelectAnimal (extends MensajeJugador)
- especie : TipoEspecie
+ MensajeSelectAnimal(nombre : String, especie : TipoEspecie)
+ getEspecie() : TipoEspecie

// tipo = SELECT_ANIMAL

#### MensajeMoveAnimals (extends MensajeJugador)
- animales : List\<int[]>
- idNodoDestino : int
+ MensajeMoveAnimals(nombre : String, animales : List\<int[]>, idNodoDestino : int)
+ getAnimales() : List\<int[]>
+ getIdNodoDestino() : int

// tipo = MOVE_ANIMALS
// Cada animal se identifica como int[]{idPack, idAnimal}.
// El servidor calcula la ruta con Mapa.getRuta().

#### MensajeAttack (extends MensajeJugador)
- atacante : int[]
- enemigo : int[]
+ MensajeAttack(nombre : String, atacante : int[], enemigo : int[])
+ getAtacante() : int[]
+ getEnemigo() : int[]

// tipo = ATTACK
// Mismo formato que Partida.mandarAtaque(A, B): int[]{idPack, idAnimal}.

#### MensajeViewZone (extends MensajeJugador)
- idZona : int
+ MensajeViewZone(nombre : String, idZona : int)
+ getIdZona() : int

// tipo = VIEW_ZONE


//Servidor -> Cliente 

#### MensajePrimerMensaje (extends MensajeServidor)
- exito : boolean
- razon : String
+ MensajePrimerMensaje(exito : boolean, razon : String)
+ isExito() : boolean
+ getRazon() : String

// tipo = PRIMER_MENSAJE
// razon indica por qué se rechazó la conexión (null si exito es true).

#### MensajeMatchCreated (extends MensajeServidor)
- codigo : String
+ MensajeMatchCreated(codigo : String)
+ getCodigo() : String

// tipo = MATCH_CREATED

#### MensajeMatchJoined (extends MensajeServidor)
- codigo : String
- jugadores : List\<String>
+ MensajeMatchJoined(¿codigo : String, jugadores : List\<String>)
+ getCodigo() : String
+ getJugadores() : List\<String>

// tipo = MATCH_JOINED

#### MensajeAnimalSelection (extends MensajeServidor)
- especiesDisponibles : List\<TipoEspecie>
- seleccionOponente : TipoEspecie
+ MensajeAnimalSelection(especiesDisponibles : List\<TipoEspecie>), especieE : TipoEspecie) 
+ getEspeciesDisponibles() : List\<TipoEspecie>
+ getSeleccionOponente() : TipoEspecie

// tipo = ANIMAL_SELECTION

#### MensajeGameStarted (extends MensajeServidor)
- datos : DatosEnviar
+ MensajeGameStarted(datos : DatosEnviar)
+ getDatos() : Datos

// tipo = GAME_STARTED
// datos = estado inicial visible para ese jugador.

#### MensajeStateUpdate (extends MensajeServidor)
- datos : DatosEnviar
+ MensajeStateUpdate(datos : DatosEnviar)
+ getDatos() : Datos

// tipo = STATE_UPDATE
// datos = estado actual visible para ese jugador.

#### MensajeGameOver (extends MensajeServidor)
- ganador : String
+ MensajeGameOver(ganador : String)
+ getGanador() : String

// tipo = GAME_OVER

#### MensajeError (extends MensajeServidor)
- descripcion : String
+ MensajeError(descripcion : String)
+ getDescripcion() : String

// tipo = ERROR
// Solo se usa si el cliente puede hacer algo al respecto; las desconexiones se manejan con excepciones del socket.

### Test

#### JsonTest
+ mensajeAJsonCreateMatchGeneraUnaSolaLinea() : void
+ roundTripTodosLosTiposDeMensajeConservanCampos() : void
+ jsonAMensajeTipoDesconocidoLanzaIllegalArgumentException() : void
+ jsonAMensajeCamposExtraLosIgnora() : void
+ roundTripGameStartedConEntidadPolimorficaResuelveAnimalCafeBandera() : void


#### MapaTest
+ getInstanceSiempreLaMismaInstancia() : void
+ getZonasDevuelveTotalZones() : void
+ vecinosSonSimetricos() : void
+ getRutaNodosEnDistintaZonaRutaConexa() : void
+ getRutaEsLaMasCortaVerificadaConDijkstraManual() : void
+ posicionSeguirRutaAvanzaConVelocidadConstante() : void
+ posicionSeguirRutaConDeltaTimeGrandeNoSobrepasaElDestino() : void
- verificarConectividadZonas(int totalZones) : boolean
- comprobarSimetriaVecinos(Nodo nodoA, Nodo nodoB) : boolean
- comprobarVelocidadConstante(Posicion posInicial, Posicion posFinal, double delta) : boolean

#### PartidaTest

// Configuración
+ agregarJugadorTercerJugadorDevuelveFalse() : void
+ seleccionarEspecieMismaEspecieDeLosDosJugadoresSeRechaza() : void
+ iniciarPartidaCreaSietePacksDosJugadoresYCincoBots() : void
+ iniciarPartidaPacksEnSieteZonasDistintas() : void
+ iniciarPartidaDiezCafesColocados() : void

// Combate
+ ataqueRestaDamageDelPackAlObjetivo() : void
+ ataqueEnergiaLlegaACeroAnimalMuere() : void
+ ataqueAntesDelCooldownNoSeEjecutaDevuelveMenosUno() : void
+ ejemploEnunciadoAnimal20EnergiaVsBot4x10MuereEn5sYMataUnBot() : void

// Bonificaciones
+ cafeIncrementaDamageDeTodoElPackYSeConsumeUnaSolaVez() : void
+ botsEliminadosCadaDosSuman1EnergiaSinSuperarMaxPlayerEnergy() : void

// Victoria y visibilidad
+ banderaTocadaPorRivalTerminaPartidaYDeclaraGanador() : void
+ verZonaSinAnimalesEnLaZonaSeRechaza() : void
+ construirDatosNoIncluyeEntidadesDeZonasNoVisibles() : void

#### ControladorTest
+ crearJuegoDevuelveCodigoDeLongitudCorrecta() : void
+ crearJuegoCodigosUnicosEntrePartidasActivas() : void
+ unirsePartidaCodigoInexistenteDevuelveFalse() : void
+ unirsePartidaCodigoEnMinusculasFuncionaCaseInsensitive() : void
+ unirsePartidaPartidaLlenaDevuelveFalse() : void
+ jugadorDesconectadoElOtroJugadorGana() : void
+ finalizarPartidaLaRemueveDelMapa() : void
- validarLongitudYFormatoCodigo(String codigo) : boolean
- comprobarUnicidadCodigos(List<String> codigosActivos) : boolean

## Diagrama
```mermaid
classDiagram
    direction TB

    %% ===================== MAIN =====================
    class Main {
        +main(String[] args)$ void
    }

    %% ===================== UTILIDADES =====================
    class Json {
        +mensajeAJson(Mensaje msg)$ String
        +jsonAMensaje(String json)$ Mensaje
    }
    class Punto {
        -double x
        -double y
        +Punto(double x, double y)
        +getX() double
        +getY() double
    }
    class Constantes {
        <<final>>
        +int LONGITUD_CODIGO$
        +String IP_SERVIDOR$
        +double ENCOUNTER_RADIUS$
        +double ANIMAL_SPEED$
        +long ATTACK_COOLDOWN_MS$
        +int MAX_PLAYER_ENERGY$
        +int PLAYER_INITIAL_ENERGY$
        +int PLAYER_DAMAGE$
        +int BOT_INITIAL_ENERGY$
        +int BOT_DAMAGE$
        +int PLAYER_PACK_SIZE$
        +int BOT_PACK_SIZE$
        +int TOTAL_ZONES$
        +int TOTAL_COFFEE_CUPS$
        +String NOMBRE_UNIVERSAL$
        +int MOVIMIENTOS_MINIMOS_CAMBIAR$
        +long INTERVALO_STATE_UPDATE_MS$
        +String LOG_FILE$
        +long SEED_POR_DEFECTO$
        +int INTERVALO_REFRESCO_UI_MS$
        +int MAX_LINEAS_CONSOLA$
        +int MAX_HISTORIAL_LOG$
        -Constantes()
    }
    class GeneradorAleatorio {
        <<singleton>>
        -GeneradorAleatorio instancia$
        -long seed
        -boolean seedFija
        -Random random
        -ConcurrentHashMap~String, Random~ streams
        -GeneradorAleatorio()
        +getInstance()$ GeneradorAleatorio
        +seedAleatoria() void
        +setSeed(long seed) void
        +getSeed() long
        +setSeedFija(boolean fija) void
        +isSeedFija() boolean
        +reiniciar() void
        +nextInt(int limite) int
        +nextInt(int min, int max) int
        +nextDouble() double
        +nextBoolean() boolean
        +elegir(List~T~ lista) T
        +mezclar(List~T~ lista) void
        +generarCodigo() String
    }
    class NivelLog {
        <<enumeration>>
        DEBUG
        INFO
        WARN
        ERROR
    }
    class EntradaLog {
        -LocalDateTime momento
        -NivelLog nivel
        -String origen
        -String mensaje
        +EntradaLog(LocalDateTime momento, NivelLog nivel, String origen, String mensaje)
        +getMomento() LocalDateTime
        +getNivel() NivelLog
        +getOrigen() String
        +getMensaje() String
        +formatear() String
    }
    class LogListener {
        <<interface>>
        +onLog(EntradaLog entrada) void
    }
    class Logger {
        <<singleton>>
        -Logger instancia$
        -Path archivo
        -BufferedWriter escritor
        -CopyOnWriteArrayList~LogListener~ listeners
        -ArrayDeque~EntradaLog~ historial
        -NivelLog nivelMinimo
        -ReentrantLock lock
        -Logger()
        +getInstance()$ Logger
        +iniciar(String ruta) void
        +cerrar() void
        +setNivelMinimo(NivelLog nivel) void
        +getNivelMinimo() NivelLog
        +agregarListener(LogListener listener) void
        +removerListener(LogListener listener) void
        +getHistorial() List~EntradaLog~
        +log(NivelLog nivel, String origen, String mensaje) void
        +debug(String origen, String mensaje) void
        +info(String origen, String mensaje) void
        +warn(String origen, String mensaje) void
        +error(String origen, String mensaje, Throwable causa) void
        -escribirEnArchivo(EntradaLog entrada) void
        -notificarListeners(EntradaLog entrada) void
    }

    %% ===================== UI =====================
    class VentanaServidor {
        -Controlador controlador
        -PanelPartidas panelPartidas
        -PanelConsola panelConsola
        -PanelControl panelControl
        -JSplitPane divisor
        -Timer timerRefresco
        +VentanaServidor(Controlador controlador)
        +iniciarRefresco() void
        +detenerRefresco() void
        -construirUI() void
        -refrescar() void
        -alCerrar() void
    }
    class PanelPartidas {
        -JPanel contenedor
        -Map~String, TarjetaPartida~ tarjetas
        -JLabel etiquetaVacio
        +PanelPartidas()
        +actualizar(List~ResumenPartida~ resumenes) void
    }
    class TarjetaPartida {
        -String codigo
        -JLabel etiquetaCodigo
        -JLabel etiquetaEstado
        -JLabel etiquetaDuracion
        -JLabel etiquetaBots
        -JLabel etiquetaCafes
        -JTable tablaJugadores
        -ModeloTablaJugadores modeloJugadores
        +TarjetaPartida(String codigo)
        +actualizar(ResumenPartida resumen) void
        -colorDeEstado(EstadoPartida estado) Color
    }
    class ModeloTablaJugadores {
        -List~ResumenJugador~ filas
        +setJugadores(List~ResumenJugador~ jugadores) void
        +getRowCount() int
        +getColumnCount() int
        +getColumnName(int col) String
        +getValueAt(int fila, int col) Object
    }
    class PanelConsola {
        -JTextPane areaTexto
        -JScrollPane scroll
        -boolean autoScroll
        -NivelLog filtroNivel
        -String filtroTexto
        -int lineas
        +PanelConsola()
        +onLog(EntradaLog entrada) void
        +cargarHistorial(List~EntradaLog~ entradas) void
        +limpiar() void
        +setAutoScroll(boolean activo) void
        +setFiltroNivel(NivelLog nivel) void
        +setFiltroTexto(String texto) void
        -agregarLinea(EntradaLog entrada) void
        -recortarSiSobrepasa() void
        -colorDeNivel(NivelLog nivel) Color
    }
    class PanelControl {
        -JTextField campoSeed
        -JCheckBox checkSeedFija
        -JButton botonAplicarSeed
        -JButton botonSeedAleatoria
        -JComboBox~NivelLog~ comboNivel
        -JCheckBox checkAutoScroll
        -JButton botonLimpiar
        -JTextField campoFiltro
        -JButton botonGuardar
        +PanelControl(PanelConsola panelConsola)
        -aplicarSeed() void
        -generarSeedAleatoria() void
    }

    %% ===================== SERVER =====================
    class ClientHandler {
        -Socket socket
        -BufferedReader entrada
        -PrintWriter salida
        -Servidor servidor
        -String nombre
        -boolean activo
        +ClientHandler(Socket socket, Servidor servidor)
        +run() void
        +getNombre() String
        +setNombre(String nombre) void
        +mandar(MensajeServidor mensaje) void
        +ascenderMensaje(MensajeJugador mensaje) void
        +cerrar() void
    }
    class ThreadConexiones {
        -ServerSocket serverSocket
        -Servidor servidor
        -boolean activo
        +ThreadConexiones(Servidor servidor, int puerto)
        +run() void
        +detener() void
    }
    class Servidor {
        -Controlador controlador
        -ConcurrentHashMap~String, ClientHandler~ clientes
        -List~ClientHandler~ conectados
        -ThreadConexiones threadConexiones
        -ExecutorService executor
        +Servidor()
        +iniciar() void
        +detener() void
        +registrarCliente(ClientHandler handler) void
        +asignarNombre(ClientHandler handler, String nombre) boolean
        +removerCliente(ClientHandler handler) void
        +mandarACliente(String nombre, MensajeServidor msg) void
        +mandarATodos(MensajeServidor msg) void
        +mandarASistema(MensajeJugador msg) void
        +getCantidadClientes() int
    }
    class ServerConfig {
        <<singleton>>
        -ServerConfig instancia$
        -String ip
        -int puerto
        -ConcurrentSkipListSet~String~ nombresOcupados
        -ServerConfig()
        +getInstance()$ ServerConfig
        +getIp() String
        +getPuerto() int
        +reservarNombre(String nombre) boolean
        +liberarNombre(String nombre) void
    }

    %% ===================== CONTROLADOR =====================
    class Controlador {
        -Mapa mapa
        -Servidor servidor
        -ConcurrentHashMap~String, Partida~ partidas
        -ConcurrentHashMap~String, String~ partidaDeJugador
        -ExecutorService ejecutor
        -EjecutorMensajes ejecutorMensajes
        +Controlador(Servidor servidor)
        +ejecutarMensaje(MensajeJugador msg) void
        +actualizarEstado(HashMap~String, DatosEnviar~ datos) void
        +enviar(String nombre, MensajeServidor msg) void
        +enviarATodos(MensajeServidor msg) void
        +crearJuego(String nickname) String
        +unirsePartida(String codigo, String nickname) boolean
        +getPartidaDe(String nombre) Partida
        +finalizarPartida(String codigo) void
        +jugadorDesconectado(String nombre) void
        +getResumenesPartidas() List~ResumenPartida~
        +getCantidadPartidas() int
    }
    class EjecutorMensajes {
        -Map~TipoMensaje, ManejadorMensaje~ manejadores
        -Controlador controlador
        +EjecutorMensajes(Controlador controlador)
        +registrar(ManejadorMensaje manejador) void
        +ejecutar(MensajeJugador msg) void
        -registrarPorDefecto() void
    }
    class ManejadorMensaje {
        <<interface>>
        +getTipo() TipoMensaje
        +ejecutar(MensajeJugador msg, Controlador controlador) void
    }
    class ManejadorCreateMatch
    class ManejadorJoinMatch
    class ManejadorSelectAnimal
    class ManejadorMoveAnimals
    class ManejadorAttack
    class ManejadorViewZone

    %% ===================== ENVIABLES =====================
    class DatosEnviar {
        -JugadorEnviar jugadorA
        -JugadorEnviar jugadorB
        -List~EntidadEnviar~ entidadesDisponibles
        -ZonaEnviar zonaActual
        -List~EventoEnviar~ eventos
        +desde(Datos datos)$ DatosEnviar
    }
    class JugadorEnviar {
        -int id
        -String nickname
        -TipoEspecie especie
        -List~AnimalEnviar~ animales
        -int cafesConsumidos
        -int botsEliminados
        -boolean conectado
        +desde(Jugador jugador, Zona zonaVisible)$ JugadorEnviar
    }
    class EntidadEnviar {
        <<abstract>>
        -int id
        -TipoEntidad tipo
        -PosicionEnviar posicion
        +desde(Entidad entidad)$ EntidadEnviar
    }
    class AnimalEnviar {
        -int idPack
        -TipoEspecie especie
        -int energia
        +desde(Animal animal)$ AnimalEnviar
    }
    class CafeEnviar {
        -boolean consumido
        +desde(Cafe cafe)$ CafeEnviar
    }
    class BanderaEnviar {
        -int idJugador
        -String color
        +desde(Bandera bandera)$ BanderaEnviar
    }
    class EventoEnviar {
        -TipoEvento tipo
        -PosicionEnviar origen
        -PosicionEnviar destino
        +desde(Evento evento)$ EventoEnviar
    }
    class ZonaEnviar {
        -int id
        -List~NodoEnviar~ nodos
        +desde(Zona zona)$ ZonaEnviar
    }
    class NodoEnviar {
        -int id
        -Punto posicion
        -int idZona
        +desde(Nodo nodo)$ NodoEnviar
    }
    class PosicionEnviar {
        -NodoEnviar nodo
        -Punto punto
        +desde(Posicion posicion)$ PosicionEnviar
    }

    %% ===================== LOGICA =====================
    class EstadoPartida {
        <<enumeration>>
        ESPERANDO
        SELECCION
        JUGANDO
        TERMINADA
    }
    class Partida {
        -Controlador controlador
        -String codigo
        -EstadoPartida estado
        -ArrayList~Jugador~ jugadores
        -ArrayList~Bot~ bots
        -ArrayList~Pack~ manadas
        -ArrayList~Cafe~ cafes
        -ArrayList~Bandera~ banderas
        -ExecutorService ejecutor
        -ConcurrentLinkedQueue~Evento~ eventosPendientes
        -Notificador notificador
        -long inicio
        -String ganador
        +Partida(Controlador controlador, String codigo)
        +run() void
        +getCodigo() String
        +getEstado() EstadoPartida
        +agregarJugador(String nickname) boolean
        +crearJuego() boolean
        +iniciarPartida() boolean
        +seleccionarEspecie(String nombre, TipoEspecie especie) void
        +moverAnimales(String nombre, List~int[]~ animales, int idNodoDestino) void
        +mandarAtaque(String nombre, int[] A, int[] B) void
        +verZona(String nombre, int idZona) void
        +registrarEvento(Evento evento) void
        +notificar(HashMap~String, DatosEnviar~ datos) void
        +finalizar(Jugador ganador) boolean
        +getResumen() ResumenPartida
        -asignarZonas() void
        -crearBanderas() void
        -crearAnimales() void
        -crearCafes() void
        -filtrarZona(Jugador jugador) Zona
        -obtenerDatos(Jugador jugador) DatosEnviar
        -construirDatos() HashMap~String, DatosEnviar~
    }
    class ResumenPartida {
        -String codigo
        -EstadoPartida estado
        -List~ResumenJugador~ jugadores
        -int botsVivos
        -int botsTotales
        -int cafesRestantes
        -long duracionMs
        -String ganador
    }
    class ResumenJugador
    class Notificador {
        -Partida partida
        +Notificador(Partida partida)
        +enviarDatos(HashMap~String, DatosEnviar~ datos) void
        +enviarFin(String ganador) void
    }
    class ThreadAnimal {
        <<abstract>>
        -Partida partida
        -long ultimoAtaque
        -boolean activo
        +run() void
        +detener() void
        +getPartida() Partida
        +decidir(long ahora) void
        -moverse(long ahora) void
        -atacar(long ahora) void
    }
    class ThreadJugador {
        -Jugador animal
        -List~int[][]~ ataquesPendientes
        +run() void
        +setObjetivo(Animal objetivo) void
        +detener() void
        +decidir(long ahora) void
        +agregarAtaque(int[] idJugador, int[] idEnemigo) void
        -moverse() void
        -atacar() void
        -verificarBandera() boolean
    }
    class ThreadBot {
        -Bot bot
        +run() void
        +detener() void
        +decidir(long ahora) void
        -moverse() void
        -atacar() void
        -buscarSiguienteRuta() void
        -intentarAtacar() boolean
    }
    class EstrategiaBot {
        <<interface>>
        +elegirDestino(Bot bot, Partida partida) Ruta
    }
    class EstrategiaDirecta
    class EstrategiaCercana
    class EstrategiaAleatoria
    class SelectorEstrategia {
        +elegir() EstrategiaBot
    }
    class TipoEntidad {
        <<enumeration>>
        ANIMAL
        CAFE
        BANDERA
    }
    class Entidad {
        <<abstract>>
        -int id
        -TipoEntidad tipo
        -Posicion posicion
        +Entidad(int id, TipoEntidad tipo, Posicion posicion)
        +getPosicion() Posicion
        +setPosicion(Posicion posicion) void
        +distanciaCon(Entidad otra) double
        +getEnviable() EntidadEnviar
    }
    class Animal {
        <<abstract>>
        -Pack pack
        -int energia
        -Ruta ruta
        -Zona zonaActual
        +getPack() Pack
        +getEnergia() int
        +setRuta(Ruta ruta) void
        +getRuta() Ruta
        +getZonaActual() Zona
        +recibirDamage(int cantidad) void
        +estaVivo() boolean
        +moverse() boolean
        +getEnviable() AnimalEnviar
    }
    class Pack {
        <<abstract>>
        -int id
        -List~Animal~ animales
        -Especie especie
        -int damage
        -int conteoDeBajas
        +getId() int
        +atacar(int[] idEnemigo) int
        +getLista() List~Animal~
        +getZonasAnimales() ArrayList~Zona~
        +getVivos() int
        +getEnergiaTotal() int
        +agregarBotEliminado() void
    }
    class Jugador {
        -String nickname
        -Zona zonaElegida
        -int cafesConsumidos
        -int botsEliminados
        -boolean conectado
        +getNickname() String
        +elegirZona(Zona zona) void
        +sumarCafe() void
        +sumarBotEliminado() void
        +isConectado() boolean
        +setConectado(boolean conectado) void
    }
    class Bot {
        -EstrategiaBot estrategia
        -Ruta rutaActual
        -int movimientosEnRuta
        +getEstrategia() EstrategiaBot
        +cambiarDeEstrategia() void
    }
    class Cafe {
        -boolean consumido
        +consumir() boolean
        +estaDisponible() boolean
        +getEnviable() CafeEnviar
    }
    class Bandera {
        -Jugador jugador
        -String color
        +fueTocadaPor(Animal animal) boolean
        +getEnviable() BanderaEnviar
    }
    class TipoEvento {
        <<enumeration>>
        ATAQUE
    }
    class Evento {
        -TipoEvento tipo
        -Posicion origen
        -Posicion destino
        +Evento(TipoEvento tipo, Posicion origen, Posicion destino)
        +getEnviable() EventoEnviar
    }
    class Datos {
        -Jugador jugadorA
        -Jugador jugadorB
        -List~Entidad~ entidadesDisponibles
        -Zona zonaActual
        -List~Evento~ eventos
        +getEnviable() DatosEnviar
    }
    class TipoEspecie {
        <<enumeration>>
        JAGUAR
        YIGUIRRO
        PEREZOSO
        DANTA
        TUCAN
        VENADO_CO_BLA
        MONO_CO_BLA
        NINGUNA
    }
    class Especie

    %% ===================== MAPA =====================
    class Posicion {
        -Nodo nodo
        -Punto punto
        +getNodo() Nodo
        +seguirRuta(Ruta ruta) boolean
        +setPunto(Punto punto) void
        +getPunto() Punto
        +getEnviable() PosicionEnviar
    }
    class Ruta {
        -List~Nodo~ nodos
        -int nodoActual
        +getNodos() List~Nodo~
        +getNodoActual() Nodo
        +avanzar() boolean
        +haTerminado() boolean
    }
    class Mapa {
        <<singleton>>
        -List~Zona~ zonas
        -Map~Nodo, List~Vecinos~~ vecinos
        -Mapa()
        -crearZonas() void
        -crearNodos() void
        -crearVecinos() void
        -crearMapaVecinos() void
        -crearMapa() Mapa
        +getInstance()$ Mapa
        +getRuta(Nodo nodoA, Nodo nodoB) Ruta
        +getZonas() List~Zona~
        +getVecinos() Map
        +getVecinosDe(Nodo nodo) List~Vecinos~
        +getPeso(Nodo a, Nodo b) Double
    }
    class Zona {
        -List~Nodo~ nodos
        -Mapa mapa
        +Zona(Mapa mapa)
        +getNodos() List~Nodo~
        +getMapa() Mapa
        +agregarNodo(Nodo nodo) void
        +contiene(Nodo nodo) boolean
        +getEnviable() ZonaEnviar
    }
    class Nodo {
        -int id
        -Punto posicion
        -Zona zona
        +Nodo(int id, Punto posicion, Zona zona)
        +getId() int
        +getPosicion() Punto
        +getZona() Zona
        +equals(Object o) boolean
        +hashCode() int
        +getEnviable() NodoEnviar
    }
    class Vecinos {
        -Nodo nodoA
        -Nodo nodoB
        -Double peso
        +Vecinos(Nodo a, Nodo b, Double peso)
        +getNodoA() Nodo
        +getNodoB() Nodo
        +getPeso() Double
        +formaParte(Nodo nodo) boolean
        +getOtro(Nodo nodo) Nodo
    }

    %% ===================== MENSAJES =====================
    class TipoMensaje {
        <<enumeration>>
        PRIMER_MENSAJE
        INDICAR_NOMBRE
        CREATE_MATCH
        JOIN_MATCH
        SELECT_ANIMAL
        MOVE_ANIMALS
        ATTACK
        VIEW_ZONE
        MATCH_CREATED
        MATCH_JOINED
        ANIMAL_SELECTION
        GAME_STARTED
        STATE_UPDATE
        GAME_OVER
        ERROR
    }
    class Mensaje {
        <<abstract>>
        -TipoMensaje tipo
        +getTipo() TipoMensaje
    }
    class MensajeJugador {
        <<abstract>>
        -String nombre
        +getNombre() String
        +setNombre(String nombre) void
    }
    class MensajeServidor {
        <<abstract>>
    }
    class MensajeIndicarNombre
    class MensajeCreateMatch
    class MensajeJoinMatch {
        -String codigo
    }
    class MensajeSelectAnimal {
        -TipoEspecie especie
    }
    class MensajeMoveAnimals {
        -List~int[]~ animales
        -int idNodoDestino
    }
    class MensajeAttack {
        -int[] atacante
        -int[] enemigo
    }
    class MensajeViewZone {
        -int idZona
    }
    class MensajePrimerMensaje {
        -boolean exito
        -String razon
    }
    class MensajeMatchCreated {
        -String codigo
    }
    class MensajeMatchJoined {
        -String codigo
        -List~String~ jugadores
    }
    class MensajeAnimalSelection {
        -List~TipoEspecie~ especiesDisponibles
        -TipoEspecie seleccionOponente
    }
    class MensajeGameStarted {
        -DatosEnviar datos
    }
    class MensajeStateUpdate {
        -DatosEnviar datos
    }
    class MensajeGameOver {
        -String ganador
    }
    class MensajeError {
        -String descripcion
    }

    %% ===================== RELACIONES =====================
    %% Main / arranque
    Main ..> Logger
    Main ..> GeneradorAleatorio
    Main ..> Servidor
    Main ..> VentanaServidor

    %% Log
    Logger o-- LogListener
    Logger o-- EntradaLog
    EntradaLog --> NivelLog
    Logger --> NivelLog
    PanelConsola ..|> LogListener

    %% UI
    VentanaServidor *-- PanelPartidas
    VentanaServidor *-- PanelConsola
    VentanaServidor *-- PanelControl
    VentanaServidor --> Controlador
    PanelPartidas *-- TarjetaPartida
    TarjetaPartida *-- ModeloTablaJugadores
    ModeloTablaJugadores o-- ResumenJugador
    TarjetaPartida ..> ResumenPartida
    PanelControl --> PanelConsola
    PanelControl ..> GeneradorAleatorio

    %% Server
    Servidor *-- ThreadConexiones
    Servidor o-- ClientHandler
    Servidor --> Controlador
    ClientHandler --> Servidor
    ThreadConexiones --> Servidor
    Servidor ..> ServerConfig

    %% Controlador / Strategy de mensajes
    Controlador --> Servidor
    Controlador --> Mapa
    Controlador o-- Partida
    Controlador *-- EjecutorMensajes
    EjecutorMensajes o-- ManejadorMensaje
    ManejadorCreateMatch ..|> ManejadorMensaje
    ManejadorJoinMatch ..|> ManejadorMensaje
    ManejadorSelectAnimal ..|> ManejadorMensaje
    ManejadorMoveAnimals ..|> ManejadorMensaje
    ManejadorAttack ..|> ManejadorMensaje
    ManejadorViewZone ..|> ManejadorMensaje

    %% Enviables
    DatosEnviar *-- JugadorEnviar
    DatosEnviar *-- EntidadEnviar
    DatosEnviar *-- ZonaEnviar
    DatosEnviar *-- EventoEnviar
    JugadorEnviar *-- AnimalEnviar
    AnimalEnviar --|> EntidadEnviar
    CafeEnviar --|> EntidadEnviar
    BanderaEnviar --|> EntidadEnviar
    EntidadEnviar --> PosicionEnviar
    EventoEnviar --> PosicionEnviar
    PosicionEnviar --> NodoEnviar
    ZonaEnviar *-- NodoEnviar
    DatosEnviar ..> Datos
    Entidad ..> EntidadEnviar

    %% Logica
    Partida --> Controlador
    Partida --> EstadoPartida
    Partida o-- Jugador
    Partida o-- Bot
    Partida o-- Pack
    Partida o-- Cafe
    Partida o-- Bandera
    Partida o-- Evento
    Partida *-- Notificador
    Partida ..> ResumenPartida
    ResumenPartida o-- ResumenJugador
    Notificador --> Partida
    Jugador --|> Pack
    Bot --|> Pack
    Pack *-- Animal
    Pack --> Especie
    Animal --|> Entidad
    Cafe --|> Entidad
    Bandera --|> Entidad
    Animal --> Pack
    Animal --> Ruta
    Animal --> Zona
    Bandera --> Jugador
    Entidad --> TipoEntidad
    Entidad --> Posicion
    Evento --> TipoEvento
    Evento --> Posicion
    Datos o-- Jugador
    Datos o-- Entidad
    Datos o-- Evento
    Datos --> Zona
    Especie --> TipoEspecie
    Jugador --> TipoEspecie

    %% Threads
    ThreadJugador --|> ThreadAnimal
    ThreadBot ..|> ThreadAnimal
    ThreadAnimal --> Partida
    ThreadJugador --> Jugador
    ThreadBot --> Bot

    %% Strategy de bots
    Bot --> EstrategiaBot
    EstrategiaDirecta ..|> EstrategiaBot
    EstrategiaCercana ..|> EstrategiaBot
    EstrategiaAleatoria ..|> EstrategiaBot
    SelectorEstrategia ..> EstrategiaBot

    %% Mapa
    Mapa *-- Zona
    Mapa o-- Vecinos
    Zona *-- Nodo
    Zona --> Mapa
    Nodo --> Zona
    Nodo --> Punto
    Vecinos --> Nodo
    Posicion --> Nodo
    Posicion --> Punto
    Ruta o-- Nodo

    %% Mensajes
    Mensaje --> TipoMensaje
    MensajeJugador --|> Mensaje
    MensajeServidor --|> Mensaje
    MensajeIndicarNombre --|> MensajeJugador
    MensajeCreateMatch --|> MensajeJugador
    MensajeJoinMatch --|> MensajeJugador
    MensajeSelectAnimal --|> MensajeJugador
    MensajeMoveAnimals --|> MensajeJugador
    MensajeAttack --|> MensajeJugador
    MensajeViewZone --|> MensajeJugador
    MensajePrimerMensaje --|> MensajeServidor
    MensajeMatchCreated --|> MensajeServidor
    MensajeMatchJoined --|> MensajeServidor
    MensajeAnimalSelection --|> MensajeServidor
    MensajeGameStarted --|> MensajeServidor
    MensajeStateUpdate --|> MensajeServidor
    MensajeGameOver --|> MensajeServidor
    MensajeError --|> MensajeServidor
    MensajeGameStarted --> DatosEnviar
    MensajeStateUpdate --> DatosEnviar
    MensajeSelectAnimal --> TipoEspecie
    Json ..> Mensaje
    ClientHandler ..> Mensaje
```

## Spec Cliente

 

 
```
src/
|-- main/
    |-- Main.java
|-- client/
    |-- constantes/
        |-- Constants.java
        |-- UIConstants.java
        |-- NetworkConstants.java
    |-- red/
        |-- ServerConnection.java
        |-- MessageDispatcher.java
    |-- mensajes/
        |-- TipoMensaje.java
        |-- Mensaje.java
        |-- MensajeJugador.java
        |-- MensajeServidor.java
        |-- MensajeIndicarNombre.java
        |-- MensajeCreateMatch.java
        |-- MensajeJoinMatch.java
        |-- MensajeSelectAnimal.java
        |-- MensajeMoveAnimals.java
        |-- MensajeAttack.java
        |-- MensajeViewZone.java
        |-- MensajePrimerMensaje.java
        |-- MensajeMatchCreated.java
        |-- MensajeMatchJoined.java
        |-- MensajeAnimalSelection.java
        |-- MensajeGameStarted.java
        |-- MensajeStateUpdate.java
        |-- MensajeGameOver.java
        |-- MensajeError.java
    |-- modelo/
        |-- GameModel.java
        |-- AccionDeMensaje.java
        |-- AccionMensaje.java
        |-- AccionMatchCreated.java
        |-- AccionMatchJoined.java
        |-- AccionAnimalSelection.java
        |-- AccionGameStarted.java
        |-- AccionGameOver.java
        |-- AccionError.java
        |-- DatosEnviar.java
        |-- Jugador.java
        |-- Entidad.java
        |-- TipoEntidad.java
        |-- Animal.java
        |-- Cafe.java
        |-- Bandera.java
        |-- Evento.java
        |-- TipoEvento.java
        |-- Zona.java
        |-- Nodo.java
        |-- Posicion.java
        |-- Punto.java
        |-- TipoEspecie.java
    |-- controlador/
        |-- SelectionManager.java
        |-- GameController.java
    |-- vista/
        |-- MainWindow.java
        |-- GamePanel.java
        |-- HUDPanel.java
    |-- util/
        |-- Observer.java
        |-- Observable.java
        |-- ImageCache.java
        |-- Json.java
|-- test/
    |-- JsonTest.java
    |-- MessageDispatcherTest.java
    |-- GameModelTest.java
    |-- SelectionManagerTest.java
```
 
### main
#### Main
+ main(args : String[]) : static void

### constantes

#### Constants (Clase final)
+ SERVER_IP : static final String
+ SERVER_PORT : static final int
+ LONGITUD_CODIGO : static final int
+ NOMBRE_UNIVERSAL : static final String
+ MAX_PLAYER_ENERGY : static final int
+ PLAYER_PACK_SIZE : static final int
+ TOTAL_ZONES : static final int
+ TOTAL_COFFEE_CUPS : static final int
+ ENCOUNTER_RADIUS : static final double
- Constants()

// Parámetros de conexión y copia de las constantes del servidor que el cliente necesita para dibujar y validar.
// SERVER_IP = "localhost". SERVER_PORT debe ser igual a ServerConfig.getPuerto() del servidor.
// NOMBRE_UNIVERSAL = "@Todos": el cliente no puede usarlo como nickname.
// LONGITUD_CODIGO = 4. Los valores numéricos deben coincidir con Constantes del servidor.

#### UIConstants (Clase final)
+ WINDOW_WIDTH : static final int
+ WINDOW_HEIGHT : static final int
+ FPS : static final int
+ ANIMAL_SIZE_PX : static final int
+ HIT_RADIUS_PX : static final int
+ MARGEN_MAPA_PX : static final int
+ DURACION_EVENTO_MS : static final long
- UIConstants()

// Dimensiones de la ventana y valores de renderizado. FPS = tasa de refresco de GamePanel.
// HIT_RADIUS_PX: distancia máxima del clic a un animal o nodo para considerarlo "clicado".
// DURACION_EVENTO_MS: cuánto tiempo se dibuja la línea de un evento ATAQUE.

#### NetworkConstants (Clase final)
+ DEFAULT_PORT : static final int
+ DEFAULT_HOST : static final String
- NetworkConstants()

// Valores predeterminados de la caja de diálogo de conexión. DEFAULT_PORT = Constants.SERVER_PORT y DEFAULT_HOST = Constants.SERVER_IP.

### red

#### ServerConnection (implements Runnable)
- socket : Socket
- entrada : BufferedReader
- salida : PrintWriter
- modelo : GameModel 
- activo : boolean
+ ServerConnection(host : String, puerto : int, dispatcher : MessageDispatcher)
+ run() : void
+ enviar (msg : Mensaje) : void
+ recibirAscender (msg : Mensaje) : void
+ isActivo() : boolean
+ desconectar() : void

### modelo


#### AccionDeMensaje
+ static obtenerAccion(msg : Mensaje) : AccionMensaje

//Strategy
#### AccionMensaje 
- mensaje : Mensaje
+ ejecutar(model : GameModel) : void

#### AccionMatchCreated (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### AccionMatchJoined (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### AccionAnimalSelection (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### AccionGameStarted (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### AccionGameOver (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### AccionError (extends AccionMensaje)
+ ejecutar(model : GameModel) : void

#### GameModel (extends Observable)
- nickname : String
- codigoPartida : String
- nombreConfirmado : boolean
- jugadoresEnPartida : List\<String>
- especiesDisponibles : List\<TipoEspecie>
- seleccionOponente : TipoEspecie
- zonaActualVisible : int
- datos : DatosEnviar
- animalesLocales : List\<Animal>
- animalesConocidos : Map\<String, Animal>
- listaObservers : List\<Observer>
- ganador : String
- ultimoError : String
+ GameModel()
+ agregarObserver (o : Observer) : void
+ setNickname(nickname : String) : void
+ getNickname() : String
+ getCodigoPartida() : String
+ isNombreConfirmado() : boolean
+ getJugadoresEnPartida() : List\<String>
+ getEspeciesDisponibles() : List\<TipoEspecie>
+ getSeleccionOponente() : TipoEspecie
+ getDatos() : DatosEnviar
+ getJugadorLocal() : Jugador
+ getOponente() : Jugador
+ getAnimalesLocales() : List\<Animal>
+ getEnergiaConocida(idPack : int) : int
+ getVivosConocidos(idPack : int) : int
+ getGanador() : String
+ getUltimoError() : String
+ getZonaActualVisible() : int
+ setZonaActualVisible(zonaId : int) : void
+ actualizarEstado(datos : DatosEnviar) : void
+ realizarAccion (accion : AccionMensaje) : void
+ onDesconexion() : void
+ reiniciarPartida() : void
- notificar(tipo : TipoMensaje) : void
- recordarAnimales(datos : DatosEnviar) : void

// Estado del juego en el cliente
// El argumento de notifyObservers es el TipoMensaje que originó el cambio, así las vistas saben qué cambió.
// reiniciarPartida() se ejecuta despues de que la partida termine

#### DatosEnviar
- jugadorA : Jugador
- jugadorB : Jugador
- entidadesDisponibles : List\<Entidad>
- zonaActual : Zona
- eventos : List\<Evento>
- DatosEnviar()
+ getJugadorA() : Jugador
+ getJugadorB() : Jugador
+ getEntidadesDisponibles() : List\<Entidad>
+ getZonaActual() : Zona
+ getEventos() : List\<Evento>
+ getAnimalesVisibles() : List\<Animal>

#### Jugador
- id : int
- nickname : String
- especie : TipoEspecie
- animales : List\<Animal>
- cafesConsumidos : int
- botsEliminados : int
- conectado : boolean
- Jugador()
+ getId() : int
+ getNickname() : String
+ getEspecie() : TipoEspecie
+ getAnimales() : List\<Animal>
+ getCafesConsumidos() : int
+ getBotsEliminados() : int
+ isConectado() : boolean
+ getVivos() : int
+ getEnergiaTotal() : int

#### TipoEntidad (enum)
+ ANIMAL
+ CAFE
+ BANDERA

#### Entidad (abstract)
- id : int
- tipo : TipoEntidad
- posicion : Posicion
+ Entidad()
+ getId() : int
+ getTipo() : TipoEntidad
+ getPosicion() : Posicion


#### Animal (extends Entidad)
- idPack : int
- especie : TipoEspecie
- energia : int
+ Animal()
+ getIdPack() : int
+ getEspecie() : TipoEspecie
+ getEnergia() : int
+ estaVivo() : boolean
+ getX() : double
+ getY() : double
+ getIdCompuesto() : int[]

#### Cafe (extends Entidad)
- consumido : boolean
+ Cafe()
+ isConsumido() : boolean
+ estaDisponible() : boolean

#### Bandera (extends Entidad)
- idJugador : int
- color : String
+ Bandera()
+ getIdJugador() : int
+ getColor() : String

#### TipoEvento (enum)
+ ATAQUE

#### Evento
- tipo : TipoEvento
- origen : Posicion
- destino : Posicion
+ Evento()
+ getTipo() : TipoEvento
+ getOrigen() : Posicion
+ getDestino() : Posicion

#### Zona
- id : int
- nodos : List\<Nodo>
+  Zona()
+ getId() : int
+ getNodos() : List\<Nodo>
+ getNodo(idNodo : int) : Nodo

#### Nodo
- id : int
- posicion : Punto
- idZona : int
+ Nodo()
+ getId() : int
+ getPosicion() : Punto
+ getIdZona() : int

#### Posicion
- nodo : Nodo
- punto : Punto
+ Posicion()
+ getNodo() : Nodo
+ getPunto() : Punto

#### Punto
- x : double
- y : double
+ Punto()
+ Punto(x : double, y : double) : Punto
+ getX() : double
+ getY() : double

#### TipoEspecie (enum)
+ JAGUAR
+ YIGUIRRO
+ PEREZOSO
+ DANTA
+ TUCAN
+ VENADO_CO_BLA
+ MONO_CO_BLA
+ NINGUNA

### controlador

#### SelectionManager
- animalesSeleccionados : List\<Animal>
+ SelectionManager()
+ seleccionar(animal : Animal) : void
+ deseleccionar(animal : Animal) : void
+ deseleccionarTodo() : void
+ estaSeleccionado(animal : Animal) : boolean
+ getSeleccionados() : List\<Animal>
+ getIdsSeleccionados() : List\<int[]>
+ sincronizar(vigentes : List\<Animal>) : void

// Controla la selección de animales por clic del usuario.
// getIdsSeleccionados() devuelve los int[]{idPack, idAnimal} listos para MensajeMoveAnimals.
// Solo se pueden seleccionar animales vivos del jugador local.

#### GameController
- model : GameModel
- dispatcher : MessageDispatcher
- conexion : ServerConnection
- hiloConexion : Thread
- seleccion : SelectionManager
+ GameController(model : GameModel, dispatcher : MessageDispatcher)
+ conectar(host : String, puerto : int, nickname : String) : boolean
+ desconectar() : void
+ crearPartida() : void
+ unirsePartida(codigo : String) : void
+ seleccionarEspecie(especie : TipoEspecie) : void
+ moverSeleccion(idNodoDestino : int) : void
+ atacar(enemigo : Animal) : void
+ verZona(idZona : int) : void
+ getSeleccion() : SelectionManager
+ getModel() : GameModel
- enviar(msg : MensajeJugador) : void
- nombreValido(nickname : String) : boolean
- codigoValido(codigo : String) : boolean

### vista

#### MainWindow (extends JFrame, implements Observer)
- controlador : GameController
- model : GameModel
- gamePanel : GamePanel
- hudPanel : HUDPanel
- layout : CardLayout
- contenedor : JPanel
+ MainWindow(controlador : GameController)
+ mostrarPantalla(panelName : String) : void
+ update(tipo : TipoMensaje) : void
+ static main(args : String[]) : void
- pedirConexion() : void
- mostrarMenuPrincipal() : void
- mostrarSeleccionEspecie() : void
- mostrarMensaje(texto : String) : void

#### GamePanel (extends JPanel, implements MouseListener)
- model : GameModel
- controlador : GameController
- timer : javax.swing.Timer
- limites : Rectangle2D.Double
- eventosActivos : Map\<Evento, Long>
- ultimoDatos : Datos
+ GamePanel(model : GameModel, controlador : GameController)
+ paintComponent(g : Graphics) : void
+ mousePressed(e : MouseEvent) : void
- calcularLimites() : void
- registrarEventosNuevos() : void
- mundoAPantalla(p : Punto) : Point2D
- pantallaAMundo(x : int, y : int) : Punto
- animalEn(x : int, y : int) : Animal
- nodoEn(x : int, y : int) : Nodo
- dibujarZona(g : Graphics2D) : void
- dibujarEntidades(g : Graphics2D) : void
- dibujarEventos(g : Graphics2D) : void

#### HUDPanel (extends JPanel, implements Observer)
- model : GameModel
- controlador : GameController
- barraVidaLocal : JProgressBar
- barraVidaOponente : JProgressBar
- etiquetaCafes : JLabel
- etiquetaCodigo : JLabel
- etiquetaZona : JLabel
- botonesZona : List\<JButton>
+ HUDPanel(model : GameModel, controlador : GameController)
+ update(tipo : TipoMensaje) : void
- construirUI() : void
- refrescar() : void

// Muestra estadísticas: barra de energía local (máximo Constants.MAX_PLAYER_ENERGY * Constants.PLAYER_PACK_SIZE) y del oponente,
// tazas de café (cafesConsumidos / Constants.TOTAL_COFFEE_CUPS), código de la partida y zona visible.
// botonesZona: uno por zona (Constants.TOTAL_ZONES). Al pulsar uno se llama controlador.verZona(idZona).


### util

#### Observer
+ update(tipo : TipoMensaje) : void
#### Observable
+ agregarObserver (o : Observer) : void
+ notificar (tipo : TipoMensaje) : void

#### ImageCache (Patrón Singleton)
- instancia : static ImageCache
- imagenes : Map\<String, BufferedImage>
- ImageCache()
+ static getInstance() : ImageCache
+ precargar() : void
+ getImagen(key : String) : BufferedImage

// Caché global de imágenes y texturas para no leer disco repetidamente.

#### Json
+ mensajeAJson(msg : Mensaje) : Static String
+ jsonAMensaje(json : String) : Static Mensaje

### mensajes

#### TipoMensaje (enum)
+ PRIMER_MENSAJE
+ INDICAR_NOMBRE
+ CREATE_MATCH
+ JOIN_MATCH
+ SELECT_ANIMAL
+ MOVE_ANIMALS
+ ATTACK
+ VIEW_ZONE
+ MATCH_CREATED
+ MATCH_JOINED
+ ANIMAL_SELECTION
+ GAME_STARTED
+ STATE_UPDATE
+ GAME_OVER
+ ERROR

#### Mensaje (abstract)
- tipo : TipoMensaje
+ Mensaje(tipo : TipoMensaje, nombre : String)
+ getTipo() : TipoMensaje

// Clase base de todos los mensajes.

#### MensajeJugador (abstract, extends Mensaje)
- nombre : String
+ MensajeJugador(tipo : TipoMensaje, nombre : String)
+ getNombre() : String
+ setNombre(nombre : String) : void
// Agrupa todos los mensajes cliente -> servidor..

#### MensajeServidor (abstract, extends Mensaje)
+ MensajeServidor(tipo : TipoMensaje)

// Agrupa todos los mensajes servidor -> cliente.


//Cliente -> Servidor 

#### MensajeIndicarNombre (extends MensajeJugador)
+ MensajeIndicarNombre(nombre : String)

// tipo = INDICAR_NOMBRE
// Solo usa el nombre heredado.

#### MensajeCreateMatch (extends MensajeJugador)
+ MensajeCreateMatch(nombre : String)

// tipo = CREATE_MATCH
// Sin payload.

#### MensajeJoinMatch (extends MensajeJugador)
- codigo : String
+ MensajeJoinMatch(nombre : String, codigo : String)
+ getCodigo() : String

// tipo = JOIN_MATCH

#### MensajeSelectAnimal (extends MensajeJugador)
- especie : TipoEspecie
+ MensajeSelectAnimal(nombre : String, especie : TipoEspecie)
+ getEspecie() : TipoEspecie

// tipo = SELECT_ANIMAL

#### MensajeMoveAnimals (extends MensajeJugador)
- animales : List\<int[]>
- idNodoDestino : int
+ MensajeMoveAnimals(nombre : String, animales : List\<int[]>, idNodoDestino : int)
+ getAnimales() : List\<int[]>
+ getIdNodoDestino() : int

// tipo = MOVE_ANIMALS
// Cada animal se identifica como int[]{idPack, idAnimal}.
// El servidor calcula la ruta con Mapa.getRuta().

#### MensajeAttack (extends MensajeJugador)
- atacante : int[]
- enemigo : int[]
+ MensajeAttack(nombre : String, atacante : int[], enemigo : int[])
+ getAtacante() : int[]
+ getEnemigo() : int[]

// tipo = ATTACK
// Mismo formato que Partida.mandarAtaque(A, B): int[]{idPack, idAnimal}.

#### MensajeViewZone (extends MensajeJugador)
- idZona : int
+ MensajeViewZone(nombre : String, idZona : int)
+ getIdZona() : int

// tipo = VIEW_ZONE


//Servidor -> Cliente 

#### MensajePrimerMensaje (extends MensajeServidor)
- exito : boolean
- razon : String
+ MensajePrimerMensaje(exito : boolean, razon : String)
+ isExito() : boolean
+ getRazon() : String

// tipo = PRIMER_MENSAJE
// razon indica por qué se rechazó la conexión (null si exito es true).

#### MensajeMatchCreated (extends MensajeServidor)
- codigo : String
+ MensajeMatchCreated(codigo : String)
+ getCodigo() : String

// tipo = MATCH_CREATED

#### MensajeMatchJoined (extends MensajeServidor)
- codigo : String
- jugadores : List\<String>
+ MensajeMatchJoined(¿codigo : String, jugadores : List\<String>)
+ getCodigo() : String
+ getJugadores() : List\<String>

// tipo = MATCH_JOINED

#### MensajeAnimalSelection (extends MensajeServidor)
- especiesDisponibles : List\<TipoEspecie>
- seleccionOponente : TipoEspecie
+ MensajeAnimalSelection(especiesDisponibles : List\<TipoEspecie>), especieE : TipoEspecie) 
+ getEspeciesDisponibles() : List\<TipoEspecie>
+ getSeleccionOponente() : TipoEspecie

// tipo = ANIMAL_SELECTION

#### MensajeGameStarted (extends MensajeServidor)
- datos : DatosEnviar
+ MensajeGameStarted(datos : DatosEnviar)
+ getDatos() : Datos

// tipo = GAME_STARTED
// datos = estado inicial visible para ese jugador.

#### MensajeStateUpdate (extends MensajeServidor)
- datos : DatosEnviar
+ MensajeStateUpdate(datos : DatosEnviar)
+ getDatos() : Datos

// tipo = STATE_UPDATE
// datos = estado actual visible para ese jugador.

#### MensajeGameOver (extends MensajeServidor)
- ganador : String
+ MensajeGameOver(ganador : String)
+ getGanador() : String

// tipo = GAME_OVER

#### MensajeError (extends MensajeServidor)
- descripcion : String
+ MensajeError(descripcion : String)
+ getDescripcion() : String

// tipo = ERROR
// Solo se usa si el cliente puede hacer algo al respecto; las desconexiones se manejan con excepciones del socket.

### Test

#### JsonTest
+ mensajeAJsonEnvuelveEnTypeYPayloadEnUnaSolaLinea() : void
+ jsonAMensajeTypeDesconocidoLanzaIllegalArgumentException() : void
+ jsonAMensajePayloadNoEncajaLanzaIllegalArgumentException() : void
+ roundTripTodosLosTiposDeMensaje() : void
+ deserializarEntidadAnimalCafeBanderaResuelveSubtipoPorTipo() : void
+ deserializarPosicionConNodoNullNoFalla() : void

#### MessageDispatcherTest
+ despacharMatchCreatedInvocaOnMatchCreated() : void
+ despacharGameStartedInvocaOnGameStarted() : void
+ despacharStateUpdateInvocaActualizarEstado() : void
+ despacharGameOverInvocaOnGameOver() : void
+ despacharJsonInvalidoNoLanzaExcepcionNiTocaElModelo() : void
+ despacharMensajeClienteAServidorSeDescarta() : void
+ conexionPerdidaInvocaOnDesconexion() : void

#### GameModelTest
+ onPrimerMensajeExitoTrueNombreConfirmado() : void
+ onPrimerMensajeExitoFalseGuardaRazonComoUltimoError() : void
+ onMatchJoinedGuardaCodigoYJugadores() : void
+ onGameStartedGuardaDatosYZonaVisibleInicial() : void
+ actualizarEstadoRecuerdaAnimalesConocidos() : void
+ getJugadorLocalIdentificaPorNickname() : void
+ reiniciarPartidaLimpiaElEstadoPeroConservaNickname() : void
+ cadaCambioNotificaObservadoresConElTipoMensajeCorrecto() : void
- validarEstadoVisibleZona(int idZona) : boolean

#### SelectionManagerTest
+ seleccionarAnimalVivoLocalSeAgrega() : void
+ seleccionarAnimalMuertoSeRechaza() : void
+ seleccionarAnimalDeOtroJugadorSeRechaza() : void
+ seleccionarDosVecesNoDuplica() : void
+ getIdsSeleccionadosDevuelveIdPackEIdAnimal() : void
+ sincronizarQuitaAnimalesQueYaNoEstanVivosONiVigentes() : void
- isAnimalSeleccionable(Animal animal) : boolean