package de.soco.software.simuspace.suscore.authentication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.interceptors.shiro.dao.CustomSessionDAO;

/**
 * A class for loading shiro configuration from shiro.ini from resources folder
 *
 * @author Zeeshan jamal
 */
public class SuSshiroAuthentication extends DefaultSecurityManager {

    /**
     * The Session manager.
     */
    private DefaultSessionManager sessionManager;

    /**
     * The Custom session dao.
     */
    private CustomSessionDAO customSessionDAO;

    /**
     * The Entity manager factory reference.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * A method for loading shiro configuration programmatically on bundle activation
     */
    public void loadShiroConfiguration() {
        SecurityUtils.setSecurityManager( this );
        sessionManager = ( ( DefaultSessionManager ) ( this ).getSessionManager() );
        int expiry = Integer.parseInt( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.USER_SESSION_EXPIRY ) );
        sessionManager.setGlobalSessionTimeout( expiry * 60000L );
        sessionManager.setSessionDAO( customSessionDAO );
    }

    /**
     * Gets the result by shiro id.
     *
     * @param id
     *         the id
     *
     * @return the result by shiro id
     */
    public SuscoreSession getResultByShiroId( Serializable id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return customSessionDAO.getResultByShiroId( entityManager, id );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Update.
     *
     * @param suscoreSession
     *         the suscoreSession
     *
     * @return the suscore session
     */
    public SuscoreSession update( SuscoreSession suscoreSession ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return customSessionDAO.update( entityManager, suscoreSession );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets result by token.
     *
     * @param token
     *         the token
     *
     * @return the result by token
     */
    public SuscoreSession getResultByToken( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return customSessionDAO.getResultByToken( entityManager, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets active sessions.
     *
     * @return the active sessions
     */
    public Collection< Session > getActiveSessions() {
        return customSessionDAO.getActiveSessions();
    }

    /**
     * Sets custom session dao.
     *
     * @param customSessionDAO
     *         the custom session dao
     */
    public void setCustomSessionDAO( CustomSessionDAO customSessionDAO ) {
        this.customSessionDAO = customSessionDAO;
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
