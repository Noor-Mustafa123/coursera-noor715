package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;

import de.soco.software.simuspace.server.dao.WorkflowCategoryDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;

/**
 * This class will be responsible for all the database operations necessary for dealing with the workflow with category relation.
 *
 * @author Nosheen.Sharif
 */
public class WorkflowCategoryDAOImpl extends AbstractGenericDAO< WorkflowCategoryEntity > implements WorkflowCategoryDAO {

    private static final String DELETE_HQL = "delete from WorkflowCategoryEntity e where e.workflow.composedId.id= :workflowId";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean assignCategoriesToWorkflow( EntityManager entityManager, List< WorkflowCategoryEntity > list, UUID workflowId ) {
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();
            final Query query = session.createQuery( DELETE_HQL );
            query.setParameter( ConstantsDAO.WORKFLOW_ID, workflowId );
            query.executeUpdate();
            if ( CollectionUtil.isNotEmpty( list ) ) {
                for ( final WorkflowCategoryEntity entity : list ) {
                    session.saveOrUpdate( entity );
                    session.flush();
                }
            }

            // Commit transaction
            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );

        }
        return true;

    }

}
