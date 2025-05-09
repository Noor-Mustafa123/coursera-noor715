package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.ArrayList;
import java.util.List;

public enum SchemeCategoryEnum {

    /**
     * The aborted.
     */
    DOE( 0, "DOE" ),

    /**
     * The approved.
     */
    OPTIMIZATION( 1, "Optimization" ),

    /**
     * The cleanedup.
     */
    MACHINE_LEARNING( 2, "Machine Learning" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static SchemeCategoryEnum getById( int id ) {
        for ( final SchemeCategoryEnum e : values() ) {
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
    public static SchemeCategoryEnum getByValue( String value ) {
        for ( final SchemeCategoryEnum e : values() ) {
            if ( e.value.equalsIgnoreCase( value ) ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets all the possible field types of fields of a workflow element.
     *
     * @return the types
     */
    public static List< String > getValues() {
        final List< String > result = new ArrayList<>();
        for ( final SchemeCategoryEnum type : SchemeCategoryEnum.values() ) {
            result.add( type.getValue() );
        }
        return result;
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
    SchemeCategoryEnum( int key, String value ) {
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
