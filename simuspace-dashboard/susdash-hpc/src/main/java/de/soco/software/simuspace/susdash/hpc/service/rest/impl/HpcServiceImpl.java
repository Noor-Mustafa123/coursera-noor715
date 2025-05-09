package de.soco.software.simuspace.susdash.hpc.service.rest.impl;

import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.cxf.phase.PhaseInterceptorChain;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Message;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.TableUI;
import de.soco.software.simuspace.suscore.common.util.BundleUtils;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.InternalExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.data.utility.ContextUtil;
import de.soco.software.simuspace.susdash.core.model.HpcDashBoardDTO;
import de.soco.software.simuspace.susdash.core.model.HpcJobDTO;
import de.soco.software.simuspace.susdash.hpc.manager.HpcManager;
import de.soco.software.simuspace.susdash.hpc.service.rest.HpcService;

/**
 * The type Hpc service.
 */
public class HpcServiceImpl extends SuSBaseService implements HpcService {

    /**
     * The hpc manager.
     */
    private HpcManager hpcManager;

    /**
     * Gets the hpc manager.
     *
     * @return the hpc manager
     */
    public HpcManager getHpcManager() {
        return hpcManager;
    }

    /**
     * Sets the hpc manager.
     *
     * @param hpcManager
     *         the new hpc manager
     */
    public void setHpcManager( HpcManager hpcManager ) {
        this.hpcManager = hpcManager;
    }

    /**
     * ***********************UGE************************
     */

