package de.soco.software.simuspace.server.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.cxf.message.Message;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;

import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.model.TokenDetails;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.DynamicScript;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.PythonEnvironmentDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.WorkflowValidationDTO;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.UserLicenseDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;

/**
 * This Interface consist of methods related to workflows. It provide configurations, workflowDTO, token, prepare workflow entity, save,
 * update workflow.
 *
 * @author Nosheen.Sharif
 */
public interface WorkflowManager {

    /**
     * To get Workflow Configration file from Server.
     *
     * @return Config
     */
    JsonNode getConfig();

    /**
     * To get Hpc Configration file from Server.
     *
     * @return Config
     */
    JsonNode getHpcConfig();

    /**
     * To get Email Configration file from Server.
     *
     * @return Config
     */
    Properties getEmailConfig();

    /**
     * Gets workflow by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow by id
     */
    WorkflowDTO getWorkflowById( EntityManager entityManager, UUID userId, String workflowId );

    /**
     * Gets the workflow and fill with job params.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param job
     *         the job
     *
     * @return the workflow and fill with job parameters
     *
     * @apiNote To be used in service calls only
     */
    WorkflowDTO getWorkflowByIdWithRefJobParams( UUID userId, String workflowId, Job job );

    /**
     * Gets the workflow list with latest versions.
     *
     * @param userId
     *         the user id
     *
     * @return the workflow list of Dto's type
     *
     * @throws SusException
     *         the sus exception
     * @apiNote To be used in service calls only
     */
    List< WorkflowDTO > getWorkflowList( UUID userId );

    /**
     * Gets the filtered workflow list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the filtered workflow list
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LatestWorkFlowDTO > getFilteredWorkflowList( UUID userId, FiltersDTO filter );

    /**
     * Gets the token to open the designer with user permission and checkout the license.
     *
     * @param currentMessage
     *         the current message
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     *
     * @return the token
     *
     * @apiNote To be used in service calls only
     */
    TokenDetails getToken( Message currentMessage, UUID userId, String userName );

    /**
     * To convert workflowDto to workflowEntity.
     *
     * @param dto
     *         the dto
     *
     * @return the workflow entity
     */
    WorkflowEntity prepareWorkflowEntity( WorkflowDTO dto );

    /**
     * Save WorkflowDto to databank.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param dto
     *         the dto
     * @param workflowJson
     *         the workflow json
     *
     * @return the save workflowDto
     */
    WorkflowDTO saveWorkflow( EntityManager entityManager, UUID userId, String parentId, WorkflowDTO dto, String workflowJson );

    /**
     * Update workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param workflowDto
     *         the workflow dto
     *
     * @return the workflow DTO
     */
    WorkflowDTO updateWorkflow( EntityManager entityManager, UUID userId, String workflowId, WorkflowDTO workflowDto, String workflowJson );

    /**
     * Gets the workflow versions by id.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow versions by id
     *
     * @apiNote To be used in service calls only
     */
    List< LatestWorkFlowDTO > getWorkflowVersionsById( UUID userId, String workflowId );

    /**
     * Gets workflow by id and version id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     *
     * @return the workflow by id and version id
     */
    WorkflowDTO getWorkflowByIdAndVersionId( EntityManager entityManager, UUID userId, String workflowId, int versionId );

    /**
     * Gets the workflow list by category id.
     *
     * @param userId
     *         the user id
     * @param categoryId
     *         the category id
     *
     * @return the workflow list by category id
     *
     * @apiNote To be used in service calls only
     */
    List< WorkflowDTO > getWorkflowListByCategoryId( UUID userId, String categoryId );

    /**
     * Gets the scheme category.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the scheme category
     */
    int getSchemeCategory( EntityManager entityManager, String workflowId, String userIdFromGeneralHeader );

    /**
     * Gets the workflow versions without definition.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflowId
     *
     * @return the workflow versions without definition
     *
     * @apiNote To be used in service calls only
     */
    List< WorkflowDTO > getWorkflowVersionsWithoutDefinition( UUID userId, String workflowId );

