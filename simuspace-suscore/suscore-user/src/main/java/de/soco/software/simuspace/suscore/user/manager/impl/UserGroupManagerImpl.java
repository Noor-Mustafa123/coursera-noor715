package de.soco.software.simuspace.suscore.user.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserProfile;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.ComparisonDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.GroupRoleCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.user.dao.UserGroupDAO;
import de.soco.software.simuspace.suscore.user.manager.UserGroupManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * Implementation of UserGroupManager Interface to communicate with DAO layer for dataBank.
 *
 * @author Nosheen.Sharif
 */
public class UserGroupManagerImpl implements UserGroupManager {

    /**
     * The userManager reference.
     */
    private UserManager userManager;

    /**
     * The userGroupDao reference.
     */
    private UserGroupDAO userGroupDao;

    /**
     * The group role common DAO.
     */
    private GroupRoleCommonDAO groupRoleCommonDAO;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant user list key for form.
     */
    public static final String USER_KEY = "users";

    /**
     * The Constant status list key for form.
     */
    public static final String STATUS_KEY = "status";

    /**
     * The Constant USER_PLUGIN_NAME key.
     */
    public static final String USER_PLUGIN_NAME = "plugin_user";

    /**
     * The Constant GROUP_NAME.
     */
    public static final String GROUP_NAME = "Group Created With Name: ";

    /**
     * The Constant GROUP_SELECTION_PATH.
     */
    private static final String USER_SELECTION_PATH = "system/user";

    /**
     * The constant USER_SELECTION_CONNECTED_TABLE_LABEL.
     */
    public static final String USER_SELECTION_CONNECTED_TABLE_LABEL = "userUid";

