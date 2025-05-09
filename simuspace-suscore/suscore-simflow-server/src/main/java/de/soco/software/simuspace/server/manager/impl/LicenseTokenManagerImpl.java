/*
 *
 */

package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.LicenseTokenManager;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.dto.UserLicenseDTO;

/**
 * The Class is responsible for license token management. It is the responsible to checkout/checkin license also handles the checkout limit
 * from license file on the behalf of user rights i.e if a user is manager he will consume the manager license same with user rights.
 */
public class LicenseTokenManagerImpl extends BaseManager implements LicenseTokenManager {

    /**
     * The Constant for initial user count.
     */
    private static final int INITIAL_USER_COUNT = 0;

    /**
     * The Constant for used manager count.
     */
    private Integer inUsedManagerCount = INITIAL_USER_COUNT;

    /**
     * The Constant for used user count.
     */
    private Integer inUsedUserCount = INITIAL_USER_COUNT;

    /**
     * The checked out license map.
     */
    private Map< String, UserLicenseDTO > checkedOutLicenseMap = new HashMap<>();

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new license token manager impl.
     */
    public LicenseTokenManagerImpl() {
        super();
    }

    /**
     * Init method to checkIn expired tokens.
     */
    public void init() {
        /*
         * Add later
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkManagerPermission( EntityManager entityManager, UUID userId ) {

        boolean manager;
        try {
            manager = isWorkflowManager( entityManager, userId );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            manager = false;
        }

        return manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkInToken( UUID userId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !checkedOutLicenseMap.containsKey( token ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.TOKEN_IS_NOT_ALREADY_CHECKED_OUT ) ),
                        getClass() );
            }
            if ( checkManagerPermission( entityManager, userId ) && ( inUsedManagerCount > INITIAL_USER_COUNT ) ) {
                inUsedManagerCount--;
            } else if ( isWorkflowUser( entityManager, userId ) && ( inUsedUserCount > INITIAL_USER_COUNT ) ) {
                inUsedUserCount--;
            }

            checkedOutLicenseMap.remove( token );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the checked out license map.
     *
     * @return the checked out license map
     */
    @Override
    public Map< String, UserLicenseDTO > getCheckedOutLicenseMap() {
        return checkedOutLicenseMap;
    }

    /**
     * Sets the checked out license map.
     *
     * @param checkedOutLicenseMap
     *         the checked out license map
     */
    @Override
    public void setCheckedOutLicenseMap( Map< String, UserLicenseDTO > checkedOutLicenseMap ) {
        this.checkedOutLicenseMap = checkedOutLicenseMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLicenseDTO verifyLicenseCheckout( String token ) {
        if ( checkedOutLicenseMap.containsKey( token ) ) {
            return checkedOutLicenseMap.get( token );
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.TOKEN_IS_NOT_ALREADY_CHECKED_OUT_PLEASE_RETRY ) ),
                    getClass() );
        }
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
