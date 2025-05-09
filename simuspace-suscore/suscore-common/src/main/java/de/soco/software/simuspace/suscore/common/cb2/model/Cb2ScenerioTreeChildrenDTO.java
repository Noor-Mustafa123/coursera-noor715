package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2LoadcaseTreeChildrenDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2ScenerioTreeChildrenDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 3246840121910528796L;

    /**
     * The id.
     */
    private String id;

    /**
     * The title.
     */
    private String title;

    /**
     * The path.
     */
    private String path;

    /**
     * The url.
     */
    private String url;

    /**
     * The children.
     */
    private List< Cb2ScenerioTreeChildrenDTO > children;

    /**
     * Gets the children.
     *
     * @return the children
     */
    public List< Cb2ScenerioTreeChildrenDTO > getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( List< Cb2ScenerioTreeChildrenDTO > children ) {
        this.children = children;
    }

    /**
     * Instantiates a new cb 2 scenerio tree children DTO.
     */
    public Cb2ScenerioTreeChildrenDTO() {
    }

    /**
     * Instantiates a new cb 2 scenerio tree children DTO.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param path
     *         the path
     * @param url
     *         the url
     * @param children
     *         the children
     */
    public Cb2ScenerioTreeChildrenDTO( String id, String title, String path, String url, List< Cb2ScenerioTreeChildrenDTO > children ) {
        super();
        this.id = id;
        this.title = title;
        this.path = path;
        this.url = url;
        this.children = children;
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

}
