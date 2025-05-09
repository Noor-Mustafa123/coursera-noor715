package de.soco.software.simuspace.suscore.executor.enums;

/**
 * The Enum SchedulerType is to specify schedulers delay.
 *
 * @author Noman Arshad
 */
public enum SchedulerType {

    /**
     * The Fixed_ delay.
     */
    FIXED_DELAY( 1 ),
    /**
     * The Fixed_ rate.
     */
    FIXED_RATE( 2 );

    /**
     * Instantiates a new scheduler type.
     *
     * @param type
     *         the type
     */
    SchedulerType( int type ) {
        this.type = type;
    }

    /**
     * The type.
     */
    private final int type;

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }
}
