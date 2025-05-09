package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.apache.commons.collections4.MapUtils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BmwCaeBenchCommonDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * This Class is designed to perform action on a workflow IO element. e.g validations of the inputs and outputs
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class WorkflowIOElementAction extends WFElementAction {

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * Auto generated serial version UID of class.
     */
    private static final long serialVersionUID = 768340383289113290L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The Constant SCHEME_POST.
     */
    private static final String SCHEME_POST = "{{__SCHEME__}}";

    /**
     * The constructor which sets different fields of IOElement.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public WorkflowIOElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        if ( element != null ) {
            setId( element.getId() );
            log.debug( "setId( element.getId() ): " + element.getId() );
        }
        this.parameters = parameters;
    }

    /**
     * Instantiates a new workflow IO element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param executedElementsIds
     *         the executed elements ids
     */
    public WorkflowIOElementAction( Job job, UserWFElement element, Map< String, Object > parameters, Set< String > executedElementsIds ) {
        super( job, element, executedElementsIds );
        this.job = job;
        this.element = element;
        if ( element != null ) {
            setId( element.getId() );
            log.debug( "setId( element.getId() ): " + element.getId() );
        }
        this.parameters = parameters;
    }

    /**
     * The doAction. This function is responsible for performing some action on a workflow element This function gets the fields of an
     * element and validate them. i.e, validating an input file path.
     *
     * @return the notification the work flow execution exception
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        if ( element != null ) {
            for ( final Field< ? > userWFElementField : element.getFields() ) {
                if ( userWFElementField.getValue() != null ) {
                    String fieldValue = null;
                    if ( userWFElementField.getType().contentEquals( FieldTypes.SERVER_FILE_EXPLORER.getType() )
                            && !userWFElementField.isVariableMode() ) {
                        final SusResponseDTO responseDTO = getSelectionList( job.getServer(), job.getRequestHeaders(), userWFElementField );
                        if ( null != responseDTO ) {
                            FilteredResponse< Object > fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ),
                                    FilteredResponse.class );
                            if ( !fr.getData().isEmpty() ) {
                                fieldValue = prepareSSFSFieldValueFromSelectionResponse( fr );
                            } else {
                                continue;
                            }
                        }
                    } else if ( userWFElementField.getType().contentEquals( FieldTypes.COLOR.getType() ) ) {
                        String allValues = getArgValueFromJson( userWFElementField.getValue() ).toString();
                        String rgbValues = allValues.split( "[\\(\\)]" )[ ConstantsInteger.INTEGER_VALUE_ONE ];
                        int index = rgbValues.lastIndexOf( "," );
                        fieldValue = "(" + rgbValues.substring( ConstantsInteger.INTEGER_VALUE_ZERO, index ) + ")";
                    } else if ( userWFElementField.getType().contentEquals( FieldTypes.INPUT_TABLE.getType() ) ) {
                        fieldValue = JsonUtils.toJson( userWFElementField.getValue() );
                        if ( fieldValue.startsWith( "\"" ) ) {
                            fieldValue = userWFElementField.getValue().toString();
                        }

                    } else if ( userWFElementField.getValue().toString().contains( SCHEME_POST ) ) {

                        if ( userWFElementField.getValue().toString().equals( SCHEME_POST ) ) {
                            fieldValue = JsonUtils.toJson( parameters.get( SCHEME_POST ) );
                        } else {
                            try {
                                String argKey = userWFElementField.getValue().toString();
                                String item = argKey.substring( argKey.indexOf( "}}." ) + 3 );
                                JSONValue.COMPRESSION = JSONStyle.LT_COMPRESS;
                                Object object = JsonPath.read( Configuration.defaultConfiguration().jsonProvider()
                                        .parse( JsonUtils.toJson( parameters.get( SCHEME_POST ) ) ), ConstantsString.DOLLAR + "." + item );
                                fieldValue = object.toString();
                            } catch ( final SusException e ) {
                                log.error( e.getLocalizedMessage(), e );
                            }
                        }

                    } else if ( userWFElementField.getType().equals( FieldTypes.CB2_OBJECTS.getType() )
                            && userWFElementField.getValue() != null
                            && !userWFElementField.getValue().equals( ConstantsString.EMPTY_STRING ) ) {
                        String url = prepareURL( "/api/workflow/cb2/entity/selection/" + userWFElementField.getValue(), job.getServer() );
                        SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
                        if ( null != susResponse ) {
                            fieldValue = prepareCB2FieldValueFromSelectionResponse( susResponse.getData() );
                        } else {
                            continue;
                        }
                    } else if ( userWFElementField.getType().contentEquals( FieldTypes.OBJECT_PARSER.getType() )
                            && userWFElementField.getTemplateType().contentEquals( FieldTemplates.CUSTOM_VARIABLE.getValue() ) ) {
                        fieldValue = getArgValueFromJson( userWFElementField.getValue() ).toString();
                    } else {
                        fieldValue = getArgValueFromJson( userWFElementField.getValue() ).toString();
                    }
                    final String actualValue =
                            ( userWFElementField.getType().contentEquals( FieldTypes.TEXT.getType() ) || userWFElementField.getType()
                                    .contentEquals( FieldTypes.TEXTAREA.getType() ) ) && !userWFElementField.isVariableMode()
                                    ? fieldValue
                                    : replaceVariablesWithValuesInFields( notif, fieldValue );
                    parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + ConstantsString.DOT
                            + userWFElementField.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, actualValue );
                }
                notif.addNotification( userWFElementField.validateField() );
            }
            notif.addNotification( element.validateException() );
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        return notif;
    }

    /**
     * Prepare SSFS field value from selection response.
     *
     * @param fr
     *         the filtered response
     *
     * @return the string
     */
    private static String prepareSSFSFieldValueFromSelectionResponse( FilteredResponse< Object > fr ) {
        String fieldValue;
        List< AdditionalFiles > additionalFiles = JsonUtils.jsonToList( JsonUtils.toJson( fr.getData() ), AdditionalFiles.class );
        List< Object > fieldDetailsList = new ArrayList<>();
        for ( AdditionalFiles additionalFile : additionalFiles ) {
            Map< String, Object > fieldProperties = new HashMap<>();
            fieldProperties.put( "name", additionalFile.getTitle() );
            fieldProperties.put( "path", new File( additionalFile.getFullPath() ).getParent() );
            fieldProperties.put( "fullPath", additionalFile.getFullPath() );
            fieldProperties.put( "type", additionalFile.getType() );
            fieldProperties.put( "size", additionalFile.getSize() );
            fieldProperties.put( "location", additionalFile.getLocation().getName() );
            if ( MapUtils.isNotEmpty( additionalFile.getCustomAttributes() ) ) {
                fieldProperties.putAll( additionalFile.getCustomAttributes() );
            }
            fieldDetailsList.add( fieldProperties );
        }
        fieldValue = JsonUtils.toJson( fieldDetailsList );
        return fieldValue;
    }

    /**
     * Prepare CB 2 field value from selection response.
     *
     * @param responseData
     *         the response data
     *
     * @return the string
     */
    private static String prepareCB2FieldValueFromSelectionResponse( Object responseData ) {
        String fieldValue;
        List< BmwCaeBenchCommonDTO > benchDTOList = JsonUtils.jsonToList( JsonUtils.toJson( responseData ), BmwCaeBenchCommonDTO.class );
        List< Object > fieldDetailsList = new ArrayList<>();
        for ( BmwCaeBenchCommonDTO benchDto : benchDTOList ) {
            Map< String, Object > fieldProperties = new HashMap<>();
            fieldProperties.put( "name", benchDto.getName() );
            fieldProperties.put( "oid", benchDto.getOid() );
            fieldProperties.put( "createdAt", benchDto.getCreatedAt() );
            fieldProperties.put( "description", benchDto.getDescription() );
            fieldProperties.put( "owner", benchDto.getOwner() );
            fieldProperties.put( "createdBy", benchDto.getCreatedBy() );
            fieldDetailsList.add( fieldProperties );
        }
        fieldValue = JsonUtils.toJson( fieldDetailsList );
        return fieldValue;
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
            executeIOElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shutdown interrupted.", e );
                Thread.currentThread().interrupt();
            }
            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();
            return new DecisionObject( true, element.getKey(), parameters );
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
    private void executeIOElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                wfLogger.info( EXE_ELEMENT + element.getName() );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.success( EXE_ELEM_COMPL + element.getName() );

                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                // update elemnt exeuted at server
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( "IO Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getMessage(), e1 );
                    wfLogger.error( e1.getLocalizedMessage() );
                }
                throw new SusRuntimeException( e.getLocalizedMessage() );
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
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final ExecutionException e ) {
            log.error( "caught exception: ", e );
            throw new SusRuntimeException( e.getMessage() );
        } catch ( final TimeoutException e ) {
            future.cancel( true );
            log.error( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ), e );
            throw new SusRuntimeException( MessagesUtil.getMessage( WFEMessages.EXECUTION_TIMEOUT, element.getName() ) );
        }
    }

    /**
     * Gets the arg value from json.
     *
     * @param object
     *         the object
     *
     * @return the arg value from json
     */
    protected Object getArgValueFromJson( Object object ) {
        Object argObject;
        try {
            if ( object instanceof EngineFile engineFile ) {
                argObject = engineFile.getPath();
            } else if ( object instanceof LinkedHashMap ) {
                argObject = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) object ),
                        de.soco.software.simuspace.workflow.model.impl.EngineFile.class ).getPath();
            } else {
                argObject = object;
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        }
        return argObject;
    }

    /**
     * Replace variables with values in fields.
     *
     * @param notif
     *         the notif
     * @param value
     *         the value
     *
     * @return the string
     */
    private String replaceVariablesWithValuesInFields( final Notification notif, String value ) {
        String replacedValue = value;
        if ( value.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES )
                && !value.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
            cmd = value;
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
            replacedValue = cmd;
        }
        return replacedValue;
    }

}
