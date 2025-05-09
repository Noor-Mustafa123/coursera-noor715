package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * Interface to have CRUD operation for user group.
 *
 * @author Nosheen.Sharif
 */
public interface UserGroupDAO extends GenericDAO< GroupEntity > {

    /**
     * Creates the user group.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the group entity
     * @param logs
     *         the logs
     *
     * @return the group entity
     */
    GroupEntity createUserGroup( EntityManager entityManager, GroupEntity groupEntity, List< AuditLogEntity > logs );

    /**
     * Update User Group.
     *
     * @param entityManager
     *         the entity manager
     * @param groupEntity
     *         the group entity
     * @param logs
     *         the logs
     *
     * @return the group entity
     */
    GroupEntity updateUserGroup( EntityManager entityManager, GroupEntity groupEntity, List< AuditLogEntity > logs );

    /**
     * Read User Group By Id.
     *
     * @param entityManager
     *         the entity manager
     * @param groupId
     *         the group id
     *
     * @return the group entity
     */
    GroupEntity readUserGroup( EntityManager entityManager, UUID groupId );

    /**
     * Gets the group and users by group id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the group and users by group id
     */
    GroupEntity getGroupAndUsersByGroupId( EntityManager entityManager, UUID id );

    /**
     * Gets user groups by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userUuid
     *         the user uuid
     *
     * @return the user groups by user id
     */
    List< GroupEntity > getUserGroupsByUserId( EntityManager entityManager, UUID userUuid );

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