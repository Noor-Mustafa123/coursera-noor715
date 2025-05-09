package de.soco.software.simuspace.suscore.role.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The Interface will be responsible for all the database operations necessary for dealing with the Role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public interface RoleDAO extends GenericDAO< RoleEntity > {

    /**
     * Save role.
     *
     * @param entityManager
     *         the entity manager
     * @param role
     *         the role
     *
     * @return the role
     */
    RoleEntity saveRole( EntityManager entityManager, RoleEntity role );

    /**
     * Read role.
     *
     * @param entityManager
     *         the entity manager
     * @param roleId
     *         the role id
     *
     * @return the role entity
     */
    RoleEntity readRole( EntityManager entityManager, UUID roleId );

    /**
     * Read role.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         the name
     *
     * @return the role entity
     */
    RoleEntity readRole( EntityManager entityManager, String name );

    /**
     * Update role.
     *
     * @param entityManager
     *         the entity manager
     * @param roleEntity
     *         the role entity
     *
     * @return the role entity
     */
    RoleEntity updateRole( EntityManager entityManager, RoleEntity roleEntity );

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