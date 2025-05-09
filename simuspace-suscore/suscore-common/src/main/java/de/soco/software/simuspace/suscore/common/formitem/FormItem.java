package de.soco.software.simuspace.suscore.common.formitem;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.ui.BindVisibility;
import de.soco.software.suscore.jsonschema.model.BindFrom;

/**
 * The interface Form item.
 */
public interface FormItem {

    /**
     * Gets label.
     *
     * @return the label
     */
    String getLabel();

    /**
     * Sets label.
     *
     * @param label
     *         the label
     */
    void setLabel( String label );

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    void setName( String name );

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Sets type.
     *
     * @param type
     *         the type
     */
    void setType( String type );

    /**
     * Gets value.
     *
     * @return the value
     */
    Object getValue();

    /**
     * Sets value.
     *
     * @param value
     *         the value
     */
    void setValue( Object value );

    /**
     * Gets help.
     *
     * @return the help
     */
    String getHelp();

    /**
     * Sets help.
     *
     * @param help
     *         the help
     */
    void setHelp( String help );

    /**
     * Gets place holder.
     *
     * @return the place holder
     */
    String getPlaceHolder();

    /**
     * Sets place holder.
     *
     * @param placeHolder
     *         the place holder
     */
    void setPlaceHolder( String placeHolder );

    /**
     * Is readonly boolean.
     *
     * @return the boolean
     */
    boolean isReadonly();

    /**
     * Sets readonly.
     *
     * @param readonly
     *         the readonly
     */
    void setReadonly( boolean readonly );

    /**
     * Gets rules.
     *
     * @return the rules
     */
    Map< String, Object > getRules();

    /**
     * Sets rules.
     *
     * @param rules
     *         the rules
     */
    void setRules( Map< String, Object > rules );

    /**
     * Gets messages.
     *
     * @return the messages
     */
    Map< String, Object > getMessages();

    /**
     * Sets messages.
     *
     * @param messages
     *         the messages
     */
    void setMessages( Map< String, Object > messages );

    /**
     * Is multiple boolean.
     *
     * @return the boolean
     */
    boolean isMultiple();

    /**
     * Sets multiple.
     *
     * @param multiple
     *         the multiple
     */
    void setMultiple( boolean multiple );

    /**
     * Get type class string [ ].
     *
     * @return the string [ ]
     */
    String[] getTypeClass();

    /**
     * Sets type class.
     *
     * @param typeClass
     *         the type class
     */
    void setTypeClass( String[] typeClass );

    /**
     * Gets external.
     *
     * @return the external
     */
    String getExternal();

    /**
     * Sets external.
     *
     * @param external
     *         the external
     */
    void setExternal( String external );

    /**
     * Gets selectable.
     *
     * @return the selectable
     */
    String getSelectable();

    /**
     * Sets selectable.
     *
     * @param selectable
     *         the selectable
     */
    void setSelectable( String selectable );

    /**
     * Is duplicate boolean.
     *
     * @return the boolean
     */
    boolean isDuplicate();

    /**
     * Sets duplicate.
     *
     * @param duplicate
     *         the duplicate
     */
    void setDuplicate( boolean duplicate );

    /**
     * Gets update field.
     *
     * @return the update field
     */
    String getUpdateField();

    /**
     * Sets update field.
     *
     * @param updateField
     *         the update field
     */
    void setUpdateField( String updateField );

    /**
     * Gets show.
     *
     * @return the show
     */
    String getShow();

    /**
     * Sets show.
     *
     * @param show
     *         the show
     */
    void setShow( String show );

    /**
     * Gets extend tree.
     *
     * @return the extend tree
     */
    Map< String, String > getExtendTree();

    /**
     * Sets extend tree.
     *
     * @param extendTree
     *         the extend tree
     */
    void setExtendTree( Map< String, String > extendTree );

    /**
     * Is sortable boolean.
     *
     * @return the boolean
     */
    boolean isSortable();

    /**
     * Sets sortable.
     *
     * @param sortable
     *         the sortable
     */
    void setSortable( boolean sortable );

    /**
     * Gets order num.
     *
     * @return the order num
     */
    int getOrderNum();

    /**
     * Sets order num.
     *
     * @param orderNum
     *         the order num
     */
    void setOrderNum( int orderNum );

    /**
     * Gets custom attributes.
     *
     * @return the custom attributes
     */
    List< Map< String, Object > > getCustomAttributes();

    /**
     * Sets custom attributes.
     *
     * @param customAttributes
     *         the custom attributes
     */
    void setCustomAttributes( List< Map< String, Object > > customAttributes );

    /**
     * Gets tooltip.
     *
     * @return the tooltip
     */
    String getTooltip();

    /**
     * Sets tooltip.
     *
     * @param tooltip
     *         the tooltip
     */
    void setTooltip( String tooltip );

    /**
     * Gets trigger modified on init.
     *
     * @return the trigger modified on init
     */
    Boolean getTriggerModifiedOnInit();

    /**
     * Sets trigger modified on init.
     *
     * @param triggerModifiedOnInit
     *         the trigger modified on init
     */
    void setTriggerModifiedOnInit( Boolean triggerModifiedOnInit );

    /**
     * Gets columns.
     *
     * @return the columns
     */
    List< String > getColumns();

    /**
     * Sets columns.
     *
     * @param columns
     *         the columns
     */
    void setColumns( List< String > columns );

    /**
     * Gets bind visibility.
     *
     * @return the bind visibility
     */
    BindVisibility getBindVisibility();

    /**
     * Sets bind visibility.
     *
     * @param bindVisibility
     *         the bind visibility
     */
    void setBindVisibility( BindVisibility bindVisibility );

    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Sets title.
     *
     * @param title
     *         the title
     */
    void setTitle( String title );

    /**
     * Gets subtitle.
     *
     * @return the subtitle
     */
    String getSubtitle();

    /**
     * Sets subtitle.
     *
     * @param subtitle
     *         the subtitle
     */
    void setSubtitle( String subtitle );

    /**
     * Is required boolean.
     *
     * @return the boolean
     */
    boolean isRequired();

    /**
     * Sets required.
     *
     * @param required
     *         the required
     */
    void setRequired( boolean required );

    /**
     * Gets bind from.
     *
     * @return the bind from
     */
    BindFrom getBindFrom();

    /**
     * Sets bind from.
     *
     * @param bindFrom
     *         the bind from
     */
    void setBindFrom( BindFrom bindFrom );

    /**
     * Gets bind to.
     *
     * @return the bind to
     */
    BindTo getBindTo();

    /**
     * Sets bind to.
     *
     * @param bindTo
     *         the bind to
     */
    void setBindTo( BindTo bindTo );

}
