package inazuma;

/**
 * Clase que representa un equipo de fútbol como plantilla para crear objetos.
 * Contiene información básica como nombre, ciudad y país, así como un
 * identificador único.
 *
 * @author jesus
 * @version 1.0
 */
public class Equipo {

    // Atributos
    private final int id;
    private String nombre;
    private String ciudad;
    private String pais;

    /**
     * Constructor que inicializa un equipo con todos sus atributos.
     *
     * @param id Identificador único del equipo
     * @param nombre Nombre del equipo
     * @param ciudad Ciudad de origen del equipo
     * @param pais País de origen del equipo
     */
    public Equipo(int id, String nombre, String ciudad, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    /**
     * Obtiene el identificador único del equipo.
     *
     * @return ID del equipo
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del equipo.
     *
     * @return Nombre del equipo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la ciudad de origen del equipo.
     *
     * @return Ciudad del equipo
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Obtiene el país de origen del equipo.
     *
     * @return País del equipo
     */
    public String getPais() {
        return pais;
    }

    /**
     * Establece un nuevo nombre para el equipo.
     *
     * @param nombre Nuevo nombre del equipo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece una nueva ciudad de origen para el equipo.
     *
     * @param ciudad Nueva ciudad del equipo
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Establece un nuevo país de origen para el equipo.
     *
     * @param pais Nuevo país del equipo
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

}
