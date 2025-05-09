package de.soco.software.simuspace.suscore.common.constants;

import java.io.File;
import java.util.UUID;

/**
 * Utility class that will contain common Strings usable in all bundles.
 *
 * @author sces126
 */
public class ConstantsString {

    /**
     * The Constant COMMAND_KARAF_LOGGING_ON.
     */
    public static final boolean COMMAND_KARAF_LOGGING_ON = true;

    /**
     * The Constant COMMAND_KARAF_LOGGING_OFF.
     */
    public static final boolean COMMAND_KARAF_LOGGING_OFF = false;

    /**
     * The colon.
     */
    public static final String COLON = ":";

    /**
     * The semi colon
     */
    public static final String SEMI_COLON = ";";

    /**
     * The Constant UPLOAD.
     */
    public static final String UPLOAD = "Upload";

    /**
     * The Constant WORKFLOW_EXECUTION.
     */
    public static final String WORKFLOW_EXECUTION = "workflow_execution.cfg";

    /**
     * The Constant EXPORT_OBJECT.
     */
    public static final String EXPORT_OBJECT = "Export";

    /**
     * The Constant DOWNLOAD.
     */
    public static final String DOWNLOAD = "Download";

    /**
     * The comma.
     */
    public static final String COMMA = ",";

    /**
     * The curly left bracket.
     */
    public static final String CURLY_LEFT_BRACKET = "{";

    /**
     * The curly right bracket.
     */
    public static final String CURLY_RIGHT_BRACKET = "}";

    /**
     * The round left bracket.
     */
    public static final String ROUND_LEFT_BRACKET = "(";

    /**
     * The round right bracket.
     */
    public static final String ROUND_RIGHT_BRACKET = ")";

    /**
     * The current dir.
     */
    public static final String CURRENT_DIR = "user.dir";

    /**
     * The dollar.
     */
    public static final String DOLLAR = "$";

    /**
     * The dot.
     */
    public static final String DOT = ".";

    /**
     * The dot regex.
     */
    public static final String DOT_REGEX = "\\.";

    /**
     * The Constant ASTERISK.
     */
    public static final String ASTERISK = "*";

    /**
     * The Constant LEFT_SQUARE_BRACKET.
     */
    public static final String LEFT_SQUARE_BRACKET = "[";

    /**
     * The Constant RIGHT_SQUARE_BRACKET.
     */
    public static final String RIGHT_SQUARE_BRACKET = "]";

    /**
     * The empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The space string.
     */
    public static final String SPACE = " ";

    /**
     * The Constant SPACE_ALTERNATE.
     */
    public static final String SPACE_ALTERNATE = "%20";

    /**
     * The forward slash.
     */
    public static final String FORWARD_SLASH = "/";

    /**
     * The Constant DOUBLE_FORWARD_SLASH.
     */
    public static final String DOUBLE_FORWARD_SLASH = "//";

    /**
     * The hash.
     */
    public static final String HASH = "#";

    /**
     * The hibernate cfg.
     */
    public static final String HIBERNATE_CFG = "sus.hibernate.cfg";

    public static final String WEN_CFG = "sus.bmw.wen.cfg";

    /**
     * The hyphen.
     */
    public static final String HYPHEN = "-";

    /**
     * The DEFAULT_FILE_NAME_SEPERATOR.
     */
    public static final String DEFAULT_FILE_NAME_SEPERATOR = HYPHEN;

    /**
     * The minus one.
     */
    public static final String MINUS_ONE = "-1";

    /**
     * The modulus.
     */
    public static final String MODULUS = "%";

    /**
     * The new line.
     */
    public static final String NEW_LINE = "\n";

    /**
     * The pipe character.
     */
    public static final String PIPE_CHARACTER = "|";

    /**
     * The tab space.
     */
    public static final String TAB_SPACE = "\t";

    /**
     * The Linux Operating system.
     */
    public static final String OS_LINUX = "Linux";

    /**
     * The Operating system name.
     */
    public static final String OS_NAME = "os.name";

    /**
     * The Windows Operating system.
     */
    public static final String OS_WINDOWS = "Windows";

    /**
     * The simspace.
     */
    public static final String SIMUSPACE = "simuspace";

    /**
     * The Windows Operating system prefix.
     */
    public static final String WINDOWS_PREFIX = "win";

    /**
     * The Windows charset.
     */
    public static final String CHARSET_WINDOWS = "windows-1252";

