package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.cxf.message.Message;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.model.JobTreeNodeDTO;
import de.soco.software.simuspace.server.service.rest.BaseService;
import de.soco.software.simuspace.server.service.rest.JobService;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.workflow.dto.JobDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;

/**
 * This class provides services for all the create/update/delete/get operations Of jobs etc.
 *
 * @author Nosheen.Sharif
 */
public class JobServiceImpl extends BaseService implements JobService {

    /**
     * The Constant CHECK_BOX_STATE.
     */
    private static final int CHECK_BOX_STATE = 15;

    /**
     * The Constant PERMISSION_LEVEL.
     */
    private static final String PERMISSION_LEVEL = "level";

    /**
     * The job manager reference.
     */
    private JobManager jobManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listJobTableUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.JOBS.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.TABLE.getKey() ) ), new TableUI( jobManager.getListOfJobUITableColumns().getColumns(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredWorkflowRelatedJobsList( String workflowId, String objectFilterJson ) {
        try {
            final FiltersDTO filters = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    jobManager.getFilteredWorkflowRelatedJobsList( getUserIdFromGeneralHeader(), filters, workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listJobWorkflowTableUI() {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getListOfJobUITableColumns().getColumns(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listChildJobTableUI() {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getListOfChildJobUITableColumns().getColumns(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response isHost( UUID id ) {
        try {
            return ResponseUtils.success( jobManager.isHostEnabled( id ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredJobsList( String objectFilterJson ) {
        try {
            final FiltersDTO filters = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.JOBS.getKey() ) ),
                    jobManager.getFilteredJobsList( getUserIdFromGeneralHeader(), filters ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listSystemJobTableUI() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SYSTEM_JOBS.getKey() ) + ConstantsString.SPACE
                                    + MessageBundleFactory.getMessage( Messages.TABLE.getKey() ) ),
                    new TableUI( jobManager.getListOfJobUITableColumns().getColumns(),
                            objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SYSTEM_JOBS_TABLE_KEY,
                                    getUserIdStringFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredSystemJobsList( String objectFilterJson ) {
        try {
            final FiltersDTO filters = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SYSTEM_JOBS.getKey() ) ),
                    jobManager.getFilteredSystemJobsList( getUserIdFromGeneralHeader(), filters ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredChildJobsList( UUID id, String objectFilterJson ) {
        try {
            final FiltersDTO filters = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success( jobManager.getFilteredChildJobsList( id, getUserIdFromGeneralHeader(), filters ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTabsViewJobUI( String jobId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.TABS_VIEW_JOB.getKey() ) ), jobManager.getTabsViewJobUI( jobId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSingleJobPropertiesUI( String id ) {
        try {
            return ResponseUtils.success( GUIUtils.listColumns( JobImpl.class ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobLogUI( String id, String treeId ) {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( LogRecord.class ),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_LOG_KEY, getUserIdStringFromGeneralHeader(),
                            null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobDataCreatedJobUI( String id ) {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getJobDataCreatedTableUI(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOB_DATA_CREATED_TABLE_KEY,
                            getUserIdStringFromGeneralHeader(), id ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredJobLogsList( String id, String treeId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( jobManager.getFilteredJobLogList( getUserIdFromGeneralHeader(), treeId,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredJobDataCreatedObjectsList( String id, String objectFilterJson ) {
        try {
            return ResponseUtils.success( jobManager.getFilteredJobDataCreatedObjectsList( getUserIdFromGeneralHeader(), id,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJob( UUID jobId ) {
        try {
            final Job job = jobManager.getJob( getUserIdFromGeneralHeader(), jobId.toString() );

            return ResponseUtils.success( job, JobImpl.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobPermissionTableUI( String id ) {
        try {
            return ResponseUtils.success( jobManager.getJobPermissionUITable( getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response showPermittedUsersAndGroupsForJob( UUID id, String objectFilterJson ) {
        try {
            return ResponseUtils.success(
                    jobManager.showPermittedUsersAndGroupsForObject( JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), id,
                            getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveView( String viewJson ) {
        return saveJobView( viewJson, ConstantsObjectViewKey.JOBS_TABLE_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemJobViews() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SYSTEM_JOBS_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveSystemJobView( String viewJson ) {
        return saveJobView( viewJson, ConstantsObjectViewKey.SYSTEM_JOBS_TABLE_KEY );
    }

    /**
     * Update view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateSystemJobView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.SYSTEM_JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteSystemJobView( String viewId ) {
        return deleteView( viewId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setSystemJobsViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.SYSTEM_JOBS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permitPermissionToJob( String permissionJson, UUID objectId, UUID sidId ) {
        boolean isPermitted;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( permissionJson, checkBoxStateMap );
            CheckBox checkBoxState = null;
            for ( final Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                if ( entry.getKey().equals( PERMISSION_LEVEL ) ) {
                    final int valueOfLevel = PermissionMatrixEnum.getKeyByValue( entry.getValue().toString() );
                    checkBoxState = new CheckBox( null, entry.getKey(), valueOfLevel );

                } else {
                    checkBoxState = new CheckBox( null, entry.getKey().substring( CHECK_BOX_STATE ),
                            Integer.parseInt( entry.getValue().toString() ) );
                }
            }
            isPermitted = jobManager.permitPermissionToJob( checkBoxState, objectId, sidId, getUserIdStringFromGeneralHeader() );
            if ( isPermitted ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PERMISSION_APPLIED_SUCCESSFULLY.getKey() ),
                        isPermitted );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_SUCCESSFULLY.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveSuSJobsView( String viewJson ) {
        return saveJobView( viewJson, ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY );
    }

    /**
     * Update su S jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.user.service.rest.DirectoryService#updateLicenseView(java.lang.String)
     */
    @Override
    public Response updateSuSJobsView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Update view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteSusJobsView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSuSJobsViews() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setSusJobsViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.JOBS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * ******************** Getters/Setters ***********************.
     */

    /**
     * @return the jobManager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the jobManager to set
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Gets the object view manager.
     *
     * @return the objectViewManager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the objectViewManager to set
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * Gets the jobs context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the jobs context
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.server.service.rest.JobService#getJobsContext(java.lang.String)
     */
    @Override
    public Response getJobsContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ),
                    ContextUtil.allOrderedContext( jobManager.getContextRouter( getUserIdFromGeneralHeader(), filter, JobDTO.class,
                            getTokenFromGeneralHeader() ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getSusJobsContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    jobManager.getContextRouter( getUserIdFromGeneralHeader(), filter, JobDTO.class, getTokenFromGeneralHeader() ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the jobs data created context.
     *
     * @param filterJson
     *         the filter json
     *
     * @return the jobs data created context
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.server.service.rest.JobService#getJobsContext(java.lang.String)
     */
    @Override
    public Response getJobsDataCreatedContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( jobManager.getDataCreatedContext( getUserIdFromGeneralHeader().toString(), filter ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Check job childern.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @Override
    public Response checkJobChildern( UUID jobId ) {
        try {
            return ResponseUtils.success( jobManager.checkJobChildern( jobId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Get job childern IDs.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @Override
    public Response getChildJobIDsByMasterJobID( String jobId ) {
        try {
            return ResponseUtils.success( jobManager.getJobChildernIDs( UUID.fromString( jobId ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getMySusJobsList( String objectFilterJson ) {
        try {

            return ResponseUtils.success( jobManager.getMySusJobsList( getUserIdFromGeneralHeader(),
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listSusJobTableUI() {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getListOfJobUITableColumns().getColumns(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY,
                            getUserIdStringFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLogViews( String id ) {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOB_LOG_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateLogView( String id, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.JOB_LOG_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteLogView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setLogViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.JOB_LOG_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPermissionViews( String id ) {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.PERMISSION_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response savePermissionView( String id, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.PERMISSION_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updatePermissionView( String id, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.PERMISSION_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deletePermissionView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setPermissionViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataCreatedViews( String id ) {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOB_DATA_CREATED_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDataCreatedView( String id, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.JOB_DATA_CREATED_TABLE_KEY, false,
                                    null ), getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDataCreatedView( String id, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.JOB_DATA_CREATED_TABLE_KEY, true,
                                    viewId ), getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDataCreatedView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setDataCreatedViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.JOB_DATA_CREATED_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the job data created objects list.
     *
     * @param id
     *         the id
     *
     * @return the job data created objects list
     */
    @Override
    public Response getJobDataCreatedObjectsList( String id ) {
        try {
            return ResponseUtils.success( jobManager.getJobDataCreatedObjectsList( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the job data created objects ids list.
     *
     * @param id
     *         the id
     *
     * @return the job data created objects ids list
     */
    @Override
    public Response getJobDataCreatedObjectsIdsList( String id ) {
        try {
            return ResponseUtils.success( jobManager.getCreatedDataObjectsIdsForJobAndChildren( getUserIdFromGeneralHeader(), id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response processTreeChildren( String id ) {
        try {
            JobTreeNodeDTO returnModel = jobManager.processTreeChildren( getUserIdStringFromGeneralHeader(), UUID.fromString( id ) );
            return ResponseUtils.success( Collections.singletonList( returnModel ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the file jobs views.
     *
     * @return the file jobs views
     */
    @Override
    public Response getFileJobsViews() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.FILE_JOBS_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Save file jobs view.
     *
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response saveFileJobsView( String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( objectViewDTO.getId() == null ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.FILE_JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Update file jobs view.
     *
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateFileJobsView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.FILE_JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Delete file jobs view.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response deleteFileJobsView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Sets the file jobs view as default.
     *
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response setFileJobsViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.FILE_JOBS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the jobs log views.
     *
     * @return the jobs log views
     */
    @Override
    public Response getJobsLogViews() {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_LOG_KEY, getUserIdStringFromGeneralHeader(),
                            null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveLogView( String id, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.JOBS_LOG_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteJobLogView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobLogView( String childJobId, String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.JOBS_LOG_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setJobLogViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.JOBS_LOG_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSingleJobSchemeUI( String id ) {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getSingleJobSchemeUI( id ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getMasterJobSchemeTabContext( String jobId, String filterJson ) {
        try {
            return ResponseUtils.success( new ArrayList<>() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getMasterJobChildsTabContext( String jobId, String filterJson ) {
        try {
            return ResponseUtils.success( new ArrayList<>() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the single job scheme data.
     *
     * @param id
     *         the id
     * @param objectFilterJson
     *         the object filter json
     *
     * @return the single job scheme data
     */
    @Override
    public Response getSingleJobSchemeData( String id, String objectFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success( jobManager.getSingleJobSchemeData( getUserIdStringFromGeneralHeader(), id, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Generate CSV file for job scheme data.
     *
     * @param id
     *         the id
     * @param token
     *         the token
     *
     * @return the response
     */
    @Override
    public Response genrateAndDownloadCSVFileforJobSchemeData( String id ) {
        try {
            String fileAddress = jobManager.generateCSVFileByJobId( CommonUtils.getCurrentUser( getTokenFromGeneralHeader() ).getId(), id,
                    getTokenFromGeneralHeader() );
            File file = new File( fileAddress );
            ResponseBuilder response = Response.ok( file );
            response.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            response.header( "File-Size", file.length() );
            return response.build();
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response genrateAndCopyCSVFileforJobSchemeData( String fromJobId, String toJobId ) {
        try {
            return ResponseUtils.success(
                    jobManager.generateAndCopyJobSummaryCSVByJobId( getUserIdStringFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            fromJobId, toJobId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * Plot CSV file by python.
     *
     * @param id
     *         the id
     * @param optionId
     *         the option id
     *
     * @return the response
     */
    @Override
    public Response plotCSVFileByPython( String id, String optionId ) {
        try {
            return ResponseUtils.success(
                    jobManager.plotCSVFileByPython( getUserIdStringFromGeneralHeader(), id, optionId, getTokenFromGeneralHeader(),
                            getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Generate Image.
     *
     * @param id
     *         the id
     * @param key
     *         the option
     *
     * @return the response
     */
    @Override
    public Response generateImage( String id, String key ) {
        try {
            return ResponseUtils.success(
                    jobManager.generateImage( getUserIdStringFromGeneralHeader(), id, key, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the job status by job id.
     *
     * @param id
     *         the id
     *
     * @return the job status by job id
     */
    @Override
    public Response getJobStatusByJobId( String id ) {
        try {
            return ResponseUtils.success( jobManager.getJobStatusByJobId( id, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the job resultsummary by job id.
     *
     * @param id
     *         the id
     *
     * @return the job resultsummary by job id
     */
    @Override
    public Response getJobResultsummaryByJobId( String id ) {
        try {
            return ResponseUtils.success( jobManager.getJobResultsummaryByJobId( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Rerun jobs.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @Override
    public Response rerunJobs( String jobId ) {
        try {

            List< UUID > child = jobManager.getJobChildernIDs( UUID.fromString( jobId ) );
            if ( child != null && !child.isEmpty() ) {
                return ResponseUtils.success( "Master job is not allowed to rerun.", true );
            } else {
                return ResponseUtils.success( jobManager.rerunJob( getUserIdFromGeneralHeader().toString(), jobId ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Discard jobs.
     *
     * @param jobId
     *         the job id
     *
     * @return the response
     */
    @Override
    public Response discardJobs( String jobId ) {
        try {
            return ResponseUtils.success(
                    jobManager.discardJob( getUserIdFromGeneralHeader().toString(), getTokenFromGeneralHeader(), jobId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobStatus( String jobId, String status ) {
        try {
            return ResponseUtils.success(
                    jobManager.updateJobStatus( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), jobId, status ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getChidlsJobView( String id ) {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.CHILDS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveChidlsJobView( String id, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.CHILDS_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateChidlsJobView( String id, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.CHILDS_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteChidlsJobView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setChidlsJobViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.CHILDS_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadJobLogsZip( String id ) {
        try {

            String fileAddress = jobManager.generateJobLogsZipFile( id, getTokenFromGeneralHeader() );
            File file = new File( fileAddress );
            ResponseBuilder responseBuilder = Response.ok( file );
            responseBuilder.header( "Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" );
            responseBuilder.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            responseBuilder.header( "File-Size", file.length() );
            return responseBuilder.build();

        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, JobServiceImpl.class );
            throw e;
            //return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSusJobsList( String objectJson ) {
        try {
            return ResponseUtils.success(
                    jobManager.getMySusJobsList( getUserIdFromGeneralHeader(), JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSuSJobsView() {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY,
                    getUserIdStringFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateSuSJobsView( String viewJson ) {
        return saveJobView( viewJson, ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateSuSJobView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.SUS_JOBS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteSusJobView( String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowJobsContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    jobManager.getContextRouter( getUserIdFromGeneralHeader(), filter, JobDTO.class, getTokenFromGeneralHeader() ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowJobsViews( String id ) {
        try {
            return ResponseUtils.success(
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.JOBS_TABLE_KEY, getUserIdStringFromGeneralHeader(),
                            id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWorkflowJobsView( String id, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.JOBS_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflowJobsView( String id, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            jobManager.prepareObjectViewDTO( id, objectJson, ConstantsObjectViewKey.JOBS_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWorkflowJobsView( String id, String viewId ) {
        try {
            if ( objectViewManager.deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setWorkflowJobsViewAsDefault( String id, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdStringFromGeneralHeader(),
                            ConstantsObjectViewKey.JOBS_TABLE_KEY, id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobInputParameters( String id ) {
        try {
            return ResponseUtils.success( jobManager.getJobInputParameters( getUserIdFromGeneralHeader(), id ) );
        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }

    }

    @Override
    public Response getAllValuesForJobTableColumn( String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    jobManager.getAllValuesForJobTableColumn( column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserJobsList( String objectFilterJson ) {
        try {
            final FiltersDTO filters = JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class );
            return ResponseUtils.success( jobManager.getUserJobsList( getTokenFromGeneralHeader(), filters ) );
        } catch ( Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getAllValuesForChildJobsTableColumn( String id, String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    jobManager.getAllValuesForChildJobsTableColumn( id, column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserJobsUI() {
        try {
            return ResponseUtils.success( new TableUI( jobManager.getUserJobsUI().getColumns(), null ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    private Response saveJobView( String viewJson, String jobsTableKey ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( jobsTableKey );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView( objectViewDTO, getUserIdStringFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

}
