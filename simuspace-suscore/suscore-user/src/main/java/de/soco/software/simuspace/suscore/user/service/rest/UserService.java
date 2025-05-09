package de.soco.software.simuspace.suscore.user.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.user.constants.ConstantsUserServiceEndPoints;

/**
 * This Interface is responsible for all the operation Of User.
 *
 * @author Ahsan Khan
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface UserService {

    /**
     * Creates the user.
     *
     * @param dirID
     *         the dir id
     * @param userJson
     *         the user json
     *
     * @return the user entity
     */
    @POST
    @Path( ConstantsUserServiceEndPoints.CREATE_USER_ON_BEHALF_OF_DIRECTORY )
    @Produces( MediaType.APPLICATION_JSON )
    Response createUser( @PathParam( ConstantsUserServiceEndPoints.DIRECTORY_ID ) UUID dirID, String userJson );

    /**
     * Updates the user.
     *
     * @param json
     *         the json
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     */
    @PUT
    @Path( ConstantsUserServiceEndPoints.UPDATE_USER )
    Response updateUser( String json );

    /**
     * Updates the user.
     *
     * @param json
     *         the json
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     */
    @PUT
    @Path( ConstantsUserServiceEndPoints.UPDATE_USER_PROFILE )
    Response updateUserProfile( String json );

    /**
     * Delete user.
     *
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsUserServiceEndPoints.DELETE_MODULE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteUser( @PathParam( "id" ) String id, @QueryParam( "mode" ) String mode );

    /**
     * Edits the user form.
     *
     * @param userId
     *         the user id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.EDIT_USER_UI )
    Response editUserForm( @PathParam( "userId" ) UUID userId );

    /**
     * Edits the user form.
     *
     * @param userId
     *         the user id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.EDIT_USER_PROFILE_UI )
    Response editUserProfileForm( @PathParam( "userId" ) UUID userId );

    /**
     * List user table columns
     *
     * @return response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.LIST_USER_TABLE_COLUMNS )
    Response listUsersColumns();

    /**
     * List user table UI including columns and view
     *
     * @return response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.LIST_USER_TABLE_UI )
    Response listUsersUI();

    /**
     * generate create user form.
     *
     * @param dirId
     *         the dir id
     * @param userUid
     *         the user uid
     *
     * @return response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.CREATE_USER_BY_DIR_ID )
    Response createUserForm( @PathParam( "dirId" ) String dirId, @PathParam( "userUid" ) String userUid );

    /**
     * This service is used to get a user profile by user-id.
     *
     * @param userId
     *         the user id
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.GET_USER_BY_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUserById( @PathParam( "userId" ) UUID userId ) throws SusException;

    /**
     * Gets all users as a paginated list.
     *
     * @return all the users
     */
    @POST
    @Path( ConstantsUserServiceEndPoints.GET_ALL_USERS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllUsers( String userFilterJson );

    /**
     * Gets current logged in user.
     *
     * @return current user
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.CURRENT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCurrentUser();

    /**
     * Gets current logged in user.
     *
     * @return current user
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.CURRENT_UID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getCurrentUserUid();

    /**
     * Gets all languages .
     *
     * @return all the languages
     */
    @POST
    @Path( ConstantsUserServiceEndPoints.GET_ALL_LANGUAGES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLanguages();

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( ConstantsUserServiceEndPoints.GET_CONTEXT_ROUTER )
    Response getContextRouter( String filterJson );

    /**
     * To View Single User Profile
     *
     * @return response
     */
    @GET
    @Path( ConstantsUserServiceEndPoints.VIEW_SINGLE_USER_UI )
    Response singleUserUI();

    /**
     * Gets the all views.
     *
     * @return the all views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllViews();

    /**
     * Gets the view.
     *
     * @param viewId
     *         the view id
     *
     * @return the view
     */
    @GET
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getView( @PathParam( "viewId" ) String viewId );

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveView( String objectJson );

    /**
     * Sets the view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteView( @PathParam( "viewId" ) String viewId );

    /**
     * Update view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets user token map.
     *
     * @return the user token map
     */
    @GET
    @Path( "/tokenizeMap" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUserTokenMap();

    @GET
    @Path( "/{userId}/update/{propertyKey}/{propertyValue}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateUserProperty( @PathParam( "userId" ) String userId, @PathParam( "propertyKey" ) String propertyKey,
            @PathParam( "propertyValue" ) String propertyValue );

    @GET
    @Path( "/options/{propertyKey}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUserPropertyField( @PathParam( "propertyKey" ) String propertyKey );

    /**
     * Gets all values for user table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for user table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForUserTableColumn( @PathParam( "column" ) String column );

    /**
     * List groups from user id ui response.
     *
     * @param userId
     *         the user id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/groups/ui" )
    Response listGroupsFromUserIdUI( @PathParam( "id" ) UUID userId );

    /**
     * List groups from user id response.
     *
     * @param userId
     *         the user id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/groups/list" )
    Response listGroupsFromUserId( @PathParam( "id" ) UUID userId, String filterJson );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( "/{id}/groups/context" )
    Response getContextRouterForUserGroups( @PathParam( "id" ) UUID userId, String filterJson );

}
