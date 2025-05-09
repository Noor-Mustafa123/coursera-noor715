package de.soco.software.simuspace.susdash.project.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The type Project classification cache.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ProjectClassificationCache implements Serializable {

    /**
     * The constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 540636209760523938L;

    /**
     * The Max depth.
     */
    private Integer maxDepth;

    /**
     * The Chart.
     */
    private List< ProjectStructureTupleWrapper > chartPayload;

    /**
     * The Structure.
     */
    private List< ProjectStructureTupleWrapper > structure;

    /**
     * The Lifecycle.
     */
    private List< LifecycleTableDTO > lifecycle;

    /**
     * The Lifecycle.
     */
    private Date lastUpdated;

    /**
     * Instantiates a new Project classification cache.
     *
     * @param maxDepth
     *         the max depth
     * @param chartPayload
     *         the chart
     * @param structure
     *         the structure
     * @param lifecycle
     *         the lifecycle
     * @param lastUpdated
     *         the last updated
     */
    public ProjectClassificationCache( Integer maxDepth, List< ProjectStructureTupleWrapper > chartPayload,
            List< ProjectStructureTupleWrapper > structure, List< LifecycleTableDTO > lifecycle, Date lastUpdated ) {
        this.maxDepth = maxDepth;
        this.chartPayload = chartPayload;
        this.structure = structure;
        this.lifecycle = lifecycle;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Instantiates a new Project classification cache.
     */
    public ProjectClassificationCache() {
    }

    /**
     * Gets chart.
     *
     * @return the chart
     */
    public List< ProjectStructureTupleWrapper > getChartPayload() {
        return chartPayload;
    }

    /**
     * Gets last updated.
     *
     * @return the last updated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Gets lifecycle.
     *
     * @return the lifecycle
     */
    public List< LifecycleTableDTO > getLifecycle() {
        return lifecycle;
    }

    /**
     * Gets max depth.
     *
     * @return the max depth
     */
    public Integer getMaxDepth() {
        return maxDepth;
    }

    /**
     * Gets structure.
     *
     * @return the structure
     */
    public List< ProjectStructureTupleWrapper > getStructure() {
        return structure;
    }

    /**
     * Sets max depth.
     *
     * @param maxDepth
     *         the max depth
     */
    public void setMaxDepth( Integer maxDepth ) {
        this.maxDepth = maxDepth;
    }

    /**
     * Sets chart.
     *
     * @param chartPayload
     *         the chart
     */
    public void setChartPayload( List< ProjectStructureTupleWrapper > chartPayload ) {
        this.chartPayload = chartPayload;
    }

    /**
     * Sets structure.
     *
     * @param structure
     *         the structure
     */
    public void setStructure( List< ProjectStructureTupleWrapper > structure ) {
        this.structure = structure;
    }

    /**
     * Sets lifecycle.
     *
     * @param lifecycle
     *         the lifecycle
     */
    public void setLifecycle( List< LifecycleTableDTO > lifecycle ) {
        this.lifecycle = lifecycle;
    }

    /**
     * Sets last updated.
     *
     * @param lastUpdated
     *         the last updated
     */
    public void setLastUpdated( Date lastUpdated ) {
        this.lastUpdated = lastUpdated;
    }

}
