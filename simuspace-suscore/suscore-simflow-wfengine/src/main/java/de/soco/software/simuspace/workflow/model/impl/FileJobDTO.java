package de.soco.software.simuspace.workflow.model.impl;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.workflow.dto.Status;

/**
 * This Class is used to transfer data to file jobs table
 */
public class FileJobDTO {

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "", renderer = "text", title = "3000032x4", orderNum = 1, isSortable = false, width = 0 )
    private String name;

    /**
     * The description of job.
     */
    @UIColumn( data = "description", name = "description", filter = "", renderer = "text", title = "3000011x4", orderNum = 2, isSortable = false )
    private String description;

    /**
     * To show progress bar on front-End.
     */
    @UIColumn( data = "progress", name = "type", filter = "", renderer = "progress", title = "3000072x4", orderNum = 3, isSortable = false )
    private ProgressBar progress;

    /**
     * The status.
     */
    @UIColumn( data = "status.name", name = "status", filter = "", renderer = "text", title = "3000049x4", orderNum = 4, isSortable = false )
    private Status status;

    /**
     * The user who create a work flow.
     */
    @UIColumn( data = "createdBy.userUid", filter = "", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy", tooltip = "{createdBy.userName}", orderNum = 6, isSortable = false )
    private UserDTO createdBy;

    /**
     * The submit time of job.
     */
    @UIColumn( data = "submitTime", name = "createdOn", filter = "", renderer = "date", title = "3000081x4", orderNum = 6, isSortable = false )
    private Date submitTime;

    /**
     * The unique id of job.
     */
    @UIColumn( data = "id", name = "id", filter = "", renderer = "text", title = "3000021x4", isShow = false, orderNum = 9, isSortable = false )
    private UUID id;

    /**
     * The run mode.
     */
    @UIColumn( data = "runsOn", name = "runsOn", filter = "", renderer = "text", title = "3000124x4", orderNum = 10, isSortable = false )
    private String runsOn;

    /**
     * The machine in which job is running.
     */
    @UIColumn( data = "machine", name = "machine", filter = "", renderer = "text", title = "3000080x4", orderNum = 16, isSortable = false )
    private String machine;

    /**
     * The completion time.
     */
    @UIColumn( data = "completionTime", name = "finishedOn", filter = "", renderer = "date", title = "3000107x4", orderNum = 19, isSortable = false )
    private Date completionTime;

    /**
     * The working directory of a workflow.
     */
    @UIColumn( data = "workingDir.path", name = "jobDirectory", filter = "", renderer = "text", title = "3000108x4", orderNum = 21, isSortable = false )
    private EngineFile workingDir;

    /**
     * The operating system.
     */
    @UIColumn( data = "os", name = "os", filter = "", renderer = "text", title = "3000109x4", orderNum = 24, isSortable = false )
    private String os;

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime( Date completionTime ) {
        this.completionTime = completionTime;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine( String machine ) {
        this.machine = machine;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public void setProgress( ProgressBar progress ) {
        this.progress = progress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus( Status status ) {
        this.status = status;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime( Date submitTime ) {
        this.submitTime = submitTime;
    }

    public EngineFile getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir( EngineFile workingDir ) {
        this.workingDir = workingDir;
    }

    public String getOs() {
        return os;
    }

    public void setOs( String os ) {
        this.os = os;
    }

    public String getRunsOn() {
        return runsOn;
    }

    public void setRunsOn( String runsOn ) {
        this.runsOn = runsOn;
    }

}
