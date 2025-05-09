package de.soco.software.simuspace.susdash.core.constants;

import java.io.File;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class HpcJobConstants.
 */
public class HpcJobConstants {

    /**
     * Instantiates a new hpc job constants.
     */
    private HpcJobConstants() {
        // private constructor to avoid instantiation
    }

    /** ********** PYTHON ARGUMENTS **********. */

    /**
     * The Constant UGE_DATA_PYTHON_ARGUMENT.
     */
    public static final String UGE_DATA = "UGE_Data";

    /**
     * The Constant PENDING.
     */
    public static final String PENDING = "Pending";

    /**
     * The Constant FILES.
     */
    public static final String FILES = "Files";

    /**
     * The Constant MONITOR.
     */
    public static final String MONITOR = "Monitor";

    /**
     * The Constant ACTION.
     */
    public static final String ACTION = "-act";

    /**
     * The Constant PD.
     */
    public static final String PD = "-pd";

    /**
     * The Constant DST.
     */
    public static final String EP = "-ep";

    /**
     * The Constant JD.
     */
    public static final String JD = "-jd";

    /**
     * The Constant FD.
     */
    public static final String FD = "-fd";

    /**
     * The Constant JID.
     */
    public static final String JID = "-jID";

    /**
     * The Constant SIM.
     */
    public static final String SIM = "-sim";

    /**
     * The Constant FILTER.
     */
    public static final String FILTER = "-filter";

    /** **************************************. */

    /** ********** URL ********** */

    /**
     * The Constant BREADCRUMB_JOB_CONTEXT_URL.
     */
    public static final String BREADCRUMB_JOB_CONTEXT_URL = "/plugins/hpc/uge/job/%s/jobs/context";

    /**
     * The Constant BREADCRUMB_JOB_NAME_CONTEXT_URL.
     */
    public static final String BREADCRUMB_JOB_NAME_CONTEXT_URL = "/plugins/hpc/uge/job/%s/%s/context";

    /** *************************. */

    /** ********** VARIABLE MATCH ********** */

    /**
     * The Constant JB_NUMBER.
     */
    public static final String JB_NUMBER = "$.JB_job_number";

    /**
     * The Constant JB_NAME.
     */
    public static final String JB_NAME = "$.JB_name";

    /**
     * The Constant JB_OWNER.
     */
    public static final String JB_OWNER = "$.JB_owner";

    /**
     * The Constant JB_APPLICATION.
     */
    public static final String JB_APPLICATION = "$.Application";

    /**
     * The Constant JB_PROJECT.
     */
    public static final String JB_PROJECT = "$.JB_project";

    /**
     * The Constant JB_SLOTS.
     */
    public static final String JB_SLOTS = "$.slots";

    /**
     * The Constant JB_QUEUE.
     */
    public static final String JB_QUEUE = "$.queue_name";

    /**
     * The Constant JB_STATE.
     */
    public static final String JB_STATE = "$.@state";

    /**
     * The Constant JB_SUBMIT_TIME.
     */
    public static final String JB_SUBMIT_TIME = "$.submittime";

    /**
     * The Constant JB_DISKSPACE.
     */
    public static final String JB_DISKSPACE = "$.diskspace";

    /**
     * The Constant JB_START_TIME.
     */
    public static final String JB_START_TIME = "$.jobstarttime";

    /**
     * The Constant JB_CALC_START_TIME.
     */
    public static final String JB_CALC_START_TIME = "$.calcstarttime";

    /**
     * The Constant JB_WORKING_DIR.
     */
    public static final String JB_WORKING_DIR = "$.workingdirectory";

    /**
     * The Constant JB_RESULT_DIR.
     */
    public static final String JB_RESULT_DIR = "$.resultdirectory";

    /**
     * The Constant JB_APPL_VERSION.
     */
    public static final String JB_APPL_VERSION = "$.applversion";

    /**
     * The Constant JB_FS_DIR.
     */
    public static final String JB_FS_DIR = "$.fsdirectory";

    /**
     * The Constant JB_WF_HOME.
     */
    public static final String JB_WF_HOME = "$.wfhome";

    /**
     * The Constant JB_WF_SITE.
     */
    public static final String JB_WF_SITE = "$.wfsite";

    /**
     * The Constant JB_HOST.
     */
    public static final String JB_HOST = "$.host";

    /**
     * The Constant JB_HOST_NAME.
     */
    public static final String JB_HOST_NAME = "$.hostname";

    /**
     * The Constant JB_PARALLEL_ENV.
     */
    public static final String JB_PARALLEL_ENV = "$.parallelenv";

