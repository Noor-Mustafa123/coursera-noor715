package de.soco.software.simuspace.suscore.common.constants;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.OSValidator;

/**
 * A utility class that would hold the constants for user related configuration file properties.
 *
 * @author Zeeshan jamal
 */
public class ConstantsFileProperties {

    /**
     * the super user configuration file.
     */
    public static final String SUPER_USER_CFG = "sus.superuser.cfg";

    /**
     * the super user, user name.
     */
    public static final String SUPER_USER_NAME_PROPERTY = "suscore.user.name";

    /**
     * the super user, user password.
     */
    public static final String SUPER_USER_PASS_PROPERTY = "suscore.user.password";

    /**
     * the super user, user status.
     */
    public static final String SUPER_USER_STATUS_PROPERTY = "suscore.user.status";

    /**
     * the imuspace configuration.
     */
    public static final String SUSCORE_SIMUSPACE_CFG = "sus.simuspace.cfg";

    /**
     * The Constant SUSCORE_ELASTICSEARCH_CFG.
     */
    public static final String SUSCORE_ELASTICSEARCH_CFG = "sus.elastic.cfg";

    /**
     * The Constant SUSCORE_ELASTICSEARCH_CFG.
     */
    public static final String SUSCORE_SHAPEMODULE_CFG = "sus.sm.cfg";

    /**
     * The Constant SHAPEMODULE_ENABLE.
     */
    public static final String SHAPEMODULE_ENABLE = "smschema.enable";

    /**
     * The Constant SHAPEMODULE_PATH.
     */
    public static final String SHAPEMODULE_PATH = "smschema.path";

    /**
     * The Constant SHAPEMODULE_URL.
     */
    public static final String SHAPEMODULE_URL = "smschema.url";

    /**
     * The Constant SUSCORE_HPC_COSTCENTERS_CFG.
     */
    public static final String SUSCORE_HPC_COSTCENTERS_CFG = "sus.hpc_costcenters.cfg";

    /**
     * The Constant ELS_URL.
     */
    public static final String ELS_URL = "elasticsearch.url";

    /**
     * The Constant ELS_URL.
     */
    public static final String ELS_REINDEX = "elasticsearch.reindex";

    /**
     * The Constant ELS_URL.
     */
    public static final String ELS_ENABLE = "elasticsearch.enable";

    /**
     * The Constant SUSCORE_AUDIT_CFG.
     */
    public static final String SUSCORE_AUDIT_CFG = "sus.audit.cfg";

    /**
     * The constant SUS_DASHBOARDS_CFG.
     */
    public static final String SUS_DASHBOARDS_CFG = "sus.dashboards.cfg";

    /**
     * The Constant SUSCORE_EXECUTION_HOST.
     */
    public static final String SUSCORE_EXECUTION_HOST = "sus.execution_host.json";

    /**
     * The constant BLACKLIST_TXT.
     */
    public static final String BLACKLIST_TXT = "blacklist.txt";

    /**
     * The Constant SUSCORE_DASHBOARD.
     */
    public static final String DASHBOARD_PLUGINS_JSON = "dashboard_plugins.json";

    public static final String HOMEPAGE_JSON = "Homepage.json";

    /**
     * The Constant SUSCORE_DASHBOARD.
     */
    public static final String OVERVIEW_PLUGINS_JSON = "overview_plugins.json";

    /**
     * The Constant CUSTOM_VIEWS.
     */
    public static final String CUSTOM_VIEWS = "custom_views.json";

    /**
     * The Constant FILE_EXTENSIONS.
     */
    public static final String FILE_EXTENSIONS = "file_extensions.json";

    /**
     * The Constant SUSCORE_DASHBOARD.
     */
    public static final String SUSCORE_DUMMY = "dummy_config.json";

    public static final String QA_DYNA_EZ_FIELDS = "run_QADyna.json";

    /**
     * The Constant SUSCORE_CB2_CFG.
     */
    public static final String SUSCORE_CB2_CFG = "sus.cb2.cfg";

    /**
     * The Constant WEB_EAM_CFG.
     */
    public static final String WEB_EAM_CFG = "sus.bmw.wen.cfg";

