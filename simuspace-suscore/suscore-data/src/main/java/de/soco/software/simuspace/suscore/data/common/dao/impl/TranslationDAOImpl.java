package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class TranslationDAOImpl.
 */
public class TranslationDAOImpl extends AbstractGenericDAO< TranslationEntity > implements TranslationDAO {

    /**
     * The susentity.
     */
    private final String SUSENTITY = "susentity";

    /**
     * Gets the all translations by list of ids and clazz.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     * @param clazz
     *         the clazz
     *
     * @return the all translations by list of ids and clazz
     */
    @Override
    public List< TranslationEntity > getAllTranslationsByListOfIds( EntityManager entityManager, List< VersionPrimaryKey > listOfIds ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( TranslationEntity.class );
        Root< ? > root = criteriaQuery.from( TranslationEntity.class );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        listOfIds.forEach( versionPrimaryKey -> idPredicates
                .add( criteriaBuilder.equal( root.get( SUSENTITY ).get( ConstantsDAO.COMPOSED_ID ), versionPrimaryKey ) ) );
        // OR condition for ID matches
        predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            return ( List< TranslationEntity > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the all translations by list of UUID.
     *
     * @param entityManager
     *         the entity manager
     * @param listOfIds
     *         the list of ids
     *
     * @return the all translations by list of UUID
     */
    @Override
    public List< TranslationEntity > getAllTranslationsByListOfUUID( EntityManager entityManager, List< UUID > listOfIds ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< ? > criteriaQuery = criteriaBuilder.createQuery( TranslationEntity.class );
        Root< ? > root = criteriaQuery.from( TranslationEntity.class );
        criteriaQuery.distinct( true );
        List< Predicate > idPredicates = new ArrayList<>();
        List< Predicate > predicates = new ArrayList<>();
        listOfIds.forEach( id -> idPredicates
                .add( criteriaBuilder.equal( root.join( SUSENTITY ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ), id ) ) );
        predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        Subquery< Integer > maxVersionSubQuery = getMaxVersionSubQuery( criteriaBuilder, criteriaQuery, root );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder
                .in( root.join( SUSENTITY ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );

        predicates.add( criteriaBuilder.or( idPredicates.toArray( Predicate[]::new ) ) );
        criteriaQuery.where( predicates.toArray( Predicate[]::new ) );
        try {
            return ( List< TranslationEntity > ) entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                    .getResultList();
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

    /**
     * Gets the max version sub query.
     *
     * @param criteriaBuilder
     *         the criteria builder
     * @param criteria
     *         the criteria
     * @param root
     *         the root
     *
     * @return the max version sub query
     */
    private Subquery< Integer > getMaxVersionSubQuery( CriteriaBuilder criteriaBuilder, CriteriaQuery< ? > criteria, Root< ? > root ) {
        List< Predicate > maxVersionPredicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = criteria.subquery( Integer.class );
        Root< ? > rootMaxVersionSubQuery = maxVersionSubQuery.from( SuSEntity.class );
        maxVersionSubQuery
                .select( criteriaBuilder.max( rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ) );
        Predicate maxIdPredicate = criteriaBuilder.equal( root.join( SUSENTITY ).get( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ),
                rootMaxVersionSubQuery.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.ID ) );
        maxVersionPredicates.add( maxIdPredicate );
        maxVersionSubQuery.where( maxVersionPredicates.toArray( Predicate[]::new ) );
        return maxVersionSubQuery;
    }

}
