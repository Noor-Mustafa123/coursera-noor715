package de.soco.software.simuspace.suscore.common.constants;

/**
 * The type Dashboard constants.
 */
public class DataDashboardConstants {

    /**
     * The type Python modes.
     */
    public static class PYTHON_MODES {

        /**
         * The constant FETCH.
         */
        public static final String FETCH = "fetch";

        /**
         * The constant REGENERATE.
         */
        public static final String REGENERATE = "regenerate";

    }

    /**
     * The type Data source fields.
     */
    public static class DATA_SOURCE_FIELDS {

        /**
         * The constant SOURCE_NAME.
         */
        public static final String SOURCE_NAME = "sourceName";

        /**
         * The constant SOURCE_TYPE.
         */
        public static final String SOURCE_TYPE = "sourceType";

        /**
         * The constant FILE_SELECTION.
         */
        public static final String FILE_SELECTION = "fileSelection";

        /**
         * The constant DELIMITER.
         */
        public static final String DELIMITER = "delimiter";

        /**
         * The constant DATABASE_TYPE.
         */
        public static final String DATABASE_TYPE = "databaseType";

        /**
         * The constant HOST.
         */
        public static final String HOST = "host";

        /**
         * The constant PORT.
         */
        public static final String PORT = "port";

        /**
         * The constant DB_NAME.
         */
        public static final String DB_NAME = "dbName";

        /**
         * The constant USER_NAME.
         */
        public static final String USER_NAME = "userName";

        /**
         * The constant PASSWORD.
         */
        public static final String PASSWORD = "password";

        /**
         * The constant TEST_BUTTON.
         */
        public static final String TEST_BUTTON = "testButton";

        /**
         * The constant PREVIEW_BUTTON.
         */
        public static final String PREVIEW_BUTTON = "previewButton";

        /**
         * The constant UPDATE_CACHE.
         */
        public static final String UPDATE_CACHE = "updateCache";

        /**
         * The constant TRAVERSAL_DEPTH.
         */
        public static final String TRAVERSAL_DEPTH = "traversalDepth";

        /**
         * The constant UPDATE_INTERVAL.
         */
        public static final String UPDATE_INTERVAL = "updateInterval";

        /**
         * The constant PROJECT_SELECTION.
         */
        public static final String PROJECT_SELECTION = "projectSelection";

        public static final String CACHE_UPDATED_AT = "cacheUpdatedAt";

    }

    /**
     * The type Mix chart widget fields.
     */
    public static class MIX_CHART_WIDGET_FIELDS {

        /**
         * The constant ADD_CHART.
         */
        public static final String ADD_CURVE = "addCurve";

        /**
         * The constant CURVE_TYPE.
         */
        public static final String CURVE_OPTION_CURVE_TYPE = "curveOptions[].curveType";

        /**
         * The constant MULTI_X.
         */
        public static final String CURVE_OPTION_X_AXIS = "curveOptions[].x_axis";

        /**
         * The constant MULTI_Y.
         */
        public static final String CURVE_OPTION_Y_AXIS = "curveOptions[].y_axis";

        /**
         * The constant MIX_STYLING.
         */
        public static final String CURVE_OPTION_STYLING = "curveOptions[].styling";

        /**
         * The constant MIX_POINT_SYMBOL.
         */
        public static final String CURVE_OPTION_POINT_SYMBOL = "curveOptions[].pointSymbol";

        /**
         * The constant MIX_SMOOTH.
         */
        public static final String CURVE_OPTION_SMOOTH = "curveOptions[].smooth";

        /**
         * The constant CURVE_OPTIONS.
         */
        public static final String CURVE_OPTIONS = "curveOptions";

    }

    /**
     * The type Treemap widget field names.
     */
    public static class TREEMAP_WIDGET_FIELDS {

        /**
         * The constant MAX_DEPTH.
         */
        public static final String LEAF_DEPTH = "leafDepth";

    }

