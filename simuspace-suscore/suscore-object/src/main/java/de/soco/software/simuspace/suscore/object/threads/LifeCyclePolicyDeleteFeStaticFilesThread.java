package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObject3DceetronEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;

/**
 * The Class LifeCyclePolicyDeleteFeStaticFilesThread.
 *
 * @author Shahzeb Iqbal
 */
@Log4j2
public class LifeCyclePolicyDeleteFeStaticFilesThread extends UserThread {

    /**
     * The Constant VAULT.
     */
    private static final String FE_STATIC = "fe_static";

    /**
     * The Constant DELETION_POLICY.
     */
    private static final String DELETION_POLICY = "FeStatic Deletion Policy: ";

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The last Deletion Date
     */
    private Date lastDeletion;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The schedule DAO.
     */
    private ScheduleDAO scheduleDAO;

    /**
     * The life cycle config.
     */
    private List< LifeCyclePolicyDTO > lifeCycleConfig;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new delete object thread.
     */
    public LifeCyclePolicyDeleteFeStaticFilesThread() {
    }

    /**
     * Instantiates a new life cycle policy delete FeStatic files thread.
     *
     * @param susDAO
     *         the sus DAO
     * @param configManager
     *         the config manager
     * @param scheduleDAO
     *         the schedule DAO
     * @param lifeCycleConfig
     *         the life cycle config
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public LifeCyclePolicyDeleteFeStaticFilesThread( SuSGenericObjectDAO< SuSEntity > susDAO, ObjectTypeConfigManager configManager,
            ScheduleDAO scheduleDAO, List< LifeCyclePolicyDTO > lifeCycleConfig, EntityManagerFactory entityManagerFactory ) {
        super();
        this.susDAO = susDAO;
        this.configManager = configManager;
        this.scheduleDAO = scheduleDAO;
        this.lifeCycleConfig = lifeCycleConfig;
        this.entityManagerFactory = entityManagerFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.soco.software.simuspace.suscore.common.base.UserThread#run()
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            int intervalInhours = getIntervalTimeFromConfiguration();
            log.info( DELETION_POLICY + "Checking for LifeCycle deletion policy schedule" );

            ScheduleEntity scheduleEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, "name", FE_STATIC );
            if ( scheduleEntity == null ) {
                createSchedulerEntityIfNotExist( entityManager, intervalInhours );
            } else {
                lastDeletion = scheduleEntity.getLastRun();
                Date curentDate = new Date();
                if ( curentDate.compareTo( scheduleEntity.getNextRun() ) < 0 ) {
                    log.info( DELETION_POLICY + curentDate + " is before " + scheduleEntity.getNextRun() );
                } else if ( curentDate.compareTo( scheduleEntity.getNextRun() ) > 0 ) {
                    log.info( DELETION_POLICY + "policy criteria matched : scheduler called" );

                    try {
                        // execute policy if Schedule date and time met the conditions
                        executeLifeCyclePolicyDeletion( entityManager );
                    } catch ( Exception e ) {
                        log.warn( DELETION_POLICY + " Error : ", e );
                    }

                    log.info( DELETION_POLICY + "updating Next Schedule Date" );
                    // updating ScheduleEntity for next Schedule deletion
                    scheduleEntity.setLastRun( lastDeletion );
                    Date nextDate = getNextScheduleDate( intervalInhours );
                    scheduleEntity.setNextRun( nextDate );
                    scheduleDAO.saveOrUpdate( entityManager, scheduleEntity );
                }
            }

        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Creates the scheduler Entity if not exist.
     *
     * @param intervalInhours
     *         the interval in hours
     */
    private void createSchedulerEntityIfNotExist( EntityManager entityManager, int intervalInHours ) {
        // create ScheduleEntity if not existed
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId( UUID.randomUUID() );
        schedule.setLastRun( new Date( 0L ) );
        schedule.setName( FE_STATIC );

        Date nextDate = getNextScheduleDate( intervalInHours );
        schedule.setNextRun( nextDate );
        scheduleDAO.saveOrUpdate( entityManager, schedule );

        log.info( DELETION_POLICY + "LifeCycle deletion policy schedule Added in Entity" );
    }

