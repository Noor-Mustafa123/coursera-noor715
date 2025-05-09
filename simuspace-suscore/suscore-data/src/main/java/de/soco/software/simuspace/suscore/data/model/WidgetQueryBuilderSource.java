package de.soco.software.simuspace.suscore.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode( callSuper = true )
public class WidgetQueryBuilderSource extends WidgetSource {

    /**
     * The Data source id.
     */
    @JsonProperty( "dataSource" )
    @UIFormField( name = "dataSource", title = "3000230x4", type = "select", required = true, orderNum = 1 )
    private String dataSource;

    /**
     * The Schema.
     */
    @JsonProperty( "schema" )
    @UIFormField( name = "schema", title = "3000239x4", type = "select", required = true, orderNum = 2 )
    private String schema;

    /**
     * The Table.
     */
    @JsonProperty( "table" )
    @UIFormField( name = "table", title = "3000247x4", type = "select", required = true, orderNum = 3 )
    private String table;

}
