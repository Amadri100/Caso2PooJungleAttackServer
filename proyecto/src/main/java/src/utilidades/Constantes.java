package src.utilidades;

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
    public static final int PLAYER_DAMAGE = 10;              
    public static final int PLAYER_PACK_SIZE = 5;     
    public static final int BOT_INITIAL_ENERGY = 100;        
    public static final int BOT_DAMAGE = 8;                  
    public static final int BOT_PACK_SIZE = 5;        
    public static final int TOTAL_ZONES = 9;             
    public static final int TOTAL_COFFEE_CUPS = 10;          
    public static final int MOVIMIENTOS_MINIMOS_CAMBIAR = 3;
    public static final long INTERVALO_STATE_UPDATE_MS = 100L; 
    public static final long SEED_POR_DEFECTO = 12345L;   
    public static final String LOG_FILE = "juego.log";      
    public static final int INTERVALO_REFRESCO_UI_MS = 50;  
    public static final int MAX_LINEAS_CONSOLA = 200;     
    public static final int MAX_HISTORIAL_LOG = 1000;   
    public static final String CARACTERES_CODIGO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int MAX_COLA = 10000;
    public static final long TIEMPO_SLEEP_MINIMO = 20;

    public static final long MS_ESPERA_MAXIMA_COLA = 100;
    public static final long MS_ANTES_DE_MORRIR = 2000;
    public static final String NOMBRE_THREAD_LOGGER = "logger";
    public static final String RUTA_ARCHIVO = "Log.log";
    public static final String ORIGEN_SERVER = "Servidor";

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
    private Constantes() {}
}