    /**
     * The constant VMCL_CFG.
     */
    public static final String VMCL_CFG = "sus.vmcl.cfg";

    /**
     * The Constant SUSCORE_CEETRON_CFG.
     */
    public static final String CEETRON_JSON = "ceetron.json";

    /**
     * The Constant SUS_ACTIONS.
     */
    public static final String SUS_ACTIONS = "dynamic_scripts.json";

    /**
     * The Constant PY_ENV.
     */
    public static final String PY_ENV = "py.env.json";

    /**
     * The Constant SUSCORE_SCHEME_PLOT.
     */
    public static final String SUSCORE_SCHEME_PLOT = "scheme_plot.json";

    /**
     * The session expiry time.
     */
    public static final String USER_SESSION_EXPIRY = "suscore.session.expiry";

    /**
     * The constant VMCL_PROJECT_SCRIPT_WRT_ENGINE.
     */
    public static final String VMCL_PROJECT_SCRIPT_WRT_ENGINE = "script.path.wrt.engine";

    /**
     * The Constant VAULT_PATH.
     */
    public static final String VAULT_PATH = "vault.path";

    /**
     * The constant SCRIPTS_BASE_PATH.
     */
    public static final String MISC_BASE_PATH = "misc.base.path";

    /**
     * The Constant HPC_COST_CENTER_COMAND.
     */
    public static final String HPC_COST_CENTER_COMAND = "hpc.costcenter";

    /**
     * The Constant IFCONFIG_BASEPATH_KEY.
     */
    public static final String IFCONFIG_BASEPATH_KEY = "ifconfig.path";

    /**
     * The Constant RE_INDEX_DATA.
     */
    public static final String RE_INDEX_DATA = "reindex";

    /**
     * The Constant VAULT_PATH.
     */
    public static final String STAGING_PATH = "staging.path";

    /**
     * The Constant TEMP_PATH.
     */
    public static final String FE_STATIC = "fe.basefolder";

    /**
     * The Constant DEFAULT_SERVRE_TEMP_PATH.
     */
    public static final String DEFAULT_SERVER_TEMP_PATH = "default.temp";

    /**
     * The constant ALLOWED_ORIGINS.
     */
    public static final String ALLOWED_ORIGINS = "allowed.origins";

    /**
     * The constant SERVER_CACHE.
     */
    public static final String SERVER_CACHE = "cache.path";

    /**
     * The Constant FAILED_LOGIN_ATTEMPTS.
     */
    public static final String FAILED_LOGIN_ATTEMPTS = "max.failed.login.attempts";

    /**
     * The constant FAILED_LOGIN_ATTEMPTS_LOCK_TIME.
     */
    public static final String FAILED_LOGIN_ATTEMPTS_LOCK_TIME = "failed.login.attempts.lock.time";

    /**
     * The Constant FFMPEG_PATH.
     */
    public static final String FFMPEG_PATH = "ffmpeg.path";

    /**
     * The Constant PLUNGER_URL.
     */
    public static final String PLUNGER_URL = "plunger.url";

    /**
     * The Constant LOCATION_MASTER.
     */
    public static final String LOCATION_MASTER = "location.master";

    /**
     * The Constant LOCATION_NAME.
     */
    public static final String LOCATION_NAME = "location.name";

    /**
     * The Constant LOCATION_TOKEN.
     */
    public static final String LOCATION_TOKEN = "location.authToken";

    /**
     * The Constant LOCATION_URL.
     */
    public static final String LOCATION_URL = "location.url";

    /**
     * The Constant LOCATION_SERVER_ADDRESS.
     */
    public static final String LOCATION_SERVER_ADDRESS = "location.serverAddress";

    /**
     * The default expiry. in seconds
     */
    public static final int DEFAULT_EXPIRY = 60 * 60 * 24; // 24 hours

    /**
     * The session expiry time.
     */
    public static final int USER_SESSION_EXPIRY_TIME = 360;

    /**
     * The Constant DOCS_PATH_POSTFIX.
     */
    public static final String DOCS_PATH_POSTFIX = "docs";

    /**
     * The Constant SUS_AUDIT_TRAIL_LEVEL.
     */
    public static final String SUS_AUDIT_TRAIL_LEVEL = "audit.trail.level";

