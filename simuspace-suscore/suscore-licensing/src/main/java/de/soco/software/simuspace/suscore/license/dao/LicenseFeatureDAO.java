package de.soco.software.simuspace.suscore.license.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface provides the only repository functions which are later used to communicate to the DAO layer and database for getting and
 * manipulating the {@link LicenseFeatureEntity}.
 *
 * @author M.Nasir.Farooq
 */
public interface LicenseFeatureDAO extends GenericDAO< LicenseFeatureEntity > {

    /**
     * Delete license feature.
     *
     * @param entityManager
     *         the entity manager
     * @param licenseFeatureEntity
     *         the license feature entity
     */
    void deleteLicenseFeature( EntityManager entityManager, LicenseFeatureEntity licenseFeatureEntity );

}
