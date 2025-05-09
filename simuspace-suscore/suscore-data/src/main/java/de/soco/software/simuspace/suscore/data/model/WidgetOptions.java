package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.WIDGET_FIELDS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class WidgetOptions {

    /**
     * The Title.
     */
    @UIFormField( name = WIDGET_FIELDS.TITLE, title = "3000126x4", section = "options", orderNum = 10 )
    private String title;

    @UIFormField( name = WIDGET_FIELDS.UPDATE_WIDGET_INTERVAL, title = "3000393x4", section = "options", orderNum = 11, type = "integer" )
    private Integer updateWidgetInterval;

}
