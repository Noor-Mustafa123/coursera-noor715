/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.suscore.common.enums.simflow;

/**
 * Utility class which will contains all the Error/Info Messages of System used in all the modules.
 *
 * @author Shan Arshad
 * @since 1.0
 */
public enum WFEMessages {

    /**
     * The address invalid.
     */
    ADDRESS_INVALID( "{0} email address not valid" ),

    /**
     * The add files in dataobject failed.
     */
    ADD_FILES_IN_DATAOBJECT_FAILED( "Add files in dataobject failed" ),

    /**
     * The allowed keys cant be null.
     */
    ALLOWED_KEYS_CANT_BE_NULL( "Allowed keys cannot be null." ),

    /**
     * The argument is null or empty.
     */
    ARGUMENT_CAN_NOT_BE_NULL( "Argument {0} is empty or null." ),

    /**
     * The category deleted successfully.
     */
    CATEGORY_DELETED_SUCCESSFULLY( "Category Deleted Successfully" ),

    /**
     * The category does not exist.
     */
    CATEGORY_DOES_NOT_EXIST( "Category:{0} does not exist" ),

    /**
     * The category saved successfully.
     */
    CATEGORY_SAVED_SUCCESSFULLY( "Category Saved Successfully" ),

    /**
     * The category updated successfully.
     */
    CATEGORY_UPDATED_SUCCESSFULLY( "Category Updated Successfully" ),

    /**
     * The config file not found.
     */
    CONFIG_FILE_NOT_FOUND( "config file not found :{0}" ),

    /**
     * The config not provided.
     */
    CONFIG_NOT_PROVIDED( "Configurations not provided." ),

    /**
     * The connection not applicable.
     */
    CONNECTION_NOT_APPLICABLE( "Not applicable in config target: {0} source : {1}" ),

    /**
     * The connection not valid.
     */
    CONNECTION_NOT_VALID( "Not valid connection -- target: {0} source :{1} " ),

    /**
     * The csv do not have valid dimensions and units.
     */
    CSV_DO_NOT_HAVE_VALID_DIMENSIONS_AND_UNITS( "CSV do not have valid Dimensions and units" ),

    /**
     * The current files of dataobject.
     */
    CURRENT_FILES_FOR_DATAOBJECT( "Current files for dataobject : {0}" ),

    /**
     * The db database query error.
     */
    DB_DATABASE_QUERY_ERROR( "Database/Query error." ),

    /**
     * The deamon already running on port.
     */
    DEAMON_ALREADY_RUNNING_ON_PORT( "Daemon already running on port :{0}" ),

    /**
     * The deamon started.
     */
    DEAMON_STARTED( "Daemon Started on port :{0}" ),

    /**
     * The directory path does not exist.
     */
    DIRECTORY_PATH_DOES_NOT_EXIST( "Directory path does not exist or not of type directory." ),

    /**
     * The files downloaded.
     */
    DOWNLOADED_FILES( "Downloaded Files : {0}" ),

    /**
     * The filter provided for downloading files.
     */
    DOWNLOAD_FILES_FILTER_PROVIDED( "Files filter provided : {0}" ),

    /**
     * The duplicate element name.
     */
    DUPLICATE_ELEMENT_NAME( "Duplicate Element name : {0}" ),

    /**
     * The duplicate element name in workflow.
     */
    DUPLICATE_ELEMENT_NAME_IN_WORKFLOW( "Duplicate Element name in User WF  : {0}" ),

    /**
     * The duplicate field.
     */
    DUPLICATE_FIELD( "Duplicate Field : {0} in element : {1}" ),

    /**
     * The duplicate field in workflow element.
     */
    DUPLICATE_FIELD_IN_WORKFLOW_ELEMENT( "Field is duplicated in workflow element." ),

    /**
     * The number of elements executed .
     */
    ELEMENTS_EXECUTED( "Elements Executed: {0}" ),

    /**
     * The element can not be null.
     */
    ELEMENT_CAN_NOT_BE_NULL( "Element can't be null." ),

    /**
     * The element execution time is not valid.
     */
    ELEMENT_EXECUTION_TIME_IS_NOT_VALID( "{0} execution time is not valid." ),

    /**
     * The element name has stop execution due to time out.
     */
    ELEMENT_HAS_STOP_EXECUTION_DUE_TO_TIME_OUT( "Element :{0} has stop execution because of exception time." ),

    /**
     * The element is going to execute for the seconds given.
     */
    ELEMENT_IS_GOING_TO_EXECUTE_FOR_SECONDS( "Element :{0} is going to execute for {1} seconds." ),

