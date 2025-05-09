package de.soco.software.simuspace.suscore.location.dao.impl;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.QueryHints;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupRoleEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.location.dao.LocationDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the Locations.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class LocationDAOImpl extends AbstractGenericDAO< LocationEntity > implements LocationDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LocationEntity > getAllLocalObjectList( EntityManager entityManager ) {
        List< LocationEntity > list = null;
        Session session = entityManager.unwrap( Session.class );

        try {
            session.beginTransaction();

            ProjectionList proj = Projections.projectionList();
            proj.add( Projections.max( ConstantsDAO.COMPOSED_ID_VERSION_ID ) );
            proj.add( Projections.groupProperty( ConstantsDAO.COMPOSED_ID_ID ) );

            DetachedCriteria detachedCriteria = DetachedCriteria.forClass( LocationEntity.class );

            detachedCriteria.setProjection( proj );

            list = session.createCriteria( LocationEntity.class ).add( Restrictions.eq( ConstantsDAO.IS_DELETE, false ) )
                    .add( Restrictions.eq( ConstantsDAO.IS_INTERNAL, true ) ).list();

            session.getTransaction().commit();

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return list;
    }

    @Override
    public LocationEntity getLatestNonDeletedLocationById( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< LocationEntity > criteriaQuery = criteriaBuilder.createQuery( LocationEntity.class );
        Root< LocationEntity > root = criteriaQuery.from( LocationEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        LocationEntity object = entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).getResultList().stream().findFirst()
                .orElse( null );
        return object;
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, LocationEntity.class );
    }

    @Override
    public List< LocationEntity > getAllFilteredNonDeletedLocationsWithPermissions( EntityManager entityManager, FiltersDTO filtersDTO,
            String userId, int permission ) {
        var entityClazz = LocationEntity.class;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< LocationEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< LocationEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( entityClazz, filtersDTO, criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserRoleIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, paginatedFilteredCriteria,
                    filteredCriteriaRoot, entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< LocationEntity > list = entityManager.createQuery( paginatedFilteredCriteria )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords(
                getFilteredCountForLocation( entityManager, criteriaBuilder, filtersDTO, entityClazz, userId, permission ) );
        filtersDTO.setTotalRecords(
                getCountWithParentIdAndPermissionForLocation( entityClazz, userId, entityManager, criteriaBuilder, permission ) );
        return list;
    }

    /**
     * Gets user role id list.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user role id list
     */
    private List< UUID > getUserRoleIdList( EntityManager entityManager, UUID id ) {
        List< GroupEntity > groupEntities = getGroupsByUserId( entityManager, id );
        List< UUID > ids = groupEntities.stream().map( GroupEntity::getId ).collect( Collectors.toList() );
        List< UUID > aclSidIds = new ArrayList<>();
        if ( CollectionUtils.isNotEmpty( ids ) ) {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< GroupRoleEntity > groupRoleEntityCriteriaQuery = criteriaBuilder.createQuery( GroupRoleEntity.class );
            Root< GroupRoleEntity > groupRoleRoot = groupRoleEntityCriteriaQuery.from( GroupRoleEntity.class );
            List< Predicate > rolePredicates = new ArrayList<>();
            for ( var groupId : ids ) {
                rolePredicates.add( criteriaBuilder.equal( groupRoleRoot.join( "groupEntity" ).get( "id" ), groupId ) );
            }
            groupRoleEntityCriteriaQuery.where( criteriaBuilder.or( rolePredicates.toArray( Predicate[]::new ) ) );
            List< GroupRoleEntity > groupRoleEntities = entityManager.createQuery( groupRoleEntityCriteriaQuery ).getResultList();
            aclSidIds = groupRoleEntities.stream()
                    .map( groupRoleEntity -> groupRoleEntity.getRoleEntity().getSecurityIdentityEntity().getId() )
                    .collect( Collectors.toSet() ).stream().toList();

        }
        return aclSidIds;
    }

    private long getFilteredCountForLocation( EntityManager entityManager, CriteriaBuilder criteriaBuilder, FiltersDTO filtersDTO,
            Class< ? > entityClazz, String userId, int permission ) {
        try {
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
            criteriaQuery.select( criteriaBuilder.count( filteredCriteriaRoot ) );
            List< Predicate > predicates = new ArrayList<>();
            preparePredicate( entityClazz, filtersDTO, null, userId, null, null, null, criteriaBuilder, criteriaQuery, filteredCriteriaRoot,
                    predicates, null, null );
            if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                List< UUID > userSecurityIds = getUserRoleIdList( entityManager, UUID.fromString( userId ) );
                Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery,
                        filteredCriteriaRoot, entityClazz );
                Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
                predicates.add( maskPredicate );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        }
    }

    private Long getCountWithParentIdAndPermissionForLocation( Class< ? > entityClazz, String userId, EntityManager entityManager,
            CriteriaBuilder criteriaBuilder, int permission ) {
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();

        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserRoleIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery, root,
                    entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

}
