package de.soco.software.simuspace.suscore.object.model;

/**
 * This enum contains the project types.
 *
 * @author M.Nasir.Farooq
 */
public enum ProjectType {

    /**
     * Project type can't contain any data, but have sub-projects.
     */
    LABEL( "Label" ),

    /**
     * Project type can contain data and sub-projects.
     */
    DATA( "Data" );

    /**
     * The key of enumeration.
     */
    private final String key;

    /**
     * Instantiates a new object type.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    ProjectType( String key ) {
        this.key = key;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
