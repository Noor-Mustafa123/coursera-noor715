package de.soco.software.simuspace.suscore.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLinuxScriptOperations;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Cb2OperationEnum;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.CB2InputParamDTO;
import de.soco.software.simuspace.suscore.common.model.CommandBuilder;
import de.soco.software.simuspace.suscore.common.model.ProcessResult;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The Class PythonUtils.
 *
 * @author noman arshad
 * @since 2.0
 */
@Log4j2
public class PythonUtils {

    /**
     * Instantiates a new Python utils.
     */
    private PythonUtils() {
    }

    /**
     * CB 2 login by python.
     *
     * @param uid
     *         the uid
     * @param password
     *         the password
     * @param action
     *         the action
     * @param refreshToken
     *         the refreshToken to accommodate for the rest login
     *
     * @return the command result
     */
    public static ProcessResult CB2LoginByPython( String uid, String password, String action, String refreshToken ) {
        ProcessResult processResult = new ProcessResult();

        try {
            log.info( "CB2LoginByPython  method called" );
            var paramFile = prepareParamInputFileForCB2(
                    prepareParamObjectCB2( ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, action,
                            ConstantsString.EMPTY_STRING, uid, password,
                            String.join( ConstantsString.COLON, PropertiesManager.cb2Ip(), String.valueOf( PropertiesManager.cb2Port() ) ),
                            ConstantsString.EMPTY_STRING, refreshToken, PropertiesManager.refreshTokenEndpoint(),
                            PropertiesManager.redirectUrlCB2() ) );
            processResult = LinuxUtils.loginLogoutCB2WithImpersonation( uid, paramFile );
            Files.deleteIfExists( Path.of( paramFile ) );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Cb 2 login by python using oidc token.
     *
     * @param oidcToken
     *         the oidc token
     * @param uid
     *         the uid
     * @param action
     *         the action
     *
     * @return the process result
     */
    public static ProcessResult CB2LoginByPythonUsingOIDCToken( String oidcToken, String uid, String action ) {
        ProcessResult processResult = new ProcessResult();
        try {
            var paramFile = prepareParamInputFileForCB2(
                    prepareParamObjectCB2( ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, action,
                            ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING,
                            String.join( ConstantsString.COLON, PropertiesManager.cb2Ip(), String.valueOf( PropertiesManager.cb2Port() ) ),
                            oidcToken, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING ) );
            processResult = LinuxUtils.loginLogoutCB2WithImpersonation( uid, paramFile );
            Files.deleteIfExists( Path.of( paramFile ) );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Prepare param object cb 2 cb 2 input param dto.
     *
     * @param treePath
     *         the tree path
     * @param oids
     *         the oids
     * @param destPath
     *         the dest path
     * @param action
     *         the action
     * @param objectType
     *         the object type
     * @param uid
     *         the uid
     * @param password
     *         the password
     * @param cb2Server
     *         the cb 2 server
     * @param oidcToken
     *         the oidc token
     * @param refreshToken
     *         the refresh token
     * @param refreshTokenUrl
     *         the refresh token url
     * @param redirectUri
     *         the redirect uri
     *
     * @return the cb 2 input param dto
     */
    private static CB2InputParamDTO prepareParamObjectCB2( String treePath, String oids, String destPath, String action, String objectType,
            String uid, String password, String cb2Server, String oidcToken, String refreshToken, String refreshTokenUrl,
            String redirectUri ) {
        return new CB2InputParamDTO( treePath, oids, destPath, action, objectType, uid, password, cb2Server, oidcToken, refreshToken,
                refreshTokenUrl, redirectUri );
    }

    /**
     * Prepare param input file for cb 2 string.
     *
     * @param inputParam
     *         the input param
     *
     * @return the string
     */
    public static String prepareParamInputFileForCB2( CB2InputParamDTO inputParam ) {
        Path inputFilePath = Paths.get( PropertiesManager.getDefaultServerTempPath() + File.separator + UUID.randomUUID() );
        return FileUtils.writeInputFile( inputParam, inputFilePath );
    }

    /**
     * CB 2 tree by python.
     *
     * @param uid
     *         the uid
     * @param password
     *         the password
     * @param refreshToken
     *         the refresh token
     * @param inputFile
     *         the input file
     *
     * @return the command result
     */
    public static ProcessResult CB2TreeByPython( String uid, String password, String refreshToken, String inputFile ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "CB2TreeByPython  method called" );
            var paramFile = prepareParamInputFileForCB2(
                    prepareParamObjectCB2( inputFile, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING,
                            String.valueOf( Cb2OperationEnum.CB2_TREE.getKey() ), ConstantsString.EMPTY_STRING, uid, password,
                            String.join( ConstantsString.COLON, PropertiesManager.cb2Ip(), String.valueOf( PropertiesManager.cb2Port() ) ),
                            ConstantsString.EMPTY_STRING, refreshToken, PropertiesManager.refreshTokenEndpoint(),
                            PropertiesManager.redirectUrlCB2() ) );
            processResult = LinuxUtils.treeCB2WithImpersonation( uid, paramFile );
            Files.deleteIfExists( Path.of( paramFile ) );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call python clean logs.
     *
     * @param cleanLogsFile
     *         the clean logs file
     * @param solver
     *         the solver
     * @param dateRangeFlag
     *         the date range flag
     * @param genCsvPath
     *         the gen csv path
     * @param total
     *         the total
     *
     * @return the command result
     */
    public static ProcessResult callPythonCleanLogs( File cleanLogsFile, String solver, String dateRangeFlag, String genCsvPath,
            String total ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callPythonCleanLogs  method called" );
            String[] command = new String[ 10 ];
            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = cleanLogsFile.getAbsolutePath();
            command[ 2 ] = "-t";
            command[ 3 ] = solver;
            command[ 4 ] = "-fr";
            command[ 5 ] = dateRangeFlag;
            command[ 6 ] = "-o";
            command[ 7 ] = genCsvPath;
            command[ 8 ] = "--tot";
            command[ 9 ] = total;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            String[] scriptCommand = CommandBuilder.prepareCommand( 14 );
            LinuxUtils.addCommonCommandProperties( false, scriptCommand );
            String[] finalCommand = CommandBuilder.builder().isImpersonated( false ).command( scriptCommand )
                    .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).javaPath( ConstantsString.EMPTY_STRING )
                    .executionCommand( builder.toString() ).build().buildCommand();

            processResult = LinuxUtils.runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call python vmcl logs.
     *
     * @param vmclLogsFile
     *         the vmcl logs file
     * @param solver
     *         the solver
     * @param dateRangeFlag
     *         the date range flag
     * @param genCsvPath
     *         the gen csv path
     *
     * @return the command result
     */
    public static ProcessResult callPythonVmclLogs( File vmclLogsFile, String solver, String dateRangeFlag, String genCsvPath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callPythonVmclLogs  method called" );
            String[] command = new String[ 8 ];
            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = vmclLogsFile.getAbsolutePath();
            command[ 2 ] = "-t";
            command[ 3 ] = solver;
            command[ 4 ] = "-fr";
            command[ 5 ] = dateRangeFlag;
            command[ 6 ] = "-o";
            command[ 7 ] = genCsvPath;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            String[] scriptCommand = CommandBuilder.prepareCommand( 14 );
            LinuxUtils.addCommonCommandProperties( false, scriptCommand );

            String[] finalCommand = CommandBuilder.builder().isImpersonated( false ).command( scriptCommand )
                    .operation( ConstantsLinuxScriptOperations.RUN_COMMAND ).javaPath( ConstantsString.EMPTY_STRING )
                    .executionCommand( builder.toString() ).build().buildCommand();

            processResult = LinuxUtils.runScript( true, ConstantsString.COMMAND_KARAF_LOGGING_ON, null, finalCommand );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call parser update python file.
     *
     * @param userUID
     *         the user UID
     * @param inputFile
     *         the input file
     * @param outputFilePath
     *         the output file path
     * @param parserParsser
     *         the parser parsser
     * @param templateFilePath
     *         the template file path
     *
     * @return the command result
     */
    public static ProcessResult callParserUpdatePythonFile( String userUID, String inputFile, String outputFilePath, String parserParsser,
            String templateFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callParserUpdatePythonFile  method called" );
            String[] command = new String[ 8 ];

            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = parserParsser;
            command[ 2 ] = "-i";
            command[ 3 ] = inputFile.contains( ConstantsString.SPACE ) ? inputFile.replace( ConstantsString.SPACE,
                    ConstantsString.SPACE_ALTERNATE ) : inputFile;
            command[ 4 ] = "-o";
            command[ 5 ] = outputFilePath;
            command[ 6 ] = "-t";
            command[ 7 ] = templateFilePath;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.callParserPythonScript( userUID, builder.toString() );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call parser extraction python file.
     *
     * @param userUID
     *         the user UID
     * @param inputFilePath
     *         the input file path
     * @param outputFilePath
     *         the output file path
     * @param parserParsser
     *         the parser parsser
     *
     * @return the command result
     */
    public static ProcessResult callParserExtractionPythonFile( String userUID, String inputFilePath, String outputFilePath,
            String parserParsser ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callParserExtractionPythonFile  method called" );
            String[] command = new String[ 6 ];

            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = parserParsser;
            command[ 2 ] = "-i";
            command[ 3 ] = inputFilePath.contains( ConstantsString.SPACE ) ? inputFilePath.replace( ConstantsString.SPACE,
                    ConstantsString.SPACE_ALTERNATE ) : inputFilePath;
            command[ 4 ] = "-o";
            command[ 5 ] = outputFilePath;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.callParserPythonScript( userUID, builder.toString() );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call vmcl python script command result.
     *
     * @param scriptPath
     *         the script path
     * @param inputPath
     *         the output path
     *
     * @return the command result
     */
    public static ProcessResult callOverviewScript( String scriptPath, String inputPath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callOverviewScript  method called" );
            StringBuilder builder = new StringBuilder();
            builder.append( PropertiesManager.getPythonExecutionPathOnServer() ).append( " " );
            builder.append( scriptPath ).append( " " );
            builder.append( "-i" ).append( " " );
            builder.append( inputPath );
            processResult = LinuxUtils.runSystemCommand( builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call generate input python file.
     *
     * @param userUID
     *         the user UID
     * @param pythonPath
     *         the python path
     * @param inputFilePath
     *         the input file path
     * @param json
     *         the json
     *
     * @return the command result
     */
    public static ProcessResult callGenerateInputPythonFile( String userUID, String pythonPath, String inputFilePath, String json ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callGenerateInputPythonFile  method called" );
            String[] command = new String[ 4 ];

            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = pythonPath;
            command[ 2 ] = inputFilePath;
            command[ 3 ] = json;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.runUserCommand( userUID, builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call generate qadyna combinations process result.
     *
     * @param pythonPath
     *         the python path
     * @param inputFilePath
     *         the input file path
     * @param outputFile
     *         the output file
     *
     * @return the process result
     */
    public static ProcessResult callGenerateQADYNACombinations( String pythonPath, String inputFilePath, String outputFile ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callGenerateQADYNACombinations  method called" );
            String[] command = new String[ 4 ];

            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = pythonPath;
            command[ 2 ] = inputFilePath;
            command[ 3 ] = outputFile;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.runSystemCommand( builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call run qadyna process result.
     *
     * @param userUID
     *         the user uid
     * @param pythonPath
     *         the python path
     * @param inputFilePath
     *         the input file path
     *
     * @return the process result
     */
    public static ProcessResult callRunQADYNA( String userUID, String pythonPath, String inputFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callRunQADYNA method called" );
            String[] command = new String[ 3 ];

            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = pythonPath;
            command[ 2 ] = inputFilePath;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.runUserCommand( userUID, builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call filter output.
     *
     * @param userUID
     *         the user UID
     * @param pythonPath
     *         the python path
     * @param flag
     *         the flag
     * @param expression
     *         the input file path
     * @param json
     *         the json
     *
     * @return the command result
     */
    public static ProcessResult callFilterOutput( String userUID, String pythonPath, String flag, String expression, String json ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callFilterOutput  method called" );
            String[] command = new String[ 5 ];
            command[ 0 ] = PropertiesManager.getPythonExecutionPathOnServer();
            command[ 1 ] = pythonPath;
            command[ 2 ] = flag;
            command[ 3 ] = json;
            command[ 4 ] = expression;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.runUserCommand( userUID, builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Gets the HCPL ist by each user.
     *
     * @param userUID
     *         the user UID
     *
     * @return the HCPL ist by each user
     */
    public static ProcessResult getHCPLIstByEachUser( String userUID ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "getHCPLIstByEachUser  method called" );
            String[] command = new String[ 1 ];

            command[ 0 ] = PropertiesManager.getHpcCostCenterCommand() + " " + userUID;

            final StringBuilder builder = new StringBuilder();
            for ( int i = 0; i < command.length; i++ ) {
                String unitCommand = command[ i ];
                builder.append( unitCommand );
                if ( i != ( command.length - 1 ) ) {
                    builder.append( " " );
                }
            }
            processResult = LinuxUtils.getSGEListByUser( userUID, builder.toString() );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Download CB 2 object file by CB 2 oid.
     *
     * @param uid
     *         the uid
     * @param oidCB2
     *         the oid CB 2
     * @param destinationForFileDownload
     *         the destination for file download
     * @param cb2ObjectType
     *         the cb 2 object type
     *
     * @return the command result
     */
    public static ProcessResult downloadCB2ObjectFileByCB2Oid( String uid, String oidCB2, String destinationForFileDownload,
            String cb2ObjectType ) {
        var paramFile = prepareParamInputFileForCB2(
                prepareParamObjectCB2( ConstantsString.EMPTY_STRING, oidCB2, destinationForFileDownload,
                        String.valueOf( Cb2OperationEnum.CB2_FILE_DOWNLOAD.getKey() ), cb2ObjectType, uid, "pass",
                        ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING,
                        ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING ) );
        String[] scriptCommand = LinuxUtils.addSudoCredential();
        LinuxUtils.addCommonCommandProperties( true, scriptCommand );

        String[] finalCommand = CommandBuilder.builder().isImpersonated( true ).command( scriptCommand ).cb2ParamFile( paramFile )
                .operation( ConstantsLinuxScriptOperations.CB2 ).enginePath( PropertiesManager.getEnginePath() )
                .javaPath( ConstantsString.EMPTY_STRING ).build().buildCommand();

        ProcessResult result = LinuxUtils.runScript( true, true, null, finalCommand );

        try {
            Files.deleteIfExists( Path.of( paramFile ) );
        } catch ( IOException e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return result;
    }

    /**
     * Call python hpc_sge_uge file.
     *
     * @param hpcPythonFilePath
     *         the hpc python file path
     *
     * @return the command result
     */
    public static ProcessResult callPythonHpcSgeUgeFile( String hpcPythonFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callPythonHpcSgeUgeFile  method called" );
            String[] cmd = new String[]{ PropertiesManager.getPythonExecutionPathOnServer(), hpcPythonFilePath };
            processResult = LinuxUtils.runCommand( cmd, ConstantsString.COMMAND_KARAF_LOGGING_ON, null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call python hpc_sge_uge file.
     *
     * @param hpcPythonFilePath
     *         the hpc python file path
     * @param arguments
     *         the arguments
     * @param userName
     *         the user name
     *
     * @return the command result
     */
    public static ProcessResult callHpcJobMonitoringFile( String hpcPythonFilePath, List< String > arguments, String userName ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callHpcJobMonitoringFile  method called" );
            String[] cmd = new String[ arguments.size() + 2 ];
            List< String > completeCommand = new ArrayList<>();
            completeCommand.add( PropertiesManager.getPythonExecutionPathOnServer() );
            completeCommand.add( hpcPythonFilePath );
            completeCommand.addAll( arguments );
            completeCommand.toArray( cmd );
            String commandString = String.join( " ", cmd );
            processResult = LinuxUtils.runUserCommand( userName, commandString, null );

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call hpc stats file process result.
     *
     * @param statsScript
     *         the stats script
     * @param userName
     *         the user name
     * @param solver
     *         the solver
     * @param chartType
     *         the chart type
     * @param dateRangeFlag
     *         the date range flag
     * @param outputFilePath
     *         the output file path
     *
     * @return the process result
     */
    public static ProcessResult callHpcStatsFile( String statsScript, String userName, String solver, String chartType,
            String dateRangeFlag, String outputFilePath ) {
        ProcessResult processResult = new ProcessResult();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append( PropertiesManager.getPythonExecutionPathOnServer() ).append( " " );
            builder.append( statsScript ).append( " " );
            builder.append( "-s" ).append( " " );
            builder.append( solver ).append( " " );
            builder.append( "-t" ).append( " " );
            builder.append( chartType ).append( " " );
            builder.append( "-fr" ).append( " " );
            builder.append( dateRangeFlag ).append( " " );
            builder.append( "-o" ).append( " " );
            builder.append( outputFilePath );
            processResult = LinuxUtils.runUserCommand( userName, builder.toString(), null );

        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call project dashboard script command result.
     *
     * @param scriptPath
     *         the script path
     * @param inputFilePath
     *         the input file path
     * @param outputFilePath
     *         the output file path
     * @param maxDepth
     *         the max depth
     * @param columnNumber
     *         the column number
     *
     * @return the command result
     */
    public static ProcessResult callProjectDashboardScript( String scriptPath, String inputFilePath, String outputFilePath, String maxDepth,
            String columnNumber ) {
        ProcessResult processResult = new ProcessResult();
        try {
            log.info( "callProjectDashboardScript  method called" );
            StringBuilder builder = new StringBuilder();
            builder.append( PropertiesManager.getPythonExecutionPathOnServer() ).append( " " );
            builder.append( scriptPath ).append( " " );
            builder.append( "-i" ).append( " " );
            builder.append( inputFilePath ).append( " " );
            builder.append( "-o" ).append( " " );
            builder.append( outputFilePath ).append( " " );
            builder.append( "-d" ).append( " " );
            builder.append( maxDepth ).append( " " );
            builder.append( "-c" ).append( " " );
            builder.append( columnNumber );
            processResult = LinuxUtils.runSystemCommand( builder.toString(), null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Call pst planning script process result.
     *
     * @param sriptPath
     *         the sript path
     * @param arguments
     *         the arguments
     *
     * @return the process result
     */
    public static ProcessResult callPstPlanningScript( String sriptPath, String arguments ) {
        ProcessResult processResult = new ProcessResult();
        try {
            String command = String.format( "%s %s %s", PropertiesManager.getPythonExecutionPathOnServer(), sriptPath, arguments );
            processResult = LinuxUtils.runSystemCommand( command, null );
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, PythonUtils.class );
        }
        return processResult;
    }

    /**
     * Gets logs directory base path.
     *
     * @return the logs directory base path
     */
    public static String getLogsDirectoryBasePath() {
        var dirPath = Path.of( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.MISC_BASE_PATH ) + "/py_logs" );
        try {
            if ( Files.notExists( dirPath ) ) {
                Files.createDirectory( dirPath );
                FileUtils.setGlobalAllFilePermissions( dirPath );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.DIR_NOT_CREATED.getKey() ) + " " + dirPath );
        }
        return dirPath.toAbsolutePath().toString();
    }

    /**
     * Gets central python cache path.
     *
     * @return the central python cache path
     */
    public static String getCentralPythonCachePath() {
        Path dirPath = Path.of( PropertiesManager.getInstance().getProperty( ConstantsFileProperties.MISC_BASE_PATH ) + "/py_cache" );
        try {
            if ( Files.notExists( dirPath ) ) {
                Files.createDirectory( dirPath );
                FileUtils.setGlobalAllFilePermissions( dirPath );
            }
        } catch ( IOException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( MessageBundleFactory.getMessage( Messages.FAILED_TO_CREATE_DIRECTORY_AT.getKey(),
                    dirPath.toAbsolutePath().toString() ) );
        }
        return dirPath.toAbsolutePath().toString();
    }

}
