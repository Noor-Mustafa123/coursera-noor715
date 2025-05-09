package de.soco.software.simuspace.workflow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.workflow.model.impl.NodeEdge;

/**
 * The Class is responsible to map the workflow json with elements and connections.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowModel {

    /**
     * The list of connections of elements.
     */
    private List< NodeEdge > edges;

    /**
     * The list of elements.
     */
    private List< WorkflowElement > nodes;

    /**
     * Instantiates a new wo rkflow definition dto.
     */
    public WorkflowModel() {
        super();
    }

    /**
     * Instantiates a new workflow definition dto.
     *
     * @param edges
     *         the edges
     * @param nodes
     *         the nodes
     */
    public WorkflowModel( List< NodeEdge > edges, List< WorkflowElement > nodes ) {
        super();
        this.edges = edges;
        this.nodes = nodes;

    }

    /**
     * Gets the connections.
     *
     * @return the connections
     */
    public List< NodeEdge > getEdges() {
        return edges;
    }

    /**
     * Gets the elements.
     *
     * @return the elements
     */
    public List< WorkflowElement > getNodes() {
        return nodes;
    }

    /**
     * Sets the connections.
     *
     * @param connections
     *         the new connections
     */
    public void setEdges( List< NodeEdge > connections ) {
        edges = connections;
    }

    /**
     * Sets the elements.
     *
     * @param elements
     *         the new elements
     */
    public void setNodes( List< WorkflowElement > elements ) {
        nodes = elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "WorkflowModel [edges=" + edges + ", nodes=" + nodes + "]";
    }

}
