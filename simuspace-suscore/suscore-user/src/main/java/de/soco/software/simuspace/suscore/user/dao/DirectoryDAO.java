package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.SuSUserDirectoryEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The interface provide blueprint to implement which is responsible to communicate with the repository and provide CRUD operations to it.
 *
 * @author M.Nasir.Farooq
 */
public interface DirectoryDAO extends GenericDAO< SuSUserDirectoryEntity > {

    /**
     * Creates the directory.
     *
     * @param entityManager
     *         the entity manager
     * @param directoryEntity
     *         the directory entity
     *
     * @return the su S user directory entity
     */
    SuSUserDirectoryEntity createDirectory( EntityManager entityManager, SuSUserDirectoryEntity directoryEntity );

    /**
     * Update directory.
     *
     * @param entityManager
     *         the entity manager
     * @param model
     *         the model
     *
     * @return the su S user directory entity
     */
    SuSUserDirectoryEntity updateDirectory( EntityManager entityManager, SuSUserDirectoryEntity model );

    /**
     * Read directory.
     *
     * @param entityManager
     *         the entity manager
     * @param directoryId
     *         the directory id
     *
     * @return the su S user directory entity
     */
    SuSUserDirectoryEntity readDirectory( EntityManager entityManager, UUID directoryId );

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
