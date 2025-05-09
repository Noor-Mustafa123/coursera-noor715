package de.soco.software.simuspace.suscore.search.dao;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.WidgetSettingsEntity;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;

public interface WidgetSettingsDAO extends GenericDAO< WidgetSettingsEntity > {

    /**
     * Gets the widget by view id.
     *
     * @param entityManager
     *         the entity manager
     * @param searchId
     *         the search id
     *
     * @return the widget by view id
     */
    WidgetSettingsEntity getWidgetByViewId( EntityManager entityManager, UUID searchId );

}
