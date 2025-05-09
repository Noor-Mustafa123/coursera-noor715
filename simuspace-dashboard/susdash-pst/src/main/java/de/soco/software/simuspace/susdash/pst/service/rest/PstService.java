package de.soco.software.simuspace.susdash.pst.service.rest;

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
 * The interface Pst service.
 */
public interface PstService {

    /**
     * Gets pst test chart.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst test chart
     */
    @GET
    @Path( "/{objectId}/planning/test" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPstTestChart( @PathParam( "objectId" ) String objectId );

    /**
     * Gets pst test archive.
     *
     * @param objectId
     *         the object id
     * @param archive
     *         the archive
     *
     * @return the pst test archive
     */
    @GET
    @Path( "/{objectId}/planning/test/{archive}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPstTestArchive( @PathParam( "objectId" ) String objectId, @PathParam( "archive" ) String archive );

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst bench chart
     */
    @GET
    @Path( "/{objectId}/planning/bench" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPstBenchChart( @PathParam( "objectId" ) String objectId );

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     * @param archive
     *         the archive
     *
     * @return the pst bench chart
     */
    @GET
    @Path( "/{objectId}/planning/bench/{archive}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPstBenchArchive( @PathParam( "objectId" ) String objectId, @PathParam( "archive" ) String archive );

    /**
     * Couple test schedule response.
     *
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/couple" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response coupleTestSchedule( @PathParam( "objectId" ) String objectId, String payload );

    /**
     * Edit test schedule response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the pst json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/edit" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editTestSchedule( @PathParam( "objectId" ) String objectId, String pstJson );

    /**
     * Edit test schedule ui response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the pst json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/planning/edit/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editTestScheduleUI( @PathParam( "objectId" ) String objectId, String pstJson );

    /**
     * Add test schedule response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the pst json
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/add" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addTestSchedule( @PathParam( "objectId" ) String objectId, String pstJson );

    /**
     * Add test schedule ui response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/planning/add/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addTestScheduleUI( @PathParam( "objectId" ) String objectId );

    /**
     * Update pre req response.
     *
     * @param objectId
     *         the object id
     * @param preReq
     *         the pre req
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/prereq" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updatePreReq( @PathParam( "objectId" ) String objectId, String preReq );

    /**
     * Update dashboard data response.
     *
     * @param objectId
     *         the object id
     * @param action
     *         the action
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/update/{action}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDashboardData( @PathParam( "objectId" ) String objectId, @PathParam( "action" ) String action );

    /**
     * Create dashboard data response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/create" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDashboardData( @PathParam( "objectId" ) String objectId );

    /**
     * Gets pst planning views.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst planning views
     */
    @GET
    @Path( "/{objectId}/planning/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPstPlanningViews( @PathParam( "objectId" ) String objectId );

    /**
     * Create pst planning view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/planning/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createPstPlanningView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update pst planning view response.
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
    @Path( "/{objectId}/planning/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updatePstPlanningView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete pst planning view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/planning/ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deletePstPlanningView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete pst planning view response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/planning/export/ace" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response exportAceLunge( @PathParam( "objectId" ) String objectId );

    /**
     * Export differences response.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/planning/export/differences/download" )
    @Produces( { MediaType.APPLICATION_OCTET_STREAM } )
    Response exportDifferences( @PathParam( "objectId" ) String objectId );

    /**
     * Export differences link response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/planning/export/differences/link" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response exportDifferencesLink( @PathParam( "objectId" ) String objectId );

    /**
     * Create pst planning archive response.
     *
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @PUT
    @Path( "/{objectId}/planning/archive/add" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createPstPlanningArchive( @PathParam( "objectId" ) String objectId, String payload );

    /**
     * Create pst planning archive response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/planning/archive/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getArchivesList( @PathParam( "objectId" ) String objectId );

    /**
     * Gets legend.
     *
     * @param objectId
     *         the object id
     *
     * @return the legend
     */
    @GET
    @Path( "/{objectId}/planning/legend" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLegend( @PathParam( "objectId" ) String objectId );

    /**
     * Gets machine.
     *
     * @param objectId
     *         the object id
     *
     * @return the machine
     */
    @GET
    @Path( "/{objectId}/planning/machine" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getMachine( @PathParam( "objectId" ) String objectId );

}
