package de.soco.software.simuspace.suscore.user.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

/**
 * The interface is responsible to provide blueprint for business logic of user Group and invoke dao class functions to communicate with
 * repository and provide CRUD operations to it.
 *
 * @author Nosheen.Sharif
 */
public interface UserGroupManager {

    /**
     * Create User Group
     *
     * @param userId
     *         the user id
     * @param userGroupDto
     *         the user group dto
     *
     * @return SuSUserGroupDTO su s user group dto
     *
     * @apiNote To be used in service calls only
     */
    SuSUserGroupDTO createUserGroup( String userId, SuSUserGroupDTO userGroupDto );

    /**
     * Update User Group
     *
     * @param userId
     *         the user id
     * @param userGroupDto
     *         the user group dto
     *
     * @return SuSUserGroupDTO su s user group dto
     *
     * @apiNote To be used in service calls only
     */
    SuSUserGroupDTO updateUserGroup( String userId, SuSUserGroupDTO userGroupDto );

    /**
     * delete User Group By Selection
     *
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     * @param mode
     *         the mode
     *
     * @return boolean boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteUserGroupBySelection( String userId, String groupId, String mode );

    /**
     * Read User Group
     *
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     *
     * @return SuSUserGroupDTO su s user group dto
     *
     * @apiNote To be used in service calls only
     */
    SuSUserGroupDTO readUserGroup( String userId, UUID groupId );

    /**
     * Read User Group
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     *
     * @return SuSUserGroupDTO su s user group dto
     */
    SuSUserGroupDTO readUserGroup( EntityManager entityManager, String userId, UUID groupId );

    /**
     * List User from Group Id
     *
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     *
     * @return SuSUserGroupDTO table ui
     *
     * @apiNote To be used in service calls only
     */
    TableUI listUsersFromGroupIdUI( String userId, UUID groupId );

    /**
     * List User from Group Id
     *
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     * @param filter
     *         the filter
     *
     * @return SuSUserGroupDTO filtered response
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< UserDTO > listUsersFromGroupId( String userId, UUID groupId, FiltersDTO filter );

    /**
     * Get User Group List Filtered
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return FilteredResponse user groups list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse getUserGroupsList( String userId, FiltersDTO filter );

    /**
     * Get User Group List Filtered
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return FilteredResponse user groups list
     */
    FilteredResponse getUserGroupsList( EntityManager entityManager, String userId, FiltersDTO filter );

    /**
     * Creates the User Group form for edit.
     *
     * @param userId
     *         the user id
     *
     * @return the list of UIFormItems
     *
     * @apiNote To be used in service calls only
     */
    UIForm createUserGroupForm( String userId );

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
    List< ContextMenuItem > getContextMenu( FiltersDTO filter );

    /**
     * Gets context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     */
    List< ContextMenuItem > getContextRouter( FiltersDTO filter );

    /**
     * Creates the User Group form for edit.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editUserGroupForm( String userId, UUID id );

    /**
     * Gets user groups by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userUuid
     *         the user uuid
     *
     * @return the user groups by user id
     */
    List< GroupEntity > getUserGroupsByUserId( EntityManager entityManager, UUID userUuid );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Gets the all group selections.
     *
     * @param selectionId
     *         the selection id
     * @param systemUserIdFromGeneralHeader
     *         the system user id from general header
     * @param filter
     *         the filter
     *
     * @return the all group selections
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse getAllGroupSelections( String selectionId, String systemUserIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Gets the all group selections.
     *
     * @param selectionId
     *         the selection id
     * @param systemUserIdFromGeneralHeader
     *         the system user id from general header
     *
     * @return the all group selections
     *
     * @apiNote To be used in service calls only
     */
    List< SuSUserGroupDTO > getAllGroupsBySelectionId( String selectionId, String systemUserIdFromGeneralHeader );

    /**
     * Prepare sus user group dto from group entity su s user group dto.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the su s user group dto
     */
    SuSUserGroupDTO prepareSusUserGroupDTOFromGroupEntity( EntityManager entityManager, GroupEntity entity );

    /**
     * Prepare group entity from sus user group DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     *
     * @return the group entity
     */
    GroupEntity prepareGroupEntityFromSusUserGroupDTO( EntityManager entityManager, SuSUserGroupDTO dto );

    /**
     * Read user group without permission.
     *
     * @param entityManager
     *         the entity manager
     * @param groupId
     *         the group id
     *
     * @return the su S user group DTO
     */
    SuSUserGroupDTO readUserGroupWithoutPermission( EntityManager entityManager, UUID groupId );

    /**
     * Gets all values for groups table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for groups table column
     */
    List< Object > getAllValuesForGroupsTableColumn( String columnName, String token );

}