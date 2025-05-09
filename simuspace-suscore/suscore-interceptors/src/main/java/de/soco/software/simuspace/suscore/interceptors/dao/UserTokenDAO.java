package de.soco.software.simuspace.suscore.interceptors.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;

/**
 * An interface responsible for saving, updating and reading user tokens.
 *
 * @author Zeeshan jamal
 */
public interface UserTokenDAO {

    /**
     * Save user token.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     *
     * @return the user token Entity
     */
    UserTokenEntity saveUserToken( EntityManager entityManager, UserTokenEntity userTokenEntity );

    /**
     * Update user token entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     */
    void updateUserTokenEntity( EntityManager entityManager, UserTokenEntity userTokenEntity );

    /**
     * Gets user token entity by active token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the user token entity by active token
     */
    UserTokenEntity getUserTokenEntityByActiveToken( EntityManager entityManager, String token );

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
     * A method for getting all active user tokens.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return list of userTokenDTO
     */
    List< UserTokenEntity > getAllActiveTokens( EntityManager entityManager );

    /**
     * a method used to get user token entity list by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user token entity list by user id
     */
    List< UserTokenEntity > getUserTokenEntityListByUserId( EntityManager entityManager, UUID id );

    /**
     * Gets the all user token entity list by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the all user token entity list by user id
     */
    List< UserTokenEntity > getAllUserTokenEntityListByUserId( EntityManager entityManager, UUID id );

    /**
     * Delete user token entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userTokenEntity
     *         the user token entity
     */
    void deleteUserTokenEntity( EntityManager entityManager, UserTokenEntity userTokenEntity );

    /**
     * Checks if is server running any job by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the list
     */
    boolean isAnyJobRunningByUserId( EntityManager entityManager, String userId );

    /**
     * Checks if is any job running by token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return true, if is any job running by token
     */
    boolean isAnyJobRunningByToken( EntityManager entityManager, String token );

    /**
     * Gets the all active tokens by filters.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     *
     * @return the all active tokens by filters
     */
    List< UserTokenEntity > getAllActiveTokensByFilters( EntityManager entityManager, FiltersDTO filter );

    /**
     * Gets the user token entity by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user token entity by id
     */
    UserTokenEntity getUserTokenEntityById( EntityManager entityManager, UUID id );

    /**
     * Gets the active token or inactive token on running job by filter.
     *
     * @param entityManager
     *         the entity manager
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the active token or inactive token on running job by filter
     */
    List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByFilter( EntityManager entityManager, FiltersDTO filtersDTO );

    /**
     * Gets the active token or inactive token on running job by ip adress.
     *
     * @param entityManager
     *         the entity manager
     * @param IpPc
     *         the ip pc
     *
     * @return the active token or inactive token on running job by ip adress
     */
    List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByIpAdress( EntityManager entityManager, String IpPc );

    /**
     * Update.
     *
     * @param entityManager
     *         the entity manager
     * @param e
     *         the e
     *
     * @return the user token entity
     */
    UserTokenEntity update( EntityManager entityManager, UserTokenEntity e );

    /**
     * Gets All tokens that are active or running a job
     *
     * @param entityManager
     *         the entity manager
     *
     * @return list of user token entities
     */
    List< UserTokenEntity > getAllActiveOrRunningJobTokens( EntityManager entityManager );

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

    /**
     * Gets active token or running job by ip address and user uid.
     *
     * @param entityManager
     *         the entity manager
     * @param ipPc
     *         the ip pc
     * @param userUid
     *         the user uid
     *
     * @return the active token or running job by ip address and user uid
     */
    List< UserTokenEntity > getActiveTokenOrRunningJobByIpAddressAndUserUid( EntityManager entityManager, String ipPc, String userUid );

}
