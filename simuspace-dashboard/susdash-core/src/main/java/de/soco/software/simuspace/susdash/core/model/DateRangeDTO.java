package de.soco.software.simuspace.susdash.core.model;

/**
 * The Class DateRangeDTO.
 */
public class DateRangeDTO {

    /**
     * The start.
     */
    private Long start;

    /**
     * The end.
     */
    private Long end;

    /**
     * Instantiates a new date range DTO.
     */
    public DateRangeDTO() {
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public Long getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start
     *         the new start
     */
    public void setStart( Long start ) {
        this.start = start;
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public Long getEnd() {
        return end;
    }

    /**
     * Sets the end.
     *
     * @param end
     *         the new end
     */
    public void setEnd( Long end ) {
        this.end = end;
    }

}
