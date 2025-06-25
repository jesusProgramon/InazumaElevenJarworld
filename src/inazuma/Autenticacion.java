package inazuma;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase encargada de gestionar la autenticación de usuarios en el sistema.
 * Proporciona funcionalidad para inicio de sesión y creación de cuentas,
 * incluyendo autenticación de dos factores mediante correo electrónico. Utiliza
 * el algoritmo Argon2 para el hashing seguro de contraseñas.
 *
 * @author jesus
 * @version 1.0
 */
public class Autenticacion {

    // Constantes para configuración
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inazuma_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final int ARGON_ITERATIONS = 2;
    private static final int ARGON_MEMORY = 65536;
    private static final int ARGON_PARALLELISM = 1;

    /**
     * Gestiona el proceso de inicio de sesión de un usuario. Verifica las
     * credenciales, realiza autenticación 2FA y da acceso al menú principal del
     * juego si es exitoso.
     *
     * @param entrada Objeto Scanner para la entrada del usuario
     */
    public static void iniciarSesion(Scanner entrada) {
        System.out.println("\n--- Inicio de Sesión ---");
        System.out.print("Introduzca su usuario: ");
        String inUser = entrada.nextLine().trim();

        try (Connection conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Verificar existencia de usuario
            if (!existeUsuario(conexion, inUser)) {
                System.out.println("Usuario no existente");
                return;
            }

            // Obtener datos del usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(
                    "SELECT Contrasena, Correo FROM usuarios WHERE Usuario = ?")) {

                pstmt.setString(1, inUser);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("Contrasena");
                        String userEmail = rs.getString("Correo");

                        System.out.print("Introduce la contraseña: ");
                        char[] password = entrada.nextLine().toCharArray();

                        Argon2 argon2 = Argon2Factory.create();
                        try {
                            boolean verificationResult = argon2.verify(storedHash, password);
                            // Pequeño delay constante para prevenir timing attacks
                            try {
                                Thread.sleep(50); // 50ms delay
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                            if (!verificationResult) {
                                System.out.println("Credenciales inválidas");
                                return;
                            }

                            // Autenticación en dos pasos
                            if (!autenticacionDosPasos(entrada, userEmail)) {
                                System.out.println("Acceso denegado");
                                return;
                            }

                            System.out.println("\nBienvenido a Inazuma Eleven Jarworld!");
                            Juego.menuJuego(entrada);

                        } finally {
                            argon2.wipeArray(password);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Gestiona el proceso de creación de una nueva cuenta de usuario. Valida la
     * disponibilidad del nombre de usuario, almacena de forma segura la
     * contraseña, realiza autenticación de dos factores y da acceso al menú
     * principal si es exitoso.
     *
     * @param entrada Objeto Scanner para la entrada del usuario
     */
    public static void crearCuenta(Scanner entrada) {
        System.out.println("\n--- Creación de Cuenta ---");
        System.out.print("Elija su nombre de usuario: ");
        String newUser = entrada.nextLine().trim();

        try (Connection conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Verificar si el usuario ya existe
            if (existeUsuario(conexion, newUser)) {
                System.out.println("Usuario ya existente");
                return;
            }

            System.out.print("Elija una contraseña: ");
            char[] password = entrada.nextLine().toCharArray();

            Argon2 argon2 = Argon2Factory.create();
            String hash;
            try {
                hash = argon2.hash(ARGON_ITERATIONS, ARGON_MEMORY, ARGON_PARALLELISM, password);
            } finally {
                argon2.wipeArray(password);
            }

            System.out.print("Introduzca su correo electrónico: ");
            String newEmail = entrada.nextLine().trim();

            // Autenticación en dos pasos
            if (!autenticacionDosPasos(entrada, newEmail)) {
                System.out.println("Verificación fallida. Cuenta no creada.");
                return;
            }

            // Crear nuevo usuario
            try (PreparedStatement pstmt = conexion.prepareStatement(
                    "INSERT INTO usuarios (Usuario, Contrasena, Correo) VALUES (?, ?, ?)")) {

                pstmt.setString(1, newUser);
                pstmt.setString(2, hash);
                pstmt.setString(3, newEmail);
                pstmt.executeUpdate();

                System.out.println("Usuario creado correctamente\n");
                System.out.println("Bienvenido a Inazuma Eleven Jarworld!");
                Juego.menuJuego(entrada);
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Verifica si un nombre de usuario ya existe en la base de datos.
     *
     * @param conexion Conexión activa a la base de datos
     * @param usuario Nombre de usuario a verificar
     * @return true si el usuario existe, false de lo contrario
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private static boolean existeUsuario(Connection conexion, String usuario) throws SQLException {
        try (PreparedStatement pstmt = conexion.prepareStatement(
                "SELECT 1 FROM usuarios WHERE Usuario = ?")) {

            pstmt.setString(1, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Gestiona el proceso de autenticación de dos factores. Genera y envía un
     * código de verificación al correo del usuario, luego verifica el código
     * introducido por el usuario.
     *
     * @param entrada Objeto Scanner para la entrada del usuario
     * @param email Dirección de correo electrónico del usuario
     * @return true si la autenticación es exitosa, false de lo contrario
     */
    private static boolean autenticacionDosPasos(Scanner entrada, String email) {
        try {
            int verificationCode = (int) (Math.random() * 900000) + 100000;
            System.out.println("Enviando código de verificación a " + email + "...");

            EmailServer.sendVerificationCode(email, String.valueOf(verificationCode));
            System.out.println("Código enviado. Revise su correo.");

            System.out.print("Introduce el código recibido: ");
            String codigoStr = entrada.nextLine().trim();

            // Validar que sea un número
            int userCode;
            try {
                userCode = Integer.parseInt(codigoStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: El código debe ser un número");
                return false;
            }

            return EmailServer.verifyCode(email, String.valueOf(userCode));
        } catch (Exception e) {
            System.out.println("Error en verificación: " + e.getMessage());
            LogErrores.registrarError(e);
            return false;
        }
    }
}
