package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.HashSet;
import java.util.Set;

/**
 * The Enum WorkflowReserveKeywordsEnums.
 *
 * @author noman arshad
 */
public enum WorkflowReserveKeywordsEnums {

    /**
     * The post.
     */
    POST( 0, "Post" ),

    /**
     * The assemble.
     */
    ASSEMBLE( 1, "Assemble" ),

    /**
     * The solve.
     */
    SOLVE( 2, "Solve" ),

    /**
     * The job.
     */
    JOB( 3, "Job" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static WorkflowReserveKeywordsEnums getById( int id ) {
        for ( final WorkflowReserveKeywordsEnums e : values() ) {
            if ( e.id == id ) {
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
    public static WorkflowReserveKeywordsEnums getByValue( String value ) {
        for ( final WorkflowReserveKeywordsEnums e : values() ) {
            if ( e.key.equalsIgnoreCase( value ) ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets the all reserve values.
     *
     * @return the all reserve values
     */
    public static Set< String > getAllReserveValues() {
        final Set< String > result = new HashSet<>();
        for ( final WorkflowReserveKeywordsEnums key : WorkflowReserveKeywordsEnums.values() ) {
            result.add( key.getKey() );
        }
        return result;
    }

    /**
     * The id.
     */
    private final int id;

    /**
     * The key.
     */
    private final String key;

    /**
     * Instantiates a new workflow reserve keywords enums.
     *
     * @param id
     *         the id
     * @param key
     *         the key
     */
    WorkflowReserveKeywordsEnums( int id, String key ) {
        this.id = id;
        this.key = key;
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
     * Gets the key.
     *
     * @return the key
     */

    public String getKey() {
        return key;
    }

}
