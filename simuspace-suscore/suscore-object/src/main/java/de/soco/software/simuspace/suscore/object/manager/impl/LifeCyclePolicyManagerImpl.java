package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.MapUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.executor.enums.SchedulerType;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.dao.DataDAO;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;
import de.soco.software.simuspace.suscore.object.dao.SubmitLoadcaseDAO;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.manager.LifeCyclePolicyManager;
import de.soco.software.simuspace.suscore.object.threads.DummyLoadcasePendingJobsUpdateThread;
import de.soco.software.simuspace.suscore.object.threads.JobFailedOnMaxTimeThread;
import de.soco.software.simuspace.suscore.object.threads.JobStatusUpdationThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyCB2SessionKeepAliveThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyDeleteFeStaticFilesThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyDeleteSelectionThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyDeleteStagingJobFileThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyDeleteVaultFileThread;
import de.soco.software.simuspace.suscore.object.threads.LifeCyclePolicyUpdateTokenizedLicenseMapThread;
import de.soco.software.simuspace.suscore.object.threads.LifecyclePolicyAccountUnlockThread;
import de.soco.software.simuspace.suscore.object.threads.SchemePlotPolicyThread;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;

/**
 * The Class LifeCyclePolicyManagerImpl.
 *
 * @author Noman Arshad
 */
@Log4j2
public class LifeCyclePolicyManagerImpl extends SuSBaseService implements LifeCyclePolicyManager {

    /**
     * The Constant TOKEN_LICENSE.
     */
    private static final String TOKEN_LICENSE = "tokenLicense";

    /**
     * The Constant CB2.
     */
    private static final String CB2 = "cb2";

    /**
     * The Constant JOB_STATUS.
     */
    private static final String JOB_STATUS = "jobStatus";

    /**
     * The Constant STAGING.
     */
    private static final String STAGING = "staging";

    /**
     * The Constant VAULT.
     */
    private static final String VAULT = "vault";

    /**
     * The Constant FE_STATIC.
     */
    private static final String FE_STATIC = "fe_static";

    /**
     * The Constant SELECTION_CLEAR.
     */
    private static final String SELECTION_CLEAR = "selectionClear";

    /**
     * The constant ACCOUNT_UNLOCK.
     */
    private static final String ACCOUNT_UNLOCK = "accountUnlock";

    /**
     * The data manager.
     */
    private DataManager dataManager;

    /**
     * The data DAO.
     */
    private DataDAO dataDAO;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * The schedule DAO.
     */
    private ScheduleDAO scheduleDAO;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The submit loadcase DAO.
     */
    private SubmitLoadcaseDAO submitLoadcaseDAO;

    /**
     * The auth manager.
     */
    private AuthManager authManager;

    /**
     * The ldap auth realm.
     */
    private LdapCustomAuthRealm ldapAuthRealm;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Life cycle policy thread map.
     */
    private final Map< UserThread, Future< ? > > lifeCyclePolicyThreadMap = new ConcurrentHashMap<>();

    /**
     * Inits the.
     */
    public void init() {
        log.info( "Activating lifeCycle policy" );

        executeLifeCyclePolicyDeletionScheduler();
        log.info( "lifeCycle policy is Activated" );

    }

    /**
     * Destroy.
     */
    public void destroy() {
        if ( !MapUtils.isEmpty( lifeCyclePolicyThreadMap ) ) {
            for ( Map.Entry< UserThread, Future< ? > > entry : lifeCyclePolicyThreadMap.entrySet() ) {
                entry.getValue().cancel( true );
                try {
                    entry.getKey().blockUntilInterrupted();
                } catch ( InterruptedException e ) {
                    log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                    Thread.currentThread().interrupt();
                }
                lifeCyclePolicyThreadMap.remove( entry.getKey() );
            }
        }
    }

