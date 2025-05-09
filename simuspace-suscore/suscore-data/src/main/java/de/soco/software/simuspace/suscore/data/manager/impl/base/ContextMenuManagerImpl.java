package de.soco.software.simuspace.suscore.data.manager.impl.base;

import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.ContextMenuDAO;
import de.soco.software.simuspace.suscore.data.common.dao.ContextMenuRelationDAO;
import de.soco.software.simuspace.suscore.data.entity.ObjectJsonSchemaEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;
import de.soco.software.simuspace.suscore.data.model.RouterConfigItem;

/**
 * An implementation class responsible for managing Context menu functions.
 *
 * @author M.Nasir.Farooq
 * @author Zeeshan jamal
 */
public class ContextMenuManagerImpl implements ContextMenuManager {

    /**
     * context menu DAO reference.
     */
    private ContextMenuDAO contextMenuDAO;

    /**
     * The context menu relation DAO.
     */
    private ContextMenuRelationDAO contextMenuRelationDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant BOTH.
     */
    public static final String BOTH = "both";

    public static final String RUN_CB2_VARIANT_CONTEXT_URL = "view/wizards/cb2/variant/{parentId}";

    public static final String RUN_CB2_VARIANT_TITLE = "4100071x4";

    /**
     * The Constant VERSION_PARAM.
     */
    public static final String VERSION_PARAM = "{versionId}";

    /**
     * The Constant ABORT_SYNCH.
     */
    private static final String ABORT_SYNCH = "Sync Abort";

    /**
     * The Constant KEY.
     */
    private static final String KEY = "?key=";

    /**
     * The Constant TOKEN_PATTERN.
     */
    public static final String KEY_PATTERN = "{?key}";

    /**
     * The Constant DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String DELETE_OBJECT_CONTEXT_URL = "delete/data/object/{objectId}/?mode=single";

    /**
     * The Constant REMOVE_OBJECT_CONTEXT_URL.
     */
    public static final String REMOVE_LINK_OBJECT_CONTEXT_URL = "link/remove/object/{objectId}/selection/{selectionId}";

    /**
     * The Constant BULK_DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_DELETE_OBJECT_CONTEXT_URL = "delete/data/object/{objectId}/?mode=bulk";

    /**
     * The Constant BULK_DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_DELETE_SCHEME_CONTEXT_URL = "delete/config/workflowscheme/{selectionId}/?mode=bulk";

    /**
     * The Constant BULK_DELETE_TRAININFALGO_CONTEXT_URL.
     */
    public static final String BULK_DELETE_TRAININFALGO_CONTEXT_URL = "delete/config/trainingalgo/{selectionId}/?mode=bulk";

    public static final String BULK_DELETE_TRAININFALGO_SINGLE_CONTEXT_URL = "delete/config/trainingalgo/{selectionId}/?mode=single";

    /**
     * The Constant BULK_DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String DELETE_SCHEME_CONTEXT_URL = "delete/config/workflowscheme/{selectionId}/?mode=single";

    /**
     * The Constant BULK_DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_DELETE_CATEGORY_CONTEXT_URL = "delete/config/schemecategory/{selectionId}/?mode=bulk";

    /**
     * The Constant BULK_DELETE_OBJECT_CONTEXT_URL.
     */
    public static final String DELETE_CATEGORY_CONTEXT_URL = "delete/config/schemecategory/{selectionId}/?mode=single";

    /**
     * The Constant LINK_OBJECT_CONTEXT_URL.
     */
    public static final String LINK_OBJECT_CONTEXT_URL = "link/create/{selectionId}/?mode=single";

    /**
     * The Constant MOVE_OBJECT_CONTEXT_URL.
     */
    public static final String MOVE_OBJECT_CONTEXT_URL = "move/create/{selectionId}/?mode=single";

    /**
     * The Constant BULK_LINK_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_LINK_OBJECT_CONTEXT_URL = "link/create/{selectionId}/?mode=bulk";

    /**
     * The Constant BULK_MOVE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_MOVE_OBJECT_CONTEXT_URL = "move/create/{selectionId}/?mode=bulk";

    /**
     * The Constant MOVE_OBJECT_CONTEXT_URL.
     */
    public static final String MOVE_WORKFLOW_PROJECT_CONTEXT_URL = "move/workflow/project/{projectId}/?mode=single";

    /**
     * The Constant BULK_MOVE_OBJECT_CONTEXT_URL.
     */
    public static final String BULK_MOVE_WORKFLOW_PROJECT_CONTEXT_URL = "move/workflow/project/{projectId}/?mode=bulk";

