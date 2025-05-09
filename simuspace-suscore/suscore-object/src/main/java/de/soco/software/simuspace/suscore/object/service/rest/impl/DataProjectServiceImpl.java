package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.SyncFileDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.model.GenericDTO;
import de.soco.software.simuspace.suscore.data.model.ProjectDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.DataProjectManager;
import de.soco.software.simuspace.suscore.object.service.rest.DataProjectService;

public class DataProjectServiceImpl extends SuSBaseService implements DataProjectService {

    private DataProjectManager dataProjectManager;

    /**
     * The Constant PROJECT.
     */
    private static final String PROJECT = "Project";

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProject( String projectJson ) {
        Object project;
        try {
            project = dataProjectManager.createSuSProject( getUserIdFromGeneralHeader(), projectJson );
            if ( project != null ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PROJECT_CREATED_SUCCESSFULLY.getKey() ), project );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_CREATE_PROJECT.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProject( String projectJson ) {
        Object project;
        try {
            project = dataProjectManager.updateProject( getUserIdFromGeneralHeader(), projectJson );
            if ( project != null ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PROJECT_UPDATED_SUCCESSFULLY.getKey() ), project );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_UPDATE_PROJECT.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response createProjectForm( String parentId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.createProjectForm( getTokenFromGeneralHeader(), UUID.fromString( parentId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectCustomAttributeUI( String projectType ) {
        try {
            return ResponseUtils.success( dataProjectManager.getProjectCustomAttributeUI( getUserIdFromGeneralHeader(), projectType ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response editProjectForm( String projectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.FORM_CREATED.getKey(),
                            MessageBundleFactory.getMessage( Messages.EDIT_DATA_PROJECT.getKey() ) ),
                    dataProjectManager.editProjectForm( getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllSubContainersByProjectId( String projectId, String typeId, String objectFilterJson ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.ALL_CHILD_OBJECTS.getKey() ) ),
                    dataProjectManager.getAllChildObjectsByTypeId( getUserIdFromGeneralHeader(), UUID.fromString( projectId ), typeId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsinProject( String projectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getAllItems( getUserIdFromGeneralHeader(), UUID.fromString( projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsByProjectId( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( dataProjectManager.getAllItemsObjects( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllFromItemsByProjectId( String projectId, String objectFilterJson ) {
        try {
            return ResponseUtils.success( dataProjectManager.getAllItemsObjects( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProject( String projectId ) {
        try {
            ProjectDTO project = dataProjectManager.getProject( getUserIdFromGeneralHeader(), UUID.fromString( projectId ) );
            if ( project != null ) {
                return ResponseUtils.success( project );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.UNABLE_TO_GET_PROJECT.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getProjectVersionsContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    ContextUtil.allOrderedContext( dataProjectManager.getDataObjectVersionContext( projectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getSyncContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( dataProjectManager.getSyncContextRouter( projectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTransferContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( dataProjectManager.getTransferContextRouter( filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLocalContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    dataProjectManager.getLocalContextRouter( getUserIdFromGeneralHeader(), projectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerItemContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    dataProjectManager.getObjectItemsContextRouter( getUserIdFromGeneralHeader(), projectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerItemFromContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext(
                    dataProjectManager.getObjectItemsContextRouter( getUserIdFromGeneralHeader(), projectId, filter ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectOptionForm( String parentId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getObjectOptionForm( getUserIdFromGeneralHeader(), UUID.fromString( parentId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listContainerTabsUI( String projectId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.OBJECT_TABS.getKey() ) ),
                    dataProjectManager.getTabsViewContainerUI( getTokenFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getItemsCount( String projectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getItemsCount( getUserIdFromGeneralHeader(), UUID.fromString( projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllFitleredItemsInProject( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success(
                    dataProjectManager.getAllFilteredItems( filter, getUserIdFromGeneralHeader(), UUID.fromString( projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerVersionsUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( dataProjectManager.getContainerVersionsUI( projectId ),
                    dataProjectManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY, getUserIdFromGeneralHeader(),
                                    projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getFilteredProjectVersionsList( String projectId, String projectJson ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectVersions( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                    JsonUtils.jsonToObject( projectJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listContainerUI( String projectId, String typeId ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.ALL_CHILD_OBJECTS.getKey() ) ),
                    dataProjectManager.getListOfObjectsUITableColumns( getUserIdFromGeneralHeader(), projectId, typeId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listProjectItemUI( String projectId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getListOfObjectsUITableColumns( getUserIdFromGeneralHeader(), projectId, GenericDTO.GENERIC_DTO_TYPE,
                            dataProjectManager.getObjectViewManager()
                                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_TO_ITEMS_TABLE_KEY,
                                            getUserIdFromGeneralHeader(), projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listProjectItemFromUI( String projectId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getListOfObjectsUITableColumns( getUserIdFromGeneralHeader(), projectId, GenericDTO.GENERIC_DTO_TYPE,
                            dataProjectManager.getObjectViewManager()
                                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_FROM_ITEMS_TABLE_KEY,
                                            getUserIdFromGeneralHeader(), projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataContext( String projectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.CONTEXT_MENU_FETCHED.getKey() ),
                    ContextUtil.allOrderedContext( dataProjectManager.getDataContextRouter( getUserIdFromGeneralHeader(), projectId, filter,
                            getTokenFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listDataUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( dataProjectManager.listDataUI( getUserIdFromGeneralHeader(), projectId ),
                    dataProjectManager.getListOfObjectView( getUserIdFromGeneralHeader(), projectId,
                            UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ) ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response listData( String projectId, String objectFilterJson ) {
        try {

            return ResponseUtils.success( dataProjectManager.getAllObjects( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                    JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ), getTokenFromGeneralHeader() ) );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectSynchStatus( String projectId ) {
        try {
            // checkin or checkout
            return ResponseUtils.success(
                    dataProjectManager.getObjectSynchStatus( getUserIdFromGeneralHeader(), UUID.fromString( projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectCheckinCheckoutStatus( String projectId, String check ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.setObjectSynchStatus( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(),
                            UUID.fromString( projectId ), check ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectzProjectConfigurations( UUID objectId ) {
        try {
            // checkin or checkout
            return ResponseUtils.success( dataProjectManager.getProjectConfigurationOfObject( getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectViewsByObjectIdAndTypeId( String objectId, String typeId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getListOfObjectView( getUserIdFromGeneralHeader(), objectId, UUID.fromString( typeId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectView( String objectId, String typeId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.saveOrUpdateObjectView( getUserIdFromGeneralHeader(), objectId, UUID.fromString( typeId ), viewJson,
                            false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setObjectViewAsDefault( String projectId, String typeId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.setObjectViewAsDefault( getUserIdFromGeneralHeader(), projectId, UUID.fromString( typeId ),
                            viewId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectView( String projectId, String typeId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectView( String objectId, String typeId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.saveOrUpdateObjectView( getUserIdFromGeneralHeader(), objectId, UUID.fromString( typeId ), viewJson,
                            true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllObjectVersionViewsByObjectId( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.VERSION_TABLE_KEY, getUserIdFromGeneralHeader(), objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveObjectVersionView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setVersionViewAsDefault( String objectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager()
                            .saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                                    ConstantsObjectViewKey.VERSION_TABLE_KEY, objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteObjectVersionView( String objectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateObjectVersionView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( objectId, objectJson, ConstantsObjectViewKey.VERSION_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllProjectSyncViewsByProjectId( String projectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.SYNC_TABLE_KEY, getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveProjectSyncViewByProjectId( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.SYNC_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setProjectSyncViewAsDefaultByProjectId( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager()
                            .saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                                    ConstantsObjectViewKey.SYNC_TABLE_KEY, projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectSyncViewByProjectId( String projectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProjectSyncViewByProjectId( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.SYNC_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllProjectLocalViewsByProjectId( String projectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LOCAL_TABLE_KEY, getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveProjectLocalViewByProjectId( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LOCAL_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setProjectLocalViewAsDefaultByProjectId( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager()
                            .saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                                    ConstantsObjectViewKey.LOCAL_TABLE_KEY, projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteProjectLocalViewByProjectId( String projectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateProjectLocalViewByProjectId( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LOCAL_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerSyncStatusUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( SyncFileDTO.class ), dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.SYNC_TABLE_KEY, getUserIdFromGeneralHeader(), projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerLocalStatusUI( String projectId ) {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( SyncFileDTO.class ), dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LOCAL_TABLE_KEY, getUserIdFromGeneralHeader(), projectId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getLifeCycleStatusById( String lifeCycleId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getLifeCycleStatusById( lifeCycleId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getObjectWithVersionUI( String objectId, int versionId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectWithVersionUI( getUserIdFromGeneralHeader(), objectId, versionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDataView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.saveOrUpdateObjectView( getUserIdFromGeneralHeader(), projectId,
                            UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ), objectJson, false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDataView( String projectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateDataView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.saveOrUpdateObjectView( getUserIdFromGeneralHeader(), projectId,
                            UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ), objectJson, true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDataViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.setObjectViewAsDefault( getUserIdFromGeneralHeader(), projectId,
                            UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ), viewId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDataViews( String projectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getListOfObjectView( getUserIdFromGeneralHeader(), projectId,
                    UUID.fromString( GenericDTO.GENERIC_DTO_TYPE ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsCountWithTypeId( String projectId, String typeId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getAllItemsCountWithTypeId( getUserIdFromGeneralHeader(), UUID.fromString( projectId ),
                            UUID.fromString( typeId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLinkedToObjectsViews( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_TO_ITEMS_TABLE_KEY, getUserIdFromGeneralHeader(), projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllLinkedFromObjectsViews( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.LINKED_FROM_ITEMS_TABLE_KEY, getUserIdFromGeneralHeader(),
                            projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveLinkedToObjectsView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LINKED_TO_ITEMS_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveLinkedFromObjectsView( String projectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LINKED_FROM_ITEMS_TABLE_KEY, false, null ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveLinkedObjectsViewAsDefault( String projectId, String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager()
                            .saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(), "", projectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteLinkedToObjectsView( String projectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteLinkedFromObjectsView( String projectId, String viewId ) {
        try {
            if ( dataProjectManager.deleteObjectView( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateLinkedToObjectsView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LINKED_TO_ITEMS_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateLinkedFromObjectsView( String projectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataProjectManager.getObjectViewManager().saveOrUpdateObjectView(
                            prepareObjectViewDTO( projectId, objectJson, ConstantsObjectViewKey.LINKED_FROM_ITEMS_TABLE_KEY, true, viewId ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getOverviewByProjectId( String projectId, String language ) {
        try {
            return ResponseUtils.success( dataProjectManager.getOverviewByProjectId( getTokenFromGeneralHeader(), projectId, language ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getContainerOrChildsById( String objectId ) {
        try {
            return ResponseUtils.success( dataProjectManager.getContainerOrChildsById( objectId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getPropertyManagerConstant() {
        return ResponseUtils.success( PropertiesManager.getVaultPath() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllItemsinProjectWithFilter( String projectId, String typeId, String objectFilterJson ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getAllItemsWithFilters( getUserIdFromGeneralHeader(), UUID.fromString( projectId ), typeId,
                            JsonUtils.jsonToObject( objectFilterJson, FiltersDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllValuesForProjectTableColumn( String projectId, String typeId, String column ) {
        try {
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.ALL_VALUES_FOR_COLUMN_RETURNED_SUCCESSFULLY.getKey(), column ),
                    dataProjectManager.getAllValuesForProjectTableColumn( projectId, typeId, column, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response changeProjectPermissions( String projectId, String objectFilterJson ) {
        try {
            Map< String, String > map = JsonUtils.convertStringToMap( objectFilterJson );
            return ResponseUtils.success( dataProjectManager.changeObjectPermissions( getUserIdFromGeneralHeader(), projectId, map ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUpdateProjectPermissionUI( String projectId, String option ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.getUpdateObjectPermissionUI( getUserIdFromGeneralHeader(), projectId, option ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response permissionProjectOptionsForm( String objectId ) {
        try {
            return ResponseUtils.success(
                    dataProjectManager.permissionObjectOptionsForm( getUserIdFromGeneralHeader(), objectId, PROJECT ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    public void setDataProjectManager( DataProjectManager dataProjectManager ) {
        this.dataProjectManager = dataProjectManager;
    }

}
