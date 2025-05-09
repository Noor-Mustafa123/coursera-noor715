package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.UserCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * An implementation class for UserCommonDAOImpl that holds the CRUD operations.
 */
public class UserCommonDAOImpl extends AbstractGenericDAO< UserEntity > implements UserCommonDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity readUserBySId( EntityManager entityManager, UUID sId ) {
        return getLatestNonDeletedObjectByProperty( entityManager, UserEntity.class, ConstantsDAO.SECURITY_IDENTITY_ENTITY_ID, sId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity readUserByUserUid( EntityManager entityManager, String userUid ) {
        return getLatestNonDeletedObjectByProperty( entityManager, UserEntity.class, ConstantsDAO.USER_UID, userUid );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity readUser( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectById( entityManager, id );
    }

}
