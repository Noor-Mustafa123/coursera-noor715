package de.soco.software.simuspace.server.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.server.constant.ConstantsActionsType;
import de.soco.software.simuspace.server.manager.CategoryManager;
import de.soco.software.simuspace.server.manager.FavoriteManager;
import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.LicenseTokenManager;
import de.soco.software.simuspace.server.manager.WorkflowCategoryManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.service.rest.BaseService;
import de.soco.software.simuspace.server.service.rest.WorkflowService;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.model.MoveDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.UserLicenseDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.model.Category;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.CategoryImpl;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.suscore.jsonschema.model.PostProcess;

/**
 * This class provides services for all the create/update/delete/get operations Of workflows and jobs etc.
 *
 * @author Nosheen.Sharif
 */
public class WorkflowServiceImpl extends BaseService implements WorkflowService {

    /**
     * The job manager reference.
     */
    private JobManager jobManager;

    /**
     * The category Manager reference.
     */
    private CategoryManager categoryManager;

    /**
     * The workflow manager reference.
     */
    private WorkflowManager workflowManager;

    /**
     * The workflow category manager reference.
     */
    private WorkflowCategoryManager workflowCategoryManager;

    /**
     * The license token manager.
     */
    private LicenseTokenManager licenseTokenManager;

    /**
     * The favorite manager.
     */
    private FavoriteManager favoriteManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * Instantiates a new workflow service impl.
     */
    public WorkflowServiceImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createJob( String jobJson ) {

        try {
            Job job = null;
            final JobImpl jobImpl = JsonUtils.jsonToObject( jobJson, JobImpl.class );
            jobImpl.setCreatedBy( new UserDTO( ConstantsString.EMPTY_STRING + getUserIdFromGeneralHeader() ) );
            if ( jobImpl.validateSaveJob() ) {
                job = jobManager.createJob( getUserIdFromGeneralHeader(), jobImpl );
            }
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_CREATED ), job );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobIdByUUID( UUID uuid ) {
        try {
            return ResponseUtils.success( jobManager.getJobIdByUUID( uuid ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveJobIds( UUID uuid ) {
        try {
            return ResponseUtils.success( jobManager.saveJobIds( uuid ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWorkflow( String parentId, String workflowJson ) {
        try {
            final LatestWorkFlowDTO dto = JsonUtils.jsonToObject( workflowJson, LatestWorkFlowDTO.class );
            if ( workflowManager.validateWorkflow( dto, false ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WORKFLOW_SAVED_SUCCESSFULLY.getKey() ),
                        workflowManager.saveWorkflow( getUserIdFromGeneralHeader(), parentId, dto, workflowJson ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_SAVE_WORKFLOW.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWorkflowAsNew( String parentId, String workflowJson ) {
        try {
            final LatestWorkFlowDTO dto = JsonUtils.jsonToObject( workflowJson, LatestWorkFlowDTO.class );
            if ( workflowManager.validateWorkflow( dto, false ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WORKFLOW_SAVED_SUCCESSFULLY.getKey() ),
                        workflowManager.saveWorkflowAsNew( getUserIdFromGeneralHeader(), parentId, dto, workflowJson ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_SAVE_WORKFLOW.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJob( String jobId ) {
        try {
            final Job job = jobManager.getJob( getUserIdFromGeneralHeader(), jobId );
            return ResponseUtils.success( job, JobImpl.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowById( String workflowId ) {
        try {
            final LatestWorkFlowDTO workflow = workflowManager.getNewWorkflowById( getUserIdFromGeneralHeader(), workflowId );
            return ResponseUtils.success( workflow );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCustomFlagList() {
        try {
            return ResponseUtils.success( workflowManager.getCustomFlagList() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCustomFlagUI( String plugins, UriInfo uriInfo ) {
        try {
            return ResponseUtils.success( workflowManager.getCustomFlagUI( plugins, uriInfo ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getShapeModuleRunTabs( String plugins ) {
        try {
            return ResponseUtils.success( workflowManager.getShapeModuleRunTabs( plugins ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCustomFlagUIForRerun( String rerunJobId, String plugins, UriInfo uriInfo ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getCustomFlagUIForRerun( getUserIdFromGeneralHeader(), rerunJobId, plugins, uriInfo ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getCustomFlagPluginUI( String plugins, String value, UriInfo uriInfo ) {
        try {
            return ResponseUtils.success( workflowManager.getCustomFlagPluginUI( plugins, value, uriInfo ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getCustomFlagPluginUIForRerun( String rerunJobId, String plugins, String value, UriInfo uriInfo ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getCustomFlagPluginUIForRerun( getUserIdFromGeneralHeader(), rerunJobId, plugins, value, uriInfo ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowBySelectionId( String selectionId ) {
        try {
            final LatestWorkFlowDTO workflow = workflowManager.getWorkflowBySelectionId( getUserIdFromGeneralHeader(), selectionId );

            return ResponseUtils.success( workflow );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowByIdWithRefJobParams( String workflowId, String jobId ) {
        try {
            final Job job = jobManager.getJob( getUserIdFromGeneralHeader(), jobId );
            final WorkflowDTO workflow = workflowManager.getWorkflowByIdWithRefJobParams( getUserIdFromGeneralHeader(), workflowId, job );

            return ResponseUtils.success( workflow );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowVersionsById( String workflowId ) {
        try {
            final List< LatestWorkFlowDTO > workflow = workflowManager.getWorkflowVersionsById( getUserIdFromGeneralHeader(), workflowId );
            return ResponseUtils.success( workflow );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowList() {
        try {
            final List< WorkflowDTO > workflowslist = workflowManager.getWorkflowList( getUserIdFromGeneralHeader() );
            return ResponseUtils.success( workflowslist, WorkflowDTO.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the filtered workflow list.
     *
     * @param userFilterJson
     *         the user filter json
     *
     * @return the filtered workflow list
     */
    @Override
    public Response getFilteredWorkflowList( String userFilterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( userFilterJson, FiltersDTO.class );
            return ResponseUtils.success( workflowManager.getFilteredWorkflowList( getUserIdFromGeneralHeader(), filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredWorkflowUI() {
        try {
            return ResponseUtils.success( new TableUI( workflowManager.getListOfWorkFlowDTOUITableColumns(),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.WORKFLOW_TABLE_KEY,
                            getUserIdStringFromGeneralHeader(), null ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getToken() {
        try {
            return ResponseUtils.success( workflowManager.getToken( PhaseInterceptorChain.getCurrentMessage(), getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the workflow manager.
     *
     * @return the workflow manager
     */
    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response loadConfig() {
        try {
            final JsonNode config = workflowManager.getConfig();
            return ResponseUtils.success( config );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcConfig() {
        try {
            final JsonNode config = workflowManager.getHpcConfig();
            return ResponseUtils.success( config );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getEmailConfig() {
        try {
            return ResponseUtils.success( workflowManager.getEmailConfig() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJob( UUID jobId, String jobJson ) {
        try {
            Job result = null;
            final JobImpl jobImpl = JsonUtils.jsonToObject( jobJson, JobImpl.class );
            jobImpl.setId( jobId );
            if ( jobImpl.validateUpdateJob() ) {
                result = jobManager.updateJob( jobImpl );
            }
            if ( result != null ) {
                return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_UPDATED ), result );
            } else {
                return ResponseUtils.failure( MessagesUtil.getMessage( WFEMessages.JOB_NOT_UPDATED ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCategoryList() {
        try {
            final List< Category > categoriesList = categoryManager.getCategories();
            return ResponseUtils.success( categoriesList );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobsList() {
        try {
            final List< Job > jobList = jobManager.getJobsList( getUserIdFromGeneralHeader() );
            return ResponseUtils.success( jobList, JobImpl.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobLog( UUID jobId, String jobJson ) {
        try {
            final JobImpl jobImpl = JsonUtils.jsonToObject( jobJson, JobImpl.class );
            jobImpl.setId( jobId );
            final Job result = jobManager.updateJobLog( jobImpl );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_UPDATED ), result );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateJobLogAndProgress( UUID jobId, String jobJson ) {
        try {
            final JobImpl jobImpl = JsonUtils.jsonToObject( jobJson, JobImpl.class );
            jobImpl.setId( jobId );
            final Job result = jobManager.updateJobLogAndProgress( jobImpl );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_UPDATED ), result );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Sets the workflow manager.
     *
     * @param workflowManager
     *         the new workflow manager
     */
    public void setWorkflowManager( WorkflowManager workflowManager ) {
        this.workflowManager = workflowManager;
    }

    /**
     * Gets the category manager.
     *
     * @return the category manager
     */
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    /**
     * Sets the category manager.
     *
     * @param categoryManager
     *         the new category manager
     */
    public void setCategoryManager( CategoryManager categoryManager ) {
        this.categoryManager = categoryManager;
    }

    /**
     * Gets the workflow category manager.
     *
     * @return the workflow category manager
     */
    public WorkflowCategoryManager getWorkflowCategoryManager() {
        return workflowCategoryManager;
    }

    /**
     * Sets the workflow category manager.
     *
     * @param workflowCategoryManager
     *         the new workflow category manager
     */
    public void setWorkflowCategoryManager( WorkflowCategoryManager workflowCategoryManager ) {
        this.workflowCategoryManager = workflowCategoryManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflow( String workflowId, String workflowJson ) {
        try {
            final LatestWorkFlowDTO dto = JsonUtils.jsonToObject( workflowJson, LatestWorkFlowDTO.class );
            if ( workflowManager.validateWorkflow( dto, false ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WORKFLOW_UPDATED_SUCCESSFULLY.getKey() ),
                        workflowManager.updateWorkflow( getUserIdFromGeneralHeader(), workflowId, dto, workflowJson ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_UPDATE_WORKFLOW.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowByIdAndVersionId( String workflowId, int versionId ) {
        try {
            final LatestWorkFlowDTO workflow = workflowManager.getNewWorkflowByIdAndVersionId( getUserIdFromGeneralHeader(), workflowId,
                    versionId );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + MessageBundleFactory.getMessage(
                            Messages.VERSION_ID.getKey() ) + ConstantsString.COLON + versionId ), workflow );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createCategory( String categoryJson ) {
        try {
            final CategoryImpl catImp = JsonUtils.jsonToObject( categoryJson, CategoryImpl.class );
            final Category category = categoryManager.addCategory( getUserIdFromGeneralHeader(), catImp );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.CATEGORY_SAVED_SUCCESSFULLY ), category );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateCategory( String categoryId, String categoryJson ) {
        try {
            final CategoryImpl catImp = JsonUtils.jsonToObject( categoryJson, CategoryImpl.class );
            catImp.setId( categoryId );
            final Category category = categoryManager.updateCategory( getUserIdFromGeneralHeader(), catImp );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.CATEGORY_UPDATED_SUCCESSFULLY ), category );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteCategory( String categoryId ) {
        try {
            final Category category = categoryManager.getCategoryById( categoryId );
            final boolean isDeleted = categoryManager.deleteCategory( getUserIdFromGeneralHeader(), categoryId );
            if ( isDeleted ) {
                return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.CATEGORY_DELETED_SUCCESSFULLY ), category );
            } else {
                return ResponseUtils.failure( MessagesUtil.getMessage( WFEMessages.RECORD_NOT_DELETED ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowListByCategoryId( String categoryId ) {
        try {
            final List< WorkflowDTO > workflow = workflowManager.getWorkflowListByCategoryId( getUserIdFromGeneralHeader(), categoryId );
            return ResponseUtils.success( workflow, WorkflowDTO.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response assignCategoriesToWorkflow( String workflowId, String listOfCategoryIds ) {
        try {
            Map< String, List< String > > map = new HashMap<>();
            map = ( HashMap< String, List< String > > ) JsonUtils.jsonToMap( listOfCategoryIds, map );
            final boolean assigned = workflowCategoryManager.assignCategoriesToWorkflow( getUserIdFromGeneralHeader(), workflowId,
                    map.get( "categories" ) );
            return ResponseUtils.success( assigned );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCategoryListByWorkflowId( String workflowId ) {
        try {
            final List< Category > categorys = categoryManager.getCategoryListByWorkflowId( workflowId );
            return ResponseUtils.success( categorys );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLastJobDirectoryByWorkFlow( String workflowId ) {
        try {
            return ResponseUtils.success( jobManager.getLastJobDirectoryByWorkFlow( workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflowAction( String workflowId, int versionId, String actionType, int actionId ) {
        try {
            if ( actionType.equals( ConstantsActionsType.STATUS ) ) {
                final boolean result = workflowManager.updateWorkflowStatus( getUserIdFromGeneralHeader(), workflowId, versionId,
                        actionId );
                return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.WORKFLOW_STATUS_CHANGED, actionId ), result );
            } else {
                // if other action in future
                return ResponseUtils.failure( null );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response checkInLicense() {
        try {
            return ResponseUtils.success( licenseTokenManager.checkInToken( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowVersionsWithoutDefinition( String id ) {
        try {
            final List< WorkflowDTO > result = workflowManager.getWorkflowVersionsWithoutDefinition( getUserIdFromGeneralHeader(), id );
            return ResponseUtils.success( result, WorkflowDTO.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the license token manager.
     *
     * @return the license token manager
     */
    public LicenseTokenManager getLicenseTokenManager() {
        return licenseTokenManager;
    }

    /**
     * Sets the license token manager.
     *
     * @param licenseTokenManager
     *         the new license token manager
     */
    public void setLicenseTokenManager( LicenseTokenManager licenseTokenManager ) {
        this.licenseTokenManager = licenseTokenManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLicenseConsumersList() {
        try {
            final List< UserLicenseDTO > userList = workflowManager.getLicenseConsumersList( getUserIdFromGeneralHeader() );
            return ResponseUtils.success( userList );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response clearToken( String json ) {
        try {
            final UserLicenseDTO userLicenseDTO = JsonUtils.jsonToObject( json, UserLicenseDTO.class );
            workflowManager.clearToken( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), userLicenseDTO );
            return ResponseUtils.success( WFEMessages.TOKEN_CLEARED_SUCCESSFULLY.getValue() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response checkoutToken() {
        try {
            final UserLicenseDTO userLicenseDTO = workflowManager.checkoutToken( getUserIdFromGeneralHeader(),
                    getUserNameFromGeneralHeader(), getTokenFromGeneralHeader() );
            return ResponseUtils.success( userLicenseDTO );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public Response addWorkflowToFavorite( String workflowId ) {
        try {
            final WorkflowDTO workflowDTO = favoriteManager.addWorkflowToFavorite( getUserIdFromGeneralHeader(), workflowId );
            if ( workflowDTO.isFavorite() ) {
                return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ADDED_TO_FAVORITES ), workflowDTO );
            } else {
                return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.WORKFLOW_REMOVED_FROM_FAVORITES ), workflowDTO );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFavoriteWorkFlowList() {
        try {
            return ResponseUtils.success( favoriteManager.getFavoriteWorkflowListByUserId( getUserIdFromGeneralHeader() ),
                    WorkflowDTO.NOT_REQUIRED_PROP );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response verifyLicenseCheckout() {
        try {
            final UserLicenseDTO userLicenseDTO = workflowManager.verifyLicenseCheckout( getTokenFromGeneralHeader() );
            return ResponseUtils.success( userLicenseDTO );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets the favorite manager.
     *
     * @return the favorite manager.
     */
    public FavoriteManager getFavoriteManager() {
        return favoriteManager;
    }

    /**
     * Sets the favorite manager.
     *
     * @param favoriteManager
     *         the new favorite manager
     */
    public void setFavoriteManager( FavoriteManager favoriteManager ) {
        this.favoriteManager = favoriteManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWorkflowForm( String parentId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) ),
                    workflowManager.createWorkflowForm( getUserIdFromGeneralHeader(), parentId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editWorkflowForm( String parentId ) {
        try {
            return ResponseUtils.success( workflowManager.editWorkflowForm( getUserIdFromGeneralHeader(), parentId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTabsViewWorkflowUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.SUBTABS.getKey() ) ),
                    workflowManager.getTabsViewWorkflowUI( objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowVersionsUI( String objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.VERSIONS.getKey() ) ), new TableUI( workflowManager.getWorkflowVersionsUI( objectId ),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY,
                            getUserIdFromGeneralHeader() + ConstantsString.EMPTY_STRING, objectId ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response workflowPermissionTableUI( UUID objectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.PERMISSIONS.getKey() ) ), new TableUI( workflowManager.workflowPermissionTableUI(),
                    workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), objectId.toString(),
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response showPermittedUsersAndGroupsForWorkflow( UUID objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.PERMISSIONS.getKey() ) ),
                    workflowManager.showPermittedUsersAndGroupsForObject( JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ),
                            objectId, getUserIdFromGeneralHeader() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permitPermissionToWorkFlow( String permissionJson, UUID workFlowId, UUID sidId ) {
        boolean isPermitted;
        try {
            Map< String, Object > checkBoxStateMap = new HashMap<>();
            checkBoxStateMap = ( Map< String, Object > ) JsonUtils.jsonToMap( permissionJson, checkBoxStateMap );
            CheckBox checkBoxState = null;
            for ( final Entry< String, Object > entry : checkBoxStateMap.entrySet() ) {
                if ( entry.getKey().equals( "level" ) ) {
                    final int valueOfLevel = PermissionMatrixEnum.getKeyByValue( entry.getValue().toString() );
                    checkBoxState = new CheckBox( null, entry.getKey(), valueOfLevel );
                } else {
                    checkBoxState = new CheckBox( null, entry.getKey().substring( 15 ), Integer.parseInt( entry.getValue().toString() ) );
                }
            }
            isPermitted = workflowManager.permitPermissionToWorkFlow( checkBoxState, workFlowId, sidId,
                    getUserIdFromGeneralHeader().toString() );
            if ( isPermitted ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PERMISSION_APPLIED_SUCCESSFULLY.getKey() ),
                        "Permission applied successfully" );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_SUCCESSFULLY.getKey(),
                        "Permission not applied successfully" ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredWorkflowVersionsList( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.VERSIONS.getKey() ) ),
                    workflowManager.getWorkflowVersions( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllWorkflows( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.WORKFLOWS_LIST_FETCHED_SUCCESSFULLY.getKey() ),
                    workflowManager.getAllWorkflows( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listOfWorkflowUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( workflowManager.getListOfWorkflowsUITableColumns( projectId ),
                    workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), projectId,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_TABLE_KEY ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectById( String projectId ) {
        try {
            final WorkflowProjectDTO project = workflowManager.getWorkflowProjectById( getUserIdFromGeneralHeader(),
                    UUID.fromString( projectId ) );
            if ( project != null ) {
                return ResponseUtils.success( project );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_GET_PROJECT.getKey() ) );
            }
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runWorkflow( String jobParametersString ) {
        JobParameters jobParameters;
        try {
            jobParameters = JsonUtils.jsonToObject( jobParametersString, JobParametersImpl.class );
        } catch ( final JsonSerializationException e ) {
            ExceptionLogger.logException( e, getClass() );
            return ResponseUtils.failure( e.getMessage() );
        }
        try {
            jobParameters.setJobType( JobTypeEnums.WORKFLOW.getKey() );
            jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.WORKFLOW.getKey() );
            jobParameters.setJobRunByUserId( getUserIdFromGeneralHeader() );
            jobParameters.setJobRunByUserUID( getUserNameFromGeneralHeader() );
            workflowManager.runServerSideJob( getUserIdFromGeneralHeader(), jobParameters );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runWorkflowFromWeb( String jobParametersString ) {
        JobParameters jobParameters = new JobParametersImpl();
        try {
            Map< String, Object > map = new HashMap<>();
            map = ( Map< String, Object > ) JsonUtils.jsonToMap( jobParametersString, map );
            jobParameters.setRequestHeaders(
                    JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "requestHeaders" ) ), RequestHeaders.class ) );

            if ( map.get( "postprocess" ) != null ) {
                jobParameters.setPostprocess(
                        JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "postprocess" ) ), PostProcess.class ) );

                jobParameters.setResultSchemeAsJson(
                        JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "resultSchemeAsJson" ) ), JobScheme.class ) );
            }
            jobParameters.setServer( JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "server" ) ), RestAPI.class ) );
            LatestWorkFlowDTO workflow = JsonUtils.jsonToObject( JsonUtils.objectToJson( map.get( "workflow" ) ), LatestWorkFlowDTO.class );

            if ( workflow.getJob() != null ) {

                jobParameters.setDescription(
                        workflow.getJob().get( "description" ) != null ? workflow.getJob().get( "description" ).toString() : "" );
                jobParameters.setName( workflow.getJob().get( "name" ) != null ? workflow.getJob().get( "name" ).toString() : "" );
                jobParameters.setRunsOn( workflow.getJob().get( "runsOn" ) != null ? workflow.getJob().get( "runsOn" ).toString() : "" );
                jobParameters.setWorkingDir(
                        JsonUtils.jsonToObject( JsonUtils.objectToJson( workflow.getJob().get( "workingDir" ) ), EngineFile.class ) );
                jobParameters.setEnvRequirements(
                        workflow.getJob().get( "environment" ) != null ? workflow.getJob().get( "environment" ).toString()
                                : ConstantsString.EMPTY_STRING );
                jobParameters.setWhlFile( workflow.getJob().get( "whlfile" ) != null ? workflow.getJob().get( "whlfile" ).toString()
                        : ConstantsString.EMPTY_STRING );
            }

            Map< String, Object > globalVariables = new HashMap<>();

            if ( workflow.getCustomFlags() != null ) {

                for ( String plugin : workflow.getCustomFlags() ) {
                    List< Map< String, Object > > fieldMapList = new ArrayList<>();
                    workflowManager.getFieldsRecursively( plugin, map, fieldMapList );
                    if ( plugin.contains( "ShapeModule" ) ) {
                        globalVariables.put( "ShapeModule", fieldMapList.get( 0 ) );
                    } else {
                        for ( Map< String, Object > field : fieldMapList ) {
                            globalVariables.put( "{{" + plugin + ConstantsString.DOT + field.get( "name" ).toString() + "}}",
                                    map.get( field.get( "name" ).toString() ) );
                        }
                    }
                }
            }

            jobParameters.setGlobalVariables( globalVariables );
            jobParameters.setWorkflow( workflow );
            jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );
            jobParameters.setJobRunByUserId( getUserIdFromGeneralHeader() );
            jobParameters.setJobRunByUserUID( getUserNameFromGeneralHeader() );

            workflowManager.runServerJobFromWeb( getUserIdFromGeneralHeader(), getUserTokenGeneralHeader(), jobParameters );
            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDynamicFields( String plugin, String jobParametersString ) {
        try {
            Map< String, Object > map = new HashMap<>();
            map = ( Map< String, Object > ) JsonUtils.jsonToMap( jobParametersString, map );
            List< Map< String, Object > > fieldMapList = new ArrayList<>();
            workflowManager.getFieldsRecursively( plugin, map, fieldMapList );
            return ResponseUtils.success( fieldMapList );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowContext( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( workflowManager.getWorkflowContextRouter( getUserIdFromGeneralHeader(), projectId,
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContext( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ),
                    ContextUtil.allOrderedContext(
                            workflowManager.getContext( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), objectId,
                                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredWorkflowContext( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    workflowManager.getContext( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), objectId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowSubProjectContext( String objectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    workflowManager.getContext( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), objectId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectTabsUI( String projectId ) {

        try {
            return ResponseUtils.success( workflowManager.getWorkflowProjectTabsUI( getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listData( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.PROJECTS_LIST_FETCHED_SUCCESSFULLY.getKey() ),
                    workflowManager.getAllObjects( getUserIdStringFromGeneralHeader(), UUID.fromString( projectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );

        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listDataUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( workflowManager.listDataUI( getUserIdStringFromGeneralHeader(), projectId ),
                    objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.ALL_DATA_TABLE_KEY,
                            getUserIdStringFromGeneralHeader(), projectId ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowSubProjectUI( String projectId ) {
        try {

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.SUB_PROJECT.getKey() ) ),
                    new TableUI( workflowManager.getListOfWorkflowProjectUITableColumns( projectId ),
                            workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), projectId,
                                    ConstantsObjectViewKey.SUB_PROJECT_TABLE_KEY ) ) );

        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectList( String projectId, String objectFilterJson ) {
        try {

            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.SUB_PROJECT.getKey() ) ),
                    workflowManager.getAllSubProject( getUserIdStringFromGeneralHeader(), UUID.fromString( projectId ),
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );

        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectPopertiesUI( String projectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                    MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                            Messages.PROPERTIES.getKey() ) ), workflowManager.getObjectSingleUI( projectId ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProject( String projectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WORKFLOW.getKey() ) + ConstantsString.SPACE + MessageBundleFactory.getMessage(
                                    Messages.PROPERTIES.getKey() ) ),
                    workflowManager.getWorkflowProjectById( getUserIdStringFromGeneralHeader(), UUID.fromString( projectId ) ) );

        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectVersionUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( workflowManager.getListOfWorkflowProjectUITableColumns( projectId ),
                    workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), projectId,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_VERSION_TABLE_KEY ) ) );

        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredObjectVersionsList( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( workflowManager.getObjectVersions( getUserIdFromGeneralHeader(), UUID.fromString( objectId ),
                    JsonUtils.jsonToObject( objectJson, FiltersDTO.class ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow permissions view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllWorkFlowPermissionViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), objectId,
                    ConstantsObjectViewKey.PERMISSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWorkFlowPermissionView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), objectId, objectJson, false,
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setWorkFlowPermissionViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    workflowManager.setObjectViewAsDefault( getUserIdStringFromGeneralHeader(), objectId, viewId,
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWorkFlowPermissionView( String objectId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateWorkFlowPermissionView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), objectId, objectJson, true,
                            ConstantsObjectViewKey.PERMISSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow project version view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllProjectWorkFlowVersionViewsByObjectId( String projectId ) {
        try {
            return ResponseUtils.success( workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), projectId,
                    ConstantsObjectViewKey.WORKFLOW_PROJECT_VERSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveProjectWorkFlowVersionView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, false,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_VERSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setProjectWorkFlowVersionViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    workflowManager.setObjectViewAsDefault( getUserIdStringFromGeneralHeader(), projectId, viewId,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_VERSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectWorkFlowVersionView( String projectId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateProjectWorkFlowVersionView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, true,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_VERSION_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow subproject view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSubProjectWorkFlowViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), objectId,
                    ConstantsObjectViewKey.SUB_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveSubProjectWorkFlowView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, false,
                            ConstantsObjectViewKey.SUB_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setSubProjectWorkFlowViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    workflowManager.setObjectViewAsDefault( getUserIdStringFromGeneralHeader(), projectId, viewId,
                            ConstantsObjectViewKey.SUB_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteSubProjectWorkFlowView( String projectId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateSubProjectWorkFlowView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, true,
                            ConstantsObjectViewKey.SUB_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllProjectWorkFlowViewsByObjectId( String projectId ) {
        try {

            return ResponseUtils.success( workflowManager.getListOfObjectView( getUserIdStringFromGeneralHeader(), projectId,
                    ConstantsObjectViewKey.WORKFLOW_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveProjectWorkFlowView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, false,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setProjectWorkFlowViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    workflowManager.setObjectViewAsDefault( getUserIdStringFromGeneralHeader(), projectId, viewId,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectWorkFlowView( String projectId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateProjectWorkFlowView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    workflowManager.saveOrUpdateObjectView( getUserIdStringFromGeneralHeader(), projectId, objectJson, true,
                            ConstantsObjectViewKey.WORKFLOW_PROJECT_TABLE_KEY ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow data view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProject( String projectJson ) {
        try {
            final WorkflowProjectDTO project = workflowManager.createWorkflowProject( getUserIdStringFromGeneralHeader(), projectJson );
            if ( project != null ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PROJECT_CREATED_SUCCESSFULLY.getKey() ), project );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_PROJECT.getKey() ) );
            }
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProjectForm( String parentId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                            MessageBundleFactory.getMessage( Messages.PROJECT.getKey() ) ),
                    workflowManager.createProjectForm( getUserIdStringFromGeneralHeader(), UUID.fromString( parentId ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllProjectDataWorkFlowViewsByObjectId( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.ALL_DATA_TABLE_KEY,
                    getUserIdFromGeneralHeader().toString(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveProjectDataWorkFlowView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.ALL_DATA_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setProjectDataWorkFlowViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader().toString(),
                            ConstantsObjectViewKey.ALL_DATA_TABLE_KEY, projectId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectDataWorkFlowView( String projectId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateProjectDataWorkFlowView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.ALL_DATA_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /*
     * **************************************** workflow version view ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllWorkFlowVersionViewsByObjectId( String versionId, String viewId ) {
        try {
            return ResponseUtils.success( objectViewManager.getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY,
                    getUserIdFromGeneralHeader().toString(), versionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveWorkFlowVersionView( String versionId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            prepareObjectViewDTO( versionId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setWorkFlowVersionViewAsDefault( String versionId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader().toString(),
                            ConstantsObjectViewKey.VERSION_TABLE_KEY, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteWorkFlowVersionView( String versionId, String viewId ) {
        try {
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Update work flow version view.
     *
     * @param projectId
     *         the project id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updateWorkFlowVersionView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    objectViewManager.saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectVersionsContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( workflowManager.getDataObjectVersionContext( projectId, filter ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowVersionsContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( workflowManager.getDataObjectVersionContext( projectId, filter ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response checkIfUserHasWorkflowLicense() {
        try {
            return ResponseUtils.success( workflowManager.checkIfUserHasWorkflowLicense( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response checkIfUserHasSchemeLicense( String workflowId ) {
        try {
            return ResponseUtils.success( workflowManager.checkIfUserHasSchemeLicense( getUserIdFromGeneralHeader(), workflowId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectContext( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    workflowManager.getContext( getUserIdFromGeneralHeader(), getTokenFromGeneralHeader(), projectId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) ) );
        } catch ( final SusException e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllViews() {
        try {
            return ResponseUtils.success( workflowManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.WORKFLOW_TABLE_KEY, getUserIdFromGeneralHeader().toString(), null ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveView( String viewJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.WORKFLOW_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    workflowManager.getObjectViewManager()
                            .saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    workflowManager.getObjectViewManager()
                            .saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader().toString(),
                                    ConstantsObjectViewKey.WORKFLOW_TABLE_KEY, null ) );
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
            if ( workflowManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateView( String viewId, String objectJson ) {
        try {
            final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.WORKFLOW_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    workflowManager.getObjectViewManager()
                            .saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader().toString() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getRelationsAndDelete( String id ) {
        try {
            return ResponseUtils.success( "relation deleted", workflowManager.getRelationListByProperty( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createRelation( String json ) {
        try {
            final Relation relationObj = JsonUtils.jsonToObject( json, Relation.class );
            return ResponseUtils.success( "relation created", workflowManager.createRelation( relationObj ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

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
    private ObjectViewDTO prepareObjectViewDTO( String objectId, String viewJson, String viewKey, boolean isUpdateable, String viewId ) {
        final ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( isUpdateable ) {
            objectViewDTO.setId( viewId );
        } else if ( !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setObjectId( objectId );
        objectViewDTO.setObjectViewKey( viewKey );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the object view manager.
     *
     * @param objectViewManager
     *         the new object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDesignVariableFilesInStagingByWorkflow( String wfId, String jobId, String runId, UUID masterJobId, String jobname,
            String json ) {
        try {
            Map< String, Object > downloadInfo = new HashMap<>();
            Map< String, Object > designSummary = ( Map< String, Object > ) JsonUtils.jsonToMap( json, downloadInfo );

            return ResponseUtils.success(
                    workflowManager.createDesignVariableFilesInStagingByWorkflowId( getUserIdFromGeneralHeader(), wfId, jobId,
                            designSummary, runId, masterJobId, jobname ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getImportWorkflowForm( String id ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.IMPORT_WORKFLOW.getKey() ),
                    workflowManager.getImportWorkflowForm( id ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response importWorkflow( String json ) {
        try {
            boolean saved = workflowManager.importWorkflow( getUserIdFromGeneralHeader().toString(), json );
            if ( saved ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WORKFLOW_SAVED_SUCCESSFULLY.getKey() ), saved );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_IMPORT_WORKFLOW.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response validateWorkflow( UUID wfId, String versionId ) {
        try {
            return ResponseUtils.success( workflowManager.getWorkflowValidated( getUserIdFromGeneralHeader().toString(), wfId, versionId,
                    getUserTokenGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response prepareGoals( String workflowId ) {
        try {
            String goal = workflowManager.prepareGoals( workflowId );
            if ( goal != null ) {
                return ResponseUtils.success( goal );
            } else {
                return ResponseUtils.success( null );
            }
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCB2EntityListBySelectionId( String selectionId ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getCB2EntityListBySelectionId( getUserIdFromGeneralHeader().toString(), selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response moveWorkflow( String filterJson ) {
        try {
            final MoveDTO filter = JsonUtils.jsonToObject( filterJson, MoveDTO.class );
            return ResponseUtils.success(
                    workflowManager.moveWorkflow( getUserIdFromGeneralHeader().toString(), UUID.fromString( filter.getSrcSelectionId() ),
                            UUID.fromString( filter.getTargetId() ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response downloadWorkflow( String id ) {
        try {
            String fileAddress = workflowManager.prepareWorkFlowInFile( getTokenFromGeneralHeader(), UUID.fromString( id ) );
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
    public Response downloadDesignSummaryLink( String workflowId ) {
        try {
            JSONObject obj = new JSONObject();
            String downloadBase = "config/workflowscheme/" + workflowId + "/designsummary/download";
            obj.put( "url", downloadBase );
            return ResponseUtils.success( obj );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response copyAssembleAndSimulateFilesInStaging( String payload ) {
        try {
            return ResponseUtils.success( workflowManager.copyAssembleAndSimulateFilesInStaging( payload, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getScanFileBySelectionId( String selectionId ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getScanFileBySelectionId( getUserNameFromGeneralHeader(), UUID.fromString( selectionId ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getTemplateScanFileBySelectionId( String selectionId ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getTemplateScanFileBySelectionId( getUserNameFromGeneralHeader(), UUID.fromString( selectionId ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getActionScriptFileNames() {
        try {
            return ResponseUtils.success( workflowManager.getActionScriptFieldsFromConfig() );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getActionScriptFileDetails( String scriptId ) {
        try {
            return ResponseUtils.success( workflowManager.getActionScriptDetailsFromConfig( scriptId ) );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPythonEnvironments() {
        try {
            return ResponseUtils.success( workflowManager.getPythonEnvironments() );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPythonEnvironment( String envId ) {
        try {
            return ResponseUtils.success( workflowManager.getPythonEnvironment( envId ) );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCustomVariableData( String workflowId, String json ) {
        try {
            return ResponseUtils.success( workflowManager.getCustomVariableData( getUserIdFromGeneralHeader().toString(), workflowId,
                    JsonUtils.jsonToObject( json, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response resubmitJob( String jobParametersJson ) {
        try {

            return ResponseUtils.success( MessagesUtil.getMessage( WFEMessages.JOB_SUBMITTED ),
                    workflowManager.resubmitInterruptedJob( jobParametersJson ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Duplicate selection response.
     *
     * @param workflowId
     *         the workflow id
     * @param selectionId
     *         the selection id
     * @param fieldType
     *         the field type
     *
     * @return the response
     */
    @Override
    public Response duplicateSelection( String selectionId, String fieldType ) {
        try {
            return ResponseUtils.success( workflowManager.duplicateSelection( selectionId, fieldType, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /**
     * Gets select script file names.
     *
     * @return the select script file names
     */
    @Override
    public Response getSelectScriptFileNames() {
        try {
            return ResponseUtils.success( workflowManager.getSelectScriptFieldsFromConfig() );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * Gets select script options.
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     * @param selectScriptPayloadJson
     *         the select script payload json
     *
     * @return the select script options
     */
    @Override
    public Response getSelectScriptOptions( String scriptId, String elementKey, String selectScriptPayloadJson ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getSelectFieldOptionsFromSelectScript( scriptId, elementKey, selectScriptPayloadJson,
                            getUserTokenGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * Gets field script file names.
     *
     * @return the field script file names
     */
    @Override
    public Response getFieldScriptFileNames() {
        try {
            return ResponseUtils.success( workflowManager.getFieldScriptFieldsFromConfig() );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    /**
     * Gets fields from field script.
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     *
     * @return the fields from field script
     */
    @Override
    public Response getFieldsFromFieldScript( String scriptId, String elementKey, String fieldJson ) {
        try {
            return ResponseUtils.success(
                    workflowManager.getFieldsFromFieldScript( scriptId, elementKey, fieldJson, getUserTokenGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, e.getClass() );
        }
    }

    @Override
    public Response getAllValuesForWorkflowSubProjectTableColumn( String projectId, String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    workflowManager.getAllValuesForWorkflowSubProjectTableColumn( projectId, column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getAllValuesForWorkflowTableColumn( String projectId, String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    workflowManager.getAllValuesForWorkflowTableColumn( projectId, column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getAllValuesForWorkflowProjectTableColumn( String projectId, String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    workflowManager.getAllValuesForWorkflowProjectTableColumn( projectId, column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

}