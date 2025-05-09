package de.soco.software.simuspace.server.service.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Interface ParserService.
 *
 * @author noman arshad
 */
public interface ParserService {

    /**
     * Gets the parser.
     *
     * @param json
     *         the json
     *
     * @return the parser
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParser( String json );

    /**
     * Gets the parser available ui.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser available ui
     */
    @GET
    @Path( "/{parserId}/available/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserAvailableUi( @PathParam( "parserId" ) String parserId );

    /**
     * Gets the parser selected ui.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser selected ui
     */
    @GET
    @Path( "/{parserId}/selected/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserSelectedUi( @PathParam( "parserId" ) String parserId );

    /**
     * Gets the parser edit.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser edit
     */
    @GET
    @Path( "/ui/edit/{parserId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserEdit( @PathParam( "parserId" ) String parserId );

    /**
     * Gets the parser available list.
     *
     * @param parserId
     *         the parser id
     * @param jsonFilter
     *         the json filter
     *
     * @return the parser available list
     */
    @POST
    @Path( "/{parserId}/available/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserAvailableList( @PathParam( "parserId" ) String parserId, String jsonFilter );

    /**
     * Gets the parser selected list.
     *
     * @param parserId
     *         the parser id
     * @param jsonFilter
     *         the json filter
     *
     * @return the parser selected list
     */
    @POST
    @Path( "/{parserId}/selected/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserSelectedList( @PathParam( "parserId" ) String parserId, String jsonFilter );

    /**
     * Gets the parser ui.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser ui
     */
    @GET
    @Path( "/{parserId}/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserUi( @PathParam( "parserId" ) String parserId );

    /**
     * Gets the parser list.
     *
     * @param parserId
     *         the parser id
     * @param jsonFilter
     *         the json filter
     *
     * @return the parser list
     */
    @POST
    @Path( "/{parserId}/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserList( @PathParam( "parserId" ) String parserId, String jsonFilter );

    /**
     * Gets the parser object selected UI.
     *
     * @param parserId
     *         the parser id
     * @param objectSelected
     *         the object selected
     *
     * @return the parser object selected UI
     */
    @GET
    @Path( "/{parserId}/objecttype/{objectSelected}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserObjectSelectedUI( @PathParam( "parserId" ) String parserId, @PathParam( "objectSelected" ) String objectSelected );

    /**
     * Gets the parser object CB 2 selector UI.
     *
     * @param parserId
     *         the parser id
     * @param typeSelected
     *         the type selected
     *
     * @return the parser object selected UI
     */
    @GET
    @Path( "/{parserId}/cb2type/{typeSelected}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserObjectCB2SelectorUI( @PathParam( "parserId" ) String parserId, @PathParam( "typeSelected" ) String typeSelected );

    /**
     * Gets the parser object selected UI for CB 2 element.
     *
     * @param parserId
     *         the parser id
     * @param objectSelected
     *         the object selected
     *
     * @return the parser object selected UI for CB 2 element
     */
    @GET
    @Path( "/objecttype/{objectSelected}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserObjectSelectedUIForCB2Element( @PathParam( "objectSelected" ) String objectSelected );

    /**
     * Gets the parser object available context.
     *
     * @param parserId
     *         the parser id
     * @param json
     *         the json
     *
     * @return the parser object available context
     */
    @POST
    @Path( "/{parserId}/available/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserObjectAvailableContext( @PathParam( "parserId" ) String parserId, String json );

    /**
     * Gets the parser object selected context.
     *
     * @param parserId
     *         the parser id
     * @param json
     *         the json
     *
     * @return the parser object selected context
     */
    @POST
    @Path( "/{parserId}/selected/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserObjectSelectedContext( @PathParam( "parserId" ) String parserId, String json );

    /**
     * Adds the entry to parser selected list.
     *
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/{parserId}/available/add/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addEntryToParserSelectedList( @PathParam( "parserId" ) String parserId, @PathParam( "selectionId" ) String selectionId );

    /**
     * Save entry to parser available and selected list.
     *
     * @param parserId
     *         the parser id
     * @param json
     *         the json
     *
     * @return the response
     */
    @PUT
    @Path( "/{parserId}/selected" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveEntryToParserAvailableAndSelectedList( @PathParam( "parserId" ) String parserId, String json );

    /**
     * Gets the edits the form for selected list.
     *
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the edits the form for selected list
     */
    @GET
    @Path( "/{parserId}/selected/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getEditFormForSelectedList( @PathParam( "parserId" ) String parserId, @PathParam( "selectionId" ) String selectionId );

    /**
     * Removes the entry from parser selected list.
     *
     * @param parserId
     *         the parser id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( "/{parserId}/selected/remove/{selectionId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response removeEntryFromParserSelectedList( @PathParam( "parserId" ) String parserId,
            @PathParam( "selectionId" ) String selectionId );

    /*
     * //////////////////////////////// VIEW APIS //////////////////////////////////////////////.
     */

    /**
     * Gets the all parser selected views.
     *
     * @param parserId
     *         the parser id
     *
     * @return the all parser selected views
     */
    @GET
    @Path( "/{parserId}/selected/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllParserSelectedViews( @PathParam( "parserId" ) String parserId );

    /**
     * Gets the all parser available views.
     *
     * @param parserId
     *         the parser id
     *
     * @return the all parser available views
     */
    @GET
    @Path( "/{parserId}/available/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllParserAvailableViews( @PathParam( "parserId" ) String parserId );

    /**
     * Save parser selected view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{parserId}/selected/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveParserSelectedView( String objectJson );

    /**
     * Save parser available view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{parserId}/available/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveParserAvailableView( String objectJson );

    /**
     * Delete parser selected view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{parserId}/selected/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteParserSelectedView( @PathParam( "viewId" ) String viewId );

    /**
     * Delete parser available view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{parserId}/available/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteParserAvailableView( @PathParam( "viewId" ) String viewId );

    /**
     * Update parser selected view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{parserId}/selected/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateParserSelectedView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Update parser available view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{parserId}/available/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateParserAvailableView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Sets the parser selected view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{parserId}/selected/ui/view/{viewId}/default" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setParserSelectedViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Sets the parser available view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{parserId}/available/ui/view/{viewId}/default" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setParserAvailableViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Sets the parser available view as default.
     *
     * @param parserId
     *         the parser id
     * @param filepath
     *         the filepath
     *
     * @return the response
     */
    @POST
    @Path( "/{parserId}/selected/list/value" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setParserAvailableViewAsDefault( @PathParam( "parserId" ) String parserId, String filepath );

    /**
     * Gets the parser selected file path by parse id.
     *
     * @param parserId
     *         the parser id
     *
     * @return the parser selected file path by parse id
     */
    @GET
    @Path( "/{parserId}/filepath" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getParserSelectedFilePathByParserId( @PathParam( "parserId" ) String parserId );

}
