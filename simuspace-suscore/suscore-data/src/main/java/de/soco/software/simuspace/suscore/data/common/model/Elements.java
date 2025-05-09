package de.soco.software.simuspace.suscore.data.common.model;

import java.util.ArrayList;

/**
 * The Class Elements.
 *
 * @author Noman Arshad
 */
public class Elements {

    /**
     * The nodes.
     */
    ArrayList< NodeIdNameData > nodes = new ArrayList<>();

    /**
     * The edges.
     */
    ArrayList< SourceTargetData > edges = new ArrayList<>();

    /**
     * Gets the edges.
     *
     * @return the edges
     */
    public ArrayList< SourceTargetData > getEdges() {
        return edges;
    }

    /**
     * Sets the edges.
     *
     * @param edges
     *         the new edges
     */
    public void setEdges( ArrayList< SourceTargetData > edges ) {
        this.edges = edges;
    }

    /**
     * Instantiates a new elements.
     */
    public Elements() {
    }

    /**
     * Gets the nodes.
     *
     * @return the nodes
     */
    public ArrayList< NodeIdNameData > getNodes() {
        return nodes;
    }

    /**
     * Sets the nodes.
     *
     * @param nodes
     *         the new nodes
     */
    public void setNodes( ArrayList< NodeIdNameData > nodes ) {
        this.nodes = nodes;
    }

}
