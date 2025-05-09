package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Shell.
 *
 * @author noman
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Shell {

    /**
     * The folders.
     */
    private Map< String, String > shell;

    /**
     * Instantiates a new shell.
     */
    public Shell() {
    }

    /**
     * Gets the shell.
     *
     * @return the shell
     */
    public Map< String, String > getShell() {
        return shell;
    }

    /**
     * Sets the shell.
     *
     * @param shell
     *         the shell
     */
    public void setShell( Map< String, String > shell ) {
        this.shell = shell;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Shell [shell=" + shell + "]";
    }

}
