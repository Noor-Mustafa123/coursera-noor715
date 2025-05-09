package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseConfigurationDAO;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseFeatureEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The repository class to persist the {@link LicenseEntity} in data base.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseConfigurationDAOImpl extends AbstractGenericDAO< LicenseEntity > implements LicenseConfigurationDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseEntity addLicense( EntityManager entityManager, LicenseEntity licenseEntity ) {
        return saveOrUpdate( entityManager, licenseEntity );
    }

    @Override
    public LicenseEntity getModuleLicense( EntityManager entityManager, String module ) {
        return findById( entityManager, module );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseEntity getModuleLicenseById( EntityManager entityManager, UUID id ) {
        return getObjectByProperty( entityManager, LicenseEntity.class, ConstantsDAO.ID, id );
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, LicenseEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LicenseEntity > getModuleLicenseList( EntityManager entityManager ) {
        return findAll( entityManager );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LicenseEntity > getModulesByUserId( EntityManager entityManager, String userId ) {

        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.LICENSEUSER_USERENTITY_COMPOSEDID, userId );

        return getListByProperties( entityManager, properties, LicenseEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseUserEntity getLicenseByModuleAndUser( EntityManager entityManager, String module, String userId ) {
        Map< String, Object > properties = new HashMap<>();

        properties.put( ConstantsDAO.USER_ENTITY_ID, UUID.fromString( userId ) );
        properties.put( ConstantsDAO.LICENSE_ENTITY_MODULE, module );

        return getUniqueObjectByPropertiesAndAlias( entityManager, properties, LicenseUserEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteModuleLicense( EntityManager entityManager, LicenseEntity licenseEntity ) {
        delete( entityManager, licenseEntity );
    }

    @Override
    public List< LicenseFeatureEntity > getLicenseFeaturesByLicense( EntityManager entityManager, LicenseEntity licenseEntity ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.LICENSE_ENTITY, licenseEntity );

        return getListByPropertiesJpa( entityManager, properties, LicenseFeatureEntity.class, true );
    }

    @Override
    public LicenseFeatureEntity getLicenseFeature( EntityManager entityManager, String feature ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.FEATURE, feature );
        return getUniqueObjectByProperties( entityManager, properties, LicenseFeatureEntity.class );
    }

}
