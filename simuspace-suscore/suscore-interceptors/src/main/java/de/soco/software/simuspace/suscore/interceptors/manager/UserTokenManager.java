package de.soco.software.simuspace.suscore.interceptors.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;
import java.util.UUID;

import org.apache.cxf.message.Message;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.model.JobTokenDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.dao.JobTokenDAO;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;

/**
 * The Interface UserTokenManager manage communication of user token with the DAO layer.
 *
 * @author Zeeshan jamal
 */
public interface UserTokenManager {

    /**
     * Prepare and persist token.
     *
     * @param entityManager
     *         the entity manager
     * @param message
     *         the message
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param expiryTime
     *         the expiry time
     * @param key
     *         the key
     *
     * @return the string
     */
    String prepareAndPersistToken( EntityManager entityManager, Message message, String userId, String uid, long expiryTime, String key );

    /**
     * Prepare and persist job token.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param message
     *         the message
     * @param jobId
     *         the job id
     * @param expiryTime
     *         the expiry time
     * @param key
     *         the key
     * @param authToken
     *         the auth token
     *
     * @return the string
     */
    String prepareAndPersistJobToken( EntityManager entityManager, String userId, String userName, Message message, String jobId,
            String key, String authToken );

    /**
     * Check request.
     *
     * @param token
     *         the token
     * @param ipAddress
     *         the ip address
     * @param key
     *         the key
     *
     * @return UserTokenDTO userTokenDTO
     */
    UserTokenDTO checkSusHttpRequest( EntityManager entityManager, String token, String ipAddress, String key );

    /**
     * Check sus job http request.
     *
     * @param token
     *         the token
     * @param ipAddress
     *         the ip address
     * @param key
     *         the key
     *
     * @return the job token DTO
     */
    JobTokenDTO checkSusJobHttpRequest( EntityManager entityManager, String token, String ipAddress, String key );

    /**
     * Gets the ip address.
     *
     * @param message
     *         the message
     *
     * @return the ip address
     */
    String getIpAddress( Message message );

    /**
     * Gets the expiry time.
     *
     * @param expiry
     *         the expiry
     *
     * @return the expiry time
     */
    long getExpiryTime( int expiry );

    /**
     * A method for expiring a token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return true if the user is logged out
     */
    boolean expireToken( EntityManager entityManager, String token );

    /**
     * Expire token without checking running jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean expireTokenWithoutCheckingRunningJobs( EntityManager entityManager, String token );

    /**
     * Expire token without checking running jobs.
     *
     * @param token
     *         the token
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean expireTokenWithoutCheckingRunningJobs( String token );

    /**
     * Expire job token.
     *
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean expireJobToken( String token );

    /**
     * Expire job token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean expireJobToken( EntityManager entityManager, String token );

    /**
     * Gets the user token DTO by token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user token DTO by token
     */
    UserTokenDTO getUserTokenDTOByToken( EntityManager entityManager, String token );

    /**
     * Gets the user active token list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the user active token list
     */
    List< UserTokenDTO > getUserActiveTokenList( EntityManager entityManager, String userId );

    /**
     * Gets the all active tokens.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all active tokens
     */
    List< UserTokenDTO > getAllActiveTokens( EntityManager entityManager );

    /**
     * Gets the user token DAO.
     *
     * @return the user token DAO
     */
    UserTokenDAO getUserTokenDAO();

    /**
     * Checks if is any job running by user id.
     *
     * @param userId
     *         the user id
     *
     * @return true, if is any job running by user id
     */
    boolean isAnyJobRunningByUserId( EntityManager entityManager, String userId );

    /**
     * Gets the job token DAO.
     *
     * @return the job token DAO
     */
    JobTokenDAO getJobTokenDAO();

    /**
     * Gets the user id by token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user id by token
     */
    UUID getUserIdByToken( EntityManager entityManager, String token );

    /**
     * Gets the user entity by token.
     *
     * @param token
     *         the token
     *
     * @return the user entity by token
     *
     * @apiNote To be used in service calls only
     */
    UserEntity getUserEntityByToken( String token );

    /**
     * Gets the user entity by token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user entity by token
     */
    UserEntity getUserEntityByToken( EntityManager entityManager, String token );

    /**
     * Gets the user id by job token.
     *
     * @param entityManager
     *         the entity manager
     * @param jobToken
     *         the token
     *
     * @return the user id by job token
     */
    String getUserIdByJobToken( EntityManager entityManager, String jobToken );

