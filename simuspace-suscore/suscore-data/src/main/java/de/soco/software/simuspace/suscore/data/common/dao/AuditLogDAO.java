/*
 *
 */

package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * An interface that holds the CRUD operations for audit logs.
 *
 * @author Zeeshan jamal
 */
public interface AuditLogDAO extends GenericDAO< AuditLogEntity > {

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

}
