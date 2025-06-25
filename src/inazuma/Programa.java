package inazuma;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 *
 * Clase main donde se despliega el menú de autenticación
 *
 * @author Jesús
 * @version 1.0
 */
public class Programa {

    /**
     *
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        /**
         * Salida en UTF-8
         */
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        /**
         * Declaraciones necesarias
         */
        Scanner entrada = new Scanner(System.in);
        boolean salir = false;

        System.out.println("Bienvenido al mundo de Inazuma Eleven\n");

        /**
         * try catch con bucle principal del programa
         */
        try {
            while (!salir) {
                /**
                 * Menú de opciones para autenticarse
                 */
                System.out.println("Elige una opción:");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Crear cuenta");
                System.out.println("3. Salir");

                /**
                 * Se recoge la elección del usuario
                 */
                System.out.print("Opción: ");
                String opcionStr = entrada.nextLine().trim();

                /**
                 * Manejo de entrada vacía
                 */
                if (opcionStr.isEmpty()) {
                    System.out.println("Error: Debes introducir una opción\n");
                    continue;
                }

                /**
                 * Manejo de entrada con cadenas
                 */
                int opcion;
                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debes introducir un número válido\n");
                    continue;
                }

                /**
                 * Se ejecuta una acción dependiendo de la elección del usuario
                 */
                switch (opcion) {
                    case 1 ->
                        Autenticacion.iniciarSesion(entrada);
                    case 2 ->
                        Autenticacion.crearCuenta(entrada);
                    case 3 ->
                        salir = true;
                    default ->
                        System.out.println("Opción inválida. Intente nuevamente.\n");
                }
            }
        } catch (Exception e) {
            /**
             * Manejo de errores y registro en el log
             */
            System.out.println("Error inesperado: " + e.getMessage());
            LogErrores.registrarError(e);
        } finally {
            /**
             * Cerramos recursos
             */
            if (entrada != null) {
                entrada.close();
            }
            LogErrores.cerrarLogger();
        }
    }
}
