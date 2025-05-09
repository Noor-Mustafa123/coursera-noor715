package de.soco.software.simuspace.suscore.local.daemon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This Class is representation of server/deamon attribute to start. it will be consumed by Front-End.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DeamonResponse {

    /**
     * The Class DaemonPort.
     */
    class DaemonPort {

        /**
         * The port on which the deamon wil be started.
         */
        private int port;

        /**
         * Instantiates a new deamon properties.
         *
         * @param port
         *         the port
         */
        public DaemonPort( int port ) {
            super();
            this.setPort( port );
        }

        /**
         * Gets the port.
         *
         * @return the port
         */
        public int getPort() {
            return port;
        }

        /**
         * Sets the port.
         *
         * @param port
         *         the port to set
         */
        public void setPort( int port ) {
            this.port = port;
        }

    }

    /**
     * The server port.
     */
    private DaemonPort server;

    /**
     * Instantiates a new deamon response.
     *
     * @param daemonPort
     *         the daemon port
     */
    public DeamonResponse( int daemonPort ) {
        super();
        setServer( new DaemonPort( daemonPort ) );
    }

    /**
     * Gets the server.
     *
     * @return the server
     */
    public DaemonPort getServer() {
        return server;
    }

    /**
     * Sets the daemon port.
     *
     * @param daemonPort
     *         the new daemon port
     */
    public void setDaemonPort( int daemonPort ) {
        setServer( new DaemonPort( daemonPort ) );

    }

    /**
     * Sets the server.
     *
     * @param server
     *         the server to set
     */
    public void setServer( DaemonPort server ) {
        this.server = server;
    }

}
