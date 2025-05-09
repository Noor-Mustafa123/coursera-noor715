package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.parser.ParseException;

import de.soco.software.simuspace.server.dao.VariableDAO;
import de.soco.software.simuspace.server.model.JobTreeNodeDTO;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.UserJobDTO;

/**
 * This interface is provides the signatures to create/update workflow jobs. Workflow job is created when a workflow is executed. A job can
 * contain information about when the job started, its status, and log statements for the user.
 *
 * @author Aroosa.Bukhari
 */
public interface JobManager {

    /**
     * It gets a workflow job by job id from the database.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the id of job to retrieve a Job object.
     *
     * @return Job object for the given jobId, or <code>null<code> if job not found with given jobId
     *
     * @apiNote To be used in service calls only
     */
    Job getJob( UUID userId, String jobId );

    /**
     * It gets a workflow job by job id from the database.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the id of job to retrieve a Job object.
     *
     * @return Job object for the given jobId, or <code>null<code> if job not found with given jobId
     */
    Job getJob( EntityManager entityManager, UUID userId, String jobId );

    /**
     * Creates a new workflow job and sets job status to running, returns the created job.
     *
     * @param userId
     *         the requesting userId
     * @param job
     *         the input with details of a job to be created
     *
     * @return created workflow job object with details about the job, or <code>null<code> if operation fails
     *
     * @apiNote To be used in service calls only
     */
    Job createJob( UUID userId, Job job );

    /**
     * Creates a new workflow job and sets job status to running, returns the created job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the requesting userId
     * @param job
     *         the input with details of a job to be created
     *
     * @return created workflow job object with details about the job, or <code>null<code> if operation fails
     */
    Job createJob( EntityManager entityManager, UUID userId, Job job );

    /**
     * Gets the job id by UUID.
     *
     * @param uuid
     *         the uuid
     *
     * @return the job id by UUID
     *
     * @apiNote To be used in service calls only
     */
    Integer getJobIdByUUID( UUID uuid );

    /**
     * Gets the job id by UUID.
     *
     * @param entityManager
     *         the entity manager
     * @param uuid
     *         the uuid
     *
     * @return the job id by UUID
     */
    Integer getJobIdByUUID( EntityManager entityManager, UUID uuid );

    /**
     * Updates a workflow job attributes, and sets job status to finished. It is called at the end of a job. It must support other status,
     * for example failed.
     *
     * @param job
     *         the input with details of a job to be updated
     *
     * @return updated workflow job object with details about the job, or <code>null<code> if operation fails
     *
     * @apiNote To be used in service calls only
     */
    Job updateJob( Job job );

    /**
     * Updates a workflow job attributes, and sets job status to finished. It is called at the end of a job. It must support other status,
     * for example failed.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the input with details of a job to be updated
     *
     * @return updated workflow job object with details about the job, or <code>null<code> if operation fails
     */
    Job updateJob( EntityManager entityManager, Job job );

    /**
     * Gets the the list of all jobs with any status.
     *
     * @param userId
     *         the user id
     *
     * @return the jobs list. If no job is found then an empty list is returned
     *
     * @apiNote To be used in service calls only
     */
    List< Job > getJobsList( UUID userId );

    /**
     * Gets the master running jobs list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the master running jobs list
     */
    List< Job > getAllDOEMasterRunningJobsList( EntityManager entityManager );

    /**
     * Gets the running childs and single jobs list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the running childs and single jobs list
     */
    List< Job > getRunningChildsAndSingleJobsList( EntityManager entityManager );

    /**
     * Updates the job log contents with given job by using its id.
     *
     * @param job
     *         the job object containing job id and new log contents
     *
     * @return the updated job, or <code>null</code> if job is not found by its id.
     *
     * @apiNote To be used in service calls only
     */
    Job updateJobLog( Job job );

