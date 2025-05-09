package de.soco.software.simuspace.suscore.authentication.service.rest.impl;

import javax.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.authentication.activator.Activator;
import de.soco.software.simuspace.suscore.authentication.service.rest.AuthService;
import de.soco.software.simuspace.suscore.authentication.service.rest.BMWOAuth2Service;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;

@Log4j2
public class BMWOAuth2ServiceImpl extends AuthMutualServiceImpl implements BMWOAuth2Service {

    private AuthService userAuthServiceBean;

    @Override
    public Response oauth2Authorization( String state ) {
        Response redirectToOAUTHServer = null;
        try {
            if ( StringUtils.isNullOrEmpty( state ) ) {
                state = "#";
            } else {
                state = "#" + state;
            }
            OAuthClientRequest accessClientRequest = OAuthClientRequest
                    .authorizationLocation( getOAuth( Activator.WELL_KNOWNS_URL ).get( "authorization_endpoint" ).asText() )
                    .setResponseType( "code" ).setClientId( PropertiesManager.webEamClientId() )
                    .setRedirectURI( PropertiesManager.webEamRedirectUri() ).setScope( "openid profile email" )
                    .setState( new String( Base64.getEncoder().encode( state.getBytes() ) ) ).buildQueryMessage();
            redirectToOAUTHServer = Response.status( 302 ).location( new URI( accessClientRequest.getLocationUri() ) ).build();
        } catch ( OAuthSystemException | URISyntaxException e ) {
            return handleException( e );
        }
        return redirectToOAUTHServer;
    }

    @Override
    public Response oauth2FetchToken( String code, String state ) {
        try {
            OAuthClient oAuthClient = new OAuthClient( new URLConnectionClient() );
            OAuthClientRequest accessClientRequest = OAuthClientRequest
                    .tokenLocation( getOAuth( Activator.WELL_KNOWNS_URL ).get( "token_endpoint" ).asText() )
                    .setGrantType( GrantType.AUTHORIZATION_CODE ).setClientId( PropertiesManager.webEamClientId() )
                    .setClientSecret( PropertiesManager.webEamClientSecrete() ).setRedirectURI( PropertiesManager.webEamRedirectUri() )
                    .setCode( code ).buildQueryMessage();
            OAuthAccessTokenResponse accessTokenResponse = oAuthClient.accessToken( accessClientRequest );
            String idToken = accessTokenResponse.getParam( "id_token" );
            JsonNode profileNode = getProfile( accessTokenResponse.getAccessToken() );
            String url = PropertiesManager.getWebBaseURL() + "/loginoauth2/" + profileNode.get( "sub" ).asText() + "/token/"
                    + accessTokenResponse.getAccessToken() + "/refreshToken/" + accessTokenResponse.getRefreshToken() + "/token_id/"
                    + idToken + "/internal-state/" + state;
            log.info( "*****************************************fetchedUrl******************************************" );
            log.info( url );
            log.info( "*********************************************************************************************" );
            return Response.status( 302 ).location( new URI( url ) ).build();
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    private JsonNode getProfile( String xAuthToken ) {
        JsonNode jsonNode = null;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + xAuthToken );
            CloseableHttpResponse closeableHttpResponse = SuSClient
                    .getExternalRequest( getOAuth( Activator.WELL_KNOWNS_URL ).get( "userinfo_endpoint" ).asText(), requestHeaders );
            HttpEntity entity = closeableHttpResponse.getEntity();
            String responseString = EntityUtils.toString( entity, "UTF-8" );
            jsonNode = JsonUtils.toJsonNode( responseString );
        } catch ( final Exception e ) {
            throw new SusException( e );
        }
        return jsonNode;
    }

    private Boolean getInfo( String xAuthToken ) {
        Boolean isAcknowledge = Boolean.FALSE;
        try {
            final Map< String, String > requestHeaders = new HashMap<>();
            requestHeaders.put( "authorization", "Bearer " + xAuthToken );
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

    @Override
    public Response oauth2IntenalLogin( String qNumber, String x_oauth_token, String refreshToken, String oauth_token_id, String state ) {
        try {
            Boolean info = getInfo( x_oauth_token );
            if ( Boolean.TRUE.equals( info ) ) {
                return userAuthServiceBean.authenticationWithOauth( oauth_token_id, x_oauth_token, refreshToken,
                        new String( Base64.getDecoder().decode( state ) ),
                        "{\"uid\":\"" + qNumber + "\",\"password\":\"" + x_oauth_token + "\"}" );
            } else {
                return Response.status( 302 ).location( new URI( PropertiesManager.getWebBaseURL() ) ).build();
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    public AuthService getUserAuthServiceBean() {
        return userAuthServiceBean;
    }

    public void setUserAuthServiceBean( AuthService userAuthServiceBean ) {
        this.userAuthServiceBean = userAuthServiceBean;
    }

}