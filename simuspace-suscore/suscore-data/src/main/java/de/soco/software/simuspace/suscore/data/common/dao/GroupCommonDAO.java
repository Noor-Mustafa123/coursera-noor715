package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * Interface to have some common operation for user group
 */
public interface GroupCommonDAO extends GenericDAO< GroupEntity > {

    /**
     * Gets the active group entity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the active group entity
     */
    GroupEntity getActiveGroupEntity( EntityManager entityManager, UUID id );

    /**
     * Gets the group entity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the group entity
     */
    GroupEntity getGroupEntity( EntityManager entityManager, UUID id );

    /**
     * Create User Group
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the groupEntity
     *
     * @return the groupEntity
     */
    GroupEntity createUserGroup( EntityManager entityManager, GroupEntity groupEntity );

    /**
     * Read user group by name.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         the name
     *
     * @return the group entity
     */
    GroupEntity readUserGroupByName( EntityManager entityManager, String name );

    /**
     * Read user group by S id.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         the name
     *
     * @return the group entity
     */
    GroupEntity readUserGroupBySId( EntityManager entityManager, UUID name );

}
