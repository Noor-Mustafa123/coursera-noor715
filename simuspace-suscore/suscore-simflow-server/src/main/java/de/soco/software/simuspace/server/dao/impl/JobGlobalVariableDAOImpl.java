package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.server.dao.JobGlobalVariableDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobGlobalVariablesEntity;

public class JobGlobalVariableDAOImpl extends AbstractGenericDAO< JobGlobalVariablesEntity > implements JobGlobalVariableDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public JobGlobalVariablesEntity getJobGlobalVariablesByJobEntityAndUserId( EntityManager entityManager, UUID jobEntityId,
            UUID userId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< JobGlobalVariablesEntity > criteriaQuery = criteriaBuilder.createQuery( JobGlobalVariablesEntity.class );
        Root< JobGlobalVariablesEntity > root = criteriaQuery.from( JobGlobalVariablesEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.JOB_ENTITY_ID ), jobEntityId ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.USER_ID ), userId ) );

        criteriaQuery.where( predicates.stream().toArray( Predicate[]::new ) );
        try {
            return entityManager.createQuery( criteriaQuery ).getSingleResult();
        } catch ( NoResultException e ) {
            return null;
        } catch ( Exception e ) {
            throw new SusException( " Error while reading custom flags from previous run ", e );
        }
    }

}
