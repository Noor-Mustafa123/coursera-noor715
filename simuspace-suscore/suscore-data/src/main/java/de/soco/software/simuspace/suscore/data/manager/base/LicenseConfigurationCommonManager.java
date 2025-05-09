package de.soco.software.simuspace.suscore.data.manager.base;

import javax.persistence.EntityManager;

public interface LicenseConfigurationCommonManager {

    /**
     * Is feature allowed to user boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return the boolean
     */
    boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId );

}
