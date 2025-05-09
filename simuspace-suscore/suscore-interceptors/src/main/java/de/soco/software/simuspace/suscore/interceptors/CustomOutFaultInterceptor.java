/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import javax.ws.rs.core.MediaType;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Headers;

import lombok.extern.log4j.Log4j2;

/**
 * REST fault out interceptor that can send exceptions raised by the interceptor, if any, according to the Accept header set by the
 * client(s)
 *
 * @author vinay
 */
@Log4j2
public class CustomOutFaultInterceptor extends AbstractOutDatabindingInterceptor {

    /**
     * Instantiates a new custom out fault interceptor.
     */
    public CustomOutFaultInterceptor() {
        super( Phase.MARSHAL );

    }

    /**
     * Instantiates a new custom out fault interceptor.
     *
     * @param phase
     *         the phase
     */
    public CustomOutFaultInterceptor( final String phase ) {
        super( phase );
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage( final Message message ) throws Fault {
        try {
            final java.io.OutputStream outOriginal = message.getContent( java.io.OutputStream.class );
            outOriginal.write( buildResponse( message ).getBytes() );
            outOriginal.flush();
            outOriginal.close();
            message.getInterceptorChain().abort();
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Builds the response.
     *
     * @param message
     *         the message
     *
     * @return the string
     */
    public String buildResponse( final Message message ) {
        final StringBuffer sb = new StringBuffer();
        final Fault fault = ( Fault ) message.getContent( Exception.class );

        int responseCode;

        message.put( Message.CONTENT_TYPE, MediaType.APPLICATION_JSON );

        sb.append( createError( fault.getCause(), fault.getCause().getMessage() ) );

        responseCode = HttpURLConnection.HTTP_FORBIDDEN;

        message.put( Message.RESPONSE_CODE, responseCode );

        // If license error
        if ( fault.getStatusCode() == 200 ) {
            message.put( Message.RESPONSE_CODE, fault.getStatusCode() );
        }

        // putting CORS strings for session expiry
        Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );
        headers.put( "Access-Control-Allow-Origin", List.of( "*" ) );
        headers.put( "Access-Control-Allow-Methods", Arrays.asList( "POST", "GET", "PUT", "DELETE" ) );
        headers.put( "Access-Control-Allow-Headers",
                Arrays.asList( "Origin", "X-Requested-With", "X-Auth-Token", "Content-Type, Accept" ) );
        headers.put( "Cache-Control", List.of( "no-cache, must-revalidate" ) );
        headers.put( "Pragma", List.of( "no-cache" ) );
        headers.put( "Server", List.of( "SimuSpace" ) );
        Map< String, List< String > > headers2 = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );
        headers2.putAll( headers );
        headers = headers2;
        message.put( Message.PROTOCOL_HEADERS, headers );

        return sb.toString();
    }

    /**
     * Creates the error.
     *
     * @param error
     *         the error
     * @param msg
     *         the msg
     *
     * @return the string
     */
    private String createError( Throwable error, String msg ) {
        if ( msg == null ) {
            return "";
        }
        if ( msg.equals( "Invalid or expired token. Login again" ) || msg.equals( "No Token sent" ) ) {
            return "{\"message\":{\"type\":\"ERROR\",\"content\":\"" + msg + "\"},\"expired\":true" + ",\"success\":false}";
        }

        return "{\"message\":{\"type\":\"ERROR\",\"content\":\"" + msg + "\"},\"success\":false}";

    }

}
