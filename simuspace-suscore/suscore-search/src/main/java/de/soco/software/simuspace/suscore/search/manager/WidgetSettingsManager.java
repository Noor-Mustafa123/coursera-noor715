package de.soco.software.simuspace.suscore.search.manager;

import javax.persistence.EntityManager;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.WidgetSettingsDTO;
import de.soco.software.simuspace.suscore.data.entity.WidgetSettingsEntity;

/**
 * The Interface WidgetSettingsManager.
 *
 * @author Ali Haider
 */
public interface WidgetSettingsManager {

    /**
     * Save widget setting.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param widgetSettingsBO
     *         the widget settings BO
     *
     * @return the widget settings DTO
     *
     * @throws Exception
     *         the exception
     */
    WidgetSettingsDTO saveWidgetSetting( EntityManager entityManager, UUID userId, WidgetSettingsEntity widgetSettingsBO ) throws Exception;

    /**
     * Gets the widget settings by id.
     *
     * @param entityManager
     *         the entity manager
     * @param searchId
     *         the search id
     *
     * @return the widget settings by id
     */
    WidgetSettingsEntity getWidgetSettingsById( EntityManager entityManager, UUID searchId );

}
