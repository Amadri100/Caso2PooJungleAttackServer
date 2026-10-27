package src.utilidades;
 
import java.time.LocalDateTime;

public class EntradaLog {
    private LocalDateTime momento;
    private NivelLog nivel;
    private String origen;
    private String mensaje;
    private Throwable causa;

    public EntradaLog(NivelLog nivel, String origen, String mensaje, Throwable causa) {
        this.momento = LocalDateTime.now();
        this.nivel = nivel;
        this.origen = origen;
        this.mensaje = mensaje;
        this.causa = causa;
    }
    public String getOrigen() {
        return origen;
    }
    public LocalDateTime getMomento() {
        return momento;
    }
    public String getMensaje() {
        return mensaje;
    }
    public NivelLog getNivel() {
        return nivel;
    }
    public Throwable getCausa() {
        return this.causa;
    }
    @Override 
    public String toString() {
        String texto = "";
        texto += getNivel().toString() + Constantes.SEPARADOR_ERROR;
        texto += getOrigen() + Constantes.ESPACIO_ENTRE_TEXTO;
        texto += getMensaje();
        if (getCausa() != null) {
            texto += Constantes.SEPARADOR_ERROR + getCausa().getMessage();
        }
        return texto;
    }
}
