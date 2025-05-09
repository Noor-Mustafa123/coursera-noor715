package de.soco.software.simuspace.workflow.processing.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.SuSConditionalExpressionEvaluator;

/**
 * This Class designed to evaluate SUS expression and on the behalf of that expression workflow will decide how to proceed further.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class ConditionalElementAction extends WFElementAction {

    /**
     * The Constant ELEMENT_MESSAGE_PRE_FIX.
     */
    private static final String ELEMENT_MESSAGE_PRE_FIX = "Element : ";

    /**
     * The Constant LOG_EXPRESSION_EVALUATION_RESULTED_AS.
     */
    private static final String LOG_EXPRESSION_EVALUATION_RESULTED_AS = "Expression evaluation resulted as: ";

    /**
     * The Constant LOG_EXECUTION_FAILED_FOR_ELEMENT.
     */
    private static final String LOG_EXECUTION_FAILED_FOR_ELEMENT = "Execution failed for element: ";

    /**
     * The Constant LOG_GOING_TO_EVALUTE_EXPRESSION.
     */
    private static final String LOG_GOING_TO_EVALUATE_EXPRESSION = "Going to evaluate expression: ";

    /**
     * The Constant DEFULT_EXIT_CODE.
     */
    private static final String DEFULT_EXIT_CODE = "0";

    /**
     * Auto generated serial version UID.
     */
    private static final long serialVersionUID = 4678923538199837597L;

    /**
     * The Constant FIELD_NAME_CONTAINS_TRUE_PATH_ELEMENT_IDS.
     */
    private static final String FIELD_NAME_CONTAINS_TRUE_PATH_ELEMENT_IDS = "truePath";

    /**
     * The Constant FIELD_NAME_CONTAINS_FALSE_PATH_ELEMENT_IDS.
     */
    private static final String FIELD_NAME_CONTAINS_FALSE_PATH_ELEMENT_IDS = "falsePath";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The true path elemen ids.
     */
    private List< String > truePathElementIds = new ArrayList<>();

    /**
     * The false path elemen ids.
     */
    private List< String > falsePathElemenIds = new ArrayList<>();

    /**
     * The expression.
     */
    private String expression;

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The result for a condition element.
     */
    boolean expressionResult = false;

    /**
     * The workflow execution manager impl.
     */
    private final transient WorkflowExecutionManagerImpl workflowExecutionManagerImpl;

    /**
     * The original user work flow.
     */
    private final transient UserWorkFlow originalUserWorkFlow;

    /**
     * The constructor which sets different fields of object.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public ConditionalElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl, UserWorkFlow originalUserWorkFlow ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
        this.originalUserWorkFlow = originalUserWorkFlow;
        setId( element.getId() );
    }

    /**
     * Instantiates a new conditional element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param workflowExecutionManagerImpl
     *         the workflow execution manager impl
     * @param originalUserWorkFlow
     *         the original user work flow
     * @param executedElementIds
     *         the executed element ids
     */
    public ConditionalElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl, UserWorkFlow originalUserWorkFlow,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
        this.originalUserWorkFlow = originalUserWorkFlow;
        setId( element.getId() );
    }

    /**
     * The doAction. This function is responsible for parsing user workflow element and replacing input variables for parameters and will
     * create expression.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        if ( parameters == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_PARAM ) ) );
        } else if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            evaluatePath( notif );
            job.getWorkingDir();
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        if ( notif.hasErrors() ) {
            return notif;
        }
        loggingExpressionResult( notif );
        return notif;
    }

    /**
     * Evaluate the given expression by third party spring expression language.
     *
     * @param noif
     *         the noif
     *
     * @return true, if successful
     */
    public boolean evaluateExpression( Notification noif ) {

        try {
            final SuSConditionalExpressionEvaluator ceEvaluator = new SuSConditionalExpressionEvaluator();
            return ceEvaluator.evaluate( expression );
        } catch ( final Exception e ) {
            log.error( "inValid expression: " + e.getMessage(), e );
            noif.addError( new Error( e.getMessage() ) );
        }
        return false;
    }

    /**
     * Evaluate path.
     *
     * @param notif
     *         the notif
     */
    private void evaluatePath( final Notification notif ) {
        String expressionStr;
        for ( final Field< ? > elementField : element.getFields() ) {
            if ( null != elementField.getValue() && !( "".equals( elementField.getValue() ) ) ) {
                if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() )
                        && elementField.getName().contentEquals( FIELD_NAME_CONTAINS_TRUE_PATH_ELEMENT_IDS ) ) {
                    truePathElementIds.addAll( ( List< String > ) elementField.getValue() );
                } else if ( elementField.getType().contentEquals( FieldTypes.SELECTION.getType() )
                        && elementField.getName().contentEquals( FIELD_NAME_CONTAINS_FALSE_PATH_ELEMENT_IDS ) ) {
                    falsePathElemenIds.addAll( ( List< String > ) elementField.getValue() );
                } else if ( elementField.getType().contentEquals( FieldTypes.TEXT.getType() ) ) {
                    expressionStr = elementField.getValue().toString().trim();
                    expression = expressionStr;
                    replaceAllVariableValuesInScript( notif );
                }
            }
        }
        if ( ( truePathElementIds == null ) || truePathElementIds.isEmpty() ) {
            truePathElementIds = new ArrayList<>();
        } else if ( ( falsePathElemenIds == null ) || falsePathElemenIds.isEmpty() ) {
            falsePathElemenIds = new ArrayList<>();
        }
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @return the decision object
     */
    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );
            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );
            executeConditionalElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shutdown interrupted.", e );
                writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
                Thread.currentThread().interrupt();
            }
            final DecisionObject result = new DecisionObject( expressionResult, element.getKey(), element.getName(), truePathElementIds,
                    falsePathElemenIds );
            result.setParameters( parameters );
            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();
            return result;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Function to perform a task. This will be executed if a task is going to be executed.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeConditionalElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.info( EXE_ELEM_COMPL + element.getName() );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                // update elemnt exeuted at server
                if ( !job.isFileRun() ) {
                    // job.getProgress().setTotal( recalculatedTotalElements );
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final SusException e ) {
                log.error( "Condition Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            ELEMENT_MESSAGE_PRE_FIX + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e1 );
                }
                wfLogger.error(
                        MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                                + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME,
                                element.getName() ) + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE
                                + LOG_EXECUTION_FAILED_FOR_ELEMENT + element.getName() );
                writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
                throw new SusRuntimeException( e.getMessage() );
            }
        };
        final Future< ? > future = executor.submit( task );
        executor.shutdown();
        try {
            if ( ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) || ( executionTime
                    == ConstantsInteger.ELEMENT_NOT_EXECUTE_AT_ALL ) ) {
                future.get();
            } else {
                wfLogger.info(
                        MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS, element.getName(), executionTime ) );
                // <-- wait for runtime seconds to finish
                future.get( executionTime, TimeUnit.SECONDS );
            }
        } catch ( final InterruptedException e ) {
            log.error( "job was interrupted ", e );
            Thread.currentThread().interrupt();
            writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final ExecutionException e ) {
            log.error( "caught exception: ", e );
            writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final TimeoutException e ) {
            future.cancel( true );
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ), e );
            writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ) );
        }

    }

    /**
     * Logging expression result.
     *
     * @param notif
     *         the notif
     */
    private void loggingExpressionResult( final Notification notif ) {
        wfLogger.info( LOG_GOING_TO_EVALUATE_EXPRESSION + expression );
        expressionResult = evaluateExpression( notif );
        wfLogger.info( LOG_EXPRESSION_EVALUATION_RESULTED_AS + expressionResult );
    }

    /**
     * Gets the variable values from parameters and replace them in script.
     *
     * @param notif
     *         the notif
     */
    private void replaceAllVariableValuesInScript( final Notification notif ) {
        cmd = expression;
        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( cmd ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            final List< String > variablesIncludingDotResolved = new ArrayList<>();
            for ( String splitters : variablesIncludingDot ) {
                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
            }
            computeGlobalCmdForMultipleDotKeys( new Notification(), variablesIncludingDotResolved, variablesIncludingDot );
        }
        final List< String > variables = getAllSimpleVariables( cmd );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            computeGlobalCmd( notif, variables );
        }
        expression = cmd;
        log.info( "After replacement notifies: " + notif.getErrors() );
        log.info( "going to execute script" );
    }

}