    /**
     * The Constant LINK_TO.
     */
    public static final String LINKS = "4100073x4";

    /**
     * The constant for create container.
     */
    public static final String DELETE_OBJECT_TITLE = "4100007x4";

    /**
     * The Constant REMOVE_LINK_TITLE.
     */
    public static final String REMOVE_LINK_TITLE = "4100010x4";

    /**
     * The Constant BULK_DELETE_OBJECT_TITLE.
     */
    public static final String BULK_DELETE_OBJECT_TITLE = "4100076x4";

    /**
     * The Constant MOVE_TO_TITLE.
     */
    public static final String MOVE_TO_TITLE = "4100016x4";

    /**
     * The Constant ADD_META_DATA_TO_OBJECT_CONTEXT_URL.
     */
    public static final String ADD_META_DATA_TO_OBJECT_CONTEXT_URL = "add/data/object/{objectId}";

    /**
     * The Constant PERMISSION_DATA_TO_OBJECT_CONTEXT_URL.
     */
    public static final String PERMISSION_DATA_TO_OBJECT_CONTEXT_URL = "perm/data/object/{objectId}";

    /**
     * The Constant WORKFLOW_EDIT_CONTEXT_URL.
     */
    public static final String WORKFLOW_EDIT_CONTEXT_URL = "workflow/ui/edit/{objectId}";

    /**
     * The Constant CHANGE_STATUS_OBJECT_CONTEXT_URL.
     */
    public static final String CHANGE_STATUS_OBJECT_CONTEXT_URL = "update/data/object/{objectId}/status";

    /**
     * The Constant GENERATE_IMAGE_URL.
     */
    public static final String GENERATE_IMAGE_URL = "job/{id}/generateimage{?key}";

    /**
     * The Constant OBJECT_ID_PARAM.
     */
    public static final String OBJECT_ID_PARAM = "{objectId}";

    /**
     * The Constant PROJECT_ID_PARAM.
     */
    public static final String PROJECT_ID_PARAM = "{projectId}";

    /**
     * The Constant SELECTION_ID_PARAM.
     */
    public static final String SELECTION_ID_PARAM = "{selectionId}";

    /**
     * The Constant ADD_META_DATA_TITLE.
     */
    public static final String ADD_META_DATA_TITLE = "4100008x4";

    /**
     * The Constant PERMISSIONS_NODE_TITLE.
     */
    public static final String PERMISSIONS_NODE_TITLE = "4100012x4";

    /**
     * The Constant CHANGE_STATUS_TITLE.
     */
    public static final String CHANGE_STATUS_TITLE = "4100005x4";

    /**
     * The Constant UPDATE_OBJECT_CONTEXT_URL.
     */
    public static final String UPDATE_OBJECT_CONTEXT_URL = "data/object/ui/edit/{objectId}";

    /**
     * The Constant UPDATE_PROJECT_CONTEXT_URL.
     */
    public static final String UPDATE_PROJECT_CONTEXT_URL = "data/project/ui/edit/{projectId}";

    /**
     * The Constant EDIT_TITLE.
     */
    public static final String EDIT_TITLE = "4100014x4";

    /**
     * The Constant DIRECT_DOWNLOAD_TITLE.
     */
    public static final String DIRECT_DOWNLOAD_TITLE = "4100030x4";

    /**
     * The Constant CLIENT_VISIBILITY.
     */
    public static final String CLIENT_VISIBILITY = "client";

    /**
     * The Constant CREAT_STRUCTURE_TITLLE.
     */
    public static final String CREAT_STRUCTURE_TITLLE = "4100077x4";

    /**
     * The Constant RUN_WORKFLOW_TITLE.
     */
    public static final String RUN_WORKFLOW_TITLE = "4100045x4";

    public static final String SHOW_WORKFLOW_JOBS_TITLE = "4100074x4";

    /**
     * The Constant RUN_WORKFLOW_CONTEXT_URL.
     */
    public static final String RUN_SCHEME_CONTEXT_URL = "run/scheme/{objectId}";

    /**
     * The Constant RUN_WORKFLOW_TITLE.
     */
    public static final String RUN_SCHEME_TITLE = "4100081x4";

    /**
     * The Constant CONTAINER_ID_PARAM.
     */
    public static final String CONTAINER_ID_PARAM = "{containerId}";

    /**
     * The Constant ID_PARAM.
     */
    private static final String ID_PARAM = "{id}";

