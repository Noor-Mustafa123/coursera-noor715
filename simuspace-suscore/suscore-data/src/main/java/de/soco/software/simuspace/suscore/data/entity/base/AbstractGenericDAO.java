package de.soco.software.simuspace.suscore.data.entity.base;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.Query;
import org.hibernate.sql.JoinType;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDate;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDateFilters;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.SuSFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectType;
import de.soco.software.simuspace.suscore.common.model.SchemeCategoryDTO;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;

/**
 * The Class AbstractGenericDAO.
 *
 * @param <E>
 *         the element type
 */
@Log4j2
@SuppressWarnings( "unchecked" )
public abstract class AbstractGenericDAO< E > implements GenericDAO< E > {

    /**
     * The Constant INVALID_OPERATION.
     */
    public static final int INVALID_OPERATION = -1;

    /**
     * The entity class.
     */
    private final Class< E > entityClass;

    /**
     * Instantiates a new abstract generic DAO.
     */
    public AbstractGenericDAO() {
        this.entityClass = ( Class< E > ) ( ( ParameterizedType ) this.getClass().getGenericSuperclass() ).getActualTypeArguments()[ 0 ];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E findById( EntityManager entityManager, final Serializable id ) {
        return entityManager.find( this.entityClass, id );
    }

    /**
     * Gets the entity with details.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the entity with details
     */
    public E getEntityWithDetails( EntityManager entityManager, Serializable id ) {
        try {
            return entityManager.find( this.entityClass, id );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Find by composite id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param compositId
     *         the composit id
     *
     * @return the e
     */
    public E findByCompositeId( EntityManager entityManager, Class< ? > clazz, VersionPrimaryKey compositId ) {
        try {
            return ( E ) entityManager.find( clazz, compositId );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E save( EntityManager entityManager, E entity ) {
        entityManager.getTransaction().begin();
        E e = entityManager.merge( entity );
        entityManager.getTransaction().commit();
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E save( EntityManager entityManager, E entity, List< ? > logs ) {
        try {
            entityManager.getTransaction().begin();
            E e = entityManager.merge( entity );
            if ( CollectionUtils.isNotEmpty( logs ) ) {
                for ( Object auditLogEntity : logs ) {
                    entityManager.merge( auditLogEntity );
                    entityManager.flush();
                }
            }
            entityManager.getTransaction().commit();
            return e;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Save relation.
     *
     * @param entityManager
     *         the entity manager
     * @param r
     *         the r
     *
     * @return the relation
     */
    public Relation saveRelation( EntityManager entityManager, Relation r ) {
        try {
            entityManager.getTransaction().begin();
            Relation relation = entityManager.merge( r );
            entityManager.getTransaction().commit();
            return relation;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll( EntityManager entityManager, List< E > entities ) {
        try {
            for ( E e : entities ) {
                save( entityManager, e );
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( EntityManager entityManager, E entity ) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove( entityManager.contains( entity ) ? entity : entityManager.merge( entity ) );
            entityManager.getTransaction().commit();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E saveOrUpdate( EntityManager entityManager, E entity ) {
        Session session = entityManager.unwrap( Session.class );
        session.beginTransaction();
        session.saveOrUpdate( entity );
        session.getTransaction().commit();
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveOrUpdateBulk( EntityManager entityManager, List< E > entity ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            entity.forEach( session::saveOrUpdate );
            session.getTransaction().commit();
            return true;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Save or update.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param childList
     *         the child list
     *
     * @return the e
     */
    public E saveOrUpdate( EntityManager entityManager, E entity, List< ? > childList ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            session.saveOrUpdate( entity );
            if ( CollectionUtils.isNotEmpty( childList ) ) {
                for ( Object childEntity : childList ) {
                    session.saveOrUpdate( childEntity );
                    session.flush();
                }
            }
            session.getTransaction().commit();
            return entity;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E update( EntityManager entityManager, E entity ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            session.update( entity );
            session.getTransaction().commit();
            return entity;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E merge( EntityManager entityManager, E entity ) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge( entity );
            entityManager.getTransaction().commit();
            return entity;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( EntityManager entityManager, E entity ) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove( entityManager.merge( entity ) );
            entityManager.getTransaction().commit();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBulk( EntityManager entityManager, List< E > entities ) {
        try {
            entityManager.getTransaction().begin();
            entities.forEach( e -> entityManager.remove( entityManager.merge( e ) ) );
            entityManager.getTransaction().commit();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > findAll( EntityManager entityManager ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< E > criteria = cb.createQuery( this.entityClass );
        Root< E > root = criteria.from( this.entityClass );
        criteria.select( root );
        return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getAllLifeCycleStatusIds( EntityManager entityManager ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< String > criteria = cb.createQuery( String.class );
        Root< SuSEntity > root = criteria.from( SuSEntity.class );
        criteria.select( root.get( ConstantsDAO.LIFECYCLE_STATUS ) );
        criteria.distinct( true );
        List< String > list = entityManager.createQuery( criteria ).getResultList();
        return list;
    }

    /**
     * Delete relations.
     *
     * @param entityManager
     *         the entity manager
     * @param r
     *         the r
     */
    public void deleteRelations( EntityManager entityManager, Relation r ) {
        try {
            entityManager.getTransaction().begin();
            entityManager.refresh( entityManager.merge( r ) );
            entityManager.remove( entityManager.merge( r ) );
            entityManager.getTransaction().commit();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the all records with desc order.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param columnToSort
     *         the column to sort
     *
     * @return the all records with desc order
     */
    public List< E > getAllRecordsWithDescOrder( EntityManager entityManager, Class< ? > clazz, String columnToSort ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteria, root, clazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        criteria.where( predicates.toArray( Predicate[]::new ) );
        criteria.orderBy( criteriaBuilder.desc( root.get( columnToSort ) ) );
        return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * Gets all records.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     *
     * @return the all records
     */
    public List< E > getAllRecords( EntityManager entityManager, Class< E > clazz ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< E > criteria = cb.createQuery( clazz );
        Root< E > root = criteria.from( clazz );
        criteria.select( root );
        criteria.distinct( true );
        return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * Gets the id of all records.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     *
     * @return the id of all records
     */
    public List< UUID > getIdOfAllRecords( EntityManager entityManager, Class< E > clazz ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< E > criteria = cb.createQuery( clazz );
        Root< E > root = criteria.from( clazz );
        criteria.select( root.get( ConstantsDAO.ID ) );
        criteria.distinct( true );
        return ( List< UUID > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * Gets the su S object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the su S object by id
     */
    public E getSuSObjectById( EntityManager entityManager, Class< ? > clazz, Serializable id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        Predicate predicate = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
        criteria.where( predicate );
        return ( E ) entityManager.createQuery( criteria ).getSingleResult();
    }

    /**
     * Gets the simple object by id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the simple object by id
     */
    public E getSimpleObjectById( EntityManager entityManager, Class< ? > clazz, Serializable id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        Predicate predicate = criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id );
        criteria.where( predicate );
        return ( E ) entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
    }

    /**
     * Gets the relation list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the relation list by property
     */
    public List< Relation > getRelationListByProperty( EntityManager entityManager, String propertyName, Object value ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< Relation > criteria = cb.createQuery( Relation.class );
        Root< Relation > root = criteria.from( Relation.class );
        Predicate predicate1 = cb.equal( getExpression( root, propertyName ), value );
        criteria.orderBy( cb.desc( root.get( propertyName ) ) );
        criteria.where( predicate1 );
        return entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * Gets the object by composite id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param clazz
     *         the clazz
     * @param versionId
     *         the version id
     *
     * @return the object by composite id
     */
    public E getObjectByCompositeId( EntityManager entityManager, UUID id, Class< ? > clazz, Integer versionId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
        predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ), versionId ) );
        criteria.where( predicates.toArray( Predicate[]::new ) );
        return ( E ) entityManager.createQuery( criteria ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > query( EntityManager entityManager, String hsql, Map< String, Object > params ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            Query< E > query = session.createQuery( hsql );
            if ( params != null ) {
                for ( Entry< String, Object > i : params.entrySet() ) {
                    query.setParameter( i.getKey(), i.getValue() );
                }
            }
            List< E > result = null;
            if ( ( hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.DELETE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.UPDATE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.INSERT_OPERATION ) == INVALID_OPERATION ) ) {
                result = query.list();
            } else if ( ( hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.DELETE_OPERATION )
                    == ConstantsInteger.INTEGER_VALUE_ZERO ) ) {
                query.executeUpdate();
            }
            session.getTransaction().commit();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Query with filter.
     *
     * @param entityManager
     *         the entity manager
     * @param hsql
     *         the hsql
     * @param params
     *         the params
     * @param filter
     *         the filter
     *
     * @return the list
     */
    public List< E > queryWithFilter( EntityManager entityManager, String hsql, Map< String, Object > params, FiltersDTO filter ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            Query< E > query = session.createQuery( hsql );
            query.setFirstResult( filter.getStart() );
            query.setMaxResults( filter.getLength() );
            if ( params != null ) {
                for ( Entry< String, Object > i : params.entrySet() ) {
                    query.setParameter( i.getKey(), i );
                }
            }
            List< E > result = null;
            if ( ( hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.DELETE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.UPDATE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.INSERT_OPERATION ) == INVALID_OPERATION ) ) {
                result = query.list();
            } else if ( ( hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.DELETE_OPERATION )
                    == ConstantsInteger.INTEGER_VALUE_ZERO ) ) {
                query.executeUpdate();
            }
            if ( result != null ) {
                filter.setFilteredRecords( ( long ) result.size() );
            }
            session.getTransaction().commit();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E queryUniqueObject( EntityManager entityManager, String hsql, Map< String, Object > params ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            Query< E > query = session.createQuery( hsql );
            if ( params != null ) {
                for ( Entry< String, Object > i : params.entrySet() ) {
                    query.setParameter( i.getKey(), i );
                }
            }
            Object result = null;
            if ( ( hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.DELETE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.UPDATE_OPERATION ) == INVALID_OPERATION ) && (
                    hsql.toUpperCase().indexOf( ConstantsDbOperationTypes.INSERT_OPERATION ) == INVALID_OPERATION ) ) {
                result = query.uniqueResult();
            }
            session.getTransaction().commit();
            return ( E ) result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the unique object by properties and alias.
     *
     * @param <T>
     *         the generic type
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     *
     * @return the unique object by properties and alias
     */
    public < T > T getUniqueObjectByPropertiesAndAlias( EntityManager entityManager, Map< String, Object > properties, Class< T > clazz ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteria = cb.createQuery( clazz );
            Root< ? > root = criteria.from( clazz );
            criteria.distinct( true );
            T object;
            List< Predicate > predicates = new ArrayList<>();
            for ( Entry< String, Object > pair : properties.entrySet() ) {
                predicates.add( cb.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
            }
            criteria.where( predicates.toArray( Predicate[]::new ) );
            object = ( T ) entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
            return object;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the list by properties jpa.
     *
     * @param <T>
     *         the generic type
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     * @param useCache
     *         the use cache
     *
     * @return the list by properties jpa
     */
    public < T > List< T > getListByPropertiesJpa( EntityManager entityManager, Map< String, Object > properties, Class< T > clazz,
            Boolean useCache ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< T > list;
        if ( useCache ) {
            list = ( List< T > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        } else {
            list = ( List< T > ) entityManager.createQuery( criteriaQuery )
                    .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).getResultList();
        }
        return list;
    }

    /**
     * Gets list by properties.
     *
     * @param <T>
     *         the type parameter
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     *
     * @return the list by properties
     */
    public < T > List< T > getListByProperties( EntityManager entityManager, Map< String, Object > properties, Class< T > clazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< T > list = ( List< T > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return list;
    }

    /**
     * Delete list by properties.
     *
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     */
    public void deleteListByProperties( EntityManager entityManager, Map< String, Object > properties, Class< E > clazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( criteriaQuery ).getResultList();

        deleteBulk( entityManager, list );
    }

    /**
     * Gets the list by properties descending order.
     *
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     * @param orderByProperty
     *         the order by property
     *
     * @return the list by properties descending order
     */
    public List< E > getListByPropertiesDescendingOrder( EntityManager entityManager, Map< String, Object > properties, Class< ? > clazz,
            String orderByProperty ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.orderBy( criteriaBuilder.desc( root.get( orderByProperty ) ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return list;
    }

    /**
     * Gets the list by properties aescending order.
     *
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     * @param orderByProperty
     *         the order by property
     *
     * @return the list by properties aescending order
     */
    public List< E > getListByPropertiesAescendingOrder( EntityManager entityManager, Map< String, Object > properties, Class< ? > clazz,
            String orderByProperty ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.orderBy( criteriaBuilder.asc( root.get( orderByProperty ) ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getObjectByCompositeKey( EntityManager entityManager, VersionPrimaryKey versionPrimaryKey ) {
        try {
            entityManager.getTransaction().begin();
            E result = entityManager.find( this.entityClass, versionPrimaryKey );
            entityManager.getTransaction().commit();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCount( EntityManager entityManager, Class< ? > clazz, boolean isDelete ) {
        Long result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > root = criteriaQuery.from( clazz );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
                predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), isDelete ) );
                predicates.add( criteriaBuilder.notEqual( getExpression( root, ConstantsDAO.COMPOSED_ID_ID ),
                        UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) ) );
                predicates.add( criteriaBuilder.notEqual( getExpression( root, ConstantsDAO.COMPOSED_ID_ID ),
                        UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) ) );
                criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            }
            result = ( Long ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the count with permissions by jpa.
     *
     * @param entityClazz
     *         the entity clazz
     * @param userId
     *         the user id
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param getUserJobsOnly
     *         the get user jobs only
     *
     * @return the count with permissions by jpa
     */
    protected Long getCountWithPermissionsByJpa( Class< ? > entityClazz, UUID userId, EntityManager entityManager,
            CriteriaBuilder criteriaBuilder, Boolean getUserJobsOnly ) {
        Long result = null;
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( getUserJobsOnly ) {
            // get result matching created by
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ), userId ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            predicates.add( criteriaBuilder.notEqual( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) ) );
            predicates.add( criteriaBuilder.notEqual( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) ) );
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.JOB_RELATION_TYPE ) ) {
                Predicate jobRelationPredicate = criteriaBuilder.or( criteriaBuilder.isNull( root.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                        criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), 1 ) );
                predicates.add( jobRelationPredicate );
            }
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, entityClazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        result = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return result;
    }

    /**
     * Gets the count with permissions by jpa.
     *
     * @param entityClazz
     *         the entity clazz
     * @param userId
     *         the user id
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param getUserJobsOnly
     *         the get user jobs only
     * @param workflowId
     *         the workflow id
     *
     * @return the count with permissions by jpa
     */
    private Long getCountWithPermissionsByJpa( Class< ? > entityClazz, UUID userId, EntityManager entityManager,
            CriteriaBuilder criteriaBuilder, Boolean getUserJobsOnly, String workflowId ) {
        Long result = null;
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( getUserJobsOnly ) {
            // get result matching created by
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ), userId ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            predicates.add( criteriaBuilder.notEqual( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) ) );
            predicates.add(
                    criteriaBuilder.equal( root.join( ConstantsDAO.WORKFLOW ).join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                            UUID.fromString( workflowId ) ) );
            predicates.add( criteriaBuilder.notEqual( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) ) );
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.JOB_RELATION_TYPE ) ) {
                Predicate jobRelationPredicate = criteriaBuilder.or( criteriaBuilder.isNull( root.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                        criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), 1 ) );
                predicates.add( jobRelationPredicate );
            }
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, entityClazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        result = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return result;
    }

    /**
     * Checks if is object alreadyt exists by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param property
     *         the property
     * @param value
     *         the value
     *
     * @return true, if is object alreadyt exists by property
     */
    public boolean isObjectAlreadyExistsByProperty( EntityManager entityManager, Class< ? > clazz, String property, Object value ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > root = criteriaQuery.from( clazz );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            Long result;
            Predicate predicate1 = criteriaBuilder.equal( getExpression( root, property ), value );
            criteriaQuery.where( predicate1 );
            result = entityManager.createQuery( criteriaQuery ).getSingleResult();
            return result > ConstantsInteger.INTEGER_VALUE_ZERO;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Checks if is object already exists by properties.
     *
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     *
     * @return true, if is object already exists by properties
     */
    public boolean isObjectAlreadyExistsByProperties( EntityManager entityManager, Map< String, Object > properties, Class< ? > clazz ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > root = criteriaQuery.from( clazz );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            List< Predicate > predicates = new ArrayList<>();
            Iterator< ? > iterator = properties.entrySet().iterator();
            Long count;
            while ( iterator.hasNext() ) {
                Map.Entry< String, Object > pair = ( Map.Entry< String, Object > ) iterator.next();
                predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            count = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
            return count > ConstantsInteger.INTEGER_VALUE_ZERO;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestNonDeletedObjectById( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( this.entityClass );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
            maxVersionSubQuery.where( maxIdPredicate );
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.ID ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        E object = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getLatestNonDeletedObjectsByIds( EntityManager entityManager, List< UUID > ids ) {
        return getLatestNonDeletedObjectsByListOfId( entityManager, ids );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllLatestObjectsByListOfIdsAndClazz( EntityManager entityManager, List< UUID > listOfIds, Class< ? > clazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            listOfIds.forEach(
                    id -> idPredicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, this.entityClass );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            listOfIds.forEach( id -> idPredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            List< E > listOfObjects = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
            return listOfObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestObjectByIdWithLifeCycle( EntityManager entityManager, UUID id, UUID userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus ) {
        if ( userId.toString().equals( ConstantsID.SUPER_USER_ID ) ) {
            return getLatestNonDeletedObjectById( entityManager, id );
        }
        E object;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        List< Predicate > predicates = new ArrayList<>();
        List< Predicate > subPredicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( this.entityClass );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                id );
        subPredicates.add( maxIdPredicate );
        Predicate ownerVisibleStatusAndUserIsOwnerPredicate = criteriaBuilder.conjunction();
        Predicate anyVisibleStatusAndUserIsOwnerPredicate = criteriaBuilder.conjunction();
        Predicate ownerVisibleStatusPredicate = criteriaBuilder.in( rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS ) )
                .value( ownerVisibleStatus );
        Predicate userIsOwnerPredicate = criteriaBuilder.equal( getExpression( rootMaxVersionSubQuery, ConstantsDAO.OWNER_ID ), userId );
        ownerVisibleStatusAndUserIsOwnerPredicate.getExpressions().add( ownerVisibleStatusPredicate );
        ownerVisibleStatusAndUserIsOwnerPredicate.getExpressions().add( userIsOwnerPredicate );
        Predicate anyVisibleStatusPredicate = criteriaBuilder.in( rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS ) )
                .value( anyVisibleStatus );
        Predicate userIsNotOwnerPredicate = criteriaBuilder.notEqual( getExpression( rootMaxVersionSubQuery, ConstantsDAO.OWNER_ID ),
                userId );
        anyVisibleStatusAndUserIsOwnerPredicate.getExpressions().add( anyVisibleStatusPredicate );
        anyVisibleStatusAndUserIsOwnerPredicate.getExpressions().add( userIsNotOwnerPredicate );
        Predicate ownerVisibleOrAnyVisiblePredicate = criteriaBuilder.disjunction();
        ownerVisibleOrAnyVisiblePredicate.getExpressions().add( ownerVisibleStatusAndUserIsOwnerPredicate );
        ownerVisibleOrAnyVisiblePredicate.getExpressions().add( anyVisibleStatusAndUserIsOwnerPredicate );
        Predicate and = criteriaBuilder.conjunction();
        and.getExpressions().add( ownerVisibleOrAnyVisiblePredicate );
        subPredicates.add( and );
        maxVersionSubQuery.where( subPredicates.toArray( Predicate[]::new ) );
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );

        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        object = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestNonDeletedActiveObjectById( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( this.entityClass );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.STATUS ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.STATUS ), true ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        E object = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
        return object;
    }

    /**
     * Gets the unique object by properties.
     *
     * @param <T>
     *         the generic type
     * @param entityManager
     *         the entity manager
     * @param properties
     *         the properties
     * @param clazz
     *         the clazz
     *
     * @return the unique object by properties
     */
    public < T > T getUniqueObjectByProperties( EntityManager entityManager, Map< String, Object > properties, Class< T > clazz ) {
        T object;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        object = ( T ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestNonDeletedObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( clazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( getExpression( rootMaxVersionSubQuery, propertyName ), value );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        predicates.add( criteriaBuilder.equal( getExpression( root, propertyName ), value ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        E object = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getLatestNonDeletedObjectListByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName,
            Object value ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, SuSEntity.class );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            predicates.add( criteriaBuilder.not( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID )
                    .in( UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.MASTER_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.HIDDEN_PROJECT_ID_FOR_SCHEME_PLOTTING ) ) ) );
        }
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( propertyName.contains( "." ) ) {
            String[] parts = propertyName.split( "\\." );
            predicates.add( criteriaBuilder.equal( root.join( parts[ 0 ] ).get( parts[ 1 ] ), value ) );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( propertyName ), value ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > object = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getLatestNonDeletedObjectListByNotNullProperty( EntityManager entityManager, Class< ? > clazz, String propertyName ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, SuSEntity.class );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            predicates.add( criteriaBuilder.isNotNull( root.get( propertyName ) ) );
            predicates.add( criteriaBuilder.not( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID )
                    .in( UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.MASTER_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.HIDDEN_PROJECT_ID_FOR_SCHEME_PLOTTING ) ) ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > object = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return object;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getNonDeletedObjectList( EntityManager entityManager ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< E > criteria = cb.createQuery( this.entityClass );
            Root< E > root = criteria.from( this.entityClass );
            criteria.distinct( true );
            Predicate predicate1 = cb.equal( root.get( ConstantsDAO.IS_DELETE ), false );
            criteria.where( predicate1 );
            List< E > objectList = entityManager.createQuery( criteria ).getResultList();
            return objectList;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the non deleted object list by clazz.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     *
     * @return the non deleted object list by clazz
     */
    public List< ? > getNonDeletedObjectListByClazz( EntityManager entityManager, Class< ? > clazz ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteria = cb.createQuery( clazz );
            Root< ? > root = criteria.from( clazz );
            criteria.distinct( true );
            Predicate predicate1 = cb.equal( root.get( ConstantsDAO.IS_DELETE ), false );
            criteria.where( predicate1 );
            List< ? > objectList = ( List< ? > ) entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
            return objectList;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestObjectById( EntityManager entityManager, Class< ? > clazz, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( clazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        }
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        E object = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
        return object;
    }

    /**
     * Gets latest non deleted object by id and clazz.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the latest non deleted object by id and clazz
     */
    public E getLatestNonDeletedObjectByIdAndClazz( EntityManager entityManager, Class< ? > clazz, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( clazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        }
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestObjectByVersionAndStatus( EntityManager entityManager, Class< ? > clazz, UUID id, int versionId, String status ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( clazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
            Predicate versionIDPredicate = criteriaBuilder.lessThanOrEqualTo(
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ), versionId );
            Predicate statusPredicate = criteriaBuilder.equal( rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS ), status );
            maxVersionSubQuery.where( maxIdPredicate, versionIDPredicate, statusPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) );
        }
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                .findFirst().orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getObjectListByProperty( EntityManager entityManager, String propertyName, Object value ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( this.entityClass );
        Root< ? > root = criteria.from( this.entityClass );
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.ITEM_ORDER ) ) {
            criteria.orderBy( cb.asc( root.get( ConstantsDAO.ITEM_ORDER ) ) );
        }
        Predicate predicate1 = cb.equal( getExpression( root, propertyName ), value );
        criteria.where( predicate1 );
        return ( List< E > ) entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        criteria.distinct( true );
        Predicate predicate1 = cb.equal( getExpression( root, propertyName ), value );
        criteria.where( predicate1 );
        return ( E ) entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllVersionsOfObjectById( EntityManager entityManager, UUID id ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteria = cb.createQuery( this.entityClass );
            Root< ? > root = criteria.from( this.entityClass );
            criteria.distinct( true );
            Predicate predicate1 = cb.equal( getExpression( root, ConstantsDAO.COMPOSED_ID_ID ), id );
            criteria.where( predicate1 );
            return ( List< E > ) entityManager.createQuery( criteria ).getResultList().stream().findFirst().orElse( null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllObjectList( EntityManager entityManager ) {
        List< Predicate > predicates = new ArrayList<>();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< E > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
            Root< E > root = criteriaQuery.from( this.entityClass );
            criteriaQuery.select( root );
            criteriaQuery.distinct( true );
            if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
                Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
                Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( this.entityClass );
                maxVersionSubQuery.select(
                        criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
                Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                        root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
                predicates.add( maxVersionCriteriaQueryPredicate );
                criteriaQuery.groupBy( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            }
            if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.IS_DELETE ) ) {
                predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            return entityManager.createQuery( criteriaQuery ).getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getUniqueObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value, boolean cache ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        Predicate predicate1 = cb.equal( getExpression( root, propertyName ), value );
        criteria.where( predicate1 );
        return ( E ) entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, cache ).getResultList().stream().findFirst()
                .orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getUniqueObjectByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value ) {
        return getUniqueObjectByProperty( entityManager, clazz, propertyName, value, true );
    }

    /**
     * Gets the non deleted object list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the non deleted object list by property
     */
    public List< E > getNonDeletedObjectListByProperty( EntityManager entityManager, Class< ? > clazz, String propertyName, Object value ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( clazz );
        Root< ? > root = criteria.from( clazz );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( cb.equal( getExpression( root, propertyName ), value ) );
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( cb.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteria.where( predicates.toArray( Predicate[]::new ) );
        return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * Gets objects by many to one.
     *
     * @param entityManager
     *         the entity manager
     * @param parent
     *         the parent
     * @param childObject
     *         the child object
     * @param childAttribute
     *         the child attribute
     * @param value
     *         the value
     * @param isDeleteFlag
     *         the is delete flag
     *
     * @return the objects by many to one
     */
    public List< E > getObjectsByManyToOne( EntityManager entityManager, Class< ? > parent, String childObject, String childAttribute,
            Object value, boolean isDeleteFlag ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteria = cb.createQuery( parent );
        Root< ? > root = criteria.from( parent );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( cb.equal( root.join( childObject ).get( childAttribute ), value ) );
        if ( isDeleteFlag ) {
            predicates.add( cb.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteria.where( predicates.toArray( Predicate[]::new ) );
        return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllDeletedObjects( EntityManager entityManager, Class< ? > entityClazz, UUID userId, FiltersDTO filtersDTO ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), true ) );
        if ( null != userId && ReflectionUtils.hasField( entityClazz, ConstantsDAO.DELETED_BY ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.DELETED_BY ).get( ConstantsDAO.ID ), userId ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getAllDeletedObjectCount( entityManager, entityClazz, filtersDTO, true, userId ) );
        filtersDTO.setTotalRecords( getAllDeletedObjectCount( entityManager, entityClazz, filtersDTO, false, userId ) );
        return list;
    }

    /**
     * Gets the all deleted object count.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param isFilterCount
     *         the is filter count
     * @param userId
     *         the user id
     *
     * @return the all deleted object count
     */
    private Long getAllDeletedObjectCount( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            boolean isFilterCount, UUID userId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( isFilterCount ) {
            addFilterPredicatesToListOfPredicates( criteriaBuilder, root, entityClazz, filtersDTO, null, predicates );
        }
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), true ) );
        if ( null != userId && ReflectionUtils.hasField( entityClazz, ConstantsDAO.DELETED_BY ) ) {
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.DELETED_BY ).get( ConstantsDAO.ID ), userId ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsByProperties( EntityManager entityManager, Class< ? > entityClazz,
            Map< String, Object > properties, FiltersDTO filtersDTO ) {
        List< E > list;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = getAllFilteredRecordsPredicates( entityClazz, filtersDTO, criteriaBuilder,
                    paginatedFilteredCriteria, filteredCriteriaRoot );
            for ( Entry< String, Object > pair : properties.entrySet() ) {
                predicates.add( criteriaBuilder.equal( getExpression( filteredCriteriaRoot, pair.getKey() ), pair.getValue() ) );
            }
            addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                    .setMaxResults( filtersDTO.getLength() ).getResultList();
            filtersDTO.setFilteredRecords( getAllFilteredRecordsCount( entityManager, entityClazz, properties, filtersDTO ) );
            filtersDTO.setTotalRecords( getAllRecordsCount( entityManager, entityClazz, properties ) );
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return list;
    }

    /**
     * Gets the all filtered records count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param properties
     *         the properties
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records count
     */
    public Long getAllFilteredRecordsCount( EntityManager entityManager, Class< ? > clazz, Map< String, Object > properties,
            FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( clazz, filtersDTO, criteriaBuilder, criteriaQuery, root );
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the all filtered records count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all filtered records count
     */
    public Long getAllFilteredRecordsCount( EntityManager entityManager, Class< ? > clazz, FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( clazz, filtersDTO, criteriaBuilder, criteriaQuery, root );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the all records count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     *
     * @return the all records count
     */
    protected Long getAllRecordsCount( EntityManager entityManager, Class< ? > clazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        attachRecordPredicates( clazz, criteriaBuilder, criteriaQuery, root, predicates );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the all records count.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param properties
     *         the properties
     *
     * @return the all records count
     */
    protected Long getAllRecordsCount( EntityManager entityManager, Class< ? > clazz, Map< String, Object > properties ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        for ( Entry< String, Object > pair : properties.entrySet() ) {
            predicates.add( criteriaBuilder.equal( getExpression( root, pair.getKey() ), pair.getValue() ) );
        }
        attachRecordPredicates( clazz, criteriaBuilder, criteriaQuery, root, predicates );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the all records count with object id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param objectId
     *         the object id
     *
     * @return the all records count with object id
     */
    protected Long getAllRecordsCountWithObjectId( EntityManager entityManager, Class< ? > clazz, UUID objectId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( objectId != null ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.OBJECT_ID ), objectId.toString() ) );
        }
        attachRecordPredicates( clazz, criteriaBuilder, criteriaQuery, root, predicates );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets filtered records count with object id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param objectId
     *         the object id
     * @param filtersDTO
     *         the filters dto
     *
     * @return the filtered records count with object id
     */
    protected Long getFilteredRecordsCountWithObjectId( EntityManager entityManager, Class< ? > clazz, UUID objectId,
            FiltersDTO filtersDTO ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( clazz, filtersDTO, criteriaBuilder, criteriaQuery, root );
        if ( objectId != null ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.OBJECT_ID ), objectId.toString() ) );
        }
        attachRecordPredicates( clazz, criteriaBuilder, criteriaQuery, root, predicates );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).getSingleResult();
    }

    /**
     * Gets the all filtered records predicates.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     *
     * @return the all filtered records predicates
     */
    protected List< Predicate > getAllFilteredRecordsPredicates( Class< ? > entityClazz, FiltersDTO filtersDTO,
            CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot ) {
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        attachRecordPredicates( entityClazz, criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot, predicates );
        return predicates;
    }

    /**
     * Attach record predicates.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param predicates
     *         the predicates
     */
    protected void attachRecordPredicates( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot, List< Predicate > predicates ) {
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate predicateRestoreSystemWFId = criteriaBuilder.notEqual(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) );
            predicates.add( predicateRestoreSystemWFId );
            Predicate predicateDeleteSystemWFId = criteriaBuilder.notEqual(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) );
            predicates.add( predicateDeleteSystemWFId );
            Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsWithPermissionsByJpa( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID userId, int permission, Boolean getUserJobsOnly ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        getFilteredPredicatesWithPermissionApplied( entityClazz, filtersDTO, criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot, predicates );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        if ( getUserJobsOnly ) {
            // get result matching created by
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID ), userId ) );
        }
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( count( entityManager, criteriaBuilder, filtersDTO, entityClazz ) );
        filtersDTO.setTotalRecords( getCountWithPermissionsByJpa( entityClazz, userId, entityManager, criteriaBuilder, getUserJobsOnly ) );
        return list;
    }

    /**
     * Gets the filtered predicates with permission applied.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param predicates
     *         the predicates
     */
    private void getFilteredPredicatesWithPermissionApplied( Class< ? > entityClazz, FiltersDTO filtersDTO, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot, List< Predicate > predicates ) {
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate predicateRestoreSystemWFId = criteriaBuilder.notEqual(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ) );
            predicates.add( predicateRestoreSystemWFId );
            Predicate predicateDeleteSystemWFId = criteriaBuilder.notEqual(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ) );
            predicates.add( predicateDeleteSystemWFId );
            Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.JOB_RELATION_TYPE ) ) {
            Predicate jobRelationPredicate = criteriaBuilder.or(
                    criteriaBuilder.isNull( filteredCriteriaRoot.get( ConstantsDAO.JOB_RELATION_TYPE ) ),
                    criteriaBuilder.notEqual( filteredCriteriaRoot.get( ConstantsDAO.JOB_RELATION_TYPE ), 1 ) );
            predicates.add( jobRelationPredicate );
        }
    }

    /**
     * Count.
     *
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param filtersDTO
     *         the filters DTO
     * @param entityClazz
     *         the entity clazz
     *
     * @return the long
     */
    protected long count( EntityManager entityManager, CriteriaBuilder criteriaBuilder, FiltersDTO filtersDTO, Class< ? > entityClazz ) {
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        getFilteredPredicatesWithPermissionApplied( entityClazz, filtersDTO, criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot, predicates );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllRecordsWithParentByJpa( EntityManager entityManager, Class< ? > entityClazz, UUID parentId ) {
        List< E > list;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
                Predicate isDeletedPredicate = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false );
                predicates.add( isDeletedPredicate );
            }
            if ( null != parentId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
                if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                    predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                            .value( relationSubquery ) );
                } else {
                    predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.ID ) ).value( relationSubquery ) );
                }
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( entityClazz, criteriaBuilder, criteriaQuery,
                        filteredCriteriaRoot );
                predicates.add( maxVersionCriteriaQueryPredicate );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            list = ( List< E > ) entityManager.createQuery( criteriaQuery ).getResultList();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Adds the composite key.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     *
     * @return the predicate
     */
    protected Predicate addCompositeKey( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot ) {
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionSubQuery.where( maxIdPredicate );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        return maxVersionCriteriaQueryPredicate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllRecordsWithParent( EntityManager entityManager, Class< ? > entityClazz, UUID parentId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                    .value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( entityClazz, criteriaBuilder, criteriaQuery,
                    filteredCriteriaRoot );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.ID ), parentId ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( criteriaQuery ).getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsByObjectId( EntityManager entityManager, Class< ? > entityClazz, UUID objectId,
            FiltersDTO filtersDTO ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( entityClazz, filtersDTO, criteriaBuilder, criteriaQuery,
                filteredCriteriaRoot );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.OBJECT_ID ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.OBJECT_ID ), objectId.toString() ) );
        }
        if ( SuSFeaturesEnum.AUDIT.getType().equals( entityClazz.getName() ) ) {
            String columnName = ConstantsDAO.OBJECT_NAME;
            Path< String > expression = getExpression( filteredCriteriaRoot, columnName );
            predicates.add( criteriaBuilder.like( criteriaBuilder.lower( expression ),
                    ConstantsDAO.SQL_LIKE_PARAMETER + filtersDTO.getSearch() + ConstantsDAO.SQL_LIKE_PARAMETER ) );
        }
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, criteriaQuery, filtersDTO );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        list = ( List< E > ) entityManager.createQuery( criteriaQuery )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getFilteredRecordsCountWithObjectId( entityManager, entityClazz, objectId, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCountWithObjectId( entityManager, entityClazz, objectId ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsByObjectIds( EntityManager entityManager, Class< ? > entityClazz, List< UUID > objectIds,
            FiltersDTO filtersDTO, List< ObjectType > objectTypeList ) {
        List< E > list = new ArrayList<>();
        if ( CollectionUtils.isEmpty( objectIds ) ) {
            filtersDTO.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ZERO );
            filtersDTO.setTotalRecords( ( long ) ConstantsInteger.INTEGER_VALUE_ZERO );
            return list;
        }
        try {
            entityManager.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList,
                    predicates );
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                Predicate hasFieldComposedIdPredicate = filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID )
                        .in( objectIds );
                predicates.add( hasFieldComposedIdPredicate );
            }
            Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
            maxVersionSubQuery.where( maxIdPredicate );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            filtersDTO.setFilteredRecords( ( long ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList().size() );
            filtersDTO.setTotalRecords( getCount( entityManager, entityClazz, false ) );
            if ( filtersDTO.getLength() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
                list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult(
                        filtersDTO.getStart() < ConstantsInteger.INTEGER_VALUE_ZERO ? ConstantsInteger.INTEGER_VALUE_ZERO
                                : filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();
            } else {
                list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList(); // for non paginated tables
            }
            entityManager.getTransaction().commit();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsByObjectIdAndVersionId( EntityManager entityManager, Class< ? > entityClazz, UUID objectId,
            int versionId, FiltersDTO filtersDTO ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( entityClazz, filtersDTO, criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.OBJECT_ID ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.OBJECT_ID ), objectId.toString() ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.OBJECT_VERSION_ID ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.OBJECT_VERSION_ID ), versionId ) );
        }
        if ( SuSFeaturesEnum.AUDIT.getType().equals( entityClazz.getName() ) ) {
            String columnName = ConstantsDAO.OBJECT_NAME;
            Path< String > expression = getExpression( filteredCriteriaRoot, columnName );
            predicates.add( criteriaBuilder.like( criteriaBuilder.lower( expression ),
                    ConstantsDAO.SQL_LIKE_PARAMETER + filtersDTO.getSearch() + ConstantsDAO.SQL_LIKE_PARAMETER ) );
        }
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getFilteredRecordsCountWithObjectId( entityManager, entityClazz, objectId, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCountWithObjectId( entityManager, entityClazz, objectId ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredVersionsById( EntityManager entityManager, Class< ? > entityClazz, UUID id, FiltersDTO filtersDTO,
            UUID userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
        }
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria )
                .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords( getFilteredVersionsCountById( entityManager, entityClazz, filtersDTO, id ) );
        filtersDTO.setTotalRecords( getAllVersionsCountById( entityManager, entityClazz, id ) );
        return list;
    }

    /**
     * Gets the all versions count by id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param id
     *         the id
     *
     * @return the all versions count by id
     */
    protected Long getAllVersionsCountById( EntityManager entityManager, Class< ? > clazz, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
        }
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            return entityManager.createQuery( criteriaQuery ).getResultList().stream().findFirst().orElse( null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the filtered versions count by id.
     *
     * @param entityManager
     *         the entity manager
     * @param clazz
     *         the clazz
     * @param filtersDTO
     *         the filters DTO
     * @param id
     *         the id
     *
     * @return the filtered versions count by id
     */
    protected Long getFilteredVersionsCountById( EntityManager entityManager, Class< ? > clazz, FiltersDTO filtersDTO, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.select( criteriaBuilder.count( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, root, clazz, filtersDTO, null, predicates );
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
        }
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        criteriaQuery.groupBy( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        try {
            return entityManager.createQuery( criteriaQuery ).getResultList().stream().findFirst().orElse( null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsWithParent( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID parentId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        addRelationAndApplyCompositeId( entityClazz, parentId, criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot,
                predicates );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        filtersDTO.setFilteredRecords( getAllFilteredRecordCount( entityManager, criteriaBuilder, entityClazz, filtersDTO, parentId ) );
        filtersDTO.setTotalRecords( getAllTotalRecordCount( entityManager, criteriaBuilder, entityClazz, parentId ) );
        return list;
    }

    /**
     * Adds the relation and apply composite id.
     *
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param predicates
     *         the predicates
     */
    protected void addRelationAndApplyCompositeId( Class< ? > entityClazz, UUID parentId, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot, List< Predicate > predicates ) {
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            if ( null != parentId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, paginatedFilteredCriteria );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                        .value( relationSubquery ) );
            }
            Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            maxVersionSubQuery.where( criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ) );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            if ( null != parentId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, paginatedFilteredCriteria );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.get( ConstantsDAO.ID ) ).value( relationSubquery ) );
            }
        }
    }

    /**
     * Gets the latest workflow entity version as sub query.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     *
     * @return the latest workflow entity version as sub query
     */
    protected Subquery< Integer > getLatestWorkflowEntityVersionAsSubQuery( CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot ) {
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< WorkflowEntity > rootMaxVersionSubQuery = maxVersionSubQuery.from( WorkflowEntity.class );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal(
                filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionSubQuery.where( maxIdPredicate );
        return maxVersionSubQuery;
    }

    /**
     * Gets the all filtered record count.
     *
     * @param emf
     *         the emf
     * @param criteriaBuilder
     *         the criteria builder
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     *
     * @return the all filtered record count
     */
    protected Long getAllFilteredRecordCount( EntityManager emf, CriteriaBuilder criteriaBuilder, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId ) {
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        addRelationAndApplyCompositeId( entityClazz, parentId, criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot,
                predicates );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return emf.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets the all filtered record count by workflow id and user id.
     *
     * @param emf
     *         the emf
     * @param criteriaBuilder
     *         the criteria builder
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the all filtered record count by workflow id and user id
     */
    protected Long getAllFilteredRecordCountByWorkflowIdAndUserId( EntityManager emf, CriteriaBuilder criteriaBuilder,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID workflowId, UUID userId ) {
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        predicates.add( criteriaBuilder.equal(
                filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), workflowId ) );
        Subquery< Integer > maxVersionSubQuery = getLatestWorkflowEntityVersionAsSubQuery( criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );
        predicates.add( criteriaBuilder.in(
                        filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery ) );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.USER_ID ), userId ) );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return emf.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets the all total record count.
     *
     * @param emf
     *         the emf
     * @param criteriaBuilder
     *         the criteria builder
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     *
     * @return the all total record count
     */
    protected Long getAllTotalRecordCount( EntityManager emf, CriteriaBuilder criteriaBuilder, Class< ? > entityClazz, UUID parentId ) {
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        addRelationAndApplyCompositeId( entityClazz, parentId, criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot,
                predicates );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return emf.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets the total records count by workflow id and user id.
     *
     * @param emf
     *         the emf
     * @param criteriaBuilder
     *         the criteria builder
     * @param entityClazz
     *         the entity clazz
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the total records count by workflow id and user id
     */
    protected Long getTotalRecordsCountByWorkflowIdAndUserId( EntityManager emf, CriteriaBuilder criteriaBuilder, Class< ? > entityClazz,
            UUID workflowId, UUID userId ) {
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        predicates.add( criteriaBuilder.equal(
                filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), workflowId ) );
        Subquery< Integer > maxVersionSubQuery = getLatestWorkflowEntityVersionAsSubQuery( criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );
        predicates.add( criteriaBuilder.in(
                        filteredCriteriaRoot.join( ConstantsDAO.WORKFLOW ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) )
                .value( maxVersionSubQuery ) );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.USER_ID ), userId ) );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return emf.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getAllFilteredRecordCountWithParentId( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            UUID parentId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
        List< Predicate > predicates = new ArrayList<>();
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, null, predicates );
        addRelationAndApplyCompositeId( entityClazz, parentId, criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot,
                predicates );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( paginatedFilteredCriteria ).getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsWithParentAndLifeCycle( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypes ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypes, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot, predicates );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords(
                getFilteredCount( entityManager, criteriaBuilder, filtersDTO, entityClazz, parentId, userId, ownerVisibleStatus,
                        anyVisibleStatus, objectTypes ) );
        filtersDTO.setTotalRecords(
                getCountWithParentId( entityClazz, parentId, userId, ownerVisibleStatus, anyVisibleStatus, entityManager,
                        criteriaBuilder ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsWithParentAndLifeCycleAndPermission( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypes, int permission ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypes, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot, predicates );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, paginatedFilteredCriteria,
                    filteredCriteriaRoot, entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< E > list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords(
                getFilteredCount( entityManager, criteriaBuilder, filtersDTO, entityClazz, parentId, userId, ownerVisibleStatus,
                        anyVisibleStatus, objectTypes, null, permission ) );
        filtersDTO.setTotalRecords(
                getCountWithParentIdAndPermission( entityClazz, parentId, userId, ownerVisibleStatus, anyVisibleStatus, entityManager,
                        criteriaBuilder, permission ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsWithParentAndLifeCycleAndPermissionWithoutCount( EntityManager entityManager,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, int permission, List< ObjectType > objectTypes ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > root = criteriaQuery.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypes, criteriaBuilder,
                criteriaQuery, root, predicates );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, root, criteriaQuery, filtersDTO );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery, root,
                    entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecords( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO ) {
        return getAllFilteredRecords( entityManager, entityClazz, filtersDTO, Boolean.FALSE );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecords( EntityManager entityManager, Class< ? > entityClazz, FiltersDTO filtersDTO,
            Boolean isCacheEnabled ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( entityClazz, filtersDTO, criteriaBuilder, paginatedFilteredCriteria,
                filteredCriteriaRoot );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< E > list;
        if ( isCacheEnabled ) {
            list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, Boolean.TRUE )
                    .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();
        } else {
            list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria )
                    .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                    .setMaxResults( filtersDTO.getLength() ).getResultList();
        }
        filtersDTO.setFilteredRecords( getAllFilteredRecordsCount( entityManager, entityClazz, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCount( entityManager, entityClazz ) );
        return list;
    }

    /**
     * Prepare predicate.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypeList
     *         the object type list
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param predicates
     *         the predicates
     */
    protected void preparePredicate( Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, List< ObjectType > objectTypeList,
            CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > paginatedFilteredCriteria, Root< ? > filteredCriteriaRoot,
            List< Predicate > predicates ) {
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypeList, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot, predicates, null, null );
    }

    /**
     * Prepare predicate.
     *
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypeList
     *         the object type list
     * @param criteriaBuilder
     *         the criteria builder
     * @param criteriaQuery
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param predicates
     *         the predicates
     * @param joinTranslation
     *         the join translation
     * @param language
     *         the language
     */
    protected void preparePredicate( Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, List< ObjectType > objectTypeList,
            CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > criteriaQuery, Root< ? > filteredCriteriaRoot, List< Predicate > predicates,
            Join< ?, TranslationEntity > joinTranslation, String language ) {

        if ( SuSEntity.class.isAssignableFrom( entityClazz ) && language != null ) {
            predicates.add( criteriaBuilder.or( criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.LANGUAGE ), language ),
                    criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.LANGUAGE ) ) ) );
        }
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList, predicates,
                joinTranslation );
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                    .value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        removeSystemWorkflows( entityClazz, predicates, criteriaBuilder, filteredCriteriaRoot );
        if ( SuSEntity.class.isAssignableFrom( entityClass ) ) {
            Subquery< Integer > maxVersionSubQuery;
            if ( null != parentId && ( parentId.toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) || parentId.toString()
                    .equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) ) {
                maxVersionSubQuery = getMaxVersionCriteriaQueryForData( userId, ownerVisibleStatus, anyVisibleStatus, criteriaBuilder,
                        criteriaQuery, filteredCriteriaRoot, entityClazz );
            } else {
                maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus, criteriaBuilder,
                        criteriaQuery, filteredCriteriaRoot, entityClazz );
            }
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
    }

    /**
     * Removes the system workflows.
     *
     * @param entityClazz
     *         the entity clazz
     * @param predicates
     *         the predicates
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     */
    private void removeSystemWorkflows( Class< ? > entityClazz, List< Predicate > predicates, CriteriaBuilder criteriaBuilder,
            Root< ? > filteredCriteriaRoot ) {
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            predicates.add( criteriaBuilder.not( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID )
                    .in( UUID.fromString( ConstantsID.RESTORE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.DELETE_SYSTEM_WORKFLOW_ID ),
                            UUID.fromString( ConstantsID.MASTER_SYSTEM_WORKFLOW_ID ) ) ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredRecordsByProperty( EntityManager entityManager, Class< ? > entityClazz, String hibernatePropertyName,
            String dtoPropertyName, Object propertyValue, FiltersDTO filtersDTO, boolean isAlias, List< ObjectType > objectTypeList ) {
        List< E > result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList,
                    predicates );
            if ( ReflectionUtils.hasField( entityClazz, dtoPropertyName ) ) {
                if ( isAlias ) {
                    filteredCriteriaRoot.alias( dtoPropertyName );
                }
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( hibernatePropertyName ), propertyValue ) );
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            result = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria )
                    .setHint( ConstantsDAO.CACHE_RETRIEVEMODE_KEY, CacheRetrieveMode.BYPASS ).setFirstResult( filtersDTO.getStart() )
                    .setMaxResults( filtersDTO.getLength() ).getResultList();
            filtersDTO.setFilteredRecords( ( long ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList().size() );
            filtersDTO.setTotalRecords(
                    getAllRecordCountByProperty( entityManager, entityClazz, hibernatePropertyName, dtoPropertyName, propertyValue,
                            isAlias ) );
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the all filtered records by property count.
     *
     * @param entityManager
     *         the entity manager
     * @param entityClazz
     *         the entity clazz
     * @param hibernatePropertyName
     *         the hibernate property name
     * @param dtoPropertyName
     *         the dto property name
     * @param propertyValue
     *         the property value
     * @param isAlias
     *         the is alias
     *
     * @return the all filtered records by property count
     */
    private Long getAllRecordCountByProperty( EntityManager entityManager, Class< ? > entityClazz, String hibernatePropertyName,
            String dtoPropertyName, Object propertyValue, boolean isAlias ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( entityClazz, dtoPropertyName ) ) {
                if ( isAlias ) {
                    filteredCriteriaRoot.alias( dtoPropertyName );
                }
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( hibernatePropertyName ), propertyValue ) );
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            return ( long ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList().size();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    public Map< String, UUID > getAllExistingOids( EntityManager entityManager, Class< ? > entityClazz, String caeBenchType,
            String caeBenchTypeValue ) {
        Session session = entityManager.unwrap( Session.class );

        try {
            Criteria criteria = session.createCriteria( entityClazz )
                    .setProjection( Projections.projectionList().add( Projections.property( "id" ), "id" )   // Fetch `id` (UUID)
                            .add( Projections.property( "oid" ), "oid" ) ).add( Restrictions.eq( caeBenchType, caeBenchTypeValue ) );

            List< Object[] > resultList = criteria.list();

            return resultList.stream().collect(
                    Collectors.toMap( row -> ( String ) row[ 1 ], row -> ( UUID ) row[ 0 ], ( existing, duplicate ) -> existing ) );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredLinkedItemsWithParentAndLifeCycle( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            int permission, List< ObjectType > objectTypeList, boolean toItems ) {
        List< E > list;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList,
                    predicates );
            if ( null != parentId ) {
                Subquery< UUID > subquery = paginatedFilteredCriteria.subquery( UUID.class );
                Root< Relation > subfrom = subquery.from( Relation.class );
                List< Predicate > relationPredicates = new ArrayList<>();
                if ( toItems ) {
                    subquery.select( subfrom.get( ConstantsDAO.CHILD ) );
                    Predicate parent = criteriaBuilder.equal( subfrom.get( ConstantsDAO.PARENT ), parentId );
                    relationPredicates.add( parent );
                } else {
                    subquery.select( subfrom.get( ConstantsDAO.PARENT ) );
                    Predicate child = criteriaBuilder.equal( subfrom.get( ConstantsDAO.CHILD ), parentId );
                    relationPredicates.add( child );
                }
                Predicate type = criteriaBuilder.equal( subfrom.get( ConstantsDAO.TYPE ), 1 );
                relationPredicates.add( type );
                subquery.where( relationPredicates.toArray( Predicate[]::new ) );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                        .value( subquery ) );
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus,
                    criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot, entityClazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
            list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).setFirstResult( filtersDTO.getStart() )
                    .setMaxResults( filtersDTO.getLength() ).getResultList();
            filtersDTO.setFilteredRecords( ( long ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList().size() );
            filtersDTO.setTotalRecords(
                    getCountWithParentId( entityClazz, parentId, userId, ownerVisibleStatus, anyVisibleStatus, entityManager,
                            criteriaBuilder ) );
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContainerEntity > getContainersForTreeSearchWithLanguage( EntityManager entityManager, String name, UUID userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, String language ) {
        List< ContainerEntity > result;
        Class< ContainerEntity > clazz = ContainerEntity.class;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ContainerEntity > criteriaQuery = criteriaBuilder.createQuery( clazz );
            Root< ContainerEntity > filteredCriteriaRoot = criteriaQuery.from( clazz );
            Join< ContainerEntity, TranslationEntity > joinTranslation = filteredCriteriaRoot.join( ConstantsDAO.TRANSLATION,
                    javax.persistence.criteria.JoinType.LEFT );
            List< Predicate > predicates = new ArrayList<>();
            clazz.isAssignableFrom( ContainerEntity.class );
            if ( language != null ) {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                        joinTranslation.get( ConstantsDAO.SUS_ENTITY ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ) );
                predicates.add( criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.LANGUAGE ), language ) );
            }
            Path< String > expression = getExpression( filteredCriteriaRoot, ConstantsDAO.NAME );
            Path< String > translationExpression = joinTranslation.get( ConstantsDAO.NAME );
            String pattern = ConstantsDAO.SQL_LIKE_PARAMETER + name.toLowerCase() + ConstantsDAO.SQL_LIKE_PARAMETER;
            predicates.add( criteriaBuilder.or( criteriaBuilder.and( translationExpression.isNull(),
                            criteriaBuilder.like( criteriaBuilder.lower( expression ), pattern ) ),
                    criteriaBuilder.like( criteriaBuilder.lower( translationExpression ), pattern ) ) );
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId.toString(), ownerVisibleStatus, anyVisibleStatus,
                    criteriaBuilder, criteriaQuery, filteredCriteriaRoot, clazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            criteriaQuery.multiselect( filteredCriteriaRoot.get( ConstantsDAO.COMPOSED_ID ),
                    filteredCriteriaRoot.get( ConstantsDAO.LIFECYCLE_STATUS ), filteredCriteriaRoot.get( ConstantsDAO.CONFIG ),
                    filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ),
                    filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_ON ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_BY ),
                    filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_BY ), criteriaBuilder.selectCase()
                            .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                                            criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ),
                                    filteredCriteriaRoot.get( ConstantsDAO.NAME ) ).otherwise( joinTranslation.get( ConstantsDAO.NAME ) ),
                    filteredCriteriaRoot.get( ConstantsDAO.DESCRIPTION ), filteredCriteriaRoot.get( ConstantsDAO.ENTITY_SIZE ),
                    filteredCriteriaRoot.get( ConstantsDAO.ICON ), filteredCriteriaRoot.get( ConstantsDAO.SELECTED_TRANSLATIONS ) );
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            result = entityManager.createQuery( criteriaQuery ).getResultList();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllFilteredLinkedItemsWithParentAndLifeCycleForAuditTrail( EntityManager entityManager, Class< ? > entityClazz,
            FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            int permission, List< ObjectType > objectTypeList, boolean isContainer ) {
        List< E > list;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
            Root< ? > root = criteriaQuery.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            addFilterPredicatesToListOfPredicates( criteriaBuilder, root, entityClazz, filtersDTO, objectTypeList, predicates );
            addRelationSubQueryToPredicateForAuditTrail( entityClazz, parentId, isContainer, criteriaQuery, criteriaBuilder, predicates,
                    root );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus,
                    criteriaBuilder, criteriaQuery, root, entityClazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            addSortingInCriteriaQuery( entityClazz, criteriaBuilder, root, criteriaQuery, filtersDTO );
            criteriaQuery.multiselect( root.get( ConstantsDAO.COMPOSED_ID ), root.get( ConstantsDAO.LIFECYCLE_STATUS ),
                    root.get( ConstantsDAO.CONFIG ), root.get( ConstantsDAO.TYPE_ID ), root.get( ConstantsDAO.CREATED_ON ),
                    root.get( ConstantsDAO.MODIFIED_ON ), root.get( ConstantsDAO.CREATED_BY ), root.get( ConstantsDAO.MODIFIED_BY ),
                    root.get( ConstantsDAO.NAME ), root.get( ConstantsDAO.DESCRIPTION ), root.get( ConstantsDAO.ENTITY_SIZE ),
                    root.get( ConstantsDAO.ICON ), root.get( ConstantsDAO.SELECTED_TRANSLATIONS ) );
            list = ( List< E > ) entityManager.createQuery( criteriaQuery ).setFirstResult( filtersDTO.getStart() )
                    .setMaxResults( filtersDTO.getLength() ).getResultList();
            filtersDTO.setFilteredRecords( ( long ) entityManager.createQuery( criteriaQuery ).getResultList().size() );
            filtersDTO.setTotalRecords(
                    getCountWithParentIdForAuditTrail( entityClazz, parentId, userId, ownerVisibleStatus, anyVisibleStatus, entityManager,
                            criteriaBuilder, isContainer ) );
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Adds the relation sub query to predicate for audit trail.
     *
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param isContainer
     *         the is container
     * @param criteriaQuery
     *         the criteria query
     * @param criteriaBuilder
     *         the criteria builder
     * @param predicates
     *         the predicates
     * @param root
     *         the root
     */
    private static void addRelationSubQueryToPredicateForAuditTrail( Class< ? > entityClazz, UUID parentId, boolean isContainer,
            CriteriaQuery< ? > criteriaQuery, CriteriaBuilder criteriaBuilder, List< Predicate > predicates, Root< ? > root ) {
        if ( null != parentId ) {
            Subquery< UUID > subquery = criteriaQuery.subquery( UUID.class );
            Root< Relation > subfrom = subquery.from( Relation.class );
            List< Predicate > relationPredicates = new ArrayList<>();
            Predicate or = criteriaBuilder.disjunction();
            or.getExpressions().add( criteriaBuilder.equal( subfrom.get( ConstantsDAO.TYPE ), AuditTrailRelationType.RELATION_CREATED ) );
            or.getExpressions().add( criteriaBuilder.equal( subfrom.get( ConstantsDAO.TYPE ), AuditTrailRelationType.RELATION_USED ) );
            relationPredicates.add( or );
            if ( isContainer ) {
                subquery.select( subfrom.get( ConstantsDAO.CHILD ) );
                Predicate parent = criteriaBuilder.equal( subfrom.get( ConstantsDAO.PARENT ), parentId );
                relationPredicates.add( parent );
            } else {
                subquery.select( subfrom.get( ConstantsDAO.PARENT ) );
                Predicate child = criteriaBuilder.equal( subfrom.get( ConstantsDAO.CHILD ), parentId );
                relationPredicates.add( child );
            }
            subquery.where( relationPredicates.toArray( Predicate[]::new ) );
            predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( subquery ) );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
    }

    /**
     * Gets count with parent id for audit trail.
     *
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param isContainer
     *         the is container
     *
     * @return the count with parent id for audit trail
     */
    private Long getCountWithParentIdForAuditTrail( Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, EntityManager entityManager, CriteriaBuilder criteriaBuilder, boolean isContainer ) {
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        addRelationSubQueryToPredicateForAuditTrail( entityClazz, parentId, isContainer, criteriaQuery, criteriaBuilder, predicates, root );
        Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus, criteriaBuilder,
                criteriaQuery, root, entityClazz );
        predicates.add(
                criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets the count with parent id.
     *
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param entityManager
     *         the entityManager
     * @param criteriaBuilder
     *         the criteria builder
     *
     * @return the count with parent id
     */
    public Long getCountWithParentId( Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, EntityManager entityManager, CriteriaBuilder criteriaBuilder ) {
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        }
        Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus, criteriaBuilder,
                criteriaQuery, root, entityClazz );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets count with parent id and permission.
     *
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param permission
     *         the permission
     *
     * @return the count with parent id and permission
     */
    public Long getCountWithParentIdAndPermission( Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, EntityManager entityManager, CriteriaBuilder criteriaBuilder, int permission ) {
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( entityClazz );
        criteriaQuery.select( criteriaBuilder.count( root ) );
        List< Predicate > predicates = new ArrayList<>();
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        }

        if ( SuSEntity.class.isAssignableFrom( entityClazz ) ) {
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( userId, ownerVisibleStatus, anyVisibleStatus,
                    criteriaBuilder, criteriaQuery, root, entityClazz );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery, root,
                    entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    /**
     * Gets the filtered count.
     *
     * @param entityManager
     *         the entityManager
     * @param criteriaBuilder
     *         the criteria builder
     * @param filtersDTO
     *         the filters DTO
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypeList
     *         the object type list
     * @param language
     *         the language
     *
     * @return the filtered count
     */
    protected long getFilteredCount( EntityManager entityManager, CriteriaBuilder criteriaBuilder, FiltersDTO filtersDTO,
            Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypeList, String language ) {
        try {
            CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );
            Join< SuSEntity, TranslationEntity > joinTranslation = null;
            if ( language != null ) {
                joinTranslation = filteredCriteriaRoot.join( ConstantsDAO.TRANSLATION, javax.persistence.criteria.JoinType.LEFT );
            }
            List< Predicate > predicates = new ArrayList<>();
            preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypeList,
                    criteriaBuilder, paginatedFilteredCriteria, filteredCriteriaRoot, predicates, joinTranslation, language );
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            return entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        }
    }

    /**
     * Gets filtered count.
     *
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param filtersDTO
     *         the filters dto
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypeList
     *         the object type list
     * @param language
     *         the language
     * @param permission
     *         the permission
     *
     * @return the filtered count
     */
    protected long getFilteredCount( EntityManager entityManager, CriteriaBuilder criteriaBuilder, FiltersDTO filtersDTO,
            Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypeList, String language, int permission ) {
        try {
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
            criteriaQuery.select( criteriaBuilder.count( filteredCriteriaRoot ) );
            Join< SuSEntity, TranslationEntity > joinTranslation = null;
            if ( language != null ) {
                joinTranslation = filteredCriteriaRoot.join( ConstantsDAO.TRANSLATION, javax.persistence.criteria.JoinType.LEFT );
            }
            List< Predicate > predicates = new ArrayList<>();
            preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypeList,
                    criteriaBuilder, criteriaQuery, filteredCriteriaRoot, predicates, joinTranslation, language );
            if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
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

    /**
     * Gets filtered count.
     *
     * @param entityManager
     *         the entity manager
     * @param criteriaBuilder
     *         the criteria builder
     * @param filtersDTO
     *         the filters dto
     * @param entityClazz
     *         the entity clazz
     * @param parentId
     *         the parent id
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param objectTypeList
     *         the object type list
     *
     * @return the filtered count
     */
    protected long getFilteredCount( EntityManager entityManager, CriteriaBuilder criteriaBuilder, FiltersDTO filtersDTO,
            Class< ? > entityClazz, UUID parentId, String userId, List< String > ownerVisibleStatus, List< String > anyVisibleStatus,
            List< ObjectType > objectTypeList ) {
        return getFilteredCount( entityManager, criteriaBuilder, filtersDTO, entityClazz, parentId, userId, ownerVisibleStatus,
                anyVisibleStatus, objectTypeList, null );
    }

    /**
     * Gets the user security id list.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the user security id list
     */
    protected List< UUID > getUserSecurityIdList( EntityManager entityManager, UUID id ) {
        List< GroupEntity > groupEntities = getGroupsByUserId( entityManager, id );
        List< UUID > ids = groupEntities.stream().map( GroupEntity::getId ).collect( Collectors.toList() );
        List< UUID > aclSidIds = null;
        if ( CollectionUtils.isNotEmpty( ids ) ) {
            ids.add( id );
            List< AclSecurityIdentityEntity > aclSecurityIdentityEntities = getAclSecurityIdentityByProperty( entityManager,
                    ConstantsDAO.SID, ids );
            aclSidIds = aclSecurityIdentityEntities.stream().map( AclSecurityIdentityEntity::getId ).collect( Collectors.toList() );
        } else {
            AclSecurityIdentityEntity aclSecurityIdentityEntity = getSecurityIdentity( entityManager, id );
            if ( aclSecurityIdentityEntity != null ) {
                aclSidIds = new ArrayList<>();
                aclSidIds.add( aclSecurityIdentityEntity.getId() );
            }
        }
        return aclSidIds;
    }

    /**
     * Gets the filtered results with predicates.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param objectTypeList
     *         the object type list
     * @param predicates
     *         the predicates
     */
    protected void addFilterPredicatesToListOfPredicates( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            Class< ? > entityClazz, FiltersDTO filtersDTO, List< ObjectType > objectTypeList, List< Predicate > predicates ) {
        addFilterPredicatesToListOfPredicates( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList, predicates,
                null );
    }

    /**
     * Gets the filtered results with predicates.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters DTO
     * @param objectTypeList
     *         the object type list
     * @param predicates
     *         the predicates
     * @param joinTranslation
     *         the join translation
     */
    protected void addFilterPredicatesToListOfPredicates( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            Class< ? > entityClazz, FiltersDTO filtersDTO, List< ObjectType > objectTypeList, List< Predicate > predicates,
            Join< ?, TranslationEntity > joinTranslation ) {
        Predicate or = criteriaBuilder.disjunction();
        Predicate and = criteriaBuilder.conjunction();
        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                populateJunctionForColumn( criteriaBuilder, filteredCriteriaRoot, entityClazz, filtersDTO, objectTypeList, filterColumn, or,
                        and, joinTranslation );
            }
        }
        if ( CollectionUtils.isNotEmpty( or.getExpressions() ) ) {
            predicates.add( or );
        }
        if ( CollectionUtils.isNotEmpty( and.getExpressions() ) ) {
            predicates.add( and );
        }
    }

    /**
     * Populate junction for column.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     * @param filtersDTO
     *         the filters dto
     * @param objectTypeList
     *         the object type list
     * @param filterColumn
     *         the filter column
     * @param or
     *         the or
     * @param and
     *         the and
     * @param joinTranslation
     *         the join translation
     */
    private void populateJunctionForColumn( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot, Class< ? > entityClazz,
            FiltersDTO filtersDTO, List< ObjectType > objectTypeList, FilterColumn filterColumn, Predicate or, Predicate and,
            Join< ?, TranslationEntity > joinTranslation ) {
        if ( filterColumn.getFilters() != null ) {
            for ( Filter filter : filterColumn.getFilters() ) {
                try {
                    populateJunctionForColumnFilters( criteriaBuilder, filteredCriteriaRoot, entityClazz, objectTypeList, filterColumn,
                            filter, or, and, joinTranslation );
                } catch ( Exception e ) {
                    ExceptionLogger.logException( e, getClass() );
                    throw new SusException(
                            MessageBundleFactory.getMessage( Messages.INVALID_VALUE_PROVIDED_FOR_COLUMN.getKey(), filter.getValue(),
                                    filterColumn.getName() ) );
                }
            }
        }
        // Add search on name and description column
        if ( isSearchColumn( filterColumn.getName(), entityClazz ) ) {
            Predicate predicate;
            if ( isTranslationColumn( filterColumn.getName() ) && joinTranslation != null && entityClazz.isAssignableFrom( SuSEntity.class )
                    && StringUtils.isNotBlank( filtersDTO.getSearch() ) ) {
                Path< String > expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
                Path< String > translationExpression = joinTranslation.get( filterColumn.getName() );
                String pattern = ConstantsDAO.SQL_LIKE_PARAMETER + filtersDTO.getSearch().toLowerCase() + ConstantsDAO.SQL_LIKE_PARAMETER;
                predicate = criteriaBuilder.or( criteriaBuilder.and( translationExpression.isNull(),
                                criteriaBuilder.like( criteriaBuilder.lower( expression ), pattern ) ),
                        criteriaBuilder.like( criteriaBuilder.lower( translationExpression ), pattern ) );
            } else {
                predicate = searchOnAnyColumnAsPredicate( criteriaBuilder, filteredCriteriaRoot, filtersDTO.getSearch(),
                        filterColumn.getName(), entityClazz );
            }
            if ( null != predicate ) {
                or.getExpressions().add( predicate );
            }
        }
    }

    /**
     * Populate junction for column filters.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     * @param objectTypeList
     *         the object type list
     * @param filterColumn
     *         the filter column
     * @param filter
     *         the filter
     * @param or
     *         the or
     * @param and
     *         the and
     * @param joinTranslation
     *         the join translation
     */
    private void populateJunctionForColumnFilters( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot, Class< ? > entityClazz,
            List< ObjectType > objectTypeList, FilterColumn filterColumn, Filter filter, Predicate or, Predicate and,
            Join< ?, TranslationEntity > joinTranslation ) {
        if ( filterColumn.getName().contains( ConstantsString.DOT ) ) {
            if ( filterColumn.getName().endsWith( ConstantsDAO.VERSION_ID ) ) {
                Path< Integer > expressionInteger = getIntegerExpression( filteredCriteriaRoot, filterColumn.getName() );
                populateJunctionAsPredicate( criteriaBuilder, expressionInteger, Integer.parseInt( filter.getValue() ),
                        filter.getCondition(), filter.getOperator(), or, and );
            } else {
                Path< String > expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
                populateJunctionAsPredicate( criteriaBuilder, expression, filter.getValue(), filter.getCondition(), filter.getOperator(),
                        or, and );
            }
        } else if ( filterColumn.getName().equals( ConstantsDAO.COMPOSED_ID_ID )
                || filterColumn.getName().equals( ConstantsDAO.ID ) && ValidationUtils.validateUUIDString( filter.getValue() ) ) {
            Path< String > expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expression, UUID.fromString( filter.getValue() ), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( filterColumn.getName().equals( ConstantsDAO.TYPE ) && CollectionUtils.isNotEmpty( objectTypeList ) ) {
            populateJunctionForTypeIdWITHTableAsPredicate( filteredCriteriaRoot, filter.getValue(), filter.getCondition(),
                    filter.getOperator(), or, and, objectTypeList );
        } else if ( filterColumn.getName().equals( ConstantsDAO.CATEGORY ) ) {
            populateJunctionForCategoryWITHTableAsPredicate( filteredCriteriaRoot, filter.getValue(), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( Integer.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) )
                || int.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< Integer > expressionInteger = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expressionInteger, Integer.parseInt( filter.getValue() ), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( Double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< Double > expressionDouble = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expressionDouble, Double.parseDouble( filter.getValue() ), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( Float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< Float > expressionFloat = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expressionFloat, Float.parseFloat( filter.getValue() ), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< UUID > expressionUUID = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expressionUUID, UUID.fromString( filter.getValue() ), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( Boolean.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< Boolean > expressionBoolean = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionAsPredicate( criteriaBuilder, expressionBoolean, filter.getValue().equalsIgnoreCase( ConstantsStatus.ACTIVE ),
                    filter.getCondition(), filter.getOperator(), or, and );
        } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            Path< Date > expressionDate = filteredCriteriaRoot.get( filterColumn.getName() );
            populateJunctionForDatesAsPredicate( criteriaBuilder, expressionDate, filter.getValue(), filter.getCondition(),
                    filter.getOperator(), or, and );
        } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, filterColumn.getName() ) ) ) {
            if ( SuSEntity.class.isAssignableFrom( entityClazz ) && filterColumn.getName().equals( ConstantsDAO.NAME )
                    && joinTranslation != null ) {
                Path< String > expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
                Path< String > translationExpression = joinTranslation.get( filterColumn.getName() );
                populateJunctionAsPredicateWithLanguage( criteriaBuilder, expression, filter.getValue(), filter.getCondition(),
                        filter.getOperator(), or, and, translationExpression );
            } else {
                Path< String > expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
                populateJunctionAsPredicate( criteriaBuilder, expression, filter.getValue(), filter.getCondition(), filter.getOperator(),
                        or, and );
            }
        }
    }

    /**
     * Gets the expression.
     *
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param filterColumnName
     *         the filter column name
     *
     * @return the expression
     */
    protected Path< String > getExpression( Root< ? > filteredCriteriaRoot, String filterColumnName ) {
        Path< String > expression;
        if ( filterColumnName.contains( ConstantsString.DOT ) ) {
            String[] names = StringUtils.split( filterColumnName, ConstantsString.DOT );
            expression = filteredCriteriaRoot.join( names[ ConstantsInteger.INTEGER_VALUE_ZERO ] );
            for ( int i = ConstantsInteger.INTEGER_VALUE_ONE; i < names.length; i++ ) {
                expression = expression.get( names[ i ] );
            }
        } else {
            expression = filteredCriteriaRoot.get( filterColumnName );
        }
        return expression;
    }

    /**
     * Gets integer expression.
     *
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param filterColumnName
     *         the filter column name
     *
     * @return the integer expression
     */
    protected Path< Integer > getIntegerExpression( Root< ? > filteredCriteriaRoot, String filterColumnName ) {
        Path< Integer > expression;
        if ( filterColumnName.contains( ConstantsString.DOT ) ) {
            String[] names = StringUtils.split( filterColumnName, ConstantsString.DOT );
            expression = filteredCriteriaRoot.join( names[ ConstantsInteger.INTEGER_VALUE_ZERO ] );
            for ( int i = ConstantsInteger.INTEGER_VALUE_ONE; i < names.length; i++ ) {
                expression = expression.get( names[ i ] );
            }
        } else {
            expression = filteredCriteriaRoot.get( filterColumnName );
        }
        return expression;
    }

    /**
     * Prepare sub query to fetch child.
     *
     * @param parentId
     *         the parent id
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     *
     * @return the subquery
     */
    protected Subquery< UUID > prepareSubQueryToFetchChild( UUID parentId, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > paginatedFilteredCriteria ) {
        Subquery< UUID > subquery = paginatedFilteredCriteria.subquery( UUID.class );
        Root< Relation > subfrom = subquery.from( Relation.class );
        subquery.select( subfrom.get( ConstantsDAO.CHILD ) );
        Predicate parent = criteriaBuilder.equal( subfrom.get( ConstantsDAO.PARENT ), parentId );
        return subquery.where( parent );
    }

    /**
     * Adds the composite key for sus entity.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     *
     * @return the predicate
     */
    protected Predicate addCompositeKeyForSusEntity( CriteriaBuilder criteriaBuilder, CriteriaQuery< SuSEntity > paginatedFilteredCriteria,
            Root< SuSEntity > filteredCriteriaRoot ) {
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( ( Class< ? > ) SuSEntity.class );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionSubQuery.where( maxIdPredicate );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        return maxVersionCriteriaQueryPredicate;
    }

    /**
     * Populate junction as predicate.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param expression
     *         the expression
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunctionAsPredicate( CriteriaBuilder criteriaBuilder, Path< ? > expression, Object value, String condition,
            String operator, Predicate or, Predicate and ) {
        Expression< String > localExpression = ( Expression< String > ) expression;
        if ( value instanceof String valueString ) {
            localExpression = criteriaBuilder.lower( localExpression );
            value = valueString.toLowerCase();
        }
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.equal( localExpression, value ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.notEqual( localExpression, value ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition,
                    criteriaBuilder.like( localExpression, ConstantsDAO.SQL_LIKE_PARAMETER + value + ConstantsDAO.SQL_LIKE_PARAMETER ), and,
                    or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.not(
                            criteriaBuilder.like( localExpression, ConstantsDAO.SQL_LIKE_PARAMETER + value + ConstantsDAO.SQL_LIKE_PARAMETER ) ),
                    and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.like( localExpression, value + ConstantsDAO.SQL_LIKE_PARAMETER ),
                    and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.like( localExpression, ConstantsDAO.SQL_LIKE_PARAMETER + value ),
                    and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThan( localExpression, value.toString() ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThanOrEqualTo( localExpression, value.toString() ), and,
                    or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( localExpression, value.toString() ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThanOrEqualTo( localExpression, value.toString() ), and, or );
        }
    }

    /**
     * Populate junction as predicate.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param expression
     *         the expression
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     * @param translationExpression
     *         the translation expression
     */
    private void populateJunctionAsPredicateWithLanguage( CriteriaBuilder criteriaBuilder, Path< ? > expression, Object value,
            String condition, String operator, Predicate or, Predicate and, Path< String > translationExpression ) {
        Expression< String > localExpression = ( Expression< String > ) expression;
        Expression< String > lowerTranslationExpression = criteriaBuilder.lower( translationExpression );
        if ( value instanceof String valueString ) {
            localExpression = criteriaBuilder.lower( localExpression );
            value = valueString.toLowerCase();
        }
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.EQUALS.getDeName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or(
                    criteriaBuilder.and( translationExpression.isNull(), criteriaBuilder.equal( localExpression, value.toString() ) ),
                    criteriaBuilder.equal( lowerTranslationExpression, value.toString() ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.NOT_EQUALS.getDeName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or(
                    criteriaBuilder.and( translationExpression.isNull(), criteriaBuilder.notEqual( localExpression, value.toString() ) ),
                    criteriaBuilder.notEqual( lowerTranslationExpression, value.toString() ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.IS_IN.getDeName() ) ) {
            String pattern = ConstantsDAO.SQL_LIKE_PARAMETER + value.toString() + ConstantsDAO.SQL_LIKE_PARAMETER;
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or(
                    criteriaBuilder.and( translationExpression.isNull(), criteriaBuilder.like( localExpression, pattern ) ),
                    criteriaBuilder.like( lowerTranslationExpression, pattern ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.IS_NOT_IN.getDeName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or( criteriaBuilder.and( translationExpression.isNull(),
                    criteriaBuilder.not( criteriaBuilder.like( localExpression,
                            ConstantsDAO.SQL_LIKE_PARAMETER + value.toString() + ConstantsDAO.SQL_LIKE_PARAMETER ) ) ), criteriaBuilder.not(
                    criteriaBuilder.like( lowerTranslationExpression,
                            ConstantsDAO.SQL_LIKE_PARAMETER + value.toString() + ConstantsDAO.SQL_LIKE_PARAMETER ) ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.BEGINS_WITH.getDeName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or( criteriaBuilder.and( translationExpression.isNull(),
                            criteriaBuilder.like( localExpression, value.toString() + ConstantsDAO.SQL_LIKE_PARAMETER ) ),
                    criteriaBuilder.like( lowerTranslationExpression, value.toString() + ConstantsDAO.SQL_LIKE_PARAMETER ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) || operator.equalsIgnoreCase(
                ConstantsOperators.ENDS_WITH.getDeName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or( criteriaBuilder.and( translationExpression.isNull(),
                            criteriaBuilder.like( localExpression, ConstantsDAO.SQL_LIKE_PARAMETER + value.toString() ) ),
                    criteriaBuilder.like( lowerTranslationExpression, ConstantsDAO.SQL_LIKE_PARAMETER + value.toString() ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThan( localExpression, value.toString() ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThanOrEqualTo( localExpression, value.toString() ), and,
                    or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( localExpression, value.toString() ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThanOrEqualTo( localExpression, value.toString() ), and, or );
        }
    }

    /**
     * Populate junction for type id WITH table as predicate.
     *
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     * @param objectTypeList
     *         the object type list
     */
    private void populateJunctionForTypeIdWITHTableAsPredicate( Root< ? > filteredCriteriaRoot, Object value, String condition,
            String operator, Predicate or, Predicate and, List< ObjectType > objectTypeList ) {
        java.util.function.Predicate< ObjectType > operatorValue = getAppliedFilterObjectType( value, operator );
        List< UUID > objectIds = objectTypeList.stream().filter( operatorValue ).map( ObjectType::getId ).collect( Collectors.toList() );
        Predicate predicate;
        if ( CollectionUtils.isNotEmpty( objectIds ) ) {
            predicate = filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID ).in( objectIds );
        } else {
            predicate = filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID ).isNull();
        }
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.getExpressions().add( predicate );
        } else {
            and.getExpressions().add( predicate );
        }
    }

    /**
     * Populate junction for category WITH table as predicate.
     *
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunctionForCategoryWITHTableAsPredicate( Root< ? > filteredCriteriaRoot, Object value, String condition,
            String operator, Predicate or, Predicate and ) {
        java.util.function.Predicate< SchemeCategoryDTO > operatorValue = getAppliedFilterSchemeCategory( value, operator );
        List< SchemeCategoryDTO > schemeCategoryDTOS = Arrays.asList( new SchemeCategoryDTO( SchemeCategoryEnum.DOE ),
                new SchemeCategoryDTO( SchemeCategoryEnum.OPTIMIZATION ), new SchemeCategoryDTO( SchemeCategoryEnum.MACHINE_LEARNING ) );
        List< Integer > objectIds = schemeCategoryDTOS.stream().filter( operatorValue ).map( SchemeCategoryDTO::getId )
                .collect( Collectors.toList() );
        Predicate predicate;
        if ( CollectionUtils.isNotEmpty( objectIds ) ) {
            predicate = filteredCriteriaRoot.get( ConstantsDAO.CATEGORY ).in( objectIds );
        } else {
            predicate = filteredCriteriaRoot.get( ConstantsDAO.CATEGORY ).isNull();
        }
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.getExpressions().add( predicate );
        } else {
            and.getExpressions().add( predicate );
        }
    }

    /**
     * Gets the applied filter object type.
     *
     * @param value
     *         the value
     * @param operator
     *         the operator
     *
     * @return the applied filter object type
     */
    private java.util.function.Predicate< ObjectType > getAppliedFilterObjectType( Object value, String operator ) {
        java.util.function.Predicate< ObjectType > operatorValue;
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            operatorValue = objectType -> objectType.getName().equals( value );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            operatorValue = objectType -> !objectType.getName().equals( value );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            operatorValue = objectType -> !objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().startsWith( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().endsWith( value.toString().toLowerCase() );
        } else {
            operatorValue = objectType -> objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        }
        return operatorValue;
    }

    /**
     * Gets the applied filter scheme category.
     *
     * @param value
     *         the value
     * @param operator
     *         the operator
     *
     * @return the applied filter scheme category
     */
    private java.util.function.Predicate< SchemeCategoryDTO > getAppliedFilterSchemeCategory( Object value, String operator ) {
        java.util.function.Predicate< SchemeCategoryDTO > operatorValue;
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            operatorValue = objectType -> objectType.getName().equals( value );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            operatorValue = objectType -> !objectType.getName().equals( value );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            operatorValue = objectType -> !objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().startsWith( value.toString().toLowerCase() );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            operatorValue = objectType -> objectType.getName().toLowerCase().endsWith( value.toString().toLowerCase() );
        } else {
            operatorValue = objectType -> objectType.getName().toLowerCase().contains( value.toString().toLowerCase() );
        }
        return operatorValue;
    }

    /**
     * Populate junction for dates as predicate.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param expression
     *         the expression
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunctionForDatesAsPredicate( CriteriaBuilder criteriaBuilder, Path< Date > expression, String value,
            String condition, String operator, Predicate or, Predicate and ) {
        switch ( value ) {
            case ConstantsDateFilters.TODAY ->
                    addFilterForSingleDay( criteriaBuilder, expression, DateUtils.getCurrentDateWithoutTime(), condition, operator, or,
                            and );
            case ConstantsDateFilters.YESTERDAY ->
                    addFilterForSingleDay( criteriaBuilder, expression, DateUtils.getYesterdayDateWithoutTime(), condition, operator, or,
                            and );
            case ConstantsDateFilters.LAST_WEEK ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getLastWeekStartDate(), DateUtils.getLastWeekEndDate(),
                            condition, operator, or, and );
            case ConstantsDateFilters.THIS_WEEK ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getThisWeekStartDate(), DateUtils.getThisWeekEndDate(),
                            condition, operator, or, and );
            case ConstantsDateFilters.LAST_MONTH ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getLastMonthStartDate(), DateUtils.getLastMonthEndDate(),
                            condition, operator, or, and );
            case ConstantsDateFilters.THIS_MONTH ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getThisMonthStartDate(), DateUtils.getThisMonthEndDate(),
                            condition, operator, or, and );
            case ConstantsDateFilters.THIS_YEAR ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getThisYearStartDate(), DateUtils.getThisYearEndDate(),
                            condition, operator, or, and );
            case ConstantsDateFilters.LAST_YEAR ->
                    addFilterForDateRange( criteriaBuilder, expression, DateUtils.getLastYearStartDate(), DateUtils.getLastYearEndDate(),
                            condition, operator, or, and );
            default -> // else if a particular date is selected
                    addFilterForSingleDay( criteriaBuilder, expression, DateUtils.fromEpochString( value ), condition, operator, or, and );
        }
    }

    /**
     * Adds the filter for single day.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param expression
     *         the expression
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void addFilterForSingleDay( CriteriaBuilder criteriaBuilder, Path< Date > expression, Date value, String condition,
            String operator, Predicate or, Predicate and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.and( criteriaBuilder.greaterThanOrEqualTo( expression, value ),
                    criteriaBuilder.lessThanOrEqualTo( expression, DateUtils.getTomorrowDate( value ) ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or( criteriaBuilder.lessThan( expression, value ),
                    criteriaBuilder.greaterThanOrEqualTo( expression, DateUtils.getTomorrowDate( value ) ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThan( expression, value ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThanOrEqualTo( expression, value ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( expression, value ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( expression, DateUtils.getTomorrowDate( value ) ), and,
                    or );
        }
    }

    /**
     * Adds the filter for date range.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param expression
     *         the expression
     * @param startdate
     *         the startdate
     * @param endDate
     *         the end date
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void addFilterForDateRange( CriteriaBuilder criteriaBuilder, Path< Date > expression, Date startdate, Date endDate,
            String condition, String operator, Predicate or, Predicate and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition,
                    criteriaBuilder.and( criteriaBuilder.greaterThanOrEqualTo( expression, startdate ),
                            criteriaBuilder.lessThanOrEqualTo( expression, endDate ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.or( criteriaBuilder.lessThan( expression, startdate ),
                    criteriaBuilder.greaterThan( expression, endDate ) ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThan( expression, endDate ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.greaterThanOrEqualTo( expression, startdate ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( expression, startdate ), and, or );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteriaQuery( condition, criteriaBuilder.lessThan( expression, endDate ), and, or );
        }
    }

    /**
     * Populate junction for dates.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunctionForDates( String column, String value, String condition, String operator, Disjunction or,
            Conjunction and ) {
        switch ( value ) {
            case ConstantsDateFilters.TODAY ->
                    addFilterForSingleDay( column, DateUtils.getCurrentDateWithoutTime(), condition, operator, or, and );
            case ConstantsDateFilters.YESTERDAY ->
                    addFilterForSingleDay( column, DateUtils.getYesterdayDateWithoutTime(), condition, operator, or, and );
            case ConstantsDateFilters.LAST_WEEK ->
                    addFilterForDateRange( column, DateUtils.getLastWeekStartDate(), DateUtils.getLastWeekEndDate(), condition, operator,
                            or, and );
            case ConstantsDateFilters.THIS_WEEK ->
                    addFilterForDateRange( column, DateUtils.getThisWeekStartDate(), DateUtils.getThisWeekEndDate(), condition, operator,
                            or, and );
            case ConstantsDateFilters.LAST_MONTH ->
                    addFilterForDateRange( column, DateUtils.getLastMonthStartDate(), DateUtils.getLastMonthEndDate(), condition, operator,
                            or, and );
            case ConstantsDateFilters.THIS_MONTH ->
                    addFilterForDateRange( column, DateUtils.getThisMonthStartDate(), DateUtils.getThisMonthEndDate(), condition, operator,
                            or, and );
            case ConstantsDateFilters.THIS_YEAR ->
                    addFilterForDateRange( column, DateUtils.getThisYearStartDate(), DateUtils.getThisYearEndDate(), condition, operator,
                            or, and );
            case ConstantsDateFilters.LAST_YEAR ->
                    addFilterForDateRange( column, DateUtils.getLastYearStartDate(), DateUtils.getLastYearEndDate(), condition, operator,
                            or, and );
            default -> // else if a particular date is selected
                    addFilterForSingleDay( column, DateUtils.fromString( value, ConstantsDate.DATE_ONLY_FORMAT ), condition, operator, or,
                            and );
        }
    }

    /**
     * Adds the filter for single day.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void addFilterForSingleDay( String column, Date value, String condition, String operator, Disjunction or, Conjunction and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.and( Restrictions.ge( column, value ), Restrictions.le( column, DateUtils.getTomorrowDate( value ) ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.or( Restrictions.lt( column, value ), Restrictions.ge( column, DateUtils.getTomorrowDate( value ) ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, DateUtils.getTomorrowDate( value ) ) );
        }
    }

    /**
     * Adds the filter for date range.
     *
     * @param column
     *         the column
     * @param startDate
     *         the start date
     * @param endDate
     *         the end date
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void addFilterForDateRange( String column, Date startDate, Date endDate, String condition, String operator, Disjunction or,
            Conjunction and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.and( Restrictions.ge( column, startDate ), Restrictions.le( column, endDate ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.or( Restrictions.lt( column, startDate ), Restrictions.gt( column, endDate ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, endDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, startDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, startDate ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.le( column, endDate ) );
        }
    }

    /**
     * Adds the logical operator to criteria query.
     *
     * @param condition
     *         the condition
     * @param predicate
     *         the predicate
     * @param and
     *         the and
     * @param or
     *         the or
     */
    private void addLogicalOperatorToCriteriaQuery( String condition, Predicate predicate, Predicate and, Predicate or ) {
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.getExpressions().add( predicate );
        } else {
            and.getExpressions().add( predicate );
        }
    }

    /**
     * Search on any column as predicate.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param searchQuery
     *         the search query
     * @param columnName
     *         the column name
     * @param entityClazz
     *         the entity clazz
     *
     * @return the predicate
     */
    private Predicate searchOnAnyColumnAsPredicate( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot, String searchQuery,
            String columnName, Class< ? > entityClazz ) {
        Predicate predicate = null;
        try {
            if ( StringUtils.isNotBlank( searchQuery ) ) {
                Path< String > expression = getExpression( filteredCriteriaRoot, columnName );
                if ( columnName.contains( ConstantsString.DOT ) ) {
                    predicate = criteriaBuilder.like( expression,
                            ConstantsDAO.SQL_LIKE_PARAMETER + searchQuery + ConstantsDAO.SQL_LIKE_PARAMETER );
                } else if ( Integer.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< Integer > expressionInteger = filteredCriteriaRoot.get( columnName );
                    predicate = criteriaBuilder.equal( expressionInteger, Integer.parseInt( searchQuery ) );
                } else if ( Double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< Double > expressionDouble = filteredCriteriaRoot.get( columnName );
                    predicate = criteriaBuilder.equal( expressionDouble, Double.parseDouble( searchQuery ) );
                } else if ( Float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< Float > expressionFloat = filteredCriteriaRoot.get( columnName );
                    predicate = criteriaBuilder.equal( expressionFloat, Float.parseFloat( searchQuery ) );
                } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< UUID > expressionUUID = filteredCriteriaRoot.get( columnName );
                    predicate = criteriaBuilder.equal( expressionUUID, UUID.fromString( searchQuery ) );
                } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< Date > expressionDate = filteredCriteriaRoot.get( columnName );
                    predicate = criteriaBuilder.equal( expressionDate,
                            DateUtils.fromString( searchQuery, ConstantsDate.DATE_ONLY_FORMAT ) );
                } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    predicate = criteriaBuilder.like( criteriaBuilder.lower( expression ),
                            ConstantsDAO.SQL_LIKE_PARAMETER + searchQuery.toLowerCase() + ConstantsDAO.SQL_LIKE_PARAMETER );
                } else if ( Boolean.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    Path< Boolean > expressionBoolean = filteredCriteriaRoot.get( columnName );
                    if ( ConstantsStatus.ACTIVE.toLowerCase().contains( searchQuery.toLowerCase() ) ) {
                        predicate = criteriaBuilder.equal( expressionBoolean, true );
                    } else if ( ConstantsStatus.DISABLE.toLowerCase().contains( searchQuery.toLowerCase() ) ) {
                        predicate = criteriaBuilder.equal( expressionBoolean, false );
                    }
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return predicate;
    }

    /**
     * Gets max version criteria query.
     *
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     *
     * @return the max version criteria query
     */
    private Subquery< Integer > getMaxVersionCriteriaQuery( String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > paginatedFilteredCriteria,
            Root< ? > filteredCriteriaRoot, Class< ? > entityClazz ) {
        List< Predicate > maxVersionPredicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionPredicates.add( maxIdPredicate );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            Predicate predicateLifeCycleStatusWithOwnerVisible = rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS )
                    .in( ownerVisibleStatus );
            Predicate predicateOwnerId = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ),
                    UUID.fromString( userId ) );
            Predicate predicateLifeCycleStatusWithOwnerVisibleAndOwnerId = criteriaBuilder.and( predicateLifeCycleStatusWithOwnerVisible,
                    predicateOwnerId );
            Predicate predicateLifeCycleStatusWithAnyVisible = rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS )
                    .in( anyVisibleStatus );
            Predicate predicateOtherThanOwnerId = criteriaBuilder.notEqual(
                    rootMaxVersionSubQuery.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ), UUID.fromString( userId ) );
            Predicate predicateLifeCycleStatusWithVisibleOtherThanOwnerId = criteriaBuilder.and( predicateLifeCycleStatusWithAnyVisible,
                    predicateOtherThanOwnerId );
            Predicate or = criteriaBuilder.or( predicateLifeCycleStatusWithOwnerVisibleAndOwnerId,
                    predicateLifeCycleStatusWithVisibleOtherThanOwnerId );
            Predicate and = criteriaBuilder.conjunction();
            and.getExpressions().add( or );
            maxVersionPredicates.add( and );
        }
        maxVersionSubQuery.where( maxVersionPredicates.toArray( Predicate[]::new ) );
        return maxVersionSubQuery;
    }

    /**
     * Gets the max version criteria query for data.
     *
     * @param userId
     *         the user id
     * @param ownerVisibleStatus
     *         the owner visible status
     * @param anyVisibleStatus
     *         the any visible status
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     *
     * @return the max version criteria query for data
     */
    private Subquery< Integer > getMaxVersionCriteriaQueryForData( String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > paginatedFilteredCriteria,
            Root< ? > filteredCriteriaRoot, Class< ? > entityClazz ) {
        List< Predicate > maxVersionPredicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionPredicates.add( maxIdPredicate );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            Predicate predicateLifeCycleStatusWithOwnerVisible = rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS )
                    .in( ownerVisibleStatus );
            Predicate predicateOwnerId = criteriaBuilder.equal( rootMaxVersionSubQuery.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ),
                    UUID.fromString( userId ) );
            Predicate predicateLifeCycleStatusWithOwnerVisibleAndOwnerId = criteriaBuilder.and( predicateLifeCycleStatusWithOwnerVisible,
                    predicateOwnerId );
            Predicate predicateLifeCycleStatusWithAnyVisible = rootMaxVersionSubQuery.get( ConstantsDAO.LIFECYCLE_STATUS )
                    .in( anyVisibleStatus );
            Predicate predicateOtherThanOwnerId = criteriaBuilder.notEqual(
                    rootMaxVersionSubQuery.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ), UUID.fromString( userId ) );
            Predicate predicateLifeCycleStatusWithVisibleOtherThanOwnerId = criteriaBuilder.and( predicateLifeCycleStatusWithAnyVisible,
                    predicateOtherThanOwnerId );
            Predicate or = criteriaBuilder.or( predicateLifeCycleStatusWithOwnerVisibleAndOwnerId,
                    predicateLifeCycleStatusWithVisibleOtherThanOwnerId );
            Predicate and = criteriaBuilder.conjunction();
            and.getExpressions().add( or );
            maxVersionPredicates.add( and );
        }
        maxVersionSubQuery.where( maxVersionPredicates.toArray( Predicate[]::new ) );
        return maxVersionSubQuery;
    }

    /**
     * Gets the max version criteria query.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param entityClazz
     *         the entity clazz
     *
     * @return the max version criteria query
     */
    protected Subquery< Integer > getMaxVersionCriteriaQuery( CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > paginatedFilteredCriteria,
            Root< ? > filteredCriteriaRoot, Class< ? > entityClazz ) {
        List< Predicate > maxVersionPredicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = paginatedFilteredCriteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( entityClazz );
        maxVersionSubQuery.select(
                criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionPredicates.add( maxIdPredicate );
        maxVersionSubQuery.where( maxVersionPredicates.toArray( Predicate[]::new ) );
        return maxVersionSubQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getLatestChildByParent( EntityManager entityManager, Class< ? > entityClazz, UUID parentId ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
            criteriaQuery.orderBy( criteriaBuilder.desc( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ) ) );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            if ( null != parentId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                        .value( relationSubquery ) );
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( entityClazz, criteriaBuilder, criteriaQuery,
                        filteredCriteriaRoot );
                predicates.add( maxVersionCriteriaQueryPredicate );
            } else {
                predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.ID ), parentId ) );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            E result = ( E ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).setMaxResults( 1 )
                    .getResultList().stream().findFirst().orElse( null );
            entityManager.getTransaction().begin();
            entityManager.getTransaction().commit();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Adds the criteria to composite key.
     *
     * @param entityClazz
     *         the entity clazz
     * @param session
     *         the session
     * @param subquery
     *         the subquery
     *
     * @return the criteria
     */
    protected Criteria addCriteriaToCompositeKey( Class< ? > entityClazz, Session session, DetachedCriteria subquery ) {
        Criteria paginatedCriteria = session.createCriteria( entityClazz, "out" )
                .add( Subqueries.propertyIn( ConstantsDAO.COMPOSED_ID_ID, subquery ) );
        DetachedCriteria maxVersionSubQuery = DetachedCriteria.forClass( entityClazz, "sub" );
        ProjectionList proj = Projections.projectionList();
        proj.add( Projections.max( "sub." + ConstantsDAO.COMPOSED_ID_VERSION_ID ) );
        maxVersionSubQuery.add( Restrictions.eqProperty( "out." + ConstantsDAO.COMPOSED_ID_ID, "sub." + ConstantsDAO.COMPOSED_ID_ID ) );
        maxVersionSubQuery.setProjection( proj );
        paginatedCriteria.add( Subqueries.propertyIn( ConstantsDAO.COMPOSED_ID_VERSION_ID, maxVersionSubQuery ) );
        return paginatedCriteria;
    }

    /**
     * Adds the sorting in criteria query.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filtersDTO
     *         the filters DTO
     * @param joinTranslation
     *         the join translation
     */
    protected void addSortingInCriteriaQuery( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            CriteriaQuery< ? > paginatedFilteredCriteria, FiltersDTO filtersDTO, Join< ?, TranslationEntity > joinTranslation ) {
        boolean hasSorting = false;
        boolean tableHasVersion = false;
        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( filterColumn.getDir() ) ) {
                    hasSorting = mergeSortFiltersToCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot,
                            paginatedFilteredCriteria, filterColumn, joinTranslation );
                }
                if ( !tableHasVersion ) {
                    tableHasVersion = ConstantsDAO.COMPOSED_ID_VERSION_ID.equals( filterColumn.getName() );
                }
            }

        }
        if ( !hasSorting && ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            paginatedFilteredCriteria.orderBy(
                    criteriaBuilder.desc( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        }
        if ( !tableHasVersion && !hasSorting && ReflectionUtils.hasField( entityClazz, ConstantsDAO.CREATED_ON ) ) {
            paginatedFilteredCriteria.orderBy( criteriaBuilder.desc( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ) ) );
        }
    }

    /**
     * Add sorting in criteria query.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filtersDTO
     *         the filters dto
     */
    protected void addSortingInCriteriaQuery( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            CriteriaQuery< ? > paginatedFilteredCriteria, FiltersDTO filtersDTO ) {
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO, null );
    }

    /**
     * Merge sort filters to criteria query.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filterColumn
     *         the filter column
     * @param joinTranslation
     *         the join translation
     *
     * @return true, if successful
     */
    protected boolean mergeSortFiltersToCriteriaQuery( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder,
            Root< ? > filteredCriteriaRoot, CriteriaQuery< ? > paginatedFilteredCriteria, FilterColumn filterColumn,
            Join< ?, TranslationEntity > joinTranslation ) {
        boolean hasSorting = true;
        if ( SuSEntity.class.isAssignableFrom( entityClazz ) && joinTranslation != null && isTranslationColumn( filterColumn.getName() ) ) {
            return mergeSortFiltersToCriteriaQueryForTranslationColumn( criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria,
                    filterColumn, joinTranslation, hasSorting );
        } else {
            return mergeSortFiltersToCriteriaQueryForGeneralColumns( entityClazz, criteriaBuilder, filteredCriteriaRoot,
                    paginatedFilteredCriteria, filterColumn, hasSorting );
        }
    }

    /**
     * Merge sort filters to criteria query for general columns boolean.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filterColumn
     *         the filter column
     * @param hasSorting
     *         the has sorting
     *
     * @return the boolean
     */
    private boolean mergeSortFiltersToCriteriaQueryForGeneralColumns( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder,
            Root< ? > filteredCriteriaRoot, CriteriaQuery< ? > paginatedFilteredCriteria, FilterColumn filterColumn, boolean hasSorting ) {
        Path< String > expression;
        if ( filterColumn.getName().equals( ConstantsDAO.TYPE ) && !ReflectionUtils.hasField( entityClazz, ConstantsDAO.TYPE ) ) {
            expression = filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID );
        } else if ( filterColumn.getName().equals( ConstantsDAO.ID ) ) {
            expression = filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID );
        } else {
            expression = getExpression( filteredCriteriaRoot, filterColumn.getName() );
        }
        // translatedName
        if ( filterColumn.getDir().equalsIgnoreCase( ConstantsString.SORTING_DIRECTION_ASCENDING ) ) {
            paginatedFilteredCriteria.orderBy( criteriaBuilder.asc( expression ) );
        } else {
            paginatedFilteredCriteria.orderBy( criteriaBuilder.desc( expression ) );
        }
        return hasSorting;
    }

    /**
     * Merge sort filters to criteria query for translation column boolean.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filterColumn
     *         the filter column
     * @param joinTranslation
     *         the join translation
     * @param hasSorting
     *         the has sorting
     *
     * @return the boolean
     */
    private boolean mergeSortFiltersToCriteriaQueryForTranslationColumn( CriteriaBuilder criteriaBuilder, Root< ? > filteredCriteriaRoot,
            CriteriaQuery< ? > paginatedFilteredCriteria, FilterColumn filterColumn, Join< ?, TranslationEntity > joinTranslation,
            boolean hasSorting ) {
        if ( filterColumn.getName().equals( ConstantsDAO.NAME ) ) {
            if ( filterColumn.getDir().equalsIgnoreCase( ConstantsString.SORTING_DIRECTION_ASCENDING ) ) {
                paginatedFilteredCriteria.orderBy( criteriaBuilder.asc( criteriaBuilder.selectCase()
                        .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                                        criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ),
                                filteredCriteriaRoot.get( ConstantsDAO.NAME ) ).otherwise( joinTranslation.get( ConstantsDAO.NAME ) ) ) );
            } else {
                paginatedFilteredCriteria.orderBy( criteriaBuilder.desc( criteriaBuilder.selectCase()
                        .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                                        criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ),
                                filteredCriteriaRoot.get( ConstantsDAO.NAME ) ).otherwise( joinTranslation.get( ConstantsDAO.NAME ) ) ) );
            }
            return hasSorting;
        }
        return false;
    }

    /**
     * Merge sort filters to criteria query boolean.
     *
     * @param entityClazz
     *         the entity clazz
     * @param criteriaBuilder
     *         the criteria builder
     * @param filteredCriteriaRoot
     *         the filtered criteria root
     * @param paginatedFilteredCriteria
     *         the paginated filtered criteria
     * @param filterColumn
     *         the filter column
     *
     * @return the boolean
     */
    protected boolean mergeSortFiltersToCriteriaQuery( Class< ? > entityClazz, CriteriaBuilder criteriaBuilder,
            Root< ? > filteredCriteriaRoot, CriteriaQuery< ? > paginatedFilteredCriteria, FilterColumn filterColumn ) {
        return mergeSortFiltersToCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filterColumn,
                null );
    }

    /**
     * Gets the filtered results.
     *
     * @param entityClazz
     *         the entity clazz
     * @param id
     *         the id
     * @param filtersDTO
     *         the filters DTO
     * @param isDeleted
     *         the is deleted
     * @param objectTypeList
     *         the object type list
     *
     * @return the filtered results
     */
    protected DetachedCriteria getFilteredResults( Class< ? > entityClazz, UUID id, FiltersDTO filtersDTO, boolean isDeleted,
            List< ObjectType > objectTypeList ) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass( entityClazz );
        Disjunction or = Restrictions.disjunction();
        Conjunction and = Restrictions.conjunction();
        if ( filtersDTO.getColumns() != null ) {
            Map< String, String > map = new HashMap<>();
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                String alias = ConstantsString.EMPTY_STRING;
                String aliasColumn = ConstantsString.EMPTY_STRING;
                String fieldName;
                if ( filterColumn.getName().contains( ConstantsString.DOT ) && !filterColumn.getName()
                        .contains( ConstantsDAO.COMPOSED_ID ) ) {
                    fieldName = filterColumn.getName()
                            .substring( ConstantsInteger.INTEGER_VALUE_ZERO, filterColumn.getName().indexOf( ConstantsString.DOT ) );
                    alias = RandomStringUtils.random( ConstantsInteger.INTEGER_VALUE_TEN, true, false );
                    if ( ReflectionUtils.hasField( entityClazz, fieldName ) && isHibernateEntity(
                            ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                        if ( filterColumn.getName().split( "\\." ).length == ConstantsInteger.INTEGER_VALUE_TWO ) {
                            aliasColumn = addAliasToCriteria( detachedCriteria, map, filterColumn, alias, fieldName );
                        } else if ( filterColumn.getName().split( "\\." ).length >= ConstantsInteger.INTEGER_VALUE_THREE ) {
                            aliasColumn = generateAliasForNestedBeans( filterColumn.getName(), detachedCriteria, map );
                        }
                    }
                } else {
                    fieldName = filterColumn.getName();
                }
                if ( filterColumn.getFilters() != null ) {
                    for ( Filter filter : filterColumn.getFilters() ) {
                        try {
                            if ( !alias.isEmpty() ) {
                                populateJunctionForAliasColumn( filterColumn.getName(), aliasColumn, filter, or, and, entityClazz );
                                // Hard code composed id and version id to critaria
                            } else if ( filterColumn.getName().equals( ConstantsDAO.COMPOSED_ID_ID ) ) {
                                populateJunction( filterColumn.getName(), UUID.fromString( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( filterColumn.getName().equals( ConstantsDAO.COMPOSED_ID_VERSION_ID ) || filterColumn.getName()
                                    .endsWith( ConstantsDAO.COMPOSED_ID_VERSION_ID ) ) {
                                populateJunction( filterColumn.getName(), Integer.parseInt( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( filterColumn.getName().equals( ConstantsDAO.TYPE ) ) {
                                if ( CollectionUtils.isNotEmpty( objectTypeList ) ) {
                                    populateJunctionForWITHTable( filter.getValue(), filter.getCondition(), filter.getOperator(), or, and,
                                            objectTypeList );
                                }
                            } else if ( Integer.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) )
                                    || int.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Integer.parseInt( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Double.parseDouble( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), Float.parseFloat( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), UUID.fromString( filter.getValue() ), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( Boolean.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                if ( ConstantsStatus.ACTIVE.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), true, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.DISABLE.equalsIgnoreCase( filter.getValue() )
                                        || ConstantsStatus.INACTIVE.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), false, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.YES.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), true, filter.getCondition(), filter.getOperator(), or, and );
                                } else if ( ConstantsStatus.NO.equalsIgnoreCase( filter.getValue() ) ) {
                                    populateJunction( filterColumn.getName(), false, filter.getCondition(), filter.getOperator(), or, and );
                                } else {
                                    populateJunction( filterColumn.getName(), null, filter.getCondition(), filter.getOperator(), or, and );
                                }
                            } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunctionForDates( filterColumn.getName(), filter.getValue(), filter.getCondition(),
                                        filter.getOperator(), or, and );
                            } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, fieldName ) ) ) {
                                populateJunction( filterColumn.getName(), filter.getValue(), filter.getCondition(), filter.getOperator(),
                                        or, and );
                            }
                        } catch ( Exception e ) {
                            ExceptionLogger.logException( e, getClass() );
                            throw new SusException(
                                    MessageBundleFactory.getMessage( Messages.INVALID_VALUE_PROVIDED_FOR_COLUMN.getKey(), filter.getValue(),
                                            filterColumn.getName() ) );
                        }
                    }
                }
                // Add search on any column
                Criterion criterion = searchOnAnyColumn( filtersDTO.getSearch(), filterColumn.getName(), alias, aliasColumn, entityClazz );
                if ( null != criterion ) {
                    and.add( criterion );
                }
            }
        }
        detachedCriteria.add( and );
        detachedCriteria.add( or );
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
            detachedCriteria.add( Restrictions.eq( ConstantsDAO.IS_DELETE, isDeleted ) );
        }
        if ( id != null ) {
            detachedCriteria.add( Restrictions.eq( ConstantsDAO.COMPOSED_ID_ID, id ) );
        }
        return detachedCriteria;
    }

    /**
     * Populate junction for WITH table.
     *
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     * @param objectTypeList
     *         the object type list
     */
    private void populateJunctionForWITHTable( String value, String condition, String operator, Disjunction or, Conjunction and,
            List< ObjectType > objectTypeList ) {
        String operatorValue;
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            operatorValue = "=  " + ConstantsString.STANDARD_SINGLE_QUOTE + value + ConstantsString.STANDARD_SINGLE_QUOTE;
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            operatorValue = "!=  " + ConstantsString.STANDARD_SINGLE_QUOTE + value + ConstantsString.STANDARD_SINGLE_QUOTE;
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            operatorValue = "like '%" + value + "%'";
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            operatorValue = "not like '%" + value + "%'";
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            operatorValue = "like '" + value + "%'";
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            operatorValue = "like '%" + value + ConstantsString.STANDARD_SINGLE_QUOTE;
        } else {
            operatorValue = "like '%" + value + "%'";
        }
        StringBuilder builder = new StringBuilder();
        for ( ObjectType objectType : objectTypeList ) {
            builder.append( "('" ).append( objectType.getId() ).append( "','" ).append( objectType.getName() ).append( "')," );
        }
        int index = builder.lastIndexOf( ConstantsString.COMMA );
        builder = builder.replace( index, ConstantsString.COMMA.length() + index, ConstantsString.EMPTY_STRING );
        String tempTableQuery =
                " typeid in ( SELECT id from (VALUES   " + builder + " ) as types (id,name) where  name " + operatorValue + ")";
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.add( Restrictions.sqlRestriction( tempTableQuery ) );
        } else {
            and.add( Restrictions.sqlRestriction( tempTableQuery ) );
        }
    }

    /**
     * Populate junction for alias column.
     *
     * @param column
     *         the column
     * @param aliasColumn
     *         the alias column
     * @param filter
     *         the filter
     * @param or
     *         the or
     * @param and
     *         the and
     * @param entityClazz
     *         the entity clazz
     */
    private void populateJunctionForAliasColumn( String column, String aliasColumn, Filter filter, Disjunction or, Conjunction and,
            Class< ? > entityClazz ) {
        if ( int.class.isAssignableFrom( ReflectionUtils.getFieldTypeByNameForInnerFields( entityClazz, column ) ) ) {
            populateJunction( aliasColumn, Integer.parseInt( filter.getValue() ), filter.getCondition(), filter.getOperator(), or, and );
        } else if ( double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByNameForInnerFields( entityClazz, column ) ) ) {
            populateJunction( aliasColumn, Double.parseDouble( filter.getValue() ), filter.getCondition(), filter.getOperator(), or, and );
        } else if ( float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByNameForInnerFields( entityClazz, column ) ) ) {
            populateJunction( aliasColumn, Float.parseFloat( filter.getValue() ), filter.getCondition(), filter.getOperator(), or, and );
        } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByNameForInnerFields( entityClazz, column ) ) ) {
            populateJunction( aliasColumn, UUID.fromString( filter.getValue() ), filter.getCondition(), filter.getOperator(), or, and );
        } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByNameForInnerFields( entityClazz, column ) ) ) {
            populateJunction( aliasColumn, filter.getValue(), filter.getCondition(), filter.getOperator(), or, and );
        }
    }

    /**
     * Populate junction.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param condition
     *         the condition
     * @param operator
     *         the operator
     * @param or
     *         the or
     * @param and
     *         the and
     */
    private void populateJunction( String column, Object value, String condition, String operator, Disjunction or, Conjunction and ) {
        if ( operator.equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.eq( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ne( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.ANYWHERE ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and,
                    Restrictions.not( Restrictions.ilike( column, value.toString(), MatchMode.ANYWHERE ) ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.START ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ilike( column, value.toString(), MatchMode.END ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.gt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.ge( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.lt( column, value ) );
        } else if ( operator.equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            addLogicalOperatorToCriteria( condition, or, and, Restrictions.le( column, value ) );
        }
    }

    /**
     * Adds the logical operator to criteria.
     *
     * @param condition
     *         the condition
     * @param or
     *         the or
     * @param and
     *         the and
     * @param criterion
     *         the criterion
     */
    private void addLogicalOperatorToCriteria( String condition, Disjunction or, Conjunction and, Criterion criterion ) {
        if ( condition.equalsIgnoreCase( ConstantsOperators.OR.getName() ) ) {
            or.add( criterion );
        } else {
            and.add( criterion );
        }
    }

    /**
     * Search on any column.
     *
     * @param searchQuery
     *         the search query
     * @param columnName
     *         the column name
     * @param alias
     *         the alias
     * @param aliasColumn
     *         the alias column
     * @param entityClazz
     *         the entity clazz
     *
     * @return the criterion
     */
    private Criterion searchOnAnyColumn( String searchQuery, String columnName, String alias, String aliasColumn, Class< ? > entityClazz ) {
        Criterion criterion = null;
        try {
            if ( StringUtils.isNotBlank( searchQuery ) ) {
                if ( !alias.isEmpty() ) {
                    criterion = Restrictions.ilike( aliasColumn, searchQuery, MatchMode.ANYWHERE );
                } else if ( Integer.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.eq( columnName, Integer.parseInt( searchQuery ) );
                } else if ( Double.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.eq( columnName, Double.parseDouble( searchQuery ) );
                } else if ( Float.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.eq( columnName, Float.parseFloat( searchQuery ) );
                } else if ( UUID.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.eq( columnName, UUID.fromString( searchQuery ) );
                } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.eq( columnName, DateUtils.fromString( searchQuery, ConstantsDate.DATE_ONLY_FORMAT ) );
                } else if ( String.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    criterion = Restrictions.ilike( columnName, searchQuery, MatchMode.ANYWHERE );
                } else if ( Boolean.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( entityClazz, columnName ) ) ) {
                    if ( ConstantsStatus.ACTIVE.toLowerCase().contains( searchQuery.toLowerCase() ) ) {
                        criterion = Restrictions.eq( columnName, true );
                    } else if ( ConstantsStatus.DISABLE.toLowerCase().contains( searchQuery.toLowerCase() ) ) {
                        criterion = Restrictions.eq( columnName, false );
                    }
                }
            }
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
        }
        return criterion;
    }

    /**
     * Checks if is search columnName.
     *
     * @param columnName
     *         the columnName
     * @param entityClazz
     *         the entity clazz
     *
     * @return true, if is search columnName
     */
    public boolean isSearchColumn( String columnName, Class< ? > entityClazz ) {

        if ( entityClazz.isAssignableFrom( UserTokenEntity.class ) ) {
            return columnName.equalsIgnoreCase( ConstantsDAO.USER_USERUID );
        }
        return columnName.equalsIgnoreCase( ConstantsDAO.NAME ) || columnName.equalsIgnoreCase( ConstantsDAO.DESCRIPTION )
                || columnName.equalsIgnoreCase( ConstantsDAO.USER_UID ) || columnName.equalsIgnoreCase( ConstantsDAO.FIRST_NAME )
                || columnName.equalsIgnoreCase( ConstantsDAO.SUR_NAME ) || columnName.equalsIgnoreCase( ConstantsDAO.VARIABLE_NAME )
                || columnName.equalsIgnoreCase( ConstantsDAO.OBJECT_NAME ) || columnName.equalsIgnoreCase( ConstantsDAO.MODULE );
    }

    /**
     * Is translation column boolean.
     *
     * @param columnName
     *         the column name
     *
     * @return the boolean
     */
    public boolean isTranslationColumn( String columnName ) {
        return columnName.equalsIgnoreCase( ConstantsDAO.NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHibernateEntity( Class< ? > clazz ) {
        return clazz != null && clazz.isAnnotationPresent( Entity.class );
    }

    /**
     * Creates the new version.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the su S entity
     */
    public SuSEntity createNewVersion( EntityManager entityManager, SuSEntity entity ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Integer > criteria = criteriaBuilder.createQuery( Integer.class );
            Root< ? > root = criteria.from( entity.getClass() );
            criteria.select( criteriaBuilder.max( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate predicate1 = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                    entity.getComposedId().getId() );
            criteria.where( predicate1 );
            final int maxVersionId = entityManager.createQuery( criteria ).getSingleResult();
            entity.getComposedId().setVersionId( maxVersionId + SusConstantObject.VERSION_INCREMENT_SIZE );
            entity.setIndexId( UUID.randomUUID() );
            entityManager.getTransaction().begin();
            var updatedEntity = entityManager.merge( entity );
            entityManager.getTransaction().commit();
            return updatedEntity;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the paginated list by property.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     * @param filter
     *         the filter
     *
     * @return the paginated list by property
     */
    public List< E > getPaginatedListByProperty( EntityManager entityManager, String propertyName, Object value, FiltersDTO filter ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< E > criteria = criteriaBuilder.createQuery( this.entityClass );
        Root< E > root = criteria.from( this.entityClass );
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.ITEM_ORDER ) ) {
            criteria.orderBy( criteriaBuilder.asc( root.get( ConstantsDAO.ITEM_ORDER ) ) );
        }
        Predicate predicate1 = criteriaBuilder.equal( getExpression( root, propertyName ), value );
        criteria.where( predicate1 );
        list = entityManager.createQuery( criteria ).setHint( QueryHints.HINT_CACHEABLE, Boolean.TRUE ).setFirstResult( filter.getStart() )
                .setMaxResults( filter.getLength() ).getResultList();
        long totalRecords = getSelectionTotalCount( entityManager, propertyName, value );
        filter.setFilteredRecords( totalRecords );
        filter.setTotalRecords( totalRecords );
        return list;
    }

    /**
     * Gets the selection total count.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the selection total count
     */
    public Long getSelectionTotalCount( EntityManager entityManager, String propertyName, Object value ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        criteriaQuery.select( criteriaBuilder.count( root ) ).distinct( Boolean.TRUE );
        Predicate predicate1 = criteriaBuilder.equal( getExpression( root, propertyName ), value );
        criteriaQuery.where( predicate1 );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, Boolean.TRUE ).getSingleResult();
    }

    /**
     * Adds the max id projection to criteria.
     *
     * @param entityClazz
     *         the entity clazz
     * @param detachedCriteria
     *         the detached criteria
     * @param id
     *         the id
     */
    protected void addMaxIdProjectionToCriteria( Class< ? > entityClazz, DetachedCriteria detachedCriteria, UUID id ) {
        if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
            if ( id == null ) {
                addMaxComposedIdProjectionToCriteria( detachedCriteria );
            } else {
                detachedCriteria.setProjection( Projections.property( ConstantsDAO.COMPOSED_ID_ID ) );
            }
        } else {
            detachedCriteria.setProjection( Projections.distinct( Projections.property( ConstantsDAO.ID ) ) );
        }
    }

    /**
     * Adds the max composed id projection to criteria.
     *
     * @param detachedCriteria
     *         the detached criteria
     */
    private void addMaxComposedIdProjectionToCriteria( DetachedCriteria detachedCriteria ) {
        ProjectionList proj = Projections.projectionList();
        proj.add( Projections.property( ConstantsDAO.COMPOSED_ID_ID ) );
        detachedCriteria.setProjection( Projections.distinct( proj ) );
    }

    /**
     * Adds the alias to criteria.
     *
     * @param detachedCriteria
     *         the detached criteria
     * @param map
     *         the map
     * @param filterColumn
     *         the filter column
     * @param alias
     *         the alias
     * @param fieldName
     *         the field name
     *
     * @return the string
     */
    private String addAliasToCriteria( DetachedCriteria detachedCriteria, Map< String, String > map, FilterColumn filterColumn,
            String alias, String fieldName ) {
        String aliasColumn;
        if ( !map.containsKey( fieldName ) ) {
            detachedCriteria.createAlias( fieldName, alias, JoinType.LEFT_OUTER_JOIN );
            map.put( fieldName, alias );
            aliasColumn = alias + filterColumn.getName().substring( filterColumn.getName().indexOf( ConstantsString.DOT ) );
        } else {
            aliasColumn = map.get( fieldName ) + filterColumn.getName().substring( filterColumn.getName().indexOf( ConstantsString.DOT ) );
        }
        return aliasColumn;
    }

    /**
     * Generate alias for nested beans.
     *
     * @param fieldname
     *         the fieldname
     * @param detachedCriteria
     *         the detached criteria
     * @param map
     *         the map
     *
     * @return the string
     */
    private String generateAliasForNestedBeans( String fieldname, DetachedCriteria detachedCriteria, Map< String, String > map ) {
        String aliasColumn = ConstantsString.EMPTY_STRING;
        String parentAlias = ConstantsString.EMPTY_STRING;
        String currentField;
        while ( fieldname.contains( ConstantsString.DOT ) ) {
            currentField = fieldname.substring( ConstantsInteger.INTEGER_VALUE_ZERO, fieldname.indexOf( ConstantsString.DOT ) );
            fieldname = fieldname.substring( fieldname.indexOf( ConstantsString.DOT ) + 1 );
            if ( !parentAlias.isEmpty() ) {
                currentField = parentAlias + ConstantsString.DOT + currentField;
            }
            if ( !map.containsKey( currentField ) ) {
                aliasColumn = RandomStringUtils.random( ConstantsInteger.INTEGER_VALUE_TEN, true, false );
                detachedCriteria.createAlias( currentField, aliasColumn, JoinType.LEFT_OUTER_JOIN );
                map.put( currentField, aliasColumn );
            } else {
                aliasColumn = map.get( currentField );
            }
            parentAlias = aliasColumn;
        }
        return aliasColumn + ConstantsString.DOT + fieldname;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCountWithParentId( EntityManager entityManager, Class< ? > clazz, UUID parentId, String userId,
            List< String > ownerVisibleStatus, List< String > anyVisibleStatus, String userSecurityIDs, int permission ) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< Long > criteriaQuery = criteriaBuilder.createQuery( Long.class );
            Root< ? > root = criteriaQuery.from( clazz );
            criteriaQuery.select( criteriaBuilder.count( root ) );
            List< Predicate > predicates = new ArrayList<>();
            if ( null != parentId ) {
                Subquery< Relation > parentSubQuery = criteriaQuery.subquery( Relation.class );
                Root< Relation > rootRarentSubQuery = parentSubQuery.from( Relation.class );
                Predicate parentPredicate = criteriaBuilder.equal( rootRarentSubQuery.get( ConstantsDAO.PARENT ), parentId );
                parentSubQuery.where( parentPredicate );
                parentSubQuery.select( rootRarentSubQuery.get( ConstantsDAO.CHILD ) );
                predicates.add(
                        criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( parentSubQuery ) );
            }
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                predicates.add( criteriaBuilder.and( criteriaBuilder.or( criteriaBuilder.and(
                                ( criteriaBuilder.in( root.get( ConstantsDAO.LIFECYCLE_STATUS ) ).value( ownerVisibleStatus ) ),
                                criteriaBuilder.equal( root.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ), UUID.fromString( userId ) ) ),
                        criteriaBuilder.and( ( criteriaBuilder.in( root.get( ConstantsDAO.LIFECYCLE_STATUS ) ).value( anyVisibleStatus ) ),
                                criteriaBuilder.notEqual( root.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID ),
                                        UUID.fromString( userId ) ) ) ) ) );
            }
            if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
                Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
                Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( clazz );
                maxVersionSubQuery.select(
                        criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
                Predicate maxIdPredicate = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                        rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
                maxVersionSubQuery.where( maxIdPredicate );
                Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                        root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
                predicates.add( maxVersionCriteriaQueryPredicate );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            return entityManager.createQuery( criteriaQuery ).getSingleResult();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCountWithParentId( EntityManager entityManager, Class< ? > clazz, UUID parentId ) {
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass( clazz );
            addParentIdToCriteria( parentId, detachedCriteria );
            ProjectionList proj = Projections.projectionList();
            proj.add( Projections.max( ConstantsDAO.COMPOSED_ID_VERSION_ID ) );
            proj.add( Projections.groupProperty( ConstantsDAO.COMPOSED_ID_ID ) );
            detachedCriteria.add( Restrictions.eq( ConstantsDAO.IS_DELETE, false ) );
            Criteria c = detachedCriteria.getExecutableCriteria( session );
            Long result = ( Long ) c.setProjection( proj ).setProjection( Projections.rowCount() )
                    .setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY ).uniqueResult();
            session.getTransaction().commit();
            return result;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Adds the parent id to criteria.
     *
     * @param parentId
     *         the parent id
     * @param subquery
     *         the subquery
     */
    public void addParentIdToCriteria( UUID parentId, DetachedCriteria subquery ) {
        DetachedCriteria dcParent = DetachedCriteria.forClass( Relation.class );
        dcParent.add( Restrictions.eq( ConstantsDAO.PARENT, parentId ) );
        dcParent.setProjection( Projections.projectionList().add( Projections.property( ConstantsDAO.CHILD ) ) );
        subquery.add( Subqueries.propertyIn( ConstantsDAO.COMPOSED_ID_ID, dcParent ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserSecurityIDs( EntityManager entityManager, UUID id ) {
        StringBuilder builder = new StringBuilder();
        List< GroupEntity > groupEntities = getGroupsByUserId( entityManager, id );
        List< UUID > ids = groupEntities.stream().map( GroupEntity::getId ).collect( Collectors.toList() );
        List< AclSecurityIdentityEntity > aclSecurityIdentityEntities;
        if ( CollectionUtils.isNotEmpty( ids ) ) {
            ids.add( id );
            aclSecurityIdentityEntities = getAclSecurityIdentityByProperty( entityManager, ConstantsDAO.SID, ids );
            for ( AclSecurityIdentityEntity aclSecurityIdentityEntity : aclSecurityIdentityEntities ) {
                builder.append( ConstantsString.STANDARD_SINGLE_QUOTE ).append( aclSecurityIdentityEntity.getId() ).append( "'," );
            }
        } else {
            AclSecurityIdentityEntity aclSecurityIdentityEntity = getSecurityIdentity( entityManager, id );
            builder.append( ConstantsString.STANDARD_SINGLE_QUOTE ).append( aclSecurityIdentityEntity.getId() ).append( "'," );
        }
        int index = builder.lastIndexOf( ConstantsString.COMMA );
        builder = builder.replace( index, ConstantsString.COMMA.length() + index, ConstantsString.EMPTY_STRING );
        return builder.toString();
    }

    /**
     * Gets the acl security identity by property.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param ids
     *         the ids
     *
     * @return the acl security identity by property
     */
    private List< AclSecurityIdentityEntity > getAclSecurityIdentityByProperty( EntityManager entityManager, String propertyName,
            List< UUID > ids ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< AclSecurityIdentityEntity > sidCriteria = criteriaBuilder.createQuery( AclSecurityIdentityEntity.class );
        Root< AclSecurityIdentityEntity > sidRoot = sidCriteria.from( AclSecurityIdentityEntity.class );
        Predicate predicate1 = criteriaBuilder.equal( sidRoot.get( ConstantsDAO.IS_DELETE ), false );
        Predicate predicate2 = sidRoot.get( propertyName ).in( ids );
        sidCriteria.where( predicate1, predicate2 );
        List< AclSecurityIdentityEntity > securityIdentityEntities = entityManager.createQuery( sidCriteria )
                .setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        return securityIdentityEntities;
    }

    /**
     * Gets the groups by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the groups by user id
     */
    public List< GroupEntity > getGroupsByUserId( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< GroupEntity > groupCriteria = criteriaBuilder.createQuery( GroupEntity.class );
        Root< GroupEntity > groupRoot = groupCriteria.from( GroupEntity.class );
        Predicate predicate1 = criteriaBuilder.equal( groupRoot.join( ConstantsDAO.USERS ).get( ConstantsDAO.ID ), id );
        Predicate predicate2 = criteriaBuilder.equal( groupRoot.get( ConstantsDAO.IS_DELETE ), false );
        Predicate predicate3 = criteriaBuilder.equal( groupRoot.get( ConstantsDAO.STATUS ), true );
        groupCriteria.where( predicate1, predicate2, predicate3 );
        List< GroupEntity > groupEntities = entityManager.createQuery( groupCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return groupEntities;
    }

    /**
     * Gets the security identity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the security identity
     */
    private AclSecurityIdentityEntity getSecurityIdentity( EntityManager entityManager, UUID id ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< AclSecurityIdentityEntity > aclSecurityIdentityCriteria = criteriaBuilder.createQuery(
                AclSecurityIdentityEntity.class );
        Root< AclSecurityIdentityEntity > aclSecurityIdentityRoot = aclSecurityIdentityCriteria.from( AclSecurityIdentityEntity.class );
        Predicate aclSecurityIdentityCondition = criteriaBuilder.equal( aclSecurityIdentityRoot.get( ConstantsDAO.SID ), id );
        aclSecurityIdentityCriteria.where( aclSecurityIdentityCondition );
        aclSecurityIdentityCriteria.distinct( true );
        AclSecurityIdentityEntity aclSecurityIdentityEntity = entityManager.createQuery( aclSecurityIdentityCriteria )
                .setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return aclSecurityIdentityEntity;
    }

    /**
     * Gets the acl child tree from parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param parendId
     *         the parend id
     *
     * @return the acl child tree from parent id
     */
    public List< AclObjectIdentityEntity > getAclChildTreeFromParentId( EntityManager entityManager, UUID parendId ) {
        try {
            List< AclObjectIdentityEntity > childList = new ArrayList<>();
            getAllAclChildTreeFromParentIdRecursively( parendId, childList, entityManager );
            return childList;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the all acl child tree from parent id recursively.
     *
     * @param parendId
     *         the parend id
     * @param fullList
     *         the full list
     * @param entityManager
     *         the entity manager
     */
    private void getAllAclChildTreeFromParentIdRecursively( UUID parendId, List< AclObjectIdentityEntity > fullList,
            EntityManager entityManager ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< AclObjectIdentityEntity > criteria = cb.createQuery( AclObjectIdentityEntity.class );
            Root< AclObjectIdentityEntity > root = criteria.from( AclObjectIdentityEntity.class );
            Predicate predicate = cb.equal( getExpression( root, ConstantsDAO.PARENT_OBJECT_ID ), parendId );
            criteria.where( predicate );
            List< AclObjectIdentityEntity > children = entityManager.createQuery( criteria ).getResultList();
            if ( CollectionUtils.isNotEmpty( children ) ) {
                fullList.addAll( children );
                for ( AclObjectIdentityEntity acl : children ) {
                    getAllAclChildTreeFromParentIdRecursively( acl.getId(), fullList, entityManager );
                }
            }
        } catch ( Exception e ) {
            entityManager.close();
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the latest non deleted objects by list of id.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     *
     * @return the latest non deleted objects by list of id
     */
    public List< E > getLatestNonDeletedObjectsByListOfId( EntityManager entityManager, List< UUID > listOfIds ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            listOfIds.forEach(
                    id -> idPredicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, this.entityClass );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            listOfIds.forEach( id -> idPredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            List< E > listOfObjects = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
            return listOfObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets latest non deleted objects by list of id and filter.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     * @param filter
     *         the filter
     *
     * @return the latest non deleted objects by list of id and filter
     */
    public List< E > getLatestNonDeletedObjectsByListOfIdAndFilter( EntityManager entityManager, List< UUID > listOfIds,
            FiltersDTO filter ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();

        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            listOfIds.forEach(
                    id -> idPredicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, this.entityClass );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            listOfIds.forEach( id -> idPredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        }
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.IS_DELETE ) ) {
            idPredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        addFilterPredicatesToListOfPredicates( criteriaBuilder, root, this.entityClass, filter, null, predicates );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            List< E > listOfObjects = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
            filter.setFilteredRecords( ( long ) listOfObjects.size() );
            filter.setTotalRecords( ( long ) listOfObjects.size() );
            return listOfObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the objects by list of id.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     *
     * @return the objects by list of id
     */
    public List< E > getObjectsByListOfId( EntityManager entityManager, List< UUID > listOfIds ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) {
            listOfIds.forEach(
                    id -> idPredicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) ) );
            // apply OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, this.entityClass );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            listOfIds.forEach( id -> idPredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), id ) ) );
            // OR condition for ID matches
            predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            List< E > listOfObjects = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
            return listOfObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllJobsWithCreatedOnAndProperties( EntityManager entityManager, Class< ? > clazz, Date minDateProperty,
            String propertyKey, List< Object > properties ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
        Root< ? > root = criteriaQuery.from( clazz );
        criteriaQuery.distinct( true );
        List< Predicate > propertiesPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        properties.forEach( property -> propertiesPredicates.add( criteriaBuilder.equal( root.get( propertyKey ), property ) ) );
        predicates.add( criteriaBuilder.or( propertiesPredicates.toArray( Predicate[]::new ) ) );
        predicates.add( criteriaBuilder.lessThan( root.get( ConstantsDAO.FINISHED_ON ), minDateProperty ) );
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), Boolean.FALSE ) );
        }
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_AUTO_DELETE ), Boolean.FALSE ) );
        predicates.add( criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.CHILD.getKey() ) );
        predicates.add( criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey() ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            return ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getRecordsForFeStaticThread( EntityManager entityManager, Class< ? > clazz, Date latestDateProperty,
            Date previousLatestDateProperty, List< Object > lifeCycleProperties ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( this.entityClass );
        Root< ? > root = criteriaQuery.from( this.entityClass );
        List< Predicate > lifeCyclePredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        lifeCycleProperties.forEach(
                lifeCycle -> lifeCyclePredicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.LIFECYCLE_STATUS ), lifeCycle ) ) );
        // OR condition for All lifecycles
        predicates.add( criteriaBuilder.or( lifeCyclePredicates.toArray( Predicate[]::new ) ) );
        predicates.add( criteriaBuilder.between( root.get( ConstantsDAO.CREATED_ON ), previousLatestDateProperty, latestDateProperty ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), Boolean.TRUE ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_AUTO_DELETE ), Boolean.TRUE ) );
        if ( ReflectionUtils.hasField( this.entityClass, ConstantsDAO.COMPOSED_ID ) ) { // get max version if SusEntity
            Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, this.entityClass );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            List< E > listOfObjects = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
            return listOfObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllPreviousRecordsWithCreatedOnAndProperties( EntityManager entityManager, Class< ? > clazz, Date minDateProperty,
            String propertyKey, List< Object > properties, boolean jobExtract ) {
        List< Predicate > predicates = new ArrayList<>();
        try {
            List< E > list;
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( clazz );
            Root< ? > root = criteriaQuery.from( clazz );
            entityManager.getTransaction().begin();
            Subquery< Integer > maxVersionSubQuery = criteriaQuery.subquery( Integer.class );
            Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( this.entityClass );
            maxVersionSubQuery.select(
                    criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                    root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
            predicates.add( maxVersionCriteriaQueryPredicate );
            predicates.add( criteriaBuilder.lessThan( root.get( ConstantsDAO.CREATED_ON ), minDateProperty ) );
            Predicate lifeCycleStatusPredicate = criteriaBuilder.disjunction();
            for ( Object value : properties ) {
                lifeCycleStatusPredicate = criteriaBuilder.or( lifeCycleStatusPredicate,
                        criteriaBuilder.equal( root.get( propertyKey ), value ) );
            }
            predicates.add( lifeCycleStatusPredicate );
            if ( ReflectionUtils.hasField( clazz, ConstantsDAO.COMPOSED_ID ) ) {
                if ( jobExtract ) {
                    predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
                    predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_AUTO_DELETE ), false ) );
                    predicates.add(
                            criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_RELATION_TYPE ), JobRelationTypeEnums.CHILD.getKey() ) );
                    predicates.add( criteriaBuilder.notEqual( root.get( ConstantsDAO.JOB_TYPE ), JobTypeEnums.SYSTEM.getKey() ) );
                } else {
                    predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
                    predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_AUTO_DELETE ), false ) );
                }
            } else {
                predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
            }
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            list = ( List< E > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
            entityManager.getTransaction().commit();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllRecords( EntityManager entityManager ) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery< E > criteria = cb.createQuery( this.entityClass );
            Root< E > root = criteria.from( this.entityClass );
            criteria.select( root );
            criteria.distinct( true );
            return ( List< E > ) entityManager.createQuery( criteria ).getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllRecordsBetweenDates( EntityManager entityManager, Class< ? > entityClazz, Map< String, Object > params,
            Date dateFrom, Date dateTo ) {
        List< E > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
        List< Predicate > predicates = new ArrayList<>();
        if ( params != null && !params.isEmpty() ) {
            for ( Entry< String, Object > conditions : params.entrySet() ) {
                predicates.add( criteriaBuilder.isNotNull( filteredCriteriaRoot.get( conditions.getKey() ) ) );
            }
        }
        predicates.add(
                criteriaBuilder.and( criteriaBuilder.greaterThanOrEqualTo( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ), dateFrom ),
                        criteriaBuilder.lessThanOrEqualTo( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ), dateTo ) ) );
        paginatedFilteredCriteria.orderBy( criteriaBuilder.asc( filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ) ) );
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< E > getAllChildrenByParentIdWithPermission( EntityManager entityManager, Class< ? > entityClazz, UUID parentId,
            UUID userId, int permission ) {
        List< E > list;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.IS_DELETE ) ) {
                Predicate isDeletedPredicate = criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false );
                predicates.add( isDeletedPredicate );
            }
            if ( null != parentId ) {
                Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, paginatedFilteredCriteria );
                predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                        .value( relationSubquery ) );
            }
            if ( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ) {
                Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( entityClazz, criteriaBuilder, paginatedFilteredCriteria,
                        filteredCriteriaRoot );
                predicates.add( maxVersionCriteriaQueryPredicate );
            }
            if ( !userId.toString().equals( ConstantsID.SUPER_USER_ID ) ) {
                List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, userId );
                Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, paginatedFilteredCriteria,
                        filteredCriteriaRoot, entityClazz );
                Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
                predicates.add( maskPredicate );
            }
            paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
            list = ( List< E > ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }

    }

    /**
     * Gets the minimum mask.
     *
     * @param userSecurityIDs
     *         the user security I ds
     * @param permission
     *         the permission
     * @param criteriaBuilder
     *         the criteria builder
     * @param maxVersionSubQuery
     *         the max version sub query
     * @param rootMaxVersionSubQuery
     *         the root max version sub query
     * @param entityClazz
     *         the entity clazz
     *
     * @return the minimum mask
     */
    protected Subquery< Integer > getMinimumMaskSubQuery( List< UUID > userSecurityIDs, int permission, CriteriaBuilder criteriaBuilder,
            CriteriaQuery< ? > maxVersionSubQuery, Root< ? > rootMaxVersionSubQuery, Class< ? > entityClazz ) {

        Subquery< Integer > aclEntryCriteriaQuery = maxVersionSubQuery.subquery( Integer.class );
        Root< AclEntryEntity > aclEntryRoot = aclEntryCriteriaQuery.from( AclEntryEntity.class );

        aclEntryCriteriaQuery.select( criteriaBuilder.min( aclEntryRoot.get( ConstantsDAO.MASK ) ) );

        Predicate sIds = aclEntryRoot.join( ConstantsDAO.SECURITY_IDENTITY_ENTITY ).get( ConstantsDAO.ID ).in( userSecurityIDs );
        Predicate mask = criteriaBuilder.equal( aclEntryRoot.get( ConstantsDAO.MASK ), permission );

        Subquery< UUID > aclObjectCriteriaQuery = getAclObjectIdentityIdSubQuery( criteriaBuilder, aclEntryCriteriaQuery,
                rootMaxVersionSubQuery, entityClazz );

        Predicate predicateAceObjectId = criteriaBuilder.in(
                aclEntryRoot.join( ConstantsDAO.OBJECT_IDENTITY_ENTITY ).get( ConstantsDAO.ID ) ).value( aclObjectCriteriaQuery );

        return aclEntryCriteriaQuery.where( sIds, mask, predicateAceObjectId );
    }

    /**
     * Gets the acl object identity id.
     *
     * @param aclEntryCriteriaBuilder
     *         the acl entry criteria builder
     * @param aclEntryCriteriaQuery
     *         the acl entry criteria query
     * @param rootMaxVersionSubQuery
     *         the root max version sub query
     * @param entityClazz
     *         the entity clazz
     *
     * @return the acl object identity id
     */
    protected Subquery< UUID > getAclObjectIdentityIdSubQuery( CriteriaBuilder aclEntryCriteriaBuilder,
            Subquery< Integer > aclEntryCriteriaQuery, Root< ? > rootMaxVersionSubQuery, Class< ? > entityClazz ) {
        Subquery< UUID > aclObjectCriteriaQuery = aclEntryCriteriaQuery.subquery( UUID.class );
        Root< AclObjectIdentityEntity > aclObjectRoot = aclObjectCriteriaQuery.from( AclObjectIdentityEntity.class );
        aclObjectCriteriaQuery.select( aclObjectRoot.join( ConstantsDAO.FINAL_PARENT_OBJECT ).get( ConstantsDAO.ID ) );
        Predicate predicateWhereAceEqualsAco;
        if ( SuSEntity.class.isAssignableFrom( entityClazz ) ) {
            predicateWhereAceEqualsAco = aclEntryCriteriaBuilder.equal( aclObjectRoot.get( ConstantsDAO.ID ),
                    rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        } else {
            predicateWhereAceEqualsAco = aclEntryCriteriaBuilder.equal(
                    aclObjectRoot.get( ConstantsDAO.CLASS_ENTITY ).get( ConstantsDAO.ID ), rootMaxVersionSubQuery.get( ConstantsDAO.ID ) );
        }
        aclObjectCriteriaQuery.where( predicateWhereAceEqualsAco );
        return aclObjectCriteriaQuery;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllFilteredRecordsWithParentAndLifeCycleAndLanguageAndPermissionForList( EntityManager entityManager,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, List< ObjectType > objectTypes, String language, int permission ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        if ( !SuSEntity.class.isAssignableFrom( entityClazz ) ) {
            throw new SusDataBaseException(
                    MessageBundleFactory.getMessage( Messages.PROVIDED_CLASS_IS_NOT_A_DESCENDANT_OF_SUSENTITY.getKey(),
                            entityClazz.getName() ) );
        }
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
        Join< ?, TranslationEntity > joinTranslation = filteredCriteriaRoot.join( ConstantsDAO.TRANSLATION,
                javax.persistence.criteria.JoinType.LEFT );
        List< Predicate > predicates = new ArrayList<>();
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypes, criteriaBuilder,
                criteriaQuery, filteredCriteriaRoot, predicates, joinTranslation, language );
        criteriaQuery.multiselect( filteredCriteriaRoot.get( ConstantsDAO.COMPOSED_ID ),
                filteredCriteriaRoot.get( ConstantsDAO.LIFECYCLE_STATUS ), filteredCriteriaRoot.get( ConstantsDAO.CONFIG ),
                filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ),
                filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_ON ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_BY ),
                filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_BY ), criteriaBuilder.selectCase()
                        .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                                        criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ),
                                filteredCriteriaRoot.get( ConstantsDAO.NAME ) ).otherwise( joinTranslation.get( ConstantsDAO.NAME ) ),
                filteredCriteriaRoot.get( ConstantsDAO.DESCRIPTION ), filteredCriteriaRoot.get( ConstantsDAO.ENTITY_SIZE ),
                filteredCriteriaRoot.get( ConstantsDAO.ICON ), filteredCriteriaRoot.get( ConstantsDAO.SELECTED_TRANSLATIONS ) );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, criteriaQuery, filtersDTO, joinTranslation );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery,
                    filteredCriteriaRoot, entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        List< ? > list;
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );

        list = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        filtersDTO.setFilteredRecords(
                getFilteredCount( entityManager, criteriaBuilder, filtersDTO, entityClazz, parentId, userId, ownerVisibleStatus,
                        anyVisibleStatus, objectTypes, language, permission ) );
        filtersDTO.setTotalRecords(
                getCountWithParentIdAndPermission( entityClazz, parentId, userId, ownerVisibleStatus, anyVisibleStatus, entityManager,
                        criteriaBuilder, permission ) );
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getAllFilteredRecordsWithParentAndLifeCycleWithLanguageAndPermissionWithoutCount( EntityManager entityManager,
            Class< ? > entityClazz, FiltersDTO filtersDTO, UUID parentId, String userId, List< String > ownerVisibleStatus,
            List< String > anyVisibleStatus, List< ObjectType > objectTypes, String language, int permission ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        if ( !SuSEntity.class.isAssignableFrom( entityClazz ) ) {
            throw new SusDataBaseException(
                    MessageBundleFactory.getMessage( Messages.PROVIDED_CLASS_IS_NOT_A_DESCENDANT_OF_SUSENTITY.getKey(),
                            entityClazz.getName() ) );
        }
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( entityClazz );
        Root< ? > filteredCriteriaRoot = criteriaQuery.from( entityClazz );
        Join< ?, TranslationEntity > joinTranslation = filteredCriteriaRoot.join( ConstantsDAO.TRANSLATION,
                javax.persistence.criteria.JoinType.LEFT );
        List< Predicate > predicates = new ArrayList<>();
        preparePredicate( entityClazz, filtersDTO, parentId, userId, ownerVisibleStatus, anyVisibleStatus, objectTypes, criteriaBuilder,
                criteriaQuery, filteredCriteriaRoot, predicates, joinTranslation, language );
        criteriaQuery.multiselect( filteredCriteriaRoot.get( ConstantsDAO.COMPOSED_ID ),
                filteredCriteriaRoot.get( ConstantsDAO.LIFECYCLE_STATUS ), filteredCriteriaRoot.get( ConstantsDAO.CONFIG ),
                filteredCriteriaRoot.get( ConstantsDAO.TYPE_ID ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_ON ),
                filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_ON ), filteredCriteriaRoot.get( ConstantsDAO.CREATED_BY ),
                filteredCriteriaRoot.get( ConstantsDAO.MODIFIED_BY ), criteriaBuilder.selectCase()
                        .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                                        criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ),
                                filteredCriteriaRoot.get( ConstantsDAO.NAME ) ).otherwise( joinTranslation.get( ConstantsDAO.NAME ) ),
                filteredCriteriaRoot.get( ConstantsDAO.DESCRIPTION ), filteredCriteriaRoot.get( ConstantsDAO.ENTITY_SIZE ),
                filteredCriteriaRoot.get( ConstantsDAO.ICON ), filteredCriteriaRoot.get( ConstantsDAO.SELECTED_TRANSLATIONS ) );
        addSortingInCriteriaQuery( entityClazz, criteriaBuilder, filteredCriteriaRoot, criteriaQuery, filtersDTO, joinTranslation );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) ) {
            List< UUID > userSecurityIds = getUserSecurityIdList( entityManager, UUID.fromString( userId ) );
            Subquery< Integer > mask = getMinimumMaskSubQuery( userSecurityIds, permission, criteriaBuilder, criteriaQuery,
                    filteredCriteriaRoot, entityClazz );
            Predicate maskPredicate = criteriaBuilder.in( mask ).value( permission );
            predicates.add( maskPredicate );
        }
        List< ? > list;
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        list = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).setFirstResult( filtersDTO.getStart() )
                .setMaxResults( filtersDTO.getLength() ).getResultList();
        return list;
    }

    /**
     * Sets select expression for all properties.
     *
     * @param propertyName
     *         the property name
     * @param selectFrom
     *         the select from
     * @param criteriaQuery
     *         the criteria query
     */
    protected static void setSelectExpressionForAllProperties( String propertyName, From< ?, ? > selectFrom,
            CriteriaQuery< Object > criteriaQuery ) {
        if ( propertyName.contains( ConstantsString.DOT ) ) {
            String[] split = propertyName.split( "\\." );
            Path< ? > baseExpression = selectFrom.join( split[ 0 ] );
            for ( int i = 1; i < split.length; i++ ) {
                baseExpression = baseExpression.get( split[ i ] );
            }
            criteriaQuery.select( baseExpression ).distinct( true );
        } else {
            criteriaQuery.select( selectFrom.get( propertyName ) ).distinct( true );
        }
    }

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param clazz
     *         the clazz
     *
     * @return the all property values
     */
    protected List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName, Class< ? > clazz ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Object > criteriaQuery = criteriaBuilder.createQuery( Object.class );
        Root< ? > root = criteriaQuery.from( clazz );
        setSelectExpressionForAllProperties( propertyName, root, criteriaQuery );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( ReflectionUtils.hasField( clazz, ConstantsDAO.EXPIRED ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.EXPIRED ), false ) );
        }
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
    }

}
