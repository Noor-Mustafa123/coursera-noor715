package de.soco.software.simuspace.suscore.search.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.WidgetSettingsEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.search.dao.WidgetSettingsDAO;

/**
 * The Class WidgetSettingsDAOImpl.
 *
 * @author Ali Haider
 */
public class WidgetSettingsDAOImpl extends AbstractGenericDAO< WidgetSettingsEntity > implements WidgetSettingsDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetSettingsEntity getWidgetByViewId( EntityManager entityManager, UUID searchId ) {
        return getEntityWithDetails( entityManager, searchId );
    }

}
