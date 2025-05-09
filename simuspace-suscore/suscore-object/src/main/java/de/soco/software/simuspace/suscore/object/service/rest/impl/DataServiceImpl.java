package de.soco.software.simuspace.suscore.object.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.model.CurveUnitDTO;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.TransferObject;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.model.WorkflowProjectDTO;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.suscore.object.manager.DataManager;
import de.soco.software.simuspace.suscore.object.model.DeletedObjectDTO;
import de.soco.software.simuspace.suscore.object.service.rest.DataService;

/**
 * Implementation Class for Interface responsible for all the operation related to sus Data object.
 */
@Log4j2
public class DataServiceImpl extends SuSBaseService implements DataService {

    /**
     * reference for DataManger.
     */
    private DataManager dataManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateWorkflowProject( String workflowProjectJson ) {
        WorkflowProjectDTO project;
        try {
            project = dataManager.updateWorkFlowProject( getTokenFromGeneralHeader(), workflowProjectJson );
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
    public Response getDeletedObjectsTableUI() {
        try {
            return ResponseUtils.success( new TableUI( GUIUtils.listColumns( DeletedObjectDTO.class, VersionDTO.class ),
                    dataManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.DELETED_OBJECTS_TABLE_KEY,
                            getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDeletedObjectList( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( dataManager.getDeletedObjectList( filter, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getAllDeletedObjectViews() {
        try {
            return ResponseUtils.success( dataManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.DELETED_OBJECTS_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveDeletedObjectView( String viewJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( viewJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.DELETED_OBJECTS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response setDeletedObjectViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.DELETED_OBJECTS_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteDeletedObjectView( String viewId ) {
        try {
            if ( dataManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response updateDeletedObjectView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.DELETED_OBJECTS_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getDeletedObjectsContext( String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( dataManager.getContextRouter( filter, DeletedObjectDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response restoreObjectBySelection( String id, String mode ) {
        try {
            dataManager.restoreObjectBySelection( getUserIdFromGeneralHeader(), id, mode );
            return ResponseUtils.success(
                    MessageBundleFactory.getMessage( Messages.SELECTED_ITEMS_STARTED_RESTORE_PROCESS_SUCCESSFULLY.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getItemsFromSelectionId( String selectionId ) {
        try {
            return ResponseUtils.success( dataManager.getItemsFromSelectionId( UUID.fromString( selectionId ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateFileToAnObject( String jobId, String objectId, String json ) {
        try {
            Map< String, DocumentDTO > map = new HashMap<>();
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.FILE_UPDATED_SUCCESSFULLY.getKey() ),
                    dataManager.addFileToAnObject( jobId, objectId, ( Map< String, DocumentDTO > ) JsonUtils.jsonToMap( json, map ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response stopJobs( String jobId, String mode ) {
        try {
            dataManager.stopJobExecution( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), jobId, mode );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.JOB_STOPPED.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response pauseJob( String jobId ) {
        try {
            dataManager.pauseJobExecution( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), jobId );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.JOB_STOPPED.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response resumeJob( String jobId ) {
        try {
            dataManager.resumeJobExecution( getUserIdFromGeneralHeader(), getUserNameFromGeneralHeader(), jobId );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.JOB_STOPPED.getKey() ), true );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /**
     * Sets the data manager.
     *
     * @param dataManager
     *         the new data manager
     */
    public void setDataManager( DataManager dataManager ) {
        this.dataManager = dataManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserReadPermission( String containerId ) {
        return ResponseUtils.success( dataManager.checkUserReadPermission( getUserIdFromGeneralHeader(), containerId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getUserDeletePermission( String containerId ) {
        return ResponseUtils.success( dataManager.checkUserDeletePermission( getUserIdFromGeneralHeader(), containerId ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response transferObject( String transferObjectStr ) {
        try {
            TransferObject transferObject = JsonUtils.jsonToObject( transferObjectStr, TransferObject.class );
            return ResponseUtils.success( dataManager.transferObject( getUserIdFromGeneralHeader(), transferObject ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response exportObjectUI() {
        try {
            return ResponseUtils.success( dataManager.exportObjectUI() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }




    /*
     * **************************************** Linked items views ****************************************.
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCurveListBySelection( String selectionId, String objectJson ) {
        try {
            return ResponseUtils.success(
                    dataManager.getDataObjectCurveBySelectionIds( selectionId, JsonUtils.jsonToObject( objectJson, CurveUnitDTO.class ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCurveXUnitsBySelection( String selectionId ) {
        try {
            return ResponseUtils.success( dataManager.getCurveXUnitsBySelection( selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getCurveYUnitsBySelection( String selectionId ) {
        try {
            return ResponseUtils.success( dataManager.getCurveYUnitsBySelection( selectionId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getTransferViews() {
        try {
            return ResponseUtils.success( dataManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.TRANSFER_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response saveTransferView( String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            if ( !objectViewDTO.isDefaultView() ) {
                objectViewDTO.setId( null );
            }
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.TRANSFER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response updateTransferView( String viewId, String objectJson ) {
        try {
            ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectJson, ObjectViewDTO.class );
            objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.TRANSFER_TABLE_KEY );
            objectViewDTO.setObjectViewType( ConstantsObjectViewType.TABLE_VIEW_TYPE );
            objectViewDTO.setObjectViewJson( JsonUtils.objectToJson( objectViewDTO.getSettings() ) );
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveOrUpdateObjectView( objectViewDTO, getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response deleteTransferView( String viewId ) {
        try {
            if ( dataManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
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
    public Response setTransferViewAsDefault( String viewId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_SET_AS_DEFAULT_SUCCESSFULLY.getKey() ),
                    dataManager.getObjectViewManager().saveDefaultObjectView( UUID.fromString( viewId ), getUserIdFromGeneralHeader(),
                            ConstantsObjectViewKey.TRANSFER_TABLE_KEY, null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the HCP list by user.
     *
     * @param userUID
     *         the user UID
     * @param solver
     *         the solver
     *
     * @return the HCP list by user
     */
    @Override
    public Response getHCPListByUser( String userUID, String solver ) {
        try {
            return ResponseUtils.success( dataManager.getHPCListByEachUser( getUserNameFromGeneralHeader(), solver ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response downloadDocument( UUID objectId, int version ) {
        try {
            return dataManager.downloadDocument( objectId, version, getTokenFromGeneralHeader() );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}