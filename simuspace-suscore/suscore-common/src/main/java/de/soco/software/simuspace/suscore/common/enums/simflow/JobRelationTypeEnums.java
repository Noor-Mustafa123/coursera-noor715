package de.soco.software.simuspace.suscore.common.enums.simflow;

public enum JobRelationTypeEnums {

    /**
     * The master.
     */
    MASTER( 0, "Master" ),

    /**
     * The child.
     */
    CHILD( 1, "Child" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static JobRelationTypeEnums getById( int id ) {
        for ( final JobRelationTypeEnums e : values() ) {
            if ( e.key == id ) {
                return e;
            }
        }
        return null;
    }

    public static JobRelationTypeEnums getByValue( String value ) {
        for ( final JobRelationTypeEnums e : values() ) {
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
    JobRelationTypeEnums( int key, String value ) {
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