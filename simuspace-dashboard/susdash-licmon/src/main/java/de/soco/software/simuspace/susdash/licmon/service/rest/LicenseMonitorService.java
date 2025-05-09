package de.soco.software.simuspace.susdash.licmon.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * The Interface LicenseMonitorService.
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface LicenseMonitorService {

    /**
     * Gets the fem settings.
     *
     * @param objectId
     *         the object id
     *
     * @return the fem settings
     */
    @GET
    @Path( "{objectId}/fem" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFemSettings( @PathParam( "objectId" ) String objectId );

    /**
     * Gets the msc settings.
     *
     * @param objectId
     *         the object id
     *
     * @return the msc settings
     */
    @GET
    @Path( "{objectId}/msc" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getMscSettings( @PathParam( "objectId" ) String objectId );

    /**
     * Generate FEM license log.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param dateRange
     *         the date range
     *
     * @return the response
     */
    @POST
    @Path( "{objectId}/fem/{solver}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response generateFEMLicenseLog( @PathParam( "objectId" ) String objectId, @PathParam( "solver" ) String solver, String dateRange );

    /**
     * Generate MSC license log.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param dateRange
     *         the date range
     *
     * @return the response
     */
    @POST
    @Path( "{objectId}/msc/{solver}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response generateMSCLicenseLog( @PathParam( "objectId" ) String objectId, @PathParam( "solver" ) String solver, String dateRange );

    /**
     * Generate VMCL log.
     *
     * @param objectId
     *         the object id
     * @param solver
     *         the solver
     * @param dateRange
     *         the date range
     *
     * @return the response
     */
    @POST
    @Path( "{objectId}/vmcl/{solver}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response generateVMCLLog( @PathParam( "objectId" ) String objectId, @PathParam( "solver" ) String solver, String dateRange );

}