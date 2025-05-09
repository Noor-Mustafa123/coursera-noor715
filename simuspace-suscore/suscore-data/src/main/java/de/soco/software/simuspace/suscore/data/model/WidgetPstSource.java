package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.PST_FIELDS;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Widget pst source.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = true )
public class WidgetPstSource extends WidgetOtherSource {

    /**
     * The Ace lounge csv.
     */
    @UIFormField( name = PST_FIELDS.ACE_LOUNGE_CSV, title = "3000273x4", type = "select", section = "Input Files", orderNum = 11 )
    private String aceLoungeCsv;

    /**
     * The Ks updates.
     */
    @UIFormField( name = PST_FIELDS.KS_UPDATES, title = "3000274x4", type = "select", section = "Input Files", orderNum = 12 )
    private String ksUpdates;

    /**
     * The Bmw updates.
     */
    @UIFormField( name = PST_FIELDS.BMW_UPDATES, title = "3000275x4", type = "select", section = "Input Files", orderNum = 13 )
    private String bmwUpdates;

    /**
     * The Apl updates.
     */
    @UIFormField( name = PST_FIELDS.APL_UPDATES, title = "3000276x4", type = "select", section = "Input Files", orderNum = 14 )
    private String aplUpdates;

}
