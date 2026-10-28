package src.utilidades;

import java.net.ContentHandler;

public enum NivelLog {
    DEBUG,
    INFO,
    WARN,
    ERROR;

    @Override 
    public String toString() {
        String texto = Constantes.TEXTO_DEFECTO;
        switch (this) {
            case DEBUG:
                texto = Constantes.DEBUG;
                break;
            case ERROR:
                texto = Constantes.ERROR;
                break;
            case INFO:
                texto = Constantes.INFO;
                break;        
            case WARN:
                texto = Constantes.WARN;
                break;
        }
        return texto;
    } 
    

}