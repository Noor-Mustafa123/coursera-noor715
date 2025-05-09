/*******************************************************************************
 * Copyright (C) 2013 - now() SOCO engineers GmbH All rights reserved.
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;

/**
 * A simple logging handler which outputs the bytes of the message to the Logger.
 *
 * @author Ali Haider
 */

public class SusLoggingOutInterceptor extends LoggingOutInterceptor {

    /**
     * The Constant HIDE_PASSWD_PATTERN for hiding password in log file.
     */
    public static final String HIDE_PASS_PATTERN = "$1:\"***\"";

    /**
     * The Constant LOG_SETUP file.
     */
    private static final String LOG_SETUP = SusLoggingOutInterceptor.class.getName() + ".log-setup";

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LogUtils.getLogger( SusLoggingOutInterceptor.class );

    /**
     * The Constant PASSWORD.
     */
    public static final String PASS = "password";

    /**
     * The Constant CONTACTS.
     */
    public static final String CONTACTS = "contacts";

    /**
     * The Constant EMAIL.
     */
    public static final String EMAIL = "email";

    /**
     * The Constant LABEL.
     */
    public static final String LABEL = "label";

    /**
     * The Constant MESSAGE_TRUNCATED_TO.
     */
    public static final String MESSAGE_TRUNCATED_TO = "(message truncated to ";

    /**
     * The Constant BYTES.
     */
    public static final String BYTES = " bytes)\n";

    /**
     * The Constant pattern for password.
     */
    private static final Pattern pattern = Pattern.compile( "(.+?password.{0,}?):\"(.+?)\"", Pattern.CASE_INSENSITIVE );

    /**
     * The Constant REPLACEMENT.
     */
    public static final String REPLACEMENT = "****\"";

    /**
     * The Constant SUS_BINARY.
     */
    public static final String SUS_BINARY = "sus_binary";

    /**
     * The zero_threshold.
     */
    private static final int ZERO_THRESHOLD = 0;

    /**
     * The Constant USERINFO_REGEX.
     */
    public static final String USERINFO_REGEX = "(?<=\"(contacts|email)\":\")[^\\n\"]*(\"?\\n?)";

    /**
     * The Class LoggingCallback redirect to the api after authentication.
     */
    class LoggingCallback implements CachedOutputStreamCallback {

        /**
         * The logger.
         */
        private final Logger logger = LogUtils.getLogger( LoggingCallback.class );

        /**
         * The message.
         */
        private final Message message;

        /**
         * The origin stream for message content.
         */
        private final OutputStream origStream;

        /**
         * Instantiates a new logging callback.
         *
         * @param logger
         *         the logger
         * @param msg
         *         the msg
         * @param os
         *         the os
         */
        public LoggingCallback( final Message msg, final OutputStream os ) {
            message = msg;
            origStream = os;
        }

        /*
         * (non-Javadoc)
         * @see org.apache.cxf.io.CachedOutputStreamCallback#onClose(org.apache.cxf. io.CachedOutputStream)
         */
        @Override
        public void onClose( CachedOutputStream cos ) {
            final Integer responseCode = ( Integer ) message.get( Message.RESPONSE_CODE );
            final LoggingMessage buffer = setUpBuffer( message );
            final String ct = ( String ) message.get( Message.CONTENT_TYPE );
            if ( !isShowBinaryContent() && isBinaryContent( ct ) ) {
                buffer.getMessage().append( BINARY_CONTENT_MESSAGE ).append( '\n' );
                log( logger, formatLoggingMessage( buffer ) );
                return;
            }

            if ( cos.getTempFile() != null ) {
                buffer.getMessage().append( "Outbound Message (saved to tmp file):\n" );
                buffer.getMessage().append( "Filename: " + cos.getTempFile().getAbsolutePath() + "\n" );
            }
            if ( cos.size() > limit ) {
                buffer.getMessage().append( MESSAGE_TRUNCATED_TO + limit + BYTES );
            }
            try {
                final String encoding = ( String ) message.get( Message.ENCODING );
                buffer.getPayload();
                if ( responseCode != 200 ) {
                    writePayload( buffer.getPayload(), cos, encoding, ct, true );
                }

            } catch ( final Exception ex ) {
                log( logger, ex.toString() );
            }
            log( logger, formatLoggingMessage( buffer ) );
            try {
                cos.lockOutputStream();
                cos.resetOut( null, false );
            } catch ( final Exception ex ) {
                log( logger, ex.toString() );
            }
            message.setContent( OutputStream.class, origStream );
        }

        /*
         * (non-Javadoc)
         * @see org.apache.cxf.io.CachedOutputStreamCallback#onFlush(org.apache.cxf. io.CachedOutputStream)
         */
        @Override
        public void onFlush( CachedOutputStream cos ) {

        }

    }

