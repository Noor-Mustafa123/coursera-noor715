package de.soco.software.simuspace.workflow.processing.impl;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.validator.routines.EmailValidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.EmailConstant;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.activator.WfEngineActivate;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWFE;
import de.soco.software.simuspace.workflow.dexecutor.DecisionObject;
import de.soco.software.simuspace.workflow.dto.EmailDTO;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFElementAction;
import de.soco.software.simuspace.workflow.util.JobLog;

@Log4j2
public class EmailElementAction extends WFElementAction {

    /**
     * The Constant RESERVE_KEY_LIST.
     */
    private static final String RESERVE_KEY_LIST = ".LIST";

    /**
     * The Constant LOG_MESSAGE_PRE_FIX_ELEMENT.
     */
    private static final String LOG_MESSAGE_PRE_FIX_ELEMENT = "Element : ";

    /**
     * Auto generated serial version UID of class.
     */
    private static final long serialVersionUID = 4678923538199837597L;

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * The job element.
     */
    private final transient UserWFElement element;

    /**
     * The Constant NEW_LINE.
     */
    private static final String NEW_LINE = "\n";

    /**
     * The Constant SEND_EMAIL_FAILED.
     */
    private static final String SEND_EMAIL_FAILED = "Failed to send Email, please correct email.conf";

    /**
     * The Constant HOST_CONNECTION_FAILED.
     */
    private static final String HOST_CONNECTION_FAILED = "Please correct host and port in email.conf";

    /**
     * The Constant EMAIL_AUTHENTICATION_FAILED.
     */
    private static final String EMAIL_AUTHENTICATION_FAILED = "Email or password not correct in email.conf";

    /**
     * The Constant WORKFLOW_EMAIL_CONFIG.
     */
    private static final String WORKFLOW_EMAIL_CONFIG = "/api/workflow/email/config";

    /**
     * The Constant TO.
     */
    private static final String TO = "To";

    /**
     * The Constant CC.
     */
    private static final String CC = "Cc";

    /**
     * The Constant BCC.
     */
    private static final String BCC = "Bcc";

