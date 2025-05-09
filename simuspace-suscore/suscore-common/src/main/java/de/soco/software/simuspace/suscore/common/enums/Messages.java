package de.soco.software.simuspace.suscore.common.enums;

/**
 * The enum Messages.
 */
public enum Messages {

    /**
     * The Utils name cant be null.
     */
    // util messages
    UTILS_NAME_CANT_BE_NULL( "0100001x4" ),

    /**
     * Utils value too large messages.
     */
    UTILS_VALUE_TOO_LARGE( "0100002x4" ),

    /**
     * Utils invalid value messages.
     */
    UTILS_INVALID_VALUE( "0100003x4" ),

    /**
     * Invalid uuid messages.
     */
    INVALID_UUID( "0100004x4" ),

    /**
     * Utils invalid special chars messages.
     */
    UTILS_INVALID_SPECIAL_CHARS( "0100005x4" ),

    /**
     * Record not deleted messages.
     */
    RECORD_NOT_DELETED( "0100006x4" ),

    /**
     * File not found messages.
     */
    FILE_NOT_FOUND( "0100007x4" ),

    /**
     * Invalid zip paths messages.
     */
    INVALID_ZIP_PATHS( "0100008x4" ),
    /**
     * File not zip messages.
     */
    FILE_NOT_ZIP( "0100009x4" ),

    /**
     * Mode not supported messages.
     */
    MODE_NOT_SUPPORTED( "0100010x4" ),

    /**
     * Record deleted messages.
     */
    RECORD_DELETED( "0100011x4" ),

    /**
     * Name should not have special character messages.
     */
    NAME_SHOULD_NOT_HAVE_SPECIAL_CHARACTER( "0100012x4" ),

    /**
     * Record restored messages.
     */
    RECORD_RESTORED( "0100013x4" ),

    /**
     * Unable to export to path messages.
     */
    UNABLE_TO_EXPORT_TO_PATH( "0100014x4" ),

    /**
     * Utils invalid value in messages.
     */
    UTILS_INVALID_VALUE_IN( "0100015x4" ),

    /**
     * The Webservice json parsing error.
     */
    // webservice messages
    WEBSERVICE_JSON_PARSING_ERROR( "0200001x4" ),

    /**
     * Webservice invalid parameters suplied messages.
     */
    WEBSERVICE_INVALID_PARAMETERS_SUPLIED( "0200002x4" ),

    /**
     * Webservice invalid property value messages.
     */
    WEBSERVICE_INVALID_PROPERTY_VALUE( "0200003x4" ),

    /**
     * Webservice disabled api messages.
     */
    WEBSERVICE_DISABLED_API( "0200004x4" ),

    /**
     * Webservice input cant be null messages.
     */
    WEBSERVICE_INPUT_CANT_BE_NULL( "0200005x4" ),

    /**
     * Webservice internal server error messages.
     */
    WEBSERVICE_INTERNAL_SERVER_ERROR( "0200006x4" ),

    /**
     * Serialization error messages.
     */
    SERIALIZATION_ERROR( "0200007x4" ),

    // object messages

    /**
     * Group delete successfully messages.
     */
    GROUP_DELETE_SUCCESSFULLY( "0300044x4" ),

    /**
     * Group updated successfully messages.
     */
    GROUP_UPDATED_SUCCESSFULLY( "0300043x4" ),

    /**
     * Group created successfully messages.
     */
    GROUP_CREATED_SUCCESSFULLY( "0300042x4" ),

    /**
     * Role delete successfully messages.
     */
    ROLE_DELETE_SUCCESSFULLY( "0300041x4" ),

    /**
     * Role updated successfully messages.
     */
    ROLE_UPDATED_SUCCESSFULLY( "0300040x4" ),

    /**
     * Role created successfully messages.
     */
    ROLE_CREATED_SUCCESSFULLY( "0300039x4" ),

    /**
     * User updated successfully messages.
     */
    USER_UPDATED_SUCCESSFULLY( "0300038x4" ),

    /**
     * User created successfully messages.
     */
    USER_CREATED_SUCCESSFULLY( "0300037x4" ),

    /**
     * Directory updated successfully messages.
     */
    DIRECTORY_UPDATED_SUCCESSFULLY( "0300036x4" ),

    /**
     * Directory created successfully messages.
     */
    DIRECTORY_CREATED_SUCCESSFULLY( "0300035x4" ),

    /**
     * License updated successfully messages.
     */
    LICENSE_UPDATED_SUCCESSFULLY( "0300034x4" ),

    /**
     * License generated successfully messages.
     */
    LICENSE_GENERATED_SUCCESSFULLY( "0300033x4" ),

    /**
     * Object updated successfully messages.
     */
    OBJECT_UPDATED_SUCCESSFULLY( "0300032x4" ),

    /**
     * File updated successfully messages.
     */
    FILE_UPDATED_SUCCESSFULLY( "0300031x4" ),

    /**
     * Matadata updated successfully messages.
     */
    MATADATA_UPDATED_SUCCESSFULLY( "0300030x4" ),

    /**
     * Project updated successfully messages.
     */
    PROJECT_UPDATED_SUCCESSFULLY( "0300029x4" ),

    /**
     * Record updated successfully messages.
     */
    RECORD_UPDATED_SUCCESSFULLY( "0300028x4" ),

    /**
     * Project and dependencies deleted successfully messages.
     */
    PROJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY( "0300027x4" ),

    /**
     * Object created successfully messages.
     */
    OBJECT_CREATED_SUCCESSFULLY( "0300026x4" ),

    /**
     * Object is not customizable messages.
     */
    OBJECT_IS_NOT_CUSTOMIZABLE( "0300001x4" ),

    /**
     * Object type is not valid messages.
     */
    OBJECT_TYPE_IS_NOT_VALID( "0300002x4" ),

    /**
     * Object name cannot empty messages.
     */
    OBJECT_NAME_CANNOT_EMPTY( "0300003x4" ),

    /**
     * Object class name cannot empty messages.
     */
    OBJECT_CLASS_NAME_CANNOT_EMPTY( "0300004x4" ),

    /**
     * Object contain list cannot empty messages.
     */
    OBJECT_CONTAIN_LIST_CANNOT_EMPTY( "0300005x4" ),

    /**
     * Object custom attributes cannot empty messages.
     */
    OBJECT_CUSTOM_ATTRIBUTES_CANNOT_EMPTY( "0300006x4" ),

    /**
     * Object type content cannot empty messages.
     */
    OBJECT_TYPE_CONTENT_CANNOT_EMPTY( "0300007x4" ),

    /**
     * Object type created on cannot empty messages.
     */
    OBJECT_TYPE_CREATED_ON_CANNOT_EMPTY( "0300008x4" ),

    /**
     * Scheme name cannot empty messages.
     */
    SCHEME_NAME_CANNOT_EMPTY( "0300009x4" ),

    /**
     * Object type id cannot empty messages.
     */
    OBJECT_TYPE_ID_CANNOT_EMPTY( "0300010x4" ),

    /**
     * Parent id is not valid messages.
     */
    PARENT_ID_IS_NOT_VALID( "0300011x4" ),

    /**
     * Parent id cannot be empty messages.
     */
    PARENT_ID_CANNOT_BE_EMPTY( "0300012x4" ),

    /**
     * Object type empty messages.
     */
    OBJECT_TYPE_EMPTY( "0300013x4" ),

    /**
     * Object not exist with id messages.
     */
    OBJECT_NOT_EXIST_WITH_ID( "0300014x4" ),

    /**
     * Object class is not valid messages.
     */
    OBJECT_CLASS_IS_NOT_VALID( "0300015x4" ),

    /**
     * Object not exist with id and version messages.
     */
    OBJECT_NOT_EXIST_WITH_ID_AND_VERSION( "0300016x4" ),

    /**
     * Objects types not found messages.
     */
    OBJECTS_TYPES_NOT_FOUND( "0300017x4" ),

    /**
     * Object not exist with object type messages.
     */
    OBJECT_NOT_EXIST_WITH_OBJECT_TYPE( "0300018x4" ),

    /**
     * Project not exist with id messages.
     */
    PROJECT_NOT_EXIST_WITH_ID( "0300019x4" ),

    /**
     * No objects found messages.
     */
    NO_OBJECTS_FOUND( "0300020x4" ),

    /**
     * Object and dependencies deleted successfully messages.
     */
    OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY( "0300021x4" ),

    /**
     * Object and dependencies restore successfully messages.
     */
    OBJECT_AND_DEPENDENCIES_RESTORE_SUCCESSFULLY( "0300051x4" ),

    /**
     * Count object and dependencies deleted successfully messages.
     */
    COUNT_OBJECT_AND_DEPENDENCIES_DELETED_SUCCESSFULLY( "0300049x4" ),

    /**
     * Source object and destination object cannot be same messages.
     */
    SOURCE_OBJECT_AND_DESTINATION_OBJECT_CANNOT_BE_SAME( "0300056x4" ),

    /**
     * Cannot move data objects to workflow project messages.
     */
    CANNOT_MOVE_DATA_OBJECTS_TO_WORKFLOW_PROJECT( "0300057x4" ),

    /**
     * Only projects can move to labels messages.
     */
    ONLY_PROJECTS_CAN_MOVE_TO_LABELS( "0300058x4" ),

    /**
     * Cannot move to children objects messages.
     */
    CANNOT_MOVE_TO_CHILDREN_OBJECTS( "0300059x4" ),

    /**
     * Cannot move because target project have different configurations messages.
     */
    CANNOT_MOVE_BECAUSE_TARGET_PROJECT_HAVE_DIFFERENT_CONFIGURATIONS( "0300060x4" ),

    /**
     * Cannot move checked out objects messages.
     */
    CANNOT_MOVE_CHECKED_OUT_OBJECTS( "0300061x4" ),

    /**
     * Cannot move because target already contains object with same name messages.
     */
    CANNOT_MOVE_BECAUSE_TARGET_ALREADY_CONTAINS_OBJECT_WITH_SAME_NAME( "0300062x4" ),

    /**
     * Category id cannot empty messages.
     */
    CATEGORY_ID_CANNOT_EMPTY( "0300063x4" ),

    /**
     * Algo command is invalid messages.
     */
    ALGO_COMMAND_IS_INVALID( "0300064x4" ),

    /**
     * Nominal value is missing in values messages.
     */
    NOMINAL_VALUE_IS_MISSING_IN_VALUES( "0300065x4" ),

    /**
     * Please correct algo config of scheme messages.
     */
    PLEASE_CORRECT_ALGO_CONFIG_OF_SCHEME( "0300066x4" ),

    /**
     * Selected items and dependencies started deletion process successfully messages.
     */
    SELECTED_ITEMS_AND_DEPENDENCIES_STARTED_DELETION_PROCESS_SUCCESSFULLY( "0300067x4" ),

    /**
     * Selected items started restore process successfully messages.
     */
    SELECTED_ITEMS_STARTED_RESTORE_PROCESS_SUCCESSFULLY( "0300068x4" ),

    /**
     * Design variables undefined in workflow messages.
     */
    DESIGN_VARIABLES_UNDEFINED_IN_WORKFLOW( "0300069x4" ),

    /**
     * Design variables required field is missing messages.
     */
    DESIGN_VARIABLES_REQUIRED_FIELD_IS_MISSING( "0300070x4" ),

    /**
     * Operation not permitted on autodelete obj messages.
     */
    OPERATION_NOT_PERMITTED_ON_AUTODELETE_OBJ( "0300071x4" ),

    /**
     * Cannot move workflow project to data objects messages.
     */
    CANNOT_MOVE_WORKFLOW_PROJECT_TO_DATA_OBJECTS( "0300072x4" ),

    /**
     * Mac address config error messages.
     */
    MAC_ADDRESS_CONFIG_ERROR( "0300077x4" ),

    /**
     * Persist system container messages.
     */
    PERSIST_SYSTEM_CONTAINER( "0300022x4" ),

    /**
     * User restricted to object messages.
     */
    USER_RESTRICTED_TO_OBJECT( "0300023x4" ),

    /**
     * Object not updated messages.
     */
    OBJECT_NOT_UPDATED( "0300024x4" ),

    /**
     * Thumbnails exported successfully messages.
     */
    THUMBNAILS_EXPORTED_SUCCESSFULLY( "0300025x4" ),

    /**
     * Object is going to transfer from location to location messages.
     */
    OBJECT_IS_GOING_TO_TRANSFER_FROM_LOCATION_TO_LOCATION( "0300052x4" ),

    /**
     * Curve not found in file messages.
     */
    CURVE_NOT_FOUND_IN_FILE( "0300045x4" ),

    /**
     * Movie not found messages.
     */
    MOVIE_NOT_FOUND( "0300046x4" ),

    /**
     * Select image file messages.
     */
    SELECT_IMAGE_FILE( "0300054x4" ),

    /**
     * Select movie file messages.
     */
    SELECT_MOVIE_FILE( "0300055x4" ),

    /**
     * User not allowed hpc uge command messages.
     */
    USER_NOT_ALLOWED_HPC_UGE_COMMAND( "0300078x4" ),

    /**
     * License is expired messages.
     */
    LICENSE_IS_EXPIRED( "0300079x4" ),

    /**
     * Cannot delete because selected category is being used in schemes messages.
     */
    CANNOT_DELETE_BECAUSE_SELECTED_CATEGORY_IS_BEING_USED_IN_SCHEMES( "0300080x4" ),

    /**
     * Invalid file type for selected object messages.
     */
    INVALID_FILE_TYPE_FOR_SELECTED_OBJECT( "0300081x4" ),

    /**
     * Invalid project selection for dashboard messages.
     */
    INVALID_PROJECT_SELECTION_FOR_DASHBOARD( "0300082x4" ),

    /**
     * Plugin does not support object selection messages.
     */
    PLUGIN_DOES_NOT_SUPPORT_OBJECT_SELECTION( "0300083x4" ),

    /**
     * Variable not found refresh list or open submit form again messages.
     */
    VARIABLE_NOT_FOUND_REFRESH_LIST_OR_OPEN_SUBMIT_FORM_AGAIN( "0300084x4" ),

    /**
     * Data project does not exist messages.
     */
    DATA_PROJECT_DOES_NOT_EXIST( "0300085x4" ),

    /**
     * Error in generating project overview messages.
     */
    ERROR_IN_GENERATING_PROJECT_OVERVIEW( "0300086x4" ),

    /**
     * Dashboard plugin not found messages.
     */
    DASHBOARD_PLUGIN_NOT_FOUND( "0300089x4" ),

    /**
     * Field is not a selection field messages.
     */
    FIELD_IS_NOT_A_SELECTION_FIELD( "0300090x4" ),

    /**
     * User is not a system user messages.
     */
    USER_IS_NOT_A_SYSTEM_USER( "0300091x4" ),

    /**
     * Object x not exist with id y messages.
     */
    OBJECT_X_NOT_EXIST_WITH_ID_Y( "0300092x4" ),

    /**
     * Simuspace tree generated successfully messages.
     */
    SIMUSPACE_TREE_GENERATED_SUCCESSFULLY( "0300093x4" ),

    /**
     * Directory selected messages.
     */
    DIRECTORY_SELECTED( "0300094x4" ),

    /**
     * Created successfully messages.
     */
    CREATED_SUCCESSFULLY( "0300095x4" ),

    /**
     * Updated successfully messages.
     */
    UPDATED_SUCCESSFULLY( "0300096x4" ),

    /**
     * Widget form messages.
     */
    WIDGET_FORM( "0300097x4" ),

    /**
     * Widget type messages.
     */
    WIDGET_TYPE( "0300098x4" ),

    /**
     * New widget created successfully messages.
     */
    NEW_WIDGET_CREATED_SUCCESSFULLY( "0300099x4" ),

