package de.soco.software.simuspace.suscore.object.service.rest;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.annotations.GZIP;

import de.soco.software.simuspace.suscore.common.constants.ConstantsGZip;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectTypes;

/**
 * This Interface is responsible for all the operation related to bread crumb.
 *
 * @author Zain ul Hassan
 */
@WebService
@Consumes( { MediaType.APPLICATION_JSON } )
@Produces( MediaType.APPLICATION_JSON )
@GZIP( force = true, threshold = ConstantsGZip.MIN_CONTENT_SIZE_TO_GZIP )
public interface BreadCrumbService {

    /**
     * Get Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_DATA_PROJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Gets the search bread crumb.
     *
     * @return the search bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_SEARCH )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSearchBreadCrumb();

    /**
     * Gets the search bread crumb by search id.
     *
     * @return the search bread crumb by search id
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_SEARCH_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSearchBreadCrumbBySearchId( @PathParam( ConstantsObjectTypes.SEARCH_ID_PARAM ) String searchId );

    /**
     * Create Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_IN_CONTAINER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDataProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Update Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_DATA_PROJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Get Data Object Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_DATA_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Get Data Object Version Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_DATA_OBJECT_BY_ID_AND_VERSION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getDataObjectVersionBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Gets the project version bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the project version bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_PROJECT_BY_ID_AND_VERSION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getProjectVersionBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Create Data Object Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_DATA_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDataObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Create Data Container Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_DATA_CONTAINER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDataContainerBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Get System Audit Logs Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_AUDIT_LOGS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemAuditLogsBreadCrumb();

    /**
     * View System Groups Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_SYSTEM_GROUPS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response viewSystemGroupsBreadCrumb();

    /**
     * Create System User Group Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_GROUP )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemUserGroupBreadCrumb();

    /**
     * Get System Permission Roles Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_PERMISSIONS_ROLES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemPermisssionRolesBreadCrumb();

    /**
     * Create System Permissions Roles Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_PERMISSIONS_ROLE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemPermissionsRoleBreadCrumb();

    /**
     * Get System User Directories Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_USER_DIRECTORIES )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemUserDirectoriesBreadCrumb();

    /**
     * Gets the job bread crumb.
     *
     * @return the job bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_JOBS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobBreadCrumb();

    /**
     * Gets the Hpc job bread crumb.
     *
     * @return the job bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.HPC_UGE_JOBS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getHpcUgeBreadCrumb( @PathParam( "objectId" ) String objectId, @PathParam( "id" ) String jobId );

    /**
     * Create System User Directory Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER_DIRECTORY )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemUserDirectoriesBreadCrumb();

    /**
     * Get System Users Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_USERS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemUsersBreadCrumb();

    /**
     * Gets the system location bread crumb.
     *
     * @return the system location bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_LOCATIONS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemLocationBreadCrumb();

    /**
     * Get System Users Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_USER_PROFILE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editSystemUsersProfileBreadCrumb();

    /**
     * Create System User Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_USER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemUserBreadCrumb();

    /**
     * Creates the system location bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LOCATION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemLocationBreadCrumb();

    /**
     * Edits the system user bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_SYSTEM_LOCATION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editSystemLocationBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Get System License Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SYSTEM_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getSystemLicenseBreadCrumb();

    /**
     * Create System License Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SYSTEM_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSystemLicenseBreadCrumb();

    /**
     * Manage System License Bread Crumb.
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.MANAGE_SYSTEM_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response manageSystemLicenseBreadCrumb();

    /**
     * Get Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_WORKFLOW_PROJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowProjectViewBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Get Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_WORKFLOW_SINGLE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowSingleViewBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectTypes.VERSION_ID_PARAM ) int versionId );

    /**
     * Gets the workflow single bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the workflow single bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_WORKFLOW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getWorkflowSingleBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Gets the job single view bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the job single view bread crumb
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.VIEW_JOB_SINGLE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getJobSingleViewBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Create Data Project Bread Crumb By objectId.
     *
     * @param objectId
     *         the object id
     *
     * @return Response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_WORKFLOW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWorkflowBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Creates the deleted objects bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.DELETED_OBJECTS_BREADCRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDeletedObjectsBreadCrumb();

    /**
     * Creates the about bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.ABOUT_BREADCRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createAboutBreadCrumb();

    /**
     * Creates the support bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.SUPPORT_BREADCRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSupportBreadCrumb();

    /**
     * Creates the download client bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.DOWNLOAD_CLIENT_BREADCRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDownloadClientBreadCrumb();

    /**
     * Creates the add meta data to object bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.ADD_META_DATA_BREAD_CRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createAddMetaDataToObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Update data object bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_DATA_OBJECT_BREAD_CRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDataObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Creates the data projects bread crumb.
     *
     * @param projectId
     *         the project id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_DATA_OBJECT_BREAD_CRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDataProjectsBreadCrumb( @PathParam( "projectId" ) String projectId );

    /**
     * Creates the status projects bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_STATUS_OBJECT_BREAD_CRUMB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createStatusProjectsBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Import workflow projects bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.IMPORT_WORKFLOW_PROJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response importWorkflowProjectsBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the workflow project bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_WORKFLOW_PROJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editWorkflowProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the role bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_ROLE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editRoleBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Manage role bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.ROLE_PERMISSION_MANAGE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response manageRoleBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the group bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_GROUP )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editGroupBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the system license bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_SYSTEM_LICENSE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editSystemLicenseBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the system directory bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_DIRECTORY )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editSystemDirectoryBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Adds the permissions to object bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.ADD_PERMISSIONS_TO_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response addPermissionsToObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the system user bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_SYSTEM_USER )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editSystemUserBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Update change status bread crumb.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_OBJECT_STATUS )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateChangeStatusBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId,
            @PathParam( "versionId" ) String versionId );

    /**
     * Update change type bread crumb.
     *
     * @param projectId
     *         the project id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_OBJECT_TYPE )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateChangeTypeBreadCrumb( @PathParam( ConstantsObjectTypes.PROJECT_ID_PARAM ) String projectId,
            @PathParam( ConstantsObjectTypes.PROJECT_ID_PARAM ) String selectionId );

    /**
     * Update project metadata bread crumb.
     *
     * @param objectId
     *         the object id
     * @param projectName
     *         the project name
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.UPDATE_PROJECT_METADATA )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateProjectMetadataBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId,
            @PathParam( "projectName" ) String projectName );

    /**
     * Project perm bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.PROJECT_PERMISSION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response projectPermBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Edits the workflow bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EDIT_WORKFLOW )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editWorkflowBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Creates the workflow project bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_WORKFLOW_PROJECT_OBJECT_ID )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWorkflowProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Run workflow project bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/run/workflow/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runWorkflowProjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Run scheme bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/run/scheme/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runSchemeBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Re run job bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.RERUN_JOB )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response reRunJobBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * Export object bread crumb.
     *
     * @param objectId
     *         the object id
     * @param selectionId
     *         the selection id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.EXPORT_OBJECT )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response exportObjectBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId,
            @PathParam( ConstantsObjectTypes.SELECTION_ID_PARAM ) String selectionId );

    /**
     * Creates the section bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( ConstantsObjectServiceEndPoints.CREATE_SECTION )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createSectionBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    /**
     * View loadcase bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "view/algorithms/loadcase" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response viewLoadcaseBreadCrumb();

    /**
     * Creates the loadcase bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "create/algorithms/loadcase/" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createLoadcaseBreadCrumb();

    /**
     * Update loadcase bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "edit/algorithms/loadcase/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateLoadcaseBreadCrumb( @PathParam( "id" ) String id );

    /**
     * View loadcase bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "view/algorithms/scheme" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response viewWFSchemeBreadCrumb();

    /**
     * Creates the loadcase bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "create/algorithms/scheme" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createWFSchemeBreadCrumb();

    /**
     * Update loadcase bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "edit/algorithms/scheme/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateWFSchemeBreadCrumb( @PathParam( "id" ) String id );

    /**
     * View training algo bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "view/algorithms/training" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response viewTrainingAlgoBreadCrumb();

    /**
     * Creates the loadcase bread crumb.
     *
     * @return the response
     */
    @GET
    @Path( "create/algorithms/training" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createTrainingAlgoBreadCrumb();

    /**
     * Update loadcase bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "edit/algorithms/training/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateTrainingAlgoBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Run variant breadcrumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "view/wizards/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runVariantBreadcrumb( @PathParam( "id" ) String id );

    /**
     * Run Dummy variant breadcrumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "view/wizards/dummy/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runDummyVariantBreadcrumb( @PathParam( "id" ) String id );

    /**
     * Creates the dummy variant breadcrumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "create/wizards/variant/reference/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response createDummyVariantBreadcrumb( @PathParam( "id" ) String id );

    @GET
    @Path( "view/wizards/qadyna/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runQadynaBreadcrumb( @PathParam( "id" ) String id );

    /**
     * Update design variable bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "update/designvariables/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateDesignVariableBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Update objective variable bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "update/objectivevariables/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response updateObjectiveVariableBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Update objective variable bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the response
     */
    @GET
    @Path( "system/permissions/groups/{id}/users" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getUsersFromGroupBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Gets groups from user bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the groups from user bread crumb
     */
    @GET
    @Path( "system/user/{id}/groups" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getGroupsFromUserBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Gets groups from user bread crumb.
     *
     * @param id
     *         the id
     *
     * @return the groups from user bread crumb
     */
    @GET
    @Path( "system/permissions/role/{id}/groups" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getGroupsFromRoleBreadCrumb( @PathParam( "id" ) String id );

    /**
     * Bookmark.
     *
     * @return the response
     */
    @GET
    @Path( "bookmark" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response getBookmarkBreadCrumb();

    /**
     * Edits the bookmark bread crumb.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @GET
    @Path( "/edit/bookmark/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response editBookmarkBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    @GET
    @Path( "/view/job/workflow/{objectId}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response workflowJobsBreadCrumb( @PathParam( ConstantsObjectTypes.OBJECT_ID_PARAM ) String objectId );

    @GET
    @Path( "view/wizards/cb2/variant/{id}" )
    @Produces( { MediaType.APPLICATION_JSON } )
    Response runCB2VariantBreadcrumb( @PathParam( "id" ) String id );

}
