package de.soco.software.simuspace.suscore.executor.model;

/**
 * The Class Properties holds the executor properties.
 *
 * @author Noman Arshad
 */

public class ExecutorProperties {

    /**
     * The core pool size.
     */
    private String corePoolSize;

    /**
     * The core max size.
     */
    private String coreMaxSize;

    /**
     * The queue size.
     */
    private String queueSize;

    /**
     * The keep alive time.
     */
    private String keepAliveTime;

    /**
     * Instantiates a new properties.
     */
    public ExecutorProperties() {
    }

    /**
     * Instantiates a new properties.
     *
     * @param coreMaxSize
     *         the core max size
     * @param queueSize
     *         the queue size
     * @param corePoolSize
     *         the core pool size
     * @param keepAliveTime
     *         the keep alive time
     */
    public ExecutorProperties( String coreMaxSize, String queueSize, String corePoolSize, String keepAliveTime ) {
        super();
        this.coreMaxSize = coreMaxSize;
        this.queueSize = queueSize;
        this.corePoolSize = corePoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * Gets the core max size.
     *
     * @return the core max size
     */
    public String getCoreMaxSize() {
        return coreMaxSize;
    }

    /**
     * Sets the core max size.
     *
     * @param coreMaxSize
     *         the new core max size
     */
    public void setCoreMaxSize( String coreMaxSize ) {
        this.coreMaxSize = coreMaxSize;
    }

    /**
     * Gets the queue size.
     *
     * @return the queue size
     */
    public String getQueueSize() {
        return queueSize;
    }

    /**
     * Sets the queue size.
     *
     * @param queueSize
     *         the new queue size
     */
    public void setQueueSize( String queueSize ) {
        this.queueSize = queueSize;
    }

    /**
     * Gets the core pool size.
     *
     * @return the core pool size
     */
    public String getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * Sets the core pool size.
     *
     * @param corePoolSize
     *         the new core pool size
     */
    public void setCorePoolSize( String corePoolSize ) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * Gets the keep alive time.
     *
     * @return the keep alive time
     */
    public String getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * Sets the keep alive time.
     *
     * @param keepAliveTime
     *         the new keep alive time
     */
    public void setKeepAliveTime( String keepAliveTime ) {
        this.keepAliveTime = keepAliveTime;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ClassPojo [coreMaxSize = " + coreMaxSize + ", queueSize = " + queueSize + ", corePoolSize = " + corePoolSize
                + ", keepAliveTime = " + keepAliveTime + "]";
    }

}