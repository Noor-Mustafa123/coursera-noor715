package de.soco.software.simuspace.suscore.core.model;

import java.util.UUID;

/**
 * Responsible to parse reorder selection request pay load.
 *
 * @author M.Nasir.Farooq
 */
public class SelectionItemOrder {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The old position.
     */
    private int oldPosition;

    /**
     * The new position.
     */
    private int newPosition;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the old position.
     *
     * @return the old position
     */
    public int getOldPosition() {
        return oldPosition;
    }

    /**
     * Sets the old position.
     *
     * @param oldPosition
     *         the new old position
     */
    public void setOldPosition( int oldPosition ) {
        this.oldPosition = oldPosition;
    }

    /**
     * Gets the new position.
     *
     * @return the new position
     */
    public int getNewPosition() {
        return newPosition;
    }

    /**
     * Sets the new position.
     *
     * @param newPosition
     *         the new new position
     */
    public void setNewPosition( int newPosition ) {
        this.newPosition = newPosition;
    }

    @Override
    public String toString() {
        return "ReOrderFilter [id=" + id + ", oldPosition=" + oldPosition + ", newPosition=" + newPosition + "]";
    }

}
