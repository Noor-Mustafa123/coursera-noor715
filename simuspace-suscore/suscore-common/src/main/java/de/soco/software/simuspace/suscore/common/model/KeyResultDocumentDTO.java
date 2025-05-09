package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class KeyResultDocumentDTO.
 *
 * @author Noamn Arshad
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class KeyResultDocumentDTO {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String name;

    /**
     * The type.
     */
    private String type;

    /**
     * The lable.
     */
    private String lable;

    /**
     * The content.
     */
    private String content;

    /**
     * The group.
     */
    private String group;

    /**
     * Instantiates a new key result document DTO.
     */
    public KeyResultDocumentDTO() {
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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the lable.
     *
     * @return the lable
     */
    public String getLable() {
        return lable;
    }

    /**
     * Sets the lable.
     *
     * @param lable
     *         the new lable
     */
    public void setLable( String lable ) {
        this.lable = lable;
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
     * Gets the group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group.
     *
     * @param group
     *         the new group
     */
    public void setGroup( String group ) {
        this.group = group;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "KeyResultDocumentDTO [id=" + id + ", name=" + name + ", type=" + type + ", lable=" + lable + ", content=" + content
                + ", group=" + group + "]";
    }

}
