package de.soco.software.simuspace.suscore.data.model;

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
 * The type Treemap widget options.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( callSuper = true )
@JsonIgnoreProperties( ignoreUnknown = true )
public class TreemapWidgetOptions extends WidgetOptions implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -5621501087329657318L;

    /**
     * The Depth.
     */
    @UIFormField( name = "leafDepth", title = "3000378x4", type = "integer", section = "options", orderNum = 12, required = true )
    private Integer leafDepth;

    /**
     * The Color column.
     */
    @UIFormField( name = "colorColumn", title = "3000278x4", type = "select", section = "options", orderNum = 13 )
    private Integer colorColumn;

    /**
     * The Color theme.
     */
    @UIFormField( name = "colorTheme", title = "3000244x4", type = "select", section = "options", orderNum = 14 )
    private String colorTheme;

}