    /**
     * Update workflow status.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     * @param actionId
     *         the action id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean updateWorkflowStatus( UUID userId, String workflowId, int versionId, int actionId );

    /**
     * Validate workflow elements and their fields.
     *
     * @param workflowDto
     *         the workflow dto
     * @param isImportWorkflow
     *         the is import workflow
     *
     * @return true, if successful
     */
    boolean validateWorkflow( LatestWorkFlowDTO workflowDto, boolean isImportWorkflow );

    /**
     * Gets the IP from token.
     *
     * @param token
     *         the token
     *
     * @return the IP from token
     */
    String getIPFromToken( String token );

    /**
     * Gets the license consumers list.
     *
     * @param userId
     *         the user id
     *
     * @return the license consumers list
     */
    List< UserLicenseDTO > getLicenseConsumersList( UUID userId );

    /**
     * Clear token.
     *
     * @param userIdFromMessage
     *         the user id from message
     * @param token
     *         the token
     * @param userLicenseDTO
     *         the user license dto
     *
     * @return true, if successful
     */
    boolean clearToken( UUID userIdFromMessage, String token, UserLicenseDTO userLicenseDTO );

    /**
     * Checkout token.
     *
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    UserLicenseDTO checkoutToken( UUID userId, String userName, String token );

    /**
     * Gets select field options from select script.
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     * @param selectScriptPayloadJson
     *         the select script payload json
     * @param userToken
     *         the user token
     *
     * @return the select field options from select script
     */
    Object getSelectFieldOptionsFromSelectScript( String scriptId, String elementKey, String selectScriptPayloadJson, String userToken );

    /**
     * Gets select script fields from config.
     *
     * @return the select script fields from config
     */
    List< WfFieldsUiDTO > getSelectScriptFieldsFromConfig();

    /**
     * Gets field script fields from config.
     *
     * @return the field script fields from config
     */
    List< WfFieldsUiDTO > getFieldScriptFieldsFromConfig();

    /**
     * Gets fields from field script.
     *
     * @param scriptId
     *         the script id
     * @param elementKey
     *         the element key
     * @param userToken
     *         the user token
     *
     * @return the fields from field script
     */
    Map< String, Object > getFieldsFromFieldScript( String scriptId, String elementKey, String fieldsScriptPayloadJson, String userToken );

    /**
     * Gets the user manager.
     *
     * @return the user manager
     */
    WorkflowUserManager getUserManager();

    /**
     * Sets the user manager.
     *
     * @param userManager
     *         the new user manager
     */
    void setUserManager( WorkflowUserManager userManager );

    /**
     * Verify license checkout.
     *
     * @param token
     *         the token
     *
     * @return the user license DTO
     */
    UserLicenseDTO verifyLicenseCheckout( String token );

    void addJobToAcl( EntityManager entityManager, String userId, JobEntity createdEntity, SuSEntity parentEntity );

    /**
     * Save workflow.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param newWorkflowDTO
     *         the new workflow DTO
     * @param workflowJson
     *         the workflow json
     *
     * @return the workflow DTO
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO saveWorkflow( UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO, String workflowJson );

    /**
     * Save workflow as new latest work flow dto.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param newWorkflowDTO
     *         the new workflow dto
     * @param workflowJson
     *         the workflow json
     *
     * @return the latest work flow dto
     */
    LatestWorkFlowDTO saveWorkflowAsNew( UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO, String workflowJson );

    /**
     * Save workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param newWorkflowDTO
     *         the new workflow DTO
     * @param workflowJson
     *         the workflow json
     *
     * @return the workflow DTO
     */
    LatestWorkFlowDTO saveWorkflow( EntityManager entityManager, UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO,
            String workflowJson );

    /**
     * Update workflow.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param dto
     *         the dto
     *
     * @return the workflow DTO
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO updateWorkflow( UUID userId, String workflowId, LatestWorkFlowDTO dto, String workflowJson );

    /**
     * Update workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param dto
     *         the dto
     * @param workflowJson
     *         the workflow json
     *
     * @return the workflow DTO
     */
    LatestWorkFlowDTO updateWorkflow( EntityManager entityManager, UUID userId, String workflowId, LatestWorkFlowDTO dto,
            String workflowJson, boolean isImport );

