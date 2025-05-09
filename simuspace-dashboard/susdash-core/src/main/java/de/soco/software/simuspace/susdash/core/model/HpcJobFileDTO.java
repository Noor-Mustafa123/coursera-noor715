package de.soco.software.simuspace.susdash.core.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The Class HpcJobFileDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class HpcJobFileDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1023518821267431967L;

    /**
     * The id.
     */
    @UIColumn( data = "id", name = "id", filter = "uuid", renderer = "text", title = "3000021x4", orderNum = 0 )
    private String id;

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000161x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The size.
     */
    @UIColumn( data = "size", name = "size", filter = "text", renderer = "text", title = "3000123x4", orderNum = 2 )
    private String size;

    /**
     * The attributes.
     */
    @UIColumn( data = "attributes", name = "permission", filter = "text", renderer = "text", title = "3000205x4", orderNum = 3 )
    private String attributes;

    /**
     * The updated on.
     */
    @UIColumn( data = "updatedOn", name = "updated_file_time", filter = "dateRange", renderer = "date", title = "3000206x4", orderNum = 4 )
    private Date updatedOn;

    /**
     * The path.
     */
    private String path;

    /**
     * The downloadable.
     */
    @UIColumn( data = "downloadable", name = "Downloadable", filter = "text", renderer = "text", title = "3000207x4", orderNum = 5 )
    private String downloadable;

    /**
     * Instantiates a new hpc job file DTO.
     */
    public HpcJobFileDTO() {
        super();
    }

    /**
     * Instantiates a new hpc file DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param size
     *         the size
     * @param attributes
     *         the attributes
     * @param updatedOn
     *         the updated on
     * @param path
     *         the path
     * @param downloadable
     *         the downloadable
     */
    public HpcJobFileDTO( String id, String name, String size, String attributes, Date updatedOn, String path, String downloadable ) {
        super();
        this.id = id;
        this.name = name;
        this.size = size;
        this.attributes = attributes;
        this.updatedOn = updatedOn;
        this.path = path;
        this.downloadable = downloadable;
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
     * Sets the id.
     *
     * @param id
     *         the id to set
     */
    public void setId( String id ) {
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
     *         the name to set
     */
    public void setName( String name ) {
        this.name = name;
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
     *         the size to set
     */
    public void setSize( String size ) {
        this.size = size;
    }

    /**
     * Gets the attributes.
     *
     * @return the attributes
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes
     *         the attributes to set
     */
    public void setAttributes( String attributes ) {
        this.attributes = attributes;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the path to set
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets the downloadable.
     *
     * @return the downloadable
     */
    public String getDownloadable() {
        return downloadable;
    }

    /**
     * Sets the downloadable.
     *
     * @param downloadable
     *         the downloadable to set
     */
    public void setDownloadable( String downloadable ) {
        if ( downloadable.equals( "1" ) ) {
            this.downloadable = "true";

        } else if ( downloadable.equals( "0" ) ) {
            this.downloadable = "false";

        }
    }

    /**
     * Gets the updated on.
     *
     * @return the updated on
     */
    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets the updated on.
     *
     * @param updatedOn
     *         the new updated on
     */
    public void setUpdatedOn( Date updatedOn ) {
        this.updatedOn = updatedOn;
    }

}