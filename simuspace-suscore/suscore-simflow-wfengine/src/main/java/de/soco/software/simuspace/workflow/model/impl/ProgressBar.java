package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This Class represent the progress of any task by showing total and completed number of execution units
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ProgressBar {

    /**
     * The number of elements which has completed the execution.
     */
    private long completed;

    /**
     * The total number of elements to be processed.
     */
    private long total;

    /**
     * Instantiates a new progress bar.
     */
    public ProgressBar() {
        super();
        total = 0;
        completed = 0;

    }

    /**
     * Instantiates a new progress bar.
     *
     * @param total
     *         the total
     */
    public ProgressBar( long total ) {
        super();
        this.total = total;
        completed = 0;
    }

    /**
     * Instantiates a new progress bar.
     *
     * @param total
     *         the total
     * @param completed
     *         the completed
     */
    public ProgressBar( long total, long completed ) {
        this( total );
        this.completed = completed;
    }

    /**
     * Gets the completed.
     *
     * @return the completed
     */
    public long getCompleted() {
        return completed;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets the completed.
     *
     * @param completed
     *         the new completed
     */
    public void setCompleted( long completed ) {
        this.completed = completed;
    }

    /**
     * Sets the total.
     *
     * @param total
     *         the new total
     */
    public void setTotal( long total ) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProgressBar[ total=" + total + ", completed=" + completed + "]";
    }

}
