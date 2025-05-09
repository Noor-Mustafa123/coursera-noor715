package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.RADAR_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Bar widget dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class RadarWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2182016034633755473L;

    /**
     * The Indicator.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.INDICATOR, title = "3000330x4", type = "select", section = "Curve Options", required = true, orderNum = 11 )
    private String indicator;

    /**
     * The Add value.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.ADD_VALUE, type = "button", title = "3000331x4", section = "Curve Options", orderNum = 12 )
    private String addValue;

    /**
     * The Value.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.VALUE, title = "3000156x4", type = "select", section = "Curve Options", required = true, orderNum = 13 )
    private List< String > value;

    /**
     * The Styling.
     */
    @UIFormField( name = WIDGET_FIELDS.STYLING, title = "3000316x4", type = "select", section = "Curve Options", orderNum = 14 )
    private List< String > styling;

    /**
     * The Point symbol.
     */
    @UIFormField( name = WIDGET_FIELDS.POINT_SYMBOL, title = "3000322x4", type = "select", section = "Curve Options", orderNum = 15 )
    private List< String > pointSymbol;

    /**
     * The Max.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.MAX, title = "3000332x4", type = "select", section = "Curve Options", orderNum = 20 )
    private String max;

    /**
     * The Min.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.MIN, title = "3000333x4", type = "select", section = "Curve Options", orderNum = 21 )
    private String min;

    /**
     * The Shape.
     */
    @UIFormField( name = RADAR_WIDGET_FIELDS.SHAPE, title = "3000334x4", type = "select", section = "Curve Options", orderNum = 22 )
    private String shape;

    /**
     * The Color theme.
     */
    @UIFormField( name = WIDGET_FIELDS.COLOR_THEME, title = "3000244x4", type = "select", section = "options", orderNum = 23 )
    private String colorTheme;

}