package de.soco.software.simuspace.workflow.model.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.simflow.ConstantsJobProps;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsElementProps;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This Class contains the properties of a Job i.e. the work flow which has been executed
 *
 * @author Aroosa.Bukhari
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class JobImpl implements Job, Serializable {

    /**
     * The Constant NOT_REQUIRED_PROP.
     */
    public static final String[] NOT_REQUIRED_PROP = { "requestHeaders", "server" };

    /**
     * The job integer.
     */
    @UIColumn( data = "jobInteger", name = "jobInteger", filter = "", renderer = "text", title = "3000159x4", orderNum = 1, isSortable = false )
    private Integer jobInteger;

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 2, width = 0 )
    private String name;

    /**
     * The description of job.
     */
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 3 )
    private String description;

    /**
     * To show progress bar on front-End.
     */
    @UIColumn( data = "progress", name = "type", filter = "", renderer = "progress", title = "3000072x4", orderNum = 4, isSortable = false )
    private ProgressBar progress;

    /**
     * The status.
     */
    @UIColumn( data = "status.name", name = "status", filter = "uuid", renderer = "text", title = "3000049x4", orderNum = 5 )
    private Status status;

    /**
     * The work flow name.
     */
    @UIColumn( data = "workflowName", name = "workflow.name", filter = "text", url = "view/workflow/{workflowId}/version/{workflowVersion.id}", renderer = "link", title = "3000059x4", orderNum = 6 )
    private String workflowName;

    /**
     * The user who create a work flow.
     */
    @UIColumn( data = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy.userUid", tooltip = "{createdBy.userName}", orderNum = 7 )
    private UserDTO createdBy;

    /**
     * The submit time of job.
     */
    @UIColumn( data = "submitTime", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000081x4", orderNum = 8 )
    private Date submitTime;

    /**
     * The unique id of job.
     */
    @UIColumn( data = "id", name = "id", filter = "text", renderer = "text", title = "3000021x4", isShow = true, orderNum = 10 )
    private UUID id;

    /**
     * can be [client,server, user-selected, null] either a work flow is running on client side or server side.
     */
    @UIColumn( data = "runMode", name = "runMode", filter = "uuid", renderer = "text", title = "3000101x4", orderNum = 11 )
    private String runMode;

    /**
     * The run mode.
     */
    @UIColumn( data = "runsOn.name", name = "runsOn", filter = "", renderer = "text", title = "3000124x4", orderNum = 12 )
    private LocationDTO runsOn;

    /**
     * The machine in which job is running.
     */
    @UIColumn( data = "machine", name = "machine", filter = "text", renderer = "text", title = "3000080x4", orderNum = 13 )
    private String machine;

    /**
     * The completion time.
     */
    @UIColumn( data = "completionTime", name = "finishedOn", filter = "dateRange", renderer = "date", title = "3000107x4", orderNum = 14 )
    private Date completionTime;

    /**
     * The work flow version.
     */
    @UIColumn( data = "workflowVersion.id", name = "workflow.composedId.versionId", filter = "uuid", renderer = "text", title = "3000106x4", orderNum = 15 )
    private VersionDTO workflowVersion;

    /**
     * The working directory of a workflow.
     */
    @UIColumn( data = "workingDir.path", name = "jobDirectory", filter = "text", renderer = "text", title = "3000108x4", orderNum = 16 )
    private EngineFile workingDir;

    /**
     * The operating system.
     */
    @UIColumn( data = "os", name = "os", filter = "text", renderer = "text", title = "3000109x4", orderNum = 17 )
    private String os;

    /**
     * The env file path.
     */
    private String envRequirements;

    /**
     * The Whl file.
     */
    private String whlFile;

    /**
     * The create child.
     */
    private boolean createChild;

    /**
     * The version.
     */
    private VersionDTO version;

    /**
     * The input and output parameters of a job.
     */
    private Map< String, Map< String, Object > > ioParameters;

    /**
     * The result parameters of a job.
     */
    private Map< String, Object > resultParameters;

    /**
     * The result summary.
     */
    private String resultSummary;

    /**
     * The is file run.
     */
    private boolean isFileRun;

    /**
     * Process Id created By Java for each Job.
     */
    private String jobProcessId;

    /**
     * The log.
     */
    private List< LogRecord > log;

    /**
     * path of log file created in working directory.
     */
    private String logPath;

    /**
     * The request headers containing token, agent etc.
     */
    private RequestHeaders requestHeaders;

    /**
     * The server.
     */
    private RestAPI server;

    /**
     * The unique work flow id.
     */
    private UUID workflowId;

    /**
     * The log summary.
     */
    /*@UIColumn ( data = "logSummary.name", filter = "text", renderer = "text", title = "3000103x4", name = "logSummary", orderNum = 11 )*/
    private DocumentDTO logSummary;

    /**
     * The log general.
     */
    /*@UIColumn ( data = "logGeneral.name", filter = "text", renderer = "text", title = "3000104x4", name = "logGeneral", orderNum = 12 )*/
    private DocumentDTO logGeneral;

    /**
     * map consist of key like elements,connection and positions workflow model field to store in db .
     */
    private LatestWorkFlowDTO definition;

    /**
     * The reference job name.
     */
    private Job rerunFromJob;

    /**
     * The element output.
     */
    private File elementOutput;

    /**
     * The log summary file path.
     */
    private String logSummaryFilePath;

    /**
     * The log general file path.
     */
    private String logGeneralFilePath;

    /**
     * The job type.
     */
    private int jobType;

    /**
     * The job type.
     */
    private int jobRelationType;

    /**
     * The job scheme category.
     */
    private int jobSchemeCategory;

    /**
     * The job type.
     */
    private Integer childJobsCount;

    /**
     * The ask on run parameters.
     */
    private LinkedHashMap< String, Object > askOnRunParameters;

    /**
     * The global variables.
     */
    private Map< String, Object > globalVariables;

    /**
     * The design summary.
     */
    private Map< String, Object > designSummary;

    /**
     * The objective variables.
     */
    private List< ObjectiveVariableDTO > objectiveVariables;

    /**
     * The objective variables.
     */
    private List< CustomVariableDTO > customVariables;

    /**
     * The is auto delete.
     */
    private boolean isAutoDelete = false;

    /**
     * The postprocess.
     */
    private PostProcess postprocess;

    /**
     * The postprocess parameters json.
     */
    private String postprocessParametersJson;

    /**
     * The job max execution time.
     */
    private Date jobMaxExecutionTime;

    /**
     * The Result scheme as json.
     */
    private JobScheme resultSchemeAsJson;

    /**
     * Instantiates a new job impl.
     */
    public JobImpl() {
        super();
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final JobImpl other = ( JobImpl ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCompletionTime() {
        return completionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Gets the element output.
     *
     * @return the element output
     */
    @Override
    public File getElementOutput() {
        return elementOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, Map< String, Object > > getIOParameters() {
        return ioParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, Object > getResultParameters() {
        return resultParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJobProcessId() {
        return jobProcessId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LogRecord > getLog() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getLogGeneral() {
        return logGeneral;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogGeneralFilePath() {
        return logGeneralFilePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogPath() {
        return logPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getLogSummary() {
        return logSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLogSummaryFilePath() {
        return logSummaryFilePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMachine() {
        return machine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOs() {
        return os;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgressBar getProgress() {
        return progress;
    }

    /**
     * Gets the request headers.
     *
     * @return the request headers
     */
    @Override
    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * Gets the rerun from job.
     *
     * @return the rerun from job
     */
    @Override
    public Job getRerunFromJob() {
        return rerunFromJob;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunMode() {
        return runMode;
    }

    /**
     * Gets the runs on.
     *
     * @return the runs on
     */
    @Override
    public LocationDTO getRunsOn() {
        return runsOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RestAPI getServer() {
        return server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    @Override
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    @Override
    public LatestWorkFlowDTO getWorkflow() {
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getWorkflowId() {
        return workflowId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkflowName() {

        return workflowName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VersionDTO getWorkflowVersion() {
        return workflowVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineFile getWorkingDir() {
        return workingDir;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( id == null ) ? 0 : id.hashCode() );
        result = ( prime * result ) + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isFileRun() {
        return isFileRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompletionTime( Date completionTime ) {
        this.completionTime = completionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Sets the element output.
     *
     * @param elementOutput
     *         the new element output
     */
    @Override
    public void setElementOutput( File elementOutput ) {
        this.elementOutput = elementOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIOParameters( Map< String, Map< String, Object > > ioParameters ) {
        this.ioParameters = ioParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultParameters( Map< String, Object > resultParameters ) {
        this.resultParameters = resultParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIsFileRun( boolean isFileRun ) {
        this.isFileRun = isFileRun;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobProcessId( String jobProcessId ) {
        this.jobProcessId = jobProcessId;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLog( List< LogRecord > log ) {
        this.log = log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogGeneral( DocumentDTO logGeneral ) {
        this.logGeneral = logGeneral;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogGeneralFilePath( String logGeneralFilePath ) {
        this.logGeneralFilePath = logGeneralFilePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogPath( String logPath ) {
        this.logPath = logPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogSummary( DocumentDTO logSummary ) {
        this.logSummary = logSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLogSummaryFilePath( String logSummaryFilePath ) {
        this.logSummaryFilePath = logSummaryFilePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMachine( String machine ) {
        this.machine = machine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOs( String os ) {
        this.os = os;
    }

    /**
     * Gets the job integer.
     *
     * @return the job integer
     */
    @Override
    public Integer getJobInteger() {
        return jobInteger;
    }

    /**
     * Sets the job integer.
     *
     * @param jobInteger
     *         the new job integer
     */
    @Override
    public void setJobInteger( Integer jobInteger ) {
        this.jobInteger = jobInteger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProgress( ProgressBar progress ) {
        this.progress = progress;
    }

    /**
     * Sets the request headers.
     *
     * @param requestHeaders
     *         the new request headers
     */
    @Override
    public void setRequestHeaders( RequestHeaders requestHeaders ) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * Sets the rerun from job.
     *
     * @param rerunFrom
     *         the new rerun from job
     */
    @Override
    public void setRerunFromJob( Job rerunFrom ) {
        rerunFromJob = rerunFrom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRunMode( String runMode ) {
        this.runMode = runMode;
    }

    /**
     * Sets the runs on.
     *
     * @param runsOn
     *         the new runs on
     */
    @Override
    public void setRunsOn( LocationDTO runsOn ) {
        this.runsOn = runsOn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServer( RestAPI server ) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus( Status status ) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubmitTime( Date submitTime ) {
        this.submitTime = submitTime;
    }

    /**
     * Sets the version.
     *
     * @param version
     *         the version to set
     */
    @Override
    public void setVersion( VersionDTO version ) {
        this.version = version;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *         the definition
     */
    @Override
    public void setWorkflow( LatestWorkFlowDTO definition ) {
        this.definition = definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkflowId( UUID workflowId ) {
        this.workflowId = workflowId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkflowName( String workflowName ) {
        this.workflowName = workflowName;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkflowVersion( VersionDTO workflowVersion ) {
        this.workflowVersion = workflowVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWorkingDir( EngineFile workingDir ) {
        this.workingDir = workingDir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "JobImpl [completionTime='" + completionTime + "', createdBy='" + createdBy + "', description='" + description + "', id='"
                + id + "', ioParameters='" + ioParameters + "', isFileRun='" + isFileRun + "', jobProcessId='" + jobProcessId + "', log='"
                + log + "', logPath='" + logPath + "', machine='" + machine + "', name='" + name + "', progress='" + progress
                + "', requestHeaders='" + requestHeaders + "', runMode='" + runMode + "', server='" + server + "', status='" + status
                + "', submitTime='" + submitTime + "', workflowId='" + workflowId + "', workflowName='" + workflowName
                + "', workflowVersion='" + workflowVersion + "', workingDir='" + workingDir + "', definition='" + definition
                + "', rerunFromJob='" + rerunFromJob + "']";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification validateForExecution() {
        final Notification notification = new Notification();

        notification.addNotification( StringUtils.validateFieldAndLength( getName(), ConstantsElementProps.NAME,
                ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );

        if ( getWorkingDir() != null ) {
            final java.io.File file = new java.io.File( getWorkingDir().getPath() );
            if ( !file.exists() ) {
                notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, getWorkingDir().getPath() ) ) );
            }
        }
        if ( getWorkflowId() == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ID_CANT_BE_NULL ) ) );
        }
        if ( getWorkflowVersion().getId() < 1 ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_WORKFLOW_VERSION ) ) );
        }

        if ( StringUtils.isNullOrEmpty( getRequestHeaders().getToken() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.TOKEN_CANT_BE_EMPTY ) ) );
        }

        if ( StringUtils.isNullOrEmpty( getRequestHeaders().getUserAgent() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.USER_AGENT_CANT_BE_EMPTY ) ) );
        }

        if ( getServer() == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.SERVER_NOT_PROVIDED ) ) );
        }

        if ( StringUtils.isNotNullOrEmpty( description ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( getDescription(), ConstantsJobProps.DESCRIPTION,
                    ConstantsLength.STANDARD_DESCRIPTION_LENGTH, true, false ) );
        }

        notification.addNotification( getServer().validate() );

        return notification;
    }

    /**
     * Validate save job.
     *
     * @return true, if successful
     */
    public boolean validateSaveJob() {
        final Notification notification = new Notification();
        if ( StringUtils.isNullOrEmpty( getName() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.JOB_NAME_IS_NULL ) ) );
        } else if ( getName().length() > ConstantsLength.STANDARD_NAME_LENGTH ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.UTILS_VALUE_TOO_LARGE, ConstantsJobProps.NAME,
                    ConstantsLength.STANDARD_NAME_LENGTH ) ) );
        }
        if ( getCreatedBy() == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.JOB_USER_IS_NULL ) ) );
        } else if ( getWorkflowId() == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.JOB_WORKFLOW_ID_IS_NULL ) ) );
        }
        if ( !notification.getErrors().isEmpty() ) {
            throw new SusException( new Exception( notification.getErrors().toString() ), getClass() );
        }
        return true;
    }

    /**
     * Validate update job.
     *
     * @return true, if successful
     */
    public boolean validateUpdateJob() {
        final Notification notification = new Notification();
        if ( StringUtils.isNullOrEmpty( getName() ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.JOB_NAME_IS_NULL ) ) );
        } else if ( getName().length() > ConstantsLength.STANDARD_NAME_LENGTH ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.UTILS_VALUE_TOO_LARGE, ConstantsJobProps.NAME,
                    ConstantsLength.STANDARD_NAME_LENGTH ) ) );
        }
        if ( getId() == null ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.JOB_ID_IS_NULL ) ) );
        }
        if ( !notification.getErrors().isEmpty() ) {
            throw new SusException( new Exception( notification.getErrors().toString() ), getClass() );
        }
        return true;
    }

    /**
     * Gets the global variables.
     *
     * @return the global variables
     */
    @Override
    public Map< String, Object > getGlobalVariables() {
        return globalVariables;
    }

    /**
     * Sets the global variables.
     *
     * @param globalVariables
     *         the global variables
     */
    public void setGlobalVariables( Map< String, Object > globalVariables ) {
        this.globalVariables = globalVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResultSummary() {
        return resultSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultSummary( String resultSummary ) {
        this.resultSummary = resultSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJobType() {
        return jobType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobType( int type ) {
        this.jobType = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJobRelationType() {
        return jobRelationType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobSchemeCategory( int jobSchemeCategory ) {
        this.jobSchemeCategory = jobSchemeCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getJobSchemeCategory() {
        return jobSchemeCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobRelationType( int type ) {
        this.jobRelationType = type;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getChildJobsCount() {
        return childJobsCount;
    }

    /**
     * {@inheritDoc}
     */
    public void setChildJobsCount( Integer childJobsCount ) {
        this.childJobsCount = childJobsCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map< String, Object > getDesignSummary() {
        return designSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDesignSummary( Map< String, Object > designSummary ) {
        this.designSummary = designSummary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectiveVariableDTO > getObjectiveVariables() {
        return objectiveVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectiveVariables( List< ObjectiveVariableDTO > objectiveVariables ) {
        this.objectiveVariables = objectiveVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< CustomVariableDTO > getCustomVariables() {
        return customVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomVariables( List< CustomVariableDTO > customVariables ) {
        this.customVariables = customVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreateChild() {
        return createChild;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCreateChild( boolean createChild ) {
        this.createChild = createChild;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoDelete() {
        return isAutoDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoDelete( boolean isAutoDelete ) {
        this.isAutoDelete = isAutoDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashMap< String, Object > getAskOnRunParameters() {
        return askOnRunParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAskOnRunParameters( LinkedHashMap< String, Object > askOnRunParameters ) {
        this.askOnRunParameters = askOnRunParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostProcess getPostprocess() {
        return postprocess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPostprocess( PostProcess postprocess ) {
        this.postprocess = postprocess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPostprocessParametersJson() {
        return postprocessParametersJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPostprocessParametersJson( String postprocessParametersJson ) {
        this.postprocessParametersJson = postprocessParametersJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getJobMaxExecutionTime() {
        return this.jobMaxExecutionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobMaxExecutionTime( Date jobMaxExecutionTime ) {
        this.jobMaxExecutionTime = jobMaxExecutionTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobScheme getResultSchemeAsJson() {
        return resultSchemeAsJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResultSchemeAsJson( JobScheme resultSchemeAsJson ) {
        this.resultSchemeAsJson = resultSchemeAsJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnvRequirements() {
        return envRequirements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvRequirements( String envRequirements ) {
        this.envRequirements = envRequirements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWhlFile() {
        return whlFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWhlFile( String whlFile ) {
        this.whlFile = whlFile;
    }

}
