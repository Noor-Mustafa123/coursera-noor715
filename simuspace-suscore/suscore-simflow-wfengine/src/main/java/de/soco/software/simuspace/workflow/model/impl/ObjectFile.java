package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class ObjectFile.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectFile {

    /**
     * The id.
     */
    private String id;

    /**
     * The version.
     */
    private ObjectVersion version;

    /**
     * The file name.
     */
    private String name;

    /**
     * The path of the document.
     */
    private String path;

    /**
     * The size.
     */
    private String size;

    /**
     * Instantiates a new object file.
     */
    public ObjectFile() {
        super();
    }

    /**
     * Instantiates a new object file.
     */
    public ObjectFile( String id, ObjectVersion version, String name, String path, String size ) {
        super();
        this.id = id;
        this.version = version;
        this.name = name;
        this.path = path;
        this.size = size;
    }

    /**
     * Instantiates a new object file.
     *
     * @param id
     *         the id
     * @param version
     *         the version
     * @param size
     *         the size
     */
    public ObjectFile( String id, ObjectVersion version, String size ) {
        super();
        this.id = id;
        this.version = version;
        this.size = size;
    }

    /**
     * Instantiates a new object file.
     *
     * @param id
     *         the id
     * @param version
     *         the version
     */
    public ObjectFile( String id, ObjectVersion version ) {
        super();
        this.id = id;
        this.version = version;
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
     * Gets the version.
     *
     * @return the version
     */
    public ObjectVersion getVersion() {
        return version;
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
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    public void setVersion( ObjectVersion version ) {
        this.version = version;
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
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

}
