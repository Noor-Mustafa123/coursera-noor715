package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.data.common.dao.LoginAttemptCommonDAO;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;

/**
 * The type Lifecycle policy account unlock thread.
 */
@Log4j2
public class LifecyclePolicyAccountUnlockThread extends UserThread {

    /**
     * The Entity manager factory.
     */
    private final EntityManagerFactory entityManagerFactory;

    /**
     * The User common manager.
     */
    private final UserCommonManager userCommonManager;

    /**
     * Instantiates a new Lifecycle policy account unlock thread.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     * @param userCommonManager
     *         the user common manager
     */
    public LifecyclePolicyAccountUnlockThread( EntityManagerFactory entityManagerFactory, UserCommonManager userCommonManager ) {
        super();
        this.entityManagerFactory = entityManagerFactory;
        this.userCommonManager = userCommonManager;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            LoginAttemptCommonDAO loginAttemptCommonDAO = userCommonManager.getLoginAttemptCommonDAO();
            var attempts = loginAttemptCommonDAO.getLoginAttemptsForLockedAccounts( entityManager );
            if ( CollectionUtils.isEmpty( attempts ) ) {
                log.info( "No locked accounts found" );
                return;
            }
            for ( var attempt : attempts ) {
                if ( attempt.getUnlockTime() != null && attempt.getUnlockTime().after( new Date() ) ) {
                    log.info( "Account will remain locked for {} until {}", attempt.getUid(), attempt.getUnlockTime() );
                } else {
                    attempt.setAttempts( ConstantsInteger.INTEGER_VALUE_ZERO );
                    attempt.setUnlockTime( null );
                    attempt.setLockTime( null );
                    attempt.setLocked( false );
                    loginAttemptCommonDAO.updateFailedLoginAttempt( entityManager, attempt );
                    var user = userCommonManager.getUserCommonDAO().readUserByUserUid( entityManager, attempt.getUserUid() );
                    if ( user != null ) {
                        user.setStatus( true );
                        userCommonManager.getUserCommonDAO().update( entityManager, user );
                    }
                    log.info( "Account unlocked for {}", attempt.getUserUid() );
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

}
