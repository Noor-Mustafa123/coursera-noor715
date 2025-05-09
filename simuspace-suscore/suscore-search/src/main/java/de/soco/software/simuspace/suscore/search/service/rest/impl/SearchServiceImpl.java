package de.soco.software.simuspace.suscore.search.service.rest.impl;

import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.search.manager.SearchManager;
import de.soco.software.simuspace.suscore.search.service.rest.SearchService;

/**
 * The Class SearchServiceImpl.
 *
 * @author Ali Haider
 */
public class SearchServiceImpl extends SuSBaseService implements SearchService {

    /**
     * The search manager.
     */
    private SearchManager searchManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchTree() {
        try {
            return ResponseUtils.success( searchManager.getSearchTree() );
        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchTreeBySearchId( String searchId ) {
        try {
            return ResponseUtils.success( searchManager.getSearchTreeBySearchId( getUserIdFromGeneralHeader(), searchId ) );
        } catch ( Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchUIBySearchId( String searchId ) {
        try {
            return ResponseUtils.success( searchManager.getSearchListUIBySearchId( getUserIdFromGeneralHeader(), searchId ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchUI() {
        try {
            return ResponseUtils.success( searchManager.getSearchListUI( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredSearchList( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( searchManager.getFilteredSearchList( getUserIdFromGeneralHeader(), null, filter ) );

        } catch ( Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredSearchListBySearchId( String searchId, String filterJson ) {
        Response response;
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            FilteredResponse< ? > filteredResponse = searchManager.getFilteredSearchListBySearchId( getUserIdFromGeneralHeader(), searchId,
                    filter );
            response = ResponseUtils.success( filteredResponse );
        } catch ( Exception e ) {
            return handleException( e );
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchContextBySearchId( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( searchManager.getSearchContextRouter( getUserIdFromGeneralHeader(), filter ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( searchManager.getSearchContextRouter( getUserIdFromGeneralHeader(), filter ) ) );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the search manager.
     *
     * @return the search manager
     */
    public SearchManager getSearchManager() {
        return searchManager;
    }

    /**
     * Sets the search manager.
     *
     * @param searchManager
     *         the new search manager
     */
    public void setSearchManager( SearchManager searchManager ) {
        this.searchManager = searchManager;
    }

}
