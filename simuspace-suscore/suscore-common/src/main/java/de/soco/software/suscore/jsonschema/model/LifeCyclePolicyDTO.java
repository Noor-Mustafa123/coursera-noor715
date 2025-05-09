package de.soco.software.suscore.jsonschema.model;

import java.util.List;

/**
 * The Class LifeCyclePolicyDTO.
 *
 * @author Noman arshad
 */
public class LifeCyclePolicyDTO {

    /**
     * The life cycle policy apply.
     */
    private String lifeCyclePolicyApply;

    /**
     * The scheduler time.
     */
    private String schedulerTimeMinutes;

    /**
     * The process list.
     */
    private List< PolicyProcess > policyProcesses;

    /**
     * Gets the life cycle policy apply.
     *
     * @return the life cycle policy apply
     */
    public String getLifeCyclePolicyApply() {
        return lifeCyclePolicyApply;
    }

    /**
     * Sets the life cycle policy apply.
     *
     * @param lifeCyclePolicyApply
     *         the new life cycle policy apply
     */
    public void setLifeCyclePolicyApply( String lifeCyclePolicyApply ) {
        this.lifeCyclePolicyApply = lifeCyclePolicyApply;
    }

    /**
     * Gets the scheduler time.
     *
     * @return the scheduler time
     */
    public String getSchedulerTimeMinutes() {
        return schedulerTimeMinutes;
    }

    /**
     * Sets the scheduler time.
     *
     * @param schedulerTime
     *         the new scheduler time
     */
    public void setSchedulerTimeMinutes( String schedulerTime ) {
        this.schedulerTimeMinutes = schedulerTime;
    }

    /**
     * Gets the deletion process.
     *
     * @return the deletion process
     */
    public List< PolicyProcess > getPolicyProcess() {
        return policyProcesses;
    }

    /**
     * Sets the deletion process.
     *
     * @param policyProcesses
     *         the new deletion process
     */
    public void setPolicyProcess( List< PolicyProcess > policyProcesses ) {
        this.policyProcesses = policyProcesses;
    }

}
