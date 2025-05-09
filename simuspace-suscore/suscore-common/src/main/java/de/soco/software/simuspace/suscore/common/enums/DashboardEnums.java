package de.soco.software.simuspace.suscore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The type Dashboard enums.
 */
public class DashboardEnums {

    /**
     * The enum Widget type options.
     */
    @Getter
    @AllArgsConstructor
    public enum WIDGET_TYPE {

        /**
         * The Multi chart 2 d.
         */
        MIX_CHART( "mixChart", "Mix Chart" ),

        /**
         * Bar widget type options.
         */
        BAR( "bar", "Bar Chart" ),

        /**
         * Scatter widget type options.
         */
        SCATTER( "scatter", "Scatter Plot" ),

        /**
         * The Line.
         */
        LINE( "line", "Line Chart" ),

        /**
         * The Pst.
         */
        PST( "pst", "Pst Widget" ),

        /**
         * Treemap widget type.
         */
        TREEMAP( "icicle", "Treemap" ),

        /**
         * Text widget type.
         */
        TEXT( "text", "Text" ),

        /**
         * Radar widget type.
         */
        RADAR( "radar", "Radar" ),

        /**
         * The Metal base.
         */
        METAL_BASE( "metalBase", "Metal Base" ),

        /**
         * Group widget type.
         */
        GROUP( "group", "Group" ),

        /**
         * Table widget type.
         */
        TABLE( "table", "Table" ),

