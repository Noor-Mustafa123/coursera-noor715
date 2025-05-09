package de.soco.software.simuspace.suscore.core.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.core.dao.SelectionItemDAO;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class SelectionItemDAOImpl responsible for providing DAO methods for selection .
 *
 * @author Noman Arshad
 */
public class SelectionItemDAOImpl extends AbstractGenericDAO< SelectionItemEntity > implements SelectionItemDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectionItemEntity > getPaginatedSelectionByProperties( EntityManager entityManager, String propertyName, Object value,
            FiltersDTO filter ) {
        return getPaginatedListByProperty( entityManager, propertyName, value, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectionItemEntity > getSelectionItemsByProperty( EntityManager entityManager, String propertyName, Object value ) {
        return getObjectListByProperty( entityManager, propertyName, value );
    }

}
