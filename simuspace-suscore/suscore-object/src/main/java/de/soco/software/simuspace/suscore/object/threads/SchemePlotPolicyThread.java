package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.properties.DesignPlotingConfig;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.FileUtils;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.OSValidator;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.DataObjectFileEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectImageEntity;
import de.soco.software.simuspace.suscore.data.entity.DataObjectTraceEntity;
import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;

/**
 * The Class SchemePlotPolicyThread it's to delete schemes plots after some hours
 *
 * @author noman.arsahd
 */
@Log4j2
public class SchemePlotPolicyThread extends UserThread {

    /**
     * The Constant PLOT.
     */
    private static final String PLOT = "plot";

    /**
     * The Constant DELETION_POLICY.
     */
    private static final String DELETION_POLICY = "Scheme Plot Deletion Policy: ";

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * The schedule DAO.
     */
    private ScheduleDAO scheduleDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new scheme plot policy thread.
     */
    public SchemePlotPolicyThread() {
    }

    /**
     * Instantiates a new life cycle policy update tokenized license map thread.
     *
     * @param susDAO
     *         the sus DAO
     * @param scheduleDAO
     *         the schedule DAO
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public SchemePlotPolicyThread( SuSGenericObjectDAO< SuSEntity > susDAO, ScheduleDAO scheduleDAO,
            EntityManagerFactory entityManagerFactory ) {
        super();
        this.susDAO = susDAO;
        this.scheduleDAO = scheduleDAO;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {

            DesignPlotingConfig.checkInstance();
            ScheduleEntity scheduleEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, "name", PLOT );
            if ( scheduleEntity == null ) {
                createSchedulerEntityIfNotExist( entityManager,
                        ( DesignPlotingConfig.getTimeToDell() != null ? Integer.parseInt( DesignPlotingConfig.getTimeToDell() ) : 5 ) );
            } else {

                Date curentDate = new Date();
                if ( ConstantsInteger.INTEGER_VALUE_ZERO > curentDate.compareTo( scheduleEntity.getNextRun() ) ) {
                    log.info( DELETION_POLICY + curentDate + " is before  plot" + scheduleEntity.getNextRun() );
                } else if ( ConstantsInteger.INTEGER_VALUE_ZERO < curentDate.compareTo( scheduleEntity.getNextRun() ) ) {
                    log.info( DELETION_POLICY + "policy criteria matched for Plot : scheduler called" );
                    deleteSchemePlotedDataObjectPermanetly( entityManager );
                    log.info( DELETION_POLICY + "updating Next Schedule Date" );
                    // updating ScheduleEntity for next Schedule deletion
                    scheduleEntity.setLastRun( new Date() );
                    Date nextDate = getNextScheduleDate(
                            ( DesignPlotingConfig.getTimeToDell() != null ? Integer.parseInt( DesignPlotingConfig.getTimeToDell() )
                                    : ConstantsInteger.INTEGER_VALUE_FIVE ) );
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
     * Delete scheme ploted data object permanetly.
     */
    private void deleteSchemePlotedDataObjectPermanetly( EntityManager entityManager ) {
        try {
            List< SuSEntity > listPlot = susDAO.getAllRecordsWithParent( entityManager, SuSEntity.class,
                    UUID.fromString( ConstantsID.HIDDEN_PROJECT_ID_FOR_SCHEME_PLOTTING ) );

            for ( SuSEntity suSEntity : listPlot ) {
                Date objectDate = getCreatedObjectDate( suSEntity.getCreatedOn() );
                Date previousDate = getObjectDeleteTimeInHour(
                        ( DesignPlotingConfig.getTimeToDell() != null ? DesignPlotingConfig.getTimeToDell() : "5" ) );
                if ( objectDate.before( previousDate ) && ( suSEntity instanceof DataObjectTraceEntity
                        || suSEntity instanceof DataObjectImageEntity ) && ( ( DataObjectFileEntity ) suSEntity ).getFile() != null ) {

                    String url = ( ( DataObjectFileEntity ) suSEntity ).getFile().getFilePath();

                    File stagingFile = new File( PropertiesManager.getVaultPath() + url );
                    if ( stagingFile.exists() ) {
                        FileUtils.setGlobalAllFilePermissions( stagingFile );

                        if ( OSValidator.isUnix() ) {
                            String[] finalCommand2 = { "rm", "-rf", stagingFile.getPath() };
                            executeProcess( finalCommand2 );
                            // delete object
                            susDAO.remove( entityManager, suSEntity );
                            log.info( DELETION_POLICY + "Object Deleted permanently from DB" + suSEntity.getName() );
                        } else if ( OSValidator.isWindows() ) {
                            String[] finalCommand2 = { "del", stagingFile.getPath() };
                            executeProcess( finalCommand2 );
                            // delete object
                            susDAO.remove( entityManager, suSEntity );

                            log.info( DELETION_POLICY + "Object Deleted permanently from DB" + suSEntity.getName() );
                        }
                        log.info( DELETION_POLICY + " File deleted From Staging >" + stagingFile.getName() + " : object Deleted" );
                    } else {
                        log.info( DELETION_POLICY + " File Do Not Exists in Staging >" + suSEntity.getComposedId().getId() );
                        susDAO.remove( entityManager, suSEntity );
                        log.info( DELETION_POLICY + "Object Deleted permanently from DB" + suSEntity.getName() );
                    }

                }
            }
        } catch ( Exception e ) {
            log.error( DELETION_POLICY + " Error : ", e );
        }
    }

    /**
     * Creates the scheduler Entity if not exist.
     *
     * @param intervalInHours
     *         the interval in hours
     */
    private void createSchedulerEntityIfNotExist( EntityManager entityManager, int intervalInHours ) {
        // create ScheduleEntity if not existed
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId( UUID.randomUUID() );
        schedule.setLastRun( new Date() );
        schedule.setName( PLOT );

        Date nextDate = getNextScheduleDate( intervalInHours );
        schedule.setNextRun( nextDate );
        scheduleDAO.saveOrUpdate( entityManager, schedule );

        log.info( DELETION_POLICY + "LifeCycle deletion policy schedule Added in Entity" );
    }

    /**
     * Gets the created object date.
     *
     * @param date
     *         the date
     *
     * @return the created object date
     */
    private Date getCreatedObjectDate( Date date ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        return calendar.getTime();
    }

    /**
     * Gets the previous date.
     *
     * @param hours
     *         the hours
     *
     * @return the previous date
     */
    private Date getObjectDeleteTimeInHour( String hours ) {
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( myDate );
        calendar.add( Calendar.HOUR_OF_DAY, ( -Integer.parseInt( hours ) ) );
        return calendar.getTime();
    }

    /**
     * Gets the next schedule date.
     *
     * @param intervalInHours
     *         the interval in hours
     *
     * @return the next schedule date
     */
    private Date getNextScheduleDate( int intervalInHours ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date() );
        calendar.add( Calendar.HOUR_OF_DAY, +intervalInHours );
        return calendar.getTime();
    }

    /**
     * Execute process.
     *
     * @param taskList
     *         the task list
     *
     * @return true, if successful Signals that an I/O exception has occurred.
     */
    public static boolean executeProcess( String[] taskList ) {

        Thread t = new Thread( () -> LinuxUtils.runCommand( taskList, ConstantsString.COMMAND_KARAF_LOGGING_ON, null ) );
        t.start();

        return true;

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

}
