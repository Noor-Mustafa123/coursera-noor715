/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class ValidationUtils only deals with validations for utility purposes available globally in the system.
 *
 * @author Ahsan Khan
 */
@Log4j2
public class ValidationUtils {

    /**
     * Instantiates a new validation utils.
     */
    private ValidationUtils() {

    }

    /**
     * The special characters that are allowed in a standard field.
     *
     * there might be some of these strings.
     */
    public static final String ALLOWED_SPECIAL_CHARACTERS = "^[\\p{L}0-9-_\\s]+$";

    /**
     * The constant ALLOWED_POSITIVE_INTEGER.
     */
    public static final String ALLOWED_POSITIVE_INTEGER = "^[0-9]\\d*$";

    /**
     * The Constant MIN_PASSWD_LENGTH.
     */
    private static final int MIN_PASSWD_LENGTH = 8;

    /**
     * The Constant REG_FOR_NATURAL.
     */
    private static final String REG_FOR_NATURAL = "^(?=.*[0-9]).{8,}$";

    /**
     * The Constant REG_FORLOWER_CASE.
     */
    private static final String REG_FOR_LOWER_CASE = "^(?=.*[a-z]).{8,}$";

    /**
     * The Constant REG_FORUPPER_CASE.
     */
    private static final String REG_FOR_UPPER_CASE = "^(?=.*[A-Z]).{8,}$";

    /**
     * The Constant REG_FORWHITE_CASE.
     */
    private static final String REG_FOR_WHITE_SPACE = "^(?=\\S+$).{8,}$";

    /**
     * The constant REG_FOR_SPECIAL_CHAR.
     */
    private static final String REG_FOR_SPECIAL_CHAR = ".*[!@#$%^&*(),.?\\\":{}|<>_\\[\\]\\\\\\\\/\\'~`\\-+=].*";

    /**
     * constant regular expression for phone number validation.
     */
    private static final String REGEX_FOR_PHONE_NUMBER = "^[.+0-9-\\(\\)\\s]*{6,14}$";

    /**
     * The constant SAFE_HTML_REGEX.
     */
    public static final String SAFE_HTML_REGEX = "^(?!.*<(?:script|iframe)[^>]*>).*[a-zA-Z0-9\\s.,?!@#&()\\-_'\"]*$";

    /**
     * The string constant representing an empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * Regex to validate UUID Pattern.
     */
    public static final String UUID_VALIDATION_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    /**
     * The constant customSafelist.
     */
    public static final Safelist customSafelist = Safelist.none()
            // Allow basic text formatting
            .addTags( "p", "b", "strong", "i", "u", "em", "small", "mark", "abbr", "cite", "dfn", "kbd", "samp", "var", "sub", "sup", "del",
                    "ins", "s", "span" )
            // Allow headings
            .addTags( "h1", "h2", "h3", "h4", "h5", "h6" )
            // Allow lists
            .addTags( "ul", "ol", "li", "dl", "dt", "dd" )
            // Allow links but restrict protocols
            .addTags( "a" )
            .addAttributes( "a", "href", "title", "rel", "target" )
            .addProtocols( "a", "href", "mailto", "http", "https", "tel" )
            .removeProtocols( "a", "href", "javascript" )
            // Allow strong element
            .addTags( "strong" )
            .addAttributes( "strong", "class" )
            // Allow images with restrictions
            .addTags( "img" )
            .addAttributes( "img", "src", "alt", "title", "width", "height" )
            .addProtocols( "img", "src", "http", "https", "data" )
            .removeAttributes( "img", "onerror", "onload", "srcset", "longdesc" )
            .removeProtocols( "img", "src", "javascript" )
            // Allow structural elements
            .addTags( "div", "section", "article", "aside", "header", "footer", "main", "nav", "html" )
            // Allow tables
            .addTags( "table", "tr", "td", "th", "thead", "tbody", "tfoot", "caption", "colgroup", "col" )
            .addAttributes( "table", "border", "cellpadding", "cellspacing", "width", "height" )
            // Allow code and preformatted text
            .addTags( "pre", "code", "blockquote", "hr", "br", "wbr" )
            // Allow forms but block JavaScript-based attacks
            .addTags( "form", "input", "label", "textarea", "button", "select", "option", "optgroup", "fieldset", "legend" )
            .addAttributes( "form", "action", "method" )
            .addAttributes( "input", "type", "name", "value", "placeholder", "checked", "disabled", "readonly" )
            .addAttributes( "button", "type", "name", "value", "disabled" )
            // Allow accessibility attributes
            .addAttributes( ":all", "class", "id", "lang", "dir", "title", "aria-*" )
            // Allow figure and figcaption for media content
            .addTags( "figure", "figcaption" )

