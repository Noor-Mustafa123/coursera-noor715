package de.soco.software.simuspace.workflow.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;

/**
 * The Class ElementOutputImport.
 */
public class ElementOutputTransfer extends ElementOutput implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The count of copied files.
     */
    private int countOfCopiedFiles = 0;

    /**
     * The count of copied objects.
     */
    private int countOfCopiedObjects = 0;

    /**
     * The copied objects.
     */
    private List< ObjectDTO > copiedObjects = new ArrayList<>();

    public List< ObjectDTO > getCopiedObjects() {
        return copiedObjects;
    }

    public int getCountOfCopiedFiles() {
        return countOfCopiedFiles;
    }

    public int getCountOfCopiedObjects() {
        return countOfCopiedObjects;
    }

    public void setCopiedObjects( List< ObjectDTO > objectDTOs ) {
        copiedObjects = objectDTOs;
    }

    public void setCountOfCopiedFiles( int countOfCopiedFiles ) {
        this.countOfCopiedFiles = countOfCopiedFiles;
    }

    public void setCountOfCopiedObjects( int countOfCopiedObjects ) {
        this.countOfCopiedObjects = countOfCopiedObjects;
    }

}
