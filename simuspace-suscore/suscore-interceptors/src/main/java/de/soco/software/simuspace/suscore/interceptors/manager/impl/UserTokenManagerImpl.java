package de.soco.software.simuspace.suscore.interceptors.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.Headers;
import org.apache.shiro.subject.Subject;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMessageHeaders;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.UIThemeEnums;
import de.soco.software.simuspace.suscore.common.model.JobTokenDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserDetail;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CompressionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.dao.JobTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.dao.PropertyAccesser;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.interceptors.shiro.dao.CustomSessionDAO;
import de.soco.software.simuspace.suscore.interceptors.utils.TokenUtils;

/**
 * An implementation class of userTokenManager that holds the methods for token business logic and communication with DAO layer.
 *
 * @author Zeeshan jamal
 * @since 1.0
 */
@Log4j2
public class UserTokenManagerImpl implements UserTokenManager {

    /**
     * The user token dao.
     */
    private UserTokenDAO userTokenDAO;

    /**
     * The job token DAO.
     */
    private JobTokenDAO jobTokenDAO;

    /**
     * The property accesser.
     */
    private PropertyAccesser propertyAccesser;

    private CustomSessionDAO customSessionDAO;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant UNKNOWN_IP.
     */
    private static final String UNKNOWN_IP = "unknown";

