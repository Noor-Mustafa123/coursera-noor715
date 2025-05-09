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
 * The Interface TemplateService.
 *
 * @author Fahad Rafi
 */
public interface TemplateService {

    /**
     * Gets the template.
     *
     * @param json
     *         the json
     *
     * @return the template
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTemplate( String json );

    /**
     * Gets the template UI.
     *
     * @param id
     *         the id
     *
     * @return the template UI
     */
    @GET
    @Path( "/{id}/table/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTemplateUI( @PathParam( "id" ) String id );

    /**
     * Gets the template list.
     *
     * @param selectionId
     *         the selectionId
     * @param json
     *         the json
     *
     * @return the template list
     */
    @POST
    @Path( "/{id}/table/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTemplateList( @PathParam( "id" ) UUID selectionId, String json );

    /**
     * Gets the template edit.
     *
     * @param templateId
     *         the template id
     *
     * @return the template edit
     */
    @GET
    @Path( "/ui/edit/{templateId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTemplateEdit( @PathParam( "templateId" ) String templateId );

    /**
     * Creates the template UI from.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/table/ui/create" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createTemplateUIFrom( @PathParam( "id" ) String id );

    /**
     * Creates the template.
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
    Response createTemplate( @PathParam( "id" ) UUID selectionId, String json );

    /**
     * Gets the file content.
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
     * Delete template.
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
    Response deleteTemplate( @PathParam( "selectionId" ) UUID selectionId, String json );

    /**
     * Edits the template UI from.
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
    Response editTemplateUIFrom( @PathParam( "selectionId" ) UUID selectionId, @PathParam( "id" ) UUID id );

    /**
     * Update template.
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
    Response updateTemplate( @PathParam( "selectionId" ) UUID selectionId, @PathParam( "id" ) UUID id, String json );

}
