package de.soco.software.simuspace.suscore.user.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * This interface lists all users in the system, “Security Id” (SID) is assigned to each user role or group. So SID may correspond to a
 * granted authority such as role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface AclSecurityIdentityDAO extends GenericDAO< AclSecurityIdentityEntity > {

    /**
     * Gets security identity by sid.
     *
     * @param entityManager
     *         the entity manager
     * @param sidId
     *         the sid id
     *
     * @return the security identity by sid
     */
    AclSecurityIdentityEntity getSecurityIdentityBySid( EntityManager entityManager, UUID sidId );

}
