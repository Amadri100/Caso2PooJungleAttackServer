## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Formato de mensajes (JSON): { "type": TipoMensaje, "payload": {Contenido} }
## Spec Server

### utilidades

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
- Constantes()

// Las constantes de este archivo no seran modificadas, por lo tanto no es necesario usar un singleton.
// Ip = "localhost", que es la ip de el dispositvo

### server
#### ClientHandler (implements Runnable, Observador) 
- socket : Socket
- entrada : BufferedReader
- salida : PrintWriter
+ ClientHandler(socket : Socket, servidor : Servidor)
+ run() : void
+ actualizar(mensaje: Mensaje) : void
+ enviar(json : String) : void
+ cerrar() : void
+ getPartida() : Partida
+ setPartida(partida : Partida) : void
- parsearMensaje(json : String) : Comando
- clasificar(comando : Comando) : void
- procesarSolicitudes() : void
- procesarMensajesPartida() : void



#### ThreadConexiones (implements Runnable) 
- serverSocket : ServerSocket
- servidor : Servidor
- activo : boolean
+ ThreadConexiones(servidor : Servidor, puerto : int)
+ run() : void
+ detener() : void
// Hilo infinito que acepta conexiones. Por cada Socket crea un ClientHandler
// y se lo entrega a Servidor.registrarCliente(). El puerto viene de Config.


// Servidor
// PATRON MEDIATOR/FACADE: se conecta con las demas partidas mediante InterfazPartidas.
// El ExecutorService se aplica a ThreadConexiones y a cada ClientHandler.
// Se usa una Observable propia (java.util.Observable esta deprecado).
#### Servidor (extends Observable) 
- clientes : List<ClientHandler>
- threadConexiones : ThreadConexiones
- executor : ExecutorService
+ Servidor()
+ iniciar() : void
+ detener() : void
+ registrarCliente(handler : ClientHandler) : void
+ removerCliente(handler : ClientHandler) : void
+ notificar(mensaje: Mensaje) : void
 
// PATRON OBSERVER: es el Observable; notifica a sus ClientHandler.

#### ServerConfig 
- static instancia : Config
- ip : String
- puerto : int
- Config()
+ static getInstance() : Config
+ getIp() : String
+ getPuerto() : int
// Sigue un patron singleton


### controlador
#### Controlador
- mapa : Map
- servidor : Server
- partidas : ConcurrentHashMap\<String, Partida>
- ejecutor : ExecutorService
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
- cafes : ArrayList\<Cafe>
- banderas : ArrayList\<Bandera>
- ejecutor : ExecutorService
- notificador : Notificador
- inicio : long
+ run() : void
+ crearJuego() : boolean
+ iniciarPartida() : boolean
+ actualizar(Datos) : void
+ notificar(Datos) : void
+ recibirDato(int jugadorId, Mensaje mensaje) : void
+ finalizar(Jugador ganador) : boolean
- asignarZonas() : void
- crearBanderas() : void
- crearAnimales() : void
- crearCafes() : void
- filtrarZona(Jugador jugador) : Zona
- ejecutarDato(Jugador jugador, Mensaje mensaje) : void
- obtenerDatos(Jugador jugador) : DatosJugador

#### ThreadAnimal (Abstract implements Runnable)
- rutaActual : Ruta
- objetivoAtaque : Animal
- moverse() : void
- atacar() : void

#### ThreadJugador (extender ThreadAnimal)
- animal : Jugador
- nickname : String
- objetivoAtaque : Animal
- ruta : Ruta
- ultimoAtaque : long
- activo : boolean
+ run() : void
+ setDestino(Posicion destino) : void
+ setObjetivo(Animal objetivo) : void
+ detener() : void
- moverse() : void
- atacar() : void
- verificarBandera() : boolean

#### ThreadBot (implements Runnable)
- bot : Bot
- rutaActual : Ruta
- instruccionActual : Instruccion
- estrategia : EstrategiaBot
- ultimoAtaque : long
- activo : boolean
+ run() : void
+ detener() : void
- buscarSiguienteRuta() : void
- ejecutarInstruccion() : void
- intentarAtacar() : boolean

#### EstrategiaBot (interface)
+ elegirDestino(Bot bot, Partida partida) : Posicion

#### EstrategiaDirecta (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Posicion

#### EstrategiaCercana (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Posicion

#### EstrategiaAleatoria (implements EstrategiaBot)
+ elegirDestino(Bot bot, Partida partida) : Posicion

