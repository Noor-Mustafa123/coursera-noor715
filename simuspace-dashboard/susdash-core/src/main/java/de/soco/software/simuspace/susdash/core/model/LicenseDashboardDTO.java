package de.soco.software.simuspace.susdash.core.model;

/**
 * The Class LicenseDashboardDTO.
 */
public class LicenseDashboardDTO {

    /**
     * The date range.
     */
    private DateRangeDTO dateRange;

    /**
     * The total.
     */
    private Integer total;

    /**
     * Instantiates a new dashborad license DTO.
     */
    public LicenseDashboardDTO() {
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
