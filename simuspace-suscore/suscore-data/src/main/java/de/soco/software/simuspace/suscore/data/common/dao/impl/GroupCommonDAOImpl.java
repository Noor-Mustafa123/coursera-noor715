package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.GroupCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * Implementation of GroupCommonDAO interface to provide operation for user Group
 */
public class GroupCommonDAOImpl extends AbstractGenericDAO< GroupEntity > implements GroupCommonDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity getActiveGroupEntity( EntityManager entityManager, UUID id ) {
        // fix-me using generic jpa
        return getLatestNonDeletedActiveObjectById( entityManager, id );
    }

    @Override
    public GroupEntity getGroupEntity( EntityManager entityManager, UUID id ) {
        // fix-me using generic jpa
        return getLatestNonDeletedObjectById( entityManager, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity createUserGroup( EntityManager entityManager, GroupEntity groupEntity ) {
        return save( entityManager, groupEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity readUserGroupByName( EntityManager entityManager, String name ) {
        return getLatestNonDeletedObjectByProperty( entityManager, GroupEntity.class, ConstantsDAO.NAME, name );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity readUserGroupBySId( EntityManager entityManager, UUID sId ) {
        return getLatestNonDeletedObjectByProperty( entityManager, GroupEntity.class, ConstantsDAO.SECURITY_IDENTITY_ENTITY_ID, sId );
    }

}
