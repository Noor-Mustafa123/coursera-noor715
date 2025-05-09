/*
 *
 */

package de.soco.software.simuspace.suscore.common.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class used To separate the Added/removed Ids for comparison of auditLogs.
 *
 * @author Nosheen.Sharif
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ComparisonDTO {

    /**
     * The added.
     */
    private List< UUID > added;

    /**
     * The removed.
     */
    private List< UUID > removed;

    /**
     * Gets the added.
     *
     * @return the added
     */
    public List< UUID > getAdded() {
        return added;
    }

    /**
     * Sets the added.
     *
     * @param added
     *         the new added
     */
    public void setAdded( List< UUID > added ) {
        this.added = added;
    }

    /**
     * Gets the removed.
     *
     * @return the removed
     */
    public List< UUID > getRemoved() {
        return removed;
    }

    /**
     * Sets the removed.
     *
     * @param removed
     *         the new removed
     */
    public void setRemoved( List< UUID > removed ) {
        this.removed = removed;
    }

}
