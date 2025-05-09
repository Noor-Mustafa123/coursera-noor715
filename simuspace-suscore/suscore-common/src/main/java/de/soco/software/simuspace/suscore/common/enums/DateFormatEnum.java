package de.soco.software.simuspace.suscore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The enum Date format.
 */
@AllArgsConstructor
@Getter
public enum DateFormatEnum {

    /**
     * Dd mm yyyy date format.
     */
    DD_MM_YYYY( "dd.MM.yyyy", "^\\d{2}\\.\\d{2}\\.\\d{4}$" ),

    /**
     * Mm dd yyyy date format.
     */
    MM_DD_YYYY( "MM/dd/yyyy", "^\\d{2}/\\d{2}/\\d{4}$" ),

    /**
     * Yyyy mm dd date format.
     */
    YYYY_MM_DD( "yyyy-MM-dd", "^\\d{4}-\\d{2}-\\d{2}$" ),

    /**
     * Dd mm yyyy hh mm ss date format.
     */
    DD_MM_YYYY_HH_MM_SS( "dd.MM.yyyy-HH:mm:ss", "^\\d{2}\\.\\d{2}\\.\\d{4}-\\d{2}:\\d{2}:\\d{2}$" ),

    /**
     * The Mm dd yyyy hh mm ss.
     */
    MM_DD_YYYY_HH_MM_SS( "MM/dd/yyyy HH:mm:ss", "^\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}$" ),

    /**
     * The Yyyy mm dd hh mm ss.
     */
    YYYY_MM_DD_HH_MM_SS( "yyyy-MM-dd HH:mm:ss", "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$" ),

    /**
     * Iso 8601 date format.
     */
    ISO_8601( "yyyy-MM-dd'T'HH:mm:ss'Z'", "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$" ),

    /**
     * Yyyy mm dd hhmm date format enum.
     */
    YYYY_MM_DD_HHMM( "yyyy_MM_dd_HHmmss", "^\\d{4}_\\d{2}_\\d{2}_\\d{6}$" ),

    /**
     * Date picker date format enum.
     */
    DATE_PICKER( "yyyy-MM-dd'T'hh:mm", "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$" ),

    /**
     * Dd mm yyyy dash date format enum.
     */
    DD_MM_YYYY_DASH( "dd-MM-yyyy", "^\\d{2}-\\d{2}-\\d{4}$" ),

    /**
     * Iso offset date time date format enum.
     */
    ISO_OFFSET_DATE_TIME( "yyyy-MM-dd'T'HH:mm:ssXXX", "^\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}$" ),

    /**
     * The Eee mmm dd hh mm ss z yyyy.
     */
    EEE_MMM_DD_HH_MM_SS_Z_YYYY( "EEE MMM dd HH:mm:ss z yyyy",
            "^[A-Za-z]{3} [A-Za-z]{3} \\d{2} \\d{2}:\\d{2}:\\d{2} [A-Za-z]{1,5} \\d{4}$" );

    /**
     * The Format name.
     */
    private final String format;

    /**
     * The Regex.
     */
    private final String regex;

    /**
     * Match format date format.
     *
     * @param date
     *         the date
     *
     * @return the date format
     */
    public static DateFormatEnum matchFormat( String date ) {
        for ( DateFormatEnum format : values() ) {
            if ( date.matches( format.getRegex() ) ) {
                return format;
            }
        }
        throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), date ) );
    }
}

