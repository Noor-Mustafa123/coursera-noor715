package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * An implementation class for auditLogDAO that holds the CRUD operations for audit Logs.
 *
 * @author Zeeshan jamal
 */
public class AuditLogDAOImpl extends AbstractGenericDAO< AuditLogEntity > implements AuditLogDAO {

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, AuditLogEntity.class );
    }

}
