package de.soco.software.simuspace.susdash.pst.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PreReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1392875836717344168L;

    @JsonProperty( "M" )
    private String name;

    @JsonProperty( "ID" )
    private String rowId;

    public String getRowId() {
        return rowId;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setRowId( String rowId ) {
        this.rowId = rowId;
    }

}
