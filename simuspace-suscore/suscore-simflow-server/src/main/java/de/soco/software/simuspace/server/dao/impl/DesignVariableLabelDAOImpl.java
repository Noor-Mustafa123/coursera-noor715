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

import de.soco.software.simuspace.server.dao.DesignVariableLabelDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableLabelEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The type Design variable label dao.
 */
public class DesignVariableLabelDAOImpl extends AbstractGenericDAO< DesignVariableLabelEntity > implements DesignVariableLabelDAO {

    @Override
    public DesignVariableLabelEntity getDesignVariableLabelByWorkflowId( EntityManager entityManager, UUID workflowId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< DesignVariableLabelEntity > criteriaQuery = criteriaBuilder.createQuery( DesignVariableLabelEntity.class );
        Root< DesignVariableLabelEntity > root = criteriaQuery.from( DesignVariableLabelEntity.class );

        List< Predicate > predicates = new ArrayList<>();

        Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
        Root< WorkflowEntity > rootMaxVersionSubQuery = maxVersionSubQuery.from( WorkflowEntity.class );
        maxVersionSubQuery
                .select( criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                workflowId );
        maxVersionSubQuery.where( maxIdPredicate );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder
                .in( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );

        predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                workflowId ) );

        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        DesignVariableLabelEntity designVariableEntity = entityManager.createQuery( criteriaQuery ).getResultList().stream().findFirst()
                .orElse( null );
        return designVariableEntity;
    }

}
