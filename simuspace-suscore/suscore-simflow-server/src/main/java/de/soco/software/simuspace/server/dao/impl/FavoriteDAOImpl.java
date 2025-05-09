package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import de.soco.software.simuspace.server.dao.FavoriteDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.FavoriteEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * This class will be responsible for all the database operations necessary for dealing with the favorites.
 *
 * @author Zeeshan jamal
 */
public class FavoriteDAOImpl extends AbstractGenericDAO< FavoriteEntity > implements FavoriteDAO {

    /**
     * The Constant FAVORITE_BY_ID.
     */
    private static final String FAVORITE_BY_ID = "favoriteBy.id";

    /**
     * The Constant DELETE_FAVORITE_WORKFLOW_HQL. for deleting favoriteEntity against the workFlowId
     */
    private static final String DELETE_FAVORITE_WORKFLOW_HQL = "delete from FavoriteEntity e where e.workflow.composedId.id= :workflowId and e.favoriteBy.id =  :userId";

    /**
     * The Constant SELECT_WORKFLOW_WITH_CREATED_BY_FROM_FAVORITE_ENTITY. for selecting workflowEntity from favoriteEntity against the
     * created by user id
     */
    private static final String SELECT_WORKFLOW_WITH_FAVORITE_BY_FROM_FAVORITE_ENTITY = "select workflow from  FavoriteEntity fe where fe.favoriteBy.id =  :userId order by fe.createdOn desc";

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWorkFlowToFavorites( EntityManager entityManager, FavoriteEntity favoriteEntity ) {
        Session session = entityManager.unwrap( Session.class );
        try {

            favoriteEntity.setCreatedOn( new Date() );
            session.beginTransaction();
            session.saveOrUpdate( favoriteEntity );
            // Commit transaction
            session.getTransaction().commit();

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeWorkFlowFromFavorites( EntityManager entityManager, UUID userId, FavoriteEntity favoriteEntity ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            final Query query = session.createQuery( DELETE_FAVORITE_WORKFLOW_HQL );
            query.setParameter( ConstantsDAO.WORKFLOW_ID, favoriteEntity.getWorkflow().getComposedId().getId() );
            query.setParameter( ConstantsDAO.USER_ID, userId );
            query.executeUpdate();

            // Commit transaction
            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowEntity > getFavouriteWorkFlowListByUserId( EntityManager entityManager, UUID userId ) {
        List< WorkflowEntity > result = null;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();

            final Query query = session.createQuery( SELECT_WORKFLOW_WITH_FAVORITE_BY_FROM_FAVORITE_ENTITY );
            query.setParameter( ConstantsDAO.USER_ID, userId );
            result = query.list();

            // Commit transaction

            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWorkFlowAlreadyFavorite( EntityManager entityManager, UUID userId, WorkflowEntity workFlowEntity ) {

        Long result;
        try ( Session session = entityManager.unwrap( Session.class ); ) {

            session.beginTransaction();

            result = ( Long ) session.createCriteria( FavoriteEntity.class ).setProjection( Projections.rowCount() )
                    .add( Restrictions.eq( FAVORITE_BY_ID, userId ) )
                    .add( Restrictions.eq( ConstantsDAO.WORKFLOW_COMPOSED_ID_ID, workFlowEntity.getComposedId().getId() ) ).uniqueResult();

            // Commit transaction
            session.getTransaction().commit();

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );

        }

        return result > 0;
    }

}