    /**
     * The type Widget field names.
     */
    public static class WIDGET_FIELDS {

        /**
         * The constant X_AXIS_TITLE.
         */
        public static final String X_AXIS_TITLE = "x_axis_title";

        /**
         * The constant Y_AXIS_TITLE.
         */
        public static final String Y_AXIS_TITLE = "y_axis_title";

        /**
         * The constant ID.
         */
        public static final String ID = "id";

        /**
         * The constant TYPE.
         */
        public static final String TYPE = "type";

        /**
         * The constant DATA_SOURCE_TYPE.
         */
        public static final String DATA_SOURCE_TYPE = "sourceType";

        /**
         * The constant X_AXIS.
         */
        public static final String X_AXIS = "x_axis";

        /**
         * The constant Y_AXIS.
         */
        public static final String Y_AXIS = "y_axis";

        /**
         * The constant DATA_SOURCE.
         */
        public static final String DATA_SOURCE = "dataSource";

        /**
         * The constant QUERY_BUILDER_SCHEMA.
         */
        public static final String QUERY_BUILDER_SCHEMA = "schema";

        /**
         * The constant QUERY_BUILDER_TABLE.
         */
        public static final String QUERY_BUILDER_TABLE = "table";

        /**
         * The constant TABLE.
         */
        public static final String WIDGET_TYPE = "type";

        /**
         * The constant PYTHON_PATH.
         */
        public static final String PYTHON_PATH = "pythonPath";

        /**
         * The constant PYTHON_OUTPUT_TYPE.
         */
        public static final String PYTHON_OUTPUT_TYPE = "outputType";

        /**
         * The constant PYTHON_SCRIPT.
         */
        public static final String PYTHON_SCRIPT = "pythonScript";

        /**
         * The constant PYTHON_STATUS.
         */
        public static final String PYTHON_STATUS = "pythonStatus";

        /**
         * The constant COLOR_SCHEME.
         */
        public static final String COLOR_THEME = "colorTheme";

        /**
         * The constant PYTHON_SCRIPT.
         */
        public static final String SYSTEM_SCRIPT = "systemScript";

        /**
         * The constant PYTHON_SCRIPT.
         */
        public static final String SELECT_SCRIPT = "selectScript";

        /**
         * The constant NAME.
         */
        public static final String NAME = "name";

        /**
         * The constant SCRIPT_OPTION.
         */
        public static final String PYTHON_SCRIPT_OPTION = "scriptOption";

        /**
         * The constant POINT_SYMBOL.
         */
        public static final String POINT_SYMBOL = "pointSymbol";

        /**
         * The constant TITLE.
         */
        public static final String TITLE = "title";

        /**
         * The constant WIDGETS.
         */
        public static final String WIDGETS = "widgets";

        /**
         * The constant STYLING.
         */
        public static final String STYLING = "styling";

        public static final String UPDATE_WIDGET_INTERVAL = "updateWidgetInterval";

    }

    public static class TABLE_WIDGET_FIELDS {

        /**
         * The constant COLUMNS.
         */
        public static final String TABLE_COLUMNS = "tableColumns";

    }

    /**
     * The type Text widget fields.
     */
    public static class TEXT_WIDGET_FIELDS {

        /**
         * The constant COLUMN.
         */
        public static final String COLUMN = "column";

        /**
         * The constant COLUMN_TITLE.
         */
        public static final String COLUMN_TITLE = "columnTitle";

        /**
         * The constant INPUT_METHOD.
         */
        public static final String INPUT_METHOD = "inputMethod";

        /**
         * The constant DISPLAY_VALUE.
         */
        public static final String DISPLAY_VALUE = "displayValue";

    }

    /**
     * The type Pst fields.
     */
    public static class PST_FIELDS {

        /**
         * The constant ACE_LOUNGE_CSV.
         */
        public static final String ACE_LOUNGE_CSV = "aceLoungeCsv";

