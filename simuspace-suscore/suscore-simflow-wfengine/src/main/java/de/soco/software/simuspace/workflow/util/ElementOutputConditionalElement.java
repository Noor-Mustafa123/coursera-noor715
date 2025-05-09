package de.soco.software.simuspace.workflow.util;

import java.io.Serializable;

/**
 * The Class ElementOutputImport.
 *
 * @author Fahad
 */
public class ElementOutputConditionalElement implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The exit code.
     */
    private String exitCode;

    /**
     * Gets the exit code.
     *
     * @return the exit code
     */
    public String getExitCode() {
        return exitCode;
    }

    /**
     * Sets the exit code.
     *
     * @param exitCode
     *         the new exit code
     */
    public void setExitCode( String exitCode ) {
        this.exitCode = exitCode;
    }

}
