package de.soco.software.simuspace.workflow.model.impl;

public class TraceObjectDTO extends ObjectDTO {

    /**
     * The plot.
     */
    private String plot;

    public TraceObjectDTO( String plot ) {
        super();
        this.plot = plot;
    }

    /**
     * Gets the plot.
     *
     * @return the plot
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Sets the plot.
     *
     * @param plot
     *         the new plot
     */
    public void setPlot( String plot ) {
        this.plot = plot;
    }

}
