package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.jpa.QueryHints;

import de.soco.software.simuspace.server.dao.RegexDAO;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

public class RegexDAOImpl extends AbstractGenericDAO< RegexEntity > implements RegexDAO {

    @Override
    public RegexEntity saveRegex( EntityManager entityManager, RegexEntity regexEntity ) {
        return save( entityManager, regexEntity );
    }

    @Override
    public RegexEntity getRegex( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectById( entityManager, id );
    }

    @Override
    public RegexEntity updateRegex( EntityManager entityManager, RegexEntity regexEntity ) {
        return update( entityManager, regexEntity );
    }

    @Override
    public Boolean deleteRegex( EntityManager entityManager, RegexEntity regexEntity ) {
        delete( entityManager, regexEntity );
        return Boolean.TRUE;
    }

    @Override
    public List< RegexEntity > getRegexListBySelectionId( EntityManager entityManager, UUID id ) {
        return getNonDeletedObjectListByProperty( entityManager, RegexEntity.class, ConstantsDAO.SELECTION_ID, id );
    }

    @Override
    public List< RegexEntity > getRegexListBySelectionId( EntityManager entityManager, UUID selectionId, FiltersDTO filtersDTO ) {
        List< RegexEntity > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< RegexEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( RegexEntity.class );
        Root< RegexEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( RegexEntity.class );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( RegexEntity.class, filtersDTO, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.SELECTION_ID ), selectionId ) );
        addSortingInCriteriaQuery( RegexEntity.class, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        list = entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();

        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.SELECTION_ID, selectionId );

        filtersDTO.setFilteredRecords( getAllFilteredRecordsCount( entityManager, RegexEntity.class, properties, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCount( entityManager, RegexEntity.class, properties ) );
        return list;

    }

}
