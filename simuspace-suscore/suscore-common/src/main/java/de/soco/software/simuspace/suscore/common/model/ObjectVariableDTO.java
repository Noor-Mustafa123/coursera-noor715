/*
 *
 */

package de.soco.software.simuspace.suscore.common.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ObjectVariableDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectVariableDTO {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The variable name.
     */
    private String variableName;

    /**
     * The line regex.
     */
    private String lineRegex;

    /**
     * The line match.
     */
    private String lineMatch;

    /**
     * The line offset.
     */
    private String lineOffset;

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
     * The highlight.
     */
    private ScanResponseDTO highlight;

    private Date createdOn;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the variable name.
     *
     * @return the variable name
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Sets the variable name.
     *
     * @param variableName
     *         the new variable name
     */
    public void setVariableName( String variableName ) {
        this.variableName = variableName;
    }

    /**
     * Gets the line regex.
     *
     * @return the line regex
     */
    public String getLineRegex() {
        return lineRegex;
    }

    /**
     * Sets the line regex.
     *
     * @param lineRegex
     *         the new line regex
     */
    public void setLineRegex( String lineRegex ) {
        this.lineRegex = lineRegex;
    }

    /**
     * Gets the line match.
     *
     * @return the line match
     */
    public String getLineMatch() {
        return lineMatch;
    }

    /**
     * Sets the line match.
     *
     * @param lineMatch
     *         the new line match
     */
    public void setLineMatch( String lineMatch ) {
        this.lineMatch = lineMatch;
    }

    /**
     * Gets the line offset.
     *
     * @return the line offset
     */
    public String getLineOffset() {
        return lineOffset;
    }

    /**
     * Sets the line offset.
     *
     * @param lineOffset
     *         the new line offset
     */
    public void setLineOffset( String lineOffset ) {
        this.lineOffset = lineOffset;
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

    /**
     * Gets the highlight.
     *
     * @return the highlight
     */
    public ScanResponseDTO getHighlight() {
        return highlight;
    }

    /**
     * Sets the highlight.
     *
     * @param highlight
     *         the new highlight
     */
    public void setHighlight( ScanResponseDTO highlight ) {
        this.highlight = highlight;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ObjectVariableDTO [variableName=" + variableName + ", lineRegex=" + lineRegex + ", lineMatch=" + lineMatch + ", lineOffset="
                + lineOffset + ", variableRegex=" + variableRegex + ", variableMatch=" + variableMatch + ", variableGroup=" + variableGroup
                + "]";
    }

}
