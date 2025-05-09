/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.license.manager;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.ModuleLicenseDTO;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;

/**
 * The Interface LicenseManager provide validation of license according to the user.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface LicenseManager {

    /**
     * Check if feature allowed to user boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     *
     * @return the boolean
     */
    boolean checkIfFeatureAllowedToUser( EntityManager entityManager, String feature );

    /**
     * Checks if is feature allowed to user.
     *
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return true, if is feature allowed to user
     *
     * @apiNote To be used in service calls only
     */
    boolean isFeatureAllowedToUser( String feature, String userId );

    /**
     * Checks if is feature allowed to user.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     * @param userId
     *         the user id
     *
     * @return true, if is feature allowed to user
     */
    boolean isFeatureAllowedToUser( EntityManager entityManager, String feature, String userId );

    /**
     * Is license added against user for module boolean.
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
    boolean isLicenseAddedAgainstUserForModule( EntityManager entityManager, String feature, String userId );

    /**
     * Gets the license config manager.
     *
     * @return the license config manager
     */
    LicenseConfigurationManager getLicenseConfigManager();

    /**
     * Sets the license config manager.
     *
     * @param licenseConfigManager
     *         the new license config manager
     */
    void setLicenseConfigManager( LicenseConfigurationManager licenseConfigManager );

    /**
     * Gets the allowed locations to user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the allowed locations to user
     */
    int getAllowedLocationsToUser( EntityManager entityManager, String userId );

    /**
     * Checks if is token based license exists.
     *
     * @return true, if is token based license exists
     *
     * @apiNote To be used in service calls only
     */
    boolean isTokenBasedLicenseExists();

    /**
     * Checks if is token based license exists.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return true, if is token based license exists
     */
    boolean isTokenBasedLicenseExists( EntityManager entityManager );

    /**
     * Adds the user in tokenized license map.
     *
     * @param token
     *         the token
     * @param userDto
     *         the user dto
     *
     * @return true, if successful
     */
    SuSCoreSessionDTO addUserInTokenizedLicenseMap( String token, SuSCoreSessionDTO sessionDTO );

    /**
     * Removes the user from tokenized license map.
     *
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    void removeUserFromTokenizedLicenseMap( String token );

    /**
     * Removes the user from tokenized license map.
     *
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    void updateTokenizedLicenseMap( java.util.Map< String, SuSCoreSessionDTO > newMap );

    /**
     * Gets the license user entity list by user id.
     *
     * @param userId
     *         the user id
     *
     * @return the license user entity list by user id
     *
     * @apiNote To be used in service calls only
     */
    List< ModuleLicenseDTO > getLicenseUserEntityListByUserId( String userId );

    /**
     * Compute license and add monitor history.
     */
    void computeLicenseAndAddMonitorHistory();

}