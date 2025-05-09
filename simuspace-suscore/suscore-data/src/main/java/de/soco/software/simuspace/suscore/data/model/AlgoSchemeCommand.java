package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AlgoSchemeCommand implements Serializable {

    private String execute;

    private String getInputs;

    private String getSummary;

    // Getter Methods

    public String getExecute() {
        return execute;
    }

    public String getGetInputs() {
        return getInputs;
    }

    public String getGetSummary() {
        return getSummary;
    }

    // Setter Methods

    public void setExecute( String execute ) {
        this.execute = execute;
    }

    public void setGetInputs( String getInputs ) {
        this.getInputs = getInputs;
    }

    public void setGetSummary( String getSummary ) {
        this.getSummary = getSummary;
    }

}
