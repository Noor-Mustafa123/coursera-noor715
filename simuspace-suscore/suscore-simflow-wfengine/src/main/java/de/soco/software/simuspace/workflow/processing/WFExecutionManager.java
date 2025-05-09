package de.soco.software.simuspace.workflow.processing;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.dexecutor.core.task.ExecutionResults;

import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.WorkflowExecutionParameters;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.SectionWorkflow;
import de.soco.software.simuspace.workflow.util.Diagraph;

/**
 * The Interface of WorkFlow Execution Manager. This is responsible for the execution of a work flow.
 *
 * @author M.Nasir.Farooq
 */
public interface WFExecutionManager {

    /**
     * Sets the subworkflow params.
     *
     * @param jobId
     *         the job id
     *
     * @return the map
     */
    Map< String, Object > setSubworkflowParams( UUID jobId );

    /**
     * Execute workflow.
     *
     * @param jobParameters
     *         the job parameters
     */
    void executeWorkflow( JobParameters jobParameters );

    /**
     * Gets the file jobs list.
     *
     * @return the file jobs list
     */
    Map< UUID, Job > getFileJobsList();

    /**
     * Stop all jobs.
     *
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    boolean stopAllJobs( String userName );

    /**
     * Stop job execution.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    boolean stopJobExecution( String jobId, String userName );

    /**
     * Run workflow with errors new.
     *
     * @param job
     *         the job
     * @param userWorkFlow
     *         the user work flow
     * @param executedElementsIds
     *         the executed elements ids
     * @param originalUserWorkFlow
     *         the original user work flow
     * @param additionalElements
     *         the additional elements
     * @param pythonPath
     *         * the Python Execution path
     *
     * @return the execution results
     */
    ExecutionResults< String, DecisionObject > runWorkflowWithErrors( Job job, UserWorkFlow userWorkFlow, Set< String > executedElementsIds,
            Map< String, Object > parameters, String pythonPath );

    /**
     * Save job.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the job
     */
    Job saveJob( Job jobImpl );

    /**
     * This method calls service updateJob and update workflow status and log.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the element
     */
    boolean updateJob( Job jobImpl );

    /**
     * This method calls service updateJob and update workflow status and log.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return the element
     */
    boolean updateJobLog( Job jobImpl );

    /**
     * Gets the design summary.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param id
     *         the id
     *
     * @return the design summary
     */
    List< Map< String, Object > > getDesignSummary( RestAPI server, RequestHeaders requestHeaders, UUID id );

    /**
     * Gets the graph with single starting point.
     *
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the graph with single starting point
     */
    Diagraph< String > getGraphWithSingleStartingPoint( UserWorkFlow userWorkFlow );

    /**
     * Gets the workflow DTO by id and version id by communicating with server using rest client {@link SuSClient}.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the workflow DTO by id and version id
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    LatestWorkFlowDTO getWorkflowDTOByIdAndVersionId( JobParameters jobParameters ) throws IOException;

    /**
     * Gets the objective variable.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param id
     *         the id
     *
     * @return the objective variable
     */
    List< ObjectiveVariableDTO > getObjectiveVariable( RestAPI server, RequestHeaders requestHeaders, UUID id );

    /**
     * Gets the custom variable.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param id
     *         the id
     *
     * @return the custom variable
     */
    List< ? > getCustomVariable( RestAPI server, RequestHeaders requestHeaders, UUID id );

    /**
     * Execute section workflow new.
     *
     * @param sectionWorkFlow
     *         the section work flow
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     * @param additionalParameters
     *         the additional parameters
     * @param userWorkFlow
     *         the user work flow
     * @param originalUserWorkFlow
     *         the original user work flow
     *
     * @return the execution results
     */
    ExecutionResults< String, DecisionObject > executeSectionWorkflow( SectionWorkflow sectionWorkFlow,
            WorkflowExecutionParameters workflowExecutionParameters, Map< String, Object > additionalParameters,
            UserWorkFlow userWorkFlow );

}