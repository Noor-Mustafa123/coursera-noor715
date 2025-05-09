package de.soco.software.simuspace.suscore.permissions.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * This interface allow us to uniquely identify any domain object class in the system.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface AclClassDAO extends GenericDAO< AclClassEntity > {

    /**
     * Gets the acl class by qualified name.
     *
     * @param entityManager
     *         the entity manager
     * @param qualifiedNmae
     *         the qualified nmae
     *
     * @return the acl class by qualified name
     */
    AclClassEntity getAclClassByQualifiedName( EntityManager entityManager, String qualifiedNmae );

    /**
     * Update class acl class entity.
     *
     * @param entityManager
     *         the entity manager
     * @param classEntity
     *         the class entity
     *
     * @return the acl class entity
     */
    AclClassEntity updateClass( EntityManager entityManager, AclClassEntity classEntity );

    /**
     * Gets latest non deleted acl class entity by id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the latest non deleted acl class entity by id
     */
    AclClassEntity getLatestNonDeletedAclClassEntityById( EntityManager entityManager, UUID id );

}
