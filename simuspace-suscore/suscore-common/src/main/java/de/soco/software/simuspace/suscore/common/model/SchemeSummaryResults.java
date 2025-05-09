package de.soco.software.simuspace.suscore.common.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class SchemeSummaryResults.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SchemeSummaryResults {

    /**
     * The experiment number.
     */
    private String experimentNumber;

    /**
     * The design summary map.
     */
    private Map< String, Object > designSummaryMap;

    /**
     * The object summary map.
     */
    private Map< String, Object > objectSummaryMap;

    /**
     * The goal.
     */
    private Map< String, Object > goal;

    /**
     * Gets the experiment number.
     *
     * @return the experiment number
     */
    public String getExperimentNumber() {
        return experimentNumber;
    }

    /**
     * Sets the experiment number.
     *
     * @param experimentNumber
     *         the new experiment number
     */
    public void setExperimentNumber( String experimentNumber ) {
        this.experimentNumber = experimentNumber;
    }

    /**
     * Gets the design summary map.
     *
     * @return the design summary map
     */
    public Map< String, Object > getDesignSummaryMap() {
        return designSummaryMap;
    }

    /**
     * Sets the design summary map.
     *
     * @param designSummaryMap
     *         the design summary map
     */
    public void setDesignSummaryMap( Map< String, Object > designSummaryMap ) {
        this.designSummaryMap = designSummaryMap;
    }

    /**
     * Gets the object summary map.
     *
     * @return the object summary map
     */
    public Map< String, Object > getObjectSummaryMap() {
        return objectSummaryMap;
    }

    /**
     * Sets the object summary map.
     *
     * @param objectSummaryMap
     *         the object summary map
     */
    public void setObjectSummaryMap( Map< String, Object > objectSummaryMap ) {
        this.objectSummaryMap = objectSummaryMap;
    }

    /**
     * Gets the goal.
     *
     * @return the goal
     */
    public Map< String, Object > getGoal() {
        return goal;
    }

    /**
     * Sets the goal.
     *
     * @param goal
     *         the goal
     */
    public void setGoal( Map< String, Object > goal ) {
        this.goal = goal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SchemeSummaryResults [experimentNumber=" + experimentNumber + ", designSummaryMap=" + designSummaryMap
                + ", objectSummaryMap=" + objectSummaryMap + ", goal=" + goal + "]";
    }

}