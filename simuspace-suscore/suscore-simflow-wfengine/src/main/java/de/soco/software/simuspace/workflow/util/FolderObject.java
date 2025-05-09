package de.soco.software.simuspace.workflow.util;

import java.util.List;

/**
 * This is the field of {@link FolderContentInfo} contains info regarding data object type and file extensions allowed for that particular
 * object.
 *
 * @author M.Nasir.Farooq
 */
public class FolderObject {

    /**
     * The object type.
     */
    private String type;

    /**
     * The file extensions.
     */
    private List< String > extensions;

    /**
     * Instantiates a new folder object.
     */
    public FolderObject() {
        super();
    }

    /**
     * Instantiates a new folder object.
     *
     * @param type
     *         the type
     * @param extensions
     *         the extensions
     */
    public FolderObject( String type, List< String > extensions ) {
        super();
        this.type = type;
        this.extensions = extensions;
    }

    /**
     * Gets the extensions.
     *
     * @return the extensions
     */
    public List< String > getExtensions() {
        return extensions;
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
     * Sets the extensions.
     *
     * @param extensions
     *         the extensions to set
     */
    public void setExtensions( List< String > extensions ) {
        this.extensions = extensions;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FolderObject [type=" + type + ", extensions=" + extensions + "]";
    }

}