#### SelectorEstrategia
- random : Random
+ elegir() : EstrategiaBot

#### Notificador
- conexiones : HashMap\<Jugador, Conexion>
+ registrar(Jugador jugador, Conexion conexion) : void
+ notificar(Jugador jugador, DatosJugador datos) : void
+ notificarTodos(HashMap\<Jugador, DatosJugador> datos) : void

#### Entidad (abstract)
- id : int
- posicion : Posicion
+ getPosicion() : Posicion
+ setPosicion(Posicion posicion) : void
+ distanciaCon(Entidad otra) : double

#### Animal (abstract, extends Entidad)
- especie : Especie
- energia : int
- danio : int
- zonaActual : Zona
+ recibirDanio(int cantidad) : void
+ estaVivo() : boolean
+ getPropiedades() : PropiedadesAnimal
+ energiaInicial() : int (abstract)
+ danioInicial() : int (abstract)
+ moverse() : void

#### Jugador (extends Animal)
- nickname : String
- pack : PackJugador
- zonaElegida : Zona
- cafesConsumidos : int
- botsEliminados : int
+ elegirZona(Zona zona) : void
+ getZonasConAnimales() : ArrayList\<Zona>
+ sumarCafe() : void
+ sumarBotEliminado() : void

#### Bot (extends Animal)
- estrategia : EstrategiaBot
+ getEstrategia() : EstrategiaBot
+ cambiarDeEstrategia() : void

#### Cafe (extends Entidad)
- consumido : boolean
+ consumir() : boolean
+ estaDisponible() : boolean

#### Bandera (extends Entidad)
- dueno : Jugador
- color : String
+ fueTocadaPor(Animal animal) : boolean

#### Evento
- tipo : TipoEvento
- origenId : int
- destinoId : int
- valor : int

#### DatosJugador
- zona : Zona
- animales : ArrayList\<Animal>
- eventos : ArrayList\<Evento>
- posicionesObjetivos : ArrayList\<Posicion>

#### TipoEspecie (enum)
+ JAGUAR
+ Yiguirro
+ Perezoso
+ Danta
+ Tucan
+ VenadoCoBla
+ MonoCaBla


### mapa

#### Mapa (singleton)
- zonas : List<Zona>
- vecinos : Map<Nodo, List'<Vecinos>>
- Mapa()
- crearZonas() : void
- crearNodos() : void
- crearVecinos() : void
- crearMapaVecinos() : void
- crearMapa() : Mapa
+ getInstance() : Mapa
+ getZonas() : List<Zona>
+ getVecinos() : Map<Nodo, List\<Vecinos>>
+ getVecinosDe(Nodo) : List<Vecinos>
+ getPeso(Nodo, Nodo) : Double

// Primero se crean las zonas, despues se crean los nodos y se insertan en las zonas y finalmente se crean los vecinos, al crear 
los vecinos de un nodo, se agrega ese mismo vecino al nodo A y al B para evitar crear el mismo objeto 2 veces.

// Como el mapa no cambia es mejor que exista un unico mapa para todo el programa.

#### Zona
- nodos : List<Nodo>
- mapa : Map
+ Zona(Map)
+ getNodos() : List<Nodo>
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

// Error indicaria que paso algo con el procedimiento actual; solo se usa si el cliente puede hacer algo, en caso de desconexion eso se 
maneja con excepciones desde el socket.

#### Mensaje













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

### UIConstants
+ WINDOW_WIDTH : static final int = ??
+ WINDOW_HEIGHT : static final int = ??
+ FPS : static final int = ?? // tasa de refresco 

### NetworkConstants
+ DEFAULT_PORT : static final int = //
+ DEFAULT_HOST : static final String = // 

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

### MessageDispatcher //

### GameModel (extends Observable) // ?? por determinar

### Animal
- id : int 
- especie : String // nombre de la especie 
- energia : int 
- posicionX : double 
- posicionY : double 
+ Animal(id : int, especie : String, energia : int)
+ estaVivo() : boolean

### SelectionManager
- animalesSeleccionados : // por determinar
+ SelectionManager()
+ seleccionar(animal : Animal) : void

### MainWindow (extends JFrame)
- gamePanel : GamePanel 
- HUDPanel : HUDPanel /
+ MainWindow()
+ mostrarPantalla(panelName : String) : void

### ImageCache (Patrón Singleton)
- instancia : static ImageCache
- imagenes : // por determinar
- ImageCache()
+ static getInstance() : ImageCache
+ getImagen(key : String) : // por determinar

