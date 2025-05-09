package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.Map;

/**
 * The Class WidgetSettingsDTO.
 *
 * @author Ali Haider
 */
public class WidgetSettingsDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2775587540359244615L;

    /**
     * The id.
     */
    private String id;

    /**
     * The title.
     */
    private String title;

    /**
     * The settings.
     */
    private Map< String, Object > settings;

    /**
     * The view id.
     */
    private Integer viewId;

    /**
     * Instantiates a new widget settings.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param settings
     *         the settings
     * @param viewId
     *         the view id
     */
    public WidgetSettingsDTO( String id, String title, Map< String, Object > settings ) {
        super();
        this.id = id;
        this.title = title;
        this.settings = settings;
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
     * Gets the settings.
     *
     * @return the settings
     */
    public Map< String, Object > getSettings() {
        return settings;
    }

    /**
     * Sets the settings.
     *
     * @param settings
     *         the settings
     */
    public void setSettings( Map< String, Object > settings ) {
        this.settings = settings;
    }

    /**
     * Gets the view id.
     *
     * @return the view id
     */
    public Integer getViewId() {
        return viewId;
    }

    /**
     * Sets the view id.
     *
     * @param viewId
     *         the new view id
     */
    public void setViewId( Integer viewId ) {
        this.viewId = viewId;
    }

}
