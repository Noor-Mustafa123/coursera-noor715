package de.soco.software.simuspace.suscore.data.model;

/**
 * The configuration class for routers in SUS.
 *
 * @author Huzaifah
 */
public class RouterConfigItem {

    /**
     * The Constant TREE_CONTEXT.
     */
    public static final String TREE_CONTEXT = "tree-context";

    /**
     * The Constant PATTERN_ID.
     */
    public static final String ID_PATTERN = "{id}";

    /**
     * The Constant KEY_PATTERN.
     */
    public static final String KEY_PATTERN = "{key}";

    /**
     * The Constant QUERY_PATTERN.
     */
    public static final String QUERY_PATTERN = "{?query}";

    /**
     * The Constant MODE_SINGLE.
     */
    public static final String MODE_SINGLE = "?mode=single";

    /**
     * The Constant MODE_BULK.
     */
    public static final String MODE_BULK = "?mode=bulk";

    /**
     * The Constant DEFUALT_VISIBILITY.
     */
    public static final String DEFUALT_VISIBILITY = "both";

    /**
     * The url on which the resource or plugin resides.
     */
    private String url;

    /**
     * the container to be loaded.
     */
    private String container;

    /**
     * the js file which would be loaded.
     */
    private String js;

    /**
     * the type for which a URL applies, e.g. table-view, context-menu, or single view etc
     */
    private String type;

    /**
     * the title which would be appeared for tree context.
     */
    private String title;

    /**
     * the icon to be shown.
     */
    private String icon;

    /**
     * className.
     */
    private String className;

    /**
     * The visibility.
     */
    private String visibility = DEFUALT_VISIBILITY;

    /**
     * Instantiates a new router config item.
     */
    public RouterConfigItem() {
        super();
    }

    /**
     * Gets the container.
     *
     * @return the container
     */
    public String getContainer() {
        return container;
    }

    /**
     * Sets the container.
     *
     * @param container
     *         the new container
     */
    public void setContainer( String container ) {
        this.container = container;
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
     * Gets the js.
     *
     * @return the js
     */
    public String getJs() {
        return js;
    }

    /**
     * Sets the js.
     *
     * @param js
     *         the new js
     */
    public void setJs( String js ) {
        this.js = js;
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
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
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
     * Gets the class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name.
     *
     * @param className
     *         the new class name
     */
    public void setClassName( String className ) {
        this.className = className;
    }

    /**
     * Gets the visibility.
     *
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility.
     *
     * @param visibility
     *         the new visibility
     */
    public void setVisibility( String visibility ) {
        this.visibility = visibility;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RouterConfigItem [url=" + url + ", container=" + container + ", js=" + js + ", type=" + type + ", title=" + title
                + ", icon=" + icon + ", className=" + className + ", visibility=" + visibility + "]";
    }

}