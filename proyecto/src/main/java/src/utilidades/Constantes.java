package src.utilidades;
import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constantes {

    public static final int LONGITUD_CODIGO = 4;
    public static final String IP_SERVIDOR = "localhost";
    public static final int PUERTO_DEFECTO = 4001;
    public static final String NOMBRE_UNIVERSAL = "@Todos";  
    public static final double ENCOUNTER_RADIUS = 50.0;      
    public static final double ANIMAL_SPEED = 2.0;           
    public static final long ATTACK_COOLDOWN_MS = 1000L;    
    public static final int MAX_PLAYER_ENERGY = 100;       
    public static final int PLAYER_INITIAL_ENERGY = 100;    
    public static final int TOTAL_JUGADORES = 2;
    public static final int TOTAL_BOTS = 5;
    public static final int PLAYER_DAMAGE = 10;              
    public static final int PLAYER_PACK_SIZE = 5;     
    public static final int BOT_INITIAL_ENERGY = 100;        
    public static final int BOT_DAMAGE = 8;                  
    public static final int BOT_PACK_SIZE = 5;        
    public static final int COFFEE_DAMAGE_BONUS = 1;
    public static final int ENERGY_BONUS_PER_TWO_BOTS = 1;
    public static final int TOTAL_ZONES = 9;
    public static final int TOTAL_COFFEE_CUPS = 10;          
    public static final int MOVIMIENTOS_MINIMOS_CAMBIAR = 3;
    public static final long INTERVALO_STATE_UPDATE_MS = 100L; 
    public static final long SEED_POR_DEFECTO = 12345L;   
    public static final String LOG_FILE = "juego.log";       
    public static final int MAX_LINEAS_CONSOLA = 200;     
    public static final int MAX_HISTORIAL_LOG = 1000;   
    public static final String CARACTERES_CODIGO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int MAX_COLA = 10000;
    public static final long TIEMPO_SLEEP_MINIMO = 20;

    public static final int TOTAL_ANIMALES = TOTAL_JUGADORES + TOTAL_BOTS;
    public static final int TOTAL_ZONAS = TOTAL_ZONES;

    public static final long MS_ESPERA_MAXIMA_COLA = 100;
    public static final long MS_ANTES_DE_MORRIR = 2000;
    public static final String NOMBRE_THREAD_LOGGER = "logger";
    public static final Path RUTA_ARCHIVO = Paths.get("logs", "Log.log");
    public static final String ORIGEN_SERVER = "Servidor";



    public static final int MAX_JUGADORES_PARTIDA = TOTAL_JUGADORES;
    public static final long INTERVALO_ACTUALIZACION_MS = INTERVALO_STATE_UPDATE_MS;
    public static final String ORIGEN_PARTIDA = "Partida";

    public static final int CANTIDAD_BOTS_PARTIDA = TOTAL_BOTS;
    public static final int CANTIDAD_CAFES_PARTIDA = TOTAL_COFFEE_CUPS;
    public static final java.awt.Color[] COLORES_BANDERAS = { java.awt.Color.RED, java.awt.Color.BLUE };
    public static final String ERROR_PARTIDA_ZONAS_INSUFICIENTES = "El mapa no tiene suficientes zonas para los jugadores de la partida";


    public static final String ERROR_PARTIDA_ESTADO_INVALIDO = "La partida no está en el estado necesario para esa acción";
    public static final String ERROR_PARTIDA_JUGADOR_NO_ENCONTRADO = "No perteneces a esta partida";
    public static final String ERROR_PARTIDA_ESPECIE_NO_DISPONIBLE = "La especie no está disponible";
    public static final String ERROR_PARTIDA_ESPECIE_YA_ELEGIDA = "Ya elegiste una especie";
    public static final String ERROR_PARTIDA_ANIMAL_AJENO = "Solo puedes dar órdenes a tus propios animales";
    public static final String ERROR_PARTIDA_MOVIMIENTO_INVALIDO = "El destino de movimiento no es válido o no tiene ruta";
    public static final String ERROR_PARTIDA_ATAQUE_INVALIDO = "Ataque inválido";
    public static final String ERROR_PARTIDA_ZONA_NO_VISIBLE = "No tienes animales en esa zona";

    public static final String ERROR_PARTIDA_ENVIAR_ESTADO = "Error al construir o enviar el estado de la partida";

    public static final String SEPARADOR_ERROR = ": ";
    public static final String ESPACIO_ENTRE_TEXTO = ", ";
    public static final String TEXTO_DEFECTO = "DEFECTO";

    public static final String DEBUG = "DEBUG";
    public static final String ERROR = "ERROR";
    public static final String INFO = "INFO";

    public static final String ERROR_EN_LOGGER = "ERROR interno en el logger";

    public static final String ERROR_CREAR = "No se pudo crear la partida";
    public static final String ERROR_ESPECIE = "Debes indicar una especie";
    public static final String ERROR_SIN_PARTIDA = "No estás en una partida";
    public static final String ERROR_ANIMALES = "El atacante y el enemigo deben ser animales válidos";
    public static final String ERROR_UNIRSE = "No se pudo unir a la partida";
    public static final String ERROR_NOMBRE_NO_DISPONIBLE = "El nombre ya esta reservado";
    public static final String NO_HAY_ERROR = "NO ERROR";
    public static final String ERROR_INICIO_SERVER = "No se pudo inicializar el serverSocket, razon: ";
    public static final String ERROR_SERIALIZACION = "No se pudo serializar el mensaje";
    public static final String ERROR_DESERIALIZACION = "No se pudo serializar el mensaje";
    public static final String CAMPO_TIPO = "tipo";
    public static final String ERROR_MENSAJE_NULO = "El mensaje no puede ser null";
    public static final String ERROR_JSON_VACIO = "El JSON está vacío";
    public static final String ERROR_TIPO_AUSENTE = "El mensaje no tiene el campo 'tipo'";
    public static final String ERROR_TIPO_DESCONOCIDO = "Tipo de mensaje desconocido";
    public static final String ERROR_CLASE_NULA = "La clase destino no puede ser null";

    public static final int INTERVALO_REFRESCO_UI_MS = 500;
    public static final String UI_TITULO_VENTANA = "Servidor - Panel de control";
    public static final int UI_ANCHO_VENTANA = 1300;
    public static final int UI_ALTO_VENTANA = 800;
    public static final int UI_POSICION_DIVISOR = 620;
    public static final double UI_PESO_DIVISOR = 0.45;
    public static final int UI_ESPACIO = 6;
    public static final int UI_ESPACIO_BARRA_ESTADO = 16;
    public static final String UI_ESTADO_CLIENTES = "Clientes conectados: ";
    public static final String UI_ESTADO_PARTIDAS = "Partidas activas: ";
    public static final String UI_ESTADO_SEED = "Seed actual: ";
    public static final String UI_ORIGEN_VENTANA = "VentanaServidor";
    public static final String UI_ERROR_REFRESCAR = "Error al refrescar la ventana";
    public static final String UI_ERROR_DETENER_SERVIDOR = "Error al detener el servidor";

    // ---------- UI: partidas ----------
    public static final String UI_TITULO_PARTIDAS = "Partidas";
    public static final String UI_TEXTO_SIN_PARTIDAS = "No hay partidas en ejecucion";
    public static final int UI_INCREMENTO_SCROLL = 16;
    public static final Color UI_COLOR_BORDE_TARJETA = new Color(160, 160, 160);
    public static final float UI_TAMANO_TITULO_TARJETA = 14f;
    public static final String UI_PREFIJO_CODIGO = "Partida ";
    public static final String UI_PREFIJO_DURACION = "Duracion: ";
    public static final String UI_PREFIJO_BOTS = "Bots: ";
    public static final String UI_SEPARADOR_BOTS = "/";
    public static final String UI_PREFIJO_CAFES = "Cafes: ";
    public static final String UI_PREFIJO_GANADOR = "Ganador: ";
    public static final String[] UI_COLUMNAS_JUGADORES = {
        "Nickname", "Especie", "Animales vivos", "Energia", "Cafes", "Bots eliminados"
    };
    public static final int UI_PRIMERA_COLUMNA_NUMERICA = 2;
    public static final long UI_MS_POR_SEGUNDO = 1000L;
    public static final long UI_SEGUNDOS_POR_MINUTO = 60L;
    public static final long UI_SEGUNDOS_POR_HORA = 3600L;
    public static final String UI_FORMATO_DURACION_CORTA = "%02d:%02d";
    public static final String UI_FORMATO_DURACION_LARGA = "%d:%02d:%02d";
    public static final Color UI_COLOR_ESTADO_ESPERANDO = new Color(240, 200, 40);
    public static final Color UI_COLOR_ESTADO_SELECCION = new Color(70, 130, 220);
    public static final Color UI_COLOR_ESTADO_JUGANDO = new Color(60, 170, 80);
    public static final Color UI_COLOR_ESTADO_TERMINADA = new Color(130, 130, 130);
    public static final Color UI_COLOR_TEXTO_ESTADO = Color.BLACK;
    public static final Color UI_COLOR_DESCONECTADO = Color.RED;

    // ---------- UI: consola ----------
    public static final int UI_MAX_LINEAS_CONSOLA = 5000;
    public static final int UI_TAMANO_FUENTE_CONSOLA = 12;
    public static final Color UI_COLOR_LOG_DEBUG = Color.GRAY;
    public static final Color UI_COLOR_LOG_WARN = new Color(255, 140, 0);
    public static final Color UI_COLOR_LOG_ERROR = Color.RED;
    public static final String UI_ORIGEN_CONSOLA = "PanelConsola";
    public static final String UI_ERROR_AGREGAR_LINEA = "No se pudo agregar una linea a la consola";
    public static final String UI_ERROR_RECORTAR_CONSOLA = "No se pudo recortar la consola";

    // ---------- UI: control ----------
    public static final int UI_COLUMNAS_CAMPO_SEED = 12;
    public static final int UI_COLUMNAS_CAMPO_FILTRO = 12;
    public static final String UI_TEXTO_ETIQUETA_SEED = "Seed:";
    public static final String UI_TEXTO_SEED_FIJA = "Seed fija";
    public static final String UI_TEXTO_APLICAR_SEED = "Aplicar";
    public static final String UI_TEXTO_SEED_ALEATORIA = "Aleatoria";
    public static final String UI_TEXTO_ETIQUETA_NIVEL = "Nivel:";
    public static final String UI_TEXTO_AUTOSCROLL = "Auto-scroll";
    public static final String UI_TEXTO_ETIQUETA_FILTRO = "Filtro:";
    public static final String UI_TEXTO_LIMPIAR = "Limpiar";
    public static final String UI_TEXTO_GUARDAR = "Guardar";
    public static final String UI_TITULO_ERROR = "Error";
    public static final String UI_ORIGEN_CONTROL = "PanelControl";
    public static final String UI_ERROR_SEED_INVALIDA = "Seed invalida: ";
    public static final String UI_ERROR_SEED_INVALIDA_DIALOGO = "La seed debe ser un numero entero (long).";
    public static final String UI_MENSAJE_SEED_APLICADA = "Seed aplicada: ";
    public static final String UI_MENSAJE_SEED_FIJA = " (fija: ";
    public static final String UI_MENSAJE_SOLO_NUEVAS = "). Solo afecta a partidas nuevas.";
    public static final String UI_MENSAJE_SEED_GENERADA = "Seed aleatoria generada: ";
    public static final String UI_NOMBRE_ARCHIVO_EXPORT = "consola.log";
    public static final String UI_MENSAJE_CONSOLA_GUARDADA = "Consola guardada en: ";
    public static final String UI_ERROR_GUARDAR_CONSOLA = "No se pudo guardar la consola en: ";
    public static final String UI_ERROR_GUARDAR_CONSOLA_DIALOGO = "No se pudo guardar el archivo.";




    private Constantes() {}
}