    /**
     * Widgets messages.
     */
    WIDGETS( "0300100x4" ),

    /**
     * Widget updated successfully messages.
     */
    WIDGET_UPDATED_SUCCESSFULLY( "0300101x4" ),

    /**
     * Widget deleted successfully messages.
     */
    WIDGET_DELETED_SUCCESSFULLY( "0300102x4" ),

    /**
     * Widget id not found messages.
     */
    WIDGET_ID_NOT_FOUND( "0300103x4" ),

    /**
     * Invalid key messages.
     */
    INVALID_KEY( "0300087x4" ),

    /**
     * Invalid value messages.
     */
    INVALID_VALUE( "0300088x4" ),

    /**
     * The Project dashboard tree generated successfully.
     */
    // VMCL Materials Messages
    PROJECT_DASHBOARD_TREE_GENERATED_SUCCESSFULLY( "0301000x4" ),

    /**
     * Project dashboard minimum depth exception messages.
     */
    PROJECT_DASHBOARD_MINIMUM_DEPTH_EXCEPTION( "0301001x4" ),

    /**
     * Project dashborad script not found messages.
     */
    PROJECT_DASHBORAD_SCRIPT_NOT_FOUND( "0301002x4" ),

    /**
     * Project icicle chart script returned error messages.
     */
    PROJECT_ICICLE_CHART_SCRIPT_RETURNED_ERROR( "0301003x4" ),

    /**
     * Project chart output file not found messages.
     */
    PROJECT_CHART_OUTPUT_FILE_NOT_FOUND( "0301004x4" ),

    /**
     * Project selection is invalid messages.
     */
    PROJECT_SELECTION_IS_INVALID( "0301005x4" ),

    /**
     * Invalid characters in cache file messages.
     */
    INVALID_CHARACTERS_IN_CACHE_FILE( "0301006x4" ),

    /**
     * Invalid project selection no data generated messages.
     */
    INVALID_PROJECT_SELECTION_NO_DATA_GENERATED( "0301007x4" ),

    /**
     * Error while executing cache generation messages.
     */
    ERROR_WHILE_EXECUTING_CACHE_GENERATION( "0301008x4" ),

    /**
     * Change status to messages.
     */
    CHANGE_STATUS_TO( "0301009x4" ),

    /**
     * Status changed successfully messages.
     */
    STATUS_CHANGED_SUCCESSFULLY( "0301010x4" ),

    /**
     * Failed to change status messages.
     */
    FAILED_TO_CHANGE_STATUS( "0301011x4" ),

    /**
     * The Pst test data returned successfully.
     */
    // PST Messages
    PST_TEST_DATA_RETURNED_SUCCESSFULLY( "0302001x4" ),

    /**
     * Error generating pst test data messages.
     */
    ERROR_GENERATING_PST_TEST_DATA( "0302002x4" ),

    /**
     * Error reading test data messages.
     */
    ERROR_READING_TEST_DATA( "0302003x4" ),

    /**
     * Pst benches data returned successfully messages.
     */
    PST_BENCHES_DATA_RETURNED_SUCCESSFULLY( "0302004x4" ),

    /**
     * Error generating pst benches data messages.
     */
    ERROR_GENERATING_PST_BENCHES_DATA( "0302005x4" ),

    /**
     * Error reading benches data messages.
     */
    ERROR_READING_BENCHES_DATA( "0302006x4" ),

    /**
     * Pre req updated successfully messages.
     */
    PRE_REQ_UPDATED_SUCCESSFULLY( "0302007x4" ),

    /**
     * Failed to update pre req messages.
     */
    FAILED_TO_UPDATE_PRE_REQ( "0302008x4" ),

    /**
     * Test schedule updated successfully messages.
     */
    TEST_SCHEDULE_UPDATED_SUCCESSFULLY( "0302009x4" ),
    /**
     * Failed to update test schedule messages.
     */
    FAILED_TO_UPDATE_TEST_SCHEDULE( "0302010x4" ),

    /**
     * Pst data updated successfully messages.
     */
    PST_DATA_UPDATED_SUCCESSFULLY( "0302011x4" ),
    /**
     * Failed to update pst data messages.
     */
    FAILED_TO_UPDATE_PST_DATA( "0302012x4" ),
    /**
     * Pst data created successfully messages.
     */
    PST_DATA_CREATED_SUCCESSFULLY( "0302013x4" ),

    /**
     * Failed to create pst data messages.
     */
    FAILED_TO_CREATE_PST_DATA( "0302014x4" ),

    /**
     * Test schedule added successfully messages.
     */
    TEST_SCHEDULE_ADDED_SUCCESSFULLY( "0302015x4" ),

    /**
     * Failed to add test schedule messages.
     */
    FAILED_TO_ADD_TEST_SCHEDULE( "0302016x4" ),

    /**
     * Ui prepared successfully messages.
     */
    UI_PREPARED_SUCCESSFULLY_FROM_JSON( "0302017x4" ),

    /**
     * Failed to prepare ui schedule messages.
     */
    FAILED_TO_PREPARE_UI_TO_ADD_TEST( "0302018x4" ),

    /**
     * Test schedule coupled successfully messages.
     */
    TEST_SCHEDULE_COUPLED_SUCCESSFULLY( "0302019x4" ),
    /**
     * Failed to coupled test schedule messages.
     */
    FAILED_TO_COUPLED_TEST_SCHEDULE( "0302020x4" ),

    /**
     * Exported ace lunge successfully messages.
     */
    EXPORTED_ACE_LUNGE_SUCCESSFULLY( "0302021x4" ),

    /**
     * Failed to prepare file for export messages.
     */
    FAILED_TO_PREPARE_FILE_FOR_EXPORT( "0302022x4" ),

    /**
     * Failed to create input file messages.
     */
    FAILED_TO_CREATE_INPUT_FILE( "0302023x4" ),

    /**
     * Archive added successfully messages.
     */
    ARCHIVE_ADDED_SUCCESSFULLY( "0302024x4" ),

    /**
     * Failed to add archive messages.
     */
    FAILED_TO_ADD_ARCHIVE( "0302025x4" ),

    /**
     * Pst test archive returned successfully messages.
     */
    PST_TEST_ARCHIVE_RETURNED_SUCCESSFULLY( "0302026x4" ),

    /**
     * Pst benches archive returned successfully messages.
     */
    PST_BENCHES_ARCHIVE_RETURNED_SUCCESSFULLY( "0302027x4" ),

    /**
     * Pst archive list returned successfully messages.
     */
    PST_ARCHIVE_LIST_RETURNED_SUCCESSFULLY( "0302028x4" ),

    /**
     * Failed to prepare ui to edit test messages.
     */
    FAILED_TO_PREPARE_UI_TO_EDIT_TEST( "0302029x4" ),

    /**
     * Color file could not be generated messages.
     */
    FAILED_TO_GENERATE_COLOR_FILE( "0302030x4" ),

    /**
     * Colors file generated successfully messages.
     */
    COLORS_FILE_GENERATED_SUCCESSFULLY( "0302031x4" ),

    /**
     * Error reading machine data messages.
     */
    ERROR_READING_MACHINE_DATA( "0302032x4" ),

    /**
     * Machine data returned successfully messages.
     */
    MACHINE_DATA_RETURNED_SUCCESSFULLY( "0302033x4" ),

    //DataDashboardMessages
    /**
     * External database connection success messages.
     */
    EXTERNAL_DATABASE_CONNECTION_SUCCESS( "0303000x4" ),

    /**
     * Failed to connect external database messages.
     */
    FAILED_TO_CONNECT_EXTERNAL_DATABASE( "0303001x4" ),

    /**
     * Error while attempting to close connection messages.
     */
    ERROR_WHILE_ATTEMPTING_TO_CLOSE_CONNECTION( "0303002x4" ),

    /**
     * Database not supported messages.
     */
    DATABASE_NOT_SUPPORTED( "0303003x4" ),

    /**
     * Data source not found messages.
     */
    DATA_SOURCE_NOT_FOUND( "0303004x4" ),

    /**
     * Can not update type data source messages.
     */
    CAN_NOT_UPDATE_TYPE_DATA_SOURCE( "0303005x4" ),

    /**
     * Unknown data source type messages.
     */
    UNKNOWN_DATA_SOURCE_TYPE( "0303006x4" ),

    /**
     * Key not found in payload messages.
     */
    KEY_NOT_FOUND_IN_PAYLOAD( "0303007x4" ),

    /**
     * Failed to get columns from database messages.
     */
    FAILED_TO_GET_COLUMNS_FROM_DATABASE( "0303008x4" ),

    /**
     * Failed to generate database preview messages.
     */
    FAILED_TO_GENERATE_DATABASE_PREVIEW( "0303009x4" ),

    /**
     * Failed to get schemas from database messages.
     */
    FAILED_TO_GET_SCHEMAS_FROM_DATABASE( "0303010x4" ),

    /**
     * Failed to get tables from database messages.
     */
    FAILED_TO_GET_TABLES_FROM_DATABASE( "0303011x4" ),

    /**
     * Missing required field messages.
     */
    MISSING_REQUIRED_FIELD( "0303012x4" ),

    /**
     * Error preparing input file messages.
     */
    ERROR_PREPARING_INPUT_FILE( "0303013x4" ),

    /**
     * Error preparing input preview messages.
     */
    ERROR_PREPARING_INPUT_PREVIEW( "0303014x4" ),

    /**
     * Python process cancelled due to new request messages.
     */
    PYTHON_PROCESS_CANCELLED_DUE_TO_NEW_REQUEST( "0303015x4" ),

    /**
     * Error occurred in python process messages.
     */
    ERROR_OCCURRED_IN_PYTHON_PROCESS( "0303016x4" ),

    /**
     * Another user is working on the widget messages.
     */
    ANOTHER_USER_IS_WORKING_ON_THE_WIDGET( "0303017x4" ),

    /**
     * Error preparing script file messages.
     */
    ERROR_PREPARING_SCRIPT_FILE( "0303018x4" ),

    /**
     * Error preparing output file messages.
     */
    ERROR_PREPARING_OUTPUT_FILE( "0303019x4" ),

    /**
     * Python executable is not readable messages.
     */
    PYTHON_EXECUTABLE_IS_NOT_READABLE( "0303020x4" ),

    /**
     * Python executable is not executable messages.
     */
    PYTHON_EXECUTABLE_IS_NOT_EXECUTABLE( "0303021x4" ),

    /**
     * Python output file not found messages.
     */
    PYTHON_OUTPUT_FILE_NOT_FOUND( "0303022x4" ),

    /**
     * Python output file is empty messages.
     */
    PYTHON_OUTPUT_FILE_IS_EMPTY( "0303023x4" ),

    /**
     * Python output file too large messages.
     */
    PYTHON_OUTPUT_FILE_TOO_LARGE( "0303024x4" ),

    /**
     * Invalid file format for file messages.
     */
    INVALID_FILE_FORMAT_FOR_FILE( "0303025x4" ),

    /**
     * Error while reading output file messages.
     */
    ERROR_WHILE_READING_OUTPUT_FILE( "0303026x4" ),

    /**
     * Incomplete widget for query messages.
     */
    INCOMPLETE_WIDGET_FOR_QUERY( "0303027x4" ),

    /**
     * Can not add group to group messages.
     */
    CAN_NOT_ADD_GROUP_TO_GROUP( "0303028x4" ),

    /**
     * Failed to get values for messages.
     */
    FAILED_TO_GET_VALUES_FOR( "0303029x4" ),

    /**
     * No order number found for provided values messages.
     */
    NO_ORDER_NUMBER_FOUND_FOR_PROVIDED_VALUES( "0303030x4" ),

    /**
     * The Custom attribute name cannot be null.
     */
    // Custom attributes messages
    CUSTOM_ATTRIBUTE_NAME_CANNOT_BE_NULL( "0400001x4" ),

    /**
     * Custom attribute type cannot be null messages.
     */
    CUSTOM_ATTRIBUTE_TYPE_CANNOT_BE_NULL( "0400002x4" ),

    /**
     * Custom attribute values cannot be null messages.
     */
    CUSTOM_ATTRIBUTE_VALUES_CANNOT_BE_NULL( "0400003x4" ),

    /**
     * Custom attribute already exists messages.
     */
    CUSTOM_ATTRIBUTE_ALREADY_EXISTS( "0400004x4" ),

    /**
     * Action was success messages.
     */
    ACTION_WAS_SUCCESS( "0300047x4" ),

    /**
     * Action was not success messages.
     */
    ACTION_WAS_NOT_SUCCESS( "0300048x4" ),

    /**
     * User restricted to system messages.
     */
    USER_RESTRICTED_TO_SYSTEM( "0300050x4" ),

    /**
     * The Provide user detail.
     */
    // User Messages
    PROVIDE_USER_DETAIL( "0500001x4" ),

    /**
     * Unauthorized user messages.
     */
    UNAUTHORIZED_USER( "0500002x4" ),

    /**
     * User already exist messages.
     */
    USER_ALREADY_EXIST( "0500003x4" ),

    /**
     * User account is inactive messages.
     */
    USER_ACCOUNT_IS_INACTIVE( "0500004x4" ),

    /**
     * Invalid login credentials messages.
     */
    INVALID_LOGIN_CREDENTIALS( "0500005x4" ),

    /**
     * To many attempts try again in n minutes messages.
     */
    TO_MANY_ATTEMPTS_TRY_AGAIN_IN_N_MINUTES( "0500006x4" ),

    /**
     * No account found for user messages.
     */
    NO_ACCOUNT_FOUND_FOR_USER( "0500007x4" ),

    /**
     * User permitted to role messages.
     */
    USER_PERMITTED_TO_ROLE( "0500008x1" ),

    /**
     * User not permitted to role messages.
     */
    USER_NOT_PERMITTED_TO_ROLE( "0500009x4" ),

    /**
     * User permitted to this resource messages.
     */
    USER_PERMITTED_TO_THIS_RESOURCE( "0500010x1" ),

    /**
     * User not permitted to this resource messages.
     */
    USER_NOT_PERMITTED_TO_THIS_RESOURCE( "0500011x4" ),

    /**
     * No permission available on current user messages.
     */
    NO_PERMISSION_AVAILABLE_ON_CURRENT_USER( "0500012x4" ),

    /**
     * Account status not supported messages.
     */
    ACCOUNT_STATUS_NOT_SUPPORTED( "0500013x1" ),

    /**
     * User logged out successfully messages.
     */
    USER_LOGGED_OUT_SUCCESSFULLY( "0500014x1" ),

    /**
     * User is already logged out messages.
     */
    USER_IS_ALREADY_LOGGED_OUT( "0500015x1" ),

    /**
     * User id cannot be null or empty messages.
     */
    USER_ID_CANNOT_BE_NULL_OR_EMPTY( "0500016x1" ),

    /**
     * Invalid base 64 string messages.
     */
    INVALID_BASE64_STRING( "0500017x1" ),

    /**
     * User name cannot be changed messages.
     */
    USER_NAME_CANNOT_BE_CHANGED( "0500018x1" ),

    /**
     * Image size less than one messages.
     */
    IMAGE_SIZE_LESS_THAN_ONE( "0500019x1" ),

    /**
     * Image not provided to resize messages.
     */
    IMAGE_NOT_PROVIDED_TO_RESIZE( "0500020x1" ),

    /**
     * Could not read image messages.
     */
    COULD_NOT_READ_IMAGE( "0500021x1" ),

    /**
     * Could not write image messages.
     */
    COULD_NOT_WRITE_IMAGE( "0500022x1" ),

    /**
     * Invalid email messages.
     */
    INVALID_EMAIL( "0500023x1" ),