        /**
         * The constant KS_UPDATES.
         */
        public static final String KS_UPDATES = "ksUpdates";

        /**
         * The constant BMW_UPDATES.
         */
        public static final String BMW_UPDATES = "bmwUpdates";

        /**
         * The constant APL_UPDATES.
         */
        public static final String APL_UPDATES = "aplUpdates";

        /**
         * The constant ACTION.
         */
        public static final String ACTION = "action";

        /**
         * The constant PAYLOAD.
         */
        public static final String PAYLOAD = "payload";

        /**
         * The constant PRUFSTAND.
         */
        public static final String PRUFSTAND = "prufstand";

        /**
         * The constant PROGRAMM.
         */
        public static final String PROGRAMM = "programm";

        /**
         * The constant MOTORTYP.
         */
        public static final String MOTORTYP = "motortyp";

        /**
         * The constant MOTOR_BG.
         */
        public static final String MOTOR_BG = "motor_bg";

        /**
         * The constant STATUS.
         */
        public static final String STATUS = "status";

        /**
         * The constant VORBEREITUNG.
         */
        public static final String VORBEREITUNG = "vorbereitung";

    }

    /**
     * The type Material inspector fields.
     */
    public static class MATERIAL_INSPECTOR_FIELDS {

        /**
         * The constant MAT_DB_SOURCE.
         */
        public static final String MAT_DB_SOURCE = "matDbSource";

        /**
         * The constant MAT_DB_SCHEMA.
         */
        public static final String MAT_DB_SCHEMA = "matDbSchema";

        /**
         * The constant GS_MATERIAL_NAME.
         */
        public static final String GS_MATERIAL_NAME = "gs_material_name";

        /**
         * The constant THICKNESS.
         */
        public static final String THICKNESS = "thickness";

        /**
         * The constant SURFACE_FINISH.
         */
        public static final String SURFACE_FINISH = "surface_finish";

        /**
         * The constant CHARGE.
         */
        public static final String CHARGE = "charge";

        /**
         * The constant LABORATORY_NAME.
         */
        public static final String LABORATORY_NAME = "laboratory_name";

        /**
         * The constant SUPPLIER_NAME.
         */
        public static final String SUPPLIER_NAME = "supplier_name";

        /**
         * The constant STATUS.
         */
        public static final String STATUS = "status";

        /**
         * The constant ORDER_NR.
         */
        public static final String ORDER_NR = "order_nr";

        /**
         * The constant PROBE.
         */
        public static final String PROBE = "probe";

        /**
         * The constant SCHEMA.
         */
        public static final String SCHEMA = "schema";

        /**
         * The constant INVESTIGATED_STRAIN_STATE.
         */
        public static final String INVESTIGATED_STRAIN_STATE = "investigatedStrainState";

        /**
         * The constant LAB_DATA_PLOT_DATA.
         */
        public static final String LAB_DATA_PLOT_DATA = "labDataPlotData";

        /**
         * The constant FT0.
         */
        public static final String FT0 = "ft0";

        /**
         * The constant FT45.
         */
        public static final String FT45 = "ft45";

        /**
         * The constant FT90.
         */
        public static final String FT90 = "ft90";

        /**
         * The constant FB.
         */
        public static final String FB = "fb";

        /**
         * The constant F_TAU.
         */
        public static final String F_TAU = "f_tau";

        /**
         * The constant EXP.
         */
        public static final String EXP = "exp";

        /**
         * The constant WEIGHT_S.
         */
        public static final String WEIGHT_S = "weightS";

        /**
         * The constant WEIGHT_R.
         */
        public static final String WEIGHT_R = "weightR";

        /**
         * The constant CAL_YIELD_LOCUS_B89.
         */
        public static final String CAL_YIELD_LOCUS_B89 = "calYieldLocusB89";