    /**
     * The Constant EXPORT_TITLE.
     */
    public static final String EXPORT_TITLE = "4100075x4";

    /**
     * The Constant EXPORT_PROJECT_CONTEXT_URL.
     */
    public static final String EXPORT_PROJECT_CONTEXT_URL = "export/data/{containerId}/{selectionId}";

    /**
     * The Constant CREAT_STRUCTURE_CONTEXT_URL.
     */
    public static final String CREAT_STRUCTURE_CONTEXT_URL = "get/data/project/{projectId}/sync/directory";

    /**
     * The Constant CREAT_DATA_OBJECT_CONTEXT_URL.
     */
    public static final String CREAT_DATA_OBJECT_CONTEXT_URL = "project/create/options/{projectId}";

    /**
     * The Constant RUN_WORKFLOW_CONTEXT_URL.
     */
    public static final String RUN_WORKFLOW_CONTEXT_URL = "run/workflow/{objectId}";

    public static final String OPEN_WF_JOBS_CONTEXT_URL = "view/job/workflow/{objectId}";

    /**
     * The Constant RUN_WORKFLOW_CONTEXT_URL.
     */
    public static final String RUN_VARIANT_CONTEXT_URL = "view/wizards/variant/{parentId}";

    /**
     * The Constant RUN_WORKFLOW_CONTEXT_URL.
     */
    public static final String RUN_VARIANT_TITLE = "4100058x4";

    /**
     * The Constant STOP_JOB_CONTEXT_TITLE.
     */
    private static final String STOP_JOB_CONTEXT_TITLE = "4100041x4";

    /**
     * The Constant PAUSE_JOB_CONTEXT_TITLE.
     */
    private static final String PAUSE_JOB_CONTEXT_TITLE = "4100144x4";

    /**
     * The Constant RESUME_JOB_CONTEXT_TITLE.
     */
    private static final String RESUME_JOB_CONTEXT_TITLE = "4100145x4";

    /**
     * The Constant STOP_JOB_CONTEXT_URL.
     */
    private static final String STOP_JOB_CONTEXT_URL = "job/stop/{id}";

    /**
     * The Constant PAUSE_JOB_CONTEXT_URL.
     */
    private static final String PAUSE_JOB_CONTEXT_URL = "job/pause/{id}";

    /**
     * The Constant RESUME_JOB_CONTEXT_URL.
     */
    private static final String RESUME_JOB_CONTEXT_URL = "job/resume/{id}";

    /**
     * {@inheritDoc}
     */
    @Override
    public FiltersDTO getFilterBySelectionId( EntityManager entityManager, String selectionId ) {

        FiltersDTO filtersDTO = null;

        if ( !StringUtils.isBlank( selectionId )
                && !de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateUUIDString( selectionId ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.PARENT_ID_IS_NOT_VALID.getKey(), selectionId ) );
        }

        ObjectJsonSchemaEntity jsonSchemaEntity = contextMenuDAO.findById( entityManager, UUID.fromString( selectionId ) );
        if ( jsonSchemaEntity != null ) {
            filtersDTO = JsonUtils.jsonToObject( jsonSchemaEntity.getContent(), FiltersDTO.class );
        }