    /**
     * Updates the job log contents and progress with given job by using its id.
     *
     * @param job
     *         the job object containing job id and new log contents
     *
     * @return the updated job, or <code>null</code> if job is not found by its id.
     *
     * @apiNote To be used in service calls only
     */
    Job updateJobLogAndProgress( Job job );

    /**
     * Gets the last job directory by work flow.
     *
     * @param workflowId
     *         the workflow_id
     *
     * @return the last job directory by work flow
     *
     * @throws UnknownHostException
     *         the unknown host exception when host not found
     * @apiNote To be used in service calls only
     */
    Map< String, String > getLastJobDirectoryByWorkFlow( String workflowId ) throws UnknownHostException;

    /**
     * Gets the my sus jobs list.
     *
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the my sus jobs list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Job > getMySusJobsList( UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets all values for job table column.
     *
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for job table column
     */
    List< Object > getAllValuesForJobTableColumn( String columnName, String token );

    /**
     * Gets all values for child job table column.
     *
     * @param jobId
     *         the job id
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for child job table column
     */
    List< Object > getAllValuesForChildJobsTableColumn( String masterJobId, String columnName, String token );

    /**
     * Gets user jobs ui.
     *
     * @return the user jobs ui
     */
    TableUI getUserJobsUI();

    /**
     * Gets the list of job UI table columns.
     *
     * @return the list of job UI table columns
     */
    TableUI getListOfJobUITableColumns();

    /**
     * Gets the filtered jobs list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the filtered jobs list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Job > getFilteredJobsList( UUID userId, FiltersDTO filter );

    /**
     * Gets filtered system jobs list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the filtered system jobs list
     */
    FilteredResponse< Job > getFilteredSystemJobsList( UUID userId, FiltersDTO filter );

    /**
     * Gets the filtered child jobs list.
     *
     * @param id
     *         the id
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the filtered child jobs list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Job > getFilteredChildJobsList( UUID id, UUID userId, FiltersDTO filter );

    /**
     * Gets the tabs view job UI.
     *
     * @param jobId
     *         the job id
     *
     * @return the tabs view job UI
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getTabsViewJobUI( String jobId );

    /**
     * Gets the filtered job log list.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param filter
     *         the filter
     *
     * @return the filtered job log list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LogRecord > getFilteredJobLogList( UUID userId, String jobId, FiltersDTO filter );

    /**
     * Gets the job permission UI table.
     *
     * @param userId
     *         the user id
     *
     * @return the job permission UI table
     *
     * @apiNote To be used in service calls only
     */
    TableUI getJobPermissionUITable( UUID userId );

    /**
     * Show permitted users and groups for object.
     *
     * @param jsonToObject
     *         the json to object
     * @param id
     *         the id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the filtered response
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO jsonToObject, UUID id,
            UUID userIdFromGeneralHeader );

    /**
     * Gets the filtered job data created objects list.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param filter
     *         the filter
     *
     * @return the filtered job data created objects list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< GenericDTO > getFilteredJobDataCreatedObjectsList( UUID userId, String jobId, FiltersDTO filter );

    /**
     * Gets the job data created table UI.
     *
     * @return the job data created table UI
     */
    List< TableColumn > getJobDataCreatedTableUI();

    /**
     * Permit permission to job.
     *
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param securityId
     *         the security id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean permitPermissionToJob( CheckBox checkBox, UUID objectId, UUID securityId, String userIdFromGeneralHeader );

    /**
     * Gets the context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     * @param clazz
     *         the clazz
     * @param token
     *         the token
     *
     * @return the context router
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContextRouter( UUID userIdFromGeneralHeader, FiltersDTO filter, Class< ? > clazz, String token );

    /**
     * Gets the data created context menu.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the data created context menu
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getDataCreatedContext( String userIdFromGeneralHeader, FiltersDTO filter );

    /**
     * Stop server work flow.
     *
     * @param jobId
     *         the job id
     * @param userName
     *         the user name
     *
     * @return true, if successful
     */
    boolean stopServerWorkFlow( String jobId, String userName );

