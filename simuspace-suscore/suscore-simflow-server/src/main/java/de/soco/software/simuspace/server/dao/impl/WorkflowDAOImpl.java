package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowCategoryEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * This class will be responsible for all the database operations necessary for dealing with the Workflow.
 *
 * @author Nosheen.Sharif
 */
public class WorkflowDAOImpl extends AbstractGenericDAO< WorkflowEntity > implements WorkflowDAO {

    /**
     * The Constant VERSION_NOT_EXIST.
     */
    private static final int VERSION_NOT_EXIST = -1;

    /**
     * The Constant WF_CAT_WORKFLOW_ID.
     */
    private static final String WF_CAT_WORKFLOW_ID = "wfcat.workflow.composedId.id";

    /**
     * The Constant WF_CAT_WORKFLOW_VERSIONID.
     */
    private static final String WF_CAT_WORKFLOW_VERSIONID = "wfcat.workflow.composedId.versionId";

    /**
     * The Constant WF_CAT_CATEGORY_ID.
     */
    private static final String WF_CAT_CATEGORY_ID = "wfcat.category.id";

    /**
     * The Constant ORDER_BY_CREATED_ON_DESC.
     */
    private static final String ORDER_BY_CREATED_ON_DESC = " order by  wf.createdOn desc";

    /**
     * The Constant SELECT_WORKFLOW_COLUMNS_WITH_MODIFIED.
     */
    private static final String SELECT_WORKFLOW_COLUMNS_WITH_MODIFIED =
            "select distinct new de.soco.software.simuspace.server.entity.impl.WorkflowEntity(wf.composedId.id,"
                    + " wf.composedId.versionId, wf.name, wf.description, wf.comments,"
                    + " wf.createdOn , wf.createdBy.id,wf.createdBy.simId,wf.status,wf.modifiedOn) from WorkflowEntity wf ";

    /**
     * The Constant GET_WORKFLOW_LIST_HQL.
     */
    private static final String GET_WORKFLOW_LIST_HQL = SELECT_WORKFLOW_COLUMNS_WITH_MODIFIED
            + " where wf.composedId.versionId= (select max(ff.composedId.versionId) from WorkflowEntity ff where wf.composedId.id =ff.composedId.id) "
            + ORDER_BY_CREATED_ON_DESC;

    /**
     * The Constant SELECT_WORKFLOW_COLUMNS.
     */
    private static final String SELECT_WORKFLOW_COLUMNS =
            "select distinct new de.soco.software.simuspace.server.entity.impl.WorkflowEntity(wf.composedId.id,"
                    + " wf.composedId.versionId, wf.name, wf.description, wf.comments,"
                    + " wf.createdOn , wf.createdBy.id,wf.createdBy.simId,wf.status,wf.modifiedBy.id,wf.modifiedBy.simId) from WorkflowEntity wf ";

    /**
     * The Constant SELECT_WORKFLOW_DISTINCT_ID_COLUMN to get workflow distinct ids.
     */
    private static final String SELECT_WORKFLOW_DISTINCT_ID_COLUMN = "select distinct wf.composedId.id from WorkflowEntity wf ";

    /**
     * The Constant GROUPBY_COMPOSED_ID for group clause in hql.
     */
    private static final String GROUPBY_COMPOSED_ID = " group by  wf.composedId.id ";

