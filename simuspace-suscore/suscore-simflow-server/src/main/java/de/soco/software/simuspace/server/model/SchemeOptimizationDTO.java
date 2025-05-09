package de.soco.software.simuspace.server.model;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;

/**
 * The Class SchemeOptimizationDTO.
 *
 * @author noman arshad
 */
public class SchemeOptimizationDTO {

    /**
     * The user id.
     */
    String userId;

    /**
     * The token.
     */
    String token;

    /**
     * The job parameters location model.
     */
    JobParametersLocationModel jobParametersLocationModel;

    /**
     * The design variable list.
     */
    List< ScanFileDTO > designVariableList;

    /**
     * The objective variable list.
     */
    List< ScanFileDTO > objectiveVariableList;

    /**
     * Instantiates a new scheme optimization DTO.
     */
    public SchemeOptimizationDTO() {
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the new user id
     */
    public void setUserId( String userId ) {
        this.userId = userId;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Gets the job parameters location model.
     *
     * @return the job parameters location model
     */
    public JobParametersLocationModel getJobParametersLocationModel() {
        return jobParametersLocationModel;
    }

    /**
     * Sets the job parameters location model.
     *
     * @param jobParametersLocationModel
     *         the new job parameters location model
     */
    public void setJobParametersLocationModel( JobParametersLocationModel jobParametersLocationModel ) {
        this.jobParametersLocationModel = jobParametersLocationModel;
    }

    /**
     * Gets the design variable list.
     *
     * @return the design variable list
     */
    public List< ScanFileDTO > getDesignVariableList() {
        return designVariableList;
    }

    /**
     * Sets the design variable list.
     *
     * @param designVariableList
     *         the new design variable list
     */
    public void setDesignVariableList( List< ScanFileDTO > designVariableList ) {
        this.designVariableList = designVariableList;
    }

    /**
     * Gets the objective variable list.
     *
     * @return the objective variable list
     */
    public List< ScanFileDTO > getObjectiveVariableList() {
        return objectiveVariableList;
    }

    /**
     * Sets the objective variable list.
     *
     * @param objectiveVariableList
     *         the new objective variable list
     */
    public void setObjectiveVariableList( List< ScanFileDTO > objectiveVariableList ) {
        this.objectiveVariableList = objectiveVariableList;
    }

}
