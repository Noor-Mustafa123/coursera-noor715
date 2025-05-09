package de.soco.software.simuspace.workflow.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.WfLogger;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;

/**
 * To create or update the log of job.
 */
@Log4j2
public class JobLog {

    /**
     * The job.
     */
    private static Job job;

    /**
     * The map to save log list for each job.
     */
    private static final Map< UUID, List< LogRecord > > jobLogMap = new ConcurrentHashMap<>();

    /**
     * The Constant WorkFlowlogger for logging user related logging information.
     */
    private static final WfLogger wfLogger = new WfLogger( ConstantsString.WF_LOGGER );

    /**
     * Properties of url which is set from configuration.
     */
    private static Properties properties;

    /**
     * The constant for property update log.
     */
    private static final String UPDATE_LOG = "UPDATE_LOG_OF_JOB";

    /**
     * The constant for property update log.
     */
    private static final String UPDATE_LOG_AND_PROGRESS = "UPDATE_LOG_AND_PROGRESS_OF_JOB";

    /**
     * The constant for property user agent.
     */
    private static final String USER_AGENT = "USER_AGENT";

    /**
     * This method put log of job and throws SusException if job not updated.
     *
     * @param id
     *         universally unique identifier for a job
     * @param logMessage
     *         the message to add in log
     */
    public static synchronized void addLog( UUID id, LogRecord logMessage ) {

        if ( !jobLogMap.containsKey( id ) ) {
            final List< LogRecord > log;
            if ( job.getLog() != null ) {
                var reverseBuffer = new ArrayList<>( job.getLog() );
                Collections.reverse( reverseBuffer );
                log = new CopyOnWriteArrayList<>( reverseBuffer );
            } else {
                log = new CopyOnWriteArrayList<>();
            }
            jobLogMap.put( id, log );
        }
        final List< LogRecord > logs = jobLogMap.get( id );
        logMessage.setDate( new Date() );
        logs.add( logMessage );
        jobLogMap.put( id, logs );
        job.setId( id );
        job.setLog( logs );
    }

    /**
     * Adds the log and progress.
     *
     * @param id
     *         the id
     * @param excutedElementCount
     *         the excuted element count
     * @param logMessage
     *         the log message
     */
    public static void addLogAndProgress( UUID id, long excutedElementCount, LogRecord logMessage ) {
        addLog( id, logMessage );
        if ( job.getProgress() != null ) {
            job.getProgress().setCompleted( excutedElementCount );
        }
    }

    /**
     * Gets the log list by job id.
     *
     * @param id
     *         the id
     *
     * @return the log list by job id
     */
    public static List< LogRecord > getlogListByJobId( UUID id ) {
        return jobLogMap.get( id );
    }

    /**
     * This method prepare headers for request.
     *
     * @param headers
     *         the request headers
     *
     * @return the map
     *
     * @throws SusException
     *         the simuspace exception
     */
    private static Map< String, String > prepareHeaders( RequestHeaders headers ) {
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
     * This method prepare the url for client.
     *
     * @param url
     *         the configurable base url
     * @param server
     *         the server containing host, port, protocol etc
     *
     * @return the string
     *
     * @throws SusException
     *         the Simuspace exception
     */
    private static String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            log.error( WFEMessages.CONFIG_NOT_PROVIDED.getValue() );
            wfLogger.error( WFEMessages.CONFIG_NOT_PROVIDED.getValue() );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }

    }

    /**
     * This method configure the log.
     *
     * @param jobimpl
     *         the new job
     */
    public static void setJob( Job jobimpl ) {
        job = jobimpl;
    }

    /**
     * This method sets the properties for url from config file. It throws SusException if file not found.
     */
    private static void setPropertiesForUrl() {
        try {
            properties = new Properties();
            properties.load( WorkflowExecutionManagerImpl.class.getClassLoader().getResourceAsStream( ConstantsString.WF_ENGINE ) );
        } catch ( final IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.FILE_PATH_NOT_EXIST, ConstantsString.WF_ENGINE ) ),
                    WorkflowExecutionManagerImpl.class );
        }
    }

    /**
     * This method updates log for job and returns true if server update the request successfully.
     *
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    public static synchronized boolean updateLog( Job job ) {
        if ( !jobLogMap.containsKey( job.getId() ) ) {
            final List< LogRecord > log = new CopyOnWriteArrayList<>();
            jobLogMap.put( job.getId(), log );
        }

        final List< LogRecord > logs = jobLogMap.get( job.getId() );
        job.setLog( logs );
        try {
            setPropertiesForUrl();
            final String url = prepareURL( properties.getProperty( UPDATE_LOG ) + job.getId(), job.getServer() );
            final String payload = JsonUtils.toFilteredJson( new String[]{}, job );
            final SusResponseDTO responseDTO = SuSClient.putRequest( url, prepareHeaders( job.getRequestHeaders() ), payload );
            if ( responseDTO != null ) {
                return responseDTO.getSuccess();
            } else {
                return false;
            }
        } catch ( Exception e ) {
            log.error( "update Job Error : ", e );
            wfLogger.error( "update Job Error : " + e );
        }
        return false;
    }

    /**
     * This method updates log for job and returns true if server update the request successfully.
     *
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    public static synchronized boolean updateLogAndProgress( Job job, long excutedElementCount ) {
        if ( !jobLogMap.containsKey( job.getId() ) ) {
            final List< LogRecord > log = new CopyOnWriteArrayList<>();
            jobLogMap.put( job.getId(), log );
        }

        final List< LogRecord > logs = jobLogMap.get( job.getId() );
        job.setLog( logs );
        if ( job.getProgress() != null ) {
            job.getProgress().setCompleted( excutedElementCount );
        }
        try {
            setPropertiesForUrl();
            final String url = prepareURL( properties.getProperty( UPDATE_LOG_AND_PROGRESS ) + job.getId(), job.getServer() );
            final String payload = JsonUtils.toFilteredJson( new String[]{}, job );
            final SusResponseDTO responseDTO = SuSClient.putRequest( url, prepareHeaders( job.getRequestHeaders() ), payload );
            if ( responseDTO != null ) {
                return responseDTO.getSuccess();
            } else {
                return false;
            }
        } catch ( Exception e ) {
            log.error( "update Job Error : ", e );
            wfLogger.error( "update Job Error : " + e );
        }
        return false;
    }

    /**
     * Constructor to hide the implicit public one.
     */
    private JobLog() {
        // Hiding explicit one
    }

}
