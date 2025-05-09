package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class JobTokenCommandLine for job token and JObId for command line workflow .
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class JobTokenCommandLine {

    /**
     * The token.
     */
    private String jobToken;

    /**
     * The user id.
     */
    private String jobId;

    /**
     * Gets the job id.
     *
     * @return the job id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the job id.
     *
     * @param jobId
     *         the new job id
     */
    public void setJobId( String jobId ) {
        this.jobId = jobId;
    }

    /**
     * Gets the job token.
     *
     * @return the job token
     */
    public String getJobToken() {
        return jobToken;
    }

    /**
     * Sets the job token.
     *
     * @param jobToken
     *         the new job token
     */
    public void setJobToken( String jobToken ) {
        this.jobToken = jobToken;
    }

}
