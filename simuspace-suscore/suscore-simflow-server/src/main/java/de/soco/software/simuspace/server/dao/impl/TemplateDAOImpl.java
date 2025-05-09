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

import de.soco.software.simuspace.server.dao.TemplateDAO;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class TemplateDAOImpl.
 *
 * @author Fahad Rafi
 */
public class TemplateDAOImpl extends AbstractGenericDAO< TemplateEntity > implements TemplateDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TemplateEntity > getTemplateListBySelectionId( EntityManager entityManager, UUID selectionId, FiltersDTO filtersDTO ) {
        List< TemplateEntity > list;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery< TemplateEntity > paginatedFilteredCriteria = criteriaBuilder.createQuery( TemplateEntity.class );
        Root< TemplateEntity > filteredCriteriaRoot = paginatedFilteredCriteria.from( TemplateEntity.class );
        List< Predicate > predicates = getAllFilteredRecordsPredicates( TemplateEntity.class, filtersDTO, criteriaBuilder,
                paginatedFilteredCriteria, filteredCriteriaRoot );
        predicates.add( criteriaBuilder.equal( filteredCriteriaRoot.get( ConstantsDAO.SELECTION_ID ), selectionId ) );
        addSortingInCriteriaQuery( TemplateEntity.class, criteriaBuilder, filteredCriteriaRoot, paginatedFilteredCriteria, filtersDTO );
        paginatedFilteredCriteria.where( predicates.stream().toArray( predicate -> new Predicate[ predicate ] ) );
        list = entityManager.createQuery( paginatedFilteredCriteria ).setHint( QueryHints.HINT_CACHEABLE, true )
                .setFirstResult( filtersDTO.getStart() ).setMaxResults( filtersDTO.getLength() ).getResultList();

        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.SELECTION_ID, selectionId );

        filtersDTO.setFilteredRecords( getAllFilteredRecordsCount( entityManager, TemplateEntity.class, properties, filtersDTO ) );
        filtersDTO.setTotalRecords( getAllRecordsCount( entityManager, TemplateEntity.class, properties ) );
        return list;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateEntity saveTemplate( EntityManager entityManager, TemplateEntity templateEntity ) {
        return save( entityManager, templateEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TemplateEntity > getTemplateListBySelectionId( EntityManager entityManager, UUID selectionId ) {
        return getNonDeletedObjectListByProperty( entityManager, TemplateEntity.class, ConstantsDAO.SELECTION_ID, selectionId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deleteTemplate( EntityManager entityManager, TemplateEntity templateEntity ) {
        delete( entityManager, templateEntity );
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateEntity getTemplate( EntityManager entityManager, UUID id ) {
        return getLatestNonDeletedObjectById( entityManager, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateEntity updateTemplate( EntityManager entityManager, TemplateEntity templateEntity ) {
        return update( entityManager, templateEntity );
    }

}
