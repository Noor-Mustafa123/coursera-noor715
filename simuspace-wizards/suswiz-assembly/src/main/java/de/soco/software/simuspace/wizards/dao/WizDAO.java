package de.soco.software.simuspace.wizards.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.VariantWizardEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface WizDAO.
 */
public interface WizDAO extends GenericDAO< VariantWizardEntity > {

    /**
     * Gets the recent variant wizard by properties.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param solverType
     *         the solver type
     *
     * @return the recent variant wizard by properties
     */
    VariantWizardEntity getRecentVariantWizardByProperties( EntityManager entityManager, String userId, String solverType );

}