    /**
     * The element is going to execute without time limit.
     */
    ELEMENT_IS_GOING_TO_EXECUTE_WITHOUT_TIME_LIMIT( "Element :{0} is going to execute without any time limit." ),

    /**
     * ELEMENT_MONITORING_THREAD_ERROR
     */
    ELEMENT_MONITORING_THREAD_ERROR( "Error occurred while monitoring the executing WFE for {0}. Thread Exiting..." ),

    /**
     * ELEMENT_MONITORING_THREAD_WARNING
     */
    ELEMENT_MONITORING_THREAD_WARNING( "Thread interrupted while monitoring the executing WFE for {0}" ),

    /**
     * The element name must be unique.
     */
    ELEMENT_NAME_MUST_BE_UNIQUE( "Element name must be unique in a workflow." ),

    /**
     * The element not executed.
     */
    ELEMENT_NOT_EXECUTED( " element not executed. Because execution time is {0}" ),

    /**
     * The element wait started.
     */
    ELEMENT_WAIT_STARTED( "Element Wait started for : {0} {1}" ),

    /**
     * The element wait exec suspended for sec.
     */
    ELE_WAIT_EXEC_SUSPENDED_FOR_SEC( "Element Wait execution suspended for sec : {0}" ),

    /**
     * The empty element key.
     */
    EMPTY_ELEMENT_KEY( "Element key cannot be empty." ),

    /**
     * The empty file.
     */
    EMPTY_FILE( "File is empty :{0}" ),

    /**
     * The empty param.
     */
    EMPTY_PARAM( "Empty or Null parameters." ),

    /**
     * The empty run mode.
     */
    EMPTY_RUN_MODE( "Runmode is empty." ),

    /**
     * The error in script format.
     */
    ERROR_IN_SCRIPT_FORMAT( "ERROR_IN_SCRIPT_FORMAT : {0}" ),

    /**
     * The workflow error message .
     */
    ERROR_MESSAGE( "Error Message: {0}" ),

    /**
     * The error while executing script.
     */
    ERROR_WHILE_EXECUTING_SCRIPT( "Error while executing Script : {0}" ),

    /**
     * The execution timeout.
     */
    EXECUTION_TIMEOUT( "Execution timout occur of element {0}" ),

    /**
     * The existing vault files.
     */
    EXISTING_VAULT_FILES( "Existing vault files of dataobject : {0}" ),

    /**
     * The field dont have expected leading trailings.
     */
    FIELD_DONT_HAVE_EXPECTED_LEADING_TRAILINGS( "{0}: {1} does not have proper leading '{2}' and trailings '{3}'" ),

    /**
     * The field not allowed.
     */
    FIELD_NOT_ALLOWED( "Field not allowed : {0}" ),

    /**
     * The field should have only one valid variable.
     */
    FIELD_SHOULD_HAVE_ONLY_ONE_VALID_VARIABLE( "Text field should have only one variable. Variable: '{0}' is not valid." ),

    /**
     * The filed key not allowed.
     */
    FILED_KEY_NOT_ALLOWED( " Field key is not valid: {0}" ),

    /**
     * The files present on server.
     */
    FILES_FOUND_FROM_SERVER( "Files founds from server : {0}" ),

    /**
     * The file not exist.
     */
    FILE_NOT_EXIST( "{0} file does not exist." ),

    /**
     * The file path not exist.
     */
    FILE_PATH_NOT_EXIST( "File path does not exist :{0}" ),

    /**
     * The file to upload should exist.
     */
    FILE_TO_UPLOAD_SHOULD_EXIST( "File to upload should exist." ),

    /**
     * The get workflow complete.
     */
    GET_WORKFLOW_COMPLETE( "Get User Workflow from Server Completed." ),

    /**
     * The get workflow config.
     */
    GET_WORKFLOW_CONFIG( "Get Workflow Configuration form Server Completed" ),

    /**
     * The get workflow from id.
     */
    GET_WORKFLOW_FROM_ID( "Get Workflow from server with id : {0}" ),

    /**
     * The going to be overwrite.
     */
    GOING_TO_BE_OVERWRITE( "List of files are going to be overwrite : {0}" ),

    /**
     * The going to execute download sus files with mapping.
     */
    GOING_TO_EXECUTE_DOWNLOAD_SUS_FILES_WITH_MAPPING( "Going to execute downloadSusFiles with mapping: " ),

