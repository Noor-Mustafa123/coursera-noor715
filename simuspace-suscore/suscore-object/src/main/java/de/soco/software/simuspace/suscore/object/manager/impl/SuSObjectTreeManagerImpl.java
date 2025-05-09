package de.soco.software.simuspace.suscore.object.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.PermissionMatrixEnum;
import de.soco.software.simuspace.suscore.common.enums.SimuspaceFeaturesEnum;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.JobDAO;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.ReportEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.SystemContainerEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowProjectEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ClickManager;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.data.manager.impl.base.ContextMenuManagerImpl;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.ObjectTreeViewDTO;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;
import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;
import de.soco.software.simuspace.suscore.data.model.TreeRequestSortDTO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.WorkflowEntity;
import de.soco.software.simuspace.suscore.lifecycle.constants.ConstantsLifeCycle;
import de.soco.software.simuspace.suscore.lifecycle.manager.LifeCycleManager;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.dao.BmwDAO;
import de.soco.software.simuspace.suscore.object.manager.SuSObjectTreeManager;
import de.soco.software.simuspace.suscore.object.model.ProjectType;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;

/**
 * Implementation class responsible for generating System Container entities , preparing tree nodes , get container and persisting
 * Container.
 */
@Log4j2
public class SuSObjectTreeManagerImpl implements SuSObjectTreeManager {

    /**
     * The Constant BMW_DUMMY_PROJECT_JSON.
     */
    private static final String BMW_DUMMY_PROJECT_JSON = "BMWDummyProject.json";

    /**
     * The constant BMW_QADYNA_PROJECT_JSON.
     */
    private static final String BMW_QADYNA_PROJECT_JSON = "BMWQADynaProject.json";

    /**
     * The Constant OPEN_WF_JOBS_CONTEXT_URL.
     */
    public static final String OPEN_WF_JOBS_CONTEXT_URL = "view/job/workflow/{objectId}";

    /**
     * The Constant RUN.
     */
    private static final String RUN = "4100058x4";

    /**
     * The Constant VIEW_WORKFLOW_PROJECT_TABLE_URL.
     */
    private static final String VIEW_WORKFLOW_PROJECT_TABLE_URL = "view/workflow/project/";

    /**
     * The Constant SEARCH_QUERY_CREATED_ON.
     */
    private static final String SEARCH_QUERY_CREATED_ON = "created-on";

    /**
     * The Constant SEARCH_QUERY_UPDATED_ON.
     */
    private static final String SEARCH_QUERY_UPDATED_ON = "updated-on";

    /**
     * The Constant COLUMN_CREATED_ON_SUSENTITY.
     */
    private static final String COLUMN_CREATED_ON_SUSENTITY = "createdOn";

    /**
     * The Constant COLUMN_MODIFIED_ON_SUSENTITY.
     */
    private static final String COLUMN_MODIFIED_ON_SUSENTITY = "modifiedOn";

    /**
     * The Constant COLUMN_NAME_SUSENTITY.
     */
    private static final String COLUMN_NAME_SUSENTITY = "name";

    /**
     * The Constant WF_CONFIG_FIELD_ELEMENT.
     */
    private static final String WF_CONFIG_FIELD_ELEMENT = "element";

    /**
     * The Constant WF_CONFIG_FIELD_ICON.
     */
    private static final String WF_CONFIG_FIELD_ICON = "icon";

    /**
     * The Constant WF_CONFIG_FIELD_TITLE.
     */
    private static final String WF_CONFIG_FIELD_TITLE = "title";

    /**
     * The Constant WF_CONFIG_FIELD_ELEMENTS.
     */
    private static final String WF_CONFIG_FIELD_ELEMENTS = "elements";

    /**
     * The Constant WF_CONFIG_FIELD_CHILDREN.
     */
    private static final String WF_CONFIG_FIELD_CHILDREN = "children";

    /**
     * The Constant VIEW_OBJECTS_URL.
     */
    public static final String VIEW_PROJECT_URL = "view/data/project/";

    /**
     * The Constant CREATE_CONTAINER_CONTEXT_URL.
     */
    public static final String DELETE_OBJECT_CONTEXT_URL = "delete/data/object/{objectId}/?mode=single";

    /**
     * The Constant CHANGE_STATUS_OBJECT_CONTEXT_URL.
     */
    public static final String CHANGE_STATUS_OBJECT_CONTEXT_URL = "update/data/object/{objectId}/status";

    /**
     * The Constant EDIT_OBJECT_CONTEXT_URL.
     */
    public static final String EDIT_OBJECT_CONTEXT_URL = "update/data/project/{parentId}";

    /**
     * The Constant EDIT_DATA_OBJECT_CONTEXT_URL.
     */
    public static final String EDIT_DATA_OBJECT_CONTEXT_URL = "update/data/object/{objectId}";

    /**
     * The constant for create container.
     */
    public static final String DELETE_OBJECT_TITLE = "Delete";

    /**
     * The Constant ADD_META_DATA_TO_OBJECT_CONTEXT_URL.
     */
    public static final String ADD_META_DATA_TO_OBJECT_CONTEXT_URL = "add/data/object/{objectId}";

    /**
     * The properties file name with complete path.
     */
    public static final String PROPERTIES_FILE_PATH = ConstantsKaraf.KARAF_CONF_PATH + File.separator + "Workflow_config.json";

    /**
     * The properties file name with complete path.
     */
    public static final String HPC_ELEMENTS_CONFIG_PATH = ConstantsKaraf.KARAF_CONF_PATH + File.separator + "Hpc_config.json";

    /**
     * The Constant PERMISSION_DATA_TO_OBJECT_CONTEXT_URL.
     */
    public static final String PERMISSION_DATA_TO_OBJECT_CONTEXT_URL = "perm/data/object/{objectId}";

    /**
     * The Constant PERMISSION_PROJECT_CONTEXT_URL.
     */
    public static final String PERMISSION_PROJECT_CONTEXT_URL = "perm/data/project/{projectId}";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "{selectionId}";

    /**
     * The Constant OBJECT_ID_PARAM.
     */
    public static final String OBJECT_ID_PARAM = "{objectId}";

    /**
     * The Constant PROJECT_ID_PARAM.
     */
    public static final String PROJECT_ID_PARAM = "{projectId}";

    /**
     * Constant Table view type.
     */
    private static final String TABLE = "table";

    /**
     * The Constant SYSTEM_CONTAINER.
     */
    private static final String SYSTEM_CONTAINER = "SystemContainer";

    /**
     * The Constant MAXIMUM_NUMBER_OF_OBJECTS_THAT_A_CONTAINER_CAN_HAVE_IN_TREE.
     */
    private static final int MAXIMUM_NUMBER_OF_OBJECTS_THAT_A_CONTAINER_CAN_HAVE_IN_TREE = Integer.parseInt(
            PropertiesManager.getInstance().getProperty( ConstantsFileProperties.MAX_TREE_CHILDREN ) );

    /**
     * The Constant TREE_CONTEXT.
     */
    private static final String TREE_CONTEXT = "tree-context";

    /**
     * The Constant GROUPS_NODE_TITLE.
     */
    private static final String GROUPS_NODE_TITLE = "Groups";

    /**
     * The Constant ALL_WORKFLOWS_NODE_TITLE.
     */
    private static final String ALL_WORKFLOWS_NODE_TITLE = "All Workflows";

    /**
     * The Constant AUDIT_LOGS_NODE_TITLE.
     */
    private static final String AUDIT_LOGS_NODE_TITLE = "Audit Logs";

    /**
     * The Constant ROLE_NODE_TITLE.
     */
    private static final String ROLE_NODE_TITLE = "Roles";

    /**
     * The Constant PERMISSIONS_NODE_TITLE.
     */
    public static final String PERMISSIONS_NODE_TITLE = "4100012x4";

    /**
     * The Constant PERMISSIONS_NODE_TREE_TITLE.
     */
    public static final String RIGHTS_NODE_TREE_TITLE = "Rights";

    /**
     * The Constant DIRECTORIES_NODE_TITLE.
     */
    private static final String DIRECTORIES_NODE_TITLE = "Directories";

    /**
     * The constant for creating copy.
     */
    public static final String CREATE_COPY_TITLE = "4100059x4";

    /**
     * The constant for creating dummy.
     */
    public static final String CREATE_DUMMY_TITLE = "4100031x4";

    /**
     * The Constant ADD_META_DATA_TITLE.
     */
    public static final String ADD_META_DATA_TITLE = "4100008x4";

    /**
     * The Constant USERS_NODE_TITLE.
     */
    private static final String USERS_NODE_TITLE = "Users";

    /**
     * The Constant LOCATIONS_NODE_TITLE.
     */
    private static final String LOCATIONS_NODE_TITLE = "Locations";

    /**
     * The Constant LICENSE_NODE_TITLE.
     */
    private static final String LICENSE_NODE_TITLE = "License";

    /**
     * The Constant SYSTEM_NODE_TITLE.
     */
    private static final String SYSTEM_NODE_TITLE = "System";

