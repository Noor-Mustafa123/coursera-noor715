package de.soco.software.simuspace.server.model;

import java.util.List;

/**
 * The class JobTreeNodeDTO contains all the relevant fields like id,title,folder.
 */
public class JobTreeNodeDTO {

    /**
     * The id.
     */
    private String id;

    /**
     * The title.
     */
    private String title;

    /**
     * The folder.
     */
    private boolean folder = true;

    /**
     * The icon.
     */
    private String icon = "fa fa-briefcase font-red";

    /**
     * The children.
     */
    private List< JobTreeNodeDTO > children;

    /**
     * The expanded.
     */
    private boolean expanded = false;

    /**
     * Instantiates a new tree node.
     */
    public JobTreeNodeDTO() {
        super();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *         the new title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * Checks if is folder.
     *
     * @return true, if is folder
     */
    public boolean isFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     *
     * @param folder
     *         the new folder
     */
    public void setFolder( boolean folder ) {
        this.folder = folder;
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List< JobTreeNodeDTO > getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( List< JobTreeNodeDTO > children ) {
        this.children = children;
    }

    /**
     * Checks if is expanded.
     *
     * @return true, if is expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Sets the expanded.
     *
     * @param expanded
     *         the new expanded
     */
    public void setExpanded( boolean expanded ) {
        this.expanded = expanded;
    }

}
