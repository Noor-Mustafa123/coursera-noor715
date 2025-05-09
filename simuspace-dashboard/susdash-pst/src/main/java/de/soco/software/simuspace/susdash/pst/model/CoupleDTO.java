package de.soco.software.simuspace.susdash.pst.model;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CoupleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3800081497255416385L;

    @JsonProperty( "ID1" )
    private String rowId1;

    @JsonProperty( "ID2" )
    private String rowId2;

    public String getRowId1() {
        return rowId1;
    }

    public void setRowId1( String rowId1 ) {
        this.rowId1 = rowId1;
    }

    public String getRowId2() {
        return rowId2;
    }

    public void setRowId2( String rowId2 ) {
        this.rowId2 = rowId2;
    }

}
