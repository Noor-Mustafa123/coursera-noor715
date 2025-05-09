package de.soco.software.simuspace.suscore.user.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.QueryHints;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.user.constants.SusUserConstantQueries;
import de.soco.software.simuspace.suscore.user.dao.UserDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the User.
 */
@Log4j2
public class UserDAOImpl extends AbstractGenericDAO< UserEntity > implements UserDAO {

    /**
     * constant USER_BY_GROUP_ID
     */
    private static final String USER_BY_GROUP_ID = "groups.id";

    /**
     * constant RESTRICTED
     */
    private static final String RESTRICTED = "restricted";

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity createUser( EntityManager entityManager, UserEntity user ) {
        return saveOrUpdate( entityManager, user );
    }

    @Override
    public Boolean isRestrictedUser( EntityManager entityManager, UUID id ) {
        Boolean isRestricted = false;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Boolean > criteriaQuery = criteriaBuilder.createQuery( Boolean.class );
        Root< ? > root = criteriaQuery.from( UserEntity.class );
        criteriaQuery.select( root.get( RESTRICTED ) );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        isRestricted = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return isRestricted;
    }

    @Override
    public List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName ) {
        return getAllPropertyValues( entityManager, propertyName, UserEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity getUserByUid( EntityManager entityManager, String uid ) {
        return getLatestNonDeletedObjectByProperty( entityManager, UserEntity.class, ConstantsDAO.USER_UID, uid );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserExist( EntityManager entityManager, String uid ) {
        return isObjectAlreadyExistsByProperty( entityManager, UserEntity.class, ConstantsDAO.USER_UID, uid );
    }

    @Override
    public UserEntity getUserEntityById( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectByIdAndClazz( entityManager, UserEntity.class, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity updateUserEntity( EntityManager entityManager, UserEntity userEntity, List< AuditLogEntity > logs ) {
        return saveOrUpdate( entityManager, userEntity, logs );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserEntity > getAllUsers( EntityManager entityManager ) {
        return getAllRecords( entityManager, UserEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > getIdOfAllUsers( EntityManager entityManager ) {
        return getIdOfAllRecords( entityManager, UserEntity.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserEntity readUser( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectById( entityManager, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< GroupEntity > getGroupsByUserId( EntityManager entityManager, String id ) {

        Map< String, Object > params = new HashMap<>();
        params.put( ConstantsDAO.USER_ID, UUID.fromString( id ) );

        List< GroupEntity > groups = ( List< GroupEntity > ) query( entityManager, SusUserConstantQueries.USER_BY_GROUPS_HQL_COMPOSED,
                params );

        for ( Iterator< GroupEntity > iterator = groups.iterator(); iterator.hasNext(); ) {
            GroupEntity grp = iterator.next();
            if ( grp.isDelete() ) {
                iterator.remove();
            }
        }

        return groups;

    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public List< GroupEntity > getAllGroups( EntityManager entityManager ) {
        return ( List< GroupEntity > ) getNonDeletedObjectListByClazz( entityManager, GroupEntity.class );
    }

    @Override
    public GroupEntity getGroupAndUsersByGroupId( EntityManager entityManager, UUID groupId ) {
        GroupEntity entity = new GroupEntity();
        Session session = entityManager.unwrap( Session.class );
        try {
            session.getTransaction().begin();

            entity = ( GroupEntity ) session.createCriteria( GroupEntity.class ).add( Restrictions.eq( ConstantsDAO.ID, groupId ) )
                    .add( Restrictions.eq( ConstantsDAO.IS_DELETE, false ) ).uniqueResult();

            session.getTransaction().commit();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        return entity;

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.suscore.user.dao.UserDAO#getUsersByGroupEntity(de.
     * soco.software.simuspace.suscore.data.entity.GroupEntity)
     */
    @Override
    public List< UserEntity > getUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( USER_BY_GROUP_ID, groupEntity.getId() );
        return getListByPropertiesJpa( entityManager, properties, UserEntity.class, true );
    }

    @Override
    public List< UserEntity > getUsersByDirectoryId( EntityManager entityManager, UUID dirId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( "directory.id", dirId );
        return getListByPropertiesJpa( entityManager, properties, UserEntity.class, true );
    }

    @Override
    public List< UserEntity > getFilteredUsersByGroupEntity( EntityManager entityManager, GroupEntity groupEntity, FiltersDTO filter ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( USER_BY_GROUP_ID, groupEntity.getId() );
        return getAllFilteredRecordsByProperties( entityManager, UserEntity.class, properties, filter );
    }

}