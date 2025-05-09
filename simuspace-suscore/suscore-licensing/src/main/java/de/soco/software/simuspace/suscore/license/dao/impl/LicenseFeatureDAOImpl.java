package de.soco.software.simuspace.suscore.license.dao.impl;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.license.dao.LicenseFeatureDAO;

/**
 * The Class LicenseFeatureDAOImpl.
 */
public class LicenseFeatureDAOImpl extends AbstractGenericDAO< LicenseFeatureEntity > implements LicenseFeatureDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLicenseFeature( EntityManager entityManager, LicenseFeatureEntity licenseFeatureEntity ) {
        delete( entityManager, licenseFeatureEntity );
    }

}
