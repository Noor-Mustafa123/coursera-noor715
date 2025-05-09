package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;

/**
 * This class contains information about Workflow server.
 *
 * @author Aroosa.Bukhari
 * @since 1.0
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class RestAPI {

    /**
     * The timeout to check server connection.
     */
    static final int TIME_OUT = 10;

    /**
     * The hostname.
     */
    private String hostname;

    /**
     * The port.
     */
    private String port;

    /**
     * The protocol.
     */
    private String protocol;

    /**
     * The request headers.
     */
    private RequestHeaders requestHeaders;

    /**
     * Instantiates a new rest api.
     */
    public RestAPI() {
        super();
    }

    /**
     * Instantiates a new rest api.
     *
     * @param protocol
     *         the protocol
     * @param hostname
     *         the hostname
     * @param port
     *         the port
     */
    public RestAPI( String protocol, String hostname, String port ) {
        this();
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Gets the hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Gets the protocol.
     *
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the request headers.
     *
     * @return the request headers
     */
    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * Sets the hostname.
     *
     * @param hostname
     *         the new hostname
     */
    public void setHostname( String hostname ) {
        this.hostname = hostname;
    }

    /**
     * Sets the port.
     *
     * @param port
     *         the new port
     */
    public void setPort( String port ) {
        this.port = port;
    }

    /**
     * Sets the protocol.
     *
     * @param protocol
     *         the new protocol
     */
    public void setProtocol( String protocol ) {
        this.protocol = protocol;
    }

    /**
     * Sets the request headers.
     *
     * @param requestHeaders
     *         the new request headers
     */
    public void setRequestHeaders( RequestHeaders requestHeaders ) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RestAPI [hostname='" + hostname + "', port='" + port + "', protocol='" + protocol + "', requestHeaders='" + requestHeaders
                + "']";
    }

    /**
     * Validates the fields of an object
     *
     * @return Notification
     */
    public Notification validate() {
        final Notification notification = new Notification();

        if ( StringUtils.isNullOrEmpty( getHostname() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.SERVER_HOSTNAME_NOT_PROVIDED ) ) );
        }
        if ( StringUtils.isNullOrEmpty( getPort() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.SERVER_PORT_NOT_PROVIDED ) ) );
        }
        if ( StringUtils.isNullOrEmpty( getProtocol() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.SERVER_PROTOCOL_NOT_PROVIDED ) ) );
        }
        return notification;
    }

}
