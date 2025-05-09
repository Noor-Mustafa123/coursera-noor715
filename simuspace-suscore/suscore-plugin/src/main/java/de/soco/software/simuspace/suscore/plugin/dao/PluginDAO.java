package de.soco.software.simuspace.suscore.plugin.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.PluginEntity;

/**
 * The interface Plugin dao.
 */
public interface PluginDAO {

    /**
     * Save plugin plugin entity.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the plugin entity
     */
    PluginEntity savePlugin( EntityManager entityManager, PluginEntity entity );

    /**
     * Gets plugin by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the plugin by id
     */
    PluginEntity getPluginById( EntityManager entityManager, UUID id );

    /**
     * Update plugin.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     */
    void updatePlugin( EntityManager entityManager, PluginEntity entity );

    /**
     * Gets plugin list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the plugin list
     */
    List< PluginEntity > getPluginList( EntityManager entityManager );

}
