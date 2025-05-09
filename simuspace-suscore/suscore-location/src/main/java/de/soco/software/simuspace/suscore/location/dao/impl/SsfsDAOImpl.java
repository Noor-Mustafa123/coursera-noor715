package de.soco.software.simuspace.suscore.location.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserUrl;
import de.soco.software.simuspace.suscore.data.entity.UserUrlEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.location.dao.SsfsDAO;

/**
 * The Class SsfsDAOImpl.
 */
@Log4j2
public class SsfsDAOImpl extends AbstractGenericDAO< UserUrlEntity > implements SsfsDAO {

    /**
     * The Constant BOOKMARK.
     */
    protected static final String BOOKMARK_FIELD = "bookmark";

    /**
     * {@inheritDoc}
     */
    @Override
    public UserUrlEntity getUserUrlByPath( EntityManager entityManager, UserUrlEntity userUrlEntity ) {
        UserUrlEntity urlEntity = null;
        Session session = entityManager.unwrap( Session.class );

        try {
            session.beginTransaction();

            urlEntity = ( UserUrlEntity ) session.createCriteria( UserUrlEntity.class )
                    .add( Restrictions.eq( ConstantsDAO.CREATED_BY_ID, userUrlEntity.getCreatedBy().getId() ) )
                    .add( Restrictions.eq( ConstantsDAO.PATH, userUrlEntity.getPath() ) )
                    .add( Restrictions.eq( BOOKMARK_FIELD, userUrlEntity.getBookmark() ) )
                    .add( Restrictions.eq( ConstantsDAO.LOCATION_ID, userUrlEntity.getLocationId() ) ).uniqueResult();
            session.getTransaction().commit();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return urlEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserUrlEntity > getAllUserUrlList( EntityManager entityManager, String userId, UUID locationId ) {
        List< UserUrlEntity > entities = null;
        Session session = entityManager.unwrap( Session.class );

        try {
            session.beginTransaction();

            entities = session.createCriteria( UserUrlEntity.class )
                    .add( Restrictions.eq( ConstantsDAO.CREATED_BY_ID, UUID.fromString( userId ) ) )
                    .add( Restrictions.eq( BOOKMARK_FIELD, ConstantsUserUrl.HISTORY ) )
                    .add( Restrictions.eq( ConstantsDAO.LOCATION_ID, locationId ) ).addOrder( Order.desc( ConstantsDAO.MODIFIED_ON ) )
                    .setMaxResults( 10 ).list();
            session.getTransaction().commit();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return entities;
    }

}
