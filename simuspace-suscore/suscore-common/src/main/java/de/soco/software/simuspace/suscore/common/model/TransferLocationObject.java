package de.soco.software.simuspace.suscore.common.model;

import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;

/**
 * The Class TransferObject.
 */
public class TransferLocationObject {

    /**
     * The file rel path.
     */
    private String filePath;

    /**
     * The target address.
     */
    private String targetAddress;

    /**
     * The target token.
     */
    private String targetToken;

    /**
     * The operation.
     */
    private TransferOperationType operation;

    public TransferLocationObject() {
        super();
    }

    /**
     * Gets the target address.
     *
     * @return the target address
     */
    public String getTargetAddress() {
        return targetAddress;
    }

    /**
     * Sets the target address.
     *
     * @param targetAddress
     *         the new target address
     */
    public void setTargetAddress( String targetAddress ) {
        this.targetAddress = targetAddress;
    }

    /**
     * Gets the target token.
     *
     * @return the target token
     */
    public String getTargetToken() {
        return targetToken;
    }

    /**
     * Sets the target token.
     *
     * @param targetToken
     *         the new target token
     */
    public void setTargetToken( String targetToken ) {
        this.targetToken = targetToken;
    }

    /**
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath
     *         the new file path
     */
    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    public TransferOperationType getOperation() {
        return operation;
    }

    /**
     * Sets the operation.
     *
     * @param operation
     *         the new operation
     */
    public void setOperation( TransferOperationType operation ) {
        this.operation = operation;
    }

    public TransferLocationObject( String filePath, String targetAddress, String targetToken, TransferOperationType operation ) {
        super();
        this.filePath = filePath;
        this.targetAddress = targetAddress;
        this.targetToken = targetToken;
        this.operation = operation;
    }

}