    /**
     * The Constant ROOT_NODE_TITLE.
     */
    private static final String ROOT_NODE_TITLE = "Root";

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant COLON.
     */
    private static final String COLON = ":";

    /**
     * The Constant CREATE_WORKFLOW_CONTEXT_URL.
     */
    public static final String CREATE_WORKFLOW_CONTEXT_URL = "create/workflow/{parentId}";

    /**
     * The Constant CREATE_WORKFLOW_TITLE.
     */
    public static final String CREATE_WORKFLOW_TITLE = "4100036x4";

    /**
     * The Constant CREATE_WORKFLOW_PROJECT_TITLE.
     */
    public static final String CREATE_WORKFLOW_PROJECT_TITLE = "4100015x4";

    /**
     * The Constant CHANGE_STATUS_TITLE.
     */
    public static final String CHANGE_STATUS_TITLE = "Change Status";

    /**
     * The Constant EDIT_TITLE.
     */
    public static final String EDIT_TITLE = "4100014x4";

    /**
     * The Constant CREATE_TITLE.
     */
    public static final String CREATE_TITLE = "4100018x4";

    /**
     * The Constant DELETED_OBJECTS.
     */
    private static final String DELETED_OBJECTS = "Deleted objects";

    /**
     * The Constant SEARCH.
     */
    private static final String SEARCH = "Search";

    /**
     * The Constant WORKFLOW_MENU_ICON.
     */
    private static final String WORKFLOW_MENU_ICON = "icon-workflow";

    /**
     * The Constant IMPORT_WORKFLOW.
     */
    private static final String IMPORT_WORKFLOW = "4100017x4";

    /**
     * The Constant DOWNLOAD_CSV_FILE.
     */
    private static final String DOWNLOAD_CSV_FILE = "4100053x4";

    /**
     * The Constant CSV_FILE_DOWNLOAD_ADDRESS.
     */
    public static final String CSV_FILE_DOWNLOAD_ADDRESS = "job/{objectId}/csv/download";

    /**
     * The Constant PARAM_PARENT_ID.
     */
    public static final String PARAM_PARENT_ID = "{parentId}";

    /**
     * The Constant SHOW_ALL_ICON.
     */
    public static final String SHOW_ALL_ICON = "fa fa-chevron-right";

    /**
     * The Constant CLIENT_VISIBILITY.
     */
    public static final String CLIENT_VISIBILITY = "client";

    /**
     * The Constant CREAT_STRUCTURE_TITLLE.
     */
    public static final String CREAT_STRUCTURE_TITLLE = "4100077x4";

    /**
     * The Constant CREAT_STRUCTURE_CONTEXT_URL.
     */
    public static final String CREAT_STRUCTURE_CONTEXT_URL = "get/data/project/{projectId}/sync/directory";

    /**
     * The Constant EXPORT_PROJECT_CONTEXT_URL.
     */
    public static final String EXPORT_PROJECT_CONTEXT_URL = "export/data/{containerId}/{selectionId}";

    /**
     * The Constant CONTAINER_ID_PARAM.
     */
    public static final String CONTAINER_ID_PARAM = "{containerId}";

    /**
     * The Constant EXPORT_TITLE.
     */
    public static final String EXPORT_TITLE = "4100075x4";

    /**
     * The Constant KEY.
     */
    private static final String KEY = "?key=";

    /**
     * The Constant KEY_PATTERN.
     */
    public static final String KEY_PATTERN = "{?key}";

    /**
     * The Constant GENERATE_IMAGE_URL.
     */
    public static final String GENERATE_IMAGE_URL = "job/{id}/generateimage{?key}";

    /**
     * The Constant SUS_DEFAULT_VIEW_NAME.
     */
    private static final String SUS_DEFAULT_VIEW_NAME = "SusDefault";

    /**
     * The config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The bmw DAO.
     */
    private BmwDAO bmwDAO;

    /**
     * The user common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The life cycle manager.
     */
    private LifeCycleManager lifeCycleManager;

    /**
     * The click managers.
     */
    private List< ClickManager > clickManagers;

