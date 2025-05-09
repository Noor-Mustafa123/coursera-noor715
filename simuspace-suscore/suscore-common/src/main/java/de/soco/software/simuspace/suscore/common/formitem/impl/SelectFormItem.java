package de.soco.software.simuspace.suscore.common.formitem.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * To Draw Single/Multi Selection DropDown On Ui form.
 */
@Setter
@Getter
public class SelectFormItem extends UIFormItem {

    /**
     * The Options.
     */
    private List< SelectOptionsUI > options;

    /**
     * The Picker.
     */
    private Map< String, Object > picker = Collections.emptyMap();

    /**
     * The Can be empty.
     */
    private boolean canBeEmpty = false;

    /**
     * The Convert.
     */
    private String convert;

    /**
     * Instantiates a new Select form item.
     */
    SelectFormItem() {
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
        int result = super.hashCode();
        result = prime * result + ( ( options == null ) ? 0 : options.hashCode() );
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
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        SelectFormItem other = ( SelectFormItem ) obj;
        if ( options == null ) {
            if ( other.options != null ) {
                return false;
            }
        } else if ( !options.equals( other.options ) ) {
            return false;
        }
        return true;
    }

}
