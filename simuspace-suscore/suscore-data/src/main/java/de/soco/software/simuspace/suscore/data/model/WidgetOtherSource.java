package de.soco.software.simuspace.suscore.data.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The type Widget other source.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = true )
public class WidgetOtherSource extends WidgetSource {

    /**
     * The Schema.
     */
    @UIFormField( name = "name", title = "3000032x4", type = "select", required = true, orderNum = 2 )
    private String name;

}
