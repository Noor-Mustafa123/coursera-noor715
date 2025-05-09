/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Headers;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.model.JobTokenDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;

/**
 * This Class is intercepting all api requests and validate user authentication tokens.
 *
 * @author Zeeshan jamal
 */
@Log4j2
public class SusAuthenticationInterceptor extends AbstractPhaseInterceptor< Message > {

    /**
     * The Constant API_SYSTEM_LOCATION_ADD.
     */
    private static final String API_SYSTEM_LOCATION_ADD = "api/system/location/add";

    /**
     * The Constant DOWNLOAD.
     */
    private static final String DOWNLOAD = "download";

    /**
     * The Constant REPORT_PDF_LINK.
     */
    private static final String REPORT_PDF_LINK = "/pdf/link";

    /**
     * The Constant REPORT_DOCX_LINK.
     */
    private static final String REPORT_DOCX_LINK = "/docx/link";

    /**
     * The Constant DOCUMENT_LINK.
     */
    private static final String DOCUMENT_LINK = "/document/.*/download";

    /**
     * The Constant SWAGGER_JSON.
     */
    private static final String SWAGGER_JSON = "swagger.json";

    /**
     * The Constant ACCESS_CONTROL_ALLOW_HEADERS is a Http header containing some extra tags.
     */
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /**
     * The Constant ACCESS_CONTROL_ALLOW_METHODS only allow desired request methods.
     */
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    /**
     * The Constant ACCESS_CONTROL_ALLOW_ORIGIN when it requests a resource from a different domain.
     */
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    /**
     * The Constant CONTENT_TYPE_ACCEPT, The MIME type of the body of the request (used with POST and PUT requests).
     */
    private static final String CONTENT_TYPE_ACCEPT = "Content-Type, Accept";

    /**
     * The Constant DELETE.
     */
    private static final String DELETE = "DELETE";

    /**
     * The Constant DELETE_VIEW.
     */
    private static final String DELETE_VIEW = "DELETEVIEW";

    /**
     * The Constant EXPIRED.
     */
    private static final String EXPIRED = "expired";

    /**
     * The Constant GET.
     */
    private static final String GET = "GET";

    /**
     * The Constant GET_VIEW.
     */
    private static final String GET_VIEW = "GETVIEW";

    /**
     * The Constant HTTP_METHOD_OPTIONS.
     */
    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

    /**
     * The Constant INVALID_TOKEN.
     */
    private static final String INVALID_TOKEN = "Invalid/expired token, login again";

    /**
     * The Constant INVALID_JOB_TOKEN.
     */
    private static final String INVALID_JOB_TOKEN = "Invalid/expired Job token";

    /**
     * The Constant MESSAGE.
     */
    private static final String MESSAGE = "message";

    /**
     * The Constant ASTERIK.
     */
    private static final String OBJECT_TO_CONVERT_IN_LIST = "*";

    /**
     * The Constant OPTIONS.
     */
    private static final String OPTIONS = HTTP_METHOD_OPTIONS;

    /**
     * The Constant ORIGIN request header indicates where a fetch originates from. It doesn't include any path information, but only the
     * server name.
     */
    private static final String ORIGIN = "Origin";

    /**
     * The Constant POST.
     */
    private static final String POST = "POST";

    /**
     * The Constant PUT.
     */
    private static final String PUT = "PUT";

    /**
     * The Constant SET_CURRENT_VIEW.
     */
    private static final String SET_CURRENT_VIEW = "SETCURRENTVIEW";

    /**
     * The Constant SET_VIEW.
     */
    private static final String SET_VIEW = "SETVIEW";

    /**
     * The Constant SUCCESS.
     */
    private static final String SUCCESS = "success";

    /**
     * The Constant NO_TOKEN_PROVIDED.
     */
    private static final String NO_TOKEN_PROVIDED = "No Token provided";

    /**
     * The Constant USER_ID.
     */
    private static final String USER_ID = "userId";

    /**
     * The Constant URL_KEY.
     */
    private static final String URL_KEY = "org.apache.cxf.request.url";

    /**
     * The Constant USER_NAME.
     */
    private static final String USER_NAME = "userName";

    /**
     * The Constant WORKFLOW_TOKEN_KEY.
     */
    private static final String WORKFLOW_TOKEN_KEY = "wfKey";

    /**
     * The Constant X_AUTH_TOKEN.
     */
    private static final String X_AUTH_TOKEN = "X-Auth-Token";

    /**
     * The Constant X_JOB_TOKEN.
     */
    private static final String X_JOB_TOKEN = "X-Job-Token";

