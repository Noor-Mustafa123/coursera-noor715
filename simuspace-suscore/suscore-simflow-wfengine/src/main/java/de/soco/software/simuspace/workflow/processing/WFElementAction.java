package de.soco.software.simuspace.workflow.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dexecutor.core.task.ExecutionResult;
import com.github.dexecutor.core.task.ExecutionResults;
import com.github.dexecutor.core.task.Task;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE.StringConst;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.main.WFApplication;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;
import de.soco.software.simuspace.workflow.util.ElementOutput;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.simuspace.workflow.util.WorkflowOutput;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

/**
 * This interface provides methods which perform actions(validate, execute) on different workflow elements.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public abstract class WFElementAction extends Task< String, DecisionObject > {

    /**
     * The Constant FIRST_INDEX.
     */
    protected static final int FIRST_INDEX = 0;

    /**
     * Auto generated serial version uid of class.
     */
    private static final long serialVersionUID = -4115084935625783278L;

    /**
     * The Constant ELEMENT_MESSAGE_PRE_FIX.
     */
    private static final String ELEMENT_MESSAGE_PRE_FIX = "Element : ";

    /**
     * The constant for message Executing Element Completed.
     */
    public static final String EXE_ELEM_COMPL = "Element execution completed: ";

    /**
     * The constant for message Element submitted for Execution.
     */
    public static final String EXE_ELEMENT = "Element execution started: ";

    /**
     * The constant for message field parameters.
     */
    public static final String FIELD_PARAM = " field parameters :";

    /**
     * This constant is for message. i.e with name.
     */
    public static final String WITH_NAME = " with name : ";

    /**
     * The constant WORKFLOW_PROPERTIES_KEY for variable_key Workflow_properties
     */
    protected static final String WORKFLOW_PROPERTIES_KEY = "{{Workflow_Properties}}";

    /**
     * The Constant DO_NOT_EXECUTE_THIS_ELEMENT.
     */
    protected static final boolean DO_NOT_EXECUTE_THIS_ELEMENT = false;

    /**
     * The Constant EXECUTE_THIS_ELEMENT.
     */
    protected static final boolean EXECUTE_THIS_ELEMENT = true;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant USER_OUTPUT_TAB.
     */
    protected static final String USER_OUTPUT_TAB = "useroutput";

    /**
     * The Constant SYSTEM_OUTPUT_TAB.
     */
    protected static final String SYSTEM_OUTPUT_TAB = "systemoutput";

    /**
     * The Constant EXIT_CODE_WITH_ERROR.
     */
    protected static final String EXIT_CODE_WITH_ERROR = "-1";

    /**
     * The Constant DEFULT_EXIT_CODE.
     */
    protected static final String DEFULT_EXIT_CODE = "0";

    /**
     * The url key for update job by id.
     */
    private static final String URL_UPDATE_JOB_BY_ID = "URL_UPDATE_JOB_BY_ID";

    /**
     * The properties object to load configuration.
     */
    private static Properties properties = new Properties();

    /**
     * The Constant USER_AGENT.
     */
    private static final String USER_AGENT = "USER_AGENT";

    /**
     * The Constant LOG_MESSAGE_ACTIVATION_ENCODE.
     */
    private static final String LOG_MESSAGE_ACTIVATION_ENCODE = "Activation encode ";

    /**
     * The Constant API_CURRENT_USER.
     */
    private static final String API_CURRENT_USER = "/api/system/user/current";

    /**
     * The executed elements ids.
     */
    protected transient Set< String > executedElementsIds;

    /**
     * The Constant ERROR.
     */
    private static final String ERROR = "error";

    /**
     * The Constant OUTPUT.
     */
    private static final String OUTPUT = "output";

    /**
     * The parent element output.
     */
    protected final transient ElementOutput parentElementOutput = new ElementOutput();

    /**
     * The workflow output.
     */
    protected WorkflowOutput workflowOutput = new WorkflowOutput();

    /**
     * The element.
     */
    private transient UserWFElement element;

    /**
     * The job.
     */
    protected transient Job job;

    /**
     * The cmd containing script to run on any operating system.
     */
    protected String cmd;

    /**
     * The parent element name.
     */
    protected String parentElementName;

    /**
     * The incoming and outgoing parameters of work flow elements.
     */
    protected transient Map< String, Object > parameters;

    /**
     * Gets the all simple variables e.g {{element}}.
     *
     * @param script
     *         the script
     *
     * @return the all simple variables
     */
    protected static List< String > getAllSimpleVariables( String script ) {
        final List< String > variablesUserInCmd = new ArrayList<>();
        final Pattern pattern = Pattern.compile( "(\\{\\{.*?\\}\\})" );
        final Matcher matcher = pattern.matcher( script );
        while ( matcher.find() ) {
            variablesUserInCmd.add( matcher.group() );
        }
        return variablesUserInCmd;
    }

    /**
     * Gets the all simple variables including dot.
     *
     * @param script
     *         the script
     *
     * @return the all simple variables including dot
     */
    protected static List< String > getAllSimpleVariablesIncludingDot( String script ) {
        final List< String > variablesUsedInCmd = new ArrayList<>();
        final Pattern pattern = Pattern.compile(
                "(\\{\\{([^\\{\\{\\}\\}]*)\\}\\}\\.(\\{\\{([^\\{\\{\\}\\}]*)\\}\\})?(\\.\\w)?[^\\{)\\s,'\\\"]*)" );
        final Matcher matcher = pattern.matcher( script );
        while ( matcher.find() ) {
            variablesUsedInCmd.add( matcher.group() );
        }
        return variablesUsedInCmd;
    }

    /**
     * Instantiates a new WF element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     */
    public WFElementAction( Job job, UserWFElement element ) {
        this.job = job;
        this.element = element;
    }

    /**
     * Instantiates a new WF element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param executedElementsIds
     *         the executed elements ids
     */
    public WFElementAction( Job job, UserWFElement element, Set< String > executedElementsIds ) {
        this.job = job;
        this.element = element;
        this.executedElementsIds = executedElementsIds;
    }

    /**
     * Function responsible to take decision either a task should execute or not.
     *
     * @param parentResults
     *         the parent results
     *
     * @return true, if successful
     */
    @Override
    public boolean shouldExecute( ExecutionResults< String, DecisionObject > parentResults ) {
        boolean execute = shouldExecuteFromParent( parentResults );
        if ( !execute ) {
            executedElementsIds.add( element.getId() );
        }
        return execute;
    }

    /**
     * Should execute from parent.
     *
     * @param parentResults
     *         the parent results
     *
     * @return true, if successful
     */
    protected boolean shouldExecuteFromParent( ExecutionResults< String, DecisionObject > parentResults ) {
        final List< ExecutionResult< String, DecisionObject > > allImediateParents = parentResults.getAll();
        boolean result = false;
        if ( allImediateParents.isEmpty() ) {
            workflowOutput = new WorkflowOutput();
            log.debug( element.getName() + ": Executing as first element." );
            return EXECUTE_THIS_ELEMENT;
        }
        for ( final ExecutionResult< String, DecisionObject > executionResult : allImediateParents ) {
            if ( null != executionResult.getResult() ) {
                parameters.putAll( executionResult.getResult().getParameters() );
                final WorkflowOutput output = executionResult.getResult().getWorkflowOutput();
                if ( output == null ) {
                    try ( InputStream targetConfigStream = new FileInputStream( job.getElementOutput() ) ) {
                        workflowOutput = JsonUtils.jsonStreamToObject( targetConfigStream, WorkflowOutput.class );
                    } catch ( final IOException e ) {
                        log.error( e.getMessage(), e );
                    }
                } else {
                    workflowOutput = output;
                }
            }
            if ( executeDecisionObjectAndReturn( executionResult ) ) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Execute decision object and return.
     *
     * @param first
     *         the first
     *
     * @return true, if successful
     */
    private boolean executeDecisionObjectAndReturn( final ExecutionResult< String, DecisionObject > first ) {
        final DecisionObject decisionObjectResult = first.getResult();
        if ( decisionObjectResult == null ) {
            return DO_NOT_EXECUTE_THIS_ELEMENT;
        }
        if ( first.isErrored() ) {
            wfLogger.info( element.getName() + " is skipped because its parent not executed successfully." );
            return DO_NOT_EXECUTE_THIS_ELEMENT;
        }
        if ( decisionObjectResult.getElementType().contentEquals( ConstantsElementKey.WFE_CONDITIONAL ) ) {
            if ( ( decisionObjectResult.isResult() && decisionObjectResult.getTruePathElementIds().contains( getId() ) )
                    || ( !decisionObjectResult.isResult() && decisionObjectResult.getFalsePathElementIds().contains( getId() ) ) ) {
                return EXECUTE_THIS_ELEMENT;
            } else {
                return DO_NOT_EXECUTE_THIS_ELEMENT;
            }
        } else if ( ( !decisionObjectResult.getElementType().contentEquals( ConstantsElementKey.WFE_FOREACHLOOP )
                && !decisionObjectResult.isResult() )
                || ( decisionObjectResult.getElementType().contentEquals( ConstantsElementKey.WFE_FOREACHLOOP )
                && decisionObjectResult.isResult() ) && decisionObjectResult.getLoopStartElementId().equals( getId() ) ) {
            return DO_NOT_EXECUTE_THIS_ELEMENT;
        } else {
            return !first.isSkipped();
        }
    }

    /**
     * Add log for unlimited execution.
     *
     * @param executionTime
     *         the execution time
     */
    protected void addLogForUnlimitedExecution( int executionTime ) {
        if ( executionTime == ConstantsInteger.UNLIMITED_TIME_FOR_ELEMENT ) {
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT, element.getName() ) );
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                        MessagesUtil.getMessage( WFEMessages.ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT, element.getName() ) ) );
            } catch ( final SusException e ) {
                log.error( e.getLocalizedMessage(), e );
            }
        }
    }

    /**
     * Gets the server file.
     *
     * @param elementField
     *         the element field
     *
     * @return the server file
     */
    protected String getServerFile( final Field< ? > elementField ) {
        if ( FieldTypes.SERVER_FILE_EXPLORER.getType().equalsIgnoreCase( elementField.getType() ) ) {
            final SusResponseDTO responseDTO = getSelectionList( job.getServer(), job.getRequestHeaders(), elementField );
            if ( responseDTO != null ) {
                FilteredResponse fr = JsonUtils.jsonToObject( JsonUtils.toJson( responseDTO.getData() ), FilteredResponse.class );
                List< AdditionalFiles > additionalFiles = JsonUtils.jsonToList( JsonUtils.toJson( fr.getData() ), AdditionalFiles.class );
                return additionalFiles.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getFullPath();
            }
            return ConstantsString.EMPTY_STRING;
        } else {
            List< AdditionalFiles > additionalFiles = JsonUtils.jsonToList( JsonUtils.toJson( elementField.getValue() ),
                    AdditionalFiles.class );
            return additionalFiles.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getFullPath();
        }
    }

    /**
     * Gets the selection list.
     *
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     * @param field
     *         the field
     *
     * @return the selection list
     */
    protected SusResponseDTO getSelectionList( RestAPI server, RequestHeaders requestHeaders, Field field ) {
        FiltersDTO filter = new FiltersDTO();
        filter.setDraw( 1 );
        filter.setStart( 0 );
        filter.setLength( Integer.MAX_VALUE );
        final String selectionURL = prepareURL( "/api/selection/" + field.getValue() + "/list", server );
        return SuSClient.postRequest( selectionURL, JsonUtils.objectToJson( filter ), prepareHeaders( requestHeaders ) );
    }

    /**
     * Gets the server multi select files.
     *
     * @param elementField
     *         the element field
     *
     * @return the server multi select files
     */
    protected List< AdditionalFiles > getServerMultiSelectFiles( final Field< ? > elementField ) {
        return JsonUtils.jsonToList( JsonUtils.toJson( elementField.getValue() ), AdditionalFiles.class );
    }

    /**
     * Compute cmd.
     *
     * @param notif
     *         the notif
     * @param variablesUsedInCmd
     *         the variables used in cmd
     */
    protected void computeGlobalCmd( final Notification notif, final List< String > variablesUsedInCmd ) {
        try {
            for ( final String argKey : variablesUsedInCmd ) {
                if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                        ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains(
                        SYSTEM_OUTPUT_TAB ) ) {
                    if ( parameters.containsKey( argKey ) ) {
                        final Object argObj = getArgValueFromJson( parameters, argKey );
                        notif.addNotification( replaceArguments( argObj, argKey ) );
                    } else if ( argKey.contentEquals( WORKFLOW_PROPERTIES_KEY ) ) {
                        final Object argObj = getWorkflowPropertiesFromParameters();
                        notif.addNotification( replaceArguments( argObj, argKey ) );
                    } else {
                        notif.addError( new Error(
                                MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                    }
                } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                    String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                    localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                    if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                        localCmd = "$" + localCmd;
                    } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                        localCmd = "%" + localCmd + "%";
                    }
                    notif.addNotification( replaceArguments( localCmd, argKey ) );
                } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                    outputTransferringElements( notif, argKey, null );
                } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                    systemTransferringElements( notif, argKey, null );
                }
            }
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Resolves {{Workflow_properties}} variable
     *
     * @return workflowProperties json String of workflow properties
     */
    protected String getWorkflowPropertiesFromParameters() {
        List< String > keys = new ArrayList<>( Arrays.asList( "CB2", "Exp", "Job", "User" ) );
        Map< String, Object > workflowProperties = new LinkedHashMap<>();
        for ( String key : keys ) {
            Object varProperty = getPropertyFromParameters( getKeysFromPropertyKeys( key ), key );
            if ( varProperty != null ) {
                workflowProperties.put( key, varProperty );
            }
        }
        return workflowProperties.isEmpty()
                ? ConstantsString.EMPTY_STRING
                : JsonUtils.convertMapToStringGernericValue( workflowProperties );
    }

    /**
     * Get workflow sub properties againsta a property ky
     *
     * @param propertyKey
     *         property to get sub-properties of
     *
     * @return properties list of matching variables in parameters
     */
    private List< String > getKeysFromPropertyKeys( String propertyKey ) {
        return switch ( propertyKey ) {
            case "CB2" -> new ArrayList<>( List.of( "Server" ) );
            case "Exp" -> new ArrayList<>( List.of( "No" ) );
            case "Job" -> new ArrayList<>( Arrays.asList( "Description", "ExecOn", "ID", "Name", "Token", "WorkingDir" ) );
            case "User" -> new ArrayList<>( Arrays.asList( "Email", "Name", "UID" ) );
            default -> null;
        };
    }

    /**
     * Get Workflow properties from parameters
     *
     * @param keys
     *         list of sub property keys
     * @param propertyKey
     *         the property key
     *
     * @return properties list of matching variables in parameters
     */
    private Object getPropertyFromParameters( List< String > keys, String propertyKey ) {
        Map< String, Object > propertiesMap = new LinkedHashMap<>();
        for ( String key : keys ) {
            String keyVariable = "{{" + propertyKey + "." + key + "}}";
            if ( parameters.containsKey( keyVariable ) ) {
                Object varValue = getArgValueFromJson( parameters, keyVariable );
                propertiesMap.put( key, varValue == null ? ConstantsString.EMPTY_STRING : varValue );
            } else {
                propertiesMap.put( key, ConstantsString.EMPTY_STRING );
            }
        }
        return propertiesMap.isEmpty() ? null : propertiesMap;
    }

    /**
     * Compute global cmd for multiple dot keys.
     *
     * @param notif
     *         the notif
     * @param variablesUserInCmd
     *         the variables user in cmd
     */
    protected void computeGlobalCmdForMultipleDotKeys( final Notification notif, final List< String > variablesIncludingDotResolved,
            final List< String > variablesUserInCmd ) {
        try {
            int keyIndex = ConstantsInteger.INTEGER_VALUE_ZERO;
            for ( final String argKey : variablesIncludingDotResolved ) {
                if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                        ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains(
                        SYSTEM_OUTPUT_TAB ) ) {
                    if ( parameters.containsKey( argKey ) ) {
                        final Object argObj = getArgValueFromJson( parameters, argKey );
                        notif.addNotification( replaceArguments( argObj, argKey ) );
                    } else if ( argKey.contains( "}}." ) ) {
                        final String basicJsonKeyword = argKey.substring( FIRST_INDEX, argKey.indexOf( "}}." ) + 2 );
                        if ( parameters.containsKey( basicJsonKeyword ) ) {
                            try {
                                final Object mainJsonObject = getArgValueFromJson( parameters, basicJsonKeyword );
                                String item = argKey.substring( argKey.indexOf( "}}." ) + 3 );
                                JSONValue.COMPRESSION = JSONStyle.LT_COMPRESS;
                                Object object = JsonPath.read(
                                        Configuration.defaultConfiguration().jsonProvider().parse( mainJsonObject.toString() ),
                                        ConstantsString.DOLLAR + ConstantsString.DOT + item );
                                notif.addNotification( replaceArguments( object, argKey ) );
                            } catch ( final SusException e ) {
                                log.error( e.getLocalizedMessage(), e );
                            }
                        }
                    } else {
                        notif.addError( new Error(
                                MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
                    }
                } else if ( argKey.startsWith( ConstantsString.SYS_CMD_INDICATION ) ) {
                    String localCmd = argKey.replace( ConstantsString.SYS_CMD_INDICATION, ConstantsString.EMPTY_STRING );
                    localCmd = localCmd.replace( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
                    if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                        localCmd = "$" + localCmd;
                    } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                        localCmd = "%" + localCmd + "%";
                    }
                    notif.addNotification( replaceArguments( localCmd, argKey ) );
                } else if ( argKey.contains( USER_OUTPUT_TAB ) ) {
                    outputTransferringElements( notif, argKey, variablesUserInCmd.get( keyIndex ) );
                } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                    systemTransferringElements( notif, argKey, variablesUserInCmd.get( keyIndex ) );
                }
                keyIndex++;
            }
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Replace after dot variables for support of json extract.
     *
     * @param splitX
     *         the split X
     *
     * @return the string
     */
    protected String replaceAfterDotVariablesForSupportOfJsonExtract( String splitX ) {
        String[] splitList = splitX.split( "}}." );
        for ( int i = 1; i < splitList.length; i++ ) {
            String splitted = splitList[ i ];
            if ( splitted.contains( "{{" ) ) {
                splitted = splitted + "}}";
                final Object argObj = getArgValueFromJson( parameters, splitted );
                splitX = splitX.replace( splitted, argObj.toString() );
            }
        }
        return splitX;

    }

    /**
     * Gets the all variables from script.
     *
     * @param script
     *         the script
     *
     * @return the all variables from script
     */
    protected List< String > getAllVariablesFromScript( String script ) {
        final List< String > variablesUserInCmd = new ArrayList<>();
        final Pattern pattern = Pattern.compile( "(\\{\\{.*?\\}\\}[^\\{\\}\\}/\\s),']*)" );
        final Matcher matcher = pattern.matcher( script );
        while ( matcher.find() ) {
            variablesUserInCmd.add( matcher.group() );
        }
        return variablesUserInCmd;
    }

    /**
     * Gets the arg value from json.
     *
     * @param parameters
     *         the parameters
     * @param argKey
     *         the arg key
     *
     * @return the arg value from json
     */
    @SuppressWarnings( "unchecked" )
    protected Object getArgValueFromJson( Map< String, Object > parameters, String argKey ) {
        Object argObject;
        try {
            final Object object = parameters.get( argKey );
            if ( object instanceof EngineFile engineFile ) {
                argObject = engineFile.getPath();
            } else if ( object instanceof LinkedHashMap ) {
                argObject = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) parameters.get( argKey ) ),
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
     * Gets the file path.
     *
     * @param value
     *         the value
     *
     * @return the file path
     */
    protected String getFilePath( final Object value ) {
        String filePath;
        if ( value instanceof EngineFile engineFile ) {
            filePath = engineFile.getPath();
        } else if ( value instanceof LinkedHashMap ) {
            filePath = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) value ), EngineFile.class ).getPath();
        } else {
            filePath = value.toString();
        }
        return filePath;
    }

    /**
     * Gets the nodes in cmd.
     *
     * @param argKey
     *         the arg key
     * @param jsonObject
     *         the json object
     * @param elementName
     *         the element name
     */
    private void getNodesInCmd( final String argKey, JsonNode jsonObject, String elementName, String jobSystemFilePath, String varKey ) {
        try {
            log.info( ">>getNodesInCmd argKey: " + argKey );
            log.info( ">>getNodesInCmd jsonObject: " + jsonObject );
            final String item = argKey.replace( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + elementName
                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + SYSTEM_OUTPUT_TAB
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES + ".", "" );
            Object object;
            if ( item.contentEquals( "{{" + elementName + ".systemoutput.PATH}}" ) ) {
                object = jobSystemFilePath;
            } else if ( item.contentEquals( "{{" + elementName + ".systemoutput}}" ) ) {
                object = jsonObject;
            } else {
                object = JsonPath.read( Configuration.defaultConfiguration().jsonProvider().parse( jsonObject.toString() ),
                        ConstantsString.DOLLAR + "." + item );
            }
            log.info( ">>getNodesInCmd object: " + object );
            replaceArguments( object, varKey != null ? varKey : argKey );
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    public Map< String, Object > getParameters() {
        return parameters;
    }

    /**
     * Gets the parent element name.
     *
     * @return the parent element name
     */
    protected String getParentElementName() {
        return parentElementName;
    }

    /**
     * Python output transferring elements.
     *
     * @param notif
     *         the notif
     * @param argKey
     *         the arg key
     *
     * @return the notification
     */
    protected Notification outputTransferringElements( final Notification notif, final String argKey, String varKey ) {
        try {
            final String jobDirectory = job.getWorkingDir().getPath() + java.io.File.separator;
            if ( jobDirectory.isEmpty() ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_FILE ) ) );
                return notif;
            }
            if ( argKey.equals( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + element.getName()
                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + USER_OUTPUT_TAB
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES ) ) {
                replaceArguments( jobDirectory
                        + argKey.replace( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES, ConstantsString.EMPTY_STRING )
                        .replaceAll( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING )
                        + ConstantsString.JSON_EXTENSION, argKey );
            }
            final String systemKey = argKey.substring( 0, argKey.lastIndexOf( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES )
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES.length() );
            log.debug( "argKey.substring( 0, argKey.indexOf( ConstantsString.DOT ) )" + systemKey );
            String parentJsonFile;
            String afterReplace = systemKey.replace( StringConst.VARIABLE_KEY_LEADING_BRACES, ConstantsString.EMPTY_STRING )
                    .replace( StringConst.VARIABLE_KEY_TRAILING_BRACES, ConstantsString.EMPTY_STRING );
            if ( systemKey.contains( "PATH" ) ) {
                parentJsonFile = afterReplace.substring( 0, afterReplace.lastIndexOf( "." ) );
            } else {
                parentJsonFile = afterReplace;
            }
            log.debug( "parentJsonFile: " + parentJsonFile );
            log.debug( "argKey: " + argKey );
            final int keysStartingIndex = argKey.indexOf( ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES )
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES.length();
            final String key = argKey.substring( keysStartingIndex );
            log.debug( "actual key: " + key );
            final String elementName = argKey
                    .substring( FIRST_INDEX, argKey.indexOf( ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS ) )
                    .replace( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES, "" );
            if ( parameters.containsKey( elementName ) && ( boolean ) parameters.get( elementName ) ) {
                String userOutputFilePath = jobDirectory + parentJsonFile + ConstantsString.JSON_EXTENSION;
                JsonNode jsonObject = null;
                synchronized ( this ) {
                    try ( InputStream userOutputFileStream = new FileInputStream( userOutputFilePath ) ) {
                        jsonObject = JsonUtils.convertInputStreamToJsonNode( userOutputFileStream );
                    } catch ( IOException e ) {
                        log.error( e.getLocalizedMessage(), e );
                        wfLogger.error( e.getLocalizedMessage() );
                    }
                }
                replaceUserOutputs( argKey, jsonObject, elementName, userOutputFilePath, varKey );
            }
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
        return notif;
    }

    /**
     * Process log and throw exception if errors are found in element.
     *
     * @param notif
     *         the notif
     */
    public void processLogAndThrowExceptionIfErrorsAreFoundInElement( final Notification notif ) {
        if ( ( notif != null ) && !notif.getErrors().isEmpty() ) {
            for ( final Error error : notif.getErrors() ) {
                wfLogger.error(
                        MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                                + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME,
                                element.getName() ) + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                                WFEMessages.ERROR_MESSAGE, error.getMessage() ) );

                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                        ELEMENT_MESSAGE_PRE_FIX + element.getName() + ConstantsString.COLON + error.getMessage() ) );
            }
            writeOutPutParentFile( EXIT_CODE_WITH_ERROR );
            // if selected value from Stop workflow on error in the exceptions tab of
            // element is yes then the workflow would be stopped
            if ( element.getStopOnWorkFlowOption().equals( ConstantsString.DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR ) ) {
                throw new SusRuntimeException( notif.getErrors().toString() );
            }
        }
    }

    /**
     * Replace arguments.
     *
     * @param argObject
     *         the arg object
     * @param argKey
     *         the arg key
     *
     * @return the notification
     */
    protected Notification replaceArguments( Object argObject, String argKey ) {
        final Notification notification = new Notification();
        try {
            if ( argObject != null ) {
                String value = argObject.toString();
                if ( value.contains( ConstantsString.DELIMETER_FOR_WIN ) ) {
                    value = value.replace( ConstantsString.DELIMETER_FOR_WIN, ConstantsString.PATH_SEPARATOR_WIN );
                }
                cmd = cmd.replace( argKey, value );
            } else {
                cmd = cmd.replace( argKey, ConstantsString.EMPTY_STRING );
                wfLogger.warn( MessagesUtil.getMessage( WFEMessages.ARGUMENT_CAN_NOT_BE_NULL, argKey ) );
            }
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
        return notification;
    }

    /**
     * Replace user outputs.
     *
     * @param argKey
     *         the arg key
     * @param jsonObject
     *         the json object
     * @param elementName
     *         the element name
     */
    private void replaceUserOutputs( final String argKey, JsonNode jsonObject, String elementName, String userOutputFilePath,
            String varKey ) {
        try {
            log.info( ">>getNodesInCmd argKey: " + argKey );
            log.info( ">>getNodesInCmd jsonObject: " + jsonObject );
            final String item = argKey.replace( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + elementName
                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + USER_OUTPUT_TAB
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES + ".", "" );
            Object object;
            if ( item.contentEquals( "{{" + elementName + ".useroutput.PATH}}" ) ) {
                object = userOutputFilePath;
            } else if ( item.contentEquals( "{{" + elementName + ".useroutput}}" ) ) {
                object = jsonObject;
            } else {
                // added this property to remove extra escape char.
                JSONValue.COMPRESSION = JSONStyle.LT_COMPRESS;
                object = JsonPath.read( Configuration.defaultConfiguration().jsonProvider().parse( String.valueOf( jsonObject ) ),
                        ConstantsString.DOLLAR + "." + item );
            }
            log.info( "half Resolved key" + argKey + " Full resolved:" + varKey + " >>getNodesInCmd object: " + object );
            replaceArguments( object, varKey != null ? varKey : argKey );
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
    }

    protected UserDTO getCurrentUser() {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + API_CURRENT_USER;
        SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
        String json = JsonUtils.toJson( susResponse.getData() );
        return JsonUtils.jsonToObject( json, UserDTO.class );
    }

    /**
     * Sets the parameters.
     *
     * @param parameters
     *         the parameters
     */
    public void setParameters( Map< String, Object > parameters ) {
        this.parameters = parameters;
    }

    /**
     * Sort by length.
     *
     * @param oldVariablesUserInCmd
     *         the old variables user in cmd
     *
     * @return the list
     */
    protected List< String > sortByLength( List< String > oldVariablesUserInCmd ) {
        List< String > newVariablesUserInCmd = null;
        if ( CollectionUtils.isNotEmpty( oldVariablesUserInCmd ) ) {
            newVariablesUserInCmd = new ArrayList<>();
            for ( final String string : oldVariablesUserInCmd ) {
                if ( string.contains( ".useroutput}}." ) ) {
                    newVariablesUserInCmd.add( string );
                }
            }
            newVariablesUserInCmd.sort( ( o1, o2 ) -> o1.length() > o2.length() ? -1 : 1 );
            oldVariablesUserInCmd.removeAll( newVariablesUserInCmd );
            if ( CollectionUtils.isNotEmpty( oldVariablesUserInCmd ) ) {
                newVariablesUserInCmd.addAll( oldVariablesUserInCmd );
            }
        }
        return newVariablesUserInCmd;
    }

    /**
     * System transferring elements.
     *
     * @param notif
     *         the notif
     * @param argKey
     *         the arg key
     *
     * @return the notification
     */
    protected Notification systemTransferringElements( final Notification notif, final String argKey, String varKey ) {
        try {
            final String jobSystemFilePath = job.getWorkingDir().getPath() + java.io.File.separator + job.getName()
                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + SYSTEM_OUTPUT_TAB + ConstantsString.JSON_EXTENSION;
            log.info( ">>systemTransferringElements jobSystemFile " + jobSystemFilePath );
            log.info( ">>systemTransferringElements " + ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES + parentElementName
                    + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + SYSTEM_OUTPUT_TAB
                    + ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES );
            if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                log.info( ">>systemTransferringElements startsWith" );
                JsonNode jsonJobSystemObject = null;
                try ( InputStream jobSystemFileStream = new FileInputStream( jobSystemFilePath ) ) {
                    jsonJobSystemObject = JsonUtils.convertInputStreamToJsonNode( jobSystemFileStream );
                } catch ( final IOException e ) {
                    log.error( e.getMessage(), e );
                }
                final String elementName = argKey
                        .substring( FIRST_INDEX, argKey.indexOf( ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS ) )
                        .replace( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES, "" );
                try {
                    /* read json element **/
                    final Object parentElemetJson = JsonPath.read(
                            Configuration.defaultConfiguration().jsonProvider().parse( String.valueOf( jsonJobSystemObject ) ),
                            ConstantsString.DOLLAR + ConstantsWFE.StringConst.SEPARATION_BETWEEN_VARIABLE_PORTIONS + elementName );

                    final ObjectMapper mapper = new ObjectMapper();
                    final JsonNode node = mapper.convertValue( parentElemetJson, JsonNode.class );
                    log.info( ">>systemTransferringElements getNodesInCmd" );
                    log.info( ">>elementName: " + elementName );
                    getNodesInCmd( argKey, node, elementName, jobSystemFilePath, varKey );
                } catch ( final SusException e ) {
                    log.error( e.getLocalizedMessage(), e );
                }
            } else {
                log.info( "failed check" );
            }
        } catch ( final SusException e ) {
            log.error( e.getLocalizedMessage(), e );
            wfLogger.error( e.getLocalizedMessage() );
        }
        return notif;
    }

    /**
     * Write out put file.
     *
     * @param exitCode
     *         the exit code
     */
    protected void writeOutPutParentFile( String exitCode ) {
        addExistingOutputFileToWorkflowOutput();
        parentElementOutput.setExitCode( exitCode );
        workflowOutput.putIfAbsent( element.getName(), parentElementOutput );
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

    /**
     * Adds Existing file output to workflow output.
     */
    protected void addExistingOutputFileToWorkflowOutput() {
        try ( InputStream elementOutputStream = new FileInputStream( job.getElementOutput().getPath() ) ) {
            WorkflowOutput fileOutput = JsonUtils.jsonStreamToObject( elementOutputStream, WorkflowOutput.class );
            if ( fileOutput != null ) {
                for ( Entry< String, Object > entry : fileOutput.entrySet() ) {
                    workflowOutput.putIfAbsent( entry.getKey(), entry.getValue() );
                }
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Sets the job result parameters.
     */
    protected void setJobResultParameters() {
        if ( job.getResultParameters() == null ) {
            job.setResultParameters( parameters );
        } else {
            job.getResultParameters().putAll( parameters );
        }
    }

    /**
     * Update job.
     *
     * @param jobImpl
     *         the job impl
     *
     * @return true, if successful
     */
    public boolean updateJob( Job jobImpl ) {
        // job updating preprations
        setPropertiesForUrl();
        final String url = prepareURL( properties.getProperty( URL_UPDATE_JOB_BY_ID ), jobImpl.getServer() );
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobImpl );
        final SusResponseDTO responseDTO = SuSClient.putRequest( url + jobImpl.getId(), prepareHeaders( jobImpl.getRequestHeaders() ),
                payload );
        return responseDTO.getSuccess();
    }

    /**
     * Sets the properties for url.
     */
    private void setPropertiesForUrl() {
        try {
            properties.load( WorkflowExecutionManagerImpl.class.getClassLoader().getResourceAsStream( ConstantsString.WF_ENGINE ) );
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, ConstantsString.WF_ENGINE ) ),
                    WorkflowExecutionManagerImpl.class );
        }
    }

    /**
     * It prepares url by getting protocol : hostname and port from server appended with url provided.
     *
     * @param url
     *         , url of API
     * @param server
     *         , server
     *
     * @return requestHeaders
     */
    protected String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            wfLogger.info( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) );
            log.info( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /**
     * It adds headers for required for communication with server.
     *
     * @param headers
     *         the headers
     *
     * @return requestHeaders
     */
    protected Map< String, String > prepareHeaders( RequestHeaders headers ) {
        setPropertiesForUrl();
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( properties.getProperty( USER_AGENT ), headers.getUserAgent() );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );

        return requestHeaders;

    }

    /**
     * Update job and status failed.
     *
     * @param e
     *         the e
     */
    protected void updateJobAndStatusFailed( Exception e ) {
        log.error( element.getName() + e.getMessage() );
        wfLogger.error( element.getName() + e.getMessage() );
        List< LogRecord > existingLogs = job.getLog();
        existingLogs.add( new LogRecord( ConstantsMessageTypes.ERROR, element.getName() + e.getMessage(), new Date() ) );
        job.setLog( existingLogs );
        job.setStatus( new Status( WorkflowStatus.FAILED ) );
        updateJob( job );

        /* if ( null != job.getWorkflow().getMasterJobId() ) {
            updateMaster( job.getWorkflow().getMasterJobId(), e );
        } else if ( null != job.getWorkflow().getDummyMasterJobId() ) {
            updateMaster( job.getWorkflow().getDummyMasterJobId(), e );
        }*/
    }

    /**
     * Update master.
     *
     * @param jobId
     *         the job id
     * @param e
     *         the e
     */
    private void updateMaster( String jobId, Exception e ) {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/workflow/job/" + jobId;
        SusResponseDTO susResponse = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );
        String json = JsonUtils.toJson( susResponse.getData() );
        Job masterJob = JsonUtils.jsonToObject( json, Job.class );
        List< LogRecord > masterExistingLogs = masterJob.getLog();
        masterExistingLogs.add(
                new LogRecord( ConstantsMessageTypes.ERROR, job.getName() + " > " + element.getName() + e.getMessage(), new Date() ) );

        masterExistingLogs.sort( Comparator.comparing( LogRecord::getDate ) );

        masterJob.setLog( masterExistingLogs );
        masterJob.setStatus( new Status( WorkflowStatus.FAILED ) );
        masterJob.setServer( job.getServer() );
        masterJob.setRequestHeaders( job.getRequestHeaders() );
        updateJob( masterJob );
    }

    /**
     * Reads output and error streams of process concurrently to avoid process hanging.
     *
     * @param process
     *         the process
     * @param out
     *         the out
     * @param error
     *         the error
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    protected void readOutputAndErrorsOfProcess( Process process, final StringBuilder out, final StringBuilder error ) throws IOException {
        // creating two separate thread for read output and read errors so that we can
        // concurrently read both streams and avoid blocking
        Thread readOutputThread = new Thread( () -> {
            try {
                String line;
                BufferedReader stdInput = null;
                if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
                    stdInput = new BufferedReader( new InputStreamReader( process.getInputStream(), StandardCharsets.UTF_8 ) );
                    log.info( LOG_MESSAGE_ACTIVATION_ENCODE + ConstantsString.ENCODING_CONST_FORNIX );
                    wfLogger.info( LOG_MESSAGE_ACTIVATION_ENCODE + ConstantsString.ENCODING_CONST_FORNIX );
                } else if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_WINDOWS ) ) {
                    stdInput = new BufferedReader(
                            new InputStreamReader( process.getInputStream(), ConstantsString.ENCODING_CONST_FORWIN ) );
                    log.info( LOG_MESSAGE_ACTIVATION_ENCODE + ConstantsString.ENCODING_CONST_FORWIN );
                    wfLogger.info( LOG_MESSAGE_ACTIVATION_ENCODE + ConstantsString.ENCODING_CONST_FORWIN );
                }

                while ( process.isAlive() ) { // reading continuous output so process doesn't hang
                    try {
                        do {
                            while ( ( line = stdInput.readLine() ) != null ) {
                                out.append( line ).append( ConstantsString.NEW_LINE );
                            }

                            if ( !process.isAlive() ) {
                                line = null;
                            }
                        } while ( line != null );

                        // Nothing else to read, lets pause for a bit before trying again
                        process.waitFor( 100, TimeUnit.MILLISECONDS );
                    } catch ( InterruptedException e ) {
                        log.warn( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_WARNING, OUTPUT ), e );
                        Thread.currentThread().interrupt();
                    } catch ( Exception e ) {
                        log.error( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, OUTPUT ), e );
                        throw new SusException( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, OUTPUT ), e );
                    }
                }

                if ( stdInput != null ) {
                    stdInput.close();
                }
            } catch ( IOException e ) {
                log.error( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, OUTPUT ), e );
                throw new SusException( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, OUTPUT ), e );
            }
        } );
        readOutputThread.start();

        // creating two separate thread for read output and read errors so that we can
        // concurrently read both streams and avoid blocking
        Thread readErrorsThread = new Thread( () -> {
            try {
                String line;
                BufferedReader stdError = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );

                while ( process.isAlive() ) { // reading continuous errors so process doesn't hang
                    try {
                        do {
                            while ( ( line = stdError.readLine() ) != null ) {
                                error.append( line ).append( ConstantsString.NEW_LINE );
                            }

                            if ( !process.isAlive() ) {
                                line = null;
                            }
                        } while ( line != null );

                        // Nothing else to read, lets pause for a bit before trying again
                        process.waitFor( 100, TimeUnit.MILLISECONDS );
                    } catch ( InterruptedException e ) {
                        log.warn( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_WARNING, ERROR ), e );
                        Thread.currentThread().interrupt();
                    } catch ( Exception e ) {
                        log.error( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, ERROR ), e );
                        throw new SusException( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, ERROR ), e );
                    }
                }

                stdError.close();
            } catch ( IOException e ) {
                log.error( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, ERROR ), e );
                throw new SusException( MessagesUtil.getMessage( WFEMessages.ELEMENT_MONITORING_THREAD_ERROR, ERROR ), e );
            }
        } );
        if ( null != error ) {
            readErrorsThread.start();
        }
    }

    /**
     * Gets the engine properties.
     *
     * @return the engine properties
     */
    public static Properties getEngineProperties() {
        Properties properties = new Properties();
        try ( InputStream engineStream = new FileInputStream( FilenameUtils.getFullPathNoEndSeparator(
                URLDecoder.decode( WFApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8" ) )
                + File.separator + "engine_host.conf" ) ) {
            properties.load( engineStream );
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
            ExceptionLogger.logMessage( e.getMessage() );
            throw new SusDataBaseException(
                    ( MessageBundleFactory.getDefaultMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), "engine_host.conf" ) ) );
        }
        return properties;
    }

    /**
     * Gets the python path.
     *
     * @param properties
     *         the properties
     *
     * @return the python path
     */
    public String getPythonPath( Properties properties ) {
        String executionPythonPath;

        if ( properties.getProperty( "pythonpath" ) == null ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL ) );
            throw new SusException( MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL ) );
        }
        executionPythonPath = properties.getProperty( "pythonpath" );
        wfLogger.info( "engine host file : python path :" + executionPythonPath );

        return executionPythonPath;
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

}