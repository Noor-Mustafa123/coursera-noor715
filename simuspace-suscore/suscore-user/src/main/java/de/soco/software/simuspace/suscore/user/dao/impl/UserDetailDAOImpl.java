package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.UserDetailDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the User Detail.
 *
 * @author Nosheen.Sharif
 */
public class UserDetailDAOImpl extends AbstractGenericDAO< UserDetailEntity > implements UserDetailDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetailEntity getUserDetailById( EntityManager entityManager, UUID id ) {
        return getUniqueObjectByProperty( entityManager, UserDetailEntity.class, ConstantsDAO.USER_ENTITY_ID, id, true );

    }

}
