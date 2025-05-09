package de.soco.software.simuspace.suscore.license.service.rest;

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
import de.soco.software.simuspace.suscore.license.utils.ConstantsLicenseConfigurationServiceEndPoints;

/**
 * The interface is responsible to provide license API end points with them GUI can communicate with the application.
 *
 * @author M.Nasir.Farooq
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface LicenseConfigurationService {

    /**
     * Adds the license.
     *
     * @param strLicense
     *         the object type id
     *
     * @return the response
     */
    @POST
    @Path( ConstantsLicenseConfigurationServiceEndPoints.ADD_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addLicense( String strLicense );

    /**
     * Adds the license.
     *
     * @param strLicense
     *         the object type id
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsLicenseConfigurationServiceEndPoints.UPDATE_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLicense( String strLicense );

    /**
     * Gets the module license list.
     *
     * @return the module license list
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.GET_ALL_LICENSES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModuleLicenseList();

    /**
     * Gets the module license list.
     *
     * @param filterJsonStr
     *         the filter json str
     *
     * @return the module license list
     */
    @POST
    @Path( ConstantsLicenseConfigurationServiceEndPoints.GET_MODULE_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getModuleLicenseList( String filterJsonStr );

    /**
     * Delete module license single or bulk.
     *
     * @param selectionId
     *         the selection id
     * @param mode
     *         the mode
     *
     * @return the response
     */

    @DELETE
    @Path( ConstantsLicenseConfigurationServiceEndPoints.DELETE_MODULE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteModuleLicense( @PathParam( "id" ) String selectionId, @QueryParam( "mode" ) String mode );

    /**
     * Checks if is feature allowed to user.
     *
     * @param feature
     *         the feature
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.CHECK_FEATURE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response isFeatureAllowedToUser( @PathParam( "feature" ) String feature );

    /**
     * List user table UI including columns and view.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.LIST_LICENSE_TABLE_UI )
    Response listLicenseUI();

    /**
     * Creates the license form.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.CREATE_LICENSE_FORM )
    Response createLicenseForm();

    /**
     * Creates the license form for edit.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.CREATE_LICENSE_FORM_FOR_EDIT )
    Response createLicenseFormForEdit( @PathParam( "id" ) UUID id );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( ConstantsLicenseConfigurationServiceEndPoints.GET_CONTEXT_ROUTER )
    Response getContextRouter( String filterJson );

    /**
     * Manage license UI.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.MANAGE_LICENSE_TABLE_UI )
    Response manageLicenseUI();

    /**
     * Gets the all user licenses.
     *
     * @param filterJsonStr
     *         the filter json str
     *
     * @return the all user licenses list
     */
    @POST
    @Path( ConstantsLicenseConfigurationServiceEndPoints.GET_USER_MODULE_LIST )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllUserLicenses( String filterJsonStr );

    /**
     * Manage user license.
     *
     * @param userId
     *         the user id
     * @param checkBoxStr
     *         the check box str
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsLicenseConfigurationServiceEndPoints.UPDATE_USER_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response manageUserLicense( @PathParam( "userId" ) UUID userId, String checkBoxStr );

    /**
     * Gets the all license views.
     *
     * @return the all license views
     */
    @GET
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllLicenseViews();

    /**
     * Gets the license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the license view
     */
    @GET
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLicenseView( @PathParam( "viewId" ) String viewId );

    /**
     * Save license view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveLicenseView( String objectJson );

    /**
     * Sets the license view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setLicenseViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteLicenseView( @PathParam( "viewId" ) String viewId );

    /**
     * Update license view.
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
    Response updateLicenseView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets the all license views.
     *
     * @return the all license views
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.SAVE_OR_LIST_VIEWS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllManageLicenseViews();

    /**
     * Gets the license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the license view
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getManageLicenseView( @PathParam( "viewId" ) String viewId );

    /**
     * Save license view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( ConstantsLicenseConfigurationServiceEndPoints.SAVE_OR_LIST_VIEWS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveManageLicenseView( String objectJson );

    /**
     * Sets the license view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsLicenseConfigurationServiceEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setManageLicenseViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete license view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( ConstantsLicenseConfigurationServiceEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteManageLicenseView( @PathParam( "viewId" ) String viewId );

    /**
     * Update license view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( ConstantsLicenseConfigurationServiceEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateManageLicenseView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets the active users ui.
     *
     * @param viewId
     *         the view id
     *
     * @return the active users ui
     */
    @GET
    @Path( "user/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getActiveUsersUi( @PathParam( "viewId" ) String viewId );

    /**
     * Gets the active users list.
     *
     * @param objectJson
     *         the object json
     *
     * @return the active users list
     */
    @POST
    @Path( "user/list" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getActiveUsersList( String objectJson );

    /**
     * Gets the all active user context.
     *
     * @param json
     *         the json
     *
     * @return the all active user context
     */
    @POST
    @Path( "user/context" )
    Response getAllActiveUserContext( String json );

    /**
     * Free licesnse.
     *
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @DELETE
    @Path( "expire/{sid}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response freeLicesnse( @PathParam( "sid" ) String id, @QueryParam( "mode" ) String mode );

    /**
     * Gets the license module options.
     *
     * @return the license module options
     */
    @GET
    @Path( "modules" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getLicenseModuleOptions();

    /**
     * Gets the licesnse history.
     *
     * @param json
     *         the json
     *
     * @return the licesnse history
     */
    @POST
    @Path( "history" )
    Response getLicesnseHistory( String json );

    /**
     * Gets all the active user views.
     *
     * @return the view
     */
    @GET
    @Path( "user" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllActiveUserViews();

    /**
     * Gets the active user view.
     *
     * @param viewId
     *         the view id
     *
     * @return the view
     */
    @GET
    @Path( "user" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getActiveUserView( @PathParam( "viewId" ) String viewId );

    /**
     * Save the active user view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "user" + ConstantsViewEndPoints.SAVE_OR_LIST_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveActiveUserView( String objectJson );

    /**
     * Sets the active user view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "user" + ConstantsViewEndPoints.UPDATE_VIEW_AS_DEFAULT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setActiveUserViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete the active user view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "user" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteActiveUserView( @PathParam( "viewId" ) String viewId );

    /**
     * Update the active user view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "user" + ConstantsViewEndPoints.DELETE_OR_UPDATE_OR_GET_VIEW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateActiveUserView( @PathParam( "viewId" ) String viewId, String objectJson );

    /**
     * Gets all values for license module table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for license module table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForLicenseModuleTableColumn( @PathParam( "column" ) String column );

    /**
     * Gets all values for license user table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for license user table column
     */
    @GET
    @Path( "/user/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForLicenseUserTableColumn( @PathParam( "column" ) String column );

}