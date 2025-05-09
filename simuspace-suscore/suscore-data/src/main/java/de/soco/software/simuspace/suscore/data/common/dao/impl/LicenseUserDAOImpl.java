package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LicenseUserDAO;
import de.soco.software.simuspace.suscore.data.entity.LicenseEntity;
import de.soco.software.simuspace.suscore.data.entity.LicenseUserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Interface provides the only repository functions which are later used to communicate to the DAO layer and database for getting and
 * manipulating the {@link LicenseUserEntity}.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseUserDAOImpl extends AbstractGenericDAO< LicenseUserEntity > implements LicenseUserDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public LicenseUserEntity addUserToModule( EntityManager entityManager, LicenseUserEntity licenseUserEntity ) {
        return save( entityManager, licenseUserEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LicenseUserEntity > getLicenseUserEntitiesByModule( EntityManager entityManager, LicenseEntity licenseEntity ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.LICENSE_ENTITY, licenseEntity );
        return getListByPropertiesJpa( entityManager, properties, LicenseUserEntity.class, true );
    }

    @Override
    public LicenseUserEntity getLicenseUserEntityByModuleAndUser( EntityManager entityManager, UUID userId, String module ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.LICENSE_ENTITY, new LicenseEntity( module ) );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( userId );
        properties.put( ConstantsDAO.USER_ENTITY, userEntity );
        return getUniqueObjectByProperties( entityManager, properties, LicenseUserEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LicenseUserEntity > getLicenseUserEntitiesByUser( EntityManager entityManager, UUID userId ) {
        Map< String, Object > properties = new HashMap<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId( userId );
        properties.put( ConstantsDAO.USER_ENTITY, userEntity );
        return getListByPropertiesJpa( entityManager, properties, LicenseUserEntity.class, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set< UUID > getUserIDsAttachedToModule( EntityManager entityManager, String module ) {
        List< LicenseUserEntity > licensedUsers = getObjectListByProperty( entityManager, ConstantsDAO.LICENSE_ENTITY_MODULE, module );
        Set< UUID > userIDsSet = new HashSet<>();
        for ( LicenseUserEntity licenseUserEntity : licensedUsers ) {
            userIDsSet.add( licenseUserEntity.getUserEntity().getId() );
        }
        return userIDsSet;
    }

}
