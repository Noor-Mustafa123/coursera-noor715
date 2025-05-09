package de.soco.software.simuspace.suscore.user.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.auth.connect.ad.authentication.ActiveDirectoryCustomRealm;
import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.constants.OAuthConstants;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDirectoryAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.user.dao.DirectoryDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDirectoryAttributeDAO;
import de.soco.software.simuspace.suscore.user.manager.DirectoryManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * The Class is responsible to provide business logic for directory and invoke to dao class functions to communicate with repository and
 * provide CRUD of directory operations to it.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class DirectoryManagerImpl implements DirectoryManager {

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The directory dao.
     */
    private DirectoryDAO directoryDAO;

    /**
     * The userDirectoryAttribute dao.
     */
    private UserDirectoryAttributeDAO userDirectoryAttributeDAO;

    /**
     * The ldap auth realm.
     */
    private LdapCustomAuthRealm ldapAuthRealm;

    /**
     * The user manager.
     */
    private UserManager userManager;

    /**
     * The ad custom realm.
     */
    private ActiveDirectoryCustomRealm adCustomRealm;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    private UserTokenManager tokenManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant LICENSE_PLUGIN_NAME.
     */
    private static final String USER_PLUGIN_NAME = "plugin_user";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The constant STATUS_SELECT_BOX.
     */
    private static final String STATUS_SELECT_BOX = "status";

    /**
     * The constant TYPE_SELECT_BOX.
     */
    private static final String TYPE_SELECT_BOX = "type";

    /**
     * The Constant DIRECTORY.
     */
    private static final String DIRECTORY = "Directory";

    /**
     * The Constant MACHINE_USER_DIRECTORY.
     */
    private static final String MACHINE_USER_DIRECTORY = "Machine User Directory";

    /**
     * The Constant SELECT_DIRECTORY_LABEL.
     */
    private static final String SELECT_DIRECTORY_LABEL = "Select Directory";

    /**
     * The Constant SELECTION.
     */
    private static final String SELECTION = "type";

    /**
     * The Constant FIELD_TYPE_SELECT.
     */
    private static final String FIELD_TYPE_SELECT = "select";

    /**
     * The Constant BIND_FROM_URL_FOR_DIRECTORY.
     */
    private static final String BIND_FROM_URL_FOR_DIRECTORY = "/system/user-directory/fields/{__value__}";

    /**
     * The Constant REMOVE_ALL_ATTRIBUTE.
     */
    private static final String[] REMOVE_ALL_ATTRIBUTE = { "userDirectoryAttribute.id", "userDirectoryAttribute.searchBase",
            "userDirectoryAttribute.userDnTemplate", "userDirectoryAttribute.systemUsername", "userDirectoryAttribute.url",
            "userDirectoryAttribute.systemPassword", "userDirectoryAttribute.ldapFirstName", "userDirectoryAttribute.ldapSurName",
            "userDirectoryAttribute.name", "userDirectoryAttribute.baseUrl", "userDirectoryAttribute.clientId",
            "userDirectoryAttribute.responseType", "userDirectoryAttribute.client_secret", "userDirectoryAttribute.scope",
            "userDirectoryAttribute.providerType", "userDirectoryAttribute.wellKnownUrl", "userDirectoryAttribute.authorizationEndpoint",
            "userDirectoryAttribute.tokenEndpoint", "userDirectoryAttribute.userInfoEndpoint", "userDirectoryAttribute.revocationEndpoint",
            "userDirectoryAttribute.redirectUri" };

    /**
     * The Constant REMOVE_ATTRIBUTES_FROM_LDAP.
     */
    private static final String[] REMOVE_ATTRIBUTES_FROM_LDAP = { "userDirectoryAttribute.searchBase", "userDirectoryAttribute.name",
            "userDirectoryAttribute.baseUrl", "userDirectoryAttribute.clientId", "userDirectoryAttribute.responseType",
            "userDirectoryAttribute.client_secret", "userDirectoryAttribute.scope", "userDirectoryAttribute.providerType",
            "userDirectoryAttribute.wellKnownUrl", "userDirectoryAttribute.authorizationEndpoint", "userDirectoryAttribute.tokenEndpoint",
            "userDirectoryAttribute.userInfoEndpoint", "userDirectoryAttribute.revocationEndpoint", "userDirectoryAttribute.redirectUri" };

    /**
     * The Constant REMOVE_ATTRIBUTES_FROM_ACTIVE_DIRECTORY.
     */
    private static final String[] REMOVE_ATTRIBUTES_FROM_ACTIVE_DIRECTORY = { "userDirectoryAttribute.userDnTemplate",
            "userDirectoryAttribute.name", "userDirectoryAttribute.baseUrl", "userDirectoryAttribute.clientId",
            "userDirectoryAttribute.responseType", "userDirectoryAttribute.client_secret", "userDirectoryAttribute.scope",
            "userDirectoryAttribute.providerType", "userDirectoryAttribute.wellKnownUrl", "userDirectoryAttribute.authorizationEndpoint",
            "userDirectoryAttribute.tokenEndpoint", "userDirectoryAttribute.userInfoEndpoint", "userDirectoryAttribute.revocationEndpoint",
            "userDirectoryAttribute.redirectUri" };

    /**
     * The constants properties to be removed for initial oauth directory creation
     */
    private static final String[] OAUTH_DIRECTORY_CREATION_FORM_REMOVAL_PARAMS = { "userDirectoryAttribute.id",
            "userDirectoryAttribute.searchBase", "userDirectoryAttribute.userDnTemplate", "userDirectoryAttribute.systemUsername",
            "userDirectoryAttribute.url", "userDirectoryAttribute.systemPassword", "userDirectoryAttribute.ldapFirstName",
            "userDirectoryAttribute.ldapSurName" };

    /**
     * The constants properties to be removed for oauth directory of provider type OIDC
     */
    private static final String[] OIDC_PROVIDER_REMOVAL_PARAMS = { "userDirectoryAttribute.systemUsername", "userDirectoryAttribute.url",
            "userDirectoryAttribute.systemPassword", "userDirectoryAttribute.ldapFirstName", "userDirectoryAttribute.ldapSurName",
            "userDirectoryAttribute.searchBase", "userDirectoryAttribute.userDnTemplate", "userDirectoryAttribute.baseUrl",
            "userDirectoryAttribute.authorizationEndpoint", "userDirectoryAttribute.tokenEndpoint",
            "userDirectoryAttribute.userInfoEndpoint", "userDirectoryAttribute.revocationEndpoint" };

    /**
     * The constants properties to be removed for oauth directory of provider type Non-OIDC
     */
    private static final String[] NON_OIDC_PROVIDER_REMOVAL_PARAMS = { "userDirectoryAttribute.systemUsername",
            "userDirectoryAttribute.url", "userDirectoryAttribute.systemPassword", "userDirectoryAttribute.ldapFirstName",
            "userDirectoryAttribute.ldapSurName", "userDirectoryAttribute.searchBase", "userDirectoryAttribute.userDnTemplate",
            "userDirectoryAttribute.baseUrl", "userDirectoryAttribute.wellKnownUrl" };

    /**
     * The Constant HIDE.
     */
    private static final String HIDE = "hidden";

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.directory.manager.DirectoryManager# createDirectory(int,
     * de.soco.software.simuspace.suscore.directory.model.SuSUserDirectoryModel)
     */
    @Override
    public SuSUserDirectoryDTO createDirectory( String userIdFromGeneralHeader, SuSUserDirectoryDTO directory ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );

            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), DIRECTORY );
            if ( directory != null ) {
                //     Basic Validation
                Notification notify = directory.validate();

                if ( notify != null && notify.hasErrors() ) {
                    throw new SusException( notify.getErrors().toString() );
                }

                if ( !isValidateDirectoryAttributes( directory ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_CONFIGURATION_ARE_NOT_VALID.getKey() ) );
                }

                SuSUserDirectoryEntity susUserDirectoryEntity = prepareDirectoryEntityFromModel( directory );

                if ( directory.getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) || directory.getType()
                        .equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) {
                    UserDirectoryAttributeEntity directoryAttributesEntity = prepareDirectoryAttributesEntityFromModel(
                            directory.getUserDirectoryAttribute(), directory.getType() );
                    userDirectoryAttributeDAO.save( entityManager, directoryAttributesEntity );
                    susUserDirectoryEntity.setUserDirectoryAttribute( directoryAttributesEntity );
                }

                if ( Boolean.TRUE.equals( PropertiesManager.isAuditDirectory() ) ) {
                    susUserDirectoryEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                            "Directory : " + directory.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED,
                            ConstantsDbOperationTypes.CREATED, userEntity.getId().toString(), "", directory.getName(), DIRECTORY ) );
                }

                //     CHECK IF THE DIRECTORY TYPE IS OAUTH THEN SAVE THE ENTITY
                if ( directory.getType().equals( ConstantsUserDirectories.OAUTH_DIRECTORY ) ) {
                    UserDirectoryAttributeEntity directoryAttributesEntity = prepareDirectoryAttributesEntityFromModel(
                            directory.getUserDirectoryAttribute(), directory.getType() );
                    //   SAVE THE NEW DIRECTORY TYPE TO DB
                    userDirectoryAttributeDAO.save( entityManager, directoryAttributesEntity );
                    susUserDirectoryEntity.setUserDirectoryAttribute( directoryAttributesEntity );
                }

                susUserDirectoryEntity.setCreatedBy( userEntity );
                susUserDirectoryEntity.setModifiedBy( userEntity );

                SuSUserDirectoryEntity directoryEntity = directoryDAO.createDirectory( entityManager, susUserDirectoryEntity );
                return prepareDirectoryModelFromEntity( entityManager, directoryEntity, true );
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is validate directory attributes.
     *
     * @param directory
     *         the directory
     *
     * @return true, if is validate directory attributes @ the sus exception
     */
    private boolean isValidateDirectoryAttributes( SuSUserDirectoryDTO directory ) {
        if ( directory.getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) ) {
            return ldapAuthRealm.isLdapConnectionEstablished( directory.getUserDirectoryAttribute() );
        } else if ( directory.getType().equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) {
            return adCustomRealm.isActiveDirectoryConnectionEstablished( directory.getUserDirectoryAttribute() );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryDTO updateDirectory( String userIdFromGeneralHeader, String token, SuSUserDirectoryDTO directory ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
            SuSUserDirectoryEntity userDirectoryEntity = readDirectory( entityManager, directory.getId() );
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.WRITE.getValue(), Messages.NO_RIGHTS_TO_UPDATE.getKey(), DIRECTORY );
            if ( directory != null ) {
                Notification notify = directory.validate();
                if ( notify != null && notify.hasErrors() ) {
                    throw new SusException( notify.getErrors().toString() );
                }
                if ( !isValidateDirectoryAttributes( directory ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_CONFIGURATION_ARE_NOT_VALID.getKey() ) );
                }
                SuSUserDirectoryEntity existing = directoryDAO.readDirectory( entityManager, directory.getId() );
                if ( existing == null ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_NOT_EXIST.getKey() ) );
                }
                directory.setCreatedOn( DateFormatStandard.format( existing.getCreatedOn() ) );
                userDirectoryEntity = prepareDirectoryEntityFromModel( directory, userDirectoryEntity );
                if ( directory.getType().equals( ConstantsUserDirectories.LDAP_DIRECTORY ) || directory.getType()
                        .equals( ConstantsUserDirectories.ACTIVE_DIRECTORY ) || directory.getType()
                        .equals( ConstantsUserDirectories.OAUTH_DIRECTORY ) ) {
                    userDirectoryAttributeDAO.update( entityManager, userDirectoryEntity.getUserDirectoryAttribute() );
                } else {
                    userDirectoryEntity.setUserDirectoryAttribute( null );
                }
                if ( Boolean.TRUE.equals( PropertiesManager.isAuditDirectory() ) ) {
                    AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userEntity.getId().toString(), existing,
                            userDirectoryEntity );
                    if ( null != auditLog ) {
                        auditLog.setObjectName( userDirectoryEntity.getName() );
                        auditLog.setObjectType( DIRECTORY );
                        auditLog.setObjectId( existing.getId().toString() );
                    }
                    userDirectoryEntity.setAuditLogEntity( auditLog );
                }
                userDirectoryEntity.setCreatedBy( existing.getCreatedBy() );
                userDirectoryEntity.setModifiedBy( userEntity );
                userDirectoryEntity.setModifiedOn( new Date() );
                userDirectoryEntity = directoryDAO.updateDirectory( entityManager, userDirectoryEntity );

                // disable users session
                if ( !userDirectoryEntity.getStatus() ) {
                    disableSession( entityManager, token, userDirectoryEntity );
                }

                return prepareDirectoryModelFromEntity( entityManager, userDirectoryEntity, false );
            } else {
                return null;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Disable session.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     * @param userDirectoryEntity
     *         the user directory entity
     */
    private void disableSession( EntityManager entityManager, String token, SuSUserDirectoryEntity userDirectoryEntity ) {
        List< UserEntity > userEntities = userManager.getUsersByDirectoryId( entityManager, userDirectoryEntity.getId() );
        if ( CollectionUtils.isNotEmpty( userEntities ) ) {
            for ( UserEntity userNeedsToBeDisabled : userEntities ) {
                List< UserTokenDTO > userTokenDTOs = tokenManager.getUserActiveTokenList( entityManager,
                        userNeedsToBeDisabled.getId().toString() );
                if ( CollectionUtils.isNotEmpty( userTokenDTOs ) ) {
                    for ( UserTokenDTO userTokenDTO : userTokenDTOs ) {
                        Map< String, String > logoutToken = new HashMap<>();
                        logoutToken.put( "token", userTokenDTO.getToken() );
                        String urlLogout = PropertiesManager.getLocationURL() + "/api/auth/logout";
                        SuSClient.postRequest( urlLogout, JsonUtils.toJson( logoutToken ),
                                CommonUtils.prepareHeadersWithAuthToken( token ) );
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDirectory( EntityManager entityManager, String userId, UUID directoryId ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
        userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.DIRECTORIES.getId(), PermissionMatrixEnum.DELETE.getValue(),
                Messages.NO_RIGHTS_TO_DELETE.getKey(), DIRECTORY );
        if ( directoryId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey(), directoryId ) );
        }
        SuSUserDirectoryEntity directoryEntity = validateAndGetExistingDirectory( entityManager, directoryId );
        directoryEntity.setDelete( true );
        if ( Boolean.TRUE.equals( PropertiesManager.isAuditDirectory() ) ) {
            directoryEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForObjects(
                    "Directory : " + directoryEntity.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.DELETED,
                    ConstantsDbOperationTypes.DELETED, userId, "", directoryEntity.getName(), DIRECTORY ) );
        }
        // Delete related SusUserDirectoryAttributeDTO
        UserDirectoryAttributeEntity attributeDTO = directoryEntity.getUserDirectoryAttribute();
        if ( attributeDTO != null ) {
            entityManager.remove( entityManager.contains( attributeDTO ) ? attributeDTO : entityManager.merge( attributeDTO ) );
        }

        directoryDAO.updateDirectory( entityManager, directoryEntity );
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDirectoryBySelection( String userIdFromGeneralHeader, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.DELETE.getValue(), Messages.NO_RIGHTS_TO_DELETE.getKey(), DIRECTORY );
            if ( mode.contentEquals( ConstantsMode.BULK ) ) {
                FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
                List< Object > ids = filtersDTO.getItems();
                for ( Object id : ids ) {
                    deleteDirectory( entityManager, userIdFromGeneralHeader, UUID.fromString( id.toString() ) );
                }
            } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
                deleteDirectory( entityManager, userIdFromGeneralHeader, UUID.fromString( selectionId ) );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.directory.manager.DirectoryManager# readDirectory(int, int)
     */
    @Override
    public SuSUserDirectoryDTO readDirectory( String userIdFromGeneralHeader, UUID directoryId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return readDirectory( entityManager, directoryId, false );
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.directory.manager.DirectoryManager# readDirectory(int, int)
     */
    @Override
    public SuSUserDirectoryDTO readDirectory( EntityManager entityManager, UUID directoryId, boolean withCredentials ) {
        if ( directoryId == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey(), directoryId ) );
        }

        SuSUserDirectoryEntity userDirectoryEntity = directoryDAO.readDirectory( entityManager, directoryId );
        return prepareDirectoryModelFromEntity( entityManager, userDirectoryEntity, withCredentials );
    }

    @Override
    public SuSUserDirectoryEntity readDirectory( EntityManager entityManager, UUID directoryId ) {
        return directoryDAO.readDirectory( entityManager, directoryId );
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.manager.DirectoryManager# getDirectoryList(int)
     */
    @Override
    public List< SuSUserDirectoryDTO > getDirectoryList( String userIdFromGeneralHeader, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), DIRECTORY );

            return prepareDirectoryModel( entityManager,
                    directoryDAO.getAllFilteredRecords( entityManager, SuSUserDirectoryEntity.class, filtersDTO ) );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< SuSUserDirectoryDTO > getAllDirectories( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.READ.getValue(), Messages.NO_RIGHTS_TO_READ.getKey(), DIRECTORY );

            return prepareDirectoryModel( entityManager, directoryDAO.getNonDeletedObjectList( entityManager ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSUserDirectoryDTO > getDirectoriesByType( String userId, String dirType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userId, SimuspaceFeaturesEnum.DIRECTORIES.getId(), PermissionMatrixEnum.READ.getValue(),
                    Messages.NO_RIGHTS_TO_READ.getKey(), DIRECTORY );
            return prepareDirectoryModel( entityManager,
                    directoryDAO.getLatestNonDeletedObjectListByProperty( entityManager, SuSUserDirectoryEntity.class, ConstantsDAO.TYPE,
                            switch ( dirType ) {
                                case "internal" -> ConstantsUserDirectories.INTERNAL_DIRECTORY;
                                case "ldap" -> ConstantsUserDirectories.LDAP_DIRECTORY;
                                case "active" -> ConstantsUserDirectories.ACTIVE_DIRECTORY;
                                case "oauth" -> ConstantsUserDirectories.OAUTH_DIRECTORY;
                                default -> ConstantsString.EMPTY_STRING;
                            } ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createUserDirectoryFormForEdit( String userIdFromGeneralHeader, UUID id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.WRITE.getValue(), Messages.NO_RIGHTS_TO_UPDATE.getKey(), DIRECTORY );

            SuSUserDirectoryEntity entityToEdit = directoryDAO.getLatestNonDeletedObjectById( entityManager, id );
            if ( entityToEdit == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_NOT_EXIST.getKey() ) );
            }
            return GUIUtils.createFormFromItems( prepareForm( prepareDirectoryModelFromEntity( entityManager, entityToEdit, false ) ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare form.
     *
     * @param objectInstance
     *         the object instance
     *
     * @return the list
     */
    private List< UIFormItem > prepareForm( SuSUserDirectoryDTO objectInstance ) {

        List< UIFormItem > list = GUIUtils.prepareForm( false, objectInstance, objectInstance.getUserDirectoryAttribute() );
        if ( CollectionUtil.isNotEmpty( list ) ) {

            for ( UIFormItem uiFormItem : list ) {
                if ( uiFormItem.getName().equals( STATUS_SELECT_BOX ) ) {
                    GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                            objectInstance.getStatus(), false );
                }

                if ( uiFormItem.getName().equals( TYPE_SELECT_BOX ) ) {
                    uiFormItem.setType( HIDE );
                }
            }
        }
        return renderDirectoryOnOptionBasis( objectInstance.getType(), list );
    }

    /**
     * A helper function to find existing directory by id or throw exception if doesn't exist.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the su S user directory entity
     */
    private SuSUserDirectoryEntity validateAndGetExistingDirectory( EntityManager entityManager, UUID id ) {
        SuSUserDirectoryEntity existing = directoryDAO.readDirectory( entityManager, id );

        if ( existing == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.REQUEST_ID_NOT_VALID.getKey(), id ) );
        }
        if ( CollectionUtils.isNotEmpty( existing.getUsers() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DIRECTORY_CANNOT_BE_DELETED.getKey() ) );
        }
        return existing;
    }

    /**
     * Prepare directory model.
     *
     * @param entityManager
     *         the entity manager
     * @param directoryEntities
     *         the directory entity
     *
     * @return the sus user directory
     */
    private List< SuSUserDirectoryDTO > prepareDirectoryModel( EntityManager entityManager,
            List< SuSUserDirectoryEntity > directoryEntities ) {
        List< SuSUserDirectoryDTO > userDirectoryList = new ArrayList<>();
        if ( directoryEntities != null ) {
            for ( SuSUserDirectoryEntity suSUserDirectoryEntity : directoryEntities ) {
                userDirectoryList.add( prepareDirectoryModelFromEntity( entityManager, suSUserDirectoryEntity, false ) );
            }
        }
        return userDirectoryList;
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.manager.DirectoryManager#createForm( java.lang.String)
     */
    @Override
    public UIForm createForm( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            userManager.isPermitted( entityManager, userIdFromGeneralHeader, SimuspaceFeaturesEnum.DIRECTORIES.getId(),
                    PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue(), Messages.NO_RIGHTS_TO_CREATE.getKey(), DIRECTORY );
        } finally {
            entityManager.close();
        }

        List< UIFormItem > listUserDirectory = GUIUtils.prepareForm( true, new SuSUserDirectoryDTO() );
        for ( UIFormItem uiFormItem : listUserDirectory ) {
            if ( uiFormItem.getName().equals( STATUS_SELECT_BOX ) ) {
                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                        ConstantsStatus.ACTIVE, false );
            }
            if ( uiFormItem.getName().equals( TYPE_SELECT_BOX ) ) {
                prepareDirectoryType( uiFormItem );

                Map< String, Object > rules = new HashMap<>();
                Map< String, Object > message = new HashMap<>();

                rules.put( REQUIRED, true );
                message.put( REQUIRED, MUST_CHOSE_OPTION );
                uiFormItem.setRules( rules );
                uiFormItem.setMessages( message );
            }
        }
        return GUIUtils.createFormFromItems( listUserDirectory );
    }

    /**
     * Prepare directory type.
     *
     * @param uiFormItem
     *         the ui form item
     */
    private void prepareDirectoryType( UIFormItem uiFormItem ) {
        SelectFormItem selectFormItem = ( SelectFormItem ) uiFormItem;
        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( ConstantsUserDirectories.LDAP_DIRECTORY, ConstantsUserDirectories.LDAP_DIRECTORY ) );
        options.add( new SelectOptionsUI( ConstantsUserDirectories.INTERNAL_DIRECTORY, ConstantsUserDirectories.INTERNAL_DIRECTORY ) );
        options.add( new SelectOptionsUI( ConstantsUserDirectories.ACTIVE_DIRECTORY, ConstantsUserDirectories.ACTIVE_DIRECTORY ) );
        options.add( new SelectOptionsUI( ConstantsUserDirectories.OAUTH_DIRECTORY, ConstantsUserDirectories.OAUTH_DIRECTORY ) );
        selectFormItem.setLabel( SELECT_DIRECTORY_LABEL );
        selectFormItem.setName( SELECTION );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom( BIND_FROM_URL_FOR_DIRECTORY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getUpdateDirectoryUI( String option ) {
        List< UIFormItem > formItems = null;
        if ( option != null && !option.isEmpty() ) {
            formItems = renderDirectoryOnOptionBasis( option, GUIUtils.prepareForm( true, new SusUserDirectoryAttributeDTO() ) );
        }
        return GUIUtils.createFormFromItems( formItems );
    }

    /**
     * Refactor specific directory.
     *
     * @param option
     *         the option
     * @param formItems
     *         the form items
     */
    private List< UIFormItem > renderDirectoryOnOptionBasis( String option, List< UIFormItem > formItems ) {
        return switch ( option ) {
            case ConstantsUserDirectories.LDAP_DIRECTORY -> removeItemsFromDirectoryList( formItems, REMOVE_ATTRIBUTES_FROM_LDAP );
            case ConstantsUserDirectories.INTERNAL_DIRECTORY, MACHINE_USER_DIRECTORY ->
                    removeItemsFromDirectoryList( formItems, REMOVE_ALL_ATTRIBUTE );
            case ConstantsUserDirectories.ACTIVE_DIRECTORY ->
                    removeItemsFromDirectoryList( formItems, REMOVE_ATTRIBUTES_FROM_ACTIVE_DIRECTORY );
            case ConstantsUserDirectories.OAUTH_DIRECTORY -> removeItemsFromListForOAuthDirectoryForm( formItems );
            default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OPTION_FOR_DIRECTORY.getKey() ) );
        };
    }

    private List< UIFormItem > removeItemsFromListForOAuthDirectoryForm( List< UIFormItem > formItems ) {
        try {
            // Collecting matching items to remove for the list
            List< UIFormItem > itemsToRemove = new ArrayList<>();
            for ( UIFormItem uIFormItem : formItems ) {
                if ( uIFormItem.getName().equals( "userDirectoryAttribute.providerType" ) ) {
                    SelectFormItem castedFormItem = ( SelectFormItem ) uIFormItem;
                    List< SelectOptionsUI > selectOptionsUIList = List.of( new SelectOptionsUI( OAuthConstants.OIDC, OAuthConstants.OIDC ),
                            new SelectOptionsUI( OAuthConstants.NON_OIDC, OAuthConstants.NON_OIDC ) );
                    castedFormItem.setOptions( selectOptionsUIList );
                    if ( uIFormItem.getValue() != null ) {
                        if ( uIFormItem.getValue().equals( OAuthConstants.OIDC ) ) {
                            for ( String param : OIDC_PROVIDER_REMOVAL_PARAMS ) {
                                itemsToRemove.addAll( formItems.stream().filter( item -> item.getName().equals( param ) ).toList() );
                            }
                        } else if ( uIFormItem.getValue().equals( OAuthConstants.NON_OIDC ) ) {
                            for ( String param : NON_OIDC_PROVIDER_REMOVAL_PARAMS ) {
                                itemsToRemove.addAll( formItems.stream().filter( item -> item.getName().equals( param ) ).toList() );
                            }
                        }
                    } else {
                        for ( String param : OAUTH_DIRECTORY_CREATION_FORM_REMOVAL_PARAMS ) {
                            itemsToRemove.addAll( formItems.stream().filter( item -> item.getName().equals( param ) ).toList() );
                        }
                    }

                }
            }
            // removing all matching items
            formItems.removeAll( itemsToRemove );
        } catch ( Exception e ) {
            log.error( "error in removing items for UiFormItems list", e );
        }
        return formItems;
    }

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     *
     * @return the context router
     */
    @Override
    public List< ContextMenuItem > getContextMenu( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // case of select all from data table
            if ( filter.getItems().isEmpty() && filter.getLength().toString().equalsIgnoreCase( "-1" ) ) {

                Long maxResults = directoryDAO.getAllFilteredRecordCountWithParentId( entityManager, SuSUserDirectoryEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSUserDirectoryEntity > allObjectsList = directoryDAO.getAllFilteredRecordsWithParent( entityManager,
                        SuSUserDirectoryEntity.class, filter, UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.forEach( entity -> itemsList.add( entity.getId() ) );

                filter.setItems( itemsList );
            }

            return contextMenuManager.getContextMenu( entityManager, USER_PLUGIN_NAME, SuSUserDirectoryDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.manager.DirectoryManager#
     * testLdapADConnection(de.soco.software.simuspace.suscore.common.model. SuSUserDirectoryDTO)
     */
    @Override
    public boolean testLdapADConnection( SuSUserDirectoryDTO susUserDirectoryDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.DIRECTORIES.getKey() );
            if ( susUserDirectoryDTO.getType() != null && (
                    !susUserDirectoryDTO.getType().equalsIgnoreCase( ConstantsUserDirectories.LDAP_DIRECTORY )
                            && !susUserDirectoryDTO.getType().equalsIgnoreCase( ConstantsUserDirectories.ACTIVE_DIRECTORY ) ) ) {
                return false;
            } else {
                return isValidateDirectoryAttributes( susUserDirectoryDTO );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * prepare entity from SusUserDirectory.
     *
     * @param susUserDirectoryDTO
     *         the sus user directory DTO
     *
     * @return the su S user directory entity
     */
    @Override
    public SuSUserDirectoryEntity prepareDirectoryEntityFromModel( SuSUserDirectoryDTO susUserDirectoryDTO ) {
        SuSUserDirectoryEntity directoryEntity = new SuSUserDirectoryEntity();
        return prepareDirectoryEntityFromModel( susUserDirectoryDTO, directoryEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryEntity prepareDirectoryEntityFromModel( SuSUserDirectoryDTO susUserDirectoryDTO,
            SuSUserDirectoryEntity directoryEntity ) {
        directoryEntity.setId( susUserDirectoryDTO.getId() == null ? UUID.randomUUID() : susUserDirectoryDTO.getId() );
        directoryEntity.setStatus( susUserDirectoryDTO.getStatus().equals( ConstantsStatus.ACTIVE ) );
        directoryEntity.setType( susUserDirectoryDTO.getType() );
        directoryEntity.setName( susUserDirectoryDTO.getName() );

        if ( susUserDirectoryDTO.getUserDirectoryAttribute() != null ) {
            directoryEntity.setUserDirectoryAttribute(
                    prepareDirectoryAttributesEntityFromModel( susUserDirectoryDTO.getUserDirectoryAttribute(),
                            directoryEntity.getUserDirectoryAttribute(), susUserDirectoryDTO.getType() ) );
        }
        directoryEntity.setDescription( susUserDirectoryDTO.getDescription() );
        if ( susUserDirectoryDTO.getId() == null ) {
            directoryEntity.setCreatedOn( new Date() );
        } else {
            directoryEntity.setCreatedOn( DateFormatStandard.toDate( susUserDirectoryDTO.getCreatedOn() ) );
            directoryEntity.setModifiedOn( new Date() );
        }

        return directoryEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryDTO prepareDirectoryModelFromEntity( EntityManager entityManager, SuSUserDirectoryEntity directoryEntity,
            boolean withCredentials ) {
        SuSUserDirectoryDTO susUserDirectoryDTO = prepareDirectoryModelFromEntityForUserModelDTO( directoryEntity );
        if ( directoryEntity != null ) {
            if ( null != directoryEntity.getCreatedOn() ) {
                susUserDirectoryDTO.setCreatedOn( DateFormatStandard.format( directoryEntity.getCreatedOn() ) );
            }
            if ( null != directoryEntity.getModifiedOn() ) {
                susUserDirectoryDTO.setModifiedOn( DateFormatStandard.format( directoryEntity.getModifiedOn() ) );
            }
            if ( null != directoryEntity.getCreatedBy() ) {

                susUserDirectoryDTO.setCreatedBy(
                        userManager.prepareUserModelFromUserEntityWitoutDirectory( entityManager, directoryEntity.getCreatedBy() ) );
            }
            if ( null != directoryEntity.getModifiedBy() ) {
                susUserDirectoryDTO.setModifiedBy(
                        userManager.prepareUserModelFromUserEntityWitoutDirectory( entityManager, directoryEntity.getModifiedBy() ) );
            }
            if ( directoryEntity.getUserDirectoryAttribute() != null ) {
                susUserDirectoryDTO.setUserDirectoryAttribute(
                        prepareSusDirectoryAttributesModelFromEntity( directoryEntity.getUserDirectoryAttribute(), withCredentials ) );
            } else {
                susUserDirectoryDTO.setUserDirectoryAttribute( new SusUserDirectoryAttributeDTO() );
            }
            return susUserDirectoryDTO;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryDTO prepareDirectoryModelFromEntityWithoutUsers( SuSUserDirectoryEntity directoryEntity,
            boolean withCredentials ) {
        SuSUserDirectoryDTO susUserDirectoryDTO = prepareDirectoryModelFromEntityForUserModelDTO( directoryEntity );
        if ( directoryEntity != null ) {
            if ( null != directoryEntity.getCreatedOn() ) {
                susUserDirectoryDTO.setCreatedOn( DateFormatStandard.format( directoryEntity.getCreatedOn() ) );
            }
            if ( null != directoryEntity.getModifiedOn() ) {
                susUserDirectoryDTO.setModifiedOn( DateFormatStandard.format( directoryEntity.getModifiedOn() ) );
            }

            if ( directoryEntity.getUserDirectoryAttribute() != null ) {
                susUserDirectoryDTO.setUserDirectoryAttribute(
                        prepareSusDirectoryAttributesModelFromEntity( directoryEntity.getUserDirectoryAttribute(), withCredentials ) );
            } else {
                susUserDirectoryDTO.setUserDirectoryAttribute( new SusUserDirectoryAttributeDTO() );
            }
            return susUserDirectoryDTO;
        } else {
            return null;
        }
    }

    @Override
    public SuSUserDirectoryDTO prepareDirectoryModelFromEntityForUserModelDTO( SuSUserDirectoryEntity directoryEntity ) {
        if ( directoryEntity != null ) {
            SuSUserDirectoryDTO susUserDirectoryDTO = new SuSUserDirectoryDTO();
            susUserDirectoryDTO.setId( directoryEntity.getId() );
            susUserDirectoryDTO.setName( directoryEntity.getName() );
            susUserDirectoryDTO.setDescription( directoryEntity.getDescription() );
            susUserDirectoryDTO.setType( directoryEntity.getType() );
            susUserDirectoryDTO.setName( directoryEntity.getName() );
            susUserDirectoryDTO.setStatus( directoryEntity.getStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );
            return susUserDirectoryDTO;
        } else {
            return null;
        }
    }

    @Override
    public List< Object > getAllValuesForDirectoryTableColumn( String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( SuSUserDirectoryDTO.class, SusUserDirectoryAttributeDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            allValues = directoryDAO.getAllPropertyValues( entityManager, columnName );
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryDTO prepareDirectoryModel( SuSUserDirectoryEntity directoryEntity ) {
        if ( directoryEntity != null ) {
            SuSUserDirectoryDTO susUserDirectoryDTO = new SuSUserDirectoryDTO();
            susUserDirectoryDTO.setId( directoryEntity.getId() );
            susUserDirectoryDTO.setName( directoryEntity.getName() );
            susUserDirectoryDTO.setDescription( directoryEntity.getDescription() );
            susUserDirectoryDTO.setType( directoryEntity.getType() );
            susUserDirectoryDTO.setName( directoryEntity.getName() );
            susUserDirectoryDTO.setStatus(
                    Boolean.TRUE.equals( directoryEntity.getStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.DISABLE );

            if ( null != directoryEntity.getCreatedOn() ) {
                susUserDirectoryDTO.setCreatedOn( DateFormatStandard.format( directoryEntity.getCreatedOn() ) );
            }
            if ( null != directoryEntity.getModifiedOn() ) {
                susUserDirectoryDTO.setModifiedOn( DateFormatStandard.format( directoryEntity.getModifiedOn() ) );
            }
            if ( directoryEntity.getUserDirectoryAttribute() != null ) {
                susUserDirectoryDTO.setUserDirectoryAttribute(
                        prepareSusDirectoryAttributesModelFromEntity( directoryEntity.getUserDirectoryAttribute(), true ) );
            } else {
                susUserDirectoryDTO.setUserDirectoryAttribute( new SusUserDirectoryAttributeDTO() );
            }
            return susUserDirectoryDTO;
        } else {
            return null;
        }

    }

    /**
     * prepare directory attributes entity from directory attributes dto.
     *
     * @param directoryAttributesDTO
     *         the directory attributes DTO
     *
     * @return the user directory attribute entity
     */
    public UserDirectoryAttributeEntity prepareDirectoryAttributesEntityFromModel( SusUserDirectoryAttributeDTO directoryAttributesDTO,
            String directoryType ) {
        return prepareDirectoryAttributesEntityFromModel( directoryAttributesDTO, null, directoryType );
    }

    /**
     * prepare directory attributes entity from directory attributes dto.
     *
     * @param directoryAttributesDTO
     *         the directory attributes DTO
     * @param directoryAttributesEntity
     *         the directory attributes entity
     *
     * @return the user directory attribute entity
     */
    public UserDirectoryAttributeEntity prepareDirectoryAttributesEntityFromModel( SusUserDirectoryAttributeDTO directoryAttributesDTO,
            UserDirectoryAttributeEntity directoryAttributesEntity, String directoryType ) {
        if ( directoryAttributesEntity == null ) {
            directoryAttributesEntity = new UserDirectoryAttributeEntity();
            directoryAttributesEntity.setId( StringUtils.isBlank( directoryAttributesDTO.getId() ) ? UUID.randomUUID()
                    : UUID.fromString( directoryAttributesDTO.getId() ) );
        }
//        FOR OAUTH_DIRECTORY TYPE
        if ( directoryType.equals( ConstantsUserDirectories.OAUTH_DIRECTORY ) ) {
            prepareOAuthProviderTypeUserDirectoryAttributeEntity( directoryAttributesEntity, directoryAttributesDTO );
        }
//        FOR ANY OTHER DIRECTORY TYPE
        else {
            prepareOtherDirectoryTypeUserDirectoryAttributeEntity( directoryAttributesEntity, directoryAttributesDTO );
        }
        return directoryAttributesEntity;
    }

    /**
     * Set properties of UserDirectoryAttributeEntity for OAuthProvider type directory
     *
     * @param directoryAttributesEntity
     *         the directory attributes entity
     * @param directoryAttributesDTO
     *         the directory attributes dto
     *
     * @return directly modifying the object passed to the method.
     */
    private void prepareOAuthProviderTypeUserDirectoryAttributeEntity( UserDirectoryAttributeEntity directoryAttributesEntity,
            SusUserDirectoryAttributeDTO directoryAttributesDTO ) {
        directoryAttributesEntity.setClientId( directoryAttributesDTO.getClient_id() );
        String enc_secret = EncryptAndDecryptUtils.encryptString( directoryAttributesDTO.getClient_secret() );
        directoryAttributesEntity.setClientSecret( enc_secret );
        directoryAttributesEntity.setName( directoryAttributesDTO.getName() );
        directoryAttributesEntity.setBaseUrl( directoryAttributesDTO.getBase_url() );
        directoryAttributesEntity.setResponseType( directoryAttributesDTO.getResponse_type() );
        directoryAttributesEntity.setScope( directoryAttributesDTO.getScope() );
        directoryAttributesEntity.setProviderType( directoryAttributesDTO.getProvider_type() );
        directoryAttributesEntity.setRedirectUri( directoryAttributesDTO.getRedirectUri() );
        directoryAttributesEntity.setWellKnownUrl( directoryAttributesDTO.getWell_known_url() );
        directoryAttributesEntity.setAuthorizationEndpoint( directoryAttributesDTO.getAuthorization_endpoint() );
        directoryAttributesEntity.setTokenEndpoint( directoryAttributesDTO.getToken_endpoint() );
        directoryAttributesEntity.setUserInfoEndpoint( directoryAttributesDTO.getUser_info_endpoint() );
        directoryAttributesEntity.setRevocationEndpoint( directoryAttributesDTO.getRevocation_endpoint() );
    }

    /**
     * Set properties of UserDirectoryAttributeEntity for other directory types.
     *
     * @param directoryAttributesEntity
     *         the directory attributes entity
     * @param directoryAttributesDTO
     *         the directory attributes dto
     *
     * @return directly modifying the object passed to the method.
     */
    private void prepareOtherDirectoryTypeUserDirectoryAttributeEntity( UserDirectoryAttributeEntity directoryAttributesEntity,
            SusUserDirectoryAttributeDTO directoryAttributesDTO ) {
        directoryAttributesEntity.setSearchBase( directoryAttributesDTO.getSearchBase() );
        directoryAttributesEntity.setUrl( directoryAttributesDTO.getUrl() );
        directoryAttributesEntity.setSystemUsername( directoryAttributesDTO.getSystemUsername() );
        directoryAttributesEntity.setUserDnTemplate( directoryAttributesDTO.getUserDnTemplate() );
        directoryAttributesEntity.setSystemPassword( directoryAttributesDTO.getSystemPassword() );
        directoryAttributesEntity.setLdapSurname( directoryAttributesDTO.getLdapSurName() );
        directoryAttributesEntity.setLdapUserName( directoryAttributesDTO.getLdapFirstName() );
    }

    /**
     * Prepare directory attributes dto from directory attributes entity.
     *
     * @param directoryAttributeEntity
     *         the directory attribute entity
     *
     * @return the sus user directory attribute DTO
     */
    public SusUserDirectoryAttributeDTO prepareSusDirectoryAttributesModelFromEntity( UserDirectoryAttributeEntity directoryAttributeEntity,
            boolean withCredentials ) {
        SusUserDirectoryAttributeDTO susDirectoryAttributes = new SusUserDirectoryAttributeDTO();
        susDirectoryAttributes.setId( directoryAttributeEntity.getId().toString() );
        susDirectoryAttributes.setSearchBase( directoryAttributeEntity.getSearchBase() );
        susDirectoryAttributes.setUrl( directoryAttributeEntity.getUrl() );
        susDirectoryAttributes.setSystemUsername( directoryAttributeEntity.getSystemUsername() );
        susDirectoryAttributes.setUserDnTemplate( directoryAttributeEntity.getUserDnTemplate() );
        susDirectoryAttributes.setLdapSurName( directoryAttributeEntity.getLdapSurname() );
        susDirectoryAttributes.setLdapFirstName( directoryAttributeEntity.getLdapUserName() );

        //  if the userDirectoryEntity type is oauth not ldap or active directory some of the above attributes would be null
        setPropertiesforOAuth( susDirectoryAttributes, directoryAttributeEntity );

        //   the client_secret is not sent back
        if ( withCredentials ) {
            susDirectoryAttributes.setSystemPassword( directoryAttributeEntity.getSystemPassword() );
        }
        return susDirectoryAttributes;
    }

    /**
     * setting additional properties for OAuthProvider type SusUserDirectoryAttributeDTO
     *
     * @param susDirectoryAttributes
     *         the sus directory attributes
     * @param directoryAttributeEntity
     *         the directory attribute entity
     *
     * @return directly modifying the susDirectoryAttributes object passed to the method.
     */
    private void setPropertiesforOAuth( SusUserDirectoryAttributeDTO susDirectoryAttributes,
            UserDirectoryAttributeEntity directoryAttributeEntity ) {
        susDirectoryAttributes.setClient_id( directoryAttributeEntity.getClientId() );
        susDirectoryAttributes.setBase_url( directoryAttributeEntity.getBaseUrl() );
        susDirectoryAttributes.setName( directoryAttributeEntity.getName() );
        susDirectoryAttributes.setResponse_type( directoryAttributeEntity.getResponseType() );
        susDirectoryAttributes.setRedirectUri( directoryAttributeEntity.getRedirectUri() );
        susDirectoryAttributes.setProvider_type( directoryAttributeEntity.getProviderType() );
        susDirectoryAttributes.setScope( directoryAttributeEntity.getScope() );
        susDirectoryAttributes.setWell_known_url( directoryAttributeEntity.getWellKnownUrl() );
        susDirectoryAttributes.setAuthorization_endpoint( directoryAttributeEntity.getAuthorizationEndpoint() );
        susDirectoryAttributes.setToken_endpoint( directoryAttributeEntity.getTokenEndpoint() );
        susDirectoryAttributes.setUser_info_endpoint( directoryAttributeEntity.getUserInfoEndpoint() );
        susDirectoryAttributes.setRevocation_endpoint( directoryAttributeEntity.getRevocationEndpoint() );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getDirectoryUI() {
        return GUIUtils.listColumns( SuSUserDirectoryDTO.class, SusUserDirectoryAttributeDTO.class );
    }

    /**
     * Removes the items from directory list.
     *
     * @param formItems
     *         the form items
     * @param params
     *         the params
     *
     * @return the list
     */
    private List< UIFormItem > removeItemsFromDirectoryList( List< UIFormItem > formItems, String... params ) {
        for ( Iterator< UIFormItem > iter = formItems.listIterator(); iter.hasNext(); ) {
            UIFormItem uIFormItem = iter.next();
            for ( String string : params ) {
                if ( uIFormItem.getName().equals( string ) ) {
                    iter.remove();
                }
            }
        }
        return formItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSUserDirectoryDTO > getListOfAllOAuthUserDirectories() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Extract all directories
            List< SuSUserDirectoryEntity > directoryEntityList = directoryDAO.getAllRecords( entityManager );
            List< SuSUserDirectoryDTO > userDirectoryDTOList = directoryEntityList.stream()
                    .filter( directoryItem -> directoryItem.getType().equals( ConstantsUserDirectories.OAUTH_DIRECTORY ) )
                    .map( directoryItem -> {
                        // setting null for security reasons of endpoint
                        directoryItem.setUserDirectoryAttribute( null );
                        directoryItem.setCreatedBy( null );
                        directoryItem.setModifiedBy( null );
                        return directoryItem;
                    } ).map( userDirectoryEntity -> prepareDirectoryModelFromEntity( entityManager, userDirectoryEntity, false ) ).toList();
            if ( userDirectoryDTOList.isEmpty() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OAUTH_DIRECTORY_RECORDS_FOUND.getKey() ) );
            }
            return userDirectoryDTOList;

        } finally {
            entityManager.close();
        }
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
     * Gets the directory dao.
     *
     * @return the directoryDAO
     */
    public DirectoryDAO getDirectoryDAO() {
        return directoryDAO;
    }

    /**
     * Sets the directory dao.
     *
     * @param directoryDAO
     *         the directoryDAO to set
     */
    public void setDirectoryDAO( DirectoryDAO directoryDAO ) {
        this.directoryDAO = directoryDAO;
    }

    /**
     * Gets the ldap auth realm.
     *
     * @return the ldapAuthRealm
     */
    public LdapCustomAuthRealm getLdapAuthRealm() {
        return ldapAuthRealm;
    }

    /**
     * Sets the ldap auth realm.
     *
     * @param ldapAuthRealm
     *         the ldapAuthRealm to set
     */
    public void setLdapAuthRealm( LdapCustomAuthRealm ldapAuthRealm ) {
        this.ldapAuthRealm = ldapAuthRealm;
    }

    /**
     * Gets the active directory custom realm.
     *
     * @return the adCustomRealm
     */
    public ActiveDirectoryCustomRealm getAdCustomRealm() {
        return adCustomRealm;
    }

    /**
     * Sets the active directory custom realm.
     *
     * @param adCustomRealm
     *         the adCustomRealm to set
     */
    public void setAdCustomRealm( ActiveDirectoryCustomRealm adCustomRealm ) {
        this.adCustomRealm = adCustomRealm;
    }

    /**
     * Gets the user directory attribute DAO.
     *
     * @return the user directory attribute DAO
     */
    public UserDirectoryAttributeDAO getUserDirectoryAttributeDAO() {
        return userDirectoryAttributeDAO;
    }

    /**
     * Sets the user directory attribute DAO.
     *
     * @param userDirectoryAttributeDAO
     *         the new user directory attribute DAO
     */
    public void setUserDirectoryAttributeDAO( UserDirectoryAttributeDAO userDirectoryAttributeDAO ) {
        this.userDirectoryAttributeDAO = userDirectoryAttributeDAO;
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
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
     * Gets the user manager.
     *
     * @return the user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * Gets token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets token manager.
     *
     * @param tokenManager
     *         the token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
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