    /**
     * Creates the workflow form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createWorkflowForm( UUID userId, String parentId );

    /**
     * Edits the workflow form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm editWorkflowForm( UUID userId, String parentId );

    /**
     * Edits the workflow project form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the list
     */
    UIForm editWorkflowProjectForm( UUID userId, String parentId );

    /**
     * Gets the new workflow by id and version id.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     *
     * @return the new workflow by id and version id
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO getNewWorkflowByIdAndVersionId( UUID userId, String workflowId, int versionId );

    /**
     * Gets the new workflow by id and version id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param versionId
     *         the version id
     *
     * @return the new workflow by id and version id
     */
    LatestWorkFlowDTO getNewWorkflowByIdAndVersionId( EntityManager entityManager, UUID userId, String workflowId, int versionId );

    /**
     * Gets the new workflow by id.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the new workflow by id
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO getNewWorkflowById( UUID userId, String workflowId );

    /**
     * Gets the new workflow by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the new workflow by id
     */
    LatestWorkFlowDTO getNewWorkflowById( EntityManager entityManager, UUID userId, String workflowId );

    /**
     * Gets the custom flag list.
     *
     * @return the custom flag list
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    List< String > getCustomFlagList() throws IOException;

    /**
     * Gets the custom flag UI.
     *
     * @param plugins
     *         the plugins
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag UI
     */
    UIForm getCustomFlagUI( String plugins, UriInfo uriInfo );

    /**
     * Gets the shape module run tabs.
     *
     * @param plugins
     *         the plugins
     *
     * @return the shape module run tabs
     */
    List< String > getShapeModuleRunTabs( String plugins );

    /**
     * Prepare select ui item from field ui form item.
     *
     * @param field
     *         the field
     *
     * @return the ui form item
     */
    UIFormItem prepareSelectUIItemFromField( Map< String, Object > field );

    /**
     * Gets the custom flag UI.
     *
     * @param userId
     *         the user id
     * @param rerunJobId
     *         the rerunJobId
     * @param plugins
     *         the plugins
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCustomFlagUIForRerun( UUID userId, String rerunJobId, String plugins, UriInfo uriInfo );

    /**
     * Gets the custom flag plugin UI.
     *
     * @param plugins
     *         the plugins
     * @param value
     *         the value
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag plugin UI
     */
    UIForm getCustomFlagPluginUI( String plugins, String value, UriInfo uriInfo );

    /**
     * Gets the custom flag plugin UI.
     *
     * @param userId
     *         the user id
     * @param rerunJobId
     *         the rerunJobId
     * @param plugins
     *         the plugins
     * @param value
     *         the value
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag plugin UI
     *
     * @apiNote To be used in service calls only
     */
    UIForm getCustomFlagPluginUIForRerun( UUID userId, String rerunJobId, String plugins, String value, UriInfo uriInfo );

    /**
     * Gets the fields recursively.
     *
     * @param plugin
     *         the plugin
     * @param map
     *         the map
     * @param fieldMap
     *         the field map
     */
    void getFieldsRecursively( String plugin, Map< String, Object > map, List< Map< String, Object > > fieldMap );

    /**
     * Gets the dynamic properties.
     *
     * @param path
     *         the path
     *
     * @return the dynamic properties
     */
    JsonNode getDynamicProperties( String path );

    /**
     * Gets the custom flag plugin UI.
     *
     * @param plugin
     *         the plugin
     *
     * @return the custom flag plugin UI
     */
    List< Map< String, Object > > getFieldsFromPluginPath( String plugin );

    /**
     * Gets the workflow by id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow by id
     */
    LatestWorkFlowDTO getWorkflowById( EntityManager entityManager, UUID userId, UUID workflowId );

    /**
     * Gets the tabs view workflow UI.
     *
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the tabs view workflow UI
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getTabsViewWorkflowUI( String workflowId, UUID userId );

    /**
     * Gets the workflow single UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow single UI
     */
    List< TableColumn > getWorkflowSingleUI( String workflowId );

    /**
     * Gets the workflow versions UI.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow versions UI
     */
    List< TableColumn > getWorkflowVersionsUI( String workflowId );

