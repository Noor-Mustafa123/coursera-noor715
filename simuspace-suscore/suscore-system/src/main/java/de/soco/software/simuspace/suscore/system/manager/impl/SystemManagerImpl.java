package de.soco.software.simuspace.suscore.system.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.base.BaseManager;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.records.LocationRecord;
import de.soco.software.simuspace.suscore.common.records.UserRecord;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseUserDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.location.dao.LocationDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.rights.manager.RightsManager;
import de.soco.software.simuspace.suscore.rights.model.RightsListDTO;
import de.soco.software.simuspace.suscore.system.manager.SystemManager;
import de.soco.software.simuspace.suscore.system.model.SystemDTO;
import de.soco.software.simuspace.suscore.user.dao.DirectoryDAO;

/**
 * The type System manager.
 */
public class SystemManagerImpl extends BaseManager implements SystemManager {

    /**
     * The constant ROLES.
     */
    private static final String ROLES = "Roles";

    /**
     * The constant GROUPS.
     */
    private static final String GROUPS = "Groups";

    /**
     * The constant USER.
     */
    private static final String USER = "User";

    /**
     * The constant DIRECTORY.
     */
    private static final String DIRECTORY = "Directory";

    /**
     * The constant AUDIT.
     */
    private static final String AUDIT = "Audit";

    /**
     * The constant SYSTEM.
     */
    private static final String SYSTEM = "System";

    /**
     * The constant ACTIVE.
     */
    private static final String ACTIVE = "Active";

    /**
     * The constant INTERNAL.
     */
    private static final String INTERNAL = "Internal";

    /**
     * The constant LDAP.
     */
    private static final String LDAP = "LDAP";

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The Directory dao.
     */
    private DirectoryDAO directoryDAO;

    /**
     * The Rights manager.
     */
    private RightsManager rightsManager;

    /**
     * The Location dao.
     */
    private LocationDAO locationDAO;

    /**
     * The License manager.
     */
    private LicenseManager licenseManager;

    /**
     * The User dao.
     */
    private UserCommonDAO userCommonDAO;

    /**
     * The Audit log dao.
     */
    private AuditLogDAO auditLogDAO;

    /**
     * The License config dao.
     */
    private LicenseConfigurationDAO licenseConfigDAO;

    /**
     * The Token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The License user dao.
     */
    private LicenseUserDAO licenseUserDAO;

    /**
     * The Document manager.
     */
    private DocumentManager documentManager;

    @Override
    public SystemDTO getSystemDataCount( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getSystemDataCount( entityManager, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets system data count.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the system data count
     */
    private SystemDTO getSystemDataCount( EntityManager entityManager, String userIdFromGeneralHeader ) {
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.READ.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), SYSTEM );
        Map< String, Long > directoriesMap = getDirectoriesData( entityManager, userIdFromGeneralHeader );
        Map< String, Long > rightsMap = getRightsData( userIdFromGeneralHeader );
        Map< String, Object > locationsMap = getLocationsData( entityManager );
        Map< String, Object > usersMap = getUsersData( entityManager, userIdFromGeneralHeader );
        Map< String, Long > auditLogMap = getAuditLogsData( entityManager, userIdFromGeneralHeader );
        Map< String, Object > licenseModulesMap = getLicenseModulesData( entityManager );

        SystemDTO systemDTO = new SystemDTO();
        systemDTO.setDirectoriesList( directoriesMap );
        systemDTO.setRightsList( rightsMap );
        systemDTO.setLocationsList( locationsMap );
        systemDTO.setUsersList( usersMap );
        systemDTO.setAuditLogsList( auditLogMap );
        systemDTO.setLicenseModuleList( licenseModulesMap );
        return systemDTO;
    }

