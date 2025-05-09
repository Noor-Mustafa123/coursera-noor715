package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;

import de.soco.software.simuspace.server.dao.CategoryDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.CategoryEntity;

/**
 * This class will be responsible for all the database operations necessary for dealing with the Category.
 *
 * @author Nosheen.Sharif
 */
public class CategoryDAOImpl extends AbstractGenericDAO< CategoryEntity > implements CategoryDAO {

    /**
     * CATEGORY_ID column name.
     */
    private static final String CATEGORY_ID = "categoryId";

    /**
     * DELETE_WORKFLOW_CATEGORY_HQL query
     */
    private static final String DELETE_WORKFLOW_CATEGORY_HQL = "delete from WorkflowCategoryEntity where category.id= :categoryId ";

    /**
     * The Constant GET_CATEGORY_LIST_BY_WORKFLOW_ID_HQL.
     */
    private static final String GET_CATEGORY_LIST_BY_WORKFLOW_ID_HQL =
            "select new de.soco.software.simuspace.server.entity.impl.CategoryEntity(cat.id,cat.name) from WorkflowCategoryEntity ent "
                    + " ,CategoryEntity cat where ent.category.id = cat.id  AND ent.workflow.composedId.id = :workflowId";

    /**
     * The Constant WORKFLOW_COUNT_BY_CATEGORY_ID_HQL.
     */
    private static final String WORKFLOW_COUNT_BY_CATEGORY_ID_HQL = "select count(entity.id) from WorkflowCategoryEntity entity where entity.category.id= :id ";

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CategoryEntity > getCategories( EntityManager entityManager ) {

        return getAllRecords( entityManager, CategoryEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryEntity addCategory( EntityManager entityManager, CategoryEntity categoryEntity ) {
        return save( entityManager, categoryEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryEntity updateCategory( EntityManager entityManager, CategoryEntity categoryEntity ) {
        return update( entityManager, categoryEntity );
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean removeCategory( EntityManager entityManager, UUID categoryId ) {
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            // delete the workflowCategoriesEntity childs

            final Query query = session.createQuery( DELETE_WORKFLOW_CATEGORY_HQL );
            query.setParameter( CATEGORY_ID, categoryId );
            query.executeUpdate();

            // delete the CategoryEntity parent
            final CategoryEntity myObject = session.get( CategoryEntity.class, categoryId );
            session.delete( myObject );

            // Commit transaction
            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryEntity getCategoryById( EntityManager entityManager, UUID categoryId ) {
        return getSimpleObjectById( entityManager, CategoryEntity.class, categoryId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CategoryEntity > getCategoryListByWorkflowId( EntityManager entityManager, UUID workflowId ) {
        List< CategoryEntity > result;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            final Query query = session.createQuery( GET_CATEGORY_LIST_BY_WORKFLOW_ID_HQL );
            query.setParameter( ConstantsDAO.WORKFLOW_ID, workflowId );

            result = query.list();

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
    public int getWorkflowCountByCategoryId( EntityManager entityManager, UUID id ) {
        int result = 0;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            // delete the workflowCategoriesEntity childs

            final Query query = session.createQuery( WORKFLOW_COUNT_BY_CATEGORY_ID_HQL );
            query.setParameter( ConstantsDAO.ID, id );
            if ( !query.list().isEmpty() ) {
                // as the category ID is unique, we are sure we find one entry at most.
                result = Math.toIntExact( ( long ) query.list().get( 0 ) );
            }

            // Commit transaction
            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return result;
    }

}
