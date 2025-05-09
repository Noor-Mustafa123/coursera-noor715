package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ScanResponseDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ScanResponseDTO {

    /**
     * The start.
     */
    private String start;

    /**
     * The end.
     */
    private String end;

    /**
     * The line number.
     */
    private String lineNumber;

    private String lineNumberIndexed;

    /**
     * The match.
     */
    private String match;

    public ScanResponseDTO() {
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start
     *         the new start
     */
    public void setStart( String start ) {
        this.start = start;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sets the end.
     *
     * @param end
     *         the new end
     */
    public void setEnd( String end ) {
        this.end = end;
    }

    /**
     * Gets the line number.
     *
     * @return the line number
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the line number.
     *
     * @param lineNumber
     *         the new line number
     */
    public void setLineNumber( String lineNumber ) {
        this.lineNumber = lineNumber;
    }

    /**
     * Gets the match.
     *
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * Sets the match.
     *
     * @param match
     *         the new match
     */
    public void setMatch( String match ) {
        this.match = match;
    }

    public String getLineNumberIndexed() {
        return lineNumberIndexed;
    }

    public void setLineNumberIndexed( String lineNumberIndexed ) {
        this.lineNumberIndexed = lineNumberIndexed;
    }

}
