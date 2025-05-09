package de.soco.software.simuspace.suscore.common.http.model;

import java.io.Serializable;

/**
 * The Class HttpReceiver only deals to provide Receiving file bytes model for utility purposes available globally in the system.
 *
 * @author Ahsan Khan
 */
public class HttpReceiver implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2628957224880733333L;

    /**
     * The file path.
     */
    private String filePath;

    /**
     * The initial range.
     */
    private long initialRange;

    /**
     * The max range.
     */
    private long maxRange;

    /**
     * The total file size.
     */
    private long totalFileSize;

    /**
     * The received bytes.
     */
    private String receivedBytes;

    /**
     * The chunk.
     */
    private long chunk;

    /**
     * Gets the initial range.
     *
     * @return the initial range
     */
    public long getInitialRange() {
        return initialRange;
    }

    /**
     * Sets the initial range.
     *
     * @param initialRange
     *         the new initial range
     */
    public void setInitialRange( long initialRange ) {
        this.initialRange = initialRange;
    }

    /**
     * Gets the max range.
     *
     * @return the max range
     */
    public long getMaxRange() {
        return maxRange;
    }

    /**
     * Sets the max range.
     *
     * @param maxRange
     *         the new max range
     */
    public void setMaxRange( long maxRange ) {
        this.maxRange = maxRange;
    }

    /**
     * Gets the total file size.
     *
     * @return the total file size
     */
    public long getTotalFileSize() {
        return totalFileSize;
    }

    /**
     * Sets the total file size.
     *
     * @param totalFileSize
     *         the new total file size
     */
    public void setTotalFileSize( long totalFileSize ) {
        this.totalFileSize = totalFileSize;
    }

    /**
     * Gets the chunk.
     *
     * @return the chunk
     */
    public long getChunk() {
        return chunk;
    }

    /**
     * Sets the chunk.
     *
     * @param chunk
     *         the new chunk
     */
    public void setChunk( long chunk ) {
        this.chunk = chunk;
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
     * Gets the received bytes.
     *
     * @return the received bytes
     */
    public String getReceivedBytes() {
        return receivedBytes;
    }

    /**
     * Sets the received bytes.
     *
     * @param receivedBytes
     *         the new received bytes
     */
    public void setReceivedBytes( String receivedBytes ) {
        this.receivedBytes = receivedBytes;
    }

}
