package de.soco.software.simuspace.suscore.homepage.utility;

import java.util.List;

import de.soco.software.simuspace.suscore.common.enums.WidgetCategory;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.WidgetCategoryGroupDTO;
import de.soco.software.simuspace.suscore.common.model.WidgetDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The type Homepage widget util.
 */
public class HomepageWidgetUtil {

    /**
     * The constant WIDGETS.
     */
    private static final String WIDGETS = "widgets";

    /**
     * Gets widget category group dtos.
     *
     * @return the widget category group dtos
     */
    private static WidgetCategoryGroupDTO getWidgetCategoryGroupDTOS() {
        return PropertiesManager.getHomepageWidgets( WIDGETS );
    }

    /**
     * Gets widget dto list by widget category.
     *
     * @param widgetCategory
     *         the widget category
     *
     * @return the widget dto list by widget category
     */
    public static List< WidgetDTO > getWidgetDTOListByWidgetCategory( String widgetCategory ) {
        WidgetCategory category = WidgetCategory.getEnumById( widgetCategory );
        var config = getWidgetCategoryGroupDTOS();
        return switch ( category ) {
            case BUILT_IN -> config.getBuiltIn();
            case PREVIEW -> config.getPreview();
        };
    }

    /**
     * Gets auto true widget dt os.
     *
     * @return the auto true widget dt os
     */
    public static List< WidgetDTO > getAutoTrueWidgetDTOs() {
        var allWidgetsInCategory = getWidgetCategoryGroupDTOS();
        List< WidgetDTO > list = allWidgetsInCategory.getBuiltIn().stream().filter( WidgetDTO::isAuto ).toList();
        if ( list.isEmpty() ) {
            throw new SusException( "widgets with true value not found " );
        }
        return list;
    }

    /**
     * Gets widget dto by widget category and type.
     *
     * @param widgetCategory
     *         the widget category
     * @param widgetType
     *         the widget type
     *
     * @return the widget dto by widget category and type
     */
    public static WidgetDTO getWidgetDTOByWidgetCategoryAndType( String widgetCategory, String widgetType ) {
        var allWidgetsInCategory = getWidgetDTOListByWidgetCategory( widgetCategory );
        var optional = allWidgetsInCategory.stream().filter( widgetDTO -> widgetDTO.getName().equals( widgetType ) ).findFirst();
        if ( optional.isPresent() ) {
            return optional.get();
        }
        throw new SusException( "not found " + widgetType + " in category " + widgetCategory );
    }

}
