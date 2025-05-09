package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class BmwCaeBenchDAOImpl.
 *
 * @author noman arshad
 */
public class BmwCaeBenchDAOImpl extends AbstractGenericDAO< BmwCaeBenchEntity > implements BmwCaeBenchDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, UUID > getAllBmwFilteredRecordsByProperty( EntityManager entityManager, String node, FiltersDTO filter,
            String caeBenchType ) {
        Class< ? > entityClazz = BmwCaeBenchEntity.class;
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            predicates.add(
                    criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.BMW_CAE_BENCH_NODE_ENTITY ).get( ConstantsDAO.NODE ),
                            node ) );
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.BMW_CAE_DATA_TYPE ), caeBenchType ) );
            paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );

            List< BmwCaeBenchEntity > list = ( List< BmwCaeBenchEntity > ) entityManager.createQuery( paginatedFilteredCriteria )
                    .getResultList();
            return list.stream().collect(
                    Collectors.toMap( entity -> ( String ) entity.getOid(), entity -> ( UUID ) entity.getId(), ( existing, duplicate ) -> {
                        return existing;
                    } ) );

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    @Override
    public List< BmwCaeBenchEntity > getAllBmwFilteredRecordsByPropertyWithoutNode( EntityManager entityManager, FiltersDTO filter,
            String caeBenchType ) {
        Class< ? > entityClazz = BmwCaeBenchEntity.class;
        try {
            DetachedCriteria subquery = getFilteredResults( entityClazz, null, filter, false, new ArrayList<>() );
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery< ? > paginatedFilteredCriteria = criteriaBuilder.createQuery( entityClazz );
            Root< ? > filteredCriteriaRoot = paginatedFilteredCriteria.from( entityClazz );
            List< Predicate > predicates = new ArrayList<>();
            predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.BMW_CAE_DATA_TYPE ), caeBenchType ) );
            predicates.add( ReflectionUtils.hasField( entityClazz, ConstantsDAO.COMPOSED_ID ) ? criteriaBuilder.equal(
                    filteredCriteriaRoot.get( ConstantsDAO.IS_DELETE ), false )
                    : criteriaBuilder.in( filteredCriteriaRoot.get( ConstantsDAO.ID ) ).value( subquery ) );
            paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );

            return ( List< BmwCaeBenchEntity > ) entityManager.createQuery( paginatedFilteredCriteria ).getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    @Override
    public Map< String, UUID > getAllBmwFilteredRecordsSetByPropertyWithoutNode( EntityManager entityManager, FiltersDTO filter,
            String caeBenchType ) {
        return getAllExistingOids( entityManager, BmwCaeBenchEntity.class, ConstantsDAO.BMW_CAE_DATA_TYPE, caeBenchType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< BmwCaeBenchEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs ) {

        List< BmwCaeBenchEntity > allObjects = new ArrayList<>();
        Object object = null;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            for ( Object id : childs ) {
                object = ( BmwCaeBenchEntity ) session.createCriteria( BmwCaeBenchEntity.class )
                        .add( Restrictions.eq( ConstantsDAO.ID, id ) ).uniqueResult();
                if ( object != null ) {
                    allObjects.add( ( BmwCaeBenchEntity ) object );
                }
            }
            session.getTransaction().commit();
            return allObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

}
