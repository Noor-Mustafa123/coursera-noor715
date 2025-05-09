package de.soco.software.simuspace.suscore.role.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.role.dao.GroupRoleDAO;
import de.soco.software.simuspace.suscore.role.dao.RoleDAO;
import de.soco.software.simuspace.suscore.role.manager.RoleManager;
import de.soco.software.simuspace.suscore.role.model.RoleDTO;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * This class manages role CRUD related (and other) operation to Dao layer.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@Getter
@Setter
public class RoleManagerImpl implements RoleManager {

    /**
     * The Constant status list key for form.
     */
    public static final String STATUS_KEY = "status";

    /**
     * The Constant groups list key for form.
     */
    public static final String GROUPS_KEY = "groups";

    /**
     * The Constant PERMISSION_PLUGIN_NAME.
     */
    private static final String PERMISSION_PLUGIN_NAME = "plugin_role";

    /**
     * The Constant ROLE_SELECTION_PATH.
     */
    private static final String ROLE_SELECTION_PATH = "system/permissions/group";

    /**
     * The constant GROUP_SELECTION_CONNECTED_TABLE_LABEL.
     */
    public static final String GROUP_SELECTION_CONNECTED_TABLE_LABEL = "name";

    /**
     * The Constant ROLE.
     */
    private static final String ROLE = "Role";

    /**
     * The Constant ADMIN.
     */
    private static final String ADMIN = "Admin";

    /**
     * The Constant DATA_ADMIN.
     */
    private static final String DATA_ADMIN = "Data Admin";

    /**
     * The Constant PROJECT_MANAGER.
     */
    private static final String PROJECT_MANAGER = "Project Manager";

    /**
     * The Constant USER.
     */
    private static final String USER = "User";

    /**
     * The Constant GROUP_NAME.
     */
    private static final String GROUP = "Group";

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The group manager.
     */
    private UserGroupManager userGroupManager;

    /**
     * The role dao.
     */
    private RoleDAO roleDAO;

    /**
     * The group role DAO.
     */
    private GroupRoleDAO groupRoleDAO;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Inits the.
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            persistSystemFeatures( entityManager );
            insertSuperUserSecurityIdentity( entityManager );
            /* Admin role created **/
            if ( roleDAO.readRole( entityManager, ADMIN ) == null ) {
                RoleDTO adminDTO = new RoleDTO( ADMIN, "Admin permitted to all except deleted objects", ConstantsStatus.ACTIVE, null );
                RoleDTO roleAdmin = createRole( entityManager, ConstantsID.SUPER_USER_ID, adminDTO, false );
                insertAllFeatureExcludingDeletedObjects( entityManager, roleAdmin.getId() );
            }

            /* Data Admin role created **/
            if ( roleDAO.readRole( entityManager, DATA_ADMIN ) == null ) {
                RoleDTO dataAdminDTO = new RoleDTO( DATA_ADMIN, "Data Admin permitted only to deleted objects", ConstantsStatus.ACTIVE,
                        null );
                RoleDTO roleDataAdmin = createRole( entityManager, ConstantsID.SUPER_USER_ID, dataAdminDTO, false );
                insertDeletedObjectsAllFeature( entityManager, roleDataAdmin.getId() );
            }