    /**
     * The token.
     */
    public static final String TOKEN = "token";

    /**
     * The UT f8.
     */
    public static final String UTF8 = "utf8";

    /**
     * The forward slash.
     */
    public static final String DELIMETER_FOR_NIX = "/";

    /**
     * ISO 8859 1 property.
     */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /**
     * The Constant WIN_C_PARAM.
     */
    public static final String WIN_C_PARAM = "/c";

    /**
     * The Constant WIN_T_PARAM.
     */
    public static final String WIN_T_PARAM = "/t";

    /**
     * The Constant WIN_EXE.
     */
    public static final String WIN_EXE = "cmd.exe";

    /**
     * The Constant WIN_EXE.
     */
    public static final String NIX_BASH = "/bin/sh";

    /**
     * The nix c param.
     */
    public static final String NIX_C_PARAM = "-c";

    /**
     * The directory.
     */
    public static final String DIRECTORY = "cd";

    /**
     * The back slash.
     */
    public static final String DELIMETER_FOR_WIN = "\\";

    /**
     * The back slash.
     */
    public static final String PATH_SEPARATOR_WIN = "\\\\";

    /**
     * The space.
     */
    public static final String STANDARD_SEPARATOR = " ";

    /**
     * The Constant HOME_DIRECTORY for home directory of machine.
     */
    public static final String HOME_DIRECTORY = "user.home";

    /**
     * The less than sign.
     */
    public static final String LESS_THAN = "<";

    /**
     * request header containing user agent information.
     */
    public static final String USER_AGENT_HEADER = "User-Agent";

    /**
     * The greater than sign.
     */
    public static final String GREATER_THAN = ">";

    /**
     * The equals sign.
     */
    public static final String EQUALS_OPERATOR = "=";

    /**
     * test resource path.
     */
    public static final String TEST_RESOURCE_PATH = "src" + File.separator + "test" + File.separator + "resources" + File.separator;

    /**
     * Constant to be used in DB queries where ordering is required.
     */
    public static final String SORTING_DIRECTION_ASCENDING = "asc";

    /**
     * Constant to be used in DB queries where ordering is required.
     */
    public static final String SORTING_DIRECTION_DESCENDING = "desc";

    /**
     * default extension to be used for images.
     */
    public static final String DEFAULT_IMAGE_TYPE = "jpg";

    /**
     * The Constant FILE_TYPE_VIDEO.
     */
    public static final String FILE_TYPE_VIDEO = "video";

    /**
     * The Constant JSON_EXTENSION.
     */
    public static final String JSON_EXTENSION = ".json";

    public static final String ZIP_EXTENSION = ".zip";

    /**
     * The Constant SIMUSPACE_SYSTEM_DIRECTORY name in user home.
     */
    public static final String SIMUSPACE_SYSTEM_DIRECTORY = "SIMuSPACE";

    /**
     * The Constant CONFIG for folder in user home.
     */
    public static final String CONFIG = "config";

    /**
     * The Constant DAEMON_LOGGER is used to log messages from daemon bundle.
     */
    public static final String DAEMON_LOGGER = "DaemonLogger";

    /**
     * Error message when a string cannot be converted to a UUID object.
     */
    public static final String INVALID_UUID_MESSAGE = "Can not construct instance of java.util.UUID from String";

    /**
     * string representing single backslash.
     */
    public static final CharSequence SINGLE_BACKSLASH = "\\";

    /**
     * string representing double backslash.
     */
    public static final CharSequence DOUBLE_BACKSLASH = "\\\\";

    /**
     * invalid credentials message.
     */
    public static final String INVALID_CREDENTIALS = "Invalid Credentials";

    /**
     * invalid dn.
     */
    public static final String INVALID_DN = "invalid DN";

    /**
     * string representing the unique constraint on license for user to module.
     */
    public static final String USER_TO_MODULE_UNIQUE_CONSTRAINT = "user_to_module_unique_constraint";

    /**
     * The Constant FILTER_VALUE_REPLACE_CHARACTER.
     */
    public static final String FILTER_VALUE_ESCAPE_ASTERISK_CHARACTER = "\\*";

    /**
     * The Constant STANDARD_SINGLE_QUOTE.
     */
    public static final String STANDARD_SINGLE_QUOTE = "'";

    /**
     * The Constant DOUBLE_QUATE_STRING.
     */
    public static final String DOUBLE_QUOTE_STRING = "\"";

