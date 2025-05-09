package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MIX_CHART_WIDGET_FIELDS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Line chart options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class LineMixChartCurveOptions extends MixChartCurveOptions {

    /**
     * The Mix styling.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_STYLING, title = "3000316x4", type = "select", section = "Curve Options", orderNum = 20 )
    private String styling;

    /**
     * The Mix point symbol.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_POINT_SYMBOL, title = "3000322x4", type = "select", section = "Curve Options", orderNum = 21 )
    private String pointSymbol;

    /**
     * The Mix smooth.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_SMOOTH, title = "3000253x4", type = "select", section = "Curve Options", orderNum = 22 )
    private String smooth;

}
