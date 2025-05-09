package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.server.dao.WFSchemeDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class WFSchemeDAOImpl extends AbstractGenericDAO< WFSchemeEntity > implements WFSchemeDAO {

    @Override
    public List< WFSchemeEntity > getLatestNonDeletedWFSchemeListByProperty( EntityManager entityManager, String propertyName,
            Object value ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< WFSchemeEntity > criteriaQuery = criteriaBuilder.createQuery( WFSchemeEntity.class );
        Root< WFSchemeEntity > root = criteriaQuery.from( WFSchemeEntity.class );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, WFSchemeEntity.class );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );
        predicates.add( criteriaBuilder.equal( getExpression( root, propertyName ), value ) );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );

        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        List< WFSchemeEntity > object = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return object;
    }

    @Override
    public List< WFSchemeEntity > getLatestNonDeletedWFSchemeList( EntityManager entityManager ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< WFSchemeEntity > criteriaQuery = criteriaBuilder.createQuery( WFSchemeEntity.class );
        Root< WFSchemeEntity > root = criteriaQuery.from( WFSchemeEntity.class );
        criteriaQuery.distinct( true );
        List< Predicate > predicates = new ArrayList<>();
        Subquery< Integer > maxVersionSubQuery = getMaxVersionCriteriaQuery( criteriaBuilder, criteriaQuery, root, WFSchemeEntity.class );
        Predicate maxVersionCriteriaQueryPredicate = criteriaBuilder.in(
                root.join( ConstantsDAO.COMPOSED_ID ).get( ConstantsDAO.VERSION_ID ) ).value( maxVersionSubQuery );
        predicates.add( maxVersionCriteriaQueryPredicate );
        predicates.add( criteriaBuilder.equal( root.get( ConstantsDAO.IS_DELETE ), false ) );

        criteriaQuery.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        List< WFSchemeEntity > object = entityManager.createQuery( criteriaQuery ).setHint( QueryHints.HINT_CACHEABLE, true )
                .getResultList();
        return object;
    }

}
