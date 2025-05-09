package de.soco.software.simuspace.suscore.local.daemon.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.soco.software.simuspace.suscore.local.daemon.entity.ComputedDataObjectEntity;

/**
 * The Interface ComputedObjectDAO for sql extracton methods
 *
 * @author noman
 */
public interface ComputedObjectDAO extends CrudRepository< ComputedDataObjectEntity, Integer > {

    /**
     * Find by id.
     *
     * @param id
     *         the id
     *
     * @return the computed data object entity
     */
    ComputedDataObjectEntity findById( String id );

    /**
     * Find by container.
     *
     * @param containerId
     *         the container id
     *
     * @return the list
     */
    List< ComputedDataObjectEntity > findByContainer( String containerId );

    /**
     * Find by name.
     *
     * @param name
     *         the name
     *
     * @return the computed data object entity
     */
    ComputedDataObjectEntity findByName( String name );

    /**
     * Find by path.
     *
     * @param path
     *         the path
     *
     * @return the computed data object entity
     */
    ComputedDataObjectEntity findByPath( String path );

}
