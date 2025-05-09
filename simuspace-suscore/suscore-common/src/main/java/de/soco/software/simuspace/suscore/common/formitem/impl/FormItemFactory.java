package de.soco.software.simuspace.suscore.common.formitem.impl;

import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The type Form item factory.
 */
public class FormItemFactory {

    /**
     * Instantiates a new Form item factory.
     */
    public FormItemFactory() {

    }

    /**
     * Create form item by type ui form item.
     *
     * @param type
     *         the type
     *
     * @return the ui form item
     */
    public UIFormItem createFormItemByType( FormItemType type ) {
        return switch ( type ) {
            case SELECT -> new SelectFormItem();
            case IMAGE -> new ImageFormItem();
            case SECTION -> new SectionFormItem();
            case INPUT_TABLE -> new InputTableFormItem();
            case TABLE -> new TableFormItem();
            case BUTTON -> new ButtonFormItem();
            case CODE -> new CodeFormItem();
            default -> new GeneralFormItem();
        };
    }

    /**
     * Create general form item ui form item.
     *
     * @return the ui form item
     */
    public UIFormItem createGeneralFormItem() {
        return new GeneralFormItem();
    }

    /**
     * Create general form item ui form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param value
     *         the value
     *
     * @return the ui form item
     */
    public UIFormItem createGeneralFormItem( String label, String name, Object value ) {
        return new GeneralFormItem( label, name, value );
    }

}
