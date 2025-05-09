package de.soco.software.simuspace.suscore.search.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.soco.software.simuspace.suscore.common.model.WidgetSettingsDTO;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.WidgetSettingsEntity;
import de.soco.software.simuspace.suscore.search.dao.WidgetSettingsDAO;
import de.soco.software.simuspace.suscore.search.manager.WidgetSettingsManager;

/**
 * The Class WidgetSettingsManagerImpl.
 *
 * @author Ali Haider
 */
public class WidgetSettingsManagerImpl implements WidgetSettingsManager {

    /**
     * The widgets DAO.
     */
    private WidgetSettingsDAO widgetsDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetSettingsDTO saveWidgetSetting( EntityManager entityManager, UUID userId, WidgetSettingsEntity widgetSettingsEntity )
            throws Exception {
        if ( widgetSettingsEntity == null ) {
            throw new Exception( "Exception" );
        }
        widgetSettingsEntity = widgetsDAO.save( entityManager, widgetSettingsEntity );
        return prepareDTO( widgetSettingsEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetSettingsEntity getWidgetSettingsById( EntityManager entityManager, UUID searchId ) {
        return widgetsDAO.getWidgetByViewId( entityManager, searchId );
    }

    /**
     * Gets the widgets DAO.
     *
     * @return the widgets DAO
     */
    public WidgetSettingsDAO getWidgetsDAO() {
        return widgetsDAO;
    }

    /**
     * Prepare DTO.
     *
     * @param widgetSettingsEntity
     *         the widget settings entity
     *
     * @return the widget settings DTO
     */
    private WidgetSettingsDTO prepareDTO( WidgetSettingsEntity widgetSettingsEntity ) {
        ObjectMapper mapper = new ObjectMapper();

        HashMap< String, Object > settings = new HashMap<>();

        try {
            settings = mapper.readValue( widgetSettingsEntity.getSettings(), new TypeReference<>() {
            } );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, WidgetSettingsManagerImpl.class );
        }

        return new WidgetSettingsDTO( widgetSettingsEntity.getId().toString(), widgetSettingsEntity.getViewName(), settings );
    }

    /**
     * Sets the widgets DAO.
     *
     * @param widgetsDAO
     *         the new widgets DAO
     */
    public void setWidgetsDAO( WidgetSettingsDAO widgetsDAO ) {
        this.widgetsDAO = widgetsDAO;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
