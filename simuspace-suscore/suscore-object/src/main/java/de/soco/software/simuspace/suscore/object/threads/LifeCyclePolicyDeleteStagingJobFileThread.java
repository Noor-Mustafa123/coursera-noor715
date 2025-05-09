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
import java.util.Set;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.data.entity.ScheduleEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.object.dao.ScheduleDAO;
import de.soco.software.suscore.jsonschema.model.LifeCyclePolicyDTO;
import de.soco.software.suscore.jsonschema.model.PolicyProcess;

/**
 * The Class LifeCyclePolicyDeleteStagingFileThread.
 *
 * @author noman arshad
 */
@Log4j2
public class LifeCyclePolicyDeleteStagingJobFileThread extends UserThread {

    /**
     * The Constant DELETION_POLICY.
     */
    private static final String DELETION_POLICY = "Staging Deletion Policy: ";

    /**
     * The Constant PROCESS.
     */
    private static final String PROCESS = "process";

    /**
     * The Constant STAGING.
     */
    private static final String STAGING = "staging";

    /**
     * The schedule DAO.
     */
    private ScheduleDAO scheduleDAO;

    /**
     * The life cycle config.
     */
    private List< LifeCyclePolicyDTO > lifeCycleConfig;

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new delete object thread.
     */
    public LifeCyclePolicyDeleteStagingJobFileThread() {
    }

    /**
     * Instantiates a new life cycle policy delete file thread.
     *
     * @param scheduleDAO
     *         the schedule DAO
     * @param lifeCycleConfig
     *         the life cycle config
     * @param jobManager
     *         the job manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public LifeCyclePolicyDeleteStagingJobFileThread( ScheduleDAO scheduleDAO, List< LifeCyclePolicyDTO > lifeCycleConfig,
            JobManager jobManager, EntityManagerFactory entityManagerFactory ) {
        super();
        this.scheduleDAO = scheduleDAO;
        this.lifeCycleConfig = lifeCycleConfig;
        this.jobManager = jobManager;
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
            log.info( DELETION_POLICY + "Checking for LifeCycle deletion policy schedule" );
            List< PolicyProcess > processList = getDeletionProcessList();
            List< PolicyProcess > preparedProcessList = new ArrayList<>();
            Map< String, Object > properties = new HashMap<>();
            properties.put( "name", STAGING );
            List< ScheduleEntity > entityList = scheduleDAO.getListByProperties( entityManager, properties, ScheduleEntity.class, true );
            if ( entityList == null || entityList.isEmpty() ) {
                createSchedulerEntityIfNotExists( entityManager, processList );
            } else {
                prepareProcessListByScheduleEntityTime( entityManager, processList, preparedProcessList, entityList );
                try {
                    if ( !preparedProcessList.isEmpty() ) {
                        executeStagingFileDeletionPolicy( entityManager, preparedProcessList );
                    }
                } catch ( Exception e ) {
                    log.error( DELETION_POLICY + " Error  Staging: ", e );
                }
            }
        } catch ( Exception e ) {
            log.error( DELETION_POLICY + " Error : ", e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare process list by schedual entity time.
     *
     * @param entityManager
     *         the entity manager
     * @param processList
     *         the process list
     * @param preparedProcessList
     *         the prepared process list
     * @param entityList
     *         the entity list
     */
    private void prepareProcessListByScheduleEntityTime( EntityManager entityManager, List< PolicyProcess > processList,
            List< PolicyProcess > preparedProcessList, List< ScheduleEntity > entityList ) {
        for ( ScheduleEntity scheduleEntity : entityList ) {
            if ( scheduleEntity.getName().equalsIgnoreCase( STAGING ) ) {
                for ( PolicyProcess policyProcess : processList ) {
                    if ( policyProcess.getProcessName().equalsIgnoreCase( scheduleEntity.getProcess() ) ) {
                        Date curentDate = new Date();
                        Date shouldRunDate = getCreatedObjectdate( scheduleEntity.getNextRun() );
                        if ( shouldRunDate.before( curentDate ) ) {
                            preparedProcessList.add( policyProcess );
                            // update schedule entry with new Date Time
                            scheduleEntity.setLastRun( new Date() );
                            Date nextDate = getNextScheduleDate( Integer.parseInt( policyProcess.getProcessTimeHours() ) );
                            scheduleEntity.setNextRun( nextDate );
                            scheduleDAO.saveOrUpdate( entityManager, scheduleEntity );
                        }
                    }
                }
            }
        }
    }