        /**
         * The constant CAL_YIELD_LOCUS_B2000.
         */
        public static final String CAL_YIELD_LOCUS_B2000 = "calYieldLocusB2000";

        /**
         * The constant DEFINE_YIELD_LOCUS.
         */
        public static final String DEFINE_YIELD_LOCUS = "defineYieldLocus";

        /**
         * The constant GEN_YIELD_LOCUS.
         */
        public static final String GEN_YIELD_LOCUS = "genYieldLocus";

        /**
         * The constant WEIGHT_N.
         */
        public static final String WEIGHT_N = "weightN";

        /**
         * The constant DISCRETIZATION.
         */
        public static final String DISCRETIZATION = "discretization";

        /**
         * The constant GENERATIONS.
         */
        public static final String GENERATIONS = "generations";

        /**
         * The constant INTERVAL_N.
         */
        public static final String INTERVAL_N = "intervalN";

        /**
         * The constant CPUS.
         */
        public static final String CPUS = "cpus";

        /**
         * The constant POP_SIZE.
         */
        public static final String POP_SIZE = "popSize";

        /**
         * The constant INITIAL_VALUE_FC_OPTIMIZATION.
         */
        public static final String INITIAL_VALUE_FC_OPTIMIZATION = "initialValueFCOptimization";

        /**
         * The constant OPT_FLOW_CURVE_MODELS.
         */
        public static final String OPT_FLOW_CURVE_MODELS = "optFlowCurveModels";

        /**
         * The constant LOAD_OPT_RESULTS.
         */
        public static final String LOAD_OPT_RESULTS = "loadOptResults";

        /**
         * The constant EXPORT_FLOW_CURVE_DATA.
         */
        public static final String EXPORT_FLOW_CURVE_DATA = "exportFlowCurveData";

        /**
         * The constant FLOW_CURVE_PLOT_DATA.
         */
        public static final String FLOW_CURVE_PLOT_DATA = "flowCurvePlotData";

        /**
         * The constant MATERIAL_CARD_NAME.
         */
        public static final String MATERIAL_CARD_NAME = "materialCardName";

        /**
         * The constant EXPORT_MEASUREMENT_DATA.
         */
        public static final String EXPORT_MEASUREMENT_DATA = "exportMeasurementData";

        /**
         * The constant EXPORT_YIELD_LOCUS.
         */
        public static final String EXPORT_YIELD_LOCUS = "exportYieldLocus";

    }

    public static class VMCL_FIELDS {

        /**
         * The constant VMCL_DATA_SOURCE.
         */
        public static final String VMCL_DATA_SOURCE = "vmclDataSource";

    }

    /**
     * The type Metal base fields.
     */
    public static class METAL_BASE_FIELDS {

        /**
         * The constant MAT_DB_SOURCE.
         */
        public static final String MAT_DB_SOURCE = "matDbSource";

        /**
         * The constant MAT_DB_SCHEMA.
         */
        public static final String MAT_DB_SCHEMA = "matDbSchema";

        /**
         * The constant GS_MATERIAL_NAME.
         */
        public static final String GS_MATERIAL_NAME = "gs_material_name";

        /**
         * The constant SURFACE_FINISH.
         */
        public static final String SURFACE_FINISH = "surface_finish";

        /**
         * The constant ANGLE.
         */
        public static final String ANGLE = "angle";

        /**
         * The constant MIN_THICKNESS.
         */
        public static final String MIN_THICKNESS = "minThickness";

        /**
         * The constant MAX_THICKNESS.
         */
        public static final String MAX_THICKNESS = "maxThickness";

        /**
         * The constant SUPPLIER_NAME.
         */
        public static final String SUPPLIER_NAME = "supplier_name";

        /**
         * The constant STATUS.
         */
        public static final String IO_STATUS = "ioStatus";

        /**
         * The constant FLOW_CURVE.
         */
        public static final String FLOW_CURVE = "flowCurve";

