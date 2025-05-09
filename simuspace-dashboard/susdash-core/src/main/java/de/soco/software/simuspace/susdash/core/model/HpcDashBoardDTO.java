package de.soco.software.simuspace.susdash.core.model;

/**
 * The Class HpcDashBoardDTO.
 */
public class HpcDashBoardDTO {

    /**
     * The date range.
     */
    private DateRangeDTO dateRange;

    /**
     * The total.
     */
    private Integer total;

    /**
     * Instantiates a new Hpc Femzip DTO.
     */
    public HpcDashBoardDTO() {
    }

    /**
     * Gets the date range.
     *
     * @return the date range
     */
    public DateRangeDTO getDateRange() {
        return dateRange;
    }

    /**
     * Sets the date range.
     *
     * @param dateRange
     *         the new date range
     */
    public void setDateRange( DateRangeDTO dateRange ) {
        this.dateRange = dateRange;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total
     *         the new total
     */
    public void setTotal( Integer total ) {
        this.total = total;
    }

}
