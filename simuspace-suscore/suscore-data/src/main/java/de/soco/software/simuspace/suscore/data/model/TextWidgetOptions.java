package de.soco.software.simuspace.suscore.data.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Text widget options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class TextWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3638414101409925134L;

    /**
     * The Input method.
     */
    @UIFormField( name = DataDashboardConstants.TEXT_WIDGET_FIELDS.INPUT_METHOD, title = "3000327x4", type = "select", section = "options", required = true, orderNum = 11 )
    private String inputMethod;

    /**
     * The Display value.
     */
    @UIFormField( name = DataDashboardConstants.TEXT_WIDGET_FIELDS.DISPLAY_VALUE, title = "3000328x4", section = "options", required = true, orderNum = 12 )
    private String displayValue;

    /**
     * The Column.
     */
    @UIFormField( name = DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN, title = "3000325x4", type = "select", section = "options", required = true, orderNum = 12 )
    private String column;

    /**
     * The Column title.
     */
    @UIFormField( name = DataDashboardConstants.TEXT_WIDGET_FIELDS.COLUMN_TITLE, title = "3000326x4", section = "options", orderNum = 13 )
    private String columnTitle;

    /*
    Temporarily commented-out till text widget themes are implemented on front-end
    @UIFormField( name = DataDashboardConstants.WIDGET_FIELDS.COLOR_THEME, title = "3000244x4", type = "select", section = "options", orderNum = 14 )
    private String colorTheme;
    */

}
