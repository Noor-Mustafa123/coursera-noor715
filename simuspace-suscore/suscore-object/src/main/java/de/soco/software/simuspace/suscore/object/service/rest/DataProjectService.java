package de.soco.software.simuspace.suscore.object.service.rest;

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

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;

/**
 * The interface Data project service.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface DataProjectService {

    /**
     * Create project response.
     *
     * @param projectJson
     *         the project json
     *
     * @return the response
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createProject( String projectJson );

    /**
     * Update project response.
     *
     * @param projectJson
     *         the project json
     *
     * @return the response
     */
    @PUT
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProject( String projectJson );

    /**
     * Create project form response.
     *
     * @param parentId
     *         the parent id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create/{parentId}" )
    Response createProjectForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId );

    /**
     * Gets project custom attribute ui.
     *
     * @param projectType
     *         the project type
     *
     * @return the project custom attribute ui
     */
    @GET
    @Path( "/ui/customattribute/{projectType}" )
    Response getProjectCustomAttributeUI( @PathParam( "projectType" ) String projectType );

    /**
     * Edit project form response.
     *
     * @param parentId
     *         the parent id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/edit/{parentId}" )
    Response editProjectForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId );

    /**
     * Gets object option form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the object option form
     */
    @GET
    @Path( "/create/options/{parentId}" )
    Response getObjectOptionForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId );

    /**
     * Gets object with version ui.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the object with version ui
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/ui" )
    Response getObjectWithVersionUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * List container tabs ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listContainerTabsUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets project.
     *
     * @param projectId
     *         the project id
     *
     * @return the project
     */
    @GET
    @Path( "/{projectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProject( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets project versions context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the project versions context
     */
    @POST
    @Path( "/{projectId}/versions/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectVersionsContext( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String filterJson );

    /**
     * Gets all fitlered items in project.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all fitlered items in project
     */
    @POST
    @Path( "/{projectId}/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllFitleredItemsInProject( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectFilterJson );

    /**
     * Gets sync context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the sync context
     */
    @POST
    @Path( "/{projectId}/sync/context" )
    Response getSyncContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets transfer context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the transfer context
     */
    @POST
    @Path( "/transfer/context" )
    Response getTransferContext( String filterJson );

    /**
     * Gets local context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the local context
     */
    @POST
    @Path( "/{projectId}/local/context" )
    Response getLocalContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets data context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the data context
     */
    @POST
    @Path( "/{projectId}/type/{typeId}/context" )
    Response getDataContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets container item context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the container item context
     */
    @POST
    @Path( "/{projectId}/toitems/context" )
    Response getContainerItemContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets container item from context.
     *
     * @param projectId
     *         the project id
     * @param filterJson
     *         the filter json
     *
     * @return the container item from context
     */
    @POST
    @Path( "/{projectId}/fromitems/context" )
    Response getContainerItemFromContext( @PathParam( "projectId" ) String projectId, String filterJson );

    /**
     * Gets container versions ui.
     *
     * @param projectId
     *         the project id
     *
     * @return the container versions ui
     */
    @GET
    @Path( "/{projectId}/versions/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getContainerVersionsUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /* *****************************************
     * project static ova APIs list start
     ******************************************/

    /**
     * Gets filtered project versions list.
     *
     * @param projectId
     *         the project id
     * @param projectJson
     *         the project json
     *
     * @return the filtered project versions list
     */
    @POST
    @Path( "/{projectId}/versions/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredProjectVersionsList( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String projectJson );

    /* *****************************************
     * project dynamic ova APIs UI start
     ******************************************/

    /**
     * List container ui response.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/type/{typeId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listContainerUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId );

    /**
     * List project item ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/toitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listProjectItemUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * List project item from ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/fromitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listProjectItemFromUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * List data ui response.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/data/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listDataUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * List data response.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/data/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response listData( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String objectFilterJson );

    /**
     * Gets all sub containers by project id.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all sub containers by project id
     */
    @POST
    @Path( "/{projectId}/type/{typeId}/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllSubContainersByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, String objectFilterJson );

    /**
     * Gets all itemsin project with filter.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all itemsin project with filter
     */
    @POST
    @Path( "/{projectId}/type/{typeId}/data/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllItemsinProjectWithFilter( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, String objectFilterJson );

    /**
     * Gets all itemsin project.
     *
     * @param projectId
     *         the project id
     *
     * @return the all itemsin project
     */
    @GET
    @Path( "/{projectId}/data/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllItemsinProject( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets all items by project id.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all items by project id
     */
    @POST
    @Path( "/{projectId}/toitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllItemsByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectFilterJson );

    /**
     * Gets all from items by project id.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all from items by project id
     */
    @POST
    @Path( "/{projectId}/fromitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllFromItemsByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectFilterJson );

    /**
     * Sets object checkin checkout status.
     *
     * @param projectId
     *         the project id
     * @param check
     *         the check
     *
     * @return the object checkin checkout status
     */
    @GET
    @Path( "/{projectId}/checkout/{check}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response setObjectCheckinCheckoutStatus( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "check" ) String check );

    /**
     * Gets object synch status.
     *
     * @param projectId
     *         the project id
     *
     * @return the object synch status
     */
    @GET
    @Path( "/{projectId}/checkout/get" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectSynchStatus( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets objectz project configurations.
     *
     * @param objectId
     *         the object id
     *
     * @return the objectz project configurations
     */
    @GET
    @Path( "/config/{objectId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectzProjectConfigurations( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) UUID objectId );

    /**
     * Gets all object views by object id and type id.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     *
     * @return the all object views by object id and type id
     */
    @GET
    @Path( "/{projectId}/type/{typeId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectViewsByObjectIdAndTypeId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId );

    /**
     * Save object view response.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/type/{typeId}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, String objectJson );

    /**
     * Sets object view as default.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     *
     * @return the object view as default
     */
    @GET
    @Path( "/{projectId}/type/{typeId}" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setObjectViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete object view response.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/type/{typeId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "viewId" ) String viewId );

    /**
     * Update object view response.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{projectId}/type/{typeId}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Save data view response.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDataView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String objectJson );

    /**
     * Delete data view response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDataView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update data view response.
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
    @Path( "/{projectId}/data" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Save data view as default response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/data" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDataViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Gets data views.
     *
     * @param projectId
     *         the project id
     *
     * @return the data views
     */
    @GET
    @Path( "/{projectId}/data" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataViews( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets all project sync views by project id.
     *
     * @param projectId
     *         the project id
     *
     * @return the all project sync views by project id
     */
    @GET
    @Path( "/{projectId}/sync" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllProjectSyncViewsByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Save project sync view by project id response.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/sync" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveProjectSyncViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectJson );

    /**
     * Sets project sync view as default by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the project sync view as default by project id
     */
    @GET
    @Path( "/{projectId}/sync" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setProjectSyncViewAsDefaultByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete project sync view by project id response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/sync" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectSyncViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update project sync view by project id response.
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
    @Path( "/{projectId}/sync" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectSyncViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /*
     * **************************************** project local view ****************************************.
     */

    /**
     * Gets all project local views by project id.
     *
     * @param projectId
     *         the project id
     *
     * @return the all project local views by project id
     */
    @GET
    @Path( "/{projectId}/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllProjectLocalViewsByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Save project local view by project id response.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/local" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveProjectLocalViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectJson );

    /**
     * Sets project local view as default by project id.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the project local view as default by project id
     */
    @GET
    @Path( "/{projectId}/local" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setProjectLocalViewAsDefaultByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete project local view by project id response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectLocalViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update project local view by project id response.
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
    @Path( "/{projectId}/local" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectLocalViewByProjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all object version views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all object version views by object id
     */
    @GET
    @Path( "/{projectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectVersionViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save object version view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets version view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the version view as default
     */
    @GET
    @Path( "/{projectId}/versions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setVersionViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete object version view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update object version view response.
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
    @Path( "/{projectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets life cycle status by id.
     *
     * @param lifeCycleId
     *         the life cycle id
     *
     * @return the life cycle status by id
     */
    @GET
    @Path( "/lifecycle/id/{lifeCycleId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLifeCycleStatusById( @PathParam( "lifeCycleId" ) String lifeCycleId );

    /**
     * Gets container sync status ui.
     *
     * @param projectId
     *         the project id
     *
     * @return the container sync status ui
     */
    @GET
    @Path( "/{projectId}/sync/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getContainerSyncStatusUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets container local status ui.
     *
     * @param projectId
     *         the project id
     *
     * @return the container local status ui
     */
    @GET
    @Path( "/{projectId}/local/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getContainerLocalStatusUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets items count.
     *
     * @param projectId
     *         the project id
     *
     * @return the items count
     */
    @GET
    @Path( "/{projectId}/data/count" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getItemsCount( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId );

    /**
     * Gets all items count with type id.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     *
     * @return the all items count with type id
     */
    @GET
    @Path( "/{projectId}/type/{typeId}/data/count" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllItemsCountWithTypeId( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId );

    /**
     * Gets all linked to objects views.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the all linked to objects views
     */
    @GET
    @Path( "/{projectId}/toitems" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLinkedToObjectsViews( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Gets all linked from objects views.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the all linked from objects views
     */
    @GET
    @Path( "/{projectId}/fromitems" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLinkedFromObjectsViews( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save linked to objects view response.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/toitems" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLinkedToObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String objectJson );

    /**
     * Save linked from objects view response.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/fromitems" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLinkedFromObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId, String objectJson );

    /**
     * Save linked objects view as default response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{projectId}/items" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLinkedObjectsViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete linked to objects view response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/toitems" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLinkedToObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete linked from objects view response.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{projectId}/fromitems" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLinkedFromObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update linked to objects view response.
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
    @Path( "/{projectId}/toitems" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLinkedToObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Update linked from objects view response.
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
    @Path( "/{projectId}/fromitems" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLinkedFromObjectsView( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets overview by project id.
     *
     * @param projectId
     *         the project id
     * @param language
     *         the language
     *
     * @return the overview by project id
     */
    @GET
    @Path( "/{projectId}/overview" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getOverviewByProjectId( @PathParam( "projectId" ) String projectId, @QueryParam( "lang" ) String language );

    /**
     * Gets container or childs by id.
     *
     * @param objectId
     *         the object id
     *
     * @return the container or childs by id
     */
    @GET
    @Path( "/container/childs/{objectId}" )
    Response getContainerOrChildsById( @PathParam( "objectId" ) String objectId );

    /**
     * Gets property manager constant.
     *
     * @return the property manager constant
     */
    @GET
    @Path( "/propertymanager/vault" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getPropertyManagerConstant();

    /**
     * Gets all values for project table column.
     *
     * @param projectId
     *         the project id
     * @param typeId
     *         the type id
     * @param column
     *         the column
     *
     * @return the all values for project table column
     */
    @GET
    @Path( "/{projectId}/type/{typeId}/column/{column}/values" )
    Response getAllValuesForProjectTableColumn( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "column" ) String column );

    /**
     * Permission project options form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/perm/ui/create/{projectId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response permissionProjectOptionsForm( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String objectId );

    /**
     * Gets update project permission ui.
     *
     * @param projectId
     *         the project id
     * @param option
     *         the option
     *
     * @return the update project permission ui
     */
    @GET
    @Path( "/{projectId}/permission/fields/{option}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getUpdateProjectPermissionUI( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectServiceEndPoints.OPTION ) String option );

    /**
     * Change project permissions response.
     *
     * @param projectId
     *         the project id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{projectId}/permission/change" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response changeProjectPermissions( @PathParam( ConstantsObjectServiceEndPoints.PROJ_ID_PARAM ) String projectId,
            String objectFilterJson );

}
