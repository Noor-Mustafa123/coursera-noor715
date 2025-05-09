package de.soco.software.simuspace.susdash.core.model;

/**
 * The Class ProjectDashboardDTO.
 */
public class ProjectDashboardDTO {

    /**
     * The column number.
     */
    private Integer columnNumber;

    /**
     * The max depth.
     */
    private Integer maxDepth;

    /**
     * Instantiates a new project dashboard DTO.
     */
    public ProjectDashboardDTO() {
        super();
    }

    /**
     * Instantiates a new project dashboard DTO.
     *
     * @param columnNumber
     *         the column number
     * @param maxDepth
     *         the max depth
     */
    public ProjectDashboardDTO( Integer columnNumber, Integer maxDepth ) {
        super();
        this.columnNumber = columnNumber;
        this.maxDepth = maxDepth;
    }

    /**
     * Gets the column number.
     *
     * @return the column number
     */
    public Integer getColumnNumber() {
        return columnNumber;
    }

    /**
     * Sets the column number.
     *
     * @param columnNumber
     *         the new column number
     */
    public void setColumnNumber( Integer columnNumber ) {
        this.columnNumber = columnNumber;
    }

    /**
     * Gets the max depth.
     *
     * @return the max depth
     */
    public Integer getMaxDepth() {
        return maxDepth;
    }

    /**
     * Sets the max depth.
     *
     * @param maxDepth
     *         the new max depth
     */
    public void setMaxDepth( Integer maxDepth ) {
        this.maxDepth = maxDepth;
    }

}
