package de.soco.software.simuspace.suscore.authentication.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;

@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface BMWOAuth2Service {

    @GET
    @Path( "/login" )
    @Produces( MediaType.APPLICATION_JSON )
    Response oauth2Authorization( @QueryParam( "state" ) String state );

    @GET
    @Path( "/access_token" )
    @Produces( MediaType.APPLICATION_JSON )
    Response oauth2FetchToken( @QueryParam( "code" ) String code, @QueryParam( "state" ) String state );

    @GET
    @Path( "/{qNumber}/token/{x_oauth_token}/refreshToken/{oauth_refresh_token}/token_id/{oauth_token_id}/internal-state/{state}/info" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response oauth2IntenalLogin( @PathParam( "qNumber" ) String qNumber, @PathParam( "x_oauth_token" ) String x_oauth_token,
            @PathParam( "oauth_refresh_token" ) String oauth_refresh_token, @PathParam( "oauth_token_id" ) String oauth_token_id,
            @PathParam( "state" ) String state );

}