package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.server.dao.WorkflowUserDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * Implemenation Class of UserManager which will be responsible to communicate to UserDao
 */
public class WorkflowUserManagerImpl extends BaseManager implements WorkflowUserManager {

    /**
     * The constructor which instantiates an object of the class
     */
    public WorkflowUserManagerImpl() {
        super();
    }

    /**
     * userDao reference for user database operation
     */
    private WorkflowUserDAO userDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO prepareUserDtoFromUserEntity( UserEntity entity ) {
        final UserDTO user = new UserDTO();
        user.setId( entity.getId().toString() );
        user.setFirstName( entity.getFirstName() );
        user.setSurName( entity.getSurName() );
        user.setUserUid( entity.getUserUid() );
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity prepareUserEntity( UserDTO user ) {
        final UserEntity entity = new UserEntity();
        entity.setId( UUID.fromString( user.getId() ) );
        entity.setUserUid( user.getUserUid() );
        return entity;
    }

    @Override
    public UserEntity getSimUser( EntityManager entityManager, UUID simUserId ) {
        final boolean isUserExist = userDao.isUserExisting( entityManager, simUserId );
        UserEntity user;
        if ( !isUserExist ) {
            // create a New User in databank

            final UserEntity entity = new UserEntity();
            entity.setId( UUID.randomUUID() );
            user = userDao.saveUser( entityManager, entity );

        } else {
            // GEt User by simId From databank
            user = userDao.getUserIdBySimId( entityManager, simUserId );
        }

        return user;
    }
    // *********************** GETTERS/SETTERS *************************

    @Override
    public UserDTO getUser( UUID id ) {
        return null;
    }

    /**
     * Gets the user dao.
     *
     * @return the user dao
     */
    public WorkflowUserDAO getUserDao() {
        return userDao;
    }

    /**
     * Sets the user dao.
     *
     * @param userDao
     *         the new user dao
     */
    public void setUserDao( WorkflowUserDAO userDao ) {
        this.userDao = userDao;
    }

}
