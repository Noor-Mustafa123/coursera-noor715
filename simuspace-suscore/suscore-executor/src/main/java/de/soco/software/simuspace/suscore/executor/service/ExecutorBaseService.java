package de.soco.software.simuspace.suscore.executor.service;

/**
 * The Class ExecutorBaseService is responsible for providing executor thread pool.
 *
 * @author Shan Arshad
 */
public class ExecutorBaseService {

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * Instantiates a new base service.
     *
     * @param serviceClass
     *         the serviceClass
     */
    public ExecutorBaseService( Class< ? > serviceClass ) {
    }

    /**
     * Instantiates a new base service.
     */
    public ExecutorBaseService() {
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
     *         the threadPoolExecutorService
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

}
