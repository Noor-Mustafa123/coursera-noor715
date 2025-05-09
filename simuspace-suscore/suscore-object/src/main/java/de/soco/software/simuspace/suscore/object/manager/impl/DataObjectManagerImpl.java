package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

import lombok.extern.log4j.Log4j;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.AuditTrailRelationType;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsImageFileTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsMode;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsProjectOverview;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUrlViews;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.UIColumnType;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.CommonLocationDTO;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.ValueUnitDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.BindVisibility;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.Renderer;
import de.soco.software.simuspace.suscore.common.ui.SelectionResponseUI;
import de.soco.software.simuspace.suscore.common.ui.SubTabsItem;
import de.soco.software.simuspace.suscore.common.ui.SubTabsUI;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.ConfigUtil;
import de.soco.software.simuspace.suscore.common.util.CsvUtils;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MapFilterUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.SearchUtil;
import de.soco.software.simuspace.suscore.common.util.SuSVaultUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDataObjectDTO;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.StatusConfigDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.AclEntryEntity;
import de.soco.software.simuspace.suscore.data.entity.AclObjectIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AclSecurityIdentityEntity;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.DataDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectCurveEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectFileEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectMovieEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPDFEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPredictionModelEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectTraceEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectValueEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.GroupEntity;
import de.soco.software.simuspace.suscore.data.entity.LibraryEntity;
import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.ReportEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionEntity;
import de.soco.software.simuspace.suscore.data.entity.SelectionItemEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.TranslationEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.AuditLogManager;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.manager.impl.base.ContextMenuManagerImpl;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.data.model.DataDashboardDTO;
import de.soco.software.simuspace.suscore.data.model.DataObject3DceetronDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectCurveDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDashboardDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectFileDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectImageDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectMovieDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectTraceDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;
import de.soco.software.simuspace.suscore.data.model.LibraryDTO;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ModelFilesDTO;
import de.soco.software.simuspace.suscore.data.model.MovieSources;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.model.ReportDTO;
import de.soco.software.simuspace.suscore.data.model.SusDTO;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.executor.service.ThreadPoolExecutorService;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.object.dao.DataDAO;
import de.soco.software.simuspace.suscore.object.manager.DataDashboardManager;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.manager.DataObjectManager;
import de.soco.software.simuspace.suscore.object.manager.DataProjectManager;
import de.soco.software.simuspace.suscore.object.manager.PreviewManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectBaseManager;
import de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO;
import de.soco.software.simuspace.suscore.object.model.MetaDataEntryDTO;
import de.soco.software.simuspace.suscore.object.model.ObjectMetaDataDTO;
import de.soco.software.simuspace.suscore.object.model.ProjectType;
import de.soco.software.simuspace.suscore.object.threads.DeleteObjectThread;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.simuspace.suscore.object.utility.OverviewPluginUtil;
import de.soco.software.simuspace.suscore.object.utility.SIBaseUnitConverter;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.ManageObjectDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.SystemWorkflow;
import de.soco.software.simuspace.workflow.model.impl.JobImpl;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.suscore.jsonschema.model.UnitsFamily;
import de.soco.software.suscore.jsonschema.model.UnitsList;

@Log4j
public class DataObjectManagerImpl extends SuSObjectBaseManager implements DataObjectManager {

    /**
     * The Constant TRACE_CAPS.
     */
    private static final String TRACE_CAPS = "Trace";

    /**
     * The Constant TRACE.
     */
    private static final String TRACE = "trace";

    /**
     * The Constant PREVIEW_CAPS.
     */
    private static final String PREVIEW_CAPS = "Preview";

    /**
     * The Constant PREVIEW.
     */
    private static final String PREVIEW = "preview";

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
     * The Constant FILE.
     */
    private static final String FILE = "file";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant OBJECT_METHOD_SET_ID.
     */
    private static final String OBJECT_METHOD_SET_ID = "setId";

    /**
     * The Constant CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN.
     */
    private static final String CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN = "value";

    /**
     * The Constant KEY.
     */
    private static final String KEY = "key";

    /**
     * The Constant JOB_NAME_DELETE_OBJECT.
     */
    private static final String JOB_NAME_DELETE_OBJECT = "Delete object: ";

    /**
     * The Constant DELETE_OBJECT_DESCRIPTION.
     */
    private static final String DELETE_OBJECT_JOB_DESCRIPTION = "Job to delete object and its dependencies";

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
     * The Constant OBJECT_METHOD_SET_CUSTOM_ATTRIBUTES_DTO.
     */
    private static final String OBJECT_METHOD_SET_CUSTOM_ATTRIBUTES_DTO = "setCustomAttributesDTO";

    /**
     * The Constant LIST_FIRST_INDEX.
     */
    private static final int LIST_FIRST_INDEX = 0;

    /**
     * The Constant OBJECT_FIELD_NAME_PARENT_ID.
     */
    private static final String METHOD_NAME_GET_NAME = "getName";

    /**
     * The Constant METHOD_DESCRIPTION_GET_NAME.
     */
    private static final String METHOD_DESCRIPTION_GET_NAME = "getDescription";

    /**
     * The Constant METHOD_GET_JOB_ID.
     */
    private static final String METHOD_GET_JOB_ID = "getJobId";

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
     * The Constant BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN.
     */
    public static final String BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN = "/data/object/ui/create/{parentId}/type/{typeId}/plugin/{__value__}";

    /**
     * The Constant BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN.
     */
    public static final String BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN_AND_CONFIG = "/data/object/ui/create/{parentId}/type/{typeId}/plugin/{plugin}/config/{__value__}";

    /**
     * The Constant OBJECT_FIELD_NAME_PARENT_ID.
     */
    private static final String OBJECT_FIELD_NAME_PARENT_ID = "parentId";

    /**
     * The Constant PARAM_PROJECT_ID.
     */
    private static final String PARAM_PROJECT_ID = "{projectId}";

    /**
     * The Constant FIELD_NAME_OBJECT_STATUS.
     */
    public static final String FIELD_NAME_OBJECT_STATUS = "status";

    /**
     * The constant BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE.
     */
    private static final String BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE = "/data/object/{objectId}/type/{typeId}/translation/{__value__}/ui/create";

    /**
     * The constant BIND_FROM_URL_FOR_OBJECT_TRANSLATION_UPDATE.
     */
    private static final String BIND_FROM_URL_FOR_OBJECT_TRANSLATION_UPDATE = "/data/object/{objectId}/type/{typeId}/translation/{__value__}/ui/update";

    /**
     * The Constant SELECT_TRANSLATION_LABEL.
     */
    private static final String SELECT_TRANSLATION_LABEL = "3000213x4";

    /**
     * The Constant ENTITY_CLASS_FIELD_NAME.
     */
    private static final String ENTITY_CLASS_FIELD_NAME = "ENTITY_CLASS";

    /**
     * The Constant PREPARE_PERMISSION_FORM.
     */
    private static final String PREPARE_PERMISSION_FORM = "prepare resource permission form";

    /**
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant GROUP_SELECTION_PATH.
     */
    private static final String GROUP_SELECTION_PATH = "system/permissions/group";

    /**
     * The prefix to be used for metadata file names.
     */
    private static final String METADATA_FILE_NAME_PREFIX = "OBJ_METADATA_";

    /**
     * The Constant PLUGIN_OBJECT.
     */
    private static final String PLUGIN_OBJECT = "plugin_object";

    /**
     * The Constant INCLUDE_LABEL.
     */
    public static final String INCLUDE_LABEL = "Include Sub-Objects";

    /**
     * The Constant NEW_STATUS_LABEL.
     */
    public static final String NEW_STATUS_LABEL = "New Status";

    /**
     * The Constant USER_SELECTION_PATH.
     */
    private static final String USER_SELECTION_PATH = "system/user";

    /**
     * The constant USER_SELECTION_CONNECTED_TABLE_LABEL.
     */
    public static final String USER_SELECTION_CONNECTED_TABLE_LABEL = "userUid";

    /**
     * The constant GROUP_SELECTION_CONNECTED_TABLE_LABEL.
     */
    public static final String GROUP_SELECTION_CONNECTED_TABLE_LABEL = "name";

    /**
     * The Constant USER_KEY.
     */
    private static final String USER_KEY = "user";

    /**
     * The Constant BIND_FROM_URL_FOR_PROJECT_CUSTOMATTRIBUTE_VALUE.
     */
    private static final String BIND_FROM_URL_FOR_PROJECT_CUSTOMATTRIBUTE_VALUE = "/data/project/ui/customattribute/{__value__}";

    /**
     * The Constant SELECTION_TYPE_TABLE.
     */
    private static final String SELECTION_TYPE_TABLE = FieldTypes.CONNECTED_TABLE.getType();

    /**
     * The Constant GROUP_KEY.
     */
    private static final String GROUP_KEY = "group";

    /**
     * The Constant SLECTION.
     */
    private static final String SELECTION = "selection";

    /**
     * The Constant INHERIT_FALSE.
     */
    public static final String INHERIT_FALSE = "0";

    /**
     * The Constant INHERIT_TRUE.
     */
    private static final String INHERIT_TRUE = "1";

    /**
     * The Constant BULK_JOB_NAME_DELETE_OBJECT.
     */
    private static final String BULK_JOB_NAME_DELETE_OBJECT = "Bulk Objects are being deleted";

    /**
     * The Constant LOCAL_CONTEXT_URL.
     */
    private static final String LOCAL_CONTEXT_URL = "put/data/project/{projectId}/sync/{selectionId}";

    /**
     * The Constant CHANGE_TYPE_CONTEXT_URL.
     */
    private static final String CHANGE_TYPE_CONTEXT_URL = "data/project/{projectId}/selection/{selectionId}/changetype";

    /**
     * The constant PROJECT_DASHBOARD_CACHE_UPDATE_URL.
     */
    public static final String PROJECT_DASHBOARD_CACHE_UPDATE_URL = "/api/dashboard/project/{projectId}/update";

    /**
     * The constant PROJECT_DASHBOARD_CACHE_UPDATE_URL.
     */
    public static final String PST_DASHBOARD_FILES_CREATE_URL = "/api/dashboard/pst/{projectId}/planning/create";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "{selectionId}";

    /**
     * The Constant TITLE_UPLOAD.
     */
    private static final String TITLE_UPLOAD_SYNC = "Sync Upload";

    /**
     * The Constant CHANGE_TYPE.
     */
    private static final String CHANGE_TYPE = "Change Type";

    /**
     * The Constant URL_TYPE.
     */
    private static final String URL_TYPE = "urlType";

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
     * The Constant DEFAULT.
     */
    private static final String DEFAULT = "Default";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_DOCUMENT_UPLOAD = "api/document/upload";

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
     * The data DAO.
     */
    private DataDAO dataDAO;

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
     * The audit log manager.
     */
    private AuditLogManager auditLogManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private LinkDAO linkDao;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Data project manager.
     */
    private DataProjectManager dataProjectManager;

    /**
     * The Data dashboard manager.
     */
    private DataDashboardManager dataDashboardManager;

    /**
     * The Data manager.
     */
    private DataManager dataManager;

    /**
     * The Preview manager.
     */
    private PreviewManager previewManager;

    /**
     * Gets the image format property.
     *
     * @return the image format property
     */
    private List< String > getImageFormatProperty() {
        return PropertiesManager.getImageMovieFormatByKey( ConstantsFileProperties.IMAGE_FORMAT );
    }

