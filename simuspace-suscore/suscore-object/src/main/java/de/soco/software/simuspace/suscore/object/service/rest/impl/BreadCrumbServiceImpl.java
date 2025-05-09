package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.object.manager.BreadCrumbManager;
import de.soco.software.simuspace.suscore.object.service.rest.BreadCrumbService;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;

/**
 * Implementation Class for Interface responsible for all the operation related to Bread Crumb.
 *
 * @author Zain ul Hassan
 */
public class BreadCrumbServiceImpl extends SuSBaseService implements BreadCrumbService {

    /**
     * Reference for BreadCrumbManager.
     */
    private BreadCrumbManager breadCrumbManager;

    /**
     * The Constant CREATE_PROJECT.
     */
    private static final String CREATE_PROJECT = "create/data/project/";

    /**
     * The Constant CREATE_WORKFLOW_PROJECT.
     */
    private static final String CREATE_WORKFLOW_PROJECT = "create/workflow/project/";

    /**
     * The Constant RUN_WORKFLOW_PROJECT.
     */
    private static final String RUN_WORKFLOW_PROJECT = "run/workflow/";

    /**
     * The Constant RUN_SCHEME.
     */
    private static final String RUN_SCHEME = "run/scheme/";

    /**
     * The Constant CREATE_WORKFLOW.
     */
    private static final String CREATE_WORKFLOW = "create/workflow/";

    /**
     * The Constant UPDATE_PROJECT.
     */
    private static final String UPDATE_PROJECT = "update/data/project/";

    /**
     * The Constant CREATE_DATA_PROJECT.
     */
    private static final String CREATE_DATA_PROJECT = "project/create/options/";

    /**
     * The Constant UPDATE_OBJECT.
     */
    private static final String UPDATE_OBJECT = "update/data/object/";

    /**
     * The Constant ADD_DATA_TO_OBJECT.
     */
    private static final String ADD_DATA_TO_OBJECT = "add/data/object/";

    /**
     * The Constant CREATE_OBJECT.
     */
    private static final String CREATE_OBJECT = "create/data/object/";

    /**
     * The Constant CREATE_CONTAINER.
     */
    private static final String CREATE_CONTAINER = "create/data/container/";

    /**
     * FAILURE_OF_BREAD_CRUMB constant.
     */
    private static final String FAILURE_OF_BREAD_CRUMB = "Unable to get BreadCrumb";

    /**
     * The Constant DOWNLOAD_CLIENT.
     */
    private static final String DOWNLOAD_CLIENT = "Download-Client";

    /**
     * The Constant SUPPORT.
     */
    private static final String SUPPORT = "Support";

    /**
     * The Constant ABOUT.
     */
    private static final String ABOUT = "About";

    /**
     * The Constant BOOKMARKS.
     */
    private static final String BOOKMARKS = "4100043x4";

    /**
     * The Constant CHANGE_STATUS.
     */
    private static final String CHANGE_STATUS = "change/status/";

    /**
     * The Constant CHANGE_TYPE.
     */
    private static final String CHANGE_TYPE = "changetype/";

    /**
     * The Constant IMPORT_WORKFLOW.
     */
    private static final String IMPORT_WORKFLOW = "workflow/import/ui/";

    /**
     * The Constant EDIT_WORKFLOW_PROJECT.
     */
    private static final String EDIT_WORKFLOW_PROJECT = "workflow/ui/edit/";

    /**
     * The Constant EDIT_DESIGN_VARIABLE.
     */
    private static final String EDIT_DESIGN_VARIABLE = "update/designvariable/";

    /**
     * The Constant EDIT_OBJECTIVE_VARIABLE.
     */
    private static final String EDIT_OBJECTIVE_VARIABLE = "update/objectivevariable/";

    /**
     * The Constant EDIT_ROLE.
     */
    private static final String EDIT_ROLE = "edit/system/permissions/role/";

    /**
     * The Constant EDIT_DATA_OBJECT.
     */
    private static final String EDIT_DATA_OBJECT = "edit/data/object/";

    /**
     * The Constant ROLE_MANAGE_PERMISSIONS.
     */
    private static final String ROLE_MANAGE_PERMISSIONS = "system/permissions/role/manage/";

    /**
     * The Constant GROUP_MANAGE_PERMISSIONS.
     */
    private static final String GROUP_MANAGE_PERMISSIONS = "edit/system/group/";

