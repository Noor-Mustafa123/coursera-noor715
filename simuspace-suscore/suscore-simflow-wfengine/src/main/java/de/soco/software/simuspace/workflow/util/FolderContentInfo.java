package de.soco.software.simuspace.workflow.util;

import java.util.List;

/**
 * This is POJO class for mapping user import info for scanning data object from a directory.
 *
 * @author M.Nasir.Farooq
 */
public class FolderContentInfo {

    /**
     * The folder name used for scanning data object.
     */
    private String folder;

    /**
     * The objects info required for scanning data objects.
     */
    private List< FolderObject > objects;

    /**
     * Instantiates a new folder content info.
     */
    public FolderContentInfo() {
        super();
    }

    /**
     * Instantiates a new folder content info.
     *
     * @param folder
     *         the folder
     * @param objects
     *         the objects
     */
    public FolderContentInfo( String folder, List< FolderObject > objects ) {
        super();
        this.setFolder( folder );
        this.setObjects( objects );
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
     * Gets the objects.
     *
     * @return the objects
     */
    public List< FolderObject > getObjects() {
        return objects;
    }

    /**
     * Sets the folder.
     *
     * @param folder
     *         the folder to set
     */
    public void setFolder( String folder ) {
        this.folder = folder;
    }

    /**
     * Sets the objects.
     *
     * @param objects
     *         the objects to set
     */
    public void setObjects( List< FolderObject > objects ) {
        this.objects = objects;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FolderContentInfo [folder=" + folder + ", objects=" + objects + "]";
    }

}
