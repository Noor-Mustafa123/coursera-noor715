package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.server.dao.VariableDAO;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.CustomVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectiveVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.VariableEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The type Variable dao.
 */
public class VariableDAOImpl extends AbstractGenericDAO< VariableEntity > implements VariableDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVariables( EntityManager entityManager, List< VariableEntity > variableEntities ) {
        saveAll( entityManager, variableEntities );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariableEntity getVariableById( EntityManager entityManager, String variableId, Class< ? > clazz ) {
        return getLatestObjectById( entityManager, clazz, UUID.fromString( variableId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllFilteredVariables( EntityManager entityManager, String workflowId, String userId, FiltersDTO filtersDTO,
            Class< ? > entityClazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );

        predicates.add( criteriaBuilder.equal(
                filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( workflowId ) ) );

        Subquery< Integer > maxVersionSubQuery = getLatestWorkflowEntityVersionAsSubQuery( criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );

        predicates.add( criteriaBuilder
                .in( filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery ) );

        Predicate predicate3 = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.USER_ID ), UUID.fromString( userId ) );
        predicates.add( predicate3 );

        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        paginatedFilteredCriteria.distinct( true );
        List< ? > list = entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        filtersDTO.setFilteredRecords( getAllFilteredRecordCountByWorkflowIdAndUserId( entityManager, criteriaBuilder, entityClazz,
                filtersDTO, UUID.fromString( workflowId ), UUID.fromString( userId ) ) );
        filtersDTO.setTotalRecords( getTotalRecordsCountByWorkflowIdAndUserId( entityManager, criteriaBuilder, entityClazz,
                UUID.fromString( workflowId ), UUID.fromString( userId ) ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< DesignVariableEntity > getAllFilteredDesignVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO ) {
        return ( List< DesignVariableEntity > ) getAllFilteredVariables( entityManager, workflowId, userId, filtersDTO,
                DesignVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectiveVariableEntity > getAllFilteredObjectiveVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO ) {
        return ( List< ObjectiveVariableEntity > ) getAllFilteredVariables( entityManager, workflowId, userId, filtersDTO,
                ObjectiveVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CustomVariableEntity > getAllFilteredCustomVariables( EntityManager entityManager, String workflowId, String userId,
            FiltersDTO filtersDTO ) {
        return ( List< CustomVariableEntity > ) getAllFilteredVariables( entityManager, workflowId, userId, filtersDTO,
                CustomVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllVariables( EntityManager entityManager, String workflowId, String userId, Class< ? > entityClazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > root = criteriaQuery.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();

        Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
        Root< WorkflowEntity > rootMaxVersionSubQuery = maxVersionSubQuery.from( WorkflowEntity.class );
        maxVersionSubQuery
                .select( criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( workflowId ) );
        maxVersionSubQuery.where( maxIdPredicate );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder
                .in( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );

        predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( workflowId ) ) );

        Predicate predicate3 = criteriaBuilder.equal( root.get( ConstantsDAO.USER_ID ), UUID.fromString( userId ) );
        predicates.add( predicate3 );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DesignVariableEntity readDesignVariable( EntityManager entityManager, UUID variableId ) {
        return ( DesignVariableEntity ) readVariable( entityManager, variableId, DesignVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectiveVariableEntity readObjectiveVariable( EntityManager entityManager, UUID variableId ) {
        return ( ObjectiveVariableEntity ) readVariable( entityManager, variableId, ObjectiveVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CustomVariableEntity readCustomVariable( EntityManager entityManager, UUID variableId ) {
        return ( CustomVariableEntity ) readVariable( entityManager, variableId, CustomVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariableEntity readVariable( EntityManager entityManager, UUID variableId, Class< ? > variableSubClass ) {
        return getSimpleObjectById( entityManager, variableSubClass, variableId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllVariablesByWorkflowIdAndClass( EntityManager entityManager, String workflowId, Class< ? > variableSubClass ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( variableSubClass );
        Root< ? > root = criteriaQuery.from( variableSubClass );
        List< Predicate > predicates = new ArrayList<>();

        Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
        Root< WorkflowEntity > rootMaxVersionSubQuery = maxVersionSubQuery.from( WorkflowEntity.class );
        maxVersionSubQuery
                .select( criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( workflowId ) );
        maxVersionSubQuery.where( maxIdPredicate );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder
                .in( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );

        predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                UUID.fromString( workflowId ) ) );

        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getResultList();
    }

    @Override
    public List< ObjectiveVariableEntity > getAllObjectiveVariablesByWorkflowId( EntityManager entityManager, String workflowId ) {
        return ( List< ObjectiveVariableEntity > ) getAllVariablesByWorkflowIdAndClass( entityManager, workflowId,
                ObjectiveVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< DesignVariableEntity > getAllDesignVariables( EntityManager entityManager, String workflowId, String userId ) {
        return ( List< DesignVariableEntity > ) getAllVariables( entityManager, workflowId, userId, DesignVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectiveVariableEntity > getAllObjectiveVariables( EntityManager entityManager, String workflowId, String userId ) {
        return ( List< ObjectiveVariableEntity > ) getAllVariables( entityManager, workflowId, userId, ObjectiveVariableEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CustomVariableEntity > getAllCustomVariables( EntityManager entityManager, String workflowId, String userId ) {
        return ( List< CustomVariableEntity > ) getAllVariables( entityManager, workflowId, userId, CustomVariableEntity.class );
    }

}
