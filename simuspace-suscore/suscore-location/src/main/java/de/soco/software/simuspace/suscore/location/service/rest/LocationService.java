package de.soco.software.simuspace.suscore.location.service.rest;

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

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;

/**
 * This Interface is responsible for providing all the services related to locations.
 *
 * @author M.Nasir.Farooq
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface LocationService {

    /**
     * Adds the new location.
     *
     * @param locationJson
     *         the location json
     *
     * @return the response
     */
    @POST
    @Path( "/add" )
    Response addNewLocation( String locationJson );

    /**
     * List user table UI including columns and view.
     *
     * @return the response
     */
    @GET
    @Path( "/ui" )
    Response listLocationUI();

    /**
     * To view single location information.
     *
     * @return the response
     */
    @GET
    @Path( "/ui/single" )
    Response singleLocationUI();

    /**
     * List user table UI including columns and view.
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create" )
    Response createLocationUI();

    /**
     * Creates the location.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createLocation( String objectJson );

    /**
     * Gets the location.
     *
     * @param id
     *         the id
     *
     * @return the location
     */
    @GET
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLocation( @PathParam( "id" ) String id );

    /**
     * Gets the all locations.
     *
     * @param locationFilterJson
     *         the location filter json
     *
     * @return the all locations
     */
    @POST
    @Path( "/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLocations( String locationFilterJson );

    /**
     * Delete location.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLocation( @PathParam( "id" ) String id );

    /**
     * Update view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLocation( String objectJson );

    /**
     * List location workflow UI.
     *
     * @return the response
     */
    @GET
    @Path( "workflow/ui" )
    Response listLocationWorkflowUI();

    /**
     * Gets the all locations include internal.
     *
     * @param locationFilterJson
     *         the location filter json
     *
     * @return the all locations include internal
     */
    @POST
    @Path( "/workflow/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLocationsIncludeInternal( String locationFilterJson );

    /**
     * Gets the all locations with selection.
     *
     * @return the all locations with selection
     */
    @POST
    @Path( "/workflow/list/selection/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLocationsWithSelection( @PathParam( "selectionId" ) String selectionId, String locationFilterJson );

    /**
     * Gets the location list with selection id.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the location list with selection id
     */
    @GET
    @Path( "/list/{selectionId : .+}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLocationListWithSelectionId( @PathParam( "selectionId" ) String selectionId );

    /**
     * Gets the local location list.
     *
     * @return the local location list
     */
    @GET
    @Path( "/local" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLocalLocationList();

    /**
     * Get staging path from configs.
     *
     * @return the response
     */
    @GET
    @Path( "/stagingpath" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLocalStagingPath();

    /**
     * Get properties manager.
     *
     * @return the response
     */
    @GET
    @Path( "/karafpath" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getKarafPath();

    /**
     * Get properties manager.
     *
     * @return the response
     */
    @GET
    @Path( "/pythonpath" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPythonPath();

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( "/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getContextRouter( String filterJson );

    /**
     * Edits the location form.
     *
     * @param locationId
     *         the location id
     *
     * @return the response
     */
    @GET
    @Path( "/ui/edit/{locationId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editLocationForm( @PathParam( "locationId" ) UUID locationId );

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
     * Gets the all workflow views.
     *
     * @return the all workflow views
     */
    @GET
    @Path( "/workflow" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllWorkflowViews();

    /**
     * Gets the workflow view.
     *
     * @param viewId
     *         the view id
     *
     * @return the view
     */
    @GET
    @Path( "/workflow" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowView( @PathParam( "viewId" ) String viewId );

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/workflow" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveWorkflowView( String objectJson );

    /**
     * Sets the view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/workflow" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setWorkflowViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/workflow" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteWorkflowView( @PathParam( "viewId" ) String viewId );

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
    @Path( "/workflow" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWorkflowView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all values for locations table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for locations table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForLocationsTableColumn( @PathParam( "column" ) String column );

}
