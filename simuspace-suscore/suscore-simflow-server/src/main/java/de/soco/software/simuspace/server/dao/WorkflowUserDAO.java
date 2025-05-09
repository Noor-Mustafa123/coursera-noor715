package de.soco.software.simuspace.server.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * to provide User Database CRUD and other user related Operations
 */
public interface WorkflowUserDAO {

    UserEntity getUserIdBySimId( EntityManager entityManager, UUID simId );

    boolean isUserExisting( EntityManager entityManager, UUID simId );

    UserEntity saveUser( EntityManager entityManager, UserEntity userEntity );

}
