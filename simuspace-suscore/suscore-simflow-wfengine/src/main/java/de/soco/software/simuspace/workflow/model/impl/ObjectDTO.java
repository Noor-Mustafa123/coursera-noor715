package de.soco.software.simuspace.workflow.model.impl;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ObjectDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectDTO {

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The file.
     */
    private ObjectFile file;

    /**
     * The type id.
     */
    private String typeId;

    /**
     * The type.
     */
    private String type;

    /**
     * The parent id.
     */
    private String parentId;

    /**
     * The job id.
     */
    private String jobId;

    /**
     * The config.
     */
    private String config;

    /**
     * The size.
     */
    private String size;

    /**
     * The custom attributes.
     */
    private Map< String, Object > customAttributes;

    /**
     * Instantiates a new object DTO.
     */
    public ObjectDTO() {
        super();
    }

    /**
     * Instantiates a new object DTO.
     *
     * @param name
     *         the name
     * @param file
     *         the file
     * @param typeId
     *         the type id
     */
    public ObjectDTO( String name, ObjectFile file, String typeId ) {
        super();
        this.name = name;
        this.file = file;
        this.typeId = typeId;
    }

    /**
     * Instantiates a new object DTO.
     *
     * @param name
     *         the name
     * @param file
     *         the file
     * @param typeId
     *         the type id
     * @param size
     *         the size
     */
    public ObjectDTO( String name, ObjectFile file, String typeId, String size ) {
        super();
        this.name = name;
        this.file = file;
        this.typeId = typeId;
        this.size = size;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public ObjectFile getFile() {
        return file;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the job id.
     *
     * @return the job id
     */
    public String getJobId() {
        return jobId;
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
     * Gets the parent id.
     *
     * @return the parent id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the type id.
     *
     * @return the type id
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @param config
     *         the config to set
     */
    public void setConfig( String config ) {
        this.config = config;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( ObjectFile file ) {
        this.file = file;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Sets the job id.
     *
     * @param jobId
     *         the new job id
     */
    public void setJobId( String jobId ) {
        this.jobId = jobId;
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
     * Sets the parent id.
     *
     * @param parentId
     *         the new parent id
     */
    public void setParentId( String parentId ) {
        this.parentId = parentId;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
    }

    /**
     * @param type
     *         the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( String typeId ) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "ObjectDTO [id=" + id + ", name=" + name + ", file=" + file + ", typeId=" + typeId + ", type=" + type + ", parentId="
                + parentId + ", jobId=" + jobId + ", config=" + config + ", size=" + size + "]";
    }

    /**
     * Gets the custom attributes.
     *
     * @return the custom attributes
     */
    public Map< String, Object > getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the custom attributes.
     *
     * @param customAttributes
     *         the custom attributes
     */
    public void setCustomAttributes( Map< String, Object > customAttributes ) {
        this.customAttributes = customAttributes;
    }

}
