package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.auth.connect.ldap.authentication.LdapCustomAuthRealm;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.enums.Cb2OperationEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PasswordUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;

/**
 * The Class LifeCyclePolicyCB2SessionKeepAliveThread is to keep alive cb2 session in BMW for api calls.
 *
 * @author noman arshad
 * @author Shahzeb Iqbal
 * @since 2.0
 * @since 2.3
 */
@Log4j2
public class LifeCyclePolicyCB2SessionKeepAliveThread extends UserThread {

    /**
     * The constant TREE_UPDATE_POLICY.
     */
    private static final String TREE_UPDATE_POLICY = "Tree updation: ";

    /**
     * The constant SESSION_UPDATE_POLICY.
     */
    private static final String SESSION_UPDATE_POLICY = "Session updation: ";

    /**
     * The constant CB2_SESSION_UPDATE_KEY.
     */
    private static final String CB2_SESSION_UPDATE_KEY = "cb2 session update";

    /**
     * The constant CB2_TREE_UPDATE_KEY.
     */
    private static final String CB2_TREE_UPDATE_KEY = "cb2 tree update";

    /**
     * The constant MODEL_TREE.
     */
    private static final String MODEL_TREE = "submodelsTree.json";

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The ldap auth realm.
     */
    private LdapCustomAuthRealm ldapAuthRealm;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The Schedule dao.
     */
    private ScheduleDAO scheduleDAO;

    /**
     * The life cycle config.
     */
    private List< LifeCyclePolicyDTO > lifeCycleConfig;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The token.
     */
    private String token;

    /**
     * Instantiates a new life cycle policy CB 2 session keep alive thread.
     */
    public LifeCyclePolicyCB2SessionKeepAliveThread() {
    }

