package de.soco.software.simuspace.workflow.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.workflow.model.impl.ObjectDTO;

/**
 * The Class ElementOutputImport.
 *
 * @author Abbas
 */
public class ElementOutputImport extends ElementOutput implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The created objects.
     */
    private int countCreatedObjects;

    /**
     * The count created files.
     */
    private int countCreatedFiles;

    /**
     * The createdobjects.
     */
    private final List< ObjectDTO > createdobjects = new ArrayList<>();

    /**
     * Gets the count created files.
     *
     * @return the count created files
     */
    public int getCountCreatedFiles() {
        return countCreatedFiles;
    }

    /**
     * Gets the count created objects.
     *
     * @return the count created objects
     */
    public int getCountCreatedObjects() {
        return countCreatedObjects;
    }

    /**
     * Gets the createdobjects.
     *
     * @return the createdobjects
     */
    public List< ObjectDTO > getCreatedobjects() {
        return createdobjects;
    }

    /**
     * Sets the count created files.
     *
     * @param countCreatedFiles
     *         the new count created files
     */
    public void setCountCreatedFiles( int countCreatedFiles ) {
        this.countCreatedFiles = countCreatedFiles;
    }

    /**
     * Sets the count created objects.
     *
     * @param countCreatedObjects
     *         the new count created objects
     */
    public void setCountCreatedObjects( int countCreatedObjects ) {
        this.countCreatedObjects = countCreatedObjects;
    }

}
