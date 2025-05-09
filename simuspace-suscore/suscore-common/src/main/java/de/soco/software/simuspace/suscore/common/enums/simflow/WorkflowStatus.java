package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * Each workflow will have some specific status. This Enum consists of all the statuses wich a workflow can have.
 */
public enum WorkflowStatus {

    /**
     * The delete.
     */
    DELETE( 00, "Delete" ),

    /**
     * The wip.
     */
    WIP( 1, "WIP" ),

    /**
     * The visible.
     */
    VISIBLE( 2, "Visible" ),

    /**
     * The approved.
     */
    APPROVED( 3, "Approved" ),

    /**
     * The rejected.
     */
    REJECTED( 4, "Rejected" ),

    /**
     * The invalid.
     */
    INVALID( 5, "Invalid" ),

    /**
     * The closed.
     */
    CLOSED( 6, "Closed" ),

    /**
     * The aborted.
     */
    ABORTED( 7, "Aborted" ),

    /**
     * The paused.
     */
    PAUSED( 8, "Paused" ),

    /**
     * The cleanedup.
     */
    CLEANEDUP( 11, "Cleanedup" ),

    /**
     * The failed.
     */
    FAILED( 19, "Failed" ),

    /**
     * The running.
     */
    RUNNING( 20, "Running" ),

    /**
     * The pending.
     */
    PENDING( 21, "Pending" ),

    /**
     * The queued.
     */
    QUEUED( 22, "Queued" ),

    /**
     * The completed.
     */
    COMPLETED( 23, "Completed" ),

    /**
     * The inactive.
     */
    INACTIVE( 24, "Inactive" ),

    /**
     * The Discard.
     */
    DISCARD( 33, "Discarded" ),

    /**
     * The deprecated.
     */
    DEPRECATED( 90, "Deprecated" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static WorkflowStatus getById( int id ) {
        for ( final WorkflowStatus e : values() ) {
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
    public static WorkflowStatus getByValue( String value ) {
        for ( final WorkflowStatus e : values() ) {
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
    WorkflowStatus( int key, String value ) {
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
