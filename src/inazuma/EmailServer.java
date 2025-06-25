package inazuma;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Clase encargada del envío y verificación de códigos de autenticación por
 * correo electrónico. Gestiona la comunicación con el servidor SMTP de Google
 * para enviar códigos de verificación y almacena temporalmente estos códigos
 * con su tiempo de expiración.
 *
 * @author jesus
 * @version 1.0
 */
public class EmailServer {

    // Credenciales
    private static final String FROM_EMAIL = "jesusmdmmoliner@gmail.com";
    private static String appPassword = null;

    // Bloque estático para inicialización segura de credenciales
    static {
        String envPassword = System.getenv("EMAIL_APP_PASSWORD");
        if (envPassword != null) {
            appPassword = envPassword;
        } else {
            try (FileInputStream fis = new FileInputStream("config.properties")) {
                Properties config = new Properties();
                config.load(fis);
                appPassword = config.getProperty("email.app.password");
            } catch (IOException e) {
                throw new RuntimeException("Error cargando configuración", e);
            }
        }

        if (appPassword == null || appPassword.isEmpty()) {
            throw new RuntimeException("Falta configuración de email");
        }
    }

    // Hashmap de los códigos de verificación
    private static final Map<String, CodeData> verificationCodes = new HashMap<>();

    /**
     * Envía un código de verificación al correo electrónico especificado.
     * Configura la sesión SMTP con las propiedades necesarias y gestiona el
     * envío.
     *
     * @param recipientEmail Dirección de correo del destinatario
     * @param code Código de verificación a enviar
     * @throws RuntimeException Si ocurre un error durante el envío del correo
     */
    public static void sendVerificationCode(String recipientEmail, String code) {

        // Almacenar código antes de enviar el correo
        storeVerificationCode(recipientEmail, code);

        // Objeto props con las característicaas de smtp de Google
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Se crea una nueva sesión
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, appPassword);
            }
        });

        try {
            // Se envía el mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject("Código de Verificación - Inazuma Eleven Jarworld");
            message.setText("Tu código de verificación es: " + code
                    + "\n\nEste código expira en 5 minutos.");

            Transport.send(message);
            System.out.println("Correo enviado exitosamente");
        } catch (MessagingException e) {
            // Manejo detallado de errores
            System.err.println("Error SMTP detallado:");
            e.printStackTrace();

            if (e instanceof AuthenticationFailedException) {
                System.err.println("Error de autenticación. Comprueba la configuración.");
            } else if (e instanceof SendFailedException) {
                System.err.println("Error de envío: Dirección inválida: " + recipientEmail);
            }

            throw new RuntimeException("Error al enviar el código de 2FA: " + e.getMessage());
        }
    }

    /**
     * Clase interna para almacenar códigos de verificación con su tiempo de
     * creación. Proporciona funcionalidad para verificar la validez del código
     * basado en el tiempo.
     */
    static class CodeData {

        String code;
        long timestamp;

        /**
         * Constructor que inicializa el código y registra el tiempo de
         * creación.
         *
         * @param code Código de verificación a almacenar
         */
        CodeData(String code) {
            this.code = code;
            this.timestamp = System.currentTimeMillis();
        }

        /**
         * Verifica si el código sigue siendo válido (no ha expirado).
         *
         * @return true si el código es válido (menos de 5 minutos), false de lo
         * contrario
         */
        boolean isValid() {
            return (System.currentTimeMillis() - timestamp) < 300000;
        }
    }

    /**
     * Almacena un código de verificación asociado a un email con marca de
     * tiempo.
     *
     * @param email Dirección de correo asociada al código
     * @param code Código de verificación a almacenar
     */
    public static void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, new CodeData(code));
    }

    /**
     * Verifica si un código es válido para un email dado. Comprueba que el
     * código coincida y no haya expirado.
     *
     * @param email Dirección de correo a verificar
     * @param code Código proporcionado por el usuario
     * @return true si el código es válido, false de lo contrario
     */
    public static boolean verifyCode(String email, String code) {
        CodeData data = verificationCodes.get(email);
        return data != null && data.isValid() && data.code.equals(code);
    }

}