            // Block dangerous elements
            .removeTags( "script", "iframe", "object", "embed", "meta", "style", "link", "json", "svg", "math", "video", "source",
                    "details", "dialog" )
            // Block dangerous attributes
            .removeAttributes( ":all", "onmouseover", "onmouseout", "onfocus", "onblur", "onclick", "onload", "onerror",
                    "onchange", "onsubmit", "onkeypress", "onkeydown", "onkeyup", "ontoggle", "onclose" )
            .removeAttributes( "div", "style" )
            .removeAttributes( ":all", "style" )
            // Ensure absolute URLs
            .preserveRelativeLinks( false );

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
        return ( null == srcString ) || EMPTY_STRING.equals( srcString );
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

            notif.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), fieldName ) ) );
            return notif;
        }

        if ( !isFieldNullOrEmpty ) {

            if ( fieldValue.length() > maxLength ) {
                notif.addError(
                        new Error( MessageBundleFactory.getMessage( Messages.UTILS_VALUE_TOO_LARGE.getKey(), fieldName, maxLength ) ) );
            }

            if ( checkSpecialChars && !validateSpecialCharacters( fieldValue ) ) {
                notif.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_SPECIAL_CHARS.getKey(), fieldName ) ) );
            }
        }

        return notif;

    }

    /**
     * Validate global field and length.
     *
     * @param fieldValue
     *         the field value
     * @param fieldName
     *         the field name
     * @param maxLength
     *         the max length
     * @param isOptional
     *         the is optional
     * @param checkSpecialChars
     *         the check special chars
     *
     * @return the notification
     */
    public static Notification validateGlobalFieldAndLength( String fieldValue, String fieldName, int maxLength, boolean isOptional,
            boolean checkSpecialChars ) {

        final boolean isFieldNullOrEmpty = isNullOrEmpty( fieldValue );
        final Notification notif = new Notification();
        // If the field is not optional it needs to be filled.
        if ( !isOptional && isFieldNullOrEmpty ) {

            notif.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), fieldName ) ) );
            return notif;
        }

        if ( !isFieldNullOrEmpty ) {

            if ( fieldValue.length() > maxLength ) {
                notif.addError(
                        new Error( MessageBundleFactory.getMessage( Messages.UTILS_VALUE_TOO_LARGE.getKey(), fieldName, maxLength ) ) );
            }

            if ( checkSpecialChars && StringUtils.isGlobalNamingConventionFollowed( fieldValue ) ) {
                notif.addError( new Error(
                        MessageBundleFactory.getMessage( Messages.NAME_SHOULD_NOT_HAVE_SPECIAL_CHARACTER.getKey(), fieldName ) ) );
            }
        }

        return notif;

    }

    /**
     * Password validation.
     *
     * @param passwd
     *         the passwd
     * @param whichField
     *         the which field
     *
     * @return the string
     */
    public static String passwordValidation( String passwd, String whichField ) {
        if ( org.apache.commons.lang3.StringUtils.isBlank( passwd ) ) {
            return MessageBundleFactory.getMessage( Messages.PASSWORD_CANNOT_BE_NULL_OR_EMPTY.getKey(), whichField );
        }
        if ( isPasswordBlacklisted( passwd ) ) {
            return MessageBundleFactory.getMessage( Messages.PASSWORD_IS_BLACKLISTED_BY_THE_SYSTEM.getKey() );
        }
        if ( passwd.length() < MIN_PASSWD_LENGTH ) {
            return MessageBundleFactory.getMessage( Messages.PASSWD_LENGTH_MUST_BE_8_CHAR.getKey(), whichField );
        }
        if ( !passwd.matches( REG_FOR_NATURAL ) ) {
            return MessageBundleFactory.getMessage( Messages.REQUIRE_ATLEAST_ONE_NATURAL_NUMBER.getKey() );
        }
        if ( !passwd.matches( REG_FOR_LOWER_CASE ) ) {
            return MessageBundleFactory.getMessage( Messages.REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET.getKey() );
        }
        if ( !passwd.matches( REG_FOR_UPPER_CASE ) ) {
            return MessageBundleFactory.getMessage( Messages.REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET.getKey() );
        }
        if ( !passwd.matches( REG_FOR_SPECIAL_CHAR ) ) {
            return MessageBundleFactory.getMessage( Messages.REQUIRE_ATLEAST_ONE_SPECIAL_CHAR.getKey() );
        }
        if ( !passwd.matches( REG_FOR_WHITE_SPACE ) ) {
            return MessageBundleFactory.getMessage( Messages.NO_WHITE_SPCE_ALLOWED.getKey() );
        }
        return null;
    }

    /**
     * Is password blacklisted boolean.
     *
     * @param passwd
     *         the passwd
     *
     * @return the boolean
     */
    private static boolean isPasswordBlacklisted( String passwd ) {
        String filePath = PropertiesManager.getBlacklistFilePath();
        try ( BufferedReader br = new BufferedReader( new FileReader( filePath ) ) ) {
            String line;
            while ( ( line = br.readLine() ) != null ) {
                if ( line.trim().equals( passwd ) ) {
                    return true;
                }
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return false;
    }

    /**
     * a utility function to make sure if the provided email is valid or not.
     *
     * @param email
     *         the email
     *
     * @return true, if is valid email address
     */
    public static boolean isValidEmailAddress( String email ) {
        return EmailValidator.getInstance().isValid( email );
    }

    /**
     * a utility function to check whether the provided phone number syntax is correct.
     *
     * @param phoneNo
     *         the phone no
     *
     * @return true, if is valid phone number
     */
    public static boolean isValidPhoneNumber( String phoneNo ) {
        Pattern pattern = Pattern.compile( REGEX_FOR_PHONE_NUMBER );
        return pattern.matcher( phoneNo ).matches();
    }

    /**
     * Is valid html boolean.
     *
     * @param html
     *         the html
     *
     * @return the boolean
     */
    public static boolean isValidHtml( String html ) {
        if ( org.apache.commons.lang3.StringUtils.isEmpty( html ) ) {
            return true;
        }

        var cleanHtml = Jsoup.clean( html, customSafelist );
        cleanHtml = Jsoup.parse( cleanHtml ).body().html();

        if ( log.isDebugEnabled() ) {
            log.debug( "{}\ncleaned to:\n{}", html, cleanHtml );
        }
        return Jsoup.isValid( html, customSafelist );
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
        return Pattern.compile( ALLOWED_SPECIAL_CHARACTERS, Pattern.CASE_INSENSITIVE ).matcher( fieldVal ).matches();
    }

    /**
     * Validate positive integer boolean.
     *
     * @param fieldVal
     *         the field val
     *
     * @return the boolean
     */
    public static boolean validatePositiveInteger( String fieldVal ) {
        return Pattern.compile( ALLOWED_POSITIVE_INTEGER, Pattern.CASE_INSENSITIVE ).matcher( fieldVal ).matches();
    }

    /**
     * Validate uuid.
     *
     * @param uuid
     *         the uuid
     */
    public static void validateUUID( String uuid ) {
        try {
            UUID.fromString( uuid );
        } catch ( IllegalArgumentException exception ) {
            ExceptionLogger.logException( exception, ValidationUtils.class );
            throw new SusException( exception.getMessage() );
        }

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
        return isNotNullOrEmpty( fieldVal ) && Pattern.compile( UUID_VALIDATION_REGEX ).matcher( fieldVal ).matches();
    }

    /**
     * Validate special characters.
     *
     * @param fieldVal
     *         the field val
     * @param fieldName
     *         the field name
     */
    public static void validateSpecialCharacters( String fieldVal, String fieldName ) {
        Pattern pattern = Pattern.compile( ALLOWED_SPECIAL_CHARACTERS, Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher( fieldVal );
        if ( !matcher.matches() ) {
            throw new SusException( new Exception( prepareInvalidArgumentMessage( fieldName ) ), ValidationUtils.class );
        }
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
        return MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE.getKey(), fieldName );
    }

    /**
     * Trim string.
     *
     * @param srcString
     *         the src string
     *
     * @return the string
     */
    public static String trimString( String srcString ) {
        if ( srcString == null ) {
            return "";
        }
        return srcString.trim();
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

        if ( ValidationUtils.isNullOrEmpty( srcString ) || ( null == token ) ) {
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
     * Validate cb 2 access with wen login.
     */
    public static void validateCB2AccessWithWENLogin() {
        String token = BundleUtils.getTokenFromMessage( PhaseInterceptorChain.getCurrentMessage() );
        validateCB2AccessWithWENLoginByToken( token );
    }

    /**
     * Validate cb 2 access with wen login.
     *
     * @param token
     *         the token
     */
    public static void validateCB2AccessWithWENLoginByToken( String token ) {
        var sessionDTO = TokenizedLicenseUtil.getSessionDTO( token );
        if ( sessionDTO != null && sessionDTO.isWenLogin() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CB2_FEATURES_NOT_AVAILABLE_WITH_WEN_LOGIN.getKey(),
                    sessionDTO.getUser().getUserUid() ) );
        }
    }

}