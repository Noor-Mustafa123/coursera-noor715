package de.soco.software.simuspace.server.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.simflow.WorkflowStatus;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.LinuxUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.workflow.constant.ConstantsMessageTypes;
import de.soco.software.simuspace.workflow.dto.Status;
import de.soco.software.simuspace.workflow.model.Job;
import de.soco.software.simuspace.workflow.model.impl.LogRecord;
import de.soco.software.simuspace.workflow.processing.WFExecutionManager;

/**
 * The Class RunSchemeOptimizationThread.
 */
@Log4j2
public class RunSchemeOptimizationThread implements Runnable {

    /**
     * The Constant OPERATION.
     */
    private static final String OPERATION = "Optimization";

    /**
     * The optimization process.
     */
    private Process optimizationProcess;

    /**
     * The file objective variables.
     */
    private final File fileObjectiveVariables;

    /**
     * The optimization python file path.
     */
    private final String optimizationPythonFilePath;

    /**
     * The options.
     */
    private final String options;

    /**
     * The execution manager.
     */
    private final WFExecutionManager executionManager;

    /**
     * The saved master job.
     */
    private final Job savedMasterJob;

    /**
     * The goals.
     */
    private final String goals;

    private final UserTokenManager tokenManager;

    /**
     * Instantiates a new run scheme optimization thread.
     *
     * @param fileObjectiveVariables
     *         the fileObjectiveVariables
     * @param optimizationPythonFilePath
     *         the optimizationPythonFilePath
     * @param options
     *         the options
     * @param executionManager
     *         the execution manager
     * @param tokenManager
     *         the token manager
     * @param savedMasterJob
     *         the saved master job
     * @param goals
     *         the goals
     */
    public RunSchemeOptimizationThread( File fileObjectiveVariables, String optimizationPythonFilePath, String options,
            WFExecutionManager executionManager, UserTokenManager tokenManager, Job savedMasterJob, String goals ) {
        super();
        this.fileObjectiveVariables = fileObjectiveVariables;
        this.optimizationPythonFilePath = optimizationPythonFilePath;
        this.options = options;
        this.executionManager = executionManager;
        this.savedMasterJob = savedMasterJob;
        this.goals = goals;
        this.tokenManager = tokenManager;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        callPythonOptimizationFile( fileObjectiveVariables, optimizationPythonFilePath, options, goals );
    }

    /**
     * Call python optimization file.
     *
     * @param fileObjectiveVariables
     *         the file objective variables
     * @param optimizationPythonFilePath
     *         the optimization python file path
     * @param options
     *         the options
     * @param goals
     *         the goals
     */
    public void callPythonOptimizationFile( File fileObjectiveVariables, String optimizationPythonFilePath, String options, String goals ) {
        try {
            List< String > cmd;
            if ( PropertiesManager.isImpersonated() ) {
                String[] sudoCmd = PropertiesManager.getSudoCommandConfigurations();
                cmd = java.util.Arrays.asList( sudoCmd[ 0 ], sudoCmd[ 1 ], savedMasterJob.getCreatedBy().getUserUid(),
                        PropertiesManager.getPythonExecutionPathOnServer(), optimizationPythonFilePath,
                        fileObjectiveVariables.getAbsolutePath(), goals, options, savedMasterJob.getId().toString() );
            } else {

                cmd = java.util.Arrays.asList( PropertiesManager.getPythonExecutionPathOnServer(), optimizationPythonFilePath,
                        fileObjectiveVariables.getAbsolutePath(), goals, options, savedMasterJob.getId().toString() );
            }
            runCommand( cmd, ConstantsString.COMMAND_KARAF_LOGGING_ON );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, e.getClass() );
        }
    }

    /**
     * Run command.
     *
     * @param command
     *         the command
     * @param karafLogging
     *         the karaf logging
     */
    private void runCommand( List< String > command, boolean karafLogging ) {
        final ProcessBuilder probuilder = new ProcessBuilder( command );
        try {
            final Process process = probuilder.start();
            optimizationProcess = process;
            ProcessResult processResult = new ProcessResult();
            processResult.setCommand( command );
            if ( karafLogging ) {
                log.info( String.format( "***Started %s with command :%s", OPERATION, probuilder.command() ) );
            }

            final StringBuilder builder = new StringBuilder();
            String line;

            try ( InputStreamReader is = new InputStreamReader( process.getInputStream() );
                    final BufferedReader reader = new BufferedReader( is ) ) {
                while ( ( line = reader.readLine() ) != null ) {
                    builder.append( line );
                    builder.append( System.getProperty( "line.separator" ) );
                }
            }

            List< LogRecord > jobLog = savedMasterJob.getLog();
            final String result = builder.toString();
            if ( !result.isEmpty() ) {
                jobLog.add( new LogRecord( ConstantsMessageTypes.INFO, result, new Date() ) );
            }
            String err = null;
            try {
                err = LinuxUtils.stdError( process );
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
            }

            process.waitFor();
            processResult.setErrorStreamString( err );
            processResult.setCommand( command );
            processResult.setOutputString( result );
            processResult.setOperation( OPERATION );
            processResult.setPid( String.valueOf( process.pid() ) );
            processResult.setExitValue( process.exitValue() );
            if ( karafLogging ) {
                processResult.logProcessExitStatus();
            }
            if ( err != null && !err.isEmpty() ) {
                if ( process.exitValue() != 0 ) {
                    jobLog.add( new LogRecord( ConstantsMessageTypes.ERROR, err, new Date() ) );
                    savedMasterJob.setLog( jobLog );
                    savedMasterJob.setStatus( new Status( WorkflowStatus.FAILED ) );
                    executionManager.updateJobLog( savedMasterJob );
                    executionManager.updateJob( savedMasterJob );
                } else {
                    jobLog.add( new LogRecord( ConstantsMessageTypes.WARNING, err, new Date() ) );
                    savedMasterJob.setLog( jobLog );
                    executionManager.updateJobLog( savedMasterJob );
                }
            } else {
                savedMasterJob.setLog( jobLog );
                executionManager.updateJobLog( savedMasterJob );
            }
            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();

        } catch ( InterruptedException e ) {
            log.warn( MessageBundleFactory.getMessage( Messages.THREAD_INTERRUPTED_WARNING.getKey() ), e );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
        } finally {
            if ( savedMasterJob.getPostprocess().getPostProcessWorkflow().getId().isEmpty() ) {
                tokenManager.expireJobToken( savedMasterJob.getRequestHeaders().getJobAuthToken() );
            }
        }
    }

    /**
     * Stopping optimization process.
     */
    public void stop() {
        if ( optimizationProcess != null ) {
            optimizationProcess.destroy();
        }
    }

}
