package de.soco.software.suscore.jsonschema.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FfmpegExtension.
 *
 * @author Noman Arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FfmpegExtension {

    /**
     * The input.
     */
    private String input;

    /**
     * The output.
     */
    private Map< String, String > output;

    /**
     * Instantiates a new file extension.
     */
    public FfmpegExtension() {
    }

    /**
     * Instantiates a new ffmpeg extension.
     *
     * @param output
     *         the output
     */
    public FfmpegExtension( Map< String, String > output ) {
        super();
        this.output = output;
    }

    /**
     * Gets the input.
     *
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the input.
     *
     * @param input
     *         the new input
     */
    public void setInput( String input ) {
        this.input = input;
    }

    /**
     * Gets the output.
     *
     * @return the output
     */
    public Map< String, String > getOutput() {
        return output;
    }

    /**
     * Sets the output.
     *
     * @param output
     *         the output
     */
    public void setOutput( Map< String, String > output ) {
        this.output = output;
    }

}
