package de.soco.software.simuspace.workflow.dexecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.github.dexecutor.core.ExecutionConfig;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.ExecutionResults;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.workflow.model.WorkflowExecutionParameters;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;

/**
 * This class is designed to build and run tasks provided by {@link WorkFlowTaskProvider} one by one depending upon their dependency on each
 * other.
 */
@Log4j2
public class ExecutorServiceManager {

    /**
     * The dexecutor.
     */
    private final SimDexecutor< String, DecisionObject > dExecutor;

    /**
     * The executor service.
     */
    private ExecutorService executorService;

    /**
     * Instantiates a new executor service manager.
     *
     * @param list
     *         the list
     * @param provider
     *         the provider
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     */
    public ExecutorServiceManager( List< NodeEdge > list, WorkFlowTaskProvider provider,
            WorkflowExecutionParameters workflowExecutionParameters ) {
        dExecutor = buildDexecutor( buildExecutor(), provider );
        buildGraph( list, workflowExecutionParameters );
    }

    /**
     * Will wait some time then shutdown executor.
     *
     * @param executor
     *         the executor
     */
    private void awaitTermination( final ExecutorService executor ) {
        try {
            executor.shutdown();
            executor.awaitTermination( 1, TimeUnit.SECONDS );
        } catch ( final InterruptedException e ) {
            ExceptionLogger.logException( e, getClass() );
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Builds the dexecutor.
     *
     * @param executorService
     *         the executor service
     * @param provider
     *         the provider
     *
     * @return the dexecutor
     */
    private SimDexecutor< String, DecisionObject > buildDexecutor( final ExecutorService executorService, WorkFlowTaskProvider provider ) {
        final SimDexecutorConfig< String, DecisionObject > config = new SimDexecutorConfig<>( executorService, provider );
        return new SimDexecutor<>( config );
    }

    /**
     * Builds the executor service for further tasks execution.
     *
     * @return the executor service
     */
    private ExecutorService buildExecutor() {
        executorService = Executors.newFixedThreadPool( ThreadPoolUtil.ioIntesivePoolSize() );
        return executorService;
    }

    /**
     * Builds the graph.
     *
     * @param connections
     *         the connections
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     */
    private void buildGraph( List< NodeEdge > connections, WorkflowExecutionParameters workflowExecutionParameters ) {
        for ( final NodeEdge elementConnection : connections ) {
            if ( StringUtils.isNotBlank( elementConnection.getData().getSource() )
                    && StringUtils.isNotBlank( elementConnection.getData().getTarget() ) ) {
                String target = elementConnection.getData().getTarget();
                String source = elementConnection.getData().getSource();
                if ( !( workflowExecutionParameters.getLoopStartEndMap().containsKey( target )
                        && workflowExecutionParameters.getLoopStartEndMap().get( target ).get( "end" ).equals( source ) ) ) {
                    dExecutor.addDependency( source, target );
                }
            } else if ( StringUtils.isNotBlank( elementConnection.getData().getSource() ) ) {
                dExecutor.addIndependent( elementConnection.getData().getSource() );
            } else if ( StringUtils.isNotBlank( elementConnection.getData().getTarget() ) ) {
                dExecutor.addIndependent( elementConnection.getData().getTarget() );
            }
            log.info( "connection add : " + elementConnection.getData().getSource() );
        }
    }

    /**
     * Responsible for executing the tasks in executor service.
     *
     * @return the execution results
     */
    public ExecutionResults< String, DecisionObject > execute() {
        ExecutionResults< String, DecisionObject > errors = new ExecutionResults<>();
        if ( dExecutor != null ) {
            errors = dExecutor.execute( ExecutionConfig.TERMINATING );
        }
        awaitTermination( executorService );
        return errors;
    }

    public Map< String, Object > getSubWorkflowParams() {
        return dExecutor.getParams();
    }

    /**
     * Gets the executor service.
     *
     * @return the executor service
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Sets the executor service.
     *
     * @param executorService
     *         the new executor service
     */
    public void setExecutorService( ExecutorService executorService ) {
        this.executorService = executorService;
    }

}
