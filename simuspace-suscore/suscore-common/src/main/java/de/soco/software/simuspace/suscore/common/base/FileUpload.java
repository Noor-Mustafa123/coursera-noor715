package de.soco.software.simuspace.suscore.common.base;

import java.io.Serializable;

/**
 * This class contains an object of a file.
 */
public class FileUpload implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The agent.
     */
    private String selType;

    /**
     * The doc id.
     */
    private int docId;

    /**
     * The location.
     */
    private int locationId;

    /**
     * The path.
     */
    private String path;

    /**
     * Instantiates a new file.
     */
    public FileUpload() {
        super();
    }

    /**
     * Instantiates a new file.
     *
     * @param path
     *         the path
     */
    public FileUpload( String path ) {
        this();
        this.path = path;
    }

    /**
     * Instantiates a new file.
     *
     * @param agent
     *         the agent
     * @param path
     *         the path
     * @param docId
     *         the doc id
     * @param location
     *         the location
     */
    public FileUpload( String agent, String path, int docId, int location ) {
        this();
        this.setSelType( agent );
        this.path = path;
        this.docId = docId;
        locationId = location;
    }

    /**
     * Gets the doc id.
     *
     * @return the doc id
     */
    public int getDocId() {
        return docId;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public int getLocationId() {
        return locationId;
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
     * Sets the doc id.
     *
     * @param docId
     *         the new doc id
     */
    public void setDocId( int docId ) {
        this.docId = docId;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocationId( int location ) {
        locationId = location;
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
     * Gets the sel type.
     *
     * @return the sel type
     */
    public String getSelType() {
        return selType;
    }

    /**
     * Sets the sel type.
     *
     * @param selType
     *         the new sel type
     */
    public void setSelType( String selType ) {
        this.selType = selType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "File [selType='" + getSelType() + "', path='" + path + "', docId='" + docId + "', location='" + locationId + "']";
    }

}
