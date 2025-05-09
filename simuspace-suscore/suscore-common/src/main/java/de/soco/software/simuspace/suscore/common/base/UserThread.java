package de.soco.software.simuspace.suscore.common.base;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * The Class UserThread.
 */
public class UserThread implements Runnable {

    /**
     * The started.
     */
    private final CountDownLatch started = new CountDownLatch( 1 );

    /**
     * The interrupted.
     */
    private final CountDownLatch interrupted = new CountDownLatch( 1 );

    /**
     * The stopped by.
     */
    private String stoppedBy;

    /**
     * Gets the stopped by.
     *
     * @return the stopped by
     */
    public String getStoppedBy() {
        return stoppedBy;
    }

    /**
     * Sets the stopped by.
     *
     * @param stoppedBy
     *         the new stopped by
     */
    public void setStoppedBy( String stoppedBy ) {
        this.stoppedBy = stoppedBy;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // to be overridden by inheritors
    }

    /**
     * Block until started.
     *
     * @throws InterruptedException
     *         the interrupted exception
     */
    public void blockUntilStarted() throws InterruptedException {
        started.await();
    }

    /**
     * Block until interrupted.
     *
     * @throws InterruptedException
     *         the interrupted exception
     */
    public void blockUntilInterrupted() throws InterruptedException {
        assert interrupted.await( 1, TimeUnit.SECONDS );
    }

}
