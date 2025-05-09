package de.soco.software.simuspace.suscore.search.manager;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.entity.SearchObject;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The Interface SearchManager manages the Search CRUD (and other) operations to Dao layer.
 *
 * @author Ali Haider
 * @since 2.1
 */
public interface SearchManager {

    /**
     * Gets the filtered search list.
     *
     * @param userId
     *         the user id from general header
     * @param searchId
     *         the search id
     * @param filter
     *         the filter
     *
     * @return the filtered search list
     *
     * @throws ParseException
     *         the parse exception
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getFilteredSearchList( String userId, String searchId, FiltersDTO filter ) throws ParseException;

    /**
     * Gets the filtered search list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id from general header
     * @param searchId
     *         the search id
     * @param filter
     *         the filter
     *
     * @return the filtered search list
     *
     * @throws ParseException
     *         the parse exception
     */
    FilteredResponse< ? > getFilteredSearchList( EntityManager entityManager, String userId, String searchId, FiltersDTO filter )
            throws ParseException;

    /**
     * Gets the search tree.
     *
     * @return the search tree
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    List< SearchObject > getSearchTree() throws IOException;

    /**
     * Gets search tree by search id.
     *
     * @param userId
     *         the user id
     * @param searchId
     *         the search id
     *
     * @return the search tree by search id
     *
     * @throws IOException
     *         the io exception
     * @apiNote To be used in service calls only
     */
    List< SearchObject > getSearchTreeBySearchId( String userId, String searchId ) throws IOException;

    /**
     * List search UI.
     *
     * @param userId
     *         the user id from general header
     * @param searchId
     *         the searchId
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    TableUI getSearchListUIBySearchId( String userId, String searchId );

    /**
     * Gets the search list UI.
     *
     * @param userId
     *         the user id from general header
     *
     * @return the search list UI
     */
    TableUI getSearchListUI( String userId );

    /**
     * Gets filtered search list by search id.
     *
     * @param userId
     *         the user id
     * @param searchId
     *         the search id
     * @param filtersDTO
     *         the filters dto
     *
     * @return the filtered search list by search id
     *
     * @throws Exception
     *         the exception
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ? > getFilteredSearchListBySearchId( String userId, String searchId, FiltersDTO filtersDTO ) throws Exception;

    /**
     * Gets the search context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the search context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getSearchContextRouter( String userIdFromGeneralHeader, FiltersDTO filter );

}