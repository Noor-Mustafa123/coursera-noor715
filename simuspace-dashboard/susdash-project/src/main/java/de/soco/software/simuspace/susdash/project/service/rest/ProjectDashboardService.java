package de.soco.software.simuspace.susdash.project.service.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ProjectDashboardService {

    /**
     * Generate materials project tree response.
     *
     * @param objectId
     *         the object id
     * @param dashBoardLicenseJson
     *         the dash board license json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/chart" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectStructureChart( @PathParam( "objectId" ) String objectId, String dashBoardLicenseJson );

    /**
     * Gets materials project tree in table form ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the materials project tree in table form ui
     */
    @GET
    @Path( "/{objectId}/table/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectStructureUI( @PathParam( "objectId" ) String objectId );

    /**
     * Gets materials project tree in table form.
     *
     * @param objectId
     *         the object id
     * @param filters
     *         the filters
     *
     * @return the materials project tree in table form
     */
    @POST
    @Path( "/{objectId}/table/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectStructureTable( @PathParam( "objectId" ) String objectId, String filters );

    /**
     * Gets object views.
     *
     * @param ojectId
     *         the oject id
     *
     * @return the object views
     */
    @GET
    @Path( "/{objectId}/table/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectStructureViews( @PathParam( "objectId" ) String ojectId );

    /**
     * Create materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/table/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createProjectStructureView( @PathParam( "objectId" ) String ojectId, String objectJson );

    /**
     * Update materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/table/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectStructureView( @PathParam( "objectId" ) String ojectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Delete materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/table/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectStructureView( @PathParam( "objectId" ) String ojectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets materials context.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the materials context
     */
    @POST
    @Path( "/{objectId}/table/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectStructureContext( @PathParam( "objectId" ) String objectId, String json );

    /**
     * Gets materials project tree in table form ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the materials project tree in table form ui
     */
    @GET
    @Path( "/{objectId}/lifecycle/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectLifecycleUI( @PathParam( "objectId" ) String objectId );

    /**
     * Gets materials project tree in table form.
     *
     * @param objectId
     *         the object id
     * @param filters
     *         the filters
     *
     * @return the materials project tree in table form
     */
    @POST
    @Path( "/{objectId}/lifecycle/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectLifecycleTable( @PathParam( "objectId" ) String objectId, String filters );

    /**
     * Gets object views.
     *
     * @param ojectId
     *         the oject id
     *
     * @return the object views
     */
    @GET
    @Path( "/{objectId}/lifecycle/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectLifecycleViews( @PathParam( "objectId" ) String ojectId );

    /**
     * Create materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/lifecycle/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createProjectLifecycleView( @PathParam( "objectId" ) String ojectId, String objectJson );

    /**
     * Update materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/lifecycle/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectLifecycleView( @PathParam( "objectId" ) String ojectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Delete materials view response.
     *
     * @param ojectId
     *         the oject id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/lifecycle/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteProjectLifecycleView( @PathParam( "objectId" ) String ojectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets materials context.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the materials context
     */
    @POST
    @Path( "/{objectId}/lifecycle/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectLifecycleContext( @PathParam( "objectId" ) String objectId, String json );

    /**
     * Gets materials context.
     *
     * @param objectId
     *         the object id
     * @param json
     *         the json
     *
     * @return the materials context
     */
    @PUT
    @Path( "/{objectId}/selection/{selectionId}/lifecycle/{statusId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response changeProjectLifecycle( @PathParam( "objectId" ) String objectId, @PathParam( "selectionId" ) String selectionId,
            @PathParam( "statusId" ) String statusId );

    @PUT
    @Path( "/{objectId}/update" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDashboardCache( @PathParam( "objectId" ) String objectId );

    @GET
    @Path( "/{objectId}/lastModified" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLastModified( @PathParam( "objectId" ) String objectId );

}
