package de.soco.software.simuspace.suscore.object.dao.impl;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.object.dao.DataDAO;

/**
 * The Class will be responsible for all the database operations necessary for dealing with the User.
 */
public class DataDAOImpl extends AbstractGenericDAO< DataObjectEntity > implements DataDAO {

    @Override
    public SuSEntity getLatestNonDeletedObjectViaCache( EntityManager entityManager, UUID id ) {
        SuSEntity suSEntity = null;
        Integer maxId = getMaxEntity( id, entityManager );
        if ( maxId != null ) {
            EntityManagerFactory entityManagerFactory = entityManager.getEntityManagerFactory();
            Cache cache = entityManagerFactory.getCache();
            VersionPrimaryKey versionPrimaryKey = new VersionPrimaryKey( id, maxId );
            if ( cache.contains( SuSEntity.class, versionPrimaryKey ) ) {
                suSEntity = entityManager.find( SuSEntity.class, versionPrimaryKey );
                if ( suSEntity.isDelete() ) {
                    return null;
                }
            } else {
                // the data is NOT cached
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery< SuSEntity > criteriaQuery = criteriaBuilder.createQuery( SuSEntity.class );
                Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );
                List< Predicate > predicates = new ArrayList<>();
                predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );
                Predicate predicateId = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
                predicates.add( predicateId );
                Predicate maxPredicateId = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ),
                        maxId );
                predicates.add( maxPredicateId );
                criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
                suSEntity = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
            }
        }
        return suSEntity;
    }

    private Integer getMaxEntity( UUID id, EntityManager entityManager ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< Integer > criteriaQuery = criteriaBuilder.createQuery( Integer.class );
        Root< SuSEntity > root = criteriaQuery.from( SuSEntity.class );
        criteriaQuery.select( criteriaBuilder.max( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id );
        criteriaQuery.where( maxIdPredicate );
        return entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true ).getSingleResult();
    }

    private List< Expression< ? > > susEntityExpression( Root< SuSEntity > root ) {
        List< Expression< ? > > expressions = new ArrayList<>();

        Expression< VersionPrimaryKey > composedId = root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID );
        expressions.add( composedId );
        Expression< String > name = root.get( ConstantsDAO.NAME );
        expressions.add( name );
        Expression< String > lifeCycleStatus = root.get( ConstantsDAO.LIFECYCLE_STATUS );
        expressions.add( lifeCycleStatus );
        Expression< String > description = root.get( ConstantsDAO.DESCRIPTION );
        expressions.add( description );
        Expression< String > config = root.get( ConstantsDAO.CONFIG );
        expressions.add( config );
        Expression< UUID > typeId = root.get( ConstantsDAO.TYPE_ID );
        expressions.add( typeId );
        Expression< CustomAttributeEntity > customAttributes = root.join( ConstantsDAO.CUSTOM_ATTRIBUTES ).get( ConstantsDAO.ID );
        expressions.add( customAttributes );
        Expression< DocumentEntity > metaDataDocument = root.join( "metaDataDocument" ).join( ConstantsDAO.COMPOSED_ID )
                .get( ConstantsDAO.ID );
        expressions.add( metaDataDocument );
        Expression< UUID > workflowId = root.get( ConstantsDAO.WORKFLOW_ID );
        expressions.add( workflowId );
        Expression< Date > createdOn = root.get( ConstantsDAO.CREATED_ON );
        expressions.add( createdOn );
        Expression< Date > modifiedOn = root.get( ConstantsDAO.MODIFIED_ON );
        expressions.add( modifiedOn );
        Expression< Boolean > isDelete = root.get( ConstantsDAO.IS_DELETE );
        expressions.add( isDelete );
        Expression< AuditLogEntity > auditLogEntity = root.join( "auditLogEntity" ).get( ConstantsDAO.ID );
        expressions.add( auditLogEntity );
        Expression< UserEntity > owner = root.join( ConstantsDAO.OWNER ).get( ConstantsDAO.ID );
        expressions.add( owner );
        Expression< String > userSelectionId = root.get( "userSelectionId" );
        expressions.add( userSelectionId );
        Expression< String > groupSelectionId = root.get( "groupSelectionId" );
        expressions.add( groupSelectionId );
        Expression< UserEntity > deletedBy = root.join( ConstantsDAO.DELETED_BY ).get( ConstantsDAO.ID );
        expressions.add( deletedBy );
        Expression< UserEntity > createdBy = root.join( ConstantsDAO.CREATED_BY ).get( ConstantsDAO.ID );
        expressions.add( createdBy );
        Expression< UserEntity > modifiedBy = root.join( ConstantsDAO.MODIFIED_BY ).get( ConstantsDAO.ID );
        expressions.add( modifiedBy );
        Expression< UUID > jobId = root.get( ConstantsDAO.JOB_ID );
        expressions.add( jobId );
        Expression< String > path = root.get( ConstantsDAO.PATH );
        expressions.add( path );
        Expression< String > icon = root.get( ConstantsDAO.ICON );
        expressions.add( icon );
        Expression< Long > entitySize = root.get( ConstantsDAO.ENTITY_SIZE );
        expressions.add( entitySize );
        Expression< String > link = root.get( ConstantsDAO.LINK );
        expressions.add( link );
        Expression< Boolean > isAutoDelete = root.get( ConstantsDAO.IS_AUTO_DELETE );
        expressions.add( isAutoDelete );
        Expression< UUID > indexId = root.get( ConstantsDAO.INDEX_ID );
        expressions.add( indexId );
        Expression< UUID > users = root.get( ConstantsDAO.USERS );
        expressions.add( users );
        Expression< UUID > groups = root.get( "groups" );
        expressions.add( groups );
        Expression< Boolean > hidden = root.get( ConstantsDAO.IS_HIDDEN );
        expressions.add( hidden );
        return expressions;
    }

}