package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MIX_CHART_WIDGET_FIELDS;
import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MixChartWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -2182016034633755473L;

    /**
     * The Chart options.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTIONS, title = "3000323x4", type = "hidden", section = "Curve Options", orderNum = 11 )
    private List< MixChartCurveOptions > curveOptions;

    /**
     * The Add chart.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.ADD_CURVE, title = "3000324x4", type = "button", section = "Curve Options", orderNum = Integer.MAX_VALUE )
    private String addCurve;

    /**
     * The X axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.X_AXIS_TITLE, title = "3000255x4", section = "options", orderNum = 30 )
    private String x_axis_title;

    /**
     * The Y axis title.
     */
    @UIFormField( name = WIDGET_FIELDS.Y_AXIS_TITLE, title = "3000256x4", section = "options", orderNum = 31 )
    private String y_axis_title;

    /**
     * The Color theme.
     */
    @UIFormField( name = WIDGET_FIELDS.COLOR_THEME, title = "3000244x4", type = "select", section = "options", orderNum = 32 )
    private String colorTheme;

    /**
     * Gets x axis.
     *
     * @return the x axis
     */
    @JsonIgnore
    public List< String > getX_axis() {
        if ( CollectionUtils.isEmpty( curveOptions ) ) {
            return null;
        }
        return curveOptions.stream().map( MixChartCurveOptions::getX_axis ).toList();
    }

    /**
     * Gets y axis.
     *
     * @return the y axis
     */
    @JsonIgnore
    public List< String > getY_axis() {
        if ( CollectionUtils.isEmpty( curveOptions ) ) {
            return null;
        }
        return curveOptions.stream().map( MixChartCurveOptions::getY_axis ).toList();
    }

}
