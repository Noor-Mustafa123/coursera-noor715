package de.soco.software.simuspace.workflow.location;

import de.soco.software.simuspace.workflow.model.JobParameters;

public class JobParametersLocationModel {

    private JobParameters jobParameters;

    private String uid;

    private String password;

    private String loadcaseName;

    public JobParameters getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters( JobParameters jobParameters ) {
        this.jobParameters = jobParameters;
    }

    public String getUid() {
        return uid;
    }

    public void setUid( String uid ) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public String getLoadcaseName() {
        return loadcaseName;
    }

    public void setLoadcaseName( String loadcaseName ) {
        this.loadcaseName = loadcaseName;
    }

    public JobParametersLocationModel( JobParameters jobParameters, String uid, String password ) {
        super();
        this.jobParameters = jobParameters;
        this.uid = uid;
        this.password = password;
    }

    public JobParametersLocationModel( JobParameters jobParameters ) {
        super();
        this.jobParameters = jobParameters;
    }

    public JobParametersLocationModel() {
        super();
    }

}
