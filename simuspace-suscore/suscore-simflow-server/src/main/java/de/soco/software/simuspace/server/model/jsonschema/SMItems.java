package de.soco.software.simuspace.server.model.jsonschema;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class SMItems {

    List< Map< String, String > > anyOf;

    public List< Map< String, String > > getAnyOf() {
        return anyOf;
    }

    public void setAnyOf( List< Map< String, String > > anyOf ) {
        this.anyOf = anyOf;
    }

}