    /**
     * The Class LogWriter.
     */
    private class LogWriter extends FilterWriter {

        /**
         * The count.
         */
        int count;

        /**
         * The logger.
         */
        Logger logger;

        /**
         * The message.
         */
        Message message;

        /**
         * The out2.
         */
        StringWriter out2;

        /**
         * Instantiates a new log writer.
         *
         * @param logger
         *         the logger
         * @param message
         *         the message
         * @param writer
         *         the writer
         */
        public LogWriter( Logger logger, Message message, Writer writer ) {
            super( writer );
            this.logger = logger;
            this.message = message;
            if ( !( writer instanceof StringWriter ) ) {
                out2 = new StringWriter();
            }
        }

        /*
         * (non-Javadoc)
         * @see java.io.FilterWriter#close()
         */
        @Override
        public void close() throws IOException {
            final LoggingMessage buffer = setUpBuffer( message );
            StringWriter writePayload = out2;
            if ( count >= limit ) {
                buffer.getMessage().append( MESSAGE_TRUNCATED_TO + limit + BYTES );
            }
            if ( writePayload == null ) {
                writePayload = ( StringWriter ) out;
            }
            final String ct = ( String ) message.get( Message.CONTENT_TYPE );
            try {
                writePayload( buffer.getPayload(), writePayload, ct );
            } catch ( final Exception ex ) {
                log( logger, ex.toString() );
            }
            log( logger, buffer.toString() );
            message.setContent( Writer.class, out );
            super.close();
        }

        /*
         * (non-Javadoc)
         * @see java.io.FilterWriter#write(char[], int, int)
         */
        @Override
        public void write( char[] cbuf, int off, int len ) throws IOException {
            super.write( cbuf, off, len );
            if ( ( out2 != null ) && ( count < limit ) ) {
                out2.write( cbuf, off, len );
            }
            count += len;
        }

        /*
         * (non-Javadoc)
         * @see java.io.FilterWriter#write(int)
         */
        @Override
        public void write( int c ) throws IOException {
            super.write( c );
            if ( ( out2 != null ) && ( count < limit ) ) {
                out2.write( c );
            }
            count++;
        }

        /*
         * (non-Javadoc)
         * @see java.io.FilterWriter#write(java.lang.String, int, int)
         */
        @Override
        public void write( String str, int off, int len ) throws IOException {
            super.write( str, off, len );
            if ( ( out2 != null ) && ( count < limit ) ) {
                out2.write( str, off, len );
            }
            count += len;
        }

    }

    /**
     * Instantiates a new log o interceptor.
     */
    public SusLoggingOutInterceptor() {
        this( Phase.PRE_STREAM );
    }

    /**
     * Instantiates a new log outputs by interceptor.
     *
     * @param lim
     *         the lim
     */
    public SusLoggingOutInterceptor( int lim ) {
        this();
        limit = lim;
    }

    /**
     * Instantiates a new log outputs by interceptor.
     *
     * @param w
     *         the w
     */
    public SusLoggingOutInterceptor( PrintWriter w ) {
        this();
        writer = w;
    }

