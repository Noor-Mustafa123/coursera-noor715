package de.soco.software.simuspace.suscore.authentication.service.rest.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Response;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.authentication.service.rest.AuthService;
import de.soco.software.simuspace.suscore.authentication.service.rest.OAuthService;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.OAuthConstants;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.OAuthStateDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDirectoryAttributeEntity;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.user.dao.DirectoryDAO;
import de.soco.software.simuspace.suscore.user.manager.DirectoryManager;

@Log4j2
@NoArgsConstructor
@Getter
@Setter
public class OAuthServiceImpl extends SuSBaseService implements OAuthService {

    /**
     * User Directory DAO Bean
     */
    private DirectoryDAO directoryDAO;

    /**
     * The Entity manager factory Bean.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Auth Service Bean
     */
    private AuthService authService;

    /**
     * User Directory Manager
     */
    private DirectoryManager directoryManager;

    @Override
    public Response oauthUserDirectoriesList() {
        try {
            List< SuSUserDirectoryDTO > userDirectoryDTOList = directoryManager.getListOfAllOAuthUserDirectories();
            return ResponseUtils.success( userDirectoryDTOList );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response oauthAuthorize( UUID directoryId ) {
        Response redirectionEndpoint = null;
        try {

            // gets the userDirectoryEntity with the attributes set
            SuSUserDirectoryEntity suSUserDirectoryEntity = getUserDirectoryEntityfromDB( directoryId );
            UserDirectoryAttributeEntity userDirectoryAttributeEntity = suSUserDirectoryEntity.getUserDirectoryAttribute();
            OAuthStateDTO oauthStateDTO = new OAuthStateDTO();
            oauthStateDTO.setDirectoryId( directoryId );
            oauthStateDTO.setProviderType( userDirectoryAttributeEntity.getProviderType() );

            // condition for OIDC provider
            if ( StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.OIDC ) ) {
                OAuthClientRequest authRequest = prepareAuthRequestForOIDCProvider( userDirectoryAttributeEntity, oauthStateDTO );
                redirectionEndpoint = Response.status( 302 )
                        .location( new URI( authRequest.getLocationUri() ) )
                        .build();
            }
            // condition for Non-OIDC provider
            if ( StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.NON_OIDC ) ) {
                OAuthClientRequest authRequest = prepareAuthRequestForNonOIDCProvider( userDirectoryAttributeEntity, oauthStateDTO );
                redirectionEndpoint = Response.status( 302 )
                        .location( new URI( authRequest.getLocationUri() ) )
                        .build();
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            return handleException( e );
        }
        if ( redirectionEndpoint == null ) {
            log.warn( "redirectEndpointResponse: " + redirectionEndpoint + "its value is null so no response is being sent" );
            throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.NO_SUCH_DIRECTORY_EXISTS.getKey() ) );
        }
        log.debug( "redirectURIForDebugging: " + redirectionEndpoint.getLocation() );
        return redirectionEndpoint;
    }

    @Override
    public Response oauthAccessToken( String stateJson, String authorization_code, String scope, String authUser, String prompt ) {
        Response redirectionEndpoint = null;
        log.debug( "State for oauthAccessToken : " + stateJson );
        try {
            stateJson = URLDecoder.decode( stateJson, StandardCharsets.UTF_8 );// Decoding
            if ( !checkIfStateParameterMatchesInOAuth( stateJson ) ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.STATE_MISMATCH.getKey() ) );
                throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.STATE_MISMATCH.getKey() ) );
            }

            OAuthStateDTO oauthStateDTO = JsonUtils.jsonToObject( stateJson, OAuthStateDTO.class );

            // get tokenEndpoint
            SuSUserDirectoryEntity userDirectoryEntity = getUserDirectoryEntityfromDB( oauthStateDTO.getDirectoryId() );
            UserDirectoryAttributeEntity userDirectoryAttributeEntity = userDirectoryEntity.getUserDirectoryAttribute();
            String tokenEndpoint = userDirectoryAttributeEntity.getTokenEndpoint();
            String client_secret = EncryptAndDecryptUtils.decryptString( userDirectoryAttributeEntity.getClientSecret() );
            String redirectUri = userDirectoryAttributeEntity.getRedirectUri();

            // Build the token request using the authorization code
            OAuthAccessTokenResponse accessTokenResponse = prepareAndValidateAccessTokenResponse( tokenEndpoint,
                    userDirectoryAttributeEntity,
                    client_secret, redirectUri, authorization_code, oauthStateDTO );

            //*  Retrieve access and refresh token values encoding to avoid path conflicts
            String accessToken = accessTokenResponse.getAccessToken();
            String encodedAccessToken = URLEncoder.encode( accessToken, StandardCharsets.UTF_8 );
            String refreshToken = accessTokenResponse.getRefreshToken();
            String idToken = null;
            if ( StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.OIDC ) ) {
                idToken = accessTokenResponse.getParam( "id_token" );
            }

            // for refresh token to facebook specifically we need to send a second request with to exchange the accessToken with the refreshToken
            if ( userDirectoryEntity.getName() != null &&
                    userDirectoryEntity.getName().toLowerCase().contains( "facebook" ) ) {
                // it is using facebook directory type
                JsonNode node = exchangeFacebookToken( userDirectoryAttributeEntity.getClientId(),
                        userDirectoryAttributeEntity.getClientSecret(), accessTokenResponse.getAccessToken() );
                log.debug( "facebook Token node: " + node.asText() );
                // changing the value of the refreshToken
                refreshToken = node.get( "access_token" ).asText();
            }

            String encodedRefreshToken = null;
            if ( !StringUtils.isBlank( refreshToken ) ) {
                encodedRefreshToken = URLEncoder.encode( refreshToken, StandardCharsets.UTF_8 );
            }

            JsonNode userInfoNOde = getUserProfileInfo( accessToken, userDirectoryAttributeEntity );
            log.debug( "JSON_NODE_USER_INFO: " + userInfoNOde.toPrettyString() );

            // Log token retrieval for debugging purpose
            log.debug( "Access token retrieved: " + accessToken );
            log.debug( "Refresh token retrieved: " + ( refreshToken != null ? refreshToken : "Not provided : " + refreshToken ) );
            log.debug( "ID token retrieved: " + ( idToken != null ? idToken : "Non-OIDC Type Provider" ) );

            // Get the qNumber for oidc and non-oidc type providers
            String qNumber = null;
            if ( StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.OIDC ) ) {
                qNumber = userInfoNOde.path( "sub" ).asText();
            } else if ( StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.NON_OIDC ) ) {
                qNumber = userInfoNOde.path( "id" ).asText();
            }
            stateJson = URLEncoder.encode( stateJson, StandardCharsets.UTF_8 ); // Encoding
            // url pointing to the checkUserAndRegister method
            String url = PropertiesManager.getLocationURL() + "/api/oauth/" + qNumber + "/token/"
                    + encodedAccessToken + "/refreshToken/" + encodedRefreshToken + "/token_id/"
                    + idToken + "/state/" + stateJson;

            log.info( "url:" + url );
            redirectionEndpoint = Response.status( 302 ).location( new URI( url ) ).build();
        } catch ( Exception e ) {
            log.error( "Error during OAuth token fetch", e );
            return handleException( e );
        }
        return redirectionEndpoint;
    }

    @Override
    public Response checkUserAndRegister( String qNumber, String x_oauth_token, String refreshToken, String oauth_token_id,
            String stateJson ) {
        Response response = null;
        try {
            stateJson = URLDecoder.decode( stateJson, StandardCharsets.UTF_8 );// Decoding
            //Checking state
            if ( !checkIfStateParameterMatchesInOAuth( stateJson ) ) {
                // throw an exception if needed
                return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
            }
            OAuthStateDTO oAuthStateDTO = JsonUtils.jsonToObject( stateJson, OAuthStateDTO.class );
            String state = EncryptAndDecryptUtils.decryptString( oAuthStateDTO.getState() );
            SuSUserDirectoryEntity suSUserDirectoryEntity = getUserDirectoryEntityfromDB( oAuthStateDTO.getDirectoryId() );
            UserDirectoryAttributeEntity userDirectoryAttributeEntity = suSUserDirectoryEntity.getUserDirectoryAttribute();
            String decodedAccessToken = URLDecoder.decode( x_oauth_token, StandardCharsets.UTF_8 );
            String refreshTokenDecoded = null;
            if ( !StringUtils.isBlank( refreshToken ) ) {
                refreshTokenDecoded = URLDecoder.decode( refreshToken, StandardCharsets.UTF_8 );
            }
            JsonNode userProfileNode = getUserProfileInfo( decodedAccessToken, userDirectoryAttributeEntity );
            String userPassword = FileUtils.getSha256CheckSum( qNumber ); // currently for oauth user the password is the encrypted userId
            // here "\" is an escape character for adding " inside a string
            String json = String.format( "{\"uid\":\"%s\",\"password\":\"%s\"}", qNumber, userPassword );
            response = authService.registerUserAfterOAuth( oauth_token_id, decodedAccessToken, refreshTokenDecoded, state, json,
                    userProfileNode, suSUserDirectoryEntity );
        } catch ( final Exception e ) {
            return handleException( e );
        }

        return response;
    }

    // UTILITY METHODS BELOW

    private JsonNode callWellKnownUrl( String wellKnownUrl ) {

        JsonNode wellKnownUrlsJsonNode = null;
        // Request header
        Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        try {
            // Send request to oauth Provider using SusClient.getExternalRequest()
            CloseableHttpResponse httpResponse = SuSClient.getExternalRequest( wellKnownUrl, requestHeaders );
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseJsonString = EntityUtils.toString( responseEntity, "UTF-8" );
            log.debug( "WELL_KNOWN_URL RESPONSE: " + responseJsonString );
            wellKnownUrlsJsonNode = JsonUtils.toJsonNode( responseJsonString );
        } catch ( Exception e ) {
            throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.COMMUNICATION_FAILURE.getKey() ), e );
        }
        return wellKnownUrlsJsonNode;
    }

    // WILL RETURN THE CURRENT OAUTH_DIRECTORY ATTRIBUTES THAT ARE BEING USED TO SIGN UP USER
    private SuSUserDirectoryEntity getUserDirectoryEntityfromDB( UUID directoryID ) {
        SuSUserDirectoryEntity responseEntity = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Retrieve the OAuth Provider from the db by name
            SuSUserDirectoryEntity suSUserDirectoryEntity = directoryDAO.readDirectory( entityManager,
                    directoryID );
            UserDirectoryAttributeEntity userDirectoryAttributeEntity = suSUserDirectoryEntity.getUserDirectoryAttribute();
            // Check if the type is OIDC then use the wellKnownUrl by default
            if ( StringUtils.equals( userDirectoryAttributeEntity.getProviderType(), OAuthConstants.OIDC ) ) {
                // Call the well-known URL to retrieve the necessary information
                JsonNode wellKnownUrlsJsonNode = callWellKnownUrl( userDirectoryAttributeEntity.getWellKnownUrl() );
                // Loops over the json node field names to get the authorizationEndpoint without case problems for different wellKnownUrl formats
                Iterator< String > fieldNames = wellKnownUrlsJsonNode.fieldNames();
                log.debug( fieldNames );
                while ( fieldNames.hasNext() ) {
                    String fieldName = fieldNames.next();
                    if ( fieldName.equalsIgnoreCase( "authorization_endpoint" ) ) {
                        String authEndpoint = wellKnownUrlsJsonNode.get( fieldName ).asText();
                        userDirectoryAttributeEntity.setAuthorizationEndpoint( authEndpoint );
                        log.debug( "Authorization Endpoint: " + authEndpoint );
                    }
                    if ( fieldName.equalsIgnoreCase( "token_endpoint" ) ) {
                        String tokenEndpoint = wellKnownUrlsJsonNode.get( fieldName ).asText();
                        userDirectoryAttributeEntity.setTokenEndpoint( tokenEndpoint );
                        log.debug( "Token Endpoint: " + tokenEndpoint );
                    }
                    if ( fieldName.equalsIgnoreCase( "userinfo_endpoint" ) ) {
                        String user_info_endpoint = wellKnownUrlsJsonNode.get( fieldName ).asText();
                        userDirectoryAttributeEntity.setUserInfoEndpoint( user_info_endpoint );
                        log.debug( "userInfoEndpoint: " + user_info_endpoint );
                    }
                    if ( fieldName.equalsIgnoreCase( "revocation_endpoint" ) ) {
                        String revocation_endpoint = wellKnownUrlsJsonNode.get( fieldName ).asText();
                        userDirectoryAttributeEntity.setRevocationEndpoint( revocation_endpoint );
                        log.debug( "revocationEndpoint: " + revocation_endpoint );
                    }
                }
            } else if ( StringUtils.equals( userDirectoryAttributeEntity.getProviderType(), OAuthConstants.NON_OIDC ) ) {
                log.debug( "the directory is of type non-oidc" );
            }
            suSUserDirectoryEntity.setUserDirectoryAttribute( userDirectoryAttributeEntity );
            responseEntity = suSUserDirectoryEntity;
        } catch ( Exception e ) {
            handleException( e );
        } finally {
            entityManager.close();
            //! Close the EntityManager
//            if ( entityManager != null && entityManager.isOpen() ) {
//                entityManager.close();
//            }
        }

        // Return user entity with set attributes
        return responseEntity;
    }

    private JsonNode getUserProfileInfo( String authToken, UserDirectoryAttributeEntity directoryAttributeEntity ) {
        JsonNode jsonNode = null;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + authToken );

            CloseableHttpResponse closeableHttpResponse = SuSClient
                    .getExternalRequest( directoryAttributeEntity.getUserInfoEndpoint(), requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    private boolean checkIfStateParameterMatchesInOAuth( String stateJson ) {
        //DESERIALIZING STATE JSON
        OAuthStateDTO oauthStateDTO = JsonUtils.jsonToObject( stateJson, OAuthStateDTO.class );
        String state = oauthStateDTO.getState();
        //  checking state for oidc
        if ( StringUtils.isNotBlank( state ) && StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.OIDC ) ) {
            String decryptedString = EncryptAndDecryptUtils.decryptString( state );
            if ( StringUtils.equals( decryptedString, OAuthConstants.STATE_OIDC ) ) {
                return true;
            } else {
                return false;
            }
        }
        //  checking state for non-oidc
        if ( StringUtils.isNotBlank( state ) && StringUtils.equals( oauthStateDTO.getProviderType(), OAuthConstants.NON_OIDC ) ) {
            String decryptedString = EncryptAndDecryptUtils.decryptString( state );
            if ( StringUtils.equals( decryptedString, OAuthConstants.STATE_NON_OIDC ) ) {
                return true;
            } else {
                return false;
            }
        }
//         if no state used
        return true;
    }

    private JsonNode exchangeFacebookToken( String appId, String clientSecret, String shortLivedToken ) {
        JsonNode jsonNode = null;
        try {
            String clientSecretDecrypted = EncryptAndDecryptUtils.decryptString( clientSecret );
            //! Build the Facebook token exchange URL as facebook requires exchange of shortlived token for refresh Token
            String fbTokenUrlFormat = PropertiesManager.getInstance().getProperty( ConstantsFileProperties.FACEBOOK_REFRESH_TOKEN_URL );
            String fbUrl = String.format( fbTokenUrlFormat, URLEncoder.encode( appId, StandardCharsets.UTF_8 ),
                    URLEncoder.encode( clientSecretDecrypted, StandardCharsets.UTF_8 ),
                    URLEncoder.encode( shortLivedToken, StandardCharsets.UTF_8 ) );
            // Optionally, set any headers if required (not needed for simple refreshToken request)
            Map< String, String > requestHeaders = new HashMap<>();

            // Perform the GET request using your existing external request method
            CloseableHttpResponse response = SuSClient.getExternalRequest( fbUrl, requestHeaders );
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );

            // Convert the response string into a JSON node
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    private OAuthClientRequest prepareAuthRequestForOIDCProvider( UserDirectoryAttributeEntity userDirectoryAttributeEntity,
            OAuthStateDTO oauthStateDTO )
            throws OAuthSystemException {
        String encryptedState = EncryptAndDecryptUtils.encryptString( OAuthConstants.STATE_OIDC );
        oauthStateDTO.setState( encryptedState ); // setting state
        // Create OAuthClientRequest for authorization
        log.debug( "OIDC CONDITION RAN" );
        // ! cant directly write the string in the setRedirectURI because google automatically adds the ""
        String redirectUri = userDirectoryAttributeEntity.getRedirectUri();
        log.debug( "oidc scope" + String.join( " ",
                userDirectoryAttributeEntity.getScope() ) );
        String clientId = userDirectoryAttributeEntity.getClientId();
        String stateJson = JsonUtils.toJsonString( oauthStateDTO );
        stateJson = URLEncoder.encode( stateJson, StandardCharsets.UTF_8 );// Encoding
        OAuthClientRequest authRequest = OAuthClientRequest
                .authorizationLocation( userDirectoryAttributeEntity.getAuthorizationEndpoint() )
                .setClientId( clientId )
                .setRedirectURI( redirectUri )
                .setResponseType( userDirectoryAttributeEntity.getResponseType() )
                .setScope( String.join( " ", userDirectoryAttributeEntity.getScope() ) )
                .setState( stateJson )
                .setParameter( "nonce", "nonce_OIDC" )
//                        .setParameter( "prompt", "consent" ) // ! Added for testing as google remembers that consent was already given the first time
//                        .setParameter( "access_type", "offline" ) // ! Added to get a refresh token everytime
                .buildQueryMessage();
        return authRequest;
    }

    private OAuthClientRequest prepareAuthRequestForNonOIDCProvider( UserDirectoryAttributeEntity userDirectoryAttributeEntity,
            OAuthStateDTO oauthStateDTO )
            throws OAuthSystemException {
        String encryptedStateNON = EncryptAndDecryptUtils.encryptString( OAuthConstants.STATE_NON_OIDC );
        oauthStateDTO.setState( encryptedStateNON );
        String stateJson = JsonUtils.toJsonString( oauthStateDTO );
        stateJson = URLEncoder.encode( stateJson, StandardCharsets.UTF_8 );// Encoding
        log.debug( "NON-OIDC CONDITION RAN" );
        String redirectUri = userDirectoryAttributeEntity.getRedirectUri();
        OAuthClientRequest authRequest = OAuthClientRequest
                .authorizationLocation( userDirectoryAttributeEntity.getAuthorizationEndpoint() )
                .setClientId( userDirectoryAttributeEntity.getClientId() )
                .setRedirectURI( redirectUri )
                .setResponseType( userDirectoryAttributeEntity.getResponseType() )
                .setScope( String.join( ",", userDirectoryAttributeEntity.getScope() ) )
                .setState( stateJson )
                .setParameter( "prompt", "login" ) // Force login
                .setParameter( "max_age", "0" ) // Expire session immediately
                .setParameter( "auth_type", "reauthenticate" ) // Force re-authentication
                .buildQueryMessage();
        return authRequest;
    }

    private OAuthAccessTokenResponse prepareAndValidateAccessTokenResponse( String tokenEndpoint,
            UserDirectoryAttributeEntity userDirectoryAttributeEntity, String clientSecret, String redirectUri, String authorizationCode,
            OAuthStateDTO oauthStateDTO )
            throws OAuthSystemException, OAuthProblemException {
        // Initialize the OAuth client using Apache's URLConnectionClient
        OAuthClient oAuthClient = new OAuthClient( new URLConnectionClient() );
        // Build the token request using the authorization code
        OAuthClientRequest accessClientRequest = OAuthClientRequest
                .tokenLocation( tokenEndpoint )
                .setGrantType( GrantType.AUTHORIZATION_CODE )
                .setClientId( userDirectoryAttributeEntity.getClientId() )
                .setClientSecret( clientSecret )
                .setRedirectURI( redirectUri )
                .setCode( authorizationCode )
                .buildBodyMessage(); // Google requires post request which has a body

        // Request the access token from the OAuth provider
        OAuthAccessTokenResponse accessTokenResponse = oAuthClient.accessToken( accessClientRequest );
        // CHECK FOR FAILURE
        if ( accessTokenResponse.getParam( "error" ) != null ) {
            log.warn( MessageBundleFactory.getDefaultMessage( Messages.AUTHORIZATION_FAILURE.getKey() ) );
            throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.AUTHORIZATION_FAILURE.getKey() ) );
        }

        return accessTokenResponse;

    }

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService( AuthService authService ) {
        this.authService = authService;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public DirectoryDAO getDirectoryDAO() {
        return directoryDAO;
    }

    public void setDirectoryDAO( DirectoryDAO directoryDAO ) {
        this.directoryDAO = directoryDAO;
    }

}



























