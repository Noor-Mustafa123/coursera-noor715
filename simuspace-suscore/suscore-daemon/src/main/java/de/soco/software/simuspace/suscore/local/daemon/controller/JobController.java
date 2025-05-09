package de.soco.software.simuspace.suscore.local.daemon.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;

/**
 * The Interface contains the expose able functions related to sus jobs through APIs.
 *
 * @author Nosheen.sharif
 */
public interface JobController {

    /**
     * Gets the sus job table UI.
     *
     * @param authToken
     *         the auth token
     *
     * @return the sus job table UI
     */
    ResponseEntity< SusResponseDTO > getSusJobTableUI( String authToken );

    /**
     * Gets the file job table UI.
     *
     * @return the file job table UI
     */
    ResponseEntity< SusResponseDTO > getFileJobTableUI( String authToken );

    /**
     * Gets the sus jobs list.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the sus jobs list
     */
    ResponseEntity< SusResponseDTO > getSusJobsList( String authToken, String objectFilterJson );

    /**
     * Gets the file jobs list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the file jobs list
     */
    ResponseEntity< SusResponseDTO > getFileJobsList( String objectFilterJson );

    /**
     * Gets the sus jobs context menu.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the sus jobs context menu
     */
    ResponseEntity< SusResponseDTO > getSusJobsContextMenu( String authToken, String objectFilterJson );

    /**
     * Gets the local jobs context menu.
     *
     * @param authToken
     *         the auth token
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the local jobs context menu
     */
    ResponseEntity< SusResponseDTO > getLocalJobsContextMenu( String authToken, String objectFilterJson );

    /**
     * Open job dir.
     *
     * @param jobId
     *         the job id
     * @param authToken
     *         the auth token
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > getJobById( String jobId, String authToken );

    /**
     * Gets the sus jobs view.
     *
     * @param authToken
     *         the auth token
     *
     * @return the sus jobs view
     */
    ResponseEntity< SusResponseDTO > getSusJobsView( String authToken );

    /**
     * Save sus jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > saveSusJobsView( String authToken, String viewJson );

    /**
     * Sets the sus jobs view as default.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > setSusJobsViewAsDefault( String authToken, String viewId );

    /**
     * Stop job.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > stopJob( String authToken, String jobId );

    /**
     * Stop job.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > pauseJob( String authToken, String jobId );

    /**
     * Stop job.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > resumeJob( String authToken, String jobId );

    /**
     * Update sus jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > updateSusJobsView( String authToken, String viewId, String viewJson );

    /**
     * Delete sus jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > deleteSusJobsView( String authToken, String viewId );

    /**
     * Re run job.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the response entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    ResponseEntity< SusResponseDTO > reRunJob( String authToken, String jobId ) throws IOException;

    /**
     * Discard job.
     *
     * @param authToken
     *         the auth token
     * @param jobId
     *         the job id
     *
     * @return the response entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws InterruptedException
     */
    ResponseEntity< SusResponseDTO > discardJob( String authToken, String jobId ) throws IOException, InterruptedException;

    /**
     * Gets the file jobs view.
     *
     * @param authToken
     *         the auth token
     *
     * @return the file jobs view
     */
    ResponseEntity< SusResponseDTO > getFileJobsView( String authToken );

    /**
     * Save file jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > saveFileJobsView( String authToken, String viewJson );

    /**
     * Sets the file jobs view as default.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > setFileJobsViewAsDefault( String authToken, String viewId );

    /**
     * Update file jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     * @param viewJson
     *         the view json
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > updateFileJobsView( String authToken, String viewId, String viewJson );

    /**
     * Delete file jobs view.
     *
     * @param authToken
     *         the auth token
     * @param viewId
     *         the view id
     *
     * @return the response entity
     */
    ResponseEntity< SusResponseDTO > deleteFileJobsView( String authToken, String viewId );

}