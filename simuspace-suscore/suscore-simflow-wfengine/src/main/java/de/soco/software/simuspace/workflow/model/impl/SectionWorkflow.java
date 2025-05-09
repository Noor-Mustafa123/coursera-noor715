package de.soco.software.simuspace.workflow.model.impl;

import java.util.ArrayList;
import java.util.List;

import de.soco.software.simuspace.workflow.model.UserWFElement;

/**
 * Section workflows are workflow which contains only divided part of original workflow.
 */
public class SectionWorkflow {

    /**
     * This is a directory to create a log file
     */
    private String startElementId;

    /**
     * This is a directory to create a log file
     */
    private String endElementId;

    /**
     * The list of elements of section work flow.
     */
    private List< UserWFElement > nodes;

    /**
     * The connections.
     */
    private List< NodeEdge > edges = new ArrayList<>();

    public SectionWorkflow( String startElementId, String endElementId, List< UserWFElement > nodes, List< NodeEdge > edges ) {
        super();
        this.startElementId = startElementId;
        this.endElementId = endElementId;
        this.nodes = nodes;
        this.edges = edges;
    }

    public String getStartElementId() {
        return startElementId;
    }

    public void setStartElementId( String startElementId ) {
        this.startElementId = startElementId;
    }

    public String getEndElementId() {
        return endElementId;
    }

    public void setEndElementId( String endElementId ) {
        this.endElementId = endElementId;
    }

    public List< UserWFElement > getNodes() {
        return nodes;
    }

    public void setNodes( List< UserWFElement > nodes ) {
        this.nodes = nodes;
    }

    public List< NodeEdge > getEdges() {
        return edges;
    }

    public void setEdges( List< NodeEdge > edges ) {
        this.edges = edges;
    }

}
