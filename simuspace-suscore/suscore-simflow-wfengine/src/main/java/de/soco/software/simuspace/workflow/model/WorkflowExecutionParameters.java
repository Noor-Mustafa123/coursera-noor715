package de.soco.software.simuspace.workflow.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import de.soco.software.simuspace.workflow.util.Diagraph;

/**
 * The Class WorkflowExecutionParameters.
 */
public class WorkflowExecutionParameters {

    /**
     * The user workFlow.
     */
    private UserWorkFlow userWorkFlow;

    /**
     * The job.
     */
    private Job job;

    /**
     * The workflow graph.
     */
    private Diagraph< String > workflowGraph;

    /**
     * The all elements in workflow.
     */
    private List< UserWFElement > allElements;

    /**
     * The executed elements count.
     */
    private AtomicInteger executedElementsCount;

    /**
     * The executed elements.
     */
    private List< UserWFElement > executedElements;

    /**
     * The python execution path.
     */
    private String pythonExecutionPath;

    /**
     * The executed elements ids.
     */
    private Set< String > executedElementsIds;

    /**
     * The loop start end map.
     */
    private Map< String, Map< String, String > > loopStartEndMap;

    /**
     * Instantiates a new workflow execution parameters.
     *
     * @param userWorkFlow
     *         the user work flow
     * @param job
     *         the job
     * @param workflowGraph
     *         the workflow graph
     * @param allElements
     *         the all elements
     * @param executedElementsCount
     *         the executed elements count
     * @param executedElements
     *         the executed elements
     * @param pythonExecutionPath
     *         the python execution path
     * @param executedElementsIds
     *         the executed elements ids
     */
    public WorkflowExecutionParameters( UserWorkFlow userWorkFlow, Job job, Diagraph< String > workflowGraph,
            List< UserWFElement > allElements, List< UserWFElement > executedElements,
            String pythonExecutionPath, Set< String > executedElementsIds ) {
        super();
        this.userWorkFlow = userWorkFlow;
        this.job = job;
        this.workflowGraph = workflowGraph;
        this.allElements = allElements;
        this.executedElements = executedElements;
        this.pythonExecutionPath = pythonExecutionPath;
        this.executedElementsIds = executedElementsIds;
    }

    /**
     * Gets the user work flow.
     *
     * @return the user work flow
     */
    public UserWorkFlow getUserWorkFlow() {
        return userWorkFlow;
    }

    /**
     * Sets the user work flow.
     *
     * @param userWorkFlow
     *         the new user work flow
     */
    public void setUserWorkFlow( UserWorkFlow userWorkFlow ) {
        this.userWorkFlow = userWorkFlow;
    }

    /**
     * Gets the job.
     *
     * @return the job
     */
    public Job getJob() {
        return job;
    }

    /**
     * Sets the job.
     *
     * @param job
     *         the new job
     */
    public void setJob( Job job ) {
        this.job = job;
    }

    /**
     * Gets the workflow graph.
     *
     * @return the workflow graph
     */
    public Diagraph< String > getWorkflowGraph() {
        return workflowGraph;
    }

    /**
     * Sets the workflow graph.
     *
     * @param workflowGraph
     *         the new workflow graph
     */
    public void setWorkflowGraph( Diagraph< String > workflowGraph ) {
        this.workflowGraph = workflowGraph;
    }

    /**
     * Gets the all elements.
     *
     * @return the all elements
     */
    public List< UserWFElement > getAllElements() {
        return allElements;
    }

    /**
     * Sets the all elements.
     *
     * @param allElements
     *         the new all elements
     */
    public void setAllElements( List< UserWFElement > allElements ) {
        this.allElements = allElements;
    }

    /**
     * Gets the executed elements count.
     *
     * @return the executed elements count
     */
    public AtomicInteger getExecutedElementsCount() {
        return executedElementsCount;
    }

    /**
     * Sets the executed elements count.
     *
     * @param executedElementsCount
     *         the new executed elements count
     */
    public void setExecutedElementsCount( AtomicInteger executedElementsCount ) {
        this.executedElementsCount = executedElementsCount;
    }

    /**
     * Gets the executed elements.
     *
     * @return the executed elements
     */
    public List< UserWFElement > getExecutedElements() {
        return executedElements;
    }

    /**
     * Sets the executed elements.
     *
     * @param executedElements
     *         the new executed elements
     */
    public void setExecutedElements( List< UserWFElement > executedElements ) {
        this.executedElements = executedElements;
    }

    /**
     * Gets the python execution path.
     *
     * @return the python execution path
     */
    public String getPythonExecutionPath() {
        return pythonExecutionPath;
    }

    /**
     * Sets the python execution path.
     *
     * @param pythonExecutionPath
     *         the new python execution path
     */
    public void setPythonExecutionPath( String pythonExecutionPath ) {
        this.pythonExecutionPath = pythonExecutionPath;
    }

    /**
     * Gets the executed elements ids.
     *
     * @return the executed elements ids
     */
    public Set< String > getExecutedElementsIds() {
        return executedElementsIds;
    }

    /**
     * Sets the executed elements ids.
     *
     * @param executedElementsIds
     *         the new executed elements ids
     */
    public void setExecutedElementsIds( Set< String > executedElementsIds ) {
        this.executedElementsIds = executedElementsIds;
    }

    /**
     * Gets the loop start end map.
     *
     * @return the loop start end map
     */
    public Map< String, Map< String, String > > getLoopStartEndMap() {
        return loopStartEndMap;
    }

    /**
     * Sets the loop start end map.
     *
     * @param loopStartEndMap
     *         the loop start end map
     */
    public void setLoopStartEndMap( Map< String, Map< String, String > > loopStartEndMap ) {
        this.loopStartEndMap = loopStartEndMap;
    }

}