    /**
     * Invalid phone number messages.
     */
    INVALID_PHONE_NUMBER( "0500024x1" ),

    /**
     * Uid cannot be changed messages.
     */
    UID_CANNOT_BE_CHANGED( "0500025x1" ),

    /**
     * Password changed successfully messages.
     */
    PASSWORD_CHANGED_SUCCESSFULLY( "0500026x1" ),

    /**
     * Old password does not matched messages.
     */
    OLD_PASSWORD_DOES_NOT_MATCHED( "0500027x1" ),

    /**
     * Password and confirm password does not matched messages.
     */
    PASSWORD_AND_CONFIRM_PASSWORD_DOES_NOT_MATCHED( "0500028x1" ),

    /**
     * User not found with id messages.
     */
    USER_NOT_FOUND_WITH_ID( "0500029x1" ),

    /**
     * Directory disabled which user belongs to messages.
     */
    DIRECTORY_DISABLED_WHICH_USER_BELONGS_TO( "0500030x1" ),

    /**
     * User deleted successfully messages.
     */
    USER_DELETED_SUCCESSFULLY( "0500031x1" ),

    /**
     * User cannot not be deleted messages.
     */
    USER_CANNOT_NOT_BE_DELETED( "0500032x1" ),

    /**
     * User profile updated successfully messages.
     */
    USER_PROFILE_UPDATED_SUCCESSFULLY( "0500033x1" ),

    /**
     * Invalid creadentials or invalid confifguration ldap messages.
     */
    INVALID_CREADENTIALS_OR_INVALID_CONFIFGURATION_LDAP( "0500034x1" ),

    /**
     * Editing not allowed on su messages.
     */
    EDITING_NOT_ALLOWED_ON_SU( "0500035x1" ),

    /**
     * User could not be authenticated in ldap messages.
     */
    USER_COULD_NOT_BE_AUTHENTICATED_IN_LDAP( "0500036x1" ),

    /**
     * Operation only allowed for current user messages.
     */
    OPERATION_ONLY_ALLOWED_FOR_CURRENT_USER( "0500037x1" ),

    /**
     * User could not be authenticated in ldap messages.
     */
    USER_NOT_FOUND_WITH_TOKEN( "0500038x1" ),

    /**
     * Update not allowed messages.
     */
    UPDATE_NOT_ALLOWED( "0500039x1" ),

    /**
     * New user form created messages.
     */
    NEW_USER_FORM_CREATED( "0500040x1" ),

    // file and directory messages
    /**
     * The File path not exist.
     */
    FILE_PATH_NOT_EXIST( "0600001x4" ),

    /**
     * Directory type is not valid messages.
     */
    DIRECTORY_TYPE_IS_NOT_VALID( "0600002x4" ),

    /**
     * Attributes should not be empty messages.
     */
    ATTRIBUTES_SHOULD_NOT_BE_EMPTY( "0600003x4" ),

    /**
     * Mapping should not be empty messages.
     */
    MAPPING_SHOULD_NOT_BE_EMPTY( "0600004x4" ),

    /**
     * Status should active or disabled messages.
     */
    STATUS_SHOULD_ACTIVE_OR_DISABLED( "0600005x4" ),

    /**
     * Request id not valid messages.
     */
    REQUEST_ID_NOT_VALID( "0600006x4" ),

    /**
     * Directory is not valid messages.
     */
    DIRECTORY_IS_NOT_VALID( "0600007x4" ),

    /**
     * User does not exist in directory messages.
     */
    USER_DOES_NOT_EXIST_IN_DIRECTORY( "0600008x4" ),

    /**
     * Directory id is null or empty messages.
     */
    DIRECTORY_ID_IS_NULL_OR_EMPTY( "0600009x4" ),

    /**
     * Directory invalid credentials messages.
     */
    DIRECTORY_INVALID_CREDENTIALS( "0600010x4" ),

    /**
     * Directory cannot be deleted messages.
     */
    DIRECTORY_CANNOT_BE_DELETED( "0600011x4" ),

    /**
     * Dirtory deleted successfully messages.
     */
    DIRTORY_DELETED_SUCCESSFULLY( "0600012x4" ),

    /**
     * Directory not exist messages.
     */
    DIRECTORY_NOT_EXIST( "0600013x4" ),

    /**
     * Directory configuration are not valid messages.
     */
    DIRECTORY_CONFIGURATION_ARE_NOT_VALID( "0600014x4" ),

    /**
     * Connection successful messages.
     */
    CONNECTION_SUCCESSFUL( "0600015x4" ),

    /**
     * Connection failed messages.
     */
    CONNECTION_FAILED( "0600016x4" ),

    /**
     * Only applicable to ldap or ad messages.
     */
    ONLY_APPLICABLE_TO_LDAP_OR_AD( "0600017x4" ),

    /**
     * Ldap system username pwd cant null messages.
     */
    LDAP_SYSTEM_USERNAME_PWD_CANT_NULL( "0600018x4" ),

    /**
     * User exist in directory messages.
     */
    USER_EXIST_IN_DIRECTORY( "0600019x4" ),

    /**
     * File removed messages.
     */
    FILE_REMOVED( "0600020x4" ),

    /**
     * Failed to create directory at messages.
     */
    FAILED_TO_CREATE_DIRECTORY_AT( "0600021x4" ),

    /**
     * Failed to copy file to from messages.
     */
    FAILED_TO_COPY_FILE_TO_FROM( "0600022x4" ),

    /**
     * Failed to create file at messages.
     */
    FAILED_TO_CREATE_FILE_AT( "0600023x4" ),

    /**
     * Document validation failed messages.
     */
    DOCUMENT_VALIDATION_FAILED( "0600024x4" ),

    /**
     * Failed to save document messages.
     */
    FAILED_TO_SAVE_DOCUMENT( "0600025x4" ),

    /**
     * Could not read file messages.
     */
    COULD_NOT_READ_FILE( "0600026x4" ),

    /**
     * Could not parse json file messages.
     */
    COULD_NOT_PARSE_JSON_FILE( "0600027x4" ),

    /**
     * Failed to copy directory to from messages.
     */
    FAILED_TO_COPY_DIRECTORY_TO_FROM( "0600028x4" ),

    /**
     * Failed to delete file directory at messages.
     */
    FAILED_TO_DELETE_FILE_DIRECTORY_AT( "0600029x4" ),

    /**
     * Failed to write file at messages.
     */
    FAILED_TO_WRITE_FILE_AT( "0600030x4" ),

    /**
     * Failed to write file from to messages.
     */
    FAILED_TO_WRITE_FILE_FROM_TO( "0600031x4" ),

    /**
     * Failed to extract zip file messages.
     */
    FAILED_TO_EXTRACT_ZIP_FILE( "0600032x4" ),

    /**
     * Failed to extract dummy zip file messages.
     */
    FAILED_TO_EXTRACT_DUMMY_ZIP_FILE( "0600033x4" ),

    /**
     * Hpc job completed messages.
     */
    HPC_JOB_COMPLETED( "0600034x4" ),

    /**
     * Femzip chart data not generated messages.
     */
    FEMZIP_CHART_DATA_NOT_GENERATED( "0600035x4" ),

    /**
     * Chart data not generated messages.
     */
    CHART_DATA_NOT_GENERATED( "0600036x4" ),

    /**
     * The Link reffered object not found.
     */
    // Project configuration schema loading messages
    LINK_REFFERED_OBJECT_NOT_FOUND( "0700001x4" ),

    /**
     * Missing attributes messages.
     */
    MISSING_ATTRIBUTES( "0700002x4" ),

    /**
     * Config file missing from master config messages.
     */
    CONFIG_FILE_MISSING_FROM_MASTER_CONFIG( "0700003x4" ),

    /**
     * Attribute value changed messages.
     */
    ATTRIBUTE_VALUE_CHANGED( "0700004x4" ),

    /**
     * Custom attribute missing messages.
     */
    CUSTOM_ATTRIBUTE_MISSING( "0700005x4" ),

    /**
     * Contains attribute missing messages.
     */
    CONTAINS_ATTRIBUTE_MISSING( "0700006x4" ),

    /**
     * Links attribute missing messages.
     */
    LINKS_ATTRIBUTE_MISSING( "0700007x4" ),

    /**
     * Configuration does not exists messages.
     */
    CONFIGURATION_DOES_NOT_EXISTS( "0700008x4" ),

    /**
     * Missing config file messages.
     */
    MISSING_CONFIG_FILE( "0700009x4" ),

    /**
     * Missing object messages.
     */
    MISSING_OBJECT( "0700010x4" ),

    /**
     * Name or class changed messages.
     */
    NAME_OR_CLASS_CHANGED( "0700011x4" ),

    /**
     * Data loss occurred messages.
     */
    DATA_LOSS_OCCURRED( "0700012x4" ),

    /**
     * Not valid java identifier messages.
     */
    NOT_VALID_JAVA_IDENTIFIER( "0700013x1" ),

    /**
     * Object id cannot be empty in config messages.
     */
    OBJECT_ID_CANNOT_BE_EMPTY_IN_CONFIG( "0700014x1" ),

    /**
     * Invalid object id provided in config messages.
     */
    INVALID_OBJECT_ID_PROVIDED_IN_CONFIG( "0700015x1" ),

    /**
     * Object name cannot be empty messages.
     */
    OBJECT_NAME_CANNOT_BE_EMPTY( "0700016x1" ),

    /**
     * Object name cannot have space messages.
     */
    OBJECT_NAME_CANNOT_HAVE_SPACE( "0700017x1" ),

    /**
     * Object classname cannot be empty messages.
     */
    OBJECT_CLASSNAME_CANNOT_BE_EMPTY( "0700018x1" ),

    /**
     * Class does not exist for object messages.
     */
    CLASS_DOES_NOT_EXIST_FOR_OBJECT( "0700019x1" ),

    /**
     * Entity class does not exist in dto messages.
     */
    ENTITY_CLASS_DOES_NOT_EXIST_IN_DTO( "0700020x1" ),

    /**
     * Dto does not have valid entity class messages.
     */
    DTO_DOES_NOT_HAVE_VALID_ENTITY_CLASS( "0700021x1" ),

    /**
     * Object type id can con be duplicated in config messages.
     */
    OBJECT_TYPE_ID_CAN_CON_BE_DUPLICATED_IN_CONFIG( "0700022x1" ),

    /**
     * Only checkout user can perform this action messages.
     */
    ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION( "0700023x1" ),

    /**
     * Contain reffered object not found messages.
     */
    CONTAIN_REFFERED_OBJECT_NOT_FOUND( "0700025x4" ),

    /**
     * View config reffered object not found messages.
     */
    VIEW_CONFIG_REFFERED_OBJECT_NOT_FOUND( "0700026x4" ),

    /**
     * Project config array is empty messages.
     */
    PROJECT_CONFIG_ARRAY_IS_EMPTY( "0700027x4" ),

    /**
     * Scheme options have not been selected messages.
     */
    SCHEME_OPTIONS_HAVE_NOT_BEEN_SELECTED( "0700028x4" ),

    /**
     * Design summary is empty messages.
     */
    DESIGN_SUMMARY_IS_EMPTY( "0700029x4" ),

    /**
     * Scheme validated successfully messages.
     */
    SCHEME_VALIDATED_SUCCESSFULLY( "0700030x4" ),

    /**
     * Syntax is correct messages.
     */
    SYNTAX_IS_CORRECT( "0700031x4" ),

    /**
     * The Db database query error.
     */
    // database message
    DB_DATABASE_QUERY_ERROR( "0800001x4" ),

    /**
     * Provided class is not a descendant of susentity messages.
     */
    PROVIDED_CLASS_IS_NOT_A_DESCENDANT_OF_SUSENTITY( "0800002x4" ),

    /**
     * The Logger not found.
     */
    // logger messages
    LOGGER_NOT_FOUND( "0900001x4" ),

    /**
     * The Permission adding failed.
     */
    // Permission messages
    PERMISSION_ADDING_FAILED( "1000001x4" ),

    /**
     * Permission added successfully messages.
     */
    PERMISSION_ADDED_SUCCESSFULLY( "1000002x4" ),

    /**
     * Resource not found messages.
     */
    RESOURCE_NOT_FOUND( "1000003x4" ),

    /**
     * Permission not found messages.
     */
    PERMISSION_NOT_FOUND( "1000004x4" ),

    /**
     * Permission applied successfully messages.
     */
    PERMISSION_APPLIED_SUCCESSFULLY( "1000005x4" ),

    /**
     * Permission not applied successfully messages.
     */
    PERMISSION_NOT_APPLIED_SUCCESSFULLY( "1000006x4" ),

    /**
     * Permission not applied on null parent messages.
     */
    PERMISSION_NOT_APPLIED_ON_NULL_PARENT( "1000007x4" ),

    /**
     * Invalid option for permission messages.
     */
    INVALID_OPTION_FOR_PERMISSION( "1000008x4" ),

    /**
     * Manage permissions messages.
     */
    MANAGE_PERMISSIONS( "1000009x4" ),
    /**
     * The Password cannot be null or empty.
     */
    // validation of password messages
    PASSWORD_CANNOT_BE_NULL_OR_EMPTY( "1100001x4" ),

    /**
     * Passwd length must be 8 char messages.
     */
    PASSWD_LENGTH_MUST_BE_8_CHAR( "1100002x4" ),

    /**
     * Require atleast one natural number messages.
     */
    REQUIRE_ATLEAST_ONE_NATURAL_NUMBER( "1100003x4" ),

    /**
     * Require atleast one lower case alphabet messages.
     */
    REQUIRE_ATLEAST_ONE_LOWER_CASE_ALPHABET( "1100004x4" ),

    /**
     * Require atleast one upper case alphabet messages.
     */
    REQUIRE_ATLEAST_ONE_UPPER_CASE_ALPHABET( "1100005x4" ),

    /**
     * No white spce allowed messages.
     */
    NO_WHITE_SPCE_ALLOWED( "1100006x4" ),

    /**
     * Require atleast one special char messages.
     */
    REQUIRE_ATLEAST_ONE_SPECIAL_CHAR( "1100007x4" ),

    /**
     * Password is blacklisted by the system messages.
     */
    PASSWORD_IS_BLACKLISTED_BY_THE_SYSTEM( "1100008x4" ),

    /**
     * The Role created.
     */
    // roles and permissions
    ROLE_CREATED( "1200001x4" ),

    /**
     * Role updated messages.
     */
    ROLE_UPDATED( "1200002x4" ),

    /**
     * Role already exist messages.
     */
    ROLE_ALREADY_EXIST( "1200003x4" ),

    /**
     * Role not provided messages.
     */
    ROLE_NOT_PROVIDED( "1200004x4" ),

    /**
     * Role deleted successfully messages.
     */
    ROLE_DELETED_SUCCESSFULLY( "1200005x4" ),

    /**
     * No role available messages.
     */
    NO_ROLE_AVAILABLE( "1200006x4" ),

    /**
     * Resource not binded with acl messages.
     */
    RESOURCE_NOT_BINDED_WITH_ACL( "1200007x4" ),

    /**
     * Resource not provided messages.
     */
    RESOURCE_NOT_PROVIDED( "1200008x4" ),

    /**
     * Parent resource is not appended with any permissions messages.
     */
    PARENT_RESOURCE_IS_NOT_APPENDED_WITH_ANY_PERMISSIONS( "1200009x4" ),

    /**
     * Resource added successfully messages.
     */
    RESOURCE_ADDED_SUCCESSFULLY( "1200010x4" ),

    /**
     * Resource failed messages.
     */
    RESOURCE_FAILED( "1200011x4" ),

    /**
     * Cyclic dependency exist messages.
     */
    CYCLIC_DEPENDENCY_EXIST( "1200012x4" ),

