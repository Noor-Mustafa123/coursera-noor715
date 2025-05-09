package de.soco.software.simuspace.wizards.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.Date;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.VariantWizardEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.wizards.dao.WizDAO;

/**
 * The Class WizDAOImpl.
 */
public class WizDAOImpl extends AbstractGenericDAO< VariantWizardEntity > implements WizDAO {

    /**
     * Gets the recent variant wizard by properties.
     *
     * @param userId
     *         the user id
     *
     * @return the recent variant wizard by properties
     */
    @Override
    public VariantWizardEntity getRecentVariantWizardByProperties( EntityManager entityManager, String userId, String solverType ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< VariantWizardEntity > criteriaQuery = criteriaBuilder.createQuery( VariantWizardEntity.class );
        Root< VariantWizardEntity > root = criteriaQuery.from( VariantWizardEntity.class );

        Subquery< Date > variantWizardSubQuery = criteriaQuery.subquery( Date.class );
        Root< VariantWizardEntity > rootMaxVersionSubQuery = variantWizardSubQuery.from( VariantWizardEntity.class );
        variantWizardSubQuery.select( criteriaBuilder.greatest( rootMaxVersionSubQuery.< Date > get( ConstantsDAO.CREATED_ON ) ) );
        Predicate solverTypePredicate1 = criteriaBuilder.equal( rootMaxVersionSubQuery.get( "solverType" ), solverType );
        Predicate userIdPredicate1 = criteriaBuilder.equal( rootMaxVersionSubQuery.get( ConstantsDAO.USER_ID ), userId );
        variantWizardSubQuery.where( solverTypePredicate1, userIdPredicate1 );

        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.equal( root.get( ConstantsDAO.CREATED_ON ), variantWizardSubQuery );
        criteriaQuery.where( maxVersionCriteriaQueryPredicate );

        VariantWizardEntity object = entityManager.createQuery( criteriaQuery ).getResultList().stream().findFirst().orElse( null );
        return object;
    }

}
