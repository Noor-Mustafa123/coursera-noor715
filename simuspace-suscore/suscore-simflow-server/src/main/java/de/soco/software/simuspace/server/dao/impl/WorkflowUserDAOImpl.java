package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.UUID;

import de.soco.software.simuspace.server.dao.WorkflowUserDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * This class will be responsible for all the database operations necessary for dealing with the User.
 *
 * @author Nosheen.Sharif
 */
public class WorkflowUserDAOImpl extends AbstractGenericDAO< UserEntity > implements WorkflowUserDAO {

    @Override
    public UserEntity getUserIdBySimId( EntityManager entityManager, UUID simId ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< UserEntity > criteria = cb.createQuery( UserEntity.class );
        Root< UserEntity > root = criteria.from( UserEntity.class );
        Predicate predicate1 = cb.equal( root.get( ConstantsDAO.ID ), simId );
        criteria.where( predicate1 );
        return entityManager.createQuery( criteria ).getSingleResult();
    }

    @Override
    public boolean isUserExisting( EntityManager entityManager, UUID simId ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteria = cb.createQuery( Long.class );
        Root< UserEntity > root = criteria.from( UserEntity.class );
        criteria.select( cb.count( root ) );
        Predicate predicate1 = cb.equal( root.get( ConstantsDAO.ID ), simId );
        criteria.where( predicate1 );
        Long result = entityManager.createQuery( criteria ).getSingleResult();
        return result > 0;
    }

    @Override
    public UserEntity saveUser( EntityManager entityManager, UserEntity userEntity ) {
        return save( entityManager, userEntity );

    }

}
