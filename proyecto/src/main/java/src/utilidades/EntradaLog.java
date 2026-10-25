package src.utilidades;
 
import java.time.LocalDateTime;

public class EntradaLog {
    private LocalDateTime momento;
    private NivelLog nivel;
    private String origen;
    private String mensaje;
    public EntradaLog(NivelLog nivel, String origen, String mensaje) {
        this.momento = LocalDateTime.now();
        this.nivel = nivel;
        this.origen = origen;
        this.mensaje = mensaje;
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
}