        PROJECT_LIFE_CYCLE( "projectLifeCycle", "Project Life Cycle" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static WIDGET_TYPE getById( String id ) {
            for ( WIDGET_TYPE option : WIDGET_TYPE.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Widget python output options.
     */
    @AllArgsConstructor
    @Getter
    public enum WidgetPythonOutputOptions {

        /**
         * The Csv.
         */
        CSV( "csv", "CSV File" ),

        /**
         * The Json.
         */
        JSON( "json", "Json File" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static WidgetPythonOutputOptions getById( String id ) {
            for ( WidgetPythonOutputOptions option : WidgetPythonOutputOptions.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Widget data from options.
     */
    @Getter
    @AllArgsConstructor
    public enum WIDGET_SOURCE_TYPE {

        /**
         * The Data source.
         */
        QUERY_BUILDER( "queryBuilder", "Query Builder" ),

        /**
         * Py thon widget data source type options.
         */
        PYTHON( "python", "Python" ),

        /**
         * Other widget data source type options.
         */
        OTHER( "other", "Other" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static WIDGET_SOURCE_TYPE getById( String id ) {
            for ( WIDGET_SOURCE_TYPE option : WIDGET_SOURCE_TYPE.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Data source database options.
     */
    @AllArgsConstructor
    @Getter
    public enum DataSourceDatabaseOptions {

        /**
         * The Postgres.
         */
        POSTGRES( "postgresql", "Postgres Database" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;
    }

    /**
     * The enum Dashboard data source options.
     */
    @Getter
    @AllArgsConstructor
    public enum DashboardDataSourceOptions {

        /**
         * The Database.
         */
        DATABASE( "database", "External Database" ),

        /**
         * The Csv.
         */
        CSV( "csv", "CSV File" ),

        /**
         * The Excel.
         */
        EXCEL( "excel", "Excel File" ),

        /**
         * The Json.
         */
        JSON( "json", "JSON File" ),

        /**
         * The Sus object.
         */
        SUS_OBJECT( "sus-object", "SuS Object" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static DashboardDataSourceOptions getById( String id ) {
            for ( DashboardDataSourceOptions option : DashboardDataSourceOptions.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Driver class name.
     */
    @AllArgsConstructor
    @Getter
    public enum DriverClassName {

        /**
         * Postgres driver class name.
         */
        POSTGRES( DashboardEnums.DataSourceDatabaseOptions.POSTGRES.getId(), "org.postgresql.Driver" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Class name.
         */
        private final String className;

        /**
         * Gets class name by id.
         *
         * @param id
         *         the id
         *
         * @return the class name by id
         */
        public static String getClassNameById( String id ) {
            for ( DriverClassName driverClassName : DriverClassName.values() ) {
                if ( driverClassName.id.equals( id ) ) {
                    return driverClassName.className;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
        }
    }

    /**
     * The enum Driver class name.
     */
    @AllArgsConstructor
    @Getter
    public enum SqlAggregateOptions {

        /**
         * Count sql aggregate options.
         */
        COUNT( "count", "Count" ),

        /**
         * Sum sql aggregate options.
         */
        SUM( "sum", "Sum" ),

        /**
         * Avg sql aggregate options.
         */
        AVG( "avg", "Average" ),

        /**
         * Variance sql aggregate options.
         */
        VARIANCE( "variance", "Variance" ),

        /**
         * The Stddev.
         */
        STDDEV( "stddev", "Standard Deviation" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Class name.
         */
        private final String name;

        /**
         * Gets class name by id.
         *
         * @param id
         *         the id
         *
         * @return the class name by id
         */
        public static SqlAggregateOptions getById( String id ) {
            for ( SqlAggregateOptions aggregateOption : SqlAggregateOptions.values() ) {
                if ( aggregateOption.id.equals( id ) ) {
                    return aggregateOption;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
        }
    }

    /**
     * The enum Color schemes.
     */
    @AllArgsConstructor
    @Getter
    public enum COLOR_SCHEMES {

        /**
         * Vintage color schemes.
         */
        VINTAGE( "vintage", "Vintage" ),

        /**
         * Dark color schemes.
         */
        DARK( "dark", "Dark" ),

        /**
         * Westeros color schemes.
         */
        WESTEROS( "westeros", "Westeros" ),

        /**
         * Essos color schemes.
         */
        ESSOS( "essos", "Essos" ),

        /**
         * Wonderland color schemes.
         */
        WONDERLAND( "wonderland", "Wonderland" ),

        /**
         * Walden color schemes.
         */
        WALDEN( "walden", "Walden" ),

        /**
         * Chalk color schemes.
         */
        CHALK( "chalk", "Chalk" ),

        /**
         * Infographic color schemes.
         */
        INFOGRAPHIC( "infographic", "Infographic" ),

        /**
         * Macarons color schemes.
         */
        MACARONS( "macarons", "Macarons" ),

        /**
         * Roma color schemes.
         */
        ROMA( "roma", "Roma" ),

        /**
         * Shine color schemes.
         */
        SHINE( "shine", "Shine" ),

        /**
         * The Purplepassion.
         */
        PURPLE_PASSION( "purple-passion", "Purple Passion" ),

        /**
         * Halloween color schemes.
         */
        HALLOWEEN( "halloween", "Halloween" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Class name.
         */
        private final String name;
    }

    /**
     * The enum Script status option.
     */
    @AllArgsConstructor
    @Getter
    public enum ScriptStatusOption {

        /**
         * The In progress.
         */
        IN_CHANGE( "inChange", "In Change" ),

        /**
         * Approved script status option.
         */
        FINAL( "final", "Final" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static ScriptStatusOption getById( String id ) {
            for ( ScriptStatusOption scriptStatusOption : ScriptStatusOption.values() ) {
                if ( scriptStatusOption.id.equals( id ) ) {
                    return scriptStatusOption;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
        }

    }

    /**
     * The enum Csv delimiter.
     */
    @AllArgsConstructor
    @Getter
    public enum CSVDelimiter {

        /**
         * Comma csv delimiter.
         */
        COMMA( ",", "Comma (,)" ),

        /**
         * Semicolon csv delimiter.
         */
        SEMICOLON( ";", "Semicolon (;)" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static CSVDelimiter getById( String id ) {
            for ( CSVDelimiter csvDelimiter : CSVDelimiter.values() ) {
                if ( csvDelimiter.id.equals( id ) ) {
                    return csvDelimiter;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
        }
    }

    /**
     * The enum Widget script option.
     */
    @AllArgsConstructor
    @Getter
    public enum WIDGET_SCRIPT_OPTION {

        /**
         * The System.
         */
        SYSTEM( "system", "Use system script" ),
        /**
         * The User.
         */
        USER( "user", "Write Code" ),
        /**
         * The File.
         */
        FILE( "file", "Select file from Server" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static WIDGET_SCRIPT_OPTION getById( String id ) {
            for ( WIDGET_SCRIPT_OPTION scriptOption : WIDGET_SCRIPT_OPTION.values() ) {
                if ( scriptOption.id.equals( id ) ) {
                    return scriptOption;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), id ) );
        }
    }

    /**
     * The enum Other widget source.
     */
    @AllArgsConstructor
    @Getter
    public enum OTHER_WIDGET_SOURCE {

        /**
         * Other other widget source.
         */
        MATERIAL_INSPECTION( "materialInspection", "Material Inspection" ),

        /**
         * The Pst.
         */
        PST( "pst", "PST Planning" ),

        /**
         * The Metal base.
         */
        METAL_BASE( "metalBase", "Metal Base" ),

        /**
         * The vMCL.
         */
        VMCL( "vMcl", "vMCL" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static OTHER_WIDGET_SOURCE getById( String id ) {
            for ( OTHER_WIDGET_SOURCE option : OTHER_WIDGET_SOURCE.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Mat inspect result tables.
     */
    @AllArgsConstructor
    @Getter
    public enum MAT_INSPECT_RESULT_TABLE {

        /**
         * Md flow curve mat inspect result tables.
         */
        md_flow_curve,

        /**
         * Md bulge mat inspect result tables.
         */
        md_bulge,

        /**
         * Md flc mat inspect result tables.
         */
        md_flc;

        /**
         * Gets by name.
         *
         * @param name
         *         the name
         *
         * @return the by name
         */
        public static MAT_INSPECT_RESULT_TABLE getByName( String name ) {
            for ( MAT_INSPECT_RESULT_TABLE option : MAT_INSPECT_RESULT_TABLE.values() ) {
                if ( name.equals( option.name() ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), name ) );
        }

    }

    /**
     * The enum Pst actions.
     */
    @AllArgsConstructor
    @Getter
    public enum PST_ACTIONS {

        /**
         * Update pst actions.
         */
        update,

        /**
         * Add form pst actions.
         */
        add_form,

        /**
         * Add test pst actions.
         */
        add_test,

        /**
         * Edit form pst actions.
         */
        edit_form,

        /**
         * Edit test pst actions.
         */
        edit_test,

        /**
         * Couple test pst actions.
         */
        couple_test,

        /**
         * Remove couple pst actions.
         */
        remove_couple,

        /**
         * Export differences pst actions.
         */
        export_differences,

        /**
         * Add archive pst actions.
         */
        add_archive,

        /**
         * Read archive pst actions.
         */
        read_archive,

        /**
         * Delete archive pst actions.
         */
        delete_archive,

        /**
         * List archive pst actions.
         */
        list_archives,

        /**
         * Get benches pst actions.
         */
        get_benches,

        /**
         * Get pst actions.
         */
        get,

        /**
         * Restore archive pst actions.
         */
        restore_archive;

        /**
         * Gets by name.
         *
         * @param name
         *         the name
         *
         * @return the by name
         */
        public static PST_ACTIONS getByName( String name ) {
            for ( PST_ACTIONS option : PST_ACTIONS.values() ) {
                if ( name.equals( option.name() ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), name ) );
        }

    }

    /**
     * The enum Widget relation.
     */
    @AllArgsConstructor
    @Getter
    public enum WIDGET_RELATION {

        /**
         * Is not in group widget relation.
         */
        IS_NOT_IN_GROUP( 0 ),

        /**
         * Is in group widget relation.
         */
        IS_IN_GROUP( 1 ),

        /**
         * Is group widget relation.
         */
        IS_GROUP( 2 );

        /**
         * The Key.
         */
        private final int key;

    }

    /**
     * The enum Line widget options.
     */
    @AllArgsConstructor
    @Getter
    public enum LINE_WIDGET_OPTIONS {

        /**
         * The Multiple x single y.
         */
        MULTIPLE_X_SINGLE_Y( "multipleX", "Multiple X Axis" ),

        /**
         * The Multiple y single x.
         */
        MULTIPLE_Y_SINGLE_X( "multipleY", "Multiple Y Axis" ),

        /**
         * The Multiple x multiple y.
         */
        MULTIPLE_X_MULTIPLE_Y( "multipleXY", "Multiple X and Y axis" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static LINE_WIDGET_OPTIONS getById( String id ) {
            for ( LINE_WIDGET_OPTIONS option : LINE_WIDGET_OPTIONS.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Line style.
     */
    @AllArgsConstructor
    @Getter
    public enum LINE_STYLE {

        /**
         * Dashed line style.
         */
        DASHED( "dashed", "Dashed" ),

        /**
         * Solid line style.
         */
        SOLID( "solid", "Solid" ),

        /**
         * Dotted line style.
         */
        DOTTED( "dotted", "Dotted" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

    }

    /**
     * The enum Multi chart plot type.
     */
    @AllArgsConstructor
    @Getter
    public enum MULTI_CHART_PLOT_TYPE {

        /**
         * Bar widget type options.
         */
        BAR( "bar", "Bar" ),

        /**
         * Scatter widget type options.
         */
        SCATTER( "scatter", "Scatter" ),

        /**
         * The Line.
         */
        LINE( "line", "Line" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static MULTI_CHART_PLOT_TYPE getById( String id ) {
            for ( MULTI_CHART_PLOT_TYPE option : MULTI_CHART_PLOT_TYPE.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Point symbol.
     */
    @AllArgsConstructor
    @Getter
    public enum POINT_SYMBOL {

        /**
         * Circle point symbol.
         */
        EMPTY_CIRCLE( "emptyCircle", "Empty Circle" ),

        /**
         * Circle point symbol.
         */
        CIRCLE( "circle", "Circle" ),

        /**
         * Rect point symbol.
         */
        RECT( "rect", "Rectangle" ),

        /**
         * The Round rect.
         */
        ROUND_RECT( "roundRect", "Round Rectangle" ),

        /**
         * Triangle point symbol.
         */
        TRIANGLE( "triangle", "Triangle" ),

        /**
         * Diamond point symbol.
         */
        DIAMOND( "diamond", "Diamond" ),

        /**
         * Pin point symbol.
         */
        PIN( "pin", "Pin" ),

        /**
         * Arrow point symbol.
         */
        ARROW( "arrow", "Arrow" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

    }

    /**
     * The enum Text widget input method.
     */
    @AllArgsConstructor
    @Getter
    public enum TEXT_WIDGET_INPUT_METHOD {

        /**
         * The User.
         */
        USER( "user", "User Input" ),

        /**
         * The Select.
         */
        SELECT( "select", "Select From Source" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static TEXT_WIDGET_INPUT_METHOD getById( String id ) {
            for ( TEXT_WIDGET_INPUT_METHOD option : TEXT_WIDGET_INPUT_METHOD.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }
    }

    /**
     * The enum Radar shape.
     */
    @AllArgsConstructor
    @Getter
    public enum RADAR_SHAPE {

        /**
         * Polygon radar shape.
         */
        POLYGON( "polygon", "Polygon" ),

        /**
         * Circle radar shape.
         */
        CIRCLE( "circle", "Circle" );

        /**
         * The Id.
         */
        private final String id;

        /**
         * The Name.
         */
        private final String name;

        /**
         * Gets by id.
         *
         * @param id
         *         the id
         *
         * @return the by id
         */
        public static RADAR_SHAPE getById( String id ) {
            for ( RADAR_SHAPE option : RADAR_SHAPE.values() ) {
                if ( option.getId().equals( id ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), id ) );
        }

    }

    /**
     * The enum Metal base action.
     */
    @AllArgsConstructor
    @Getter
    public enum METAL_BASE_ACTION {

        /**
         * Review status metal base action.
         */
        review_status,

        /**
         * Export data metal base action.
         */
        export_data,

        /**
         * Export graphics metal base action.
         */
        export_graphics,

        /**
         * Plot data metal base action.
         */
        plotData;

        /**
         * Gets by name.
         *
         * @param name
         *         the name
         *
         * @return the by name
         */
        public static METAL_BASE_ACTION getByName( String name ) {
            for ( METAL_BASE_ACTION option : METAL_BASE_ACTION.values() ) {
                if ( name.equals( option.name() ) ) {
                    return option;
                }
            }
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_VALUE.getKey(), name ) );
        }
    }

}