    /**
     * Workflow permission table UI.
     *
     * @return the list
     */
    List< TableColumn > workflowPermissionTableUI();

    /**
     * Show permitted users and groups for object.
     *
     * @param filter
     *         the filter
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     *
     * @return the filtered response
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO filter, UUID workflowId, UUID userId );

    /**
     * Permit permission to work flow.
     *
     * @param checkBox
     *         the check box
     * @param objectId
     *         the object id
     * @param securityId
     *         the security id
     * @param userId
     *         the user id
     *
     * @return true, if successful
     *
     * @apiNote To be used in service calls only
     */
    boolean permitPermissionToWorkFlow( CheckBox checkBox, UUID objectId, UUID securityId, String userId );

    /**
     * Gets the workflow versions.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param filter
     *         the filter
     *
     * @return the workflow versions
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LatestWorkFlowDTO > getWorkflowVersions( UUID userId, UUID workflowId, FiltersDTO filter );

    /**
     * Gets the all workflows.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     *
     * @return the all workflows
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< LatestWorkFlowDTO > getAllWorkflows( UUID userId, UUID projectId, FiltersDTO filter );

    /**
     * Gets the list of workflows UI table columns.
     *
     * @param projectId
     *         the project id
     *
     * @return the list of workflows UI table columns
     */
    List< TableColumn > getListOfWorkflowsUITableColumns( String projectId );

    /**
     * Gets the workflow project by id.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the workflow project by id
     *
     * @apiNote To be used in service calls only
     */
    WorkflowProjectDTO getWorkflowProjectById( UUID userId, UUID workflowId );

    /**
     * Run server job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param masterJobId
     *         the master job id
     * @param masterJobInteger
     *         the master job integer
     * @param masterJobName
     *         the master job name
     */
    void runServerJobForDOE( EntityManager entityManager, UUID userId, JobParameters jobParameters, Map< String, Object > designSummary,
            UUID masterJobId, Integer masterJobInteger, String masterJobName );

    /**
     * Gets the workflow context router.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     *
     * @return the workflow context router
     *
     * @apiNote To be used in service calls only
     */
    Object getWorkflowContextRouter( UUID userIdFromGeneralHeader, String projectId, FiltersDTO filter );

    /**
     * Gets the context.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param token
     *         the token
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the context
     *
     * @apiNote To be used in service calls only
     */
    List< ContextMenuItem > getContext( UUID userIdFromGeneralHeader, String token, String objectId, FiltersDTO filter );

    /**
     * Gets the workflow project tabs UI.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     *
     * @return the workflow project tabs UI
     *
     * @apiNote To be used in service calls only
     */
    SubTabsItem getWorkflowProjectTabsUI( UUID userIdFromGeneralHeader, String projectId );

    /**
     * List data UI.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > listDataUI( String userId, String objectId );

    /**
     * Gets the all sub project.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return the all sub project
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< WorkflowProjectDTO > getAllSubProject( String userId, UUID parentId, FiltersDTO filter );

    /**
     * Gets the workflow project by id.
     *
     * @param userId
     *         the user id string from general header
     * @param projectId
     *         the from string
     *
     * @return the workflow project by id
     *
     * @apiNote To be used in service calls only
     */
    WorkflowProjectDTO getWorkflowProjectById( String userId, UUID projectId );

    /**
     * Gets the object single UI.
     *
     * @param objectId
     *         the object id
     *
     * @return the object single UI
     *
     * @apiNote To be used in service calls only
     */
    List< TableColumn > getObjectSingleUI( String objectId );

    /**
     * Gets the object versions.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the object versions
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< WorkflowProjectDTO > getObjectVersions( UUID userId, UUID projectId, FiltersDTO filtersDTO );

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
     * Adds the to acl.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param parentEntity
     *         the parent entity
     */
    void addToAcl( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity );

    /**
     * Gets the list of workflow project UI table columns.
     *
     * @param objectId
     *         the object id
     *
     * @return the list of workflow project UI table columns
     */
    List< TableColumn > getListOfWorkflowProjectUITableColumns( String objectId );

