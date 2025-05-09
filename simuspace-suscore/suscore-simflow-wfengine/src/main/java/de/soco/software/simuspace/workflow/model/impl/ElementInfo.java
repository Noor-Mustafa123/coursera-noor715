package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is responsible of comments, description and version of a work flow.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ElementInfo implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2909218203922319504L;

    /**
     * The comments of a work flow element.
     */
    private String comments;

    /**
     * The description of a work flow element.
     */
    private String description;

    /**
     * The version of a work flow element.
     */
    private String version;

    /**
     * Instantiates a new element info.
     */
    public ElementInfo() {
        super();
    }

    /**
     * Instantiates a new element info.
     *
     * @param version
     *         the version
     */
    public ElementInfo( String version ) {
        this();
        this.version = version;
    }

    /**
     * Instantiates a new element info.
     *
     * @param version
     *         the version
     * @param description
     *         the description
     * @param comments
     *         the comments
     */
    public ElementInfo( String version, String description, String comments ) {
        this( version );
        this.description = description;
        this.comments = comments;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the comments.
     *
     * @param comments
     *         the new comments
     */
    public void setComments( String comments ) {
        this.comments = comments;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ElementInfo [comments=" + comments + ", description=" + description + ", version=" + version + "]";
    }

}
