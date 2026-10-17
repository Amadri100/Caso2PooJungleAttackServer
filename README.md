## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Formato de mensajes (JSON): { "type": TipoMensaje, "payload": {Contenido} }
## Spec Server

### constantes
#### Constantes (Clase final)
+ IpServidor : static final String 
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

### Constantes
#### Contantes
+ longitudCodigoPartida : Static final int
- Constantes()

## Spec Cliente
// TEMP hasta crear el otro proyecto.

### constantes
#### Constantes (Clase final)
+ ServerIP : String
- Constantes()