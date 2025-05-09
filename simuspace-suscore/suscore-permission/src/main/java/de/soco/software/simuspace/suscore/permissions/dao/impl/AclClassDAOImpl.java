package de.soco.software.simuspace.suscore.permissions.dao.impl;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AclClassEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclClassDAO;

/**
 * This interface allow us to uniquely identify any domain object class in the system.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class AclClassDAOImpl extends AbstractGenericDAO< AclClassEntity > implements AclClassDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public AclClassEntity getAclClassByQualifiedName( EntityManager entityManager, String qualifiedNmae ) {
        return getLatestNonDeletedObjectByProperty( entityManager, AclClassEntity.class, ConstantsDAO.NAME, qualifiedNmae );
    }

    @Override
    public AclClassEntity updateClass( EntityManager entityManager, AclClassEntity classEntity ) {
        return update( entityManager, classEntity );
    }

    @Override
    public AclClassEntity getLatestNonDeletedAclClassEntityById( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< AclClassEntity > criteriaQuery = criteriaBuilder.createQuery( AclClassEntity.class );
        Root< AclClassEntity > root = criteriaQuery.from( AclClassEntity.class );
        List< Predicate > predicates = new ArrayList<>();

        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        AclClassEntity object = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).getResultList().stream().findFirst()
                .orElse( null );
        return object;
    }

}
