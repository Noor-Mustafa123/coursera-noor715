package de.soco.software.simuspace.suscore.permissions.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.subject.Subject;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CompressionUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.DeletedObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.data.entity.TrainingAlgoNodeEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.interceptors.shiro.dao.CustomSessionDAO;
import de.soco.software.simuspace.suscore.permissions.constants.PermissionManageForm;
import de.soco.software.simuspace.suscore.permissions.dao.AclClassDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclObjectIdentityDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;

/**
 * The Class PermissionManager manages the permission operations to Dao layer.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
@Log4j2
public class PermissionManagerImpl implements PermissionManager {

    /**
     * The Constant DENY.
     */
    private static final int DENY = 0;

    /**
     * The Constant ALLOW.
     */
    private static final int ALLOW = 1;

    /**
     * The Constant USER.
     */
    private static final String USER = "User";

    /**
     * The Constant GROUP.
     */
    private static final String GROUP = "Group";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant SUPER_USER_ID_FROM_FILE.
     */
    public static final String SUPER_USER_ID_FROM_FILE = "0";

    /**
     * The Constant ENTRY_ACL.
     */
    private static final String ENTRY_ACL = "ACL Entry: ";

    /**
     * The Constant USER_KEY.
     */
    private static final String USER_KEY = "user";

    /**
     * The Constant GROUP_KEY.
     */
    private static final String GROUP_KEY = "group";

    /**
     * The class DAO.
     */
    private AclClassDAO classDAO;

    /**
     * The object identity DAO.
     */
    private AclObjectIdentityDAO objectIdentityDAO;

    /**
     * The entry DAO.
     */
    private AclEntryDAO entryDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private JobDAO jobDao;

    private CustomSessionDAO customSessionDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addObjectToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO ) {
        boolean isObjectPermitted;

        AclClassEntity classEntity = addObjectClassToAclClass( entityManager, resourceAccessControlDTO );
        if ( resourceAccessControlDTO.getParent() == null ) {
            isObjectPermitted = addRootObjectToAcl( entityManager, resourceAccessControlDTO, classEntity );
        } else {
            isObjectPermitted = addChildObjectToAcl( entityManager, resourceAccessControlDTO, classEntity );
        }
        return isObjectPermitted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addTreeNodeToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO ) {
        boolean isValid = false;
        if ( classDAO.getLatestNonDeletedObjectById( entityManager, resourceAccessControlDTO.getId() ) == null ) {
            AclClassEntity classEntity = addFeaturesToAclClass( entityManager, resourceAccessControlDTO );
            if ( classEntity != null ) {
                isValid = true;
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Adds the acl class.
     *
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return the acl class entity
     */
    private AclClassEntity addFeaturesToAclClass( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO ) {
        AclClassEntity classEntity = prepareClassEntityFromResourceAccessControlDTOForFeatures( resourceAccessControlDTO );
        classEntity = classDAO.save( entityManager, classEntity );
        return classEntity;
    }

    /**
     * Adds the object to acl class.
     *
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return the acl class entity
     */
    private AclClassEntity addObjectClassToAclClass( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO ) {
        AclClassEntity aclClassEntity;
        aclClassEntity = classDAO.getAclClassByQualifiedName( entityManager, resourceAccessControlDTO.getQualifiedName() );
        if ( aclClassEntity == null ) {
            aclClassEntity = prepareClassEntityFromResourceAccessControlDTOForObject( resourceAccessControlDTO );
            if ( BooleanUtils.isTrue( PropertiesManager.isAuditPermission() ) ) {
                aclClassEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                        ENTRY_ACL + resourceAccessControlDTO.getName(), ConstantsDbOperationTypes.CREATED, ConstantsID.SUPER_USER_ID, "",
                        resourceAccessControlDTO.getName(), "Permissions" ) );
            }
            aclClassEntity = classDAO.save( entityManager, aclClassEntity );

        }
        return aclClassEntity;
    }

    /**
     * Adds the child object to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     * @param classEntity
     *         the class entity
     *
     * @return true, if successful
     */
    private boolean addChildObjectToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO,
            AclClassEntity classEntity ) {
        boolean isObjectPermitted;
        AclObjectIdentityEntity objectIdentityEntity = objectIdentityDAO.save( entityManager,
                prepareObjectIdentity( entityManager, classEntity, resourceAccessControlDTO ) );
        if ( objectIdentityEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_CANT_BE_NULL.getKey() ) );
        }
        if ( resourceAccessControlDTO.getParent().isInherit() ) {
            isObjectPermitted = true;
        } else {
            List< AclEntryEntity > entryEntities = entryDAO.getAclEntryListByObjectId( entityManager,
                    resourceAccessControlDTO.getParent().getId() );
            if ( entryEntities != null ) {
                for ( AclEntryEntity entryEntity : entryEntities ) {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    entryDAO.save( entityManager, permissionDTO.prepareEntryEntityOnly( objectIdentityEntity,
                            entryEntity.getSecurityIdentityEntity(), entryEntity.getMask() ) );
                }
            }
            isObjectPermitted = true;
        }
        return isObjectPermitted;
    }

    /**
     * Adds the root object to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     * @param classEntity
     *         the class entity
     *
     * @return true, if successful
     */
    private boolean addRootObjectToAcl( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO,
            AclClassEntity classEntity ) {
        boolean isObjectPermitted = false;
        AclObjectIdentityEntity objectIdentityEntity = objectIdentityDAO.save( entityManager,
                prepareObjectIdentity( entityManager, classEntity, resourceAccessControlDTO ) );
        // add all permissions of user to ACEs for this object.
        for ( PermissionMatrixEnum permission : PermissionMatrixEnum.values() ) {
            AclSecurityIdentityEntity currentUser = userCommonManager.getAclCommonSecurityIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, resourceAccessControlDTO.getSecurityIdentity() );
            if ( permission.getKey() != PermissionMatrixEnum.NONE.getKey() ) {
                addNewAclEntry( entityManager, resourceAccessControlDTO.getId(), currentUser, permission.getKey() );
            }
        }
        if ( objectIdentityEntity != null ) {
            isObjectPermitted = true;
        }
        return isObjectPermitted;
    }

    /**
     * {@inheritDoc}
     *
     * @throws SusException
     */
    @Override
    public boolean isPermitted( String userId, String resourcePermissionSet ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return isPermitted( entityManager, userId, resourcePermissionSet );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean isPermitted( EntityManager entityManager, String userId, String resourcePermissionSet ) {
        log.debug( ">>isPermitted :userId: " + userId + " : resource: " + resourcePermissionSet );
        if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            return true;
        }
        Subject subject = getSubjectByUserIdFromActiveSessions( entityManager, userId );

        if ( subject != null ) {
            // permission check
            boolean isPermited = subject.isPermitted( resourcePermissionSet );
            // update session last request time
            subject.getSession().touch();
            return isPermited;
        }
        return false;
    }

    /**
     * Gets subject by user id from active sessions.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the subject by user id from active sessions
     */
    private Subject getSubjectByUserIdFromActiveSessions( EntityManager entityManager, String userId ) {
        SuscoreSession sessionEnt = customSessionDAO.getResultByUserId( entityManager, userId );
        if ( sessionEnt != null ) {
            return new Subject.Builder().sessionId( CompressionUtils.deserialize( sessionEnt.getSession() ).getId() ).buildSubject();
        }
        log.warn( "Session not found for userId: " + userId );
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void isPermitted( EntityManager entityManager, String userIdFromGeneralHeader, String permissionQuery, String message,
            String name ) {
        if ( !userIdFromGeneralHeader.equals( ConstantsID.SUPER_USER_ID )
                && !isPermitted( entityManager, userIdFromGeneralHeader, permissionQuery ) ) {
            throw new SusException( MessageBundleFactory.getMessage( message, name ) );
        }
        isRestricted( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
    }

    /**
     * Checks if is restricted.
     *
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitFeaturesBySecurityIdentityAndResourceId( EntityManager entityManager, CheckBox checkBox, UUID resourceId,
            AclSecurityIdentityEntity securityIdentityEntity ) {
        AclObjectIdentityEntity objectIdentityEntity;
        boolean isPermitted = false;
        AclClassEntity classEntity = classDAO.getLatestNonDeletedAclClassEntityById( entityManager, resourceId );
        if ( classEntity != null ) {
            objectIdentityEntity = objectIdentityDAO.getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId( entityManager,
                    classEntity.getId(), securityIdentityEntity.getId() );
            if ( objectIdentityEntity == null ) {
                objectIdentityEntity = prepareObjectIdentityEntityFromPermissionDTO( checkBox, classEntity, securityIdentityEntity );
                objectIdentityEntity = objectIdentityDAO.save( entityManager, objectIdentityEntity );
            }
            isPermitted = permitObjectBySecurityIdentityAndResourceId( entityManager, checkBox, objectIdentityEntity.getId(),
                    securityIdentityEntity );
        }
        return isPermitted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceAccessControlDTO getResourceAccessControl( EntityManager entityManager, UUID id ) {
        ResourceAccessControlDTO resourceAccessControlDTO = null;
        AclObjectIdentityEntity aclObjectIdentityEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager, id );
        if ( aclObjectIdentityEntity != null ) {
            resourceAccessControlDTO = new ResourceAccessControlDTO();
            resourceAccessControlDTO.setId( aclObjectIdentityEntity.getId() );
            resourceAccessControlDTO.setInherit( aclObjectIdentityEntity.isInherit() );
        }
        return resourceAccessControlDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< PermissionManageForm > getResourceAccessControlList( EntityManager entityManager, UUID securityIdentity ) {
        List< PermissionManageForm > allResourcePermissions = null;
        List< AclClassEntity > classEntities = classDAO.getAllObjectList( entityManager );
        if ( CollectionUtil.isNotEmpty( classEntities ) ) {
            List< AclClassEntity > featuresEntities = fetchFeatures( classEntities );
            if ( CollectionUtil.isNotEmpty( featuresEntities ) ) {
                log.info( "Fetch all resources binded with security identity" );
                allResourcePermissions = new ArrayList<>();
                for ( AclClassEntity classEntity : featuresEntities ) {
                    AclObjectIdentityEntity objectIdentityEntity = objectIdentityDAO
                            .getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId( entityManager, classEntity.getId(),
                                    securityIdentity );
                    if ( objectIdentityEntity != null ) {
                        log.info( "objectIdentityEntity: " + objectIdentityEntity.getId() );
                        log.info( "objectIdentityEntity: " + objectIdentityEntity.getClassEntity().getQualifiedName() );
                    }
                    allResourcePermissions
                            .add( preparePermissionManageFormWithPermissionSet( entityManager, classEntity, objectIdentityEntity ) );
                }
            }
        }
        return allResourcePermissions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addFeatures( EntityManager entityManager, ResourceAccessControlDTO resourceAccessControlDTO ) {
        boolean isValid = false;
        if ( resourceAccessControlDTO != null ) {
            isValid = addTreeNodeToAcl( entityManager, resourceAccessControlDTO );
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceAccessControlDTO prepareResourceAccessControlDTO( UUID id, String name, String qualifiedName, String description ) {
        ResourceAccessControlDTO resourceAccessControlDTO = null;
        if ( id != null ) {
            resourceAccessControlDTO = new ResourceAccessControlDTO();
            resourceAccessControlDTO.setId( id );
            resourceAccessControlDTO.setQualifiedName( qualifiedName );
            resourceAccessControlDTO.setName( name );
            resourceAccessControlDTO.setCreatedOn( DateFormatStandard.format( new Date() ) );
            resourceAccessControlDTO.setUpdatedOn( DateFormatStandard.format( new Date() ) );
            resourceAccessControlDTO.setDescription( description );
        }
        return resourceAccessControlDTO;
    }

    /**
     * Fetch features.
     *
     * @param classEntities
     *         the class entities
     *
     * @return the list
     */
    private List< AclClassEntity > fetchFeatures( List< AclClassEntity > classEntities ) {
        List< AclClassEntity > featuresEntities = null;
        if ( CollectionUtil.isNotEmpty( classEntities ) ) {
            featuresEntities = new ArrayList<>();
            for ( AclClassEntity aclClassEntity : classEntities ) {
                Object obj;
                try {
                    Class< ? > clazz = Class.forName( aclClassEntity.getQualifiedName() );
                    obj = clazz.getDeclaredConstructor().newInstance();
                } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
                          | InvocationTargetException | NoSuchMethodException | SecurityException e ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(),
                            aclClassEntity.getQualifiedName() ) );
                }

                if ( obj instanceof RoleEntity || obj instanceof GroupEntity || obj instanceof LicenseEntity || obj instanceof UserEntity
                        || obj instanceof AuditLogEntity || obj instanceof SuSUserDirectoryEntity || obj instanceof DeletedObjectEntity
                        || obj instanceof LocationEntity || obj instanceof LoadCaseProjectEntity || obj instanceof WFSchemeProjectEntity
                        || obj instanceof TrainingAlgoNodeEntity ) {
                    featuresEntities.add( aclClassEntity );
                }
            }
        }
        return featuresEntities;
    }

    /**
     * Prepare resource access control DTO with permission set.
     *
     * @param entityManager
     *         the entity manager
     * @param classEntity
     *         the class entity
     * @param objectIdentityEntity
     *         the object identity entity
     *
     * @return the resource access control DTO
     */
    private PermissionManageForm preparePermissionManageFormWithPermissionSet( EntityManager entityManager, AclClassEntity classEntity,
            AclObjectIdentityEntity objectIdentityEntity ) {
        PermissionManageForm resourceAccessControlDTO;
        List< PermissionDTO > permissionDTOs = new ArrayList<>();
        if ( objectIdentityEntity != null ) {
            permissionDTOs = getPermittedResourceOnIteration( entityManager, objectIdentityEntity, true );
            permissionDTOs = populateNotSelectedOperations( permissionDTOs, true );
            resourceAccessControlDTO = prepareResourceAccessControlByClassEntity( entityManager, classEntity, permissionDTOs,
                    objectIdentityEntity );
        } else {
            permissionDTOs = populateNotSelectedOperations( permissionDTOs, true );
            resourceAccessControlDTO = prepareResourceAccessControlByClassEntity( entityManager, classEntity, permissionDTOs, null );
        }
        return resourceAccessControlDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > managePermissionTableUI() {
        log.info( "prepare resource permission form" );
        Field[] attributes = PermissionManageForm.class.getDeclaredFields();
        return extractColumnList( attributes );
    }

    /**
     * Extract column list.
     *
     * @param attributes
     *         the attributes
     *
     * @return the list
     */
    @Override
    public List< TableColumn > extractColumnList( Field[] attributes ) {
        List< TableColumn > columnsList = new ArrayList<>();
        for ( Field field : attributes ) {
            if ( java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
                continue;
            }
            UIColumn annot = field.getAnnotation( UIColumn.class );
            if ( annot != null
                    && ( annot.name().contentEquals( PermissionManageForm.NAME ) || annot.name().contentEquals( ManageObjectDTO.TYPE ) ) ) {
                columnsList.add( GUIUtils.prepareTableColumn( annot ) );
            } else if ( annot != null && annot.name().contentEquals( ManageObjectDTO.LEVEL_LITERAL ) ) {
                TableColumn levelColumn = GUIUtils.prepareTableColumn( annot );
                levelColumn.getRenderer().setManage( annot.manage() );
                columnsList.add( levelColumn );
            } else if ( annot != null ) {
                int index = 0;
                for ( PermissionMatrixEnum permissionMatrix : PermissionMatrixEnum.values() ) {
                    if ( permissionMatrix.getKey() > PermissionMatrixEnum.MANAGE.getKey() ) {
                        TableColumn moduleColumn = preparePermissionColumn( annot, index, permissionMatrix );
                        moduleColumn.setSortable( false );
                        columnsList.add( moduleColumn );
                        index++;
                    }
                }
            }
        }
        return columnsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadable( EntityManager entityManager, UUID userId, UUID id ) {
        return userId.toString().equals( ConstantsID.SUPER_USER_ID )
                || isPermitted( entityManager, userId.toString(), id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() );
    }

    /**
     * Checks if is writable.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return true, if is writable
     */
    @Override
    public boolean isWritable( EntityManager entityManager, UUID userIdFromGeneralHeader, UUID id ) {
        return userIdFromGeneralHeader.toString().equals( ConstantsID.SUPER_USER_ID ) || isPermitted( entityManager,
                userIdFromGeneralHeader.toString(), id + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() );
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.permissions.manager.PermissionManager#isManageable(java.util.UUID, java.util.UUID)
     */
    @Override
    public boolean isManageable( EntityManager entityManager, UUID userIdFromGeneralHeader, UUID id ) {
        return userIdFromGeneralHeader.toString().equals( ConstantsID.SUPER_USER_ID ) || isPermitted( entityManager,
                userIdFromGeneralHeader.toString(), id + ConstantsString.COLON + PermissionMatrixEnum.MANAGE.getValue() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ManageObjectDTO > prepareObjectManagerDTOs( EntityManager entityManager, UUID objectId, boolean isWriteAble,
            FiltersDTO mainFilter ) {
        List< ManageObjectDTO > objectManageDTOs = new ArrayList<>();
        AclObjectIdentityEntity aclObjectIdentityEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        if ( aclObjectIdentityEntity != null ) {
            if ( aclObjectIdentityEntity.isInherit() ) {
                aclObjectIdentityEntity = propagateToTreeParent( entityManager, aclObjectIdentityEntity );
                isWriteAble = false;
            }
            final List< AclEntryEntity > aclEntryEntities = entryDAO.getAclEntryListByObjectId( entityManager,
                    aclObjectIdentityEntity.getId() );
            if ( CollectionUtil.isNotEmpty( aclEntryEntities ) ) {
                objectManageDTOs = prepareObjectManagerDTOsByUniqueSIDs( entityManager, isWriteAble, aclObjectIdentityEntity.getId(),
                        aclEntryEntities );
            }
        }
        mainFilter.setTotalRecords( ( long ) objectManageDTOs.size() );
        Predicate< ManageObjectDTO > predicate = null;
        if ( mainFilter.getColumns() != null ) {
            for ( FilterColumn filterColumn : mainFilter.getColumns() ) {
                if ( filterColumn.getFilters() != null ) {
                    for ( Filter filter : filterColumn.getFilters() ) {
                        if ( "name".equals( filterColumn.getName() ) && CollectionUtils.isNotEmpty( filterColumn.getFilters() ) ) {
                            predicate = createPredicate( filter );
                        }
                    }
                }
            }
        }

        if ( mainFilter.getSearch() != null && !mainFilter.getSearch().isEmpty() ) {
            objectManageDTOs = objectManageDTOs.stream()
                    .filter( objectManageDTO -> objectManageDTO.getName().contains( mainFilter.getSearch() ) )
                    .toList();
        }
        if ( predicate != null ) {
            objectManageDTOs = objectManageDTOs.stream().filter( predicate ).toList();
        }
        mainFilter.setFilteredRecords( ( long ) objectManageDTOs.size() );
        return objectManageDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getObjectUserIds( EntityManager entityManager, UUID objectId ) {
        List< String > users = new ArrayList<>();

        AclObjectIdentityEntity aclObjectIdentityEntity = getObjectIdentityDAO().getLatestNonDeletedObjectById( entityManager, objectId );
        if ( aclObjectIdentityEntity != null ) {
            if ( aclObjectIdentityEntity.isInherit() ) {
                aclObjectIdentityEntity = propagateToTreeParent( entityManager, aclObjectIdentityEntity );
            }
            final List< AclEntryEntity > aclEntryEntities = entryDAO.getAclEntryListByObjectId( entityManager,
                    aclObjectIdentityEntity.getId() );
            if ( CollectionUtil.isNotEmpty( aclEntryEntities ) ) {
                users = getObjectUserIds( entityManager, aclEntryEntities );
            }
        }
        return users;
    }

    /**
     * Gets object user ids.
     *
     * @param aclEntryEntities
     *         the acl entry entities
     *
     * @return the list
     */
    private List< String > getObjectUserIds( EntityManager entityManager, List< AclEntryEntity > aclEntryEntities ) {
        List< String > userIds = new ArrayList<>();
        final Set< UUID > sidIds = fetchPermittedAclSecurityIdentrities( aclEntryEntities );
        if ( CollectionUtil.isNotEmpty( sidIds ) ) {
            for ( final UUID sidId : sidIds ) {
                UserDTO user = userCommonManager.getUserBySID( entityManager, sidId );

                if ( user != null ) {
                    userIds.add( user.getId() );
                }
            }
        }
        return userIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getObjectGroupIds( EntityManager entityManager, UUID objectId ) {
        List< String > groupIds = new ArrayList<>();

        AclObjectIdentityEntity aclObjectIdentityEntity = getObjectIdentityDAO().getLatestNonDeletedObjectById( entityManager, objectId );
        if ( aclObjectIdentityEntity != null ) {
            if ( aclObjectIdentityEntity.isInherit() ) {
                aclObjectIdentityEntity = propagateToTreeParent( entityManager, aclObjectIdentityEntity );
            }
            final List< AclEntryEntity > aclEntryEntities = entryDAO.getAclEntryListByObjectId( entityManager,
                    aclObjectIdentityEntity.getId() );
            if ( CollectionUtil.isNotEmpty( aclEntryEntities ) ) {
                groupIds = getObjectGroupIds( entityManager, aclEntryEntities );
            }
        }
        return groupIds;
    }

    /**
     * Get object group ids.
     *
     * @param entityManager
     *         the entity manager
     * @param aclEntryEntities
     *         the acl entry entities
     *
     * @return the list
     */
    private List< String > getObjectGroupIds( EntityManager entityManager, List< AclEntryEntity > aclEntryEntities ) {
        List< String > groupIds = new ArrayList<>();
        final Set< UUID > sidIds = fetchPermittedAclSecurityIdentrities( aclEntryEntities );
        if ( CollectionUtil.isNotEmpty( sidIds ) ) {
            for ( final UUID sidId : sidIds ) {
                SuSUserGroupDTO group = userCommonManager.getUserGroupBySID( entityManager, sidId );

                if ( group != null ) {
                    groupIds.add( group.getId().toString() );
                }
            }
        }
        return groupIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToObject( EntityManager entityManager, CheckBox checkBox, UUID objectId, UUID securityId,
            String userIdFromGeneralHeader ) {
        SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        if ( susEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_ID_IS_NOT_VALID.getKey(), objectId ) );
        }
        isPermittedtoManage( entityManager, userIdFromGeneralHeader, objectId.toString() );
        boolean isPermit;
        isWritable( entityManager, UUID.fromString( userIdFromGeneralHeader ), objectId );
        AclSecurityIdentityEntity aclSecurityIdentityEntity = userCommonManager.getAclCommonSecurityIdentityDAO()
                .getLatestNonDeletedObjectById( entityManager, securityId );
        if ( aclSecurityIdentityEntity != null ) {
            UserDTO userDTO = userCommonManager.getUserById( entityManager, aclSecurityIdentityEntity.getSid() );
            isRestricted( checkBox, userDTO );
            isPermit = permitObjectBySecurityIdentityAndResourceId( entityManager, checkBox, objectId, aclSecurityIdentityEntity );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_SECURITY.getKey() ) );
        }
        updateObjectSelectionItems( entityManager, objectId );
        return isPermit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSelectionForNewEntity( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        List< UUID > parentUserSelectionIds = null;
        List< UUID > parentGroupSelectionIds = null;

        if ( parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.DATA.getId() ) )
                || parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.WFSCHEMES.getId() ) )
                || parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) ) {
            // current user selection accordingly
            List< UUID > selectionOfUser = new ArrayList<>();
            selectionOfUser.add( UUID.fromString( userId ) );
            SelectionResponseUI userSelectionId = saveSelectionItems( entityManager, userId, SelectionOrigins.USER.getOrigin(),
                    selectionOfUser );
            createdEntity.setUserSelectionId( userSelectionId.getId() );
        } else {
            if ( parentEntity.getUserSelectionId() != null && !parentEntity.getUserSelectionId().isEmpty() ) {
                parentUserSelectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        parentEntity.getUserSelectionId() );
            }
            if ( parentEntity.getGroupSelectionId() != null && !parentEntity.getGroupSelectionId().isEmpty() ) {
                parentGroupSelectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        parentEntity.getGroupSelectionId() );
            }

            SelectionResponseUI userSelectionId;
            SelectionResponseUI groupSelectionId;
            if ( CollectionUtils.isNotEmpty( parentUserSelectionIds ) ) {
                userSelectionId = saveSelectionItems( entityManager, userId, USER_KEY, parentUserSelectionIds );
                createdEntity.setUserSelectionId( userSelectionId.getId() );
            }
            if ( CollectionUtils.isNotEmpty( parentGroupSelectionIds ) ) {
                groupSelectionId = saveSelectionItems( entityManager, userId, GROUP_KEY, parentGroupSelectionIds );
                createdEntity.setGroupSelectionId( groupSelectionId.getId() );
            }
        }
        susDAO.saveOrUpdate( entityManager, createdEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSelectionForNewJob( EntityManager entityManager, String userId, JobEntity createdEntity, SuSEntity parentEntity ) {
        List< UUID > parentUserSelectionIds = null;
        List< UUID > parentGroupSelectionIds = null;

        if ( parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.DATA.getId() ) )
                || parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.WFSCHEMES.getId() ) )
                || parentEntity.getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) ) {
            // current user selection accordingly
            List< UUID > selectionOfUser = new ArrayList<>();
            selectionOfUser.add( UUID.fromString( userId ) );
            SelectionResponseUI userSelectionId = saveSelectionItems( entityManager, userId, USER_KEY, selectionOfUser );
            createdEntity.setUserSelectionId( userSelectionId.getId() );
        } else {
            if ( parentEntity.getUserSelectionId() != null && !parentEntity.getUserSelectionId().isEmpty() ) {
                parentUserSelectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        parentEntity.getUserSelectionId() );
            }
            if ( parentEntity.getGroupSelectionId() != null && !parentEntity.getGroupSelectionId().isEmpty() ) {
                parentGroupSelectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                        parentEntity.getGroupSelectionId() );
            }

            SelectionResponseUI userSelectionId;
            SelectionResponseUI groupSelectionId;
            if ( CollectionUtils.isNotEmpty( parentUserSelectionIds ) ) {
                userSelectionId = saveSelectionItems( entityManager, userId, USER_KEY, parentUserSelectionIds );
                createdEntity.setUserSelectionId( userSelectionId.getId() );
            }
            if ( CollectionUtils.isNotEmpty( parentGroupSelectionIds ) ) {
                groupSelectionId = saveSelectionItems( entityManager, userId, GROUP_KEY, parentGroupSelectionIds );
                createdEntity.setGroupSelectionId( groupSelectionId.getId() );
            }
        }
        jobDao.saveOrUpdate( entityManager, createdEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceAccessControlDTO prepareResourceAccessControlDTOFromObject( EntityManager entityManager, SuSEntity createdEntity,
            SuSEntity parentEntity ) {
        ResourceAccessControlDTO resourceAccessControlDTO = null;
        if ( createdEntity != null ) {
            resourceAccessControlDTO = new ResourceAccessControlDTO();
            resourceAccessControlDTO.setId( createdEntity.getComposedId().getId() );
            resourceAccessControlDTO.setName( createdEntity.getName() );
            resourceAccessControlDTO.setQualifiedName( createdEntity.getClass().getName() );
            resourceAccessControlDTO.setCreatedOn( DateFormatStandard.format( createdEntity.getCreatedOn() ) );
            resourceAccessControlDTO.setUpdate( true );
            if ( createdEntity.getModifiedOn() != null ) {
                resourceAccessControlDTO.setUpdatedOn( DateFormatStandard.format( createdEntity.getModifiedOn() ) );
            }
            if ( parentEntity != null ) {
                resourceAccessControlDTO.setParent( getResourceAccessControl( entityManager, parentEntity.getComposedId().getId() ) );
            }
        }
        return resourceAccessControlDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceAccessControlDTO prepareResourceAccessControlDTOFromJob( EntityManager entityManager, JobEntity createdEntity,
            SuSEntity parentEntity ) {
        ResourceAccessControlDTO resourceAccessControlDTO = null;
        if ( createdEntity != null ) {
            resourceAccessControlDTO = new ResourceAccessControlDTO();
            resourceAccessControlDTO.setId( createdEntity.getId() );
            resourceAccessControlDTO.setName( createdEntity.getName() );
            resourceAccessControlDTO.setQualifiedName( createdEntity.getClass().getName() );
            resourceAccessControlDTO.setCreatedOn( DateFormatStandard.format( createdEntity.getCreatedOn() ) );
            resourceAccessControlDTO.setUpdate( true );
            if ( createdEntity.getModifiedOn() != null ) {
                resourceAccessControlDTO.setUpdatedOn( DateFormatStandard.format( createdEntity.getModifiedOn() ) );
            }
            if ( parentEntity != null ) {
                resourceAccessControlDTO.setParent( getResourceAccessControl( entityManager, parentEntity.getComposedId().getId() ) );
            }
        }
        return resourceAccessControlDTO;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.permissions.manager.PermissionManager#setChildrenFinalACLRecusively(de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity)
     */
    @Override
    public void setChildrenFinalACLRecusively( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity ) {
        List< AclObjectIdentityEntity > children = objectIdentityDAO.getChildAclObjectIdentitiesById( entityManager,
                aclObjectIdentityEntity.getId() );
        if ( CollectionUtils.isNotEmpty( children ) ) {
            for ( AclObjectIdentityEntity child : children ) {
                if ( child.isInherit() && null != aclObjectIdentityEntity.getFinalParentObject() ) {
                    child.setFinalParentObject( aclObjectIdentityEntity.getFinalParentObject() );
                } else {
                    child.setFinalParentObject( child );
                }
                objectIdentityDAO.saveOrUpdate( entityManager, child );
                setChildrenFinalACLRecusively( entityManager, child );
            }
        }
    }

    /**
     * Improved version of setChildrenFinalACLRecusively; Gets all childrens in one db call instead of recursive.
     *
     * @param aclObjectIdentityEntity
     *         the new children final ACL
     */
    @Override
    public void setChildrenFinalACL( AclObjectIdentityEntity aclObjectIdentityEntity ) {
        Thread thread = new Thread( "New Thread" ) {

            @Override
            public void run() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                List< AclObjectIdentityEntity > allChildrens = objectIdentityDAO.getAclObjectChildTreeFromParentId( entityManager,
                        aclObjectIdentityEntity.getId() );

                if ( CollectionUtils.isNotEmpty( allChildrens ) ) {
                    for ( AclObjectIdentityEntity allChildren : allChildrens ) {
                        if ( allChildren.isInherit() && null != aclObjectIdentityEntity.getFinalParentObject() ) {
                            allChildren.setFinalParentObject( aclObjectIdentityEntity.getFinalParentObject() );
                        } else {
                            allChildren.setFinalParentObject( allChildren );
                        }
                    }
                }

                objectIdentityDAO.saveOrUpdateBulk( entityManager, allChildrens );
            }
        };
        thread.start();
    }

    /**
     * Save selection items.
     *
     * @param userId
     *         the id
     * @param origin
     *         the origin
     * @param selectionIds
     *         the selection ids
     *
     * @return the uuid
     */
    @Override
    public SelectionResponseUI saveSelectionItems( EntityManager entityManager, String userId, String origin, List< UUID > selectionIds ) {
        FiltersDTO filtersDTO = new FiltersDTO();
        List< Object > objects = new ArrayList<>( selectionIds );
        filtersDTO.setItems( objects );
        return selectionManager.createSelection( entityManager, userId, SelectionOrigins.getByOrigin( origin ), filtersDTO );
    }

    /**
     * Update object selection items.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     */
    private void updateObjectSelectionItems( EntityManager entityManager, UUID objectId ) {
        log.debug( ">>updateObjectSelectionItems" );
        SuSEntity entity = susDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        List< UUID > aclSecurityIdentityIds = entryDAO.getAclSecurityIdentityIdsByObjectId( entityManager, objectId );
        List< UUID > permittedUsers = new ArrayList<>();
        List< UUID > permittedGroups = new ArrayList<>();
        for ( UUID aclSecurityIdentityId : aclSecurityIdentityIds ) {
            UserDTO userDTO = userCommonManager.getUserBySID( entityManager, aclSecurityIdentityId );
            SuSUserGroupDTO groupDTO = userCommonManager.getUserGroupBySID( entityManager, aclSecurityIdentityId );
            if ( userDTO != null ) {
                permittedUsers.add( UUID.fromString( userDTO.getId() ) );
            }
            if ( groupDTO != null ) {
                permittedGroups.add( groupDTO.getId() );
            }
        }
        log.debug( ">>updateObjectSelectionItems: permittedUsers: {}", permittedUsers );
        log.debug( ">>updateObjectSelectionItems: permittedGroups: {}", permittedGroups );
        entity.setUserSelectionId( null );
        entity.setGroupSelectionId( null );
        if ( !permittedUsers.isEmpty() ) {
            entity.setUserSelectionId(
                    saveSelectionItems( entityManager, ConstantsID.SUPER_USER_ID, SelectionOrigins.USER.getOrigin(), permittedUsers )
                            .getId() );
        }
        if ( !permittedGroups.isEmpty() ) {
            entity.setGroupSelectionId(
                    saveSelectionItems( entityManager, ConstantsID.SUPER_USER_ID, SelectionOrigins.GROUP.getOrigin(), permittedGroups )
                            .getId() );
        }
        susDAO.saveOrUpdate( entityManager, entity );
    }

    /**
     * Checks if is permitted to manage.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedtoManage( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID )
                && !isPermitted( entityManager, userId, id + ":" + PermissionMatrixEnum.MANAGE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_MANAGE.getKey(), "Object" ) );
        }
    }

    /**
     * Permit object by security identity and resource id.
     *
     * @param entityManager
     *         the entity manager
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param aclSecurityIdentity
     *         the acl security identity
     *
     * @return true, if successful
     */
    private boolean permitObjectBySecurityIdentityAndResourceId( EntityManager entityManager, CheckBox checkBox, UUID objectId,
            AclSecurityIdentityEntity aclSecurityIdentity ) {
        boolean isPermitted;
        if ( "level".equals( checkBox.getName() ) ) {
            isPermitted = addACELevel( entityManager, checkBox, objectId, aclSecurityIdentity );

        } else {
            int mask = PermissionMatrixEnum.getKeyByValue( checkBox.getName() );
            AclEntryEntity entryEntity = entryDAO.getAclEntryEntityByObjectIdAndMaskAndSidId( entityManager, objectId,
                    aclSecurityIdentity.getId(), mask );

            isPermitted = addDeleteAclEntry( entityManager, checkBox, objectId, aclSecurityIdentity, mask, entryEntity );
        }
        return isPermitted;
    }

    /**
     * Adds the ACE level.
     *
     * @param entityManager
     *         the entity manager
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param aclSecurityIdentity
     *         the acl security identity
     *
     * @return true, if successful
     */
    private boolean addACELevel( EntityManager entityManager, CheckBox checkBox, UUID objectId,
            AclSecurityIdentityEntity aclSecurityIdentity ) {
        boolean isPermitted = true;
        int selectedValue = checkBox.getValue();
        // delete all ACEs upto Manage
        List< AclEntryEntity > entryList = entryDAO.getAclEntryListByObjectIdAndSidId( entityManager, objectId,
                aclSecurityIdentity.getId() );

        for ( AclEntryEntity aclEntryEntity : entryList ) {
            log.info( ">>addACELevel entryList deleting" + aclEntryEntity.getMask() );
            if ( aclEntryEntity.getMask() <= PermissionMatrixEnum.MANAGE.getKey() ) {
                entryDAO.delete( entityManager, aclEntryEntity );
            }
        }
        // add new ACEs upto selectedValue
        for ( PermissionMatrixEnum permissionMatrix : PermissionMatrixEnum.values() ) {
            if ( permissionMatrix.getKey() <= selectedValue && permissionMatrix.getKey() != PermissionMatrixEnum.NONE.getKey() ) {
                isPermitted = addNewAclEntry( entityManager, objectId, aclSecurityIdentity, permissionMatrix.getKey() );
            }
        }

        return isPermitted;
    }

    /**
     * Adds the acl entry.
     *
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param aclSecurityIdentity
     *         the acl security identity
     * @param mask
     *         the mask
     * @param entryEntity
     *         the entry entity
     *
     * @return true, if successful
     */
    private boolean addDeleteAclEntry( EntityManager entityManager, CheckBox checkBox, UUID objectId,
            AclSecurityIdentityEntity aclSecurityIdentity, int mask, AclEntryEntity entryEntity ) {
        boolean isPermitted;
        if ( entryEntity == null ) {
            isPermitted = addNewAclEntry( entityManager, objectId, aclSecurityIdentity, mask );
        } else {
            entryEntity.setModifiedOn( new Date() );

            /* Uncheck existing ace **/
            if ( checkBox.getValue() == DENY ) {
                entryDAO.delete( entityManager, entryEntity );

            }
            isPermitted = true;
        }
        return isPermitted;
    }

    /**
     * Adds the new acl entry.
     *
     * @param objectId
     *         the object id
     * @param aclSecurityIdentity
     *         the acl security identity
     * @param mask
     *         the mask
     *
     * @return true, if successful
     */
    private boolean addNewAclEntry( EntityManager entityManager, UUID objectId, AclSecurityIdentityEntity aclSecurityIdentity, int mask ) {
        boolean isPermitted = false;
        AclEntryEntity entryEntity = new AclEntryEntity();
        entryEntity.setId( UUID.randomUUID() );
        entryEntity.setDelete( false );
        entryEntity.setMask( mask );
        entryEntity.setCreatedOn( new Date() );
        entryEntity.setSecurityIdentityEntity( aclSecurityIdentity );
        AclObjectIdentityEntity aclObjectIdentityEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        if ( aclObjectIdentityEntity != null ) {
            entryEntity.setObjectIdentityEntity( aclObjectIdentityEntity );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_RESOURCE.getKey() ) );
        }
        entryEntity = entryDAO.save( entityManager, entryEntity );
        if ( entryEntity != null ) {
            isPermitted = true;
        }
        return isPermitted;
    }

    /**
     * Checks if is restricted.
     *
     * @param checkBox
     *         the check box
     * @param userDTO
     *         the user DTO
     *
     * @return true, if is restricted
     */
    private boolean isRestricted( CheckBox checkBox, UserDTO userDTO ) {
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER )
                && ( !checkBox.getName().equals( PermissionMatrixEnum.VIEW.getValue() )
                && !checkBox.getName().equals( PermissionMatrixEnum.READ.getValue() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_OBJECT.getKey(), userDTO.getName() ) );
        }
        return true;
    }

    /**
     * Propagate to tree parent.
     *
     * @param parentObjectIdentityEntity
     *         the parent object identity entity
     *
     * @return the object identity entity
     */
    private AclObjectIdentityEntity propagateToTreeParent( EntityManager entityManager,
            AclObjectIdentityEntity parentObjectIdentityEntity ) {
        if ( parentObjectIdentityEntity.isInherit() ) {
            parentObjectIdentityEntity = getObjectIdentityDAO().getLatestNonDeletedObjectById( entityManager,
                    parentObjectIdentityEntity.getParentObject().getId() );
            return propagateToTreeParent( entityManager, parentObjectIdentityEntity );
        } else {
            return parentObjectIdentityEntity;
        }
    }

    /**
     * Prepare object manager DTOs by unique SIds.
     *
     * @param entityManager
     *         the entity manager
     * @param isWriteAble
     *         the is write able
     * @param objectId
     *         the object id
     * @param aclEntryEntities
     *         the acl entry entities
     *
     * @return the list
     */
    private List< ManageObjectDTO > prepareObjectManagerDTOsByUniqueSIDs( EntityManager entityManager, boolean isWriteAble, UUID objectId,
            List< AclEntryEntity > aclEntryEntities ) {
        List< ManageObjectDTO > objectManageDTOs = new ArrayList<>();
        final Set< UUID > sidIds = fetchPermittedAclSecurityIdentrities( aclEntryEntities );
        if ( CollectionUtil.isNotEmpty( sidIds ) ) {
            objectManageDTOs = new ArrayList<>();
            for ( final UUID sidId : sidIds ) {
                final ManageObjectDTO objectManagerDTO = showSingleGrantedUserOrGroupForObject( entityManager, objectId, sidId,
                        isWriteAble );
                if ( objectManagerDTO != null ) {
                    objectManageDTOs.add( objectManagerDTO );
                }
            }
        }
        return objectManageDTOs;
    }

    /**
     * Fetch permitted acl security identrities.
     *
     * @param aclEntryEntities
     *         the acl entry entities
     *
     * @return the sets the
     */
    private Set< UUID > fetchPermittedAclSecurityIdentrities( List< AclEntryEntity > aclEntryEntities ) {
        Set< UUID > sidIds = null;
        if ( CollectionUtil.isNotEmpty( aclEntryEntities ) ) {
            sidIds = new HashSet<>();
            for ( final AclEntryEntity aclEntryEntity : aclEntryEntities ) {
                sidIds.add( aclEntryEntity.getSecurityIdentityEntity().getId() );
            }
        }
        return sidIds;
    }

    /**
     * Show single granted user or group for object.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param securityConsumer
     *         the security consumer
     * @param isWriteable
     *         the is writeable
     *
     * @return the object manager DTO
     */
    private ManageObjectDTO showSingleGrantedUserOrGroupForObject( EntityManager entityManager, UUID objectId, UUID securityConsumer,
            boolean isWriteable ) {
        ManageObjectDTO objectManageDTO = null;

        boolean isGreyedOut = false;
        final AclSecurityIdentityEntity aclSecurityIdentityEntity = userCommonManager.getAclCommonSecurityIdentityDAO()
                .getLatestNonDeletedObjectById( entityManager, securityConsumer );

        final UserDTO userDTO = userCommonManager.getUserById( entityManager, aclSecurityIdentityEntity.getSid() );
        if ( userDTO != null ) {
            if ( userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
                isGreyedOut = true;
            }
            objectManageDTO = prepareObjectNameAndType( userDTO.getUserUid(), USER, securityConsumer );
        } else {
            final SuSUserGroupDTO suSUserGroupDTO = userCommonManager.getUserGroupById( entityManager, aclSecurityIdentityEntity.getSid() );
            if ( suSUserGroupDTO != null ) {
                objectManageDTO = prepareObjectNameAndType( suSUserGroupDTO.getName(), GROUP, securityConsumer );
            } else {
                return objectManageDTO;
            }
        }
        final List< AclEntryEntity > aclEntryEntities = entryDAO.getAclEntryListByObjectIdAndSidId( entityManager, objectId,
                securityConsumer );

        if ( CollectionUtil.isNotEmpty( aclEntryEntities ) ) {
            final List< PermissionDTO > permissionDTOs = getPermittedObjectOnIteration( aclEntryEntities, isWriteable );
            if ( isGreyedOut && isWriteable ) {
                greyedOutRestrictedUser( permissionDTOs );
                objectManageDTO.setManage( false );
            }
            if ( !isWriteable ) {
                objectManageDTO.setManage( false );
            }
            objectManageDTO.setPermissionDTOs( permissionDTOs );

            objectManageDTO.setLevel( Arrays.asList( PermissionMatrixEnum.NONE.getValue(), PermissionMatrixEnum.VIEW.getValue(),
                    PermissionMatrixEnum.READ.getValue(), PermissionMatrixEnum.WRITE.getValue(), PermissionMatrixEnum.DELETE.getValue(),
                    PermissionMatrixEnum.RESTORE.getValue(), PermissionMatrixEnum.MANAGE.getValue() ) );
            objectManageDTO.setCurrentLevel( computeCurrentLevel( aclEntryEntities ) );

        }
        return objectManageDTO;
    }

    /**
     * Compute current level.
     *
     * @param aclEntryEntities
     *         the acl entry entities
     *
     * @return the string
     */
    private String computeCurrentLevel( List< AclEntryEntity > aclEntryEntities ) {
        String currentLevel = PermissionMatrixEnum.NONE.getValue();
        Map< String, Integer > levelMap = new HashMap<>();
        for ( AclEntryEntity aclEntryEntity : aclEntryEntities ) {
            if ( aclEntryEntity.getMask() <= PermissionMatrixEnum.MANAGE.getKey() ) {
                levelMap.put( PermissionMatrixEnum.getValueByKey( aclEntryEntity.getMask() ), aclEntryEntity.getMask() );
            }
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.MANAGE.getValue() ) ) {
            return PermissionMatrixEnum.MANAGE.getValue();
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.RESTORE.getValue() ) ) {
            return PermissionMatrixEnum.RESTORE.getValue();
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.DELETE.getValue() ) ) {
            return PermissionMatrixEnum.DELETE.getValue();
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.WRITE.getValue() ) ) {
            return PermissionMatrixEnum.WRITE.getValue();
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.READ.getValue() ) ) {
            return PermissionMatrixEnum.READ.getValue();
        }
        if ( levelMap.containsKey( PermissionMatrixEnum.VIEW.getValue() ) ) {
            return PermissionMatrixEnum.VIEW.getValue();
        }
        return currentLevel;
    }

    /**
     * Gets the permitted resource on iteration.
     *
     * @param aclEntryEntities
     *         the acl entry entities
     * @param isWriteable
     *         the is writeable
     *
     * @return the permitted resource on iteration
     */
    private List< PermissionDTO > getPermittedObjectOnIteration( List< AclEntryEntity > aclEntryEntities, boolean isWriteable ) {
        List< PermissionDTO > permissionDTOs = null;
        if ( aclEntryEntities != null ) {
            permissionDTOs = getPermittedResourceOnIteration( aclEntryEntities, isWriteable );
            permissionDTOs = populateNotSelectedOperations( permissionDTOs, isWriteable );
        }
        return permissionDTOs;
    }

    /**
     * Greyed out restricted user.
     *
     * @param permissionDTOs
     *         the permission DT os
     */
    private void greyedOutRestrictedUser( List< PermissionDTO > permissionDTOs ) {
        for ( final PermissionDTO permissionDTO : permissionDTOs ) {
            permissionDTO.setManage( ( PermissionMatrixEnum.VIEW.getKey() == permissionDTO.getMatrixKey() )
                    || ( PermissionMatrixEnum.READ.getKey() == permissionDTO.getMatrixKey() ) );
        }
    }

    /**
     * Gets the permitted resource on iteration.
     *
     * @param aclEntryEntities
     *         the acl entry entities
     * @param isInherit
     *         the is inherit
     *
     * @return the permitted resource on iteration
     */
    private List< PermissionDTO > getPermittedResourceOnIteration( List< AclEntryEntity > aclEntryEntities, boolean isInherit ) {
        List< PermissionDTO > permissionDTOs;
        permissionDTOs = new ArrayList<>();
        for ( final AclEntryEntity entryEntity : aclEntryEntities ) {
            final PermissionDTO permissionDTO = preparePermissionDTOFromEntryEntity( entryEntity );
            if ( permissionDTO != null ) {
                permissionDTO.setManage( isInherit );
                permissionDTOs.add( permissionDTO );
            }
        }
        return permissionDTOs;
    }

    /**
     * Prepare object name and type.
     *
     * @param name
     *         the name
     * @param type
     *         the type
     * @param sidId
     *         the sid id
     *
     * @return the object manager DTO
     */
    private ManageObjectDTO prepareObjectNameAndType( String name, String type, UUID sidId ) {
        ManageObjectDTO objectManageDTO;
        objectManageDTO = new ManageObjectDTO();
        objectManageDTO.setName( name );
        objectManageDTO.setType( type );
        objectManageDTO.setSidId( sidId );
        objectManageDTO.setManage( true );
        return objectManageDTO;
    }

    /**
     * Prepare permission column.
     *
     * @param annot
     *         the annot
     * @param index
     *         the index
     * @param permissionMatrix
     *         the permission matrix
     *
     * @return the table column
     */
    private TableColumn preparePermissionColumn( UIColumn annot, int index, PermissionMatrixEnum permissionMatrix ) {
        TableColumn moduleColumn = new TableColumn();
        moduleColumn.setData( annot.data().replace( PermissionManageForm.INDEX_PATTERN, ConstantsString.EMPTY_STRING + index ) );
        moduleColumn.setName( annot.name().replace( PermissionManageForm.PERMISSION_NAME_PATTERN, permissionMatrix.getValue() ) );
        moduleColumn.setTitle( permissionMatrix.getValue() );
        Renderer renderer = new Renderer();
        renderer.setType( annot.renderer() );
        renderer.setManage( annot.manage().replace( PermissionManageForm.INDEX_PATTERN, ConstantsString.EMPTY_STRING + index ) );
        moduleColumn.setRenderer( renderer );
        return moduleColumn;
    }

    /**
     * Populate not selected operations.
     *
     * @param permissionDTOs
     *         the permission DT os
     * @param isManageable
     *         the is inherit
     *
     * @return the list
     */
    private List< PermissionDTO > populateNotSelectedOperations( List< PermissionDTO > permissionDTOs, boolean isManageable ) {

        List< PermissionDTO > returnPermissionDTOs = new ArrayList<>();
        for ( PermissionMatrixEnum permissionMatrix : PermissionMatrixEnum.values() ) {
            if ( permissionMatrix.getKey() <= PermissionMatrixEnum.MANAGE.getKey() ) {
                continue;
            }
            boolean isAllowed = false;
            if ( permissionDTOs != null ) {
                for ( PermissionDTO permissionDTO : permissionDTOs ) {
                    if ( permissionDTO.getMatrixKey() == permissionMatrix.getKey() ) {
                        permissionDTO.setValue( ALLOW );
                        permissionDTO.setMatrexValue( permissionMatrix.getValue() );
                        permissionDTO.setMatrixKey( PermissionMatrixEnum.getKeyByValue( permissionMatrix.getValue() ) );
                        permissionDTO.setManage( isManageable );
                        returnPermissionDTOs.add( permissionDTO );
                        isAllowed = true;
                        break;
                    }
                }
            }
            if ( !isAllowed ) {
                PermissionDTO emptyPermission = new PermissionDTO();
                emptyPermission.setValue( DENY );
                emptyPermission.setMatrixKey( PermissionMatrixEnum.getKeyByValue( permissionMatrix.getValue() ) );
                emptyPermission.setMatrexValue( permissionMatrix.getValue() );
                emptyPermission.setManage( isManageable );
                returnPermissionDTOs.add( emptyPermission );
            }
        }
        return returnPermissionDTOs;

    }

    /**
     * Gets the permitted resource on iteration.
     *
     * @param entityManager
     *         the entity manager
     * @param objectIdentityEntity
     *         the object identity entity
     * @param isManageable
     *         the is inherit
     *
     * @return the permitted resource on iteration
     */
    private List< PermissionDTO > getPermittedResourceOnIteration( EntityManager entityManager,
            AclObjectIdentityEntity objectIdentityEntity, boolean isManageable ) {
        List< PermissionDTO > permissionDTOs = null;

        List< AclEntryEntity > entryEntities = entryDAO.getAclEntryListByObjectId( entityManager, objectIdentityEntity.getId() );
        if ( entryEntities != null ) {
            permissionDTOs = new ArrayList<>();
            for ( AclEntryEntity entryEntity : entryEntities ) {
                PermissionDTO permissionDTO = preparePermissionDTOFromEntryEntity( entryEntity );
                if ( permissionDTO != null ) {
                    permissionDTO.setManage( isManageable );
                    permissionDTOs.add( permissionDTO );
                }
            }
        }
        return permissionDTOs;
    }

    /**
     * Prepare resource access control by class entity.
     *
     * @param entityManager
     *         the entity manager
     * @param classEntity
     *         the class entity
     * @param permissionDTOs
     *         the permission DT os
     * @param objectIdentityEntity
     *         the object identity entity
     *
     * @return the resource access control DTO
     */
    private PermissionManageForm prepareResourceAccessControlByClassEntity( EntityManager entityManager, AclClassEntity classEntity,
            List< PermissionDTO > permissionDTOs, AclObjectIdentityEntity objectIdentityEntity ) {
        PermissionManageForm resourceAccessControlDTO = null;
        if ( classEntity != null ) {
            resourceAccessControlDTO = new PermissionManageForm();
            resourceAccessControlDTO.setId( classEntity.getId() );
            String featureName = "";
            for ( SuSFeaturesEnum featuresEnumFullyQualified : SuSFeaturesEnum.values() ) {
                if ( classEntity.getId().equals( UUID.fromString( featuresEnumFullyQualified.getId() ) ) ) {
                    featureName = featuresEnumFullyQualified.getKey();
                }
            }
            if ( StringUtils.isNullOrEmpty( featureName ) ) {
                resourceAccessControlDTO.setName( classEntity.getName() );
            } else {
                resourceAccessControlDTO.setName( featureName );
            }
            if ( permissionDTOs != null ) {
                resourceAccessControlDTO.setPermissionDTOs( permissionDTOs );
            }
            if ( objectIdentityEntity != null ) {
                resourceAccessControlDTO.setCurrentLevel(
                        computeCurrentLevel( entryDAO.getAclEntryListByObjectId( entityManager, objectIdentityEntity.getId() ) ) );
            } else {
                resourceAccessControlDTO.setCurrentLevel( PermissionMatrixEnum.NONE.getValue() );
            }
            resourceAccessControlDTO.setLevel( Arrays.asList( PermissionMatrixEnum.NONE.getValue(), PermissionMatrixEnum.VIEW.getValue(),
                    PermissionMatrixEnum.READ.getValue(), PermissionMatrixEnum.WRITE.getValue(), PermissionMatrixEnum.DELETE.getValue(),
                    PermissionMatrixEnum.RESTORE.getValue(), PermissionMatrixEnum.MANAGE.getValue() ) );
            resourceAccessControlDTO.setManage( true );

        }
        return resourceAccessControlDTO;
    }

    /**
     * Prepare permission DTO from entry entity.
     *
     * @param entryEntity
     *         the entry entity
     *
     * @return the permission DTO
     */
    private PermissionDTO preparePermissionDTOFromEntryEntity( AclEntryEntity entryEntity ) {
        PermissionDTO permissionDTO = null;
        if ( entryEntity != null ) {
            permissionDTO = new PermissionDTO();
            permissionDTO.setId( entryEntity.getId() );
            permissionDTO.setMatrixKey( entryEntity.getMask() );
            permissionDTO.setMatrexValue( PermissionMatrixEnum.getValueByKey( entryEntity.getMask() ) );
            permissionDTO.setValue( 1 );
            if ( entryEntity.getCreatedOn() != null ) {
                permissionDTO.setCreatedOn( entryEntity.getCreatedOn() );
            }
            if ( entryEntity.getModifiedOn() != null ) {
                permissionDTO.setUpdatedOn( entryEntity.getModifiedOn() );
            }
        }
        return permissionDTO;
    }

    /**
     * Prepare object identity entity from permission DTO.
     *
     * @param checkBox
     *         the check box
     * @param classEntity
     *         the class entity
     * @param securityIdentityEntity
     *         the security identity entity
     *
     * @return the object identity entity
     */
    private AclObjectIdentityEntity prepareObjectIdentityEntityFromPermissionDTO( CheckBox checkBox, AclClassEntity classEntity,
            AclSecurityIdentityEntity securityIdentityEntity ) {
        AclObjectIdentityEntity objectIdentityEntity = null;
        if ( checkBox != null ) {
            objectIdentityEntity = new AclObjectIdentityEntity();
            if ( classEntity != null && securityIdentityEntity != null ) {
                objectIdentityEntity.setId( UUID.randomUUID() );
                objectIdentityEntity.setClassEntity( classEntity );
                objectIdentityEntity.setOwnerSid( securityIdentityEntity );
                objectIdentityEntity.setCreatedOn( new Date() );
                objectIdentityEntity.setFinalParentObject( objectIdentityEntity );
            }
        }
        return objectIdentityEntity;
    }

    /**
     * Prepare class entity from resource access control DTO for features.
     *
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return the acl class entity
     */
    private AclClassEntity prepareClassEntityFromResourceAccessControlDTOForFeatures( ResourceAccessControlDTO resourceAccessControlDTO ) {
        AclClassEntity entity = null;
        if ( resourceAccessControlDTO != null ) {
            entity = new AclClassEntity();
            entity.setId( resourceAccessControlDTO.getId() );
            entity.setQualifiedName( resourceAccessControlDTO.getQualifiedName() );
            entity.setName( resourceAccessControlDTO.getName() );
            entity.setDescription( resourceAccessControlDTO.getDescription() );
            entity.setCreatedOn( new Date() );
        }
        return entity;
    }

    /**
     * Prepare class entity from resource access control DTO for object.
     *
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return the acl class entity
     */
    private AclClassEntity prepareClassEntityFromResourceAccessControlDTOForObject( ResourceAccessControlDTO resourceAccessControlDTO ) {
        AclClassEntity entity = null;
        if ( resourceAccessControlDTO != null ) {
            entity = new AclClassEntity();
            entity.setId( UUID.randomUUID() );
            entity.setName( resourceAccessControlDTO.getName() );
            entity.setQualifiedName( resourceAccessControlDTO.getQualifiedName() );
            entity.setDescription( resourceAccessControlDTO.getDescription() );
            entity.setCreatedOn( new Date() );
        }
        return entity;
    }

    /**
     * Prepare object identity.
     *
     * @param entityManager
     *         the entity manager
     * @param classEntity
     *         the class entity
     * @param resourceAccessControlDTO
     *         the resource access control DTO
     *
     * @return the acl object identity entity
     */
    private AclObjectIdentityEntity prepareObjectIdentity( EntityManager entityManager, AclClassEntity classEntity,
            ResourceAccessControlDTO resourceAccessControlDTO ) {
        AclObjectIdentityEntity objectIdentityEntity = new AclObjectIdentityEntity();
        objectIdentityEntity.setId( resourceAccessControlDTO.getId() );
        if ( resourceAccessControlDTO.getParent() != null ) {
            objectIdentityEntity.setInherit( resourceAccessControlDTO.getParent().isInherit() );
        }
        objectIdentityEntity.setCreatedOn( new Date() );
        if ( resourceAccessControlDTO.getParent() != null ) {
            AclObjectIdentityEntity parentObjectIdentityEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager,
                    resourceAccessControlDTO.getParent().getId() );
            objectIdentityEntity.setParentObject( parentObjectIdentityEntity );

            if ( parentObjectIdentityEntity != null ) {
                SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, parentObjectIdentityEntity.getId() );
                // inheritance always on for first level project's objects
                if ( susEntity != null ) {
                    List< SuSEntity > parents = susDAO.getParents( entityManager, susEntity );
                    if ( CollectionUtils.isNotEmpty( parents )
                            && ( parents.get( 0 ).getComposedId().getId().equals( UUID.fromString( SimuspaceFeaturesEnum.DATA.getId() ) )
                            || parents.get( 0 ).getComposedId().getId()
                            .equals( UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) ) ) {
                        objectIdentityEntity.setInherit( true );
                    }
                }
                //

                if ( objectIdentityEntity.isInherit() && null != parentObjectIdentityEntity.getFinalParentObject() ) {
                    objectIdentityEntity.setFinalParentObject( parentObjectIdentityEntity.getFinalParentObject() );
                } else {
                    objectIdentityEntity.setFinalParentObject( objectIdentityEntity );
                }
            }
        } else {
            objectIdentityEntity.setFinalParentObject( objectIdentityEntity );
        }

        AclSecurityIdentityEntity aclSecurityIdentityEntity = userCommonManager.getAclCommonSecurityIdentityDAO()
                .getLatestNonDeletedObjectById( entityManager, resourceAccessControlDTO.getSecurityIdentity() );
        objectIdentityEntity.setOwnerSid( aclSecurityIdentityEntity );
        objectIdentityEntity.setClassEntity( classEntity );
        return objectIdentityEntity;
    }

    /**
     * Creates the predicate.
     *
     * @param filter
     *         the filter
     *
     * @return the predicate
     */
    private Predicate< ManageObjectDTO > createPredicate( Filter filter ) {
        Predicate< ManageObjectDTO > predicate;
        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            predicate = objectType -> objectType.getName().equals( filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            predicate = objectType -> !objectType.getName().equals( filter.getValue() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            predicate = objectType -> objectType.getName().toLowerCase().contains( filter.getValue().toLowerCase() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            predicate = objectType -> !objectType.getName().toLowerCase().contains( filter.getValue().toLowerCase() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            predicate = objectType -> objectType.getName().toLowerCase().startsWith( filter.getValue().toLowerCase() );
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            predicate = objectType -> objectType.getName().toLowerCase().endsWith( filter.getValue().toLowerCase() );
        } else {
            predicate = objectType -> objectType.getName().toLowerCase().contains( filter.getValue().toLowerCase() );
        }
        return predicate;
    }

    /**
     * Sets the class DAO.
     *
     * @param classDAO
     *         the new class DAO
     */
    public void setClassDAO( AclClassDAO classDAO ) {
        this.classDAO = classDAO;
    }

    /**
     * Gets the object identity DAO.
     *
     * @return the object identity DAO
     */
    @Override
    public AclObjectIdentityDAO getObjectIdentityDAO() {
        return objectIdentityDAO;
    }

    /**
     * Sets the object identity DAO.
     *
     * @param objectIdentityDAO
     *         the new object identity DAO
     */
    public void setObjectIdentityDAO( AclObjectIdentityDAO objectIdentityDAO ) {
        this.objectIdentityDAO = objectIdentityDAO;
    }

    /**
     * Gets the entry DAO.
     *
     * @return the entry DAO
     */
    @Override
    public AclEntryDAO getEntryDAO() {
        return entryDAO;
    }

    /**
     * Sets the entry DAO.
     *
     * @param entryDAO
     *         the new entry DAO
     */
    public void setEntryDAO( AclEntryDAO entryDAO ) {
        this.entryDAO = entryDAO;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
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
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    @Override
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    @Override
    public void updateParentInAclEntity( EntityManager entityManager, String userId, UUID objectId, UUID newParentId ) {
        var childAclEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        var parentAclEntity = objectIdentityDAO.getLatestNonDeletedObjectById( entityManager, newParentId );
        childAclEntity.setParentObject( parentAclEntity );
        if ( childAclEntity.isInherit() && parentAclEntity.isInherit() ) {
            childAclEntity.setFinalParentObject( parentAclEntity.getFinalParentObject() );
        } else if ( childAclEntity.isInherit() ) {
            childAclEntity.setFinalParentObject( parentAclEntity );
        }
        objectIdentityDAO.saveOrUpdate( entityManager, childAclEntity );
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Gets custom session dao.
     *
     * @return the custom session dao
     */
    public CustomSessionDAO getCustomSessionDAO() {
        return customSessionDAO;
    }

    /**
     * Sets custom session dao.
     *
     * @param customSessionDAO
     *         the custom session dao
     */
    public void setCustomSessionDAO( CustomSessionDAO customSessionDAO ) {
        this.customSessionDAO = customSessionDAO;
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

    public void setJobDao( JobDAO jobDao ) {
        this.jobDao = jobDao;
    }

}
