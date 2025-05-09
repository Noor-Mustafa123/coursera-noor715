package de.soco.software.simuspace.server.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.constant.ConstantsKaraf;
import de.soco.software.simuspace.server.dao.SchemeSchemaDAO;
import de.soco.software.simuspace.server.dao.VariableDAO;
import de.soco.software.simuspace.server.dao.WorkflowDAO;
import de.soco.software.simuspace.server.manager.BaseManager;
import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.server.manager.LicenseTokenManager;
import de.soco.software.simuspace.server.manager.ParserManager;
import de.soco.software.simuspace.server.manager.RegexManager;
import de.soco.software.simuspace.server.manager.ShapeModuleManager;
import de.soco.software.simuspace.server.manager.TemplateManager;
import de.soco.software.simuspace.server.manager.WorkflowManager;
import de.soco.software.simuspace.server.manager.WorkflowUserManager;
import de.soco.software.simuspace.server.model.SchemeOptimizationDTO;
import de.soco.software.simuspace.server.model.TokenDetails;
import de.soco.software.simuspace.server.model.jsonschema.SMJSONSchema;
import de.soco.software.simuspace.server.threads.RunSchemeDoeThread;
import de.soco.software.simuspace.server.threads.RunSchemeOptimizationThread;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsCustomFlagFields;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.constants.simflow.StatusConstants;
import de.soco.software.simuspace.suscore.common.enums.AlgoTypeEnum;
import de.soco.software.simuspace.suscore.common.enums.BmwCaeBenchEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.TransferOperationType;
import de.soco.software.simuspace.suscore.common.enums.UIColumnType;
import de.soco.software.simuspace.suscore.common.enums.VariableDropDownEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.SchemeCategoryEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowReserveKeywordsEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.FeatureNotAllowedException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.DynamicScript;
import de.soco.software.simuspace.suscore.common.model.FileObject;
import de.soco.software.simuspace.suscore.common.model.JobSubmitResponseDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectVariableDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.model.PythonEnvironmentDTO;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.ScanResponseDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateVariableDTO;
import de.soco.software.simuspace.suscore.common.model.TransferLocationObject;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.ValidateWorkflowFeildsModel;
import de.soco.software.simuspace.suscore.common.model.ValidateWorkflowModel;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.model.WorkFlowAdditionalAttributeDTO;
import de.soco.software.simuspace.suscore.common.model.WorkflowValidationDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserDTO;
import de.soco.software.simuspace.suscore.common.parser.model.ParserPartDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.WfFieldsUiDTO;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.DynamicScriptsUtil;
import de.soco.software.simuspace.suscore.common.util.EncryptAndDecryptUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.ExecutionHosts;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.Hosts;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.PasswordUtils;
import de.soco.software.simuspace.suscore.common.util.PythonUtils;
import de.soco.software.simuspace.suscore.common.util.StringUtils;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchDAO;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.ParserVariableDTO;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.CustomVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectFileEntity;
import de.soco.software.simuspace.suscore.data.entity.DesignVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.ObjectiveVariableEntity;
import de.soco.software.simuspace.suscore.data.entity.ParserEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectOverviewEntity;
import de.soco.software.simuspace.suscore.data.entity.RegexEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SchemeSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.TemplateEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserDetailEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VariableEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WFSchemeEntity;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.manager.impl.base.ContextMenuManagerImpl;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.data.model.CustomVariableDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectFileDTO;
import de.soco.software.simuspace.suscore.data.model.DesignVariableDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ObjectiveVariableDTO;
import de.soco.software.simuspace.suscore.data.model.SsfeSelectionDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;
import de.soco.software.simuspace.suscore.executor.util.SusExecutorUtil;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.constant.ConstantsWorkFlowProps;
import de.soco.software.simuspace.workflow.dto.ImportWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.LatestWorkFlowDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.dto.UserLicenseDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.dto.WorkflowModel;
import de.soco.software.simuspace.workflow.location.JobParametersLocationModel;
import de.soco.software.simuspace.workflow.location.MoreAdvApi32;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.UserWorkFlow;
import de.soco.software.simuspace.workflow.model.impl.EngineFile;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;
import de.soco.software.simuspace.workflow.model.impl.RequestHeaders;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.model.impl.SelectScriptPayload;
import de.soco.software.simuspace.workflow.model.impl.UserWorkflowImpl;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;
import de.soco.software.simuspace.workflow.util.VariableUtil;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

/**
 * This Class will be called by the service layer AND is responsible to call the DAO layer to communicate to database Before calling the DAO
 * layer it may Execute some business logic Like Model validation , getpreparation of Entities from Model class and any other if required.
 * All the Methods will have return type of DTOs so that it will be process as response on service layer
 */
@Log4j2
public class WorkflowManagerImpl extends BaseManager implements WorkflowManager {

    /**
     * The Constant ERROR_DOWNLOADING_RE_NAMING_CB2_ASSEMBLE_AND_SIMULATE_RELATED_FILE.
     */
    private static final String ERROR_DOWNLOADING_RE_NAMING_CB2_ASSEMBLE_AND_SIMULATE_RELATED_FILE = "Error downloading/re-naming cb2 Assemble and simulate related File: ";

    /**
     * The Constant ERROR_COPY_SUS_OBJECT_ASSEMBLE_AND_SIMULATE_RELATED_FILE.
     */
    private static final String ERROR_COPY_SUS_OBJECT_ASSEMBLE_AND_SIMULATE_RELATED_FILE = "Error  Copy sus Object Assemble and simulate related File: ";

    /**
     * The Constant PARSER_FILE_CREATION_FAILED.
     */
    private static final String PARSER_FILE_CREATION_FAILED = "parser File creation failed: ";

    /**
     * The Constant ERROR_PARSING_CB2_FILE.
     */
    private static final String ERROR_PARSING_CB2_FILE = "ERROR parsing CB2 File";

    /**
     * The Constant DESIGN_VARIABLE_FILE_WRITE_FAIL.
     */
    private static final String DESIGN_VARIABLE_FILE_WRITE_FAIL = "Design Variable file Write Fail :";

    /**
     * The Constant DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL.
     */
    private static final String DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL = "Design Variable file/dir Write Fail :";

    /**
     * The Constant PARSER.
     */
    private static final String PARSER = "parser";

    /**
     * The Constant SELECTION.
     */
    private static final String SELECTION = "selection";

    /**
     * The Constant TYPE.
     */
    private static final String TYPE = "type";

    /**
     * The Constant VALUE.
     */
    private static final String VALUE = "value";

    /**
     * The Constant TITLE.
     */
    private static final String TITLE = "title";

    /**
     * The Constant NODES.
     */
    private static final String NODES = "nodes";

    /**
     * The Constant FIELDS.
     */
    private static final String FIELDS = "fields";

    /**
     * The Constant DYNAMIC_BASE_PATH_KEY.
     */
    private static final String DYNAMIC_BASE_PATH_KEY = "dynamicBasePath";

    /**
     * The Constant TAB_KEY_SCHEME_OPTIONS.
     */
    private static final String TAB_KEY_SCHEME_OPTIONS = "Scheme Options";

    /**
     * The Constant IS_LOCATION.
     */
    private static final String IS_LOCATION = "isLocation";

    /**
     * The Constant INACTIVE.
     */
    private static final String INACTIVE = "Inactive";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant ITEMS.
     */
    private static final String ITEMS = "items";

    /**
     * The Constant CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE.
     */
    private static final String CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE = "text";

    /**
     * The Constant CUSTOM_ATTRIBUTES_FIELD_NAME.
     */
    public static final String CUSTOM_ATTRIBUTES_FIELD_NAME = "customAttributes.";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "name";

    /**
     * The Constant ID_FIELD.
     */
    private static final String ID_FIELD = "composedId.id";

    /**
     * The Constant LOG_MESSAGE_HAS_VISIBILTY.
     */
    private static final String LOG_MESSAGE_HAS_VISIBILTY = " has visibilty: ";

    /**
     * The Constant LOG_MESSAGE_WORKFLOW_WITH_STATUS.
     */
    private static final String LOG_MESSAGE_WORKFLOW_WITH_STATUS = "Workflow with status : ";

    /**
     * The Constant PLUGIN_OBJECT.
     */
    private static final String PLUGIN_OBJECT = "plugin_object";

    /**
     * The Constant VIEW_WORKFLOW_BY_ID_AND_VERSION_URL.
     */
    public static final String VIEW_WORKFLOW_BY_ID_AND_VERSION_URL = "view/workflow/{id}/version/{version.id}";

    /**
     * The constant BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE.
     */
    private static final String BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE = "/data/object/{objectId}/type/{typeId}/translation/{__value__}/ui/create";

    /**
     * The constant BIND_FROM_URL_FOR_OBJECT_TRANSLATION_UPDATE.
     */
    private static final String BIND_FROM_URL_FOR_OBJECT_TRANSLATION_UPDATE = "/data/object/{objectId}/type/{typeId}/translation/{__value__}/ui/update";

    /**
     * The Constant WORKFLOW_CONTEXT_URL.
     */
    public static final String WORKFLOW_CONTEXT_URL = "data/project/ui/edit/{objectId}";

    /**
     * The Constant VIEW_WORKFLOW_BY_ID_AND_VERSION_URL.
     */
    public static final String VIEW_WORKFLOW_BY_ID = "view/workflow/{id}";

    /**
     * The Constant VIEW_WORKFLOW_PROJECT_URL.
     */
    public static final String VIEW_WORKFLOW_PROJECT_URL = "view/workflow/project/{id}";

    /**
     * The Constant OBJECT_TYPE_ID_HIDDEN.
     */
    private static final String FIELD_TYPE_HIDDEN = "hidden";

    /**
     * The Constant PARAM_OBJECT_ID.
     */
    public static final String PARAM_OBJECT_ID = "{objectId}";

    /**
     * The Constant PARAM_VERSION_ID.
     */
    public static final String PARAM_VERSION_ID = "{versionId}";

    /**
     * The constant PARAM_TYPE_ID.
     */
    public static final String PARAM_TYPE_ID = "{typeId}";

    /**
     * The Constant COLON.
     */
    private static final String COLON = ":";

    /**
     * The Constant API_LOCATION_EXPORT_FILE.
     */
    private static final String API_LOCATION_EXPORT_FILE = "/api/core/location/export/staging/file";

    /**
     * The FILES_DIR.
     */
    private static final String FILES = "files";

    /**
     * The Constant UI_ALL_WORKFLOW_COLUMNS_NAME.
     */
    private static final String UI_ALL_WORKFLOW_COLUMNS_NAME = "name";

    /**
     * The Constant UI_ALL_WORKFLOW_CLOUMNS_URL.
     */
    private static final String UI_ALL_WORKFLOW_CLOUMNS_URL = "view/workflow/project/{id}";

    /**
     * The Constant WORKFLOW_CHANGE_STATUS_URL.
     */
    public static final String WORKFLOW_CHANGE_STATUS_URL = "update/data/object/{objectId}/version/{versionId}/status";

    /**
     * The Constant CHANGE_STATUS_TITLE.
     */
    private static final String CHANGE_STATUS_TITLE = "4100005x4";

    /**
     * The Constant TAB_NAME_DESIGNER.
     */
    private static final String TAB_KEY_DESIGNER = "designer";

    /**
     * The Constant MANAAGER_FEATURES.
     */
    private static final String MANAAGER_FEATURES = "manager";

    /**
     * The Constant LINK_TYPE_YES.
     */
    private static final String LINK_TYPE_YES = "Yes";

    /**
     * The Constant LINK_TYPE_NO.
     */
    private static final String LINK_TYPE_NO = "No";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant MULTIPLE.
     */
    private static final String MULTIPLE = "multiple";

    /**
     * The Constant DEFAULT.
     */
    private static final String DEFAULT = "Default";

    /**
     * The Constant FIELD_TYPE_SELECT.
     */
    public static final String FIELD_TYPE_SELECT = "select";

    /**
     * The Constant SELECTED_TRANSLATION_KEY.
     */
    public static final String SELECTED_TRANSLATIONS = "selectedTranslations";

    /**
     * The Constant SELECT_TRANSLATION_LABEL.
     */
    private static final String SELECT_TRANSLATION_LABEL = "3000213x4";

    private static final Map< String, Object > resultMapSectionList = new HashMap<>();

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * userDao reference for user database operation.
     */
    private WorkflowUserManager userManager;

    /**
     * workflowDao reference for workflow database operation.
     */
    private WorkflowDAO workflowDao;

    /**
     * The custom variable dao.
     */
    private VariableDAO variableDAO;

    /**
     * The license token manager.
     */
    private LicenseTokenManager licenseTokenManager;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The execution manager.
     */
    private WFExecutionManager executionManager;

    /**
     * The link DAO.
     */
    private LinkDAO linkDAO;

    /**
     * The context menu manager.
     */
    private ContextMenuManager contextMenuManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The template manager.
     */
    private TemplateManager templateManager;

    /**
     * The regex manager.
     */
    private RegexManager regexManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The bmw cae bench DAO.
     */
    private BmwCaeBenchDAO bmwCaeBenchDAO;

    /**
     * The scheme option schema DAO.
     */
    private SchemeSchemaDAO schemeSchemaDAO;

    /**
     * The parser manager.
     */
    private ParserManager parserManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The translation DAO.
     */
    private TranslationDAO translationDAO;

    /**
     * The ShapeModule Manager.
     */
    private ShapeModuleManager shapeModuleManager;

    /**
     * The translation DAO.
     */
    private JobDAO jobDao;

    /**
     * The Constant JAVA_PATH.
     */
    public static final String JAVA_PATH = PropertiesManager.getJavaRunTimePath();

    /**
     * The constructor which instantiates an object of the class.
     */
    public WorkflowManagerImpl() {
        super();
    }

    /**
     * Will creates system workflows.
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            insertSuperUserSecurityIdentity( entityManager );

            for ( final SystemWorkflow systemWorkFLow : SystemWorkflow.values() ) {
                if ( susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( systemWorkFLow.getId() ) )
                        == null ) {
                    final WorkflowDTO updateWorkflowDTO = new WorkflowDTO( UUID.fromString( systemWorkFLow.getId() ),
                            systemWorkFLow.getName(), systemWorkFLow.getDescription(), ConstantsID.SUPER_USER_ID );
                    saveWorkflow( entityManager, UUID.fromString( ConstantsID.SUPER_USER_ID ), null, updateWorkflowDTO,
                            JsonUtils.toJson( updateWorkflowDTO ) );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIPFromToken( String token ) {
        return null;
    }

    @Override
    public List< Object > getAllValuesForWorkflowSubProjectTableColumn( String projectId, String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var language = CommonUtils.resolveLanguage( token );
            var allColumns = GUIUtils.listColumns( WorkflowProjectDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            return susDAO.getAllPropertyValuesByParentId( entityManager, UUID.fromString( projectId ), columnName, language );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForWorkflowTableColumn( String projectId, String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var allColumns = GUIUtils.listColumns( LatestWorkFlowDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            return workflowDao.getAllPropertyValuesByParentId( entityManager, columnName, UUID.fromString( projectId ) );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< Object > getAllValuesForWorkflowProjectTableColumn( String projectId, String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var language = CommonUtils.resolveLanguage( token );

            var allColumns = GUIUtils.listColumns( GenericDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            if ( columnName.equalsIgnoreCase( ConstantsString.TYPE ) ) {
                final String projectType = ConfigUtil.labelNames( configManager.getMasterConfigurationFileNamesForWorkFlows() )
                        .get( FIRST_INDEX );
                final SuSObjectModel suSObjectModel = configManager.getProjectModelByConfigLabel( projectType );
                allValues = new ArrayList<>();
                allValues.add( suSObjectModel.getName() );
            } else {
                allValues = susDAO.getAllPropertyValuesByParentId( entityManager, UUID.fromString( projectId ), columnName, language );
            }
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonNode getConfig() {
        final String configFilePath =
                System.getProperty( ConstantsKaraf.KARAF_HOME ) + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                        + ConstantsString.CONFIG_FILE;
        final File initialConfigFile = new File( configFilePath );
        JsonNode config;
        try ( InputStream targetConfigStream = new FileInputStream( initialConfigFile ) ) {
            config = JsonUtils.convertInputStreamToJsonNode( targetConfigStream );
        } catch ( final IOException e ) {
            log.error( MessagesUtil.getMessage( WFEMessages.CONFIG_FILE_NOT_FOUND, initialConfigFile ), e );
            throw new SusException(
                    new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_FILE_NOT_FOUND, initialConfigFile ), e.getCause() ) );
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonNode getHpcConfig() {
        final String configFilePath =
                System.getProperty( ConstantsKaraf.KARAF_HOME ) + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                        + ConstantsString.HPC_CONFIG_FILE;
        final File initialConfigFile = new File( configFilePath );
        JsonNode config;
        try ( InputStream targetConfigStream = new FileInputStream( initialConfigFile ) ) {
            config = JsonUtils.convertInputStreamToJsonNode( targetConfigStream );
        } catch ( final IOException e ) {
            log.error( MessagesUtil.getMessage( WFEMessages.CONFIG_FILE_NOT_FOUND, initialConfigFile ), e );
            throw new SusException(
                    new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_FILE_NOT_FOUND, initialConfigFile ), e.getCause() ) );
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getEmailConfig() {
        Properties prop = new Properties();
        try ( InputStream stream = new FileInputStream( PropertiesManager.getEmailConf() ) ) {
            prop.load( stream );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return prop;
    }

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
    @Override
    public WorkflowDTO getWorkflowById( EntityManager entityManager, UUID userId, String workflowId ) {
        // validates if the user has the role work flow user, else throws exception
        if ( !isSystemWorkflow( workflowId ) ) {
            isWorkflowUser( entityManager, userId );
        }
        if ( !StringUtils.validateUUIDString( workflowId ) ) {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                    getClass() );
        }
        return getUserWorkflowById( entityManager, userId, workflowId );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO getNewWorkflowById( UUID userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getNewWorkflowById( entityManager, userId, workflowId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO getNewWorkflowById( EntityManager entityManager, UUID userId, String workflowId ) {

        try {
            // sir huzaifa requirement:
            List< UUID > selectionList = selectionManager.getSelectedIdsListBySelectionId( entityManager, workflowId );
            if ( selectionList != null && !selectionList.isEmpty() ) {
                workflowId = selectionList.get( 0 ).toString();
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }

        validateWorkflow( entityManager, workflowId );
        WorkflowDTO wf = getWorkflowById( entityManager, userId, workflowId );
        LatestWorkFlowDTO latestWf = prepareLatestWorkflowFromWorkflowDTO( wf );

        try {
            // duplicate selection if attribute are attached to selection
            org.json.JSONObject jsonObj = new org.json.JSONObject( JsonUtils.toJson( latestWf.getElements() ) );
            JSONArray nodes = ( JSONArray ) jsonObj.get( NODES );
            for ( int i = 0; i < nodes.length(); i++ ) {
                org.json.JSONObject nodeDetails = ( org.json.JSONObject ) nodes.get( i );

                org.json.JSONObject data = ( org.json.JSONObject ) nodeDetails.get( "data" );
                if ( data.get( "key" ).toString().equalsIgnoreCase( "wfe_io" ) ) {
                    JSONArray feildsList = ( JSONArray ) data.get( FIELDS );
                    for ( int j = 0; j < feildsList.length(); j++ ) {
                        org.json.JSONObject select = ( org.json.JSONObject ) feildsList.get( j );
                        if ( select.get( "type" ).toString().equalsIgnoreCase( "object" ) ) {
                            Object sId = select.get( VALUE );
                            select.remove( VALUE );
                            SelectionResponseUI selection = selectionManager.duplicateSelectionAndSelectionItemsIfAttributeExists(
                                    entityManager, sId.toString(), userId.toString(), "Duplicated" );
                            if ( selection != null ) {
                                select.put( VALUE, selection.getId() );
                            } else {
                                select.put( VALUE, sId );
                            }
                        } else if ( select.get( "type" ).toString().equalsIgnoreCase( "CB2 Objects" ) ) {
                            Object sId = select.get( VALUE );
                            select.remove( VALUE );
                            SelectionResponseUI selection = selectionManager.duplicateSelectionAndSelectionItemsIfAttributeExists(
                                    entityManager, sId.toString(), userId.toString(), "Duplicated" );
                            if ( selection != null ) {
                                select.put( VALUE, selection.getId() );
                            } else {
                                select.put( VALUE, sId );
                            }
                        }
                    }
                }
            }

            log.debug( jsonObj.toString() );
            Map< String, Object > elements = ( Map< String, Object > ) JsonUtils.jsonToMap( jsonObj.toString(), new HashMap<>() );
            latestWf.setElements( elements );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return latestWf;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getCustomFlagList() throws IOException {
        List< FileObject > pluginsList = FileUtils.getFilelist( getDynamicProperty(), false );
        List< String > mainPlugin = pluginsList.stream().map( FileObject::getTitle ).collect( Collectors.toList() );
        if ( PropertiesManager.isSMEnabled() ) {
            SMJSONSchema smjsonSchema = shapeModuleManager.getSMJsonSchema();
            mainPlugin.add( smjsonSchema.getTitle().toString().substring( 0, 11 ) );
        }
        return mainPlugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCustomFlagUI( String plugins, UriInfo uriInfo ) {
        List< UIFormItem > formItems = new ArrayList<>();
        List< String > pluginList = new ArrayList<>();
        if ( plugins.contains( ConstantsString.COMMA ) ) {
            pluginList = Arrays.asList( plugins.split( ConstantsString.COMMA ) );
        } else {
            pluginList.add( plugins );
        }
        for ( String plugin : pluginList ) {
            if ( plugin.contains( "ShapeModule" ) ) {
                formItems.addAll( shapeModuleManager.getSMInCustomFlag( uriInfo ) );
            } else {
                for ( Map< String, Object > field : getFieldsFromPluginPath( plugin ) ) {
                    SelectFormItem item = prepareSelectUIForCustomFlag( uriInfo, field, Boolean.FALSE );
                    formItems.add( item );
                }
            }
        }
        log.debug( "leaving getCustomFlagUI method" );
        return GUIUtils.createFormFromItems( formItems );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< String > getShapeModuleRunTabs( String plugins ) {
        List< SubTabsUI > subTabsUIs = new ArrayList<>();
        SMJSONSchema smjsonSchema = shapeModuleManager.getSMJsonSchema();
        List< String > allProperties = new ArrayList< String >( smjsonSchema.getProperties().keySet() );
        return allProperties;
    }

    /**
     * Gets the custom flag UI for rerun.
     *
     * @param userID
     *         the user ID
     * @param rerunJobId
     *         the rerun job id
     * @param plugins
     *         the plugins
     * @param uriInfo
     *         the uri info
     *
     * @return the custom flag UI for rerun
     */
    @Override
    public UIForm getCustomFlagUIForRerun( UUID userID, String rerunJobId, String plugins, UriInfo uriInfo ) {

        // item bindform urls will be implicitly different based on uriInfo
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        log.debug( "entered in getCustomFlagUI method" );
        if ( plugins.contains( "ShapeModule" ) ) {
            // treat normally if plugin is shape module i.e don't populate fields
            return getCustomFlagUI( plugins, uriInfo );
        } else {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            Map< String, Object > globalVariables;
            try {
                globalVariables = jobManager.getGlobalVariablesFromJobIdAndUserId( entityManager, UUID.fromString( rerunJobId ), userID );
            } finally {
                entityManager.close();
            }
            List< String > pluginList = new ArrayList<>();
            if ( plugins.contains( ConstantsString.COMMA ) ) {
                pluginList = Arrays.asList( plugins.split( ConstantsString.COMMA ) );
            } else {
                pluginList.add( plugins );
            }
            for ( String plugin : pluginList ) {
                for ( Map< String, Object > field : getFieldsFromPluginPath( plugin ) ) {
                    SelectFormItem item = prepareSelectUIForCustomFlagForRerun( plugin, uriInfo, field, globalVariables, Boolean.FALSE );
                    listUserDirectory.add( item );
                }
            }
            log.debug( "leaving getCustomFlagUI method" );
            return GUIUtils.createFormFromItems( listUserDirectory );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< CustomVariableDTO > getCustomVariableData( String userId, String workflowId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_ACCOUNT_FOUND_FOR_USER.getKey(),
                        SimuspaceFeaturesEnum.USERS.getKey() ) );
            }
            WorkflowDTO workflowDTO = getWorkflowById( entityManager, UUID.fromString( userId ), workflowId );
            String definitionJson = JsonUtils.toJson( workflowDTO.getDefinition() );
            final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( definitionJson, WorkflowDefinitionDTO.class );
            final UserWorkFlow userWorkFlow = new UserWorkflowImpl();
            if ( null != workflowDefinitionDTO.getElements() ) {
                userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
            }
            List< CustomVariableDTO > customVariables = new ArrayList<>();
            List< CustomVariableEntity > customVariableEntities = variableDAO.getAllCustomVariables( entityManager, workflowId, userId );
            if ( customVariableEntities == null || customVariableEntities.isEmpty() ) {
                customVariableEntities = cloneCustomVariablesFromWorkflowOwner( userId, entityManager, userWorkFlow, workflowDTO );
            }
            if ( CollectionUtil.isNotEmpty( customVariableEntities ) ) {
                for ( CustomVariableEntity customVariableEntity : customVariableEntities ) {
                    customVariables.add( prepareCustomVariableModelFromEntity( customVariableEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, customVariables );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Clone objective variables from wrokflow owner list.
     *
     * @param userId
     *         the user id
     * @param entityManager
     *         the entity manager
     * @param userWorkFlow
     *         the user work flow
     * @param workflowDTO
     *         the workflow dto
     *
     * @return the list
     */
    private List< CustomVariableEntity > cloneCustomVariablesFromWorkflowOwner( String userId, EntityManager entityManager,
            UserWorkFlow userWorkFlow, WorkflowDTO workflowDTO ) {
        List< CustomVariableEntity > newCustomVariables = new ArrayList<>();
        List< CustomVariableEntity > ownerCustomVariables = variableDAO.getAllCustomVariables( entityManager, workflowDTO.getId(),
                workflowDTO.getCreatedBy().getId() );
        if ( ownerCustomVariables == null || ownerCustomVariables.isEmpty() ) {
            return newCustomVariables;
        }
        for ( CustomVariableEntity ownerVariable : ownerCustomVariables ) {
            if ( !isVariableAskOnRun( ownerVariable.getName(), userWorkFlow ) ) {
                CustomVariableEntity newVariable = new CustomVariableEntity();
                newVariable.setUserId( UUID.fromString( userId ) );
                newVariable.setCreatedOn( new Date() );
                newVariable.setId( UUID.randomUUID() );
                newVariable.setNominalValue( ownerVariable.getNominalValue() );
                newVariable.setLabel( ownerVariable.getLabel() );
                newVariable.setName( ownerVariable.getName() );
                newVariable.setWorkflow( ownerVariable.getWorkflow() );
                newVariable.setFieldName( ownerVariable.getFieldName() );
                newVariable.setElementName( ownerVariable.getElementName() );
                newVariable.setType( ownerVariable.getType() );
                newCustomVariables.add( ( CustomVariableEntity ) variableDAO.saveOrUpdate( entityManager, newVariable ) );
            }
        }
        return newCustomVariables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobSubmitResponseDTO resubmitInterruptedJob( String jobParametersJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            JobParameters jobParameters = JsonUtils.jsonToObject( jobParametersJson, JobParameters.class );
            SusResponseDTO response = postJobToLocation( jobParameters.getJobRunByUserId(), jobParameters, null,
                    jobParameters.getJobRunByUserUID(), prepareLocationForJob( entityManager, jobParameters ) );
            if ( response == null || !response.getSuccess() ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_SUBMIT_JOB.getKey() ) );
            }
            return new JobSubmitResponseDTO( jobParameters.getId(), jobParameters.getName() );
        } catch ( SusException e ) {
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_SUBMIT_JOB.getKey() ), e );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public SelectionResponseUI duplicateSelection( String selectionId, String fieldType, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var oldSelection = selectionManager.getSelectionEntityById( entityManager, selectionId );
            if ( fieldType.equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
                log.info( "duplicating selection for parser" );
                String newSelectionId = duplicateSelectionForParser( entityManager, selectionId );
                SelectionResponseUI selectionResponse = new SelectionResponseUI();
                selectionResponse.setId( newSelectionId );
                return selectionResponse;
            } else if ( fieldType.equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                log.info( "duplicating selection for regex" );
                var newSelection = duplicateSelectionForRegex( entityManager, oldSelection );
                return selectionManager.prepareSelectionResponseFormSelectionEntity( entityManager, newSelection.getId().toString(),
                        oldSelection.getOrigin() );
            } else if ( fieldType.equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) ) {
                log.info( "duplicating selection for template" );
                var newSelection = duplicateSelectionForTemplate( entityManager, oldSelection );
                return selectionManager.prepareSelectionResponseFormSelectionEntity( entityManager, newSelection.getId().toString(),
                        oldSelection.getOrigin() );
            } else if ( isFieldValueSelectionId( fieldType ) ) {
                log.info( "duplicating selection for " + fieldType );
                var newSelection = duplicateSelectionForWorkflowFields( entityManager, oldSelection );
                return selectionManager.prepareSelectionResponseFormSelectionEntity( entityManager, newSelection.getId().toString(),
                        oldSelection.getOrigin() );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FIELD_IS_NOT_A_SELECTION_FIELD.getKey(), fieldType ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is variable ask on run.
     *
     * @param variableName
     *         the variable name
     * @param userWorkFlow
     *         the user work flow
     *
     * @return true, if is variable ask on run
     */
    private boolean isVariableAskOnRun( String variableName, UserWorkFlow userWorkFlow ) {

        boolean isAskOnRun = false;
        for ( final UserWFElement element : userWorkFlow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) && null != element.getFields() ) {
                for ( final Field< ? > field : element.getFields() ) {
                    if ( variableName.contains( element.getName() ) && variableName.contains( field.getName() ) && field.isChangeOnRun() ) {
                        isAskOnRun = true;
                    }
                }
            }
        }
        return isAskOnRun;
    }

    /**
     * Prepare objective variable model from entity.
     *
     * @param customVariableEntity
     *         the custom variable entity
     *
     * @return the objective variable DTO
     */
    private CustomVariableDTO prepareCustomVariableModelFromEntity( CustomVariableEntity customVariableEntity ) {
        CustomVariableDTO dto = null;
        if ( customVariableEntity != null ) {
            dto = new CustomVariableDTO( customVariableEntity.getLabel(), customVariableEntity.getName(), customVariableEntity.getType(),
                    customVariableEntity.getNominalValue(), customVariableEntity.getWorkflow().getComposedId().getId() );
            dto.setId( customVariableEntity.getId() );
            dto.setCreatedOn( customVariableEntity.getCreatedOn() );
            dto.setFieldName( customVariableEntity.getFieldName() );
            dto.setElementName( customVariableEntity.getElementName() );
        }
        return dto;
    }

