package de.soco.software.simuspace.suscore.core.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.UUID;

/**
 * The Interface LinkService provide interface for linking objects.
 *
 * @author Ahsan.Khan
 */
public interface LinkService {

    /**
     * Objects linking.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the response
     */
    @POST
    @Path( "/" )
    Response objectsLinking( String filterJson );

    /**
     * Removes the link object by id.
     *
     * @param objectId
     *         the object id
     * @param mode
     *         the mode
     *
     * @return the response
     */
    @POST
    @Path( "/remove/{objectId}" )
    @Produces( MediaType.APPLICATION_JSON )
    Response removeLinkObjectById( @PathParam( "objectId" ) UUID objectId, String selectionFrom );

    /**
     * Gets the linked relation by child id.
     *
     * @param objectId
     *         the object id
     *
     * @return the linked relation by child id
     */
    @GET
    @Path( "/relation/{objectId}" )
    Response getLinkedRelationByChildId( @PathParam( "objectId" ) UUID objectId );

}
