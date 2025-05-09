package de.soco.software.simuspace.suscore.executor.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.executor.enums.ThreadPoolEnum;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.executor.service.impl.ThreadPoolExecutorServiceImpl;
import de.soco.software.simuspace.suscore.executor.service.impl.TrackingThreadPoolExecutor;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.WorkflowLocationThread;

/**
 * The Class SusExecutorUtil.
 */
@Log4j2
public class SusExecutorUtil {

    /**
     * The thread pool executor service.
     */
    public static final ThreadPoolExecutorService threadPoolExecutorService = ThreadPoolExecutorServiceImpl.getInstance();

    /**
     * The Constant API_RUN_WORKFLOW.
     */
    private static final String API_RESUBMIT_WORKFLOW = "/api/workflow/resubmit/job";

    /**
     * The Constant WORKFLOW_QUEUE_FILE.
     */
    private static final String WORKFLOW_QUEUE_FILE = "workflow_queue.json";

    /**
     * The Constant HOST_WORKFLOW_QUEUE_FILE.
     */
    private static final String HOST_WORKFLOW_QUEUE_FILE = "remote_workflow_queue.json";

    /**
     * Saves Jobs queue to file so it can read and resubmitted later.
     */
    public static void saveJobsQueueToFile() {
        try {
            saveWorkflowQueueToFile();
            saveRemoteHostWorkflowQueueToFile();
        } catch ( Exception e ) {
            log.error( "Executor pool save ERROR", e );
        }
    }

    /**
     * Saves workflow queue to file so it can read and resubmitted later.
     */
    private static void saveWorkflowQueueToFile() {
        File queueFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + WORKFLOW_QUEUE_FILE );

        if ( threadPoolExecutorService.isExecutorShutdownOrTerminated( ThreadPoolEnum.WORKFLOW.getName() ) ) {
            throw new SusException( "workflow pool is not Active" );
        }
        TrackingThreadPoolExecutor workflowPoolExecutor = threadPoolExecutorService.getWorkflowPoolExecutor();
        List< Runnable > queue = workflowPoolExecutor.shutdownNow();
        log.info( "queue size to store:" + queue.size() );
        Map< UUID, Future< ? > > futureMap = threadPoolExecutorService.getFutureMap();
        List< UUID > queueJobIds = new ArrayList<>();

        // iterate queue

