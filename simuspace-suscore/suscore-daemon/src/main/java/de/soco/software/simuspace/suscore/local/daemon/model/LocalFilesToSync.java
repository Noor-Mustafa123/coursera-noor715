package de.soco.software.simuspace.suscore.local.daemon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class LocalFilesToSync to contain all the local files details so it can be compared while uploading or downloading object.
 *
 * @author noman
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LocalFilesToSync {

    /**
     * The name.
     */
    private String name;

    /**
     * The path.
     */
    private String path;

    /**
     * The hash.
     */
    private String hash;

    /**
     * The is dir.
     */
    private boolean isDir;

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
     * Gets the hash.
     *
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash.
     *
     * @param hash
     *         the new hash
     */
    public void setHash( String hash ) {
        this.hash = hash;
    }

    /**
     * Gets the checks if is dir.
     *
     * @return the checks if is dir
     */
    public boolean getIsDir() {
        return isDir;
    }

    /**
     * Sets the checks if is dir.
     *
     * @param isDir
     *         the new checks if is dir
     */
    public void setIsDir( boolean isDir ) {
        this.isDir = isDir;
    }

}