        return filtersDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextMenu( EntityManager entityManager, String pluginName, Class< ? > moduleDTOClazz,
            FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( pluginName );
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.QUERY_PATTERN ) ) {
                    String urlWithParam = routerConfigItem.getUrl()
                            .replace( RouterConfigItem.ID_PATTERN, selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_SINGLE );
                    contextToReturn.add( new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                } else if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && ( routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN ) ) ) {
                    String urlWithParam = routerConfigItem.getUrl().replace( RouterConfigItem.ID_PATTERN,
                            selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                    contextToReturn.add( new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                }
            } );
        } else if ( CollectionUtil.isNotEmpty( selectedIds ) ) {

            UUID selectionId = saveSelectionFilter( entityManager, moduleDTOClazz, filter );
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.QUERY_PATTERN ) ) {
                    String url = routerConfigItem.getUrl().replace( RouterConfigItem.ID_PATTERN, selectionId.toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_BULK );
                    contextToReturn.add( new ContextMenuItem( url, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) + ContextMenuItem.BULK_SUFIX_TO_TITLE ) );
                }
            } );
        }
        return contextToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextGenerateImageMenu( String jobId ) {
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
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getContextMenu( EntityManager entityManager, String pluginName, String id, Class< ? > moduleDTOClazz,
            FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( pluginName );
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.KEY_PATTERN ) ) {
                    String urlWithParam = routerConfigItem.getUrl()
                            .replace( RouterConfigItem.KEY_PATTERN, selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_SINGLE );
                    contextToReturn.add( new ContextMenuItem( urlWithParam.replace( RouterConfigItem.ID_PATTERN, id ),
                            routerConfigItem.getIcon(), MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                } else if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && ( routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN ) ) ) {
                    String urlWithParam = routerConfigItem.getUrl().replace( RouterConfigItem.KEY_PATTERN,
                            selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                    contextToReturn.add( new ContextMenuItem( urlWithParam.replace( RouterConfigItem.ID_PATTERN, id ),
                            routerConfigItem.getIcon(), MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                }
            } );
        } else if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            UUID selectionId = saveSelectionFilter( entityManager, moduleDTOClazz, filter );
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getType().contentEquals( RouterConfigItem.TREE_CONTEXT )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.QUERY_PATTERN ) ) {
                    String url = routerConfigItem.getUrl().replace( RouterConfigItem.KEY_PATTERN, selectionId.toString() )
                            .replace( RouterConfigItem.QUERY_PATTERN, RouterConfigItem.MODE_BULK );
                    contextToReturn.add( new ContextMenuItem( url.replace( RouterConfigItem.ID_PATTERN, id ), routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) + ContextMenuItem.BULK_SUFIX_TO_TITLE ) );
                }
            } );
        }
        return contextToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID saveSelectionFilter( EntityManager entityManager, Class< ? > clazz, FiltersDTO filtersDTO ) {
        ObjectJsonSchemaEntity masterEntity = new ObjectJsonSchemaEntity();
        masterEntity.setId( UUID.randomUUID() );
        masterEntity.setName( clazz.getName() );
        masterEntity.setContent( JsonUtils.toJson( filtersDTO, filtersDTO.getClass() ) );
        masterEntity.setCreatedOn( new Date() );
        Notification notify = validateObjectJsonEntity( masterEntity );
        if ( notify.hasErrors() ) {
            throw new SusException( notify.getErrors().toString() );
        }
        ObjectJsonSchemaEntity jsonSchemaEntity = contextMenuDAO.save( entityManager, masterEntity );
        if ( jsonSchemaEntity != null ) {
            return jsonSchemaEntity.getId();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getSyncContextMenu( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter,
            String objectId, String token, int size ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( puginName );

        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            UUID selectionId = UUID.fromString( selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( routerConfigItem.getUrl() == null ) {
                    return;
                }
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && !MessageBundleFactory.getMessage( routerConfigItem.getTitle() ).equals( ABORT_SYNCH ) ) {
                    String urlWithParam = routerConfigItem.getUrl().replace( PROJECT_ID_PARAM, projectId )
                            .replace( SELECTION_ID_PARAM, selectionId.toString() ).replace( OBJECT_ID_PARAM, objectId )
                            .replace( VERSION_PARAM, String.valueOf( ConstantsInteger.INTEGER_VALUE_ONE ) );
                    ContextMenuItem cml = new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) );
                    cml.setVisibility( routerConfigItem.getVisibility() );
                    contextToReturn.add( cml );
                }
            } );
        }

        // removing download api context for multiple objects
        if ( size > 1 ) {
            Iterator< ContextMenuItem > iterator = contextToReturn.iterator();
            while ( iterator.hasNext() ) {
                ContextMenuItem item = iterator.next();
                if ( item.getTitle().equals( MessageBundleFactory.getMessage( DIRECT_DOWNLOAD_TITLE ) ) ) {
                    iterator.remove();
                }
            }
        }

        return contextToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getRunningJobContext( UUID jobId ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        ContextMenuItem cm = new ContextMenuItem();
        cm.setUrl( STOP_JOB_CONTEXT_URL.replace( ID_PARAM, jobId.toString() ) );
        cm.setTitle( MessageBundleFactory.getMessage( STOP_JOB_CONTEXT_TITLE ) );
        cml.add( cm );
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getSyncContainerContextMenu( EntityManager entityManager, String pluginName, String projectId,
            Class< ? > moduleDTOClazz, FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( pluginName );
        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {

            UUID selectionId = saveSelectionFilter( entityManager, moduleDTOClazz, filter );
            routerConfigItems.stream().forEach( routerConfigItem -> {
                if ( moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() ) && ( MessageBundleFactory
                        .getMessage( routerConfigItem.getTitle() ).equalsIgnoreCase( ConstantsString.UPLOAD )
                        || MessageBundleFactory.getMessage( routerConfigItem.getTitle() ).equalsIgnoreCase( ConstantsString.DOWNLOAD ) ) ) {

                    String urlWithParam = routerConfigItem.getUrl().replace( PROJECT_ID_PARAM, projectId ).replace( SELECTION_ID_PARAM,
                            selectionId.toString() );
                    contextToReturn.add( new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                }
            } );

        }

        return contextToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataContextMenu( EntityManager entityManager, String pluginName, String projectId,
            Class< ? > moduleDTOClazz, FiltersDTO filter, boolean isContainer, boolean isObject, UUID containerId, String selectionId ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            addDataObjectContextMenuItems( entityManager, cml, UUID.fromString( projectId ), objectId, isContainer, isObject, containerId,
                    selectionId );
        }
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataRemoveLinkContextMenu( String puginName, String objectId, String selectionId ) {
        List< ContextMenuItem > cml = new ArrayList<>();
        cml.add( prepareRemoveLinkObjectContext( objectId, selectionId ) );
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getWorkflowContextMenu( FiltersDTO filter, boolean isWorkFlow ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareDeleteObjectContext( objectId ) );
            cml.add( preparePermissionDataToObjectContext( objectId ) );
            cml.add( prepareStatusChangeObjectContext( objectId ) );
            cml.add( prepareAddMetaDataToObjectContext( objectId ) );
            if ( isWorkFlow ) {
                cml.add( prepareRunWorkflowContext( objectId ) );
                cml.add( prepareRunSchemeContext( objectId ) );
            }
        }
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getWorkflowContextMenuBulk( FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareBulkDeleteObjectContext( objectId ) );
        }
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getSchemeContextMenu( UUID selectionId, boolean isBulk ) {
        List< ContextMenuItem > contextItems = new ArrayList<>();
        if ( !isBulk ) {
            contextItems.add(
                    new ContextMenuItem( "edit/config/workflowscheme/" + selectionId, "",
                            MessageBundleFactory.getMessage( "4100014x4" ) ) );
            ContextMenuItem deleteCMI = new ContextMenuItem();
            deleteCMI.setUrl( DELETE_SCHEME_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            deleteCMI.setTitle( MessageBundleFactory.getMessage( DELETE_OBJECT_TITLE ) );
            contextItems.add( deleteCMI );
        } else {
            ContextMenuItem bulkDeleteCMI = new ContextMenuItem();
            bulkDeleteCMI
                    .setUrl( BULK_DELETE_SCHEME_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            bulkDeleteCMI.setTitle( MessageBundleFactory.getMessage( BULK_DELETE_OBJECT_TITLE ) );
            contextItems.add( bulkDeleteCMI );
        }
        return contextItems;
    }

    /**
     * Gets the training algo context menu.
     *
     * @param selectionId
     *         the selection id
     * @param isBulk
     *         the is bulk
     *
     * @return the training algo context menu
     */
    @Override
    public List< ContextMenuItem > getTrainingAlgoContextMenu( UUID selectionId, boolean isBulk ) {
        List< ContextMenuItem > contextItems = new ArrayList<>();
        if ( !isBulk ) {
            contextItems.add(
                    new ContextMenuItem( "edit/config/trainingalgo/" + selectionId, "", MessageBundleFactory.getMessage( "4100014x4" ) ) );
            ContextMenuItem deleteCMI = new ContextMenuItem();
            deleteCMI.setUrl(
                    BULK_DELETE_TRAININFALGO_SINGLE_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            deleteCMI.setTitle( MessageBundleFactory.getMessage( DELETE_OBJECT_TITLE ) );
            contextItems.add( deleteCMI );
        } else {
            ContextMenuItem bulkDeleteCMI = new ContextMenuItem();
            bulkDeleteCMI.setUrl(
                    BULK_DELETE_TRAININFALGO_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            bulkDeleteCMI.setTitle( MessageBundleFactory.getMessage( BULK_DELETE_OBJECT_TITLE ) );
            contextItems.add( bulkDeleteCMI );
        }
        return contextItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobPauseOption( List< ContextMenuItem > contextMenuItem, UUID jobId ) {
        ContextMenuItem cm = new ContextMenuItem();
        cm.setUrl( PAUSE_JOB_CONTEXT_URL.replace( ID_PARAM, jobId.toString() ) );
        cm.setTitle( MessageBundleFactory.getMessage( PAUSE_JOB_CONTEXT_TITLE ) );
        contextMenuItem.add( cm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobResumeOption( List< ContextMenuItem > contextMenuItem, UUID jobId ) {
        ContextMenuItem cm = new ContextMenuItem();
        cm.setUrl( RESUME_JOB_CONTEXT_URL.replace( ID_PARAM, jobId.toString() ) );
        cm.setTitle( MessageBundleFactory.getMessage( RESUME_JOB_CONTEXT_TITLE ) );
        contextMenuItem.add( cm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getSchemeCategoryContextMenu( UUID selectionId, boolean isBulk ) {
        List< ContextMenuItem > contextItems = new ArrayList<>();
        if ( !isBulk ) {
            contextItems.add( new ContextMenuItem( "update/config/schemecategory/" + selectionId, "",
                    MessageBundleFactory.getMessage( "4100014x4" ) ) );
            ContextMenuItem deleteCMI = new ContextMenuItem();
            deleteCMI.setUrl( DELETE_CATEGORY_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            deleteCMI.setTitle( MessageBundleFactory.getMessage( DELETE_OBJECT_TITLE ) );
            contextItems.add( deleteCMI );
        } else {
            ContextMenuItem bulkDeleteCMI = new ContextMenuItem();
            bulkDeleteCMI
                    .setUrl( BULK_DELETE_CATEGORY_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
            bulkDeleteCMI.setTitle( MessageBundleFactory.getMessage( BULK_DELETE_OBJECT_TITLE ) );
            contextItems.add( bulkDeleteCMI );
        }
        return contextItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getJobsDataCreatedContextMenu( UUID selectionId, boolean isBulk ) {
        List< ContextMenuItem > contextItems = new ArrayList<>();
        if ( !isBulk ) {
            contextItems.add( prepareUpdateObjectContext( selectionId, false ) );
            contextItems.add( prepareDeleteObjectContext( selectionId ) );
        } else {
            contextItems.add( prepareBulkDeleteObjectContext( selectionId ) );
        }
        return contextItems;
    }

    /**
     * Adds the data object context menu items.
     *
     * @param cml
     *         the cml
     * @param parentId
     *         the parent id
     * @param objectId
     *         the object id
     * @param isContainer
     *         the is container
     * @param isObject
     *         the is object
     * @param containerId
     *         the container id
     * @param selectionId
     *         the selection id
     */
    private void addDataObjectContextMenuItems( EntityManager entityManager, List< ContextMenuItem > cml, UUID parentId, UUID objectId,
            boolean isContainer, boolean isObject, UUID containerId, String selectionId ) {
        cml.add( prepareDeleteObjectContext( objectId ) );
        cml.add( prepareAddMetaDataToObjectContext( objectId ) );
        cml.add( preparePermissionDataToObjectContext( objectId ) );
        cml.add( prepareStatusChangeObjectContext( objectId ) );
        cml.add( prepareUpdateObjectContext( objectId, isContainer ) );
        cml.add( prepareExportObjectContext( objectId, containerId ) );
        if ( isContainer ) {
            cml.add( prepareCreatStructuerContext( objectId ) );
        }
        if ( !isObject ) {
            cml.add( null == contextMenuRelationDAO.getRelationLinkByParentIdAndChildId( entityManager, parentId, objectId )
                    ? prepareLinkToObjectContext( objectId )
                    : prepareRemoveLinkObjectContext( parentId.toString(), selectionId ) );
        }
        cml.add( prepareMoveToObjectContext( objectId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataContextMenuBulk( String puginName, String projectId, Class< ? > moduleDTOClazz, FiltersDTO filter,
            boolean isObject ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareBulkDeleteObjectContext( objectId ) );
            cml.add( prepareBulkMoveToObjectContext( objectId ) );
            if ( !isObject ) {
                cml.add( prepareBulkLinkToObjectContext( objectId ) );
            }
        }
        return cml;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getDataContextMenuForReport( String puginName, String projectId, Class< ? > moduleDTOClazz,
            FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareBulkDeleteObjectContext( objectId ) );
            cml.add( prepareBulkMoveToObjectContext( objectId ) );
        }
        return cml;
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
        // sending object id insted of selection id because of context issue : will be
        // changed
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( EXPORT_PROJECT_CONTEXT_URL.replace( CONTAINER_ID_PARAM, ConstantsString.EMPTY_STRING + containerId )
                .replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( EXPORT_TITLE ) );
        containerCMI.setVisibility( CLIENT_VISIBILITY );
        return containerCMI;
    }

    /**
     * Prepare add meta data to object context.
     *
     * @param objectId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareRunWorkflowContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( RUN_WORKFLOW_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( RUN_WORKFLOW_TITLE ) );
        containerCMI.setVisibility( BOTH );
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
        containerCMI.setUrl( RUN_SCHEME_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( RUN_SCHEME_TITLE ) );
        containerCMI.setVisibility( BOTH );
        return containerCMI;
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
        containerCMI.setTitle( MessageBundleFactory.getMessage( CHANGE_STATUS_TITLE ) );
        return containerCMI;
    }

    /**
     * Preparation of deleting object from context menu.
     *
     * @param objectId
     *         UUID
     *
     * @return ContextMenutItem
     */
    private ContextMenuItem prepareDeleteObjectContext( UUID objectId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( DELETE_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( DELETE_OBJECT_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare remove link object context.
     *
     * @param objectId
     *         the object id
     * @param selectionId
     *         the selection id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareRemoveLinkObjectContext( String objectId, String selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI
                .setUrl( REMOVE_LINK_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, objectId ).replace( SELECTION_ID_PARAM, selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( REMOVE_LINK_TITLE ) );
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
        containerCMI.setUrl( BULK_DELETE_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( BULK_DELETE_OBJECT_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare link to object context.
     *
     * @param selectionId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareLinkToObjectContext( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( LINK_OBJECT_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( LINKS ) );
        return containerCMI;
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
        containerCMI.setUrl( MOVE_OBJECT_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( MOVE_TO_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare move to object context.
     *
     * @param selectionId
     *         the object id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareBulkMoveToObjectContext( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( BULK_MOVE_OBJECT_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( MOVE_TO_TITLE ) );
        return containerCMI;
    }

    /**
     * Prepare buik link to object context.
     *
     * @param selectionId
     *         the selection id
     *
     * @return the context menu item
     */
    private ContextMenuItem prepareBulkLinkToObjectContext( UUID selectionId ) {
        ContextMenuItem containerCMI = new ContextMenuItem();
        containerCMI.setUrl( BULK_LINK_OBJECT_CONTEXT_URL.replace( SELECTION_ID_PARAM, ConstantsString.EMPTY_STRING + selectionId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( LINKS ) );
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
        containerCMI.setUrl( isContainer ? UPDATE_PROJECT_CONTEXT_URL.replace( PROJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId )
                : UPDATE_OBJECT_CONTEXT_URL.replace( OBJECT_ID_PARAM, ConstantsString.EMPTY_STRING + objectId ) );
        containerCMI.setTitle( MessageBundleFactory.getMessage( EDIT_TITLE ) );
        return containerCMI;
    }

    /**
     * Validate object json entity.
     *
     * @param masterEntity
     *         the master entity
     *
     * @return the notification object by filling the error messages (if any) after validating the OjbectJsonSchemaEntity
     */
    private Notification validateObjectJsonEntity( ObjectJsonSchemaEntity masterEntity ) {
        Notification notify = new Notification();
        if ( masterEntity != null ) {
            if ( isBlank( masterEntity.getName() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_NAME_CANNOT_EMPTY.getKey() ) ) );
            }
            if ( isBlank( masterEntity.getContent() ) ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_CONTENT_CANNOT_EMPTY.getKey() ) ) );
            }
            if ( masterEntity.getCreatedOn() == null ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.OBJECT_TYPE_CREATED_ON_CANNOT_EMPTY.getKey() ) ) );
            }
        }
        return notify;
    }

    /**
     * sets contextMenuDAO.
     *
     * @param contextMenuDAO
     *         the new context menu DAO
     */
    public void setContextMenuDAO( ContextMenuDAO contextMenuDAO ) {
        this.contextMenuDAO = contextMenuDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< ContextMenuItem > getWorkflowContextMenu( String puginName, String projectId, Class< ? > moduleDTOClazz,
            FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > cml = new ArrayList<>();
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            UUID objectId = UUID.fromString( ( String ) selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
            cml.add( prepareDeleteObjectContext( objectId ) );
        }
        return cml;
    }

    /**
     * Gets the parser table context.
     *
     * @param parserId
     *         the parser id
     * @param filter
     *         the filter
     *
     * @return the parser table context
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager#
     * getParserTableContext(java.lang.String,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO)
     */
    @Override
    public List< ContextMenuItem > getParserTableContext( String parserId, FiltersDTO filter ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( "plugin_workflow" );
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            for ( RouterConfigItem routerItem : routerConfigItems ) {
                if ( routerItem.getClassName() != null && "ParserVariableDTO".equalsIgnoreCase( routerItem.getClassName() ) ) {
                    String urlWithParam = routerItem.getUrl().replace( "{parserId}", parserId ).replace( SELECTION_ID_PARAM,
                            selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                    contextToReturn
                            .add( new ContextMenuItem( urlWithParam, null, MessageBundleFactory.getMessage( routerItem.getTitle() ) ) );
                }
            }
        }
        return contextToReturn;
    }

    /**
     * Gets the context menu relation DAO.
     *
     * @return the context menu relation DAO
     */
    public ContextMenuRelationDAO getContextMenuRelationDAO() {
        return contextMenuRelationDAO;
    }

    /**
     * Sets the context menu relation DAO.
     *
     * @param contextMenuRelationDAO
     *         the new context menu relation DAO
     */
    public void setContextMenuRelationDAO( ContextMenuRelationDAO contextMenuRelationDAO ) {
        this.contextMenuRelationDAO = contextMenuRelationDAO;
    }

    /**
     * Sets the context visibility to client only.
     *
     * @param context
     *         the context
     *
     * @return the list
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager#
     * setContextVisibilityToClientOnly(java.util.List)
     */
    @Override
    public List< ContextMenuItem > setContextVisibilityToClientOnly( List< ContextMenuItem > context ) {
        for ( ContextMenuItem menuItem : context ) {
            menuItem.setVisibility( CLIENT_VISIBILITY );
        }
        return context;
    }

    /**
     * Gets the context job ploting and job log download.
     *
     * @param puginName
     *         the pugin name
     * @param moduleDTOClazz
     *         the module DTO clazz
     * @param filter
     *         the filter
     * @param token
     *         the token
     * @param jobTypeSchemeOrWf
     *         the job type scheme or wf
     *
     * @return the context job ploting and job log download
     */
    /*
     * (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.manager.base.ContextMenuManager#
     * getContextJobPlotingAndJobLogDownload(java.lang.String, java.lang.Class,
     * de.soco.software.simuspace.suscore.common.base.FiltersDTO, java.lang.String,
     * boolean)
     */
    @Override
    public List< ContextMenuItem > getContextJobPlotingAndJobLogDownload( String puginName, Class< ? > moduleDTOClazz, FiltersDTO filter,
            String token, boolean jobTypeSchemeOrWf ) {
        List< Object > selectedIds = filter.getItems();
        List< ContextMenuItem > contextToReturn = new ArrayList<>();
        List< RouterConfigItem > routerConfigItems = Activator.getRoutersBypluginName( puginName );
        if ( CollectionUtil.isNotEmpty( selectedIds ) && selectedIds.size() == ConstantsInteger.INTEGER_VALUE_ONE ) {
            routerConfigItems.stream().forEach( routerConfigItem -> {

                if ( jobTypeSchemeOrWf && moduleDTOClazz.getSimpleName().equalsIgnoreCase( routerConfigItem.getClassName() )
                        && routerConfigItem.getUrl().contains( RouterConfigItem.ID_PATTERN )
                        && !routerConfigItem.getTitle().equals( ConstantsString.GENERATE_IMAGE_KEY ) ) {
                    String urlWithParam = routerConfigItem.getUrl()
                            .replace( RouterConfigItem.ID_PATTERN, selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() )
                            .replace( "{optionId}", DesignPlotingConfig.getOptionIdByName( routerConfigItem.getTitle() ) );
                    contextToReturn.add( new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                }
                if ( "4100048x4".equalsIgnoreCase( routerConfigItem.getTitle() ) ) {
                    String urlWithParam = routerConfigItem.getUrl()
                            .replace( RouterConfigItem.ID_PATTERN, selectedIds.get( ConstantsInteger.INTEGER_VALUE_ZERO ).toString() );
                    contextToReturn.add( new ContextMenuItem( urlWithParam, routerConfigItem.getIcon(),
                            MessageBundleFactory.getMessage( routerConfigItem.getTitle() ) ) );
                }

            } );
        }
        return contextToReturn;

    }

    /**
     * Sets the entity manager factory.
     *
     * @param entityManagerFactory
     *         the new entity manager factory
     */
    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
