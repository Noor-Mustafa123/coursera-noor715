package de.soco.software.simuspace.suscore.role.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.role.dao.GroupRoleDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the Group Role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class GroupRoleDAOImpl extends AbstractGenericDAO< GroupRoleEntity > implements GroupRoleDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GroupRoleEntity > getGroupRoleByRoleId( EntityManager entityManager, UUID id ) {
        return getObjectsByManyToOne( entityManager, GroupRoleEntity.class, "roleEntity", ConstantsDAO.ID, id, true );
    }

    @Override
    public List< GroupRoleEntity > getGroupRoleByGroupId( EntityManager entityManager, UUID id ) {
        return getNonDeletedObjectListByProperty( entityManager, GroupRoleEntity.class, ConstantsDAO.GROUP_ENTITY_ID, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupRoleEntity saveGroupRole( EntityManager entityManager, GroupRoleEntity groupRoleEntity ) {
        return save( entityManager, groupRoleEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GroupRoleEntity > getFilteredGroupsByRole( EntityManager entityManager, UUID roleId, FiltersDTO filter ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.GROUPS_BY_ROLE_ID, roleId );
        return getAllFilteredRecordsByProperties( entityManager, GroupRoleEntity.class, properties, filter );
    }

}
