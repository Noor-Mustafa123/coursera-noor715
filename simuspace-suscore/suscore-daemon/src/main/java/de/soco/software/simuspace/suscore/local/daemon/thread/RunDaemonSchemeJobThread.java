package de.soco.software.simuspace.suscore.local.daemon.thread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.client.SuSClient;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.simflow.ElementKeys;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTemplates;
import de.soco.software.simuspace.suscore.common.enums.simflow.FieldTypes;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobRelationTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.JobTypeEnums;
import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ScanFileDTO;
import de.soco.software.simuspace.suscore.common.model.TemplateFileDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.workflow.dto.WorkflowDefinitionDTO;
import de.soco.software.simuspace.workflow.dto.WorkflowElement;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.JobParameters;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.JobParametersImpl;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.RestAPI;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;
import de.soco.software.simuspace.workflow.processing.impl.WorkflowExecutionManagerImpl;
import de.soco.software.simuspace.workflow.util.WorkflowDefinitionUtil;

/**
 * The Class RunDaemonSchemeJobThread.
 */
public class RunDaemonSchemeJobThread extends UserThread {

    /**
     * The exit variable used to stop the thread.
     */
    private volatile boolean exit = false;

    /**
     * The execution manager of workflow engine to start stop jobs.
     */
    private WFExecutionManager executionManager;

    /**
     * The job parameters.
     */
    private JobParameters jobParameters;

    private Job savedMasterJob;

    private String jobName;

    /**
     * logger for logging daemong logging information.
     */
    private static final Logger logger = Logger.getLogger( ConstantsString.DAEMON_LOGGER );

    /**
     * Instantiates a new run workflow thread.
     *
     * @param wfExecutionManager
     * @param uid
     *         the user id
     * @param jobParameters
     *         the jobParameters
     * @param workflowManager
     *         the workflowManager
     */
    public RunDaemonSchemeJobThread( WFExecutionManager executionManager, JobParameters jobParameters, Job savedMasterJob,
            String jobName ) {
        super();
        this.executionManager = executionManager;
        this.jobParameters = jobParameters;
        this.savedMasterJob = savedMasterJob;
        this.jobName = jobName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        List< Map< String, Object > > designSummary = getExecutionManager().getDesignSummary( jobParameters.getServer(),
                jobParameters.getRequestHeaders(), jobParameters.getWorkflow().getId() );

        for ( int jobCount = 1; jobCount <= designSummary.size(); jobCount++ ) {
            if ( exit ) {
                return;
            }

            jobParameters.setId( UUID.randomUUID().toString() );
            jobParameters.setName( jobName + "_" + jobCount );
            jobParameters.getWorkflow().setWorkflowType( JobTypeEnums.SCHEME.getKey() );
            jobParameters.setJobType( JobTypeEnums.SCHEME.getKey() );
            jobParameters.setJobRelationType( JobRelationTypeEnums.CHILD.getKey() );

            // saving master child job relation
            saveRelation( jobParameters, savedMasterJob );
            createObjectiveAndDesignVaribaleFileInGivenPath( jobParameters, designSummary.get( jobCount - 1 ) );
            JobParameters rePreparedJobParameters = rePrepareJobParameters( jobParameters );
            WFExecutionManager workflowExecutionManager = new WorkflowExecutionManagerImpl();
            workflowExecutionManager.executeWorkflow( rePreparedJobParameters );
        }
    }

    /**
     * Sets the exit boolean to true so the thread can be stopped.
     */
    public void stop() {
        exit = true;
    }

