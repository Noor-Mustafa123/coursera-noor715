package de.soco.software.simuspace.workflow.model;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This Interface is designed to contain all the properties that a job has.
 *
 * @author Aroosa.Bukhari
 */
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonDeserialize( as = JobImpl.class )
public interface Job {

    /**
     * Gets the completion time.
     *
     * @return the completion time
     */
    Date getCompletionTime();

    /**
     * Gets the user who executed the job.
     *
     * @return the created by
     */
    UserDTO getCreatedBy();

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
     * Gets the element output.
     *
     * @return the element output
     */
    File getElementOutput();

    /**
     * Gets the id of the job.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Gets the input/output parameters of a job. It contains ElementKey.ElementName and a map of all Field names and values against it
     *
     * @return the input parameters
     */
    Map< String, Map< String, Object > > getIOParameters();

    /**
     * Gets the result parameters of a job. It contains parameters used in workflows
     *
     * @return the result parameters
     */
    Map< String, Object > getResultParameters();

    /**
     * Gets the result summary.
     *
     * @return the result summary
     */
    String getResultSummary();

    /**
     * Sets the result summary.
     *
     * @param resultSummary
     *         the new result summary
     */
    void setResultSummary( String resultSummary );

    /**
     * Gets the job process id.
     *
     * @return the job process id
     */
    String getJobProcessId();

    /**
     * Gets the log of a Job.
     *
     * @return the log
     */
    List< LogRecord > getLog();

    /**
     * Gets the log general.
     *
     * @return the log general
     */
    DocumentDTO getLogGeneral();

    /**
     * Gets the log general file path.
     *
     * @return the log general file path
     */
    String getLogGeneralFilePath();

    /**
     * Gets the log path.
     *
     * @return the log path
     */
    String getLogPath();

    /**
     * Gets the log summary.
     *
     * @return the log summary
     */
    DocumentDTO getLogSummary();

    /**
     * Gets the log summary file path.
     *
     * @return the log summary file path
     */
    String getLogSummaryFilePath();

    /**
     * Gets the machine where job was executed.
     *
     * @return the machine
     */
    String getMachine();

    /**
     * Gets the name of the job.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the os.
     *
     * @return the os
     */
    String getOs();

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    ProgressBar getProgress();

    /**
     * Gets the request headers.
     *
     * @return the request headers
     */
    RequestHeaders getRequestHeaders();

    /**
     * Gets the rerun from j ob.
     *
     * @return the rerun from j ob
     */
    Job getRerunFromJob();

    /**
     * Gets the run mode. e.g. client, server etc
     *
     * @return the run mode
     */
    String getRunMode();

    /**
     * Gets the runs on.
     *
     * @return the runs on
     */
    LocationDTO getRunsOn();

    /**
     * Gets the server info. It contains server ip address and active port
     *
     * @return the server
     */
    RestAPI getServer();

    /**
     * Gets the status of a job. e.g. Running/Finished etc
     *
     * @return the status
     */
    Status getStatus();

    /**
     * Gets the Submission date of a job.
     *
     * @return the submit date
     */
    Date getSubmitTime();

    /**
     * Gets the version.
     *
     * @return the version
     */
    VersionDTO getVersion();

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    LatestWorkFlowDTO getWorkflow();

    /**
     * Gets the workflow id which is to be executed.
     *
     * @return the workflow id
     */
    UUID getWorkflowId();

    /**
     * Gets the workflow name.
     *
     * @return the workflow name
     */
    String getWorkflowName();

    /**
     * Gets the workflow version.
     *
     * @return the workflow version
     */
    VersionDTO getWorkflowVersion();

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
     * Sets the job submit date.
     *
     * @param completionTime
     *         the new completion date
     */
    void setCompletionTime( Date completionTime );

    /**
     * Sets the user who submits the job.
     *
     * @param createdBy
     *         the new created by
     */
    void setCreatedBy( UserDTO createdBy );

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
     * Sets the element output.
     *
     * @param elementOutput
     *         the new element output
     */
    void setElementOutput( File elementOutput );

    /**
     * Sets the id of a job.
     *
     * @param id
     *         the new id
     */
    void setId( UUID id );

    /**
     * Sets the input/output parameters of a job. It contains ElementKey.ElementName and a map of all Field names and values against it.
     *
     * @param inputParameters
     *         the input/output parameters
     */
    void setIOParameters( Map< String, Map< String, Object > > inputParameters );

    /**
     * Sets the result parameters of a job. It contains parameters used in workflow.
     *
     * @param inputParameters
     *         the input parameters
     */
    void setResultParameters( Map< String, Object > inputParameters );

    /**
     * Sets the checks if is file run.
     *
     * @param isFileRun
     *         the new checks if is file run
     */
    void setIsFileRun( boolean isFileRun );

    /**
     * Sets the job process id.
     *
     * @param jobProcessId
     *         the new job process id
     */
    void setJobProcessId( String jobProcessId );

    /**
     * Sets the log of a job.
     *
     * @param log
     *         the new log
     */
    void setLog( List< LogRecord > log );

