package de.soco.software.simuspace.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.workflow.model.impl.ElementConfigImpl;
import de.soco.software.simuspace.workflow.model.impl.ElementInfo;
import de.soco.software.simuspace.workflow.model.impl.Field;

/**
 * The Interface ElementConfig. This interface contains properties of a work flow element
 */
@JsonDeserialize( as = ElementConfigImpl.class )
@JsonIgnoreProperties( ignoreUnknown = true )
public interface ElementConfig extends BaseElement {

    /**
     * Gets the allowed connection keys. These are connection keys that an element is allowed to connect with an element
     *
     * @return the allowed connections
     */
    List< String > getAllowedConnections();

    /**
     * Gets the allowed field keys. These are fields that an element is allowed to attach with an element
     *
     * @return the allowed field keys
     */

    List< String > getAllowedFieldKeys();

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    List< Category > getCategories();

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    @Override
    String getComments();

    /**
     * {@inheritDoc}
     *
     * @return the description
     */
    @Override
    String getDescription();

    /**
     * Gets the fields. Fields are properties that an element can have i.e. an element can have a text area, a drop down etc
     *
     * @return the fields
     */
    List< Field< ? > > getFields();

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    String getIcon();

    /**
     * Gets the ElementInfo .
     *
     * @return the ElementInfo
     */
    @Override
    ElementInfo getInfo();

    /**
     * Gets the key.
     *
     * @return the key
     */
    @Override
    String getKey();

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    String getName();

    /**
     * Gets the run mode.
     *
     * @return the run mode
     */
    String getRunMode();

    /**
     * Gets the shape.
     *
     * @return the shape
     */
    String getShape();

    /**
     * Checks if is active.
     *
     * @return true, if is active
     */
    boolean isActive();

    /**
     * Checks if is internal.
     *
     * @return true, if is internal
     */
    boolean isInternal();

    /**
     * Sets the active.
     *
     * @param isActive
     *         the new active
     */
    void setActive( boolean isActive );

    /**
     * Sets the allowed connections.
     *
     * @param allowedConnections
     *         the new allowed connections
     */
    void setAllowedConnections( List< String > allowedConnections );

    /**
     * Sets the allowedFieldKeys.
     *
     * @param allowedFieldKeys
     *         the new allowedFieldKeys
     */
    void setAllowedFieldKeys( List< String > allowedFieldKeys );

    /**
     * Sets the categories.
     *
     * @param categories
     *         the new categories
     */
    void setCategories( List< Category > categories );

    /**
     * {@inheritDoc}
     *
     * @param description
     *         the new description
     */
    @Override
    void setDescription( String description );

    /**
     * Sets the field list of the element
     *
     * @param fields
     *         the fields
     */
    void setFields( List< Field< ? > > fields );

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    void setIcon( String icon );

    /**
     * Sets the internal.
     *
     * @param isInternal
     *         the new internal
     */
    void setInternal( boolean isInternal );

    /**
     * Sets the key.
     *
     * @param key
     *         the new key
     */
    @Override
    void setKey( String key );

    /**
     * Sets the name.
     *
     * @param key
     *         the new name
     */
    @Override
    void setName( String key );

    /**
     * Sets the run mode.
     *
     * @param mode
     *         the new run mode
     */
    void setRunMode( String mode );

    /**
     * Sets the shape.
     *
     * @param shape
     *         the new shape
     */
    void setShape( String shape );

}
