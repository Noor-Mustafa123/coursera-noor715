package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * The Enum JobTypeEnums.
 */
public enum JobTypeEnums {

    /**
     * Workflow job type enums.
     */
    WORKFLOW( 0, "Workflow" ),

    /**
     * Scheme job type enums.
     */
    SCHEME( 1, "Scheme" ),

    /**
     * System job type enums.
     */
    SYSTEM( 2, "System" ),

    /**
     * The Variant.
     */
    VARIANT( 3, "Variant" ),

    /**
     * Qadyna job type enums.
     */
    QADYNA( 4, "QADYNA" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static JobTypeEnums getById( int id ) {
        for ( final JobTypeEnums e : values() ) {
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
    public static JobTypeEnums getByValue( String value ) {
        for ( final JobTypeEnums e : values() ) {
            if ( e.value.equalsIgnoreCase( value ) ) {
                return e;
            }
        }
        return null;
    }

    /**
     * The key.
     */
    private final Integer key;

    /**
     * The value.
     */
    private final String value;

    /**
     * Instantiates a new workflow status.
     *
     * @param key
     *         the key
     * @param value
     *         the value
     */
    JobTypeEnums( int key, String value ) {
        this.key = key;
        this.value = value;
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
