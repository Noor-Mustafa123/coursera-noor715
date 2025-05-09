package de.soco.software.simuspace.susdash.hpc.service.rest;

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

/**
 * The Interface HpcService.
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface HpcService {

    /* ************************ Job Monitoring ************************ */

    /**
     * Gets data object hpc uge monitor ui.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object hpc uge monitor UI
     */
    @GET
    @Path( "/{objectId}/uge/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDataObjectHpcUgeMonitorUI( @PathParam( "objectId" ) String objectId );

    /**
     * Gets the hpc uge monitor ui view.
     *
     * @param objectId
     *         the object id
     *
     * @return the hpc uge monitor ui view
     */
    @GET
    @Path( "/{objectId}/uge/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgeMonitorUiView( @PathParam( "objectId" ) String objectId );

    /**
     * Save hpc uge monitor ui view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/uge/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response saveHpcUgeMonitorUiView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update hpc uge monitor ui view.
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
    @Path( "/{objectId}/uge/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateHpcUgeMonitorUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Delete hpc uge monitor ui view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/uge/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteHpcUgeMonitorUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets the hpc uge monitor list.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the hpc uge monitor list
     */
    @POST
    @Path( "/{objectId}/uge/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcUgeMonitorList( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Gets the uge context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the uge context
     */
    @POST
    @Path( "/{objectId}/uge/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUgeContext( @PathParam( "objectId" ) String objectId, String filterJson );

    /**
     * Gets the live interval.
     *
     * @param objectId
     *         the object id
     *
     * @return the live interval
     */
    @GET
    @Path( "/uge/getLive" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLiveInterval();

    /**
     * Gets the hpc job bread crumb by id.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the hpc job bread crumb by id
     */
    @GET
    @Path( "/{objectId}/uge/job/breadcrumb/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcJobBreadCrumbById( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Gets the hpc uge UI.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the hpc uge UI
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgeUI( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /* ************************PROPERTIES************************ */

    /**
     * Gets hpc uge properties ui.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the hpc uge properties UI
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/properties/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgePropertiesUI( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Gets the hpc uge properties ui view.
     *
     * @param objectId
     *         the object id
     *
     * @return the hpc uge properties ui view
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/properties/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgePropertiesUiView( @PathParam( "objectId" ) String objectId );

    /**
     * Save hpc uge properties ui view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/properties/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response saveHpcUgePropertiesUiView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update hpc uge properties ui view.
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
    @Path( "/{objectId}/uge/job/{id}/properties/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateHpcUgePropertiesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Delete hpc uge properties ui view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/uge/job/{id}/properties/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteHpcUgePropertiesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets the hpc uge properties list.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param objectJson
     *         the object json
     *
     * @return the hpc uge properties list
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/properties/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcUgePropertiesList( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId, String objectJson );

    /**
     * Gets the properties context.
     *
     * @param objectId
     *         the object id
     * @param filterJson
     *         the filter json
     *
     * @return the properties context
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/properties/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getPropertiesContext( @PathParam( "objectId" ) String objectId, String filterJson );

    /**
     * Gets the breadcrumb job context.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the breadcrumb job context
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/jobs/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getbreadcrumbJobContext( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Gets the breadcrumb job name context.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param jobName
     *         the job name
     *
     * @return the breadcrumb job name context
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/{jobName}/context" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getbreadcrumbJobNameContext( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId,
            @PathParam( "jobName" ) String jobName );

    /* ************************FILES************************ */

    /**
     * Gets hpc uge files ui.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the hpc uge files UI
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/files/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgeFilesUI( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Gets the hpc uge files ui view.
     *
     * @param objectId
     *         the object id
     *
     * @return the hpc uge files ui view
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/files/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgeFilesUiView( @PathParam( "objectId" ) String objectId );

    /**
     * Save hpc uge files ui view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/files/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response saveHpcUgeFilesUiView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update hpc uge files ui view.
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
    @Path( "/{objectId}/uge/job/{id}/files/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateHpcUgeFilesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Delete hpc uge files ui view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/uge/job/{id}/files/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteHpcUgeFilesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets the hpc uge files list.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param objectJson
     *         the object json
     *
     * @return the hpc uge files list
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/files/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcUgeFilesList( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId, String objectJson );

    /**
     * Gets the uge files context.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param filterJson
     *         the filter json
     *
     * @return the uge files context
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/files/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUgeFilesContext( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId, String filterJson );

    /**
     * Download file.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param fileName
     *         the file name
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/file/{fileName}/download" )
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    Response downloadFile( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId,
            @PathParam( "fileName" ) String fileName );

    /**
     * Tail file.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param name
     *         the name
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/file/{name}/tail" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response tailFile( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId, @PathParam( "name" ) String name,
            String filterJson );

    /**
     * Kill job response.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/kill" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response killJob( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );
    /* ************************MONITOR*********************** */

    /**
     * Gets job curve.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the job curve
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/plot" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobCurve( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /* ************************PENDING MESSAGES************************ */

    /**
     * Gets hpc uge pending messages ui.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     *
     * @return the hpc uge pending messages UI
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/ui" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgePendingMessagesUI( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Gets the hpc uge pending messages ui view.
     *
     * @param objectId
     *         the object id
     *
     * @return the hpc uge pending messages ui view
     */
    @GET
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getHpcUgePendingMessagesUiView( @PathParam( "objectId" ) String objectId );

    /**
     * Save hpc uge pending messages ui view.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/ui/view" )
    @Produces( MediaType.APPLICATION_JSON )
    Response saveHpcUgePendingMessagesUiView( @PathParam( "objectId" ) String objectId, String objectJson );

    /**
     * Update hpc uge pending messages ui view.
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
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateHpcUgePendingMessagesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId,
            String objectJson );

    /**
     * Delete hpc uge pending messages ui view.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/ui/view/{viewId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteHpcUgePendingMessagesUiView( @PathParam( "objectId" ) String objectId, @PathParam( "viewId" ) String viewId );

    /**
     * Gets the hpc uge pending messages list.
     *
     * @param objectId
     *         the object id
     * @param jobId
     *         the job id
     * @param objectJson
     *         the object json
     *
     * @return the hpc uge pending messages list
     */
    @POST
    @Path( "/{objectId}/uge/job/{id}/pendingmessages/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcUgePendingMessagesList( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId, String objectJson );

    /* ******************job monitoring end******************** */
    /* ****************** femzip statistics ******************* */

    /**
     * Gets the hpc fem zip type by name.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param type
     *         the job id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc fem zip type by name
     */
    @POST
    @Path( "/{objectId}/femzip/solver/{solver}/type/{type}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcFemZipChartBySolverAndType( @PathParam( "objectId" ) String objectId, @PathParam( "solver" ) String solver,
            @PathParam( "type" ) String type, String jsonPayload );

    /* ****************** HPC DashBoard statistics ******************* */

    /**
     * Gets hpc ls dyna board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc ls dyna board
     */
    @POST
    @Path( "/{objectId}/lsdyna/cpu" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcLsDynaCpuBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

    /**
     * Gets hpc ls dyna version board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc ls dyna version board
     */
    @POST
    @Path( "/{objectId}/lsdyna/version" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcLsDynaVersionBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

    /**
     * Gets hpc ls dyna status board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc ls dyna status board
     */
    @POST
    @Path( "/{objectId}/lsdyna/status" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcLsDynaStatusBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

    /**
     * Gets hpc abaqus cpu board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc abaqus cpu board
     */
    @POST
    @Path( "/{objectId}/abaqus/cpu" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcAbaqusCpuBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

    /**
     * Gets hpc abaqus version board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc abaqus version board
     */
    @POST
    @Path( "/{objectId}/abaqus/version" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcAbaqusVersionBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

    /**
     * Gets hpc abaqus status board.
     *
     * @param objectId
     *         the object id
     * @param jsonPayload
     *         the json payload
     *
     * @return the hpc abaqus status board
     */
    @POST
    @Path( "/{objectId}/abaqus/status" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcAbaqusStatusBoard( @PathParam( "objectId" ) String objectId, String jsonPayload );

}