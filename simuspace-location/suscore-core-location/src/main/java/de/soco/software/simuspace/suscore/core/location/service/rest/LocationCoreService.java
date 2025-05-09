package de.soco.software.simuspace.suscore.core.location.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

/**
 * This Interface is responsible for providing all the services related to remote locations.
 *
 * @author M.Nasir.Farooq
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface LocationCoreService {

    /**
     * Checks if is up.
     *
     * @param token
     *         the token
     *
     * @return the response
     */
    @GET
    @Path( "/isup" )
    Response isUp( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token );

    /**
     * Checks if is path exist.
     *
     * @param token
     *         the token
     * @param pathJson
     *         the path json
     *
     * @return the response
     */
    @POST
    @Path( "/isexists" )
    Response isPathExists( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token, String pathJson );

    /**
     * Export vault file.
     *
     * @param token
     *         the token
     * @param transferObjStr
     *         the transfer obj str
     *
     * @return the response
     */
    @POST
    @Path( "/export/vault/file" )
    Response exportVaultFile( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token, String transferObjStr );

    /**
     * Export staging file.
     *
     * @param locationAuthToken
     *         the token
     * @param transferObjStr
     *         the transfer obj str
     *
     * @return the response
     */
    @POST
    @Path( "/export/staging/file" )
    Response exportStagingFile( @HeaderParam( ConstantRequestHeader.USER_UID ) String userUid,
            @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String locationAuthToken, String transferObjStr );

    /**
     * Download vault file.
     *
     * @param token
     *         the token
     * @param id
     *         the id
     * @param version
     *         the version
     *
     * @return the response
     */
    @GET
    @Path( "/download/vault/file/{id}/version/{versionId}" )
    Response downloadVaultFile( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token, @PathParam( "id" ) UUID id,
            @PathParam( "versionId" ) int version );

    /**
     * Delete vault file.
     *
     * @param token
     *         the token
     * @param transferObjStr
     *         the transfer obj str
     *
     * @return the response
     */
    @POST
    @Path( "/delete/file" )
    Response deleteVaultFile( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token, String transferObjStr );

    /**
     * Run workflow.
     *
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response
     */
    @POST
    @Path( "/run/job" )
    @Produces( MediaType.APPLICATION_JSON )
    Response runWorkflow( String jobParametersString );

    /**
     * Run system workflow.
     *
     * @param jobParametersString
     *         the job parameters string
     *
     * @return the response
     */
    @POST
    @Path( "/run/system/job" )
    @Produces( MediaType.APPLICATION_JSON )
    Response runSystemWorkflow( String jobParametersString );

    /**
     * Gets the file list.
     *
     * @param locationAuthToken
     *         the token
     * @param pathJson
     *         the path json
     *
     * @return the file list
     */
    @POST
    @Path( "/files" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getFileList( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String locationAuthToken, String pathJson );

    /**
     * Gets the user home.
     *
     * @param token
     *         the token
     * @param pathJson
     *         the path json
     *
     * @return the user home
     */
    @POST
    @Path( "/userHome" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUserHome( @HeaderParam( ConstantRequestHeader.AUTHORIZATION ) String token, String pathJson );

}
