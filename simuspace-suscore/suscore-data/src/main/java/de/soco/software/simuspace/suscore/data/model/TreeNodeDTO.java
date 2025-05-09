package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

/**
 * The class TreeNodeDTO contains all the relevant fields like id,title,folder.
 */
public class TreeNodeDTO {

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
     * The lazy.
     */
    private boolean lazy = true;

    /**
     * The icon.
     */
    private String icon = "fa fa-briefcase font-red";

    /**
     * The description.
     */
    private String description;

    /**
     * The url.
     */
    private String url;

    /**
     * The children.
     */
    private List< TreeNodeDTO > children;

    /**
     * The state.
     */
    private int state = 0;

    /**
     * The element.
     */
    private Object element;

    private boolean expanded = false;

    /**
     * Instantiates a new tree node.
     */
    public TreeNodeDTO() {
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List< TreeNodeDTO > getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( List< TreeNodeDTO > children ) {
        this.children = children;
    }

    /**
     * Checks if is lazy.
     *
     * @return true, if is lazy
     */
    public boolean isLazy() {
        return lazy;
    }

    /**
     * Sets the lazy.
     *
     * @param lazy
     *         the new lazy
     */
    public void setLazy( boolean lazy ) {
        this.lazy = lazy;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state
     *         the new state
     */
    public void setState( int state ) {
        this.state = state;
    }

    /**
     * Gets the element.
     *
     * @return the element
     */
    public Object getElement() {
        return element;
    }

    /**
     * Sets the element.
     *
     * @param element
     *         the new element
     */
    public void setElement( Object element ) {
        this.element = element;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded( boolean expanded ) {
        this.expanded = expanded;
    }

}
