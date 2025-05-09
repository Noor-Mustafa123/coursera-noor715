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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.server.constant.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsAlgorithmsServiceEndpoints;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * This Interface is responsible for all the operation Of workflows.
 *
 * @author Nosheen.Sharif
 */
@WebService
@Consumes( { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface WorkflowService {

    /**
     * Creates the job.
     *
     * @param jobJson
     *         the job json
     *
     * @return the response
     */
    @POST
    @Path( "/job" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createJob( String jobJson );

    /**
     * Creates the workflow.
     *
     * @param parentId
     *         the parent id
     * @param workflowJson
     *         the workflow json
     *
     * @return the response
     */
    @POST
    @Path( "/{parentId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWorkflow( @PathParam( "parentId" ) String parentId, String workflowJson );

    /**
     * Create workflow as new response.
     *
     * @param parentId
     *         the parent id
     * @param workflowJson
     *         the workflow json
     *
     * @return the response
     */
    @POST
    @Path( "/{parentId}/new" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWorkflowAsNew( @PathParam( "parentId" ) String parentId, String workflowJson );

    /**
     * It gets workflow job by job id.
     *
     * @param jobId
     *         the job id
     *
     * @return the job
     */
    @GET
    @Path( "/job/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJob( @PathParam( "jobId" ) String jobId );

    /**
     * Gets the workflow by id.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow by id
     */
    @GET
    @Path( "/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowById( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the workflow by id.
     *
     * @return the workflow by id
     */
    @GET
    @Path( "/plugin" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomFlagList();

    /**
     * Gets the custom flag list UI.
     *
     * @param plugins
     *         the plugins
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag list UI
     */
    @GET
    @Path( "/plugin/{plugins}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomFlagUI( @PathParam( "plugins" ) String plugins, @Context UriInfo uriInfo );

    /**
     * Gets the shape module run tabs.
     *
     * @param plugins
     *         the plugins
     *
     * @return the shape module run tabs
     */
    @GET
    @Path( "/plugin/{plugins}/run/tabs" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getShapeModuleRunTabs( @PathParam( "plugins" ) String plugins );

    /**
     * Gets the custom flag list UI.
     *
     * @param rerunJobId
     *         the rerunJobId
     * @param plugins
     *         the plugins
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag list UI
     */
    @GET
    @Path( "rerunJobId/{rerunJobId}/plugin/{plugins}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomFlagUIForRerun( @PathParam( "rerunJobId" ) String rerunJobId, @PathParam( "plugins" ) String plugins,
            @Context UriInfo uriInfo );

    /**
     * Gets the custom flag list UI.
     *
     * @param plugins
     *         the plugins
     * @param value
     *         the value
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag list UI
     */
    @GET
    @Path( "/plugin/{pluginName}/{value : .+}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomFlagPluginUI( @PathParam( "pluginName" ) String plugins, @PathParam( "value" ) String value,
            @Context UriInfo uriInfo );

    /**
     * Gets the custom flag list UI on ReRun.
     *
     * @param rerunJobId
     *         the rerunJobId
     * @param plugins
     *         the plugins
     * @param value
     *         the value
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag list UI
     */
    @GET
    @Path( "/rerunJobId/{rerunJobId}/plugin/{pluginName}/{value : .+}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomFlagPluginUIForRerun( @PathParam( "rerunJobId" ) String rerunJobId, @PathParam( "pluginName" ) String plugins,
            @PathParam( "value" ) String value, @Context UriInfo uriInfo );

    /**
     * Gets the dynamic fields.
     *
     * @param plugin
     *         the plugin
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the dynamic fields
     */
    @POST
    @Path( "/dynamic/fields/{pluginName}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDynamicFields( @PathParam( "pluginName" ) String plugin, String jobParametersString );

    /**
     * Gets the workflow by selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the workflow by selection id
     */
    @GET
    @Path( "/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowBySelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the workflow by id with ref job params.
     *
     * @param workflowId
     *         the workflow id
     * @param jobId
     *         the job id
     *
     * @return the workflow by id with ref job params
     */
    @GET
    @Path( "/{workflowId}/job/{jobId}/rerun" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowByIdWithRefJobParams( @PathParam( "workflowId" ) String workflowId, @PathParam( "jobId" ) String jobId );

    /**
     * Gets the workflow versions by id.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow versions by id
     */
    @GET
    @Path( "/version/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowVersionsById( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the workflow list.
     *
     * @return the workflow list
     */
    @GET
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowList();

    /**
     * Gets the token.
     *
     * @return the token
     */
    @GET
    @Path( "/token" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getToken();

    /**
     * Load config.
     *
     * @return the response
     */
    @GET
    @Path( "/config" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response loadConfig();

    /**
     * Load config.
     *
     * @return the response
     */
    @GET
    @Path( "/hpc/config" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcConfig();

    /**
     * Load email config.
     *
     * @return the response
     */
    @GET
    @Path( "/email/config" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getEmailConfig();

    /**
     * It updates a workflow job.
     *
     * @param jobId
     *         the job id
     * @param jobJson
     *         the job json
     *
     * @return the response
     */
    @PUT
    @Path( "/job/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJob( @PathParam( "jobId" ) UUID jobId, String jobJson );

    /**
     * Update workflow.
     *
     * @param workflowId
     *         the workflow id
     * @param workflowJson
     *         the workflow json
     *
     * @return the response
     */
    @PUT
    @Path( "/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    @Consumes( { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON } )
    Response updateWorkflow( @PathParam( "workflowId" ) String workflowId, String workflowJson );

    /**
     * Gets the workflow by id and version id.
     *
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     *
     * @return the workflow by id and version id
     */
    @GET
    @Path( "{workflowId}/version/{versionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowByIdAndVersionId( @PathParam( "workflowId" ) String workflowId, @PathParam( "versionId" ) int versionId );

    /**
     * Gets the category list.
     *
     * @return the category list
     */
    @GET
    @Path( "/category" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCategoryList();

    /**
     * Creates the category.
     *
     * @param categoryJson
     *         the category json
     *
     * @return the response
     */
    @POST
    @Path( "/category" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createCategory( String categoryJson );

    /**
     * Update category.
     *
     * @param categoryId
     *         the category id
     * @param categoryJson
     *         the category json
     *
     * @return the response
     */
    @PUT
    @Path( "/category/{categoryId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateCategory( @PathParam( "categoryId" ) String categoryId, String categoryJson );

    /**
     * Delete category.
     *
     * @param categoryId
     *         the category id
     *
     * @return the response
     */
    @DELETE
    @Path( "/category/{categoryId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteCategory( @PathParam( "categoryId" ) String categoryId );

    /**
     * Gets the workflow list by category id.
     *
     * @param categoryId
     *         the category id
     *
     * @return the workflow list by category id
     */
    @GET
    @Path( "/list/category/{categoryId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowListByCategoryId( @PathParam( "categoryId" ) String categoryId );

    /**
     * Assign categories to workflow.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( "/category/list/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCategoryListByWorkflowId( @PathParam( "workflowId" ) String workflowId );

    /**
     * Assign categories to workflow.
     *
     * @param workflowId
     *         the workflow id
     * @param idsJson
     *         the ids json
     *
     * @return the response
     */
    @PUT
    @Path( "{workflowId}/assign/category" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response assignCategoriesToWorkflow( @PathParam( "workflowId" ) String workflowId, String idsJson );

    /**
     * Update job log.
     *
     * @param jobId
     *         the job id
     * @param jobJson
     *         the job json
     *
     * @return the response
     */
    @PUT
    @Path( "/job/log/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJobLog( @PathParam( "jobId" ) UUID jobId, String jobJson );

    /**
     * Update job log.
     *
     * @param jobId
     *         the job id
     * @param jobJson
     *         the job json
     *
     * @return the response
     */
    @PUT
    @Path( "/job/log/progress/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateJobLogAndProgress( @PathParam( "jobId" ) UUID jobId, String jobJson );

    /**
     * Gets the jobs list.
     *
     * @return the jobs list
     */
    @GET
    @Path( "/job" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobsList();

    /**
     * Gets the last job directory by work flow.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the last job directory by work flow
     */
    @GET
    @Path( "/job/dir/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLastJobDirectoryByWorkFlow( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the workflow versions without definition.
     *
     * @param id
     *         the id
     *
     * @return the workflow versions without definition
     */
    @GET
    @Path( "{id}/versions" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowVersionsWithoutDefinition( @PathParam( "id" ) String id );

    /**
     * Update workflow action.
     *
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     * @param actionType
     *         the action type
     * @param actionId
     *         the action id
     *
     * @return the response
     */
    @PUT
    @Path( "{id}/version/{versionId}/{actionType}/{actionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkflowAction( @PathParam( "id" ) String workflowId, @PathParam( "versionId" ) int versionId,
            @PathParam( "actionType" ) String actionType, @PathParam( "actionId" ) int actionId );

    /**
     * Checks in license based on user rights.
     *
     * @return the response
     */
    @GET
    @Path( "/license/checkin" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkInLicense();

    /**
     * Gets the license consumers list.
     *
     * @return the license consumers list
     */
    @GET
    @Path( "/license/consumers" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLicenseConsumersList();

    /**
     * Clear token.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/license/checkin/usertoken" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response clearToken( String json );

    /**
     * Checkout token.
     *
     * @return the response
     */
    @GET
    @Path( "/license/checkout" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkoutToken();

    /**
     * Add workflow to favorite.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @POST
    @Path( "{workflowId}/favorite" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addWorkflowToFavorite( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the favorite workflow list.
     *
     * @return the workflow list
     */
    @GET
    @Path( "/list/favorite" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFavoriteWorkFlowList();

    /**
     * Checkout token.
     *
     * @return the response
     */
    @GET
    @Path( "/license/verify" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response verifyLicenseCheckout();

    /**
     * Creates the workflow form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create/{parentId}" )
    Response createWorkflowForm( @PathParam( "parentId" ) String parentId );

    /**
     * Edits the workflow form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/edit/{parentId}" )
    Response editWorkflowForm( @PathParam( "parentId" ) String parentId );

    /*
     * breadcrumb/view/data/object/7ae91204-8a5e-4f08-af73-5719fc058dbe/version/2 get.
     */

    /**
     * Single View With Sub-tabs of object.
     *
     * @param workflowId
     *         the object id
     *
     * @return the tabs view data object UI
     */
    @GET
    @Path( "/{workflowId}/subtabs" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTabsViewWorkflowUI( @PathParam( "workflowId" ) String workflowId );

    /**
     * Manage object permission table UI.
     *
     * @param workflowId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{workflowId}/permissions/ui" )
    Response workflowPermissionTableUI( @PathParam( "workflowId" ) UUID workflowId );

    /**
     * Show permitted users and groups for object.
     *
     * @param workflowId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{workflowId}/permissions/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response showPermittedUsersAndGroupsForWorkflow( @PathParam( "workflowId" ) UUID workflowId, String objectFilterJson );

    /**
     * Permit permission to work flow.
     *
     * @param permissionJson
     *         the permission json
     * @param workFlowId
     *         the work flow id
     * @param sidId
     *         the sid id
     *
     * @return the response
     */
    @PUT
    @Path( "/{workFlowId}/permissions/{sidId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response permitPermissionToWorkFlow( String permissionJson, @PathParam( "workFlowId" ) UUID workFlowId,
            @PathParam( "sidId" ) UUID sidId );

    /**
     * Gets the data object versions UI.
     *
     * @param workflowId
     *         the object id
     *
     * @return the data object versions UI
     */
    @GET
    @Path( "/version/{workflowId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowVersionsUI( @PathParam( "workflowId" ) String workflowId );

    /**
     * Get Filtered List of Versions of Given DataObject id.
     *
     * @param workflowId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the filtered data object versions list
     */
    @POST
    @Path( "/version/{workflowId}/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredWorkflowVersionsList( @PathParam( "workflowId" ) String workflowId, String objectJson );

    /**
     * Gets the workflow by id.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow by id
     */
    @GET
    @Path( "/project/{projectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectById( @PathParam( "projectId" ) String workflowId );

    /**
     * List Objects UI Table View.
     *
     * @param projectId
     *         the project id
     *
     * @return Response workflow project tabs ui
     */
    @GET
    @Path( "/project/{projectId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowProjectTabsUI( @PathParam( "projectId" ) String projectId );

    /**
     * List Objects UI Table View.
     *
     * @param projectId
     *         the project id
     *
     * @return Response response
     */
    @GET
    @Path( "/project/{projectId}/workflow/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listOfWorkflowUI( @PathParam( "projectId" ) String projectId );

    /**
     * Gets the all workflows.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all workflows
     */
    @POST
    @Path( "/project/{projectId}/workflow/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllWorkflows( @PathParam( "projectId" ) String projectId, String objectFilterJson );

    /**
     * Run workflow.
     *
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response
     */
    @POST
    @Path( "/run/job" )
    @Produces( MediaType.APPLICATION_JSON )
    Response runWorkflow( String jobParametersString );

    /**
     * Run workflow from web.
     *
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response
     */
    @POST
    @Path( "/runjob" )
    @Produces( MediaType.APPLICATION_JSON )
    Response runWorkflowFromWeb( String jobParametersString );

    /**
     * Gets the workflow context.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflow context
     */
    @POST
    @Path( "/project/{projectId}/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getWorkflowContext( @PathParam( "projectId" ) String projectId, String objectFilterJson );

    /**
     * Gets the workflow sub project context.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflow sub project context
     */
    @POST
    @Path( "/subproject/{projectId}/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getWorkflowSubProjectContext( @PathParam( "projectId" ) String projectId, String objectFilterJson );

    /**
     * Gets the context.
     *
     * @param objectId
     *         the objectId id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the context
     */
    @POST
    @Path( "/project/{objectId}/workflow/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getContext( @PathParam( "objectId" ) String objectId, String objectFilterJson );

    /*
     * **************************************** project dynamic ova APIs UI start ****************************************.
     */

    /**
     * List data UI.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/data/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listDataUI( @PathParam( "projectId" ) String projectId );

    /**
     * List data.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/data/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response listData( @PathParam( "projectId" ) String projectId, String objectFilterJson );

    /**
     * List data UI.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/subproject/{projectId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowSubProjectUI( @PathParam( "projectId" ) String projectId );

    /**
     * List data.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/subproject/{projectId}/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getWorkflowProjectList( @PathParam( "projectId" ) String projectId, String objectFilterJson );

    /**
     * List data UI.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/properties/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowProjectPopertiesUI( @PathParam( "projectId" ) String projectId );

    /**
     * List data.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/properties/data" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getWorkflowProject( @PathParam( "projectId" ) String projectId );

    /**
     * List data UI.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/versions/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowProjectVersionUI( @PathParam( "projectId" ) String projectId );

    /**
     * Gets the filtered object versions list.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the filtered object versions list
     */
    @POST
    @Path( "/project/{projectId}/versions/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredObjectVersionsList( @PathParam( "projectId" ) String objectId, String objectJson );

    /*
     * **************************************** workflow permissions view ****************************************.
     */

    /**
     * Gets the all work flow permission views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all work flow permission views by object id
     */
    @GET
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllWorkFlowPermissionViewsByObjectId( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Save work flow permission view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveWorkFlowPermissionView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Sets the work flow permission view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setWorkFlowPermissionViewAsDefault( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete work flow permission view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWorkFlowPermissionView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update work flow permission view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkFlowPermissionView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /*
     * **************************************** workflow project version view ****************************************.
     */

    /**
     * Gets the all data object version views by object id.
     *
     * @param projectId
     *         the project id
     *
     * @return the all data object version views by object id
     */
    @GET
    @Path( "/project/{projectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllProjectWorkFlowVersionViewsByObjectId( @PathParam( "projectId" ) String projectId );

    /**
     * Save project work flow version view.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveProjectWorkFlowVersionView( @PathParam( "projectId" ) String projectId, String objectJson );

    /**
     * Sets the project work flow version view as default.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/versions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setProjectWorkFlowVersionViewAsDefault( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete project work flow version view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/project/{projectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectWorkFlowVersionView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update project work flow version view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/project/{projectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectWorkFlowVersionView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /*
     * **************************************** workflow subproject view ****************************************.
     */

    /**
     * Gets the all sub project work flow views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all sub project work flow views by object id
     */
    @GET
    @Path( "/subproject/{projectId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllSubProjectWorkFlowViewsByObjectId( @PathParam( "projectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Save sub project work flow view.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/subproject/{projectId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveSubProjectWorkFlowView( @PathParam( "projectId" ) String projectId, String objectJson );

    /**
     * Sets the sub project work flow view as default.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/subproject/{projectId}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setSubProjectWorkFlowViewAsDefault( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete sub project work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/subproject/{projectId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteSubProjectWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update sub project work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/subproject/{projectId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateSubProjectWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /*
     * **************************************** workflow view ****************************************.
     */

    /**
     * Gets the all project work flow views by object id.
     *
     * @param projectId
     *         the project id
     *
     * @return the all project work flow views by object id
     */
    @GET
    @Path( "/project/{projectId}/workflow" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllProjectWorkFlowViewsByObjectId( @PathParam( "projectId" ) String projectId );

    /**
     * Save project work flow view.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/workflow" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveProjectWorkFlowView( @PathParam( "projectId" ) String projectId, String objectJson );

    /**
     * Sets the project work flow view as default.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/workflow" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setProjectWorkFlowViewAsDefault( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete project work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/project/{projectId}/workflow" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update project work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/project/{projectId}/workflow" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /*
     * **************************************** workflow data view ****************************************.
     */

    /**
     * Create Project.
     *
     * @param projectJson
     *         the project json
     *
     * @return Response response
     */
    @POST
    @Path( "/project" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createProject( String projectJson );

    /**
     * generate create project form.
     *
     * @param parentId
     *         the parent id
     *
     * @return Response response
     */
    @GET
    @Path( "/project/ui/create/{parentId}" )
    Response createProjectForm( @PathParam( "parentId" ) String parentId );

    /**
     * Gets the all project data work flow views by object id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the all project data work flow views by object id
     */
    @GET
    @Path( "/project/{projectId}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllProjectDataWorkFlowViewsByObjectId( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Save project data work flow view.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/project/{projectId}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveProjectDataWorkFlowView( @PathParam( "projectId" ) String projectId, String objectJson );

    /**
     * Sets the project data work flow view as default.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/project/{projectId}/data" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setProjectDataWorkFlowViewAsDefault( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete project data work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/project/{projectId}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectDataWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update project data work flow view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/project/{projectId}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectDataWorkFlowView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /*
     * **************************************** workflow version view ****************************************.
     */

    /**
     * Gets the all work flow version views by object id.
     *
     * @param versionId
     *         the version id
     * @param viewId
     *         the view id
     *
     * @return the all work flow version views by object id
     */
    @GET
    @Path( "/version/{versionId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllWorkFlowVersionViewsByObjectId( @PathParam( "versionId" ) String versionId, @PathParam( "viewId" ) String viewId );

    /**
     * Save work flow version view.
     *
     * @param versionId
     *         the version id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/version/{versionId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveWorkFlowVersionView( @PathParam( "versionId" ) String versionId, String objectJson );

    /**
     * Sets the work flow version view as default.
     *
     * @param versionId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/version/{versionId}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setWorkFlowVersionViewAsDefault( @PathParam( "versionId" ) String versionId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete work flow version view.
     *
     * @param versionId
     *         the version id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/version/{versionId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWorkFlowVersionView( @PathParam( "versionId" ) String versionId, @PathParam( "viewId" ) String viewId );

    /**
     * Update work flow version view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/version/{projectId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkFlowVersionView( @PathParam( "projectId" ) String projectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Check if user has workflow license.
     *
     * @return the response
     */
    @GET
    @Path( "/user/haslicense" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkIfUserHasWorkflowLicense();

    /**
     * Check if user has scheme license.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( "/user/schemelicense/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response checkIfUserHasSchemeLicense( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId );

    /**
     * Gets the project versions context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the project versions context
     */
    @POST
    @Path( "/project/{projectId}/versions/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectVersionsContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets the workflow versions context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the workflow versions context
     */
    @POST
    @Path( "/version/{projectId}/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowVersionsContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets the filtered workflow list.
     *
     * @param userFilterJson
     *         the user filter json
     *
     * @return the filtered workflow list
     */
    @POST
    @Path( "/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredWorkflowList( String userFilterJson );

    /**
     * Gets the filtered workflow UI.
     *
     * @return the filtered workflow UI
     */
    @GET
    @Path( "/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredWorkflowUI();

    /**
     * Gets the filtered workflow context.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered workflow context
     */
    @POST
    @Path( "/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredWorkflowContext( @PathParam( "objectId" ) String objectId, String objectFilterJson );

    /**
     * Gets the workflow project context.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the workflow project context
     */
    @POST
    @Path( "/project/{projectId}/data/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getWorkflowProjectContext( @PathParam( "projectId" ) String projectId, String objectFilterJson );

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
     * Sets the view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setViewAsDefault( @PathParam( "viewId" ) String viewId );

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
     * Update view.
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
     * Gets the relations and delete.
     *
     * @param id
     *         the id
     *
     * @return the relations and delete
     */
    @GET
    @Path( ConstantsViewEndPoints.GET_RELATION_DELETE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRelationsAndDelete( @PathParam( "id" ) String id );

    /**
     * Creates the relation.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.CREATE_RELATION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createRelation( String json );

    /**
     * Creates the objective varibale file in staging by workflow.
     *
     * @param wfId
     *         the wf id
     * @param jobId
     *         the job id
     * @param runId
     *         the run id
     * @param masterJobId
     *         the master job id
     * @param jobname
     *         the jobname
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/workflow/{wfId}/masterJobId/{masterJobId}/job/{jobId}/jobname/{jobname}/runson/{runId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDesignVariableFilesInStagingByWorkflow( @PathParam( "wfId" ) String wfId, @PathParam( "jobId" ) String jobId,
            @PathParam( "runId" ) String runId, @PathParam( "masterJobId" ) UUID masterJobId, @PathParam( "jobname" ) String jobname,
            String json );

    /**
     * Gets the import workflow form.
     *
     * @param id
     *         the id
     *
     * @return the import workflow form
     */
    @GET
    @Path( "import/ui/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getImportWorkflowForm( @PathParam( "id" ) String id );

    /**
     * Import workflow.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/import" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response importWorkflow( String json );

    /**
     * Validate workflow.
     *
     * @param wfId
     *         the wf id
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @GET
    @Path( "/{workflowId}/version/{versionId}/validate" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response validateWorkflow( @PathParam( "workflowId" ) UUID wfId, @PathParam( "versionId" ) String versionId );

    /**
     * Download workflow.
     *
     * @param workflowId
     *         the workflow id
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/download/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response downloadWorkflow( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the job id by UUID.
     *
     * @param uuid
     *         the uuid
     *
     * @return the job id by UUID
     */
    @GET
    @Path( "/getJobIdByUUID/{uuid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobIdByUUID( @PathParam( "uuid" ) UUID uuid );

    /**
     * Save job ids.
     *
     * @param uuid
     *         the uuid
     *
     * @return the response
     */
    @GET
    @Path( "/saveJobIds/{uuid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveJobIds( @PathParam( "uuid" ) UUID uuid );

    /**
     * Download design summary link.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( "/designsummary/download/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response downloadDesignSummaryLink( @PathParam( "workflowId" ) String workflowId );

    /**
     * Prepare goals.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the response
     */
    @GET
    @Path( "goal/{workflowId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response prepareGoals( @PathParam( "workflowId" ) String workflowId );

    /**
     * Gets the CB 2 entity list by selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the CB 2 entity list by selection id
     */
    @GET
    @Path( "cb2/entity/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCB2EntityListBySelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Move workflow.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/move" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response moveWorkflow( String filterJson );

    /**
     * Copy assemble and simulate local files in staging.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/assemblesimulate/files/copy" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response copyAssembleAndSimulateFilesInStaging( String filterJson );

    /**
     * Gets the regex list by selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the regex list by selection id
     */
    @GET
    @Path( "/regex/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getScanFileBySelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the template scan file by selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the template scan file by selection id
     */
    @GET
    @Path( "/template/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTemplateScanFileBySelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the list of scripts names from dynamic_scripts.json for SUSAction WFElement
     *
     * @return the script list
     */
    @GET
    @Path( "/susaction/scripts" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getActionScriptFileNames();

    /**
     * Gets the details of scripts from dynamic_scripts.json for SUSAction WFElement
     *
     * @param scriptId
     *         the script id
     *
     * @return the script list
     */
    @GET
    @Path( "/susaction/scripts/{scriptId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getActionScriptFileDetails( @PathParam( "scriptId" ) String scriptId );

    /**
     * Gets the list of py environments from py.env.json for PythonWFE
     *
     * @return the python environments
     */
    @GET
    @Path( "/python/environment" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPythonEnvironments();

    /**
     * Gets the details of py environment from py.env.json for PythonWFE
     *
     * @param envId
     *         the env id
     *
     * @return the python environments
     */
    @GET
    @Path( "/python/environment/{envId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPythonEnvironment( @PathParam( "envId" ) String envId );

    /**
     * Gets the custom variable data.
     *
     * @param workflowId
     *         the workflow id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the custom variable data
     */
    @POST
    @Path( ConstantsAlgorithmsServiceEndpoints.GET_CUSTOM_VARIABLES_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCustomVariableData( @PathParam( ConstantsAlgorithmsServiceEndpoints.WORKFLOW_ID_PARAM ) String workflowId,
            String objectFilterJson );

    /**
     * Resubmit job response.
     *
     * @param jobParametersJson
     *         the job parameters string
     *
     * @return the response
     */
    @POST
    @Path( "/resubmit/job" )
    @Produces( MediaType.APPLICATION_JSON )
    Response resubmitJob( String jobParametersJson );

    /**
     * Duplicate selection by field type response.
     *
     * @param workflowId
     *         the workflow id
     * @param selectionId
     *         the selection id
     * @param fieldType
     *         the field type
     *
     * @return the response
     */
    @GET
    @Path( "/selection/{selectionId}/fieldType/{fieldType}/duplicate" )
    @Produces( MediaType.APPLICATION_JSON )
    Response duplicateSelection( @PathParam( "selectionId" ) String selectionId, @PathParam( "fieldType" ) String fieldType );

    /**
     * Gets select script file names.
     *
     * @return the select script file names
     */
    @GET
    @Path( "/select/scripts" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getSelectScriptFileNames();

    /**
     * Gets the details of scripts from dynamic_scripts.json for SUSAction WFElement
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     * @param selectScriptPayloadJson
     *         the select script payload json
     *
     * @return the script list
     */
    @POST
    @Path( "/select/scripts/{scriptId}/element/{elementKey}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSelectScriptOptions( @PathParam( "scriptId" ) String scriptId, @PathParam( "elementKey" ) String elementKey,
            String selectScriptPayloadJson );

    /**
     * Gets field script file names.
     *
     * @return the field script file names
     */
    @GET
    @Path( "/field/scripts" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFieldScriptFileNames();

    /**
     * Gets the details of scripts from dynamic_scripts.json for SUSAction WFElement
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     *
     * @return the script list
     */
    @POST
    @Path( "/field/scripts/{scriptId}/element/{elementKey}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFieldsFromFieldScript( @PathParam( "scriptId" ) String scriptId, @PathParam( "elementKey" ) String elementKey,
            String fieldJson );

    /**
     * Gets all values for workflow sub project table column.
     *
     * @param projectId
     *         the project id
     * @param column
     *         the column
     *
     * @return the all values for workflow sub project table column
     */
    @GET
    @Path( "/subproject/{projectId}/column/{column}/values" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllValuesForWorkflowSubProjectTableColumn( @PathParam( "projectId" ) String projectId,
            @PathParam( "column" ) String column );

    /**
     * Gets all values for workflow table column.
     *
     * @param projectId
     *         the project id
     * @param column
     *         the column
     *
     * @return the all values for workflow table column
     */
    @GET
    @Path( "/project/{projectId}/workflow/column/{column}/values" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllValuesForWorkflowTableColumn( @PathParam( "projectId" ) String projectId, @PathParam( "column" ) String column );

    /**
     * Gets all values for workflow project table column.
     *
     * @param projectId
     *         the project id
     * @param column
     *         the column
     *
     * @return the all values for workflow project table column
     */
    @GET
    @Path( "/project/{projectId}/data/column/{column}/values" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllValuesForWorkflowProjectTableColumn( @PathParam( "projectId" ) String projectId, @PathParam( "column" ) String column );

}