    /**
     * Creates the objective varibale file in staging.
     *
     * @param jobParameters
     *         the job parameters
     * @param designSummary
     *         the design summary
     */
    private void createObjectiveAndDesignVaribaleFileInGivenPath( JobParameters jobParameters, Map< String, Object > designSummary ) {
        if ( designSummary != null ) {

            String basePath = jobParameters.getWorkingDir().getItems().get( 0 ).toString() + File.separator + jobParameters.getName();
            File file1 = new File( basePath + File.separator + jobParameters.getId() );
            createFileWithFileObject( file1 );

            List< ScanFileDTO > scanList = new ArrayList<>();
            List< TemplateFileDTO > templateList = new ArrayList<>();
            // Getting workflow elements from job parameter
            final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinition( jobParameters.getWorkflow().prepareDefinition() );
            final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );

            // get paths of all local files used in workflow
            for ( UserWFElement element : jobWFElements ) {
                final List< Field< ? > > elementFields = element.getFields();
                for ( final Field< ? > field : elementFields ) {
                    if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() )
                            && field.getTemplateType().equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                        scanList.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class ) );
                    }
                    if ( field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() )
                            && field.getTemplateType().equals( FieldTemplates.DESIGN_VARIABLE.getValue() ) ) {
                        templateList.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), TemplateFileDTO.class ) );
                    }
                }
            }

            try {
                if ( !scanList.isEmpty() ) {
                    for ( ScanFileDTO scanFileDTO : scanList ) {
                        File temp = new File( scanFileDTO.getFile() );
                        File fileObjectiveVariables = new File(
                                basePath + File.separator + jobParameters.getId() + File.separator + temp.getName() );
                        fileObjectiveVariables.createNewFile();

                        List< ScanFileDTO > scanFileList = new ArrayList< ScanFileDTO >();
                        scanFileList.add( scanFileDTO );
                        WorkflowDefinitionUtil.prepareRegexFileInStagingInJobContainer( scanFileList, designSummary,
                                fileObjectiveVariables.getPath() );
                    }
                } else {
                    for ( TemplateFileDTO templateFile : templateList ) {
                        File temp = new File( templateFile.getFile() );
                        File fileObjectiveVariables = new File(
                                basePath + File.separator + jobParameters.getId() + File.separator + temp.getName() );
                        fileObjectiveVariables.createNewFile();

                        List< TemplateFileDTO > templateFileList = new ArrayList< TemplateFileDTO >();
                        templateFileList.add( templateFile );
                        WorkflowDefinitionUtil.prepareTemplateFileInStagingInJobContainer( templateFileList, designSummary,
                                fileObjectiveVariables.getPath() );
                    }
                }

                createObjectiveVariableFileInJobContainer( jobParameters );

            } catch ( IOException e ) {
                logger.debug( "Design Variable file Write Fail :" + e );
            }
        }
    }

    /**
     * Sets the workflow elements.
     *
     * @param def
     *         the def
     *
     * @return the list
     */
    public static List< UserWFElement > setWorkflowElements( WorkflowDefinitionDTO def ) {
        final List< UserWFElement > userElements = new ArrayList<>();
        for ( final WorkflowElement workflowElement : def.getElements().getNodes() ) {
            final UserWFElement element = workflowElement.getData();
            if ( ElementKeys.getkeys().contains( element.getKey() ) ) {
                userElements.add( element );
            } else {
                final List< NodeEdge > edges = def.getElements().getEdges();
                final Iterator< NodeEdge > edgeList = edges.iterator();
                while ( edgeList.hasNext() ) {
                    final NodeEdge ed = edgeList.next();
                    if ( ed.getData().getSource().equals( element.getId() ) || ed.getData().getTarget().equals( element.getId() ) ) {
                        edgeList.remove();
                    }
                }
            }
        }
        return userElements;
    }

    /**
     * Gets the workflow definition.
     *
     * @param workflowDefinitionMap
     *         the workflow definition map
     *
     * @return the workflow definition
     */
    public static WorkflowDefinitionDTO getWorkflowDefinition( Map< String, Object > workflowDefinitionMap ) {
        String json = null;

        if ( workflowDefinitionMap != null ) {
            logger.debug( ">>getWorkflowDefinition workflowDefinitionMap " + workflowDefinitionMap );
            json = JsonUtils.toJson( workflowDefinitionMap );
        }
        return JsonUtils.jsonToObject( json, WorkflowDefinitionDTO.class );
    }

    /**
     * Creates the objective variable in job container.
     *
     * @param jobParameters
     *         the jobParameters
     */
    private void createObjectiveVariableFileInJobContainer( JobParameters jobParameters ) {

        List< ScanFileDTO > scanlist = new ArrayList<>();
        List< TemplateFileDTO > templatelist = new ArrayList<>();
        // Getting workflow elements from job parameter
        final WorkflowDefinitionDTO jobParamDef = getWorkflowDefinition( jobParameters.getWorkflow().prepareDefinition() );
        final List< UserWFElement > jobWFElements = setWorkflowElements( jobParamDef );

        // get paths of all local files used in workflow
        for ( UserWFElement element : jobWFElements ) {
            final List< Field< ? > > elementFields = element.getFields();
            for ( final Field< ? > field : elementFields ) {
                if ( field.getType().equals( FieldTypes.REGEX_FILE.getType() )
                        && field.getTemplateType().equals( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {
                    scanlist.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), ScanFileDTO.class ) );
                }
                if ( field.getType().equals( FieldTypes.TEMPLATE_FILE.getType() )
                        && field.getTemplateType().equals( FieldTemplates.OBJECTIVE_VARIABLE.getValue() ) ) {
                    templatelist.add( JsonUtils.jsonToObject( JsonUtils.toJson( field.getValue() ), TemplateFileDTO.class ) );
                }
            }
        }
        if ( !scanlist.isEmpty() ) {
            String basePath = jobParameters.getWorkingDir().getItems().get( 0 ) + File.separator + jobParameters.getName();
            File temp = new File( scanlist.get( 0 ).getFile() );
            String targetPath = basePath + File.separator + jobParameters.getId() + File.separator + temp.getName();
            try {
                Files.copy( Paths.get( scanlist.get( 0 ).getFile() ), Paths.get( targetPath ), StandardCopyOption.REPLACE_EXISTING );
            } catch ( IOException e ) {
                logger.info( "objectiveVariable file write failed" );
            }
        } else {
            String basePath = jobParameters.getWorkingDir().getItems().get( 0 ) + File.separator + jobParameters.getName();
            File temp = new File( templatelist.get( 0 ).getFile() );
            String targetPath = basePath + File.separator + jobParameters.getId() + File.separator + temp.getName();
            try {
                Files.copy( Paths.get( templatelist.get( 0 ).getFile() ), Paths.get( targetPath ), StandardCopyOption.REPLACE_EXISTING );
            } catch ( IOException e ) {
                logger.info( "objectiveVariable file write failed" );
            }
        }
    }

    /**
     * Creates the file with file object.
     *
     * @param file
     *         the file
     */
    private void createFileWithFileObject( File file ) {
        if ( !file.exists() ) {
            file.mkdirs();
        }
    }

    /**
     * Save relation.
     *
     * @param jobParameters
     *         the job parameters
     * @param savedMasterJob
     *         the saved master job
     */
    private void saveRelation( JobParameters jobParameters, Job savedMasterJob ) {
        Relation relation = new Relation( savedMasterJob.getId(), UUID.fromString( jobParameters.getId() ) );
        final String realtionURL = prepareURL( "/api/workflow/create/relation", jobParameters.getServer() );
        SuSClient.postRequest( realtionURL, JsonUtils.objectToJson( relation ),
                WorkflowDefinitionUtil.prepareHeaders( jobParameters.getRequestHeaders() ) );
    }

    /**
     * It prepares url by getting protocol : hostname and port from server appended with url provided.
     *
     * @param url
     *         , url of API
     * @param server
     *         , server
     *
     * @return requestHeaders
     */
    private String prepareURL( String url, RestAPI server ) {
        if ( server != null ) {
            return server.getProtocol() + server.getHostname() + ConstantsString.COLON + server.getPort() + url;
        } else {
            throw new SusException( new Exception( MessagesUtil.getMessage( WFEMessages.CONFIG_NOT_PROVIDED ) ) );
        }
    }

    /**
     * Re prepare job parameters.
     *
     * @param jobParameters
     *         the job parameters
     *
     * @return the job parameters
     */
    private JobParameters rePrepareJobParameters( JobParameters jobParameters ) {
        JobParameters rePreparedJobParameters = new JobParametersImpl();
        rePreparedJobParameters.setComments( jobParameters.getComments() );
        rePreparedJobParameters.setDescription( jobParameters.getDescription() );
        rePreparedJobParameters.setGlobalVariables( jobParameters.getGlobalVariables() );
        rePreparedJobParameters.setId( jobParameters.getId() );
        rePreparedJobParameters.setName( jobParameters.getName() );
        rePreparedJobParameters.setRequestHeaders( jobParameters.getRequestHeaders() );
        rePreparedJobParameters.setRunsOn( jobParameters.getRunsOn() );
        rePreparedJobParameters.setServer( jobParameters.getServer() );
        rePreparedJobParameters.setWorkflow( jobParameters.getWorkflow() );
        rePreparedJobParameters.setWorkingDir( jobParameters.getWorkingDir() );
        rePreparedJobParameters.setJobRelationType( jobParameters.getJobRelationType() );
        return rePreparedJobParameters;
    }

    /**
     * Gets the execution manager.
     *
     * @return the execution manager
     */
    public WFExecutionManager getExecutionManager() {
        return executionManager;
    }

    /**
     * Sets the execution manager.
     *
     * @param executionManager
     *         the new execution manager
     */
    public void setExecutionManager( WFExecutionManager executionManager ) {
        this.executionManager = executionManager;
    }

    /**
     * Gets the job parameters.
     *
     * @return the job parameters
     */
    public JobParameters getJobParameters() {
        return jobParameters;
    }

}
