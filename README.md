## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Formato de mensajes (JSON): { "type": TipoMensaje, "payload": {Contenido} }
## Spec Server

### main
#### Main
+ main(String args[]) : static void 

### utilidades

####

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
- Constantes()

// Las constantes de este archivo no seran modificadas, por lo tanto no es necesario usar un singleton.
// Ip = "localhost", que es la ip de el dispositvo
// NOMBRE_UNIVERSAL string como @Todos, reservado especificamente para referirse a todos los clientes.

### server
#### ClientHandler (implements Runnable) 
- socket : Socket
- input : BufferedReader
- output : BufferedWritter
- entrada : BufferedReader
- salida : PrintWriter
+ ClientHandler(socket : Socket, servidor : Servidor)
+ run() : void
+ actualizar(mensaje: Mensaje) : void
+ cerrar() : void
+ mandar(mensaje : Mensaje) : void
+ ascenderMensaje(mensaje : Mensaje) : void

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
- clientes : Map\<String, ClientHandler>
- threadConexiones : ThreadConexiones
- executor : ExecutorService
+ Servidor()
+ iniciar() : void
+ detener() : void
+ registrarCliente(handler : ClientHandler) : void
+ removerCliente(handler : ClientHandler) : void
+ mandarAClientes(datos : Datos) : void 
+ mandarASistema(msg : Mensaje) : void 
 
El ExecutorService se aplica a ThreadConexiones y a cada ClientHandler.
// Se manda el mensaje segun el nombre que esta asociado al dato, si tiene el NOMBRE_UNIVERSAL, se manda ese mismo mensaje a todos
los clientes.

#### ServerConfig 
- static instancia : Config
- ip : String
- puerto : int
- nombresOcupados : List\<String>
- Config()
+ static getInstance() : Config
+ getIp() : String
+ getPuerto() : int

// Sigue un patron singleton


### controlador
#### Controlador
- mapa : Mapa
- servidor : Server
- partidas : ConcurrentHashMap\<String, Partida>
- ejecutor : ExecutorService
+ crearMensaje(Datos) : void
+ ejecutarMensaje(msg : Mensaje) : void
+ crearJuego(String nickname) : String
+ unirsePartida(String codigo, String nickname) : boolean
+ recibirDato(String codigo, int jugadorId, Mensaje mensaje) : void
+ finalizarPartida(String codigo) : void

### logica

#### EstadoPartida (enum)
+ ESPERANDO
+ SELECCION
+ JUGANDO
+ TERMINADA

#### Partida (implements Runnable)
- controlado: Controlador
- codigo : String
- estado : EstadoPartida
- jugadores : ArrayList\<Jugador>
- bots : ArrayList\<Bot>
- manadas : ArrayList\<Pack>
- cafes : ArrayList\<Cafe>
- banderas : ArrayList\<Bandera>
- ejecutor : ExecutorService
- notificador : Notificador
- inicio : long
+ run() : void
+ mandarAtaque(A : int[], B : int[])
+ crearJuego() : boolean
+ iniciarPartida() : boolean
+ actualizar(Datos) : void
+ notificar(Datos) : void
+ recibirDato(mensaje : Mensaje) : void
+ finalizar(Jugador ganador) : boolea
- asignarZonas() : void
- crearBanderas() : void
- crearAnimales() : void
- crearCafes() : void
- filtrarZona(Jugador jugador) : Zona
- ejecutarDato(Jugador jugador, Mensaje mensaje) : void
- obtenerDatos(Jugador jugador) : DatosJugador

// El cliente al elegir un animal se elige el animal como {idPack, idDelAnimal} y lo mismo para el enemigo por lo 
tanto eso es el int[]

#### ThreadAnimal (Abstract implements Runnable)
- partida : Partida
- ultimoAtaque : long
- moverse() : void
+ run () : void
+ getPartida() : Partida
+ getUltimoAtaque() : long
+ agregarUltimoAtaque(long milis) : void
+ reinciarAtaque() : void

#### ThreadJugador (extender ThreadAnimal)
- animal : Jugador
- activo : boolean
- ataquesPendientes : List\<int[][]>
+ run() : void
+ setObjetivo(Animal objetivo) : void
+ detener() : void
- moverse() : void
- atacar() : void
+ agregarAtaque(idJugador : int[], idEnemigo : int[]) : void 
- verificarBandera() : boolean



#### ThreadBot (implements Runnable) 
- bot : Bot
- activo : boolean
+ run() : void
+ detener() : void
- buscarSiguienteRuta() : void
- intentarAtacar() : boolean

