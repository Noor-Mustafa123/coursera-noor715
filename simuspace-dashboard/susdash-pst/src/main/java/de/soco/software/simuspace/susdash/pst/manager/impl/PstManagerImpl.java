package de.soco.software.simuspace.susdash.pst.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.DashboardConfigEnums;
import de.soco.software.simuspace.suscore.common.enums.DashboardPluginEnums;
import de.soco.software.simuspace.suscore.common.enums.ExitCodesAndSignals;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.model.DashboardPluginConfigDTO;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.DashboardPluginUtil;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.permissions.manager.PermissionManager;
import de.soco.software.simuspace.susdash.pst.constants.PstConstants;
import de.soco.software.simuspace.susdash.pst.manager.PstManager;
import de.soco.software.simuspace.susdash.pst.model.CoupleDTO;
import de.soco.software.simuspace.susdash.pst.model.PreReqDTO;
import de.soco.software.simuspace.susdash.pst.model.PstDTO;
import de.soco.software.simuspace.susdash.pst.util.PstUtil;

/**
 * The type Pst manager.
 */
@Log4j2
public class PstManagerImpl implements PstManager {

    /**
     * The Sus dao.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The Object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Permission manager.
     */
    private PermissionManager permissionManager;

    /**
     * The Update all dashboards future.
     */
    private ScheduledFuture< ? > updateDashboardFeature = null;

    /**
     * The Config.
     */
    private final DashboardPluginConfigDTO pstPlanningConfig = DashboardPluginUtil.getPluginConfigByPluginAndConfig(
            DashboardConfigEnums.PST_PLANNING.getPluginId(), DashboardConfigEnums.PST_PLANNING.getConfig() );

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Init.
     */
    public void init() {
        try {
            scheduleUpdateThread( ConstantsString.DEFAULT_LANGUAGE );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Gets pst test chart.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the pst test chart
     */
    @Override
    public Map< String, Object > getPstTestJson( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readTestJson();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets pst test archive.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archive
     *         the archive
     *
     * @return the pst test archive
     */
    @Override
    public Map< String, Object > getPstTestArchive( String objectId, String token, String archive ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readTestArchive( archive );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets pst bench chart.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the pst bench chart
     */
    @Override
    public Map< String, Object > getPstBenchData( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readBenchJson();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets pst bench archive.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archive
     *         the archive
     *
     * @return the pst bench archive
     */
    @Override
    public Map< String, Object > getPstBenchArchive( String objectId, String token, String archive ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readBenchArchive( archive );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Couple test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param coupleDTO
     *         the couple dto
     *
     * @return the boolean
     */
    @Override
    public Boolean coupleTestSchedule( String objectId, String token, CoupleDTO coupleDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        Path inputFile = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.COUPLE_TEST );
            checkWritePermissions( objectId, token, entityManager );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.COUPLE_TEST,
                    coupleDTO, null, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
            FileUtils.deleteFiles( inputFile );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.COUPLE_TEST );

        }
        return result;
    }

    /**
     * Move test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the move settings
     *
     * @return the boolean
     */
    @Override
    public Boolean editTestSchedule( String objectId, String token, PstDTO pstDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        Path inputFile = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.EDIT_TEST );
            checkWritePermissions( objectId, token, entityManager );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.EDIT_TEST,
                    pstDTO, null, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
            FileUtils.deleteFiles( inputFile );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.EDIT_TEST );
        }
        return result;
    }

