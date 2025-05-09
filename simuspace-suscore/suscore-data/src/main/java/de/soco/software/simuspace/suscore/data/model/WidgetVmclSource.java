package de.soco.software.simuspace.suscore.data.model;

import static de.soco.software.simuspace.suscore.common.constants.DataDashboardConstants.VMCL_FIELDS;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Widget material inspector source.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = true )
public class WidgetVmclSource extends WidgetOtherSource {

    /**
     * The Mat db source.
     */
    @UIFormField( name = VMCL_FIELDS.VMCL_DATA_SOURCE, title = "3000377x4", type = "select", required = true, orderNum = 11 )
    private String vmclDataSource;

}
