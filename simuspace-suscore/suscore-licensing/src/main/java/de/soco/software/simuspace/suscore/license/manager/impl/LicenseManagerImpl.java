package de.soco.software.simuspace.suscore.license.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.BaseManager;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLicenseType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.MonitorLicenseEntity;
import de.soco.software.simuspace.suscore.license.manager.LicenseConfigurationManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.license.manager.MonitorLicenseManager;

/**
 * The Class provides validation of license according to the user.
 *
 * @author M.Nasir.Farooq
 * @since 2.0
 */
@Log4j2
public class LicenseManagerImpl extends BaseManager implements LicenseManager {

    /**
     * The monitor license manager.
     */
    private MonitorLicenseManager monitorLicenseManager;

    /**
     * The license config manager.
     */
    private LicenseConfigurationManager licenseConfigManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new license manager impl.
     */
    public LicenseManagerImpl() {
        super( LicenseManagerImpl.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ModuleLicenseDTO > getLicenseUserEntityListByUserId( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< ModuleLicenseDTO > moduleLicenseDTOS;
        try {
            if ( userId == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.USER_ID_CANNOT_BE_NULL.getKey() ) );
            }
            moduleLicenseDTOS = licenseConfigManager.getLicenseUserEntityListByUserId( entityManager, userId );
        } finally {
            entityManager.close();
        }
        return moduleLicenseDTOS;
    }

