package de.soco.software.simuspace.suscore.object.service.rest;

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

import de.soco.software.simuspace.server.constant.ConstantsGZip;
import de.soco.software.simuspace.suscore.common.constants.ConstantsViewEndPoints;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

@WebService
@Consumes( { MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface BookmarkService {

    /**
     * Creates the bookmark.
     *
     * @param bookmarkJson
     *         the bookmarkJson
     *
     * @return the response
     */
    @POST
    @Path( "/" )
    @Produces( MediaType.APPLICATION_JSON )
    Response createBookmark( String bookmarkJson );

    /**
     * Updates the bookmark.
     *
     * @param bookmarkId
     *         the bookmark id
     * @param bookmarkJson
     *         the bookmark json
     *
     * @return the user entity
     *
     * @throws SusException
     *         the sus exception
     */
    @PUT
    @Path( "/{id}" )
    Response updateBookmark( @PathParam( "id" ) UUID bookmarkId, String bookmarkJson );

    /**
     * List job table UI.
     *
     * @return the response
     */
    @GET
    @Path( "/ui" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response listBookmarkTableUI();

    /**
     * Gets the bookmark list.
     *
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the filtered jobs list
     */
    @POST
    @Path( "/list" )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredBookmarkList( String objectFilterJson );

    /**
     * Creates the bookmark form.
     *
     * @return the response
     */
    @GET
    @Path( "/ui/create" )
    Response createBookmarkForm();

    /**
     * Edits the bookmark form.
     *
     * @return the response
     */
    @GET
    @Path( "/ui/edit/{bookmarkId}" )
    Response editBookmarkForm( @PathParam( "bookmarkId" ) UUID bookmarkId );

    /**
     * Gets the context router.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the context router
     */
    @POST
    @Path( "/context" )
    Response getContextRouter( String filterJson );

    /**
     * Gets the tree context router.
     *
     * @return the context router
     */
    @GET
    @Path( "/tree/context" )
    Response getTreeContextRouter();

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
    @Path( "/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteBookmark( @PathParam( "id" ) String id, @QueryParam( "mode" ) String mode );
    
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

}
