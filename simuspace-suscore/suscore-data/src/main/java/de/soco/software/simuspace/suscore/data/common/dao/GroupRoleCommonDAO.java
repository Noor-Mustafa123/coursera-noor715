package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the GroupRoleDAO.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface GroupRoleCommonDAO extends GenericDAO< GroupRoleEntity > {

    /**
     * Gets the group role by group id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the group role by group id
     */
    List< GroupRoleEntity > getGroupRoleByGroupId( EntityManager entityManager, UUID id );

}
