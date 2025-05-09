package de.soco.software.simuspace.suscore.authentication.service.rest;

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

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.suscore.authentication.constant.ConstantsAuthServiceEndPoints;
import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.user.constants.ConstantsUserServiceEndPoints;

/**
 * This Interface is responsible for all the operation related to user authentication.
 *
 * @author Zeeshan jamal
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface AuthService {

    /**
     * A method for validating user login credentials and perform login.
     *
     * @param json
     *         the json containing username and password
     *
     * @return the response
     *
     * @requestPayload {"uid" : "test" , "password" : "test"}
     * @responsePayload {"data":{"X-Auth-Token":"c81d08fb00b09a879cc7963341372c72d0942244","userName":"test","userId":
     * "064bc4b6-0567-4b18-9ea6-d9256056e28e"} ,"message":{"content":"User Logged in successfully" ,"type":"SUCCESS"},"success":true}
     */
    @POST
    @Path( ConstantsAuthServiceEndPoints.LOGIN_IN )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response authenticate( String json );

    /**
     * Logout.
     *
     * @param json
     *         the logout json
     *
     * @return the response
     *
     * @method POST
     * @requestPayload {"token":"c3a3a779c0dbe8c6e17012828c90bf59b8a25d7e"}
     * @responsePayload {"success":true,"message":{"type":"SUCCESS","content":"Logged out successfully."}}
     */
    @POST
    @Path( ConstantsAuthServiceEndPoints.LOG_OUT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response logout( String json );

    /**
     * Expire job token.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsAuthServiceEndPoints.JOB_EXPIRE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response expireJobToken( String json );

    /**
     * Updates the user password.
     *
     * @param userId
     *         the userId
     * @param token
     *         the token
     * @param json
     *         the payload
     *
     * @return the response
     */
    @POST
    @Path( ConstantsUserServiceEndPoints.UPDATE_USER_PASS )
    Response updateUserPassword( @PathParam( "userId" ) String userId, @PathParam( "token" ) String token, String json );

    /**
     * Prepare job token.
     *
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param jobId
     *         the job id
     * @param authToken
     *         the auth token
     *
     * @return the response
     */
    @GET
    @Path( "/prepareJobToken/{userId}/{uid}/{jobId}/{authToken}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response prepareJobToken( @PathParam( "userId" ) String userId, @PathParam( "uid" ) String uid, @PathParam( "jobId" ) String jobId,
            @PathParam( "authToken" ) String authToken );

    /**
     * Verify user by header token.
     *
     * @return the response
     */
    @GET
    @Path( "/verify" )
    @Produces( MediaType.APPLICATION_JSON )
    Response verifyUserByHeaderToken();

    /**
     * Cb 2 login.
     *
     * @param authToken
     *         the auth token
     *
     * @return the response
     */
    @GET
    @Path( "/cb2/login/{authToken}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response cb2Login( @PathParam( "authToken" ) String authToken );

    /**
     * Authentication with oauth.
     *
     * @param id_token
     *         the id token
     * @param x_oauth_token
     *         the x oauth token
     * @param refreshToken
     *         the refresh token
     * @param state
     *         the state
     * @param json
     *         the json
     *
     * @return the response
     */
    Response authenticationWithOauth( String id_token, String x_oauth_token, String refreshToken, String state, String json );

    /**
     * Authentication with oauth.
     *
     * @param id_token
     *         the id token
     * @param x_oauth_token
     *         the x oauth token
     * @param refreshToken
     *         the refresh token
     * @param state
     *         the state
     * @param json
     *         the json
     * @param userInfoNode
     *         the user info JsonNode
     *
     * @return Response
     **/

    Response registerUserAfterOAuth( String id_token, String x_oauth_token, String refreshToken, String state, String json,
            JsonNode userInfoNode, SuSUserDirectoryEntity suSUserDirectoryEntity );

    /**
     * Verify user by header token.
     *
     * @return the response
     */
    @GET
    @Path( "/verify/{userUid}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response verifyUserByUserUid( @PathParam( "userUid" ) String userUid );

    /**
     * Cb 2 login via rest after successfull oauth login.
     *
     * @param oidcToken
     *         the oidc token
     * @param uid
     *         the uid
     *
     * @return the response
     */
    Response cb2LoginViaRest( String oidcToken, String refreshToken, String uid );

}