    @Override
    public Response getDataObjectHpcUgeMonitorUI( String objectId ) {
        try {
            return ResponseUtils
                    .success( new TableUI( hpcManager.getListOfHpcUITableColumns( objectId ).getColumns(), hpcManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcUgeMonitorUiView( String objectId ) {
        try {
            return ResponseUtils.success( hpcManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_TABLE_KEY,
                    getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveHpcUgeMonitorUiView( String objectId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_TABLE_KEY, true ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateHpcUgeMonitorUiView( String objectId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_TABLE_KEY, false ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response deleteHpcUgeMonitorUiView( String objectId, String viewId ) {
        try {
            if ( hpcManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getHpcUgeMonitorList( String objectId, String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            final FilteredResponse< HpcJobDTO > filteredList = hpcManager.getHpcList( getUserNameFromGeneralHeader(), filter );

            return ResponseUtils.success( filteredList );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getUgeContext( String objectId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( hpcManager.getUgeJobsContext( objectId, filter, getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getLiveInterval() {
        try {
            return ResponseUtils.success( hpcManager.getLiveInterval() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcJobBreadCrumbById( String objectId, String jobId ) {
        try {
            return ResponseUtils.success( hpcManager.getHpcBreadCrumb( jobId, getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcUgeUI( String objectId, String jobId ) {
        try {
            return ResponseUtils.success( hpcManager.getHpcJobSubtabs( getUserNameFromGeneralHeader(), jobId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /** ************************************************ */

    /**
     * ***********************PROPERTIES************************
     */

    @Override
    public Response getHpcUgePropertiesUI( String objectId, String jobId ) {
        try {
            return ResponseUtils.success(
                    new TableUI( hpcManager.getListOfHpcPropertiesUITableColumns( jobId, getUserNameFromGeneralHeader() ).getColumns(),
                            hpcManager.getObjectViewManager().getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_PROPERTIES_TABLE_KEY,
                                    getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcUgePropertiesUiView( String objectId ) {
        try {
            return ResponseUtils.success( hpcManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_PROPERTIES_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveHpcUgePropertiesUiView( String objectId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_PROPERTIES_TABLE_KEY, true ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateHpcUgePropertiesUiView( String objectId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_PROPERTIES_TABLE_KEY, false ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response deleteHpcUgePropertiesUiView( String objectId, String viewId ) {
        try {
            if ( hpcManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getHpcUgePropertiesList( String objectId, String jobId, String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            return ResponseUtils.success( hpcManager.getHpcPropertiesList( getUserNameFromGeneralHeader(), jobId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getPropertiesContext( String objectId, String filterJson ) {
        try {
            return ResponseUtils.success( null );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    @Override
    public Response getbreadcrumbJobContext( String objectId, String jobId ) {
        try {
            return ResponseUtils.success( null );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    @Override
    public Response getbreadcrumbJobNameContext( String objectId, String jobId, String jobName ) {
        try {
            return ResponseUtils.success( null );
        } catch ( final Exception e ) {
            return handleException( e );
        }

    }

    /** ************************************************ */

    /**
     * ***********************FILES************************
     */

    @Override
    public Response getHpcUgeFilesUI( String objectId, String jobId ) {
        try {
            return ResponseUtils
                    .success( new TableUI( hpcManager.getListOfHpcFilesUITableColumns().getColumns(), hpcManager.getObjectViewManager()
                            .getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_FILE_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcUgeFilesUiView( String objectId ) {
        try {
            return ResponseUtils.success( hpcManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_FILE_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveHpcUgeFilesUiView( String objectId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_FILE_TABLE_KEY, true ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateHpcUgeFilesUiView( String objectId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_FILE_TABLE_KEY, false ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response deleteHpcUgeFilesUiView( String objectId, String viewId ) {
        try {
            if ( hpcManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getHpcUgeFilesList( String objectId, String jobId, String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            return ResponseUtils.success( hpcManager.getHpcFilesByJobId( getUserNameFromGeneralHeader(), jobId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getUgeFilesContext( String objectId, String jobId, String filterJson ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJson, FiltersDTO.class );
            return ResponseUtils.success( ContextUtil.allOrderedContext( hpcManager.getFilesContextRouter( objectId, jobId,
                    getUserNameFromGeneralHeader(), filter, HpcJobDTO.class, getTokenFromGeneralHeader() ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response downloadFile( String objectId, String jobId, String fileName ) {
        try {
            return hpcManager.downloadFile( jobId, fileName, getTokenFromGeneralHeader() );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response tailFile( String objectId, String jobId, String name, String filterJson ) {
        try {
            Map< String, ? > filterMap = new HashMap<>();
            return ResponseUtils.success( hpcManager.tailFile( jobId, name,
                    ( Map< String, ? > ) JsonUtils.jsonToMap( filterJson, filterMap ), getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response killJob( String objectId, String jobId ) {
        try {
            return ResponseUtils.success( hpcManager.killHpcJob( objectId, jobId, getUserNameFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /** ************************************************ */

    /**
     * ***********************MONITOR************************
     */

    @Override
    public Response getJobCurve( String objectId, String jobId ) {
        try {
            return ResponseUtils.success( hpcManager.getJobCurve( getUserNameFromGeneralHeader(), jobId ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    /** ************************************************ */

    /**
     * ***********************PENDING MESSAGES************************
     */

    @Override
    public Response getHpcUgePendingMessagesUI( String objectId, String jobId ) {
        try {
            return ResponseUtils.success(
                    new TableUI( hpcManager.getHpcPendingMessagesUITableColumn( jobId, getUserNameFromGeneralHeader() ).getColumns(),
                            hpcManager.getObjectViewManager().getUserObjectViewsByKey(
                                    ConstantsObjectViewKey.HPC_PENDING_MESSAGES_TABLE_KEY, getUserIdFromGeneralHeader(), null ) ) );
        } catch ( final Exception e ) {
            return handleException( e, getClass() );
        }
    }

    @Override
    public Response getHpcUgePendingMessagesUiView( String objectId ) {
        try {
            return ResponseUtils.success( hpcManager.getObjectViewManager()
                    .getUserObjectViewsByKey( ConstantsObjectViewKey.HPC_PENDING_MESSAGES_TABLE_KEY, getUserIdFromGeneralHeader(), null ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response saveHpcUgePendingMessagesUiView( String objectId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_PENDING_MESSAGES_TABLE_KEY, true ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateHpcUgePendingMessagesUiView( String objectId, String viewId, String viewJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    hpcManager.getObjectViewManager().saveOrUpdateObjectView(
                            hpcManager.prepareObjectView( viewJson, ConstantsObjectViewKey.HPC_PENDING_MESSAGES_TABLE_KEY, false ),
                            getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response deleteHpcUgePendingMessagesUiView( String objectId, String viewId ) {
        try {
            if ( hpcManager.getObjectViewManager().deleteObjectView( UUID.fromString( viewId ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getHpcUgePendingMessagesList( String objectId, String jobId, String filterJsonStr ) {
        try {
            final FiltersDTO filter = JsonUtils.jsonToObject( filterJsonStr, FiltersDTO.class );
            return ResponseUtils.success( hpcManager.getHpcPendingMessagesList( getUserNameFromGeneralHeader(), jobId, filter ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* ******************job monitoring end******************** */
    /* ****************** femzip statistics ******************* */

    @Override
    public Response getHpcFemZipChartBySolverAndType( String objectId, String solver, String type, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcFemZipChartBySovlerAndType( objectId, solver, type, getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* ****************** Hpc Dashboard statistics ******************* */

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcLsDynaCpuBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "lsdyna", "cpu", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcLsDynaVersionBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "lsdyna", "version", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcLsDynaStatusBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "lsdyna", "status", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcAbaqusCpuBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "abaqus", "cpu", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcAbaqusVersionBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "abaqus", "version", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Response getHpcAbaqusStatusBoard( String objectId, String jsonPayload ) {
        try {
            HpcDashBoardDTO hpcDahBoardDTO = JsonUtils.jsonToObject( jsonPayload, HpcDashBoardDTO.class );
            return ResponseUtils.success( Message.SUCCESS,
                    hpcManager.getHpcBoard( objectId, "abaqus", "status", getUserNameFromGeneralHeader(), hpcDahBoardDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /** ************************************************ */

    /**
     * Handle exception response.
     *
     * @param exception
     *         the exception
     * @param clazz
     *         the clazz
     *
     * @return the response
     */
    protected Response handleException( Exception exception, Class clazz ) {

        if ( exception instanceof SusException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, exception, getClass() );
            return ResponseUtils.failure( ( ( SusException ) exception ).getStatusCode(), exception.getMessage() );
        }
        if ( exception instanceof SusDataBaseException ) {
            final String userName = BundleUtils.getUserNameFromMessageBundle( PhaseInterceptorChain.getCurrentMessage() );
            ExceptionLogger.logException( userName, exception, getClass() );
            return ResponseUtils.failure( ( ( SusDataBaseException ) exception ).getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.DB_DATABASE_QUERY_ERROR.getKey() ) );
        } else {
            InternalExceptionLogger.logException( exception, getClass() );
            return ResponseUtils.failure( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                    MessageBundleFactory.getMessage( Messages.WEBSERVICE_INTERNAL_SERVER_ERROR.getKey() ) );

        }
    }

}
