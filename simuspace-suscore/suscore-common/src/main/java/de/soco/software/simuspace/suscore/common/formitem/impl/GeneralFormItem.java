package de.soco.software.simuspace.suscore.common.formitem.impl;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The type General form item.
 */
public class GeneralFormItem extends UIFormItem {

    /**
     * Instantiates a new General form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param value
     *         the value
     */
    GeneralFormItem( String label, String name, Object value ) {
        setLabel( label );
        setName( name );
        setValue( value );
    }

    /**
     * Instantiates a new General form item.
     */
    GeneralFormItem() {
    }

}