//Los ataques del Jugador y Bot deben notificar el evento a la
partida.
//El bot cambia de ruta despues de una cantidad determinada
de movimientos

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
- random : Random
+ elegir() : EstrategiaBot

#### Entidad (abstract)
- id : int
- posicion : Posicion
+ getPosicion() : Posicion
+ setPosicion(posicion : Posicion) : void
+ distanciaCon(otra : Entidad) : double

#### Animal (abstract, extends Entidad)
- pack : Pack
- energia : int
- ruta : Ruta
- zonaActual : Zona
+ setRuta(ruta : Ruta) : void
+ getRuta() : Ruta
+ recibirDamage(cantidad : int) : void
+ estaVivo() : boolean
+ moverse() : boolean 

#### Pack (abstract)
- id : int
- animales : List\<Animal>
- especie : Especie
- damage : int
- conteoDeBajas : int
+ atacar(jugador : id) : int
+ getLista() : List\<Animal>
+ getZonasAnimales() : ArrayList\<Zona>
+ agregarBotEliminado() : void

// si atacar() devuelve -1 significa que no se ejecuto

#### Jugador (extends Pack)
- nickname : String
- zonaElegida : Zona
- cafesConsumidos : int
- botsEliminados : int
+ elegirZona(zona : Zona) : void
+ sumarCafe() : void

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

#### Datos



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
+ ANIMAL_SELECTION
+ MATCH_JOINED 
+ ERROR
+ ANIMAL_SELECTION
+ GAME_STARTED
+ STATE_UPDATE
+ GAME_OVER

// Inicio
// PRIMER_MENSAJE, servidor -> cliente en el PRIMER_MENSAJE se indica de si hubo una conexion exitosa, si se rechaza y las razones, 
// INDICAR_NOMBRE cliente -> servidor, se manda con un nombre.

// Pantalla de selección de partidas
// CREATE_MATCH cliente -> servidor
// JOIN_MATCH cliente -> servidor

// Juego
// SELECT_ANIMAL cliente -> servidor
// MOVE_ANIMALS cliente -> servidor
// ATTACK cliente -> servidor
// VIEW_ZONE cliente -> servidor
// MATCH_CREATED Server → Client
// ANIMAL_SELECTION Server → Client
// MATCH_JOINED Server → Client
// GAME_STARTED Server → Client
// STATE_UPDATE Server → Client
// GAME_OVER cliente -> servidor

// General
// ERROR: Server → Client indicaria que paso algo con el procedimiento actual; solo se usa si el cliente puede hacer algo, en caso de desconexion eso se 
maneja con excepciones desde el socket.

 

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

// Inicio
// PRIMER_MENSAJE        servidor -> cliente
// INDICAR_NOMBRE        cliente -> servidor

// Pantalla de selección de partidas
// CREATE_MATCH          cliente -> servidor
// JOIN_MATCH            cliente -> servidor
// MATCH_CREATED         servidor -> cliente
// MATCH_JOINED          servidor -> cliente

// Juego
// ANIMAL_SELECTION      servidor -> cliente
// SELECT_ANIMAL         cliente -> servidor
// GAME_STARTED          servidor -> cliente
// MOVE_ANIMALS          cliente -> servidor
// ATTACK                cliente -> servidor
// VIEW_ZONE             cliente -> servidor
// STATE_UPDATE          servidor -> cliente
// GAME_OVER             servidor -> cliente

// General
// ERROR                 servidor -> cliente


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


// ===================== Cliente -> Servidor =====================

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


// ===================== Servidor -> Cliente =====================

#### MensajePrimerMensaje (extends MensajeServidor)
- exito : boolean
- razon : String
+ MensajePrimerMensaje(nombre : String, exito : boolean, razon : String)
+ isExito() : boolean
+ getRazon() : String

// tipo = PRIMER_MENSAJE
// razon indica por qué se rechazó la conexión (null si exito es true).

#### MensajeMatchCreated (extends MensajeServidor)
- codigo : String
+ MensajeMatchCreated(nombre : String, codigo : String)
+ getCodigo() : String

// tipo = MATCH_CREATED

#### MensajeMatchJoined (extends MensajeServidor)
- codigo : String
- jugadores : List\<String>
+ MensajeMatchJoined(nombre : String, codigo : String, jugadores : List\<String>)
+ getCodigo() : String
+ getJugadores() : List\<String>

// tipo = MATCH_JOINED

