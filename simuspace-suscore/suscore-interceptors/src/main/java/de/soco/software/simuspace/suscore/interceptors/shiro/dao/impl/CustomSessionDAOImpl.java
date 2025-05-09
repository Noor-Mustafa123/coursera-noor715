package de.soco.software.simuspace.suscore.interceptors.shiro.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.hibernate.jpa.QueryHints;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.util.CompressionUtils;
import de.soco.software.simuspace.suscore.data.entity.SuscoreSession;
import de.soco.software.simuspace.suscore.interceptors.shiro.dao.CustomSessionDAO;

/**
 * The Class CustomSessionDAOImpl.
 */
@Log4j2
public class CustomSessionDAOImpl extends CachingSessionDAO implements CustomSessionDAO, SessionDAO {

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Prepare suscore session.
     *
     * @param session
     *         the session
     * @param sessionId
     *         the session id
     *
     * @return the suscore session
     */
    private SuscoreSession prepareSuscoreSession( byte[] session, Serializable sessionId ) {
        SuscoreSession suscoreSession = null;
        if ( session != null ) {
            suscoreSession = new SuscoreSession();
            suscoreSession.setId( sessionId );
            suscoreSession.setSession( session );
        }
        return suscoreSession;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doUpdate( Session session ) {
        if ( session instanceof ValidatingSession validatingSession && !validatingSession.isValid() ) {
            return;
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuscoreSession > criteria = criteriaBuilder.createQuery( SuscoreSession.class );
        Root< SuscoreSession > root = criteria.from( SuscoreSession.class );
        Predicate predicate = criteriaBuilder.equal( root.get( ConstantsDAO.ID ), session.getId() );
        criteria.where( predicate );
        SuscoreSession suscoreSession = entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        if ( suscoreSession.getUserId() == null && log.isDebugEnabled() ) {
            log.warn( "internal update of suscore session without user Id", new Exception() );
        }
        suscoreSession.setSession( CompressionUtils.serialize( session ) );
        entityManager.getTransaction().begin();
        entityManager.merge( suscoreSession );
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDelete( Session session ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete< SuscoreSession > criteriaDelete = criteriaBuilder.createCriteriaDelete( SuscoreSession.class );
        Root< SuscoreSession > root = criteriaDelete.from( SuscoreSession.class );
        criteriaDelete.where( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), session.getId() ) );
        entityManager.getTransaction().begin();
        entityManager.createQuery( criteriaDelete ).setHint( QueryHints.HINT_CACHEABLE, true ).executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Serializable doCreate( Session session ) {
        Serializable sessionId = generateSessionId( session );
        assignSessionId( session, sessionId );
        SuscoreSession suscoreSession = prepareSuscoreSession( CompressionUtils.serialize( session ), sessionId );
        if ( suscoreSession.getUserId() == null && log.isDebugEnabled() ) {
            log.warn( "creating suscore session without user Id", new Exception() );
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge( suscoreSession );
        entityManager.getTransaction().commit();
        entityManager.close();
        return session.getId();
    }

    /**
     * Do read session.
     *
     * @param sessionId
     *         the session id
     *
     * @return the session
     */
    @Override
    protected Session doReadSession( Serializable sessionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< byte[] > criteria = criteriaBuilder.createQuery( byte[].class );
        Root< SuscoreSession > root = criteria.from( SuscoreSession.class );
        criteria.select( root.get( ConstantsDAO.SESSION ) );
        Predicate predicate = criteriaBuilder.equal( root.get( ConstantsDAO.ID ), sessionId );
        criteria.where( predicate );
        byte[] session = entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        entityManager.close();
        return CompressionUtils.deserialize( session );
    }

    /**
     * Gets the active sessions.
     *
     * @return the active sessions
     */
    @Override
    public Collection< Session > getActiveSessions() {
        List< Session > sessions = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< byte[] > criteria = criteriaBuilder.createQuery( byte[].class );
        Root< SuscoreSession > root = criteria.from( SuscoreSession.class );
        criteria.select( root.get( ConstantsDAO.SESSION ) );
        List< byte[] > sessionsAsString = entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        entityManager.close();
        if ( CollectionUtils.isNotEmpty( sessionsAsString ) ) {
            sessions = sessionsAsString.stream().filter( Objects::nonNull ).map( CompressionUtils::deserialize ).toList();
        }
        return sessions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuscoreSession getResultByToken( EntityManager entityManager, String token ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuscoreSession > query = cb.createQuery( SuscoreSession.class );
        Root< SuscoreSession > root = query.from( SuscoreSession.class );
        Predicate predicate = cb.equal( root.get( ConstantsDAO.TOKEN ), token );
        query.where( predicate );
        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream().findFirst()
                .orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuscoreSession getResultByUserId( EntityManager entityManager, String userid ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuscoreSession > query = cb.createQuery( SuscoreSession.class );
        Root< SuscoreSession > root = query.from( SuscoreSession.class );
        List< Predicate > predicates = new ArrayList<>();
        Predicate predicate1 = cb.equal( root.get( ConstantsDAO.USER_ID ), userid );
        predicates.add( predicate1 );
        Predicate predicate2 = cb.isNotNull( root.get( ConstantsDAO.SESSION ) );
        predicates.add( predicate2 );
        query.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream().findFirst()
                .orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuscoreSession getResultByShiroId( EntityManager entityManager, Serializable id ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuscoreSession > query = cb.createQuery( SuscoreSession.class );
        Root< SuscoreSession > root = query.from( SuscoreSession.class );

        Predicate predicate = cb.equal( root.get( ConstantsDAO.ID ), id );
        query.where( predicate );

        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream().findFirst()
                .orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    public SuscoreSession update( EntityManager entityManager, SuscoreSession suscoreSession ) {
        entityManager.getTransaction().begin();
        if ( suscoreSession.getUserId() == null && log.isDebugEnabled() ) {
            log.warn( "updating suscore session without user Id", new Exception() );
        }
        SuscoreSession updatedSession = entityManager.merge( suscoreSession );
        entityManager.getTransaction().commit();
        return updatedSession;
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
