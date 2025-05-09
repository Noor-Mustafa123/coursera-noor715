package de.soco.software.simuspace.suscore.local.daemon.model;

/**
 * The Class contains daemon API end points.
 *
 * @author Nasir.Farooq
 */

public class WorkflowEndPoints {

    /**
     * The Constant RERUN_JOB_ID_STOP.
     */
    public static final String RERUN_JOB_ID_STOP = "/rerun/{id}";

    public static final String DISCARD_JOB_ID_STOP = "/discard/{id}";

    /**
     * The Constant DISCARD_JOB_API.
     */
    public static final String DISCARD_JOB_API = "/discard/{id}";

    /**
     * The Constant API_V1_WORKFLOW.
     */
    public static final String API_WORKFLOW = "/api/workflow/";

    /**
     * The Constant API_JOB.
     */
    public static final String API_JOB = "api/job";

    /**
     * The Constant ISUP.
     */
    public static final String ISUP = "/isup";

    /**
     * The Constant shutdown.
     */
    public static final String SHUTDOWN = "/shutdown/pid/{pid}";

    /**
     * The Constant JOBS_FILES_RUNNING_LIST.
     */
    public static final String JOBS_FILES_RUNNING_LIST = "/jobs/files/running/list";

    /**
     * The Constant JOB_JOB_ID_STOP.
     */
    public static final String JOB_JOB_ID_STOP = "/job/{jobId}/stop";

    /**
     * The Constant JOBS_FILES_LIST.
     */
    public static final String JOBS_FILES_LIST = "/jobs/files/list";

    /**
     * The Constant RUNJOB.
     */
    public static final String RUNJOB = "runjob";

    /**
     * The Constant API_SUS_JOBS.
     */
    public static final String API_SUS_JOBS = "/sus/list";

    /**
     * The Constant API_LOCAL_JOBS.
     */
    public static final String API_LOCAL_JOBS = "/local/list";

    /**
     * The Constant API_SUS_JOB_TABLE_UI.
     */
    public static final String API_SUS_JOB_TABLE_UI = "/sus/ui";

    /**
     * The Constant API_FILES_JOB_TABLE_UI.
     */
    public static final String API_FILES_JOB_TABLE_UI = "/local/ui";

    /**
     * The Constant API_JOB_CONTEXT.
     */
    public static final String API_JOB_CONTEXT = "/sus/context";

    /**
     * The Constant API_JOB_CONTEXT.
     */
    public static final String API_LOCAL_JOB_CONTEXT = "/local/context";

    /**
     * The Constant GET_JOB_BY_ID.
     */
    public static final String GET_JOB_BY_ID = "/{jobId}";

    /**
     * The Constant GET_FILE_JOBS_API.
     */
    public static final String GET_FILE_JOBS_API = "/local";

    /**
     * The Constant SUS_JOBS_VIEW.
     */
    public static final String SUS_JOBS_VIEW = "/sus/ui/view";

    /**
     * The Constant SUS_UPDATE_JOBS_VIEW.
     */
    public static final String SUS_DELETE_OR_UPDATE_JOBS_VIEW = "/sus/ui/view/{viewId}";

    /**
     * The Constant SUS_JOBS_VIEW_AS_DEFAULT.
     */
    public static final String SUS_JOBS_VIEW_AS_DEFAULT = "/sus/ui/view/{viewId}/default";

    /**
     * The Constant FILE_JOBS_VIEW.
     */
    public static final String FILE_JOBS_VIEW = "/local/ui/view";

    /**
     * The Constant FILE_DELETE_OR_UPDATE_JOBS_VIEW.
     */
    public static final String FILE_DELETE_OR_UPDATE_JOBS_VIEW = "/local/ui/view/{viewId}";

    /**
     * The Constant FILE_JOBS_VIEW_AS_DEFAULT.
     */
    public static final String FILE_JOBS_VIEW_AS_DEFAULT = "/local/ui/view/{viewId}/default";

    /**
     * The Constant SUS_IMPORT_UI.
     */
    public static final String SUS_IMPORT_UI = "/import/ui/{parentId}";

    /**
     * The Constant SUS_IMPORT.
     */
    public static final String SUS_IMPORT = "/import";

    /**
     * Private constructor to hide implicit one.
     */
    private WorkflowEndPoints() {
        super();
    }

}
