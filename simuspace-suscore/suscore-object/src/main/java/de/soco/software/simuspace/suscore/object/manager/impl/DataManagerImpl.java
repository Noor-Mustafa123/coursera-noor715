package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.threads.RunSchemeDoeThread;
import de.soco.software.simuspace.server.threads.RunSchemeOptimizationThread;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.TransferResult;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.CommonLocationDTO;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.model.TransferObject;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.SearchUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPredictionModelEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.data.model.LibraryDTO;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.model.SusDTO;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.executor.util.SusExecutorUtil;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.manager.PreviewManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectBaseManager;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.suscore.object.model.DeletedObjectDTO;
import de.soco.software.simuspace.suscore.object.threads.RestoreObjectThread;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.simuspace.suscore.object.utility.SIBaseUnitConverter;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.util.JobLog;
import de.soco.software.suscore.jsonschema.model.UnitsFamily;
import de.soco.software.suscore.jsonschema.model.UnitsList;

/**
 * The Class is responsible for providing to methods to save and retrieve the data mainly for projects and objects.
 */
@Log4j2
public class DataManagerImpl extends SuSObjectBaseManager implements DataManager {

    /**
     * The Constant Y_UNITS.
     */
    private static final String Y_UNITS = "Y Units";

    /**
     * The Constant SEPARATOR.
     */
    private static final String SEPARATOR = "SEPARATOR";

    /**
     * The Constant Y_QUANTITY_TYPE.
     */
    private static final String Y_QUANTITY_TYPE = "Y Quantity Type";

    /**
     * The Constant X_QUANTITY_TYPE.
     */
    private static final String X_QUANTITY_TYPE = "X Quantity Type";

    /**
     * The Constant X_UNITS.
     */
    private static final String X_UNITS = "X Units";

    /**
     * The Constant Y_QUANTITY_TYPE.
     */
    private static final String Y_QUANTITY_TYPE_JSON_TYPE = "yDimension";

    /**
     * The Constant X_QUANTITY_TYPE.
     */
    private static final String X_QUANTITY_TYPE_JSON_TYPE = "xDimension";

    /**
     * The Constant X_UNITS.
     */
    private static final String X_UNITS_JSON_TYPE = "xUnit";

    /**
     * The Constant Y_UNITS.
     */
    private static final String Y_UNITS_JSON_TYPE = "yUnit";

    /**
     * The Constant EXPORT_TO.
     */
    private static final String EXPORT_TO = "exportPath";

    /**
     * The Constant SELECT_EXPORT_DIRECTORY.
     */
    private static final String SELECT_EXPORT_DIRECTORY = "Select Export Directory";

    /**
     * The Constant OS_DIRECTORY.
     */
    private static final String OS_DIRECTORY = "os-directory";

    /**
     * The Constant PATH_KEY.
     */
    private static final String PATH_KEY = "path";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant OBJECT_ALREADY_EXISTS_ON_TARGET_LOCATION.
     */
    private static final String OBJECT_ALREADY_EXISTS_ON_TARGET_LOCATION = "Object already exists on target location: ";

    /**
     * The Constant LOCATION_DOES_NOT_EXIST_WITH_ID.
     */
    private static final String LOCATION_DOES_NOT_EXIST_WITH_ID = "Location does not exist with id: ";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN.
     */
    private static final String CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN = "value";

    /**
     * The Constant JOB_NAME_RESTORE_OBJECTS.
     */
    private static final String JOB_NAME_RESTORE_OBJECTS = "Restore objects ";

    /**
     * The Constant RESTORE_DELETED_OBJECT_JOB_DESCRIPTION.
     */
    private static final String RESTORE_DELETED_OBJECT_JOB_DESCRIPTION = "Job to restore deleted object and its dependencies";

    /**
     * The Constant MACHINE.
     */
    private static final String MACHINE = "server";

    /**
     * The Constant SERVER.
     */
    private static final String RUN_MODE = MACHINE;

    /**
     * The Constant INFO.
     */
    private static final String INFO = "Info";

    /**
     * The Constant CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE.
     */
    private static final String CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE = "text";

    /**
     * The Constant LIST_FIRST_INDEX.
     */
    private static final int LIST_FIRST_INDEX = 0;

    /**
     * The Constant FIELD_TYPE_SELECT.
     */
    public static final String FIELD_TYPE_SELECT = "select";

    /**
     * The Constant FIELD_NAME_OBJECT_TYPE_ID.
     */
    public static final String FIELD_NAME_OBJECT_TYPE_ID = "typeId";

    /**
     * The Constant FIELD_NAME_OBJECT_CHANGE_STATUS.
     */
    public static final String FIELD_NAME_OBJECT_CHANGE_STATUS = "includeChilds";

    /**
     * The Constant SELECTED_TRANSLATION_KEY.
     */
    public static final String SELECTED_TRANSLATIONS = "selectedTranslations";

    /**
     * The Constant PARAM_PARENT_ID.
     */
    public static final String PARAM_PARENT_ID = "{parentId}";

    /**
     * The Constant PARAM_TYPE_ID.
     */
    public static final String PARAM_TYPE_ID = "{typeId}";

    /**
     * The Constant PARAM_PARENT_ID.
     */
    public static final String PARAM_OBJECT_ID = "{objectId}";

    /**
     * The Constant FIELD_NAME_OBJECT_STATUS.
     */
    public static final String FIELD_NAME_OBJECT_STATUS = "status";

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant PLUGIN_OBJECT.
     */
    private static final String PLUGIN_OBJECT = "plugin_object";

    /**
     * The Constant INCLUDE_LABEL.
     */
    public static final String INCLUDE_LABEL = "Include Sub-Objects";

    /**
     * The Constant INHERIT_FALSE.
     */
    public static final String INHERIT_FALSE = "0";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "{selectionId}";

    /**
     * The Constant DELETE_OBJECT_COUNT_MESSAGE.
     */
    private static final String DELETE_OBJECT_COUNT_MESSAGE = "object(s) in this container";

    /**
     * The Constant UPDATE.
     */
    private static final String UPDATE = "update";

    /**
     * The Constant LINK_TYPE_YES.
     */
    private static final String LINK_TYPE_YES = "Yes";

    /**
     * The Constant LINK_TYPE_NO.
     */
    private static final String LINK_TYPE_NO = "No";

    /**
     * The Constant IS_LOCATION.
     */
    private static final String IS_LOCATION = "isLocation";

    /**
     * The Constant FILE_NOT_EXISTS_IN_VAULT.
     */
    private static final String FILE_NOT_EXISTS_IN_VAULT = "File does not exist in vault.";

    /**
     * The Constant API_LOCATION_UPLOAD.
     */
    private static final String API_LOCATION_UPLOAD = "/api/core/location/upload";

    /**
     * The Constant API_LOCATION_EXPORT_FILE.
     */
    private static final String API_LOCATION_EXPORT_FILE = "/api/core/location/export/vault/file";

    /**
     * The Constant API_LOCATION_DELETE_FILE.
     */
    private static final String API_LOCATION_DELETE_FILE = "/api/core/location/delete/file";

    /**
     * The Constant API_STOP_JOB
     */
    private static final String API_STOP_JOB = "/api/data/stop/";

    /**
     * The Constant RESTRICTED_USER.
     */
    private static final String RESTRICTED_USER = "Yes";

    /**
     * The Constant Free license Stop Job Mode
     **/
    private static final String FREE_LICENSE = "freeLicense";

    /**
     * The Constant DEFAULT.
     */
    private static final String DEFAULT = "Default";

    /**
     * The Constant LOCATION_TYPE_EXECUTION_ONLY.
     */
    private static final String LOCATION_TYPE_EXECUTION_ONLY = "Execution-only";

    /**
     * The Constant SCRIPT.
     */
    private static final String SCRIPT = "scripts";

    /* ***** Movie Format Constants ***** */