    /**
     * The going to execute download sus files with parameters.
     */
    GOING_TO_EXECUTE_DOWNLOAD_SUS_FILES_WITH_PARAMETERS( "Going to execute downloadSusFiles with parameters: " ),

    /**
     * The graph created .
     */
    GRAPH_CREATED( "Graph Created" ),

    /**
     * The hibernate session is null.
     */
    HIBERNATE_SESSION_IS_NULL( "Hibernate session is null." ),

    /**
     * The inavlid seconds provided.
     */
    INAVLID_SECONDS_PROVIDED( "Invalid seconds provided : {0}" ),

    /**
     * The invalid applicable key.
     */
    INVALID_APPLICABLE_KEY( "Invalid applicable key: {0}" ),

    /**
     * The invalid applicable value.
     */
    INVALID_APPLICABLE_VALUE( "Invalid applicable value : {0}" ),

    /**
     * The invalid command.
     */
    INVALID_COMMAND_PROVIDED( "Invalid Command Provided!" ),

    /**
     * The invalid file.
     */
    INVALID_DIRECTORY( "Not a valid directory:{0}" ),

    /**
     * The invalid element key.
     */
    INVALID_ELEMENT_KEY( "Invalid element key: {0}" ),

    /**
     * The invalid field type.
     */
    INVALID_FIELD_TYPE( "Invalid field type: {0}" ),

    /**
     * The invalid file.
     */
    INVALID_FILE( "Not a valid file:{0}" ),

    /**
     * The invalid param.
     */
    INVALID_PARAM( "Invalid parameters provided." ),

    /**
     * The invalid run mode.
     */
    INVALID_RUN_MODE( "Invalid runmode: {0}" ),

    /**
     * The invalid status change.
     */
    INVALID_STATUS_CHANGE( "You cannot move to :{0} from status :{1}" ),

    /**
     * The invalid uuid.
     */
    INVALID_UUID( "Not valid UUID : {0}" ),

    /**
     * invalid value provided for element field.
     */
    INVALID_VALUE_FOR_FIELD( "Invalid Value provided for field : '{0}' type mismatches." ),

    /**
     * The invalid workflow version.
     */
    INVALID_WORKFLOW_VERSION( "Invalid version provided for workflow" ),

    /**
     * The invisibility status message.
     */
    INVISIBILITY_STATUS_MESSAGE( "You Cannot see the workflow with status:{0} " ),

    /**
     * The job created.
     */
    JOB_CREATED( "Job Created" ),

    /**
     * The job does and workflow element fields size does not match.
     */
    JOB_DOES_AND_WORKFLOW_FIELDS_NOT_SAME_FOR_ELEMENT( "Job and workflow does not have same number of fields for element: {0}." ),

    /**
     * The job input field does not in workflow.
     */
    JOB_DOES_NOT_HAVE_ELEMENT_FIELDS_IN_WORKFLOW( "Job does not have fields of element: {0}." ),

    /**
     * The job file not exist.
     */
    JOB_FILE_NOT_EXIST( "Job file doesn't exist at Path : {0}" ),

    /**
     * The job id is null.
     */
    JOB_ID_IS_NULL( "Input job id is null!" ),

    /**
     * The job input field does not in workflow.
     */
    JOB_INPUT_FIELD_DOES_NOT_MATCH_IN_WORKFLOW_ELEMENT( "Job input field: {0} does not match in element: {1} of workflow." ),

    /**
     * The job name.
     */
    JOB_NAME( "Job name : {0}" ),

    /**
     * The job name is null.
     */
    JOB_NAME_IS_NULL( "Input job name is null!" ),

    /**
     * The job not found.
     */
    JOB_NOT_FOUND( "Job not Found." ),

    /**
     * The job not run on this os.
     */
    JOB_NOT_RUN_ON_THIS_OS( "Unable to find selected machine. Cannot run this job on : {0}" ),

    /**
     * The job not updated.
     */
    JOB_NOT_UPDATED( "Job not updated" ),

    /**
     * The job saved on server.
     */
    JOB_SAVED_ON_SERVER( "Job {0} Saved successfully on Server" ),

    /**
     * The environment creating for job.
     */
    ENVIRONMENT_CREATING_FOR_JOB( "Creating environment for job" ),

    /**
     * The Installing whl files in environment.
     */
    INSTALLING_WHL_FILES_IN_ENVIRONMENT( "Installing whl file in new environment" ),

    /**
     * The Installing dependencies in environment.
     */
    INSTALLING_DEPENDENCIES_IN_ENVIRONMENT( "Installing dependencies in new environment" ),

