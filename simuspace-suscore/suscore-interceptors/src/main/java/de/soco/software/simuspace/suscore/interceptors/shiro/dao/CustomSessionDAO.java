package de.soco.software.simuspace.suscore.interceptors.shiro.dao;

import javax.persistence.EntityManager;

import java.io.Serializable;

import org.apache.shiro.session.mgt.eis.SessionDAO;

import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;

/**
 * The Interface CustomSessionDAO.
 */
public interface CustomSessionDAO extends SessionDAO {

    /**
     * Gets the result by shiro id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the result by shiro id
     */
    SuscoreSession getResultByShiroId( EntityManager entityManager, Serializable id );

    /**
     * Gets the result by token.
     *
     * @param token
     *         the token
     *
     * @return the result by token
     */
    SuscoreSession getResultByToken( EntityManager entityManager, String token );

    /**
     * Update.
     *
     * @param entityManager
     *         the entity manager
     * @param suscoreSession
     *         the suscore session
     *
     * @return the suscore session
     */
    SuscoreSession update( EntityManager entityManager, SuscoreSession suscoreSession );

    /**
     * Gets result by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userid
     *         the userid
     *
     * @return the result by user id
     */
    SuscoreSession getResultByUserId( EntityManager entityManager, String userid );

}
