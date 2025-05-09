package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dexecutor.core.task.ExecutionResults;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dto.ForEachLoopElementDTO;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.WorkflowExecutionParameters;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.SectionWorkflow;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WFLoopsUtils;

/**
 * The Class WorkFlowLoopElementAction.
 */
@Log4j2
public class WorkFlowLoopElementAction extends WFElementAction {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1117582284107816688L;

    /**
     * The Constant LOOP_START_FIELD.
     */
    protected static final String LOOP_START_FIELD_NAME = "loop-start";

    /**
     * The Constant LOOP_END_FIELD.
     */
    protected static final String LOOP_END_FIELD_NAME = "loop-end";

    /**
     * The Constant INDEX_FIELD.
     */
    private static final String INDEX = "INDEX";

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The workflow execution manager impl.
     */
    public transient WFExecutionManager workflowExecutionManagerImpl;

    /**
     * The original user work flow.
     */
    private transient UserWorkFlow userWorkFlow;

    /**
     * The element.
     */
    private final transient UserWFElement element;

    /**
     * The python execution path.
     */
    private String pythonExecutionPath;

    /**
     * The loop name.
     */
    private String loopName;

    /**
     * The loop start element id.
     */
    private String loopStartElementId;

    /**
     * The loop end element id.
     */
    private String loopEndElementId;

    /**
     * The loop start element name.
     */
    private String loopStartElementName;

    /**
     * The loop end element name.
     */
    private String loopEndElementName;

    /**
     * The workflow execution parameters.
     */
    private WorkflowExecutionParameters workflowExecutionParameters;

    /**
     * The inner loops.
     */
    private List< WorkFlowLoopElementAction > innerLoops = new ArrayList<>();

    /**
     * The Constant WorkFlow logger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * Instantiates a new work flow loop element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     */
    public WorkFlowLoopElementAction( Job job, UserWFElement element ) {
        super( job, element );
        this.job = job;
        this.element = element;
    }

