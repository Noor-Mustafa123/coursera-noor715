package de.soco.software.simuspace.suscore.local.daemon.thread.service;

import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;

/**
 * The Class ExecutorBaseService is responsible for providing executor thread pool.
 *
 * @author Noman Arshad
 */
public class DaemonExecutorBaseService {

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * Instantiates a new base service.
     *
     * @param serviceClass
     */
    public DaemonExecutorBaseService( Class< ? > serviceClass ) {
    }

    /**
     * Gets the thread pool executor service.
     *
     * @return the threadPoolExecutorService
     */
    public ThreadPoolExecutorService getThreadPoolExecutorService() {
        return threadPoolExecutorService;
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

}