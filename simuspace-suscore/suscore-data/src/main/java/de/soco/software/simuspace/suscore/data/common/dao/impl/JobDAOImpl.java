package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * This class will be responsible for all the database operations necessary for dealing with the Job.
 *
 * @author Aroosa.Bukhari
 */
public class JobDAOImpl extends AbstractGenericDAO< JobEntity > implements JobDAO {

    /**
     * The machine on which the job executed.
     */
    private static final String MACHINE = "machine";

    private static final String JOB_SCHEME_CATEGORY = "jobSchemeCategory";

    private final Class< JobEntity > entityClazz = JobEntity.class;

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity getJob( EntityManager entityManager, UUID jobId ) {
        return getSimpleObjectById( entityManager, JobEntity.class, jobId );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity saveJob( EntityManager entityManager, JobEntity jobEntity ) {
        return saveOrUpdate( entityManager, jobEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity updateJobStatus( EntityManager entityManager, JobEntity jobEntity ) {
        return update( entityManager, jobEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getJobsList( EntityManager entityManager ) {
        return getAllRecordsWithDescOrder( entityManager, JobEntity.class, ConstantsDAO.CREATED_ON );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getMasterRunningJobsList( EntityManager entityManager ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( JobEntity.class );
        Root< ? > root = criteria.from( JobEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( cb.or( cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.RUNNING.getKey() ),
                cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.PAUSED.getKey() ) ) );
        predicates.add( cb.equal( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.MASTER.getKey() ) );
        predicates.add( cb.or( cb.equal( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SCHEME.getKey() ),
                cb.equal( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.VARIANT.getKey() ) ) );
        predicates.add( cb.equal( root.get( JOB_SCHEME_CATEGORY ), SchemeCategoryEnum.DOE.getKey() ) );
        criteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        List< JobEntity > list = ( List< JobEntity > ) entityManager.createQuery( criteria ).getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getRunningChildsAndSingleJobsList( EntityManager entityManager ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( JobEntity.class );
        Root< ? > root = criteria.from( JobEntity.class );
        Predicate running = cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.RUNNING.getKey() );
        Predicate paused = cb.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.PAUSED.getKey() );
        Predicate status = cb.or( running, paused );
        Predicate runningJobsSingleAndChilds = cb.or(
                cb.and( cb.equal( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SCHEME.getKey() ),
                        cb.equal( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.CHILD.getKey() ) ),
                cb.and( cb.equal( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.WORKFLOW.getKey() ),
                        cb.equal( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.MASTER.getKey() ) ) );
        criteria.where( status, runningJobsSingleAndChilds );
        List< JobEntity > list = ( List< JobEntity > ) entityManager.createQuery( criteria ).getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity updateJobLog( EntityManager entityManager, JobEntity entity ) {
        return update( entityManager, entity );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobEntity getLastJobByWorkFlow( EntityManager entityManager, UUID workflowId, String machine ) {
        JobEntity result;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();

            final DetachedCriteria maxModifiedOn = DetachedCriteria.forClass( JobEntity.class ).add( Restrictions.eq( MACHINE, machine ) )
                    .add( Restrictions.eq( ConstantsDAO.WORKFLOW_COMPOSED_ID_ID, workflowId ) )
                    .setProjection( Projections.max( ConstantsDAO.MODIFIED_ON ) );

            result = ( JobEntity ) session.createCriteria( JobEntity.class )
                    .add( Property.forName( ConstantsDAO.MODIFIED_ON ).eq( maxModifiedOn ) )

                    .uniqueResult();
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
    public List< JobEntity > getJobsByUserId( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO ) {
        return getAllFilteredRecordsWithPermissionsByJpa( entityManager, JobEntity.class, filtersDTO, userId,
                PermissionMatrixEnum.READ.getKey(), Boolean.TRUE );
    }

    @Override
    public List< JobEntity > getAllJobsByWorkflowId( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO,
            String workflowId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobEntity > query = criteriaBuilder.createQuery( entityClazz );
        Root< JobEntity > from = query.from( entityClazz );
        Map< String, Object > queryProperties = new HashMap<>();
        queryProperties.put( ConstantsDAO.WORKFLOW + ConstantsString.DOT + ConstantsDAO.COMPOSED_ID + ConstantsString.DOT + ConstantsDAO.ID,
                UUID.fromString( workflowId ) );
        queryProperties.put( ConstantsDAO.JOB_RELATION_TYPE, JobRelationTypeEnums.MASTER.getKey() );
        List< Predicate > predicates = new ArrayList<>( propertiesToPredicates( criteriaBuilder, from, queryProperties ) );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, from, query, filtersDTO );
        getFilteredPredicates( filtersDTO, criteriaBuilder, from, predicates );
        query.where( predicates.toArray( Predicate[]::new ) );

        List< JobEntity > list = entityManager.createQuery( query )
                .setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();

        Long setFilteredRecords = getCountOfJobWithProperties( entityManager, criteriaBuilder, queryProperties, true, filtersDTO );
        Long setTotalRecords = getCountOfJobWithProperties( entityManager, criteriaBuilder, queryProperties, false, filtersDTO );
        filtersDTO.setFilteredRecords( setFilteredRecords );
        filtersDTO.setTotalRecords( setTotalRecords );

        return list;

    }

    /**
     * Properties to predicates list.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param from
     *         the from
     * @param properties
     *         the properties
     *
     * @return the list
     */
    List< Predicate > propertiesToPredicates( CriteriaBuilder criteriaBuilder, Root< JobEntity > from,
            Map< String, Object > properties ) {
        List< Predicate > propertiesToPredicates = new LinkedList<>();
        for ( Map.Entry< String, Object > property : properties.entrySet() ) {
            propertiesToPredicates.add( getPredicateForProperty( property, criteriaBuilder, from ) );
        }
        return propertiesToPredicates;
    }

    /**
     * Gets predicate for property.
     *
     * @param property
     *         the property
     * @param criteriaBuilder
     *         the criteria builder
     * @param selectFrom
     *         the select from
     *
     * @return the predicate for property
     */
    private static Predicate getPredicateForProperty( Map.Entry< String, Object > property, CriteriaBuilder criteriaBuilder,
            Root< JobEntity > selectFrom ) {
        String propertyName = property.getKey();
        Object propertyValue = property.getValue();
        if ( propertyName.contains( ConstantsString.DOT ) ) {
            String[] split = propertyName.split( "\\." );
            Path< ? > baseExpression = selectFrom.join( split[ 0 ] );
            for ( int i = 1; i < split.length; i++ ) {
                baseExpression = baseExpression.get( split[ i ] );
            }
            return criteriaBuilder.equal( baseExpression, propertyValue );
        } else {
            return criteriaBuilder.equal( selectFrom.get( propertyName ), propertyValue );
        }
    }

    /**
     * Gets count of job with predicates.
     *
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param queryProperties
     *         the query properties
     *
     * @return the count of job with predicates
     */
    private long getCountOfJobWithProperties( EntityManager entityManager, CriteriaBuilder criteriaBuilder,
            Map< String, Object > queryProperties, boolean addFilterPredicates,
            FiltersDTO filtersDTO ) {
        CriteriaQuery< Long > query = criteriaBuilder.createQuery( Long.class );
        Root< JobEntity > from = query.from( entityClazz );
        query.select( criteriaBuilder.count( from ) );
        List< Predicate > predicates = propertiesToPredicates( criteriaBuilder, from, queryProperties );
        if ( addFilterPredicates ) {
            getFilteredPredicates( filtersDTO, criteriaBuilder, from, predicates );
        }
        predicates.add( criteriaBuilder.notEqual( from.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey()) );
        query.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    private long getCountOfSystemJobWithProperties( EntityManager entityManager, CriteriaBuilder criteriaBuilder,
            Map< String, Object > queryProperties, boolean addFilterPredicates,
            FiltersDTO filtersDTO ) {
        CriteriaQuery< Long > query = criteriaBuilder.createQuery( Long.class );
        Root< JobEntity > from = query.from( entityClazz );
        query.select( criteriaBuilder.count( from ) );
        List< Predicate > predicates = propertiesToPredicates( criteriaBuilder, from, queryProperties );
        if ( addFilterPredicates ) {
            getFilteredPredicates( filtersDTO, criteriaBuilder, from, predicates );
        }
        predicates.add( criteriaBuilder.equal( from.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey()) );
        query.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( query ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getAllJobsForList( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobEntity > criteriaQuery = criteriaBuilder.createQuery( JobEntity.class );
        Root< JobEntity > jobRoot = criteriaQuery.from( JobEntity.class );
        Join< JobEntity, WorkflowEntity > joinWorkflow = jobRoot.join( ConstantsDAO.WORKFLOW, JoinType.LEFT );
        Map< String, Object > queryProperties = new HashMap<>();
        queryProperties.put( ConstantsDAO.JOB_RELATION_TYPE, JobRelationTypeEnums.MASTER.getKey() );
        List< Predicate > predicates = new ArrayList<>( propertiesToPredicates( criteriaBuilder, jobRoot, queryProperties ) );
        criteriaQuery.multiselect( jobRoot.get( ConstantsDAO.ID ), jobRoot.get( ConstantsDAO.NAME ),
                jobRoot.get( ConstantsDAO.DESCRIPTION ), jobRoot.get( ConstantsDAO.PID ), jobRoot.get( ConstantsDAO.CREATED_ON ),
                jobRoot.get( ConstantsDAO.FINISHED_ON ), jobRoot.get( ConstantsDAO.OS ), jobRoot.get( ConstantsDAO.MACHINE ),
                jobRoot.get( ConstantsDAO.STATUS ), jobRoot.get( ConstantsDAO.RUN_MODE ), jobRoot.get( ConstantsDAO.RUNS_ON ),
                jobRoot.get( ConstantsDAO.CREATED_BY ), jobRoot.get( ConstantsDAO.JOB_DIRECTORY ),
                jobRoot.get( ConstantsDAO.TOTAL_ELEMENTS ), jobRoot.get( ConstantsDAO.COMPLETED_ELEMENTS ),
                jobRoot.get( ConstantsDAO.WORKFLOW_ID ), jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ),
                jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.NAME ) );
        predicates.add( criteriaBuilder.equal( jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ),
                joinWorkflow.get( ConstantsDAO.COMPOSED_ID ) ) );
        predicates.add( criteriaBuilder.notEqual( jobRoot.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey()) );
        getFilteredPredicates( filtersDTO, criteriaBuilder, jobRoot, predicates );
        addSortingInCriteriaQuery( criteriaBuilder, jobRoot, criteriaQuery, filtersDTO );
        criteriaQuery.where( predicates.stream().toArray( Predicate[]::new ) );
        List< JobEntity > list = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getCountOfJobWithProperties( entityManager, criteriaBuilder, queryProperties, true, filtersDTO ) );
        filtersDTO
                .setTotalRecords( getCountOfJobWithProperties( entityManager, criteriaBuilder, queryProperties, false, filtersDTO ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getAllSystemJobsForList( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobEntity > criteriaQuery = criteriaBuilder.createQuery( JobEntity.class );
        Root< JobEntity > jobRoot = criteriaQuery.from( JobEntity.class );
        Join< JobEntity, WorkflowEntity > joinWorkflow = jobRoot.join( ConstantsDAO.WORKFLOW, JoinType.LEFT );
        Map< String, Object > queryProperties = new HashMap<>();
        queryProperties.put( ConstantsDAO.JOB_RELATION_TYPE, JobRelationTypeEnums.MASTER.getKey() );
        List< Predicate > predicates = new ArrayList<>( propertiesToPredicates( criteriaBuilder, jobRoot, queryProperties ) );
        criteriaQuery.multiselect( jobRoot.get( ConstantsDAO.ID ), jobRoot.get( ConstantsDAO.NAME ),
                jobRoot.get( ConstantsDAO.DESCRIPTION ), jobRoot.get( ConstantsDAO.PID ), jobRoot.get( ConstantsDAO.CREATED_ON ),
                jobRoot.get( ConstantsDAO.FINISHED_ON ), jobRoot.get( ConstantsDAO.OS ), jobRoot.get( ConstantsDAO.MACHINE ),
                jobRoot.get( ConstantsDAO.STATUS ), jobRoot.get( ConstantsDAO.RUN_MODE ), jobRoot.get( ConstantsDAO.RUNS_ON ),
                jobRoot.get( ConstantsDAO.CREATED_BY ), jobRoot.get( ConstantsDAO.JOB_DIRECTORY ),
                jobRoot.get( ConstantsDAO.TOTAL_ELEMENTS ), jobRoot.get( ConstantsDAO.COMPLETED_ELEMENTS ),
                jobRoot.get( ConstantsDAO.WORKFLOW_ID ), jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ),
                jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.NAME ) );
        predicates.add( criteriaBuilder.equal( jobRoot.get( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ),
                joinWorkflow.get( ConstantsDAO.COMPOSED_ID ) ) );
        predicates.add( criteriaBuilder.equal( jobRoot.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey()) );
        getFilteredPredicates( filtersDTO, criteriaBuilder, jobRoot, predicates );
        addSortingInCriteriaQuery( criteriaBuilder, jobRoot, criteriaQuery, filtersDTO );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< JobEntity > list = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getCountOfSystemJobWithProperties( entityManager, criteriaBuilder, queryProperties, true, filtersDTO ) );
        filtersDTO
                .setTotalRecords( getCountOfSystemJobWithProperties( entityManager, criteriaBuilder, queryProperties, false, filtersDTO ) );
        return list;
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Object > criteriaQuery = criteriaBuilder.createQuery( Object.class );
        Root< JobEntity > root = criteriaQuery.from( JobEntity.class );
        setSelectExpressionForAllProperties( propertyName, root, criteriaQuery );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        Predicate jobRelationPredicate = criteriaBuilder.or( criteriaBuilder.isNull( root.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.CHILD.getKey() ) );
        predicates.add( jobRelationPredicate );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();

    }

    @Override
    public List< Object > getAllPropertyValuesWithParentId( EntityManager entityManager, String propertyName, UUID masterJobId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Object > criteriaQuery = criteriaBuilder.createQuery( Object.class );
        Root< JobEntity > root = criteriaQuery.from( JobEntity.class );
        setSelectExpressionForAllProperties( propertyName, root, criteriaQuery );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( JobEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        Predicate jobRelationPredicate = criteriaBuilder.or( criteriaBuilder.isNull( root.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.MASTER.getKey() ) );
        Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( masterJobId, criteriaBuilder, criteriaQuery );
        predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        predicates.add( jobRelationPredicate );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< JobEntity > getAllUserJobsMinimal( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobEntity > criteriaQuery = criteriaBuilder.createQuery( JobEntity.class );
        Root< JobEntity > jobRoot = criteriaQuery.from( JobEntity.class );
        Join< JobEntity, UserEntity > joinUser = jobRoot.join( ConstantsDAO.CREATED_BY, JoinType.LEFT );
        List< Predicate > predicates = new ArrayList<>();
        criteriaQuery.multiselect( jobRoot.get( ConstantsDAO.ID ), jobRoot.get( ConstantsDAO.NAME ),
                jobRoot.get( ConstantsDAO.STATUS ), jobRoot.get( ConstantsDAO.TOTAL_ELEMENTS ),
                jobRoot.get( ConstantsDAO.COMPLETED_ELEMENTS ) );
        predicates.add(
                criteriaBuilder.equal( jobRoot.get( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ), joinUser.get( ConstantsDAO.ID ) ) );
        predicates.add( criteriaBuilder.equal( jobRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        predicates.add( criteriaBuilder.notEqual( jobRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) ) );
        predicates.add( criteriaBuilder.notEqual( jobRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) ) );
        Predicate jobRelationPredicate = criteriaBuilder.or( criteriaBuilder.isNull( jobRoot.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                criteriaBuilder.notEqual( jobRoot.get( ConstantsDAO.JOB_RELATION_TYPE ), 1 ) );
        predicates.add( jobRelationPredicate );
        getFilteredPredicates( filtersDTO, criteriaBuilder, jobRoot, predicates );
        addSortingInCriteriaQuery( criteriaBuilder, jobRoot, criteriaQuery, filtersDTO );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< JobEntity > list = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( count( entityManager, criteriaBuilder, filtersDTO, JobEntity.class ) );
        filtersDTO
                .setTotalRecords( getCountWithPermissionsByJpa( JobEntity.class, userId, entityManager, criteriaBuilder, Boolean.FALSE ) );
        return list;
    }

    /**
     * Adds the sorting in criteria query.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filtersDTO
     *         the filters DTO
     */
    private void addSortingInCriteriaQuery( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            CriteriaQuery< ? > paginatedFilteredCriteria, FiltersDTO filtersDTO ) {
        boolean hasSorting = false;
        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( filterColumn.getDir() ) ) {
                    hasSorting = mergeSortFiltersToCriteriaQuery( JobEntity.class, criteriaBuilder, filteredCriteriaRoot,
                            paginatedFilteredCriteria, filterColumn );
                }
            }
        }
        if ( !hasSorting ) {
            paginatedFilteredCriteria.orderBy( criteriaBuilder.desc( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ) ) );
        }
    }

    private void getFilteredPredicates( FiltersDTO filtersDTO, CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            List< Predicate > predicates ) {
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, JobEntity.class, filtersDTO, null, predicates );
        Predicate jobRelationPredicate = criteriaBuilder.or(
                criteriaBuilder.isNull( filteredCriteriaRoot.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                criteriaBuilder.notEqual( filteredCriteriaRoot.get( ConstantsDAO.JOB_RELATION_TYPE ), 1 ) );
        predicates.add( jobRelationPredicate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Relation > getjobRelationsById( EntityManager entityManager, Object value ) {
        return getRelationListByProperty( entityManager, ConstantsDAO.PARENT, value );
    }

    @Override
    public List< JobEntity > getAllChildrenOfMasterJob( EntityManager entityManager, UUID jobId ) {
        List< JobEntity > list;
        var entityClazz = JobEntity.class;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
                Predicate isDeletedPredicate = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false );
                predicates.add( isDeletedPredicate );
            }
            if ( null != jobId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( jobId, criteriaBuilder, paginatedFilteredCriteria );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.get( ConstantsDAO.ID ) ).value( relationSubquery ) );
            }
            paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
            list = ( List< JobEntity > ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    @Override
    public List< JobEntity > getAllFilteredChildrenOfMasterJob( EntityManager entityManager, FiltersDTO filter, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( JobEntity.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( JobEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, JobEntity.class, filter, null, predicates );
        Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( id, criteriaBuilder, paginatedFilteredCriteria );
        predicates.add( criteriaBuilder.in( filteredCriteriaRoot.get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        addSortingInCriteriaQuery( JobEntity.class, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filter );
        paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        List< JobEntity > list = ( List< JobEntity > ) entityManager.createQuery( paginatedFilteredCriteria )
                .setFirstResult( filter.getStart() ).setMaxResults( filter.getLength() ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        filter.setFilteredRecords( getAllFilteredRecordCount( entityManager, criteriaBuilder, JobEntity.class, filter, id ) );
        filter.setTotalRecords( getAllTotalRecordCount( entityManager, criteriaBuilder, JobEntity.class, id ) );
        return list;
    }

}
