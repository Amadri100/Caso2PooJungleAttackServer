package src.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

import src.controlador.Controlador;
import src.server.ServerConfig;
import src.server.Servidor;
import src.ui.VentanaServidor;
import src.utilidades.Constantes;
import src.utilidades.GeneradorAleatorio;
import src.utilidades.ListenerArchivo;
import src.utilidades.Logger;
import src.utilidades.NivelLog;

public class Main {

    // Orden de arranque: Logger -> GeneradorAleatorio -> Servidor -> Ventana -> Servidor.iniciar()
    public static void main(String[] args) {
        // 1. Logger (primero, para que todo lo que sigue quede registrado)
        Logger logger = Logger.getInstance();
        try {
            logger.agregarListener(new ListenerArchivo());
        } catch (IOException e) {
            // Sin archivo se sigue funcionando solo con la consola
            System.err.println(Constantes.MAIN_ERROR_ARCHIVO_LOG + Constantes.SEPARADOR_ERROR + e);
        }
        logger.iniciar();
        logger.crearLog(NivelLog.INFO, Constantes.ORIGEN_MAIN, Constantes.MAIN_MENSAJE_INICIO, null);

        // 2. GeneradorAleatorio (se inicializa con la seed por defecto)
        GeneradorAleatorio generador = GeneradorAleatorio.getInstance();
        logger.crearLog(NivelLog.INFO, Constantes.ORIGEN_MAIN,
                Constantes.MAIN_MENSAJE_SEED + generador.getSeed(), null);

        // 3. Servidor y Controlador (se enlazan entre si)
        Servidor servidor = new Servidor();
        Controlador controlador = new Controlador();
        controlador.setServidor(servidor);
        servidor.setControlador(controlador);

        // 4. Ventana (se crea y muestra en el hilo de Swing; se espera a que termine)
        try {
            SwingUtilities.invokeAndWait(() -> {
                VentanaServidor ventana = new VentanaServidor(controlador);
                ventana.setVisible(true);
                ventana.iniciarRefresco();
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            terminarConError(logger, Constantes.MAIN_ERROR_VENTANA, e);
            return;
        } catch (InvocationTargetException e) {
            terminarConError(logger, Constantes.MAIN_ERROR_VENTANA, e.getCause());
            return;
        }

        // 5. Servidor.iniciar() (empieza a aceptar conexiones)
        if (!servidor.iniciar()) {
            terminarConError(logger, Constantes.MAIN_ERROR_SERVIDOR, null);
            return;
        }
        logger.crearLog(NivelLog.INFO, Constantes.ORIGEN_MAIN,
                Constantes.MAIN_MENSAJE_ESCUCHANDO + ServerConfig.getInstance().getIp()
                        + Constantes.MAIN_SEPARADOR_PUERTO + ServerConfig.getInstance().getPuerto(),
                null);
    }

    private static void terminarConError(Logger logger, String descripcion, Throwable causa) {
        logger.crearLog(NivelLog.ERROR, Constantes.ORIGEN_MAIN, descripcion, causa);
        logger.cerrar(); // Vacia la cola del logger antes de salir
        System.exit(1);
    }
}