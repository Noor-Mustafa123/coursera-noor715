package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class FieldAttributes.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class FieldAttributes {

    /**
     * The set.
     */
    private boolean set;

    /**
     * The manage.
     */
    private boolean manage;

    /**
     * Instantiates a new field rules.
     */
    public FieldAttributes() {
        super();
    }

    /**
     * Instantiates a new field rules.
     *
     * @param required
     *         the required
     */
    public FieldAttributes( boolean set, boolean manage ) {
        this();
        this.set = set;
        this.manage = manage;
    }

    /**
     * Checks if is sets the.
     *
     * @return true, if is sets the
     */
    public boolean isSet() {
        return set;
    }

    /**
     * Sets the sets the.
     *
     * @param set
     *         the new sets the
     */
    public void setSet( boolean set ) {
        this.set = set;
    }

    /**
     * Checks if is manage.
     *
     * @return true, if is manage
     */
    public boolean isManage() {
        return manage;
    }

    /**
     * Sets the manage.
     *
     * @param manage
     *         the new manage
     */
    public void setManage( boolean manage ) {
        this.manage = manage;
    }

}
