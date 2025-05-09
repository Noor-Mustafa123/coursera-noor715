package de.soco.software.simuspace.suscore.object.threads;

import java.util.UUID;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.workflow.model.Job;

/**
 * The Class StopWFThread.
 */
public class StopWFThread implements Runnable {

    /**
     * The user id.
     */
    private String userId;

    /**
     * The object id.
     */
    private UUID objectId;

    /**
     * The job.
     */
    private Job job;

    private DataManager dataManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * Instantiates a new run import thread.
     */
    public StopWFThread() {
    }

    /**
     * Instantiates a new run import thread.
     *
     * @param importProcessBO
     *         the import process bo
     * @param userId
     *         the user id
     * @param us
     *         the us
     * @param processTreeForDiscard
     *         the process tree for discard
     * @param processManagerImpl
     *         the process manager impl
     */
    public StopWFThread( String userId, UUID objectId, Job job, JobManager jobManager, DataManager dataManager ) {
        super();
        this.userId = userId;
        this.objectId = objectId;
        this.job = job;
        this.jobManager = jobManager;
        this.dataManager = dataManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        while ( !Thread.currentThread().isInterrupted() ) {
            System.out.println( "print" );
        }

    }

}
