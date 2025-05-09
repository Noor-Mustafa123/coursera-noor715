package de.soco.software.simuspace.suscore.common.client;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/**
 * The Class ProgressEntityWrapper.
 */
public class ProgressEntityWrapper extends HttpEntityWrapper {

    /**
     * The listener interface for receiving progress events. The class that is interested in processing a progress event implements this
     * interface, and the object created with that class is registered with a component using the component's
     * <code>addProgressListener<code> method. When the progress event occurs, that object's appropriate method is invoked.
     *
     * @see ProgressEvent
     */
    public interface ProgressListener {

        /**
         * Progress.
         *
         * @param percentage
         *         the percentage
         */
        void progress( float percentage );

    }

    /**
     * The listener.
     */
    private ProgressListener listener;

    /**
     * Instantiates a new progress entity wrapper.
     *
     * @param entity
     *         the entity
     * @param listener
     *         the listener
     */
    public ProgressEntityWrapper( HttpEntity entity, ProgressListener listener ) {
        super( entity );
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTo( OutputStream outstream ) throws IOException {
        try ( CountingOutputStream countingOutputStream = new CountingOutputStream( outstream, listener, getContentLength() ) ) {
            super.writeTo( countingOutputStream );
        }
    }

}