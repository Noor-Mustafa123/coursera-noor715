package de.soco.software.simuspace.suscore.data.common.model;

/**
 * The Class SourceTargetData.
 */
public class SourceTargetData {

    /**
     * The Nodeid name defination.
     */
    SourceTargetDefination data;

    /**
     * The classes.
     */
    public String classes;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public SourceTargetDefination getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( SourceTargetDefination data ) {
        this.data = data;
    }

    /**
     * Gets the classes.
     *
     * @return the classes
     */
    public String getClasses() {
        return classes;
    }

    /**
     * Sets the classes.
     *
     * @param classes
     *         the new classes
     */
    public void setClasses( String classes ) {
        this.classes = classes;
    }

}