    /**
     * Resource not available in db messages.
     */
    RESOURCE_NOT_AVAILABLE_IN_DB( "1200013x4" ),

    /**
     * No active control list provided messages.
     */
    NO_ACTIVE_CONTROL_LIST_PROVIDED( "1200014x4" ),

    /**
     * Resource updated messages.
     */
    RESOURCE_UPDATED( "1200015x4" ),

    /**
     * Resource already exist messages.
     */
    RESOURCE_ALREADY_EXIST( "1200016x4" ),

    /**
     * Root dont have parent messages.
     */
    ROOT_DONT_HAVE_PARENT( "1200017x4" ),

    /**
     * Child must be appended with parent messages.
     */
    CHILD_MUST_BE_APPENDED_WITH_PARENT( "1200018x4" ),

    /**
     * Unauthorized mask messages.
     */
    UNAUTHORIZED_MASK( "1200019x4" ),

    /**
     * Creating root node dont need permission messages.
     */
    CREATING_ROOT_NODE_DONT_NEED_PERMISSION( "1200020x4" ),

    /**
     * No need to provide permission set with resource on creating messages.
     */
    NO_NEED_TO_PROVIDE_PERMISSION_SET_WITH_RESOURCE_ON_CREATING( "1200021x4" ),

    /**
     * No need to provide inherit flag on save messages.
     */
    NO_NEED_TO_PROVIDE_INHERIT_FLAG_ON_SAVE( "1200022x4" ),

    /**
     * Root must be always false messages.
     */
    ROOT_MUST_BE_ALWAYS_FALSE( "1200023x4" ),

    /**
     * Duplication not allowed while updating resource messages.
     */
    DUPLICATION_NOT_ALLOWED_WHILE_UPDATING_RESOURCE( "1200024x4" ),

    /**
     * Please provide correct parent resource messages.
     */
    PLEASE_PROVIDE_CORRECT_PARENT_RESOURCE( "1200025x4" ),

    /**
     * Permission already applied on acl messages.
     */
    PERMISSION_ALREADY_APPLIED_ON_ACL( "1200026x4" ),

    /**
     * Saving object as resource messages.
     */
    SAVING_OBJECT_AS_RESOURCE( "1200027x4" ),

    /**
     * The Module name cannot be null.
     */
    // licensing module
    MODULE_NAME_CANNOT_BE_NULL( "1300001x4" ),

    /**
     * Mac address cannot be null messages.
     */
    MAC_ADDRESS_CANNOT_BE_NULL( "1300002x4" ),

    /**
     * Expiry time cannot be null messages.
     */
    EXPIRY_TIME_CANNOT_BE_NULL( "1300003x4" ),

    /**
     * Expiry time cannot be parsed messages.
     */
    EXPIRY_TIME_CANNOT_BE_PARSED( "1300004x4" ),

    /**
     * License signature is not valid messages.
     */
    LICENSE_SIGNATURE_IS_NOT_VALID( "1300005x4" ),

    /**
     * Mac address is invalid messages.
     */
    MAC_ADDRESS_IS_INVALID( "1300006x4" ),

    /**
     * License not found for requested module messages.
     */
    LICENSE_NOT_FOUND_FOR_REQUESTED_MODULE( "1300007x4" ),

    /**
     * License input cannot be null messages.
     */
    LICENSE_INPUT_CANNOT_BE_NULL( "1300008x4" ),

    /**
     * User id cannot be null messages.
     */
    USER_ID_CANNOT_BE_NULL( "1300009x4" ),

    /**
     * Allowed users exceeds from available messages.
     */
    ALLOWED_USERS_EXCEEDS_FROM_AVAILABLE( "1300010x4" ),

    /**
     * Restricted users exceeds from available messages.
     */
    RESTRICTED_USERS_EXCEEDS_FROM_AVAILABLE( "1300011x4" ),

    /**
     * Unable to add empty users messages.
     */
    UNABLE_TO_ADD_EMPTY_USERS( "1300012x4" ),

    /**
     * User license successfully updated messages.
     */
    USER_LICENSE_SUCCESSFULLY_UPDATED( "1300013x4" ),

    /**
     * Module license is successfully deleted messages.
     */
    MODULE_LICENSE_IS_SUCCESSFULLY_DELETED( "1300014x4" ),

    /**
     * User does not exist messages.
     */
    USER_DOES_NOT_EXIST( "1300015x4" ),

    /**
     * Same user cannot be added twice messages.
     */
    SAME_USER_CANNOT_BE_ADDED_TWICE( "1300016x4" ),

    /**
     * Check box state should not be null messages.
     */
    CHECK_BOX_STATE_SHOULD_NOT_BE_NULL( "1300017x4" ),

    /**
     * Check box column name should not be null messages.
     */
    CHECK_BOX_COLUMN_NAME_SHOULD_NOT_BE_NULL( "1300018x4" ),

    /**
     * Provided mode is not supported messages.
     */
    PROVIDED_MODE_IS_NOT_SUPPORTED( "1300019x4" ),

    /**
     * Feature is already attached with license messages.
     */
    FEATURE_IS_ALREADY_ATTACHED_WITH_LICENSE( "1300020x4" ),

    /**
     * Feature name should not be null or empty messages.
     */
    FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY( "1300021x4" ),

    /**
     * Duplicate feature name exists license messages.
     */
    DUPLICATE_FEATURE_NAME_EXISTS_LICENSE( "1300022x4" ),

    /**
     * Feature not allowed to user messages.
     */
    FEATURE_NOT_ALLOWED_TO_USER( "1300023x4" ),

    /**
     * License is not valid for requested module messages.
     */
    LICENSE_IS_NOT_VALID_FOR_REQUESTED_MODULE( "1300024x4" ),

    /**
     * License assign to user successfully messages.
     */
    LICENSE_ASSIGN_TO_USER_SUCCESSFULLY( "1300025x4" ),

    /**
     * License unassign from user successfully messages.
     */
    LICENSE_UNASSIGN_FROM_USER_SUCCESSFULLY( "1300026x4" ),

    /**
     * Cannot verify license messages.
     */
    CANNOT_VERIFY_LICENSE( "1300027x4" ),

    /**
     * Cb 2 license is not assigned messages.
     */
    CB2_LICENSE_IS_NOT_ASSIGNED( "1300028x4" ),

    /**
     * Cb 2 api attempt failed messages.
     */
    CB2_API_ATTEMPT_FAILED( "1300029x4" ),

    /**
     * License feature mismatch messages.
     */
    LICENSE_FEATURE_MISMATCH( "1300030x4" ),

    /**
     * Remove cb 2 connector license to login messages.
     */
    REMOVE_CB2_CONNECTOR_LICENSE_TO_LOGIN( "1300031x4" ),

    /**
     * Cb 2 features not available with wen login messages.
     */
    CB2_FEATURES_NOT_AVAILABLE_WITH_WEN_LOGIN( "1300032x4" ),

    /**
     * The User invalid or empty fingerprint items.
     */
    // token messages
    USER_INVALID_OR_EMPTY_FINGERPRINT_ITEMS( "1400001x4" ),

    /**
     * The Shiro authorization cache cleared.
     */
    // shiro messages
    SHIRO_AUTHORIZATION_CACHE_CLEARED( "1500001x4" ),

    /**
     * The Search menu not found.
     */
    // START - filter messages
    SEARCH_MENU_NOT_FOUND( "1600001x4" ),

    /**
     * Filters operator not found by id messages.
     */
    FILTERS_OPERATOR_NOT_FOUND_BY_ID( "1600002x4" ),

    /**
     * Filters operator not found by name messages.
     */
    FILTERS_OPERATOR_NOT_FOUND_BY_NAME( "1600003x4" ),

    /**
     * Protocol with id not found messages.
     */
    PROTOCOL_WITH_ID_NOT_FOUND( "1600004x4" ),

    /**
     * Filters cannot be null messages.
     */
    FILTERS_CANNOT_BE_NULL( "1600005x4" ),

    /**
     * Invalid value provided for column messages.
     */
    INVALID_VALUE_PROVIDED_FOR_COLUMN( "1600006x4" ),

    /**
     * Field not found in class messages.
     */
    FIELD_NOT_FOUND_IN_CLASS( "1600007x4" ),

    /**
     * Class not found or not able to initialize messages.
     */
    CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE( "1600008x4" ),

    /**
     * Unable to prepare entity of dto messages.
     */
    UNABLE_TO_PREPARE_ENTITY_OF_DTO( "1600009x4" ),

    /**
     * Invalid search id messages.
     */
    INVALID_SEARCH_ID( "1600010x4" ),

    /**
     * Cannot parse lucene query messages.
     */
    CANNOT_PARSE_LUCENE_QUERY( "1600011x4" ),

    /**
     * Operation cannot be performed for given column messages.
     */
    OPERATION_CANNOT_BE_PERFORMED_FOR_GIVEN_COLUMN( "1600012x4" ),

    /**
     * All values for column returned successfully messages.
     */
    ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY( "1600013x4" ),

    /**
     * Invalid attribute for class messages.
     */
    INVALID_ATTRIBUTE_IN_CLASS_FOR_FORM( "1600014x4" ),

    // END - filter messages

    /**
     * The Object id cannot empty.
     */
    // meta data messages
    OBJECT_ID_CANNOT_EMPTY( "1700001x4" ),

    /**
     * Metadata object cannot empty messages.
     */
    METADATA_OBJECT_CANNOT_EMPTY( "1700002x4" ),

    /**
     * Duplicate keys found in metadata messages.
     */
    DUPLICATE_KEYS_FOUND_IN_METADATA( "1700003x4" ),

    /**
     * Vault service couldnot read messages.
     */
    VAULT_SERVICE_COULDNOT_READ( "1700004x4" ),

    /**
     * Key not found in metadata messages.
     */
    KEY_NOT_FOUND_IN_METADATA( "1700005x4" ),

    /**
     * Max entries voilated in metadata messages.
     */
    MAX_ENTRIES_VOILATED_IN_METADATA( "1700006x4" ),

    /**
     * Vault service couldnot write messages.
     */
    VAULT_SERVICE_COULDNOT_WRITE( "1700007x4" ),

    /**
     * Object type not valid messages.
     */
    OBJECT_TYPE_NOT_VALID( "1700008x4" ),

    /**
     * Metadata key length messages.
     */
    METADATA_KEY_LENGTH( "1700009x4" ),

    /**
     * Metadata value length messages.
     */
    METADATA_VALUE_LENGTH( "1700010x4" ),

    /**
     * No path in config file messages.
     */
    NO_PATH_IN_CONFIG_FILE( "1700011x4" ),

    /**
     * Invalid path in config file messages.
     */
    INVALID_PATH_IN_CONFIG_FILE( "1700012x4" ),

    /**
     * Invlid id of object or metadata messages.
     */
    INVLID_ID_OF_OBJECT_OR_METADATA( "1700013x4" ),

    /**
     * Invalid object messages.
     */
    INVALID_OBJECT( "1700014x4" ),

    /**
     * Could not write file messages.
     */
    COULD_NOT_WRITE_FILE( "1700015x4" ),

    /**
     * Object id is not valid messages.
     */
    OBJECT_ID_IS_NOT_VALID( "1700016x4" ),

    /**
     * Metadata added messages.
     */
    METADATA_ADDED( "1700017x4" ),

    /**
     * Staging service couldnot write messages.
     */
    STAGING_SERVICE_COULDNOT_WRITE( "1700018x4" ),

    /**
     * The Plugin added successfully.
     */
    // Plugin Messages
    PLUGIN_ADDED_SUCCESSFULLY( "1800001x4" ),

    /**
     * Plugin not found with id messages.
     */
    PLUGIN_NOT_FOUND_WITH_ID( "1800002x4" ),

    /**
     * Plugin enable successfully messages.
     */
    PLUGIN_ENABLE_SUCCESSFULLY( "1800003x4" ),

    /**
     * Plugin started successfully messages.
     */
    PLUGIN_STARTED_SUCCESSFULLY( "1800004x4" ),

    /**
     * Plugin failed to start messages.
     */
    PLUGIN_FAILED_TO_START( "1800005x4" ),

    /**
     * Zip file not selected messages.
     */
    ZIP_FILE_NOT_SELECTED( "1800006x4" ),

    // END - Plugin messages

    /**
     * Download success messages.
     */
    DOWNLOAD_SUCCESS( "2000005x4" ),

    /**
     * File open success messages.
     */
    FILE_OPEN_SUCCESS( "2000012x4" ),

    /**
     * Document download success messages.
     */
    DOCUMENT_DOWNLOAD_SUCCESS( "2000006x4" ),

    /**
     * Upload success messages.
     */
    UPLOAD_SUCCESS( "2000007x4" ),

    /**
     * Checkout object messages.
     */
    CHECKOUT_OBJECT( "2000008x4" ),

    /**
     * Discard object messages.
     */
    DISCARD_OBJECT( "2000009x4" ),

    /**
     * Checkin object messages.
     */
    CHECKIN_OBJECT( "2000010x4" ),

    /**
     * Abort object messages.
     */
    ABORT_OBJECT( "2000011x4" ),

    /**
     * No group with id exist messages.
     */
    NO_GROUP_WITH_ID_EXIST( "1900001x4" ),

    /**
     * No account found for group messages.
     */
    NO_ACCOUNT_FOUND_FOR_GROUP( "1900002x4" ),

    /**
     * Deamon started messages.
     */
    DEAMON_STARTED( "2000001x4" ),

    /**
     * Map not provided for acknowledge bytes messages.
     */
    MAP_NOT_PROVIDED_FOR_ACKNOWLEDGE_BYTES( "2000002x4" ),

    /**
     * Invalid file provided messages.
     */
    INVALID_FILE_PROVIDED( "2000003x4" ),

    /**
     * Map not provided for checksum messages.
     */
    MAP_NOT_PROVIDED_FOR_CHECKSUM( "2000004x4" ),

    /**
     * Invalid url provided messages.
     */
    INVALID_URL_PROVIDED( "2100001x4" ),

    /**
     * Checksum file should not be null messages.
     */
    CHECKSUM_FILE_SHOULD_NOT_BE_NULL( "2100002x4" ),

    /**
     * Os not supported messages.
     */
    OS_NOT_SUPPORTED( "2100003x4" ),

    /**
     * Invalid checksum messages.
     */
    INVALID_CHECKSUM( "2100004x4" ),

    /**
     * File uploaded successfully messages.
     */
    FILE_UPLOADED_SUCCESSFULLY( "2100005x4" ),

    /**
     * Invalid indexer messages.
     */
    INVALID_INDEXER( "2100006x4" ),

    /**
     * Dir created messages.
     */
    DIR_CREATED( "2100007x4" ),

    /**
     * Dir not created messages.
     */
    DIR_NOT_CREATED( "2100008x4" ),

    /**
     * Checksum not calculated messages.
     */
    CHECKSUM_NOT_CALCULATED( "2100009x4" ),

    /**
     * Bytes compression error messages.
     */
    BYTES_COMPRESSION_ERROR( "2100010x4" ),

    /**
     * File chunk exception messages.
     */
    FILE_CHUNK_EXCEPTION( "2100011x4" ),

    /**
     * File not closed messages.
     */
    FILE_NOT_CLOSED( "2100012x4" ),

    /**
     * File not created messages.
     */
    FILE_NOT_CREATED( "2100013x4" ),

    /**
     * Fetching property issue messages.
     */
    FETCHING_PROPERTY_ISSUE( "2100014x4" ),

    /**
     * Stream issue messages.
     */
    STREAM_ISSUE( "2100015x4" ),

    /**
     * File not removed messages.
     */
    FILE_NOT_REMOVED( "2100016x4" ),

