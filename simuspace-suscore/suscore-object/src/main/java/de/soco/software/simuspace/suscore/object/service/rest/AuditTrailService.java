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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;

/**
 * The Interface AuditTrailService.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface AuditTrailService {

    /**
     * Gets the audit trail by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail by object id
     */
    @GET
    @Path( "/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailByObjectId( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Gets the audit trail by object id outs.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail by object id outs
     */
    @GET
    @Path( "/selection/{selectionId}/graph/outgoing" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailByObjectIdOuts( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Gets the audit trail by object id ins.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail by object id ins
     */
    @GET
    @Path( "/selection/{selectionId}/graph/incoming" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailByObjectIdIns( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Gets the audit trail details UI outs.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail details UI outs
     */
    @GET
    @Path( "/selection/{selectionId}/data/outgoing/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailDetailsUIOuts( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Gets the audit trail details U ins.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail details U ins
     */
    @GET
    @Path( "/selection/{selectionId}/data/incoming/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailDetailsUIns( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Gets the audit trail list outs details by object id.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the audit trail list outs details by object id
     */
    @POST
    @Path( "/selection/{selectionId}/data/outgoing/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailListOutsDetailsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            String filter );

    /**
     * Gets the audit trail list ins details by object id.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the audit trail list ins details by object id
     */
    @POST
    @Path( "/selection/{selectionId}/data/incoming/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailListInsDetailsByObjectId( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            String filter );

    /**
     * Gets the audit trail context ins.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the audit trail context ins
     */
    @POST
    @Path( "/selection/{selectionId}/data/incoming/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailContextIns( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            String filterJson );

    /**
     * Gets the audit trail context outs.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the audit trail context outs
     */
    @POST
    @Path( "/selection/{selectionId}/data/outgoing/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailContextOuts( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            String filterJson );

    /**
     * Gets the audit trail details UI view.
     *
     * @param objectId
     *         the object id
     *
     * @return the audit trail details UI view
     */
    @GET
    @Path( "/selection/{selectionId}/data/{param: outgoing|incoming}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAuditTrailDetailsUIView( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId );

    /**
     * Audit trail save object view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/selection/{selectionId}/data/{param: outgoing|incoming}" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response auditTrailSaveObjectView( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            String objectJson );

    /**
     * Audit trail update object view.
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
    @Path( "/selection/{selectionId}/data/{param: outgoing|incoming}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response auditTrailUpdateObjectView( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Audit trail delete object view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/selection/{selectionId}/data/{param: outgoing|incoming}" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response auditTrailDeleteObjectView( @PathParam( ConstantsObjectServiceEndPoints.SELECTION_ID_PARAM ) String objectId,
            @PathParam( "viewId" ) String viewId );

}