        /**
         * The constant BULGE.
         */
        public static final String BULGE = "bulge";

        /**
         * The constant QUANTILE_LIMIT.
         */
        public static final String QUANTILE_LIMIT = "quantileLimit";

        /**
         * The constant ORDER_NR.
         */
        public static final String ORDER_NR = "order_nr";

        /**
         * The constant ALL_TEST_IDS.
         */
        public static final String ALL_TEST_IDS = "allTestIds";

        /**
         * The constant TEST_CREATED_BETWEEN.
         */
        public static final String TEST_CREATED_BETWEEN = "testCreatedBetween";

        /**
         * The constant SHOW_ONLY_IN_REVIEW.
         */
        public static final String SHOW_ONLY_IN_REVIEW = "showOnlyInReview";

        /**
         * The constant LAB_DATA_PLOT_DATA.
         */
        public static final String PLOT_DATA = "plotData";

        /**
         * The constant CHARACTERISTICS.
         */
        public static final String CHARACTERISTICS = "characteristics";

    }

    /**
     * The type Bar widget fields.
     */
    public static class BAR_WIDGET_FIELDS {

        /**
         * The constant BAR_COLOR.
         */
        public static final String BAR_COLOR = "barColor";

        /**
         * The constant COLOR_AGGREGATE.
         */
        public static final String COLOR_AGGREGATE = "colorAggregate";

    }

    /**
     * The type Radar widget fields.
     */
    public static class RADAR_WIDGET_FIELDS {

        /**
         * The constant INDICATOR.
         */
        public static final String INDICATOR = "indicator";

        /**
         * The constant VALUE.
         */
        public static final String VALUE = "value";

        /**
         * The constant MIN.
         */
        public static final String MIN = "min";

        /**
         * The constant MAX.
         */
        public static final String MAX = "max";

        /**
         * The constant SHAPE.
         */
        public static final String SHAPE = "shape";

        /**
         * The constant ADD_VALUE.
         */
        public static final String ADD_VALUE = "addValue";

    }

    /**
     * The type Scatter widget fields.
     */
    public static class SCATTER_WIDGET_FIELDS {

        /**
         * The constant POINT_SIZE.
         */
        public static final String POINT_SIZE = "pointSize";

        /**
         * The constant POINT_COLOR.
         */
        public static final String POINT_COLOR = "pointColor";

    }

    /**
     * The type Line widget fields.
     */
    public static class LINE_WIDGET_FIELDS {

        /**
         * The constant SMOOTH.
         */
        public static final String SMOOTH = "smooth";

        /**
         * The constant LIN_WIDGET_TYPE.
         */
        public static final String LINE_WIDGET_TYPE = "lineWidgetType";

    }

    /**
     * The type Pst widget fields.
     */
    public static class PST_WIDGET_FIELDS {

        /**
         * The constant UPDATE_INTERVAL.
         */
        public static final String UPDATE_INTERVAL = "updateInterval";

        /**
         * The constant PRUFSTAND.
         */
        public static final String PRUFSTAND = "prufstand";

        /**
         * The constant PROGRAMM.
         */
        public static final String PROGRAMM = "programm";

        /**
         * The constant MOTORTYP.
         */
        public static final String MOTORTYP = "motortyp";

        /**
         * The constant MOTOR_BG.
         */
        public static final String MOTOR_BG = "motor_bg";

        /**
         * The constant STATUS.
         */
        public static final String STATUS = "status";

        /**
         * The constant VORBEREITUNG.
         */
        public static final String VORBEREITUNG = "vorbereitung";

    }

    /**
     * The type Url params.
     */
    public static class URL_PARAMS {

        /**
         * The constant PARAM_OBJECT_ID.
         */
        public static final String PARAM_OBJECT_ID = "{objectId}";

        /**
         * The constant PARAM_WIDGET_ID.
         */
        public static final String PARAM_WIDGET_ID = "{widgetId}";

