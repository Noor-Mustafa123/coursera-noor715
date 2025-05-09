package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantRequestHeader;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileExtension;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsImageFileTypes;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsProjectOverview;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUrlViews;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.LocationsEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SelectionOrigins;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.UIColumnType;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.formitem.impl.TableFormItem;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbDTO;
import de.soco.software.simuspace.suscore.common.model.BreadCrumbItemDTO;
import de.soco.software.simuspace.suscore.common.model.CommonLocationDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.DummyTypeDTO;
import de.soco.software.simuspace.suscore.common.model.FileInfo;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.SyncFileDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
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
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.PaginationUtil;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;
import de.soco.software.simuspace.suscore.common.util.SearchUtil;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.TranslationDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
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
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.manager.impl.base.ContextMenuManagerImpl;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectDashboardDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectFileDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectHtmlDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectImageDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectMovieDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectPredictionModelDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectTraceDTO;
import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;
import de.soco.software.simuspace.suscore.data.model.LibraryDTO;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.model.ReportDTO;
import de.soco.software.simuspace.suscore.data.model.SusDTO;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.suscore.data.model.WorkFlowDTOConf;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.document.manager.DocumentManager;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.location.manager.LocationManager;
import de.soco.software.simuspace.suscore.object.dao.DataDAO;
import de.soco.software.simuspace.suscore.object.manager.BreadCrumbManager;
import de.soco.software.simuspace.suscore.object.manager.DataDashboardManager;
import de.soco.software.simuspace.suscore.object.manager.DataProjectManager;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectBaseManager;
import de.soco.software.simuspace.suscore.object.model.ProjectType;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectServiceEndPoints;
import de.soco.software.simuspace.suscore.object.utility.ConstantsObjectTypes;
import de.soco.software.simuspace.suscore.object.utility.OverviewPluginUtil;
import de.soco.software.simuspace.suscore.permissions.dao.AclEntryDAO;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.suscore.permissions.model.PermissionDTO;
import de.soco.software.simuspace.suscore.permissions.model.ResourceAccessControlDTO;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

@Log4j2
public class DataProjectManagerImpl extends SuSObjectBaseManager implements DataProjectManager {

    /**
     * The Constant DATA.
     */
    private static final String DATA = "data";

    /**
     * The Constant PERMISSIONS.
     */
    private static final String PERMISSIONS = "permissions";

    /**
     * The Constant VERSIONS.
     */
    private static final String VERSIONS = "versions";

    /**
     * The Constant SUBPROJECT.
     */
    private static final String SUBPROJECT = "subproject";

    /**
     * The Constant METADATA.
     */
    private static final String METADATA = "metadata";

    /**
     * The Constant PROPERTIES.
     */
    private static final String PROPERTIES = "properties";

    /**
     * The Constant AUDIT.
     */
    private static final String AUDIT = "audit";

    /**
     * The Constant LABEL.
     */
    private static final String LABEL = "Label";

    /**
     * The Constant MUST_CHOSE_OPTION.
     */
    private static final String MUST_CHOSE_OPTION = "Must Chose Option";

    /**
     * The Constant REQUIRED.
     */
    private static final String REQUIRED = "required";

    /**
     * The Constant LIVESEARCH.
     */
    private static final String LIVESEARCH = "liveSearch";

    /**
     * The Constant CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN.
     */
    private static final String CUSTOM_ATTRIBUTE_DEFAULT_VALUE_COLUMN = "value";

    /**
     * The Constant CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE.
     */
    private static final String CUSTOM_ATTRIBUTE_FIELD_TYPE_FOR_TABLE = "text";

    /**
     * The Constant LIST_FIRST_INDEX.
     */
    private static final int LIST_FIRST_INDEX = 0;

    /**
     * The Constant CUSTOM_NAME.
     */
    private static final String CUSTOM_NAME = "createOption";

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
     * The Constant BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM.
     */
    public static final String BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM = "/data/object/ui/create/{parentId}/type/{__value__}";

    /**
     * The Constant PARAM_PROJECT_ID.
     */
    private static final String PARAM_PROJECT_ID = "{projectId}";

    /**
     * The Constant FIELD_NAME_OBJECT_STATUS.
     */
    public static final String FIELD_NAME_OBJECT_STATUS = "status";

    /**
     * The Constant BIND_FROM_URL_FOR_PROJECT_PERMISSION.
     */
    private static final String BIND_FROM_URL_FOR_PROJECT_PERMISSION = "/data/project/{projectId}/permission/fields/{__value__}";

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
     * The Constant OBJECT.
     */
    private static final String OBJECT = "Object";

    /**
     * The Constant GROUP_SELECTION_PATH.
     */
    private static final String GROUP_SELECTION_PATH = "system/permissions/group";

    /**
     * The Constant PLUGIN_OBJECT.
     */
    private static final String PLUGIN_OBJECT = "plugin_object";

    /**
     * The Constant OBJECT_TYPE_LABEL.
     */
    private static final String OBJECT_TYPE_LABEL = "3000212x4";

    /**
     * The Constant INCLUDE_LABEL.
     */
    public static final String INCLUDE_LABEL = "Include Sub-Objects";

    /**
     * The Constant PROJECT.
     */
    private static final String PROJECT = "Project";

    /**
     * The Constant BIND_FROM_URL_FOR_OBJECT_PERMISSION.
     */
    private static final String BIND_FROM_URL_FOR_OBJECT_PERMISSION = "/data/object/{objectId}/permission/fields/{__value__}";

    /**
     * The Constant USER_SELECTION_PATH.
     */
    private static final String USER_SELECTION_PATH = "system/user";

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
     * The Constant SELECT_PERMISSION_LABEL.
     */
    private static final String SELECT_PERMISSION_LABEL = "4100112x4";

    /**
     * The Constant SET_INHERITED_LABEL.
     */
    private static final String SET_INHERITED_LABEL = "4100117x4";

    /**
     * The Constant UNSET_INHERITED_LABEL.
     */
    private static final String UNSET_INHERITED_LABEL = "4100118x4";

    /**
     * The Constant SELECTION.
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
     * The Constant ABORT_CONTEXT_URL.
     */
    private static final String ABORT_CONTEXT_URL = "put/data/project/abort/{selectionId}";

    /**
     * The Constant LOCAL_CONTEXT_URL.
     */
    private static final String LOCAL_CONTEXT_URL = "put/data/project/{projectId}/sync/{selectionId}";

    /**
     * The Constant CHANGE_TYPE_CONTEXT_URL.
     */
    private static final String CHANGE_TYPE_CONTEXT_URL = "data/project/{projectId}/selection/{selectionId}/changetype";

    /**
     * The Constant BULK_CURVE_COMPARE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_CURVE_COMPARE_OBJECT_CONTEXT_URL = "view/data/curvecompare/{selectionId}";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "{selectionId}";

    /**
     * The Constant TITLE_UPLOAD.
     */
    private static final String TITLE_UPLOAD_SYNC = "Sync Upload";

    /**
     * The Constant ABORT_SYNCH.
     */
    private static final String ABORT_SYNCH = "Sync Abort";

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
     * The Constant BMW_DUMMY_PROJECT_JSON.
     */
    private static final String BMW_DUMMY_PROJECT_JSON = "BMWDummyProject.json";

    /**
     * The Constant DISCARD_LOCAL_FILE.
     */
    private static final String DISCARD_LOCAL_FILE = "4100025x4";

    /**
     * The Constant API_DOCUMENT_UPLOAD.
     */
    private static final String API_DOCUMENT_UPLOAD = "api/document/upload";

    /**
     * The constant BMW_QADYNA_PROJECT_JSON.
     */
    private static final String BMW_QADYNA_PROJECT_JSON = "BMWQADynaProject.json";

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
     * The context selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private LinkDAO linkDao;

    /**
     * The location manager.
     */
    private LocationManager locationManager;

    /**
     * The bread crumb manager.
     */
    private BreadCrumbManager breadCrumbManager;

    /**
     * The Data dashboard manager.
     */
    private DataDashboardManager dataDashboardManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm createProjectForm( String token, UUID parentId ) {
        log.debug( "entered in createProjectForm method" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > uiFormItemList;

            SuSEntity susEntity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, parentId );
            entityManager.close();
            final SuSObjectModel suSObjectModel = configManager.getProjectModelByConfigLabel(
                    ConfigUtil.labelNames( configManager.getMasterConfigurationFileNames() ).get( ConstantsInteger.INTEGER_VALUE_ZERO ) );

            boolean isWorkflowProject = false;
            if ( parentId.equals( UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) )
                    || susEntity instanceof WorkflowProjectEntity ) {
                uiFormItemList = GUIUtils.prepareForm( true, new WorkflowProjectDTO() );
                isWorkflowProject = true;
            } else {
                uiFormItemList = GUIUtils.prepareForm( true, new ProjectDTO() );
            }

            if ( PropertiesManager.hasTranslation() ) {
                createTranslationFieldsUI( parentId.toString(), suSObjectModel.getId(), uiFormItemList );

            }

