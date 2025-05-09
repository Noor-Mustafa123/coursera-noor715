package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * To perform User Operations.
 */
public interface WorkflowUserManager {

    /**
     * Gets sim user.
     *
     * @param entityManager
     *         the entity manager
     * @param simUserId
     *         the sim user id
     *
     * @return the sim user
     */
    UserEntity getSimUser( EntityManager entityManager, UUID simUserId );

    /**
     * Convert the UserEntity To UserDto
     *
     * @param entity
     *         the entity
     *
     * @return UserDTO user dto
     */
    UserDTO prepareUserDtoFromUserEntity( UserEntity entity );

    /**
     * To convert userDto to userEntity
     *
     * @param user
     *         the user
     *
     * @return the user entity
     */
    UserEntity prepareUserEntity( UserDTO user );

    /**
     * Gets the user.
     *
     * @param id
     *         the id
     *
     * @return the user
     */
    UserDTO getUser( UUID id );

}