        /**
         * The constant PARAM_SOURCE_TYPE.
         */
        public static final String PARAM_SOURCE_TYPE = "{sourceType}";

        /**
         * The constant PARAM_FIELD_NAME.
         */
        public static final String PARAM_FIELD_NAME = "{fieldName}";

        /**
         * The constant PARAM_ACTION.
         */
        public static final String PARAM_ACTION = "{action}";

        /**
         * The constant PARAM_SOURCE_ID.
         */
        public static final String PARAM_SOURCE_ID = "{sourceId}";

    }

    /**
     * The type Bind from urls.
     */
    public static class BIND_FROM_URLS {

        /**
         * The constant CREATE_OBJECT_UI_FORM_DATA_SOURCE.
         */
        public static final String CREATE_OBJECT_UI_FORM_DATA_SOURCE = "/data/dashboard/{objectId}/ui/create/dataSource/{__value__}";

        /**
         * The constant BIND_FROM_WIDGET_TYPES.
         */
        public static final String WIDGET_TYPES = "/data/dashboard/{objectId}/widget/form/{__value__}";

        /**
         * The constant DATA_SOURCE_TYPE.
         */
        public static final String DATA_SOURCE_TYPE = "/data/dashboard/{objectId}/widget/{widgetId}/sourceType/{__value__}";

        /**
         * The constant DATA_SOURCE.
         */
        public static final String DATA_SOURCE = "/data/dashboard/{objectId}/widget/{widgetId}/sourceType/{sourceType}/dataSource/{__value__}";

        /**
         * The constant OTHER_SOURCE.
         */
        public static final String OTHER_SOURCE = "/data/dashboard/{objectId}/widget/{widgetId}/source/other/ui/{__value__}";

        /**
         * The constant PYTHON_OUTPUT.
         */
        public static final String PYTHON_OUTPUT = "/data/dashboard/{objectId}/python/ui/{__value__}";

        /**
         * The constant PYTHON_SCRIPT_OPTIONS.
         */
        public static final String PYTHON_SCRIPT_OPTIONS = "/data/dashboard/{objectId}/python/options/ui/{__value__}";

        /**
         * The constant RESULT_TABLE.
         */
        public static final String MATERIAL_INSPECTOR = "/data/dashboard/{objectId}/widget/{widgetId}/materialInspector/ui/{fieldName}/{__value__}";

        /**
         * The constant METAL_BASE.
         */
        public static final String METAL_BASE = "/data/dashboard/{objectId}/widget/{widgetId}/metalBase/ui/{fieldName}/{__value__}";

        /**
         * The constant LINE_WIDGET_OPTIONS.
         */
        public static final String LINE_WIDGET_OPTIONS = "/data/dashboard/{objectId}/widget/{widgetId}/lineWidget/{__value__}";

        /**
         * The constant TEXT_INPUT_OPTIONS.
         */
        public static final String TEXT_INPUT_OPTIONS = "/data/dashboard/{objectId}/widget/{widgetId}/textWidget/{__value__}";

        /**
         * The constant ADD_CURVE_IN_MIX_CHART.
         */
        public static final String ADD_CURVE_IN_MIX_CHART = "/data/dashboard/{objectId}/widget/{widgetId}/mix/addCurve";

        /**
         * The constant ADD_VALUE_IN_RADAR_CHART.
         */
        public static final String ADD_VALUE_IN_RADAR_CHART = "/data/dashboard/{objectId}/widget/{widgetId}/radar/addValue";

        /**
         * The constant GET_CURVE_OPTIONS_IN_MIX_CHART.
         */
        public static final String GET_CURVE_OPTIONS_IN_MIX_CHART = "/data/dashboard/{objectId}/widget/{widgetId}/curveOptions/{__value__}";

    }

    /**
     * The type Bind to urls.
     */
    public static class BIND_TO_URLS {

