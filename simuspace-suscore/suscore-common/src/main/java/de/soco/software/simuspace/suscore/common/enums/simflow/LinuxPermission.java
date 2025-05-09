package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * The Enum LinuxPermission provide the all kind of linux os permissions.
 */
public enum LinuxPermission {

    /**
     * The all permission.
     */
    ALL_PERMISSION( 0, "chmod -R 777" ),

    /**
     * The read permission.
     */
    READ_PERMISSION( 1, "chmod -R 744" ),

    /**
     * The execute permission.
     */
    EXECUTE_PERMISSION( 2, "chmod -R 755" );

    /**
     * The key.
     */
    private final Integer key;

    /**
     * The value.
     */
    private final String value;

    /**
     * Instantiates a new linux permission.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    LinuxPermission( int key, String value ) {
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
    public static LinuxPermission getById( int id ) {
        for ( final LinuxPermission e : values() ) {
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
    public static LinuxPermission getByValue( String value ) {
        for ( final LinuxPermission e : values() ) {
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
