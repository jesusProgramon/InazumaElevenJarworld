package inazuma;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria para el registro de errores en un archivo de log.
 * Proporciona métodos para registrar excepciones y mensajes de error con marca
 * de tiempo, escribiendo tanto en consola como en un archivo de log
 * persistente.
 *
 * @author jesus
 * @version 1.0
 */
public class LogErrores {

    private static final String LOG_FILE = "inazuma_errors.log";
    private static PrintWriter logWriter;

    // Bloque estático para inicializar el sistema de logging
    static {
        try {
            // Inicializar el writer en modo append
            logWriter = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO: No se pudo inicializar el logger: " + e.getMessage());
        }
    }

    /**
     * Registra una excepción en el log, incluyendo su mensaje y stack trace.
     *
     * @param e Excepción a registrar
     */
    public static void registrarError(Exception e) {
        registrarError(e.getMessage(), e);
    }

    /**
     * Registra un mensaje de error personalizado junto con una excepción
     * opcional. Genera una entrada con timestamp tanto en consola como en el
     * archivo de log.
     *
     * @param mensaje Mensaje descriptivo del error
     * @param e Excepción asociada al error (puede ser null)
     */
    public static void registrarError(String mensaje, Exception e) {
        if (logWriter == null) {
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = ahora.format(formatter);

        String logEntry = String.format("[%s] ERROR: %s", timestamp, mensaje);

        // Escribir en consola
        System.err.println(logEntry);

        // Escribir en archivo
        logWriter.println(logEntry);

        if (e != null) {
            // Escribir stack trace en el archivo
            e.printStackTrace(logWriter);
        }

        logWriter.flush(); // Forzar escritura inmediata
    }

    /**
     * Cierra el archivo de log de manera segura. Debe llamarse antes de
     * terminar la aplicación para liberar recursos.
     */
    public static void cerrarLogger() {
        if (logWriter != null) {
            logWriter.close();
        }
    }

}
