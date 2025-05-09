/**
 *
 */

package de.soco.software.simuspace.suscore.object.service.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Interface SuSObjectTreeService.
 *
 * @author Huzaifah
 */
public interface SuSObjectTreeService {

    /**
     * Gets the object tree.
     *
     * @return the object tree
     */
    @GET
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectTree();

    /**
     * Filter object tree.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response filterObjectTree( String objectJson );

    /**
     * Gets object tree for field.
     *
     * @param fieldType
     *         the field type
     *
     * @return the object tree for field
     */
    @GET
    @Path( "/{fieldType}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getObjectTreeForField( @PathParam( "fieldType" ) String fieldType );

    /**
     * Filter object tree children.
     *
     * @param containerId
     *         the container id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/{containerId}/children" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response filterObjectTreeChildren( @PathParam( "containerId" ) String containerId, String objectJson );

    /**
     * Gets the tree context.
     *
     * @param containerId
     *         the container id
     * @param objectJson
     *         the object json
     *
     * @return the tree context
     */
    @POST
    @Path( "/{containerId}/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTreeContext( @PathParam( "containerId" ) String containerId, String objectJson );

    /**
     * Gets the tree context.
     *
     * @param containerId
     *         the container id
     *
     * @return the tree context
     */
    @GET
    @Path( "/{containerId}/context" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getTreeContext( @PathParam( "containerId" ) String containerId );

    /**
     * Gets the all object tree views.
     *
     * @return the all object tree views
     */
    @GET
    @Path( "/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getAllObjectTreeViews();

    /**
     * Save object tree view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/ui/view" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveObjectTreeView( String objectJson );

    /**
     * Save object tree view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @POST
    @Path( "/ui/view/copy" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response saveAsObjectTreeView( String objectJson );

    /**
     * Sets the object tree view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @GET
    @Path( "ui/view/{viewId}/default" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response setObjectTreeViewAsDefault( @PathParam( "viewId" ) String viewId );

    /**
     * Delete object tree view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @DELETE
    @Path( "ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response deleteObjectTreeView( @PathParam( "viewId" ) String viewId );

    /**
     * Update object tree view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @PUT
    @Path( "ui/view/{viewId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectTreeView( @PathParam( "viewId" ) String viewId, String objectJson );

}