            /* Project Manager role created **/
            if ( roleDAO.readRole( entityManager, PROJECT_MANAGER ) == null ) {
                RoleDTO projectManagerDTO = new RoleDTO( PROJECT_MANAGER,
                        "Project Manager permitted only to Groups up to write level with all unique permissions", ConstantsStatus.ACTIVE,
                        null );
                RoleDTO roleProjectManager = createRole( entityManager, ConstantsID.SUPER_USER_ID, projectManagerDTO, false );
                insertWriteLevel( entityManager, roleProjectManager.getId(), UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
                insertAllUniquePermissions( entityManager, roleProjectManager.getId(),
                        UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
                insertReadLevel( entityManager, roleProjectManager.getId(), UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
            }

            /* User role created **/
            if ( roleDAO.readRole( entityManager, USER ) == null ) {
                RoleDTO userDTO = new RoleDTO( USER, "User provided no permission", ConstantsStatus.ACTIVE, null );
                createRole( entityManager, ConstantsID.SUPER_USER_ID, userDTO, false );
            }

            List< AclObjectIdentityEntity > list = permissionManager.getObjectIdentityDAO()
                    .getAclObjectEntityIDsByInheritFalseAndNullFinalPermission( entityManager );
            for ( AclObjectIdentityEntity aclObjectIdentityEntity : list ) {
                permissionManager.setChildrenFinalACLRecusively( entityManager, aclObjectIdentityEntity );

            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * populates system features.
     *
     * @param entityManager
     *         the entity manager
     */
    private void persistSystemFeatures( EntityManager entityManager ) {
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.LICENSE.getId() ),
                        SuSFeaturesEnum.LICENSE.getKey(), SuSFeaturesEnum.LICENSE.getType(),
                        "License provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.USERS.getId() ),
                        SuSFeaturesEnum.USERS.getKey(), SuSFeaturesEnum.USERS.getType(),
                        "User provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.DIRECTORIES.getId() ),
                        SuSFeaturesEnum.DIRECTORIES.getKey(), SuSFeaturesEnum.DIRECTORIES.getType(),
                        "Directories provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.ROLE.getId() ),
                        SuSFeaturesEnum.ROLE.getKey(), SuSFeaturesEnum.ROLE.getType(), "Role provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.AUDIT.getId() ),
                        SuSFeaturesEnum.AUDIT.getKey(), SuSFeaturesEnum.AUDIT.getType(),
                        "Audit provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.GROUPS.getId() ),
                        SuSFeaturesEnum.GROUPS.getKey(), SuSFeaturesEnum.GROUPS.getType(),
                        "Groups provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.DELETED_OBJECTS.getId() ),
                        SuSFeaturesEnum.DELETED_OBJECTS.getKey(), SuSFeaturesEnum.DELETED_OBJECTS.getType(),
                        "Deleted Objects provided as a feature on system startup." ) );
        permissionManager.addFeatures( entityManager,
                permissionManager.prepareResourceAccessControlDTO( UUID.fromString( SuSFeaturesEnum.LOCATIONS.getId() ),
                        SuSFeaturesEnum.LOCATIONS.getKey(), SuSFeaturesEnum.LOCATIONS.getType(),
                        "Locations provided as a feature on system startup." ) );

    }

    /**
     * Insert superuser security identity.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return true, if successful
     */
    private boolean insertSuperUserSecurityIdentity( EntityManager entityManager ) {
        boolean isInserted = false;
        if ( userCommonManager.getUserById( entityManager, UUID.fromString( ConstantsID.SUPER_USER_ID ) ) == null ) {
            UserEntity userEntity = userCommonManager.getUserCommonDAO().save( entityManager, prepareUserEntityForSuperUser() );
            // update userDetails with mapping
            userEntity.getUserDetails().iterator().next().setUserEntity( userEntity );
            userEntity = userCommonManager.getUserCommonDAO().update( entityManager, userEntity );
            if ( userEntity != null ) {
                isInserted = true;
            }
        } else {
            isInserted = true;
        }
        return isInserted;
    }

    /**
     * Prepare user entity for superuser.
     *
     * @return the user entity
     */
    private UserEntity prepareUserEntityForSuperUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
        AclSecurityIdentityEntity acl = new AclSecurityIdentityEntity();
        acl.setId( UUID.fromString( ConstantsID.SUPER_USER_SID_ID ) );
        acl.setSid( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
        acl.setCreatedOn( new Date() );
        userEntity.setSecurityIdentityEntity( acl );
        userEntity.setDirectory( null );
        userEntity.setUserUid( ConstantsString.SIMUSPACE );
        userEntity.setSurName( ConstantsString.SIMUSPACE );
        userEntity.setFirstName( ConstantsString.SIMUSPACE );
        userEntity.setStatus( true );
        userEntity.setChangeable( true );
        userEntity.setRestricted( false );
        userEntity.setUserDetails( fillUserDetailEntity() );
        userEntity.setPassword( FileUtils.getSha256CheckSum( ConstantsString.SIMUSPACE ) );
        userEntity.setGroups( null );
        userEntity.setCreatedOn( new Date() );
        return userEntity;
    }

    /**
     * Fill user detail entity.
     *
     * @return the sets the
     */
    private Set< UserDetailEntity > fillUserDetailEntity() {
        Set< UserDetailEntity > list = new HashSet<>();
        UserDetailEntity userDetailEntity = new UserDetailEntity( UUID.randomUUID(), ConstantsString.SIMUSPACE, ConstantsString.SIMUSPACE,
                ConstantsString.SIMUSPACE, ConstantsString.SIMUSPACE, ConstantsString.DEFAULT_LANGUAGE, null );
        list.add( userDetailEntity );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    public List< TableColumn > managePermissionTableUI( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), ROLE );
            return permissionManager.managePermissionTableUI();
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< PermissionManageForm > getAllPermissionsByRoleId( FiltersDTO filter, String userId, UUID roleId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            RoleDTO roleModel = readRole( entityManager, userId, roleId );
            List< PermissionManageForm > resourceAccessControlDTOs = permissionManager.getResourceAccessControlList( entityManager,
                    roleModel.getSecurityIdentity() );
            filter.setFilteredRecords( ( long ) resourceAccessControlDTOs.size() );
            filter.setTotalRecords( ( long ) resourceAccessControlDTOs.size() );
            return PaginationUtil.constructFilteredResponse( filter, PaginationUtil.getPaginatedList( resourceAccessControlDTOs, filter ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToRole( String userId, CheckBox checkBox, UUID roleId, UUID resourceId, boolean isAllowPermission ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return permitPermissionToRole( entityManager, userId, checkBox, roleId, resourceId, isAllowPermission );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToRole( EntityManager entityManager, String userId, CheckBox checkBox, UUID roleId, UUID resourceId,
            boolean isAllowPermission ) {
        if ( isAllowPermission ) {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_WRITE.getKey(), ROLE );
        }
        RoleDTO roleModel = readRole( entityManager, userId, roleId );
        /* need security identity to permit permission **/
        AclSecurityIdentityEntity securityIdentityEntity = userManager.getSecurityIdentityDAO()
                .getLatestNonDeletedObjectById( entityManager, roleModel.getSecurityIdentity() );
        return permissionManager.permitFeaturesBySecurityIdentityAndResourceId( entityManager, checkBox, resourceId,
                securityIdentityEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO readRole( String userId, UUID roleId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return readRole( entityManager, userId, roleId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO readRole( EntityManager entityManager, String userId, UUID roleId ) {
        RoleEntity roleEntity = null;
        if ( roleId != null ) {
            roleEntity = roleDAO.readRole( entityManager, roleId );
            if ( roleEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ROLE_WITH_ID_EXIST.getKey(), roleId ) );
            }
            roleEntity.setGroups( groupRoleDAO.getGroupRoleByRoleId( entityManager, roleId ) );
        }
        return prepareRoleModelFromRoleEntity( entityManager, userId, roleEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO createRole( String userId, RoleDTO roleModel, boolean isAllowPermission ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createRole( entityManager, userId, roleModel, isAllowPermission );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO createRole( EntityManager entityManager, String userId, RoleDTO roleModel, boolean isAllowPermission ) {
        UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        if ( isAllowPermission ) {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), ROLE );
        }
        RoleEntity roleEntity = null;
        if ( roleModel != null ) {
            Notification notify = roleModel.validate();
            if ( notify != null && notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }

            roleModel.setGroups( getGroupDtoListFromSelectionId( entityManager, roleModel.getSelectionId() ) );
            roleEntity = prepareRoleEntityFromRoleDTO( roleModel );
            Date currentDate = new Date();
            roleEntity.setCreatedOn( currentDate );
            roleEntity.setModifiedOn( currentDate );
            if ( isAllowPermission && BooleanUtils.isTrue( PropertiesManager.isAuditRole() ) ) {
                roleEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                        "Role :" + roleModel.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED,
                        ConstantsDbOperationTypes.CREATED, userId, "", roleEntity.getName(), "Role" ) );
            }

            AclSecurityIdentityEntity securityIdentityEntity = saveAndReturnSecurityIdentity( entityManager, roleEntity );
            if ( securityIdentityEntity != null ) {
                roleEntity.setSecurityIdentityEntity( securityIdentityEntity );
            }
            roleEntity.setCreatedBy( userEntity );
            roleEntity.setModifiedBy( userEntity );
            roleEntity = roleDAO.saveRole( entityManager, roleEntity );
            for ( SuSUserGroupDTO group : roleModel.getGroups() ) {
                SuSUserGroupDTO userGroup = userGroupManager.readUserGroup( entityManager, userId, group.getId() );
                userGroup.setUpdate( true );
                saveGroupRoleRelationEntity( entityManager,
                        userGroupManager.prepareGroupEntityFromSusUserGroupDTO( entityManager, userGroup ), roleEntity, userEntity );
            }
        }
        return prepareRoleDTOFromRoleEntity( entityManager, userId, roleEntity );
    }

    /**
     * Save group role relation entity.
     *
     * @param group
     *         the group
     * @param roleEntity
     *         the role entity
     */
    private void saveGroupRoleRelationEntity( EntityManager entityManager, GroupEntity group, RoleEntity roleEntity,
            UserEntity userEntity ) {
        GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
        groupRoleEntity.setId( UUID.randomUUID() );
        groupRoleEntity.setGroupEntity( group );
        groupRoleEntity.setRoleEntity( roleEntity );
        Date currentDate = new Date();
        groupRoleEntity.setCreatedOn( currentDate );
        groupRoleEntity.setModifiedOn( currentDate );
        groupRoleEntity.setCreatedBy( userEntity );
        groupRoleEntity.setModifiedBy( userEntity );
        groupRoleDAO.saveGroupRole( entityManager, groupRoleEntity );
    }

    /**
     * Features security identity.
     *
     * @param roleEntity
     *         the role entity
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity saveAndReturnSecurityIdentity( EntityManager entityManager, RoleEntity roleEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( roleEntity.getId() );
        securityIdentityEntity.setPrinciple( Boolean.FALSE );
        securityIdentityEntity = userManager.getSecurityIdentityDAO().save( entityManager, securityIdentityEntity );
        return securityIdentityEntity;
    }

    /**
     * Gets the group dto list from selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the group dto list from selection id
     */
    private List< SuSUserGroupDTO > getGroupDtoListFromSelectionId( EntityManager entityManager, String selectionId ) {
        List< SuSUserGroupDTO > group = new ArrayList<>();

        if ( StringUtils.isEmpty( selectionId ) ) {
            return group;
        }

        List< UUID > selectedUsers = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
        if ( CollectionUtil.isNotEmpty( selectedUsers ) ) {
            for ( UUID uuid : selectedUsers ) {
                SuSUserGroupDTO groupEntity = new SuSUserGroupDTO();
                groupEntity.setId( uuid );
                group.add( groupEntity );

            }
        }
        return group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && "-1".equalsIgnoreCase( filter.getLength().toString() ) ) {

                Long maxResults = roleDAO.getAllFilteredRecordCountWithParentId( entityManager, RoleEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< RoleEntity > allObjectsList = roleDAO.getAllFilteredRecordsWithParent( entityManager, RoleEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.forEach( entity -> itemsList.add( entity.getId() ) );

                filter.setItems( itemsList );
            }

            return contextMenuManager.getContextMenu( entityManager, PERMISSION_PLUGIN_NAME, RoleDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO updateRole( String userId, RoleDTO roleModel ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return updateRole( entityManager, userId, roleModel );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleDTO updateRole( EntityManager entityManager, String userId, RoleDTO roleModel ) {
        UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.WRITE.getValue(),
                Messages.NO_RIGHTS_TO_UPDATE.getKey(), ROLE );
        RoleEntity retriveEntity = null;
        if ( roleModel != null ) {
            retriveEntity = roleDAO.readRole( entityManager, roleModel.getId() );
            if ( retriveEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ROLE_WITH_ID_EXIST.getKey(), roleModel.getId() ) );
            }
            Notification notify = roleModel.validate();

            if ( notify != null && notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }

            if ( StringUtils.isNotBlank( roleModel.getSelectionId() ) ) {
                userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.READ.getValue(),
                        Messages.NO_RIGHTS_TO_READ.getKey(), GROUP );
            }

            roleModel.setGroups( getGroupDtoListFromSelectionId( entityManager, roleModel.getSelectionId() ) );
            prepareRoleEntityFromRoleDTO( roleModel, retriveEntity );
            if ( BooleanUtils.isTrue( PropertiesManager.isAuditDirectory() ) ) {
                AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, retriveEntity, retriveEntity,
                        retriveEntity.getId().toString(), retriveEntity.getName(), "Role" );
                if ( null != auditLog ) {
                    auditLog.setObjectId( retriveEntity.getId().toString() );
                }
                retriveEntity.setAuditLogEntity( auditLog );
            }
            retriveEntity.setModifiedBy( userEntity );
            clearPreviouslySavedGroupRoles( entityManager, retriveEntity );
            for ( SuSUserGroupDTO group : roleModel.getGroups() ) {
                SuSUserGroupDTO userGroup = userGroupManager.readUserGroupWithoutPermission( entityManager, group.getId() );
                if ( null != userGroup ) {
                    userGroup.setUpdate( true );
                    saveGroupRoleRelationEntity( entityManager,
                            userGroupManager.prepareGroupEntityFromSusUserGroupDTO( entityManager, userGroup ), retriveEntity, userEntity );
                }

            }
            retriveEntity = roleDAO.updateRole( entityManager, retriveEntity );
        }

        return prepareRoleModelFromRoleEntity( entityManager, userId, retriveEntity );
    }

    /**
     * Deletes previously saved group roles.
     *
     * @param entityManager
     *         the entity manager
     * @param roleEntity
     *         the role entity
     */
    private void clearPreviouslySavedGroupRoles( EntityManager entityManager, RoleEntity roleEntity ) {
        List< GroupRoleEntity > groupRoleList = groupRoleDAO.getGroupRoleByRoleId( entityManager, roleEntity.getId() );
        if ( groupRoleList != null && !groupRoleList.isEmpty() ) {
            groupRoleDAO.deleteBulk( entityManager, groupRoleList );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteRoleBySelection( String userId, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.DELETE.getValue(),
                    Messages.NO_RIGHTS_TO_DELETE.getKey(), ROLE );
            if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteGroupRoleById( entityManager, userId, UUID.fromString( id.toString() ) );
                }
            } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                deleteGroupRoleById( entityManager, userId, UUID.fromString( selectionId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< RoleDTO > getRoleNamesForUser( EntityManager entityManager, String userId ) {
        UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        List< RoleEntity > roleEntities = getRoleListByUserEntity( entityManager, userEntity );
        List< RoleDTO > roleModels = new ArrayList<>();
        if ( roleEntities != null ) {
            for ( RoleEntity entity : roleEntities ) {
                roleModels.add( prepareRoleModelOnly( entityManager, entity ) );
            }
        }
        return roleModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createRoleForm( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), ROLE );
            entityManager.close();
            List< UIFormItem > list = GUIUtils.prepareForm( true, new RoleDTO() );
            for ( UIFormItem uiFormItem : list ) {
                if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                            ConstantsStatus.ACTIVE, false );
                }
                if ( uiFormItem.getName().equals( GROUPS_KEY ) ) {
                    TableFormItem groupsTable = ( TableFormItem ) uiFormItem;
                    groupsTable.setMultiple( true );
                    groupsTable.setConnected( ROLE_SELECTION_PATH );
                    groupsTable.setConnectedTableLabel( GROUP_SELECTION_CONNECTED_TABLE_LABEL );
                    groupsTable.setSelectable( null );
                    groupsTable.setLabel( MessageBundleFactory.getMessage( Messages.SELECTED_GROUPS.getKey() ) );
                    groupsTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_GROUPS.getKey() ) );
                    groupsTable.setType( FieldTypes.CONNECTED_TABLE.getType() );
                }
            }
            return GUIUtils.createFormFromItems( list );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    @Override
    public UIForm editRoleForm( String userId, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), ROLE );
            RoleDTO dto;
            RoleEntity retriveEntity = roleDAO.readRole( entityManager, id );
            if ( retriveEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ROLE_WITH_ID_EXIST.getKey(), id ) );
            }
            retriveEntity.setGroups( groupRoleDAO.getGroupRoleByRoleId( entityManager, id ) );
            dto = prepareRoleDTOFromRoleEntity( entityManager, userId, retriveEntity );
            List< UIFormItem > list = GUIUtils.prepareForm( false, dto );
            if ( CollectionUtil.isNotEmpty( list ) ) {

                for ( UIFormItem uiFormItem : list ) {
                    if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
                        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                                dto.getStatus(), false );
                    }
                    if ( uiFormItem.getName().equals( GROUPS_KEY ) ) {
                        TableFormItem groupsTable = ( TableFormItem ) uiFormItem;
                        groupsTable.setMultiple( true );
                        groupsTable.setValue( retriveEntity.getSelectionid() );
                        groupsTable.setConnected( ROLE_SELECTION_PATH );
                        groupsTable.setConnectedTableLabel( GROUP_SELECTION_CONNECTED_TABLE_LABEL );
                        groupsTable.setSelectable( null );
                        groupsTable.setLabel( MessageBundleFactory.getMessage( Messages.SELECTED_GROUPS.getKey() ) );
                        groupsTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_GROUPS.getKey() ) );
                        groupsTable.setType( FieldTypes.CONNECTED_TABLE.getType() );
                    }
                }
            }
            return GUIUtils.createFormFromItems( list );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< RoleDTO > getRoleList( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.ROLES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), ROLE );

            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_GROUP.getKey() ) );
            }
            List< FilterColumn > columns = filter.getColumns() != null ? filter.getColumns() : new ArrayList<>();
            for ( FilterColumn filterColumn : columns ) {
                if ( filterColumn.getName().contains( GROUPS_KEY ) ) {
                    filterColumn.setName( ConstantsString.EMPTY_STRING );
                }
            }
            List< RoleDTO > list = new ArrayList<>();
            List< RoleEntity > entityList = roleDAO.getAllFilteredRecords( entityManager, RoleEntity.class, filter );
            if ( CollectionUtil.isNotEmpty( entityList ) ) {
                for ( RoleEntity roleEntity : entityList ) {
                    roleEntity.setGroups( null );
                    if ( !StringUtils.isEmpty( roleEntity.getSelectionid() ) ) {
                        roleEntity.setGroups( groupRoleDAO.getGroupRoleByRoleId( entityManager, roleEntity.getId() ) );
                    }
                    list.add( prepareRoleModelFromRoleEntityInEagerMode( entityManager, userId, roleEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, list );
        } finally {
            entityManager.close();
        }
    }

    private List< RoleEntity > getRoleListByUserEntity( EntityManager entityManager, UserEntity userEntity ) {
        List< RoleEntity > roleEntities = null;
        List< GroupEntity > groupEntities;

        if ( userEntity != null ) {
            groupEntities = new ArrayList<>();
            List< GroupEntity > userGroupEntities = userGroupManager.getUserGroupsByUserId( entityManager, userEntity.getId() );
            if ( userGroupEntities != null ) {
                groupEntities.addAll( userGroupEntities );
                if ( !groupEntities.isEmpty() ) {
                    List< GroupRoleEntity > groupRoleEntities = getGroupRoleEntityList( entityManager, groupEntities );
                    if ( groupRoleEntities != null ) {
                        roleEntities = getRoleEntityListFromGroupRoleEntityList( entityManager, groupRoleEntities );
                    }
                }
            }
        }
        return roleEntities;
    }

    /**
     * Gets role entity list from group role entity list.
     *
     * @param entityManager
     *         the entity manager
     * @param groupRoleEntities
     *         the group role entities
     *
     * @return the role entity list from group role entity list
     */
    private List< RoleEntity > getRoleEntityListFromGroupRoleEntityList( EntityManager entityManager,
            List< GroupRoleEntity > groupRoleEntities ) {
        List< RoleEntity > roleEntities;
        roleEntities = new ArrayList<>();
        for ( GroupRoleEntity groupRoleEntity : groupRoleEntities ) {
            roleEntities.add( roleDAO.getLatestNonDeletedActiveObjectById( entityManager, groupRoleEntity.getRoleEntity().getId() ) );
        }
        return roleEntities;
    }

    /**
     * Gets group role entity list.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntities
     *         the group entities
     *
     * @return the group role entity list
     */
    private List< GroupRoleEntity > getGroupRoleEntityList( EntityManager entityManager, List< GroupEntity > groupEntities ) {
        List< GroupRoleEntity > groupRoleEntities = null;
        if ( CollectionUtil.isNotEmpty( groupEntities ) ) {
            groupRoleEntities = new ArrayList<>();
            for ( GroupEntity groupEntity : groupEntities ) {
                List< GroupRoleEntity > gREntities = groupRoleDAO.getGroupRoleByGroupId( entityManager, groupEntity.getId() );
                if ( gREntities != null ) {
                    groupRoleEntities.addAll( gREntities );
                }
            }
        }
        return groupRoleEntities;
    }

    /**
     * Delete group role by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param roleId
     *         the role id
     */
    private void deleteGroupRoleById( EntityManager entityManager, String userId, UUID roleId ) {
        if ( roleId != null ) {
            RoleEntity entity = roleDAO.readRole( entityManager, roleId );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ROLE_WITH_ID_EXIST.getKey(), roleId ) );
            }
            entity.setDelete( Boolean.TRUE );
            if ( BooleanUtils.isTrue( PropertiesManager.isAuditRole() ) ) {
                entity.setAuditLogEntity(
                        AuditLogDTO.prepareAuditLogEntityForObjects( entity.getName(), ConstantsDbOperationTypes.DELETED, userId, "",
                                entity.getName(), "Role" ) );
            }
            roleDAO.updateRole( entityManager, entity );
        }

    }

    /**
     * Prepare role model from role entity.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the role model
     */
    private RoleDTO prepareRoleModelFromRoleEntity( EntityManager entityManager, String userId, RoleEntity entity ) {
        RoleDTO dto = null;
        if ( entity != null ) {
            dto = new RoleDTO();
            dto.setId( entity.getId() );
            dto.setDescription( entity.getDescription() );
            dto.setStatus( BooleanUtils.isTrue( entity.isStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );
            dto.setName( entity.getName() );
            AclSecurityIdentityEntity securityIdentityEntity = userManager.getSecurityIdentityDAO()
                    .getSecurityIdentityBySid( entityManager, entity.getId() );
            if ( securityIdentityEntity != null ) {
                dto.setSecurityIdentity( securityIdentityEntity.getId() );
            }
            if ( null != entity.getCreatedBy() ) {
                dto.setCreatedBy( userManager.prepareUserModelFromUserEntity( entityManager, entity.getCreatedBy() ) );
            }
            if ( null != entity.getModifiedBy() ) {
                dto.setModifiedBy( userManager.prepareUserModelFromUserEntity( entityManager, entity.getModifiedBy() ) );
            }
            if ( entity.getCreatedOn() != null ) {
                dto.setCreatedOn( DateFormatStandard.format( entity.getCreatedOn() ) );
            }
            if ( entity.getModifiedOn() != null ) {
                dto.setModifiedOn( DateFormatStandard.format( entity.getModifiedOn() ) );
            }
            List< SuSUserGroupDTO > groups = getGroupList( entityManager, userId, entity.getGroups() );
            dto.setGroups( groups );
            dto.setGroupsCount( groups.size() );
        }
        return dto;
    }

    /**
     * Prepare role model from role entity in eager mode.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the role DTO
     */
    private RoleDTO prepareRoleModelFromRoleEntityInEagerMode( EntityManager entityManager, String userId, RoleEntity entity ) {
        RoleDTO dto = null;
        if ( entity != null ) {
            dto = new RoleDTO();
            dto.setId( entity.getId() );
            dto.setDescription( entity.getDescription() );
            dto.setStatus( BooleanUtils.isTrue( entity.isStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );
            dto.setName( entity.getName() );
            dto.setCreatedBy( userCommonManager.prepareUserModelFromUserEntityWithDirectory( entity.getCreatedBy() ) );
            dto.setModifiedBy( userCommonManager.prepareUserModelFromUserEntityWithDirectory( entity.getModifiedBy() ) );
            if ( entity.getCreatedOn() != null ) {
                dto.setCreatedOn( DateFormatStandard.format( entity.getCreatedOn() ) );
            }
            if ( entity.getModifiedOn() != null ) {
                dto.setModifiedOn( DateFormatStandard.format( entity.getModifiedOn() ) );
            }
            List< SuSUserGroupDTO > groups = getGroupList( entityManager, userId, entity.getGroups() );
            dto.setGroups( groups );
            dto.setGroupsCount( groups.size() );
        }
        return dto;
    }

    /**
     * Prepare role model only.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the role model
     */
    private RoleDTO prepareRoleModelOnly( EntityManager entityManager, RoleEntity entity ) {
        RoleDTO dto = null;
        if ( entity != null ) {
            dto = new RoleDTO();
            dto.setId( entity.getId() );

            dto.setDescription( entity.getDescription() );
            dto.setStatus( BooleanUtils.isTrue( entity.isStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );
            dto.setName( entity.getName() );
            AclSecurityIdentityEntity securityIdentityEntity = userManager.getSecurityIdentityDAO()
                    .getSecurityIdentityBySid( entityManager, entity.getId() );
            dto.setSecurityIdentity( securityIdentityEntity.getId() );
        }
        return dto;
    }

    /**
     * Prepare role DTO from role entity.
     *
     * @param entityManager
     *         the entity manager
     * @param roleEntity
     *         the role entity
     *
     * @return the role model
     */
    private RoleDTO prepareRoleDTOFromRoleEntity( EntityManager entityManager, String userId, RoleEntity roleEntity ) {
        RoleDTO roleModel = null;
        if ( roleEntity != null ) {
            roleModel = new RoleDTO();
            roleModel.setId( roleEntity.getId() );
            roleModel.setName( roleEntity.getName() );
            roleModel.setSelectionId( roleEntity.getSelectionid() );
            roleModel.setDescription( roleEntity.getDescription() );
            roleModel.setStatus( BooleanUtils.isTrue( roleEntity.isStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );
            List< SuSUserGroupDTO > groups = getGroupList( entityManager, userId,
                    groupRoleDAO.getGroupRoleByRoleId( entityManager, roleEntity.getId() ) );
            roleModel.setGroups( groups );
            roleModel.setGroupsCount( groups.size() );
            if ( roleEntity.getCreatedOn() != null ) {
                roleModel.setCreatedOn( DateFormatStandard.format( roleEntity.getCreatedOn() ) );
            }
            if ( roleEntity.getModifiedOn() != null ) {
                roleModel.setModifiedOn( DateFormatStandard.format( roleEntity.getModifiedOn() ) );
            }

            if ( null != roleEntity.getCreatedBy() ) {
                roleModel.setCreatedBy( userManager.prepareUserModelFromUserEntity( entityManager, roleEntity.getCreatedBy() ) );
            }
            if ( null != roleEntity.getModifiedBy() ) {
                roleModel.setModifiedBy( userManager.prepareUserModelFromUserEntity( entityManager, roleEntity.getModifiedBy() ) );
            }
        }
        return roleModel;
    }

    /**
     * Get User Model List.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param groupRoleEntities
     *         the group role entities
     *
     * @return the user group model list
     */
    private List< SuSUserGroupDTO > getGroupList( EntityManager entityManager, String userId, List< GroupRoleEntity > groupRoleEntities ) {
        List< SuSUserGroupDTO > suSUserGroupDTOs = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( groupRoleEntities ) ) {
            groupRoleEntities.stream().filter(
                            groupRoleEntity -> groupRoleEntity.getGroupEntity() != null && groupRoleEntity.getGroupEntity().getId() != null )
                    .forEach( groupRoleEntity -> {
                        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
                        suSUserGroupDTOs.add(
                                userGroupManager.readUserGroup( entityManager, userId, groupRoleEntity.getGroupEntity().getId() ) );
                    } );
        }
        return suSUserGroupDTOs;
    }

    /**
     * Prepare role entity from role DTO.
     *
     * @param roleModel
     *         the role model
     *
     * @return the role entity
     */
    private RoleEntity prepareRoleEntityFromRoleDTO( RoleDTO roleModel ) {
        RoleEntity roleEntity = null;
        if ( roleModel != null ) {
            roleEntity = new RoleEntity();
            if ( !roleModel.isUpdate() ) {
                roleEntity.setId( UUID.randomUUID() );
            } else {
                roleEntity.setId( roleModel.getId() );
                roleEntity.setModifiedOn( new Date() );
            }
            roleEntity.setName( roleModel.getName() );
            roleEntity.setSelectionid( roleModel.getSelectionId() );
            roleEntity.setDescription( roleModel.getDescription() );
            roleEntity.setSelectionid( roleModel.getSelectionId() );
            roleEntity.setStatus( roleModel.getStatus().equals( ConstantsStatus.ACTIVE ) );
        }
        return roleEntity;
    }

    /**
     * Prepare role entity from role DTO.
     *
     * @param roleModel
     *         the role model
     */
    private void prepareRoleEntityFromRoleDTO( RoleDTO roleModel, RoleEntity roleEntity ) {
        if ( roleModel != null ) {
            roleEntity.setModifiedOn( new Date() );
            roleEntity.setName( roleModel.getName() );
            roleEntity.setSelectionid( roleModel.getSelectionId() );
            roleEntity.setDescription( roleModel.getDescription() );
            roleEntity.setSelectionid( roleModel.getSelectionId() );
            roleEntity.setStatus( roleModel.getStatus().equals( ConstantsStatus.ACTIVE ) );
        }
    }

    /**
     * Insert feature.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role admin
     */
    private void insertAllFeatureExcludingDeletedObjects( EntityManager entityManager, UUID roleId ) {
        insertGroupAllFeature( entityManager, roleId );
        insertAuditAllFeatures( entityManager, roleId );
        insertRoleAllFeature( entityManager, roleId );
        insertDirectoriesAllFeatures( entityManager, roleId );
        insertUsersAllFeatures( entityManager, roleId );
        insertLicenseAllFeatures( entityManager, roleId );
    }

    /**
     * Insert license features.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertLicenseAllFeatures( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ) );
    }

    /**
     * Insert users features.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertUsersAllFeatures( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ) );
    }

    /**
     * Insert directories features.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertDirectoriesAllFeatures( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
    }

    /**
     * Insert role feature.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertRoleAllFeature( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ) );
    }

    /**
     * Insert audit features.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertAuditAllFeatures( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.AUDIT.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.AUDIT.getId() ) );
    }

    /**
     * Insert group feature.
     *
     * @param roleId
     *         the role id
     */
    private void insertGroupAllFeature( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
    }

    /**
     * Insert deleted objects all feature.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     */
    private void insertDeletedObjectsAllFeature( EntityManager entityManager, UUID roleId ) {
        insertManageLevel( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() ) );
        insertAllUniquePermissions( entityManager, roleId, UUID.fromString( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() ) );
    }

    /**
     * Insert all unique permissions.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertAllUniquePermissions( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID,
                new CheckBox( null, PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), 1 ), roleId, resourceId, false );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.EXECUTE.getValue(), 1 ),
                roleId, resourceId, false );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.KILL.getValue(), 1 ),
                roleId, resourceId, false );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.SHARE.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert manage level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertManageLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        insertRestoreLevel( entityManager, roleId, resourceId );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.MANAGE.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert restore level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertRestoreLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        insertDeleteLevel( entityManager, roleId, resourceId );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.RESTORE.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert delete level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertDeleteLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        insertWriteLevel( entityManager, roleId, resourceId );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.DELETE.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert write level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertWriteLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        insertReadLevel( entityManager, roleId, resourceId );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.WRITE.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert read level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertReadLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        insertViewLevel( entityManager, roleId, resourceId );
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.READ.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * Insert view level.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param resourceId
     *         the resource id
     */
    private void insertViewLevel( EntityManager entityManager, UUID roleId, UUID resourceId ) {
        permitPermissionToRole( entityManager, ConstantsID.SUPER_USER_ID, new CheckBox( null, PermissionMatrixEnum.VIEW.getValue(), 1 ),
                roleId, resourceId, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getUserObjectViewsByKey( String key, String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getUserObjectViewsByKey( entityManager, key, userId, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( ObjectViewDTO viewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveDefaultObjectView( UUID viewId, String userId, String objectViewKey, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveDefaultObjectView( entityManager, viewId, userId, objectViewKey, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO getObjectViewById( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getObjectViewById( entityManager, viewId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, viewId );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForRoleTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( RoleDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = roleDAO.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI listGroupsFromRoleIdUI( String roleId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return new TableUI( GUIUtils.listColumns( SuSUserGroupDTO.class ), userGroupManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.GROUP_TABLE_KEY, roleId, null ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< SuSUserGroupDTO > listGroupsFromRoleId( String userId, UUID roleId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SuSUserGroupDTO > groupList = new ArrayList<>();
            List< GroupRoleEntity > groupRoleEntities = groupRoleDAO.getFilteredGroupsByRole( entityManager, roleId, filter );
            if ( CollectionUtil.isNotEmpty( groupRoleEntities ) ) {
                groupList.addAll( getGroupList( entityManager, userId, groupRoleEntities ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, groupList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouterForRoleGroups( FiltersDTO filter ) {
        return userGroupManager.getContextMenu( filter );
    }

}
