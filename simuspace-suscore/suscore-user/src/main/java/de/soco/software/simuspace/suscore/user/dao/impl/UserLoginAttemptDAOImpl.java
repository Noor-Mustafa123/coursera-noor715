package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.UserLoginAttemptDAO;

/**
 * A class that would hold the database operations for user login attempts
 *
 * @param <T>
 *
 * @author Zeeshan
 */

public class UserLoginAttemptDAOImpl< T > extends AbstractGenericDAO< LoginAttemptEntity > implements UserLoginAttemptDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginAttemptEntity getFailedLoginAttempts( EntityManager entityManager, String uid ) {
        return getUniqueObjectByProperty( entityManager, LoginAttemptEntity.class, ConstantsDAO.USER_UID, uid );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginAttemptEntity saveFailedLoginAttempt( EntityManager entityManager, String uid ) {
        LoginAttemptEntity loginAttempt = new LoginAttemptEntity();
        loginAttempt.setUuid( UUID.randomUUID() );
        loginAttempt.setUid( uid );
        loginAttempt.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
        loginAttempt.setLdapAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
        loginAttempt.setLastMofied( new Date() );
        return saveOrUpdate( entityManager, loginAttempt );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginAttemptEntity updateFailedLoginAttempt( EntityManager entityManager, LoginAttemptEntity loginAttempt ) {
        return saveOrUpdate( entityManager, loginAttempt );
    }

}