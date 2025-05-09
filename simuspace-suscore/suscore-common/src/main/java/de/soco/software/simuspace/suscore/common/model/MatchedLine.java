package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class MatchedLine.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class MatchedLine {

    /**
     * The line.
     */
    private String line;

    /**
     * The line number.
     */
    private Integer lineNumber;

    /**
     * The lines matched.
     */
    private Integer linesMatched;

    /**
     * Gets the line.
     *
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * Sets the line.
     *
     * @param line
     *         the new line
     */
    public void setLine( String line ) {
        this.line = line;
    }

    /**
     * Gets the line number.
     *
     * @return the line number
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the line number.
     *
     * @param lineNumber
     *         the new line number
     */
    public void setLineNumber( Integer lineNumber ) {
        this.lineNumber = lineNumber;
    }

    /**
     * Gets the lines matched.
     *
     * @return the lines matched
     */
    public Integer getLinesMatched() {
        return linesMatched;
    }

    /**
     * Sets the lines matched.
     *
     * @param linesMatched
     *         the new lines matched
     */
    public void setLinesMatched( Integer linesMatched ) {
        this.linesMatched = linesMatched;
    }

}
