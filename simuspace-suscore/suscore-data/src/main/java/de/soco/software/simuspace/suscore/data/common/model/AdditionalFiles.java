package de.soco.software.simuspace.suscore.data.common.model;

import java.util.Map;

import de.soco.software.simuspace.suscore.data.model.Location;

/**
 * The Class AdditionalFiles is to map additional files from Run Dummy varient
 *
 * @author Noman Arshad
 */
public class AdditionalFiles {

    /**
     * The full path.
     */
    private String fullPath;

    /**
     * The path.
     */
    private String path;

    /**
     * The folder.
     */
    private String folder;

    /**
     * The size.
     */
    private String size;

    /**
     * The lazy.
     */
    private String lazy;

    /**
     * The update.
     */
    private String update;

    /**
     * The location.
     */
    private Location location;

    /**
     * The type.
     */
    private String type;

    /**
     * The title.
     */
    private String title;

    /**
     * The custom attributes.
     */
    private Map< String, Object > customAttributes;

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
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets the folder.
     *
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     *
     * @param folder
     *         the new folder
     */
    public void setFolder( String folder ) {
        this.folder = folder;
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
     * Gets the lazy.
     *
     * @return the lazy
     */
    public String getLazy() {
        return lazy;
    }

    /**
     * Sets the lazy.
     *
     * @param lazy
     *         the new lazy
     */
    public void setLazy( String lazy ) {
        this.lazy = lazy;
    }

    /**
     * Gets the update.
     *
     * @return the update
     */
    public String getUpdate() {
        return update;
    }

    /**
     * Sets the update.
     *
     * @param update
     *         the new update
     */
    public void setUpdate( String update ) {
        this.update = update;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "AdditionalFiles [fullPath=" + fullPath + ", path=" + path + ", folder=" + folder + ", size=" + size + ", lazy=" + lazy
                + ", update=" + update + ", location=" + location + ", type=" + type + ", title=" + title + ", customAttributes="
                + customAttributes + "]";
    }

}
