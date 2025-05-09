package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2TreeChildrenDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2TreeChildrenDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1010264990982549512L;

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
    private boolean lazy = false;

    /**
     * The icon.
     */
    private String icon = "fa fa-briefcase font-black";

    /**
     * The description.
     */
    private String description;

    /**
     * The url.
     */
    private String url;

    /**
     * The state.
     */
    private int state = 0;

    /**
     * The element.
     */
    private String element;

    /**
     * The expanded.
     */
    private boolean expanded = false;

    /**
     * The project.
     */
    private String path;

    /**
     * The children.
     */
    private List< Cb2TreeChildrenDTO > children;

    /**
     * Instantiates a new cb 2 tree children DTO.
     */
    public Cb2TreeChildrenDTO() {
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
    public String getElement() {
        return element;
    }

    /**
     * Sets the element.
     *
     * @param element
     *         the new element
     */
    public void setElement( String element ) {
        this.element = element;
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

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List< Cb2TreeChildrenDTO > getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( List< Cb2TreeChildrenDTO > children ) {
        this.children = children;
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
     * Sets the path.
     *
     * @param path
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
