package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UserConfigFile.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserConfigFile {

    /**
     * The server.
     */
    ServerConfig server;

    /**
     * The python.
     */
    LocalPython python;

    /**
     * The daemon log.
     */
    String daemonLog;

    /**
     * Gets the server.
     *
     * @return the server
     */
    public ServerConfig getServer() {
        return server;
    }

    /**
     * Sets the server.
     *
     * @param server
     *         the new server
     */
    public void setServer( ServerConfig server ) {
        this.server = server;
    }

    /**
     * Instantiates a new user config file.
     */
    public UserConfigFile() {
        super();
    }

    /**
     * Gets the daemon log.
     *
     * @return the daemon log
     */
    public String getDaemonLog() {
        return daemonLog;
    }

    /**
     * Sets the daemon log.
     *
     * @param daemonLog
     *         the new daemon log
     */
    public void setDaemonLog( String daemonLog ) {
        this.daemonLog = daemonLog;
    }

    /**
     * Gets the python.
     *
     * @return the python
     */
    public LocalPython getPython() {
        return python;
    }

    /**
     * Sets the python.
     *
     * @param python
     *         the new python
     */
    public void setPython( LocalPython python ) {
        this.python = python;
    }

}
