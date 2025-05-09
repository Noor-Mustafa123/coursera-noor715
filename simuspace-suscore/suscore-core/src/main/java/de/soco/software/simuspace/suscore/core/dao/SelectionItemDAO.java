package de.soco.software.simuspace.suscore.core.dao;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

/**
 * The SelectionItemDAO The Interface will be responsible for all the database operations necessary for dealing with the selections.
 *
 * @author Noman Arshad
 */
public interface SelectionItemDAO extends GenericDAO< SelectionItemEntity > {

    /**
     * Gets the paginated selection by properties.
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
     * @return the paginated selection by properties
     */
    List< SelectionItemEntity > getPaginatedSelectionByProperties( EntityManager entityManager, String propertyName, Object value,
            FiltersDTO filter );

    /**
     * Gets the selection items by propery.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param value
     *         the value
     *
     * @return the selection items by propery
     */
    List< SelectionItemEntity > getSelectionItemsByProperty( EntityManager entityManager, String propertyName, Object value );

}