    /**
     * The Constant GROUP_NAME.
     */
    public static final String GROUP = "Group";

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO createUserGroup( String userId, SuSUserGroupDTO userGroupDto ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), GROUP );
            UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );

            GroupEntity groupEntity = null;
            if ( userGroupDto != null ) {
                Notification notify = userGroupDto.validate();

                if ( notify != null && notify.hasErrors() ) {
                    throw new SusException( notify.getErrors().toString() );
                }
                userGroupDto.setUsers( getUsersDtoListFromSelectionId( entityManager, userGroupDto.getSelectionId() ) );
                List< AuditLogEntity > logs = createAuditLogForUsers( entityManager, userId, groupEntity, userGroupDto );
                groupEntity = prepareGroupEntityFromSusUserGroupDTO( entityManager, userGroupDto );
                if ( Boolean.TRUE.equals( PropertiesManager.isAuditGroup() ) ) {
                    groupEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects( GROUP_NAME + userGroupDto.getName(),
                            ConstantsDbOperationTypes.CREATED, userId, "", groupEntity.getName(), GROUP ) );
                }
                AclSecurityIdentityEntity securityIdentityEntity = saveSecurityIdentity( entityManager, groupEntity );
                if ( securityIdentityEntity != null ) {
                    groupEntity.setSecurityIdentityEntity( securityIdentityEntity );
                }
                groupEntity.setCreatedOn( new Date() );
                groupEntity.setCreatedBy( userEntity );
                groupEntity.setModifiedBy( userEntity );

                groupEntity = userGroupDao.createUserGroup( entityManager, groupEntity, logs );
            }
            return prepareSusUserGroupDTOFromGroupEntity( entityManager, groupEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the audit log for users.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param groupEntity
     *         the group entity
     * @param dto
     *         the dto
     *
     * @return the list
     */
    private List< AuditLogEntity > createAuditLogForUsers( EntityManager entityManager, String userId, GroupEntity groupEntity,
            SuSUserGroupDTO dto ) {
        List< AuditLogEntity > list = null;
        List< UUID > oldList = new ArrayList<>();
        if ( userId != null && !userId.equals( ConstantsUserProfile.DEFAULT_USER_ID ) ) {
            // do something good

            if ( groupEntity != null ) {
                oldList = CollectionUtil.isNotEmpty( groupEntity.getUsers() ) ? groupEntity.getUsers().stream().map( UserEntity::getId )
                        .toList() : new ArrayList<>();
            }
            List< String > newList = CollectionUtil.isNotEmpty( dto.getUsers() ) ? dto.getUsers().stream().map( UserDTO::getId ).toList()
                    : new ArrayList<>();

            ComparisonDTO idsList = CollectionUtil.getComparedList( CollectionUtil.getUUIDListFromStringType( newList ), oldList );

            list = prepareUserAuditLogList( entityManager, userId, dto, idsList );

        }

        return list;
    }

    /**
     * Prepare user audit log list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param group
     *         the group entity
     * @param idsList
     *         the ids list
     *
     * @return the list
     */
    private List< AuditLogEntity > prepareUserAuditLogList( EntityManager entityManager, String userId, SuSUserGroupDTO group,
            ComparisonDTO idsList ) {
        List< AuditLogEntity > list = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( idsList.getAdded() ) ) {
            List< UserEntity > usersAdded = userManager.getUsersByUidList( entityManager, idsList.getAdded() );

            for ( UserEntity user : usersAdded ) {
                list.add( AuditLogDTO.prepareAuditLogEntityForObjects( user.getUserUid() + " assigned To Group " + group.getName(),
                        ConstantsDbOperationTypes.ASSIGNED, userId, null != group.getId() ? group.getId().toString() : "", group.getName(),
                        GROUP ) );
            }
        }

        if ( CollectionUtil.isNotEmpty( idsList.getRemoved() ) ) {
            List< UserEntity > usersRemoved = userManager.getUsersByUidList( entityManager, idsList.getRemoved() );

            for ( UserEntity user : usersRemoved ) {
                list.add( AuditLogDTO.prepareAuditLogEntityForObjects( user.getUserUid() + " removed from Group " + group.getName(),
                        ConstantsDbOperationTypes.DE_ASSIGN, userId, null != group.getId() ? group.getId().toString() : "", group.getName(),
                        GROUP ) );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO updateUserGroup( String userId, SuSUserGroupDTO userGroupDto ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            GroupEntity groupEntity = null;
            UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), GROUP );
            if ( userGroupDto != null ) {

                GroupEntity retriveEntity = userGroupDao.getGroupAndUsersByGroupId( entityManager, userGroupDto.getId() );
                if ( retriveEntity == null ) {
                    throw new SusException(
                            MessageBundleFactory.getMessage( Messages.NO_GROUP_WITH_ID_EXIST.getKey(), userGroupDto.getId() ) );
                }
                Notification notify = userGroupDto.validate();
                if ( notify != null && notify.hasErrors() ) {
                    throw new SusException( notify.getErrors().toString() );
                }
                userGroupDto.setUsers( getUsersDtoListFromSelectionId( entityManager, userGroupDto.getSelectionId() ) );
                List< AuditLogEntity > logs = createAuditLogForUsers( entityManager, userId, retriveEntity, userGroupDto );
                entityManager.detach( retriveEntity );
                groupEntity = prepareGroupEntityFromSusUserGroupDTO( entityManager, userGroupDto );
                groupEntity.setCreatedOn( retriveEntity.getCreatedOn() );
                groupEntity.setCreatedBy( retriveEntity.getCreatedBy() );
                if ( Boolean.TRUE.equals( PropertiesManager.isAuditGroup() ) ) {
                    AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, retriveEntity, groupEntity,
                            groupEntity.getId().toString(), groupEntity.getName(), GROUP );
                    if ( null != auditLog ) {
                        auditLog.setObjectId( retriveEntity.getId().toString() );
                    }
                    groupEntity.setAuditLogEntity( auditLog );
                }
                groupEntity.setModifiedBy( userEntity );
                groupEntity.setSecurityIdentityEntity( retriveEntity.getSecurityIdentityEntity() );
                groupEntity = userGroupDao.updateUserGroup( entityManager, groupEntity, logs );

            }
            return prepareSusUserGroupDTOFromGroupEntity( entityManager, groupEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteUserGroupBySelection( String userId, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.DELETE.getValue(),
                    Messages.NO_RIGHTS_TO_DELETE.getKey(), GROUP );
            if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteUserGroupById( entityManager, userId, UUID.fromString( id.toString() ) );
                }
            } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                deleteUserGroupById( entityManager, userId, UUID.fromString( selectionId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO readUserGroup( String userId, UUID groupId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return readUserGroup( entityManager, userId, groupId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO readUserGroup( EntityManager entityManager, String userId, UUID groupId ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), GROUP );
        return readUserGroupWithoutPermission( entityManager, groupId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI listUsersFromGroupIdUI( String userId, UUID groupId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return new TableUI( userManager.listUsersUI(), userManager.getObjectViewManager()
                    .getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.USER_TABLE_KEY, userId, null ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserDTO > listUsersFromGroupId( String userId, UUID groupId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UserDTO > users = new ArrayList<>();

            GroupEntity groupEntity = userGroupDao.readUserGroup( entityManager, groupId );
            List< UserEntity > userEntities = userManager.getFilteredUsersByGroupEntity( entityManager, groupEntity, filter );
            if ( userEntities != null ) {
                for ( UserEntity userEntity : userEntities ) {
                    users.add( userManager.prepareUserModelFromUserEntity( entityManager, userEntity ) );
                }
            }

            return PaginationUtil.constructFilteredResponse( filter, users );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO readUserGroupWithoutPermission( EntityManager entityManager, UUID groupId ) {
        GroupEntity groupEntity = null;
        if ( groupId != null ) {
            groupEntity = userGroupDao.readUserGroup( entityManager, groupId );
            if ( groupEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_GROUP_WITH_ID_EXIST.getKey(), groupId ) );
            }
        }
        return prepareSusUserGroupDTOFromGroupEntity( entityManager, groupEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Object > getAllValuesForGroupsTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( SuSUserGroupDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = userGroupDao.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     */
    @Override
    public FilteredResponse< SuSUserGroupDTO > getUserGroupsList( String userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getUserGroupsList( entityManager, userId, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< SuSUserGroupDTO > getUserGroupsList( EntityManager entityManager, String userId, FiltersDTO filter ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), GROUP );
        if ( filter == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
        }

        List< SuSUserGroupDTO > list = new ArrayList<>();
        List< GroupEntity > entityList = userGroupDao.getAllFilteredRecords( entityManager, GroupEntity.class, filter );

        if ( CollectionUtil.isNotEmpty( entityList ) ) {
            for ( GroupEntity group : entityList ) {
                list.add( prepareSusUserGroupDTOFromGroupEntity( entityManager, group ) );
            }
        }
        return PaginationUtil.constructFilteredResponse( filter, list );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createUserGroupForm( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), GROUP );
            List< UIFormItem > list = GUIUtils.prepareForm( true, new SuSUserGroupDTO() );

            for ( UIFormItem uiFormItem : list ) {
                if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptionsForUser(),
                            ConstantsStatus.ACTIVE, false );
                }

                if ( uiFormItem.getName().equals( USER_KEY ) ) {

                    TableFormItem usersTable = ( TableFormItem ) uiFormItem;
                    usersTable.setMultiple( true );
                    usersTable.setConnected( USER_SELECTION_PATH );
                    usersTable.setConnectedTableLabel( USER_SELECTION_CONNECTED_TABLE_LABEL );
                    usersTable.setSelectable( null );
                    usersTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_USERS.getKey() ) );
                    usersTable.setType( FieldTypes.CONNECTED_TABLE.getType() );
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
    public List< ContextMenuItem > getContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // case of select all from data table
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = userGroupDao.getAllFilteredRecordCountWithParentId( entityManager, GroupEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< GroupEntity > allObjectsList = userGroupDao.getAllFilteredRecordsWithParent( entityManager, GroupEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( entity -> itemsList.add( entity.getId() ) );

                filter.setItems( itemsList );
            }

            return contextMenuManager.getContextMenu( entityManager, USER_PLUGIN_NAME, SuSUserGroupDTO.class, filter );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextRouter( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return userManager.getContextRouter( entityManager, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editUserGroupForm( String userId, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.GROUPS.getId(), PermissionMatrixEnum.WRITE.getValue(),
                    Messages.NO_RIGHTS_TO_UPDATE.getKey(), GROUP );
            GroupEntity retriveEntity = userGroupDao.readUserGroup( entityManager, id );
            if ( retriveEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_GROUP_WITH_ID_EXIST.getKey(), id ) );
            }
            retriveEntity = userGroupDao.getGroupAndUsersByGroupId( entityManager, retriveEntity.getId() );
            SuSUserGroupDTO dto = prepareSusUserGroupDTOFromGroupEntity( entityManager, retriveEntity );

            List< UIFormItem > list = GUIUtils.prepareForm( false, dto );

            editUserGroupFormUIList( dto, list );
            return GUIUtils.createFormFromItems( list );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Edits the user group form UI list.
     *
     * @param dto
     *         the dto
     * @param list
     *         the list
     */
    private void editUserGroupFormUIList( SuSUserGroupDTO dto, List< UIFormItem > list ) {
        if ( CollectionUtil.isNotEmpty( list ) ) {

            for ( UIFormItem uiFormItem : list ) {
                if ( dto != null ) {
                    if ( uiFormItem.getName().equals( STATUS_KEY ) ) {
                        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptionsForUser(),
                                dto.getStatus(), false );
                    }
                    if ( uiFormItem.getName().equals( USER_KEY ) ) {

                        TableFormItem usersTable = ( TableFormItem ) uiFormItem;
                        usersTable.setMultiple( true );
                        usersTable.setConnected( USER_SELECTION_PATH );
                        usersTable.setConnectedTableLabel( USER_SELECTION_CONNECTED_TABLE_LABEL );
                        usersTable.setValue( dto.getSelectionId() );
                        usersTable.setSelectable( null );
                        usersTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_USERS.getKey() ) );
                        usersTable.setType( FieldTypes.CONNECTED_TABLE.getType() );

                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GroupEntity > getUserGroupsByUserId( EntityManager entityManager, UUID userUuid ) {
        return userGroupDao.getUserGroupsByUserId( entityManager, userUuid );
    }

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
    @Override
    public SuSUserGroupDTO prepareSusUserGroupDTOFromGroupEntity( EntityManager entityManager, GroupEntity entity ) {
        SuSUserGroupDTO dto = null;
        if ( entity != null ) {
            dto = new SuSUserGroupDTO();
            dto.setSelectionId( entity.getSelectionId() );
            dto.setId( entity.getId() );

            dto.setDescription( entity.getDescription() );
            dto.setStatus( Boolean.TRUE.equals( entity.getStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
            dto.setName( entity.getName() );
            if ( entity.getSecurityIdentityEntity() != null ) {
                AclSecurityIdentityEntity securityIdentityEntity = userManager.getSecurityIdentityDAO()
                        .getSecurityIdentityBySid( entityManager, entity.getId() );
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
                dto.setUpdatedOn( DateFormatStandard.format( entity.getModifiedOn() ) );
            }
            List< UserEntity > userEntities = userManager.getUsersByGroupEntity( entityManager, entity );
            if ( userEntities != null ) {
                dto.setUsers( getUserModelList( new HashSet<>( userEntities ) ) );
            }
            if ( userEntities != null ) {
                dto.setUsersCount( userEntities.size() );
            } else {
                dto.setUsersCount( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
        }
        return dto;
    }

    /**
     * Delete User Group By Id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param groupId
     *         the group id
     */
    private void deleteUserGroupById( EntityManager entityManager, String userId, UUID groupId ) {
        if ( groupId != null ) {
            GroupEntity entity = userGroupDao.readUserGroup( entityManager, groupId );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_GROUP_WITH_ID_EXIST.getKey(), groupId ) );
            }
            entity.setDelete( Boolean.TRUE );
            if ( Boolean.TRUE.equals( PropertiesManager.isAuditGroup() ) ) {
                entity.setAuditLogEntity(
                        AuditLogDTO.prepareAuditLogEntityForObjects( entity.getName(), ConstantsDbOperationTypes.DELETED, userId, "",
                                entity.getName(), GROUP ) );
            }
            entity.setUsers( new HashSet<>() );
            removeRelationFromGroupRole( entityManager, groupId );
            userGroupDao.updateUserGroup( entityManager, entity, null );
        }
    }

    /**
     * Removes the relation from group role.
     *
     * @param groupId
     *         the group id
     */
    private void removeRelationFromGroupRole( EntityManager entityManager, UUID groupId ) {
        List< GroupRoleEntity > groupRoleEntities = groupRoleCommonDAO.getGroupRoleByGroupId( entityManager, groupId );
        for ( GroupRoleEntity groupRoleEntity : groupRoleEntities ) {

            FiltersDTO removeId = new FiltersDTO();
            List< Object > removeAbleItems = new ArrayList<>();
            removeAbleItems.add( groupId );
            removeId.setItems( removeAbleItems );
            selectionManager.removeSelectionItemsInExistingSelection( entityManager, groupRoleEntity.getRoleEntity().getSelectionid(),
                    removeId );

            groupRoleEntity.setRoleEntity( null );
            groupRoleEntity.setGroupEntity( null );
            groupRoleEntity = groupRoleCommonDAO.update( entityManager, groupRoleEntity );
            if ( groupRoleEntity != null ) {
                groupRoleCommonDAO.delete( entityManager, groupRoleEntity );
            }
        }
    }

    /**
     * Get User Model List.
     *
     * @param users
     *         the users
     *
     * @return List<UserDTO>
     */
    private List< UserDTO > getUserModelList( Set< UserEntity > users ) {
        List< UserDTO > list = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( users ) ) {
            for ( UserEntity userGrp : users ) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserUid( userGrp.getUserUid() );
                list.add( userDTO );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity prepareGroupEntityFromSusUserGroupDTO( EntityManager entityManager, SuSUserGroupDTO dto ) {
        GroupEntity entity = null;
        if ( dto != null ) {
            entity = new GroupEntity();
            if ( !dto.isUpdate() ) {
                entity.setId( UUID.randomUUID() );
            } else {
                entity.setId( dto.getId() );
            }

            entity.setName( dto.getName() );
            entity.setSelectionId( dto.getSelectionId() );
            entity.setDescription( dto.getDescription() );
            entity.setStatus( dto.getStatus().equals( ConstantsStatus.ACTIVE ) );
            entity.setModifiedOn( new Date() );
            if ( !StringUtils.isBlank( dto.getSelectionId() ) ) {
                entity.setUsers( getUsersFromSelections( entityManager, dto ) );
            }
        }
        return entity;
    }

    /**
     * Gets the users form selections.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     *
     * @return the users form selections
     */
    private Set< UserEntity > getUsersFromSelections( EntityManager entityManager, SuSUserGroupDTO dto ) {

        Set< UserEntity > userSet = new HashSet<>();
        List< UUID > slectedUsers = selectionManager.getSelectedIdsListBySelectionId( entityManager, dto.getSelectionId() );

        if ( CollectionUtil.isNotEmpty( slectedUsers ) ) {
            for ( UUID uuid : slectedUsers ) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId( uuid );
                userSet.add( userEntity );

            }
        }

        return userSet;
    }

    /**
     * Gets the users dto list from selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param selectionId
     *         the selection id
     *
     * @return the users dto list from selection id
     */
    private List< UserDTO > getUsersDtoListFromSelectionId( EntityManager entityManager, String selectionId ) {
        List< UserDTO > users = new ArrayList<>();
        if ( StringUtils.isNotEmpty( selectionId ) ) {
            List< UUID > slectedUsers = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            if ( CollectionUtil.isNotEmpty( slectedUsers ) ) {
                for ( UUID uuid : slectedUsers ) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId( uuid.toString() );
                    users.add( userDTO );
                }
            }
        }
        return users;
    }

    /**
     * User security identity.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the group entity
     *
     * @return the security identity entity
     */
    private AclSecurityIdentityEntity saveSecurityIdentity( EntityManager entityManager, GroupEntity groupEntity ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( groupEntity.getId() );
        securityIdentityEntity.setPrinciple( false );
        securityIdentityEntity.setCreatedOn( new Date() );
        securityIdentityEntity = userManager.getSecurityIdentityDAO().save( entityManager, securityIdentityEntity );
        return securityIdentityEntity;
    }

    /**
     * Sets the user group dao.
     *
     * @param userGroupDao
     *         the userGroupDao to set
     */
    public void setUserGroupDao( UserGroupDAO userGroupDao ) {
        this.userGroupDao = userGroupDao;
    }

    /**
     * Gets the user manager.
     *
     * @return the userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the userManager to set
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * sets context Menu Manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * Sets the group role common DAO.
     *
     * @param groupRoleCommonDAO
     *         the new group role common DAO
     */
    public void setGroupRoleCommonDAO( GroupRoleCommonDAO groupRoleCommonDAO ) {
        this.groupRoleCommonDAO = groupRoleCommonDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< SuSUserGroupDTO > getAllGroupSelections( String selectionId, String systemUserIdFromGeneralHeader,
            FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SelectionItemEntity > selectedUserIds = selectionManager.getUserSelectionsBySelectionIds( entityManager, selectionId,
                    filter );
            List< SuSUserGroupDTO > userGroupDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( SelectionItemEntity user : selectedUserIds ) {
                    SuSUserGroupDTO group = prepareSusUserGroupDTOFromGroupEntity( entityManager,
                            userGroupDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( user.getItem() ) ) );
                    if ( group != null ) {
                        userGroupDTOList.add( group );
                    }
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, userGroupDTOList );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< SuSUserGroupDTO > getAllGroupsBySelectionId( String selectionId, String systemUserIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectedUserIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            List< SuSUserGroupDTO > userGroupDTOList = new ArrayList<>();
            if ( CollectionUtil.isNotEmpty( selectedUserIds ) ) {
                for ( UUID grpId : selectedUserIds ) {
                    SuSUserGroupDTO group = prepareSusUserGroupDTOFromGroupEntity( entityManager,
                            userGroupDao.getLatestNonDeletedObjectById( entityManager, grpId ) );
                    if ( group != null ) {
                        userGroupDTOList.add( group );
                    }
                }
            }
            return userGroupDTOList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
