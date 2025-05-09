package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;

/**
 * The Class StringUtils. This Class contains all the utilities function that can applied on a string To check and validate value of a
 * string
 */
public class StringUtils {

    /**
     * The string constant representing an empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The special characters that are allowed in a standard field.
     *
     * Please specify what makes this characters special. Change the name afterwards to symbolize what makes them special. Seems to me there
     * might be some of these strings.
     */
    public static final String ALLOWED_SPECIAL_CHARACTERS = "^[\\p{L}0-9-_\\s]+$";

    /**
     * Regex to validate UUID Pattern.
     */
    public static final String UUID_VALIDATION_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    /**
     * Instantiates a new string utils.
     */
    private StringUtils() {
        super();
    }

    /**
     * Checks if the string passed is not null or empty.
     *
     * @param srcString
     *         the string to be checked.
     *
     * @return <code>true</code> if the string passed is not null or empty, <code>false</code> otherwise.
     */
    public static boolean isNotNullOrEmpty( final String srcString ) {
        return !isNullOrEmpty( srcString );
    }

    /**
     * Checks if the string passed is null or empty.
     *
     * @param srcString
     *         the string to be checked.
     *
     * @return <code>true</code> if the string passed is null or empty, <code>false</code> otherwise.
     */
    public static boolean isNullOrEmpty( final String srcString ) {
        return ( ( null == srcString ) || EMPTY_STRING.equals( srcString ) );
    }

    /**
     * Checks if is valid email address.
     *
     * @param email
     *         the email
     *
     * @return true, if is valid email address
     */
    public static boolean isValidEmailAddress( String email ) {
        final String ePattern = "^[\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]+\\.)+[\\p{L}0-9.!#$%&'*+/=?^_`{|}~-]{2,}))$";
        final java.util.regex.Pattern p = java.util.regex.Pattern.compile( ePattern );
        final java.util.regex.Matcher m = p.matcher( email );
        return m.matches();
    }

    /**
     * Checks if is global naming convention followed.
     *
     * @param name
     *         the name
     *
     * @return true, if is global naming convention followed
     */
    public static boolean isGlobalNamingConventionFollowed( String name ) {
        boolean check1 = Pattern.compile( ".*[<|>|:|\"|/|\\\\|\\|\\?|\\*|[\\\0]].*" ).matcher( name ).find();
        boolean check2 = Pattern.compile( ".*[\\s|\\.]$" ).matcher( name ).find();
        return ( check1 || check2 );
    }

    /**
     * Prepare invalid argument message.
     *
     * @param fieldName
     *         the field name
     *
     * @return the string
     */
    public static String prepareInvalidArgumentMessage( String fieldName ) {
        return MessagesUtil.getMessage( Messages.UTILS_INVALID_VALUE, fieldName );
    }

    /**
     * Tokenize string by char.
     *
     * @param srcString
     *         the src string
     * @param token
     *         the token
     *
     * @return the string[]
     */
    public static String[] tokenizeStringByChar( final String srcString, final String token ) {

        if ( StringUtils.isNullOrEmpty( srcString ) || ( null == token ) ) {
            return new String[ 0 ];
        }

        String[] result;
        int counter = 0;

        final StringTokenizer stringTokenizer = new StringTokenizer( srcString, token );
        result = new String[ stringTokenizer.countTokens() ];

        while ( stringTokenizer.hasMoreElements() ) {
            result[ counter ] = stringTokenizer.nextElement().toString();
            counter++;
        }

        return result;

    }

