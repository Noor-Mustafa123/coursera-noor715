package de.soco.software.simuspace.suscore.common.enums.simflow;

import java.util.HashSet;
import java.util.Set;

/**
 * This Enum contains all configured runmodes Generally an executable element could have a runmodes can be [client,server, user-selected,
 * null] a runmode is users way of running a workflow either a workflow is running of client side or server side.
 */
public enum RunMode {

    /**
     * A run mode "client": The workflow to run on the client.
     */

    LOCAL( 0, "local" ),
    /**
     * A run mode "server": The workflow to run on the server.
     */
    SERVER( 1, "server" ),
    /**
     * A run mode "user-selected": The workflow is running on either client or server, depending upon user selection.
     */
    USERSELECTED( 2, "user-selected" );

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the {@link RunMode} that is associated with the ID, <code>null</code> if the id does not match a run mode.
     */
    public static RunMode getById( int id ) {
        for ( final RunMode e : values() ) {
            if ( e.id == id ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets the by id.
     *
     * @param id
     *         the id
     *
     * @return the {@link RunMode} that is associated with the ID, <code>null</code> if the id does not match a run mode.
     */
    public static RunMode getByValue( String value ) {
        for ( final RunMode e : values() ) {
            if ( e.key.equalsIgnoreCase( value ) ) {
                return e;
            }
        }
        return null;
    }

    /**
     * Gets all the possible run mode values.
     *
     * @return the run modes
     */
    public static Set< String > getRunModes() {
        final Set< String > result = new HashSet<>();
        for ( final RunMode key : RunMode.values() ) {
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
     * Instantiates a new run mode.
     *
     * @param id
     *         the id
     * @param key
     *         the key
     */

    RunMode( int id, String key ) {
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