    /**
     * The Constant BACK_SLASHED_DOULBLE_QUOTES.
     */
    public static final String BACK_SLASHED_DOULBLE_QUOTES = "\\\"";

    /**
     * The Constant BACK_SLASHED_CURLY_LEFT_BRACKET.
     */
    public static final String BACK_SLASHED_CURLY_LEFT_BRACKET = "\\{";

    /**
     * The Constant STRING_VALUE_ZERO.
     */
    public static final String STRING_VALUE_ZERO = "0";

    /**
     * The Constant LINK .
     */
    public static final String LINK_UI_KEY = "link";

    /**
     * The Constant PARSER_SELECTED_FILE_NAME.
     */
    public static final String PARSER_SELECTED_FILE_NAME = "SELECTED_PARSED.json";

    /**
     * The Constant OBJECT_KEY.
     */
    public static final String OBJECT_KEY = "object";

    /**
     * The Constant PROJECT_KEY.
     */
    public static final String PROJECT_KEY = "project";

    /**
     * The Constant WORKFLOW_KEY.
     */
    public static final String WORKFLOW_KEY = "workflow";

    /**
     * The Constant WORKFLOW_PROJECT_KEY.
     */
    public static final String WORKFLOW_PROJECT_KEY = "workflowproject";

    /**
     * The Constant USER PROFILE .
     */
    public static final String USERPROFILE = "User Profile";

    /**
     * The Constant PROJECT_CONF_FILE_NAME.
     */
    public static final String PROJECT_CONF_FILE_NAME = "project.json";

    /**
     * The Constant DAEMON_CONF_FILE_NAME.
     */
    public static final String DAEMON_CONF_FILE_NAME = "daemon_thread_pool.cfg";

    /**
     * The Constant SYNC_CONF_FILE_NAME.
     */
    public static final String SYNC_CONF_FILE_NAME = "sync.json";

    /**
     * The Constant APPS_CONF_FILE_NAME.
     */
    public static final String APPS_CONF_FILE_NAME = "apps.json";

    /**
     * The Constant Ffmpeg_CONF_FILE_NAME.
     */
    public static final String Ffmpeg_CONF_FILE_NAME = "ffmpeg.json";

    /**
     * The Constant STATIC_PATH.
     */
    public static final String STATIC_PATH = "static";

    public static final String TMP_STATIC_PATH = "tmp-static";

    /**
     * The Constant OBJECT_THUMB_NAIL_FILE.
     */
    public static final String OBJECT_THUMB_NAIL_FILE_POSTFIX = "thumb";

    /**
     * The Constant FILE_TYPE_JSON.
     */
    public static final String FILE_TYPE_JSON = "json";

    /**
     * The base path for 3Dmodels on fee-static
     */
    public static final String BASE_PATH_3D_MODEL = "cee";

    /**
     * The prefix for 3d models folders
     */
    public static final String PREFIX_3D_MODEL_FOLDER = "modelKey#";

    /**
     * The Constant to get execution time.
     */
    public static final String MAXIMUM_EXECUTION_TIME = "max-execution-time";

    /**
     * The Constant STOP_ON_ERROR.
     */
    public static final String STOP_ON_ERROR = "stoponerror";

    /**
     * The Constant DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR.
     */
    public static final String DEFAULT_VALUE_FOR_WORKFLOW_STOP_ON_ERROR = "yes";

    /**
     * The Constant WF_LOGGER is used to log system level logging information.
     */
    public static final String WF_LOGGER = "WorkFlowLogger";

    /**
     * The Constant WF_LOGGER_SUMMARY.
     */
    public static final String WF_LOGGER_SUMMARY = "WorkFlowLoggerSummary";

    /**
     * The wf engine.
     */
    public static final String WF_ENGINE = "wfengine.cfg";

    /**
     * The sys cmd indication.
     */
    public static final String SYS_CMD_INDICATION = "{{#";

    /**
     * The Constant EncodingConst for linux.
     */
    public static final String ENCODING_CONST_FORNIX = "utf-8";

    /**
     * The Constant EncodingConst for windows.
     */
    public static final String ENCODING_CONST_FORWIN = "CP1252";

    /**
     * The Constant NIX_QUOTE_STANDARD.
     */
    public static final String NIX_QUOTE_STANDARD = "\'";

    /**
     * The Constant WIN_QUOTE_STANDARD.
     */
    public static final String WIN_QUOTE_STANDARD = "\"";

    /**
     * The Constant for timeout of command.
     */
    public static final String TIMEOUT_FOR_ELEMENT = "timeout";

