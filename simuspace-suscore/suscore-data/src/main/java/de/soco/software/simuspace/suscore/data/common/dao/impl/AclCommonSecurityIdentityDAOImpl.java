package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.common.dao.AclCommonSecurityIdentityDAO;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * Implementation of AclCommonSecurityIdentityDAO interface to provide security identity.
 */
public class AclCommonSecurityIdentityDAOImpl extends AbstractGenericDAO< AclSecurityIdentityEntity >
        implements AclCommonSecurityIdentityDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public AclSecurityIdentityEntity getSecurityIdentityBySid( EntityManager entityManager, UUID sidId ) {
        return getLatestNonDeletedObjectByProperty( entityManager, AclSecurityIdentityEntity.class, ConstantsDAO.SID, sidId );
    }

}
