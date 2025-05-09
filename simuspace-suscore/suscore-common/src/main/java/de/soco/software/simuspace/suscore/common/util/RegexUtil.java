package de.soco.software.simuspace.suscore.common.util;

import java.util.regex.Pattern;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

public class RegexUtil {

    /**
     * Prepare regex.
     *
     * @param objectiveRegex
     *         the objective regex
     *
     * @return the string
     */
    public static String prepareRegex( String objectiveRegex ) {
        String newRegex = ConstantsString.EMPTY_STRING;
        if ( objectiveRegex.contains( ConstantsString.DOUBLE_QUOTE_STRING ) ) {
            newRegex = objectiveRegex.replace( ConstantsString.DOUBLE_QUOTE_STRING, ConstantsString.BACK_SLASHED_DOULBLE_QUOTES );
        } else if ( objectiveRegex.contains( ConstantsString.CURLY_LEFT_BRACKET ) ) {
            newRegex = objectiveRegex.replace( ConstantsString.CURLY_LEFT_BRACKET, ConstantsString.BACK_SLASHED_CURLY_LEFT_BRACKET );
        }
        return newRegex.isEmpty() ? objectiveRegex : newRegex;
    }

    /**
     * Prepare pattern.
     *
     * @param regex
     *         the regex
     * @param caseSensative
     *         the case sensative
     *
     * @return the pattern
     */
    public static Pattern preparePattern( String regex, boolean caseSensative ) {
        try {
            if ( caseSensative ) {
                return Pattern.compile( prepareRegex( regex ) );
            } else {
                return Pattern.compile( regex, Pattern.CASE_INSENSITIVE );
            }
        } catch ( Exception e ) {
            throw new SusException( "Cannot parse regex" );
        }
    }

    private RegexUtil() {

    }

}
