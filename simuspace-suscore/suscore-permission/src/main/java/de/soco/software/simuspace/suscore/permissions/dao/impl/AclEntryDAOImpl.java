package de.soco.software.simuspace.suscore.permissions.dao.impl;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;

/**
 * This class stores individual permission to each recipient. In this entity we specify what action can be performed on each domain object
 * instance by the desired user, group or role.
 *
 * @author Ahsan Khan
 * @since 2.0
 */
public class AclEntryDAOImpl extends AbstractGenericDAO< AclEntryEntity > implements AclEntryDAO {

    /**
     * The Constant MASK.
     */
    private static final String MASK = "mask";

    /**
     * {@inheritDoc}
     */
    @Override
    public AclEntryEntity getAclEntryEntityByObjectIdAndMaskAndSidId( EntityManager entityManager, UUID objectIdentityId, UUID sidId,
            int mask ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID, objectIdentityId );
        properties.put( ConstantsDAO.SECURITY_IDENTITY_ENTITY_ID, sidId );
        properties.put( MASK, mask );
        return getUniqueObjectByProperties( entityManager, properties, AclEntryEntity.class );
    }

    @Override
    public List< AclEntryEntity > getAclEntryEntityByObjectIdAndMaskAndGivenSidList( EntityManager entityManager, UUID objectIdentityId,
            List< UUID > sidList, int mask ) {
        List< AclEntryEntity > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< AclEntryEntity > criteriaQuery = criteriaBuilder.createQuery( AclEntryEntity.class );
        Root< AclEntryEntity > root = criteriaQuery.from( AclEntryEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( MASK ), mask ) );
        predicates.add( criteriaBuilder.equal( getExpression( root, ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID ), objectIdentityId ) );
        Expression< String > securityIdsExpression = getExpression( root, ConstantsDAO.SECURITY_IDENTITY_ENTITY_ID );
        Predicate securityIdsPredicate = securityIdsExpression.in( sidList );
        predicates.add( securityIdsPredicate );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        list = entityManager.createQuery( criteriaQuery ).setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS )
                .getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< AclEntryEntity > getAclEntryListByObjectIdAndSidId( EntityManager entityManager, UUID objectIdentityId, UUID sidId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID, objectIdentityId );
        properties.put( ConstantsDAO.SECURITY_IDENTITY_ENTITY_ID, sidId );
        return getListByPropertiesJpa( entityManager, properties, AclEntryEntity.class, true );
    }

    /**
     * * {@inheritDoc}
     */
    @Override
    public List< AclEntryEntity > getAclEntryListByObjectId( EntityManager entityManager, UUID objectIdentityId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID, objectIdentityId );
        return getListByPropertiesJpa( entityManager, properties, AclEntryEntity.class, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getAclSecurityIdentityIdsByObjectId( EntityManager entityManager,
            UUID objectIdentityId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID, objectIdentityId );
        List< AclEntryEntity > aceList = getListByPropertiesJpa( entityManager, properties, AclEntryEntity.class, true );
        return aceList.stream().map( aclEntryEntity -> aclEntryEntity.getSecurityIdentityEntity().getId() ).distinct()
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getAclSecurityIdentitySidsByObjectId( EntityManager entityManager,
            UUID objectIdentityId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.OBJECT_IDENTITY_ENTITY_ID, objectIdentityId );
        List< AclEntryEntity > aceList = getListByPropertiesJpa( entityManager, properties, AclEntryEntity.class, true );
        return aceList.stream().map( aclEntryEntity -> aclEntryEntity.getSecurityIdentityEntity().getSid() ).distinct()
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteACEbyAclSecurityIdentityEntity( EntityManager entityManager, UUID objectIdentityId,
            AclSecurityIdentityEntity aclSecurityIdentityEntity ) {
        List< AclEntryEntity > aclEntries = getAclEntryListByObjectId( entityManager, objectIdentityId );
        for ( AclEntryEntity aclEntryEntity : aclEntries ) {
            if ( aclEntryEntity.getSecurityIdentityEntity().getId().equals( aclSecurityIdentityEntity.getId() ) ) {
                delete( entityManager, aclEntryEntity );
            }
        }
    }

}
