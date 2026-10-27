package src.utilidades;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ListenerArchivo implements LogListener {

    private final BufferedWriter escritor;

    public ListenerArchivo() throws IOException {
        Path archivo = Path.of(Constantes.RUTA_ARCHIVO);
        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        this.escritor = Files.newBufferedWriter(archivo,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @Override
    public void onLog(EntradaLog entrada) {
        try {
            escritor.write(entrada.toString());
            escritor.newLine();
        } catch (IOException e) {
            Logger.reportarError(e);
        }
    }

    // Termina un lote de mensajes: se vacía el buffer al disco
    @Override
    public void finalLote() {
        try {
            escritor.flush();
        } catch (IOException e) {
            Logger.reportarError(e);
        }
    }

    // Cierra el archivo; lo llama el Logger al cerrarse
    @Override
    public void finalEjecucion() {
        try {
            escritor.close();
        } catch (IOException e) {
            Logger.reportarError(e);
        }
    }
}