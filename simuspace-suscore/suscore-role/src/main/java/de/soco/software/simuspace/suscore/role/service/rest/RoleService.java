package de.soco.software.simuspace.suscore.role.service.rest;

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
import org.json.simple.parser.ParseException;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.permissions.constants.ConstantsPermissions;

/**
 * The Interface RoleServices provides the Roles API.
 *
 * @author Ahsan Khan
 * @since 2.0
 */

@Path( "/" )
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface RoleService {

    /**
     * Creates the role.
     *
     * @param roleJson
     *         the role json
     *
     * @return the response
     *
     * @throws ParseException
     * @throws SusException
     *         the sus exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @POST
    @Path( ConstantsPermissions.CREATE_STANDARD_API_URL )
    @Produces( MediaType.APPLICATION_JSON )
    Response createRole( String roleJson ) throws ParseException;

    /**
     * Creates the user form.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPermissions.CREATE_ROLE_FORM )
    Response createRoleForm();

    /**
     * Edits the role form.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPermissions.EDIT_ROLE_FORM )
    Response editRoleForm( @PathParam( "id" ) UUID id );

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

    /**
     * Checks for role.
     *
     * @param roleJson
     *         the role json
     *
     * @return the string
     *
     * @throws SusException
     *         the sus exception
     */
    @PUT
    @Path( ConstantsPermissions.STANDARD_API_URL )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateRole( String roleJson );

    /**
     * Gets the role.
     *
     * @param roleId
     *         the role id
     * @param mode
     *         the mode
     *
     * @return the role
     */
    @DELETE
    @Path( ConstantsPermissions.STANDARD_API_URL_FOR_DELETE )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteRole( @PathParam( "id" ) String roleId, @QueryParam( "mode" ) String mode );

    /**
     * List role UI.
     *
     * @return the response
     */

    @GET
    @Path( ConstantsPermissions.LIST_ROLE_TABLE_UI )
    @Produces( MediaType.APPLICATION_JSON )
    Response listRoleUI();

    /**
     * List users.
     *
     * @param userJson
     *         the user json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsPermissions.LIST_ALL_ROLES )
    @Produces( MediaType.APPLICATION_JSON )
    Response getRoleList( String userJson );

    /**
     * Read role.
     *
     * @param roleId
     *         the role id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPermissions.READ_ROLE )
    Response readRole( @PathParam( "id" ) UUID roleId );

    /**
     * Permit permission to role.
     *
     * @param permissionJson
     *         the permission json
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsPermissions.PERMIT_PERMISSION_TO_ROLE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response permitPermissionToRole( String permissionJson, @PathParam( "roleId" ) UUID roleId,
            @PathParam( "resourceId" ) UUID resourceId );

    /**
     * Gets the all permissions 1.
     *
     * @param filterJsonStr
     *         the filter json str
     * @param roleId
     *         the role id
     *
     * @return the all permissions 1
     */
    @POST
    @Path( ConstantsPermissions.GET_PERMISSION_MODULE_LIST_BY_ROLE_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllPermissionsByRoleId( String filterJsonStr, @PathParam( "id" ) UUID roleId );

    /**
     * Manage role permission UI.
     *
     * @param roleId
     *         the role id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPermissions.MANAGE_PERMISSION_TABLE )
    Response manageRolePermissionUI( @PathParam( "id" ) UUID roleId );

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
     * Gets the all manage role views.
     *
     * @param projectId
     *         the project id
     *
     * @return the all manage role views
     */
    @GET
    @Path( ConstantsPermissions.SAVE_OR_LIST_VIEWS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllManageRoleViews( @PathParam( "id" ) String projectId );

    /**
     * Save manage role view.
     *
     * @param projectId
     *         the project id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsPermissions.SAVE_OR_LIST_VIEWS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveManageRoleView( @PathParam( "id" ) String projectId, String objectJson );

    /**
     * Sets the manage role view as default.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsPermissions.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setManageRoleViewAsDefault( @PathParam( "id" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Delete manage role view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsPermissions.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteManageRoleView( @PathParam( "id" ) String projectId, @PathParam( "viewId" ) String viewId );

    /**
     * Update manage role view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsPermissions.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateManageRoleView( @PathParam( "id" ) String projectId, @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * List groups from role id ui response.
     *
     * @param roleId
     *         the role id
     *
     * @return the response
     */
    @GET
    @Path( "/{id}/groups/ui" )
    Response listGroupsFromRoleIdUI( @PathParam( "id" ) UUID roleId );

    /**
     * List groups from role id response.
     *
     * @param roleId
     *         the role id
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/{id}/groups/list" )
    Response listGroupsFromRoleId( @PathParam( "id" ) UUID roleId, String filterJson );

    /**
     * Gets the context router.
     *
     * @param roleId
     *         the role id
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( "/{id}/groups/context" )
    Response getContextRouterForRoleGroups( @PathParam( "id" ) UUID roleId, String filterJson );

    /**
     * Gets all values for role table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for user table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForRoleTableColumn( @PathParam( "column" ) String column );

}