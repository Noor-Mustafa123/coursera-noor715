package de.soco.software.simuspace.suscore.common.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobSchemeSummary {

    List< Map< String, Object > > design;

    List< Map< String, Object > > objective;

    public JobSchemeSummary() {
        super();

    }

    public JobSchemeSummary( List< Map< String, Object > > design, List< Map< String, Object > > objective ) {
        super();
        this.design = design;
        this.objective = objective;
    }

    public List< Map< String, Object > > getDesign() {
        return design;
    }

    public void setDesign( List< Map< String, Object > > design ) {
        this.design = design;
    }

    public List< Map< String, Object > > getObjective() {
        return objective;
    }

    public void setObjective( List< Map< String, Object > > objective ) {
        this.objective = objective;
    }

}
