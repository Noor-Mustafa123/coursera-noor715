package de.soco.software.simuspace.suscore.lifecycle.model;

import java.util.List;

/**
 * This class is responsible for mapping Lifecycle configuration objects
 *
 * @author Nosheen.Sharif
 */
public class LifeCycleConfigration {

    /**
     * The life cycles.
     */
    private List< LifeCycleDTO > lifeCycles;

    /**
     * Gets the life cycles.
     *
     * @return the lifeCycles
     */
    public List< LifeCycleDTO > getLifeCycles() {
        return lifeCycles;
    }

    /**
     * Sets the life cycles.
     *
     * @param lifeCycles
     *         the lifeCycles to set
     */
    public void setLifeCycles( List< LifeCycleDTO > lifeCycles ) {
        this.lifeCycles = lifeCycles;
    }

}
