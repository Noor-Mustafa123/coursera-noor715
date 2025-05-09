package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Group widget options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class GroupWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -7311756803410338783L;

    /**
     * The Widgets.
     */
    @UIFormField( name = WIDGET_FIELDS.WIDGETS, title = "4000006x4", type = "hidden", section = "options", orderNum = 11 )
    private Set< String > widgets;

}