    /**
     * Performs some basic validations on the field passed: This method is used in DTOs validation
     * <ul>
     * <li>Whether it is not null or empty (in case it is not optional).
     * <li>Whether the text entered is short enough.
     * <li>Whether the characters all are allowed.
     * </ul>
     *
     * @param fieldValue
     *         The field value to be verified.
     * @param fieldName
     *         The name of the field to be verified.
     * @param maxLength
     *         The maximum length of the field to be verified.
     * @param isOptional
     *         Whether the field to be verified is optional.
     * @param checkSpecialChars
     *         Whether to check special characters of the field.
     *
     * @return <code>true</code> if the validation was successful, <code>false</code> otherwise.
     */
    public static Notification validateFieldAndLength( String fieldValue, String fieldName, int maxLength, boolean isOptional,
            boolean checkSpecialChars ) {

        final boolean isFieldNullOrEmpty = isNullOrEmpty( fieldValue );
        final Notification notif = new Notification();
        // If the field is not optional it needs to be filled.
        if ( !isOptional && isFieldNullOrEmpty ) {

            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.UTILS_NAME_CANT_BE_NULL, fieldName ) ) );
            return notif;
        }

        if ( !isFieldNullOrEmpty ) {

            if ( fieldValue.length() > maxLength ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.UTILS_VALUE_TOO_LARGE, fieldName, maxLength ) ) );
            }

            if ( checkSpecialChars && !validateSpecialCharacters( fieldValue ) ) {
                notif.addError( new Error(
                        MessagesUtil.getMessage( WFEMessages.UTILS_INVALID_SPECIAL_CHARS, fieldValue, ALLOWED_SPECIAL_CHARACTERS ) ) );
            }
        }

        return notif;

    }

    /**
     * Validates if the string passed contains only the allowed special characters.
     *
     * @param fieldVal
     *         the field val
     *
     * @return <code>true</code> if the string passed contains only allowed characters without special characters in it, <code>false</code>
     * if string passed contains $%&*^()_.
     */
    public static boolean validateSpecialCharacters( String fieldVal ) {

        final Pattern pattern = Pattern.compile( ALLOWED_SPECIAL_CHARACTERS, Pattern.CASE_INSENSITIVE );
        final Matcher matcher = pattern.matcher( fieldVal );
        return matcher.matches();

    }

    /**
     * Validate UUID string.If the passed String valid UUID
     *
     * @param fieldVal
     *         the field val
     *
     * @return true, if successful
     */
    public static boolean validateUUIDString( String fieldVal ) {

        final Pattern pattern = Pattern.compile( UUID_VALIDATION_REGEX );
        final Matcher matcher = pattern.matcher( fieldVal );
        return matcher.matches();

    }

    /**
     * Replace wild card with regex.
     *
     * @param wildCard
     *         the wild card
     *
     * @return the string
     */
    public static String replaceWildCardWithRegex( String wildCard ) {

        return wildCard.replace( ".", "\\." ).replace( "*", ".*" );
    }

    /**
     * Gets the all variables from script with key node.
     *
     * @return the all variables from script with key node
     */
    public static List< String > getAllVariablesFromScriptWithKeyNode( String cmd, String parentElementName ) {
        final List< String > variablesUserInCmd = new ArrayList<>();
        final Pattern pattern = Pattern.compile( "([^\\s]+)" );
        final Matcher matcher = pattern.matcher( cmd );
        while ( matcher.find() ) {
            if ( matcher.group().contains( parentElementName + ".useroutput" ) ) {
                variablesUserInCmd.add( matcher.group() );
            }

        }
        return variablesUserInCmd;
    }

    /**
     * Gets the all system variables from script with key node.
     *
     * @param cmd
     *         the cmd
     * @param parentElementName
     *         the parent element name
     *
     * @return the all system variables from script with key node
     */
    public static List< String > getAllSystemVariablesFromScriptWithKeyNode( String cmd, String parentElementName ) {
        final List< String > variablesUserInCmd = new ArrayList<>();
        final Pattern pattern = Pattern.compile( "([^\\s]+)" );
        final Matcher matcher = pattern.matcher( cmd );
        while ( matcher.find() ) {
            if ( matcher.group().contains( parentElementName + ".systemoutput" ) ) {
                variablesUserInCmd.add( matcher.group() );
            }

        }
        return variablesUserInCmd;
    }

    /**
     * Removes the special char.
     *
     * @param line
     *         the line
     *
     * @return the string
     */
    public static String removeSpecialChar( String line ) {
        return line.replaceAll( "[^a-zA-Z]+", "" );
    }

    /**
     * Compares AlphaNumeric Strings and decides which string has greater value.
     *
     * @param s1
     *         the s1
     * @param s2
     *         the s2
     *
     * @return the int
     */
    public static int compareAlphaNumericStrings( String s1, String s2 ) {
        if ( ( s1 == null ) || ( s2 == null ) ) {
            return 0;
        }

        int thisMarker = 0;
        int thatMarker = 0;
        int s1Length = s1.length();
        int s2Length = s2.length();

        while ( thisMarker < s1Length && thatMarker < s2Length ) {
            String thisChunk = getChunk( s1, s1Length, thisMarker );
            thisMarker += thisChunk.length();

            String thatChunk = getChunk( s2, s2Length, thatMarker );
            thatMarker += thatChunk.length();

            // If both chunks contain numeric characters, sort them numerically
            int result = 0;
            if ( isDigit( thisChunk.charAt( 0 ) ) && isDigit( thatChunk.charAt( 0 ) ) ) {
                // Simple chunk comparison by length.
                int thisChunkLength = thisChunk.length();
                result = thisChunkLength - thatChunk.length();
                // If equal, the first different number counts
                if ( result == 0 ) {
                    for ( int i = 0; i < thisChunkLength; i++ ) {
                        result = thisChunk.charAt( i ) - thatChunk.charAt( i );
                        if ( result != 0 ) {
                            return result;
                        }
                    }
                }
            } else {
                result = thisChunk.compareTo( thatChunk );
            }

            if ( result != 0 ) {
                return result;
            }
        }

        return s1Length - s2Length;
    }

    /**
     * Length of string is passed in for improved efficiency (only need to calculate it once)
     **/
    private static String getChunk( String s, int slength, int marker ) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt( marker );
        chunk.append( c );
        marker++;
        if ( isDigit( c ) ) {
            while ( marker < slength ) {
                c = s.charAt( marker );
                if ( !isDigit( c ) ) {
                    break;
                }
                chunk.append( c );
                marker++;
            }
        } else {
            while ( marker < slength ) {
                c = s.charAt( marker );
                if ( isDigit( c ) ) {
                    break;
                }
                chunk.append( c );
                marker++;
            }
        }
        return chunk.toString();
    }

    /**
     * Checks if is Digit.
     *
     * @param ch
     *         the ch
     */
    private static boolean isDigit( char ch ) {
        return ( ( ch >= 48 ) && ( ch <= 57 ) );
    }

    /**
     * NullSafe equal check
     *
     * @param String
     *         a
     * @param String
     *         b
     *
     * @return true if both null or equal, false if one is null, false if strings unequal
     */
    public static boolean areEqual( String a, String b ) {
        if ( a == null && b == null ) {
            return true;
        }
        if ( a == null || b == null ) {
            return false;
        }
        return a.equals( b );
    }

}