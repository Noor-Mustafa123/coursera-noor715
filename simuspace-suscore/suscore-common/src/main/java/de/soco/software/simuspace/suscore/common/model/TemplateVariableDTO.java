package de.soco.software.simuspace.suscore.common.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class TemplateVariableDTO.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class TemplateVariableDTO extends ScanResponseDTO {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The variable name.
     */
    private String variableName;

    /**
     * The created on.
     */
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
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "TemplateVariableDTO [id=" + id + ", variableName=" + variableName + ", createdOn=" + createdOn + ", getStart()="
                + getStart() + ", getEnd()=" + getEnd() + ", getLineNumber()=" + getLineNumber() + ", getMatch()=" + getMatch()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }

}