    /**
     * Invalid option for directory messages.
     */
    INVALID_OPTION_FOR_DIRECTORY( "2100017x4" ),

    /**
     * No role with id exist messages.
     */
    NO_ROLE_WITH_ID_EXIST( "2200001x4" ),

    /**
     * No design variable with id exist messages.
     */
    NO_DESIGN_VARIABLE_WITH_ID_EXIST( "2200002x4" ),

    /**
     * No objective variable with id exist messages.
     */
    NO_OBJECTIVE_VARIABLE_WITH_ID_EXIST( "2200003x4" ),

    /**
     * Document file not found messages.
     */
    DOCUMENT_FILE_NOT_FOUND( "0080001x4" ),

    /**
     * Document not registered agent messages.
     */
    DOCUMENT_NOT_REGISTERED_AGENT( "0080002x4" ),

    /**
     * Document only zip files are allowed to upload run sim flow messages.
     */
    DOCUMENT_ONLY_ZIP_FILES_ARE_ALLOWED_TO_UPLOAD_RUN_SIM_FLOW( "0080003x4" ),

    /**
     * Document filename key cant be null messages.
     */
    DOCUMENT_FILENAME_KEY_CANT_BE_NULL( "0080004x4" ),

    /**
     * Document you have selected invalid image or type allowed are messages.
     */
    DOCUMENT_YOU_HAVE_SELECTED_INVALID_IMAGE_OR_TYPE_ALLOWED_ARE( "0080005x4" ),

    /**
     * Document the size is too large for image messages.
     */
    DOCUMENT_THE_SIZE_IS_TOO_LARGE_FOR_IMAGE( "0080006x4" ),

    /**
     * Document movie files are allowed to upload dataobject messages.
     */
    DOCUMENT_MOVIE_FILES_ARE_ALLOWED_TO_UPLOAD_DATAOBJECT( "0080007x4" ),

    /**
     * Db record not saved messages.
     */
    DB_RECORD_NOT_SAVED( "0080008x4" ),

    /**
     * Document id cannot be null messages.
     */
    DOCUMENT_ID_CANNOT_BE_NULL( "0080009x4" ),

    /**
     * No file to upload messages.
     */
    NO_FILE_TO_UPLOAD( "0080010x4" ),

    /**
     * No origin provided to upload messages.
     */
    NO_ORIGIN_PROVIDED_TO_UPLOAD( "0080011x4" ),

    /**
     * No content type provided to upload messages.
     */
    NO_CONTENT_TYPE_PROVIDED_TO_UPLOAD( "0080012x4" ),

    /**
     * No token provided messages.
     */
    NO_TOKEN_PROVIDED( "0080013x4" ),

    /**
     * Invalid or expired token messages.
     */
    INVALID_OR_EXPIRED_TOKEN( "0080014x4" ),

    /**
     * Document does not exist messages.
     */
    DOCUMENT_DOES_NOT_EXIST( "0080015x4" ),

    /**
     * Document upload failed messages.
     */
    DOCUMENT_UPLOAD_FAILED( "0080016x4" ),

    /**
     * File already exist on server messages.
     */
    FILE_ALREADY_EXIST_ON_SERVER( "0080017x4" ),

    /**
     * Document does not exist with name messages.
     */
    DOCUMENT_DOES_NOT_EXIST_WITH_NAME( "0080018x4" ),

    /**
     * No rights to read messages.
     */
    NO_RIGHTS_TO_READ( "2300001x4" ),

    /**
     * No rights to update messages.
     */
    NO_RIGHTS_TO_UPDATE( "2300002x4" ),

    /**
     * No rights to create messages.
     */
    NO_RIGHTS_TO_CREATE( "2300003x4" ),

    /**
     * No rights to delete messages.
     */
    NO_RIGHTS_TO_DELETE( "2300004x4" ),

    /**
     * No rights to write messages.
     */
    NO_RIGHTS_TO_WRITE( "2300005x4" ),

    /**
     * No rights to manage messages.
     */
    NO_RIGHTS_TO_MANAGE( "2300006x4" ),

    /**
     * No rights to execute messages.
     */
    NO_RIGHTS_TO_EXECUTE( "2300007x4" ),

    /**
     * No rights to restore messages.
     */
    NO_RIGHTS_TO_RESTORE( "2300008x4" ),

    /**
     * No rights to discard messages.
     */
    NO_RIGHTS_TO_DISCARD( "2300009x4" ),

    /**
     * Permission already inherited messages.
     */
    PERMISSION_ALREADY_INHERITED( "2300015x4" ),

    /* ***************** PROJECT ***********************. */

    /**
     * Project created successfully messages.
     */
    PROJECT_CREATED_SUCCESSFULLY( "0700024x1" ),

    /**
     * Unable to create project messages.
     */
    UNABLE_TO_CREATE_PROJECT( "2400001x4" ),

    /**
     * Unable to update project messages.
     */
    UNABLE_TO_UPDATE_PROJECT( "2400002x4" ),

    /**
     * Unable to get project messages.
     */
    UNABLE_TO_GET_PROJECT( "2400003x4" ),

    /**
     * Project type lable cant contains data messages.
     */
    PROJECT_TYPE_LABLE_CANT_CONTAINS_DATA( "2400004x4" ),

    /**
     * Unable to create object messages.
     */
    UNABLE_TO_CREATE_OBJECT( "2400005x4" ),

    /**
     * Unable to create object parent is not of type container messages.
     */
    UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_OF_TYPE_CONTAINER( "2400006x4" ),

    /**
     * Unable to create object parent is not found messages.
     */
    UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_FOUND( "2400007x4" ),

    /**
     * Unable to create object type is not fonund in config messages.
     */
    UNABLE_TO_CREATE_OBJECT_TYPE_IS_NOT_FONUND_IN_CONFIG( "2400008x4" ),

    /**
     * Container cannot contain same name messages.
     */
    CONTAINER_CANNOT_CONTAIN_SAME_NAME( "2400009x4" ),

    /**
     * Object type doesnot exists in parentz contains messages.
     */
    OBJECT_TYPE_DOESNOT_EXISTS_IN_PARENTZ_CONTAINS( "2400010x4" ),

    /**
     * Type updated successfully messages.
     */
    TYPE_UPDATED_SUCCESSFULLY( "2400011x4" ),

    /**
     * Cannot move dataobjects to workflow messages.
     */
    CANNOT_MOVE_DATAOBJECTS_TO_WORKFLOW( "2400012x4" ),

    /**
     * Algo configuration cannot contain same name messages.
     */
    ALGO_CONFIGURATION_CANNOT_CONTAIN_SAME_NAME( "2400013x4" ),

    /**
     * Ui prepared successfully messages.
     */
    UI_PREPARED_SUCCESSFULLY( "2400014x4" ),

    /**
     * Data prepared successfully messages.
     */
    DATA_PREPARED_SUCCESSFULLY( "2400015x4" ),

    /**
     * Form created messages.
     */
    FORM_CREATED( "2400016x4" ),

    /**
     * The View type not supported.
     */
    // view Messages
    VIEW_TYPE_NOT_SUPPORTED( "2500001x4" ),

    /**
     * View added successfully messages.
     */
    VIEW_ADDED_SUCCESSFULLY( "2500002x4" ),

    /**
     * View deleted successfully messages.
     */
    VIEW_DELETED_SUCCESSFULLY( "2500003x4" ),

    /**
     * View updated successfully messages.
     */
    VIEW_UPDATED_SUCCESSFULLY( "2500004x4" ),

    /**
     * View set as default successfully messages.
     */
    VIEW_SET_AS_DEFAULT_SUCCESSFULLY( "2500005x4" ),

    /**
     * View does not exist messages.
     */
    VIEW_DOES_NOT_EXIST( "2500006x4" ),

    /**
     * View already exist messages.
     */
    VIEW_ALREADY_EXIST( "2500007x4" ),

    /**
     * View name duplicate update existing messages.
     */
    VIEW_NAME_DUPLICATE_UPDATE_EXISTING( "2500008x4" ),

    /**
     * The Admin cannot perform such operation.
     */
    // admin messages
    ADMIN_CANNOT_PERFORM_SUCH_OPERATION( "2600001x4" ),

    /**
     * Object added to acl messages.
     */
    OBJECT_ADDED_TO_ACL( "2700001x4" ),

    /**
     * Object class not available messages.
     */
    OBJECT_CLASS_NOT_AVAILABLE( "2700002x4" ),

    /**
     * Parent object cant be null messages.
     */
    PARENT_OBJECT_CANT_BE_NULL( "2700003x4" ),

    /**
     * Object cant be null messages.
     */
    OBJECT_CANT_BE_NULL( "2700004x4" ),

    /**
     * Invalid security messages.
     */
    INVALID_SECURITY( "2700005x4" ),

    /**
     * Invalid resource messages.
     */
    INVALID_RESOURCE( "2700006x4" ),

    /**
     * Incorrect resource messages.
     */
    INCORRECT_RESOURCE( "2700007x4" ),

    /**
     * Object id cant be null or empty messages.
     */
    OBJECT_ID_CANT_BE_NULL_OR_EMPTY( "2800001x4" ),

    /**
     * Object type not identified messages.
     */
    OBJECT_TYPE_NOT_IDENTIFIED( "2800002x4" ),

    /**
     * Provided user not available messages.
     */
    PROVIDED_USER_NOT_AVAILABLE( "2900001x4" ),

    /**
     * Selection items not saved messages.
     */
    SELECTION_ITEMS_NOT_SAVED( "2900002x4" ),

    /**
     * Selection items not removed messages.
     */
    SELECTION_ITEMS_NOT_REMOVED( "2900004x4" ),

    /**
     * Selection not saved messages.
     */
    SELECTION_NOT_SAVED( "2900003x4" ),

    /**
     * Selection saved messages.
     */
    SELECTION_SAVED( "2900005x4" ),

    /**
     * Selection added in existing selection messages.
     */
    SELECTION_ADDED_IN_EXISTING_SELECTION( "2900006x4" ),

    /**
     * Selection removed in existing selection messages.
     */
    SELECTION_REMOVED_IN_EXISTING_SELECTION( "2900007x4" ),

    /**
     * Selected project not found messages.
     */
    SELECTED_PROJECT_NOT_FOUND( "2900008x4" ),

    /**
     * Selected object not found messages.
     */
    SELECTED_OBJECT_NOT_FOUND( "2900009x4" ),

    /**
     * Selection not availiable messages.
     */
    SELECTION_NOT_AVAILIABLE( "2900010x4" ),

    /**
     * Translation selected messages.
     */
    TRANSLATION_SELECTED( "2900011x4" ),
    /**
     * Dto ui title active directory searchbase messages.
     */
    DTO_UI_TITLE_ACTIVE_DIRECTORY_SEARCHBASE( "3000001x4" ),

    /**
     * Dto ui title added by messages.
     */
    DTO_UI_TITLE_ADDED_BY( "3000002x4" ),

    /**
     * Dto ui title allowed user limit messages.
     */
    DTO_UI_TITLE_ALLOWED_USER_LIMIT( "3000003x4" ),

    /**
     * Dto ui title ldap firstname messages.
     */
    DTO_UI_TITLE_LDAP_FIRSTNAME( "3000129x4" ),

    /**
     * Dto ui title ldap surname messages.
     */
    DTO_UI_TITLE_LDAP_SURNAME( "3000130x4" ),

    /**
     * Dto ui title change password on login messages.
     */
    DTO_UI_TITLE_CHANGE_PASSWORD_ON_LOGIN( "3000004x4" ),

    /**
     * Dto ui title configuration messages.
     */
    DTO_UI_TITLE_CONFIGURATION( "3000005x4" ),

    /**
     * Dto ui title confirm new password messages.
     */
    DTO_UI_TITLE_CONFIRM_NEW_PASSWORD( "3000006x4" ),

    /**
     * Dto ui title contacts messages.
     */
    DTO_UI_TITLE_CONTACTS( "3000007x4" ),

    /**
     * Dto ui title created on messages.
     */
    DTO_UI_TITLE_CREATED_ON( "3000008x4" ),

    /**
     * Dto ui title customer messages.
     */
    DTO_UI_TITLE_CUSTOMER( "3000009x4" ),

    /**
     * Dto ui title department messages.
     */
    DTO_UI_TITLE_DEPARTMENT( "3000010x4" ),

    /**
     * Dto ui title description messages.
     */
    DTO_UI_TITLE_DESCRIPTION( "3000011x4" ),

    /**
     * Dto ui title designation messages.
     */
    DTO_UI_TITLE_DESIGNATION( "3000012x4" ),

    /**
     * Dto ui title details messages.
     */
    DTO_UI_TITLE_DETAILS( "3000013x4" ),

    /**
     * Dto ui title direcory name messages.
     */
    DTO_UI_TITLE_DIRECORY_NAME( "3000014x4" ),

    /**
     * Dto ui title email messages.
     */
    DTO_UI_TITLE_EMAIL( "3000015x4" ),

    /**
     * Dto ui title expiry time messages.
     */
    DTO_UI_TITLE_EXPIRY_TIME( "3000016x4" ),

    /**
     * Dto ui title features messages.
     */
    DTO_UI_TITLE_FEATURES( "3000017x4" ),

    /**
     * Dto ui title first name messages.
     */
    DTO_UI_TITLE_FIRST_NAME( "3000018x4" ),

    /**
     * Dto ui title group set messages.
     */
    DTO_UI_TITLE_GROUP_SET( "3000019x4" ),

    /**
     * Dto ui title groups messages.
     */
    DTO_UI_TITLE_GROUPS( "3000020x4" ),

    /**
     * Dto ui title id messages.
     */
    DTO_UI_TITLE_ID( "3000021x4" ),

    /**
     * Dto ui title ldap or active directory system password messages.
     */
    DTO_UI_TITLE_LDAP_OR_ACTIVE_DIRECTORY_SYSTEM_PASSWORD( "3000022x4" ),

    /**
     * Dto ui title ldap or active directory system username messages.
     */
    DTO_UI_TITLE_LDAP_OR_ACTIVE_DIRECTORY_SYSTEM_USERNAME( "3000023x4" ),

    /**
     * Dto ui title ldap or active directory url messages.
     */
    DTO_UI_TITLE_LDAP_OR_ACTIVE_DIRECTORY_URL( "3000024x4" ),

    /**
     * Dto ui title ldap user dn template messages.
     */
    DTO_UI_TITLE_LDAP_USER_DN_TEMPLATE( "3000025x4" ),

    /**
     * Dto ui title license json messages.
     */
    DTO_UI_TITLE_LICENSE_JSON( "3000026x4" ),

    /**
     * Dto ui title license type messages.
     */
    DTO_UI_TITLE_LICENSE_TYPE( "3000027x4" ),

    /**
     * Dto ui title mac address messages.
     */
    DTO_UI_TITLE_MAC_ADDRESS( "3000028x4" ),

    /**
     * Dto ui title modified on messages.
     */
    DTO_UI_TITLE_MODIFIED_ON( "3000029x4" ),

    /**
     * Dto ui title module messages.
     */
    DTO_UI_TITLE_MODULE( "3000030x4" ),

    /**
     * Dto ui title module name messages.
     */
    DTO_UI_TITLE_MODULE_NAME( "3000031x4" ),

    /**
     * Dto ui title name messages.
     */
    DTO_UI_TITLE_NAME( "3000032x4" ),

    /**
     * Dto ui title new password messages.
     */
    DTO_UI_TITLE_NEW_PASSWORD( "3000033x4" ),

    /**
     * Dto ui title object id messages.
     */
    DTO_UI_TITLE_OBJECT_ID( "3000034x4" ),

    /**
     * Dto ui title object name messages.
     */
    DTO_UI_TITLE_OBJECT_NAME( "3000035x4" ),

