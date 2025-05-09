package de.soco.software.simuspace.suscore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The enum Filter type.
 */
@AllArgsConstructor
@Getter
public enum FilterType {

    /**
     * Number filter type.
     */
    number,
    /**
     * Text filter type.
     */
    text,
    /**
     * Date range filter type.
     */
    dateRange,
    /**
     * Uuid filter type.
     */
    uuid;

    public static FilterType getByName( String name ) {
        for ( FilterType option : FilterType.values() ) {
            if ( name.equals( option.name() ) ) {
                return option;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), name ) );
    }
}
