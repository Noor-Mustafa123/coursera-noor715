/**
 *
 */

package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.dao.UserGroupDAO;

/**
 * Implementation of UserGroupDAO interface to provide CRUD operation for user Group
 *
 * @author Nosheen.Sharif
 */
public class UserGroupDAOImpl extends AbstractGenericDAO< GroupEntity > implements UserGroupDAO {

    /**
     * The Constant USER_ALIAS.
     */
    private static final String USER_ALIAS = "users.id";

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.user.dao.UserGroupDAO#createUserGroup(
     * de.soco.software.simuspace.suscore.data.entity.GroupEntity)
     */
    @Override
    public GroupEntity createUserGroup( EntityManager entityManager, GroupEntity groupEntity, List< AuditLogEntity > logs ) {

        return save( entityManager, groupEntity, logs );
    }

    @Override
    public GroupEntity updateUserGroup( EntityManager entityManager, GroupEntity groupEntity, List< AuditLogEntity > logs ) {
        return saveOrUpdate( entityManager, groupEntity, logs );
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.user.dao.UserGroupDAO#readUserGroup(
     * java.util.UUID)
     */
    @Override
    public GroupEntity readUserGroup( EntityManager entityManager, UUID groupId ) {
        return getLatestNonDeletedObjectById( entityManager, groupId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupEntity getGroupAndUsersByGroupId( EntityManager entityManager, UUID id ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< GroupEntity > criteria = cb.createQuery( GroupEntity.class ).distinct( true );
        Root< GroupEntity > root = criteria.from( GroupEntity.class );
        Predicate predicate1 = cb.equal( root.get( ConstantsDAO.ID ), id );
        Predicate predicate2 = cb.equal( root.get( ConstantsDAO.IS_DELETE ), false );
        criteria.where( predicate1, predicate2 );
        GroupEntity groupEntity = entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
        return groupEntity;
    }

    @Override
    public List< GroupEntity > getUserGroupsByUserId( EntityManager entityManager, UUID userUuid ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( USER_ALIAS, userUuid );
        properties.put( ConstantsDAO.IS_DELETE, false );
        properties.put( ConstantsDAO.STATUS, true );
        return getListByPropertiesJpa( entityManager, properties, GroupEntity.class, true );
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, GroupEntity.class );
    }

}