    /**
     * Gets directories data.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the directories data
     */
    private Map< String, Long > getDirectoriesData( EntityManager entityManager, String userIdFromGeneralHeader ) {
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.VIEW.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), DIRECTORY );
        List< SuSUserDirectoryEntity > directoriesList = directoryDAO.getAllObjectList( entityManager );
        long totalDirectoriesCount = directoriesList.size();
        long activeDirectoriesCount = countByDirectoryType( directoriesList, ACTIVE );
        long internalDirectoriesCount = countByDirectoryType( directoriesList, INTERNAL );
        long lDAPDirectoriesCount = countByDirectoryType( directoriesList, LDAP );

        return getDirectoriesLongMap( totalDirectoriesCount, activeDirectoriesCount, internalDirectoriesCount, lDAPDirectoriesCount );
    }

    /**
     * Gets rights data.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the rights data
     */
    private Map< String, Long > getRightsData( String userIdFromGeneralHeader ) {
        RightsListDTO rightsRecords = rightsManager.getAllRightsList( userIdFromGeneralHeader );
        long rightsGroupsCount = rightsRecords.getGroupsList().size();
        long rightsRolesCount = rightsRecords.getRolesList().size();
        return getRightsLongMap( rightsGroupsCount, rightsRolesCount );
    }

    /**
     * Gets locations data.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the locations data
     */
    private Map< String, Object > getLocationsData( EntityManager entityManager ) {
        List< LocationEntity > locationsList = locationDAO.getAllObjectList( entityManager );
        List< LocationRecord > locationRecordsList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( locationsList ) ) {
            for ( LocationEntity locationEntity : locationsList ) {
                locationRecordsList.add( prepareLocationRecordFromLocationEntity( locationEntity ) );
            }
        }
        long locationsListCount = locationRecordsList.size();
        Map< String, Object > locationsMap = new HashMap<>();
        locationsMap.put( MessageBundleFactory.getMessage( Messages.TOTAL_LOCATIONS.getKey() ), locationsListCount );
        locationsMap.put( MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_DATAOBJECT_LOCATION.getKey() ), locationRecordsList );
        return locationsMap;
    }

    /**
     * Gets users data.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the users data
     */
    private Map< String, Object > getUsersData( EntityManager entityManager, String userIdFromGeneralHeader ) {
        licenseManager.checkIfFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.USERS.getKey() );
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.VIEW.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), USER );
        List< UserEntity > userEntitiesList = userCommonDAO.getAllObjectList( entityManager );
        List< UserRecord > userRecordsList = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( userEntitiesList ) ) {
            for ( UserEntity user : userEntitiesList ) {
                userRecordsList.add( prepareUserRecordFromUserEntity( user ) );
            }
        }
        long totalUsersCount = userRecordsList.size();
        long activeDirectoryUsersCount = countUsersByDirectory( userEntitiesList, ACTIVE );
        long internalDirectoryUsersCount = countUsersByDirectory( userEntitiesList, INTERNAL );
        long lDAPDirectoryUsersCount = countUsersByDirectory( userEntitiesList, LDAP );

        return getUsersMap( userRecordsList, totalUsersCount, activeDirectoryUsersCount, internalDirectoryUsersCount,
                lDAPDirectoryUsersCount );
    }

    /**
     * Prepare user record from user entity user record.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user record
     */
    private UserRecord prepareUserRecordFromUserEntity( UserEntity userEntity ) {
        DocumentDTO documentDTO = null;
        if ( null != userEntity.getDocument() ) {
            documentDTO = documentManager.prepareDocumentDTO( userEntity.getDocument() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.SUS_WEB_BASE_URL ) )
                    + File.separator + ConstantsString.STATIC_PATH + documentDTO.getPath() + File.separator + documentDTO.getName();
            documentDTO.setUrl( url );
        }
        return new UserRecord( userEntity.getId().toString(), documentDTO, userEntity.getUserUid(), userEntity.getDescription(),
                userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE,
                Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO, userEntity.getFirstName(),
                userEntity.getSurName() != null ? userEntity.getSurName() : "", userEntity.getLocationPreferenceSelectionId() );
    }

    /**
     * Prepare location record from location entity user record.
     *
     * @param locationEntity
     *         the location entity
     *
     * @return the user record
     */
    private LocationRecord prepareLocationRecordFromLocationEntity( LocationEntity locationEntity ) {
        String statusStr = locationEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE;
        return new LocationRecord( locationEntity.getId(), locationEntity.getName(), locationEntity.getDescription(), statusStr,
                locationEntity.getType(), locationEntity.getPriority(), locationEntity.getVault(), locationEntity.getStaging(),
                locationEntity.getUrl(), locationEntity.getAuthToken(), locationEntity.isInternal() );
    }

    /**
     * Gets audit logs data.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the audit logs data
     */
    private Map< String, Long > getAuditLogsData( EntityManager entityManager, String userIdFromGeneralHeader ) {
        permissionManager.isPermitted( entityManager, userIdFromGeneralHeader, PermissionMatrixEnum.VIEW.getValue(),
                Messages.NO_RIGHTS_TO_READ.getKey(), AUDIT );
        List< AuditLogEntity > auditLogsList = auditLogDAO.getAllObjectList( entityManager );
        long auditLogsCount = auditLogsList.size();
        Map< String, Long > auditLogMap = new HashMap<>();
        auditLogMap.put( MessageBundleFactory.getMessage( Messages.TOTAL_AUDIT_LOGS.getKey() ), auditLogsCount );
        return auditLogMap;
    }

    /**
     * Gets license modules data.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the license modules data
     */
    private Map< String, Object > getLicenseModulesData( EntityManager entityManager ) {
        List< LicenseEntity > licenseList = licenseConfigDAO.getAllObjectList( entityManager );
        long licenseCount = licenseList.size();
        long currentActiveLicenseCount;
        List< LicenseUserEntity > licenseUserEntity = licenseUserDAO.getAllObjectList( entityManager );
        currentActiveLicenseCount = licenseUserEntity.size();

        List< String > modules = licenseList.stream()
                .map( license -> license.getModule().replace( "SIMuSPACE.", "" ) )  // Remove the "SIMuSPACE." prefix
                .toList();

        String formattedModules = modules.isEmpty() ? "No modules available" : String.join( " | ", modules );

        Map< String, Object > licenseModulesMap = new HashMap<>();
        licenseModulesMap.put( MessageBundleFactory.getMessage( Messages.TOTAL_LICENSE_MODULES.getKey() ), licenseCount );
        licenseModulesMap.put( MessageBundleFactory.getMessage( Messages.CURRENT_ACTIVE_LICENSE.getKey() ), currentActiveLicenseCount );
        licenseModulesMap.put( MessageBundleFactory.getMessage( Messages.LICENSE_MODULES.getKey() ), formattedModules );
        return licenseModulesMap;
    }

    /**
     * Count by directory type long.
     *
     * @param directoriesList
     *         the directories list
     * @param directoryType
     *         the directory type
     *
     * @return the long
     */
    private long countByDirectoryType( List< SuSUserDirectoryEntity > directoriesList, String directoryType ) {
        return directoriesList.stream().filter( item -> item.getName().equalsIgnoreCase( directoryType ) ).count();
    }

    /**
     * Count users by directory long.
     *
     * @param usersList
     *         the users list
     * @param directoryType
     *         the directory type
     *
     * @return the long
     */
    private long countUsersByDirectory( List< UserEntity > usersList, String directoryType ) {
        return usersList.stream().filter(
                        userEntity -> userEntity.getDirectory() != null && userEntity.getDirectory().getName().equalsIgnoreCase( directoryType ) )
                .count();
    }

    /**
     * Gets rights long map.
     *
     * @param rightsGroupsCount
     *         the rights groups count
     * @param rightsRolesCount
     *         the rights roles count
     *
     * @return the rights long map
     */
    private Map< String, Long > getRightsLongMap( long rightsGroupsCount, long rightsRolesCount ) {
        Map< String, Long > rightsMap = new HashMap<>();
        rightsMap.put( ROLES, rightsRolesCount );
        rightsMap.put( GROUPS, rightsGroupsCount );
        return rightsMap;
    }

    /**
     * Gets directories long map.
     *
     * @param totalDirectoriesCount
     *         the total directories count
     * @param activeDirectoriesCount
     *         the active directories count
     * @param internalDirectoriesCount
     *         the internal directories count
     * @param lDAPDirectoriesCount
     *         the l dap directories count
     *
     * @return the directories long map
     */
    private Map< String, Long > getDirectoriesLongMap( long totalDirectoriesCount, long activeDirectoriesCount,
            long internalDirectoriesCount, long lDAPDirectoriesCount ) {
        Map< String, Long > directoriesMap = new HashMap<>();
        directoriesMap.put( MessageBundleFactory.getMessage( Messages.TOTAL_DIRECTORIES.getKey() ), totalDirectoriesCount );
        directoriesMap.put( ACTIVE, activeDirectoriesCount );
        directoriesMap.put( INTERNAL, internalDirectoriesCount );
        directoriesMap.put( LDAP, lDAPDirectoriesCount );
        return directoriesMap;
    }

    /**
     * Gets users map.
     *
     * @param usersList
     *         the users list
     * @param totalUsersCount
     *         the total users count
     * @param activeDirectoryUsersCount
     *         the active directory users count
     * @param internalDirectoryUsersCount
     *         the internal directory users count
     * @param lDAPDirectoryUsersCount
     *         the l dap directory users count
     *
     * @return the users map
     */
    private Map< String, Object > getUsersMap( List< UserRecord > usersList, long totalUsersCount, long activeDirectoryUsersCount,
            long internalDirectoryUsersCount, long lDAPDirectoryUsersCount ) {
        Map< String, Object > usersMap = new HashMap<>();
        usersMap.put( MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_USERS.getKey() ), usersList );
        usersMap.put( MessageBundleFactory.getMessage( Messages.TOTAL_USERS.getKey() ), totalUsersCount );
        usersMap.put( ConstantsUserDirectories.ACTIVE_DIRECTORY, activeDirectoryUsersCount );
        usersMap.put( ConstantsUserDirectories.INTERNAL_DIRECTORY, internalDirectoryUsersCount );
        usersMap.put( ConstantsUserDirectories.LDAP_DIRECTORY, lDAPDirectoryUsersCount );
        return usersMap;
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

    /**
     * Sets directory dao.
     *
     * @param directoryDAO
     *         the directory dao
     */
    public void setDirectoryDAO( DirectoryDAO directoryDAO ) {
        this.directoryDAO = directoryDAO;
    }

    /**
     * Sets rights manager.
     *
     * @param rightsManager
     *         the rights manager
     */
    public void setRightsManager( RightsManager rightsManager ) {
        this.rightsManager = rightsManager;
    }

    /**
     * Sets location dao.
     *
     * @param locationDAO
     *         the location dao
     */
    public void setLocationDAO( LocationDAO locationDAO ) {
        this.locationDAO = locationDAO;
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
     * Sets user dao.
     *
     * @param userCommonDAO
     *         the user common dao
     */
    public void setUserCommonDAO( UserCommonDAO userCommonDAO ) {
        this.userCommonDAO = userCommonDAO;
    }

    /**
     * Sets audit log dao.
     *
     * @param auditLogDAO
     *         the audit log dao
     */
    public void setAuditLogDAO( AuditLogDAO auditLogDAO ) {
        this.auditLogDAO = auditLogDAO;
    }

    /**
     * Sets license config dao.
     *
     * @param licenseConfigDAO
     *         the license config dao
     */
    public void setLicenseConfigDAO( LicenseConfigurationDAO licenseConfigDAO ) {
        this.licenseConfigDAO = licenseConfigDAO;
    }

    /**
     * Sets license user dao.
     *
     * @param licenseUserDAO
     *         the license user dao
     */
    public void setLicenseUserDAO( LicenseUserDAO licenseUserDAO ) {
        this.licenseUserDAO = licenseUserDAO;
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
     * Sets document manager.
     *
     * @param documentManager
     *         the document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
    }

}
