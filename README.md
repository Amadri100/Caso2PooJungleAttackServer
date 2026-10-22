## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Formato de mensajes (JSON): { "type": TipoMensaje, "payload": {Contenido} }
## Spec Server

### main
#### Main
+ main(String args[]) : static void 

// Orden de arranque:
// Logger -> GeneradorAleatorio ->Servidor -> Ventana -> Servidor.iniciar()

### utilidades

#### Json
+ mensajeAJson(msg : Mensaje) : String
+ jsonAMensaje(json : String) : Mensaje

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
+ actualizarEstado(datos : HashMap\<String, Datos>) : void
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
+ notificar(datos : HashMap\<String, Datos>) : void
+ finalizar(ganador : Jugador) : boolean
+ getResumen() : ResumenPartida
- asignarZonas() : void
- crearBanderas() : void
- crearAnimales() : void
- crearCafes() : void
- filtrarZona(jugador : Jugador) : Zona
- obtenerDatos(jugador : Jugador) : Datos
- construirDatos() : HashMap\<String, Datos>

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
+ enviarDatos(datos : HashMap\<String, Datos>) : void
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
+ getPosicion() : Posicion
+ setPosicion(posicion : Posicion) : void
+ distanciaCon(otra : Entidad) : double

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

#### Bandera (extends Entidad)
- jugador : Jugador
- color : String
+ fueTocadaPor(Animal animal) : boolean

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
// nombre es el cliente asociado al mensaje: remitente si va cliente -> servidor,
// destinatario si va servidor -> cliente.
// Si nombre es Constantes.NOMBRE_UNIVERSAL (@Todos), el servidor lo manda a todos los clientes.
// Cada subclase concreta fija su propio tipo en el constructor, por eso no lo recibe.
// Json.jsonAMensaje() lee el campo tipo y deserializa a la subclase concreta correspondiente.


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
- datos : Datos
+ MensajeGameStarted(datos : Datos)
+ getDatos() : Datos

// tipo = GAME_STARTED
// datos = estado inicial visible para ese jugador.

#### MensajeStateUpdate (extends MensajeServidor)
- datos : Datos
+ MensajeStateUpdate(datos : Datos)
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


## Cliente
Sujeto a cambios...
//
```
src/
|-- client/
    |-- constants/
        |-- Constants.java
        |-- UIConstants.java
        |-- NetworkConstants.java
    |-- network/
        |-- ServerConnection.java
        |-- MessageDispatcher.java
    |-- protocol/
        |-- Message.java
        |-- MessageType.java
        |-- JsonSerializer.java
    |-- model/
        |-- GameModel.java
        |-- Animal.java
        |-- Punto.java
        |-- Datos.java
    |-- controller/
        |-- SelectionManager.java
        |-- GameController.java
    |-- screen/
        |-- MainWindow.java
        |-- GamePanel.java
        |-- HUDPanel.java
    |-- render/
        |-- ImageCache.java
```

## Spec Cliente
// TEMP hasta crear el otro proyecto.

### constantes

#### Constants (Clase final)
+ SERVER_IP : static final String
+ SERVER_PORT : static final int
- Constants()
// almacena los parámetros principales de conexión al servidor del juego

#### UIConstants
+ WINDOW_WIDTH : static final int 
+ WINDOW_HEIGHT : static final int 
+ FPS : static final int // Tasa de refresco de la interfaz gráfica

// se define las dimensiones de la ventana de Swing y los valores de renderizado

#### NetworkConstants
+ DEFAULT_PORT : static final int 
+ DEFAULT_HOST : static final String 

// Valores predeterminados de red 


### red

#### ServerConnection (implements Runnable)
- socket : Socket 
- entrada : BufferedReader 
- salida : PrintWriter // escritor para enviar solicitudes JSON
- dispatcher : MessageDispatcher // mensajes recibidos hacia el modelo
- activo : boolean // estado de conexión
+ ServerConnection(host : String, puerto : int, dispatcher : MessageDispatcher)
+ run() : void 
+ enviar(json : String) : void // envía una línea JSON al servidor
+ desconectar() : void // cierra el socket 

