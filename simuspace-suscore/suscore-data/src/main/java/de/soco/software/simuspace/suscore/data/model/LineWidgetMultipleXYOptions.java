package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

/**
 * The type Line widget multiple xy options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class LineWidgetMultipleXYOptions extends LineWidgetOptions {

    /**
     * The X axis.
     */
    @UIFormField( name = WIDGET_FIELDS.X_AXIS, title = "3000241x4", type = "select", section = "options", show = false, orderNum = 20 )
    private List< String > x_axis;

    /**
     * The Y axis.
     */
    @UIFormField( name = WIDGET_FIELDS.Y_AXIS, title = "3000242x4", type = "select", section = "options", show = false, orderNum = 21 )
    private List< String > y_axis;

    /**
     * The Styling.
     */
    @UIFormField( name = WIDGET_FIELDS.STYLING, title = "3000316x4", type = "select", section = "options", show = false, orderNum = 22 )
    private List< String > styling;

    /**
     * The Point symbol.
     */
    @UIFormField( name = WIDGET_FIELDS.POINT_SYMBOL, title = "3000322x4", type = "select", section = "options", show = false, orderNum = 23 )
    private List< String > pointSymbol;

}
