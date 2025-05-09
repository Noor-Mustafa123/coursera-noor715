package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.GroupRoleCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class GroupRoleCommonDAOImpl extends AbstractGenericDAO< GroupRoleEntity > implements GroupRoleCommonDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GroupRoleEntity > getGroupRoleByGroupId( EntityManager entityManager, UUID id ) {
        return getNonDeletedObjectListByProperty( entityManager, GroupRoleEntity.class, ConstantsDAO.GROUP_ENTITY_ID, id );
    }

}
