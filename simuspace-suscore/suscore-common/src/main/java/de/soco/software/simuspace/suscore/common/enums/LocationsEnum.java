/*
 *
 */

package de.soco.software.simuspace.suscore.common.enums;

import java.util.HashSet;
import java.util.Set;

/**
 * The Enum LocationsEnum.
 */
public enum LocationsEnum {

    /**
     * The default location.
     */
    DEFAULT_LOCATION( "8d040038-8599-11e8-adc0-fa7ae01bbebc", "Default", false ),

    /**
     * The local windows.
     */
    LOCAL_WINDOWS( "ce91e09e-88b7-11e8-9cb2-a6cf71072f73", "Local (Windows)", true ),

    /**
     * The local linux.
     */
    LOCAL_LINUX( "ce91e634-88b7-11e8-9cb2-a6cf71072f73", "Local (Linux)", true );

    /**
     * The id.
     */
    private final String id;

    /**
     * The name.
     */
    private final String name;

    private final boolean local;

    /**
     * Instantiates a new locations enum.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    LocationsEnum( String id, String name, boolean local ) {
        this.id = id;
        this.name = name;
        this.local = local;

    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the local locations.
     *
     * @return the local locations
     */
    public static Set< String > getLocalLocations() {
        final Set< String > result = new HashSet<>();
        for ( final LocationsEnum item : LocationsEnum.values() ) {
            if ( item.isLocal() ) {
                result.add( item.getId() );
            }
        }
        return result;
    }

    /**
     * Gets the id by name.
     *
     * @param name
     *         the name
     *
     * @return the id by name
     */
    public static String getIdByName( String name ) {
        String id = "";
        for ( LocationsEnum location : values() ) {
            if ( location.getName().contains( name ) ) {
                id = location.getId();
                break;
            }
        }
        return id;
    }

    /**
     * Gets the name by id.
     *
     * @param id
     *         the id
     *
     * @return the name by id
     */
    public static String getNameById( String id ) {
        String name = "";
        for ( LocationsEnum location : values() ) {
            if ( location.getId().equals( id ) ) {
                name = location.getName();
                break;
            }
        }
        return name;
    }

    public boolean isLocal() {
        return local;
    }

}
