package de.soco.software.simuspace.workflow.model.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldModes;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;

/**
 * A work flow element could have many fields i.e, textarea,dropdown etc.
 *
 * @param <T>
 *         the generic type
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Field< T > extends BaseFieldImpl implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The help for a field.
     */
    private String help;

    /**
     * The label of a field.
     */
    private String label;

    /**
     * The multiple. if dropdown can have multiple selected values.
     */
    private boolean multiple;

    /**
     * The list of options in a select field.
     */
    private List< Object > options;

    /**
     * The rule object for field to check the object is required or not.
     */
    private FieldRules rules;

    /**
     * The attributes.
     */
    private FieldAttributes attributes;

    /**
     * The sortable.
     */
    private boolean sortable;

    /**
     * The custom view.
     */
    private boolean customView;

    /**
     * The bind visibility.
     */
    private List< BindVisibility > bindVisibility;

    /**
     * The sub title of a field.
     */
    private String subtitle;

    /**
     * The title of section.
     */
    private String title;

    /**
     * The external.
     */
    private String external;

    /**
     * The bind simdef.
     */
    private String bindSimdef;

    /**
     * The bind variant.
     */
    private String bindVariant;

    /**
     * The bind to.
     */
    private BindTo bindTo;

    /**
     * The ajax.
     */
    private String ajax;

    /**
     * The value of a field. it can be integer, string, float etc
     */
    private T value;

    /**
     * The variable mode.
     */
    @JsonProperty( "variable-mode" )
    private boolean variableMode;

    /**
     * The sub fields.
     */
    private Map< String, Field< T > > subFields;

    /**
     * The template type.
     */
    private String templateType;

    /**
     * The change on run.
     */
    private boolean changeOnRun;

    /**
     * Instantiates a new field impl.
     */
    public Field() {
        super();
    }

    /**
     * Instantiates a new field impl.
     *
     * @param mode
     *         the key
     * @param name
     *         the name
     */
    public Field( String mode, String name ) {
        super( mode, name );
    }

    /**
     * Instantiates a new field.
     *
     * @param help
     *         the help
     * @param label
     *         the label
     * @param subTitle
     *         the sub title
     * @param title
     *         the title
     * @param options
     *         the options
     * @param rules
     *         the rules
     * @param value
     *         the value
     * @param multiple
     *         the multiple
     * @param bindVisibility
     *         the bind visibility
     */
    public Field( String help, String label, String subtitle, String title, List< Object > options, FieldRules rules, T value,
            boolean multiple, List< BindVisibility > bindVisibility ) {
        this();
        this.help = help;
        this.label = label;
        this.subtitle = subtitle;
        this.title = title;
        this.options = options;
        this.rules = rules;
        this.value = value;
        this.multiple = multiple;
        this.bindVisibility = bindVisibility;
    }

    /**
     * Gets the help.
     *
     * @return the help
     */
    public String getHelp() {
        return help;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the rules.
     *
     * @return the rules
     */
    public FieldRules getRules() {
        return rules;
    }

    /**
     * Gets the attributes.
     *
     * @return the attributes
     */
    public FieldAttributes getAttributes() {
        return attributes;
    }

    /**
     * Checks if is sortable.
     *
     * @return true, if is sortable
     */
    public boolean isSortable() {
        return sortable;
    }

    /**
     * Checks if is custom view.
     *
     * @return true, if is custom view
     */
    public boolean isCustomView() {
        return customView;
    }

    /**
     * Gets the sub fields.
     *
     * @return the sub fields
     */
    public Map< String, Field< T > > getSubFields() {
        return subFields;
    }

    /**
     * Gets the sub title.
     *
     * @return the sub title
     */
    public String getSubtitle() {
        return subtitle;
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
     * Gets the external.
     *
     * @return the external
     */
    public String getExternal() {
        return external;
    }

    /**
     * Sets the external.
     *
     * @param external
     *         the new external
     */
    public void setExternal( String external ) {
        this.external = external;
    }

    /**
     * Gets the bind to.
     *
     * @return the bind to
     */
    public BindTo getBindTo() {
        return bindTo;
    }

    /**
     * Sets the bind to.
     *
     * @param bindTo
     *         the new bind to
     */
    public void setBindTo( BindTo bindTo ) {
        this.bindTo = bindTo;
    }

    /**
     * Gets the bind simdef.
     *
     * @return the bind simdef
     */
    public String getBindSimdef() {
        return bindSimdef;
    }

    /**
     * Sets the bind simdef.
     *
     * @param bindSimdef
     *         the new bind simdef
     */
    public void setBindSimdef( String bindSimdef ) {
        this.bindSimdef = bindSimdef;
    }

    /**
     * Gets the bind variant.
     *
     * @return the bind variant
     */
    public String getBindVariant() {
        return bindVariant;
    }

    /**
     * Sets the bind variant.
     *
     * @param bindVariant
     *         the new bind variant
     */
    public void setBindVariant( String bindVariant ) {
        this.bindVariant = bindVariant;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Checks if is boolean value.
     *
     * @return true, if is boolean value
     */
    private boolean isBooleanValue() {
        return !Arrays.asList( Boolean.TRUE.toString(), Boolean.FALSE.toString() ).contains( getValue().toString() );
    }

    /**
     * Checks if is multiple.
     *
     * @return true, if is multiple
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * Checks if is variable mode.
     *
     * @return true, if is variable mode
     */
    public boolean isVariableMode() {
        return variableMode;
    }

    /**
     * Sets the help.
     *
     * @param help
     *         the new help
     */
    public void setHelp( String help ) {
        this.help = help;
    }

    /**
     * Sets the label.
     *
     * @param label
     *         the new label
     */
    public void setLabel( String label ) {
        this.label = label;
    }

    /**
     * Sets the multiple.
     *
     * @param multiple
     *         the new multiple
     */
    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    /**
     * Sets the rules.
     *
     * @param rules
     *         the new rules
     */
    public void setRules( FieldRules rules ) {
        this.rules = rules;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes
     *         the new attributes
     */
    public void setAttributes( FieldAttributes attributes ) {
        this.attributes = attributes;
    }

    /**
     * Sets the sortable.
     *
     * @param sortable
     *         the new sortable
     */
    public void setSortable( boolean sortable ) {
        this.sortable = sortable;
    }

    /**
     * Sets the custom view.
     *
     * @param customView
     *         the new custom view
     */
    public void setCustomView( boolean customView ) {
        this.customView = customView;
    }

    /**
     * Sets the sub fields.
     *
     * @param subFields
     *         the sub fields
     */
    public void setSubFields( Map< String, Field< T > > subFields ) {
        this.subFields = subFields;
    }

    /**
     * Sets the sub title.
     *
     * @param subtitle
     *         the new sub title
     */
    public void setSubtitle( String subtitle ) {
        this.subtitle = subtitle;
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
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( T value ) {
        this.value = value;
    }

    /**
     * Sets the variable mode.
     *
     * @param variableMode
     *         the new variable mode
     */
    public void setVariableMode( boolean variableMode ) {
        this.variableMode = variableMode;
    }

    /**
     * Gets the template type.
     *
     * @return the template type
     */
    public String getTemplateType() {
        return templateType;
    }

    /**
     * Sets the template type.
     *
     * @param templateType
     *         the new template type
     */
    public void setTemplateType( String templateType ) {
        this.templateType = templateType;
    }

    /**
     * Checks if is change on run.
     *
     * @return true, if is change on run
     */
    public boolean isChangeOnRun() {
        return changeOnRun;
    }

    /**
     * Sets the tchange on run.
     *
     * @param changeOnRun
     *         the new change on run
     */
    public void setChangeOnRun( boolean changeOnRun ) {
        this.changeOnRun = changeOnRun;
    }

    /**
     * Gets the bind visibility.
     *
     * @return the bind visibility
     */
    public List< BindVisibility > getBindVisibility() {
        return bindVisibility;
    }

    /**
     * Sets the bind visibility.
     *
     * @param bindVisibility
     *         the new bind visibility
     */
    public void setBindVisibility( List< BindVisibility > bindVisibility ) {
        this.bindVisibility = bindVisibility;
    }

    /**
     * Gets the ajax.
     *
     * @return the ajax
     */
    public String getAjax() {
        return ajax;
    }

    /**
     * Sets the ajax.
     *
     * @param ajax
     *         the new ajax
     */
    public void setAjax( String ajax ) {
        this.ajax = ajax;
    }

    /**
     * Gets the options.
     *
     * @return the options
     */
    public List< Object > getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *         the new options
     */
    public void setOptions( List< Object > options ) {
        this.options = options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Field{" + "help='" + help + '\'' + ", label='" + label + '\'' + ", multiple=" + multiple + ", options=" + options
                + ", rules=" + rules + ", attributes=" + attributes + ", sortable=" + sortable + ", customView=" + customView
                + ", bindVisibility=" + bindVisibility + ", subtitle='" + subtitle + '\'' + ", title='" + title + '\'' + ", external='"
                + external + '\'' + ", bindSimdef='" + bindSimdef + '\'' + ", bindVariant='" + bindVariant + '\'' + ", bindTo=" + bindTo
                + ", ajax='" + ajax + '\'' + ", value=" + value + ", variableMode=" + variableMode + ", subFields=" + subFields
                + ", templateType='" + templateType + '\'' + ", changeOnRun=" + changeOnRun + '}';
    }

    /**
     * This function gets the fields of an element and validate them. i.e, validating an input file path.
     *
     * @return Notification
     */
    public Notification validateField() {
        final Notification notif = new Notification();
        if ( !isVariableMode() && getMode().contains( FieldModes.USER.getType() ) && ( getValue() != null ) ) {
            if ( getType().contains( FieldTypes.SELECTION.getType() ) ) {
                if ( null != getOptions() && !( getOptions().isEmpty() ) && !( getOptions() instanceof ArrayList< ? > ) ) {
                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, getName() ) ) );
                }
            } else if ( getType().contains( FieldTypes.INTEGER.getType() ) || getType().contains( FieldTypes.FLOAT.getType() ) ) {
                try {
                    if ( getRules().isRequired() && getValue().toString().isEmpty() ) {
                        notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.REQUIRED_FIELD_IS_MISSING, getName() ) ) );
                    } else if ( getRules().isRequired() || !getValue().toString().isEmpty() ) {
                        if ( getType().contains( FieldTypes.INTEGER.getType() ) ) {
                            Double.parseDouble( getValue().toString() );
                        } else {
                            Float.parseFloat( getValue().toString() );
                        }

                    }
                } catch ( final NumberFormatException ex ) {
                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, getName() ) ) );
                }
            } else if ( getType().contains( FieldTypes.BOOLEAN.getType() ) ) {
                if ( isBooleanValue() ) {
                    notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, getName() ) ) );
                }

            } else if ( ( getType().contains( FieldTypes.TEXT.getType() ) || getType().contains( FieldTypes.TEXTAREA.getType() ) )
                    && !( getValue().toString().isEmpty() ) && !( getValue() instanceof String ) ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_VALUE_FOR_FIELD, getName() ) ) );
            }
        }

        return notif;
    }

}