    /**
     * Instantiates a new log outputs by interceptor.
     *
     * @param phase
     *         the phase
     */
    public SusLoggingOutInterceptor( String phase ) {
        super( phase );
        addBefore( StaxOutInterceptor.class.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String formatLoggingMessage( LoggingMessage buffer ) {
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;

    }

    /**
     * Handle binary.
     *
     * @param logger
     *         the logger
     * @param message
     *         the message
     *
     * @return true, if successful
     */
    private boolean handleBinary( Logger logger, Message message ) {
        final LoggingMessage buffer = setUpBuffer( message );
        if ( message.getExchange().containsKey( SUS_BINARY ) && ( Boolean ) message.getExchange().get( SUS_BINARY ) ) {
            buffer.getMessage().append( BINARY_CONTENT_MESSAGE ).append( ConstantsString.NEW_LINE );
            log( logger, formatLoggingMessage( buffer ) );
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf. message.Message)
     */
    @Override
    public void handleMessage( Message message ) throws Fault {

        // if any logging IN/OUT interceptor is called for an API, CXF
        // always caches body on filesystem if > 64KB
        // It is always recommended to bypass caching for large contents to
        // avoid filesystem choking
        // you can set sus_binary in exchange to true to bypass such caching

        final Logger logger = getLogger();

        final OutputStream os = message.getContent( OutputStream.class );
        final Writer iowriter = message.getContent( Writer.class );
        if ( ( os == null ) && ( iowriter == null ) ) {
            return;
        }
        if ( logger.isLoggable( Level.INFO ) || ( writer != null ) ) {
            final boolean hasLogged = message.containsKey( LOG_SETUP );
            if ( !hasLogged ) {
                message.put( LOG_SETUP, Boolean.TRUE );
                if ( os != null ) {
                    if ( !handleBinary( logger, message ) ) {
                        final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream( os );
                        if ( threshold > ZERO_THRESHOLD ) {
                            newOut.setThreshold( threshold );
                        }
                        message.setContent( OutputStream.class, newOut );
                        newOut.registerCallback( new LoggingCallback( message, os ) );
                    }
                } else {
                    message.setContent( Writer.class, new LogWriter( logger, message, iowriter ) );
                }
            }
        }
    }

    /**
     * Hide password.
     *
     * @param currentEnvelope
     *         the current envelope
     */
    private void hidePassword( StringBuilder currentEnvelope ) {
        if ( currentEnvelope.toString().toLowerCase().contains( PASS ) ) {

            final Matcher matcherstd = pattern.matcher( currentEnvelope );

            final StringBuffer sb = new StringBuffer();
            while ( matcherstd.find() ) {
                matcherstd.appendReplacement( sb, HIDE_PASS_PATTERN );
            }
            matcherstd.appendTail( sb );

            currentEnvelope.replace( ZERO_THRESHOLD, currentEnvelope.length(), sb.toString() );
        }
    }

    /**
     * Hide UserInfo.
     *
     * @param currentEnvelope
     *         the current envelope
     */
    private void hideUserInfo( StringBuilder currentEnvelope ) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = ( JSONObject ) parser.parse( currentEnvelope.toString() );

            if ( json.containsKey( "data" ) ) {
                Object data = json.get( "data" );

                if ( data instanceof JSONObject ) {
                    if ( currentEnvelope.toString().toLowerCase().contains( CONTACTS )
                            || currentEnvelope.toString().toLowerCase().contains( EMAIL ) ) {
                        StringBuilder sb = new StringBuilder();
                        sb.append( currentEnvelope );
                        Pattern p = Pattern.compile( USERINFO_REGEX );
                        Matcher m = p.matcher( sb );
                        currentEnvelope.replace( ZERO_THRESHOLD, currentEnvelope.length(), m.replaceAll( REPLACEMENT ) );
                    }
                } else if ( data instanceof JSONArray jsonArray ) {

                    for ( JSONObject jsonObj : ( Iterable< JSONObject > ) jsonArray ) {
                        if ( jsonObj.get( LABEL ) != null && ( jsonObj.get( LABEL ).toString().equalsIgnoreCase( CONTACTS )
                                || jsonObj.get( LABEL ).toString().equalsIgnoreCase( EMAIL ) ) ) {
                            jsonObj.replace( "value", REPLACEMENT );
                        }
                    }

                    json.put( "data", jsonArray );
                    currentEnvelope.replace( ZERO_THRESHOLD, currentEnvelope.length(), json.toString() );
                }
            }
        } catch ( Exception e ) {
            LOGGER.info( e.getMessage() );
        }
    }

    /**
     * Hide workflows.
     *
     * @param currentEnvelope
     *         the current envelope
     */
    private void hideWorkflows( StringBuilder currentEnvelope ) {
        if ( currentEnvelope.toString().toLowerCase().contains( "workflowtype" )
                && currentEnvelope.toString().toLowerCase().contains( "elements" ) ) {
            currentEnvelope.replace( ConstantsInteger.INTEGER_VALUE_ZERO, currentEnvelope.length(), REPLACEMENT );
        }
    }

    /**
     * Setup buffer.
     *
     * @param message
     *         the message
     *
     * @return the logging message
     */
    private LoggingMessage setUpBuffer( Message message ) {
        String id = ( String ) message.getExchange().get( LoggingMessage.ID_KEY );
        if ( id == null ) {
            id = LoggingMessage.nextId();
            message.getExchange().put( LoggingMessage.ID_KEY, id );
        }
        final LoggingMessage buffer = new LoggingMessage( "Outbound Message LogOInterceptor\n---------------------------", id );

        final Integer responseCode = ( Integer ) message.get( Message.RESPONSE_CODE );
        if ( responseCode != null ) {
            buffer.getResponseCode().append( responseCode );
        }

        final String encoding = ( String ) message.get( Message.ENCODING );
        if ( encoding != null ) {
            buffer.getEncoding().append( encoding );
        }
        final String httpMethod = ( String ) message.get( Message.HTTP_REQUEST_METHOD );
        if ( httpMethod != null ) {
            buffer.getHttpMethod().append( httpMethod );
        }
        final String address = ( String ) message.get( Message.ENDPOINT_ADDRESS );
        if ( address != null ) {
            buffer.getAddress().append( address );
        }
        final String ct = ( String ) message.get( Message.CONTENT_TYPE );
        if ( ct != null ) {
            buffer.getContentType().append( ct );
        }
        final Object headers = message.get( Message.PROTOCOL_HEADERS );
        if ( headers != null ) {
            buffer.getHeader().append( headers );
        }
        return buffer;
    }

}
