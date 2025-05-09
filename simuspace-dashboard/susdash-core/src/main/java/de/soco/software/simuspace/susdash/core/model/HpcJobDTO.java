package de.soco.software.simuspace.susdash.core.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;

/**
 * A user model class for mapping to json
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class HpcJobDTO implements Serializable {

    private static final long serialVersionUID = -8194366832482910262L;

    /**
     * The Id.
     */
    @UIColumn( data = "id", name = "JB_job_number", filter = "uuid", renderer = "text", title = "3000021x4", orderNum = 0 )
    private String id;

    /**
     * The Job name.
     */
    @UIColumn( data = "jobName", name = "JB_name", filter = "text", renderer = "text", title = "3000197x4", orderNum = 1 )
    private String jobName;

    /**
     * The User.
     */
    @UIColumn( data = "user", name = "JB_owner", filter = "text", renderer = "text", title = "3000198x4", orderNum = 2 )
    private String user;

    /**
     * The Application.
     */
    @UIColumn( data = "application", name = "Application", filter = "text", renderer = "text", title = "3000199x4", isShow = false, orderNum = 3 )
    private String application;

    /**
     * The Project.
     */
    @UIColumn( data = "project", name = "JB_project", filter = "text", renderer = "text", title = "3000200x4", orderNum = 4 )
    private String project;

    /**
     * The Slots.
     */
    @UIColumn( data = "slots", name = "slots", filter = "text", renderer = "text", title = "3000201x4", orderNum = 5 )
    private String slots;

    /**
     * The Queue.
     */
    @UIColumn( data = "queue", name = "queue_name", filter = "text", renderer = "text", title = "3000202x4", orderNum = 6 )
    private String queue;

    /**
     * The State.
     */
    @UIColumn( data = "state", name = "@state", filter = "text", renderer = "text", title = "3000203x4", orderNum = 7 )
    private String state;

    /**
     * The Submit time.
     */
    @UIColumn( data = "submitTime", name = "submittime", filter = "dateRange", renderer = "date", title = "3000204x4", orderNum = 8 )
    private Date submitTime;

    /**
     * The Job Start Time.
     */
    @UIColumn( data = "jobStartTime", name = "jobstarttime", filter = "dateRange", renderer = "date", title = "3000217x4", isShow = false, orderNum = 9 )
    private Date jobStartTime;

    /**
     * The Job Start Time.
     */
    @UIColumn( data = "calcStartTime", name = "calcstarttime", filter = "dateRange", renderer = "date", title = "3000218x4", isShow = false, orderNum = 10 )
    private Date calcStartTime;

    /**
     * The Working directory.
     */
    @UIColumn( data = "workingDirectory", name = "workingdirectory", filter = "text", renderer = "text", title = "3000215x4", isShow = false, orderNum = 11 )
    private String workingDirectory;

    /**
     * The Working directory.
     */
    @UIColumn( data = "resultDirectory", name = "resultdirectory", filter = "text", renderer = "text", title = "3000219x4", isShow = false, orderNum = 12 )
    private String resultDirectory;

    /**
     * The Application Version.
     */
    @UIColumn( data = "applVersion", name = "applversion", filter = "text", renderer = "text", title = "3000216x4", isShow = false, orderNum = 13 )
    private String applVersion;

    /**
     * The FS DIRECTORY:.
     */
    @UIColumn( data = "fsDirectory", name = "fsdirectory", filter = "text", renderer = "text", title = "3000220x4", isShow = false, orderNum = 14 )
    private String fsDirectory;

    /**
     * The FS DIRECTORY:.
     */
    @UIColumn( data = "wfHome", name = "wfhome", filter = "text", renderer = "text", title = "3000221x4", isShow = false, orderNum = 15 )
    private String wfHome;

    /**
     * The FS DIRECTORY.
     */
    @UIColumn( data = "wfSite", name = "wfsite", filter = "text", renderer = "text", title = "3000222x4", isShow = false, orderNum = 16 )
    private String wfSite;

    /**
     * The Host.
     */
    @UIColumn( data = "host", name = "host", filter = "text", renderer = "text", title = "3000223x4", isShow = false, orderNum = 17 )
    private String host;

    /**
     * The Host Name.
     */
    @UIColumn( data = "hostName", name = "hostname", filter = "text", renderer = "text", title = "3000224x4", isShow = false, orderNum = 18 )
    private String hostName;

    /**
     * The Disk Space.
     */
    @UIColumn( data = "diskSpace", name = "diskspace", filter = "text", renderer = "text", title = "3000225x4", isShow = false, orderNum = 19 )
    private String diskSpace;

    /**
     * The Host Name.
     */
    @UIColumn( data = "parallelEnv", name = "parallelenv", filter = "text", renderer = "text", title = "3000226x4", isShow = false, orderNum = 20 )
    private String parallelEnv;

    /**
     * The Host Name.
     */
    @UIColumn( data = "nodes", name = "nodes", filter = "text", renderer = "text", title = "3000227x4", isShow = false, orderNum = 21 )
    private String nodes;

    /**
     * The Workflow.
     */
    @UIColumn( data = "workflow", name = "workflow", filter = "text", renderer = "text", title = "3000228x4", isShow = false, orderNum = 22 )
    private String workflow;

    /**
     * Instantiates a new Hpc dto.
     */
    public HpcJobDTO() {
        super();
    }

    /**
     * Instantiates a new Hpc dto.
     *
     * @param id
     *         the id
     * @param jobName
     *         the job name
     * @param user
     *         the user
     * @param application
     *         the application
     * @param project
     *         the project
     * @param slots
     *         the slots
     * @param queue
     *         the queue
     * @param state
     *         the state
     * @param submitTime
     *         the submit time
     */
    public HpcJobDTO( String id, String host, String hostName, String jobName, String user, String application, String project,
            String wfHome, String wfSite, String slots, String queue, String state, Date submitTime, Date jobStartTime, Date calcStartTime,
            String workingDirectory, String resultDirectory, String fsDirectory, String applVersion, String diskSpace, String parallelEnv,
            String nodes ) {
        super();
        this.id = id;
        this.jobName = jobName;
        this.user = user;
        this.application = application;
        this.project = project;
        this.wfHome = wfHome;
        this.wfSite = wfSite;
        this.slots = slots;
        this.queue = queue;
        this.state = state;
        this.submitTime = submitTime;
        this.jobStartTime = jobStartTime;
        this.calcStartTime = calcStartTime;
        this.workingDirectory = workingDirectory;
        this.resultDirectory = resultDirectory;
        this.fsDirectory = fsDirectory;
        this.applVersion = applVersion;
        this.host = host;
        this.hostName = hostName;
        this.diskSpace = diskSpace;
        this.parallelEnv = parallelEnv;
        this.nodes = nodes;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id to set
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets job name.
     *
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets job name.
     *
     * @param jobName
     *         the jobName to set
     */
    public void setJobName( String jobName ) {
        this.jobName = jobName;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user
     *         the user to set
     */
    public void setUser( String user ) {
        this.user = user;
    }

    /**
     * Gets application.
     *
     * @return the application
     */
    public String getApplication() {
        return application;
    }

    /**
     * Sets application.
     *
     * @param application
     *         the application to set
     */
    public void setApplication( String application ) {
        this.application = application;
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project
     *         the project to set
     */
    public void setProject( String project ) {
        this.project = project;
    }

    /**
     * Gets slots.
     *
     * @return the slots
     */
    public String getSlots() {
        return slots;
    }

    /**
     * Sets slots.
     *
     * @param slots
     *         the slots to set
     */
    public void setSlots( String slots ) {
        this.slots = slots;
    }

    /**
     * Gets queue.
     *
     * @return the queue
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Sets queue.
     *
     * @param queue
     *         the queue to set
     */
    public void setQueue( String queue ) {
        this.queue = queue;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state
     *         the state to set
     */
    public void setState( String state ) {
        this.state = state;
    }

    /**
     * Gets submit time.
     *
     * @return the submitTime
     */
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     * Sets submit time.
     *
     * @param submitTime
     *         the submitTime to set
     */
    public void setSubmitTime( Date submitTime ) {
        this.submitTime = submitTime;
    }

    /**
     * Gets working directory.
     *
     * @return the workingDirectory
     */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Sets working directory.
     *
     * @param workingDirectory
     *         the workingDirectory to set
     */
    public void setWorkingDirectory( String workingDirectory ) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * Gets application version.
     *
     * @return the applVersion
     */
    public String getApplicationVersion() {
        return applVersion;
    }

    /**
     * Sets application version.
     *
     * @param applVersion
     *         the applVersion to set
     */
    public void setApplicationVersion( String applVersion ) {
        this.applVersion = applVersion;
    }

    /**
     * Gets Job Start Time.
     *
     * @return the jobStartTime
     */
    public Date getJobStartTime() {
        return jobStartTime;
    }

    /**
     * Sets Job Start Time.
     *
     * @param jobStartTime
     *         the jobStartTime to set
     */
    public void setJobStartTime( Date jobStartTime ) {
        this.jobStartTime = jobStartTime;
    }

    /**
     * Gets Calculation Start Time.
     *
     * @return the calcStartTime
     */
    public Date getCalcStartTime() {
        return calcStartTime;
    }

    /**
     * Sets Calculation Start Time.
     *
     * @param calcStartTime
     *         the calcStartTime to set
     */
    public void setCalcStartTime( Date calcStartTime ) {
        this.calcStartTime = calcStartTime;
    }

    /**
     * Gets Result Directory.
     *
     * @return the resultDirectory
     */
    public String getResultDirectory() {
        return resultDirectory;
    }

    /**
     * Sets Result Directory.
     *
     * @param resultDirectory
     *         the resultDirectory to set
     */
    public void setResultDirectory( String resultDirectory ) {
        this.resultDirectory = resultDirectory;
    }

    /**
     * Gets FS Directory.
     *
     * @return the fsDirectory
     */
    public String getFSDirectory() {
        return fsDirectory;
    }

    /**
     * Sets FS Directory.
     *
     * @param fsDirectory
     *         the fsDirectory to set
     */
    public void setFSDirectory( String fsDirectory ) {
        this.fsDirectory = fsDirectory;
    }

    /**
     * Gets Workflow Home.
     *
     * @return the wfHome
     */
    public String getWFHome() {
        return wfHome;
    }

    /**
     * Sets Workflow Home.
     *
     * @param wfHome
     *         the wfHome to set
     */
    public void setWFHome( String wfHome ) {
        this.wfHome = wfHome;
    }

    /**
     * Gets Workflow Site.
     *
     * @return the wfSite
     */
    public String getWFSite() {
        return wfSite;
    }

    /**
     * Sets Workflow Site.
     *
     * @param wfSite
     *         the wfSite to set
     */
    public void setWFSite( String wfSite ) {
        this.wfSite = wfSite;
    }

    /**
     * Gets Host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets Host.
     *
     * @param host
     *         the host to set
     */
    public void setHost( String host ) {
        this.host = host;
    }

    /**
     * Gets Host Name.
     *
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets Host Name.
     *
     * @param hostName
     *         the hostName to set
     */
    public void setHostName( String hostName ) {
        this.hostName = hostName;
    }

    /**
     * Gets Disk Space.
     *
     * @return the diskSpace
     */
    public String getDiskSpace() {
        return diskSpace;
    }

    /**
     * Sets Disk Space.
     *
     * @param diskSpace
     *         the diskSpace to set
     */
    public void setDiskSpace( String diskSpace ) {
        this.diskSpace = diskSpace;
    }

    /**
     * Gets Parallel Environment.
     *
     * @return the parallelEnv
     */
    public String getParalleEnv() {
        return parallelEnv;
    }

    /**
     * Sets Parallel Environment.
     *
     * @param parallelEnv
     *         the parallelEnv to set
     */
    public void setParalleEnv( String parallelEnv ) {
        this.parallelEnv = parallelEnv;
    }

    /**
     * Gets Nodes.
     *
     * @return the nodes
     */
    public String getNodes() {
        return nodes;
    }

    /**
     * Sets Nodes.
     *
     * @param nodes
     *         the nodes to set
     */
    public void setNodes( String nodes ) {
        this.nodes = nodes;
    }

    /**
     * Gets Workflow.
     *
     * @return the workflow
     */
    public String getWorkflow() {
        return workflow;
    }

    /**
     * Sets Workflow.
     *
     * @param workflow
     *         the workflow to set
     */
    public void setWorkflow( String workflow ) {
        this.workflow = workflow;
    }

}