            for ( UIFormItem uiFormItem : uiFormItemList ) {
                switch ( uiFormItem.getName() ) {
                    case ProjectDTO.UI_COLUMN_NAME_CONFIG -> {
                        populateConfigDropdownOptions( uiFormItem, isWorkflowProject,
                                SimuspaceFeaturesEnum.DATA.getId().equals( parentId.toString() ) );
                        setRulesAndMessageOnUI( uiFormItem );
                        setLiveSearch( ( SelectFormItem ) uiFormItem );
                    }
                    case ProjectDTO.UI_COLUMN_NAME_PARENT_ID -> uiFormItem.setValue( parentId );
                    case ProjectDTO.UI_COLUMN_NAME_TYPE_ID -> uiFormItem.setValue( UUID.fromString( suSObjectModel.getId() ) );
                    case ProjectDTO.UI_COLUMN_NAME_TYPE -> {
                        Map< String, String > map = new HashMap<>();
                        ProjectType[] projectTypes = ProjectType.values();
                        for ( ProjectType projectType : projectTypes ) {
                            map.put( projectType.getKey(), projectType.getKey() );
                        }
                        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( map ),
                                ProjectType.DATA.getKey(), false );
                        setRulesAndMessageOnUI( uiFormItem );
                    }
                    case ProjectDTO.UI_COLUMN_NAME_STATUS ->
                            GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getStatusSelectObjectOptions(),
                                    ConstantsStatus.ACTIVE, false );
                }
            }
            log.debug( "leaving createProjectForm method" );
            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * Sets the liveSearch to true.
     *
     * @param selectFormItemItem
     *         the selectUiItem
     */
    private void setLiveSearch( SelectFormItem selectFormItemItem ) {
        Map< String, Object > liveSearch = new HashMap<>();
        liveSearch.put( LIVESEARCH, true );
        selectFormItemItem.setPicker( liveSearch );
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
     * @param setBindUrlForCustomAttributes
     *         the set bind url for custom attributes
     */
    private void populateConfigDropdownOptions( UIFormItem uiFormItem, boolean isWorkflowProject, boolean setBindUrlForCustomAttributes ) {
        Map< String, String > map = new HashMap<>();
        List< ConfigUtil.Config > validOptions;

        if ( isWorkflowProject ) {
            validOptions = configManager.getMasterConfigurationFileNamesForWorkFlows();
        } else {
            validOptions = configManager.getMasterConfigurationFileNames();
        }

        if ( CollectionUtil.isNotEmpty( validOptions ) ) {
            for ( ConfigUtil.Config op : validOptions ) {
                map.put( op.file(), op.label() );
            }
            SelectFormItem selectConfig = GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem,
                    GUIUtils.getSelectBoxOptions( map ), ConfigUtil.fileNames( validOptions ).get( LIST_FIRST_INDEX ), false );
            if ( setBindUrlForCustomAttributes ) {
                selectConfig.setBindFrom( BIND_FROM_URL_FOR_PROJECT_CUSTOMATTRIBUTE_VALUE );
            }
            selectConfig.setReadonly( false );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UIForm editProjectForm( String userId, String projectId ) {
        log.debug( "entered in editProjectForm method" );
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( StringUtils.isEmpty( projectId ) || !ValidationUtils.validateUUIDString( projectId ) ) {
                throw new SusException(
                        MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ProjectDTO.PROJECT_ID ) );
            }

            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( projectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), projectId ) );
            }
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                    entity.getConfig() );

            Object filledObject = createDTOFromSusEntity( entityManager, null, entity, true );
            List< UIFormItem > uiFormItemList = GUIUtils.prepareForm( false, filledObject );
            for ( UIFormItem uiFormItem : uiFormItemList ) {
                switch ( uiFormItem.getName() ) {
                    case FIELD_NAME_OBJECT_TYPE_ID -> uiFormItem.setValue( entity.getTypeId() );
                    case ProjectDTO.UI_COLUMN_NAME_CONFIG -> {
                        Map< String, String > projectConfigMap = new HashMap<>();
                        projectConfigMap.put( entity.getConfig(), entity.getConfig() );
                        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( projectConfigMap ),
                                entity.getConfig(), false );
                    }
                    case ProjectDTO.UI_COLUMN_NAME_TYPE -> {
                        ProjectEntity pEntity = ( ProjectEntity ) entity;
                        Map< String, String > projectTypeMap = new HashMap<>();
                        projectTypeMap.put( pEntity.getType(), pEntity.getType() );
                        GUIUtils.updateSelectUIFormItem( ( SelectFormItem ) uiFormItem, GUIUtils.getSelectBoxOptions( projectTypeMap ),
                                pEntity.getType(), false );
                    }
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

            log.debug( "leaving editProjectForm method" );

            return GUIUtils.createFormFromItems( uiFormItemList );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO createSuSProject( String userId, String projectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity user = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
            return createProject( entityManager, user, projectJson );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO createProject( EntityManager entityManager, UserEntity userEntity, String projectJson ) {
        ProjectDTO projectDTO = JsonUtils.jsonToObject( projectJson, ProjectDTO.class );
        if ( !SimuspaceFeaturesEnum.DATA.getId().equals( String.valueOf( projectDTO.getParentId() ) ) && !permissionManager.isPermitted(
                entityManager, userEntity.getId().toString(),
                projectDTO.getParentId() + ConstantsString.COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_CREATE.getKey(), projectDTO.getName() ) );
        }
        ProjectEntity createdEntity;
        Notification notif = projectDTO.validate();
        if ( notif.hasErrors() ) {
            throw new SusException( notif.getErrors().toString() );
        }
        if ( de.soco.software.simuspace.suscore.common.util.StringUtils.isGlobalNamingConventionFollowed( projectDTO.getName() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NAME_SHOULD_NOT_HAVE_SPECIAL_CHARACTER.getKey() ) );
        }
        SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectDTO.getParentId() );
        if ( !isNameUniqueAmongSiblings( entityManager, projectDTO.getName(), null, parentEntity ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
        }
        SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( projectDTO.getTypeId().toString(),
                ConfigUtil.labelByFile( configManager.getMasterConfigurationFileNames(), projectDTO.getConfig() ) );
        projectDTO.setCustomAttributesDTO( suSObjectModel.getCustomAttributes() );
        createdEntity = projectDTO.prepareEntity( userEntity.getId().toString() );

        createdEntity.setConfig( ConfigUtil.labelByFile( configManager.getMasterConfigurationFileNames(), projectDTO.getConfig() ) );
        createdEntity.setCreatedBy( userEntity );
        createdEntity.setModifiedBy( userEntity );
        createdEntity.setDescription( projectDTO.getDescription() );
        createdEntity.setSize( org.apache.commons.lang3.StringUtils.isBlank( projectDTO.getSize() ) ? ConstantsInteger.INTEGER_VALUE_ZERO
                : Long.parseLong( projectDTO.getSize() ) );
        createdEntity.setIcon( suSObjectModel.getIcon() );
        createdEntity.setTranslation( setMultiNames( entityManager, projectJson ) );
        try {
            createdEntity.setSelectedTranslations(
                    JsonUtils.toJson( new ObjectMapper().readTree( projectJson ).path( SELECTED_TRANSLATIONS ) ) );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        if ( createdEntity.getType().equalsIgnoreCase( ProjectType.LABEL.getKey() ) ) {
            createdEntity.setIcon( PropertiesManager.getProjectLabelIcon() );
        }
        StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( projectDTO.getTypeId().toString(), createdEntity.getConfig() );
        createdEntity.setLifeCycleStatus( dto.getId() );
        // audit log
        if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
            createdEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                    projectDTO.getName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CREATED, ConstantsDbOperationTypes.CREATED,
                    userEntity.getId().toString(), createdEntity, suSObjectModel.getName() ) );
        }

        createdEntity.setPath( populatePath( entityManager, userEntity.getId().toString(), parentEntity ) );
        createdEntity = ( ProjectEntity ) susDAO.createAnObject( entityManager, createdEntity );
        Relation relation = new Relation( parentEntity.getComposedId().getId(), createdEntity.getComposedId().getId() );
        susDAO.createRelation( entityManager, relation );
        addToAcl( entityManager, userEntity.getId().toString(), createdEntity, parentEntity );
        saveOrUpdateIndexEntity( entityManager, suSObjectModel, createdEntity );
        selectionManager.sendCustomerEventOnCreate( entityManager, userEntity.getId().toString(), createdEntity, parentEntity );

        log.debug( "leaving createProject method" );

        return createProjectDTOFromProjectEntity( createdEntity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object updateProject( String userId, String projectJson ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ProjectDTO projectDTO = JsonUtils.jsonToObject( projectJson, ProjectDTO.class );
            SuSEntity existingEntity;
            SuSEntity updateEntity = null;
            List< SuSEntity > parents = new ArrayList<>();
            if ( projectDTO != null ) {
                existingEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectDTO.getId() );
                updateEntity = prepareRelevantEntityFromProjectDTO( userId, projectDTO, existingEntity );
                isPermittedToUpdate( entityManager, userId, projectDTO.getId().toString() );
                Notification notif = projectDTO.validate();
                if ( notif.hasErrors() ) {
                    throw new SusException( notif.getErrors().toString() );
                }

                if ( null == existingEntity ) {
                    throw new SusException(
                            MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), projectDTO.getId() ) );
                }
                prepareCommonAttributesForUpdateEntity( existingEntity, updateEntity );
                if ( !isLifeCycleStatusAllowed( updateEntity ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.OBJECT_NOT_UPDATED.getKey(), projectDTO.getId() ) );
                }

                parents = susDAO.getParents( entityManager,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectDTO.getId() ) );

                if ( CollectionUtils.isNotEmpty( parents ) && !isNameUniqueAmongSiblings( entityManager, projectDTO.getName(), updateEntity,
                        susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                                parents.get( LIST_FIRST_INDEX ).getComposedId().getId() ) ) ) {
                    throw new SusException( MessageBundleFactory.getMessage( Messages.CONTAINER_CANNOT_CONTAIN_SAME_NAME.getKey() ) );
                }

                SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( updateEntity.getTypeId().toString(),
                        updateEntity.getConfig() );

                projectDTO.setCustomAttributesDTO(
                        configManager.getObjectTypeByIdAndConfigName( updateEntity.getTypeId().toString(), updateEntity.getConfig() )
                                .getCustomAttributes() );
                updateEntity.setCustomAttributes( SusDTO.prepareCustomAttributes( updateEntity, projectDTO.getCustomAttributes(),
                        suSObjectModel.getCustomAttributes() ) );
                updateEntity.setModifiedOn( new Date() );
                updateEntity.setName( projectDTO.getName() );
                updateEntity.setDescription( projectDTO.getDescription() );
                updateEntity.setModifiedBy( userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) ) );
                updateEntity.setTranslation( setMultiNames( entityManager, projectJson ) );
                try {
                    updateEntity.setSelectedTranslations(
                            JsonUtils.toJson( new ObjectMapper().readTree( projectJson ).path( SELECTED_TRANSLATIONS ) ) );
                } catch ( IOException e ) {
                    log.error( e.getMessage(), e );
                    throw new SusException( e.getMessage() );
                }

                // audit log
                ContainerEntity oldEntity = ( ContainerEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        projectDTO.getId() );
                if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
                    AuditLogEntity auditLog = AuditLogDTO.prepareAuditLogEntityForUpdatedObjects( userId, oldEntity, updateEntity,
                            updateEntity.getComposedId().getId().toString(), updateEntity.getName(), suSObjectModel.getName() );
                    if ( null != auditLog ) {
                        auditLog.setObjectId( oldEntity.getComposedId().getId().toString() );
                    }
                    updateEntity.setAuditLogEntity( auditLog );
                }

                StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( updateEntity.getTypeId().toString(),
                        updateEntity.getConfig() );
                updateEntity.setLifeCycleStatus( dto.getId() );
                changeStatusInVersionsRecursively( entityManager, updateEntity );
                updateEntity = susDAO.updateAnObject( entityManager, updateEntity );
                saveOrUpdateIndexEntity( entityManager, suSObjectModel, updateEntity );
                selectionManager.sendCustomerEvent( entityManager, userId, updateEntity, UPDATE );
            }

            if ( parents.isEmpty() ) {
                return null;
            }

            return createDTOFromSusEntity( entityManager,
                    UUID.fromString( parents.get( LIST_FIRST_INDEX ).getComposedId().getId().toString() ), updateEntity, true );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare relevant entity from project dto su s entity.
     *
     * @param userId
     *         the user id
     * @param projectDTO
     *         the project dto
     * @param existingEntity
     *         the existing entity
     *
     * @return the su s entity
     */
    private SuSEntity prepareRelevantEntityFromProjectDTO( String userId, ProjectDTO projectDTO, SuSEntity existingEntity ) {
        SuSEntity updateEntity;
        if ( existingEntity instanceof VariantEntity ) {
            updateEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( VariantDTO.prepareFromProjectDTO( projectDTO ), userId );
        } else if ( existingEntity instanceof LibraryEntity ) {
            updateEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( LibraryDTO.prepareFromProjectDTO( projectDTO ), userId );
        } else {
            updateEntity = ( SuSEntity ) ReflectionUtils.invokePrepareEntity( projectDTO, userId );
        }
        return updateEntity;
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
        selectFormItem.setBindFrom(
                BIND_FROM_URL_FOR_OBJECT_TRANSLATION_CREATE.replace( PARAM_OBJECT_ID, parentId ).replace( PARAM_TYPE_ID, typeId ) );
        uiFormItemList.add( selectFormItem );
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
        uiFormItemList.add( selectFormItem );
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
     * Sets the path.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param createdEntity
     *         the created entity
     *
     * @return the string
     */
    private String populatePath( EntityManager entityManager, String userId, SuSEntity createdEntity ) {

        StringBuilder url = new StringBuilder();
        if ( createdEntity.getComposedId().getId().toString().equalsIgnoreCase( SimuspaceFeaturesEnum.DATA.getId() ) ) {
            return ConstantsString.FORWARD_SLASH + SimuspaceFeaturesEnum.DATA.getKey();
        } else {
            BreadCrumbDTO breadCrumb = breadCrumbManager.createBreadCrumb( entityManager, userId,
                    createdEntity.getComposedId().getId().toString(), null );
            for ( BreadCrumbItemDTO breadCrumbItem : breadCrumb.getBreadCrumbItems() ) {
                url.append( ConstantsString.FORWARD_SLASH ).append( breadCrumbItem.getName() );
            }
        }
        return url.toString();
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
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO getProject( String userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getProject( entityManager, projectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProjectDTO getProject( EntityManager entityManager, UUID projectId ) {
        ProjectDTO projectDTO = null;
        if ( projectId != null ) {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, ContainerEntity.class, projectId );
            if ( susEntity != null ) {
                projectDTO = new ProjectDTO();
                projectDTO.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
                projectDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
                projectDTO.setName( susEntity.getName() );
            }
        }
        return projectDTO;

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
    public TableUI getListOfObjectsUITableColumns( String userIdFromGeneralHeader, String objectId, String typeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getListOfObjectsUITableColumns( entityManager, userIdFromGeneralHeader, objectId, typeId );
        } finally {
            entityManager.close();
        }
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
            isPermittedToRead( entityManager, userIdFromGeneralHeader, objectId );
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
    public List< TableColumn > listDataUI( String userId, String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( latestEntity != null ) {
                if ( !( latestEntity instanceof SystemContainerEntity ) ) {
                    isPermittedToRead( entityManager, userId, objectId );
                } else {
                    if ( latestEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.LOADCASES.getId() ) ) {
                        return GUIUtils.listColumns( LoadCaseDTO.class );
                    }
                }
            }

            return GUIUtils.listColumns( GenericDTO.class );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAllObjects( String userId, UUID parentId, FiltersDTO filter, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getAllObjects( entityManager, userId, parentId, filter, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAllObjects( EntityManager entityManager, String userId, UUID parentId, FiltersDTO filter,
            String token ) {
        if ( filter == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        }
        filter.setTypeClass( null );

        SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, parentId );
        List< SuSEntity > suSEntities = new ArrayList<>();
        List< Object > objectDTOList = new ArrayList<>();
        if ( latestEntity instanceof SystemContainerEntity ) {
            String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName( ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME )
                    .get( 0 ).getId();
            // start hack for showing loadcases in selection on run-variant.
            if ( ( null == filter.getTypeClass() || filter.getTypeClass().isEmpty() ) ) {
                if ( latestEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.LOADCASES.getId() ) ) {
                    userId = ConstantsID.SUPER_USER_ID;
                    objectDTOList = getLoadcasesForSelection( entityManager, userId, parentId, filter, latestEntity.getConfig() );
                    return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
                }
                suSEntities = prepareEntities( entityManager, userId, parentId, filter, latestEntity, lifecycleId,
                        CommonUtils.resolveLanguage( token ) );
            } else {
                if ( latestEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.LOADCASES.getId() ) ) {
                    userId = ConstantsID.SUPER_USER_ID;
                }
                suSEntities = getFilteredRecordsByTypeClasses( entityManager, userId, parentId, filter, lifecycleId, latestEntity );
            }
        } else if ( latestEntity != null ) {
            SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );
            if ( null == filter.getTypeClass() || filter.getTypeClass().isEmpty() ) {
                String language = CommonUtils.resolveLanguage( token );
                suSEntities = ( List< SuSEntity > ) susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndLanguageAndPermissionForList(
                        entityManager, SuSEntity.class, filter, parentId, userId,
                        lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                        configManager.getTypesFromConfiguration( latestEntity.getConfig() ), language, PermissionMatrixEnum.VIEW.getKey() );
            } else {
                suSEntities = getFilteredRecordsByTypeClasses( entityManager, userId, parentId, filter, entityModel.getLifeCycle(),
                        latestEntity );
            }
        }

        populateGenericDTO( entityManager, userId, parentId, suSEntities, objectDTOList );
        return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
    }

    /**
     * Prepare entities.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param latestEntity
     *         the latest entity
     * @param lifecycleId
     *         the lifecycle id
     *
     * @return the list
     */
    private List< SuSEntity > prepareEntities( EntityManager entityManager, String userId, UUID parentId, FiltersDTO filter,
            SuSEntity latestEntity, String lifecycleId, String language ) {
        List< SuSEntity > suSEntities;
        if ( null == latestEntity.getConfig() ) {
            suSEntities = ( List< SuSEntity > ) susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndLanguageAndPermissionForList(
                    entityManager, SuSEntity.class, filter, parentId, userId,
                    lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ), configManager.getAllTypesFromConfiguration(), language,
                    PermissionMatrixEnum.VIEW.getKey() );
        } else {
            suSEntities = ( List< SuSEntity > ) susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndLanguageAndPermissionForList(
                    entityManager, SuSEntity.class, filter, parentId, userId,
                    lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ),
                    configManager.getTypesFromConfiguration( latestEntity.getConfig() ), language, PermissionMatrixEnum.VIEW.getKey() );
        }
        return suSEntities;
    }

    /**
     * Gets Filtered Records by Type classes.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param lifeCycleId
     *         the life cycle id
     * @param latestEntity
     *         the latestEntity
     *
     * @return the filtered records
     */
    private List< SuSEntity > getFilteredRecordsByTypeClasses( EntityManager entityManager, String userId, UUID parentId, FiltersDTO filter,
            String lifeCycleId, SuSEntity latestEntity ) {
        List< SuSEntity > suSEntities = new ArrayList<>();
        FiltersDTO filterForTypeClass = getFilterForTypeClassRecords( filter );
        long filteredRecords = 0;
        long totalRecords = 0;
        for ( String type : filterForTypeClass.getTypeClass() ) {
            if ( null == latestEntity.getConfig() ) {
                suSEntities.addAll( susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager,
                        ReflectionUtils.getFieldByName( initializeObject( type ).getClass(), ENTITY_CLASS_FIELD_NAME ).getType(),
                        filterForTypeClass, parentId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifeCycleId ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( lifeCycleId ), configManager.getAllTypesFromConfiguration() ) );
            } else {
                suSEntities.addAll( susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager,
                        ReflectionUtils.getFieldByName( initializeObject( type ).getClass(), ENTITY_CLASS_FIELD_NAME ).getType(),
                        filterForTypeClass, parentId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifeCycleId ),
                        lifeCycleManager.getAnyVisibleStatusByPolicyId( lifeCycleId ),
                        configManager.getTypesFromConfiguration( latestEntity.getConfig() ) ) );
            }
            filteredRecords += filterForTypeClass.getFilteredRecords();
            totalRecords += filterForTypeClass.getTotalRecords();
        }
        filter.setFilteredRecords( filteredRecords );
        filter.setTotalRecords( totalRecords );
        return PaginationUtil.getPaginatedList( suSEntities, filter );
    }

    /**
     * Gets the filter for type class records.
     *
     * @param filter
     *         the filter
     *
     * @return the list of loadcaseDTO
     */
    private FiltersDTO getFilterForTypeClassRecords( FiltersDTO filter ) {
        FiltersDTO filterTypeClass = new FiltersDTO();
        filterTypeClass.setStart( 0 ); // To get all records of typeclass and paginate later
        filterTypeClass.setLength( filter.getStart() + filter.getLength() );

        filterTypeClass.setColumns( filter.getColumns() );
        filterTypeClass.setDraw( filter.getDraw() );
        filterTypeClass.setItems( filter.getItems() );
        filterTypeClass.setSearch( filter.getSearch() );
        filterTypeClass.setSearchId( filter.getSearchId() );
        filterTypeClass.setSearchIn( filter.getSearchIn() );
        filterTypeClass.setType( filter.getType() );
        filterTypeClass.setTypeClass( filter.getTypeClass() );

        return filterTypeClass;
    }

    /**
     * Gets the list of LoadcaseDTO for selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     * @param config
     *         the config
     *
     * @return the list of loadcaseDTO
     */
    private List< Object > getLoadcasesForSelection( EntityManager entityManager, String userId, UUID parentId, FiltersDTO filter,
            String config ) {
        List< Object > loadcaseDTOList = new ArrayList<>();

        String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName( ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME ).get( 0 )
                .getId();
        List< SuSEntity > suSEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, SuSEntity.class, filter,
                parentId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId ),
                lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId ), configManager.getTypesFromConfiguration( config ) );

        for ( SuSEntity suSEntity : suSEntities ) {
            loadcaseDTOList.add( createLoadcaseDTOFromEntity( suSEntity ) );
        }

        return loadcaseDTOList;
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
     * Extract columns from container.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param objectDTOList
     *         the object DTO list
     * @param suSEntities
     *         the su S entities
     * @param userId
     *         the user id
     */
    private void extractColumnsFromContainer( EntityManager entityManager, UUID projectId, List< Object > objectDTOList,
            List< SuSEntity > suSEntities, String userId ) {
        if ( CollectionUtil.isNotEmpty( suSEntities ) ) {
            for ( SuSEntity suSEntity : suSEntities ) {
                log.info( ">>extractColumnsFromContainer >>entity" + suSEntity.getName() );
                objectDTOList.add( createGenericDTOFromObjectEntity( entityManager, userId, projectId, suSEntity,
                        suSEntity.getTranslation().stream().toList(), true ) );
                log.info( ">>extractColumnsFromContainer <<for" + suSEntity.getName() );
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
    public UIForm getProjectCustomAttributeUI( String userId, String projectType ) {

        log.debug( "entered in getProjectCustomAttributeUI method" );
        SuSObjectModel suSObjectModel = configManager.getProjectModelByConfigLabel(
                ConfigUtil.labelByFile( configManager.getMasterConfigurationFileNames(), projectType ) );
        List< UIFormItem > uiFormItemList = new ArrayList<>(
                convertCustomAttributeJSONtoFormItems( suSObjectModel.getCustomAttributes(), null ) );
        log.debug( "leaving getProjectCustomAttributeUI method" );
        return GUIUtils.createFormFromItems( uiFormItemList );
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

    @Override
    public List< Object > getAllValuesForProjectTableColumn( String projectId, String typeId, String columnName, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            var language = CommonUtils.resolveLanguage( token );
            var parent = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( projectId ) );
            var allColumns = GUIUtils.listColumns( GenericDTO.class );
            GUIUtils.validateColumnForAllValues( columnName, allColumns );
            List< Object > allValues;
            if ( projectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) && columnName.equalsIgnoreCase( ConstantsString.TYPE ) ) {
                allValues = getAllValuesForTypesInDataRoot();
            } else if ( columnName.equalsIgnoreCase( ConstantsString.TYPE ) ) {
                allValues = getAllValuesForTypeInDataProject( projectId, entityManager, language, parent );
            } else {
                allValues = susDAO.getAllPropertyValuesByParentId( entityManager, UUID.fromString( projectId ), columnName, language );
            }
            return allValues;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets all values for type in data project.
     *
     * @param projectId
     *         the project id
     * @param entityManager
     *         the entity manager
     * @param language
     *         the language
     * @param parent
     *         the parent
     *
     * @return the all values for type in data project
     */
    private List< Object > getAllValuesForTypeInDataProject( String projectId, EntityManager entityManager, String language,
            SuSEntity parent ) {
        List< Object > allValues;
        allValues = new ArrayList<>();
        List< Object > allTypeIds = susDAO.getAllPropertyValuesByParentId( entityManager, UUID.fromString( projectId ), "typeId",
                language );
        for ( var contained : allTypeIds ) {
            allValues.add( configManager.getObjectTypeByIdAndConfigName( contained.toString(), parent.getConfig() ).getName() );
        }
        return allValues;
    }

    /**
     * Gets all values for types in data root.
     *
     * @return the all values for types in data root
     */
    private List< Object > getAllValuesForTypesInDataRoot() {
        List< Object > allValues;
        allValues = new ArrayList<>();
        final SuSObjectModel suSObjectModel = configManager.getProjectModelByConfigLabel(
                ConfigUtil.labelNames( configManager.getMasterConfigurationFileNames() ).get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
        allValues.add( suSObjectModel.getName() );
        return allValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getSyncContextRouter( String projectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            for ( Object ids : filter.getItems() ) {
                SuSEntity latestSuSEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        UUID.fromString( ids.toString() ) );
                if ( latestSuSEntity instanceof ContainerEntity ) {
                    return contextMenuManager.getSyncContainerContextMenu( entityManager, PLUGIN_OBJECT, projectId, SyncFileDTO.class,
                            filter );
                }
            }
            return contextMenuManager.getSyncContextMenu( PLUGIN_OBJECT, projectId, SyncFileDTO.class, filter, ConstantsString.EMPTY_STRING,
                    ConstantsString.EMPTY_STRING, filter.getItems().size() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the transfer context router.
     *
     * @param filter
     *         the filter
     *
     * @return the transfer context router
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#getTransferContextRouter(java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public List< ContextMenuItem > getTransferContextRouter( FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ContextMenuItem > contextToReturn = new ArrayList<>();
            UUID selectionId = contextMenuManager.saveSelectionFilter( entityManager, SyncFileDTO.class, filter );
            String urlWithParam = ABORT_CONTEXT_URL.replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, selectionId.toString() );
            contextToReturn.add( new ContextMenuItem( urlWithParam, ConstantsString.EMPTY_STRING, ABORT_SYNCH ) );
            return contextToReturn;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getLocalContextRouter( String userIdFromGeneralHeader, String projectId, FiltersDTO filter ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getLocalContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter );
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
    public List< ContextMenuItem > getDataContextRouter( String userIdFromGeneralHeader, String projectId, FiltersDTO filter,
            String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getDataContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter, String token ) {
        UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userIdFromGeneralHeader ) );
        if ( BooleanUtils.isTrue( userEntity.isRestricted() ) ) {
            return new ArrayList<>();
        }

        // case of select all from data table
        if ( filter.getItems().isEmpty() && "-1".equalsIgnoreCase( filter.getLength().toString() ) ) {

            Long maxResults = susDAO.getAllFilteredRecordCountWithParentId( entityManager, SuSEntity.class, filter,
                    UUID.fromString( projectId ) );
            filter.setLength( Integer.valueOf( maxResults.toString() ) );
            List< SuSEntity > allObjectsList = susDAO.getAllFilteredRecordsWithParent( entityManager, SuSEntity.class, filter,
                    UUID.fromString( projectId ) );
            List< Object > itemsList = new ArrayList<>();
            allObjectsList.forEach( susEntity -> itemsList.add( susEntity.getComposedId().getId() ) );

            filter.setItems( itemsList );
        }

        // basic context behaviour
        List< String > selectedIds = new ArrayList<>( filter.getItems().size() );
        for ( Object object : filter.getItems() ) {
            selectedIds.add( Objects.toString( object, null ) );
        }

        if ( projectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
            return getDataListContext( entityManager, userIdFromGeneralHeader, projectId, filter, selectedIds, token, false );
        }

        List< SuSEntity > serverSusEntities = getServerEntitiesList( entityManager, selectedIds );
        List< ContextMenuItem > contextMenuItemList;
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == 1 ) {
            contextMenuItemList = getSingleSelectionContext( entityManager, userIdFromGeneralHeader, projectId, filter, selectedIds, token,
                    serverSusEntities );
            if ( serverSusEntities.stream().anyMatch( entity -> ( selectedIds.contains( entity.getComposedId().getId().toString() )
                    && entity instanceof DataObjectEntity dataObjectEntity && BooleanUtils.isFalse(
                    dataObjectEntity.getCheckedOut() ) ) ) ) {

                contextMenuItemList.removeIf(
                        contextMenuItem -> contextMenuItem.getTitle().equals( MessageBundleFactory.getMessage( DISCARD_LOCAL_FILE ) ) );
            }
        } else {
            contextMenuItemList = getMultiSelectContext( entityManager, userIdFromGeneralHeader, projectId, filter, selectedIds,
                    serverSusEntities );
            if ( serverSusEntities.stream().anyMatch( entity -> ( selectedIds.contains( entity.getComposedId().getId().toString() )
                    && entity instanceof DataObjectEntity dataObjectEntity && BooleanUtils.isFalse(
                    dataObjectEntity.getCheckedOut() ) ) ) ) {

                contextMenuItemList.removeIf(
                        contextMenuItem -> contextMenuItem.getTitle().equals( MessageBundleFactory.getMessage( DISCARD_LOCAL_FILE ) ) );
            }
        }
        // Remove this List removal for Client context menu.
        contextMenuItemList.removeIf(
                contextMenuItem -> contextMenuItem.getVisibility().equalsIgnoreCase( ContextMenuManagerImpl.CLIENT_VISIBILITY ) );
        return contextMenuItemList;

    }

    /**
     * Gets the single selection context.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param selectedIds
     *         the selected ids
     * @param token
     *         the token
     * @param serverSusEntities
     *         the server sus entities
     *
     * @return the single selection context
     */
    private List< ContextMenuItem > getSingleSelectionContext( EntityManager entityManager, String userIdFromGeneralHeader,
            String projectId, FiltersDTO filter, List< String > selectedIds, String token, List< SuSEntity > serverSusEntities ) {
        List< String > serverIds = new ArrayList<>();
        List< String > deletedIds = new ArrayList<>();
        for ( SuSEntity susEntity : serverSusEntities ) {
            serverIds.add( susEntity.getComposedId().getId().toString() );
            if ( susEntity.isDelete() ) {
                deletedIds.add( susEntity.getComposedId().getId().toString() );
            }
        }

        if ( isServerFile( selectedIds, serverIds ) ) {
            if ( deletedIds.contains( selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) ) ) {
                return new ArrayList<>();
            }
            return getServerContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter, token, false );
        } else {
            return getLocalContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter );
        }
    }

    /**
     * Gets the multi select context.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param selectedIds
     *         the selected ids
     * @param serverSusEntities
     *         the server sus entities
     *
     * @return the multi select context
     */
    private List< ContextMenuItem > getMultiSelectContext( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter, List< String > selectedIds, List< SuSEntity > serverSusEntities ) {
        List< String > common = new ArrayList<>();
        List< String > deletedIds = new ArrayList<>();

        boolean isObject = false;
        boolean isContainer = false;
        boolean isReport = false;

        for ( SuSEntity susEntity : serverSusEntities ) {
            common.add( susEntity.getComposedId().getId().toString() );
            if ( susEntity.isDelete() ) {
                deletedIds.add( susEntity.getComposedId().getId().toString() );
            }
            if ( susEntity instanceof ReportEntity ) {
                isReport = true;
            } else if ( susEntity instanceof DataObjectEntity ) {
                isObject = true;
            } else if ( susEntity instanceof ContainerEntity ) {
                isContainer = true;
            }
        }
        if ( new HashSet<>( common ).containsAll( selectedIds ) ) {
            if ( new HashSet<>( deletedIds ).containsAll( selectedIds ) ) {
                return new ArrayList<>();
            }
            return getDataContextMultiSelection( entityManager, userIdFromGeneralHeader, projectId, filter, serverSusEntities, isObject,
                    isContainer, isReport );
        }
        common.retainAll( selectedIds );

        if ( !common.isEmpty() && common.size() < selectedIds.size() ) {
            return new ArrayList<>();
        } else {
            return getLocalContextRouter( entityManager, userIdFromGeneralHeader, projectId, filter );
        }
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
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == 1 ) {
            return getServerContextRouter( entityManager, userId, projectId, filter, token, getSearchContext );
        } else {
            boolean isObject = false;
            boolean isContainer = false;
            boolean isReport = false;
            List< SuSEntity > susEntities = new ArrayList<>();
            for ( Object object : filter.getItems() ) {
                SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class,
                        UUID.fromString( object.toString() ) );
                if ( susEntity instanceof ReportEntity ) {
                    isReport = true;
                } else if ( susEntity instanceof DataObjectEntity ) {
                    isObject = true;
                } else if ( susEntity instanceof ContainerEntity ) {
                    isContainer = true;
                }
                susEntities.add( susEntity );
            }
            return getDataContextMultiSelection( entityManager, userId, projectId, filter, susEntities, isObject, isContainer, isReport );
        }
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
     * Gets the data context multi selection.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param susEntities
     *         the sus entities
     * @param isObject
     *         the is object
     * @param isContainer
     *         the is container
     * @param isReport
     *         the is report
     *
     * @return the data context multi selection
     */
    private List< ContextMenuItem > getDataContextMultiSelection( EntityManager entityManager, String userIdFromGeneralHeader,
            String projectId, FiltersDTO filter, List< SuSEntity > susEntities, boolean isObject, boolean isContainer, boolean isReport ) {
        int size = filter.getItems().size();
        List< ContextMenuItem > syncContext = new ArrayList<>();
        SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                SelectionOrigins.CONTEXT, filter );
        List< Object > selectionId = new ArrayList<>();
        selectionId.add( selectionResponseUI.getId() );
        filter.setItems( selectionId );
        if ( !isContainer ) {
            syncContext = contextMenuManager.getSyncContextMenu( PLUGIN_OBJECT, projectId, SyncFileDTO.class, filter,
                    ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, size );
        }

        boolean isAllCurves = checkAllCurve( susEntities );
        List< ContextMenuItem > cml;
        if ( isReport ) {
            cml = contextMenuManager.getDataContextMenuForReport( PLUGIN_OBJECT, projectId, DataObjectDTO.class, filter );
        } else {
            cml = contextMenuManager.getDataContextMenuBulk( PLUGIN_OBJECT, projectId, DataObjectDTO.class, filter, isObject );
            if ( isAllCurves ) {
                cml.add( prepareCurveCompareObject( UUID.fromString( selectionResponseUI.getId() ) ) );
            }
        }

        syncContext.addAll( cml );
        return syncContext;
    }

    /**
     * Check all curve.
     *
     * @param susEntities
     *         the sus entities
     *
     * @return true, if successful
     */
    private boolean checkAllCurve( List< SuSEntity > susEntities ) {
        boolean isAllCurves = true;
        for ( SuSEntity suSEntity : susEntities ) {
            if ( !( suSEntity instanceof DataObjectCurveEntity ) ) {
                isAllCurves = false;
                break;
            }
        }
        return isAllCurves;
    }

    /**
     * Prepare curve compare object.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCurveCompareObject( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl(
                BULK_CURVE_COMPARE_OBJECT_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100079x4" ) );
        return containerCMI;
    }

    /**
     * Gets the server context router.
     *
     * @param entityManager
     *         the entity manager
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param projectId
     *         the project id
     * @param filter
     *         the filter
     * @param token
     *         the token
     * @param getSearchContext
     *         the get search context
     *
     * @return the server context router
     */
    private List< ContextMenuItem > getServerContextRouter( EntityManager entityManager, String userIdFromGeneralHeader, String projectId,
            FiltersDTO filter, String token, boolean getSearchContext ) {
        boolean isContainer = false;
        boolean isReport = false;
        UUID containerID = null;
        List< Object > objectItems = filter.getItems();
        SuSEntity susEntity = null;
        for ( Object object : objectItems ) {
            susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( object.toString() ) );
            List< SuSEntity > container = susDAO.getParents( entityManager, susEntity );
            SuSEntity parent = container.get( LIST_FIRST_INDEX );
            containerID = parent.getComposedId().getId();
            if ( susEntity instanceof WorkflowProjectEntity ) {
                return contextMenuManager.getWorkflowContextMenu( filter, false );
            } else if ( susEntity instanceof ContainerEntity ) {
                isContainer = true;
            } else if ( susEntity instanceof ReportEntity ) {
                isReport = true;
            } else if ( susEntity instanceof WorkflowEntity ) {
                return contextMenuManager.getWorkflowContextMenu( filter, true );
            }
        }
        SelectionResponseUI selectionResponseUI = selectionManager.createSelection( entityManager, userIdFromGeneralHeader,
                SelectionOrigins.CONTEXT, filter );
        List< ContextMenuItem > cml = getDataContextMenu( entityManager, filter, isContainer, isReport, containerID );
        if ( !isContainer && !getSearchContext && !isReport ) {

            List< Object > selectionId = new ArrayList<>();
            selectionId.add( selectionResponseUI.getId() );
            filter.setItems( selectionId );

            cml.addAll( contextMenuManager.getSyncContextMenu( PLUGIN_OBJECT, projectId, SyncFileDTO.class, filter,
                    objectItems.get( 0 ).toString(), token, 0 ) );
        }

        if ( susEntity instanceof DataObjectEntity dataObjectEntity && null == dataObjectEntity.getFile() ) {
            cml.removeIf( cl -> MessageBundleFactory.getMessage( Messages.DOWNLOAD.getKey() ).equals( cl.getTitle() ) );
        }
        return cml;
    }

    /**
     * Gets the data context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     * @param isContainer
     *         the is container
     * @param isReport
     *         the is report
     * @param containerId
     *         the container id
     *
     * @return the data context menu
     */
    private List< ContextMenuItem > getDataContextMenu( EntityManager entityManager, FiltersDTO filter, boolean isContainer,
            boolean isReport, UUID containerId ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            addDataObjectContextMenuItems( entityManager, cml, objectId, isContainer, isReport, containerId );
        }
        return cml;
    }

    /**
     * Adds the data object context menu items.
     *
     * @param entityManager
     *         the entity manager
     * @param cml
     *         the cml
     * @param objectId
     *         the object id
     * @param isContainer
     *         the is container
     * @param isReport
     *         the is report
     * @param containerId
     *         the container id
     */
    private void addDataObjectContextMenuItems( EntityManager entityManager, List< ContextMenuItem > cml, UUID objectId,
            boolean isContainer, boolean isReport, UUID containerId ) {
        cml.add( prepareDeleteObjectContext( objectId ) );
        cml.add( prepareStatusChangeObjectContext( objectId ) );
        cml.add( prepareUpdateObjectContext( objectId, isContainer ) );

        if ( !isReport ) {
            cml.add( prepareExportObjectContext( objectId, containerId ) );
        }
        if ( isContainer ) {
            SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, objectId );
            cml.add( prepareCreateStructureContext( objectId ) );

            if ( isRunnableVariant( entityManager, objectId ) ) {
                cml.add( prepareRunVariantContext( objectId ) );
                cml.add( prepareCB2RunVariantContext( objectId ) );
            }
            if ( latestEntity.getConfig().equals( BMW_DUMMY_PROJECT_JSON ) && isContainerLabelProject( ( ContainerEntity ) latestEntity )
                    && ( latestEntity instanceof ProjectEntity ) ) {
                cml.add( prepareDummyRunVariantContext( latestEntity ) );
            }
            if ( latestEntity.getConfig().equals( BMW_QADYNA_PROJECT_JSON ) && !isContainerLabelProject( ( ContainerEntity ) latestEntity )
                    && ( latestEntity instanceof ProjectEntity ) ) {
                cml.add( prepareQADynaContext( latestEntity ) );
            }
        }
        cml.add( prepareMoveToObjectContext( objectId ) );
    }

    /**
     * Checks if container is LABEL project.
     *
     * @param containerEntity
     *         the project entity
     *
     * @return boolean boolean
     */
    private boolean isContainerLabelProject( ContainerEntity containerEntity ) {
        return !( containerEntity instanceof ProjectEntity projectEntity ) || !projectEntity.getType()
                .equalsIgnoreCase( ProjectType.LABEL.getKey() );
    }

    /**
     * Prepare dummy run variant context.
     *
     * @param containerEntity
     *         the project entity
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareDummyRunVariantContext( SuSEntity containerEntity ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl(
                "view/wizards/dummy/{parentId}".replace( PARAM_PARENT_ID, containerEntity.getComposedId().getId().toString() ) );
        containerCMI.setTitle( "Create Dummy Variant" );
        return containerCMI;
    }

    /**
     * Prepare qa dyna context context menu item.
     *
     * @param containerEntity
     *         the container entity
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareQADynaContext( SuSEntity containerEntity ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl(
                "view/wizards/qadyna/{parentId}".replace( PARAM_PARENT_ID, containerEntity.getComposedId().getId().toString() ) );
        containerCMI.setTitle( "Run QA Dyna" );
        return containerCMI;
    }

    /**
     * Prepare run variant context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareRunVariantContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.RUN_VARIANT_CONTEXT_URL.replace( PARAM_PARENT_ID, "" + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.RUN_VARIANT_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare cb 2 run variant context context menu item.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCB2RunVariantContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.RUN_CB2_VARIANT_CONTEXT_URL.replace( PARAM_PARENT_ID, "" + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.RUN_CB2_VARIANT_TITLE ) );
        return containerCMI;
    }

    /**
     * Checks if the object is a runnable variant.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     *
     * @return is runnable variant
     */
    private boolean isRunnableVariant( EntityManager entityManager, UUID objectId ) {
        SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, objectId );
        SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                latestEntity.getConfig() );
        return ( entityModel.getId().equals( ConstantsID.VARIANT_TYPE_ID ) && latestEntity.getConfig().equals( BMW_DUMMY_PROJECT_JSON ) );
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
        containerCMI.setUrl( ContextMenuManagerImpl.MOVE_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.SELECTION_ID_PARAM,
                ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100016x4" ) );
        return containerCMI;
    }

    /**
     * Prepare create structure context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCreateStructureContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.CREAT_STRUCTURE_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.CREAT_STRUCTURE_TITLLE ) );
        containerCMI.setVisibility( ContextMenuManagerImpl.CLIENT_VISIBILITY );

        return containerCMI;
    }

    /**
     * Prepare export object context.
     *
     * @param objectId
     *         the object id
     * @param containerId
     *         the container id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareExportObjectContext( UUID objectId, UUID containerId ) {
        // sending object id instead of selection id because of context issue : will be
        // changed
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.EXPORT_PROJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.CONTAINER_ID_PARAM,
                        ConstantsString.EMPTY_STRING + containerId )
                .replace( ContextMenuManagerImpl.SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.EXPORT_TITLE ) );
        containerCMI.setVisibility( ContextMenuManagerImpl.CLIENT_VISIBILITY );

        return containerCMI;
    }

    /**
     * Prepare update object context.
     *
     * @param objectId
     *         the object id
     * @param isContainer
     *         the is container
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareUpdateObjectContext( UUID objectId, boolean isContainer ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        if ( isContainer ) {
            containerCMI.setUrl( ContextMenuManagerImpl.UPDATE_PROJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.PROJECT_ID_PARAM,
                    ConstantsString.EMPTY_STRING + objectId ) );
        } else {
            containerCMI.setUrl( ContextMenuManagerImpl.UPDATE_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                    ConstantsString.EMPTY_STRING + objectId ) );
        }
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.EDIT_TITLE ) );
        return containerCMI;
    }

    /**
     * Preparation of deleting object from context menu.
     *
     * @param objectId
     *         UUID
     *
     * @return ContextMenuItem context menu item
     */
    private ContextMenuItem prepareDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ContextMenuManagerImpl.DELETE_OBJECT_CONTEXT_URL.replace( ContextMenuManagerImpl.OBJECT_ID_PARAM,
                ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.DELETE_OBJECT_TITLE ) );
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
        containerCMI.setTitle( MessageBundleFactory.getMessage( ContextMenuManagerImpl.CHANGE_STATUS_TITLE ) );
        return containerCMI;
    }

    /**
     * Checks if is server file.
     *
     * @param selectedIds
     *         the selected ids
     * @param serverIds
     *         the server ids
     *
     * @return true, if is server file
     */
    private boolean isServerFile( List< String > selectedIds, List< String > serverIds ) {
        return serverIds.contains( selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
    }

    /**
     * Gets the server ids.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedIds
     *         the selected ids
     *
     * @return the server ids
     */
    private List< SuSEntity > getServerEntitiesList( EntityManager entityManager, List< String > selectedIds ) {
        List< UUID > selectedIdUUID = new ArrayList<>();
        for ( String string : selectedIds ) {
            selectedIdUUID.add( UUID.fromString( string ) );
        }
        return susDAO.getObjectsByListOfIds( entityManager, selectedIdUUID );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< SelectFormItem > permissionObjectOptionsForm( String userId, String objectId, String option ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            isPermittedToWrite( entityManager, userId, objectId );
            SelectFormItem selectFormItem = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            fillSelectUIFieldForPermission( entityManager, objectId, selectFormItem, option );
            return List.of( selectFormItem );
        } finally {
            entityManager.close();
        }
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
            isPermittedToManage( entityManager, userId, objectId );
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
            usersTable.setConnectedTableLabel( DataObjectManagerImpl.USER_SELECTION_CONNECTED_TABLE_LABEL );
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
            groupsTable.setConnectedTableLabel( DataObjectManagerImpl.GROUP_SELECTION_CONNECTED_TABLE_LABEL );
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
    public UIForm getObjectOptionForm( String userId, UUID parentId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UIFormItem > listUserDirectory = new ArrayList<>();
            log.debug( "entered in getObjectOptionForm method" );
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, parentId );
            entityManager.close();
            List< SelectOptionsUI > options = new ArrayList<>();

            SuSObjectModel projectSuSObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            boolean isDataProject = false;
            if ( susEntity instanceof ProjectEntity projectEntity ) {
                if ( projectEntity.getType().equals( ProjectType.DATA.getKey() ) ) {
                    isDataProject = true;
                } else if ( projectEntity.getType().equals( ProjectType.LABEL.getKey() ) ) {
                    SelectOptionsUI project = new SelectOptionsUI();
                    project.setId( projectSuSObjectModel.getId() );
                    project.setName( projectSuSObjectModel.getName() );
                    project.setIcon( projectSuSObjectModel.getIcon() );
                    options.add( project );
                }
            }
            if ( !( susEntity instanceof ProjectEntity ) || isDataProject ) {
                List< String > containingObjectIds = projectSuSObjectModel.getContains();

                if ( CollectionUtil.isNotEmpty( containingObjectIds ) ) {

                    for ( String objectId : containingObjectIds ) {
                        SuSObjectModel objectTypeModel = configManager.getObjectTypeByIdAndConfigName( objectId, susEntity.getConfig() );
                        SelectOptionsUI object = new SelectOptionsUI();
                        object.setId( objectTypeModel.getId() );
                        object.setName( objectTypeModel.getName() );
                        object.setIcon( objectTypeModel.getIcon() );
                        options.add( object );
                    }
                }
            }

            SelectFormItem item = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );
            item.setLabel( MessageBundleFactory.getMessage( OBJECT_TYPE_LABEL ) );
            item.setOptions( options );
            item.setName( CUSTOM_NAME );
            item.setType( FIELD_TYPE_SELECT );
            item.setReadonly( false );
            item.setBindFrom( BIND_FROM_URL_FOR_CREATE_OBJECT_UI_FORM.replace( PARAM_PARENT_ID, "" + parentId ) );

            setRulesAndMessageOnUI( item );
            setLiveSearch( item );

            listUserDirectory.add( item );
            log.debug( "leaving getObjectOptionForm method" );

            return GUIUtils.createFormFromItems( listUserDirectory );
        } finally {
            if ( entityManager.isOpen() ) {
                entityManager.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        applyPermissionsOnSelectedUsersOrGroups( entityManager, aclObjectIdentityEntity, userGroupSelection, objectInitiallyInherited );
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
     * Apply permissions on selected users.
     *
     * @param entityManager
     *         the entity manager
     * @param aclObjectIdentityEntity
     *         the acl object identity entity
     * @param newSidSelection
     *         the new sid selection
     * @param objectInitiallyInherited
     *         the object initially inherited
     */
    @Override
    public void applyPermissionsOnSelectedUsersOrGroups( EntityManager entityManager, AclObjectIdentityEntity aclObjectIdentityEntity,
            List< UUID > newSidSelection, boolean objectInitiallyInherited ) {
        log.debug( ">>applyPermissionsOnSelectedUsers" );
        /*
         * Logic to apply permissions 1. getCurrentACEs for object form db 2. Delete all ACE of sid who is not in newUserSelection 3. Keep
         * all ACE of sid who is in newUserSelection 4. Add view ACE of sid who is in newUserSelection and not yet processed in Step 3.
         */
        List< UUID > permittedSids;
        AclObjectIdentityEntity parentAclObjectIdentityEntity = null;
        if ( null != aclObjectIdentityEntity.getParentObject() && objectInitiallyInherited ) {
            parentAclObjectIdentityEntity = propagateToTreeParent( entityManager, permissionManager.getObjectIdentityDAO()
                    .getLatestNonDeletedObjectById( entityManager, aclObjectIdentityEntity.getParentObject().getId() ) );
            permittedSids = permissionManager.getEntryDAO()
                    .getAclSecurityIdentitySidsByObjectId( entityManager, parentAclObjectIdentityEntity.getId() );
        } else {
            permittedSids = permissionManager.getEntryDAO()
                    .getAclSecurityIdentitySidsByObjectId( entityManager, aclObjectIdentityEntity.getId() );
        }

        // userCommonManager.getSecurityIdentityById( consumerId ).getId()
        List< AclSecurityIdentityEntity > newUserSelectionSids = getSidListBySidIds( entityManager, newSidSelection );

        // Step 2 - Delete all ACE of sid who is not in newUserSelection
        for ( UUID permittedSid : permittedSids ) {
            if ( !newSidSelection.contains( permittedSid ) ) {
                permissionManager.getEntryDAO().deleteACEbyAclSecurityIdentityEntity( entityManager, aclObjectIdentityEntity.getId(),
                        userCommonManager.getSecurityIdentityBySidId( entityManager, permittedSid ) );
            }
        }

        // Step 3 - Keep all ACE of sid who is in newUserSelection - no logic needed.
        // Step 4 - Add view ACE of sid who is in newUserSelection and not yet processed in Step 3.
        for ( AclSecurityIdentityEntity newUserSelectionSid : newUserSelectionSids ) {
            if ( permittedSids.contains( newUserSelectionSid.getSid() ) ) {
                if ( null != parentAclObjectIdentityEntity ) {
                    List< AclEntryEntity > entryEntities = permissionManager.getEntryDAO()
                            .getAclEntryListByObjectId( entityManager, parentAclObjectIdentityEntity.getId() );
                    if ( entryEntities != null ) {
                        for ( AclEntryEntity entryEntity : entryEntities ) {
                            if ( entryEntity.getSecurityIdentityEntity().getSid().equals( newUserSelectionSid.getSid() ) ) {
                                PermissionDTO permissionDTO = new PermissionDTO();
                                permissionManager.getEntryDAO().save( entityManager,
                                        permissionDTO.prepareEntryEntityOnly( aclObjectIdentityEntity,
                                                entryEntity.getSecurityIdentityEntity(), entryEntity.getMask() ) );
                            }
                        }
                    }
                }
            } else {
                // Step 4 - Add view ACE of sid who is in newUserSelection and not yet processed in Step 3.
                AclEntryEntity aclEntryEntity = new AclEntryEntity( aclObjectIdentityEntity, newUserSelectionSid,
                        PermissionMatrixEnum.VIEW.getKey() );
                aclEntryEntity.setCreatedOn( new Date() );
                permissionManager.getEntryDAO().saveOrUpdate( entityManager, aclEntryEntity );
            }
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
     * Creates the workflow DTO conf from workflow entity.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the work flow DTO conf
     */
    private WorkFlowDTOConf createWorkflowDTOConfFromWorkflowEntity( SuSEntity susEntity ) {
        WorkFlowDTOConf workflowDTOConf = null;
        if ( susEntity != null ) {
            workflowDTOConf = new WorkFlowDTOConf();
            workflowDTOConf.setId( susEntity.getComposedId() != null ? susEntity.getComposedId().getId() : null );
            workflowDTOConf.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            workflowDTOConf.setName( susEntity.getName() );
            workflowDTOConf.setDescription( susEntity.getDescription() );
            workflowDTOConf.setConfig( susEntity.getConfig() );
            workflowDTOConf.setCreatedOn( susEntity.getCreatedOn() );
            workflowDTOConf.setModifiedOn( susEntity.getModifiedOn() );
            if ( null != susEntity.getCreatedBy() ) {
                workflowDTOConf.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( null != susEntity.getModifiedBy() ) {
                workflowDTOConf.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
            }
            workflowDTOConf.setTypeId( susEntity.getTypeId() );
            workflowDTOConf.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( susEntity.getTypeId(), susEntity.getLifeCycleStatus(),
                            susEntity.getConfig() ) );
            if ( null != susEntity.getConfig() ) {
                workflowDTOConf.setType(
                        configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
            }

        }
        return workflowDTOConf;
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
     * Prepare report DTO.
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
    private Object prepareReportDTO( EntityManager entityManager, UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setName( susEntity.getName() );
        reportDTO.setId( susEntity.getComposedId().getId() );
        reportDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
        reportDTO.setCreatedOn( susEntity.getCreatedOn() );
        reportDTO.setModifiedOn( susEntity.getModifiedOn() );
        reportDTO.setParentId( projectId );
        reportDTO.setTypeId( susEntity.getTypeId() );

        UserDTO userById = userCommonManager.getUserById( entityManager, susEntity.getOwner().getId() );
        if ( userById != null ) {
            reportDTO.setCreatedBy( userById );
        }
        reportDTO.setDescription( susEntity.getDescription() );
        if ( setCustomAttributes ) {
            reportDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
        }

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
     * Prepare data object prediction model DTO.
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
    private Object prepareDataObjectPredictionModelDTO( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        DataObjectPredictionModelDTO dataObjectDTO = new DataObjectPredictionModelDTO();

        Object objectDTO = null;
        if ( susEntity instanceof DataObjectPredictionModelEntity dataObjectPredictionModelEntity ) {

            dataObjectDTO.setName( dataObjectPredictionModelEntity.getName() );
            dataObjectDTO.setId( dataObjectPredictionModelEntity.getComposedId().getId() );
            dataObjectDTO.setVersion( new VersionDTO( dataObjectPredictionModelEntity.getComposedId().getVersionId() ) );
            dataObjectDTO.setCreatedOn( dataObjectPredictionModelEntity.getCreatedOn() );
            dataObjectDTO.setModifiedOn( dataObjectPredictionModelEntity.getModifiedOn() );
            dataObjectDTO.setParentId( projectId );
            dataObjectDTO.setAutoDeleted( dataObjectPredictionModelEntity.isAutoDelete() );
            dataObjectDTO.setTypeId( dataObjectPredictionModelEntity.getTypeId() );
            dataObjectDTO.setType( ConstantsObjectTypes.PREDICTION_MODEL_TYPE );
            dataObjectDTO.setSize( dataObjectPredictionModelEntity.getSize() != null
                    && dataObjectPredictionModelEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectPredictionModelEntity.getSize() )
                    : ConstantsString.NOT_AVAILABLE );

            DocumentEntity file = dataObjectPredictionModelEntity.getFile();
            if ( null != file ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
                if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                    List< LocationDTO > locationDTOs = getDocumentLocations( file );
                    dataObjectDTO.setLocations( locationDTOs );
                }
            }

            if ( null != dataObjectPredictionModelEntity.getCreatedBy() ) {
                dataObjectDTO.setCreatedBy(
                        userCommonManager.prepareUserModelFromUserEntity( dataObjectPredictionModelEntity.getCreatedBy() ) );
            }
            if ( null != dataObjectPredictionModelEntity.getModifiedBy() ) {
                dataObjectDTO.setModifiedBy(
                        userCommonManager.prepareUserModelFromUserEntity( dataObjectPredictionModelEntity.getModifiedBy() ) );
            }
            dataObjectDTO.setDescription( dataObjectPredictionModelEntity.getDescription() );
            if ( setCustomAttributes ) {
                dataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectPredictionModelEntity.getCustomAttributes() ) );
            }
            if ( dataObjectPredictionModelEntity.getTypeId() != null ) {
                dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectPredictionModelEntity.getTypeId().toString(),
                        dataObjectPredictionModelEntity.getConfig() ).getName() );
                dataObjectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( dataObjectPredictionModelEntity.getTypeId(),
                        dataObjectPredictionModelEntity.getLifeCycleStatus(), dataObjectPredictionModelEntity.getConfig() ) );
            }
            DocumentEntity binFile = dataObjectPredictionModelEntity.getBinFile();
            if ( null != binFile ) {
                dataObjectDTO.setBinFile( documentManager.prepareDocumentDTO( binFile ) );
            }

            if ( dataObjectPredictionModelEntity.getFile() != null ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( dataObjectPredictionModelEntity.getFile() ) );
            }

            DocumentEntity jsonFile = dataObjectPredictionModelEntity.getJsonFile();
            if ( null != jsonFile ) {
                dataObjectDTO.setJsonFile( documentManager.prepareDocumentDTO( jsonFile ) );
            }

            objectDTO = dataObjectDTO;
        }
        return objectDTO;

    }

    /**
     * Prepare data object value DTO.
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
    private Object prepareDataObjectValueDTO( EntityManager entityManager, UUID projectId, SuSEntity susEntity,
            boolean setCustomAttributes ) {
        DataObjectValueDTO dataObjectDTO = new DataObjectValueDTO();
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
        if ( ( ( DataObjectValueEntity ) susEntity ).getValue() != null ) {
            dataObjectDTO.setValue( ( ( DataObjectValueEntity ) susEntity ).getValue() );
        }

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

        }
        if ( null != susEntity.getModifiedBy() ) {
            dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
        }
        if ( null != susEntity.getConfig() ) {
            dataObjectDTO.setType(
                    configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() ).getName() );
        }
        if ( null != susEntity.getSize() ) {
            dataObjectDTO.getFile().setSize( susEntity.getSize() );
        }

        return dataObjectDTO;
    }

    /**
     * Creates the object file DTO from object entity.
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
    private Object createObjectFileDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        Object objectDTO = null;
        if ( susEntity instanceof DataObjectEntity dataObjectEntity ) {
            DataObjectFileDTO dataObjectDTO = new DataObjectFileDTO();
            dataObjectDTO.setName( dataObjectEntity.getName() );
            dataObjectDTO.setId( dataObjectEntity.getComposedId().getId() );
            dataObjectDTO.setVersion( new VersionDTO( dataObjectEntity.getComposedId().getVersionId() ) );
            dataObjectDTO.setCreatedOn( dataObjectEntity.getCreatedOn() );
            dataObjectDTO.setModifiedOn( dataObjectEntity.getModifiedOn() );
            dataObjectDTO.setParentId( projectId );
            dataObjectDTO.setTypeId( dataObjectEntity.getTypeId() );
            dataObjectDTO.setAutoDeleted( dataObjectEntity.isAutoDelete() );
            if ( null != dataObjectEntity.getCreatedBy() ) {
                dataObjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectEntity.getCreatedBy() ) );
            }
            if ( null != dataObjectEntity.getModifiedBy() ) {
                dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectEntity.getModifiedBy() ) );
            }
            DocumentEntity file = dataObjectEntity.getFile();
            if ( null != file ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
                if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                    List< LocationDTO > locationDTOs = getDocumentLocations( file );
                    dataObjectDTO.setLocations( locationDTOs );
                }
            }

            dataObjectDTO.setDescription( dataObjectEntity.getDescription() );
            if ( setCustomAttributes ) {
                dataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectEntity.getCustomAttributes() ) );
            }
            if ( dataObjectEntity.getTypeId() != null ) {
                dataObjectDTO.setTypeId( dataObjectEntity.getTypeId() );
                dataObjectDTO.setLifeCycleStatus(
                        configManager.getStatusByIdandObjectType( dataObjectEntity.getTypeId(), dataObjectEntity.getLifeCycleStatus(),
                                dataObjectEntity.getConfig() ) );
            }
            if ( null != dataObjectEntity.getConfig() ) {
                dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() ).getName() );
            }
            dataObjectDTO.setSize( dataObjectEntity.getSize() != null && dataObjectEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                    ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectEntity.getSize() )
                    : ConstantsString.NOT_AVAILABLE );

            objectDTO = dataObjectDTO;
        }
        return objectDTO;
    }

    /**
     * Creates the object movie DTO from object entity.
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
    private Object createObjectMovieDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        Object objectDTO = null;
        if ( susEntity instanceof DataObjectMovieEntity dataObjectMovieEntity ) {
            DataObjectMovieDTO dataObjectDTO = new DataObjectMovieDTO();
            dataObjectDTO.setName( dataObjectMovieEntity.getName() );
            dataObjectDTO.setId( dataObjectMovieEntity.getComposedId().getId() );
            dataObjectDTO.setVersion( new VersionDTO( dataObjectMovieEntity.getComposedId().getVersionId() ) );
            dataObjectDTO.setCreatedOn( dataObjectMovieEntity.getCreatedOn() );
            dataObjectDTO.setModifiedOn( dataObjectMovieEntity.getModifiedOn() );
            dataObjectDTO.setTypeId( dataObjectMovieEntity.getTypeId() );
            dataObjectDTO.setParentId( projectId );
            dataObjectDTO.setAutoDeleted( dataObjectMovieEntity.isAutoDelete() );
            dataObjectDTO.setType( ConstantsObjectTypes.MOVIE_TYPE );
            dataObjectDTO.setSize(
                    dataObjectMovieEntity.getSize() != null && dataObjectMovieEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                            ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectMovieEntity.getSize() )
                            : ConstantsString.NOT_AVAILABLE );

            if ( null != dataObjectMovieEntity.getCreatedBy() ) {
                dataObjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectMovieEntity.getCreatedBy() ) );
            }
            if ( null != dataObjectMovieEntity.getModifiedBy() ) {
                dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectMovieEntity.getModifiedBy() ) );
            }
            DocumentEntity file = dataObjectMovieEntity.getFile();
            if ( null != file ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
                if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                    List< LocationDTO > locationDTOs = getDocumentLocations( file );
                    dataObjectDTO.setLocations( locationDTOs );
                }
            }

            dataObjectDTO.setDescription( dataObjectMovieEntity.getDescription() );
            if ( setCustomAttributes ) {
                dataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectMovieEntity.getCustomAttributes() ) );
            }
            if ( dataObjectMovieEntity.getTypeId() != null ) {
                dataObjectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( dataObjectMovieEntity.getTypeId(),
                        dataObjectMovieEntity.getLifeCycleStatus(), dataObjectMovieEntity.getConfig() ) );
                dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectMovieEntity.getTypeId().toString(),
                        dataObjectMovieEntity.getConfig() ).getName() );
            }

            DocumentEntity thumb = dataObjectMovieEntity.getThumbnail();

            if ( null != thumb && thumb.getFileType() != null && ( thumb.getFileType().equalsIgnoreCase( ConstantsImageFileTypes.JPG )
                    || thumb.getFileType().equalsIgnoreCase( ConstantsImageFileTypes.BMP ) || thumb.getFileType()
                    .equalsIgnoreCase( ConstantsImageFileTypes.PNG ) ) ) {
                DocumentDTO thumbnail = getDataObjectMovieThumbNail( dataObjectMovieEntity.getThumbnail() );
                dataObjectDTO.setThumbnailImage( thumbnail );
            }

            objectDTO = dataObjectDTO;
        }
        return objectDTO;
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
        Object objectDTO = null;
        if ( susEntity instanceof DataObjectImageEntity dataObjectImageEntity ) {
            DataObjectImageDTO dataObjectDTO = new DataObjectImageDTO();
            dataObjectDTO.setName( dataObjectImageEntity.getName() );
            dataObjectDTO.setId( dataObjectImageEntity.getComposedId().getId() );
            dataObjectDTO.setVersion( new VersionDTO( dataObjectImageEntity.getComposedId().getVersionId() ) );
            dataObjectDTO.setCreatedOn( dataObjectImageEntity.getCreatedOn() );
            dataObjectDTO.setModifiedOn( dataObjectImageEntity.getModifiedOn() );
            dataObjectDTO.setParentId( projectId );
            dataObjectDTO.setAutoDeleted( dataObjectImageEntity.isAutoDelete() );
            dataObjectDTO.setTypeId( dataObjectImageEntity.getTypeId() );
            dataObjectDTO.setType( ConstantsObjectTypes.IMAGE_TYPE );
            dataObjectDTO.setSize(
                    dataObjectImageEntity.getSize() != null && dataObjectImageEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                            ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectImageEntity.getSize() )
                            : ConstantsString.NOT_AVAILABLE );

            DocumentEntity file = dataObjectImageEntity.getFile();
            if ( null != file ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
                if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                    List< LocationDTO > locationDTOs = getDocumentLocations( file );
                    dataObjectDTO.setLocations( locationDTOs );
                }
            }

            if ( null != dataObjectImageEntity.getCreatedBy() ) {
                dataObjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectImageEntity.getCreatedBy() ) );
            }
            if ( null != dataObjectImageEntity.getModifiedBy() ) {
                dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectImageEntity.getModifiedBy() ) );
            }
            dataObjectDTO.setDescription( dataObjectImageEntity.getDescription() );
            if ( setCustomAttributes ) {
                dataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectImageEntity.getCustomAttributes() ) );
            }
            if ( dataObjectImageEntity.getTypeId() != null ) {
                dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectImageEntity.getTypeId().toString(),
                        dataObjectImageEntity.getConfig() ).getName() );
                dataObjectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( dataObjectImageEntity.getTypeId(),
                        dataObjectImageEntity.getLifeCycleStatus(), dataObjectImageEntity.getConfig() ) );
            }
            DocumentEntity thumb = dataObjectImageEntity.getThumbNail();
            if ( null != thumb && thumb.getFileType() != null && dataObjectImageEntity.getFile() != null ) {
                DocumentDTO thumbnail = getDataObjectImageThumbNail( dataObjectImageEntity.getThumbNail() );
                dataObjectDTO.setThumbnailImage( thumbnail );
            }

            objectDTO = dataObjectDTO;
        }
        return objectDTO;
    }

    /**
     * Creates the variant DTO from object entity.
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
    private Object createVariantDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        Object objectDTO = null;

        if ( susEntity instanceof VariantEntity ) {
            VariantDTO variantDTO = new VariantDTO();
            variantDTO.setName( susEntity.getName() );
            variantDTO.setId( susEntity.getComposedId().getId() );
            variantDTO.setCreatedOn( susEntity.getCreatedOn() );
            variantDTO.setModifiedOn( susEntity.getModifiedOn() );
            variantDTO.setDescription( susEntity.getDescription() );
            variantDTO.setParentId( projectId );
            variantDTO.setAutoDeleted( susEntity.isAutoDelete() );
            variantDTO.setTypeId( susEntity.getTypeId() );
            variantDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            if ( null != susEntity.getCreatedBy() ) {
                variantDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( setCustomAttributes ) {
                variantDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            }
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
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    private Object createProjectDTOFromProjectEntityWithOverview( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        Object objectDTO = null;

        if ( susEntity instanceof ProjectEntity projectOverviewEntity ) {
            ProjectDTO projectOverviewDTO;
            if ( null != projectOverviewEntity.getHtmlJson() ) {
                projectOverviewDTO = JsonUtils.jsonToObject( ByteUtil.convertByteToString( projectOverviewEntity.getHtmlJson() ),
                        ProjectDTO.class );
            } else {
                projectOverviewDTO = new ProjectDTO();
            }

            updateProjectDTOFromEntity( projectId, setCustomAttributes, projectOverviewEntity, projectOverviewDTO );
            objectDTO = projectOverviewDTO;
        }

        return objectDTO;
    }

    /**
     * Update project dto from entity.
     *
     * @param projectId
     *         the project id
     * @param setCustomAttributes
     *         the set custom attributes
     * @param projectEntity
     *         the project entity
     * @param projectDTO
     *         the project dto
     */
    private void updateProjectDTOFromEntity( UUID projectId, boolean setCustomAttributes, ProjectEntity projectEntity,
            ProjectDTO projectDTO ) {
        projectDTO.setDescription( projectEntity.getDescription() );
        projectDTO.setName( projectEntity.getName() );
        projectDTO.setId( projectEntity.getComposedId().getId() );
        projectDTO.setVersion( new VersionDTO( projectEntity.getComposedId().getVersionId() ) );
        projectDTO.setCreatedOn( projectEntity.getCreatedOn() );
        projectDTO.setModifiedOn( projectEntity.getModifiedOn() );
        projectDTO.setParentId( projectId );
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
     * @param projectId
     *         the project id
     * @param susEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    private Object createLibraryDTOFromObjectEntity( UUID projectId, SuSEntity susEntity, boolean setCustomAttributes ) {
        Object objectDTO = null;

        if ( susEntity instanceof LibraryEntity ) {
            LibraryDTO libraryDTO = new LibraryDTO();
            libraryDTO.setName( susEntity.getName() );
            libraryDTO.setId( susEntity.getComposedId().getId() );
            libraryDTO.setCreatedOn( susEntity.getCreatedOn() );
            libraryDTO.setModifiedOn( susEntity.getModifiedOn() );
            libraryDTO.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
            libraryDTO.setParentId( projectId );
            libraryDTO.setTypeId( susEntity.getTypeId() );
            libraryDTO.setAutoDeleted( susEntity.isAutoDelete() );
            libraryDTO.setDescription( susEntity.getDescription() );
            if ( null != susEntity.getCreatedBy() ) {
                libraryDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
            }
            if ( setCustomAttributes ) {
                libraryDTO.setCustomAttributes( CustomAttributeDTO.prepareCustomAttributesMapFromSet( susEntity.getCustomAttributes() ) );
            }
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
    private LoadCaseDTO createLoadcaseDTOFromEntity( SuSEntity suSEntity ) {
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
     * Fill select UI field for permission.
     *
     * @param entityManager
     *         the entity manager
     * @param objectId
     *         the object id
     * @param selectFormItem
     *         the select UI
     * @param option
     *         the option
     */
    private void fillSelectUIFieldForPermission( EntityManager entityManager, String objectId, SelectFormItem selectFormItem,
            String option ) {

        AclObjectIdentityEntity aclObjectIdentityEntity = permissionManager.getObjectIdentityDAO()
                .getLatestNonDeletedObjectById( entityManager, UUID.fromString( objectId ) );

        List< SelectOptionsUI > options = new ArrayList<>();
        options.add( new SelectOptionsUI( INHERIT_TRUE, MessageBundleFactory.getMessage( SET_INHERITED_LABEL ) ) );
        options.add( new SelectOptionsUI( INHERIT_FALSE, MessageBundleFactory.getMessage( UNSET_INHERITED_LABEL ) ) );
        selectFormItem.setLabel( MessageBundleFactory.getMessage( SELECT_PERMISSION_LABEL ) );
        selectFormItem.setName( SELECTION );
        selectFormItem.setType( FIELD_TYPE_SELECT );
        selectFormItem.setMultiple( Boolean.FALSE );
        selectFormItem.setOptions( options );
        switch ( option ) {
            case OBJECT -> selectFormItem.setBindFrom( BIND_FROM_URL_FOR_OBJECT_PERMISSION.replace( PARAM_OBJECT_ID, objectId ) );
            case PROJECT -> selectFormItem.setBindFrom( BIND_FROM_URL_FOR_PROJECT_PERMISSION.replace( PARAM_PROJECT_ID, objectId ) );
            default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_OPTION_FOR_PERMISSION.getKey() ) );
        }
        if ( aclObjectIdentityEntity.isInherit() ) {
            selectFormItem.setValue( INHERIT_TRUE );
        } else {
            selectFormItem.setValue( INHERIT_FALSE );
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
    private void isPermittedToRead( EntityManager entityManager, String userId, String id ) {
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
    private void isPermittedToWrite( EntityManager entityManager, String userId, String id ) {
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
    private void isPermittedToManage( EntityManager entityManager, String userId, String id ) {
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
    private void isPermittedToUpdate( EntityManager entityManager, String userId, String id ) {
        if ( !userId.equals( ConstantsID.SUPER_USER_ID ) && !permissionManager.isPermitted( entityManager, userId,
                id + ConstantsString.COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), OBJECT ) );
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
     * {@inheritDoc}
     */
    @Override
    public SubTabsItem getTabsViewContainerUI( String token, String projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDTO user = TokenizedLicenseUtil.getUser( token );
            if ( projectId.equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                final SubTabsItem subTabs = new SubTabsItem();
                subTabs.setTitle(
                        MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.getCodeById( SimuspaceFeaturesEnum.DATA.getId() ) ) );
                subTabs.setTabs( List.of( new SubTabsUI( GenericDTO.GENERIC_DTO_TYPE, SimuspaceFeaturesEnum.DATA.getKey().toLowerCase(),
                        SimuspaceFeaturesEnum.DATA.getKey() ) ) );
                return subTabs;
            }
            SuSEntity entity = dataDAO.getLatestNonDeletedObjectViaCache( entityManager, UUID.fromString( projectId ) );
            if ( null == entity ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.DATA_PROJECT_DOES_NOT_EXIST.getKey() ) );
            }
            SuSObjectModel model = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );
            List< SubTabsUI > subTabsUIs = SubTabsUI.getSubTabsList( model.getViewConfig() );
            if ( StringUtils.isNotBlank( model.getOverviewPlugin() ) ) {
                OverviewPluginUtil.getUpdatedSubTabsList( model.getOverviewPlugin(), subTabsUIs,
                        TokenizedLicenseUtil.getUserLanguage( token ) );
            }
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager,
                    Collections.singletonList( entity.getComposedId() ) );
            translateName( user, entity, translationEntityList );
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
    public SubTabsItem getObjectWithVersionUI( String userIdFromGeneralHeader, String objectId, int versionId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getObjectByIdAndVersion( entityManager, SuSEntity.class, UUID.fromString( objectId ), versionId );
            SuSObjectModel model = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() );
            List< SubTabsUI > subTabsUIS = SubTabsUI.getSubTabsList( model.getViewConfig() );
            return new SubTabsItem( entity.getComposedId().getId().toString(), entity.getName(), subTabsUIS, entity.getIcon() );
        } finally {
            entityManager.close();
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
     * {@inheritDoc}
     */
    @Override
    public List< FileInfo > getAllItemsWithFilters( String userId, UUID projectId, String typeId, FiltersDTO filtersDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity latestEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectId );

            filtersDTO = new FiltersDTO();
            filtersDTO.setStart( 0 );
            filtersDTO.setLength( Integer.MAX_VALUE );

            SuSObjectModel entityModel;
            Class< ? > entityClass;
            if ( typeId.equals( GenericDTO.GENERIC_DTO_TYPE ) ) {
                entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(), latestEntity.getConfig() );
                entityClass = SuSEntity.class;
            } else {
                entityModel = configManager.getObjectTypeByIdAndConfigName( typeId, latestEntity.getConfig() );
                Object object = initializeObject( entityModel.getClassName() );
                entityClass = ReflectionUtils.getFieldByName( object.getClass(), ENTITY_CLASS_FIELD_NAME ).getType();
            }
            List< SuSEntity > suSEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, entityClass, filtersDTO,
                    projectId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                    configManager.getTypesFromConfiguration( latestEntity.getConfig() ) );
            List< FileInfo > items = getItems( entityManager, suSEntities, userId, projectId );
            entityManager.close();
            return items;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            entityManager.close();
            throw new SusException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< FileInfo > getAllItems( String userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, projectId );
            FiltersDTO filtersDTO = new FiltersDTO();
            filtersDTO.setStart( 0 );
            filtersDTO.setLength( Integer.MAX_VALUE );

            SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                    latestEntity.getConfig() );

            return getItems( entityManager,
                    susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, SuSEntity.class, filtersDTO, projectId, userId,
                            lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                            lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                            configManager.getTypesFromConfiguration( latestEntity.getConfig() ) ), userId, projectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilteredResponse< Object > getAllChildObjectsByTypeId( String userId, UUID projectId, String typeId, FiltersDTO filter,
            String token ) {
        if ( filter == null ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_OBJECTS_FOUND.getKey() ) );
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( typeId.equals( GenericDTO.GENERIC_DTO_TYPE ) ) {
                return getAllObjects( entityManager, userId, projectId, filter, token );
            }
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, projectId );
            SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( typeId, susEntity.getConfig() );
            Object object = initializeObject( susObjectModel.getClassName() );
            Class< ? > entityClass = ReflectionUtils.getFieldByName( object.getClass(), ENTITY_CLASS_FIELD_NAME ).getType();
            List< Object > objectDTOList = new ArrayList<>();
            List< SuSEntity > suSEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, entityClass, filter,
                    projectId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( susObjectModel.getLifeCycle() ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( susObjectModel.getLifeCycle() ),
                    configManager.getTypesFromConfiguration( susEntity.getConfig() ) );
            UserDTO user = TokenizedLicenseUtil.getUser(
                    BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );

            List< VersionPrimaryKey > listOfIds = suSEntities.stream().map( SuSEntity::getComposedId ).toList();
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager, listOfIds );
            suSEntities.forEach( entity -> translateName( user, entity, translationEntityList.stream()
                    .filter( translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( entity.getComposedId() ) )
                    .toList() ) );
            extractColumnsFromContainer( entityManager, projectId, objectDTOList, suSEntities, userId );
            return PaginationUtil.constructFilteredResponse( filter, objectDTOList );
        } finally {
            entityManager.close();
        }
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
    public Long getItemsCount( String userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< SuSEntity > sustEntityList = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class, projectId );
            if ( CollectionUtils.isNotEmpty( sustEntityList ) ) {
                return ( long ) sustEntityList.size();
            }
            return ( long ) ConstantsInteger.INTEGER_VALUE_ZERO;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< FileInfo > getAllFilteredItems( FiltersDTO filtersDTO, String userId, UUID projectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SuSEntity latestEntity = susDAO.getLatestNonDeletedObjectById( entityManager, projectId );
        SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( latestEntity.getTypeId().toString(),
                latestEntity.getConfig() );
        List< SuSEntity > sustEntityList = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, SuSEntity.class, filtersDTO,
                projectId, userId, lifeCycleManager.getOwnerVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                lifeCycleManager.getAnyVisibleStatusByPolicyId( entityModel.getLifeCycle() ),
                configManager.getTypesFromConfiguration( latestEntity.getConfig() ) );
        return getItemsWithHashes( entityManager, sustEntityList );
    }

    /**
     * Gets the items.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntityList
     *         the sus entity list
     *
     * @return the items
     */
    private List< FileInfo > getItemsWithHashes( EntityManager entityManager, List< SuSEntity > susEntityList ) {
        List< FileInfo > items = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( susEntityList ) ) {
            for ( SuSEntity susEntity : susEntityList ) {

                FileInfo fileInfo = extractFileInfo( susEntity );

                // getting list of versions of object and adding all versions hash to list
                List< SuSEntity > versionsList = susDAO.getAllVersionsOfObjectById( entityManager, susEntity.getComposedId().getId() );
                List< String > hashes = new ArrayList<>();

                if ( susEntity instanceof DataObjectEntity ) {

                    for ( SuSEntity verSusEntity : versionsList ) {
                        if ( ( ( DataObjectEntity ) verSusEntity ).getFile() != null
                                && verSusEntity.getComposedId().getVersionId() != susEntity.getComposedId().getVersionId() ) {
                            hashes.add( ( ( DataObjectEntity ) verSusEntity ).getFile().getHash() );
                        }
                    }
                    fileInfo.setHashList( hashes );
                }

                if ( susEntity instanceof ContainerEntity ) {
                    fileInfo.setHash( null );
                    fileInfo.setId( susEntity.getComposedId().getId().toString() );
                    fileInfo.setCheckedOut( false );
                    fileInfo.setCheckedOutUser( null );
                    fileInfo.setDir( true );
                }
                items.add( fileInfo );
            }
        }
        return items;
    }

    /**
     * Gets the items.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntityList
     *         the sus entity list
     * @param userId
     *         the user id
     * @param parentId
     *         the parent id
     *
     * @return the items
     */
    private List< FileInfo > getItems( EntityManager entityManager, List< SuSEntity > susEntityList, String userId, UUID parentId ) {
        List< FileInfo > items = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( susEntityList ) ) {
            List< VersionPrimaryKey > listOfIds = susEntityList.stream().map( SuSEntity::getComposedId ).toList();
            List< TranslationEntity > translationEntityList = translationDAO.getAllTranslationsByListOfIds( entityManager, listOfIds );
            for ( SuSEntity susEntity : susEntityList ) {
                UserDTO user = TokenizedLicenseUtil.getUser(
                        BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
                translateName( user, susEntity, translationEntityList.stream()
                        .filter( translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( susEntity.getComposedId() ) )
                        .toList() );
                FileInfo fileInfo = extractFileInfo( susEntity );
                fileInfo.setCreatedOn( susEntity.getCreatedOn() );
                fileInfo.setModifiedOn( susEntity.getModifiedOn() );
                fileInfo.setParentId( parentId );
                fileInfo.setAutoDeleted( susEntity.isAutoDelete() );

                if ( susEntity.getCreatedBy() != null ) {
                    fileInfo.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getCreatedBy() ) );
                }
                if ( susEntity.getModifiedBy() != null ) {
                    fileInfo.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( susEntity.getModifiedBy() ) );
                }

                List< Relation > relation = linkDao.getLinkedRelationByChildId( entityManager, susEntity.getComposedId().getId() );
                fileInfo.setVersion( new VersionDTO( susEntity.getComposedId().getVersionId() ) );
                if ( CollectionUtils.isNotEmpty( relation ) ) {
                    fileInfo.setLink( LINK_TYPE_YES );
                } else {
                    fileInfo.setLink( LINK_TYPE_NO );
                }

                if ( susEntity instanceof ContainerEntity ) {
                    fileInfo.setHash( null );
                    fileInfo.setId( susEntity.getComposedId().getId().toString() );
                    fileInfo.setCheckedOut( false );
                    fileInfo.setCheckedOutUser( null );
                    fileInfo.setDir( true );
                    fileInfo.setCheckedOutSame( false );
                    fileInfo.setUrlType( ConstantsString.PROJECT_KEY );
                } else {
                    fileInfo.setUrlType( ConstantsString.OBJECT_KEY );
                    SuSObjectModel objectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                            susEntity.getConfig() );
                    if ( susEntity instanceof DataObjectEntity dataObject ) {

                        List< CommonLocationDTO > locationList = new ArrayList<>();
                        if ( dataObject.getFile() != null ) {
                            for ( LocationEntity locationObj : dataObject.getFile().getLocations() ) {
                                if ( locationObj != null ) {
                                    String statusStr = BooleanUtils.isTrue( locationObj.isStatus() ) ? ConstantsStatus.ACTIVE
                                            : ConstantsStatus.INACTIVE;
                                    locationList.add( new CommonLocationDTO( locationObj.getId().toString(), locationObj.getName(),
                                            locationObj.getDescription(), statusStr, locationObj.getType(), locationObj.getPriority(),
                                            locationObj.getVault(), locationObj.getStaging(), locationObj.getUrl(),
                                            locationObj.getAuthToken(), locationObj.isInternal() ) );
                                }
                            }
                        }
                        fileInfo.setLocation( locationList );
                        fileInfo.setCheckedOut( dataObject.getCheckedOut() );
                        fileInfo.setCheckedOutSame(
                                dataObject.getCheckedOut() && dataObject.getCheckedOutUser().getId().toString().equals( userId ) );
                    }

                    if ( null != objectModel ) {
                        fileInfo.setType( objectModel.getName() );
                        fileInfo.setIcon( objectModel.getIcon() );
                    }

                }
                items.add( fileInfo );
            }
        }
        return items;
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
    private UserEntity prepareUserEntityFromUserModel( UserDTO userDTO ) {
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
     * Sets the object synch status.
     *
     * @param userIdFromGeneralHeader
     *         the user id from general header
     * @param userName
     *         the user name
     * @param fromString
     *         the from string
     * @param operation
     *         the operation
     *
     * @return the data object DTO
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.object.manager.DataManager#objectCheckoutStatus(java.lang.String, java.lang.String,
     * java.util.UUID, java.lang.String)
     */
    @Override
    public DataObjectDTO setObjectSynchStatus( String userIdFromGeneralHeader, String userName, UUID fromString, String operation ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            DataObjectEntity dataObjectEntity = dataDAO.getLatestObjectById( entityManager, DataObjectEntity.class, fromString );
            SuSObjectModel suSObjectModel = configManager.getObjectTypeByIdAndConfigName( dataObjectEntity.getTypeId().toString(),
                    dataObjectEntity.getConfig() );

            UserEntity checkoutUser = prepareUserEntityFromUserModel(
                    userCommonManager.getUserById( entityManager, UUID.fromString( userIdFromGeneralHeader ) ) );

            // checkout operation
            if ( BooleanUtils.isFalse( dataObjectEntity.getCheckedOut() ) && dataObjectEntity.getCheckedOutUser() == null
                    && operation.equals( ConstantsDbOperationTypes.CHECKOUT ) ) {
                // Audit log entry for checkout
                if ( BooleanUtils.isTrue( PropertiesManager.isAuditData() ) ) {
                    dataObjectEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                            checkoutUser.getFirstName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CHECKOUT,
                            ConstantsDbOperationTypes.CHECKOUT, userIdFromGeneralHeader, dataObjectEntity, suSObjectModel.getName() ) );
                }

                dataObjectEntity.setCheckedOut( true );
                dataObjectEntity.setCheckedOutUser( checkoutUser );

            } else if ( BooleanUtils.isTrue( dataObjectEntity.getCheckedOut() ) && dataObjectEntity.getCheckedOutUser() != null
                    && operation.equals( ConstantsDbOperationTypes.DISCARD ) && dataObjectEntity.getCheckedOutUser().getId()
                    .equals( UUID.fromString( userIdFromGeneralHeader ) ) ) {
                // Audit log entry for checkout
                dataObjectEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                        checkoutUser.getFirstName() + ConstantsString.SPACE + ConstantsDbOperationTypes.DISCARD,
                        ConstantsDbOperationTypes.DISCARD, userIdFromGeneralHeader, dataObjectEntity, suSObjectModel.getName() ) );
                dataObjectEntity.setCheckedOut( false );
                dataObjectEntity.setCheckedOutUser( null );
            } else if ( BooleanUtils.isTrue( dataObjectEntity.getCheckedOut() ) && dataObjectEntity.getCheckedOutUser() != null
                    && operation.equals( ConstantsDbOperationTypes.CHECKIN ) && dataObjectEntity.getCheckedOutUser().getId()
                    .equals( UUID.fromString( userIdFromGeneralHeader ) ) ) {

                dataObjectEntity.setCheckedOutUser( null );
                dataObjectEntity.setCheckedOut( false );

                // Audit log entry for checkout
                dataObjectEntity.setAuditLogEntity( AuditLogDTO.prepareAuditLogEntityForSusEntity(
                        checkoutUser.getFirstName() + ConstantsString.SPACE + ConstantsDbOperationTypes.CHECKIN,
                        ConstantsDbOperationTypes.CHECKIN, userIdFromGeneralHeader, dataObjectEntity, suSObjectModel.getName() ) );

                // status change to WIP
                StatusDTO dto = configManager.getDefaultStatusByObjectTypeId( dataObjectEntity.getTypeId().toString(),
                        dataObjectEntity.getConfig() );
                dataObjectEntity.setLifeCycleStatus( dto.getId() );
                changeStatusInVersionsRecursively( entityManager, dataObjectEntity );

            } else {
                // other user try to checkout when object is already checked out
                throw new SusException( MessageBundleFactory.getMessage( Messages.ONLY_CHECKOUT_USER_CAN_PERFORM_THIS_ACTION.getKey() ) );
            }

            dataObjectEntity = dataDAO.saveOrUpdate( entityManager, dataObjectEntity );
            DataObjectDTO dataObject = new DataObjectDTO();
            dataObject.setCheckedOut( dataObjectEntity.getCheckedOut() );
            return dataObject;
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets the object synch status.
     *
     * @param userId
     *         the user id
     * @param fromString
     *         the from string
     *
     * @return the object synch status
     */
    @Override
    public DataObjectDTO getObjectSynchStatus( String userId, UUID fromString ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, fromString );
            DataObjectDTO dataObject = new DataObjectDTO();

            StatusConfigDTO detail = lifeCycleManager.getLifeCycleStatusByStatusId( susEntity.getLifeCycleStatus() );
            StatusDTO statusLifeCycle = new StatusDTO( detail.getId(), detail.getName() );
            dataObject.setLifeCycleStatus( statusLifeCycle );

            if ( susEntity instanceof DataObjectEntity dataObjectEntity ) {

                dataObject.setCheckedOut( dataObjectEntity.getCheckedOut() );

                dataObject.setCheckedOutSame( dataObjectEntity.getCheckedOutUser() != null && dataObjectEntity.getCheckedOutUser().getId()
                        .equals( UUID.fromString( userId ) ) );
            } else {
                dataObject.setCheckedOutSame( false );
                dataObject.setCheckedOut( false );
            }

            return dataObject;
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
        if ( susEntity instanceof DataObjectImageEntity ) {
            return createObjectImageDTOFromObjectEntity( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectPredictionModelEntity ) {
            return prepareDataObjectPredictionModelDTO( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectMovieEntity ) {
            return createObjectMovieDTOFromObjectEntity( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectHtmlsEntity dataObjectHtmlsEntity ) {
            return prepareDataObjectHtmlDTO( id, dataObjectHtmlsEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectDashboardEntity dataObjectDashboardEntity ) {
            return prepareDataObjectDashboardDTO( entityManager, id, dataObjectDashboardEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectTraceEntity dataObjectTraceEntity ) {
            return prepareDataObjectTraceDTO( entityManager, id, dataObjectTraceEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectFileEntity ) {
            return createObjectFileDTOFromObjectEntity( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectValueEntity ) {
            return prepareDataObjectValueDTO( entityManager, id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof ReportEntity ) {
            return prepareReportDTO( entityManager, id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataObjectEntity ) {
            return createObjectDTOFromObjectEntity( entityManager, id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof DataDashboardEntity dataDashboardEntity ) {
            return dataDashboardManager.prepareDataDashboardDTO( entityManager, id, dataDashboardEntity, setCustomAttributes );
        } else if ( susEntity instanceof VariantEntity ) {
            return createVariantDTOFromObjectEntity( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof ProjectEntity ) {
            return createProjectDTOFromProjectEntityWithOverview( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof LibraryEntity ) {
            return createLibraryDTOFromObjectEntity( id, susEntity, setCustomAttributes );
        } else if ( susEntity instanceof WorkflowProjectEntity ) {
            return createWorkflowProjectDTOFromWorkflowProjectEntity( susEntity, setCustomAttributes );
        } else if ( susEntity instanceof WorkflowEntity ) {
            return createWorkflowDTOConfFromWorkflowEntity( susEntity );
        }
        return null;
    }

    /**
     * Prepare data object html dto object.
     *
     * @param id
     *         the id
     * @param dataObjectHtmlsEntity
     *         the data object htmls entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    private Object prepareDataObjectHtmlDTO( UUID id, DataObjectHtmlsEntity dataObjectHtmlsEntity, boolean setCustomAttributes ) {

        Object objectDTO = null;
        if ( dataObjectHtmlsEntity != null ) {

            DataObjectHtmlDTO dataObjectDTO;
            if ( null != dataObjectHtmlsEntity.getHtmlJson() ) {
                dataObjectDTO = JsonUtils.jsonToObject( ByteUtil.convertByteToString( dataObjectHtmlsEntity.getHtmlJson() ),
                        DataObjectHtmlDTO.class );
            } else {
                dataObjectDTO = new DataObjectHtmlDTO();
            }

            dataObjectDTO.setName( dataObjectHtmlsEntity.getName() );
            dataObjectDTO.setId( dataObjectHtmlsEntity.getComposedId().getId() );
            dataObjectDTO.setVersion( new VersionDTO( dataObjectHtmlsEntity.getComposedId().getVersionId() ) );
            dataObjectDTO.setCreatedOn( dataObjectHtmlsEntity.getCreatedOn() );
            dataObjectDTO.setModifiedOn( dataObjectHtmlsEntity.getModifiedOn() );
            dataObjectDTO.setParentId( id );
            dataObjectDTO.setTypeId( dataObjectHtmlsEntity.getTypeId() );
            dataObjectDTO.setAutoDeleted( dataObjectHtmlsEntity.isAutoDelete() );
            if ( null != dataObjectHtmlsEntity.getCreatedBy() ) {
                dataObjectDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectHtmlsEntity.getCreatedBy() ) );
            }
            if ( null != dataObjectHtmlsEntity.getModifiedBy() ) {
                dataObjectDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dataObjectHtmlsEntity.getModifiedBy() ) );
            }
            DocumentEntity file = dataObjectHtmlsEntity.getFile();
            if ( null != file ) {
                dataObjectDTO.setFile( documentManager.prepareDocumentDTO( file ) );
                if ( CollectionUtils.isNotEmpty( file.getLocations() ) ) {
                    List< LocationDTO > locationDTOs = getDocumentLocations( file );
                    dataObjectDTO.setLocations( locationDTOs );
                }
            }

            dataObjectDTO.setDescription( dataObjectHtmlsEntity.getDescription() );
            if ( setCustomAttributes ) {
                dataObjectDTO.setCustomAttributes(
                        CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectHtmlsEntity.getCustomAttributes() ) );
                if ( null != dataObjectHtmlsEntity.getAttachments() && !( dataObjectHtmlsEntity.getAttachments().isEmpty() ) ) {
                    DocumentEntity zipFile = dataObjectHtmlsEntity.getAttachments().iterator().next();
                    dataObjectDTO.setZipFile( documentManager.prepareDocumentDTO( zipFile ) );
                }
            }
            if ( dataObjectHtmlsEntity.getTypeId() != null ) {
                dataObjectDTO.setTypeId( dataObjectHtmlsEntity.getTypeId() );
                dataObjectDTO.setLifeCycleStatus( configManager.getStatusByIdandObjectType( dataObjectHtmlsEntity.getTypeId(),
                        dataObjectHtmlsEntity.getLifeCycleStatus(), dataObjectHtmlsEntity.getConfig() ) );
            }
            if ( null != dataObjectHtmlsEntity.getConfig() ) {
                dataObjectDTO.setType( configManager.getObjectTypeByIdAndConfigName( dataObjectHtmlsEntity.getTypeId().toString(),
                        dataObjectHtmlsEntity.getConfig() ).getName() );
            }
            dataObjectDTO.setSize(
                    dataObjectHtmlsEntity.getSize() != null && dataObjectHtmlsEntity.getSize() > ConstantsInteger.INTEGER_VALUE_ZERO
                            ? org.apache.commons.io.FileUtils.byteCountToDisplaySize( dataObjectHtmlsEntity.getSize() )
                            : ConstantsString.NOT_AVAILABLE );

            objectDTO = dataObjectDTO;
        }
        return objectDTO;
    }

    /**
     * Prepare data object dashboard DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param id
     *         the id
     * @param dashboardEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    @Deprecated( since = "soco/2.3.1/release", forRemoval = true )
    private Object prepareDataObjectDashboardDTO( EntityManager entityManager, UUID id, DataObjectDashboardEntity dashboardEntity,
            boolean setCustomAttributes ) {

        Object objectDTO;
        DataObjectDashboardDTO dashboardDTO = new DataObjectDashboardDTO();
        dashboardDTO.setName( dashboardEntity.getName() );
        dashboardDTO.setId( dashboardEntity.getComposedId().getId() );
        dashboardDTO.setCreatedOn( dashboardEntity.getCreatedOn() );
        dashboardDTO.setModifiedOn( dashboardEntity.getModifiedOn() );
        dashboardDTO.setParentId( id );
        dashboardDTO.setTypeId( dashboardEntity.getTypeId() );
        dashboardDTO.setAutoDeleted( dashboardEntity.isAutoDelete() );
        dashboardDTO.setPlugin( dashboardEntity.getPlugin() );
        Map< String, String > settings = new HashMap<>();
        dashboardDTO.setSettings(
                ( Map< String, String > ) JsonUtils.jsonToMap( ByteUtil.convertByteToString( dashboardEntity.getSettings() ), settings ) );
        if ( null != dashboardEntity.getCreatedBy() ) {
            dashboardDTO.setCreatedBy( userCommonManager.prepareUserModelFromUserEntity( dashboardEntity.getCreatedBy() ) );
        }
        if ( null != dashboardEntity.getModifiedBy() ) {
            dashboardDTO.setModifiedBy( userCommonManager.prepareUserModelFromUserEntity( dashboardEntity.getModifiedBy() ) );
        }

        dashboardDTO.setDescription( dashboardEntity.getDescription() );
        if ( setCustomAttributes ) {
            dashboardDTO.setCustomAttributes(
                    CustomAttributeDTO.prepareCustomAttributesMapFromSet( dashboardEntity.getCustomAttributes() ) );
        }
        if ( dashboardEntity.getTypeId() != null ) {
            dashboardDTO.setTypeId( dashboardEntity.getTypeId() );
            dashboardDTO.setLifeCycleStatus(
                    configManager.getStatusByIdandObjectType( dashboardEntity.getTypeId(), dashboardEntity.getLifeCycleStatus(),
                            dashboardEntity.getConfig() ) );
        }
        if ( null != dashboardEntity.getConfig() ) {
            dashboardDTO.setType(
                    configManager.getObjectTypeByIdAndConfigName( dashboardEntity.getTypeId().toString(), dashboardEntity.getConfig() )
                            .getName() );
        }
        if ( null != dashboardEntity.getSelection() ) {
            prepareAndValidateSelectionForDashboardDTO( entityManager, dashboardEntity, dashboardDTO );
        }

        objectDTO = dashboardDTO;
        return objectDTO;
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
        updatedEntity.setIcon( existingEntity.getIcon() );
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
     * {@inheritDoc}
     */
    @Override
    public ProjectConfiguration getProjectConfigurationOfObject( String userId, UUID objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, objectId );
            List< SuSObjectModel > list = configManager.getObjectTypesByConfigName( susEntity.getConfig() );
            SuSObjectModel objectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            return new ProjectConfiguration( susEntity.getConfig(), list, objectModel );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectViewDTO > getListOfObjectView( String userId, String objectId, UUID typeId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            return getObjectViewList( entityManager, entity, userId, typeId.toString() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO saveOrUpdateObjectView( String userId, String objectId, UUID typeId, String viewJson, boolean isUpdateable ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            if ( entity.getConfig() != null ) {
                SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                        entity.getConfig() );
                String config = entity.getConfig();
                return objectViewManager.saveOrUpdateObjectView( entityManager,
                        prepareObjectViewDTO( config, viewJson, getObjectViewKey( susObjectModel, typeId.toString() ), objectId,
                                isUpdateable ), userId );
            } else {
                return objectViewManager.saveOrUpdateObjectView( entityManager,
                        prepareObjectViewDTO( null, viewJson, typeId.toString(), objectId, isUpdateable ), userId );
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectViewDTO setObjectViewAsDefault( String userId, String objectId, UUID typeId, String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            SuSEntity entity = susDAO.getLatestObjectById( entityManager, SuSEntity.class, UUID.fromString( objectId ) );
            if ( entity == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_NOT_EXIST_WITH_ID.getKey(), objectId ) );
            }
            if ( entity.getConfig() != null ) {
                SuSObjectModel susObjectModel = configManager.getObjectTypeByIdAndConfigName( entity.getTypeId().toString(),
                        entity.getConfig() );
                return objectViewManager.saveDefaultObjectViewByConfig( entityManager, UUID.fromString( viewId ), userId,
                        getObjectViewKey( susObjectModel, typeId.toString() ), null, entity.getConfig() );
            } else {
                return objectViewManager.saveDefaultObjectView( entityManager, UUID.fromString( viewId ), userId, typeId.toString(),
                        entity.getComposedId().getId().toString() );
            }
        } finally {
            entityManager.close();
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
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
        if ( !isUpdateable ) {
            objectViewDTO.setId( null );
        }
        if ( objectViewDTO.getSettings().getSearch().length() > ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.DTO_UI_MIX_VALUE_ERROR_MESSAGE.getKey(),
                    ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) );
        }

        objectViewDTO.setObjectId( objectId );
        objectViewDTO.setConfig( config );
        objectViewDTO.setObjectViewKey( viewKey );
        objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
        objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
        return objectViewDTO;
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
        SuSObjectModel config = null;
        if ( susEntity.getConfig() != null ) {
            config = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(), susEntity.getConfig() );
        }
        if ( null != config ) {
            String viewKey = getObjectViewKey( config, typeId );
            return objectViewManager.getUserObjectViewsByKeyAndConfig( entityManager, viewKey, userId, susEntity.getConfig() );
        } else {
            return objectViewManager.getUserObjectViewsByKey( entityManager, typeId, userId, susEntity.getComposedId().getId().toString() );
        }
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
        List< OVAConfigTab > viewConfig = susObjectModel.getViewConfig();
        for ( OVAConfigTab ovaConfigTab : viewConfig ) {
            if ( null != ovaConfigTab.getTypeId() && ovaConfigTab.getTypeId().equals( typeId ) ) {
                return susObjectModel.getName() + ConstantsString.DOT + ovaConfigTab.getKey();
            }
        }
        return susObjectModel.getName();

    }

    /**
     * Gets the life cycle status by id.
     *
     * @param lifeCycleId
     *         the life cycle id
     *
     * @return the life cycle status by id
     */
    @Override
    public StatusConfigDTO getLifeCycleStatusById( String lifeCycleId ) {
        return lifeCycleManager.getLifeCycleStatusByStatusId( lifeCycleId );
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
    public Object getOverviewByProjectId( String token, String id, String language ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        isPermittedToRead( entityManager, TokenizedLicenseUtil.getNotNullUser( token ).getId(), id );
        try {
            ProjectEntity projectEntity = ( ProjectEntity ) susDAO.getLatestObjectByTypeAndId( entityManager, ProjectEntity.class,
                    UUID.fromString( id ) );
            return readProjectOverviewObject( entityManager, projectEntity, token, language );
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
            ProjectDTO projectDTO = ( ProjectDTO ) createProjectDTOFromProjectEntityWithOverview( null, projectEntity, true );
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
        updateProjectDTOFromEntity( null, false, projectEntity, projectDTO );
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
     * {@inheritDoc}
     */
    @Override
    public List< Object > getContainerOrChildsById( String objectId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getContainerOrChildsById( entityManager, objectId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< Object > getContainerOrChildsById( EntityManager entityManager, String objectId ) {
        List< Object > allObject = new ArrayList<>();
        try {
            List< UUID > selectionIds = selectionManager.getSelectedIdsListBySelectionId( entityManager, objectId );
            for ( UUID sId : selectionIds ) {
                prepareContainersAndChilds( entityManager, allObject, sId );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
        return allObject;
    }

    /**
     * Prepare containers and childs.
     *
     * @param entityManager
     *         the entity manager
     * @param allObject
     *         the all object
     * @param sId
     *         the s id
     */
    private void prepareContainersAndChilds( EntityManager entityManager, List< Object > allObject, UUID sId ) {
        SuSEntity susEntity = susDAO.getLatestNonDeletedObjectById( entityManager, sId );
        if ( susEntity instanceof DataObjectEntity ) {
            allObject.add( createDTOFromSusEntity( entityManager, null, susEntity, false ) );
        } else if ( susEntity instanceof ContainerEntity ) {
            List< SuSEntity > childrenEntities = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                    susEntity.getComposedId().getId() );
            if ( !childrenEntities.isEmpty() ) {
                for ( SuSEntity suSEntity2 : childrenEntities ) {
                    prepareContainersAndChilds( entityManager, allObject, suSEntity2.getComposedId().getId() );
                }
            }
        }
    }

    /**
     * Prepare data object trace DTO.
     *
     * @param entityManager
     *         the entity manager
     * @param projectId
     *         the project id
     * @param dataObjectTraceEntity
     *         the sus entity
     * @param setCustomAttributes
     *         the set custom attributes
     *
     * @return the object
     */
    private Object prepareDataObjectTraceDTO( EntityManager entityManager, UUID projectId, DataObjectTraceEntity dataObjectTraceEntity,
            boolean setCustomAttributes ) {
        DataObjectTraceDTO dataObjectDTO = new DataObjectTraceDTO();
        dataObjectDTO.setName( dataObjectTraceEntity.getName() );
        dataObjectDTO.setId( dataObjectTraceEntity.getComposedId().getId() );
        dataObjectDTO.setVersion( new VersionDTO( dataObjectTraceEntity.getComposedId().getVersionId() ) );
        dataObjectDTO.setCreatedOn( dataObjectTraceEntity.getCreatedOn() );
        dataObjectDTO.setModifiedOn( dataObjectTraceEntity.getModifiedOn() );
        dataObjectDTO.setParentId( projectId );
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
        if ( setCustomAttributes ) {
            dataObjectDTO.setCustomAttributes(
                    CustomAttributeDTO.prepareCustomAttributesMapFromSet( dataObjectTraceEntity.getCustomAttributes() ) );
        }
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
     * Sets the bread crumb manager.
     *
     * @param breadCrumbManager
     *         the new bread crumb manager
     */
    public void setBreadCrumbManager( BreadCrumbManager breadCrumbManager ) {
        this.breadCrumbManager = breadCrumbManager;
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

}
