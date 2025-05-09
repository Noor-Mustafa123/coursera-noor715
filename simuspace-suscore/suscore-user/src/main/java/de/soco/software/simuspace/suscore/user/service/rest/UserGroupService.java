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
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.user.constants.UserGroupServiceEndPoints;

/**
 * Service EndPoint for User Groups.
 *
 * @author Nosheen.Sharif
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )

@Path( "/" )
public interface UserGroupService {

    /**
     * Get User Groups List.
     *
     * @param userGrpFilterJson
     *         the user grp filter json
     *
     * @return the user groups list
     */
    @POST
    @Path( GUIUtils.LIST_ALL )
    Response getUserGroupsList( String userGrpFilterJson );

    /**
     * Create User Group.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( UserGroupServiceEndPoints.CREATE_USER_GRP )
    Response createUserGroup( String objectJson );

    /**
     * Update User Group.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( UserGroupServiceEndPoints.UPDATE_USER_GRP )
    Response updateUserGroup( String objectJson );

    /**
     * Delete User Group.
     *
     * @param groupId
     *         the group id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( UserGroupServiceEndPoints.DELETE_USER_GRP )
    Response deleteUserGroup( @PathParam( "id" ) String groupId, @QueryParam( "mode" ) String mode );

    /**
     * Read User Group By Id.
     *
     * @param groupId
     *         the group id
     *
     * @return the response
     */
    @GET
    @Path( UserGroupServiceEndPoints.READ_USER_GRP )
    Response readUserGroup( @PathParam( "id" ) UUID groupId );

    /**
     * List users group columns.
     *
     * @return the response
     */
    @GET
    @Path( GUIUtils.LIST_TABLE_COLUMNS )
    Response listUsersGroupColumns();

    /**
     * List user table UI including columns and view.
     *
     * @return the response
     */
    @GET
    @Path( GUIUtils.LIST_TABLE_UI )
    Response listUsersGroupUI();

    /**
     * Get Users from group id.
     *
     * @param groupId
     *         the group id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/users/ui" )
    Response listUsersFromGroupIdUI( @PathParam( "id" ) UUID groupId );

    /**
     * Get Users from group id.
     *
     * @param groupId
     *         the group id
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/users/list" )
    Response listUsersFromGroupId( @PathParam( "id" ) UUID groupId, String filterJson );

    /**
     * generate create user group form.
     *
     * @return the response
     */
    @GET
    @Path( GUIUtils.CREATE_UI_FORM )
    Response createUserGroupForm();

    /**
     * Creates the user group form for edit.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( GUIUtils.CREATE_FORM_FOR_EDIT )
    Response editUserGroupForm( @PathParam( "id" ) UUID id );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( GUIUtils.GET_CONTEXT_ROUTER )
    Response getContextRouter( String filterJson );

    @POST
    @Path( "/{groupId}/users/context" )
    Response getContextRouterForUser( String filterJson );

    @GET
    @Path( "/{groupId}/context" )
    Response getContextRouterForSingleGroup( @PathParam( "groupId" ) UUID groupId );

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
     * Gets the view.
     *
     * @return the view
     */
    @GET
    @Path( "/{id}/users" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getGroupUsersViews();

    /**
     * Save view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/users" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveGroupUsersView( String objectJson );

    /**
     * Sets the view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/users" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setGroupUsersViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "/{id}/users" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteGroupUsersView( @PathParam( "viewId" ) String viewId );

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
    @Path( "/{id}/users" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateGroupUsersView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all values for groups table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for user table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForGroupsTableColumn( @PathParam( "column" ) String column );

}
