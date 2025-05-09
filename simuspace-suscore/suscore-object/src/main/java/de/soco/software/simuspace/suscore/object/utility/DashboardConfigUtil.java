package de.soco.software.simuspace.suscore.object.utility;

import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.DashboardDataSourceOptions;
import static de.soco.software.simuspace.suscore.common.enums.DashboardEnums.WidgetPythonOutputOptions;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The type Dashboard config util.
 */
public class DashboardConfigUtil {

    /**
     * Instantiates a new Dashboard config util.
     */
    private DashboardConfigUtil() {

    }

    /**
     * The constant DATABASE_TO_CSV.
     */
    private static final String DATABASE_TO_CSV = "templates.database.csv";

    /**
     * The constant PYTHON_OUTPUT_FILE_PREVIEW_SIZE_LIMIT.
     */
    private static final String PYTHON_OUTPUT_FILE_PREVIEW_SIZE_LIMIT = "limit.python.output";

    /**
     * The constant PYTHON_EXECUTION_TIMEOUT.
     */
    private static final String PYTHON_EXECUTION_TIMEOUT = "limit.python.timeout";

    /**
     * The constant EXCEL_TO_CSV.
     */
    private static final String EXCEL_TO_CSV = "templates.excel.csv";

    /**
     * The constant CSV_TO_CSV.
     */
    private static final String CSV_TO_CSV = "templates.csv.csv";

    /**
     * The constant DASHBOARD_QUERY_LIMIT.
     */
    private static final String DASHBOARD_QUERY_LIMIT = "limit.query";

    /**
     * The constant MATERIAL_INSPECTION_SCRIPT_PATH.
     */
    private static final String MATERIAL_INSPECTION_SCRIPT_PATH = "script.system.materialInspection";

    /**
     * The constant MATERIAL_INSPECTION_SCRIPT_PATH.
     */
    private static final String TREEMAP_VMCL_SCRIPT_PATH = "script.system.vMcl.treeMap";

    /**
     * The constant TABLE_VMCL_SCRIPT_PATH.
     */
    private static final String TABLE_VMCL_SCRIPT_PATH = "script.system.vMcl.table";

    /**
     * The constant PROJECT_LIFECYCLE_VMCL_SCRIPT_PATH.
     */
    private static final String PROJECT_LIFECYCLE_VMCL_SCRIPT_PATH = "script.system.vMcl.projectLifeCycle";

    /**
     * The constant METAL_BASE_SCRIPT_PATH.
     */
    private static final String METAL_BASE_SCRIPT_PATH = "script.system.metalBase";

    /**
     * The constant MATERIAL_INSPECTION_ENV.
     */
    private static final String MATERIAL_INSPECTION_ENV = "env.materialInspection";

    /**
     * The constant METAL_BASE_ENV.
     */
    private static final String METAL_BASE_ENV = "env.metalBase";

    /**
     * The constant PST_PLANNING_SCRIPT_PATH.
     */
    private static final String PST_PLANNING_SCRIPT_PATH = "script.system.pstPlanning";

    /**
     * The constant JSON_TO_CSV.
     */
    private static final String JSON_TO_CSV = "templates.json.csv";

    /**
     * The constant CSV_TO_JSON.
     */
    private static final String CSV_TO_JSON = "templates.csv.json";

    /**
     * The constant DATABASE_TO_JSON.
     */
    private static final String DATABASE_TO_JSON = "templates.database.json";

    /**
     * The constant EXCEL_TO_JSON.
     */
    private static final String EXCEL_TO_JSON = "templates.excel.json";

    /**
     * The constant JSON_TO_JSON.
     */
    private static final String JSON_TO_JSON = "templates.json.json";

    /**
     * The constant SUS_TO_JSON.
     */
    private static final String SUS_TO_JSON = "templates.sus.json";

    /**
     * The constant SUS_TO_CSV.
     */
    private static final String SUS_TO_CSV = "templates.sus.json";