    /**
     * Prepare select UI for custom flag.
     *
     * @param plugin
     *         the plugin
     * @param uriInfo
     *         the uri info
     * @param field
     *         the field
     * @param globalVariables
     *         the global variables
     * @param last
     *         the last
     *
     * @return the select UI
     */
    private SelectFormItem prepareSelectUIForCustomFlagForRerun( String plugin, UriInfo uriInfo, Map< String, Object > field,
            Map< String, Object > globalVariables, boolean last ) {
        SelectFormItem item = ( SelectFormItem ) prepareSelectUIItemFromField( field );

        if ( !last && !field.get( TYPE ).toString().equals( FieldTypes.INPUT_TABLE.getType() ) ) {
            item.setBindFrom( uriInfo.getRequestUri().getPath().replace( "/api/", ConstantsString.EMPTY_STRING )
                    + ConstantsCustomFlagFields.VALUE_POSTFIX );
        }
        Object customFlagValue = getFieldValueFromGlobalVariableMap( plugin, field, globalVariables );
        if ( customFlagValue != null ) {
            item.setValue( customFlagValue );
        }

        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCustomFlagPluginUI( String plugins, String value, UriInfo uriInfo ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        List< String > pluginList = new ArrayList<>();

        if ( plugins.contains( ConstantsString.COMMA ) ) {
            pluginList = Arrays.asList( plugins.split( ConstantsString.COMMA ) );
        } else {
            pluginList.add( plugins );
        }
        if ( plugins.contains( "ShapeModule" ) ) {
            listUserDirectory.addAll( shapeModuleManager.getCustomFlagPluginUI( plugins, value ) );
        }
        for ( String plugin : pluginList ) {
            if ( plugins.contains( "ShapeModule" ) ) {
                continue;
            }
            List< String > multiValueList = new ArrayList<>();
            String path = getListIfMultiSelect( value, multiValueList );
            for ( String val : multiValueList ) {
                String pluginPath = plugin + ConstantsString.FORWARD_SLASH + path + ConstantsString.FORWARD_SLASH + val;
                for ( Map< String, Object > field : getFieldsFromPluginPath( pluginPath ) ) {
                    listUserDirectory.add( prepareSelectUIItemFromField( field ) );
                }
            }
        }
        return GUIUtils.createFormFromItems( listUserDirectory );
    }

    /**
     * Prepare UI item from field.
     *
     * @param field
     *         the field
     *
     * @return the ui form item
     */
    public UIFormItem prepareSelectUIItemFromField( Map< String, Object > field ) {
        return prepareSelectUIItemFromFieldBasic( field );
    }

    /**
     * Prepare UI item from field.
     *
     * @param plugin
     *         the plugin
     * @param field
     *         the field
     * @param globalVariables
     *         the global variables
     *
     * @return the ui form item
     */
    public UIFormItem prepareSelectUIItemFromField( String plugin, Map< String, Object > field, Map< String, Object > globalVariables ) {
        var item = prepareSelectUIItemFromFieldBasic( field );
        Object customFlagValue = getFieldValueFromGlobalVariableMap( plugin, field, globalVariables );
        if ( customFlagValue != null ) {
            item.setValue( customFlagValue );
        }
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getCustomFlagPluginUIForRerun( UUID userID, String rerunJobId, String plugins, String value, UriInfo uriInfo ) {
        List< UIFormItem > listUserDirectory = new ArrayList<>();
        List< String > pluginList = new ArrayList<>();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Map< String, Object > globalVariables;
        try {
            globalVariables = jobManager.getGlobalVariablesFromJobIdAndUserId( entityManager, UUID.fromString( rerunJobId ), userID );
        } finally {
            entityManager.close();
        }
        if ( plugins.contains( ConstantsString.COMMA ) ) {
            pluginList = Arrays.asList( plugins.split( ConstantsString.COMMA ) );
        } else {
            pluginList.add( plugins );
        }
        if ( plugins.contains( "ShapeModule" ) ) {
            listUserDirectory = shapeModuleManager.getCustomFlagPluginUI( plugins, value );
        }
        for ( String plugin : pluginList ) {
            List< String > multiValueList = new ArrayList<>();
            String path = getListIfMultiSelect( value, multiValueList );
            for ( String val : multiValueList ) {
                String pluginPath = plugin + ConstantsString.FORWARD_SLASH + path + ConstantsString.FORWARD_SLASH + val;
                for ( Map< String, Object > field : getFieldsFromPluginPath( pluginPath ) ) {
                    listUserDirectory.add( prepareSelectUIItemFromField( plugin, field, globalVariables ) );
                }
            }
        }
        return GUIUtils.createFormFromItems( listUserDirectory );
    }

    /**
     * Gets the field value from global variable map.
     *
     * @param plugins
     *         the plugins
     * @param field
     *         the field
     * @param globalVariables
     *         the global variables
     *
     * @return the field value from global variable map
     */
    private Object getFieldValueFromGlobalVariableMap( String plugins, Map< String, Object > field,
            Map< String, Object > globalVariables ) {
        if ( field.containsKey( NAME_FIELD ) ) {
            String variableName = "{{" + plugins + ConstantsString.DOT + field.get( NAME_FIELD ) + "}}";
            if ( globalVariables != null && globalVariables.containsKey( variableName ) ) {
                return globalVariables.get( variableName );
            }
        }
        return null;
    }

    /**
     * Gets the list if multi select.
     *
     * @param value
     *         the value
     * @param multiValueList
     *         the multi value list
     *
     * @return the list if multi select
     */
    private String getListIfMultiSelect( String value, List< String > multiValueList ) {
        String path = "";
        if ( value.contains( ConstantsString.FORWARD_SLASH ) ) {
            String valueWithComma = value.substring(
                    value.lastIndexOf( ConstantsString.FORWARD_SLASH ) + ConstantsInteger.INTEGER_VALUE_ONE );
            path = value.substring( ConstantsInteger.INTEGER_VALUE_ZERO, value.lastIndexOf( ConstantsString.FORWARD_SLASH ) );
            if ( valueWithComma.contains( ConstantsString.COMMA ) ) {
                multiValueList.addAll( Arrays.asList( valueWithComma.split( ConstantsString.COMMA ) ) );
            } else {
                multiValueList.add( valueWithComma );
            }
        } else {
            multiValueList.add( value );
        }
        return path;
    }

    public List< Map< String, Object > > extractShapeModuleFields( Map< String, Object > map ) {
        List< String > excludeKeys = List.of( "workflow", "server", "requestHeaders", "Submit/Run" );
        String[] mainFieldNameArray = { "geometry_list", "analysis_wrapper_list", "mesh_motion_list", "parameterization_list",
                "mapper_list", "coupling_list", "optimization" };
        List< String > mainFieldNameList = Arrays.asList( mainFieldNameArray );
        Map< String, Object > innerField = new HashMap<>();
        List< Map< String, Object > > fields = new ArrayList<>();

        for ( String fieldName : mainFieldNameList ) {
            String mapKey = fieldName.replace( "_", " " );
            Object value = map.get( mapKey );
            Map< String, Object > geometryListMap = ( Map< String, Object > ) value;

            Map< String, Object > yamlData = processKeysAndValues( geometryListMap, 0, excludeKeys );

            if ( yamlData.containsKey( fieldName ) ) {
                Map< String, Object > fieldResult = processListData( yamlData, fieldName );
                Object result = fieldResult.get( fieldName );
                innerField.put( fieldName, result );
            }
        }
        innerField.put( "getSections", resultMapSectionList );
        fields.add( innerField );

        return fields;
    }

    public static Map< String, Object > processKeysAndValues( Map< String, Object > data, int indent, List< String > excludeKeys ) {
        Map< String, Object > result = new HashMap<>();

        for ( Map.Entry< String, Object > entry : data.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if ( !excludeKeys.contains( key ) ) {
                if ( value instanceof Map ) {
                    Map< String, Object > processedValue = processKeysAndValues( ( Map< String, Object > ) value, indent + 2, excludeKeys );
                    result.put( key, processedValue );
                } else if ( value instanceof List || ( key.contains( "_path" ) ) ) {
                    List< Object > listValue = ( List< Object > ) value;
                    List< Object > processedListValue = processList( listValue );
                    if ( key.contains( "_path" ) ) {
                        result.put( key, processedListValue.get( 0 ) );
                    } else {
                        result.put( key, processedListValue );
                    }
                } else {
                    result.put( key, value );
                }
            }
        }

        return result;
    }

    private static List< Object > processList( List< Object > list ) {
        for ( int i = 0; i < list.size(); i++ ) {
            Object item = list.get( i );
            if ( item instanceof Map ) {
                Map< String, Object > mapItem = ( Map< String, Object > ) item;
                list.set( i, mapItem.getOrDefault( "fullPath", "N/A" ) );
            }
        }
        return list;
    }

    private static Map< String, Object > replaceValuesRecursive( Map< String, Object > value, Map< String, Object > data ) {
        Map< String, Object > newDict = new HashMap<>();
        for ( Map.Entry< String, Object > entry : value.entrySet() ) {
            String k = entry.getKey();
            Object v = entry.getValue();

            if ( v instanceof Map ) {
                newDict.put( k, replaceValuesRecursive( ( Map< String, Object > ) v, data ) );
            } else if ( v instanceof List ) {
                List< ? > listv = ( List< ? > ) v;

                //
                for ( Object item : listv ) {
                    for ( int i = 0; i < 10; i++ ) {
                        if ( data.containsKey( item.toString() + String.valueOf( i ) ) ) {
                            Map< String, Object > listValuee = ( Map< String, Object > ) data.get( item + String.valueOf( i ) );
                            for ( Map.Entry< String, Object > entry1 : listValuee.entrySet() ) {
                                if ( ( item.toString().toLowerCase().contains( "response" ) || item.toString().toLowerCase()
                                        .contains( "tricks" ) || item.toString().toLowerCase().contains( "submesh" ) ) ) {
                                    resultMapSectionList.put( item + String.valueOf( i ), findName( listValuee ) );
                                }
                            }
                        }
                    }
                }
                //
                List< Object > newList = new ArrayList<>();
                if ( !listv.isEmpty() && listv.size() > 0 ) {
                    for ( Object item : listv ) {
                        for ( int i = 0; i < 10; i++ ) {
                            if ( data.get( item + String.valueOf( i ) ) != null ) {
                                newList.add(
                                        replaceValuesRecursive( ( Map< String, Object > ) data.get( item + String.valueOf( i ) ), data ) );
                            } else {
                                if ( !newList.contains( item ) ) {
                                    newList.add( item );
                                }
                            }
                        }
                    }
                    newDict.put( k, newList );
                } else if ( !listv.isEmpty() ) {
                    for ( Object item : listv ) {
                        for ( int i = 1; i < 10; i++ ) {
                            if ( data.containsKey( item + String.valueOf( i ) ) ) {
                                newList.add(
                                        replaceValuesRecursive( ( Map< String, Object > ) data.get( item + String.valueOf( i ) ), data ) );
                            } else {
                                if ( !newList.contains( item ) ) {
                                    newList.add( item );
                                }
                            }
                        }
                    }
                    newDict.put( k, newList );
                }
                // }
            } else if ( v instanceof String ) {
                for ( int i = 0; i < 10; i++ ) {
                    if ( data.containsKey( v + String.valueOf( i ) ) ) {
                        newDict.put( k, replaceValuesRecursive( ( Map< String, Object > ) data.get( v + String.valueOf( i ) ), data ) );
                    } else {
                        if ( !newDict.containsKey( k ) ) {
                            newDict.put( k, v );
                        }
                    }
                }
            } else {
                newDict.put( k, v );
            }
        }
        return newDict;

    }

    private static List< Map< String, Object > > processListInfo( Map< String, Object > geometryInfo ) {
        List< Map< String, Object > > processedList = new ArrayList<>();
        Map< String, Object > currentItem = new HashMap<>();

        for ( Map.Entry< String, Object > entry : geometryInfo.entrySet() ) {
            String key = entry.getKey();
            Object val = entry.getValue();

            if ( val instanceof Map ) {
                currentItem.put( key, new HashMap<>( ( Map< String, Object > ) val ) );
            } else if ( val instanceof List< ? > ) {
                List< Map< String, Object > > multiplelist = new ArrayList<>();
                for ( Object item : ( List< ? > ) val ) {
                    if ( item instanceof Map ) {
                        multiplelist.add( new HashMap<>( ( Map< String, Object > ) item ) );
                    }
                }
                currentItem.put( key, multiplelist );
            } else {
                currentItem.put( key, val );
            }
        }

        processedList.add( currentItem );
        return processedList;
    }

    private static void processSingleValue( Object val, Map< String, Object > infoMap, String key ) {
        if ( val instanceof Map ) {
            Map< String, Object > nestedMap = new HashMap<>();
            processNestedMap( val, nestedMap, key );
            infoMap.put( key, nestedMap );
        } else if ( val instanceof List ) {
            processNestedList( val, infoMap, key );
        } else {
            infoMap.put( key, val );
        }
    }

    private static void processNestedMap( Object val, Map< String, Object > nestedMap, String parentKey ) {
        if ( val instanceof Map ) {
            for ( Map.Entry< String, Object > entry : ( ( Map< String, Object > ) val ).entrySet() ) {
                String currentKey = entry.getKey();
                Object currentVal = entry.getValue();

                if ( currentVal instanceof Map ) {
                    for ( Map.Entry< String, Object > entry111 : ( ( Map< String, Object > ) currentVal ).entrySet() ) {
                        String currentKey111 = entry111.getKey();
                        Object currentVal111 = entry111.getValue();
                        if ( currentVal instanceof Map ) {
                            for ( Map.Entry< String, Object > entry112 : ( ( Map< String, Object > ) currentVal111 ).entrySet() ) {
                                String currentKey112 = entry112.getKey();
                                Object currentVal112 = entry112.getValue();
                                try {
                                    int parsedValue = Integer.parseInt( currentKey112 );
                                    currentKey112 = currentKey;
                                } catch ( NumberFormatException e ) {
                                    currentKey112 = currentKey112;
                                }
                                if ( currentVal112 instanceof Map ) {
                                    nestedMap.put( currentKey112,
                                            getLastObjectAndIgnoreFirstKeys( ( Map< String, Object > ) currentVal112 ) );
                                } else if ( currentVal112 instanceof List ) {
                                    processNestedList( currentVal112, nestedMap, currentKey112 );
                                } else {
                                    nestedMap.put( currentKey112, currentVal112 );
                                }
                            }
                        }
                    }
                } else if ( currentVal instanceof List ) {
                    processNestedList( currentVal, nestedMap, currentKey );
                } else {
                    nestedMap.put( currentKey, currentVal );
                }
            }
        }
    }

    public static Map< String, Object > getLastObjectAndIgnoreFirstKeys( Map< String, Object > input ) {
        Map.Entry< String, Object > entry = input.entrySet().iterator().next();
        Object value = entry.getValue();

        if ( value instanceof Map ) {
            return getLastObjectAndIgnoreFirstKeys( ( Map< String, Object > ) value );
        } else {
            return input;
        }
    }

    private static void processNestedList( Object val, Map< String, Object > infoMap, String key ) {
        if ( val instanceof List ) {
            List< Map< String, Object > > furtherInfo = new ArrayList<>();
            List< Object > furtherInfo1 = new ArrayList<>();
            for ( Object item : ( List< ? > ) val ) {
                if ( item instanceof Map ) {
                    Map< String, Object > subMap = new HashMap<>();
                    for ( Map.Entry< String, Object > vEntry : ( ( Map< String, Object > ) item ).entrySet() ) {
                        if ( vEntry.getValue() instanceof Map ) {
                            for ( Map.Entry< String, Object > vEntryValue : ( ( Map< String, Object > ) vEntry.getValue() ).entrySet() ) {
                                Map< String, Object > subMap1 = new HashMap<>();
                                processNestedMap( vEntryValue.getValue(), subMap1, key );
                                furtherInfo.add( subMap1 );
                            }
                        } else {
                            processNestedMap( vEntry.getValue(), subMap, key );
                            furtherInfo.add( subMap );
                        }
                    }
                    infoMap.put( key, furtherInfo );
                } else if ( key.contains( "_list" ) || key.equals( "files_to_keep" ) ) {
                    furtherInfo1.add( item );
                    infoMap.put( key, furtherInfo1 );
                    if ( key.equals( "files_to_keep" ) ) {

                    }
                }
                // else {
                // furtherInfo.put( item );
                // }
            }
            // infoMap.put( key, furtherInfo );
        }

    }

    private static void processListValue( Map< String, Object > listValue, List< Map< String, Object > > resultList,
            Map< String, Object > data ) {
        for ( Map.Entry< String, Object > entry : listValue.entrySet() ) {
            String i = entry.getKey();
            Map< String, Object > value = ( Map< String, Object > ) entry.getValue();
            boolean containsIntegerKey = value.keySet().stream().anyMatch( key -> key.matches( "\\d+" ) );

            Map< String, Object > finalValue; // Declare finalValue variable here

            if ( i.matches( "(true|false)" ) && containsIntegerKey ) {
                for ( Map.Entry< String, Object > ent : value.entrySet() ) {
                    String key11 = ent.getKey();
                    Map< String, Object > value11 = ( Map< String, Object > ) ent.getValue();
                    finalValue = replaceValuesRecursive( value11, data );

                    Map< String, Object > listInfo = new HashMap<>();
                    for ( Map.Entry< String, Object > finalValueEntry : finalValue.entrySet() ) {
                        String keyy = finalValueEntry.getKey();
                        Object val = finalValueEntry.getValue();
                        processSingleValue( val, listInfo, keyy );
                    }

                    List< Map< String, Object > > processedList = processListInfo( listInfo );
                    resultList.addAll( processedList );
                }

            } else if ( i.matches( "\\d+" ) && value.containsKey( i ) ) {
                finalValue = ( Map< String, Object > ) value.get( i );
                finalValue = replaceValuesRecursive( finalValue, data );

                Map< String, Object > listInfo = new HashMap<>();
                for ( Map.Entry< String, Object > finalValueEntry : finalValue.entrySet() ) {
                    String keyy = finalValueEntry.getKey();
                    Object val = finalValueEntry.getValue();
                    processSingleValue( val, listInfo, keyy );
                }

                List< Map< String, Object > > processedList = processListInfo( listInfo );
                resultList.addAll( processedList );
            }
        }
    }

    private static Map< String, Object > processListData( Map< String, Object > data, String listName ) {
        List< Map< String, Object > > resultList = new ArrayList<>();
        Map< String, Object > result = new HashMap<>();
        if ( data.get( listName ) != null && !( data.get( listName ) instanceof List ) ) {
            Map< String, Object > listValue = ( Map< String, Object > ) data.get( data.get( listName ).toString() + String.valueOf( 0 ) );
            processListValue( listValue, resultList, data );
            result.put( listName, resultList.get( 0 ) );
        } else {
            for ( String key : ( List< String > ) data.getOrDefault( listName, new ArrayList<>() ) ) {
                for ( int i = 1; i < 11; i++ ) {
                    if ( data.containsKey( key + String.valueOf( i ) ) ) {
                        // if ( data.containsKey( key ) ) {
                        Map< String, Object > listValue = ( Map< String, Object > ) data.get( key + String.valueOf( i ) );
                        // Map< String, Object > listValue = ( Map< String, Object > ) data.get( key );
                        processListValue( listValue, resultList, data );
                        for ( Map.Entry< String, Object > entry : listValue.entrySet() ) {
                            if ( ( key.toLowerCase().contains( "geometry" ) || listName.contains( "wrapper" ) || listName.contains(
                                    "parameterization" ) ) && entry.getKey().matches( "(true|false)" ) ) {
                                resultMapSectionList.put( key + String.valueOf( i ), findName( listValue ) );
                            }
                        }
                    }
                }
            }
            result.put( listName, resultList );
        }

        return result;
    }

    private static String findName( Map< String, Object > obj ) {
        for ( Map.Entry< String, Object > entry : obj.entrySet() ) {
            if ( "name".equals( entry.getKey() ) ) {
                return entry.getValue().toString();
            } else if ( entry.getValue() instanceof Map ) {
                String result = findName( ( Map< String, Object > ) entry.getValue() );
                if ( result != null ) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFieldsRecursively( String plugin, Map< String, Object > map, List< Map< String, Object > > fieldMap ) {
        List< Map< String, Object > > fields = null;
        if ( plugin.contains( "ShapeModule" ) ) {
            fields = extractShapeModuleFields( map );
            fieldMap.addAll( fields );
        } else {
            fields = getFieldsFromPluginPath( plugin );
            if ( fields.isEmpty() ) {
                String path = getDynamicProperty() + ConstantsString.FORWARD_SLASH + plugin;
                String lastLayoutFilePath = path.substring( ConstantsInteger.INTEGER_VALUE_ZERO,
                        path.lastIndexOf( ConstantsString.FORWARD_SLASH ) + ConstantsInteger.INTEGER_VALUE_ONE ) + "layout.json";
                List< Map< String, Object > > lastJsonfields = new ArrayList<>();
                var dynamicProperties = getDynamicProperties( lastLayoutFilePath );

                if ( dynamicProperties != null ) {
                    lastJsonfields = ( List< Map< String, Object > > ) JsonUtils.jsonToList( JsonUtils.toJsonString( dynamicProperties ),
                            lastJsonfields );
                    for ( Map< String, Object > lastJsonField : lastJsonfields ) {
                        if ( lastJsonField.get( TYPE ).equals( FieldTypes.SELECTION.getType() ) ) {
                            fieldMap.add( lastJsonField );
                        }
                    }
                }
            }
            for ( Map< String, Object > field : fields ) {
                if ( field.get( TYPE ).equals( FieldTypes.SELECTION.getType() ) ) {
                    String newPath = plugin + ConstantsString.FORWARD_SLASH + map.get( field.get( NAME_FIELD ).toString() );
                    fieldMap.add( field );
                    getFieldsRecursively( newPath, map, fieldMap );
                } else {
                    fieldMap.add( field );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return the dynamic property
     */
    private String getDynamicProperty() {
        return PropertiesManager.getDynamicPathByKey( DYNAMIC_BASE_PATH_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Map< String, Object > > getFieldsFromPluginPath( String plugin ) {
        List< Map< String, Object > > fields = new ArrayList<>();
        String pluginPath = "";
        if ( plugin.contains( "ShapeModule" ) ) {
            List< UIFormItem > items = new ArrayList<>();
            if ( plugin.equals( "ShapeModule" ) ) {
                items = shapeModuleManager.getCustomFlagPluginUI( "ShapeModule", plugin + ".false" );
            } else {
                String[] value = plugin.split( "/" );
                items = shapeModuleManager.getCustomFlagPluginUI( "ShapeModule", value[ 1 ].replace( "[", "" ).replace( "]", "" ) );
            }

            fields = items.stream().map( this::toMap ).collect( Collectors.toList() );
        } else {
            pluginPath = getDynamicProperty() + ConstantsString.FORWARD_SLASH + plugin + ConstantsString.FORWARD_SLASH + "layout.json";
            if ( new File( pluginPath ).exists() ) {
                var dynamicProperties = getDynamicProperties( pluginPath );
                if ( dynamicProperties != null ) {
                    fields = ( List< Map< String, Object > > ) JsonUtils.jsonToList( JsonUtils.toJsonString( dynamicProperties ), fields );
                }
            }
        }
        return fields;
    }

    private Map< String, Object > toMap( UIFormItem item ) {
        Map< String, Object > result = new HashMap<>();
        result = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( item ), result );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonNode getDynamicProperties( String path ) {
        JsonNode jsonDynamicPathObject;
        try ( InputStream dummyFileStream = new FileInputStream( path ) ) {
            jsonDynamicPathObject = JsonUtils.convertInputStreamToJsonNode( dummyFileStream );
        } catch ( FileNotFoundException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), path ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey(), path ) );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), path ), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.COULD_NOT_READ_FILE.getKey(), path ) );
        }
        return jsonDynamicPathObject != null ? jsonDynamicPathObject.get( FIELDS ) : null;
    }

    /**
     * Checks if workflow Id belongs to a valid workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow Id
     */
    private void validateWorkflow( EntityManager entityManager, String workflowId ) {
        final List< UUID > wfList = workflowDao.getWorkflowIdsList( entityManager );
        boolean isWorkflowValid = false;
        for ( final UUID UserWorkflow : wfList ) {
            if ( workflowId.equals( UserWorkflow.toString() ) ) {
                isWorkflowValid = true;
                break;
            }
        }
        if ( !isWorkflowValid ) {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.SELECTED_ITEM_IS_NOT_A_WORKFLOW ) ), getClass() );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO getWorkflowBySelectionId( UUID userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< UUID > selectedObjects = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectionId );
            return prepareLatestWorkflowFromWorkflowDTO(
                    getWorkflowById( entityManager, userId, selectedObjects.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() ) );
        } finally {
            entityManager.close();
        }
    }

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
     * @return the workflow and fill with job params
     */
    @Override
    public WorkflowDTO getWorkflowByIdWithRefJobParams( UUID userId, String workflowId, Job job ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role work flow user, else throws exception
            isWorkflowUser( entityManager, userId );

            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        getClass() );
            }

            final WorkflowDTO workflow = getWorkflowByIdAndVersionId( entityManager, userId, workflowId, job.getWorkflowVersion().getId() );
            checkStatusOfWorkflow( workflow );

