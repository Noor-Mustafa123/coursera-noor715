package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface provides the only repository functions which are later used to communicate to the DAO layer and database for getting and
 * manipulating the {@link LicenseUserEntity}.
 *
 * @author M.Nasir.Farooq
 */
public interface LicenseUserDAO extends GenericDAO< LicenseUserEntity > {

    /**
     * Adds the user to module.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseUserEntity
     *         the license user entity
     *
     * @return the license user entity
     */
    LicenseUserEntity addUserToModule( EntityManager entityManager, LicenseUserEntity licenseUserEntity );

    /**
     * Gets the license user entity by module.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntity
     *         the license entity
     *
     * @return the license user entity by module
     */
    List< LicenseUserEntity > getLicenseUserEntitiesByModule( EntityManager entityManager, LicenseEntity licenseEntity );

    /**
     * Returns the list of user ID's attached to a licensing module.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the user I ds attached to module
     */
    Set< UUID > getUserIDsAttachedToModule( EntityManager entityManager, String module );

    /**
     * Gets license user entity by module and user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param module
     *         the module
     *
     * @return the license user entity by module and user
     */
    LicenseUserEntity getLicenseUserEntityByModuleAndUser( EntityManager entityManager, UUID userId, String module );

    /**
     * Gets the license user entities by user.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the license user entities by user
     */
    List< LicenseUserEntity > getLicenseUserEntitiesByUser( EntityManager entityManager, UUID userId );

}
