package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchProjectsDTO extends BmwCaeBenchDTO {

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000173x4", orderNum = 3 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000173x4", name = "type", orderNum = 3 )
    private String type;

    /**
     * The type.
     */
    @UIFormField( name = "shortName", title = "3000195x4", orderNum = 3 )
    @UIColumn( data = "shortName", filter = "text", renderer = "text", title = "3000195x4", name = "shortName", orderNum = 3 )
    private String shortName;

    public String getShortName() {
        return shortName;
    }

    public void setShortName( String shortName ) {
        this.shortName = shortName;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

}
