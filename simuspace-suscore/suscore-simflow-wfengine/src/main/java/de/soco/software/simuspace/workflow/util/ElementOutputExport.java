package de.soco.software.simuspace.workflow.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;

/**
 * The Class ElementOutputExport.
 *
 * @author Ababs
 */
public class ElementOutputExport extends ElementOutput implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The count exported files.
     */
    private int countExportedFiles;

    /**
     * The count exported objects.
     */
    private int countExportedObjects;

    /**
     * The exported objects.
     */
    private List< DataObjectDTO > exportedObjects = new ArrayList<>();

    /**
     * Gets the count exported files.
     *
     * @return the count exported files
     */
    public int getCountExportedFiles() {
        return countExportedFiles;
    }

    /**
     * Gets the count exported objects.
     *
     * @return the count exported objects
     */
    public int getCountExportedObjects() {
        return countExportedObjects;
    }

    /**
     * Gets the exported objects.
     *
     * @return the exported objects
     */
    public List< DataObjectDTO > getExportedObjects() {
        return exportedObjects;
    }

    /**
     * Sets the count exported files.
     *
     * @param countExportedFiles
     *         the new count exported files
     */
    public void setCountExportedFiles( int countExportedFiles ) {
        this.countExportedFiles = countExportedFiles;
    }

    /**
     * Sets the count exported objects.
     *
     * @param countExportedObjects
     *         the new count exported objects
     */
    public void setCountExportedObjects( int countExportedObjects ) {
        this.countExportedObjects = countExportedObjects;
    }

    /**
     * Sets the exported objects.
     *
     * @param exportedObjects
     *         the new exported objects
     */
    public void setExportedObjects( List< DataObjectDTO > exportedObjects ) {
        this.exportedObjects = exportedObjects;
    }

}
