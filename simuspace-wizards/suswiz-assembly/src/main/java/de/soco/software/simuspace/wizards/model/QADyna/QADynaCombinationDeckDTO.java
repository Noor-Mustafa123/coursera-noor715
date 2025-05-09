package de.soco.software.simuspace.wizards.model.QADyna;

/**
 * The type Dyna combination object dto.
 *
 * @author Ali Haider
 */
public class QADynaCombinationDeckDTO {

    /**
     * The Id.
     */
    private String id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Path.
     */
    private String fullPath;

    /**
     * The Version.
     */
    private String version;

    /**
     * Instantiates a new Qa dyna combination object dto.
     */
    public QADynaCombinationDeckDTO() {
    }

    /**
     * Instantiates a new Qa dyna combination object dto.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param fullPath
     *         the full path
     * @param version
     *         the version
     */
    public QADynaCombinationDeckDTO( String id, String name, String fullPath, String version ) {
        this.id = id;
        this.name = name;
        this.fullPath = fullPath;
        this.version = version;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Sets fullPath.
     *
     * @param fullPath
     *         the fullPath
     */
    public void setFullPath( String fullPath ) {
        this.fullPath = fullPath;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version
     *         the version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

}