    /**
     * Execute life cycle policy deletion scheduler.
     */
    public void executeLifeCyclePolicyDeletionScheduler() {
        int schedulerTimeVault = 300; // if config doesn't have schedulerTime for vault
        int schedulerTimeFeStatic = 300; // if config doesn't have schedulerTime for FeStatic
        int schedulerTimeStaging = 300;// if config doesn't have schedulerTime for staging
        int schedulerTimeTokenizedLicense = 5;// if config doesn't have schedulerTime for token license
        int schedulerTimeCB2Expiry = 300;// if config doesn't have schedulerTime for cb2
        int schedulerTimeJobStatus = 30;// if config doesn't have schedulerTime for job status update
        int schedulerTimeSelectionClear = 30;// if config doesn't have schedulerTime for selection clear
        int schedulerTimeAccountUnlock = 1;// if config doesn't have schedulerTime for account unlock

        List< LifeCyclePolicyDTO > lifeCycleConfig = lifeCycleManager.getLifeCyclePolicyConfigurationByFileName();

        for ( LifeCyclePolicyDTO lifeCycleDTO : lifeCycleConfig ) {
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( VAULT ) && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeVault = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( FE_STATIC ) && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeFeStatic = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( STAGING ) && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeStaging = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( TOKEN_LICENSE )
                    && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeTokenizedLicense = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( CB2 ) && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeCB2Expiry = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( JOB_STATUS ) && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeJobStatus = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( SELECTION_CLEAR )
                    && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeSelectionClear = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
            if ( lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( ACCOUNT_UNLOCK )
                    && lifeCycleDTO.getSchedulerTimeMinutes() != null ) {
                schedulerTimeAccountUnlock = Integer.parseInt( lifeCycleDTO.getSchedulerTimeMinutes() );
            }
        }

        /* Vault policy scheduled **/
        log.info( "Vault policy scheduled" );
        LifeCyclePolicyDeleteVaultFileThread deleteFileThread = new LifeCyclePolicyDeleteVaultFileThread( susDAO, configManager,
                scheduleDAO, lifeCycleConfig, entityManagerFactory );
        Future< ? > deleteFileThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( deleteFileThread, TimeUnit.MINUTES,
                SchedulerType.FIXED_DELAY, schedulerTimeVault, schedulerTimeVault );
        lifeCyclePolicyThreadMap.put( deleteFileThread, deleteFileThreadFuture );

        /* Fe_Static policy scheduled **/
        log.info( "Fe_Static policy scheduled" );
        LifeCyclePolicyDeleteFeStaticFilesThread deleteFeStaticFilesThread = new LifeCyclePolicyDeleteFeStaticFilesThread( susDAO,
                configManager, scheduleDAO, lifeCycleConfig, entityManagerFactory );
        Future< ? > deleteFeStaticFileThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( deleteFeStaticFilesThread,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, schedulerTimeFeStatic, schedulerTimeFeStatic );
        lifeCyclePolicyThreadMap.put( deleteFeStaticFilesThread, deleteFeStaticFileThreadFuture );

        /* staging policy scheduled **/
        log.info( "staging policy scheduled" );
        LifeCyclePolicyDeleteStagingJobFileThread deleteStagingFileThread = new LifeCyclePolicyDeleteStagingJobFileThread( scheduleDAO,
                lifeCycleConfig, jobManager, entityManagerFactory );
        Future< ? > deleteStagingFileThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( deleteStagingFileThread,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, schedulerTimeStaging, schedulerTimeStaging );
        lifeCyclePolicyThreadMap.put( deleteStagingFileThread, deleteStagingFileThreadFuture );

        /* Tokenized License Map Update policy scheduled **/
        log.info( "Tokenized License Map Update policy scheduled" );
        LifeCyclePolicyUpdateTokenizedLicenseMapThread tokenizedLicenseUpdate = new LifeCyclePolicyUpdateTokenizedLicenseMapThread(
                licenseManager, tokenManager, entityManagerFactory );
        Future< ? > tokenizedLicenseUpdateFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( tokenizedLicenseUpdate,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, schedulerTimeTokenizedLicense, schedulerTimeTokenizedLicense );
        lifeCyclePolicyThreadMap.put( tokenizedLicenseUpdate, tokenizedLicenseUpdateFuture );

        /* Delete scheme plots policy scheduled **/
        log.info( "Tokenized License Map Update policy scheduled" );
        SchemePlotPolicyThread schemePlotPolicyThread = new SchemePlotPolicyThread( susDAO, scheduleDAO, entityManagerFactory );
        Future< ? > schemePlotPolicyThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( schemePlotPolicyThread,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, 1, 1 );
        lifeCyclePolicyThreadMap.put( schemePlotPolicyThread, schemePlotPolicyThreadFuture );

        /* Cb2 Session Keep Alive Policy Scheduled **/
        log.info( "CB2 session keep alive scheduled" );
        LifeCyclePolicyCB2SessionKeepAliveThread cb2SessionThread = new LifeCyclePolicyCB2SessionKeepAliveThread( licenseManager,
                getTokenFromGeneralHeader(), tokenManager, ldapAuthRealm, userCommonManager, lifeCycleConfig, scheduleDAO,
                entityManagerFactory );
        Future< ? > cb2SessionThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( cb2SessionThread, TimeUnit.MINUTES,
                SchedulerType.FIXED_DELAY, schedulerTimeCB2Expiry, schedulerTimeCB2Expiry );
        lifeCyclePolicyThreadMap.put( cb2SessionThread, cb2SessionThreadFuture );

        log.info( "Parent Job updation scheduled" );
        JobStatusUpdationThread jobUpdationThread = new JobStatusUpdationThread( jobManager, susDAO, authManager, tokenManager,
                entityManagerFactory );
        Future< ? > jobUpdationThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( jobUpdationThread,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, schedulerTimeJobStatus, schedulerTimeJobStatus );
        lifeCyclePolicyThreadMap.put( jobUpdationThread, jobUpdationThreadFuture );

        log.info( "Job Failed on Max Execution time thread scheduled" );
        JobFailedOnMaxTimeThread jobFailedOnMaxTimeThread = new JobFailedOnMaxTimeThread( dataManager, jobManager, authManager,
                entityManagerFactory );
        Future< ? > jobFailedOnMaxTimeThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( jobFailedOnMaxTimeThread,
                TimeUnit.MINUTES, SchedulerType.FIXED_DELAY, 1, 1 );
        lifeCyclePolicyThreadMap.put( jobFailedOnMaxTimeThread, jobFailedOnMaxTimeThreadFuture );

        log.info( "Dummy Job updation scheduled" );
        DummyLoadcasePendingJobsUpdateThread dummyJobUpdate = new DummyLoadcasePendingJobsUpdateThread( submitLoadcaseDAO,
                entityManagerFactory );
        Future< ? > dummyJobUpdateFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( dummyJobUpdate, TimeUnit.MINUTES,
                SchedulerType.FIXED_DELAY, 1, 1 );
        lifeCyclePolicyThreadMap.put( dummyJobUpdate, dummyJobUpdateFuture );

        log.info( "Selection removal scheduled" );
        LifeCyclePolicyDeleteSelectionThread selectionThread = new LifeCyclePolicyDeleteSelectionThread( selectionManager,
                entityManagerFactory );
        Future< ? > selectionThreadFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( selectionThread, TimeUnit.MINUTES,
                SchedulerType.FIXED_DELAY, schedulerTimeSelectionClear, schedulerTimeSelectionClear );
        lifeCyclePolicyThreadMap.put( selectionThread, selectionThreadFuture );

        log.info( "Account Lock scheduled" );
        LifecyclePolicyAccountUnlockThread accountLockThread = new LifecyclePolicyAccountUnlockThread( entityManagerFactory,
                userCommonManager );
        Future< ? > accountLockFuture = threadPoolExecutorService.registerAndExecuteScheduledTask( accountLockThread, TimeUnit.MINUTES,
                SchedulerType.FIXED_DELAY, schedulerTimeAccountUnlock, schedulerTimeAccountUnlock );
        lifeCyclePolicyThreadMap.put( accountLockThread, accountLockFuture );
    }