    /**
     * The environment created.
     */
    ENVIRONMENT_CREATED( "Environment created and dependencies installed" ),

    /**
     * The job status is null.
     */
    JOB_STATUS_IS_NULL( "Input job status is null!" ),

    /**
     * The job stop failed.
     */
    JOB_STOP_FAILED( "Job Stop failed" ),

    /**
     * The job stop successfully.
     */
    JOB_STOP_SUCCESSFULLY( "Job Stop Successfully" ),

    /**
     * The job submitted.
     */
    JOB_SUBMITTED( "Job Submitted Successfully." ),

    /**
     * The job updated.
     */
    JOB_UPDATED( "Job Updated" ),

    /**
     * The job user is null.
     */
    JOB_USER_IS_NULL( "Input user is null!" ),

    /**
     * The job workflow id is null.
     */
    JOB_WORKFLOW_ID_IS_NULL( "Input workflow id is null!" ),

    /**
     * The settings file is not valid json.
     */
    JSON_FILE_IS_NOT_VALID_JSON( "Json file is not have valid json." ),

    /**
     * The json file not found.
     */
    JSON_FILE_NOT_FOUND( "Settings file of routine is not found or don't have rights." ),

    /**
     * The key or value is missing in object.
     */
    KEY_OR_VALUE_IS_MISSING_IN_OBJECT( "Key or value is missing in mapping object: {0}" ),

    /**
     * The license does not allow this feature.
     */
    LICENSE_DOES_NOT_ALLOW_THIS_FEATURE( "License does not allow this feature" ),

    /**
     * The locking start for file.
     */
    LOCKING_START_FOR_FILE( "Locking start for the file : {0}" ),

    /**
     * The lock achieved.
     */
    LOCK_ACHIEVED( "Lock achieved for the file : {0}" ),

    /**
     * The logger not found.
     */
    LOGGER_NOT_FOUND( "Logger not found." ),

    /**
     * The manager license limit is consumed.
     */
    MANAGER_LICENSE_LIMIT_IS_CONSUMED( "Manager license limit is consumed. Please free or checkin some license" ),

    /**
     * The mapping key or value is missing in parameters.
     */
    MAPPING_KEY_OR_VALUE_IS_MISSING_IN_PARAMETERS( "Mapping {0}: {1}  is missing or have not direct connections." ),

    /**
     * The custom method sufix.
     */
    MESSAGE_IN_VALID_CUSTOM_METHOD_SUFIX( " not a valid method of SimuSpace Expression language." ),

    /**
     * The message provided conditional expression is invalid.
     */
    MESSAGE_PROVIDED_EXPRESSION_IS_INVALID(
            "Provided conditional expression: \"{0}\" can not be parsed on SimuSpace Expression Language. Seems to be invalid." ),
    /**
     * The move to status cannot null.
     */
    MOVE_TO_STATUS_CANNOT_NULL( "moveOldVersionToStatus cannot be null for status:{0} " ),

    /**
     * The new file created.
     */
    NEW_FILE_CREATED( "New File created : " ),

    /**
     * The no category exist with id.
     */
    NO_CATEGORY_EXIST_WITH_ID( "No Category Exist with Id: {0}" ),

    /**
     * The no connection info.
     */
    NO_CONNECTION_INFO( "No connection information is provided." ),

    /**
     * The no connection info for element.
     */
    NO_CONNECTION_INFO_FOR_ELEMENT( "No connection information is provided for Element: {0}" ),

    /**
     * The no element in workflow.
     */
    NO_ELEMENT_IN_WORKFLOW( "Workflow has no elements" ),

    /**
     * The files that are not found for filter.
     */
    NO_FILE_FOUND_FOR_FILTER( "No files found for the filter : {0}" ),

    /**
     * The project type not found for object type name.
     */
    OBJECT_TYPE_NOT_FOUND_FOR_OBJECT_NAME( "Object type not found in import-settings for Object name: {0}" ),

    /**
     * The object type not valid for status.
     */
    OBJECT_TYPE_NOT_VALID_FOR_STATUS( "Type:{0} Not valid for status configration" ),

    /**
     * The param dont contain argument.
     */
    PARAM_DONT_CONTAIN_ARGUMENT( "Parameters does not contains argument: {0} or the element: {1} does not have direct connection." ),

    /**
     * The param used should have some value.
     */
    PARAM_USED_SHOULD_HAVE_SOME_VALUE( "Parameters used: {0} should have not null value." ),

    /**
     * The path not exists.
     */
    PATH_NOT_EXISTS( "Path not exists : {0}" ),

