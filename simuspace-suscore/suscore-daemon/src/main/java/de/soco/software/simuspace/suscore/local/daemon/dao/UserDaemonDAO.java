package de.soco.software.simuspace.suscore.local.daemon.dao;

import org.springframework.data.repository.CrudRepository;

import de.soco.software.simuspace.suscore.local.daemon.entity.UserDaemonEntity;

public interface UserDaemonDAO extends CrudRepository< UserDaemonEntity, Integer > {

    /**
     * Find by id.
     *
     * @param id
     *         the id
     *
     * @return the computed data object entity
     */
    UserDaemonEntity findById( String id );

}
