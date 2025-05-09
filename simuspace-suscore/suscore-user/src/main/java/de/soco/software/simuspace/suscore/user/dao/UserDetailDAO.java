package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the User Details\
 *
 * @author Nosheen.Sharif
 */
public interface UserDetailDAO extends GenericDAO< UserDetailEntity > {

    /**
     * To Get User Details By user Id
     *
     * @param id
     *         the id
     *
     * @return UserDetailEntity user detail by id
     */
    UserDetailEntity getUserDetailById( EntityManager entityManager, UUID id );

}
