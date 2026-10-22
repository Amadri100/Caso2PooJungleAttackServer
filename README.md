## Generalidades
Codigo de partida: 4 caracteres alfanumericos, case-insensitive.
Se utilizo java 21 con Maven, Jackson, jsvg y JUnit5

## Spec Server

### test

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

#### MainUtilidades
+ main(String args[]) : static void 

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


#### MainServer
+ main(String args[]) : static void 

### enviables

//Datos enviados por mensaje con modificaciones para que no de errores

 #### DatosEnviar 
- jugadorA : JugadorEnviar
- jugadorB : JugadorEnviar
- entidadesDisponibles : List\<EntidadEnviar>
- zonaActual : ZonaEnviar
- eventos : List\<EventoEnviar>
+ DatosEnviar(jugadorA : JugadorEnviar, jugadorB : JugadorEnviar, entidadesDisponibles : List\<EntidadEnviar>, zonaActual : ZonaEnviar, eventos : List\<EventoEnviar>)
+ static desde(datos : Datos) : DatosEnviar
+ getJugadorA() : JugadorEnviar
+ getJugadorB() : JugadorEnviar
+ getEntidadesDisponibles() : List\<EntidadEnviar>
+ getZonaActual() : ZonaEnviar
+ getEventos() : List\<EventoEnviar>

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
+ static desde(jugador : Jugador, zonaVisible : Zona) : JugadorEnviar
+ getId() : int
+ getNickname() : String
+ getEspecie() : TipoEspecie
+ getAnimales() : List\<AnimalEnviar>
+ getCafesConsumidos() : int
+ getBotsEliminados() : int
+ isConectado() : boolean

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

#### MainLogica
+ main(String args[]) : static void 

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

#### MainMapa
+ main(String args[]) : static void 

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
+ Mensaje(tipo : TipoMensaje)
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


## Spec Cliente
 
```
src/
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
        |-- Datos.java
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
        |-- ImageCache.java
        |-- Json.java
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
// ENCOUNTER_RADIUS se usa solo para pintar el alcance de ataque.

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
- datos : Datos
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
+ getDatos() : Datos
+ getJugadorLocal() : Jugador
+ getOponente() : Jugador
+ getAnimalesLocales() : List\<Animal>
+ getEnergiaConocida(idPack : int) : int
+ getVivosConocidos(idPack : int) : int
+ getGanador() : String
+ getUltimoError() : String
+ getZonaActualVisible() : int
+ setZonaActualVisible(zonaId : int) : void
+ actualizarEstado(datos : Datos) : void
+ realizarAccion (accion : AccionMensaje) : void
+ onDesconexion() : void
+ reiniciarPartida() : void
- notificar(tipo : TipoMensaje) : void
- recordarAnimales(datos : Datos) : void

// Estado del juego en el cliente
// El argumento de notifyObservers es el TipoMensaje que originó el cambio, así las vistas saben qué cambió.
// reiniciarPartida() se ejecuta despues de que la partida termine

#### Datos
- jugadorA : Jugador
- jugadorB : Jugador
- entidadesDisponibles : List\<Entidad>
- zonaActual : Zona
- eventos : List\<Evento>
- Datos()
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

// Espejo de Jugador (extends Pack) del servidor. id es el idPack usado en int[]{idPack, idAnimal}.
// Los animales de un jugador pueden venir incompletos: solo llegan los visibles desde la zona actual.
// getVivos() y getEnergiaTotal() se calculan a partir de la lista de animales; no son campos del JSON
// (Jackson está configurado sin autodetección de getters, así que nunca se serializan ni se exigen).

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

// Espejo de Entidad del servidor.
// Polimorfismo con anotaciones de Jackson sobre esta clase:
//   @JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "tipo", visible = true)
//   @JsonSubTypes({ @Type(value = Animal.class, name = "ANIMAL"), @Type(value = Cafe.class, name = "CAFE"), @Type(value = Bandera.class, name = "BANDERA") })
// EXISTING_PROPERTY usa el campo "tipo" que ya existe (no lo duplica) y visible = true lo conserva para llenar Entidad.tipo.
// Los nombres de @Type deben coincidir con TipoEntidad.name(). Como la anotación se hereda, Jugador.animales (List<Animal>) también exige "tipo":"ANIMAL".

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

// Representa a un animal para ser renderizado. En el servidor Animal tiene referencia a Pack, pero aquí solo se guarda idPack
// para evitar referencias circulares.
// getIdCompuesto() devuelve int[]{idPack, id}, el formato de MensajeMoveAnimals y MensajeAttack.
// getX() y getY() delegan en getPosicion().getPunto().

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

// En el servidor Bandera tiene una referencia a Jugador. El cliente solo necesita el id del dueño (idPack del Jugador).

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

// GamePanel dibuja una línea entre origen y destino durante UIConstants.DURACION_EVENTO_MS al recibir el evento.
// Como Evento se recrea en cada snapshot, no se usa como llave de eventosActivos por identidad: se compara por origen, destino y tipo (ver comentarios finales).

#### Zona
- id : int
- nodos : List\<Nodo>
+  Zona()
+ getId() : int
+ getNodos() : List\<Nodo>
+ getNodo(idNodo : int) : Nodo

// Espejo de Zona. El campo id no existe en la Zona del servidor (ver comentarios al final): el cliente lo necesita para VIEW_ZONE.
// El campo Zona.mapa del servidor no se mapea. Si llegara en el JSON, Jackson lo ignora (FAIL_ON_UNKNOWN_PROPERTIES desactivado).

#### Nodo
- id : int
- posicion : Punto
- idZona : int
+ Nodo()
+ getId() : int
+ getPosicion() : Punto
+ getIdZona() : int

// En el servidor Nodo apunta a su Zona. El cliente solo guarda el id para evitar un ciclo Nodo <-> Zona.
// El id del nodo es el mismo que se manda como idNodoDestino en MensajeMoveAnimals.

#### Posicion
- nodo : Nodo
- punto : Punto
+ Posicion()
+ getNodo() : Nodo
+ getPunto() : Punto

// Posición lógica (nodo) y posición real en el plano (punto). El punto es el que se dibuja.
// "nodo" puede venir null o incompleto (solo id y posicion) para no repetir toda la zona en cada entidad.

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

// Debe ser idéntico al del servidor: Jackson serializa los enums por nombre (no usar WRITE_ENUMS_USING_TO_STRING ni WRITE_ENUMS_USING_INDEX).

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
+ Mensaje(tipo : TipoMensaje)
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