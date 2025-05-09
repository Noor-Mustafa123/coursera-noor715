package de.soco.software.simuspace.suscore.data.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class TableWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -1467889470399043113L;

    /**
     * The multi select column field.
     */
    @UIFormField( name = DataDashboardConstants.TABLE_WIDGET_FIELDS.TABLE_COLUMNS, title = "3000392x4", type = "select", section = "options", required = true, orderNum = 11, multiple = true )
    private List< String > tableColumns = new ArrayList<>();

}
