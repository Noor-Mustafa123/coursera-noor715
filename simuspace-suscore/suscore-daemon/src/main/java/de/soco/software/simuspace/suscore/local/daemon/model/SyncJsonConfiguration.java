package de.soco.software.simuspace.suscore.local.daemon.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class SyncDataConfig is to map files or folders.
 *
 * @author Ali Haider
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SyncJsonConfiguration {

    /**
     * The configuration.
     */
    private Map< String, List< String > > folders;

    /**
     * The files.
     */
    private Map< String, List< String > > files;

    /**
     * Gets the folders.
     *
     * @return the folders
     */
    public Map< String, List< String > > getFolders() {
        return folders;
    }

    /**
     * Sets the folders.
     *
     * @param folders
     *         the folders
     */
    public void setFolders( Map< String, List< String > > folders ) {
        this.folders = folders;
    }

    /**
     * Gets the files.
     *
     * @return the files
     */
    public Map< String, List< String > > getFiles() {
        return files;
    }

    /**
     * Sets the files.
     *
     * @param files
     *         the files
     */
    public void setFiles( Map< String, List< String > > files ) {
        this.files = files;
    }

}