        for ( Runnable runnable : queue ) {
            Future< ? > future = ( Future< ? > ) runnable;
            log.info( "Executor Queue: " + future );
            for ( Map.Entry< UUID, Future< ? > > entry : futureMap.entrySet() ) {
                if ( future.equals( entry.getValue() ) ) {
                    queueJobIds.add( entry.getKey() );
                    log.info( "Matched Queue Job Id: " + entry.getKey() );
                    break;
                }
            }
        }
        log.info( "found jobs:" + queueJobIds.size() );
        List< JobParameters > queueJobsList = getQueueJobsList( queueJobIds );
        String filePayload = JsonUtils.convertListToJson( queueJobsList );
        try ( FileWriter file = new FileWriter( queueFile, false ) ) {
            file.write( filePayload );
            file.flush();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Saves dynamic host queue to file so it can read and resubmitted later.
     */
    private static void saveRemoteHostWorkflowQueueToFile() {
        File queueFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + HOST_WORKFLOW_QUEUE_FILE );

        if ( queueFile.exists() ) {
            queueFile.delete(); // delete old queue file
        }

        try ( FileWriter file = new FileWriter(
                PropertiesManager.getDefaultServerTempPath() + File.separator + HOST_WORKFLOW_QUEUE_FILE ) ) {
            Map< String, TrackingThreadPoolExecutor > hostPoolMap = threadPoolExecutorService.getHostPoolExecutorMap();
            Map< UUID, Future< ? > > futureMap = threadPoolExecutorService.getFutureMap();

            List< UUID > queueJobIds = new ArrayList<>();

            for ( Map.Entry< String, TrackingThreadPoolExecutor > hostPool : hostPoolMap.entrySet() ) {
                TrackingThreadPoolExecutor hostExecutor = hostPool.getValue();
                BlockingQueue< Runnable > queue = hostExecutor.getQueue();

                // iterate queue

                for ( Runnable runnable : queue ) {
                    Future< ? > future = ( Future< ? > ) runnable;
                    log.info( "Executor Queue: " + future );

                    for ( Map.Entry< UUID, Future< ? > > entry : futureMap.entrySet() ) {
                        if ( future.equals( entry.getValue() ) ) {
                            queueJobIds.add( entry.getKey() );
                            log.info( "Matched Queue Job Id: " + entry.getKey() );
                            break;
                        }
                    }
                }
            }

            List< JobParameters > queueJobsList = getQueueJobsList( queueJobIds );

            file.write( JsonUtils.convertListToJson( queueJobsList ) );
            file.flush();

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Get queue jobs list .
     *
     * @param queueJobIds
     *         the queue Job Ids
     */
    private static List< JobParameters > getQueueJobsList( List< UUID > queueJobIds ) {
        Map< UUID, Runnable > threadMap = threadPoolExecutorService.getThreadMap();
        List< JobParameters > jobsList = new ArrayList<>();

        for ( UUID jobId : queueJobIds ) {
            Runnable jobThread = threadMap.get( jobId );

            if ( jobThread instanceof WorkflowLocationThread workflowLocationThread ) {
                JobParameters jobParameters = workflowLocationThread.getJobParametersLocationModel().getJobParameters();

                if ( jobParameters.getJobType() != JobTypeEnums.VARIANT.getKey() ) {
                    jobsList.add( jobParameters );
                }
            }
        }

        return jobsList;
    }

    /**
     * Read from jobs queue file and resubmit jobs queue.
     */
    public static void resubmitJobsQueue() {
        resubmitWorkflowQueue();
        resubmitRemoteHostWorkflowQueue();
    }

    /**
     * Read from workflow queue file and Resubmit workflow queue.
     */
    private static void resubmitWorkflowQueue() {
        File queueFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + WORKFLOW_QUEUE_FILE );

        if ( queueFile.exists() && queueFile.length() != 0 ) {

            new Thread( () -> {
                try {
                    List< JobParameters > jobsList = JsonUtils.jsonFileToObjectList( queueFile, JobParameters.class );

                    log.info( "restore list size:" + jobsList.size() );
                    for ( JobParameters jobParameters : jobsList ) {
                        resubmitJob( jobParameters );
                    }
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                }
            } ).start();
        }
    }

    /**
     * Read from workflow queue file and Resubmit workflow queue.
     */
    private static void resubmitRemoteHostWorkflowQueue() {
        File queueFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + HOST_WORKFLOW_QUEUE_FILE );

        if ( queueFile.exists() && queueFile.length() != 0 ) {

            new Thread( () -> {
                try {
                    List< JobParameters > jobsList = JsonUtils.jsonFileToObjectList( queueFile, JobParameters.class );

                    for ( JobParameters job : jobsList ) {
                        resubmitJob( job );
                        TimeUnit.SECONDS.sleep( 10 );
                    }
                } catch ( InterruptedException e ) {
                    log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
                }
            } ).start();
        }
    }

    /**
     * Submit server job.
     *
     * @param jobParameters
     *         the job parameters
     */
    public static void resubmitJob( JobParameters jobParameters ) {
        jobParameters.getRequestHeaders().setToken( null );
        final String payload = JsonUtils.toFilteredJson( new String[]{}, jobParameters );
        String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort() + API_RESUBMIT_WORKFLOW;
        SuSClient.postRequest( url, payload, prepareHeaders( jobParameters.getRequestHeaders() ) );
    }

    /**
     * It adds headers for required for communication with server.
     *
     * @param headers
     *         the headers
     *
     * @return requestHeaders
     */
    private static Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( "User-Agent", headers.getUserAgent() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );

        return requestHeaders;
    }

    /**
     * Instantiates a new sus executor util.
     */
    private SusExecutorUtil() {

    }

}