    /**
     * Prepare object view DTO.
     *
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param isUpdateable
     *         the is updateable
     * @param viewId
     *         the view id
     *
     * @return the object view DTO
     */
    ObjectViewDTO prepareObjectViewDTO( String objectId, String viewJson, String viewKey, boolean isUpdateable, String viewId );

    /**
     * Gets the job by id.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return the job by id
     */
    JobEntity getJobById( EntityManager entityManager, UUID jobId );

    /**
     * Gets the job data created objects list.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the job data created objects list
     *
     * @apiNote To be used in service calls only
     */
    List< SuSEntity > getJobDataCreatedObjectsList( UUID userId, String jobId );

    /**
     * Gets the job data created objects list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the job data created objects list
     */
    List< SuSEntity > getJobDataCreatedObjectsList( EntityManager entityManager, UUID userId, String jobId );

    /**
     * Check job childern.
     *
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean checkJobChildern( UUID jobId );

    /**
     * Check job childern IDs.
     *
     * @param jobId
     *         the job id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    List< UUID > getJobChildernIDs( UUID jobId );

    /**
     * Process tree children.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param jobId
     *         the job id
     *
     * @return the job tree node DTO
     *
     * @apiNote To be used in service calls only
     */
    JobTreeNodeDTO processTreeChildren( String userIdFromGeneralHeader, UUID jobId );

    /**
     * Gets the job data created objects ids list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param id
     *         the id
     *
     * @return the job data created objects ids list
     *
     * @apiNote To be used in service calls only
     */
    List< String > getCreatedDataObjectsIdsForJobAndChildren( UUID userIdFromGeneralHeader, String id );

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    SuSGenericObjectDAO< JobEntity > getJobEntityDAO();

    /**
     * Gets the single job scheme data.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param filter
     *         the filter
     *
     * @return the single job scheme data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getSingleJobSchemeData( String userId, String id, FiltersDTO filter );

    /**
     * Gets the single job scheme UI.
     *
     * @param id
     *         the id
     *
     * @return the single job scheme UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getSingleJobSchemeUI( String id );

    /**
     * Generate CSV file by job id.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @apiNote To be used in service calls only
     */
    String generateCSVFileByJobId( String userId, String id, String token ) throws IOException;

    /**
     * Generate CSV file by job id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    String generateCSVFileByJobId( EntityManager entityManager, String userId, String id, String token ) throws IOException;

    /**
     * Plot CSV file by python.
     *
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param optionId
     *         the option id
     * @param token
     *         the token
     * @param userUID
     *         the user UID
     *
     * @return the object
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     * @apiNote To be used in service calls only
     */
    Object plotCSVFileByPython( String userId, String id, String optionId, String token, String userUID )
            throws IOException, ParseException;

    /**
     * Generate Image.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the id
     * @param key
     *         the key
     * @param token
     *         the token
     *
     * @return the object
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     * @apiNote To be used in service calls only
     */
    Object generateImage( String userId, String jobId, String key, String token ) throws IOException, ParseException;

    /**
     * Gets the job status by job id.
     *
     * @param id
     *         the id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the job status by job id
     *
     * @apiNote To be used in service calls only
     */
    Object getJobStatusByJobId( String id, UUID userIdFromGeneralHeader );

    /**
     * Gets the job resultsummary by job id.
     *
     * @param id
     *         the id
     *
     * @return the job resultsummary by job id
     *
     * @apiNote To be used in service calls only
     */
    Object getJobResultsummaryByJobId( String id );

    /**
     * Gets the all childrens of master job.
     *
     * @param job
     *         the job
     *
     * @return the all childrens of master job
     *
     * @apiNote To be used in service calls only
     */
    List< Job > getAllChildrensOfMasterJob( Job job );

    /**
     * Gets the all childrens of master job.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     *
     * @return the all childrens of master job
     */
    List< Job > getAllChildrensOfMasterJob( EntityManager entityManager, Job job );

