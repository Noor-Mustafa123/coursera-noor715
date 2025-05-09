package de.soco.software.simuspace.suscore.location.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.UserUrlEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

public interface SsfsDAO extends GenericDAO< UserUrlEntity > {

    /**
     * Gets the user url by path.
     *
     * @param entityManager
     *         the entity manager
     * @param userUrlEntity
     *         the user url entity
     *
     * @return the user url by path
     */
    UserUrlEntity getUserUrlByPath( EntityManager entityManager, UserUrlEntity userUrlEntity );

    /**
     * Gets the all user url list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the userId
     * @param locationId
     *         the locationId
     *
     * @return the all user url list
     */
    List< UserUrlEntity > getAllUserUrlList( EntityManager entityManager, String userId, UUID locationId );

}
