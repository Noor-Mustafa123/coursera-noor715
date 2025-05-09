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
public class WidgetPythonSource extends WidgetSource {

    /**
     * The Data source id.
     */
    @JsonProperty( "dataSource" )
    @UIFormField( name = "dataSource", title = "3000230x4", type = "select", required = true, orderNum = 1 )
    private String dataSource;

    /**
     * The Python path.
     */
    @JsonProperty( "pythonPath" )
    @UIFormField( name = "pythonPath", title = "3000249x4", type = "server-file-explorer", orderNum = 2 )
    private String pythonPath;

    /**
     * The Output type.
     */
    @JsonProperty( "scriptOption" )
    @UIFormField( name = "scriptOption", title = "3000263x4", type = "select", required = true, orderNum = 3 )
    private String scriptOption;

    /**
     * The Output type.
     */
    @JsonProperty( "outputType" )
    @UIFormField( name = "outputType", title = "3000250x4", type = "select", required = true, orderNum = 3 )
    private String outputType;

    /**
     * The Python script.
     */
    @JsonProperty( "pythonScript" )
    @UIFormField( name = "pythonScript", title = "3000252x4", type = "code", required = true, orderNum = 4 )
    private String pythonScript;

    /**
     * The system script.
     */
    @JsonProperty( "systemScript" )
    @UIFormField( name = "systemScript", title = "3000252x4", type = "select", required = true, orderNum = 4 )
    private String systemScript;

    /**
     * The select script.
     */
    @JsonProperty( "selectScript" )
    @UIFormField( name = "selectScript", title = "3000252x4", type = "server-file-explorer", required = true, orderNum = 4 )
    private String selectScript;

    /**
     * The Python status.
     */
    @JsonProperty( "pythonStatus" )
    @UIFormField( name = "pythonStatus", title = "3000257x4", type = "select", required = true, orderNum = 5 )
    private String pythonStatus;

}
