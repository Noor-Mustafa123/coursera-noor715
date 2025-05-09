/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.interceptors;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * A simple logging handler which outputs the bytes of the message to the Logger.
 *
 * @author Ali Haider
 */

public class SusLoggingInInterceptor extends LoggingInInterceptor {

    /**
     * The Constant HIDE_PASSWD_PATTERN.
     */
    public static final String HIDE_PASS_PATTERN = "$1:\"***\"";

    /**
     * The Constant LOG.
     */
    private static final Logger LOGGER = LogUtils.getLogger( SusLoggingInInterceptor.class );

    /**
     * The Constant MULTIPART_FORM_DATA.
     */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

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
     * The Constant pattern.
     */
    private static final Pattern pattern = Pattern.compile( "(.+?password.{0,}?):\"(.+?)\"", Pattern.CASE_INSENSITIVE );

    /**
     * The Constant REPLACEMENT.
     */
    public static final String REPLACEMENT = "****\"";

    /**
     * The Constant SEPARATOR_HTTP_ADDRESS_FROM_PARAMETERS.
     */
    public static final String SEPARATOR_HTTP_ADDRESS_FROM_PARAMETERS = "?";

    /**
     * The Constant SKIPPING_LOG_MESSAGE_FOR_MULTIPART.
     */
    private static final String SKIPPING_LOG_MESSAGE_FOR_MULTIPART = "Skipping log message for multipart/form-data.";

    /**
     * The Constant INBOUND_MESSAGE.
     */
    private static final String INBOUND_MESSAGE = "Inbound Message\n----------------------------";

    /**
     * Log data in log file.
     */
    private boolean logBytes = false;

    /**
     * The Constant USERINFO_REGEX.
     */
    public static final String USERINFO_REGEX = "(?<=\"(contacts|email)\":\")[^\\n\"]*(\"?\\n?)";

    /**
     * Instantiates a new log interceptor.
     */
    public SusLoggingInInterceptor() {
        super();
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.interceptor.LoggingInInterceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage( Message message ) throws Fault {

        if ( ( writer != null ) || getLogger().isLoggable( Level.INFO ) ) {
            logging( message );
        }
    }

    /**
     * Logging.
     *
     * @param message
     *         the message
     *
     * @throws Fault
     *         the fault
     */
    protected void logging( Message message ) throws Fault {

        if ( message.containsKey( LoggingMessage.ID_KEY ) ) {
            return;
        }
        String id = ( String ) message.getExchange().get( LoggingMessage.ID_KEY );
        if ( id == null ) {
            id = LoggingMessage.nextId();
            message.getExchange().put( LoggingMessage.ID_KEY, id );
        }
        message.put( LoggingMessage.ID_KEY, id );
        final LoggingMessage buffer = new LoggingMessage( INBOUND_MESSAGE, id );

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
        final String ct = ( String ) message.get( Message.CONTENT_TYPE );
        if ( ct != null ) {
            buffer.getContentType().append( ct );
        }
        final Object headers = message.get( Message.PROTOCOL_HEADERS );

        if ( headers != null ) {
            buffer.getHeader().append( headers );
        }
        final String uri = ( String ) message.get( Message.REQUEST_URL );
        if ( uri != null ) {
            buffer.getAddress().append( uri );
            final String query = ( String ) message.get( Message.QUERY_STRING );
            if ( query != null ) {
                buffer.getAddress().append( SEPARATOR_HTTP_ADDRESS_FROM_PARAMETERS ).append( query );
            }
        }

        final InputStream is = message.getContent( InputStream.class );
        if ( is != null ) {
            try ( CachedOutputStream bos = new CachedOutputStream() ) {
                IOUtils.copy( is, bos );

                bos.flush();
                is.close();
                message.setContent( InputStream.class, bos.getInputStream() );
                if ( !isLogBytes() ) {
                    if ( ( ct != null ) && !ct.contains( MULTIPART_FORM_DATA ) ) {
                        writePayload( buffer.getPayload(), bos, encoding, ct, true );
                    } else {
                        getLogger().info( SKIPPING_LOG_MESSAGE_FOR_MULTIPART + bos.size() );
                    }
                }
            } catch ( final Exception e ) {
                throw new Fault( e );
            }
        }
        hidePassword( buffer.getPayload() );
        hideUserInfo( buffer.getPayload() );
        hideWorkflows( buffer.getPayload() );

        try {
            log( getLogger(), formatLoggingMessage( buffer ) );
        } catch ( final Exception e ) {
            LOGGER.info( e.getMessage() );
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

            currentEnvelope.replace( ConstantsInteger.INTEGER_VALUE_ZERO, currentEnvelope.length(), sb.toString() );
        }

    }

    /**
     * Hide UserInfo.
     *
     * @param currentEnvelope
     *         the current envelope
     */
    private void hideUserInfo( StringBuilder currentEnvelope ) {
        if ( currentEnvelope.toString().toLowerCase().contains( CONTACTS ) || currentEnvelope.toString().toLowerCase().contains( EMAIL ) ) {
            StringBuilder sb = new StringBuilder();
            sb.append( currentEnvelope );
            Pattern p = Pattern.compile( USERINFO_REGEX );
            Matcher m = p.matcher( sb );
            currentEnvelope.replace( ConstantsInteger.INTEGER_VALUE_ZERO, currentEnvelope.length(), m.replaceAll( REPLACEMENT ) );
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
     * Sets the log bytes.
     *
     * @param logBytes
     *         the new log bytes
     */
    public void setLogBytes( boolean logBytes ) {
        this.logBytes = logBytes;
    }

    /**
     * Checks if is log bytes.
     *
     * @return true, if is log bytes
     */
    public boolean isLogBytes() {
        return logBytes;
    }

    /* (non-Javadoc)
     * @see org.apache.cxf.interceptor.LoggingInInterceptor#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

}
