package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobScheme implements Serializable {

    private static final long serialVersionUID = 3057017704745274292L;

    private WorkflowScheme workflow;

    private List< JobSchemeExperiment > experiments;

    private JobSchemeFileSummary summary;

    private JobSchemeAlgo algo;

    public JobScheme() {
    }

    public JobScheme( WorkflowScheme workflow, List< JobSchemeExperiment > experiments, JobSchemeFileSummary summary, JobSchemeAlgo algo ) {
        super();
        this.algo = algo;
        this.workflow = workflow;
        this.experiments = experiments;
        this.summary = summary;
    }

    public WorkflowScheme getWorkflow() {
        return workflow;
    }

    public void setWorkflow( WorkflowScheme workflow ) {
        this.workflow = workflow;
    }

    public List< JobSchemeExperiment > getExperiments() {
        return experiments;
    }

    public void setExperiments( List< JobSchemeExperiment > experiments ) {
        this.experiments = experiments;
    }

    public JobSchemeFileSummary getSummary() {
        return summary;
    }

    public void setSummary( JobSchemeFileSummary summary ) {
        this.summary = summary;
    }

    public JobSchemeAlgo getAlgo() {
        return algo;
    }

    public void setAlgo( JobSchemeAlgo algo ) {
        this.algo = algo;
    }

    @Override
    public String toString() {
        return "JobScheme [workflow=" + workflow + ", experiments=" + experiments + ", summary=" + summary + "]";
    }

}