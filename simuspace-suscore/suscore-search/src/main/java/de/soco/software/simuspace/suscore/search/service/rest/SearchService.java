package de.soco.software.simuspace.suscore.search.service.rest;

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

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectTypes;
import de.soco.software.simuspace.suscore.search.constants.ConstantsSearchServiceEndPoints;

/**
 * This Interface is responsible for all the operation Of Search.
 *
 * @author Ali Haider
 */

@Path( "/" )
@WebService
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = ConstantsGZip.GZIP_FORCE, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface SearchService {

    /**
     * Gets the search tree.
     *
     * @return the search tree
     */
    @GET
    @Path( ConstantsSearchServiceEndPoints.GET_SEARCH_TREE )
    Response getSearchTree();

    /**
     * Gets the search tree by search id.
     *
     * @param searchId
     *         the search id
     *
     * @return the search tree by search id
     */
    @GET
    @Path( ConstantsSearchServiceEndPoints.GET_SEARCH_TREE_BY_SEARCH_ID )
    Response getSearchTreeBySearchId( @PathParam( ConstantsObjectTypes.SEARCH_ID_PARAM ) String searchId );

    /**
     * Gets the search tree UI.
     *
     * @return the search tree UI
     */
    @GET
    @Path( ConstantsSearchServiceEndPoints.GET_SEARCH_TABLE_UI_BY_SEARCH_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSearchUIBySearchId( @PathParam( ConstantsObjectTypes.SEARCH_ID_PARAM ) String searchId );

    /**
     * Gets the search UI.
     *
     * @return the search UI
     */
    @GET
    @Path( ConstantsSearchServiceEndPoints.GET_SEARCH_TABLE_UI )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSearchUI();

    /**
     * Gets the filtered search list.
     *
     * @param filteredSearchJson
     *         the filtered search json
     *
     * @return the filtered search list
     */
    @POST
    @Path( ConstantsSearchServiceEndPoints.GET_SEARCH_TABLE_LIST )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredSearchList( String filteredSearchJson );

    /**
     * Gets the filtered search list by search id.
     *
     * @param searchId
     *         the search id
     *
     * @return the filtered search list by search id
     */
    @POST
    @Path( ConstantsSearchServiceEndPoints.GET_FILTER_SEARCH_ID )
    @Produces( MediaType.APPLICATION_JSON )
    Response getFilteredSearchListBySearchId( @PathParam( ConstantsObjectTypes.SEARCH_ID_PARAM ) String searchId,
            String filteredSearchJson );

    /**
     * Gets the search context by search id.
     *
     * @param filteredSearchJson
     *         the filtered search json
     *
     * @return the search context by search id
     */
    @POST
    @Path( ConstantsSearchServiceEndPoints.GET_CONTEXT_SEARCH )
    @Produces( MediaType.APPLICATION_JSON )
    Response getSearchContext( String filteredSearchJson );

    /**
     * Gets the search context by search id.
     *
     * @param filteredSearchJson
     *         the filtered search json
     *
     * @return the search context by search id
     */
    @POST
    @Path( ConstantsSearchServiceEndPoints.GET_CONTEXT_SEARCH_ID )
    @Produces( MediaType.APPLICATION_JSON )
    Response getSearchContextBySearchId( String filteredSearchJson );

}
