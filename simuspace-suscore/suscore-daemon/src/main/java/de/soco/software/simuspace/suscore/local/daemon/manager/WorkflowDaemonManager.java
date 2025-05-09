/*
 *
 */

package de.soco.software.simuspace.suscore.local.daemon.manager;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;

/**
 * The Interface of the business class responsible for communications with workflow engine and process accordingly.
 *
 * @author Nasir.Farooq
 */
public interface WorkflowDaemonManager {

    /**
     * This method will be responsible to execute Job.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @throws SusException
     *         the sus exception
     */
    void runjob( JobParameters jobParameters );

    /**
     * Run scheme.
     *
     * @param jobParameters
     *         the job parameters
     */
    void runScheme( JobParameters jobParameters );

    /**
     * Stop job.
     *
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     */
    boolean stopJob( String authToken, String jobId, Job jobDto );

    /**
     * Stop all jobs. on deamon close ,stop all file and sus jobs
     *
     * @return true, if successful
     */
    boolean stopAllJobs();

    /**
     * Gets the running jobs List. will be called on closing of workflow Designer to prompt running jobs count
     *
     * @return the running jobs
     */
    List< Job > getRunningJobs();

    /**
     * Checks if is local job running.
     *
     * @return true, if is local job running
     */
    boolean isLocalJobRunning();

    /**
     * Gets the all jobs.
     *
     * @return the all jobs
     */
    List< Job > getAllJobs();

    /**
     * Gets the file based jobs List.
     *
     * @return the file base jobs
     */
    List< Job > getFileBaseJobs();

    /**
     * Creates the workflow form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the list
     */
    UIForm createWorkflowForm( String parentId );

    /**
     * Import workflow.
     *
     * @param authToken
     *         the auth token
     * @param objectJson
     *         the object json
     *
     * @return true, if successful
     */
    boolean importWorkflow( String authToken, String objectJson );

    /**
     * Gets the dynamic property.
     *
     * @param plugin
     *         the plugin
     * @param jobParametersString
     *         the job parameters string
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     *
     * @return the dynamic property
     */
    List< Map< String, Object > > getDynamicFields( String plugin, String jobParametersString, RestAPI server,
            RequestHeaders requestHeaders );

}