    private static final String REPLY_TO = "Reply To";

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
    public EmailElementAction( Job job, UserWFElement element, Map< String, Object > parameters ) {
        super( job, element );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }

    }

    /**
     * Instantiates a new email element action.
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
    public EmailElementAction( Job job, UserWFElement element, Map< String, Object > parameters, Set< String > executedElementIds ) {
        super( job, element, executedElementIds );
        this.job = job;
        this.element = element;
        this.parameters = parameters;
        if ( element != null ) {
            setId( element.getId() );
        }

    }

    @Override
    public DecisionObject execute() {
        try {
            final int executionTime = element.getExecutionValue();
            addLogForUnlimitedExecution( executionTime );

            final ExecutorService executor = Executors.newFixedThreadPool( ConstantsInteger.INTEGER_VALUE_ONE );

            executeEmailElement( executor, executionTime );

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
     * Executes Hpc element.
     *
     * @param executor
     *         the executor
     * @param executionTime
     *         the execution time
     */
    private void executeEmailElement( ExecutorService executor, int executionTime ) {
        final Runnable task = () -> {
            try {
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.INFO, EXE_ELEMENT + element.getName() ) );
                wfLogger.info( EXE_ELEMENT + element.getName() );
                final Notification notif = doAction();
                processLogAndThrowExceptionIfErrorsAreFoundInElement( notif );
                wfLogger.success( EXE_ELEM_COMPL + element.getName() );

                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.SUCCESS, EXE_ELEM_COMPL + element.getName() ) );
                executedElementsIds.add( element.getId() );
                if ( !job.isFileRun() ) {
                    JobLog.updateLogAndProgress( job, executedElementsIds.size() );
                }

            } catch ( final Exception e ) {
                log.error( e.getMessage(), e );
                wfLogger.error( "Email Element Execution Error in Thread: ", e );
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
     * The doAction. This function is responsible for parsing user workflow element and replacing input variables for parameters and will
     * create expression.
     *
     * @return the notification
     */
    public Notification doAction() {
        final Notification notif = new Notification();
        Map< String, InternetAddress[] > recepiants = new HashMap<>();
        if ( parameters == null ) {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.EMPTY_PARAM ) ) );
        } else if ( element != null ) {
            notif.addNotification( element.validateException() );
            String subject = ConstantsString.EMPTY_STRING;
            String message = ConstantsString.EMPTY_STRING;
            String protocol = ConstantsString.EMPTY_STRING;
            String replyTo = ConstantsString.EMPTY_STRING;

            for ( Field< ? > userWFElementField : element.getFields() ) {
                String fieldData = userWFElementField.getValue().toString();
                fieldData = replaceAllVariableValuesInText( fieldData, notif );
                if ( !fieldData.isEmpty() ) {
                    switch ( userWFElementField.getName() ) {
                        case "protocol" -> protocol = fieldData;
                        case "smtp-to" -> recepiants.put( TO, getAllValidEmailAddress( Arrays.asList(
                                        Arrays.stream( fieldData.split( ConstantsString.COMMA ) ).map( String::trim ).toArray( String[]::new ) ),
                                notif ) );
                        case "smtp-cc" -> recepiants.put( CC, getAllValidEmailAddress( Arrays.asList(
                                        Arrays.stream( fieldData.split( ConstantsString.COMMA ) ).map( String::trim ).toArray( String[]::new ) ),
                                notif ) );
                        case "smtp-bcc" -> recepiants.put( BCC, getAllValidEmailAddress( Arrays.asList(
                                        Arrays.stream( fieldData.split( ConstantsString.COMMA ) ).map( String::trim ).toArray( String[]::new ) ),
                                notif ) );
                        case "smtp-replyTo" -> {
                            if ( !EmailValidator.getInstance().isValid( fieldData ) ) {
                                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ADDRESS_INVALID, REPLY_TO ) ) );
                            } else {
                                replyTo = fieldData;
                            }
                        }
                        case "smtp-subject", "unix-subject" -> subject = fieldData;
                        case "smtp-message", "unix-message" -> message = fieldData;
                        default -> {
                            // do nothing
                        }
                    }
                }
            }

            if ( notif.hasErrors() ) {
                return notif;
            }
            if ( protocol.contentEquals( EmailConstant.SMTP.toString() ) ) {
                prepareAndSendSmtpEmail( notif, recepiants, subject, message, replyTo );
            } else if ( protocol.contentEquals( EmailConstant.UNIX.toString() ) ) {
                prepareAndSendUnixEmail( notif, subject, message );
            }

        } else {
            notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ELEMENT_CAN_NOT_BE_NULL ) ) );
        }

        return notif;
    }

    private InternetAddress[] getAllValidEmailAddress( List< String > addresses, Notification notif ) {
        List< InternetAddress > finalEmailAddresses = new ArrayList<>();
        for ( String emailAddress : addresses ) {
            try {
                finalEmailAddresses.add( new InternetAddress( emailAddress ) );
            } catch ( AddressException e ) {
                log.error( e.getMessage(), e );
                notif.addError( new Error( MessagesUtil.getMessage( WFEMessages.ADDRESS_INVALID, emailAddress ) ) );
            }
        }
        return finalEmailAddresses.toArray( new InternetAddress[ addresses.size() ] );
    }

    private void prepareAndSendUnixEmail( Notification notif, String subject, String emailMessage ) {
        UserDTO userDTO = getCurrentUser();
        // echo "message from sus" | mail -s "subject from sus" username
        String[] cmdArray = null;
        if ( System.getProperty( ConstantsString.OS_NAME ).contains( ConstantsString.OS_LINUX ) ) {
            cmdArray = new String[]{ "echo", "\"" + emailMessage + "\"", "|", "mail", "-s", "\"" + subject + "\"", userDTO.getUserUid() };
        }
        try {
            final StringBuilder out = new StringBuilder();
            final StringBuilder error = new StringBuilder();
            final ProcessBuilder pb = new ProcessBuilder( cmdArray );
            final Process process = pb.start();
            readOutputAndErrorsOfProcess( process, out, error );
            if ( StringUtils.isNotNullOrEmpty( error.toString() ) ) {
                log.error( error.toString() );
                wfLogger.error( error.toString() );
                JobLog.addLog( job.getId(), new LogRecord( ConstantsMessageTypes.ERROR, error.toString() ) );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            notif.addError( new Error( e.getLocalizedMessage() ) );
        }
    }

    /**
     * Prepares and sends email.
     */
    private void prepareAndSendSmtpEmail( Notification notif, Map< String, InternetAddress[] > recepiants, String subject,
            String emailMessage, String replyTo ) {

        EmailDTO emailDto;

        if ( !isLocal( job.getRunsOn().getId().toString() ) ) {
            emailDto = populateEmailDtofromConf();
        } else {
            String url = job.getServer().getProtocol() + job.getServer().getHostname() + ConstantsString.COLON + job.getServer().getPort()
                    + WORKFLOW_EMAIL_CONFIG;
            emailDto = populateEmailDtofromConf( url );
        }

        Properties properties = new Properties();
        properties.put( "mail.smtp.host", emailDto.getHost() );
        properties.put( "mail.smtp.port", emailDto.getPort() );
        properties.put( "mail.smtps.auth", emailDto.getSmtpAuth() );
        properties.put( "mail.smtp.starttls.enable", emailDto.getStartTlsEnable() );
        properties.put( "mail.smtp.ssl.protocols", emailDto.getSslProtocols() );

        Session session = Session.getDefaultInstance( properties, null );
        try {
            Message msg = new MimeMessage( session );
            msg.setFrom( new InternetAddress( emailDto.getFromEmail() ) );
            if ( StringUtils.isNotNullOrEmpty( replyTo ) ) {
                msg.setReplyTo( new Address[]{ new InternetAddress( replyTo ) } );
            }
            msg.setRecipients( Message.RecipientType.TO, recepiants.get( TO ) );
            msg.setRecipients( Message.RecipientType.CC, recepiants.get( CC ) );
            msg.setRecipients( Message.RecipientType.BCC, recepiants.get( BCC ) );
            msg.setSubject( subject );
            msg.setContent( emailMessage, "text/html; charset=utf-8" );

            Transport transport = session.getTransport( emailDto.getMailProtocol() );
            transport.connect( emailDto.getHost(), Integer.parseInt( emailDto.getPort() ), emailDto.getFromEmail(),
                    emailDto.getPassword() );
            transport.sendMessage( msg, msg.getAllRecipients() );
            transport.close();
        } catch ( AuthenticationFailedException ex ) {
            log.error( ex.getMessage(), ex );
            notif.addError( new Error( ex + NEW_LINE + EMAIL_AUTHENTICATION_FAILED ) );
        } catch ( MessagingException ex ) {
            log.error( ex.getMessage(), ex );
            notif.addError( new Error( ex + NEW_LINE + HOST_CONNECTION_FAILED ) );
        } catch ( Exception ex ) {
            log.error( ex.getMessage(), ex );
            notif.addError( new Error( ex + NEW_LINE + SEND_EMAIL_FAILED ) );
        }
    }

    private boolean isLocal( String runsOn ) {
        return runsOn.equals( LocationsEnum.LOCAL_LINUX.getId() ) || runsOn.equals( LocationsEnum.LOCAL_WINDOWS.getId() );
    }

    private EmailDTO populateEmailDtofromConf() {
        EmailDTO emailDto = new EmailDTO();
        emailDto.setFromEmail( WfEngineActivate.getEmailAddress() );
        emailDto.setPassword( WfEngineActivate.getEmailPassword() );
        emailDto.setHost( WfEngineActivate.getEmailHost() );
        emailDto.setPort( WfEngineActivate.getEmailPort() );
        emailDto.setStartTlsEnable( WfEngineActivate.getEmailStarttlsEnable() );
        emailDto.setSslProtocols( WfEngineActivate.getEmailSslProtocols() );
        emailDto.setSmtpAuth( WfEngineActivate.getSmtpAuth() );
        emailDto.setMailProtocol( WfEngineActivate.getMailProtocol() );
        return emailDto;
    }

    private EmailDTO populateEmailDtofromConf( String url ) {
        Map< String, String > emailPropertyMap = ( Map< String, String > ) SuSClient.getRequest( url,
                prepareHeaders( job.getRequestHeaders() ) ).getData();

        EmailDTO emailDto = new EmailDTO();
        emailDto.setFromEmail( emailPropertyMap.get( "email" ) );
        emailDto.setPassword( emailPropertyMap.get( "password" ) );
        emailDto.setHost( emailPropertyMap.get( "host" ) );
        emailDto.setPort( emailPropertyMap.get( "port" ) );
        emailDto.setStartTlsEnable( emailPropertyMap.get( "starttls.enable" ) );
        emailDto.setSslProtocols( emailPropertyMap.get( "ssl.protocols" ) );
        emailDto.setSmtpAuth( emailPropertyMap.get( "smtp.auth" ) );
        emailDto.setMailProtocol( emailPropertyMap.get( "mail.protocol" ) );
        return emailDto;
    }

    /**
     * Replaces all variable values in text
     */
    private String replaceAllVariableValuesInText( String textData, Notification notif ) {
        final List< String > variablesIncludingDot = sortByLength( getAllSimpleVariablesIncludingDot( textData ) );
        if ( CollectionUtils.isNotEmpty( variablesIncludingDot ) ) {
            textData = replaceVariableValues( textData, variablesIncludingDot, new Notification() );
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

    /**
     * Sets the cmd.
     *
     * @param cmd
     *         the cmd to set
     */
    public void setCmd( String cmd ) {
        this.cmd = cmd;
    }

}