    /**
     * The Constant SYSTEM_LICENSE_EDIT.
     */
    private static final String SYSTEM_LICENSE_EDIT = "edit/system/license/";

    /**
     * The Constant EDIT_SYSTEM_DIRECTORY.
     */
    private static final String EDIT_SYSTEM_DIRECTORY = "edit/system/user-directory/";

    /**
     * The Constant ADD_PERMISSIONS.
     */
    private static final String ADD_PERMISSIONS = "add/permissions/";

    /**
     * The Constant EDIT_SYSTEM_USER.
     */
    private static final String EDIT_SYSTEM_USER = "edit/system/user/";

    private static final String EDIT_SYSTEM_LOCATION = "edit/system/location/";

    /**
     * The Constant EDIT_WORKFLOW.
     */
    private static final String EDIT_WORKFLOW = "workflow/ui/edit";

    private static final String JOB_RERUN = "jobs/rerun/";

    /**
     * The Constant EXPORT.
     */
    private static final String EXPORT = "/export/";

    /**
     * The Constant SECTION.
     */
    private static final String SECTION = "/section/";

    /**
     * The Constant BOOKMARK_TREE_CONTEXT.
     */
    private static final String BOOKMARK_TREE_CONTEXT = "/bookmark/tree/context";

    /**
     * The Constant API_LOCATION_EXPORT_FILE.
     */
    private static final String API_HPC_BREADCRUMB = "/api/dashboard/hpc/{objectId}/uge/job/breadcrumb/";

    /**
     * The Constant CREATE_TRAININGALGO.
     */
    private static final String CREATE_TRAINING_ALGO = "create/config/trainingalgo/";

    /**
     * The Constant CREATE_WFSCHEME.
     */
    private static final String CREATE_WFSCHEME = "create/config/wfscheme/";

    /**
     * The Constant CREATE_LOADCASE.
     */
    private static final String CREATE_LOADCASE = "create/config/loadcase/";

    /**
     * The Constant EDIT_WFSCHEME.
     */
    private static final String EDIT_WFSCHEME = "edit/config/wfscheme/";

    /**
     * The Constant EDIT_TRAINING_ALGO.
     */
    private static final String EDIT_TRAINING_ALGO = "edit/config/trainingalgo/";

    /**
     * The Constant EDIT_LOADCASE.
     */
    private static final String EDIT_LOADCASE = "edit/config/loadcase/";

