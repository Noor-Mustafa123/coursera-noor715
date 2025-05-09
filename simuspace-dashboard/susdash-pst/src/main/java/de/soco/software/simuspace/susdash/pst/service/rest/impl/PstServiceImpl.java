package de.soco.software.simuspace.susdash.pst.service.rest.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.message.Message;
import org.json.simple.JSONObject;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.DateUtils;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.susdash.pst.constants.PstConstants;
import de.soco.software.simuspace.susdash.pst.manager.PstManager;
import de.soco.software.simuspace.susdash.pst.model.CoupleDTO;
import de.soco.software.simuspace.susdash.pst.model.PreReqDTO;
import de.soco.software.simuspace.susdash.pst.model.PstDTO;
import de.soco.software.simuspace.susdash.pst.service.rest.PstService;

/**
 * The type Pst service.
 */
public class PstServiceImpl extends SuSBaseService implements PstService {

    /**
     * The Pst manager.
     */
    private PstManager pstManager;

    /**
     * Gets pst test chart.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst test chart
     */
    @Override
    public Response getPstTestChart( String objectId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_TEST_DATA_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getPstTestJson( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets pst test chart.
     *
     * @param objectId
     *         the object id
     * @param archive
     *         the archive
     *
     * @return the pst test chart
     */
    @Override
    public Response getPstTestArchive( String objectId, String archive ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_TEST_ARCHIVE_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getPstTestArchive( objectId, getTokenFromGeneralHeader(), archive ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst bench chart
     */
    @Override
    public Response getPstBenchChart( String objectId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_BENCHES_DATA_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getPstBenchData( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     * @param archive
     *         the archive
     *
     * @return the pst bench chart
     */
    @Override
    public Response getPstBenchArchive( String objectId, String archive ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_BENCHES_ARCHIVE_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getPstBenchArchive( objectId, getTokenFromGeneralHeader(), archive ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Couple test schedule response.
     *
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response coupleTestSchedule( String objectId, String payload ) {
        try {
            CoupleDTO coupleDTO = JsonUtils.jsonToObject( payload, CoupleDTO.class );
            Boolean success = pstManager.coupleTestSchedule( objectId, getTokenFromGeneralHeader(), coupleDTO );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.TEST_SCHEDULE_COUPLED_SUCCESSFULLY.getKey(),
                        coupleDTO.getRowId1(), coupleDTO.getRowId2() ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_COUPLED_TEST_SCHEDULE.getKey(),
                        coupleDTO.getRowId1(), coupleDTO.getRowId2() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Move test schedule response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the move settings
     *
     * @return the response
     */
    @Override
    public Response editTestSchedule( String objectId, String pstJson ) {
        try {
            PstDTO pstDTO = JsonUtils.jsonToObject( pstJson, PstDTO.class );
            Boolean success = pstManager.editTestSchedule( objectId, getTokenFromGeneralHeader(), pstDTO );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success(
                        MessageBundleFactory.getMessage( Messages.TEST_SCHEDULE_UPDATED_SUCCESSFULLY.getKey(), pstDTO.getPstOrt() ) );
            } else {
                return ResponseUtils
                        .failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_UPDATE_TEST_SCHEDULE.getKey(), pstDTO.getPstOrt() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Edit test schedule ui response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the pst json
     *
     * @return the response
     */
    @Override
    public Response editTestScheduleUI( String objectId, String pstJson ) {
        try {
            PstDTO pstDTO = JsonUtils.jsonToObject( pstJson, PstDTO.class );
            return ResponseUtils.success( pstManager.editTestScheduleUI( objectId, getTokenFromGeneralHeader(), pstDTO ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Add test schedule response.
     *
     * @param objectId
     *         the object id
     * @param pstJson
     *         the pst json
     *
     * @return the response
     */
    @Override
    public Response addTestSchedule( String objectId, String pstJson ) {
        try {
            PstDTO pstDTO = JsonUtils.jsonToObject( pstJson, PstDTO.class );
            Boolean success = pstManager.addTestSchedule( objectId, getTokenFromGeneralHeader(), pstDTO );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success(
                        MessageBundleFactory.getMessage( Messages.TEST_SCHEDULE_ADDED_SUCCESSFULLY.getKey(), pstDTO.getPstOrt() ) );
            } else {
                return ResponseUtils
                        .failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_ADD_TEST_SCHEDULE.getKey(), pstDTO.getPstOrt() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Add test schedule ui response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @Override
    public Response addTestScheduleUI( String objectId ) {
        try {
            return ResponseUtils.success( pstManager.addTestScheduleUI( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update pre req response.
     *
     * @param objectId
     *         the object id
     * @param moveSettings
     *         the move settings
     *
     * @return the response
     */
    @Override
    public Response updatePreReq( String objectId, String moveSettings ) {
        try {
            PreReqDTO preReqDTO = JsonUtils.jsonToObject( moveSettings, PreReqDTO.class );
            Boolean success = pstManager.updatePreReq( objectId, getTokenFromGeneralHeader(), preReqDTO );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PRE_REQ_UPDATED_SUCCESSFULLY.getKey(),
                        preReqDTO.getName(), preReqDTO.getRowId() ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_UPDATE_PRE_REQ.getKey(),
                        preReqDTO.getName(), preReqDTO.getRowId() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update dashboard data response.
     *
     * @param objectId
     *         the object id
     * @param action
     *         the action
     *
     * @return the response
     */
    @Override
    public Response updateDashboardData( String objectId, String action ) {
        try {
            Boolean success = pstManager.updateDashboardData( objectId, getTokenFromGeneralHeader(), action );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_DATA_UPDATED_SUCCESSFULLY.getKey() ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_UPDATE_PST_DATA.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Create dashboard data response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @Override
    public Response createDashboardData( String objectId ) {
        try {
            Boolean success = pstManager.createDashboardData( objectId, getTokenFromGeneralHeader() );
            if ( Boolean.TRUE == success ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_DATA_CREATED_SUCCESSFULLY.getKey() ) );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_PST_DATA.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets pst planning views.
     *
     * @param objectId
     *         the object id
     *
     * @return the pst planning views
     */
    @Override
    public Response getPstPlanningViews( String objectId ) {
        try {
            return ResponseUtils.success( pstManager.getUserPstPlanningViews( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Create pst planning view response.
     *
     * @param objectId
     *         the object id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response createPstPlanningView( String objectId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_ADDED_SUCCESSFULLY.getKey() ),
                    pstManager.saveOrUpdatePstPlanningViews( objectJson, getUserIdFromGeneralHeader(), true ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Update pst planning view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     * @param objectJson
     *         the object json
     *
     * @return the response
     */
    @Override
    public Response updatePstPlanningView( String objectId, String viewId, String objectJson ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_UPDATED_SUCCESSFULLY.getKey() ),
                    pstManager.saveOrUpdatePstPlanningViews( objectJson, getUserIdFromGeneralHeader(), false ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Delete pst planning view response.
     *
     * @param objectId
     *         the object id
     * @param viewId
     *         the view id
     *
     * @return the response
     */
    @Override
    public Response deletePstPlanningView( String objectId, String viewId ) {
        try {
            if ( pstManager.deletePstPlanningViews( viewId ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Export ace lunge response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @Override
    public Response exportAceLunge( String objectId ) {
        Path file = null;
        try {
            file = pstManager.getAceLungeFile( objectId, getTokenFromGeneralHeader() );
            return downloadExportFile( file, file.getFileName().toString() );
        } catch ( final Exception e ) {
            return handleException( e );
        } finally {
            FileUtils.deleteFiles( file );
        }
    }

    /**
     * Download export file response.
     *
     * @param file
     *         the file
     * @param nameForDownloadFile
     *         the name for download file
     *
     * @return the response
     *
     * @throws IOException
     *         the io exception
     */
    private static Response downloadExportFile( Path file, String nameForDownloadFile ) throws IOException {
        if ( Files.exists( file ) ) {
            Response.ResponseBuilder response = Response.ok( file.toFile() );
            response.header( "Content-Disposition", "attachment; filename=\"" + nameForDownloadFile + "\"" );
            response.header( Message.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM );
            response.header( "File-Size", Files.size( file ) );
            return response.build();
        } else {
            return ResponseUtils
                    .failure( MessageBundleFactory.getMessage( Messages.FILE_NOT_FOUND.getKey(), file.toAbsolutePath().toString() ) );
        }
    }

    /**
     * Export differences response.
     *
     * @param objectId
     *         the object id
     *
     * @return the response
     */
    @Override
    public Response exportDifferences( String objectId ) {
        try {
            Path file = pstManager.getTestDifferencesFile( objectId, getTokenFromGeneralHeader() );
            return downloadExportFile( file, String.format( PstConstants.EXPORT_DIFFERENCES_TEMPLATE,
                    DateUtils.changeFormatOfDateProvided( new Date(), DateUtils.DATE_PATTERN_DD_MM_YYYY ) ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    public Response exportDifferencesLink( String objectId ) {
        try {
            JSONObject obj = new JSONObject();
            String downloadBase = "/dashboard/pst/" + objectId + "/planning/export/differences/download";
            obj.put( "url", downloadBase );
            return ResponseUtils.success( obj );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Create pst planning archive response.
     *
     * @param objectId
     *         the object id
     * @param payload
     *         the payload
     *
     * @return the response
     */
    @Override
    public Response createPstPlanningArchive( String objectId, String payload ) {
        try {
            var jsonToMap = ( Map< String, String > ) JsonUtils.jsonToMap( payload, new HashMap<>() );
            if ( pstManager.createPstPlanningArchive( objectId, getTokenFromGeneralHeader(), jsonToMap.get( PstConstants.NAME ) ) ) {
                return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.ARCHIVE_ADDED_SUCCESSFULLY.getKey() ), true );
            } else {
                return ResponseUtils.failure( MessageBundleFactory.getMessage( Messages.FAILED_TO_ADD_ARCHIVE.getKey() ) );
            }
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets archives list.
     *
     * @param objectId
     *         the object id
     *
     * @return the archives list
     */
    @Override
    public Response getArchivesList( String objectId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.PST_ARCHIVE_LIST_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getArchivesList( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets status colors.
     *
     * @param objectId
     *         the object id
     *
     * @return the status colors
     */
    @Override
    public Response getLegend( String objectId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.COLORS_FILE_GENERATED_SUCCESSFULLY.getKey() ),
                    pstManager.getLegend( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Gets machine.
     *
     * @param objectId
     *         the object id
     *
     * @return the machine
     */
    @Override
    public Response getMachine( String objectId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.MACHINE_DATA_RETURNED_SUCCESSFULLY.getKey() ),
                    pstManager.getMachine( objectId, getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /**
     * Sets pst manager.
     *
     * @param pstManager
     *         the pst manager
     */
    public void setPstManager( PstManager pstManager ) {
        this.pstManager = pstManager;
    }

}
