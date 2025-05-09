package de.soco.software.simuspace.suscore.common.enums;

import lombok.Getter;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The enum Widget category.
 */
@Getter
public enum WidgetCategory {
    /**
     * Built in widget category.
     */
    BUILT_IN( "builtIn", "Built In" ),

    /**
     * Preview widget category.
     */
    PREVIEW( "preview", "Preview" );

    /**
     * The Key.
     */
    private final String id;

    /**
     * The Value.
     */
    private final String name;

    /**
     * Instantiates a new Widget category.
     *
     * @param id
     *         the id
     * @param name
     *         the value
     */
    WidgetCategory( String id, String name ) {
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
    public static WidgetCategory getEnumById( String id ) {
        for ( WidgetCategory option : WidgetCategory.values() ) {
            if ( option.getId().equals( id ) ) {
                return option;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
    }

    /**
     * Gets by id.
     *
     * @param id
     *         the id
     *
     * @return the by id
     */
    public static String getEnumIdById( String id ) {
        for ( WidgetCategory option : WidgetCategory.values() ) {
            if ( option.getId().equals( id ) ) {
                return option.id;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
    }
}
