package de.soco.software.simuspace.suscore.common.formitem.impl;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The type Code form item.
 */
public class CodeFormItem extends UIFormItem {

    /**
     * Instantiates a new Code form item.
     */
    CodeFormItem() {

    }

    /**
     * The Syntax.
     */
    private String syntax;

    /**
     * Gets syntax.
     *
     * @return the syntax
     */
    public String getSyntax() {
        return syntax;
    }

    /**
     * Sets syntax.
     *
     * @param syntax
     *         the syntax
     */
    public void setSyntax( String syntax ) {
        this.syntax = syntax;
    }

}
