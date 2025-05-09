package de.soco.software.simuspace.suscore.local.daemon.controller;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;

/**
 * The Interface contains the expose able functions through APIs.
 *
 * @author Nasir.Farooq
 */
public interface WorkflowDaemonController {

    /**
     * Run job.
     *
     * @param authToken
     *         the auth token
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response entity
     */
    public abstract ResponseEntity< SusResponseDTO > runJob( String authToken, String jobParametersString );

    /**
     * Gets the file jobs.
     *
     * @return the file jobs
     */
    public abstract ResponseEntity< SusResponseDTO > getfileJobs();

    /**
     * Stop job.
     *
     * @param jobId
     *         the job id
     *
     * @return the response entity
     */
    public abstract ResponseEntity< SusResponseDTO > stopJob( String authToken, String jobId );

    /**
     * Gets the list of running jobs .
     *
     * @return the list of running jobs
     */
    public abstract ResponseEntity< SusResponseDTO > getListOfRunningJobs();

    /**
     * Checks if the daemon is up on that port or not.
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > isUp();

    /**
     * Checks to stop deamon
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > shutdown( Integer pid );

    /**
     * Import workflow form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the response entity
     */
    public ResponseEntity< SusResponseDTO > importWorkflowForm( String parentId );

    /**
     * Import workflow.
     *
     * @param authToken
     *         the auth token
     * @param objectJson
     *         the object json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > importWorkflow( String authToken, String objectJson );

    ResponseEntity< SusResponseDTO > addPID( Integer pid );

}