    /**
     * The Constant X_REQUESTED_WITH.
     */
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    /**
     * The user token manager.
     */
    private UserTokenManager userTokenManager;

    /**
     * Instantiates a new authentication interceptor.
     */
    public SusAuthenticationInterceptor() {
        super( Phase.PRE_PROTOCOL );
    }

    /**
     * Handle message.
     *
     * @param message
     *         the message
     */
    /*
     * (non-Javadoc)
     *
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.
     * message.Message)
     */
    @Override
    public void handleMessage( Message message ) {
        if ( userTokenManager == null ) {
            userTokenManager = getTokenManagerService();
        }
        EntityManager entityManager = userTokenManager.getEntityManagerFactory().createEntityManager();
        try {
            String token = null;
            final String url = ( String ) message.get( URL_KEY );
            final String method = ( String ) message.get( Message.HTTP_REQUEST_METHOD );
            final Map< String, List< String > > headers = getSetProtocolHeaders( message );
            setProtocolHeaders( message );

            if ( url.toLowerCase().contains( SWAGGER_JSON ) || url.toLowerCase().contains( DOWNLOAD ) || url.toLowerCase()
                    .contains( REPORT_PDF_LINK ) || url.toLowerCase().contains( REPORT_DOCX_LINK ) || url.toLowerCase()
                    .contains( API_SYSTEM_LOCATION_ADD ) || url.toLowerCase().matches( DOCUMENT_LINK ) ) {
                entityManager.close();
                return;
            }
            if ( StringUtils.isNotBlank( url ) && method.equals( OPTIONS ) ) {
                log.debug( "################### Pass Authentication Interceptor: Login Request : " + url + " ####################" );

            } else if ( headers.containsKey( X_JOB_TOKEN ) && CollectionUtils.isNotEmpty( headers.get( X_JOB_TOKEN ) ) ) {
                final List< String > tokenlist = headers.get( X_JOB_TOKEN );
                if ( CollectionUtils.isNotEmpty( tokenlist ) ) {
                    token = tokenlist.get( ConstantsInteger.INTEGER_VALUE_ZERO );
                }
                validateJobHttpRequest( entityManager, message, token );
            } else if ( headers.containsKey( X_AUTH_TOKEN ) ) {
                final List< String > tokenlist = headers.get( X_AUTH_TOKEN );
                if ( CollectionUtils.isNotEmpty( tokenlist ) ) {
                    token = tokenlist.get( ConstantsInteger.INTEGER_VALUE_ZERO );
                }
                validateHttpRequest( entityManager, message, token );
            } else {
                entityManager.close();
                stopResourceAccessOnInvalidRequest( message, NO_TOKEN_PROVIDED );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * A method that validates any request to the server.
     *
     * @param message
     *         the message
     * @param token
     *         the token
     */
    private void validateHttpRequest( EntityManager entityManager, Message message, String token ) {
        String ipAddress = userTokenManager.getIpAddress( message );
        final UserTokenDTO userTokenBO = userTokenManager.checkSusHttpRequest( entityManager, token, ipAddress, WORKFLOW_TOKEN_KEY );
        if ( null != userTokenBO ) {
            message.put( USER_ID, userTokenBO.getUserId() );
            message.put( USER_NAME, userTokenBO.getUserName() );
            message.put( "token", userTokenBO.getToken() );
            entityManager.close();
        } else {
            entityManager.close();
            stopResourceAccessOnInvalidRequest( message, INVALID_TOKEN );
        }
    }

    /**
     * Validate job http request.
     *
     * @param message
     *         the message
     * @param token
     *         the token
     */
    private void validateJobHttpRequest( EntityManager entityManager, Message message, String token ) {
        String ipAddress = userTokenManager.getIpAddress( message );
        final JobTokenDTO jobTokenDTO = userTokenManager.checkSusJobHttpRequest( entityManager, token, ipAddress, WORKFLOW_TOKEN_KEY );
        if ( null != jobTokenDTO ) {
            message.put( USER_ID, jobTokenDTO.getUserId() );
            message.put( USER_NAME, jobTokenDTO.getUserName() );
            message.put( "token", jobTokenDTO.getToken() );
            // update auth token last request time
            userTokenManager.updateLastRequestInShiro( entityManager, jobTokenDTO.getAuthToken() );
            entityManager.close();
        } else {
            entityManager.close();
            stopResourceAccessOnInvalidRequest( message, INVALID_JOB_TOKEN );
        }
    }

    /**
     * A method that is used to send restricted resource access message to user on invalid request.
     *
     * @param message
     *         the message
     * @param errorMessage
     *         the error message
     */
    private void stopResourceAccessOnInvalidRequest( Message message, String errorMessage ) {
        final Map< String, Object > hm = new HashMap<>();
        hm.put( SUCCESS, Boolean.FALSE );
        hm.put( EXPIRED, Boolean.TRUE );
        hm.put( MESSAGE, errorMessage );
        final ObjectMapper mapper = new ObjectMapper();
        Response response = null;
        try {
            response = Response.status( Response.Status.OK ).type( MediaType.APPLICATION_JSON ).entity( mapper.writeValueAsString( hm ) )
                    .build();
            message.getExchange().put( Response.class, response );
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
        message.getExchange().put( Response.class, response );
        sendErrorResponseToUser( errorMessage, message );
    }

    /**
     * A method that sends error message to user in response.
     *
     * @param errorMessage
     *         the error message
     * @param message
     *         the message
     */
    private void sendErrorResponseToUser( String errorMessage, Message message ) {
        final Exception exception = new Exception( errorMessage );
        final Fault fault = new Fault( exception );
        fault.setMessage( errorMessage );
        fault.setStatusCode( Response.Status.UNAUTHORIZED.getStatusCode() );
        message.getInterceptorChain().abort();
        throw fault;
    }

    /**
     * Sets the protocol headers.
     *
     * @param message
     *         the message
     *
     * @return the message
     */
    private Message setProtocolHeaders( Message message ) {
        Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );
        List< String > originHeaders = headers.get( "Origin" );
        if ( originHeaders != null && !originHeaders.isEmpty() ) {
            String origin = originHeaders.get( 0 );
            // Check if the Origin is allowed
            if ( PropertiesManager.getAllowedOrigins().contains( origin ) ) {
                // Set the Access-Control-Allow-Origin header
                headers.put( "Access-Control-Allow-Origin", List.of( origin ) );
            }
        }
        if ( !headers.containsKey( "Access-Control-Allow-Origin" ) ) {
            headers.put( "Access-Control-Allow-Origin", List.of( PropertiesManager.getAllowedOrigins().get( 0 ) ) );
        }
        headers.put( ACCESS_CONTROL_ALLOW_METHODS,
                Arrays.asList( POST, GET, PUT, DELETE, GET_VIEW, SET_VIEW, SET_CURRENT_VIEW, DELETE_VIEW ) );
        headers.put( ACCESS_CONTROL_ALLOW_HEADERS,
                Arrays.asList( ORIGIN, X_REQUESTED_WITH, X_AUTH_TOKEN, X_JOB_TOKEN, CONTENT_TYPE_ACCEPT ) );
        final Map< String, List< String > > protocolHeader = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );
        protocolHeader.putAll( headers );
        headers = protocolHeader;
        message.put( Message.PROTOCOL_HEADERS, headers );
        return message;
    }

    /**
     * Gets the sets the protocol headers.
     *
     * @param message
     *         the message
     *
     * @return the sets the protocol headers
     */
    private Map< String, List< String > > getSetProtocolHeaders( Message message ) {
        return Headers.getSetProtocolHeaders( message );
    }

    /**
     * if any package is restarted or re deployed then services will be refreshed. So there is a chance that it will return null as in case
     * service will not be found.
     *
     * @return the service
     */
    private UserTokenManager getTokenManagerService() {
        final BundleContext bundleContext = FrameworkUtil.getBundle( SusAuthenticationInterceptor.class ).getBundleContext();
        final ServiceReference< ? > serviceReference = bundleContext.getServiceReference( UserTokenManager.class.getName() );
        return ( UserTokenManager ) bundleContext.getService( serviceReference );
    }

    /**
     * Gets the payload from message.
     *
     * @param message
     *         the message
     *
     * @return the payload from message
     */
    private String getPayloadFromMessage( Message message ) {
        String soapMessage = null;
        message.put( Message.ENCODING, "UTF-8" );
        InputStream is = message.getContent( InputStream.class );
        if ( is != null ) {
            CachedOutputStream bos = new CachedOutputStream();
            try {
                IOUtils.copy( is, bos );
                soapMessage = new String( bos.getBytes() );
                bos.flush();
                message.setContent( InputStream.class, is );

                is.close();
                ByteArrayInputStream inputStream = new ByteArrayInputStream( soapMessage.getBytes() );
                message.setContent( InputStream.class, inputStream );
                bos.close();
            } catch ( IOException ioe ) {
                log.error( ioe.getMessage(), ioe );
            }
        }
        return soapMessage;
    }

    /**
     * Sets the user token manager.
     *
     * @param userTokenManager
     *         the new user token manager
     */
    public void setUserTokenManager( UserTokenManager userTokenManager ) {
        this.userTokenManager = userTokenManager;
    }

}