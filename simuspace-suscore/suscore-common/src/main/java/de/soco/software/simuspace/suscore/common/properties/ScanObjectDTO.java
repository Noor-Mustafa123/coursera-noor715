package de.soco.software.simuspace.suscore.common.properties;

/**
 * The Class ObjectScanDTO.
 */
public class ScanObjectDTO {

    /**
     * The line.
     */
    private String line;

    /**
     * The line number.
     */
    private String lineNumber;

    /**
     * The variable regex.
     */
    private String variableRegex;

    /**
     * The variable match.
     */
    private String variableMatch;

    /**
     * The variable group.
     */
    private String variableGroup;

    /**
     * Instantiates a new Scan Object.
     */
    public ScanObjectDTO() {

    }

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
     * Gets the variable regex.
     *
     * @return the variable regex
     */
    public String getVariableRegex() {
        return variableRegex;
    }

    /**
     * Sets the variable regex.
     *
     * @param variableRegex
     *         the new variable regex
     */
    public void setVariableRegex( String variableRegex ) {
        this.variableRegex = variableRegex;
    }

    /**
     * Gets the variable match.
     *
     * @return the variable match
     */
    public String getVariableMatch() {
        return variableMatch;
    }

    /**
     * Sets the variable match.
     *
     * @param variableMatch
     *         the new variable match
     */
    public void setVariableMatch( String variableMatch ) {
        this.variableMatch = variableMatch;
    }

    /**
     * Gets the variable group.
     *
     * @return the variable group
     */
    public String getVariableGroup() {
        return variableGroup;
    }

    /**
     * Sets the variable group.
     *
     * @param variableGroup
     *         the new variable group
     */
    public void setVariableGroup( String variableGroup ) {
        this.variableGroup = variableGroup;
    }

}
