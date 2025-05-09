package de.soco.software.simuspace.suscore.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ScanFileDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ScanFileDTO {

    /**
     * The file path.
     */
    private String file;

    /**
     * The variables.
     */
    private List< ObjectVariableDTO > variables;

    /**
     * The variable type.
     */
    private String variableType;

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

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ScanFileDTO [file=" + file + ", variables=" + variables + "]";
    }

    /**
     * Gets the variable type.
     *
     * @return the variable type
     */
    public String getVariableType() {
        return variableType;
    }

    /**
     * Sets the variable type.
     *
     * @param variableType
     *         the new variable type
     */
    public void setVariableType( String variableType ) {
        this.variableType = variableType;
    }

}
