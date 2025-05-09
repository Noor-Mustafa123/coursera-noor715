package de.soco.software.simuspace.suscore.object.manager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.object.model.BookmarkDTO;

/**
 * The interface Bookmark manager.
 */
public interface BookmarkManager {

    /**
     * Creates bookmark.
     *
     * @param user
     *         the user
     * @param bookmarkModel
     *         the bookmark model
     *
     * @return the created bookmark
     *
     * @apiNote To be used in service calls only
     */
    BookmarkDTO createBookmark( UUID user, BookmarkDTO bookmarkModel );

    /**
     * Updates bookmark.
     *
     * @param user
     *         the user
     * @param bookmarkModel
     *         the bookmark model
     *
     * @return the updated bookmark
     *
     * @apiNote To be used in service calls only
     */
    BookmarkDTO updateBookmark( UUID user, BookmarkDTO bookmarkModel );

    /**
     * Gets the list of bookmark UI table columns.
     *
     * @return the list of bookmark UI table columns
     *
     * @apiNote To be used in service calls only
     */
    TableUI getListOfBookmarkUITableColumns( String userId );

    /**
     * create ui form
     *
     * @return UIForm item list
     */
    UIForm createForm();

    /**
     * edit ui form
     *
     * @param bookmarkId
     *         the bookmark id
     *
     * @return UIForm item list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editForm( UUID bookmarkId );

    /**
     * get all bookmarks
     *
     * @param filter
     *         the filter
     *
     * @return bookmarks list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< BookmarkDTO > getAllBookmarks( String username, FiltersDTO filter );

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextRouter( String userName, FiltersDTO filter );

    /**
     * Gets the tree context router.
     *
     * @return the context router
     */
    List< ContextMenuItem > getTreeContextRouter();

    /**
     * Delete bookmark by selection.
     *
     * @param id
     *         the id
     * @param mode
     *         the mode
     *
     * @return the boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteBookmarkBySelection( String id, String mode );

    /**
     * Gets the user object views by key.
     *
     * @param key
     *         the key
     * @param userId
     *         the user id
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the user object views by key
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId );

    /**
     * Save or update object view.
     *
     * @param viewDTO
     *         the view DTO
     * @param userId
     *         the user id
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId );

    /**
     * Save default object view.
     *
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     * @param objectViewKey
     *         the object view key
     * @param objectId
     *         the object id (set null if its not for any other object)
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId );

    /**
     * Gets the object view by id.
     *
     * @param viewId
     *         the view id
     *
     * @return the object view by id
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO getObjectViewById( UUID viewId );

    /**
     * Delete object view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectView( UUID viewId );

    /**
     * Sets type for bookmark element.
     *
     * @param userId
     *         the userId
     * @param bookmarkModel
     *         the bookmark model
     */
    void setTypeForBookmarkElement( String userId, BookmarkDTO bookmarkModel );

}
