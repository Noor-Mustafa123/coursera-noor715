package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * Interface to have some common operation related to security identity.
 */
public interface AclCommonSecurityIdentityDAO extends GenericDAO< AclSecurityIdentityEntity > {

    /**
     * Gets the security identity by.
     *
     * @param entityManager
     *         the entity manager
     * @param sidId
     *         the sid id
     *
     * @return the security identity by
     */
    AclSecurityIdentityEntity getSecurityIdentityBySid( EntityManager entityManager, UUID sidId );

}
