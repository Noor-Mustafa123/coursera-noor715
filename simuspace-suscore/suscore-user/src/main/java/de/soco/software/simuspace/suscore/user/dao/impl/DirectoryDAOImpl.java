package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.DirectoryDAO;

/**
 * The Class is responsible to communicate with the repository and provide CRUD operations to it.
 *
 * @author M.Nasir.Farooq
 */
public class DirectoryDAOImpl extends AbstractGenericDAO< SuSUserDirectoryEntity > implements DirectoryDAO {

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.user.dao.DirectoryDAO#createDirectory(de.
     * soco.software.simuspace.suscore.user.entity.SuSUserDirectoryEntity)
     */
    @Override
    public SuSUserDirectoryEntity createDirectory( EntityManager entityManager, SuSUserDirectoryEntity directoryEntity ) {
        return save( entityManager, directoryEntity );
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.user.dao.DirectoryDAO#updateDirectory(de.
     * soco.software.simuspace.suscore.user.entity.SuSUserDirectoryEntity)
     */
    @Override
    public SuSUserDirectoryEntity updateDirectory( EntityManager entityManager, SuSUserDirectoryEntity directoryEntity ) {
        return saveOrUpdate( entityManager, directoryEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSUserDirectoryEntity readDirectory( EntityManager entityManager, UUID directoryId ) {
        return getLatestNonDeletedObjectById( entityManager, directoryId );
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, SuSUserDirectoryEntity.class );
    }

}