#### MensajeAnimalSelection (extends MensajeServidor)
- especiesDisponibles : List\<TipoEspecie>
- seleccionOponente : TipoEspecie
+ MensajeAnimalSelection(nombre : String, especiesDisponibles : List\<TipoEspecie>), especieE : TipoEspecie) 
+ getEspeciesDisponibles() : List\<TipoEspecie>
+ getSeleccionOponente() : TipoEspecie

// tipo = ANIMAL_SELECTION

#### MensajeGameStarted (extends MensajeServidor)
- datos : Datos
+ MensajeGameStarted(nombre : String, datos : Datos)
+ getDatos() : Datos

// tipo = GAME_STARTED
// datos = estado inicial visible para ese jugador.

#### MensajeStateUpdate (extends MensajeServidor)
- datos : Datos
+ MensajeStateUpdate(nombre : String, datos : Datos)
+ getDatos() : Datos

// tipo = STATE_UPDATE
// datos = estado actual visible para ese jugador.

#### MensajeGameOver (extends MensajeServidor)
- ganador : String
+ MensajeGameOver(nombre : String, ganador : String)
+ getGanador() : String

// tipo = GAME_OVER

#### MensajeError (extends MensajeServidor)
- descripcion : String
+ MensajeError(nombre : String, descripcion : String)
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
        |-- Position.java
    |-- controller/
        |-- SelectionManager.java
    |-- view/
        |-- MainWindow.java
        |-- GamePanel.java
        |-- HUDPanel.java
    |-- render/
        |-- ImageCache.java
```

## Spec Cliente
// TEMP hasta crear el otro proyecto.

### Constants (Clase final)
+ SERVER_IP : static final String
+ SERVER_PORT : static final int
- Constants()
// almacena los parámetros principales de conexión al servidor del juego

### UIConstants
+ WINDOW_WIDTH : static final int = ??
+ WINDOW_HEIGHT : static final int = ??
+ FPS : static final int = ?? // tasa de refresco 
+ WINDOW_WIDTH : static final int = 1280
+ WINDOW_HEIGHT : static final int = 720
+ FPS : static final int = 30 // Tasa de refresco de la interfaz gráfica

// se define las dimensiones de la ventana de Swing y los valores de renderizado

### NetworkConstants
+ DEFAULT_PORT : static final int = //
+ DEFAULT_HOST : static final String = // 

// Valores predeterminados de red 

### ServerConnection (implements Runnable)
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

### MessageDispatcher 
- model : GameModel 
+ MessageDispatcher(model : GameModel)
+ despachar(json : String) : void // parsea el JSON recibido a Message y actualiza el modelo usando 
// procesa los mensajes raw que vienen de la red, los deserializa y actualiza el modelo local de forma segura para Swing

### GameModel (extends Observable) 
- nickname : String // nombre del jugador local
- codigoPartida : String // código
- zonaActualVisible : int // ID de la zona que el jugador está viendo
- animalesLocales : List<Animal> // Lista de animales del paquete del jugador
+ GameModel()
+ actualizarEstado(datos : Datos) : void // sincroniza el estado local 
+ getZonaActualVisible() : int
+ setZonaActualVisible(zonaId : int) : void

// almacena la información que el servidor reporta y notifica a las vistas

### Animal
- id : int 
- especie : String // nombre de la especie 
- energia : int 
- posicionX : double 
- posicionY : double 
+ Animal(id : int, especie : String, energia : int)
+ estaVivo() : boolean

/ Representa de forma local a un animal del juego, conteniendo su posición, energía y especie para ser renderizado

### SelectionManager
- animalesSeleccionados : // por determinar
+ SelectionManager()
+ seleccionar(animal : Animal) : void
+ deseleccionarTodo() : void
+ getSeleccionados() : List<Animal>

// controla la selección de elementos mediante clics del usuario en la interfaz para mandar órdenes luego

### MainWindow (extends JFrame)
- gamePanel : GamePanel 
- HUDPanel : HUDPanel /
+ MainWindow()
+ mostrarPantalla(panelName : String) : void

// ventana principal de la aplicación en Swing que gestiona el cambio entre paneles 
// HUDPanel de la interfaz de usuario para mostrar estadísticas de juego, tazas de café y barras de vida

### ImageCache (Patrón Singleton)
- instancia : static ImageCache
- imagenes : // por determinar
- ImageCache()
+ static getInstance() : ImageCache
+ getImagen(key : String) : // por determinar

// Caché global que precarga y almacena en memoria las imágenes y texturas para evitar lecturas de disco que sean de forma repetitiva
