## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Formato de mensajes (JSON): { "type": TipoMensaje, "payload": {Contenido} }
## Spec

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

#### Config 
- static instancia : Config
- ip : String
- puerto : int
- Config()
+ static getInstance() : Config
+ getIp() : String
+ getPuerto() : int
// Sigue un patron singleton

### mensajes
#### Mensaje

### Constantes
#### Contantes
+ longitudCodigoPartida : Static final int
- Constantes()
