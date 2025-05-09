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
 * The interface Data object service.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface DataObjectService {

    /**
     * Gets audit context.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit context
     */
    @POST
    @Path( "/{objectId}/audit/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditContext( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /* ************************************ Modelfiles Views ******************************************/

    /**
     * Gets all objectmodelfiles views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all objectmodelfiles views by object id
     */
    @GET
    @Path( "/{objectId}/modelfiles" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectmodelfilesViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save objectmodelfiles view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/modelfiles" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectmodelfilesView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the view as default
     */
    @GET
    @Path( "/{objectId}/modelfiles" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setmodelfilesViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete objectmodelfiles view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/modelfiles" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectmodelfilesView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update objectmodelfiles view response.
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
    @Path( "/{objectId}/modelfiles" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectmodelfilesView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets plot list by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the plot list by object id
     */
    @GET
    @Path( "/{objectId}/plot" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPlotListByObjectId( @PathParam( "objectId" ) String objectId );

    /**
     * Gets plot list by object id and version id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the plot list by object id and version id
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/plot" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPlotListByObjectIdAndVersionId( @PathParam( "objectId" ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * Save plot list by object id response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/plot" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response savePlotListByObjectId( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Gets trace list by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the trace list by object id
     */
    @GET
    @Path( "/{objectId}/trace" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTraceListByObjectId( @PathParam( "objectId" ) String objectId );

    /**
     * Gets html object by object id.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the html object by object id
     */
    @GET
    @Path( "/{objectId}/html" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHtmlObjectByObjectId( @PathParam( "objectId" ) String objectId, @QueryParam( "lang" ) String language );

    /**
     * Gets model by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the model by object id
     */
    @GET
    @Path( "/{objectId}/model" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModelByObjectId( @PathParam( "objectId" ) String objectId );

    /**
     * Gets trace list by version and object id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the trace list by version and object id
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/trace" )
    Response getTraceListByVersionAndObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * Gets curve y units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve y units
     */
    @GET
    @Path( "/{objectId}/curve/y" )
    Response getCurveYUnits( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object value units.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object value units
     */
    @GET
    @Path( "/{objectId}/value/units" )
    Response getDataObjectValueUnits( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object value unitsby version.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object value unitsby version
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/value/units" )
    Response getDataObjectValueUnitsbyVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * Gets data object value.
     *
     * @param objectId
     *         the object id
     * @param unitJson
     *         the unit json
     *
     * @return the data object value
     */
    @POST
    @Path( "/{objectId}/value" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectValue( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String unitJson );

    /**
     * Gets data object value by version.
     *
     * @param objectId
     *         the object id
     * @param unitJson
     *         the unit json
     * @param versionId
     *         the version id
     *
     * @return the data object value by version
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/value" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectValueByVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String unitJson,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * Create object without refresing table response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{parentId}/type/{typeId}/refresh" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createObjectWithoutRefresingTable( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( "typeId" ) String typeId, String objectJson );

    /**
     * Gets ceetron 3 d object model.
     *
     * @param objectId
     *         the object id
     *
     * @return the ceetron 3 d object model
     */
    @GET
    @Path( "/{objectId}/ceetron" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCeetron3DObjectModel( @PathParam( "objectId" ) UUID objectId );

    /**
     * Gets curve x units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve x units
     */
    @GET
    @Path( "/{objectId}/curve/x" )
    Response getCurveXUnits( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets all object meta data views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all object meta data views by object id
     */
    @GET
    @Path( "/{objectId}/metadata" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectMetaDataViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save object meta data view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/metadata" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectMetaDataView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets object meta data view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the object meta data view as default
     */
    @GET
    @Path( "/{objectId}/metadata" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setObjectMetaDataViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete object meta data view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/metadata" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectMetaDataView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update object meta data view response.
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
    @Path( "/{objectId}/metadata" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectMetaDataView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /*
     * **************************************** dataobject version view ****************************************.
     */

    /**
     * Gets all data object version views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all data object version views by object id
     */
    @GET
    @Path( "/{objectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllDataObjectVersionViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save data object version view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/versions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveDataObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets data object version view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the data object version view as default
     */
    @GET
    @Path( "/{objectId}/versions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setDataObjectVersionViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete data object version view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteDataObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update data object version view response.
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
    @Path( "/{objectId}/versions" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataObjectVersionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Audit log for data object view response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/audit/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response auditLogForDataObjectView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets log list by data object.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the log list by data object
     */
    @POST
    @Path( "/{objectId}/audit/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLogListByDataObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Gets model files ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the model files ui
     */
    @GET
    @Path( "/{objectId}/modelfiles/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModelFilesUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets model files data.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the model files data
     */
    @POST
    @Path( "/{objectId}/modelfiles/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModelFilesData( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Gets model files context.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the model files context
     */
    @POST
    @Path( "/{objectId}/modelfiles/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModelFilesContext( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Gets object file info.
     *
     * @param objectId
     *         the object id
     *
     * @return the object file info
     */
    @GET
    @Path( "/{objectId}/fileInfo" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectFileInfo( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets all object audit views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all object audit views by object id
     */
    @GET
    @Path( "/{objectId}/audit" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectAuditViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save object audit view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/audit" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectAuditView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets audit view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the audit view as default
     */
    @GET
    @Path( "/{objectId}/audit" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setAuditViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete object audit view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/audit" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectAuditView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update object audit view response.
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
    @Path( "/{objectId}/audit" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectAuditView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all object permission views by object id.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the all object permission views by object id
     */
    @GET
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectPermissionViewsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Save object permission view response.
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
    Response saveObjectPermissionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Sets object permission view as default.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the object permission view as default
     */
    @GET
    @Path( "/{objectId}/permissions" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setObjectPermissionViewAsDefault( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Delete object permission view response.
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
    Response deleteObjectPermissionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

    /**
     * Update object permission view response.
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
    Response updateObjectPermissionView( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all items by object id and version.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     * @param versionId
     *         the version id
     *
     * @return the all items by object id and version
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/toitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllItemsByObjectIdAndVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            String objectFilterJson, @PathParam( "versionId" ) int versionId );

    /**
     * Gets all items by object id.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all items by object id
     */
    @POST
    @Path( "/{objectId}/toitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllItemsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectFilterJson );

    /**
     * Gets all from items by object id.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the all from items by object id
     */
    @POST
    @Path( "/{objectId}/fromitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllFromItemsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            String objectFilterJson );

    /**
     * Gets all from items by object id and version.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     * @param versionId
     *         the version id
     *
     * @return the all from items by object id and version
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/fromitems/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllFromItemsByObjectIdAndVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            String objectFilterJson, @PathParam( "versionId" ) int versionId );

    /*
     * **************************************** project dynamic ova APIs list end ****************************************.
     */

    /**
     * Gets data object preview.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object preview
     */
    @GET
    @Path( "/{objectId}/preview" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectPreview( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object version preview.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version preview
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/preview" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionPreview( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object curve.
     *
     * @param objectId
     *         the object id
     * @param unitJson
     *         the unit json
     *
     * @return the data object curve
     */
    @POST
    @Path( "/{objectId}/curve" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectCurve( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String unitJson );

    /**
     * Gets data object curve by version.
     *
     * @param objectId
     *         the object id
     * @param unitJson
     *         the unit json
     * @param versionId
     *         the version id
     *
     * @return the data object curve by version
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/curve" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectCurveByVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String unitJson,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object version curve.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version curve
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/curve" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionCurve( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object version html.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version html
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/html" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionHtml( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object version curve y.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version curve y
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/curve/y" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionCurveY( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object version curve x.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version curve x
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/curve/x" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionCurveX( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object movie.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object movie
     */
    @GET
    @Path( "/{objectId}/movie" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectMovie( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object version movie.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version movie
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/movie" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionMovie( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets object version meta data table ui.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the object version meta data table ui
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/metadata/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectVersionMetaDataTableUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Create edit data object form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/edit/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createEditDataObjectForm( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Update data object response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectJson );

    /**
     * Change status object option form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/status/ui" )
    Response changeStatusObjectOptionForm( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Change status object version option form response.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/status/ui" )
    Response changeStatusObjectVersionOptionForm( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Change status object response.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/status" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response changeStatusObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json );

    /**
     * Change status object version response.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/status" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response changeStatusObjectVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json,
            @PathParam( "versionId" ) int versionId );

    /**
     * List object item ui response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/toitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listObjectItemUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * List object item version ui response.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/toitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listObjectItemVersionUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * List object item from ui response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/fromitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listObjectItemFromUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * List object item from version ui response.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/fromitems/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listObjectItemFromVersionUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Update file to an object response.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/file" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateFileToAnObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json );

    /**
     * Gets object item context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the object item context
     */
    @POST
    @Path( "/{objectId}/toitems/context" )
    Response getObjectItemContext( @PathParam( "objectId" ) String objectId, String filterJson );

    /**
     * Gets object item from context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the object item from context
     */
    @POST
    @Path( "/{objectId}/fromitems/context" )
    Response getObjectItemFromContext( @PathParam( "objectId" ) String objectId, String filterJson );

    /**
     * Delete meta data by selection response.
     *
     * @param objectId
     *         the object id
     * @param key
     *         the key
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/metadata/{key}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteMetaDataBySelection( @PathParam( "objectId" ) String objectId, @PathParam( "key" ) String key,
            @QueryParam( "mode" ) String mode );

    /**
     * Create meta data form for edit response.
     *
     * @param objectId
     *         the object id
     * @param key
     *         the key
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/metadata/ui/edit/{key}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createMetaDataFormForEdit( @PathParam( "objectId" ) String objectId, @PathParam( "key" ) String key );

    /**
     * Update meta data to an object response.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/metadata" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateMetaDataToAnObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json );

    /**
     * Gets object meta data table ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the object meta data table ui
     */
    @GET
    @Path( "/{objectId}/metadata/ui" )
    Response getObjectMetaDataTableUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Add meta data to an object response.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/metadata" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addMetaDataToAnObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String json );

    /**
     * Gets object meta datalist.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the object meta datalist
     */
    @POST
    @Path( "/{objectId}/metadata/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectMetaDatalist( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String objectFilterJson );

    /**
     * Gets object meta datalist by version.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the object meta datalist by version
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/metadata/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getObjectMetaDatalistByVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId, String objectFilterJson );

    /**
     * Create meta data form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/metadata/ui/create" )
    @Produces( MediaType.APPLICATION_JSON )
    Response createMetaDataForm( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets object meta data context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the object meta data context
     */
    @POST
    @Path( "/{objectId}/metadata/context" )
    Response getObjectMetaDataContext( @PathParam( "objectId" ) String objectId, String filterJson );

    /**
     * Permit permission to object response.
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
    @Path( "/{objectId}/permissions/{sidId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response permitPermissionToObject( String permissionJson, @PathParam( "objectId" ) UUID objectId, @PathParam( "sidId" ) UUID sidId );

    /**
     * Show permitted users and groups for object response.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/permissions/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response showPermittedUsersAndGroupsForObject( @PathParam( "objectId" ) UUID objectId, String objectFilterJson );

    /**
     * Permission object options form response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/perm/ui/create/{objectId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response permissionObjectOptionsForm( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets update object permission ui.
     *
     * @param objectId
     *         the object id
     * @param option
     *         the option
     *
     * @return the update object permission ui
     */
    @GET
    @Path( "/{objectId}/permission/fields/{option}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getUpdateObjectPermissionUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.OPTION ) String option );

    /**
     * Change object permissions response.
     *
     * @param objectId
     *         the object id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/permission/change" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response changeObjectPermissions( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            String objectFilterJson );

    /**
     * Delete object by id response.
     *
     * @param objectId
     *         the object id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteObjectById( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @QueryParam( "mode" ) String mode );

    /**
     * Object permission table ui response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/permissions/ui" )
    Response objectPermissionTableUI( @PathParam( "id" ) UUID objectId );

    /**
     * Create object response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{parentId}/type/{typeId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createObject( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( "typeId" ) String typeId, String objectJson );

    /**
     * Create object form response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create/{parentId}/type/{typeId}" )
    Response createObjectForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( "typeId" ) String typeId );

    /**
     * Create translation form response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the response
     */
    @GET
    @Path( "/{parentId}/type/{typeId}/translation/{translations}/ui/create" )
    Response createTranslationForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( "typeId" ) String typeId, @PathParam( ConstantsObjectServiceEndPoints.TRANSLATIONS_PARAM ) String translations );

    /**
     * Update translation form response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the response
     */
    @GET
    @Path( "/{parentId}/type/{typeId}/translation/{translations}/ui/update" )
    Response updateTranslationForm( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( "typeId" ) String typeId, @PathParam( ConstantsObjectServiceEndPoints.TRANSLATIONS_PARAM ) String translations );

    /**
     * Create object form dashboard plugin response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param plugin
     *         the plugin
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create/{parentId}/type/{typeId}/plugin/{plugin}" )
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    Response createObjectFormDashboardPlugin( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "plugin" ) String plugin );

    /**
     * Gets config from object and plugin.
     *
     * @param objectId
     *         the object id
     * @param plugin
     *         the plugin
     *
     * @return the config from object and plugin
     */
    @GET
    @Path( "/{objectId}/ui/edit/plugin/{plugin}" )
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    Response getConfigFromObjectAndPlugin( @PathParam( "objectId" ) String objectId, @PathParam( "plugin" ) String plugin );

    /**
     * Create object form dashboard plugin and config response.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create/{parentId}/type/{typeId}/plugin/{plugin}/config/{config}" )
    Response createObjectFormDashboardPluginAndConfig( @PathParam( ConstantsObjectServiceEndPoints.PARENT_ID_PARAM ) String parentId,
            @PathParam( ConstantsObjectServiceEndPoints.TYPE ) String typeId, @PathParam( "plugin" ) String plugin,
            @PathParam( "config" ) String config );

    /**
     * Gets config options from object and plugin and config.
     *
     * @param objectId
     *         the object id
     * @param plugin
     *         the plugin
     * @param config
     *         the config
     *
     * @return the config options from object and plugin and config
     */
    @GET
    @Path( "/{objectId}/ui/edit/plugin/{plugin}/config/{config}" )
    Response getConfigOptionsFromObjectAndPluginAndConfig( @PathParam( "objectId" ) String objectId, @PathParam( "plugin" ) String plugin,
            @PathParam( "config" ) String config );

    /**
     * Gets data object properties.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object properties
     */
    @GET
    @Path( "/{objectId}/properties/data" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectProperties( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object
     */
    @GET
    @Path( "/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObject( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Move object response.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/move" )
    Response moveObject( String filterJson );

    /**
     * Gets object by id and version.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the object by id and version
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/properties/data" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectByIdAndVersion( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets filtered object versions list.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the filtered object versions list
     */
    @POST
    @Path( "/{objectId}/versions/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFilteredObjectVersionsList( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            String objectJson );

    /**
     * Gets tabs view data object ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the tabs view data object ui
     */
    @GET
    @Path( "/{objectId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTabsViewDataObjectUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets single data object properties ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the single data object properties ui
     */
    @GET
    @Path( "/{objectId}/properties/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSingleDataObjectPropertiesUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object version properties ui.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version properties ui
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/properties/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionPropertiesUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets data object version audit log ui.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object version audit log ui
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/audit/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionAuditLogUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) int versionId );

    /**
     * Gets object version audit log list.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param objectJson
     *         the object json
     *
     * @return the object version audit log list
     */
    @POST
    @Path( "/{objectId}/version/{versionId}/audit/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectVersionAuditLogList( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId, String objectJson );

    /**
     * Gets single data object version ui.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the single data object version ui
     */
    @GET
    @Path( "/{objectId}/version/{versionId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSingleDataObjectVersionUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectServiceEndPoints.VERSION_ID_PARAM ) int versionId );

    /**
     * Gets data object versions ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object versions ui
     */
    @GET
    @Path( "/{objectId}/versions/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionsUI( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId );

    /**
     * Gets data object versions context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the data object versions context
     */
    @POST
    @Path( "/{objectId}/versions/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionsContext( @PathParam( ConstantsObjectServiceEndPoints.OBJ_ID_PARAM ) String objectId, String filterJson );

}
