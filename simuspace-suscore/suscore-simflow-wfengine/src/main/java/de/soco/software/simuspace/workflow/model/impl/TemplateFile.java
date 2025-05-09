package de.soco.software.simuspace.workflow.model.impl;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;

/**
 * The Class TemplateFile.
 *
 * @author Ali Haider
 */
public class TemplateFile {

    /**
     * The file.
     */
    private String file;

    /**
     * The variables.
     */
    private List< ObjectVariableDTO > variables;

    /**
     * Instantiates a new template file.
     */
    public TemplateFile() {
        super();
    }

    /**
     * Instantiates a new template file.
     *
     * @param file
     *         the file
     * @param variables
     *         the variables
     */
    public TemplateFile( String file, List< ObjectVariableDTO > variables ) {
        super();
        this.file = file;
        this.variables = variables;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( String file ) {
        this.file = file;
    }

    /**
     * Gets the variables.
     *
     * @return the variables
     */
    public List< ObjectVariableDTO > getVariables() {
        return variables;
    }

    /**
     * Sets the variables.
     *
     * @param variables
     *         the new variables
     */
    public void setVariables( List< ObjectVariableDTO > variables ) {
        this.variables = variables;
    }

}
