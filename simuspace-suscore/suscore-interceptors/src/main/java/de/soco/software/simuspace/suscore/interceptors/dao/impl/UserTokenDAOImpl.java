package de.soco.software.simuspace.suscore.interceptors.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.interceptors.dao.UserTokenDAO;

/**
 * An implementation class of UserTokenDAO. It is responsible for the CRUD operation related to user tokens.
 *
 * @author Zeeshan jamal
 */
public class UserTokenDAOImpl extends AbstractGenericDAO< UserTokenEntity > implements UserTokenDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity saveUserToken( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        return save( entityManager, userTokenEntity );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity getUserTokenEntityById( EntityManager entityManager, UUID id ) {
        return getLatestObjectById( entityManager, UserTokenEntity.class, id );
    }

    @Override
    public void updateUserTokenEntity( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        saveOrUpdate( entityManager, userTokenEntity );
    }

    @Override
    public UserTokenEntity getUserTokenEntityByActiveToken( EntityManager entityManager, String token ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.TOKEN, token );
        properties.put( ConstantsDAO.EXPIRED, false );
        return getUniqueObjectByProperties( entityManager, properties, UserTokenEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity getUserTokenEntityByToken( EntityManager entityManager, String token ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.TOKEN, token );
        return getUniqueObjectByProperties( entityManager, properties, UserTokenEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getAllActiveTokens( EntityManager entityManager ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.EXPIRED, Boolean.FALSE );
        return getListByPropertiesJpa( entityManager, properties, UserTokenEntity.class, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getAllActiveOrRunningJobTokens( EntityManager entityManager ) {

        List< UserTokenEntity > list;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< UserTokenEntity > criteriaQuery = criteriaBuilder.createQuery( UserTokenEntity.class );
        Root< UserTokenEntity > root = criteriaQuery.from( UserTokenEntity.class );
        Predicate notExpired = criteriaBuilder.equal( root.get( ConstantsDAO.EXPIRED ), Boolean.FALSE );
        Predicate runningJob = criteriaBuilder.equal( root.get( ConstantsDAO.RUNNING_JOB ), Boolean.TRUE );
        Predicate either = criteriaBuilder.or( notExpired, runningJob );
        criteriaQuery.where( either );
        list = entityManager.createQuery( criteriaQuery ).getResultList();
        return list;
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, UserTokenEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrRunningJobByIpAddressAndUserUid( EntityManager entityManager, String ipPc,
            String userUid ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< UserTokenEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( UserTokenEntity.class );
        Root< UserTokenEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( UserTokenEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        Predicate tokenActive = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.EXPIRED ), Boolean.FALSE );
        Predicate isRunningJob = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.RUNNING_JOB ), Boolean.TRUE );
        //active or running job predicate
        predicates.add( criteriaBuilder.or( tokenActive, isRunningJob ) );
        //ip predicate
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IPADDRESS ), ipPc ) );
        //userUid predicate
        predicates.add(
                criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.USER_ENTITY ).get( ConstantsDAO.USER_UID ), userUid ) );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
    }

    /**
     * Gets the all active tokens by filters.
     *
     * @param filter
     *         the filter
     *
     * @return the all active tokens by filters
     */
    @Override
    public List< UserTokenEntity > getAllActiveTokensByFilters( EntityManager entityManager, FiltersDTO filter ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.EXPIRED, Boolean.FALSE );
        return getAllFilteredRecordsByProperties( entityManager, UserTokenEntity.class, properties, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByFilter( EntityManager entityManager, FiltersDTO filtersDTO ) {
        List< UserTokenEntity > list;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< UserTokenEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( UserTokenEntity.class );
        Root< UserTokenEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( UserTokenEntity.class );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( UserTokenEntity.class, filtersDTO, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot );

        Predicate or = getActiveTokenOrInactiveTokenOnRunningJob( criteriaBuilder, filteredCriteriaRoot );
        predicates.add( or );

        addSortingInCriteriaQuery( UserTokenEntity.class, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        list = entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getAllFilteredRecordsCount( entityManager, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCount( entityManager ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getActiveTokenOrInactiveTokenOnRunningJobByIpAdress( EntityManager entityManager, String ipPc ) {
        List< UserTokenEntity > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< UserTokenEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( UserTokenEntity.class );
        Root< UserTokenEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( UserTokenEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        Predicate or = getActiveTokenOrInactiveTokenOnRunningJob( criteriaBuilder, filteredCriteriaRoot );
        predicates.add( or );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IPADDRESS ), ipPc ) );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        list = entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getUserTokenEntityListByUserId( EntityManager entityManager, UUID id ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.USER_ENTITY_ID, id );
        properties.put( ConstantsDAO.EXPIRED, false );
        return getListByPropertiesJpa( entityManager, properties, UserTokenEntity.class, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserTokenEntity > getAllUserTokenEntityListByUserId( EntityManager entityManager, UUID id ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.USER_ENTITY_ID, id );
        properties.put( ConstantsDAO.EXPIRED, true );
        return getListByPropertiesJpa( entityManager, properties, UserTokenEntity.class, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUserTokenEntity( EntityManager entityManager, UserTokenEntity userTokenEntity ) {
        delete( entityManager, userTokenEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAnyJobRunningByUserId( EntityManager entityManager, String userId ) {
        Long result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< JobEntity > root = criteriaQuery.from( JobEntity.class );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            criteriaQuery.distinct( true );
            Predicate createdBY = criteriaBuilder.equal( root.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ),
                    UUID.fromString( userId ) );
            Predicate runningJob = criteriaBuilder.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.RUNNING.getKey() );
            Predicate pausedJob = criteriaBuilder.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.PAUSED.getKey() );
            criteriaQuery.where( createdBY, criteriaBuilder.or( runningJob, pausedJob ) );
            result = entityManager.createQuery( criteriaQuery ).getSingleResult();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            result = 0L;
        }
        return result > 0;
    }

    /**
     * Checks if is any job running by token.
     *
     * @param token
     *         the token
     *
     * @return true, if is any job running by token
     */
    @Override
    public boolean isAnyJobRunningByToken( EntityManager entityManager, String token ) {
        Long result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< JobEntity > root = criteriaQuery.from( JobEntity.class );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            criteriaQuery.distinct( true );
            Predicate predicate1 = criteriaBuilder.equal( root.get( ConstantsDAO.TOKEN ), token );
            Predicate running = criteriaBuilder.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.RUNNING.getKey() );
            Predicate paused = criteriaBuilder.equal( root.get( ConstantsDAO.STATUS ), WorkflowStatus.PAUSED.getKey() );
            Predicate predicate2 = criteriaBuilder.or( running, paused );
            criteriaQuery.where( predicate1, predicate2 );
            result = entityManager.createQuery( criteriaQuery ).getSingleResult();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            result = 0L;
        }
        return result > 0;
    }

    /**
     * Gets the all records count.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the all records count
     */
    private Long getAllRecordsCount( EntityManager entityManager ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< UserTokenEntity > root = criteriaQuery.from( UserTokenEntity.class );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        Predicate or = getActiveTokenOrInactiveTokenOnRunningJob( criteriaBuilder, root );
        predicates.add( or );
        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the active token or inactive token on running job.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     *
     * @return the active token or inactive token on running job
     */
    private Predicate getActiveTokenOrInactiveTokenOnRunningJob( CriteriaBuilder criteriaBuilder,
            Root< UserTokenEntity > filteredCriteriaRoot ) {
        Predicate or = criteriaBuilder.disjunction();

        Predicate tokenActive = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.EXPIRED ), Boolean.FALSE );
        or.getExpressions().add( tokenActive );

        Predicate onExpiredRunningJob = criteriaBuilder.conjunction();
        Predicate tokenInactive = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.EXPIRED ), Boolean.TRUE );
        onExpiredRunningJob.getExpressions().add( tokenInactive );
        Predicate isRunningJob = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.RUNNING_JOB ), Boolean.TRUE );
        onExpiredRunningJob.getExpressions().add( isRunningJob );

        or.getExpressions().add( onExpiredRunningJob );
        return or;
    }

    /**
     * Gets the all filtered records count.
     *
     * @param entityManager
     *         the entity manager
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records count
     */
    private Long getAllFilteredRecordsCount( EntityManager entityManager, FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< UserTokenEntity > root = criteriaQuery.from( UserTokenEntity.class );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( UserTokenEntity.class, filtersDTO, criteriaBuilder, criteriaQuery,
                root );

        Predicate or = getActiveTokenOrInactiveTokenOnRunningJob( criteriaBuilder, root );
        predicates.add( or );

        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserTokenEntity update( EntityManager entityManager, UserTokenEntity e ) {
        return saveOrUpdate( entityManager, e );
    }

}