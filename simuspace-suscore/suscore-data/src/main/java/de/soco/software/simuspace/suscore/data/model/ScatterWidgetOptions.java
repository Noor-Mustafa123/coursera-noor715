package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.SCATTER_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Scatter widget dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class ScatterWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -1467889470399043113L;

    /**
     * The X axis.
     */
    @UIFormField( name = WIDGET_FIELDS.X_AXIS, title = "3000241x4", type = "select", section = "options", required = true, orderNum = 11 )
    private String x_axis;

    /**
     * The X axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.X_AXIS_TITLE, title = "3000255x4", section = "options", orderNum = 12 )
    private String x_axis_title;

    /**
     * The Y axis.
     */
    @UIFormField( name = WIDGET_FIELDS.Y_AXIS, title = "3000242x4", type = "select", section = "options", required = true, orderNum = 13 )
    private String y_axis;

    /**
     * The Y axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.Y_AXIS_TITLE, title = "3000256x4", section = "options", orderNum = 14 )
    private String y_axis_title;

    /**
     * The Point size.
     */
    @UIFormField( name = SCATTER_WIDGET_FIELDS.POINT_SIZE, title = "3000248x4", type = "select", section = "options", orderNum = 15 )
    private String pointSize;

    /**
     * The Point color.
     */
    @UIFormField( name = SCATTER_WIDGET_FIELDS.POINT_COLOR, title = "3000243x4", type = "select", section = "options", orderNum = 16 )
    private String pointColor;

    /**
     * The Point symbol.
     */
    @UIFormField( name = WIDGET_FIELDS.POINT_SYMBOL, title = "3000322x4", type = "select", section = "options", orderNum = 17 )
    private String pointSymbol;

    /**
     * The Color theme.
     */
    @UIFormField( name = WIDGET_FIELDS.COLOR_THEME, title = "3000244x4", type = "select", section = "options", orderNum = 18 )
    private String colorTheme;

}
