package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.PST_WIDGET_FIELDS;

import javax.validation.constraints.NotNull;

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
 * The type Pst widget dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class PstWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1711611471705686823L;

    /**
     * The X axis.
     */
    @UIFormField( name = PST_WIDGET_FIELDS.UPDATE_INTERVAL, title = "3000262x4", section = "options", orderNum = 11 )
    private String updateInterval;

    /**
     * The Prufstand.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.PRUFSTAND, title = "3000279x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > prufstand;

    /**
     * The Programm.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.PROGRAMM, title = "3000280x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > programm;

    /**
     * The Motortyp.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.MOTORTYP, title = "3000281x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > motortyp;

    /**
     * The Motor bg.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.MOTOR_BG, title = "3000282x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > motor_bg;

    /**
     * The Status.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.STATUS, title = "3000283x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > status;

    /**
     * The Vorbereitung.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = PST_WIDGET_FIELDS.VORBEREITUNG, title = "3000284x4", type = "input-table", section = "options", required = true, orderNum = 12 )
    private List< List< Object > > vorbereitung;

}