    /**
     * Execute staging file deletion policy.
     *
     * @param entityManager
     *         the entity manager
     * @param processList
     *         the process list
     */
    private void executeStagingFileDeletionPolicy( EntityManager entityManager, List< PolicyProcess > processList ) {
        Set< String > allDeletionDays = new LinkedHashSet<>();
        Set< String > allDeletionStatus = new LinkedHashSet<>();
        getDeletionDaysAndJobStatus( allDeletionDays, allDeletionStatus );
        log.info( DELETION_POLICY + allDeletionDays + "Days " + " Job status to be deleted " + allDeletionStatus );
        int min = allDeletionDays.stream().mapToInt( Integer::parseInt ).min().getAsInt();
        allDeletionDays.clear();
        allDeletionDays.add( String.valueOf( min ) );
        Date minDateProperty = prepareCreatedOnProperties( allDeletionDays );
        log.debug( DELETION_POLICY + "minimum date : " + minDateProperty );
        log.debug( DELETION_POLICY + "minimum date Not applied " );
        List< Object > lifeCycleProperties = new ArrayList<>();
        prepareJobStatusProperties( allDeletionStatus, lifeCycleProperties );
        List< JobEntity > criteriaDletionList = jobManager.getJobDao()
                .getAllJobsWithCreatedOnAndProperties( entityManager, JobEntity.class, minDateProperty, ConstantsDAO.STATUS,
                        lifeCycleProperties );
        log.info( DELETION_POLICY + " jobs found : " + criteriaDletionList.size() );
        List< JobEntity > preparedJobEntity = new ArrayList<>();
        prepareJobEntityListByMatchingPolicyCriteria( processList, criteriaDletionList, preparedJobEntity );
        log.info( DELETION_POLICY + " jobs processed to delete : " + preparedJobEntity.size() );
        if ( !preparedJobEntity.isEmpty() ) {
            for ( JobEntity jobEntity : preparedJobEntity ) {
                try {
                    Integer id = jobManager.getJobIdByUUID( entityManager, jobEntity.getId() );
                    final String bmwSuggestion = File.separator + jobEntity.getName() + "_" + id;
                    if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.WORKFLOW.getKey() ) ) ) {
                        deleteWorkflowFiles( jobEntity, bmwSuggestion );
                    } else if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.SCHEME.getKey() ) ) ) {
                        deleteSchemeFiles( entityManager, jobEntity );
                    } else if ( jobEntity.getJobType().toString().equals( String.valueOf( JobTypeEnums.VARIANT.getKey() ) ) ) {
                        runVariantClearStagingRecursively( entityManager, jobEntity );
                    }
                    jobEntity.setStatus( WorkflowStatus.DISCARD.getKey() );
                    // mark job as auto deleted (means staging is clear for marked jobs)
                    jobEntity.setAutoDelete( true );
                    jobManager.getJobDao().update( entityManager, jobEntity );
                } catch ( Exception e ) {
                    log.error( jobEntity.getName() + " : job stagging area clean : failed :", e );
                }
            }
        }
    }

    /**
     * Delete scheme files.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     */
    private void deleteSchemeFiles( EntityManager entityManager, JobEntity jobEntity ) {
        try {
            // deleting wf dir where all the wf files are being uplaoded
            String stagingBase = PropertiesManager.getUserStagingPath( jobEntity.getCreatedBy().getUserUid() );
            String dirOrFilePathWF = stagingBase + File.separator + jobEntity.getWorkflowId().toString();
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePathWF );
            // getting all childs of master job to delete Dir from staging
            List< JobEntity > childJobs = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, jobEntity.getId() );

            if ( childJobs != null && !childJobs.isEmpty() ) {
                childJobs.stream().filter( child -> child != null && child.getName() != null && child.getCreatedBy() != null
                                && child.getCreatedBy().getUserUid() != null )
                        .forEach( child -> deleteChildJobFromStaging( entityManager, child, stagingBase, jobEntity.getName() ) );
            }
            log.info( DELETION_POLICY + "Scheme jobs deleted : " + jobEntity.getName() );
        } catch ( Exception e ) {
            log.error( jobEntity.getName() + "Scheme job stagging area clean : failed :", e );
        }
    }

    /**
     * Delete workflow files.
     *
     * @param jobEntity
     *         the job entity
     * @param bmwSuggestion
     *         the bmw suggestion
     */
    private static void deleteWorkflowFiles( JobEntity jobEntity, String bmwSuggestion ) {
        try {
            // deleting child job directory
            String staggingBase = PropertiesManager.getUserStagingPath( jobEntity.getCreatedBy().getUserUid() );
            String dirOrFilePath = staggingBase + File.separator + bmwSuggestion;
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePath );
            // deleting wf dir where all the wf files are being uplaoded
            String dirOrFilePathWF = staggingBase + File.separator + jobEntity.getWorkflowId().toString();
            LinuxUtils.deleteFileOrDirByPath( jobEntity.getCreatedBy().getUserUid(), dirOrFilePathWF );
            log.info( DELETION_POLICY + "WF jobs deleted : " + jobEntity.getName() );
        } catch ( Exception e ) {
            log.error( jobEntity.getName() + "Workflow job staging area clean : failed :", e );
        }
    }

    /**
     * Delete files for child job in Staging
     *
     * @param child
     *         the child entity
     * @param stagingBase
     *         Base path of staging
     * @param parentName
     *         Name of parent job
     */
    private void deleteChildJobFromStaging( EntityManager entityManager, JobEntity child, String stagingBase, String parentName ) {
        try {
            Integer childId = jobManager.getJobIdByUUID( entityManager, child.getId() );
            String userId = child.getCreatedBy().getUserUid();
            final String bmwSuggestionChild = File.separator + parentName + "_" + childId;
            String dirOrFilePath = stagingBase + File.separator + bmwSuggestionChild;
            LinuxUtils.deleteFileOrDirByPath( userId, dirOrFilePath );
            String normalChild = File.separator + child.getName() + "_" + childId;
            String normalDirOrFilePath = stagingBase + File.separator + normalChild;
            LinuxUtils.deleteFileOrDirByPath( userId, normalDirOrFilePath );
        } catch ( Exception e ) {
            log.error( child.getName() + " child of " + parentName + " staging area clean : failed :", e );
        }
    }

    /**
     * Run varient clear stagging recursively.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     */
    private void runVariantClearStagingRecursively( EntityManager entityManager, JobEntity jobEntity ) {
        try {
            List< JobEntity > innerEntityList = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, jobEntity.getId() );
            if ( innerEntityList != null && !innerEntityList.isEmpty() ) {
                JobEntity childEntity = innerEntityList.get( 0 );
                String staggingBase = PropertiesManager.getUserStagingPath( childEntity.getCreatedBy().getUserUid() );
                Integer childId = jobManager.getJobIdByUUID( entityManager, childEntity.getId() );
                final String bmwSuggestionChild = File.separator + jobEntity.getName() + "_" + childId;
                String dirOrFilePath = staggingBase + File.separator + bmwSuggestionChild;
                LinuxUtils.deleteFileOrDirByPath( childEntity.getCreatedBy().getUserUid(), dirOrFilePath );
                log.info( DELETION_POLICY + "Dummy jobs deleted : " + jobEntity.getName() );
                runVariantClearStagingRecursively( entityManager, childEntity );
            }
        } catch ( Exception e ) {
            log.error( jobEntity.getName() + "VARIANT job stagging area clean : failed :", e );
        }
    }

    /**
     * Prepare job entity list by matching policy criteria.
     *
     * @param processList
     *         the process list
     * @param criteriaDletionList
     *         the criteria dletion list
     * @param preparedJobEntity
     *         the prepared job entity
     */

    private void prepareJobEntityListByMatchingPolicyCriteria( List< PolicyProcess > processList, List< JobEntity > criteriaDletionList,
            List< JobEntity > preparedJobEntity ) {
        for ( JobEntity jobEntity : criteriaDletionList ) {
            PolicyProcess deletionProcess = isFileStatusExistsInConfig( processList, jobEntity );
            if ( deletionProcess != null ) {
                Date dateBeforeDeletionTime = getDateBeforeDeletionTime( deletionProcess );
                Date jobCreationDate = getCreatedObjectdate( jobEntity.getCreatedOn() );
                if ( jobCreationDate.before( dateBeforeDeletionTime ) ) {
                    preparedJobEntity.add( jobEntity );
                }
            }
        }
    }

    /**
     * Checks if is file status exists in config.
     *
     * @param processList
     *         the process list
     * @param jobEntity
     *         the job entity
     *
     * @return the deletion process
     */
    private PolicyProcess isFileStatusExistsInConfig( List< PolicyProcess > processList, JobEntity jobEntity ) {
        for ( PolicyProcess config : processList ) {
            if ( config.getProcessName().equalsIgnoreCase( WorkflowStatus.getById( jobEntity.getStatus() ).getValue() ) ) {
                return config;
            }
        }
        return null;
    }

    /**
     * Gets the created objectdate.
     *
     * @param date
     *         the date
     *
     * @return the created objectdate
     */
    private Date getCreatedObjectdate( Date date ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        return calendar.getTime();
    }

    /**
     * Gets the previous date.
     *
     * @param config
     *         the config
     *
     * @return the previous date
     */
    private Date getDateBeforeDeletionTime( PolicyProcess config ) {
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( myDate );
        calendar.add( Calendar.HOUR_OF_DAY, ( -Integer.parseInt( config.getProcessTimeHours() ) ) );
        return calendar.getTime();
    }

    /**
     * Gets the deletion days and job status.
     *
     * @param allDeletionDays
     *         the all deletion days
     * @param allDeletionLifeCycles
     *         the all deletion life cycles
     */
    private void getDeletionDaysAndJobStatus( Set< String > allDeletionDays, Set< String > allDeletionLifeCycles ) {
        lifeCycleConfig.stream().filter( lifeCyclePolicyDTO -> lifeCyclePolicyDTO.getLifeCyclePolicyApply().equalsIgnoreCase( STAGING ) )
                .flatMap( lifeCyclePolicyDTO -> lifeCyclePolicyDTO.getPolicyProcess().stream() )
                .filter( policyProcess -> policyProcess.getProcessTimeHours() != null && policyProcess.getProcessName() != null )
                .forEach( policyProcess -> {
                    allDeletionDays.add( policyProcess.getProcessTimeHours() );
                    allDeletionLifeCycles.add( policyProcess.getProcessName() );
                } );
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
     * Prepare job status properties.
     *
     * @param allDeletionLifeCycles
     *         the all deletion life cycles
     * @param lifeCycleProperties
     *         the life cycle properties
     */
    private void prepareJobStatusProperties( Set< String > allDeletionLifeCycles, List< Object > lifeCycleProperties ) {
        for ( String lifeCycleId : allDeletionLifeCycles ) {
            lifeCycleProperties.add( WorkflowStatus.getByValue( lifeCycleId ).getKey() );
        }
    }

    /**
     * Creates the scheduler entity if not exists.
     *
     * @param entityManager
     *         the entity manager
     * @param processList
     *         the process list
     */
    private void createSchedulerEntityIfNotExists( EntityManager entityManager, List< PolicyProcess > processList ) {
        for ( PolicyProcess scheduleEntity : processList ) {
            ScheduleEntity sEntity = scheduleDAO.getUniqueObjectByProperty( entityManager, ScheduleEntity.class, PROCESS,
                    scheduleEntity.getProcessName() );
            if ( sEntity == null ) {
                // create ScheduleEntity for all process : if not exists
                ScheduleEntity schedule = new ScheduleEntity();
                schedule.setId( UUID.randomUUID() );
                schedule.setLastRun( new Date() );
                schedule.setName( STAGING );
                schedule.setProcess( scheduleEntity.getProcessName() );
                Date nextDate = getNextScheduleDate( Integer.parseInt( scheduleEntity.getProcessTimeHours() ) );
                schedule.setNextRun( nextDate );
                scheduleDAO.saveOrUpdate( entityManager, schedule );
            } else {
                sEntity.setLastRun( new Date() );
                sEntity.setName( STAGING );
                sEntity.setProcess( scheduleEntity.getProcessName() );
                Date nextDate = getNextScheduleDate( Integer.parseInt( scheduleEntity.getProcessTimeHours() ) );
                sEntity.setNextRun( nextDate );
                scheduleDAO.saveOrUpdate( entityManager, sEntity );
            }
        }
        log.info( DELETION_POLICY + "LifeCycle deletion policy schedule Added in Entity" );
    }

    /**
     * Gets the deletion process list.
     *
     * @return the deletion process list
     */
    private List< PolicyProcess > getDeletionProcessList() {
        for ( LifeCyclePolicyDTO lifeCyclePolicyDTO : lifeCycleConfig ) {
            if ( lifeCyclePolicyDTO.getLifeCyclePolicyApply().equalsIgnoreCase( STAGING ) ) {
                return lifeCyclePolicyDTO.getPolicyProcess();
            }
        }
        return new ArrayList<>();
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

}