    /**
     * The constant PROJECT_CLASSES.
     */
    private static final List< String > PROJECT_CLASSES = List.of( ProjectDTO.class.getSimpleName(), VariantDTO.class.getSimpleName(),
            LibraryDTO.class.getSimpleName(), WorkflowProjectDTO.class.getSimpleName() );

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The translation DAO.
     */
    private TranslationDAO translationDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The document manager.
     */
    private DocumentManager documentManager;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The thread pool executor service.
     */
    private ThreadPoolExecutorService threadPoolExecutorService;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private LinkDAO linkDao;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The Preview manager.
     */
    private PreviewManager previewManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Inits the method.
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            createHiddenProjectForSchemePlotting( entityManager );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the hidden project for scheme plotting.
     *
     * @param entityManager
     *         the entity manager
     */
    private void createHiddenProjectForSchemePlotting( EntityManager entityManager ) {
        ProjectEntity entity = new ProjectEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.fromString( ConstantsID.HIDDEN_PROJECT_ID_FOR_SCHEME_PLOTTING ), 1 ) );
        entity.setName( "_proj_contain_hidden_scheme_plot_" );
        entity.setDescription( "This hidden container contains hidden plots which will be deleted after some time permanently" );
        entity.setConfig( "Unrestricted" );
        entity.setCreatedOn( new Date() );
        entity.setLifeCycleStatus( "d762f4ef-e706-4a44-a46d-6b334745e2e5" );
        entity.setPath( "/Data" );
        entity.setType( "Data" );
        entity.setHidden( true );
        entity.setTypeId( UUID.fromString( ProjectEntity.CLASS_ID.toString() ) );
        susDAO.saveOrUpdate( entityManager, entity );
    }

    /**
     * Prepares job model.
     *
     * @param entityManager
     *         the entity manager
     * @param jobName
     *         the job name
     * @param description
     *         the description
     * @param workflow
     *         the workflow
     *
     * @return the job
     */
    private Job prepareJobModel( EntityManager entityManager, String jobName, String description, SystemWorkflow workflow ) {
        Status status = new Status( WorkflowStatus.RUNNING );

        final Job jobImpl = new JobImpl();
        final LocationDTO locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        jobImpl.setDescription( description );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( UUID.fromString( workflow.getId() ) );
        jobImpl.setWorkflowName( workflow.getName() );
        jobImpl.setRunMode( RUN_MODE );
        jobImpl.setOs( OSValidator.getOperationSystemName() );
        jobImpl.setRunsOn( locationDTO );

        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            log.error( "Machine Name Error: ", e );
        }

        jobImpl.setStatus( status );
        jobImpl.setWorkflowVersion( new VersionDTO( SusConstantObject.DEFAULT_VERSION_NO ) );

        return jobImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowProjectDTO updateWorkFlowProject( String token, String workflowProjectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            SuSEntity existingEntity;
            SuSEntity updateEntity;
            WorkflowProjectDTO workflowProjectDto = null;
            WorkflowProjectDTO projectDTO = JsonUtils.jsonToObject( workflowProjectJson, WorkflowProjectDTO.class );
            UserDTO user = TokenizedLicenseUtil.getNotNullUser( token );
            if ( projectDTO != null ) {
                Notification notif = projectDTO.validate();
                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }

                updateEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( projectDTO, user.getId() );
                existingEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectDTO.getId() );
                if ( null == existingEntity ) {
                    throw new SusException(
                            MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), projectDTO.getId() ) );
                }
                checkUserUpdatePermission( entityManager, token, existingEntity );
                prepareCommonAttributesForUpdateEntity( existingEntity, updateEntity );
                final SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( projectDTO.getTypeId().toString(),
                        updateEntity.getConfig() );
                if ( !isLifeCycleStatusAllowd( updateEntity ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), projectDTO.getId() ) );
                }

                List< SuSEntity > parents = susDAO.getParents( entityManager,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectDTO.getId() ) );

                if ( CollectionUtils.isNotEmpty( parents ) && !isNameUniqueAmongSiblings( entityManager, projectDTO.getName(), updateEntity,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                                parents.get( LIST_FIRST_INDEX ).getComposedId().getId() ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
                }

                prepareUpdatedEntity( entityManager, user.getId(), workflowProjectJson, updateEntity, suSObjectModel );
                changeStatusInVersionsRecursively( entityManager, updateEntity );
                updateEntity = susDAO.updateAnObject( entityManager, updateEntity );
                saveOrUpdateIndexEntity( entityManager, suSObjectModel, updateEntity );
                selectionManager.sendCustomerEvent( entityManager, user.getId(), updateEntity, UPDATE );
                workflowProjectDto = createWorkflowProjectDTOFromWorkflowProjectEntity( updateEntity, true );
                if ( null != workflowProjectDto ) {
                    workflowProjectDto.setCustomAttributesDTO( projectDTO.getCustomAttributesDTO() );
                }

            }
            return workflowProjectDto;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is life cycle status allowd.
     *
     * @param updateEntity
     *         the update entity
     *
     * @return true, if is life cycle status allowd
     */
    private boolean isLifeCycleStatusAllowd( SuSEntity updateEntity ) {
        StatusConfigDTO statusConfig = lifeCycleManager.getLifeCycleStatusByStatusId( updateEntity.getLifeCycleStatus() );
        return ( statusConfig.isAllowChanges() );

    }

    /**
     * Prepare updated entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowProjectJson
     *         the workflow project json
     * @param updateEntity
     *         the update entity
     * @param suSObjectModel
     *         the su s object model
     */
    private void prepareUpdatedEntity( EntityManager entityManager, String userId, String workflowProjectJson, SuSEntity updateEntity,
            SuSObjectModel suSObjectModel ) {
        WorkflowProjectDTO projectDTO = JsonUtils.jsonToObject( workflowProjectJson, WorkflowProjectDTO.class );
        projectDTO.setCustomAttributesDTO(
                configManager.getObjectTypeByIdAndConfigName( updateEntity.getTypeId().toString(), updateEntity.getConfig() )
                        .getCustomAttributes() );
        updateEntity.setCustomAttributes(
                SusDTO.prepareCustomAttributes( updateEntity, projectDTO.getCustomAttributes(), suSObjectModel.getCustomAttributes() ) );
        updateEntity.setIcon( suSObjectModel.getIcon() );
        updateEntity.setModifiedOn( new Date() );
        updateEntity.setName( projectDTO.getName() );
        updateEntity.setDescription( projectDTO.getDescription() );
        updateEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) ) );
        updateEntity.setTranslation( setMultiNames( entityManager, workflowProjectJson ) );
        try {
            updateEntity.setSelectedTranslations(
                    JsonUtils.toJson( new ObjectMapper().readTree( workflowProjectJson ).path( SELECTED_TRANSLATIONS ) ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
        // audit log
        ContainerEntity oldEntity = ( ContainerEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                projectDTO.getId() );
        if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
            AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, oldEntity, updateEntity );
            if ( null != auditLog ) {
                auditLog.setObjectId( oldEntity.getComposedId().getId().toString() );
            }
            updateEntity.setAuditLogEntity( auditLog );
        }
        StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( updateEntity.getTypeId().toString(), updateEntity.getConfig() );
        updateEntity.setLifeCycleStatus( dto.getId() );
    }

    /**
     * Sets the multiple names and files.
     *
     * @param entityManager
     *         the entity manager
     * @param json
     *         the object json
     *
     * @return the list
     */
    private Set< TranslationEntity > setMultiNames( EntityManager entityManager, String json ) {
        Set< TranslationEntity > entityList = new HashSet<>();
        for ( String lang : PropertiesManager.getRequiredlanguages().keySet() ) {
            try {
                TranslationEntity entity = new TranslationEntity();
                entity.setId( UUID.randomUUID() );
                entity.setLanguage( lang );
                entity.setName( JsonUtils.getValue( json, "name-" + lang ) );
                DocumentDTO savedDocument = JsonUtils.jsonToObject(
                        JsonUtils.toJson( new ObjectMapper().readTree( json ).path( "file-" + lang ) ), DocumentDTO.class );
                if ( null != savedDocument ) {
                    entity.setFile( documentManager.getDocumentEntityById( entityManager, UUID.fromString( savedDocument.getId() ) ) );
                }
                translationDAO.saveOrUpdate( entityManager, entity );
                entityList.add( entity );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e );
            }
        }
        return entityList;
    }

    /**
     * Creates the index entity.
     *
     * @param entityManager
     *         the entity manager
     * @param susObjectModel
     *         the susObjectModel
     * @param entity
     *         the entity
     */
    private void saveOrUpdateIndexEntity( EntityManager entityManager, SuSObjectModel susObjectModel, SuSEntity entity ) {
        try {
            if ( PropertiesManager.enableElasticSearch() && CommonUtils.validateURL() ) {
                final Map< String, String > requestHeaders = new HashMap<>();
                requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
                requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );

                var matchingPair = SearchUtil.getIndexMap().values().stream()
                        .filter( pair -> susObjectModel.getName().equals( pair.name() ) ).findFirst().orElse( null );
                if ( matchingPair != null ) {
                    int index = matchingPair.index();
                    String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + index + "/_doc/"
                            + entity.getComposedId().getId();
                    SuSClient.postRequest( url, JsonUtils.toJson(
                                    createGenericDTOFromObjectEntity( entityManager, null, null, entity, new ArrayList<>(), false ) ),
                            requestHeaders );
                }

            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Delete index.
     *
     * @param entity
     *         the entity
     */
    @Override
    public void deleteIndex( SuSEntity entity ) {
        try {
            if ( PropertiesManager.enableElasticSearch() && CommonUtils.validateURL() ) {
                SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                        entity.getConfig() );
                final Map< String, String > requestHeaders = new HashMap<>();
                requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
                requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );

                var matchingPair = SearchUtil.getIndexMap().values().stream()
                        .filter( pair -> susObjectModel.getName().equals( pair.name() ) ).findFirst().orElse( null );
                if ( matchingPair != null ) {
                    int index = matchingPair.index();
                    String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + index + "/_doc/"
                            + entity.getComposedId().getId();
                    SuSClient.deleteRequest( url, requestHeaders );
                }
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > verifyPermissionsAndGetDeleteObjectListBulk( EntityManager entityManager, String userId, String selectionId ) {
        List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );

        List< UUID > editablefiles = new ArrayList<>();
        for ( UUID uuid : selectedObjects ) {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, uuid );
            if ( isEditable( susEntity, userId ) ) {
                editablefiles.add( susEntity.getComposedId().getId() );
            }
        }

        List< SuSEntity > susEntityList = new ArrayList<>();
        for ( UUID uuid : selectedObjects ) {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, uuid );
            for ( UUID removeSelected : editablefiles ) {
                if ( uuid.equals( removeSelected ) ) {
                    susEntityList.add( susEntity );
                }
            }
        }

        for ( SuSEntity suSEntity : susEntityList ) {
            List< SuSEntity > nonPermittedObjects = getChildObjectCount( entityManager, userId, suSEntity.getComposedId().getId() );
            if ( !nonPermittedObjects.isEmpty() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DELETE.getKey(),
                        ConstantsString.SPACE + nonPermittedObjects.size() + ConstantsString.SPACE + DELETE_OBJECT_COUNT_MESSAGE ) );
            }
        }

        List< SuSEntity > susEntitiesToDelete = new ArrayList<>();

        for ( SuSEntity objectId : susEntityList ) {
            List< SuSEntity > allChildEntities = new ArrayList<>();
            populateAllChildEntitiesVersions( entityManager, userId, objectId.getComposedId().getId(), allChildEntities );
            susEntitiesToDelete.addAll( allChildEntities );
        }

        return susEntitiesToDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SuSEntity > verifyPermissionsAndGetDeleteObjectList( EntityManager entityManager, String userId, UUID objectId ) {
        isPermittedToDelete( entityManager, userId, objectId.toString() );
        List< SuSEntity > nonPermittedObjects = getChildObjectCount( entityManager, userId, objectId );
        if ( !nonPermittedObjects.isEmpty() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DELETE.getKey(),
                    ConstantsString.SPACE + nonPermittedObjects.size() + ConstantsString.SPACE + DELETE_OBJECT_COUNT_MESSAGE ) );
        }
        List< SuSEntity > objectsToDelete = new ArrayList<>();
        populateAllChildEntitiesVersions( entityManager, userId, objectId, objectsToDelete );
        return objectsToDelete;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopJobExecution( String userId, String userName, String jobId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return stopJobExecution( entityManager, userId, userName, jobId, mode );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopJobExecution( EntityManager entityManager, String userId, String userName, String jobId, String mode ) {
        boolean isStop = false;
        boolean skipPermissions = StringUtils.equals( mode, FREE_LICENSE );
        // Stop Jobs to free license hence skip job permission check.
        Job stopJob = jobManager.getJob( entityManager, UUID.fromString( userId ), jobId );
        if ( stopJob != null ) {
            if ( !skipPermissions && ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                    stopJob.getId().toString() + ConstantsString.COLON + PermissionMatrixEnum.KILL.getValue() ) ) ) {
                throw new SusException( "You do not have Permission to stop the Job" );
            }
            if ( null != stopJob.getRunsOn() && null != stopJob.getRunsOn().getUrl() && !stopJob.getRunsOn().getUrl().isEmpty()
                    && PropertiesManager.getLocationURL() != null ) {
                if ( StringUtils.equals( getHostName( stopJob.getRunsOn().getUrl() ),
                        getHostName( PropertiesManager.getLocationURL() ) ) ) {
                    isStop = abortLocalJob( entityManager, userId, userName, stopJob );
                } else {
                    // remote job
                    Map< String, String > requestHeaders = CommonUtils.prepareHeadersWithAuthToken( stopJob.getRunsOn().getAuthToken() );
                    requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, stopJob.getRequestHeaders().getJobAuthToken() );
                    try {
                        SusResponseDTO response = SuSClient.getRequest( stopJob.getRunsOn().getUrl() + API_STOP_JOB + stopJob.getId(),
                                requestHeaders );
                        if ( MessageBundleFactory.getMessage( Messages.JOB_STOPPED.getKey() ).equals( response.getData() ) ) {
                            isStop = true;
                        }
                    } catch ( Exception e ) {
                        // Safer approach; update job status to aborted if could not kill on remote
                        isStop = abortLocalJob( entityManager, userId, userName, stopJob );
                        log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_KILL_REMOTE_JOB_PROCESS.getKey(),
                                stopJob.getRunsOn().getUrl() ), e );
                    }
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_DETERMINE_JOBS_RUNNING_LOCATION.getKey() ) );
            }

        }

        return isStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseJobExecution( String userId, String userName, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // Stop Jobs to free license hence skip job permission check.
        Job jobToPause = jobManager.getJob( entityManager, UUID.fromString( userId ), jobId );
        if ( jobToPause == null ) {
            return;
        }
        if ( ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                jobToPause.getId().toString() + ConstantsString.COLON + PermissionMatrixEnum.KILL.getValue() ) ) ) {
            throw new SusException( "You do not have Permission to pause the Job" );
        }
        if ( null != jobToPause.getRunsOn() && null != jobToPause.getRunsOn().getUrl() && !jobToPause.getRunsOn().getUrl().isEmpty()
                && PropertiesManager.getLocationURL() != null ) {
            if ( StringUtils.equals( getHostName( jobToPause.getRunsOn().getUrl() ), getHostName( PropertiesManager.getLocationURL() ) ) ) {
                pauseLocalJob( entityManager, jobToPause );
            } else {
                throw new SusException( "can not pause remote jobs" );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_DETERMINE_JOBS_RUNNING_LOCATION.getKey() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resumeJobExecution( String userId, String userName, String jobId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // Stop Jobs to free license hence skip job permission check.
        Job jobToResume = jobManager.getJob( entityManager, UUID.fromString( userId ), jobId );
        if ( jobToResume == null ) {
            return;
        }
        if ( ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                jobToResume.getId().toString() + ConstantsString.COLON + PermissionMatrixEnum.KILL.getValue() ) ) ) {
            throw new SusException( "You do not have Permission to resume the Job" );
        }
        if ( null != jobToResume.getRunsOn() && null != jobToResume.getRunsOn().getUrl() && !jobToResume.getRunsOn().getUrl().isEmpty()
                && PropertiesManager.getLocationURL() != null ) {
            if ( StringUtils.equals( getHostName( jobToResume.getRunsOn().getUrl() ),
                    getHostName( PropertiesManager.getLocationURL() ) ) ) {
                resumeLocalJob( entityManager, jobToResume );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), "remote" ) );
            }
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_DETERMINE_JOBS_RUNNING_LOCATION.getKey() ) );
        }
    }

    /**
     * Gets host name.
     *
     * @param url
     *         the url
     *
     * @return the host name
     */
    private static String getHostName( String url ) {
        try {
            return ( new URL( url ).getHost() );
        } catch ( MalformedURLException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Abort local job boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param jobToPause
     *         the stop job
     */
    private void pauseLocalJob( EntityManager entityManager, Job jobToPause ) {
        if ( jobToPause.getJobType() == JobTypeEnums.SCHEME.getKey()
                && jobToPause.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.SCHEME.getValue() ) );

        } else if ( jobToPause.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.VARIANT.getValue() ) );

        } else if ( isSystemJob( jobToPause.getWorkflowId().toString() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.SYSTEM.getValue() ) );
        } else {
            pauseWorkflowJobExecution( entityManager, jobToPause );
        }
    }

    /**
     * Abort local job boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param jobToResume
     *         the stop job
     */
    private void resumeLocalJob( EntityManager entityManager, Job jobToResume ) {
        if ( jobToResume.getJobType() == JobTypeEnums.SCHEME.getKey()
                && jobToResume.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.SCHEME.getValue() ) );

        } else if ( jobToResume.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.VARIANT.getValue() ) );

        } else if ( isSystemJob( jobToResume.getWorkflowId().toString() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), JobTypeEnums.SYSTEM.getValue() ) );
        } else {
            resumeWorkflowJobExecution( entityManager, jobToResume );
        }
    }

    /**
     * Stop workflow job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowJob
     *         the job
     */
    private void resumeWorkflowJobExecution( EntityManager entityManager, Job workflowJob ) {
        if ( workflowJob.getStatus().getId() == WorkflowStatus.PAUSED.getKey() ) {
            ProcessResult processResult = null;
            if ( OSValidator.isWindows() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), "windows" ) );
            } else if ( OSValidator.isUnix() && StringUtils.equals( getHostName( workflowJob.getRunsOn().getUrl() ),
                    getHostName( PropertiesManager.getLocationURL() ) ) ) {
                boolean isForChildProcess = true;
                processResult = LinuxUtils.resumeCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null,
                        !isForChildProcess );
                LinuxUtils.resumeCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null, isForChildProcess );
            }
            if ( processResult != null && processResult.getExitValue() == 0 && checkIfJobIsRunningAfterResumption( workflowJob ) ) {

                workflowJob.setStatus( new Status( WorkflowStatus.RUNNING ) );
                JobLog.setJob( workflowJob );
                workflowJob.setCompletionTime( new Date() );
                JobLog.addLog( workflowJob.getId(),
                        new LogRecord( ConstantsMessageTypes.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_RESUMED ) ) );
                jobManager.updateJob( entityManager, workflowJob );
            } else {
                workflowJob.setStatus( new Status( WorkflowStatus.FAILED ) );
                JobLog.setJob( workflowJob );
                workflowJob.setCompletionTime( new Date() );
                JobLog.addLog( workflowJob.getId(), new LogRecord( ConstantsMessageTypes.INFO, "Failed to resume job" ) );
                JobLog.addLog( workflowJob.getId(),
                        new LogRecord( ConstantsMessageTypes.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_FAILED ) ) );
                jobManager.updateJob( entityManager, workflowJob );
                expireJobToken( entityManager, workflowJob );
                throw new SusException( "Failed to resume job" );
            }
        }
    }

    /**
     * Check if job is running boolean.
     *
     * @param workflowJob
     *         the workflow job
     *
     * @return true if job's process is running, false otherwise
     */
    private boolean checkIfJobIsRunningAfterResumption( Job workflowJob ) {
        try {
            Thread.sleep( 200 );
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        }
        return LinuxUtils.killCommand( workflowJob.getCreatedBy().getUserUid(), "-0", workflowJob.getJobProcessId(), null, false )
                .getExitValue() == 0;
    }

    /**
     * Stop workflow job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowJob
     *         the job
     */
    private void pauseWorkflowJobExecution( EntityManager entityManager, Job workflowJob ) {
        if ( workflowJob.getStatus().getId() == WorkflowStatus.RUNNING.getKey() ) {
            if ( OSValidator.isWindows() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CAN_NOT_PAUSE_RESUME_TYPE_JOB.getKey(), "windows" ) );
            }
            ProcessResult result = null;
            if ( StringUtils.equals( getHostName( workflowJob.getRunsOn().getUrl() ),
                    getHostName( PropertiesManager.getLocationURL() ) ) ) {
                boolean forChildProcesses = true;
                LinuxUtils.pauseCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null, forChildProcesses );
                result = LinuxUtils.pauseCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null,
                        !forChildProcesses );
            }
            if ( result != null && result.getExitValue() == 0 ) {
                workflowJob.setStatus( new Status( WorkflowStatus.PAUSED ) );
                JobLog.setJob( workflowJob );
                workflowJob.setCompletionTime( new Date() );
                JobLog.addLog( workflowJob.getId(),
                        new LogRecord( ConstantsMessageTypes.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_PAUSED ) ) );
                jobManager.updateJob( entityManager, workflowJob );
            } else {
                throw new SusException( "Failed to Pause job. See logs for details" );
            }
        }
    }

    /**
     * Abort local job boolean.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param userName
     *         the user name
     * @param stopJob
     *         the stop job
     *
     * @return the boolean
     */
    private boolean abortLocalJob( EntityManager entityManager, String userId, String userName, Job stopJob ) {

        boolean isStop = false;

        if ( stopJob.getJobType() == JobTypeEnums.SCHEME.getKey()
                && stopJob.getJobRelationType() == JobRelationTypeEnums.MASTER.getKey() ) {
            isStop = stopSchemeJobExecution( entityManager, stopJob );

        } else if ( stopJob.getJobType() == JobTypeEnums.VARIANT.getKey() ) {
            isStop = stopVariantJobExecution( entityManager, stopJob );

        } else if ( isSystemJob( stopJob.getWorkflowId().toString() ) ) {
            List< Job > jobs = jobManager.getAllChildrensOfMasterJob( entityManager, stopJob );
            if ( CollectionUtils.isNotEmpty( jobs ) ) {
                for ( Job job : jobs ) {
                    isStop = stopSystemJobExecution( entityManager, userName, job );
                }
            } else {
                Job job = jobManager.getJob( entityManager, UUID.fromString( userId ), stopJob.getId().toString() );
                isStop = stopSystemJobExecution( entityManager, userName, job );
            }
        } else {
            stopWorkflowJobExecution( entityManager, stopJob, true );
            isStop = true;
        }
        return isStop;
    }

    /**
     * Expire job token.
     *
     * @param entityManager
     *         the entity manager
     * @param stopJob
     *         the job
     */
    private void expireJobToken( EntityManager entityManager, Job stopJob ) {
        JobTokenEntity tokenJob = tokenManager.getJobTokenDAO()
                .getJobTokenEntityByJobToken( entityManager, stopJob.getRequestHeaders().getJobAuthToken() );
        if ( tokenJob != null ) {
            tokenJob.setExpired( true );
            tokenManager.getJobTokenDAO().updateJobTokenEntity( entityManager, tokenJob );
        }
    }

    /**
     * Stop workflow job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowJob
     *         the job
     * @param expireToken
     *         the expire token
     */
    private void stopWorkflowJobExecution( EntityManager entityManager, Job workflowJob, boolean expireToken ) {
        if ( workflowJob.getStatus().getId() == WorkflowStatus.RUNNING.getKey()
                || workflowJob.getStatus().getId() == WorkflowStatus.PAUSED.getKey() ) {
            if ( OSValidator.isWindows() ) {
                LinuxUtils.runCommand( ( "taskkill /F /pid " + workflowJob.getJobProcessId() ).split( ConstantsString.SPACE ), true, null );
            } else if ( OSValidator.isUnix() && StringUtils.equals( getHostName( workflowJob.getRunsOn().getUrl() ),
                    getHostName( PropertiesManager.getLocationURL() ) ) ) {
                // this check avoids the risk of killing a random process if job is not running on this machine
                // kill job process
                LinuxUtils.killCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null, false );
                // kill all child processes
                LinuxUtils.killCommand( workflowJob.getCreatedBy().getUserUid(), workflowJob.getJobProcessId(), null, true );
            }
            workflowJob.setStatus( new Status( WorkflowStatus.ABORTED ) );
            JobLog.setJob( workflowJob );
            workflowJob.setCompletionTime( new Date() );
            JobLog.addLog( workflowJob.getId(),
                    new LogRecord( ConstantsMessageTypes.INFO, MessagesUtil.getMessage( WFEMessages.WORKFLOW_ABORTED ) ) );
            JobLog.addLog( workflowJob.getId(), new LogRecord( ConstantsMessageTypes.INFO,
                    MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( workflowJob ) ) ) );
            jobManager.updateJob( entityManager, workflowJob );

            deleteScriptFiles( workflowJob );
        }

        if ( expireToken && workflowJob.getJobRelationType() != JobRelationTypeEnums.CHILD.getKey() ) {
            expireJobToken( entityManager, workflowJob );
        }
    }

    /**
     * Gets execution time.
     *
     * @param jobImpl
     *         jobImpl
     *
     * @return job execution time
     */
    private String getExecutionTime( Job jobImpl ) {

        if ( jobImpl.getCompletionTime() == null ) {
            jobImpl.setCompletionTime( new Date() );
        }
        long timeDifference = jobImpl.getCompletionTime().getTime() - jobImpl.getSubmitTime().getTime();
        long sec = ( timeDifference / 1000 ) % 60;
        long min = ( timeDifference / ( 1000 * 60 ) ) % 60;
        long hour = ( timeDifference / ( 1000 * 60 * 60 ) ) % 24;
        return hour + "h:" + min + "m:" + sec + "s";
    }

    /**
     * Deletes script files from job staging.
     *
     * @param job
     *         the job
     */
    private void deleteScriptFiles( Job job ) {
        try {
            String jobScriptDirPath = job.getWorkingDir().getPath() + File.separator + SCRIPT;
            LinuxUtils.deleteFileOrDirByPath( job.getCreatedBy().getUserUid(), jobScriptDirPath );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Stop job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param userName
     *         the user name
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    private boolean stopSystemJobExecution( EntityManager entityManager, String userName, Job job ) {
        boolean isStop = SusExecutorUtil.threadPoolExecutorService.stopJobExecution( userName, job.getId() );
        setJobStatusToAborted( entityManager, job );

        return isStop;
    }

    /**
     * Stop scheme job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    private boolean stopSchemeJobExecution( EntityManager entityManager, Job job ) {
        Runnable schemeJobThread = SusExecutorUtil.threadPoolExecutorService.getThreadMap().get( job.getId() );

        if ( schemeJobThread instanceof RunSchemeDoeThread runSchemeDoeThread ) {
            runSchemeDoeThread.stop();
            expireJobToken( entityManager, job );
        } else if ( schemeJobThread instanceof RunSchemeOptimizationThread runSchemeOptimizationThread ) {
            runSchemeOptimizationThread.stop();
        }
        stopChildJobsOfJob( entityManager, job );
        setJobStatusToAborted( entityManager, job );

        return true;
    }

    /**
     * Stop child jobs.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     */
    private void stopChildJobsOfJob( EntityManager entityManager, Job job ) {
        List< Job > jobs = jobManager.getAllChildrensOfMasterJob( entityManager, job );
        if ( CollectionUtils.isNotEmpty( jobs ) ) {
            for ( Job childJob : jobs ) {
                stopWorkflowJobExecution( entityManager, childJob, false ); // stopping running child jobs
            }
        }
    }

    /**
     * Set Job Status to Aborted.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     */
    private void setJobStatusToAborted( EntityManager entityManager, Job job ) {
        if ( !WorkflowStatus.COMPLETED.getValue().equals( job.getStatus().getName() ) ) {
            job.setStatus( new Status( WorkflowStatus.ABORTED ) );
            jobManager.updateJob( entityManager, job );
        }
    }

    /**
     * Stop variant job execution.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     *
     * @return true, if successful
     */
    private boolean stopVariantJobExecution( EntityManager entityManager, Job job ) {
        List< Job > jobs = jobManager.getAllChildrensOfMasterJob( entityManager, job );
        if ( CollectionUtils.isNotEmpty( jobs ) ) {
            for ( Job childJob : jobs ) {
                stopWorkflowJobExecution( entityManager, childJob, false ); // stopping running child jobs
            }
        }

        setJobStatusToAborted( entityManager, job );
        expireJobToken( entityManager, job );

        return true;
    }

    /**
     * Checks if is system job.
     *
     * @param workflowId
     *         the workflow id
     *
     * @return true, if is system job
     */
    private boolean isSystemJob( String workflowId ) {

        for ( SystemWorkflow s : SystemWorkflow.values() ) {
            if ( s.getId().equals( workflowId ) ) {
                return true;
            }
        }

        return false;

    }

    /**
     * Gets the child object count.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     *
     * @return the child object count
     */
    private List< SuSEntity > getChildObjectCount( EntityManager entityManager, String userId, UUID objectId ) {
        List< SuSEntity > nonPermittedObjects = new ArrayList<>();
        SuSEntity object = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                object.getComposedId().getId().toString() + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            nonPermittedObjects.add( object );
        }
        List< SuSEntity > childrenEntities = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                object.getComposedId().getId() );
        if ( CollectionUtil.isNotEmpty( childrenEntities ) ) {
            for ( SuSEntity childEntity : childrenEntities ) {
                nonPermittedObjects.addAll( getChildObjectCount( entityManager, userId, childEntity.getComposedId().getId() ) );
            }
        }
        return nonPermittedObjects;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObject( EntityManager entityManager, String userId, SuSEntity susEntity ) {
        List< SuSEntity > susEntityVersions = susDAO.getObjectVersionListById( entityManager, SuSEntity.class,
                susEntity.getComposedId().getId() );
        SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                susEntity.getConfig() );
        susEntityVersions.forEach( suSEntity -> {
            if ( !suSEntity.isDelete() && suSEntity.getComposedId().getVersionId() == susEntity.getComposedId().getVersionId() ) {
                suSEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity( "Delete object : " + susEntity.getName(),
                        ConstantsDbOperationTypes.DELETED, userId, susEntity, susObjectModel.getName() ) );
            }
            suSEntity.setDelete( Boolean.TRUE );
            suSEntity.setDeletedBy( userCommonManager.getUserCommonDAO().findById( entityManager, UUID.fromString( userId ) ) );
            suSEntity.setDeletedOn( new Date() );
            suSEntity.setModifiedOn( new Date() );
        } );
        susDAO.saveOrUpdateBulk( entityManager, susEntityVersions );
        return true;
    }

    /**
     * Populate all child entities versions.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param allChildEntities
     *         the all child entities
     */
    private void populateAllChildEntitiesVersions( EntityManager entityManager, String userId, UUID objectId,
            List< SuSEntity > allChildEntities ) {
        /* Need permission and license checks **/
        SuSEntity object = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        isPermittedToDelete( entityManager, userId, object.getComposedId().getId().toString() );
        List< SuSEntity > childrenEntities = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                object.getComposedId().getId() );
        if ( CollectionUtil.isNotEmpty( childrenEntities ) ) {
            for ( SuSEntity childEntity : childrenEntities ) {
                populateAllChildEntitiesVersions( entityManager, userId, childEntity.getComposedId().getId(), allChildEntities );
            }
        }
        allChildEntities.add( object );
    }

    /**
     * Gets the document locations.
     *
     * @param file
     *         the file
     *
     * @return the document locations
     */
    private static List< LocationDTO > getDocumentLocations( DocumentEntity file ) {
        List< LocationDTO > locationDTOs = new ArrayList<>();
        for ( LocationEntity location : ( file.getLocations() ) ) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId( location.getId() );
            locationDTO.setName( location.getName() );
            locationDTO.setUrl( location.getUrl() );
            locationDTO.setAuthToken( location.getAuthToken() );
            locationDTOs.add( locationDTO );
        }
        return locationDTOs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities, boolean forList ) {
        GenericDTO genericDTO = new GenericDTO();
        if ( susEntity != null ) {
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setParentId( projectId );
            genericDTO.setDescription( susEntity.getDescription() );
            genericDTO.setTypeId( susEntity.getTypeId() );
            genericDTO.setAutoDeleted( susEntity.isAutoDelete() );
            genericDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
            SuSObjectModel susobject = null;
            if ( susEntity.getTypeId() != null && susEntity.getConfig() != null ) {
                susobject = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() );
                if ( susobject != null ) {
                    genericDTO.setLifeCycleStatus( lifeCycleManager.getStatusByLifeCycleNameAndStatusId( susobject.getLifeCycle(),
                            susEntity.getLifeCycleStatus() ) );
                    genericDTO.setType( susobject.getName() );
                }
                genericDTO.setIcon( susEntity.getIcon() );
            }
            setGenericDtoUrlType( susEntity, genericDTO, susobject );
            genericDTO.setLink(
                    CollectionUtils.isNotEmpty( linkDao.getLinkedRelationByChildId( entityManager, susEntity.getComposedId().getId() ) )
                            ? LINK_TYPE_YES : LINK_TYPE_NO );

            if ( null != susEntity.getCreatedBy() ) {
                genericDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                genericDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            if ( !forList ) {
                genericDTO.setUsers(
                        null != susEntity.getUserSelectionId() ? selectionManager.getSelectedIdsListBySelectionId( entityManager,
                                susEntity.getUserSelectionId() ) : new ArrayList<>() );
                genericDTO.setGroups(
                        null != susEntity.getGroupSelectionId() ? selectionManager.getSelectedIdsListBySelectionId( entityManager,
                                susEntity.getGroupSelectionId() ) : new ArrayList<>() );
            }
        }
        return genericDTO;
    }

    /**
     * Sets generic dto url type.
     *
     * @param susEntity
     *         the sus entity
     * @param genericDTO
     *         the generic dto
     * @param susObjectModel
     *         the susObjectModel
     */
    private void setGenericDtoUrlType( SuSEntity susEntity, GenericDTO genericDTO, SuSObjectModel susObjectModel ) {
        if ( susEntity instanceof WorkflowEntity ) {
            genericDTO.setUrlType( ConstantsString.WORKFLOW_KEY );
            genericDTO.setAutoDeleted( false );
        } else if ( susEntity instanceof WorkflowProjectEntity || susObjectModel.getClassName()
                .contains( WorkflowProjectDTO.class.getSimpleName() ) ) {
            genericDTO.setUrlType( ConstantsString.WORKFLOW_PROJECT_KEY );
        } else if ( susEntity instanceof ContainerEntity || PROJECT_CLASSES.stream()
                .anyMatch( clazz -> susObjectModel.getClassName().contains( clazz ) ) ) {
            genericDTO.setUrlType( ConstantsString.PROJECT_KEY );
        } else {
            genericDTO.setUrlType( ConstantsString.OBJECT_KEY );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntity ) {
        return createGenericDTOFromObjectEntity( entityManager, userId, projectId, susEntity, translationEntity, true );
    }

    /**
     * Checks if is editable.
     *
     * @param susEntity
     *         the sus entity
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return true, if is editable
     */
    private boolean isEditable( SuSEntity susEntity, String userIdFromGeneralHeader ) {

        if ( !isLifeCycleStatusAllowd( susEntity ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), susEntity.getComposedId().getId() ) );
        }
        if ( susEntity instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getCheckedOut() ) {
            return ( dataObjectEntity.getCheckedOutUser().getId().equals( UUID.fromString( userIdFromGeneralHeader ) ) );
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreObjectBySelection( String userId, String selectionId, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Job restoreJob = prepareRestoreJob( entityManager, userId );
            restoreJob.setJobType( JobTypeEnums.SYSTEM.getKey() );
            jobManager.saveJobIds( entityManager, restoreJob.getId() );

            RestoreObjectThread workflowRunnable = new RestoreObjectThread( userId, restoreJob, selectionId, mode, jobManager, this,
                    entityManagerFactory );
            threadPoolExecutorService.deleteExecute( restoreJob.getId(), workflowRunnable );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare Restore Job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the job
     */
    private Job prepareRestoreJob( EntityManager entityManager, String userId ) {
        Job resotoredeletedObjectsJob = prepareJobModel( entityManager, JOB_NAME_RESTORE_OBJECTS, RESTORE_DELETED_OBJECT_JOB_DESCRIPTION,
                SystemWorkflow.RESTORE );

        List< LogRecord > jobLog = new ArrayList<>();
        resotoredeletedObjectsJob.setJobType( JobTypeEnums.SYSTEM.getKey() );
        resotoredeletedObjectsJob.setLog( jobLog );
        resotoredeletedObjectsJob.setWorkflowId( UUID.fromString( SystemWorkflow.RESTORE.getId() ) );
        resotoredeletedObjectsJob.setCreatedBy( new UserDTO( userId ) );

        return jobManager.createJob( entityManager, UUID.fromString( userId ), resotoredeletedObjectsJob );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UUID > verifyPermissionsAndGetObjectIdsForRestore( EntityManager entityManager, String userId, String selectionId,
            String mode ) {
        List< UUID > objectIds = new ArrayList<>();

        if ( mode.contentEquals( ConstantsMode.BULK ) ) {

            FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionId );
            List< Object > ids = filtersDTO.getItems();
            for ( Object id : ids ) {
                isPermittedToRestore( entityManager, userId, id.toString() );
                objectIds.add( UUID.fromString( id.toString() ) );
            }

        } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
            isPermittedToRestore( entityManager, userId, selectionId );
            objectIds.add( UUID.fromString( selectionId ) );

        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
        }

        return objectIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< DeletedObjectDTO > getDeletedObjectList( FiltersDTO filter, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isRestricted( entityManager, UUID.fromString( userId ) );
            List< SuSEntity > susEntityList;
            /* for super user return all deleted objects for all users **/
            if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                susEntityList = susDAO.getAllDeletedObjects( entityManager, SuSEntity.class, null, filter );
            } else {
                /* for normal user return all deleted objects for all users if he had manage permission **/
                if ( permissionManager.isPermitted( entityManager, userId,
                        SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() + ConstantsString.COLON + PermissionMatrixEnum.MANAGE.getValue() ) ) {
                    susEntityList = susDAO.getAllDeletedObjects( entityManager, SuSEntity.class, null, filter );
                }
                /* for normal user return his/her deleted objects if he had read permission **/
                else {
                    if ( permissionManager.isPermitted( entityManager, userId,
                            SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() + ConstantsString.COLON
                                    + PermissionMatrixEnum.READ.getValue() ) ) {
                        susEntityList = susDAO.getAllDeletedObjects( entityManager, SuSEntity.class, UUID.fromString( userId ), filter );
                    } else {
                        throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(),
                                SimuspaceFeaturesEnum.DELETED_OBJECTS.getKey() ) );
                    }
                }
            }

            List< DeletedObjectDTO > listToReturn = new ArrayList<>();
            for ( SuSEntity suSEntity : susEntityList ) {
                listToReturn.add( prepareDeletedObjectDTOfromSusEntity( suSEntity ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, listToReturn );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List< TableColumn > getContainerVersionsUI( String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columns = null;
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( projectId ) );
            if ( susEntity instanceof ProjectEntity ) {
                columns = getProjectVersionUI();
            } else if ( susEntity instanceof LibraryEntity ) {
                columns = getLibraryVersionUI();
            } else if ( susEntity instanceof VariantEntity ) {
                columns = getVariantVersionUI();
            }
            if ( susEntity != null && CollectionUtils.isNotEmpty( columns ) ) {
                addObjectCustomAttributeToColumns( susEntity, columns );
            }
            return columns;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean changeStatusObject( EntityManager entityManager, String userIdFromGeneralHeader, UUID objectId,
            ChangeStatusDTO changeStatusDTO ) {
        isPermittedToWrite( entityManager, userIdFromGeneralHeader, objectId.toString() );
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        if ( susEntity instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getCheckedOutUser() != null
                && !dataObjectEntity.getCheckedOutUser().getId().equals( UUID.fromString( userIdFromGeneralHeader ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey() ) );

        }
        SuSEntity oldEntityForAuditLog = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        oldEntityForAuditLog.setLifeCycleStatus(
                lifeCycleManager.getLifeCycleStatusByStatusId( oldEntityForAuditLog.getLifeCycleStatus() ).getName() );
        susEntity.setLifeCycleStatus( lifeCycleManager.getLifeCycleStatusByStatusId( changeStatusDTO.getStatus() ).getName() );

        SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                susEntity.getConfig() );
        if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
            AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userIdFromGeneralHeader, oldEntityForAuditLog,
                    susEntity, susEntity.getComposedId().getId().toString(), susEntity.getName(), suSObjectModel.getName() );

            if ( null != auditLog ) {
                auditLog.setObjectId( oldEntityForAuditLog.getComposedId().getId().toString() );
            }
            susEntity.setAuditLogEntity( auditLog );
        }
        susEntity.setLifeCycleStatus( changeStatusDTO.getStatus() );
        changeStatusInVersionsRecursively( entityManager, susEntity );
        susDAO.update( entityManager, susEntity );
        saveOrUpdateIndexEntity( entityManager, suSObjectModel, susEntity );
        return true;
    }

    /**
     * Gets the data object curve by selection ids.
     *
     * @param selectionId
     *         the selection id
     * @param curveUnitDTO
     *         the curve unit DTO
     *
     * @return the data object curve by selection ids
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    @Override
    public List< DataObjectCurveDTO > getDataObjectCurveBySelectionIds( String selectionId, CurveUnitDTO curveUnitDTO ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String seperator = null;
            List< DataObjectCurveDTO > curveDTOList = new ArrayList<>();
            List< UUID > selectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            for ( UUID uuid : selectionIds ) {
                DataObjectCurveDTO curveDTO = new DataObjectCurveDTO();
                boolean cb2 = false;
                DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                        DataObjectEntity.class, uuid );
                if ( null != dataObjectEntity ) {
                    SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                            dataObjectEntity.getConfig() );
                    if ( susModel.getClassName().equalsIgnoreCase( DataObjectCurveDTO.class.getName() )
                            && dataObjectEntity.getFile() != null ) {
                        DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );
                        if ( documentDTO != null ) {
                            LocationEntity locationEntity = null;
                            DocumentEntity documentEntity = dataObjectEntity.getFile();
                            if ( !documentEntity.getLocations().isEmpty() ) {
                                locationEntity = documentEntity.getLocations().get( 0 );
                                if ( !locationEntity.getName().equals( DEFAULT ) && BooleanUtils.isTrue(
                                        dataObjectEntity.isAutoDelete() ) ) {
                                    return curveDTOList;
                                }
                            }
                            try ( InputStream stream = getDocumentStream( documentDTO, locationEntity ) ) {
                                if ( null == stream ) {
                                    return curveDTOList;
                                }
                                curveDTO = JsonUtils.jsonToObject( stream, DataObjectCurveDTO.class );
                                convertCurveCoordinatesToSIValues( curveDTO, curveUnitDTO );
                                curveDTOList.add( curveDTO );
                                continue;
                            } catch ( Exception e ) {
                                log.error( "Map Object Failed : Map to Cb2 file" + documentDTO.getName(), e );
                            }
                            InputStream inputStream = readFileFromLocationIfNotInDefault( documentDTO,
                                    dataObjectEntity.getFile().getLocations().get( 0 ) );
                            byte[] byteArray = IOUtils.toByteArray( inputStream );
                            inputStream.close();
                            try ( InputStream inputS1 = new ByteArrayInputStream( byteArray ); BufferedReader br = new BufferedReader(
                                    new InputStreamReader( inputS1 ) ) ) {
                                String line;
                                List< double[] > convertedValues = new ArrayList<>();
                                while ( ( line = br.readLine() ) != null ) {
                                    if ( line.contains( ConstantsString.COLON ) ) {
                                        seperator = prepareCurveModelByCB2File( curveDTO, seperator, line );
                                    } else if ( curveDTO != null && line.contains( ConstantsString.COMMA ) ) {
                                        prepareCurvePointsFromCB2File( line, convertedValues );
                                        cb2 = true;
                                    }
                                }
                                if ( curveDTO != null && cb2 ) {
                                    curveDTO.setName( dataObjectEntity.getName() );
                                    curveDTO.setCurve( convertedValues );
                                } else {
                                    try ( InputStream fileFromVaultStream = new ByteArrayInputStream( byteArray ) ) {
                                        curveDTO = JsonUtils.jsonToObject( fileFromVaultStream, DataObjectCurveDTO.class );
                                    }
                                }
                                convertCurveCoordinatesToSIValues( curveDTO, curveUnitDTO );
                            } catch ( Exception e ) {
                                log.error( e.getMessage(), e );
                            }
                        }
                    }
                }
                curveDTOList.add( curveDTO );
            }
            log.info( "curve match " );
            DataObjectCurveDTO curveCompare = curveDTOList.get( 0 );
            for ( DataObjectCurveDTO curve : curveDTOList ) {
                if ( !curveCompare.equals( curve ) ) {
                    throw new SusException( "Curves dimensions do not Match" );
                }
                log.info( "curve > " + curve );

            }
            return curveDTOList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the data object curve by sus entity.
     *
     * @param documentDTO
     *         the document DTO
     * @param locationEntity
     *         the location entity
     *
     * @return the file from temp
     */
    private InputStream getDocumentStream( DocumentDTO documentDTO, LocationEntity locationEntity ) {
        InputStream stream;
        if ( locationEntity.getName().equals( DEFAULT ) ) {
            try {
                stream = documentManager.readVaultFromDisk( documentDTO );
            } catch ( Exception e ) {
                log.error( "Object not found in vault " + documentDTO.getName(), e );
                throw new SusException( "Object not found in vault " + documentDTO.getName() );
            }
        } else {
            stream = readFileFromLocationIfNotInDefault( documentDTO, locationEntity );
        }
        return stream;
    }

    /**
     * Gets the data object curve by sus entity.
     *
     * @param documentDTO
     *         the document DTO
     * @param locationEntity
     *         the location entity
     *
     * @return the file from temp
     */
    private InputStream readFileFromLocationIfNotInDefault( DocumentDTO documentDTO, LocationEntity locationEntity ) {
        InputStream fileStream = null;

        try {
            fileStream = documentManager.readVaultFromDisk( documentDTO );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        if ( fileStream == null ) {
            String hex = CommonUtils.getHex( UUID.fromString( documentDTO.getId() ), documentDTO.getVersion().getId() );
            File hexDir = new File(
                    PropertiesManager.getDefaultServerTempPath() + File.separator + hex.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                            ConstantsInteger.INTEGER_VALUE_TWO ) );
            hexDir.mkdirs();
            try {
                SuSClient.downloadRequest(
                        locationEntity.getUrl() + "/api/core/location/download/vault/file/" + documentDTO.getId() + "/version/"
                                + documentDTO.getVersion().getId(), hexDir.getPath(),
                        CommonUtils.prepareHeadersWithAuthToken( locationEntity.getAuthToken() ), null );
                fileStream = documentManager.readDocumentFromTemp( documentDTO );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
        return fileStream;
    }

    /**
     * Convert curve coordinates to SI values.
     *
     * @param curveDTO
     *         the curve DTO
     * @param curveUnitDTO
     *         the curve Unit DTO
     */
    private void convertCurveCoordinatesToSIValues( DataObjectCurveDTO curveDTO, CurveUnitDTO curveUnitDTO ) {

        if ( curveUnitDTO != null && StringUtils.isNotBlank( curveDTO.getxUnit() ) && StringUtils.isNotBlank( curveDTO.getyUnit() )
                && StringUtils.isNotBlank( curveDTO.getxDimension() ) && StringUtils.isNotBlank( curveDTO.getyDimension() ) ) {
            List< double[] > convertedValues = new ArrayList<>();

            String toUnitX = StringUtils.isBlank( curveUnitDTO.getXunit() ) ? curveDTO.getxUnit() : curveUnitDTO.getXunit();
            String toUnitY = StringUtils.isBlank( curveUnitDTO.getYunit() ) ? curveDTO.getyUnit() : curveUnitDTO.getYunit();

            for ( double[] curveValues : curveDTO.getCurve() ) {
                double[] arr = new double[ curveValues.length ];
                for ( int i = 0; i < curveValues.length; i++ ) {
                    if ( i == 0 ) {
                        arr[ i ] = SIBaseUnitConverter.convert( curveDTO.getxDimension().toLowerCase(), curveDTO.getxUnit().toLowerCase(),
                                toUnitX.toLowerCase(), curveValues[ i ] );
                    } else {
                        arr[ i ] = SIBaseUnitConverter.convert( curveDTO.getyDimension().toLowerCase(), curveDTO.getyUnit().toLowerCase(),
                                toUnitY.toLowerCase(), curveValues[ i ] );
                    }
                }
                convertedValues.add( arr );
            }

            curveDTO.setCurve( convertedValues );
            curveDTO.setxUnit( toUnitX );
            curveDTO.setyUnit( toUnitY );
        }
    }

    /**
     * Prepare cureve points fron CB 2 file.
     *
     * @param line
     *         the line
     * @param convertedValues
     *         the converted values
     */
    private void prepareCurvePointsFromCB2File( String line, List< double[] > convertedValues ) {
        String[] values = line.trim().split( ConstantsString.COMMA );
        double[] arr = new double[ values.length ];

        int i = 0;
        for ( String edges : values ) {
            arr[ i++ ] = Double.parseDouble( edges.replace( "[", "" ).replace( "]", "" ).trim() );
        }
        convertedValues.add( arr );
    }

    /**
     * Prepare cure model by CB 2 file.
     *
     * @param curveDTO
     *         the curve DTO
     * @param seperator
     *         the seperator
     * @param line
     *         the line
     *
     * @return the string
     */
    private String prepareCurveModelByCB2File( DataObjectCurveDTO curveDTO, String seperator, String line ) {
        String[] values = line.split( ":" );
        for ( String str : values ) {

            // for old json type curve object
            if ( str.contains( X_UNITS_JSON_TYPE ) ) {
                curveDTO.setxUnit( de.soco.software.simuspace.suscore.common.util.StringUtils.removeSpecialChar( values[ 1 ] ) );
            } else if ( str.contains( Y_UNITS_JSON_TYPE ) ) {
                curveDTO.setyUnit( de.soco.software.simuspace.suscore.common.util.StringUtils.removeSpecialChar( values[ 1 ] ) );
            } else if ( str.contains( X_QUANTITY_TYPE_JSON_TYPE ) ) {
                curveDTO.setxDimension( de.soco.software.simuspace.suscore.common.util.StringUtils.removeSpecialChar( values[ 1 ] ) );
            } else if ( str.contains( Y_QUANTITY_TYPE_JSON_TYPE ) ) {
                curveDTO.setyDimension( de.soco.software.simuspace.suscore.common.util.StringUtils.removeSpecialChar( values[ 1 ] ) );
            }

            // for PPO based CSV type curve object
            else if ( str.contains( X_UNITS ) ) {
                curveDTO.setxUnit( values[ 1 ] );
            } else if ( str.contains( Y_UNITS ) ) {
                curveDTO.setyUnit( values[ 1 ] );
            } else if ( str.contains( X_QUANTITY_TYPE ) ) {
                curveDTO.setxDimension( values[ 1 ] );
            } else if ( str.contains( Y_QUANTITY_TYPE ) ) {
                curveDTO.setyDimension( values[ 1 ] );
            } else if ( str.contains( SEPARATOR ) ) {
                seperator = values[ 1 ];
            }

        }
        return seperator;
    }

    /**
     * Gets the curve X units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve X units by selection
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getCurveXUnitsBySelection(java.lang.String)
     */
    @Override
    public Object getCurveXUnitsBySelection( String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            if ( !selectionIds.isEmpty() ) {
                return getCurveXUnits( entityManager, selectionIds.get( 1 ).toString() );
            }
            return new ArrayList<>();
        } catch ( Exception e ) {
            log.error( "Curve match fail ", e );
            throw new SusException( "Curve match fail :" + e );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets the curve Y units by selection.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the curve Y units by selection
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getCurveYUnitsBySelection(java.lang.String)
     */
    @Override
    public Object getCurveYUnitsBySelection( String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > selectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            if ( !selectionIds.isEmpty() ) {
                return getCurveYUnits( entityManager, selectionIds.get( 1 ).toString() );
            }
            return new ArrayList<>();
        } catch ( Exception e ) {
            log.error( "Curve match fail ", e );
            throw new SusException( "Curve match fail :" + e );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets the variant version UI.
     *
     * @return the variant version UI
     */
    private List< TableColumn > getVariantVersionUI() {
        List< TableColumn > columns;
        columns = GUIUtils.listColumns( VariantDTO.class, VersionDTO.class );
        for ( TableColumn tableColumn : columns ) {
            if ( VariantDTO.VARIANT_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_PROJECT_BY_ID_AND_VERSION_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return columns;
    }

    /**
     * Gets the library version UI.
     *
     * @return the library version UI
     */
    private List< TableColumn > getLibraryVersionUI() {
        List< TableColumn > columns;
        columns = GUIUtils.listColumns( LibraryDTO.class, VersionDTO.class );
        for ( TableColumn tableColumn : columns ) {
            if ( LibraryDTO.LIBRARY_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_PROJECT_BY_ID_AND_VERSION_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return columns;
    }

    /**
     * Gets the project version UI.
     *
     * @return the project version UI
     */
    private List< TableColumn > getProjectVersionUI() {
        List< TableColumn > columns;
        columns = GUIUtils.listColumns( ProjectDTO.class, VersionDTO.class );
        for ( TableColumn tableColumn : columns ) {
            if ( ProjectDTO.PROJECT_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_PROJECT_BY_ID_AND_VERSION_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
        return columns;
    }

    /**
     * Adds the object custom attribute to columns.
     *
     * @param susEntity
     *         the sus entity
     * @param columns
     *         the columns
     */
    private void addObjectCustomAttributeToColumns( SuSEntity susEntity, List< TableColumn > columns ) {
        if ( susEntity != null && susEntity.getTypeId() != null ) {
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            List< CustomAttributeDTO > customAttributeDTOs = susObjectModel.getCustomAttributes();
            columns.addAll( prepareCustomAttributesTableColumns( customAttributeDTOs ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean restoreDeletedObjects( EntityManager entityManager, String userId, UUID id, Job job ) {

        job.setJobType( JobTypeEnums.SYSTEM.getKey() );
        job.getLog()
                .add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.RESTORING_OBJECT_ID.getKey() ) + id, new Date() ) );
        SuSEntity restoredObject = restoreDeletedObject( entityManager, id, userId );
        job.getLog()
                .add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.OBJECT_RESTORED.getKey() ) + restoredObject.getName(),
                        new Date() ) );

        return true;

    }

    /**
     * Checks if a sibling with same name, ignoring case exists.
     *
     * @param entityManager
     *         the entity manager
     * @param name
     *         to check against siblings
     * @param updateEntity
     *         to skip check for current entity if being updated, can be null
     * @param parent
     *         container to get siblings
     *
     * @return true if name is unique among its siblings, otherwise false
     */
    private boolean isNameUniqueAmongSiblings( EntityManager entityManager, String name, SuSEntity updateEntity, SuSEntity parent ) {

        List< SuSEntity > sameNameList = susDAO.getSiblingsBySameIName( entityManager, name, updateEntity, parent );
        return sameNameList.isEmpty();

    }

    /**
     * Creates the work flow project DTO from work flow project entity.
     *
     * @param susEntity
     *         the work flow project entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the work flow project DTO
     */
    private WorkflowProjectDTO createWorkflowProjectDTOFromWorkflowProjectEntity( SuSEntity susEntity, boolean setCustomAttributes ) {
        WorkflowProjectDTO workflowProjectDTO = null;
        if ( susEntity != null ) {
            workflowProjectDTO = new WorkflowProjectDTO();
            workflowProjectDTO.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
            workflowProjectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            workflowProjectDTO.setName( susEntity.getName() );
            workflowProjectDTO.setDescription( susEntity.getDescription() );
            workflowProjectDTO.setConfig( susEntity.getConfig() );
            workflowProjectDTO.setCreatedOn( susEntity.getCreatedOn() );
            workflowProjectDTO.setModifiedOn( susEntity.getModifiedOn() );
            if ( setCustomAttributes ) {
                workflowProjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            }
            if ( null != susEntity.getCreatedBy() ) {
                workflowProjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                workflowProjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            workflowProjectDTO.setTypeId( susEntity.getTypeId() );
            workflowProjectDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                            susEntity.getConfig() ) );
        }
        return workflowProjectDTO;
    }

    /**
     * Creates the data object DTO from data object entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param susEntity
     *         the data object entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the data object DTO
     */
    private Object createObjectDTOFromObjectEntity( EntityManager entityManager, UUID projectId, SuSEntity susEntity,
            boolean setCustomAttributes ) {
        return prepareHtmlDtoAndExtractZipFile( entityManager, projectId, susEntity, setCustomAttributes );
    }

    /**
     * Prepare data object DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    private Object prepareHtmlDtoAndExtractZipFile( EntityManager entityManager, UUID projectId, SuSEntity susEntity,
            boolean setCustomAttributes ) {
        DataObjectDTO dataObjectDTO = new DataObjectDTO();
        dataObjectDTO.setName( susEntity.getName() );
        dataObjectDTO.setId( susEntity.getComposedId().getId() );
        dataObjectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
        dataObjectDTO.setCreatedOn( susEntity.getCreatedOn() );
        dataObjectDTO.setModifiedOn( susEntity.getModifiedOn() );
        dataObjectDTO.setParentId( projectId );
        dataObjectDTO.setTypeId( susEntity.getTypeId() );
        dataObjectDTO.setAutoDeleted( susEntity.isAutoDelete() );
        dataObjectDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );

        UserDTO userById = userCommonManager.getUserById( entityManager, susEntity.getOwner().getId() );
        if ( userById != null ) {
            dataObjectDTO.setCreatedBy( userById );
        }
        dataObjectDTO.setDescription( susEntity.getDescription() );

        if ( setCustomAttributes ) {
            dataObjectDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
        }
        if ( susEntity.getTypeId() != null ) {
            dataObjectDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                            susEntity.getConfig() ) );
        }
        DocumentEntity file = ( ( DataObjectEntity ) susEntity ).getFile();
        if ( null != file ) {
            dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
            if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                List< LocationDTO > locationDTOs = getDocumentLocations( file );
                dataObjectDTO.setLocations( locationDTOs );
            }
            dataObjectDTO.getFile().setSize( susEntity.getSize() );
        }
        if ( null != susEntity.getModifiedBy() ) {
            dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
        }
        if ( null != susEntity.getConfig() ) {
            dataObjectDTO.setType(
                    configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
        }

        return dataObjectDTO;
    }

    /**
     * Creates the loadcase DTO from entity.
     *
     * @param suSEntity
     *         the suSEntity
     *
     * @return the LoadCaseDTO
     */
    public LoadCaseDTO createLoadcaseDTOFromEntity( SuSEntity suSEntity ) {
        LoadCaseDTO loadCaseDTO = null;
        if ( suSEntity != null ) {
            loadCaseDTO = new LoadCaseDTO();
            loadCaseDTO.setName( suSEntity.getName() );
            loadCaseDTO.setId( suSEntity.getComposedId().getId() );
            loadCaseDTO.setVersion( new VersionDTO( suSEntity.getComposedId().getVersionId() ) );
            loadCaseDTO.setDescription( suSEntity.getDescription() );
            loadCaseDTO.setCreatedOn( suSEntity.getCreatedOn() );
            loadCaseDTO.setAutoDeleted( suSEntity.isAutoDelete() );
            loadCaseDTO.setModifiedOn( suSEntity.getModifiedOn() );
            if ( suSEntity.getCreatedBy() != null ) {
                loadCaseDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( suSEntity.getCreatedBy() ) );
            }
            if ( suSEntity.getModifiedBy() != null ) {
                loadCaseDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( suSEntity.getModifiedBy() ) );
            }

            if ( suSEntity instanceof LoadCaseEntity loadCaseEntity ) {
                loadCaseDTO.setTimeout( loadCaseEntity.getTimeout() );
                loadCaseDTO.setIsInternal( loadCaseEntity.getIsInternal() );
                if ( loadCaseEntity.getDummyTypeEntity() != null ) {
                    DummyTypeDTO dummyTypeDTO = new DummyTypeDTO();
                    dummyTypeDTO.setId( loadCaseEntity.getDummyTypeEntity().getId() );
                    dummyTypeDTO.setDummyTypeName( loadCaseEntity.getDummyTypeEntity().getDummyTypeName() );
                    loadCaseDTO.setDummyTypeDTO( dummyTypeDTO );
                }
            }
        }
        return loadCaseDTO;
    }

    /**
     * Initialize object.
     *
     * @param className
     *         the class name
     *
     * @return the object
     */
    private static Object initializeObject( String className ) {

        try {
            return Class.forName( className ).getDeclaredConstructor().newInstance();
        } catch ( ReflectiveOperationException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), className ) );
        }
    }

    /**
     * Permitted to write.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedToWrite( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_WRITE.getKey(), OBJECT ) );
        }
    }

    /**
     * Checks if is permitted To restore.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedToRestore( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.RESTORE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_RESTORE.getKey(), OBJECT ) );
        }
    }

    /**
     * Permitted to delete.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedToDelete( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DELETE.getKey(), OBJECT ) );
        }
    }

    /**
     * Duplicate translation.
     *
     * @param susEntity
     *         the sus entity
     */
    private void duplicateTranslation( SuSEntity susEntity ) {
        Set< TranslationEntity > translationList = new HashSet<>();
        susEntity.getTranslation().forEach( translationEntity -> {
            TranslationEntity translation = new TranslationEntity( translationEntity.getName(), translationEntity.getLanguage(),
                    translationEntity.getFile() );
            translationList.add( translation );
        } );
        susEntity.setTranslation( translationList );
    }

    /**
     * Prepare custom attributes table columns.
     *
     * @param customAttributeDTOs
     *         the custom attribute DT os
     *
     * @return the list
     */
    private List< TableColumn > prepareCustomAttributesTableColumns( List< CustomAttributeDTO > customAttributeDTOs ) {
        List< TableColumn > tableColumns = new ArrayList<>();
        for ( CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
            TableColumn moduleColumn = new TableColumn();
            moduleColumn.setData( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + customAttributeDTO.getName() );
            moduleColumn.setName( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN );
            moduleColumn.setTitle( customAttributeDTO.getTitle() );
            moduleColumn.setFilter( CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE );
            Renderer renderer = new Renderer();
            renderer.setType( CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE );
            moduleColumn.setRenderer( renderer );

            tableColumns.add( moduleColumn );
        }
        return tableColumns;
    }

    /**
     * Prepare deleted object DT ofrom sus entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the deleted object DTO
     */
    private DeletedObjectDTO prepareDeletedObjectDTOfromSusEntity( SuSEntity susEntity ) {
        return new DeletedObjectDTO( susEntity.getComposedId().getId().toString(), new VersionDTO( susEntity.getVersionId() ),
                susEntity.getName(), susEntity.getClass().getSimpleName(),
                susEntity.getDeletedBy() != null ? userCommonManager.prepareUserModelFromUserEntity( susEntity.getDeletedBy() )
                        : new UserDTO(), susEntity.getDeletedOn() != null ? DateFormatStandard.format( susEntity.getDeletedOn() ) : null );
    }

    /**
     * Recursive status change.
     *
     * @param entityManager
     *         the entity manager
     * @param updatedEntity
     *         the entity
     */
    private void changeStatusInVersionsRecursively( EntityManager entityManager, SuSEntity updatedEntity ) {
        final StatusConfigDTO detail = lifeCycleManager.getLifeCycleStatusByStatusId( updatedEntity.getLifeCycleStatus() );
        log.info( ">>changeStatusInVersionsRecursively updatedEntity lifecycle:" + detail.getName() );
        if ( detail.isUnique() ) {
            final SuSEntity oldEntity = susDAO.getLatestObjectByVersionAndStatus( entityManager, SuSEntity.class,
                    updatedEntity.getComposedId().getId(), updatedEntity.getVersionId(), updatedEntity.getLifeCycleStatus() );
            if ( oldEntity != null ) {
                log.info( ">>changeStatusInVersionsRecursively oldEntity v" + oldEntity.getVersionId() + " lifecycle:"
                        + oldEntity.getLifeCycleStatus() );

                oldEntity.setLifeCycleStatus( detail.getMoveOldVersionToStatus() );
                changeStatusInVersionsRecursively( entityManager, oldEntity );
                susDAO.update( entityManager, oldEntity );
            }
        }
    }

    /**
     * Extract file info.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the file info
     */
    private FileInfo extractFileInfo( SuSEntity susEntity ) {
        SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( String.valueOf( susEntity.getTypeId() ),
                susEntity.getConfig() );
        FileInfo fileInfo = new FileInfo( susEntity.getComposedId().getId().toString(), susEntity.getName(), null, true,
                entityModel != null ? entityModel.getName() : ConstantsString.EMPTY_STRING, susEntity.getCreatedOn() );
        fileInfo.setIcon( susEntity.getIcon() );
        fileInfo.setTypeId( susEntity.getTypeId() );
        if ( susEntity.getLifeCycleStatus() != null ) {
            StatusConfigDTO statusConfigDTO = lifeCycleManager.getLifeCycleStatusByStatusId( susEntity.getLifeCycleStatus() );
            fileInfo.setLifeCycleStatus( new StatusDTO( susEntity.getLifeCycleStatus(),
                    statusConfigDTO != null ? statusConfigDTO.getName() : ConstantsString.EMPTY_STRING ) );
        }
        fileInfo.setDescription( susEntity.getDescription() );

        fileInfo.setSize(
                susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO ? susEntity.getSize().toString()
                        : ConstantsString.NOT_AVAILABLE );

        if ( susEntity instanceof DataObjectEntity dataObjectEntity ) {
            List< CommonLocationDTO > commonLocations = new ArrayList<>();
            if ( null != dataObjectEntity.getFile() ) {
                for ( LocationEntity location : dataObjectEntity.getFile().getLocations() ) {
                    CommonLocationDTO commonLocationDTO = new CommonLocationDTO();
                    commonLocationDTO.setName( location.getName() );
                    commonLocationDTO.setUrl( location.getUrl() );
                    commonLocationDTO.setAuthToken( location.getAuthToken() );
                    commonLocations.add( commonLocationDTO );
                }
                fileInfo.setLocation( commonLocations );
            }

            computeHashOfFile( dataObjectEntity, fileInfo );
        }

        fileInfo.setAdditionalFiles( getAdditionalFiles( susEntity ) );
        return fileInfo;
    }

    /**
     * Gets additional files.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the additional files
     */
    private List< DocumentDTO > getAdditionalFiles( SuSEntity susEntity ) {
        List< DocumentDTO > additionalFiles = new ArrayList<>();

        if ( susEntity instanceof DataObjectPredictionModelEntity traceEntity ) {
            if ( traceEntity.getJsonFile() != null ) {
                additionalFiles.add( documentManager.prepareDocumentDTO( traceEntity.getJsonFile() ) );
            }

            if ( traceEntity.getBinFile() != null ) {
                additionalFiles.add( documentManager.prepareDocumentDTO( traceEntity.getBinFile() ) );
            }
        }

        return additionalFiles;
    }

    /**
     * Gets file info.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return the file info
     */
    private FileInfo getFileInfo( EntityManager entityManager, String objectId ) {
        SuSEntity latestSuSEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
        return extractFileInfo( latestSuSEntity );
    }

    /**
     * Compute hash of file.
     *
     * @param dataObjectEntity
     *         the data object entity
     * @param fileInfo
     *         the file info
     */
    private void computeHashOfFile( DataObjectEntity dataObjectEntity, FileInfo fileInfo ) {
        fileInfo.setCheckedOut( dataObjectEntity.getCheckedOut() );
        fileInfo.setDir( false );
        // compute hash of its file here. Saved in DB
        if ( dataObjectEntity.getFile() != null ) {
            fileInfo.setHash( dataObjectEntity.getFile().getHash() );
            fileInfo.setFile( documentManager.prepareDocumentDTO( dataObjectEntity.getFile() ) );
        }
        if ( dataObjectEntity.getCheckedOutUser() != null ) {
            fileInfo.setCheckedOutUser( userCommonManager.prepareUserModelFromUserEntity( dataObjectEntity.getCheckedOutUser() ) );
        }
    }

    /**
     * Prepare user entity from user model.
     *
     * @param userDTO
     *         the user DTO
     *
     * @return the user entity
     */
    public UserEntity prepareUserEntityFromUserModel( UserDTO userDTO ) {
        UserEntity user = new UserEntity();
        user.setId( UUID.fromString( userDTO.getId() ) );
        user.setUserUid( userDTO.getUserUid() );
        user.setFirstName( userDTO.getFirstName() );
        user.setSurName( userDTO.getSurName() );
        user.setStatus( userDTO.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) );
        user.setRestricted( userDTO.getRestricted().equalsIgnoreCase( ConstantsStatus.ACTIVE ) );
        user.setChangeable( userDTO.isChangable() );
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getItemsFromSelectionId( UUID selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > items = contextMenuManager.getFilterBySelectionId( entityManager, selectionId.toString() ).getItems();
            List< String > returnList = new ArrayList<>();
            for ( Object id : items ) {
                returnList.add( ( String ) id );

            }
            return returnList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object addFileToAnObject( String jobId, String objectId, Map< String, DocumentDTO > docMap, String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                    DataObjectEntity.class, UUID.fromString( objectId ) );
            // detach retrieved entity to avoid exception on update
            entityManager.detach( dataObjectEntity );
            if ( null != jobId ) {
                dataObjectEntity.setJobId( UUID.fromString( jobId ) );
            }
            isPermittedToWrite( entityManager, userIdFromGeneralHeader, objectId );
            dataObjectEntity.setModifiedOn( new Date() );
            DocumentDTO fileDoc = JsonUtils.jsonToObject( JsonUtils.toJson( docMap.get( "file" ) ), DocumentDTO.class );
            DocumentEntity documentEntity = documentManager.getDocumentEntityById( entityManager, UUID.fromString( fileDoc.getId() ) );
            if ( dataObjectEntity.getFile() != null ) {
                documentEntity.setLocations( dataObjectEntity.getFile().getLocations() );
                entityManager.detach( dataObjectEntity.getFile() );
            }
            dataObjectEntity.setFile( documentEntity );
            DataObjectHtmlDTO dataObjectHtmlDTO = null;
            if ( dataObjectEntity instanceof DataObjectHtmlsEntity ) {
                dataObjectHtmlDTO = new DataObjectHtmlDTO();
                dataObjectHtmlDTO.setFile( fileDoc );
                if ( null != docMap.get( "zipFile" ) ) {
                    DocumentDTO zipFileDoc = JsonUtils.jsonToObject( JsonUtils.toJson( docMap.get( "zipFile" ) ), DocumentDTO.class );
                    dataObjectHtmlDTO.setZipFile( zipFileDoc );
                    dataObjectEntity.setAttachments( prepareZipDocumentEntity( zipFileDoc, null ) );
                }
            }
            previewManager.createPreview( entityManager, fileDoc, userIdFromGeneralHeader, dataObjectEntity, documentEntity,
                    dataObjectHtmlDTO );
            changeStatusInVersionsRecursively( entityManager, dataObjectEntity );
            dataObjectEntity.setSize( fileDoc.getSize() );

            duplicateTranslation( dataObjectEntity );
            SuSEntity susEntity = susDAO.updateAnObject( entityManager, dataObjectEntity );
            documentManager.saveOrUpdateDocument( entityManager, documentEntity );
            selectionManager.sendCustomerEvent( entityManager, userIdFromGeneralHeader, susEntity, UPDATE );
            return createObjectDTOFromObjectEntity( entityManager, null, susEntity, true );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare zip document entity.
     *
     * @param documentDTO
     *         the document DTO
     * @param existingZipEntity
     *         the existing zip entity
     *
     * @return the sets the
     */
    private Set< DocumentEntity > prepareZipDocumentEntity( DocumentDTO documentDTO, DocumentEntity existingZipEntity ) {
        Set< DocumentEntity > attachments = new HashSet<>();
        if ( null != documentDTO ) {
            DocumentEntity documentEntity;
            if ( existingZipEntity != null ) {
                attachments.add( existingZipEntity );
            } else {
                documentEntity = new DocumentEntity( UUID.fromString( documentDTO.getId() ) );

                documentEntity.setFileName( documentDTO.getName() );
                documentEntity.setFilePath( documentDTO.getPath() );
                documentEntity.setEncoding( documentDTO.getEncoding() );
                documentEntity.setHash( documentDTO.getHash() );
                documentEntity.setSize( documentDTO.getSize() );
                documentEntity.setFileSize( documentDTO.getSize() );
                documentEntity.setFileType( documentDTO.getType() );
                documentEntity.setCreatedOn( documentDTO.getCreatedOn() );

                documentEntity.setEncrypted( documentDTO.isEncrypted() );
                documentEntity.setIsTemp( documentDTO.getIsTemp() );
                documentEntity.setProperties( documentDTO.getProperties() );
                documentEntity.setExpiry( documentDTO.getExpiry() );

                attachments.add( documentEntity );

            }

        }
        return attachments;
    }

    /**
     * Prepare common attributes for update entity.
     *
     * @param existingEntity
     *         the existing entity
     * @param updatedEntity
     *         the updated entity
     */
    private void prepareCommonAttributesForUpdateEntity( SuSEntity existingEntity, SuSEntity updatedEntity ) {
        updatedEntity.setModifiedOn( new Date() );
        if ( updatedEntity.getLifeCycleStatus() == null ) {
            updatedEntity.setLifeCycleStatus( existingEntity.getLifeCycleStatus() );
        }
        updatedEntity.getComposedId().setId( existingEntity.getComposedId().getId() );
        updatedEntity.setOwner( existingEntity.getOwner() );
        updatedEntity.setCreatedOn( existingEntity.getCreatedOn() );
        updatedEntity.setCreatedBy( existingEntity.getCreatedBy() );
        updatedEntity.setTypeId( existingEntity.getTypeId() );
        updatedEntity.setConfig( existingEntity.getConfig() );
        updatedEntity.setPath( existingEntity.getPath() );
        updatedEntity.setUserSelectionId( existingEntity.getUserSelectionId() );
    }

    /**
     * Gets the context router.
     *
     * @param filter
     *         the filter
     * @param clazz
     *         the clazz
     *
     * @return the context router
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getContextRouter(de.soco.software.simuspace.suscore.common.base.
     * FiltersDTO, java.lang.Class)
     */
    @Override
    public List< ContextMenuItem > getContextRouter( FiltersDTO filter, Class< ? > clazz ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager(); // case of select all from data table
        try {
            if ( filter.getItems().isEmpty() && "-1".equalsIgnoreCase( filter.getLength().toString() ) ) {

                Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, SuSEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, SuSEntity.class, filter,
                        UUID.fromString( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

                filter.setItems( itemsList );
            }

            return contextMenuManager.getContextMenu( entityManager, PLUGIN_OBJECT, clazz, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Restore deleted object.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return true, if successful
     */
    private SuSEntity restoreDeletedObject( EntityManager entityManager, UUID objectId, String userId ) {
        SuSEntity object = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        restoreParentObjects( entityManager, object );
        SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( object.getTypeId().toString(), object.getConfig() );
        List< SuSEntity > list = susDAO.getObjectVersionListById( entityManager, SuSEntity.class, objectId );
        for ( SuSEntity susEntity : list ) {
            if ( susEntity.getComposedId().getVersionId() == object.getComposedId().getVersionId() && BooleanUtils.isTrue(
                    PropertiesManager.isAuditData() ) ) {
                susEntity.setAuditLogEntity(
                        AuditLogDTO.prepareAuditLogEntityForSusEntity( susEntity.getName() + ConstantsString.SPACE + "Restored",
                                ConstantsDbOperationTypes.CREATED, userId, susEntity, suSObjectModel.getName() ) );
            }
            restoreObject( entityManager, susEntity );
        }
        saveOrUpdateIndexEntity( entityManager, suSObjectModel, object );
        return object;
    }

    /**
     * Restore parent objects.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     */
    private void restoreParentObjects( EntityManager entityManager, SuSEntity susEntity ) {
        if ( null != susEntity ) {
            for ( SuSEntity parentSusEntity : susDAO.getDeletedParents( entityManager, susEntity ) ) {
                restoreObject( entityManager, parentSusEntity );
                SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( parentSusEntity.getTypeId().toString(),
                        parentSusEntity.getConfig() );
                saveOrUpdateIndexEntity( entityManager, suSObjectModel, parentSusEntity );
                restoreParentObjects( entityManager, parentSusEntity );
            }
        }
    }

    /**
     * Restore object.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     */
    private void restoreObject( EntityManager entityManager, SuSEntity susEntity ) {
        susEntity.setDelete( false );
        susEntity.setDeletedBy( null );
        susEntity.setDeletedOn( null );
        susEntity.setModifiedOn( new Date() );

        List< SuSEntity > parent = susDAO.getParents( entityManager, susEntity );

        if ( CollectionUtils.isNotEmpty( parent ) && !isNameUniqueAmongSiblings( entityManager, susEntity.getName(), susEntity,
                susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        parent.get( LIST_FIRST_INDEX ).getComposedId().getId() ) ) ) {

            susEntity.setName( susEntity.getName() + "-" + DateUtils.getPrintableDateTime() );
        }

        susDAO.saveOrUpdate( entityManager, susEntity );
    }

    /**
     * Sets the life cycle manager.
     *
     * @param lifeCycleManager
     *         the new life cycle manager
     */
    public void setLifeCycleManager( LifeCycleManager lifeCycleManager ) {
        this.lifeCycleManager = lifeCycleManager;
    }

    /**
     * Sets the document manager./.
     *
     * @param documentManager
     *         the new document manager
     */
    public void setDocumentManager( DocumentManager documentManager ) {
        this.documentManager = documentManager;
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
     * Sets the context menu manager.
     *
     * @param contextMenuManager
     *         the new context menu manager
     */
    public void setContextMenuManager( ContextMenuManager contextMenuManager ) {
        this.contextMenuManager = contextMenuManager;
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
     * Sets the config manager.
     *
     * @param configManager
     *         the new config manager
     */
    public void setConfigManager( ObjectTypeConfigManager configManager ) {
        this.configManager = configManager;
    }

    /**
     * Sets the selection manager.
     *
     * @param selectionManager
     *         the new selection manager
     */
    public void setSelectionManager( SelectionManager selectionManager ) {
        this.selectionManager = selectionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * Sets the thread pool executor service.
     *
     * @param threadPoolExecutorService
     *         the new thread pool executor service
     */
    public void setThreadPoolExecutorService( ThreadPoolExecutorService threadPoolExecutorService ) {
        this.threadPoolExecutorService = threadPoolExecutorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkUserReadPermission( String userId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                return true;
            }
            return ( permissionManager.isPermitted( entityManager, userId,
                    id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Check user update permission.
     *
     * @param entityManager
     *         the entity manager
     * @param token
     *         the token
     * @param entity
     *         the entity
     */
    private void checkUserUpdatePermission( EntityManager entityManager, String token, SuSEntity entity ) {
        if ( !permissionManager.isWritable( entityManager, UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ),
                entity.getComposedId().getId() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), entity.getName() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkUserDeletePermission( String userId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( userId.equals( ConstantsID.SUPER_USER_ID ) ) {
                return true;
            }
            return ( permissionManager.isPermitted( entityManager, userId,
                    id + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferResult transferObject( String userId, TransferObject transferObject ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return transferObject( entityManager, userId, transferObject );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferResult transferObject( EntityManager entityManager, String userId, TransferObject transferObject ) {
        TransferResult transferResult = new TransferResult();

        LocationDTO locationDTO = locationManager.getLocation( entityManager, transferObject.getTargetLocationId() );
        if ( locationDTO == null ) {
            throw new SusException( LOCATION_DOES_NOT_EXIST_WITH_ID + transferObject.getTargetLocationId() );
        }
        validateLocationStatus( locationDTO );

        permissionManager.isPermitted( entityManager, userId,
                transferObject.getTargetLocationId() + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() + ConstantsString.COLON
                        + IS_LOCATION, Messages.NO_RIGHTS_TO_WRITE.getKey(), locationDTO.getName() );

        SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( transferObject.getObjectId() ) );
        if ( entity == null ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), transferObject.getObjectId() ) );
        }

        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                entity.getComposedId().getId() + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            transferResult.addError( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_WRITE.getKey(), OBJECT ) );
            return transferResult;
        }

        if ( entity instanceof ContainerEntity ) {

            List< SuSEntity > sustEntityList = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                    entity.getComposedId().getId() );
            for ( SuSEntity susEntity : sustEntityList ) {
                if ( susEntity == null || !permissionManager.isReadable( entityManager, UUID.fromString( userId ),
                        susEntity.getComposedId().getId() ) ) {
                    continue;
                }
                TransferObject tObject = new TransferObject();
                tObject.setObjectId( susEntity.getComposedId().getId().toString() );
                tObject.setOperationType( transferObject.getOperationType() );
                tObject.setTargetLocationId( transferObject.getTargetLocationId() );
                if ( susEntity instanceof ContainerEntity ) {
                    TransferResult result = transferObject( entityManager, userId, tObject );
                    transferResult.addInfos( result.getInfos() );
                    transferResult.addErrors( result.getErrors() );
                    transferResult.getTransferedObjects().addAll( result.getTransferedObjects() );
                } else if ( susEntity instanceof DataObjectEntity dataObjectEntity ) {
                    String info = transferSingleObject( entityManager, userId, tObject, locationDTO, dataObjectEntity );
                    List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager,
                            Collections.singletonList( susEntity.getComposedId() ) );
                    Object object = createGenericDTOFromObjectEntity( entityManager, userId, null, susEntity, translationEntityList );
                    transferResult.addInfo( info );
                    transferResult.addObject( object );
                }
            }

        } else if ( entity instanceof DataObjectEntity dataObjectEntity ) {
            String info = transferSingleObject( entityManager, userId, transferObject, locationDTO, dataObjectEntity );
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager,
                    Collections.singletonList( entity.getComposedId() ) );
            Object object = createGenericDTOFromObjectEntity( entityManager, userId, null, entity, translationEntityList );
            transferResult.addInfo( info );
            transferResult.addObject( object );
        }

        return transferResult;
    }

    /**
     * Validate location status.
     *
     * @param locationDTO
     *         the location dto
     */
    private void validateLocationStatus( LocationDTO locationDTO ) {
        try {
            locationManager.validatePaths( locationDTO );
        } catch ( Exception e ) {
            log.error( MessageBundleFactory.getMessage( Messages.TARGET_LOCATION_IS_NOT_ACCESSIBLE.getKey(), locationDTO.getName() ), e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.TARGET_LOCATION_IS_NOT_ACCESSIBLE.getKey(), locationDTO.getName() ) );
        }
        if ( !locationDTO.getStatus().equalsIgnoreCase( ConstantsStatus.ACTIVE ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.TARGET_LOCATION_IS_IN_ACTIVE.getKey(), locationDTO.getName() ) );
        }
        if ( locationDTO.getType() != null && locationDTO.getType().equalsIgnoreCase( LOCATION_TYPE_EXECUTION_ONLY ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.TARGET_LOCATION_IS_FOR_EXECUTION_ONLY.getKey(), locationDTO.getName() ) );
        }

    }

    /**
     * Transfer single object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param transferObject
     *         the transfer object
     * @param locationDTO
     *         the location DTO
     * @param dataObjectEntity
     *         the data object entity
     *
     * @return the string
     */
    private String transferSingleObject( EntityManager entityManager, String userId, TransferObject transferObject, LocationDTO locationDTO,
            DataObjectEntity dataObjectEntity ) {

        FileInfo fileInfo = getFileInfo( entityManager, transferObject.getObjectId() );
        String filePath = fileInfo.getFile().getPath();

        List< LocationEntity > locations = dataObjectEntity.getFile().getLocations();
        for ( LocationEntity locationEntity : locations ) {
            if ( locationEntity.getId().equals( UUID.fromString( transferObject.getTargetLocationId() ) ) ) {
                throw new SusException( OBJECT_ALREADY_EXISTS_ON_TARGET_LOCATION + transferObject.getTargetLocationId() );
            }
        }

        UserEntity user = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        List< LocationEntity > userPrioLocations = new ArrayList<>();
        if ( StringUtils.isNoneEmpty( user.getLocationPreferenceSelectionId() ) ) {
            userPrioLocations = locationManager.getLocationEntitiesBySelectionId( entityManager, userId,
                    user.getLocationPreferenceSelectionId() );
        }

        LocationEntity prioLocation = null;
        if ( CollectionUtils.isEmpty( userPrioLocations ) ) {
            locations.sort( ( LocationEntity a, LocationEntity b ) -> b.getPriority() > a.getPriority() ? -1 : 1 );
            prioLocation = locations.get( FIRST_INDEX );
        } else {
            for ( LocationEntity locationEntity : userPrioLocations ) {
                if ( locations.contains( locationEntity ) ) {
                    prioLocation = locationEntity;
                }
            }
            if ( prioLocation == null ) {
                locations.sort( ( LocationEntity a, LocationEntity b ) -> b.getPriority() > a.getPriority() ? -1 : 1 );
                prioLocation = locations.get( FIRST_INDEX );
            }
        }

        String info = MessageBundleFactory.getMessage( Messages.OBJECT_IS_GOING_TO_TRANSFER_FROM_LOCATION_TO_LOCATION.getKey(),
                dataObjectEntity.getName(), transferObject.getOperationType(), prioLocation.getName(), locationDTO.getName() );

        if ( prioLocation.getId().equals( UUID.fromString( LocationsEnum.DEFAULT_LOCATION.getId() ) ) && locationDTO.getUrl()
                .equals( prioLocation.getUrl() ) ) {
            // hit upload api
            File file = new File( PropertiesManager.getVaultPath() + filePath );

            if ( !file.exists() ) {
                throw new SusException( FILE_NOT_EXISTS_IN_VAULT );
            }
            DocumentDTO documentDTO = SuSClient.uploadFileRequest( locationDTO.getUrl() + API_LOCATION_UPLOAD, file,
                    prepareDownloadHeaders( prioLocation.getAuthToken(), fileInfo.getFile().getPath() ) );
            if ( documentDTO != null ) {
                handleTransferOperationType( transferObject, dataObjectEntity.getFile(), filePath );
                updatedDataObjectAddLocation( entityManager, locationDTO, dataObjectEntity.getFile() );
            }
        } else {
            // hit export api of source location.
            TransferLocationObject transferLocationObject = new TransferLocationObject();
            transferLocationObject.setFilePath( fileInfo.getFile().getPath() );
            transferLocationObject.setTargetAddress( locationDTO.getUrl() );
            transferLocationObject.setTargetToken( locationDTO.getAuthToken() );
            transferLocationObject.setOperation( transferObject.getOperationType() );
            final String josnObjectDTO = JsonUtils.toJsonString( transferLocationObject );
            SusResponseDTO responseDTO = SuSClient.postRequest( prioLocation.getUrl() + API_LOCATION_EXPORT_FILE, josnObjectDTO,
                    CommonUtils.prepareHeadersWithAuthToken( prioLocation.getAuthToken() ) );
            if ( responseDTO.getSuccess() ) {
                handleTransferOperationType( transferObject, dataObjectEntity.getFile(), filePath );
                updatedDataObjectAddLocation( entityManager, locationDTO, dataObjectEntity.getFile() );
            }
        }
        return info;
    }

    /**
     * Handle transfer operation type.
     *
     * @param transferObject
     *         the transfer object
     * @param documentEntity
     *         the entity
     * @param fileRelPath
     *         the file rel path
     */
    private void handleTransferOperationType( TransferObject transferObject, DocumentEntity documentEntity, String fileRelPath ) {
        if ( transferObject.getOperationType().equals( TransferOperationType.MOVE ) ) {

            Iterator< LocationEntity > itr = documentEntity.getLocations().iterator();
            while ( itr.hasNext() ) {
                LocationEntity locationEntity = itr.next();
                Map< String, String > pathMap = new HashMap<>();
                pathMap.put( PATH_KEY, fileRelPath );
                // delete from all other locations.
                final String josnPath = JsonUtils.toJsonString( pathMap );
                SuSClient.postRequest( locationEntity.getUrl() + API_LOCATION_DELETE_FILE, josnPath,
                        CommonUtils.prepareHeadersWithAuthToken( locationEntity.getAuthToken() ) );
                itr.remove();
            }
        }
    }

    /**
     * Updated data object add location.
     *
     * @param entityManager
     *         the entity manager
     * @param locationDTO
     *         the location DTO
     * @param documentEntity
     *         the entity
     */
    private void updatedDataObjectAddLocation( EntityManager entityManager, LocationDTO locationDTO, DocumentEntity documentEntity ) {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setId( locationDTO.getId() );
        documentEntity.getLocations().add( locationEntity );
        documentManager.saveOrUpdateDocument( entityManager, documentEntity );
    }

    /**
     * Prepare download headers.
     *
     * @param token
     *         the token
     * @param destPath
     *         the dest path
     *
     * @return the map
     */
    private Map< String, String > prepareDownloadHeaders( String token, String destPath ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTHORIZATION, token );
        requestHeaders.put( ConstantRequestHeader.DEST_PATH, destPath );

        return requestHeaders;

    }

    /**
     * Gets the curve X units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve X units
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getCurveXUnits(java.lang.String)
     */
    @Override
    public List< SelectOptionsUI > getCurveXUnits( EntityManager entityManager, String objectId ) {
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectCurveEntity dataObjectEntity = ( DataObjectCurveEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                    DataObjectCurveEntity.class, UUID.fromString( objectId ) );
            DataObjectCurveDTO dataObjectCurve = calculateCurveXYUnitsByObjectId( entityManager, dataObjectEntity );
            if ( null != dataObjectCurve ) {
                List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                    if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectCurve.getxDimension().toLowerCase() ) ) {
                        for ( UnitsList units : unitsFamily.getUnits() ) {
                            listToReturn.add( new SelectOptionsUI( units.getScale().toLowerCase(), units.getName().toLowerCase() ) );
                        }
                        break;
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return listToReturn;
    }

    /**
     * Gets the curve Y units.
     *
     * @param objectId
     *         the object id
     *
     * @return the curve Y units
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getCurveYUnits(java.lang.String)
     */
    @Override
    public List< SelectOptionsUI > getCurveYUnits( EntityManager entityManager, String objectId ) {
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectCurveEntity dataObjectEntity = ( DataObjectCurveEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                    DataObjectCurveEntity.class, UUID.fromString( objectId ) );
            DataObjectCurveDTO dataObjectCurve = calculateCurveXYUnitsByObjectId( entityManager, dataObjectEntity );
            if ( null != dataObjectCurve ) {
                List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                    if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectCurve.getyDimension().toLowerCase() ) ) {
                        for ( UnitsList units : unitsFamily.getUnits() ) {
                            listToReturn.add( new SelectOptionsUI( units.getScale().toLowerCase(), units.getName().toLowerCase() ) );
                        }
                        break;
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return listToReturn;
    }

    /**
     * Export object UI.
     *
     * @return the list
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#exportObjectUI()
     */
    @Override
    public UIForm exportObjectUI() {
        List< UIFormItem > row = new ArrayList<>();
        UIFormItem ui = GUIUtils.createFormItem();
        ui.setType( OS_DIRECTORY );
        ui.setLabel( SELECT_EXPORT_DIRECTORY );
        ui.setName( EXPORT_TO );

        Map< String, Object > rules = new HashMap<>();
        rules.put( REQUIRED, true );
        ui.setRules( rules );
        row.add( ui );
        return GUIUtils.createFormFromItems( row );
    }

    /**
     * Gets the all items count with type id.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the object id
     * @param typeId
     *         the type id
     *
     * @return the all items count with type id
     */
    public Long getAllItemsCountWithTypeId( String userId, UUID projectId, UUID typeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Long count = null;
            if ( typeId.equals( UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ) ) ) {
                SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, projectId );
                if ( latestEntity != null ) {
                    if ( latestEntity instanceof SystemContainerEntity ) {
                        String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName(
                                ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME ).get( 0 ).getId();
                        count = susDAO.getCountWithParentId( entityManager, SuSEntity.class, projectId, userId,
                                lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                                lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                                susDAO.getUserSecurityIDs( entityManager, UUID.fromString( userId ) ), PermissionMatrixEnum.VIEW.getKey() );

                    } else {
                        SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                                latestEntity.getConfig() );
                        count = susDAO.getCountWithParentId( entityManager, SuSEntity.class, projectId, userId,
                                lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                                lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                                susDAO.getUserSecurityIDs( entityManager, UUID.fromString( userId ) ), PermissionMatrixEnum.VIEW.getKey() );
                    }
                }
                return count;
            }
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectId );
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( typeId.toString(), susEntity.getConfig() );
            Object object = initializeObject( susObjectModel.getClassName() );
            Class< ? > entityClass = ReflectionUtils.getFieldByName( object.getClass(), ENTITY_CLASS_FIELD_NAME ).getType();
            count = susDAO.getCountWithParentId( entityManager, entityClass, projectId, userId,
                    lifeCycleManager.getOwnerVisibleStatusByPolicyId( susObjectModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( susObjectModel.getLifeCycle() ),
                    susDAO.getUserSecurityIDs( entityManager, UUID.fromString( userId ) ), PermissionMatrixEnum.READ.getKey() );

            return count;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Calculate curve XY units by object id.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectEntity
     *         the data object entity
     *
     * @return the data object curve DTO
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private DataObjectCurveDTO calculateCurveXYUnitsByObjectId( EntityManager entityManager, DataObjectCurveEntity dataObjectEntity )
            throws IOException {
        DataObjectCurveDTO curveDTO = new DataObjectCurveDTO();
        InputStream stream = null;
        boolean cb2 = false;
        String seperator = null;
        if ( null != dataObjectEntity ) {
            SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                    dataObjectEntity.getConfig() );
            if ( susModel.getClassName().equalsIgnoreCase( DataObjectCurveDTO.class.getName() ) && dataObjectEntity.getFile() != null ) {
                DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );
                if ( documentDTO != null ) {
                    LocationEntity locationEntity = null;
                    DocumentEntity documentEntity = dataObjectEntity.getFile();
                    if ( !documentEntity.getLocations().isEmpty() ) {
                        locationEntity = documentEntity.getLocations().get( 0 );
                    }
                    try {
                        if ( locationEntity.getName().equals( DEFAULT ) ) {
                            stream = documentManager.readVaultFromDisk( documentDTO );
                        } else {
                            if ( BooleanUtils.isTrue( dataObjectEntity.isAutoDelete() ) ) {
                                return curveDTO;
                            }
                            stream = readFileFromLocationIfNotInDefault( documentDTO, locationEntity );
                            if ( null == stream ) {
                                return curveDTO;
                            }
                        }
                        curveDTO = JsonUtils.jsonToObject( stream, DataObjectCurveDTO.class );
                        convertCurveCoordinatesToSIValues( curveDTO, new CurveUnitDTO() );
                        return curveDTO;
                    } catch ( Exception e ) {
                        log.error( "Map Object Failed : Map to Cb2 file" + documentDTO.getName(), e );
                    } finally {
                        stream.close();
                    }
                    try ( BufferedReader br = new BufferedReader(
                            new InputStreamReader( getDocumentStream( documentDTO, locationEntity ) ) ) ) {
                        String line;
                        List< double[] > convertedValues = new ArrayList<>();
                        while ( ( line = br.readLine() ) != null ) {
                            if ( line.contains( ConstantsString.COLON ) ) {
                                seperator = prepareCurveModelByCB2File( curveDTO, seperator, line );
                            } else if ( curveDTO != null && line.contains( ConstantsString.COMMA ) ) {
                                prepareCurvePointsFromCB2File( line, convertedValues );
                                cb2 = true;
                            }
                        }
                        if ( curveDTO != null && cb2 ) {
                            curveDTO.setName( dataObjectEntity.getName() );
                            curveDTO.setCurve( convertedValues );
                            convertCurveCoordinatesToSIValues( curveDTO, new CurveUnitDTO() );
                        }
                    } catch ( Exception e ) {
                        log.error( e.getMessage(), e );
                    }

                }
            }
        }
        return curveDTO;
    }

    /**
     * Checks if is restricted.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     */
    private void isRestricted( EntityManager entityManager, UUID userId ) {
        UserDTO userDTO = userCommonManager.getUserById( entityManager, userId );
        if ( userDTO != null && userDTO.getRestricted().equals( RESTRICTED_USER ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.USER_RESTRICTED_TO_SYSTEM.getKey(), userDTO.getName() ) );
        }
    }

    /**
     * Gets the HPC list by each user.
     *
     * @param userUID
     *         the user UID
     * @param solver
     *         the solver
     *
     * @return the HPC list by each user
     */
    @Override
    public List< WfFieldsUiDTO > getHPCListByEachUser( String userUID, String solver ) {
        List< String > parserKeySet = new ArrayList<>();
        List< WfFieldsUiDTO > sgeList = new ArrayList<>();
        try {
            ProcessResult result = PythonUtils.getHCPLIstByEachUser( userUID );
            String[] varSplitArray = result.getOutputString().split( Pattern.quote( "\n" ) );
            parserKeySet.addAll( Arrays.asList( varSplitArray ) );
        } catch ( Exception e ) {
            log.error( "SGE run Command failed ", e );
        }
        for ( String costcenter : getCostCenterListBySolver( parserKeySet, solver ) ) {
            sgeList.add( new WfFieldsUiDTO( costcenter, costcenter, null ) );
        }
        return sgeList;
    }

    /**
     * Gets the cost center list by solver.
     *
     * @param parserKeySet
     *         the parser key set
     * @param solver
     *         the solver
     *
     * @return the cost center list by solver
     */
    private List< String > getCostCenterListBySolver( List< String > parserKeySet, String solver ) {
        Pattern pattern;
        List< String > list = new ArrayList<>();
        switch ( solver ) {
            case "abaqus_standard" -> pattern = Pattern.compile( PropertiesManager.getCostcenterRegex( "abaqus_standard" ) );
            case "nastran" -> pattern = Pattern.compile( PropertiesManager.getCostcenterRegex( "nastran" ) );
            case "lsdyna" -> pattern = Pattern.compile( PropertiesManager.getCostcenterRegex( "lsdyna" ) );
            case "abaqus_explicit" -> pattern = Pattern.compile( PropertiesManager.getCostcenterRegex( "abaqus_explicit" ) );
            case "optistruct" -> pattern = Pattern.compile( PropertiesManager.getCostcenterRegex( "optistruct" ) );
            default -> pattern = Pattern.compile( ".*" );
        }
        for ( String key : parserKeySet ) {
            Matcher matcher = pattern.matcher( key );
            if ( matcher.find() ) {
                list.add( matcher.group() );
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadDocument( UUID objectId, int version, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( objectId.toString() ) );
            List< UUID > docIds = new ArrayList<>();
            String docId = ConstantsString.EMPTY_STRING;
            if ( susEntity instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getFile() != null ) {
                docId = dataObjectEntity.getFile().getId().toString();
                docIds.add( UUID.fromString( docId ) );
                for ( DocumentEntity document : dataObjectEntity.getAttachments() ) {
                    if ( !docIds.contains( document.getId() ) ) {
                        docIds.add( document.getId() );
                    }

                }
            }
            if ( docIds.size() == 1 && !( susEntity instanceof DataObjectHtmlsEntity ) ) {
                int size;
                DocumentDTO document = documentManager.getDocumentById( entityManager, UUID.fromString( docId ) );

                try ( InputStream fileStream = readFileFromLocationIfNotInDefault( document,
                        ( ( DataObjectEntity ) susEntity ).getFile().getLocations().get( ConstantsInteger.INTEGER_VALUE_ZERO ) ) ) {
                    byte[] byteArray = IOUtils.toByteArray( fileStream );
                    try ( InputStream inputS2 = new ByteArrayInputStream( byteArray ) ) {
                        /* calculate File size*/
                        size = IOUtils.toByteArray( inputS2 ).length;
                    }
                    ResponseBuilder response = Response.ok( new ByteArrayInputStream( byteArray ) );
                    response.header( "Content-Disposition", "attachment; filename=\"" + document.getName() + "\"" );
                    response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
                    response.header( "File-Size", size );
                    return response.build();
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    return null;
                }
            } else {
                List< DocumentDTO > docList = new ArrayList<>( documentManager.getDocumentsByIds( entityManager, docIds ) );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try ( ZipOutputStream zos = new ZipOutputStream( baos ) ) {
                    for ( DocumentDTO documentDTO : docList ) {

                        InputStream fis = documentManager.readVaultFromDisk( documentDTO );
                        // begin writing a new ZIP entry, positions the stream to the start of the entry data
                        zos.putNextEntry( new ZipEntry( documentDTO.getName() ) );
                        byte[] data = new byte[ 2048 ];
                        BufferedInputStream entryStream = new BufferedInputStream( fis, 2048 );
                        int length;
                        while ( ( length = entryStream.read( data, 0, 2048 ) ) != -1 ) {
                            zos.write( data, 0, length );
                        }
                        // close the InputStream
                        fis.close();
                    }
                    zos.closeEntry();
                } catch ( IOException ioe ) {
                    log.error( ioe.getMessage(), ioe );
                }
                byte[] zipBytes = baos.toByteArray();
                try ( ByteArrayInputStream bis = new ByteArrayInputStream( zipBytes ) ) {
                    ResponseBuilder response = Response.ok( new ByteArrayInputStream( zipBytes ) );
                    response.header( "Content-Disposition", "attachment; filename=\"" + susEntity.getName() + ".zip" + "\"" );
                    response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
                    response.header( "File-Size", IOUtils.toByteArray( bis ).length );
                    return response.build();
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    return null;
                }
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

    /**
     * Sets entity manager factory.
     *
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;

    }

    /**
     * Sets translation dao.
     *
     * @param translationDAO
     *         the translation dao
     */
    public void setTranslationDAO( TranslationDAO translationDAO ) {
        this.translationDAO = translationDAO;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    @Override
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Sets the link dao.
     *
     * @param linkDao
     *         the new link dao
     */
    public void setLinkDao( LinkDAO linkDao ) {
        this.linkDao = linkDao;
    }

    /**
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Sets the user common manager.
     *
     * @param userCommonManager
     *         the new user common manager
     */
    public void setUserCommonManager( UserCommonManager userCommonManager ) {
        this.userCommonManager = userCommonManager;
    }

    /**
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Sets the location manager.
     *
     * @param locationManager
     *         the new location manager
     */
    public void setLocationManager( LocationManager locationManager ) {
        this.locationManager = locationManager;
    }

    /**
     * Sets preview manager.
     *
     * @param previewManager
     *         the preview manager
     */
    public void setPreviewManager( PreviewManager previewManager ) {
        this.previewManager = previewManager;
    }

}