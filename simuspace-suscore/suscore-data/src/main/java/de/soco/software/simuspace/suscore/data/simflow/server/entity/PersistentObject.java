package de.soco.software.simuspace.suscore.data.simflow.server.entity;

import java.util.UUID;

/**
 * This interface defines object uniqueness in the system with a UUID identifier. All entities in the system must implement this interface
 *
 * @author Nosheen.Sharif
 */
public interface PersistentObject {

    /**
     * Gets the id.
     *
     * @return the id
     */
    UUID getId();

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    void setId( UUID id );

}