    /**
     * Instantiates a new work flow DAO impl.
     */
    public WorkflowDAOImpl() {
        super();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowEntity > getWorkflowList( EntityManager entityManager ) {

        List< WorkflowEntity > result = null;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            final Query query = session.createQuery( GET_WORKFLOW_LIST_HQL );

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
    public WorkflowEntity saveWorkflow( EntityManager entityManager, WorkflowEntity workflowEntity ) {
        return save( entityManager, workflowEntity );
    }

    @Override
    public WorkflowEntity updateWorkflow( EntityManager entityManager, WorkflowEntity newWorkflowEntity ) {
        // Save will be called as update means to save workflow with latest version
        createNewVersion( entityManager, newWorkflowEntity );
        return newWorkflowEntity;

    }

    /**
     * {@inheritDoc}
     */
    // this method will give all workflow regardless of status
    // It may include deprecated,active etc
    @Override
    public List< WorkflowEntity > getWorkflowVersionsById( EntityManager entityManager, UUID workflowId ) {
        List< WorkflowEntity > result = null;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            final DetachedCriteria maxId = DetachedCriteria.forClass( WorkflowEntity.class )
                    .setProjection( Projections.max( ConstantsDAO.COMPOSED_ID_VERSION_ID ) )
                    .add( Restrictions.eq( ConstantsDAO.COMPOSED_ID_ID, workflowId ) );

            result = session.createCriteria( WorkflowEntity.class )

                    .add( Property.forName( ConstantsDAO.COMPOSED_ID_VERSION_ID ).lt( maxId ) )
                    .add( Restrictions.eq( ConstantsDAO.COMPOSED_ID_ID, workflowId ) ).list();

            // Commit transaction

            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }

        return result;
    }

    @Override
    public WorkflowEntity getWorkflowByIdAndVersionId( EntityManager entityManager, UUID workflowId, int versionId ) {
        WorkflowEntity result = null;
        EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
        Cache cache = entityManagerFactory.getCache();
        VersionPrimaryKey versionPrimaryKey = new VersionPrimaryKey( workflowId, versionId );
        if ( cache.contains( WorkflowEntity.class, versionPrimaryKey ) ) {
            result = entityManager.find( WorkflowEntity.class, versionPrimaryKey );
        } else {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< WorkflowEntity > criteria = cb.createQuery( WorkflowEntity.class );
            Root< WorkflowEntity > root = criteria.from( WorkflowEntity.class );
            criteria.distinct( true );
            Predicate predicate1 = cb.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), workflowId );
            Predicate predicate2 = cb.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ), versionId );
            Predicate predicate3 = cb.equal( root.get( ConstantsDAO.IS_DELETE ), false );
            criteria.where( predicate1, predicate2, predicate3 );
            result = entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream().findFirst()
                    .orElse( null );
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getWorkflowListByCategoryId( EntityManager entityManager, UUID categoryId ) {

        List< UUID > result;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();
            final StringBuilder queryString = new StringBuilder();
            queryString.append( SELECT_WORKFLOW_DISTINCT_ID_COLUMN );
            queryString.append( " ,WorkflowCategoryEntity wcat ,CategoryEntity cat" );

            queryString.append( " where  wf.composedId.id=wcat.workflow.composedId.id " );
            queryString.append( " and wcat.category.id = cat.id " );
            queryString.append( " and cat.id = '" + categoryId + "'" );

            queryString.append( GROUPBY_COMPOSED_ID );

            final Query query = session.createQuery( queryString.toString() );
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
    public List< UUID > getWorkflowListWithoutCategory( EntityManager entityManager ) {
        List< UUID > result;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            final StringBuilder queryString = new StringBuilder();
            queryString.append( SELECT_WORKFLOW_DISTINCT_ID_COLUMN );
            queryString.append( " where 1=1  " );

            queryString.append(
                    " and wf.composedId.id Not In  (select distinct wcat.workflow.composedId.id from WorkflowCategoryEntity wcat where wf.composedId.id=wcat.workflow.composedId.id) " );
            queryString.append( GROUPBY_COMPOSED_ID );
            final Query query = session.createQuery( queryString.toString() );
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
    public boolean isWorkflowExistInCategory( EntityManager entityManager, WorkflowEntity entity, UUID categoryId ) {
        WorkflowCategoryEntity result;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            final Criteria criteria = session.createCriteria( WorkflowCategoryEntity.class )

                    .add( Restrictions.eq( WF_CAT_WORKFLOW_ID, entity.getComposedId().getId() ) )
                    .add( Restrictions.eq( WF_CAT_WORKFLOW_VERSIONID, entity.getVersionId() ) )
                    .add( Restrictions.eq( WF_CAT_CATEGORY_ID, categoryId ) );
            result = ( WorkflowCategoryEntity ) criteria.uniqueResult();

            // Commit transaction

            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return result != null;

    }

    @Override
    public boolean isAlreadyWipWorkflowExist( EntityManager entityManager, UUID userId, UUID workflowId ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< WorkflowEntity > criteria = cb.createQuery( WorkflowEntity.class );
        Root< WorkflowEntity > root = criteria.from( WorkflowEntity.class );
        criteria.distinct( true );
        Predicate predicate1 = cb.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), workflowId );
        Predicate predicate2 = cb.equal( root.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ), userId );
        Predicate predicate3 = cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.WIP.getKey() );
        criteria.where( predicate1, predicate2, predicate3 );
        WorkflowEntity result = entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
        return result != null;
    }

    @Override
    public List< WorkflowEntity > getWorkflowVersionsWithoutDefinition( EntityManager entityManager, UUID id ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< WorkflowEntity > criteria = cb.createQuery( WorkflowEntity.class );
        Root< WorkflowEntity > root = criteria.from( WorkflowEntity.class );
        Predicate predicate1 = cb.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
        criteria.where( predicate1 );
        criteria.orderBy( cb.desc( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        return entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getWorkflowIdsList( EntityManager entityManager ) {

        List< UUID > result;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            final Criteria criteria = session.createCriteria( WorkflowEntity.class )

                    .setProjection( Projections.projectionList()
                            .add( Projections.distinct( Projections.property( ConstantsDAO.COMPOSED_ID_ID ) ) ) );

            result = criteria.list();

            // Commit transaction

            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return result;
    }

    @Override
    public boolean isAllVersionsOfWorkflowDeleted( EntityManager entityManager, UUID workflowId ) {
        boolean result;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< WorkflowEntity > criteria = cb.createQuery( WorkflowEntity.class );
        Root< WorkflowEntity > root = criteria.from( WorkflowEntity.class );
        Predicate predicate1 = cb.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), workflowId );
        criteria.where( predicate1 );
        final List< WorkflowEntity > returnList = entityManager.createQuery( criteria ).getResultList();
        result = ( returnList != null ) && returnList.isEmpty();
        return result;
    }

    @Override
    public long getTotalJobCountByWorkflowIdAndVersion( EntityManager entityManager, UUID workflowId, int verisonId ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteria = cb.createQuery( Long.class );
        Root< JobEntity > root = criteria.from( JobEntity.class );
        criteria.select( cb.count( root ) );
        Predicate predicate1 = cb.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                workflowId );
        Predicate predicate2 = cb.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ),
                verisonId );
        criteria.where( predicate1, predicate2 );
        return entityManager.createQuery( criteria ).getSingleResult();
    }

    @Override
    public List< Object > getAllPropertyValuesByParentId( EntityManager entityManager, String propertyName, UUID parentId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Object > criteriaQuery = criteriaBuilder.createQuery( Object.class );
        Root< WorkflowEntity > root = criteriaQuery.from( WorkflowEntity.class );

        List< Predicate > predicates = new ArrayList<>();
        setSelectExpressionForAllProperties( propertyName, root, criteriaQuery );
        if ( ReflectionUtils.hasField( WorkflowEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( WorkflowEntity.class, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( WorkflowEntity.class, criteriaBuilder, criteriaQuery, root );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), parentId ) );
        }

        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
    }

    @Override
    public long getCompletedJobCountByWorkflowIdAndVersion( EntityManager entityManager, UUID workflowId, int verisonId ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteria = cb.createQuery( Long.class );
        Root< JobEntity > root = criteria.from( JobEntity.class );
        criteria.select( cb.count( root ) );
        Predicate predicate1 = cb.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                workflowId );
        Predicate predicate2 = cb.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ),
                verisonId );
        Predicate predicate3 = cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.COMPLETED.getKey() );
        criteria.where( predicate1, predicate2, predicate3 );
        return entityManager.createQuery( criteria ).getSingleResult();
    }

}
