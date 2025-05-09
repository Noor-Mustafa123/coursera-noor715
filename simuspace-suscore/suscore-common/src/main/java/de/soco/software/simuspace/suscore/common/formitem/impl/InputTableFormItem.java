package de.soco.software.simuspace.suscore.common.formitem.impl;

import java.util.List;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The type Input table form item.
 */
public class InputTableFormItem extends UIFormItem {

    /**
     * The Options.
     */
    private List< OptionField > fields;

    /**
     * Instantiates a new Input table form item.
     */
    InputTableFormItem() {

    }

    /**
     * Gets fields.
     *
     * @return the fields
     */
    public List< OptionField > getFields() {
        return fields;
    }

    /**
     * Sets fields.
     *
     * @param fields
     *         the fields
     */
    public void setFields( List< OptionField > fields ) {
        this.fields = fields;
    }

    /**
     * The type OptionField.
     */
    public record OptionField( String label, String columnType, List< String > options, String multiple ) {

    }

}