package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * The Enum WindowsPermission provide all kind of windows os permissions.
 */
public enum WindowsPermission {

    /**
     * The all permission.
     */
    ALL_PERMISSION( 0, "attrib -r" );

    /**
     * The key.
     */
    private final Integer key;

    /**
     * The value.
     */
    private final String value;

    /**
     * Instantiates a new windows permission.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    WindowsPermission( int key, String value ) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static WindowsPermission getById( int id ) {
        for ( final WindowsPermission e : values() ) {
            if ( e.key == id ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets the by value.
     *
     * @param value
     *         the value
     *
     * @return the by value
     */
    public static WindowsPermission getByValue( String value ) {
        for ( final WindowsPermission e : values() ) {
            if ( e.value.equalsIgnoreCase( value ) ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public int getKey() {
        return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

}
