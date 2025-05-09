package de.soco.software.simuspace.workflow.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class ImportObject.
 */
public class ImportObject {

    /**
     * The name.
     */
    private String name;

    /**
     * The path.
     */
    private String path;

    /**
     * The is folder.
     */
    private boolean isFolder;

    /**
     * The parent.
     */
    private String parent;

    /**
     * The object type.
     */
    private String objectType;

    /**
     * The group.
     */
    private String group;

    /**
     * The children.
     */
    private List< ImportObject > children = new ArrayList<>();

    /**
     * The custom attributes.
     */
    private Map< String, Object > customAttributes;

    /**
     * Instantiates a new import object.
     */
    public ImportObject() {
        super();
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List< ImportObject > getChildren() {
        return children;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public String getParent() {
        return parent;
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
     * Checks if is folder.
     *
     * @return true, if is folder
     */
    public boolean isFolder() {
        return isFolder;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( List< ImportObject > children ) {
        this.children = children;
    }

    /**
     * Sets the folder.
     *
     * @param isFolder
     *         the new folder
     */
    public void setFolder( boolean isFolder ) {
        this.isFolder = isFolder;
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
     * Sets the object type.
     *
     * @param objectType
     *         the new object type
     */
    public void setObjectType( String objectType ) {
        this.objectType = objectType;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *         the new parent
     */
    public void setParent( String parent ) {
        this.parent = parent;
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
     * Gets the group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group.
     *
     * @param group
     *         the new group
     */
    public void setGroup( String group ) {
        this.group = group;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ImportObject [name=" + name + ", path=" + path + ", isFolder=" + isFolder + ", parent=" + parent + ", objectType="
                + objectType + "]";
    }

    /**
     * Gets the custom attributes.
     *
     * @return the custom attributes
     */
    public Map< String, Object > getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the custom attributes.
     *
     * @param customAttributes
     *         the custom attributes
     */
    public void setCustomAttributes( Map< String, Object > customAttributes ) {
        this.customAttributes = customAttributes;
    }

}