    /**
     * Creates the project form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the list
     *
     * @apiNote To be used in service calls only
     */
    UIForm createProjectForm( String userId, UUID parentId );

    /**
     * Creates the workflow project.
     *
     * @param userId
     *         the user id
     * @param workflowProjectJson
     *         the workflow project json
     *
     * @return the workflow project DTO
     *
     * @apiNote To be used in service calls only
     */
    WorkflowProjectDTO createWorkflowProject( String userId, String workflowProjectJson );

    /**
     * Save or update object view.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param viewJson
     *         the view json
     * @param isUpdateable
     *         the is updateable
     * @param key
     *         the key
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, String viewJson, boolean isUpdateable, String key );

    /**
     * Sets the object view as default.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     * @param key
     *         the key
     *
     * @return the object view DTO
     *
     * @apiNote To be used in service calls only
     */
    ObjectViewDTO setObjectViewAsDefault( String userId, String objectId, String viewId, String key );

    /**
     * Gets the list of object view.
     *
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param key
     *         the key
     *
     * @return the list of object view
     *
     * @apiNote To be used in service calls only
     */
    List< ObjectViewDTO > getListOfObjectView( String userId, String objectId, String key );

    /**
     * Gets the object view manager.
     *
     * @return the object view manager
     */
    ObjectViewManager getObjectViewManager();

    /**
     * Check if user has workflow license.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    boolean checkIfUserHasWorkflowLicense( UUID userIdFromGeneralHeader );

    /**
     * Check if user has scheme license.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     *
     * @return the object
     *
     * @apiNote To be used in service calls only
     */
    boolean checkIfUserHasSchemeLicense( UUID userIdFromGeneralHeader, String workflowId );

    /**
     * Gets the data object version context.
     *
     * @param objectId
     *         the object id
     * @param filter
     *         the filter
     *
     * @return the data object version context
     */
    List< ContextMenuItem > getDataObjectVersionContext( String objectId, FiltersDTO filter );

    /**
     * Gets the workflow by selection id.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param selectionId
     *         the selection id
     *
     * @return the workflow by selection id
     *
     * @apiNote To be used in service calls only
     */
    LatestWorkFlowDTO getWorkflowBySelectionId( UUID userIdFromGeneralHeader, String selectionId );

    /**
     * Gets the list of work flow DTOUI table columns.
     *
     * @return the list of work flow DTOUI table columns
     */
    List< TableColumn > getListOfWorkFlowDTOUITableColumns();

    /**
     * Gets list of workflows in a workflow project.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return Object
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< Object > getAllObjects( String userId, UUID parentId, FiltersDTO filter );

    /**
     * Execute WF process on server.
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     * @param jobParameters
     *         the job parameters
     */
    void executeWFProcessOnServer( EntityManager entityManager, String uid, JobParameters jobParameters );

    /**
     * Prepare workflow DTO.
     *
     * @param workflowEntity
     *         the workflow entity
     *
     * @return prepareWorkflowDTO
     */
    LatestWorkFlowDTO prepareWorkflowDTO( WorkflowEntity workflowEntity );

    /**
     * Gets the relation list by property.
     *
     * @param id
     *         the id
     *
     * @return the relation list by property
     *
     * @apiNote To be used in service calls only
     */
    boolean getRelationListByProperty( String id );

    /**
     * Creates the relation.
     *
     * @param relationObj
     *         the relation obj
     *
     * @return the relation
     *
     * @apiNote To be used in service calls only
     */
    Relation createRelation( Relation relationObj );

    /**
     * Run server job from web.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param token
     *         the token
     * @param jobParameters
     *         the job parameters
     *
     * @apiNote To be used in service calls only
     */
    void runServerJobFromWeb( UUID userIdFromGeneralHeader, String token, JobParameters jobParameters );

    /**
     * Run scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param uid
     *         the uid
     * @param jobParameters
     *         the job parameters
     * @param category
     *         the category
     * @param schemeEntity
     *         the scheme entity
     *
     * @return the object
     */
    boolean runScheme( EntityManager entityManager, String workflowId, String userIdFromGeneralHeader, String uid,
            JobParameters jobParameters, int category, WFSchemeEntity schemeEntity );

