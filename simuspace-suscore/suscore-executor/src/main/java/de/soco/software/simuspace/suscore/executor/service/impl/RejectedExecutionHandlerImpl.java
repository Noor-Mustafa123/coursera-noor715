package de.soco.software.simuspace.suscore.executor.service.impl;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The type Rejected execution handler.
 */
@Log4j2
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    /**
     * Method that may be invoked by a {@link ThreadPoolExecutor} when {@link ThreadPoolExecutor#execute execute} cannot accept a task. This
     * may occur when no more threads or queue slots are available because their bounds would be exceeded, or upon shutdown of the
     * Executor.
     *
     * @param task
     *         the runnable task requested to be executed
     * @param executor
     *         the executor attempting to execute this task
     */
    @Override
    public void rejectedExecution( Runnable task, ThreadPoolExecutor executor ) {

        try {
            String executorName;
            if ( executor instanceof TrackingThreadPoolExecutor trackingExecutor ) {
                executorName = trackingExecutor.getName();
            } else {
                executorName = executor.getClass().getName();
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.TASK_REJECTED_BY_EXECUTOR.getKey(), executorName, task ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        }
    }

}
