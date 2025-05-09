package de.soco.software.simuspace.suscore.homepage.manager;

import java.util.List;

import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.data.common.model.UserWidgetDTO;

/**
 * The interface Homepage manager.
 */
public interface HomepageManager {

    /**
     * Create widget form ui form.
     *
     * @param token
     *         the token
     *
     * @return the ui form
     */
    UIForm createWidgetForm( String token );

    /**
     * Create object form widget category ui form.
     *
     * @param userId
     *         the user id
     * @param widgetCategory
     *         the widget category
     *
     * @return the ui form
     */
    UIForm createObjectFormWidgetCategory( String userId, String widgetCategory );

    /**
     * Add new widget user widget dto.
     *
     * @param token
     *         the token
     * @param widgetJSON
     *         the widget json
     *
     * @return the user widget dto
     */
    UserWidgetDTO addNewWidget( String token, String widgetJSON );

    /**
     * Gets widgets list.
     *
     * @param userId
     *         the user id
     *
     * @return the widgets list
     */
    List< UserWidgetDTO > getWidgetsList( String userId );

    /**
     * Update widget user widget dto.
     *
     * @param token
     *         the token
     * @param widgetJSON
     *         the widget json
     *
     * @return the user widget dto
     */
    UserWidgetDTO updateWidget( String token, String widgetJSON );

    /**
     * Delete widget by selection boolean.
     *
     * @param widgetId
     *         the widget id
     * @param mode
     *         the mode
     *
     * @return the boolean
     */
    boolean deleteWidgetBySelection( String widgetId );

}