    /**
     * Dto ui title object type messages.
     */
    DTO_UI_TITLE_OBJECT_TYPE( "3000036x4" ),

    /**
     * Dto ui title object type id messages.
     */
    DTO_UI_TITLE_OBJECT_TYPE_ID( "3000037x4" ),

    /**
     * Dto ui title object version id messages.
     */
    DTO_UI_TITLE_OBJECT_VERSION_ID( "3000038x4" ),

    /**
     * Dto ui title old password messages.
     */
    DTO_UI_TITLE_OLD_PASSWORD( "3000039x4" ),

    /**
     * Dto ui title operation type messages.
     */
    DTO_UI_TITLE_OPERATION_TYPE( "3000040x4" ),

    /**
     * Dto ui title parent id messages.
     */
    DTO_UI_TITLE_PARENT_ID( "3000041x4" ),

    /**
     * Dto ui title password messages.
     */
    DTO_UI_TITLE_PASSWORD( "3000042x4" ),

    /**
     * Dto ui title permission messages.
     */
    DTO_UI_TITLE_PERMISSION( "3000043x4" ),

    /**
     * Dto ui title project type messages.
     */
    DTO_UI_TITLE_PROJECT_TYPE( "3000044x4" ),

    /**
     * Dto ui title reseller messages.
     */
    DTO_UI_TITLE_RESELLER( "3000045x4" ),

    /**
     * Dto ui title restricted messages.
     */
    DTO_UI_TITLE_RESTRICTED( "3000046x4" ),

    /**
     * Dto ui title restricted user limit messages.
     */
    DTO_UI_TITLE_RESTRICTED_USER_LIMIT( "3000047x4" ),

    /**
     * Dto ui title role messages.
     */
    DTO_UI_TITLE_ROLE( "3000048x4" ),

    /**
     * Dto ui title status messages.
     */
    DTO_UI_TITLE_STATUS( "3000049x4" ),

    /**
     * Dto ui title surname messages.
     */
    DTO_UI_TITLE_SURNAME( "3000050x4" ),

    /**
     * Dto ui title type messages.
     */
    DTO_UI_TITLE_TYPE( "3000051x4" ),

    /**
     * Dto ui title uid messages.
     */
    DTO_UI_TITLE_UID( "3000052x4" ),

    /**
     * Dto ui title updated on messages.
     */
    DTO_UI_TITLE_UPDATED_ON( "3000053x4" ),

    /**
     * Dto ui title user messages.
     */
    DTO_UI_TITLE_USER( "3000054x4" ),

    /**
     * Dto ui title users messages.
     */
    DTO_UI_TITLE_USERS( "3000055x4" ),

    /**
     * Dto ui title vendor messages.
     */
    DTO_UI_TITLE_VENDOR( "3000056x4" ),

    /**
     * Dto ui title version messages.
     */
    DTO_UI_TITLE_VERSION( "3000057x4" ),

    /**
     * Dto ui title workflow id messages.
     */
    DTO_UI_TITLE_WORKFLOW_ID( "3000058x4" ),

    /**
     * Dto ui title workflow name messages.
     */
    DTO_UI_TITLE_WORKFLOW_NAME( "3000059x4" ),

    /**
     * Dto ui title resource name messages.
     */
    DTO_UI_TITLE_RESOURCE_NAME( "3000060x4" ),

    /**
     * Dto ui title profile image messages.
     */
    DTO_UI_TITLE_PROFILE_IMAGE( "3000061x4" ),

    /**
     * Dto ui title meta data key messages.
     */
    DTO_UI_TITLE_META_DATA_KEY( "3000062x4" ),

    /**
     * Dto ui title meta data value messages.
     */
    DTO_UI_TITLE_META_DATA_VALUE( "3000063x4" ),

    /**
     * Dto ui title created by messages.
     */
    DTO_UI_TITLE_CREATED_BY( "3000064x4" ),

    /**
     * Dto ui title modified by messages.
     */
    DTO_UI_TITLE_MODIFIED_BY( "3000065x4" ),

    /**
     * Dto ui life cycle name messages.
     */
    DTO_UI_LIFE_CYCLE_NAME( "3000066x4" ),

    /**
     * Dto ui title number of versions messages.
     */
    DTO_UI_TITLE_NUMBER_OF_VERSIONS( "3000067x4" ),

    /**
     * Dto ui title number of approved versions messages.
     */
    DTO_UI_TITLE_NUMBER_OF_APPROVED_VERSIONS( "3000068x4" ),

    /**
     * Dto ui title deleted on messages.
     */
    DTO_UI_TITLE_DELETED_ON( "3000069x4" ),

    /**
     * Dto ui title sync status messages.
     */
    DTO_UI_TITLE_SYNC_STATUS( "3000071x4" ),

    /**
     * Dto ui title progress messages.
     */
    DTO_UI_TITLE_PROGRESS( "3000072x4" ),

    /**
     * Dto ui title action messages.
     */
    DTO_UI_TITLE_ACTION( "3000073x4" ),

    /**
     * Dto ui title type id messages.
     */
    DTO_UI_TITLE_TYPE_ID( "3000074x4" ),

    /**
     * Dto ui title license version messages.
     */
    DTO_UI_TITLE_LICENSE_VERSION( "3000075x4" ),

    /**
     * Dto ui title build fe simuspace messages.
     */
    DTO_UI_TITLE_BUILD_FE_SIMUSPACE( "3000076x4" ),

    /**
     * Dto ui title build be simuspace messages.
     */
    DTO_UI_TITLE_BUILD_BE_SIMUSPACE( "3000077x4" ),

    /**
     * Dto ui title support tital id messages.
     */
    DTO_UI_TITLE_SUPPORT_TITAL_ID( "3000078x4" ),

    /**
     * Dto ui title support url messages.
     */
    DTO_UI_TITLE_SUPPORT_URL( "3000079x4" ),

    /**
     * Dto ui title time out messages.
     */
    DTO_UI_TITLE_TIME_OUT( "3000080x4" ),

    /**
     * Dto ui title internal messages.
     */
    DTO_UI_TITLE_INTERNAL( "3000081x4" ),

    /**
     * Dto ui title data object messages.
     */
    DTO_UI_TITLE_DATA_OBJECT( "3000085x4" ),

    /**
     * Dto ui title log summary messages.
     */
    DTO_UI_TITLE_LOG_SUMMARY( "3000103x4" ),

    /**
     * Dto ui title log general messages.
     */
    DTO_UI_TITLE_LOG_GENERAL( "3000104x4" ),

    /**
     * Dto ui title consumed allowed user limit messages.
     */
    DTO_UI_TITLE_CONSUMED_ALLOWED_USER_LIMIT( "3000110x4" ),

    /**
     * Dto ui title consumed restricted user limit messages.
     */
    DTO_UI_TITLE_CONSUMED_RESTRICTED_USER_LIMIT( "3000111x4" ),

    /**
     * Dto ui title available allowed user limit messages.
     */
    DTO_UI_TITLE_AVAILABLE_ALLOWED_USER_LIMIT( "3000112x4" ),

    /**
     * Dto ui title available restricted user limit messages.
     */
    DTO_UI_TITLE_AVAILABLE_RESTRICTED_USER_LIMIT( "3000113x4" ),

    /**
     * Dto ui title vault messages.
     */
    DTO_UI_TITLE_VAULT( "3000115x4" ),

    /**
     * Dto ui title staging messages.
     */
    DTO_UI_TITLE_STAGING( "3000116x4" ),

    /**
     * Dto ui title hpc messages.
     */
    DTO_UI_TITLE_HPC( "3000117x4" ),

    /**
     * Dto ui title auth token messages.
     */
    DTO_UI_TITLE_AUTH_TOKEN( "3000118x4" ),

    /**
     * Dto ui title priority messages.
     */
    DTO_UI_TITLE_PRIORITY( "3000119x4" ),

    /**
     * Dto ui title url messages.
     */
    DTO_UI_TITLE_URL( "3000120x4" ),

    /**
     * Dto ui title link messages.
     */
    DTO_UI_TITLE_LINK( "3000121x4" ),

    /**
     * Dto ui title location messages.
     */
    DTO_UI_TITLE_LOCATION( "3000122x4" ),

    /**
     * Dto ui title dataobject location messages.
     */
    DTO_UI_TITLE_DATAOBJECT_LOCATION( "3000125x4" ),

    /**
     * Dto ui title file size messages.
     */
    DTO_UI_TITLE_FILE_SIZE( "3000123x4" ),

    /**
     * The Dto ui min value error message.
     */
    // DTO UI Messages
    DTO_UI_MIN_VALUE_ERROR_MESSAGE( "3100001x4" ),

    /**
     * Dto ui mix value error message messages.
     */
    DTO_UI_MIX_VALUE_ERROR_MESSAGE( "3100002x4" ),

    /**
     * Dto ui not null error message messages.
     */
    DTO_UI_NOT_NULL_ERROR_MESSAGE( "3100003x4" ),

    /**
     * Dto ui scheme minimum value tooltip message messages.
     */
    DTO_UI_SCHEME_MINIMUM_VALUE_TOOLTIP_MESSAGE( "3100004x4" ),

    /**
     * Dto ui scheme max value tooltip message messages.
     */
    DTO_UI_SCHEME_MAX_VALUE_TOOLTIP_MESSAGE( "3100005x4" ),

    /**
     * Dto ui scheme step size tooltip message messages.
     */
    DTO_UI_SCHEME_STEP_SIZE_TOOLTIP_MESSAGE( "3100006x4" ),

    /**
     * Dto ui scheme values tooltip message messages.
     */
    DTO_UI_SCHEME_VALUES_TOOLTIP_MESSAGE( "3100007x4" ),

    /**
     * Dto ui scheme optional tooltip message messages.
     */
    DTO_UI_SCHEME_OPTIONAL_TOOLTIP_MESSAGE( "3100008x4" ),

    /**
     * Dto ui scheme numeric values tooltip message messages.
     */
    DTO_UI_SCHEME_NUMERIC_VALUES_TOOLTIP_MESSAGE( "3100010x4" ),

    /**
     * Dto ui scheme levels tooltip message messages.
     */
    DTO_UI_SCHEME_LEVELS_TOOLTIP_MESSAGE( "3100010x4" ),

    /**
     * Project messages.
     */
    PROJECT( "3000200x4" ),

    /**
     * Dto ui pending messages title messages.
     */
    DTO_UI_PENDING_MESSAGES_TITLE( "3000209x4" ),

    /**
     * Dto ui language title messages.
     */
    DTO_UI_LANGUAGE_TITLE( "3000210x4" ),

    /**
     * Dto ui theme title messages.
     */
    DTO_UI_THEME_TITLE( "3000211x4" ),

    /**
     * Dto ui select option title messages.
     */
    DTO_UI_SELECT_OPTION_TITLE( "3000212x4" ),

    /**
     * Dto ui select translation title messages.
     */
    DTO_UI_SELECT_TRANSLATION_TITLE( "3000213x4" ),

    /**
     * Dto ui full path title messages.
     */
    DTO_UI_FULL_PATH_TITLE( "3000214x4" ),

    /**
     * Dto ui working directory title messages.
     */
    DTO_UI_WORKING_DIRECTORY_TITLE( "3000215x4" ),

    /**
     * Dto ui application version title messages.
     */
    DTO_UI_APPLICATION_VERSION_TITLE( "3000216x4" ),

    /**
     * Dto ui job start time title messages.
     */
    DTO_UI_JOB_START_TIME_TITLE( "3000217x4" ),

    /**
     * Dto ui calculation start time title messages.
     */
    DTO_UI_CALCULATION_START_TIME_TITLE( "3000218x4" ),

    /**
     * Dto ui result directory title messages.
     */
    DTO_UI_RESULT_DIRECTORY_TITLE( "3000219x4" ),

    /**
     * Dto ui fs directory title messages.
     */
    DTO_UI_FS_DIRECTORY_TITLE( "3000220x4" ),

    /**
     * Dto ui workflow home title messages.
     */
    DTO_UI_WORKFLOW_HOME_TITLE( "3000221x4" ),

    /**
     * Dto ui workflow site title messages.
     */
    DTO_UI_WORKFLOW_SITE_TITLE( "3000222x4" ),

    /**
     * Dto ui host title messages.
     */
    DTO_UI_HOST_TITLE( "3000223x4" ),

    /**
     * Dto ui host name title messages.
     */
    DTO_UI_HOST_NAME_TITLE( "3000224x4" ),

    /**
     * Dto ui disk space title messages.
     */
    DTO_UI_DISK_SPACE_TITLE( "3000225x4" ),

    /**
     * Dto ui parallel environment title messages.
     */
    DTO_UI_PARALLEL_ENVIRONMENT_TITLE( "3000226x4" ),

    /**
     * Dto ui nodes title messages.
     */
    DTO_UI_NODES_TITLE( "3000227x4" ),

    /**
     * Dto ui workflow title messages.
     */
    DTO_UI_WORKFLOW_TITLE( "3000228x4" ),

    /**
     * Table messages.
     */
    TABLE( "3000247x4" ),

    /**
     * Version id messages.
     */
    VERSION_ID( "3000261x4" ),

    /**
     * Total training algos messages.
     */
    TOTAL_TRAINING_ALGOS( "3000335x4" ),

    /**
     * Total wfschemes messages.
     */
    TOTAL_WFSCHEMES( "3000336x4" ),

    /**
     * Categories messages.
     */
    CATEGORIES( "3000337x4" ),

    /**
     * Selection of id messages.
     */
    SELECTION_OF_ID( "3000338x4" ),

    /**
     * License module messages.
     */
    LICENSE_MODULE( "3000339x4" ),

    /**
     * Create license messages.
     */
    CREATE_LICENSE( "3000340x4" ),

    /**
     * Edit license messages.
     */
    EDIT_LICENSE( "3000341x4" ),

    /**
     * Manage license messages.
     */
    MANAGE_LICENSE( "3000342x4" ),

    /**
     * Active users messages.
     */
    ACTIVE_USERS( "3000343x4" ),

    /**
     * Locations with selectionid messages.
     */
    LOCATIONS_WITH_SELECTIONID( "3000344x4" ),

    /**
     * Edit location messages.
     */
    EDIT_LOCATION( "3000345x4" ),

    /**
     * Ssfs locations messages.
     */
    SSFS_LOCATIONS( "3000346x4" ),

    /**
     * Ssfs location url messages.
     */
    SSFS_LOCATION_URL( "3000347x4" ),

    /**
     * Ssfs files list messages.
     */
    SSFS_FILES_LIST( "3000348x4" ),

    /**
     * Items messages.
     */
    ITEMS( "3000349x4" ),

    /**
     * Single data object properties messages.
     */
    SINGLE_DATA_OBJECT_PROPERTIES( "3000350x4" ),

    /**
     * Edit data project messages.
     */
    EDIT_DATA_PROJECT( "3000351x4" ),

    /**
     * All child objects messages.
     */
    ALL_CHILD_OBJECTS( "3000352x4" ),

    /**
     * Object tabs messages.
     */
    OBJECT_TABS( "3000353x4" ),

    /**
     * Object tree children messages.
     */
    OBJECT_TREE_CHILDREN( "3000354x4" ),

    /**
     * Tabs view job messages.
     */
    TABS_VIEW_JOB( "3000355x4" ),

    /**
     * Training algo messages.
     */
    TRAINING_ALGO( "3000356x4" ),

    /**
     * Wf scheme messages.
     */
    WF_SCHEME( "3000357x4" ),

    /**
     * Subtabs messages.
     */
    SUBTABS( "3000358x4" ),

    /**
     * Directory messages.
     */
    DIRECTORY( "3000359x4" ),