    /**
     * The Constant SUS_DATA_NODE_NAME.
     */
    public static final String SUS_DATA_NODE_NAME = "data.node.name";

    /**
     * The Constant CB2_LOGIN.
     */
    public static final String CB2_LOGIN = "loginToCb2";

    /**
     * The Constant CB2_IP.
     */
    public static final String CB2_IP = "CB2_REST_SERVER";

    /**
     * The Constant CB2_PORT.
     */
    public static final String CB2_PORT = "CB2_REST_PORT";

    /**
     * The Constant CB2_PROCESS_REQUEST_TIMEOUT.
     */
    public static final String CB2_PROCESS_REQUEST_TIMEOUT = "CB2_PROCESS_REQUEST_TIMEOUT";

    /**
     * The constant QADYNA_BASE_INPUT_PROJECT_PATH.
     */
    public static final String QADYNA_BASE_INPUT_PROJECT_PATH = "qadyna.baseinput.project";

    /**
     * The constant QADYNA_BASE_INPUT_BASE_STATUS.
     */
    public static final String QADYNA_BASE_INPUT_BASE_STATUS = "qadyna.baseinput.base";

    /**
     * The constant CB2_OBJECT_TREE.
     */
    public static final String CB2_OBJECT_TREE = "object.tree.path";

    /**
     * The Constant CB2_SCHEDULED_IN_HOURS.
     */
    public static final String CB2_SCHEDULED_IN_HOURS = "cb2ScheduledInHours";

    /**
     * The constant WEB_EAM_AUTHORIZE.
     */
    public static final String WEB_EAM_AUTHORIZE = "web.eam.authorize";

    /**
     * The constant WEB_EAM_ACCESS_TOKEN.
     */
    public static final String WEB_EAM_ACCESS_TOKEN = "web.eam.access.token";

    /**
     * The Constant WEB_EAM_CLIENT_ID.
     */
    public static final String WEB_EAM_CLIENT_ID = "web.eam.clientId";

    /**
     * FB refreshToken request url with placeholders
     */
    public static final String FACEBOOK_REFRESH_TOKEN_URL = "facebook.refresh.url";

    /**
     * The Constant WEB_EAM_CLIENT_SECRETE.
     */
    public static final String WEB_EAM_CLIENT_SECRETE = "web.eam.clientSecret";

    /**
     * The Constant WEB_EAM_SCOPE.
     */
    public static final String WEB_EAM_SCOPE = "web.eam.scope";

    /**
     * The Constant WEB_EAM_REDIRECT_URI.
     */
    public static final String WEB_EAM_REDIRECT_URI = "web.eam.redirect.uri";

    /**
     * The Constant WEB_EAM_WELL_KNOWN_URL.
     */
    public static final String WEB_EAM_WELL_KNOWN_URL = "web.eam.well_known_url";

    /**
     * The Constant WEB_EAM_TOKEN_INFO.
     */
    public static final String WEB_EAM_TOKEN_INFO = "web.eam.info_token";

    /**
     * The Constant WEB_EAM_TOKEN_REVOKE.
     */
    public static final String WEB_EAM_TOKEN_REVOKE = "web.eam.token_revoke";

    /**
     * The constant WEB_EAM_REFRESH_TOKEN.
     */
    public static final String WEB_EAM_REFRESH_TOKEN = "web.eam.refresh_token";

    /**
     * The Constant WEB_EAM_ROOT.
     */
    public static final String WEB_EAM_ROOT = "web.eam.root";

    /**
     * The Constant WEB_EAM_REALM.
     */
    public static final String WEB_EAM_REALM = "web.eam.realm";

    /**
     * The Constant SUS_AUDIT_TRAIL_DEPTH.
     */
    public static final String SUS_AUDIT_TRAIL_DEPTH = "audit.trail.depth";

    /**
     * The Constant JOB_MONITORING_TIME.
     */
    public static final String JOB_MONITORING_TIME_SECONDS = "jobMonitoringTimeSeconds";

    /**
     * The sus web base url.
     */
    public static final String SUS_WEB_BASE_URL = "sus.web.base.url";

    /**
     * The Constant SUS_WEB_DOCS_URL.
     */
    public static final String SUS_WEB_DOCS_URL = "sus.web.docs.url";