    /**
     * The Constant SIMUSPACE_SYSTEM_LOG_DIRECTORY name in user home.
     */
    public static final String SIMUSPACE_SYSTEM_LOG_DIRECTORY = SIMUSPACE_SYSTEM_DIRECTORY + "/logs";

    /**
     * The job dir.
     */
    public static final String JOB_DIR = "Job Directory";

    /**
     * The workflow config file name.
     */
    public static final String CONFIG_FILE = "Workflow_config.json";

    /**
     * The hpc config file name.
     */
    public static final String HPC_CONFIG_FILE = "Hpc_config.json";

    /**
     * The email config file name.
     */
    public static final String EMAIL_CONFIG_FILE = "/email.conf";

    /**
     * The Constant DEFAULT_BE_USER_ID.
     */
    public static final UUID DEFAULT_BE_USER_ID = java.util.UUID.randomUUID();

    /**
     * The pid executors.
     */
    public static final String PID_EXECUTORS = "sim.executors";

    /**
     * The pid simuspace.
     */
    public static final String PID_SIMUSPACE = "sim.simuspace";

    /**
     * The process schedule core poolsize.
     */
    public static final String PROCESSSCHEDULE_CORE_POOLSIZE = "processscheduler.core.pool.size";

    /**
     * The process schedule initial delay.
     */
    public static final String PROCESSSCHEDULE_INITIAL_DELAY = "processscheduler.initial.delay";

    /**
     * The process schedule interval.
     */
    public static final String PROCESSSCHEDULE_INTERVAL = "processscheduler.interval";

    /**
     * The schedule core poolsize.
     */
    public static final String SCHEDULE_CORE_POOLSIZE = "scheduler.core.pool.size";

    /**
     * The schedule initial delay.
     */
    public static final String SCHEDULE_INITIAL_DELAY = "scheduler.initial.delay";

    /**
     * The schedule interval.
     */
    public static final String SCHEDULE_INTERVAL = "scheduler.interval";

    /**
     * The monitor core poolsize.
     */
    public static final String MONITOR_CORE_POOLSIZE = "monitor.core.pool.size";

    /**
     * The monitor initial delay.
     */
    public static final String MONITOR_INITIAL_DELAY = "monitor.initial.delay";

    /**
     * The monitor interval.
     */
    public static final String MONITOR_INTERVAL = "monitor.interval";

    /**
     * The Constant ARCHIVE.
     */
    public static final String ARCHIVE = "Archive";

    /**
     * The Constant IMPORT.
     */
    public static final String IMPORT = "Import";

    /**
     * The Constant EXPORT.
     */
    public static final String EXPORT = "Export";

    /**
     * The Constant WORKFLOW.
     */
    public static final String WORKFLOW = "Workflow";

    /**
     * The Constant WORKFLOW.
     */
    public static final String SCHEME = "Scheme";

    /**
     * The Constant SYSTEM_WORKFLOW.
     */
    public static final String SYSTEM_WORKFLOW = "SystemWorkflow";

    /**
     * The Constant DELETE.
     */
    public static final String DELETE = "Delete";

    /**
     * The Constant DYNAMIC_HOST.
     */
    public static final String DYNAMIC_HOST = "DynamicHosts";

    /**
     * The Constant RESTORE.
     */
    public static final String RESTORE = "Restore";

    /**
     * The Constant LIFECYCLE.
     */
    public static final String LIFECYCLE = "Lifecycle";

    /**
     * The Constant THUMBNAIL.
     */
    public static final String THUMBNAIL = "Thumbnail";

    /**
     * The Constant FFMPEG.
     */
    public static final String FFMPEG = "ffmpeg";

    /**
     * The Constant INDEXING.
     */
    public static final String INDEXING = "Indexing";

    /**
     * The Constant CONIGFILE.
     */
    public static final String CONIGFILE = "config.json";

    /**
     * The Constant SERVERFILE.
     */
    public static final String SERVERFILE = "server.json";

    /**
     * The Constant ABOUT_MENU_ID.
     */
    public static final String ABOUT_MENU_ID = "62db977b-e952-4d60-91e2-e8d44e1ddf07";

    /**
     * The Constant ENPT_DECPT_ID.
     */
    public static final String ENPT_DECPT_ID = "3a3739a2-5de3-48e1-9f54-6b78be3c728c";

    /**
     * The Constant SERVER.
     */
    public static final String SERVER = "server";

