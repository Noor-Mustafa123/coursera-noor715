package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AlgoSchemeDTO implements Serializable {

    private String type;

    private List< Object > options;

    private String generateInput;

    private String filterOutput;

    private AlgoSchemeCommand command;

    private String pythonFilePath;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *         the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * @return the options
     */
    public List< Object > getOptions() {
        return options;
    }

    /**
     * @param options
     *         the options to set
     */
    public void setOptions( List< Object > options ) {
        this.options = options;
    }

    /**
     * @return the generateInput
     */
    public String getGenerateInput() {
        return generateInput;
    }

    /**
     * @param generateInput
     *         the generateInput to set
     */
    public void setGenerateInput( String generateInput ) {
        this.generateInput = generateInput;
    }

    /**
     * @return the filterOutput
     */
    public String getFilterOutput() {
        return filterOutput;
    }

    /**
     * @param filterOutput
     *         the filterOutput to set
     */
    public void setFilterOutput( String filterOutput ) {
        this.filterOutput = filterOutput;
    }

    /**
     * @return the command
     */
    public AlgoSchemeCommand getCommand() {
        return command;
    }

    /**
     * @param command
     *         the command to set
     */
    public void setCommand( AlgoSchemeCommand command ) {
        this.command = command;
    }

    public String getPythonFilePath() {
        return pythonFilePath;
    }

    public void setPythonFilePath( String pythonFilePath ) {
        this.pythonFilePath = pythonFilePath;
    }

}
