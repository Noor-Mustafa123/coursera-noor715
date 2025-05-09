package de.soco.software.simuspace.suscore.authentication.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.authentication.SuSshiroAuthentication;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.model.VerificationDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.user.manager.UserManager;

/**
 * An implementation of AuthManager class holding methods for authentication.
 *
 * @author Zeeshan jamal
 */
@Log4j2
public class AuthManagerImpl implements AuthManager {

    /**
     * userTokenManager reference.
     */
    private UserTokenManager userTokenManager;

    /**
     * userManager reference.
     */
    private UserManager userManager;

    /**
     * The su sshiro authentication.
     */
    private SuSshiroAuthentication suSshiroAuthentication;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareToken( UserDTO user, Message currentMessage ) {
        String token;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            long expiryTime = 0;
            try {
                int expiry = Integer.parseInt( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.USER_SESSION_EXPIRY ) );
                expiryTime = userTokenManager.getExpiryTime( expiry );
            } catch ( Exception ex ) {
                expiryTime = userTokenManager.getExpiryTime( ConstantsFileProperties.USER_SESSION_EXPIRY_TIME );
                log.error( ex.getMessage(), ex );
            }

            token = userTokenManager.prepareAndPersistToken( entityManager, PhaseInterceptorChain.getCurrentMessage(), user.getId(),
                    user.getUserUid(), expiryTime, ConstantsString.EMPTY_STRING );
        } finally {
            entityManager.close();
        }
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prepareJobToken( String userId, String userName, String jobId, Message currentMessage, String authToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String token;
        try {
            // runing job flag on
            UserTokenEntity userTokenE = userTokenManager.getUserTokenDAO().getUserTokenEntityByActiveToken( entityManager, authToken );
            userTokenE.setRunningJob( true );
            userTokenManager.getUserTokenDAO().update( entityManager, userTokenE );

            token = userTokenManager.prepareAndPersistJobToken( entityManager, userId, userName, currentMessage, jobId,
                    ConstantsString.EMPTY_STRING, authToken );
        } finally {
            entityManager.close();
        }
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logout( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean expired;
        try {
            expired = userTokenManager.expireToken( entityManager, token );
        } finally {
            entityManager.close();
        }
        return expired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logoutWithoutCheckingRunningJobs( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean isExpired;
        try {
            isExpired = userTokenManager.expireTokenWithoutCheckingRunningJobs( entityManager, token );
        } finally {
            entityManager.close();
        }
        return isExpired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUserIdByToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UUID userId;
        try {
            userId = userTokenManager.getUserIdByToken( entityManager, token );
        } finally {
            entityManager.close();
        }
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserEntitydByToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserEntity userEntity;
        try {
            userEntity = userTokenManager.getUserEntityByToken( entityManager, token );
        } finally {
            entityManager.close();
        }
        return userEntity;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public UserEntity getUserEntityByJobToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String userId;
        UserEntity userEntity;
        try {
            userId = userTokenManager.getUserIdByJobToken( entityManager, token );
            userEntity = userManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        } finally {
            entityManager.close();
        }
        return userEntity;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.authentication.manager.AuthManager#
     * expireJobToken(java.lang.String)
     */
    @Override
    public boolean expireJobToken( EntityManager entityManager, String token ) {
        return userTokenManager.expireJobToken( entityManager, token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenDTO > getAllActiveTokens() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< UserTokenDTO > allActiveTokens;
        try {
            allActiveTokens = userTokenManager.getAllActiveTokens( entityManager );
        } finally {
            entityManager.close();
        }
        return allActiveTokens;
    }

    /**
     * gets userTokenManager.
     *
     * @return userTokenManager
     */
    public UserTokenManager getUserTokenManager() {
        return userTokenManager;
    }

    /**
     * sets userTokenManager.
     *
     * @param userTokenManager
     *         the new user token manager
     */
    public void setUserTokenManager( UserTokenManager userTokenManager ) {
        this.userTokenManager = userTokenManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.authentication.manager.AuthManager#
     * getSuSshiroAuthentication()
     */
    @Override
    public SuSshiroAuthentication getSuSshiroAuthentication() {
        return suSshiroAuthentication;
    }

    /**
     * Sets the su sshiro authentication.
     *
     * @param suSshiroAuthentication
     *         the new su sshiro authentication
     */
    public void setSuSshiroAuthentication( SuSshiroAuthentication suSshiroAuthentication ) {
        this.suSshiroAuthentication = suSshiroAuthentication;
    }

    /**
     * gets userManager.
     *
     * @return userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * sets userManager.
     *
     * @param userManager
     *         the new user manager
     */
    public void setUserManager( UserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object verifyUserByHeaderToken( String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        UserDTO user;
        try {
            user = userManager.getUserById( entityManager, userIdFromGeneralHeader, UUID.fromString( userIdFromGeneralHeader ) );
            user.setPassword( "" );
        } finally {
            entityManager.close();
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserAuthenticateInLdap( String uid, String password ) {
        return userManager.isUserAuthenticateInLdap( uid, password );
    }

    @Override
    public VerificationDTO getUserByUserUid( String userUid ) {
        var userDto = userManager.getUserByUserUidForVerification( userUid );
        if ( userDto == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_NOT_FOUND_WITH_ID.getKey(), userUid ) );
        }
        return new VerificationDTO( userDto.getName(), userDto.getProfilePhoto() != null ? userDto.getProfilePhoto().getUrl() : null,
                userDto.getSusUserDirectoryDTO() != null ? userDto.getSusUserDirectoryDTO().getType() : null );
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
