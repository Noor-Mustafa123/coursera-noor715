package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.AclSecurityIdentityDAO;

/**
 * This Class lists all users in the system, “Security Id” (SID) is assigned to each user role or group. So SID may correspond to a granted
 * authority such as role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class AclSecurityIdentityDAOImpl extends AbstractGenericDAO< AclSecurityIdentityEntity > implements AclSecurityIdentityDAO {

    @Override
    public AclSecurityIdentityEntity getSecurityIdentityBySid( EntityManager entityManager, UUID sidId ) {
        return getLatestNonDeletedObjectByProperty( entityManager, AclSecurityIdentityEntity.class, ConstantsDAO.SID, sidId );
    }

}
