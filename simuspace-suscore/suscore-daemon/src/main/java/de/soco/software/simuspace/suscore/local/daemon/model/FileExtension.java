package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FileExtension.
 *
 * @author noman
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FileExtension {

    /**
     * The shell.
     */
    private Map< String, String > shell;

    /**
     * Instantiates a new file extension.
     *
     * @param shell
     *         the shell
     */
    public FileExtension( Map< String, String > shell ) {
        super();
        this.shell = shell;
    }

    /**
     * Instantiates a new file extension.
     */
    public FileExtension() {
    }

    @Override
    public String toString() {
        return "FileExtension [shell=" + shell + "]";
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

}
