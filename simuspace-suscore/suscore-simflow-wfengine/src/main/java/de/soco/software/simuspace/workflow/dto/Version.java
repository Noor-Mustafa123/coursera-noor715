package de.soco.software.simuspace.workflow.dto;

/**
 * The Class provide version id and name as object representation
 *
 * @author Nosheen.Sharif
 */
public class Version {

    /**
     * The version id of workflow.
     */
    private int id;

    /**
     * The version label of workflow.
     */
    private String name;

    /**
     * Instantiates a new version.
     */
    public Version() {
        super();
    }

    /**
     * Instantiates a new version.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public Version( int id, String name ) {
        this();
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( int id ) {
        this.id = id;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

}