    /**
     * Users from group id messages.
     */
    USERS_FROM_GROUP_ID( "3000360x4" ),

    /**
     * Single messages.
     */
    SINGLE( "3000361x4" ),

    /**
     * Groups from user id messages.
     */
    GROUPS_FROM_USER_ID( "3000362x4" ),

    /**
     * Total directories messages.
     */
    TOTAL_DIRECTORIES( "3000380x4" ),

    /**
     * Total locations messages.
     */
    TOTAL_LOCATIONS( "3000381x4" ),

    /**
     * Total users messages.
     */
    TOTAL_USERS( "3000382x4" ),

    /**
     * Total audit logs messages.
     */
    TOTAL_AUDIT_LOGS( "3000383x4" ),

    /**
     * Total license module messages.
     */
    TOTAL_LICENSE_MODULES( "3000384x4" ),

    /**
     * Current active license messages.
     */
    CURRENT_ACTIVE_LICENSE( "3000385x4" ),

    /**
     * License modules messages.
     */
    LICENSE_MODULES( "3000386x4" ),

    /**
     * Groups list messages.
     */
    GROUPS_LIST( "3000387x4" ),

    /**
     * Roles list messages.
     */
    ROLES_LIST( "3000388x4" ),

    /**
     * Rights messages.
     */
    RIGHTS( "3000389x4" ),

    /**
     * No modules available messages.
     */
    NO_MODULES_AVAILABLE( "3000390x4" ),

    // JOB LOGS

    /**
     * Going to delete object and dependencies messages.
     */
    GOING_TO_DELETE_OBJECT_AND_DEPENDENCIES( "3200001x4" ),

    /**
     * Delete object with name messages.
     */
    DELETE_OBJECT_WITH_NAME( "3200002x4" ),

    /**
     * Restoring object id messages.
     */
    RESTORING_OBJECT_ID( "3200003x4" ),

    /**
     * Object restored messages.
     */
    OBJECT_RESTORED( "3200004x4" ),
    /**
     * Going to restore total deleted objects messages.
     */
    GOING_TO_RESTORE_TOTAL_DELETED_OBJECTS( "3200005x4" ),

    /**
     * Going to convert data object image to thumbnail messages.
     */
    GOING_TO_CONVERT_DATA_OBJECT_IMAGE_TO_THUMBNAIL( "3200006x4" ),

    /**
     * Job stopped messages.
     */
    JOB_STOPPED( "3200007x4" ),

    /**
     * Going to create data object movie to images messages.
     */
    GOING_TO_CREATE_DATA_OBJECT_MOVIE_TO_IMAGES( "3200008x4" ),

    /**
     * Job stopped by messages.
     */
    JOB_STOPPED_BY( "3200009x4" ),

    /**
     * Going to make master job messages.
     */
    GOING_TO_MAKE_MASTER_JOB( "3200010x4" ),

    /**
     * Object id failed to transfer messages.
     */
    OBJECT_ID_FAILED_TO_TRANSFER( "3200011x4" ),

    /**
     * Job not submitted param not selected messages.
     */
    JOB_NOT_SUBMITTED_PARAM_NOT_SELECTED( "3200012x4" ),

    /**
     * Job location inactive messages.
     */
    JOB_LOCATION_INACTIVE( "3200013x4" ),

    /**
     * Could not kill remote job process messages.
     */
    COULD_NOT_KILL_REMOTE_JOB_PROCESS( "3200014x4" ),

    /**
     * Error in preparing job entities messages.
     */
    ERROR_IN_PREPARING_JOB_ENTITIES( "3200015x4" ),

    /**
     * Could not determine jobs running location messages.
     */
    COULD_NOT_DETERMINE_JOBS_RUNNING_LOCATION( "3200016x4" ),

    /**
     * Could not prepare job token messages.
     */
    COULD_NOT_PREPARE_JOB_TOKEN( "3200017x4" ),

    /**
     * Could not submit job messages.
     */
    COULD_NOT_SUBMIT_JOB( "3200018x4" ),

    /**
     * Can not pause resume type job messages.
     */
    CAN_NOT_PAUSE_RESUME_TYPE_JOB( "3200019x4" ),

    /**
     * Going to run ls dyna job messages.
     */
    GOING_TO_RUN_LS_DYNA_JOB( "3200020x4" ),

    /**
     * Error in scheme plot generation messages.
     */
    ERROR_IN_SCHEME_PLOT_GENERATION( "3200021x4" ),

    /**
     * Can not plot for incomplete jobs messages.
     */
    CAN_NOT_PLOT_FOR_INCOMPLETE_JOBS( "3200022x4" ),

    /**
     * No child jobs found messages.
     */
    NO_CHILD_JOBS_FOUND( "3200023x4" ),

    /**
     * At least 4 experiments required for generating curve messages.
     */
    AT_LEAST_4_EXPERIMENTS_REQUIRED_FOR_GENERATING_CURVE( "3200024x4" ),

    /**
     * At least 2 experiments required for stack plot messages.
     */
    AT_LEAST_2_EXPERIMENTS_REQUIRED_FOR_STACK_PLOT( "3200025x4" ),

    /**
     * At least 2 experiments required for correlation messages.
     */
    AT_LEAST_2_EXPERIMENTS_REQUIRED_FOR_CORRELATION( "3200026x4" ),

    /**
     * The Life cycle name cannot be empty.
     */
    // Life Cycle Messages ***/
    LIFE_CYCLE_NAME_CANNOT_BE_EMPTY( "3300001x4" ),

    /**
     * Life cycle name already present messages.
     */
    LIFE_CYCLE_NAME_ALREADY_PRESENT( "3300002x4" ),

    /**
     * Life cycle status id cannot be null empty messages.
     */
    LIFE_CYCLE_STATUS_ID_CANNOT_BE_NULL_EMPTY( "3300003x4" ),

    /**
     * Life cycle status name cannot be null empty messages.
     */
    LIFE_CYCLE_STATUS_NAME_CANNOT_BE_NULL_EMPTY( "3300004x4" ),

    /**
     * Life cycle status name be unique messages.
     */
    LIFE_CYCLE_STATUS_NAME_BE_UNIQUE( "3300005x4" ),

    /**
     * Life cycle status id be unique messages.
     */
    LIFE_CYCLE_STATUS_ID_BE_UNIQUE( "3300006x4" ),

    /**
     * Life cycle status move to cannot be null messages.
     */
    LIFE_CYCLE_STATUS_MOVE_TO_CANNOT_BE_NULL( "3300007x4" ),

    /**
     * Status not valid for object messages.
     */
    STATUS_NOT_VALID_FOR_OBJECT( "3300008x4" ),

    /**
     * Status is being used in sus entity messages.
     */
    STATUS_IS_BEING_USED_IN_SUS_ENTITY( "3300009x4" ),

    /**
     * Life cycle id cannot be null empty messages.
     */
    LIFE_CYCLE_ID_CANNOT_BE_NULL_EMPTY( "3300010x4" ),

    /**
     * Not a valid visibility messages.
     */
    NOT_A_VALID_VISIBILITY( "3300011x4" ),

    /**
     * Entity cannot move to status messages.
     */
    ENTITY_CANNOT_MOVE_TO_STATUS( "3300012x4" ),

    /**
     * Life cycle status configuration cannot be null empty messages.
     */
    LIFE_CYCLE_STATUS_CONFIGURATION_CANNOT_BE_NULL_EMPTY( "3300013x4" ),

    /**
     * Life cycle applicable key cannot be null empty messages.
     */
    LIFE_CYCLE_APPLICABLE_KEY_CANNOT_BE_NULL_EMPTY( "3300014x4" ),

    /**
     * Webservice properties not registered messages.
     */
    WEBSERVICE_PROPERTIES_NOT_REGISTERED( "3400001x4" ),

    /**
     * Webservice property not found messages.
     */
    WEBSERVICE_PROPERTY_NOT_FOUND( "3400002x4" ),

    /**
     * Method invalid argument messages.
     */
    METHOD_INVALID_ARGUMENT( "3400003x4" ),

    /**
     * Executor not configured in conf messages.
     */
    EXECUTOR_NOT_CONFIGURED_IN_CONF( "3400004x4" ),

    /**
     * Task rejected by executor messages.
     */
    TASK_REJECTED_BY_EXECUTOR( "3400005x4" ),

    // WFE messages

    /**
     * Field should have only one valid variable messages.
     */
    FIELD_SHOULD_HAVE_ONLY_ONE_VALID_VARIABLE( "3500001x4" ),

    /**
     * Field contains invalid email address messages.
     */
    FIELD_CONTAINS_INVALID_EMAIL_ADDRESS( "3500002x4" ),

    /**
     * Thread interrupted warning messages.
     */
    THREAD_INTERRUPTED_WARNING( "3500003x4" ),

    /**
     * Invalid context menu for wfp messages.
     */
    INVALID_CONTEXT_MENU_FOR_WFP( "3600001x4" ),

    /**
     * Invalid context menu for p messages.
     */
    INVALID_CONTEXT_MENU_FOR_P( "3600002x4" ),

    /**
     * Context menu fetched messages.
     */
    CONTEXT_MENU_FETCHED( "3600003x4" ),
    /**
     * Target item not availabe messages.
     */
    TARGET_ITEM_NOT_AVAILABE( "3700001x4" ),

    /**
     * Target linking objects not availabe messages.
     */
    TARGET_LINKING_OBJECTS_NOT_AVAILABE( "3700002x4" ),

    /**
     * Source item not availabe messages.
     */
    SOURCE_ITEM_NOT_AVAILABE( "3700003x4" ),

    /**
     * Source linking objects not availabe messages.
     */
    SOURCE_LINKING_OBJECTS_NOT_AVAILABE( "3700004x4" ),

    /**
     * Reverse link is not applicable messages.
     */
    REVERSE_LINK_IS_NOT_APPLICABLE( "3700005x4" ),

    /**
     * Link to itself not allowed messages.
     */
    LINK_TO_ITSELF_NOT_ALLOWED( "3700006x4" ),

    /**
     * Source must contain target messages.
     */
    SOURCE_MUST_CONTAIN_TARGET( "3700007x4" ),

    /**
     * Link already exist messages.
     */
    LINK_ALREADY_EXIST( "3700008x4" ),

    /**
     * Only container is allowed messages.
     */
    ONLY_CONTAINER_IS_ALLOWED( "3700009x4" ),

    /**
     * Link not available messages.
     */
    LINK_NOT_AVAILABLE( "3700010x4" ),

    /**
     * Link deleted messages.
     */
    LINK_DELETED( "3700011x4" ),

    /**
     * Link not deleted messages.
     */
    LINK_NOT_DELETED( "3700012x4" ),

    /**
     * Link not possible with same name messages.
     */
    LINK_NOT_POSSIBLE_WITH_SAME_NAME( "3700013x4" ),

    /**
     * Section created messages.
     */
    SECTION_CREATED( "3800001x4" ),

    /**
     * Section not created messages.
     */
    SECTION_NOT_CREATED( "3800002x4" ),

    /**
     * Section detail messages.
     */
    SECTION_DETAIL( "3800003x4" ),

    /**
     * Report not found messages.
     */
    REPORT_NOT_FOUND( "3800004x4" ),

    /**
     * Section not available messages.
     */
    SECTION_NOT_AVAILABLE( "3800005x4" ),

    /**
     * Section issue messages.
     */
    SECTION_ISSUE( "3800006x4" ),

    /**
     * Section mismatched messages.
     */
    SECTION_MISMATCHED( "3800007x4" ),

    // ----------------Location Messages -------------------

    /**
     * Target location is in active messages.
     */
    TARGET_LOCATION_IS_IN_ACTIVE( "3900001x4" ),

    /**
     * Target location is not accessible messages.
     */
    TARGET_LOCATION_IS_NOT_ACCESSIBLE( "3900002x4" ),

    /**
     * Target location is for execution only messages.
     */
    TARGET_LOCATION_IS_FOR_EXECUTION_ONLY( "3900003x4" ),

    /**
     * Internal api log message messages.
     */
    INTERNAL_API_LOG_MESSAGE( "3900004x4" ),

    /**
     * Create project messages.
     */
    CREATE_PROJECT( "4100001x4" ),

    /**
     * Edit project messages.
     */
    EDIT_PROJECT( "4100002x4" ),

    /**
     * View objects messages.
     */
    VIEW_OBJECTS( "4100003x4" ),

    /**
     * Single view messages.
     */
    SINGLE_VIEW( "4100004x4" ),

    /**
     * Change status messages.
     */
    CHANGE_STATUS( "4100005x4" ),

    /**
     * Manage messages.
     */
    MANAGE( "4100006x4" ),

    /**
     * Delete messages.
     */
    DELETE( "4100007x4" ),

    /**
     * Add meta data messages.
     */
    ADD_META_DATA( "4100008x4" ),

    /**
     * Link messages.
     */
    LINK( "4100009x4" ),

    /**
     * Remove link messages.
     */
    REMOVE_LINK( "4100010x4" ),

    /**
     * Add section messages.
     */
    ADD_SECTION( "4100011x4" ),

    /**
     * Permissions messages.
     */
    PERMISSIONS( "4100012x4" ),

    /**
     * Open file messages.
     */
    OPEN_FILE( "4100013x4" ),

    /**
     * Edit messages.
     */
    EDIT( "4100014x4" ),

    /**
     * Create workflow project messages.
     */
    CREATE_WORKFLOW_PROJECT( "4100015x4" ),

    /**
     * Move to messages.
     */
    MOVE_TO( "4100016x4" ),

    /**
     * Import workflow messages.
     */
    IMPORT_WORKFLOW( "4100017x4" ),

    /**
     * Create messages.
     */
    CREATE( "4100018x4" ),

    /**
     * Deleted objects messages.
     */
    DELETED_OBJECTS( "4100019x4" ),

    /**
     * Sync download messages.
     */
    SYNC_DOWNLOAD( "4100020x4" ),

    /**
     * Change type messages.
     */
    CHANGE_TYPE( "4100021x4" ),

    /**
     * Sync upload messages.
     */
    SYNC_UPLOAD( "4100022x4" ),

    /**
     * Checkout messages.
     */
    CHECKOUT( "4100023x4" ),

    /**
     * Checkin messages.
     */
    CHECKIN( "4100024x4" ),

    /**
     * Discard local file messages.
     */
    DISCARD_LOCAL_FILE( "4100025x4" ),

    /**
     * Sync abort messages.
     */
    SYNC_ABORT( "4100026x4" ),

    /**
     * Restore messages.
     */
    RESTORE( "4100027x4" ),

    /**
     * Download to folder messages.
     */
    DOWNLOAD_TO_FOLDER( "4100028x4" ),

    /**
     * Search messages.
     */
    SEARCH( "4100029x4" ),

    /**
     * Download messages.
     */
    DOWNLOAD( "4100030x4" ),

    /**
     * Create dummy variant messages.
     */
    CREATE_DUMMY_VARIANT( "4100031x4" ),

    /**
     * Edit profile messages.
     */
    EDIT_PROFILE( "4100032x4" ),

    /**
     * Free license messages.
     */
    FREE_LICENSE( "4100033x4" ),

    /**
     * Show tail messages.
     */
    SHOW_TAIL( "4100034x4" ),

    /**
     * Run workflow dialog messages.
     */
    RUN_WORKFLOW_DIALOG( "4100035x4" ),

    /**
     * Create workflow messages.
     */
    CREATE_WORKFLOW( "4100036x4" ),

    /**
     * Update workflow messages.
     */
    UPDATE_WORKFLOW( "4100037x4" ),

    /**
     * Import workflow 2 messages.
     */
    IMPORT_WORKFLOW2( "4100038x4" ),

    /**
     * Workflows messages.
     */
    WORKFLOWS( "4100039x4" ),

