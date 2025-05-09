/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Headers;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class EnableCORSInterceptor.
 *
 * @author Shan Arshad
 */
@Log4j2
public class EnableCORSInterceptor extends AbstractPhaseInterceptor< Message > {

    /**
     * Instantiates a new enable cors interceptor.
     */
    public EnableCORSInterceptor() {
        super( Phase.PRE_LOGICAL );
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage( Message message ) {

        try {
            Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );

            // skip cxf client requests from simcore to simstream
            if ( headers.containsKey( "internal" ) ) {
                return;
            }
            Exchange e = message.getExchange();
            Message in = e.getInMessage();
            Map< String, List< String > > inHeaders = Headers.getSetProtocolHeaders( in );
            List< String > originHeaders = inHeaders.get( "Origin" );
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
            headers.put( "Access-Control-Allow-Methods", List.of( "POST", "GET", "PUT", "DELETE" ) );
            headers.put( "Access-Control-Allow-Headers",
                    Arrays.asList( "Origin", "X-Requested-With", "X-Auth-Token", "Content-Type, Accept" ) );
            headers.put( "Pragma", List.of( "no-cache" ) );
            headers.put("Access-Control-Expose-Headers", List.of("Content-Disposition","File-Size"));
            headers.put( "Accept-Encoding", List.of( "gzip" ) );

            headers.put( "Connection", List.of( "keep-alive" ) );

            headers.put( "Cache-Control", List.of( "no-cache" ) );
            headers.put( "X-Accel-Buffering", List.of( "no" ) );

            Map< String, List< String > > headers2 = new TreeMap<>( String.CASE_INSENSITIVE_ORDER );
            headers2.putAll( headers );
            headers = headers2;
            message.put( Message.PROTOCOL_HEADERS, headers );
            log.debug( "################### Changed Headers CORS Interceptor: {} ####################",
                    message.get( Message.PROTOCOL_HEADERS ) );
        } catch ( Exception ce ) {
            log.error( ce.getMessage(), ce );
        }
    }

}