    /**
     * The Constant SUSCORE_EXECUTOR_JSON.
     */
    public static final String SUSCORE_EXECUTOR_JSON = "executor.json";

    /**
     * The Constant SIM_VERSION_JSON.
     */
    public static final String SIM_VERSION_JSON = "sus.version.json";

    /**
     * The Constant ENCRP_DECRP_FILE.
     */
    public static final String ENCRP_DECRP_FILE = "encryptionDecryption.json";

    /**
     * The Constant PYTHON_PATH.
     */
    public static final String PYTHON_PATH = "pythonpath";

    /**
     * The Constant SERVER_LOG_PATH.
     */
    public static final String SERVER_LOG_PATH = "server.log.path";

    /**
     * The Constant PREPARE_SGE_JOB.
     */
    public static final String PREPARE_SGE_JOB = "preparesgejob";

    /**
     * The Constant IMPERSONATED.
     */
    public static final String IMPERSONATED = "impersonation";

    /**
     * The Constant TRANSLATION.
     */
    public static final String TRANSLATION = "translation";

    /**
     * The Constant LANGUAGES.
     */
    public static final String LANGUAGES = "languages";

    /**
     * The Constant EXECUTE_ON_REMOTE.
     */
    public static final String EXECUTE_ON_REMOTE = "execute.on.remote";

    /**
     * The Constant PROJECT_LABLE_ICON.
     */
    public static final String PROJECT_LABLE_ICON = "project.label.icon";

    /**
     * The Constant EMAIL_ADDRESS.
     */
    public static final String EMAIL_ADDRESS = "email";

    /**
     * The Constant EMAIL_PASSWORD.
     */
    public static final String EMAIL_PASSWORD = "password";

    /**
     * The Constant EMAIL_HOST.
     */
    public static final String EMAIL_HOST = "host";

    /**
     * The Constant EMAIL_PORT.
     */
    public static final String EMAIL_PORT = "port";

    /**
     * The Constant EMAIL_STARTTLS_ENABLE.
     */
    public static final String EMAIL_STARTTLS_ENABLE = "starttls.enable";

    /**
     * The Constant EMAIL_SSL_PROTOCOLS.
     */
    public static final String EMAIL_SSL_PROTOCOLS = "ssl.protocols";

    /**
     * The Constant MAIL_PROTOCOL.
     */
    public static final String MAIL_PROTOCOL = "mail.protocol";

    /**
     * The Constant SMTPS_AUTH.
     */
    public static final String SMTP_AUTH = "smtp.auth";

    /**
     * The Constant AVI.
     */
    public static final String AVI = "avi";

    /**
     * The Constant WEBM.
     */
    public static final String WEBM = "webm";

    /**
     * The Constant MKV.
     */
    public static final String MKV = "mkv";

    /**
     * The Constant MOV.
     */
    public static final String MOV = "mov";

    /**
     * The Constant MP4.
     */
    public static final String MP4 = "mp4";

    /**
     * The Constant SUDO_COMMAND.
     */
    public static final String SUDO_COMMAND_CONFIGS = "sudo.command.configs";

    /**
     * The Constant AUDIT_DIRECTORY.
     */
    public static final String AUDIT_DIRECTORY = "Audit_Directory";

    /**
     * The Constant AUDIT_ROLE.
     */
    public static final String AUDIT_ROLE = "Audit_Role";

    /**
     * The Constant AUDIT_GROUP.
     */
    public static final String AUDIT_GROUP = "Audit_Group";

    /**
     * The Constant AUDIT_USER.
     */
    public static final String AUDIT_USER = "Audit_User";

    /**
     * The Constant AUDIT_SYSTEM_VIEW.
     */
    public static final String AUDIT_SYSTEM_VIEW = "Audit_System_View";

    /**
     * The Constant AUDIT_PERMISSION.
     */
    public static final String AUDIT_PERMISSION = "Audit_Permissions";

    /**
     * The Constant AUDIT_DATA.
     */
    public static final String AUDIT_DATA = "Audit_Data";

    /**
     * The Constant AUDIT_WORKFLOW.
     */
    public static final String AUDIT_WORKFLOW = "Audit_Workflow";

