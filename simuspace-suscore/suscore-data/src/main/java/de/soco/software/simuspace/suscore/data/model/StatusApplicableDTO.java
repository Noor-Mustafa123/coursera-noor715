package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

/**
 * This Class represent the validation Detail of any object to be ued for status Change
 *
 * @author Nosheen.Sharif
 */
public class StatusApplicableDTO {

    /**
     * list of status can apply to any object.
     */
    private List< String > validStatusList;

    /**
     * The default status of any object .
     */
    private String defaultStatus;

    /**
     * Gets the default status.
     *
     * @return the default status
     */
    public String getDefaultStatus() {
        return defaultStatus;
    }

    /**
     * Gets the valid status list.
     *
     * @return the valid status list
     */
    public List< String > getValidStatusList() {
        return validStatusList;
    }

    /**
     * Sets the default status.
     *
     * @param defaultStatus
     *         the new default status
     */
    public void setDefaultStatus( String defaultStatus ) {
        this.defaultStatus = defaultStatus;
    }

    /**
     * Sets the valid status list.
     *
     * @param validStatusList
     *         the new valid status list
     */
    public void setValidStatusList( List< String > validStatusList ) {
        this.validStatusList = validStatusList;
    }

}
