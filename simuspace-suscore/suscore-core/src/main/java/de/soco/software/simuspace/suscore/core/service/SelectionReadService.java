package de.soco.software.simuspace.suscore.core.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.constants.ConstantsSelectionServiceEndPoints;

/**
 * The Interface SelectionReadService for providing paginated and prepared list of features.
 *
 * @author Noman Arshad
 */
public interface SelectionReadService {

    /**
     * Gets the all user selections with pagination.
     *
     * @param selectionId
     *         the selection id
     * @param filterJson
     *         the filter json
     *
     * @return the all user selections with pagination
     */
    @POST
    @Path( ConstantsSelectionServiceEndPoints.GET_USER_SELECTIONS )
    Response getAllSelectionsWithPagination( @PathParam( "id" ) String selectionId, String filterJson );

    /**
     * Gets the all user selections with pagination.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the all user selections with pagination
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.GET_USER_SELECTIONS )
    Response getAllSelectionsWithoutPagination( @PathParam( "id" ) String selectionId );

}
