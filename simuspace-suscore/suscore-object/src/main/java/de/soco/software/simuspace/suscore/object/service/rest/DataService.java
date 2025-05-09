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
import de.soco.software.simuspace.suscore.document.constants.ConstantsDocumentServiceEndPoints;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;

/**
 * This Interface is responsible for all the operation related to sus Data Object.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface DataService {

    /**
     * Update workflow project.
     *
     * @param projectJson
     *         the project json
     *
     * @return the response
     */
    @PUT
    @Path( "/workflow/project" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkflowProject( String projectJson );

    /**
     * Gets the items from selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the items from selection id
     */
    @GET
    @Path( "/selection/{selectionId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getItemsFromSelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the deleted objects table UI.
     *
     * @return the deleted objects table UI
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.DELETED_OBJECTS_TABLE_UI )
    Response getDeletedObjectsTableUI();

    /**
     * Gets the deleted object list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the deleted object list
     */
    @POST
    @Path( ConstantsObjectServiceEndPoints.DELETED_OBJECTS_LIST )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDeletedObjectList( String objectFilterJson );

    // ----------

    /**
     * Gets the all deleted object views.
     *
     * @return the all deleted object views
     */
    @GET
    @Path( "/deletedobjects" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllDeletedObjectViews();

    /**
     * Save deleted object view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/deletedobjects" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDeletedObjectView( String objectJson );

    /**
     * Sets the deleted object view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/deletedobjects" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setDeletedObjectViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete deleted object view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/deletedobjects" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDeletedObjectView( @PathParam( "viewId" ) String viewId );

    /**
     * Update deleted object view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/deletedobjects" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDeletedObjectView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Update file to an object.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the response
     */
    @PUT
    @Path( "/{jobId}/object/{objectId}/file" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateFileToAnObject( @PathParam( ConstantsObjectServiceEndPoints.JOB_ID_PARAM ) String jobId,
            @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json );

    /**
     * Gets the deleted objects context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the deleted objects context
     */
    @POST
    @Path( ConstantsObjectServiceEndPoints.DELETED_OBJECTS_CONTEXT )
    Response getDeletedObjectsContext( String filterJson );

    /**
     * Restore object by selection.
     *
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @PUT
    @Path( "/restore/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response restoreObjectBySelection( @PathParam( "id" ) String id, @QueryParam( "mode" ) String mode );

    /**
     * Gets the user export permission.
     *
     * @param containerId
     *         the container id
     *
     * @return the user export permission
     */
    @GET
    @Path( "/perm/read/check/{containerId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getUserReadPermission( @PathParam( "containerId" ) String containerId );

    /**
     * Gets the user delete permission.
     *
     * @param containerId
     *         the container id
     *
     * @return the user export permission
     */
    @GET
    @Path( "/perm/delete/check/{containerId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getUserDeletePermission( @PathParam( "containerId" ) String containerId );

    /**
     * Stop jobs.
     *
     * @param jobId
     *         the job id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @GET
    @Path( "/stop/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response stopJobs( @PathParam( "jobId" ) String jobId, @QueryParam( "mode" ) String mode );

    /**
     * Pause job.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/pause/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response pauseJob( @PathParam( "jobId" ) String jobId );

    /**
     * Resume job.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/resume/{jobId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response resumeJob( @PathParam( "jobId" ) String jobId );






    /*
     * **************************************** project metadata view ****************************************.
     */

    /**
     * Transfer object.
     *
     * @param transferObjectStr
     *         the transfer object str
     *
     * @return the response
     */
    @POST
    @Path( "/transfer/object" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response transferObject( String transferObjectStr );

    /**
     * Export object.
     *
     * @return the response
     */
    @GET
    @Path( "/export/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response exportObjectUI();

    /**
     * Gets the curve X units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve X units by selection
     */
    @GET
    @Path( "/curve/list/{selectionId}/x" )
    Response getCurveXUnitsBySelection( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the curve X units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve X units by selection
     */
    @GET
    @Path( "/curve/list/{selectionId}/y" )
    Response getCurveYUnitsBySelection( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the curve list by selection.
     *
     * @param selectionId
     *         the selection id
     * @param objectJson
     *         the object json
     *
     * @return the curve list by selection
     */

    @POST
    @Path( "/curve/list/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCurveListBySelection( @PathParam( "selectionId" ) String selectionId, String objectJson );



    /*
     * **************************************** transfer views ****************************************.
     */

    /**
     * Gets the Transfer views.
     *
     * @return the Transfer views
     */
    @GET
    @Path( "/transfer" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTransferViews();

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/transfer" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveTransferView( String objectJson );

    /**
     * Update Transfer view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/transfer" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateTransferView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete Transfer view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/transfer" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteTransferView( @PathParam( "viewId" ) String viewId );

    /**
     * Sets the Transfer view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/transfer" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setTransferViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Gets the HCP list by user.
     *
     * @param userUID
     *         the user UID
     *
     * @return the HCP list by user
     */
    @GET
    @Path( "/sge/user/{userUID}/type/{solver}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHCPListByUser( @PathParam( "userUID" ) String userUID, @PathParam( "solver" ) String solver );

    /**
     * Download content of the document.
     *
     * @param objectId
     *         the object id
     *
     * @return {@link MediaType}.APPLICATION_OCTET_STREAM
     *
     * @url /{documentId}/{versionId}/download
     * @method GET
     * @see {@link MediaType}
     */
    @GET
    @Path( ConstantsDocumentServiceEndPoints.DOWNLOAD_DOCUMENT_BY_ID )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadDocument( @PathParam( ConstantsDocumentServiceEndPoints.OBJECT_ID ) UUID objectId,
            @PathParam( ConstantsDocumentServiceEndPoints.VERSION ) int version );

}
