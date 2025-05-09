package de.soco.software.simuspace.workflow.model.impl;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.workflow.dto.Status;

/**
 * The type Personal job dto.
 */
public class UserJobDTO {

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
     * The status.
     */
    @UIColumn( data = "status.name", name = "status", filter = "uuid", renderer = "text", title = "3000049x4", orderNum = 5 )
    private Status status;

    /**
     * To show progress bar on front-End.
     */
    @UIColumn( data = "progress", name = "type", filter = "", renderer = "progress", title = "3000072x4", orderNum = 4, isSortable = false )
    private ProgressBar progress;

    /**
     * Gets job integer.
     *
     * @return the job integer
     */
    public Integer getJobInteger() {
        return jobInteger;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public ProgressBar getProgress() {
        return progress;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets job integer.
     *
     * @param jobInteger
     *         the job integer
     */
    public void setJobInteger( Integer jobInteger ) {
        this.jobInteger = jobInteger;
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Sets status.
     *
     * @param status
     *         the status
     */
    public void setStatus( Status status ) {
        this.status = status;
    }

    /**
     * Sets progress.
     *
     * @param progress
     *         the progress
     */
    public void setProgress( ProgressBar progress ) {
        this.progress = progress;
    }

}