            return workflow;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Check workflow is executable or not.
     *
     * @param workflow
     *         the workflow
     */
    private void checkStatusOfWorkflow( WorkflowDTO workflow ) {
        // if workflow status = deprecated prompted warning message for user

        if ( ( workflow.getLifeCycleStatus() != null ) && !workflow.isExecutable() ) {
            log.error( "Workflow is not executable according to status configration" );
            throw new SusException( new Exception( "WOrkflow is not executable according to configration" ),
                    WorkflowExecutionManagerImpl.class );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowDTO > getWorkflowList( UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< WorkflowDTO > resultList = new ArrayList<>();
            isWorkflowUser( entityManager, userId );
            final List< UUID > wfList = workflowDao.getWorkflowIdsList( entityManager );

            if ( CollectionUtil.isNotEmpty( wfList ) ) {
                for ( final UUID entity : wfList ) {
                    final WorkflowDTO workflowDTO = getUserWorkflowById( entityManager, userId, entity.toString() );
                    resultList.add( workflowDTO );
                }
            }
            // to sort the workflows list
            resultList.sort( Comparator.comparing( WorkflowDTO::getCreatedOn, Comparator.nullsLast( Comparator.reverseOrder() ) ) );

            return resultList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the filtered workflow list.
     *
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     *
     * @return the filtered workflow list
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getFilteredWorkflowList(java.util.UUID,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public FilteredResponse< LatestWorkFlowDTO > getFilteredWorkflowList( UUID userId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isWorkflowUser( entityManager, userId );
            final List< LatestWorkFlowDTO > objectDTOList = new ArrayList<>();
            final String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName(
                    ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME ).get( 0 ).getId();
            final List< SuSEntity > dataObjectEntityList = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager,
                    WorkflowEntity.class, filter, null, userId.toString(), lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ), new ArrayList<>() );
            if ( CollectionUtil.isNotEmpty( dataObjectEntityList ) ) {
                for ( final SuSEntity dataObjectEntity : dataObjectEntityList ) {
                    objectDTOList.add( prepareWorkflowDTO( ( WorkflowEntity ) dataObjectEntity ) );
                }
            }

            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenDetails getToken( Message currentMessage, UUID userId, String userName ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // validates if the user has the role work flow user, else throws exception
        try {
            isWorkflowUser( entityManager, userId );
            return null;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowEntity prepareWorkflowEntity( WorkflowDTO dto ) {
        final WorkflowEntity entity = new WorkflowEntity();

        final VersionPrimaryKey pk = new VersionPrimaryKey();
        if ( dto.getId() != null ) {
            pk.setId( UUID.fromString( dto.getId() ) );
        } else {
            pk.setId( UUID.randomUUID() );
        }
        // set Composed Id to WorkflowEntity.
        entity.setComposedId( pk );
        // Set Entity Object
        entity.setName( dto.getName() );
        entity.setActive( true );
        entity.setDescription( dto.getDescription() );
        entity.setComments( dto.getComments() );
        entity.setTypeId( dto.getTypeId() );
        entity.setConfig( dto.getConfig() );

        if ( dto.getVersion() != null ) {
            entity.setVersionId( dto.getVersion().getId() );
        }

        if ( dto.getCreatedBy() != null ) {
            final UserEntity user = userManager.prepareUserEntity( dto.getCreatedBy() );
            entity.setKeyuser( user );
            entity.setCreatedBy( user );
            entity.setOwner( user );
        }

        entity.setDefinition( CollectionUtil.convertMapToByteArray( dto.getDefinition() ) );
        entity.setCustomFlags( dto.getCustomFlags() );

        return entity;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowDTO updateWorkflow( EntityManager entityManager, UUID userId, String workflowId, WorkflowDTO workflowDto,
            String workflowJson ) {
        // validates if the user has the role work flow manager, else throws exception
        isWorkflowManager( entityManager, userId );

        if ( StringUtils.isNotNullOrEmpty( workflowId ) && ( workflowDto != null ) ) {

            // get work flow with latest version
            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        getClass() );
            }

            if ( !permissionManager.isWritable( entityManager, userId, UUID.fromString( workflowId ) ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), "this workflow" ) );
            }
            final WorkflowDTO oldWorkflowDTO = getWorkflowById( entityManager, userId, workflowId );
            workflowDto.validate();

            workflowDto.setUpdatedBy( new UserDTO( ConstantsString.EMPTY_STRING + userId ) );
            // check if all versions of work flow deleted then prompt error message
            if ( workflowDao.isAllVersionsOfWorkflowDeleted( entityManager, UUID.fromString( workflowId ) ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        getClass() );
            }
            workflowDto.setId( workflowId );
            final WorkflowEntity entity = prepareWorkflowEntity( workflowDto );
            List< SuSEntity > parents = susDAO.getParents( entityManager, entity );
            if ( CollectionUtils.isNotEmpty( parents ) && !isNameUniqueAmongSiblings( entityManager, workflowDto.getName(), entity,
                    susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                            parents.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getComposedId().getId() ) ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey(), entity.getName() ) );
            }
            // null==customFlags means update workflow through edit form
            if ( workflowDto.getDefinition() == null || null == workflowDto.getCustomFlags() ) {
                entity.setDefinition( CollectionUtil.convertMapToByteArray( oldWorkflowDTO.getDefinition() ) );
            }
            setUserAttributes( entityManager, userId, workflowDto, entity );

            final StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( LatestWorkFlowDTO.TYPE_ID.toString(),
                    oldWorkflowDTO.getConfig() );
            entity.setLifeCycleStatus( dto.getId() );
            entity.setCreatedOn( oldWorkflowDTO.getCreatedOn() );
            entity.setTypeId( oldWorkflowDTO.getTypeId() );
            entity.setConfig( oldWorkflowDTO.getConfig() );
            // get version of work flow and add 1
            entity.setVersionId( entity.getVersionId() + ConstantsInteger.INTEGER_VALUE_ONE );
            entity.setModifiedOn( new Date() );
            entity.setModifiedBy( userCommonManager.getUserEntityById( entityManager, userId ) );
            entity.setUserSelectionId( oldWorkflowDTO.getUserSelectionId() );
            entity.setGroupSelectionId( oldWorkflowDTO.getGroupSelectionId() );
            entity.setRunOnLocation( getLocationEntities( entityManager, userId, workflowDto ) );
            entity.setLocations( workflowDto.getLocations() );
            entity.setCustomFlags( workflowDto.getCustomFlags() );
            if ( workflowJson.contains( SELECTED_TRANSLATIONS ) ) {
                entity.setTranslation( setMultiNames( entityManager, workflowJson ) );
                try {
                    entity.setSelectedTranslations(
                            JsonUtils.toJson( new ObjectMapper().readTree( workflowJson ).path( SELECTED_TRANSLATIONS ) ) );
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage() );
                }
            } else {
                WorkflowEntity workflowEntity = workflowDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( workflowId ) );
                entity.setTranslation( prepareTranslations( entityManager, workflowEntity.getTranslation() ) );
                entity.setSelectedTranslations( workflowEntity.getSelectedTranslations() );
            }
            workflowDao.updateWorkflow( entityManager, entity );
            selectionManager.sendCustomerEvent( entityManager, userId.toString(), entity, "update" );
            return prepareWorkflowDTO( entityManager, userId, entity );
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_ID_SHOULD_NOT_EMPTY ) ), getClass() );
        }
    }

    /**
     * Prepare translations list.
     *
     * @param translation
     *         the translation
     *
     * @return the list
     */
    private Set< TranslationEntity > prepareTranslations( EntityManager entityManager, Set< TranslationEntity > translation ) {
        Set< TranslationEntity > entityList = new HashSet<>();
        for ( TranslationEntity translationEntity : translation ) {
            TranslationEntity entity = new TranslationEntity();
            entity.setId( UUID.randomUUID() );
            entity.setLanguage( translationEntity.getLanguage() );
            entity.setName( translationEntity.getName() );
            translationDAO.saveOrUpdate( entityManager, entity );
            entityList.add( entity );
        }
        return entityList;
    }

    /**
     * Gets the relation list by property.
     *
     * @param id
     *         the id
     *
     * @return the relation list by property
     */
    @Override
    public boolean getRelationListByProperty( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // getting all relations
            log.info( "getting all relations list" );
            final List< Relation > relationList = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.PARENT, UUID.fromString( id ) );
            log.info( "relation list : " + relationList );
            log.info( "deleting relations" );
            if ( CollectionUtil.isNotEmpty( relationList ) ) {
                // deleting previous relation entries
                susDAO.deleteRelation( entityManager, relationList );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the relation.
     *
     * @param relationObj
     *         the relation obj
     *
     * @return the relation
     */
    @Override
    public Relation createRelation( Relation relationObj ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return susDAO.createRelation( entityManager, relationObj );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the location entities.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowDto
     *         the workflow dto
     *
     * @return the location entities
     */
    private List< LocationEntity > getLocationEntities( EntityManager entityManager, UUID userId, WorkflowDTO workflowDto ) {
        final List< LocationEntity > locations = new ArrayList<>();
        if ( ( null != workflowDto.getRunOnLocation() ) && !workflowDto.getRunOnLocation().isEmpty() ) {
            for ( final LocationDTO location : workflowDto.getRunOnLocation() ) {

                if ( location.getId().toString().equals( ConstantsID.LOCAL_LINUX_ID ) || location.getId().toString()
                        .equals( ConstantsID.LOCAL_WINDOWS_ID ) ) {
                    final LocationEntity locationEntity = new LocationEntity( location.getId() );
                    locationEntity.setName( location.getName() );
                    locationEntity.setInternal( true );
                    locations.add( locationEntity );
                } else if ( Boolean.TRUE.equals( PropertiesManager.isHostEnable() ) ) {
                    locations.add(
                            prepareLocationEntity( locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() ) ) );
                } else {
                    locations.add( prepareLocationEntity( locationManager.getLocation( entityManager, location.getId().toString() ) ) );
                }
            }
        } else {
            locations.add( prepareLocationEntity( locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() ) ) );

            workflowDto.setLocations( selectionManager.createSelectionForSingleItem( entityManager, userId.toString(),
                    SelectionOrigins.WORKFLOW_LOCATION.getOrigin(), LocationsEnum.DEFAULT_LOCATION.getId() ).getId() );
        }

        return locations;
    }

    /**
     * Prepare location entity.
     *
     * @param location
     *         the location
     *
     * @return the location entity
     */
    private LocationEntity prepareLocationEntity( LocationDTO location ) {
        return new LocationEntity( location.getId(), location.getDescription(), location.getName(),
                org.apache.commons.lang.StringUtils.equals( location.getStatus(), ConstantsStatus.ACTIVE ) );
    }

    /**
     * Prepare location entities.
     *
     * @param locationEntities
     *         the location entities
     *
     * @return the list
     */
    private List< LocationDTO > prepareLocationDTO( List< LocationEntity > locationEntities ) {
        final List< LocationDTO > locationList = new ArrayList<>();
        for ( final LocationEntity entity : locationEntities ) {
            final LocationDTO location = new LocationDTO( entity.getId(), entity.getName() );
            location.setStatus( Boolean.TRUE.equals( entity.isStatus() ) ? ConstantsStatus.ACTIVE : ConstantsStatus.INACTIVE );
            locationList.add( location );
        }
        return locationList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO updateWorkflow( UUID userId, String workflowId, LatestWorkFlowDTO dto, String workflowJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return updateWorkflow( entityManager, userId, workflowId, dto, workflowJson, false );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO updateWorkflow( EntityManager entityManager, UUID userId, String workflowId, LatestWorkFlowDTO dto,
            String workflowJson, boolean isImport ) {
        if ( org.apache.commons.lang3.StringUtils.isNotBlank( dto.getLocations() ) ) {
            final List< LocationDTO > locations = locationManager.getAllLocationsWithSelection( entityManager, userId.toString(),
                    dto.getLocations() );
            dto.setRunOnLocation( locations );
        } else {
            final List< LocationDTO > locations = new ArrayList<>();
            locations.add( locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() ) );
            dto.setRunOnLocation( locations );
        }
        final WorkflowDTO workflow = prepareWorkflowDTOFromNewDTO( dto );
        if ( isImport ) {
            updateSelectionsInNewDTO( entityManager, workflow );
        }
        final WorkflowDTO updated = updateWorkflow( entityManager, userId, workflowId, workflow, workflowJson );

        new Thread( () -> {
            log.debug( "calling saveVariables method in thread" );
            EntityManager threadEntityManager = entityManagerFactory.createEntityManager();
            try {
                saveVariables( threadEntityManager, userId.toString(), workflowId, updated );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            } finally {
                threadEntityManager.close();
            }
        } ).start();

        return prepareLatestWorkflowFromWorkflowDTO( updated );
    }

    /**
     * Validate all html fields in elements.
     *
     * @param workflow
     *         the workflow
     */
    private void validateAllHtmlFieldsInElements( WorkflowDTO workflow ) {
        var definition = WorkflowDefinitionUtil.getWorkflowDefinitionAsJSONObject( workflow.getDefinition() );
        if ( definition == null ) {
            return;
        }
        var nodesJson = WorkflowDefinitionUtil.getNodesFromDefinitionAsJSONObject( definition );
        if ( nodesJson != null ) {
            nodesJson.forEach( element -> validateAllHtmlFieldsInElement( ( JSONObject ) element ) );
            workflow.setDefinition(
                    ( java.util.Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( definition ), new HashMap<>() ) );
        }
    }

    private void validateAllHtmlFieldsInElement( JSONObject elementJson ) {
        if ( elementJson == null ) {
            return;
        }
        var elementDataJson = ( JSONObject ) elementJson.get( WorkflowDefinitionUtil.DATA );
        if ( elementDataJson == null ) {
            return;
        }
        validateGeneralHtmlInElements( elementDataJson );
        var elementFieldsJson = ( org.json.simple.JSONArray ) elementDataJson.get( WorkflowDefinitionUtil.FIELDS );
        if ( elementFieldsJson == null ) {
            return;
        }
        elementFieldsJson.forEach( jsonField -> validateHtmlFieldsInElement( ( JSONObject ) jsonField ) );

    }

    /**
     * Validate html fields in element.
     *
     * @param jsonField
     *         the json field
     */
    private void validateHtmlFieldsInElement( JSONObject jsonField ) {
        String fieldName = ( String ) jsonField.get( UI_ALL_WORKFLOW_COLUMNS_NAME );
        if ( "smtp-message".equals( fieldName ) || "unix-message".equals( fieldName ) ) {
            String value = ( String ) jsonField.get( WorkflowDefinitionUtil.VALUE );
            if ( !ValidationUtils.isValidHtml( value ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE_IN.getKey(), "Message", ElementKeys.WFE_EMAIL ) );
            }
        }
    }

    /**
     * Udpate selections in new DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param workflow
     *         the workflow
     */
    private void updateSelectionsInNewDTO( EntityManager entityManager, WorkflowDTO workflow ) {
        var definition = WorkflowDefinitionUtil.getWorkflowDefinitionAsJSONObject( workflow.getDefinition() );
        if ( definition == null ) {
            return;
        }
        var nodesJson = WorkflowDefinitionUtil.getNodesFromDefinitionAsJSONObject( definition );
        if ( nodesJson != null ) {
            nodesJson.forEach( element -> duplicateSelectionEntitiesInElement( entityManager, ( JSONObject ) element ) );
            workflow.setDefinition(
                    ( java.util.Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( definition ), new HashMap<>() ) );
        }
    }

    /**
     * Duplicate selection entities in elementJson.
     *
     * @param entityManager
     *         the entity manager
     * @param elementJson
     *         the elementJson
     */
    private void duplicateSelectionEntitiesInElement( EntityManager entityManager, JSONObject elementJson ) {
        if ( elementJson == null ) {
            return;
        }
        var elementDataJson = ( JSONObject ) elementJson.get( WorkflowDefinitionUtil.DATA );
        if ( elementDataJson == null ) {
            return;
        }
        validateGeneralHtmlInElements( elementDataJson );
        var elementFieldsJson = ( org.json.simple.JSONArray ) elementDataJson.get( WorkflowDefinitionUtil.FIELDS );
        if ( elementFieldsJson == null ) {
            return;
        }
        elementFieldsJson.forEach(
                jsonField -> duplicateSelectionAndUpdateField( entityManager, elementDataJson, ( JSONObject ) jsonField, elementJson ) );

    }

    private void validateGeneralHtmlInElements( JSONObject elementDataJson ) {
        String name = ( String ) elementDataJson.get( UI_ALL_WORKFLOW_COLUMNS_NAME );
        String comment = ( String ) elementDataJson.get( "comments" );
        if ( !ValidationUtils.isValidHtml( comment ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE_IN.getKey(),
                    MessageBundleFactory.getMessage( "3000082x4" ), name ) );
        }
        String description = ( String ) elementDataJson.get( "description" );
        if ( !ValidationUtils.isValidHtml( description ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UTILS_INVALID_VALUE_IN.getKey(),
                    MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_DESCRIPTION.getKey() ), name ) );
        }
    }

    /**
     * Duplicate selection and update field.
     *
     * @param entityManager
     *         the entity manager
     * @param elementDataJson
     *         the elementDataJson
     * @param jsonField
     *         the json field
     * @param elementJson
     *         the elementJson
     */
    private void duplicateSelectionAndUpdateField( EntityManager entityManager, JSONObject elementDataJson, JSONObject jsonField,
            JSONObject elementJson ) {
        validateHtmlFieldsInElement( jsonField );
        String fieldType = ( String ) jsonField.get( WorkflowDefinitionUtil.TYPE );
        if ( fieldType.equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
            duplicateParserSelection( entityManager, jsonField );
        } else if ( fieldType.equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
            duplicateRegexSelection( entityManager, jsonField );
        } else if ( fieldType.equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) ) {
            duplicateTemplateSelection( entityManager, jsonField );
        } else if ( Boolean.TRUE != jsonField.get( WorkflowDefinitionUtil.VARIABLE_MODE ) && isFieldValueSelectionId( fieldType ) ) {
            duplicateGeneralSelection( entityManager, elementDataJson, jsonField, elementJson );
        }
    }

    /**
     * Duplicate general selection.
     *
     * @param entityManager
     *         the entity manager
     * @param elementDataJson
     *         the elementDataJson
     * @param jsonField
     *         the raw field
     * @param elementJson
     *         the elementJson
     */
    private void duplicateGeneralSelection( EntityManager entityManager, JSONObject elementDataJson, JSONObject jsonField,
            JSONObject elementJson ) {
        String fieldType = ( String ) jsonField.get( WorkflowDefinitionUtil.TYPE );
        if ( log.isDebugEnabled() ) {
            log.debug( String.format( "duplicating selection of %s.%s ", elementDataJson.get( "name" ), jsonField.get( "name" ) ) );
            log.debug( "field type:::" + fieldType );
        }
        String fieldValue = ( String ) jsonField.get( WorkflowDefinitionUtil.VALUE );
        if ( null == fieldValue || fieldValue.isEmpty() ) {
            return;
        }
        SelectionEntity selectionEntity = selectionManager.getSelectionEntityById( entityManager, fieldValue );
        if ( selectionEntity == null ) {
            // selection does not exist
            if ( fieldType.equals( FieldTypes.SUBWORKFLOW.getType() ) ) {
                jsonField.put( WorkflowDefinitionUtil.VALUE, null );
                elementJson.put( "selected", false );
            }
        } else {
            // selection exists, hence create duplicate
            SelectionEntity newSelectionEntity = duplicateSelectionForWorkflowFields( entityManager, selectionEntity );
            jsonField.put( WorkflowDefinitionUtil.VALUE, newSelectionEntity.getId().toString() );
            if ( log.isDebugEnabled() ) {
                log.debug( String.format( "duplicate of selection with id %s created with id %s", selectionEntity.getId().toString(),
                        newSelectionEntity.getId().toString() ) );
            }
        }
    }

    private SelectionEntity duplicateSelectionForWorkflowFields( EntityManager entityManager, SelectionEntity selectionEntity ) {
        return selectionManager.duplicateSelectionAndSelectionItems( entityManager, selectionEntity, false );
    }

    /**
     * Checks if is field value selection id.
     *
     * @param fieldType
     *         the field type
     *
     * @return true, if is field value selection id
     */
    private boolean isFieldValueSelectionId( String fieldType ) {
        return ( fieldType.equals( FieldTypes.OBJECT.getType() ) || fieldType.equals( FieldTypes.CB2_OBJECTS.getType() )
                || fieldType.equals( FieldTypes.SERVER_FILE_EXPLORER.getType() ) || fieldType.equals( "ssfe" ) || fieldType.equals(
                FieldTypes.PROJECT_SELECTOR.getType() ) || fieldType.equals( FieldTypes.SUBWORKFLOW.getType() ) );
    }

    /**
     * Duplicate template selection.
     *
     * @param entityManager
     *         the entity manager
     * @param jsonField
     *         the raw field
     */
    private void duplicateTemplateSelection( EntityManager entityManager, JSONObject jsonField ) {
        String fieldValue = ( String ) jsonField.get( WorkflowDefinitionUtil.VALUE );
        SelectionEntity currentSelection = selectionManager.getSelectionEntityById( entityManager, fieldValue );
        if ( currentSelection == null ) {
            return;
        }
        SelectionEntity newSelectionEntity = duplicateSelectionForTemplate( entityManager, currentSelection );

        jsonField.put( WorkflowDefinitionUtil.VALUE, newSelectionEntity.getId().toString() );
        if ( log.isDebugEnabled() ) {
            log.debug( String.format( "duplicate of selection with id %s created with id %s", currentSelection.getId().toString(),
                    newSelectionEntity.getId().toString() ) );
        }
    }

    private SelectionEntity duplicateSelectionForTemplate( EntityManager entityManager, SelectionEntity currentSelection ) {
        SelectionEntity newSelectionEntity = selectionManager.duplicateSelectionAndSelectionItems( entityManager, currentSelection, true );
        new Thread( () -> templateManager.duplicateTemplateEntitiesBySelectionId( currentSelection.getId(),
                newSelectionEntity.getId() ) ).start();
        return newSelectionEntity;
    }

    /**
     * Duplicate regex selection.
     *
     * @param entityManager
     *         the entity manager
     * @param jsonField
     *         the raw field
     */
    private void duplicateRegexSelection( EntityManager entityManager, JSONObject jsonField ) {
        String fieldValue = ( String ) jsonField.get( WorkflowDefinitionUtil.VALUE );
        SelectionEntity currentSelection = selectionManager.getSelectionEntityById( entityManager, fieldValue );
        if ( currentSelection == null ) {
            return;
        }

        SelectionEntity newSelectionEntity = duplicateSelectionForRegex( entityManager, currentSelection );

        jsonField.put( WorkflowDefinitionUtil.VALUE, newSelectionEntity.getId().toString() );
        if ( log.isDebugEnabled() ) {
            log.debug( String.format( "duplicate of selection with id %s created with id %s", currentSelection.getId().toString(),
                    newSelectionEntity.getId().toString() ) );
        }
    }

    private SelectionEntity duplicateSelectionForRegex( EntityManager entityManager, SelectionEntity currentSelection ) {
        SelectionEntity newSelectionEntity = selectionManager.duplicateSelectionAndSelectionItems( entityManager, currentSelection, true );
        new Thread(
                () -> regexManager.duplicateRegexEntitiesBySelectionId( currentSelection.getId(), newSelectionEntity.getId() ) ).start();
        return newSelectionEntity;
    }

    private void duplicateParserSelection( EntityManager entityManager, JSONObject rawField ) {
        String oldId = ( String ) rawField.get( WorkflowDefinitionUtil.VALUE );
        if ( oldId == null ) {
            return;
        }
        String newId = duplicateSelectionForParser( entityManager, oldId );
        rawField.put( WorkflowDefinitionUtil.VALUE, newId );
        if ( log.isDebugEnabled() ) {
            log.debug( String.format( "duplicate of parser with id %s created with id %s", oldId, newId ) );
        }
    }

    private String duplicateSelectionForParser( EntityManager entityManager, String oldId ) {
        return parserManager.duplicateParserEntity( entityManager, oldId ).getId().toString();
    }

    /**
     * Save design, objectiv and custom variables.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param dto
     *         the dto
     */
    private void saveVariables( EntityManager entityManager, String userId, String workflowId, WorkflowDTO dto ) {
        Map< String, Object > map = dto.getDefinition();
        List< Object > variableDTOs = new ArrayList<>();

        String json = null;
        if ( map != null ) {
            json = JsonUtils.toJson( map );
        }
        final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
        final UserWorkFlow userWorkFlow = new UserWorkflowImpl();
        if ( null != workflowDefinitionDTO.getElements() ) {
            userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
            getVariablesFromWorkflow( entityManager, workflowId, userWorkFlow, variableDTOs );
        }

        String algoType = getSchemeAlgoTypeByUserAndWorkflowId( entityManager, userId, workflowId );

        List< VariableEntity > variableEntities = new ArrayList<>();
        preparVariableEntitiesFromDTOs( entityManager, dto, variableDTOs, algoType, variableEntities );
        removeOldVariables( entityManager, userId, workflowId );
        variableDAO.addVariables( entityManager, variableEntities );
    }

    /**
     * Prepar variable entities from dt os.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     * @param variableDTOs
     *         the variable dt os
     * @param algoType
     *         the algo type
     * @param variableEntities
     *         the variable entities
     */
    private void preparVariableEntitiesFromDTOs( EntityManager entityManager, WorkflowDTO dto, List< Object > variableDTOs, String algoType,
            List< VariableEntity > variableEntities ) {
        for ( Object variableDTO : variableDTOs ) {
            if ( variableDTO instanceof DesignVariableDTO designVariableDTO ) {
                variableEntities.add(
                        prepareDesignVariableEntity( entityManager, designVariableDTO, dto.getCreatedBy().getId(), algoType ) );
            } else if ( variableDTO instanceof ObjectiveVariableDTO objectiveVariableDTO ) {
                variableEntities.add( prepareObjectiveVariableEntity( entityManager, objectiveVariableDTO, dto.getCreatedBy().getId() ) );
            } else if ( variableDTO instanceof CustomVariableDTO customVariableDTO ) {
                variableEntities.add( prepareCustomVariableEntity( entityManager, customVariableDTO, dto.getCreatedBy().getId() ) );
            }
        }
    }

    /**
     * Remove old variables.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     */
    private void removeOldVariables( EntityManager entityManager, String userId, String workflowId ) {
        List< VariableEntity > allVariables = ( List< VariableEntity > ) variableDAO.getAllVariables( entityManager, workflowId, userId,
                VariableEntity.class );
        if ( CollectionUtils.isNotEmpty( allVariables ) ) {
            allVariables.forEach( variableEntity -> variableDAO.delete( entityManager, variableEntity ) );
        }
    }

    /**
     * Gets the scheme algo type by user and workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     *
     * @return the scheme algo type by user and workflow id
     */
    private String getSchemeAlgoTypeByUserAndWorkflowId( EntityManager entityManager, String userId, String workflowId ) {
        Map< String, String > optionScheme = getOptionScheme( entityManager, userId, workflowId, TAB_KEY_SCHEME_OPTIONS );
        String schId = optionScheme.get( "scheme" );
        String algoType = "";
        if ( schId != null ) {

            WFSchemeEntity wFSchemeEntit = ( WFSchemeEntity ) susDAO.getLatestObjectById( entityManager, WFSchemeEntity.class,
                    UUID.fromString( schId ) );
            JSONObject jsonAlgoConfig;
            JSONParser parser = new JSONParser();
            try ( InputStream decriptStreamn = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                    new File( PropertiesManager.getVaultPath() + wFSchemeEntit.getAlgoConfig().getFilePath() ),
                    prepareEncryptionDecryptionDTO( wFSchemeEntit.getAlgoConfig().getEncryptionDecryption() ) ) ) {
                jsonAlgoConfig = ( JSONObject ) parser.parse( new InputStreamReader( decriptStreamn ) );
                if ( jsonAlgoConfig.containsKey( "type" ) ) {
                    algoType = ( String ) jsonAlgoConfig.get( "type" );
                } else {
                    log.warn( "Algo file do not contain 'type' key" );
                    algoType = AlgoTypeEnum.DEFAULT.getKey();
                }
            } catch ( IOException | ParseException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e );
            }
        }
        return algoType;
    }

    /**
     * Prepare objective variable entity.
     *
     * @param entityManager
     *         the entity manager
     * @param objectiveVariableDTO
     *         the objective variable DTO
     * @param userId
     *         the user id
     *
     * @return the objective variable entity
     */
    private ObjectiveVariableEntity prepareObjectiveVariableEntity( EntityManager entityManager, ObjectiveVariableDTO objectiveVariableDTO,
            String userId ) {
        ObjectiveVariableEntity entity = new ObjectiveVariableEntity();
        entity.setId( UUID.randomUUID() );
        entity.setLabel( objectiveVariableDTO.getLabel() );
        entity.setName( objectiveVariableDTO.getName() );
        entity.setNominalValue( objectiveVariableDTO.getNominalValue() );
        entity.setCreatedOn( objectiveVariableDTO.getCreatedOn() );
        entity.setWorkflow( workflowDao.getLatestNonDeletedObjectById( entityManager, objectiveVariableDTO.getWorkflowId() ) );
        entity.setCreatedOn( objectiveVariableDTO.getCreatedOn() == null ? new Date() : objectiveVariableDTO.getCreatedOn() );
        entity.setUserId( UUID.fromString( userId ) );
        return entity;
    }

    /**
     * Prepare designvariable entity.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableDTO
     *         the design variable DTO
     * @param userId
     *         the user id
     * @param algoType
     *         the algo type
     *
     * @return the design variable entity
     */
    private DesignVariableEntity prepareDesignVariableEntity( EntityManager entityManager, DesignVariableDTO designVariableDTO,
            String userId, String algoType ) {
        DesignVariableEntity entity = new DesignVariableEntity();
        entity.setId( UUID.randomUUID() );
        entity.setLabel( designVariableDTO.getLabel() );
        entity.setName( designVariableDTO.getName() );
        entity.setNominalValue( designVariableDTO.getNominalValue() );
        entity.setType( designVariableDTO.getType() );
        entity.setWorkflow( workflowDao.getLatestNonDeletedObjectById( entityManager, designVariableDTO.getWorkflowId() ) );
        entity.setCreatedOn( designVariableDTO.getCreatedOn() == null ? new Date() : designVariableDTO.getCreatedOn() );
        entity.setIndexNumber( designVariableDTO.getIndex() );
        entity.setUserId( UUID.fromString( userId ) );
        entity.setAlgoType( algoType );

        return entity;
    }

    /**
     * Prepare custom variable entity.
     *
     * @param entityManager
     *         the entity manager
     * @param customVariableDTO
     *         the custom variable DTO
     * @param userId
     *         the user id
     *
     * @return the custom variable entity
     */
    private CustomVariableEntity prepareCustomVariableEntity( EntityManager entityManager, CustomVariableDTO customVariableDTO,
            String userId ) {
        CustomVariableEntity entity = new CustomVariableEntity();
        entity.setId( UUID.randomUUID() );
        entity.setLabel( customVariableDTO.getLabel() );
        entity.setName( customVariableDTO.getName() );
        entity.setNominalValue( customVariableDTO.getValue() );
        entity.setType( customVariableDTO.getType() );
        entity.setWorkflow( workflowDao.getLatestNonDeletedObjectById( entityManager, customVariableDTO.getWorkflowId() ) );
        entity.setCreatedOn( customVariableDTO.getCreatedOn() == null ? new Date() : customVariableDTO.getCreatedOn() );
        entity.setUserId( UUID.fromString( userId ) );
        entity.setElementName( customVariableDTO.getElementName() );
        entity.setFieldName( customVariableDTO.getFieldName() );
        return entity;
    }

    /**
     * This method sets the user attributes.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowDTO
     *         the workflow data transfer object
     * @param entity
     *         the entity
     */
    private void setUserAttributes( EntityManager entityManager, UUID userId, WorkflowDTO workflowDTO, WorkflowEntity entity ) {
        final UserEntity user = userManager.getSimUser( entityManager, userId );
        final WorkflowDTO workflowWithFirstVersion = getWorkflowByIdAndVersionId( entityManager, userId, workflowDTO.getId(),
                ConstantsInteger.INTEGER_VALUE_ONE );

        if ( user != null ) {
            if ( workflowDao.isAlreadyWipWorkflowExist( entityManager, user.getId(), UUID.fromString( workflowDTO.getId() ) ) ) {
                throw new SusException(
                        new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_WIP_ALREADY_EXIST, workflowDTO.getId() ) ),
                        getClass() );
            }
            entity.setModifiedBy( user );
        }
        if ( workflowWithFirstVersion != null ) {
            final UserEntity createdBy = userManager.getSimUser( entityManager,
                    UUID.fromString( workflowWithFirstVersion.getCreatedBy().getId() ) );
            entity.setKeyuser( createdBy );
            entity.setCreatedBy( createdBy );
            entity.setOwner( createdBy );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< LatestWorkFlowDTO > getWorkflowVersionsById( UUID userId, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role work flow user, else throws exception
            isWorkflowUser( entityManager, userId );

            final List< LatestWorkFlowDTO > resultList = new ArrayList<>();
            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                        getClass() );
            }

            final List< WorkflowEntity > entities = workflowDao.getWorkflowVersionsById( entityManager, UUID.fromString( workflowId ) );

            for ( final WorkflowEntity workflowEntity : entities ) {

                final WorkflowDTO workflowDTO = prepareWorkflowDTO( entityManager, userId, workflowEntity );
                if ( isWorkflowVisible( entityManager, workflowDTO ) ) {
                    final LatestWorkFlowDTO newWorkflowDTO = prepareLatestWorkflowFromWorkflowDTO( workflowDTO );
                    newWorkflowDTO.setElements( new HashMap<>() );
                    resultList.add( newWorkflowDTO );
                }

            }

            return resultList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the workflow by id and version id.
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
    @Override
    public WorkflowDTO getWorkflowByIdAndVersionId( EntityManager entityManager, UUID userId, String workflowId, int versionId ) {

        // validates if the user has the role work flow user, else throws exception
        isWorkflowUser( entityManager, userId );

        if ( !StringUtils.validateUUIDString( workflowId ) ) {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, workflowId ) ),
                    getClass() );
        }
        final WorkflowEntity entity = workflowDao.getWorkflowByIdAndVersionId( entityManager, UUID.fromString( workflowId ), versionId );
        if ( entity != null ) {
            return prepareWorkflowDTO( entityManager, userId, entity );
        } else {
            ExceptionLogger.logMessage(
                    MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID_AND_VERSION_ID, workflowId, versionId ) );
            throw new SusException( new Exception(
                    MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID_AND_VERSION_ID, workflowId, versionId ) ),
                    this.getClass() );
        }
    }

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
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getNewWorkflowByIdAndVersionId(java.util.UUID, java.lang.String, int)
     */
    @Override
    public LatestWorkFlowDTO getNewWorkflowByIdAndVersionId( UUID userId, String workflowId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getNewWorkflowByIdAndVersionId( entityManager, userId, workflowId, versionId );
        } finally {
            entityManager.close();
        }
    }

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
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getNewWorkflowByIdAndVersionId(java.util.UUID, java.lang.String, int)
     */
    @Override
    public LatestWorkFlowDTO getNewWorkflowByIdAndVersionId( EntityManager entityManager, UUID userId, String workflowId, int versionId ) {
        return prepareLatestWorkflowFromWorkflowDTO( getWorkflowByIdAndVersionId( entityManager, userId, workflowId, versionId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowDTO saveWorkflow( EntityManager entityManager, UUID userId, String parentId, WorkflowDTO workflowDto,
            String workflowJson ) {
        SuSEntity parentEntity = null;
        WorkflowEntity createdEntity;
        if ( parentId != null ) {
            parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
        }

        // validates if the user has the role work flow manager, else throws exception
        isWorkflowManager( entityManager, userId );
        workflowDto.validate();
        UserDTO userDTO = userManager.prepareUserDtoFromUserEntity( userCommonManager.getUserEntityById( entityManager, userId ) );
        workflowDto.setCreatedBy( userDTO );
        workflowDto.setUpdatedBy( userDTO );
        final WorkflowEntity entity = prepareWorkflowEntity( workflowDto );
        if ( ( ( parentId != null ) && ( parentEntity != null ) ) && ( !susDAO.getSiblingsBySameIName( entityManager, entity.getName(),
                entity, parentEntity ).isEmpty() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
        }
        // As New Work flow is going to be created so need to set UUID and version id=1
        entity.getComposedId().setVersionId( SusConstantObject.DEFAULT_VERSION_NO );

        // GET Sim USER and Set to work flow entity to save
        final SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( LatestWorkFlowDTO.TYPE_ID.toString(),
                LatestWorkFlowDTO.WORKFLOW_CONFIG );
        final UserEntity user = userManager.prepareUserEntity( workflowDto.getCreatedBy() );

        entity.setKeyuser( user );
        entity.setCreatedBy( user );
        entity.setOwner( user );
        entity.setModifiedBy( user );
        entity.setCreatedOn( new Date() );
        entity.setModifiedOn( new Date() );
        if ( Boolean.TRUE.equals( PropertiesManager.isAuditWorkflow() ) ) {
            entity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                    entity.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED, ConstantsDbOperationTypes.CREATED,
                    userDTO.getId(), entity, "Workflow" ) );
        }
        entity.setTypeId( LatestWorkFlowDTO.TYPE_ID );
        entity.setConfig( LatestWorkFlowDTO.WORKFLOW_CONFIG );
        if ( parentEntity != null ) {

            final StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( LatestWorkFlowDTO.TYPE_ID.toString(),
                    parentEntity.getConfig() );
            entity.setLifeCycleStatus( dto.getId() );
        }
        entity.setRunOnLocation( getLocationEntities( entityManager, userId, workflowDto ) );
        entity.setLocations( workflowDto.getLocations() );
        entity.setTranslation( setMultiNames( entityManager, workflowJson ) );
        try {
            entity.setSelectedTranslations( JsonUtils.toJson( new ObjectMapper().readTree( workflowJson ).path( SELECTED_TRANSLATIONS ) ) );
        } catch ( IOException e ) {
            log.info( e.getMessage() );
            throw new SusException( e.getMessage() );
        }
        createdEntity = workflowDao.saveWorkflow( entityManager, entity );
        if ( ( null != createdEntity ) && ( parentEntity != null ) ) {

            final Relation relationEntity = new Relation( parentEntity.getComposedId().getId(), createdEntity.getComposedId().getId() );
            susDAO.createRelation( entityManager, relationEntity );
            addToAcl( entityManager, userId.toString(), createdEntity, parentEntity );
            createIndexEntity( entityManager, suSObjectModel, createdEntity );
            selectionManager.sendCustomerEventOnCreate( entityManager, userId.toString(), createdEntity, parentEntity );
        }
        return prepareWorkflowDTO( entityManager, userId, createdEntity );

    }

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
    @Override
    public void addToAcl( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        log.info( MessageBundleFactory.getMessage( Messages.SAVING_OBJECT_AS_RESOURCE.getKey() ) );
        final ResourceAccessControlDTO resourceAccessControlDTO = permissionManager.prepareResourceAccessControlDTOFromObject(
                entityManager, createdEntity, parentEntity );
        if ( resourceAccessControlDTO != null ) {
            resourceAccessControlDTO.setSecurityIdentity(
                    userCommonManager.getAclCommonSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( userId ) )
                            .getId() );
            permissionManager.addObjectToAcl( entityManager, resourceAccessControlDTO );
            permissionManager.saveSelectionForNewEntity( entityManager, userId, createdEntity, parentEntity );
        }
    }

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
    @Override
    public void addJobToAcl( EntityManager entityManager, String userId, JobEntity createdEntity, SuSEntity parentEntity ) {
        log.info( MessageBundleFactory.getMessage( Messages.SAVING_OBJECT_AS_RESOURCE.getKey() ) );
        final ResourceAccessControlDTO resourceAccessControlDTO = permissionManager.prepareResourceAccessControlDTOFromJob( entityManager,
                createdEntity, parentEntity );
        if ( resourceAccessControlDTO != null ) {
            resourceAccessControlDTO.setSecurityIdentity(
                    userCommonManager.getAclCommonSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( userId ) )
                            .getId() );
            permissionManager.addObjectToAcl( entityManager, resourceAccessControlDTO );
            permissionManager.saveSelectionForNewJob( entityManager, userId, createdEntity, parentEntity );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO saveWorkflow( UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO, String workflowJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return saveWorkflow( entityManager, userId, parentId, newWorkflowDTO, workflowJson );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO saveWorkflowAsNew( UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO, String workflowJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity parentEntity = null;
            if ( parentId != null ) {
                parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
            }
            WorkflowDTO workflow = prepareWorkflowDTOFromNewDTO( newWorkflowDTO );
            final WorkflowEntity entity = prepareWorkflowEntity( workflow );
            if ( ( ( parentId != null ) && ( parentEntity != null ) ) && ( !susDAO.getSiblingsBySameIName( entityManager, entity.getName(),
                    entity, parentEntity ).isEmpty() ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey(), entity.getName() ) );
            }
            return prepareLatestWorkflowFromWorkflowDTO( saveWorkflow( entityManager, userId, parentId, workflow, workflowJson ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LatestWorkFlowDTO saveWorkflow( EntityManager entityManager, UUID userId, String parentId, LatestWorkFlowDTO newWorkflowDTO,
            String workflowJson ) {
        if ( !permissionManager.isPermitted( entityManager, userId.toString(),
                parentId + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), newWorkflowDTO.getName() ) );
        }
        return prepareLatestWorkflowFromWorkflowDTO(
                saveWorkflow( entityManager, userId, parentId, prepareWorkflowDTOFromNewDTO( newWorkflowDTO ), workflowJson ) );
    }

    /**
     * Prepare workflow DTO from new workflow DTO.
     *
     * @param newWorkflowDTO
     *         the new workflow DTO
     *
     * @return the workflow DTO
     */
    private WorkflowDTO prepareWorkflowDTOFromNewDTO( LatestWorkFlowDTO newWorkflowDTO ) {
        final WorkflowDTO workflowDTO = new WorkflowDTO();
        workflowDTO.setName( newWorkflowDTO.getName() );
        workflowDTO.setDescription( newWorkflowDTO.getDescription() );
        workflowDTO.setComments( newWorkflowDTO.getComments() );
        workflowDTO.setDefinition( newWorkflowDTO.prepareDefinition() );
        workflowDTO.setVersion( newWorkflowDTO.getVersion() );
        workflowDTO.setLocations( newWorkflowDTO.getLocations() );
        workflowDTO.setRunOnLocation( newWorkflowDTO.getRunOnLocation() );
        workflowDTO.setCustomFlags( newWorkflowDTO.getCustomFlags() );

        return workflowDTO;
    }

    /**
     * Prepare new workflow from old DTO.
     *
     * @param workflowDTO
     *         the entity
     *
     * @return the new workflow DTO
     */
    private LatestWorkFlowDTO prepareLatestWorkflowFromWorkflowDTO( WorkflowDTO workflowDTO ) {
        final LatestWorkFlowDTO newWorkflowDTO = new LatestWorkFlowDTO();
        newWorkflowDTO.setId( UUID.fromString( workflowDTO.getId() ) );
        newWorkflowDTO.setName( workflowDTO.getName() );
        newWorkflowDTO.setInteractive( false );
        newWorkflowDTO.setVersion( workflowDTO.getVersion() );
        newWorkflowDTO.setVersions( workflowDTO.getVersions() );
        newWorkflowDTO.setDescription( workflowDTO.getDescription() );
        newWorkflowDTO.setComments( workflowDTO.getComments() );
        // get StatusName from config file
        newWorkflowDTO.setActions( workflowDTO.getActions() );
        newWorkflowDTO.setLifeCycleStatus( workflowDTO.getLifeCycleStatus() );
        newWorkflowDTO.setCreatedOn( workflowDTO.getCreatedOn() );
        newWorkflowDTO.getModifiedOn( workflowDTO.getModifiedOn() );
        newWorkflowDTO.setExecutable( workflowDTO.isExecutable() );
        newWorkflowDTO.setTypeId( workflowDTO.getTypeId() );
        newWorkflowDTO.setConfig( workflowDTO.getConfig() );
        if ( workflowDTO.getDefinition() != null ) {
            newWorkflowDTO.setWithDefinition( workflowDTO.getDefinition() );
        }
        newWorkflowDTO.setCreatedBy( workflowDTO.getCreatedBy() );
        newWorkflowDTO.setModifiedBy( workflowDTO.getUpdatedBy() );
        newWorkflowDTO.setJobs( workflowDTO.getJobs() );
        newWorkflowDTO.setRunOnLocation( workflowDTO.getRunOnLocation() );
        newWorkflowDTO.setLocations( workflowDTO.getLocations() );
        newWorkflowDTO.setCustomFlags( workflowDTO.getCustomFlags() );
        return newWorkflowDTO;
    }

    /**
     * Gets the workflow list by category id.
     *
     * @param userId
     *         the user id
     * @param categoryId
     *         the category id
     *
     * @return the workflow list by category id
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getWorkflowListByCategoryId(java.lang.Integer, java.lang.String)
     */
    @Override
    public List< WorkflowDTO > getWorkflowListByCategoryId( UUID userId, String categoryId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role workflow user, else throws exception
            isWorkflowUser( entityManager, userId );

            final List< WorkflowDTO > resultList = new ArrayList<>();
            List< UUID > workflowIdsList;

            if ( categoryId.equals( ConstantsString.MINUS_ONE ) ) {
                // get workflows with No category
                workflowIdsList = workflowDao.getWorkflowListWithoutCategory( entityManager );
            } else {
                // get workflows by category ID
                if ( !StringUtils.validateUUIDString( categoryId ) ) {
                    throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, categoryId ) ), getClass() );
                }

                workflowIdsList = workflowDao.getWorkflowListByCategoryId( entityManager, UUID.fromString( categoryId ) );
            }
            if ( CollectionUtil.isNotEmpty( workflowIdsList ) ) {
                for ( final UUID id : workflowIdsList ) {
                    final WorkflowDTO workflowDTO = getUserWorkflowById( entityManager, userId, id.toString() );
                    resultList.add( workflowDTO );

                }
            }
            // to sort the workflows list
            resultList.sort( Comparator.comparing( WorkflowDTO::getCreatedOn, Comparator.nullsLast( Comparator.reverseOrder() ) ) );

            return resultList;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateWorkflow( LatestWorkFlowDTO workflowDto, boolean isImportWorkflow ) {
        if ( workflowDto == null ) {
            return false;
        }

        Object json = JsonUtils.toJson( workflowDto.prepareDefinition() );
        final WorkflowDefinitionDTO workflowDefinitionDTO = JsonUtils.jsonToObject( json.toString(), WorkflowDefinitionDTO.class );
        final UserWorkFlow userWorkFlow = new UserWorkflowImpl();

        if ( null != workflowDefinitionDTO.getElements() ) {
            userWorkFlow.setNodes( WorkflowDefinitionUtil.prepareWorkflowElements( workflowDefinitionDTO ) );
            if ( !validateWorkFlowElementsAndFields( userWorkFlow, isImportWorkflow ) ) {
                return false;
            }
        }

        return validateHtmlFieldsInWorkflowAndElements( workflowDto, isImportWorkflow );
    }

    /**
     * Validate html fields in workflow and elements boolean.
     *
     * @param workflowDto
     *         the workflow dto
     * @param isImportWorkflow
     *
     * @return the boolean
     */
    private boolean validateHtmlFieldsInWorkflowAndElements( LatestWorkFlowDTO workflowDto, boolean isImportWorkflow ) {
        try {
            workflowDto.validateHtmlFields();
            validateAllHtmlFieldsInElements( prepareWorkflowDTOFromNewDTO( workflowDto ) );
            return true;
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            log.warn( "HTML validation failed for {} workflow {}",
                    isImportWorkflow ? "imported" : "workflow with id " + workflowDto.getId() + " and version " + workflowDto.getVersion(),
                    isImportWorkflow ? "fields" : "fields for element fields" );
            return false;
        }
    }

    /**
     * Creates the design variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userWorkFlow
     *         the user work flow
     * @param variableDTOs
     *         the variable dt os
     */
    private void getVariablesFromWorkflow( EntityManager entityManager, String workflowId, UserWorkFlow userWorkFlow,
            List< Object > variableDTOs ) {
        for ( final UserWFElement element : userWorkFlow.getNodes() ) {
            if ( element.getKey().equals( ElementKeys.IO.getKey() ) && null != element.getFields() ) {
                for ( final Field< ? > field : element.getFields() ) {
                    if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) ) {
                        prepareRegexVariables( workflowId, variableDTOs, element, field );
                    } else if ( field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) ) {
                        prepareParserDesignAndObjectiveVariable( entityManager, workflowId, variableDTOs, element, field );
                    } else if ( field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() ) ) {
                        prepareTemplateVariables( workflowId, variableDTOs, element, field );
                    } else if ( field.getType().equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                        prepareRegexScanOnServerSideVariables( entityManager, workflowId, variableDTOs, element, field );
                    } else if ( field.getType().equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) ) {
                        prepareTemplateObjectVariables( entityManager, workflowId, variableDTOs, element, field );
                    }
                }
            }
        }
    }

    /**
     * Prepare parser design and objective variable.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param variableDTOs
     *         the design variables
     * @param element
     *         the element
     * @param field
     *         the field
     */
    private void prepareParserDesignAndObjectiveVariable( EntityManager entityManager, String workflowId, List< Object > variableDTOs,
            UserWFElement element, final Field< ? > field ) {
        List< ParserVariableDTO > list = parserManager.getSelectedParserEntriesListByParserId( entityManager, ( String ) field.getValue() );
        if ( CollectionUtils.isEmpty( list ) ) {
            return;
        }
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equalsIgnoreCase( field.getTemplateType() ) ) {
            list.stream().map( selectedDesigns -> VariableUtil.prepareDesignVariableDTOFromParserVariableDTO( selectedDesigns, namePrefix,
                    UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.OBJECTIVE_VARIABLE.getValue().equalsIgnoreCase( field.getTemplateType() ) ) {
            list.stream().map( selectedDesigns -> VariableUtil.prepareObjectiveVariableDTOFromParserVariableDTO( selectedDesigns,
                    ConstantsString.EMPTY_STRING, UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equalsIgnoreCase( field.getTemplateType() ) ) {
            list.stream()
                    .map( selectedDesigns -> VariableUtil.prepareCustomVariableDTOFromParserVariableDTO( selectedDesigns, element.getName(),
                            field.getName(), UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        }
    }

    /**
     * Prepare regex variables.
     *
     * @param workflowId
     *         the workflow id
     * @param variableDTOs
     *         the design variables
     * @param element
     *         the element
     * @param field
     *         the field
     */
    private void prepareRegexVariables( String workflowId, List< Object > variableDTOs, final UserWFElement element,
            final Field< ? > field ) {
        LinkedHashMap< String, List< Object > > map = ( LinkedHashMap< String, List< Object > > ) field.getValue();
        ScanFileDTO scanFile = JsonUtils.jsonToObject( JsonUtils.toJson( map ), ScanFileDTO.class );
        if ( scanFile == null || CollectionUtils.isEmpty( scanFile.getVariables() ) ) {
            return;
        }
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            scanFile.getVariables().stream()
                    .map( objectVariable -> VariableUtil.prepareDesignVariableDTOFromObjectVariableDTO( objectVariable, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.OBJECTIVE_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            scanFile.getVariables().stream()
                    .map( objectVariable -> VariableUtil.prepareObjectiveVariableDTOFromObjectVariableDTO( objectVariable, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equalsIgnoreCase( field.getTemplateType() ) ) {
            scanFile.getVariables().stream()
                    .map( objectVariable -> VariableUtil.prepareCustomVariableDTOFromObjectVariableDTO( objectVariable, element.getName(),
                            field.getName(), UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );

        }
    }

    /**
     * Prepare regex scan on server side variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param variableDTOs
     *         the variable dt os
     * @param element
     *         the element
     * @param field
     *         the field
     */
    private void prepareRegexScanOnServerSideVariables( EntityManager entityManager, String workflowId, List< Object > variableDTOs,
            final UserWFElement element, final Field< ? > field ) {
        String fieldValue = ( String ) field.getValue();
        if ( StringUtils.isNullOrEmpty( fieldValue ) ) {
            return;
        }
        UUID selectionId = UUID.fromString( fieldValue );
        List< RegexEntity > regexEntities = regexManager.getRegexDAO().getRegexListBySelectionId( entityManager, selectionId );
        if ( CollectionUtils.isEmpty( regexEntities ) ) {
            return;
        }
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            regexEntities.stream().map( regexEntity -> VariableUtil.prepareDesignVariableDTOFromRegexEntity( regexEntity, namePrefix,
                    UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.OBJECTIVE_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            regexEntities.stream().map( regexEntity -> VariableUtil.prepareObjectiveVariableDTOFromRegexEntity( regexEntity, namePrefix,
                    UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equalsIgnoreCase( field.getTemplateType() ) ) {
            regexEntities.stream().map( regexEntity -> VariableUtil.prepareCustomVariableDTOFromRegexEntity( regexEntity, element.getName(),
                    field.getName(), UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );

        }
    }

    /**
     * Prepare template object variables.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param variableDTOs
     *         the variable dt os
     * @param element
     *         the element
     * @param field
     *         the field
     */
    private void prepareTemplateObjectVariables( EntityManager entityManager, String workflowId, List< Object > variableDTOs,
            final UserWFElement element, final Field< ? > field ) {
        UUID selectionId = UUID.fromString( ( String ) field.getValue() );
        List< TemplateEntity > templateEntities = templateManager.getTemplateDAO()
                .getTemplateListBySelectionId( entityManager, selectionId );
        if ( CollectionUtils.isEmpty( templateEntities ) ) {
            return;
        }
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateEntities.stream()
                    .map( templateEntity -> VariableUtil.prepareDesignVariableDTOFromTemplateEntity( templateEntity, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.OBJECTIVE_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateEntities.stream()
                    .map( templateEntity -> VariableUtil.prepareObjectiveVariableDTOFromTemplateEntity( templateEntity, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateEntities.stream()
                    .map( templateEntity -> VariableUtil.prepareCustomVariableDTOFromTemplateEntity( templateEntity, element.getName(),
                            field.getName(), UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        }
    }

    /**
     * Prepare template variables.
     *
     * @param workflowId
     *         the workflow id
     * @param variableDTOs
     *         the variable dt os
     * @param element
     *         the element
     * @param field
     *         the field
     */
    private void prepareTemplateVariables( String workflowId, List< Object > variableDTOs, final UserWFElement element,
            final Field< ? > field ) {
        LinkedHashMap< String, List< Object > > map = ( LinkedHashMap< String, List< Object > > ) field.getValue();
        TemplateFileDTO templateFile = JsonUtils.jsonToObject( JsonUtils.toJson( map ), TemplateFileDTO.class );
        if ( templateFile == null || CollectionUtils.isEmpty( templateFile.getVariables() ) ) {
            return;
        }
        String namePrefix = element.getName() + ConstantsString.DOT + field.getName() + ConstantsString.DOT;
        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateFile.getVariables().stream()
                    .map( templateVariable -> VariableUtil.prepareDesignVariableDTOFromTemplateVariableDTO( templateVariable, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.OBJECTIVE_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateFile.getVariables().stream()
                    .map( templateVariable -> VariableUtil.prepareObjectiveVariableDTOFromTemplateVariableDTO( templateVariable, namePrefix,
                            UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        } else if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            templateFile.getVariables().stream()
                    .map( templateVariable -> VariableUtil.prepareCustomVariableDTOFromTemplateVariableDTO( templateVariable,
                            element.getName(), field.getName(), UUID.fromString( workflowId ) ) ).forEach( variableDTOs::add );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScanFileDTO getScanFileBySelectionId( String uid, UUID selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< RegexEntity > regexEntities = regexManager.getRegexDAO().getRegexListBySelectionId( entityManager, selectionId );
            return prepareScanFileDTOFromRegexServerObject( entityManager, uid, selectionId, regexEntities );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WorkflowDTO > getWorkflowVersionsWithoutDefinition( UUID userId, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< WorkflowDTO > list = new ArrayList<>();
            isWorkflowUser( entityManager, userId );

            if ( !StringUtils.validateUUIDString( id ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, id ) ), getClass() );
            }
            final List< WorkflowEntity > wfList = workflowDao.getWorkflowVersionsWithoutDefinition( entityManager, UUID.fromString( id ) );

            // get versions of workflows
            for ( final WorkflowEntity workflow : wfList ) {
                if ( workflow != null ) {
                    final WorkflowDTO wfDto = prepareWorkflowDTO( entityManager, userId, workflow );

                    if ( isWorkflowVisible( entityManager, wfDto ) ) {
                        list.add( wfDto );
                    }
                }
            }

            return list;
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateWorkflowStatus( UUID userId, String workflowId, int versionId, int actionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // validates if the user has the role work flow manager, else throws exception
            boolean isupdated = false;
            isWorkflowManager( entityManager, userId );
            if ( !StringUtils.validateUUIDString( workflowId ) ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.INVALID_UUID, workflowId ) ), getClass() );
            }

            final WorkflowEntity entity = workflowDao.getWorkflowByIdAndVersionId( entityManager, UUID.fromString( workflowId ),
                    versionId );

            if ( entity != null ) {

                final long completedJobsCount = workflowDao.getCompletedJobCountByWorkflowIdAndVersion( entityManager,
                        UUID.fromString( workflowId ), versionId );
                // work flow with job completed count> 0 cannot be deleted
                if ( ( completedJobsCount > ConstantsInteger.INTEGER_VALUE_ZERO ) && ( WorkflowStatus.getById( actionId ).getKey()
                        == WorkflowStatus.DELETE.getKey() ) ) {

                    throw new SusException(
                            new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_JOB_CANNOT_DELETE, completedJobsCount ) ),
                            getClass() );
                }

                final UserEntity user = userManager.getSimUser( entityManager, userId );
                if ( user != null ) {
                    entity.setModifiedBy( user );
                }

                isupdated = true;
            }
            return isupdated;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< UserLicenseDTO > getLicenseConsumersList( UUID userId ) {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean clearToken( UUID userIdFromMessage, String token, UserLicenseDTO userLicense ) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLicenseDTO checkoutToken( UUID userId, String userName, String token ) {
        return null;
    }

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
    @Override
    public Object getSelectFieldOptionsFromSelectScript( String scriptId, String elementKey, String selectScriptPayloadJson,
            String userToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DynamicScript selectScript = DynamicScriptsUtil.getDynamicScriptDetailsFromConfigByNameAndKey( scriptId,
                    DynamicScript.SELECT_SCRIPTS_KEY );
            if ( selectScript == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), scriptId ) );
            }
            String fieldsJson = replaceSelectionFieldValues( entityManager, selectScriptPayloadJson );
            log.debug( "python payload for select script {} is {}", scriptId, fieldsJson );
            return DynamicScriptsUtil.callSelectScript( selectScript, TokenizedLicenseUtil.getNotNullUser( userToken ).getUserUid(),
                    elementKey, fieldsJson );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Replace selection field values string.
     *
     * @param entityManager
     *         the entity manager
     * @param selectScriptPayloadJson
     *         the select script payload json
     *
     * @return the string
     */
    private String replaceSelectionFieldValues( EntityManager entityManager, String selectScriptPayloadJson ) {
        try {
            SelectScriptPayload selectScriptPayload = JsonUtils.jsonToObject( selectScriptPayloadJson, SelectScriptPayload.class );
            for ( var field : selectScriptPayload.getInputArgument() ) {
                if ( field.getValue() == null || org.apache.commons.lang3.StringUtils.isEmpty( field.getValue().toString() ) ) {
                    continue;
                }
                String fieldType = field.getType();
                if ( FieldTypes.OBJECT.getType().equals( fieldType ) || FieldTypes.PROJECT_SELECTOR.getType().equals( fieldType ) ) {
                    var selectedIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, field.getValue().toString() );
                    if ( CollectionUtils.isEmpty( selectedIds ) ) {
                        field.setValue( null );
                    } else if ( selectedIds.size() <= 1 ) {
                        field.setValue( selectedIds.get( 0 ) );
                    } else {
                        field.setValue( selectedIds );
                    }
                } else if ( FieldTypes.CB2_OBJECTS.getType().equals( fieldType ) ) {
                    var selection = selectionManager.getSelectionEntityById( field.getValue().toString() );
                    if ( CollectionUtils.isEmpty( selection.getItems() ) ) {
                        field.setValue( null );
                    } else if ( selection.getItems().size() <= 1 ) {
                        BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                                UUID.fromString( selection.getItems().stream().findFirst().get().getItem() ) );
                        field.setValue( cb2ObjectEntity );
                    } else {
                        List< BmwCaeBenchEntity > selectedEntities = new ArrayList<>( selection.getItems().size() );
                        selection.getItems().forEach( item -> {
                            BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                                    UUID.fromString( item.getItem() ) );
                            selectedEntities.add( cb2ObjectEntity );
                        } );
                        field.setValue( selectedEntities );
                    }
                } else if ( FieldTypes.SERVER_FILE_EXPLORER.getType().equals( fieldType ) ) {
                    List< SsfeSelectionDTO > selectedList = selectionManager.getSelectedFilesFromSsfsSelection( entityManager,
                            field.getValue().toString() );
                    if ( CollectionUtils.isEmpty( selectedList ) ) {
                        field.setValue( null );
                    } else if ( selectedList.size() <= 1 ) {
                        field.setValue( selectedList.get( 0 ).getFullPath() );
                    } else {
                        field.setValue( selectedList.stream().map( SsfeSelectionDTO::getFullPath ).toList() );
                    }
                }
            }
            return JsonUtils.toJson( selectScriptPayload );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }

    }

    @Override
    public List< WfFieldsUiDTO > getSelectScriptFieldsFromConfig() {
        return DynamicScriptsUtil.getDynamicScriptFieldsFromConfigByKey( DynamicScript.SELECT_SCRIPTS_KEY );
    }

    @Override
    public List< WfFieldsUiDTO > getFieldScriptFieldsFromConfig() {
        return DynamicScriptsUtil.getDynamicScriptFieldsFromConfigByKey( DynamicScript.FIELD_SCRIPTS_KEY );
    }

    @Override
    public Map< String, Object > getFieldsFromFieldScript( String scriptId, String elementKey, String fieldsScriptPayloadJson,
            String userToken ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DynamicScript fieldScript = DynamicScriptsUtil.getDynamicScriptDetailsFromConfigByNameAndKey( scriptId,
                    DynamicScript.FIELD_SCRIPTS_KEY );
            if ( fieldScript == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_KEY.getKey(), scriptId ) );
            }
            String fieldsJson = replaceSelectionFieldValues( entityManager, fieldsScriptPayloadJson );
            log.debug( "python payload for fields script {} is {}", scriptId, fieldsJson );
            return DynamicScriptsUtil.callFieldScript( fieldScript, TokenizedLicenseUtil.getNotNullUser( userToken ).getUserUid(),
                    elementKey, fieldsJson );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLicenseDTO verifyLicenseCheckout( String token ) {
        if ( null == token ) {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.TOKEN_CANT_BE_NULL ) ), getClass() );
        }

        return licenseTokenManager.verifyLicenseCheckout( token );
    }

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
    @Override
    public WorkflowDTO prepareWorkflowDTO( EntityManager entityManager, UUID userId, WorkflowEntity entity ) {
        final WorkflowDTO dto = new WorkflowDTO();
        if ( entity == null ) {
            return dto;
        }
        dto.setId( entity.getComposedId() != null ? entity.getComposedId().getId().toString() : null );
        dto.setName( entity.getName() );
        dto.setInteractive( false );
        dto.setVersion( prepareVersion( entity.getVersionId() ) );
        dto.setDescription( entity.getDescription() );
        dto.setComments( entity.getComments() );
        if ( entity.getLifeCycleStatus() != null ) {
            final StatusConfigDTO statusDetail = lifeCycleManager.getLifeCycleStatusByStatusId( entity.getLifeCycleStatus() );
            dto.setExecutable( statusDetail != null && statusDetail.isExecutable() );
        }
        if ( entity.getTypeId() != null ) {
            dto.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( entity.getTypeId(), entity.getLifeCycleStatus(), entity.getConfig() ) );
            dto.setType( configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() ).getName() );
        }
        dto.setCreatedOn( entity.getCreatedOn() );
        dto.setModifiedOn( entity.getModifiedOn() );
        dto.setTypeId( entity.getTypeId() );
        dto.setConfig( entity.getConfig() );
        if ( entity.getDefinition() != null ) {
            dto.setDefinition( ( Map< String, Object > ) CollectionUtil.convertByteArrayToMap( entity.getDefinition() ) );
        }
        dto.setCreatedBy( userManager.prepareUserDtoFromUserEntity( entity.getCreatedBy() ) );
        if ( entity.getModifiedBy() != null ) {
            dto.setUpdatedBy( userManager.prepareUserDtoFromUserEntity( entity.getModifiedBy() ) );
        }
        // set jobs total and complete count
        final ProgressBar jobsProgress = new ProgressBar();
        jobsProgress.setTotal( workflowDao.getTotalJobCountByWorkflowIdAndVersion( entityManager, entity.getComposedId().getId(),
                entity.getComposedId().getVersionId() ) );
        jobsProgress.setCompleted( workflowDao.getCompletedJobCountByWorkflowIdAndVersion( entityManager, entity.getComposedId().getId(),
                entity.getComposedId().getVersionId() ) );
        dto.setJobs( jobsProgress );
        dto.setUserSelectionId( entity.getUserSelectionId() );
        dto.setGroupSelectionId( entity.getGroupSelectionId() );
        dto.setCurentSimUserId( userId );
        dto.setLocations( entity.getLocations() );
        dto.setCustomFlags( entity.getCustomFlags() );

        return dto;
    }

    /**
     * Prepare version.
     *
     * @param versionId
     *         the version id
     *
     * @return the version
     */
    private VersionDTO prepareVersion( int versionId ) {
        final VersionDTO version = new VersionDTO();
        version.setId( versionId );
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserManager( WorkflowUserManager userManager ) {
        this.userManager = userManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowUserManager getUserManager() {
        return userManager;
    }

    /**
     * Sets the workflow dao.
     *
     * @param workflowDao
     *         the new workflow dao
     */
    public void setWorkflowDao( WorkflowDAO workflowDao ) {
        this.workflowDao = workflowDao;
    }

    /**
     * This method validate elements and their fields. Length checks for name, comment, description etc Uniqueness of element in a work flow
     * Uniqueness of fields in an element
     *
     * @param userWorkFlow
     *         the user work flow
     * @param isImportWorkflow
     *         the is import workflow
     *
     * @return boolean
     */
    private boolean validateWorkFlowElementsAndFields( UserWorkFlow userWorkFlow, boolean isImportWorkflow ) {

        final HashSet< String > elementNameSet = new HashSet<>();
        for ( final UserWFElement element : userWorkFlow.getNodes() ) {
            if ( !elementNameSet.add( element.getName() ) ) {
                log.warn( "Duplicate element name found: {} in {}", element.getName(),
                        isImportWorkflow ? "imported workflow" : "workflow with id " + userWorkFlow.getId() );
                return false;
            }
        }
        return true;
    }

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
     */
    @Override
    public Set< String > getFilePathsFromWfJobParameters( String uid, JobParameters jobParameters, WorkflowDefinitionDTO jobParamDef,
            List< UserWFElement > jobWFElements ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Set< String > filePaths = new HashSet<>();

            // get paths of all local files used in workflow
            for ( UserWFElement element : jobWFElements ) {
                final List< Field< ? > > elementFields = element.getFields();
                for ( final Field< ? > field : elementFields ) {
                    if ( field.getType().equals( "os-file" ) && !field.isVariableMode() ) {
                        Map< String, Object > map = ( Map< String, Object > ) field.getValue();
                        String path = map.get( ITEMS ).toString();
                        path = path.substring( 1, path.length() - 1 ); // Remove square brackets

                        if ( new File( path ).exists() ) {
                            filePaths.add( path );
                        }
                    } else if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) ) {
                        ScanFileDTO scanFile = JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class );

                        if ( new File( scanFile.getFile() ).exists() ) {
                            filePaths.add( scanFile.getFile() );
                        }
                    } else if ( field.getType().equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                        UUID selectionId = UUID.fromString( ( String ) field.getValue() );
                        filePaths.add( getFullPath( entityManager, uid, selectionId, REGEX_OBJECT ) );
                    } else if ( field.getType().equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) ) {
                        UUID selectionId = UUID.fromString( ( String ) field.getValue() );
                        filePaths.add( getFullPath( entityManager, uid, selectionId, REGEX_OBJECT ) );
                    }
                }
            }

            return filePaths;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Checks if is workflow visible .
     *
     * @param entityManager
     *         the entity manager
     * @param workflowDto
     *         the workflow dto
     *
     * @return true, if is workflow visible
     */
    private boolean isWorkflowVisible( EntityManager entityManager, WorkflowDTO workflowDto ) {
        boolean isVisible = false;
        if ( ( workflowDto != null ) && ( workflowDto.getLifeCycleStatus() != null ) ) {

            // to handle the internal calls to get any workflow with default user id .NO
            // need to check visibility
            if ( workflowDto.getCurentSimUserId() == ConstantsString.DEFAULT_BE_USER_ID ) {

                return true;
            }

            final StatusConfigDTO detail = lifeCycleManager.getLifeCycleStatusByStatusId( workflowDto.getLifeCycleStatus().getId() );

            if ( detail != null ) {
                final String visibleTo = detail.getVisible();
                switch ( visibleTo ) {
                    case StatusConstants.VISIBLE_NONE ->
                            log.info( LOG_MESSAGE_WORKFLOW_WITH_STATUS + detail.getName() + LOG_MESSAGE_HAS_VISIBILTY + visibleTo );
                    case StatusConstants.VISIBLE_ALL -> {
                        log.info( LOG_MESSAGE_WORKFLOW_WITH_STATUS + detail.getName() + LOG_MESSAGE_HAS_VISIBILTY + visibleTo );
                        isVisible = true;
                    }
                    case StatusConstants.VISIBLE_OWNER -> {
                        log.info( LOG_MESSAGE_WORKFLOW_WITH_STATUS + detail.getName() + LOG_MESSAGE_HAS_VISIBILTY + visibleTo );
                        final UserEntity user = userManager.getSimUser( entityManager, workflowDto.getCurentSimUserId() );
                        if ( ( user != null ) && ( workflowDto.getCreatedBy() != null ) && workflowDto.getCreatedBy().getId()
                                .equals( "" + user.getId() ) ) {

                            isVisible = true;
                        } else {
                            log.error( LOG_MESSAGE_WORKFLOW_WITH_STATUS + detail.getName() + LOG_MESSAGE_HAS_VISIBILTY + visibleTo );
                            isVisible = false;
                        }
                    }
                    default -> throw new SusException( "Not a valid visible status: " + visibleTo );
                }

            }
            log.info( "is visible: " + isVisible );
        }
        return isVisible;
    }

    /**
     * Gets the user workflow by id.
     *
     * @param entityManager
     *         the entity manager
     * @param currentUserId
     *         the current user id
     * @param workflowId
     *         the workflow id
     *
     * @return the user workflow by id
     */
    private WorkflowDTO getUserWorkflowById( EntityManager entityManager, UUID currentUserId, String workflowId ) {
        boolean isVisible = false;
        WorkflowDTO dto = new WorkflowDTO();
        StatusConfigDTO detail = new StatusConfigDTO();
        // get all versions of workflow
        final List< WorkflowEntity > list = workflowDao.getWorkflowVersionsWithoutDefinition( entityManager,
                UUID.fromString( workflowId ) );
        if ( CollectionUtil.isNotEmpty( list ) ) {
            for ( final WorkflowEntity workflowEntity : list ) {
                dto = prepareWorkflowDTO( entityManager, currentUserId, workflowEntity );
                detail = lifeCycleManager.getLifeCycleStatusByStatusId( dto.getLifeCycleStatus().getId() );
                if ( isWorkflowVisible( entityManager, dto ) ) {
                    isVisible = true;
                    break;
                }
            }
        }
        if ( !isVisible ) {
            throw new SusException( LOG_MESSAGE_WORKFLOW_WITH_STATUS + detail.getName() + LOG_MESSAGE_HAS_VISIBILTY + detail.getVisible() );
        }
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createWorkflowForm( UUID userId, String parentId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isWorkflowManager( entityManager, userId );
            return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, new LatestWorkFlowDTO() ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editWorkflowForm( UUID userId, String parentId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isWorkflowManager( entityManager, userId );
            final SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
            if ( entity == null ) {
                throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.WORKFLOW_DOESNOT_EXIST_WITH_ID, parentId ) ),
                        getClass() );
            }
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                    entity.getConfig() );
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, prepareWorkflowDTO( ( WorkflowEntity ) entity ) );
            if ( PropertiesManager.hasTranslation() ) {
                updateTranslationFieldsUI( entity, userCommonManager.getUserEntityById( entityManager, userId ), uiFormItemList,
                        susObjectModel.hasTranslation() );

            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    private void updateTranslationFieldsUI( SuSEntity entity, UserEntity userEntity, List< UIFormItem > uiFormItemList,
            boolean objectHasTranslation ) {

        Map< String, String > languages = PropertiesManager.getRequiredlanguages();
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "selectedTranslations" );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( SELECT_TRANSLATION_LABEL ) );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        languages.keySet().forEach( language -> options.add( new SelectOptionsUI( language, languages.get( language ) ) ) );
        selectFormItem.setMultiple( Boolean.TRUE );
        selectFormItem.setOptions( options );
        selectFormItem.setValue(
                null != entity.getSelectedTranslations() ? JsonUtils.jsonToList( entity.getSelectedTranslations(), String.class )
                        : ConstantsString.EMPTY_STRING );
        selectFormItem.setBindFrom(
                BIND_FROM_URL_FOR_OBJECT_TRANSLATION_UPDATE.replace( PARAM_OBJECT_ID, entity.getComposedId().getId().toString() )
                        .replace( PARAM_TYPE_ID, entity.getTypeId().toString() ) );
        uiFormItemList.add( selectFormItem );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editWorkflowProjectForm( UUID userId, String parentId ) {
        final WorkflowDTO workflowDTO = new WorkflowDTO();
        workflowDTO.setId( parentId );
        return GUIUtils.createFormFromItems( GUIUtils.prepareForm( true, workflowDTO ) );
    }

    /**
     * {@inheritDoc}
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.server.manager.WorkflowManager#getWorkflowById(
     * java.util.UUID, java.util.UUID)
     */
    @Override
    public LatestWorkFlowDTO getWorkflowById( EntityManager entityManager, UUID userIdFromGeneralHeader, UUID workflowId ) {
        return prepareWorkflowDTO( ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, workflowId ) );
    }

    /**
     * Gets the tabs view workflow UI.
     *
     * @param objectId
     *         the object id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     *
     * @return the tabs view workflow UI
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getTabsViewWorkflowUI(java.lang.String)
     */
    @Override
    public SubTabsItem getTabsViewWorkflowUI( String objectId, UUID userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedtoRead( entityManager, userIdFromGeneralHeader.toString(), objectId );
            final WorkflowEntity entity = ( WorkflowEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( objectId ) );
            final List< SubTabsUI > subTabsUIs = new ArrayList<>();
            final SuSObjectModel model = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );
            for ( final OVAConfigTab ovaConfigTab : model.getViewConfig() ) {
                if ( ( ovaConfigTab.getKey() != null ) && ovaConfigTab.getKey().equalsIgnoreCase( TAB_KEY_DESIGNER ) ) {
                    licenseManager.checkIfFeatureAllowedToUser( entityManager, MANAAGER_FEATURES );
                    subTabsUIs.add( new SubTabsUI( ovaConfigTab.getKey(), ovaConfigTab.getKey(),
                            MessageBundleFactory.getMessage( ovaConfigTab.getTitle() ) ) );
                } else {
                    subTabsUIs.add( new SubTabsUI( ovaConfigTab.getKey(), ovaConfigTab.getKey(),
                            MessageBundleFactory.getMessage( ovaConfigTab.getTitle() ) ) );
                }
            }

            return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), entity.getVersionId(), subTabsUIs, null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getWorkflowSingleUI( String objectId ) {

        return GUIUtils.listColumns( LatestWorkFlowDTO.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getWorkflowVersionsUI( String objectId ) {
        final List< TableColumn > columns = GUIUtils.listColumns( LatestWorkFlowDTO.class, VersionDTO.class );
        prepareWorkflowTableColumns( columns );
        return columns;
    }

    /**
     * Workflow permission table UI.
     *
     * @return the list
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * workflowPermissionTableUI()
     */
    @Override
    public List< TableColumn > workflowPermissionTableUI() {
        final java.lang.reflect.Field[] attributes = ManageObjectDTO.class.getDeclaredFields();
        return permissionManager.extractColumnList( attributes );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO filter, UUID objectId, UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OBJECT.getKey() ) );
            }
            final boolean isManageable = permissionManager.isManageable( entityManager, userId, objectId );
            final List< ManageObjectDTO > objectManagerDTOs = permissionManager.prepareObjectManagerDTOs( entityManager, objectId,
                    isManageable, filter );

            List< ManageObjectDTO > filteredList = objectManagerDTOs.subList( filter.getStart(),
                    ( objectManagerDTOs.size() - filter.getStart() ) <= filter.getLength() ? objectManagerDTOs.size()
                            : ( filter.getStart() + filter.getLength() ) );

            filter.setFilteredRecords( ( long ) objectManagerDTOs.size() );
            filter.setTotalRecords( ( ( long ) objectManagerDTOs.size() ) );
            return PaginationUtil.constructFilteredResponse( filter, filteredList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToWorkFlow( CheckBox checkBox, UUID objectId, UUID securityId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        log.info( ">>permitPermissionToWorkFlow checkBox:" + checkBox );
        try {
            return permissionManager.permitPermissionToObject( entityManager, checkBox, objectId, securityId, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the workflow versions.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     * @param filter
     *         the filter
     *
     * @return the workflow versions
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.server.manager.WorkflowManager#getWorkflowVersions
     * (java.util.UUID, java.util.UUID,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public FilteredResponse< LatestWorkFlowDTO > getWorkflowVersions( UUID userIdFromGeneralHeader, UUID workflowId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isPermittedtoRead( entityManager, userIdFromGeneralHeader.toString(), workflowId.toString() );

            final List< LatestWorkFlowDTO > workflowDTOList = new ArrayList<>();
            final SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, workflowId );
            final SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            // fix-me for generic jpa
            final List< SuSEntity > dataObjectEntityList = susDAO.getAllFilteredVersionsById( entityManager, SuSEntity.class, workflowId,
                    filter, userIdFromGeneralHeader, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ) );
            if ( CollectionUtil.isNotEmpty( dataObjectEntityList ) ) {
                for ( final SuSEntity susEntity : dataObjectEntityList ) {
                    workflowDTOList.add( prepareWorkflowDTO( ( WorkflowEntity ) susEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, workflowDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the data object DTO from data object entity.
     *
     * @param workflowEntity
     *         the data object entity
     *
     * @return the data object DTO
     */
    @Override
    public LatestWorkFlowDTO prepareWorkflowDTO( WorkflowEntity workflowEntity ) {
        LatestWorkFlowDTO workflowDTO = null;

        if ( workflowEntity != null ) {
            workflowDTO = new LatestWorkFlowDTO();
            workflowDTO.setName( workflowEntity.getName() );
            workflowDTO.setId( workflowEntity.getComposedId().getId() );

            workflowDTO.setVersion( new VersionDTO( workflowEntity.getComposedId().getVersionId() ) );
            workflowDTO.setCreatedOn( workflowEntity.getCreatedOn() );
            workflowDTO.getModifiedOn( workflowEntity.getModifiedOn() );
            if ( workflowEntity.getCreatedBy() != null ) {
                workflowDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( workflowEntity.getCreatedBy() ) );
            }
            if ( workflowEntity.getModifiedBy() != null ) {
                workflowDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( workflowEntity.getModifiedBy() ) );
            }
            workflowDTO.setDescription( workflowEntity.getDescription() );
            workflowDTO.setComments( workflowEntity.getComments() );
            workflowDTO.setTypeId( workflowEntity.getTypeId() );
            workflowDTO.setConfig( workflowEntity.getConfig() );
            if ( workflowEntity.getTypeId() != null ) {
                workflowDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( workflowEntity.getTypeId(), workflowEntity.getLifeCycleStatus(),
                                workflowEntity.getConfig() ) );
                workflowDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( workflowEntity.getTypeId().toString(), workflowEntity.getConfig() )
                                .getName() );
            }
            if ( null != workflowEntity.getRunOnLocation() ) {
                workflowDTO.setRunOnLocation( prepareLocationDTO( workflowEntity.getRunOnLocation() ) );
            }
        }
        return workflowDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getListOfWorkflowsUITableColumns( String objectId ) {

        final List< TableColumn > columns = GUIUtils.listColumns( LatestWorkFlowDTO.class );

        prepareWorkflowTableColumns( columns );

        return columns;
    }

    /**
     * Gets the list of work flow DTOUI table columns.
     *
     * @return the list of work flow DTOUI table columns
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getListOfWorkFlowDTOUITableColumns()
     */
    @Override
    public List< TableColumn > getListOfWorkFlowDTOUITableColumns() {

        final List< TableColumn > columns = GUIUtils.listColumns( LatestWorkFlowDTO.class );

        prepareWorkflowTableColumns( columns );

        return columns;
    }

    /**
     * Prepares workflow table columns.
     *
     * @param columns
     *         the columns
     */
    private void prepareWorkflowTableColumns( List< TableColumn > columns ) {
        for ( final TableColumn tableColumn : columns ) {

            if ( NAME_FIELD.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( VIEW_WORKFLOW_BY_ID_AND_VERSION_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }

            if ( tableColumn.getName().equalsIgnoreCase( ID_FIELD ) || tableColumn.getName().equalsIgnoreCase( "id" ) ) {
                tableColumn.setVisible( false );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getListOfWorkflowProjectUITableColumns( String objectId ) {

        final List< TableColumn > columns = GUIUtils.listColumns( WorkflowProjectDTO.class );

        for ( final TableColumn tableColumn : columns ) {

            if ( NAME_FIELD.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( VIEW_WORKFLOW_PROJECT_URL );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }

            if ( tableColumn.getName().equalsIgnoreCase( ID_FIELD ) || tableColumn.getName().equalsIgnoreCase( "id" ) ) {
                tableColumn.setVisible( false );
            }

        }
        return columns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< LatestWorkFlowDTO > getAllWorkflows( UUID userId, UUID projectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            isWorkflowUser( entityManager, userId );
            final List< LatestWorkFlowDTO > objectDTOList = new ArrayList<>();
            final SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, projectId );
            final SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            final List< SuSEntity > sustEntityList = susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndPermission( entityManager,
                    WorkflowEntity.class, filter, projectId, userId.toString(),
                    lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    configManager.getTypesFromConfiguration( latestEntity.getConfig() ), PermissionMatrixEnum.VIEW.getKey() );
            UserDTO user = TokenizedLicenseUtil.getUser(
                    BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
            sustEntityList.forEach( entity -> translateName( user, entity ) );
            for ( final SuSEntity susEntity : sustEntityList ) {
                objectDTOList.add( prepareWorkflowDTO( ( WorkflowEntity ) susEntity ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowProjectDTO getWorkflowProjectById( UUID userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowProjectDTO projectDTO = null;
            if ( projectId != null ) {
                final SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, ContainerEntity.class, projectId );
                if ( susEntity != null ) {
                    projectDTO = new WorkflowProjectDTO();
                    projectDTO.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
                    projectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
                    projectDTO.setName( susEntity.getName() );
                }
            }
            return projectDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Insert super user security identity.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return true, if successful
     */
    private boolean insertSuperUserSecurityIdentity( EntityManager entityManager ) {
        boolean isInserted = false;
        if ( userCommonManager.getUserById( entityManager, UUID.fromString( ConstantsID.SUPER_USER_ID ) ) == null ) {
            final UserEntity userEntity = userCommonManager.getUserCommonDAO().save( entityManager, prepareUserEntityForSuperUser() );
            if ( userEntity != null ) {
                isInserted = true;
            }
        } else {
            isInserted = true;
        }
        return isInserted;
    }

    /**
     * Prepare user entity fro super user.
     *
     * @return the user entity
     */
    private UserEntity prepareUserEntityForSuperUser() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
        final AclSecurityIdentityEntity acl = new AclSecurityIdentityEntity();
        acl.setId( UUID.fromString( ConstantsID.SUPER_USER_SID_ID ) );
        acl.setSid( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
        acl.setCreatedOn( new Date() );
        userEntity.setSecurityIdentityEntity( acl );
        userEntity.setDirectory( null );
        userEntity.setUserUid( ConstantsString.SIMUSPACE );
        userEntity.setSurName( ConstantsString.SIMUSPACE );
        userEntity.setFirstName( ConstantsString.SIMUSPACE );
        userEntity.setStatus( true );
        userEntity.setChangeable( true );
        userEntity.setRestricted( false );
        userEntity.setUserDetails( fillUserDetailEntity() );
        userEntity.setPassword( FileUtils.getSha256CheckSum( ConstantsString.SIMUSPACE ) );
        userEntity.setGroups( null );
        userEntity.setCreatedOn( new Date() );
        return userEntity;
    }

    /**
     * Fill user detail entity.
     *
     * @return the sets the
     */
    private Set< UserDetailEntity > fillUserDetailEntity() {
        final Set< UserDetailEntity > list = new HashSet<>();
        final UserDetailEntity userDetailEntity = new UserDetailEntity( UUID.randomUUID(), ConstantsString.SIMUSPACE,
                ConstantsString.SIMUSPACE, ConstantsString.SIMUSPACE, ConstantsString.SIMUSPACE, null, null );
        list.add( userDetailEntity );
        return list;
    }

    /**
     * Gets the execution manager.
     *
     * @return the execution manager
     */
    public WFExecutionManager getExecutionManager() {
        return executionManager;
    }

    /**
     * Sets the execution manager.
     *
     * @param executionManager
     *         the new execution manager
     */
    public void setExecutionManager( WFExecutionManager executionManager ) {
        this.executionManager = executionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerJobForDOE( EntityManager entityManager, UUID userId, JobParameters jobParameters,
            Map< String, Object > designSummary, UUID masterJobId, Integer masterJobInteger, String masterJobName ) {

        if ( ( jobParameters.getWorkflow().getId() != null ) && ( !userId.toString().equals( ConstantsID.SUPER_USER_ID )
                && !permissionManager.isPermitted( entityManager, userId.toString(),
                jobParameters.getWorkflow().getId() + COLON + PermissionMatrixEnum.EXECUTE.getValue() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), OBJECT ) );
        }
        final UserEntity user = userManager.getSimUser( entityManager, userId );
        startJobOnLocation( entityManager, user.getId(), jobParameters, designSummary, masterJobId, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerSideJob( UUID userId, JobParameters jobParameters ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            runServerSideJob( entityManager, userId, jobParameters );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerSideJob( EntityManager entityManager, UUID userId, JobParameters jobParameters ) {
        final UserEntity user = userManager.getSimUser( entityManager, userId );
        runServerJob( entityManager, user, jobParameters, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runServerJob( EntityManager entityManager, UserEntity userEntity, JobParameters jobParameters, String loadcaseName ) {
        if ( ( jobParameters.getWorkflow().getId() != null ) && ( !userEntity.getId().toString().equals( ConstantsID.SUPER_USER_ID )
                && !permissionManager.isPermitted( entityManager, userEntity.getId().toString(),
                jobParameters.getWorkflow().getId() + COLON + PermissionMatrixEnum.EXECUTE.getValue() ) ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), jobParameters.getWorkflow().getName() ) );
        }
        List< CustomVariableDTO > customVariable = ( List< CustomVariableDTO > ) getExecutionManager().getCustomVariable(
                jobParameters.getServer(), jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );
        jobParameters.setCustomVariables( customVariable );

        startJobOnLocation( entityManager, userEntity.getId(), jobParameters, null, null, loadcaseName );
    }

    /**
     * Start job on location.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param customVariable
     *         the custom variable
     * @param masterJobId
     *         the master job id
     * @param loadcaseName
     *         the loadcase name
     */
    private void startJobOnLocation( EntityManager entityManager, UUID userId, JobParameters jobParameters,
            Map< String, Object > designSummary, UUID masterJobId, String loadcaseName ) {
        final UserEntity user = userManager.getSimUser( entityManager, userId );
        LocationDTO locationDTO = prepareLocationForJob( entityManager, jobParameters );
        if ( locationDTO.getUrl() == null || locationDTO.getUrl().isEmpty() ) {
            throw new SusException( "Please select server location to run workflow from WEB." );
        }
        validateActiveLocationForJobSubmission( locationDTO );
        permissionManager.isPermitted( entityManager, userId.toString(),
                locationDTO.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue() + ConstantsString.COLON + IS_LOCATION,
                Messages.NO_RIGHTS_TO_EXECUTE.getKey(), locationDTO.getName() );

        setWorkingDirPath( user.getUserUid(), jobParameters, locationDTO );

        jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );

        // create simple Job here
        if ( jobParameters.getJobInteger() != null ) {
            Integer jobInt = jobManager.saveJobIdIfNotExist( entityManager, UUID.fromString( jobParameters.getId() ) );
            jobParameters.setJobInteger( jobInt );
        }

        if ( masterJobId != null ) {
            Relation relationEntity = new Relation( masterJobId, UUID.fromString( jobParameters.getId() ) );
            susDAO.createRelation( entityManager, relationEntity );
        }
        updateCustomVariableField( entityManager, user, jobParameters );
        createDesignVariableFilesInStaging( entityManager, user, jobParameters, designSummary, masterJobId );
        createParserDesignVariableFilesInStaging( entityManager, user, jobParameters, designSummary, masterJobId );
        downloadFilesInStagingForAssembleAndSimulate( entityManager, user, jobParameters, masterJobId );

        postJobToLocation( userId, jobParameters, loadcaseName, user.getUserUid(), locationDTO );

    }

    /**
     * Post job to location.
     *
     * @param userId
     *         the userUid id
     * @param jobParameters
     *         the job parameters
     * @param loadcaseName
     *         the loadcase name
     * @param userUid
     *         the userUid
     * @param locationDTO
     *         the location dto
     *
     * @return sus response dto
     */
    private SusResponseDTO postJobToLocation( UUID userId, JobParameters jobParameters, String loadcaseName, String userUid,
            LocationDTO locationDTO ) {
        final JobParametersLocationModel jobParametersLocationModel = new JobParametersLocationModel( jobParameters, userUid,
                PasswordUtils.getPasswordById( userId.toString() ) );

        if ( loadcaseName != null ) {
            jobParametersLocationModel.setLoadcaseName( loadcaseName );
        }

        final String payload = JsonUtils.toJson( jobParametersLocationModel );
        return SuSClient.postRequest( locationDTO.getUrl() + "/api/core/location/run/job", payload,
                prepareLocationHeaders( locationDTO.getAuthToken() ) );
    }

    /**
     * Prepare location for job location dto.
     *
     * @param entityManager
     *         the entity manager
     * @param jobParameters
     *         the job parameters
     *
     * @return the location dto
     */
    private LocationDTO prepareLocationForJob( EntityManager entityManager, JobParameters jobParameters ) {
        LocationDTO locationDTO;
        if ( Boolean.TRUE.equals( PropertiesManager.isHostEnable() ) ) {
            ExecutionHosts executionHost = null;
            Hosts hostList = PropertiesManager.getHosts();
            if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
                for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                    if ( host.getId().toString().equals( jobParameters.getRunsOn() ) ) {
                        executionHost = host;
                        break;
                    }
                }
            }
            if ( null == executionHost ) {
                locationDTO = locationManager.getLocation( entityManager, jobParameters.getRunsOn() );
            } else {
                locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
            }
        } else {
            locationDTO = locationManager.getLocation( entityManager, jobParameters.getRunsOn() );
        }
        return locationDTO;
    }

    /**
     * Update custom variable field.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param customVariable
     *         the custom variable
     */
    private void updateCustomVariableField( EntityManager entityManager, UserEntity user, JobParameters jobParameters ) {
        try {
            WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( jobParameters.getWorkflow().getElements() ),
                    WorkflowModel.class );
            List< CustomVariableEntity > customVariableEntities = variableDAO.getAllCustomVariables( entityManager,
                    jobParameters.getWorkflow().getId().toString(), user.getId().toString() );
            if ( !customVariableEntities.isEmpty() ) {
                workflowModel.getNodes().stream().map( WorkflowElement::getData )
                        .filter( data -> ElementKeys.IO.getKey().equals( data.getKey() ) )
                        .forEach( element -> element.getFields().forEach( rawField -> {
                            Field< String > field = ( Field< String > ) rawField;
                            List< CustomVariableDTO > customVariables;
                            customVariables = getCustomVariablesByFieldType( entityManager, jobParameters, element, field,
                                    customVariableEntities );
                            VariableUtil.replaceCustomVariablesInWorkflowModel( field.getName(), customVariables, element.getName(),
                                    workflowModel );
                        } ) );
                jobParameters.getWorkflow().setElements( JsonUtils.convertStringToMapGenericValue( JsonUtils.toJson( workflowModel ) ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Gets custom variables by field type.
     *
     * @param entityManager
     *         the entity manager
     * @param jobParameters
     *         the job parameters
     * @param element
     *         the element
     * @param field
     *         the field
     * @param customVariableEntities
     *         the custom variable entities
     *
     * @return the custom variables by field type
     */
    private List< CustomVariableDTO > getCustomVariablesByFieldType( EntityManager entityManager, JobParameters jobParameters,
            UserWFElement element, Field< String > field, List< CustomVariableEntity > customVariableEntities ) {
        List< CustomVariableDTO > customVariables;
        if ( field.isChangeOnRun() ) {
            if ( FieldTypes.OBJECT_PARSER.getType().equals( field.getType() ) ) {
                customVariables = getParserAORCustomVariableList( entityManager, field, element.getName() );
            } else if ( FieldTypes.REGEX_SCAN_SERVER.getType().equals( field.getType() ) ) {
                customVariables = getRegexAORCustomVariableList( entityManager, field, element.getName(),
                        jobParameters.getWorkflow().getId() );
            } else if ( FieldTypes.TEMPLATE_SCAN_SERVER.getType().equals( field.getType() ) ) {
                customVariables = getTemplateAORCustomVariableList( entityManager, field, element.getName(),
                        jobParameters.getWorkflow().getId() );
            } else {
                customVariables = new ArrayList<>();
            }
        } else {
            customVariables = customVariableEntities != null ? customVariableEntities.stream().filter(
                            entity -> element.getName().equals( entity.getElementName() ) && field.getName().equals( entity.getFieldName() ) )
                    .map( this::prepareCustomVariableModelFromEntity ).toList() : new ArrayList<>();

        }
        return customVariables;
    }

    /**
     * Gets template aor custom variable list.
     *
     * @param entityManager
     *         the entity manager
     * @param field
     *         the field
     * @param elementName
     *         the element name
     * @param workflowId
     *         the workflow id
     *
     * @return the template aor custom variable list
     */
    private List< CustomVariableDTO > getTemplateAORCustomVariableList( EntityManager entityManager, Field< String > field,
            String elementName, UUID workflowId ) {
        String fieldValue = field.getValue();
        List< CustomVariableDTO > customVariables = new ArrayList<>();
        List< TemplateEntity > templateEntities = templateManager.getTemplateDAO()
                .getTemplateListBySelectionId( entityManager, UUID.fromString( fieldValue ) );
        if ( CollectionUtils.isEmpty( templateEntities ) ) {
            return customVariables;
        }
        if ( FieldTemplates.CUSTOM_VARIABLE.getValue().equals( field.getTemplateType() ) ) {
            customVariables = templateEntities.stream()
                    .map( templateEntity -> VariableUtil.prepareCustomVariableDTOFromTemplateEntity( templateEntity, elementName,
                            field.getName(), workflowId ) ).collect( Collectors.toList() );
        }
        return customVariables;
    }

    /**
     * Gets regex aor custom variable list.
     *
     * @param entityManager
     *         the entity manager
     * @param field
     *         the field
     * @param elementName
     *         the element name
     * @param workflowId
     *         the workflow id
     *
     * @return the regex aor custom variable list
     */
    private List< CustomVariableDTO > getRegexAORCustomVariableList( EntityManager entityManager, Field< String > field, String elementName,
            UUID workflowId ) {

        String fieldValue = field.getValue();
        List< CustomVariableDTO > customVariables = new ArrayList<>();
        if ( StringUtils.isNotNullOrEmpty( fieldValue ) ) {
            List< RegexEntity > regexEntities = regexManager.getRegexDAO()
                    .getRegexListBySelectionId( entityManager, UUID.fromString( fieldValue ) );
            if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                for ( RegexEntity regexEntity : regexEntities ) {
                    customVariables.add(
                            VariableUtil.prepareCustomVariableDTOFromRegexEntity( regexEntity, elementName, field.getName(), workflowId ) );
                }
            }
        }
        return customVariables;
    }

    /**
     * Gets parser aor custom variable list.
     *
     * @param entityManager
     *         the entity manager
     * @param field
     *         the field
     * @param elementName
     *         the element name
     *
     * @return the parser aor custom variable list
     */
    private List< CustomVariableDTO > getParserAORCustomVariableList( EntityManager entityManager, Field< String > field,
            String elementName ) {
        String fieldValue = field.getValue();
        if ( StringUtils.isNotNullOrEmpty( fieldValue ) ) {
            List< CustomVariableDTO > customVariables;
            ParserEntity schemaEntity = parserManager.getParserSchemaDAO()
                    .getLatestObjectById( entityManager, ParserEntity.class, UUID.fromString( fieldValue ) );
            customVariables = parserManager.getSelectedParserEntriesListCustomVariables( schemaEntity );
            customVariables.forEach( customVariable -> {
                customVariable.setElementName( elementName );
                customVariable.setFieldName( field.getName() );
            } );
            return customVariables;

        }
        return new ArrayList<>();
    }

    /**
     * Download shape module schema.
     *
     * @return the object
     */
    public Object downloadShapeModuleSchema() {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        String SM_SCHEMA_URL = PropertiesManager.SHAPE_MODULE_CFG_URL();
        log.info( "SM_SCHEMA_URL>>" + SM_SCHEMA_URL );
        CloseableHttpResponse closeableHttpResponse = SuSClient.getExternalRequest( SM_SCHEMA_URL, requestHeaders );

        try {
            HttpEntity entity = closeableHttpResponse.getEntity();
            String resp = EntityUtils.toString( entity, "UTF-8" );
            com.fasterxml.jackson.databind.JsonNode node = JsonUtils.toJsonNode( resp );
            log.info( "resp:>>" + node.toString() );
            return node.toString();
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Download cb 2 files in staging.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param masterJobId
     *         the master job id
     *
     * @return the string
     */
    private String downloadFilesInStagingForAssembleAndSimulate( EntityManager entityManager, UserEntity user, JobParameters jobParameters,
            UUID masterJobId ) {

        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        Map< String, Map< String, String > > allObjectFieldIds = getSUSObjectSelectedFromJobParametersForAssembleAndSimulate( jobParamDef );

        if ( !allObjectFieldIds.isEmpty() ) {
            final String bmwSuggestion = File.separator + jobParameters.getName() + "_" + jobParameters.getJobInteger();
            String jobDirPathStagging = PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + bmwSuggestion;

            File jobDir = new File( jobDirPathStagging );

            LinuxUtils.createDirectory( user.getUserUid(), jobDir.getAbsolutePath() );

            Map< String, String > allCB2ObjectFieldIds = allObjectFieldIds.get( "cb2-object" );
            Map< String, String > allsusbjectFieldIds = allObjectFieldIds.get( "sus-object" );

            // download cb2 files in staging and rename file
            if ( allCB2ObjectFieldIds != null ) {
                for ( Entry< String, String > cb2ObjectsFieldId : allCB2ObjectFieldIds.entrySet() ) {
                    BmwCaeBenchEntity bmwCaeBenchEntity = downloadCB2ObjectsOfField( entityManager, user, cb2ObjectsFieldId.getValue(),
                            jobDir, masterJobId );
                    if ( bmwCaeBenchEntity != null ) {
                        String cb2SrcPath = jobDir.getAbsolutePath() + File.separator + bmwCaeBenchEntity.getName();
                        String cb2DesPath = jobDir.getAbsolutePath() + File.separator + cb2ObjectsFieldId.getKey();
                        try {

                            LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), cb2SrcPath, cb2DesPath );
                            LinuxUtils.deleteFileOrDirByPath( user.getUserUid(), cb2SrcPath );

                        } catch ( Exception e ) {
                            log.error( ERROR_DOWNLOADING_RE_NAMING_CB2_ASSEMBLE_AND_SIMULATE_RELATED_FILE + cb2SrcPath, e );
                            throw new SusException( ERROR_DOWNLOADING_RE_NAMING_CB2_ASSEMBLE_AND_SIMULATE_RELATED_FILE + e.getMessage() );
                        }
                    }
                }
            }
            // download sus object file in staging and rename it with sus object name
            if ( allsusbjectFieldIds != null ) {
                for ( Entry< String, String > susObjectsFieldId : allsusbjectFieldIds.entrySet() ) {

                    List< String > selectedSusObjects = selectionManager.getSelectedIdsStringListBySelectionId( entityManager,
                            susObjectsFieldId.getValue() );
                    if ( selectedSusObjects != null && !selectedSusObjects.isEmpty() ) {
                        for ( String susObj : selectedSusObjects ) {
                            SuSEntity susEnt = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( susObj ) );
                            if ( susEnt instanceof DataObjectFileEntity dataObjectFileEntity ) {

                                DocumentEntity doc = dataObjectFileEntity.getFile();
                                if ( doc != null ) {
                                    File vaultFile = SuSVaultUtils.getEncrypedFileFromPathIfEncpEnabled( doc.getId(),
                                            ConstantsInteger.INTEGER_VALUE_ONE, PropertiesManager.getVaultPath() );

                                    File decryptedTempFile = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndSave(
                                            new DocumentDTO( doc.isEncrypted(),
                                                    prepareEncryptionDecryptionDTO( doc.getEncryptionDecryption() ) ), vaultFile,
                                            PropertiesManager.getDefaultServerTempPath() + File.separator + vaultFile.getName() );

                                    FileUtils.setGlobalAllFilePermissions( decryptedTempFile );

                                    String destPath = jobDir.getAbsolutePath() + File.separator + susEnt.getName();

                                    try {

                                        LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), decryptedTempFile.getAbsolutePath(),
                                                destPath );

                                    } catch ( Exception e ) {
                                        log.error( ERROR_COPY_SUS_OBJECT_ASSEMBLE_AND_SIMULATE_RELATED_FILE + vaultFile, e );
                                        throw new SusException( ERROR_COPY_SUS_OBJECT_ASSEMBLE_AND_SIMULATE_RELATED_FILE + e.getMessage() );
                                    } finally {
                                        if ( decryptedTempFile.exists() ) {
                                            decryptedTempFile.delete();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Download cb 2 files in staging.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param masterJobId
     *         the master job id
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    private String downloadCb2FilesInStaging( EntityManager entityManager, UserEntity user, JobParameters jobParameters, UUID masterJobId )
            throws IOException {

        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        Set< String > allCB2ObjectFieldIds = getCB2ObjectSelectedFromJobParameters( jobParamDef );

        if ( !allCB2ObjectFieldIds.isEmpty() ) {
            final String bmwSuggestion = File.separator + jobParameters.getName() + "_" + jobParameters.getJobInteger();
            String jobDirPathStagging = PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + bmwSuggestion;

            File jobDir = new File( jobDirPathStagging );

            LinuxUtils.createDirectory( user.getUserUid(), jobDir.getAbsolutePath() );

            for ( String cb2ObjectsFieldId : allCB2ObjectFieldIds ) {
                downloadCB2ObjectsOfField( entityManager, user, cb2ObjectsFieldId, jobDir, masterJobId );
            }
        }

        return null;
    }

    /**
     * Downloads cb2 objects of field.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param cb2ObjectsFieldId
     *         the cb2ObjectsFieldId
     * @param jobDir
     *         the job dir
     * @param masterJobId
     *         the master job id
     *
     * @return the bmw cae bench entity
     */
    private BmwCaeBenchEntity downloadCB2ObjectsOfField( EntityManager entityManager, UserEntity user, String cb2ObjectsFieldId,
            File jobDir, UUID masterJobId ) {
        BmwCaeBenchEntity cb2ObjectEntity = null;

        List< String > selectedCB2Objects = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, cb2ObjectsFieldId );
        if ( selectedCB2Objects != null && !selectedCB2Objects.isEmpty() ) {
            for ( String cb2OID : selectedCB2Objects ) {
                String cb2DesPath = jobDir.getAbsolutePath();
                cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( cb2OID ) );

                // only download submodels
                if ( cb2ObjectEntity.getBmwCaeDataType().contains( BmwCaeBenchEnums.CB2_SUBMODEL.getValue() ) ) {
                    ProcessResult commandResult = PythonUtils.downloadCB2ObjectFileByCB2Oid( user.getUserUid(), cb2ObjectEntity.getOid(),
                            cb2DesPath, cb2ObjectEntity.getBmwCaeDataType() );

                    if ( commandResult.getExitValue() == 2 ) {
                        log.error( commandResult.getErrorStreamString() );
                        if ( masterJobId != null ) {
                            String errorMsg = "ERROR downloading CB2 File " + commandResult.getErrorStreamString();
                            updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
                        }
                        throw new SusException( "File not found on cb2 for " + cb2ObjectEntity.getName() );
                    } else if ( commandResult.getExitValue() != 0 ) {
                        log.error( commandResult.getErrorStreamString() );
                        if ( masterJobId != null ) {
                            String errorMsg = "ERROR downloading CB2 File " + commandResult.getErrorStreamString();
                            updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
                        }
                        throw new SusException( "ERROR downloading CB2 File" + cb2ObjectEntity.getName() );
                    }
                    if ( commandResult.getErrorStreamString() != null && !commandResult.getErrorStreamString().isEmpty() ) {
                        log.warn( commandResult.getErrorStreamString() );
                    }
                }
            }
        }
        return cb2ObjectEntity;
    }

    /**
     * Creates the parser file in staging.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param masterJobId
     *         the master job id
     *
     * @return the string
     */
    private List< String > createParserDesignVariableFilesInStaging( EntityManager entityManager, UserEntity user,
            JobParameters jobParameters, Map< String, Object > designSummary, UUID masterJobId ) {
        try {
            if ( designSummary != null ) {
                final WorkflowDefinitionDTO definitionDTO = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                        jobParameters.getWorkflow().prepareDefinition() );
                List< UUID > parserIdList = getListOfParserIdsFromWorkflowDefinition( definitionDTO );
                if ( !parserIdList.isEmpty() ) {
                    final String bmwSuggestion = File.separator + jobParameters.getName() + "_" + jobParameters.getJobInteger();

                    String jobDirPathStaging = PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + bmwSuggestion;

                    File jobDir = new File( jobDirPathStaging );

                    LinuxUtils.createDirectory( user.getUserUid(), jobDir.getAbsolutePath() );

                    List< ParserEntity > parserEntities = parserManager.getParserSchemaDAO()
                            .getAllLatestObjectsByListOfIdsAndClazz( entityManager, parserIdList, ParserEntity.class );

                    List< ParserVariableDTO > selectedParserList = parserManager.getSelectedParserEntriesList( parserEntities );
                    for ( ParserVariableDTO parserVariableDTO : selectedParserList ) {
                        if ( designSummary.containsKey( parserVariableDTO.getVariableName() ) ) {
                            parserVariableDTO.setScannedValue( String.valueOf( designSummary.get( parserVariableDTO.getVariableName() ) ) );
                        }
                    }
                    List< ParserPartDTO > convertedSelectionsUpdateData = parserManager.convertParserVariableDTOListToParserPartDTO(
                            selectedParserList );

                    ParserDTO selectedFinal = parserManager.preparePartDTOFromParserPartDTOList( convertedSelectionsUpdateData );

                    List< String > outputFiles = new ArrayList<>();
                    for ( ParserEntity parserEntity : parserEntities ) {
                        Map< String, String > formMap = parserManager.getFormDataFromJson( parserEntity.getFormJsonAsString() );
                        String parserType = formMap.get( TYPE );
                        String scriptOutputFileName = null;
                        String sharedDirectory =
                                PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow()
                                        .getId() + File.separator + FILES;
                        if ( VariableDropDownEnum.INTERNAL.getId().equalsIgnoreCase( parserType ) || VariableDropDownEnum.LOCAL.getId()
                                .equalsIgnoreCase( parserType ) ) {
                            scriptOutputFileName = prepareSelectedFileInStagingForInternalOrLocalParserObject( entityManager,
                                    user.getUserUid(), jobDirPathStaging, parserType, formMap.get( SELECTION ), sharedDirectory );
                        } else if ( VariableDropDownEnum.SERVER.getId().equalsIgnoreCase( parserType ) ) {
                            scriptOutputFileName = prepareSelectedFileInStaginForServerParserObject( user.getUserUid(), parserEntity,
                                    formMap.get( SELECTION ), sharedDirectory, jobDirPathStaging );
                        } else if ( VariableDropDownEnum.CB2.getId().equalsIgnoreCase( parserType ) ) {
                            scriptOutputFileName = prepareSelectedFileInStagingForCB2ParserObject( entityManager, user.getUserUid(),
                                    masterJobId, formMap.get( SELECTION ), sharedDirectory, jobDirPathStaging );
                        }
                        if ( scriptOutputFileName != null ) {
                            String scriptOutputFilePath = updateParsedFileInJobDirectory( user.getUserUid(), jobDirPathStaging,
                                    selectedFinal, formMap.get( PARSER ), scriptOutputFileName, sharedDirectory );
                            outputFiles.add( scriptOutputFilePath );
                        }
                    }
                    return outputFiles;
                }

            }
        } catch ( Exception e ) {
            log.error( PARSER_FILE_CREATION_FAILED, e );
            if ( masterJobId != null ) {
                String errorMsg = PARSER_FILE_CREATION_FAILED + e.getMessage();
                updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
            }
            throw new SusException( PARSER_FILE_CREATION_FAILED, e );
        }
        return null;
    }

    /**
     * Update parsed file in job directory.
     *
     * @param userUid
     *         the user uid
     * @param jobDirPathStaging
     *         the job dir path staging
     * @param selectedFinal
     *         the selected final
     * @param parserPath
     *         the parser path
     * @param scriptOutputFileName
     *         the script output file name
     * @param sharedDirectory
     *         the shared directory
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static String updateParsedFileInJobDirectory( String userUid, String jobDirPathStaging, ParserDTO selectedFinal,
            String parserPath, String scriptOutputFileName, String sharedDirectory ) throws IOException {
        String scriptInputFile;
        String parserTemplateFile = createParserTemplateFileInJobDirectory( userUid, jobDirPathStaging, selectedFinal );
        scriptInputFile = sharedDirectory + File.separator + scriptOutputFileName;
        String scriptOutputFilePath = jobDirPathStaging + File.separator + scriptOutputFileName;

        String updatePYFile = PropertiesManager.getParserPathsByKeyAndIdentifierKey( PropertiesManager.getParserKeyByPath( parserPath ),
                "replace" );
        PythonUtils.callParserUpdatePythonFile( userUid, scriptInputFile, scriptOutputFilePath, updatePYFile, parserTemplateFile );
        return scriptOutputFilePath;
    }

    /**
     * Creates the parser template file in job directory.
     *
     * @param userUid
     *         the user uid
     * @param jobDirPathStaging
     *         the job dir path staging
     * @param selectedFinal
     *         the selected final
     *
     * @return the string
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private static String createParserTemplateFileInJobDirectory( String userUid, String jobDirPathStaging, ParserDTO selectedFinal )
            throws IOException {
        String parserTemplateFile = jobDirPathStaging + File.separator + ConstantsString.PARSER_SELECTED_FILE_NAME;

        LinuxUtils.createFile( userUid, parserTemplateFile );
        LinuxUtils.writeFile( userUid, parserTemplateFile, JsonUtils.toJson( selectedFinal ) );

        return parserTemplateFile;
    }

    /**
     * Prepare selected file in staging for cb 2 parser object string.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     * @param masterJobId
     *         the master job id
     * @param selectionObject
     *         the selection object
     * @param sharedDirectory
     *         the shared directory
     * @param jobDirPathStaging
     *         the job dir path staging
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    private String prepareSelectedFileInStagingForCB2ParserObject( EntityManager entityManager, String userUid, UUID masterJobId,
            String selectionObject, String sharedDirectory, String jobDirPathStaging ) throws IOException {
        List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( selectionObject );
        BmwCaeBenchEntity cb2ObjectEntity;
        if ( cb2OidList != null && !cb2OidList.isEmpty() ) {

            String cb2DesPath = PropertiesManager.getDefaultServerTempPath();
            String cb2OID = cb2OidList.get( 0 );
            cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( cb2OID ) );
            if ( cb2ObjectEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTED_OBJECT_NOT_FOUND.getKey() ) );
            }

            // download file
            ProcessResult commandResult = PythonUtils.downloadCB2ObjectFileByCB2Oid( userUid, cb2ObjectEntity.getOid(), cb2DesPath,
                    cb2ObjectEntity.getBmwCaeDataType() );
            if ( commandResult.getExitValue() == 2 ) {
                log.error( commandResult.getErrorStreamString() );
                if ( masterJobId != null ) {
                    String errorMsg = ERROR_PARSING_CB2_FILE + commandResult.getErrorStreamString();
                    updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
                }
                throw new SusException( "File not found on cb2 for " + cb2ObjectEntity.getName() );
            } else if ( commandResult.getExitValue() != 0 ) {
                log.error( commandResult.getErrorStreamString() );
                if ( masterJobId != null ) {
                    String errorMsg = ERROR_PARSING_CB2_FILE + commandResult.getErrorStreamString();
                    updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
                }
                throw new SusException( "ERROR downloading CB2 File" + cb2ObjectEntity.getName() );
            }
            if ( commandResult.getErrorStreamString() != null && !commandResult.getErrorStreamString().isEmpty() ) {
                log.warn( commandResult.getErrorStreamString() );
            }
            // copy downloaded file to shared path.
            File fileInSharedDirectory = new File( sharedDirectory + File.separator + cb2ObjectEntity.getName() );
            if ( Files.notExists( fileInSharedDirectory.toPath() ) ) {
                if ( Files.notExists( Path.of( sharedDirectory ) ) ) {
                    LinuxUtils.createDirectory( userUid, sharedDirectory );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( userUid, cb2DesPath + File.separator + cb2ObjectEntity.getName(),
                        sharedDirectory );
            }
            // copy file from shared directory to job directory
            if ( Files.exists( fileInSharedDirectory.toPath() ) ) {
                if ( Files.notExists( Path.of( jobDirPathStaging ) ) ) {
                    LinuxUtils.createDirectory( userUid, jobDirPathStaging );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( userUid, fileInSharedDirectory.getAbsolutePath(), sharedDirectory );
            }
            return cb2ObjectEntity.getName();
        }
        return null;
    }

    /**
     * Prepare selected file in stagin for server parser object string.
     *
     * @param userUid
     *         the user uid
     * @param parserEntity
     *         the parser entity
     * @param selectionObject
     *         the selection object
     * @param sharedDirectory
     *         the shared directory
     * @param jobDirPathStaging
     *         the job dir path staging
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    private String prepareSelectedFileInStaginForServerParserObject( String userUid, ParserEntity parserEntity, String selectionObject,
            String sharedDirectory, String jobDirPathStaging ) throws IOException {
        File selectedFile;
        Map< String, String > serverFileSelectedMap = parserManager.getServerFileSelectionObjectByJson(
                parserEntity.getFormJsonAsString() );
        if ( null != serverFileSelectedMap && serverFileSelectedMap.containsKey( TITLE ) ) {
            selectedFile = new File( selectionObject );

            File fileInSharedDirectory = new File( sharedDirectory + File.separator + selectedFile.getName() );
            // copy from vault to shared path. it will only run for 1st child
            if ( Files.notExists( fileInSharedDirectory.toPath() ) ) {
                if ( Files.notExists( Path.of( sharedDirectory ) ) ) {
                    LinuxUtils.createDirectory( userUid, sharedDirectory );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( userUid, selectedFile.getAbsolutePath(), sharedDirectory );
            }
            // copy From shared directory to job dir for update
            if ( fileInSharedDirectory.exists() ) {
                if ( Files.notExists( Path.of( jobDirPathStaging ) ) ) {
                    LinuxUtils.createDirectory( userUid, jobDirPathStaging );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( userUid, fileInSharedDirectory.getAbsolutePath(), sharedDirectory );
            }
        } else {
            ExceptionLogger.logException( new SusException( "parser selection not found for server file" ), getClass() );
            throw new SusException( "parser selection not found for server file" );
        }
        return selectedFile.getName();
    }

    /**
     * Prepare selected file in staging for internal or local parser object string.
     *
     * @param entityManager
     *         the entity manager
     * @param userUid
     *         the user uid
     * @param jobDirPathStaging
     *         the job dir path staging
     * @param parserType
     *         the parser type
     * @param selectionObject
     *         the selection object
     * @param sharedDirectory
     *         the shared directory
     *
     * @return the string
     *
     * @throws IOException
     *         the io exception
     */
    private String prepareSelectedFileInStagingForInternalOrLocalParserObject( EntityManager entityManager, String userUid,
            String jobDirPathStaging, String parserType, String selectionObject, String sharedDirectory ) throws IOException {
        DocumentEntity selectedDocument = parserManager.getSelectedInternalObjectFilePath( entityManager, parserType, selectionObject );
        if ( selectedDocument != null && null != selectedDocument.getFileName() && null != selectedDocument.getFilePath() ) {
            String vaultFilePath = PropertiesManager.getVaultPath() + selectedDocument.getFilePath();

            File fileInSharedDirectory = new File( sharedDirectory + File.separator + selectedDocument.getFileName() );
            // copy from vault to shared path. it will only run for 1st child
            if ( Files.notExists( fileInSharedDirectory.toPath() ) ) {
                copyParserVaultFileToSharedPath( userUid, selectedDocument, vaultFilePath, fileInSharedDirectory.getParentFile() );
            }
            // copy From shared directory to job dir for update
            if ( fileInSharedDirectory.exists() ) {
                if ( Files.notExists( Path.of( jobDirPathStaging ) ) ) {
                    LinuxUtils.createDirectory( userUid, jobDirPathStaging );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( userUid, fileInSharedDirectory.getAbsolutePath(), jobDirPathStaging );
            }
        } else {
            ExceptionLogger.logException( new SusException( "parser selection not found" ), getClass() );
            throw new SusException( "parser selection not found" );
        }
        return selectedDocument.getFileName();
    }

    /**
     * Copy parser vault file to shared path.
     *
     * @param userUid
     *         the user uid
     * @param selectedDocument
     *         the selected document
     * @param vaultFilePath
     *         the vault file path
     * @param destinationFileObj
     *         the destination file obj
     */
    private void copyParserVaultFileToSharedPath( String userUid, DocumentEntity selectedDocument, String vaultFilePath,
            File destinationFileObj ) {
        try ( InputStream decryptedStreamFromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                new File( vaultFilePath ), prepareEncryptionDecryptionDTO( selectedDocument.getEncryptionDecryption() ) ) ) {
            // copy in temp first to avoid permission errors and enforce impersonation
            File tempFile = new File( PropertiesManager.getDefaultServerTempPath() + File.separator + selectedDocument.getFileName() );
            Files.copy( decryptedStreamFromVault, tempFile.toPath() );
            FileUtils.setGlobalAllFilePermissions( tempFile );
            // copy from temp to shared with with impersonation check
            if ( Files.notExists( Path.of( destinationFileObj.getAbsolutePath() ) ) ) {
                LinuxUtils.createDirectory( userUid, destinationFileObj.getAbsolutePath() );
            }
            LinuxUtils.copyFileFromSrcPathToDestPath( userUid, tempFile.getAbsolutePath(), destinationFileObj.getAbsolutePath() );

            // delete temp file
            Files.deleteIfExists( tempFile.toPath() );

        } catch ( IOException e ) {
            log.error( "could not copy vault file to shared path: " + destinationFileObj.getName(), e );
            throw new SusException( "could not copy vault file to shared path: " + destinationFileObj.getName(), e );
        }
    }

    /**
     * Update job and status.
     *
     * @param masterJobId
     *         the master job id
     * @param errotMsg
     *         the errot msg
     */
    @Override
    public void updateMasterJobAndStatus( EntityManager entityManager, UUID masterJobId, String errotMsg ) {
        if ( masterJobId != null ) {
            JobEntity jobEntity = jobDao.getLatestNonDeletedObjectById( entityManager, masterJobId );
            List< LogRecord > masterJobLogs = convertFromByteArrayToLogRecordList( jobEntity.getLog() );
            masterJobLogs.add( new LogRecord( "ERROR", errotMsg, new Date() ) );
            jobEntity.setLog( convertFromLogRecordListToByteArray( masterJobLogs ) );
            jobEntity.setStatus( WorkflowStatus.FAILED.getKey() );
            jobDao.saveOrUpdate( entityManager, jobEntity );
        }
    }

    /**
     * It converts from log record list to byte array.
     *
     * @param logRecords
     *         log records of a job
     *
     * @return the serialized logs from the records list passed, an empty byte array in case there are no log recorde.
     */
    private byte[] convertFromLogRecordListToByteArray( List< LogRecord > logRecords ) {
        byte[] result = null;
        if ( logRecords != null ) {

            try ( ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream( bos ) ) {

                oos.writeObject( logRecords );
                result = bos.toByteArray();
            } catch ( final IOException e ) {
                log.error( e.getMessage(), e );
                result = null;
            }

        }
        return result;
    }

    /**
     * It converts from byte array to log record list.
     *
     * @param bytes
     *         serialized data
     *
     * @return list of logrecords of a job
     */
    @SuppressWarnings( "unchecked" )
    private List< LogRecord > convertFromByteArrayToLogRecordList( byte[] bytes ) {
        List< LogRecord > logger = null;
        if ( bytes != null ) {
            try ( ByteArrayInputStream bis = new ByteArrayInputStream( bytes ); ObjectInputStream ois = new ObjectInputStream( bis ) ) {
                logger = ( List< LogRecord > ) ois.readObject();

            } catch ( final ClassNotFoundException | IOException e ) {
                log.error( "Error : " + e.getMessage(), e );
            }
            if ( logger != null ) {
                Collections.reverse( logger );
            }
        }
        return logger;

    }

    /**
     * get List Of ParserIds From Workflow Definition
     *
     * @param definitionDTO
     *         the definitionDTO
     *
     * @return the List of parser object Ids
     */
    public static List< UUID > getListOfParserIdsFromWorkflowDefinition( WorkflowDefinitionDTO definitionDTO ) {
        List< UUID > parserIdList = new ArrayList<>();
        if ( ( definitionDTO.getElements() != null ) ) {
            definitionDTO.getElements().getNodes().forEach( element -> element.getData().getFields().stream()
                    .filter( field -> field.getType().equals( FieldTypes.OBJECT_PARSER.getType() ) )
                    .forEach( field -> parserIdList.add( UUID.fromString( ( String ) field.getValue() ) ) ) );
        }
        return parserIdList;
    }

    /**
     * Gets the CB 2 object selected from job parameters.
     *
     * @param def
     *         the def
     *
     * @return the CB 2 object selected from job parameters
     */
    public static Set< String > getCB2ObjectSelectedFromJobParameters( WorkflowDefinitionDTO def ) {
        Set< String > allCB2FieldsSelection = new HashSet<>();
        if ( ( def.getElements() == null ) || CollectionUtil.isEmpty( def.getElements().getNodes() ) ) {
            return allCB2FieldsSelection;
        }
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            WorkflowElement newWorkflowElement = JsonUtils.jsonToObject( JsonUtils.toJson( workflowElement ), WorkflowElement.class );
            final UserWFElement element = newWorkflowElement.getData();
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( FieldTypes.CB2_OBJECTS.getType() ) && field.getValue() != null ) {
                    String selectionId = ( String ) field.getValue();
                    if ( !selectionId.isEmpty() ) {
                        allCB2FieldsSelection.add( selectionId );
                    }
                }
            }
        }
        return allCB2FieldsSelection;
    }

    /**
     * Gets the SUS object selected from job parameters for assemble and simulate.
     *
     * @param def
     *         the def
     *
     * @return the SUS object selected from job parameters for assemble and simulate
     */
    public static Map< String, Map< String, String > > getSUSObjectSelectedFromJobParametersForAssembleAndSimulate(
            WorkflowDefinitionDTO def ) {

        Map< String, Map< String, String > > objMap = new HashMap<>();

        if ( ( def.getElements() == null ) || CollectionUtil.isEmpty( def.getElements().getNodes() ) ) {
            return objMap;
        }
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            WorkflowElement newWorkflowElement = JsonUtils.jsonToObject( JsonUtils.toJson( workflowElement ), WorkflowElement.class );

            final UserWFElement element = newWorkflowElement.getData();

            if ( element.getKey().equals( ElementKeys.WFE_ASSEMBLE_AND_SIMULATE.getKey() ) ) {
                final List< Field< ? > > elementFields = element.getFields();
                for ( final Field< ? > field : elementFields ) {
                    if ( field.getType().equals( FieldTypes.CB2_INCLUDE.getType() ) && field.getValue() != null ) {

                        try {
                            List< Object > includes = new ArrayList<>();
                            Map< String, Object > jsonObjMain = new HashMap<>();
                            jsonObjMain.put( "includes", field.getValue() );
                            JSONParser parser = new JSONParser();
                            JSONObject obj4 = ( JSONObject ) parser.parse( JsonUtils.toJson( jsonObjMain ) );
                            includes = ( List< Object > ) JsonUtils.jsonToList( obj4.get( "includes" ).toString(), includes );
                            for ( Object Objectincludes : includes ) {

                                Map< String, String > mytypeMap = new HashMap<>();

                                JSONObject obj2 = ( JSONObject ) parser.parse( JsonUtils.toJson( Objectincludes ) );
                                if ( obj2.containsKey( "selection_type" ) ) {

                                    if ( obj2.get( "selection_type" ).toString().equalsIgnoreCase( "cb2-object" ) && obj2.containsKey(
                                            "submodel_value" ) ) {

                                        mytypeMap.put( obj2.get( "name" ).toString(), obj2.get( "submodel_value" ).toString() );
                                        objMap.put( "cb2-object", mytypeMap );

                                    } else if ( obj2.get( "selection_type" ).toString().equalsIgnoreCase( "sus-object" )
                                            && obj2.containsKey( "submodel_value" ) ) {

                                        mytypeMap.put( obj2.get( "name" ).toString(), obj2.get( "submodel_value" ).toString() );
                                        objMap.put( "sus-object", mytypeMap );
                                    }
                                }
                            }
                        } catch ( ParseException e ) {
                            log.error( "Error extracting cb2 include files for downlaod : " + e.getMessage(), e );
                        }
                    }
                }
            }
        }
        return objMap;
    }

    /**
     * Gets the CB 2 object selected from job parameters for assemble and simulate.
     *
     * @param def
     *         the def
     *
     * @return the CB 2 object selected from job parameters for assemble and simulate
     */
    public static Map< String, String > getCB2ObjectSelectedFromJobParametersForAssembleAndSimulate( WorkflowDefinitionDTO def ) {
        Map< String, String > cb2Map = new HashMap<>();

        if ( ( def.getElements() == null ) || CollectionUtil.isEmpty( def.getElements().getNodes() ) ) {
            return cb2Map;
        }
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            WorkflowElement newWorkflowElement = JsonUtils.jsonToObject( JsonUtils.toJson( workflowElement ), WorkflowElement.class );

            final UserWFElement element = newWorkflowElement.getData();

            if ( element.getKey().equals( ElementKeys.WFE_ASSEMBLE_AND_SIMULATE.getKey() ) ) {
                final List< Field< ? > > elementFields = element.getFields();
                for ( final Field< ? > field : elementFields ) {
                    if ( field.getType().equals( FieldTypes.CB2_INCLUDE.getType() ) && field.getValue() != null ) {

                        try {
                            List< Object > includes = new ArrayList<>();
                            Map< String, Object > jsonObjMain = new HashMap<>();
                            jsonObjMain.put( "includes", field.getValue() );
                            JSONParser parser = new JSONParser();
                            JSONObject obj4 = ( JSONObject ) parser.parse( JsonUtils.toJson( jsonObjMain ) );
                            includes = ( List< Object > ) JsonUtils.jsonToList( obj4.get( "includes" ).toString(), includes );
                            for ( Object Objectincludes : includes ) {
                                JSONObject obj2 = ( JSONObject ) parser.parse( JsonUtils.toJson( Objectincludes ) );
                                if ( obj2.containsKey( "selection_type" ) && obj2.get( "selection_type" ).toString()
                                        .equalsIgnoreCase( "cb2-object" ) && obj2.containsKey( "submodel_value" ) ) {

                                    cb2Map.put( obj2.get( "name" ).toString(), obj2.get( "submodel_value" ).toString() );
                                }
                            }
                        } catch ( ParseException e ) {
                            log.debug( "Error extracting cb2 include files for downlaod : " + e.getMessage(), e );
                        }
                    }
                }
            }
        }
        return cb2Map;
    }

    /**
     * Creates the objective varibale file in staging.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param masterJobId
     *         the master job id
     *
     * @return the string
     */
    private List< String > createDesignVariableFilesInStaging( EntityManager entityManager, UserEntity user, JobParameters jobParameters,
            Map< String, Object > designSummary, UUID masterJobId ) {
        try {
            if ( designSummary != null ) {
                final String bmwSuggestion = File.separator + jobParameters.getName() + "_" + jobParameters.getJobInteger();

                File file = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + bmwSuggestion );

                LinuxUtils.createDirectory( user.getUserUid(), file.getAbsolutePath() );

                List< ScanFileDTO > scanList = new ArrayList<>();
                List< TemplateFileDTO > templateList = new ArrayList<>();
                // Getting workflow elements from job parameter
                final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                        jobParameters.getWorkflow().prepareDefinition() );
                final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.prepareWorkflowElements( jobParamDef );

                // get paths of all local files used in workflow
                getPathsOfAllFilesUsedInWorkflow( entityManager, user, scanList, templateList, jobWFElements );
                // return null if both templateList : scanList values is empty

                List< String > filePaths = new ArrayList<>();
                if ( !scanList.isEmpty() ) {
                    createDesignVariableFilesInStagingByScanList( user, jobParameters, designSummary, bmwSuggestion, scanList, filePaths );

                } else if ( !templateList.isEmpty() ) {
                    createDesignVariableFilesInStagingByTemplateList( user, jobParameters, designSummary, bmwSuggestion, templateList,
                            filePaths );
                }
                return filePaths;
            }
        } catch ( Exception e ) {
            log.error( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL, e );
            if ( masterJobId != null ) {
                String errorMsg = DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + e.getMessage();
                updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
            }
            throw new SusException( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + e.getMessage() );
        }
        return null;
    }

    /**
     * Creates the design variable files in staging by template list.
     *
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param bmwSuggestion
     *         the bmw suggestion
     * @param templateList
     *         the template list
     * @param filePaths
     *         the file paths
     */
    private void createDesignVariableFilesInStagingByTemplateList( UserEntity user, JobParameters jobParameters,
            Map< String, Object > designSummary, String bmwSuggestion, List< TemplateFileDTO > templateList, List< String > filePaths ) {
        try {
            String templatePath;
            templatePath = templateList.get( FIRST_INDEX ).getFile();

            // prepare files path for replacements
            File temp = new File( OSValidator.convertPathToRelitiveOS( templatePath ) );
            File fileObjectiveVariables = new File( OSValidator.convertPathToRelitiveOS(
                    PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + bmwSuggestion + File.separator
                            + temp.getName() ) );
            String originalDesignFilePath = new File(
                    PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow()
                            .getId() ).getPath() + File.separator + FILES + File.separator + temp.getName();

            File originalDesignFil = new File( originalDesignFilePath );

            // run scheme from web Case: copy file from shared path in Files Dir
            if ( !originalDesignFil.exists() ) {
                if ( Files.notExists( Path.of( originalDesignFil.getParent() ) ) ) {
                    LinuxUtils.createDirectory( user.getUserUid(), originalDesignFil.getParent() );
                }
                LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), temp.getAbsolutePath(), originalDesignFil.getParent() );
            }

            if ( PropertiesManager.isImpersonated() ) {

                File variableTempFile = new File(
                        PropertiesManager.getDefaultServerTempPath() + File.separator + bmwSuggestion + temp.getName() );
                StringBuilder dataAfterReplacement = WorkflowDefinitionUtil.prepareTemplateFileDataForStaging( originalDesignFilePath,
                        templateList, designSummary );

                // write dataAFterReplacement to temp file
                writeDataToTempFile( variableTempFile, dataAfterReplacement );

                // copy from temp file to working Directory with impersonation
                LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), variableTempFile.getAbsolutePath(),
                        fileObjectiveVariables.getAbsolutePath() );

                // delete temp file
                if ( variableTempFile.exists() ) {
                    variableTempFile.delete();
                }

            } else {
                fileObjectiveVariables.createNewFile();
                WorkflowDefinitionUtil.prepareTemplateFileInStagingInJobContainer( templateList, designSummary,
                        fileObjectiveVariables.getPath() );
            }
            filePaths.add( fileObjectiveVariables.getPath() );
        } catch ( IOException e ) {
            log.error( DESIGN_VARIABLE_FILE_WRITE_FAIL + "Template File", e );
            throw new SusException( DESIGN_VARIABLE_FILE_WRITE_FAIL + "Template File", e );
        } catch ( Exception e ) {
            log.error( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + "Template File", e );
            throw new SusException( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + "Template File", e );
        }
    }

    /**
     * Write data to temp file.
     *
     * @param variableTempFile
     *         the variable temp file
     * @param dataAfterReplacement
     *         the data after replacement
     */
    private static void writeDataToTempFile( File variableTempFile, StringBuilder dataAfterReplacement ) {
        try {
            Writer targetFileWriter = new FileWriter( variableTempFile.getAbsolutePath() );
            targetFileWriter.write( dataAfterReplacement.toString() );
            targetFileWriter.close();
            FileUtils.setGlobalAllFilePermissions( variableTempFile );
        } catch ( IOException e ) {
            log.error( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), variableTempFile.getAbsolutePath() ),
                    e );
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_FILE_AT.getKey(), variableTempFile.getAbsolutePath() ), e );
        }
    }

    /**
     * Creates the design variable files in staging by scan list.
     *
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     * @param bmwSuggestion
     *         the bmw suggestion
     * @param scanList
     *         the scan list
     * @param filePaths
     *         the file paths
     */
    private void createDesignVariableFilesInStagingByScanList( UserEntity user, JobParameters jobParameters,
            Map< String, Object > designSummary, String bmwSuggestion, List< ScanFileDTO > scanList, List< String > filePaths ) {
        try {
            String scanPath;
            Map< Integer, ObjectVariableDTO > replacementMap = new HashMap<>();
            scanList.stream().forEach( scanFileDTO -> scanFileDTO.getVariables()
                    .forEach( variable -> replacementMap.put( Integer.parseInt( variable.getHighlight().getLineNumber() ), variable ) ) );
            for ( ScanFileDTO scan : scanList ) {
                scanPath = scan.getFile();

                // prepare files path for replacements
                File temp = new File( OSValidator.convertPathToRelitiveOS( scanPath ) );
                File fileObjectiveVariables = new File( OSValidator.convertPathToRelitiveOS(
                        PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + bmwSuggestion + File.separator
                                + temp.getName() ) );
                String originalDesignFilePath = new File(
                        PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow()
                                .getId() ).getPath() + File.separator + FILES + File.separator + temp.getName();

                File originalDesignFil = new File( originalDesignFilePath );

                // run scheme from web Case: copy file from shared path in Files Dir
                if ( !originalDesignFil.exists() ) {
                    if ( Files.notExists( Path.of( originalDesignFil.getParent() ) ) ) {
                        LinuxUtils.createDirectory( user.getUserUid(), originalDesignFil.getParent() );
                    }
                    LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), temp.getAbsolutePath(), originalDesignFil.getParent() );
                }

                if ( PropertiesManager.isImpersonated() ) {

                    File variableTempFile = new File(
                            PropertiesManager.getDefaultServerTempPath() + File.separator + bmwSuggestion + temp.getName() );
                    StringBuilder dataAfterReplacement = WorkflowDefinitionUtil.prepareRegexFileDataForStaging( originalDesignFilePath,
                            designSummary, replacementMap );

                    // write to dataFterReplacement to temp file
                    writeDataToTempFile( variableTempFile, dataAfterReplacement );

                    // copy from temp file to working Directory with impersonation
                    LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), variableTempFile.getAbsolutePath(),
                            fileObjectiveVariables.getAbsolutePath() );

                    // delete temp file
                    if ( variableTempFile.exists() ) {
                        variableTempFile.delete();
                    }

                } else {
                    fileObjectiveVariables.createNewFile();
                    WorkflowDefinitionUtil.prepareRegexFileInStagingInJobContainer( scanList, designSummary,
                            fileObjectiveVariables.getPath() );
                }
                filePaths.add( fileObjectiveVariables.getPath() );
            }
        } catch ( IOException e ) {
            log.error( DESIGN_VARIABLE_FILE_WRITE_FAIL + "Scan File", e );
            throw new SusException( DESIGN_VARIABLE_FILE_WRITE_FAIL + "Scan File", e );
        } catch ( Exception e ) {
            log.error( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + "Scan File", e );
            throw new SusException( DESIGN_VARIABLE_FILE_DIR_WRITE_FAIL + "Scan File", e );
        }

    }

    /**
     * Gets the paths of all files used in workflow.
     *
     * @param entityManager
     *         the entity manager
     * @param user
     *         the user
     * @param scanList
     *         the scan list
     * @param templateList
     *         the template list
     * @param jobWFElements
     *         the job WF elements
     */
    private void getPathsOfAllFilesUsedInWorkflow( EntityManager entityManager, UserEntity user, List< ScanFileDTO > scanList,
            List< TemplateFileDTO > templateList, List< UserWFElement > jobWFElements ) {
        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() ) && field.getTemplateType()
                        .equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                    scanList.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class ) );
                }
                if ( field.getType().equals( FieldTypes.REGEX_SCAN_SERVER.getType() ) && field.getTemplateType()
                        .equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                    UUID selectionId = UUID.fromString( ( String ) field.getValue() );
                    List< RegexEntity > regexEntities = regexManager.getRegexDAO().getRegexListBySelectionId( entityManager, selectionId );
                    if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                        scanList.add(
                                prepareScanFileDTOFromRegexServerObject( entityManager, user.getUserUid(), selectionId, regexEntities ) );
                    }
                }
                if ( field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() ) && field.getTemplateType()
                        .equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                    templateList.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), TemplateFileDTO.class ) );
                }
                if ( field.getType().equals( FieldTypes.TEMPLATE_SCAN_SERVER.getType() ) && field.getTemplateType()
                        .equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                    UUID selectionId = UUID.fromString( ( String ) field.getValue() );
                    List< TemplateEntity > templateEntities = templateManager.getTemplateDAO()
                            .getTemplateListBySelectionId( entityManager, selectionId );
                    if ( CollectionUtils.isNotEmpty( templateEntities ) ) {
                        scanList.add( prepareScanFileDTOFromTemplateServerObject( entityManager, user.getUserUid(), selectionId,
                                templateEntities ) );
                    }
                }
            }
        }
    }

    /**
     * Prepare scan file DTO from regex server object.
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     * @param selectionId
     *         the selection id
     * @param regexEntities
     *         the regex entities
     *
     * @return the scan file DTO
     */
    private ScanFileDTO prepareScanFileDTOFromRegexServerObject( EntityManager entityManager, String uid, UUID selectionId,
            List< RegexEntity > regexEntities ) {
        String fullPath = getFullPath( entityManager, uid, selectionId, REGEX_OBJECT );
        List< ObjectVariableDTO > objectVariableDTOs = new ArrayList<>();
        for ( RegexEntity regexEntity : regexEntities ) {
            objectVariableDTOs.add( prepareObjectVariableDTO( regexEntity ) );
        }
        ScanFileDTO scanFileDTO = new ScanFileDTO();
        scanFileDTO.setFile( fullPath );
        scanFileDTO.setVariables( objectVariableDTOs );
        return scanFileDTO;
    }

    /**
     * Prepare scan file DTO from template server object.
     *
     * @param entityManager
     *         the entity manager
     * @param uid
     *         the uid
     * @param selectionId
     *         the selection id
     * @param templateEntities
     *         the template entities
     *
     * @return the scan file DTO
     */
    private ScanFileDTO prepareScanFileDTOFromTemplateServerObject( EntityManager entityManager, String uid, UUID selectionId,
            List< TemplateEntity > templateEntities ) {
        String fullPath = getFullPath( entityManager, uid, selectionId, REGEX_OBJECT );
        List< ObjectVariableDTO > objectVariableDTOs = new ArrayList<>();
        for ( TemplateEntity templateEntity : templateEntities ) {
            objectVariableDTOs.add( prepareObjectVariableDTOFromTemplateServerObject( templateEntity ) );
        }
        ScanFileDTO scanFileDTO = new ScanFileDTO();
        scanFileDTO.setFile( fullPath );
        scanFileDTO.setVariables( objectVariableDTOs );
        return scanFileDTO;
    }

    @Override
    public ScanFileDTO getTemplateScanFileBySelectionId( String uid, UUID selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TemplateEntity > templateEntities = templateManager.getTemplateDAO()
                    .getTemplateListBySelectionId( entityManager, selectionId );
            return prepareScanFileDTOFromTemplateServerObject( entityManager, uid, selectionId, templateEntities );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the full path.
     *
     * @param entityManager
     *         the entity manager
     * @param userUID
     *         the user UID
     * @param id
     *         the id
     * @param type
     *         the type
     *
     * @return the full path
     */
    private String getFullPath( EntityManager entityManager, String userUID, UUID id, String type ) {
        com.fasterxml.jackson.databind.JsonNode selectedJson = getJson( entityManager, id );
        com.fasterxml.jackson.databind.JsonNode regexobjectNode = selectedJson.get( type );
        String fullPath = null;
        com.fasterxml.jackson.databind.JsonNode selectedValue = null;
        if ( regexobjectNode != null ) {
            if ( regexobjectNode.has( VALUE ) ) {
                selectedValue = regexobjectNode.get( VALUE );
            }
            if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.LOCAL.getId() ) ) {
                fullPath = getFilePath( entityManager, UUID.fromString( selectedValue.get( ID ).asText() ) );
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.INTERNAL.getId() ) ) {
                List< UUID > selectedItemsList = selectionManager.getSelectedIdsListBySelectionId( entityManager, selectedValue.asText() );
                if ( selectedItemsList != null && !selectedItemsList.isEmpty() ) {
                    fullPath = getFilePath( entityManager, selectedItemsList.get( 0 ) );
                }
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.SERVER.getId() ) ) {
                if ( selectedValue.isArray() ) {
                    for ( com.fasterxml.jackson.databind.JsonNode jsonNode : selectedValue ) {
                        fullPath = jsonNode.get( FULL_PATH ).asText();
                    }
                }
            } else if ( regexobjectNode.get( TYPE ).asText().equalsIgnoreCase( VariableDropDownEnum.CB2.getId() ) ) {
                List< String > cb2OidList = selectionManager.getSelectedIdsStringListBySelectionId( entityManager, selectedValue.asText() );
                if ( cb2OidList != null && !cb2OidList.isEmpty() ) {
                    String cb2OID = cb2OidList.get( 0 );
                    BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( cb2OID ) );
                    fullPath = downloadCB2FileAndReturnPath( userUID, cb2ObjectEntity );
                }
            }
        }
        return fullPath;
    }

    /**
     * Gets the file path.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the file path
     */
    private String getFilePath( EntityManager entityManager, UUID id ) {
        DocumentEntity documentEntity = getDocumentObjectByObjectId( entityManager, id );
        if ( documentEntity == null ) {
            throw new SusException( "Object not available" );
        }
        String selectedFilePath = PropertiesManager.getVaultPath() + documentEntity.getFilePath();
        File selectedFileObj = new File(
                PropertiesManager.getDefaultServerTempPath() + File.separator + documentEntity.getFileName().replaceAll( "\\s", "_" ) );
        try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                new File( selectedFilePath ), prepareEncryptionDecryptionDTO( documentEntity.getEncryptionDecryption() ) ) ) {
            Files.copy( decritedStreamDromVault, selectedFileObj.toPath(), StandardCopyOption.REPLACE_EXISTING );
            FileUtils.setGlobalAllFilePermissions( selectedFileObj );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return selectedFileObj.getAbsolutePath();
    }

    /**
     * Prepare object variable DTO.
     *
     * @param regexEntity
     *         the regex entity
     *
     * @return the object variable DTO
     */
    private ObjectVariableDTO prepareObjectVariableDTO( RegexEntity regexEntity ) {
        ObjectVariableDTO objectVariableDTO = null;
        if ( regexEntity != null ) {
            objectVariableDTO = new ObjectVariableDTO();
            objectVariableDTO.setId( regexEntity.getId() );
            objectVariableDTO.setVariableName( regexEntity.getVariableName() );
            objectVariableDTO.setLineRegex( regexEntity.getLineRegex() );
            objectVariableDTO.setLineMatch( regexEntity.getLineMatch() );
            objectVariableDTO.setLineOffset( regexEntity.getLineOffset() );
            objectVariableDTO.setVariableRegex( regexEntity.getVariableRegex() );
            objectVariableDTO.setVariableMatch( regexEntity.getVariableMatch() );
            objectVariableDTO.setVariableGroup( regexEntity.getVariableGroup() );

            ScanResponseDTO ScanResponseDTO = new ScanResponseDTO();
            ScanResponseDTO.setLineNumber( String.valueOf( Integer.parseInt( regexEntity.getScannedLine() ) - 1 ) );
            ScanResponseDTO.setMatch( regexEntity.getScannedValue() );
            ScanResponseDTO.setStart( regexEntity.getStart() );
            ScanResponseDTO.setEnd( regexEntity.getEnd() );
            objectVariableDTO.setHighlight( ScanResponseDTO );
        }
        return objectVariableDTO;
    }

    /**
     * Prepare object variable DTO from template server object.
     *
     * @param templateEntity
     *         the template entity
     *
     * @return the object variable DTO
     */
    private ObjectVariableDTO prepareObjectVariableDTOFromTemplateServerObject( TemplateEntity templateEntity ) {
        ObjectVariableDTO objectVariableDTO = null;
        if ( templateEntity != null ) {
            objectVariableDTO = new ObjectVariableDTO();
            objectVariableDTO.setId( templateEntity.getId() );
            objectVariableDTO.setVariableName( templateEntity.getVariableName() );

            ScanResponseDTO ScanResponseDTO = new ScanResponseDTO();
            ScanResponseDTO.setLineNumber( String.valueOf( Integer.parseInt( templateEntity.getLineNumber() ) - 1 ) );
            ScanResponseDTO.setMatch( templateEntity.getMatch() );
            ScanResponseDTO.setStart( templateEntity.getStart() );
            ScanResponseDTO.setEnd( templateEntity.getEnd() );
            objectVariableDTO.setHighlight( ScanResponseDTO );
        }
        return objectVariableDTO;
    }

    /**
     * Copy assemble and simulate local files in staging.
     *
     * @param payload
     *         the payload
     * @param userId
     *         the user id
     *
     * @return the string
     */
    @Override
    public String copyAssembleAndSimulateFilesInStaging( String payload, UUID userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = userCommonManager.getUserById( entityManager, userId );
            JSONParser parser = new JSONParser();
            JSONObject subModels = ( JSONObject ) parser.parse( payload );

            String jobWorkingDir = ( String ) subModels.get( "jobdir" );

            JSONArray includeList = ( JSONArray ) new JSONTokener( JsonUtils.toJson( subModels.get( "IncludeList" ) ) ).nextValue();

            for ( int i = 0; i < includeList.length(); i++ ) {
                org.json.JSONObject localSubmodel = includeList.getJSONObject( i );
                if ( localSubmodel.get( "selection_type" ).toString().equalsIgnoreCase( "local-file" ) ) {

                    org.json.JSONObject submodelValue = ( org.json.JSONObject ) localSubmodel.get( "submodel_value" );

                    LinuxUtils.createFile( user.getUserUid(), jobWorkingDir + submodelValue.get( "name" ) );
                    LinuxUtils.writeFile( user.getUserUid(), PropertiesManager.getVaultPath() + submodelValue.get( "path" ),
                            jobWorkingDir + submodelValue.get( "name" ) );

                    FileUtils.setGlobalAllFilePermissions( new File( jobWorkingDir + submodelValue.get( "name" ) ) );
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return "Files Copied to staging";

    }

    /**
     * Copy cb 2 files from vault to working directory.
     *
     * @param user
     *         the user
     * @param srcDestPaths
     *         the src dest paths
     */
    private static void copyCb2FilesFromVaultToWorkingDirectory( UserDTO user, Entry< String, String > srcDestPaths ) {
        try {
            LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), srcDestPaths.getKey(), srcDestPaths.getValue() );
            FileUtils.setGlobalAllFilePermissions( new File( srcDestPaths.getValue() ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Prepare files from shared path.
     *
     * @param user
     *         the user
     * @param temp
     *         the temp
     * @param originalDesignFil
     *         the original design fil
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void prepareFilesFromSharedPath( UserEntity user, File temp, File originalDesignFil ) throws IOException {
        File filesDir = new File( originalDesignFil.getParent() );
        LinuxUtils.createDirectory( user.getUserUid(), filesDir.getAbsolutePath() );
        LinuxUtils.copyFileFromSrcPathToDestPath( user.getUserUid(), temp.getAbsolutePath(), originalDesignFil.getAbsolutePath() );
    }

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
     */
    @Override
    public List< String > createDesignVariableFilesInStagingByWorkflowId( UUID userId, String wfId, String jobId,
            Map< String, Object > designSummary, String runsOn, UUID masterJobId, String jobname ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            WorkflowEntity wf = workflowDao.getLatestNonDeletedObjectById( entityManager, UUID.fromString( wfId ) );

            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, userId );
            LatestWorkFlowDTO latestWfDTO = prepareLatestWorkflowFromWorkflowDTO( prepareWorkflowDTO( entityManager, userId, wf ) );
            JobParameters jobParameters = new JobParametersImpl();
            jobParameters.setId( jobId );
            jobParameters.setWorkflow( latestWfDTO );
            jobParameters.setName( jobname );
            jobParameters.setJobInteger( jobManager.saveJobIds( entityManager, UUID.fromString( jobId ) ) );

            List< String > designVarFilePaths = createDesignVariableFilesInStaging( entityManager, userEntity, jobParameters, designSummary,
                    masterJobId );
            List< String > parserFilePaths = createParserDesignVariableFilesInStaging( entityManager, userEntity, jobParameters,
                    designSummary, masterJobId );
            String cb2ObjectFile = downloadCb2FilesInStaging( entityManager, userEntity, jobParameters, masterJobId );

            List< String > allFilePaths = new ArrayList<>();
            if ( cb2ObjectFile != null && !cb2ObjectFile.isEmpty() ) {
                allFilePaths.add( cb2ObjectFile );
            }
            if ( designVarFilePaths != null && !designVarFilePaths.isEmpty() ) {
                allFilePaths.addAll( designVarFilePaths );
            }
            if ( parserFilePaths != null && !parserFilePaths.isEmpty() ) {
                allFilePaths.addAll( parserFilePaths );
            }
            return allFilePaths;
        } catch ( Exception e ) {
            log.error( DESIGN_VARIABLE_FILE_WRITE_FAIL, e );
            if ( masterJobId != null ) {
                String errorMsg = DESIGN_VARIABLE_FILE_WRITE_FAIL + e.getMessage();
                updateMasterJobAndStatus( entityManager, masterJobId, errorMsg );
            }
            throw new SusException( DESIGN_VARIABLE_FILE_WRITE_FAIL + e.getMessage() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the working dir path.
     *
     * @param userUid
     *         the user uid
     * @param jobParameters
     *         the job parameters
     * @param locationDTO
     *         the location DTO
     */
    private void setWorkingDirPath( String userUid, JobParameters jobParameters, final LocationDTO locationDTO ) {
        if ( jobParameters.getJobType() != JobTypeEnums.VARIANT.getKey() ) {
            List< String > items = new ArrayList<>();
            items.add( PropertiesManager.getUserStagingPathFromStaging( userUid, locationDTO.getStaging() ) );
            EngineFile engineFil = new EngineFile();
            engineFil.setItems( items );
            engineFil.setPath( PropertiesManager.getUserStagingPathFromStaging( userUid, locationDTO.getStaging() ) + File.separator
                    + jobParameters.getName() + "_" + jobParameters.getJobInteger() );
            jobParameters.setWorkingDir( engineFil );
        }
    }

    /**
     * Prepare location headers.
     *
     * @param authToken
     *         the auth token
     *
     * @return the map
     */
    private Map< String, String > prepareLocationHeaders( String authToken ) {
        final Map< String, String > requestHeaders = new HashMap<>();

        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( "Authorization", authToken );

        return requestHeaders;
    }

    /**
     * Execute WF process on server.
     *
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     */
    @Override
    public void executeWFProcessOnServer( EntityManager entityManager, String userId, JobParameters jobParameters ) {
        /*
         * STEPS create java process through executorService copy jobParameters to a
         * file submit java process with command java -jar wfengine.jar -wf
         * <STAGING>/job_id/job_name.json -u userid
         */
        File jobFile = new File( "/tmp/script" );

        UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
        final File jobFileDir = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + "scripts" );
        jobFileDir.mkdirs();
        FileUtils.setGlobalAllFilePermissions( jobFileDir );
        try {
            jobFile = File.createTempFile( jobParameters.getName(), ".json", jobFileDir );
        } catch ( final IOException e1 ) {
            log.error( e1.getMessage(), e1 );
        }
        FileUtils.setGlobalAllFilePermissions( jobFile );
        jobParameters.getWorkingDir().setPath( PropertiesManager.getUserStagingPath( user.getUserUid() ) );
        try {
            new ObjectMapper().writeValue( jobFile, jobParameters );
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }

        executeSubProcess( entityManager, userId, jobFile );

    }

    /**
     * Execute sub process.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobFile
     *         the job file
     */
    private void executeSubProcess( EntityManager entityManager, String userId, File jobFile ) {
        final UserEntity user = userManager.getSimUser( entityManager, UUID.fromString( userId ) );
        if ( OSValidator.isUnix() ) {
            String[] command = prepareSubProcessExecutionCommandForUnix( user, jobFile );
            LinuxUtils.runCommand( command, ConstantsString.COMMAND_KARAF_LOGGING_ON, null );
        } else if ( OSValidator.isWindows() ) {
            final String[] command = { JAVA_PATH, "-jar", "-Dkaraf.base=\"" + System.getProperty( ConstantsKaraf.KARAF_HOME ) + "\"",
                    "\"" + PropertiesManager.getEnginePath() + File.separator + "suscore-simflow-wfengine.jar\"", "-wf",
                    "\"" + jobFile.getAbsolutePath() + "\"" };

            final WString nullW = null;
            final PROCESS_INFORMATION processInformation = new PROCESS_INFORMATION();
            final STARTUPINFO startupInfo = new STARTUPINFO();
            final boolean result = MoreAdvApi32.INSTANCE.CreateProcessWithLogonW( new WString( user.getUserUid() ), // user
                    nullW, // domain , null if local
                    new WString( PasswordUtils.getPasswordById( userId ) ), // password
                    MoreAdvApi32.LOGON_WITH_PROFILE, // dwLogonFlags
                    nullW, // lpApplicationName
                    new WString( String.join( " ", command ) ), // command line
                    MoreAdvApi32.CREATE_NO_WINDOW, // dwCreationFlags
                    null, // lpEnvironment
                    new WString( PropertiesManager.getKarafPath() ), // directory
                    startupInfo, processInformation );

            if ( !result ) {
                final int error = Kernel32.INSTANCE.GetLastError();
                log.info( "OS error #" + error );
                log.info( Kernel32Util.formatMessageFromLastErrorCode( error ) );
            }
        }

    }

    /**
     * Prepares command for execute sub process for unix.
     *
     * @param user
     *         the user
     * @param jobFile
     *         the job file
     *
     * @return the string[]
     */
    private String[] prepareSubProcessExecutionCommandForUnix( UserEntity user, File jobFile ) {
        String[] sudo_command = PropertiesManager.getSudoCommandConfigurations();
        int sudo_command_length = sudo_command.length;

        String[] command = new String[ sudo_command_length + 5 ];

        // adding sudo command to main command
        System.arraycopy( sudo_command, 0, command, 0, sudo_command_length );

        // adding rest of the command
        command[ sudo_command_length ] = user.getUserUid();
        command[ sudo_command_length + 1 ] = PropertiesManager.getEnginePath() + File.separator + "suscore-simflow-wfengine.sh";
        command[ sudo_command_length + 2 ] = JAVA_PATH;
        command[ sudo_command_length + 3 ] = System.getProperty( ConstantsKaraf.KARAF_HOME );
        command[ sudo_command_length + 4 ] = jobFile.getAbsolutePath();

        return command;
    }

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
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getWorkflowContextRouter(java.util.UUID, java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public Object getWorkflowContextRouter( UUID userIdFromGeneralHeader, String projectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final List< Object > selectedIds = filter.getItems();

            if ( CollectionUtil.isNotEmpty( selectedIds ) && ( selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) ) {
                return contextMenuManager.getWorkflowContextMenu( PLUGIN_OBJECT, projectId, WorkflowProjectDTO.class, filter );
            } else {
                final SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager,
                        userIdFromGeneralHeader.toString(), SelectionOrigins.CONTEXT, filter );
                final List< Object > selectionId = new ArrayList<>();
                selectionId.add( selectionResponseUI.getId() );
                filter.setItems( selectionId );

                boolean isObject = false;
                for ( final Object object : selectedIds ) {
                    final SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                            UUID.fromString( object.toString() ) );
                    if ( susEntity instanceof DataObjectEntity ) {
                        isObject = true;
                    }
                }
                return contextMenuManager.getDataContextMenuBulk( PLUGIN_OBJECT, projectId, WorkflowProjectDTO.class, filter, isObject );
            }
        } finally {
            entityManager.close();
        }
    }

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
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getWorkflowContextRouter(java.util.UUID, java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public List< ContextMenuItem > getContext( UUID userIdFromGeneralHeader, String token, String objectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            // case of select all from data table
            if ( filter.getItems().isEmpty() && Objects.toString( filter.getLength(), "" ).equalsIgnoreCase( "-1" ) ) {

                Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, SuSEntity.class, filter,
                        UUID.fromString( objectId ) );
                filter.setLength( Integer.valueOf( maxResults.toString() ) );
                List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, SuSEntity.class, filter,
                        UUID.fromString( objectId ) );
                List< Object > itemsList = new ArrayList<>();
                allObjectsList.stream().forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

                filter.setItems( itemsList );
            }

            final List< Object > selectedIds = filter.getItems();
            if ( CollectionUtil.isNotEmpty( selectedIds ) && ( selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) ) {
                final SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        UUID.fromString( selectedIds.get( FIRST_INDEX ).toString() ) );
                boolean isWorkFlow = susEntity instanceof WorkflowEntity;
                return getWorkflowContextMenu( entityManager, userIdFromGeneralHeader.toString(), token, filter, isWorkFlow );
            } else {
                SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager,
                        userIdFromGeneralHeader.toString(), SelectionOrigins.CONTEXT, filter );
                List< ContextMenuItem > cml = new ArrayList<>();
                if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
                    cml.add( prepareBulkDeleteObjectContext( UUID.fromString( selectionResponseUI.getId() ) ) );
                    cml.add( prepareBulkMoveToObjectContext( UUID.fromString( selectionResponseUI.getId() ) ) );
                }
                return cml;
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare move to object context.
     *
     * @param selectionId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareMoveToObjectContext( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.MOVE_WORKFLOW_PROJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.MOVE_TO_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare bulk move to object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareBulkMoveToObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.BULK_MOVE_WORKFLOW_PROJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.MOVE_TO_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare bulk delete object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareBulkDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.BULK_DELETE_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.BULK_DELETE_OBJECT_TITLE ) );
        return containerCMI;
    }

    /**
     * Gets the workflow context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param filter
     *         the filter
     * @param isWorkFlow
     *         the is work flow
     *
     * @return the workflow context menu
     */
    private List< ContextMenuItem > getWorkflowContextMenu( EntityManager entityManager, String userId, String token, FiltersDTO filter,
            boolean isWorkFlow ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareDeleteObjectContext( objectId ) );
            cml.add( prepareStatusChangeObjectContext( objectId ) );
            cml.add( prepareMoveToObjectContext( objectId ) );
            if ( isWorkFlow ) {
                cml.add( prepareShowWorkflowJobsContext( objectId ) );
                cml.add( prepareExportWorkflowContext( objectId, token ) );
                cml.add( prepareRunWorkflowContext( objectId ) );
                if ( permissionManager.isPermitted( entityManager, userId,
                        SimuspaceFeaturesEnum.WFSCHEMES.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue() ) ) {
                    cml.add( prepareRunSchemeContext( objectId ) );
                }
            } else {
                cml.add( prepareEditWorkflowContext( objectId ) );
            }
        }
        return cml;
    }

    /**
     * Prepare show workflow jobs context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareShowWorkflowJobsContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.OPEN_WF_JOBS_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.SHOW_WORKFLOW_JOBS_TITLE ) );
        containerCMI.setVisibility( ContextMenuManagerImpl.BOTH );
        return containerCMI;
    }

    /**
     * Prepare export workflow context.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareExportWorkflowContext( UUID objectId, String token ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "workflow/download/" + objectId );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100030x4" ) );
        containerCMI.setVisibility( "web" );
        return containerCMI;
    }

    /**
     * Prepare delete object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.DELETE_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.DELETE_OBJECT_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare edit object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareEditWorkflowContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl(
                WORKFLOW_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100014x4" ) );
        return containerCMI;
    }

    /**
     * Prepare status change object context.
     *
     * @param id
     *         the id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareStatusChangeObjectContext( UUID id ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.CHANGE_STATUS_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + id ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( CHANGE_STATUS_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare run workflow context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareRunWorkflowContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.RUN_WORKFLOW_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100045x4" ) );
        containerCMI.setVisibility( ContextMenuManagerImpl.BOTH );
        return containerCMI;
    }

    /**
     * Prepare run scheme context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareRunSchemeContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.RUN_SCHEME_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.RUN_SCHEME_TITLE ) );
        containerCMI.setVisibility( ContextMenuManagerImpl.BOTH );
        return containerCMI;
    }

    /**
     * Checks if is system workflow.
     *
     * @param workFlowId
     *         the work flow id
     *
     * @return true, if is system workflow
     */
    private boolean isSystemWorkflow( String workFlowId ) {
        return Arrays.stream( SystemWorkflow.values() ).anyMatch( systemWorkflow -> systemWorkflow.getId().equals( workFlowId ) );
    }

    /**
     * Gets the context menu manager.
     *
     * @return the context menu manager
     */
    public ContextMenuManager getContextMenuManager() {
        return contextMenuManager;
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
     * Gets the selection manager.
     *
     * @return the selection manager
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
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
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getWorkflowProjectTabsUI( UUID userIdFromGeneralHeader, String projectId ) {
        if ( projectId.equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) {

            final SubTabsItem subTabs = new SubTabsItem();
            subTabs.setTitle( MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.ALLWORKFLOWS.getCode() ) );
            subTabs.setTabs( List.of(
                    new SubTabsUI( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId(), SimuspaceFeaturesEnum.ALLWORKFLOWS.getKey().toLowerCase(),
                            MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.ALLWORKFLOWS.getCode() ) ) ) );
            return subTabs;
        }

        if ( projectId.equals( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId() ) ) {

            final SubTabsItem subTabs = new SubTabsItem();
            subTabs.setTitle( MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getKey() ) );
            subTabs.setTabs( List.of( new SubTabsUI( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId(),
                    SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getKey().toLowerCase(),
                    MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getCode() ) ) ) );
            return subTabs;
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        final SuSEntity entity;
        List< SubTabsUI > subTabsUIs;
        try {
            entity = getWorkflowProjectEntity( entityManager, userIdFromGeneralHeader, UUID.fromString( projectId ) );

            final SuSObjectModel objectTypeById = getConfigManager().getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                    entity.getConfig() );
            subTabsUIs = SubTabsUI.getSubTabsList( objectTypeById.getViewConfig() );
        } finally {
            entityManager.close();
        }
        return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), entity.getVersionId(), subTabsUIs,
                entity.getIcon() );
    }

    /**
     * Gets the data object entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param dataobjectId
     *         the dataobject id
     *
     * @return the data object entity
     */
    private SuSEntity getWorkflowProjectEntity( EntityManager entityManager, UUID userId, UUID dataobjectId ) {
        SuSEntity result = null;
        if ( dataobjectId != null ) {
            final SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, dataobjectId );
            if ( latestEntity != null ) {
                log.trace( latestEntity.getTypeId().toString() );
                log.trace( latestEntity.getConfig() );
                final SuSObjectModel entityModel = getConfigManager().getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                        latestEntity.getConfig() );
                result = susDAO.getLatestObjectByIdWithLifeCycle( entityManager, dataobjectId, userId,
                        getLifeCycleManager().getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        getLifeCycleManager().getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ) );

                isPermittedtoRead( entityManager, userId.toString(), dataobjectId.toString() );
            }
        }

        if ( result == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        }

        return result;
    }

    /**
     * Permitted to read.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedtoRead( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey(), OBJECT ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > listDataUI( String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !objectId.equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) && !objectId.equals(
                    SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId() ) ) {
                isPermittedtoRead( entityManager, userId, objectId );
            }
            final List< TableColumn > columns = GUIUtils.listColumns( GenericDTO.class );
            GUIUtils.setLinkColumn( UI_ALL_WORKFLOW_COLUMNS_NAME, UI_ALL_WORKFLOW_CLOUMNS_URL, columns );
            for ( TableColumn tableColumn : columns ) {
                if ( "entitySize".equalsIgnoreCase( tableColumn.getName() ) || tableColumn.getName().equalsIgnoreCase( "link" )
                        || tableColumn.getName().equalsIgnoreCase( ID_FIELD ) || tableColumn.getName().equalsIgnoreCase( "id" ) ) {
                    tableColumn.setVisible( false );
                }
            }
            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the life cycle manager.
     *
     * @return the life cycle manager
     */
    public LifeCycleManager getLifeCycleManager() {
        return lifeCycleManager;
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
     * Gets the config manager.
     *
     * @return the config manager
     */
    public ObjectTypeConfigManager getConfigManager() {
        return configManager;
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
     * {@inheritDoc}
     */
    @Override
    public WorkflowProjectDTO createWorkflowProject( String userId, String workflowProjectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        log.debug( "entered in createWorkflowProject method" );

        try {
            WorkflowProjectEntity createdEntity = null;
            WorkflowProjectDTO workflowProjectDTO = JsonUtils.jsonToObject( workflowProjectJson, WorkflowProjectDTO.class );
            if ( workflowProjectDTO != null ) {
                final Notification notif = workflowProjectDTO.validate();
                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }

                if ( !isNameUniqueAmongSiblings( entityManager, workflowProjectDTO.getName(), null,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, workflowProjectDTO.getParentId() ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
                }
                List< ConfigUtil.Config > configFiles = configManager.getMasterConfigurationFileNamesForWorkFlows();
                workflowProjectDTO.setCustomAttributesDTO(
                        configManager.getObjectTypeByIdAndConfigName( workflowProjectDTO.getTypeId().toString(),
                                ConfigUtil.labelByFile( configFiles, workflowProjectDTO.getConfig() ) ).getCustomAttributes() );

                createdEntity = workflowProjectDTO.prepareEntity( userId );
                final SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName(
                        workflowProjectDTO.getTypeId().toString(), ConfigUtil.labelByFile( configFiles, workflowProjectDTO.getConfig() ) );
                createdEntity.setIcon( suSObjectModel.getIcon() );
                createdEntity.setConfig( ConfigUtil.labelByFile( configFiles, workflowProjectDTO.getConfig() ) );
                final StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( workflowProjectDTO.getTypeId().toString(),
                        ConfigUtil.labelByFile( configFiles, workflowProjectDTO.getConfig() ) );
                createdEntity.setLifeCycleStatus( dto.getId() );
                final UserEntity user = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
                createdEntity.setCreatedBy( user );
                createdEntity.setModifiedBy( user );
                createdEntity.setCreatedOn( new Date() );
                createdEntity.setType( workflowProjectDTO.getTypeId().toString() );
                createdEntity.setOwner( user );
                createdEntity.setDescription( workflowProjectDTO.getDescription() );
                createdEntity.setTranslation( setMultiNames( entityManager, workflowProjectJson ) );
                try {
                    createdEntity.setSelectedTranslations(
                            JsonUtils.toJson( new ObjectMapper().readTree( workflowProjectJson ).path( SELECTED_TRANSLATIONS ) ) );
                } catch ( IOException e ) {
                    log.info( e.getMessage() );
                    throw new SusException( e.getMessage() );
                }
                // audit log
                if ( Boolean.TRUE.equals( PropertiesManager.isAuditWorkflow() ) ) {
                    createdEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                            workflowProjectDTO.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED,
                            ConstantsDbOperationTypes.CREATED, userId, createdEntity, suSObjectModel.getName() ) );
                }

                createdEntity = ( WorkflowProjectEntity ) susDAO.createAnObject( entityManager, createdEntity );

                final SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        workflowProjectDTO.getParentId() );
                if ( ( null != parentEntity ) && ( null != createdEntity ) ) {
                    final Relation relation = new Relation( parentEntity.getComposedId().getId(), createdEntity.getComposedId().getId() );
                    susDAO.createRelation( entityManager, relation );
                    addToAcl( entityManager, userId, createdEntity, parentEntity );
                    createIndexEntity( entityManager, suSObjectModel, createdEntity );
                    selectionManager.sendCustomerEventOnCreate( entityManager, userId, createdEntity, parentEntity );
                }
            }
            log.debug( "leaving createWorkflowProject method" );
            return prepareWorkflowProjectDTO( createdEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Sets the multiple names and files.
     *
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
                translationDAO.saveOrUpdate( entityManager, entity );
                entityList.add( entity );
            } catch ( IOException e ) {
                throw new SusException( e );
            }
        }
        return entityList;
    }

    /**
     * Translate name.
     *
     * @param userId
     *         the user id
     * @param suSEntities
     *         the su S entities
     */
    private void translateName( UserDTO user, SuSEntity suSEntity ) {
        if ( null != user || user.getId().equals( ConstantsID.SUPER_USER_ID ) ) {
            return;
        }
        if ( null != user.getUserDetails() ) {
            String userLang = user.getUserDetails().iterator().next().getLanguage();
            if ( PropertiesManager.hasTranslation() && de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty(
                    userLang ) ) {
                suSEntity.getTranslation().forEach( translation -> {
                    if ( userLang.equals( translation.getLanguage() )
                            && de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( translation.getName() ) ) {
                        suSEntity.setName( translation.getName() );
                    }
                } );
            }
        }
    }

    /**
     * Creates the index entity.
     *
     * @param entityManager
     *         the entity manager
     * @param susObjectModel
     *         the sus object model
     * @param entity
     *         the entity
     */
    private void createIndexEntity( EntityManager entityManager, SuSObjectModel susObjectModel, SuSEntity entity ) {
        try {
            if ( PropertiesManager.enableElasticSearch() && CommonUtils.validateURL() ) {
                final Map< String, String > requestHeaders = new HashMap<>();
                requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
                requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
                String url = PropertiesManager.getElasticSearchURL() + ConstantsString.FORWARD_SLASH + new String(
                        Base64.getEncoder().encode( susObjectModel.getName().getBytes() ) ).toLowerCase() + "/_doc/"
                        + entity.getComposedId().getId();
                SuSClient.postRequest( url, JsonUtils.toJson( createGenericDTOFromObjectEntity( entityManager, null, entity ) ),
                        requestHeaders );
            }
        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createProjectForm( String userId, UUID parentId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            log.debug( "entered in createProjectForm method" );
            isWorkflowManager( entityManager, UUID.fromString( userId ) );
            final List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( true, new WorkflowProjectDTO() );
            final SuSObjectModel suSObjectModel = configManager.getProjectModelByConfigLabel(
                    ConfigUtil.labelNames( configManager.getMasterConfigurationFileNamesForWorkFlows() ).get( FIRST_INDEX ) );

            for ( final UIFormItem uiFormItem : uiFormItemList ) {
                switch ( uiFormItem.getName() ) {
                    case WorkflowProjectDTO.UI_COLUMN_NAME_PARENT_ID -> uiFormItem.setValue( parentId );
                    case WorkflowProjectDTO.UI_COLUMN_NAME_TYPE_ID -> {
                        uiFormItem.setValue( suSObjectModel.getId() );
                        uiFormItem.setType( FIELD_TYPE_HIDDEN );
                    }
                    case WorkflowProjectDTO.UI_COLUMN_NAME_TYPE -> {
                        uiFormItem.setValue( suSObjectModel.getName() );
                        uiFormItem.setType( FIELD_TYPE_HIDDEN );
                    }
                    case WorkflowProjectDTO.UI_COLUMN_NAME_CONFIG -> {
                        uiFormItem.setValue(
                                ConfigUtil.fileNames( configManager.getMasterConfigurationFileNamesForWorkFlows() ).get( FIRST_INDEX ) );
                        uiFormItem.setType( FIELD_TYPE_HIDDEN );
                    }
                    case WorkflowProjectDTO.LIFE_CYCLE_STATUS -> uiFormItem.setType( FIELD_TYPE_HIDDEN );
                }
            }
            uiFormItemList.addAll( getProjectCustomAttributeUI( suSObjectModel ) );

            log.debug( "leaving createProjectForm method" );

            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Create translation fields ui.
     *
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param uiFormItemList
     *         the ui form item list
     */
    private void createTranslationFieldsUI( String parentId, String typeId, List< UIFormItem > uiFormItemList ) {
        Map< String, String > languages = PropertiesManager.getRequiredlanguages();
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( "selectedTranslations" );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( SELECT_TRANSLATION_LABEL ) );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        languages.keySet().forEach( language -> options.add( new SelectOptionsUI( language, languages.get( language ) ) ) );
        selectFormItem.setMultiple( Boolean.TRUE );
        selectFormItem.setOptions( options );
        selectFormItem.setBindFrom(
                BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE.replace( PARAM_OBJECT_ID, parentId ).replace( PARAM_TYPE_ID, typeId ) );
        uiFormItemList.add( selectFormItem );
    }

    /**
     * Gets the project custom attribute UI.
     *
     * @param suSObjectModel
     *         the su S object model
     *
     * @return the project custom attribute UI
     */
    private List< UIFormItem > getProjectCustomAttributeUI( SuSObjectModel suSObjectModel ) {

        log.debug( "entered in getProjectCustomAttributeUI method" );
        return convertJSONtoFormItems( suSObjectModel.getCustomAttributes(), null );
    }

    /**
     * Convert JSON to form items.
     *
     * @param customAttributes
     *         the custom attributes
     * @param attributeEntities
     *         the attribute entities
     *
     * @return the list
     */
    private List< UIFormItem > convertJSONtoFormItems( List< CustomAttributeDTO > customAttributes,
            Set< CustomAttributeEntity > attributeEntities ) {

        log.debug( "entered convertJSONtoFormItems method" );

        final List< UIFormItem > toReturn = new ArrayList<>();
        for ( final CustomAttributeDTO item : customAttributes ) {
            UIFormItem selectUI = GUIUtils.createFormItem( FormItemType.GENERAL );
            if ( item.getType().contains( UIColumnType.SELECT.getKey() ) ) {
                selectUI = GUIUtils.createFormItem( FormItemType.SELECT );
            }
            selectUI.setLabel( item.getTitle() );
            selectUI.setName( CUSTOM_ATTRIBUTES_FIELD_NAME + item.getName() );
            selectUI.setType( item.getType() );
            selectUI.setValue( getItemValue( attributeEntities, item ) );

            if ( item.getType().contains( UIColumnType.SELECT.getKey() ) ) {
                final Map< Object, Object > map = new HashMap<>();

                final List< ? > validOptions = item.getOptions();
                for ( final Object op : validOptions ) {
                    map.put( op, op );
                }
                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) selectUI, GUIUtils.getSelectBoxOptions( map ),
                        ( String ) selectUI.getValue(), false );
            }
            toReturn.add( selectUI );
        }

        log.debug( "leaving convertJSONtoFormItems method" );

        return toReturn;
    }

    /**
     * Gets the item value.
     *
     * @param attributeEntities
     *         the attribute entities
     * @param item
     *         the item
     *
     * @return the item value
     */
    private String getItemValue( Set< CustomAttributeEntity > attributeEntities, CustomAttributeDTO item ) {
        String itemValue = null;
        if ( attributeEntities != null ) {
            for ( final CustomAttributeEntity customAttributeEntity : attributeEntities ) {

                if ( item.getName().equals( customAttributeEntity.getName() ) ) {
                    itemValue = customAttributeEntity.getValue() != null ? ByteUtil.convertByteToString( customAttributeEntity.getValue() )
                            : null;
                    break;
                }
            }
        } else {
            itemValue = item.getValue() != null ? item.getValue().toString() : null;
        }
        return itemValue;
    }

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
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * de.soco.software.simuspace.server.manager.WorkflowManager#getAllSubProject(
     * java.lang.String, java.util.UUID,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public FilteredResponse< WorkflowProjectDTO > getAllSubProject( String userId, UUID parentId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, parentId );
            final List< WorkflowProjectDTO > workflowProjectDTOs = new ArrayList<>();
            final SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            final List< SuSEntity > sustEntityList = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager,
                    WorkflowProjectEntity.class, filter, parentId, userId,
                    lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    configManager.getTypesFromConfiguration( latestEntity.getConfig() ) );
            for ( final SuSEntity suSEntity : sustEntityList ) {
                workflowProjectDTOs.add( createProjectDTOFromProjectEntity( entityManager, parentId, suSEntity ) );
            }
            return PaginationUtil.constructFilteredResponse( filter, workflowProjectDTOs );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the project DTO from project entity.
     *
     * @param entityManager
     *         the entity manager
     * @param parentId
     *         the parent id
     * @param susEntity
     *         the sus entity
     *
     * @return the project DTO
     */
    private WorkflowProjectDTO createProjectDTOFromProjectEntity( EntityManager entityManager, UUID parentId, SuSEntity susEntity ) {
        WorkflowProjectDTO projectDTO = null;
        if ( susEntity != null ) {
            projectDTO = new WorkflowProjectDTO();
            projectDTO.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
            projectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            projectDTO.setName( susEntity.getName() );
            projectDTO.setDescription( susEntity.getDescription() );
            projectDTO.setCreatedOn( susEntity.getCreatedOn() != null ? susEntity.getCreatedOn() : null );
            projectDTO.setCreatedBy(
                    susEntity.getCreatedBy() != null ? userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() )
                            : null );
            projectDTO.setModifiedOn( susEntity.getModifiedOn() != null ? susEntity.getModifiedOn() : null );
            projectDTO.setConfig( susEntity.getConfig() );
            projectDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            projectDTO.setParentId( parentId );
            if ( susEntity.getModifiedBy() != null ) {
                projectDTO.setModifiedBy( userCommonManager.getUserById( entityManager, susEntity.getModifiedBy().getId() ) );
            }
            projectDTO.setTypeId( susEntity.getTypeId() );
            projectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                    susEntity.getConfig() ) );

        }
        return projectDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkflowProjectDTO getWorkflowProjectById( String userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity latestEntity = susDAO.getLatestObjectById( entityManager, WorkflowProjectEntity.class, projectId );
            return createProjectDTOFromProjectEntity( entityManager, null, latestEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectSingleUI( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            List< TableColumn > columns = GUIUtils.listColumns( WorkflowProjectDTO.class );
            columns.removeIf( table -> table.getData().equals( "parentId" ) || table.getName().equals( "typeId" ) );
            appendCustomAttributeColumns( susEntity, columns );
            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Append custom attribute columns.
     *
     * @param susEntity
     *         the sus entity
     * @param columns
     *         the columns
     */
    private void appendCustomAttributeColumns( SuSEntity susEntity, List< TableColumn > columns ) {

        if ( ( susEntity != null ) && ( susEntity.getTypeId() != null ) ) {

            final SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            columns.addAll( prepareCustomAttributesTableColumns( susObjectModel.getCustomAttributes() ) );
        }
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
        final List< TableColumn > tableColumns = new ArrayList<>();
        for ( final CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
            final TableColumn moduleColumn = new TableColumn();
            moduleColumn.setData( CUSTOM_ATTRIBUTES_FIELD_NAME + customAttributeDTO.getName() );
            moduleColumn.setName( CUSTOM_ATTRIBUTES_FIELD_NAME + VALUE );
            moduleColumn.setTitle( customAttributeDTO.getTitle() );
            moduleColumn.setFilter( CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE );
            final Renderer renderer = new Renderer();
            renderer.setType( CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE );
            moduleColumn.setRenderer( renderer );

            tableColumns.add( moduleColumn );
        }
        return tableColumns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< WorkflowProjectDTO > getObjectVersions( UUID userId, UUID projectId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filtersDTO == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            final List< WorkflowProjectDTO > objectDTOList = new ArrayList<>();
            final SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectId );
            final SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            final List< SuSEntity > objectEntityList = susDAO.getAllFilteredVersionsById( entityManager, SuSEntity.class, projectId,
                    filtersDTO, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ) );
            if ( CollectionUtil.isNotEmpty( objectEntityList ) ) {
                for ( final SuSEntity objectEntity : objectEntityList ) {
                    objectDTOList.add( createProjectDTOFromProjectEntity( entityManager, null, objectEntity ) );
                }
            }
            return PaginationUtil.constructFilteredResponse( filtersDTO, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, String viewJson, boolean isUpdateable, String key ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            final String config = entity.getConfig();
            return objectViewManager.saveOrUpdateObjectView( entityManager,
                    prepareObjectViewDTO( objectId, config, viewJson, key, isUpdateable ), userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO setObjectViewAsDefault( String userId, String objectId, String viewId, String key ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            return objectViewManager.saveDefaultObjectViewByConfig( entityManager, UUID.fromString( viewId ), userId, key, null,
                    entity.getConfig() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getListOfObjectView( String userId, String objectId, String key ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            final SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            return objectViewManager.getUserObjectViewsByKeyAndConfig( entityManager, key, userId, entity.getConfig() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopServerWorkFlow( String jobId, String userName ) {
        return executionManager.stopJobExecution( jobId, userName );
    }

    /**
     * Prepare workflow project DTO.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the workflow project DTO
     */
    private WorkflowProjectDTO prepareWorkflowProjectDTO( SuSEntity susEntity ) {
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
            workflowProjectDTO.setCustomAttributes(
                    CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            workflowProjectDTO.setTypeId( susEntity.getTypeId() );
            workflowProjectDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                            susEntity.getConfig() ) );
        }
        return workflowProjectDTO;
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

        final List< SuSEntity > sameNameList = susDAO.getSiblingsBySameIName( entityManager, name, updateEntity, parent );
        return sameNameList.isEmpty();

    }

    /**
     * Prepare object view DTO.
     *
     * @param objectId
     *         the object id
     * @param config
     *         the config
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTO( String objectId, String config, String viewJson, String viewKey, boolean isUpdateable ) {
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( !isUpdateable && !objectViewDTO.isDefaultView() ) {
            objectViewDTO.setId( null );
        }
        objectViewDTO.setConfig( config );
        objectViewDTO.setObjectId( objectId );
        objectViewDTO.setObjectViewKey( viewKey );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataObjectVersionContext( String objectId, FiltersDTO filter ) {

        final List< ContextMenuItem > menu = new ArrayList<>();
        if ( filter.getItems().size() == 1 ) {
            final ContextMenuItem containerCMI = new ContextMenuItem();
            containerCMI.setUrl( WORKFLOW_CHANGE_STATUS_URL.replace( PARAM_OBJECT_ID, objectId )
                    .replace( PARAM_VERSION_ID, filter.getItems().get( FIRST_INDEX ) + ConstantsString.EMPTY_STRING ) );
            containerCMI.setTitle( MessageBundleFactory.getMessage( CHANGE_STATUS_TITLE ) );
            menu.add( containerCMI );
        }

        return menu;
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
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfUserHasWorkflowLicense( UUID userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return isWorkflowUser( entityManager, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfUserHasSchemeLicense( UUID userIdFromGeneralHeader, String workflowId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isWorkflowUser( entityManager, userIdFromGeneralHeader );
            validateUserSchemeLicense( entityManager, userIdFromGeneralHeader.toString(), workflowId );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Check user scheme license.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param workflowId
     *         the workflow id
     */
    private void validateUserSchemeLicense( EntityManager entityManager, String userIdFromGeneralHeader, String workflowId ) {
        int category = getSchemeCategory( entityManager, workflowId, userIdFromGeneralHeader );

        if ( category == ConstantsInteger.INTEGER_VALUE_ZERO && !licenseManager.isFeatureAllowedToUser( entityManager,
                SimuspaceFeaturesEnum.DOE.getKey(), userIdFromGeneralHeader ) ) {
            throw new SusException( "User does not have DOE scheme license" );
        } else if ( category == ConstantsInteger.INTEGER_VALUE_ONE && !licenseManager.isFeatureAllowedToUser( entityManager,
                SimuspaceFeaturesEnum.OPTIMIZATION.getKey(), userIdFromGeneralHeader ) ) {
            throw new SusException( "User does not have OPTIMIZATION scheme license" );
        }
    }

    /**
     * Gets the all objects.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return the all objects
     */
    @Override
    public FilteredResponse< Object > getAllObjects( String userId, UUID parentId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            final SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, parentId );
            List< SuSEntity > suSEntities;
            final List< Object > objectDTOList = new ArrayList<>();
            if ( latestEntity instanceof SystemContainerEntity ) {
                final String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName(
                        ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME ).get( 0 ).getId();
                suSEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndPermission( entityManager, SuSEntity.class, filter,
                        parentId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ), configManager.getAllTypesFromConfiguration(),
                        PermissionMatrixEnum.VIEW.getKey() );
            } else {
                final SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                        latestEntity.getConfig() );
                suSEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndPermission( entityManager, SuSEntity.class, filter,
                        parentId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        configManager.getTypesFromConfiguration( latestEntity.getConfig() ), PermissionMatrixEnum.VIEW.getKey() );
            }
            UserDTO user = TokenizedLicenseUtil.getUser(
                    BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
            suSEntities.forEach( entity -> translateName( user, entity ) );

            if ( CollectionUtil.isNotEmpty( suSEntities ) ) {
                for ( final SuSEntity suSEntity : suSEntities ) {
                    final Object object = createGenericDTOFromObjectEntity( entityManager, parentId, suSEntity );
                    if ( null != object ) {
                        objectDTOList.add( object );
                    }
                }
            }
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the generic DTO from object entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, UUID projectId, SuSEntity susEntity ) {
        GenericDTO objectDTO = null;
        if ( susEntity != null ) {
            final GenericDTO genericDTO = new GenericDTO();
            genericDTO.setName( susEntity.getName() );
            genericDTO.setId( susEntity.getComposedId().getId() );
            genericDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDTO.setParentId( projectId );
            genericDTO.setDescription( susEntity.getDescription() );
            genericDTO.setTypeId( susEntity.getTypeId() );
            genericDTO.setSize( ( susEntity.getSize() != null ) && ( susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO )
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
            if ( null != susEntity.getCreatedBy() ) {
                genericDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                genericDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }

            if ( susEntity instanceof ContainerEntity ) {
                genericDTO.setUrlType( ConstantsString.PROJECT_KEY );
            } else {
                genericDTO.setUrlType( ConstantsString.OBJECT_KEY );
            }

            if ( susEntity.getTypeId() != null ) {
                genericDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                                susEntity.getConfig() ) );
                genericDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
                genericDTO.setIcon( susEntity.getIcon() );
            }

            final List< Relation > relation = linkDAO.getLinkedRelationByChildId( entityManager, susEntity.getComposedId().getId() );

            if ( CollectionUtils.isNotEmpty( relation ) ) {
                genericDTO.setLink( LINK_TYPE_YES );
            } else {
                genericDTO.setLink( LINK_TYPE_NO );
            }

            objectDTO = genericDTO;
        }
        return objectDTO;
    }

    /**
     * Validate rights.
     *
     * @param entityManager
     *         the entity manager
     * @param jobParameters
     *         the job parameters
     * @param userIdFromGeneralHeader
     *         the user id from general header
     */
    private void validateRights( EntityManager entityManager, JobParameters jobParameters, UUID userIdFromGeneralHeader ) {
        isWorkflowUser( entityManager, userIdFromGeneralHeader );

        if ( jobParameters.getWorkflow().getId() != null ) {
            String perm = jobParameters.getWorkflow().getId().toString() + COLON + PermissionMatrixEnum.EXECUTE.getValue();

            if ( !permissionManager.isPermitted( entityManager, userIdFromGeneralHeader.toString(), perm ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), jobParameters.getWorkflow().getName() ) );
            }
        }
    }

    /**
     * Gets the location.
     *
     * @param entityManager
     *         the entity manager
     * @param jobParameters
     *         the job parameters
     *
     * @return the location
     */
    private LocationDTO getLocation( EntityManager entityManager, JobParameters jobParameters ) {
        return locationManager.prepareDTO( locationManager.getLocationDAO()
                .getLatestNonDeletedObjectById( entityManager, UUID.fromString( jobParameters.getRunsOn() ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobParameters getJobParametersByWorkflowId( EntityManager entityManager, String userId, UUID wfId, String token ) {
        WorkflowEntity workflowEntity = workflowDao.getLatestNonDeletedObjectById( entityManager, wfId );
        LatestWorkFlowDTO latestWfDTO = prepareLatestWorkflowFromWorkflowDTO(
                prepareWorkflowDTO( entityManager, UUID.fromString( userId ), workflowEntity ) );
        RestAPI server = getServerByLocationId( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );

        UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );

        JobParameters jobParameters = new JobParametersImpl();
        jobParameters.setName( latestWfDTO.getName() );
        jobParameters.setWorkingDir( new EngineFile( PropertiesManager.getUserStagingPath( user.getUserUid() ) ) );
        jobParameters.setWorkflow( latestWfDTO );
        jobParameters.setRequestHeaders( new RequestHeaders( token,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36" ) );
        jobParameters.setServer( server );
        jobParameters.setRunsOn( LocationsEnum.DEFAULT_LOCATION.getId() );
        jobParameters.setId( UUID.randomUUID().toString() );
        return jobParameters;
    }

    /**
     * Gets the server by location id.
     *
     * @param entityManager
     *         the entity manager
     * @param locationId
     *         the location id
     *
     * @return the server by location id
     */
    private RestAPI getServerByLocationId( EntityManager entityManager, String locationId ) {
        LocationDTO location = locationManager.getLocation( entityManager, locationId );
        URL url = null;
        try {
            url = new URL( location.getUrl() );
        } catch ( MalformedURLException e ) {
            log.error( e.getMessage() );
        }
        return new RestAPI( url.getProtocol() + "://", url.getHost(), url.getPort() + "" );
    }

    /**
     * Run server job from web.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param token
     *         the token
     * @param jobParameters
     *         the job parameters
     */
    @Override
    public void runServerJobFromWeb( UUID userIdFromGeneralHeader, String token, JobParameters jobParameters ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( jobParameters != null ) {
                validateRights( entityManager, jobParameters, userIdFromGeneralHeader );
                if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {
                    if ( jobParameters.isFileRun() ) {
                        throw new SusException( "Cannot run file based workflows on server." );
                    }
                    LocationDTO jobLocation = getLocation( entityManager, jobParameters );
                    validateActiveLocationForJobSubmission( jobLocation );

                    jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.WORKFLOW.getKey() );
                    jobParameters.setJobType( JobTypeEnums.WORKFLOW.getKey() );
                    jobParameters.getRequestHeaders().setToken( token );

                    Thread thread = new Thread( () -> runServerSideJob( userIdFromGeneralHeader, jobParameters ) );
                    thread.start();
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run scheme.
     *
     * @param workflowId
     *         the workflow id
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param jobParameters
     *         the job parameters
     *
     * @return true, if successful
     */
    @Override
    public boolean runScheme( EntityManager entityManager, String workflowId, String userId, String uid, JobParameters jobParameters,
            int category, WFSchemeEntity schemeEntity ) {
        if ( jobParameters != null ) {
            validateRights( entityManager, jobParameters, UUID.fromString( userId ) );
            if ( !WorkflowDefinitionUtil.isLocal( jobParameters.getRunsOn() ) ) {
                validateSchemeLocation( entityManager, userId, jobParameters );
                jobParameters.setJobType( JobTypeEnums.SCHEME.getKey() );
                jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.SCHEME.getKey() );
                Job masterJobParameters = prepareMasterJobParameters( entityManager, userId, jobParameters, workflowId, category );
                Job savedMasterJob = saveMasterJob( masterJobParameters );
                if ( category == SchemeCategoryEnum.DOE.getKey() ) {
                    runSchemeDOE( userId, jobParameters, savedMasterJob, jobParameters.getName() );
                } else if ( category == SchemeCategoryEnum.OPTIMIZATION.getKey() ) {
                    runSchemeOptimization( entityManager, userId, uid, jobParameters, savedMasterJob, executionManager, schemeEntity );
                }
            }
        }
        return true;
    }

    /**
     * Validate scheme location.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     */
    private void validateSchemeLocation( EntityManager entityManager, String userId, JobParameters jobParameters ) {
        if ( jobParameters.isFileRun() ) {
            throw new SusException( "Cannot run file based workflows on server." );
        }
        LocationDTO jobLocation = getLocation( entityManager, jobParameters );
        validateActiveLocationForJobSubmission( jobLocation );
        final UserEntity user = userManager.getSimUser( entityManager, UUID.fromString( userId ) );
        prepareWfUploadedFiles( user, jobParameters );
        validateAndFixOsPathPrefrence( user, jobParameters );
    }

    /**
     * Prepare master job parameters job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     * @param workflowId
     *         the workflow id
     * @param category
     *         the category
     *
     * @return the job
     */
    private Job prepareMasterJobParameters( EntityManager entityManager, String userId, JobParameters jobParameters, String workflowId,
            int category ) {
        Job masterJobParameters = prepareJobModel(
                jobParameters.getName() + "_" + new SimpleDateFormat( "h_m_s" ).format( new Date() ) + "_Master",
                jobParameters.getDescription(), getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
        masterJobParameters.setId( UUID.randomUUID() );
        setRunsOnInJobParameters( jobParameters, masterJobParameters );

        masterJobParameters.setProgress( new ProgressBar( ConstantsInteger.INTEGER_VALUE_ZERO,
                new AtomicInteger( ConstantsInteger.INTEGER_VALUE_ZERO ).longValue() ) );

        masterJobParameters.setServer( jobParameters.getServer() );
        masterJobParameters.setRequestHeaders( jobParameters.getRequestHeaders() );
        masterJobParameters.setJobType( JobTypeEnums.SCHEME.getKey() );
        masterJobParameters.setRunMode( jobParameters.getRunsOn() );
        masterJobParameters.setJobRelationType( JobRelationTypeEnums.MASTER.getKey() );
        masterJobParameters.setWorkingDir( getWorkingDirPath( entityManager, UUID.fromString( userId ), jobParameters ) );
        masterJobParameters.setWorkflow( jobParameters.getWorkflow() );
        masterJobParameters.setRequestHeaders( jobParameters.getRequestHeaders() );
        masterJobParameters.setPostprocess( jobParameters.getPostprocess() );
        masterJobParameters.setDescription( jobParameters.getDescription() );
        masterJobParameters.setJobSchemeCategory( category );

        masterJobParameters.setPostprocessParametersJson(
                setPostProcessJobParameters( entityManager, UUID.fromString( userId ), jobParameters ) );

        Integer id = jobManager.saveJobIds( entityManager, masterJobParameters.getId() );
        if ( id != null ) {
            masterJobParameters.setJobInteger( id );
        }

        masterJobParameters.getRequestHeaders().setJobAuthToken(
                prepareJobToken( userId, jobParameters.getJobRunByUserUID(), masterJobParameters.getId().toString(),
                        masterJobParameters.getServer(), masterJobParameters.getRequestHeaders() ) );

        setChildJobsCount( entityManager, userId, masterJobParameters, category, jobParameters );

        return masterJobParameters;
    }

    /**
     * Sets the post process job parameters.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     *
     * @return the string
     */
    private String setPostProcessJobParameters( EntityManager entityManager, UUID userId, JobParameters jobParameters ) {

        if ( jobParameters.getPostprocess() != null && jobParameters.getPostprocess().getPostProcessWorkflow() != null
                && jobParameters.getPostprocess().getPostProcessWorkflow().getElements() != null ) {

            log.debug( "preparing post process job parameters" );

            LatestWorkFlowDTO latestWorkFlowDTO = getNewWorkflowById( entityManager, userId,
                    jobParameters.getPostprocess().getPostProcessWorkflow().getId() );

            try {
                // read the json file

                org.json.JSONObject jsonObj = new org.json.JSONObject( JsonUtils.toJson( latestWorkFlowDTO.getElements() ) );
                JSONArray nodes = ( JSONArray ) jsonObj.get( NODES );

                Map< String, Object > map = jobParameters.getPostprocess().getPostProcessWorkflow().getElements();

                for ( Map.Entry< String, Object > entry : map.entrySet() ) {
                    for ( int i = 0; i < nodes.length(); i++ ) {
                        org.json.JSONObject nodeDetails = ( org.json.JSONObject ) nodes.get( i );
                        org.json.JSONObject data = ( org.json.JSONObject ) nodeDetails.get( "data" );
                        if ( data.get( "id" ).toString().equalsIgnoreCase( entry.getKey() ) ) {
                            JSONArray feildsList = ( JSONArray ) data.get( FIELDS );
                            Map< String, String > postElementsMap = ( Map< String, String > ) entry.getValue();
                            for ( Map.Entry< String, String > entry1 : postElementsMap.entrySet() ) {
                                for ( int j = 0; j < feildsList.length(); j++ ) {
                                    org.json.JSONObject select = ( org.json.JSONObject ) feildsList.get( j );
                                    if ( select.get( "name" ).toString().equalsIgnoreCase( entry1.getKey() )
                                            && entry1.getValue() != null ) {
                                        select.remove( VALUE );
                                        select.put( VALUE, entry1.getValue() );
                                    }
                                }
                            }
                        }
                    }
                }
                Map< String, Object > elements = ( Map< String, Object > ) JsonUtils.jsonToMap( jsonObj.toString(), new HashMap<>() );
                latestWorkFlowDTO.setElements( elements );
                JobParameters jobParameterNew = new JobParametersImpl();
                jobParameterNew.setWorkflow( latestWorkFlowDTO );
                log.debug( "post process job parameters Set successfully" );
                return JsonUtils.toJson( jobParameterNew );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
        return null;
    }

    /**
     * Refresh scheme shared directory.
     *
     * @param userUid
     *         the user uid
     * @param workflowId
     *         the workflow id
     */
    private void refreshSchemeSharedDirectory( String userUid, String workflowId ) {
        String sharedDirectoryPath = PropertiesManager.getUserStagingPath( userUid ) + File.separator + workflowId;
        if ( Files.exists( Path.of( sharedDirectoryPath ) ) ) {
            LinuxUtils.deleteFileOrDirByPath( userUid, sharedDirectoryPath );
            LinuxUtils.createDirectory( userUid, sharedDirectoryPath );
        }
    }

    /**
     * Prepare job token.
     *
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param id
     *         the id
     * @param server
     *         the server
     * @param requestHeaders
     *         the request headers
     *
     * @return the string
     */
    private String prepareJobToken( String userId, String uid, String id, RestAPI server, RequestHeaders requestHeaders ) {
        final String url = prepareURL( "/api/auth/prepareJobToken/" + userId + "/" + uid + "/" + id + "/" + requestHeaders.getToken(),
                server );
        SusResponseDTO susResponseDTO = SuSClient.getRequest( url, prepareHeaders( requestHeaders ) );
        String token;
        if ( susResponseDTO != null && !susResponseDTO.getSuccess() ) {
            throw new SusException( new Exception( susResponseDTO.getMessage().getContent() ), getClass() );
        } else {
            token = susResponseDTO.getData().toString();
        }
        return token;
    }

    /**
     * Prepare URL.
     *
     * @param url
     *         the url
     * @param server
     *         the server
     *
     * @return the string
     */
    private String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /**
     * Prepare headers.
     *
     * @param headers
     *         the headers
     *
     * @return the map
     */
    private Map< String, String > prepareHeaders( RequestHeaders headers ) {
        final Map< String, String > requestHeaders = new HashMap<>();
        requestHeaders.put( ConstantRequestHeader.CONTENT_TYPE, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.ACCEPT, ConstantRequestHeader.APPLICATION_JSON );
        requestHeaders.put( ConstantRequestHeader.AUTH_TOKEN, headers.getToken() );
        requestHeaders.put( ConstantRequestHeader.JOB_TOKEN, headers.getJobAuthToken() );
        return requestHeaders;
    }

    /**
     * Saves master job.
     *
     * @param masterJobParameters
     *         the master job parameters
     *
     * @return the saved master job
     */
    private Job saveMasterJob( Job masterJobParameters ) {
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add(
                new LogRecord( ConstantsMessageTypes.INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_MAKE_MASTER_JOB.getKey() ),
                        new Date() ) );
        masterJobParameters.setLog( jobLog );
        Job savedMasterJob = executionManager.saveJob( masterJobParameters );
        log.info( "Monitoring job with id: " + savedMasterJob.getId() );
        savedMasterJob.setRequestHeaders( masterJobParameters.getRequestHeaders() );
        savedMasterJob.setServer( masterJobParameters.getServer() );

        return savedMasterJob;
    }

    /**
     * Sets the runs on in job parameters.
     *
     * @param jobParameters
     *         the job parameters
     * @param masterJobParameters
     *         the master job parameters
     */
    private void setRunsOnInJobParameters( JobParameters jobParameters, Job masterJobParameters ) {
        masterJobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID() : UUID.fromString( jobParameters.getId() ) );
        if ( Boolean.TRUE.equals( PropertiesManager.isHostEnable() ) ) {
            ExecutionHosts executionHost = null;
            Hosts hostList = PropertiesManager.getHosts();
            if ( hostList != null && CollectionUtils.isNotEmpty( hostList.getExcutionHosts() ) ) {
                for ( ExecutionHosts host : hostList.getExcutionHosts() ) {
                    if ( host.getId().toString().equals( jobParameters.getRunsOn() ) ) {
                        masterJobParameters.setMachine( host.getName() );
                        executionHost = host;
                        break;
                    }
                }
            }
            if ( null == executionHost ) {
                masterJobParameters.setRunsOn( new LocationDTO( UUID.fromString( jobParameters.getRunsOn() ) ) );
            } else {
                masterJobParameters.setRunsOn( new LocationDTO( UUID.fromString( LocationsEnum.DEFAULT_LOCATION.getId() ) ) );
            }
        } else {
            masterJobParameters.setRunsOn( new LocationDTO( UUID.fromString( jobParameters.getRunsOn() ) ) );
        }
    }

    /**
     * Gets the working dir path.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     *
     * @return the working dir path
     */
    private EngineFile getWorkingDirPath( EntityManager entityManager, UUID userId, JobParameters jobParameters ) {
        LocationDTO locationDTO;
        if ( Boolean.TRUE.equals( PropertiesManager.isHostEnable() ) ) {
            locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        } else {
            locationDTO = locationManager.getLocation( entityManager, jobParameters.getRunsOn() );
        }
        final UserEntity user = userManager.getSimUser( entityManager, userId );
        List< String > items = new ArrayList<>();
        items.add( PropertiesManager.getUserStagingPathFromStaging( user.getUserUid(), locationDTO.getStaging() ) );
        EngineFile engineFile = new EngineFile();
        engineFile.setItems( items );
        engineFile.setPath( PropertiesManager.getUserStagingPathFromStaging( user.getUserUid(), locationDTO.getStaging() ) + File.separator
                + jobParameters.getWorkflow().getId() );
        return engineFile;
    }

    /**
     * Sets child jobs count field for master job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param masterJobParameters
     *         the master job parameters
     * @param category
     *         the category
     * @param jobParameters
     *         the job parameters
     */
    private void setChildJobsCount( EntityManager entityManager, String userId, Job masterJobParameters, int category,
            JobParameters jobParameters ) {
        if ( category == ConstantsInteger.INTEGER_VALUE_ZERO ) {
            List< Map< String, Object > > designSummary = getExecutionManager().getDesignSummary( jobParameters.getServer(),
                    jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );
            masterJobParameters.setChildJobsCount( designSummary.size() );
        } else if ( category == ConstantsInteger.INTEGER_VALUE_ONE ) {
            try {
                String maxJobsToRun = getSchemeMaxJobsToRun( entityManager, jobParameters.getWorkflow().getId().toString(), userId,
                        jobParameters );
                Integer maxJobs = Integer.parseInt( maxJobsToRun );
                if ( maxJobs != null && maxJobs != 0 ) {
                    masterJobParameters.setChildJobsCount( maxJobs );
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
    }

    /**
     * Validate and fix os path prefrence.
     *
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     */
    private void validateAndFixOsPathPrefrence( UserEntity user, JobParameters jobParameters ) {
        final WorkflowDefinitionDTO jobParamDef = WorkflowDefinitionUtil.getWorkflowDefinitionDTOFromMap(
                jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = WorkflowDefinitionUtil.prepareWorkflowElements( jobParamDef );

        WorkflowDefinitionUtil.renameLocalPathsToStagingPathWithOSValidator( jobWFElements,
                PropertiesManager.getUserStagingPath( user.getUserUid() ), jobParamDef, jobParameters );
    }

    /**
     * Prepares files used by workflow elements by extracting the uploaded zip file in the vault.
     *
     * @param user
     *         the user
     * @param jobParameters
     *         the job parameters
     */
    private void prepareWfUploadedFiles( UserEntity user, JobParameters jobParameters ) {
        String zipPath = PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow().getId()
                + File.separator + jobParameters.getName();

        File zipFile = new File( zipPath );

        if ( zipFile.exists() ) {
            try {
                if ( zipFile.renameTo( new File( zipPath + ConstantsFileExtension.ZIP ) ) ) {

                    FileUtils.setGlobalAllFilePermissions( new File(
                            PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow()
                                    .getId() ) );
                    LinuxUtils.createDirectory( user.getUserUid(),
                            PropertiesManager.getUserStagingPath( user.getUserUid() ) + File.separator + jobParameters.getWorkflow().getId()
                                    + File.separator + "/files" );
                    LinuxUtils.extractZipFile( user.getUserUid(), zipFile.getAbsolutePath() + ConstantsFileExtension.ZIP );

                    FileUtils.deleteFile( zipPath + ConstantsFileExtension.ZIP ); // removing temp zip file after extract
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }
        }
    }

    /**
     * Create extract directory for zip file.
     *
     * @param userUid
     *         the user uid
     * @param jobParameters
     *         the job parameters
     *
     * @return the file
     */
    private File createFilesDirectory( String userUid, JobParameters jobParameters ) {
        String filesPath =
                PropertiesManager.getUserStagingPath( userUid ) + File.separator + jobParameters.getWorkflow().getId() + File.separator
                        + FILES;
        File dir = new File( filesPath );
        try {
            if ( !dir.exists() ) {
                dir.mkdir();
            } else if ( dir.isDirectory() ) {
                org.apache.commons.io.FileUtils.cleanDirectory( dir );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
        }
        return dir;
    }

    /**
     * Run scheme optimization.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     * @param jobParameters
     *         the job parameters
     * @param savedMasterJob
     *         the saved master job
     * @param executionManager
     *         the execution manager
     * @param schemeEntity
     *         the scheme entity
     */
    private void runSchemeOptimization( EntityManager entityManager, String userId, String uid, JobParameters jobParameters,
            Job savedMasterJob, WFExecutionManager executionManager, WFSchemeEntity schemeEntity ) {

        List< String > parserIdList = new ArrayList<>();
        List< ScanFileDTO > designVariableList = new ArrayList<>();
        List< ScanFileDTO > objectiveVariableList = new ArrayList<>();
        Map< String, Object > templateVariableList = new HashMap<>();

        WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( jobParameters.getWorkflow().getElements() ),
                WorkflowModel.class );

        prepareDesignVarAndObjectiveVar( entityManager, designVariableList, objectiveVariableList, workflowModel, parserIdList,
                templateVariableList, userId, uid );

        if ( designVariableList.isEmpty() ) {
            if ( !parserIdList.isEmpty() ) {
                ScanFileDTO scanDto = new ScanFileDTO();
                scanDto.setVariableType( FieldTypes.OBJECT_PARSER.getType() );
                designVariableList.add( scanDto );
                objectiveVariableList.add( scanDto );
            } else if ( !templateVariableList.isEmpty() ) {
                ScanFileDTO scanDto = new ScanFileDTO();
                scanDto.setVariableType( FieldTypes.TEMPLATE_FILE.getType() );
                designVariableList.add( scanDto );
                objectiveVariableList.add( scanDto );
            }
        }

        if ( ( jobParameters.getWorkflow().getId() != null ) && ( !userId.equals( ConstantsID.SUPER_USER_ID )
                && !permissionManager.isPermitted( entityManager, userId,
                jobParameters.getWorkflow().getId() + COLON + PermissionMatrixEnum.EXECUTE.getValue() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_EXECUTE.getKey(), OBJECT ) );
        }

        final UserEntity user = userManager.getSimUser( entityManager, UUID.fromString( userId ) );
        LocationDTO locationDTO = prepareLocationForJob( entityManager, jobParameters );

        if ( locationDTO.getUrl() == null || locationDTO.getUrl().isEmpty() ) {
            throw new SusException( "Please select server location to run workflow from WEB." );
        }

        permissionManager.isPermitted( entityManager, userId,
                locationDTO.getId() + ConstantsString.COLON + PermissionMatrixEnum.EXECUTE.getValue() + ConstantsString.COLON + IS_LOCATION,
                Messages.NO_RIGHTS_TO_EXECUTE.getKey(), locationDTO.getName() );

        setWorkingDirPath( user.getUserUid(), jobParameters, locationDTO );

        jobParameters.getWorkflow().setMasterJobId( savedMasterJob.getId().toString() );
        jobParameters.setId( jobParameters.getId() == null ? UUID.randomUUID().toString() : jobParameters.getId() );

        jobParameters.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );
        jobParameters.setJobSchemeCategory( savedMasterJob.getJobSchemeCategory() );
        jobParameters.setCreateChild( true );

        List< ObjectiveVariableDTO > objectiveVar = getExecutionManager().getObjectiveVariable( jobParameters.getServer(),
                jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );
        jobParameters.setObjectiveVariables( objectiveVar );

        SchemeOptimizationDTO pythonParameters = prepareSchemeOptimizationDTO( userId, jobParameters, designVariableList,
                objectiveVariableList, user );

        final String pythonParameterJson = JsonUtils.toJson( pythonParameters );

        String wfDirAddress = File.separator + jobParameters.getWorkflow().getId().toString();

        File wfDir = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + wfDirAddress );

        LinuxUtils.createDirectory( user.getUserUid(), wfDir.getAbsolutePath() );
        String pyFileAddress =
                wfDirAddress + File.separator + jobParameters.getWorkflow().getName() + "_" + new Date().getSeconds() + "_py.json";
        File fileObjectiveVariables = new File( PropertiesManager.getUserStagingPath( user.getUserUid() ) + pyFileAddress );

        LinuxUtils.createFile( user.getUserUid(), fileObjectiveVariables.getAbsolutePath() );
        LinuxUtils.writeFile( user.getUserUid(), fileObjectiveVariables.getAbsolutePath(), pythonParameterJson );

        String url = jobParameters.getServer().getProtocol() + jobParameters.getServer().getHostname() + ConstantsString.COLON
                + jobParameters.getServer().getPort();
        TransferLocationObject transferLocationObject = new TransferLocationObject();
        transferLocationObject.setFilePath( pyFileAddress );
        transferLocationObject.setTargetAddress( locationDTO.getUrl() );
        transferLocationObject.setTargetToken( locationDTO.getAuthToken() );
        transferLocationObject.setOperation( TransferOperationType.MOVE );
        final String josnObjectDTO = JsonUtils.toJsonString( transferLocationObject );
        SuSClient.postRequest( url + API_LOCATION_EXPORT_FILE, josnObjectDTO,
                WorkflowDefinitionUtil.prepareHeaders( locationDTO.getAuthToken(), user.getUserUid() ) );

        String options = getOptionSchemeOtherThanMaxJob( entityManager, jobParameters.getWorkflow().getId().toString(), userId,
                jobParameters );
        String optimizationPythonFilePath = getOptimizationPyFilePathFromAlgoConfigOfchemeOptions( schemeEntity );
        String goals = prepareGoalsWithLabel( entityManager, jobParameters.getWorkflow().getId().toString() );
        RunSchemeOptimizationThread runSchemeOptimizationThread = new RunSchemeOptimizationThread( fileObjectiveVariables,
                optimizationPythonFilePath, options, executionManager, tokenManager, savedMasterJob, goals );
        SusExecutorUtil.threadPoolExecutorService.schemeExecute( runSchemeOptimizationThread, savedMasterJob.getId() );
    }

    /**
     * Prepare goals.
     *
     * @param id
     *         the id
     *
     * @return the string
     */
    @Override
    public String prepareGoals( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String goals = null;
            List< ObjectiveVariableEntity > objectiveVariableEntities = variableDAO.getAllObjectiveVariablesByWorkflowId( entityManager,
                    id );
            if ( CollectionUtils.isNotEmpty( objectiveVariableEntities ) ) {
                Map< String, String > map = new HashMap<>();
                for ( ObjectiveVariableEntity objectiveVariableEntity : objectiveVariableEntities ) {
                    map.put( objectiveVariableEntity.getLabel() + "_goal", objectiveVariableEntity.getGoal() );
                }
                goals = JsonUtils.toJson( map );
            }
            return goals;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare goals with label.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     *
     * @return the string
     */
    private String prepareGoalsWithLabel( EntityManager entityManager, String id ) {
        String goals = null;
        List< ObjectiveVariableEntity > objectiveVariableEntities = ( List< ObjectiveVariableEntity > ) variableDAO.getAllObjectiveVariablesByWorkflowId(
                entityManager, id );
        if ( CollectionUtils.isNotEmpty( objectiveVariableEntities ) ) {
            Map< String, String > map = new HashMap<>();
            for ( ObjectiveVariableEntity objectiveVariableEntity : objectiveVariableEntities ) {

                if ( map.containsKey( objectiveVariableEntity.getLabel() ) && objectiveVariableEntity.getGoal() != null
                        && !objectiveVariableEntity.getGoal().isEmpty() ) {
                    map.put( objectiveVariableEntity.getLabel(), objectiveVariableEntity.getGoal() );
                } else if ( !map.containsKey( objectiveVariableEntity.getLabel() ) ) {
                    map.put( objectiveVariableEntity.getLabel(), objectiveVariableEntity.getGoal() );
                }
            }
            goals = JsonUtils.toJson( map );
        }
        return goals;
    }

    /**
     * Gets the optimization py file path from algo config ofcheme options.
     *
     * @param schemeOptions
     *         the scheme options
     *
     * @return the optimization py file path from algo config ofcheme options
     */
    private String getOptimizationPyFilePathFromAlgoConfigOfchemeOptions( WFSchemeEntity schemeOptions ) {
        try {
            JSONObject jsonAlgoConfig;
            JSONParser parser = new JSONParser();

            try ( InputStream decriptStreamn = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                    new File( PropertiesManager.getVaultPath() + schemeOptions.getAlgoConfig().getFilePath() ),
                    prepareEncryptionDecryptionDTO( schemeOptions.getAlgoConfig().getEncryptionDecryption() ) ) ) {
                jsonAlgoConfig = ( JSONObject ) parser.parse( new InputStreamReader( decriptStreamn ) );
            }

            return ( ( String ) jsonAlgoConfig.get( "pythonFilePath" ) ).replace( ConstantsFileProperties.REGEX_REPLACE_KARAF_SCRIPT_PATH,
                    PropertiesManager.getScriptsPath() );
        } catch ( IOException | ParseException e ) {
            log.error( "please add <pythonFilePath> key in optimization schema algo file", e );
            ExceptionLogger.logException( e, e.getClass() );
        }
        return null;
    }

    /**
     * Prepare scheme optimization DTO.
     *
     * @param userId
     *         the user id
     * @param jobParameters
     *         the job parameters
     * @param designVariableList
     *         the design variable list
     * @param objectiveVariableList
     *         the objective variable list
     * @param user
     *         the user
     *
     * @return the scheme optimization DTO
     */
    private SchemeOptimizationDTO prepareSchemeOptimizationDTO( String userId, JobParameters jobParameters,
            List< ScanFileDTO > designVariableList, List< ScanFileDTO > objectiveVariableList, final UserEntity user ) {
        final JobParametersLocationModel jobParametersLocationModel = new JobParametersLocationModel( jobParameters, user.getUserUid(),
                null );

        SchemeOptimizationDTO pythonParameters = new SchemeOptimizationDTO();
        pythonParameters.setDesignVariableList( designVariableList );
        pythonParameters.setObjectiveVariableList( objectiveVariableList );
        pythonParameters.setJobParametersLocationModel( jobParametersLocationModel );
        pythonParameters.setToken( jobParameters.getRequestHeaders().getToken() );
        pythonParameters.setUserId( userId );
        return pythonParameters;
    }

    /**
     * Prepare design var and objective var.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableList
     *         the design variable list
     * @param objectiveVariableList
     *         the objective variable list
     * @param workflowModel
     *         the workflow model
     * @param parserIdList
     *         the parser id list
     * @param templateVariableList
     *         the template variable list
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     */
    private void prepareDesignVarAndObjectiveVar( EntityManager entityManager, List< ScanFileDTO > designVariableList,
            List< ScanFileDTO > objectiveVariableList, WorkflowModel workflowModel, List< String > parserIdList,
            Map< String, Object > templateVariableList, String userId, String uid ) {
        if ( null != workflowModel.getNodes() ) {
            for ( WorkflowElement node : workflowModel.getNodes() ) {
                if ( node.getData().getKey().equals( "wfe_io" ) ) {
                    for ( Field f : node.getData().getFields() ) {
                        prepareDesignVarList( entityManager, designVariableList, f, parserIdList, templateVariableList, userId, uid );
                        prepareObjectiveVarList( entityManager, objectiveVariableList, f, parserIdList, uid );
                    }
                }
            }
        }
    }

    /**
     * Prepare objective var list.
     *
     * @param entityManager
     *         the entity manager
     * @param objectiveVariableList
     *         the objective variable list
     * @param f
     *         the f
     * @param parserIdList
     *         the parser id list
     * @param uid
     *         the uid
     */
    private void prepareObjectiveVarList( EntityManager entityManager, List< ScanFileDTO > objectiveVariableList, Field f,
            List< String > parserIdList, String uid ) {

        if ( f.getTemplateType() != null && f.getTemplateType().equalsIgnoreCase( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {

            if ( f.getType().equalsIgnoreCase( FieldTypes.REGEX_FILE.getType() ) ) {
                ScanFileDTO designVar = JsonUtils.jsonToObject( JsonUtils.toJson( f.getValue() ), ScanFileDTO.class );
                objectiveVariableList.add( designVar );
            } else if ( f.getType().equalsIgnoreCase( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                UUID selectionId = UUID.fromString( ( String ) f.getValue() );
                List< RegexEntity > regexEntities = regexManager.getRegexDAO().getRegexListBySelectionId( entityManager, selectionId );
                if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                    objectiveVariableList.add( prepareScanFileDTOFromRegexServerObject( entityManager, uid, selectionId, regexEntities ) );
                }
            } else if ( f.getType().equalsIgnoreCase( FieldTypes.OBJECT_PARSER.getType() ) ) {
                // parking area for parser objective variable support
                parserIdList.add( String.valueOf( f.getValue() ) );
            }
        }
    }

    /**
     * Prepare design var list.
     *
     * @param entityManager
     *         the entity manager
     * @param designVariableList
     *         the design variable list
     * @param f
     *         the f
     * @param parserIdList
     *         the parser id list
     * @param templateVariableList
     *         the template variable list
     * @param userId
     *         the user id
     * @param uid
     *         the uid
     */
    private void prepareDesignVarList( EntityManager entityManager, List< ScanFileDTO > designVariableList, Field f,
            List< String > parserIdList, Map< String, Object > templateVariableList, String userId, String uid ) {

        if ( FieldTemplates.DESIGN_VARIABLE.getValue().equalsIgnoreCase( f.getTemplateType() ) ) {

            if ( f.getType().equalsIgnoreCase( FieldTypes.REGEX_FILE.getType() ) ) {
                ScanFileDTO designVar = JsonUtils.jsonToObject( JsonUtils.toJson( f.getValue() ), ScanFileDTO.class );
                designVariableList.add( designVar );
            } else if ( f.getType().equalsIgnoreCase( FieldTypes.REGEX_SCAN_SERVER.getType() ) ) {
                UUID selectionId = UUID.fromString( ( String ) f.getValue() );
                List< RegexEntity > regexEntities = regexManager.getRegexDAO().getRegexListBySelectionId( entityManager, selectionId );
                if ( CollectionUtils.isNotEmpty( regexEntities ) ) {
                    designVariableList.add( prepareScanFileDTOFromRegexServerObject( entityManager, uid, selectionId, regexEntities ) );
                }
            } else if ( f.getType().equalsIgnoreCase( FieldTypes.OBJECT_PARSER.getType() ) ) {
                parserIdList.add( String.valueOf( f.getValue() ) );
            } else if ( f.getType().equalsIgnoreCase( FieldTypes.TEMPLATE_FILE.getType() ) ) {

                TemplateFileDTO templateFile = JsonUtils.jsonToObject( JsonUtils.toJson( f.getValue() ), TemplateFileDTO.class );
                File temp = new File( OSValidator.convertPathToRelitiveOS( templateFile.getFile() ) );
                templateFile.setFile( OSValidator.convertPathToRelitiveOS(
                        PropertiesManager.getUserStagingPath( userId ) + File.separator + temp.getName() ) );

                for ( TemplateVariableDTO scanVar : templateFile.getVariables() ) {

                    try {
                        templateVariableList.put( scanVar.getVariableName(),
                                Files.readAllLines( Paths.get( templateFile.getFile() ) ).get( Integer.parseInt( scanVar.getLineNumber() ) )
                                        .substring( Integer.parseInt( scanVar.getStart() ), Integer.parseInt( scanVar.getEnd() ) ) );

                    } catch ( IOException e ) {
                        log.error( e.getMessage(), e );
                    }

                }

            }
        }

    }

    /**
     * Run scheme DOE.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param jobParameters
     *         the job parameters
     * @param savedMasterJob
     *         the saved master job
     * @param jobName
     *         the job name
     */
    private void runSchemeDOE( String userIdFromGeneralHeader, JobParameters jobParameters, Job savedMasterJob, String jobName ) {
        List< Map< String, Object > > designSummary = getExecutionManager().getDesignSummary( jobParameters.getServer(),
                jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );

        List< ObjectiveVariableDTO > objectiveVar = getExecutionManager().getObjectiveVariable( jobParameters.getServer(),
                jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );
        jobParameters.setObjectiveVariables( objectiveVar );
        refreshSchemeSharedDirectory( jobParameters.getJobRunByUserUID(), String.valueOf( jobParameters.getWorkflow().getId() ) );
        RunSchemeDoeThread runSchemeDoeThread = new RunSchemeDoeThread( userIdFromGeneralHeader, jobName, jobParameters, savedMasterJob,
                designSummary, this );
        SusExecutorUtil.threadPoolExecutorService.schemeExecute( runSchemeDoeThread, savedMasterJob.getId() );
    }

    /**
     * Prepare job model.
     *
     * @param jobName
     *         the job name
     * @param description
     *         the description
     * @param workflow
     *         the workflow
     *
     * @return the job
     */
    private Job prepareJobModel( String jobName, String description, WorkflowDTO workflow ) {
        Status status = new Status( WorkflowStatus.COMPLETED );
        final Job jobImpl = new JobImpl();
        jobImpl.setDescription( description );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( UUID.fromString( workflow.getId() ) );
        jobImpl.setWorkflowName( workflow.getName() );
        jobImpl.setOs( OSValidator.getOperationSystemName() );
        try {
            jobImpl.setMachine( InetAddress.getLocalHost().getHostName() );
        } catch ( final UnknownHostException e ) {
            log.error( "Machine Name Error: " + e.getMessage(), e );
        }
        jobImpl.setStatus( status );
        jobImpl.setWorkflowVersion( workflow.getVersion() );
        return jobImpl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSchemeCategory( EntityManager entityManager, String workflowId, String userIdFromGeneralHeader ) {
        Map< String, String > options = getOptionScheme( entityManager, userIdFromGeneralHeader, workflowId, TAB_KEY_SCHEME_OPTIONS );
        return options.containsKey( "category" ) ? Integer.parseInt( options.get( "category" ) ) : 0;
    }

    /**
     * Gets the scheme max jobs to run.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param jobParameters
     *         the job parameters
     *
     * @return the scheme max jobs to run
     */
    public String getSchemeMaxJobsToRun( EntityManager entityManager, String workflowId, String userIdFromGeneralHeader,
            JobParameters jobParameters ) {
        Map< String, String > options = getOptionScheme( entityManager, userIdFromGeneralHeader, workflowId, TAB_KEY_SCHEME_OPTIONS,
                jobParameters.getWorkflow().getVersion().getId() );
        if ( options.containsKey( "maxJobs" ) && options.get( "maxJobs" ) != null && !options.get( "maxJobs" ).isEmpty() ) {
            return options.get( "maxJobs" );
        }
        return "0";
    }

    /**
     * Gets the option scheme other than max job.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow id
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param jobParameters
     *         the job parameters
     *
     * @return the option scheme other than max job
     */
    private String getOptionSchemeOtherThanMaxJob( EntityManager entityManager, String workflowId, String userIdFromGeneralHeader,
            JobParameters jobParameters ) {
        Map< String, String > options = getOptionScheme( entityManager, userIdFromGeneralHeader, workflowId, TAB_KEY_SCHEME_OPTIONS,
                jobParameters.getWorkflow().getVersion().getId() );
        return JsonUtils.convertMapToString( options );
    }

    /**
     * Gets the scheme schema DAO.
     *
     * @return the scheme schema DAO
     */
    public SchemeSchemaDAO getSchemeSchemaDAO() {
        return schemeSchemaDAO;
    }

    /**
     * Sets the scheme schema DAO.
     *
     * @param schemeSchemaDAO
     *         the new scheme schema DAO
     */
    public void setSchemeSchemaDAO( SchemeSchemaDAO schemeSchemaDAO ) {
        this.schemeSchemaDAO = schemeSchemaDAO;
    }

    @Override
    public Map< String, String > getOptionScheme( EntityManager entityManager, String userId, String workflowId, String schemeOptions,
            int workflowVersionId ) {
        SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                workflowVersionId, schemeOptions, UUID.fromString( userId ) );
        Map< String, String > optionScheme = new HashMap<>();
        if ( null != entity ) {
            optionScheme = ( Map< String, String > ) JsonUtils.jsonToMap( entity.getContent(), optionScheme );
        }
        return optionScheme;
    }

    /**
     * Gets the option scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowId
     *         the workflow id
     * @param schemeOptions
     *         the scheme options
     *
     * @return the option scheme
     */
    private Map< String, String > getOptionScheme( EntityManager entityManager, String userId, String workflowId, String schemeOptions ) {
        WorkflowEntity workflowEntity = prepareWorkflowEntity( getWorkflowById( entityManager, UUID.fromString( userId ), workflowId ) );
        SchemeSchemaEntity entity = schemeSchemaDAO.getSchemeSchemaByWorkflowIdAndName( entityManager, UUID.fromString( workflowId ),
                workflowEntity.getVersionId(), schemeOptions, UUID.fromString( userId ) );
        Map< String, String > optionScheme = new HashMap<>();
        if ( null != entity ) {
            optionScheme = ( Map< String, String > ) JsonUtils.jsonToMap( entity.getContent(), optionScheme );
        }
        return optionScheme;
    }

    /**
     * Gets the import workflow form.
     *
     * @param parentId
     *         the parent id
     *
     * @return the import workflow form
     */
    @Override
    public UIForm getImportWorkflowForm( String parentId ) {
        ImportWorkFlowDTO workflowDTO = new ImportWorkFlowDTO();
        workflowDTO.setParentId( UUID.fromString( parentId ) );

        List< UIFormItem > itemsUI = GUIUtils.prepareForm( true, workflowDTO );
        for ( UIFormItem uiFormItem : itemsUI ) {
            if ( uiFormItem.getName().equalsIgnoreCase( "file" ) ) {
                uiFormItem.setLabel( "Select File" );
                uiFormItem.setType( "file-upload" );
                uiFormItem.setSelectable( "status" );
                uiFormItem.setUpdateField( "name" );
                setRulesAndMessageOnUI( uiFormItem );
            }
        }
        return GUIUtils.createFormFromItems( itemsUI );
    }

    /**
     * Import workflow.
     *
     * @param userID
     *         the user ID
     * @param json
     *         the json
     *
     * @return true, if successful
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     * @throws ParseException
     *         the parse exception
     */
    @Override
    public boolean importWorkflow( String userID, String json ) throws IOException, ParseException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectFileDTO importWorkFlowDTO = JsonUtils.jsonToObject( json, DataObjectFileDTO.class );
            JSONParser parser = new JSONParser();

            Object obj;
            try ( InputStream decritedStreamDromVault = EncryptAndDecryptUtils.decryptFileIfEncpEnabledAndReturnStream(
                    new File( PropertiesManager.getVaultPath() + importWorkFlowDTO.getFile().getPath() ),
                    importWorkFlowDTO.getFile().getEncryptionDecryption() ) ) {

                obj = parser.parse( new InputStreamReader( decritedStreamDromVault ) );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Invalid Json File!" );
            }

            JSONObject jsonObject = ( JSONObject ) obj;
            LatestWorkFlowDTO latestWorkFlowDTO = JsonUtils.jsonToObject( jsonObject.toString(), LatestWorkFlowDTO.class );
            latestWorkFlowDTO.setName( importWorkFlowDTO.getName() );
            latestWorkFlowDTO.setDescription( importWorkFlowDTO.getDescription() );
            if ( validateWorkflow( latestWorkFlowDTO, true ) ) {
                LatestWorkFlowDTO savedWf = saveWorkflow( entityManager, UUID.fromString( userID ),
                        importWorkFlowDTO.getParentId().toString(), latestWorkFlowDTO, jsonObject.toString() );
                updateWorkflow( entityManager, UUID.fromString( userID ), savedWf.getId().toString(), savedWf, json, true );
                return true;
            } else {
                return false;
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Sets the rules and message on UI.
     *
     * @param uiFormItem
     *         the new rules and message on UI
     */
    private void setRulesAndMessageOnUI( UIFormItem uiFormItem ) {
        Map< String, Object > rules = new HashMap<>();
        Map< String, Object > message = new HashMap<>();

        rules.put( REQUIRED, true );
        message.put( REQUIRED, MUST_CHOSE_OPTION );
        uiFormItem.setRules( rules );
        uiFormItem.setMessages( message );
    }

    /**
     * Gets the workflow validated.
     *
     * @param userId
     *         the user id
     * @param wfId
     *         the wf id
     * @param versionId
     *         the version id
     * @param token
     *         the token
     *
     * @return the workflow validated
     */
    @Override
    public WorkflowValidationDTO getWorkflowValidated( String userId, UUID wfId, String versionId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            WorkflowDTO workflowDTO;
            try {
                workflowDTO = getWorkflowByIdAndVersionId( entityManager, UUID.fromString( userId ), wfId.toString(),
                        Integer.parseInt( versionId ) );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                throw new SusException( "Save Workflow First to Validate." );
            }

            LatestWorkFlowDTO latestWorkFlow = prepareLatestWorkflowFromWorkflowDTO( workflowDTO );

            JobParameters jobParameters = getJobParametersByWorkflowId( entityManager, userId, wfId, token );

            ValidateWorkflowModel validateWF = new ValidateWorkflowModel();
            Map< String, String > elements = new HashMap<>();
            Map< String, String > elementsFeilds = new HashMap<>();

            validateWorkflowStandards( latestWorkFlow, elements );
            validateWorkFlowElementsAndFieldsAndReserveKeywords( latestWorkFlow, elementsFeilds );

            checkWfManagerPermission( entityManager, userId, elements );
            checkWfPermission( entityManager, userId, wfId, elements );
            checkIfWfIsFileBased( jobParameters, elements );

            validateWF.setFields( new ValidateWorkflowFeildsModel( elementsFeilds ) );
            validateWF.setElements( elements );

            WorkflowValidationDTO validate = new WorkflowValidationDTO();
            validate.setValidate( true );

            if ( !elements.isEmpty() || !elementsFeilds.isEmpty() ) {
                validate.setValidate( false );
                validate.setInfo( Collections.singletonMap( latestWorkFlow.getName(), validateWF ) );
                return validate;
            }

            return validate;
        } finally {
            entityManager.close();
        }

    }

    /**
     * Validate work flow elements and fields and reserve keywords.
     *
     * @param workflowDto
     *         the workflow dto
     * @param elementsFeilds
     *         the elements feilds
     */
    private void validateWorkFlowElementsAndFieldsAndReserveKeywords( LatestWorkFlowDTO workflowDto,
            Map< String, String > elementsFeilds ) {
        try {
            if ( workflowDto.getElements() != null ) {

                if ( CollectionUtils.isEmpty( ( Collection< ? > ) workflowDto.getElements().get( NODES ) ) ) {
                    elementsFeilds.put( "Workflow", MessagesUtil.getMessage( WFEMessages.WORKFLOW_HAS_NO_ELEMENTS ) );
                } else {

                    WorkflowModel workflowModel = JsonUtils.jsonToObject( JsonUtils.toJson( workflowDto.getElements() ),
                            WorkflowModel.class );

                    for ( WorkflowElement element : workflowModel.getNodes() ) {
                        if ( element.getData() != null ) {
                            for ( String reserveKey : WorkflowReserveKeywordsEnums.getAllReserveValues() ) {
                                if ( element.getData().getName().equalsIgnoreCase( reserveKey ) ) {
                                    elementsFeilds.put( element.getData().getKey(), element.getData().getName() + " : is Reserve Keyword" );
                                }
                            }
                        }
                    }
                }
            }
        } catch ( final Exception ex ) {
            log.error( ex.getMessage(), ex );
            throw new SusException( ex.getMessage() );
        }
    }

    /**
     * Check if wf is file based.
     *
     * @param jobParameters
     *         the job parameters
     * @param elements
     *         the elements
     */
    private void checkIfWfIsFileBased( JobParameters jobParameters, Map< String, String > elements ) {
        if ( jobParameters.isFileRun() ) {
            elements.put( "EXEXUTION", "Cannot run file based workflows on server." );
        }
    }

    /**
     * Check wf manager permission.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param elements
     *         the elements
     */
    private void checkWfManagerPermission( EntityManager entityManager, String userId, Map< String, String > elements ) {
        try {
            licenseManager.isLicenseAddedAgainstUserForModule( entityManager, SimuspaceFeaturesEnum.WORKFLOW_MANAGER.getKey(), userId );
        } catch ( final FeatureNotAllowedException e ) {
            log.error( e.getMessage(), e );
            elements.put( "License", "This User has no WF Manager License" );
        }
    }

    /**
     * Check wf permission.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param wfId
     *         the wf id
     * @param elements
     *         the elements
     */
    private void checkWfPermission( EntityManager entityManager, String userId, UUID wfId, Map< String, String > elements ) {
        if ( wfId != null ) {
            String perm = wfId + COLON + PermissionMatrixEnum.EXECUTE.getValue();
            if ( !permissionManager.isPermitted( entityManager, userId, perm ) ) {
                elements.put( "Permission", "This User Has No EXECUTE Rights" );
            }
        }
    }

    /**
     * Validate workflow standards.
     *
     * @param workflowDTO
     *         the workflow DTO
     * @param elements
     *         the elements
     */
    public void validateWorkflowStandards( LatestWorkFlowDTO workflowDTO, Map< String, String > elements ) {
        final Notification notification = new Notification();

        if ( StringUtils.isNullOrEmpty( workflowDTO.getName() ) ) {

            notification.addError( new Error( MessagesUtil.getMessage( WFEMessages.WORKFLOW_NAME_SHOULD_NOT_EMPTY ) ) );
        } else {
            notification.addNotification( StringUtils.validateFieldAndLength( workflowDTO.getName(),

                    ConstantsWorkFlowProps.NAME + ConstantsString.STANDARD_SEPARATOR + workflowDTO.getName(),
                    ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );

        }
        if ( StringUtils.isNotNullOrEmpty( workflowDTO.getDescription() ) ) {
            notification.addNotification(
                    StringUtils.validateFieldAndLength( workflowDTO.getDescription(), ConstantsWorkFlowProps.DESCRIPTION,
                            ConstantsLength.STANDARD_DESCRIPTION_LENGTH, true, false ) );
        }
        if ( StringUtils.isNotNullOrEmpty( workflowDTO.getComments() ) ) {
            notification.addNotification( StringUtils.validateFieldAndLength( workflowDTO.getComments(), ConstantsWorkFlowProps.COMMENTS,
                    ConstantsLength.STANDARD_COMMENT_LENGTH, true, false ) );
        }
        if ( !notification.getErrors().isEmpty() ) {

            for ( Error error : notification.getErrors() ) {
                elements.put( "ERROR", error.toString() );
            }
        }
    }

    /**
     * Gets the parser manager.
     *
     * @return the parser manager
     */
    public ParserManager getParserManager() {
        return parserManager;
    }

    /**
     * Sets the parser manager.
     *
     * @param parserManager
     *         the new parser manager
     */
    public void setParserManager( ParserManager parserManager ) {
        this.parserManager = parserManager;
    }

    /**
     * Gets the workflow dao.
     *
     * @return the workflow dao
     */

    @Override
    public WorkflowDAO getWorkflowDao() {
        return workflowDao;
    }

    /**
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
    }

    /**
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
    }

    /**
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    @Override
    public JobManager getJobManager() {
        return jobManager;
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
     * Gets the bmw cae bench DAO.
     *
     * @return the bmw cae bench DAO
     */
    public BmwCaeBenchDAO getBmwCaeBenchDAO() {
        return bmwCaeBenchDAO;
    }

    /**
     * Sets the bmw cae bench DAO.
     *
     * @param bmwCaeBenchDAO
     *         the new bmw cae bench DAO
     */
    public void setBmwCaeBenchDAO( BmwCaeBenchDAO bmwCaeBenchDAO ) {
        this.bmwCaeBenchDAO = bmwCaeBenchDAO;
    }

    /**
     * Gets the CB 2 entity list by selection id.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     *
     * @return the CB 2 entity list by selection id
     */
    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.server.manager.WorkflowManager#
     * getCB2EntityListBySelectionId(java.lang.String, java.lang.String)
     */
    @Override
    public List< ? > getCB2EntityListBySelectionId( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< Object > selectedCB2List = new ArrayList<>();

            SelectionEntity mainObjSelection = selectionManager.getSelectionDAO()
                    .getLatestObjectById( entityManager, SelectionEntity.class, UUID.fromString( selectionId ) );

            Set< SelectionItemEntity > list = mainObjSelection.getItems();
            if ( list != null && !list.isEmpty() ) {
                for ( SelectionItemEntity cb2Ref : list ) {
                    BmwCaeBenchEntity cb2ObjectEntity = bmwCaeBenchDAO.getLatestNonDeletedObjectById( entityManager,
                            UUID.fromString( cb2Ref.getItem() ) );
                    Map< String, Object > map = new HashMap<>();
                    map = ( Map< String, Object > ) JsonUtils.jsonToMap( JsonUtils.toJson( cb2ObjectEntity ), map );

                    if ( cb2Ref.getAdditionalAttributesJson() != null && !cb2Ref.getAdditionalAttributesJson().isEmpty() ) {
                        List< WorkFlowAdditionalAttributeDTO > additionalAttrib = JsonUtils.jsonToList(
                                cb2Ref.getAdditionalAttributesJson(), WorkFlowAdditionalAttributeDTO.class );
                        for ( WorkFlowAdditionalAttributeDTO additionalAttribent : additionalAttrib ) {
                            map.put( additionalAttribent.getName(), additionalAttribent );
                        }
                    }
                    selectedCB2List.add( map );
                }
            }
            return selectedCB2List;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveWorkflow( String userId, UUID srcSelectionId, UUID targetSelectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UUID > objectTargets = selectionManager.getSelectedIdsListBySelectionId( entityManager, targetSelectionId.toString() );
            if ( CollectionUtils.isEmpty( objectTargets ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.TARGET_ITEM_NOT_AVAILABE.getKey() ) );
            }
            SuSEntity singleSrc = susDAO.getLatestNonDeletedObjectById( entityManager, srcSelectionId );
            if ( singleSrc == null ) {
                return moveMultiObjects( entityManager, userId, srcSelectionId, objectTargets.get( 0 ) );
            } else {
                return moveSingleObject( entityManager, userId, srcSelectionId, objectTargets.get( 0 ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WfFieldsUiDTO > getActionScriptFieldsFromConfig() {
        return DynamicScriptsUtil.getDynamicScriptFieldsFromConfigByKey( DynamicScript.ACTION_SCRIPTS_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DynamicScript getActionScriptDetailsFromConfig( String name ) {
        return DynamicScriptsUtil.getDynamicScriptDetailsFromConfigByNameAndKey( name, DynamicScript.ACTION_SCRIPTS_KEY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< WfFieldsUiDTO > getPythonEnvironments() {
        List< WfFieldsUiDTO > fields = new ArrayList<>();
        // add default option
        fields.add( new WfFieldsUiDTO( DEFAULT, DEFAULT, null ) );
        List< PythonEnvironmentDTO > environments;
        try {
            environments = PropertiesManager.getPythonEnvironments();
            environments.forEach( env -> fields.add(
                    new WfFieldsUiDTO( env.getName().replace( ConstantsString.DOUBLE_QUOTE_STRING, ConstantsString.EMPTY_STRING ),
                            env.getName(), null ) ) );

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

        return fields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PythonEnvironmentDTO getPythonEnvironment( String envName ) {
        List< PythonEnvironmentDTO > envDTOs;
        try {
            envDTOs = PropertiesManager.getPythonEnvironments();
            for ( PythonEnvironmentDTO envDTO : envDTOs ) {
                if ( envDTO.getName() != null && envDTO.getName().equalsIgnoreCase( envName ) ) {
                    return envDTO;
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }

        return null;
    }

    /**
     * Prepare work flow in file.
     *
     * @param token
     *         the token
     * @param wfId
     *         the wf id
     *
     * @return the string
     */
    @Override
    public String prepareWorkFlowInFile( String token, UUID wfId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UUID userId = tokenManager.getUserIdByToken( entityManager, token );
            if ( userId == null ) {
                throw new SusException( "User not found against the token : " + token );
            }

            if ( !permissionManager.isManageable( entityManager, userId, wfId ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_MANAGE.getKey(), "this workflow" ) );
            }

            LatestWorkFlowDTO latestWorkFlowDTO = getWorkflowById( entityManager, userId, wfId );
            if ( latestWorkFlowDTO == null ) {
                throw new SusException( "Workflow not found." );
            }
            LatestWorkFlowDTO workFlowDTO = getNewWorkflowByIdAndVersionId( entityManager, userId, latestWorkFlowDTO.getId().toString(),
                    latestWorkFlowDTO.getVersion().getId() );
            workFlowDTO.setUserSignature( latestWorkFlowDTO.getUserSignature() );

            File workflowEntityAsFile = new File(
                    PropertiesManager.getDefaultServerTempPath() + File.separator + workFlowDTO.getName() + ".suswf" );

            String json = JsonUtils.toFilteredJson(
                    new String[]{ "name", "type", "lifeCycleStatus", "config", "createdOn", "createdBy", "modifiedOn", "modifiedBy",
                            "typeId", "runOnLocation", "runsOn", "token", "versions", "interactive", "jobs", "favorite", "executable",
                            "curentSimUserId", "actions", "categories", "workflowType", "masterJobId", "dummyMasterJobId" }, workFlowDTO );

            byte[] latestWorkFlowDTOAsBytes = json.getBytes( StandardCharsets.UTF_8 );
            try ( ByteArrayInputStream bais = new ByteArrayInputStream(
                    latestWorkFlowDTOAsBytes ); FileOutputStream fos = new FileOutputStream( workflowEntityAsFile ) ) {
                IOUtils.copy( bais, fos );
            } catch ( IOException e ) {
                log.error( e.getMessage(), e );
            }
            return workflowEntityAsFile.getAbsolutePath();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Move single object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the source selection id
     * @param targetId
     *         the target id
     *
     * @return true, if successful
     */
    private boolean moveSingleObject( EntityManager entityManager, String userId, UUID srcSelectionId, UUID targetId ) {
        validateTargetForSingleSrc( entityManager, userId, srcSelectionId, targetId );
        if ( removeObjectFromCurrentContainer( entityManager, srcSelectionId ) ) {
            Relation relation = new Relation( targetId, srcSelectionId );
            return ( susDAO.createRelation( entityManager, relation ) != null );
        } else {
            return false;
        }
    }

    /**
     * Move multiple objects.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the source selection id
     * @param targetId
     *         the target id
     *
     * @return true, if successful
     */
    private boolean moveMultiObjects( EntityManager entityManager, String userId, UUID srcSelectionId, UUID targetId ) {
        List< UUID > objectSrc = selectionManager.getSelectedIdsListBySelectionId( entityManager, srcSelectionId.toString() );
        if ( CollectionUtils.isEmpty( objectSrc ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SOURCE_ITEM_NOT_AVAILABE.getKey() ) );
        }
        validateTargetForMultiSrc( entityManager, userId, objectSrc, targetId );
        for ( UUID srcSelection : objectSrc ) {
            if ( removeObjectFromCurrentContainer( entityManager, srcSelection ) ) {
                Relation relation = new Relation( targetId, srcSelection );
                if ( susDAO.createRelation( entityManager, relation ) == null ) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove object from current container.
     *
     * @param entityManager
     *         the entity manager
     * @param srcSelectionId
     *         the source selection id
     *
     * @return true, if successful
     */
    private boolean removeObjectFromCurrentContainer( EntityManager entityManager, UUID srcSelectionId ) {
        final List< Relation > relationList = susDAO.getListByPropertyDesc( entityManager, ConstantsDAO.CHILD, srcSelectionId );
        List< Relation > containerObjectRelations = new ArrayList<>();
        for ( Relation relation : relationList ) {
            if ( !Integer.valueOf( relation.getType() ).equals( ConstantsInteger.INTEGER_VALUE_ONE ) ) {
                containerObjectRelations.add( relation ); // adding only container object relations, filter out link
                // relations
            }
        }
        return susDAO.deleteRelation( entityManager, containerObjectRelations );
    }

    /**
     * Validates Target for moving single object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the source selection id
     * @param targetId
     *         the target id
     */
    private void validateTargetForSingleSrc( EntityManager entityManager, String userId, UUID srcSelectionId, UUID targetId ) {
        if ( srcSelectionId.equals( targetId ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.SOURCE_OBJECT_AND_DESTINATION_OBJECT_CANNOT_BE_SAME.getKey() ) );
        }
        isPermittedtoWrite( entityManager, userId, srcSelectionId.toString() );
        isPermittedtoWrite( entityManager, userId, targetId.toString() );
        SuSEntity singleSrc = susDAO.getLatestNonDeletedObjectById( entityManager, srcSelectionId );
        SuSEntity target = susDAO.getLatestNonDeletedObjectById( entityManager, targetId );
        if ( null != target && !isNameUniqueAmongSiblings( entityManager, singleSrc.getName(), singleSrc,
                susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, target.getComposedId().getId() ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
        }
        if ( target instanceof ProjectEntity || target instanceof VariantEntity || target instanceof LibraryEntity
                || target instanceof ProjectOverviewEntity ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_WORKFLOW_PROJECT_TO_DATA_OBJECTS.getKey() ) );
        }
        // Cannot move object to its child
        List< SuSEntity > allParentEntities = getAllParents( entityManager, target );
        for ( SuSEntity parent : allParentEntities ) {
            if ( parent.equals( singleSrc ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_TO_CHILDREN_OBJECTS.getKey() ) );
            }
        }
    }

    /**
     * Validates Target for moving multi object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param srcSelectionId
     *         the source selection id
     * @param targetId
     *         the target id
     */
    private void validateTargetForMultiSrc( EntityManager entityManager, String userId, List< UUID > srcSelectionId, UUID targetId ) {
        isPermittedtoWrite( entityManager, userId, targetId.toString() );
        for ( UUID singleSelection : srcSelectionId ) {
            isPermittedtoWrite( entityManager, userId, singleSelection.toString() );
            if ( singleSelection.equals( targetId ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.SOURCE_OBJECT_AND_DESTINATION_OBJECT_CANNOT_BE_SAME.getKey() ) );
            }
        }
        List< SuSEntity > srcObjects = susDAO.getLatestNonDeletedObjectsByIds( entityManager, srcSelectionId );
        SuSEntity target = susDAO.getLatestNonDeletedObjectById( entityManager, targetId );
        if ( target instanceof ProjectEntity || target instanceof VariantEntity || target instanceof LibraryEntity
                || target instanceof ProjectOverviewEntity ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_WORKFLOW_PROJECT_TO_DATA_OBJECTS.getKey() ) );
        }
        // Cannot move object to its child
        List< SuSEntity > allParentEntities = getAllParents( entityManager, target );
        for ( SuSEntity parent : allParentEntities ) {
            for ( SuSEntity singleSrc : srcObjects ) {
                if ( parent.equals( singleSrc ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_TO_CHILDREN_OBJECTS.getKey() ) );
                }
            }
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
    private void isPermittedtoWrite( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_WRITE.getKey(), OBJECT ) );
        }
    }

    /**
     * Gets all the parents of an object.
     *
     * @param entityManager
     *         the entity manager
     * @param object
     *         the object
     *
     * @return list of all the parents
     */
    private List< SuSEntity > getAllParents( EntityManager entityManager, SuSEntity object ) {
        List< SuSEntity > allParentEntities = new ArrayList<>();
        getParentsRecursively( entityManager, susDAO.getParents( entityManager, object ), allParentEntities );
        return allParentEntities;
    }

    /**
     * Recursively adds all the parents to allParentEntities list Adds all parents i.e parents of parent till no parent left
     *
     * @param entityManager
     *         the entity manager
     * @param directParentEntities
     *         the direct parent entities
     * @param allParentEntities
     *         the all parent entities
     */
    private void getParentsRecursively( EntityManager entityManager, List< SuSEntity > directParentEntities,
            List< SuSEntity > allParentEntities ) {
        allParentEntities.addAll( directParentEntities );
        for ( SuSEntity parent : directParentEntities ) {
            if ( parent instanceof SystemContainerEntity ) {
                return;
            } else {
                directParentEntities = susDAO.getParents( entityManager, parent );
            }
        }
        getParentsRecursively( entityManager, directParentEntities, allParentEntities );
    }

    /**
     * Validate active location for job submission.
     *
     * @param jobLocation
     *         the job location
     */
    private void validateActiveLocationForJobSubmission( LocationDTO jobLocation ) {
        if ( jobLocation != null && jobLocation.getStatus() != null && jobLocation.getStatus().equalsIgnoreCase( INACTIVE ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.JOB_LOCATION_INACTIVE.getKey(), jobLocation.getName() ) );
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
     * Sets the sus DAO.
     *
     * @param susDAO
     *         the new sus DAO
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
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
     * Sets the permission manager.
     *
     * @param permissionManager
     *         the new permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

    /**
     * Gets the link DAO.
     *
     * @return the link DAO
     */
    public LinkDAO getLinkDAO() {
        return linkDAO;
    }

    /**
     * Sets the link DAO.
     *
     * @param linkDAO
     *         the new link DAO
     */
    public void setLinkDAO( LinkDAO linkDAO ) {
        this.linkDAO = linkDAO;
    }

    /**
     * Gets the location manager.
     *
     * @return the location manager
     */
    public LocationManager getLocationManager() {
        return locationManager;
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
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
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
     * Gets the regex manager.
     *
     * @return the regex manager
     */
    public RegexManager getRegexManager() {
        return regexManager;
    }

    /**
     * Sets the regex manager.
     *
     * @param regexManager
     *         the new regex manager
     */
    public void setRegexManager( RegexManager regexManager ) {
        this.regexManager = regexManager;
    }

    /**
     * Gets the template manager.
     *
     * @return the template manager
     */
    public TemplateManager getTemplateManager() {
        return templateManager;
    }

    /**
     * Sets the template manager.
     *
     * @param templateManager
     *         the new template manager
     */
    public void setTemplateManager( TemplateManager templateManager ) {
        this.templateManager = templateManager;
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
     * {@inheritDoc}
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    /**
     * Sets variable dao.
     *
     * @param variableDAO
     *         the variable dao
     */
    public void setVariableDAO( VariableDAO variableDAO ) {
        this.variableDAO = variableDAO;
    }

    /**
     * Gets the translation DAO.
     *
     * @return the translation DAO
     */
    public TranslationDAO getTranslationDAO() {
        return translationDAO;
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
     * Gets the ShapeModule Manager.
     *
     * @return the ShapeModule Manager
     */
    public ShapeModuleManager getShapeModuleManager() {
        return shapeModuleManager;
    }

    /**
     * Sets ShapeModule Manager.
     *
     * @param ShapeModule
     *         Manager the ShapeModule Manager
     */
    public void setShapeModuleManager( ShapeModuleManager shapeModuleManager ) {
        this.shapeModuleManager = shapeModuleManager;
    }

    /**
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setDocumentDAO( DocumentDAO documentDAO ) {
        this.documentDAO = documentDAO;
    }

    /**
     * Sets document dao.
     *
     * @param documentDAO
     *         the document dao
     */
    public void setJobDao( JobDAO jobDao ) {
        this.jobDao = jobDao;
    }

}