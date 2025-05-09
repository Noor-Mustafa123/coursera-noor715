/**
 *
 */

package de.soco.software.simuspace.suscore.object.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;

/**
 * The interface is responsible for providing tree structure. Which will be consume able for services and for other managers if they are
 * coupled through dependency injection.
 *
 * @author Huzaifah
 */
public interface SuSObjectTreeManager {

    /**
     * Gets the tree.
     *
     * @param client
     *         the client
     * @param currentUser
     *         the current user
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return the tree
     *
     * @apiNote To be used in service calls only
     */
    List< TreeNodeDTO > getTree( String client, String currentUser, UUID parentId, ObjectTreeViewDTO filter, String token );

    /**
     * Gets the tree.
     *
     * @param entityManager
     *         the entity manager
     * @param client
     *         the client
     * @param currentUser
     *         the current user
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return the tree
     */
    List< TreeNodeDTO > getTree( EntityManager entityManager, String client, String currentUser, UUID parentId, ObjectTreeViewDTO filter,
            String token );

    /**
     * Find menu.
     *
     * @param currentUser
     *         the current user
     * @param token
     *         the token
     * @param parentId
     *         the parent id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > findMenu( String currentUser, String token, String parentId );

    /**
     * Find menu.
     *
     * @param entityManager
     *         the entity manager
     * @param currentUser
     *         the current user
     * @param token
     *         the token
     * @param parentId
     *         the parent id
     *
     * @return the list
     */
    List< ContextMenuItem > findMenu( EntityManager entityManager, String currentUser, String token, String parentId );

    /**
     * Gets the all object tree views.
     *
     * @param userId
     *         the user id
     *
     * @return the all object tree views
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectTreeViewDTO > getAllObjectTreeViews( String userId, String token );

    /**
     * Save object tree view.
     *
     * @param objectTreeViewDTO
     *         the object tree view DTO
     * @param userId
     *         the user id
     *
     * @return the object tree view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectTreeViewDTO saveObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId, String viewId );

    /**
     * Save as object tree view object tree view dto.
     *
     * @param objectTreeViewDTO
     *         the object tree view dto
     * @param userId
     *         the user id
     * @param viewId
     *         the view id
     *
     * @return the object tree view dto
     */
    ObjectTreeViewDTO saveAsObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId, String viewId );

    /**
     * Update object tree view.
     *
     * @param objectTreeViewDTO
     *         the object tree view DTO
     * @param userId
     *         the user id
     *
     * @return the object tree view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectTreeViewDTO updateObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId );

    /**
     * Sets the object tree view as default.
     *
     * @param viewId
     *         the view id
     * @param userId
     *         the user id
     *
     * @apiNote To be used in service calls only
     */
    void setObjectTreeViewAsDefault( UUID viewId, String userId );

    /**
     * Delete object tree view.
     *
     * @param viewId
     *         the view id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteObjectTreeView( UUID viewId );

    /**
     * Persist system container tree node dto.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param name
     *         the name
     * @param parentId
     *         the parent id
     * @param icon
     *         the icon
     */
    void persistSystemContainer( EntityManager entityManager, UUID id, String name, UUID parentId, String icon );

    /**
     * Gets tree for field.
     *
     * @param token
     *         the token
     * @param objectTreeViewDTO
     *         the object tree view dto
     * @param fieldType
     *         the field type
     *
     * @return the tree for field
     */
    TreeNodeDTO getTreeForField( String token, ObjectTreeViewDTO objectTreeViewDTO, String fieldType );

}
