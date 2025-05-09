package de.soco.software.simuspace.workflow.processing.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.UserConfigFile;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.properties.EnginePropertiesManager;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * The AssembleAndSimulateCB2ElementAction.
 *
 * @author noman arshad
 */
@Log4j2
public class AssembleAndSimulateCB2ElementAction extends WFElementAction {

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The Constant ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_FILE_ERROR.
     */
    private static final String ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_FILE_ERROR = "Assemble and simulate Template json file error: ";

    /**
     * The Constant ASSEMBLE_AND_SIMULATE_PYTHON_FILE_PATH_ERROR_ENGINE_HOST_CONF.
     */
    private static final String ASSEMBLE_AND_SIMULATE_PYTHON_FILE_PATH_ERROR_ENGINE_HOST_CONF = "Assemble and simulate python file path Error : engine_host.conf";

    /**
     * The Constant PREPARING_ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_VARIABLES.
     */
    private static final String PREPARING_ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_VARIABLES = "Preparing assemble and simulate Template json variables";

    /**
     * The Constant WAIT.
     */
    private static final String WAIT = "wait";

    /**
     * The Constant NEW_VARIANT_NAME.
     */
    private static final String NEW_VARIANT_NAME = "newVariantName";

    /**
     * The Constant CREATE_NEW.
     */
    private static final String CREATE_NEW = "createNew";

    /**
     * The Constant INCLUDE_LIST.
     */
    private static final String INCLUDE_LIST = "IncludeList";

    /**
     * The Constant REFERENCE_VARIANT.
     */
    private static final String REFERENCE_VARIANT = "referenceVariant";

    /**
     * The Constant CB2_JOB_NAME.
     */
    private static final String CB2_JOB_NAME = "cb2JobName";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This is the element information coming from designer containing fields having information what to do with that element.
     */
    private final transient UserWFElement element;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * Instantiates a new assemble and simulate CB 2 element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     */
    public AssembleAndSimulateCB2ElementAction( Job job, UserWFElement element ) {
        super( job, element );
        this.job = job;
        this.element = element;
    }

