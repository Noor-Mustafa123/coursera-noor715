package de.soco.software.suscore.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class PolicyProcess.
 *
 * @author Noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class PolicyProcess {

    /**
     * The deletion time.
     */
    private String processTimeHours;

    /**
     * The status.
     */
    private String processName;

    /**
     * Gets the deletion time.
     *
     * @return the deletion time
     */
    public String getProcessTimeHours() {
        return processTimeHours;
    }

    /**
     * Sets the deletion time.
     *
     * @param processTimeHours
     *         the new deletion time
     */
    public void setProcessTimeHours( String processTimeHours ) {
        this.processTimeHours = processTimeHours;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Sets the status.
     *
     * @param processName
     *         the new status
     */
    public void setProcessName( String processName ) {
        this.processName = processName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ClassPojo [deletionTimeHours = " + processTimeHours + ", status = " + processName + "]";
    }

}