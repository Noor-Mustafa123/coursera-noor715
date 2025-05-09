package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;

public interface LoginAttemptCommonDAO {

    /**
     * Checks the failed login attempts that user have done while logging in
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     *
     * @return failed login attempts
     */
    LoginAttemptEntity getFailedLoginAttempts( EntityManager entityManager, String uid );

    /**
     * Update the login attempt of the user
     *
     * @param entityManager
     *         the entity manager
     * @param loginAttempt
     *         the login attempt
     */
    void updateFailedLoginAttempt( EntityManager entityManager, LoginAttemptEntity loginAttempt );

    /**
     * Gets login attempts for locked accounts.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the login attempts for locked accounts
     */
    List< LoginAttemptEntity > getLoginAttemptsForLockedAccounts( EntityManager entityManager );

}
