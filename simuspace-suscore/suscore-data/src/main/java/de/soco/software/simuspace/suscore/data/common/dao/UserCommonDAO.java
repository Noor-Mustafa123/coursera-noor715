package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * An implementation class for UserCommonDAO that holds the CRUD operations related to EserEntity.
 */
public interface UserCommonDAO extends GenericDAO< UserEntity > {

    /**
     * Read user by S id.
     *
     * @param entityManager
     *         the entity manager
     * @param sId
     *         the s id
     *
     * @return the user entity
     */
    UserEntity readUserBySId( EntityManager entityManager, UUID sId );

    /**
     * Read user by user uid.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     *
     * @return the user entity
     */
    UserEntity readUserByUserUid( EntityManager entityManager, String userUid );

    /**
     * Read user user entity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user entity
     */
    UserEntity readUser( EntityManager entityManager, UUID id );

}
