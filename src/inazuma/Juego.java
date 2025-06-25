package inazuma;

import java.sql.DatabaseMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la lógica del juego y las operaciones de base de
 * datos.
 *
 * @author Jesús
 * @version 1.0
 */
public class Juego {

    /**
     * Constantes para la conexión a la base de datos
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inazuma_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    /**
     * Variables que se necesitan declarar
     */
    private static Connection conexion;
    private static Scanner entrada;

    /**
     * Muestra el menú principal del juego y gestiona las opciones
     * seleccionadas. Verifica la conexión a la base de datos y la existencia de
     * la tabla necesaria.
     *
     * @param scanner Objeto Scanner para la entrada de usuario
     */
    public static void menuJuego(Scanner scanner) {
        entrada = scanner;
        boolean salir = false;

        try {
            conexion = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            try {
                DatabaseMetaData meta = conexion.getMetaData(); // Usar la interfaz estándar
                try (ResultSet tables = meta.getTables(null, null, "equipos", new String[]{"TABLE"})) {
                    if (!tables.next()) {
                        System.out.println("ERROR: Base de datos no inicializada.");
                        return;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error verificando estructura de BD: " + e.getMessage());
                return;
            }

            while (!salir) {
                System.out.println("\nElige una opción:");
                System.out.println("1. Ver lista de equipos");
                System.out.println("2. Crear equipo");
                System.out.println("3. Crear jugador");
                System.out.println("4. Cerrar sesión");
                System.out.print("Opción: ");

                String opcionStr = entrada.nextLine().trim();

                if (opcionStr.isEmpty()) {
                    System.out.println("Error: Debes introducir una opción\n");
                    continue;
                }

                int opcion;
                try {
                    opcion = Integer.parseInt(opcionStr);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debes introducir un número válido\n");
                    continue;
                }

                switch (opcion) {
                    case 1 ->
                        listaEquipos();
                    case 2 ->
                        crearEquipo();
                    case 3 ->
                        crearJugador();
                    case 4 ->
                        salir = true;
                    default ->
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
            LogErrores.registrarError(e);
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                    LogErrores.registrarError(e);
                }
            }
        }
    }

    /**
     * Muestra la lista de equipos disponibles y permite seleccionar uno para
     * ver sus detalles.
     */
    private static void listaEquipos() {
        try {
            System.out.println("\n--- LISTA DE EQUIPOS ---");
            List<Equipo> equipos = new ArrayList<>();

            String sql = "SELECT * FROM equipos";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    equipos.add(new Equipo(
                            rs.getInt("ID"),
                            rs.getString("Nombre"),
                            rs.getString("Ciudad"),
                            rs.getString("País")
                    ));
                }
            }

            for (int i = 0; i < equipos.size(); i++) {
                Equipo e = equipos.get(i);
                System.out.println((i + 1) + ". " + e.getNombre() + " (" + e.getCiudad() + ")");
            }

            System.out.print("\nSelecciona un equipo (0 para volver): ");
            String seleccionStr = entrada.nextLine().trim();

            if (seleccionStr.isEmpty()) {
                return;
            }

            try {
                int seleccion = Integer.parseInt(seleccionStr);
                if (seleccion > 0 && seleccion <= equipos.size()) {
                    menuEquipo(equipos.get(seleccion - 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener equipos: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Muestra el menú de gestión para un equipo específico.
     *
     * @param equipo Equipo seleccionado por el usuario
     */
    private static void menuEquipo(Equipo equipo) {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- EQUIPO: " + equipo.getNombre().toUpperCase() + " ---");
            System.out.println("1. Ver detalles del equipo");
            System.out.println("2. Ver jugadores");
            System.out.println("3. Editar datos del equipo");
            System.out.println("4. Jugar un partido");
            System.out.println("5. Volver al menú principal");
            System.out.print("Elige una opción: ");

            String opcionStr = entrada.nextLine().trim();
            if (opcionStr.isEmpty()) {
                System.out.println("Error: Debes introducir una opción\n");
                continue;
            }

            int opcion;
            try {
                opcion = Integer.parseInt(opcionStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido\n");
                continue;
            }

            switch (opcion) {
                case 1 ->
                    verDetallesEquipo(equipo);
                case 2 ->
                    verJugadoresEquipo(equipo);
                case 3 ->
                    editarEquipo(equipo);
                case 4 ->
                    jugarPartido(equipo);
                case 5 ->
                    volver = true;
                default ->
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }
    }

    /**
     * Muestra los detalles básicos de un equipo.
     *
     * @param equipo Equipo cuyos detalles se mostrarán
     */
    private static void verDetallesEquipo(Equipo equipo) {
        System.out.println("\n--- DETALLES DEL EQUIPO ---");
        System.out.println("Nombre: " + equipo.getNombre());
        System.out.println("Ciudad: " + equipo.getCiudad());
        System.out.println("País: " + equipo.getPais());
        System.out.println("Presiona Enter para continuar...");
        entrada.nextLine();
    }

    /**
     * Muestra la lista de jugadores de un equipo y permite seleccionar uno para
     * ver sus detalles.
     *
     * @param equipo Equipo del cual se listarán los jugadores
     */
    private static void verJugadoresEquipo(Equipo equipo) {
        try {
            System.out.println("\n--- JUGADORES DE " + equipo.getNombre().toUpperCase() + " ---");
            List<Jugador> jugadores = new ArrayList<>();

            String sql = "SELECT * FROM jugadores WHERE ID_equipo = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, equipo.getId());
                try (ResultSet rs = pstmt.executeQuery()) {

                    System.out.println("Dorsal\tNombre");
                    System.out.println("----------------");
                    while (rs.next()) {
                        int dorsal = rs.getInt("Dorsal");
                        String nombre = rs.getString("Nombre");
                        System.out.println(dorsal + "\t" + nombre);

                        jugadores.add(new Jugador(
                                rs.getInt("ID"),
                                nombre,
                                equipo.getId(),
                                dorsal,
                                rs.getString("Posición"),
                                rs.getString("PosiciónSecundaria"),
                                rs.getInt("Habilidad"),
                                rs.getString("Afinidad"),
                                rs.getString("SupertécnicaPrincipal"),
                                rs.getString("SupertécnicaSecundaria"),
                                rs.getString("Nacionalidad")
                        ));
                    }
                }
            }

            System.out.print("\nIntroduce el dorsal del jugador para ver detalles (0 para volver): ");
            String dorsalStr = entrada.nextLine().trim();

            if (dorsalStr.isEmpty()) {
                return;
            }

            try {
                int dorsalSeleccionado = Integer.parseInt(dorsalStr);
                if (dorsalSeleccionado != 0) {
                    for (Jugador jugador : jugadores) {
                        if (jugador.getDorsal() == dorsalSeleccionado) {
                            menuJugador(jugador, equipo);
                            return;
                        }
                    }
                    System.out.println("No se encontró jugador con dorsal " + dorsalSeleccionado);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: El dorsal debe ser un número válido");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener jugadores: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Muestra el menú de gestión para un jugador específico.
     *
     * @param jugador Jugador seleccionado por el usuario
     * @param equipo Equipo actual del jugador
     */
    private static void menuJugador(Jugador jugador, Equipo equipo) {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n--- JUGADOR: " + jugador.getNombre().toUpperCase() + " ---");
            System.out.println("1. Ver detalles completos");
            System.out.println("2. Transferir a otro equipo");
            System.out.println("3. Editar jugador");
            System.out.println("4. Volver al menú anterior");
            System.out.print("Elige una opción: ");

            String opcionStr = entrada.nextLine().trim();
            if (opcionStr.isEmpty()) {
                System.out.println("Error: Debes introducir una opción\n");
                continue;
            }

            int opcion;
            try {
                opcion = Integer.parseInt(opcionStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido\n");
                continue;
            }

            switch (opcion) {
                case 1 ->
                    verDetallesJugador(jugador);
                case 2 ->
                    transferirJugador(jugador, equipo);
                case 3 ->
                    editarJugador(jugador);
                case 4 ->
                    volver = true;
                default ->
                    System.out.println("Opción no válida");
            }
        }
    }

    /**
     * Permite editar los atributos de un jugador existente.
     *
     * @param jugador Jugador que se editará
     */
    private static void editarJugador(Jugador jugador) {
        try {
            System.out.println("\n--- EDITAR JUGADOR ---");

            // Nombre
            System.out.println("Nombre actual: " + jugador.getNombre());
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nuevoNombre = entrada.nextLine();

            // Dorsal
            System.out.println("Dorsal actual: " + jugador.getDorsal());
            System.out.print("Nuevo dorsal (1-99) (Enter para mantener): ");
            String dorsalStr = entrada.nextLine();
            int nuevoDorsal = dorsalStr.isEmpty() ? jugador.getDorsal() : Integer.parseInt(dorsalStr);
            if (nuevoDorsal < 1 || nuevoDorsal > 99) {
                System.out.println("Error: El dorsal debe estar entre 1 y 99. Se mantendrá el actual.");
                nuevoDorsal = jugador.getDorsal();
            }

            // Posición principal
            System.out.println("Posición principal actual: " + jugador.getPosicion());
            System.out.print("Nueva posición principal (Enter para mantener): ");
            String nuevaPosicion = entrada.nextLine();

            // Posición secundaria
            System.out.println("Posición secundaria actual: " + jugador.getPosicionSec());
            System.out.print("Nueva posición secundaria (Enter para mantener): ");
            String nuevaPosicionSec = entrada.nextLine();

            // Habilidad
            System.out.println("Habilidad actual: " + jugador.getHabilidad());
            System.out.print("Nueva habilidad (1-100) (Enter para mantener): ");
            String habilidadStr = entrada.nextLine();
            int nuevaHabilidad = habilidadStr.isEmpty() ? jugador.getHabilidad() : Integer.parseInt(habilidadStr);
            if (nuevaHabilidad < 1 || nuevaHabilidad > 100) {
                System.out.println("Error: La habilidad debe estar entre 1 y 100. Se mantendrá el valor actual.");
                nuevaHabilidad = jugador.getHabilidad();
            }

            // Afinidad
            System.out.println("Afinidad actual: " + jugador.getAfinidad());
            System.out.print("Nueva afinidad (Fuego, Aire, Montaña, Bosque, Neutro) (Enter para mantener): ");
            String nuevaAfinidad = entrada.nextLine();

            // Técnica principal
            System.out.println("Técnica principal actual: " + jugador.getTecnica());
            System.out.print("Nueva técnica principal (Enter para mantener): ");
            String nuevaTecnica = entrada.nextLine();

            // Técnica secundaria
            System.out.println("Técnica secundaria actual: " + jugador.getTecnicaSec());
            System.out.print("Nueva técnica secundaria (Enter para mantener): ");
            String nuevaTecnicaSec = entrada.nextLine();

            // Nacionalidad
            System.out.println("Nacionalidad actual: " + jugador.getNacionalidad());
            System.out.print("Nueva nacionalidad (Enter para mantener): ");
            String nuevaNacionalidad = entrada.nextLine();

            // Actualizar objeto jugador
            if (!nuevoNombre.isEmpty()) {
                jugador.setNombre(nuevoNombre);
            }
            jugador.setDorsal(nuevoDorsal);
            if (!nuevaPosicion.isEmpty()) {
                jugador.setPosicion(nuevaPosicion);
            }
            if (!nuevaPosicionSec.isEmpty()) {
                jugador.setPosicionSec(nuevaPosicionSec);
            }
            jugador.setHabilidad(nuevaHabilidad);
            if (!nuevaAfinidad.isEmpty()) {
                jugador.setAfinidad(nuevaAfinidad);
            }
            if (!nuevaTecnica.isEmpty()) {
                jugador.setTecnica(nuevaTecnica);
            }
            if (!nuevaTecnicaSec.isEmpty()) {
                jugador.setTecnicaSec(nuevaTecnicaSec);
            }
            if (!nuevaNacionalidad.isEmpty()) {
                jugador.setNacionalidad(nuevaNacionalidad);
            }

            // Verificar si el nuevo dorsal está disponible
            if (nuevoDorsal != jugador.getDorsal() && !dorsalDisponible(jugador.getIdEquipo(), nuevoDorsal, jugador.getId())) {
                System.out.println("Error: El dorsal " + nuevoDorsal + " ya está en uso. No se realizaron cambios.");
                return;
            }

            // Actualizar base de datos
            String sql = "UPDATE jugadores SET "
                    + "Nombre = ?, "
                    + "Dorsal = ?, "
                    + "Posición = ?, "
                    + "PosiciónSecundaria = ?, "
                    + "Habilidad = ?, "
                    + "Afinidad = ?, "
                    + "SupertécnicaPrincipal = ?, "
                    + "SupertécnicaSecundaria = ?, "
                    + "Nacionalidad = ? "
                    + "WHERE ID = ?";

            // Prepared Statement para ejecutar la sentencia evitando inyección SQL
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, jugador.getNombre());
                pstmt.setInt(2, jugador.getDorsal());
                pstmt.setString(3, jugador.getPosicion());
                pstmt.setString(4, jugador.getPosicionSec());
                pstmt.setInt(5, jugador.getHabilidad());
                pstmt.setString(6, jugador.getAfinidad());
                pstmt.setString(7, jugador.getTecnica());
                pstmt.setString(8, jugador.getTecnicaSec());
                pstmt.setString(9, jugador.getNacionalidad());
                pstmt.setInt(10, jugador.getId());

                int filasActualizadas = pstmt.executeUpdate();
                if (filasActualizadas > 0) {
                    System.out.println("¡Jugador actualizado correctamente!");
                } else {
                    System.out.println("No se pudo actualizar el jugador.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar jugador: " + e.getMessage());
            LogErrores.registrarError(e);
        } catch (NumberFormatException e) {
            System.out.println("Error: El dorsal y habilidad deben ser números enteros válidos");
        }
    }

    /**
     * Verifica la disponibilidad de un dorsal en un equipo.
     *
     * @param idEquipo ID del equipo donde se verificará el dorsal
     * @param dorsal Dorsal a verificar
     * @param idJugador ID del jugador que se está editando (para excluirlo en
     * la verificación)
     * @return true si el dorsal está disponible, false si ya está en uso
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private static boolean dorsalDisponible(int idEquipo, int dorsal, int idJugador) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM jugadores WHERE ID_equipo = ? AND Dorsal = ? AND ID != ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipo);
            pstmt.setInt(2, dorsal);
            pstmt.setInt(3, idJugador);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") == 0;
                }
            }
        }
        return false;
    }

    /**
     * Transfiere un jugador a otro equipo seleccionado.
     *
     * @param jugador Jugador a transferir
     * @param equipoActual Equipo actual del jugador
     */
    private static void transferirJugador(Jugador jugador, Equipo equipoActual) {
        try {
            System.out.println("\n--- TRANSFERIR JUGADOR ---");
            List<Equipo> equipos = new ArrayList<>();

            String sqlEquipos = "SELECT * FROM equipos WHERE ID != ?";
            try (PreparedStatement pstmtEquipos = conexion.prepareStatement(sqlEquipos)) {
                pstmtEquipos.setInt(1, equipoActual.getId());
                try (ResultSet rs = pstmtEquipos.executeQuery()) {

                    System.out.println("Equipos disponibles:");
                    int i = 1;
                    while (rs.next()) {
                        Equipo e = new Equipo(
                                rs.getInt("ID"),
                                rs.getString("Nombre"),
                                rs.getString("Ciudad"),
                                rs.getString("País")
                        );
                        equipos.add(e);
                        System.out.println(i++ + ". " + e.getNombre());
                    }
                }
            }

            System.out.print("Selecciona equipo destino (0 para cancelar): ");
            String seleccionStr = entrada.nextLine().trim();

            if (seleccionStr.isEmpty()) {
                return;
            }

            try {
                int seleccion = Integer.parseInt(seleccionStr);
                if (seleccion == 0) {
                    System.out.println("Transferencia cancelada.");
                    return;
                }

                if (seleccion > 0 && seleccion <= equipos.size()) {
                    Equipo equipoDestino = equipos.get(seleccion - 1);
                    int nuevoDorsal = encontrarDorsalDisponible(equipoDestino.getId());

                    if (nuevoDorsal == -1) {
                        System.out.println("No hay dorsales disponibles en el equipo destino");
                        return;
                    }

                    String sqlUpdate = "UPDATE jugadores SET ID_equipo = ?, Dorsal = ? WHERE ID = ?";
                    try (PreparedStatement pstmtUpdate = conexion.prepareStatement(sqlUpdate)) {
                        pstmtUpdate.setInt(1, equipoDestino.getId());
                        pstmtUpdate.setInt(2, nuevoDorsal);
                        pstmtUpdate.setInt(3, jugador.getId());
                        pstmtUpdate.executeUpdate();
                    }

                    jugador.setIdEquipo(equipoDestino.getId());
                    jugador.setDorsal(nuevoDorsal);

                    System.out.println("¡" + jugador.getNombre() + " transferido a " + equipoDestino.getNombre()
                            + " con el dorsal " + nuevoDorsal + "!");
                } else {
                    System.out.println("Selección inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido");
            }

        } catch (SQLException e) {
            System.out.println("Error en transferencia: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Encuentra el primer dorsal disponible en un equipo.
     *
     * @param idEquipo ID del equipo donde se buscará el dorsal disponible
     * @return Número de dorsal disponible, o -1 si no hay dorsales libres
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private static int encontrarDorsalDisponible(int idEquipo) throws SQLException {
        String sql = "SELECT Dorsal FROM jugadores WHERE ID_equipo = ? ORDER BY Dorsal";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipo);
            try (ResultSet rs = pstmt.executeQuery()) {

                int dorsalEsperado = 1;
                while (rs.next()) {
                    int dorsalActual = rs.getInt("Dorsal");
                    if (dorsalActual > dorsalEsperado) {
                        return dorsalEsperado;
                    }
                    dorsalEsperado++;
                }
                return (dorsalEsperado <= 99) ? dorsalEsperado : -1;
            }
        }
    }

    /**
     * Muestra todos los atributos de un jugador.
     *
     * @param jugador Jugador cuyos detalles se mostrarán
     */
    private static void verDetallesJugador(Jugador jugador) {
        System.out.println("\n--- DETALLES COMPLETOS ---");
        System.out.println("Nombre: " + jugador.getNombre());
        System.out.println("Dorsal: " + jugador.getDorsal());
        System.out.println("Posición principal: " + jugador.getPosicion());
        System.out.println("Posición secundaria: " + jugador.getPosicionSec());
        System.out.println("Habilidad: " + jugador.getHabilidad());
        System.out.println("Afinidad: " + jugador.getAfinidad());
        System.out.println("Técnica principal: " + jugador.getTecnica());
        System.out.println("Técnica secundaria: " + jugador.getTecnicaSec());
        System.out.println("Nacionalidad: " + jugador.getNacionalidad());
        System.out.println("Presiona Enter para continuar...");
        entrada.nextLine();
    }

    /**
     * Permite editar los atributos de un equipo existente.
     *
     * @param equipo Equipo que se editará
     */
    private static void editarEquipo(Equipo equipo) {
        try {
            System.out.println("\n--- EDITAR EQUIPO ---");
            System.out.println("Nombre actual: " + equipo.getNombre());
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nuevoNombre = entrada.nextLine();

            System.out.println("Ciudad actual: " + equipo.getCiudad());
            System.out.print("Nueva ciudad (Enter para mantener): ");
            String nuevaCiudad = entrada.nextLine();

            System.out.println("País actual: " + equipo.getPais());
            System.out.print("Nuevo país (Enter para mantener): ");
            String nuevoPais = entrada.nextLine();

            // Actualizar objeto
            if (!nuevoNombre.isEmpty()) {
                equipo.setNombre(nuevoNombre);
            }
            if (!nuevaCiudad.isEmpty()) {
                equipo.setCiudad(nuevaCiudad);
            }
            if (!nuevoPais.isEmpty()) {
                equipo.setPais(nuevoPais);
            }

            // Actualizar base de datos
            String sql = "UPDATE equipos SET Nombre = ?, Ciudad = ?, País = ? WHERE ID = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, equipo.getNombre());
                pstmt.setString(2, equipo.getCiudad());
                pstmt.setString(3, equipo.getPais());
                pstmt.setInt(4, equipo.getId());
                pstmt.executeUpdate();
            }

            System.out.println("¡Equipo actualizado correctamente!");

        } catch (SQLException e) {
            System.out.println("Error al actualizar equipo: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Cuenta la cantidad de jugadores en un equipo específico.
     *
     * @param idEquipo ID del equipo a verificar
     * @return Cantidad de jugadores en el equipo
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private static int contarJugadoresEnEquipo(int idEquipo) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM jugadores WHERE ID_equipo = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    /**
     * Simula un partido entre el equipo local y un equipo visitante
     * seleccionado. Calcula probabilidades de victoria y genera un resultado
     * aleatorio.
     *
     * @param equipoLocal Equipo que juega como local
     */
    private static void jugarPartido(Equipo equipoLocal) {
        try {
            System.out.println("\n--- JUGAR PARTIDO ---");
            System.out.println("Local: " + equipoLocal.getNombre());
            List<Equipo> equiposVisitantes = new ArrayList<>();

            String sql = "SELECT * FROM equipos WHERE ID != ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, equipoLocal.getId());
                try (ResultSet rs = pstmt.executeQuery()) {

                    int index = 1;
                    while (rs.next()) {
                        equiposVisitantes.add(new Equipo(
                                rs.getInt("ID"),
                                rs.getString("Nombre"),
                                rs.getString("Ciudad"),
                                rs.getString("País")
                        ));
                        System.out.println(index + ". " + rs.getString("Nombre"));
                        index++;
                    }
                }
            }

            System.out.print("Elige el equipo visitante (0 para cancelar): ");
            String opcionStr = entrada.nextLine().trim();

            if (opcionStr.isEmpty()) {
                System.out.println("Partido cancelado.");
                return;
            }

            try {
                int opcion = Integer.parseInt(opcionStr);
                if (opcion == 0) {
                    System.out.println("Partido cancelado.");
                    return;
                }

                if (opcion < 1 || opcion > equiposVisitantes.size()) {
                    System.out.println("Selección inválida.");
                    return;
                }

                Equipo equipoVisitante = equiposVisitantes.get(opcion - 1);
                System.out.println("Visitante: " + equipoVisitante.getNombre());

                // Verificar que ambos equipos tengan jugadores
                int jugadoresLocal = contarJugadoresEnEquipo(equipoLocal.getId());
                int jugadoresVisitante = contarJugadoresEnEquipo(equipoVisitante.getId());

                if (jugadoresLocal <= 6) {
                    System.out.println("\nERROR: El equipo local no tiene suficientes jugadores. No se puede jugar el partido.");
                    System.out.println("Cada equipo debe tener al menos 7 jugadores para poder jugar.");
                    System.out.println("\nPresiona Enter para continuar...");
                    entrada.nextLine();
                    return;
                }

                if (jugadoresVisitante <= 6) {
                    System.out.println("\nERROR: El equipo visitante no tiene suficientes jugadores. No se puede jugar el partido.");
                    System.out.println("Cada equipo debe tener al menos 7 jugadores para poder jugar.");
                    System.out.println("\nPresiona Enter para continuar...");
                    entrada.nextLine();
                    return;
                }

                // Calcular media de cada equipo
                double mediaLocal = calcularMediaEquipo(equipoLocal.getId());
                double mediaVisitante = calcularMediaEquipo(equipoVisitante.getId());

                System.out.printf("\nHabilidad media de %s: %.2f\n", equipoLocal.getNombre(), mediaLocal);
                System.out.printf("Habilidad media de %s: %.2f\n", equipoVisitante.getNombre(), mediaVisitante);

                // Calcular probabilidades
                double diferencia = mediaLocal - mediaVisitante;
                double porcentajeLocal = 50 + (diferencia / 2);
                double porcentajeVisitante = 50 - (diferencia / 2);

                // Ajustar para que no sea menor a 5% ni mayor a 95%
                porcentajeLocal = Math.max(5, Math.min(95, porcentajeLocal));
                porcentajeVisitante = Math.max(5, Math.min(95, porcentajeVisitante));

                // Normalizar para que sumen 100%
                double suma = porcentajeLocal + porcentajeVisitante;
                porcentajeLocal = (porcentajeLocal / suma) * 100;
                porcentajeVisitante = (porcentajeVisitante / suma) * 100;

                System.out.printf("\nProbabilidades de victoria:\n%s: %.1f%%\n%s: %.1f%%\n",
                        equipoLocal.getNombre(), porcentajeLocal,
                        equipoVisitante.getNombre(), porcentajeVisitante);

                // Simular resultado
                double random = Math.random() * 100;
                int golesLocal = 0;
                int golesVisitante = 0;
                String ganador = "";

                // Determinar resultado basado en probabilidades
                if (random < porcentajeLocal) {
                    ganador = equipoLocal.getNombre();
                    // El equipo local gana - máximo 6 goles
                    golesLocal = 1 + (int) (Math.random() * 6);
                    golesVisitante = (int) (Math.random() * golesLocal);
                } else {
                    ganador = equipoVisitante.getNombre();
                    // El equipo visitante gana - máximo 6 goles
                    golesVisitante = 1 + (int) (Math.random() * 6);
                    golesLocal = (int) (Math.random() * golesVisitante);
                }

                // Ajustes especiales para resultados típicos
                // 1. Probabilidad de empate (15% de los casos)
                if (Math.random() < 0.15) {
                    golesLocal = (int) (Math.random() * 7);  // 0-6
                    golesVisitante = golesLocal;
                    ganador = "Empate";
                } // 2. Resultados comunes (30% de los casos que no son empate)
                else if (Math.random() < 0.3) {
                    int[][] resultados = {
                        {1, 0}, {0, 1}, {1, 1}, {2, 1}, {1, 2}, {2, 0}, {0, 2},
                        {3, 0}, {0, 3}, {3, 1}, {1, 3}, {2, 2}, {3, 2}, {2, 3},
                        {4, 0}, {0, 4}, {4, 1}, {1, 4}, {4, 2}, {2, 4}
                    };
                    int idx = (int) (Math.random() * resultados.length);
                    golesLocal = resultados[idx][0];
                    golesVisitante = resultados[idx][1];

                    // Asegurar consistencia con el ganador
                    if (golesLocal > golesVisitante) {
                        ganador = equipoLocal.getNombre();
                    } else if (golesLocal < golesVisitante) {
                        ganador = equipoVisitante.getNombre();
                    } else {
                        ganador = "Empate";
                    }
                }

                // Anunciar resultado
                System.out.println("\n--- RESULTADO FINAL ---");
                System.out.println(equipoLocal.getNombre() + " " + golesLocal + " - "
                        + golesVisitante + " " + equipoVisitante.getNombre());

                if (ganador.equals("Empate")) {
                    System.out.println("¡El partido terminó en empate!");
                } else {
                    System.out.println("¡" + ganador + " es el ganador!");
                }

                System.out.println("\nPresiona Enter para continuar...");
                entrada.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido");
            }
        } catch (SQLException e) {
            System.out.println("Error al jugar partido: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Calcula la habilidad promedio de los jugadores de un equipo.
     *
     * @param idEquipo ID del equipo a calcular
     * @return Valor promedio de habilidad del equipo
     * @throws SQLException Si ocurre un error al acceder a la base de datos
     */
    private static double calcularMediaEquipo(int idEquipo) throws SQLException {
        String sql = "SELECT AVG(Habilidad) as media FROM jugadores WHERE ID_equipo = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("media");
                }
            }
        }
        return 0;
    }

    /**
     * Crea un nuevo equipo con los datos proporcionados por el usuario.
     */
    public static void crearEquipo() {
        try {
            System.out.println("\n--- CREAR NUEVO EQUIPO ---");
            System.out.print("Nombre del equipo: ");
            String nombre = entrada.nextLine();

            System.out.print("Ciudad: ");
            String ciudad = entrada.nextLine();

            System.out.print("País: ");
            String pais = entrada.nextLine();

            String sql = "INSERT INTO equipos (Nombre, Ciudad, País) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, ciudad);
                pstmt.setString(3, pais);
                pstmt.executeUpdate();
            }

            System.out.println("¡Equipo creado exitosamente!");

        } catch (SQLException e) {
            System.out.println("Error al crear equipo: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }

    /**
     * Crea un nuevo jugador con los datos proporcionados por el usuario. Asigna
     * automáticamente un dorsal disponible en el equipo seleccionado.
     */
    public static void crearJugador() {
        try {
            System.out.println("\n--- CREAR NUEVO JUGADOR ---");
            List<Equipo> equipos = new ArrayList<>();

            String sqlEquipos = "SELECT * FROM equipos";
            try (PreparedStatement pstmtEquipos = conexion.prepareStatement(sqlEquipos); ResultSet rsEquipos = pstmtEquipos.executeQuery()) {

                System.out.println("Selecciona un equipo para el jugador:");
                int i = 1;
                while (rsEquipos.next()) {
                    Equipo e = new Equipo(
                            rsEquipos.getInt("ID"),
                            rsEquipos.getString("Nombre"),
                            rsEquipos.getString("Ciudad"),
                            rsEquipos.getString("País")
                    );
                    equipos.add(e);
                    System.out.println(i + ". " + e.getNombre());
                    i++;
                }
            }

            System.out.print("Elige un equipo (0 para cancelar): ");
            String seleccionStr = entrada.nextLine().trim();

            if (seleccionStr.isEmpty()) {
                System.out.println("Creación cancelada.");
                return;
            }

            try {
                int seleccionEquipo = Integer.parseInt(seleccionStr);
                if (seleccionEquipo == 0) {
                    System.out.println("Creación cancelada.");
                    return;
                }

                if (seleccionEquipo < 1 || seleccionEquipo > equipos.size()) {
                    System.out.println("Selección inválida.");
                    return;
                }

                Equipo equipoSeleccionado = equipos.get(seleccionEquipo - 1);
                int dorsal = encontrarDorsalDisponible(equipoSeleccionado.getId());

                if (dorsal == -1) {
                    System.out.println("No hay dorsales disponibles en este equipo.");
                    return;
                }

                // Recoger datos del nuevo jugador
                System.out.println("\nDorsal asignado automáticamente: " + dorsal);
                System.out.print("Nombre del jugador: ");
                String nombre = entrada.nextLine();

                System.out.print("Posición principal: ");
                String posicion = entrada.nextLine();

                System.out.print("Posición secundaria (opcional): ");
                String posicionSec = entrada.nextLine();
                if (posicionSec.isEmpty()) {
                    posicionSec = null;
                }

                int habilidad = 0;
                boolean habilidadValida = false;
                while (!habilidadValida) {
                    System.out.print("Habilidad (1-100): ");
                    String habilidadStr = entrada.nextLine();
                    try {
                        habilidad = Integer.parseInt(habilidadStr);
                        if (habilidad >= 1 && habilidad <= 100) {
                            habilidadValida = true;
                        } else {
                            System.out.println("Error: La habilidad debe estar entre 1 y 100");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Debes introducir un número válido");
                    }
                }

                System.out.print("Afinidad (Fuego, Aire, Montaña, Bosque, Neutro): ");
                String afinidad = entrada.nextLine();

                System.out.print("Técnica principal (opcional): ");
                String tecnica = entrada.nextLine();
                if (tecnica.isEmpty()) {
                    tecnica = null;
                }

                System.out.print("Técnica secundaria (opcional): ");
                String tecnicaSec = entrada.nextLine();
                if (tecnicaSec.isEmpty()) {
                    tecnicaSec = null;
                }

                System.out.print("Nacionalidad: ");
                String nacionalidad = entrada.nextLine();

                // Insertar en la base de datos
                String sql = "INSERT INTO jugadores (Nombre, ID_equipo, Dorsal, Posición, PosiciónSecundaria, "
                        + "Habilidad, Afinidad, SupertécnicaPrincipal, SupertécnicaSecundaria, Nacionalidad) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setInt(2, equipoSeleccionado.getId());
                    pstmt.setInt(3, dorsal);
                    pstmt.setString(4, posicion);
                    pstmt.setString(5, posicionSec);
                    pstmt.setInt(6, habilidad);
                    pstmt.setString(7, afinidad);
                    pstmt.setString(8, tecnica);
                    pstmt.setString(9, tecnicaSec);
                    pstmt.setString(10, nacionalidad);
                    pstmt.executeUpdate();
                }

                System.out.println("¡Jugador creado exitosamente con el dorsal " + dorsal + "!");

            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear jugador: " + e.getMessage());
            LogErrores.registrarError(e);
        }
    }
}