    /**
     * Instantiates a new assemble and simulate CB 2 element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     */
    public AssembleAndSimulateCB2ElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new assemble and simulate CB 2 element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param executedElementIds
     *         the executed element ids
     */
    public AssembleAndSimulateCB2ElementAction( Job job, UserWFElement element, Map< String, Object > parameters,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
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
            doAction();
            wfLogger.success( EXE_ELEM_COMPL + element.getName() );
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
            if ( !job.isFileRun() ) {
                JobLog.updateLogAndProgress( job, executedElementsIds.size() );
            }
            Boolean isPermitted = true;
            parameters.put( element.getName(), isPermitted );
            writeOutPutParentFile( DEFULT_EXIT_CODE );
            setJobResultParameters();
            return new DecisionObject( true, true, element.getKey(), element.getName(), parameters );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            updateJobAndStatusFailed( e );
            throw new SusException( e.getLocalizedMessage() );
        }
    }

    /**
     * Wite assemble simulate template file.
     *
     * @param payload
     *         the payload
     *
     * @return the string
     */
    private String witeAssembleSimulateTemplateFile( String payload ) {
        String assembleAndSimulateTemplate = job.getWorkingDir().getPath() + File.separator + element.getName() + ".json";
        wfLogger.info( "Writing assemble and simulate template file :" + assembleAndSimulateTemplate );
        try ( FileWriter file = new FileWriter( assembleAndSimulateTemplate ) ) {
            file.write( payload );
            file.flush();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            wfLogger.error( ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_FILE_ERROR + e.getMessage() );
            throw new SusException( ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_FILE_ERROR + e.getMessage() );
        }
        return assembleAndSimulateTemplate;
    }

    /**
     * Gets the assemble simulate python path.
     *
     * @param properties
     *         the properties
     *
     * @return the assemble simulate python path
     */
    private String getAssembleSimulatePythonPath( Properties properties ) {
        String assembleAndSimulatePythonFile;
        if ( properties.getProperty( "assemble.simulate.wf.path" ) != null ) {
            assembleAndSimulatePythonFile = properties.getProperty( "assemble.simulate.wf.path" )
                    .replace( "${karaf.script}", EnginePropertiesManager.getKarafScriptPath() );
        } else {
            wfLogger.error( ASSEMBLE_AND_SIMULATE_PYTHON_FILE_PATH_ERROR_ENGINE_HOST_CONF );
            log.error( ASSEMBLE_AND_SIMULATE_PYTHON_FILE_PATH_ERROR_ENGINE_HOST_CONF );
            throw new SusException( ASSEMBLE_AND_SIMULATE_PYTHON_FILE_PATH_ERROR_ENGINE_HOST_CONF );
        }
        return assembleAndSimulatePythonFile;
    }

    /**
     * Prepare refrance varient object.
     *
     * @param cb2RefranceVarSelectionKey
     *         the cb 2 refrance var selection key
     *
     * @return the object
     */
    private Object prepareRefranceVarientObject( String cb2RefranceVarSelectionKey ) {
        try {
            Object reqData = getBmwCaeBenchObjetBySelection( cb2RefranceVarSelectionKey );
            List< Object > selectedBaseVarientList = new ArrayList<>();
            selectedBaseVarientList = ( List< Object > ) JsonUtils.jsonToList( JsonUtils.toJson( reqData ), selectedBaseVarientList );

            for ( Object object : selectedBaseVarientList ) {
                return object;
            }

        } catch ( Exception e ) {
            wfLogger.error( "Refrance varient get from server Failed: " + e.getMessage() );
            log.error( "Refrance varient get from server Failed: " + e.getMessage(), e );
        }
        return "";
    }

    /**
     * Gets the bmw cae bench objet by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the bmw cae bench objet by selection
     */
    private Object getBmwCaeBenchObjetBySelection( final String selectionId ) {

        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/wizards/dummyfiles/list/selection/" + selectionId;
        SusResponseDTO request = SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) );

        return request.getData();

    }

    /**
     * Do action.
     *
     * @return the notification
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public Notification doAction() throws IOException {
        Notification notif = new Notification();
        Map< String, Object > jsonObjMain = new HashMap<>();
        wfLogger.info( PREPARING_ASSEMBLE_AND_SIMULATE_TEMPLATE_JSON_VARIABLES );
        for ( Field< ? > fields : element.getFields() ) {
            if ( fields.getName().equalsIgnoreCase( REFERENCE_VARIANT ) ) {
                String cb2RefranceVarSelectionKey = fields.getValue().toString();
                Object selectedBaseVarient = prepareRefranceVarientObject( cb2RefranceVarSelectionKey );
                jsonObjMain.put( REFERENCE_VARIANT, selectedBaseVarient );
            } else if ( fields.getName().equalsIgnoreCase( INCLUDE_LIST ) ) {
                List< Object > listIncludes = getVariableIncludesPrepared( fields, notif );
                jsonObjMain.put( INCLUDE_LIST, listIncludes );
            } else if ( fields.getName().equalsIgnoreCase( CREATE_NEW ) ) {
                jsonObjMain.put( CREATE_NEW, Boolean.valueOf( fields.getValue().toString() ) );
            } else if ( fields.getName().equalsIgnoreCase( NEW_VARIANT_NAME ) ) {
                String varName = replaceAllVariableValuesInText( fields.getValue().toString(), notif );
                jsonObjMain.put( NEW_VARIANT_NAME, varName );
            } else if ( fields.getName().equalsIgnoreCase( WAIT ) ) {
                jsonObjMain.put( WAIT, Boolean.valueOf( fields.getValue().toString() ) );
            } else if ( fields.getName().equalsIgnoreCase( CB2_JOB_NAME ) ) {
                String jobName = replaceAllVariableValuesInText( fields.getValue().toString(), notif );
                jsonObjMain.put( CB2_JOB_NAME, jobName );
            } else {
                jsonObjMain.put( fields.getName(), fields.getValue() );
            }
        }
        // common properties
        jsonObjMain.put( "jobdir", job.getWorkingDir().getPath() + File.separator );
        jsonObjMain.put( "vaultPath", getServerVaultPath() );
        jsonObjMain.put( "jobId", job.getId().toString() );

        wfLogger.info( "calling assemble and simulate copy local files" );
        String payload = JsonUtils.toJson( jsonObjMain );
        String result = copyLocalFilesToWorkingDir( payload );
        wfLogger.info( result );

        String assembleAndSimulateTemplate = witeAssembleSimulateTemplateFile( payload );
        Properties properties = getEngineProperties();
        String assembleAndSimulatePythonFile = getAssembleSimulatePythonPath( properties );
        String executionPythonPath = getPythonPath( properties );
        String outputFilePath = getOutputFilePath();

        String[] command = new String[]{ executionPythonPath, assembleAndSimulatePythonFile, assembleAndSimulateTemplate, outputFilePath };

        wfLogger.info( "calling assemble and simulate python file > " + Arrays.toString( command ) );
        ProcessResult commandExe = WorkflowExecutionManagerImpl.runCommand( Arrays.asList( command ), element.getName(), true, null );
        wfLogger.debug( "Output: python File output stream result: " );
        wfLogger.debug( commandExe.getOutputString() );

        JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, commandExe.getOutputString() ) );
        if ( commandExe.getErrorStreamString() != null && !commandExe.getErrorStreamString().isEmpty() ) {
            JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, commandExe.getErrorStreamString() ) );
            wfLogger.error( "Error after python command : " + commandExe.getErrorStreamString() );
            throw new SusException( MessagesUtil.getMessage( WFEMessages.PYTHON_EXECUTION_PATH_IS_NOT_SET, ConstantsString.LOCAL ) );
        }

        return notif;
    }

    /**
     * Gets the variable includes prepared.
     *
     * @param fields
     *         the fields
     * @param notif
     *         the notif
     *
     * @return the variable includes prepared
     */
    private List< Object > getVariableIncludesPrepared( Field< ? > fields, Notification notif ) {
        List< Object > includesPrepared = new ArrayList<>();
        try {
            Map< String, Object > jsonObjMain = new HashMap<>();
            jsonObjMain.put( "includes", fields.getValue() );
            JSONParser parser = new JSONParser();
            JSONObject obj4 = ( JSONObject ) parser.parse( JsonUtils.toJson( jsonObjMain ) );

            JSONArray obj4Array = ( JSONArray ) parser.parse( obj4.get( "includes" ).toString() );

            for ( Object o : obj4Array ) {
                JSONObject obj2 = ( JSONObject ) parser.parse( o.toString() );
                if ( obj2.containsKey( "selection_type" ) && obj2.get( "selection_type" ).toString().equalsIgnoreCase( "variable" ) ) {
                    String name = obj2.get( "name" ).toString();
                    name = replaceAllVariableValuesInText( name, notif );
                    obj2.put( "name", name );
                    log.debug( "obj2" + obj2 );
                }
                includesPrepared.add( obj2 );
            }
        } catch ( ParseException e ) {
            log.error( "Error extracting cb2 include files for downlaod : " + e.getMessage(), e );
        }
        log.debug( "includesPrepared " + JsonUtils.toJson( includesPrepared ) );
        return includesPrepared;

    }

    /**
     * Gets the server vault path.
     *
     * @return the server vault path
     */
    private String getServerVaultPath() {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/data/project/propertymanager/vault";
        return SuSClient.getRequest( url, prepareHeaders( job.getRequestHeaders() ) ).getData().toString();
    }

    /**
     * Copy local files to working dir.
     *
     * @param payload
     *         the payload
     *
     * @return the string
     */
    private String copyLocalFilesToWorkingDir( String payload ) {
        String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                + "/api/workflow/assemblesimulate/files/copy";
        return SuSClient.postRequest( url, payload, prepareHeaders( job.getRequestHeaders() ) ).getData().toString();

    }

    /**
     * Gets the output file path.
     *
     * @return the output file path
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private String getOutputFilePath() throws IOException {
        File outputFile = new File( job.getWorkingDir().getPath() + File.separator + element.getName() + "_output.json" );
        outputFile.createNewFile();
        wfLogger.info( "output.json file created : " + outputFile.getPath() );
        return outputFile.getPath();
    }

    /**
     * Gets the local python path from config home.
     *
     * @return the local python path from config home
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public static String getLocalPythonPathFromConfigHome() throws IOException {
        final String propFileName =
                System.getProperty( ConstantsString.HOME_DIRECTORY ) + File.separator + ConstantsString.SIMUSPACE_SYSTEM_DIRECTORY
                        + File.separator + ConstantsString.CONFIG + File.separator + ConstantsString.CONIGFILE;
        final File file = new File( propFileName );
        UserConfigFile userConfigFile;

        try ( final InputStream targetConfigStream = new FileInputStream( file ) ) {
            userConfigFile = JsonUtils.jsonToObject( targetConfigStream, UserConfigFile.class );
        }
        return userConfigFile.getPython().getPythonpath();
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
     * Replaces all variable values in text
     */
    private String replaceAllVariableValuesInText( String textData, Notification notif ) {

        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( textData ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            final List< String > variablesIncludingDotResolved = new ArrayList<>();
            for ( String splitters : variablesIncludingDot ) {
                variablesIncludingDotResolved.add( replaceAfterDotVariablesForSupportOfJsonExtract( splitters ) );
            }
            textData = replaceVariableValuesForMultipleDotKeys( textData, variablesIncludingDotResolved, variablesIncludingDot,
                    new Notification() );
        }

        final List< String > variables = getAllSimpleVariables( textData );
        if ( CollectionUtils.isNotEmpty( variables ) ) {
            textData = replaceVariableValues( textData, variables, notif );
        }

        log.info( "After replacement notifies: " + notif.getErrors() );
        if ( notif.getErrors().size() > ConstantsInteger.INTEGER_VALUE_ZERO ) {
            wfLogger.error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_TYPE, element.getKey() ) + ConstantsString.PIPE_CHARACTER
                    + ConstantsString.TAB_SPACE + MessagesUtil.getMessage( WFEMessages.WORKFLOW_ELEMENT_NAME, element.getName() )
                    + ConstantsString.PIPE_CHARACTER + ConstantsString.TAB_SPACE + MessagesUtil.getMessage(
                    WFEMessages.ERROR_IN_SCRIPT_FORMAT, notif.getErrors() ) );

        }

        return textData;
    }

    /**
     * Replace variable values for multiple dot keys.
     *
     * @param textData
     *         the text data
     * @param variablesIncludingDotResolved
     *         the variables including dot resolved
     * @param variablesIncludingDot
     *         the variables including dot
     * @param notif
     *         the notif
     *
     * @return the string
     */
    private String replaceVariableValuesForMultipleDotKeys( String textData, List< String > variablesIncludingDotResolved,
            List< String > variablesIncludingDot, Notification notif ) {
        int keyIndex = 0;
        for ( final String argKey : variablesIncludingDotResolved ) {
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                    ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    textData = replaceArguments( argObj, argKey, textData );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    textData = computeReserveKeyWordLIST( argKey, textData );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
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
                outputTransferringElements( notif, argKey, variablesIncludingDot.get( keyIndex ) );
                textData = cmd;
            } else if ( argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                cmd = textData; // below method operates on cmd hence assigning textData to cmd
                systemTransferringElements( notif, argKey, variablesIncludingDot.get( keyIndex ) );
                textData = cmd;
            }
            keyIndex++;
        }
        return textData;
    }

    /**
     * Replaces all variable values in text
     */
    private String replaceVariableValues( String textData, List< String > variables, Notification notif ) {
        for ( final String argKey : variables ) {
            if ( argKey.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && !argKey.startsWith(
                    ConstantsString.SYS_CMD_INDICATION ) && !argKey.contains( USER_OUTPUT_TAB ) && !argKey.contains( SYSTEM_OUTPUT_TAB ) ) {
                if ( parameters.containsKey( argKey ) ) {
                    final Object argObj = getArgValueFromJson( parameters, argKey );
                    textData = replaceArguments( argObj, argKey, textData );
                } else if ( argKey.contains( RESERVE_KEY_LIST ) ) {
                    textData = computeReserveKeyWordLIST( argKey, textData );
                } else {
                    notif.addError(
                            new Error( MessagesUtil.getMessage( WFEMessages.PARAM_DONT_CONTAIN_ARGUMENT, argKey, element.getName() ) ) );
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
     *
     * @return the notification
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
            wfLogger.warn( MessagesUtil.getMessage( WFEMessages.ARGUMENT_CAN_NOT_BE_NULL, argKey ) );
        }
        return textData;
    }

    /**
     * Compute reserve key word LIST.
     *
     * @param argKey
     *         the arg key
     */
    private String computeReserveKeyWordLIST( final String argKey, String textData ) {
        String fieldz = argKey.replace( RESERVE_KEY_LIST, "" );
        String replaceField = fieldz;
        String[] splitCMD = fieldz.split( "}}" );
        String idValue = splitCMD[ 0 ];
        idValue = idValue + "}}";
        fieldz = fieldz.replace( idValue, "" );
        if ( parameters.containsKey( idValue ) ) {
            Object fId = parameters.get( idValue );
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
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
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

}
