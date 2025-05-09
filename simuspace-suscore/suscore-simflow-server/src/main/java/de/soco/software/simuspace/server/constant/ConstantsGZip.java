package de.soco.software.simuspace.server.constant;

/**
 * Utility class which will contain all the constants for GZIP
 *
 * @author Zeeshan Jamal
 */
public class ConstantsGZip {

    /**
     * The min content size to gzip.
     */
    public static final int MIN_CONTENT_SIZE_TO_GZIP = 200; // Content size in byte

    /**
     * Private constructor to prevent instantiation.
     */
    private ConstantsGZip() {
        // Private constructor to hide the implicit public one.
    }

}
