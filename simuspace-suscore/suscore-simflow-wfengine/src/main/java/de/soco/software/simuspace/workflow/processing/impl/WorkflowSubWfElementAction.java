package de.soco.software.simuspace.workflow.processing.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serial;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.dexecutor.core.task.ExecutionResults;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.ElementConnectionImpl;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowOutput;

/**
 * The Class WorkflowSubWfElementAction.
 */
@Log4j2
public class WorkflowSubWfElementAction extends WFElementAction {

    /**
     * The Constant FIELD_SUBWORKFLOW_NAME.
     */
    private static final String FIELD_SUBWORKFLOW_NAME = "subworkflow";

    /**
     * The main map.
     */
    private final Map< String, Map< String, Object > > mainMap = new HashMap<>();

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * This is the element information coming from designer containing fields having information what to do with that element.
     */
    private final transient UserWFElement element;

    /**
     * The rest API server credentials.
     */
    private transient RestAPI restAPI;

    /**
     * The python execution path.
     */
    private String pythonExecutionPath;

    /**
     * The user work flow.
     */
    private UserWorkFlow userWorkFlow;

    /**
     * The workflow execution manager impl.
     */
    private final WorkflowExecutionManagerImpl workflowExecutionManagerImpl;

    /**
     * Instantiates a new sus data object element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param userWorkFlow
     *         the user work flow
     * @param workflowExecutionManagerImpl
     *         the workflow execution manager
     */
    public WorkflowSubWfElementAction( Job job, UserWFElement element, Map< String, Object > parameters, UserWorkFlow userWorkFlow,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
        this.userWorkFlow = userWorkFlow;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
    }

    /**
     * Instantiates a new workflow sub wf element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param userWorkFlow
     *         the user work flow
     * @param workflowExecutionManagerImpl
     *         the workflow execution manager impl
     * @param restApi
     *         the rest api
     * @param executedElementIds
     *         the executed element ids
     * @param pythonExecutionPath
     *         the python execution path
     */
    public WorkflowSubWfElementAction( Job job, UserWFElement element, Map< String, Object > parameters, UserWorkFlow userWorkFlow,
            WorkflowExecutionManagerImpl workflowExecutionManagerImpl, RestAPI restApi, Set< String > executedElementIds,
            String pythonExecutionPath ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        this.restAPI = restApi;
        this.pythonExecutionPath = pythonExecutionPath;
        if ( element != null ) {
            setId( element.getId() );
        }
        this.userWorkFlow = userWorkFlow;
        this.workflowExecutionManagerImpl = workflowExecutionManagerImpl;
    }

