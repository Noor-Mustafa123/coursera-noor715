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

import java.util.UUID;

/**
 * The interface Regex service.
 */
public interface RegexService {

    /**
     * Gets regex.
     *
     * @param json
     *         the json
     *
     * @return the regex
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegex( String json );

    /**
     * Gets regex edit.
     *
     * @param regexId
     *         the regex id
     *
     * @return the regex edit
     */
    @GET
    @Path( "/ui/edit/{regexId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegexEdit( @PathParam( "regexId" ) String regexId );

    /**
     * Gets regex dropdown selected.
     *
     * @param wfId
     *         the wf id
     * @param selectedOption
     *         the selected option
     *
     * @return the regex dropdown selected
     */
    @GET
    @Path( "/wf/{wfId}/dropdown/{selectedOption}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegexDropdownSelected( @PathParam( "wfId" ) String wfId, @PathParam( "selectedOption" ) String selectedOption );

    /**
     * Gets regex dropdown selected.
     *
     * @param wfId
     *         the wf id
     * @param selectedOption
     *         the selected option
     *
     * @return the regex dropdown selected
     */
    @GET
    @Path( "/wf/{wfId}/cb2/{selectedOption}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegexCb2SelectorUI( @PathParam( "wfId" ) String wfId, @PathParam( "selectedOption" ) String selectedOption );

    /**
     * Gets file content.
     *
     * @param id
     *         the id
     *
     * @return the file content
     */
    @GET
    @Path( "/{id}/content" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFileContent( @PathParam( "id" ) UUID id );

    /**
     * Gets regex context.
     *
     * @param id
     *         the id
     *
     * @return the regex context
     */
    @GET
    @Path( "/{id}/table/context" )
    Response getRegexContext( @PathParam( "id" ) UUID id );

    /**
     * Create regex ui from response.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/table/ui/create" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createRegexUIFrom( @PathParam( "id" ) String id );

    /**
     * Create regex response.
     *
     * @param selectionId
     *         the selectionId
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/table/create" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createRegex( @PathParam( "id" ) UUID selectionId, String json );

    /**
     * Edit regex ui from response.
     *
     * @param selectionId
     *         the selection id
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{selectionId}/table/ui/edit/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editRegexUIFrom( @PathParam( "selectionId" ) UUID selectionId, @PathParam( "id" ) UUID id );

    /**
     * Update regex response.
     *
     * @param selectionId
     *         the selection id
     * @param id
     *         the id
     * @param json
     *         the json
     *
     * @return the response
     */
    @PUT
    @Path( "/{selectionId}/table/edit/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateRegex( @PathParam( "selectionId" ) UUID selectionId, @PathParam( "id" ) UUID id, String json );

    /**
     * Gets regex ui.
     *
     * @param selectionId
     *         the selectionId
     *
     * @return the regex ui
     */
    @GET
    @Path( "/{id}/table/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegexUI( @PathParam( "id" ) String selectionId );

    /**
     * Gets regex list.
     *
     * @param selectionId
     *         the selection id
     * @param json
     *         the json
     *
     * @return the regex list
     */
    @POST
    @Path( "/{id}/table/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegexList( @PathParam( "id" ) UUID selectionId, String json );

    /**
     * Delete regex response.
     *
     * @param selectionId
     *         the selection id
     * @param json
     *         the json
     *
     * @return the response
     */
    @DELETE
    @Path( "/{selectionId}/table/delete" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteRegex( @PathParam( "selectionId" ) UUID selectionId, String json );

    /**
     * Gets regex.
     *
     * @param id
     *         the id
     *
     * @return the regex
     */
    @GET
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getRegex( @PathParam( "id" ) UUID id );

}