    /**
     * Gets the Bread Crumb.
     *
     * @param uuid
     *         the uuid
     * @param api
     *         the api
     *
     * @return the bread crumb
     */
    private Response getBreadCrumb( String uuid, String api ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.createBreadCrumb( getUserIdFromGeneralHeader(), uuid, api );
            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataProjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectVersionBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDataProjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CREATE_DATA_PROJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWorkflowProjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CREATE_WORKFLOW_PROJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runWorkflowProjectBreadCrumb( String objectId ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.createRunWorkflowBreadCrumb( getUserIdFromGeneralHeader(), objectId,
                    RUN_WORKFLOW_PROJECT + objectId );

            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runSchemeBreadCrumb( String objectId ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.createRunSchemeBreadCrumb( getUserIdFromGeneralHeader(), objectId,
                    RUN_SCHEME + objectId );

            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDataProjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, UPDATE_PROJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataObjectVersionBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDataObjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CREATE_OBJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDataContainerBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CREATE_CONTAINER + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemAuditLogsBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.AUDIT.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemPermisssionRolesBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.ROLES.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemPermissionsRoleBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.ROLES.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_PERMISSIONS_ROLE );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response viewSystemGroupsBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.GROUPS.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemUserGroupBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.GROUPS.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_GROUP );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemUserDirectoriesBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.DIRECTORIES.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobBreadCrumb() {
        return getBreadCrumb( SimuspaceFeaturesEnum.JOBS.getId(), null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcUgeBreadCrumb( String objectId, String jobId ) {
        String url = PropertiesManager.getLocationURL() + API_HPC_BREADCRUMB.replace( "{objectId}", objectId ) + jobId;
        RequestHeaders requestHeader = new RequestHeaders( getTokenFromGeneralHeader(),
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.41 Safari/537.36" );
        SusResponseDTO responseDTO = SuSClient.getRequest( url, CommonUtils.prepareHeadersWithAuthToken( requestHeader.getToken() ) );
        if ( responseDTO != null ) {
            return ResponseUtils.success( responseDTO.getData() );
        } else {
            return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemUserDirectoriesBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.DIRECTORIES.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_DIRECTORY );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemUsersBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.USERS.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemUserBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.USERS.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemLocationBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOCATIONS.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemLocationBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOCATIONS.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LOCATION );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response editSystemLocationBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOCATIONS.getId(), EDIT_SYSTEM_LOCATION + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editSystemUsersProfileBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.USERS.getId(), ConstantsObjectServiceEndPoints.EDIT_USER_PROFILE );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSystemLicenseBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LICENSES.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSystemLicenseBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LICENSES.getId(), ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LICENSE );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response manageSystemLicenseBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LICENSES.getId(), ConstantsObjectServiceEndPoints.MANAGE_SYSTEM_LICENSE );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the bread crumb manager.
     *
     * @return the BreadCrumbManager
     */
    public BreadCrumbManager getBreadCrumbManager() {
        return breadCrumbManager;
    }

    /**
     * Sets the Bread Crumb Manager.
     *
     * @param breadCrumbManager
     *         the new bread Crumb manager
     */
    public void setBreadCrumbManager( BreadCrumbManager breadCrumbManager ) {
        this.breadCrumbManager = breadCrumbManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowProjectViewBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response workflowJobsBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWorkflowBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CREATE_WORKFLOW + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDeletedObjectsBreadCrumb() {

        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowSingleViewBreadCrumb( String objectId, int versionId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getWorkflowSingleBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getJobSingleViewBreadCrumb( String objectId ) {

        return getBreadCrumb( objectId, null );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createAboutBreadCrumb() {
        try {
            BreadCrumbItemDTO breadCrumbDTO = new BreadCrumbItemDTO();
            breadCrumbDTO.setName( ABOUT );
            List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
            breadCrumbItems.add( breadCrumbDTO );
            return ResponseUtils.success( breadCrumbItems );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSupportBreadCrumb() {
        try {
            BreadCrumbItemDTO bereadCrumbDTO = new BreadCrumbItemDTO();
            bereadCrumbDTO.setName( SUPPORT );
            List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
            breadCrumbItems.add( bereadCrumbDTO );
            return ResponseUtils.success( breadCrumbItems );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDownloadClientBreadCrumb() {
        try {
            BreadCrumbItemDTO bereadCrumbDTO = new BreadCrumbItemDTO();
            bereadCrumbDTO.setName( DOWNLOAD_CLIENT );
            List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
            breadCrumbItems.add( bereadCrumbDTO );
            return ResponseUtils.success( breadCrumbItems );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createAddMetaDataToObjectBreadCrumb( String objectId ) {
        return getBreadCrumb( objectId, ADD_DATA_TO_OBJECT + objectId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDataObjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, UPDATE_OBJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDataProjectsBreadCrumb( String projectId ) {
        try {
            return getBreadCrumb( projectId, CREATE_PROJECT + projectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createStatusProjectsBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, CHANGE_STATUS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response importWorkflowProjectsBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, IMPORT_WORKFLOW + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editWorkflowProjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, EDIT_WORKFLOW_PROJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editRoleBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.ROLES.getId(), EDIT_ROLE + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response manageRoleBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.ROLES.getId(), ROLE_MANAGE_PERMISSIONS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editGroupBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.GROUPS.getId(), GROUP_MANAGE_PERMISSIONS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editSystemLicenseBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LICENSES.getId(), SYSTEM_LICENSE_EDIT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editSystemDirectoryBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.DIRECTORIES.getId(), EDIT_SYSTEM_DIRECTORY + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response addPermissionsToObjectBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, ADD_PERMISSIONS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editSystemUserBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.USERS.getId(), EDIT_SYSTEM_USER + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateChangeStatusBreadCrumb( String objectId, String versionId ) {
        try {
            return getBreadCrumb( objectId, CHANGE_STATUS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateChangeTypeBreadCrumb( String objectId, String versionId ) {
        try {
            return getBreadCrumb( objectId, CHANGE_TYPE + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProjectMetadataBreadCrumb( String objectId, String projectName ) {
        try {
            return getBreadCrumb( objectId, EDIT_DATA_OBJECT + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response projectPermBreadCrumb( String objectId ) {
        try {
            return getBreadCrumb( objectId, ADD_PERMISSIONS + objectId );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editWorkflowBreadCrumb( String objectId ) {
        return getBreadCrumb( objectId, EDIT_WORKFLOW + objectId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response reRunJobBreadCrumb( String objectId ) {
        return getBreadCrumb( objectId, JOB_RERUN + objectId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response exportObjectBreadCrumb( String objectId, String selectionId ) {
        return getBreadCrumb( objectId, EXPORT + objectId );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createSectionBreadCrumb( String objectId ) {
        return getBreadCrumb( objectId, SECTION + objectId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response viewLoadcaseBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOADCASES.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createLoadcaseBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOADCASES.getId(), CREATE_LOADCASE + SimuspaceFeaturesEnum.LOADCASES.getId() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response viewWFSchemeBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.WFSCHEMES.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createWFSchemeBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.WFSCHEMES.getId(), CREATE_WFSCHEME + SimuspaceFeaturesEnum.WFSCHEMES.getId() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWFSchemeBreadCrumb( String id ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.WFSCHEMES.getId(), EDIT_WFSCHEME + id );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response viewTrainingAlgoBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.TRAINING_ALGO.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createTrainingAlgoBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.TRAINING_ALGO.getId(),
                    CREATE_TRAINING_ALGO + SimuspaceFeaturesEnum.TRAINING_ALGO.getId() );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateTrainingAlgoBreadCrumb( String id ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.TRAINING_ALGO.getId(), EDIT_TRAINING_ALGO + id );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchBreadCrumb() {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.SEARCH.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateLoadcaseBreadCrumb( String id ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.LOADCASES.getId(), EDIT_LOADCASE + id );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSearchBreadCrumbBySearchId( String searchId ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.SEARCH.getId(), null );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runVariantBreadcrumb( String id ) {
        return getBreadCrumb( id, "view/wizards/variant/" + id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response runDummyVariantBreadcrumb( String id ) {
        return getBreadCrumb( id, "view/wizards/dummy/" + id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createDummyVariantBreadcrumb( String id ) {
        return getBreadCrumb( id, "create/wizards/variant/reference/" + id );
    }

    @Override
    public Response runQadynaBreadcrumb( String id ) {
        return getBreadCrumb( id, "view/wizards/qadyna/" + id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDesignVariableBreadCrumb( String id ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.WORKFLOWS.getId(), EDIT_DESIGN_VARIABLE + id );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectiveVariableBreadCrumb( String id ) {
        try {
            return getBreadCrumb( SimuspaceFeaturesEnum.WORKFLOWS.getId(), EDIT_OBJECTIVE_VARIABLE + id );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUsersFromGroupBreadCrumb( String id ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.getGroupUsersBreadCrumb( getUserIdFromGeneralHeader(), id );
            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getGroupsFromUserBreadCrumb( String id ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.getUserGroupsBreadCrumb( getUserIdFromGeneralHeader(), id );
            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getGroupsFromRoleBreadCrumb( String id ) {
        try {
            BreadCrumbDTO breadCrumbDTO = breadCrumbManager.getRoleGroupsBreadCrumb( getUserIdFromGeneralHeader(), id );
            if ( breadCrumbDTO != null ) {
                return ResponseUtils.success( breadCrumbDTO.getBreadCrumbItems() );
            } else {
                return ResponseUtils.failure( FAILURE_OF_BREAD_CRUMB );
            }
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getBookmarkBreadCrumb() {
        try {
            BreadCrumbItemDTO breadCrumbDTO = new BreadCrumbItemDTO();
            breadCrumbDTO.setName( MessageBundleFactory.getMessage( BOOKMARKS ) );
            breadCrumbDTO.setContext( BOOKMARK_TREE_CONTEXT );
            List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
            breadCrumbItems.add( breadCrumbDTO );
            return ResponseUtils.success( breadCrumbItems );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editBookmarkBreadCrumb( String objectId ) {
        try {
            BreadCrumbItemDTO breadCrumbDTO = new BreadCrumbItemDTO();
            breadCrumbDTO.setName( MessageBundleFactory.getMessage( BOOKMARKS ) );
            breadCrumbDTO.setContext( BOOKMARK_TREE_CONTEXT );
            breadCrumbDTO.setItemId( UUID.fromString( objectId ) );
            List< BreadCrumbItemDTO > breadCrumbItems = new ArrayList<>();
            breadCrumbItems.add( breadCrumbDTO );
            return ResponseUtils.success( breadCrumbItems );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    @Override
    public Response runCB2VariantBreadcrumb( String id ) {
        return getBreadCrumb( id, "view/wizards/cb2/variant/" + id );
    }

}
