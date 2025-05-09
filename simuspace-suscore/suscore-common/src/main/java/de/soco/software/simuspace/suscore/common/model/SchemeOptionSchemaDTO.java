package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class SchemeOptionSchemaDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SchemeOptionSchemaDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2531946539500408804L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The content.
     */
    private String content;

    /**
     * The workflow id.
     */
    private UUID workflowId;

    /**
     * Instantiates a new scheme option schema DTO.
     */
    public SchemeOptionSchemaDTO() {
    }

    /**
     * Instantiates a new scheme option schema DTO.
     *
     * @param id
     *         the id
     * @param content
     *         the content
     * @param workflowId
     *         the workflow id
     */
    public SchemeOptionSchemaDTO( UUID id, String content, UUID workflowId ) {
        super();
        this.id = id;
        this.content = content;
        this.workflowId = workflowId;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content
     *         the new content
     */
    public void setContent( String content ) {
        this.content = content;
    }

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
     * Gets the workflow id.
     *
     * @return the workflow id
     */
    public UUID getWorkflowId() {
        return workflowId;
    }

    /**
     * Sets the workflow id.
     *
     * @param workflowId
     *         the new workflow id
     */
    public void setWorkflowId( UUID workflowId ) {
        this.workflowId = workflowId;
    }

}
