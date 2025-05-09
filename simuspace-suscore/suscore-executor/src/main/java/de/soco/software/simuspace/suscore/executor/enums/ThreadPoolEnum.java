package de.soco.software.simuspace.suscore.executor.enums;

/**
 * The Enum ThreadPoolEnum. This enum corresponds to executor.json config file
 */
public enum ThreadPoolEnum {

    /**
     * The workflow.
     */
    WORKFLOW( "Workflow" ),

    /**
     * The archive.
     */
    ARCHIVE( "Archive" ),

    /**
     * The import.
     */
    IMPORT( "Import" ),

    /**
     * The export.
     */
    EXPORT( "Export" ),

    /**
     * The delete.
     */
    DELETE( "Delete" ),

    /**
     * The lifecycle.
     */
    LIFECYCLE( "Lifecycle" ),

    /**
     * The thumbnail.
     */
    THUMBNAIL( "Thumbnail" ),

    /**
     * The indexing.
     */
    INDEXING( "Indexing" ),

    /**
     * The dynamic hosts.
     */
    DYNAMIC_HOSTS( "DynamicHosts" ),

    /**
     * The system workflow.
     */
    SYSTEM_WORKFLOW( "SystemWorkflow" ),

    /**
     * The scheme.
     */
    SCHEME( "Scheme" ),

    /**
     * The ffmpeg.
     */
    FFMPEG( "ffmpeg" ),

    /**
     * The upload.
     */
    UPLOAD( "Upload" );

    /**
     * Instantiates a new thread pool enum.
     *
     * @param name
     *         the name
     */
    ThreadPoolEnum( String name ) {
        this.name = name;
    }

    /**
     * The name.
     */
    private final String name;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
