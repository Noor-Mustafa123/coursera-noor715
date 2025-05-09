package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface provides the only repository functions which are later used to communicate to the DAO layer and database.
 *
 * @author M.Nasir.Farooq
 */
public interface LicenseConfigurationDAO extends GenericDAO< LicenseEntity > {

    /**
     * Adds the license.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntity
     *         the license entity
     *
     * @return the license entity
     */
    LicenseEntity addLicense( EntityManager entityManager, LicenseEntity licenseEntity );

    /**
     * Gets module license.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     *
     * @return the module license
     */
    LicenseEntity getModuleLicense( EntityManager entityManager, String module );

    /**
     * Gets the module license list.
     *
     * @return the module license list
     */
    List< LicenseEntity > getModuleLicenseList( EntityManager entityManager );

    /**
     * Gets the modules by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the modules by user id
     */
    List< LicenseEntity > getModulesByUserId( EntityManager entityManager, String userId );

    /**
     * Gets the license by module and user.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     * @param userId
     *         the user id
     *
     * @return the license by module and user
     */
    LicenseUserEntity getLicenseByModuleAndUser( EntityManager entityManager, String module, String userId );

    /**
     * Delete module license.
     *
     * @param entityManager
     *         the entity manager
     * @param module
     *         the module
     */
    void deleteModuleLicense( EntityManager entityManager, LicenseEntity module );

    /**
     * Gets license features by license.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseEntity
     *         the license entity
     *
     * @return the license features by license
     */
    List< LicenseFeatureEntity > getLicenseFeaturesByLicense( EntityManager entityManager, LicenseEntity licenseEntity );

    /**
     * Gets license feature.
     *
     * @param entityManager
     *         the entity manager
     * @param feature
     *         the feature
     *
     * @return the license feature
     */
    LicenseFeatureEntity getLicenseFeature( EntityManager entityManager, String feature );

    /**
     * Gets the module license.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the module license
     */
    LicenseEntity getModuleLicenseById( EntityManager entityManager, UUID id );

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

}