    /**
     * The path to import should zip or directory.
     */
    PATH_TO_IMPORT_SHOULD_ZIP_OR_DIRECTORY( "Path: {0} to import should directory." ),

    /**
     * The permission denied for directory.
     */
    PERMISSION_DENIED_FOR_DIRECTORY( "Read Permission Denied for directory :{0}" ),

    /**
     * The permission denied for file.
     */
    PERMISSION_DENIED_FOR_FILE( "Read Permission Denied for file :{0}" ),

    /**
     * The please provide correct date.
     */
    PLEASE_PROVIDE_CORRECT_DATE( "Please provide correct date : {0}" ),

    /**
     * The please provide correct date.
     */
    PLEASE_PROVIDE_IN_MILISECONDS( "Please provide date in ISO 8601 format" ),

    /**
     * The project configurations not found for object.
     */
    PROJECT_CONFIGURATIONS_NOT_FOUND_FOR_OBJECT( "Project configurations not found for objectId: {0}" ),

    /**
     * The project type not found for object type name.
     */
    PROJECT_TYPE_NOT_FOUND_FOR_OBJECT_TYPE_NAME( "Project type not found in configuration for type name: {0}" ),

    /**
     * The python execution path is not set.
     */
    PYTHON_EXECUTION_PATH_IS_NOT_SET( "Python execution path is not set on {0}" ),

    /**
     * The environment creation failed.
     */
    ENVIRONMENT_CREATION_FAILED( "Python environment not created" ),

    /**
     * The installation failed for environment.
     */
    INSTALLATION_FAILED_FOR_ENVIRONMENT( "{0} installation failed" ),

    /**
     * The python execution path not set.
     */
    PYTHON_EXECUTION_PATH_NOT_SET(
            "Python execution path is not set on : {0}. The pythonpath {1} is not configured correctly, please configure in {2}. For Example: {3}" ),

    /**
     * The record deleted successfully.
     */
    RECORD_DELETED_SUCCESSFULLY( "Record Deleted Successfully" ),

    /**
     * The record not deleted.
     */
    RECORD_NOT_DELETED( "Record Not Deleted" ) // generic messages
    ,
    /**
     * The record saved successfully.
     */
    RECORD_SAVED_SUCCESSFULLY( "Record Saved Successfully" ),

    /**
     * The record updated successfully.
     */
    RECORD_UPDATED_SUCCESSFULLY( "Record Updated Successfully" ),

    /**
     * The field is missing or have in valid value.
     */
    REQUIRED_FIELD_IS_MISSING( "'{0}' is required field. But that is missing." ),

    /**
     * The field is missing or have in valid value.
     */
    REQUIRED_CUSTOM_ATTRIBUTE_IS_MISSING_FOR( "'{0}' is required field. But that is missing for {1}" ),

    /**
     * Selected item is not a workflow
     */
    SELECTED_ITEM_IS_NOT_A_WORKFLOW( "Selected item is not a workflow" ),

    /**
     * The server hostname not provided.
     */
    SERVER_HOSTNAME_NOT_PROVIDED( "Server host name not provided" ),

    /**
     * The server not provided.
     */
    SERVER_NOT_PROVIDED( "Server information not provided" ),

    /**
     * The server not reachable.
     */
    SERVER_NOT_REACHABLE( "Server {0} is not reachable" ),

    /**
     * The server port not provided.
     */
    SERVER_PORT_NOT_PROVIDED( "Server port not provided" ),

    /**
     * The server protocol not provided.
     */
    SERVER_PROTOCOL_NOT_PROVIDED( "Server protocol not provided" ),

    /**
     * The settings file not find.
     */
    SETTINGS_FILE_NOT_FOUND( "Settings file of routine is not found or don't have rights." ),

    /**
     * The simcore files are not available.
     */
    SIMCORE_FILES_ARE_NOT_AVAILABLE( "Simcore files are not availabe !" ),

    /**
     * The simcore response is empty.
     */
    SIMCORE_RESPONSE_IS_EMPTY( "Simcore response is empty !" ),

    /**
     * The files skipped.
     */
    SKIPPED_FILES( "Skipped Files : {0}" ),

    /**
     * The skipping file.
     */
    SKIPPING_FILE( "Skipping file : {0}" ),

    /**
     * The skipping file because parent is in error.
     */
    SKIPPING_FILE_BECAUSE_PARENT_IS_IN_ERROR( "{0} is skipped because its parent not executed successfully." ),

    /**
     * The some fields are missing.
     */
    SOME_FIELDS_ARE_MISSING( "Some fields are invalid ! " ),