        /**
         * The constant WIDGET_DATA_SOURCE_SCHEMAS.
         */
        public static final String WIDGET_DATA_SOURCE_SCHEMAS = "/data/dashboard/{objectId}/widget/{widgetId}/dataSource/schemas/{__value__}";

        /**
         * The constant WIDGET_DATA_SOURCE_SCHEMA_TABLES.
         */
        public static final String WIDGET_DATA_SOURCE_SCHEMA_TABLES = "/data/dashboard/{objectId}/widget/{widgetId}/dataSource/tables/{__value__}";

        /**
         * The constant WIDGET_DATA_SOURCE_TABLE_COLUMNS.
         */
        public static final String WIDGET_DATA_SOURCE_TABLE_COLUMNS = "/data/dashboard/{objectId}/widget/{widgetId}/sourceType/{sourceType}/columns/{__value__}";

        /**
         * The constant WIDGET_DATA_SOURCE_TABLE_COLUMNS.
         */
        public static final String WIDGET_GROUP_BY_FOR_AGGREGATE = "/data/dashboard/{objectId}/widget/{widgetId}/groupBy/{__value__}";

        /**
         * The constant MATERIAL_INSPECTOR.
         */
        public static final String MATERIAL_INSPECTOR = "/data/dashboard/{objectId}/widget/{widgetId}/materialInspector/{fieldName}/{__value__}";

        /**
         * The constant METAL_BASE.
         */
        public static final String METAL_BASE = "/data/dashboard/{objectId}/widget/{widgetId}/metalBase/{fieldName}/{__value__}";

        /**
         * The constant BIND_TO_URL_FOR_TEST_CONNECTION.
         */
        public static final String BIND_TO_URL_FOR_TEST_CONNECTION = "/data/dashboard/source/test";

        /**
         * The constant BIND_TO_URL_FOR_PREVIEW_CONNECTION.
         */
        public static final String BIND_TO_URL_FOR_PREVIEW_CONNECTION = "/data/dashboard/{objectId}/source/preview";

        /**
         * The constant BIND_TO_URL_FOR_UPDATE_CACHE.
         */
        public static final String BIND_TO_URL_FOR_UPDATE_CACHE = "/data/dashboard/{objectId}/source/{sourceId}/cache/update";

        /**
         * The constant WIDGET_ACTION.
         */
        public static final String WIDGET_ACTION = "/data/dashboard/{objectId}/widget/{widgetId}/action/{action}";

    }

    /**
     * The type Sql constants.
     */
    public static class SQL_CONSTANTS {

        /**
         * The constant SELECT_1.
         */
        public static final String SELECT_1 = "SELECT 1";

        /**
         * The constant TABLE_SCHEM.
         */
        public static final String TABLE_SCHEM = "TABLE_SCHEM";

        /**
         * The constant TABLE_NAME.
         */
        public static final String TABLE_NAME = "TABLE_NAME";

        /**
         * The constant TABLE.
         */
        public static final String TABLE = "TABLE";

        /**
         * The constant COLUMN_NAME.
         */
        public static final String COLUMN_NAME = "COLUMN_NAME";

        /**
         * The constant TYPE_NAME.
         */
        public static final String TYPE_NAME = "TYPE_NAME";

        /**
         * The constant X_AXIS_ALIAS.
         */
        public static final String X_AXIS_ALIAS = "x_axis";

        /**
         * The constant Y_AXIS_ALIAS.
         */
        public static final String Y_AXIS_ALIAS = "y_axis";

        /**
         * The constant BAR_COLOR_ALIAS.
         */
        public static final String BAR_COLOR_ALIAS = "barcolor";

        /**
         * The constant POINT_COLOR_ALIAS.
         */
        public static final String POINT_COLOR_ALIAS = "pointcolor";

        /**
         * The constant POINT_SIZE_ALIAS.
         */
        public static final String POINT_SIZE_ALIAS = "pointsize";

    }

}