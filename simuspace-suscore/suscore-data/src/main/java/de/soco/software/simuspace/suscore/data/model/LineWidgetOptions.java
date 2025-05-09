package de.soco.software.simuspace.suscore.data.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.LINE_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

/**
 * The type Line widget options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class LineWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -89195254459496613L;

    /**
     * The X axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.X_AXIS_TITLE, title = "3000255x4", section = "options", orderNum = 12 )
    private String x_axis_title;

    /**
     * The Y axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.Y_AXIS_TITLE, title = "3000256x4", section = "options", orderNum = 13 )
    private String y_axis_title;

    /**
     * The Color theme.
     */
    @UIFormField( name = LINE_WIDGET_FIELDS.SMOOTH, title = "3000253x4", type = "select", section = "options", orderNum = 14 )
    private String smooth;

    /**
     * The Color theme.
     */
    @UIFormField( name = WIDGET_FIELDS.COLOR_THEME, title = "3000244x4", type = "select", section = "options", orderNum = 15 )
    private String colorTheme;

    /**
     * The Line widget type.
     */
    @UIFormField( name = LINE_WIDGET_FIELDS.LINE_WIDGET_TYPE, title = "3000289x4", type = "select", section = "options", required = true, orderNum = 19 )
    private String lineWidgetType;

}
