package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TemplateFileDTO {

    /**
     * The file path.
     */
    private String file;

    /**
     * The variables.
     */
    private List< TemplateVariableDTO > variables;

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
    public List< TemplateVariableDTO > getVariables() {
        return variables;
    }

    /**
     * Sets the variables.
     *
     * @param variables
     *         the new variables
     */
    public void setVariables( List< TemplateVariableDTO > variables ) {
        this.variables = variables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TemplateFileDTO [file=" + file + ", variables=" + variables + "]";
    }

}