    /**
     * Jobs messages.
     */
    JOBS( "4100040x4" ),

    /**
     * Stop messages.
     */
    STOP( "4100041x4" ),

    /**
     * Open working dir messages.
     */
    OPEN_WORKING_DIR( "4100042x4" ),

    /**
     * Bookmarks messages.
     */
    BOOKMARKS( "4100043x4" ),

    /**
     * Search workflow messages.
     */
    SEARCH_WORKFLOW( "4100044x4" ),

    /**
     * Run workflow messages.
     */
    RUN_WORKFLOW( "4100045x4" ),

    /**
     * Rerun job messages.
     */
    RERUN_JOB( "4100046x4" ),

    /**
     * Discard job messages.
     */
    DISCARD_JOB( "4100047x4" ),

    /**
     * Download job logs messages.
     */
    DOWNLOAD_JOB_LOGS( "4100048x4" ),

    /**
     * Heatmap messages.
     */
    HEATMAP( "4100049x4" ),

    /**
     * Plot bubble chart messages.
     */
    PLOT_BUBBLE_CHART( "4100050x4" ),

    /**
     * Plot correlation messages.
     */
    PLOT_CORRELATION( "4100051x4" ),

    /**
     * Generate image messages.
     */
    GENERATE_IMAGE( "4100052x4" ),

    /**
     * Download csv file messages.
     */
    DOWNLOAD_CSV_FILE( "4100053x4" ),

    /**
     * Add messages.
     */
    ADD( "4100054x4" ),

    /**
     * Remove messages.
     */
    REMOVE( "4100055x4" ),

    /**
     * Workflow jobs messages.
     */
    WORKFLOW_JOBS( "4100056x4" ),

    /**
     * Open log messages.
     */
    OPEN_LOG( "4100057x4" ),

    /**
     * Run messages.
     */
    RUN( "4100058x4" ),

    /**
     * Create copy messages.
     */
    CREATE_COPY( "4100059x4" ),

    /**
     * Create loadcase messages.
     */
    CREATE_LOADCASE( "4100060x4" ),

    /**
     * Run schemes messages.
     */
    RUN_SCHEMES( "4100061x4" ),

    /**
     * Edit scheme messages.
     */
    EDIT_SCHEME( "4100062x4" ),

    /**
     * Create scheme messages.
     */
    CREATE_SCHEME( "4100063x4" ),

    /**
     * Delete scheme messages.
     */
    DELETE_SCHEME( "4100064x4" ),

    /**
     * Edit training algo messages.
     */
    EDIT_TRAINING_ALGO( "4100065x4" ),

    /**
     * Create training algo messages.
     */
    TRAINING_ALGO_CREATED( "4100066x4" ),

    /**
     * Delete training algo messages.
     */
    DELETE_TRAINING_ALGO( "4100067x4" ),

    /**
     * Edit category messages.
     */
    EDIT_CATEGORY( "4100068x4" ),

    /**
     * Create category messages.
     */
    CREATE_CATEGORY( "4100069x4" ),

    /**
     * Delete category messages.
     */
    DELETE_CATEGORY( "4100070x4" ),

    /**
     * Cb 2 run messages.
     */
    CB2_RUN( "4100071x4" ),

    /**
     * Update messages.
     */
    UPDATE( "4100072x4" ),

    /**
     * Link items messages.
     */
    LINK_ITEMS( "4100073x4" ),

    /**
     * Show workflow jobs messages.
     */
    SHOW_WORKFLOW_JOBS( "4100074x4" ),

    /**
     * Export data messages.
     */
    EXPORT_DATA( "4100075x4" ),

    /**
     * Bulk delete messages.
     */
    BULK_DELETE( "4100076x4" ),

    /**
     * Create local directory messages.
     */
    CREATE_LOCAL_DIRECTORY( "4100077x4" ),

    /**
     * Open with messages.
     */
    OPEN_WITH( "4100078x4" ),

    /**
     * Compare messages.
     */
    COMPARE( "4100079x4" ),

    /**
     * Free licenses messages.
     */
    FREE_LICENSES( "4100080x4" ),

    /**
     * Run scheme messages.
     */
    RUN_SCHEME( "4100081x4" ),

    /**
     * Delete bulk messages.
     */
    DELETE_BULK( "4100082x4" ),

    /**
     * Restore bulk messages.
     */
    RESTORE_BULK( "4100083x4" ),

    /**
     * None messages.
     */
    NONE( "4100084x4" ),

    /**
     * View messages.
     */
    VIEW( "4100085x4" ),

    /**
     * Read messages.
     */
    READ( "4100086x4" ),

    /**
     * Write messages.
     */
    WRITE( "4100087x4" ),

    /**
     * The Delete.
     */
    // Note: renamed from DELETE to DELETE_ to avoid conflict with the keyword
    DELETE_( "4100088x4" ),

    /**
     * The Restore.
     */
    // Note: renamed from RESTORE to RESTORE_ to avoid conflict with the previous RESTORE
    RESTORE_( "4100089x4" ),

    /**
     * The Manage.
     */
    // Note: renamed from MANAGE to MANAGE_ to avoid conflict with the previous MANAGE
    MANAGE_( "4100090x4" ),

    /**
     * Create new object messages.
     */
    CREATE_NEW_OBJECT( "4100091x4" ),

    /**
     * Execute messages.
     */
    EXECUTE( "4100092x4" ),

    /**
     * Kill messages.
     */
    KILL( "4100093x4" ),

    /**
     * Share messages.
     */
    SHARE( "4100094x4" ),

    /**
     * All objects messages.
     */
    ALL_OBJECTS( "4100095x4" ),

    /**
     * Properties messages.
     */
    PROPERTIES( "4100096x4" ),

    /**
     * Metadata messages.
     */
    METADATA( "4100097x4" ),

    /**
     * Versions messages.
     */
    VERSIONS( "4100098x4" ),

    /**
     * Linked to messages.
     */
    LINKED_TO( "4100099x4" ),

    /**
     * Linked from messages.
     */
    LINKED_FROM( "4100100x4" ),

    /**
     * Audit messages.
     */
    AUDIT( "4100101x4" ),

    /**
     * Trail messages.
     */
    TRAIL( "4100102x4" ),

    /**
     * Linked items messages.
     */
    LINKED_ITEMS( "4100103x4" ),

    /**
     * Movie messages.
     */
    MOVIE( "4100104x4" ),

    /**
     * Preview messages.
     */
    PREVIEW( "4100105x4" ),

    /**
     * Overview messages.
     */
    OVERVIEW( "4100106x4" ),

    /**
     * Substitute from messages.
     */
    SUBSTITUTE_FROM( "4100107x4" ),

    /**
     * Substitute cards messages.
     */
    SUBSTITUTE_CARDS( "4100108x4" ),

    /**
     * Sub project messages.
     */
    SUB_PROJECT( "4100109x4" ),

    /**
     * Designer messages.
     */
    DESIGNER( "4100110x4" ),

    /**
     * Workflow messages.
     */
    WORKFLOW( "4100111x4" ),

    /**
     * Select permission messages.
     */
    SELECT_PERMISSION( "4100112x4" ),

    /**
     * Select users messages.
     */
    SELECT_USERS( "4100113x4" ),

    /**
     * Select groups messages.
     */
    SELECT_GROUPS( "4100114x4" ),

    /**
     * Edit metadata messages.
     */
    EDIT_METADATA( "4100115x4" ),

    /**
     * Edit loadcase messages.
     */
    EDIT_LOADCASE( "4100116x4" ),

    /**
     * Set inherited messages.
     */
    SET_INHERITED( "4100117x4" ),

    /**
     * Unset inherited messages.
     */
    UNSET_INHERITED( "4100118x4" ),

    /**
     * Curves messages.
     */
    CURVES( "4100119x4" ),

    /**
     * Value messages.
     */
    VALUE( "4100120x4" ),

    /**
     * Model messages.
     */
    MODEL( "4100121x4" ),

    /**
     * Model files messages.
     */
    MODEL_FILES( "4100122x4" ),

    /**
     * Report messages.
     */
    REPORT( "4100123x4" ),

    /**
     * Trace messages.
     */
    TRACE( "4100124x4" ),

    /**
     * Html messages.
     */
    HTML( "4100125x4" ),

    /**
     * View 3 d messages.
     */
    VIEW_3D( "4100126x4" ),
    /**
     * The Dashboard.
     */
    DASHBOARD( "4100127x4" ),

    /**
     * Files messages.
     */
    FILES( "4100128x4" ),

    /**
     * Monitor messages.
     */
    MONITOR( "4100129x4" ),

    /**
     * Log messages.
     */
    LOG( "4100130x4" ),

    /**
     * Data created messages.
     */
    DATA_CREATED( "4100131x4" ),

    /**
     * Parameters messages.
     */
    PARAMETERS( "4100132x4" ),

    /**
     * Child jobs messages.
     */
    CHILD_JOBS( "4100133x4" ),

    /**
     * Scheme messages.
     */
    SCHEME( "4100134x4" ),

    /**
     * Runs on messages.
     */
    RUNS_ON( "4100135x4" ),

    /**
     * Maximum execution time messages.
     */
    MAXIMUM_EXECUTION_TIME( "4100136x4" ),

    /**
     * Additional files local messages.
     */
    ADDITIONAL_FILES_LOCAL( "4100137x4" ),

    /**
     * Additional files sus messages.
     */
    ADDITIONAL_FILES_SUS( "4100138x4" ),

    /**
     * Additional files server messages.
     */
    ADDITIONAL_FILES_SERVER( "4100139x4" ),

    /**
     * Cb 2 additional file messages.
     */
    CB2_ADDITIONAL_FILE( "4100140x4" ),

    /**
     * Solver type messages.
     */
    SOLVER_TYPE( "4100141x4" ),

    /**
     * Variant reference messages.
     */
    VARIANT_REFERENCE( "4100142x4" ),

    /**
     * Select reference messages.
     */
    SELECT_REFERENCE( "4100143x4" ),

    /**
     * Pause beta messages.
     */
    PAUSE_BETA( "4100144x4" ),

    /**
     * Resume beta messages.
     */
    RESUME_BETA( "4100145x4" ),

    /**
     * Run qa dyna messages.
     */
    RUN_QA_DYNA( "4100146x4" ),

    /**
     * Cpu messages.
     */
    CPU( "4100147x4" ),

    /**
     * Ez messages.
     */
    EZ( "4100148x4" ),

    /**
     * Dyna version messages.
     */
    DYNA_VERSION( "4100149x4" ),

    /**
     * Master control messages.
     */
    MASTER_CONTROL( "4100150x4" ),

    /**
     * Mat db messages.
     */
    MAT_DB( "4100151x4" ),

    /**
     * Mat db qs messages.
     */
    MAT_DB_QS( "4100152x4" ),

    /**
     * Base messages.
     */
    BASE( "4100153x4" ),

    /**
     * Parameters messages.
     */
    SYSTEM_JOBS( "4100154x4" ),
    /**
     * The Simulation setup.
     */
    // Note: renamed from PARAMETERS to PARAMETERS_ to avoid conflict with the previous PARAMETERS
    REVIEW_INPUTS( "4100155x4" ),

    /**
     * Review inputs messages.
     */
    RESULTS( "4100156x4" ),

    /**
     * Selected users messages.
     */
    SELECTED_USERS( "4100157x4" ),

    /**
     * Selected groups messages.
     */
    SELECTED_GROUPS( "4100158x4" ),

    /**
     * Select location messages.
     */
    SELECT_LOCATION( "4100159x4" ),

    /**
     * Workflow saved successfully messages.
     */
    WORKFLOW_SAVED_SUCCESSFULLY( "4100160x4" ),

    /**
     * Workflow updated successfully messages.
     */
    WORKFLOW_UPDATED_SUCCESSFULLY( "4100161x4" ),

    /**
     * Failed to save workflow messages.
     */
    FAILED_TO_SAVE_WORKFLOW( "4100162x4" ),

    /**
     * Failed to import workflow messages.
     */
    FAILED_TO_IMPORT_WORKFLOW( "4100163x4" ),

    /**
     * Failed to update workflow messages.
     */
    FAILED_TO_UPDATE_WORKFLOW( "4100164x4" ),

    /**
     * Algorithms count data fetched messages.
     */
    ALGORITHMS_COUNT_DATA_FETCHED( "4100165x4" ),

    /**
     * Projects list fetched successfully messages.
     */
    PROJECTS_LIST_FETCHED_SUCCESSFULLY( "4100166x4" ),

    /**
     * Workflows list fetched successfully messages.
     */
    WORKFLOWS_LIST_FETCHED_SUCCESSFULLY( "4100167x4" ),

    /**
     * Wf scheme created messages.
     */
    WF_SCHEME_CREATED( "4100168x4" ),

    /**
     * System count data fetched messages.
     */
    SYSTEM_COUNT_DATA_FETCHED( "4100169x4" ),

    // Process Messages

    /**
     * Process command messages.
     */
    PROCESS_COMMAND( "4200001x4" ),

    /**
     * Process output messages.
     */
    PROCESS_OUTPUT( "4200002x4" ),

    /**
     * Process error messages.
     */
    PROCESS_ERROR( "4200003x4" ),

    /**
     * Process warning messages.
     */
    PROCESS_WARNING( "4200004x4" ),

    /**
     * Process exit code messages.
     */
    PROCESS_EXIT_CODE( "4200005x4" ),

    /**
     * Process signal messages.
     */
    PROCESS_SIGNAL( "4200006x4" ),

    /**
     * Loadcase messages.
     */
    LOADCASE( "9800012x4" ),

    // OAuthService messages ***/
    /**
     * No UserDirectory found with the matching id
     */
    NO_SUCH_DIRECTORY_EXISTS( "8900001x4" ),

    /**
     * State mismatch
     */
    STATE_MISMATCH( "8900002x4" ),

    /**
     * WellKnownURL communication failed
     */
    COMMUNICATION_FAILURE( "8900003x4" ),

    /**
     * OAuth Authorization Failure
     */
    AUTHORIZATION_FAILURE( "8900004x4" ),

    /**
     * Message shown to user after inactive account creation
     */
    INACTIVE_USER_CREATION_MESSAGE( "8900005x4" ),

    /**
     * User already registered in system
     */
    ALREADY_REGISTERED_MESSAGE( "8900006x4" ),

    /**
     * User profile creation failed
     */
    USER_CREATION_FAILURE_MESSAGE( "8900007x4" ),

    // StringListConverter messages

    /**
     * String to list conversion failed
     */
    LIST_TO_JSON_CONVERSION_FAILED( "7100001x4" ),

    /**
     * List to string conversion failed
     */
    JSON_TO_LIST_CONVERSION_FAILED( "7100002x4" ),

    /**
     * No oauthDirectory records found in db for authentication
     */
    NO_OAUTH_DIRECTORY_RECORDS_FOUND( "8900008x4" ),

    /**
     * no user account found with the matching uid
     */
    NO_ACCOUNT_FOUND_MATCHING_UID( "8700001x4" ),

    /**
     * cb2 session is inactive login to cb2 again
     */
    CB2_SESSION_INACTIVE_LOGIN_TO_CB2_AGAIN( "8700002x4" ),

    /**
     * user session is expired when trying to login to cb2 via bmw auth
     */
    USER_SESSION_EXPIRED_LOGIN_AGAIN( "8700003x4" ),

    /**
     * cb2 login via rest failed
     */
    CB2_LOGIN_VIA_REST_FAILED( "8700004x4" );

    /**
     * The Key.
     */
    private final String key;

    /**
     * Instantiates a new Messages.
     *
     * @param key
     *         the key
     */
    Messages( String key ) {
        this.key = key;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }
}