    /**
     * The Constant REGEX_CONTEXT_LINE.
     */
    public static final String REGEX_CONTEXT_LINE = "regex.scan.context";

    /**
     * The constant CONVERSION_COMMAND.
     */
    public static final String CONVERSION_COMMAND = "conversioncommand";

    /**
     * The constant OUTPUT_PATH.
     */
    public static final String OUTPUT_PATH = "outputPath";

    /**
     * The constant SERVER_PATH.
     */
    public static final String SERVER_PATH = "serverPath";

    /**
     * The constant DYNAMIC_BASE_PATH.
     */
    public static final String DYNAMIC_BASE_PATH = "dynamicBasePath";

    /**
     * THE CONSTANT JRE_PATH.
     */
    public static final String JAVA_PATH = "java.path";

    /**
     * THE CONSTANT SCHEME_TEMPLATE_FILE_SIZE.
     */
    public static final String SCHEME_TEMPLATE_FILE_SIZE = "scheme.template.file.size";

    /**
     * The Constant REGEX_REPLACE_KARAF_PATH.
     */
    public static final String REGEX_REPLACE_KARAF_PATH = "${karaf.base}";

    /**
     * The constant REGEX_REPLACE_KARAF_SCRIPT_PATH.
     */
    public static final String REGEX_REPLACE_KARAF_SCRIPT_PATH = "${karaf.script}";

    /**
     * The constant PROJECT_OVERVIEW_SCRIPT.
     */
    public static final String PROJECT_OVERVIEW_SCRIPT = "project.overview.script";

    public static final String MAX_TREE_CHILDREN = "max.tree.children";

    /**
     * The Constant IMAGE_FORMAT.
     */
    public static final String IMAGE_FORMAT = "imageFormats";

    /**
     * The Constant MOVIE_FORMAT.
     */
    public static final String MOVIE_FORMAT = "movieFormats";

    /**
     * Private Constructor to avoid Object Instantiation.
     */
    private ConstantsFileProperties() {

    }

    /**
     * The Enum Commands & extensions.
     */
    public enum Commands {

        /**
         * The M p4.
         */
        MP4( getMP4Command(), getMP4Extension(), prepareMP4() ),
        /**
         * The webm.
         */
        WEBM( getWebmCommand(), getWebmExtension(), prepareWEBM() ),
        /**
         * The ogv.
         */
        OGV( getOgvCommand(), getOgvExtension(), prepareOGV() ),
        /**
         * The avi.
         */
        AVI( getAVICommand(), getAVIExtension(), prepareAVI() ),
        /**
         * The mkv.
         */
        MKV( getMKVCommand(), getMKVExtension(), prepareMKV() ),

        /**
         * The webm 2.
         */
        WEBM_2( getWebm_2Command(), getWebmExtension(), prepareWEBM() ),
        /**
         * The image poster.
         */
        IMAGE_POSTER( getImagePosterCommand(), getPosterPicExtension(), prepareImagePoster() );

        /**
         * The Constant FFMPEG.
         */
        private static final String FFMPEG = "ffmpeg ";

        /**
         * The Constant FFMPEG_PATH_HOLDER_VAR.
         */
        private static final String FFMPEG_PATH_HOLDER_VAR = "#k";

        /**
         * -vcodec libx264 specifies that we want to use libx264 as our video codec -pix_fmt yuv420p specifies the yuv420p pixel format,
         * which works on more platforms than other formats -profile:v baseline uses the x264 'baseline' profile (makes Android happy)
         * -preset slower tells ffmpeg to prefer slower encoding and better results over faster encoding time -crf 18 sets the constant rate
         * factor to 18, which affects quality. Lower numbers are better quality -vf "scale=..." uses the scale video filter to make sure
         * that the video doesn't have an odd width/height
         */
        private static final String MP4_COMMAND = "ffmpeg -i $inp -vcodec libx264 -pix_fmt yuv420p -profile:v baseline -preset slower -crf 18 -vf scale='trunc(in_w/2)*2:trunc(in_h/2)*2' -movflags faststart $ -y";

        /**
         * The Constant MP4_EXTENSION.
         */
        private static final String MP4_EXTENSION = ".mp4";

