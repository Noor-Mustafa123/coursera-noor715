package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class LocalPython.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LocalPython {

    /**
     * The pythonpath.
     */
    public String pythonpath;

    /**
     * Gets the pythonpath.
     *
     * @return the pythonpath
     */
    public String getPythonpath() {
        return pythonpath;
    }

    /**
     * Sets the pythonpath.
     *
     * @param pythonpath
     *         the new pythonpath
     */
    public void setPythonpath( String pythonpath ) {
        this.pythonpath = pythonpath;
    }

}
