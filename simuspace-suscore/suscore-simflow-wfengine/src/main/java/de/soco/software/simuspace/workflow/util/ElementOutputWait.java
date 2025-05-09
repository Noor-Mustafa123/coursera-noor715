package de.soco.software.simuspace.workflow.util;

import java.io.Serializable;
import java.sql.Time;

/**
 * The Class ElementOutputWait.
 *
 * @author Abbas
 */
public class ElementOutputWait extends ElementOutput implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The waited in seconds.
     */
    private Time waitedInSeconds;

    /**
     * Gets the waited in seconds.
     *
     * @return the waited in seconds
     */
    public Time getWaitedInSeconds() {
        return waitedInSeconds;
    }

    /**
     * Sets the waited in seconds.
     *
     * @param waitedInSeconds
     *         the new waited in seconds
     */
    public void setWaitedInSeconds( Time waitedInSeconds ) {
        this.waitedInSeconds = waitedInSeconds;
    }

}