    /**
     * Gets the movie format property.
     *
     * @return the movie format property
     */
    private List< String > getMovieFormatProperty() {
        return PropertiesManager.getImageMovieFormatByKey( ConstantsFileProperties.MOVIE_FORMAT );
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
     * Populate parentConfig drop down options.
     *
     * @param uiFormItem
     *         the ui form item
     * @param isWorkflowProject
     *         the is workflow project
     * @param parentConfig
     *         the parent config
     * @param inheritConfig
     *         the inherit config
     */
    private void populateConfigDropdownOptions( UIFormItem uiFormItem, boolean isWorkflowProject, String parentConfig,
            boolean inheritConfig ) {
        Map< String, String > map = new HashMap<>();
        List< ConfigUtil.Config > validOptions;

        if ( isWorkflowProject ) {
            validOptions = configManager.getMasterConfigurationFileNamesForWorkFlows();
        } else if ( !inheritConfig ) {
            validOptions = configManager.getMasterConfigurationFileNames();
        } else {
            validOptions = List.of(
                    new ConfigUtil.Config( ConfigUtil.fileByLabel( configManager.getMasterConfigurationFileNames(), parentConfig ),
                            parentConfig ) );
        }

        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( ConfigUtil.Config op : validOptions ) {
                map.put( op.file(), op.label() );
            }
            SelectFormItem selectConfig = GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem,
                    GUIUtils.getSelectBoxOptions( map ), ConfigUtil.fileNames( validOptions ).get( LIST_FIRST_INDEX ), false );
            selectConfig.setBindFrom( BIND_FROM_URL_FOR_PROJECT_CUSTOMATTRIBUTE_VALUE );
            selectConfig.setReadonly( inheritConfig );
        }
    }

    /**
     * Prepares job model.
     *
     * @param entityManager
     *         the entity manager
     * @param jobName
     *         the job name
     *
     * @return the job
     */
    private Job prepareDeleteJobModel( EntityManager entityManager, String jobName ) {
        Status status = new Status( WorkflowStatus.RUNNING );

        final Job jobImpl = new JobImpl();
        final LocationDTO locationDTO = locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() );
        jobImpl.setDescription( DataObjectManagerImpl.DELETE_OBJECT_JOB_DESCRIPTION );
        jobImpl.setName( jobName );
        jobImpl.setWorkflowId( UUID.fromString( SystemWorkflow.DELETE.getId() ) );
        jobImpl.setWorkflowName( SystemWorkflow.DELETE.getName() );
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
     * Checks if is life cycle status allowed.
     *
     * @param updateEntity
     *         the update entity
     *
     * @return true, if is life cycle status allowed
     */
    private boolean isLifeCycleStatusAllowed( SuSEntity updateEntity ) {
        StatusConfigDTO statusConfig = lifeCycleManager.getLifeCycleStatusByStatusId( updateEntity.getLifeCycleStatus() );
        return ( statusConfig.isAllowChanges() );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createObjectForm( String token, String parentId, String typeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Notification notify = validateObjectTypeAndParentIds( parentId, typeId );
            if ( notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }
            SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
            SuSObjectModel susObjectModel = validateTypeInParent( entityManager, typeId, parentEntity );
            entityManager.close();
            Object objectToCreate = initializeObject( susObjectModel.getClassName() );
            List< UIFormItem > uiFormItemList = GUIUtils.prepareOrderedForm( true, objectToCreate );
            if ( PropertiesManager.hasTranslation() ) {
                createTranslationFieldsUI( parentId, typeId, uiFormItemList );
            }
            boolean isWorkflowProject = false;
            for ( UIFormItem uiFormItem : uiFormItemList ) {
                switch ( uiFormItem.getName() ) {
                    case OBJECT_FIELD_NAME_PARENT_ID -> uiFormItem.setValue( UUID.fromString( parentId ) );
                    case FIELD_NAME_OBJECT_TYPE_ID -> uiFormItem.setValue( UUID.fromString( typeId ) );
                    case ProjectDTO.UI_COLUMN_NAME_CONFIG -> {
                        populateConfigDropdownOptions( uiFormItem, isWorkflowProject, parentEntity.getConfig(),
                                susObjectModel.isInheritConfig() );
                        setRulesAndMessageOnUI( uiFormItem );
                    }
                    case DataObjectDashboardDTO.UI_COLUMN_NAME_PLUGIN -> {
                        populatePluginDropdownOptions( uiFormItem, parentId, typeId );
                        setRulesAndMessageOnUI( uiFormItem );
                    }
                    case ProjectDTO.UI_COLUMN_NAME_TYPE ->
                            prepareTypeDropdownOptions( uiFormItem, parentEntity, susObjectModel.isInheritType() );
                    case ProjectDTO.UI_COLUMN_NAME_STATUS ->
                            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                                    ConstantsStatus.ACTIVE, false );
                    case "file" -> {
                        uiFormItem.setUpdateField( "name" );
                        if ( objectToCreate instanceof DataObjectHtmlDTO || objectToCreate instanceof DataObject3DceetronDTO ) {
                            Map< String, Object > rules = new HashMap<>();
                            rules.put( REQUIRED, true );
                            uiFormItem.setRules( rules );
                        }
                    }
                }
            }
            uiFormItemList.addAll( convertCustomAttributeJSONtoFormItems( susObjectModel.getCustomAttributes(), null ) );
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * Prepare type dropdown options.
     *
     * @param uiFormItem
     *         the ui form item
     * @param parentEntity
     *         the parent entity
     * @param inheritType
     *         the inherit type
     */
    private void prepareTypeDropdownOptions( UIFormItem uiFormItem, SuSEntity parentEntity, boolean inheritType ) {

        Map< String, String > map = new HashMap<>();
        if ( parentEntity instanceof ProjectEntity projectEntity && inheritType ) {
            map.put( projectEntity.getType(), projectEntity.getType() );
            uiFormItem.setReadonly( true );
            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), projectEntity.getType(),
                    false );
        } else {
            ProjectType[] projectTypes = ProjectType.values();
            for ( ProjectType projectType : projectTypes ) {
                map.put( projectType.getKey(), projectType.getKey() );
            }
            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ), ProjectType.DATA.getKey(),
                    false );
        }
        setRulesAndMessageOnUI( uiFormItem );

    }

    /**
     * Creates the translation form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the list
     */
    @Override
    public UIForm createTranslationForm( String userId, String parentId, String typeId, String translations ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList = new ArrayList<>();
            Map< String, String > languages = PropertiesManager.getRequiredlanguages();
            SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( typeId, parentEntity.getConfig() );
            for ( String lang : translations.split( ConstantsString.COMMA ) ) {
                UIFormItem uiFormItem = GUIUtils.createFormItem( "Name (" + languages.get( lang ) + ")", "name-" + lang,
                        ConstantsString.EMPTY_STRING );
                uiFormItem.setType( FieldTypes.TEXT.getType() );
                uiFormItem.setOrderNum( 11 );
                uiFormItemList.add( uiFormItem );
                if ( null != susObjectModel && susObjectModel.hasTranslation() ) {
                    UIFormItem uiFormItemFile = GUIUtils.createFormItem( "Select File (" + languages.get( lang ) + ")", "file-" + lang,
                            ConstantsString.EMPTY_STRING );
                    uiFormItemFile.setType( "file-upload" );
                    uiFormItemList.add( uiFormItemFile );
                    uiFormItem.setOrderNum( 14 );
                }
            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Adds the translation fields UI.
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
        selectFormItem.setName( SELECTED_TRANSLATIONS );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( SELECT_TRANSLATION_LABEL ) );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        languages.keySet().forEach( language -> options.add( new SelectOptionsUI( language, languages.get( language ) ) ) );
        selectFormItem.setMultiple( Boolean.TRUE );
        selectFormItem.setOptions( options );
        selectFormItem.setOrderNum( 10 );
        selectFormItem.setBindFrom(
                BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE.replace( PARAM_OBJECT_ID, parentId ).replace( PARAM_TYPE_ID, typeId ) );
        uiFormItemList.add( selectFormItem );
    }

    /**
     * Update translation form.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     * @param translations
     *         the translations
     *
     * @return the list
     */
    @Override
    public UIForm updateTranslationForm( String userId, String parentId, String typeId, String translations ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList = new ArrayList<>();
            Map< String, String > languages = PropertiesManager.getRequiredlanguages();
            SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( parentId ) );
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( typeId, parentEntity.getConfig() );
            for ( String language : translations.split( ConstantsString.COMMA ) ) {
                TranslationEntity trans = parentEntity.getTranslation().stream()
                        .filter( translation -> translation.getLanguage().equals( language ) ).findAny().orElse( null );
                UIFormItem uiFormItem = GUIUtils.createFormItem( "Name (" + languages.get( language ) + ")", "name-" + language,
                        ConstantsString.EMPTY_STRING );
                uiFormItem.setType( "text" );
                uiFormItem.setValue( null != trans && null != trans.getName() ? trans.getName() : ConstantsString.EMPTY_STRING );
                uiFormItemList.add( uiFormItem );
                if ( susObjectModel.hasTranslation() ) {
                    UIFormItem uiFormItemFile = GUIUtils.createFormItem( "Select File (" + languages.get( language ) + ")",
                            "file-" + language, ConstantsString.EMPTY_STRING );
                    uiFormItemFile.setType( "file-upload" );
                    uiFormItemFile.setValue(
                            null != trans && null != trans.getFile() ? documentManager.prepareDocumentDTO( trans.getFile() )
                                    : ConstantsString.EMPTY_STRING );
                    uiFormItemList.add( uiFormItemFile );
                }
            }
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update translation fields UI.
     *
     * @param entity
     *         the entity
     * @param uiFormItemList
     *         the ui form item list
     */
    private void updateTranslationFieldsUI( SuSEntity entity, List< UIFormItem > uiFormItemList ) {

        Map< String, String > languages = PropertiesManager.getRequiredlanguages();
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        selectFormItem.setName( SELECTED_TRANSLATIONS );
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
        selectFormItem.setOrderNum( 10 );
        uiFormItemList.add( selectFormItem );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    public UIForm createObjectFormDashboardPlugin( String userId, String parentId, String typeId, String plugin ) {
        log.debug( "entered in changeObjectOptionForm method" );
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        selectFormItem.setLabel( "Config" );
        selectFormItem.setName( "settings.config" );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );

        List< DashboardPluginDTO > validOptions = PropertiesManager.getDashboardPlugins( "plugin" );
        String value = null;
        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( DashboardPluginDTO dashboardPluginDTO : validOptions ) {
                if ( dashboardPluginDTO.getId().equals( plugin ) ) {
                    DashboardPluginConfigDTO configFirstIndex = dashboardPluginDTO.getConfig().stream().findFirst().orElseThrow();
                    value = ( !( DashboardPluginEnums.PLUNGERLIFT.getName().equalsIgnoreCase( plugin ) ) ) ? configFirstIndex.getName()
                            : "";
                    dashboardPluginDTO.getConfig().forEach( dto -> options.add( new SelectOptionsUI( dto.getName(), dto.getName() ) ) );
                }
            }
            selectFormItem.setBindFrom( BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN_AND_CONFIG.replace( PARAM_PARENT_ID, parentId )
                    .replace( PARAM_TYPE_ID, typeId ).replace( "{plugin}", plugin ) );
        }
        selectFormItem.setValue( value );
        setRulesAndMessageOnUI( selectFormItem );
        selectFormItem.setOptions( options );

        return GUIUtils.createFormFromItems( List.of( selectFormItem ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createObjectFormDashboardPluginAndConfig( String userId, String parentId, String typeId, String plugin, String config ) {
        DashboardPluginConfigDTO dashboardPluginConfigDTO = DashboardPluginUtil.getPluginConfigByPluginAndConfig( plugin, config );
        List< UIFormItem > uiFormItems = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( dashboardPluginConfigDTO.getFields() ) ) {
            for ( Map< String, Object > field : dashboardPluginConfigDTO.getFields() ) {
                UIFormItem item = jobManager.getWorkflowManager().prepareSelectUIItemFromField( field );
                item.setExternal( null );
                uiFormItems.add( item );

            }
        }
        return GUIUtils.createFormFromItems( uiFormItems );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    public UIForm getConfigFromObjectAndPlugin( String userIdFromGeneralHeader, String objectId, String plugin ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            Object filledObject = createDTOFromSusEntity( entityManager, null, entity, true );
            SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            if ( filledObject instanceof DataObjectDashboardDTO dashboardDTO ) {
                prepareSelectUIForDashboardDTO( plugin, selectFormItem, dashboardDTO );
            }

            return GUIUtils.createFormFromItems( List.of( selectFormItem ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare select ui for dashboard dto.
     *
     * @param plugin
     *         the plugin
     * @param selectFormItem
     *         the select ui
     * @param dashboardDTO
     *         the dashboard dto
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    private void prepareSelectUIForDashboardDTO( String plugin, SelectFormItem selectFormItem, DataObjectDashboardDTO dashboardDTO ) {
        selectFormItem.setLabel( "Config" );
        selectFormItem.setName( "settings.config" );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );
        String value = dashboardDTO.getSettings().get( "config" );
        selectFormItem.setBindFrom( "/data/object/{objectId}/ui/edit/plugin/{plugin}/config/{__value__}".replace( PARAM_OBJECT_ID,
                dashboardDTO.getId().toString() ).replace( "{plugin}", plugin ) );
        selectFormItem.setValue( value );
        setRulesAndMessageOnUI( selectFormItem );
        selectFormItem.setOptions( Collections.singletonList( new SelectOptionsUI( value, value ) ) );
        selectFormItem.setReadonly( Boolean.TRUE );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    public UIForm getConfigOptionsFromObjectAndPluginAndConfig( String userIdFromGeneralHeader, String objectId, String plugin,
            String config ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            Object filledObject = createDTOFromSusEntity( entityManager, null, entity, true );
            if ( filledObject instanceof DataObjectDashboardDTO dashboardDTO ) {
                DashboardPluginConfigDTO dashboardPluginConfigDTO = DashboardPluginUtil.getPluginConfigByPluginAndConfig( plugin, config );
                UIFormItem item = null;
                if ( dashboardPluginConfigDTO.getFields() != null ) {
                    for ( Map< String, Object > field : dashboardPluginConfigDTO.getFields() ) {
                        item = jobManager.getWorkflowManager().prepareSelectUIItemFromField( field );
                        if ( FieldTypes.OBJECT.getType().equals( String.valueOf( field.get( "type" ) ) )
                                && DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig().equals( config ) ) {
                            item.setExternal( null );
                            item.setValue( dashboardDTO.getProjectSelection() );
                        }
                    }
                }
                return item != null ? GUIUtils.createFormFromItems( List.of( item ) ) : new UIForm();
            }
            return new UIForm();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Populate plugin dropdown options.
     *
     * @param uiFormItem
     *         the ui form item
     * @param parentId
     *         the parent id
     * @param typeId
     *         the type id
     */
    private void populatePluginDropdownOptions( UIFormItem uiFormItem, String parentId, String typeId ) {
        Map< String, String > map = new HashMap<>();
        List< DashboardPluginDTO > validOptions = PropertiesManager.getDashboardPlugins( "plugin" );
        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( DashboardPluginDTO dashboardPluginDTO : validOptions ) {
                map.put( dashboardPluginDTO.getId(), dashboardPluginDTO.getName() );
            }
            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ),
                    map.entrySet().iterator().next().getKey(), false );
            if ( parentId != null && typeId != null ) {
                ( ( SelectFormItem ) uiFormItem ).setBindFrom(
                        BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM_PLUGIN.replace( PARAM_PARENT_ID, parentId )
                                .replace( PARAM_TYPE_ID, typeId ) );
            }
        }
    }

    /**
     * Validate image and video file.
     *
     * @param json
     *         the json
     * @param susObjectModel
     *         the sus object model
     */
    private void validateImageAndVideoFile( String json, SuSObjectModel susObjectModel ) {
        JSONObject jsonObj = new JSONObject( json );
        if ( jsonObj.has( FILE ) ) {
            if ( initializeObject( susObjectModel.getClassName() ) instanceof DataObjectMovieDTO ) {
                List< String > movieFormats = getMovieFormatProperty();
                DataObjectMovieDTO parse = JsonUtils.jsonToObject( json, DataObjectMovieDTO.class );
                if ( parse.getFile() != null && parse.getFile().getType() != null ) {
                    if ( !containsIgnoreCase( movieFormats,
                            parse.getFile().getType().replaceFirst( "^video/", "" ).replaceFirst( "^application/", "" ) ) ) {
                        throw new SusException( MessageBundleFactory.getMessage( Messages.SELECT_MOVIE_FILE.getKey() ) );
                    }
                }

            } else if ( initializeObject( susObjectModel.getClassName() ) instanceof DataObjectImageDTO ) {
                List< String > imageFormats = getImageFormatProperty();
                DataObjectImageDTO parse = JsonUtils.jsonToObject( json, DataObjectImageDTO.class );
                if ( parse.getFile() != null && parse.getFile().getType() != null && containsIgnoreCase( imageFormats,
                        parse.getFile().getType().replaceFirst( "^image/", "" ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.SELECT_IMAGE_FILE.getKey() ) );
                }

            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createSuSObject( String userId, String parentId, String typeId, String objectJson, boolean autoRefresh, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return createSuSObject( entityManager, userId, parentId, typeId, objectJson, autoRefresh, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createSuSObject( EntityManager entityManager, String userId, String parentId, String typeId, String objectJson,
            boolean autoRefresh, String token ) {
        UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        return createObject( entityManager, userEntity, parentId, typeId, objectJson, autoRefresh, token );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object createObject( EntityManager entityManager, UserEntity userEntity, String parentId, String typeId, String objectJson,
            boolean autoRefresh, String token ) {
        Object objectDTO = null;
        if ( objectJson != null ) {
            Notification notify = validateObjectTypeAndParentIds( parentId, typeId );
            if ( notify.hasErrors() ) {
                throw new SusException( notify.getErrors().toString() );
            }
            SuSEntity parentEntity = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( parentId ) );
            if ( null == parentEntity ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_FOUND.getKey() ) );
            }
            SuSObjectModel susObjectModel = validateTypeInParent( entityManager, typeId, parentEntity );
            if ( typeId != null && typeId.equals( ProjectEntity.CLASS_ID.toString() ) ) {
                return dataProjectManager.createProject( entityManager, userEntity, objectJson );
            }
            validateImageAndVideoFile( objectJson, susObjectModel );
            objectDTO = JsonUtils.jsonToObject( objectJson, initializeObject( susObjectModel.getClassName() ).getClass() );
            String objectName = ( String ) ReflectionUtils.invokeMethod( METHOD_NAME_GET_NAME, objectDTO );
            String description = ( String ) ReflectionUtils.invokeMethod( METHOD_DESCRIPTION_GET_NAME, objectDTO );
            if ( !permissionManager.isPermitted( entityManager, userEntity.getId().toString(),
                    parentId + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), objectName ) );
            }
            if ( de.soco.software.simuspace.suscore.common.util.StringUtils.isGlobalNamingConventionFollowed( objectName ) ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NAME_SHOULD_NOT_HAVE_SPECIAL_CHARACTER.getKey() ) );
            }
            if ( !isNameUniqueAmongSiblings( entityManager, objectName, null,
                    susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, parentEntity.getComposedId().getId() ) ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey(), objectName ) );
            }
            if ( null != objectDTO ) {
                ReflectionUtils.invokeMethodWithListParam( OBJECT_METHOD_SET_CUSTOM_ATTRIBUTES_DTO, objectDTO,
                        susObjectModel.getCustomAttributes() );
                SuSEntity createdEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( objectDTO, userEntity.getId().toString() );
                createdEntity.setJobId( ( UUID ) ReflectionUtils.invokeMethod( METHOD_GET_JOB_ID, objectDTO ) );
                createdEntity.setHidden( ( boolean ) ReflectionUtils.invokeMethod( "isHidden", objectDTO ) );
                createdEntity.setConfig( parentEntity.getConfig() );
                StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( typeId, parentEntity.getConfig() );

                createdEntity.setLifeCycleStatus( dto.getId() );
                UserEntity ownerUserEntity = new UserEntity();
                ownerUserEntity.setId( UUID.fromString( userEntity.getId().toString() ) );
                createdEntity.setOwner( ownerUserEntity );
                createdEntity.setIcon( susObjectModel.getIcon() );
                createdEntity.setDescription( description );
                createdEntity.setCreatedBy( userEntity );
                createdEntity.setModifiedBy( userEntity );
                createdEntity.setTranslation( setMultiNames( entityManager, objectJson ) );
                if ( susObjectModel.isInheritConfig() ) {
                    createdEntity.setConfig( parentEntity.getConfig() );
                }
                try {
                    createdEntity.setSelectedTranslations(
                            JsonUtils.toJson( new ObjectMapper().readTree( objectJson ).path( SELECTED_TRANSLATIONS ) ) );
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage() );
                }
                // audit log
                if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
                    createdEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                            objectName + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED, ConstantsDbOperationTypes.CREATED,
                            userEntity.getId().toString(), createdEntity, susObjectModel.getName() ) );
                }
                if ( createdEntity instanceof DataObjectEntity createdDataObjectEntity && createdDataObjectEntity.getFile() != null ) {
                    prepareCreatedObjectFileproperties( entityManager, objectJson, createdDataObjectEntity );
                }
                if ( createdEntity instanceof DataObjectDashboardEntity dashboardEntity ) {
                    prepareAndValidateSelectionForDashboardDTO( entityManager, dashboardEntity, ( DataObjectDashboardDTO ) objectDTO );
                    verifyDashboardSingularity( entityManager, ( DataObjectDashboardDTO ) objectDTO );
                }
                if ( createdEntity instanceof ProjectEntity projectEntity && susObjectModel.isInheritType()
                        && parentEntity instanceof ProjectEntity parentProject ) {
                    projectEntity.setType( parentProject.getType() );
                }
                submitCreatePreviewJob( entityManager, userEntity.getId().toString(), createdEntity, objectDTO );
                createdEntity = susDAO.createAnObject( entityManager, createdEntity );
                if ( null != createdEntity ) {
                    Relation r = new Relation( parentEntity.getComposedId().getId(), createdEntity.getComposedId().getId(),
                            AuditTrailRelationType.RELATION_DEFAULT );
                    susDAO.createRelation( entityManager, r );

                    ReflectionUtils.invokeSetMethodWithUUIDParam( OBJECT_METHOD_SET_ID, objectDTO, createdEntity.getComposedId().getId() );
                    if ( autoRefresh ) {
                        selectionManager.sendCustomerEventOnCreate( entityManager, userEntity.getId().toString(), createdEntity,
                                parentEntity );
                    }
                    addToAcl( entityManager, userEntity.getId().toString(), createdEntity, parentEntity );
                    saveOrUpdateIndexEntityInThread( createdEntity.getComposedId().getId(), susObjectModel );
                    createDashboardFilesInThread( token, createdEntity, objectDTO );
                    return objectDTO;
                }
            }
        }
        return objectDTO;
    }

    /**
     * Verify dashboard singularity.
     *
     * @param entityManager
     *         the entity manager
     * @param dto
     *         the dto
     */
    @Deprecated( since = "soco/2.3.1/release" )
    private void verifyDashboardSingularity( EntityManager entityManager, DataObjectDashboardDTO dto ) {
        var plugin = dto.getPlugin();
        var config = dto.getSettings().get( "config" );
        if ( !DashboardPluginUtil.getPluginConfigByPluginAndConfig( plugin, config ).getAllowMultiple() ) {
            var allDashboardsByPlugin = susDAO.getLatestNonDeletedObjectListByProperty( entityManager, DataObjectDashboardEntity.class,
                    "plugin", plugin );
            allDashboardsByPlugin.stream().filter(
                            susEntity -> susEntity instanceof DataObjectDashboardEntity dashboardEntity && config.equals(
                                    ( ( Map< String, String > ) JsonUtils.jsonToMap( ByteUtil.convertByteToString( dashboardEntity.getSettings() ),
                                            new HashMap<>() ) ).get( "config" ) ) ).map( susEntity -> ( DataObjectDashboardEntity ) susEntity )
                    .forEach( dashboardEntity -> {
                        throw new SusException(
                                String.format( "dashboard with plugin '%s' and config '%s' already exists with id %s", plugin, config,
                                        dashboardEntity.getComposedId().getId().toString() ) );
                    } );
        }
    }

    /**
     * Create dashboard cache in thread.
     *
     * @param token
     *         the token
     * @param createdEntity
     *         the created entity
     * @param objectDTO
     *         the object dto
     */
    @Deprecated( since = "soco/2.3.1/release" )
    private void createDashboardFilesInThread( String token, SuSEntity createdEntity, Object objectDTO ) {
        if ( token != null && createdEntity instanceof DataObjectDashboardEntity ) {
            log.debug( "checking to create dashboard cache" );
            DataObjectDashboardDTO dashboardDto = ( DataObjectDashboardDTO ) objectDTO;
            if ( DashboardPluginEnums.PROJECT.getId().equals( dashboardDto.getPlugin() )
                    && DashboardConfigEnums.PROJECT_CLASSIFICATION.getConfig().equals( dashboardDto.getSettings().get( "config" ) ) ) {
                var headers = CommonUtils.prepareHeadersWithAuthToken( token );
                var url = PropertiesManager.getLocationURL() + PROJECT_DASHBOARD_CACHE_UPDATE_URL.replace( PARAM_PROJECT_ID,
                        createdEntity.getComposedId().getId().toString() );
                log.debug( "creating dashboard cache" );
                new Thread( () -> SuSClient.putRequest( url, headers, "" ) ).start();
            } else if ( DashboardPluginEnums.PST.getId().equals( dashboardDto.getPlugin() ) && DashboardConfigEnums.PST_PLANNING.getConfig()
                    .equals( dashboardDto.getSettings().get( "config" ) ) ) {
                var headers = CommonUtils.prepareHeadersWithAuthToken( token );
                var url = PropertiesManager.getLocationURL() + PST_DASHBOARD_FILES_CREATE_URL.replace( PARAM_PROJECT_ID,
                        createdEntity.getComposedId().getId().toString() );
                log.debug( "creating pst-planning files" );
                new Thread( () -> SuSClient.putRequest( url, headers, "" ) ).start();
            }
        }

    }

    /**
     * Save or update index entity in thread.
     *
     * @param createdId
     *         the created id
     * @param susObjectModel
     *         the sus object model
     */
    private void saveOrUpdateIndexEntityInThread( UUID createdId, SuSObjectModel susObjectModel ) {
        new Thread( () -> {
            EntityManager threadEntityManager = entityManagerFactory.createEntityManager();
            try {
                SuSEntity createdEntity = susDAO.getLatestNonDeletedObjectById( threadEntityManager, createdId );
                saveOrUpdateIndexEntity( threadEntityManager, susObjectModel, createdEntity );
            } finally {
                threadEntityManager.close();
            }

        } ).start();
    }

    /**
     * Prepare created object fileproperties.
     *
     * @param entityManager
     *         the entity manager
     * @param objectJson
     *         the object json
     * @param createdEntity
     *         the created data object entity
     */
    private void prepareCreatedObjectFileproperties( EntityManager entityManager, String objectJson, DataObjectEntity createdEntity ) {
        JSONObject jsonObj = new JSONObject( objectJson );
        if ( jsonObj.has( FILE ) ) {
            DataObjectFileDTO fileAttached = JsonUtils.jsonToObject( objectJson, DataObjectFileDTO.class );
            createdEntity.setSize( fileAttached.getFile().getSize() );
            createdEntity.getFile().setSize( fileAttached.getFile().getSize() );
            createdEntity.getFile().setFileSize( fileAttached.getFile().getSize() );
        }

        createdEntity.getFile().setLocations(
                documentManager.getDocumentDAO().getLatestObjectById( entityManager, DocumentEntity.class, createdEntity.getFile().getId() )
                        .getLocations() );
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
     * Submit create preview job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     * @param objectDTO
     *         the object DTO
     */
    private void submitCreatePreviewJob( EntityManager entityManager, String userId, SuSEntity createdEntity, Object objectDTO ) {
        if ( createdEntity instanceof DataObjectEntity createdDataObject && createdDataObject.getFile() != null ) {
            previewManager.createPreview( entityManager,
                    documentManager.getDocumentById( entityManager, createdDataObject.getFile().getId() ), userId, createdDataObject,
                    documentManager.getDocumentEntityById( entityManager, createdDataObject.getFile().getId() ), objectDTO );
            if ( PropertiesManager.hasTranslation() ) {
                createdDataObject.getTranslation().forEach( translation -> {
                    if ( null != translation.getFile() ) {
                        writeToDiskInFETempByLocation( entityManager, documentManager.getDocumentById( translation.getFile().getId() ),
                                documentManager.getDocumentEntityById( entityManager, translation.getFile().getId() ).getLocations() );
                        documentManager.writeToDiskInFETemp( entityManager,
                                documentManager.getDocumentById( translation.getFile().getId() ), PropertiesManager.getFeStaticPath() );
                    }
                } );
            }
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
    private void addToAcl( EntityManager entityManager, String userId, SuSEntity createdEntity, SuSEntity parentEntity ) {
        log.info( MessageBundleFactory.getMessage( Messages.SAVING_OBJECT_AS_RESOURCE.getKey() ) );
        ResourceAccessControlDTO resourceAccessControlDTO = permissionManager.prepareResourceAccessControlDTOFromObject( entityManager,
                createdEntity, parentEntity );
        if ( resourceAccessControlDTO != null ) {
            resourceAccessControlDTO.setSecurityIdentity(
                    userCommonManager.getAclCommonSecurityIdentityDAO().getSecurityIdentityBySid( entityManager, UUID.fromString( userId ) )
                            .getId() );
            permissionManager.addObjectToAcl( entityManager, resourceAccessControlDTO );
            permissionManager.saveSelectionForNewEntity( entityManager, userId, createdEntity, parentEntity );
        }
    }

    /**
     * Delete selected data objects and its versions and relations.
     *
     * @param userId
     *         the user id
     * @param selectionId
     *         the selection id
     */
    @Override
    public void deleteObjectVersionsAndRelationsBulk( String userId, String selectionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Job bulkDeleteJob;
        try {
            bulkDeleteJob = prepareBulkDeleteJob( entityManager, userId );
            jobManager.saveJobIds( entityManager, bulkDeleteJob.getId() );
        } finally {
            entityManager.close();
        }

        DeleteObjectThread deleteObjectThread = new DeleteObjectThread( userId, selectionId, ConstantsMode.BULK, bulkDeleteJob, jobManager,
                dataManager, selectionManager, entityManagerFactory );
        threadPoolExecutorService.deleteExecute( bulkDeleteJob.getId(), deleteObjectThread );
    }

    /**
     * Prepare bulk delete job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     *
     * @return the job
     */
    private Job prepareBulkDeleteJob( EntityManager entityManager, String userId ) {
        Job deleteProjectJob = prepareDeleteJobModel( entityManager, BULK_JOB_NAME_DELETE_OBJECT );
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_DELETE_OBJECT_AND_DEPENDENCIES.getKey() ),
                new Date() ) );
        deleteProjectJob.setLog( jobLog );
        deleteProjectJob.setCreatedBy( new UserDTO( userId ) );
        deleteProjectJob.setWorkflowId( UUID.fromString( SystemWorkflow.DELETE.getId() ) );
        deleteProjectJob.setJobType( JobTypeEnums.SYSTEM.getKey() );
        return jobManager.createJob( entityManager, UUID.fromString( userId ), deleteProjectJob );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteObjectVersionsAndRelations( String userId, UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            SuSEntity objectToDelete = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );

            if ( isEditable( objectToDelete, userId ) ) {

                Job deleteProjectJob = prepareDeleteJob( entityManager, userId, objectToDelete );

                jobManager.saveJobIds( entityManager, deleteProjectJob.getId() );

                DeleteObjectThread deleteObjectThread = new DeleteObjectThread( userId, objectId.toString(), ConstantsMode.SINGLE,
                        deleteProjectJob, jobManager, dataManager, selectionManager, entityManagerFactory );
                threadPoolExecutorService.deleteExecute( deleteProjectJob.getId(), deleteObjectThread );
                selectionManager.sendCustomerEvent( entityManager, userId, objectToDelete, "expire" );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey(), objectId ) );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare delete job.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectToDelete
     *         the objectToDelete
     *
     * @return the job
     */
    private Job prepareDeleteJob( EntityManager entityManager, String userId, SuSEntity objectToDelete ) {
        String deleteJobName = JOB_NAME_DELETE_OBJECT + objectToDelete.getName();
        if ( deleteJobName.length() >= ConstantsInteger.MAX_NAME_LENGTH ) {
            deleteJobName = deleteJobName.substring( ConstantsInteger.INTEGER_VALUE_ZERO, ConstantsInteger.MAX_NAME_LENGTH );
        }

        Job deleteProjectJob = prepareDeleteJobModel( entityManager, deleteJobName );
        List< LogRecord > jobLog = new ArrayList<>();
        jobLog.add( new LogRecord( INFO, MessageBundleFactory.getMessage( Messages.GOING_TO_DELETE_OBJECT_AND_DEPENDENCIES.getKey() ),
                new Date() ) );
        deleteProjectJob.setLog( jobLog );
        deleteProjectJob.setCreatedBy( new UserDTO( userId ) );
        deleteProjectJob.setJobType( JobTypeEnums.SYSTEM.getKey() );
        return jobManager.createJob( entityManager, UUID.fromString( userId ), deleteProjectJob );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveObject( String userId, UUID srcSelectionId, UUID targetSelectionId ) {
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
            if ( susDAO.createRelation( entityManager, relation ) == null ) {
                return false;
            }
            permissionManager.updateParentInAclEntity( entityManager, userId, srcSelectionId, targetId );
            return true;
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
                permissionManager.updateParentInAclEntity( entityManager, userId, srcSelection, targetId );
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
                containerObjectRelations.add( relation ); // adding only container object relations, filter out link relations
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

        if ( target instanceof WorkflowProjectEntity ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_DATA_OBJECTS_TO_WORKFLOW_PROJECT.getKey() ) );
        }

        validateDataAndTypesForMove( entityManager, singleSrc, target );

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

        if ( target instanceof WorkflowProjectEntity ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_DATA_OBJECTS_TO_WORKFLOW_PROJECT.getKey() ) );
        }

        for ( SuSEntity singleSrc : srcObjects ) {
            validateDataAndTypesForMove( entityManager, singleSrc, target );
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
     * Validates data and types of source and target for move operation.
     *
     * @param entityManager
     *         the entity manager
     * @param singleSrc
     *         the source object
     * @param target
     *         the target object
     */
    private void validateDataAndTypesForMove( EntityManager entityManager, SuSEntity singleSrc, SuSEntity target ) {
        if ( !( singleSrc.getConfig().equals( target.getConfig() ) ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_BECAUSE_TARGET_PROJECT_HAVE_DIFFERENT_CONFIGURATIONS.getKey() ) );
        }

        // Verify if target container can hold src item
        SuSObjectModel suSObjectModelForTarget = configManager.getObjectTypeByIdAndConfigName( target.getTypeId().toString(),
                target.getConfig() );
        if ( !suSObjectModelForTarget.getContains().contains( singleSrc.getTypeId().toString() ) ) {
            throw new SusException( "Cannot move because target container does not support selected item type" );
        }

        if ( singleSrc instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getCheckedOut() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CANNOT_MOVE_CHECKED_OUT_OBJECTS.getKey() ) );
        }

        if ( !isNameUniqueAmongSiblings( entityManager, singleSrc.getName(), null, target ) ) {
            throw new SusException( MessageBundleFactory.getMessage(
                    Messages.CANNOT_MOVE_BECAUSE_TARGET_ALREADY_CONTAINS_OBJECT_WITH_SAME_NAME.getKey() ) );
        }

        // Can only move projects into Label
        if ( target.getTypeId().equals( ProjectEntity.CLASS_ID ) && ( ( ProjectEntity ) target ).getType()
                .equals( ProjectType.LABEL.getKey() ) && !( singleSrc.getTypeId().equals( ProjectEntity.CLASS_ID ) ) ) {

            throw new SusException( MessageBundleFactory.getMessage( Messages.ONLY_PROJECTS_CAN_MOVE_TO_LABELS.getKey() ) );
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
     * {@inheritDoc}
     */
    @Override
    public Object getObjectByIdAndVersion( String userIdFromGeneralHeader, VersionPrimaryKey key ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Object dto = null;
            if ( key != null ) {
                SuSEntity susEntity = susDAO.getObjectByCompositeKey( entityManager, key );
                if ( susEntity instanceof ProjectEntity projectEntity ) {
                    dto = createProjectDTOFromProjectEntity( projectEntity );
                } else if ( susEntity instanceof LibraryEntity ) {
                    dto = createLibraryDTOFromObjectEntity( susEntity );
                } else if ( susEntity instanceof VariantEntity ) {
                    dto = createVariantDTOFromObjectEntity( susEntity );
                } else if ( susEntity instanceof ReportEntity ) {
                    dto = prepareReportDTO( entityManager, susEntity );
                } else if ( susEntity instanceof DataObjectEntity ) {
                    dto = createObjectDTOFromObjectEntity( entityManager, susEntity );
                }
            }
            return dto;
        } finally {
            entityManager.close();
        }
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
    private SuSEntity getDataObjectEntity( EntityManager entityManager, String userId, UUID dataobjectId ) {
        SuSEntity result = null;
        if ( dataobjectId != null ) {
            SuSEntity latestEntity = dataDAO.getLatestNonDeletedObjectViaCache( entityManager, dataobjectId );
            if ( latestEntity != null ) {
                if ( latestEntity instanceof SystemContainerEntity ) {
                    return latestEntity;
                }
                log.trace( latestEntity.getTypeId().toString() );
                log.trace( latestEntity.getConfig() );
                SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                        latestEntity.getConfig() );
                result = susDAO.getLatestObjectByIdWithLifeCycle( entityManager, dataobjectId, UUID.fromString( userId ),
                        lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ) );
                isPermittedtoRead( entityManager, userId, dataobjectId.toString() );
            }
        }
        if ( result == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        }
        UserDTO user = TokenizedLicenseUtil.getUser(
                BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
        List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager,
                Collections.singletonList( result.getComposedId() ) );
        translateName( user, result, translationEntityList );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObjectProperties( String userId, UUID dataObjectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = getDataObjectEntity( entityManager, userId, dataObjectId );
            GenericDataObjectDTO genericDataObjectDTO = new GenericDataObjectDTO();
            genericDataObjectDTO.setName( susEntity.getName() );
            genericDataObjectDTO.setId( susEntity.getComposedId().getId() );
            genericDataObjectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            genericDataObjectDTO.setCreatedOn( susEntity.getCreatedOn() );
            genericDataObjectDTO.setConfig( susEntity.getConfig() );
            genericDataObjectDTO.setModifiedOn( susEntity.getModifiedOn() );
            genericDataObjectDTO.setDescription( susEntity.getDescription() );
            genericDataObjectDTO.setTypeId( susEntity.getTypeId() );
            genericDataObjectDTO.setAutoDeleted( susEntity.isAutoDelete() );
            genericDataObjectDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
            SuSObjectModel susobject = null;
            if ( susEntity.getTypeId() != null && susEntity.getConfig() != null ) {
                susobject = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() );
                if ( susobject != null ) {
                    genericDataObjectDTO.setLifeCycleStatus( lifeCycleManager.getStatusByLifeCycleNameAndStatusId( susobject.getLifeCycle(),
                            susEntity.getLifeCycleStatus() ) );
                    genericDataObjectDTO.setType( susobject.getName() );
                }
                genericDataObjectDTO.setIcon( susEntity.getIcon() );
            }
            setGenericDtoUrlType( susEntity, genericDataObjectDTO, susobject );
            genericDataObjectDTO.setLink(
                    CollectionUtils.isNotEmpty( linkDao.getLinkedRelationByChildId( entityManager, susEntity.getComposedId().getId() ) )
                            ? LINK_TYPE_YES : LINK_TYPE_NO );

            if ( null != susEntity.getCreatedBy() ) {
                genericDataObjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                genericDataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            DocumentEntity file = null;
            if ( susEntity instanceof DataObjectEntity dataObjectEntity ) {
                file = dataObjectEntity.getFile();
                if ( dataObjectEntity instanceof DataObjectImageEntity imageEntity ) {
                    DocumentEntity thumb = imageEntity.getThumbNail();
                    if ( null != thumb && thumb.getFileType() != null && imageEntity.getFile() != null ) {
                        genericDataObjectDTO.setThumbnailImage( getDataObjectImageThumbNail( imageEntity.getThumbNail() ) );
                    }
                } else if ( dataObjectEntity instanceof DataObjectMovieEntity movieEntity ) {
                    DocumentEntity thumb = movieEntity.getThumbnail();

                    if ( null != thumb && thumb.getFileType() != null && (
                            thumb.getFileType().equalsIgnoreCase( ConstantsImageFileTypes.JPG ) || thumb.getFileType()
                                    .equalsIgnoreCase( ConstantsImageFileTypes.BMP ) || thumb.getFileType()
                                    .equalsIgnoreCase( ConstantsImageFileTypes.PNG ) ) ) {
                        genericDataObjectDTO.setThumbnailImage( getDataObjectMovieThumbNail( thumb ) );
                    }
                }

            } else if ( susEntity instanceof ProjectEntity projectEntity ) {
                file = projectEntity.getFile();
            }
            if ( null != file && CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                List< LocationDTO > locationDTOs = getDocumentLocations( file );
                genericDataObjectDTO.setLocations( locationDTOs );
            }

            if ( CollectionUtils.isNotEmpty( susEntity.getCustomAttributes() ) ) {
                genericDataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            }

            return genericDataObjectDTO;
        } finally {
            entityManager.close();
        }
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
    public Object getDataObject( String userId, UUID dataobjectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObject( entityManager, userId, dataobjectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObject( EntityManager entityManager, String userId, UUID dataobjectId ) {
        Object dto = null;
        if ( dataobjectId != null ) {
            dto = createDTOFromSusEntity( entityManager, null, getDataObjectEntity( entityManager, userId, dataobjectId ), true );
        }
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfObjectsUITableColumns( EntityManager entityManager, String userIdFromGeneralHeader, String objectId,
            String typeId ) {
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );

        return getListOfObjectsUITableColumnsByEntity( entityManager, userIdFromGeneralHeader, objectId, typeId, susEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfObjectsUITableColumns( String userIdFromGeneralHeader, String objectId, String typeId,
            List< ObjectViewDTO > views ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );

            TableUI table = getListOfObjectsUITableColumnsByEntity( entityManager, userIdFromGeneralHeader, objectId, typeId, susEntity );
            table.setViews( views );

            return table;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI getListOfObjectsUITableColumnsByVersions( String userIdFromGeneralHeader, String objectId, String typeId,
            int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getObjectByIdAndVersion( entityManager, SuSEntity.class, UUID.fromString( objectId ), versionId );

            return getListOfObjectsUITableColumnsByEntity( entityManager, userIdFromGeneralHeader, objectId, typeId, susEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the list of objects UI table columns by entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param typeId
     *         the type id
     * @param susEntity
     *         the sus entity
     *
     * @return the list of objects UI table columns by entity
     */
    private TableUI getListOfObjectsUITableColumnsByEntity( EntityManager entityManager, String userIdFromGeneralHeader, String objectId,
            String typeId, SuSEntity susEntity ) {
        List< TableColumn > columns;
        SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( typeId, susEntity.getConfig() );
        if ( !objectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
            isPermittedtoRead( entityManager, userIdFromGeneralHeader, objectId );
        }
        if ( typeId.contentEquals( GenericDTO.GENERIC_DTO_TYPE ) ) {
            columns = GUIUtils.listColumns( GenericDTO.class );

            Map< String, String > nameUrlValues = new HashMap<>();
            nameUrlValues.put( ConstantsString.OBJECT_KEY, ConstantsUrlViews.DATA_OBJECT_VIEW );
            nameUrlValues.put( ConstantsString.PROJECT_KEY, ConstantsUrlViews.DATA_PROJECT_VIEW );
            GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, URL_TYPE, nameUrlValues, columns );
            if ( objectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                for ( final TableColumn tableColumn : columns ) {

                    if ( "entitySize".equalsIgnoreCase( tableColumn.getName() ) ) {
                        tableColumn.setVisible( false );
                    }
                }
            }

        } else {

            Object o = initializeObject( susObjectModel.getClassName() );
            Class< ? > clazz = o.getClass();
            log.info( ">>getListOfObjectsUITableColumns Checking class:" + clazz.getName() );
            columns = GUIUtils.listColumns( clazz );
            Object entity = new DataObjectEntity();
            try {
                Field f = clazz.getDeclaredField( ENTITY_CLASS_FIELD_NAME );
                f.setAccessible( true );
                entity = f.get( o );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }

            if ( entity instanceof ContainerEntity ) {
                GUIUtils.setLinkColumn( ProjectDTO.PROJECT_NAME, ConstantsUrlViews.DATA_PROJECT_VIEW, columns );
            } else {
                GUIUtils.setLinkColumn( DataObjectDTO.DATAOBJECT_NAME, ConstantsUrlViews.DATA_OBJECT_VIEW, columns );
            }

            List< CustomAttributeDTO > customAttributeDTOs = susObjectModel.getCustomAttributes();
            columns.addAll( prepareCustomAttributesTableColumns( customAttributeDTOs ) );
        }

        return new TableUI( columns, getObjectViewList( entityManager, susEntity, userIdFromGeneralHeader, typeId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAllItemsObjects( String userId, UUID parentId, FiltersDTO filter, boolean toItems ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, parentId );

            return getAllItemsObjectsBySusEntity( entityManager, userId, parentId, filter, toItems, latestEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the all items objects by version.
     *
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param toItems
     *         the to items
     * @param versionId
     *         the version id
     *
     * @return the all items objects by version
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getAllItemsObjectsByVersion(java.lang.String, java.util.UUID,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO, boolean, int)
     */
    @Override
    public FilteredResponse< Object > getAllItemsObjectsByVersion( String userId, UUID parentId, FiltersDTO filter, boolean toItems,
            int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            SuSEntity latestEntity = susDAO.getObjectByIdAndVersion( entityManager, SuSEntity.class, parentId, versionId );

            return getAllItemsObjectsBySusEntity( entityManager, userId, parentId, filter, toItems, latestEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the all items objects by sus entity.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param toItems
     *         the to items
     * @param latestEntity
     *         the latest entity
     *
     * @return the all items objects by sus entity
     */
    private FilteredResponse< Object > getAllItemsObjectsBySusEntity( EntityManager entityManager, String userId, UUID parentId,
            FiltersDTO filter, boolean toItems, SuSEntity latestEntity ) {
        List< SuSEntity > suSEntities = new ArrayList<>();
        List< Object > objectDTOList = new ArrayList<>();
        if ( latestEntity != null ) {
            if ( latestEntity instanceof ContainerEntity ) {
                String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName( ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME )
                        .get( 0 ).getId();
                suSEntities = susDAO.getAllFilteredLinkedItemsWithParentAndLifeCycle( entityManager, SuSEntity.class, filter, parentId,
                        userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ), PermissionMatrixEnum.READ.getKey(),
                        configManager.getTypesFromConfiguration( latestEntity.getConfig() ), toItems );

            } else {
                SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                        latestEntity.getConfig() );
                suSEntities = susDAO.getAllFilteredLinkedItemsWithParentAndLifeCycle( entityManager, SuSEntity.class, filter, parentId,
                        userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ), PermissionMatrixEnum.READ.getKey(),
                        configManager.getTypesFromConfiguration( latestEntity.getConfig() ), toItems );
            }
        }

        populateGenericDTO( entityManager, userId, parentId, suSEntities, objectDTOList );
        return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GenericDTO createGenericDTOFromObjectEntity( EntityManager entityManager, String userId, UUID projectId, SuSEntity susEntity,
            List< TranslationEntity > translationEntities, boolean forList ) {
        return dataProjectManager.createGenericDTOFromObjectEntity( entityManager, userId, projectId, susEntity, translationEntities,
                forList );
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
        return dataProjectManager.createGenericDTOFromObjectEntity( entityManager, userId, projectId, susEntity, translationEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getDataObjectVersionsUI( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            entityManager.close();
            List< TableColumn > columns = GUIUtils.listColumns( DataObjectDTO.class, VersionDTO.class );

            for ( TableColumn tableColumn : columns ) {

                if ( DataObjectDTO.DATAOBJECT_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
                    tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_OBJECT_BY_ID_AND_VERSION_URL );
                    tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );

                }
            }

            if ( susEntity != null && susEntity.getTypeId() != null ) {

                SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                        susEntity.getConfig() );

                List< CustomAttributeDTO > customAttributeDTOs = susObjectModel.getCustomAttributes();

                columns.addAll( prepareCustomAttributesTableColumns( customAttributeDTOs ) );
            }
            return columns;
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

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
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectVersionContext(java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public List< ContextMenuItem > getDataObjectVersionContext( String objectId, FiltersDTO filter ) {

        List< ContextMenuItem > menu = new ArrayList<>();
        if ( filter.getItems().size() == 1 ) {
            ContextMenuItem containerCMI = new ContextMenuItem();
            containerCMI.setUrl( "update/data/object/{objectId}/version/{versionId}/status".replace( PARAM_OBJECT_ID, objectId )
                    .replace( "{versionId}", filter.getItems().get( 0 ) + "" ) );
            containerCMI.setTitle( MessageBundleFactory.getMessage( "4100005x4" ) );
            menu.add( containerCMI );
        }

        return menu;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getTabsViewDataObjectVersionUI( String userId, String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getObjectByCompositeKey( entityManager,
                    new VersionPrimaryKey( UUID.fromString( objectId ), versionId ) );
            SuSObjectModel objectTypeById = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                    entity.getConfig() );
            return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), entity.getVersionId(),
                    SubTabsUI.getSubTabsList( objectTypeById.getViewConfig() ), entity.getIcon() );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getObjectVersions( String userId, UUID objectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
            }
            List< Object > objectDTOList = new ArrayList<>();
            SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
            SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            List< SuSEntity > objectEntityList = susDAO.getAllFilteredVersionsById( entityManager, SuSEntity.class, objectId, filter,
                    UUID.fromString( userId ), lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ) );
            if ( CollectionUtil.isNotEmpty( objectEntityList ) ) {
                for ( SuSEntity objectEntity : objectEntityList ) {
                    objectDTOList.add( createDTOFromSusEntity( entityManager, null, objectEntity, true ) );
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
    public String getTypeOfSusEntityById( UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
            return configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(), latestEntity.getConfig() ).getName();
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean permitPermissionToObject( CheckBox checkBox, UUID objectId, UUID securityId, String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            log.info( ">>permitPermissionToObject checkBox:" + checkBox );
            return permissionManager.permitPermissionToObject( entityManager, checkBox, objectId, securityId, userIdFromGeneralHeader );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableUI objectPermissionTableUI( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        log.info( PREPARE_PERMISSION_FORM );
        try {
            Field[] attributes = ManageObjectDTO.class.getDeclaredFields();
            List< TableColumn > columnsList = permissionManager.extractColumnList( attributes );
            TableUI tui = new TableUI();
            tui.setColumns( columnsList );
            tui.setViews(
                    objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.PERMISSION_TABLE_KEY, userId, null ) );
            return tui;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getTabsViewDataObjectUI( String token, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String userIdFromGeneralHeader = TokenizedLicenseUtil.getNotNullUser( token ).getId();
            List< SubTabsUI > subTabsUIs;
            SuSObjectModel model = null;
            SuSEntity entity = getDataObjectEntity( entityManager, userIdFromGeneralHeader, UUID.fromString( objectId ) );
            if ( !entity.isHidden() ) {
                model = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );
                subTabsUIs = SubTabsUI.getSubTabsList( model.getViewConfig() );
                if ( StringUtils.isNotBlank( model.getOverviewPlugin() ) ) {
                    OverviewPluginUtil.getUpdatedSubTabsList( model.getOverviewPlugin(), subTabsUIs,
                            TokenizedLicenseUtil.getUserLanguage( token ) );
                }
            } else {
                subTabsUIs = new ArrayList<>();
                // in case of hidden object (plot : image : bubble : correlation etc )
                SubTabsUI subTab = new SubTabsUI();
                if ( entity.getTypeId().equals( UUID.fromString( SimuspaceFeaturesEnum.IMAGE_DATA_OBJECTS.getId() ) ) ) {
                    subTab.setName( PREVIEW );
                    subTab.setTitle( PREVIEW_CAPS );
                } else if ( entity.getTypeId().equals( DataObjectTraceEntity.CLASS_ID ) ) {
                    subTab.setName( TRACE );
                    subTab.setTitle( TRACE_CAPS );
                }
                subTabsUIs.add( subTab );
            }

            return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), entity.getVersionId(), subTabsUIs,
                    entity.getIcon() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectFormItem > permissionObjectOptionsForm( String userId, String objectId, String option ) {
        return dataProjectManager.permissionObjectOptionsForm( userId, objectId, option );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectSingleUI( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columns = null;
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( susEntity instanceof DataObjectValueEntity ) {
                columns = GUIUtils.listColumns( DataObjectValueDTO.class );
            } else if ( susEntity instanceof DataObjectImageEntity ) {
                columns = GUIUtils.listColumns( DataObjectImageDTO.class );
            } else if ( susEntity instanceof DataObjectMovieEntity ) {
                columns = GUIUtils.listColumns( DataObjectMovieDTO.class );
            } else if ( susEntity instanceof DataObjectFileEntity ) {
                columns = GUIUtils.listColumns( DataObjectFileDTO.class );
            } else if ( susEntity instanceof ReportEntity ) {
                columns = GUIUtils.listColumns( ReportDTO.class );
            } else if ( susEntity instanceof DataObjectEntity ) {
                columns = GUIUtils.listColumns( DataObjectDTO.class );
            } else if ( susEntity instanceof VariantEntity ) {
                columns = GUIUtils.listColumns( VariantDTO.class );
            } else if ( susEntity instanceof LibraryEntity ) {
                columns = GUIUtils.listColumns( LibraryDTO.class );
            } else if ( susEntity instanceof ProjectEntity ) {
                columns = GUIUtils.listColumns( ProjectDTO.class );
            } else if ( susEntity instanceof DataDashboardEntity ) {
                columns = GUIUtils.listColumns( DataDashboardDTO.class );
            }
            appendCustomAttributeColumns( susEntity, columns );
            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectVersionUI( String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< TableColumn > columns = GUIUtils.listColumns( DataObjectDTO.class, VersionDTO.class );
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            appendCustomAttributeColumns( susEntity, columns );

            return columns;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TableColumn > getObjectMetaDataTableUI( String objectId ) {
        List< TableColumn > listColumns = GUIUtils.listColumns( MetaDataEntryDTO.class );
        for ( TableColumn tableColumn : listColumns ) {
            tableColumn.setData( tableColumn.getName() );
        }
        return listColumns;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm prepareMetaDataFormForCreate( String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = getDataObjectEntity( entityManager, userId, UUID.fromString( objectId ) );
            if ( isEditable( susEntity, userId ) ) {
                isPermittedtoWrite( entityManager, userId, susEntity.getComposedId().getId().toString() );

                List< UIFormItem > itemsUI = GUIUtils.prepareForm( true, new MetaDataEntryDTO() );
                for ( UIFormItem uiFormItem : itemsUI ) {
                    if ( uiFormItem.getLabel().equalsIgnoreCase( ObjectMetaDataDTO.KEY_FIELD ) ) {
                        setRulesAndMessageOnUI( uiFormItem );
                    }
                }
                return GUIUtils.createFormFromItems( itemsUI );

            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey(), objectId ) );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectMetaDataDTO addMetaDataToAnObject( String objectId, ObjectMetaDataDTO objectMetaDataDTO, String userId,
            boolean isCreateOperation ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SusDTO susDTO = ( SusDTO ) getDataObject( entityManager, userId, UUID.fromString( objectId ) );
            isPermittedtoWrite( entityManager, userId, susDTO.getId().toString() );
            ObjectMetaDataDTO objectMetaDataDTOToReturn = new ObjectMetaDataDTO();
            Notification notification = objectMetaDataDTO.validate();
            if ( notification.hasErrors() ) {
                throw new SusException( notification.getErrors().toString() );
            }

            MapFilterUtil metaDataMapToMerge = new MapFilterUtil();
            for ( MetaDataEntryDTO metaDataEntry : objectMetaDataDTO.getMetadata() ) {
                metaDataMapToMerge.put( metaDataEntry.getKey().toLowerCase(), metaDataEntry.getValue() );
            }
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( susEntity != null ) {

                if ( !isLifeCycleStatusAllowed( susEntity ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), objectId ) );
                }
                MapFilterUtil metaDataFromVaultToPersist = readMetaDataFromCurrentObjectVersion( susEntity );

                if ( isCreateOperation ) {
                    validateForDuplicateEntryInMetaData( objectMetaDataDTO, metaDataFromVaultToPersist );
                }
                metaDataFromVaultToPersist.putAll( metaDataMapToMerge );
                ObjectMetaDataDTO.validateMaximumEntriesInMetaDataMap( metaDataMapToMerge );
                DocumentDTO savedDocumentDTO = createDocumentForObjectMetaData( entityManager, userId,
                        susEntity.getComposedId().getId().toString(), susEntity.getComposedId().getVersionId(),
                        metaDataFromVaultToPersist );
                updateMetaDataDocumentToObject( entityManager, savedDocumentDTO, susEntity, userId, ConstantsString.CREATED );
                objectMetaDataDTOToReturn.setMetadata( objectMetaDataDTO.getMetadata() );
            }
            return objectMetaDataDTOToReturn;
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< MetaDataEntryDTO > getObjectMetaDataList( String userId, String objectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedtoRead( entityManager, userId, objectId );
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            return getMetaDataList( susEntity, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< MetaDataEntryDTO > getObjectMetaDataListByVersion( String userId, String objectId, int versionId,
            FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedtoRead( entityManager, userId, objectId );
            SuSEntity susEntity = susDAO.getObjectByCompositeKey( entityManager,
                    new VersionPrimaryKey( UUID.fromString( objectId ), versionId ) );
            return getMetaDataList( susEntity, filter );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getMetaDataContextRouter( String objectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return contextMenuManager.getContextMenu( entityManager, PLUGIN_OBJECT, objectId, ObjectMetaDataDTO.class, filter );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getLocalContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter ) {
        SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                SelectionOrigins.CONTEXT, filter );
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        String urlWithParam = LOCAL_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM, projectId )
                .replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, selectionResponseUI.getId() );
        contextToReturn.add( new ContextMenuItem( urlWithParam, ConstantsString.EMPTY_STRING, TITLE_UPLOAD_SYNC ) );
        String urlWithParamForChangeType = CHANGE_TYPE_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM, projectId )
                .replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, selectionResponseUI.getId() );
        contextToReturn.add( new ContextMenuItem( urlWithParamForChangeType, ConstantsString.EMPTY_STRING, CHANGE_TYPE ) );
        return contextToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter, String token ) {
        return dataProjectManager.getDataContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter, token );

    }

    /**
     * Gets the data list context.
     *
     * @param userId
     *         the user id
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param selectedIds
     *         the selected ids
     * @param token
     *         the token
     * @param getSearchContext
     *         the get search context
     *
     * @return the data list context
     */
    @Override
    public List< ContextMenuItem > getDataListContext( EntityManager entityManager, String userId, String projectId, FiltersDTO filter,
            List< String > selectedIds, String token, boolean getSearchContext ) {
        return dataProjectManager.getDataListContext( entityManager, userId, projectId, filter, selectedIds, token, getSearchContext );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getObjectItemsContextRouter( String userIdFromGeneralHeader, String objectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return contextMenuManager.getDataRemoveLinkContextMenu( PLUGIN_OBJECT, objectId,
                    createRemoveSelection( entityManager, userIdFromGeneralHeader, filter ).getId() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the remove selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param filter
     *         the filter
     *
     * @return the selection response UI
     */
    private SelectionResponseUI createRemoveSelection( EntityManager entityManager, String userIdFromGeneralHeader, FiltersDTO filter ) {
        return selectionManager.createSelection( entityManager, userIdFromGeneralHeader, SelectionOrigins.CONTEXT, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMetaDataBySelection( String userIdFromGeneralHeader, String objectId, String selectionKey, String mode ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = getDataObjectEntity( entityManager, userIdFromGeneralHeader, UUID.fromString( objectId ) );

            if ( isEditable( susEntity, userIdFromGeneralHeader ) ) {
                return prepareToDeleteMetaData( entityManager, userIdFromGeneralHeader, objectId, selectionKey, mode );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey(), objectId ) );
            }
        } finally {
            entityManager.close();
        }

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

        if ( !isLifeCycleStatusAllowed( susEntity ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), susEntity.getComposedId().getId() ) );
        }
        if ( susEntity instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getCheckedOut() ) {
            return ( dataObjectEntity.getCheckedOutUser().getId().equals( UUID.fromString( userIdFromGeneralHeader ) ) );
        }
        return true;
    }

    /**
     * Prepare to delete meta data.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param selectionKey
     *         the selection key
     * @param mode
     *         the mode
     *
     * @return true, if successful
     */
    private boolean prepareToDeleteMetaData( EntityManager entityManager, String userIdFromGeneralHeader, String objectId,
            String selectionKey, String mode ) {
        SusDTO susDTO = ( SusDTO ) getDataObject( entityManager, userIdFromGeneralHeader, UUID.fromString( objectId ) );
        isPermittedtoDelete( entityManager, userIdFromGeneralHeader, susDTO.getId().toString() );
        if ( mode.contentEquals( ConstantsMode.BULK ) ) {
            FiltersDTO filtersDTO = contextMenuManager.getFilterBySelectionId( entityManager, selectionKey );
            List< Object > selectionKeys = filtersDTO.getItems();
            deleteMetaDataEntryFromObject( entityManager, userIdFromGeneralHeader, objectId, selectionKeys.toArray( new String[ 0 ] ) );
        } else if ( mode.contentEquals( ConstantsMode.SINGLE ) ) {
            return deleteMetaDataEntryFromObject( entityManager, userIdFromGeneralHeader, objectId, selectionKey );
        } else {
            throw new SusException( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), mode ) );
        }
        selectionManager.sendCustomerEvent( entityManager, userIdFromGeneralHeader,
                susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) ), UPDATE );
        return true;
    }

    /**
     * Delete meta data entry from object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param keys
     *         the keys
     *
     * @return true, if successful
     */
    private boolean deleteMetaDataEntryFromObject( EntityManager entityManager, String userId, String objectId, String... keys ) {
        SusDTO susDTO = ( SusDTO ) getDataObject( entityManager, userId, UUID.fromString( objectId ) );
        isPermittedtoDelete( entityManager, userId, susDTO.getId().toString() );
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
        if ( susEntity instanceof DataObjectEntity dataObjectEntity && dataObjectEntity.getCheckedOutUser() != null
                && !dataObjectEntity.getCheckedOutUser().getId().equals( UUID.fromString( userId ) ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey() ) );
        }
        if ( susEntity != null ) {
            MapFilterUtil metaDataFromVault = readMetaDataFromCurrentObjectVersion( susEntity );
            for ( String key : keys ) {
                metaDataFromVault.remove( key );
            }
            DocumentDTO savedDocumentDTO = createDocumentForObjectMetaData( entityManager, userId,
                    susEntity.getComposedId().getId().toString(), susEntity.getComposedId().getVersionId(), metaDataFromVault );
            updateMetaDataDocumentToObject( entityManager, savedDocumentDTO, susEntity, userId, ConstantsString.DELETE );
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ManageObjectDTO > showPermittedUsersAndGroupsForObject( FiltersDTO filter, UUID objectId,
            String userIdFromGeneralHeader ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( filter == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OBJECT.getKey() ) );
            }
            boolean isManageable = permissionManager.isManageable( entityManager, UUID.fromString( userIdFromGeneralHeader ), objectId );
            List< ManageObjectDTO > objectManagerDTOs = permissionManager.prepareObjectManagerDTOs( entityManager, objectId, isManageable,
                    filter );

            // in case some changes reduced number of records to less than the required size to be on current page
            decreasePageNumberToListSize( filter, objectManagerDTOs.size() );

            List< ManageObjectDTO > filteredList = objectManagerDTOs.subList( filter.getStart(),
                    ( objectManagerDTOs.size() - filter.getStart() ) <= filter.getLength() ? objectManagerDTOs.size()
                            : ( filter.getStart() + filter.getLength() ) );

            return PaginationUtil.constructFilteredResponse( filter, filteredList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Decrease page number to list size.
     *
     * @param filter
     *         the filter
     * @param size
     *         the size
     */
    private void decreasePageNumberToListSize( FiltersDTO filter, int size ) {
        if ( size <= ConstantsInteger.INTEGER_VALUE_ZERO ) {
            filter.setStart( ConstantsInteger.INTEGER_VALUE_ZERO );
        } else if ( filter.getStart() >= size ) {
            filter.setStart( Math.max( ( filter.getStart() - filter.getLength() ), ConstantsInteger.INTEGER_VALUE_ZERO ) );
            decreasePageNumberToListSize( filter, size );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm prepareMetaDataFormForEdit( String userIdFromGeneralHeader, String objectId, String key ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SuSEntity susEntity = getDataObjectEntity( entityManager, userIdFromGeneralHeader, UUID.fromString( objectId ) );
        try {
            if ( isEditable( susEntity, userIdFromGeneralHeader ) ) {
                return GUIUtils.createFormFromItems( editMataDataForm( entityManager, userIdFromGeneralHeader, objectId, key ) );
            } else {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey(), objectId ) );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Edits the mata data form.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param key
     *         the key
     *
     * @return the list
     */
    private List< UIFormItem > editMataDataForm( EntityManager entityManager, String userId, String objectId, String key ) {
        SusDTO susDTO = ( SusDTO ) getDataObject( entityManager, userId, UUID.fromString( objectId ) );
        isPermittedtoUpdate( entityManager, userId, susDTO.getId().toString() );
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );

        List< UIFormItem > itemFormList;
        if ( susEntity != null ) {
            MapFilterUtil metaDataFromVault = readMetaDataFromCurrentObjectVersion( susEntity );
            MetaDataEntryDTO metaDataEntryDTOToSet = new MetaDataEntryDTO();
            metaDataEntryDTOToSet.setKey( key );
            metaDataEntryDTOToSet.setValue( metaDataFromVault.get( key ) );
            itemFormList = GUIUtils.prepareForm( true, metaDataEntryDTOToSet );
            for ( UIFormItem uiFormItem : itemFormList ) {
                if ( uiFormItem.getLabel().equalsIgnoreCase( ObjectMetaDataDTO.KEY_FIELD ) ) {
                    uiFormItem.setReadonly( true );
                }
            }
            return itemFormList;
        }

        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeObjectPermissions( String userId, String objectId, Map< String, String > map ) {
        log.debug( ">>changeObjectPermissions" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            boolean isPermitted;
            isPermittedtoManage( entityManager, userId, objectId );
            AclObjectIdentityEntity aclObjectIdentityEntity = permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );
            if ( aclObjectIdentityEntity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.RESOURCE_NOT_BINDED_WITH_ACL.getKey() ) );
            }
            if ( map.get( SELECTION ).equals( INHERIT_FALSE ) ) {
                isPermitted = setPermissionInheritedFalse( entityManager, aclObjectIdentityEntity, map );
            } else {
                isPermitted = setPermissionInheritedTrue( entityManager, aclObjectIdentityEntity );
            }

            new Thread( () -> {
                EntityManager threadEntityManager = entityManagerFactory.createEntityManager();
                try {
                    SuSEntity entity = susDAO.getLatestNonDeletedObjectById( threadEntityManager, UUID.fromString( objectId ) );
                    SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                            entity.getConfig() );
                    if ( null == entity.getUserSelectionId() && null == entity.getGroupSelectionId() ) {
                        SuSEntity parentEntityForInheritPermission = getRecursiveParentForPermission( threadEntityManager, entity );
                        entity.setUserSelectionId( parentEntityForInheritPermission.getUserSelectionId() );
                        entity.setGroupSelectionId( parentEntityForInheritPermission.getGroupSelectionId() );
                        saveOrUpdateIndexEntity( threadEntityManager, suSObjectModel, entity );
                        updateInheritedChildInSearch( threadEntityManager, UUID.fromString( objectId ),
                                parentEntityForInheritPermission.getUserSelectionId(),
                                parentEntityForInheritPermission.getGroupSelectionId() );
                    } else {
                        saveOrUpdateIndexEntity( threadEntityManager, suSObjectModel, entity );
                        updateInheritedChildInSearch( threadEntityManager, UUID.fromString( objectId ), entity.getUserSelectionId(),
                                entity.getGroupSelectionId() );
                    }
                } finally {
                    threadEntityManager.close();
                }
            } ).start();
            return isPermitted;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets recursive parent for permission.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return recursive parent for permission
     */
    private SuSEntity getRecursiveParentForPermission( EntityManager entityManager, SuSEntity entity ) {
        List< SuSEntity > parent = susDAO.getParents( entityManager, entity );
        if ( null == parent.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getGroupSelectionId() && null == parent.get(
                ConstantsInteger.INTEGER_VALUE_ZERO ).getUserSelectionId() ) {
            return getRecursiveParentForPermission( entityManager, parent.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        }
        return parent.get( ConstantsInteger.INTEGER_VALUE_ZERO );
    }

    /**
     * Save inherited child in search recursively.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userSelectionId
     *         the user selection id
     * @param groupSelectionId
     *         the group selection id
     */
    private void updateInheritedChildInSearch( EntityManager entityManager, UUID objectId, String userSelectionId,
            String groupSelectionId ) {
        List< SuSEntity > childs = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class, objectId );
        for ( SuSEntity suSEntity : childs ) {
            AclObjectIdentityEntity aclObjectIdentityEntity = permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, suSEntity.getComposedId().getId() );
            if ( aclObjectIdentityEntity.isInherit() ) {
                SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( suSEntity.getTypeId().toString(),
                        suSEntity.getConfig() );
                suSEntity.setGroupSelectionId( groupSelectionId );
                suSEntity.setUserSelectionId( userSelectionId );
                saveOrUpdateIndexEntity( entityManager, suSObjectModel, suSEntity );
            }
            updateInheritedChildInSearch( entityManager, suSEntity.getComposedId().getId(), userSelectionId, groupSelectionId );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm getUpdateObjectPermissionUI( String userId, String objectId, String option ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > formItems;

            formItems = new ArrayList<>();
            TableFormItem usersTable = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            usersTable.setMultiple( true );
            usersTable.setName( USER_KEY );
            usersTable.setLabel( MessageBundleFactory.getMessage( Messages.SELECTED_USERS.getKey() ) );
            usersTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_USERS.getKey() ) );
            usersTable.setType( SELECTION_TYPE_TABLE );
            usersTable.setConnected( USER_SELECTION_PATH );
            usersTable.setConnectedTableLabel( USER_SELECTION_CONNECTED_TABLE_LABEL );
            usersTable.setValue( getUserSelectionIdForPermission( entityManager, userId, objectId ) );
            usersTable.setSelectable( null );
            usersTable.setReadonly( INHERIT_TRUE.contentEquals( option ) );
            formItems.add( usersTable );

            TableFormItem groupsTable = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
            groupsTable.setMultiple( true );
            groupsTable.setName( GROUP_KEY );
            groupsTable.setLabel( MessageBundleFactory.getMessage( Messages.SELECTED_GROUPS.getKey() ) );
            groupsTable.setButtonLabel( MessageBundleFactory.getMessage( Messages.SELECT_GROUPS.getKey() ) );
            groupsTable.setType( SELECTION_TYPE_TABLE );
            groupsTable.setConnected( GROUP_SELECTION_PATH );
            groupsTable.setConnectedTableLabel( GROUP_SELECTION_CONNECTED_TABLE_LABEL );
            groupsTable.setSelectable( FIELD_NAME_OBJECT_STATUS );
            groupsTable.setValue( getGroupSelectionIdForPermission( entityManager, userId, objectId ) );
            groupsTable.setReadonly( INHERIT_TRUE.contentEquals( option ) );
            formItems.add( groupsTable );

            return GUIUtils.createFormFromItems( formItems );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Get user selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object Id
     *
     * @return user selection id
     */
    private String getUserSelectionIdForPermission( EntityManager entityManager, String userId, String objectId ) {
        List< String > objectUsers = permissionManager.getObjectUserIds( entityManager, UUID.fromString( objectId ) );

        if ( CollectionUtil.isNotEmpty( objectUsers ) ) {
            return selectionManager.createSelectionForMultipleItems( entityManager, userId, SelectionOrigins.PERMISSION.getOrigin(),
                    objectUsers ).getId();
        } else {
            return null;
        }
    }

    /**
     * Get group selection id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object Id
     *
     * @return group selection id
     */
    private String getGroupSelectionIdForPermission( EntityManager entityManager, String userId, String objectId ) {
        List< String > objectGroups = permissionManager.getObjectGroupIds( entityManager, UUID.fromString( objectId ) );

        if ( CollectionUtil.isNotEmpty( objectGroups ) ) {
            return selectionManager.createSelectionForMultipleItems( entityManager, userId, SelectionOrigins.PERMISSION.getOrigin(),
                    objectGroups ).getId();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm changeStatusObjectOptionForm( String userId, UUID objectId ) {
        log.debug( "entered in changeObjectOptionForm method" );
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        selectFormItem.setLabel( NEW_STATUS_LABEL );
        selectFormItem.setName( FIELD_NAME_OBJECT_STATUS );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SuSEntity entity;
        try {
            entity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        } finally {
            entityManager.close();
        }

        List< String > moveStatusesList = lifeCycleManager.getLifeCycleStatusByStatusId( entity.getLifeCycleStatus() ).getCanMoveToStatus();

        for ( String statusId : moveStatusesList ) {
            StatusConfigDTO status = lifeCycleManager.getLifeCycleStatusByStatusId( statusId );
            options.add( new SelectOptionsUI( status.getId(), status.getName() ) );
        }
        setRulesAndMessageOnUI( selectFormItem );
        selectFormItem.setOptions( options );

        return GUIUtils.createFormFromItems( List.of( selectFormItem ) );

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm changeStatusObjectVersionOptionForm( String userId, UUID objectId, int versionId ) {
        log.debug( ">>changeStatusObjectVersionOptionForm method" );
        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
        List< SelectOptionsUI > options = new ArrayList<>();
        selectFormItem.setLabel( NEW_STATUS_LABEL );
        selectFormItem.setName( FIELD_NAME_OBJECT_STATUS );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SuSEntity entity;
        try {
            entity = susDAO.getObjectByCompositeId( entityManager, SuSEntity.class, objectId, versionId );
        } finally {
            entityManager.close();
        }

        List< String > moveStatusesList = lifeCycleManager.getLifeCycleStatusByStatusId( entity.getLifeCycleStatus() ).getCanMoveToStatus();

        for ( String statusId : moveStatusesList ) {
            StatusConfigDTO status = lifeCycleManager.getLifeCycleStatusByStatusId( statusId );
            options.add( new SelectOptionsUI( status.getId(), status.getName() ) );
        }

        selectFormItem.setOptions( options );

        return GUIUtils.createFormFromItems( List.of( selectFormItem ) );

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeStatusObject( String userIdFromGeneralHeader, UUID objectId, ChangeStatusDTO changeStatusDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return changeStatusObject( entityManager, userIdFromGeneralHeader, objectId, changeStatusDTO );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean changeStatusObject( EntityManager entityManager, String userIdFromGeneralHeader, UUID objectId,
            ChangeStatusDTO changeStatusDTO ) {
        isPermittedtoWrite( entityManager, userIdFromGeneralHeader, objectId.toString() );
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
     * Change status object version.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param objectId
     *         the object id
     * @param changeStatusDTO
     *         the change status DTO
     * @param versionId
     *         the version id
     *
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#changeStatusObjectVersion(java.lang.String, java.util.UUID,
     * de.soco.software.simuspace.suscore.object.model.ChangeStatusDTO, int)
     */
    @Override
    public boolean changeStatusObjectVersion( String userIdFromGeneralHeader, UUID objectId, ChangeStatusDTO changeStatusDTO,
            int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getObjectByCompositeId( entityManager, SuSEntity.class, objectId, versionId );
            susEntity.setLifeCycleStatus( changeStatusDTO.getStatus() );
            changeStatusInVersionsRecursively( entityManager, susEntity );
            susDAO.update( entityManager, susEntity );
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDataObjectPreview( UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                    DataObjectEntity.class, objectId );
            return getDataObjectPreviewFromObjectEntity( dataObjectEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDataObjectVersionPreview( UUID userId, UUID objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeId( entityManager, DataObjectEntity.class,
                    objectId, versionId );
            return getDataObjectPreviewFromObjectEntity( dataObjectEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * gets the preview document from data object entity
     *
     * @param dataObjectEntity
     *         the data Object Entity
     *
     * @return the preview documentDTO
     */
    private DocumentDTO getDataObjectPreviewFromObjectEntity( DataObjectEntity dataObjectEntity ) {
        UserDTO user = TokenizedLicenseUtil.getUser(
                BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
        if ( null != user && null != user.getUserDetails() ) {
            String userLang = user.getUserDetails().iterator().next().getLanguage();
            if ( PropertiesManager.hasTranslation() && de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty(
                    userLang ) ) {
                dataObjectEntity.getTranslation().forEach( translation -> {
                    if ( userLang.equals( translation.getLanguage() ) && null != translation.getFile()
                            && dataObjectEntity instanceof DataObjectPDFEntity ) {
                        dataObjectEntity.setFile( translation.getFile() );
                    }
                } );
            }
        }
        DocumentDTO documentDTO = null;
        if ( dataObjectEntity instanceof DataObjectImageEntity imageEntity && imageEntity.getFile() != null ) {
            documentDTO = documentManager.prepareDocumentDTO( imageEntity.getFile() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + documentDTO.getName();
            documentDTO.setUrl( url );

        } else if ( dataObjectEntity instanceof DataObjectMovieEntity movieEntity && movieEntity.getPreviewImage() != null ) {
            documentDTO = documentManager.prepareDocumentDTO( movieEntity.getPreviewImage() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + documentDTO.getName() + "." + ConstantsFileExtension.PNG;
            documentDTO.setUrl( url );

        } else if ( dataObjectEntity instanceof DataObjectPDFEntity pdfEntity && pdfEntity.getFile() != null ) {
            documentDTO = documentManager.prepareDocumentDTO( pdfEntity.getFile() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + documentDTO.getName();
            documentDTO.setUrl( url );

        }
        return documentDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectCurveDTO getDataObjectCurve( UUID objectId, CurveUnitDTO curveUnitDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObjectCurve( entityManager, objectId, curveUnitDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectCurveDTO getDataObjectCurve( EntityManager entityManager, UUID objectId, CurveUnitDTO curveUnitDTO ) {
        DataObjectCurveEntity dataObjectEntity = ( DataObjectCurveEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                DataObjectCurveEntity.class, objectId );
        return getDataObjectCurveBySusEntity( entityManager, curveUnitDTO, dataObjectEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectCurveDTO getDataObjectCurveByVersion( UUID objectId, CurveUnitDTO curveUnitDTO, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObjectCurveByVersion( entityManager, objectId, curveUnitDTO, versionId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectCurveDTO getDataObjectCurveByVersion( EntityManager entityManager, UUID objectId, CurveUnitDTO curveUnitDTO,
            int versionId ) {
        DataObjectCurveEntity dataObjectEntity = ( DataObjectCurveEntity ) susDAO.getObjectByIdAndVersion( entityManager,
                DataObjectCurveEntity.class, objectId, versionId );
        return getDataObjectCurveBySusEntity( entityManager, curveUnitDTO, dataObjectEntity );
    }

    /**
     * Gets the data object curve by sus entity.
     *
     * @param entityManager
     *         the entity manager
     * @param curveUnitDTO
     *         the curve unit DTO
     * @param dataObjectEntity
     *         the data object entity
     *
     * @return the data object curve by sus entity
     */
    private DataObjectCurveDTO getDataObjectCurveBySusEntity( EntityManager entityManager, CurveUnitDTO curveUnitDTO,
            DataObjectCurveEntity dataObjectEntity ) {
        DataObjectCurveDTO curveDTO = new DataObjectCurveDTO();
        String seperator = null;
        boolean cb2 = false;
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
                        if ( !locationEntity.getName().equals( DEFAULT ) && BooleanUtils.isTrue( dataObjectEntity.isAutoDelete() ) ) {
                            return curveDTO;
                        }
                    }
                    // mapping file to DataObjectCurveDTO object if it doest map then its CB2 file
                    try ( InputStream stream = getDocumentStream( documentDTO, locationEntity ) ) {
                        if ( null == stream ) {
                            return curveDTO;
                        }
                        curveDTO = JsonUtils.jsonToObject( stream, DataObjectCurveDTO.class );
                        convertCurveCoordinatesToSIValues( curveDTO, curveUnitDTO );
                        return curveDTO;
                    } catch ( Exception e ) {
                        log.error( "Map Object Failed : Map to Cb2 file" + documentDTO.getName(), e );
                    }
                    // if not mapped to curve object, read cb2 file and prepare value , unit and unit families
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
                            convertCurveCoordinatesToSIValues( curveDTO, curveUnitDTO );
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
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Override
    public DataObjectCurveDTO getDataObjectVersionCurve( UUID objectId, int versionId ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectCurveDTO curveDTO = new DataObjectCurveDTO();
            boolean cb2 = false;
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeId( entityManager, DataObjectEntity.class,
                    objectId, versionId );

            if ( null != dataObjectEntity ) {
                SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() );

                if ( susModel.getClassName().equalsIgnoreCase( DataObjectCurveDTO.class.getName() )
                        && dataObjectEntity.getFile() != null ) {

                    DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );

                    if ( documentDTO != null ) {

                        InputStream inputStream = documentManager.readVaultFromDisk( documentDTO );

                        byte[] byteArray = IOUtils.toByteArray( inputStream );
                        inputStream.close();

                        try ( InputStream inputS1 = new ByteArrayInputStream( byteArray ); BufferedReader br = new BufferedReader(
                                new InputStreamReader( inputS1 ) ) ) {
                            String line;
                            String seperator = null;
                            List< double[] > convertedValues = new ArrayList<>();
                            while ( ( line = br.readLine() ) != null ) {
                                if ( line.contains( ConstantsString.COLON ) ) {
                                    seperator = prepareCurveModelByCB2File( curveDTO, seperator, line );
                                } else if ( line.contains( ConstantsString.COMMA ) ) {
                                    prepareCurvePointsFromCB2File( line, convertedValues );
                                    cb2 = true;
                                }
                            }

                            if ( !cb2 ) {
                                try ( InputStream fileFromVaultStream = new ByteArrayInputStream( byteArray ) ) {
                                    curveDTO = JsonUtils.jsonToObject( fileFromVaultStream, DataObjectCurveDTO.class );
                                }
                            }
                        } catch ( Exception e ) {
                            log.error( e.getMessage(), e );
                        }
                    }
                }

            }
            return curveDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObjectVersionHtml( UUID objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeId( entityManager, DataObjectEntity.class,
                    objectId, versionId );
            return readHtmlJsonFromHtmlObject( dataObjectEntity );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectMovieDTO getDataObjectMovie( UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObjectMovie( entityManager, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectMovieDTO getDataObjectMovie( EntityManager entityManager, UUID objectId ) {
        DataObjectMovieDTO moviewDTO = null;
        DataObjectMovieEntity dataObjectEntity = ( DataObjectMovieEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                DataObjectEntity.class, objectId );

        if ( null != dataObjectEntity ) {
            SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                    dataObjectEntity.getConfig() );

            if ( susModel.getClassName().equalsIgnoreCase( DataObjectMovieDTO.class.getName() ) && dataObjectEntity.getFile() != null ) {

                DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );

                if ( documentDTO != null ) {

                    moviewDTO = prepareMovieDto( dataObjectEntity );

                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.MOVIE_NOT_FOUND.getKey() ) );
            }

        }
        return moviewDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectMovieDTO getDataObjectVersionMovie( UUID objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectMovieDTO moviewDTO = null;
            DataObjectMovieEntity dataObjectEntity = ( DataObjectMovieEntity ) susDAO.getObjectByCompositeId( entityManager,
                    DataObjectMovieEntity.class, objectId, versionId );
            if ( null != dataObjectEntity ) {
                SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() );

                if ( susModel.getClassName().equalsIgnoreCase( DataObjectMovieDTO.class.getName() )
                        && dataObjectEntity.getFile() != null ) {

                    DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );

                    if ( documentDTO != null ) {

                        moviewDTO = prepareMovieDto( dataObjectEntity );

                    }
                } else {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.MOVIE_NOT_FOUND.getKey() ) );
                }

            }
            return moviewDTO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare movie dto.
     *
     * @param dataObjectEntity
     *         the data object entity
     *
     * @return the data object movie DTO
     */
    private DataObjectMovieDTO prepareMovieDto( DataObjectMovieEntity dataObjectEntity ) {

        DataObjectMovieDTO moviewDTO = new DataObjectMovieDTO();
        DocumentDTO movieDocument = documentManager.prepareDocumentDTO( dataObjectEntity.getFile() );

        String srcFilePathOnVault = PropertiesManager.getVaultPath() + movieDocument.getPath();

        String srcfileName = srcFilePathOnVault.substring( srcFilePathOnVault.lastIndexOf( ConstantsString.FORWARD_SLASH ) + 1 );
        if ( dataObjectEntity.getPreviewImage() != null ) {
            moviewDTO.setPreviewImage( documentManager.prepareDocumentDTO( dataObjectEntity.getPreviewImage() ) );
        }

        if ( dataObjectEntity.getPreviewImage() != null ) {
            DocumentDTO previewDoc = prepareUrl( dataObjectEntity.getPreviewImage() );
            String poster = previewDoc != null ? previewDoc.getUrl() + ConstantsString.DOT + ConstantsFileExtension.PNG : null;
            moviewDTO.setPoster( poster );
        }

        if ( dataObjectEntity.getThumbnail() != null ) {
            DocumentDTO thumbDoc = prepareUrl( dataObjectEntity.getThumbnail() );

            String thumbPath = thumbDoc != null ? thumbDoc.getUrl() + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX
                    + FilenameUtils.EXTENSION_SEPARATOR + ConstantsFileExtension.PNG : null;
            moviewDTO.setThumbnail( thumbPath );
        }

        if ( dataObjectEntity.getFile() != null ) {
            moviewDTO.setSources( new MovieSources() );

            DocumentDTO documentDTO = documentManager.prepareDocumentDTO( dataObjectEntity.getFile() );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + srcfileName;

            moviewDTO.getSources().setMp4( url + ConstantsFileProperties.Commands.MP4.getExtension() );
            moviewDTO.getSources().setWebm( url + ConstantsFileProperties.Commands.WEBM.getExtension() );
        }

        return moviewDTO;
    }

    /**
     * Prepare url.
     *
     * @param documentEntity
     *         the document entity
     *
     * @return the document DTO
     */
    private DocumentDTO prepareUrl( DocumentEntity documentEntity ) {
        DocumentDTO documentDTO = null;
        if ( null != documentEntity ) {
            documentDTO = documentManager.prepareDocumentDTO( documentEntity );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + FilenameUtils.removeExtension( documentDTO.getName() );

            documentDTO.setUrl( url );

        }
        return documentDTO;
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
     * Sets the permission inherited false.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     * @param map
     *         the map
     *
     * @return true, if successful
     */
    private boolean setPermissionInheritedFalse( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity,
            Map< String, String > map ) {
        log.debug( ">>setPermissionInheritedFalse" );
        if ( aclObjectIdentityEntity.isInherit() ) {
            propagateToTreeParent( entityManager, permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, aclObjectIdentityEntity.getParentObject().getId() ) );

        } else {
            propagateToTreeParent( entityManager, permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, aclObjectIdentityEntity.getId() ) );
        }

        boolean objectInitiallyInherited = aclObjectIdentityEntity.isInherit();

        aclObjectIdentityEntity.setInherit( false );
        aclObjectIdentityEntity.setFinalParentObject( aclObjectIdentityEntity );
        aclObjectIdentityEntity = permissionManager.getObjectIdentityDAO().saveOrUpdate( entityManager, aclObjectIdentityEntity );
        saveSelectionForUsersAndGroupsInObject( entityManager, aclObjectIdentityEntity, map, objectInitiallyInherited );
        permissionManager.setChildrenFinalACL( aclObjectIdentityEntity );
        return true;
    }

    /**
     * Save selection for users and groups for object.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     * @param map
     *         the map
     * @param objectInitiallyInherited
     *         the object initially inherited
     */
    private void saveSelectionForUsersAndGroupsInObject( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity,
            Map< String, String > map, boolean objectInitiallyInherited ) {
        log.debug( ">>saveSelectionForUsersAndGroupsInObject" );
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, aclObjectIdentityEntity.getId() );

        String userSelectionId = null;
        String groupSelectionId = null;
        List< UUID > newUserSelection = new ArrayList<>();
        List< UUID > newGroupSelection = new ArrayList<>();
        if ( map.get( USER_KEY ) != null && !map.get( USER_KEY ).isEmpty() ) {
            userSelectionId = map.get( USER_KEY );
            newUserSelection = selectionManager.getSelectedIdsListBySelectionId( entityManager, map.get( USER_KEY ) );
            if ( newUserSelection.isEmpty() ) {
                List< UserEntity > allUsers = userCommonManager.getUserCommonDAO().getAllRecords( entityManager );
                List< UUID > itemsList = new ArrayList<>();
                allUsers.forEach( entity -> itemsList.add( entity.getId() ) );
                prepareAndSaveItemSelections( entityManager, itemsList,
                        selectionManager.getSelectionDAO().findById( entityManager, UUID.fromString( userSelectionId ) ) );
                newUserSelection.addAll( itemsList );
            }
        }
        if ( map.get( GROUP_KEY ) != null && !map.get( GROUP_KEY ).isEmpty() ) {
            groupSelectionId = map.get( GROUP_KEY );
            newGroupSelection = selectionManager.getSelectedIdsListBySelectionId( entityManager, map.get( GROUP_KEY ) );

            if ( newGroupSelection.isEmpty() ) {
                List< GroupEntity > allGroups = userCommonManager.getGroupCommonDAO().getAllRecords( entityManager );
                List< UUID > itemsList = new ArrayList<>();
                allGroups.forEach( entity -> itemsList.add( entity.getId() ) );
                prepareAndSaveItemSelections( entityManager, itemsList,
                        selectionManager.getSelectionDAO().findById( entityManager, UUID.fromString( groupSelectionId ) ) );
                newGroupSelection.addAll( itemsList );
            }
        }
        List< UUID > userGroupSelection = new ArrayList<>();
        userGroupSelection.addAll( newUserSelection );
        userGroupSelection.addAll( newGroupSelection );
        dataProjectManager.applyPermissionsOnSelectedUsersOrGroups( entityManager, aclObjectIdentityEntity, userGroupSelection,
                objectInitiallyInherited );
        susEntity.setUserSelectionId( userSelectionId );
        susEntity.setGroupSelectionId( groupSelectionId );

        susDAO.saveOrUpdate( entityManager, susEntity );
        saveOrUpdateIndexEntity( entityManager,
                configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ), susEntity );
    }

    /**
     * Prepare and save item selections.
     *
     * @param entityManager
     *         the entity manager
     * @param list
     *         the list
     * @param exitingSelection
     *         the exiting selection
     */
    private void prepareAndSaveItemSelections( EntityManager entityManager, List< UUID > list, SelectionEntity exitingSelection ) {
        if ( CollectionUtils.isNotEmpty( list ) ) {
            Set< SelectionItemEntity > itemEntities = new HashSet<>();
            int order = 0;
            for ( Object item : list ) {
                SelectionItemEntity entity = new SelectionItemEntity();
                entity.setId( UUID.randomUUID() );
                entity.setSelectionEntity( exitingSelection );
                entity.setItem( item.toString() );
                entity.setItemOrder( order );
                itemEntities.add( entity );
                order++;
            }
            exitingSelection.setItems( itemEntities );
        }
        if ( selectionManager.getSelectionDAO().saveOrUpdate( entityManager, exitingSelection ) == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.SELECTION_ITEMS_NOT_SAVED.getKey() ) );
        }
    }

    /**
     * Propagate to tree parent.
     *
     * @param entityManager
     *         the entity manager
     * @param parentObjectIdentityEntity
     *         the parent object identity entity
     *
     * @return the object identity entity
     */
    private AclObjectIdentityEntity propagateToTreeParent( EntityManager entityManager,
            AclObjectIdentityEntity parentObjectIdentityEntity ) {
        if ( parentObjectIdentityEntity.isInherit() ) {
            parentObjectIdentityEntity = permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, parentObjectIdentityEntity.getParentObject().getId() );
            return propagateToTreeParent( entityManager, parentObjectIdentityEntity );
        } else {
            return parentObjectIdentityEntity;
        }
    }

    /**
     * Gets the sid list by sid ids.
     *
     * @param entityManager
     *         the entity manager
     * @param sidIds
     *         the sid ids
     *
     * @return the sid list by sid ids
     */
    private List< AclSecurityIdentityEntity > getSidListBySidIds( EntityManager entityManager, List< UUID > sidIds ) {
        List< AclSecurityIdentityEntity > sidEntities = new ArrayList<>();
        for ( UUID newUser : sidIds ) {
            sidEntities.add( userCommonManager.getSecurityIdentityBySidId( entityManager, newUser ) );
        }
        return sidEntities;
    }

    /**
     * Sets the permission inherited true.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     *
     * @return true, if successful
     */
    private boolean setPermissionInheritedTrue( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity ) {

        if ( aclObjectIdentityEntity.isInherit() ) {
            log.info( Messages.PERMISSION_ALREADY_INHERITED.getKey() );
            return true;
        } else if ( aclObjectIdentityEntity.getParentObject() == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_ON_NULL_PARENT.getKey() ) );
        } else {
            aclObjectIdentityEntity.setFinalParentObject( aclObjectIdentityEntity.getParentObject().getFinalParentObject() );
            aclObjectIdentityEntity.setInherit( true );
            permissionManager.getObjectIdentityDAO().saveOrUpdate( entityManager, aclObjectIdentityEntity );
            removeACEsByObjectIdentityId( entityManager, aclObjectIdentityEntity );
            removeSelection( entityManager, aclObjectIdentityEntity.getId() );
            permissionManager.setChildrenFinalACL( aclObjectIdentityEntity );
            return true;
        }
    }

    /**
     * Removes the selection.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     */
    private void removeSelection( EntityManager entityManager, UUID objectId ) {
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
        if ( susEntity != null ) {
            if ( susEntity.getUserSelectionId() != null && !susEntity.getUserSelectionId().isEmpty() ) {
                removeUserSelection( entityManager, susEntity );
            }
            if ( susEntity.getGroupSelectionId() != null && !susEntity.getGroupSelectionId().isEmpty() ) {
                removeGroupSelection( entityManager, susEntity );
            }
            susDAO.saveOrUpdate( entityManager, susEntity );
        }
    }

    /**
     * Removes the group selection.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     */
    private void removeGroupSelection( EntityManager entityManager, SuSEntity susEntity ) {
        List< UUID > userSelectioItems = selectionManager.getSelectedIdsListBySelectionId( entityManager, susEntity.getGroupSelectionId() );
        if ( CollectionUtils.isNotEmpty( userSelectioItems ) ) {
            for ( UUID uuid : userSelectioItems ) {
                selectionManager.removeSelection( entityManager, uuid, false );
            }
            selectionManager.removeSelection( entityManager, UUID.fromString( susEntity.getGroupSelectionId() ), true );
            susEntity.setGroupSelectionId( null );
        }
    }

    /**
     * Removes the user selection.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     */
    private void removeUserSelection( EntityManager entityManager, SuSEntity susEntity ) {
        List< UUID > userSelectioItems = selectionManager.getSelectedIdsListBySelectionId( entityManager, susEntity.getUserSelectionId() );
        if ( CollectionUtils.isNotEmpty( userSelectioItems ) ) {
            for ( UUID uuid : userSelectioItems ) {
                selectionManager.removeSelection( entityManager, uuid, false );
            }
            selectionManager.removeSelection( entityManager, UUID.fromString( susEntity.getUserSelectionId() ), true );
            susEntity.setUserSelectionId( null );
        }
    }

    /**
     * Removes the AC es by object identity id.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     */
    private void removeACEsByObjectIdentityId( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity ) {
        AclEntryDAO aclEntryDAO = permissionManager.getEntryDAO();
        List< AclEntryEntity > aclEntryEntities = aclEntryDAO.getAclEntryListByObjectId( entityManager, aclObjectIdentityEntity.getId() );
        if ( CollectionUtils.isNotEmpty( aclEntryEntities ) ) {
            for ( AclEntryEntity aclEntryEntity : aclEntryEntities ) {
                aclEntryDAO.delete( entityManager, aclEntryEntity );
            }
        }
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
     * Convert JSON to form items.
     *
     * @param customAttributes
     *         the custom attributes
     * @param attributeEntities
     *         the attribute entities
     *
     * @return the list
     */
    private List< UIFormItem > convertCustomAttributeJSONtoFormItems( List< CustomAttributeDTO > customAttributes,
            Set< CustomAttributeEntity > attributeEntities ) {

        log.debug( "entered convertCustomAttributeJSONtoFormItems method" );

        List< UIFormItem > toReturn = new ArrayList<>();
        for ( CustomAttributeDTO item : customAttributes ) {
            SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            selectFormItem.setLabel( item.getTitle() );
            selectFormItem.setName( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + item.getName() );
            selectFormItem.setType( item.getType() );
            selectFormItem.setValue( getItemValue( attributeEntities, item ) );
            selectFormItem.setRules( item.getRules() );
            selectFormItem.setCanBeEmpty( item.isCanBeEmpty() );
            selectFormItem.setBindVisibility( adaptBindVisibilityToCustomAttributes( item.getBindVisibility() ) );
            selectFormItem.setTooltip( item.getTooltip() );

            if ( item.getType().contains( UIColumnType.SELECT.getKey() ) ) {
                Map< Object, Object > map = new HashMap<>();

                List< ? > validOptions = item.getOptions();
                for ( Object op : validOptions ) {
                    map.put( op, op );
                }
                selectFormItem.setOptions( GUIUtils.getSelectBoxOptions( map ) );
                selectFormItem.setMultiple( item.isMultiple() );
            }
            toReturn.add( selectFormItem );
        }

        log.debug( "leaving convertCustomAttributeJSONtoFormItems method" );

        return toReturn;
    }

    /**
     * Adapt bind visibility to custom attributes.
     *
     * @param bindVisibility
     *         the bind visibility
     *
     * @return the bind visibility
     */
    private BindVisibility adaptBindVisibilityToCustomAttributes( BindVisibility bindVisibility ) {
        if ( bindVisibility != null ) {
            BindVisibility newBindVisibility = new BindVisibility();
            newBindVisibility.setName( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + bindVisibility.getName() );
            newBindVisibility.setValue( bindVisibility.getValue() );
            return newBindVisibility;
        }
        return null;
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
    private Object getItemValue( Set< CustomAttributeEntity > attributeEntities, CustomAttributeDTO item ) {
        Object itemValue = null;
        if ( attributeEntities != null ) {
            for ( CustomAttributeEntity customAttributeEntity : attributeEntities ) {

                if ( item.getName().equals( customAttributeEntity.getName() ) ) {
                    itemValue = ByteUtil.convertByteToObject( customAttributeEntity.getValue() );
                    break;
                }
            }
        } else {
            itemValue = item.getValue();
        }
        return itemValue;
    }

    /**
     * Creates the project DTO from project entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the project DTO
     */
    private ProjectDTO createProjectDTOFromProjectEntity( ProjectEntity susEntity ) {
        ProjectDTO projectDTO = null;
        if ( susEntity != null ) {
            projectDTO = new ProjectDTO();
            projectDTO.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
            projectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            projectDTO.setName( susEntity.getName() );
            projectDTO.setDescription( susEntity.getDescription() );
            projectDTO.setCreatedOn( susEntity.getCreatedOn() );
            projectDTO.setModifiedOn( susEntity.getModifiedOn() );
            projectDTO.setAutoDeleted( susEntity.isAutoDelete() );
            projectDTO.setConfig( susEntity.getConfig() );
            projectDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
            projectDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            projectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                    susEntity.getConfig() ) );
            projectDTO.setTypeId( susEntity.getTypeId() );
            if ( null != susEntity.getCreatedBy() ) {
                projectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                projectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }

            projectDTO.setType( susEntity.getType() );
        }
        return projectDTO;
    }

    /**
     * Creates the data object DTO from data object entity.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the data object entity
     *
     * @return the data object DTO
     */
    private Object createObjectDTOFromObjectEntity( EntityManager entityManager, SuSEntity susEntity ) {
        return prepareHtmlDtoAndExtractZipFile( entityManager, susEntity );
    }

    /**
     * Prepare data object DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object prepareHtmlDtoAndExtractZipFile( EntityManager entityManager, SuSEntity susEntity ) {
        DataObjectDTO dataObjectDTO = new DataObjectDTO();
        dataObjectDTO.setName( susEntity.getName() );
        dataObjectDTO.setId( susEntity.getComposedId().getId() );
        dataObjectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
        dataObjectDTO.setCreatedOn( susEntity.getCreatedOn() );
        dataObjectDTO.setModifiedOn( susEntity.getModifiedOn() );
        dataObjectDTO.setParentId( null );
        dataObjectDTO.setTypeId( susEntity.getTypeId() );
        dataObjectDTO.setAutoDeleted( susEntity.isAutoDelete() );
        dataObjectDTO.setSize( susEntity.getSize() != null && susEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( susEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );

        UserDTO userById = userCommonManager.getUserById( entityManager, susEntity.getOwner().getId() );
        if ( userById != null ) {
            dataObjectDTO.setCreatedBy( userById );
        }
        dataObjectDTO.setDescription( susEntity.getDescription() );

        dataObjectDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
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
     * Prepare report DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object prepareReportDTO( EntityManager entityManager, SuSEntity susEntity ) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setName( susEntity.getName() );
        reportDTO.setId( susEntity.getComposedId().getId() );
        reportDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
        reportDTO.setCreatedOn( susEntity.getCreatedOn() );
        reportDTO.setModifiedOn( susEntity.getModifiedOn() );
        reportDTO.setParentId( null );
        reportDTO.setTypeId( susEntity.getTypeId() );

        UserDTO userById = userCommonManager.getUserById( entityManager, susEntity.getOwner().getId() );
        if ( userById != null ) {
            reportDTO.setCreatedBy( userById );
        }
        reportDTO.setDescription( susEntity.getDescription() );
        reportDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );

        if ( susEntity.getTypeId() != null ) {
            reportDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                    susEntity.getConfig() ) );
        }

        reportDTO.setLocations(
                Collections.singletonList( locationManager.getLocation( entityManager, LocationsEnum.DEFAULT_LOCATION.getId() ) ) );

        if ( null != susEntity.getModifiedBy() ) {
            reportDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
        }
        if ( null != susEntity.getConfig() ) {
            reportDTO.setType(
                    configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
        }

        return reportDTO;
    }

    /**
     * Creates the object image DTO from object entity.
     *
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    @Override
    public Object createObjectImageDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        return dataProjectManager.createObjectImageDTOFromObjectEntity( projectId, susEntity, setCustomAttributes );
    }

    /**
     * Creates the variant DTO from object entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object createVariantDTOFromObjectEntity( SuSEntity susEntity ) {
        Object objectDTO = null;

        if ( susEntity instanceof VariantEntity ) {
            VariantDTO variantDTO = new VariantDTO();
            variantDTO.setName( susEntity.getName() );
            variantDTO.setId( susEntity.getComposedId().getId() );
            variantDTO.setCreatedOn( susEntity.getCreatedOn() );
            variantDTO.setModifiedOn( susEntity.getModifiedOn() );
            variantDTO.setDescription( susEntity.getDescription() );
            variantDTO.setParentId( null );
            variantDTO.setAutoDeleted( susEntity.isAutoDelete() );
            variantDTO.setTypeId( susEntity.getTypeId() );
            variantDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            if ( null != susEntity.getCreatedBy() ) {
                variantDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            variantDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            if ( susEntity.getTypeId() != null ) {
                variantDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                                susEntity.getConfig() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                variantDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            if ( null != susEntity.getConfig() ) {
                variantDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
            }

            objectDTO = variantDTO;
        }

        return objectDTO;
    }

    /**
     * Creates the variant DTO from object entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object createProjectDTOFromProjectEntityWithOverview( SuSEntity susEntity ) {
        Object objectDTO = null;

        if ( susEntity instanceof ProjectEntity projectOverviewEntity ) {
            ProjectDTO projectOverviewDTO;
            if ( null != projectOverviewEntity.getHtmlJson() ) {
                projectOverviewDTO = JsonUtils.jsonToObject( ByteUtil.convertByteToString( projectOverviewEntity.getHtmlJson() ),
                        ProjectDTO.class );
            } else {
                projectOverviewDTO = new ProjectDTO();
            }

            updateProjectDTOFromEntity( true, projectOverviewEntity, projectOverviewDTO );
            objectDTO = projectOverviewDTO;
        }

        return objectDTO;
    }

    /**
     * Update project dto from entity.
     *
     * @param setCustomAttributes
     *         the set custom attributes
     * @param projectEntity
     *         the project entity
     * @param projectDTO
     *         the project dto
     */
    private void updateProjectDTOFromEntity( boolean setCustomAttributes, ProjectEntity projectEntity, ProjectDTO projectDTO ) {
        projectDTO.setDescription( projectEntity.getDescription() );
        projectDTO.setName( projectEntity.getName() );
        projectDTO.setId( projectEntity.getComposedId().getId() );
        projectDTO.setVersion( new VersionDTO( projectEntity.getComposedId().getVersionId() ) );
        projectDTO.setCreatedOn( projectEntity.getCreatedOn() );
        projectDTO.setModifiedOn( projectEntity.getModifiedOn() );
        projectDTO.setParentId( null );
        projectDTO.setTypeId( projectEntity.getTypeId() );
        projectDTO.setAutoDeleted( projectEntity.isAutoDelete() );
        if ( null != projectEntity.getCreatedBy() ) {
            projectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( projectEntity.getCreatedBy() ) );
        }
        if ( null != projectEntity.getModifiedBy() ) {
            projectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( projectEntity.getModifiedBy() ) );
        }
        DocumentEntity file = projectEntity.getFile();
        if ( null != file ) {
            projectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
            if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                List< LocationDTO > locationDTOs = getDocumentLocations( file );
                projectDTO.setLocations( locationDTOs );
            }
        }

        projectDTO.setDescription( projectEntity.getDescription() );
        if ( setCustomAttributes ) {
            projectDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( projectEntity.getCustomAttributes() ) );
            if ( null != projectEntity.getAttachments() && !( projectEntity.getAttachments().isEmpty() ) ) {
                DocumentEntity zipFile = projectEntity.getAttachments().iterator().next();
                projectDTO.setZipFile( documentManager.prepareDocumentDTO( zipFile ) );
            }
        }
        if ( projectEntity.getTypeId() != null ) {
            projectDTO.setTypeId( projectEntity.getTypeId() );
            projectDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( projectEntity.getTypeId(), projectEntity.getLifeCycleStatus(),
                            projectEntity.getConfig() ) );
        }
        if ( null != projectEntity.getConfig() ) {
            projectDTO.setType(
                    configManager.getObjectTypeByIdAndConfigName( projectEntity.getTypeId().toString(), projectEntity.getConfig() )
                            .getName() );
        }
        projectDTO.setSize( projectEntity.getSize() != null && projectEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( projectEntity.getSize() ) : ConstantsString.NOT_AVAILABLE );
    }

    /**
     * Creates the library DTO from object entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object createLibraryDTOFromObjectEntity( SuSEntity susEntity ) {
        Object objectDTO = null;

        if ( susEntity instanceof LibraryEntity ) {
            LibraryDTO libraryDTO = new LibraryDTO();
            libraryDTO.setName( susEntity.getName() );
            libraryDTO.setId( susEntity.getComposedId().getId() );
            libraryDTO.setCreatedOn( susEntity.getCreatedOn() );
            libraryDTO.setModifiedOn( susEntity.getModifiedOn() );
            libraryDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            libraryDTO.setParentId( null );
            libraryDTO.setTypeId( susEntity.getTypeId() );
            libraryDTO.setAutoDeleted( susEntity.isAutoDelete() );
            libraryDTO.setDescription( susEntity.getDescription() );
            if ( null != susEntity.getCreatedBy() ) {
                libraryDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            libraryDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            if ( susEntity.getTypeId() != null ) {
                libraryDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                                susEntity.getConfig() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                libraryDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            if ( null != susEntity.getConfig() ) {
                libraryDTO.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
            }

            objectDTO = libraryDTO;
        }

        return objectDTO;
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
     * Validate type and parent existence.
     *
     * @param entityManager
     *         the entity manager
     * @param typeId
     *         the type id
     * @param parentEntity
     *         the parent entity
     *
     * @return the sus object model
     */
    private SuSObjectModel validateTypeInParent( EntityManager entityManager, String typeId, SuSEntity parentEntity ) {

        validateParent( typeId, parentEntity );

        ContainerEntity containerEntity = ( ContainerEntity ) parentEntity;

        SuSObjectModel susObjectModel = null;
        SuSObjectModel parentObjectModel = null;
        ProjectConfiguration projConfig = configManager.getProjectConfiguration( entityManager, parentEntity.getConfig() );

        for ( SuSObjectModel suSObjectModel : projConfig.getEntityConfig() ) {
            if ( suSObjectModel.getId().contentEquals( typeId ) ) {
                susObjectModel = suSObjectModel;
            }
            if ( suSObjectModel.getId().contentEquals( containerEntity.getTypeId().toString() ) ) {
                parentObjectModel = suSObjectModel;
            }
        }

        if ( susObjectModel == null ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_TYPE_IS_NOT_FONUND_IN_CONFIG.getKey(), typeId,
                            containerEntity.getConfig() ) );
        }
        if ( !ProjectEntity.CLASS_ID.toString().contentEquals( typeId ) && !parentObjectModel.getContains()
                .contains( susObjectModel.getId() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_DOESNOT_EXISTS_IN_PARENTZ_CONTAINS.getKey(),
                    susObjectModel.getName(), parentObjectModel.getName() ) );
        }

        return susObjectModel;
    }

    /**
     * Validate parent.
     *
     * @param typeId
     *         the type id
     * @param parentEntity
     *         the parent entity
     */
    private void validateParent( String typeId, SuSEntity parentEntity ) {
        if ( parentEntity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_FOUND.getKey() ) );
        } else if ( parentEntity instanceof ProjectEntity projectEntity && projectEntity.getType()
                .contentEquals( ProjectType.LABEL.getKey() ) && !typeId.equals( ProjectEntity.CLASS_ID.toString() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_TYPE_LABLE_CANT_CONTAINS_DATA.getKey() ) );
        } else if ( !( parentEntity instanceof ContainerEntity ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_OF_TYPE_CONTAINER.getKey() ) );
        }
    }

    /**
     * Validate object type and parent ids.
     *
     * @param parentId
     *         the parent id
     * @param objectTypeId
     *         the object type id
     *
     * @return the notification
     */
    private Notification validateObjectTypeAndParentIds( String parentId, String objectTypeId ) {
        Notification notify = new Notification();

        if ( StringUtils.isBlank( parentId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_CANNOT_BE_EMPTY.getKey() ) ) );

        } else if ( !StringUtils.isBlank( parentId ) && !de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateUUIDString(
                parentId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), parentId ) ) );
        }

        if ( StringUtils.isBlank( objectTypeId ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_ID_CANNOT_EMPTY.getKey() ) ) );

        }
        return notify;
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
     * Checks if is permitted to manage.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedtoManage( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.MANAGE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_MANAGE.getKey(), OBJECT ) );
        }
    }

    /**
     * Permitted to update.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     */
    private void isPermittedtoUpdate( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), OBJECT ) );
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
    private void isPermittedtoDelete( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_DELETE.getKey(), OBJECT ) );
        }
    }

    /**
     * Update meta data document to object.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document DTO
     * @param susEntity
     *         the sus entity
     * @param userId
     *         the user id
     * @param operationType
     *         the operation type
     */
    private void updateMetaDataDocumentToObject( EntityManager entityManager, DocumentDTO documentDTO, SuSEntity susEntity, String userId,
            String operationType ) {
        DocumentEntity documentEntity = documentManager.getDocumentDAO()
                .getLatestNonDeletedObjectById( entityManager, UUID.fromString( documentDTO.getId() ) );
        susEntity.setMetaDataDocument( documentEntity );
        documentEntity.setMetaDataDocument( documentEntity );
        StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( susEntity.getTypeId().toString(), susEntity.getConfig() );
        susEntity.setLifeCycleStatus( dto.getId() );
        susEntity.setVersionId(
                susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, susEntity.getComposedId().getId() ).getVersionId()
                        + ConstantsInteger.INTEGER_VALUE_ONE );
        changeStatusInVersionsRecursively( entityManager, susEntity );
        SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                susEntity.getConfig() );
        if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
            susEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                    "Metadata :" + documentDTO.getName() + ConstantsString.SPACE + operationType, operationType, userId, susEntity,
                    susObjectModel.getName() ) );
        }
        Map< String, Object > customAttributes = susEntity.getCustomAttributes().stream()
                .collect( Collectors.toMap( CustomAttributeEntity::getName, CustomAttributeEntity::getValueAsObject ) );
        susEntity.setCustomAttributes(
                SusDTO.prepareCustomAttributes( susEntity, customAttributes, susObjectModel.getCustomAttributes() ) );
        entityManager.detach( susEntity );
        duplicateTranslation( susEntity );
        susDAO.updateAnObject( entityManager, susEntity );
        selectionManager.sendCustomerEvent( entityManager, userId, susEntity, UPDATE );
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
     * Creates the document for object meta data.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     * @param metaDataMapToPersist
     *         the meta data map to persist
     *
     * @return the document DTO
     */
    private DocumentDTO createDocumentForObjectMetaData( EntityManager entityManager, String userId, String objectId, int versionId,
            MapFilterUtil metaDataMapToPersist ) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId( UUID.randomUUID().toString() );
        if ( PropertiesManager.isEncrypted() ) {
            documentDTO.setEncryptionDecryption( PropertiesManager.getEncryptionDecryptionDTO() );
        }
        documentDTO.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
        documentDTO.setPath( documentManager.getDocumentPathInHex( documentDTO ) );
        try ( InputStream stream = new ByteArrayInputStream( JsonUtils.toJson( metaDataMapToPersist ).getBytes() ) ) {
            documentManager.writeToDiskInVault( ConstantsString.EMPTY_STRING, documentDTO, stream );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        documentDTO.setType( ConstantsString.FILE_TYPE_JSON );
        documentDTO.setUserId( ValidationUtils.validateUUIDString( userId ) ? UUID.fromString( userId ) : null );
        documentDTO.setName( METADATA_FILE_NAME_PREFIX + objectId + versionId + ConstantsFileExtension.JSON );
        return documentManager.saveDocument( entityManager, documentDTO );
    }

    /**
     * Read meta data from previous object version.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the map
     */
    private MapFilterUtil readMetaDataFromCurrentObjectVersion( SuSEntity susEntity ) {
        MapFilterUtil metaDataFromVault = new MapFilterUtil();
        if ( null != susEntity && null != susEntity.getMetaDataDocument() ) {

            String text;
            try ( InputStream stream = documentManager.readVaultFromDisk(
                    documentManager.prepareDocumentDTO( susEntity.getMetaDataDocument() ) ); Reader reader = new InputStreamReader(
                    stream ) ) {
                text = CharStreams.toString( reader );
            } catch ( Exception e ) {
                log.error( "read MetaData From Current Object Version Error: ", e );
                throw new SusException( "read MetaData From Current Object Version Error: " + e.getMessage() );
            }
            metaDataFromVault = JsonUtils.jsonToObject( text, MapFilterUtil.class );
        }
        return metaDataFromVault;
    }

    /**
     * Gets the meta data list.
     *
     * @param susEntity
     *         the sus entity
     * @param filter
     *         the filter
     *
     * @return the meta data list
     */
    private FilteredResponse< MetaDataEntryDTO > getMetaDataList( SuSEntity susEntity, FiltersDTO filter ) {
        List< MetaDataEntryDTO > listToReturn = new ArrayList<>();
        MapFilterUtil metaDataFromVaultFile = readMetaDataFromCurrentObjectVersion( susEntity );

        Map< String, String > filteredMetaData = new HashMap<>();
        // if user search with key or value tag
        if ( filter.getSearch().contains( ConstantsString.EQUALS_OPERATOR ) ) {
            MetaDataEntryDTO searchKeyValue = getSearchKeyValue( filter.getSearch() );
            String key = searchKeyValue.getKey();
            String value = searchKeyValue.getValue();
            // if search contains regex
            if ( ( key != null && key.contains( ConstantsString.ASTERISK ) ) || ( value != null && value.contains(
                    ConstantsString.ASTERISK ) ) ) {
                if ( de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( key )
                        || de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( value ) ) {
                    String regexSearchString =
                            de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( key ) ? key : value;
                    String strPattern = regexSearchString.replace( ConstantsString.ASTERISK, ".*" );
                    Pattern pattern = Pattern.compile( strPattern, Pattern.CASE_INSENSITIVE );

                    for ( Map.Entry< String, String > entry : metaDataFromVaultFile.entrySet() ) {
                        if ( pattern.matcher(
                                de.soco.software.simuspace.suscore.common.util.StringUtils.isNotNullOrEmpty( key ) ? entry.getKey()
                                        : entry.getValue() ).matches() ) {
                            listToReturn.add( new MetaDataEntryDTO( entry.getKey(), entry.getValue() ) );
                        }
                    }
                }
                // if search does not contain regex
            } else {
                for ( Map.Entry< String, String > entry : metaDataFromVaultFile.entrySet() ) {
                    if ( ( entry.getKey() != null && entry.getKey().equals( key ) ) || ( null == key && entry.getValue() != null
                            && entry.getValue().equalsIgnoreCase( value ) ) ) {
                        listToReturn.add( new MetaDataEntryDTO( entry.getKey(), entry.getValue() ) );
                    }
                }
            }
            // if search does not contain key or value tag
        } else {
            filteredMetaData = metaDataFromVaultFile.filterMap( filter );
        }
        for ( Map.Entry< String, String > metaDataKeyValue : filteredMetaData.entrySet() ) {
            listToReturn.add( new MetaDataEntryDTO( metaDataKeyValue.getKey(), metaDataKeyValue.getValue() ) );
        }
        filter.setTotalRecords( ( long ) metaDataFromVaultFile.size() );
        filter.setFilteredRecords( ( long ) listToReturn.size() );
        return PaginationUtil.constructFilteredResponse( filter, listToReturn );
    }

    /**
     * Gets the search key value.
     *
     * @param search
     *         the search
     *
     * @return the search key value
     */
    private MetaDataEntryDTO getSearchKeyValue( String search ) {
        MetaDataEntryDTO metaDataEntryDTOToSet = new MetaDataEntryDTO();
        String[] parts = search.split( ConstantsString.EQUALS_OPERATOR );
        if ( parts.length == 2 ) {
            String key = parts[ 0 ].trim();
            String value = parts[ 1 ].trim();
            if ( key.equals( KEY ) ) {
                metaDataEntryDTOToSet.setKey( value );
            } else if ( key.equals( CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN ) ) {
                metaDataEntryDTOToSet.setValue( value );
            }
        }
        return metaDataEntryDTOToSet;
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

        if ( susEntity != null && susEntity.getTypeId() != null ) {

            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            if ( CollectionUtils.isNotEmpty( susObjectModel.getCustomAttributes() ) ) {
                List< CustomAttributeDTO > updatedList = updateListForBindVisibility( susObjectModel.getCustomAttributes(), susEntity );
                columns.addAll( prepareCustomAttributesTableColumns( updatedList ) );
            }
        }
    }

    /**
     * Update list for bind visibility.
     *
     * @param customAttributes
     *         the custom attributes
     * @param susEntity
     *         the sus entity
     *
     * @return the list
     */
    private List< CustomAttributeDTO > updateListForBindVisibility( List< CustomAttributeDTO > customAttributes, SuSEntity susEntity ) {
        if ( CollectionUtils.isEmpty( susEntity.getCustomAttributes() ) ) {
            return customAttributes;
        }
        customAttributes.forEach( customAttribute -> customAttribute.setValue( ByteUtil.convertByteToObject(
                susEntity.getCustomAttributes().stream()
                        .filter( customAttributeEntity -> customAttributeEntity.getName().equals( customAttribute.getName() ) ).findFirst()
                        .orElse( new CustomAttributeEntity() ).getValue() ) ) );
        return customAttributes.stream().filter(
                customAttribute -> customAttribute.getBindVisibility() == null || customAttribute.getBindVisibility().getValue().equals(
                        customAttributes.stream().filter(
                                        customAttributeDTO -> customAttributeDTO.getName().equals( customAttribute.getBindVisibility().getName() ) )
                                .findFirst().orElse( new CustomAttributeDTO() ).getValue() ) ).collect( Collectors.toList() );
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
     * Translate name.
     *
     * @param user
     *         the user
     * @param entity
     *         the entity
     * @param translationEntities
     *         the translation entities
     */
    private void translateName( UserDTO user, SuSEntity entity, List< TranslationEntity > translationEntities ) {
        if ( PropertiesManager.hasTranslation() && null != user && !user.getId().equals( ConstantsID.SUPER_USER_ID )
                && null != user.getUserDetails() ) {
            String userLang = user.getUserDetails().iterator().next().getLanguage();
            translationEntities.forEach( translation -> {
                if ( userLang.equals( translation.getLanguage() ) && null != translation.getName() && !translation.getName().isEmpty() ) {
                    entity.setName( translation.getName() );
                }
            } );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileInfo getFileInfo( String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getFileInfo( entityManager, userId, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileInfo getFileInfo( EntityManager entityManager, String userId, String objectId ) {
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
            isPermittedtoWrite( entityManager, userIdFromGeneralHeader, objectId );
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
            return createObjectDTOFromObjectEntity( entityManager, susEntity );
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
     * Write to disk in fe temp by location.
     *
     * @param entityManager
     *         the entity manager
     * @param documentDTO
     *         the document dto
     * @param locationEntityList
     *         the location entity list
     */
    private void writeToDiskInFETempByLocation( EntityManager entityManager, DocumentDTO documentDTO,
            List< LocationEntity > locationEntityList ) {
        boolean presentOnDefault = false;
        LocationEntity locationEntity = null;
        for ( LocationEntity location : locationEntityList ) {
            if ( location.getId().toString().equals( LocationsEnum.DEFAULT_LOCATION.getId() ) ) {
                presentOnDefault = true;
                break;
            } else {
                locationEntity = location;
            }
        }
        if ( documentDTO != null ) {
            if ( presentOnDefault ) {
                try {
                    documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
                } catch ( Exception e ) {
                    log.error( "Object not found in vault " + documentDTO.getName(), e );
                    throw new SusException( "Object not found in vault " + documentDTO.getName() );
                }
            } else if ( locationEntity != null ) {
                try ( InputStream ignored = readFileFromLocationIfNotInDefault( documentDTO, locationEntity ) ) {
                    SuSVaultUtils.copyFileFromTmpToVault( documentDTO.getPath() );
                } catch ( IOException e ) {
                    log.error( "Tmp To Vault copying failed", e );
                    throw new SusException( "Tmp To Vault copying failed" );
                }
                documentManager.writeToDiskInFETemp( entityManager, documentDTO, PropertiesManager.getFeStaticPath() );
            }
        }
    }

    /**
     * Creates the DTO from sus entity.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    @Override
    public Object createDTOFromSusEntity( EntityManager entityManager, UUID id, SuSEntity susEntity, boolean setCustomAttributes ) {
        return dataProjectManager.createDTOFromSusEntity( entityManager, id, susEntity, setCustomAttributes );
    }

    /**
     * Prepare and validate selection for dashboard dto.
     *
     * @param entityManager
     *         the entity manager
     * @param dashboardEntity
     *         the dashboard entity
     * @param dashboardDTO
     *         the dashboard dto
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    private void prepareAndValidateSelectionForDashboardDTO( EntityManager entityManager, DataObjectDashboardEntity dashboardEntity,
            DataObjectDashboardDTO dashboardDTO ) {
        if ( dashboardEntity.getPlugin().equals( DashboardPluginEnums.PROJECT.getId() ) ) {
            List< UUID > selectedProjectId = selectionManager.getSelectedIdsListBySelectionId( entityManager,
                    String.valueOf( dashboardEntity.getSelection().getId() ) );
            SuSEntity selectedEntity = null;
            if ( CollectionUtils.isNotEmpty( selectedProjectId ) ) {
                selectedEntity = susDAO.getLatestObjectById( entityManager, SuSEntity.class,
                        selectedProjectId.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );

            }
            if ( dashboardDTO != null && selectedEntity instanceof ProjectEntity projectEntity ) {
                dashboardDTO.setProjectName( projectEntity.getName() );
                dashboardDTO.setProjectId( String.valueOf( projectEntity.getComposedId().getId() ) );
                dashboardDTO.setProjectSelection( dashboardEntity.getSelection().getId().toString() );
            }
        }
    }

    /**
     * Edits the data object form.
     *
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the list
     */
    @Override
    public UIForm editDataObjectForm( String objectId, String userId ) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return editDataObjectForm( entityManager, objectId, userId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Edits the data object form.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param userId
     *         the user id
     *
     * @return the list
     */
    @Override
    public UIForm editDataObjectForm( EntityManager entityManager, String objectId, String userId ) {

        if ( StringUtils.isEmpty( objectId ) || !ValidationUtils.validateUUIDString( objectId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_UUID.getKey(), ProjectDTO.PROJECT_ID ) );
        }

        SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
        if ( entity == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
        }

        if ( BooleanUtils.isTrue( entity.isAutoDelete() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OPERATION_NOT_PERMITTED_ON_AUTODELETE_OBJ.getKey(), objectId ) );
        }

        if ( !isEditable( entity, userId ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey(), objectId ) );
        }

        SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );

        Object filledObject = createDTOFromSusEntity( entityManager, null, entity, true );
        List< UIFormItem > uiFormItemList = GUIUtils.prepareOrderedForm( false, filledObject );
        for ( UIFormItem uiFormItem : uiFormItemList ) {
            if ( uiFormItem.getName().equals( FIELD_NAME_OBJECT_TYPE_ID ) ) {
                uiFormItem.setValue( entity.getTypeId() );
            } else if ( "file".equals( uiFormItem.getName() ) ) {
                uiFormItem.setUpdateField( "name" );
                if ( filledObject instanceof DataObjectHtmlDTO ) {
                    Map< String, Object > rules = new HashMap<>();
                    rules.put( REQUIRED, true );
                    uiFormItem.setRules( rules );
                }
            }
            if ( uiFormItem.getName().equals( DataObjectDashboardDTO.UI_COLUMN_NAME_PLUGIN )
                    && filledObject instanceof DataObjectDashboardDTO ) {
                ( ( SelectFormItem ) uiFormItem ).setOptions( Collections.singletonList(
                        new SelectOptionsUI( uiFormItem.getValue().toString(),
                                DashboardPluginUtil.getDashboardPluginNameFromPluginId( uiFormItem.getValue().toString() ) ) ) );
                ( ( SelectFormItem ) uiFormItem ).setBindFrom(
                        "/data/object/{objectId}/ui/edit/plugin/{__value__}".replace( PARAM_OBJECT_ID, objectId ) );
                uiFormItem.setReadonly( true );
            }
        }
        if ( PropertiesManager.hasTranslation() ) {
            updateTranslationFieldsUI( entity, uiFormItemList );

        }

        log.debug( "getting custom Attribute form configurations" );
        // add custom attributes for this project
        List< UIFormItem > customAttributesItems = convertCustomAttributeJSONtoFormItems( susObjectModel.getCustomAttributes(),
                entity.getCustomAttributes() );

        uiFormItemList.addAll( customAttributesItems );
        return GUIUtils.createFormFromItems( uiFormItemList );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object updateDataObject( String userId, String objectId, String objectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity updateEntity = null;
            if ( objectJson != null ) {
                isPermittedtoUpdate( entityManager, userId, objectId );
                SuSEntity dataEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );

                SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( dataEntity.getTypeId().toString(),
                        dataEntity.getConfig() );

                List< SuSEntity > parents = susDAO.getParents( entityManager, dataEntity );
                Object objectDTO = JsonUtils.jsonToObject( objectJson, initializeObject( suSObjectModel.getClassName() ).getClass() );
                updateEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( objectDTO, userId );
                validateImageAndVideoFile( objectJson, suSObjectModel );

                String objectName = ( String ) ReflectionUtils.invokeMethod( METHOD_NAME_GET_NAME, objectDTO );
                updateEntity.setName( objectName );
                String description = ( String ) ReflectionUtils.invokeMethod( METHOD_DESCRIPTION_GET_NAME, objectDTO );
                updateEntity.setDescription( description );
                if ( CollectionUtils.isNotEmpty( parents ) && !isNameUniqueAmongSiblings( entityManager, objectName, dataEntity,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                                parents.get( LIST_FIRST_INDEX ).getComposedId().getId() ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
                }
                ReflectionUtils.invokeMethodWithListParam( OBJECT_METHOD_SET_CUSTOM_ATTRIBUTES_DTO, objectDTO,
                        suSObjectModel.getCustomAttributes() );
                UserDTO user = userCommonManager.getUserById( entityManager, UUID.fromString( userId ) );
                updateEntity.setModifiedBy( prepareUserEntityFromUserModel( user ) );
                if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
                    AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, dataEntity, updateEntity,
                            dataEntity.getComposedId().getId().toString(), dataEntity.getName(), suSObjectModel.getName() );
                    if ( null != auditLog ) {
                        auditLog.setObjectId( updateEntity.getComposedId().getId().toString() );
                    }
                    updateEntity.setAuditLogEntity( auditLog );
                }
                updateEntity.setIcon( suSObjectModel.getIcon() );
                updateEntity.setTranslation( setMultiNames( entityManager, objectJson ) );
                try {
                    updateEntity.setSelectedTranslations(
                            JsonUtils.toJson( new ObjectMapper().readTree( objectJson ).path( SELECTED_TRANSLATIONS ) ) );
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage() );
                }
                prepareCommonAttributesForUpdateEntity( dataEntity, updateEntity );
                if ( updateEntity instanceof DataObjectDashboardEntity dashboardEntity ) {
                    DataObjectDashboardEntity existingEntity = ( DataObjectDashboardEntity ) dataEntity;
                    dashboardEntity.setPlugin( existingEntity.getPlugin() );
                    dashboardEntity.setSettings( existingEntity.getSettings() );
                    prepareAndValidateSelectionForDashboardDTO( entityManager, dashboardEntity, ( DataObjectDashboardDTO ) objectDTO );
                }
                updateDataSourcesAndWidgetsForDataDashboard( updateEntity, dataEntity );
                updatePlotOfDataObjectTraceEntity( dataEntity, updateEntity );
                submitCreatePreviewJob( entityManager, userId, updateEntity, objectDTO );
                changeStatusInVersionsRecursively( entityManager, updateEntity );

                updateEntity.setCustomAttributes(
                        prepareCustomAttributesSetFromPreviousAndNewAtributes( dataEntity.getCustomAttributes(), objectJson ) );
                updateEntity = susDAO.updateAnObject( entityManager, updateEntity );
                final SuSEntity finalEntity = updateEntity;
                new Thread( () -> {
                    EntityManager threadEntityManager = entityManagerFactory.createEntityManager();
                    saveOrUpdateIndexEntity( threadEntityManager, suSObjectModel, finalEntity );
                    threadEntityManager.close();
                } ).start();
                selectionManager.sendCustomerEvent( entityManager, userId, dataEntity, UPDATE );
            }
            return createDTOFromSusEntity( entityManager, null, updateEntity, true );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Update data sources and widgets for data dashboard.
     *
     * @param updateEntity
     *         the update entity
     * @param dataEntity
     *         the data entity
     */
    private void updateDataSourcesAndWidgetsForDataDashboard( SuSEntity updateEntity, SuSEntity dataEntity ) {
        if ( updateEntity instanceof DataDashboardEntity newEntity && dataEntity instanceof DataDashboardEntity existingEntity ) {
            newEntity.setDataSources(
                    dataDashboardManager.createCopiesOfDataSourcesForNewVersion( existingEntity.getDataSources(), newEntity,
                            existingEntity.getVersionId() + 1 ) );
            newEntity.setWidgets( dataDashboardManager.createCopiesOfWidgetsForNewVersion( existingEntity.getWidgets(), newEntity,
                    existingEntity.getVersionId() + 1 ) );
        }
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
     * Prepare custom attributes set from previous and new attributes.
     *
     * @param customAttributes
     *         the custom attributes
     * @param objJson
     *         the obj json
     *
     * @return the sets the
     */
    public static Set< CustomAttributeEntity > prepareCustomAttributesSetFromPreviousAndNewAtributes(
            Set< CustomAttributeEntity > customAttributes, String objJson ) {

        Set< CustomAttributeEntity > setCustomAttributes = new HashSet<>();
        JSONObject jsonObject = new JSONObject( objJson );
        if ( jsonObject.has( "customAttributes" ) ) {
            JSONObject objectMain = ( JSONObject ) jsonObject.get( "customAttributes" );

            Iterator< String > keys = objectMain.keys();

            while ( keys.hasNext() ) {
                String key = keys.next();

                CustomAttributeEntity savedAttribute = getcustomAttributesEntityByKey( customAttributes, key );
                if ( savedAttribute == null ) {
                    CustomAttributeEntity attributeEntity = new CustomAttributeEntity();
                    attributeEntity.setId( UUID.randomUUID() );
                    attributeEntity.setName( key );
                    attributeEntity.setType( "" );
                    attributeEntity.setOptions( null );
                    attributeEntity.setValue( ByteUtil.convertStringToByte( jsonObject.get( key ).toString() ) );
                    setCustomAttributes.add( attributeEntity );
                } else {
                    savedAttribute.setValue( ByteUtil.convertStringToByte( objectMain.get( key ).toString() ) );
                    setCustomAttributes.add( savedAttribute );
                }
            }
        }
        return setCustomAttributes;
    }

    /**
     * Gets the custom attributes entity by key.
     *
     * @param customAttributes
     *         the custom attributes
     * @param keyName
     *         the key name
     *
     * @return the custom attributes entity by key
     */
    private static CustomAttributeEntity getcustomAttributesEntityByKey( Set< CustomAttributeEntity > customAttributes, String keyName ) {

        for ( CustomAttributeEntity customAttributeEntity : customAttributes ) {
            if ( customAttributeEntity.getName().equalsIgnoreCase( keyName ) ) {
                return customAttributeEntity;
            }
        }
        return null;

    }

    /**
     * Update plot of data object trace entity.
     *
     * @param dataEntity
     *         the data entity
     * @param updateEntity
     *         the update entity
     */
    private void updatePlotOfDataObjectTraceEntity( SuSEntity dataEntity, SuSEntity updateEntity ) {
        if ( updateEntity instanceof DataObjectTraceEntity updateTraceEntity && dataEntity instanceof DataObjectTraceEntity traceEntity ) {
            if ( traceEntity.getFile() == null ) {
                traceEntity.setPlot( null );
            } else if ( updateTraceEntity.getPlot() != null ) {
                traceEntity.setPlot( updateTraceEntity.getPlot() );
            }
        }
    }

    /**
     * Gets the data object movie thumb nail.
     *
     * @param documentEntity
     *         the document entity
     *
     * @return the data object movie thumb nail
     */
    private DocumentDTO getDataObjectMovieThumbNail( DocumentEntity documentEntity ) {
        DocumentDTO documentDTO = null;
        if ( null != documentEntity ) {
            documentDTO = documentManager.prepareDocumentDTO( documentEntity );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + FilenameUtils.removeExtension( documentDTO.getName() )
                    + ConstantsString.OBJECT_THUMB_NAIL_FILE_POSTFIX + FilenameUtils.EXTENSION_SEPARATOR + ConstantsFileExtension.PNG;
            documentDTO.setUrl( url );
        }
        return documentDTO;
    }

    /**
     * Gets the data object image thumb nail.
     *
     * @param documentEntity
     *         the document entity
     *
     * @return the data object image thumb nail
     */
    private DocumentDTO getDataObjectImageThumbNail( DocumentEntity documentEntity ) {
        DocumentDTO documentDTO = null;
        if ( null != documentEntity ) {
            documentDTO = documentManager.prepareDocumentDTO( documentEntity );
            String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + documentDTO.getPath() + File.separator + documentDTO.getName();
            documentDTO.setUrl( url );
        }
        return documentDTO;
    }

    /**
     * Validate for duplicate entry in meta data.
     *
     * @param objectMetaDataDTO
     *         the object meta data DTO
     * @param metaDataFromVaultToPersist
     *         the meta data from vault to persist
     */
    private void validateForDuplicateEntryInMetaData( ObjectMetaDataDTO objectMetaDataDTO, MapFilterUtil metaDataFromVaultToPersist ) {
        List< String > duplicateKeyList = new ArrayList<>();
        for ( MetaDataEntryDTO metaDataEntryDTO : objectMetaDataDTO.getMetadata() ) {
            for ( String key : metaDataFromVaultToPersist.keySet() ) {
                if ( metaDataEntryDTO.getKey().equalsIgnoreCase( key ) ) {
                    duplicateKeyList.add( metaDataEntryDTO.getKey() );
                }
            }
        }
        if ( !duplicateKeyList.isEmpty() ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.DUPLICATE_KEYS_FOUND_IN_METADATA.getKey(), duplicateKeyList.toString() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectView( String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, UUID.fromString( viewId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare object view DTO.
     *
     * @param config
     *         the config
     * @param viewJson
     *         the view json
     * @param viewKey
     *         the view key
     * @param objectId
     *         the object id
     * @param isUpdateable
     *         the is updateable
     *
     * @return the object view DTO
     */
    @Override
    public ObjectViewDTO prepareObjectViewDTO( String config, String viewJson, String viewKey, String objectId, boolean isUpdateable ) {
        return dataProjectManager.prepareObjectViewDTO( config, viewJson, viewKey, objectId, isUpdateable );
    }

    /**
     * Gets the object view list.
     *
     * @param susEntity
     *         the sus entity
     * @param userId
     *         the user id
     * @param typeId
     *         the type id
     *
     * @return the object view list
     */
    @Override
    public List< ObjectViewDTO > getObjectViewList( EntityManager entityManager, SuSEntity susEntity, String userId, String typeId ) {
        return dataProjectManager.getObjectViewList( entityManager, susEntity, userId, typeId );
    }

    /**
     * Gets the object view key.
     *
     * @param susObjectModel
     *         the config
     * @param typeId
     *         the type id
     *
     * @return the object view key
     */
    @Override
    public String getObjectViewKey( SuSObjectModel susObjectModel, String typeId ) {
        return dataProjectManager.getObjectViewKey( susObjectModel, typeId );
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
    public List< SelectOptionsUI > getCurveXUnits( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getCurveXUnits( entityManager, objectId );
        } finally {
            entityManager.close();
        }

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
    public List< SelectOptionsUI > getCurveYUnits( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getCurveYUnits( entityManager, objectId );
        } finally {
            entityManager.close();
        }
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
     * Gets the data object value units.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object value units
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValueUnits(java.lang.String)
     */
    @Override
    public List< SelectOptionsUI > getDataObjectValueUnits( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectValueDTO dataObjectValue = getDataObjectValue( entityManager, UUID.fromString( objectId ), null );
            if ( null != dataObjectValue ) {
                List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                    if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectValue.getDimension().toLowerCase() ) ) {
                        for ( UnitsList units : unitsFamily.getUnits() ) {
                            listToReturn.add( new SelectOptionsUI( units.getLabel(), units.getName().toLowerCase() ) );
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
     * Gets the data object value units by version.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object value units by version
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValueUnitsByVersion(java.lang.String, int)
     */
    @Override
    public List< SelectOptionsUI > getDataObjectValueUnitsByVersion( String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectValueDTO dataObjectValue = getDataObjectValueByVersion( entityManager, UUID.fromString( objectId ), null, versionId );
            if ( null != dataObjectValue ) {
                List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                    if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectValue.getDimension().toLowerCase() ) ) {
                        for ( UnitsList units : unitsFamily.getUnits() ) {
                            listToReturn.add( new SelectOptionsUI( units.getName().toLowerCase(), units.getName().toLowerCase() ) );
                        }
                        break;
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return listToReturn;
    }

    /**
     * Gets the data object value.
     *
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the value unit DTO
     *
     * @return the data object value
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValue(java.util.UUID,
     * de.soco.software.simuspace.suscore.common.model.ValueUnitDTO)
     */
    @Override
    public DataObjectValueDTO getDataObjectValue( UUID objectId, ValueUnitDTO valueUnitDTO ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObjectValue( entityManager, objectId, valueUnitDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValue(java.util.UUID,
     * de.soco.software.simuspace.suscore.common.model.ValueUnitDTO)
     */
    @Override
    public DataObjectValueDTO getDataObjectValue( EntityManager entityManager, UUID objectId, ValueUnitDTO valueUnitDTO )
            throws IOException {
        DataObjectValueDTO dataObjectValue = new DataObjectValueDTO();
        DataObjectEntity dataObjectValueEntity = ( DataObjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                DataObjectValueEntity.class, objectId );
        return getDataObjectValueBySusEntity( entityManager, valueUnitDTO, dataObjectValue, dataObjectValueEntity );
    }

    /**
     * Gets the data object value by version.
     *
     * @param objectId
     *         the object id
     * @param valueUnitDTO
     *         the value unit DTO
     * @param versionId
     *         the version id
     *
     * @return the data object value by version
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValueByVersion(java.util.UUID,
     * de.soco.software.simuspace.suscore.common.model.ValueUnitDTO, int)
     */
    @Override
    public DataObjectValueDTO getDataObjectValueByVersion( UUID objectId, ValueUnitDTO valueUnitDTO, int versionId ) throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataObjectValueByVersion( entityManager, objectId, valueUnitDTO, versionId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getDataObjectValueByVersion(java.util.UUID,
     * de.soco.software.simuspace.suscore.common.model.ValueUnitDTO, int)
     */
    @Override
    public DataObjectValueDTO getDataObjectValueByVersion( EntityManager entityManager, UUID objectId, ValueUnitDTO valueUnitDTO,
            int versionId ) throws IOException {
        DataObjectValueDTO dataObjectValue = new DataObjectValueDTO();
        DataObjectEntity dataObjectValueEntity = ( DataObjectEntity ) susDAO.getObjectByIdAndVersion( entityManager,
                DataObjectValueEntity.class, objectId, versionId );
        return getDataObjectValueBySusEntity( entityManager, valueUnitDTO, dataObjectValue, dataObjectValueEntity );
    }

    /**
     * Gets the data object value by sus entity.
     *
     * @param entityManager
     *         the entity manager
     * @param valueUnitDTO
     *         the value unit DTO
     * @param dataObjectValue
     *         the data object value
     * @param dataObjectValueEntity
     *         the data object value entity
     *
     * @return the data object value by sus entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private DataObjectValueDTO getDataObjectValueBySusEntity( EntityManager entityManager, ValueUnitDTO valueUnitDTO,
            DataObjectValueDTO dataObjectValue, DataObjectEntity dataObjectValueEntity ) throws IOException {
        InputStream fileStream;

        if ( null != dataObjectValueEntity && dataObjectValueEntity.getFile() != null ) {
            DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectValueEntity.getFile().getId() );
            if ( documentDTO != null ) {

                LocationEntity locationEntity = dataObjectValueEntity.getFile().getLocations().get( 0 );
                if ( locationEntity.getName().equals( DEFAULT ) ) {
                    try {
                        fileStream = documentManager.readVaultFromDisk( documentDTO );
                    } catch ( Exception e ) {
                        log.error( "Object not found in vault " + documentDTO.getName(), e );
                        throw new SusException( "Object not found in vault " + documentDTO.getName() );
                    }
                } else {
                    if ( BooleanUtils.isTrue( dataObjectValueEntity.isAutoDelete() ) ) {
                        return dataObjectValue;
                    }
                    fileStream = readFileFromLocationIfNotInDefault( documentDTO, locationEntity );
                    if ( null == fileStream ) {
                        return dataObjectValue;
                    }
                }
                try {
                    dataObjectValue = JsonUtils.jsonToObject( fileStream, DataObjectValueDTO.class );
                    if ( valueUnitDTO != null && StringUtils.isNotBlank( dataObjectValue.getValue() ) && StringUtils.isNotBlank(
                            dataObjectValue.getDimension() ) ) {
                        String convertToUnit =
                                ( StringUtils.isBlank( valueUnitDTO.getUnit() ) ) ? dataObjectValue.getUnit() : valueUnitDTO.getUnit();

                        double value = SIBaseUnitConverter.convert( dataObjectValue.getDimension().toLowerCase(),
                                dataObjectValue.getUnit().toLowerCase(), convertToUnit.toLowerCase(),
                                Double.parseDouble( dataObjectValue.getValue() ) );
                        dataObjectValue.setValue( Double.toString( value ) );
                        dataObjectValue.setUnit( convertToUnit );
                    }
                } catch ( Exception e ) {
                    log.error( e.getMessage(), e );
                } finally {
                    if ( fileStream != null ) {
                        fileStream.close();
                    }
                }
            }
        }
        return dataObjectValue;
    }

    @Override
    public Map< String, String > getCeetron3DObjectModel( String userId, UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = getDataObjectEntity( entityManager, userId, objectId );
            String outputFile = susEntity.getName() + susEntity.getComposedId().getId();
            Map< String, String > map = new HashMap<>();
            map.put( "modelKey", outputFile );
            map.put( "cugServerUrl", PropertiesManager.getCeetronServerUrl().asText() );
            return map;
        } finally {
            entityManager.close();
        }
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
     * Populate generic DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param suSEntities
     *         the su S entities
     * @param objectDTOList
     *         the object DTO list
     */
    private void populateGenericDTO( EntityManager entityManager, String userId, UUID parentId, List< SuSEntity > suSEntities,
            List< Object > objectDTOList ) {
        if ( CollectionUtil.isNotEmpty( suSEntities ) ) {
            List< VersionPrimaryKey > listOfIds = suSEntities.stream().map( SuSEntity::getComposedId ).toList();
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager, listOfIds );
            for ( SuSEntity suSEntity : suSEntities ) {
                Object object = createGenericDTOFromObjectEntity( entityManager, userId, parentId, suSEntity, translationEntityList.stream()
                        .filter( translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( suSEntity.getComposedId() ) )
                        .toList() );
                objectDTOList.add( object );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObjectTraceByObjectId( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager,
                    DataObjectEntity.class, UUID.fromString( id ) );

            return readCsvFromTraceObject( entityManager, dataObjectEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Read csv from trace object.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectEntity
     *         the data Object Entity
     *
     * @return the list
     */
    private List< Map< String, Object > > readCsvFromTraceObject( EntityManager entityManager, DataObjectEntity dataObjectEntity ) {
        if ( null != dataObjectEntity ) {
            SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                    dataObjectEntity.getConfig() );
            if ( susModel.getClassName().equalsIgnoreCase( DataObjectTraceDTO.class.getName() ) && dataObjectEntity.getFile() != null ) {
                DocumentDTO documentDTO = documentManager.getDocumentById( entityManager, dataObjectEntity.getFile().getId() );
                if ( documentDTO != null ) {
                    InputStream inputStream2 = documentManager.readVaultFromDisk( documentDTO );
                    return CsvUtils.generateCsv( inputStream2 );
                }
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getHtmlObjectByObjectId( String userId, String userUid, String token, String id, String language ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedtoRead( entityManager, userId, id );
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( id ) );
            if ( susEntity instanceof DataObjectHtmlsEntity ) {
                return readHtmlJsonFromHtmlObject( susEntity );
            } else if ( susEntity instanceof ProjectEntity projectEntity ) {
                return readProjectOverviewObject( entityManager, projectEntity, token, language );
            }
            return new DataObjectHtmlDTO();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Read project overview object.
     *
     * @param entityManager
     *         the entity manager
     * @param projectEntity
     *         the project entity
     * @param token
     *         the token
     *
     * @return the project DTO
     */
    private ProjectDTO readProjectOverviewObject( EntityManager entityManager, ProjectEntity projectEntity, String token,
            String language ) {
        try {
            SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( projectEntity.getTypeId().toString(),
                    projectEntity.getConfig() );

            if ( suSObjectModel.getViewConfig().stream().noneMatch( viewConf -> "html".equals( viewConf.getKey() ) ) ) {
                return new ProjectDTO();
            }

            if ( StringUtils.isBlank( suSObjectModel.getOverviewPlugin() ) ) {
                throw new SusException( "no overview plugin defined for " + suSObjectModel.getName() + " in " + projectEntity.getConfig() );
            }
            String parsedLanguage = OverviewPluginUtil.parseLanguageQueryParam( token, language, suSObjectModel.getOverviewPlugin() );

            var status = lifeCycleManager.getStatusByLifeCycleNameAndStatusId( suSObjectModel.getLifeCycle(),
                    projectEntity.getLifeCycleStatus() );
            var statusConfig = lifeCycleManager.getLifeCycleStatusByStatusId( status.getId() );
            if ( statusConfig.isUpdateOverview() ) {
                // lifecycle status dictates that new overview should be generated. Call the overview generation script if necessary
                if ( OverviewPluginUtil.checkIfGenerationIsInProgress( projectEntity.getComposedId().getId(), parsedLanguage ) ) {
                    // if overview generation for the node is already in progress, wait for it, and return the result
                    log.info( "overview file generation already in progress for " + "projectEntity.getComposedId().getId().toString()"
                            + ". waiting..." );
                    ProjectDTO projectDTO = OverviewPluginUtil.getRunningThread( projectEntity.getComposedId().getId(), parsedLanguage )
                            .get();
                    log.info( "wait released for overview file generation for " + projectEntity.getComposedId().getId() );
                    return projectDTO;
                } else {
                    // normal flow, i.e Call the overview generation script
                    log.info( "starting overview file generation for " + projectEntity.getComposedId().getId().toString() );
                    Callable< ProjectDTO > callableTask = () -> generateProjectOverview( entityManager, projectEntity, token,
                            suSObjectModel, parsedLanguage );
                    Future< ProjectDTO > future = OverviewPluginUtil.submitOverviewGenerationFuture( projectEntity.getComposedId().getId(),
                            parsedLanguage, callableTask );
                    ProjectDTO projectDTO = future.get();
                    log.info( "overview file generation complete for " + projectEntity.getComposedId().getId() );
                    return projectDTO;
                }
            } else {
                // lifecycle status dictates that new overview should not be generated, use last generated files
                return getExistingOverview( projectEntity, parsedLanguage );
            }
        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
            Thread.currentThread().interrupt();
        } catch ( ExecutionException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_IN_GENERATING_PROJECT_OVERVIEW.getKey() ) );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
        return new ProjectDTO();
    }

    /**
     * Gets existing overview.
     *
     * @param projectEntity
     *         the project entity
     * @param language
     *         the language
     *
     * @return the existing overview
     */
    private ProjectDTO getExistingOverview( ProjectEntity projectEntity, String language ) {
        if ( projectEntity.getHtmlJson() != null ) {
            ProjectDTO projectDTO = ( ProjectDTO ) createProjectDTOFromProjectEntityWithOverview( projectEntity );
            updateDocumentFromTranslationInsideProjectDTO( projectDTO, projectEntity, language );
            String urlFe = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + projectDTO.getFile().getPath() + File.separator;
            projectDTO.setBaseurl( urlFe );
            if ( StringUtils.isNotEmpty( projectDTO.getHtml_index() ) ) {
                projectDTO.setHtml_index( urlFe + projectDTO.getHtml_index() );
            }
            if ( StringUtils.isNotEmpty( projectDTO.getJs_index() ) ) {
                projectDTO.setJs_index( urlFe + projectDTO.getJs_index() );
            }
            return projectDTO;
        } else {
            return new ProjectDTO();
        }
    }

    /**
     * Generate project overview project dto.
     *
     * @param entityManager
     *         the entity manager
     * @param projectEntity
     *         the project entity
     * @param token
     *         the token
     * @param suSObjectModel
     *         the su s object model
     * @param language
     *         the language
     *
     * @return the project dto
     *
     * @throws IOException
     *         the io exception
     */
    private ProjectDTO generateProjectOverview( EntityManager entityManager, ProjectEntity projectEntity, String token,
            SuSObjectModel suSObjectModel, String language ) throws IOException {
        ProjectDTO projectDTO = updateOverViewFilesInEntity( entityManager, projectEntity, suSObjectModel, token, language );
        updateDTOFromNewFile( projectDTO, projectEntity );
        updateProjectDTOFromEntity( false, projectEntity, projectDTO );
        updateProjectOverviewAttachmentsFromZipFile( projectDTO );
        susDAO.saveOrUpdateObject( entityManager, projectEntity );
        return projectDTO;
    }

    /**
     * Update document from translation inside project DTO.
     *
     * @param projectDTO
     *         the project DTO
     * @param projectEntity
     *         the project entity
     */
    private void updateDocumentFromTranslationInsideProjectDTO( ProjectDTO projectDTO, ProjectEntity projectEntity, String language ) {
        if ( !PropertiesManager.hasTranslation() ) {
            return;
        }
        TranslationEntity translation = OverviewPluginUtil.getRelevantTranslationEntityForOverview( projectEntity, language );
        if ( translation == null || translation.getFile() == null ) {
            return;
        }
        projectDTO.setFile( documentManager.prepareDocumentDTO( translation.getFile() ) );
        // to update paths of translated document inside response
        updateDTOFromNewFile( projectDTO, projectEntity );
    }

    /**
     * Update DTO from new file.
     *
     * @param projectDTO
     *         the project DTO
     * @param projectEntity
     *         the project entity
     */
    private void updateDTOFromNewFile( ProjectDTO projectDTO, ProjectEntity projectEntity ) {
        if ( projectDTO.getFile() != null ) {
            File jsonPath = new File( PropertiesManager.getVaultPath() + projectDTO.getFile().getPath() );
            ProjectDTO dtoFromFile = OverviewPluginUtil.readOverviewJsonFromFile( jsonPath, projectDTO.getFile() );
            projectDTO.setAttachments( dtoFromFile.getAttachments() );
            projectDTO.setHtml_index( dtoFromFile.getHtml_index() );
            projectDTO.setJs_index( dtoFromFile.getJs_index() );
            projectDTO.setHtml( dtoFromFile.getHtml() );
            projectDTO.setJs( dtoFromFile.getJs() );
            projectDTO.setBaseurl(
                    CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                            + File.separator + projectDTO.getFile().getPath() + File.separator );
            projectEntity.setHtmlJson( ByteUtil.convertStringToByte( JsonUtils.toJson( dtoFromFile ) ) );
        }
    }

    /**
     * Update over view files in entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectEntity
     *         the project entity
     * @param suSObjectModel
     *         the su S object model
     * @param token
     *         the token
     * @param language
     *         the language
     *
     * @return the project DTO
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private ProjectDTO updateOverViewFilesInEntity( EntityManager entityManager, ProjectEntity projectEntity, SuSObjectModel suSObjectModel,
            String token, String language ) throws IOException {
        String basePath =
                PropertiesManager.getDefaultServerTempPath() + File.separator + suSObjectModel.getOverviewPlugin() + File.separator
                        + projectEntity.getComposedId().getId() + ConstantsString.UNDERSCORE + language;
        try {
            ProjectDTO projectDTO = new ProjectDTO();
            String parentsModelName = getParentsModelNameFromSusEntity( entityManager, projectEntity );

            String expectedFileName = parentsModelName + ConstantsString.HASH + suSObjectModel.getName();
            String inputFilePath = prepareOverviewScriptPayload( entityManager, projectEntity, basePath, expectedFileName, token,
                    language );
            OverviewPluginUtil.generateProjectOverviewFromPython( inputFilePath );
            String address = PropertiesManager.getLocationURL() + File.separator + API_DOCUMENT_UPLOAD;
            var headers = SuSClient.prepareDownloadHeaders( token );
            TranslationEntity translationEntity = OverviewPluginUtil.getRelevantTranslationEntityForOverview( projectEntity, language );
            File jsonFile = new File( basePath + File.separator + expectedFileName + ConstantsFileExtension.JSON );
            uploadJsonFileInProjectEntity( entityManager, projectEntity, projectDTO, jsonFile, address, headers, translationEntity );
            File zipFile = new File( basePath + File.separator + expectedFileName + ConstantsFileExtension.ZIP );
            uploadZipFileInProjectEntity( entityManager, projectEntity, projectDTO, zipFile, address, headers, translationEntity );
            if ( projectEntity.getTranslation() == null ) {
                Set< TranslationEntity > translations = new HashSet<>();
                projectEntity.setTranslation( translations );
            }
            if ( projectEntity.getTranslation().stream()
                    .noneMatch( translation -> translation.getId().equals( translationEntity.getId() ) ) ) {
                projectEntity.getTranslation().add( translationEntity );
            }
            return projectDTO;
        } finally {
            FileUtils.deleteFiles( Path.of( basePath ) );
        }
    }

    /**
     * Upload zip file in project entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectEntity
     *         the project entity
     * @param projectDTO
     *         the project DTO
     * @param zipFile
     *         the zip file
     * @param address
     *         the address
     * @param headers
     *         the headers
     * @param translationEntity
     *         the translation entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void uploadZipFileInProjectEntity( EntityManager entityManager, ProjectEntity projectEntity, ProjectDTO projectDTO,
            File zipFile, String address, Map< String, String > headers, TranslationEntity translationEntity ) throws IOException {
        if ( Files.exists( zipFile.toPath() ) ) {
            DocumentDTO zipDTO = SuSClient.uploadFileRequest( address, zipFile, headers );
            projectDTO.setZipFile( zipDTO );
            DocumentEntity zipEntity = documentManager.getDocumentEntityById( entityManager, UUID.fromString( zipDTO.getId() ) );
            Set< DocumentEntity > zipSet;
            zipSet = prepareZipDocumentEntity( zipDTO, zipEntity );
            projectEntity.setAttachments( zipSet );
            translationEntity.setAttachments( zipSet );
            Files.delete( zipFile.toPath() );

        }
    }

    /**
     * Upload json file in project entity.
     *
     * @param entityManager
     *         the entity manager
     * @param projectEntity
     *         the project entity
     * @param projectDTO
     *         the project DTO
     * @param jsonFile
     *         the json file
     * @param address
     *         the address
     * @param headers
     *         the headers
     * @param translationEntity
     *         the translation entity
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    private void uploadJsonFileInProjectEntity( EntityManager entityManager, ProjectEntity projectEntity, ProjectDTO projectDTO,
            File jsonFile, String address, Map< String, String > headers, TranslationEntity translationEntity ) throws IOException {
        if ( Files.exists( jsonFile.toPath() ) ) {
            DocumentDTO jsonDTO = SuSClient.uploadFileRequest( address, jsonFile, headers );
            projectDTO.setFile( jsonDTO );
            DocumentEntity jsonEntity = documentManager.getDocumentEntityById( entityManager, UUID.fromString( jsonDTO.getId() ) );
            projectEntity.setFile( jsonEntity );
            translationEntity.setFile( jsonEntity );
            Files.delete( jsonFile.toPath() );
        }
    }

    /**
     * Update project overview attachments from zip file.
     *
     * @param projectDTO
     *         the project DTO
     */
    private void updateProjectOverviewAttachmentsFromZipFile( ProjectDTO projectDTO ) {
        try {
            if ( projectDTO == null ) {
                projectDTO = new ProjectDTO();
            }
            if ( projectDTO.getFile() == null || projectDTO.getZipFile() == null || projectDTO.getAttachments() == null ) {
                return;
            }
            String urlFe = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                    + projectDTO.getFile().getPath() + File.separator;
            projectDTO.setBaseurl( urlFe );
            if ( StringUtils.isNotEmpty( projectDTO.getHtml_index() ) ) {
                projectDTO.setHtml_index( urlFe + projectDTO.getHtml_index() );
            }
            if ( StringUtils.isNotEmpty( projectDTO.getJs_index() ) ) {
                projectDTO.setJs_index( urlFe + projectDTO.getJs_index() );
            }

            String zipSourcePath = PropertiesManager.getVaultPath() + projectDTO.getZipFile().getPath();
            String zipDestPath = PropertiesManager.getFeStaticPath() + projectDTO.getFile().getPath();
            File zipSourceFile = new File( zipSourcePath );
            FileUtils.setGlobalExecuteFilePermissions( zipSourceFile.toPath() );
            FileUtils.extractZipFile( zipSourceFile.getAbsolutePath(), zipDestPath );
            if ( projectDTO.getAttachments() != null ) {
                List< Map< String, String > > updateAttachmentList = new ArrayList<>();
                for ( Map< String, String > attFiles : projectDTO.getAttachments() ) {
                    // simple attachments for now.
                    // support for movies and Ceetron maybe needed in future
                    Map< String, String > updateAttachment = new HashMap<>();
                    updateAttachment.put( "name", attFiles.get( "name" ) );
                    updateAttachment.put( "type", attFiles.get( "type" ) );
                    updateAttachment.put( "url", urlFe + attFiles.get( "name" ).replace( "\\", "/" ) );
                    updateAttachmentList.add( updateAttachment );
                }
                projectDTO.setAttachments( updateAttachmentList );
            }

        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( "Extraction Failed for " + projectDTO.getZipFile().getName() + " in " + projectDTO.getName() );
        }
    }

    /**
     * Read html json from html object.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the data object html DTO
     */
    private DataObjectHtmlDTO readHtmlJsonFromHtmlObject( SuSEntity susEntity ) {
        if ( susEntity instanceof DataObjectHtmlsEntity htmlEntity ) {
            if ( htmlEntity.getHtmlJson() != null ) {
                DataObjectHtmlDTO dataObjectHtmlDTO = JsonUtils.jsonToObject( ByteUtil.convertByteToString( htmlEntity.getHtmlJson() ),
                        DataObjectHtmlDTO.class );
                resolveUrlsInHtmlObject( dataObjectHtmlDTO );
                return dataObjectHtmlDTO;
            } else {
                return new DataObjectHtmlDTO();
            }
        }
        return null;
    }

    /**
     * Resolve urls in html object.
     *
     * @param dataObjectHtmlDTO
     *         the data object html dto
     */
    private void resolveUrlsInHtmlObject( DataObjectHtmlDTO dataObjectHtmlDTO ) {
        dataObjectHtmlDTO.getAttachments().forEach( map -> map.computeIfPresent( "url", ( key, value ) -> replaceWebBaseUrl( value ) ) );
        if ( dataObjectHtmlDTO.getHtml_index() != null ) {
            dataObjectHtmlDTO.setHtml_index( replaceWebBaseUrl( dataObjectHtmlDTO.getHtml_index() ) );
        }
        if ( dataObjectHtmlDTO.getJs_index() != null ) {
            dataObjectHtmlDTO.setJs_index( replaceWebBaseUrl( dataObjectHtmlDTO.getJs_index() ) );
        }
        if ( dataObjectHtmlDTO.getBaseurl() != null ) {
            dataObjectHtmlDTO.setBaseurl( replaceWebBaseUrl( dataObjectHtmlDTO.getBaseurl() ) );
        }
    }

    /**
     * Replace web base url string.
     *
     * @param value
     *         the value
     *
     * @return the string
     */
    private String replaceWebBaseUrl( String value ) {
        try {
            URL url = new URL( value );
            url.toURI();
            String fullUrl = url.getProtocol() + "://" + url.getHost();
            return value.replace( fullUrl,
                    CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH );
        } catch ( MalformedURLException | URISyntaxException e ) {
            return CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH + value;
        }
    }

    /**
     * Gets the parents model name from sus entity.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     *
     * @return the parents model name from sus entity
     */
    private String getParentsModelNameFromSusEntity( EntityManager entityManager, SuSEntity susEntity ) {
        List< SuSEntity > parents = susDAO.getParents( entityManager, susEntity );
        if ( CollectionUtils.isEmpty( parents ) ) {
            return null;
        }
        SuSEntity parent = parents.get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var parentModel = configManager.getObjectTypeByIdAndConfigName( parent.getTypeId().toString(), parent.getConfig() );
        return parentModel != null ? parentModel.getName() : null;
    }

    /**
     * Prepare overview script payload.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     * @param basePath
     *         the base path
     * @param fileName
     *         the file name
     * @param token
     *         the token
     * @param language
     *         the language
     *
     * @return the string
     */
    private String prepareOverviewScriptPayload( EntityManager entityManager, SuSEntity susEntity, String basePath, String fileName,
            String token, String language ) {
        Path inputFilePath = Path.of( basePath + File.separator + ConstantsProjectOverview.INPUT_FILE_NAME );
        OverviewPluginUtil.recreateInputDirectory( Path.of( basePath ), inputFilePath );
        Map< String, Object > payloadMap = new HashMap<>();
        List< Map< String, Object > > parents = new ArrayList<>();
        addParentsToList( entityManager, susEntity, parents );
        Collections.reverse( parents );
        payloadMap.put( ConstantsProjectOverview.PARENTS, parents );
        var children = susDAO.getChildren( entityManager, susEntity );
        if ( children != null ) {
            payloadMap.put( ConstantsProjectOverview.CHILDREN, children.stream().map( this::prepareBasicAndCustomAttributes ).toList() );
        }
        payloadMap.put( ConstantsProjectOverview.CURRENT_NODE, prepareBasicAndCustomAttributes( susEntity ) );
        payloadMap.put( ConstantsProjectOverview.OVERVIEW_JSON, basePath + File.separator + fileName + ConstantsFileExtension.JSON );
        payloadMap.put( ConstantsProjectOverview.OVERVIEW_ZIP, basePath + File.separator + fileName + ConstantsFileExtension.ZIP );
        payloadMap.put( ConstantsProjectOverview.TOKEN, token );
        payloadMap.put( ConstantsProjectOverview.BASE_URL, PropertiesManager.getLocationURL() );
        payloadMap.put( ConstantsProjectOverview.USER_LANGUAGE, language );
        payloadMap.put( ConstantsProjectOverview.TRANSLATION_ENABLED, PropertiesManager.hasTranslation() );
        String jsonPayload = JsonUtils.toJson( payloadMap );
        OverviewPluginUtil.writeOverviewPayloadToInputFile( inputFilePath, jsonPayload );
        return inputFilePath.toAbsolutePath().toString();
    }

    /**
     * Prepare basic and custom attributes.
     *
     * @param entity
     *         the entity
     *
     * @return the map
     */
    private Map< String, Object > prepareBasicAndCustomAttributes( SuSEntity entity ) {
        Map< String, Object > attributes = new HashMap<>();
        attributes.put( ConstantsProjectOverview.ID, entity.getComposedId().getId() );
        attributes.put( ConstantsProjectOverview.NAME, entity.getName() );
        attributes.put( ConstantsProjectOverview.DESCRIPTION, entity.getDescription() );
        var model = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );
        if ( model == null ) {
            return attributes;
        }
        attributes.put( ConstantsProjectOverview.TYPE, model.getName() );
        attributes.put( ConstantsProjectOverview.LIFE_CYCLE_STATUS,
                lifeCycleManager.getStatusByLifeCycleNameAndStatusId( model.getLifeCycle(), entity.getLifeCycleStatus() ) );
        attributes.put( ConstantsProjectOverview.OVERVIEW_PLUGIN, model.getOverviewPlugin() );
        if ( CollectionUtils.isNotEmpty( entity.getCustomAttributes() ) && CollectionUtil.isNotEmpty( model.getCustomAttributes() ) ) {
            Map< String, Object > customAttributesMap = new HashMap<>();
            entity.getCustomAttributes().forEach( customAttributeEntity -> customAttributesMap.put( customAttributeEntity.getName(),
                    ByteUtil.convertByteToObject( customAttributeEntity.getValue() ) ) );
            attributes.put( ConstantsProjectOverview.CUSTOM_ATTRIBUTES, customAttributesMap );
        }
        return attributes;
    }

    /**
     * Add parent name to list.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     * @param list
     *         the list
     */
    private void addParentsToList( EntityManager entityManager, SuSEntity susEntity, List< Map< String, Object > > list ) {
        List< SuSEntity > parents = susDAO.getParents( entityManager, susEntity );
        if ( CollectionUtils.isNotEmpty( parents ) ) {
            SuSEntity parent = parents.get( ConstantsInteger.INTEGER_VALUE_ZERO );
            if ( parent.getTypeId() != null && parent.getConfig() != null ) {
                list.add( prepareBasicAndCustomAttributes( parent ) );
                addParentsToList( entityManager, parent, list );
            }
        }
    }

    /**
     * Gets the data object trace by version and object id.
     *
     * @param objectId
     *         the object id
     * @param versionId
     *         the version id
     *
     * @return the data object trace by version and object id
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager# getDataObjectTraceByVersionAndObjectId(java.lang.String, int)
     */
    @Override
    public Object getDataObjectTraceByVersionAndObjectId( String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeKey( entityManager,
                    new VersionPrimaryKey( UUID.fromString( objectId ), versionId ) );

            return readCsvFromTraceObject( entityManager, dataObjectEntity );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObjectVersionCurveY( UUID objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeId( entityManager, DataObjectEntity.class,
                    objectId, versionId );
            if ( null != dataObjectEntity ) {
                SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() );
                if ( susModel.getClassName().equalsIgnoreCase( DataObjectCurveDTO.class.getName() )
                        && dataObjectEntity.getFile() != null ) {
                    DataObjectCurveDTO dataObjectCurve = calculateCurveXYUnitsByObjectId( entityManager,
                            ( DataObjectCurveEntity ) dataObjectEntity );
                    if ( null != dataObjectCurve ) {
                        List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                        for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                            if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectCurve.getyDimension().toLowerCase() ) ) {
                                for ( UnitsList units : unitsFamily.getUnits() ) {
                                    listToReturn.add(
                                            new SelectOptionsUI( units.getScale().toLowerCase(), units.getName().toLowerCase() ) );
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return listToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getDataObjectVersionCurveX( UUID objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        try {
            DataObjectEntity dataObjectEntity = ( DataObjectEntity ) susDAO.getObjectByCompositeId( entityManager, DataObjectEntity.class,
                    objectId, versionId );
            if ( null != dataObjectEntity ) {
                SuSObjectModel susModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() );
                if ( susModel.getClassName().equalsIgnoreCase( DataObjectCurveDTO.class.getName() )
                        && dataObjectEntity.getFile() != null ) {
                    DataObjectCurveDTO dataObjectCurve = calculateCurveXYUnitsByObjectId( entityManager,
                            ( DataObjectCurveEntity ) dataObjectEntity );
                    if ( null != dataObjectCurve ) {
                        List< UnitsFamily > unitsFamilyList = PropertiesManager.getConvertionUnits();
                        for ( UnitsFamily unitsFamily : unitsFamilyList ) {
                            if ( unitsFamily.getUnitsFamily().equalsIgnoreCase( dataObjectCurve.getxDimension().toLowerCase() ) ) {
                                for ( UnitsList units : unitsFamily.getUnits() ) {
                                    listToReturn.add(
                                            new SelectOptionsUI( units.getScale().toLowerCase(), units.getName().toLowerCase() ) );
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return listToReturn;
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
     * {@inheritDoc}
     */
    @Override
    public List< Object > getContainerOrChildsById( EntityManager entityManager, String objectId ) {
        return dataProjectManager.getContainerOrChildsById( entityManager, objectId );
    }

    /**
     * Save data object trace plot by object id.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the data object trace DTO
     */
    @Override
    public DataObjectTraceDTO saveDataObjectTracePlotByObjectId( String objectId, String objectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            DataObjectTraceEntity susEntity = ( DataObjectTraceEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( objectId ) );
            if ( susEntity != null ) {
                susEntity.setModifiedOn( new Date() );
                susEntity.setPlot( ByteUtil.convertStringToByte( objectJson ) );
                DataObjectTraceEntity ent = ( DataObjectTraceEntity ) susDAO.saveOrUpdate( entityManager, susEntity );
                return ( DataObjectTraceDTO ) prepareDataObjectTraceDTO( entityManager, ent );

            } else {
                return new DataObjectTraceDTO( new HashMap< String, String >() );
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Gets the data object trace plot by object id.
     *
     * @param objectId
     *         the object id
     *
     * @return the data object trace plot by object id
     */
    @Override
    public DataObjectTraceDTO getDataObjectTracePlotByObjectId( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectTraceEntity susEntity = ( DataObjectTraceEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                    UUID.fromString( objectId ) );
            if ( susEntity != null ) {
                if ( susEntity.getFile() == null ) {
                    susEntity.setPlot( null );
                    susDAO.save( entityManager, susEntity );
                    return new DataObjectTraceDTO( new HashMap< String, String >() );
                } else if ( susEntity.getPlot() != null ) {
                    return JsonUtils.jsonToObject( ByteUtil.convertByteToString( susEntity.getPlot() ), DataObjectTraceDTO.class );
                }
            } else {
                return new DataObjectTraceDTO( new HashMap< String, String >() );
            }
            return new DataObjectTraceDTO( new HashMap< String, String >() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElsObjectIfPermissionNone( CheckBox checkBox, UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( checkBox.getValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
                SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                        susEntity.getConfig() );
                saveOrUpdateIndexEntity( entityManager, suSObjectModel, susEntity );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectTraceDTO getDataObjectTracePlotByObjectIdAndVersionId( String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectTraceEntity susEntity = ( DataObjectTraceEntity ) susDAO.getObjectByIdAndVersion( entityManager, SuSEntity.class,
                    UUID.fromString( objectId ), versionId );
            if ( susEntity != null ) {
                if ( susEntity.getFile() == null ) {
                    susEntity.setPlot( null );
                    susDAO.save( entityManager, susEntity );
                    return new DataObjectTraceDTO( new HashMap< String, String >() );
                } else if ( susEntity.getPlot() != null ) {
                    return JsonUtils.jsonToObject( ByteUtil.convertByteToString( susEntity.getPlot() ), DataObjectTraceDTO.class );
                }
            } else {
                return new DataObjectTraceDTO( new HashMap< String, String >() );
            }
            return new DataObjectTraceDTO( new HashMap< String, String >() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare data object trace DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param dataObjectTraceEntity
     *         the sus entity
     *
     * @return the object
     */
    private Object prepareDataObjectTraceDTO( EntityManager entityManager, DataObjectTraceEntity dataObjectTraceEntity ) {
        DataObjectTraceDTO dataObjectDTO = new DataObjectTraceDTO();
        dataObjectDTO.setName( dataObjectTraceEntity.getName() );
        dataObjectDTO.setId( dataObjectTraceEntity.getComposedId().getId() );
        dataObjectDTO.setVersion( new VersionDTO( dataObjectTraceEntity.getComposedId().getVersionId() ) );
        dataObjectDTO.setCreatedOn( dataObjectTraceEntity.getCreatedOn() );
        dataObjectDTO.setModifiedOn( dataObjectTraceEntity.getModifiedOn() );
        dataObjectDTO.setParentId( null );
        dataObjectDTO.setTypeId( dataObjectTraceEntity.getTypeId() );
        dataObjectDTO.setSize(
                dataObjectTraceEntity.getSize() != null && dataObjectTraceEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                        ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectTraceEntity.getSize() )
                        : ConstantsString.NOT_AVAILABLE );

        UserDTO userById = userCommonManager.getUserById( entityManager, dataObjectTraceEntity.getOwner().getId() );
        if ( userById != null ) {
            dataObjectDTO.setCreatedBy( userById );
        }
        dataObjectDTO.setDescription( dataObjectTraceEntity.getDescription() );
        if ( dataObjectTraceEntity.getTypeId() != null ) {
            dataObjectDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( dataObjectTraceEntity.getTypeId(), dataObjectTraceEntity.getLifeCycleStatus(),
                            dataObjectTraceEntity.getConfig() ) );
        }
        DocumentEntity file = dataObjectTraceEntity.getFile();
        if ( null != file ) {
            dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
            if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                List< LocationDTO > locationDTOs = getDocumentLocations( file );
                dataObjectDTO.setLocations( locationDTOs );
            }
        }
        if ( null != dataObjectTraceEntity.getModifiedBy() ) {
            dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectTraceEntity.getCreatedBy() ) );
        }
        if ( null != dataObjectTraceEntity.getConfig() ) {
            dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectTraceEntity.getTypeId().toString(),
                    dataObjectTraceEntity.getConfig() ).getName() );
        }
        if ( null != dataObjectTraceEntity.getSize() ) {
            dataObjectDTO.getFile().setSize( dataObjectTraceEntity.getSize() );
        }

        if ( dataObjectTraceEntity.getPlot() != null ) {
            DataObjectTraceDTO tracePlot = JsonUtils.jsonToObject( ByteUtil.convertByteToString( dataObjectTraceEntity.getPlot() ),
                    DataObjectTraceDTO.class );
            dataObjectDTO.setLayout( tracePlot.getLayout() );
            dataObjectDTO.setData( tracePlot.getData() );
        }

        return dataObjectDTO;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public Object getModelByObjectId( String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectPredictionModelEntity predictionModelEntity = ( DataObjectPredictionModelEntity ) susDAO.getLatestObjectByTypeAndId(
                    entityManager, SuSEntity.class, UUID.fromString( id ) );
            Object obj = new JSONParser().parse(
                    new FileReader( PropertiesManager.getVaultPath() + predictionModelEntity.getFile().getFilePath() ) );

            org.json.simple.JSONObject response = ( org.json.simple.JSONObject ) obj;

            response.put( "kerasModel", getKerasModelEntry( predictionModelEntity ) );

            return response;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return null;
    }

    /**
     * Gets the keras model entry.
     *
     * @param predictionModelEntity
     *         the prediction model entity
     *
     * @return the keras model entry
     */
    private Map< String, String > getKerasModelEntry( DataObjectPredictionModelEntity predictionModelEntity ) {
        Map< String, String > kerasEntry = new HashMap<>();
        kerasEntry.put( "name", predictionModelEntity.getJsonFile().getFileName() );
        kerasEntry.put( "path", predictionModelEntity.getJsonFile().getFilePath() );
        String url = CommonUtils.getBaseUrl( PropertiesManager.getWebBaseURL() ) + File.separator + ConstantsString.STATIC_PATH
                + predictionModelEntity.getJsonFile().getFilePath() + File.separator + predictionModelEntity.getJsonFile().getFileName();
        kerasEntry.put( "url", url );
        return kerasEntry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< ModelFilesDTO > getModelFilesData( UUID objectId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ModelFilesDTO > listOfFiles = new ArrayList<>();
            DataObjectPredictionModelEntity predictionModelEntity = ( DataObjectPredictionModelEntity ) susDAO.getLatestObjectByTypeAndId(
                    entityManager, SuSEntity.class, UUID.fromString( objectId.toString() ) );
            if ( predictionModelEntity.getJsonFile() != null ) {
                listOfFiles.add( getJsonModelFile( predictionModelEntity ) );
            }
            if ( predictionModelEntity.getBinFile() != null ) {
                listOfFiles.add( getBinModelFile( predictionModelEntity ) );
            }
            List< ModelFilesDTO > filteredList = applySearchOnModelFiles( filtersDTO, listOfFiles );
            return PaginationUtil.constructFilteredResponse( filtersDTO, filteredList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Apply search on model files list.
     *
     * @param filtersDTO
     *         the filters to apply
     * @param listOfFiles
     *         list of all model files
     *
     * @return List of files that match the search pattern
     */
    private List< ModelFilesDTO > applySearchOnModelFiles( FiltersDTO filtersDTO, List< ModelFilesDTO > listOfFiles ) {
        filtersDTO.setTotalRecords( ( long ) listOfFiles.size() );
        if ( ( filtersDTO.getSearch() == null ) || ( filtersDTO.getSearch().isEmpty() ) ) {
            filtersDTO.setFilteredRecords( ( long ) listOfFiles.size() );
            return listOfFiles;
        }
        List< ModelFilesDTO > filteredList = new ArrayList<>();
        for ( ModelFilesDTO file : listOfFiles ) {
            if ( file.getName().toLowerCase().contains( filtersDTO.getSearch().toLowerCase() ) ) {
                filteredList.add( file );
            }
        }
        filtersDTO.setFilteredRecords( ( long ) filteredList.size() );
        return filteredList;
    }

    /**
     * Gets json model file.
     *
     * @param predictionModelEntity
     *         the prediction model entity
     *
     * @return the json model file
     */
    private ModelFilesDTO getJsonModelFile( DataObjectPredictionModelEntity predictionModelEntity ) {
        ModelFilesDTO modelFileJson = new ModelFilesDTO();
        modelFileJson.setName( predictionModelEntity.getJsonFile().getFileName() );
        modelFileJson.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( predictionModelEntity.getCreatedBy() ) );
        modelFileJson.setCreatedOn( predictionModelEntity.getCreatedOn() );
        modelFileJson.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( predictionModelEntity.getModifiedBy() ) );
        modelFileJson.setModifiedOn( predictionModelEntity.getModifiedOn() );

        if ( predictionModelEntity.getJsonFile().getSize() != null
                && predictionModelEntity.getJsonFile().getSize() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
            modelFileJson.setSize(
                    org.apache.commons.io.FileUtils.byteCountToDisplaySize( predictionModelEntity.getJsonFile().getSize() ) );
        } else {
            modelFileJson.setSize( ConstantsString.NOT_AVAILABLE );
        }

        return modelFileJson;
    }

    /**
     * Gets bin model file.
     *
     * @param predictionModelEntity
     *         the prediction model entity
     *
     * @return the bin model file
     */
    private ModelFilesDTO getBinModelFile( DataObjectPredictionModelEntity predictionModelEntity ) {
        ModelFilesDTO modelFileJson = new ModelFilesDTO();
        modelFileJson.setName( predictionModelEntity.getBinFile().getFileName() );
        modelFileJson.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( predictionModelEntity.getCreatedBy() ) );
        modelFileJson.setCreatedOn( predictionModelEntity.getCreatedOn() );
        modelFileJson.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( predictionModelEntity.getModifiedBy() ) );
        modelFileJson.setModifiedOn( predictionModelEntity.getModifiedOn() );

        if ( predictionModelEntity.getBinFile().getSize() != null
                && predictionModelEntity.getBinFile().getSize() != ConstantsInteger.INTEGER_VALUE_ZERO ) {
            modelFileJson.setSize( org.apache.commons.io.FileUtils.byteCountToDisplaySize( predictionModelEntity.getBinFile().getSize() ) );
        } else {
            modelFileJson.setSize( ConstantsString.NOT_AVAILABLE );
        }

        return modelFileJson;
    }

    /**
     * Contains ignore case.
     *
     * @param list
     *         the list
     * @param value
     *         the value
     *
     * @return true, if successful
     */
    private static boolean containsIgnoreCase( List< String > list, String value ) {
        return list.stream().noneMatch( format -> format.equalsIgnoreCase( value ) );
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
     * Sets data manager.
     *
     * @param dataManager
     *         the data manager
     */
    public void setDataManager( DataManager dataManager ) {
        this.dataManager = dataManager;
    }

    /**
     * Sets data project manager.
     *
     * @param dataProjectManager
     *         the data project manager
     */
    public void setDataProjectManager( DataProjectManager dataProjectManager ) {
        this.dataProjectManager = dataProjectManager;
    }

    /**
     * Sets data dashboard manager.
     *
     * @param dataDashboardManager
     *         the data dashboard manager
     */
    public void setDataDashboardManager( DataDashboardManager dataDashboardManager ) {
        this.dataDashboardManager = dataDashboardManager;
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
     * Sets the data DAO.
     *
     * @param dataDAO
     *         the new data DAO
     */
    public void setDataDAO( DataDAO dataDAO ) {
        this.dataDAO = dataDAO;
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
     * Gets the audit log manager.
     *
     * @return the audit log manager
     */
    @Override
    public AuditLogManager getAuditLogManager() {
        return auditLogManager;
    }

    /**
     * Sets the audit log manager.
     *
     * @param auditLogManager
     *         the new audit log manager
     */
    public void setAuditLogManager( AuditLogManager auditLogManager ) {
        this.auditLogManager = auditLogManager;
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