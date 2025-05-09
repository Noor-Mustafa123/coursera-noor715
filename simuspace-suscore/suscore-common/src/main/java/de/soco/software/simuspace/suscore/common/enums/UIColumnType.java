package de.soco.software.simuspace.suscore.common.enums;

public enum UIColumnType {

    /**
     * UI Column type Select.
     */
    SELECT( "select" );

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
    UIColumnType( String key ) {
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
