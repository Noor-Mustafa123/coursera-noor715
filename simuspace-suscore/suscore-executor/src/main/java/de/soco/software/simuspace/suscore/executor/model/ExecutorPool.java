package de.soco.software.simuspace.suscore.executor.model;

import java.util.List;

/**
 * The Class ExecutorPool holds file data for executor.
 *
 * @author Noman Arshad
 */
public class ExecutorPool {

    /**
     * The pools.
     */
    private List< Pools > pools;

    /**
     * Gets the pools.
     *
     * @return the pools
     */
    public List< Pools > getPools() {
        return pools;
    }

    /**
     * Sets the pools.
     *
     * @param pools
     *         the new pools
     */
    public void setPools( List< Pools > pools ) {
        this.pools = pools;
    }

}