    /**
     * Gets template path.
     *
     * @param source
     *         the source
     * @param target
     *         the target
     *
     * @return the template path
     */
    public static String getTemplatePath( DashboardDataSourceOptions source, WidgetPythonOutputOptions target ) {
        var dashboardProperties = PropertiesManager.getDashboardProperties();
        var path = switch ( source ) {
            case CSV -> switch ( target ) {
                case CSV -> dashboardProperties.getProperty( CSV_TO_CSV );
                case JSON -> dashboardProperties.getProperty( CSV_TO_JSON );
            };
            case EXCEL -> switch ( target ) {
                case CSV -> dashboardProperties.getProperty( EXCEL_TO_CSV );
                case JSON -> dashboardProperties.getProperty( EXCEL_TO_JSON );
            };
            case DATABASE -> switch ( target ) {
                case CSV -> dashboardProperties.getProperty( DATABASE_TO_CSV );
                case JSON -> dashboardProperties.getProperty( DATABASE_TO_JSON );
            };
            case JSON -> switch ( target ) {
                case CSV -> dashboardProperties.getProperty( JSON_TO_CSV );
                case JSON -> dashboardProperties.getProperty( JSON_TO_JSON );
            };
            case SUS_OBJECT -> switch ( target ) {
                case CSV -> dashboardProperties.getProperty( SUS_TO_CSV );
                case JSON -> dashboardProperties.getProperty( SUS_TO_JSON );
            };
        };
        return path.replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets query records limit.
     *
     * @return the query records limit
     */
    public static Integer getQueryRecordsLimit() {
        final int DEFAULT_VALUE = 1000000; // 1 million
        var limitFromCfg = PropertiesManager.getDashboardProperties().getProperty( DASHBOARD_QUERY_LIMIT );
        return limitFromCfg == null ? DEFAULT_VALUE : Integer.parseInt( limitFromCfg );
    }

    /**
     * Gets python output file size limit for preview.
     *
     * @return the python output file size limit for preview
     */
    public static Long getPythonOutputFileSizeLimitForPreview() {
        final long DEFAULT_VALUE = 30; // 30MB
        var limitFromCfg = PropertiesManager.getDashboardProperties().getProperty( PYTHON_OUTPUT_FILE_PREVIEW_SIZE_LIMIT );
        var limit = limitFromCfg == null ? DEFAULT_VALUE : Long.parseLong( limitFromCfg );
        return limit * 1024 * 1024;
    }

    /**
     * Gets timeout for python process.
     *
     * @return the timeout for python process
     */
    public static Integer getTimeoutForPythonProcess() {
        final int DEFAULT_VALUE = 5; // 5 minutes
        var timeoutFromCfg = PropertiesManager.getDashboardProperties().getProperty( PYTHON_EXECUTION_TIMEOUT );
        var timeout = timeoutFromCfg == null ? DEFAULT_VALUE : Integer.parseInt( timeoutFromCfg );
        return timeout * 60;
    }

    /**
     * Gets material inspection script path.
     *
     * @return the material inspection script path
     */
    public static String getMaterialInspectionScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( MATERIAL_INSPECTION_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets vMCL script path.
     *
     * @return the vMCL script path
     */
    public static String getTreemapVmclScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( TREEMAP_VMCL_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets vMCL script path.
     *
     * @return the vMCL script path
     */
    public static String getTableVmclScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( TABLE_VMCL_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    public static String getProjectLifeCycleVmclScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( PROJECT_LIFECYCLE_VMCL_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets metal base script path.
     *
     * @return the metal base script path
     */
    public static String getMetalBaseScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( METAL_BASE_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets pst script path.
     *
     * @return the pst script path
     */
    public static String getPstScriptPath() {
        return PropertiesManager.getDashboardProperties().getProperty( PST_PLANNING_SCRIPT_PATH )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets material inspection python path.
     *
     * @return the material inspection python path
     */
    public static String getMaterialInspectionPythonPath() {
        return PropertiesManager.getDashboardProperties().getProperty( MATERIAL_INSPECTION_ENV )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

    /**
     * Gets metal base python path.
     *
     * @return the material base python path
     */
    public static String getMetalBasePythonPath() {
        return PropertiesManager.getDashboardProperties().getProperty( METAL_BASE_ENV )
                .replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH, PropertiesManager.getScriptsPath() );
    }

}
