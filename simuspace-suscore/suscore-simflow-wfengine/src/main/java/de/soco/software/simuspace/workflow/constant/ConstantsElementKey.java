package de.soco.software.simuspace.workflow.constant;

/**
 * Utility class which will contain all the constants for element key.
 *
 * @author Zeeshan Jamal
 */
public class ConstantsElementKey {

    /**
     * The key for workflow IO element. i.e. wfe_io
     */
    public static final String WFE_IO = "wfe_io";

    /**
     * The key for workflow script element. i.e. wfe_script
     */
    public static final String WFE_SCRIPT = "wfe_script";

    /**
     * The key for workflow sus file element. i.e. wfe_sus_file
     */
    public static final String WFE_SUS_EXPORT_FILES = "wfe_export_files";

    /**
     * The key for workflow wait element. i.e. wfe_wait
     */
    public static final String WFE_WAIT = "wfe_wait";

    /**
     * The key for workflow conditional element. i.e. wfe_condition
     */
    public static final String WFE_CONDITIONAL = "wfe_condition";

    /**
     * The Constant WFE_IMPORT_PROCESS.
     */
    public static final String WFE_SUS_IMPORT_OBJECTS = "wfe_import_objects";

    /**
     * The key for workflow dataobject export element. i.e. wfe_export_dataobject
     */
    public static final String WFE_SUS_EXPORT_DATAOBJECT = "wfe_export_dataobject";

    /**
     * The key for workflow dataobject import element. i.e. wfe_dataObject_import
     */
    public static final String WFE_SUS_IMPORT_DATAOBJECT = "wfe_import_dataobject";

    /**
     * The Constant WFE_SUS_TRANSFER_LOCATION.
     */
    public static final String WFE_SUS_TRANSFER_LOCATION = "wfe_transfer_location";

    /**
     * The Constant WFE_SCRIPT_PYTHON.
     */
    public static final String WFE_SCRIPT_PYTHON = "wfe_python";

    /**
     * The Constant WFE_SUS_ACTION
     */
    public static final String WFE_SUS_ACTION = "wfe_sus_action";

    /**
     * The key for subworkflow.
     */
    public static final String WFE_SUB_WORKFLOW = "wfe_subworkflow";

    /**
     * The key for subworkflow.
     */
    public static final String WFE_IMPORT_CB2 = "wfe_import_cb2";

    /**
     * The key for workflow hpc WFE_ABAQUS.
     */
    public static final String WFE_ABAQUS_EXPLIZIT = "wfe_abaqusexplizit";

    /**
     * The key for workflow hpc WFE_ABAQUS.
     */
    public static final String WFE_ABAQUS_STANDARD = "wfe_abaqusstandard";

    /**
     * The key for workflow hpc WFE_LSDYNA.
     */
    public static final String WFE_LSDYNA = "wfe_lsDyna";

    /**
     * The key for workflow hpc WFE_NASTRAN.
     */
    public static final String WFE_NASTRAN = "wfe_nastran";

    /**
     * The key for workflow hpc WFE_OPTISTRUCT.
     */
    public static final String WFE_OPTISTRUCT = "wfe_optistruct";

    /**
     * The key for workflow WFE_EMAIL.
     */
    public static final String WFE_EMAIL = "wfe_email";

    /**
     * The Constant WFE_ASSEMBLE_AND_SIMULATE.
     */
    public static final String WFE_ASSEMBLE_AND_SIMULATE = "wfe_assemble_and_simulate";

    /**
     * The key for workflow WFE_MACHINE_LEARNING.
     */
    public static final String WFE_MACHINE_LEARNING = "wfe_train_model_sus";

    /**
     * The Constant WFE_FOREACHLOOP.
     */
    public static final String WFE_FOREACHLOOP = "wfe_foreachloop";

    /**
     * Private constructor to prevent instantiation.
     */
    private ConstantsElementKey() {
        // Private constructor to hide the implicit public one.
    }

}
