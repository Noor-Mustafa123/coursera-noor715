package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ServerConfig.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ServerConfig {

    /**
     * The hostname.
     */
    String hostname;

    /**
     * The protocol.
     */
    String protocol;

    /**
     * The port.
     */
    String port;

    /**
     * Gets the hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
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
     * Gets the protocol.
     *
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
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
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
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
     * Instantiates a new server config.
     */
    public ServerConfig() {
        super();
    }

}
