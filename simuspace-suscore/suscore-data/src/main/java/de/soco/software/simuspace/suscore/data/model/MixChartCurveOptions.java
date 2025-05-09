package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.MIX_CHART_WIDGET_FIELDS;

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
 * The type Widget 2 d chart options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = false )
@JsonIgnoreProperties( ignoreUnknown = true )
public class MixChartCurveOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 2800879536634858190L;

    /**
     * The Curve type.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_CURVE_TYPE, title = "3000321x4", type = "select", section = "Curve Options", required = true, orderNum = 11 )
    private String curveType;

    /**
     * The Multi x.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_X_AXIS, title = "3000241x4", type = "select", section = "Curve Options", required = true, orderNum = 12 )
    private String x_axis;

    /**
     * The Multi y.
     */
    @UIFormField( name = MIX_CHART_WIDGET_FIELDS.CURVE_OPTION_Y_AXIS, title = "3000242x4", type = "select", section = "Curve Options", required = true, orderNum = 13 )
    private String y_axis;

}
