package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.server.manager.JobManager;
import de.soco.software.simuspace.suscore.authentication.manager.AuthManager;
import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.model.JobScheme;
import de.soco.software.simuspace.suscore.common.model.UserTokenDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.model.impl.ProgressBar;

/**
 * Schedular for status updation of master jobs, if all childs are completed.
 *
 * @author Ali Haider
 */
@Log4j2
public class JobStatusUpdationThread extends UserThread {

    /**
     * The job manager.
     */
    private JobManager jobManager;

    /**
     * SuSGenericDAO of susEntity Type reference.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    private AuthManager authManager;

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    private EntityManagerFactory entityManagerFactory;

    /**
     * The Constant POST_WORKFLOW.
     */
    public static final String POST_WORKFLOW = ">>Post Workflow: ";

    /**
     * Instantiates a new life cycle policy CB 2 session keep alive thread.
     */
    public JobStatusUpdationThread() {
    }

    /**
     * Instantiates a new job status updation thread.
     *
     * @param jobManager
     *         the job manager
     * @param susDAO
     *         the sus DAO
     */
    public JobStatusUpdationThread( JobManager jobManager, SuSGenericObjectDAO< SuSEntity > susDAO, AuthManager authManager,
            UserTokenManager tokenManager, EntityManagerFactory entityManagerFactory ) {
        super();
        this.jobManager = jobManager;
        this.susDAO = susDAO;
        this.authManager = authManager;
        this.tokenManager = tokenManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        log.info( "Trying to update status for parent job" );
        try {
            List< Job > jobsList = jobManager.getAllDOEMasterRunningJobsList( entityManager );
            log.info( "Found " + ( jobsList == null ? 0 : jobsList.size() ) + " jobs for status update" );
            for ( Job job : jobsList ) {
                try {
                    Job jobProcessed = prepareMasterJobForScheme( entityManager, job );
                    jobManager.updateJob( entityManager, jobProcessed );

                    runPostProcessJobIfExisitsInScheme( entityManager, jobProcessed );

                } catch ( Exception e ) {
                    authManager.expireJobToken( entityManager, job.getRequestHeaders().getJobAuthToken() );
                    log.error( e.getMessage(), e );
                }
            }
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run post process job if exisits in scheme.
     *
     * @param jobImpl
     *         the job
     */
    private void runPostProcessJobIfExisitsInScheme( EntityManager entityManager, Job jobImpl ) {
        try {

            if ( jobImpl.getStatus().getName().equals( WorkflowStatus.COMPLETED.getValue() ) && jobImpl.getPostprocess() != null
                    && jobImpl.getPostprocess().getPostProcessWorkflow() != null
                    && jobImpl.getPostprocess().getPostProcessWorkflow().getElements() != null ) {
                log.info( POST_WORKFLOW + "Starting  Preprations" );
                JobScheme jobScheme = jobManager.prepareJobSchemeAsPostJson( entityManager, jobImpl );
                log.info( POST_WORKFLOW + "JobScheme prepared" );

                String auth = getActiveTokenById( entityManager, jobImpl.getCreatedBy().getId() );
                JobParameters jobParameters = JsonUtils.jsonToObject( jobImpl.getPostprocessParametersJson(), JobParameters.class );
                jobParameters.setResultSchemeAsJson( jobScheme );
                jobParameters.getWorkflow().setDummyMasterJobId( jobImpl.getId().toString() );
                jobParameters.setJobRelationType( JobRelationTypeEnums.MASTER.getKey() );
                jobParameters.setJobType( JobTypeEnums.WORKFLOW.getKey() );
                jobParameters.setPostprocess( jobImpl.getPostprocess() );
                jobParameters.setName( jobParameters.getWorkflow().getName() );
                jobParameters.setServer( jobImpl.getServer() );
                jobParameters.setRequestHeaders( jobImpl.getRequestHeaders() );
                jobParameters.getRequestHeaders().setToken( auth );
                jobParameters.getRequestHeaders().setJobAuthToken( null );

                log.info( POST_WORKFLOW + "Submitting Job" );
                String urlRun = PropertiesManager.getLocationURL() + "/api/workflow/runjob";
                SusResponseDTO susResponseRunJob = SuSClient.postRequest( urlRun, JsonUtils.toJson( jobParameters ),
                        CommonUtils.prepareHeadersWithAuthToken( auth ) );
                log.info( POST_WORKFLOW + "job submitted" + susResponseRunJob.getData() );
            }

        } catch ( Exception e ) {
            log.error( POST_WORKFLOW + "job submission failed", e );
        }
    }

    /**
     * Prepare master job for scheme.
     *
     * @param entityManager
     *         the entity manager
     * @param job
     *         the job
     *
     * @return the job
     */
    private Job prepareMasterJobForScheme( EntityManager entityManager, Job job ) {
        if ( job != null ) {
            boolean createChild = true;
            List< JobEntity > allChildren = getAllChildrensOfMasterJob( entityManager, job );
            // Set progress and status of master job
            long completedChildren = 0;
            long totalChildren;
            long failedChildren = 0;
            Status otherStatus = null;

            if ( job.getChildJobsCount() != null ) {
                totalChildren = job.getChildJobsCount();
            } else {
                totalChildren = allChildren.size();
            }

            for ( final JobEntity jobEntity : allChildren ) {
                if ( Boolean.TRUE != jobEntity.isCreateChild() ) {
                    createChild = false;
                }
                if ( WorkflowStatus.COMPLETED.equals( WorkflowStatus.getById( jobEntity.getStatus() ) ) ) {
                    completedChildren += 1;
                }
                if ( WorkflowStatus.FAILED.equals( WorkflowStatus.getById( jobEntity.getStatus() ) ) ) {
                    failedChildren += 1;
                } else if ( !WorkflowStatus.COMPLETED.equals( WorkflowStatus.getById( jobEntity.getStatus() ) ) ) {
                    otherStatus = new Status( WorkflowStatus.getById( jobEntity.getStatus() ) );
                }
            }
            final ProgressBar progres = new ProgressBar();
            progres.setTotal( totalChildren );
            progres.setCompleted( completedChildren );
            job.setProgress( progres );
            if ( otherStatus != null ) {
                job.setStatus( otherStatus );
            } else if ( Long.valueOf( completedChildren ).equals( totalChildren ) && ( !Long.valueOf( completedChildren ).equals( 0L ) )
                    && !createChild ) {
                List< LogRecord > jobLog = job.getLog();
                job.setCompletionTime( new Date() );
                jobLog.add( new LogRecord( ConstantsMessageTypes.INFO,
                        MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( job ) ), new Date() ) );
                job.setStatus( new Status( WorkflowStatus.COMPLETED ) );
            } else {
                double completedPercentage = ( ( double ) completedChildren / ( double ) totalChildren ) * 100;
                double failedPercentage = ( ( double ) failedChildren / ( double ) totalChildren ) * 100;
                List< LogRecord > jobLog = job.getLog();
                job.setCompletionTime( new Date() );
                jobLog.add( new LogRecord( ConstantsMessageTypes.INFO,
                        MessagesUtil.getMessage( WFEMessages.TOTAL_EXECUTION_TIME, getExecutionTime( job ) ), new Date() ) );
                if ( completedPercentage >= 90.0 ) {
                    job.setStatus( new Status( WorkflowStatus.COMPLETED ) );
                } else if ( failedPercentage >= 10.00 ) {
                    job.setStatus( new Status( WorkflowStatus.FAILED ) );
                }
            }
            if ( null != job.getRequestHeaders().getJobAuthToken() && ( WorkflowStatus.FAILED.getKey() == job.getStatus().getId()
                    || WorkflowStatus.COMPLETED.getKey() == job.getStatus().getId() ) ) {
                authManager.expireJobToken( entityManager, job.getRequestHeaders().getJobAuthToken() );
            }
        }
        return job;
    }

    /**
     * Gets execution time.
     *
     * @param jobImpl
     *         the job
     *
     * @return the execution time
     */
    private String getExecutionTime( Job jobImpl ) {
        long timeDifference = jobImpl.getCompletionTime().getTime() - jobImpl.getSubmitTime().getTime();
        long sec = ( timeDifference / 1000 ) % 60;
        long min = ( timeDifference / ( 1000 * 60 ) ) % 60;
        long hour = ( timeDifference / ( 1000 * 60 * 60 ) ) % 24;
        return hour + "h:" + min + "m:" + sec + "s";
    }

    /**
     * Gets the all childrens of master job.
     *
     * @param masterJob
     *         the master job
     *
     * @return the all childrens of master job
     */
    private List< JobEntity > getAllChildrensOfMasterJob( EntityManager entityManager, Job masterJob ) {
        List< JobEntity > allChildren = new ArrayList<>();
        if ( JobTypeEnums.SCHEME.getKey() != masterJob.getJobType() && JobTypeEnums.WORKFLOW.getKey() != masterJob.getJobType() ) {

            List< JobEntity > assemblyList = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, masterJob.getId() );
            List< JobEntity > solveList = new ArrayList<>();
            List< JobEntity > postList = new ArrayList<>();
            assemblyList.stream().forEach( assembly -> {
                List< JobEntity > solve = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, assembly.getId() );
                solveList.addAll( solve );
            } );
            solveList.stream().forEach( solve -> {
                List< JobEntity > post = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, solve.getId() );
                postList.addAll( post );
            } );
            allChildren.addAll( assemblyList );
            allChildren.addAll( solveList );
            allChildren.addAll( postList );
        } else {
            List< JobEntity > assemblyList = jobManager.getJobDao().getAllChildrenOfMasterJob( entityManager, masterJob.getId() );
            allChildren.addAll( assemblyList );
        }
        return allChildren;
    }

    /**
     * Gets active token by id.
     *
     * @param userId
     *         the user id
     *
     * @return the active token by id
     */
    private String getActiveTokenById( EntityManager entityManager, String userId ) {
        List< UserTokenDTO > userToken = tokenManager.getUserActiveTokenList( entityManager, userId );
        if ( userToken != null && !userToken.isEmpty() ) {
            return userToken.get( 0 ).getToken();
        }
        return null;
    }

    /**
     * Gets the job manager.
     *
     * @return the job manager
     */
    public JobManager getJobManager() {
        return jobManager;
    }

    /**
     * Sets the job manager.
     *
     * @param jobManager
     *         the new job manager
     */
    public void setJobManager( JobManager jobManager ) {
        this.jobManager = jobManager;
    }

    /**
     * Gets token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets token manager.
     *
     * @param tokenManager
     *         the token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

}