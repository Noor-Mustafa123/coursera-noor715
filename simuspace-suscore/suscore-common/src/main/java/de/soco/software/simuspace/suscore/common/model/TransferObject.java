package de.soco.software.simuspace.suscore.common.model;

import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;

/**
 * The Class TransferObject.
 */
public class TransferObject {

    /**
     * The object id.
     */
    private String objectId;

    /**
     * The target location id.
     */
    private String targetLocationId;

    /**
     * The operation type.
     */
    private TransferOperationType operationType;

    /**
     * Gets the object id.
     *
     * @return the object id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Sets the object id.
     *
     * @param objectId
     *         the new object id
     */
    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    /**
     * Gets the target location id.
     *
     * @return the target location id
     */
    public String getTargetLocationId() {
        return targetLocationId;
    }

    /**
     * Sets the target location id.
     *
     * @param targetLocationId
     *         the new target location id
     */
    public void setTargetLocationId( String targetLocationId ) {
        this.targetLocationId = targetLocationId;
    }

    /**
     * Gets the operation type.
     *
     * @return the operation type
     */
    public TransferOperationType getOperationType() {
        return operationType;
    }

    /**
     * Sets the operation type.
     *
     * @param operationType
     *         the new operation type
     */
    public void setOperationType( TransferOperationType operationType ) {
        this.operationType = operationType;
    }

}
