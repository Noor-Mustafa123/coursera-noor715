package de.soco.software.simuspace.suscore.common.model;

import java.util.Arrays;

import lombok.Builder;
import lombok.Getter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;

/**
 * The type Command builder.
 */
@Builder
@Getter
public class CommandBuilder {

    /**
     * The Is impersonated.
     */
    private boolean isImpersonated;

    /**
     * The Command.
     */
    private String[] command;

    /**
     * The User uid.
     */
    private String userUID;

    /**
     * The Operation.
     */
    private String operation;

    /**
     * The Engine path.
     */
    private String enginePath;

    /**
     * The Server dir or file path.
     */
    private String serverDirOrFilePath;

    /**
     * The Src path.
     */
    private String srcPath;

    /**
     * The Action.
     */
    private String action;

    /**
     * The Temp path.
     */
    private String tempPath;

    /**
     * The Dest path.
     */
    private String destPath;

    /**
     * The Execution command.
     */
    private String executionCommand;

    /**
     * The Cb 2 param file.
     */
    private String cb2ParamFile;

    /**
     * The Java path.
     */
    private String javaPath;

    /**
     * The Process id.
     */
    private String processId;

    /**
     * The Process flag.
     */
    private String processFlag;

    /**
     * The Work flow path.
     */
    private String workFlowPath;

    /**
     * The Execution host address.
     */
    private String executionHostAddress;

    /**
     * The Engine script file.
     */
    private String engineScriptFile;

    /**
     * The Karaf path.
     */
    private String karafPath;

    /**
     * The Python path.
     */
    private String pythonPath;

    /**
     * The constant SUDO_COMMAND_LENGTH.
     */
    private static final int SUDO_COMMAND_LENGTH = PropertiesManager.getSudoCommandConfigurations().length;

    /**
     * Build command string [ ].
     *
     * @return the string [ ]
     */
    public String[] buildCommand() {

        if ( userUID != null ) {
            setUserUid( isImpersonated, command, userUID );
        }
        if ( operation != null ) {
            setOperation( isImpersonated, command, operation );
        }
        if ( enginePath != null ) {
            setEnginePath( isImpersonated, command, enginePath );
        }
        if ( srcPath != null ) {
            setSrcPath( isImpersonated, command, srcPath );
        }
        if ( serverDirOrFilePath != null ) {
            setServerDirOrFilePath( isImpersonated, command, serverDirOrFilePath );
        }
        if ( action != null ) {
            setAction( isImpersonated, command, action );
        }
        if ( tempPath != null ) {
            setTempPath( isImpersonated, command, tempPath );
        }
        if ( destPath != null ) {
            setDestPath( isImpersonated, command, destPath );
        }
        if ( executionCommand != null ) {
            setExecutionCommand( isImpersonated, command, executionCommand );
        }
        if ( cb2ParamFile != null ) {
            setCb2ParamFile( isImpersonated, command, cb2ParamFile );
        }
        if ( javaPath != null ) {
            setJavaPath( isImpersonated, command, javaPath );
        }
        if ( processId != null ) {
            setProcessId( isImpersonated, command, processId );
        }
        if ( processFlag != null ) {
            setProcessFlag( isImpersonated, command, processFlag );
        }
        if ( workFlowPath != null ) {
            setWorkFlowPath( isImpersonated, command, workFlowPath );
        }
        if ( executionHostAddress != null ) {
            setExecutionHostAddress( isImpersonated, command, executionHostAddress );
        }
        if ( engineScriptFile != null ) {
            setEngineScriptFile( isImpersonated, command, engineScriptFile );
        }
        if ( karafPath != null ) {
            setKarafPath( isImpersonated, command, karafPath );
        }
        if ( pythonPath != null ) {
            setPythonPath( isImpersonated, command, pythonPath );
        }

        return command;
    }

    /**
     * Prepare command string [ ].
     *
     * @param size
     *         the size
     *
     * @return the string [ ]
     */
    public static String[] prepareCommand( int size ) {
        String[] command = new String[ size ];
        Arrays.fill( command, ConstantsString.EMPTY_STRING );
        return command;
    }