    /**
     * The source connection not valid.
     */
    SOURCE_CONNECTION_NOT_VALID( "Source Connection is not valid:{0}" ), // server bundle messages

    /**
     * status already exist.
     */
    STATUS_ALREADY_EXIST( "Status:{0} already exist" ),

    /**
     * The status id be unique.
     */
    STATUS_ID_BE_UNIQUE( "Status Id should be unique: {0} " ),

    /**
     * The status id not empty.
     */
    STATUS_ID_NOT_EMPTY( "Status Id Cannot be empty/null" ),

    /**
     * The status name be unique.
     */
    STATUS_NAME_BE_UNIQUE( "Status Name should be unique: {0}" ),

    /**
     * The status name not empty.
     */
    STATUS_NAME_NOT_EMPTY( "Status Name Cannot be empty" ),

    /**
     * The status not valid for object.
     */
    STATUS_NOT_VALID_FOR_OBJECT( "Not valid status:{0} for Object :{1}" ),

    /**
     * The subworkflow cannot contain another subworkflow.
     */
    SUBWORKFLOW_CANNOT_CONTAIN_ANOTHER_SUBWORKFLOW( "Subworkflow cannot contain another subworkflow : {0}" ),

    /**
     * The sus file download error.
     */
    SUS_FILE_DOWNLOAD_ERROR( "SuS File Download Error : " ),

    /**
     * The target connection not valid.
     */
    TARGET_CONNECTION_NOT_VALID( "Target Connection is not valid:{0}" ),

    /**
     * The token cant be empty.
     */
    TOKEN_CANT_BE_EMPTY( "Token not provided for Job Execution" ),

    /**
     * The token cannot be null.
     */
    TOKEN_CANT_BE_NULL( "Token cannot be null" ),

    /**
     * The token cleared successfully.
     */
    TOKEN_CLEARED_SUCCESSFULLY( "Token cleared successfully" ),

    /**
     * The token is already checked out.
     */
    TOKEN_IS_ALREADY_CHECKED_OUT( "Token: {0} is already checked out." ),

    /**
     * The token is not already checked out.
     */
    TOKEN_IS_NOT_ALREADY_CHECKED_OUT( "Token: {0} is not already checked out." ),

    /**
     * The token is not already checked out please retry.
     */
    TOKEN_IS_NOT_ALREADY_CHECKED_OUT_PLEASE_RETRY( "Token is not already checked out please retry." ),

    /**
     * The total number of elements in workflow .
     */
    TOTAL_ELEMENTS_IN_WORKFLOW( "Total elements in workflow : {0}" ),

    /**
     * The total number of elements to be executed .
     */
    TOTAL_ELEMENTS_TO_BE_EXECUTED( "Total elements to be executed : {0}" ),

    /**
     * The total execution time.
     */
    TOTAL_EXECUTION_TIME( "Total execution time : {0}" ),

    /**
     * The unable to get user.
     */
    UNABLE_TO_GET_USER( "Unable to get user : {0}" ),

    /**
     * The update job.
     */
    UPDATE_JOB( "Job {0} Status Updated at Server to : {1}" ),

    /**
     * The uploading file.
     */
    UPLOADING_FILE( "Uploading file : {0}" ),

    /**
     * The user agent cant be empty.
     */
    USER_AGENT_CANT_BE_EMPTY( "User Agent not provided for Job Execution" ),

    /**
     * The user license limit is consumed.
     */
    USER_LICENSE_LIMIT_IS_CONSUMED( "User license limit is consumed. Please free or checkin some license" ),

    /**
     * The utils invalid special chars.
     */
    UTILS_INVALID_SPECIAL_CHARS( "The string passed ('{0}') contatins an invalid special characters. Pattern '{1}'" ),

    /**
     * The utils invalid value.
     */
    UTILS_INVALID_VALUE( "Value of '{0}' is not valid." ),

    /**
     * The utils name cant be null.
     */
    UTILS_NAME_CANT_BE_NULL( "{0} can not be null or empty." ),

    /**
     * The utils string passed.
     */
    UTILS_STRING_PASSED( "The string passed ('{0}') contains no invalid characters" ), // server bundle messages

    /**
     * The utils too small value.
     */
    UTILS_TOO_SMALL_VALUE( "Value too small for {0} (MinLength={1})." ),

    /**
     * The utils value too large.
     */
    UTILS_VALUE_TOO_LARGE( "Value too large for {0} (MaxLength={1})." ),

