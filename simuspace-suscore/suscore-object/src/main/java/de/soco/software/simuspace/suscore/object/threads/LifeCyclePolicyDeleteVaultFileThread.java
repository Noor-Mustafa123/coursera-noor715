package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.ProjectConfiguration;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.DataObjectEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.LocationEntity;
import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;
import de.soco.software.suscore.jsonschema.model.PolicyProcess;

/**
 * The Class LifeCyclePolicyDeleteVaultFileThread.
 *
 * @author noman arshad
 */
@Log4j2
public class LifeCyclePolicyDeleteVaultFileThread extends UserThread {

    /**
     * The Constant VAULT.
     */
    private static final String VAULT = "vault";

    /**
     * The Constant API_LOCATION_DELETE_FILE.
     */
    private static final String API_LOCATION_DELETE_FILE = "/api/core/location/delete/file";

    /**
     * The Constant PATH_KEY.
     */
    private static final String PATH_KEY = "path";

    /**
     * The Constant DELETION_POLICY.
     */
    private static final String DELETION_POLICY = "Vault Deletion Policy: ";

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

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
    public LifeCyclePolicyDeleteVaultFileThread() {
    }

    /**
     * Instantiates a new life cycle policy delete file thread.
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
    public LifeCyclePolicyDeleteVaultFileThread( SuSGenericObjectDAO< SuSEntity > susDAO, ObjectTypeConfigManager configManager,
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
            int intervalInhours = getIntervalTimeFromConfigration();
            log.info( DELETION_POLICY + "Checking for LifeCycle deletion policy schedule" );

            ScheduleEntity scheduleEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, "name", VAULT );
            if ( scheduleEntity == null ) {
                createSchedulerENtityIfNotExist( entityManager, intervalInhours );
            } else {

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
                    scheduleEntity.setLastRun( new Date() );
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
     * @param entityManager
     *         the entity manager
     * @param intervalInhours
     *         the interval inhours
     */
    private void createSchedulerENtityIfNotExist( EntityManager entityManager, int intervalInhours ) {
        // create ScheduleEntity if not existed
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId( UUID.randomUUID() );
        schedule.setLastRun( new Date() );
        schedule.setName( VAULT );

        Date nextDate = getNextScheduleDate( intervalInhours );
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
     * Gets the interval time from configration.
     *
     * @return the interval time from configration
     */
    private int getIntervalTimeFromConfigration() {
        LifeCyclePolicyDTO lifeCycle = lifeCycleConfig.stream()
                .filter( lifeCycleDTO -> lifeCycleDTO.getLifeCyclePolicyApply().equalsIgnoreCase( VAULT ) ).findFirst().orElse( null );
        if ( lifeCycle == null ) {
            // by default value is 12 hrs for lifecycle policy
            return 12;
        }
        PolicyProcess policyProcess = lifeCycle.getPolicyProcess().stream().findFirst().orElse( null );
        // by default value is 12 hrs for lifecycle policy
        return policyProcess != null ? Integer.parseInt( policyProcess.getProcessTimeHours() ) : 12;
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
        Date minDateProperty = prepareCreatedOnProperties( allDeletionDays );

        List< Object > lifeCycleProperties = new ArrayList<>();
        prepareLifeCycleProperties( allDeletionLifeCycles, lifeCycleProperties );

        List< SuSEntity > criteriaDletionList = susDAO.getAllPreviousRecordsWithCreatedOnAndProperties( entityManager, SuSEntity.class,
                minDateProperty, "lifeCycleStatus", lifeCycleProperties, false );

        List< SuSEntity > susEntitylist = new ArrayList<>();

        // again perform check with config lifecycle deletion policy
        prepareDeletionListwithConfig( criteriaDletionList, susEntitylist );

        if ( !susEntitylist.isEmpty() ) {
            // perform final deletion task on any location
            log.info( DELETION_POLICY + "Deletion process started" );
            for ( SuSEntity suSEntity : susEntitylist ) {
                try {
                    if ( suSEntity instanceof DataObjectEntity dataObject ) {
                        List< LocationEntity > locations = dataObject.getFile().getLocations();
                        for ( LocationEntity location : locations ) {

                            if ( PropertiesManager.getLocationURL().equalsIgnoreCase( location.getUrl() ) ) {
                                deleteFilesFromDefaultLocation( entityManager, suSEntity, dataObject );
                            } else {
                                deleteFilesFromRemoteLocation( entityManager, suSEntity, dataObject, location );
                            }
                        }
                    }
                } catch ( Exception e ) {
                    log.error( DELETION_POLICY + " Skipping object" + suSEntity.getName() + " ERROR : ", e );
                }
            }
        } else {
            log.info( DELETION_POLICY + " Fetched dataObject list is Empty : Skipping deletion" );
        }
    }

    /**
     * Delete files from remote location.
     *
     * @param entityManager
     *         the entity manager
     * @param suSEntity
     *         the su s entity
     * @param dataObject
     *         the data object
     * @param location
     *         the location
     */
    private void deleteFilesFromRemoteLocation( EntityManager entityManager, SuSEntity suSEntity, DataObjectEntity dataObject,
            LocationEntity location ) {
        String locationURl = location.getUrl() + location.getVault() + dataObject.getFile().getFilePath();
        log.info( DELETION_POLICY + "Object deletion on location STARTED" );
        Map< String, String > pathMap = new HashMap<>();
        pathMap.put( PATH_KEY, dataObject.getFile().getFilePath() );
        // delete from all other locations.
        final String josnPath = JsonUtils.toJsonString( pathMap );
        SusResponseDTO response = SuSClient.postRequest( location.getUrl() + API_LOCATION_DELETE_FILE, josnPath,
                CommonUtils.prepareHeadersWithAuthToken( location.getAuthToken() ) );
        log.info( DELETION_POLICY + "response " + response.getSuccess() + "  " + response.getMessage() );

        if ( response.getSuccess() ) {
            suSEntity.setAutoDelete( true );
            susDAO.saveOrUpdate( entityManager, suSEntity );
            log.info( DELETION_POLICY + locationURl + "File on Location DELTED" );
        }
    }

    /**
     * Delete files from default location.
     *
     * @param suSEntity
     *         the su s entity
     * @param dataObject
     *         the data object
     */
    private void deleteFilesFromDefaultLocation( EntityManager entityManager, SuSEntity suSEntity, DataObjectEntity dataObject ) {
        String url = PropertiesManager.getVaultPath() + File.separator + dataObject.getFile().getFilePath();

        // locate file and delete it
        File file = new File( url );
        if ( file.exists() ) {
            file.delete();
            log.info( DELETION_POLICY + url + "Local DELTED" );

            // aditonal check only for image entity because it creates 2 copies of image tumbnail 1 copy is being
            // deleted because its save in path other copy path is not saved : exp (84734838thumb)
            if ( suSEntity instanceof DataObjectImageEntity ) {
                try {
                    File fileThumb = new File( url + "thumb" );
                    if ( fileThumb.exists() ) {
                        fileThumb.delete();
                    }
                } catch ( Exception e ) {
                    log.info( DELETION_POLICY + url + "thumb deletion error :", e );

                }
            }

            suSEntity.setAutoDelete( true );
            susDAO.saveOrUpdate( entityManager, suSEntity );
        }
    }

    /**
     * Prepare deletion listwith config.
     *
     * @param criteriaDletionList
     *         the criteria dletion list
     * @param susEntitylist
     *         the sus entitylist
     */
    private void prepareDeletionListwithConfig( List< SuSEntity > criteriaDletionList, List< SuSEntity > susEntitylist ) {
        for ( SuSEntity suSEntity : criteriaDletionList ) {
            if ( suSEntity instanceof DataObjectEntity ) {
                Map< String, ProjectConfiguration > masterConfig = configManager.getProjectConfigurationsMap();
                for ( Entry< String, ProjectConfiguration > pConfig : masterConfig.entrySet() ) {
                    if ( pConfig.getKey().equalsIgnoreCase( suSEntity.getConfig() ) ) {
                        ProjectConfiguration config = pConfig.getValue();
                        for ( SuSObjectModel suSObjectModel : config.getEntityConfig() ) {
                            if ( suSObjectModel.getId().equalsIgnoreCase( suSEntity.getTypeId().toString() )
                                    && suSObjectModel.isDeletionPolicy() && suSObjectModel.getDeletionDays() != null && getDataObjectDate(
                                    suSEntity ).before( getPerviousDate( suSObjectModel ) ) ) {
                                matchLifeCycleCriteriaAndUpdateList( susEntitylist, suSEntity, suSObjectModel );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Match life cycle criteria and update list.
     *
     * @param susEntitylist
     *         the sus entitylist
     * @param suSEntity
     *         the su s entity
     * @param suSObjectModel
     *         the su s object model
     */
    private static void matchLifeCycleCriteriaAndUpdateList( List< SuSEntity > susEntitylist, SuSEntity suSEntity,
            SuSObjectModel suSObjectModel ) {
        log.info( DELETION_POLICY + "Date criteria Matched" );
        List< String > lc = suSObjectModel.getDeletionLifeCycle();
        if ( lc != null && !lc.isEmpty() ) {
            for ( String lifecycleId : lc ) {
                if ( suSEntity.getLifeCycleStatus().equalsIgnoreCase( lifecycleId ) ) {
                    log.info( DELETION_POLICY + "lifeCycle criteria Matched : adding Obj to List" );
                    susEntitylist.add( suSEntity );
                    break;
                }
            }
        }
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
