package src.utilidades;

public interface LogListener {
    public void onLog(EntradaLog entrada); //Funcion ejecutar principal
    public void finalLote(); //Funcion al terminar de ejecutar todos los mensajes para hacer limpiezas 
                                //por ejemplo
    public void finalEjecucion(); //Cierra todo lo que necesite cerrar para ser eliminado
}

