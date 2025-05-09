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
 * @author Zeeshan jamal
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
@Path( "/" )
public interface AuditLogService {

    /**
     * Search audit log response.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsObjectServiceEndPoints.SEARCH_AUDIT_LOG )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response searchAuditLog( String objectJson );

    /**
     * List audit logs ui response.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.LIST_AUDIT_LOG_TABLE_UI )
    Response listAuditLogsUI();

    /**
     * List audit log columns response.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.LIST_AUDIT_LOG_TABLE_COLUMNS )
    Response listAuditLogColumns();

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
     * Gets the view.
     *
     * @param viewId
     *         the view id
     *
     * @return the view
     */
    @GET
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getView( @PathParam( "viewId" ) String viewId );

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
     * Gets all values for user table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for user table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForAuditLogTableColumn( @PathParam( "column" ) String column );

}
