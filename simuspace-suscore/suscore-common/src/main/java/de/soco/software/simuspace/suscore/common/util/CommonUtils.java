/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class CommonUtils is responsible for creating utility methods which can be used by other classes.
 *
 * @author Nasir.Farooq
 * @since 1.0
 */
@Log4j2
public class CommonUtils {

    /**
     * The Constant SORT_ASC.
     */
    public static final int SORT_ASC = 1;

    /**
     * The Constant SORT_DESC.
     */
    public static final int SORT_DESC = -1;

    /**
     * The Constant BASE_URL_UUID_REGEX.
     */
    private static final String BASE_URL_UUID_REGEX = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";

    /**
     * private constructor for static class.
     */
    private CommonUtils() {
        // private
    }

    /**
     * Checks if is integer.
     *
     * @param value
     *         the value
     *
     * @return true, if is integer
     */
    public static boolean isInteger( String value ) {
        if ( ValidationUtils.isNotNullOrEmpty( value ) ) {
            return value.matches( "\\d{1,9}$" );
        }
        return false;
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
     * Validate url boolean.
     *
     * @return the boolean
     */
    public static boolean validateURL() {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        try {
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequestWithoutRetry( PropertiesManager.getElasticSearchURL(),
                    requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            JsonNode jsonNode = JsonUtils.toJsonNode( responseString );
            String server = jsonNode.get( "cluster_name" ).asText();
            if ( server == null ) {
                return false;
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            return false;
        }
        return true;
    }

    /**
     * List to string.
     *
     * @param ids
     *         the ids
     *
     * @return the string
     */
    public static < T > String listToString( List< T > ids ) {
        final StringBuilder idList = new StringBuilder();
        for ( final T id : ids ) {
            if ( ValidationUtils.isNotNullOrEmpty( idList.toString() ) ) {
                idList.append( " , " );
            }
            idList.append( id );
        }
        return idList.toString();
    }

    /**
     * Cast object to list.
     *
     * @param object
     *         the object
     *
     * @return the list object
     */
    @SuppressWarnings( "unchecked" )
    public static < T extends List< ? > > T castObjectToList( Object object ) {
        if ( object instanceof List< ? > ) {
            return ( T ) object;
        } else {
            return null;
        }
    }

    /**
     * The method finds any port which is not being used by any of the operating system process.
     *
     * @return int an open port
     */
    public static int getOpenPort() {
        final int minPort = 1024;
        final int maxPort = 49151;
        for ( int i = minPort; i <= maxPort; i++ ) {
            int randomNum = ThreadLocalRandom.current().nextInt( 1024, 49151 + 1 );
            try ( ServerSocket serverSocket = new ServerSocket( randomNum ); DatagramSocket dataSocket = new DatagramSocket( randomNum ) ) {
                serverSocket.setReuseAddress( true );
                dataSocket.setReuseAddress( true );
                return randomNum;
            } catch ( final IOException e ) {
                log.error( e.getMessage(), e );
            }

        }
        throw new SusException( "Port not available!" );
    }

    /**
     * A method of reading a property from the jar's manifest provided.
     *
     * @param clazz
     *         Class on a running jar
     * @param property
     *         A jar's manifest key
     *
     * @return a property value
     */
    public static String readPropertyFromJarManifest( Class< ? > clazz, String property ) {
        String value = ConstantsString.EMPTY_STRING;
        try {
            final URI jarURI = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
            if ( ( null != jarURI ) && ( null != jarURI.getPath() ) ) {
                final File jarFile = new File( jarURI.getPath() );
                if ( jarURI.getPath().endsWith( ".jar" ) && jarFile.isFile() ) {
                    try ( JarFile jar = new JarFile( jarFile ) ) {

                        value = jar.getManifest().getMainAttributes().getValue( property );
                        return null != value ? value : ConstantsString.EMPTY_STRING;
                    }
                }
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
        return value;
    }

    /**
     * A util function to crop the file extension from the provided file name
     *
     * @param fileName
     *         the filename
     *
     * @return the extension
     */
    public static String getFileExtension( String fileName ) {
        if ( StringUtils.isEmpty( fileName ) ) {
            return ConstantsString.EMPTY_STRING;
        }
        return fileName.substring( fileName.lastIndexOf( ConstantsString.DOT ) + ConstantsInteger.INTEGER_VALUE_ONE );
    }

    /**
     * Encodes the name according the user agent of the client. In case the encoding is not supported for the provided browser detail, the
     * name is return as is. @NullPointerException is thrown in case the fileName object is provided as null.
     *
     * @param fileName
     *         the filename
     * @param browserDetails
     *         the browserDetails
     *
     * @return the encoded filename if no exception occurred, else the original filename
     */
    public static String getEncodedName( String fileName, String browserDetails ) {

        Charset encoding;
        if ( browserDetails != null && browserDetails.toLowerCase().contains( ConstantsString.WINDOWS_PREFIX ) ) {
            encoding = Charset.forName( ConstantsString.CHARSET_WINDOWS );
        } else {
            encoding = Charset.forName( ConstantsString.UTF8.toUpperCase() );
        }
        try {
            fileName = new String( fileName.getBytes( encoding ), ConstantsString.UTF8.toUpperCase() );
        } catch ( UnsupportedEncodingException e ) {
            log.error( e.getMessage(), e );
        }
        return fileName;
    }

    /**
     * This utility function can be used to replace single backslashes in a json with double backslashes to avoid escape character
     * exception
     *
     * @param json
     *         the json
     *
     * @return the json with double back slashes
     */
    public static String findAndReplaceSingleBackSlashes( String json ) {
        if ( json.contains( ConstantsString.SINGLE_BACKSLASH ) && !json.contains( ConstantsString.DOUBLE_BACKSLASH ) ) {
            json = json.replace( ConstantsString.SINGLE_BACKSLASH, ConstantsString.DOUBLE_BACKSLASH );
        }
        return json;
    }

    /**
     * Removes the slashes.
     *
     * @param license
     *         the license
     *
     * @return the string
     */
    public static String removeSlashes( String license ) {

        return license.replace( ConstantsString.BACK_SLASHED_DOULBLE_QUOTES, ConstantsString.DOUBLE_QUOTE_STRING )
                .replace( ConstantsString.NEW_LINE, ConstantsString.EMPTY_STRING );
    }

    /**
     * Gets the hex.
     *
     * @param uuid
     *         the uuid
     * @param versionId
     *         the version id
     *
     * @return the hex
     */
    @Deprecated
    public static String getHex( UUID uuid, int versionId ) {
        return DigestUtils.shaHex( uuid + ConstantsString.COLON + versionId );
    }

    /**
     * Gets sha 1 hex.
     *
     * @param uuid
     *         the uuid
     * @param versionId
     *         the version id
     *
     * @return the sha 1 hex
     */
    public static String getSha1Hex( UUID uuid, int versionId ) {
        return DigestUtils.sha1Hex( uuid + ConstantsString.COLON + versionId );
    }

    /**
     * Gets the base url.
     *
     * @param baseUrl
     *         the base url
     *
     * @return the base url
     */
    public static String getBaseUrl( String baseUrl ) {
        try {
            var parsedURL = new URL( baseUrl );
            return parsedURL.getProtocol() + ConstantsString.COLON + ConstantsString.DOUBLE_FORWARD_SLASH + parsedURL.getAuthority();
        } catch ( MalformedURLException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Extract UUID from url.
     *
     * @param url
     *         the url
     *
     * @return the string builder
     */
    public static StringBuilder extractUUIDFromUrl( String url ) {
        StringBuilder uuidString = new StringBuilder();
        final Matcher matcher = Pattern.compile( BASE_URL_UUID_REGEX ).matcher( url );
        while ( matcher.find() ) {
            uuidString.append( matcher.group( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        }
        return uuidString;
    }

    /**
     * Get current user.
     *
     * @param authToken
     *         the auth token
     *
     * @return current userDTO
     */
    public static UserDTO getCurrentUser( String authToken ) {
        String url = PropertiesManager.getLocationURL() + "/api/system/user/current";
        SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeadersWithAuthToken( authToken ) );
        String json = JsonUtils.toJson( susResponse.getData() );
        return JsonUtils.jsonToObject( json, UserDTO.class );
    }

    /**
     * Get current user.
     *
     * @param authToken
     *         the auth token
     * @param url
     *         the url
     *
     * @return current userDTO
     */
    public static UserDTO getCurrentUser( String authToken, String url ) {
        String api = url + "/api/system/user/current";
        SusResponseDTO susResponse = SuSClient.getRequest( api, prepareHeadersWithAuthToken( authToken ) );
        String json = JsonUtils.toJson( susResponse.getData() );
        return JsonUtils.jsonToObject( json, UserDTO.class );
    }

    /**
     * Get current user uid.
     *
     * @param authToken
     *         the auth token
     * @param url
     *         the url
     *
     * @return current userDTO
     */
    public static String getCurrentUserUid( String authToken, String url ) {
        UserDTO user = getCurrentUser( authToken, url );
        return ( user != null && user.getUserUid() != null ? user.getUserUid() : null );
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param authToken
     *         the auth token
     *
     * @return requestHeaders
     */
    public static Map< String, String > prepareHeadersWithAuthToken( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, authToken );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, authToken );
        return requestHeaders;
    }

    /**
     * It adds headers for required for communication with server.<br>
     *
     * @param jobToken
     *         the job token
     *
     * @return requestHeaders
     */
    public static Map< String, String > prepareHeadersWithJobToken( String jobToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, jobToken );
        return requestHeaders;
    }

    /**
     * Log out user by token.
     *
     * @param userToken
     *         the user token
     *
     * @return true, if successful
     */
    public static boolean logOutUserByAuthToken( String userToken ) {
        Map< String, String > logoutToken = new HashMap<>();
        logoutToken.put( "token", userToken );
        String urlLogout = PropertiesManager.getLocationURL() + "/api/auth/logout";
        SusResponseDTO response = SuSClient.postRequest( urlLogout, JsonUtils.toJson( logoutToken ),
                prepareHeadersWithAuthToken( userToken ) );
        return response != null && response.getSuccess();

    }

    public static String checkAndRemoveExtensionFromName( String name, String className ) {
        if ( !name.contains( ConstantsString.DOT ) ) {
            return name;
        }
        String extension = name.substring( name.lastIndexOf( ConstantsString.DOT ) );
        var extensionsToRemove = PropertiesManager.getInstance().getFileExtensions( className );
        if ( CollectionUtils.isEmpty( extensionsToRemove ) ) {
            return name;
        } else if ( extensionsToRemove.contains( extension ) ) {
            return name.replace( extension, ConstantsString.EMPTY_STRING );
        }
        return name;
    }

    /**
     * Resolve language string.
     *
     * @param token
     *         the token
     *
     * @return the string
     */
    public static String resolveLanguage( String token ) {
        return PropertiesManager.hasTranslation()
                ? TokenizedLicenseUtil.getUserLanguage( token ) != null ? TokenizedLicenseUtil.getUserLanguage( token )
                : ConstantsString.DEFAULT_LANGUAGE
                : ConstantsString.DEFAULT_LANGUAGE;
    }

}
