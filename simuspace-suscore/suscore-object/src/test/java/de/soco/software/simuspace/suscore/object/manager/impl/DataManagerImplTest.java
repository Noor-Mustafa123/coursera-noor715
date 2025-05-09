package de.soco.software.simuspace.suscore.object.manager.impl;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MovieUtils;
import de.soco.software.suscore.jsonschema.reader.ConfigFilePathReader;

/**
 * Test Cases For DataManagerImpl.
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { ConfigFilePathReader.class, JsonUtils.class, FileUtils.class, PropertiesManager.class, CommonUtils.class,
        MovieUtils.class } )
public class DataManagerImplTest {

//    /**
//     * The Constant INDEX.
//     */
//    private static final int INDEX = 0;
//
//    /**
//     * The Constant VIEW_NAME.
//     */
//    private static final String VIEW_NAME = "VIEW";
//
//    /**
//     * The Constant OBJECT_VIEW_ID.
//     */
//    private static final String OBJECT_VIEW_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant OBJECT_VIEW_PAYLOAD.
//     */
//    private static final String OBJECT_VIEW_PAYLOAD = "{\"id\":\"" + OBJECT_VIEW_ID
//            + "\",\"name\": \"view-1\",\"defaultView\": false,\"settings\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";
//
//    /**
//     * The Constant MAKE_MP4_FILE.
//     */
//    private static final String MAKE_MP4_FILE = "makeMp4File";
//
//    /**
//     * The Constant MAKE_WEBM_FILE.
//     */
//    private static final String MAKE_WEBM_FILE = "makeWebmFile";
//
//    /**
//     * The Constant PREPARE_THUMBNAIL_FROM_MOVIE_FILE.
//     */
//    private static final String PREPARE_THUMBNAIL_FROM_MOVIE_FILE = "prepareThumbnailFromMovieFile";
//
//    /**
//     * The Constant GET_MOVIE_POSTER.
//     */
//    private static final String GET_MOVIE_POSTER = "getMoviePoster";
//
//    /**
//     * The Constant PREPARE_FF_MPEG_COMMAND.
//     */
//    private static final String PREPARE_FF_MPEG_COMMAND = "prepareFFMpegCommand";
//
//    /**
//     * The Constant GET_VAULT_PATH.
//     */
//    private static final String GET_VAULT_PATH = "getVaultPath";
//
//    /**
//     * The Constant FIRST_INDEX_OF_LIST.
//     */
//    private static final int FIRST_INDEX_OF_LIST = 0;
//
//    /**
//     * The Constant GENERIC_DTO_TYPE.
//     */
//    private static final String GENERIC_DTO_TYPE = "32ab43d4-d062-460b-b4aa-e1a4d9252001";
//
//    /**
//     * The Constant OTHER_DTO_TYPE.
//     */
//    private static final String OTHER_DTO_TYPE = "32ab43d4-d062-460b-b4ww-e1a4d9252001";
//
//    /**
//     * The Constant GROUP_TABLE_NAME.
//     */
//    private static final String GROUP_TABLE_NAME = "group";
//
//    /**
//     * The Constant USER_TABLE_NAME.
//     */
//    private static final String USER_TABLE_NAME = "user";
//
//    /**
//     * The Constant UI_COLUMN_VALUE_YES.
//     */
//    private static final String UI_COLUMN_OPTION_VALUE_YES = "Yes";
//
//    /**
//     * The Constant UI_COLUMN_OPTION_VALUE_NO.
//     */
//    private static final String UI_COLUMN_OPTION_VALUE_NO = "No";
//
//    /**
//     * The Constant GROUP_SELECTION.
//     */
//    private static final String GROUP_SELECTION = UUID.randomUUID().toString();
//
//    /**
//     * The Constant SELECT_USER.
//     */
//    private static final String SELECT_USER = "Select Users";
//
//    /**
//     * The Constant SELECTION_TYPE_TABLE.
//     */
//    private static final String SELECTION_TYPE_TABLE = "table";
//
//    /**
//     * The Constant USER_SELECTION_PATH.
//     */
//    private static final String USER_SELECTION_PATH = "system/user";
//
//    /**
//     * The Constant status list key for form.
//     */
//    public static final String STATUS_KEY = "status";
//
//    /**
//     * The Constant SELECT_GROUP.
//     */
//    private static final String SELECT_GROUP = "Select Groups";
//
//    /**
//     * The Constant GROUP_SELECTION_PATH.
//     */
//    private static final String GROUP_SELECTION_PATH = "system/permissions/groups";
//
//    /**
//     * The Constant VAULT_PATH.
//     */
//    private static final String VAULT_PATH = "asdf";
//
//    /**
//     * The Constant FILE_CALCULATED_CHECKSUM.
//     */
//    private static final String FILE_CALCULATED_CHECKSUM = "calculated check sum";
//
//    /**
//     * The Constant CONTEXT_SELECTION_KEY_FOR_DELETING_META_DATA.
//     */
//    private static final String CONTEXT_SELECTION_KEY_FOR_DELETING_META_DATA = "test";
//
//    /**
//     * The Constant IN_VALID_CLASS_NAME.
//     */
//    private static final String IN_VALID_CLASS_NAME = "in_valid_class_name";
//
//    /**
//     * The Constant LOCATION_ID.
//     */
//    private static final UUID LOCATION_ID = UUID.randomUUID();
//
//    /**
//     * The Constant DEAFULT_LOCATION_NAME.
//     */
//    private static final String DEAFULT_LOCATION_NAME = "Default";
//
//    /**
//     * The Constant DATA_OBJECT_DTO_CLASS_NAME.
//     */
//    private static final String DATA_OBJECT_DTO_CLASS_NAME = "de.soco.software.simuspace.suscore.data.model.DataObjectDTO";
//
//    /**
//     * The Constant EMPTY_JSON_OBJECT_STR.
//     */
//    private static final String EMPTY_JSON_OBJECT_STR = "{\"name\":\"\"}";
//
//    /**
//     * The Constant STR_UUID_PRENT_ID.
//     */
//    private static final String STR_UUID_PRENT_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant STR_UUID_OBJECT_ID.
//     */
//    private static final String STR_UUID_OBJECT_TYPE_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant EMPTY_STR_UUID_PRENT_ID.
//     */
//    private static final String EMPTY_STR_UUID_PRENT_ID = "";
//
//    /**
//     * The Constant EMPTY_STR_UUID_OBJECT_TYPE_ID.
//     */
//    private static final String EMPTY_STR_UUID_OBJECT_TYPE_ID = "";
//
//    /**
//     * The Constant INVALID_STR_UUID_PRENT_ID.
//     */
//    private static final String INVALID_STR_UUID_PRENT_ID = "in_valid_parent_uuid_string";
//
//    /**
//     * The Constant INVALID_STR_UUID_OBJECT_TYPE_ID.
//     */
//    private static final String INVALID_STR_UUID_OBJECT_TYPE_ID = "in_valid_object_type_uuid_string";
//
//    /**
//     * The Constant UI_COLUMN_TYPE_SELECT.
//     */
//    private static final String UI_COLUMN_TYPE_SELECT = "select";
//
//    /**
//     * The Constant PROJECT_CONFIG_FILE_NAME.
//     */
//    private static final String PROJECT_CONFIG_FILE_NAME = "masterConfig.js";
//
//    /**
//     * The Constant SUPER_USER_NAME.
//     */
//    private static final String SUPER_USER_NAME = "simuspace";
//
//    /**
//     * The Constant LIFECYCLE.
//     */
//    private static final String WIP_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";
//
//    /**
//     * The Constant WIP_NAME.
//     */
//    private static final String WIP_NAME = "WIP";
//
//    /**
//     * The Constant SYSTEM_WORKFLOW_ID.
//     */
//    private static final String SYSTEM_WORKFLOW_ID = "b0049f08-1481-4a3f-83c5-c719797510c6";
//
//    /**
//     * The mock control.
//     */
//    private static IMocksControl mockControl = EasyMock.createControl();
//
//    /**
//     * The context selection manager.
//     */
//    private SelectionManager selectionManager;
//
//    /**
//     * DataManagerImpl reference.
//     */
//    private DataManagerImpl manager;
//
//    /**
//     * The config manager.
//     */
//    private ObjectTypeConfigManagerImpl configManager;
//
//    /**
//     * SusDao reference.
//     */
//    private SuSGenericObjectDAO< SuSEntity > dao;
//
//    /**
//     * The permission manager.
//     */
//    private PermissionManager permissionManager;
//
//    /**
//     * The user common manager.
//     */
//    private UserCommonManager userCommonManager;
//
//    /**
//     * The acl common security identity DAO.
//     */
//    private AclCommonSecurityIdentityDAO aclCommonSecurityIdentityDAO;
//
//    /**
//     * The Constant CLASS_ID.
//     */
//    private static final UUID CLASS_ID = UUID.randomUUID();
//
//    /**
//     * The Constant OBJECT_IDENTITY_ID.
//     */
//    private static final UUID OBJECT_IDENTITY_ID = UUID.randomUUID();
//
//    /**
//     * The Constant PARENT_OBJECT_IDENTITY_ID.
//     */
//    private static final UUID PARENT_OBJECT_IDENTITY_ID = UUID.randomUUID();
//
//    /**
//     * The Constant ENTRY_ID.
//     */
//    private static final UUID ENTRY_ID = UUID.randomUUID();
//
//    /**
//     * The object identity DAO.
//     */
//    private AclObjectIdentityDAO objectIdentityDAO;
//
//    /**
//     * The acl entry DAO.
//     */
//    private AclEntryDAO aclEntryDAO;
//
//    /**
//     * The filters DTO.
//     */
//    private FiltersDTO filtersDTO;
//
//    /**
//     * The class entity.
//     */
//    private AclClassEntity classEntity;
//
//    /**
//     * The object identity entity.
//     */
//    private AclObjectIdentityEntity objectIdentityEntity;
//
//    /**
//     * The acl object identity entities.
//     */
//    private List< AclObjectIdentityEntity > aclObjectIdentityEntities;
//
//    /**
//     * The acl entry entities.
//     */
//    private List< AclEntryEntity > aclEntryEntities;
//
//    /**
//     * The context menu manager.
//     */
//    private ContextMenuManager contextMenuManager;
//
//    /**
//     * The object view manager.
//     */
//    private ObjectViewManager objectViewManager;
//
//    /**
//     * The job manager.
//     */
//    private JobManager jobManager;
//
//    /**
//     * The user common DAO.
//     */
//    private UserCommonDAO userCommonDAO;
//
//    /**
//     * The life cycle manager.
//     */
//    private LifeCycleManager lifeCycleManager;
//
//    /**
//     * The location manager.
//     */
//    private LocationManager locationManager;
//
//    /**
//     * Generic Rule for the expected exception.
//     */
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//
//    /**
//     * Dummy Empty String for test Cases.
//     */
//    private static final String EMPTY_STRING = "";
//
//    /**
//     * Dummy Default user Id.
//     */
//    private static final String DEFAULT_USER_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant SUPER_USER_ID.
//     */
//    private static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";
//
//    /**
//     * The Constant SUPER_USER_ID_FROM_FILE.
//     */
//    private static final String SUPER_USER_ID_FROM_FILE = UUID.randomUUID().toString();
//
//    /**
//     * Dummy Version Id for test Cases.
//     */
//    private static final int DEFAULT_VERSION_ID = 1;
//
//    /**
//     * Dummy project Id.
//     */
//    private static final UUID PROJECT_ID = UUID.randomUUID();
//
//    private static final String TYPE_ID = UUID.randomUUID().toString();
//
//    /**
//     * Project Id nOt Exist Error Msg.
//     */
//    private static final String PROJECT_ID_NOT_EXIST_ERROR_MSG = "Project Not Exist with Id:";
//
//    /**
//     * Dummy Object Id.
//     */
//    private static final UUID DATA_OBJECT_ID = UUID.randomUUID();
//
//    /**
//     * Dummy Project Name of an object.
//     */
//    private static final String PROJECT_NAME = "Test PROJECT name";
//
//    /**
//     * The Constant DATA_OBJECT_CONFIG.
//     */
//    private static final String DATA_OBJECT_CONFIG = "62c4c2f6-15f8-11e7-93ae-92361f002674";
//
//    /**
//     * Dummy Data Object Name of an object.
//     */
//    private static final String DATA_OBJECT_NAME = "Test Data Object name";
//
//    /**
//     * Dummy Project Description of an object.
//     */
//    private static final String PROJECT_DESCRPTION = "Test PROJECT Description";
//
//    /**
//     * TEST_CUSTOM_ATTRIBUTE.
//     */
//    private static final String TEST_CUSTOM_ATTRIBUTE = "test1";
//
//    private static final byte[] TEST_CUSTOM_ATTRIBUTE_BYTE = new byte[]{ 0x74, 0x65, 0x73, 0x74, 0x31, 0x22 };
//
//    /**
//     * TEST_CUSTOM_ATTRIBUTE.
//     */
//    private static final String TEST_CUSTOM_ATTRIBUTE_OPTIONS = "{test1,test2,test3}";
//
//    /**
//     * CUSTOM_ATTRIBUTE_UUID.
//     */
//    private static final UUID CUSTOM_ATTRIBUTE_UUID = UUID.randomUUID();
//
//    /**
//     * Dummy projectDto Object.
//     */
//    private ProjectDTO projectDTO = new ProjectDTO();
//
//    /**
//     * The workflow project DTO.
//     */
//    private WorkflowProjectDTO workflowProjectDTO = new WorkflowProjectDTO();
//
//    /**
//     * Dummy Error Message to test.
//     */
//    private static final String PROJECT_ID_CANNOT_NULL = "Project Id can not be null or empty";
//
//    /**
//     * Dummy Error Message to test.
//     */
//    private static final String NO_OBJECTS_FOUND_ERROR_MSG = "No Objects Found";
//
//    /**
//     * Dummy Index for List get.
//     */
//    private static final int FIRST_INDEX = 0;
//
//    /**
//     * The Constant SET_INHERITED.
//     */
//    private static final String SET_INHERITED = "1";
//
//    /**
//     * Data dao reference for mocking.
//     */
//    private DataDAO dataDAO;
//
//    /**
//     * Dummy project Entity Object.
//     */
//
//    private ProjectEntity projectEntity = new ProjectEntity();
//
//    /**
//     * The workflow project entity.
//     */
//    private WorkflowProjectEntity workflowProjectEntity = new WorkflowProjectEntity();
//
//    /**
//     * The library entity.
//     */
//    private LibraryEntity libraryEntity = new LibraryEntity();
//
//    /**
//     * The variant entity.
//     */
//    private VariantEntity variantEntity = new VariantEntity();
//
//    /**
//     * Dummy dataObject Entity Object.
//     */
//    private DataObjectEntity dataObjectEntity = new DataObjectEntity();
//
//    /**
//     * The user.
//     */
//    private UserDTO user;
//
//    /**
//     * Dummy CreatedOn Project Date.
//     */
//    private static final Date CREATED_ON_DATE = new Date();
//
//    /**
//     * Dummy UpdatedOn Project Date.
//     */
//    private static final Date UPDATED_ON_DATE = new Date();
//
//    /**
//     * Dummy Custom Attributes Values.
//     */
//    private static final String DUMMY_CUSTOM_ATTRIBUTE_1 = "test1";
//
//    /**
//     * Dummy Custom Attributes Values.
//     */
//    private static final String DUMMY_CUSTOM_ATTRIBUTE_2 = "test2";
//
//    /**
//     * Dummy Custom Attributes Values.
//     */
//    private static final String DUMMY_CUSTOM_ATTRIBUTE_3 = "test3";
//
//    /**
//     * Dummy Error Message to test.
//     */
//    private static final String PROJECT_NOT_EXIST_MSG = "Project Not Exist with Id:";
//
//    /**
//     * The Constant SID_ID.
//     */
//    private static final UUID SID_ID = UUID.randomUUID();
//
//    /**
//     * The Constant OBJECT_ID.
//     */
//    private static final UUID OBJECT_ID = UUID.randomUUID();
//
//    /**
//     * The Constant SIMUSPACE.
//     */
//    private static final String SIMUSPACE = "simuspace";
//
//    /**
//     * The Constant USER.
//     */
//    private static final String USER = "User";
//
//    /**
//     * The Constant RESTRICTED_USER.
//     */
//    private static final String NOT_RESTRICTED = "No";
//
//    /**
//     * The Constant OBJECT_TYPE_ID.
//     */
//    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();
//
//    /**
//     * The Constant USER_ID.
//     */
//    private static final UUID USER_ID = UUID.randomUUID();
//
//    /**
//     * The Constant USER_SELECTION.
//     */
//    private static final String USER_SELECTION = UUID.randomUUID().toString();
//
//    /**
//     * The Constant META_DATA_KEY.
//     */
//    public static final String META_DATA_KEY = "key";
//
//    /**
//     * The Constant META_DATA_VALUE.
//     */
//    public static final String META_DATA_VALUE = "value";
//
//    /**
//     * The document manager.
//     */
//    private DocumentManager documentManager;
//
//    /**
//     * The document DAO.
//     */
//    private DocumentDAO documentDAO;
//
//    /**
//     * The thread pool executor service.
//     */
//    private ThreadPoolExecutorService threadPoolExecutorService;
//
//    /**
//     * The Constant TEST_META_DATA_FILE.
//     */
//    private static final String TEST_META_DATA_FILE = "test.txt";
//
//    /**
//     * The Constant JSON_TO_OBJECT_JSON_UTILS_STATIC_METHOD.
//     */
//    private static final String JSON_TO_OBJECT_JSON_UTILS_STATIC_METHOD = "jsonToObject";
//
//    /**
//     * The Constant CONVERT_FILE_CONTENTS_TO_STRING.
//     */
//    private static final String CONVERT_FILE_CONTENTS_TO_STRING = "convertFileContentsToString";
//
//    /**
//     * The Constant GET_BASE_URL_COMMON_UTILS_METHOD.
//     */
//    private static final String GET_BASE_URL_COMMON_UTILS_METHOD = "getBaseUrl";
//
//    /**
//     * The Constant PARENT_ID_OF_WORKFLOWS.
//     */
//    private static final String PARENT_ID_OF_WORKFLOWS = "dc14ac39-1243-484a-94ba-12db7bb46930";
//
//    /**
//     * The Constant DEFAULT_LIFECYCLE_STATUS.
//     */
//    private static final String WIP_LIFECYCLE_STATUS_ID = "553536c7-71ec-409d-8f48-ec779a98a68e";
//
//    /**
//     * The Constant TABLE_COLUMN_LIST.
//     */
//    private static final String TABLE_COLUMN_LIST = "[{\"data\":\"name\",\"title\":\"Name\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"name\",\"manage\":true},\"name\":\"name\",\"orderNum\":0},{\"data\":\"type\",\"title\":\"Type\",\"filter\":\"text\",\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"text\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":\"type\",\"manage\":true},\"name\":\"type\",\"orderNum\":0},{\"data\":\"permissionDTOs.0.value\",\"title\":\"View\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.0.manage\"},\"name\":\"permissionDTOs-View\",\"orderNum\":0},{\"data\":\"permissionDTOs.1.value\",\"title\":\"ViewAuditLog\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.1.manage\"},\"name\":\"permissionDTOs-ViewAuditLog\",\"orderNum\":0},{\"data\":\"permissionDTOs.2.value\",\"title\":\"Read\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.2.manage\"},\"name\":\"permissionDTOs-Read\",\"orderNum\":0},{\"data\":\"permissionDTOs.3.value\",\"title\":\"Write\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.3.manage\"},\"name\":\"permissionDTOs-Write\",\"orderNum\":0},{\"data\":\"permissionDTOs.4.value\",\"title\":\"Execute\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.4.manage\"},\"name\":\"permissionDTOs-Execute\",\"orderNum\":0},{\"data\":\"permissionDTOs.5.value\",\"title\":\"Export\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.5.manage\"},\"name\":\"permissionDTOs-Export\",\"orderNum\":0},{\"data\":\"permissionDTOs.6.value\",\"title\":\"Delete\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.6.manage\"},\"name\":\"permissionDTOs-Delete\",\"orderNum\":0},{\"data\":\"permissionDTOs.7.value\",\"title\":\"Restore\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.7.manage\"},\"name\":\"permissionDTOs-Restore\",\"orderNum\":0},{\"data\":\"permissionDTOs.8.value\",\"title\":\"CreateNewObject\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.8.manage\"},\"name\":\"permissionDTOs-CreateNewObject\",\"orderNum\":0},{\"data\":\"permissionDTOs.9.value\",\"title\":\"CreateNewVersion\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.9.manage\"},\"name\":\"permissionDTOs-CreateNewVersion\",\"orderNum\":0},{\"data\":\"permissionDTOs.10.value\",\"title\":\"Update\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.10.manage\"},\"name\":\"permissionDTOs-Update\",\"orderNum\":0},{\"data\":\"permissionDTOs.11.value\",\"title\":\"Kill\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.11.manage\"},\"name\":\"permissionDTOs-Kill\",\"orderNum\":0},{\"data\":\"permissionDTOs.12.value\",\"title\":\"Share\",\"filter\":null,\"sortable\":true,\"visible\":true,\"renderer\":{\"type\":\"checkbox\",\"url\":null,\"separator\":null,\"labelClass\":null,\"data\":null,\"manage\":\"permissionDTOs.12.manage\"},\"name\":\"permissionDTOs-Share\",\"orderNum\":0}]";
//
//    /**
//     * The Constant SYNC_CONTEXT_TITLE.
//     */
//    private static final String SYNC_CONTEXT_TITLE = "Sync Files";
//
//    /**
//     * The Constant SYNC_CONTEXT_TITLE2.
//     */
//    private static final String SYNC_CONTEXT_TITLE2 = "Sync Files2";
//
//    /**
//     * The Constant SYNC_CONTEXT_URL.
//     */
//    private static final String SYNC_CONTEXT_URL = "Sync Url";
//
//    /**
//     * The Constant SYNC_CONTEXT_URL2.
//     */
//    private static final String SYNC_CONTEXT_URL2 = "Sync Url2";
//
//    /**
//     * The Constant DOCUMENT_ID.
//     */
//    private static final UUID DOCUMENT_ID = UUID.randomUUID();
//
//    /**
//     * The Constant TEST_CONTEXT_URL.
//     */
//    private static final String TEST_CONTEXT_URL = "test-url";
//
//    /**
//     * The Constant TEST_CONTEXT_ICON.
//     */
//    private static final String TEST_CONTEXT_ICON = "fe fe-icon";
//
//    /**
//     * The Constant TEST_CONTEXT_TITLE.
//     */
//    private static final String TEST_CONTEXT_TITLE = "test-title";
//
//    /**
//     * The Constant SELECTION_ID.
//     */
//    private static final UUID SELECTION_ID = UUID.randomUUID();
//
//    /**
//     * The Constant WIP_LIFECYCLE_STATUS_NAME.
//     */
//    private static final String WIP_LIFECYCLE_STATUS_NAME = "WIP";
//
//    /**
//     * The Constant ALL_VISIBILITY.
//     */
//    private static final String ALL_VISIBILITY = "all";
//
//    /**
//     * The Constant UNSET_INHERITED.
//     */
//    private static final String UNSET_INHERITED = "0";
//
//    /**
//     * The Constant DEFAULT_SELECTION_ID.
//     */
//    private static final String DEFAULT_SELECTION_ID = "default";
//
//    /**
//     * The Constant TEST_FILE_NAME.
//     */
//    private static final String TEST_FILE_NAME = "test";
//
//    /**
//     * The Constant TEST_FILE_NAME.
//     */
//    private static final String CURVE_FILE_NAME = "curve.json";
//
//    /**
//     * The Constant X_DIMENSION.
//     */
//    private static final String X_DIMENSION = "X";
//
//    /**
//     * The Constant Y_DIMENSION.
//     */
//    private static final String Y_DIMENSION = "Y";
//
//    /**
//     * The Constant X_UNIT.
//     */
//    private static final String X_UNIT = "x";
//
//    /**
//     * The Constant Y_UNIT.
//     */
//    private static final String Y_UNIT = "y";
//
//    /**
//     * The Constant CURVE_JSON_FILE_PATH.
//     */
//    private static final String CURVE_JSON_FILE_PATH = "src/test/resources/TestCurve.json";
//
//    /**
//     * The Constant WEBM.
//     */
//    private static final String WEBM = "webm";
//
//    /**
//     * The Constant MP4.
//     */
//    private static final String MP4 = "mp4";
//
//    /**
//     * The Constant POSTER.
//     */
//    private static final String POSTER = "test.png";
//
//    /**
//     * The Constant THUMB_NAIL.
//     */
//    private static final String THUMB_NAIL = "testthumb.png";
//
//    /**
//     * The Constant VIDEO_MP4.
//     */
//    private static final String VIDEO_MP4 = "video/mp4";
//
//    /**
//     * The Constant VIEW_ID.
//     */
//    private static final String VIEW_ID = UUID.randomUUID().toString();
//
//    /**
//     * The Constant STATIC_PATH.
//     */
//    private static final String STATIC_PATH = File.separator + "statictest" + File.separator;
//
//    /**
//     * The link DAO.
//     */
//    private LinkDAO linkDAO;
//
//    /**
//     * Mock control.
//     *
//     * @return the i mocks control
//     */
//    static IMocksControl mockControl() {
//        return mockControl;
//    }
//
//    /**
//     * setUp which is called before entering in each test case.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Before
//    public void setUp() throws Exception {
//        mockControl().resetToNice();
//        manager = new DataManagerImpl();
//        dao = mockControl().createMock( SuSGenericObjectDAO.class );
//        userCommonManager = mockControl().createMock( UserCommonManager.class );
//        userCommonDAO = mockControl().createMock( UserCommonDAO.class );
//        aclCommonSecurityIdentityDAO = mockControl.createMock( AclCommonSecurityIdentityDAO.class );
//        aclEntryDAO = mockControl.createMock( AclEntryDAO.class );
//        selectionManager = mockControl.createMock( SelectionManager.class );
//        permissionManager = mockControl.createMock( PermissionManager.class );
//        objectIdentityDAO = mockControl.createMock( AclObjectIdentityDAO.class );
//        contextMenuManager = mockControl.createMock( ContextMenuManager.class );
//        lifeCycleManager = mockControl.createMock( LifeCycleManager.class );
//        locationManager = mockControl.createMock( LocationManager.class );
//        objectViewManager = mockControl.createMock( ObjectViewManager.class );
//        linkDAO = mockControl.createMock( LinkDAO.class );
//        manager.setSusDAO( dao );
//        dataDAO = mockControl().createMock( DataDAO.class );
//        manager.setDataDAO( dataDAO );
//        manager.setPermissionManager( permissionManager );
//        manager.setUserCommonManager( userCommonManager );
//        manager.setObjectViewManager( objectViewManager );
//        configManager = mockControl().createMock( ObjectTypeConfigManagerImpl.class );
//        documentManager = mockControl().createMock( DocumentManager.class );
//        documentDAO = mockControl().createMock( DocumentDAO.class );
//        manager.setConfigManager( configManager );
//        manager.setContextMenuManager( contextMenuManager );
//        manager.setLifeCycleManager( lifeCycleManager );
//        manager.setSelectionManager( selectionManager );
//        threadPoolExecutorService = mockControl.createMock( ThreadPoolExecutorService.class );
//        manager.setThreadPoolExecutorService( threadPoolExecutorService );
//        manager.setLocationManager( locationManager );
//        manager.setLinkDao( linkDAO );
//        fillProjectEntityOfTypeData();
//        fillLibraryEntityOfTypeData();
//        fillVariantEntityOfTypeData();
//        fillWorkflowProjectEntityOfTypeData();
//        fillProjectDto();
//        fillWorkflowProjectDto();
//        populateFilterDTO();
//        fillClassEntity();
//        fillObjectIdentity( classEntity, fillSecurityIdentity( SID_ID ) );
//        aclObjectIdentityEntities = new ArrayList<>();
//        aclObjectIdentityEntities.add( objectIdentityEntity );
//        aclEntryEntities = new ArrayList<>();
//        aclEntryEntities.add( fillEntryEntity( objectIdentityEntity, fillSecurityIdentity( SID_ID ) ) );
//        fillParentObjectIdentity( fillClassEntity(), fillSecurityIdentity( SID_ID ) );
//        manager.setDocumentManager( documentManager );
//        jobManager = mockControl().createMock( JobManager.class );
//        manager.setJobManager( jobManager );
//        EasyMock.expect( configManager.getStatusByIdandObjectType( EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getStatusDto() ).anyTimes();
//        EasyMock.expect( configManager.getDefaultStatusByObjectTypeId( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getStatusDto() ).anyTimes();
//    }
//
//    /**
//     * ********************************** UPDATE PROJECT ***************************************.
//     */
//
//    /**
//     * Should Successfully Update Project With Incremented Version When Valid Project Dto Is Given
//     */
//    @Test
//    public void shouldSuccessfullyUpdateProjectWIthIncrementedVersionWhenValidProjectDtoIsGiven() {
//        SuSObjectModel model = getFilledSuSModelObject();
//
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//        ProjectDTO expected = projectDTO;
//
//        ProjectEntity expectedEntity = projectEntity;
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setUnique( false );
//        expectedEntity.getComposedId().setVersionId( DEFAULT_VERSION_ID + 1 );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( expectedEntity ) ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( expectedEntity )
//                .anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( userDTO ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        ProjectDTO actual = ( ProjectDTO ) manager.updateProject( DEFAULT_USER_ID, expected.toString() );
//        Assert.assertEquals( expected, actual );
//        // To check Version
//        Assert.assertTrue( expected.getVersion().getId() + 1 == actual.getVersion().getId() );
//
//    }
//
//    /**
//     * Gets the status config.
//     *
//     * @return the status config
//     */
//    private StatusConfigDTO getStatusConfig() {
//        List< String > canMoveTo = new ArrayList<>();
//        canMoveTo.add( WIP_LIFECYCLE_STATUS_ID );
//        StatusConfigDTO statusConfigDTO = new StatusConfigDTO();
//        statusConfigDTO.setId( WIP_LIFECYCLE_STATUS_ID );
//        statusConfigDTO.setName( WIP_LIFECYCLE_STATUS_NAME );
//        statusConfigDTO.setUnique( true );
//        statusConfigDTO.setMoveOldVersionToStatus( WIP_LIFECYCLE_STATUS_ID );
//        statusConfigDTO.setCanMoveToStatus( canMoveTo );
//        statusConfigDTO.setVisible( ALL_VISIBILITY );
//        statusConfigDTO.setAllowChanges( true );
//        statusConfigDTO.setAllowCheckOut( true );
//        return statusConfigDTO;
//    }
//
//    /**
//     * Should Throw SuSException If No Project Exist With Given ProjectId.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfProjectNameIsEmpty() {
//        ProjectEntity expectedEntity = projectEntity;
//        SuSObjectModel model = getFilledSuSModelObject();
//        thrown.expect( SusException.class );
//
//        projectDTO.setName( EMPTY_STRING );
//        ProjectDTO expected = projectDTO;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.updateProject( DEFAULT_USER_ID, expected.toString() );
//
//    }
//
//    /**
//     * Should Throw SuSException If No Project Exist With Given ProjectId.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfNoProjectExistWithGivenProjectId() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( PROJECT_NOT_EXIST_MSG + projectDTO.getId() );
//        ProjectDTO expected = projectDTO;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.updateProject( DEFAULT_USER_ID, expected.toString() );
//
//    }
//
//    /**
//     * Should Get Null Project Dto When Input Parameter Is Null For Update Project.
//     */
//    @Test
//    public void shouldGetNullProjectDtoWhenInputParameterIsNullForUpdateProject() {
//
//        ProjectDTO actual = ( ProjectDTO ) manager.updateProject( DEFAULT_USER_ID, null );
//        Assert.assertNull( actual );
//
//    }
//
//    /**
//     * ********************************** UPDATE WORKFLOW PROJECT ***************************************.
//     */
//
//    /**
//     * Should Successfully Update Workflow Project With Incremented Version When Valid Project Dto Is Given
//     */
//    @Test
//    public void shouldSuccessfullyUpdateWorkFlowProjectWithIncrementedVersionWhenValidWorkflowProjectDtoIsGiven() {
//        SuSObjectModel model = getFilledSuSModelObject();
//        WorkflowProjectDTO expected = workflowProjectDTO;
//        WorkflowProjectEntity expectedEntity = workflowProjectEntity;
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setUnique( false );
//
//        expectedEntity.getComposedId().setVersionId( DEFAULT_VERSION_ID + 1 );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( workflowProjectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( expectedEntity ) ).anyTimes();
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( expectedEntity )
//                .anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        mockControl.replay();
//        WorkflowProjectDTO actual = manager.updateWorkFlowProject( DEFAULT_USER_ID, EMPTY_STRING );
//        Assert.assertEquals( expected, actual );
//    }
//
//    /**
//     * Should Throw SuSException If No Workflow Project Exist With Given ProjectId.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfWorkflowProjectNameIsEmpty() {
//        ProjectEntity expectedEntity = projectEntity;
//        SuSObjectModel model = getFilledSuSModelObject();
//        thrown.expect( SusException.class );
//
//        workflowProjectDTO.setName( EMPTY_STRING );
//        String expected = EMPTY_STRING;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.updateWorkFlowProject( DEFAULT_USER_ID, expected );
//
//    }
//
//    /**
//     * Should throw sus exception if workflow project life cycle is not changeable.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfWorkflowProjectLifecycleIsNotChangeable() {
//        ProjectEntity expectedEntity = projectEntity;
//        expectedEntity.setLifeCycleStatus( "invalidStatus" );
//        SuSObjectModel model = getFilledSuSModelObject();
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), workflowProjectDTO.getId() ) );
//
//        StatusConfigDTO statusConfig = getStatusConfig();
//        statusConfig.setAllowChanges( false );
//
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( statusConfig ).anyTimes();
//        String expected = EMPTY_STRING;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.updateWorkFlowProject( DEFAULT_USER_ID, expected );
//
//    }
//
//    /**
//     * Should Throw SuSException If No Workflow Project Exist With Given ProjectId.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfNoWorkflowProjectExistWithGivenProjectId() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( PROJECT_NOT_EXIST_MSG + projectDTO.getId() );
//        String expected = EMPTY_STRING;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.updateWorkFlowProject( DEFAULT_USER_ID, expected );
//    }
//
//    /**
//     * Should Get Null Workflow Project Dto When Input Parameter Is Null For Update Workflow Project.
//     */
//    @Test
//    public void shouldGetNullWorkflowProjectDtoWhenInputParameterIsNullForUpdateWorkflowProject() {
//
//        WorkflowProjectDTO actual = manager.updateWorkFlowProject( DEFAULT_USER_ID, null );
//        Assert.assertNull( actual );
//
//    }
//
//    /**
//     * ********************************** UPDATE PROJECT FORM ***************************************.
//     */
//    /**
//     * Should Successfully Get Edit Project Form Filled With ProjectDto
//     */
//    @Test
//    public void shouldSuccessfullyGetEditProjectFormFilledWithProjectDto() {
//        SuSObjectModel model = getFilledSuSModelObject();
//
//        List< UIFormItem > expected = GUIUtils.prepareForm( false, projectDTO );
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        mockControl.replay();
//    }
//
//    /**
//     * Should Get Exception In Edit Project Form When Dao Return Null ProjectEntity.
//     */
//    @Test
//    public void shouldGetExceptionInEditProjectFormWhenDaoReturnNullProjectEntity() {
//        SuSEntity expected = null;
//        thrown.expect( SusException.class );
//        thrown.expectMessage( PROJECT_ID_NOT_EXIST_ERROR_MSG + PROJECT_ID.toString() );
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl.replay();
//
//        manager.editProjectForm( DEFAULT_USER_ID, PROJECT_ID.toString() );
//
//    }
//
//    /**
//     * Should Throw Exception In Edit Project Form With Null ProjectId.
//     */
//    @Test
//    public void shouldThrowExceptionInEditProjectFormWithNullProjectId() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( PROJECT_ID_CANNOT_NULL );
//
//        mockControl.replay();
//        manager.editProjectForm( DEFAULT_USER_ID, null );
//
//    }
//
//    /**
//     * Should Throw Exception In Edit Project Form With Empty String As ProjectId.
//     */
//    @Test
//    public void shouldThrowExceptionInEditProjectFormWithEmptyProjectId() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( PROJECT_ID_CANNOT_NULL );
//
//        mockControl.replay();
//        manager.editProjectForm( DEFAULT_USER_ID, EMPTY_STRING );
//
//    }
//
//    /**
//     * ********************************** GET Project ***************************************.
//     */
//
//    /**
//     * Should Successfully Get ProjectDto When Valid projectId Is Given
//     */
//    @Test
//    public void shouldSuccessfullyGetProjectDtoWhenValidProjectIdIsGiven() {
//
//        ProjectEntity expected = fillProjectEntityOfTypeData();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl.replay();
//        ProjectDTO actual = manager.getProject( DEFAULT_USER_ID, PROJECT_ID );
//        Assert.assertEquals( expected.getComposedId().getId(), actual.getId() );
//        Assert.assertEquals( expected.getName(), actual.getName() );
//
//    }
//
//    /**
//     * Should Not Get ProjectDto When InValid ProjectId Is Given.
//     */
//    @Test
//    public void shouldNotGetProjectDtoWhenInValidProjectIdIsGiven() {
//
//        ProjectEntity expected = null;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl.replay();
//        ProjectDTO actual = manager.getProject( DEFAULT_USER_ID, null );
//        Assert.assertEquals( expected, actual );
//
//    }
//
//    /**
//     * ********************************** GET DATAOBJECT ***************************************.
//     */
//
//    /**
//     * Should Successfully Get DataObject When Valid DataObject Id Is Given
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectWhenValidDataObjectIdIsGiven() {
//
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        prepareMockGetDataObjectMethods( expected );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        mockControl.replay();
//        DataObjectDTO actual = ( DataObjectDTO ) manager.getDataObject( EasyMock.anyObject( EntityManager.class ), SUPER_USER_ID,
//                DATA_OBJECT_ID );
//        Assert.assertEquals( expected.getComposedId().getId(), actual.getId() );
//        Assert.assertEquals( expected.getName(), actual.getName() );
//
//    }
//
//    /**
//     * Should successfully return object after recursive visibility check.
//     */
//    @Test
//    public void shouldSuccessfullyReturnObjectAfterVisibilityCheck() {
//        DataObjectEntity expected = fillDataObjectEntity();
//        expected.setLifeCycleStatus( null );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//
//        prepareMockGetDataObjectMethods( expected );
//        mockControl.replay();
//        DataObjectDTO actual = ( DataObjectDTO ) manager.getDataObject( DEFAULT_USER_ID, DATA_OBJECT_ID );
//        Assert.assertEquals( expected.getComposedId().getId(), actual.getId() );
//        Assert.assertEquals( expected.getName(), actual.getName() );
//    }
//
//    /**
//     * Should Not Get Data Object When InValid DataObject Id Is Given.
//     */
//    @Test
//    public void shouldNotGetDataObjectWhenInValidDataObjectIdIsGiven() {
//
//        DataObjectEntity expected = null;
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        mockControl.replay();
//        DataObjectDTO actual = ( DataObjectDTO ) manager.getDataObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, null );
//        Assert.assertEquals( expected, actual );
//
//    }
//
//    /**
//     * ******************************** Single Object UI *****************************************.
//     *
//     * @throws Exception
//     *         the exception
//     */
//
//    @Test
//    public void shouldSuccessFullyGetTabsViewListForDataObjectDtoWhenValidObjectIdIsGiven() throws Exception {
//        SuSEntity expectedObject = fillSuSEntityWithDataObjectEntity();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        prepareMockGetDataObjectMethods( expectedObject );
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        List< SubTabsUI > actual = manager.getTabsViewDataObjectUI( USER_ID.toString(), DATA_OBJECT_CONFIG ).getTabs();
//        Assert.assertFalse( actual.isEmpty() );
//        Assert.assertEquals( actual.size(), model.getViewConfig().size() );
//    }
//
//    /**
//     * Should success fully get table columns for all objects.
//     */
//    @Test
//    public void shouldSuccessFullyGetTableColumnsForAllObjects() {
//
//        SuSObjectModel model = getFilledSuSModelObject();
//        List< TableColumn > expected = prepareDataObjectTableUI( model.getCustomAttributes() );
//
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        mockControl.replay();
//        TableUI actual = manager.getListOfObjectsUITableColumns( EasyMock.anyObject( EntityManager.class ), USER, PROJECT_ID.toString(),
//                GENERIC_DTO_TYPE );
//        Assert.assertNotNull( actual );
//        for ( TableColumn actualTableColumn : actual.getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * Should successfully generate data object UI when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGenerateDataObjectUIWhenValidInputIsGiven() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        List< TableColumn > expected = prepareDataObjectTableUI( model.getCustomAttributes() );
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        mockControl.replay();
//        TableUI actual = manager.getListOfObjectsUITableColumns( EasyMock.anyObject( EntityManager.class ), USER, PROJECT_ID.toString(),
//                OTHER_DTO_TYPE );
//        Assert.assertNotNull( actual );
//        for ( TableColumn actualTableColumn : actual.getColumns() ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//        }
//    }
//
//    /**
//     * Should successfully get all data objects of project when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetAllDataObjectsOfProjectWhenValidInputIsGiven() {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( fillDataObjectEntity() ) )
//                .anyTimes();
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        FiltersDTO filtersDTO = fillFilterForDataTable();
//        FilteredResponse< Object > filteredResponse = manager.getAllChildObjectsByTypeId( USER_ID.toString(), PROJECT_ID,
//                UUID.randomUUID().toString(), filtersDTO, null );
//        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, filteredResponse.getData().size() );
//        Assert.assertEquals( createDataObjectDTOFromDataObjectEntity( PROJECT_ID, fillDataObjectEntity() ),
//                filteredResponse.getData().get( FIRST_INDEX_OF_LIST ) );
//    }
//
//    /**
//     * Should successfully get all librarys of project when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetAllLibrarysOfProjectWhenValidInputIsGiven() {
//        LibraryEntity expected = fillLibraryEntity();
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( fillLibraryEntity() ) )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        prepareMockGetDataObjectMethods( expected );
//        mockControl.replay();
//        FiltersDTO filtersDTO = fillFilterForDataTable();
//        FilteredResponse< Object > filteredResponse = manager.getAllChildObjectsByTypeId( USER_ID.toString(), PROJECT_ID,
//                UUID.randomUUID().toString(), filtersDTO, null );
//        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, filteredResponse.getData().size() );
//        Assert.assertEquals( createLibraryDTOFromLibraryEntity( PROJECT_ID, fillLibraryEntity() ),
//                filteredResponse.getData().get( FIRST_INDEX_OF_LIST ) );
//    }
//
//    /**
//     * Should successfully get all variants of project when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetAllVariantsOfProjectWhenValidInputIsGiven() {
//        VariantEntity expected = fillVariantEntity();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( fillVariantEntity() ) )
//                .anyTimes();
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        prepareMockGetDataObjectMethods( expected );
//        mockControl.replay();
//        FiltersDTO filtersDTO = fillFilterForDataTable();
//        FilteredResponse< Object > filteredResponse = manager.getAllChildObjectsByTypeId( USER_ID.toString(), PROJECT_ID,
//                UUID.randomUUID().toString(), filtersDTO, null );
//        Assert.assertEquals( ConstantsInteger.INTEGER_VALUE_ONE, filteredResponse.getData().size() );
//        Assert.assertEquals( createVariantDTOFromVariantEntity( PROJECT_ID, fillVariantEntity() ),
//                filteredResponse.getData().get( FIRST_INDEX_OF_LIST ) );
//    }
//
//    /**
//     * Should successfully generate project UI when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGenerateProjectVersionUIWhenValidInputIsGiven() {
//        List< TableColumn > expectedColumns = getProjectVersionUI();
//        TableColumn customAttribute = addCustomAttributeToTable();
//        expectedColumns.add( customAttribute );
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        SuSObjectModel suSObjectModel = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( suSObjectModel ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actualColumns = manager.getContainerVersionsUI( UUID.randomUUID().toString() );
//        Assert.assertEquals( expectedColumns.size(), actualColumns.size() );
//    }
//
//    /**
//     * Should successfully generate library version UI when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGenerateLibraryVersionUIWhenValidInputIsGiven() {
//        List< TableColumn > expectedColumns = getLibraryVersionUI();
//        TableColumn customAttribute = addCustomAttributeToTable();
//        expectedColumns.add( customAttribute );
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( libraryEntity ).anyTimes();
//        SuSObjectModel suSObjectModel = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( suSObjectModel ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actualColumns = manager.getContainerVersionsUI( UUID.randomUUID().toString() );
//        Assert.assertEquals( expectedColumns.size(), actualColumns.size() );
//    }
//
//    /**
//     * Should successfully generate variant version UI when valid input is given.
//     */
//    @Test
//    public void shouldSuccessfullyGenerateVariantVersionUIWhenValidInputIsGiven() {
//        List< TableColumn > expectedColumns = getVariantVersionUI();
//        TableColumn customAttribute = addCustomAttributeToTable();
//        expectedColumns.add( customAttribute );
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( variantEntity ).anyTimes();
//        SuSObjectModel suSObjectModel = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( suSObjectModel ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actualColumns = manager.getContainerVersionsUI( UUID.randomUUID().toString() );
//        Assert.assertEquals( expectedColumns.size(), actualColumns.size() );
//    }
//
//    /**
//     * Should success fully get table columns for single data object dto when object id exists in database.
//     */
//    @Test
//    public void shouldSuccessFullyGetTableColumnsForSingleDataObjectDtoWhenObjectIdExistsInDatabase() {
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        List< TableColumn > expected = prepareDataObjectTableUI( model.getCustomAttributes() );
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actual = manager.getObjectSingleUI( PROJECT_ID.toString() );
//        Assert.assertNotNull( actual );
//        for ( TableColumn actualTableColumn : actual ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * Should success fully get table columns for data object version when object id exists in database.
//     */
//    @Test
//    public void shouldSuccessFullyGetTableColumnsForDataObjectVersionWhenObjectIdExistsInDatabase() {
//
//        SuSObjectModel model = getFilledSuSModelObject();
//        List< TableColumn > expected = prepareDataObjectTableUI( model.getCustomAttributes() );
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actual = manager.getObjectVersionUI( PROJECT_ID.toString(), DEFAULT_VERSION_ID );
//        Assert.assertNotNull( actual );
//        for ( TableColumn actualTableColumn : actual ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * Should SuccessFully Get Tabs View List Of DataObjectDto.
//     */
//    @Test
//    public void shouldSuccessFullyGetTabsViewListOfDataObjectVersion() {
//        List< String > expected = getDummyTabsViewOfProperties();
//        SuSObjectModel model = getFilledSuSModelObject();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model );
//        EasyMock.expect(
//                        dao.getObjectByCompositeKey( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andReturn( fillDataObjectEntity );
//        mockControl.replay();
//        List< SubTabsUI > actual = manager.getTabsViewDataObjectVersionUI( USER_ID.toString(), OBJECT_ID.toString(), 1 ).getTabs();
//        Assert.assertTrue( !actual.isEmpty() );
//        for ( SubTabsUI subTabsUI : actual ) {
//            Assert.assertTrue( expected.contains( subTabsUI.getName() ) );
//        }
//    }
//
//    /**
//     * ********************************** CREATE PROJECT FORM ***************************************.
//     *
//     * @throws Exception
//     *             the exception
//     */
//
//    /**
//     * Should successfully get create project form filled with project dto.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetCreateProjectFormFilledWithProjectDto() throws Exception {
//        SuSObjectModel model = getFilledSuSModelObject();
//        List< ConfigUtil.Config > projectStartConfig = new ArrayList<>();
//        projectStartConfig.add( new ConfigUtil.Config( PROJECT_CONFIG_FILE_NAME, PROJECT_CONFIG_FILE_NAME ) );
//        ProjectDTO expectedProjectDTO = fillProjectDTOWithoutParentId();
//        List< UIFormItem > expectedCreateProjectForm = GUIUtils.prepareForm( false, expectedProjectDTO );
//        mockStaticGetMasterConfigurationFileNames( PROJECT_CONFIG_FILE_NAME );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( configManager.getMasterConfigurationFileNames() ).andReturn( projectStartConfig ).anyTimes();
//        EasyMock.expect( configManager.getProjectModelByConfigLabel( PROJECT_CONFIG_FILE_NAME ) ).andReturn( model ).anyTimes();
//
//        mockControl.replay();
//
//    }
//
//    /**
//     * Should successfully get custom attributes UI form items for create project.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetCustomAttributesUIFormItemsForCreateProject() throws Exception {
//        SuSObjectModel model = getFilledSuSModelObject();
//
//        EasyMock.expect( configManager.getProjectModelByConfigLabel( PROJECT_CONFIG_FILE_NAME ) ).andReturn( model ).anyTimes();
//
//        mockControl.replay();
//
//        List< UIFormItem > expectedCustomAttrib = convertJSONtoFormItems( model.getCustomAttributes() );
//
//    }
//
//    /**
//     * ********************************** CREATE PROJECT ***************************************.
//     */
//
//    /**
//     * Should successfully create project W ith default version when valid project dto is given.
//     */
//    @Test
//    public void shouldSuccessfullyCreateProjectWIthDefaultVersionWhenValidProjectDtoIsGiven() {
//        SuSObjectModel model = getFilledSuSModelObject();
//
//        ProjectDTO expected = projectDTO;
//        expected.setParentId( UUID.fromString( PARENT_ID_OF_WORKFLOWS ) );
//        ProjectEntity expectedEntity = projectEntity;
//        expectedEntity.setCreatedOn( CREATED_ON_DATE );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedEntity );
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO );
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() );
//
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//        ProjectDTO actual = manager.createSuSProject( DEFAULT_USER_ID, expected.toString() );
//        Assert.assertEquals( expected.getDescription(), actual.getDescription() );
//        Assert.assertEquals( expected.getId(), actual.getId() );
//        Assert.assertEquals( expected.getName(), actual.getName() );
//
//    }
//
//    /**
//     * Should throw SuS exception if provided project name is empty in create project.
//     */
//    @Test
//    public void shouldThrowSuSExceptionIfProvidedProjectNameIsEmptyInCreateProject() {
//
//        Notification notif = new Notification();
//        projectDTO.setName( EMPTY_STRING );
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        notif.addError(
//                new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ProjectDTO.PROJECT_NAME ) ) );
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( notif.getErrors().toString() );
//
//        ProjectDTO expected = projectDTO;
//        manager.createSuSProject( DEFAULT_USER_ID, expected.toString() );
//    }
//
//    /************************************
//     * CREATE OBJECT OPTIONS FORM ***************************************.
//     */
//
//    /**
//     * Should throw sus exception if object name already exists in parent.
//     */
//    @Test
//    public void shouldThrowSusExceptionIfObjectHasSameNameAsItsSiblings() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        ProjectEntity expectedEntity = fillProjectEntityOfTypeData();
//
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( expectedEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject() ) ).andReturn( Arrays.asList( expectedEntity ) ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//
//        DataObjectDTO objectDTO = fillDataObjectDTO();
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, UUID.randomUUID().toString(),
//                UUID.randomUUID().toString(), JsonUtils.toJson( objectDTO ), true, null );
//
//    }
//
//    /**
//     * ********************************** CREATE OBJECT FORM ***************************************.
//     */
//
//    /**
//     * Should throw exception when empty parent id and object type id is passed in create object form.
//     */
//    @Test
//    public void shouldThrowExceptionWhenEmptyParentIdAndObjectTypeIdIsPassedInCreateObjectForm() {
//
//        Notification notify = new Notification();
//
//        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_CANNOT_BE_EMPTY.getKey() ) ) );
//        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_ID_CANNOT_EMPTY.getKey() ) ) );
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( notify.getErrors().toString() );
//
//        manager.createObjectForm( DEFAULT_USER_ID, EMPTY_STR_UUID_PRENT_ID, EMPTY_STR_UUID_OBJECT_TYPE_ID );
//
//    }
//
//    /**
//     * Should throw exception when invalid parent id and object type id is passed in create object form.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInvalidParentIdAndObjectTypeIdIsPassedInCreateObjectForm() {
//
//        Notification notify = new Notification();
//
//        notify.addError(
//                new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), INVALID_STR_UUID_PRENT_ID ) ) );
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( notify.getErrors().toString() );
//
//        manager.createObjectForm( DEFAULT_USER_ID, INVALID_STR_UUID_PRENT_ID, INVALID_STR_UUID_OBJECT_TYPE_ID );
//
//    }
//
//    /**
//     * Should throw exception when in create object form parent with provided parent id does not exist in database.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInCreateObjectFormParentWithProvidedParentIdDoesNotExistInDatabase() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_FOUND.getKey() ) );
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
//        mockControl.replay();
//
//        manager.createObjectForm( DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID );
//    }
//
//    /**
//     * Should throw exception when get create object is called with object type not having valid class name in configurations.
//     */
//    @Test
//    public void shouldThrowExceptionWhenGetCreateObjectIsCalledWithObjectTypeNotHavingValidClassNameInConfigurations() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage(
//                MessageBundleFactory.getMessage( Messages.CLASS_NOT_FOUND_OR_NOT_ABLE_TO_INITIALIZE.getKey(), IN_VALID_CLASS_NAME ) );
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        model.setClassName( IN_VALID_CLASS_NAME );
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//
//        manager.createObjectForm( DEFAULT_USER_ID, PROJECT_ID.toString(), STR_UUID_OBJECT_TYPE_ID );
//    }
//
//    /**
//     * Should throw sus exception when create object form is called and parent id is of project type label.
//     */
//    @Test
//    public void shouldThrowSusExceptionWhenCreateObjectFormIsCalledAndParentIdIsOfProjectTypeLabel() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PROJECT_TYPE_LABLE_CANT_CONTAINS_DATA.getKey() ) );
//
//        ProjectEntity expectedEntity = fillProjectEntityOfTypeData();
//        expectedEntity.setType( ProjectType.LABEL.getKey() );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedEntity ).anyTimes();
//        mockControl.replay();
//
//        manager.createObjectForm( DEFAULT_USER_ID, PROJECT_ID.toString(), STR_UUID_OBJECT_TYPE_ID );
//
//    }
//
//    /**
//     * Should successfully get create object form filled when parent id is not of project type label.
//     */
//    @Test
//    public void shouldSuccessfullyGetCreateObjectFormFilledWhenParentIdIsNotOfProjectTypeLabel() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        List< UIFormItem > expected = prepareCreateDataObjectForm();
//        expected.addAll( convertJSONtoFormItems( model.getCustomAttributes() ) );
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//
//    }
//
//    /**
//     * ********************************* Delete Objects *******************************.
//     */
//
//    /**
//     * Should successfully delete object when delete object versions and childs with valid id is going to delete.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteObjectWhenDeleteObjectVersionsAndChildsWithValidIdIsGoingToDelete() throws Exception {
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        getPermittedTrue();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( job );
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//    }
//
//    /**
//     * Should successfully delete object and child when delete object versions and childs with valid id having child is going to delete.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteObjectAndChildWhenDeleteObjectVersionsAndChildsWithValidIdHavingChildIsGoingToDelete()
//            throws Exception {
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                projectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                dataObjectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), dataObjectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        getPermittedTrue();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( job );
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//        Assert.assertFalse( dataObjectEntity.isDelete() );
//    }
//
//    /**
//     * Should successfully delete object when delete object versions and childs with valid id is going to delete.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteAllObjectWhenDeleteObjectVersionsAndChildsWithValidIdIsGoingToDelete() throws Exception {
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        List< UUID > uuids = new ArrayList<>();
//        uuids.add( UUID.randomUUID() );
//
//        EasyMock.expect(
//                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( uuids ).anyTimes();
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( job );
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//    }
//
//    /**
//     * Should successfully delete object and child when delete object versions and childs with valid id having child is going to delete.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteAllObjectAndChildWhenDeleteObjectVersionsAndChildsWithValidIdHavingChildIsGoingToDelete()
//            throws Exception {
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//        List< UUID > uuids = new ArrayList<>();
//        uuids.add( UUID.randomUUID() );
//
//        // EasyMock.expect( selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyString() ) ).andReturn( uuids ).anyTimes();
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                projectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                dataObjectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), dataObjectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        getPermittedTrue();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( job );
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//        Assert.assertFalse( dataObjectEntity.isDelete() );
//    }
//
//    /**
//     * Should successfully return status when jobis already completed stop job ececution on server.
//     */
//    @Test
//    public void shouldSuccessfullyReturnStatusWhenJobisAlreadyCompletedStopJobEcecutionOnServer() {
//        UUID id = UUID.randomUUID();
//        Job job = new JobImpl();
//        job.setId( id );
//        job.setLog( new ArrayList<>() );
//        job.setStatus( new Status( WorkflowStatus.COMPLETED.getKey(), WorkflowStatus.COMPLETED.getValue() ) );
//        boolean deleted = manager.stopJobExecution( DEFAULT_USER_ID, SIMUSPACE, id.toString(), null );
//        Assert.assertFalse( deleted );
//    }
//
//    /**
//     * Should successfully stop job ececution on server.
//     *
//     * @throws InterruptedException
//     *         the interrupted exception
//     */
//    @Test
//    public void shouldSuccessfullyStopJobEcecutionOnServer() throws InterruptedException {
//        UUID id = UUID.randomUUID();
//        Job job = new JobImpl();
//        job.setId( id );
//        job.setLog( new ArrayList<>() );
//        job.setWorkflowId( UUID.fromString( SYSTEM_WORKFLOW_ID ) );
//        job.setStatus( new Status( WorkflowStatus.RUNNING.getKey(), WorkflowStatus.RUNNING.getValue() ) );
//        EasyMock.expect( jobManager.getJob( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( job );
//        EasyMock.expect( jobManager.stopServerWorkFlow( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true );
//        EasyMock.expect( threadPoolExecutorService.stopJobExecution( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( true );
//        mockControl.replay();
//        boolean status = manager.stopJobExecution( DEFAULT_USER_ID, SIMUSPACE, id.toString(), null );
//        Assert.assertTrue( status );
//    }
//
//    /**
//     * Should return false status if thread map is empty.
//     */
//    @Test
//    public void shouldReturnFalseStatusIfThreadMapIsEmpty() {
//
//        boolean deleted = manager.stopJobExecution( DEFAULT_USER_ID, SIMUSPACE, UUID.randomUUID().toString(), null );
//        Assert.assertFalse( deleted );
//    }
//
//    /**
//     * Should successfully delete object when valid id is going to delete.
//     */
//    @Test
//    public void shouldSuccessfullyDeleteObjectWhenValidIdIsGoingToDelete() {
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        Job job = new JobImpl();
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( job );
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//        boolean deleted = manager.deleteObjectAndDependencies( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID,
//                projectEntity.getComposedId().getId(), job );
//        Assert.assertTrue( deleted );
//    }
//
//    /**
//     * Should successfully delete object and dependencies when valid id is going to delete.
//     */
//    @Test
//    public void shouldSuccessfullyDeleteObjectAndDependenciesWhenValidIdIsGoingToDelete() {
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                projectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( projectEntity ) ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserCommonDAO() ).andReturn( userCommonDAO ).anyTimes();
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                dataObjectEntity.getComposedId().getId() ) ).andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), dataObjectEntity ) ).andReturn( new ArrayList<>() )
//                .anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        Job job = new JobImpl();
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( job );
//        mockControl.replay();
//        Assert.assertFalse( projectEntity.isDelete() );
//        Assert.assertFalse( dataObjectEntity.isDelete() );
//        boolean deleted = manager.deleteObjectAndDependencies( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID,
//                projectEntity.getComposedId().getId(), job );
//        Assert.assertTrue( deleted );
//    }
//
//    /**
//     * ********************************** CREATE OBJECT ***************************************.
//     */
//
//    /**
//     * Should throw sus exception if object is going to create in project of type label.
//     */
//    @Test
//    public void shouldThrowSusExceptionIfObjectIsGoingToCreateInProjectOfTypeLabel() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PROJECT_TYPE_LABLE_CANT_CONTAINS_DATA.getKey() ) );
//
//        ProjectEntity expectedEntity = fillProjectEntityOfTypeData();
//        expectedEntity.setType( ProjectType.LABEL.getKey() );
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( expectedEntity ).anyTimes();
//        mockControl.replay();
//
//        DataObjectDTO objectDTO = fillDataObjectDTO();
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID,
//                JsonUtils.toJson( objectDTO ), true, null );
//
//    }
//
//    /**
//     * Should create object if object is going to create in project of type data with valid inputs. with valid inputs.
//     */
//    @Test
//    public void shouldCreateObjectIfObjectIsGoingToCreateInProjectOfTypeDataWithValidInputs() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        DataObjectDTO expectedObjectDto = fillDataObjectDTO();
//        DataObjectEntity expectedObjectEnatity = expectedObjectDto.prepareEntity( DEFAULT_USER_ID );
//        expectedObjectDto.setId( expectedObjectEnatity.getComposedId().getId() );
//
//        EasyMock.expect( configManager.getProjectConfiguration( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new ProjectConfiguration() ).anyTimes();
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectEnatity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO );
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() );
//
//        EasyMock.expect(
//                        userCommonManager.getUserEntityById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( new UserEntity() );
//
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( EasyMock.anyObject( EntityManager.class ),
//                DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID, JsonUtils.toJson( expectedObjectDto ), true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * Should create object by super user if object is going to create in project of type data with valid inputs.
//     */
//    @Test
//    public void shouldCreateObjectBySuperUserIfObjectIsGoingToCreateInProjectOfTypeDataWithValidInputs() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        DataObjectDTO expectedObjectDto = fillDataObjectDTO();
//        DataObjectEntity expectedObjectEnatity = expectedObjectDto.prepareEntity( SUPER_USER_ID );
//        expectedObjectDto.setId( expectedObjectEnatity.getComposedId().getId() );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectEnatity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO );
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( EasyMock.anyObject( EntityManager.class ),
//                SUPER_USER_ID_FROM_FILE, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID, JsonUtils.toJson( expectedObjectDto ), true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * Should create object with super user if object is going to create in project of type data with valid inputs.
//     */
//    @Test
//    public void shouldCreateObjectWithSuperUserIfObjectIsGoingToCreateInProjectOfTypeDataWithValidInputs() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//
//        DataObjectDTO expectedObjectDto = fillDataObjectDTO();
//        DataObjectEntity expectedObjectEnatity = expectedObjectDto.prepareEntity( DEFAULT_USER_ID );
//        expectedObjectDto.setId( expectedObjectEnatity.getComposedId().getId() );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectEnatity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() );
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( EasyMock.anyObject( EntityManager.class ),
//                SUPER_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID, JsonUtils.toJson( expectedObjectDto ), true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * Should not create object if null object is provided to creat object.
//     */
//    @Test
//    public void shouldNotCreateObjectIfNullObjectIsProvidedToCreatObject() {
//
//        DataObjectDTO expectedObjectDto = null;
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( DEFAULT_USER_ID, STR_UUID_PRENT_ID,
//                STR_UUID_OBJECT_TYPE_ID, null, true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * ********************************** CREATE OBJECT ***************************************.
//     */
//
//    /**
//     * Should throw exception when empty parent id and object type id is passed in create object.
//     */
//    @Test
//    public void shouldThrowExceptionWhenEmptyParentIdAndObjectTypeIdIsPassedInCreateObject() {
//
//        Notification notify = new Notification();
//
//        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_CANNOT_BE_EMPTY.getKey() ) ) );
//        notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_ID_CANNOT_EMPTY.getKey() ) ) );
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( notify.getErrors().toString() );
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, EMPTY_STR_UUID_PRENT_ID,
//                EMPTY_STR_UUID_OBJECT_TYPE_ID, EMPTY_JSON_OBJECT_STR, true, null );
//
//    }
//
//    /**
//     * Should throw exception when invalid parent id and object type id is passed in create object.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInvalidParentIdAndObjectTypeIdIsPassedInCreateObject() {
//
//        Notification notify = new Notification();
//
//        notify.addError(
//                new Error( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), INVALID_STR_UUID_PRENT_ID ) ) );
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( notify.getErrors().toString() );
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, INVALID_STR_UUID_PRENT_ID,
//                INVALID_STR_UUID_OBJECT_TYPE_ID, EMPTY_JSON_OBJECT_STR, true, null );
//
//    }
//
//    /**
//     * Should throw exception when in create object parent with provided parent id does not exist in database.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInCreateObjectParentWithProvidedParentIdDoesNotExistInDatabase() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_FOUND.getKey() ) );
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( null ).anyTimes();
//        mockControl.replay();
//
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID,
//                EMPTY_JSON_OBJECT_STR, true, null );
//    }
//
//    /**
//     * Should throw exception when in create object parent with provided parent id is not of type container.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInCreateObjectParentWithProvidedParentIdIsNotOfTypeContainer() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage(
//                MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_PARENT_IS_NOT_OF_TYPE_CONTAINER.getKey() ) );
//
//        SuSEntity datobjectEntity = new DataObjectEntity();
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( datobjectEntity ).anyTimes();
//        mockControl.replay();
//
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID,
//                EMPTY_JSON_OBJECT_STR, true, null );
//    }
//
//    /**
//     * Should throw exception when in create object object type with provided type id does not exist in configurations.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInCreateObjectObjectTypeWithProvidedTypeIdDoesNotExistInConfigurations() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_OBJECT_TYPE_IS_NOT_FONUND_IN_CONFIG.getKey(),
//                STR_UUID_OBJECT_TYPE_ID, PROJECT_CONFIG_FILE_NAME ) );
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        SuSObjectModel model = null;
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model );
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( null )
//                .anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new JobImpl() );
//        mockControl.replay();
//
//        manager.createSuSObject( EasyMock.anyObject( EntityManager.class ), DEFAULT_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID,
//                EMPTY_JSON_OBJECT_STR, true, null );
//    }
//
//    /**
//     * ******************************* getDataObjectVersionsUI ***********************************.
//     */
//
//    /**
//     * Should Successfully Get Versions View When Valid DataObject Id Is Given
//     */
//    @Test
//    public void shouldSuccessfullyGetVersionsViewWhenValidDataObjectIdIsGiven() {
//
//        SuSObjectModel model = getFilledSuSModelObject();
//        List< TableColumn > expected = prepareDataObjectTableUI( model.getCustomAttributes() );
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity() ).anyTimes();
//        mockControl.replay();
//        List< TableColumn > actual = manager.getDataObjectVersionsUI( DATA_OBJECT_ID.toString() );
//        for ( TableColumn actualTableColumn : actual ) {
//            for ( TableColumn expectedTableColumn : expected ) {
//                if ( expectedTableColumn.getName().equals( actualTableColumn.getName() ) ) {
//                    Assert.assertEquals( expectedTableColumn.getData(), actualTableColumn.getData() );
//                }
//            }
//
//        }
//    }
//
//    /**
//     * ******************************* getDataObjectByIdAndVersion ***********************************.
//     */
//
//    /**
//     * Should Successfully Get DataObject By Id And Version When Valid DataObject Id Is Given As Input Parameter
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectByIdAndVersionWhenValidDataObjectIdIsGivenAsInputParameter() {
//        SuSEntity expected = fillDataObjectEntity();
//
//        EasyMock.expect(
//                        dao.getObjectByCompositeKey( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andReturn( expected ).anyTimes();
//        mockControl.replay();
//
//        VersionPrimaryKey expectedkey = new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID );
//        DataObjectDTO actual = ( DataObjectDTO ) manager.getObjectByIdAndVersion( DEFAULT_USER_ID, expectedkey );
//        Assert.assertEquals( expected.getComposedId().getId(), actual.getId() );
//        Assert.assertEquals( expected.getComposedId().getVersionId(), actual.getVersion().getId() );
//        Assert.assertEquals( expected.getName(), actual.getName() );
//
//    }
//
//    /**
//     * Should Failed To Get DataObject By Id And Version When InValid DataObject Id Is Given As Input Parameter.
//     */
//    @Test
//    public void shouldFailedToGetDataObjectByIdAndVersionWhenInvalidDataObjectIdIsGivenAsInputParameter() {
//        SuSEntity expected = null;
//        EasyMock.expect(
//                        dao.getObjectByCompositeKey( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( VersionPrimaryKey.class ) ) )
//                .andReturn( expected ).anyTimes();
//        mockControl.replay();
//
//        VersionPrimaryKey expectedkey = new VersionPrimaryKey( null, DEFAULT_VERSION_ID );
//        DataObjectDTO actual = ( DataObjectDTO ) manager.getObjectByIdAndVersion( DEFAULT_USER_ID, expectedkey );
//        Assert.assertEquals( expected, actual );
//
//    }
//
//    /**
//     * ******************************* getDataObjectVersions ***********************************.
//     */
//
//    @Test
//    public void shouldSuccessfullyGetFilteredListOfDataObjectVersionsWhenValidObjectIdIsGiven() {
//        FiltersDTO expectedFilter = fillFilterForDataTable();
//        List< SuSEntity > expectedList = new ArrayList<>();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        expectedList.add( fillDataObjectEntity );
//
//        EasyMock.expect( dao.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//        FilteredResponse< Object > actual = manager.getObjectVersions( SUPER_USER_ID, DATA_OBJECT_ID, expectedFilter );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedList.size(), actual.getData().size() );
//        Assert.assertEquals( expectedFilter.getDraw(), actual.getDraw() );
//        DataObjectDTO actualObjectDTO = ( DataObjectDTO ) actual.getData().get( FIRST_INDEX );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getId(), actualObjectDTO.getId() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getVersionId(), actualObjectDTO.getVersion().getId() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualObjectDTO.getName() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getCreatedOn(), actualObjectDTO.getCreatedOn() );
//    }
//
//    /**
//     * Should successfully get filtered list of library versions when valid object id is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetFilteredListOfLibraryVersionsWhenValidObjectIdIsGiven() {
//        FiltersDTO expectedFilter = fillFilterForDataTable();
//        List< SuSEntity > expectedList = new ArrayList<>();
//        LibraryEntity fillLibraryEntity = fillLibraryEntity();
//        expectedList.add( fillLibraryEntity );
//
//        EasyMock.expect( dao.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        LibraryEntity expected = fillLibraryEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//        FilteredResponse< Object > actual = manager.getObjectVersions( SUPER_USER_ID, DATA_OBJECT_ID, expectedFilter );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedList.size(), actual.getData().size() );
//        Assert.assertEquals( expectedFilter.getDraw(), actual.getDraw() );
//        LibraryDTO actualObjectDTO = ( LibraryDTO ) actual.getData().get( FIRST_INDEX );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getId(), actualObjectDTO.getId() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualObjectDTO.getName() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getCreatedOn(), actualObjectDTO.getCreatedOn() );
//    }
//
//    /**
//     * Should successfully get filtered list of variant versions when valid object id is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetFilteredListOfVariantVersionsWhenValidObjectIdIsGiven() {
//        FiltersDTO expectedFilter = fillFilterForDataTable();
//        List< SuSEntity > expectedList = new ArrayList<>();
//        VariantEntity fillVariantEntity = fillVariantEntity();
//        expectedList.add( fillVariantEntity );
//
//        EasyMock.expect( dao.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        VariantEntity expected = fillVariantEntity();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        mockControl.replay();
//        FilteredResponse< Object > actual = manager.getObjectVersions( SUPER_USER_ID, DATA_OBJECT_ID, expectedFilter );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedList.size(), actual.getData().size() );
//        Assert.assertEquals( expectedFilter.getDraw(), actual.getDraw() );
//        VariantDTO actualObjectDTO = ( VariantDTO ) actual.getData().get( FIRST_INDEX );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getId(), actualObjectDTO.getId() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualObjectDTO.getName() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getCreatedOn(), actualObjectDTO.getCreatedOn() );
//    }
//
//    /**
//     * Should successfully get filtered list of project versions when valid object id is given.
//     */
//    @Test
//    public void shouldSuccessfullyGetFilteredListOfProjectVersionsWhenValidObjectIdIsGiven() {
//        FiltersDTO expectedFilter = fillFilterForDataTable();
//        List< SuSEntity > expectedList = new ArrayList<>();
//        ProjectEntity fillProjectEntity = fillProjectEntityOfTypeData();
//        expectedList.add( fillProjectEntity );
//
//        EasyMock.expect( dao.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        ProjectEntity expected = fillProjectEntityOfTypeData();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//        FilteredResponse< Object > actual = manager.getObjectVersions( SUPER_USER_ID, DATA_OBJECT_ID, expectedFilter );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedList.size(), actual.getData().size() );
//        Assert.assertEquals( expectedFilter.getDraw(), actual.getDraw() );
//        ProjectDTO actualObjectDTO = ( ProjectDTO ) actual.getData().get( FIRST_INDEX );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getComposedId().getId(), actualObjectDTO.getId() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getName(), actualObjectDTO.getName() );
//        Assert.assertEquals( expectedList.get( FIRST_INDEX ).getCreatedOn(), actualObjectDTO.getCreatedOn() );
//    }
//
//    /**
//     * Should Get Empty Filtered List Of DataObject Versions When Dao Returns Null As List.
//     */
//    @Test
//    public void shouldGetEmptyFilteredListOfDataObjectVersionsWhenDaoReturnsNullAsList() {
//        FiltersDTO expectedFilter = fillFilterForDataTable();
//        List< SuSEntity > expectedList = new ArrayList<>();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        DataObjectEntity expected = fillDataObjectEntity();
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//
//        EasyMock.expect( dao.getAllFilteredVersionsById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( Class.class ),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ),
//                EasyMock.anyObject( List.class ), EasyMock.anyObject( List.class ) ) ).andReturn( expectedList ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() ).anyTimes();
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        mockControl.replay();
//        FilteredResponse< Object > actual = manager.getObjectVersions( SUPER_USER_ID, DATA_OBJECT_ID, expectedFilter );
//        Assert.assertNotNull( actual );
//        Assert.assertEquals( expectedList.size(), actual.getData().size() );
//
//    }
//
//    /**
//     * Should Throw Exception In Get Filtered List Of DataObject Versions When Input FilterDto Is Given Null.
//     */
//    @Test
//    public void shouldThrowExceptionInGetFilteredListOfDataObjectVersionsWhenInputFilterDtoIsGivenNull() {
//        FiltersDTO expectedFilter = null;
//        thrown.expect( SusException.class );
//        thrown.expectMessage( NO_OBJECTS_FOUND_ERROR_MSG );
//        manager.getObjectVersions( DEFAULT_USER_ID, DATA_OBJECT_ID, expectedFilter );
//    }
//
//    /**
//     * ************************** HELPER METHODS **************************.
//     *
//     * @return the filters DTO
//     */
//
//    /**
//     * Prepare filter For table.
//     *
//     * @return the filters DTO
//     */
//    private FiltersDTO fillFilterForDataTable() {
//        FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
//                ConstantsInteger.INTEGER_VALUE_TEN );
//        FilterColumn filterColumn = new FilterColumn();
//
//        filterColumn.setName( SuSEntity.FIELD_NAME_MODIFIED_ON );
//        filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
//        filtersDTO.setColumns( Arrays.asList( filterColumn ) );
//        filtersDTO.setFilteredRecords( ( long ) ConstantsInteger.INTEGER_VALUE_TEN );
//
//        return filtersDTO;
//    }
//
//    /**
//     * Should successfully add meta data and associate to object when valid object id and meta data are provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyAddMetaDataAndAssociateToObjectWhenValidObjectIdAndMetaDataAreProvided() throws Exception {
//        ObjectMetaDataDTO expected = prepareObjectMetaDataDTO();
//        DocumentDTO documentDTO = prepareDocumentDTO();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setFirstName( ConstantsString.SIMUSPACE );
//        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( userEntity );
//
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect(
//                        documentManager.saveDocument( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( documentDTO );
//        EasyMock.expect( documentManager.getDocumentDAO() ).andReturn( documentDAO );
//        EasyMock.expect(
//                        documentDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( documentEntity );
//        /*        EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( new File( documentEntity.getFileName() ) );*/
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( getUser() ).anyTimes();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        ObjectMetaDataDTO acual = manager.addMetaDataToAnObject( DATA_OBJECT_ID.toString(), expected, SUPER_USER_ID, false );
//        Assert.assertEquals( expected.getMetadata(), acual.getMetadata() );
//    }
//
//    /**
//     * Should successfully add meta data and associate to object when valid object id and meta data are provided with recursive life cycle
//     * status changes.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyAddMetaDataAndAssociateToObjectWhenValidObjectIdAndMetaDataAreProvidedWithRecursiveLifeCycleStatusChanges()
//            throws Exception {
//        ObjectMetaDataDTO expected = prepareObjectMetaDataDTO();
//        DocumentDTO documentDTO = prepareDocumentDTO();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setFirstName( ConstantsString.SIMUSPACE );
//        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( userEntity );
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//        getPermittedTrue();
//        mockAccessControlEntities();
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect(
//                        documentManager.saveDocument( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( documentDTO );
//        EasyMock.expect( documentManager.getDocumentDAO() ).andReturn( documentDAO );
//        EasyMock.expect(
//                        documentDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( documentEntity );
//
//        /*EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( new File( documentEntity.getFileName() ) );*/
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByVersionAndStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt(), EasyMock.anyString() ) ).andReturn( fillDataObjectEntity );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( getUser() ).anyTimes();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        ObjectMetaDataDTO acual = manager.addMetaDataToAnObject( DATA_OBJECT_ID.toString(), expected, SUPER_USER_ID, false );
//        Assert.assertEquals( expected.getMetadata(), acual.getMetadata() );
//    }
//
//    /**
//     * Should successfully get meta data list when valid object id is provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetMetaDataListWhenValidObjectIdIsProvided() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//        StatusConfigDTO detail = getStatusConfig();
//
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//        /*      EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( new File( documentEntity.getFileName() ) );*/
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( userDTO );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        mockControl.replay();
//
//        FiltersDTO filterDTO = new FiltersDTO();
//        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
//        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_TEN );
//        FilteredResponse< MetaDataEntryDTO > actual = manager.getObjectMetaDataList( DATA_OBJECT_ID.toString(), USER_ID.toString(),
//                filterDTO );
//        Assert.assertNotNull( actual.getData() );
//    }
//
//    /**
//     * Should not successfully add meta data to file and throw exception when invalid meta data keys are provided.
//     */
//    @Test
//    public void shouldNotSuccessfullyAddMetaDataToFileAndThrowExceptionWhenInvalidMetaDataKeysAreProvided() {
//        ObjectMetaDataDTO expected = prepareObjectMetaDataDTO();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        expected.getMetadata().get( ConstantsInteger.INTEGER_VALUE_ZERO ).setKey( null );
//        getPermittedTrue();
//        mockAccessControlEntities();
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() );
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), META_DATA_KEY ) );
//        manager.addMetaDataToAnObject( DATA_OBJECT_ID.toString(), expected, SUPER_USER_ID, true );
//    }
//
//    /**
//     * Should successfully get object meta data UI columns.
//     */
//    @Test
//    public void shouldSuccessfullyGetObjectMetaDataUIColumns() {
//        List< TableColumn > expected = GUIUtils.listColumns( MetaDataEntryDTO.class );
//        List< TableColumn > actual = manager.getObjectMetaDataTableUI( OBJECT_ID.toString() );
//        Assert.assertEquals( expected.size(), actual.size() );
//    }
//
//    /**
//     * Should successfully delete meta data entry from object when valid object id and key is provided for bulk delete mode.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteMetaDataEntryFromObjectWhenValidObjectIdAndKeyIsProvidedForBulkDeleteMode() throws Exception {
//        DocumentDTO documentDTO = prepareDocumentDTO();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        statusConfigDTO.setVisible( ALL_VISIBILITY );
//
//        EasyMock.expect(
//                        dao.getLatestNonDeletedActiveObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( fillDataObjectEntity );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect(
//                        documentManager.saveDocument( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( documentDTO );
//        EasyMock.expect( documentManager.getDocumentDAO() ).andReturn( documentDAO );
//        EasyMock.expect(
//                        documentDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( documentEntity );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect(
//                        contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ) ) )
//                .andReturn( getFilterDTOForDeletingMetaDataBySelection() ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( userDTO ).anyTimes();
//
//        getPermittedTrue();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockAccessControlEntities();
//        mockControl.replay();
//        boolean status = manager.deleteMetaDataBySelection( USER_ID.toString(), OBJECT_ID.toString(),
//                CONTEXT_SELECTION_KEY_FOR_DELETING_META_DATA, ConstantsMode.BULK );
//        Assert.assertTrue( status );
//    }
//
//    /**
//     * Should successfully delete meta data entry from object when valid object id and key is provided for Single delete mode.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyDeleteMetaDataEntryFromObjectWhenValidObjectIdAndKeyIsProvidedForSingleDeleteMode() throws Exception {
//        DocumentDTO documentDTO = prepareDocumentDTO();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setVisible( ALL_VISIBILITY );
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect(
//                        documentManager.saveDocument( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( documentDTO );
//        EasyMock.expect( documentManager.getDocumentDAO() ).andReturn( documentDAO );
//        EasyMock.expect(
//                        documentDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( documentEntity );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect(
//                        contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ) ) )
//                .andReturn( getFilterDTOForDeletingMetaDataBySelection() ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( userDTO ).anyTimes();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        mockControl.replay();
//        boolean status = manager.deleteMetaDataBySelection( USER_ID.toString(), OBJECT_ID.toString(),
//                CONTEXT_SELECTION_KEY_FOR_DELETING_META_DATA, ConstantsMode.SINGLE );
//        Assert.assertTrue( status );
//    }
//
//    /**
//     * Should not delete meta data entry and throw exception when provided delete mode is not supported.
//     */
//    @Test
//    public void shouldNotDeleteMetaDataEntryAndThrowExceptionWhenProvidedDeleteModeIsNotSupported() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), ConstantsString.EMPTY_STRING ) );
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        StatusConfigDTO detail = getStatusConfig();
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( userDTO );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        boolean status = manager.deleteMetaDataBySelection( USER_ID.toString(), OBJECT_ID.toString(),
//                CONTEXT_SELECTION_KEY_FOR_DELETING_META_DATA, ConstantsString.EMPTY_STRING );
//        Assert.assertTrue( status );
//    }
//
//    /**
//     * Should successfully prepare edit form for meta data when valid object id and meta data key value are provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyPrepareEditFormForMetaDataWhenValidObjectIdAndMetaDataKeyValueAreProvided() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        mockStaticMethodOfJsonUtilsClass();
//        mockStaticMethodsOfFileUtilsClass();
//
//        /*        EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) )
//                .andReturn( new File( documentEntity.getFileName() ) );*/
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() );
//        getPermittedTrue();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockAccessControlEntities();
//        mockControl.replay();
//    }
//
//    /**
//     * Should not successfully prepare edit form for meta data and return empty list when in valid object id is provided.
//     */
//    @Test
//    public void shouldNotSuccessfullyPrepareEditFormForMetaDataAndReturnEmptyListWhenInValidObjectIdIsProvided() {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        fillDataObjectEntity.setMetaDataDocument( documentEntity );
//        documentEntity.setMetaDataDocument( documentEntity );
//        StatusConfigDTO detail = getStatusConfig();
//        UserDTO userDTO = prepareUserDTO( USER, NOT_RESTRICTED );
//
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        // List< UIFormItem > actualUIFormItemList = manager.prepareMetaDataFormForEdit( USER_ID.toString(), DATA_OBJECT_ID.toString(),
//        // META_DATA_KEY );
//        // Assert.assertTrue( actualUIFormItemList.isEmpty() );
//    }
//
//    /**
//     * Should successfully prepare add form for meta data.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyPrepareAddFormForMetaData() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( getUser() );
//        getPermittedTrue();
//        mockAccessControlEntities();
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl.replay();
//        // List< UIFormItem > actualUIFormItemList = manager.prepareMetaDataFormForCreate( SUPER_USER_ID, DATA_OBJECT_ID.toString() );
//        // Assert.assertNotNull( actualUIFormItemList );
//    }
//
//    /**
//     * Should show manage permission object columns when api is called.
//     */
//    @Test
//    public void shouldShowManagePermissionObjectColumnsWhenApiIsCalled() {
//        TableUI expectedUI = getFilledColumns();
//        EasyMock.expect( permissionManager.extractColumnList( EasyMock.anyObject() ) ).andReturn( expectedUI.getColumns() ).anyTimes();
//        mockControl.replay();
//        TableUI actualTableUI = manager.objectPermissionTableUI( USER_ID.toString() );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getData(),
//                actualTableUI.getColumns().get( FIRST_INDEX ).getData() );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getFilter(),
//                actualTableUI.getColumns().get( FIRST_INDEX ).getFilter() );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getName(),
//                actualTableUI.getColumns().get( FIRST_INDEX ).getName() );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getTitle(),
//                MessageBundleFactory.getMessage( Messages.DTO_UI_TITLE_NAME.getKey() ) );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getData(),
//                actualTableUI.getColumns().get( FIRST_INDEX ).getRenderer().getData() );
//        Assert.assertEquals( expectedUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage(),
//                actualTableUI.getColumns().get( FIRST_INDEX ).getRenderer().getManage() );
//    }
//
//    /**
//     * ******************************** Sync Files *****************************************.
//     *
//     * @throws Exception
//     *             the exception
//     */
//
//    /**
//     * Should success fully get tabs view container UI when container tabs exists in config.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessFullyGetTabsViewContainerUIWhenContainerTabsExistsInConfig() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( fillDataObjectEntity );
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        mockControl().replay();
//        List< String > expected = getGeneralTabsListAsString();
//
//        List< SubTabsUI > actual = manager.getTabsViewContainerUI( SUPER_USER_ID.toString(), DATA_OBJECT_ID.toString() ).getTabs();
//        Assert.assertFalse( actual.isEmpty() );
//        for ( SubTabsUI subTabsUI : actual ) {
//            Assert.assertTrue( expected.contains( subTabsUI.getName() ) );
//        }
//    }
//
//    /**
//     * Should return sync context menu when valid context is returned by context manager.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldReturnSyncContextMenuWhenValidContextIsReturnedByContextManager() throws Exception {
//
//        FiltersDTO Selection = new FiltersDTO();
//        Selection.setItems( Arrays.asList( UUID.randomUUID().toString() ) );
//        List< ContextMenuItem > expectedContext = fillExpectedContextMenuItems( STR_UUID_PRENT_ID );
//        EasyMock.expect( contextMenuManager.getSyncContextMenu( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyInt() ) ).andReturn( expectedContext )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, DATA_OBJECT_ID ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//        mockControl.replay();
//        List< ContextMenuItem > actualContextMenuItems = manager.getSyncContextRouter( STR_UUID_PRENT_ID, Selection );
//        Assert.assertFalse( actualContextMenuItems.isEmpty() );
//        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
//        Assert.assertEquals( expectedContext, actualContextMenuItems );
//    }
//
//    /**
//     * Should return data context router menu when valid context is returned by context manager.
//     */
//    @Test
//    public void shouldReturnDataContextRouterMenuWhenValidContextIsReturnedByContextManager() {
//        DataObjectEntity expected = fillDataObjectEntity();
//        expected.setConfig( PROJECT_CONFIG_FILE_NAME );
//        expected.setCheckedOut( false );
//        List< SuSEntity > expectedList = new ArrayList<>();
//        expectedList.add( expected );
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setUnique( false );
//        SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
//        selectionResponseUI.setId( SUPER_USER_ID );
//        FiltersDTO expectedSelection = new FiltersDTO();
//        expectedSelection.setItems( Arrays.asList( DATA_OBJECT_ID ) );
//        List< ContextMenuItem > expectedContext = fillExpectedContextMenuItems( STR_UUID_PRENT_ID );
//
//        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
//                .andReturn( selectionResponseUI ).anyTimes();
//        EasyMock.expect( contextMenuManager.getSyncContextMenu( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyInt() ) ).andReturn( expectedContext )
//                .anyTimes();
//
//        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( expected ) ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect( dataDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//        EasyMock.expect( dao.getAllVersionsOfObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( expectedList )
//                .anyTimes();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( fillDataObjectEntity() ) )
//                .anyTimes();
//        prepareMockGetDataObjectMethods( expected );
//        mockControl.replay();
//        List< ContextMenuItem > actualContextMenuItems = manager.getDataContextRouter( SUPER_USER_ID, STR_UUID_PRENT_ID, expectedSelection,
//                ConstantsString.EMPTY_STRING );
//        Assert.assertFalse( actualContextMenuItems.isEmpty() );
//        Assert.assertEquals( expectedContext.size(), actualContextMenuItems.size() );
//        Assert.assertEquals( expectedContext, actualContextMenuItems );
//    }
//
//    /**
//     * Should return bulk data context router menu when valid context is returned by context manager.
//     */
//    @Test
//    public void shouldReturnBulkDataContextRouterMenuWhenValidContextIsReturnedByContextManager() {
//        SuSEntity entity1 = fillSuSEntityWithDataObjectEntity( DATA_OBJECT_ID );
//        SuSEntity entity2 = fillSuSEntityWithDataObjectEntity( ENTRY_ID );
//        List< SuSEntity > expectedList = new ArrayList<>();
//        expectedList.add( entity1 );
//        expectedList.add( entity2 );
//        SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
//        selectionResponseUI.setId( SUPER_USER_ID );
//        FiltersDTO expectedSelection = new FiltersDTO();
//        List< Object > filterList = new ArrayList<>();
//        filterList.add( DATA_OBJECT_ID );
//        filterList.add( ENTRY_ID );
//        expectedSelection.setItems( filterList );
//        DataObjectEntity expected = fillDataObjectEntity();
//        expected.setConfig( PROJECT_CONFIG_FILE_NAME );
//        expected.setCheckedOut( false );
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setUnique( false );
//
//        List< ContextMenuItem > expectedContext = fillExpectedContextMenuItems( STR_UUID_PRENT_ID );
//        List< ContextMenuItem > expectedSyncContext = fillExpectedSyncContextMenuItems( STR_UUID_PRENT_ID );
//
//        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
//                .andReturn( selectionResponseUI ).anyTimes();
//
//        EasyMock.expect( contextMenuManager.getDataContextMenuBulk( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
//                EasyMock.anyObject(), EasyMock.anyBoolean() ) ).andReturn( expectedContext ).anyTimes();
//
//        EasyMock.expect( contextMenuManager.getSyncContextMenu( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyInt() ) ).andReturn( expectedSyncContext )
//                .anyTimes();
//
//        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedList ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect( dataDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//        EasyMock.expect( dao.getAllVersionsOfObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( entity1 );
//        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( entity2 );
//
//        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) )
//                .andReturn( Arrays.asList( "553536c7-71ec-409d-8f48-ec779a98a68e", "d762f4ef-e706-4a44-a46d-6b334745e2e5",
//                        "29d94aa2-62f2-4add-9233-2f4781545c35" ) )
//                .anyTimes();
//        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) )
//                .andReturn( Arrays.asList( "d762f4ef-e706-4a44-a46d-6b334745e2e5", "29d94aa2-62f2-4add-9233-2f4781545c35" ) ).anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity(), fillSecondDataObjectEntity() ) ).anyTimes();
//        mockControl.replay();
//        List< ContextMenuItem > actualContextMenuItems = manager.getDataContextRouter( EasyMock.anyObject( EntityManager.class ),
//                SUPER_USER_ID, STR_UUID_PRENT_ID, expectedSelection, ConstantsString.EMPTY_STRING );
//        Assert.assertFalse( actualContextMenuItems.isEmpty() );
//        Assert.assertEquals( expectedSyncContext.size(), actualContextMenuItems.size() );
//        Assert.assertEquals( expectedSyncContext, actualContextMenuItems );
//    }
//
//    /**
//     * Should return empty context router menu when local and sync both selected.
//     */
//    @Test
//    public void shouldReturnEmptyContextRouterMenuWhenLocalAndSyncBothSelected() {
//        SuSEntity entity1 = fillSuSEntityWithDataObjectEntity( DATA_OBJECT_ID );
//        SuSEntity entity2 = fillSuSEntityWithDataObjectEntity( ENTRY_ID );
//        List< SuSEntity > expectedList = new ArrayList<>();
//        expectedList.add( entity1 );
//        expectedList.add( entity2 );
//        SelectionResponseUI selectionResponseUI = new SelectionResponseUI();
//        selectionResponseUI.setId( SUPER_USER_ID );
//        FiltersDTO expectedSelection = new FiltersDTO();
//        List< Object > filterList = new ArrayList<>();
//        filterList.add( DATA_OBJECT_ID );
//        filterList.add( ENTRY_ID );
//        expectedSelection.setItems( filterList );
//        DataObjectEntity expected = fillDataObjectEntity();
//        expected.setConfig( PROJECT_CONFIG_FILE_NAME );
//        expected.setCheckedOut( false );
//        StatusConfigDTO statusConfigDTO = getStatusConfig();
//        statusConfigDTO.setUnique( false );
//
//        List< ContextMenuItem > expectedContext = fillExpectedContextMenuItems( STR_UUID_PRENT_ID );
//
//        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( selectionResponseUI ).anyTimes();
//
//        EasyMock.expect( contextMenuManager.getDataContextMenu( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyString(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyBoolean(), EasyMock.anyBoolean(),
//                EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( expectedContext ).anyTimes();
//        EasyMock.expect( contextMenuManager.getDataContextMenuBulk( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(),
//                EasyMock.anyObject(), EasyMock.anyBoolean() ) ).andReturn( expectedContext ).anyTimes();
//        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( expected ) ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( statusConfigDTO ).anyTimes();
//        EasyMock.expect( dataDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//        EasyMock.expect( dao.getAllVersionsOfObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) ).andReturn( Arrays.asList( fillDataObjectEntity() ) )
//                .anyTimes();
//        prepareMockGetDataObjectMethods( expected );
//        mockControl.replay();
//        List< ContextMenuItem > actualContextMenuItems = manager.getDataContextRouter( SUPER_USER_ID, STR_UUID_PRENT_ID, expectedSelection,
//                ConstantsString.EMPTY_STRING );
//        Assert.assertTrue( actualContextMenuItems.isEmpty() );
//    }
//
//    /**
//     * Should successfullay add file to an object when valid object id and document is provided.
//     */
//    @Test
//    public void shouldSuccessfullayAddFileToAnObjectWhenValidObjectIdAndDocumentIsProvided() {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentDTO expectedDocumentDTO = new DocumentDTO();
//        expectedDocumentDTO.setId( DOCUMENT_ID.toString() );
//        StatusConfigDTO detail = new StatusConfigDTO();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//        detail.setUnique( false );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DataObjectEntity.class, DATA_OBJECT_ID ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//
//        DocumentEntity documentEntity = new DocumentEntity();
//        documentEntity.setFileType( ConstantsString.EMPTY_STRING );
//        // documentEntity.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        // EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DocumentEntity.class, DOCUMENT_ID ) )
//        // .andReturn( documentEntity ).anyTimes();
//        dataObjectEntity.setFile( documentEntity );
//        dataObjectEntity.setOwner( new UserEntity( USER_ID ) );
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expectedDocumentDTO ).anyTimes();
//
//        mockControl.replay();
//        DocumentDTO documentDTO = new DocumentDTO();
//        documentDTO.setId( DOCUMENT_ID.toString() );
//        documentDTO.setType( VIDEO_MP4 );
//
//        Map< String, DocumentDTO > map = new HashMap< String, DocumentDTO >();
//        map.put( "file", documentDTO );
//
//        DataObjectDTO actualObjectDTO = ( DataObjectDTO ) manager.addFileToAnObject( DATA_OBJECT_ID.toString(), null, map, SUPER_USER_ID );
//        Assert.assertNotNull( actualObjectDTO.getFile() );
//
//    }
//
//    /**
//     * Should successfully add file to an object and create file image preview when valid object id and document is provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyAddFileToAnObjectAndCreateFileImagePreviewWhenValidObjectIdAndDocumentIsProvided() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentDTO expectedDocumentDTO = new DocumentDTO();
//        expectedDocumentDTO.setId( DOCUMENT_ID.toString() );
//        expectedDocumentDTO.setPath( TEST_FILE_NAME );
//        expectedDocumentDTO.setName( TEST_FILE_NAME );
//        StatusConfigDTO detail = new StatusConfigDTO();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//        detail.setUnique( false );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DataObjectEntity.class, DATA_OBJECT_ID ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//        DocumentEntity documentEntity = new DocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        documentEntity.setFileExt( ConstantsString.DOT + ConstantsFileExtension.JPEG );
//        // documentEntity.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        // mockStaticMethodOfCommonUtilsClass();
//        // EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DocumentEntity.class, DOCUMENT_ID ) )
//        // .andReturn( documentEntity ).anyTimes();
//        dataObjectEntity.setFile( documentEntity );
//        dataObjectEntity.setOwner( new UserEntity( USER_ID ) );
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expectedDocumentDTO ).anyTimes();
//        mockControl.replay();
//        Map< String, DocumentDTO > map = new HashMap< String, DocumentDTO >();
//        map.put( "file", expectedDocumentDTO );
//
//        DataObjectDTO actualObjectDTO = ( DataObjectDTO ) manager.addFileToAnObject( DATA_OBJECT_ID.toString(), null, map, SUPER_USER_ID );
//        Assert.assertEquals( actualObjectDTO.getFile(), expectedDocumentDTO );
//
//    }
//
//    /**
//     * Should successfully add movie file to an object and create image preview when valid object id and document is provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyAddMovieFileToAnObjectAndCreateImagePreviewWhenValidObjectIdAndDocumentIsProvided() throws Exception {
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        DocumentDTO expectedDocumentDTO = new DocumentDTO();
//        expectedDocumentDTO.setId( DOCUMENT_ID.toString() );
//        expectedDocumentDTO.setPath( TEST_FILE_NAME );
//        expectedDocumentDTO.setName( TEST_FILE_NAME );
//        expectedDocumentDTO.setType( VIDEO_MP4 );
//        DataObjectMovieEntity movieEntity = fillDataObjectMovieEntity();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity ) ).anyTimes();
//        StatusConfigDTO detail = new StatusConfigDTO();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//        detail.setUnique( false );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail ).anyTimes();
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DataObjectEntity.class, DATA_OBJECT_ID ) )
//                .andReturn( movieEntity ).anyTimes();
//        DocumentEntity documentEntity = new DocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        documentEntity.setFileExt( ConstantsString.DOT + ConstantsFileExtension.JPEG );
//        // documentEntity.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        // mockStaticMethodOfCommonUtilsClass();
//        // mockStaticMethodOfPropertiesUtilClass();
//        // EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), DocumentEntity.class, DOCUMENT_ID ) )
//        // .andReturn( documentEntity ).anyTimes();
//
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) ).andReturn( movieEntity )
//                .anyTimes();
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expectedDocumentDTO ).anyTimes();
//        mockControl.replay();
//
//        Map< String, DocumentDTO > map = new HashMap< String, DocumentDTO >();
//        map.put( "file", expectedDocumentDTO );
//
//        DataObjectDTO actualObjectDTO = ( DataObjectDTO ) manager.addFileToAnObject( DATA_OBJECT_ID.toString(), null, map, SUPER_USER_ID );
//        Assert.assertEquals( actualObjectDTO.getFile(), expectedDocumentDTO );
//
//    }
//
//    /**
//     * Should successfully get all items to of A container when valid object id is provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetAllItemsToOfAContainerWhenValidObjectIdIsProvided() throws Exception {
//        List< SuSEntity > expectedList = Arrays.asList( fillSuSEntityWithDataObjectEntity() );
//        DocumentDTO expectedDocumentDTO = new DocumentDTO();
//        expectedDocumentDTO.setId( DOCUMENT_ID.toString() );
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        expected.setStatus( ConstantsStatus.ACTIVE );
//        expected.setRestricted( ConstantsStatus.ACTIVE );
//        expected.setChangable( true );
//        StatusConfigDTO detail = new StatusConfigDTO();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( manager.prepareUserEntityFromUserModel( expected ) );
//        fillDataObjectEntity.setCheckedOut( false );
//        prepareMockGetDataObjectMethods( fillDataObjectEntity );
//        EasyMock.expect(
//                        dataDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class, PROJECT_ID ) )
//                .andReturn( projectEntity ).anyTimes();
//        DocumentEntity documentEntity = new DocumentEntity();
//        documentEntity.setId( DATA_OBJECT_ID );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), projectEntity, null ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        dataObjectEntity.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        dataObjectEntity.setTypeId( OBJECT_TYPE_ID );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), SuSEntity.class,
//                dataObjectEntity.getComposedId().getId() ) ).andReturn( dataObjectEntity ).anyTimes();
//
//        EasyMock.expect( dao.getAllVersionsOfObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedList ).anyTimes();
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expectedDocumentDTO ).anyTimes();
//        EasyMock.expect(
//                        permissionManager.isReadable( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( dao.getAllRecordsWithParent( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getAllFilteredRecordsWithParentAndLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                        EasyMock.anyObject( FiltersDTO.class ), EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyObject(),
//                        EasyMock.anyObject(), EasyMock.anyInt(), EasyMock.anyObject() ) )
//                .andReturn( Arrays.asList( fillDataObjectEntity(), fillSecondDataObjectEntity() ) ).anyTimes();
//        mockControl.replay();
//        DocumentDTO documentDTO = new DocumentDTO();
//        documentDTO.setId( DOCUMENT_ID.toString() );
//        List< FileInfo > actualFileInfoList = manager.getAllItems( SUPER_USER_ID, PROJECT_ID );
//        Assert.assertFalse( actualFileInfoList.isEmpty() );
//
//    }
//
//    /**
//     * Should get restore deleted object context when valid filter is provided.
//     */
//    @Test
//    public void shouldGetRestoreDeletedObjectContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( TEST_CONTEXT_URL, TEST_CONTEXT_ICON, TEST_CONTEXT_TITLE ) );
//        mockControl.replay();
//        List< ContextMenuItem > actualList = manager.getContextRouter( populateFilterDTO(), DeletedObjectDTO.class );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should get meta data context when valid filter is provided.
//     */
//    @Test
//    public void shouldGetMetaDataContextWhenValidFilterIsProvided() {
//        List< ContextMenuItem > expectedList = new ArrayList<>();
//        expectedList.add( new ContextMenuItem( TEST_CONTEXT_URL, TEST_CONTEXT_ICON, TEST_CONTEXT_TITLE ) );
//        mockControl.replay();
//        List< ContextMenuItem > actualList = manager.getMetaDataContextRouter( OBJECT_ID.toString(), populateFilterDTO() );
//        Assert.assertEquals( expectedList, actualList );
//    }
//
//    /**
//     * Should successfully restore object when valid selection is provided for bulk restore mode.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyRestoreObjectWhenValidSelectionIsProvidedForBulkRestoreMode() throws Exception {
//        List< SuSEntity > expectedList = Arrays.asList( fillSuSEntityWithDataObjectEntity() );
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect(
//                        permissionManager.isPermitted( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( true ).anyTimes();
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( job );
//        EasyMock.expect(
//                        dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( expectedList.get( FIRST_INDEX ) ).anyTimes();
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedList ).anyTimes();
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( new ArrayList<>() ).anyTimes();
//        EasyMock.expect(
//                        contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ) ) )
//                .andReturn( getFilterDTOForRestoringObjectBySelection() ).anyTimes();
//        mockControl.replay();
//    }
//
//    /**
//     * Should successfully restore object when valid selection id is provided for single restore mode.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void shouldSuccessfullyRestoreObjectWhenValidSelectionIdIsProvidedForSingleRestoreMode() throws Exception {
//        List< SuSEntity > expectedList = Arrays.asList( fillSuSEntityWithDataObjectEntity() );
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedList ).anyTimes();
//        EasyMock.expect(
//                        contextMenuManager.getFilterBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( String.class ) ) )
//                .andReturn( getFilterDTOForRestoringObjectBySelection() ).anyTimes();
//        EasyMock.expect( locationManager.getLocation( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( new LocationDTO( LOCATION_ID, DEAFULT_LOCATION_NAME ) ).anyTimes();
//        getPermittedTrue();
//        Job job = new JobImpl();
//        job.setId( UUID.randomUUID() );
//        job.setLog( new ArrayList<>() );
//        EasyMock.expect( jobManager.createJob( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( job );
//        mockControl.replay();
//    }
//
//    /**
//     * Should successfully restore object when valid selection id is provided for bulk objects.
//     */
//    @Test
//    public void shouldSuccessfullyRestoreObjectWhenValidSelectionIdIsProvidedForBulkObjects() {
//        List< SuSEntity > expectedList = Arrays.asList( fillSuSEntityWithDataObjectEntity() );
//
//        ProjectEntity expected = fillProjectEntityOfTypeData();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected );
//
//        EasyMock.expect( dao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedList ).anyTimes();
//        EasyMock.expect( dao.getDeletedParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( new ArrayList<>() );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( expected ).anyTimes();
//        getPermittedTrue();
//        mockControl.replay();
//        Job job = new JobImpl();
//        job.setLog( new ArrayList<>() );
//        boolean status = manager.restoreDeletedObjects( EasyMock.anyObject( EntityManager.class ), USER_ID.toString(), SELECTION_ID, job );
//        Assert.assertTrue( status );
//    }
//
//    /**
//     * Should not restore object and throw exception when provided restore mode is not supported.
//     */
//    @Test
//    public void shouldNotRestoreObjectAndThrowExceptionWhenProvidedRestoreModeIsNotSupported() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MODE_NOT_SUPPORTED.getKey(), ConstantsString.EMPTY_STRING ) );
//        manager.restoreObjectBySelection( USER_ID.toString(), SELECTION_ID.toString(), ConstantsString.EMPTY_STRING );
//    }
//
//    /**
//     * Should throw exception for remove AC es with user selections when inherit through parent is null.
//     */
//    @Test
//    public void shouldThrowExceptionForRemoveACEsWithUserSelectionsWhenInheritThroughParentIsNull() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PERMISSION_NOT_APPLIED_ON_NULL_PARENT.getKey() ) );
//        mockAndReturnObjectIdentityEntityWithInheritanceFalseButParentNotAvailabe();
//        dataObjectEntity.setUserSelectionId( USER_SELECTION );
//        mockLatestObjectByTypeAndId();
//        mockSelectionIds();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map );
//    }
//
//    /**
//     * Should success fully generate permission F orm for set inherited when valid input is given.
//     */
//    @Test
//    public void shouldSuccessFullyGeneratePermissionFOrmForSetInheritedWhenValidInputIsGiven() {
//        List< SelectFormItem > expected = fillSelectUI( SET_INHERITED );
//        mockAndReturnObjectIdentityEntityWithInheritanceTrue();
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        List< SelectFormItem > actual = manager.permissionObjectOptionsForm( USER_ID.toString(), OBJECT_ID.toString(), "Object" );
//        Assert.assertEquals( expected.size(), actual.size() );
//    }
//
//    /**
//     * Should success fully generate permission F orm for un set inherited when valid input is given.
//     */
//    @Test
//    public void shouldSuccessFullyGenerateProjectPermissionFOrmForUnSetInheritedWhenValidInputIsGiven() {
//        List< SelectFormItem > expected = fillSelectUI( UNSET_INHERITED );
//        mockAndReturnObjectIdentityEntityWithInheritanceFalse();
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        List< SelectFormItem > actual = manager.permissionObjectOptionsForm( USER_ID.toString(), OBJECT_ID.toString(), "Project" );
//        Assert.assertEquals( expected.size(), actual.size() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getHelp(), actual.get( FIRST_INDEX ).getHelp() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getLabel(), actual.get( FIRST_INDEX ).getLabel() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getName(), actual.get( FIRST_INDEX ).getName() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getPlaceHolder(), actual.get( FIRST_INDEX ).getPlaceHolder() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getTooltip(), actual.get( FIRST_INDEX ).getTooltip() );
//    }
//
//    /**
//     * Should success fully generate object permission F orm for un set inherited when valid input is given.
//     */
//    @Test
//    public void shouldSuccessFullyGenerateObjectPermissionFOrmForUnSetInheritedWhenValidInputIsGiven() {
//        List< SelectFormItem > expected = fillSelectUI( UNSET_INHERITED );
//        mockAndReturnObjectIdentityEntityWithInheritanceFalse();
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        List< SelectFormItem > actual = manager.permissionObjectOptionsForm( USER_ID.toString(), OBJECT_ID.toString(), "Object" );
//        Assert.assertEquals( expected.size(), actual.size() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getHelp(), actual.get( FIRST_INDEX ).getHelp() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getLabel(), actual.get( FIRST_INDEX ).getLabel() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getName(), actual.get( FIRST_INDEX ).getName() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getPlaceHolder(), actual.get( FIRST_INDEX ).getPlaceHolder() );
//        Assert.assertEquals( expected.get( FIRST_INDEX ).getTooltip(), actual.get( FIRST_INDEX ).getTooltip() );
//    }
//
//    /**
//     * Should throw exception when invalid option provided.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInvalidOptionProvided() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.INVALID_OPTION_FOR_PERMISSION.getKey() ) );
//        mockAndReturnObjectIdentityEntityWithInheritanceFalse();
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//        manager.permissionObjectOptionsForm( USER_ID.toString(), OBJECT_ID.toString(), "inavlid" );
//    }
//
//    /**
//     * Should success fully remove AC es with group selections and inherit false through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithGroupSelectionsAndInheritFalseThroughParentWhenValidInputIsProvided() {
//        String groupSelectionId = UUID.randomUUID().toString();
//        mockAndReturnObjectIdentityEntityWithInheritanceTrue();
//        mockAclObjectEntityWithNewInstance();
//        mockLatestObjectByTypeAndId();
//        dataObjectEntity.setGroupSelectionId( groupSelectionId );
//        mockSaveOrUpdateDataObjectEntity();
//        mockAclDAO();
//        mockGetAclEntryListByObjectId();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        List< UUID > uuids = new ArrayList<>();
//        uuids.add( UUID.randomUUID() );
//        EasyMock.expect(
//                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( uuids ).anyTimes();
//        AclSecurityIdentityEntity aclSecurityIdentityEntity = getAclSecurityIdentityEntity();
//        mockGetSecurityIdentityById( aclSecurityIdentityEntity );
//        mockControl.replay();
//        Map< String, String > map = prepareMapForGroup( groupSelectionId );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es with group selections and inherit through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithGroupSelectionsAndInheritThroughParentWhenValidInputIsProvided() {
//        mockInheritanceFalseAndProvideACEsForInheritanceTrue();
//        dataObjectEntity.setGroupSelectionId( GROUP_SELECTION );
//        mockLatestObjectByTypeAndId();
//        mockSelectionIds();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es without having selections and inherit through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithoutHavingSelectionsAndInheritThroughParentWhenValidInputIsProvided() {
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockInheritanceFalseAndProvideACEsForInheritanceTrue();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es with out user and group selections and inherit false through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithOutUserAndGroupSelectionsAndInheritFalseThroughParentWhenValidInputIsProvided() {
//        mockAndReturnObjectIdentityEntityWithInheritanceTrue();
//        mockAclObjectEntityWithNewInstance();
//        mockLatestObjectByTypeAndId();
//        mockSaveOrUpdateDataObjectEntity();
//        mockAclDAO();
//        mockGetAclEntryListByObjectId();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockControl.replay();
//        Map< String, String > map = updateObject( UNSET_INHERITED );
//        map.put( GROUP_TABLE_NAME, "" );
//        map.put( USER_TABLE_NAME, "" );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es with user and group selections and inherit through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithUserAndGroupSelectionsAndInheritThroughParentWhenValidInputIsProvided() {
//        mockInheritanceFalseAndProvideACEsForInheritanceTrue();
//        dataObjectEntity.setGroupSelectionId( GROUP_SELECTION );
//        dataObjectEntity.setUserSelectionId( USER_SELECTION );
//        mockLatestObjectByTypeAndId();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockSelectionIds();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es with user selections and inherit false through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithUserSelectionsAndInheritFalseThroughParentWhenValidInputIsProvided() {
//        String userSelectionId = UUID.randomUUID().toString();
//        mockAndReturnObjectIdentityEntityWithInheritanceTrue();
//        mockAclObjectEntityWithNewInstance();
//        mockLatestObjectByTypeAndId();
//        dataObjectEntity.setUserSelectionId( userSelectionId );
//        mockSaveOrUpdateDataObjectEntity();
//        mockAclDAO();
//        mockGetAclEntryListByObjectId();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        List< UUID > uuids = new ArrayList<>();
//        uuids.add( UUID.randomUUID() );
//        EasyMock.expect(
//                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( uuids ).anyTimes();
//        AclSecurityIdentityEntity aclSecurityIdentityEntity = getAclSecurityIdentityEntity();
//        mockGetSecurityIdentityById( aclSecurityIdentityEntity );
//        mockControl.replay();
//        Map< String, String > map = prepareMapForUser( userSelectionId );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should success fully remove AC es with user selections and inherit through parent when valid input is provided.
//     */
//    @Test
//    public void shouldSuccessFullyRemoveACEsWithUserSelectionsAndInheritThroughParentWhenValidInputIsProvided() {
//        mockInheritanceFalseAndProvideACEsForInheritanceTrue();
//        dataObjectEntity.setUserSelectionId( USER_SELECTION );
//        mockLatestObjectByTypeAndId();
//        mockSelectionIds();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        Assert.assertTrue( manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map ) );
//    }
//
//    /**
//     * Should throw exception when inherited object tries to inherit it again.
//     */
//    @Test
//    public void shouldThrowExceptionWhenInheritedObjectTriesToInheritItAgain() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.PERMISSION_ALREADY_INHERITED.getKey() ) );
//        mockAndReturnObjectIdentityEntityWithInheritanceTrue();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map );
//    }
//
//    /**
//     * Should throw exception when object is not binded with acl.
//     */
//    @Test
//    public void shouldThrowExceptionWhenObjectIsNotBindedWithAcl() {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.RESOURCE_NOT_BINDED_WITH_ACL.getKey() ) );
//        EasyMock.expect( permissionManager.getObjectIdentityDAO() ).andReturn( objectIdentityDAO ).anyTimes();
//        mockControl.replay();
//        Map< String, String > map = updateObject( SET_INHERITED );
//        manager.changeObjectPermissions( SUPER_USER_ID, OBJECT_ID.toString(), map );
//    }
//
//    /**
//     * Should return null when object is inherited true.
//     */
//    @Test
//    public void shouldReturnNullWhenObjectIsInheritedTrue() {
//        Assert.assertNull( manager.getUpdateObjectPermissionUI( SUPER_USER_ID, OBJECT_ID.toString(), SET_INHERITED ) );
//    }
//
//    /**
//     * Should return object permission form with user and group selection when valid input is given.
//     */
//    @Test
//    public void shouldReturnObjectPermissionFormWithUserAndGroupSelectionWhenValidInputIsGiven() {
//        List< UIFormItem > expected = fillUIFormItem();
//        dataObjectEntity.setUserSelectionId( OBJECT_ID.toString() );
//        dataObjectEntity.setGroupSelectionId( OBJECT_ID.toString() );
//        mockLatestObjectByTypeAndId();
//        mockControl.replay();
//        // List< UIFormItem > actual = manager.getUpdateObjectPermissionUI( SUPER_USER_ID, OBJECT_ID.toString(),
//        // DataManagerImpl.INHERIT_FALSE );
//        // Assert.assertEquals( expected.size(), actual.size() );
//        // Assert.assertEquals( expected.get( FIRST_INDEX ).getHelp(), actual.get( FIRST_INDEX ).getHelp() );
//        // Assert.assertEquals( expected.get( FIRST_INDEX ).getLabel(), actual.get( FIRST_INDEX ).getLabel() );
//        // Assert.assertEquals( expected.get( FIRST_INDEX ).getName(), actual.get( FIRST_INDEX ).getName() );
//        // Assert.assertEquals( expected.get( FIRST_INDEX ).getPlaceHolder(), actual.get( FIRST_INDEX ).getPlaceHolder() );
//        // Assert.assertEquals( expected.get( FIRST_INDEX ).getSelectable(), actual.get( FIRST_INDEX ).getSelectable() );
//    }
//
//    /**
//     * Should create object by super user when valid selection id is passed.
//     */
//    @Test
//    public void shouldCreateObjectBySuperUserWhenValidSelectionIdIsPassed() {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        projectEntity.setGroupSelectionId( DEFAULT_SELECTION_ID );
//        projectEntity.setUserSelectionId( DEFAULT_SELECTION_ID );
//        List< UUID > ids = new ArrayList<>();
//        ids.add( ENTRY_ID );
//        SelectionResponseUI selection = new SelectionResponseUI();
//        selection.setId( ENTRY_ID.toString() );
//        DataObjectDTO expectedObjectDto = fillDataObjectDTO();
//        DataObjectEntity expectedObjectEnatity = expectedObjectDto.prepareEntity( USER_ID.toString() );
//        expectedObjectDto.setId( expectedObjectEnatity.getComposedId().getId() );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectEnatity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        EasyMock.expect(
//                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( ids ).anyTimes();
//
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO ).anyTimes();
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() ).anyTimes();
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( SUPER_USER_ID, STR_UUID_PRENT_ID,
//                STR_UUID_OBJECT_TYPE_ID, JsonUtils.toJson( expectedObjectDto ), true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * Should successfully return form when valid parent id is given.
//     */
//    @Test
//    public void shouldSuccessfullyReturnFormWhenValidParentIdIsGiven() {
//        List< UIFormItem > expected = prepareChangeStatusOptionsForm();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( getDataObjectEntity() ).anyTimes();
//        mockControl.replay();
//        // List< UIFormItem > actual = manager.changeStatusObjectOptionForm( DEFAULT_USER_ID, PROJECT_ID );
//        // Assert.assertNotNull( actual );
//        // for ( UIFormItem actualItem : actual ) {
//        // for ( UIFormItem expectedItem : expected ) {
//        // if ( expectedItem.getName().equals( actualItem.getName() ) ) {
//        // Assert.assertEquals( expectedItem.getValue(), actualItem.getValue() );
//        // }
//        // }
//        // }
//    }
//
//    /**
//     * Should throw exception when status is not valid for object.
//     */
//    @Test
//    public void shouldSuccessfullyChangeStatusIncludedChilds() {
//        List< SuSEntity > suSEntities = new ArrayList<>();
//        SuSEntity dataObjectEntity = getDataObjectEntity();
//
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setFirstName( ConstantsString.SIMUSPACE );
//        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( userEntity );
//
//        suSEntities.add( dataObjectEntity );
//        StatusConfigDTO status = getStatusConfig();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( suSEntities );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( status ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByVersionAndStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt(), EasyMock.anyString() ) ).andReturn( getDataObjectEntity() );
//        mockControl.replay();
//        boolean changeStatus = manager.changeStatusObject( ConstantsID.SUPER_USER_ID, PROJECT_ID, getChangeStatusWipIndividual() );
//        Assert.assertTrue( changeStatus );
//    }
//
//    /**
//     * Should successfully change status individual object.
//     */
//    @Test
//    public void shouldSuccessfullyChangeStatusIndividualObject() {
//        List< SuSEntity > suSEntities = new ArrayList<>();
//        SuSEntity dataObjectEntity = getDataObjectEntity();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setFirstName( ConstantsString.SIMUSPACE );
//        userEntity.setId( UUID.fromString( ConstantsID.SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( userEntity );
//
//        suSEntities.add( dataObjectEntity );
//        StatusConfigDTO status = getStatusConfig();
//        EasyMock.expect( dao.getLatestObjectByVersionAndStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt(), EasyMock.anyString() ) ).andReturn( getDataObjectEntity() );
//        EasyMock.expect( dao.getLatestObjectByVersionAndStatus( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt(), EasyMock.anyString() ) ).andReturn( getDataObjectEntity() );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( suSEntities );
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new ArrayList<>() );
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( suSEntities );
//        EasyMock.expect( dao.getChildren( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(), EasyMock.anyObject() ) )
//                .andReturn( new ArrayList<>() );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( status ).anyTimes();
//        mockControl.replay();
//        boolean changeStatus = manager.changeStatusObject( ConstantsID.SUPER_USER_ID, PROJECT_ID, getChangeStatusWipIncludeChild() );
//        Assert.assertTrue( changeStatus );
//    }
//
//    /**
//     * Should successfully return true if export permission is set to the user.
//     */
//    @Test
//    public void shouldSuccessfullyReturnTrueIfExportPermissionIsSetToTheUser() {
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( true ).anyTimes();
//        mockControl.replay();
//
//        boolean changeStatus = manager.checkUserReadPermission( DEFAULT_USER_ID, PROJECT_ID.toString() );
//        Assert.assertTrue( changeStatus );
//    }
//
//    /**
//     * Should successfully return false if export permission is set to the user.
//     */
//    @Test
//    public void shouldSuccessfullyReturnFalseIfExportPermissionIsSetToTheUser() {
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( false ).anyTimes();
//        mockControl.replay();
//
//        boolean changeStatus = manager.checkUserReadPermission( DEFAULT_USER_ID, PROJECT_ID.toString() );
//        Assert.assertFalse( changeStatus );
//    }
//
//    /**
//     * Gets the change status wip.
//     *
//     * @return the change status wip
//     */
//    private ChangeStatusDTO getChangeStatusWipIndividual() {
//        ChangeStatusDTO changeStatus = new ChangeStatusDTO();
//        changeStatus.setStatus( WIP_ID );
//        changeStatus.setIncludeChilds( UI_COLUMN_OPTION_VALUE_NO );
//        return changeStatus;
//    }
//
//    /**
//     * Gets the change status wip include child.
//     *
//     * @return the change status wip include child
//     */
//    private ChangeStatusDTO getChangeStatusWipIncludeChild() {
//        ChangeStatusDTO changeStatus = new ChangeStatusDTO();
//        changeStatus.setStatus( WIP_ID );
//        changeStatus.setIncludeChilds( UI_COLUMN_OPTION_VALUE_YES );
//        return changeStatus;
//    }
//
//    /**
//     * Prepare change status options form.
//     *
//     * @return the list
//     */
//    private List< UIFormItem > prepareChangeStatusOptionsForm() {
//        List< UIFormItem > list = new ArrayList<>();
//
//        SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
//        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) item, GUIUtils.getIncludeChildObjectOptionsForUser(), null, false );
//        item.setLabel( DataManagerImpl.INCLUDE_LABEL );
//        item.setName( DataManagerImpl.FIELD_NAME_OBJECT_CHANGE_STATUS );
//        list.add( item );
//        return list;
//    }
//
//    /**
//     * Fill UI form item.
//     *
//     * @return the list
//     */
//    private List< UIFormItem > fillUIFormItem() {
//
//        List< UIFormItem > uiFormItems = new ArrayList<>();
//
//        TableFormItem usersTable = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
//        usersTable.setMultiple( true );
//        usersTable.setName( USER_TABLE_NAME );
//        usersTable.setLabel( SELECT_USER );
//        usersTable.setType( SELECTION_TYPE_TABLE );
//        usersTable.setConnected( USER_SELECTION_PATH );
//        usersTable.setValue( OBJECT_ID );
//        usersTable.setSelectable( null );
//        uiFormItems.add( usersTable );
//
//        TableFormItem groupsTable = ( TableFormItem ) GUIUtils.createFormItem( FormItemType.TABLE );
//        groupsTable.setMultiple( true );
//        groupsTable.setName( GROUP_TABLE_NAME );
//        groupsTable.setLabel( SELECT_GROUP );
//        groupsTable.setType( SELECTION_TYPE_TABLE );
//        groupsTable.setConnected( GROUP_SELECTION_PATH );
//        groupsTable.setSelectable( STATUS_KEY );
//        groupsTable.setValue( OBJECT_ID );
//        uiFormItems.add( groupsTable );
//        return uiFormItems;
//    }
//
//    /**
//     * Prepare map for user.
//     *
//     * @param userSelectionId
//     *         the user selection id
//     *
//     * @return the map
//     */
//    private Map< String, String > prepareMapForUser( String userSelectionId ) {
//        Map< String, String > map = updateObject( UNSET_INHERITED );
//        map.put( USER_TABLE_NAME, userSelectionId );
//        return map;
//    }
//
//    /**
//     * Mock inheritance false and provide AC es for inheritance true.
//     */
//    private void mockInheritanceFalseAndProvideACEsForInheritanceTrue() {
//        mockAndReturnObjectIdentityEntityWithInheritanceFalse();
//        mockAclDAO();
//        mockGetAclEntryListByObjectId();
//    }
//
//    /**
//     * Prepare map for group.
//     *
//     * @param groupSelectionId
//     *         the group selection id
//     *
//     * @return the map
//     */
//    private Map< String, String > prepareMapForGroup( String groupSelectionId ) {
//        Map< String, String > map = updateObject( UNSET_INHERITED );
//        map.put( GROUP_TABLE_NAME, groupSelectionId );
//        return map;
//    }
//
//    /**
//     * Mock get security identity by id.
//     *
//     * @param aclSecurityIdentityEntity
//     *         the acl security identity entity
//     */
//    private void mockGetSecurityIdentityById( AclSecurityIdentityEntity aclSecurityIdentityEntity ) {
//        EasyMock.expect( userCommonManager.getSecurityIdentityBySidId( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( aclSecurityIdentityEntity ).anyTimes();
//    }
//
//    /**
//     * Gets the acl security identity entity.
//     *
//     * @return the acl security identity entity
//     */
//    private AclSecurityIdentityEntity getAclSecurityIdentityEntity() {
//        AclSecurityIdentityEntity aclSecurityIdentityEntity = new AclSecurityIdentityEntity();
//        aclSecurityIdentityEntity.setId( UUID.randomUUID() );
//        return aclSecurityIdentityEntity;
//    }
//
//    /**
//     * Mock get acl entry list by object id.
//     */
//    private void mockGetAclEntryListByObjectId() {
//        EasyMock.expect(
//                        aclEntryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( aclEntryEntities ).anyTimes();
//    }
//
//    /**
//     * Mock get acl entry list by object id.
//     */
//    private void mockGetAclSecurityIdentityEntityListByObjectId() {
//        EasyMock.expect(
//                        aclEntryDAO.getAclSecurityIdentityEntityListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( Arrays.asList( getAclSecurityIdentityEntity() ) ).anyTimes();
//        UserDTO user = new UserDTO();
//        user.setId( UUID.randomUUID().toString() );
//        SuSUserGroupDTO group = new SuSUserGroupDTO();
//        group.setId( UUID.randomUUID() );
//        EasyMock.expect( userCommonManager.getUserBySID( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( user ).anyTimes();
//        EasyMock.expect(
//                        userCommonManager.getUserGroupBySID( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( group ).anyTimes();
//        // EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( UUID.class ) ) ).andReturn( getDataObjectEntity() )
//        // .anyTimes();
//        SelectionResponseUI response = new SelectionResponseUI();
//        response.setId( UUID.randomUUID().toString() );
//        EasyMock.expect( selectionManager.createSelectionForSingleItem( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( response ).anyTimes();
//    }
//
//    /**
//     * Mock acl DAO.
//     */
//    private void mockAclDAO() {
//        EasyMock.expect( permissionManager.getEntryDAO() ).andReturn( aclEntryDAO ).anyTimes();
//    }
//
//    /**
//     * Mock save or update data object entity.
//     */
//    private void mockSaveOrUpdateDataObjectEntity() {
//        EasyMock.expect( dao.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//    }
//
//    /**
//     * Mock acl object entity with new instance.
//     */
//    private void mockAclObjectEntityWithNewInstance() {
//        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
//        EasyMock.expect( objectIdentityDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( AclObjectIdentityEntity.class ) ) ).andReturn( aclObjectIdentityEntity );
//    }
//
//    /**
//     * Mock and return object identity entity with inheritance true.
//     */
//    private void mockAndReturnObjectIdentityEntityWithInheritanceTrue() {
//        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
//        aclObjectIdentityEntity.setInherit( true );
//        EasyMock.expect( permissionManager.getObjectIdentityDAO() ).andReturn( objectIdentityDAO ).anyTimes();
//        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( aclObjectIdentityEntity );
//    }
//
//    /**
//     * Fill select UI.
//     *
//     * @param value
//     *         the value
//     *
//     * @return the list
//     */
//    private List< SelectFormItem > fillSelectUI( String value ) {
//        List< SelectFormItem > selectFormItems = new ArrayList<>();
//        SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
//        selectFormItem.setLabel( "Select Permission" );
//        selectFormItem.setName( "selection" );
//        selectFormItem.setType( "select" );
//        selectFormItem.setValue( value );
//        selectFormItem.setReadonly( false );
//        selectFormItem.setDuplicate( false );
//        selectFormItem.setMultiple( false );
//        selectFormItem.setBindFrom( "/data/object/" + OBJECT_ID + "/permission/fields/{__value__}" );
//        List< SelectOptionsUI > options = new ArrayList<>();
//        SelectOptionsUI selectOptionsUIForInheritTrue = new SelectOptionsUI();
//        selectOptionsUIForInheritTrue.setId( SET_INHERITED );
//        selectOptionsUIForInheritTrue.setName( "Set Inherited" );
//        SelectOptionsUI selectOptionsUIForInheritFalse = new SelectOptionsUI();
//        selectOptionsUIForInheritTrue.setId( UNSET_INHERITED );
//        selectOptionsUIForInheritTrue.setName( "Unset Inherited" );
//        options.add( selectOptionsUIForInheritTrue );
//        options.add( selectOptionsUIForInheritFalse );
//        selectFormItem.setOptions( options );
//        selectFormItems.add( selectFormItem );
//        return selectFormItems;
//    }
//
//    /**
//     * Update object.
//     *
//     * @param value
//     *         the value
//     *
//     * @return the map
//     */
//    private Map< String, String > updateObject( String value ) {
//        Map< String, String > map = new HashMap<>();
//        map.put( "selection", value );
//        return map;
//    }
//
//    /**
//     * Mock and return object identity entity with inheritance false but parent not availabe.
//     */
//    private void mockAndReturnObjectIdentityEntityWithInheritanceFalseButParentNotAvailabe() {
//        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
//        aclObjectIdentityEntity.setInherit( false );
//        EasyMock.expect( permissionManager.getObjectIdentityDAO() ).andReturn( objectIdentityDAO ).anyTimes();
//        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( aclObjectIdentityEntity );
//    }
//
//    /**
//     * Mock latest object by type and id.
//     */
//    private void mockLatestObjectByTypeAndId() {
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity ).anyTimes();
//    }
//
//    /**
//     * Mock and return object identity entity with inheritance false.
//     */
//    private void mockAndReturnObjectIdentityEntityWithInheritanceFalse() {
//        AclObjectIdentityEntity aclObjectIdentityEntity = new AclObjectIdentityEntity();
//        aclObjectIdentityEntity.setInherit( false );
//        aclObjectIdentityEntity.setParentObject( new AclObjectIdentityEntity() );
//        EasyMock.expect( permissionManager.getObjectIdentityDAO() ).andReturn( objectIdentityDAO ).anyTimes();
//        EasyMock.expect( objectIdentityDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( aclObjectIdentityEntity );
//    }
//
//    /**
//     * Mock selection ids.
//     */
//    private void mockSelectionIds() {
//        List< UUID > uuids = new ArrayList<>();
//        uuids.add( UUID.randomUUID() );
//        EasyMock.expect(
//                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( uuids ).anyTimes();
//    }
//
//    /**
//     * Should successfully get edit data object form when valid object id is provided.
//     */
//    @Test
//    public void shouldSuccessfullyGetEditDataObjectFormWhenValidObjectIdIsProvided() {
//        SuSObjectModel model = getFilledSuSModelObject();
//        DataObjectDTO dataObjectDTO = fillDataObjectDTO();
//        List< UIFormItem > expected = GUIUtils.prepareForm( false, dataObjectDTO );
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//        dataObjectEntity.getCustomAttributes().add( fillCustomAttributeEntity() );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( getStatusConfig() ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        mockControl.replay();
//        // List< UIFormItem > actual = manager.editDataObjectForm( EasyMock.anyObject( EntityManager.class ), OBJECT_ID.toString(),
//        // ConstantsID.SUPER_USER_ID );
//        // Assert.assertEquals( actual.get( FIRST_INDEX ).getType(), expected.get( FIRST_INDEX ).getType() );
//    }
//
//    /**
//     * Should successfully update object when valid object DTO is provided.
//     */
//    @Test
//    public void shouldSuccessfullyUpdateObjectWhenValidObjectDTOIsProvided() {
//        DataObjectDTO expectedDataObjectDTO = fillDataObjectDTO();
//        expectedDataObjectDTO.setId( OBJECT_ID );
//        Map< String, Object > map = new HashMap<>();
//        map.put( TEST_CUSTOM_ATTRIBUTE, TEST_CUSTOM_ATTRIBUTE );
//        expectedDataObjectDTO.setCustomAttributes( map );
//        StatusConfigDTO detail = getStatusConfig();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//        dataObjectEntity.getCustomAttributes().add( fillCustomAttributeEntity() );
//        getPermittedTrue();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//        EasyMock.expect( dao.getParents( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( SuSEntity.class ) ) )
//                .andReturn( Arrays.asList( dataObjectEntity ) ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( new ArrayList<>() ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity );
//        DataObjectEntity updatedEntity = fillDataObjectEntity();
//        updatedEntity.setName( PROJECT_NAME );
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( updatedEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyObject() ) ).andReturn( detail );
//        EasyMock.expect( dao.updateAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( dataObjectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSObjectTypeDataObject() ).anyTimes();
//        mockControl.replay();
//        DataObjectDTO actual = ( DataObjectDTO ) manager.updateDataObject( DEFAULT_USER_ID, expectedDataObjectDTO.getId().toString(),
//                JsonUtils.toJson( expectedDataObjectDTO ) );
//        Assert.assertEquals( actual.getName(), expectedDataObjectDTO.getName() );
//        Assert.assertEquals( actual.getCustomAttributes(), expectedDataObjectDTO.getCustomAttributes() );
//        Assert.assertEquals( actual.getDescription(), expectedDataObjectDTO.getDescription() );
//    }
//
//    /**
//     * Creates the generic DTO from object entity.
//     *
//     * @param projectId
//     *         the project id
//     * @param susEntity
//     *         the sus entity
//     *
//     * @return the object
//     */
//    private GenericDTO createGenericDTOFromObjectEntity( UUID projectId, SuSEntity susEntity ) {
//        GenericDTO genericDTO = null;
//        if ( susEntity != null ) {
//            genericDTO = new GenericDTO();
//            genericDTO.setName( susEntity.getName() );
//            genericDTO.setId( susEntity.getComposedId().getId() );
//            genericDTO.setCreatedOn( susEntity.getCreatedOn() );
//            genericDTO.setModifiedOn( susEntity.getModifiedOn() );
//            genericDTO.setParentId( projectId );
//        }
//        return genericDTO;
//    }
//
//    /**
//     * Should create data object and generate its thumb nail when valid object is given.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldCreateDataObjectAndGenerateItsThumbNailWhenValidObjectIsGiven() throws Exception {
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        ProjectEntity projectEntity = fillProjectEntityOfTypeData();
//        DataObjectDTO expectedObjectDto = fillDataObjectDTO();
//        DocumentDTO documentDTO = prepareDocumentDTO();
//        documentDTO.setType( ConstantsImageFileTypes.JPG );
//        documentDTO.setPath( ConstantsString.EMPTY_STRING );
//        expectedObjectDto.setFile( documentDTO );
//        DataObjectEntity expectedObjectEntity = expectedObjectDto.prepareEntity( DEFAULT_USER_ID );
//        expectedObjectEntity.setFile( prepareDocumentEntity() );
//        expectedObjectDto.setId( expectedObjectEntity.getComposedId().getId() );
//        EasyMock.expect( documentManager.getDocumentDAO() ).andReturn( documentDAO ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( documentManager.getDocumentById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( expectedObjectDto.getFile() ).anyTimes();
//        EasyMock.expect( documentManager.saveDocument( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectDto.getFile() );
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( prepareUserDTO( USER, NOT_RESTRICTED ) );
//
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( dao.createAnObject( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expectedObjectEntity ).anyTimes();
//        EasyMock.expect( dao.getSiblingsBySameIName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
//                EasyMock.anyObject( SuSEntity.class ), EasyMock.anyObject( SuSEntity.class ) ) ).andReturn( Arrays.asList() ).anyTimes();
//        EasyMock.expect( userCommonManager.getAclCommonSecurityIdentityDAO() ).andReturn( aclCommonSecurityIdentityDAO );
//        EasyMock.expect( aclCommonSecurityIdentityDAO.getSecurityIdentityBySid( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( new AclSecurityIdentityEntity() );
//        mockGetAclSecurityIdentityEntityListByObjectId();
//        mockAccessControlEntities();
//        mockControl.replay();
//
//        DataObjectDTO actualDataObjectDTO = ( DataObjectDTO ) manager.createSuSObject( EasyMock.anyObject( EntityManager.class ),
//                SUPER_USER_ID, STR_UUID_PRENT_ID, STR_UUID_OBJECT_TYPE_ID, JsonUtils.toJson( expectedObjectDto ), true, null );
//        Assert.assertEquals( expectedObjectDto, actualDataObjectDTO );
//    }
//
//    /**
//     * Fill filtered response for generic DTO.
//     *
//     * @param genericDTO
//     *         the generic DTO
//     *
//     * @return the filtered response
//     */
//    private FilteredResponse< Object > fillFilteredResponseForGenericDTO( GenericDTO genericDTO ) {
//        FilteredResponse< Object > expected = new FilteredResponse<>();
//        expected.setData( Arrays.asList( genericDTO ) );
//        expected.setDisplayStart( 0 );
//        expected.setDraw( 1 );
//        expected.setLength( 10 );
//        expected.setStart( 0 );
//        return expected;
//    }
//
//    /**
//     * Gets the generic DTO.
//     *
//     * @return the generic DTO
//     */
//    private GenericDTO getGenericDTO() {
//        GenericDTO genericDTO = new GenericDTO();
//        genericDTO.setId( UUID.fromString( "7b9c4990-0fa4-46d4-a71e-25b9b9f3399e" ) );
//        genericDTO.setLifeCycleStatus( new StatusDTO( "553536c7-71ec-409d-8f48-ec779a98a68e", "WIP" ) );
//        genericDTO.setName( "Test Data Object name" );
//        genericDTO.setVersion( new VersionDTO( 1 ) );
//        return genericDTO;
//    }
//
//    /**
//     * Should successfully get data object preview image when valid object id is passed.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectPreviewImageWhenValidObjectIdIsPassed() throws Exception {
//        DocumentDTO expected = new DocumentDTO();
//        expected.setId( DOCUMENT_ID.toString() );
//        expected.setPath( TEST_FILE_NAME );
//        expected.setName( TEST_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        DataObjectImageEntity dataObjectEntity = fillDataObjectImageEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity );
//        mockControl.replay();
//        DocumentDTO actual = manager.getDataObjectPreview( OBJECT_ID );
//        Assert.assertEquals( actual, expected );
//    }
//
//    /**
//     * Should successfully get data object preview image when valid object id and version id is passed.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectPreviewImageWhenValidObjectIdAndVersionIdIsPassed() throws Exception {
//        DocumentDTO expected = new DocumentDTO();
//        expected.setId( DOCUMENT_ID.toString() );
//        expected.setPath( TEST_FILE_NAME );
//        expected.setName( TEST_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        DataObjectImageEntity dataObjectEntity = fillDataObjectImageEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( documentManager.prepareDocumentDTO( documentEntity ) ).andReturn( expected ).anyTimes();
//        EasyMock.expect( dao.getObjectByCompositeId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( dataObjectEntity );
//        mockControl.replay();
//        DocumentDTO actual = manager.getDataObjectVersionPreview( USER_ID, OBJECT_ID, DEFAULT_VERSION_ID );
//        Assert.assertEquals( actual, expected );
//    }
//
//    /**
//     * Should successfully get data object curve when valid object id is passed.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectCurveWhenValidObjectIdIsPassed() throws Exception {
//
//        File curveJsonFile = new File( CURVE_JSON_FILE_PATH );
//        DataObjectCurveDTO expectedCurve = fillDataObjectCurveDto();
//        DocumentDTO expected = new DocumentDTO();
//        expected.setId( DOCUMENT_ID.toString() );
//        expected.setPath( TEST_FILE_NAME );
//        expected.setName( CURVE_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForCurve() ).anyTimes();
//
//        EasyMock.expect( documentManager.getDocumentById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( expected ).anyTimes();
//        /*     EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) ).andReturn( curveJsonFile )
//                .anyTimes();*/
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity ).anyTimes();
//        mockControl.replay();
//        DataObjectCurveDTO actual = manager.getDataObjectCurve( EasyMock.anyObject( EntityManager.class ), OBJECT_ID, null );
//        Assert.assertEquals( actual.getName(), expectedCurve.getName() );
//        Assert.assertEquals( actual.getxDimension(), expectedCurve.getxDimension() );
//        Assert.assertEquals( actual.getyDimension(), expectedCurve.getyDimension() );
//        Assert.assertEquals( actual.getxUnit(), expectedCurve.getxUnit() );
//        Assert.assertEquals( actual.getyUnit(), expectedCurve.getyUnit() );
//
//    }
//
//    /**
//     * Should successfully get data object version movie when valid object id is passed.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectVersionMovieWhenValidObjectIdIsPassed() throws Exception {
//
//        DataObjectMovieDTO expected = fillDataObjectMoviewDto();
//        DocumentDTO expectedDoc = new DocumentDTO();
//        expectedDoc.setId( DOCUMENT_ID.toString() );
//        expectedDoc.setPath( TEST_FILE_NAME );
//        expectedDoc.setName( TEST_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        mockStaticMethodOfPropertiesUtilClass();
//        mockStaticMethodOfMovieUtilClass();
//        DataObjectEntity dataObjectEntity = fillDataObjectMovieEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsFileExtension.MP4 );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForMovie() ).anyTimes();
//
//        EasyMock.expect( documentManager.getDocumentById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expectedDoc ).anyTimes();
//
//        EasyMock.expect( documentManager.prepareDocumentDTO( EasyMock.anyObject( DocumentEntity.class ) ) ).andReturn( expectedDoc )
//                .anyTimes();
//
//        EasyMock.expect( dao.getObjectByCompositeId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( dataObjectEntity ).anyTimes();
//        mockControl.replay();
//        DataObjectMovieDTO actual = manager.getDataObjectVersionMovie( OBJECT_ID, DEFAULT_VERSION_ID );
//
//        Assert.assertEquals( expected.getPoster(), actual.getPoster() );
//        Assert.assertEquals( expected.getThumbnail(), actual.getThumbnail() );
//
//    }
//
//    /**
//     * Should successfully get data object movie when valid object id is passed.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldSuccessfullyGetDataObjectMovieWhenValidObjectIdIsPassed() throws Exception {
//
//        DataObjectMovieDTO expected = fillDataObjectMoviewDto();
//        DocumentDTO expectedDoc = new DocumentDTO();
//        expectedDoc.setId( DOCUMENT_ID.toString() );
//        expectedDoc.setPath( TEST_FILE_NAME );
//        expectedDoc.setName( TEST_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        mockStaticMethodOfPropertiesUtilClass();
//        mockStaticMethodOfMovieUtilClass();
//        DataObjectEntity dataObjectEntity = fillDataObjectMovieEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsFileExtension.MP4 );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForMovie() ).anyTimes();
//
//        EasyMock.expect( documentManager.getDocumentById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( expectedDoc ).anyTimes();
//        EasyMock.expect( documentManager.prepareDocumentDTO( EasyMock.anyObject( DocumentEntity.class ) ) ).andReturn( expectedDoc )
//                .anyTimes();
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity ).anyTimes();
//        mockControl.replay();
//        DataObjectMovieDTO actual = manager.getDataObjectMovie( EasyMock.anyObject( EntityManager.class ), OBJECT_ID );
//
//        Assert.assertEquals( expected.getPoster(), actual.getPoster() );
//        Assert.assertEquals( expected.getThumbnail(), actual.getThumbnail() );
//
//    }
//
//    /**
//     * Should get errro message in get dataobject movie if document type is not movie.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldGetErrroMessageInGetDataobjectMovieIfDocumentTypeIsNotMovie() throws Exception {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MOVIE_NOT_FOUND.getKey() ) );
//
//        DataObjectEntity dataObjectEntity = fillDataObjectMovieEntity();
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForCurve() ).anyTimes();
//
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity ).anyTimes();
//        mockControl.replay();
//        manager.getDataObjectMovie( EasyMock.anyObject( EntityManager.class ), OBJECT_ID );
//
//    }
//
//    /**
//     * Should get errro message in get dataobject version movie if document type is not movie.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldGetErrroMessageInGetDataobjectVersionMovieIfDocumentTypeIsNotMovie() throws Exception {
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.MOVIE_NOT_FOUND.getKey() ) );
//
//        DataObjectEntity dataObjectEntity = fillDataObjectMovieEntity();
//
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForCurve() ).anyTimes();
//
//        EasyMock.expect( dao.getObjectByCompositeId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ), EasyMock.anyInt() ) ).andReturn( dataObjectEntity ).anyTimes();
//
//        mockControl.replay();
//        manager.getDataObjectVersionMovie( OBJECT_ID, DEFAULT_VERSION_ID );
//
//    }
//
//    /**
//     * Should not successfully get data object curve when empty curve json file name is provided.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    @Test
//    public void shouldNotSuccessfullyGetDataObjectCurveWhenEmptyCurveJsonFileNameIsProvided() throws Exception {
//
//        File curveJsonFile = new File( ConstantsString.EMPTY_STRING );
//        DataObjectCurveDTO expectedCurve = null;
//        DocumentDTO expected = new DocumentDTO();
//        expected.setId( DOCUMENT_ID.toString() );
//        expected.setPath( TEST_FILE_NAME );
//        expected.setName( CURVE_FILE_NAME );
//        mockStaticMethodOfCommonUtilsClass();
//        DataObjectEntity dataObjectEntity = fillDataObjectEntity();
//        DocumentEntity documentEntity = prepareDocumentEntity();
//        documentEntity.setFileType( ConstantsImageFileTypes.JPG );
//        dataObjectEntity.setFile( documentEntity );
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( getFilledSuSModelObjectForCurve() ).anyTimes();
//
//        EasyMock.expect( documentManager.getDocumentByName( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
//                .andReturn( expected ).anyTimes();
//
//        /*    EasyMock.expect( documentManager.readVaultFromDisk( EasyMock.anyObject( DocumentDTO.class ) ) ).andReturn( curveJsonFile )
//                .anyTimes();*/
//        EasyMock.expect( dao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( dataObjectEntity );
//        mockControl.replay();
//        DataObjectCurveDTO actual = manager.getDataObjectCurve( EasyMock.anyObject( EntityManager.class ), OBJECT_ID, null );
//        Assert.assertEquals( actual, expectedCurve );
//
//    }
//
//    /**
//     * Should not successfully get data object curve when in valid object id is passed.
//     */
//    @Test
//    public void shouldNotSuccessfullyGetDataObjectCurveWhenInValidObjectIdIsPassed() {
//        DataObjectCurveDTO expected = null;
//        DataObjectCurveDTO actual = manager.getDataObjectCurve( EasyMock.anyObject( EntityManager.class ), OBJECT_ID, null );
//        Assert.assertEquals( expected, actual );
//    }
//
//    /**
//     * Should not successfully get data object preview image when in valid object id is passed.
//     */
//    @Test
//    public void shouldNotSuccessfullyGetDataObjectPreviewImageWhenInValidObjectIdIsPassed() {
//        DocumentDTO expected = prepareInvalidDocumentDTO();
//        DocumentDTO actual = manager.getDataObjectPreview( OBJECT_ID );
//        Assert.assertEquals( expected, actual );
//    }
//
//    /**
//     * Should not successfully get data object version preview image when in valid object id and version is passed.
//     */
//    @Test
//    public void shouldNotSuccessfullyGetDataObjectVersionPreviewImageWhenInValidObjectIdAndVersionIsPassed() {
//        DocumentDTO expected = prepareInvalidDocumentDTO();
//        DocumentDTO actual = manager.getDataObjectVersionPreview( USER_ID, OBJECT_ID, DEFAULT_VERSION_ID );
//        Assert.assertEquals( expected, actual );
//    }
//
//    /**
//     * Should successfully check in object when valid parameter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyCheckInObjectWhenValidParameterIsProvided() {
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        expected.setStatus( ConstantsStatus.ACTIVE );
//        expected.setRestricted( ConstantsStatus.ACTIVE );
//        expected.setChangable( true );
//        SuSObjectModel model = getFilledSuSModelObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( manager.prepareUserEntityFromUserModel( expected ) );
//        fillDataObjectEntity.setCheckedOut( true );
//        prepareEntitiesForCheckoutAndCheckinStatus( fillDataObjectEntity, expected );
//        DataObjectDTO actual = manager.setObjectSynchStatus( SUPER_USER_ID, ConstantsString.SIMUSPACE, UUID.randomUUID(),
//                ConstantsDbOperationTypes.CHECKIN );
//        Assert.assertEquals( fillDataObjectEntity.getCheckedOut(), actual.isCheckedOut() );
//    }
//
//    /**
//     * Should successfully check out object when valid parameter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyCheckOutObjectWhenValidParameterIsProvided() {
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        SuSObjectModel model = getFilledSuSModelObject();
//
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( null );
//        fillDataObjectEntity.setCheckedOut( false );
//        prepareEntitiesForCheckoutAndCheckinStatus( fillDataObjectEntity, expected );
//        DataObjectDTO actual = manager.setObjectSynchStatus( SUPER_USER_ID, ConstantsString.SIMUSPACE, UUID.randomUUID(),
//                ConstantsDbOperationTypes.CHECKOUT );
//        Assert.assertEquals( fillDataObjectEntity.getCheckedOut(), actual.isCheckedOut() );
//    }
//
//    /**
//     * Should successfully discard object when valid parameter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyDiscardObjectWhenValidParameterIsProvided() {
//
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        expected.setStatus( ConstantsStatus.ACTIVE );
//        expected.setRestricted( ConstantsStatus.ACTIVE );
//        expected.setChangable( true );
//        SuSObjectModel model = getFilledSuSModelObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( manager.prepareUserEntityFromUserModel( expected ) );
//        fillDataObjectEntity.setCheckedOut( true );
//        prepareEntitiesForCheckoutAndCheckinStatus( fillDataObjectEntity, expected );
//
//        DataObjectDTO actual = manager.setObjectSynchStatus( SUPER_USER_ID, ConstantsString.SIMUSPACE, UUID.randomUUID(),
//                ConstantsDbOperationTypes.DISCARD );
//        Assert.assertEquals( fillDataObjectEntity.getCheckedOut(), actual.isCheckedOut() );
//
//    }
//
//    /**
//     * Should throw exception when an other user tries to checkout obj that is already checked out.
//     */
//    @Test
//    public void shouldThrowExceptionWhenAnOtherUserTriesToCheckoutObjThatIsAlreadyCheckedOut() {
//
//        thrown.expect( SusException.class );
//        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey() ) );
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        expected.setStatus( ConstantsStatus.ACTIVE );
//        expected.setRestricted( ConstantsStatus.ACTIVE );
//        expected.setChangable( true );
//
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( manager.prepareUserEntityFromUserModel( expected ) );
//        fillDataObjectEntity.setCheckedOut( true );
//
//        EasyMock.expect( dataDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//        mockControl.replay();
//        String boolValue = "true";
//        manager.setObjectSynchStatus( SUPER_USER_ID, ConstantsString.SIMUSPACE, UUID.randomUUID(), boolValue );
//    }
//
//    /**
//     * Should successfully return object status when valid parameter is provided.
//     */
//    @Test
//    public void shouldSuccessfullyReturnObjectStatusWhenValidParameterIsProvided() {
//
//        UserDTO expected = new UserDTO();
//        expected.setId( SUPER_USER_ID );
//        expected.setFirstName( ConstantsString.SIMUSPACE );
//        expected.setStatus( ConstantsStatus.ACTIVE );
//        expected.setRestricted( ConstantsStatus.ACTIVE );
//        expected.setChangable( true );
//
//        DataObjectEntity fillDataObjectEntity = fillDataObjectEntity();
//        fillDataObjectEntity.getComposedId().setId( UUID.fromString( SUPER_USER_ID ) );
//        fillDataObjectEntity.setCheckedOutUser( manager.prepareUserEntityFromUserModel( expected ) );
//        fillDataObjectEntity.setCheckedOut( true );
//        fillDataObjectEntity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        StatusConfigDTO detail = new StatusConfigDTO();
//        detail.setId( SUPER_USER_ID );
//        detail.setName( ConstantsString.SIMUSPACE );
//
//        EasyMock.expect( dataDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( detail ).anyTimes();
//        mockControl.replay();
//
//        DataObjectDTO actualDataObject = manager.getobjectSynchStatus( SUPER_USER_ID, UUID.randomUUID() );
//        Assert.assertEquals( fillDataObjectEntity.getCheckedOut(), actualDataObject.isCheckedOut() );
//    }
//
//    /******************************************
//     * project ova tabs view
//     ******************************************/
//
//    @Test
//    public void shouldReturnListOfViewsWhenValidInputIsGiven() {
//        SuSObjectModel model = getFilledSuSModelObject();
//        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
//        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( objectViewManager.getUserObjectViewsByKeyAndConfig( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( expectedResponse );
//        mockControl.replay();
//        List< ObjectViewDTO > list = manager.getListOfObjectView( USER_ID.toString(), OBJECT_ID.toString(), DataObjectEntity.CLASS_ID );
//        Assert.assertEquals( objectViewDTO.getName(), list.get( INDEX ).getName() );
//        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( INDEX ).getObjectViewJson() );
//        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( INDEX ).getObjectViewKey() );
//    }
//
//    /**
//     * Should save object views when valid input is given.
//     */
//    @Test
//    public void shouldSaveObjectViewsWhenValidInputIsGiven() {
//        SuSObjectModel model = getFilledSuSModelObject();
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( EntityManager.class ),
//                EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) ).andReturn( expected );
//        mockControl.replay();
//        ObjectViewDTO objectViewDTO = manager.saveOrUpdateObjectView( USER_ID.toString(), OBJECT_ID.toString(),
//                DataObjectFileEntity.CLASS_ID, OBJECT_VIEW_PAYLOAD, true );
//        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
//        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
//        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
//    }
//
//    /**
//     * Should save object view as default view when valid input is given.
//     */
//    @Test
//    public void shouldSaveObjectViewAsDefaultViewWhenValidInputIsGiven() {
//        ObjectViewDTO expected = prepareObjectViewDTO();
//        SuSObjectModel model = getFilledSuSModelObject();
//        EasyMock.expect( dao.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( projectEntity ).anyTimes();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyObject(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//        EasyMock.expect( objectViewManager.saveDefaultObjectViewByConfig( EasyMock.anyObject( EntityManager.class ),
//                        EasyMock.anyObject( UUID.class ), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
//                .andReturn( expected );
//        mockControl.replay();
//        ObjectViewDTO objectViewDTO = manager.setObjectViewAsDefault( USER_ID.toString(), OBJECT_ID.toString(), DataObjectEntity.CLASS_ID,
//                VIEW_ID );
//        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
//        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
//        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
//    }
//
//    /**
//     * Prepare object view DTO.
//     *
//     * @return the object view DTO
//     */
//    private ObjectViewDTO prepareObjectViewDTO() {
//        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
//        objectViewDTO.setName( VIEW_NAME );
//        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.PROJECT_TABLE_KEY );
//        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
//        return objectViewDTO;
//    }
//
//    /**
//     * ************************** HELPER METHODS **************************.
//     *
//     * @param objectIdentityEntity
//     *         the object identity entity
//     * @param securityIdentityEntity
//     *         the security identity entity
//     *
//     * @return the acl entry entity
//     */
//    private AclEntryEntity fillEntryEntity( AclObjectIdentityEntity objectIdentityEntity,
//            AclSecurityIdentityEntity securityIdentityEntity ) {
//        AclEntryEntity entryEntity = new AclEntryEntity();
//        entryEntity.setId( ENTRY_ID );
//        entryEntity.setCreatedOn( new Date() );
//        entryEntity.setMask( 2 );
//        entryEntity.setDelete( Boolean.FALSE );
//        if ( objectIdentityEntity != null ) {
//            entryEntity.setObjectIdentityEntity( objectIdentityEntity );
//        }
//        if ( securityIdentityEntity != null ) {
//            entryEntity.setSecurityIdentityEntity( securityIdentityEntity );
//        }
//        return entryEntity;
//    }
//
//    /**
//     * Gets the variant version UI.
//     *
//     * @return the variant version UI
//     */
//    private List< TableColumn > getVariantVersionUI() {
//        List< TableColumn > columns;
//        columns = GUIUtils.listColumns( VariantDTO.class, VersionDTO.class );
//        for ( TableColumn tableColumn : columns ) {
//            if ( VariantDTO.VARIANT_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
//                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_OBJECT_BY_ID_AND_VERSION_URL );
//                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
//            }
//        }
//        return columns;
//    }
//
//    /**
//     * Gets the library version UI.
//     *
//     * @return the library version UI
//     */
//    private List< TableColumn > getLibraryVersionUI() {
//        List< TableColumn > columns;
//        columns = GUIUtils.listColumns( LibraryDTO.class, VersionDTO.class );
//        for ( TableColumn tableColumn : columns ) {
//            if ( LibraryDTO.LIBRARY_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
//                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_OBJECT_BY_ID_AND_VERSION_URL );
//                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
//            }
//        }
//        return columns;
//    }
//
//    /**
//     * Gets the project version UI.
//     *
//     * @return the project version UI
//     */
//    private List< TableColumn > getProjectVersionUI() {
//        List< TableColumn > columns;
//        columns = GUIUtils.listColumns( ProjectDTO.class, VersionDTO.class );
//        for ( TableColumn tableColumn : columns ) {
//            if ( ProjectDTO.PROJECT_NAME.equalsIgnoreCase( tableColumn.getName() ) ) {
//                tableColumn.getRenderer().setUrl( ConstantsObjectServiceEndPoints.VIEW_PROJECT_BY_ID_AND_VERSION_URL );
//                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
//            }
//        }
//        return columns;
//    }
//
//    /**
//     * Prepare entities for checkout and checkin status.
//     *
//     * @param fillDataObjectEntity
//     *         the fill data object entity
//     * @param expected
//     *         the expected
//     *
//     * @return the data object entity
//     */
//    private void prepareEntitiesForCheckoutAndCheckinStatus( DataObjectEntity fillDataObjectEntity, UserDTO expected ) {
//        StatusConfigDTO statusConfigDTO = new StatusConfigDTO();
//        statusConfigDTO.setName( WIP_NAME );
//        EasyMock.expect( dataDAO.getLatestObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject( UUID.class ) ) ).andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( userCommonManager.getUserById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//        EasyMock.expect( dataDAO.saveOrUpdate( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( fillDataObjectEntity ).anyTimes();
//        EasyMock.expect( lifeCycleManager.getLifeCycleStatusByStatusId( EasyMock.anyString() ) ).andReturn( new StatusConfigDTO() );
//
//        mockControl.replay();
//    }
//
//    /**
//     * Fill expected sync context menu items.
//     *
//     * @param parentId
//     *         the parent id
//     *
//     * @return the list
//     */
//    private List< ContextMenuItem > fillExpectedContextMenuItems( String parentId ) {
//        List< ContextMenuItem > cml = new ArrayList<>();
//        cml.add( new ContextMenuItem( SYNC_CONTEXT_URL, null, SYNC_CONTEXT_TITLE ) );
//        return cml;
//    }
//
//    /**
//     * Fill expected sync context menu items.
//     *
//     * @param parentId
//     *         the parent id
//     *
//     * @return the list
//     */
//    private List< ContextMenuItem > fillExpectedSyncContextMenuItems( String parentId ) {
//        List< ContextMenuItem > cml = new ArrayList<>();
//        cml.add( new ContextMenuItem( SYNC_CONTEXT_URL2, null, SYNC_CONTEXT_TITLE2 ) );
//        return cml;
//    }
//
//    /**
//     * Prepare user DTO.
//     *
//     * @param name
//     *         the name
//     * @param restricted
//     *         the restricted the restricted
//     *
//     * @return the user DTO
//     */
//    private UserDTO prepareUserDTO( String name, String restricted ) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId( USER_ID.toString() );
//        userDTO.setUserUid( SIMUSPACE );
//        userDTO.setUserName( name );
//        userDTO.setFirstName( SIMUSPACE );
//        userDTO.setSurName( SIMUSPACE );
//        userDTO.setStatus( ConstantsStatus.ACTIVE );
//        userDTO.setChangable( true );
//        userDTO.setRestricted( restricted );
//        return userDTO;
//    }
//
//    /**
//     * Gets the filled columns.
//     *
//     * @return the filled columns
//     */
//    private TableUI getFilledColumns() {
//        TableUI tableUI = new TableUI();
//        tableUI.setColumns( JsonUtils.jsonToList( TABLE_COLUMN_LIST, TableColumn.class ) );
//        return tableUI;
//    }
//
//    /**
//     * Fill parent object identity.
//     *
//     * @param classEntity
//     *         the class entity
//     * @param securityIdentityEntity
//     *         the security identity entity
//     *
//     * @return the object identity entity
//     */
//    private AclObjectIdentityEntity fillParentObjectIdentity( AclClassEntity classEntity,
//            AclSecurityIdentityEntity securityIdentityEntity ) {
//        AclObjectIdentityEntity objectIdentityEntity = new AclObjectIdentityEntity();
//        objectIdentityEntity.setId( PARENT_OBJECT_IDENTITY_ID );
//        objectIdentityEntity.setClassEntity( classEntity );
//        objectIdentityEntity.setOwnerSid( securityIdentityEntity );
//        objectIdentityEntity.setCreatedOn( new Date() );
//        objectIdentityEntity.setInherit( false );
//        return objectIdentityEntity;
//    }
//
//    /**
//     * Populates filter DTO.
//     *
//     * @return the filters DTO
//     */
//    private FiltersDTO populateFilterDTO() {
//        filtersDTO = new FiltersDTO();
//        filtersDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
//        filtersDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
//        filtersDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
//        filtersDTO.setFilteredRecords( 1L );
//        return filtersDTO;
//    }
//
//    /**
//     * Fill object identity.
//     *
//     * @param classEntity
//     *         the class entity
//     * @param securityIdentityEntity
//     *         the security identity entity
//     *
//     * @return the object identity entity
//     */
//    private AclObjectIdentityEntity fillObjectIdentity( AclClassEntity classEntity, AclSecurityIdentityEntity securityIdentityEntity ) {
//        objectIdentityEntity = new AclObjectIdentityEntity();
//        objectIdentityEntity.setId( OBJECT_IDENTITY_ID );
//        objectIdentityEntity.setClassEntity( classEntity );
//        objectIdentityEntity.setOwnerSid( securityIdentityEntity );
//        objectIdentityEntity.setCreatedOn( new Date() );
//        return objectIdentityEntity;
//    }
//
//    /**
//     * Fill class entity.
//     *
//     * @return the class entity
//     */
//    private AclClassEntity fillClassEntity() {
//        classEntity = new AclClassEntity();
//        classEntity.setId( CLASS_ID );
//        classEntity.setQualifiedName( "Object1" );
//        classEntity.setDescription( "" );
//        classEntity.setCreatedOn( new Date() );
//        return classEntity;
//    }
//
//    /**
//     * Fill security identity.
//     *
//     * @param sidId
//     *         the sid id
//     *
//     * @return the security identity entity
//     */
//    private AclSecurityIdentityEntity fillSecurityIdentity( UUID sidId ) {
//        AclSecurityIdentityEntity securityIdentityEntity = new AclSecurityIdentityEntity();
//        securityIdentityEntity.setId( UUID.randomUUID() );
//        securityIdentityEntity.setSid( sidId );
//        securityIdentityEntity.setPrinciple( Boolean.FALSE );
//        return securityIdentityEntity;
//    }
//
//    /**
//     * Dummy Tabs For Test Expected Case.
//     *
//     * @return List of Tabs names
//     */
//    private List< OVAConfigTab > getGeneralTabsList() {
//        List< OVAConfigTab > subTbs = new ArrayList<>();
//        subTbs.add( new OVAConfigTab( SusConstantObject.PROPERTIES_TAB, SusConstantObject.PROPERTIES_TAB, true ) );
//        subTbs.add( new OVAConfigTab( SusConstantObject.AUDIT_TAB, SusConstantObject.AUDIT_TAB, true ) );
//        subTbs.add( new OVAConfigTab( SusConstantObject.FILES_TAB, SusConstantObject.FILES_TAB, true ) );
//        return subTbs;
//    }
//
//    /**
//     * Gets the general tabs list as string.
//     *
//     * @return the general tabs list as string
//     */
//    private List< String > getGeneralTabsListAsString() {
//        List< String > subTbs = new ArrayList<>();
//        subTbs.add( SusConstantObject.PROPERTIES_TAB );
//        subTbs.add( SusConstantObject.AUDIT_TAB );
//        subTbs.add( SusConstantObject.FILES_TAB );
//        return subTbs;
//    }
//
//    /**
//     * Gets the object tabs list.
//     *
//     * @return the object tabs list
//     */
//    private List< String > getObjectTabsList() {
//        List< String > subTbs = new ArrayList<>();
//        subTbs.add( SusConstantObject.PROPERTIES_TAB );
//        return subTbs;
//    }
//
//    /**
//     * Dummy Tabs For Test Expected Case.
//     *
//     * @return List of Tabs names
//     */
//    private List< String > getDummyTabsViewOfProperties() {
//        List< String > subTbs = new ArrayList<>();
//        subTbs.add( SusConstantObject.PROPERTIES_TAB );
//        return subTbs;
//    }
//
//    /**
//     * A method to populate the project Dto for Expected Result of test.
//     *
//     * @return projectDTO
//     */
//    private ProjectDTO fillProjectDto() {
//
//        projectDTO.setId( PROJECT_ID );
//        projectDTO.setName( PROJECT_NAME );
//        projectDTO.setVersion( new VersionDTO( DEFAULT_VERSION_ID ) );
//        projectDTO.setDescription( PROJECT_DESCRPTION );
//        projectDTO.setCreatedOn( CREATED_ON_DATE );
//        projectDTO.setModifiedOn( UPDATED_ON_DATE );
//        projectDTO.setCustomAttributes( getMapOfCustomAttributes() );
//        projectDTO.setConfig( PROJECT_CONFIG_FILE_NAME );
//        projectDTO.setType( ProjectType.DATA.getKey() );
//        projectDTO.setTypeId( OBJECT_TYPE_ID );
//        projectDTO.setLifeCycleStatus( getStatusDto() );
//
//        return projectDTO;
//    }
//
//    /**
//     * Fill workflow project dto.
//     *
//     * @return the workflow project DTO
//     */
//    private WorkflowProjectDTO fillWorkflowProjectDto() {
//        workflowProjectDTO.setId( PROJECT_ID );
//        workflowProjectDTO.setName( PROJECT_NAME );
//        workflowProjectDTO.setVersion( new VersionDTO( DEFAULT_VERSION_ID + 1 ) );
//        workflowProjectDTO.setDescription( PROJECT_DESCRPTION );
//        workflowProjectDTO.setCreatedOn( CREATED_ON_DATE );
//        workflowProjectDTO.setModifiedOn( UPDATED_ON_DATE );
//        workflowProjectDTO.setCustomAttributes( getMapOfCustomAttributes() );
//        workflowProjectDTO.setConfig( PROJECT_CONFIG_FILE_NAME );
//        workflowProjectDTO.setTypeId( OBJECT_TYPE_ID );
//        workflowProjectDTO.setLifeCycleStatus( getStatusDto() );
//        return workflowProjectDTO;
//    }
//
//    /**
//     * Gets the status dto.
//     *
//     * @return the status dto
//     */
//    private StatusDTO getStatusDto() {
//
//        return new StatusDTO( WIP_LIFECYCLE_STATUS_ID, WIP_LIFECYCLE_STATUS_NAME );
//    }
//
//    /**
//     * Fill project DTO without parent id.
//     *
//     * @return the project DTO
//     */
//    private ProjectDTO fillProjectDTOWithoutParentId() {
//        ProjectDTO expectedProjectDTO = new ProjectDTO();
//        expectedProjectDTO.setParentId( PROJECT_ID );
//        expectedProjectDTO.setType( ProjectType.DATA.getKey() );
//        expectedProjectDTO.setTypeId( OBJECT_TYPE_ID );
//        expectedProjectDTO.setConfig( PROJECT_CONFIG_FILE_NAME );
//        return expectedProjectDTO;
//    }
//
//    /**
//     * A method to populate the project Entity for Expected Result of test.
//     *
//     * @return projectEntity;
//     */
//    private ProjectEntity fillProjectEntityOfTypeData() {
//
//        projectEntity.setComposedId( new VersionPrimaryKey( PROJECT_ID, DEFAULT_VERSION_ID ) );
//        projectEntity.setName( PROJECT_NAME );
//        projectEntity.setDescription( PROJECT_DESCRPTION );
//        projectEntity.setCustomAttributes( getSetOfCustomAttributes() );
//        projectEntity.setConfig( PROJECT_CONFIG_FILE_NAME );
//        projectEntity.setType( ProjectType.DATA.getKey() );
//        projectEntity.setTypeId( OBJECT_TYPE_ID );
//        projectEntity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        return projectEntity;
//    }
//
//    /**
//     * Fill library entity of type data.
//     *
//     * @return the library entity
//     */
//    private LibraryEntity fillLibraryEntityOfTypeData() {
//
//        libraryEntity.setComposedId( new VersionPrimaryKey( PROJECT_ID, DEFAULT_VERSION_ID ) );
//        libraryEntity.setName( PROJECT_NAME );
//        libraryEntity.setDescription( PROJECT_DESCRPTION );
//        libraryEntity.setCustomAttributes( getSetOfCustomAttributes() );
//        libraryEntity.setConfig( PROJECT_CONFIG_FILE_NAME );
//        libraryEntity.setTypeId( OBJECT_TYPE_ID );
//        libraryEntity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        return libraryEntity;
//    }
//
//    /**
//     * Fill variant entity of type data.
//     *
//     * @return the library entity
//     */
//    private VariantEntity fillVariantEntityOfTypeData() {
//
//        variantEntity.setComposedId( new VersionPrimaryKey( PROJECT_ID, DEFAULT_VERSION_ID ) );
//        variantEntity.setName( PROJECT_NAME );
//        variantEntity.setDescription( PROJECT_DESCRPTION );
//        variantEntity.setCustomAttributes( getSetOfCustomAttributes() );
//        variantEntity.setConfig( PROJECT_CONFIG_FILE_NAME );
//        variantEntity.setTypeId( OBJECT_TYPE_ID );
//        variantEntity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        return variantEntity;
//    }
//
//    /**
//     * Fill workflow project entity of type data.
//     *
//     * @return the workflow project entity
//     */
//    private WorkflowProjectEntity fillWorkflowProjectEntityOfTypeData() {
//        workflowProjectEntity.setComposedId( new VersionPrimaryKey( PROJECT_ID, DEFAULT_VERSION_ID ) );
//        workflowProjectEntity.setName( PROJECT_NAME );
//        workflowProjectEntity.setDescription( PROJECT_DESCRPTION );
//        workflowProjectEntity.setCustomAttributes( getSetOfCustomAttributes() );
//        workflowProjectEntity.setConfig( PROJECT_CONFIG_FILE_NAME );
//        workflowProjectEntity.setType( ProjectType.DATA.getKey() );
//        workflowProjectEntity.setTypeId( OBJECT_TYPE_ID );
//        workflowProjectEntity.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//        workflowProjectEntity.setCreatedOn( CREATED_ON_DATE );
//        return workflowProjectEntity;
//    }
//
//    /**
//     * A method to populate the DataObject Entity for Expected Result of test.
//     *
//     * @return projectEntity;
//     */
//    private DataObjectEntity fillDataObjectEntity() {
//
//        DataObjectEntity dob = new DataObjectEntity();
//        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//        dob.setOwner( new UserEntity( USER_ID ) );
//
//        return dob;
//    }
//
//    /**
//     * Fill second data object entity.
//     *
//     * @return the data object entity
//     */
//    private DataObjectEntity fillSecondDataObjectEntity() {
//
//        DataObjectEntity dob = new DataObjectEntity();
//        dob.setComposedId( new VersionPrimaryKey( ENTRY_ID, DEFAULT_VERSION_ID ) );
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//        dob.setOwner( new UserEntity( USER_ID ) );
//
//        return dob;
//    }
//
//    /**
//     * A method to populate the DataObject Entity for Expected Result of test.
//     *
//     * @return projectEntity;
//     */
//    private DataObjectImageEntity fillDataObjectImageEntity() {
//
//        DataObjectImageEntity dob = new DataObjectImageEntity();
//        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//        dob.setOwner( new UserEntity( USER_ID ) );
//
//        return dob;
//    }
//
//    /**
//     * Fill data object movie entity.
//     *
//     * @return the data object movie entity
//     */
//    private DataObjectMovieEntity fillDataObjectMovieEntity() {
//
//        DataObjectMovieEntity dob = new DataObjectMovieEntity();
//        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//        dob.setOwner( new UserEntity( USER_ID ) );
//        dob.setPreviewImage( prepareDocumentEntity() );
//        dob.setFile( prepareDocumentEntity() );
//        dob.setThumbnail( prepareDocumentEntity() );
//
//        return dob;
//    }
//
//    /**
//     * Creates the data object DTO from data object entity.
//     *
//     * @param projectId
//     *         the project id
//     * @param dataObjectEntity
//     *         the data object entity
//     *
//     * @return the data object DTO
//     */
//    private DataObjectDTO createDataObjectDTOFromDataObjectEntity( UUID projectId, DataObjectEntity dataObjectEntity ) {
//        DataObjectDTO dataObjectDTO = new DataObjectDTO();
//
//        if ( dataObjectEntity != null ) {
//            dataObjectDTO.setName( dataObjectEntity.getName() );
//            dataObjectDTO.setId( dataObjectEntity.getComposedId().getId() );
//            dataObjectDTO.setTypeId( dataObjectEntity.getTypeId() );
//            StatusDTO statusDTO = new StatusDTO( null, WIP_LIFECYCLE_STATUS_ID );
//            dataObjectDTO.setLifeCycleStatus( statusDTO );
//            dataObjectDTO.setParentId( projectId );
//        }
//
//        return dataObjectDTO;
//    }
//
//    /**
//     * Creates the library DTO from library entity.
//     *
//     * @param projectId
//     *         the project id
//     * @param libraryEntity
//     *         the library entity
//     *
//     * @return the library DTO
//     */
//    private LibraryDTO createLibraryDTOFromLibraryEntity( UUID projectId, LibraryEntity libraryEntity ) {
//        LibraryDTO libraryDTO = new LibraryDTO();
//
//        if ( libraryEntity != null ) {
//            libraryDTO.setName( libraryEntity.getName() );
//            libraryDTO.setId( libraryEntity.getComposedId().getId() );
//            libraryDTO.setTypeId( libraryEntity.getTypeId() );
//            StatusDTO statusDTO = new StatusDTO( null, WIP_LIFECYCLE_STATUS_ID );
//            libraryDTO.setLifeCycleStatus( statusDTO );
//            libraryDTO.setParentId( projectId );
//        }
//
//        return libraryDTO;
//    }
//
//    /**
//     * Creates the variant DTO from variant entity.
//     *
//     * @param projectId
//     *         the project id
//     * @param variantEntity
//     *         the variant entity
//     *
//     * @return the variant DTO
//     */
//    private VariantDTO createVariantDTOFromVariantEntity( UUID projectId, VariantEntity variantEntity ) {
//        VariantDTO variantDTO = new VariantDTO();
//
//        if ( variantEntity != null ) {
//            variantDTO.setName( variantEntity.getName() );
//            variantDTO.setId( variantEntity.getComposedId().getId() );
//            variantDTO.setTypeId( variantEntity.getTypeId() );
//            StatusDTO statusDTO = new StatusDTO( null, WIP_LIFECYCLE_STATUS_ID );
//            variantDTO.setLifeCycleStatus( statusDTO );
//            variantDTO.setParentId( projectId );
//        }
//
//        return variantDTO;
//    }
//
//    /**
//     * Fill library entity.
//     *
//     * @return the library entity
//     */
//    private LibraryEntity fillLibraryEntity() {
//
//        LibraryEntity dob = new LibraryEntity();
//        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        return dob;
//    }
//
//    /**
//     * Fill variant entity.
//     *
//     * @return the variant entity
//     */
//    private VariantEntity fillVariantEntity() {
//
//        VariantEntity dob = new VariantEntity();
//        dob.setComposedId( new VersionPrimaryKey( DATA_OBJECT_ID, DEFAULT_VERSION_ID ) );
//        dob.setName( DATA_OBJECT_NAME );
//        dob.setTypeId( OBJECT_TYPE_ID );
//        dob.setLifeCycleStatus( WIP_LIFECYCLE_STATUS_ID );
//
//        return dob;
//    }
//
//    /**
//     * A method to get Set of Custom Attributes of Entities.
//     *
//     * @return the sets the of custom attributes
//     */
//    private Set< CustomAttributeEntity > getSetOfCustomAttributes() {
//        Set< CustomAttributeEntity > set = new HashSet<>();
//        set.add( getFilledCustomAttributesEntity() );
//        return set;
//    }
//
//    /**
//     * A method to get Map of key value pair of customer Attributes.
//     *
//     * @return the map of custom attributes
//     */
//    private Map< String, Object > getMapOfCustomAttributes() {
//        Map< String, Object > map = new HashMap<>();
//
//        map.put( TEST_CUSTOM_ATTRIBUTE, TEST_CUSTOM_ATTRIBUTE );
//        return map;
//    }
//
//    /**
//     * A method to fill CustomAttributeEntity.
//     *
//     * @return the filled custom attributes entity
//     */
//    private CustomAttributeEntity getFilledCustomAttributesEntity() {
//        CustomAttributeEntity customAttributeEntity = new CustomAttributeEntity();
//        customAttributeEntity.setId( CUSTOM_ATTRIBUTE_UUID );
//        customAttributeEntity.setName( TEST_CUSTOM_ATTRIBUTE );
//        customAttributeEntity.setType( TEST_CUSTOM_ATTRIBUTE );
//        customAttributeEntity.setOptions( TEST_CUSTOM_ATTRIBUTE_OPTIONS );
//        customAttributeEntity.setValue( TEST_CUSTOM_ATTRIBUTE_BYTE );
//        return customAttributeEntity;
//
//    }
//
//    /**
//     * A method to fill CustomAttributeEntity as Expected Object.
//     *
//     * @return the custom attribute DTO
//     */
//    private CustomAttributeDTO fillCustomAttributeDTO() {
//        CustomAttributeDTO customAttributeDTO = new CustomAttributeDTO();
//        List< String > options = new ArrayList<>();
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_1 );
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_2 );
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_3 );
//
//        customAttributeDTO.setName( TEST_CUSTOM_ATTRIBUTE );
//        customAttributeDTO.setType( TEST_CUSTOM_ATTRIBUTE );
//
//        customAttributeDTO.setValue( null );
//        customAttributeDTO.setType( UIColumnType.SELECT.getKey() );
//
//        customAttributeDTO.setOptions( options );
//        return customAttributeDTO;
//
//    }
//
//    /**
//     * Prepare SuSModel For Expected Test.
//     *
//     * @return the filled su S model object
//     */
//    private SuSObjectModel getFilledSuSModelObject() {
//        SuSObjectModel model = new SuSObjectModel();
//        model.setId( OBJECT_TYPE_ID.toString() );
//        model.setName( ConstantsString.SIMUSPACE );
//        OVAConfigTab ovaConfigTab = new OVAConfigTab();
//        ovaConfigTab.setKey( SusConstantObject.PROPERTIES_TAB );
//        ovaConfigTab.setVisible( true );
//        List< OVAConfigTab > listOVA = new ArrayList<>();
//        listOVA.add( ovaConfigTab );
//        model.setViewConfig( listOVA );
//        List< CustomAttributeDTO > listCustomAttributeDTO = new ArrayList<>();
//        listCustomAttributeDTO.add( fillCustomAttributeDTO() );
//        model.setCustomAttributes( listCustomAttributeDTO );
//        model.setClassName( ProjectDTO.class.getName() );
//
//        return model;
//
//    }
//
//    /**
//     * Gets the filled su S model object for curve.
//     *
//     * @return the filled su S model object for curve
//     */
//    private SuSObjectModel getFilledSuSModelObjectForCurve() {
//        SuSObjectModel model = new SuSObjectModel();
//        model.setId( OBJECT_TYPE_ID.toString() );
//        model.setName( ConstantsString.SIMUSPACE );
//        OVAConfigTab ovaConfigTab = new OVAConfigTab();
//        ovaConfigTab.setKey( SusConstantObject.PROPERTIES_TAB );
//        ovaConfigTab.setVisible( true );
//        List< OVAConfigTab > listOVA = new ArrayList<>();
//        listOVA.add( ovaConfigTab );
//        model.setViewConfig( listOVA );
//        List< CustomAttributeDTO > listCustomAttributeDTO = new ArrayList<>();
//        listCustomAttributeDTO.add( fillCustomAttributeDTO() );
//        model.setCustomAttributes( listCustomAttributeDTO );
//        model.setClassName( DataObjectCurveDTO.class.getName() );
//
//        return model;
//
//    }
//
//    /**
//     * Gets the filled su S model object for movie.
//     *
//     * @return the filled su S model object for movie
//     */
//    private SuSObjectModel getFilledSuSModelObjectForMovie() {
//        SuSObjectModel model = new SuSObjectModel();
//        model.setId( OBJECT_TYPE_ID.toString() );
//        model.setName( ConstantsString.SIMUSPACE );
//        OVAConfigTab ovaConfigTab = new OVAConfigTab();
//        ovaConfigTab.setKey( SusConstantObject.PROPERTIES_TAB );
//        ovaConfigTab.setVisible( true );
//        List< OVAConfigTab > listOVA = new ArrayList<>();
//        listOVA.add( ovaConfigTab );
//        model.setViewConfig( listOVA );
//        List< CustomAttributeDTO > listCustomAttributeDTO = new ArrayList<>();
//        listCustomAttributeDTO.add( fillCustomAttributeDTO() );
//        model.setCustomAttributes( listCustomAttributeDTO );
//        model.setClassName( DataObjectMovieDTO.class.getName() );
//
//        return model;
//
//    }
//
//    /**
//     * Gets the filled sus model object.
//     *
//     * @param className
//     *         the class name
//     *
//     * @return the filled sus model object
//     */
//    private SuSObjectModel getFilledSusModelObject( String className ) {
//        SuSObjectModel model = new SuSObjectModel();
//        model.setId( "62c4c2f6-15f8-11e7-93ae-92361f002674" );
//        model.setName( DATA_OBJECT_NAME );
//        model.setClassName( className );
//        model.setCreatedOn( new Date() );
//        model.setHasLifeCycle( false );
//        model.setIcon( "fa fa-file-image-o font-black" );
//        model.setCategorizable( false );
//        model.setMassdata( true );
//        model.setUpdate( false );
//        model.setVersionable( true );
//        model.setLifeCycle( "ca836b74-d07c-4df2-8e57-c3d608523116" );
//        model.setVersion( new VersionDTO( 0 ) );
//
//        CustomAttributeDTO customAttributeDTO = new CustomAttributeDTO();
//        customAttributeDTO.setName( "customatt1" );
//        customAttributeDTO.setOptions( Arrays.asList( "3", "2", "1" ) );
//        customAttributeDTO.setTitle( "Object Custom Attribute" );
//        customAttributeDTO.setType( "select" );
//        customAttributeDTO.setValue( null );
//        model.setCustomAttributes( Arrays.asList( customAttributeDTO ) );
//
//        OVAConfigTab configTab = new OVAConfigTab();
//        configTab.setTypeId( "32ab43d4-d062-460b-b4aa-e1a4d9252006" );
//        configTab.setKey( "properties" );
//        configTab.setTitle( "properties" );
//        configTab.setVisible( true );
//        model.setContains( Arrays.asList( OBJECT_TYPE_ID.toString() ) );
//        return model;
//    }
//
//    /**
//     * Prepare create data object form.
//     *
//     * @return the list
//     */
//    private List< UIFormItem > prepareCreateDataObjectForm() {
//        DataObjectDTO dataObjectDTO = new DataObjectDTO();
//        dataObjectDTO.setParentId( PROJECT_ID );
//        dataObjectDTO.setTypeId( UUID.fromString( STR_UUID_OBJECT_TYPE_ID ) );
//        return GUIUtils.prepareForm( false, dataObjectDTO );
//    }
//
//    /**
//     * Mock static get master configuration file names.
//     *
//     * @param projectConfigurationsFileName
//     *         the project configurations file name
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticGetMasterConfigurationFileNames( String... projectConfigurationsFileName ) throws Exception {
//        PowerMockito.spy( ConfigFilePathReader.class );
//        PowerMockito.doReturn( Arrays.asList( projectConfigurationsFileName ) ).when( ConfigFilePathReader.class,
//                ConfigFilePathReader.STATIC_METHOD_GET_MASTER_CONFIGURATION_FILE_NAMES,
//                ObjectTypeConfigManagerImpl.MASTER_CONFIG_PATH_PROPERTY );
//    }
//
//    /**
//     * Mock static method of json utils class.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticMethodOfJsonUtilsClass() throws Exception {
//        PowerMockito.spy( JsonUtils.class );
//        MapFilterUtil mapFilterUtil = new MapFilterUtil();
//        mapFilterUtil.put( META_DATA_KEY, META_DATA_VALUE );
//        PowerMockito.doReturn( mapFilterUtil ).when( JsonUtils.class, JSON_TO_OBJECT_JSON_UTILS_STATIC_METHOD, Matchers.anyString(),
//                Matchers.any( MapFilterUtil.class ) );
//
//    }
//
//    /**
//     * Mock static method of file utils class.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticMethodsOfFileUtilsClass() throws Exception {
//        PowerMockito.spy( FileUtils.class );
//        PowerMockito.doReturn( ConstantsString.EMPTY_STRING ).when( FileUtils.class, CONVERT_FILE_CONTENTS_TO_STRING, Matchers.any() );
//    }
//
//    /**
//     * Mock static method of common utils class.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticMethodOfCommonUtilsClass() throws Exception {
//        PowerMockito.spy( CommonUtils.class );
//        PowerMockito.doReturn( ConstantsString.EMPTY_STRING ).when( CommonUtils.class, GET_BASE_URL_COMMON_UTILS_METHOD,
//                Matchers.anyString() );
//    }
//
//    /**
//     * Mock static method of properties util class.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticMethodOfPropertiesUtilClass() throws Exception {
//        PowerMockito.spy( PropertiesManager.class );
//        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH ).when( PropertiesManager.class, GET_VAULT_PATH );
//
//    }
//
//    /**
//     * Mock static method of movie util class.
//     *
//     * @throws Exception
//     *         the exception
//     */
//    private void mockStaticMethodOfMovieUtilClass() throws Exception {
//        PowerMockito.spy( MovieUtils.class );
//        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH ).when( MovieUtils.class, PREPARE_FF_MPEG_COMMAND, Matchers.anyString(),
//                Matchers.anyString(), Matchers.anyString() );
//        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH + POSTER ).when( MovieUtils.class, GET_MOVIE_POSTER, Matchers.anyString(),
//                Matchers.anyString() );
//        PowerMockito.doReturn( ConstantsString.TEST_RESOURCE_PATH + THUMB_NAIL ).when( MovieUtils.class, PREPARE_THUMBNAIL_FROM_MOVIE_FILE,
//                Matchers.anyString(), Matchers.anyString(), Matchers.anyString() );
//
//        PowerMockito.doNothing().when( MovieUtils.class, MAKE_WEBM_FILE, Matchers.anyString(), Matchers.anyString(), Matchers.anyString() );
//        PowerMockito.doNothing().when( MovieUtils.class, MAKE_MP4_FILE, Matchers.anyString(), Matchers.anyString(), Matchers.anyString() );
//
//    }
//
//    /**
//     * Convert JSO nto form items.
//     *
//     * @param customAttributes
//     *         the custom attributes
//     *
//     * @return the list
//     */
//
//    private List< UIFormItem > convertJSONtoFormItems( List< CustomAttributeDTO > customAttributes ) {
//        List< UIFormItem > toReturn = new ArrayList<>();
//        for ( CustomAttributeDTO item : customAttributes ) {
//            UIFormItem i;
//            if ( item.getType().contains( UI_COLUMN_TYPE_SELECT ) ) {
//                i = GUIUtils.createFormItem( FormItemType.SELECT );
//            } else {
//                i = GUIUtils.createFormItem();
//            }
//            i.setLabel( item.getTitle() );
//            i.setName( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + item.getName() );
//            i.setType( item.getType() );
//            i.setValue( item.getValue() );
//
//            if ( item.getType().contains( UI_COLUMN_TYPE_SELECT ) ) {
//                Map< Object, Object > map = new HashMap<>();
//
//                List< ? > validOptions = item.getOptions();
//                for ( Object op : validOptions ) {
//                    map.put( op, op );
//                }
//                GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) i, GUIUtils.getSelectBoxOptions( map ), item.getValue().toString(),
//                        false );
//            }
//            toReturn.add( i );
//        }
//
//        return toReturn;
//    }
//
//    /**
//     * Fill data object DTO.
//     *
//     * @return the data object DTO
//     */
//    private DataObjectDTO fillDataObjectDTO() {
//        DataObjectDTO dataObjectDTO = new DataObjectDTO();
//        dataObjectDTO.setParentId( PROJECT_ID );
//        dataObjectDTO.setName( DATA_OBJECT_NAME );
//        return dataObjectDTO;
//    }
//
//    /**
//     * Prepare SuSModel For Expected Test.
//     *
//     * @return the filled su S object type data object
//     */
//    private SuSObjectModel getFilledSuSObjectTypeDataObject() {
//        SuSObjectModel model = new SuSObjectModel();
//        model.setId( OBJECT_TYPE_ID.toString() );
//        model.setClassName( DATA_OBJECT_DTO_CLASS_NAME );
//        List< CustomAttributeDTO > listCustomAttributeDTO = new ArrayList<>();
//        listCustomAttributeDTO.add( fillCustomAttributeDTO() );
//        model.setCustomAttributes( listCustomAttributeDTO );
//        model.setViewConfig( getGeneralTabsList() );
//        model.setContains( Arrays.asList( OBJECT_TYPE_ID.toString() ) );
//        return model;
//    }
//
//    /**
//     * Fill SuSEntity With Data Object Entity.
//     *
//     * @param uuid
//     *         the uuid
//     *
//     * @return SuSEntity
//     */
//    private SuSEntity fillSuSEntityWithDataObjectEntity( UUID uuid ) {
//
//        SuSEntity dataObjectEntity = new DataObjectEntity();
//        dataObjectEntity.setComposedId( new VersionPrimaryKey() );
//        dataObjectEntity.getComposedId().setId( uuid );
//        dataObjectEntity.getComposedId().setVersionId( ConstantsInteger.INTEGER_VALUE_ONE );
//        dataObjectEntity.setName( "Object-1" );
//        dataObjectEntity.setTypeId( OBJECT_TYPE_ID );
//        return dataObjectEntity;
//    }
//
//    private SuSEntity fillSuSEntityWithDataObjectEntity() {
//
//        SuSEntity dataObjectEntity = new DataObjectEntity();
//        dataObjectEntity.setComposedId( new VersionPrimaryKey() );
//        dataObjectEntity.getComposedId().setId( OBJECT_ID );
//        dataObjectEntity.getComposedId().setVersionId( ConstantsInteger.INTEGER_VALUE_ONE );
//        dataObjectEntity.setName( "Object-1" );
//        dataObjectEntity.setTypeId( OBJECT_TYPE_ID );
//        return dataObjectEntity;
//    }
//
//    /**
//     * Prepare data object table UI.
//     *
//     * @param customAttributeDTOs
//     *         the custom attribute DT os
//     *
//     * @return the list
//     */
//    private List< TableColumn > prepareDataObjectTableUI( List< CustomAttributeDTO > customAttributeDTOs ) {
//        List< TableColumn > tableColumns = GUIUtils.listColumns( DataObjectDTO.class );
//        tableColumns.addAll( prepareCustomAttributesTableColumns( customAttributeDTOs ) );
//        return tableColumns;
//    }
//
//    /**
//     * Prepare custom attributes table columns.
//     *
//     * @param customAttributeDTOs
//     *         the custom attribute DT os
//     *
//     * @return the list
//     */
//    private List< TableColumn > prepareCustomAttributesTableColumns( List< CustomAttributeDTO > customAttributeDTOs ) {
//        List< TableColumn > tableColumns = new ArrayList<>();
//        for ( CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
//            TableColumn moduleColumn = new TableColumn();
//            moduleColumn.setData( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME + customAttributeDTO.getName() );
//            moduleColumn.setName( customAttributeDTO.getName() );
//            moduleColumn.setTitle( customAttributeDTO.getTitle() );
//            Renderer renderer = new Renderer();
//            renderer.setType( customAttributeDTO.getType() );
//            moduleColumn.setRenderer( renderer );
//
//            tableColumns.add( moduleColumn );
//        }
//        return tableColumns;
//    }
//
//    /**
//     * Prepare object meta data DTO.
//     *
//     * @return the object metadata DTO
//     */
//    private ObjectMetaDataDTO prepareObjectMetaDataDTO() {
//        ObjectMetaDataDTO objectMetadataDTO = new ObjectMetaDataDTO();
//        objectMetadataDTO.setMetadata( Arrays.asList( new MetaDataEntryDTO( META_DATA_KEY, META_DATA_VALUE ) ) );
//        return objectMetadataDTO;
//    }
//
//    /**
//     * Prepare document DTO.
//     *
//     * @return the document DTO
//     */
//    private DocumentDTO prepareDocumentDTO() {
//        DocumentDTO documentDTO = new DocumentDTO();
//        documentDTO.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        documentDTO.setId( UUID.randomUUID().toString() );
//        documentDTO.setName( TEST_META_DATA_FILE );
//        return documentDTO;
//    }
//
//    /**
//     * Prepare invalid document DTO.
//     *
//     * @return the document DTO
//     */
//    private DocumentDTO prepareInvalidDocumentDTO() {
//        return null;
//    }
//
//    /**
//     * Prepare document entity.
//     *
//     * @return the document entity
//     */
//    private DocumentEntity prepareDocumentEntity() {
//        DocumentEntity documentEntity = new DocumentEntity();
//        documentEntity.setId( UUID.randomUUID() );
//        documentEntity.setName( TEST_META_DATA_FILE );
//        documentEntity.setOwner( new UserEntity( USER_ID ) );
//        documentEntity.setFileName( ConstantsString.TEST_RESOURCE_PATH + TEST_META_DATA_FILE );
//        return documentEntity;
//    }
//
//    /**
//     * Gets the permitted true.
//     *
//     * @return the permitted true
//     */
//    private void getPermittedTrue() {
//        EasyMock.expect( permissionManager.isPermitted( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( true ).anyTimes();
//    }
//
//    /**
//     * Mock access control entities.
//     */
//    private void mockAccessControlEntities() {
//        EasyMock.expect( permissionManager.getEntryDAO() ).andReturn( aclEntryDAO ).anyTimes();
//        EasyMock.expect(
//                        aclEntryDAO.getAclEntryListByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
//                .andReturn( aclEntryEntities ).anyTimes();
//    }
//
//    /**
//     * Gets the filter DTO for deleting meta data by selection .
//     *
//     * @return the filter DTO for deleting meta data by selection
//     */
//    private FiltersDTO getFilterDTOForDeletingMetaDataBySelection() {
//        List< Object > uuids = new ArrayList<>();
//        uuids.add( META_DATA_KEY );
//        uuids.add( "3" );
//        FiltersDTO filtersDTO = new FiltersDTO();
//        filtersDTO.setItems( uuids );
//        return filtersDTO;
//    }
//
//    /**
//     * Gets the filter DTO for restoring object by selection.
//     *
//     * @return the filter DTO for restoring object by selection
//     */
//    private FiltersDTO getFilterDTOForRestoringObjectBySelection() {
//        List< Object > uuids = new ArrayList<>();
//        uuids.add( OBJECT_ID );
//        FiltersDTO filtersDTO = new FiltersDTO();
//        filtersDTO.setItems( uuids );
//        return filtersDTO;
//    }
//
//    /**
//     * Gets the data object entity.
//     *
//     * @return the data object entity
//     */
//    private SuSEntity getDataObjectEntity() {
//        SuSEntity suSEntity = new DataObjectEntity();
//        suSEntity.setComposedId( new VersionPrimaryKey( OBJECT_ID, 1 ) );
//        suSEntity.setLifeCycleStatus( WIP_ID );
//        suSEntity.setName( ConstantsString.EMPTY_STRING );
//        suSEntity.setOwner( new UserEntity( OBJECT_IDENTITY_ID ) );
//        return suSEntity;
//    }
//
//    /**
//     * Gets the user.
//     *
//     * @return the user
//     */
//    private UserDTO getUser() {
//        user = new UserDTO();
//        user.setSurName( SUPER_USER_NAME );
//        user.setFirstName( SUPER_USER_NAME );
//        return user;
//    }
//
//    /**
//     * Fill custom attribute entity.
//     *
//     * @return the custom attribute entity
//     */
//    private CustomAttributeEntity fillCustomAttributeEntity() {
//        CustomAttributeEntity customAttributeEntity = new CustomAttributeEntity();
//        List< String > options = new ArrayList<>();
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_1 );
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_2 );
//        options.add( DUMMY_CUSTOM_ATTRIBUTE_3 );
//        customAttributeEntity.setName( TEST_CUSTOM_ATTRIBUTE );
//        customAttributeEntity.setType( TEST_CUSTOM_ATTRIBUTE );
//
//        customAttributeEntity.setValue( TEST_CUSTOM_ATTRIBUTE_BYTE );
//        customAttributeEntity.setType( UIColumnType.SELECT.getKey() );
//
//        return customAttributeEntity;
//
//    }
//
//    /**
//     * Adds the custom attribute to table.
//     *
//     * @return the table column
//     */
//    private TableColumn addCustomAttributeToTable() {
//        TableColumn customAttribute = new TableColumn();
//        customAttribute.setData( "customAttributes.test1" );
//        customAttribute.setName( "test1" );
//        customAttribute.setOrderNum( 0 );
//        return customAttribute;
//    }
//
//    /**
//     * Prepare mock get data object methods.
//     *
//     * @param expected
//     *         the expected
//     */
//    private void prepareMockGetDataObjectMethods( SuSEntity expected ) {
//
//        expected.setConfig( PROJECT_CONFIG_FILE_NAME );
//        expected.setTypeId( OBJECT_TYPE_ID );
//        EasyMock.expect( lifeCycleManager.getOwnerVisibleStatusByPolicyId( EasyMock.anyString() ) )
//                .andReturn( Arrays.asList( "553536c7-71ec-409d-8f48-ec779a98a68e", "d762f4ef-e706-4a44-a46d-6b334745e2e5",
//                        "29d94aa2-62f2-4add-9233-2f4781545c35" ) )
//                .anyTimes();
//        EasyMock.expect( lifeCycleManager.getAnyVisibleStatusByPolicyId( EasyMock.anyString() ) )
//                .andReturn( Arrays.asList( "d762f4ef-e706-4a44-a46d-6b334745e2e5", "29d94aa2-62f2-4add-9233-2f4781545c35" ) ).anyTimes();
//        EasyMock.expect( dao.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
//                .andReturn( expected ).anyTimes();
//
//        SuSObjectModel model = getFilledSuSObjectTypeDataObject();
//        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
//                .anyTimes();
//
//        EasyMock.expect( dao.getLatestObjectByIdWithLifeCycle( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
//                EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( expected ).anyTimes();
//
//    }
//
//    /**
//     * Fill data object curve dto.
//     *
//     * @return the data object curve DTO
//     */
//    private DataObjectCurveDTO fillDataObjectCurveDto() {
//        DataObjectCurveDTO document = new DataObjectCurveDTO();
//        document.setName( DATA_OBJECT_NAME );
//        document.setxDimension( X_DIMENSION );
//        document.setyDimension( Y_DIMENSION );
//        document.setxUnit( X_UNIT );
//        document.setyUnit( Y_UNIT );
//        List< double[] > curveList = new ArrayList<>();
//        double[] m = new double[ ConstantsInteger.INTEGER_VALUE_FOUR ];
//        curveList.add( m );
//        document.setCurve( curveList );
//        document.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        return document;
//    }
//
//    /**
//     * Fill data object moview dto.
//     *
//     * @return the data object movie DTO
//     */
//    private DataObjectMovieDTO fillDataObjectMoviewDto() {
//        DataObjectMovieDTO movieDto = new DataObjectMovieDTO();
//        movieDto.setId( DATA_OBJECT_ID );
//        movieDto.setName( DATA_OBJECT_NAME );
//        movieDto.setPoster( STATIC_PATH + POSTER );
//        movieDto.setThumbnail( STATIC_PATH + THUMB_NAIL );
//        MovieSources sources = new MovieSources();
//        sources.setMp4( MP4 );
//        sources.setWebm( WEBM );
//        movieDto.setSources( sources );
//        movieDto.setVersion( new VersionDTO( ConstantsInteger.INTEGER_VALUE_ONE ) );
//        return movieDto;
//    }
//
//    /**
//     * Gets the location manager.
//     *
//     * @return the location manager
//     */
//    public LocationManager getLocationManager() {
//        return locationManager;
//    }
//
//    /**
//     * Sets the location manager.
//     *
//     * @param locationManager
//     *         the new location manager
//     */
//    public void setLocationManager( LocationManager locationManager ) {
//        this.locationManager = locationManager;
//    }

}
