package de.soco.software.suscore.jsonschema.model;

/**
 * Reprents a tab configuration properties
 *
 * @author Huzaifah
 */
public class OVAConfigTab {

    /**
     * The id.
     */
    private String typeId;

    /**
     * unique attribute for each tab
     */
    private String key;

    /**
     * to show to the user in UI
     */
    private String title;

    /**
     * tab is visible or not from configuration
     */
    private boolean visible;

    /**
     * Gets the type id.
     *
     * @return the type id
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( String typeId ) {
        this.typeId = typeId;
    }

    /**
     * get key of this tab
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * set key of this tab
     *
     * @param key
     */
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * get title to show for this tab
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title to show for this tab
     *
     * @param title
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * if tab is visible or not
     *
     * @return visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * set if tab is visible or not
     *
     * @param visible
     */
    public void setVisible( boolean visible ) {
        this.visible = visible;
    }

    /**
     * Create a new {@link OVAConfigTab}
     *
     * @param key
     * @param title
     * @param visible
     */
    public OVAConfigTab( String key, String title, boolean visible ) {
        super();
        this.key = key;
        this.title = title;
        this.visible = visible;
    }

    /**
     * Empty Constructor for Jackson
     */
    public OVAConfigTab() {
        super();
    }

}