        /**
         * The Constant PREPARE_MP4.
         */
        private static final boolean PREPARE_MP4 = true;

        /**
         * ffmpeg command for avi
         * <p>
         * the Constant AVI_COMMAND. just convert the file format and copy it to given destination
         */
        private static final String AVI_COMMAND = "ffmpeg -i $inp $output";

        /**
         * The Constant AVI_EXTENSION.
         */
        private static final String AVI_EXTENSION = ".avi";

        /**
         * The Constant PREPARE_AVI.
         */
        private static final boolean PREPARE_AVI = true;

        /**
         * ffmpeg command for mkv
         * <p>
         * the Constant MKV_COMMAND. just convert the file format and copy it to given destination
         */
        private static final String MKV_COMMAND = "ffmpeg -i $inp -vcodec copy -acodec copy $output";

        /**
         * The Constant MKV_EXTENSION.
         */
        private static final String MKV_EXTENSION = ".mkv";

        /**
         * The Constant PREPARE_MKV.
         */
        private static final boolean PREPARE_MKV = true;

        /**
         * This will turn input.ext into output.webm. -c:v libvpx -c:a libvorbis specifies the vp8 and vorbis encodings -quality good can be
         * good, best, or fast. 'best' is not suggested. -b:v 2M sets the target bitrate to 2 MB/s. Adjust this to fit your bandwidth
         * requirements (it will affect quality) -crf 5 sets the constant rate factor. This works on a different scale than x264. Modify
         * -crf and -b:v to adjust the output quality
         */
        private static final String WEBM_COMMAND = "ffmpeg -i $inp -c:v libvpx -c:a libvorbis -pix_fmt yuv420p -quality good -b:v 2M -crf 5 -vf scale='trunc(in_w/2)*2:trunc(in_h/2)*2' $output -y";

        /**
         * ffmpeg command for webm the Constant WEBM_COMMAND_2 just convert the file format and copy it to given destination.
         */
        private static final String WEBM_COMMAND_2 = "ffmpeg -i $inp $output";

        /**
         * The Constant WEBM_EXTENSION.
         */
        private static final String WEBM_EXTENSION = ".webm";

        /**
         * The Constant PREPARE_WEBM.
         */
        private static final boolean PREPARE_WEBM = true;

        /**
         * -q 5 sets the desired quality.
         */
        private static final String OGV_COMMAND = "ffmpeg -i $inp -q 5 -pix_fmt yuv420p -acodec libvorbis -vcodec libtheora $output -y";

        /**
         * The Constant OGV_EXTENSION.
         */
        private static final String OGV_EXTENSION = ".ogv";

        /**
         * The Constant PREPARE_OGV.
         */
        private static final boolean PREPARE_OGV = false;

        /**
         * Extract single Image/Poster for movie:.
         */
        private static final String IMAGE_POSTER_COMMAND = "OUT=\"$(#k -i $inp 2>&1 | grep \"Duration\"| cut -d ' ' -f 4 | sed s/,// | sed 's@\\..*@@g' | awk '{ split($1, A, \":\"); split(A[3], B, \".\"); print (3600*A[1] + 60*A[2] + B[1])*0.1 }')\" ; #k -i $inp -ss $OUT -f image2 -vframes 1 $output -y";

        /**
         * The Constant IMAGE_POSTER_COMMAND_WIN.
         */
        private static final String IMAGE_POSTER_COMMAND_WIN = "#k -i $inp -r 10 -f image2 $output -y";

        /**
         * The Constant IMAGE_POSTER_EXTENSION.
         */
        private static final String IMAGE_POSTER_EXTENSION = ".png";

        /**
         * The Constant PREPARE_IMAGE_POSTER.
         */
        private static final boolean PREPARE_IMAGE_POSTER = true;

        /**
         * The command.
         */
        private String command;

        /**
         * The enabled.
         */
        private boolean enabled;

        /**
         * The extension.
         */
        private String extension;

        /**
         * Instantiates a new commands.
         *
         * @param command
         *         the command
         * @param extension
         *         the extension
         * @param enabled
         *         the enabled
         */
        private Commands( String command, String extension, boolean enabled ) {
            this.command = command;
            this.enabled = enabled;
            this.extension = extension;
        }