    /**
     * The Constant LOCAL.
     */
    public static final String LOCAL = "local";

    /**
     * The Constant NIX_SHELL.
     */
    public static final String CREATED = "Created";

    /**
     * The Constant NIX_SHELL.
     */
    public static final String UPDATED = "Updated";

    /**
     * The Constant NIX_SHELL.
     */
    public static final String NIX_SHELL = "/bin/bash";

    /**
     * The Constant NOT_AVAILABLE.
     */
    public static final String NOT_AVAILABLE = "n/a";

    /**
     * The Constant PROJECT_TYPE.
     */
    public static final String PROJECT_TYPE = "Data";

    /**
     * The Constant SUS_SI_SYSTEM_UNITS_FILE.
     */
    public static final String SUS_SI_SYSTEM_UNITS_FILE = "SuSSISystem.json";

    /**
     * The Constant SUS_PARSER_JSON.
     */
    public static final String SUS_PARSER_JSON = "sus.parser.json";

    /**
     * The Constant SUS_LANGUAGE_JSON.
     */
    public static final String SUS_LANGUAGE_JSON = "sus.languages.json";

    /**
     * The Constant DYNAMIC_PROPERTIES.
     */
    public static final String DYNAMIC_PROPERTIES = "dynamic_properties.json";

    /**
     * The Constant IMG_MOV_FORMAT_PROPERTIES.
     */
    public static final String IMG_MOV_FORMAT_PROPERTIES = "ImageMovieFormat.json";

    /**
     * The Constant Assembly Method.
     */
    public static final String ASSEMBLY_METHOD = "Assembly Version";

    /**
     * The Constant Solver Method.
     */
    public static final String SOLVER_METHOD = "Solver Version";

    /**
     * The Constant Post Method.
     */
    public static final String POST_METHOD = "Post Version";

    /**
     * The Constant GENERATE_IMAGE.
     */
    public static final String GENERATE_IMAGE = "Generate Image";

    /**
     * The Constant PLOT_CORRELATION.
     */
    public static final String PLOT_CORRELATION = "Plot correlation";

    /**
     * The Constant PLOT_BUBBLE_CHART.
     */
    public static final String PLOT_BUBBLE_CHART = "Plot Bubble chart";

    /**
     * The Constant HEATMAP.
     */
    public static final String HEATMAP = "Heatmap";

    /**
     * The Constant GENERATE_IMAGE_KEY.
     */
    public static final String GENERATE_IMAGE_KEY = "4100052x4";

    /**
     * The Constant PLOT_CORRELATION_KEY.
     */
    public static final String PLOT_CORRELATION_KEY = "4100051x4";

    /**
     * The Constant PLOT_BUBBLE_CHART_KEY.
     */
    public static final String PLOT_BUBBLE_CHART_KEY = "4100050x4";

    /**
     * The Constant HEATMAP_KEY.
     */
    public static final String HEATMAP_KEY = "4100049x4";

    /**
     * The Constant INTEGER.
     */
    public static final String INTEGER_TYPE = "Integer";

    /**
     * The Constant FLOAT.
     */
    public static final String FLOAT_TYPE = "Float";

    /**
     * The Constant STRING.
     */
    public static final String STRING_TYPE = "String";

    /**
     * The Constant ENCP_OFF.
     */
    public static final String ENCP_OFF = "OFF";

    /**
     * The Constant UNDERSCORE.
     */
    public static final String UNDERSCORE = "_";

    /**
     * The Constant CUSTOM_ATTRIBUTES_FIELD_NAME.
     */
    public static final String CUSTOM_ATTRIBUTES_FIELD_NAME = "customAttributes.";

    /**
     * The Constant DEFAULT_LANGUAGE.
     */
    public static final String DEFAULT_LANGUAGE = "en";

    /**
     * The Constant SUS_VARIABLE_PREFIX.
     */
    public static final String SUS_VARIABLE_PREFIX = "{{";

    /**
     * The constant STATUS.
     */
    public static final String STATUS = "status";

    /**
     * The constant TYPE.
     */
    public static final String TYPE = "type";

    /**
     * The constant TEXT.
     */
    public static final String TEXT = "text";

    /**
     * The constant UUID.
     */
    public static final String UUID = "uuid";

    /**
     * The constant DETAILS.
     */
    public static final String DETAILS = "details";

    public static final String OR_CONDITION = "OR";

    /**
     * private constructor to prevent initialization of class.
     */
    private ConstantsString() {
    }

}