    /**
     * The vault files are not available.
     */
    VAULT_FILES_ARE_NOT_AVAILABLE( "Vault Files are not available" ),

    /**
     * The wait execution error.
     */
    WAIT_EXECUTION_ERROR( "Wait Execution Error : {0}" ),

    /**
     * The watching path.
     */
    WATCHING_PATH( "Watching path: {0}" ),

    /**
     * The webservice disabled api.
     */
    WEBSERVICE_DISABLED_API( "This API is disabled" ),

    /**
     * The webservice input cant be null.
     */
    WEBSERVICE_INPUT_CANT_BE_NULL( "Input can not be null" ),

    /**
     * The webservice internal server error.
     */
    WEBSERVICE_INTERNAL_SERVER_ERROR( "Internal server error." ),

    /**
     * The webservice invalid parameters suplied.
     */
    WEBSERVICE_INVALID_PARAMETERS_SUPLIED( "Invalid parameters provided." ),

    /**
     * The webservice invalid property value.
     */
    WEBSERVICE_INVALID_PROPERTY_VALUE( "Invalid Property value : {0}" ),

    /**
     * The webservice json parsing error.
     */
    WEBSERVICE_JSON_PARSING_ERROR( "Unable to parse the json." ) // WebService
    ,
    /**
     * The webservice login again.
     */
    WEBSERVICE_LOGIN_AGAIN( "Login again : User session expired." ),

    /**
     * The webservice pid not found.
     */
    WEBSERVICE_PID_NOT_FOUND( "PID not found : {0}" ),

    /**
     * The webservice properties not registered.
     */
    WEBSERVICE_PROPERTIES_NOT_REGISTERED( "Properties not registered." ),

    /**
     * The webservice property not found.
     */
    WEBSERVICE_PROPERTY_NOT_FOUND( "Property not found : {0}" ),

    /**
     * The workflow aborted.
     */
    WORKFLOW_ABORTED( "Workflow execution aborted" ),

    /**
     * The workflow aborted.
     */
    WORKFLOW_PAUSED( "Workflow execution paused" ),

    /**
     * The workflow aborted.
     */
    WORKFLOW_RESUMED( "Workflow execution resumed" ),

    /**
     * Work flow addition to favorites .
     */
    WORKFLOW_ADDED_TO_FAVORITES( "Workflow added to favorites" ),

    /**
     * The workflow and job number of elements does not match.
     */
    WORKFLOW_AND_JOB_NUMBER_OF_ELEMENTS_DOES_NOT_MATCH( "Workflow and job does not have same number of elements." ),

    /**
     * The workflow stopped on .
     */
    WORKFLOW_COMPLETION_TIME( "Workflow stopped on : {0}" ),

    /**
     * The workflow deleted cannt execute.
     */
    WORKFLOW_DELETED_CANNT_EXECUTE( "Cannot execute deleted workflow" ),

    /**
     * The workflow details.
     */
    WORKFLOW_DETAILS( "WorkFlow Details" ),

    /**
     * The workflow doesnot exist with id.
     */
    WORKFLOW_DOESNOT_EXIST_WITH_ID( "No Workflow exist with Id:{0}" ),

    /**
     * The workflow doesnot exist with id and version id.
     */
    WORKFLOW_DOESNOT_EXIST_WITH_ID_AND_VERSION_ID( "No Workflow exist with Id:{0} and Version:{1}" ),

    /**
     * The workflow element and job fields does not match.
     */
    WORKFLOW_ELEMENT_AND_JOB_FIELDS_DOES_NOT_MATCH( "Element: {0} number fields in workflow and job does not match." ),

    /**
     * The workflow element Name .
     */
    WORKFLOW_ELEMENT_NAME( "Workflow Element Name: {0}" ),

    /**
     * The workflow element type .
     */
    WORKFLOW_ELEMENT_TYPE( "Workflow Element Type: {0}" ),

    /**
     * The workflow build version.
     */
    WORKFLOW_ENGINE_VERSION( "WorkFlow Engine Version : {0}" ),

    /**
     * The workflow executed.
     */
    WORKFLOW_EXECUTED( "Workflow executed successfully" ),

    /**
     * The workflow executed with errors.
     */
    WORKFLOW_EXECUTED_WITH_ERRORS( "Workflow execution stopped because of errors" ),

    /**
     * The workflow stopped.
     */
    WORKFLOW_FAILED( "Workflow execution failed" ),

    /**
     * The workflow has no elements.
     */
    WORKFLOW_HAS_NO_ELEMENTS( "Workflow has no elements" ),