    /**
     * Gets the next schedule date.
     *
     * @param intervalInhours
     *         the interval inhours
     *
     * @return the next schedule date
     */
    private Date getNextScheduleDate( int intervalInhours ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.HOUR_OF_DAY, +intervalInhours );
        return calendar.getTime();
    }

    /**
     * Gets the interval time from configuration.
     *
     * @return the interval time from configuration
     */
    private int getIntervalTimeFromConfiguration() {
        LifeCyclePolicyDTO lifeCycleDTO = lifeCycleConfig.stream()
                .filter( lifeCycle -> FE_STATIC.equalsIgnoreCase( lifeCycle.getLifeCyclePolicyApply() ) ).findFirst().orElse( null );
        if ( lifeCycleDTO == null ) {
            return 12;
        }
        var lifeCyclePolicyConfiguration = lifeCycleDTO.getPolicyProcess().stream().findFirst().orElse( null );
        return lifeCyclePolicyConfiguration != null ? Integer.parseInt( lifeCyclePolicyConfiguration.getProcessTimeHours() ) : 12;

    }

    /**
     * Execute life cycle policy deletion.
     */
    private void executeLifeCyclePolicyDeletion( EntityManager entityManager ) {

        Set< String > allDeletionDays = new LinkedHashSet<>();
        Set< String > allDeletionLifeCycles = new LinkedHashSet<>();

        getDeletionDaysAndLifeCycles( allDeletionDays, allDeletionLifeCycles );

        log.info( DELETION_POLICY + allDeletionDays + "Days " + " lifeCyle to delete" + allDeletionLifeCycles );

        int min = allDeletionDays.stream().mapToInt( Integer::parseInt ).min().getAsInt();
        allDeletionDays.clear();
        allDeletionDays.add( String.valueOf( min ) );
        Date latestDateProperty = prepareCreatedOnProperties( allDeletionDays );
        Date previousLatestDateProperty = lastDeletion;
        lastDeletion = latestDateProperty;

        List< Object > lifeCycleProperties = new ArrayList<>();
        prepareLifeCycleProperties( allDeletionLifeCycles, lifeCycleProperties );

        List< SuSEntity > criteriaDeletionList = susDAO.getRecordsForFeStaticThread( entityManager, DataObjectEntity.class,
                latestDateProperty, previousLatestDateProperty, lifeCycleProperties );

        List< SuSEntity > susEntitylist = new ArrayList<>();

        // again perform check with config lifecycle deletion policy
        prepareDeletionListwithConfig( criteriaDeletionList, susEntitylist );

        if ( !susEntitylist.isEmpty() ) {
            // perform final deletion task on any location
            log.info( DELETION_POLICY + "Deletion process started" );
            StringBuilder url = new StringBuilder();
            for ( SuSEntity susEntity : susEntitylist ) {
                try {
                    DataObjectEntity dataObject = ( ( DataObjectEntity ) susEntity );
                    url.append( PropertiesManager.getFeStaticPath() ).append( File.separator ).append( dataObject.getFile().getFilePath() );

                    // locate file and delete it
                    File file = new File( url.toString() );
                    if ( file.exists() ) {
                        deleteObjectFiles( url, susEntity, file );
                    }
                    if ( susEntity instanceof DataObject3DceetronEntity ) {
                        // additional check for ceetron entity because it is stored in a different subfolder in FESTATIC
                        deleteCeetronFiles( url, susEntity );

                    } else if ( susEntity instanceof DataObjectHtmlsEntity ) {
                        // additional check for html entity because it stores ceetron files in a different subfolder in FeStatic
                        deleteHtmlFiles( url, file );

                    }
                    url.setLength( ConstantsInteger.INTEGER_VALUE_ZERO );
                } catch ( Exception e ) {
                    url.setLength( ConstantsInteger.INTEGER_VALUE_ZERO );
                    log.error( DELETION_POLICY + " Skipping object " + susEntity.getName() + " ERROR : ", e );
                }
            }
        } else {
            log.info( DELETION_POLICY + " Fetched dataObject list is Empty : Skipping deletion" );
        }
    }

    /**
     * Delete object files.
     *
     * @param url
     *         the url
     * @param susEntity
     *         the sus entity
     * @param file
     *         the file
     *
     * @throws IOException
     *         the io exception
     */
    private static void deleteObjectFiles( StringBuilder url, SuSEntity susEntity, File file ) throws IOException {
        FileUtils.forceDelete( file ); // forcing delete as it might be a non-empty directory
        log.info( DELETION_POLICY + url + "Local DELETED" );

        if ( susEntity instanceof DataObjectImageEntity ) {
            // additonal check for image entity because it creates 2 copies of image tumbnail
            try {
                File fileThumb = new File( url + "thumb" );
                if ( fileThumb.exists() ) {
                    FileUtils.forceDelete( fileThumb );
                }
            } catch ( Exception e ) {
                log.info( DELETION_POLICY + url + "thumb deletion error :", e );

            }
        }
    }

    /**
     * Delete html files.
     *
     * @param url
     *         the url
     * @param file
     *         the file
     */
    private static void deleteHtmlFiles( StringBuilder url, File file ) {
        try {
            url.setLength( ConstantsInteger.INTEGER_VALUE_ZERO );
            url.append( PropertiesManager.getCeetronOutputPath().asText() );
            File ceeFolder = new File( url.toString() );
            if ( ceeFolder.exists() && ceeFolder.isDirectory() ) {
                for ( File subFolder : ceeFolder.listFiles() ) {
                    if ( subFolder.exists() && subFolder.isDirectory() && subFolder.getName()
                            .startsWith( ConstantsString.PREFIX_3D_MODEL_FOLDER ) && subFolder.getName().endsWith( file.getName() ) ) {
                        FileUtils.forceDelete( subFolder );
                    }
                }
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        }
    }

    /**
     * Delete ceetron files.
     *
     * @param url
     *         the url
     * @param susEntity
     *         the sus entity
     */
    private static void deleteCeetronFiles( StringBuilder url, SuSEntity susEntity ) {
        try {
            url.setLength( ConstantsInteger.INTEGER_VALUE_ZERO );
            url.append( PropertiesManager.getCeetronOutputPath().asText() ).append( ConstantsString.PREFIX_3D_MODEL_FOLDER )
                    .append( susEntity.getName() ).append( susEntity.getComposedId().getId() );
            File ceeFolder = new File( url.toString() );
            if ( ceeFolder.exists() ) {
                FileUtils.forceDelete( ceeFolder );
            }
        } catch ( Exception e ) {
            log.info( DELETION_POLICY + url + "ceetron deletion error :", e );

        }
    }

    /**
     * Prepare deletion listwith config.
     *
     * @param criteriaDeletionList
     *         the criteria deletion list
     * @param susEntityList
     *         the sus entity list
     */
    private void prepareDeletionListwithConfig( List< SuSEntity > criteriaDeletionList, List< SuSEntity > susEntityList ) {
        for ( SuSEntity susEntity : criteriaDeletionList ) {
            if ( susEntity.isAutoDelete() && shouldDeleteByLifeCycle( susEntity ) ) {
                susEntityList.add( susEntity );
                if ( lastDeletion.after( susEntity.getCreatedOn() ) ) {
                    lastDeletion = susEntity.getCreatedOn();
                }
            }

        }
    }

    /**
     * Should delete by life cycle boolean.
     *
     * @param susEntity
     *         the sus entity
     *
     * @return the boolean
     */
    private boolean shouldDeleteByLifeCycle( SuSEntity susEntity ) {
        Map< String, ProjectConfiguration > masterConfig = configManager.getProjectConfigurationsMap();
        for ( Entry< String, ProjectConfiguration > pConfig : masterConfig.entrySet() ) {
            if ( pConfig.getKey().equalsIgnoreCase( susEntity.getConfig() ) ) {
                ProjectConfiguration config = pConfig.getValue();
                for ( SuSObjectModel susObjectModel : config.getEntityConfig() ) {
                    if ( susObjectModel.getId().equalsIgnoreCase( susEntity.getTypeId().toString() ) && susObjectModel.isDeletionPolicy()
                            && susObjectModel.getDeletionDays() != null && getDataObjectDate( susEntity ).before(
                            getPerviousDate( susObjectModel ) ) && matchLifeCycleCriteria( susEntity, susObjectModel ) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Match life cycle criteria boolean.
     *
     * @param susEntity
     *         the sus entity
     * @param susObjectModel
     *         the sus object model
     *
     * @return the boolean
     */
    private static boolean matchLifeCycleCriteria( SuSEntity susEntity, SuSObjectModel susObjectModel ) {
        log.info( DELETION_POLICY + "Date criteria Matched" );
        List< String > lc = susObjectModel.getDeletionLifeCycle();
        if ( lc != null && !lc.isEmpty() ) {
            for ( String lifecycleId : lc ) {
                if ( susEntity.getLifeCycleStatus().equalsIgnoreCase( lifecycleId ) ) {
                    log.info( DELETION_POLICY + "lifeCycle criteria Matched : adding Obj to List" );
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the data object date.
     *
     * @param suSEntity
     *         the su S entity
     *
     * @return the data object date
     */
    private Date getDataObjectDate( SuSEntity suSEntity ) {
        Calendar createdOn = Calendar.getInstance();
        createdOn.setTime( suSEntity.getCreatedOn() );
        return createdOn.getTime();
    }

    /**
     * Gets the pervious date.
     *
     * @param suSObjectModel
     *         the su S object model
     *
     * @return the pervious date
     */
    private Date getPerviousDate( SuSObjectModel suSObjectModel ) {
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( myDate );
        calendar.add( Calendar.DAY_OF_YEAR, -Integer.parseInt( suSObjectModel.getDeletionDays() ) );
        return calendar.getTime();
    }

    /**
     * Prepare life cycle properties.
     *
     * @param allDeletionLifeCycles
     *         the all deletion life cycles
     * @param lifeCycleProperties
     *         the life cycle properties
     */
    private void prepareLifeCycleProperties( Set< String > allDeletionLifeCycles, List< Object > lifeCycleProperties ) {
        lifeCycleProperties.addAll( allDeletionLifeCycles );
    }

    /**
     * Prepare created on properties.
     *
     * @param allDeletionDays
     *         the all deletion days
     *
     * @return the date
     */
    private Date prepareCreatedOnProperties( Set< String > allDeletionDays ) {
        String days = allDeletionDays.stream().findFirst().orElse( null );
        if ( days != null ) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( new Date() );
            calendar.add( Calendar.DAY_OF_MONTH, -Integer.parseInt( days ) );
            return calendar.getTime();
        } else {
            log.info( DELETION_POLICY + "Deletion hrs are Not Set : Skipping deletion policy" );
            return null;
        }
    }

    /**
     * Gets the deletion days and life cycles.
     *
     * @param allDeletionDays
     *         the all deletion days
     * @param allDeletionLifeCycles
     *         the all deletion life cycles
     */
    private void getDeletionDaysAndLifeCycles( Set< String > allDeletionDays, Set< String > allDeletionLifeCycles ) {
        Map< String, ProjectConfiguration > masterConfig = configManager.getProjectConfigurationsMap();
        for ( Entry< String, ProjectConfiguration > pConfig : masterConfig.entrySet() ) {
            ProjectConfiguration config = pConfig.getValue();
            for ( SuSObjectModel suSObjectModel : config.getEntityConfig() ) {
                if ( suSObjectModel.isDeletionPolicy() ) {
                    if ( suSObjectModel.getDeletionDays() != null ) {
                        allDeletionDays.add( suSObjectModel.getDeletionDays() );
                    }
                    List< String > lc = suSObjectModel.getDeletionLifeCycle();
                    if ( lc != null && !lc.isEmpty() ) {
                        allDeletionLifeCycles.addAll( lc );
                    }
                }
            }
        }
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
     * Gets the schedule DAO.
     *
     * @return the schedule DAO
     */
    public ScheduleDAO getScheduleDAO() {
        return scheduleDAO;
    }

    /**
     * Sets the schedule DAO.
     *
     * @param scheduleDAO
     *         the new schedule DAO
     */
    public void setScheduleDAO( ScheduleDAO scheduleDAO ) {
        this.scheduleDAO = scheduleDAO;
    }

    /**
     * Gets the life cycle config.
     *
     * @return the life cycle config
     */
    public List< LifeCyclePolicyDTO > getLifeCycleConfig() {
        return lifeCycleConfig;
    }

    /**
     * Sets the life cycle config.
     *
     * @param lifeCycleConfig
     *         the new life cycle config
     */
    public void setLifeCycleConfig( List< LifeCyclePolicyDTO > lifeCycleConfig ) {
        this.lifeCycleConfig = lifeCycleConfig;
    }

}