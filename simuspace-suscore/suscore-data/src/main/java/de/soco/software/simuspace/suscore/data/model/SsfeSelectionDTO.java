package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * The Class SsfeSelectionDTO.
 *
 * @author Ali Haider
 */
public class SsfeSelectionDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -5374676238119397807L;

    /**
     * The name.
     */
    @UIColumn( data = "title", name = "title", filter = "text", renderer = "dirLabel", title = "3000032x4", orderNum = 1 )
    private String title;

    /**
     * The file path.
     */
    @UIColumn( data = "fullPath", name = "fullPath", filter = "text", renderer = "text", title = "3000214x4", orderNum = 2 )
    private String fullPath;

    /**
     * The type.
     */
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "replaceText", options = "<i class='{icon}'></i> {type}", title = "3000051x4", orderNum = 3 )
    private String type;

    /**
     * The size.
     */
    @UIColumn( data = "size", name = "size", filter = "text", renderer = "text", title = "3000123x4", orderNum = 4 )
    private String size;

    /**
     * The location.
     */
    @UIColumn( data = "location.name", name = "location", filter = "text", renderer = "text", title = "3000122x4", orderNum = 5 )
    private Location location;

    /**
     * The folder.
     */
    private boolean folder;

    /**
     * Instantiates a new ssfe selection DTO.
     */
    public SsfeSelectionDTO() {
        super();
    }

    /**
     * Instantiates a new ssfe selection DTO.
     *
     * @param title
     *         the title
     * @param fullPath
     *         the full path
     * @param type
     *         the type
     * @param size
     *         the size
     * @param location
     *         the location
     */
    @JsonCreator
    public SsfeSelectionDTO( @JsonProperty( "title" ) String title, @JsonProperty( "fullPath" ) String fullPath,
            @JsonProperty( "type" ) String type, @JsonProperty( "size" ) String size ) {
        super();
        this.title = title;
        this.fullPath = fullPath;
        this.type = type;
        this.size = size;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *         the new title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * Gets the full path.
     *
     * @return the full path
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Sets the full path.
     *
     * @param fullPath
     *         the new full path
     */
    public void setFullPath( String fullPath ) {
        this.fullPath = fullPath;
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

    /**
     * Gets the location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocation( Location location ) {
        this.location = location;
    }

    /**
     * Checks if is folder.
     *
     * @return true, if is folder
     */
    public boolean isFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     *
     * @param folder
     *         the new folder
     */
    public void setFolder( boolean folder ) {
        this.folder = folder;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "SsfeSelectionDTO [name=" + title + ", path=" + fullPath + ", type=" + type + ", size=" + size + ", location=" + location
                + "]";
    }

}
