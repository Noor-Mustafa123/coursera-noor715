package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserProfile;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserGroupDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserDetail;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonEntryDAO;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonObjectIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.common.dao.GroupCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LoginAttemptCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.RoleCommonDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDirectoryAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;

/**
 * The class is responsible to expose the common function for user as blueprint service.
 *
 * @author M.Nasir.Farooq
 */
public class UserCommonManagerImpl implements UserCommonManager {

    /**
     * The user common DAO.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The group common DAO.
     */
    private GroupCommonDAO groupCommonDAO;

    /**
     * The Role common dao.
     */
    private RoleCommonDAO roleCommonDAO;

    /**
     * The acl common security identity DAO.
     */
    private AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO;

    /**
     * The acl common object identity DAO.
     */
    private AclCommonObjectIdentityDAO aclCommonObjectIdentityDAO;

    /**
     * The login attempt common DAO.
     */
    private LoginAttemptCommonDAO loginAttemptCommonDAO;

    /**
     * The entry DAO.
     */
    private AclCommonEntryDAO entryDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO createUserGroup( EntityManager entityManager, String userId, SuSUserGroupDTO userGroupDto ) {
        GroupEntity groupEntity = null;
        if ( userGroupDto != null ) {
            Notification notify = userGroupDto.validate();

            if ( notify != null && notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }
            groupEntity = prepareGroupEntityFromSusUserGroupDTO( userGroupDto );
            if ( userId != null && !ConstantsUserProfile.DEFAULT_USER_ID.equals( userId ) && PropertiesManager.isAuditGroup() ) {
                groupEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects( userGroupDto.getName(),
                        ConstantsDbOperationTypes.CREATED, userId, "", groupEntity.getName(), "Group" ) );
            }
            AclSecurityIdentityEntity securityIdentityEntity = saveSecurityIdentity( entityManager, groupEntity.getId() );
            if ( securityIdentityEntity != null ) {
                groupEntity.setSecurityIdentityEntity( securityIdentityEntity );
            }
            groupEntity = groupCommonDAO.createUserGroup( entityManager, groupEntity );
        }
        return prepareSusUserGroupDTOFromGroupEntity( groupEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclSecurityIdentityEntity getSecurityIdentityBySidId( EntityManager entityManager, UUID sidId ) {
        return aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( entityManager, sidId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< UserDTO > getFilteredUsers( EntityManager entityManager, FiltersDTO filter ) {

        if ( filter == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
        }

        List< UserDTO > userDTOList = new ArrayList<>();
        List< UserEntity > userEntitiesList = userCommonDAO.getAllFilteredRecords( entityManager, UserEntity.class, filter );
        if ( CollectionUtil.isNotEmpty( userEntitiesList ) ) {
            for ( UserEntity user : userEntitiesList ) {
                userDTOList.add( prepareUserModelFromUserEntityWithDirectory( user ) );
            }
        }
        return PaginationUtil.constructFilteredResponse( filter, userDTOList );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO updateUserType( EntityManager entityManager, UUID userId, boolean type ) {

        UserEntity userEntity = userCommonDAO.getLatestNonDeletedObjectById( entityManager, userId );

        if ( null != userEntity ) {
            userEntity.setRestricted( type );
            return prepareUserModelFromUserEntityWithDirectory( userCommonDAO.merge( entityManager, userEntity ) );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey() ) );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserById( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = null;
        UserEntity userEntity = userCommonDAO.getLatestNonDeletedObjectById( entityManager, userId );
        if ( null != userEntity ) {
            userDTO = prepareUserModelFromUserEntity( userEntity );
        }
        return userDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserBySID( EntityManager entityManager, UUID sId ) {
        UserDTO userDTO = null;
        UserEntity userEntity = userCommonDAO.readUserBySId( entityManager, sId );
        if ( null != userEntity ) {
            userDTO = prepareUserModelFromUserEntityWithDirectory( userEntity );
        }
        return userDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserByUserUid( EntityManager entityManager, String userUid ) {
        UserDTO userDTO = null;
        UserEntity userEntity = userCommonDAO.readUserByUserUid( entityManager, userUid );
        if ( null != userEntity ) {
            userDTO = prepareUserModelFromUserEntityWithDirectory( userEntity );
        }
        return userDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO getUserGroupById( EntityManager entityManager, UUID groupId ) {
        SuSUserGroupDTO suSUserGroupDTO = null;
        GroupEntity groupEntity = groupCommonDAO.getActiveGroupEntity( entityManager, groupId );
        if ( null != groupEntity ) {
            suSUserGroupDTO = prepareSusUserGroupDTOFromGroupEntity( groupEntity );
        }
        return suSUserGroupDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO getUserGroupByIdForBreadcrumb( EntityManager entityManager, UUID groupId ) {
        SuSUserGroupDTO suSUserGroupDTO = null;
        GroupEntity groupEntity = groupCommonDAO.getGroupEntity( entityManager, groupId );
        if ( null != groupEntity ) {
            suSUserGroupDTO = prepareSusUserGroupDTOFromGroupEntity( groupEntity );
        }
        return suSUserGroupDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserGroupDTO getUserGroupBySID( EntityManager entityManager, UUID sId ) {
        SuSUserGroupDTO suSUserGroupDTO = null;
        GroupEntity groupEntity = groupCommonDAO.readUserGroupBySId( entityManager, sId );
        if ( null != groupEntity ) {
            suSUserGroupDTO = prepareSusUserGroupDTOFromGroupEntity( groupEntity );
        }
        return suSUserGroupDTO;
    }

    /**
     * Prepare sus user group DTO from group entity.
     *
     * @param entity
     *         the entity
     *
     * @return the suS user group DTO
     */
    private SuSUserGroupDTO prepareSusUserGroupDTOFromGroupEntity( GroupEntity entity ) {
        SuSUserGroupDTO dto = null;
        if ( entity != null ) {
            dto = new SuSUserGroupDTO();
            dto.setId( entity.getId() );
            dto.setName( entity.getName() );
            dto.setUsers( prepareUserDTOList( entity.getUsers() ) );
        }
        return dto;
    }

    /**
     * Prepare user DTO list.
     *
     * @param users
     *         the users
     *
     * @return the list
     */
    private List< UserDTO > prepareUserDTOList( Set< UserEntity > users ) {
        List< UserDTO > userDTOList = new ArrayList<>();
        if ( users == null || users.isEmpty() ) {
            return null;
        }
        for ( UserEntity userEntity : users ) {
            userDTOList.add( prepareUserModelFromUserEntityWithDirectory( userEntity ) );
        }
        return userDTOList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserRestricted( EntityManager entityManager, UUID userId ) {
        UserEntity userEntity = userCommonDAO.findById( entityManager, userId );

        if ( null != userEntity ) {
            return userEntity.isRestricted();
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserModelFromUserEntityWithDirectory( UserEntity userEntity ) {
        UserDTO user = prepareUserModelFromUserEntity( userEntity );
        if ( null != userEntity.getDirectory() ) {
            user.setSusUserDirectoryDTO( prepareDirectoryModelFromEntity( userEntity.getDirectory() ) );
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserModelFromUserEntity( UserEntity userEntity ) {
        UserDTO user = new UserDTO();
        user.setId( userEntity.getId().toString() );
        user.setUserUid( userEntity.getUserUid() );
        user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
        user.setRestricted( Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO );
        user.setFirstName( userEntity.getFirstName() );
        user.setSurName( userEntity.getSurName() );
        user.setName( user.getName() );
        user.setChangable( userEntity.isChangeable() );
        List< UserDetailEntity > userDetailList = new ArrayList<>();
        UserDetailEntity detail = null;
        if ( CollectionUtils.isNotEmpty( userEntity.getUserDetails() ) ) {
            detail = userEntity.getUserDetails().stream().findFirst().orElse( null );
        }

        if ( detail != null ) {
            userDetailList.add( detail );
        }
        user.setUserDetails( prepareUserDetailsFromUser( userDetailList ) );
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getUserFailedLdapAttempts( EntityManager entityManager, String userUid ) {
        var attempts = loginAttemptCommonDAO.getFailedLoginAttempts( entityManager, userUid );
        return attempts.getLdapAttempts() != null ? attempts.getLdapAttempts() : ConstantsInteger.INTEGER_VALUE_ZERO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFailedLdapAttemptByOne( EntityManager entityManager, String userUid ) {
        var attempts = loginAttemptCommonDAO.getFailedLoginAttempts( entityManager, userUid );
        if ( attempts.getLdapAttempts() == null ) {
            attempts.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ONE );
        } else {
            attempts.setLdapAttempts( attempts.getLdapAttempts() + ConstantsInteger.INTEGER_VALUE_ONE );
        }
        loginAttemptCommonDAO.updateFailedLoginAttempt( entityManager, attempts );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetFailedLdapAttempts( EntityManager entityManager, String userUid ) {
        var attempts = loginAttemptCommonDAO.getFailedLoginAttempts( entityManager, userUid );
        if ( attempts.getLdapAttempts() == null || attempts.getLdapAttempts() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            attempts.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
            loginAttemptCommonDAO.updateFailedLoginAttempt( entityManager, attempts );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRoleNameById( EntityManager entityManager, UUID roleId ) {
        RoleEntity roleEntity = roleCommonDAO.getLatestNonDeletedObjectById( entityManager, roleId );
        if ( null != roleEntity ) {
            return roleEntity.getName();
        }
        return ConstantsString.EMPTY_STRING;
    }

    /**
     * Prepare user details from user list.
     *
     * @param userDetailList
     *         the user detail list
     *
     * @return the list
     */
    private List< UserDetail > prepareUserDetailsFromUser( List< UserDetailEntity > userDetailList ) {
        List< UserDetail > returnList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( userDetailList ) ) {
            UserDetail userDetailDTO;
            for ( UserDetailEntity userDetailEntity : userDetailList ) {
                userDetailDTO = new UserDetail( userDetailEntity.getId() != null ? userDetailEntity.getId().toString() : null,
                        userDetailEntity.getDesignation(), userDetailEntity.getContacts(), userDetailEntity.getEmail(),
                        userDetailEntity.getDepartment() );

                if ( userDetailEntity.getLanguage() != null ) {
                    userDetailDTO.setLanguage( userDetailEntity.getLanguage() );
                }
                returnList.add( userDetailDTO );
            }
        }
        return returnList;
    }

    /**
     * Prepare directory model from entity.
     *
     * @param directoryEntity
     *         the directory entity
     *
     * @return the suS user directory DTO
     */
    public SuSUserDirectoryDTO prepareDirectoryModelFromEntity( SuSUserDirectoryEntity directoryEntity ) {

        if ( directoryEntity != null ) {
            SuSUserDirectoryDTO susUserDirectoryDTO = new SuSUserDirectoryDTO();
            susUserDirectoryDTO.setId( directoryEntity.getId() );
            susUserDirectoryDTO.setName( directoryEntity.getName() );
            susUserDirectoryDTO.setDescription( directoryEntity.getDescription() );
            susUserDirectoryDTO.setType( directoryEntity.getType() );
            susUserDirectoryDTO.setStatus( directoryEntity.getStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );

            if ( null != directoryEntity.getCreatedOn() ) {
                susUserDirectoryDTO.setCreatedOn( DateFormatStandard.format( directoryEntity.getCreatedOn() ) );
            }
            if ( null != directoryEntity.getModifiedOn() ) {
                susUserDirectoryDTO.setModifiedOn( DateFormatStandard.format( directoryEntity.getModifiedOn() ) );
            }
            if ( directoryEntity.getUserDirectoryAttribute() != null ) {
                susUserDirectoryDTO.setUserDirectoryAttribute(
                        prepareSusDirectoryAttributesModelFromEntity( directoryEntity.getUserDirectoryAttribute() ) );
            } else {
                susUserDirectoryDTO.setUserDirectoryAttribute( new SusUserDirectoryAttributeDTO() );
            }
            return susUserDirectoryDTO;
        } else {
            return null;
        }

    }

    /**
     * Prepare Group Entity From Sus User Group DTO.
     *
     * @param dto
     *         the dto
     *
     * @return GroupEntity
     */
    private GroupEntity prepareGroupEntityFromSusUserGroupDTO( SuSUserGroupDTO dto ) {
        GroupEntity entity = null;
        if ( dto != null ) {
            entity = new GroupEntity();
            if ( !dto.isUpdate() ) {
                entity.setId( UUID.randomUUID() );
            } else {
                entity.setId( dto.getId() );
            }
            entity.setName( dto.getName() );
            entity.setDescription( dto.getDescription() );
            entity.setStatus( dto.getStatus().equals( ConstantsStatus.ACTIVE ) );
            entity.setCreatedOn( new Date() );
            entity.setModifiedOn( new Date() );
        }
        return entity;
    }

    /**
     * Save security identity.
     *
     * @param id
     *         the id
     *
     * @return the acl security identity entity
     */
    private AclSecurityIdentityEntity saveSecurityIdentity( EntityManager entityManager, UUID id ) {
        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
        securityIdentityEntity.setId( UUID.randomUUID() );
        securityIdentityEntity.setSid( id );
        securityIdentityEntity.setPrinciple( false );
        securityIdentityEntity = aclCommonSecurityIdentityDAO.save( entityManager, securityIdentityEntity );
        return securityIdentityEntity;
    }

    /**
     * Prepare sus directory attributes model from entity.
     *
     * @param directoryAttributeEntity
     *         the directory attribute entity
     *
     * @return the sus user directory attribute DTO
     */
    public SusUserDirectoryAttributeDTO prepareSusDirectoryAttributesModelFromEntity(
            UserDirectoryAttributeEntity directoryAttributeEntity ) {
        SusUserDirectoryAttributeDTO susDirectoryAttributes = new SusUserDirectoryAttributeDTO();
        susDirectoryAttributes.setId( directoryAttributeEntity.getId().toString() );
        susDirectoryAttributes.setSearchBase( directoryAttributeEntity.getSearchBase() );
        susDirectoryAttributes.setUrl( directoryAttributeEntity.getUrl() );
        susDirectoryAttributes.setSystemUsername( directoryAttributeEntity.getSystemUsername() );
        susDirectoryAttributes.setSystemPassword( directoryAttributeEntity.getSystemPassword() );
        susDirectoryAttributes.setUserDnTemplate( directoryAttributeEntity.getUserDnTemplate() );
        return susDirectoryAttributes;
    }

    @Override
    public UserEntity getUserEntityById( EntityManager entityManager, UUID userId ) {
        return userCommonDAO.getLatestNonDeletedObjectById( entityManager, userId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserCommonDAO getUserCommonDAO() {
        return userCommonDAO;
    }

    /**
     * Sets the user common DAO.
     *
     * @param userCommonDAO
     *         the new user common DAO
     */
    public void setUserCommonDAO( UserCommonDAO userCommonDAO ) {
        this.userCommonDAO = userCommonDAO;
    }

    /**
     * Gets the group common DAO.
     *
     * @return the group common DAO
     */
    @Override
    public GroupCommonDAO getGroupCommonDAO() {
        return groupCommonDAO;
    }

    /**
     * Sets the group common DAO.
     *
     * @param groupCommonDAO
     *         the new group common DAO
     */
    public void setGroupCommonDAO( GroupCommonDAO groupCommonDAO ) {
        this.groupCommonDAO = groupCommonDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclCommonSecurityIdentityDAO getAclCommonSecurityIdentityDAO() {
        return aclCommonSecurityIdentityDAO;
    }

    /**
     * Sets the acl common security identity DAO.
     *
     * @param aclCommonSecurityIdentityDAO
     *         the new acl common security identity DAO
     */
    public void setAclCommonSecurityIdentityDAO( AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO ) {
        this.aclCommonSecurityIdentityDAO = aclCommonSecurityIdentityDAO;
    }

    /**
     * Gets the acl common object identity DAO.
     *
     * @return the acl common object identity DAO
     */
    public AclCommonObjectIdentityDAO getAclCommonObjectIdentityDAO() {
        return aclCommonObjectIdentityDAO;
    }

    /**
     * Sets the acl common object identity DAO.
     *
     * @param aclCommonObjectIdentityDAO
     *         the new acl common object identity DAO
     */
    public void setAclCommonObjectIdentityDAO( AclCommonObjectIdentityDAO aclCommonObjectIdentityDAO ) {
        this.aclCommonObjectIdentityDAO = aclCommonObjectIdentityDAO;
    }

    /**
     * Gets the entry DAO.
     *
     * @return the entry DAO
     */
    public AclCommonEntryDAO getEntryDAO() {
        return entryDAO;
    }

    /**
     * Sets the entry DAO.
     *
     * @param entryDAO
     *         the new entry DAO
     */
    public void setEntryDAO( AclCommonEntryDAO entryDAO ) {
        this.entryDAO = entryDAO;
    }

    /**
     * Sets the entity manager factory.
     *
     * @param entityManagerFactory
     *         the new entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Sets the login attempt common DAO.
     *
     * @param loginAttemptCommonDAO
     *         the new login attempt common DAO
     */
    public void setLoginAttemptCommonDAO( LoginAttemptCommonDAO loginAttemptCommonDAO ) {
        this.loginAttemptCommonDAO = loginAttemptCommonDAO;
    }

    /**
     * Gets login attempt common dao.
     *
     * @return the login attempt common dao
     */
    @Override
    public LoginAttemptCommonDAO getLoginAttemptCommonDAO() {
        return loginAttemptCommonDAO;
    }

    /**
     * Gets role common dao.
     *
     * @return the role common dao
     */
    public RoleCommonDAO getRoleCommonDAO() {
        return roleCommonDAO;
    }

    /**
     * Sets role common dao.
     *
     * @param roleCommonDAO
     *         the role common dao
     */
    public void setRoleCommonDAO( RoleCommonDAO roleCommonDAO ) {
        this.roleCommonDAO = roleCommonDAO;
    }

}