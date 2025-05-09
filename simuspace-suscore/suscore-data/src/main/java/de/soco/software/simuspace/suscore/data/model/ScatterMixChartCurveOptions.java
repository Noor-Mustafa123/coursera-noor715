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
 * The type Scatter 2 d chart options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class ScatterMixChartCurveOptions extends MixChartCurveOptions {

    /**
     * The Mix point symbol.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_POINT_SYMBOL, title = "3000322x4", type = "select", section = "Curve Options", orderNum = 20 )
    private String pointSymbol;

}
