package de.soco.software.simuspace.suscore.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The type Tokenized license util.
 */
@Log4j2
public class TokenizedLicenseUtil {

    private TokenizedLicenseUtil() {
        // private constructor to hide public one
    }

    /**
     * The constant tokenizedLicenseEntryMap.
     */
    private static Map< String, SuSCoreSessionDTO > tokenizedLicenseEntryMap = new HashMap<>();

    /**
     * Gets user.
     *
     * @param token
     *         the token
     *
     * @return the user
     */
    public static UserDTO getUser( String token ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "getting user with token " + token );
        }
        var sessionDTO = getSessionDTO( token );
        if ( log.isTraceEnabled() ) {
            log.trace( "session found against token " + token + " is: " + sessionDTO );
        }
        return sessionDTO == null ? null : sessionDTO.getUser();
    }

    /**
     * Gets user.
     *
     * @param token
     *         the token
     *
     * @return the user
     */
    public static UserDTO getNotNullUser( String token ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "getting user with token " + token );
        }
        var sessionDTO = getSessionDTO( token );
        if ( log.isTraceEnabled() ) {
            log.trace( "session found against token " + token + " is: " + sessionDTO );
        }
        if ( sessionDTO == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_NOT_FOUND_WITH_TOKEN.getKey(), token ) );
        }
        return sessionDTO.getUser();
    }

    /**
     * Gets user uid.
     *
     * @param userId
     *         the user id
     *
     * @return the user uid
     */
    public static String getUserUID( String userId ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "getting userUid with userId {}", userId );
        }
        return tokenizedLicenseEntryMap.values().stream()
                .filter( suSCoreSessionDTO -> userId.equals( suSCoreSessionDTO.getUser().getId() ) )
                .map( suSCoreSessionDTO -> suSCoreSessionDTO.getUser().getUserUid() )
                .findFirst()
                .orElse( null );
    }

    /**
     * Is wen login boolean.
     *
     * @param token
     *         the token
     *
     * @return the boolean
     */
    public static Boolean isWenLogin( String token ) {
        var sessionDTO = getSessionDTO( token );
        return sessionDTO == null ? null : sessionDTO.isWenLogin();
    }

    /**
     * Gets session dto.
     *
     * @param token
     *         the token
     *
     * @return the session dto
     */
    public static SuSCoreSessionDTO getSessionDTO( String token ) {
        return tokenizedLicenseEntryMap.get( token );
    }

    /**
     * Does token exist boolean.
     *
     * @param token
     *         the token
     *
     * @return the boolean
     */
    public static boolean doesTokenExist( String token ) {
        return tokenizedLicenseEntryMap.containsKey( token );
    }

    /**
     * Remove entry.
     *
     * @param token
     *         the token
     */
    public static void removeEntry( String token ) {
        tokenizedLicenseEntryMap.remove( token );
    }

    /**
     * Add session.
     *
     * @param token
     *         the token
     * @param sessionDTO
     *         the session dto
     */
    public static void addSession( String token, SuSCoreSessionDTO sessionDTO ) {
        if ( log.isTraceEnabled() ) {
            log.trace( "Adding session to map: " + sessionDTO );
        }
        tokenizedLicenseEntryMap.put( token, sessionDTO );
    }

    /**
     * Gets map.
     *
     * @return the map
     */
    public static Map< String, SuSCoreSessionDTO > getMap() {
        return tokenizedLicenseEntryMap;
    }

    /**
     * Update map.
     *
     * @param newMap
     *         the new map
     */
    public static void updateMap( Map< String, SuSCoreSessionDTO > newMap ) {
        tokenizedLicenseEntryMap = newMap;
    }

    /**
     * Gets the user language.
     *
     * @param token
     *         the token
     *
     * @return the user language
     */
    public static String getUserLanguage( String token ) {
        var user = getUser( token );
        if ( log.isTraceEnabled() ) {
            log.trace( "user found against token " + token + " is: " + user );
        }
        return ( user != null && CollectionUtils.isNotEmpty( user.getUserDetails() ) )
                ? user.getUserDetails().get( ConstantsInteger.INTEGER_VALUE_ZERO ).getLanguage()
                : null;
    }

    /**
     * Update user in session.
     *
     * @param newUserDto
     *         the new user dto
     */
    public static void updateUserInSession( UserDTO newUserDto ) {
        tokenizedLicenseEntryMap.values().stream().filter( session -> session.getUser().getId().equals( newUserDto.getId() ) )
                .forEach( session -> session.setUser( newUserDto ) );
    }

}
