package de.soco.software.simuspace.suscore.authentication.service.rest.impl;

import javax.ws.rs.core.Response;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.authentication.activator.Activator;
import de.soco.software.simuspace.suscore.authentication.service.rest.AuthService;
import de.soco.software.simuspace.suscore.authentication.service.rest.CB2AuthService;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

@Log4j2
public class CB2AuthServiceImpl extends AuthMutualServiceImpl implements CB2AuthService {

    AuthService authService;

    /**
     * CONSTANTS
     */
    public static String AUTHORIZATION_ENDPOINT = "authorization_endpoint";

    public static String TOKEN_ENDPOINT = "token_endpoint";

    public static String SCOPE = "openid profile email";

    public static String CB2_STATE = "cb2_state";

    public static String SUB = "sub";

    @Override
    public Response authorize() {
        try {
            // create auth request
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest.authorizationLocation(
                            getOAuth( Activator.WELL_KNOWNS_URL ).path( AUTHORIZATION_ENDPOINT ).asText() )
                    .setClientId( PropertiesManager.webEamClientId() ).setScope( SCOPE ).setResponseType( "code" )
                    .setRedirectURI( PropertiesManager.webEamRedirectUri() ).setState( EncryptAndDecryptUtils.encryptString( CB2_STATE ) )
                    .buildQueryMessage();
            // redirect user to the response uri
            log.debug( "RELOCATION_URI: " + oAuthClientRequest.getLocationUri() );
            return Response.status( 302 ).location( new URI( oAuthClientRequest.getLocationUri() ) ).build();
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response fetchAccessToken( String authCode, String state ) {
        try {
            if ( !checkIfStateParameterMatches( URLDecoder.decode( state, StandardCharsets.UTF_8 ) ) ) {
                log.warn( MessageBundleFactory.getDefaultMessage( Messages.STATE_MISMATCH.getKey() ) );
                throw new SusException( MessageBundleFactory.getDefaultMessage( Messages.STATE_MISMATCH.getKey() ) );
            }
            // prepare request for accessToken
            OAuthClient oAuthClient = new OAuthClient( new URLConnectionClient() );
            OAuthClientRequest accessClientRequest = OAuthClientRequest.tokenLocation(
                            getOAuth( Activator.WELL_KNOWNS_URL ).path( TOKEN_ENDPOINT ).asText() ).setGrantType( GrantType.AUTHORIZATION_CODE )
                    .setClientId( PropertiesManager.webEamClientId() ).setClientSecret( PropertiesManager.webEamClientSecrete() )
                    .setRedirectURI( PropertiesManager.webEamRedirectUri() ).setCode( authCode ).buildQueryMessage();
            // send request
            OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken( accessClientRequest );
            String encodedAccessToken = URLEncoder.encode( oauthResponse.getAccessToken(), StandardCharsets.UTF_8 );
            String encodedRefreshToken = URLEncoder.encode( oauthResponse.getRefreshToken(), StandardCharsets.UTF_8 );
            String encodedIdToken = URLEncoder.encode( oauthResponse.getRefreshToken(), StandardCharsets.UTF_8 );
            String encodedState = URLEncoder.encode( state, StandardCharsets.UTF_8 );
            log.debug( "accessToken: " + oauthResponse.getAccessToken() );
            log.debug( "refreshToken: " + oauthResponse.getRefreshToken() );
            // making url using path params
            String url = PropertiesManager.getLocationURL() + "/api/cb2login" + "/accessToken/" + encodedAccessToken + "/refreshToken/"
                    + encodedRefreshToken + "/token_id/" + encodedIdToken + "/state/" + encodedState;
            log.debug( "url: " + url );
            return Response.status( 304 ).location( new URI( url ) ).build();
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response logUserIntoCB2( String accessToken, String refreshToken, String tokenId, String state ) {
        try {
            Boolean userStatus = checkUserStatus( accessToken );
            if ( Boolean.TRUE.equals( userStatus ) ) {
                // decode the tokens
                String decodedAccessToken = URLDecoder.decode( accessToken, StandardCharsets.UTF_8 );
                String decodedRefreshToken = URLDecoder.decode( refreshToken, StandardCharsets.UTF_8 );
                String decodedIDToken = URLDecoder.decode( tokenId, StandardCharsets.UTF_8 );
                String decodedState = URLDecoder.decode( state, StandardCharsets.UTF_8 );
                JsonNode jsonNode = getUserDetails( accessToken );
                String uid = jsonNode.path( SUB ).asText();
                return authService.cb2LoginViaRest( decodedIDToken, decodedRefreshToken, uid );
            } else {
                return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
            }
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    private Boolean checkUserStatus( String accessToken ) {
        Boolean isAcknowledge = Boolean.FALSE;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + accessToken );
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest( PropertiesManager.webEamTokenInfo(),
                    requestHeaders );
            if ( 200 == closeableHttpResponse.getStatusLine().getStatusCode() ) {
                isAcknowledge = Boolean.TRUE;
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return isAcknowledge;
    }

    private JsonNode getUserDetails( String xAuthToken ) {
        JsonNode jsonNode = null;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + xAuthToken );
            CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest(
                    getOAuth( Activator.WELL_KNOWNS_URL ).get( "userinfo_endpoint" ).asText(), requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    private boolean checkIfStateParameterMatches( String decryptedState ) {
        //  checking state for cb2
        if ( StringUtils.isNotBlank( decryptedState ) ) {
            String decryptedString = EncryptAndDecryptUtils.decryptString( decryptedState );
            if ( StringUtils.equals( decryptedString, CB2_STATE ) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService( AuthService authService ) {
        this.authService = authService;
    }

}