    /**
     * Do action.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        if ( element != null ) {
            notif.addNotification( element.validateException() );
            if ( notif.hasErrors() ) {
                return notif;
            }
            prepareParameters( notif );
        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }
        log.debug( "leaving doAction()" );
        return notif;
    }

    /**
     * Prepare parameters.
     *
     * @param notif
     *         the notif
     */
    private void prepareParameters( final Notification notif ) {
        List< Object > fileList = new ArrayList<>();
        Map< String, Object > fieldMap = new HashMap<>();
        final Field< ? > elementField = element.getFields().stream().filter( field -> ( field.getName().equals( FIELD_SUBWORKFLOW_NAME ) ) )
                .findFirst().orElse( null );
        if ( elementField != null ) {
            for ( final Entry< String, ? > entry : elementField.getSubFields().entrySet() ) {
                String[] splitSubFieldKey = entry.getKey().split( "\\." );
                String subWorkflowElementName = splitSubFieldKey[ 0 ];
                String subWorkflowFieldName = splitSubFieldKey[ 1 ];
                final Field< Object > field = ( Field< Object > ) entry.getValue();
                String strValue = null;
                if ( null != field.getValue() ) {
                    strValue = field.getValue().toString();
                }

                if ( field.isVariableMode() ) {
                    if ( strValue != null && strValue.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES )
                            && !strValue.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                        fieldMap = prepareFieldMapForVariableModeAndLeadingBraces( notif, fieldMap, subWorkflowElementName,
                                subWorkflowFieldName, field, strValue );

                    } else {
                        fieldMap = prepareFieldMapForVariableMode( fieldMap, subWorkflowElementName, subWorkflowFieldName, field,
                                strValue );
                    }
                } else {
                    fieldMap = prepareFieldMapForNonVariableMode( fileList, fieldMap, subWorkflowElementName, subWorkflowFieldName, field,
                            strValue );
                }
                field.setValue( fieldMap.get( subWorkflowFieldName ) );
                JobLog.addLog( job.getId(),
                        new LogRecord( ConstantsMessageTypes.INFO, "Element : " + element.getKey() + " Field: " + field.getName() ) );
                wfLogger.info( "Element : " + element.getKey() + " Field " + field.getName() );

            }
        }
    }

    /**
     * Prepare field map for non variable mode map.
     *
     * @param fileList
     *         the file list
     * @param fieldMap
     *         the field map
     * @param subWorkflowElementName
     *         the sub workflow element name
     * @param subWorkflowFieldName
     *         the sub workflow field name
     * @param field
     *         the field
     * @param strValue
     *         the str value
     *
     * @return the map
     */
    private Map< String, Object > prepareFieldMapForNonVariableMode( List< Object > fileList, Map< String, Object > fieldMap,
            String subWorkflowElementName, String subWorkflowFieldName, Field< ? > field, String strValue ) {
        parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + "." + field.getName()
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, strValue );
        String file;
        if ( null == mainMap.get( subWorkflowElementName ) ) {
            fieldMap = prepareNewFieldMapForNonVariableMode( fileList, subWorkflowElementName, subWorkflowFieldName, field );
        } else {
            fieldMap = mainMap.get( subWorkflowElementName );
            if ( FieldTypes.FILE.getType().equals( field.getType() ) || FieldTypes.DIRECTORY.getType().equals( field.getType() ) ) {
                LinkedHashMap< String, List< String > > map = ( LinkedHashMap< String, List< String > > ) field.getValue();
                file = map.get( "items" ).get( ConstantsInteger.INTEGER_VALUE_ZERO );
                Map< String, String > properties = new HashMap<>();
                properties.put( "name", file.substring( ( file.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
                properties.put( "path", file );
                fileList.add( properties );
                fieldMap.put( subWorkflowFieldName, fileList );
            } else if ( FieldTypes.OBJECT.getType().equals( field.getType() ) && field.getValue() != null
                    && !field.getValue().equals( "" ) ) {
                FiltersDTO filter = new FiltersDTO();
                filter.setDraw( 1 );
                filter.setStart( 0 );
                filter.setLength( Integer.MAX_VALUE );
                final String selectionURL = prepareURL( "/api/selection/" + field.getValue() + "/list", job.getServer() );
                final SusResponseDTO responseDTO = SuSClient.postRequest( selectionURL, JsonUtils.objectToJson( filter ),
                        prepareHeaders( job.getRequestHeaders() ) );
                if ( responseDTO != null ) {
                    FilteredResponse< ? > fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                    fieldMap.put( subWorkflowFieldName, fr.getData() );
                }
            } else if ( FieldTypes.INPUT_TABLE.getType().equals( field.getType() ) ) {
                if ( field.getValue() != null && !field.getValue().equals( "" ) ) {

                    String fieldValue = JsonUtils.toJson( field.getValue() );
                    if ( fieldValue != null && fieldValue.startsWith( "\"" ) ) {
                        fieldMap.put( subWorkflowFieldName, field.getValue().toString() );
                    } else {
                        fieldMap.put( subWorkflowFieldName, field.getValue() );
                    }
                }
            } else if ( FieldTypes.SELECTION.getType().equals( field.getType() ) && field.getValue() != null
                    && !field.getValue().equals( "" ) ) {
                fieldMap.put( subWorkflowFieldName, field.getValue() );
            } else {
                file = ( String ) field.getValue();
                fieldMap.put( subWorkflowFieldName, file );
            }
            mainMap.put( subWorkflowElementName, fieldMap );
        }
        return fieldMap;
    }

    /**
     * Prepare new field map for non variable mode map.
     *
     * @param fileList
     *         the file list
     * @param subWorkflowElementName
     *         the sub workflow element name
     * @param subWorkflowFieldName
     *         the sub workflow field name
     * @param field
     *         the field
     *
     * @return the map
     */
    private Map< String, Object > prepareNewFieldMapForNonVariableMode( List< Object > fileList, String subWorkflowElementName,
            String subWorkflowFieldName, Field< ? > field ) {
        String file;
        Map< String, Object > fieldMap;
        fieldMap = new HashMap<>();
        if ( field.getType().equals( FieldTypes.FILE.getType() ) || field.getType().equals( FieldTypes.DIRECTORY.getType() ) ) {
            LinkedHashMap< String, List< String > > map = ( LinkedHashMap< String, List< String > > ) field.getValue();
            file = map.get( "items" ).get( ConstantsInteger.INTEGER_VALUE_ZERO );
            Map< String, String > properties = new HashMap<>();
            properties.put( "name", file.substring( ( file.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
            properties.put( "path", file );
            fileList.add( properties );
            fieldMap.put( subWorkflowFieldName, fileList );
        } else if ( field.getType().equals( FieldTypes.OBJECT.getType() ) && field.getValue() != null && !field.getValue().equals( "" ) ) {
            FiltersDTO filter = new FiltersDTO();
            filter.setDraw( 1 );
            filter.setStart( 0 );
            filter.setLength( Integer.MAX_VALUE );
            final String selectionURL = prepareURL( "/api/selection/" + field.getValue() + "/list", job.getServer() );
            final SusResponseDTO responseDTO = SuSClient.postRequest( selectionURL, JsonUtils.objectToJson( filter ),
                    prepareHeaders( job.getRequestHeaders() ) );
            if ( responseDTO != null ) {
                FilteredResponse< ? > fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                fieldMap.put( subWorkflowFieldName, fr.getData() );
            }
        } else if ( FieldTypes.INPUT_TABLE.getType().equals( field.getType() ) ) {
            if ( field.getValue() != null && !field.getValue().equals( ConstantsString.EMPTY_STRING ) ) {

                String fieldValue = JsonUtils.toJson( field.getValue() );
                if ( fieldValue != null && fieldValue.startsWith( ConstantsString.DOUBLE_QUOTE_STRING ) ) {
                    fieldMap.put( subWorkflowFieldName, field.getValue().toString() );
                } else {
                    fieldMap.put( subWorkflowFieldName, field.getValue() );
                }
            }
        } else if ( FieldTypes.SELECTION.getType().equals( field.getType() ) && field.getValue() != null
                && !field.getValue().equals( ConstantsString.EMPTY_STRING ) ) {
            fieldMap.put( subWorkflowFieldName, field.getValue() );
        } else {
            file = ( String ) field.getValue();
            fieldMap.put( subWorkflowFieldName, file );
        }
        mainMap.put( subWorkflowElementName, fieldMap );
        return fieldMap;
    }

    /**
     * Prepare field map for server files.
     *
     * @param fieldMap
     *         the field map
     * @param field
     *         the field
     * @param subWorkflowFieldName
     *         the sub workflow field name
     */
    private void prepareFieldMapForServerFiles( Map< String, Object > fieldMap, Field< ? > field, String subWorkflowFieldName ) {
        List< Object > fileList = new ArrayList<>();
        if ( field.getValue() == null || field.getValue().equals( "" ) ) {
            fieldMap.put( subWorkflowFieldName, getEmptyServerFileField() );
        } else {
            String serverFileValue = JsonUtils.toJson( field.getValue() );
            if ( serverFileValue != null && serverFileValue.startsWith( "\"" ) ) {
                fieldMap.put( subWorkflowFieldName, field.getValue().toString() );
            } else {
                try {
                    List< AdditionalFiles > additionalFilesList = JsonUtils.jsonToList( JsonUtils.toJson( field.getValue() ),
                            AdditionalFiles.class );

                    for ( AdditionalFiles additionalFiles : additionalFilesList ) {

                        Map< String, String > properties = new HashMap<>();
                        properties.put( "name", additionalFiles.getTitle() );
                        properties.put( "path", additionalFiles.getFullPath() );
                        properties.put( "attribute", ( additionalFiles.getCustomAttributes() != null
                                ? additionalFiles.getCustomAttributes().toString()
                                : " " ) );
                        fileList.add( properties );
                    }
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                    wfLogger.info( "server details write error for systemoutput file" + e.getMessage() );
                }
                fieldMap.put( subWorkflowFieldName, fileList );
            }
        }
    }

    /**
     * Prepare field map for variable mode map.
     *
     * @param fieldMap
     *         the field map
     * @param subWorkflowElementName
     *         the sub workflow element name
     * @param subWorkflowFieldName
     *         the sub workflow field name
     * @param field
     *         the field
     * @param strValue
     *         the str value
     *
     * @return the map
     */
    private Map< String, Object > prepareFieldMapForVariableMode( Map< String, Object > fieldMap, String subWorkflowElementName,
            String subWorkflowFieldName, Field< ? > field, String strValue ) {
        parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName() + "." + field.getName()
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, strValue );
        String valueSubstring = strValue.substring( ( strValue.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE );
        if ( null == mainMap.get( subWorkflowElementName ) ) {
            fieldMap = new HashMap<>();
            if ( field.getType().equals( FieldTypes.FILE.getType() ) || field.getType().equals( FieldTypes.DIRECTORY.getType() ) ) {
                Map< String, String > properties = new HashMap<>();
                properties.put( "name", valueSubstring );
                properties.put( "path", strValue );
                fieldMap.put( subWorkflowFieldName, properties );
            } else {
                fieldMap.put( subWorkflowFieldName, strValue );
            }
            mainMap.put( subWorkflowElementName, fieldMap );
        } else {
            fieldMap = mainMap.get( subWorkflowElementName );
            if ( field.getType().equals( FieldTypes.FILE.getType() ) || field.getType().equals( FieldTypes.DIRECTORY.getType() ) ) {
                Map< String, String > properties = new HashMap<>();
                properties.put( "name", valueSubstring );
                properties.put( "path", strValue );
                fieldMap.put( subWorkflowFieldName, properties );
            } else {
                fieldMap.put( subWorkflowFieldName, strValue );
            }
            mainMap.put( subWorkflowElementName, fieldMap );
        }
        return fieldMap;
    }

    /**
     * Prepare field map for variable mode and leading braces map.
     *
     * @param notif
     *         the notif
     * @param fieldMap
     *         the field map
     * @param subWorkflowElementName
     *         the sub workflow element name
     * @param subWorkflowFieldName
     *         the sub workflow field name
     * @param field
     *         the field
     * @param strValue
     *         the str value
     *
     * @return the map
     */
    private Map< String, Object > prepareFieldMapForVariableModeAndLeadingBraces( Notification notif, Map< String, Object > fieldMap,
            String subWorkflowElementName, String subWorkflowFieldName, Field< ? > field, String strValue ) {
        cmd = strValue;
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
        parameters.put( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + field.getName()
                + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, cmd );

        if ( null == mainMap.get( subWorkflowElementName ) ) {
            fieldMap = getFieldMap( new HashMap<>(), subWorkflowFieldName, field );
            mainMap.put( subWorkflowElementName, fieldMap );
        } else {
            fieldMap = mainMap.get( subWorkflowElementName );
            mainMap.put( subWorkflowElementName, getFieldMap( fieldMap, subWorkflowFieldName, field ) );
        }
        return fieldMap;
    }

    /**
     * Gets the empty server file field.
     *
     * @return the empty server file field
     */
    private Object getEmptyServerFileField() {
        List< Object > feildDetailsList = new ArrayList<>();
        Map< String, String > properties = new HashMap<>();
        properties.put( "name", "" );
        properties.put( "path", "" );
        properties.put( "attribute", "" );
        feildDetailsList.add( properties );
        return feildDetailsList;
    }

    /**
     * Gets the field map.
     *
     * @param fieldMap
     *         the field map
     * @param subWorkflowFieldName
     *         the sub workflow field name
     * @param field
     *         the field
     *
     * @return the field map
     */
    private Map< String, Object > getFieldMap( Map< String, Object > fieldMap, String subWorkflowFieldName, final Field< ? > field ) {
        if ( field.getType().equals( FieldTypes.FILE.getType() ) || field.getType().equals( FieldTypes.DIRECTORY.getType() ) ) {
            Map< String, String > properties = new HashMap<>();
            properties.put( "name", cmd.substring( ( cmd.lastIndexOf( File.separator ) ) + ConstantsInteger.INTEGER_VALUE_ONE ) );
            properties.put( "path", cmd );
            fieldMap.put( subWorkflowFieldName, properties );
        } else if ( field.getType().equals( FieldTypes.OBJECT.getType() ) && field.getValue() != null && !field.getValue().equals( "" ) ) {
            FiltersDTO filter = new FiltersDTO();
            filter.setDraw( 1 );
            filter.setStart( 0 );
            filter.setLength( Integer.MAX_VALUE );
            final String selectionURL = prepareURL( "/api/selection/" + cmd + "/list", job.getServer() );
            final SusResponseDTO responseDTO = SuSClient.postRequest( selectionURL, JsonUtils.objectToJson( filter ),
                    prepareHeaders( job.getRequestHeaders() ) );
            if ( responseDTO != null ) {
                FilteredResponse< ? > fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                fieldMap.put( subWorkflowFieldName, fr.getData() );
            }
        } else {
            fieldMap.put( subWorkflowFieldName, cmd );
        }
        return fieldMap;
    }

    /**
     * Execute decision object.
     *
     * @return the decision object
     */
    /* (non-Javadoc)
     * @see com.github.dexecutor.core.task.Task#execute()
     */
    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );
            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );
            UserWorkFlow subWorkflow = workflowExecutionManagerImpl.getSubWorkflowBySelectionId( job,
                    this.element.getFields().get( 0 ).getValue().toString() );
            prepareSubFieldsFromOriginalWorkflow( subWorkflow );
            executeWorkflowSubWfElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shutdown interrupted.", e );
                Thread.currentThread().interrupt();
            }
            addExistingOutputFileToWorkflowOutput( mainMap );
            setJobResultParameters();
            final Map< String, Object > askOnRunParametersFromSubWorkFlow = prepareAskOnRunParametersFromVariables( job,
                    prepareVariablesFromWorkflow() );
            workflowExecutionManagerImpl.replaceAskOnRunParamValues( subWorkflow, askOnRunParametersFromSubWorkFlow );

            // set variable mode if changed in subworkflow AOR fields
            updateVariableModeInSubWorkflowFields( subWorkflow, askOnRunParametersFromSubWorkFlow );

            if ( CollectionUtils.isEmpty( subWorkflow.getEdges() ) ) {
                subWorkflow.setEdges( List.of( new NodeEdge(
                        new ElementConnectionImpl( subWorkflow.getNodes().get( FIRST_INDEX ).getId(), ConstantsString.EMPTY_STRING ) ) ) );
            }
            ExecutionResults< String, DecisionObject > errors = workflowExecutionManagerImpl.runWorkflowWithErrors( job, subWorkflow,
                    new HashSet<>(), parameters, pythonExecutionPath );
            if ( errors.hasAnyResult()
                    && element.getStopOnWorkFlowOption().equals( ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR ) ) {
                SusException susException = new SusException( "SubworkFlow failed !" );
                updateJobAndStatusFailed( susException );
                throw new SusException( susException.getLocalizedMessage() );
            }
            // set subworkflow parameters
            parameters.putAll( workflowExecutionManagerImpl.setSubworkflowParams( job.getId() ) );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
            executedElementsIds.add( element.getId() );
            if ( !job.isFileRun() ) {
                JobLog.updateLogAndProgress( job, executedElementsIds.size() );
            }
            return new DecisionObject( true, element.getKey(), parameters, workflowOutput, element.getName() );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Update variable mode in sub workflow fields.
     *
     * @param subWorkflow
     *         the sub workflow
     * @param askOnRunParametersFromSubWorkFlow
     *         the ask on run parameters from sub work flow
     */
    private void updateVariableModeInSubWorkflowFields( UserWorkFlow subWorkflow,
            Map< String, Object > askOnRunParametersFromSubWorkFlow ) {
        element.getFields().forEach( elementField -> elementField.getSubFields().values().stream().filter( Field::isVariableMode )
                .forEach( subWfField -> subWorkflow.getNodes().stream().flatMap( node -> node.getFields().stream().filter( field -> {
                    String param = node.getName() + ConstantsString.DOT + field.getName();
                    return askOnRunParametersFromSubWorkFlow.containsKey( param ) && subWfField.getLabel().equals( param );
                } ).peek( field -> field.setVariableMode( true ) ) ).findFirst() ) );

    }

    /**
     * Prepare ask on run parameters from variables map.
     *
     * @param job
     *         the job
     * @param resolvedVariables
     *         the resolved variables
     *
     * @return the map
     */
    private Map< String, Object > prepareAskOnRunParametersFromVariables( Job job, Map< String, Object > resolvedVariables ) {
        final Map< String, Object > askOnRunParameters = new LinkedHashMap<>();
        userWorkFlow.getNodes().stream().filter( userWFElement -> userWFElement.getName().equals( this.element.getName() ) )
                .forEach( userWFElement -> userWFElement.getFields().stream().flatMap( field -> field.getSubFields().values().stream() )
                        .filter( subWfField -> !subWfField.getType().equalsIgnoreCase( FieldTypes.SECTION.getType() ) )
                        .forEach( subWfField -> {
                            if ( ( subWfField.getValue() != null ) && ( !subWfField.getRules().isRequired()
                                    || StringUtils.isNotBlank( subWfField.getValue().toString() ) ) ) {
                                var ioParam = resolvedVariables.get( subWfField.getValue().toString() );
                                askOnRunParameters.put( subWfField.getName(), ioParam == null ? subWfField.getValue() : ioParam );
                            } else if ( !subWfField.isChangeOnRun() && subWfField.getRules().isRequired() && ( subWfField.getValue() == null
                                    || ConstantsString.EMPTY_STRING.equals( subWfField.getValue().toString() ) ) ) {
                                // fail the job if a field was not in the element(i.e not ask on run) but it is required in th original
                                // workflow and is missing
                                failJobOnError( job, MessageBundleFactory.getDefaultMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(),
                                        subWfField.getName() ) );
                            }
                        } ) );
        return askOnRunParameters;
    }

    /**
     * Prepare variables from workflow map.
     *
     * @return the map
     */
    private Map< String, Object > prepareVariablesFromWorkflow() {
        final Map< String, Object > resolvedVariables = new LinkedHashMap<>();
        userWorkFlow.getNodes().forEach( userWFElement -> userWFElement.getFields().forEach( field -> {
            String paramKey = ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + userWFElement.getName() + ConstantsString.DOT
                    + field.getName() + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES;
            resolvedVariables.put( paramKey, field.isChangeOnRun() ? field.getValue() : parameters.get( paramKey ) );
        } ) );
        return resolvedVariables;
    }

    /**
     * Fail job on error.
     *
     * @param job
     *         the job
     * @param logMessage
     *         the log message
     */
    private void failJobOnError( Job job, String logMessage ) {
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        if ( !job.isFileRun() ) {
            updateJob( job );
        } else {
            job.setCompletionTime( new Date() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, workflowExecutionManagerImpl.getExecutionTime( job ) ) ) );
        }
        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, logMessage ) );
        throw new SusException( logMessage );
    }

    /**
     * Prepare sub fields from original workflow.
     *
     * @param subWorkflow
     *         the sub workflow
     */
    private void prepareSubFieldsFromOriginalWorkflow( UserWorkFlow subWorkflow ) {
        Field< ? > subWFField = element.getFields().stream().filter( field -> field.getType().equals( FieldTypes.SUBWORKFLOW.getType() ) )
                .findFirst().orElse( null );
        if ( subWFField != null ) {
            Map< String, Field< ? > > updatedSubFieldMap = new HashMap<>();
            subWorkflow.getNodes().stream().filter( node -> ( node.getKey().equals( ElementKeys.IO.getKey() ) ) )
                    .forEach( node -> node.getFields()
                            .forEach( field -> updatedSubFieldMap.put( node.getName() + ConstantsString.DOT + field.getName(), field ) ) );
            for ( Entry< String, Field< ? > > subField : updatedSubFieldMap.entrySet() ) {
                subWFField.getSubFields()
                        .putIfAbsent( subField.getKey(), JsonUtils.jsonToObject( JsonUtils.toJson( subField.getValue() ), Field.class ) );
            }
        }

    }

    /**
     * Add existing output file to workflow output.
     *
     * @param outMap
     *         the out map
     */
    private void addExistingOutputFileToWorkflowOutput( Map< String, Map< String, Object > > outMap ) {
        try ( InputStream elementOutputStream = new FileInputStream( job.getElementOutput().getPath() ) ) {
            workflowOutput = JsonUtils.jsonStreamToObject( elementOutputStream, WorkflowOutput.class );
            for ( Map.Entry< String, Map< String, Object > > entry : outMap.entrySet() ) {
                workflowOutput.putIfAbsent( entry.getKey(), entry.getValue() );
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
        if ( workflowOutput != null ) {
            final String json = JsonUtils.objectToJson( workflowOutput );
            final File file = job.getElementOutput();
            try ( FileOutputStream fos = new FileOutputStream( file );
                    final Writer fileWriter = new OutputStreamWriter( fos, StandardCharsets.UTF_8 ) ) {
                fileWriter.write( json );
                fileWriter.flush();
            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
            }
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
    private void executeWorkflowSubWfElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final Exception e ) {
                log.error( "Sub-Workflow Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(),
                            new LogRecord( ConstantsMessageTypes.ERROR, "Element : " + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e );
                }
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
     * Gets the rest api.
     *
     * @return the rest api
     */
    public RestAPI getRestAPI() {
        return restAPI;
    }

    /**
     * Sets the rest api.
     *
     * @param restAPI
     *         the new rest api
     */
    public void setRestAPI( RestAPI restAPI ) {
        this.restAPI = restAPI;
    }

    /**
     * Gets sub workflow.
     *
     * @return the sub workflow
     */
    public UserWorkFlow getSubWorkflow() {
        return userWorkFlow;
    }

    /**
     * Sets sub workflow.
     *
     * @param subWorkflow
     *         the sub workflow
     */
    public void setSubWorkflow( UserWorkFlow subWorkflow ) {
        this.userWorkFlow = subWorkflow;
    }

}
