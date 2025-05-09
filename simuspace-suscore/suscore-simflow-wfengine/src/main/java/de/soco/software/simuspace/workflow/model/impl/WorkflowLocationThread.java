package de.soco.software.simuspace.workflow.model.impl;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;

/**
 * The Class WorkflowLocationThread.
 */
public class WorkflowLocationThread extends UserThread {

    /**
     * The job parameters location model.
     */
    protected JobParametersLocationModel jobParametersLocationModel;

    /**
     * Gets the job parameters location model.
     *
     * @return the job parameters location model
     */
    public JobParametersLocationModel getJobParametersLocationModel() {
        return jobParametersLocationModel;
    }

    /**
     * Sets the job parameters location model.
     *
     * @param jobParametersLocationModel
     *         the new job parameters location model
     */
    public void setJobParametersLocationModel( JobParametersLocationModel jobParametersLocationModel ) {
        this.jobParametersLocationModel = jobParametersLocationModel;
    }

}