    /**
     * Gets the all active tokens by filter.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     *
     * @return the all active tokens by filter
     */
    List< UserTokenDTO > getAllActiveTokensByFilter( EntityManager entityManager, FiltersDTO filter );

    /**
     * Gets the user token entity by token.
     *
     * @param token
     *         the token
     *
     * @return the user token entity by token
     *
     * @apiNote To be used in service calls only
     */
    UserTokenEntity getUserTokenEntityByToken( String token );

    /**
     * Gets the user token entity by token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user token entity by token
     */
    UserTokenEntity getUserTokenEntityByToken( EntityManager entityManager, String token );

    /**
     * validate And Update User Token
     *
     * @param entityManager
     *         the entity manager
     * @param userToken
     *         the user token
     *
     * @return the update user token
     */
    UserTokenDTO validateAndUpdateUserToken( EntityManager entityManager, UserTokenEntity userToken );

    /**
     * Validate token time.
     *
     * @param lastRequestTime
     *         the last request time
     *
     * @return true, if successful
     */
    boolean validateTokenTime( Long lastRequestTime );

    /**
     * Update last request in shiro user token dto.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user token dto
     */
    void updateLastRequestInShiro( EntityManager entityManager, String token );

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * Gets the job token entity by token.
     *
     * @param token
     *         the token
     *
     * @return the job token entity by token
     *
     * @apiNote To be used in service calls only
     */
    JobTokenEntity getJobTokenEntityByJobToken( String token );

    /**
     * Gets job token entity by auth token.
     *
     * @param token
     *         the token
     *
     * @return the job token entity by auth token
     *
     * @apiNote To be used in service calls only
     */
    List< JobTokenEntity > getJobTokenEntityByAuthToken( String token );

    /**
     * Gets job token entity by auth token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the job token entity by auth token
     */
    List< JobTokenEntity > getJobTokenEntityByAuthToken( EntityManager entityManager, String token );

    /**
     * Gets active token or inactive token on running job by ip adress.
     *
     * @param IpPc
     *         the ip pc
     *
     * @return the active token or inactive token on running job by ip adress
     *
     * @apiNote To be used in service calls only
     */
    List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByIpAdress( String IpPc );

    List< UserTokenEntity > getActiveTokenOrRunningJobByIpAddressAndUserUid( String IpPc, String userUid );

    /**
     * Update.
     *
     * @param userTokenEntity
     *         the user token entity
     *
     * @return the user token entity
     *
     * @apiNote To be used in service calls only
     */
    UserTokenEntity updateUserToken( UserTokenEntity userTokenEntity );

    /**
     * Update.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     *
     * @return the user token entity
     */
    UserTokenEntity updateUserToken( EntityManager entityManager, UserTokenEntity userTokenEntity );

    /**
     * Gets the active token or inactive token on running job by filter.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the active token or inactive token on running job by filter
     *
     * @apiNote To be used in service calls only
     */
    List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByFilter( FiltersDTO filtersDTO );

    /**
     * Update job token entity.
     *
     * @param jobTokenEntity
     *         the job token entity
     *
     * @apiNote To be used in service calls only
     */
    void updateJobTokenEntity( JobTokenEntity jobTokenEntity );

    /**
     * Gets the all active tokens.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all active tokens
     */
    List< UserTokenEntity > getAllActiveTokenEntities( EntityManager entityManager );

    /**
     * Gets the all active tokens.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all active tokens
     */
    List< UserTokenEntity > getAllActiveOrRunningJobTokens( EntityManager entityManager );

    /**
     * Gets session dto by auth token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the session dto by auth token
     */
    SuSCoreSessionDTO getSessionDTOByAuthToken( EntityManager entityManager, String token );

    /**
     * Gets refresh token from current session using uid.
     *
     * @param uid
     *         the uid
     *
     * @return the refresh token from current session using uid
     */
    String getRefreshTokenFromCurrentSessionUsingUid( String uid );

    /**
     * Update old refresh token in suscore session entity with new one for cb2 login.
     *
     * @param newRefreshToken
     *         the new oidc token
     *
     * @return the string
     */
    void updateOldRefreshTokenInSuscoreSessionEntityUsingUserUid( String newRefreshToken, String uid );

}