    /**
     * Run server job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     * @param loadcaseName
     *         the loadcase name
     */
    void runServerJob( EntityManager entityManager, UserEntity userId, JobParameters jobParameters, String loadcaseName );

    /**
     * Creates the objective varibale file in staging by workflow id.
     *
     * @param userId
     *         the user id
     * @param wfId
     *         the wf id
     * @param jobId
     *         the job id
     * @param designSummary
     *         the design summary
     * @param runsOn
     *         the runs on
     * @param masterJobId
     *         the master job id
     * @param jobname
     *         the jobname
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    List< String > createDesignVariableFilesInStagingByWorkflowId( UUID userId, String wfId, String jobId,
            Map< String, Object > designSummary, String runsOn, UUID masterJobId, String jobname );

    /**
     * Gets the job parameters by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param wfId
     *         the wf id
     * @param token
     *         the token
     *
     * @return the job parameters by workflow id
     */
    JobParameters getJobParametersByWorkflowId( EntityManager entityManager, String userId, UUID wfId, String token );

    /**
     * Gets the import workflow form.
     *
     * @param id
     *         the id
     *
     * @return the import workflow form
     */
    Object getImportWorkflowForm( String id );

    /**
     * Import workflow.
     *
     * @param userId
     *         the user id
     * @param json
     *         the json
     *
     * @return true, if successful
     *
     * @throws FileNotFoundException
     *         the file not found exception
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     * @apiNote To be used in service calls only
     */
    boolean importWorkflow( String userId, String json ) throws IOException, ParseException;

    /**
     * Gets the workflow validated.
     *
     * @param string
     *         the string
     * @param wfId
     *         the wf id
     * @param versionId
     *         the version id
     * @param token
     *         the token
     *
     * @return the workflow validated
     *
     * @apiNote To be used in service calls only
     */
    WorkflowValidationDTO getWorkflowValidated( String string, UUID wfId, String versionId, String token );

    /**
     * Run server side job.
     *
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     *
     * @apiNote To be used in service and thread calls only
     */
    void runServerSideJob( UUID userId, JobParameters jobParameters );

    /**
     * Run server side job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     */
    void runServerSideJob( EntityManager entityManager, UUID userId, JobParameters jobParameters );

    /**
     * Update job and status.
     *
     * @param entityManager
     *         the entity manager
     * @param masterJobId
     *         the master job id
     * @param errotMsg
     *         the errot msg
     */
    void updateMasterJobAndStatus( EntityManager entityManager, UUID masterJobId, String errotMsg );

    /**
     * Prepare goals.
     *
     * @param id
     *         the id
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    String prepareGoals( String id );

    /**
     * Gets the CB 2 entity list by selection id.
     *
     * @param string
     *         the string
     * @param selectionId
     *         the selection id
     *
     * @return the CB 2 entity list by selection id
     *
     * @apiNote To be used in service calls only
     */
    List< ? > getCB2EntityListBySelectionId( String string, String selectionId );

    /**
     * Move Workflow.
     *
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the src selection id
     * @param targetSelectionId
     *         the target selection id
     *
     * @return boolean
     *
     * @apiNote To be used in service calls only
     */
    boolean moveWorkflow( String userId, UUID srcSelectionId, UUID targetSelectionId );

    /**
     * Prepare work flow in file.
     *
     * @param token
     *         the token
     * @param wfId
     *         the wf id
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    String prepareWorkFlowInFile( String token, UUID wfId );

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    JobManager getJobManager();

    /**
     * Copy assemble and simulate files in staging.
     *
     * @param payload
     *         the payload
     * @param userid
     *         the userid
     *
     * @return the string
     *
     * @apiNote To be used in service calls only
     */
    String copyAssembleAndSimulateFilesInStaging( String payload, UUID userid );

    /**
     * Gets the workflow dao.
     *
     * @return the workflow dao
     */
    WorkflowDAO getWorkflowDao();

    /**
     * Prepare workflow DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param entity
     *         the entity
     *
     * @return the workflow DTO
     */
    WorkflowDTO prepareWorkflowDTO( EntityManager entityManager, UUID userId, WorkflowEntity entity );

