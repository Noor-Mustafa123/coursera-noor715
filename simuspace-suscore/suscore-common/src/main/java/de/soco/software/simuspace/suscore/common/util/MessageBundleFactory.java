package de.soco.software.simuspace.suscore.common.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * This class, `MessageBundleFactory`, is responsible for working with resource bundles to retrieve and format localized messages. It
 * provides methods to fetch messages in different languages based on user settings and formats them according to the provided arguments.
 * The class is designed with static methods to prevent instantiation. It uses `ResourceBundle` and `MessageFormat` classes for retrieving
 * and formatting messages.
 *
 * Constants:
 * - DEFAULT_LOCALE: Default locale key.
 * - EN_LOCALE: Default language code (English).
 * - LANGUAGE_FILE: Language file reference.
 *
 * Methods:
 * - `getString`: Retrieves a localized string from the resource bundle based on the user language and message key.
 * - `format`: Formats a message string based on the user language, pattern, and provided arguments.
 * - `getExternalMessage`: Retrieves a formatted message string based on user token, translation settings, and additional parameters.
 * - `getMessage`: Fetches and formats a message string using a message key and parameters. It considers user translation settings.
 * - `getDefaultMessage`: Retrieves and formats the default message string based on the message key and parameters, using the default
 * locale.
 *
 * Designed to be used statically and provides internationalization support for messages.
 *
 * Author: Zeeshan Jamal
 */
public class MessageBundleFactory {

    /**
     * constant DEFAULT_LOCALE for loading default locale of simuspace
     */
    public static final String DEFAULT_LOCALE = "default.locale";

    /**
     * constant DEFAULT_LOCALE for loading default locale which is English
     */
    public static final String EN_LOCALE = "en";

    /**
     * constant LANGUAGE_FILE for loading default locale
     */
    public static final String LANGUAGE_FILE = "language.txt";

    /**
     * private constructor to avoid instantiation
     */
    private MessageBundleFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves a string from the resource bundle based on the given user language and key.
     *
     * @param userLang
     *         the language code representing the user language
     * @param key
     *         the key for the desired string in the resource bundle
     *
     * @return the string associated with the specified key and user language
     */
    public static String getString( String userLang, String key ) {
        return ResourceBundle.getBundle( "MessagesBundle", new Locale( userLang ) ).getString( key );
    }

    /**
     * Formats a message based on the given user language, pattern, and arguments.
     *
     * @param userLang
     *         the language code representing the user language
     * @param pattern
     *         the pattern string to format
     *
     * @
     */
    public static String format( String userLang, String pattern, Object... arguments ) {
        Locale loc = new Locale( userLang );
        MessageFormat formatter = new MessageFormat( pattern, loc );
        return formatter.format( arguments );
    }

    /**
     * Retrieves a message based on the provided parameters and user language.
     *
     * @param hasTranslation
     *         a boolean indicating if translation is required
     * @param token
     *         a string representing the user authentication token
     * @param key
     *         the key for the desired message
     * @param params
     *         optional parameters to be included in the message
     *
     * @return the formatted message based on the input parameters and user language
     */
    public static String getExternalMessage( boolean hasTranslation, String token, String key, Object... params ) {
        UserDTO user = TokenizedLicenseUtil.getUser( token );
        if ( hasTranslation && user != null ) {
            String userLang = EN_LOCALE;
            if ( user.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
                return format( EN_LOCALE, getString( EN_LOCALE, key ), params );
            } else if ( user.getUserDetails() != null
                    && StringUtils.isNotNullOrEmpty( user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage() ) ) {
                userLang = user.getUserDetails().iterator().next().getLanguage();
            }
            return format( userLang, getString( userLang, key ), params );
        }
        return format( EN_LOCALE, getString( EN_LOCALE, key ), params );
    }

    /**
     * Retrieves a message based on the provided key and parameters.
     *
     * @param key
     *         the key for the desired message
     * @param params
     *         any parameters to be included in the message
     *
     * @return the formatted message based on the key and parameters
     */
    public static String getMessage( String key, Object... params ) {
        return getExternalMessage( PropertiesManager.hasTranslation(),
                BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ), key, params );
    }

    /**
     * Retrieves the default message based on the provided key and parameters.
     *
     * @param key
     *         the key for the desired message
     * @param params
     *         any parameters to be included in the message
     *
     * @return the formatted message based on the key and parameters
     */
    public static String getDefaultMessage( String key, Object... params ) {
        return format( EN_LOCALE, getString( EN_LOCALE, key ), params );
    }

}