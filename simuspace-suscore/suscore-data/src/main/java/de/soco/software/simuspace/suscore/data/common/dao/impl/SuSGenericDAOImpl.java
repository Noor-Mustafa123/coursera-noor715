package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.Category;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.MetaDataFile;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;

/**
 * Implementation class of SuSGenericDAO.
 *
 * @author Nosheen.Sharif
 */
public class SuSGenericDAOImpl extends AbstractGenericDAO< SuSEntity > implements SuSGenericObjectDAO< SuSEntity > {

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity createAnObject( EntityManager entityManager, SuSEntity entity ) {
        return save( entityManager, entity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity updateAnObject( EntityManager entityManager, SuSEntity entity ) {
        return createNewVersion( entityManager, entity );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Relation createRelation( EntityManager entityManager, Relation r ) {
        return saveRelation( entityManager, r );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getParents( EntityManager entityManager, SuSEntity entity ) {
        return getRelations( entityManager, entity, true, null, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getDeletedParents( EntityManager entityManager, SuSEntity t ) {
        return getRelations( entityManager, t, true, null, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getChildren( EntityManager entityManager, SuSEntity t ) {
        return getRelations( entityManager, t, false, null, false );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getParents( EntityManager entityManager, SuSEntity suSEntity, ObjectTreeViewDTO filter ) {
        // will handle versioning query On woth versioning story
        return getLatestVersionRelations( entityManager, suSEntity, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getChildren( EntityManager entityManager, SuSEntity t, ObjectTreeViewDTO filter ) {
        // will handle versioning query later On woth versioning story
        return getLatestVersionRelations( entityManager, t, false );
    }

    /**
     * Given SUSEntity and filter it will list down the hierarichal relationships.
     *
     * @param entityManager
     *         the entityManager
     * @param entity
     *         the entity
     * @param getParents
     *         the get parents
     * @param filter
     *         the filter
     * @param isDeleted
     *         the is deleted
     *
     * @return the relations
     */
    private List< SuSEntity > getRelations( EntityManager entityManager, SuSEntity entity, boolean getParents, ObjectTreeViewDTO filter,
            boolean isDeleted ) {

        String columnName = ConstantsDAO.PARENT;
        String selectColumn = ConstantsDAO.CHILD;
        if ( getParents ) {
            columnName = ConstantsDAO.CHILD;
            selectColumn = ConstantsDAO.PARENT;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuSEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( SuSEntity.class );
        Root< SuSEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( SuSEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), isDeleted ) );
        }
        if ( null != entity.getComposedId().getId() ) {
            Subquery< UUID > subquery = paginatedFilteredCriteria.subquery( UUID.class );
            Root< Relation > subfrom = subquery.from( Relation.class );
            subquery.select( subfrom.get( selectColumn ) );
            Subquery< UUID > relationSubquery = subquery.where(
                    criteriaBuilder.equal( subfrom.get( columnName ), entity.getComposedId().getId() ),
                    criteriaBuilder.equal( subfrom.get( ConstantsDAO.TYPE ), 0 ) );
            predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                    .value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKeyForSusEntity( criteriaBuilder, paginatedFilteredCriteria,
                    filteredCriteriaRoot );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }

        if ( filter != null ) {

            if ( filter.getSort() != null && filter.getSort().getDir() != null ) {

                if ( filter.getSort().getDir().equalsIgnoreCase( "asc" ) ) {
                    paginatedFilteredCriteria.orderBy( criteriaBuilder.asc(
                            getExpression( filteredCriteriaRoot, getEntityAttributeFromParam( filter.getSort().getParam() ) ) ) );
                } else {
                    paginatedFilteredCriteria.orderBy( criteriaBuilder.desc(
                            getExpression( filteredCriteriaRoot, getEntityAttributeFromParam( filter.getSort().getParam() ) ) ) );
                }
            }

            if ( filter.getSearch() != null && !filter.getSearch().isEmpty() ) {
                predicates.add( criteriaBuilder.like( criteriaBuilder.lower( filteredCriteriaRoot.get( ConstantsDAO.NAME ) ),
                        "%" + filter.getSearch().toLowerCase() + "%" ) );
            }
        }
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< SuSEntity > list = entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
        return list;
    }

    /**
     * Given SUSEntity Latest Versions.
     *
     * @param susEntity
     *         the sus entity
     * @param getParents
     *         the get parents
     *
     * @return the latest version relations
     */
    private List< SuSEntity > getLatestVersionRelations( EntityManager entityManager, SuSEntity susEntity, boolean getParents ) {
        String columnName = ConstantsDAO.PARENT;
        String selectColumn = ConstantsDAO.CHILD;
        if ( getParents ) {
            columnName = ConstantsDAO.CHILD;
            selectColumn = ConstantsDAO.PARENT;
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< SuSEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( SuSEntity.class );
        Root< SuSEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( SuSEntity.class );
        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( null != susEntity.getComposedId().getId() ) {
            Subquery< UUID > subquery = paginatedFilteredCriteria.subquery( UUID.class );
            Root< Relation > subfrom = subquery.from( Relation.class );
            subquery.select( subfrom.get( selectColumn ) );
            Subquery< UUID > relationSubquery = subquery.where(
                    criteriaBuilder.equal( subfrom.get( columnName ), susEntity.getComposedId().getId() ),
                    criteriaBuilder.equal( subfrom.get( ConstantsDAO.TYPE ), 0 ) );
            predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                    .value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKeyForSusEntity( criteriaBuilder, paginatedFilteredCriteria,
                    filteredCriteriaRoot );
            predicates.add( maxVersionCriteriaQueryPredicate );
        }
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        List< SuSEntity > list = entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return list;
    }

    /**
     * Gets the entity attribute from param.
     *
     * @param orderParam
     *         the order param
     *
     * @return the entity attribute from param
     */
    private String getEntityAttributeFromParam( String orderParam ) {
        if ( orderParam.equalsIgnoreCase( ConstantsDAO.OWNER ) ) {
            return ConstantsDAO.OWNER_USER_UUID;
        }
        if ( orderParam.equalsIgnoreCase( "created-on" ) ) {
            return ConstantsDAO.CREATED_ON;
        }
        if ( orderParam.equalsIgnoreCase( "updated-on" ) ) {
            return ConstantsDAO.MODIFIED_ON;
        }

        return ConstantsDAO.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity saveOrUpdateObject( EntityManager entityManager, SuSEntity entity ) {
        return saveOrUpdate( entityManager, entity );

    }

    /**
     * {@inheritDoc}
     */

    @Override
    public List< SuSEntity > getListOfObjectByType( EntityManager entityManager, Class< ? > clazz ) {

        List< SuSEntity > result = null;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();

            String getListOfLatestObjectVersionHQL =
                    "from " + clazz.getName() + " wf" + " where wf.composedId.versionId= (select max(ff.composedId.versionId) from "
                            + clazz.getName() + " ff where wf.composedId.id =ff.composedId.id) " + "order by  wf.createdOn desc";

            final Query query = session.createQuery( getListOfLatestObjectVersionHQL );

            result = query.list();
            // Commit transaction

            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            result = null;
            throw new SusDataBaseException( e.getMessage() );
        }

        return result;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity addContainer( SuSEntity t ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeObject( SuSEntity t ) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeContainer( SuSEntity t ) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity addObjectToContainer( SuSEntity object, SuSEntity container ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity addObjectToMultipleContainer( SuSEntity object, List< SuSEntity > multipleContainer ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity changeAttributeOfAnObject( SuSEntity object ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity changeMetadataOfAnObject( SuSEntity object ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity addCustomAttributeToObject( SuSEntity object, CustomAttributeEntity customAttributeEntity ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity removeCustomAttributeFromObject( SuSEntity object, CustomAttributeEntity customAttributeEntity ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity changeCustomAttributeOfAnObject( SuSEntity object, CustomAttributeEntity customAttributeEntity ) {
        return null;
    }

    @Override
    public List< Object > getAllPropertyValuesByParentId( EntityManager entityManager, UUID parentId, String propertyName,
            String language ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Object > criteriaQuery = criteriaBuilder.createQuery( Object.class );
        Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );

        List< Predicate > predicates = new ArrayList<>();
        if ( propertyName.equalsIgnoreCase( ConstantsDAO.NAME ) ) {
            Join< ?, TranslationEntity > joinTranslation = root.join( ConstantsDAO.TRANSLATION, javax.persistence.criteria.JoinType.LEFT );
            criteriaQuery.select( criteriaBuilder.selectCase()
                    .when( criteriaBuilder.or( criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.NAME ) ),
                            criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.NAME ), "" ) ), root.get( ConstantsDAO.NAME ) )
                    .otherwise( joinTranslation.get( ConstantsDAO.NAME ) ) ).distinct( true );
            predicates.add( criteriaBuilder.or( criteriaBuilder.equal( joinTranslation.get( ConstantsDAO.LANGUAGE ), language ),
                    criteriaBuilder.isNull( joinTranslation.get( ConstantsDAO.LANGUAGE ) ) ) );
        } else if ( propertyName.contains( ConstantsString.DOT ) ) {
            String[] split = propertyName.split( "\\." );
            Path< ? > baseExpression = root.get( split[ 0 ] );
            for ( int i = 1; i < split.length; i++ ) {
                baseExpression = baseExpression.get( split[ i ] );
            }
            criteriaQuery.select( baseExpression ).distinct( true );
        } else {
            criteriaQuery.select( root.get( propertyName ) ).distinct( true );
        }
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, criteriaQuery );
            predicates.add( criteriaBuilder.in( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) ).value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( SuSEntity.class, criteriaBuilder, criteriaQuery, root );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.ID ), parentId ) );
        }

        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getTheTreeStructureFromJSONAtBoostrap() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity createNewVersionOfObject( SuSEntity object ) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getAllprojects() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getAllVersionsOfProject( SuSEntity project ) {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getAllObjectTypeInProject( SuSEntity project ) {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaDataFile getMetadataOfAnObject() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< DataObjectEntity > getListOfDataObjectsInContainer() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CustomAttributeEntity > getAllCustomAttributesOfObject() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category getTheCategoryOfTheProject() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity moveContainer() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeAttributeValueOfAnObject() {
        // Will be implemented later
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeMetadataOfObject() {
        // Will be implemented later
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity getObjectByCompositeId( EntityManager entityManager, Class< ? > clazz, UUID id, Integer versionId ) {
        return getObjectByCompositeId( entityManager, id, clazz, versionId );
    }

    @Override
    public SuSEntity getLatestObjectByTypeAndId( EntityManager entityManager, Class< ? > clazz, UUID id ) {
        SuSEntity suSEntity = null;
        Integer maxId = getMaxEntity( id, entityManager );
        if ( null == maxId ) {
            return null;
        }
        EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
        Cache cache = entityManagerFactory.getCache();
        VersionPrimaryKey versionPrimaryKey = new VersionPrimaryKey( id, maxId );
        if ( cache.contains( SuSEntity.class, versionPrimaryKey ) ) {
            suSEntity = entityManager.find( SuSEntity.class, versionPrimaryKey );
        } else {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< SuSEntity > criteriaQuery = criteriaBuilder.createQuery( SuSEntity.class );
            Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );
            List< Predicate > predicates = new ArrayList<>();
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            predicates.add( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ), maxId ) );
            criteriaQuery.orderBy( criteriaBuilder.desc( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
            criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
            suSEntity = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getResultList().stream()
                    .findFirst().orElse( null );
        }
        return suSEntity;
    }

    @Override
    public Long getAllRecordsCountWithParent( EntityManager entityManager, UUID parentId ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Long > paginatedFilteredCriteria = criteriaBuilder.createQuery( Long.class );
        Root< ContainerEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( ContainerEntity.class );

        paginatedFilteredCriteria.select( criteriaBuilder.count( filteredCriteriaRoot ) );

        List< Predicate > predicates = new ArrayList<>();
        if ( ReflectionUtils.hasField( ContainerEntity.class, ConstantsDAO.IS_DELETE ) ) {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false ) );
        }
        if ( null != parentId ) {
            Subquery< UUID > relationSubquery = prepareSubQueryToFetchChild( parentId, criteriaBuilder, paginatedFilteredCriteria );
            predicates.add( criteriaBuilder.in( filteredCriteriaRoot.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) )
                    .value( relationSubquery ) );
        }
        if ( ReflectionUtils.hasField( ContainerEntity.class, ConstantsDAO.COMPOSED_ID ) ) {
            Predicate maxVersionCriteriaQueryPredicate = addCompositeKey( ContainerEntity.class, criteriaBuilder, paginatedFilteredCriteria,
                    filteredCriteriaRoot );
            predicates.add( maxVersionCriteriaQueryPredicate );
        } else {
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.ID ), parentId ) );
        }
        paginatedFilteredCriteria.where( predicates.toArray( Predicate[]::new ) );
        Long list = entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return list;
    }

    /**
     * Gets the max entity.
     *
     * @param id
     *         the id
     * @param entityManager
     *         the entity manager
     *
     * @return the max entity
     */
    private Integer getMaxEntity( UUID id, EntityManager entityManager ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Integer > criteriaQuery = criteriaBuilder.createQuery( Integer.class );
        Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );
        criteriaQuery.select( criteriaBuilder.max( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
        criteriaQuery.where( maxIdPredicate );
        Integer maxId = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
        return maxId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getObjectVersionListById( EntityManager entityManager, Class< ? > clazz, UUID id ) {
        List< SuSEntity > result;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< SuSEntity > criteriaQuery = criteriaBuilder.createQuery( SuSEntity.class );
            Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );
            criteriaQuery.where( criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) );
            result = entityManager.createQuery( criteriaQuery ).getResultList();
            return result;
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SuSEntity getObjectByIdAndVersion( EntityManager entityManager, Class< ? > clazz, UUID id, int version ) {
        SuSEntity result = null;
        Session session = entityManager.unwrap( Session.class );
        try {

            session.beginTransaction();
            final Criteria cr = session.createCriteria( clazz );
            result = ( SuSEntity ) cr.add( Restrictions.eq( ConstantsDAO.COMPOSED_ID_ID, id ) )
                    .add( Restrictions.eq( ConstantsDAO.COMPOSED_ID_VERSION_ID, version ) ).uniqueResult();

            session.getTransaction().commit();
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
    public List< SuSEntity > getSiblingsBySameIName( EntityManager entityManager, String name, SuSEntity updateEntity, SuSEntity parent ) {

        List< SuSEntity > list = null;
        Session session = entityManager.unwrap( Session.class );

        try {
            session.beginTransaction();
            DetachedCriteria subquery = DetachedCriteria.forClass( SuSEntity.class );
            if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.IS_DELETE ) ) {

                subquery.add( Restrictions.eq( ConstantsDAO.IS_DELETE, false ) );
            }

            DetachedCriteria dcParent = DetachedCriteria.forClass( Relation.class );
            dcParent.add( Restrictions.eq( ConstantsDAO.PARENT, parent.getComposedId().getId() ) );
            dcParent.setProjection( Projections.projectionList().add( Projections.property( ConstantsDAO.CHILD ) ) );
            if ( updateEntity != null ) {
                subquery.add( Restrictions.ne( ConstantsDAO.COMPOSED_ID_ID, updateEntity.getComposedId().getId() ) );
            }

            subquery.add( Subqueries.propertyIn( ConstantsDAO.COMPOSED_ID_ID, dcParent ) );
            subquery.add( Restrictions.eq( "name", name ).ignoreCase() );

            addMaxIdProjectionToCriteria( SuSEntity.class, subquery, null );
            Criteria paginatedCriteria = session.createCriteria( SuSEntity.class );
            if ( ReflectionUtils.hasField( SuSEntity.class, ConstantsDAO.COMPOSED_ID ) ) {

                paginatedCriteria = addCriteriaToCompositeKey( SuSEntity.class, session, subquery );
                paginatedCriteria.add( Restrictions.eq( "name", name ).ignoreCase() );
            } else {

                paginatedCriteria = session.createCriteria( SuSEntity.class ).add( Subqueries.propertyIn( ConstantsDAO.ID, subquery ) );
            }

            list = paginatedCriteria.list();
            session.getTransaction().commit();
            return list;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }

    }

    /**
     * Generate sorting String for sorting query of HQL.
     *
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the sorting string for HQL
     */
    private String getSortingStringForHQL( FiltersDTO filtersDTO ) {
        String sort = "order by  wf.createdOn desc";

        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                if ( ValidationUtils.isNotNullOrEmpty( filterColumn.getDir() ) ) {
                    String columnName = filterColumn.getName();

                    if ( filterColumn.getName().equals( ConstantsDAO.ID ) ) {
                        columnName = ConstantsDAO.COMPOSED_ID_ID;
                    }

                    if ( filterColumn.getDir().equalsIgnoreCase( ConstantsString.SORTING_DIRECTION_ASCENDING ) ) {

                        sort = "order by  wf." + columnName + " asc";

                    } else {

                        sort = "order by  wf." + columnName + " desc";

                    }
                }
            }
        }

        return sort;
    }

    /**
     * Generate filters String for filter query of HQL.
     *
     * @param filtersDTO
     *         the filtersDTO
     * @param clazz
     *         the clazz
     *
     * @return the filters string for HQL
     */
    private String getFiltersStringForHQL( FiltersDTO filtersDTO, Class< ? > clazz ) {
        String filterString = "";

        if ( filtersDTO.getColumns() != null ) {
            for ( FilterColumn filterColumn : filtersDTO.getColumns() ) {
                String columnName = filterColumn.getName();
                if ( filterColumn.getName().contains( ConstantsString.DOT ) ) {
                    columnName = filterColumn.getName().substring( 0, filterColumn.getName().indexOf( ConstantsString.DOT ) );
                }
                if ( filterColumn.getName().equals( ConstantsDAO.ID ) ) {
                    columnName = ConstantsDAO.COMPOSED_ID_ID;
                }

                if ( filterColumn.getFilters() != null ) {
                    for ( Filter filter : filterColumn.getFilters() ) {
                        if ( filterColumn.getName().equals( ConstantsDAO.ID ) ) {
                            filterString = populateFiltersString( filterString, columnName, filter );
                        } else if ( Date.class.isAssignableFrom( ReflectionUtils.getFieldTypeByName( clazz, filterColumn.getName() ) ) ) {
                            filterString = populateFiltersStringforDates( filterString, columnName, filter );
                        } else {
                            filterString = populateFiltersString( filterString, columnName, filter );
                        }
                    }
                }
            }
        }

        if ( !filterString.isEmpty() ) {
            filterString = filterString.substring( 0, filterString.lastIndexOf( ' ' ) );
        }

        return filterString;
    }

    /**
     * Generate String for filter query of HQL.
     *
     * @param filterString
     *         the filterString
     * @param columnName
     *         the columnName
     * @param filter
     *         the filter
     *
     * @return the string
     */
    private String populateFiltersString( String filterString, String columnName, Filter filter ) {

        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + "='" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + "='" + filter.getValue() + "' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + "!='" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + "!='" + filter.getValue() + "' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_IN.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + " like '%" + filter.getValue() + "%' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + " like '%" + filter.getValue() + "%' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.IS_NOT_IN.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + " not like '%" + filter.getValue() + "%' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + " not like '%" + filter.getValue() + "%' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEGINS_WITH.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + " like '" + filter.getValue() + "%' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + " like '" + filter.getValue() + "%' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.ENDS_WITH.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + " like '%" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + " like '%" + filter.getValue() + "' " + filter.getCondition();
            }
        }

        return filterString;
    }

    /**
     * Generate String for filter query of HQL for Date fields.
     *
     * @param filterString
     *         the filterString
     * @param columnName
     *         the columnName
     * @param filter
     *         the filter
     *
     * @return the string
     */
    private String populateFiltersStringforDates( String filterString, String columnName, Filter filter ) {

        // preparing date of next day
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-d" );
        LocalDate inputDate = LocalDate.parse( filter.getValue(), formatter );
        inputDate = inputDate.plusDays( 1 );
        String tommorowDate = inputDate.format( formatter );

        if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.EQUALS.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + ">'" + filter.getValue() + "' ";
            } else {
                filterString = " wf." + columnName + ">'" + filter.getValue() + "' ";
            }
            filterString += "and wf." + columnName + "<'" + tommorowDate + "' " + filter.getCondition();
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.NOT_EQUALS.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + "<'" + filter.getValue() + "' ";
            } else {
                filterString = " wf." + columnName + "<'" + filter.getValue() + "' ";
            }
            filterString += "or wf." + columnName + ">='" + tommorowDate + "' " + filter.getCondition();
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + ">'" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + ">'" + filter.getValue() + "' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.AFTER_OR_EQUAL_TO.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + ">='" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + ">='" + filter.getValue() + "' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + "<'" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + "<'" + filter.getValue() + "' " + filter.getCondition();
            }
        } else if ( filter.getOperator().equalsIgnoreCase( ConstantsOperators.BEFORE_OR_EQUAL_TO.getName() ) ) {
            if ( filterString.isEmpty() ) {
                filterString = "and wf." + columnName + "<='" + filter.getValue() + "' " + filter.getCondition();
            } else {
                filterString += " wf." + columnName + "<='" + filter.getValue() + "' " + filter.getCondition();
            }
        }

        return filterString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Relation > getListByPropertyDesc( EntityManager entityManager, String propertyName, Object value ) {
        return getRelationListByProperty( entityManager, propertyName, value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteRelation( EntityManager entityManager, List< Relation > r ) {
        for ( Relation relation : r ) {
            deleteRelations( entityManager, relation );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs ) {
        return getLatestNonDeletedObjectsByListOfId( entityManager, childs );
    }

    @Override
    public List< SuSEntity > getLatestNonDeletedObjectsByListOfIdsAndFilter( EntityManager entityManager, List< UUID > selectedIdUUID,
            FiltersDTO filter ) {
        return getLatestNonDeletedObjectsByListOfIdAndFilter( entityManager, selectedIdUUID, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > getObjectsByListOfIds( EntityManager entityManager, List< UUID > childs ) {
        return getObjectsByListOfId( entityManager, childs );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ? > getListByPropertyMapAndClass( EntityManager entityManager, Map< String, Object > propertyMap, Class< ? > clazz ) {
        return getListByProperties( entityManager, propertyMap, clazz );
    }

}
