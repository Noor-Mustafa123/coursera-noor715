package de.soco.software.simuspace.suscore.common.formitem.impl;

import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;

/**
 * The section ui item for form.
 *
 * @author Nasir.Farooq
 */
public class SectionFormItem extends UIFormItem {

    /**
     * The title.
     */
    private String title;

    /**
     * The sub title.
     */
    private String subtitle;

    /**
     * The Mode.
     */
    private String mode;

    /**
     * Instantiates a new Section form item.
     */
    SectionFormItem() {

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
     * Sets the title.
     *
     * @param title
     *         the new title
     */
    public void setTitle( String title ) {
        this.title = title;
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
     * Sets the sub title.
     *
     * @param subTitle
     *         the sub title
     */
    public void setSubtitle( String subTitle ) {
        this.subtitle = subTitle;
    }

    /**
     * Gets the Mode.
     *
     * @return the Mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the Mode.
     *
     * @param mode
     *         the new Mode
     */
    public void setMode( String mode ) {
        this.mode = mode;
    }

}
