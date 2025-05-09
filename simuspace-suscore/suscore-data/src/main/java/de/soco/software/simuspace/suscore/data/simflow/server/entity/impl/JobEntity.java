package de.soco.software.simuspace.suscore.data.simflow.server.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * This Class represent the Job related parameters to store in databank and is mapped to database as Entity.
 *
 * @author Nosheen Sharif
 */
@Entity
@Table( name = "job" )
public class JobEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1347911829688322157L;

    /**
     * id of the object. versioning composite primary key reference
     */
    @Id
    @Column( name = "id" )
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The name.
     */
    @Column( name = "name" )
    private String name;

    /**
     * The description.
     */
    @Column( name = "description", length = 1024 )
    private String description;

    /**
     * owner mapping.
     */
    @ManyToOne
    private UserEntity owner;

    /**
     * Creation date of object.
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * who created the workflow.
     */
    // make it eager as we need user information with workflow
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "created_by", referencedColumnName = "id" )
    private UserEntity createdBy;

    /**
     * modification date of object.
     */
    @Column( name = "updated_on" )
    private Date modifiedOn;

    /**
     * who created the workflow.
     */
    // make it eager as we need user information with workflow
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "updated_by", referencedColumnName = "id" )
    private UserEntity modifiedBy;

    /**
     * The meta data document.
     */
    @OneToOne( fetch = FetchType.LAZY )
    private DocumentEntity metaDataDocument;

    /**
     * The meta data document.
     */
    @Column( name = "job_workflow_id", nullable = true )
    @Type( type = "uuid-char" )
    private UUID workflowId;

    /**
     * isDeleted.
     */
    @Column( name = "is_auto_delete" )
    private Boolean isAutoDelete = false;

    /**
     * status id of an object.
     */
    @Column( name = "lifecycle_status" )
    private String lifeCycleStatus;

    /**
     * The user selection id.
     */
    @Column( name = "perm_user_selection_id" )
    private String userSelectionId;

    /**
     * The group selection id.
     */
    @Column( name = "perm_group_selection_id" )
    private String groupSelectionId;

    /**
     * Date of completion of job.
     */
    @Column( name = "finished_on" )
    private Date finishedOn;

    /**
     * The io parameters required for job execution.
     */
    @Column( name = "io_parameters" )
    @Lob
    private byte[] ioParameters;

    /**
     * The result summary.
     */
    @Column( name = "resultSummary" )
    @Lob
    private byte[] resultSummary;

    /**
     * The log file of job execution.
     */
    @Column( name = "log" )
    @Lob
    private byte[] log;

    /**
     * The machine name (linux/window etc).
     */
    @Column( name = "machine" )
    private String machine;

    /**
     * The run mode of job(client/server etc).
     */
    @Column( name = "run_mode" )
    private int runMode;

    /**
     * The runs on.
     */
    @ManyToOne
    private LocationEntity runsOn;

    /**
     * The started on Date of job.
     */
    @Column( name = "started_on" )
    private Date startedOn;

    /**
     * The started on Date of job.
     */
    @Column( name = "job_dir", length = 2048 )
    private String jobDirectory;

    /**
     * The machine ip address on which the job is executing.
     */
    @Column( name = "machine_ip" )
    private String machineIP;

    /**
     * The process id of job in java.
     */
    @Column( name = "process_id" )
    private String pid;

    /**
     * The job log path in working dir.
     */
    @Column( name = "job_log_path", length = 2048 )
    private String jobLogPath;

    /**
     * The total elements of job to be exceuted .
     */
    @Column( name = "total_elements" )
    private Long totalElements;

    /**
     * No. of elements exceuted
     */
    @Column( name = "completed_elements" )
    private Long completedElements;

    /**
     * The status of object . Id will be from status enum class
     */
    @Column( name = "status" )
    private int status;

    /**
     * The os.
     */
    @Column( name = "operation_system" )
    private String os;

    /**
     * save as blob consist of key like elements,connection and positions workflow model field to store in db .
     */
    @Column( name = "definition" )
    @Lob
    private byte[] definition;

    /**
     * The token.
     */
    @Column( name = "token" )
    private String token;

    /**
     * reference job id of the job.
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumns( @JoinColumn( name = "ref_job_id", referencedColumnName = "id" ) )
    private JobEntity refJob;

    /**
     * The workflow against which the job is excuting.
     */
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumns( value = { @JoinColumn( name = "workflow_id", referencedColumnName = "id" ),
            @JoinColumn( name = "workflow_version_id", referencedColumnName = "version_id" ) } )
    private WorkflowEntity workflow;

    /**
     * The log summary.
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumns( @JoinColumn( name = "log_summary_document_id", referencedColumnName = "id" ) )
    private DocumentEntity logSummary;

    /**
     * The log general.
     */
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumns( @JoinColumn( name = "log_general_document_id", referencedColumnName = "id" ) )
    private DocumentEntity logGeneral;

    /**
     * The create child.
     */
    @Column( name = "create_child" )
    private Boolean createChild = false;

    /**
     * The job type.
     */
    private Integer jobType;

    /**
     * The job type.
     */
    private Integer jobRelationType;

    /**
     * The job type.
     */
    private Integer jobSchemeCategory;

    /**
     * The child Jobs Count.
     */
    private Integer childJobsCount;

    /**
     * The ask on run parameters.
     */
    @Column( name = "askOnRunParameters" )
    @Lob
    private byte[] askOnRunParameters;

    /**
     * The post process.
     */
    @Column( name = "postProcessJson" )
    @Lob
    private byte[] postProcess;

    /**
     * The rest API.
     */
    @Column( name = "restAPI" )
    @Lob
    private byte[] restAPI;

    /**
     * The postprocess parameters json.
     */
    @Column( name = "postprocessParametersJson" )
    @Lob
    private byte[] postprocessParametersJson;

    /**
     * The job max execution time.
     */
    @Column( name = "job_max_exec_time" )
    private Date jobMaxExecutionTime;

    /**
     * Instantiates a new job entity.
     */
    public JobEntity() {
        super();
    }

    /**
     * Instantiates a new job entity.
     *
     * @param composedId
     *         the composed id
     * @param name
     *         the name
     * @param description
     *         the description
     * @param pid
     *         the pid
     * @param createdOn
     *         the created on
     * @param finishedOn
     *         the finished on
     * @param os
     *         the os
     * @param machine
     *         the machine
     * @param status
     *         the status
     * @param runMode
     *         the run mode
     * @param runsOn
     *         the runs on
     * @param createdBy
     *         the created by
     * @param jobDirectory
     *         the job directory
     * @param totalElements
     *         the total elements
     * @param completedElements
     *         the completed elements
     * @param workflowId
     *         the workflow id
     * @param wfComposedId
     *         the wf composed id
     * @param wfName
     *         the wf name
     */
    public JobEntity( UUID id, String name, String description, String pid, Date createdOn, Date finishedOn, String os, String machine,
            int status, int runMode, LocationEntity runsOn, UserEntity createdBy, String jobDirectory, Long totalElements,
            Long completedElements, UUID workflowId, VersionPrimaryKey wfComposedId, String wfName ) {
        setId( id );
        setName( name );
        setDescription( description );
        setPid( pid );
        setCreatedOn( createdOn );
        setFinishedOn( finishedOn );
        setOs( os );
        setMachine( machine );
        setStatus( status );
        setRunMode( runMode );
        setRunsOn( runsOn );
        setCreatedBy( createdBy );
        setJobDirectory( jobDirectory );
        setTotalElements( totalElements );
        setCompletedElements( completedElements );
        setWorkflowId( workflowId );
        setWorkflow( new WorkflowEntity() );
        getWorkflow().setComposedId( wfComposedId );
        getWorkflow().setName( wfName );
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Instantiates a new Job entity.
     *
     * @param composedId
     *         the composed id
     * @param name
     *         the name
     * @param status
     *         the status
     * @param totalElements
     *         the total elements
     * @param completedElements
     *         the completed elements
     */
    public JobEntity( UUID id, String name, int status, Long totalElements, Long completedElements ) {
        setId( id );
        setName( name );
        setStatus( status );
        setTotalElements( totalElements );
        setCompletedElements( completedElements );
    }

    /**
     * Gets the finished on.
     *
     * @return the finished on
     */
    public Date getFinishedOn() {
        return finishedOn;
    }

    public byte[] getRestAPI() {
        return restAPI;
    }

    public void setRestAPI( byte[] restAPI ) {
        this.restAPI = restAPI;
    }

    /**
     * Gets the input output parameters.
     *
     * @return the input parameters
     */
    public byte[] getIOParameters() {
        return ioParameters;
    }

    /**
     * Gets the log.
     *
     * @return the log
     */
    public byte[] getLog() {
        return log;
    }

    /**
     * Gets the started on.
     *
     * @return the started on
     */
    public Date getStartedOn() {
        return startedOn;
    }

    /**
     * Gets the workflow.
     *
     * @return the workflow
     */
    public WorkflowEntity getWorkflow() {
        return workflow;
    }

    /**
     * Sets the finished on.
     *
     * @param finishedOn
     *         the new finished on
     */
    public void setFinishedOn( Date finishedOn ) {
        this.finishedOn = finishedOn;
    }

    /**
     * Sets the input output parameters.
     *
     * @param inputParameters
     *         the new input parameters
     */
    public void setIOParameters( byte[] inputParameters ) {
        ioParameters = inputParameters;
    }

    /**
     * Sets the log.
     *
     * @param log
     *         the new log
     */
    public void setLog( byte[] log ) {
        this.log = log;
    }

    /**
     * Sets the started on.
     *
     * @param startedOn
     *         the new started on
     */
    public void setStartedOn( Date startedOn ) {
        this.startedOn = startedOn;
    }

    /**
     * Sets the workflow.
     *
     * @param workflow
     *         the new workflow
     */
    public void setWorkflow( WorkflowEntity workflow ) {
        this.workflow = workflow;
    }

    /**
     * Gets the job directory.
     *
     * @return the job directory
     */
    public String getJobDirectory() {
        return jobDirectory;
    }

    /**
     * Sets the job directory.
     *
     * @param jobDirectory
     *         the new job directory
     */
    public void setJobDirectory( String jobDirectory ) {
        this.jobDirectory = jobDirectory;
    }

    /**
     * Gets the post process.
     *
     * @return the post process
     */
    public byte[] getPostProcess() {
        return postProcess;
    }

    /**
     * Sets the post process.
     *
     * @param postProcess
     *         the new post process
     */
    public void setPostProcess( byte[] postProcess ) {
        this.postProcess = postProcess;
    }

    /**
     * Gets the machine IP.
     *
     * @return the machine IP
     */
    public String getMachineIP() {
        return machineIP;
    }

    /**
     * Sets the machine IP.
     *
     * @param machineIP
     *         the new machine IP
     */
    public void setMachineIP( String machineIP ) {
        this.machineIP = machineIP;
    }

    /**
     * Gets the pid.
     *
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the pid.
     *
     * @param pid
     *         the new pid
     */
    public void setPid( String pid ) {
        this.pid = pid;
    }

    /**
     * Gets the job log path.
     *
     * @return the job log path
     */
    public String getJobLogPath() {
        return jobLogPath;
    }

    /**
     * Sets the job log path.
     *
     * @param jobLogPath
     *         the new job log path
     */
    public void setJobLogPath( String jobLogPath ) {
        this.jobLogPath = jobLogPath;
    }

    /**
     * Gets the machine.
     *
     * @return the machine
     */
    public String getMachine() {
        return machine;
    }

    /**
     * Sets the machine.
     *
     * @param machine
     *         the new machine
     */
    public void setMachine( String machine ) {
        this.machine = machine;
    }

    /**
     * Gets the total elements.
     *
     * @return the total elements
     */
    public Long getTotalElements() {
        return totalElements;
    }

    /**
     * Sets the total elements.
     *
     * @param totalElements
     *         the new total elements
     */
    public void setTotalElements( Long totalElements ) {
        this.totalElements = totalElements;
    }

    /**
     * Gets the completed elements.
     *
     * @return the completed elements
     */
    public Long getCompletedElements() {
        return completedElements;
    }

    /**
     * Sets the completed elements.
     *
     * @param completedElements
     *         the new completed elements
     */
    public void setCompletedElements( Long completedElements ) {
        this.completedElements = completedElements;
    }

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    public byte[] getDefinition() {
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *         the new definition
     */
    public void setDefinition( byte[] definition ) {
        this.definition = definition;
    }

    /**
     * Gets the reference job.
     *
     * @return the reference job
     */
    public JobEntity getRefJob() {
        return refJob;
    }

    /**
     * Sets the reference job.
     *
     * @param refJob
     *         the new reference job
     */
    public void setRefJob( JobEntity refJob ) {
        this.refJob = refJob;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the status to set
     */
    public void setStatus( int status ) {
        this.status = status;
    }

    /**
     * Gets the log summary.
     *
     * @return the log summary
     */
    public DocumentEntity getLogSummary() {
        return logSummary;
    }

    /**
     * Sets the log summary.
     *
     * @param logSummary
     *         the new log summary
     */
    public void setLogSummary( DocumentEntity logSummary ) {
        this.logSummary = logSummary;
    }

    /**
     * Gets the log general.
     *
     * @return the log general
     */
    public DocumentEntity getLogGeneral() {
        return logGeneral;
    }

    /**
     * Sets the log general.
     *
     * @param logGeneral
     *         the new log general
     */
    public void setLogGeneral( DocumentEntity logGeneral ) {
        this.logGeneral = logGeneral;
    }

    /**
     * Gets the os.
     *
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * Sets the os.
     *
     * @param os
     *         the os to set
     */
    public void setOs( String os ) {
        this.os = os;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( completedElements == null ) ? 0 : completedElements.hashCode() );
        result = prime * result + Arrays.hashCode( definition );
        result = prime * result + ( ( finishedOn == null ) ? 0 : finishedOn.hashCode() );
        result = prime * result + Arrays.hashCode( ioParameters );
        result = prime * result + ( ( startedOn == null ) ? 0 : startedOn.hashCode() );
        result = prime * result + status;
        result = prime * result + ( ( totalElements == null ) ? 0 : totalElements.hashCode() );
        result = prime * result + ( ( workflow == null ) ? 0 : workflow.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        return getClass() != obj.getClass();

    }

    /**
     * Gets the run mode.
     *
     * @return the run mode
     */
    public int getRunMode() {
        return runMode;
    }

    /**
     * Sets the run mode.
     *
     * @param runMode
     *         the new run mode
     */
    public void setRunMode( int runMode ) {
        this.runMode = runMode;
    }

    /**
     * Gets the runs on.
     *
     * @return the runs on
     */
    public LocationEntity getRunsOn() {
        return runsOn;
    }

    /**
     * Sets the runs on.
     *
     * @param runsOn
     *         the new runs on
     */
    public void setRunsOn( LocationEntity runsOn ) {
        this.runsOn = runsOn;
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

    public byte[] getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary( byte[] resultSummary ) {
        this.resultSummary = resultSummary;
    }

    /**
     * Gets the job type.
     *
     * @return the job type
     */
    public Integer getJobType() {
        return jobType;
    }

    /**
     * Sets the job type.
     *
     * @param jobType
     *         the new job type
     */
    public void setJobType( Integer jobType ) {
        this.jobType = jobType;
    }

    /**
     * Gets the job relation type.
     *
     * @return the job relation type
     */
    public Integer getJobRelationType() {
        return jobRelationType;
    }

    /**
     * Sets the job relation type.
     *
     * @param jobType
     *         the new job relation type
     */
    public void setJobRelationType( Integer jobType ) {
        this.jobRelationType = jobType;
    }

    /**
     * Gets the job scheme category.
     *
     * @return the job scheme category
     */
    public Integer getJobSchemeCategory() {
        return jobSchemeCategory;
    }

    /**
     * Sets the job scheme category.
     *
     * @param jobSchemeCategory
     *         the new job scheme category
     */
    public void setJobSchemeCategory( Integer jobSchemeCategory ) {
        this.jobSchemeCategory = jobSchemeCategory;
    }

    /**
     * Gets the child jobs count.
     *
     * @return the child jobs count
     */
    public Integer getChildJobsCount() {
        return childJobsCount;
    }

    /**
     * Sets the child jobs count.
     *
     * @param childJobsCount
     *         the new child jobs count
     */
    public void setChildJobsCount( Integer childJobsCount ) {
        this.childJobsCount = childJobsCount;
    }

    /**
     * Checks if is creates the child.
     *
     * @return true, if is creates the child
     */
    public Boolean isCreateChild() {
        return createChild;
    }

    /**
     * Sets the creates the child.
     *
     * @param createChild
     *         the new creates the child
     */
    public void setCreateChild( Boolean createChild ) {
        this.createChild = createChild;
    }

    public byte[] getAskOnRunParameters() {
        return askOnRunParameters;
    }

    public void setAskOnRunParameters( byte[] askOnRunParameters ) {
        this.askOnRunParameters = askOnRunParameters;
    }

    public byte[] getPostprocessParametersJson() {
        return postprocessParametersJson;
    }

    public void setPostprocessParametersJson( byte[] postprocessParametersJson ) {
        this.postprocessParametersJson = postprocessParametersJson;
    }

    /**
     * Gets the job max execution time.
     *
     * @return the job max execution time
     */
    public Date getJobMaxExecutionTime() {
        return jobMaxExecutionTime;
    }

    /**
     * Sets the job max execution time.
     *
     * @param jobMaxExecutionTime
     *         the new job max execution time
     */
    public void setJobMaxExecutionTime( Date jobMaxExecutionTime ) {
        this.jobMaxExecutionTime = jobMaxExecutionTime;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public UserEntity getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *         the owner to set
     */
    public void setOwner( UserEntity owner ) {
        this.owner = owner;
    }

    /**
     * Gets the created on.
     *
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the createdOn to set
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the modified on.
     *
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the modifiedOn to set
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * Gets the meta data document.
     *
     * @return the meta data document
     */
    public DocumentEntity getMetaDataDocument() {
        return metaDataDocument;
    }

    /**
     * Sets the meta data document.
     *
     * @param metaDataDocument
     *         the new meta data document
     */
    public void setMetaDataDocument( DocumentEntity metaDataDocument ) {
        this.metaDataDocument = metaDataDocument;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy( UserEntity createdBy ) {
        this.createdBy = createdBy;
    }

    public UserEntity getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy( UserEntity modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    public UUID getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId( UUID workflowId ) {
        this.workflowId = workflowId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Boolean isAutoDelete() {
        return isAutoDelete;
    }

    public void setAutoDelete( Boolean autoDelete ) {
        isAutoDelete = autoDelete;
    }

    public String getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    public void setLifeCycleStatus( String lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    public String getUserSelectionId() {
        return userSelectionId;
    }

    public void setUserSelectionId( String userSelectionId ) {
        this.userSelectionId = userSelectionId;
    }

    public String getGroupSelectionId() {
        return groupSelectionId;
    }

    public void setGroupSelectionId( String groupSelectionId ) {
        this.groupSelectionId = groupSelectionId;
    }

}