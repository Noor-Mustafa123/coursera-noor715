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
public interface CB2AuthService {

    @GET
    @Path( "/login" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response authorize();

    @GET
    @Path( "/access_token" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response fetchAccessToken( @QueryParam( "code" ) String code, @QueryParam( "state" ) String state );

    @GET
    @Path( "/accessToken/{access_token}/refreshToken/{refresh_token}/token_id/{token_id}/state/{state}" )
    Response logUserIntoCB2( @PathParam( "access_token" ) String accessToken, @PathParam( "refresh_token" ) String refreshToken,
            @PathParam( "token_id" ) String tokenId, @PathParam( "state" ) String state );

}