    /**
     * Sets the log general.
     *
     * @param logGeneral
     *         the new log general
     */
    void setLogGeneral( DocumentDTO logGeneral );

    /**
     * Sets the log general file path.
     *
     * @param logGeneralFilePath
     *         the new log general file path
     */
    void setLogGeneralFilePath( String logGeneralFilePath );

    /**
     * Sets the log path.
     *
     * @param logPath
     *         the new log path
     */
    void setLogPath( String logPath );

    /**
     * Sets the log summary.
     *
     * @param logSummary
     *         the new log summary
     */
    void setLogSummary( DocumentDTO logSummary );

    /**
     * Sets the log summary file path.
     *
     * @param logSummaryFilePath
     *         the new log summary file path
     */
    void setLogSummaryFilePath( String logSummaryFilePath );

    /**
     * Sets the machine where job was executed.
     *
     * @param machine
     *         the new machine
     */
    void setMachine( String machine );

    /**
     * Sets the name of the job.
     *
     * @param name
     *         the new name
     */
    void setName( String name );

    /**
     * Sets the os.
     *
     * @param os
     *         the new os
     */
    void setOs( String os );

    /**
     * Sets the progress.
     *
     * @param progress
     *         the new progress
     */
    void setProgress( ProgressBar progress );

    /**
     * Sets the request headers.
     *
     * @param requestHeaders
     *         the new request headers
     */
    void setRequestHeaders( RequestHeaders requestHeaders );

    /**
     * Sets the rerun from j ob.
     *
     * @param rerunFromJOb
     *         the new rerun from j ob
     */
    void setRerunFromJob( Job rerunFromJOb );

    /**
     * Sets the run mode of job. e.g. client, server
     *
     * @param runMode
     *         the new run mode
     */
    void setRunMode( String runMode );

    /**
     * Sets the runs on.
     *
     * @param runsOn
     *         the new runs on
     */
    void setRunsOn( LocationDTO runsOn );

    /**
     * Sets the server info. It contains server ip address and active port.
     *
     * @param server
     *         the new server
     */
    void setServer( RestAPI server );

    /**
     * Sets the status of a job.
     *
     * @param status
     *         the new status
     */
    void setStatus( Status status );

    /**
     * Sets the submit time.
     *
     * @param submitTime
     *         the new submit date
     */
    void setSubmitTime( Date submitTime );

    /**
     * Sets the version.
     *
     * @param version
     *         the new version
     */
    void setVersion( VersionDTO version );

    /**
     * Sets the definition.
     *
     * @param workFlow
     *         the new workflow
     */
    void setWorkflow( LatestWorkFlowDTO workFlow );

    /**
     * Sets the workflow id which is to be executed.
     *
     * @param workflowId
     *         the new workflow id
     */
    void setWorkflowId( UUID workflowId );

    /**
     * Sets the workflow name.
     *
     * @param workflowName
     *         the new workflow name
     */
    void setWorkflowName( String workflowName );

    /**
     * Sets the workflow version.
     *
     * @param workflowVersion
     *         the new workflow version
     */
    void setWorkflowVersion( VersionDTO workflowVersion );

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
     * Gets the type.
     *
     * @return the type
     */
    int getJobType();

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    void setJobType( int type );

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
     * Gets the child jobs count.
     *
     * @return the child jobs count
     */
    Integer getChildJobsCount();

    /**
     * Sets the child jobs count.
     *
     * @param childJobsCount
     *         the new child jobs count
     */
    void setChildJobsCount( Integer childJobsCount );

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
    List< CustomVariableDTO > getCustomVariables();

    /**
     * Sets the objective variables.
     *
     * @param objectiveVariables
     *         the new objective variables
     */
    void setCustomVariables( List< CustomVariableDTO > customVariables );

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
     * Checks if is auto delete.
     *
     * @return true, if is auto delete
     */
    boolean isAutoDelete();

    /**
     * Sets the auto delete.
     *
     * @param isAutoDelete
     *         the new auto delete
     */
    void setAutoDelete( boolean isAutoDelete );

    /**
     * Gets the ask on run parameters.
     *
     * @return the ask on run parameters
     */
    LinkedHashMap< String, Object > getAskOnRunParameters();

    /**
     * Sets the ask on run parameters.
     *
     * @param askOnRunParameters
     *         the new ask on run parameters
     */
    void setAskOnRunParameters( LinkedHashMap< String, Object > askOnRunParameters );

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
    String getPostprocessParametersJson();

    /**
     * Sets the postprocess parameters json.
     *
     * @param postprocessParametersJson
     *         the new postprocess parameters json
     */
    void setPostprocessParametersJson( String postprocessParametersJson );

    /**
     * Gets the job max execution time.
     *
     * @return the job max execution time
     */
    Date getJobMaxExecutionTime();

    /**
     * Sets the job max execution time.
     *
     * @param date
     *         the new job max execution time
     */
    void setJobMaxExecutionTime( Date date );

    JobScheme getResultSchemeAsJson();

    void setResultSchemeAsJson( JobScheme resultSchemeAsJson );

}
