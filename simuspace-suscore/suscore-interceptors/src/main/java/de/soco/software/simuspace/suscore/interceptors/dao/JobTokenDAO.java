package de.soco.software.simuspace.suscore.interceptors.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;

/**
 * An interface responsible for saving, updating and reading user tokens.
 *
 * @author Zeeshan jamal
 */
public interface JobTokenDAO {

    /**
     * Save job token.
     *
     * @param entityManager
     *         the entity manager
     * @param jobTokenEntity
     *         the job token entity
     *
     * @return the job token entity
     */
    JobTokenEntity saveJobToken( EntityManager entityManager, JobTokenEntity jobTokenEntity );

    /**
     * Gets job token entity by job token.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     *
     * @return the job token entity by job token
     */
    JobTokenEntity getJobTokenEntityByJobToken( EntityManager entityManager, String token );

    /**
     * Update job token entity.
     *
     * @param entityManager
     *         the entity manager
     * @param jobTokenEntity
     *         the job token entity
     */
    void updateJobTokenEntity( EntityManager entityManager, JobTokenEntity jobTokenEntity );

    /**
     * Gets job token entity by auth token.
     *
     * @param entityManager
     *         the entity manager
     * @param authtoken
     *         the authtoken
     *
     * @return the job token entity by auth token
     */
    List< JobTokenEntity > getJobTokenEntityByAuthToken( EntityManager entityManager, String authtoken );

    /**
     * Gets the job token entity by auth token expire and non expire.
     *
     * @param entityManager
     *         the entity manager
     * @param authtoken
     *         the authtoken
     *
     * @return the job token entity by auth token expire and non expire
     */
    List< JobTokenEntity > getJobTokenEntityByAuthTokenExpireAndNonExpire( EntityManager entityManager, String authtoken );

}
