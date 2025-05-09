package de.soco.software.simuspace.suscore.common.formitem;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.BindTo;
import de.soco.software.simuspace.suscore.common.ui.BindVisibility;
import de.soco.software.suscore.jsonschema.model.BindFrom;

/**
 * The type Ui form item.
 */
@Setter
@Getter
public abstract class UIFormItem implements FormItem {

    /**
     * The Label.
     */
    private String label;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Type.
     */
    private String type;

    /**
     * The Value.
     */
    private Object value;

    /**
     * The Help.
     */
    private String help;

    /**
     * The Place holder.
     */
    private String placeHolder;

    /**
     * The Readonly.
     */
    private boolean readonly;

    /**
     * The Rules.
     */
    private Map< String, Object > rules;

    /**
     * The Messages.
     */
    private Map< String, Object > messages;

    /**
     * The Multiple.
     */
    private boolean multiple;

    /**
     * The Selectable.
     */
    private String selectable;

    /**
     * The Type class.
     */
    private String[] typeClass;

    /**
     * The Duplicate.
     */
    private boolean duplicate = false;

    /**
     * The Update field.
     */
    private String updateField;

    /**
     * The Show.
     */
    private String show;

    /**
     * The External.
     */
    private String external;

    /**
     * The Extend tree.
     */
    private Map< String, String > extendTree;

    /**
     * The Sortable.
     */
    private boolean sortable = false;

    /**
     * The Order num.
     */
    private int orderNum;

    /**
     * The Custom attributes.
     */
    private List< Map< String, Object > > customAttributes;

    /**
     * The Tooltip.
     */
    private String tooltip;

    /**
     * The Trigger modified on init.
     */
    private Boolean triggerModifiedOnInit;

    /**
     * The Columns.
     */
    private List< String > columns;

    /**
     * The Bind visibility.
     */
    private BindVisibility bindVisibility;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Subtitle.
     */
    private String subtitle;

    /**
     * The Section.
     */
    private String section = "default";

    /**
     * The Required.
     */
    private boolean required;

    /**
     * The Bind from.
     */
    private BindFrom bindFrom;

    /**
     * The Bind to.
     */
    private BindTo bindTo;

    /**
     * Instantiates a new Ui form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param value
     *         the value
     */
    public UIFormItem( String label, String name, Object value ) {
        super();
        this.label = label;
        this.name = name;
        this.value = value;
    }

    /**
     * Instantiates a new Ui form item.
     */
    public UIFormItem() {
        super();
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }

    /**
     * Equals boolean.
     *
     * @param obj
     *         the obj
     *
     * @return the boolean
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        UIFormItem other = ( UIFormItem ) obj;
        if ( label == null ) {
            if ( other.label != null ) {
                return false;
            }
        } else if ( !label.equals( other.label ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( value == null ) {
            if ( other.value != null ) {
                return false;
            }
        } else if ( !value.equals( other.value ) ) {
            return false;
        }
        return true;
    }

    public void setBindFrom( String bindUrl ) {
        this.bindFrom = new BindFrom( bindUrl );
    }

    public void setBindFrom( BindFrom bindFrom ) {
        this.bindFrom = bindFrom;
    }

}