    /**
     * Add test schedule boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the pst dto
     *
     * @return the boolean
     */
    @Override
    public Boolean addTestSchedule( String objectId, String token, PstDTO pstDTO ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        Path inputFile = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.ADD_TEST );
            checkWritePermissions( objectId, token, entityManager );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.ADD_TEST,
                    pstDTO, null, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
            FileUtils.deleteFiles( inputFile );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.ADD_TEST );
        }
        return result;
    }

    /**
     * Edit test schedule ui list.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param pstDTO
     *         the pst dto
     *
     * @return the list
     */
    @Override
    public List< UIFormItem > editTestScheduleUI( String objectId, String token, PstDTO pstDTO ) {
        Path updatedFormJson = null;
        Path inputFile = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.GET_FORM );
            updatedFormJson = Path.of( PropertiesManager.getDefaultServerTempPath() + File.separator + objectId + ConstantsString.UNDERSCORE
                    + PstConstants.ACTIONS.GET_FORM + PstConstants.OUTPUT_POSTFIX );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.GET_FORM,
                    null, updatedFormJson.toAbsolutePath().toString(), null, CommonUtils.resolveLanguage( token ) );
            boolean result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
            List< UIFormItem > formItemsFromFile = null;
            if ( result ) {
                formItemsFromFile = PstUtil.readFormItemsFromFormJson( updatedFormJson );
                for ( UIFormItem item : formItemsFromFile ) {
                    item.setValue( PstUtil.getFormItemValueFromDTO( item, pstDTO ) );
                }
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_PREPARE_UI_TO_EDIT_TEST.getKey() ) );
            }
            return formItemsFromFile;
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            FileUtils.deleteFiles( inputFile, updatedFormJson );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.GET_FORM );
        }
    }

    /**
     * Gets value from dto.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the value from dto
     */
    @Override
    public List< UIFormItem > addTestScheduleUI( String objectId, String token ) {
        Path inputFile = null;
        Path updatedFormJson = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.GET_FORM );
            updatedFormJson = Path.of( PropertiesManager.getDefaultServerTempPath() + File.separator + objectId + ConstantsString.UNDERSCORE
                    + PstConstants.ACTIONS.GET_FORM + PstConstants.OUTPUT_POSTFIX );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.GET_FORM,
                    null, updatedFormJson.toAbsolutePath().toString(), null, CommonUtils.resolveLanguage( token ) );
            boolean result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
            if ( result ) {
                return PstUtil.readFormItemsFromFormJson( updatedFormJson );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_PREPARE_UI_TO_ADD_TEST.getKey() ) );
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            FileUtils.deleteFiles( inputFile, updatedFormJson );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.GET_FORM );
        }
    }

    /**
     * Update pre req boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param preReq
     *         the pre req
     *
     * @return the boolean
     */
    @Override
    public Boolean updatePreReq( String objectId, String token, PreReqDTO preReq ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        Path inputFile = null;
        try {
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.UPDATE_PREREQ );
            checkWritePermissions( objectId, token, entityManager );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId,
                    PstConstants.ACTIONS.UPDATE_PREREQ, preReq, null, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
            FileUtils.deleteFiles( inputFile );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.UPDATE_PREREQ );
        }
        return result;
    }

    /**
     * Update dashboard boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param action
     *         the action
     *
     * @return the boolean
     */
    @Override
    public Boolean updateDashboardData( String objectId, String token, String action ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        try {
            checkReadPermissions( objectId, token, entityManager );
            switch ( action ) {
                case PstConstants.ACE -> result = updateDashboardACEData( objectId,
                        CommonUtils.resolveLanguage( token ) ) == ExitCodesAndSignals.SUCCESS.getExitCode();
                case PstConstants.DAILY -> result = updateDashboardDailyData( objectId,
                        CommonUtils.resolveLanguage( token ) ) == ExitCodesAndSignals.SUCCESS.getExitCode();
                default -> result = false;
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * Update dashboard boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the boolean
     */
    @Override
    public Boolean createDashboardData( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        try {
            checkWritePermissions( objectId, token, entityManager );
            result = createDashboardDataWithLanguage( objectId, CommonUtils.resolveLanguage( token ) ) == ExitCodesAndSignals.SUCCESS
                    .getExitCode();
            if ( updateDashboardFeature == null ) {
                scheduleUpdateThread( CommonUtils.resolveLanguage( token ) );
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * Delete pst planning views boolean.
     *
     * @param viewId
     *         the view id
     *
     * @return the boolean
     */
    @Override
    public boolean deletePstPlanningViews( String viewId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.deleteObjectView( entityManager, UUID.fromString( viewId ) );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets user pst planning views.
     *
     * @param token
     *         the token
     *
     * @return the user pst planning views
     */
    @Override
    public List< ObjectViewDTO > getUserPstPlanningViews( String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.getUserObjectViewsByKey( entityManager, ConstantsObjectViewKey.PST_PLANNING_TABLE_VIEW_KEY,
                    Objects.requireNonNull( TokenizedLicenseUtil.getNotNullUser( token ) ).getUserUid(), null );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Save or update pst planning views object view dto.
     *
     * @param objectJson
     *         the object json
     * @param token
     *         the token
     * @param save
     *         the save
     *
     * @return the object view dto
     */
    @Override
    public ObjectViewDTO saveOrUpdatePstPlanningViews( String objectJson, String token, boolean save ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return objectViewManager.saveOrUpdateObjectView( entityManager, PstUtil.prepareObjectView( objectJson, save ),
                    Objects.requireNonNull( TokenizedLicenseUtil.getNotNullUser( token ) ).getUserUid() );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets ace lunge file.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the ace lunge file
     */
    @Override
    public Path getAceLungeFile( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        try {
            checkReadPermissions( objectId, token, entityManager );
            String outputFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator
                    + TokenizedLicenseUtil.getNotNullUser( token ).getUserUid() + File.separator + objectId + ConstantsString.UNDERSCORE
                    + PstConstants.ACTIONS.EXPORT + PstConstants.OUTPUT_POSTFIX;
            result = prepareAceLungeFileForExport( outputFilePath, objectId, token ) == ExitCodesAndSignals.SUCCESS.getExitCode();
            if ( result ) {
                return Path.of( outputFilePath );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_PREPARE_FILE_FOR_EXPORT.getKey() ) );
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets test differences file.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the test differences file
     */
    @Override
    public Path getTestDifferencesFile( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean result;
        try {
            checkReadPermissions( objectId, token, entityManager );
            String outputFilePath = PropertiesManager.getDefaultServerTempPath() + File.separator
                    + TokenizedLicenseUtil.getNotNullUser( token ).getUserUid() + File.separator + objectId + ConstantsString.UNDERSCORE
                    + PstConstants.ACTIONS.EXPORT_DIFFERENCES + PstConstants.OUTPUT_POSTFIX;
            Path outputPath = Path.of( outputFilePath );
            Files.deleteIfExists( outputPath );
            result = prepareTestDifferencesFileForExport( outputFilePath, objectId, token ) == ExitCodesAndSignals.SUCCESS.getExitCode();
            if ( result ) {
                return outputPath;
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_PREPARE_FILE_FOR_EXPORT.getKey() ) );
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    private int prepareTestDifferencesFileForExport( String outputFilePath, String objectId, String token ) {
        Path inputFile = null;
        int result;
        try {
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId,
                    PstConstants.ACTIONS.EXPORT_DIFFERENCES, null, outputFilePath, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ), pstPlanningConfig );
        } finally {
            FileUtils.deleteFiles( inputFile );
        }
        return result;
    }

    /**
     * Create pst planning archive boolean.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param archiveName
     *         the archive name
     *
     * @return the boolean
     */
    @Override
    public boolean createPstPlanningArchive( String objectId, String token, String archiveName ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Path inputFile = null;
        PstUtil.acquireLockByAction( PstConstants.ACTIONS.ADD_ARCHIVE );
        try {
            checkWritePermissions( objectId, token, entityManager );
            PstUtil.acquireLockByAction( PstConstants.ACTIONS.ADD_ARCHIVE );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.ADD_ARCHIVE,
                    null, null, archiveName, CommonUtils.resolveLanguage( token ) );
            return PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
            FileUtils.deleteFiles( inputFile );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.ADD_ARCHIVE );
        }
    }

    /**
     * Gets archives list.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the archives list
     */
    @Override
    public Map< String, Object > getArchivesList( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readArchiveJson();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets status colors.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the status colors
     */
    @Override
    public Map< String, Object > getLegend( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Path inputFile = null;
        Path colorJson = null;
        PstUtil.acquireLockByAction( PstConstants.ACTIONS.GET_LEGEND );
        try {
            checkReadPermissions( objectId, token, entityManager );
            colorJson = Path.of( PropertiesManager.getDefaultServerTempPath() + File.separator
                    + TokenizedLicenseUtil.getNotNullUser( token ).getUserUid() + File.separator + objectId + ConstantsString.UNDERSCORE
                    + PstConstants.ACTIONS.GET_LEGEND + PstConstants.OUTPUT_POSTFIX );
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.GET_LEGEND,
                    null, colorJson.toAbsolutePath().toString(), null, CommonUtils.resolveLanguage( token ) );
            boolean result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ),
                    pstPlanningConfig ) == ExitCodesAndSignals.SUCCESS.getExitCode();
            if ( result ) {
                return PstUtil.readJsonFileAsMap( colorJson );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_GENERATE_COLOR_FILE.getKey() ) );
            }
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            FileUtils.deleteFiles( inputFile, colorJson );
            PstUtil.releaseLockByAction( PstConstants.ACTIONS.GET_LEGEND );
            entityManager.close();
        }

    }

    /**
     * Gets machine.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the machine
     */
    @Override
    public Map< String, Object > getMachine( String objectId, String token ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            checkReadPermissions( objectId, token, entityManager );
            return readMachineJson();
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        } finally {
            entityManager.close();
        }

    }

    /**
     * Read machine json map.
     *
     * @return the map
     */
    private Map< String, Object > readMachineJson() {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var machineJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator
                + propertiesMap.get( PstConstants.CONFIG_KEYS.MASCHINE_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( machineJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_MACHINE_DATA.getKey() ) );
        }
    }

    /**
     * Prepare ace lunge file for export int.
     *
     * @param outputFilePath
     *         the output file path
     * @param objectId
     *         the object id
     * @param token
     *         the token
     *
     * @return the int
     */
    private int prepareAceLungeFileForExport( String outputFilePath, String objectId, String token ) {
        Path inputFile = null;
        int result;
        try {
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.EXPORT,
                    null, outputFilePath, null, CommonUtils.resolveLanguage( token ) );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ), pstPlanningConfig );
        } finally {
            FileUtils.deleteFiles( inputFile );
        }
        return result;
    }

    /**
     * Check write permissions.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param entityManager
     *         the entity manager
     */
    private void checkWritePermissions( String objectId, String token, EntityManager entityManager ) {
        DataObjectDashboardEntity dashboardEntity = ( DataObjectDashboardEntity ) susDAO.getLatestObjectById( entityManager,
                DataObjectDashboardEntity.class, UUID.fromString( objectId ) );
        var user = TokenizedLicenseUtil.getNotNullUser( token );
        if ( !permissionManager.isWritable( entityManager, UUID.fromString( Objects.requireNonNull( user ).getId() ),
                dashboardEntity.getComposedId().getId() ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_UPDATE.getKey(), dashboardEntity.getComposedId().getId() ) );
        }
    }

    /**
     * Check read permissions.
     *
     * @param objectId
     *         the object id
     * @param token
     *         the token
     * @param entityManager
     *         the entity manager
     */
    private void checkReadPermissions( String objectId, String token, EntityManager entityManager ) {
        DataObjectDashboardEntity dashboardEntity = ( DataObjectDashboardEntity ) susDAO.getLatestObjectById( entityManager,
                DataObjectDashboardEntity.class, UUID.fromString( objectId ) );
        var user = TokenizedLicenseUtil.getNotNullUser( token );
        if ( !permissionManager.isReadable( entityManager, UUID.fromString( Objects.requireNonNull( user ).getId() ),
                dashboardEntity.getComposedId().getId() ) ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.NO_RIGHTS_TO_READ.getKey() ) );
        }
    }

    /**
     * Read test json map.
     *
     * @return the map
     */
    private Map< String, Object > readTestJson() {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var testJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator
                + propertiesMap.get( PstConstants.CONFIG_KEYS.TEST_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( testJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_TEST_DATA.getKey() ) );
        }
    }

    /**
     * Read test json map.
     *
     * @param archiveName
     *         the archive name
     *
     * @return the map
     */
    private Map< String, Object > readTestArchive( String archiveName ) {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var testJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator + archiveName
                + File.separator + propertiesMap.get( PstConstants.CONFIG_KEYS.TEST_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( testJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_TEST_DATA.getKey() ) );
        }
    }

    /**
     * Read bench json map.
     *
     * @return the map
     */
    private Map< String, Object > readBenchJson() {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var benchJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator
                + propertiesMap.get( PstConstants.CONFIG_KEYS.BENCH_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( benchJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_BENCHES_DATA.getKey() ) );
        }
    }

    /**
     * Read bench json map.
     *
     * @param archiveName
     *         the archive name
     *
     * @return the map
     */
    private Map< String, Object > readBenchArchive( String archiveName ) {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var benchJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator + archiveName
                + File.separator + propertiesMap.get( PstConstants.CONFIG_KEYS.BENCH_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( benchJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_BENCHES_DATA.getKey() ) );
        }
    }

    /**
     * Read archive json map.
     *
     * @return the map
     */
    private Map< String, Object > readArchiveJson() {
        var propertiesMap = pstPlanningConfig.getProperties().get( ConstantsInteger.INTEGER_VALUE_ZERO );
        var archiveJsonPath = Paths.get( propertiesMap.get( PstConstants.CONFIG_KEYS.BASE_PATH ) + File.separator
                + propertiesMap.get( PstConstants.CONFIG_KEYS.ARCHIVE_JSON ) );
        try {
            return PstUtil.readJsonFileAsMap( archiveJsonPath );
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.ERROR_READING_TEST_DATA.getKey() ) );
        }
    }

    /**
     * Schedule update thread.
     *
     * @param language
     *         the language
     */
    private void scheduleUpdateThread( String language ) {
        if ( Boolean.TRUE != pstPlanningConfig.getProperties().get( 0 ).get( PstConstants.CONFIG_KEYS.AUTO_UPDATE ) ) {
            return;
        }
        ScheduledExecutorService executor = Executors.newScheduledThreadPool( 1 );

        // Get current time
        Calendar now = Calendar.getInstance();

        // Calculate delay until time from config
        long delay = PstUtil.getTimeUntilFirstRun( now, pstPlanningConfig );

        // If it's already past time from config, schedule the task for the next day
        if ( delay < 0 ) {
            delay += TimeUnit.DAYS.toMillis( 1 );
        }

        // Schedule the task to run daily at time from config
        updateDashboardFeature = executor.scheduleAtFixedRate( () -> updateDashboardDataIfExists( language ), delay,
                TimeUnit.DAYS.toMillis( 1 ), TimeUnit.MILLISECONDS );
    }

    /**
     * Pst planning dashboard exists boolean.
     *
     * @return the boolean
     */
    private String getExistingPstDashboardId() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var allDashboardsByPlugin = susDAO.getLatestNonDeletedObjectListByProperty( entityManager, DataObjectDashboardEntity.class,
                "plugin", DashboardPluginEnums.PST.getId() );
        // AtomicBoolean dashboardExists = new AtomicBoolean( false );
        var dashboard = allDashboardsByPlugin.stream()
                .filter( susEntity -> susEntity instanceof DataObjectDashboardEntity dashboardEntity && pstPlanningConfig.getName()
                        .equals( ( ( Map< String, String > ) JsonUtils
                                .jsonToMap( ByteUtil.convertByteToString( dashboardEntity.getSettings() ), new HashMap<>() ) )
                                .get( "config" ) ) )
                .map( susEntity -> ( DataObjectDashboardEntity ) susEntity ).findFirst().orElse( null );
        if ( dashboard != null ) {
            return dashboard.getComposedId().getId().toString();
        }
        return null;
    }

    /**
     * Update dashboard data int.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the int
     */
    private int updateDashboardACEData( String objectId, String language ) {
        Path inputFile = null;
        int result;
        try {
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.UPDATE_ACE,
                    null, null, null, language );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ), pstPlanningConfig );
            return result;
        } finally {
            FileUtils.deleteFiles( inputFile );
        }
    }

    /**
     * Update dashboard daily data int.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the int
     */
    private int updateDashboardDailyData( String objectId, String language ) {
        Path inputFile = null;
        int result;
        try {
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId,
                    PstConstants.ACTIONS.UPDATE_DAILY, null, null, null, language );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ), pstPlanningConfig );
        } finally {
            FileUtils.deleteFiles( inputFile );
        }
        return result;
    }

    /**
     * Update dashboard date.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     */
    private void updateDashboardData( String objectId, String language ) {
        updateDashboardDailyData( objectId, language );
        updateDashboardACEData( objectId, language );

    }

    /**
     * Update dashboard data if exists.
     *
     * @param language
     *         the language
     */
    private void updateDashboardDataIfExists( String language ) {
        String objectId = getExistingPstDashboardId();
        if ( objectId != null ) {
            updateDashboardData( objectId, language );
        }
    }

    /**
     * Update dashboard data int.
     *
     * @param objectId
     *         the object id
     * @param language
     *         the language
     *
     * @return the int
     */
    private int createDashboardDataWithLanguage( String objectId, String language ) {
        Path inputFile = null;
        int result;
        try {
            inputFile = PstUtil.preparePstInputFile( pstPlanningConfig.getProperties().get( 0 ), objectId, PstConstants.ACTIONS.CREATE,
                    null, null, null, language );
            result = PstUtil.callPstScript( PstUtil.preparePstArgs( inputFile.toAbsolutePath().toString() ), pstPlanningConfig );
        } finally {
            FileUtils.deleteFiles( inputFile );
        }
        return result;
    }

    /**
     * Destroy.
     */
    public void destroy() {
        if ( updateDashboardFeature != null ) {
            updateDashboardFeature.cancel( true );
        }
    }

    /**
     * Sets sus dao.
     *
     * @param susDAO
     *         the sus dao
     */
    public void setSusDAO( SuSGenericObjectDAO< SuSEntity > susDAO ) {
        this.susDAO = susDAO;
    }

    /**
     * Sets object view manager.
     *
     * @param objectViewManager
     *         the object view manager
     */
    public void setObjectViewManager( ObjectViewManager objectViewManager ) {
        this.objectViewManager = objectViewManager;
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
     * Sets permission manager.
     *
     * @param permissionManager
     *         the permission manager
     */
    public void setPermissionManager( PermissionManager permissionManager ) {
        this.permissionManager = permissionManager;
    }

}
