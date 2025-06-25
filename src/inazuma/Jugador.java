package inazuma;

/**
 * Clase que representa a un jugador de fútbol con superpoderes inusuales y con
 * la capacidad de recorrer el campo en 2 segundos, o en 40 minutos. Esto es una
 * plantilla para crear objetos. Contiene información detallada sobre sus
 * características, habilidades y atributos deportivos. Incluye validaciones en
 * los setters para garantizar la integridad de los datos.
 *
 * @author jesus
 * @version 1.0
 */
public class Jugador {

    // Atributos
    private int id;
    private int idEquipo;
    private String nombre;
    private int dorsal;
    private String posicion;
    private String posicionSec;
    private int habilidad;
    private String afinidad;
    private String tecnica;
    private String tecnicaSec;
    private String nacionalidad;

    /**
     * Constructor que inicializa un jugador con todos sus atributos.
     *
     * @param id Identificador único del jugador
     * @param nombre Nombre completo del jugador
     * @param idEquipo ID del equipo al que pertenece el jugador
     * @param dorsal Número de dorsal (1-99)
     * @param posicion Posición principal en el campo
     * @param posicionSec Posición secundaria (opcional)
     * @param habilidad Nivel de habilidad (1-100)
     * @param afinidad Elemento de afinidad (Fuego, Aire, Montaña, Bosque,
     * Neutro)
     * @param tecnica Técnica especial principal
     * @param tecnicaSec Técnica especial secundaria (opcional)
     * @param nacionalidad Nacionalidad del jugador
     */
    public Jugador(int id, String nombre, int idEquipo, int dorsal, String posicion, String posicionSec, int habilidad, String afinidad, String tecnica, String tecnicaSec, String nacionalidad) {
        this.id = id;
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.posicionSec = posicionSec;
        this.habilidad = habilidad;
        this.afinidad = afinidad;
        this.tecnica = tecnica;
        this.tecnicaSec = tecnicaSec;
        this.nacionalidad = nacionalidad;
    }

    // Getters
    /**
     * Obtiene el ID único del jugador.
     *
     * @return ID del jugador
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre completo del jugador.
     *
     * @return Nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el ID del equipo al que pertenece el jugador.
     *
     * @return ID del equipo
     */
    public int getIdEquipo() {
        return idEquipo;
    }

    /**
     * Obtiene el número de dorsal del jugador.
     *
     * @return Número de dorsal (1-99)
     */
    public int getDorsal() {
        return dorsal;
    }

    /**
     * Obtiene la posición principal del jugador.
     *
     * @return Posición principal
     */
    public String getPosicion() {
        return posicion;
    }

    /**
     * Obtiene la posición secundaria del jugador.
     *
     * @return Posición secundaria (puede ser null)
     */
    public String getPosicionSec() {
        return posicionSec;
    }

    /**
     * Obtiene el nivel de habilidad del jugador.
     *
     * @return Nivel de habilidad (1-100)
     */
    public int getHabilidad() {
        return habilidad;
    }

    /**
     * Obtiene el elemento de afinidad del jugador.
     *
     * @return Afinidad (Fuego, Aire, Montaña, Bosque, Neutro)
     */
    public String getAfinidad() {
        return afinidad;
    }

    /**
     * Obtiene la técnica especial principal del jugador.
     *
     * @return Técnica principal (puede ser null)
     */
    public String getTecnica() {
        return tecnica;
    }

    /**
     * Obtiene la técnica especial secundaria del jugador.
     *
     * @return Técnica secundaria (puede ser null)
     */
    public String getTecnicaSec() {
        return tecnicaSec;
    }

    /**
     * Obtiene la nacionalidad del jugador.
     *
     * @return Nacionalidad
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    // Setters
    /**
     * Establece el nombre del jugador.
     *
     * @param nombre Nuevo nombre (no puede estar vacío ni exceder 255
     * caracteres)
     * @throws IllegalArgumentException Si el nombre es inválido
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 255) {
            throw new IllegalArgumentException("El nombre no puede exceder los 255 caracteres");
        }
        this.nombre = nombre;
    }

    /**
     * Establece el ID del equipo al que pertenece el jugador.
     *
     * @param idEquipo Nuevo ID de equipo
     */
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    /**
     * Establece el número de dorsal del jugador.
     *
     * @param dorsal Nuevo dorsal (debe estar entre 1 y 99)
     * @throws IllegalArgumentException Si el dorsal está fuera de rango
     */
    public void setDorsal(int dorsal) {
        if (dorsal < 1 || dorsal > 99) {
            throw new IllegalArgumentException("El dorsal debe estar entre 1 y 99");
        }
        this.dorsal = dorsal;
    }

    /**
     * Establece la posición principal del jugador.
     *
     * @param posicion Nueva posición principal (no puede estar vacía)
     * @throws IllegalArgumentException Si la posición es inválida
     */
    public void setPosicion(String posicion) {
        if (posicion == null || posicion.trim().isEmpty()) {
            throw new IllegalArgumentException("La posición principal no puede estar vacía");
        }
        this.posicion = posicion;
    }

    /**
     * Establece la posición secundaria del jugador.
     *
     * @param posicionSec Nueva posición secundaria (puede ser null)
     */
    public void setPosicionSec(String posicionSec) {
        this.posicionSec = posicionSec;
    }

    /**
     * Establece el nivel de habilidad del jugador.
     *
     * @param habilidad Nueva habilidad (debe estar entre 1 y 100)
     * @throws IllegalArgumentException Si la habilidad está fuera de rango
     */
    public void setHabilidad(int habilidad) {
        if (habilidad < 1 || habilidad > 100) {
            throw new IllegalArgumentException("La habilidad debe estar entre 1 y 100");
        }
        this.habilidad = habilidad;
    }

    /**
     * Establece el elemento de afinidad del jugador.
     *
     * @param afinidad Nueva afinidad (Fuego, Aire, Montaña, Bosque, Neutro)
     * @throws IllegalArgumentException Si la afinidad no es válida
     */
    public void setAfinidad(String afinidad) {
        if (afinidad != null && !afinidad.isEmpty()) {
            String[] afinidadesValidas = {"Fuego", "Aire", "Montaña", "Bosque", "Neutro"};
            boolean valida = false;
            for (String a : afinidadesValidas) {
                if (a.equalsIgnoreCase(afinidad)) {
                    valida = true;
                    break;
                }
            }
            if (!valida) {
                throw new IllegalArgumentException("Afinidad inválida. Valores permitidos: Fuego, Aire, Montaña, Bosque, Neutro");
            }
        }
        this.afinidad = afinidad;
    }

    /**
     * Establece la técnica especial principal del jugador.
     *
     * @param tecnica Nueva técnica principal (puede ser null)
     */
    public void setTecnica(String tecnica) {
        this.tecnica = tecnica;
    }

    /**
     * Establece la técnica especial secundaria del jugador.
     *
     * @param tecnicaSec Nueva técnica secundaria (puede ser null)
     */
    public void setTecnicaSec(String tecnicaSec) {
        this.tecnicaSec = tecnicaSec;
    }

    /**
     * Establece la nacionalidad del jugador.
     *
     * @param nacionalidad Nueva nacionalidad (no puede estar vacía ni exceder
     * 255 caracteres)
     * @throws IllegalArgumentException Si la nacionalidad es inválida
     */
    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La nacionalidad no puede estar vacía");
        }
        if (nacionalidad.length() > 255) {
            throw new IllegalArgumentException("La nacionalidad no puede exceder los 255 caracteres");
        }
        this.nacionalidad = nacionalidad;
    }

}
