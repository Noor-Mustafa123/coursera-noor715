package de.soco.software.simuspace.suscore.common.enums;

/**
 * @author Ahmar Nadeem
 *
 * This enum contains the object types. Currently there are only two types but may contain more if needed.
 */
public enum ObjectTypeEnum {

    /**
     * Enum for project type object.
     */
    PROJECT( "project" ),

    /**
     * Enum for variant type object.
     */
    VARIANT( "variant" );

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
    ObjectTypeEnum( String key ) {
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