// hilo independiente que mantiene la comunicación con el servidor, leyendo líneas JSON 

#### MessageDispatcher 
- model : GameModel 
+ MessageDispatcher(model : GameModel)
+ despachar(json : String) : void // parsea el JSON recibido a Message y actualiza el modelo usando 

// procesa los mensajes raw que vienen de la red, los deserializa y actualiza el modelo local de forma segura para Swing


### modelo

#### GameModel (extends Observable) 
- nickname : String // nombre del jugador local
- codigoPartida : String // código
- zonaActualVisible : int // ID de la zona que el jugador está viendo
- animalesLocales : List\<Animal> // Lista de animales del paquete del jugador
+ GameModel()
+ actualizarEstado(datos : Datos) : void // sincroniza el estado local 
+ getZonaActualVisible() : int
+ setZonaActualVisible(zonaId : int) : void

// almacena la información que el servidor reporta y notifica a las vistas

#### Datos
- jugadorA : Jugador
- jugadorB : Jugador
- entidadesDisponibles : List\<Entidad>
- zonaActual : Zona
- evento : List\<Evento> 
+ getJugadorA() : Jugador
+ getJugadorB() : Jugador
+ getEntidadesDisponibles() : List\<Entidad>
+ getZonaActual() : Zona
+ getEventos() : List\<Evento>

// espejo local del snapshot enviado por el servidor en GAME_STARTED y STATE_UPDATE

#### Animal
- id : int 
- especie : String // nombre de la especie 
- energia : int 
- posicionX : double 
- posicionY : double 
+ Animal(id : int, especie : String, energia : int)
+ estaVivo() : boolean

/ Representa de forma local a un animal del juego, conteniendo su posición, energía y especie para ser renderizado

#### Punto 
- x : double
- y : double
+ Punto(x : double, y : double) : Punto
+ getX() : double
+ getY() : double


### controlador

#### SelectionManager
- animalesSeleccionados : List\<Animal>
+ SelectionManager()
+ seleccionar(animal : Animal) : void
+ deseleccionarTodo() : void
+ getSeleccionados() : List\<Animal>

// controla la selección de elementos mediante clics del usuario en la interfaz para mandar órdenes luego


### vista

#### MainWindow (extends JFrame)
- gamePanel : GamePanel 
- HUDPanel : HUDPanel 
+ MainWindow()
+ mostrarPantalla(panelName : String) : void

// ventana principal de la aplicación en Swing que gestiona el cambio entre paneles

#### GamePanel (extends JPanel)
// dibuja el mapa y los animales 

#### HUDPanel (extends JPanel)
// HUDPanel de la interfaz de usuario para mostrar estadísticas de juego, tazas de café y barras de vida


### util

#### ImageCache (Patrón Singleton)
- instancia : static ImageCache
- imagenes : Map<String, BufferedImage>
- ImageCache()
+ static getInstance() : ImageCache
+ getImagen(key : String) : BufferedImage
+ getImagen(key : String) : // por determinar

// Caché global que precarga y almacena en memoria las imágenes y texturas para evitar lecturas de disco que sean de forma repetitiva


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
// nombre es el cliente asociado al mensaje: remitente si va cliente -> servidor,
// destinatario si va servidor -> cliente.
// Si nombre es Constantes.NOMBRE_UNIVERSAL (@Todos), el servidor lo manda a todos los clientes.
// Cada subclase concreta fija su propio tipo en el constructor, por eso no lo recibe.
// Json.jsonAMensaje() lee el campo tipo y deserializa a la subclase concreta correspondiente.


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
- datos : Datos
+ MensajeGameStarted(datos : Datos)
+ getDatos() : Datos

// tipo = GAME_STARTED
// datos = estado inicial visible para ese jugador.

#### MensajeStateUpdate (extends MensajeServidor)
- datos : Datos
+ MensajeStateUpdate(datos : Datos)
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
