package de.soco.software.simuspace.suscore.rights.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.records.DocumentRecord;
import de.soco.software.simuspace.suscore.common.records.GroupRecord;
import de.soco.software.simuspace.suscore.common.records.RoleRecord;
import de.soco.software.simuspace.suscore.common.records.UserEntityRecord;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.rights.manager.RightsManager;
import de.soco.software.simuspace.suscore.rights.model.RightsListDTO;
import de.soco.software.simuspace.suscore.role.dao.RoleDAO;
import de.soco.software.simuspace.suscore.user.dao.UserGroupDAO;

/**
 * The type Rights manager.
 */
public class RightsManagerImpl implements RightsManager {

    /**
     * The constant ROLE.
     */
    private static final String ROLE = "Role";

    /**
     * The constant GROUP.
     */
    private static final String GROUP = "Group";

    /**
     * The Permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The License manager.
     */
    private LicenseManager licenseManager;

    /**
     * The User group dao.
     */
    private UserGroupDAO userGroupDAO;

    /**
     * The Role dao.
     */
    private RoleDAO roleDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Document manager.
     */
    private DocumentManager documentManager;

    @Override
    public RightsListDTO getAllRightsList( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getAllRightsList( entityManager, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets all rights list.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the all rights list
     */
    private RightsListDTO getAllRightsList( EntityManager entityManager, String userIdFromGeneralHeader ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.GROUPS.getKey() );
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), GROUP );
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), ROLE );

        List< GroupEntity > groupEntities = userGroupDAO.getAllObjectList( entityManager );
        List< RoleEntity > roleEntities = roleDAO.getAllObjectList( entityManager );
        List< GroupRecord > groupRecords = new ArrayList<>();
        List< RoleRecord > roleRecords = new ArrayList<>();
        if ( CollectionUtils.isNotEmpty( groupEntities ) ) {
            for ( GroupEntity groupEntity : groupEntities ) {
                List< UserEntityRecord > userEntityRecords = new ArrayList<>();
                for ( UserEntity userEntity : groupEntity.getUsers() ) {
                    DocumentRecord profilePhoto = getDocumentRecord( userEntity );
                    userEntityRecords.add( prepareUserEntityRecordFromGroupEntity( userEntity, profilePhoto ) );
                }
                groupRecords.add( prepareGroupsRecordFromGroupEntity( groupEntity, userEntityRecords ) );
            }
        }
        if ( CollectionUtils.isNotEmpty( roleEntities ) ) {
            for ( RoleEntity roleEntity : roleEntities ) {
                roleRecords.add( prepareRolesRecordFromRoleEntity( roleEntity ) );
            }
        }
        Map< String, Object > groupsData = new HashMap<>();
        groupsData.put( MessageBundleFactory.getMessage( Messages.GROUPS_LIST.getKey() ), groupRecords );
        Map< String, Object > rolesData = new HashMap<>();
        rolesData.put( MessageBundleFactory.getMessage( Messages.ROLES_LIST.getKey() ), roleRecords );
        RightsListDTO rightsListDTO = new RightsListDTO();
        rightsListDTO.setGroupsList( groupsData );
        rightsListDTO.setRolesList( rolesData );
        return rightsListDTO;

    }

    /**
     * Gets document record.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the document record
     */
    private DocumentRecord getDocumentRecord( UserEntity userEntity ) {
        DocumentEntity documentEntity = userEntity.getDocument();
        if ( null != documentEntity ) {
            DocumentDTO documentDTO = documentManager.prepareDocumentDTO( documentEntity );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                    + File.separator + ConstantsString.STATIC_PATH + documentDTO.getPath() + File.separator + documentDTO.getName();
            UUID userId = null != documentEntity.getOwner() ? documentEntity.getOwner().getId() : null;
            String id = null != documentEntity.getId() ? String.valueOf( documentEntity.getId() ) : null;
            return new DocumentRecord( id, userId, documentEntity.getFileName(), documentEntity.getFileType(), documentEntity.getIsTemp(),
                    documentEntity.getProperties(), documentEntity.getFileSize(), documentEntity.getExpiry(), documentEntity.getAgent(),
                    documentEntity.getFilePath(), documentEntity.getEncoding(), documentEntity.getHash(), url,
                    documentEntity.isEncrypted() );
        }
        return null;
    }

    /**
     * Prepare user entity record from group entity user entity record.
     *
     * @param userEntity
     *         the user entity
     * @param documentRecord
     *         the document record
     *
     * @return the user entity record
     */
    private UserEntityRecord prepareUserEntityRecordFromGroupEntity( UserEntity userEntity, DocumentRecord profilePhoto ) {
        return new UserEntityRecord( userEntity.getId(), userEntity.isDelete(), userEntity.getUserUid(), userEntity.getFirstName(),
                userEntity.getSurName(), userEntity.isChangeable(), userEntity.getStatus(), userEntity.getDescription(), profilePhoto,
                userEntity.isRestricted(), userEntity.getTheme(), userEntity.getLocationPreferenceSelectionId(),
                userEntity.getLdapFirstName(), userEntity.getLdapSurName() );
    }

    /**
     * Prepare groups record from group entity group record.
     *
     * @param groupEntity
     *         the group entity
     * @param userEntityRecords
     *         the user entity records
     *
     * @return the group record
     */
    private GroupRecord prepareGroupsRecordFromGroupEntity( GroupEntity groupEntity, List< UserEntityRecord > userEntityRecords ) {
        return new GroupRecord( userEntityRecords, groupEntity.getName(), groupEntity.getDescription(), groupEntity.getStatus(),
                groupEntity.getSelectionId(), userEntityRecords.size() );
    }

    /**
     * Prepare roles record from role entity role record.
     *
     * @param roleEntity
     *         the role entity
     *
     * @return the role record
     */
    private RoleRecord prepareRolesRecordFromRoleEntity( RoleEntity roleEntity ) {
        return new RoleRecord( roleEntity.getDescription(), roleEntity.getName(), roleEntity.isStatus(), roleEntity.getSelectionid(),
                roleEntity.getGroups().size() );
    }

    /**
     * Sets license manager.
     *
     * @param licenseManager
     *         the license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Sets user group dao.
     *
     * @param userGroupDAO
     *         the user group dao
     */
    public void setUserGroupDAO( UserGroupDAO userGroupDAO ) {
        this.userGroupDAO = userGroupDAO;
    }

    /**
     * Sets role dao.
     *
     * @param roleDAO
     *         the role dao
     */
    public void setRoleDAO( RoleDAO roleDAO ) {
        this.roleDAO = roleDAO;
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

    /**
     * Sets permission manager.
     *
     * @param permissionManager
     *         the permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

}