    /**
     * Gets user jobs list.
     *
     * @param token
     *         the token
     * @param filters
     *         the filters
     *
     * @return the user jobs list
     */
    FilteredResponse< UserJobDTO > getUserJobsList( String token, FiltersDTO filters );

    /**
     * Gets the workflow manager.
     *
     * @return the workflow manager
     */
    WorkflowManager getWorkflowManager();

    /**
     * Rerun job.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return the latest work flow DTO
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO rerunJob( String userId, String jobId );

    /**
     * Gets global variables from job id and user id.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     * @param userId
     *         the user id
     *
     * @return the global variables from job id and user id
     */
    Map< String, Object > getGlobalVariablesFromJobIdAndUserId( EntityManager entityManager, UUID jobId, UUID userId );

    /**
     * Discard job.
     *
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param jobId
     *         the job id
     *
     * @return the response
     *
     * @apiNote To be used in service calls only
     */
    Response discardJob( String userId, String token, String jobId );

    /**
     * Update job status to complete.
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     * @param status
     *         the status
     *
     * @return the response
     *
     * @apiNote To be used in service calls only
     */
    Response updateJobStatus( UUID userId, String token, String jobId, String status );

    /**
     * Prepare job scheme as post json job scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param jobImpl
     *         the jobImpl
     *
     * @return the JobScheme
     *
     * @throws IOException
     *         the io exception
     */
    JobScheme prepareJobSchemeAsPostJson( EntityManager entityManager, Job jobImpl ) throws IOException;

    /**
     * Gets the list of child job UI table columns.
     *
     * @return the list of child job UI table columns
     */
    TableUI getListOfChildJobUITableColumns();

    /**
     * Checks if is host enabled.
     *
     * @param id
     *         the id
     *
     * @return the boolean
     */
    ExecutionHosts isHostEnabled( UUID id );

    /**
     * Gets the job dao.
     *
     * @return the job dao
     */
    JobDAO getJobDao();

    /**
     * Generate job logs zip file.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the string
     */
    String generateJobLogsZipFile( String id, String token );

    /**
     * Save job ids.
     *
     * @param uuid
     *         the uuid
     *
     * @return the integer
     *
     * @apiNote To be used in service calls only
     */
    Integer saveJobIds( UUID uuid );

    /**
     * Save job ids.
     *
     * @param entityManager
     *         the entity manager
     * @param uuid
     *         the uuid
     *
     * @return the integer
     */
    Integer saveJobIds( EntityManager entityManager, UUID uuid );

    /**
     * Save job id if not exist integer.
     *
     * @param entityManager
     *         the entity manager
     * @param uuid
     *         the uuid
     *
     * @return the integer
     */
    Integer saveJobIdIfNotExist( EntityManager entityManager, UUID uuid );

    /**
     * Generate and copy job summary CSV by job id.
     *
     * @param userId
     *         the user id
     * @param userUID
     *         the user UID
     * @param fromJobId
     *         the from job id
     * @param toJobId
     *         the to job id
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @apiNote To be used in service calls only
     */
    String generateAndCopyJobSummaryCSVByJobId( String userId, String userUID, String fromJobId, String toJobId ) throws IOException;

    /**
     * Gets filtered workflow related jobs list.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filters
     *         the filters
     * @param workflowId
     *         the workflow id
     *
     * @return the filtered workflow related jobs list
     *
     * @apiNote To be used in service calls only
     */
    Object getFilteredWorkflowRelatedJobsList( UUID userIdFromGeneralHeader, FiltersDTO filters, String workflowId );

    /**
     * Gets objective variable dao.
     *
     * @return the objective variable dao
     */
    VariableDAO getVariableDAO();

    /**
     * returns all input parameters of job
     *
     * @param userId
     *         the user id
     * @param jobId
     *         the job id
     *
     * @return List of {@link UIFormItem}
     *
     * @apiNote To be used in service calls only
     */
    UIForm getJobInputParameters( UUID userId, String jobId );

}