package de.soco.software.simuspace.suscore.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * The class is used to close the resources.
 */
public class IOUtils {

    /**
     * Safely close the closeable resource.
     *
     * @param closeable
     *         the closeable
     */
    public static void safeClose( Closeable closeable ) {
        try {
            if ( closeable != null ) {
                closeable.close();
            }
        } catch ( final IOException e ) {
            ExceptionLogger.logException( e, IOUtils.class );
        }
    }

}
