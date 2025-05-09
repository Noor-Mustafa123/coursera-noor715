package de.soco.software.simuspace.suscore.role.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the GroupRoleDAO.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface GroupRoleDAO extends GenericDAO< GroupRoleEntity > {

    /**
     * Gets the group role by role id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the group role by role id
     */
    List< GroupRoleEntity > getGroupRoleByRoleId( EntityManager entityManager, UUID id );

    /**
     * Gets group role by group id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the group role by group id
     */
    List< GroupRoleEntity > getGroupRoleByGroupId( EntityManager entityManager, UUID id );

    /**
     * Save group role.
     *
     * @param entityManager
     *         the entity manager
     * @param groupRoleEntity
     *         the group role entity
     *
     * @return the group role entity
     */
    GroupRoleEntity saveGroupRole( EntityManager entityManager, GroupRoleEntity groupRoleEntity );

    /**
     * Gets filtered groups by role.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     * @param filter
     *         the filter
     *
     * @return the filtered groups by role
     */
    List< GroupRoleEntity > getFilteredGroupsByRole( EntityManager entityManager, UUID roleId, FiltersDTO filter );

}
