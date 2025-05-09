package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobSchemeExperiment implements Serializable {

    private Integer no;

    private JobSchemeDetail job;

    private JobSchemeSummary summary;

    public JobSchemeExperiment() {
    }

    public JobSchemeExperiment( Integer no, JobSchemeDetail job, JobSchemeSummary summary ) {
        super();
        this.no = no;
        this.job = job;
        this.summary = summary;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo( Integer no ) {
        this.no = no;
    }

    public JobSchemeDetail getJob() {
        return job;
    }

    public void setJob( JobSchemeDetail job ) {
        this.job = job;
    }

    public JobSchemeSummary getSummary() {
        return summary;
    }

    public void setSummary( JobSchemeSummary summary ) {
        this.summary = summary;
    }

}
