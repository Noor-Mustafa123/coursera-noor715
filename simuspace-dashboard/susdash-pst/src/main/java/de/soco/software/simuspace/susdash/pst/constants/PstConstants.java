package de.soco.software.simuspace.susdash.pst.constants;

/**
 * The type Pst util.
 */
public class PstConstants {

    /**
     * The constant ACE.
     */
    public static final String ACE = "ace";

    /**
     * The constant DAILY.
     */
    public static final String DAILY = "daily";

    /**
     * The constant NAME.
     */
    public static final String NAME = "name";

    /**
     * The constant INPUT_POSTFIX.
     */
    public static final String INPUT_POSTFIX = "_input";

    /**
     * The constant OUTPUT_POSTFIX.
     */
    public static final String OUTPUT_POSTFIX = "_output";

    /**
     * The constant EXPORT_DIFFERENCES_FILE_NAME.
     */
    public static final String EXPORT_DIFFERENCES_TEMPLATE = "Aenderungen-in-PST-Planung-%s.csv";

    /**
     * The type CONFIG_KEYS.
     */
    public static final class CONFIG_KEYS {

        /**
         * Instantiates a new CONFIG_KEYS.
         */
        private CONFIG_KEYS() {

        }

        /**
         * The constant PLANNING_SCRIPT_NAME.
         */
        public static final String PLANNING_SCRIPT_NAME = "pst_planning";

        /**
         * The constant BASE_PATH.
         */
        public static final String BASE_PATH = "basePath";

        /**
         * The constant DAILY_STATUS.
         */
        public static final String DAILY_STATUS = "Daily-Status";

        /**
         * The constant UPDATE_TIME.
         */
        public static final String UPDATE_TIME = "updateTime";

        /**
         * The constant TEST_JSON.
         */
        public static final String TEST_JSON = "testJson";

        /**
         * The constant BENCH_JSON.
         */
        public static final String BENCH_JSON = "benchJson";

        /**
         * The constant ACE_LOUNGE_CSV_
         */
        public static final String ACE_LUNGE_CSV = "ACE-LoungeCsv";

        /**
         * The constant AUTO_UPDATE.
         */
        public static final String AUTO_UPDATE = "autoUpdate";

        /**
         * The constant CONFIG_FILE.
         */
        public static final String CONFIG_FILE = "configFile";

        /**
         * The constant FORM_JSON.
         */
        public static final String FORM_JSON = "formJson";

        /**
         * The constant MASCHINE_JSON.
         */
        public static final String MASCHINE_JSON = "maschineJson";

        /**
         * The constant ARCHIVE_JSON.
         */
        public static final String ARCHIVE_JSON = "archiveJson";

        /**
         * The constant RUNNING_TESTS.
         */
        public static final String RUNNING_TESTS = "runningTests";

        /**
         * The constant IDS_JSON.
         */
        public static final String IDS_JSON = "IDsJson";

    }

    /**
     * The type Actions.
     */
    public static final class ACTIONS {

        /**
         * Instantiates a new Actions.
         */
        private ACTIONS() {

        }

        /**
         * The constant UPDATE_ACE.
         */
        public static final String UPDATE_ACE = "update_ace";

        /**
         * The constant UPDATE_DAILY.
         */
        public static final String UPDATE_DAILY = "update_daily";

        /**
         * The constant CREATE.
         */
        public static final String CREATE = "create";

        /**
         * The constant UPDATE_TEST.
         */
        public static final String EDIT_TEST = "edit_test";

        /**
         * The constant ADD_TEST.
         */
        public static final String ADD_TEST = "add_test";

        /**
         * The constant COUPLE_TEST.
         */
        public static final String COUPLE_TEST = "couple_test";

        /**
         * The constant UPDATE_PREREQ.
         */
        public static final String UPDATE_PREREQ = "update_prereq";

        /**
         * The constant EXPORT.
         */
        public static final String EXPORT = "export";

        /**
         * The constant CREATE_ARCHIVE.
         */
        public static final String ADD_ARCHIVE = "add_archive";

        /**
         * The constant GET_FORM.
         */
        public static final String GET_FORM = "get_form";

        /**
         * The constant GET_LEGEND.
         */
        public static final String GET_LEGEND = "get_legend";

        /**
         * The constant EXPORT_DIFFERENCES.
         */
        public static final String EXPORT_DIFFERENCES = "export_differences";

    }

    /**
     * The type Json properties.
     */
    public static final class JSON_PROPERTIES {

        /**
         * Instantiates a new Json properties.
         */
        private JSON_PROPERTIES() {

        }

        /**
         * The constant STATUS.
         */
        public static final String STATUS = "Status";

        /**
         * The constant ID.
         */
        public static final String ID = "ID";

        /**
         * The constant VERKN.
         */
        public static final String VERKN = "Verkn";

        /**
         * The constant MOTOR_NR.
         */
        public static final String MOTOR_NR = "Motornr";

        /**
         * The constant MOTOR_TYPE.
         */
        public static final String MOTOR_TYPE = "Motortyp";

        /**
         * The constant MOTOR_BG.
         */
        public static final String MOTOR_BG = "Motor-BG";

        /**
         * The constant PROGRAM.
         */
        public static final String PROGRAM = "Programm";

        /**
         * The constant TOTAL_CYCLES.
         */
        public static final String TOTAL_CYCLES = "Soll Zyk/km";

        /**
         * The constant CURRENT_CYCLES.
         */
        public static final String CURRENT_CYCLES = "Erreicht Zyk/km";

        /**
         * The constant PROGRESS.
         */
        public static final String PROGRESS = "Erreicht %";

        /**
         * The constant PST_ORT.
         */
        public static final String PST_ORT = "PST Ort";

        /**
         * The constant ACTUAL_START_DATE.
         */
        public static final String ACTUAL_START_DATE = "Start Dauerlauf Planung";

        /**
         * The constant ACTUAL_END_DATE.
         */
        public static final String ACTUAL_END_DATE = "Ende Dauerlauf Planung";

        /**
         * The constant PLANNED_START_DATE.
         */
        public static final String PLANNED_START_DATE = "DL-Start Prog/Ist";

        /**
         * The constant PLANNED_END_DATE.
         */
        public static final String PLANNED_END_DATE = "DL-Ende Prog/Ist";

    }

    /**
     * The type Input file keys.
     */
    public static final class INPUT_FILE_KEYS {

        /**
         * Instantiates a new Input file keys.
         */
        private INPUT_FILE_KEYS() {

        }

        /**
         * The constant ACTION.
         */
        public static final String ACTION = "action";

        /**
         * The constant PAYLOAD.
         */
        public static final String PAYLOAD = "payload";

        /**
         * The constant OUTPUT_FILE.
         */
        public static final String OUTPUT_FILE = "outputFile";

        /**
         * The constant ARCHIVE.
         */
        public static final String ARCHIVE = "archive";

        /**
         * The constant LANGUAGE.
         */
        public static final String LANGUAGE = "lang";

    }

}