    /**
     * The constant MILLIS_IN_MINUTE.
     */
    private static final long MILLIS_IN_MINUTE = 60000L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareAndPersistToken( EntityManager entityManager, Message message, final String userId, String uid, long expiryTime,
            String key ) {
        UserTokenDTO userTokenDTO;
        String browserAgent = BundleUtils.getBrowserAgent( message );
        String ipAddress = getIpAddress( message );
        Date currentTime = new Date();
        String token = TokenUtils.generateToken( userId, currentTime, ipAddress, key );
        if ( StringUtils.isNotBlank( ipAddress ) ) {
            userTokenDTO = new UserTokenDTO();
            userTokenDTO.setUserId( userId );
            userTokenDTO.setToken( token );
            userTokenDTO.setIpAddress( ipAddress );
            userTokenDTO.setBrowserAgent( browserAgent );
            userTokenDTO.setExpiryTime( expiryTime );
            userTokenDTO.setExpired( false );
            userTokenDTO.setKey( key );
            userTokenDTO.setLastRequestTime( currentTime );
            userTokenDTO.setCreatedOn( currentTime );

            final UserTokenEntity userTokenEntity = prepareUserTokenEntityFromUserTokenDTO( userTokenDTO );

            if ( StringUtils.isNotBlank( userId ) && ValidationUtils.validateUUIDString( userId ) ) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId( UUID.fromString( userId ) );
                userEntity.setUserUid( uid );
                userTokenEntity.setUserEntity( userEntity );
            }
            final UserTokenEntity savedTokenEntity = userTokenDAO.saveUserToken( entityManager, userTokenEntity );
            token = savedTokenEntity.getToken();
        }
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareAndPersistJobToken( EntityManager entityManager, String userId, String userName, Message message, String jobId,
            String key, String authToken ) {
        JobTokenDTO jobTokenDTO;
        String browserAgent = BundleUtils.getBrowserAgent( message );
        String ipAddress = getIpAddress( message );
        Date currentTime = new Date();
        String token = TokenUtils.generateToken( jobId, currentTime, ipAddress, key );
        if ( StringUtils.isNotBlank( ipAddress ) ) {
            jobTokenDTO = new JobTokenDTO( jobId, token, ipAddress, browserAgent, false, key );
            jobTokenDTO.setCreatedOn( currentTime );
            jobTokenDTO.setUserId( userId );
            jobTokenDTO.setUserName( userName );
            jobTokenDTO.setAuthToken( authToken );
            final JobTokenEntity savedTokenEntity = jobTokenDAO.saveJobToken( entityManager,
                    prepareJobTokenEntityFromJobTokenDTO( jobTokenDTO ) );
            token = savedTokenEntity.getToken();
        }
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenDTO checkSusHttpRequest( EntityManager entityManager, final String token, final String ipAddress, final String key ) {
        UserTokenEntity userTokenEntity = null;
        UserTokenDTO userTokenDTO = null;
        if ( null != token && !token.isEmpty() ) {
            userTokenEntity = userTokenDAO.getUserTokenEntityByActiveToken( entityManager, token );
            if ( userTokenEntity != null ) {
                userTokenDTO = prepareUserTokenDTOFromUserTokenEntity( userTokenEntity );
                if ( !userTokenEntity.getIpAddress().equals( ipAddress ) && !PropertiesManager.getAllowedOrigins().contains( "*" )
                        && PropertiesManager.getAllowedOrigins().stream().noneMatch( allowed -> allowed.contains( ipAddress ) ) ) {
                    return null;
                }
            }
        }

        if ( null != userTokenDTO ) {
            if ( validateTokenTime( userTokenDTO.getLastRequestTime().getTime() ) ) {
                // updating last request time in sus token
                updateLastReqTime( entityManager, userTokenEntity );
                updateLastRequestInShiro( entityManager, token );
                return userTokenDTO;
            } else {
                userTokenDTO.setExpired( true );
                userTokenEntity.setExpired( true );
                userTokenEntity.setLastRequestTime( userTokenDTO.getLastRequestTime() );
                userTokenDAO.updateUserTokenEntity( entityManager, userTokenEntity );
                return null;

            }
        }
        return null;
    }

    /**
     * Gets the job token DTO by token.
     *
     * @param token
     *         the token
     *
     * @return the job token DTO by token
     */
    public JobTokenDTO getJobTokenDTOByToken( EntityManager entityManager, final String token ) {
        JobTokenDTO jobTokenDTO = null;
        if ( null != token && !token.isEmpty() ) {
            final JobTokenEntity jobTokenEntity = jobTokenDAO.getJobTokenEntityByJobToken( entityManager, token );
            if ( jobTokenEntity != null ) {
                jobTokenDTO = prepareJobTokenDTOFromUserTokenEntity( jobTokenEntity );
            }
        }
        return jobTokenDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastRequestInShiro( EntityManager entityManager, final String token ) {
        boolean sessionTouched = touchSessionToUpdateLastRequestTime( customSessionDAO.getResultByToken( entityManager, token ) );
        if ( !sessionTouched ) {
            // fail safe approach to expire user token if shiro session could not be touched.
            expireToken( entityManager, token );
        }
    }

    /**
     * Touch session to update last request time.
     *
     * @param suscoreSession
     *         the suscore session
     */
    private boolean touchSessionToUpdateLastRequestTime( SuscoreSession suscoreSession ) {
        if ( suscoreSession != null ) {
            var shiroSession = CompressionUtils.deserialize( suscoreSession.getSession() );
            if ( log.isDebugEnabled() ) {
                log.debug( "touching shiroSession {}", shiroSession );
            }
            Subject subject = new Subject.Builder().sessionId( CompressionUtils.deserialize( suscoreSession.getSession() ).getId() )
                    .buildSubject();
            if ( subject != null ) {
                var sessionFromSubject = subject.getSession( false );
                if ( sessionFromSubject != null ) {
                    shiroSession.touch();
                    sessionFromSubject.touch();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobTokenDTO checkSusJobHttpRequest( EntityManager entityManager, final String token, final String ipAddress, final String key ) {
        return getJobTokenDTOByToken( entityManager, token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIpAddress( Message message ) {
        Map< String, List< String > > headers = Headers.getSetProtocolHeaders( message );
        String ip = null;
        if ( null != headers ) {
            List< String > ipList = headers.get( ConstantsMessageHeaders.X_REAL_IP );
            ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            if ( ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase( ip ) ) {
                ipList = headers.get( ConstantsMessageHeaders.REQUEST_FORWARDED_FOR );
                ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
            if ( ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase( ip ) ) {
                ipList = headers.get( ConstantsMessageHeaders.PROXY_CLIENT_IP );
                ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
            if ( ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase( ip ) ) {
                ipList = headers.get( ConstantsMessageHeaders.WL_PROXY_CLIENT_IP );
                ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
            if ( ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase( ip ) ) {
                ipList = headers.get( ConstantsMessageHeaders.HTTP_CLIENT_IP );
                ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
            if ( ip == null || ip.isEmpty() || UNKNOWN_IP.equalsIgnoreCase( ip ) ) {
                ipList = headers.get( ConstantsMessageHeaders.HTTP_REQUEST_FORWARDED_FOR );
                ip = ipList == null ? ConstantsString.EMPTY_STRING : ipList.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            }
            if ( StringUtils.isBlank( ip ) ) {
                HttpServletRequest request = ( HttpServletRequest ) message.get( AbstractHTTPDestination.HTTP_REQUEST );
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getExpiryTime( int expiry ) {
        return System.currentTimeMillis() + MILLIS_IN_MINUTE * expiry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean expireToken( EntityManager entityManager, String token ) {
        return expireTokenWithoutCheckingRunningJobs( entityManager, token );
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager#
     * expireTokenWithoutCheckingRunningJobs(java.lang.String)
     */
    @Override
    public boolean expireTokenWithoutCheckingRunningJobs( EntityManager entityManager, String token ) {
        UserTokenEntity userTokenEntity = userTokenDAO.getUserTokenEntityByActiveToken( entityManager, token );
        if ( userTokenEntity != null ) {
            userTokenEntity.setExpired( true );
            userTokenDAO.updateUserTokenEntity( entityManager, userTokenEntity );
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager#
     * expireTokenWithoutCheckingRunningJobs(java.lang.String)
     */
    @Override
    public boolean expireTokenWithoutCheckingRunningJobs( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean expired;
        try {
            expired = expireTokenWithoutCheckingRunningJobs( entityManager, token );
        } finally {
            entityManager.close();
        }
        return expired;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUserIdByToken( EntityManager entityManager, String token ) {
        UserTokenEntity userTokenEntity = userTokenDAO.getUserTokenEntityByActiveToken( entityManager, token );
        if ( userTokenEntity != null ) {
            return userTokenEntity.getUserEntity().getId();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntityByToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserEntity userEntity;
        try {
            userEntity = getUserEntityByToken( entityManager, token );
        } finally {
            entityManager.close();
        }
        return userEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntityByToken( EntityManager entityManager, String token ) {
        UserTokenEntity userTokenEntity = userTokenDAO.getUserTokenEntityByToken( entityManager, token );
        if ( userTokenEntity != null ) {
            return userTokenEntity.getUserEntity();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserIdByJobToken( EntityManager entityManager, String jobToken ) {
        JobTokenEntity jobTokenEntity = jobTokenDAO.getJobTokenEntityByJobToken( entityManager, jobToken );

        if ( jobTokenEntity != null ) {
            return jobTokenEntity.getUserId();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager#
     * expireJobToken(java.lang.String)
     */
    @Override
    public boolean expireJobToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isExpired;
        try {
            isExpired = expireJobToken( entityManager, token );
        } finally {
            entityManager.close();
        }
        return isExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean expireJobToken( EntityManager entityManager, String token ) {
        JobTokenEntity jobTokenEntity = jobTokenDAO.getJobTokenEntityByJobToken( entityManager, token );
        if ( jobTokenEntity != null ) {
            jobTokenEntity.setExpired( true );
            jobTokenDAO.updateJobTokenEntity( entityManager, jobTokenEntity );
            return true;
        }
        return false;
    }

    @Override
    public List< UserTokenDTO > getUserActiveTokenList( EntityManager entityManager, String userId ) {
        List< UserTokenEntity > userTokenEntityListByUserId = userTokenDAO.getUserTokenEntityListByUserId( entityManager,
                UUID.fromString( userId ) );
        List< UserTokenDTO > listToReturn = new ArrayList<>();
        for ( UserTokenEntity userTokenEntity : userTokenEntityListByUserId ) {
            listToReturn.add( prepareUserTokenDTOFromUserTokenEntity( userTokenEntity ) );
        }
        return listToReturn;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager#
     * isAnyJobRunningByUserId(java.lang.String)
     */
    @Override
    public boolean isAnyJobRunningByUserId( EntityManager entityManager, String userId ) {
        return userTokenDAO.isAnyJobRunningByUserId( entityManager, userId );
    }

    /**
     * Would be called on bean destruction to expire all active tokens.
     */
    public void cleanActiveTokens() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            cleanActiveTokens( entityManager );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Would be called on bean destruction to expire all active tokens.
     */
    public void cleanActiveTokens( EntityManager entityManager ) {
        List< UserTokenEntity > activeTokens = userTokenDAO.getAllActiveTokens( entityManager );
        for ( UserTokenEntity userTokenEntity : activeTokens ) {
            if ( !userTokenDAO.isAnyJobRunningByToken( entityManager, userTokenEntity.getToken() ) ) {
                userTokenEntity.setExpired( true );
                userTokenDAO.updateUserTokenEntity( entityManager, userTokenEntity );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenDTO > getAllActiveTokens( EntityManager entityManager ) {
        List< UserTokenEntity > userTokenEntityList = userTokenDAO.getAllActiveTokens( entityManager );
        List< UserTokenDTO > listToReturn = new ArrayList<>();
        for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
            listToReturn.add( prepareUserTokenDTOFromUserTokenEntity( userTokenEntity ) );
        }
        return listToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenDTO > getAllActiveTokensByFilter( EntityManager entityManager, FiltersDTO filter ) {
        List< UserTokenEntity > userTokenEntityList = userTokenDAO.getAllActiveTokensByFilters( entityManager, filter );
        List< UserTokenDTO > listToReturn = new ArrayList<>();
        for ( UserTokenEntity userTokenEntity : userTokenEntityList ) {
            listToReturn.add( prepareUserTokenDTOFromUserTokenEntity( userTokenEntity ) );
        }
        return listToReturn;
    }

    /**
     * A method for preparing user token entity from user token DTO
     *
     * @param userTokenDTO
     *         the userTokenDTO
     *
     * @return UserTokenEntity
     */
    private UserTokenEntity prepareUserTokenEntityFromUserTokenDTO( UserTokenDTO userTokenDTO ) {
        UserTokenEntity userTokenEntity = new UserTokenEntity();
        userTokenEntity.setId( UUID.randomUUID() );
        userTokenEntity.setToken( userTokenDTO.getToken() );
        userTokenEntity.setIpAddress( userTokenDTO.getIpAddress() );
        userTokenEntity.setBrowserAgent( userTokenDTO.getBrowserAgent() );
        userTokenEntity.setExpired( userTokenDTO.isExpired() );
        userTokenEntity.setLastRequestTime( userTokenDTO.getLastRequestTime() );
        userTokenEntity.setCreatedOn( userTokenDTO.getCreatedOn() );
        userTokenEntity.setExpiryTime( new Date( userTokenDTO.getExpiryTime() ) );
        return userTokenEntity;
    }

    /**
     * Prepare job token entity from job token DTO.
     *
     * @param jobTokenDTO
     *         the job token DTO
     *
     * @return the job token entity
     */
    private JobTokenEntity prepareJobTokenEntityFromJobTokenDTO( JobTokenDTO jobTokenDTO ) {
        JobTokenEntity jobTokenEntity = new JobTokenEntity();
        jobTokenEntity.setId( UUID.randomUUID() );
        jobTokenEntity.setToken( jobTokenDTO.getToken() );
        jobTokenEntity.setIpAddress( jobTokenDTO.getIpAddress() );
        jobTokenEntity.setAuthToken( jobTokenDTO.getAuthToken() );
        jobTokenEntity.setBrowserAgent( jobTokenDTO.getBrowserAgent() );
        jobTokenEntity.setExpired( jobTokenDTO.getExpired() );
        jobTokenEntity.setCreatedOn( jobTokenDTO.getCreatedOn() );
        jobTokenEntity.setUserId( jobTokenDTO.getUserId() );
        jobTokenEntity.setUserName( jobTokenDTO.getUserName() );
        return jobTokenEntity;
    }

    /**
     * A method for preparing user token dto from user token entity.
     *
     * @param userTokenEntity
     *         the user token entity
     *
     * @return UserTokenDTO
     */
    private UserTokenDTO prepareUserTokenDTOFromUserTokenEntity( UserTokenEntity userTokenEntity ) {
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setId( userTokenEntity.getId().toString() );
        userTokenDTO.setToken( userTokenEntity.getToken() );
        userTokenDTO.setIpAddress( userTokenEntity.getIpAddress() );
        userTokenDTO.setBrowserAgent( userTokenEntity.getBrowserAgent() );
        userTokenDTO.setExpiryTime( userTokenEntity.getExpiryTime().getTime() );
        userTokenDTO.setLastRequestTime( userTokenEntity.getLastRequestTime() );
        userTokenDTO.setExpired( userTokenEntity.isExpired() );
        userTokenDTO.setUserId( userTokenEntity.getUserEntity() != null ? userTokenEntity.getUserEntity().getId().toString()
                : ConstantsInteger.INTEGER_VALUE_ZERO + ConstantsString.EMPTY_STRING );
        userTokenDTO.setUserName( userTokenEntity.getUserEntity().getUserUid() );
        return userTokenDTO;
    }

    /**
     * Prepare job token DTO from user token entity.
     *
     * @param jobTokenEntity
     *         the job token entity
     *
     * @return the job token DTO
     */
    private JobTokenDTO prepareJobTokenDTOFromUserTokenEntity( JobTokenEntity jobTokenEntity ) {
        JobTokenDTO jobTokenDTO = new JobTokenDTO();
        jobTokenDTO.setId( jobTokenEntity.getId().toString() );
        jobTokenDTO.setToken( jobTokenEntity.getToken() );
        jobTokenDTO.setIpAddress( jobTokenEntity.getIpAddress() );
        jobTokenDTO.setBrowserAgent( jobTokenEntity.getBrowserAgent() );
        jobTokenDTO.setExpired( jobTokenEntity.isExpired() );
        jobTokenDTO.setUserId( jobTokenEntity.getUserId() );
        jobTokenDTO.setUserName( jobTokenEntity.getUserName() );
        jobTokenDTO.setAuthToken( jobTokenEntity.getAuthToken() );
        return jobTokenDTO;
    }

    /**
     * A method for validating token time.
     *
     * @param lastRequestTime
     *         the last request time
     *
     * @return true, if successful
     */
    @Override
    public boolean validateTokenTime( Long lastRequestTime ) {
        Long expiryTimeLimit = MILLIS_IN_MINUTE * Integer.parseInt( PropertiesManager.getSessionExpiry() );
        Long expiryTime = lastRequestTime + expiryTimeLimit;
        return System.currentTimeMillis() < expiryTime;
    }

    @Override
    public UserTokenDTO getUserTokenDTOByToken( EntityManager entityManager, String token ) {
        if ( null != token && !token.isEmpty() ) {
            final UserTokenEntity userTokenEntity = userTokenDAO.getUserTokenEntityByActiveToken( entityManager, token );
            if ( userTokenEntity != null ) {
                return prepareUserTokenDTOFromUserTokenEntity( userTokenEntity );
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity getUserTokenEntityByToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getUserTokenEntityByToken( entityManager, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity getUserTokenEntityByToken( EntityManager entityManager, String token ) {
        if ( null != token && !token.isEmpty() ) {
            return userTokenDAO.getUserTokenEntityByToken( entityManager, token );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public UserTokenDTO validateAndUpdateUserToken( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        SuscoreSession session = customSessionDAO.getResultByToken( entityManager, userTokenEntity.getToken() );
        if ( session == null ) {
            log.warn( "Shiro Session does not exist anymore against user token {}. Verify session expiry time and cb2 session renewal time",
                    userTokenEntity.getToken() );
            //if Shiro session does not exist than token should also be expired
            userTokenEntity.setExpired( true );
            return prepareUserTokenDTOFromUserTokenEntity( userTokenDAO.update( entityManager, userTokenEntity ) );

        }
        if ( !userTokenEntity.isExpired() ) {
            CompressionUtils.deserialize( session.getSession() ).touch();
            if ( validateTokenTime( userTokenEntity.getLastRequestTime().getTime() ) ) {
                // update token
                Date currentTime = new Date();
                userTokenEntity.setLastRequestTime( currentTime );
                userTokenEntity.setExpiryTime(
                        DateUtils.addMinutes( currentTime, Integer.parseInt( PropertiesManager.getSessionExpiry() ) ) );
                userTokenEntity.setExpired( false );
            } else {
                userTokenEntity.setExpired( true );
            }
            userTokenEntity = userTokenDAO.update( entityManager, userTokenEntity );
        }
        return prepareUserTokenDTOFromUserTokenEntity( userTokenEntity );
    }

    /**
     * Update last request time.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     */
    private void updateLastReqTime( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        Date currentTime = new Date();
        userTokenEntity.setLastRequestTime( currentTime );
        userTokenEntity.setExpiryTime( DateUtils.addMinutes( currentTime, Integer.valueOf( PropertiesManager.getSessionExpiry() ) ) );
        userTokenEntity.setExpired( false );
        userTokenDAO.updateUserTokenEntity( entityManager, userTokenEntity );
    }

    /**
     * Gets the property accesser.
     *
     * @return the property accesser
     */
    public PropertyAccesser getPropertyAccesser() {
        return propertyAccesser;
    }

    /**
     * Sets the property accesser.
     *
     * @param propertyAccesser
     *         the new property accesser
     */
    public void setPropertyAccesser( PropertyAccesser propertyAccesser ) {
        this.propertyAccesser = propertyAccesser;
    }

    /**
     * Gets the user token DAO.
     *
     * @return the user token DAO
     */
    @Override
    public UserTokenDAO getUserTokenDAO() {
        return userTokenDAO;
    }

    /**
     * sets userTokenDAO.
     *
     * @param userTokenDAO
     *         userTokenDAO
     */
    public void setUserTokenDAO( UserTokenDAO userTokenDAO ) {
        this.userTokenDAO = userTokenDAO;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager#
     * getJobTokenDAO()
     */
    @Override
    public JobTokenDAO getJobTokenDAO() {
        return jobTokenDAO;
    }

    /**
     * Sets the job token DAO.
     *
     * @param jobTokenDAO
     *         the new job token DAO
     */
    public void setJobTokenDAO( JobTokenDAO jobTokenDAO ) {
        this.jobTokenDAO = jobTokenDAO;
    }

    public CustomSessionDAO getCustomSessionDAO() {
        return customSessionDAO;
    }

    public void setCustomSessionDAO( CustomSessionDAO customSessionDAO ) {
        this.customSessionDAO = customSessionDAO;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public JobTokenEntity getJobTokenEntityByJobToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return jobTokenDAO.getJobTokenEntityByJobToken( entityManager, token );
        } finally {
            entityManager.close();
        }

    }

    @Override
    public List< JobTokenEntity > getJobTokenEntityByAuthToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getJobTokenEntityByAuthToken( entityManager, token );
        } finally {
            entityManager.close();
        }

    }

    @Override
    public List< JobTokenEntity > getJobTokenEntityByAuthToken( EntityManager entityManager, String token ) {
        return jobTokenDAO.getJobTokenEntityByAuthToken( entityManager, token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByIpAdress( String IpPc ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return userTokenDAO.getActiveTokenOrInactiveTokenOnRunningJobByIpAdress( entityManager, IpPc );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrRunningJobByIpAddressAndUserUid( String IpPc, String userUid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return userTokenDAO.getActiveTokenOrRunningJobByIpAddressAndUserUid( entityManager, IpPc, userUid );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity updateUserToken( UserTokenEntity userTokenEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return updateUserToken( entityManager, userTokenEntity );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserTokenEntity updateUserToken( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        return userTokenDAO.update( entityManager, userTokenEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByFilter( FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return userTokenDAO.getActiveTokenOrInactiveTokenOnRunningJobByFilter( entityManager, filtersDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateJobTokenEntity( JobTokenEntity jobTokenEntity ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            jobTokenDAO.updateJobTokenEntity( entityManager, jobTokenEntity );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< UserTokenEntity > getAllActiveTokenEntities( EntityManager entityManager ) {
        return userTokenDAO.getAllActiveTokens( entityManager );
    }

    @Override
    public List< UserTokenEntity > getAllActiveOrRunningJobTokens( EntityManager entityManager ) {
        return userTokenDAO.getAllActiveOrRunningJobTokens( entityManager );
    }

    @Override
    public SuSCoreSessionDTO getSessionDTOByAuthToken( EntityManager entityManager, String token ) {
        var sessionEntity = customSessionDAO.getResultByToken( entityManager, token );
        return prepareSessionDTO( entityManager, sessionEntity, token );
    }

    /**
     * Prepare session DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param sessionEntity
     *         the session entity
     * @param token
     *         the token
     *
     * @return the su S core session DTO
     */
    private SuSCoreSessionDTO prepareSessionDTO( EntityManager entityManager, SuscoreSession sessionEntity, String token ) {
        if ( sessionEntity == null ) {
            return null;
        }
        SuSCoreSessionDTO sessionDTO = new SuSCoreSessionDTO();
        // not preparing shiro and oAuth session variables to avoid unnecessary overhead. Might be needed in future
        sessionDTO.setRefreshToken( sessionEntity.getRefreshToken() );
        sessionDTO.setOauthOriginalToken( sessionEntity.getOauthOriginalToken() );
        sessionDTO.setWenLogin( sessionEntity.isWenLogin() );
        sessionDTO.setToken( token );
        sessionDTO.setUser( prepareUserDTOFromEntity( getUserEntityByToken( entityManager, token ) ) );
        return sessionDTO;
    }

    /**
     * Prepare User DTO from entity.
     *
     * @param userEntity
     *         the user entity
     *
     * @return the user DTO
     */
    private UserDTO prepareUserDTOFromEntity( UserEntity userEntity ) {
        var user = new UserDTO();
        user.setId( userEntity.getId().toString() );
        user.setUserUid( userEntity.getUserUid() );
        user.setStatus( userEntity.isStatus() ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
        user.setRestricted( Boolean.TRUE.equals( userEntity.isRestricted() ) ? ConstantsStatus.YES : ConstantsStatus.NO );
        user.setFirstName( userEntity.getFirstName() );
        user.setSurName( userEntity.getSurName() != null ? userEntity.getSurName() : "" );
        user.setName( user.getName() );
        user.setDescription( userEntity.getDescription() );
        user.setChangable( userEntity.isChangeable() );
        user.setPassword( userEntity.getPassword() );
        user.setLocationPreferenceSelectionId( userEntity.getLocationPreferenceSelectionId() );
        List< UserDetailEntity > userDetailList = new ArrayList<>();
        UserDetailEntity detail = null;
        if ( CollectionUtils.isNotEmpty( userEntity.getUserDetails() ) ) {
            detail = userEntity.getUserDetails().stream().findFirst().orElse( null );
        }

        if ( detail != null ) {
            userDetailList.add( detail );
        }
        user.setUserDetails( prepareUserDetailsFromUser( userDetailList ) );
        user.setTheme( userEntity.getTheme() == null ? UIThemeEnums.SYSTEM.getName() : userEntity.getTheme() );
        return user;
    }

    /**
     * Prepare user details from user.
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

    @Override
    public String getRefreshTokenFromCurrentSessionUsingUid( String uid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuscoreSession suscoreSession = customSessionDAO.getResultByUserId( entityManager, uid );
            return suscoreSession.getRefreshToken();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void updateOldRefreshTokenInSuscoreSessionEntityUsingUserUid( String newRefreshToken, String uid ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuscoreSession suscoreSession = customSessionDAO.getResultByUserId( entityManager, uid );
            suscoreSession.setRefreshToken( newRefreshToken );
            customSessionDAO.update( entityManager, suscoreSession );
        } finally {
            entityManager.close();
        }
    }

}
