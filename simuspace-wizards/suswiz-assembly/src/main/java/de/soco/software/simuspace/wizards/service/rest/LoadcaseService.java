package de.soco.software.simuspace.wizards.service.rest;

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

import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * The Interface LoadcaseService.
 */
public interface LoadcaseService {

    /**
     * Gets the loadcase UI.
     *
     * @return the loadcase UI
     */
    @GET
    @Path( "/loadcase/ui/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLoadcaseUI();

    /**
     * Gets the loadcase data.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the loadcase data
     */
    @POST
    @Path( "/loadcase/list/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLoadcaseData( String objectFilterJson );

    /**
     * Gets the loadcase context menu.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the loadcase context menu
     */
    @POST
    @Path( "/loadcase/context/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLoadcaseContextMenu( String objectFilterJson );

    /**
     * Creates the loadcase UI.
     *
     * @return the response
     */
    @GET
    @Path( "/loadcase/ui/create" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createLoadcaseUI();

    /**
     * Creates the loadcase.
     *
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( "/loadcase" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createLoadcase( String payload );

    /**
     * Edits the loadcase UI.
     *
     * @param loadcaseId
     *         the loadcase id
     *
     * @return the response
     */
    @GET
    @Path( "/loadcase/ui/edit/{loadcaseId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editLoadcaseUI( @PathParam( "loadcaseId" ) String loadcaseId );

    /**
     * Update loadcase.
     *
     * @param loadcaseId
     *         the loadcase id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( "/loadcase/{loadcaseId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLoadcase( @PathParam( "loadcaseId" ) String loadcaseId, String payload );

    /**
     * Delete loadcase.
     *
     * @param loadcaseId
     *         the loadcase id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( "/loadcase/{loadcaseId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLoadcase( @PathParam( "loadcaseId" ) UUID loadcaseId, @QueryParam( "mode" ) String mode );

    /**
     * Gets loadcase views.
     *
     * @return the all deleted object views
     */
    @GET
    @Path( "/loadcase" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLoadcaseViews();

    /**
     * Save loadcase view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/loadcase" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLoadcaseView( String objectJson );

    /**
     * Sets the loadcase view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/loadcase" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setLoadcaseViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete loadcase view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/loadcase" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLoadcaseView( @PathParam( "viewId" ) String viewId );

    /**
     * Update loadcase view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/loadcase" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLoadcaseView( @PathParam( "viewId" ) String viewId, String objectJson );

}
