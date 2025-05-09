package de.soco.software.simuspace.suscore.role.manager;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;

/**
 * The Interface RoleManager manages the Role CRUD (and other) operations to Dao layer.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface RoleManager {

    /**
     * Creates the role.
     *
     * @param userId
     *         the user id
     * @param roleModel
     *         the role model
     * @param isAllowPermission
     *         the is allow permission
     *
     * @return the role model
     *
     * @apiNote To be used in service calls only
     */
    RoleDTO createRole( String userId, RoleDTO roleModel, boolean isAllowPermission );

    /**
     * Creates the role.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param roleModel
     *         the role model
     * @param isAllowPermission
     *         the is allow permission
     *
     * @return the role model
     */
    RoleDTO createRole( EntityManager entityManager, String userId, RoleDTO roleModel, boolean isAllowPermission );

    /**
     * Gets role names for user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the role names for user
     */
    List< RoleDTO > getRoleNamesForUser( EntityManager entityManager, String userId );

    /**
     * Update role.
     *
     * @param userId
     *         the user id
     * @param roleModel
     *         the role model
     *
     * @return the role model
     *
     * @apiNote To be used in service calls only
     */
    RoleDTO updateRole( String userId, RoleDTO roleModel );

    /**
     * Update role.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param roleModel
     *         the role model
     *
     * @return the role model
     */
    RoleDTO updateRole( EntityManager entityManager, String userId, RoleDTO roleModel );

    /**
     * Delete role by selection.
     *
     * @param userId
     *         the user id
     * @param roleId
     *         the role id
     * @param mode
     *         the mode
     *
     * @return true, if successful\
     *
     * @apiNote To be used in service calls only
     */
    boolean deleteRoleBySelection( String userId, String roleId, String mode );

    /**
     * Creates the role form.
     *
     * @param userId
     *         the user id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createRoleForm( String userId );

    /**
     * Edits the role form.
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
    UIForm editRoleForm( String userId, UUID id );

    /**
     * Gets the context menu.
     *
     * @param filter
     *         the filter
     *
     * @return the context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextMenu( FiltersDTO filter );

    /**
     * Read role.
     *
     * @param roleId
     *         the role id
     *
     * @return the role model
     *
     * @apiNote To be used in service calls only
     */
    RoleDTO readRole( String userId, UUID roleId );

    /**
     * Read role.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param roleId
     *         the role id
     *
     * @return the role model
     */
    RoleDTO readRole( EntityManager entityManager, String userId, UUID roleId );

    /**
     * Gets the role list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the role list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< RoleDTO > getRoleList( String userId, FiltersDTO filter );

    /**
     * Permit permission to role.
     *
     * @param userId
     *         the user id
     * @param checkBox
     *         the check box
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     * @param isAllowPermission
     *         the is allow permission
     *
     * @return the permission DTO
     *
     * @apiNote To be used in service calls only
     */
    boolean permitPermissionToRole( String userId, CheckBox checkBox, UUID roleId, UUID resourceId, boolean isAllowPermission );

    /**
     * Permit permission to role.
     *
     * @param userId
     *         the user id
     * @param checkBox
     *         the check box
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     * @param isAllowPermission
     *         the is allow permission
     *
     * @return the permission DTO 1
     */
    boolean permitPermissionToRole( EntityManager entityManager, String userId, CheckBox checkBox, UUID roleId, UUID resourceId,
            boolean isAllowPermission );

    /**
     * Gets the all permissions by role id.
     *
     * @param filter
     *         the filter
     * @param userId
     *         the user id
     * @param roleId
     *         the role id
     *
     * @return the all permissions by role id
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< PermissionManageForm > getAllPermissionsByRoleId( FiltersDTO filter, String userId, UUID roleId );

    /**
     * Manage permission table UI.
     *
     * @param userId
     *         the user id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > managePermissionTableUI( String userId );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

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
     * List groups from role id ui table ui.
     *
     * @param roleId
     *         the role id
     *
     * @return the table ui
     */
    TableUI listGroupsFromRoleIdUI( String roleId );

    /**
     * List groups from role id filtered response.
     *
     * @param userId
     *         the user id
     * @param roleId
     *         the role id
     * @param filter
     *         the filter
     *
     * @return the filtered response
     */
    FilteredResponse< SuSUserGroupDTO > listGroupsFromRoleId( String userId, UUID roleId, FiltersDTO filter );

    /**
     * Gets context router for role groups.
     *
     * @param filter
     *         the filter
     *
     * @return the context router for role groups
     */
    List< ContextMenuItem > getContextRouterForRoleGroups( FiltersDTO filter );

    /**
     * Gets all values for role table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for role table column
     */
    List< Object > getAllValuesForRoleTableColumn( String columnName, String token );

}