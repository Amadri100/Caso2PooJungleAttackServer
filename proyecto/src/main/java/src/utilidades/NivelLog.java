package src.utilidades;
 
public enum NivelLog {
    DEBUG,
    INFO,
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
        }
        return texto;
    } 
    

}