    /**
     * Gets operation.
     *
     * @param command
     *         the command
     *
     * @return the operation
     */
    public static String getOperation( String[] command ) {
        if ( command[ 0 ].equals( "sudo" ) ) {
            return command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FIVE ];
        } else {
            return command[ ConstantsInteger.INTEGER_VALUE_FOUR ];
        }
    }

    /**
     * Sets user uid.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param userUid
     *         the user uid
     */
    private static void setUserUid( boolean isImpersonated, String[] command, String userUid ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ZERO ] = userUid;
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ELEVEN ] = userUid;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TEN ] = userUid;
        }
    }

    /**
     * Sets operation.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param operation
     *         the operation
     */
    private static void setOperation( boolean isImpersonated, String[] command, String operation ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FIVE ] = operation;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FOUR ] = operation;
        }
    }

    /**
     * Sets engine path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param enginePath
     *         the engine path
     */
    private static void setEnginePath( boolean isImpersonated, String[] command, String enginePath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = enginePath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = enginePath;
        }
    }

    /**
     * Sets src path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param path
     *         the path
     */
    private static void setSrcPath( boolean isImpersonated, String[] command, String path ) {
        setServerDirOrFilePath( isImpersonated, command, path );
    }

    /**
     * Sets server dir or file path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param serverDirOrFilePath
     *         the server dir or file path
     */
    private static void setServerDirOrFilePath( boolean isImpersonated, String[] command, String serverDirOrFilePath ) {
        String replaced = serverDirOrFilePath.contains( ConstantsString.SPACE ) ? serverDirOrFilePath.replace( ConstantsString.SPACE,
                ConstantsString.SPACE_ALTERNATE ) : serverDirOrFilePath;
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = replaced;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = replaced;
        }
    }

    /**
     * Sets action.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param action
     *         the action
     */
    private static void setAction( boolean isImpersonated, String[] command, String action ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_NINE ] = action;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_EIGHT ] = action;
        }
    }

    /**
     * Sets dest path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param destPath
     *         the dest path
     */
    private static void setDestPath( boolean isImpersonated, String[] command, String destPath ) {
        setFileData( isImpersonated, command, destPath );
    }

    /**
     * Sets file data.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param fileData
     *         the file data
     */
    private static void setFileData( boolean isImpersonated, String[] command, String fileData ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_EIGHT ] = fileData;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SEVEN ] = fileData;
        }
    }

    /**
     * Sets temp path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param tempPath
     *         the temp path
     */
    private static void setTempPath( boolean isImpersonated, String[] command, String tempPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TEN ] = tempPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_NINE ] = tempPath;
        }
    }

    /**
     * Sets execution command.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param executionCommand
     *         the execution command
     */
    private static void setExecutionCommand( boolean isImpersonated, String[] command, String executionCommand ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FOURTEEN ] = executionCommand;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_THIRTEEN ] = executionCommand;
        }
    }

    /**
     * Sets cb 2 param file.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param cb2ParamFile
     *         the cb 2 param file
     */
    private void setCb2ParamFile( boolean isImpersonated, String[] command, String cb2ParamFile ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = cb2ParamFile;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = cb2ParamFile;
        }
    }

    /**
     * Sets java path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param javaPath
     *         the java path
     */
    private void setJavaPath( boolean isImpersonated, String[] command, String javaPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TWO ] = javaPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ONE ] = javaPath;
        }
    }

    /**
     * Sets process id.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param processId
     *         the process id
     */
    private void setProcessId( boolean isImpersonated, String[] command, String processId ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = processId;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = processId;
        }
    }

    /**
     * Sets process flag.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param processFlag
     *         the process flag
     */
    private void setProcessFlag( boolean isImpersonated, String[] command, String processFlag ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_NINE ] = processFlag;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_EIGHT ] = processFlag;
        }
    }

    /**
     * Sets work flow path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param workFlowPath
     *         the work flow path
     */
    private void setWorkFlowPath( boolean isImpersonated, String[] command, String workFlowPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SEVEN ] = workFlowPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_SIX ] = workFlowPath;
        }
    }

    /**
     * Sets execution host address.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param address
     *         the address
     */
    private void setExecutionHostAddress( boolean isImpersonated, String[] command, String address ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_THIRTEEN ] = address;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TWELVE ] = address;
        }
    }

    /**
     * Sets engine script file.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param scriptFile
     *         the script file
     */
    private void setEngineScriptFile( boolean isImpersonated, String[] command, String scriptFile ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_ONE ] = scriptFile;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ZERO ] = scriptFile;
        }
    }

    /**
     * Sets karaf path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param karafPath
     *         the karaf path
     */
    private void setKarafPath( boolean isImpersonated, String[] command, String karafPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_THREE ] = karafPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_TWO ] = karafPath;
        }
    }

    /**
     * Sets python path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param pythonPath
     *         the python path
     */
    private void setPythonPath( boolean isImpersonated, String[] command, String pythonPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_FOUR ] = pythonPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_THREE ] = pythonPath;
        }
    }

    /**
     * Sets script path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param scriptPath
     *         the workflow path
     */
    public static void setScriptPath( boolean isImpersonated, String[] command, String scriptPath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = scriptPath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = scriptPath;
        }
    }

    /**
     * Sets work flow path.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param treePath
     *         the tree path
     */
    public static void setCB2TreePath( boolean isImpersonated, String[] command, String treePath ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_SIX ] = treePath;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_FIVE ] = treePath;
        }
    }

    /**
     * Sets cb 2 oid.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param oid
     *         the oid
     */
    public static void setCB2Oid( boolean isImpersonated, String[] command, String oid ) {
        setServerDirOrFilePath( isImpersonated, command, oid );
    }

    /**
     * Sets cb 2 object type.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param objectType
     *         the object type
     */
    public static void setCB2ObjectType( boolean isImpersonated, String[] command, String objectType ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TEN ] = objectType;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_NINE ] = objectType;
        }
    }

    /**
     * Sets password.
     *
     * @param isImpersonated
     *         the is impersonated
     * @param command
     *         the command
     * @param password
     *         the password
     */
    public static void setPassword( boolean isImpersonated, String[] command, String password ) {
        if ( isImpersonated ) {
            command[ SUDO_COMMAND_LENGTH + ConstantsInteger.INTEGER_VALUE_TWELVE ] = password;
        } else {
            command[ ConstantsInteger.INTEGER_VALUE_ELEVEN ] = password;
        }
    }

}