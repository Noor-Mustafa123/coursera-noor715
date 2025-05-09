package de.soco.software.simuspace.workflow.dto;

import java.util.List;

/**
 * This Class represent the validation Detail of any object to be ued for status Change
 *
 * @author Nosheen.Sharif
 */
public class StatusObjectDetail {

    /**
     * list of status can apply to any object.
     */
    private List< Integer > validStatusList;

    /**
     * The default status of any object .
     */
    private Integer defaultStatus;

    /**
     * The object type for status change.
     */
    private String type;

    /**
     * Gets the default status.
     *
     * @return the default status
     */
    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the valid status list.
     *
     * @return the valid status list
     */
    public List< Integer > getValidStatusList() {
        return validStatusList;
    }

    /**
     * Sets the default status.
     *
     * @param defaultStatus
     *         the new default status
     */
    public void setDefaultStatus( Integer defaultStatus ) {
        this.defaultStatus = defaultStatus;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Sets the valid status list.
     *
     * @param validStatusList
     *         the new valid status list
     */
    public void setValidStatusList( List< Integer > validStatusList ) {
        this.validStatusList = validStatusList;
    }

}
