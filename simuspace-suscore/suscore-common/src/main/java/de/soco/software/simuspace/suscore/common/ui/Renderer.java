package de.soco.software.simuspace.suscore.common.ui;

import java.util.Map;

/**
 * The Rendered class for rendering items at front end.
 */
public class Renderer {

    /**
     * the type for items to be rendered.
     */
    private String type;

    /**
     * The image width.
     */
    private int imageWidth;

    /**
     * the url of the router.
     */
    private String url;

    /**
     * The url key.
     */
    private String url_key;

    /**
     * The url values.
     */
    private Map< String, String > url_values;

    /**
     * the separator.
     */
    private String separator;

    /**
     * the label class used as class for which the table/items are rendered.
     */
    private String labelClass;

    /**
     * the column name to display in case of list of object for table view.
     */
    private String data;

    /**
     * The manage.
     */
    private Object manage;

    /**
     * The options.
     */
    private String options;

    /**
     * The tooltip.
     */
    private String tooltip;

    /**
     * The auto delete.
     */
    private String autoDeleted;

    public Renderer( String type, int imageWidth, String data, Object manage ) {
        super();
        this.type = type;
        this.imageWidth = imageWidth;
        this.data = data;
        this.manage = manage;
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
     * Gets the url key.
     *
     * @return the url key
     */
    public String getUrl_key() {
        return url_key;
    }

    /**
     * Sets the url key.
     *
     * @param url_key
     *         the new url key
     */
    public void setUrl_key( String url_key ) {
        this.url_key = url_key;
    }

    /**
     * Gets the url values.
     *
     * @return the url values
     */
    public Map< String, String > getUrl_values() {
        return url_values;
    }

    /**
     * Sets the url values.
     *
     * @param url_values
     *         the url values
     */
    public void setUrl_values( Map< String, String > url_values ) {
        this.url_values = url_values;
    }

    /**
     * Gets the separator.
     *
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets the separator.
     *
     * @param separator
     *         the new separator
     */
    public void setSeparator( String separator ) {
        this.separator = separator;
    }

    /**
     * Gets the label class.
     *
     * @return the label class
     */
    public String getLabelClass() {
        return labelClass;
    }

    /**
     * Sets the label class.
     *
     * @param labelClass
     *         the new label class
     */
    public void setLabelClass( String labelClass ) {
        this.labelClass = labelClass;
    }

    /**
     * Instantiates a new renderer.
     */
    public Renderer() {
        super();
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( String data ) {
        this.data = data;
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
     * Gets the manage.
     *
     * @return the manage
     */
    public Object getManage() {
        return manage;
    }

    /**
     * Sets the manage.
     *
     * @param manage
     *         the new manage
     */
    public void setManage( Object manage ) {
        this.manage = manage;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public String getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *         the new options
     */
    public void setOptions( String options ) {
        this.options = options;
    }

    /**
     * Gets the tooltip.
     *
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the tooltip.
     *
     * @param tooltip
     *         the new tooltip
     */
    public void setTooltip( String tooltip ) {
        this.tooltip = tooltip;
    }

    /**
     * Checks if is auto delete.
     *
     * @return true, if is auto delete
     */
    public String getAutoDeleted() {
        return autoDeleted;
    }

    /**
     * Sets the auto delete.
     *
     * @param autoDelete
     *         the new auto delete
     */
    public void setAutoDeleted( String autoDeleted ) {
        this.autoDeleted = autoDeleted;
    }

    /**
     * Gets the image width.
     *
     * @return the image width
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the image width.
     *
     * @param imageWidth
     *         the new image width
     */
    public void setImageWidth( int imageWidth ) {
        this.imageWidth = imageWidth;
    }

}