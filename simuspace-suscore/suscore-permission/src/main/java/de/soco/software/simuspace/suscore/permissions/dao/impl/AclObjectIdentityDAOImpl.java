package de.soco.software.simuspace.suscore.permissions.dao.impl;

import javax.persistence.Cache;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclObjectIdentityDAO;

/**
 * This class stores each and every domain object instance in the system. Each object must have an owner and the owner’s SID (user, group or
 * role)
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class AclObjectIdentityDAOImpl extends AbstractGenericDAO< AclObjectIdentityEntity > implements AclObjectIdentityDAO {

    /**
     * The Constant FINAL_PARENT_OBJECT.
     */
    private static final String FINAL_PARENT_OBJECT = "finalParentObject";

    /**
     * The Constant INHERIT.
     */
    private static final String INHERIT = "inherit";

    /**
     * The Constant OBJECT_IDENTITY_BY_CLASS_ID.
     */
    private static final String OBJECT_IDENTITY_BY_CLASS_ID = "classEntity.id";

    /**
     * The Constant SECURITY_ID.
     */
    private static final String SECURITY_ID = "ownerSid.id";

    /**
     * {@inheritDoc}
     */
    @Override
    public AclObjectIdentityEntity getAclObjectIdentityByClassEntityId( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectByProperty( entityManager, AclObjectIdentityEntity.class, OBJECT_IDENTITY_BY_CLASS_ID, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AclObjectIdentityEntity getAclObjectIdentityByClassEntityIdAndBySecurityIdentityEntityId( EntityManager entityManager,
            UUID classId, UUID sidId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( OBJECT_IDENTITY_BY_CLASS_ID, classId );
        properties.put( SECURITY_ID, sidId );
        properties.put( ConstantsDAO.IS_DELETE, false );
        AclObjectIdentityEntity aclObjectIdentityEntity;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< AclObjectIdentityEntity > criteriaQuery = criteriaBuilder.createQuery( AclObjectIdentityEntity.class );
        Root< AclObjectIdentityEntity > root = criteriaQuery.from( AclObjectIdentityEntity.class );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Map.Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        aclObjectIdentityEntity = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).getResultList().stream().findFirst()
                .orElse( null );
        return aclObjectIdentityEntity;
    }

    @Override
    public List< AclObjectIdentityEntity > getAclObjectIdentitiesByClassId( EntityManager entityManager, UUID classId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( OBJECT_IDENTITY_BY_CLASS_ID, classId );
        properties.put( ConstantsDAO.IS_DELETE, false );
        return getListByPropertiesJpa( entityManager, properties, AclObjectIdentityEntity.class, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< AclObjectIdentityEntity > getChildAclObjectIdentitiesById( EntityManager entityManager, UUID id ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.PARENT_OBJECT_ID, id );
        properties.put( ConstantsDAO.IS_DELETE, false );
        return getListByPropertiesJpa( entityManager, properties, AclObjectIdentityEntity.class, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< AclObjectIdentityEntity > getAclObjectChildTreeFromParentId( EntityManager entityManager, UUID id ) {
        return getAclChildTreeFromParentId( entityManager, id );
    }

    @Override
    public List< AclObjectIdentityEntity > getAclObjectEntityIDsByInheritFalseAndNullFinalPermission( EntityManager entityManager ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( INHERIT, false );
        properties.put( FINAL_PARENT_OBJECT, null );
        return getListByPropertiesJpa( entityManager, properties, AclObjectIdentityEntity.class, true );
    }

    @Override
    public AclObjectIdentityEntity getAclObjectIdentityEntityById( EntityManager entityManager, UUID id ) {
        AclObjectIdentityEntity object;
        EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
        Cache cache = entityManagerFactory.getCache();
        if ( cache.contains( AclObjectIdentityEntity.class, id ) ) {
            object = entityManager.find( AclObjectIdentityEntity.class, id );
        } else {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< AclObjectIdentityEntity > criteriaQuery = criteriaBuilder.createQuery( AclObjectIdentityEntity.class );
            Root< AclObjectIdentityEntity > root = criteriaQuery.from( AclObjectIdentityEntity.class );
            List< Predicate > predicates = new ArrayList<>();
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
            object = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                    .findFirst().orElse( null );
        }
        return object;
    }

}