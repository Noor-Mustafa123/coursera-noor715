/*
 *
 */

package de.soco.software.simuspace.workflow.model.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsElementProps;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This Class contains the information of a Job to execute i.e. the workflow which has to execute.
 *
 * @author Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class JobParametersImpl implements JobParameters {

    /**
     * The id.
     */
    private String id;

    /**
     * The comments.
     */
    private String comments;

    /**
     * map consist of key like elements,connection and positions workflow model field to store in db .
     */
    private LatestWorkFlowDTO workflow;

    /**
     * The description.
     */
    private String description;

    /**
     * The env requirements.
     */
    private String envRequirements;

    /**
     * The Whl file.
     */
    private String whlFile;

    /**
     * The name.
     */
    private String name;

    /**
     * The request headers.
     */
    private RequestHeaders requestHeaders;

    /**
     * The run mode.
     */
    private String runMode;

    /**
     * The server.
     */
    private RestAPI server;

    /**
     * The workflow id.
     */
    private UUID workflowId;

    /**
     * The workflow version.
     */
    VersionDTO workflowVersion;

    /**
     * The working dir.
     */
    private EngineFile workingDir;

    /**
     * The reference job id for rerun a job.
     */
    private UUID refJobId;

    /**
     * The file run.
     */
    private boolean fileRun;

    /**
     * The create child.
     */
    private boolean createChild = false;

    /**
     * The global variables.
     */
    private Map< String, Object > globalVariables;

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
    private int jobType;

    /**
     * The design summary.
     */
    private Map< String, Object > designSummary;

    /**
     * The objective variables.
     */
    private List< ObjectiveVariableDTO > objectiveVariables;

    /**
     * The custom variables.
     */
    private List< CustomVariableDTO > customVariables;

    /**
     * The job run by user id.
     */
    private UUID jobRunByUserId;

    /**
     * The job run by user UID.
     */
    private String jobRunByUserUID;

    /**
     * The result scheme as json.
     */
    private JobScheme resultSchemeAsJson;

    /**
     * The job integer.
     */
    private Integer jobInteger;

    /**
     * The postprocess.
     */
    private PostProcess postprocess;

    /**
     * The postprocess parameters json.
     */
    private Object postprocessParametersJson;

    /**
     * The job max execution time.
     */
    private Date jobMaxExecutionTime;

    /**
     * Instantiates a new job parameters impl.
     */
    public JobParametersImpl() {
        super();
    }

    /**
     * Instantiates a new job parameters impl.
     *
     * @param comments
     *         the comments
     * @param description
     *         the description
     * @param name
     *         the name
     * @param workflowId
     *         the workflow id
     * @param workingDir
     *         the working dir
     * @param workflow
     *         the workflow
     * @param runMode
     *         the run mode
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param workflowVersion
     *         the workflow version
     * @param refJobId
     *         the ref job id
     * @param jobType
     *         the job type
     * @param designSummary
     *         the design summary
     * @param objectiveVariables
     *         the objective variables
     * @param jobRunByUserUID
     *         the job run by user UID
     * @param jobRunByUserId
     *         the job run by user id
     * @param jobInteger
     *         the job integer
     */
    public JobParametersImpl( String comments, String description, String name, UUID workflowId, EngineFile workingDir,
            LatestWorkFlowDTO workflow, String runMode, RestAPI server, RequestHeaders requestHeaders, VersionDTO workflowVersion,
            UUID refJobId, int jobType, Map< String, Object > designSummary, List< ObjectiveVariableDTO > objectiveVariables,
            String jobRunByUserUID, UUID jobRunByUserId, Integer jobInteger ) {
        super();
        this.comments = comments;
        this.description = description;
        this.name = name;
        this.workflowId = workflowId;
        this.workingDir = workingDir;
        this.workflow = workflow;
        this.runMode = runMode;
        this.server = server;
        this.requestHeaders = requestHeaders;
        this.workflowVersion = workflowVersion;
        this.refJobId = refJobId;
        this.jobType = jobType;
        this.designSummary = designSummary;
        this.objectiveVariables = objectiveVariables;
        this.jobRunByUserUID = jobRunByUserUID;
        this.jobRunByUserId = jobRunByUserId;
        this.jobInteger = jobInteger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComments() {
        return comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
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
    public String getWhlFile() {
        return whlFile;
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
    public UUID getRefJobId() {
        return refJobId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRunsOn() {

        return runMode;
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
    public LatestWorkFlowDTO getWorkflow() {
        return workflow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineFile getWorkingDir() {
        return workingDir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFileRun() {
        return getWorkflow().getId() == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComments( String comments ) {
        this.comments = comments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription( String description ) {
        this.description = description;
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
    public void setWhlFile( String whlFile ) {
        this.whlFile = whlFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFileRun( boolean fileRun ) {
        this.fileRun = fileRun;
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
    public void setRequestHeaders( RequestHeaders requestHeaders ) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRunsOn( String runMode ) {
        this.runMode = runMode;
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
    public void setWorkflow( LatestWorkFlowDTO workFlow ) {
        workflow = workFlow;
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
        return "JobParameters [comments=" + comments + ", description=" + description + ", definition=" + workflow + ", name=" + name
                + ", requestHeaders=" + requestHeaders + ", runMode=" + runMode + ", server=" + server + ", workflowId=" + workflowId
                + ", workingDir=" + workingDir + "]";
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
            final java.io.File ioFile = new java.io.File( getWorkingDir().getPath() );
            if ( !ioFile.exists() ) {
                notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, getWorkingDir().getPath() ) ) );
            }
        }
        if ( !getRunsOn().contentEquals( ConstantsString.SERVER ) && ( getWorkflow().getId() == null ) ) {
            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ID_CANT_BE_NULL ) ) );
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

        if ( StringUtils.isNotNullOrEmpty( comments ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( getComments(), "Comments",
                    ConstantsLength.STANDARD_COMMENT_LENGTH, false, false ) );
            if ( !notification.getErrors().isEmpty() ) {
                notification.addErrors( notification.getErrors() );
            } else {
                setComments( getComments() );
            }
        }
        if ( StringUtils.isNotNullOrEmpty( description ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( getComments(), "Description",
                    ConstantsLength.STANDARD_DESCRIPTION_LENGTH, false, false ) );
            if ( !notification.getErrors().isEmpty() ) {
                notification.addErrors( notification.getErrors() );
            } else {
                setDescription( getDescription() );
            }
        }

        notification.addNotification( getServer().validate() );

        return notification;
    }

    /**
     * Gets the global variables.
     *
     * @return the global variables
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.JobParameters#getGlobalVariables()
     */
    public Map< String, Object > getGlobalVariables() {
        return globalVariables;
    }

    /**
     * Sets the global variables.
     *
     * @param globalVariables
     *         the global variables
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.JobParameters#setGlobalVariables(java.util.Map)
     */
    public void setGlobalVariables( Map< String, Object > globalVariables ) {
        this.globalVariables = globalVariables;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.JobParameters#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.workflow.model.JobParameters#setId(java.lang.String)
     */
    public void setId( String id ) {
        this.id = id;
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
    public void setJobRelationType( int type ) {
        this.jobRelationType = type;
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
    public UUID getJobRunByUserId() {
        return jobRunByUserId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobRunByUserId( UUID jobRunByUserId ) {
        this.jobRunByUserId = jobRunByUserId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getJobRunByUserUID() {
        return jobRunByUserUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobRunByUserUID( String jobRunByUserUID ) {
        this.jobRunByUserUID = jobRunByUserUID;

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
    public void setJobSchemeCategory( int jobSchemeCategory ) {
        this.jobSchemeCategory = jobSchemeCategory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getJobInteger() {
        return jobInteger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setJobInteger( Integer jobInteger ) {
        this.jobInteger = jobInteger;
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
    public Object getPostprocessParametersJson() {
        return postprocessParametersJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPostprocessParametersJson( Object postprocessParametersJson ) {
        this.postprocessParametersJson = postprocessParametersJson;
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
    public Date getJobMaxExecutionTime() {
        return jobMaxExecutionTime;
    }

    /**
     * Gets the result scheme as json.
     *
     * @return the result scheme as json
     */
    public JobScheme getResultSchemeAsJson() {
        return resultSchemeAsJson;
    }

    /**
     * Sets the result scheme as json.
     *
     * @param resultSchemeAsJson
     *         the new result scheme as json
     */
    public void setResultSchemeAsJson( JobScheme resultSchemeAsJson ) {
        this.resultSchemeAsJson = resultSchemeAsJson;
    }

}