        /**
         * Gets the command.
         *
         * @return the command
         */
        public String getCommand() {
            return this.command;
        }

        /**
         * Gets the extension.
         *
         * @return the extension
         */
        public String getExtension() {
            return this.extension;
        }

        /**
         * Checks if is enabled.
         *
         * @return true, if is enabled
         */
        public boolean isEnabled() {
            return this.enabled;
        }

        /**
         * Gets the m p4 command.
         *
         * @return the m p4 command
         */
        private static String getMP4Command() {
            return PropertiesManager.getFFmpegPath() + MP4_COMMAND;
        }

        /**
         * Gets the AVI command.
         *
         * @return the AVI command
         */
        private static String getAVICommand() {
            return PropertiesManager.getFFmpegPath() + AVI_COMMAND;
        }

        /**
         * Gets the MKV command.
         *
         * @return the MKV command
         */
        private static String getMKVCommand() {
            return PropertiesManager.getFFmpegPath() + MKV_COMMAND;
        }

        /**
         * Gets the webm command.
         *
         * @return the webm command
         */
        private static String getWebmCommand() {
            return PropertiesManager.getFFmpegPath() + WEBM_COMMAND;
        }

        /**
         * Gets the webm 2 command.
         *
         * @return the webm 2 command
         */
        private static String getWebm_2Command() {
            return PropertiesManager.getFFmpegPath() + WEBM_COMMAND_2;
        }

        /**
         * Gets the ogv command.
         *
         * @return the ogv command
         */
        private static String getOgvCommand() {
            return PropertiesManager.getFFmpegPath() + OGV_COMMAND;
        }

        /**
         * Gets the image poster command.
         *
         * @return the image poster command
         */
        private static String getImagePosterCommand() {
            if ( OSValidator.isUnix() ) {
                return IMAGE_POSTER_COMMAND.replaceAll( FFMPEG_PATH_HOLDER_VAR, PropertiesManager.getFFmpegPath() + FFMPEG );
            } else {
                return IMAGE_POSTER_COMMAND_WIN.replace( FFMPEG_PATH_HOLDER_VAR, PropertiesManager.getFFmpegPath() + FFMPEG );

            }

        }

        /**
         * Prepare m p4.
         *
         * @return true, if successful
         */
        public static boolean prepareMP4() {
            return PREPARE_MP4;
        }

        /**
         * Prepare AVI.
         *
         * @return true, if successful
         */
        public static boolean prepareAVI() {
            return PREPARE_AVI;
        }

        /**
         * Prepare MKV.
         *
         * @return true, if successful
         */
        public static boolean prepareMKV() {
            return PREPARE_MKV;
        }

        /**
         * Prepare webm.
         *
         * @return true, if successful
         */
        public static boolean prepareWEBM() {
            return PREPARE_WEBM;
        }

        /**
         * Prepare ogv.
         *
         * @return true, if successful
         */
        public static boolean prepareOGV() {
            return PREPARE_OGV;
        }

        /**
         * Prepare image poster.
         *
         * @return true, if successful
         */
        private static boolean prepareImagePoster() {
            return PREPARE_IMAGE_POSTER;
        }

        /**
         * Gets the m p4 extension.
         *
         * @return the m p4 extension
         */
        private static String getMP4Extension() {
            return MP4_EXTENSION;
        }

        /**
         * Gets the AVI extension.
         *
         * @return the AVI extension
         */
        private static String getAVIExtension() {
            return AVI_EXTENSION;
        }

        /**
         * Gets the MKV extension.
         *
         * @return the MKV extension
         */
        private static String getMKVExtension() {
            return MKV_EXTENSION;
        }

        /**
         * Gets the ogv extension.
         *
         * @return the ogv extension
         */
        private static String getOgvExtension() {
            return OGV_EXTENSION;
        }

        /**
         * Gets the webm extension.
         *
         * @return the webm extension
         */
        private static String getWebmExtension() {
            return WEBM_EXTENSION;
        }

        /**
         * Gets the poster pic extension.
         *
         * @return the poster pic extension
         */
        private static String getPosterPicExtension() {
            return IMAGE_POSTER_EXTENSION;
        }
    }

}