    /**
     * The Constant JB_NODES.
     */
    public static final String JB_NODES = "$.nodes";

    /**
     * The Constant JB_WORKFLOW.
     */
    public static final String JB_WORKFLOW = "$.workflow";

    /** ************************************. */

    /** ********** SUBTABS ********** */

    /**
     * The Constant PROPERTIES_SUBTAB.
     */
    public static final String PROPERTIES_SUBTAB = "Properties";

    /**
     * The Constant FILES_SUBTAB.
     */
    public static final String FILES_SUBTAB = "Files";

    /**
     * The Constant MONITOR_SUBTAB.
     */
    public static final String MONITOR_SUBTAB = "Monitor";

    /**
     * The Constant PENDING_MESSAGES_SUBTAB.
     */
    public static final String PENDING_MESSAGES_SUBTAB = "pending_messages";

    /** ****************************. */

    /** ********** COLUMN DATA ********** */

    /**
     * The Constant NAME_FIELD.
     */
    public static final String JOB_NAME_FIELD = "JB_name";

    /**
     * The Constant SGE_JOBS.
     */
    public static final String SGE_JOBS = "sge_jobs";

    /**
     * The Constant TOTAL_JOBS_TAG.
     */
    public static final String TOTAL_JOBS_TAG = "total_jobs";

    /**
     * The Constant FILTERED_JOBS_TAG.
     */
    public static final String FILTERED_JOBS_TAG = "filtered_jobs";

    /** ********************************. */

    /** ********** JOB STATE ********** */

    /**
     * The Constant JOB_STATE_RUNNING.
     */
    public static final String JOB_STATE_RUNNING = "running";

    /**
     * The Constant JOB_STATE_PENDING.
     */
    public static final String JOB_STATE_PENDING = "pending";

    /** *******************************. */

    /** ********** DASHBOARD PLUGIN KEYS ********** */

    /**
     * The Constant JOB_DATA_FILE_NAME.
     */
    public static final String JOB_DATA_FILE_NAME = "job_data.json";

    /**
     * The Constant PENDING_DATA_FILE_NAME.
     */
    public static final String PENDING_DATA_FILE_NAME = "pending_data.json";

    /**
     * The Constant FILES_DATA_FILE_NAME.
     */
    public static final String FILES_DATA_FILE_NAME = "files_data.json";

    /**
     * The Constant ENERGY_CURVE_FILE_NAME.
     */
    public static final String ENERGY_CURVE_FILE_NAME = "hpc_job_monitoring.json";

    /**
     * The Constant HPC_JOB_BASE_KEY.
     */
    public static final String HPC_JOB_BASE_KEY = "hpcjob_base";

    /**
     * The Constant HPC_JOB_MONITORING_KEY.
     */
    public static final String HPC_JOB_MONITORING_KEY = "hpc_jobs_monitoring";

    /** *********************************************. */

    /** ********** PYTHON LOGGER REGEX ********** */

    /**
     * The Constant PYTHON_LOGGER_INFO_REGEX.
     */
    public static final String PYTHON_LOGGER_INFO_REGEX = "(^.*)(INFO)(.*$)";

    /**
     * The Constant PYTHON_LOGGER_CRITICAL_REGEX.
     */
    public static final String PYTHON_LOGGER_CRITICAL_REGEX = "(^.*)(CRITICAL)(.*$)";

    /**
     * The Constant PYTHON_LOGGER_ERROR_REGEX.
     */
    public static final String PYTHON_LOGGER_ERROR_REGEX = "(^.*)(ERROR)(.*$)";

    /**
     * The Constant INFO.
     */
    public static final String INFO = "INFO";

    /**
     * The Constant CRITICAL.
     */
    public static final String CRITICAL = "CRITICAL";

    /**
     * The Constant ERROR.
     */
    public static final String ERROR = "ERROR";

    /** ******************************************. */

    /**
     * The Constant HPC_MONITOR_CONF.
     */
    public static final String HPC_MONITOR_CONF = "HPCMonitor.json";

    /**
     * The Constant HPC_MONITOR_TEMP_PATH.
     */
    public static final String HPC_MONITOR_TEMP_PATH = PropertiesManager.getDefaultServerTempPath() + File.separator + "hpc_job_monitor";

    /**
     * The Constant HPC_MONITOR_CFG_HPC_JOB_ID.
     */
    public static final String HPC_MONITOR_CFG_HPC_JOB_ID = "6576baa0-6fbf-11ed-a1eb-0242ac120002";

    /**
     * The Constant PENDING_MESSAGES.
     */
    public static final String PENDING_MESSAGES = "PendingMessages";

    public static final String KILL_JOB_ACTION = "Kill_Job";

}