    /**
     * The workflow id cant be null.
     */
    WORKFLOW_ID_CANT_BE_NULL( "Workflow id can not be null" ),

    /**
     * The workflow id should not empty.
     */
    WORKFLOW_ID_SHOULD_NOT_EMPTY( "Workflow Id should not be Empty" ),

    /**
     * The workflow invalid cannt execute.
     */
    WORKFLOW_INVALID_CANNT_EXECUTE( "Cannot execute Invalid workflow" ),

    /**
     * The workflow is deprecated.
     */
    WORKFLOW_IS_DEPRECATED( "Warning !! workflow is deprecated " ),

    /**
     * The workflow executed with errors.
     */
    WORKFLOW_JOB_CANNOT_DELETE( " Workflow  Cannot be deleted.It has Completed Jobs:{0}" ),

    /**
     * The workflow executed with errors.
     */
    WORKFLOW_KILLED_EXTERNALLY( "Workflow execution stopped because it was killed externally" ),

    /**
     * The workflow Name.
     */
    WORKFLOW_NAME( "WorkFlow Name : {0}" ),

    /**
     * The workflow name should not empty.
     */
    WORKFLOW_NAME_SHOULD_NOT_EMPTY( "Workflow name should not be empty" ),

    /**
     * The workflow not executeable.
     */
    WORKFLOW_NOT_EXECUTEABLE( " Workflow is not executable according to configration" ),

    /**
     * The workflow created by.
     */
    WORKFLOW_OWNER( "Owner : {0}" ),

    /**
     * Work flow removal from favorites .
     */
    WORKFLOW_REMOVED_FROM_FAVORITES( "Workflow removed from favorites" ),

    /**
     * The workflow saved succesfully.
     */
    WORKFLOW_SAVED_SUCCESFULLY( "Workflow saved successfully." ),

    /**
     * The workflow start time.
     */
    WORKFLOW_STARTED_ON( "Started on : {0}" ),

    /**
     * The workflow status .
     */
    WORKFLOW_STATUS( "Workflow Status : {0}" ),

    /**
     * The workflow status changed.
     */
    WORKFLOW_STATUS_CHANGED( "  Workflow Status Changed to:{0}" ),

    /**
     * The workflow status not valid.
     */
    WORKFLOW_STATUS_NOT_VALID( " Not Valid Status Value:{0}" ),

    /**
     * The workflow updated successfully.
     */
    WORKFLOW_UPDATED_SUCCESSFULLY( "Workflow Updated Successfully" ),

    /**
     * The workflow validated.
     */
    WORKFLOW_VALIDATED( "Workflow Validated Successfully" ),

    /**
     * The workflow validated.
     */
    WORKFLOW_VALIDATED_JOB_IS_STARTING( "Workflow Validated Successfully. Job has started execution." ),

    /**
     * The workflow version.
     */
    WORKFLOW_VERSION( "WorkFlow version : {0}" ),

    /**
     * The workflow wip already exist.
     */
    WORKFLOW_WIP_ALREADY_EXIST( " Already WIP Exist for workflow:{0}" ),

    /**
     * The workflow wip of other user.
     */
    WORKFLOW_WIP_OF_OTHER_USER( " Cannot open WIP workflow of other user" ),

    /**
     * The workflow working directory cant be null.
     */
    WORKFLOW_WORKING_DIRECTORY_CANT_BE_NULL( "Workflow working directory can not be null." ),

    /**
     * The working directory.
     */
    WORKING_DIRECTORY( "Working Directory : {0}" ), // server bundle messages

    /**
     * The wrong date selected for wait element.
     */
    WRONG_DATE_SELECTED_FOR_WAIT_ELEMENT( "wrong date selected for wait element." ),

    /**
     * The license manage permission message.
     */
    YOU_ARE_NOT_A_LICENSE_MANAGER( "You don't have manage permission of License" ), // Conditional Expression language messaage parsing

    /**
     * The you are not workflow manager.
     */
    YOU_ARE_NOT_WORKFLOW_MANAGER( "You are not a workflow manager" ),

    /**
     * The you are not workflow user.
     */
    YOU_ARE_NOT_WORKFLOW_USER( "You are not a workflow user" ),

    /**
     * The import element properties not valid.
     */
    IMPORT_ELEMENT_PROPERTIES_NOT_VALID( "Please use valid import element properties" );

    /**
     * The value of enumeration.
     */
    private final String value;

    /**
     * Instantiates a new messages.
     *
     * @param value
     *         the value
     */
    WFEMessages( String value ) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

}