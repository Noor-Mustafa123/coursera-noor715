/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FileObject for file properties.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FileObject implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -7812483085990078938L;

    /**
     * The lazy.
     */
    private boolean lazy;

    /**
     * The title.
     */
    private String title;

    /**
     * The update.
     */
    private Long update;

    /**
     * The path.
     */
    private String path;

    /**
     * The folder.
     */
    private boolean folder;

    /**
     * The type.
     */
    private String type;

    /**
     * The size.
     */
    private Long size;

    /**
     * Instantiates a new simstream file object.
     */
    public FileObject() {
        super();
    }

    /**
     * Instantiates a new simstream file object.
     *
     * @param lazy
     *         the lazy
     * @param title
     *         the title
     * @param update
     *         the update
     * @param path
     *         the path
     * @param folder
     *         the folder
     * @param type
     *         the type
     * @param size
     *         the size
     */
    public FileObject( boolean lazy, String title, Long update, String path, boolean folder, String type, Long size ) {
        super();
        this.lazy = lazy;
        this.title = title;
        this.update = update;
        this.path = path;
        this.setFolder( folder );
        this.type = type;
        this.size = size;
    }

    /**
     * Checks if is lazy.
     *
     * @return true, if is lazy
     */
    public boolean isLazy() {
        return lazy;
    }

    /**
     * Sets the lazy.
     *
     * @param lazy
     *         the new lazy
     */
    public void setLazy( boolean lazy ) {
        this.lazy = lazy;
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
     * Gets the update.
     *
     * @return the update
     */
    public Long getUpdate() {
        return update;
    }

    /**
     * Sets the update.
     *
     * @param update
     *         the new update
     */
    public void setUpdate( Long update ) {

        this.update = update;
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
     * Gets the size.
     *
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( Long size ) {
        this.size = size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "FileObject [lazy=" + lazy + ", title=" + title + ", update=" + update + ", path=" + path + ", folder=" + folder + ", type="
                + type + ", size=" + size + "]";
    }

}
