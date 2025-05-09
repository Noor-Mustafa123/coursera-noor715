package de.soco.software.simuspace.workflow.model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This Interface is designed to contain all the properties that a job has.
 *
 * @author Aroosa.Bukhari
 */
@JsonDeserialize( as = JobParametersImpl.class )
@JsonIgnoreProperties( ignoreUnknown = true )
public interface JobParameters {

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( String id );

    /**
     * Gets the comments of a job.
     *
     * @return the comments
     */
    String getComments();

    /**
     * Gets the description of a job.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the env requirements.
     *
     * @return the env requirements
     */
    String getEnvRequirements();

    /**
     * Gets whl file.
     *
     * @return the whl file
     */
    String getWhlFile();

    /**
     * Gets the name of the job.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the reference job id.
     *
     * @return the reference job id
     */
    UUID getRefJobId();

    /**
     * Gets the request headers.
     *
     * @return the request headers
     */
    RequestHeaders getRequestHeaders();

    /**
     * Gets the run mode.
     *
     * @return the run mode
     */
    String getRunsOn();

    /**
     * Gets the server info. It contains server ip address and active port
     *
     * @return the server
     */
    RestAPI getServer();

    /**
     * Gets the input/output parameters of a job. It contains ElementKey.ElementName and a map of all Field names and values against it
     *
     * @return the definition
     */
    LatestWorkFlowDTO getWorkflow();

    /**
     * Gets the working dir where log file and other temporary files (if any) will be created.
     *
     * @return the working dir
     */
    EngineFile getWorkingDir();

    /**
     * Checks if is file run.
     *
     * @return true, if is file run
     */
    boolean isFileRun();

    /**
     * Sets the comments of a job.
     *
     * @param comments
     *         the new comments
     */
    void setComments( String comments );

    /**
     * Sets the description of a job.
     *
     * @param description
     *         the new description
     */
    void setDescription( String description );

    /**
     * Sets the env requirements.
     *
     * @param envRequirements
     *         the new env requirements
     */
    void setEnvRequirements( String envRequirements );

    /**
     * Sets whl file.
     *
     * @param whlFile
     *         the whl file
     */
    void setWhlFile( String whlFile );

    /**
     * Sets the file run.
     *
     * @param fileRun
     *         the new file run
     */
    void setFileRun( boolean fileRun );

    /**
     * Sets the name of the job.
     *
     * @param name
     *         the new name
     */
    void setName( String name );

    /**
     * Sets the request headers.
     *
     * @param requestHeaders
     *         the new request headers
     */
    void setRequestHeaders( RequestHeaders requestHeaders );

    /**
     * Sets the run mode.
     *
     * @param runMode
     *         the new run mode
     */
    void setRunsOn( String runMode );

    /**
     * Sets the server info. It contains server ip address and active port.
     *
     * @param server
     *         the new server
     */
    void setServer( RestAPI server );

    /**
     * Sets the input/output parameters of a job. It contains ElementKey.ElementName and a map of all Field names and values against it.
     *
     * @param definition
     *         the definition
     */
    void setWorkflow( LatestWorkFlowDTO definition );

    /**
     * Sets the working dir where job logs will be created and other temporary files(if any) will be created.
     *
     * @param workingDir
     *         the new working dir
     */
    void setWorkingDir( EngineFile workingDir );

    /**
     * Validates the jobImpl object provided for execution.
     *
     * @return Notification
     */
    Notification validateForExecution();

    /**
     * Sets the global variables.
     *
     * @param prepareGlobalVariables
     *         the prepare global variables
     */
    void setGlobalVariables( Map< String, Object > prepareGlobalVariables );

    /**
     * Gets the global variables.
     *
     * @return the global variables
     */
    Map< String, Object > getGlobalVariables();

    /**
     * Gets the relation type.
     *
     * @return the relation type
     */
    int getJobRelationType();

    /**
     * Sets the relation type.
     *
     * @param type
     *         the new job relation type
     */
    void setJobRelationType( int type );

    /**
     * Gets the job scheme category.
     *
     * @return the job scheme category
     */
    int getJobSchemeCategory();

    /**
     * Sets the job scheme category.
     *
     * @param jobSchemeCategory
     *         the new job scheme category
     */
    void setJobSchemeCategory( int jobSchemeCategory );

    /**
     * Gets the relation type.
     *
     * @return the relation type
     */
    int getJobType();

    /**
     * Sets the relation type.
     *
     * @param type
     *         the new job type
     */
    void setJobType( int type );

    /**
     * Gets the design summary.
     *
     * @return the design summary
     */
    Map< String, Object > getDesignSummary();

    /**
     * Sets the design summary.
     *
     * @param designSummary
     *         the design summary
     */
    void setDesignSummary( Map< String, Object > designSummary );

    /**
     * Gets the objective variables.
     *
     * @return the objective variables
     */
    List< ObjectiveVariableDTO > getObjectiveVariables();

    /**
     * Sets the objective variables.
     *
     * @param objectiveVariables
     *         the new objective variables
     */
    void setObjectiveVariables( List< ObjectiveVariableDTO > objectiveVariables );

    /**
     * Gets the custom variables.
     *
     * @return the custom variables
     */
    List< CustomVariableDTO > getCustomVariables();

    /**
     * Sets the custom variables.
     *
     * @param customVariables
     *         the new custom variables
     */
    void setCustomVariables( List< CustomVariableDTO > customVariables );

    /**
     * Checks if is creates the child.
     *
     * @return true, if is creates the child
     */
    boolean isCreateChild();

    /**
     * Sets the creates the child.
     *
     * @param createChild
     *         the new creates the child
     */
    void setCreateChild( boolean createChild );

    /**
     * Gets the job run by user id.
     *
     * @return the job run by user id
     */
    UUID getJobRunByUserId();

    /**
     * Sets the job run by user id.
     *
     * @param userId
     *         the new job run by user id
     */
    void setJobRunByUserId( UUID userId );

    /**
     * Gets the job run by user UID.
     *
     * @return the job run by user UID
     */
    String getJobRunByUserUID();

    /**
     * Sets the job run by user UID.
     *
     * @param userUId
     *         the new job run by user UID
     */
    void setJobRunByUserUID( String userUId );

    /**
     * Gets the job integer.
     *
     * @return the job integer
     */
    Integer getJobInteger();

    /**
     * Sets the job integer.
     *
     * @param jobInteger
     *         the new job integer
     */
    void setJobInteger( Integer jobInteger );

    /**
     * Gets the postprocess.
     *
     * @return the postprocess
     */
    PostProcess getPostprocess();

    /**
     * Sets the postprocess.
     *
     * @param postprocess
     *         the new postprocess
     */
    void setPostprocess( PostProcess postprocess );

    /**
     * Gets the postprocess parameters json.
     *
     * @return the postprocess parameters json
     */
    Object getPostprocessParametersJson();

    /**
     * Sets the postprocess parameters json.
     *
     * @param postprocessParametersJson
     *         the new postprocess parameters json
     */
    void setPostprocessParametersJson( Object postprocessParametersJson );

    /**
     * Gets the job max execution time.
     *
     * @return the job max execution time
     */
    Date getJobMaxExecutionTime();

    /**
     * Sets the job max execution time.
     *
     * @param jobMaxExecutionTime
     *         the new job max execution time
     */
    void setJobMaxExecutionTime( Date jobMaxExecutionTime );

    JobScheme getResultSchemeAsJson();

    void setResultSchemeAsJson( JobScheme resultSchemeAsJson );

}