    /**
     * Instantiates a new life cycle policy CB 2 session keep alive thread.
     *
     * @param licenseManager
     *         the license manager
     * @param token
     *         the token
     * @param tokenManager
     *         the user-token manager
     * @param ldapAuthRealm
     *         the ldapAuthRealm
     * @param userCommonManager
     *         the user common manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public LifeCyclePolicyCB2SessionKeepAliveThread( LicenseManager licenseManager, String token, UserTokenManager tokenManager,
            LdapCustomAuthRealm ldapAuthRealm, UserCommonManager userCommonManager, List< LifeCyclePolicyDTO > lifeCycleConfig,
            ScheduleDAO scheduleDAO, EntityManagerFactory entityManagerFactory ) {
        this.licenseManager = licenseManager;
        this.token = token;
        this.tokenManager = tokenManager;
        this.ldapAuthRealm = ldapAuthRealm;
        this.userCommonManager = userCommonManager;
        this.lifeCycleConfig = lifeCycleConfig;
        this.scheduleDAO = scheduleDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( PropertiesManager.loginToCB2() ) {

                log.info( "updating session for cb2 apis" );
                List< UserTokenEntity > userTokenEntityList = tokenManager.getAllActiveOrRunningJobTokens( entityManager );
                updateCB2Session( entityManager, userTokenEntityList );
                updateCB2TreeJson( entityManager, userTokenEntityList );
            }
        } catch ( Exception e ) {
            log.error( e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update cb 2 session.
     *
     * @param userTokenEntityList
     *         the user token entity list
     * @param entityManager
     *         the entity manager
     */
    private void updateCB2Session( EntityManager entityManager, List< UserTokenEntity > userTokenEntityList ) {
        try {
            ScheduleEntity scheduleEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, "name",
                    CB2_SESSION_UPDATE_KEY );
            int intervalInHours = getIntervalTimeFromConfiguration( "updateSession" );
            if ( scheduleEntity == null ) {
                createSchedulerEntityIfNotExist( entityManager, intervalInHours, CB2_SESSION_UPDATE_KEY );
            } else {
                Date lastDeletion = scheduleEntity.getLastRun();
                Date curentDate = new Date();
                if ( curentDate.compareTo( scheduleEntity.getNextRun() ) < ConstantsInteger.INTEGER_VALUE_ZERO ) {
                    log.info( SESSION_UPDATE_POLICY + curentDate + " is before " + scheduleEntity.getNextRun() );
                } else if ( curentDate.compareTo( scheduleEntity.getNextRun() ) > ConstantsInteger.INTEGER_VALUE_ZERO ) {
                    log.info( SESSION_UPDATE_POLICY + "policy criteria matched : scheduler called" );
                    updateSession( userTokenEntityList, entityManager );
                    log.info( SESSION_UPDATE_POLICY + "updating Next Schedule Date" );
                    // updating ScheduleEntity for next Schedule
                    updateScheduleEntity( entityManager, scheduleEntity, lastDeletion, intervalInHours );
                }
            }

        } catch ( Exception e ) {
            log.error( " Error : ", e );
        }
    }

    /**
     * Update session.
     *
     * @param userTokenEntityList
     *         the user token entity list
     * @param entityManager
     *         the entity manager
     */
    private void updateSession( List< UserTokenEntity > userTokenEntityList, EntityManager entityManager ) {
        try {
            // execute policy if Schedule date and time met the conditions
            if ( CollectionUtil.isNotEmpty( userTokenEntityList ) ) {
                updateSessionForLicenseTokens( entityManager, userTokenEntityList );
            } else {
                checkAndUpdateSessionForNamedLicense( entityManager );
            }
        } catch ( Exception e ) {
            log.warn( SESSION_UPDATE_POLICY + " Error : ", e );
        }
    }

    /**
     * Update cb 2 tree json.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntityList
     *         the user token entity list
     */
    private void updateCB2TreeJson( EntityManager entityManager, List< UserTokenEntity > userTokenEntityList ) {
        try {
            ScheduleEntity scheduleEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, "name",
                    CB2_TREE_UPDATE_KEY );
            int intervalInHours = getIntervalTimeFromConfiguration( "updateTree" );
            if ( scheduleEntity == null ) {
                createSchedulerEntityIfNotExist( entityManager, intervalInHours, CB2_TREE_UPDATE_KEY );
            } else {
                Date curentDate = new Date();
                if ( curentDate.compareTo( scheduleEntity.getNextRun() ) < ConstantsInteger.INTEGER_VALUE_ZERO ) {
                    log.info( TREE_UPDATE_POLICY + curentDate + " is before " + scheduleEntity.getNextRun() );
                } else if ( curentDate.compareTo( scheduleEntity.getNextRun() ) > ConstantsInteger.INTEGER_VALUE_ZERO ) {
                    log.info( TREE_UPDATE_POLICY + "policy criteria matched : scheduler called" );
                    updateTree( entityManager, userTokenEntityList, scheduleEntity, intervalInHours );
                    log.info( TREE_UPDATE_POLICY + "updating Next Schedule Date" );
                }
            }
        } catch ( Exception e ) {
            log.error( " Error : ", e );
        }
    }

    /**
     * Update tree.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntityList
     *         the user token entity list
     * @param scheduleEntity
     *         the schedule entity
     * @param intervalInHours
     *         the interval in hours
     */
    private void updateTree( EntityManager entityManager, List< UserTokenEntity > userTokenEntityList, ScheduleEntity scheduleEntity,
            int intervalInHours ) {
        try {
            // execute policy if Schedule date and time met the conditions
            var userTokenList = userTokenEntityList.stream().filter( user -> cb2LoginCriteria( entityManager, user ) ).toList();
            if ( userTokenList.isEmpty() ) {
                log.info( TREE_UPDATE_POLICY + " skipping tree update due to lack of active cb2 session" );
            } else {
                for ( var userToken : userTokenList ) {
                    var userUid = userToken.getUserEntity().getUserUid();
                    // updating ScheduleEntity for next Schedule
                    updateScheduleEntity( entityManager, scheduleEntity, scheduleEntity.getNextRun(), intervalInHours );
                    var inputFile = Paths.get( PropertiesManager.getUserDefaultServerTempPath( userUid ), MODEL_TREE );
                    if ( Files.notExists( inputFile.getParent() ) ) {
                        Files.createDirectory( inputFile.getParent() );
                    }
                    FileUtils.setGlobalAllFilePermissions( inputFile.getParent() );
                    ProcessResult result = PythonUtils.CB2TreeByPython( userUid, getPasswordIfNull( userToken.getUserEntity() ),
                            tokenManager.getRefreshTokenFromCurrentSessionUsingUid( userUid ), inputFile.toString() );
                    if ( result.getExitValue() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
                        Date currDate = new Date();
                        scheduleEntity.setLastRun( currDate );
                        scheduleEntity.setNextRun( currDate );
                        scheduleDAO.saveOrUpdate( entityManager, scheduleEntity );
                    }
                }

            }
        } catch ( Exception e ) {
            log.warn( TREE_UPDATE_POLICY + " Error : ", e );
        }
    }

    /**
     * Update schedule entity.
     *
     * @param entityManager
     *         the entity manager
     * @param scheduleEntity
     *         the schedule entity
     * @param lastRun
     *         the last deletion
     * @param intervalInHours
     *         the interval in hours
     */
    private void updateScheduleEntity( EntityManager entityManager, ScheduleEntity scheduleEntity, Date lastRun, int intervalInHours ) {
        scheduleEntity.setLastRun( lastRun );
        Date nextDate = getNextScheduleDate( intervalInHours );
        scheduleEntity.setNextRun( nextDate );
        scheduleDAO.saveOrUpdate( entityManager, scheduleEntity );
    }

    /**
     * Creates the scheduler Entity if not exist.
     *
     * @param intervalInHours
     *         the interval in hours
     */
    private void createSchedulerEntityIfNotExist( EntityManager entityManager, int intervalInHours, String processName ) {
        // create ScheduleEntity if not existed
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId( UUID.randomUUID() );
        schedule.setLastRun( new Date( 0L ) );
        schedule.setName( processName );
        Date nextDate = getNextScheduleDate( intervalInHours );
        schedule.setNextRun( nextDate );
        scheduleDAO.saveOrUpdate( entityManager, schedule );
    }

    /**
     * Gets the interval time from configuration.
     *
     * @return the interval time from configuration
     */
    private int getIntervalTimeFromConfiguration( String processName ) {
        LifeCyclePolicyDTO lifeCycleDTO = lifeCycleConfig.stream()
                .filter( lifeCycle -> "cb2".equalsIgnoreCase( lifeCycle.getLifeCyclePolicyApply() ) ).findFirst().orElse( null );
        if ( lifeCycleDTO == null ) {
            throw new SusException( "Please update CB2 process in Lifecycle policy" );
        }
        var lifeCyclePolicyConfiguration = lifeCycleDTO.getPolicyProcess().stream()
                .filter( policy -> processName.equals( policy.getProcessName() ) ).findFirst().orElse( null );
        if ( lifeCyclePolicyConfiguration != null ) {
            return Integer.parseInt( lifeCyclePolicyConfiguration.getProcessTimeHours() );
        }
        throw new SusException( "Please update " + processName + "  in Lifecycle policy" );
    }

    /**
     * Gets the next schedule date.
     *
     * @param intervalInHours
     *         the interval in hours
     *
     * @return the next schedule date
     */
    private Date getNextScheduleDate( int intervalInHours ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.HOUR_OF_DAY, +intervalInHours );
        return calendar.getTime();
    }

    /**
     * update session in case of named license
     */
    private void checkAndUpdateSessionForNamedLicense( EntityManager entityManager ) {
        List< UserTokenEntity > allActiveTokens = tokenManager.getAllActiveTokenEntities( entityManager );
        if ( CollectionUtils.isEmpty( allActiveTokens ) ) {
            return;
        }
        List< String > userIds = new ArrayList<>();
        for ( UserTokenEntity userTokenEntity : allActiveTokens ) {
            if ( !userIds.contains( userTokenEntity.getUserEntity().getId().toString() ) ) {
                userIds.add( userTokenEntity.getUserEntity().getId().toString() );
                if ( cb2LoginCriteria( entityManager, userTokenEntity ) ) {
                    log.info( "updating session for cb2 apis for Named license" );
                    String password = PasswordUtils.getPasswordById( userTokenEntity.getUserEntity().getId().toString() );
                    if ( null != password ) {
                        validAuthenticationToCb2( entityManager, userTokenEntity.getUserEntity().getUserUid(), password, userTokenEntity );
                    }
                }
            }
        }
    }

    /**
     * update session in case of token license
     *
     * @param userTokenEntityList
     *         the user token entity list
     */
    private void updateSessionForLicenseTokens( EntityManager entityManager, List< UserTokenEntity > userTokenEntityList ) {
        log.info( "updating session for cb2 apis for license tokens" );
        for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
            UserEntity userEntity = userTokenEntity.getUserEntity();
            if ( cb2LoginCriteria( entityManager, userTokenEntity ) ) {
                String passwd = getPasswordIfNull( userEntity );
                if ( passwd != null ) {
                    validAuthenticationToCb2( entityManager, userEntity.getUserUid(), passwd, userTokenEntity );
                }
            }
        }
    }

    /**
     * Cb 2 login criteria.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     *
     * @return true, if successful
     */
    private boolean cb2LoginCriteria( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        return !ConstantsID.SUPER_USER_ID.equals( userTokenEntity.getUserEntity().getId().toString() ) && PropertiesManager.loginToCB2()
                && licenseManager.isFeatureAllowedToUser( entityManager, SimuspaceFeaturesEnum.CB2_CONNECTOR.getKey(),
                userTokenEntity.getUserEntity().getId().toString() ) && BooleanUtils.isFalse(
                TokenizedLicenseUtil.isWenLogin( userTokenEntity.getToken() ) )
                && userCommonManager.getUserFailedLdapAttempts( entityManager, userTokenEntity.getUserEntity().getUserUid() )
                < ConstantsInteger.INTEGER_VALUE_ONE;
    }

    /**
     * get password
     *
     * @param user
     *         the user
     *
     * @return the user's password
     */
    private String getPasswordIfNull( UserEntity user ) {
        if ( null != user.getPassword() && !user.getPassword().isEmpty() ) {
            return user.getPassword();
        } else {
            return PasswordUtils.getPasswordById( user.getId().toString() );
        }
    }

    /**
     * Valid authentication to cb 2.
     *
     * @param uid
     *         the uid
     * @param passwd
     *         the passwd
     * @param userToken
     *         the user token
     */
    private void validAuthenticationToCb2( EntityManager entityManager, String uid, String passwd, UserTokenEntity userToken ) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken( uid, passwd.toCharArray() );
        try {
            if ( isUserAuthenticateInLdap( entityManager, usernamePasswordToken ) ) { // try authentication in ldap first, then cb2
                log.info( "User '{}' authenticated in ldap", uid );

                UserTokenDTO userTokenDTO = tokenManager.validateAndUpdateUserToken( entityManager, userToken );
                if ( ( userTokenDTO != null && !userTokenDTO.isExpired() ) || userToken.getRunningJob() != Boolean.FALSE ) {
                    callToCB2PyScript( uid, passwd, tokenManager.getRefreshTokenFromCurrentSessionUsingUid( uid ) );
                } else {
                    log.error( "Token is not valid anymore for user {}", uid );
                }
            } else {
                log.warn( MessageBundleFactory.getMessage( Messages.USER_COULD_NOT_BE_AUTHENTICATED_IN_LDAP.getKey(), uid ) );
                log.warn( "user {} will be logged out from SIMuSPACE", uid );
                CommonUtils.logOutUserByAuthToken( userToken.getToken() );
            }
        } catch ( Exception e ) {
            log.error( "failed to refresh cb2 login for {}", uid, e );
            throw new SusException( "failed to refresh cb2 login for " + uid );
        }
    }

    /**
     * calls cb2 login script
     *
     * @param uid
     *         the uid
     * @param passwd
     *         the passwd
     */
    private static void callToCB2PyScript( String uid, String passwd, String refreshToken ) {
        var result = PythonUtils.CB2LoginByPython( uid, passwd,
                String.valueOf( Cb2OperationEnum.CB2_LOGIN_OR_KEEP_ALIVE_SESSION.getKey() ), refreshToken );
        if ( result.getErrorStreamString() == null || result.getErrorStreamString().isEmpty() ) {
            log.info( "Session updated for User '" + uid + "'. Script response : " + result.getOutputString() );
        }

    }

    /**
     * Is User Exist In Ldap Directory.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return boolean boolean
     */
    private boolean isUserAuthenticateInLdap( EntityManager entityManager, UsernamePasswordToken token ) {
        AuthenticationInfo info = null;
        try {
            info = ldapAuthRealm.getAuthenticationInfoFromLdap( token );
            userCommonManager.resetFailedLdapAttempts( entityManager, token.getUsername() );
        } catch ( javax.naming.AuthenticationException e ) {
            // in case of incorrect credentials.
            // Case: user was logged in in simuspace but ldap+cb2 password was changed.
            ExceptionLogger.logException( e, getClass() );
            userCommonManager.updateFailedLdapAttemptByOne( entityManager, token.getUsername() );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return info != null && info.getCredentials() != null;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

}