    /**
     * Instantiates a new work flow loop element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param workflowExecutionManagerImpl
     *         the workflow execution manager impl
     * @param userWorkFlow
     *         the original user work flow
     * @param pythonExecutionPath
     *         the python execution path
     */
    public WorkFlowLoopElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl, UserWorkFlow userWorkFlow, String pythonExecutionPath ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
        this.userWorkFlow = userWorkFlow;
        this.pythonExecutionPath = pythonExecutionPath;
        setId( element.getId() );
    }

    /**
     * Instantiates a new work flow loop element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param workflowExecutionManagerImpl
     *         the workflow execution manager impl
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     * @param userWorkFlow
     *         the user work flow
     * @param pythonExecutionPath
     *         the python execution path
     * @param executedElementIds
     *         the executed element ids
     */
    public WorkFlowLoopElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl, WorkflowExecutionParameters workflowExecutionParameters,
            UserWorkFlow userWorkFlow, String pythonExecutionPath, Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
        this.workflowExecutionParameters = workflowExecutionParameters;
        this.userWorkFlow = userWorkFlow;
        this.pythonExecutionPath = pythonExecutionPath;
        setId( element.getId() );
    }

    /**
     * Gets the loop name.
     *
     * @return the loop name
     */
    public String getLoopName() {
        return loopName;
    }

    /**
     * Sets the loop name.
     *
     * @param loopName
     *         the new loop name
     */
    public void setLoopName( String loopName ) {
        this.loopName = loopName;
    }

    /**
     * Gets the loop start element id.
     *
     * @return the loop start element id
     */
    public String getLoopStartElementId() {
        return loopStartElementId;
    }

    /**
     * Sets the loop start element id.
     *
     * @param loopStartElementId
     *         the new loop start element id
     */
    public void setLoopStartElementId( String loopStartElementId ) {
        this.loopStartElementId = loopStartElementId;
    }

    /**
     * Gets the loop end element id.
     *
     * @return the loop end element id
     */
    public String getLoopEndElementId() {
        return loopEndElementId;
    }

    /**
     * Sets the loop end element id.
     *
     * @param loopEndElementId
     *         the new loop end element id
     */
    public void setLoopEndElementId( String loopEndElementId ) {
        this.loopEndElementId = loopEndElementId;
    }

    /**
     * Gets the loop start element name.
     *
     * @return the loop start element name
     */
    public String getLoopStartElementName() {
        return loopStartElementName;
    }

    /**
     * Gets the loop end element name.
     *
     * @return the loop end element name
     */
    public String getLoopEndElementName() {
        return loopEndElementName;
    }

    /**
     * Gets the inner loops.
     *
     * @return the inner loops
     */
    public List< WorkFlowLoopElementAction > getInnerLoops() {
        return innerLoops;
    }

    /**
     * Sets the inner loops.
     *
     * @param innerLoops
     *         the new inner loops
     */
    public void setInnerLoops( List< WorkFlowLoopElementAction > innerLoops ) {
        this.innerLoops = innerLoops;
    }

    /**
     * Gets the loop start variable key.
     *
     * @return the loop start variable key
     */
    public String getLoopStartVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + LOOP_START_FIELD_NAME
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Gets the loop end variable key.
     *
     * @return the loop end variable key
     */
    public String getLoopEndVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + LOOP_END_FIELD_NAME
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Gets the index variable key.
     *
     * @return the index variable key
     */
    public String getIndexVariableKey() {
        return ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + getLoopName()
                + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + INDEX
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
    }

    /**
     * Sets the loop start and end element names.
     *
     * @param userWorkFlow
     *         the new loop start and end element names
     */
    public void setLoopStartAndEndElementNames( UserWorkFlow userWorkFlow ) {
        for ( UserWFElement element : userWorkFlow.getNodes() ) {
            if ( element.getId().equals( loopStartElementId ) ) {
                loopStartElementName = element.getName();
            }

            if ( element.getId().equals( loopEndElementId ) ) {
                loopEndElementName = element.getName();
            }
        }
    }

    /**
     * Execute.
     *
     * @return the decision object
     */
    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );
            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );
            executeLoopElement( executor, executionTime );
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
            return new DecisionObject( true, element.getKey(), element.getName(), parameters, loopStartElementId, loopEndElementId );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Execute loop element.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeLoopElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.info( EXE_ELEM_COMPL + element.getName() );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final SusException e ) {
                log.error( "Loop Element Execution Error in Thread: ", e );
                try {
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e1 );
                }
                writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
                throw new SusRuntimeException( e.getMessage() );
            }
        };
        final Future< ? > future = executor.submit( task );
        executor.shutdown();
        try {
            if ( ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT )
                    || ( executionTime == ConstantsInteger.ELEMENT_NOT_EXECUTE_AT_ALL ) ) {
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
     * Do action.
     *
     * @return the notification
     */
    private Notification doAction() {
        if ( element.getKey().equals( ConstantsElementKey.WFE_FOREACHLOOP ) ) {
            ForEachLoopElementDTO loopElement = new ForEachLoopElementDTO( element, userWorkFlow, job );
            loopStartElementId = loopElement.getLoopStartElementId();
            loopEndElementId = loopElement.getLoopEndElementId();
            ExecutionResults< String, DecisionObject > errors = executeForEachLoop( loopElement, workflowExecutionParameters, parameters,
                    userWorkFlow );
            if ( errors.hasAnyResult() ) {
                SusException susException = new SusException( "Loop failed : " + element.getKey() );
                updateJobAndStatusFailed( susException );
                throw new SusException( susException.getLocalizedMessage() );
            }
        }
        return new Notification();
    }

    /**
     * Execute for each loop.
     *
     * @param additionalParameters
     *         the additional parameters
     * @param originalUserWorkflow
     *         the original user workflow
     * @param isConditionExecutionViaLoop
     *         the is condition execution via loop
     * @param forEachLoop
     *         the for each loop
     * @param workflowExecutionParameters
     *         the workflow execution parameters
     * @param userWorkFlow
     *         the user work flow
     *
     * @return the execution results
     */
    private ExecutionResults< String, DecisionObject > executeForEachLoop( ForEachLoopElementDTO forEachLoop,
            WorkflowExecutionParameters workflowExecutionParameters, Map< String, Object > additionalParameters,
            UserWorkFlow userWorkFlow ) {
        ExecutionResults< String, DecisionObject > executionResults = new ExecutionResults<>();
        SectionWorkflow iterationWorkFlow = WFLoopsUtils.getSectionOfWorkflow( forEachLoop.getLoopStartElementId(),
                forEachLoop.getLoopEndElementId(), true, true, workflowExecutionParameters );
        if ( iterationWorkFlow.getNodes().stream()
                .anyMatch( loop -> workflowExecutionParameters.getLoopStartEndMap().containsKey( loop.getId() ) ) ) {
            iterationWorkFlow.setEdges( WFLoopsUtils.rearrangeRelationsForLoops( iterationWorkFlow.getEdges(), iterationWorkFlow.getNodes(),
                    workflowExecutionParameters.getLoopStartEndMap() ) );
        }
        List< Thread > concurrentThreads = new ArrayList<>();
        forEachLoop.addLoopVariablesToJob( workflowExecutionParameters.getJob() );
        int loopNumber = 1;
        if ( forEachLoop.isRunCocurrently() ) {
            JobLog.addLog( job.getId(),
                    new LogRecord( ConstantsMessageTypes.INFO, element.getName() + " is running concurrently. Logs may not be sync" ) );
        }
        List< ExecutionResults< String, DecisionObject > > resultList = new ArrayList<>();
        for ( String value : forEachLoop.getValuesList() ) {
            additionalParameters.put( forEachLoop.getValueVariableKey(), value );
            additionalParameters.put( forEachLoop.getIndexVariableKey(), loopNumber );
            if ( forEachLoop.isRunCocurrently() ) {
                executeIterationConcurrently( iterationWorkFlow, concurrentThreads, loopNumber, workflowExecutionParameters,
                        additionalParameters, userWorkFlow, resultList );
            } else {
                executionResults = workflowExecutionManagerImpl.executeSectionWorkflow( iterationWorkFlow, workflowExecutionParameters,
                        additionalParameters, userWorkFlow );
            }
            if ( executionResults.hasAnyResult() ) {
                return executionResults;
            }
            parameters.putAll( workflowExecutionManagerImpl.setSubworkflowParams( job.getId() ) );
            loopNumber++;
        }
        if ( forEachLoop.isRunCocurrently() ) {
            for ( Thread thread : concurrentThreads ) {
                try {
                    thread.join();
                } catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                }
            }
            for ( ExecutionResults< String, DecisionObject > result : resultList ) {
                if ( result.hasAnyResult()
                        && element.getStopOnWorkFlowOption().equals( ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR ) ) {
                    return result;
                }
            }
        }
        // Adding loop elements result parameters to global so other elements can use it
        workflowExecutionParameters.getJob().getGlobalVariables().putAll( workflowExecutionParameters.getJob().getResultParameters() );
        workflowExecutionParameters.getJob().getGlobalVariables().remove( "loopNumber" );
        return executionResults;
    }

    private void executeIterationConcurrently( SectionWorkflow iterationWorkFlow, List< Thread > concurrentThreads, int loopNumber,
            WorkflowExecutionParameters workflowExecutionParameters, Map< String, Object > additionalParameters, UserWorkFlow userWorkflow,
            List< ExecutionResults< String, DecisionObject > > resultList ) {
        // needs test and work
        // creating copy map for additionalParameters so changes in original do not interfere with this map
        Map< String, Object > additionalParametersCopy = new HashMap<>( additionalParameters );
        addLoopNumberToParameters( loopNumber, additionalParametersCopy );
        Thread thread = new Thread( () -> resultList.add( workflowExecutionManagerImpl.executeSectionWorkflow( iterationWorkFlow,
                workflowExecutionParameters, additionalParametersCopy, userWorkflow ) ) );
        thread.start();
        concurrentThreads.add( thread );
    }

    private void addLoopNumberToParameters( int loopNumber, Map< String, Object > additionalParametersCopy ) {
        if ( !additionalParametersCopy.containsKey( "loopNumber" ) ) {
            additionalParametersCopy.put( "loopNumber", "_Loop_" + loopNumber );
        } else {
            additionalParametersCopy.put( "loopNumber", additionalParametersCopy.get( "loopNumber" ).toString() + "_" + loopNumber );
        }
    }

    /**
     * Replaces all variable values in text.
     *
     * @param textData
     *         the text data
     * @param notif
     *         the notif
     *
     * @return the string
     */
    protected String replaceAllVariableValuesInText( String textData, Notification notif ) {
        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( textData ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            textData = replaceVariableValues( textData, variablesIncludingDot, new Notification() );
        }

        final List< String > variables = getAllSimpleVariables( textData );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            textData = replaceVariableValues( textData, variables, notif );
        }

        if ( !notif.getErrors().isEmpty() ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, loopName ) + ConstantsString.PIPE_CHARACTER
                    + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, loopName )
                    + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE
                    + MessagesUtil.getMessage( WFEMessages.ERROR_IN_SCRIPT_FORMAT, notif.getErrors() ) );

        }

        return textData;
    }

    /**
     * Replaces all variable values in text.
     *
     * @param textData
     *         the text data
     * @param variables
     *         the variables
     * @param notif
     *         the notif
     *
     * @return the string
     */
    private String replaceVariableValues( String textData, List< String > variables, Notification notif ) {
        for ( final String argKey : variables ) {
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES )
                    && !argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB )
                    && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    textData = replaceArguments( argObj, argKey, textData );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    textData = computeReserveKeyWordLIST( argKey, textData );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, getLoopName() ) ) );
                }
            } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                    localCmd = "$" + localCmd;
                } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                    localCmd = "%" + localCmd + "%";
                }
                textData = replaceArguments( localCmd, argKey, textData );
            } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                outputTransferringElements( notif, argKey, null );
                textData = cmd;
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                systemTransferringElements( notif, argKey, null );
                textData = cmd;
            }
        }
        return textData;
    }

    /**
     * Replace arguments.
     *
     * @param argObject
     *         the arg object
     * @param argKey
     *         the arg key
     * @param textData
     *         the text data
     *
     * @return the string
     */
    protected String replaceArguments( Object argObject, String argKey, String textData ) {
        if ( argObject != null ) {
            String value = argObject.toString();
            if ( value.contains( ConstantsString.DELIMETER_FOR_WIN ) ) {
                value = value.replace( ConstantsString.DELIMETER_FOR_WIN, ConstantsString.PATH_SEPARATOR_WIN );
            }
            textData = textData.replace( argKey, value );
        } else {
            textData = textData.replace( argKey, ConstantsString.EMPTY_STRING );
        }
        return textData;
    }

    /**
     * Compute reserve key word LIST.
     *
     * @param argKey
     *         the arg key
     * @param textData
     *         the text data
     *
     * @return the string
     */
    private String computeReserveKeyWordLIST( final String argKey, String textData ) {
        String fieldz = argKey.replace( RESERVE_KEY_LIST, "" );
        String replaceField = fieldz;
        String[] splitCMD = fieldz.split( "}}" );
        String IdValue = splitCMD[ 0 ];
        IdValue = IdValue + "}}";
        fieldz = fieldz.replace( IdValue, "" );
        if ( parameters.containsKey( IdValue ) ) {
            Object fId = parameters.get( IdValue );
            fieldz = fieldz.replace( replaceField, "" );
            String json = getChildObjectsFromServer( fId );
            if ( !fieldz.isEmpty() ) {
                List< Object > listObject = JsonUtils.jsonToList( json, Object.class );
                Map< String, Object > listMap = new HashMap<>();
                listMap.put( "objects", listObject );
                try ( ByteArrayInputStream bis = new ByteArrayInputStream( JsonUtils.toJson( listMap ).getBytes() ) ) {
                    JsonNode jsonObject = JsonUtils.convertInputStreamToJsonNode( bis );
                    Object object = JsonPath.read( Configuration.defaultConfiguration().jsonProvider().parse( jsonObject.toString() ),
                            ( ConstantsString.DOLLAR + fieldz ).trim() );
                    textData = textData.replace( argKey, object.toString() );
                } catch ( Exception ignored ) {
                }
            } else {
                textData = textData.replace( argKey, json );
            }

        }

        return textData;
    }

    /**
     * Gets the child objects from server.
     *
     * @param fId
     *         the f id
     *
     * @return the child objects from server
     */
    private String getChildObjectsFromServer( Object fId ) {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/data/project/container/childs/" + fId;
        return JsonUtils.objectToJson( SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) ).getData() );
    }

    /**
     * Gets the workflow execution parameters.
     *
     * @return the workflow execution parameters
     */
    public WorkflowExecutionParameters getWorkflowExecutionParameters() {
        return workflowExecutionParameters;
    }

    /**
     * Sets the workflow execution parameters.
     *
     * @param workflowExecutionParameters
     *         the new workflow execution parameters
     */
    public void setWorkflowExecutionParameters( WorkflowExecutionParameters workflowExecutionParameters ) {
        this.workflowExecutionParameters = workflowExecutionParameters;
    }

}