    @Override
    public boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature ) {
        return licenseConfigManager.checkIfFeatureAllowedToUser( entityManager, feature, getUserIdFromMessage() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFeatureAllowedToUser( String feature, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return isFeatureAllowedToUser( entityManager, feature, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId ) {
        return licenseConfigManager.isFeatureAllowedToUser( entityManager, feature, userId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAllowedLocationsToUser( EntityManager entityManager, String userId ) {
        return licenseConfigManager.getAllowedLocationsToUser( entityManager, userId );
    }

    @Override
    public boolean isLicenseAddedAgainstUserForModule( EntityManager entityManager, String feature, String userId ) {
        return getLicenseConfigManager().checkIfFeatureAllowedToUser( entityManager, feature, userId );
    }

    /**
     * Gets the license config manager.
     *
     * @return the licenseConfigManager
     */
    @Override
    public LicenseConfigurationManager getLicenseConfigManager() {
        return licenseConfigManager;
    }

    /**
     * Sets the license config manager.
     *
     * @param licenseConfigManager
     *         the licenseConfigManager to set
     */
    @Override
    public void setLicenseConfigManager( LicenseConfigurationManager licenseConfigManager ) {
        this.licenseConfigManager = licenseConfigManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenBasedLicenseExists() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return licenseConfigManager.isTokenBasedLicenseExists( entityManager );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is token based license exists.
     *
     * @return true, if is token based license exists
     */
    @Override
    public boolean isTokenBasedLicenseExists( EntityManager entityManager ) {
        return licenseConfigManager.isTokenBasedLicenseExists( entityManager );
    }

    /**
     * Adds the user in tokenized license map.
     *
     * @param token
     *         the token
     * @param userDto
     *         the user dto
     *
     * @return true, if successful
     */
    @Override
    public SuSCoreSessionDTO addUserInTokenizedLicenseMap( String token, SuSCoreSessionDTO sessionDTO ) {
        updateUserWithDetailsIfNotSet( sessionDTO );
        TokenizedLicenseUtil.addSession( token, sessionDTO );
        addMonitorEntryInThread();
        if ( log.isDebugEnabled() ) {
            log.debug( "user Added into Token Map :" + TokenizedLicenseUtil.getMap().size() );
        }
        return sessionDTO;
    }

    /**
     * Update user with details if not set.
     *
     * @param sessionDTO
     *         the session DTO
     */
    private void updateUserWithDetailsIfNotSet( SuSCoreSessionDTO sessionDTO ) {
        if ( CollectionUtils.isEmpty( sessionDTO.getUser().getUserDetails() ) ) {
            var userWithDetails = licenseConfigManager.getUserFromId( sessionDTO.getUser().getId() );
            sessionDTO.setUser( userWithDetails );
        }
    }

    /**
     * Removes the user from tokenized license map.
     *
     * @param token
     *         the token
     */
    @Override
    public void removeUserFromTokenizedLicenseMap( String token ) {
        TokenizedLicenseUtil.removeEntry( token );
        addMonitorEntryInThread();
        if ( log.isDebugEnabled() ) {
            log.debug( "user Added into Token Map :" + TokenizedLicenseUtil.getMap().size() );
        }
    }

    @Override
    public void updateTokenizedLicenseMap( Map< String, SuSCoreSessionDTO > newMap ) {
        TokenizedLicenseUtil.updateMap( newMap );
        addMonitorEntryInThread();
    }

    /**
     * Adds the monitor entry in thread.
     */
    private void addMonitorEntryInThread() {
        Runnable computeLicenseAndAddMonitorHistory = this::computeLicenseAndAddMonitorHistory;
        Thread thread = new Thread( computeLicenseAndAddMonitorHistory );
        thread.start();
    }

    @Override
    public void computeLicenseAndAddMonitorHistory() {
        MonitorLicenseEntity monitorEntity = new MonitorLicenseEntity();

        monitorEntity.setId( UUID.randomUUID() );
        monitorEntity.setDescription( "Auth service entry point" );
        monitorEntity.setCreatedOn( new Date() );

        int wfUserLicenseConsume = 0;
        int AssemblyLicenseConsume = 0;
        int dataLicenseConsume = 0;
        int managerLicenseConsume = 0;
        int postLicenseConsume = 0;
        int cb2LicenseConsume = 0;
        int doeLicenseConsume = 0;
        int optimizationLicenseConsume = 0;

        List< String > uuidList = getUserIdsOfActiveUsers();

        for ( String userId : uuidList ) {
            List< ModuleLicenseDTO > modulesListOfSingleUser = getLicenseUserEntityListByUserId( userId );
            for ( ModuleLicenseDTO modulesOfSingleUser : modulesListOfSingleUser ) {
                if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DATA ) ) {
                    dataLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER ) ) {
                    managerLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule()
                        .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_ASSEMBLY ) ) {
                    AssemblyLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule()
                        .equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_MANAGER_POST ) ) {
                    postLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_WORKFLOW_USER ) ) {
                    wfUserLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_CB2 ) ) {
                    cb2LicenseConsume++;
                } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_DOE ) ) {
                    doeLicenseConsume++;
                } else if ( modulesOfSingleUser.getModule().equalsIgnoreCase( ConstantsLicenseType.MODULE_SIMUSPACE_OPTIMIZATION ) ) {
                    optimizationLicenseConsume++;
                }
            }
        }
        monitorEntity.setDataLicense( dataLicenseConsume );
        monitorEntity.setWorkflowLicense( managerLicenseConsume );
        monitorEntity.setUserLicense( wfUserLicenseConsume );
        monitorEntity.setAssemblyLicense( AssemblyLicenseConsume );
        monitorEntity.setPostLicense( postLicenseConsume );
        monitorEntity.setCb2License( cb2LicenseConsume );
        monitorEntity.setDoeLicense( doeLicenseConsume );
        monitorEntity.setOptimizationLicense( optimizationLicenseConsume );

        monitorLicenseManager.addMonitoringHistory( monitorEntity );
    }

    /**
     * Gets the user ids of active users.
     *
     * @return the user ids of active users
     */
    private List< String > getUserIdsOfActiveUsers() {
        List< String > uuidList = new ArrayList<>();
        if ( isTokenBasedLicenseExists() ) {
            Map< String, SuSCoreSessionDTO > activeUsersMap = TokenizedLicenseUtil.getMap();
            for ( Map.Entry< String, SuSCoreSessionDTO > activeSession : activeUsersMap.entrySet() ) {
                uuidList.add( activeSession.getValue().getUser().getId() );
            }
        } else {
            List< UserTokenDTO > activeTokenList = licenseConfigManager.getAllActiveTokens();
            for ( UserTokenDTO userTokenDTO : activeTokenList ) {
                if ( !uuidList.contains( userTokenDTO.getUserId() ) ) {
                    uuidList.add( userTokenDTO.getUserId() );
                }
            }
        }
        return uuidList;
    }

    /**
     * Sets the monitor license manager.
     *
     * @param monitorLicenseManager
     *         the new monitor license manager
     */
    public void setMonitorLicenseManager( MonitorLicenseManager monitorLicenseManager ) {
        this.monitorLicenseManager = monitorLicenseManager;
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

}