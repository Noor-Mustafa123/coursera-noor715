package de.soco.software.simuspace.suscore.notification.service.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.io.EofException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.notification.service.rest.WSService;

/**
 * The Class WSServiceImpl.
 */
@Log4j2
@Path( "/" )
public class WSServiceImpl implements WSService {

    /**
     * The Constant MAX_ERROR_COUNT.
     */
    protected static final int MAX_ERROR_COUNT = 2;

    /**
     * The executor.
     */
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * The monitors.
     */
    private final Map< String, WriterHolder< OutputStream > > monitors = new ConcurrentHashMap<>();

    /**
     * Monitor customers.
     *
     * @param key
     *         the key
     *
     * @return the streaming output
     */
    @GET
    @Path( "/monitor/{key}" )
    @Produces( "text/*" )
    public StreamingOutput monitorCustomers( @PathParam( "key" ) String key ) {
        return out -> {
            monitors.put( key, new WriterHolder<>( out, MAX_ERROR_COUNT ) );
            String response = JsonUtils
                    .objectToJson( ResponseUtils.successResponse( "Subscribed at " + new java.util.Date() + " key: " + key, null ) );
            out.write( response.getBytes() );
        };
    }

    /**
     * Unmonitor customers.
     *
     * @param key
     *         the key
     *
     * @return the string
     */
    @GET
    @Path( "/unmonitor/{key}" )
    @Produces( "text/*" )
    public String unmonitorCustomers( @PathParam( "key" ) String key ) {
        return ( monitors.remove( key ) != null ? "Removed: " : "Already removed: " ) + key;
    }

    /**
     * Send customer event.
     *
     * @param msg
     *         the msg
     */
    private void sendCustomerEvent( final Object msg ) {
        executor.execute( () -> {
            try {
                log.debug( "WEB-SOCKETS Initialization -----" );
                synchronized ( monitors ) {
                    for ( Iterator< WriterHolder< OutputStream > > it = monitors.values().iterator(); it.hasNext(); ) {
                        WriterHolder< OutputStream > wh = it.next();
                        try {
                            OutputStream outputStream = wh.getValue();
                            if ( outputStream != null ) {
                                outputStream.write( msg.toString().getBytes() );
                                outputStream.flush();
                                wh.reset();
                            } else {
                                log.warn( "Output stream is null, skipping." );
                            }
                        } catch ( EofException e ) {
                            log.error( "Client connection closed: " + e.getMessage(), e );
                            it.remove();
                        } catch ( IOException e ) {
                            log.error( "WEB-SOCKETS 1:" + e.getMessage(), e );
                            if ( wh.increment() ) {
                                // the max error count reached; purging the output resource
                                try {
                                    wh.getValue().close();
                                } catch ( IOException e2 ) {
                                    // ignore
                                    if ( log.isDebugEnabled() ) {
                                        log.warn( "WEB-SOCKETS 2:" + e2.getMessage(), e );
                                    } else {
                                        log.warn( "WEB-SOCKETS 2:" + e2.getMessage() );
                                    }
                                }
                                it.remove();
                            }
                        }
                    }
                }
            } catch ( Exception e ) {
                log.error( "WEB-SOCKETS 3:" + e.getMessage(), e );
            }
            log.debug( "WEB-SOCKETS exit -----" );
        } );
    }

    /**
     * The Class WriterHolder.
     *
     * @param <T>
     *         the generic type
     */
    private static class WriterHolder< T > {

        /**
         * The value.
         */
        private final T value;

        /**
         * The max.
         */
        private final int max;

        /**
         * The error count.
         */
        private final AtomicInteger errorCount;

        /**
         * Instantiates a new writer holder.
         *
         * @param object
         *         the object
         * @param max
         *         the max
         */
        WriterHolder( T object, int max ) {
            this.value = object;
            this.max = max;
            this.errorCount = new AtomicInteger();
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Gets the.
         *
         * @return the int
         */
        public int get() {
            return errorCount.get();
        }

        /**
         * Increment.
         *
         * @return true, if successful
         */
        public boolean increment() {
            return max < errorCount.getAndIncrement();
        }

        /**
         * Reset.
         */
        public void reset() {
            errorCount.getAndSet( 0 );
        }

    }

    /**
     * Post update.
     *
     * @param json
     *         the json
     *
     * @return the string
     */
    @Override
    public String postUpdate( String json ) {
        this.sendCustomerEvent( json );
        return "";
    }

    /**
     * Unmonitor client.
     *
     * @param keyToken
     *         the key token
     *
     * @return true, if successful
     */
    @Override
    public boolean unmonitorClient( String keyToken ) {
        try {
            monitors.remove( keyToken );
            return true;
        } catch ( Exception e ) {
            log.error( "WEB-SOCKETS 4 unmonitor:" + e.getMessage(), e );
            return false;
        }
    }

    /**
     * Active SSE.
     *
     * @return the int
     */
    @Override
    public int activeSSE() {
        return monitors.size();
    }

}
