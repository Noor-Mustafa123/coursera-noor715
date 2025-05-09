package de.soco.software.simuspace.suscore.role.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.RoleEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.role.dao.RoleDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the Role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class RoleDAOImpl extends AbstractGenericDAO< RoleEntity > implements RoleDAO {

    /**
     * The Constant DELETE_USERS_BY_GRP_ID_HQL .
     */
    private static final String DELETE_GROUPS_BY_ROLE_ID_HQL = "delete from GroupRoleEntity u where u.roleEntity.id= :grpId ";

    /**
     * The Constant GRP_ID_KEY.
     */
    private static final String ROLE_ID_KEY = "grpId";

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleEntity saveRole( EntityManager entityManager, RoleEntity role ) {
        return saveOrUpdate( entityManager, role );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleEntity readRole( EntityManager entityManager, UUID roleId ) {
        return getLatestNonDeletedObjectById( entityManager, roleId );
    }

    @Override
    public RoleEntity updateRole( EntityManager entityManager, RoleEntity roleEntity ) {
        return update( entityManager, roleEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleEntity readRole( EntityManager entityManager, String name ) {
        return getLatestNonDeletedObjectByProperty( entityManager, RoleEntity.class, ConstantsDAO.NAME, name );
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, RoleEntity.class );
    }

}