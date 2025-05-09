package de.soco.software.simuspace.workflow.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.ElementConnection;

/**
 * The is responsible to map workflow element node edge.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class NodeEdge {

    /**
     * The connections.
     */
    private ElementConnection data;

    /**
     * Instantiates a new node edge.
     */
    public NodeEdge() {
        super();
    }

    /**
     * Instantiates a new node edge.
     *
     * @param data
     *         the data
     */
    public NodeEdge( ElementConnection data ) {
        super();
        this.data = data;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public ElementConnection getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( ElementConnection data ) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NodeEdge [data=" + data + "]";
    }

}
