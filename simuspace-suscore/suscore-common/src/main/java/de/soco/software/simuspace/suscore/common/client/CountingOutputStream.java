package de.soco.software.simuspace.suscore.common.client;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.soco.software.simuspace.suscore.common.client.ProgressEntityWrapper.ProgressListener;

/**
 * The Class CountingOutputStream.
 */
public class CountingOutputStream extends FilterOutputStream {

    /**
     * The listener.
     */
    private ProgressListener listener;

    /**
     * The transferred.
     */
    private long transferred;

    /**
     * The total bytes.
     */
    private long totalBytes;

    /**
     * Instantiates a new counting output stream.
     *
     * @param out
     *         the out
     * @param listener
     *         the listener
     * @param totalBytes
     *         the total bytes
     */
    public CountingOutputStream( OutputStream out, ProgressListener listener, long totalBytes ) {
        super( out );
        this.listener = listener;
        transferred = 0;
        this.totalBytes = totalBytes;
    }

    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    @Override
    public void write( byte[] b, int off, int len ) throws IOException {
        out.write( b, off, len );
        transferred += len;
        listener.progress( getCurrentProgress() );
    }

    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(int)
     */
    @Override
    public void write( int b ) throws IOException {
        out.write( b );
        transferred++;
        listener.progress( getCurrentProgress() );
    }

    /**
     * Gets the current progress.
     *
     * @return the current progress
     */
    private float getCurrentProgress() {
        return ( ( float ) transferred / totalBytes ) * 100;
    }

}