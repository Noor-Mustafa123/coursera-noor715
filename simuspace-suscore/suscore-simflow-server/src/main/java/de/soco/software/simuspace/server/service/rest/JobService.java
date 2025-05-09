/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.server.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.server.constant.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * This Interface is responsible for all the operation Of Jobs for workflows.
 *
 * @author Nosheen.Sharif
 */
@WebService
@Consumes( { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface JobService {

    /**
     * Gets the filtered jobs list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered jobs list
     */
    @POST
    @Path( "/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredJobsList( String objectFilterJson );

    /**
     * List job table UI.
     *
     * @return the response
     */
    @GET
    @Path( "/system/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listSystemJobTableUI();

    /**
     * Gets filtered system jobs list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered system jobs list
     */
    @POST
    @Path( "/system/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredSystemJobsList( String objectFilterJson );

    /**
     * Gets the filtered child jobs list.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered child jobs list
     */
    @POST
    @Path( "{id}/childs/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredChildJobsList( @PathParam( "id" ) UUID id, String objectFilterJson );

    /**
     * List job table UI.
     *
     * @return the response
     */
    @GET
    @Path( "/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listJobTableUI();

    /**
     * List job workflow table ui response.
     *
     * @return the response
     */
    @GET
    @Path( "workflow/{workflowId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listJobWorkflowTableUI();

    /**
     * Gets filtered workflow related jobs list.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered workflow related jobs list
     */
    @POST
    @Path( "workflow/{workflowId}/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredWorkflowRelatedJobsList( @PathParam( "workflowId" ) String workflowId, String objectFilterJson );

    /**
     * List child job table UI.
     *
     * @return the response
     */
    @GET
    @Path( "{id}/childs/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listChildJobTableUI();

    /**
     * Checks if is host enabled.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "isHost/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response isHost( @PathParam( "id" ) UUID id );

    /**
     * Gets the tabs view job UI.
     *
     * @param id
     *         the job id
     *
     * @return the tabs view job UI
     */
    @GET
    @Path( "/{id}/tabs/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTabsViewJobUI( @PathParam( "id" ) String id );

    /**
     * Gets the single job properties UI.
     *
     * @param id
     *         the id
     *
     * @return the single job properties UI
     */
    @GET
    @Path( "/{id}/properties/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSingleJobPropertiesUI( @PathParam( "id" ) String id );

    /**
     * Gets the job log UI.
     *
     * @param id
     *         the id
     * @param treeId
     *         the tree id
     *
     * @return the job log UI
     */
    @GET
    @Path( "/{id}/log/tree/{treeId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobLogUI( @PathParam( "id" ) String id, @PathParam( "treeId" ) String treeId );

    /**
     * Gets the job data created job UI.
     *
     * @param id
     *         the id
     *
     * @return the job data created job UI
     */
    @GET
    @Path( "/{id}/data/created/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobDataCreatedJobUI( @PathParam( "id" ) String id );

    /**
     * Gets the job permission table UI.
     *
     * @param id
     *         the id
     *
     * @return the job permission table UI
     */
    @GET
    @Path( "/{id}/permissions/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobPermissionTableUI( @PathParam( "id" ) String id );

    /**
     * Show permitted users and groups for job.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/permissions/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response showPermittedUsersAndGroupsForJob( @PathParam( "id" ) UUID id, String objectFilterJson );

    /**
     * Gets the job.
     *
     * @param id
     *         the id
     *
     * @return the job
     */
    @GET
    @Path( "/{id}/properties/data" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJob( @PathParam( "id" ) UUID id );

    /**
     * Gets the filtered job logs list.
     *
     * @param id
     *         the id
     * @param treeId
     *         the tree id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered job logs list
     */
    @POST
    @Path( "/{id}/log/tree/{treeId}/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredJobLogsList( @PathParam( "id" ) String id, @PathParam( "treeId" ) String treeId, String objectFilterJson );

    /**
     * Gets the filtered job data created objects list.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered job data created objects list
     */
    @POST
    @Path( "/{id}/data/created/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredJobDataCreatedObjectsList( @PathParam( "id" ) String id, String objectFilterJson );

    /**
     * Gets the job data created objects list.
     *
     * @param id
     *         the id
     *
     * @return the job data created objects list
     */
    @GET
    @Path( "/{id}/created/object/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getJobDataCreatedObjectsList( @PathParam( "id" ) String id );

    /**
     * Gets the job data created objects list.
     *
     * @param id
     *         the id
     *
     * @return the job data created objects list
     */
    @GET
    @Path( "/{id}/created/object/ids/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getJobDataCreatedObjectsIdsList( @PathParam( "id" ) String id );

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveView( String objectJson );

    /**
     * Gets the all views.
     *
     * @return the all views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllViews();

    /**
     * Gets the all views.
     *
     * @return the all views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_SYSTEM_JOB_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemJobViews();

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_SYSTEM_JOB_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveSystemJobView( String objectJson );

    /**
     * Update su S jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_SYSTEM_JOB_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateSystemJobView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_SYSTEM_JOB_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteSystemJobView( @PathParam( "viewId" ) String viewId );

    /**
     * Sets sus jobs view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the sus jobs view as default
     */
    @GET
    @Path( ConstantsViewEndPoints.SYSTEM_JOB_UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setSystemJobsViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Permit permission to job.
     *
     * @param permissionJson
     *         the permission json
     * @param objectId
     *         the object id
     * @param sidId
     *         the sid id
     *
     * @return the response
     */
    @PUT
    @Path( "/{jobId}/permissions/{sidId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response permitPermissionToJob( String permissionJson, @PathParam( "jobId" ) UUID objectId, @PathParam( "sidId" ) UUID sidId );

    /**
     * Gets the jobs context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the jobs context
     */
    @POST
    @Path( "/context" )
    Response getJobsContext( String filterJson );

    /**
     * Gets the jobs context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the jobs context
     */
    @POST
    @Path( "/sus/jobs/context" )
    Response getSusJobsContext( String filterJson );

    /**
     * Gets the jobs context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the jobs context
     */
    @POST
    @Path( "/{id}/data/created/context" )
    Response getJobsDataCreatedContext( String filterJson );

    /**
     * Check job childern.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/{jobId}/child" )
    Response checkJobChildern( @PathParam( "jobId" ) UUID jobId );

    /**
     * Check job childern.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/{jobId}/childId" )
    Response getChildJobIDsByMasterJobID( @PathParam( "jobId" ) String jobId );

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SUS_SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveSuSJobsView( String objectJson );

    /**
     * Update su S jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsViewEndPoints.SUS_DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateSuSJobsView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Update su s job view response.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "sus/jobs/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateSuSJobView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets the su S jobs view.
     *
     * @return the su S jobs view
     */
    @GET
    @Path( "/sus/jobs/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSuSJobsView();

    /**
     * Update su s jobs view response.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/sus/jobs/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateSuSJobsView( String objectJson );

    /**
     * Update su S jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteView( @PathParam( "viewId" ) String viewId );

    /**
     * Delete sus jobs view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.SUS_DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteSusJobsView( @PathParam( "viewId" ) String viewId );

    /**
     * Gets the su S jobs views.
     *
     * @return the su S jobs views
     */
    @GET
    @Path( ConstantsViewEndPoints.SUS_SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSuSJobsViews();

    /**
     * Sets the sus jobs view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.SUS_UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setSusJobsViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Sets the sus jobs view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Gets the my SUS jobs list.
     *
     * @param objectJson
     *         the object json
     *
     * @return the my SUS jobs list
     */
    @POST
    @Path( "/list/myjobs" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getMySusJobsList( String objectJson );

    /**
     * Gets the sus jobs list.
     *
     * @param objectJson
     *         the object json
     *
     * @return the sus jobs list
     */
    @POST
    @Path( "/sus/jobs/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSusJobsList( String objectJson );

    /**
     * List sus job table UI.
     *
     * @return the response
     */
    @GET
    @Path( "/sus/jobs/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listSusJobTableUI();

    /*
     * **************************************** Log views ****************************************.
     */

    /**
     * Gets the Log views.
     *
     * @param id
     *         the id
     *
     * @return the Log views
     */
    @GET
    @Path( "/{id}/log" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLogViews( @PathParam( "id" ) String id );

    /**
     * Update Log view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{id}/log" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLogView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete Log view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}/log" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLogView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Sets the Log view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/log" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setLogViewAsDefault( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /*
     * **************************************** Permission views ****************************************.
     */

    /**
     * Gets the permission views.
     *
     * @param id
     *         the id
     *
     * @return the permission views
     */
    @GET
    @Path( "/{id}/permissions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPermissionViews( @PathParam( "id" ) String id );

    /**
     * Save view.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/permissions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response savePermissionView( @PathParam( "id" ) String id, String objectJson );

    /**
     * Update permission view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{id}/permissions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updatePermissionView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete permission view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}/permissions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deletePermissionView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Sets the permission view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/permissions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setPermissionViewAsDefault( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /*
     * **************************************** Data Created views ****************************************.
     */

    /**
     * Gets the Data Created view.
     *
     * @param id
     *         the id
     *
     * @return the Data Created view
     */
    @GET
    @Path( "/{id}/data/created" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataCreatedViews( @PathParam( "id" ) String id );

    /**
     * Save view.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/data/created" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDataCreatedView( @PathParam( "id" ) String id, String objectJson );

    /**
     * Update Data Created view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{id}/data/created" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataCreatedView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete Data Created view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}/data/created" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDataCreatedView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Sets the Data Created view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/data/created" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setDataCreatedViewAsDefault( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Process tree children.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/log/tree" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response processTreeChildren( @PathParam( "id" ) String id );

    /*
     * **************************************** File Jobs views ****************************************.
     */

    /**
     * Gets the file jobs views.
     *
     * @return the file jobs views
     */
    @GET
    @Path( "/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFileJobsViews();

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveFileJobsView( String objectJson );

    /**
     * Update file jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateFileJobsView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete file jobs view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteFileJobsView( @PathParam( "viewId" ) String viewId );

    /**
     * Delete sus job view response.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "sus/jobs/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteSusJobView( @PathParam( "viewId" ) String viewId );

    /**
     * Sets the file jobs view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/local" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setFileJobsViewAsDefault( @PathParam( "viewId" ) String viewId );

    /*
     * **************************************** File Jobs log views ****************************************.
     */

    /**
     * Gets the jobs log views.
     *
     * @return the jobs log views
     */
    @GET
    @Path( "/{parentJobId}/log/tree/{childJobId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobsLogViews();

    /**
     * Save view.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{parentJobId}/log/tree/{id}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLogView( @PathParam( "id" ) String id, String objectJson );

    /**
     * Delete job log view.
     *
     * @param childJobId
     *         the child job id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{parentJobId}/log/tree/{childJobId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteJobLogView( @PathParam( "childJobId" ) String childJobId, @PathParam( "viewId" ) String viewId );

    /**
     * Update job log view.
     *
     * @param childJobId
     *         the child job id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{parentJobId}/log/tree/{childJobId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJobLogView( @PathParam( "childJobId" ) String childJobId, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Sets the job log view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{parentJobId}/log/tree/{childJobId}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setJobLogViewAsDefault( @PathParam( "childJobId" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Gets the single job scheme UI.
     *
     * @param id
     *         the id
     *
     * @return the single job scheme UI
     */
    @GET
    @Path( "/{id}/scheme/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSingleJobSchemeUI( @PathParam( "id" ) String id );

    /**
     * Gets the master job scheme tab context.
     *
     * @param jobId
     *         the job id
     * @param filterJson
     *         the filter json
     *
     * @return the master job scheme tab context
     */
    @POST
    @Path( "/{id}/scheme/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getMasterJobSchemeTabContext( @PathParam( "id" ) String jobId, String filterJson );

    /**
     * Gets the master job childs tab context.
     *
     * @param jobId
     *         the job id
     * @param filterJson
     *         the filter json
     *
     * @return the master job childs tab context
     */
    @POST
    @Path( "/{id}/childs/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getMasterJobChildsTabContext( @PathParam( "id" ) String jobId, String filterJson );

    /**
     * Gets the single job scheme data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the single job scheme data
     */
    @POST
    @Path( "/{id}/scheme/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSingleJobSchemeData( @PathParam( "id" ) String id, String objectFilterJson );

    /**
     * Genrate CSV filefor job scheme data.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/csv/download" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response genrateAndDownloadCSVFileforJobSchemeData( @PathParam( "id" ) String id );

    /**
     * Genrate and copy csv filefor job scheme data response.
     *
     * @param fromId
     *         the from id
     * @param toId
     *         the to id
     *
     * @return the response
     */
    @GET
    @Path( "/{fromId}/csv/download/{toId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response genrateAndCopyCSVFileforJobSchemeData( @PathParam( "fromId" ) String fromId, @PathParam( "toId" ) String toId );

    /**
     * Plot CSV file by python.
     *
     * @param id
     *         the id
     * @param optionId
     *         the option id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/plot/option/{optionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response plotCSVFileByPython( @PathParam( "id" ) String id, @PathParam( "optionId" ) String optionId );

    /**
     * Generate Image.
     *
     * @param id
     *         the id
     * @param key
     *         the key
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/generateimage" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response generateImage( @PathParam( "id" ) String id, @QueryParam( "key" ) String key );

    /**
     * Gets the job status by job id.
     *
     * @param id
     *         the id
     *
     * @return the job status by job id
     */
    @GET
    @Path( "/{id}/status" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobStatusByJobId( @PathParam( "id" ) String id );

    /**
     * Gets the job resultsummary by job id.
     *
     * @param id
     *         the id
     *
     * @return the job resultsummary by job id
     */
    @GET
    @Path( "/{id}/resultsummary" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobResultsummaryByJobId( @PathParam( "id" ) String id );

    /**
     * Rerun jobs.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/rerun/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response rerunJobs( @PathParam( "jobId" ) String jobId );

    /**
     * Discard jobs.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/discard/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response discardJobs( @PathParam( "jobId" ) String jobId );

    /**
     * Update job status.
     *
     * @param jobId
     *         the job id
     * @param status
     *         the status
     *
     * @return the response
     */
    @GET
    @Path( "/{jobId}/{status}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJobStatus( @PathParam( "jobId" ) String jobId, @PathParam( "status" ) String status );

    /**
     * Gets the chidls job view.
     *
     * @param id
     *         the id
     *
     * @return the chidls job view
     */
    @GET
    @Path( "/{id}/childs/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getChidlsJobView( @PathParam( "id" ) String id );

    /**
     * Save chidls job view.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/childs/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveChidlsJobView( @PathParam( "id" ) String id, String objectJson );

    /**
     * Update chidls job view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{id}/childs" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateChidlsJobView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete chidls job view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}/childs" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteChidlsJobView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Sets the chidls job view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/childs" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setChidlsJobViewAsDefault( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Download job logs zip.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/logs/download" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response downloadJobLogsZip( @PathParam( "id" ) String id );

    /**
     * Gets workflow jobs context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the workflow jobs context
     */
    @POST
    @Path( "/workflow/{workflowId}/context" )
    Response getWorkflowJobsContext( String filterJson );

    /* ******************************************* Show Workflow Jobs Views ***********************************************************/

    /**
     * Gets the WorkFlow Jobs view.
     *
     * @param id
     *         the id
     *
     * @return the WorkFlow Jobs view
     */
    @GET
    @Path( "/workflow/{id}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowJobsViews( @PathParam( "id" ) String id );

    /**
     * Save view.
     *
     * @param id
     *         the id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/workflow/{id}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveWorkflowJobsView( @PathParam( "id" ) String id, String objectJson );

    /**
     * Update WorkFlow Jobs view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/workflow/{id}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkflowJobsView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete WorkFlow Jobs view.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/workflow/{id}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWorkflowJobsView( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * Sets the WorkFlow Jobs view as default.
     *
     * @param id
     *         the id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/workflow/{id}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setWorkflowJobsViewAsDefault( @PathParam( "id" ) String id, @PathParam( "viewId" ) String viewId );

    /**
     * gets job's input paramaters
     *
     * @param id
     *         the id
     *
     * @return list of UIFormItems
     */
    @GET
    @Path( ( "/{id}/parameters" ) )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobInputParameters( @PathParam( "id" ) String id );

    /**
     * Gets the filtered jobs list.
     *
     * @param column
     *         the column
     *
     * @return the filtered jobs list
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllValuesForJobTableColumn( @PathParam( "column" ) String column );

    /**
     * Gets the filtered jobs list.
     *
     * @param column
     *         the column
     *
     * @return the filtered jobs list
     */
    @GET
    @Path( "/{id}/childs/column/{column}/values" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllValuesForChildJobsTableColumn( @PathParam( "id" ) String id, @PathParam( "column" ) String column );

    /**
     * Gets user jobs list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the personal jobs list
     */
    @POST
    @Path( ( "/user/list" ) )
    Response getUserJobsList( String objectFilterJson );

    /**
     * Gets user jobs ui.
     *
     * @return the personal jobs ui
     */
    @GET
    @Path( ( "/user/ui" ) )
    Response getUserJobsUI();

}
