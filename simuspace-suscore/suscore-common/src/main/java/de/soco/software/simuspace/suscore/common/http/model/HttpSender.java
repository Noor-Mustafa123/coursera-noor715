package de.soco.software.simuspace.suscore.common.http.model;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.HttpUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class HttpSender only deals to provide Sending file or directory model for utility purposes available globally in the system.
 *
 * @author Ahsan Khan
 */
public class HttpSender implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -9081162269011948835L;

    /**
     * The Constant DESTINATION_URL.
     */
    private static final Object DESTINATION_URL = "Destination Url";

    /**
     * The Constant DESTINATION_FILE_PATH.
     */
    private static final Object DESTINATION_FILE_PATH = "Destination file";

    /**
     * The src file.
     */
    private File srcFile;

    /**
     * The chunk.
     */
    private long chunk;

    /**
     * The location.
     */
    private String location;

    /**
     * The ack bytes.
     */
    private long ackBytes;

    /**
     * The destination file path.
     */
    private String destinationFilePath;

    /**
     * The headers.
     */
    private Map< String, String > headers;

    /**
     * The check sum.
     */
    private String checkSum;

    /**
     * Gets the src file.
     *
     * @return the src file
     */
    public File getSrcFile() {
        return srcFile;
    }

    /**
     * Sets the src file.
     *
     * @param srcFile
     *         the new src file
     */
    public void setSrcFile( File srcFile ) {
        this.srcFile = srcFile;
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
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location
     *         the new location
     */
    public void setLocation( String location ) {
        this.location = location;
    }

    /**
     * Gets the ack bytes.
     *
     * @return the ack bytes
     */
    public long getAckBytes() {
        return ackBytes;
    }

    /**
     * Sets the ack bytes.
     *
     * @param ackBytes
     *         the new ack bytes
     */
    public void setAckBytes( long ackBytes ) {
        this.ackBytes = ackBytes;
    }

    /**
     * Gets the headers.
     *
     * @return the headers
     */
    public Map< String, String > getHeaders() {
        return headers;
    }

    /**
     * Sets the headers.
     *
     * @param headers
     *         the headers
     */
    public void setHeaders( Map< String, String > headers ) {
        this.headers = headers;
    }

    /**
     * Gets the destination file path.
     *
     * @return the destination file path
     */
    public String getDestinationFilePath() {
        return destinationFilePath;
    }

    /**
     * Sets the destination file path.
     *
     * @param destinationFilePath
     *         the new destination file path
     */
    public void setDestinationFilePath( String destinationFilePath ) {
        this.destinationFilePath = destinationFilePath;
    }

    /**
     * Gets the check sum.
     *
     * @return the check sum
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     * Sets the check sum.
     *
     * @param checkSum
     *         the new check sum
     */
    public void setCheckSum( String checkSum ) {
        this.checkSum = checkSum;
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notification = new Notification();
        if ( !getSrcFile().exists() ) {
            notification.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), getSrcFile().getAbsolutePath() ) ) );
        } else if ( StringUtils.isEmpty( getLocation() ) ) {
            notification
                    .addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), DESTINATION_URL ) ) );
        } else if ( !HttpUtils.pingUrl( getLocation() ) ) {
            notification.addError( new Error( MessageBundleFactory.getMessage( Messages.INVALID_URL_PROVIDED.getKey(), getLocation() ) ) );
        } else if ( StringUtils.isEmpty( getDestinationFilePath() ) ) {
            notification.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), DESTINATION_FILE_PATH ) ) );
        }
        return notification;
    }

}
