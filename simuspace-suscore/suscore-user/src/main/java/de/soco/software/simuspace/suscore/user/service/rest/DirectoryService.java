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
import de.soco.software.simuspace.suscore.user.constants.DirectoryServiceEndPoints;

/**
 * Implementation of Directory Service responsible to communicate with the business lyre.
 *
 * @author M.Nasir.Farooq
 */
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
@Path( "/" )
public interface DirectoryService {

    /**
     * Get the filtered directory list
     *
     * @param filterJson
     *         the filter json
     *
     * @return directory list
     */
    @POST
    @Path( DirectoryServiceEndPoints.READ_DIRECTORY_LIST )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDirectoryList( String filterJson );

    /**
     * Get the all non-filtered directory list
     *
     * @return all directories
     */
    @GET
    @Path( DirectoryServiceEndPoints.GET_ALL_DIRECTORIES )
    @Produces( MediaType.APPLICATION_JSON )
    Response getAllDirectories();

    /**
     * Gets the directories by type.
     *
     * @param dirType
     *         the dir type
     *
     * @return the directories by type
     */
    @GET
    @Path( DirectoryServiceEndPoints.GET_DIRECTORIES_BY_TYPE )
    @Produces( MediaType.APPLICATION_JSON )
    Response getDirectoriesByType( @PathParam( "type" ) String dirType );

    /**
     * Create user directory.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     *
     * @Request payload
     * :{"name":"Name","description":"Description","status":1,"type":1,"attributes":{"Name":"Description"},"mapping":{"Name":"Description"},"id":null,"createdOn":null,"updatedOn":null}
     * @Response payload
     * :{"data":{"name":"Name","description":"Description","status":0,"type":1,"attributes":{"Name":"Description"},"mapping":{"Name":"Description"},"id":null,"createdOn":null,"updatedOn":null},"success":true}
     */
    @POST
    @Path( DirectoryServiceEndPoints.CREATE_DIRECTORY )
    @Produces( MediaType.APPLICATION_JSON )
    Response createDirectory( String objectJson );

    /**
     * Update user directory.
     *
     * @param objectJson
     *         the object json
     *
     * @return response
     *
     * @Request payload
     * :{"name":"Name","description":"Description","status":1,"type":1,"attributes":{"Name":"Description"},"mapping":{"Name":"Description"},"id":null,"createdOn":null,"updatedOn":null}
     * @Response payload
     * :{"data":{"name":"Name","description":"Description","status":0,"type":1,"attributes":{"Name":"Description"},"mapping":{"Name":"Description"},"id":null,"createdOn":null,"updatedOn":null},"success":true}
     */
    @PUT
    @Path( DirectoryServiceEndPoints.UPDATE_DIRECTORY )
    @Produces( MediaType.APPLICATION_JSON )
    Response updateDirectory( String objectJson );

    /**
     * Delete directory.
     *
     * @param id
     *         the selection id
     * @param mode
     *         the delete mode
     *
     * @return the response
     *
     * @Response payload : {"data":true,"success":true}
     */
    @DELETE
    @Path( DirectoryServiceEndPoints.DELETE_DIRECTORY )
    @Produces( MediaType.APPLICATION_JSON )
    Response deleteDirectory( @PathParam( "id" ) String id, @QueryParam( "mode" ) String mode );

    /**
     * Read directory.
     *
     * @param directoryId
     *         the directory id
     *
     * @return the response
     *
     * @Response payload :
     * {"data":{"name":"Name","description":"Description","status":0,"type":1,"attributes":{"Name":"Description"},"mapping":{"Name":"Description"},"id":null,"createdOn":null,"updatedOn":null},"success":true}
     */
    @GET
    @Path( DirectoryServiceEndPoints.READ_DIRECTORY )
    @Produces( MediaType.APPLICATION_JSON )
    Response readDirectory( @PathParam( "id" ) UUID directoryId );

    /**
     * generate create directory form
     *
     * @return response
     */
    @GET
    @Path( DirectoryServiceEndPoints.CREATE_DIRECTORY_FORM )
    @Produces( MediaType.APPLICATION_JSON )
    Response createDirectoryForm();

    /**
     * Gets the update directory UI.
     *
     * @param option
     *         the option
     *
     * @return the update directory UI
     */
    @GET
    @Path( "fields/{option}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getUpdateDirectoryUI( @PathParam( "option" ) String option );

    /**
     * List directory table UI including columns and view
     *
     * @return response
     */
    @GET
    @Path( DirectoryServiceEndPoints.LIST_DIRECTORY_TABLE_UI )
    @Produces( MediaType.APPLICATION_JSON )
    Response listDirectoyUI();

    /**
     * List directory table columns
     *
     * @return response
     */
    @GET
    @Path( DirectoryServiceEndPoints.LIST_DIRECTORY_TABLE_COLUMNS )
    @Produces( MediaType.APPLICATION_JSON )
    Response listDirectoryColumn();

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( DirectoryServiceEndPoints.GET_CONTEXT_ROUTER )
    @Produces( MediaType.APPLICATION_JSON )
    Response getContextRouter( String filterJson );

    /**
     * Creates user directory form for edit.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( DirectoryServiceEndPoints.CREATE_USER_DIRECTORY_FOR_EDIT )
    @Produces( MediaType.APPLICATION_JSON )
    Response createUserDirectoryFormForEdit( @PathParam( "id" ) UUID id );

    /**
     * Test ldap or active directory connection.
     *
     * @param json
     *         the json
     *
     * @return the response
     */
    @POST
    @Path( DirectoryServiceEndPoints.TEST_CONNECTION )
    @Produces( MediaType.APPLICATION_JSON )
    Response testDirectoryConnection( String json );

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
     * Gets all values for directory table column.
     *
     * @param column
     *         the column
     *
     * @return the all values for directory table column
     */
    @GET
    @Path( "/column/{column}/values" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllValuesForDirectoryTableColumn( @PathParam( "column" ) String column );

}
