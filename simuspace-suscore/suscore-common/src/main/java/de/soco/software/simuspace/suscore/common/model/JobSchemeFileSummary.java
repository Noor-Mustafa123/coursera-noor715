package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobSchemeFileSummary implements Serializable {

    private String json_path;

    private String csv_path;

    public JobSchemeFileSummary() {
        super();
    }

    public JobSchemeFileSummary( String json_path, String csv_path ) {
        super();
        this.json_path = json_path;
        this.csv_path = csv_path;
    }

    public String getJson_path() {
        return json_path;
    }

    public void setJson_path( String json_path ) {
        this.json_path = json_path;
    }

    public String getCsv_path() {
        return csv_path;
    }

    public void setCsv_path( String csv_path ) {
        this.csv_path = csv_path;
    }

}