    private JobDAO jobDao;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > findMenu( String userId, String token, String id ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return findMenu( entityManager, userId, token, id );
        } finally {
            entityManager.close();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > findMenu( EntityManager entityManager, String userId, String token, String id ) {

        UserEntity userEntity = userCommonManager.getUserEntityById( entityManager, UUID.fromString( userId ) );
        if ( BooleanUtils.isTrue( userEntity.isRestricted() ) ) {
            return new ArrayList<>();
        }

        var jobEntity = jobDao.getJob( entityManager, UUID.fromString( id ) );
        if ( jobEntity != null ) {
            return findJobMenu( token, jobEntity );
        }
        SuSEntity susEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SuSEntity.class, UUID.fromString( id ) );

        if ( null == susEntity || susEntity.isHidden() ) {
            return new ArrayList<>(); // if hidden item then show empty context
        }

        if ( !( susEntity instanceof WorkflowProjectEntity ) && !( susEntity instanceof SystemContainerEntity )
                && susEntity instanceof ContainerEntity containerEntity ) {
            return findContainerMenu( entityManager, userId, containerEntity );
        }
        if ( susEntity instanceof WorkflowProjectEntity workflowProjectEntity ) {
            return findWorkflowProjectMenu( entityManager, userId, workflowProjectEntity );
        }
        if ( susEntity instanceof WorkflowEntity workflow ) {
            return findWorkflowMenu( entityManager, userId, workflow );
        }

        List< ContextMenuItem > cml = new ArrayList<>();
        for ( String key : Activator.getRouters().keySet() ) {

            for ( RouterConfigItem i : Activator.getRouters().get( key ) ) {

                if ( i.getContainer() != null && i.getContainer().equalsIgnoreCase( id ) && i.getType().equalsIgnoreCase( TREE_CONTEXT ) ) {

                    ContextMenuItem cm = new ContextMenuItem();

                    cm.setUrl( i.getUrl().replace( PARAM_PARENT_ID, id ) );

                    cm.setTitle( MessageBundleFactory.getMessage( i.getTitle() ) );
                    cml.add( cm );
                }
            }
        }

        if ( !( susEntity instanceof SystemContainerEntity ) ) {

            cml.addAll( getContainingObjectsCreateContextMenu( susEntity ) );

            if ( permissionManager.isPermitted( entityManager, userId,
                    susEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
                cml.add( prepareDeleteObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareStatusChangeObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareAddMetaDataToObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareEditMetaDataToObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareEditObjectContext( susEntity ) );
                cml.add( preparePermissionDataToObjectContext( susEntity.getComposedId().getId() ) );
                if ( !( susEntity instanceof ReportEntity ) ) {
                    cml.add( prepareExportDataContext( entityManager, susEntity ) );
                }
            } else if ( permissionManager.isPermitted( entityManager, userId,
                    susEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                cml.add( prepareStatusChangeObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareAddMetaDataToObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareEditMetaDataToObjectContext( susEntity.getComposedId().getId() ) );
                cml.add( prepareEditObjectContext( susEntity ) );
                cml.add( preparePermissionDataToObjectContext( susEntity.getComposedId().getId() ) );
                if ( !( susEntity instanceof ReportEntity ) ) {
                    cml.add( prepareExportDataContext( entityManager, susEntity ) );
                }
            } else if ( permissionManager.isPermitted( entityManager, userId,
                    susEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                cml.add( preparePermissionDataToObjectContext( susEntity.getComposedId().getId() ) );
                if ( !( susEntity instanceof ReportEntity ) ) {
                    cml.add( prepareExportDataContext( entityManager, susEntity ) );
                }
            }

            if ( susEntity instanceof ContainerEntity ) {
                cml.add( prepareCreatStructuerContext( susEntity.getComposedId().getId() ) );
            }
        }

        for ( ClickManager cm : clickManagers ) {
            cml.addAll( cm.findMenu( susEntity ) );
        }
        return cml;
    }

    /**
     * Prepare add meta data to object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareAddMetaDataToObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( ADD_META_DATA_TO_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( ADD_META_DATA_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare edit meta data to object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareEditMetaDataToObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( "edit/data/object/" + objectId );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100115x4" ) );
        return containerCMI;
    }

    /**
     * Prepare permission data to object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem preparePermissionDataToObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( PERMISSION_DATA_TO_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( PERMISSIONS_NODE_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare permission project context.
     *
     * @param projectId
     *         the project id
     *
     * @return the context menu item
     */
    private ContextMenuItem preparePermissionProjectContext( UUID projectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( PERMISSION_PROJECT_CONTEXT_URL.replace( PROJECT_ID_PARAM, ConstantsString.EMPTY_STRING + projectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( PERMISSIONS_NODE_TITLE ) );
        return containerCMI;
    }

    /**
     * Persist some tree nodes, add system features and insert super user at the start of application.
     */
    public void bootloading() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            persistSystemNodes( entityManager );
        } finally {
            entityManager.close();
        }

    }

    /**
     * populates all available system nodes.
     *
     * @param entityManager
     *         the entity manager
     */
    private void persistSystemNodes( EntityManager entityManager ) {
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), ROOT_NODE_TITLE, null,
                "fa fa-briefcase font-red" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), SYSTEM_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "admin_panel_settings" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.DATA.getId() ), PropertiesManager.getDataNodeName(),
                UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "database" );

        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.JOBS.getId() ), SimuspaceFeaturesEnum.JOBS.getKey(),
                UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "work" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.SEARCH.getId() ), SEARCH,
                UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "search" );

        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ),
                SimuspaceFeaturesEnum.ALLWORKFLOWS.getKey(), UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "rebase" );

        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.ELEMENTS.getId() ),
                SimuspaceFeaturesEnum.ELEMENTS.getKey(), null, "jump_to_element" );

        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.LICENSES.getId() ), LICENSE_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "license" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.USERS.getId() ), USERS_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "group" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.LOCATIONS.getId() ), LOCATIONS_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "location_on" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.DIRECTORIES.getId() ), DIRECTORIES_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "folder_open" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.RIGHTS.getId() ), RIGHTS_NODE_TREE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "lock_person" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.ROLES.getId() ), ROLE_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.RIGHTS.getId() ), "passkey" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.AUDIT.getId() ), AUDIT_LOGS_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.SYSTEM.getId() ), "published_with_changes" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.GROUPS.getId() ), GROUPS_NODE_TITLE,
                UUID.fromString( SimuspaceFeaturesEnum.RIGHTS.getId() ), "groups" );
        persistSystemContainer( entityManager, UUID.fromString( SimuspaceFeaturesEnum.DELETED_OBJECTS.getId() ), DELETED_OBJECTS,
                UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), "delete" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TreeNodeDTO > getTree( String client, String currentUserId, UUID parentId, ObjectTreeViewDTO filter, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return getTree( entityManager, client, currentUserId, parentId, filter, token );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< TreeNodeDTO > getTree( EntityManager entityManager, String client, String currentUserId, UUID parentId,
            ObjectTreeViewDTO filter, String token ) {
        if ( client != null && !client.isEmpty() && filter.getOptional() != null ) {
            switch ( client ) {
                case "bmw-dummyfiles", "bmw-loadcases" -> {
                    return bmwDAO.getDummyTree( filter.getOptional() );
                }
                default -> {
                    // do nothing
                }
            }
        }
        if ( parentId == null ) {
            return getContainer( entityManager, currentUserId, UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ), filter, token );
        } else {
            return getContainerChildren( entityManager, currentUserId, parentId, filter, token );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ObjectTreeViewDTO > getAllObjectTreeViews( String userId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< ObjectTreeViewDTO > list = new ArrayList<>();
            List< ObjectViewDTO > userViews = objectViewManager.getUserObjectViewsByKey( entityManager,
                    ConstantsObjectViewKey.OBJECT_TREE_VIEW_KEY, userId, null );
            for ( ObjectViewDTO viewDTO : userViews ) {
                List< TreeNodeDTO > treeNodeList = JsonUtils.jsonToList( viewDTO.getObjectViewJson(), TreeNodeDTO.class );
                if ( CollectionUtils.isEmpty( treeNodeList ) ) {
                    treeNodeList = getTree( entityManager, null, userId, null, null, token );
                }
                filterObjectTreeViewNodesForDeletedNodes( entityManager, treeNodeList );
                ObjectTreeViewDTO treeViewDTO = prepareObjectTreeViewDTOFromObjectViewDTO( viewDTO );
                treeViewDTO.setView_nodes( treeNodeList );
                list.add( treeViewDTO );
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
    public ObjectTreeViewDTO saveObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId, String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ObjectViewDTO viewDTO = new ObjectViewDTO();
            viewDTO.setId( null );
            return getObjectTreeViewDTO( entityManager, objectTreeViewDTO, userId, viewDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectTreeViewDTO saveAsObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId, String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ObjectViewDTO viewDTO = new ObjectViewDTO();
            if ( objectTreeViewDTO.isDefaultView() && !isSusDefaultView( entityManager, objectTreeViewDTO ) ) {
                viewDTO.setId( objectTreeViewDTO.getId() );
            } else {
                viewDTO.setId( null );
            }
            return getObjectTreeViewDTO( entityManager, objectTreeViewDTO, userId, viewDTO );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets object tree view dto.
     *
     * @param entityManager
     *         the entity manager
     * @param objectTreeViewDTO
     *         the object tree view dto
     * @param userId
     *         the user id
     * @param entityManager
     *         the entity manager
     * @param viewDTO
     *         the view dto
     *
     * @return the object tree view dto
     */
    private ObjectTreeViewDTO getObjectTreeViewDTO( EntityManager entityManager, ObjectTreeViewDTO objectTreeViewDTO, String userId,
            ObjectViewDTO viewDTO ) {
        viewDTO.setName( objectTreeViewDTO.getName() );
        viewDTO.setObjectViewType( ConstantsObjectViewType.OBJECT_TREE_VIEW_TYPE );
        viewDTO.setSearch( objectTreeViewDTO.getSearch() );
        viewDTO.setDefaultView( objectTreeViewDTO.isDefaultView() );
        if ( objectTreeViewDTO.getSort() != null ) {
            viewDTO.setSortDirection( objectTreeViewDTO.getSort().getDir() );
            viewDTO.setSortParameter( objectTreeViewDTO.getSort().getParam() );
        }
        for ( TreeNodeDTO treeNode : objectTreeViewDTO.getView_nodes() ) {
            treeNode.setChildren( null );
        }
        viewDTO.setObjectViewJson( JsonUtils.convertListToJson( objectTreeViewDTO.getView_nodes() ) );
        viewDTO.setObjectViewKey( ConstantsObjectViewKey.OBJECT_TREE_VIEW_KEY );
        ObjectViewDTO objectViewDTO = objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
        if ( null != objectViewDTO ) {
            return prepareObjectTreeViewDTOFromObjectViewDTO( objectViewDTO );
        }
        return null;
    }

    /**
     * Is sus default view.
     *
     * @param entityManager
     *         the entity manager
     * @param objectTreeViewDTO
     *         the objectTreeViewDTO
     *
     * @return the boolean
     */
    private boolean isSusDefaultView( EntityManager entityManager, ObjectTreeViewDTO objectTreeViewDTO ) {
        ObjectViewDTO viewDTO = objectViewManager.getObjectViewById( entityManager, UUID.fromString( objectTreeViewDTO.getId() ) );
        return SUS_DEFAULT_VIEW_NAME.equals( viewDTO.getName() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectTreeViewDTO updateObjectTreeView( ObjectTreeViewDTO objectTreeViewDTO, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            ObjectTreeViewDTO objectTreeViewDTOToReturn = null;
            ObjectViewDTO viewDTO = objectViewManager.getObjectViewById( entityManager, UUID.fromString( objectTreeViewDTO.getId() ) );
            viewDTO.setObjectViewJson( JsonUtils.convertListToJson( objectTreeViewDTO.getView_nodes() ) );
            if ( !viewDTO.getName().equals( SUS_DEFAULT_VIEW_NAME ) ) { // susDefault view name cannot be changed
                viewDTO.setName( objectTreeViewDTO.getName() );
            }
            if ( objectTreeViewDTO.getSearch().length() > ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.DTO_UI_MIX_VALUE_ERROR_MESSAGE.getKey(),
                        ConstantsInteger.DEFAULT_DESCRIPTION_LENGTH ) );
            }
            viewDTO.setSearch( objectTreeViewDTO.getSearch() );
            viewDTO.setDefaultView( objectTreeViewDTO.isDefaultView() );
            if ( objectTreeViewDTO.getSort() != null ) {
                viewDTO.setSortDirection( objectTreeViewDTO.getSort().getDir() );
                viewDTO.setSortParameter( objectTreeViewDTO.getSort().getParam() );
            } else {
                viewDTO.setSortDirection( null );
                viewDTO.setSortParameter( null );
            }

            ObjectViewDTO viewDTOTemp = objectViewManager.saveOrUpdateObjectView( entityManager, viewDTO, userId );
            if ( null != viewDTOTemp ) {
                return prepareObjectTreeViewDTOFromObjectViewDTO( viewDTOTemp );
            }
            return objectTreeViewDTOToReturn;
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setObjectTreeViewAsDefault( UUID viewId, String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            objectViewManager.saveDefaultObjectView( entityManager, viewId, userId, ConstantsObjectViewKey.OBJECT_TREE_VIEW_KEY, null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteObjectTreeView( UUID viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, viewId );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Preparation of deleting object from context menu.
     *
     * @param objectId
     *         UUID
     *
     * @return ContextMenutItem context menu item
     */
    private ContextMenuItem prepareDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( DELETE_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100007x4" ) );
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
        containerCMI.setUrl( CHANGE_STATUS_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + id ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100005x4" ) );
        return containerCMI;
    }

    /**
     * Prepare edit object context.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareEditObjectContext( SuSEntity susEntity ) {

        ContextMenuItem containerCMI = new ContextMenuItem();
        if ( susEntity instanceof ContainerEntity ) {
            containerCMI.setUrl(
                    EDIT_OBJECT_CONTEXT_URL.replace( PARAM_PARENT_ID, ConstantsString.EMPTY_STRING + susEntity.getComposedId().getId() ) );
        } else {
            // its DataObject
            containerCMI.setUrl( EDIT_DATA_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM,
                    ConstantsString.EMPTY_STRING + susEntity.getComposedId().getId() ) );

        }
        containerCMI.setTitle( MessageBundleFactory.getMessage( EDIT_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare creat structuer context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCreatStructuerContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( CREAT_STRUCTURE_CONTEXT_URL.replace( PROJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( CREAT_STRUCTURE_TITLLE ) );
        containerCMI.setVisibility( CLIENT_VISIBILITY );

        return containerCMI;
    }

    /**
     * Prepare create workflow context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareCreateWorkflowContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( CREATE_WORKFLOW_CONTEXT_URL.replace( PARAM_PARENT_ID, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( CREATE_WORKFLOW_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare object tree view DTO from object view DTO.
     *
     * @param objectViewDTO
     *         the object view DTO
     *
     * @return the object tree view DTO
     */
    private ObjectTreeViewDTO prepareObjectTreeViewDTOFromObjectViewDTO( ObjectViewDTO objectViewDTO ) {
        ObjectTreeViewDTO treeViewDTO = new ObjectTreeViewDTO();
        treeViewDTO.setId( objectViewDTO.getId() );
        treeViewDTO.setName( objectViewDTO.getName() );
        treeViewDTO.setDefaultView( objectViewDTO.isDefaultView() );
        treeViewDTO.setObjectViewJson( objectViewDTO.getObjectViewJson() );
        treeViewDTO.setObjectViewKey( objectViewDTO.getObjectViewKey() );
        treeViewDTO.setObjectViewType( objectViewDTO.getObjectViewType() );
        treeViewDTO.setView_nodes( JsonUtils.jsonToList( objectViewDTO.getObjectViewJson(), TreeNodeDTO.class ) );
        treeViewDTO.setSearch( objectViewDTO.getSearch() );
        if ( objectViewDTO.getSortDirection() != null && objectViewDTO.getSortParameter() != null ) {
            treeViewDTO.setSort( new TreeRequestSortDTO( objectViewDTO.getSortDirection(), objectViewDTO.getSortParameter() ) );
        }
        return treeViewDTO;
    }

    /**
     * Filter node roots for tree views based on their view states.
     *
     * @param filter
     *         the filter
     * @param childrenEntities
     *         the children entities
     *
     * @return the list
     */
    private List< SuSEntity > filterRootNodesForObjectTreeViews( ObjectTreeViewDTO filter, List< SuSEntity > childrenEntities ) {
        List< SuSEntity > listToReturn = new ArrayList<>();
        for ( SuSEntity suSEntity : childrenEntities ) {
            for ( TreeNodeDTO treeNode : filter.getView_nodes() ) {
                if ( treeNode.getState() == ConstantsInteger.INTEGER_VALUE_ONE && treeNode.getId()
                        .equals( suSEntity.getComposedId().getId().toString() ) ) {
                    listToReturn.add( suSEntity );
                }
            }
        }
        if ( CollectionUtil.isEmpty( listToReturn ) ) {
            return childrenEntities;
        }

        return listToReturn;
    }

    /**
     * Filter object tree view nodes for deleted nodes.
     *
     * @param entityManager
     *         the entity manager
     * @param treeNodeList
     *         the tree node list
     */
    private void filterObjectTreeViewNodesForDeletedNodes( EntityManager entityManager, List< TreeNodeDTO > treeNodeList ) {
        for ( Iterator< TreeNodeDTO > iterator = treeNodeList.iterator(); iterator.hasNext(); ) {
            TreeNodeDTO treeNode = iterator.next();
            SuSEntity susEntityFromDB = susDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( treeNode.getId() ) );
            if ( null == susEntityFromDB ) {
                iterator.remove();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void persistSystemContainer( EntityManager entityManager, UUID id, String name, UUID parentId, String icon ) {
        SuSEntity entity = susDAO.getLatestObjectByTypeAndId( entityManager, SystemContainerEntity.class, id );
        log.info( MessageBundleFactory.getMessage( Messages.PERSIST_SYSTEM_CONTAINER.getKey(), name ) );
        TreeNodeDTO treeNode = null;
        if ( entity == null ) {
            // persist and return
            entity = susDAO.createAnObject( entityManager, generateSystemContainerEntity( id, name, icon ) );
            SuSEntity parentEntity = susDAO.getLatestObjectByTypeAndId( entityManager, SystemContainerEntity.class, parentId );
            if ( null != parentEntity ) {
                Relation r = new Relation( parentEntity.getComposedId().getId(), entity.getComposedId().getId() );
                susDAO.createRelation( entityManager, r );
            }
        } else {
            // change data node name if changed in simuspace.cfg
            if ( !name.equals( entity.getName() ) ) {
                entity.setName( name );
                susDAO.update( entityManager, entity );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TreeNodeDTO getTreeForField( String token, ObjectTreeViewDTO objectTreeViewDTO, String fieldType ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( FieldTypes.SUS_OBJECT.getType().equals( fieldType ) ) {
                return getContainerWithChildren( entityManager, Objects.requireNonNull( TokenizedLicenseUtil.getUser( token ) ).getId(),
                        UUID.fromString( SimuspaceFeaturesEnum.DATA.getId() ), objectTreeViewDTO, token );
            } else if ( FieldTypes.SUS_WORKFLOW.getType().equals( fieldType ) || FieldTypes.SUBWORKFLOW.getType().equals( fieldType ) ) {
                return getContainerWithChildren( entityManager, Objects.requireNonNull( TokenizedLicenseUtil.getUser( token ) ).getId(),
                        UUID.fromString( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ), objectTreeViewDTO, token );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FIELD_NOT_FOUND_IN_CLASS.getKey(), fieldType,
                        FieldTypes.class.getSimpleName() ) );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare tree node DTO lazily.
     *
     * @param entityManager
     *         the entity manager
     * @param treeNode
     *         the tree node
     * @param userId
     *         the user name
     * @param susEntity
     *         the sus entity
     * @param treeView
     *         the treeView
     * @param token
     *         the token
     */
    private void prepareTreeNodeDTOLazily( EntityManager entityManager, TreeNodeDTO treeNode, String userId, SuSEntity susEntity,
            ObjectTreeViewDTO treeView, String token ) {

        log.info( ">>prepareTreeNodeDTOLazily start" );

        List< SuSEntity > childrenEntities;
        Long count;

        if ( susEntity instanceof SystemContainerEntity ) {

            String lifecycleId = lifeCycleManager.getLifeCycleConfigurationByFileName( ConstantsLifeCycle.LIFE_CYCLE_CONFIG_FILE_NAME )
                    .get( 0 ).getId();
            if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) {
                List< String > lifeCycleList = lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId );
                List< String > lifeCycleListVisible = lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId );
                FiltersDTO customFilter = prepareFilterForMaxNumberOfChildren( susEntity.getComposedId().getId(), treeView );
                childrenEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycleAndPermissionWithoutCount( entityManager,
                        ContainerEntity.class, customFilter, susEntity.getComposedId().getId(), userId, lifeCycleList, lifeCycleListVisible,
                        PermissionMatrixEnum.VIEW.getKey(), configManager.getTypesFromConfiguration( susEntity.getConfig() ) );
                count = ( long ) childrenEntities.size();
            } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                List< String > lifeCycleList = lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId );
                List< String > lifeCycleListVisible = lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId );
                FiltersDTO customFilter = prepareFilterForMaxNumberOfChildren( susEntity.getComposedId().getId(), treeView );
                childrenEntities = ( List< SuSEntity > ) susDAO.getAllFilteredRecordsWithParentAndLifeCycleWithLanguageAndPermissionWithoutCount(
                        entityManager, ContainerEntity.class, customFilter, susEntity.getComposedId().getId(), userId, lifeCycleList,
                        lifeCycleListVisible, configManager.getTypesFromConfiguration( susEntity.getConfig() ),
                        CommonUtils.resolveLanguage( token ), PermissionMatrixEnum.VIEW.getKey() );
                count = ( long ) childrenEntities.size();
            } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.CONFIGURATION.getId() ) ) {

                FiltersDTO customFilter = prepareFilterForMaxNumberOfChildren( susEntity.getComposedId().getId(), treeView );

                childrenEntities = susDAO.getAllFilteredRecordsWithParent( entityManager, ContainerEntity.class, customFilter,
                        susEntity.getComposedId().getId() );
                count = customFilter.getFilteredRecords();
                childrenEntities.forEach( entity -> {
                    if ( !entity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                        translateSystemEntityName( entity );
                    }
                } );
                childrenEntities.removeIf( entity -> !permissionManager.isPermitted( entityManager, userId,
                        entity.getComposedId().getId() + ConstantsString.COLON + PermissionMatrixEnum.VIEW.getValue() ) );
            } else {

                FiltersDTO customFilter = prepareFilterForMaxNumberOfChildren( susEntity.getComposedId().getId(), treeView );
                childrenEntities = susDAO.getAllFilteredRecordsWithParent( entityManager, ContainerEntity.class, customFilter,
                        susEntity.getComposedId().getId() );
                count = customFilter.getFilteredRecords();
                childrenEntities.forEach( entity -> {
                    if ( !entity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                        translateSystemEntityName( entity );
                    }
                } );
            }
        } else {

            SuSObjectModel entityModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                    susEntity.getConfig() );
            String lifecycleId = entityModel.getLifeCycle();
            List< String > lifeCycleList = lifeCycleManager.getOwnerVisibleStatusByPolicyId( lifecycleId );
            List< String > lifeCycleListVisible = lifeCycleManager.getAnyVisibleStatusByPolicyId( lifecycleId );
            FiltersDTO customFilter = prepareFilterForMaxNumberOfChildren( susEntity.getComposedId().getId(), treeView );

            childrenEntities = susDAO.getAllFilteredRecordsWithParentAndLifeCycle( entityManager, ContainerEntity.class, customFilter,
                    susEntity.getComposedId().getId(), userId, lifeCycleList, lifeCycleListVisible,
                    configManager.getTypesFromConfiguration( susEntity.getConfig() ) );
            count = customFilter.getFilteredRecords();
            childrenEntities.forEach( entity -> {
                UserDTO user = TokenizedLicenseUtil.getUser(
                        BundleUtils.getUserTokenFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() ) );
                translateName( user, entity );
            } );
        }

        if ( treeView != null && CollectionUtils.isNotEmpty( treeView.getView_nodes() ) ) {
            childrenEntities = filterRootNodesForObjectTreeViews( treeView, childrenEntities );
        }

        List< TreeNodeDTO > childrenDTO = new ArrayList<>();
        if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ELEMENTS.getId() ) ) {
            childrenDTO = populateWorkflowElements();
        }

        if ( childrenEntities != null ) {
            // 1st loop
            for ( SuSEntity child : childrenEntities ) {
                TreeNodeDTO childNode = populateTreeNodeWithoutReadPermission( child );
                if ( ( child instanceof ContainerEntity ) ) {
                    childrenDTO.add( childNode );
                    childNode.setLazy( hasChild( entityManager, child ) );
                } else {
                    childNode.setLazy( false );
                }
            }
            if ( count > MAXIMUM_NUMBER_OF_OBJECTS_THAT_A_CONTAINER_CAN_HAVE_IN_TREE ) {
                childrenDTO.add( showAllTreeNode( susEntity ) );
            }
        }
        treeNode.setChildren( childrenDTO );
    }

    /**
     * Checks for child.
     *
     * @param entityManager
     *         the entity manager
     * @param susEntity
     *         the sus entity
     *
     * @return true, if successful
     */
    private boolean hasChild( EntityManager entityManager, SuSEntity susEntity ) {
        if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ELEMENTS.getId() ) ) {
            return true;
        }
        Long childCounts = susDAO.getAllRecordsCountWithParent( entityManager, susEntity.getComposedId().getId() );
        return childCounts > 0L;
    }

    /**
     * Populates workflow elements from configurations.
     *
     * @return the list
     */
    private List< TreeNodeDTO > populateWorkflowElements() {
        List< TreeNodeDTO > workflowElements = new ArrayList<>();

        final JSONParser parser = new JSONParser();

        try {
            final Object obj = parser.parse( new FileReader( PROPERTIES_FILE_PATH ) );

            final JSONObject jsonObject = ( JSONObject ) obj;
            // loop array
            final JSONArray msg = ( JSONArray ) jsonObject.get( WF_CONFIG_FIELD_ELEMENTS );

            for ( JSONObject object : ( Iterable< JSONObject > ) msg ) {
                TreeNodeDTO childNode = new TreeNodeDTO();

                if ( object.containsKey( "id" ) && object.get( "id" ).equals( ConstantsID.HPC_CONTAINER_ID ) ) {
                    workflowElements.add( getHpcContainerWithElements( object ) );
                    continue;
                }

                childNode.setId( UUID.randomUUID().toString() );
                childNode.setTitle( ( String ) object.get( WF_CONFIG_FIELD_TITLE ) );
                childNode.setIcon( ( String ) object.get( WF_CONFIG_FIELD_ICON ) );
                childNode.setElement( object.get( WF_CONFIG_FIELD_ELEMENT ) );
                childNode.setFolder( false );
                childNode.setLazy( false );

                if ( object.containsKey( WF_CONFIG_FIELD_CHILDREN ) && object.get( WF_CONFIG_FIELD_CHILDREN ) != null ) {
                    childNode.setChildren( getElementChildren( object ) );
                }

                workflowElements.add( childNode );
            }

        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }

        return workflowElements;
    }

    /**
     * Gets element children.
     *
     * @param element
     *         the element
     *
     * @return the element children
     */
    private List< TreeNodeDTO > getElementChildren( JSONObject element ) {
        List< TreeNodeDTO > childElements = new ArrayList<>();

        final JSONArray children = ( JSONArray ) element.get( WF_CONFIG_FIELD_CHILDREN );

        for ( JSONObject child : ( Iterable< JSONObject > ) children ) {
            TreeNodeDTO childNode = new TreeNodeDTO();

            childNode.setId( UUID.randomUUID().toString() );
            childNode.setTitle( ( String ) child.get( WF_CONFIG_FIELD_TITLE ) );
            childNode.setIcon( ( String ) child.get( WF_CONFIG_FIELD_ICON ) );
            childNode.setElement( child.get( WF_CONFIG_FIELD_ELEMENT ) );
            childNode.setFolder( false );
            childNode.setLazy( false );

            childElements.add( childNode );
        }

        return childElements;
    }

    /**
     * Gets hpc elements.
     *
     * @param element
     *         the element
     *
     * @return the hpc container with elements
     */
    private TreeNodeDTO getHpcContainerWithElements( JSONObject element ) {
        TreeNodeDTO hpcContainer = new TreeNodeDTO();
        hpcContainer.setId( ( String ) element.get( "id" ) );
        hpcContainer.setTitle( ( String ) element.get( WF_CONFIG_FIELD_TITLE ) );
        hpcContainer.setIcon( ( String ) element.get( WF_CONFIG_FIELD_ICON ) );
        hpcContainer.setChildren( getHpcElements() );
        return hpcContainer;
    }

    /**
     * Gets hpc elements.
     *
     * @return hpc elements
     */
    private List< TreeNodeDTO > getHpcElements() {
        List< TreeNodeDTO > hpcElements = new ArrayList<>();

        final JSONParser parser = new JSONParser();

        try {
            final Object obj = parser.parse( new FileReader( HPC_ELEMENTS_CONFIG_PATH ) );

            final JSONObject jsonObject = ( JSONObject ) obj;
            // loop array
            final JSONArray msg = ( JSONArray ) jsonObject.get( WF_CONFIG_FIELD_ELEMENTS );

            for ( JSONObject object : ( Iterable< JSONObject > ) msg ) {
                TreeNodeDTO childNode = new TreeNodeDTO();

                childNode.setId( UUID.randomUUID().toString() );
                childNode.setTitle( ( String ) object.get( WF_CONFIG_FIELD_TITLE ) );
                childNode.setIcon( ( String ) object.get( WF_CONFIG_FIELD_ICON ) );
                childNode.setElement( object.get( WF_CONFIG_FIELD_ELEMENT ) );
                childNode.setFolder( false );
                childNode.setLazy( false );
                hpcElements.add( childNode );
            }

        } catch ( final Exception e ) {
            log.error( e.getMessage(), e );
        }
        return hpcElements;
    }

    /**
     * Generate system container entity when id and name passed.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param icon
     *         the icon
     *
     * @return the system container entity
     */
    private SystemContainerEntity generateSystemContainerEntity( UUID id, String name, String icon ) {

        SystemContainerEntity entity = new SystemContainerEntity();
        entity.setComposedId( new VersionPrimaryKey( id, SusConstantObject.DEFAULT_VERSION_NO ) );
        entity.setDescription( SYSTEM_CONTAINER );
        entity.setName( name );
        entity.setIcon( icon );
        entity.setCreatedOn( new Date() );
        entity.setModifiedOn( new Date() );

        return entity;

    }

    /**
     * get the container and return the TreeNode Object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the name
     * @param id
     *         the id
     * @param filter
     *         the filter
     *
     * @return the container
     */
    private List< TreeNodeDTO > getContainer( EntityManager entityManager, String userId, UUID id, ObjectTreeViewDTO filter,
            String token ) {
        // filter and sort tree
        if ( filter != null && filter.getSearch() != null && !filter.getSearch().isEmpty() ) {
            List< ContainerEntity > listFromDB = susDAO.getContainersForTreeSearchWithLanguage( entityManager, filter.getSearch(),
                    UUID.fromString( userId ), lifeCycleManager.getOwnerVisibleStatusByPolicyId( ConstantsID.OBJECT_LIFECYCLE_ID ),
                    lifeCycleManager.getAnyVisibleStatusByPolicyId( ConstantsID.OBJECT_LIFECYCLE_ID ),
                    CommonUtils.resolveLanguage( token ) );
            SuSEntity rootEntity = susDAO.getLatestObjectById( entityManager, SystemContainerEntity.class,
                    UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ) );
            TreeNodeDTO parentNode = populateTreeNode( entityManager, userId, rootEntity );
            List< TreeNodeDTO > a = new ArrayList<>();
            for ( SuSEntity susEntity : listFromDB ) {
                a.add( populateTreeNodeWithoutReadPermission( susEntity ) );
            }
            parentNode.setChildren( a );
            return parentNode.getChildren();
        } else {
            return getContainerChildren( entityManager, userId, id, filter, token );
        }
    }

    /**
     * Gets container with children.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param id
     *         the id
     * @param filter
     *         the filter
     *
     * @return the container with children
     */
    private TreeNodeDTO getContainerWithChildren( EntityManager entityManager, String userId, UUID id, ObjectTreeViewDTO filter,
            String token ) {
        SuSEntity suSEntity = susDAO.getLatestObjectByTypeAndId( entityManager, ContainerEntity.class, id );
        TreeNodeDTO treeNode = populateTreeNodeWithoutReadPermission( suSEntity );
        prepareTreeNodeDTOLazily( entityManager, treeNode, userId, suSEntity, filter, token );
        return treeNode;
    }

    /**
     * Translate system entity name.
     *
     * @param entity
     *         the entity
     */
    private void translateSystemEntityName( SuSEntity entity ) {
        entity.setName( MessageBundleFactory.getMessage( SimuspaceFeaturesEnum.getCodeById( entity.getComposedId().getId().toString() ) ) );
    }

    /**
     * get the container and return the TreeNode Object.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the name
     * @param parentId
     *         the parentId
     * @param filter
     *         the filter
     *
     * @return the container
     */
    private List< TreeNodeDTO > getContainerChildren( EntityManager entityManager, String userId, UUID parentId, ObjectTreeViewDTO filter,
            String token ) {
        SuSEntity suSEntity = susDAO.getLatestObjectByTypeAndId( entityManager, ContainerEntity.class, parentId );
        TreeNodeDTO treeNode = populateTreeNodeWithoutReadPermission( suSEntity );
        prepareTreeNodeDTOLazily( entityManager, treeNode, userId, suSEntity, filter, token );
        return treeNode.getChildren();
    }

    /**
     * Prepare filter for max ten number of children.
     *
     * @param parentId
     *         the parent id
     * @param filter
     *         the filter
     *
     * @return the filters DTO
     */
    private FiltersDTO prepareFilterForMaxNumberOfChildren( UUID parentId, ObjectTreeViewDTO filter ) {
        FiltersDTO filtersDTO = new FiltersDTO( ConstantsInteger.INTEGER_VALUE_ONE, ConstantsInteger.INTEGER_VALUE_ZERO,
                MAXIMUM_NUMBER_OF_OBJECTS_THAT_A_CONTAINER_CAN_HAVE_IN_TREE );
        FilterColumn filterColumn = new FilterColumn();
        if ( null != filter && filter.getSort() != null ) {
            if ( filter.getSort().getParam().equalsIgnoreCase( SEARCH_QUERY_UPDATED_ON ) ) {
                filterColumn.setName( COLUMN_MODIFIED_ON_SUSENTITY );
            } else if ( filter.getSort().getParam().equalsIgnoreCase( SEARCH_QUERY_CREATED_ON ) ) {
                filterColumn.setName( COLUMN_CREATED_ON_SUSENTITY );
            } else {
                filterColumn.setName( filter.getSort().getParam() );
            }
            filterColumn.setDir( filter.getSort().getDir() );
        } else {
            if ( parentId.equals( UUID.fromString( SimuspaceFeaturesEnum.ROOT.getId() ) ) ) {
                filterColumn.setName( COLUMN_NAME_SUSENTITY );
            } else {
                filterColumn.setName( COLUMN_CREATED_ON_SUSENTITY );
            }

            filterColumn.setDir( ConstantsString.SORTING_DIRECTION_DESCENDING );
        }
        filtersDTO.setColumns( List.of( filterColumn ) );

        return filtersDTO;
    }

    /**
     * Populate tree node.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param susEntity
     *         the sus entity
     *
     * @return the tree node DTO
     */
    private TreeNodeDTO populateTreeNode( EntityManager entityManager, String userId, SuSEntity susEntity ) {
        TreeNodeDTO treeNode = new TreeNodeDTO();
        treeNode.setId( susEntity.getComposedId().getId().toString() );
        treeNode.setTitle( susEntity.getName() );

        if ( susEntity instanceof ContainerEntity container ) {

            treeNode.setDescription( container.getDescription() );
            treeNode.setIcon( container.getIcon() );
            if ( susEntity instanceof SystemContainerEntity ) {
                if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                    treeNode.setUrl( findProjectURL( treeNode.getId() ) );
                } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) {
                    treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
                } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId() ) ) {
                    treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
                } else {
                    treeNode.setUrl( findURL( treeNode.getId() ) );
                }
            } else if ( permissionManager.isPermitted( entityManager, userId,
                    susEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.READ.getValue() ) ) {
                if ( susEntity instanceof ProjectEntity ) {
                    treeNode.setUrl( findProjectURL( treeNode.getId() ) );
                } else if ( susEntity instanceof WorkflowProjectEntity ) {
                    treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
                } else {
                    treeNode.setUrl( VIEW_PROJECT_URL + susEntity.getComposedId().getId() );
                }
            }
        } else {
            treeNode.setUrl( findURL( treeNode.getId() ) );
        }

        return treeNode;
    }

    /**
     * Populate tree node without read permission tree node dto.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the tree node dto
     */
    private TreeNodeDTO populateTreeNodeWithoutReadPermission( SuSEntity susEntity ) {
        TreeNodeDTO treeNode = new TreeNodeDTO();
        treeNode.setId( susEntity.getComposedId().getId().toString() );
        treeNode.setTitle( susEntity.getName() );
        if ( susEntity instanceof ContainerEntity container ) {
            treeNode.setDescription( container.getDescription() );
            treeNode.setIcon( container.getIcon() );
            if ( susEntity instanceof SystemContainerEntity ) {
                if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                    treeNode.setUrl( findProjectURL( treeNode.getId() ) );
                } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) {
                    treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
                } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.WORKFLOWS_PLOTTING.getId() ) ) {
                    treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
                } else {
                    treeNode.setUrl( findURL( treeNode.getId() ) );
                }
            } else if ( susEntity instanceof ProjectEntity ) {
                treeNode.setUrl( findProjectURL( treeNode.getId() ) );
            } else if ( susEntity instanceof WorkflowProjectEntity ) {
                treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
            } else {
                treeNode.setUrl( VIEW_PROJECT_URL + susEntity.getComposedId().getId() );
            }
        } else {
            treeNode.setUrl( findURL( treeNode.getId() ) );
        }
        return treeNode;
    }

    /**
     * Translate names.
     *
     * @param user
     *         the user
     * @param entity
     *         the entity
     */
    private void translateName( UserDTO user, SuSEntity entity ) {
        if ( PropertiesManager.hasTranslation() && null != user && !user.getId().equals( ConstantsID.SUPER_USER_ID )
                && null != user.getUserDetails() ) {
            String userLang = user.getUserDetails().iterator().next().getLanguage();
            entity.getTranslation().forEach( translation -> {
                if ( userLang.equals( translation.getLanguage() ) && null != translation.getName() && !translation.getName().isEmpty() ) {
                    entity.setName( translation.getName() );
                }
            } );
        }
    }

    /**
     * Show all tree node.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the tree node DTO
     */
    private TreeNodeDTO showAllTreeNode( SuSEntity susEntity ) {
        TreeNodeDTO treeNode = new TreeNodeDTO();
        treeNode.setId( susEntity.getComposedId().getId().toString() );
        treeNode.setTitle( "Show All" );
        if ( susEntity instanceof ProjectEntity ) {
            treeNode.setUrl( findProjectURL( treeNode.getId() ) );
        } else if ( susEntity instanceof SystemContainerEntity ) {
            if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.DATA.getId() ) ) {
                treeNode.setUrl( findProjectURL( treeNode.getId() ) );
            } else if ( susEntity.getComposedId().getId().toString().equals( SimuspaceFeaturesEnum.ALLWORKFLOWS.getId() ) ) {
                treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
            } else {
                treeNode.setUrl( findURL( treeNode.getId() ) );
            }
        } else if ( susEntity instanceof WorkflowProjectEntity ) {
            treeNode.setUrl( VIEW_WORKFLOW_PROJECT_TABLE_URL + susEntity.getComposedId().getId() );
        } else {
            treeNode.setUrl( VIEW_PROJECT_URL + susEntity.getComposedId().getId() );
        }
        treeNode.setIcon( SHOW_ALL_ICON );
        treeNode.setLazy( false );
        return treeNode;
    }

    /**
     * Find container menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param containerEntity
     *         the container entity
     *
     * @return the list
     */
    private List< ContextMenuItem > findContainerMenu( EntityManager entityManager, String userId, ContainerEntity containerEntity ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        UUID objectId = containerEntity.getComposedId().getId();
        for ( String key : Activator.getRouters().keySet() ) {
            for ( RouterConfigItem configItem : Activator.getRouters().get( key ) ) {
                if ( configItem.getClassName() != null && configItem.getClassName().equalsIgnoreCase( SusConstantObject.MENU_DTO )
                        && configItem.getType().equalsIgnoreCase( TREE_CONTEXT ) ) {
                    addProjectContextMenu( entityManager, userId, containerEntity, cml, configItem );
                }
            }
        }
        if ( permissionManager.isPermitted( entityManager, userId,
                containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.MANAGE.getValue() ) ) {
            cml.add( preparePermissionProjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareDeleteObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareAddMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareEditMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareExportDataContext( entityManager, containerEntity ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            cml.add( prepareDeleteObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareAddMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareEditMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareExportDataContext( entityManager, containerEntity ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            cml.add( prepareStatusChangeObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareAddMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareEditMetaDataToObjectContext( containerEntity.getComposedId().getId() ) );
            cml.add( prepareExportDataContext( entityManager, containerEntity ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.READ.getValue() ) ) {
            cml.add( prepareExportDataContext( entityManager, containerEntity ) );
        }
        if ( permissionManager.isPermitted( entityManager, userId,
                containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
            cml.add( prepareCreatStructuerContext( containerEntity.getComposedId().getId() ) );

            if ( isRunnableVariant( entityManager, objectId ) ) {
                cml.add( prepareCB2RunVariantContext( objectId ) );
            }

            if ( containerEntity.getConfig().equals( BMW_DUMMY_PROJECT_JSON ) && !isContainerLabelProject( containerEntity )
                    && ( containerEntity instanceof ProjectEntity ) ) {
                cml.add( prepareDummyRunVariantContext( containerEntity ) );
            }

            if ( containerEntity.getConfig().equals( BMW_QADYNA_PROJECT_JSON ) && !isContainerLabelProject( containerEntity )
                    && ( containerEntity instanceof ProjectEntity ) ) {
                cml.add( prepareQADynaContext( containerEntity ) );
            }
        }
        if ( isRunnableVariant( entityManager, containerEntity.getComposedId().getId() ) ) {
            cml.add( prepareRunVariantContext( containerEntity.getComposedId().getId() ) );
        }
        return cml;
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
        return containerEntity instanceof ProjectEntity projectEntity && projectEntity.getType()
                .equalsIgnoreCase( ProjectType.LABEL.getKey() );
    }

    /**
     * Prepare export data context.
     *
     * @param entityManager
     *         the entity manager
     * @param containerEntity
     *         the container entity
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareExportDataContext( EntityManager entityManager, SuSEntity containerEntity ) {

        List< SuSEntity > parentEntities = susDAO.getParents( entityManager, containerEntity );
        SuSEntity parentEntity = null;
        if ( CollectionUtils.isNotEmpty( parentEntities ) ) {
            parentEntity = parentEntities.get( FIRST_INDEX );
        }
        // sending object id insted of selection id because of context issue : will be
        // changed
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( EXPORT_PROJECT_CONTEXT_URL.replace( CONTAINER_ID_PARAM,
                        ConstantsString.EMPTY_STRING + parentEntity.getComposedId().getId() )
                .replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + containerEntity.getComposedId().getId() ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( EXPORT_TITLE ) );
        containerCMI.setVisibility( CLIENT_VISIBILITY );

        return containerCMI;

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
        containerCMI.setTitle( MessageBundleFactory.getMessage( CREATE_DUMMY_TITLE ) );
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
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100146x4" ) );
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
     * Find workflow project menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectEntity
     *         the project entity
     *
     * @return the list
     */
    private List< ContextMenuItem > findWorkflowProjectMenu( EntityManager entityManager, String userId,
            WorkflowProjectEntity projectEntity ) {
        List< ContextMenuItem > cml = new ArrayList<>();

        for ( String key : Activator.getRouters().keySet() ) {
            for ( RouterConfigItem configItem : Activator.getRouters().get( key ) ) {
                if ( configItem.getClassName() != null && configItem.getClassName()
                        .equalsIgnoreCase( SusConstantObject.WORKFLOW_PROJECT_DTO ) && configItem.getType()
                        .equalsIgnoreCase( TREE_CONTEXT ) ) {
                    addWorkFlowProjectContextMenu( entityManager, userId, projectEntity, cml, configItem );
                }
            }
        }
        if ( permissionManager.isPermitted( entityManager, userId,
                projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.MANAGE.getValue() ) ) {
            cml.add( preparePermissionDataToObjectContext( projectEntity.getComposedId().getId() ) );
            cml.add( prepareDeleteObjectContext( projectEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( projectEntity.getComposedId().getId() ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            cml.add( prepareDeleteObjectContext( projectEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( projectEntity.getComposedId().getId() ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            cml.add( prepareStatusChangeObjectContext( projectEntity.getComposedId().getId() ) );
        }
        if ( permissionManager.isPermitted( entityManager, userId,
                projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
            cml.add( prepareCreateWorkflowContext( projectEntity.getComposedId().getId() ) );
            cml.add( prepareAddMetaDataToObjectContext( projectEntity.getComposedId().getId() ) );
            cml.add( prepareEditMetaDataToObjectContext( projectEntity.getComposedId().getId() ) );
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
        containerCMI.setUrl( OPEN_WF_JOBS_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( "4100074x4" ) );
        return containerCMI;
    }

    /**
     * Adds the work flow project context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param projectEntity
     *         the project entity
     * @param cml
     *         the cml
     * @param configItem
     *         the config item
     */
    private void addWorkFlowProjectContextMenu( EntityManager entityManager, String userId, WorkflowProjectEntity projectEntity,
            List< ContextMenuItem > cml, RouterConfigItem configItem ) {
        switch ( configItem.getTitle() ) {
            case EDIT_TITLE -> {
                if ( permissionManager.isPermitted( entityManager, userId,
                        projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                    cml.add( prepareContextMenuItemForWorkFlowProject( projectEntity, configItem ) );
                }
            }
            case CREATE_WORKFLOW_PROJECT_TITLE, IMPORT_WORKFLOW -> {
                if ( permissionManager.isPermitted( entityManager, userId,
                        projectEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
                    cml.add( prepareContextMenuItemForWorkFlowProject( projectEntity, configItem ) );
                }
            }
            default -> throw new SusException( MessageBundleFactory.getMessage( Messages.INVALID_CONTEXT_MENU_FOR_WFP.getKey() ) );
        }
    }

    /**
     * Prepare context menu item for work flow project.
     *
     * @param projectEntity
     *         the project entity
     * @param configItem
     *         the config item
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareContextMenuItemForWorkFlowProject( WorkflowProjectEntity projectEntity, RouterConfigItem configItem ) {
        ContextMenuItem cm = new ContextMenuItem();
        cm.setUrl( configItem.getUrl().replace( PARAM_PARENT_ID, "" + projectEntity.getComposedId().getId() ) );
        cm.setTitle( MessageBundleFactory.getMessage( configItem.getTitle() ) );
        return cm;
    }

    /**
     * Adds the project context menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param containerEntity
     *         the project entity
     * @param cml
     *         the cml
     * @param configItem
     *         the config item
     */
    private void addProjectContextMenu( EntityManager entityManager, String userId, ContainerEntity containerEntity,
            List< ContextMenuItem > cml, RouterConfigItem configItem ) {
        switch ( configItem.getTitle() ) {
            case CREATE_TITLE -> {
                if ( permissionManager.isPermitted( entityManager, userId,
                        containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.CREATE_NEW_OBJECT.getValue() ) ) {
                    cml.add( prepareContextMenuItemForProject( containerEntity, configItem ) );
                }
            }
            case EDIT_TITLE -> {
                if ( permissionManager.isPermitted( entityManager, userId,
                        containerEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
                    cml.add( prepareContextMenuItemForProject( containerEntity, configItem ) );
                }
            }
            default -> {
                // do nothing
            }
        }
    }

    /**
     * Prepare context menu item for project.
     *
     * @param projectEntity
     *         the project entity
     * @param configItem
     *         the config item
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareContextMenuItemForProject( ContainerEntity projectEntity, RouterConfigItem configItem ) {
        ContextMenuItem cmCreate = new ContextMenuItem();
        cmCreate.setUrl( configItem.getUrl().replace( PARAM_PARENT_ID, "" + projectEntity.getComposedId().getId() ) );
        cmCreate.setTitle( MessageBundleFactory.getMessage( configItem.getTitle() ) );
        return cmCreate;
    }

    /**
     * Find workflow menu.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param workflowEntity
     *         the workflow entity
     *
     * @return the list
     */
    private List< ContextMenuItem > findWorkflowMenu( EntityManager entityManager, String userId, WorkflowEntity workflowEntity ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( permissionManager.isPermitted( entityManager, userId,
                workflowEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.MANAGE.getValue() ) ) {
            cml.add( preparePermissionProjectContext( workflowEntity.getComposedId().getId() ) );
            cml.add( prepareDeleteObjectContext( workflowEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( workflowEntity.getComposedId().getId() ) );
            cml.add( prepareShowWorkflowJobsContext( workflowEntity.getComposedId().getId() ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                workflowEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.DELETE.getValue() ) ) {
            cml.add( prepareDeleteObjectContext( workflowEntity.getComposedId().getId() ) );
            cml.add( prepareStatusChangeObjectContext( workflowEntity.getComposedId().getId() ) );
        } else if ( permissionManager.isPermitted( entityManager, userId,
                workflowEntity.getComposedId().getId() + COLON + PermissionMatrixEnum.WRITE.getValue() ) ) {
            cml.add( prepareStatusChangeObjectContext( workflowEntity.getComposedId().getId() ) );
        }
        return cml;
    }

    /**
     * Find workflow project menu.
     *
     * @param token
     *         the token
     * @param jobEntity
     *         the project entity
     *
     * @return the list
     */
    private List< ContextMenuItem > findJobMenu( String token, JobEntity jobEntity ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        cml.add( preparePermissionDataToObjectContext( jobEntity.getId() ) );
        if ( jobEntity.getJobType() == JobTypeEnums.SCHEME.getKey() ) {
            List< ContextMenuItem > cmlList = new ArrayList<>();
            for ( String key : Activator.getRouters().keySet() ) {
                for ( RouterConfigItem configItem : Activator.getRouters().get( key ) ) {
                    if ( configItem.getClassName() != null && "DesignPlotingConfig".equalsIgnoreCase( configItem.getClassName() ) ) {
                        if ( configItem.getTitle().equals( DOWNLOAD_CSV_FILE ) ) {
                            String url = CSV_FILE_DOWNLOAD_ADDRESS.replace( OBJECT_ID_PARAM,
                                    ConstantsString.EMPTY_STRING + jobEntity.getId() );
                            cmlList.add( new ContextMenuItem( url, configItem.getIcon(),
                                    MessageBundleFactory.getMessage( configItem.getTitle() ) ) );
                        } else if ( configItem.getTitle().equals( ConstantsString.GENERATE_IMAGE_KEY ) ) {
                            cmlList.addAll( getContextGenerateImageMenu( jobEntity.getId().toString() ) );
                        } else {
                            String url = configItem.getUrl().replace( "{id}", jobEntity.getId().toString() )
                                    .replace( "{optionId}", DesignPlotingConfig.getOptionIdByName( configItem.getTitle() ) );
                            cmlList.add( new ContextMenuItem( url, configItem.getIcon(),
                                    MessageBundleFactory.getMessage( configItem.getTitle() ) ) );
                        }
                    }
                }
            }
            cml.addAll( cmlList );
        }
        return cml;
    }

    /**
     * {@inheritDoc}
     *
     * @param jobId
     *         the job id
     *
     * @return the context generate image menu
     */
    private List< ContextMenuItem > getContextGenerateImageMenu( String jobId ) {
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        Set< String > options = DesignPlotingConfig.getGenerateImageOptions();

        for ( String option : options ) {
            contextToReturn.add( prepareGenerateImageContextForOption( jobId, option ) );
        }

        return contextToReturn;
    }

    /**
     * Prepare generate image context for option.
     *
     * @param jobId
     *         the job id
     * @param option
     *         the option
     *
     * @return the generate image context
     */
    private ContextMenuItem prepareGenerateImageContextForOption( String jobId, String option ) {
        ContextMenuItem contextItem = new ContextMenuItem();
        contextItem.setTitle( MessageBundleFactory.getMessage( ConstantsString.GENERATE_IMAGE_KEY ) + ConstantsString.SPACE + option );
        contextItem.setUrl( GENERATE_IMAGE_URL.replace( RouterConfigItem.ID_PATTERN, jobId ).replace( KEY_PATTERN, KEY + option ) );

        return contextItem;
    }

    /**
     * Gets the containing objects context menu.
     *
     * @param susEntity
     *         the project entity
     *
     * @return the contain objects context
     */
    private List< ContextMenuItem > getContainingObjectsCreateContextMenu( SuSEntity susEntity ) {

        List< ContextMenuItem > containingObjectsCMIs = new ArrayList<>();
        SuSObjectModel projectSuSObjectModel = configManager.getObjectTypeByIdAndConfigName( susEntity.getTypeId().toString(),
                susEntity.getConfig() );

        if ( susEntity instanceof ProjectEntity projectEntity && projectEntity.getType() != null && projectEntity.getType()
                .contentEquals( ProjectType.LABEL.getKey() ) ) {
            return containingObjectsCMIs;
        }

        List< String > containingObjectIds = projectSuSObjectModel.getContains();

        if ( CollectionUtil.isNotEmpty( containingObjectIds ) ) {

            for ( String key : Activator.getRouters().keySet() ) {

                for ( RouterConfigItem configItem : Activator.getRouters().get( key ) ) {

                    if ( configItem.getClassName() != null && configItem.getTitle() != null && configItem.getClassName()
                            .equalsIgnoreCase( SusConstantObject.MENU_DTO ) && configItem.getType().equalsIgnoreCase( TREE_CONTEXT ) ) {

                        if ( ( configItem.getTitle().contains( RUN ) || configItem.getTitle().contains( CREATE_COPY_TITLE )
                                || configItem.getTitle().contains( CREATE_DUMMY_TITLE ) ) && !susEntity.getConfig()
                                .equals( BMW_DUMMY_PROJECT_JSON ) ) {
                            continue; // run, create copy and create dummy options only available at bmw variant
                        }

                        ContextMenuItem cm = new ContextMenuItem();
                        cm.setUrl( configItem.getUrl().replace( PARAM_PARENT_ID, "" + susEntity.getComposedId().getId() ) );
                        cm.setTitle( MessageBundleFactory.getMessage( configItem.getTitle() ) );
                        containingObjectsCMIs.add( cm );
                    }
                }
            }
        }

        return containingObjectsCMIs;

    }

    /**
     * Find URL.
     *
     * @param id
     *         the id
     *
     * @return the string
     */
    private String findURL( String id ) {
        for ( String key : Activator.getRouters().keySet() ) {

            for ( RouterConfigItem i : Activator.getRouters().get( key ) ) {
                if ( i.getContainer() != null && i.getContainer().equalsIgnoreCase( id ) && i.getType().equalsIgnoreCase( TABLE ) ) {
                    return i.getUrl();
                }
            }
        }
        return null;
    }

    /**
     * Find project URL.
     *
     * @param id
     *         the id
     *
     * @return the string
     */
    private String findProjectURL( String id ) {
        // 2nd loop
        for ( String key : Activator.getRouters().keySet() ) {
            // 3rd loop
            for ( RouterConfigItem i : Activator.getRouters().get( key ) ) {

                if ( i.getClassName() != null && i.getClassName().equalsIgnoreCase( SusConstantObject.PROJECT_DTO ) && i.getType()
                        .equalsIgnoreCase( TABLE ) ) {
                    return i.getUrl().replace( PROJECT_ID_PARAM, id );
                }
            }
        }
        return null;
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
     * Gets the sus DAO.
     *
     * @return the sus DAO
     */
    public SuSGenericObjectDAO< SuSEntity > getSusDAO() {
        return susDAO;
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
     * Gets the permission manager.
     *
     * @return the permission manager
     */
    public PermissionManager getPermissionManager() {
        return permissionManager;
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
     * Gets the object view manager.
     *
     * @return the view manager
     */
    public ObjectViewManager getObjectViewManager() {
        return objectViewManager;
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
     * Gets the user common manager.
     *
     * @return the user common manager
     */
    public UserCommonManager getUserCommonManager() {
        return userCommonManager;
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
     * Sets the click managers.
     *
     * @param clickManagers
     *         the new click managers
     */
    public void setClickManagers( List< ClickManager > clickManagers ) {
        this.clickManagers = clickManagers;
    }

    /**
     * Sets the bmw DAO.
     *
     * @param bmwDAO
     *         the new bmw DAO
     */
    public void setBmwDAO( BmwDAO bmwDAO ) {
        this.bmwDAO = bmwDAO;
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

    public JobDAO getJobDao() {
        return jobDao;
    }

    public void setJobDao( JobDAO jobDao ) {
        this.jobDao = jobDao;
    }

}