    /**
     * Gets the data DAO.
     *
     * @return the data DAO
     */
    public DataDAO getDataDAO() {
        return dataDAO;
    }

    /**
     * Sets the data DAO.
     *
     * @param dataDAO
     *         the new data DAO
     */
    public void setDataDAO( DataDAO dataDAO ) {
        this.dataDAO = dataDAO;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
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
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    public LifeCycleManager getLifeCycleManager() {
        return lifeCycleManager;
    }

    /**
     * Sets the life cycle manager.
     *
     * @param lifeCycleManager
     *         the new life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    /**
     * Gets the config manager.
     *
     * @return the config manager
     */
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    /**
     * Gets the data manager.
     *
     * @return the data manager
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Sets the data manager.
     *
     * @param dataManager
     *         the new data manager
     */
    public void setDataManager( DataManager dataManager ) {
        this.dataManager = dataManager;
    }

    /**
     * Gets the thread pool executor service.
     *
     * @return the thread pool executor service
     */
    public ThreadPoolExecutorService getThreadPoolExecutorService() {
        return threadPoolExecutorService;
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the new thread pool executor service
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * Gets the schedule DAO.
     *
     * @return the schedule DAO
     */
    public ScheduleDAO getScheduleDAO() {
        return scheduleDAO;
    }

    /**
     * Sets the schedule DAO.
     *
     * @param scheduleDAO
     *         the new schedule DAO
     */
    public void setScheduleDAO( ScheduleDAO scheduleDAO ) {
        this.scheduleDAO = scheduleDAO;
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
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
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
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
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets the submit loadcase DAO.
     *
     * @return the submit loadcase DAO
     */
    public SubmitLoadcaseDAO getSubmitLoadcaseDAO() {
        return submitLoadcaseDAO;
    }

    /**
     * Sets the submit loadcase DAO.
     *
     * @param submitLoadcaseDAO
     *         the new submit loadcase DAO
     */
    public void setSubmitLoadcaseDAO( SubmitLoadcaseDAO submitLoadcaseDAO ) {
        this.submitLoadcaseDAO = submitLoadcaseDAO;
    }

    /**
     * Gets the auth manager.
     *
     * @return the auth manager
     */
    public AuthManager getAuthManager() {
        return authManager;
    }

    /**
     * Sets the auth manager.
     *
     * @param authManager
     *         the new auth manager
     */
    public void setAuthManager( AuthManager authManager ) {
        this.authManager = authManager;
    }

    /**
     * Gets the ldap auth realm.
     *
     * @return the ldap auth realm
     */
    public LdapCustomAuthRealm getLdapAuthRealm() {
        return ldapAuthRealm;
    }

    /**
     * Sets the ldap auth realm.
     *
     * @param ldapAuthRealm
     *         the new ldap auth realm
     */
    public void setLdapAuthRealm( LdapCustomAuthRealm ldapAuthRealm ) {
        this.ldapAuthRealm = ldapAuthRealm;
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
     *         the new selectionManager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
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