    /**
     * Gets the file paths from wf job parameters.
     *
     * @param uid
     *         the uid
     * @param jobParameters
     *         the job parameters
     * @param jobParamDef
     *         the job param def
     * @param jobWFElements
     *         the job WF elements
     *
     * @return the file paths from wf job parameters
     *
     * @apiNote To be used in service calls only
     */
    Set< String > getFilePathsFromWfJobParameters( String uid, JobParameters jobParameters, WorkflowDefinitionDTO jobParamDef,
            List< UserWFElement > jobWFElements );

    /**
     * Gets the scan file by selection id.
     *
     * @param uid
     *         the uid
     * @param selectionId
     *         the selection id
     *
     * @return the scan file by selection id
     *
     * @apiNote To be used in service calls only
     */
    ScanFileDTO getScanFileBySelectionId( String uid, UUID selectionId );

    /**
     * Gets the template scan file by selection id.
     *
     * @param uid
     *         the uid
     * @param selectionId
     *         the selection id
     *
     * @return the template scan file by selection id
     *
     * @apiNote To be used in service calls only
     */
    ScanFileDTO getTemplateScanFileBySelectionId( String uid, UUID selectionId );

    /**
     * Gets list of script names from dynamic_scripts.json
     *
     * @return the script fields from config
     */
    List< WfFieldsUiDTO > getActionScriptFieldsFromConfig();

    /**
     * Gets details of scripts from dynamic_scripts.json
     *
     * @param scriptId
     *         the script id
     *
     * @return the script details from config
     */
    DynamicScript getActionScriptDetailsFromConfig( String scriptId );

    /**
     * Gets python environments from py.env.json.
     *
     * @return the python environments
     */
    List< WfFieldsUiDTO > getPythonEnvironments();

    /**
     * Gets details of scripts from py.env.json
     *
     * @param envName
     *         the env name
     *
     * @return the python environment
     */
    PythonEnvironmentDTO getPythonEnvironment( String envName );

    /**
     * Gets option scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param schemeOptions
     *         the scheme options
     * @param workflowVersionId
     *         the workflow version id
     *
     * @return the option scheme
     */
    Map< String, String > getOptionScheme( EntityManager entityManager, String userId, String workflowId, String schemeOptions,
            int workflowVersionId );

    /**
     * Gets entity manager factory.
     *
     * @return the entity manager factory
     */
    EntityManagerFactory getEntityManagerFactory();

    /**
     * Gets the custom variable data.
     *
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param jsonToObject
     *         the json to object
     *
     * @return the custom variable data
     *
     * @apiNote To be used in service calls only
     */
    FilteredResponse< CustomVariableDTO > getCustomVariableData( String userId, String workflowId, FiltersDTO jsonToObject );

    /**
     * Resubmit interrupted job.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the sus response dto
     */
    JobSubmitResponseDTO resubmitInterruptedJob( String jobParameters );

    /**
     * Duplicate selection selection response ui.
     *
     * @param workflowId
     *         the workflow id
     * @param selectionId
     *         the selection id
     * @param fieldType
     *         the field type
     * @param token
     *         the token
     *
     * @return the selection response ui
     */
    SelectionResponseUI duplicateSelection( String selectionId, String fieldType, String token );

    /**
     * Gets all values for workflow sub project table column.
     *
     * @param projectId
     *         the project id
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for workflow sub project table column
     */
    List< Object > getAllValuesForWorkflowSubProjectTableColumn( String projectId, String columnName, String token );

    /**
     * Gets all values for workflow table column.
     *
     * @param projectId
     *         the project id
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for workflow table column
     */
    List< Object > getAllValuesForWorkflowTableColumn( String projectId, String columnName, String token );

    /**
     * Gets all values for workflow project table column.
     *
     * @param projectId
     *         the project id
     * @param columnName
     *         the column name
     * @param token
     *         the token
     *
     * @return the all values for workflow project table column
     */
    List< Object > getAllValuesForWorkflowProjectTableColumn( String projectId, String columnName, String token );

}