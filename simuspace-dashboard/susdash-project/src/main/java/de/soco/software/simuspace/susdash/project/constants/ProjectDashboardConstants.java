package de.soco.software.simuspace.susdash.project.constants;

import java.io.File;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The type Project dashboard constants.
 */
public class ProjectDashboardConstants {

    /**
     * The constant ICICLE_CHART.
     */
    public static final String ICICLE_CHART = "project_dashboard_icicle_chart";

    /**
     * The constant MAX_DEPTH_KEY.
     */
    public static final String MAX_DEPTH_KEY = "maxDepth";

    /**
     * The constant UPDATE_INTERVAL_KEY.
     */
    public static final String UPDATE_INTERVAL_KEY = "updateInterval";

    /**
     * The constant INTERVAL_UNIT_KEY.
     */
    public static final String INTERVAL_UNIT_KEY = "updateIntervalUnit";

    /**
     * The constant LEVEL.
     */
    public static final String LEVEL = "level";

    /**
     * The constant URL_FORMAT.
     */
    public static final String URL_FORMAT = "view/data/{type}/{id}";

    /**
     * The constant TYPE_PROJECT.
     */
    public static final String TYPE_PROJECT = "project";

    /**
     * The constant TYPE_OBJECT.
     */
    public static final String TYPE_OBJECT = "object";

    /**
     * The constant PARAM_TYPE.
     */
    public static final String PARAM_TYPE = "{type}";

    /**
     * The constant PARAM_ID.
     */
    public static final String PARAM_ID = "{id}";

    /**
     * The constant ICICLE_CHART_FILE.
     */
    public static final String ICICLE_CHART_FILE = "chart.json";

    /**
     * The constant CACHE_FILE_NAME.
     */
    public static final String CACHE_FILE_NAME = "cache.json";

    /**
     * The constant LOCK_FILE_NAME.
     */
    public static final String LOCK_FILE_NAME = "cache.json.lck";

    /**
     * The constant DASHBOARD_CACHE_BASE_PATH.
     */
    public static final String DASHBOARD_CACHE_BASE_PATH = PropertiesManager.getDefaultServerTempPath() + File.separator
            + "project_dashboard_cache";

    public static final String UPDATE_LIFECYCLE_URL_FORMAT = "/dashboard/project/%s/selection/%s/lifecycle/%s";

}
