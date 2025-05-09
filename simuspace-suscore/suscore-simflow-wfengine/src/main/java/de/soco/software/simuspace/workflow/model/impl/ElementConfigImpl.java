package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.ElementConfig;

/**
 * This Class contains properties of a Workflow element
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ElementConfigImpl implements ElementConfig, Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The allowed field keys. These are fields that an element is allowed to attach with an element
     */
    private List< String > allowedFieldKeys;

    /**
     * The categories.
     */
    private List< Category > categories;

    /**
     * The comments.
     */
    private String comments;

    /**
     * The description.
     */
    private String description;

    /**
     * The fields. Fields are properties that an element can have i.e, an element can have a text area, a dropdown etc
     */

    private List< Field< ? > > fields;

    /**
     * The icon. Name of icon that is attached to a element icon name is a String
     */
    private String icon;

    /**
     * The info.
     */
    private ElementInfo info;

    /**
     * The is active.
     */
    private boolean isActive;

    /**
     * The is internal.
     */
    private boolean isInternal;

    /**
     * The key.
     */
    private String key;

    /**
     * The name.
     */
    private String name;

    /**
     * The run mode. It can be [client,server, user-selected, null] either a workflow is running on client side or server side
     */
    private String runMode;

    /**
     * The shape.
     */
    private String shape;

    /**
     * The version.
     */
    private String version;

    /**
     * The allowed connections.
     */
    private List< String > allowedConnections;

    /**
     * Instantiates a new element config impl.
     */
    public ElementConfigImpl() {
        super();
    }

    /**
     * Instantiates a new element config impl.
     *
     * @param name
     *         the name
     * @param key
     *         the key
     * @param description
     *         the description
     * @param comments
     *         the comments
     * @param allowedFieldKeys
     *         the allowed field keys
     * @param fields
     *         the fields
     * @param shape
     *         the shape
     * @param icon
     *         the icon
     * @param isInternal
     *         the is internal
     * @param isActive
     *         the is active
     * @param runMode
     *         the run mode
     * @param categories
     *         the categories
     * @param version
     *         the version
     */
    public ElementConfigImpl( String name, String key, String description, String comments, List< String > allowedFieldKeys,
            List< Field< ? > > fields, String shape, String icon, boolean isInternal, boolean isActive, String runMode,
            List< Category > categories, String version ) {
        super();
        this.name = name;
        this.key = key;
        this.description = description;
        this.comments = comments;
        this.allowedFieldKeys = allowedFieldKeys;
        this.fields = fields;
        this.shape = shape;
        this.icon = icon;
        this.isInternal = isInternal;
        this.isActive = isActive;
        this.runMode = runMode;
        this.categories = categories;
        this.version = version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getAllowedConnections() {
        return allowedConnections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getAllowedFieldKeys() {
        return allowedFieldKeys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Category > getCategories() {
        return categories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComments() {
        return comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Field< ? > > getFields() {
        return fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon() {
        return icon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElementInfo getInfo() {
        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunMode() {
        return runMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getShape() {
        return shape;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return isActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal() {
        return isInternal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActive( boolean isActive ) {
        this.isActive = isActive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAllowedConnections( List< String > allowedConnections ) {
        this.allowedConnections = allowedConnections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAllowedFieldKeys( List< String > allowedFieldKeys ) {
        this.allowedFieldKeys = allowedFieldKeys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCategories( List< Category > categories ) {
        this.categories = categories;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComments( String comments ) {
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFields( List< Field< ? > > fields ) {
        this.fields = fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Sets the info.
     *
     * @param info
     *         the new info
     */
    public void setInfo( ElementInfo info ) {
        this.info = info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInternal( boolean isInternal ) {
        this.isInternal = isInternal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKey( String key ) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRunMode( String runMode ) {
        this.runMode = runMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShape( String shape ) {
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ElementConfigImpl [name='" + name + "', key='" + key + "', description='" + description + "', comments='" + comments
                + "', allowedFieldKeys='" + allowedFieldKeys + "', Fields='" + fields + "', shape='" + shape + "', icon='" + icon
                + "', isInternal='" + isInternal + "', isActive='" + isActive + "', runMode='" + runMode + "', categories='" + categories
                + "', version='" + version + "']";
    }

}
