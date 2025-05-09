package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.LoginAttemptCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class LoginAttemptCommonDAOImpl extends AbstractGenericDAO< LoginAttemptEntity > implements LoginAttemptCommonDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginAttemptEntity getFailedLoginAttempts( EntityManager entityManager, String uid ) {
        return getUniqueObjectByProperty( entityManager, LoginAttemptEntity.class, ConstantsDAO.USER_UID, uid, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFailedLoginAttempt( EntityManager entityManager, LoginAttemptEntity loginAttempt ) {
        saveOrUpdate( entityManager, loginAttempt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LoginAttemptEntity > getLoginAttemptsForLockedAccounts( EntityManager entityManager ) {
        return getNonDeletedObjectListByProperty( entityManager, LoginAttemptEntity.class, "locked", true );
    }

}
