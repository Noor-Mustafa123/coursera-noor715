package de.soco.software.simuspace.suscore.common.enums;

import lombok.Getter;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The enum Widget type.
 */
@Getter
public enum WidgetType {

    /**
     * The Running jobs.
     */
    RUNNING_JOBS( "runningJobs", "Running Jobs" ),

    /**
     * The Hpc jobs.
     */
    HPC_JOBS( "hpcJobs", "HPC Jobs" ),
    /**
     * Bookmarks widget type.
     */
    BOOKMARKS( "bookmarks", "Bookmarks" ),
    /**
     * The Recent objects.
     */
    RECENT_OBJECTS( "recentObjects", "Recent Objects" ),
    /**
     * The Recent data objects.
     */
    RECENT_DATA_OBJECTS( "recentDataObjects", "Recent Data Objects" ),

    /**
     * The My workflows.
     */
    MY_WORKFLOWS( "myWorkflows", "My Workflows" ),
    /**
     * The Completed jobs.
     */
    COMPLETED_JOBS( "completedJobs", "Completed Jobs" );

    /**
     * The Key.
     */
    private final String id;

    /**
     * The Value.
     */
    private final String name;

    /**
     * Instantiates a new Widget type.
     *
     * @param id
     *         the key
     * @param name
     *         the value
     */
    WidgetType( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static String getById( String id ) {
        for ( WidgetType widgetType : WidgetType.values() ) {
            if ( widgetType.getId().equals( id ) ) {
                return widgetType.id;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
    }
}
