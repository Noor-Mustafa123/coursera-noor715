package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import de.soco.software.simuspace.suscore.data.entity.LoginAttemptEntity;

/**
 * The interface User login attempt dao.
 */
public interface UserLoginAttemptDAO {

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
     * Used to save the login attempt of the user who perform login with invalid login credentials
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     */
    LoginAttemptEntity saveFailedLoginAttempt( EntityManager entityManager, String uid );

    /**
     * Update the login attempt of the user
     *
     * @param entityManager
     *         the entity manager
     * @param loginAttempt
     *         the login attempt
     *
     * @return the login attempt entity
     */
    LoginAttemptEntity updateFailedLoginAttempt( EntityManager entityManager, LoginAttemptEntity loginAttempt );

}
