package de.soco.software.simuspace.workflow.processing.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.UserWFElementImpl;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

/**
 * This Class designed to download vault files from simuspace-1.1 using user information submitted. at the time of execution of workflow.
 *
 * @author M.Nasir.Farooq
 */
@Log4j2
public class SuSFileElementAction extends WFElementAction {

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * Auto generated serial version UID of class.
     */
    private static final long serialVersionUID = 1508338098099613182L;

    /**
     * The Constant GET.
     */
    private static final String HTTP_GET_REQUEST = "GET";

    /**
     * The Constant JAVA_SCRIPT_CODE.
     */
    private static final String JAVA_SCRIPT_CODE = "function myFunction(json) {obj = JSON.parse(json);return obj.url;}";

    /**
     * The Constant is used as argument for creating notification message.
     */
    private static final String KEY = "Key";

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The Constant METHOD for specifying HTTP request method type.
     */
    private static final String METHOD = "method";

    /**
     * The Constant OBJECT_KEY_VALUE_LENGTH. constant to check the length after splitting object
     */
    private static final int OBJECT_KEY_VALUE_LENGTH = 2;

    /**
     * This simuspace-1.1 API to create download link.
     */
    private static final String URL_DOWNLOAD_LINK = "/api/v1/dataobject/download/link/";

    /**
     * The Constant is used as argument for creating notification message.
     */
    private static final String VALUE = "Value";

    /**
     * This is the element information coming from designer containing fields having information what to do with that element.
     */
    private final transient UserWFElement element;

    /**
     * The rest API server credentials.
     */
    private transient RestAPI restAPI;

    /**
     * The constructor which sets different fields of object.
     *
     * @param job
     *         the job
     * @param element
     *         the element generic one to execute.
     * @param parameters
     *         these are the fields of connected elements to that element.
     */

    public SuSFileElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.element = element;
        this.parameters = parameters;
        this.job = job;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * Instantiates a new su S file element action.
     *
     * @param job
     *         the job
     * @param element
     *         the element
     * @param parameters
     *         the parameters
     * @param restAPI
     *         the rest API
     * @param executedElementIds
     *         the executed element ids
     */
    public SuSFileElementAction( Job job, UserWFElement element, Map< String, Object > parameters, RestAPI restAPI,
            Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.element = element;
        this.parameters = parameters;
        this.job = job;
        this.restAPI = restAPI;
        if ( element != null ) {
            setId( element.getId() );
        }
    }

    /**
     * The doAction. This function is responsible for performing some action on sus-file element This function gets the fields of an element
     * understand the information in fields and perform action accordingly.
     *
     * @return the notification
     *
     * @throws JsonSerializationException
     *         the json serialization exception
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
            Map< String, String > objectMapping = new HashMap<>();
            for ( final Field< ? > elementField : element.getFields() ) {
                if ( elementField.getType().contentEquals( FieldTypes.TEXTAREA.getType() ) ) {
                    String mapping = ( String ) elementField.getValue();
                    mapping = mapping.trim();
                    mapping = mapping.replace( ConstantsString.NEW_LINE, ConstantsString.EMPTY_STRING );
                    objectMapping = getMappingKeyValues( notif, mapping );
                }
            }

            if ( notif.hasErrors() ) {
                return notif;
            }
            log.info( MessagesUtil.getMessage( WFEMessages.GOING_TO_EXECUTE_DOWNLOAD_SUS_FILES_WITH_MAPPING ) + objectMapping );
            log.info( MessagesUtil.getMessage( WFEMessages.GOING_TO_EXECUTE_DOWNLOAD_SUS_FILES_WITH_PARAMETERS ) + parameters );
            final Notification notification = downloadSusFiles( parameters, objectMapping );
            notif.addErrors( notification.getErrors() );

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }

        return notif;

    }

    /**
     * This function is responsible for creating new internal element {@link RestElementAction} who is further responsible for making rest
     * class.
     *
     * @param parameters
     *         these are the fields of connected elements to that element.
     * @param map
     *         this is the information map which contains keys of vault_id fields and value file where to download that file.
     *
     * @return the notification
     */
    private Notification downloadSusFiles( Map< String, Object > parameters, Map< String, String > map ) {

        final UserWFElement wfElement = new UserWFElementImpl();

        final Map< String, Object > parametersToRest = prepareRestParameters( parameters, map );

        final Field< String > field = new Field<>();
        field.setType( FieldTypes.TEXTAREA.getType() );
        field.setValue( JAVA_SCRIPT_CODE );
        final List< Field< ? > > fields = new ArrayList<>();

        fields.add( field );
        wfElement.setFields( fields );

        final RestElementAction restElementAction = new RestElementAction( parametersToRest );
        restElementAction.setRestAPI( restAPI );
        return restElementAction.doAction();

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

            executeSuSFileElement( executor, executionTime );
            // wait all unfinished tasks for 2 sec
            try {
                if ( !executor.awaitTermination( ConstantsInteger.INTEGER_VALUE_TWO, TimeUnit.SECONDS ) ) {
                    executor.shutdownNow();
                }
            } catch ( final InterruptedException e ) {
                log.error( "Executor shoutdown interrupted.", e );
                Thread.currentThread().interrupt();
            }
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
    private void executeSuSFileElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                log.info( EXE_ELEMENT + element.getName() );

                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );

                log.info( EXE_ELEM_COMPL + element.getName() );
                wfLogger.info( EXE_ELEM_COMPL + element.getName() );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }
            } catch ( final SusException e ) {
                log.error( "IO Element Execution Error in Thread: ", e );
                try {
                    JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR,
                            LOG_MESSAGE_PRE_FIX_ELEMENT + element.getName() + ConstantsString.COLON + e ) );
                    updateJobAndStatusFailed( e );
                } catch ( final SusException e1 ) {
                    log.error( e1.getLocalizedMessage(), e1 );
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
     * Gets the file from json.
     *
     * @param object
     *         the object
     *
     * @return the file from json
     */
    private EngineFile getFileFromJson( Object object ) {

        EngineFile fileObject;
        try {
            if ( object instanceof EngineFile engineFile ) {
                fileObject = engineFile;
            } else {
                if ( object instanceof String ) {
                    fileObject = JsonUtils.jsonToObject( object.toString(), EngineFile.class );
                } else {
                    fileObject = JsonUtils.jsonToObject( JsonUtils.toJson( ( LinkedHashMap< String, String > ) object ), EngineFile.class );
                }

            }
        } catch ( final JsonSerializationException e ) {
            log.error( e.getMessage(), e );
            wfLogger.error( e.getMessage() );
            throw e;
        }
        return fileObject;
    }

    /**
     * Gets the map entry.
     *
     * @param notif
     *         the notifies for wrong mapping information.
     * @param mappingObject
     *         the mapping object string to be convert to map entry.
     *
     * @return the map entry parsed from string mapping object.
     */
    private Entry< String, String > getMapEntry( final Notification notif, String mappingObject ) {

        Map.Entry< String, String > entry = null;

        final String[] objKeyValue = mappingObject.split( ConstantsString.COLON );
        if ( objKeyValue.length == OBJECT_KEY_VALUE_LENGTH ) {

            final String key = objKeyValue[ 0 ].trim();
            final String value = objKeyValue[ 1 ].trim();
            if ( !validateStringWithBraces( key ) ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.FIELD_DONT_HAVE_EXPECTED_LEADING_TRAILINGS, KEY, key,
                        ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES, ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES ) ) );
            } else if ( !validateStringWithBraces( value ) ) {
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.FIELD_DONT_HAVE_EXPECTED_LEADING_TRAILINGS, VALUE, value,
                        ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES, ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES ) ) );
            } else {

                entry = new AbstractMap.SimpleEntry<>( key.replace( ConstantsString.STANDARD_SEPARATOR, ConstantsString.EMPTY_STRING ),
                        value.replace( ConstantsString.STANDARD_SEPARATOR, ConstantsString.EMPTY_STRING ) );

            }

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.KEY_OR_VALUE_IS_MISSING_IN_OBJECT, mappingObject ) ) );
        }

        return entry;
    }

    /**
     * Gets the map of source and destination mapping objects.
     *
     * @param notif
     *         the notifies for wrong mapping information.
     * @param mapping
     *         the mapping string from designer.
     *
     * @return the mapping key values of object fields.
     */
    private Map< String, String > getMappingKeyValues( Notification notif, final String mapping ) {
        final Map< String, String > map = new HashMap<>();

        if ( validateStringWithBraces( mapping ) ) {

            final String[] mappingObjects = mapping.split( ConstantsString.COMMA );
            for ( final String mappingObject : mappingObjects ) {
                final Entry< String, String > entry = getMapEntry( notif, mappingObject );

                if ( entry != null ) {
                    map.put( entry.getKey(), entry.getValue() );
                    if ( !parameters.containsKey( entry.getKey() ) ) {
                        notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.MAPPING_KEY_OR_VALUE_IS_MISSING_IN_PARAMETERS, KEY,
                                entry.getKey() ) ) );
                    }
                    if ( !parameters.containsKey( entry.getValue() ) ) {
                        notif.addError( new Error(
                                MessagesUtil.getMessage( WFEMessages.MAPPING_KEY_OR_VALUE_IS_MISSING_IN_PARAMETERS, VALUE,
                                        entry.getValue() ) ) );
                    }
                }

            }

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.INVALID_COMMAND_PROVIDED ) ) );
        }

        return map;
    }

    /**
     * Prepare rest credentials to request the rest APIs form sus-1.1.
     *
     * @param parameters
     *         These are the fields of connected elements to that element.
     * @param map
     *         The information map which contains information which vault file is to download as which file.
     *
     * @return the map The information map usable for {@linkplain RestElementAction} making rest requests.
     */
    private Map< String, Object > prepareRestParameters( Map< String, Object > parameters, Map< String, String > map ) {

        final Map< String, Object > restParameters = new HashMap<>();

        for ( final Entry< String, String > mappingObj : map.entrySet() ) {
            final Map< String, String > apiRedirect = new HashMap<>();

            Integer vaultId = null;

            if ( parameters.get( mappingObj.getKey() ) instanceof String vaultIdStr ) {
                vaultId = Integer.parseInt( vaultIdStr );

            } else {
                vaultId = ( Integer ) parameters.get( mappingObj.getKey() );
            }

            final EngineFile file = getFileFromJson( parameters.get( mappingObj.getValue() ) );

            apiRedirect.put( ConstantRequestHeader.URL, URL_DOWNLOAD_LINK + vaultId );
            apiRedirect.put( METHOD, HTTP_GET_REQUEST );
            apiRedirect.put( ConstantRequestHeader.AUTH_TOKEN, restAPI.getRequestHeaders().getToken() );
            apiRedirect.put( ConstantRequestHeader.FILE, file.getPath() );

            restParameters.put( UUID.randomUUID().toString(), apiRedirect );

        }

        return restParameters;
    }

    /**
     * Sets the rest API.
     *
     * @param restAPI
     *         the new rest API
     */
    public void setRestAPI( RestAPI restAPI ) {
        this.restAPI = restAPI;
    }

    /**
     * Validate leading and trailing braces of field variable.
     *
     * @param string
     *         the string
     *
     * @return true, if successful
     */
    private boolean validateStringWithBraces( String string ) {

        if ( string != null ) {
            return string.startsWith( ConstantsWFE.StringConst.VARIABLE_KEY_LEADING_BRACES ) && string.endsWith(
                    ConstantsWFE.StringConst.VARIABLE_KEY_TRAILING_BRACES );
        }

        return false;
    }

}
